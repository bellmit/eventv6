package cn.ffcs.zhsq.timeApplication.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 时限申请 南昌(NCH)大联动(JointCooperation)
 * @author zhangls
 *
 */
@Service(value = "timeApplication4NCHJointCooperationService")
public class TimeApplication4NCHJointCooperationServiceImpl extends 
		TimeApplication4BaseServiceImpl {
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Override
	public Map<String, Object> initTimeApplication4Event(Long eventId, String applicationType, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		Long curTaskId = -1L, instanceId = -1L;
		//事件时限审核回调服务名称
		String eventCallbackServiceName = "timeApplicationCallback4EventService",
			   userOrgCode = userInfo.getOrgCode();
		boolean isAutoAudit = true;
		
		params.putAll(super.initTimeApplication4Event(eventId, applicationType, userInfo, extraParam));//提前是为了后续的属性调整
		
		if(StringUtils.isBlank(applicationType) && CommonFunctions.isNotBlank(extraParam, "applicationType")) {
			applicationType = extraParam.get("applicationType").toString();
		}
		if(CommonFunctions.isNotBlank(extraParam, "instanceId")) {
			try {
				instanceId = Long.valueOf(extraParam.get("instanceId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(instanceId < 0) {
			instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
		}
		
		if(CommonFunctions.isNotBlank(extraParam, "curTaskId")) {
			try {
				curTaskId = Long.valueOf(extraParam.get("curTaskId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(curTaskId < 0 && instanceId != null && instanceId > 0) {
			curTaskId = eventDisposalWorkflowService.curNodeTaskId(instanceId);
		}
		
		if(CommonFunctions.isNotBlank(extraParam, "isAutoAudit")) {
			isAutoAudit = Boolean.valueOf(extraParam.get("isAutoAudit").toString());
		}
		
		if(isAutoAudit) {//需要自动审核时，才获取审核人员
			if(ITimeApplicationService.APPLICATION_TYPE.EVENT_INSPECT.getValue().equals(applicationType) || 
					ITimeApplicationService.APPLICATION_TYPE.EVENT_REPORT.getValue().equals(applicationType) || 
					ITimeApplicationService.APPLICATION_TYPE.EVENT_SEND.getValue().equals(applicationType)) {
				if(CommonFunctions.isBlank(extraParam, "auditorId")) {
					params.put("auditorId", userInfo.getUserId());
				}
				if(CommonFunctions.isBlank(extraParam, "auditorOrgId")) {
					params.put("auditorOrgId", userInfo.getOrgId());
				}
				
				params.put("timeAppCheckStatus", ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.getValue());
			} else if(ITimeApplicationService.APPLICATION_TYPE.EVENT_DELAY.getValue().equals(applicationType)) {
				List<Map<String, Object>> taskMapList = eventDisposalWorkflowService.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
				if(taskMapList != null) {//获取延时申请审核人员，该事件对应的最近一次督查督办操作人员
					Long taskId = -1L;
					Map<String, Object> timeAppCheckParam = new HashMap<String, Object>(),
										timeAppCheck = null;
					List<Map<String, Object>> timeAppCheckList = null;
					timeAppCheckParam.put("applicationType", ITimeApplicationService.APPLICATION_TYPE.EVENT_INSPECT.getValue());
					
					for(Map<String, Object> taskMap : taskMapList) {
						taskId = -1L;
						
						if(CommonFunctions.isNotBlank(taskMap, "TASK_ID")) {
							try {
								taskId = Long.valueOf(taskMap.get("TASK_ID").toString());
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
						}
						
						if(taskId > 0) {
							timeAppCheckParam.put("businessId", taskId);
							
							timeAppCheckList = this.findTimeApplicationList(timeAppCheckParam);
							
							if(timeAppCheckList != null && timeAppCheckList.size() > 0) {
								timeAppCheck = timeAppCheckList.get(0);
							}
							
							if(timeAppCheck != null && !timeAppCheck.isEmpty()) {
								if(CommonFunctions.isNotBlank(timeAppCheck, "auditorId")) {
									params.put("auditorId", timeAppCheck.get("auditorId"));
								}
								if(CommonFunctions.isNotBlank(timeAppCheck, "auditorOrgId")) {
									params.put("auditorOrgId", timeAppCheck.get("auditorOrgId"));
								}
								
								break;
							}
						}
					}
				}
			} else if(ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue().equals(applicationType)) {//待办延时申请
				EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
				if(event != null) {
					String eventStatus = event.getStatus();
					
					if(ConstantValue.EVENT_STATUS_REPORT.equals(eventStatus)) {//上报
						OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
						
						if(orgInfo != null && CommonFunctions.isBlank(extraParam, "auditorOrgId")) {
							params.put("auditorOrgId", orgInfo.getParentOrgId());//旧组织树的用法
						}
						
						if(CommonFunctions.isBlank(extraParam, "auditorType")) {//默认设置为 组织 进行审核
							params.put("auditorType", ITimeApplicationCheckService.AUDITOR_TYPE.AUDITOR_ORG.getValue());
						}
					} else if(ConstantValue.EVENT_STATUS_DISTRIBUTE.equals(eventStatus)) {//下派
						Map<String, Object> preTaskMap = findPreTaskMap(instanceId, userInfo);
						if(preTaskMap != null) {
							if(CommonFunctions.isBlank(extraParam, "auditorId") && CommonFunctions.isNotBlank(preTaskMap, "TRANSACTOR_ID")) {
								params.put("auditorId", preTaskMap.get("TRANSACTOR_ID"));
							}
							if(CommonFunctions.isBlank(extraParam, "auditorOrgId") && CommonFunctions.isNotBlank(preTaskMap, "ORG_ID")) {
								params.put("auditorOrgId", preTaskMap.get("ORG_ID"));
							}
						}
					} else {
						throw new Exception("目前缺少时限审核人员！");
					}
				}
			}
		}
		
		params.put("isAutoAudit", isAutoAudit);
		params.put("businessId", curTaskId);
		params.put("businessKeyId", eventId);
		params.put("applicationType", applicationType);
		params.put("serviceName", eventCallbackServiceName);
		params.put("userOrgCode", userOrgCode);
		
		return params;
	}
	
	/**
	 * 获取上一非驳回操作步骤
	 * @param instanceId	流程实例id
	 * @param userInfo		用户信息
	 * @return
	 */
	private Map<String, Object> findPreTaskMap(Long instanceId, UserInfo userInfo) {
		List<Map<String, Object>> queryTaskList = eventDisposalWorkflowService.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
		Map<String, Object> preTaskMap = null;
		
		if(queryTaskList != null) {
			String OPERATE_PASS = "1", 		//操作——提交
				   OPERATE_REJECT = "2",	//操作——驳回
				   OPERATE_RECALL = "5", //操作——撤回
				   operateType = null;
			Long taskId = -1L, preTaskId = -1L;
			
			for(Map<String, Object> taskMap : queryTaskList) {
				operateType = null;
				taskId = -1L;
				
				if(CommonFunctions.isNotBlank(taskMap, "OPERATE_TYPE")) {
					operateType = taskMap.get("OPERATE_TYPE").toString();
					
					if(OPERATE_PASS.equals(operateType)) {
						if(CommonFunctions.isNotBlank(taskMap, "TASK_ID")) {
							taskId = Long.valueOf(taskMap.get("TASK_ID").toString());
						}
						
						if(preTaskId < 0 || preTaskId.equals(taskId)) {
							preTaskMap = taskMap; 
							break;
						}
					} else if(OPERATE_REJECT.equals(operateType) || OPERATE_RECALL.equals(operateType)) {
						if(CommonFunctions.isNotBlank(taskMap, "PRE_TASK_ID")) {
							preTaskId = Long.valueOf(taskMap.get("PRE_TASK_ID").toString());
						}
					}
				}
			}
		}
		
		return preTaskMap;
	}

}
