package cn.ffcs.zhsq.event.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 云南省->楚雄彝族自治州->楚雄市 工作流使用接口实现
 * @ClassName:   EventDisposalWorkflow4YNCXServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年7月2日 上午9:08:43
 */
@Service(value = "eventDisposalWorkflow4YNCXService")
public class EventDisposalWorkflow4YNCXServiceImpl extends EventDisposalWorkflowForEventNewServiceImpl {
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	private static final String CLOSE_NODE_CODE = "task8";	//结案环节节点编码
	private static final String END_NODE_CODE = "end1";	//归档环节
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String instanceId = super.startFlowByWorkFlow(eventId, workFlowId, toClose, userInfo, extraParam);
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(StringUtils.isNotBlank(instanceId) && ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)) {
			extraParam.put("eventId", eventId);
			autoEva(Long.valueOf(instanceId), extraParam);
		}
		
		return instanceId;
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception {
		boolean flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		
		if(flag && CLOSE_NODE_CODE.equals(nextNodeName)) {
			autoEva(instanceId, extraParam);
		}
		
		return flag;
	}
	
	/**
	 * 群众事件 微信小程序上报则自动归档，并生成默认评价信息
	 * @param instanceId	流程实例id
	 * @param extraParam	额外参数
	 * 			eventId		事件id
	 * 			formId		事件id
	 * @return
	 */
	private boolean autoEva(Long instanceId, Map<String, Object> extraParam) {
		String MASS_EVENT_BIZ_PLATFORM = "532301001",//群众事件 微信小程序上报
			   IOT_EVENT_BIZ_PLATFORM = "532301002",//物联网
			   eventBizPlatform = null;
		EventDisposal event = null;
		Long eventId = null;
		boolean flag = false;
		
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(CommonFunctions.isNotBlank(extraParam, "eventId")) {
			eventId = Long.valueOf(extraParam.get("eventId").toString());
		} else if(CommonFunctions.isNotBlank(extraParam, "formId")) {
			eventId = Long.valueOf(extraParam.get("formId").toString());
		} else if(instanceId != null && instanceId > 0) {
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			if(proInstance != null) {
				eventId = proInstance.getFormId();
			}
		}
		
		if(eventId != null && eventId > 0) {
			event = eventDisposalService.findEventByIdSimple(eventId);
			
			if(event != null) {
				eventBizPlatform = event.getBizPlatform();
			}
		}
		
		if(MASS_EVENT_BIZ_PLATFORM.equals(eventBizPlatform) || IOT_EVENT_BIZ_PLATFORM.equals(eventBizPlatform)) {
			Map<String, Object> curNodeData = curNodeData(instanceId);
			String curUserId = null, curOrgId = null, curTaskId = null;
			UserInfo curUserInfo = new UserInfo();
			
			if(CommonFunctions.isNotBlank(curNodeData, "WF_ORGID_ALL")) {
				OrgSocialInfoBO curOrgInfo = null;
				
				curOrgId = curNodeData.get("WF_ORGID_ALL").toString().split(",")[0];
				
				curOrgInfo = orgSocialInfoService.findByOrgId(Long.valueOf(curOrgId));
				
				if(curOrgInfo != null && curOrgInfo.getOrgId() != null) {
					curUserInfo.setOrgId(curOrgInfo.getOrgId());
					curUserInfo.setOrgCode(curOrgInfo.getOrgCode());
					curUserInfo.setOrgName(curOrgInfo.getOrgName());
				}
			}
			if(CommonFunctions.isNotBlank(curNodeData, "WF_USERID_ALL")) {
				UserBO userBO = null;
				
				curUserId = curNodeData.get("WF_USERID_ALL").toString().split(",")[0];
				
				userBO = userManageService.getUserInfoByUserId(Long.valueOf(curUserId));
				
				if(userBO != null && userBO.getUserId() != null) {
					curUserInfo.setUserId(userBO.getUserId());
					curUserInfo.setPartyName(userBO.getPartyName());
					
					if(userBO.getVerifyMobile() != null) {
						curUserInfo.setVerifyMobile(userBO.getVerifyMobile().toString());
					}
				}
			} else if(StringUtils.isNotBlank(curOrgId)) {
				List<UserBO> userBOList = userManageService.getUserListByUserExampleParamsOut(null, null, Long.valueOf(curOrgId));
				
				if(userBOList != null && userBOList.size() > 0) {
					UserBO userBO = userBOList.get(0);
					
					if(userBO != null && userBO.getUserId() != null) {
						curUserInfo.setUserId(userBO.getUserId());
						curUserInfo.setPartyName(userBO.getPartyName());
						
						if(userBO.getVerifyMobile() != null) {
							curUserInfo.setVerifyMobile(userBO.getVerifyMobile().toString());
						}
					}
				}
			}
			if(CommonFunctions.isNotBlank(curNodeData, "WF_DBID_")) {
				curTaskId = curNodeData.get("WF_DBID_").toString();
			}
			
			if(curUserInfo.getUserId() != null && curUserInfo.getOrgId() != null) {
				Map<String, Object> evaMap = new HashMap<String, Object>();
				String advice = "系统自动评价";
				
				evaMap.putAll(extraParam);
				evaMap.put("isEvaluate", true);
				evaMap.put("eventId", eventId);
				evaMap.put("evaLevel", "02");
				evaMap.put("evaContent", "满意");
				
				//防止强制归档失败，导致原有操作失败
				try {
					flag = super.subWorkFlowForEndingAndEvaluate(instanceId, curTaskId, END_NODE_CODE, curOrgId, curUserId, advice, curUserInfo, null, evaMap);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return flag;
	}
}
