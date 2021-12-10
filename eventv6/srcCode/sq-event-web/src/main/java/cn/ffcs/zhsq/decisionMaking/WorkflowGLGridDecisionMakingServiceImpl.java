package cn.ffcs.zhsq.decisionMaking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.NodeTransition;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.handlerCfg.service.IEventHandlerCfgService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.EventNodeHandler;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 鼓楼网格(GLGrid) 工作流节点跳转决策类
 * 必填参数
 * 		decisionService	指定实现类 workflowGLGridDecisionMakingService
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
@Service(value = "workflowGLGridDecisionMakingService")
public class WorkflowGLGridDecisionMakingServiceImpl extends
		ApplicationObjectSupport implements IWorkflowDecisionMakingService<Map<String, Object>> {

	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventHandlerCfgService eventHandlerCfgService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	//决策1
	private static final String DECISION_MAKING_NODE_CODE = "decision1";	//决策1环节节点编码
	private static final String COLLECT_NODE_CODE = "task1";				//采集事件环节节点编码
	private static final String GRID_ADMIN_NODE_CODE = "task2";				//网格员处理环节
	private static final String COMMUNITY_NODE_CODE_COLLECT = "task3";		//社区处理环节
	private static final String STREET_NODE_CODE_COLLECT = "task4";			//街道处理环节
	
	//决策2
	private static final String DECISION_MAKING_NODE_CODE_2 = "decision2";	//决策2环节节点编码
	private static final String DISTRICT_COMMAND_CENTER_NODE_CODE = "task5";//区网格指挥中心受理处理环节
	private static final String DISTRICT_DEPARTMENT_NODE_CODE = "task6";	//区直处置单位办理
	private static final String END_NODE_CODE = "end1";						//归档环节
	
	@Override
	public Map<String, Object> makeDecision(Map<String, Object> params) throws Exception {
		Long curUserId = -1L,		//当前用户
			 createUserId = -1L,	//当前事件的采集人员
			 curOrgId = -1L,		//当前用户所属组织
			 instanceId = -1L;		//流程实例id
		UserBO curUserBO = null;	//当前用户信息
		OrgSocialInfoBO curOrgInfo = null;	//当前组织信息
		String curNodeCode = "",	//当前环节节点编码
			   nextNodeCode = "",	//下一环节节点编码
			   chiefLevl = "",		//当前组织层级
			   toClose = "0";		//是否结案归档
		StringBuffer transactorIds = new StringBuffer(""),
					 transactorOrgIds = new StringBuffer("");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(CommonFunctions.isNotBlank(params, "curUserId")) {
			try {
				curUserId = Long.valueOf(params.get("curUserId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[curUserId]："+params.get("curUserId")+" 不能转换为Long型！");
			}
			
			if(curUserId > 0) {
				curUserBO = userManageService.getUserInfoByUserId(curUserId);
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curUserId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "createUserId")) {
			try {
				createUserId = Long.valueOf(params.get("createUserId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[createUserId]："+params.get("createUserId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[createUserId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curOrgId")) {
			try {
				curOrgId = Long.valueOf(params.get("curOrgId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[curOrgId]："+params.get("curOrgId")+" 不能转换为Long型！");
			}
			
			if(curOrgId > 0) {
				curOrgInfo = orgSocialInfoService.findByOrgId(curOrgId);
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curOrgId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
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
		
		if(curOrgInfo != null) {
			chiefLevl = curOrgInfo.getChiefLevel();
		}
		
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) {//决策1
			if(ConstantValue.GRID_ORG_LEVEL.equals(chiefLevl)) {
				nextNodeCode = GRID_ADMIN_NODE_CODE;
			} else if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(chiefLevl)) {
				nextNodeCode = COMMUNITY_NODE_CODE_COLLECT;
			} else if(ConstantValue.STREET_ORG_LEVEL.equals(chiefLevl)) {
				nextNodeCode = STREET_NODE_CODE_COLLECT;
			}
		} else if(DECISION_MAKING_NODE_CODE_2.equals(curNodeCode) || GRID_ADMIN_NODE_CODE.equals(curNodeCode)
				|| COMMUNITY_NODE_CODE_COLLECT.equals(curNodeCode) || STREET_NODE_CODE_COLLECT.equals(curNodeCode)) {//决策2
			boolean isAutoDistribution = false;
			List<EventNodeHandler> eventUserCfgList = null;
			
			if(instanceId != null && instanceId > 0) {
				ProInstance proInstance = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
				if(proInstance != null) {
					EventDisposal event = eventDisposalService.findEventByIdSimple(proInstance.getFormId());
					if(event != null) {
						eventUserCfgList = eventHandlerCfgService.findEventNodeHandlers(proInstance.getWorkFlowId(), event.getType(), DISTRICT_DEPARTMENT_NODE_CODE, event.getGridCode());
						isAutoDistribution = eventUserCfgList != null && eventUserCfgList.size() > 0;
					}
				}
			}
			
			//该区域该事件类型是否有对应的办理人员配置
			if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)) {//归档
				nextNodeCode = END_NODE_CODE;
			} else if(isAutoDistribution) {//人员自动分配
				nextNodeCode = DISTRICT_DEPARTMENT_NODE_CODE;
				
				if(!DECISION_MAKING_NODE_CODE_2.equals(curNodeCode)) {//非决策节点时，进行人员获取
					Long userId = -1L, userOrgId = -1L;
					
					for(EventNodeHandler cfgUser : eventUserCfgList) {
						userId = cfgUser.getUserId();
						userOrgId = cfgUser.getOrgId();
						
						if(userId != null && userId > 0 && userOrgId != null && userOrgId > 0) {
							transactorIds.append(",").append(userId);
							transactorOrgIds.append(",").append(userOrgId);
						}
					}
					
					if(transactorIds.length() > 0) {
						transactorIds = new StringBuffer(transactorIds.substring(1));
					}
					
					if(transactorOrgIds.length() > 0) {
						transactorOrgIds = new StringBuffer(transactorOrgIds.substring(1));
					}
					
					resultMap.put("transactorIds", transactorIds.toString());//办理人员id，使用英文逗号相连
					resultMap.put("transactorOrgIds", transactorOrgIds.toString());//办理人员组织id，使用英文逗号相连，与transactorIds相对应
				}
			} else {
				nextNodeCode = DISTRICT_COMMAND_CENTER_NODE_CODE;
				
				if(!DECISION_MAKING_NODE_CODE_2.equals(curNodeCode)) {//非决策节点时，进行人员获取
					Node curNode = eventDisposalWorkflowService.findNodeById(instanceId, curNodeCode),
						 decisionNode = eventDisposalWorkflowService.findNodeById(instanceId, DECISION_MAKING_NODE_CODE_2),
						 nextNode = eventDisposalWorkflowService.findNodeById(instanceId, nextNodeCode);
					
					if(curNode != null && decisionNode != null && nextNode != null) {
						String nodeCodeFrom = curNode.getNodeCode(),
							   nodeCodeTo = nextNode.getNodeCode(),
							   operateType = "",
							   nodeCode = "";
						int fromLevel = 0, toLevel = 0;
						
						NodeTransition nodeTransition = eventDisposalWorkflowService.findNodeTransition(decisionNode.getNodeId(), nextNode.getNodeId());
						if(nodeTransition != null) {
							operateType = nodeTransition.getType();
						}
						
						if(StringUtils.isNotBlank(nodeCodeFrom) && nodeCodeFrom.length() > 1) {
							String fromLevelStr = nodeCodeFrom.substring(1, 2);
							
							try {
								fromLevel = Integer.valueOf(fromLevelStr);
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
						}
						
						if(StringUtils.isNotBlank(nodeCodeTo) && nodeCodeTo.length() > 1) {
							String toLevelStr = nodeCodeTo.substring(1, 2);
							
							try {
								toLevel = Integer.valueOf(toLevelStr);
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
						}
						
						//人工构建nodeCode
						nodeCode = nodeCodeFrom + operateType + (Math.abs(fromLevel - toLevel)) + nodeCodeTo;
						//人工构建当前登入用户信息
						UserInfo userInfo = new UserInfo();
						if(curUserBO != null) {
							userInfo.setUserId(curUserBO.getUserId());
							userInfo.setPartyName(curUserBO.getPartyName());
						}
						if(curOrgInfo != null) {
							userInfo.setOrgId(curOrgInfo.getOrgId());
							userInfo.setOrgCode(curOrgInfo.getOrgCode());
							userInfo.setOrgName(curOrgInfo.getOrgName());
						}
						//人工模拟环节选中效果
						Map<String, Object> extraParam = new HashMap<String, Object>();
						ProInstance proInstance = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
						if(proInstance != null) {
							extraParam.put("eventId", proInstance.getFormId());
						}
						
						Map<String, Object> nodeResultMap = fiveKeyElementService.getNodeInfoForEvent(userInfo, curNode.getNodeName(), nextNode.getNodeName(), nodeCode, nextNode.getNodeId().toString(), extraParam);
						
						if(CommonFunctions.isNotBlank(nodeResultMap, "userIds")) {
							resultMap.put("transactorIds", nodeResultMap.get("userIds"));
						}
						if(CommonFunctions.isNotBlank(nodeResultMap, "orgIds")) {
							resultMap.put("transactorOrgIds", nodeResultMap.get("orgIds"));
						}
					}
				}
			}
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		resultMap.put("nextNodeCode", nextNodeCode);
		
		return resultMap;
	}
}
