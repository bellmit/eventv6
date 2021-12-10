package cn.ffcs.zhsq.timeApplication.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.common.message.bo.ReceiverBO;
import cn.ffcs.shequ.zzgl.service.holidayinfo.IHolidayInfoService;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCallbackService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * 时限申请事件回调
 * @author zhangls
 *
 */
@Service(value = "timeApplicationCallback4EventService")
public class TimeApplicationCallback4EventServiceImpl implements
		ITimeApplicationCallbackService {
	@Autowired
	private ITimeApplicationService timeApplicationService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IHolidayInfoService holidayInfoService;
	
	@Autowired
	private IEventExtendService eventExtendService;
	
	@Autowired
	private MessageOutService messageService;
	
	@Override
	public void timeApplicationCallback(Map<String, Object> params) {
		String timeAppCheckStaus = null;
		
		if(CommonFunctions.isNotBlank(params, "timeAppCheckStatus")) {
			timeAppCheckStaus = params.get("timeAppCheckStatus").toString();
		}
		
		//只有审核完成后，才需要进行后续的操作
		if(ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.getValue().equals(timeAppCheckStaus)
		|| ITimeApplicationCheckService.CHECK_STATUS.STATUS_FAIL.getValue().equals(timeAppCheckStaus)) {
			Map<String, Object> timeApplication = new HashMap<String, Object>();
			
			if(CommonFunctions.isNotBlank(params, "applicationId")) {
				Long applicationId = Long.valueOf(params.get("applicationId").toString());
				
				timeApplication = timeApplicationService.findTimeAppliationById(applicationId, null);
			}
			
			if(CommonFunctions.isNotBlank(timeApplication, "businessKeyId")) {
				Long eventId = Long.valueOf(timeApplication.get("businessKeyId").toString());
				EventDisposal event = new EventDisposal();
				String applicationType = null;
				
				if(CommonFunctions.isNotBlank(timeApplication, "applicationType")) {
					applicationType = timeApplication.get("applicationType").toString();
				}
				
				if(eventId > 0 && ITimeApplicationService.APPLICATION_TYPE.EVENT_INSPECT.toString().equals(applicationType)) {
					//督查督办时，设置事件扩展属性
					Map<String, Object> eventExtend = new HashMap<String, Object>();
					eventExtend.put("eventId", eventId);
					eventExtend.put("isInspected", "1");
					if(CommonFunctions.isNotBlank(params, "creatorId")) {
						eventExtend.put("creatorId", params.get("creatorId"));
					}
					
					try {
						eventExtendService.saveEventExtend(eventExtend);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				//审核通过时，调整事件时限
				if(ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.getValue().equals(timeAppCheckStaus)) {
					int interval = 0;
					Date applicationDate = null;
					
					if(CommonFunctions.isNotBlank(timeApplication, "interval")) {
						interval = Integer.valueOf(timeApplication.get("interval").toString());
					}
					
					//待办延时申请 时限为：原办结期限 + 申请天数
					if(ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue().equals(applicationType)) {
						EventDisposal eventTmp = eventDisposalService.findEventByIdSimple(eventId);
						
						if(eventTmp != null) {
							applicationDate = eventTmp.getHandleDate();
						}
					} else if(CommonFunctions.isNotBlank(timeApplication, "createTime")) {
						applicationDate = (Date)timeApplication.get("createTime");
					}
					
					if(applicationDate != null && interval > 0) {
						String intervalUnit = null;
						Date handleDate = null;
						
						if(CommonFunctions.isNotBlank(timeApplication, "intervalUnit")) {
							intervalUnit = timeApplication.get("intervalUnit").toString();
						}
						
						if(ITimeApplicationService.INTERVAL_UNIT.WEEK_DAY.toString().equals(intervalUnit)) {
							handleDate = holidayInfoService.getWorkDateByAfterWorkDay(applicationDate, interval);
						} else if(ITimeApplicationService.INTERVAL_UNIT.NATURAL_DAY.toString().equals(intervalUnit)) {
							handleDate = DateUtils.addInterval(applicationDate, interval, "00");
						} else if(ITimeApplicationService.INTERVAL_UNIT.NATURAL_HOUR.getValue().equals(intervalUnit)) {
							handleDate = DateUtils.addInterval(applicationDate, interval, "03");
						}
						
						//调整事件办理时限及时限状态
						if(handleDate != null) {
							event.setEventId(eventId);
							event.setHandleDate(handleDate);
							event.setHandleDateFlag(ConstantValue.LIMIT_TIME_STATUS_NORMAL);
							
							eventDisposalService.updateEventDisposalById(event);
						}
					}
				}
				
				//延时申请 审核后进行消息发送
				if(ITimeApplicationService.APPLICATION_TYPE.EVENT_DELAY.getValue().equals(applicationType)) {
					sendMsg4Audit(timeApplication, params);
				}
			}
		}
	}
	
	/**
	 * 审核通过或者不通过时，发送审核消息
	 * @param timeApplication
	 * 			businessKeyId	事件id
	 * 			creatorId		消息接收人员id
	 * @param timeAppCheck
	 * 			auditorId		消息发送人员id
	 * 			auditorOrgId	消息发送人员组织id
	 */
	private void sendMsg4Audit(Map<String, Object> timeApplication, Map<String, Object> timeAppCheck) {
		String timeAppCheckStaus = null;
		
		if(CommonFunctions.isNotBlank(timeAppCheck, "timeAppCheckStatus")) {
			timeAppCheckStaus = timeAppCheck.get("timeAppCheckStatus").toString();
		}
		
		if(ITimeApplicationCheckService.CHECK_STATUS.STATUS_FAIL.getValue().equals(timeAppCheckStaus)
		   || ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.getValue().equals(timeAppCheckStaus)) {
			Long eventId = -1L;
			EventDisposal event = null;
			
			if(CommonFunctions.isNotBlank(timeApplication, "businessKeyId")) {
				eventId = Long.valueOf(timeApplication.get("businessKeyId").toString());
			}
			
			if(eventId > 0) {
				event = eventDisposalService.findEventByIdSimple(eventId);
			}
			
			if(event != null) {
				List<Long> nextUserIdList = new ArrayList<Long>();
				ReceiverBO receiver = new ReceiverBO();
				String EVENT_MEG_MODULE_CODE = "02",//事件消息所属模块编码
					   EVENT_VIEW_LINK = "$EVENT_DOMAIN/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&eventId=",//事件消息查看详情链接
					   EVENT_MSG_ACTION = "发送了",//消息发送操作
					   viewLink = null, 
					   msgContent = "您的督查督办事件，事件编号为：@eventCode，延期申请";//消息内容
				Long receiverId = -1L, sendUserId = -1L, sendOrgId = -1L;
				
				if(CommonFunctions.isNotBlank(timeApplication, "creatorId")) {
					receiverId = Long.valueOf(timeApplication.get("creatorId").toString());
				}
				if(CommonFunctions.isNotBlank(timeAppCheck, "auditorId")) {
					sendUserId = Long.valueOf(timeAppCheck.get("auditorId").toString());
				}
				if(CommonFunctions.isNotBlank(timeAppCheck, "auditorOrgId")) {
					sendOrgId = Long.valueOf(timeAppCheck.get("auditorOrgId").toString());
				}
				
				viewLink = EVENT_VIEW_LINK + eventId;
				
				msgContent = msgContent.replaceAll("@eventCode", event.getCode());
				
				if(CommonFunctions.isNotBlank(timeAppCheck, "timeAppCheckStatusName")) {
					msgContent += timeAppCheck.get("timeAppCheckStatusName");
				}
				if(ITimeApplicationCheckService.CHECK_STATUS.STATUS_FAIL.getValue().equals(timeAppCheckStaus)) {
					if(CommonFunctions.isNotBlank(timeAppCheck, "checkAdvice")) {
						msgContent += "，未通过原因：" + timeAppCheck.get("checkAdvice");
					}
				}
				
				nextUserIdList.add(receiverId);
				
				receiver.setUserIdList(nextUserIdList);
				
				messageService.sendCommonMessageA(sendUserId, sendOrgId, EVENT_MEG_MODULE_CODE, eventId, viewLink, msgContent, EVENT_MSG_ACTION, receiver);
			}
		}
	}
}
