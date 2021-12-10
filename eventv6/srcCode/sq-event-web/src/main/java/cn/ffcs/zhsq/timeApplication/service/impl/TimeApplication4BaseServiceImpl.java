package cn.ffcs.zhsq.timeApplication.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.cookie.service.CacheService;
import cn.ffcs.sms.bo.SendResult;
import cn.ffcs.sms.service.SendSmsService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.mybatis.persistence.timeApplication.TimeApplicationMapper;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * 时限申请 基本实现
 * @author zhangls
 *
 */
@Service(value = "timeApplication4BaseService")
public class TimeApplication4BaseServiceImpl implements ITimeApplicationService {
	@Autowired
	private TimeApplicationMapper timeApplicationMapper;
	
	@Autowired
	private ITimeApplicationCheckService timeApplicationCheckService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private SendSmsService sendSmsService;
	
	//时限申请类别
	@SuppressWarnings("serial")
	private Map<String, String> applicationTypeMap = new HashMap<String, String>() {
		{
			for(ITimeApplicationService.APPLICATION_TYPE applicationType : ITimeApplicationService.APPLICATION_TYPE.values()) {
				put(applicationType.getValue(), applicationType.getName());
			}
		}
	};
	
	@Override
	public Map<String, Object> initTimeApplication4Event(Long eventId, String applicationType, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		Map<String, Object> initParams = new HashMap<String, Object>();
		String userOrgCode = userInfo.getOrgCode(), isShowSendMsgConfigStr = null;
		boolean isShowSendMsg = false, isShowSendMsgConfig = true;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		initParams.putAll(extraParam);
		
		if(StringUtils.isBlank(applicationType)) {
			if(CommonFunctions.isNotBlank(extraParam, "applicationType")) {
				applicationType = extraParam.get("applicationType").toString();
			}
		} else if(CommonFunctions.isBlank(extraParam, "applicationType")) {
			extraParam.put("applicationType", applicationType);
		}
		
		if(CommonFunctions.isBlank(initParams, "noteType")) {
			initParams.put("noteType", "timeApplicationNoteType"+applicationType);
		}
		
		isShowSendMsgConfigStr = funConfigurationService.turnCodeToValue(ConstantValue.SEND_MSG_AND_SMS_CFG, ConstantValue.IS_SHOW_SEND_MSG, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
		
		if(StringUtils.isNotBlank(isShowSendMsgConfigStr)) {
			isShowSendMsgConfig = Boolean.valueOf(isShowSendMsgConfigStr);
		}
		
		if(CommonFunctions.isNotBlank(extraParam, "isShowSendMsg")) {
			isShowSendMsg = Boolean.valueOf(extraParam.get("isShowSendMsg").toString());
		}
		
		initParams.put("isShowSendMsg", isShowSendMsg && isShowSendMsgConfig);
		
		if(ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_INVALID.toString().equals(applicationType)) {
			Integer invalidType = 1;
			
			if(CommonFunctions.isNotBlank(extraParam, "invalidType")) {
				try {
				invalidType = Integer.valueOf(extraParam.get("invalidType").toString());
				} catch(NumberFormatException e) {}
			}
			initParams.put("auditorType", ITimeApplicationCheckService.AUDITOR_TYPE.AUDITOR_NONE.toString());
			initParams.put("businessId", eventId);
			initParams.put("businessKeyId", eventId);
			initParams.put("isAutoAudit", true);
			initParams.put("serviceName", "timeApplicationCallback4InvalidEventService");
			initParams.put("defaultTipMsg", "事件删除成功！");
			initParams.put("isDuplicatedCheck", false);
			initParams.put("noteType", "invalidNodeType" + invalidType);//短信模板编号
		}
		
		return initParams;
	}
	
	@Override
	public Long saveTimeApplication(Map<String, Object> timeApplication) throws Exception {
		Long applicationId = -1L;
		StringBuffer msgWrong = new StringBuffer("");
		boolean isDuplicatedCheck = true;
		String applicationType = null;
		
		if(CommonFunctions.isBlank(timeApplication, "applicationType")) {
			msgWrong.append("缺少属性[applicationType]，请检查！");
		} else {
			applicationType = timeApplication.get("applicationType").toString();
		}
		if(CommonFunctions.isBlank(timeApplication, "businessId")) {
			msgWrong.append("缺少属性[businessId]，请检查！");
		}
		if(CommonFunctions.isBlank(timeApplication, "interval")) {
			if(ITimeApplicationService.APPLICATION_TYPE.EVENT_INSPECT.getValue().equals(applicationType)
			|| ITimeApplicationService.APPLICATION_TYPE.EVENT_DELAY.getValue().equals(applicationType)
			|| ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue().equals(applicationType)) {
				msgWrong.append("缺少属性[interval]，请检查！");
			}
		} else {
			Integer interval = 0;
			
			try {
				interval = Integer.valueOf(timeApplication.get("interval").toString());
			} catch(NumberFormatException e) {
				msgWrong.append("属性[interval]：【"+ timeApplication.get("interval") +"】 不是有效的数值！");
			}
			
			if(interval > 0) {
				timeApplication.put("interval", interval);
			} else {
				msgWrong.append("属性[interval]：【"+ timeApplication.get("interval") +"】 不是有效的时限！");
			}
		}
		if(CommonFunctions.isNotBlank(timeApplication, "isDuplicatedCheck")) {
			isDuplicatedCheck = Boolean.valueOf(timeApplication.get("isDuplicatedCheck").toString());
		}
		
		if(msgWrong.length() == 0 && (!isDuplicatedCheck || !isDuplicated(timeApplication))) {
			if(CommonFunctions.isBlank(timeApplication, "status")) {
				timeApplication.put("status", "1");
			}
			if(CommonFunctions.isBlank(timeApplication, "intervalUnit")) {
				timeApplication.put("intervalUnit", ITimeApplicationService.INTERVAL_UNIT.WEEK_DAY.toString());
			}
			
			if(timeApplicationMapper.insert(timeApplication) > 0) {
				boolean isAutoAudit = false;
				
				if(CommonFunctions.isNotBlank(timeApplication, "isAutoAudit")) {
					isAutoAudit = Boolean.valueOf(timeApplication.get("isAutoAudit").toString());
				}
				
				if(isAutoAudit) {
					String timeAppCheckStatus = ITimeApplicationCheckService.CHECK_STATUS.STATUS_INIT.getValue();
					
					applicationId = Long.valueOf(timeApplication.get("applicationId").toString());
					
					if(CommonFunctions.isNotBlank(timeApplication, "timeAppCheckStatus")) {
						timeAppCheckStatus = timeApplication.get("timeAppCheckStatus").toString();
					}
					
					if(!ITimeApplicationCheckService.CHECK_STATUS.STATUS_INIT.getValue().equals(timeAppCheckStatus) 
							&& CommonFunctions.isNotBlank(timeApplication, "reason") 
							&& CommonFunctions.isBlank(timeApplication, "checkAdvice")) {
						timeApplication.put("checkAdvice", timeApplication.get("reason"));
					}
					
					timeApplication.put("timeAppCheckStatus", timeAppCheckStatus);
							
					timeApplicationCheckService.saveTimeAppCheck(timeApplication);
				}
			}
		} else {
			throw new Exception(msgWrong.toString());
		}
		
		return applicationId;
	}

	@Override
	public boolean updateTimeApplicationById(Map<String, Object> timeApplication) throws Exception {
		boolean flag = false;
		StringBuffer msgWrong = new StringBuffer("");
		
		if(CommonFunctions.isBlank(timeApplication, "applicationId")) {
			msgWrong.append("缺少属性[applicationId]，请检查！");
		}
		
		if(msgWrong.length() == 0) {
			flag = timeApplicationMapper.update(timeApplication) > 0;
			
			if(flag && CommonFunctions.isNotBlank(timeApplication, "checkId")) {
				timeApplicationCheckService.updateTimeAppCheckById(timeApplication);
			}
				
		} else {
			throw new Exception(msgWrong.toString());
		}
		
		return flag;
	}

	@Override
	public boolean delTimeApplicationById(Long applicationId, Long delUserId) {
		boolean flag = false;
		
		if(applicationId != null && applicationId > 0) {
			flag = timeApplicationMapper.delete(applicationId, delUserId) > 0;
		}
		
		return flag;
	}

	@Override
	public Map<String, Object> findTimeAppliationById(Long applicationId, Map<String, Object> params) {
		Map<String, Object> timeApplication = null;
		boolean isTimeAppCheckNeeded = false;
		
		if(applicationId != null && applicationId > 0) {
			if(CommonFunctions.isNotBlank(params, "isTimeAppCheckNeeded")) {
				isTimeAppCheckNeeded = Boolean.valueOf(params.get("isTimeAppCheckNeeded").toString());
			}
			
			if(isTimeAppCheckNeeded) {
				params.put("applicationId", applicationId);
				
				List<Map<String, Object>> timeAppCheckList = this.findTimeApplicationList(params);
				if(timeAppCheckList != null && timeAppCheckList.size() > 0) {
					timeApplication = timeAppCheckList.get(0);
				}
			} else {
				timeApplication = timeApplicationMapper.findById(applicationId);
				
				if(timeApplication != null) {
					List<Map<String, Object>> timeApplicationList = new ArrayList<Map<String, Object>>();
					timeApplicationList.add(timeApplication);
					
					formatDataOut(timeApplicationList, params);
				}
			}
		}
		
		return timeApplication;
	}

	@Override
	public int findTimeApplicationCount(Map<String, Object> timeApplication) {
		return timeApplicationMapper.findCountByCriteria(timeApplication);
	}
	
	@Override
	public List<Map<String, Object>> findTimeApplicationList(
			Map<String, Object> timeApplication) {
		List<Map<String, Object>> timeApplicationList = timeApplicationMapper.findApplicationList(timeApplication);
		
		formatDataOut(timeApplicationList, timeApplication);
		formatTimeAppCheckOut(timeApplicationList, timeApplication);
		
		return timeApplicationList;
	}

	@Override
	public EUDGPagination findTimeApplicationPagination(int pageNo,
			int pageSize, Map<String, Object> timeApplication) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = 0;
		List<Map<String, Object>> timeApplicationList = new ArrayList<Map<String, Object>>();
		
		count = timeApplicationMapper.findCountByCriteria(timeApplication);
		
		if(count > 0) {
			timeApplicationList = timeApplicationMapper.findPageListByCriteria(timeApplication, rowBounds);
		}
		
		EUDGPagination timeApplicationPagination = new EUDGPagination(count, timeApplicationList);
		
		return timeApplicationPagination;
	}
	
	@Override
	public int findEventTimeAppCount(Map<String, Object> timeApplication) throws Exception {
		int count = 0;
		
		formatParamIn4EventTimeApp(timeApplication);

		count = timeApplicationMapper.findEventTimeAppCountByCriteria(timeApplication);
		
		return count;
	}
	
	@Override
	public EUDGPagination findEventTimeAppPagination(int pageNo, int pageSize,
			Map<String, Object> timeApplication) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = 0;
		String userOrgCode = null;
		List<Map<String, Object>> timeApplicationList = new ArrayList<Map<String, Object>>();
		
		formatParamIn4EventTimeApp(timeApplication);

		count = timeApplicationMapper.findEventTimeAppCountByCriteria(timeApplication);
		
		if(count > 0) {
			timeApplicationList = timeApplicationMapper.findEventTimeAppListByCriteria(timeApplication, rowBounds);
			
			if(CommonFunctions.isNotBlank(timeApplication, "userOrgCode")) {
				userOrgCode = timeApplication.get("userOrgCode").toString();
			}
			
			formatDataOut(timeApplicationList, timeApplication);
			formatTimeAppCheckOut(timeApplicationList, timeApplication);
			formatEventDataOut(timeApplicationList, userOrgCode);
		}
		
		EUDGPagination timeApplicationPagination = new EUDGPagination(count, timeApplicationList);
		
		return timeApplicationPagination;
	}
	
	/**
	 * 事件时限申请相关参数格式化
	 * @param timeApplication
	 */
	protected void formatParamIn4EventTimeApp(Map<String, Object> timeApplication) {
		int listType = 1;
		
		if(CommonFunctions.isNotBlank(timeApplication, "listType")) {
			listType = Integer.valueOf(timeApplication.get("listType").toString());
		}
		
		switch(listType) {
			case 1: {//我的审核列表
				if(CommonFunctions.isBlank(timeApplication, "auditorId") && CommonFunctions.isNotBlank(timeApplication, "operateUserId")) {
					timeApplication.put("auditorId", timeApplication.get("operateUserId"));
				}
				if(CommonFunctions.isBlank(timeApplication, "auditorOrgId") && CommonFunctions.isNotBlank(timeApplication, "operateUserOrgId")) {
					timeApplication.put("auditorOrgId", timeApplication.get("operateUserOrgId"));
				}
				break;
			}
			case 2: {//我的时限申请
				if(CommonFunctions.isBlank(timeApplication, "applicationTypeList")) {
					List<String> applicationTypeList = new ArrayList<String>();
					
					applicationTypeList.add(ITimeApplicationService.APPLICATION_TYPE.EVENT_DELAY.toString());
					applicationTypeList.add(ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.toString());
					
					timeApplication.put("applicationTypeList", applicationTypeList);
				}
				
				if(CommonFunctions.isBlank(timeApplication, "creatorId") && CommonFunctions.isNotBlank(timeApplication, "operateUserId")) {
					timeApplication.put("creatorId", timeApplication.get("operateUserId"));
				}
				break;
			}
			case 31: {}
			case 3: {//事件删除审核
				List<String> applicationTypeList = new ArrayList<String>();
				
				applicationTypeList.add(ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_INVALID.toString());
				
				timeApplication.put("applicationTypeList", applicationTypeList);
				
				break;
			}
			case 33: {}
			case 32: {//事件挂起申请
				List<String> applicationTypeList = new ArrayList<String>();
				
				applicationTypeList.add(ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_SUSPEND.toString());
				
				timeApplication.put("applicationTypeList", applicationTypeList);
				
				break;
			}
			case 41: {}
			case 4: {//事件删除列表
				List<String> applicationTypeList = new ArrayList<String>(),
						 	 timeAppCheckStatusList = new ArrayList<String>();
			
				applicationTypeList.add(ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_INVALID.toString());
				
				timeAppCheckStatusList.add(ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.toString());
				
				timeApplication.put("eventStatus", ConstantValue.STATUS_DEL_AND_RETURN_BACK);
				timeApplication.put("applicationTypeList", applicationTypeList);
				timeApplication.put("timeAppCheckStatusList", timeAppCheckStatusList);
				
				break;
			}
			case 43: {}
			case 42: {//已挂起事件列表
				List<String> applicationTypeList = new ArrayList<String>(),
						 	 timeAppCheckStatusList = new ArrayList<String>();
			
				applicationTypeList.add(ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_SUSPEND.toString());
				
				timeAppCheckStatusList.add(ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.toString());
				
				timeApplication.put("eventStatus", ConstantValue.STATUS_DEL_AND_RETURN_BACK);
				timeApplication.put("applicationTypeList", applicationTypeList);
				timeApplication.put("timeAppCheckStatusList", timeAppCheckStatusList);
				
				break;
			}
			case 5: {//事件预处理审核列表
				
				timeApplication.put("applicationType", ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_PREPRESS.toString());
				
				break;
			}
			case 6: {//核验不通过列表
				List<String> applicationTypeList = new ArrayList<String>(),
						 	 timeAppCheckStatusList = new ArrayList<String>();
			
				applicationTypeList.add(ITimeApplicationService.APPLICATION_TYPE.EVENT_CHECK.toString());
				
				timeAppCheckStatusList.add(ITimeApplicationCheckService.CHECK_STATUS.STATUS_FAIL.toString());
				
				timeApplication.put("eventStatus", ConstantValue.STATUS_DEL_AND_RETURN_BACK);
				timeApplication.put("applicationTypeList", applicationTypeList);
				timeApplication.put("timeAppCheckStatusList", timeAppCheckStatusList);
				
				break;
			}
			default: {
				throw new IllegalArgumentException("无效的列表类型listType：【" + listType + "】！");
			}
		}
		
		if(CommonFunctions.isNotBlank(timeApplication, "eventStatusArray")) {
			//do nothing
		} else if(CommonFunctions.isNotBlank(timeApplication, "eventStatus")) {
			String eventStatus = timeApplication.get("eventStatus").toString();
			
			if(eventStatus.contains(",")) {
				timeApplication.put("eventStatusArray", eventStatus.split(","));
				timeApplication.remove("eventStatus");
			}
		}
		
		//为了让申请类型能够通过外部进行更调
		if(CommonFunctions.isNotBlank(timeApplication, "applicationType")) {
			String applicationType = timeApplication.get("applicationType").toString();
			
			if(applicationType.contains(",")) {
				timeApplication.put("applicationTypeList", Arrays.asList(applicationType.split(",")));
				timeApplication.remove("applicationType");
			}
		}
		
		if(CommonFunctions.isNotBlank(timeApplication, "isCapApplicant")) {//强制转换为boolean类型
			timeApplication.put("isCapApplicant", Boolean.valueOf(timeApplication.get("isCapApplicant").toString()));
		}
		
		if(CommonFunctions.isBlank(timeApplication, "infoOrgCode") && CommonFunctions.isNotBlank(timeApplication, "startInfoOrgCode")) {
			boolean isJurisdictionQuery = true;
			
			if(CommonFunctions.isNotBlank(timeApplication, "isJurisdictionQuery")) {
				isJurisdictionQuery = Boolean.valueOf(timeApplication.get("isJurisdictionQuery").toString());
			}
			
			if(isJurisdictionQuery) {
				timeApplication.put("infoOrgCode", timeApplication.get("startInfoOrgCode"));
			}
		}
		
		if(CommonFunctions.isNotBlank(timeApplication, "infoOrgCode")) {
			String infoOrgCode = timeApplication.get("infoOrgCode").toString();
			boolean isEnableBuildScope = true;
			
			if(CommonFunctions.isNotBlank(timeApplication, "isEnableBuildScope")) {
				isEnableBuildScope = Boolean.valueOf(timeApplication.get("isEnableBuildScope").toString());
			}
			
			//功能编码为：BUILD_SCOPE_SETTING，每个只能平台配置一个
			if(isEnableBuildScope && infoOrgCode.equals(cacheService.getBuildScopeSettingCode())) {
				timeApplication.remove("infoOrgCode");
			}
		}
		
		if(CommonFunctions.isNotBlank(timeApplication, "isCreatedBySelf") && CommonFunctions.isBlank(timeApplication, "creatorId") && CommonFunctions.isNotBlank(timeApplication, "operateUserId")) {
			boolean isCreatedBySelf = Boolean.valueOf(timeApplication.get("isCreatedBySelf").toString());
			
			if(isCreatedBySelf) {
				timeApplication.put("creatorId", timeApplication.get("operateUserId"));
			}
		}
		
		if(CommonFunctions.isNotBlank(timeApplication, "eventCollectWay")) {
			String eventCollectWay = timeApplication.get("eventCollectWay").toString();
			
			if(eventCollectWay.contains(",")) {
				timeApplication.put("eventCollectWayArray", eventCollectWay.split(","));
				timeApplication.remove("eventCollectWay");
			}
		}
		
		if(CommonFunctions.isNotBlank(timeApplication, "eventSource")) {
			String eventSource = timeApplication.get("eventSource").toString();
			
			if(eventSource.contains(",")) {
				timeApplication.put("eventSourceArray", eventSource.split(","));
				timeApplication.remove("eventSource");
			}
		}
	}
	
	/**
	 * 判重验证
	 * @param timeApplication
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	protected boolean isDuplicated(Map<String, Object> timeApplication) throws Exception {
		boolean isDuplicated = false;
		
		if(timeApplication != null && !timeApplication.isEmpty()) {
			String applicationType = null, businessId = null;
			
			if(CommonFunctions.isNotBlank(timeApplication, "applicationType")) {
				applicationType = timeApplication.get("applicationType").toString();
			}
			if(CommonFunctions.isNotBlank(timeApplication, "businessId")) {
				businessId = timeApplication.get("businessId").toString();
			}
			
			if(StringUtils.isNotBlank(applicationType) && StringUtils.isNotBlank(businessId)) {
				List<Map<String, Object>> timeAppMapList = null;
				List<String> statusList = null;
				Map<String, Object> timeAppMap = new HashMap<String, Object>(),
									timeAppCheckMap = null;
				int timeAppMapListSize = 0;
				Long applicationId = -1L;
				
				timeAppMap.put("applicationType", applicationType);
				timeAppMap.put("businessId", businessId);
				timeAppMap.put("isTransferUser", true);
				timeAppMap.put("isTransferOrg", true);
				
				if(CommonFunctions.isNotBlank(timeApplication, "timeAppCheckStatusList")) {
					Object timeAppCheckStatusObject = timeApplication.get("timeAppCheckStatusList");
					
					if(timeAppCheckStatusObject instanceof List) {
						statusList = (List<String>) timeApplication.get("timeAppCheckStatusList");
					}
				} else {
					statusList = new ArrayList<String>();
					
					if(ITimeApplicationService.APPLICATION_TYPE.EVENT_DELAY.getValue().equals(applicationType) || 
							ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue().equals(applicationType)) {
						statusList.add(ITimeApplicationCheckService.CHECK_STATUS.STATUS_FAIL.getValue());
					}
					
					statusList.add(ITimeApplicationCheckService.CHECK_STATUS.STATUS_INIT.getValue());
				}
				
				timeAppMap.put("timeAppCheckStatusList", statusList);
				
				timeAppMapList = this.findTimeApplicationList(timeAppMap);
				
				if(timeAppMapList != null) {
					timeAppMapListSize = timeAppMapList.size();
					
					if(timeAppMapListSize > 0) {
						timeAppCheckMap = timeAppMapList.get(0);
						
						if(CommonFunctions.isNotBlank(timeApplication, "applicationId")) {
							isDuplicated = timeAppMapListSize > 1;
							applicationId = Long.valueOf(timeApplication.get("applicationId").toString());
							
							if(!isDuplicated && timeAppMapListSize == 1) {
								isDuplicated = !applicationId.equals(timeAppCheckMap.get("applicationId"));
							}
						} else {
							isDuplicated = timeAppMapListSize > 0;
							
							if(isDuplicated) {
								applicationId = Long.valueOf(timeAppCheckMap.get("applicationId").toString());
							}
						}
					}
				}
				
				if(isDuplicated) {
					StringBuffer msgWrong = new StringBuffer("");
					
					msgWrong.append("该记录已有申请：【").append(applicationTypeMap.get(applicationType)).append("】");
						
					if(CommonFunctions.isNotBlank(timeAppCheckMap, "auditorName")
							|| CommonFunctions.isNotBlank(timeAppCheckMap, "auditorOrgName")) {
						msgWrong.append("，审核人员为：");
						
						if(CommonFunctions.isNotBlank(timeAppCheckMap, "auditorName")) {
							msgWrong.append(timeAppCheckMap.get("auditorName"));
						}
						if(CommonFunctions.isNotBlank(timeAppCheckMap, "auditorOrgName")) {
							msgWrong.append("【").append(timeAppCheckMap.get("auditorOrgName")).append("】");
						}
					}
					if(CommonFunctions.isNotBlank(timeAppCheckMap, "timeAppCheckStatusName")) {
						msgWrong.append("，审核状态为：【").append(timeAppCheckMap.get("timeAppCheckStatusName")).append("】");
					}
					
					msgWrong.append("！");
					
					throw new Exception(msgWrong.toString());
				}
			}
		}
		
		return isDuplicated;
	}
	
	/**
	 * 发送短信
	 * @param params
	 * 	必填参数
	 * 		smsContent		短信内容
	 * 		otherMobileNums	短信接收号码，多个值使用英文逗号连接
	 * 		userOrgCode		组织编码
	 * 		creatorId		操作用户id，优先于updaterId生效
	 * 		updaterId		操作用户id
	 * @return
	 */
	protected boolean sendSMS(Map<String, Object> params) throws Exception {
		String smsContent = null;
		String[] otherMobileNumArray = null;
		Set<String> mobileNumSet = null;
		boolean sendSMSFlag = false;
		
		if(CommonFunctions.isBlank(params, "smsContent")) {
			throw new IllegalArgumentException("缺少短信发送内容【smsContent】！");
		}
		if(CommonFunctions.isBlank(params, "otherMobileNums")) {
			throw new IllegalArgumentException("缺少短信接收号码【otherMobileNums】！");
		}
		
		smsContent = params.get("smsContent").toString();
		otherMobileNumArray = params.get("otherMobileNums").toString().split(",");
		mobileNumSet = new HashSet<String>();
		
		//去除重复的接收号码
		for(String phoneNum : otherMobileNumArray) {
			if(StringUtils.isNotBlank(phoneNum)) {
				mobileNumSet.add(phoneNum.trim());
			}
		}
		
		if(mobileNumSet.size() > 0) {
			Long userId = null;
			String userOrgCode = null;
			String[] mobileNumArray = mobileNumSet.toArray(new String[mobileNumSet.size()]);
			
			if(CommonFunctions.isNotBlank(params, "creatorId")) {
				userId = Long.valueOf(params.get("creatorId").toString());
			} else if(CommonFunctions.isNotBlank(params, "updaterId")) {
				userId = Long.valueOf(params.get("updaterId").toString());
			}
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			}
			
			try {
				SendResult result = sendSmsService.sendSms(ConstantValue.SMS_PLATFORM_FLAG,
										userId, userOrgCode,
										mobileNumArray, smsContent, SendSmsService.TYPE_SMS, null,
										null);
				if(result != null) {
					sendSMSFlag = result.getCode() == 0;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return sendSMSFlag;
	}
	
	/**
	 * 格式化时限申请数据
	 * @param timeApplicationList
	 * @param params
	 * 			isTransferDict	是否进行字典转换，true为是，默认为false
	 * 			userOrgCode		组织编码，isTransferDict为true时，必填
	 */
	private void formatDataOut(List<Map<String, Object>> timeApplicationList, Map<String, Object> params) {
		if(timeApplicationList != null && timeApplicationList.size() > 0) {
			StringBuffer intervalName = new StringBuffer("");
			String intervalUnitName = null, userOrgCode = null;
			//时限单位
			Map<String, String> intervalUnitMap = new HashMap<String, String>();
			Map<String, Object> eventTypeMap = null;
			boolean isTransferDict = false;
			List<BaseDataDict> eventTypeDictList = null, 
							   inspectDegreeDictList = null,
							   inspectTypeDictList = null;
			
			if(CommonFunctions.isNotBlank(params, "isTransferDict")) {
				isTransferDict = Boolean.valueOf(params.get("isTransferDict").toString());
			}
			if(isTransferDict) {
				Map<String, Object> dictMap = new HashMap<String, Object>();
				String INSPECT_DEGEREE_PCODE = "B191", INSPECT_TYPE_PCODE = "B197"; 
				
				if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
					userOrgCode = params.get("userOrgCode").toString();
				} else {
					throw new IllegalArgumentException("字典转换，缺少组织编码[userOrgCode]，请检查！");
				}
				
				dictMap.put("orgCode", userOrgCode);
				dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
				
				eventTypeDictList = baseDictionaryService.findDataDictListByCodes(dictMap);
				
				inspectDegreeDictList = baseDictionaryService.getDataDictListOfSinglestage(INSPECT_DEGEREE_PCODE, userOrgCode);
				inspectTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(INSPECT_TYPE_PCODE, userOrgCode);
			}
			
			for(ITimeApplicationService.INTERVAL_UNIT checkStatus : ITimeApplicationService.INTERVAL_UNIT.values()) {
				intervalUnitMap.put(checkStatus.getValue(), checkStatus.getName());
			}
			
			for(Map<String, Object> timeApplication : timeApplicationList) {
				intervalName.setLength(0);
				intervalUnitName = null;
				
				if(CommonFunctions.isNotBlank(timeApplication, "createTime")) {
					timeApplication.put("createTimeStr", DateUtils.formatDate((Date)timeApplication.get("createTime"), DateUtils.PATTERN_24TIME));
				}
				
				if(CommonFunctions.isNotBlank(timeApplication, "applicationType")) {
					timeApplication.put("applicationTypeName", applicationTypeMap.get(timeApplication.get("applicationType")));
				}
				
				if(CommonFunctions.isNotBlank(timeApplication, "interval")) {
					intervalName.append(timeApplication.get("interval"));
					
					if(CommonFunctions.isNotBlank(timeApplication, "intervalUnit")) {
						intervalUnitName = intervalUnitMap.get(timeApplication.get("intervalUnit"));
					}
					
					if(StringUtils.isNotBlank(intervalUnitName)) {
						timeApplication.put("intervalUnitName", intervalUnitName);
						intervalName.append("(").append(intervalUnitName).append(")");
					}
					
					timeApplication.put("intervalName", intervalName.toString());
				}
				
				if(CommonFunctions.isNotBlank(timeApplication, "eventTypePre")) {
					eventTypeMap = DataDictHelper.capMultilevelDictInfo(timeApplication.get("eventTypePre").toString(), ConstantValue.BIG_TYPE_PCODE, eventTypeDictList);
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
						timeApplication.put("eventClassPre", eventTypeMap.get("dictFullPath"));
					}
				}
				if(CommonFunctions.isNotBlank(timeApplication, "eventTypeAfter")) {
					eventTypeMap = DataDictHelper.capMultilevelDictInfo(timeApplication.get("eventTypeAfter").toString(), ConstantValue.BIG_TYPE_PCODE, eventTypeDictList);
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
						timeApplication.put("eventClassAfter", eventTypeMap.get("dictFullPath"));
					}
				}
				if(CommonFunctions.isNotBlank(timeApplication, "inspectDegree")) {
					DataDictHelper.setDictValueForField(timeApplication, "inspectDegree", "inspectDegreeName", inspectDegreeDictList);
				}
				if(CommonFunctions.isNotBlank(timeApplication, "inspectType")) {
					DataDictHelper.setDictValueForField(timeApplication, "inspectType", "inspectTypeName", inspectTypeDictList);
				}
			}
		}
	}
	
	/**
	 * 格式化审核
	 * @param timeAppCheckList
	 * @param params
	 */
	private void formatTimeAppCheckOut(List<Map<String, Object>> timeAppCheckList, Map<String, Object> params) {
		if(timeAppCheckList != null && timeAppCheckList.size() > 0) {
			Long auditorId = -1L, auditorOrgId = -1L;
			boolean isTransferUser = false, isTransferOrg = false;
			UserBO userBO = null;
			OrgSocialInfoBO orgInfo = null;
			//时限申请审核状态
			Map<String, String> checkStatusMap = new HashMap<String, String>();
			Integer listType = 0;
			
			if(CommonFunctions.isNotBlank(params, "listType")) {
				try {
					listType = Integer.valueOf(params.get("listType").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			switch(listType) {
				case 3: {//事件删除审核列表
					checkStatusMap.put("3", "待复核");
					checkStatusMap.put("1", "已删除");
					checkStatusMap.put("2", "已恢复");
					break;
				}
				case 31: {//事件督查删除审核列表
					checkStatusMap.put("3", "待复核");
					checkStatusMap.put("1", "已督查");
					checkStatusMap.put("2", "已恢复");
					break;
				}
				case 32: {//事件挂起审核列表
					checkStatusMap.put("3", "待复核");
					checkStatusMap.put("1", "已挂起");
					checkStatusMap.put("2", "已恢复");
					break;
				}
				case 33: {//事件无效审核列表
					checkStatusMap.put("3", "待复核");
					checkStatusMap.put("1", "已无效");
					checkStatusMap.put("2", "已恢复");
					break;
				}
				default: {
					for(ITimeApplicationCheckService.CHECK_STATUS checkStatus : ITimeApplicationCheckService.CHECK_STATUS.values()) {
						checkStatusMap.put(checkStatus.getValue(), checkStatus.getName());
					}
					break;
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "isTransferUser")) {
				isTransferUser = Boolean.valueOf(params.get("isTransferUser").toString());
			}
			if(CommonFunctions.isNotBlank(params, "isTransferOrg")) {
				isTransferOrg = Boolean.valueOf(params.get("isTransferOrg").toString());
			}
			
			for(Map<String, Object> timeAppCheck : timeAppCheckList) {
				if(isTransferUser) {
					auditorId = -1L;
					
					if(CommonFunctions.isNotBlank(timeAppCheck, "auditorId")) {
						auditorId = Long.valueOf(timeAppCheck.get("auditorId").toString());
					}
					
					if( auditorId > 0) {
						userBO = userManageService.getUserInfoByUserId(auditorId);
						if(userBO != null) {
							timeAppCheck.put("auditorName", userBO.getPartyName());
						}
					}
				}
				
				if(isTransferOrg) {
					auditorOrgId = -1L;
					
					if(CommonFunctions.isNotBlank(timeAppCheck, "auditorOrgId")) {
						auditorOrgId = Long.valueOf(timeAppCheck.get("auditorOrgId").toString());
					}
					
					if(auditorOrgId > 0) {
						orgInfo = orgSocialInfoService.findByOrgId(auditorOrgId);
						if(orgInfo != null) {
							timeAppCheck.put("auditorOrgName", orgInfo.getOrgName());
						}
					}
				}
				
				if(CommonFunctions.isNotBlank(timeAppCheck, "timeAppCheckStatus")) {
					timeAppCheck.put("timeAppCheckStatusName", checkStatusMap.get(timeAppCheck.get("timeAppCheckStatus").toString()));
				}
			}
		}
	}
	
	/**
	 * 格式化输出数据
	 * @param timeApplicationList
	 * @param orgCode			组织编码
	 */
	private void formatEventDataOut(List<Map<String, Object>> timeApplicationList, String orgCode) {
		if(timeApplicationList != null && timeApplicationList.size() > 0) {
			Map<String, Object> dictMap = new HashMap<String, Object>();
			dictMap.put("orgCode", orgCode);
			dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
			
			List<BaseDataDict> eventTypeDict = baseDictionaryService.findDataDictListByCodes(dictMap),
							   statusDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.STATUS_PCODE, orgCode),
							   subStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SUB_STATUS_PCODE, orgCode);
			String eventStatus = null,
				   eventSubStatus = null, eventSubStatusName = null;
			
			for(Map<String, Object> timeApplication : timeApplicationList) {
				if(CommonFunctions.isNotBlank(timeApplication, "happenTime") && CommonFunctions.isBlank(timeApplication, "happenTimeStr")) {
					timeApplication.put("happenTimeStr", DateUtils.formatDate((Date)timeApplication.get("happenTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(timeApplication, "handleDate") && CommonFunctions.isBlank(timeApplication, "handleDateStr")) {
					timeApplication.put("handleDateStr", DateUtils.formatDate((Date)timeApplication.get("handleDate"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(timeApplication, "eventCreateTime") && CommonFunctions.isBlank(timeApplication, "eventCreateTimeStr")) {
					timeApplication.put("eventCreateTimeStr", DateUtils.formatDate((Date)timeApplication.get("eventCreateTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(timeApplication, "type") && eventTypeDict != null) {
					Map<String, Object> eventTypeMap = DataDictHelper.capMultilevelDictInfo(timeApplication.get("type").toString(), ConstantValue.BIG_TYPE_PCODE, eventTypeDict);
					
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictName")) {
						timeApplication.put("typeName", eventTypeMap.get("dictName"));
					}
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
						timeApplication.put("eventClass", eventTypeMap.get("dictFullPath"));
					}
				}
				
				if(CommonFunctions.isNotBlank(timeApplication, "eventStatus")) {// 事件状态
					eventStatus = timeApplication.get("eventStatus").toString();
					for(BaseDataDict statusDict : statusDictList) {
						if(eventStatus.equals(statusDict.getDictGeneralCode())) {
							timeApplication.put("eventStatusName", statusDict.getDictName());
							break;
						}
					}
				}
				if(CommonFunctions.isNotBlank(timeApplication, "eventSubStatus")) {
					eventSubStatus = timeApplication.get("eventSubStatus").toString();
					eventSubStatusName = null;
					
					for(BaseDataDict subStatusDict : subStatusDictList) {
						if(eventSubStatus.equals(subStatusDict.getDictGeneralCode())) {
							eventSubStatusName = subStatusDict.getDictName();
							break;
						}
					}
					
					if(CommonFunctions.isNotBlank(timeApplication, "eventStatusName")) {
						if(ConstantValue.REJECT_SUB_STATUS.equals(eventSubStatus)) {
							timeApplication.put("eventStatusName", eventSubStatusName);
						} else {
							timeApplication.put("eventStatusName", timeApplication.get("eventStatusName") + "-" + eventSubStatusName);
						}
					}
				}
				
				//去除网格全路径中的顶级路径
				if(CommonFunctions.isNotBlank(timeApplication, "gridPath")) {
					timeApplication.put("gridPath", CommonFunctions.replaceScopePath(timeApplication.get("gridPath").toString(), cacheService));
				}
			}
		}
	}

}
