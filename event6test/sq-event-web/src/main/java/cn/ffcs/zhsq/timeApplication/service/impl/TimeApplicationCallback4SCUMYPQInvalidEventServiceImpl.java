package cn.ffcs.zhsq.timeApplication.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 智慧城管(Smart City Urban Manage)延平区(Yan Ping Qu) 事件删除申请回调
 * @ClassName:   TimeApplicationCallback4SCUMYPQInvalidEventServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年6月11日 下午4:27:31
 */
@Service(value = "timeApplicationCallback4SCUMYPQInvalidEventService")
public class TimeApplicationCallback4SCUMYPQInvalidEventServiceImpl
		extends TimeApplicationCallback4InvalidEventServiceImpl {
	@Autowired
	private ITimeApplicationService timeApplicationService;
	
	@Autowired
	private ITimeApplicationCheckService timeApplicationCheckService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Override
	public void timeApplicationCallback(Map<String, Object> params) {
		String timeAppCheckStatus = null,
			   CHECK_STATUS_RECOVERY = "4";//挂起事件恢复
		
		if(CommonFunctions.isNotBlank(params, "timeAppCheckStatus")) {
			timeAppCheckStatus = params.get("timeAppCheckStatus").toString();
		}
		
		if(CHECK_STATUS_RECOVERY.equals(timeAppCheckStatus)) {
			timeAppCheckStatus = ITimeApplicationCheckService.CHECK_STATUS.STATUS_FAIL.getValue();
			
			params.put("timeAppCheckStatus", timeAppCheckStatus);
		}
		
		//删除审核不通过，恢复事件办理时限，填补滞留时间段
		if(ITimeApplicationCheckService.CHECK_STATUS.STATUS_FAIL.toString().equals(timeAppCheckStatus)) {
			Long checkId = null, applicationId = null, eventId = null;
			Date timeCheckOperateDate = null, timeAppCreateDate = null, eventHandleDate = null;
			
			if(CommonFunctions.isNotBlank(params, "updateTime")) {
				timeCheckOperateDate = (Date) params.get("updateTime");
			} else {
				if(CommonFunctions.isNotBlank(params, "checkId")) {
					checkId = Long.valueOf(params.get("checkId").toString());
				}
				
				if(checkId != null && checkId > 0) {
					Map<String, Object> timeAppCheckMap = timeApplicationCheckService.findTimeAppCheckById(checkId);
					
					if(CommonFunctions.isNotBlank(timeAppCheckMap, "updateTime")) {
						timeCheckOperateDate = (Date) timeAppCheckMap.get("updateTime");
					}
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "applicationId")) {
				applicationId = Long.valueOf(params.get("applicationId").toString());
			}
			
			if(applicationId != null && applicationId > 0) {
				Map<String, Object> timeAppMap = timeApplicationService.findTimeAppliationById(applicationId, null);
				
				if(CommonFunctions.isNotBlank(timeAppMap, "createTime")) {
					timeAppCreateDate = (Date) timeAppMap.get("createTime");
				}
				
				if(CommonFunctions.isNotBlank(timeAppMap, "businessKeyId")) {
					eventId = Long.valueOf(timeAppMap.get("businessKeyId").toString());
				}
			}
			
			if(eventId != null && eventId > 0) {
				EventDisposal event = null;
				Map<String, Object> eventParams = new HashMap<String, Object>(),
									eventMap = null;
				
				eventParams.put("isIgnoreStatus", true);
				
				try {
					eventMap = eventDisposalService.findEventByMap(eventId, eventParams);
				} catch (Exception e) {}
				
				if(CommonFunctions.isNotBlank(eventMap, "event")) {
					event = (EventDisposal) eventMap.get("event");
				}
				
				if(event != null) {
					eventHandleDate = event.getHandleDate();
				}
			}
			
			//变更事件办理时限
			if(eventHandleDate != null && timeCheckOperateDate != null && timeAppCreateDate != null) {
				EventDisposal event = new EventDisposal();
				event.setEventId(eventId);
				
				event.setHandleDate(new Date(eventHandleDate.getTime() + (timeCheckOperateDate.getTime() - timeAppCreateDate.getTime())));
				event.setHandleDateFlag(ConstantValue.LIMIT_TIME_STATUS_NORMAL);
				
				eventDisposalService.updateEventDisposalById(event);
			}
		}
		
		super.timeApplicationCallback(params);
	}
}
