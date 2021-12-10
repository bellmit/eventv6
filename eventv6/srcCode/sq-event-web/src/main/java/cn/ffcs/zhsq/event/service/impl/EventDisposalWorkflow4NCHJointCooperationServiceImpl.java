package cn.ffcs.zhsq.event.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.common.message.bo.ReceiverBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 南昌(NCH)大联动工作流相关接口
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflow4NCHJointCooperationService")
public class EventDisposalWorkflow4NCHJointCooperationServiceImpl extends
		EventDisposalWorkflowForEventNewServiceImpl {
	
	@Autowired
	private MessageOutService messageService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventExtendService eventExtendService;
	
	@Autowired
	private ITimeApplicationService timeApplicationService;
	
	private final String CLOSE_NODE_CODE = "task8",				//结案环节编码
						 CITIZEN_HOTLINE_NODE_CODE = "task16";	//市民热线环节编码
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String instanceIdStr = super.startFlowByWorkFlow(eventId, workFlowId, toClose, userInfo, extraParam);
		
		//采集并结案时才发送消息
		if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose) && StringUtils.isNotBlank(instanceIdStr)) {
			sendMsg4Event(Long.valueOf(instanceIdStr), eventId, null, userInfo);
		}
		
		return instanceIdStr;
	}
		
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception {
		boolean result = false;
		
		if(StringUtils.isNotBlank(advice) && CommonFunctions.isNotBlank(extraParam, "assistOrgNames")) {
			String assistOrgLabel = "办理单位";
			
			if(CommonFunctions.isNotBlank(extraParam, "assistOrgLabel")) {
				assistOrgLabel = extraParam.get("assistOrgLabel").toString();
			}
			
			advice += " " + assistOrgLabel + "：" + extraParam.get("assistOrgNames").toString();
		}
		
		result = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		
		//提交给“市民热线”、“处理中”时，不设置办理时限、不发送消息
		if(result && !CITIZEN_HOTLINE_NODE_CODE.equals(nextNodeName) && !ConstantValue.HANDLING_TASK_CODE.equals(nextNodeName)) {
			sendMsg4Event(instanceId, null, nextStaffId, userInfo);
			
			if(CommonFunctions.isNotBlank(extraParam, "formId")) {
				Long eventId = Long.valueOf(extraParam.get("formId").toString());
				String eventStatus = "", applicationType = "";
				int interval = 0;//时限间隔
				
				EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
				
				if(event != null) {
					eventStatus = event.getStatus();
				}
				
				if(ConstantValue.EVENT_STATUS_REPORT.equals(eventStatus)) {
					interval = 2;//上报操作，默认在当前系统时间上，添加2个工作日
					applicationType = ITimeApplicationService.APPLICATION_TYPE.EVENT_REPORT.getValue();
				} else if(ConstantValue.EVENT_STATUS_DISTRIBUTE.equals(eventStatus)) {
					if(CommonFunctions.isNotBlank(extraParam, "interval")) {
						try {
							interval = Integer.valueOf(extraParam.get("interval").toString());
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
					}
					
					if(interval > 0) {
						applicationType = ITimeApplicationService.APPLICATION_TYPE.EVENT_SEND.getValue();
					}
				}
				
				if(StringUtils.isNotBlank(applicationType)) {
					Map<String, Object> extendMap = new HashMap<String, Object>(),
										applicationMap = new HashMap<String, Object>();
					
					extendMap.put("interval", interval);
					extendMap.put("isAutoAudit", true);
					extendMap.put("timeAppCheckStatus", ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.getValue());
					
					applicationMap = eventExtendService.initTimeApplication4Event(eventId, applicationType, userInfo, extendMap);
					
					timeApplicationService.saveTimeApplication(applicationMap);
				}
			}
		}
		
		return result;
	}
	
	@Override
	public boolean rejectWorkFlow(Map<String, Object> params, UserInfo userInfo) throws Exception {
		boolean result = super.rejectWorkFlow(params, userInfo);
		
		if(result) {
			Long eventId = -1L, instanceId = -1L;
			
			if(CommonFunctions.isNotBlank(params, "eventId")) {
				eventId = Long.valueOf(params.get("eventId").toString());
			}
			
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			}
			
			sendMsg4Event(instanceId, eventId, null, userInfo);
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		Map<String, Object> initMap = super.initFlowInfo(taskId, instanceId, userInfo, params);
		String eventStatus = "", handleDateFlag = "";
		
		if(CommonFunctions.isNotBlank(initMap, "proInstance")) {
			ProInstance proInstance = (ProInstance)initMap.get("proInstance");
			Long eventId = proInstance.getFormId();
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			
			if(event != null) {
				eventStatus = event.getStatus();
				handleDateFlag = event.getHandleDateFlag();
			}
		}
		
		if(ConstantValue.EVENT_STATUS_REPORT.equals(eventStatus) ||
				ConstantValue.EVENT_STATUS_DISTRIBUTE.equals(eventStatus)) {
			Map<String, Object> timeAppCheckParam = new HashMap<String, Object>();
			List<String> applicationTypeList = new ArrayList<String>();
			List<Map<String, Object>> timeAppList = null;
			String applicationType = null;
			
			applicationTypeList.add(ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue());
			
			if(ConstantValue.EVENT_STATUS_REPORT.equals(eventStatus)) {//上报
				if(ConstantValue.LIMIT_TIME_STATUS_EXPIRED.equals(handleDateFlag)) {
					applicationType = ITimeApplicationService.APPLICATION_TYPE.EVENT_REPORT.getValue();
				}
			} else if(ConstantValue.EVENT_STATUS_DISTRIBUTE.equals(eventStatus)) {//下派
				applicationType = ITimeApplicationService.APPLICATION_TYPE.EVENT_SEND.getValue();
			}
			
			if(StringUtils.isNotBlank(applicationType)) {
				applicationTypeList.add(applicationType);
			}
			
			timeAppCheckParam.put("businessId", taskId);
			timeAppCheckParam.put("applicationTypeList", applicationTypeList);
			
			timeAppList = timeApplicationService.findTimeApplicationList(timeAppCheckParam);
			
			if(timeAppList != null && timeAppList.size() > 0) {//将下一可选环节限制为只有“结案”
				if(CommonFunctions.isNotBlank(initMap, "taskNodes")) {
					List<Node> taskNodes = (List<Node>)initMap.get("taskNodes");
					for(Node taskNode : taskNodes) {
						if(CLOSE_NODE_CODE.equals(taskNode.getNodeName())) {
							taskNodes = new ArrayList<Node>();
							taskNodes.add(taskNode);
							break;
						}
					}
					
					initMap.put("taskNodes", taskNodes);
				}
			}
		} else if(ConstantValue.EVENT_STATUS_ARCHIVE.equals(eventStatus)) {//结案
			if(CommonFunctions.isNotBlank(initMap, "operateLists")) {
				List<Map<String, Object>> taskList = this.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
				if(taskList != null && taskList.size() > 1) {
					Map<String, Object> taskMap = taskList.get(1);
					
					//市民热线结案时，评价环节去除“驳回”操作按钮
					if(CommonFunctions.isNotBlank(taskMap, "TASK_CODE")) {
						if(CITIZEN_HOTLINE_NODE_CODE.equals(taskMap.get("TASK_CODE").toString())) {
							List<OperateBean> operateLists = (List<OperateBean>) initMap.get("operateLists");
							String REJECT_BTN_EVENT = "reject";//驳回按钮相应事件名称
							
							if(operateLists != null) {
								for(OperateBean operate : operateLists) {
									if(REJECT_BTN_EVENT.equals(operate.getOperateEvent())) {
										operateLists.remove(operate);
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		
		return initMap;
	}
	
	/**
	 * 发送事件消息
	 * @param instanceId	实例id
	 * @param eventId		事件id
	 * @param nextUserIdStr	下一办理人员，多个值以英文逗号连接，为空时，使用当前办理人员
	 * @param userInfo		办理用户信息
	 */
	private void sendMsg4Event(Long instanceId, Long eventId, String nextUserIdStr, UserInfo userInfo) {
		if(instanceId != null && instanceId > 0 && (eventId == null || eventId < 0)) {
			ProInstance pro = this.capProInstanceByInstanceId(instanceId);
			
			if(pro != null) {
				eventId = pro.getFormId();
			}
		} else if(eventId != null && eventId > 0 && (instanceId == null || instanceId < 0)) {
			instanceId = this.capInstanceIdByEventIdForLong(eventId);
		}
		
		if(instanceId != null && instanceId > 0 && eventId != null && eventId > 0) {
			if(StringUtils.isBlank(nextUserIdStr)) {
				nextUserIdStr = this.curNodeUserIds(instanceId);
			}
			
			if(StringUtils.isNotBlank(nextUserIdStr)) {
				String[] nextUserIdArray = nextUserIdStr.split(",");
				List<Long> nextUserIdList = new ArrayList<Long>();
				ReceiverBO receiver = new ReceiverBO();
				EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
				String EVENT_MEG_MODULE_CODE = "02",//事件消息所属模块编码
					   EVENT_VIEW_LINK = "$EVENT_DOMAIN/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=todo&eventId=",//事件消息查看详情链接
					   EVENT_MSG_ACTION = "发送了",//消息发送操作
					   viewLink = null, 
					   msgContent = "您有一条待办事件，事件编号为：@eventCode，请尽快处理";//消息内容
				
				if(event != null) {
					viewLink = EVENT_VIEW_LINK + eventId + "&instanceId=" + instanceId;
					msgContent = msgContent.replaceAll("@eventCode", event.getCode());
					
					for(String nextUserId : nextUserIdArray) {
						nextUserIdList.add(Long.valueOf(nextUserId));
					}
					
					receiver.setUserIdList(nextUserIdList);
					
					messageService.sendCommonMessage(userInfo.getUserId(), userInfo.getOrgId(), EVENT_MEG_MODULE_CODE, viewLink, msgContent, EVENT_MSG_ACTION, receiver);
				}
			}
		} else {
			logger.error("未发送事件消息，流程实例id为[" + instanceId + "]， 事件id为[" + eventId + "]！");
		}
	}
	
}
