package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.gmis.mybatis.domain.workcircle.WorkCircle;
import cn.ffcs.gmis.workcircle.service.WorkCircleService;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 延平区(YPQ) 工作流节点跳转决策类
 * 必填参数
 * 		decisionService	指定实现类 workflowYPQDecisionMakingService
 * 		curUserId		当前用户
 * 		createUserId	当前事件的采集人员
 * 		curOrgId		当前用户所属组织
 * 		curNodeCode		当前环节节点编码
 * 		instanceId		流程实例id
 * 非必填参数
 * 		toClose			是否结案归档 1：结案归档；0 未结案归档；默认为0
 * @author zhangls
 *
 */
@Service(value = "workflowYPQDecisionMakingService")
public class WorkflowYPQDecisionMakingServiceImpl extends
		WorkflowGLGridDecisionMakingServiceImpl {
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private WorkCircleService workCircleService;//工作圈接口
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	private static final String DISTRICT_COMMAND_CENTER_NODE_CODE = "task5";//区网格指挥中心受理处理环节
	private static final String DISTRICT_DEPARTMENT_NODE_CODE = "task6";	//区直处置单位办理
	
	private static final String WORK_CIRCLE_WCTYPE_EVENT = "1";//工作圈类型 事件
	private static final String WORK_CIRCLE_OPTYPE_REPORT = "1";//工作圈操作类型 上报
	private static final String WORK_CIRCLE_OPTYPE_ARCHIVE = "4";//工作圈类型 上报并结案
	
	@Override
	public Map<String, Object> makeDecision(Map<String, Object> params) throws Exception {
		String toClose = "0",		//是否结案归档
			   curUserName = "",
			   curOrgCode = "",
			   curOrgName = "";
		Long curUserId = -1L,		//当前用户
			 curOrgId = -1L,		//当前用户所属组织
			 instanceId = -1L;		//流程实例id
		UserBO curUserBO = null;	//当前用户信息
		OrgSocialInfoBO curOrgInfo = null;	//当前组织信息
		Map<String, Object> resultMap = null;
		
		if(CommonFunctions.isNotBlank(params, "curUserId")) {
			try {
				curUserId = Long.valueOf(params.get("curUserId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[curUserId]："+params.get("curUserId")+" 不能转换为Long型！");
			}
			
			if(curUserId > 0) {
				curUserBO = userManageService.getUserInfoByUserId(curUserId);
				if(curUserBO != null) {
					curUserName = curUserBO.getPartyName();
				}
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curUserId]，请检查！");
		}
		if(CommonFunctions.isNotBlank(params, "curOrgId")) {
			try {
				curOrgId = Long.valueOf(params.get("curOrgId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[curOrgId]："+params.get("curOrgId")+" 不能转换为Long型！");
			}
			
			if(curOrgId > 0) {
				curOrgInfo = orgSocialInfoService.findByOrgId(curOrgId);
				if(curOrgInfo != null) {
					curOrgCode = curOrgInfo.getOrgCode();
					curOrgName = curOrgInfo.getOrgName();
				}
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curOrgId]，请检查！");
		}
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			String instanceIdStr = params.get("instanceId").toString();
			try {
				instanceId = Long.valueOf(instanceIdStr);
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[instanceId]："+params.get("instanceId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[instanceId]，请检查！");
		}
		if(CommonFunctions.isNotBlank(params, "toClose")) {
			toClose = params.get("toClose").toString();
		}
		
		resultMap = super.makeDecision(params);//进行决策
		
		String nextNodeCode = "";
		
		if(CommonFunctions.isNotBlank(resultMap, "nextNodeCode")) {
			nextNodeCode = resultMap.get("nextNodeCode").toString();
		}
		
		if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose) || DISTRICT_COMMAND_CENTER_NODE_CODE.equals(nextNodeCode) || DISTRICT_DEPARTMENT_NODE_CODE.equals(nextNodeCode)) {
			WorkCircle workCircle = new WorkCircle();
			workCircle.setWcType(WORK_CIRCLE_WCTYPE_EVENT);
			workCircle.setOpUserId(curUserId);
			workCircle.setOpUserName(curUserName);
			workCircle.setOpDeptCode(curOrgCode);
			workCircle.setOpDeptName(curOrgName);
			
			if(instanceId != null && instanceId > 0) {
				Long eventId = -1L;
				ProInstance pro = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
				
				if(pro != null) {
					eventId = pro.getFormId();
				}
				
				if(eventId > 0) {
					EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
					if(event != null) {
						workCircle.setEventId(eventId);
						workCircle.setEventTitle(event.getEventName());
						workCircle.setEventTime(event.getHappenTime());
						workCircle.setEventAddr(event.getOccurred());
					}
				}
			}
			
			if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)) {//归档
				//添加 上报并结案 工作圈记录
				workCircle.setOpType(WORK_CIRCLE_OPTYPE_ARCHIVE);//上报并结案
				workCircle.setNextUserId(curUserId.toString());
				workCircle.setNextUserName(curUserName);
				workCircle.setNextDeptCode(curOrgCode);
				workCircle.setNextDeptName(curOrgName);
				
			} else if(DISTRICT_COMMAND_CENTER_NODE_CODE.equals(nextNodeCode) || DISTRICT_DEPARTMENT_NODE_CODE.equals(nextNodeCode)) {//添加 上报 工作圈记录
				workCircle.setOpType(WORK_CIRCLE_OPTYPE_REPORT);//上报并结案
				
				if(CommonFunctions.isNotBlank(resultMap, "transactorIds")) {//下一环节办理人员id
					workCircle.setNextUserId(resultMap.get("transactorIds").toString());
				}
				if(CommonFunctions.isNotBlank(resultMap, "transactorOrgIds")) {//下一环节办理人员组织id
					String[] transactorOrgIdArray = resultMap.get("transactorOrgIds").toString().split(",");
					Long orgId = -1L;
					OrgSocialInfoBO orgInfo = null;
					StringBuffer orgCodeBuffer = new StringBuffer(""),
							     orgNameBuffer = new StringBuffer("");
					
					for(String transactorOrgId : transactorOrgIdArray) {
						if(StringUtils.isNotBlank(transactorOrgId)) {
							try {
								orgId = Long.valueOf(transactorOrgId);
							} catch(NumberFormatException e) {
								orgId = -1L;
								e.printStackTrace();
							}
							
							if(orgId > 0) {
								orgInfo = orgSocialInfoService.findByOrgId(orgId);
								if(orgInfo != null) {
									orgCodeBuffer.append(",").append(orgInfo.getOrgCode());
									orgNameBuffer.append(",").append(orgInfo.getOrgName());
								}
							}
						}
					}
					
					if(orgCodeBuffer.length() > 0) {
						workCircle.setNextDeptCode(orgCodeBuffer.substring(1));
						workCircle.setNextDeptName(orgNameBuffer.substring(1));
					}
				}
			}
			
			try {
				workCircleService.insert(workCircle);
			} catch(Exception e) {//防止工作圈异常导致决策失败
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}
}
