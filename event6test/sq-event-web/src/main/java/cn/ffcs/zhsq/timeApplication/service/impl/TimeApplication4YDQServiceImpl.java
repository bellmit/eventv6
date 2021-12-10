package cn.ffcs.zhsq.timeApplication.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 盐都区(YDQ) 
 * @author zhangls
 *
 */
@Service(value = "timeApplication4YDQService")
public class TimeApplication4YDQServiceImpl extends TimeApplication4BaseServiceImpl {
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Override
	public Long saveTimeApplication(Map<String, Object> timeApplication) throws Exception {
		Long applicationId = -1L;
		StringBuffer msgWrong = new StringBuffer("");
		String applicationType = null;
		
		if(CommonFunctions.isBlank(timeApplication, "applicationType")) {
			msgWrong.append("缺少属性[applicationType]，请检查！");
		} else {
			applicationType = timeApplication.get("applicationType").toString();
		}
		
		if(ITimeApplicationService.APPLICATION_TYPE.EVENT_CHECK.getValue().equals(applicationType)) {
			Long eventId = null;
			
			if(CommonFunctions.isNotBlank(timeApplication, "businessKeyId")) {
				try {
					eventId = Long.valueOf(timeApplication.get("businessKeyId").toString());
				} catch(NumberFormatException e) {
					msgWrong.append("属性【businessKeyId】：【"+ timeApplication.get("businessKeyId") +"】不是有效的数值！");
					e.printStackTrace();
				}
			}
			
			if(eventId != null && eventId > 0) {
				EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
				
				if(event != null) {
					String eventStatus = event.getStatus();
					
					if(ConstantValue.EVENT_STATUS_DRAFT.equals(eventStatus)) {
						msgWrong.append("该事件为草稿事件，不可进行核验操作！");
					} else if(ConstantValue.EVENT_STATUS_END.equals(eventStatus)) {
						msgWrong.append("该事件已完结，不可进行核验操作！");
					}
				} else {
					msgWrong.append("事件id【"+ eventId +"】没有找到有效的事件信息！");
				}
			} else {
				msgWrong.append(ITimeApplicationService.APPLICATION_TYPE.EVENT_CHECK.getName() + "：缺少属性【businessKeyId】！");
			}
			
			if(msgWrong.length() == 0) {
				Map<String, Object> timeAppParam = new HashMap<String, Object>();
				List<Map<String, Object>> timeAppList = null;
				
				timeAppParam.put("businessKeyId", eventId);
				timeAppParam.put("applicationType", ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_PREPRESS.getValue());
				timeAppParam.put("timeAppCheckStatus",  ITimeApplicationCheckService.CHECK_STATUS.STATUS_INIT.getValue());
				
				timeAppList = this.findTimeApplicationList(timeAppParam);
				
				if(timeAppList != null && timeAppList.size() > 0) {
					msgWrong.append("请先完成该事件的预处理审核记录！");
				}
			}
		}
		
		if(msgWrong.length() > 0) {
			throw new Exception(msgWrong.toString());
		} else {
			applicationId = super.saveTimeApplication(timeApplication);
		}
		
		return applicationId;
	}
	
	@Override
	public Map<String, Object> initTimeApplication4Event(Long eventId, String applicationType, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		Map<String, Object> resultMap = super.initTimeApplication4Event(eventId, applicationType, userInfo, extraParam);
		
		if((eventId == null || eventId < 0) && (CommonFunctions.isNotBlank(extraParam, "eventId"))) {
			try {
				eventId = Long.valueOf(extraParam.get("eventId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if(eventId == null || eventId < 0) {
			throw new IllegalArgumentException("缺少有效的事件id！");
		}
		
		if(StringUtils.isBlank(applicationType) && CommonFunctions.isNotBlank(extraParam, "applicationType")) {
			applicationType = extraParam.get("applicationType").toString();
		}
		
		if(StringUtils.isBlank(applicationType)) {
			throw new IllegalArgumentException("缺少有效的审核类别！");
		}
		
		//事件核验
		if(ITimeApplicationService.APPLICATION_TYPE.EVENT_CHECK.getValue().equals(applicationType)) {
			//事件时限审核回调服务名称
			String eventCallbackServiceName = "timeApplicationCallback4YDQEventService",
					  userOrgCode = userInfo.getOrgCode();
			
			resultMap = new HashMap<String, Object>();

			resultMap.put("auditorId", userInfo.getUserId());
			resultMap.put("auditorOrgId", userInfo.getOrgId());
			resultMap.put("auditorType", ITimeApplicationCheckService.AUDITOR_TYPE.AUDITOR_NONE.getValue());
			resultMap.put("isDuplicatedCheck", false);
			resultMap.put("isAutoAudit", true);
			resultMap.put("businessId", eventId);
			resultMap.put("businessKeyId", eventId);
			resultMap.put("applicationType", applicationType);
			resultMap.put("serviceName", eventCallbackServiceName);
			resultMap.put("userOrgCode", userOrgCode);
		}
		
		return resultMap;
	}
}
