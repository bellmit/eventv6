package cn.ffcs.zhsq.timeApplication.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 智慧城管(Smart City Urban Manage)延平区(Yan Ping Qu) 时限申请
 * @ClassName:   TimeApplication4SCUMYPQServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年6月10日 上午11:23:30
 */
@Service(value = "timeApplication4SCUMYPQService")
public class TimeApplication4SCUMYPQServiceImpl extends TimeApplication4BaseServiceImpl {
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Map<String, Object> initTimeApplication4Event(Long eventId, String applicationType, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(StringUtils.isBlank(applicationType) && CommonFunctions.isNotBlank(extraParam, "applicationType")) {
			applicationType = extraParam.get("applicationType").toString();
		}
		
		extraParam.put("isShowSendMsg", 
				ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue().equals(applicationType)
				|| ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_INVALID.toString().equals(applicationType));
		
		params.putAll(super.initTimeApplication4Event(eventId, applicationType, userInfo, extraParam));
		
		if(ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue().equals(applicationType)
				|| ITimeApplicationService.APPLICATION_TYPE.EVENT_SEND.getValue().equals(applicationType)) {
			//事件时限审核回调服务名称
			String eventCallbackServiceName = "timeApplicationCallback4EventService",
				   userOrgCode = userInfo.getOrgCode();
			List<Map<String, Object>> intervalUnitMapList = new ArrayList<Map<String, Object>>();
			List<GdZTreeNode> countyTreeNodeList = null;
			Map<String, Object> intervalUnitMap = null;
			
			params.put("isAutoAudit", true);
			params.put("businessId", eventId);
			params.put("businessKeyId", eventId);
			params.put("applicationType", applicationType);
			params.put("serviceName", eventCallbackServiceName);
			params.put("defaultIntervalUnit", ITimeApplicationService.INTERVAL_UNIT.NATURAL_HOUR.toString());
			params.put("userOrgCode", userOrgCode);
			
			intervalUnitMap = new HashMap<String, Object>();
			intervalUnitMap.put("dictGeneralCode", ITimeApplicationService.INTERVAL_UNIT.NATURAL_HOUR.getValue());
			intervalUnitMap.put("dictName", ITimeApplicationService.INTERVAL_UNIT.NATURAL_HOUR.getName());
			intervalUnitMapList.add(intervalUnitMap);
			
			intervalUnitMap = new HashMap<String, Object>();
			intervalUnitMap.put("dictGeneralCode", ITimeApplicationService.INTERVAL_UNIT.NATURAL_DAY.getValue());
			intervalUnitMap.put("dictName", ITimeApplicationService.INTERVAL_UNIT.NATURAL_DAY.getName());
			intervalUnitMapList.add(intervalUnitMap);
			
			//构造时限单位
			params.put("intervalUnitMapList", intervalUnitMapList);
			
			countyTreeNodeList = fiveKeyElementService.getTreeForEvent(userInfo, null, "__"+INodeCodeHandler.OPERATE_REPORT_X+"0"+INodeCodeHandler.ORG_UINT+INodeCodeHandler.COUNTY , null, null, params);
			
			if(countyTreeNodeList != null && countyTreeNodeList.size() > 0) {
				params.put("auditorType", ITimeApplicationCheckService.AUDITOR_TYPE.AUDITOR_ORG.getValue());
				params.put("auditorOrgId", Long.valueOf(countyTreeNodeList.get(0).getId()));
			}
		}
		
		return params;
	}
	
	@Override
	public Long saveTimeApplication(Map<String, Object> timeApplication) throws Exception {
		Long applicationId = super.saveTimeApplication(timeApplication);
		
		if(applicationId != null && applicationId > 0) {
			String applicationType = null;
			
			if(CommonFunctions.isNotBlank(timeApplication, "applicationType")) {
				applicationType = timeApplication.get("applicationType").toString();
			}
			
			//待办延时申请、挂起申请有短信发送
			if(ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue().equals(applicationType)
					|| ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_SUSPEND.getValue().equals(applicationType)
					|| ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_INVALID.toString().equals(applicationType)) {
				String defaultMobileNums = null, otherMobileNums = null, noteType = null, userOrgCode = null;
				StringBuffer mobileNums = new StringBuffer("");
				
				if(CommonFunctions.isNotBlank(timeApplication, "otherMobileNums")) {
					otherMobileNums = timeApplication.get("otherMobileNums").toString();
					mobileNums.append(otherMobileNums);
				}
				if(CommonFunctions.isNotBlank(timeApplication, "noteType")) {
					noteType = timeApplication.get("noteType").toString();
				}
				if(CommonFunctions.isNotBlank(timeApplication, "userOrgCode")) {
					userOrgCode = timeApplication.get("userOrgCode").toString();
				}
				
				defaultMobileNums = funConfigurationService.turnCodeToValue(ConstantValue.SMS_NOTE_TYPE, noteType+"RceiverMobilePhones", IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
				
				if(StringUtils.isNotBlank(defaultMobileNums)) {
					mobileNums.append(",").append(defaultMobileNums);
				}
				
				timeApplication.put("otherMobileNums", mobileNums.toString());
				
				try {
					if(!super.sendSMS(timeApplication)) {
						if(logger.isErrorEnabled()) {
							logger.error("短信发送失败！");
						}
					}
				} catch(Exception e) {
					if(logger.isErrorEnabled()) {
						logger.error(e.getMessage());
					}
				}
			}
		}
		
		return applicationId;
	}
}
