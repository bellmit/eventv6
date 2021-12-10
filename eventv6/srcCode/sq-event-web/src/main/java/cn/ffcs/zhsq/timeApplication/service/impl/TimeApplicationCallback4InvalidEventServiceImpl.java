package cn.ffcs.zhsq.timeApplication.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCallbackService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 事件删除申请回调
 * @author zhangls
 *
 */
@Service(value = "timeApplicationCallback4InvalidEventService")
public class TimeApplicationCallback4InvalidEventServiceImpl implements
		ITimeApplicationCallbackService {
	
	@Autowired
	private ITimeApplicationService timeApplicationService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Resource(name = "workflowDecisionMakingService")
	private IWorkflowDecisionMakingService<String> workflowDecisionMakingService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Override
	public void timeApplicationCallback(Map<String, Object> params) {
		Map<String, Object> timeApplication = new HashMap<String, Object>();
		Long eventId = null, updaterId = null;
		String timeAppCheckStatus = null;
		
		if(CommonFunctions.isNotBlank(params, "applicationId")) {
			Long applicationId = Long.valueOf(params.get("applicationId").toString());
			
			timeApplication = timeApplicationService.findTimeAppliationById(applicationId, null);
		}
		
		if(CommonFunctions.isNotBlank(params, "timeAppCheckStatus")) {
			timeAppCheckStatus = params.get("timeAppCheckStatus").toString();
		}
		if(CommonFunctions.isNotBlank(timeApplication, "businessKeyId")) {
			eventId = Long.valueOf(timeApplication.get("businessKeyId").toString());
		}
		
		if(CommonFunctions.isNotBlank(params, "updaterId")) {
			updaterId = Long.valueOf(params.get("updaterId").toString());
		} else if(CommonFunctions.isNotBlank(params, "creatorId")) {
			updaterId = Long.valueOf(params.get("creatorId").toString());
		}
		
		if(eventId != null && eventId > 0) {
			if(ITimeApplicationCheckService.CHECK_STATUS.STATUS_INIT.toString().equals(timeAppCheckStatus)
					|| ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.toString().equals(timeAppCheckStatus)) {
				//审核记录添加成功或者删除申请通过，则删除事件
				List<Long> eventIdList = new ArrayList<Long>();
				
				eventIdList.add(eventId);
				
				eventDisposalService.deleteEventDisposalByIds(eventIdList, updaterId);
			} else if(ITimeApplicationCheckService.CHECK_STATUS.STATUS_FAIL.toString().equals(timeAppCheckStatus)) {
				//删除审核不通过，恢复事件状态
				ProInstance proInstance = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
				String eventStatus = null,
					   STATUS_DONE = "2";	//实例已归档
				
				if(proInstance != null) {
					if(STATUS_DONE.equals(proInstance.getStatus())) {
						eventStatus = ConstantValue.EVENT_STATUS_END;
					} else {
						String eventStatusDecision = null,
							   EVENT_STATUS_DECISION_SERVICE = "eventStatusDecisionMakingService";//事件状态决策类
						EventDisposal event = null;
						Map<String, Object> decisionParams = new HashMap<String, Object>(),
											eventParams = new HashMap<String, Object>(),
											eventMap = null;
						String orgCode = null;
						Long orgId = proInstance.getOrgId();
						StringBuffer trigCondition = new StringBuffer("");
						
						eventParams.put("isIgnoreStatus", true);
						
						try {
							eventMap = eventDisposalService.findEventByMap(eventId, eventParams);
						} catch (Exception e) {}
						
						if(CommonFunctions.isNotBlank(eventMap, "event")) {
							event = (EventDisposal) eventMap.get("event");
						}
						
						if(orgId != null && orgId > 0) {
							OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
							
							if(orgInfo != null) {
								orgCode = orgInfo.getOrgCode();
							}
						}
						if(event != null) {
							String eventType = event.getType(),
								   bizPlatform = event.getBizPlatform();
							
							if(StringUtils.isNotBlank(eventType)) {
								trigCondition.append("[").append(ConstantValue.EVENT_STATUS).append("_").append(eventType);
								
								if(StringUtils.isNotBlank(bizPlatform) && !ConstantValue.DEFAULT_EVENT_BIZ_PLATFORM.equals(bizPlatform)) {
									trigCondition.append("|").append(ConstantValue.EVENT_STATUS).append("_").append(bizPlatform).append("_").append(eventType);
								}
								
								trigCondition.append("]");
							}
						}
						
						if(trigCondition.length() == 0) {
							trigCondition.append(ConstantValue.EVENT_STATUS);
						}
						
						eventStatusDecision = funConfigurationService.turnCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, trigCondition.toString(), IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);
						if(StringUtils.isBlank(eventStatusDecision)) {
							eventStatusDecision = EVENT_STATUS_DECISION_SERVICE;
						}
						
						List<Map<String, Object>> eventTaskMapList = eventDisposalWorkflowService.queryProInstanceDetail(proInstance.getInstanceId(), IEventDisposalWorkflowService.SQL_ORDER_DESC);
						if(eventTaskMapList != null && eventTaskMapList.size() > 1) {
							Map<String, Object> eventTaskMap = null;
							String operateType = null,
								   REJECT_OPERATE = "2",
								   taskId = null,
								   preTaskId = null;
							
							decisionParams.put("nextNodeCode", eventTaskMapList.get(0).get("TASK_CODE"));
							
							//获取首个非人为创建、非驳回操作的历史任务
							for(int index = 1, len = eventTaskMapList.size(); index < len; index++) {
								eventTaskMap = eventTaskMapList.get(index);
								if(CommonFunctions.isNotBlank(eventTaskMap, "NODE_ID")) {
									if(CommonFunctions.isNotBlank(eventTaskMap, "OPERATE_TYPE")) {
										operateType = eventTaskMap.get("OPERATE_TYPE").toString();
									}
									
									if(CommonFunctions.isNotBlank(eventTaskMap, "TASK_ID")) {
										taskId = eventTaskMap.get("TASK_ID").toString();
									}
									if(preTaskId == null && REJECT_OPERATE.equals(operateType)) {
										if(CommonFunctions.isNotBlank(eventTaskMap, "PRE_TASK_ID")) {
											preTaskId = eventTaskMap.get("PRE_TASK_ID").toString();
										}
									}
									
									if(preTaskId == null || preTaskId.equals(taskId)) {
										decisionParams.put("curNodeCode", eventTaskMap.get("TASK_CODE"));
										break;
									}
								}
							}
							
							//当采集提交后，有驳回给采集环节时，此时获取不到curNodeCode，将curNodeCode设置为nextNodeCode的值即可
							if(CommonFunctions.isBlank(decisionParams, "curNodeCode")) {
								decisionParams.put("curNodeCode", decisionParams.get("nextNodeCode"));
							}
						}
						
						decisionParams.put("eventId", eventId);
						decisionParams.put("userId", updaterId);
						decisionParams.put("orgId", orgId);
						decisionParams.put("decisionService", eventStatusDecision);
						decisionParams.put("isRecovery", true);//只恢复事件状态，不添加事件中间过程
						
						try {
							eventStatus = workflowDecisionMakingService.makeDecision(decisionParams);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					eventStatus = ConstantValue.EVENT_STATUS_DRAFT;
				}
				
				if(StringUtils.isNotBlank(eventStatus)) {
					EventDisposal event = new EventDisposal();
					
					event.setEventId(eventId);
					event.setStatus(eventStatus);
					event.setUpdatorId(updaterId);
					
					eventDisposalService.updateEventDisposalById(event);
				}
			}
		}
	}

}
