package cn.ffcs.zhsq.dataExchange.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import oracle.sql.TIMESTAMP;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.persistence.dataExchange.DataExchangeStatusTwoWayMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

@Service(value="dataExchangeStatusTwoWayServiceImpl")
public class DataExchangeStatusTwoWayServiceImpl implements
		IDataExchangeStatusTwoWayService {

	@Autowired
	private DataExchangeStatusTwoWayMapper dataExchangeStatusTwoWayMapper;
	
	@Autowired
	private IInvolvedPeopleService involvedPeopleService;
	
	//功能配置
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final String CLOSE_TASK_NAME = "结案";
	
	@Override
	public List<Map<String, Object>> findEventNewDocking(Map<String, Object> params){
		List<Map<String, Object>> resultMap = null;
		
		if(params != null){
			defaultDataExchange(params);
			
			resultMap = dataExchangeStatusTwoWayMapper.findEventNewDocking(params);
			this.formateEventOut(resultMap);
		}
		
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> findEventAppointed(Map<String, Object> params){
		List<Map<String, Object>> resultMap = null;
		
		if(params != null){
			String activityNames = "";	//当前事件环节名称
			String userIds = "";		//当前事件办理人员
			String groupIds = "";		//当前事件办理人员所属组织
			String orgIds = "";			//当前事件办理组织
			StringBuffer msgWarning = new StringBuffer("");
			
//			defaultDataExchange(params);
			
			//获取事件当前办理环节名称
			if(CommonFunctions.isNotBlank(params, "activityNames")){
				//activityNames = params.get("activityNames").toString();
			}else if(CommonFunctions.isNotBlank(params, "bizPlatform")){
				String bizPlatform = params.get("bizPlatform").toString();
				activityNames = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_CURRENT_ACTIVITY_NAME_TRIGGER+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}else if(CommonFunctions.isNotBlank(params, "activityNameTrigger")){
				String activityNameTrigger = params.get("activityNameTrigger").toString();
				activityNames = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, activityNameTrigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}
			//获取事件当前办理人员id
			if(CommonFunctions.isNotBlank(params, "userIds")){
				userIds = params.get("userIds").toString();
			}else if(CommonFunctions.isNotBlank(params, "bizPlatform")){
				String bizPlatform = params.get("bizPlatform").toString();
				userIds = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_CURRENT_USER_ID_TRIGGER+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}else if(CommonFunctions.isNotBlank(params, "userIdTrigger")){
				String userIdTrigger = params.get("userIdTrigger").toString();
				userIds = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, userIdTrigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}
			//获取事件当前办理人员所属组织id
			if(CommonFunctions.isNotBlank(params, "groupIds")){
				groupIds = params.get("groupIds").toString();
			}else if(CommonFunctions.isNotBlank(params, "bizPlatform")){
				String bizPlatform = params.get("bizPlatform").toString();
				groupIds = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_CURRENT_GROUP_ID_TRIGGER+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}else if(CommonFunctions.isNotBlank(params, "groupIdTrigger")){
				String groupIdTrigger = params.get("groupIdTrigger").toString();
				groupIds = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, groupIdTrigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}
			//获取事件当前办理组织id
			if(CommonFunctions.isNotBlank(params, "orgIds")){
				//orgIds = params.get("orgIds").toString();
			}else if(CommonFunctions.isNotBlank(params, "bizPlatform")){
				String bizPlatform = params.get("bizPlatform").toString();
				orgIds = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_CURRENT_ORG_ID_TRIGGER+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}else if(CommonFunctions.isNotBlank(params, "orgIdTrigger")){
				String orgIdTrigger = params.get("orgIdTrigger").toString();
				orgIds = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, orgIdTrigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}
			
			if(StringUtils.isNotBlank(activityNames)){
				params.put("activityNames", activityNames.split(","));
			}else{
				msgWarning.append("功能配置："+ConstantValue.THREE_PART_JOINT_UNOIN+"->"+ConstantValue.DEFAULT_CURRENT_ACTIVITY_NAME_TRIGGER+"_"+"未配置配置值！");
			}
			
			if(StringUtils.isNotBlank(userIds)){
				params.put("userIds", userIds.split(","));
			}else{
				msgWarning.append("功能配置："+ConstantValue.THREE_PART_JOINT_UNOIN+"->"+ConstantValue.DEFAULT_CURRENT_USER_ID_TRIGGER+"_"+"未配置配置值！");
				return resultMap;
			}
			
			if(StringUtils.isNotBlank(groupIds)){
				params.put("groupIds", groupIds.split(","));
			}else{
				msgWarning.append("功能配置："+ConstantValue.THREE_PART_JOINT_UNOIN+"->"+ConstantValue.DEFAULT_CURRENT_GROUP_ID_TRIGGER+"_"+"未配置配置值！");
				return resultMap;
			}
			
			if(StringUtils.isNotBlank(orgIds)){
				params.put("orgIds", orgIds.split(","));
			}else{
				msgWarning.append("功能配置："+ConstantValue.THREE_PART_JOINT_UNOIN+"->"+ConstantValue.DEFAULT_CURRENT_ORG_ID_TRIGGER+"_"+"未配置配置值！");
			}
			
			if(msgWarning.length() > 0){
				logger.warn("有如下警告信息："+msgWarning);
			}
			
			resultMap = dataExchangeStatusTwoWayMapper.findEventAppointed(params);
			formateEventOut(resultMap);
		}
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> findCloseEventAppointed(Map<String, Object> params){
		return dataExchangeStatusTwoWayMapper.findCloseEventAppointed(params);
	}
	
	@Override
	public List<Map<String, Object>> findSubTaskAppointed(Map<String, Object> params){
		return dataExchangeStatusTwoWayMapper.findSubTaskAppointed(params);
	}
	
	@Override
	public List<Map<String, Object>> findEventNewAppointed(Map<String, Object> params){
		List<Map<String, Object>> resultMap = null;
		
		if(params != null){
			String activityNames = "";	//当前事件环节名称
			String userIds = "";		//当前事件办理人员
			String groupIds = "";		//当前事件办理人员所属组织
			String orgIds = "";			//当前事件办理组织
			StringBuffer msgWarning = new StringBuffer("");
			
			defaultDataExchange(params);
			
			//获取事件当前办理环节名称
			if(CommonFunctions.isNotBlank(params, "activityNames")){
				//activityNames = params.get("activityNames").toString();
			}else if(CommonFunctions.isNotBlank(params, "bizPlatform")){
				String bizPlatform = params.get("bizPlatform").toString();
				activityNames = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_CURRENT_ACTIVITY_NAME_TRIGGER+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}else if(CommonFunctions.isNotBlank(params, "activityNameTrigger")){
				String activityNameTrigger = params.get("activityNameTrigger").toString();
				activityNames = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, activityNameTrigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}
			//获取事件当前办理人员id
			if(CommonFunctions.isNotBlank(params, "userIds")){
				userIds = params.get("userIds").toString();
			}else if(CommonFunctions.isNotBlank(params, "bizPlatform")){
				String bizPlatform = params.get("bizPlatform").toString();
				userIds = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_CURRENT_USER_ID_TRIGGER+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}else if(CommonFunctions.isNotBlank(params, "userIdTrigger")){
				String userIdTrigger = params.get("userIdTrigger").toString();
				userIds = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, userIdTrigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}
			//获取事件当前办理人员所属组织id
			if(CommonFunctions.isNotBlank(params, "groupIds")){
				groupIds = params.get("groupIds").toString();
			}else if(CommonFunctions.isNotBlank(params, "bizPlatform")){
				String bizPlatform = params.get("bizPlatform").toString();
				groupIds = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_CURRENT_GROUP_ID_TRIGGER+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}else if(CommonFunctions.isNotBlank(params, "groupIdTrigger")){
				String groupIdTrigger = params.get("groupIdTrigger").toString();
				groupIds = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, groupIdTrigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}
			//获取事件当前办理组织id
			if(CommonFunctions.isNotBlank(params, "orgIds")){
				//orgIds = params.get("orgIds").toString();
			}else if(CommonFunctions.isNotBlank(params, "bizPlatform")){
				String bizPlatform = params.get("bizPlatform").toString();
				orgIds = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_CURRENT_ORG_ID_TRIGGER+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}else if(CommonFunctions.isNotBlank(params, "orgIdTrigger")){
				String orgIdTrigger = params.get("orgIdTrigger").toString();
				orgIds = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, orgIdTrigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}
			
			if(StringUtils.isNotBlank(activityNames)){
				params.put("activityNames", activityNames.split(","));
			}else{
				msgWarning.append("功能配置："+ConstantValue.THREE_PART_JOINT_UNOIN+"->"+ConstantValue.DEFAULT_CURRENT_ACTIVITY_NAME_TRIGGER+"_"+"未配置配置值！");
			}
			
			if(StringUtils.isNotBlank(userIds)){
				params.put("userIds", userIds.split(","));
			}else{
				msgWarning.append("功能配置："+ConstantValue.THREE_PART_JOINT_UNOIN+"->"+ConstantValue.DEFAULT_CURRENT_USER_ID_TRIGGER+"_"+"未配置配置值！");
				return resultMap;
			}
			
			if(StringUtils.isNotBlank(groupIds)){
				params.put("groupIds", groupIds.split(","));
			}else{
				msgWarning.append("功能配置："+ConstantValue.THREE_PART_JOINT_UNOIN+"->"+ConstantValue.DEFAULT_CURRENT_GROUP_ID_TRIGGER+"_"+"未配置配置值！");
				return resultMap;
			}
			
			if(StringUtils.isNotBlank(orgIds)){
				params.put("orgIds", orgIds.split(","));
			}else{
				msgWarning.append("功能配置："+ConstantValue.THREE_PART_JOINT_UNOIN+"->"+ConstantValue.DEFAULT_CURRENT_ORG_ID_TRIGGER+"_"+"未配置配置值！");
			}
			
			if(msgWarning.length() > 0){
				logger.warn("有如下警告信息："+msgWarning);
			}
			
			resultMap = dataExchangeStatusTwoWayMapper.findEventNewAppointed(params);
			formateEventOut(resultMap);
		}
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> findTaskNewAppointed(Map<String, Object> params){
		List<Map<String, Object>> resultMap = null;
		
		if(params != null){
			defaultDataExchange(params);
			
			if(CommonFunctions.isBlank(params, "statusTask")){
				params.put("statusTask", IDataExchangeStatusService.STATUS_VALIDATE);
			}
			if(CommonFunctions.isBlank(params, "srcPlatformTask") && CommonFunctions.isBlank(params, "destPlatformTask")){
				//msgWrong.append("参数[srcPlatformTask]和[destPlatformTask]至少需要一个，请检查！");
			}else if(CommonFunctions.isBlank(params, "srcPlatformTask")){
				params.put("srcPlatformTask", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
			}else if(CommonFunctions.isBlank(params, "destPlatformTask")){
				params.put("destPlatformTask", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
			}
			if(CommonFunctions.isNotBlank(params, "isEventExclude")){
				String isEventExcludeStr = params.get("isEventExclude").toString();
				boolean isEventExclude = Boolean.valueOf(isEventExcludeStr);
				params.put("isEventExclude", isEventExclude);
			}else{
				params.put("isEventExclude", false);
			}
			
			resultMap = dataExchangeStatusTwoWayMapper.findTaskNewAppointed(params);
			
			formatTaskOut(resultMap);
		}
		
		return resultMap;
	}
	
	/**
	 * 增添默认属性
	 * @param params
	 */
	private void defaultDataExchange(Map<String, Object> params){
		StringBuffer msgWrong = new StringBuffer("");
		
		if(CommonFunctions.isBlank(params, "status")){
			params.put("status", IDataExchangeStatusService.STATUS_VALIDATE);
		}
		
		if(CommonFunctions.isBlank(params, "srcPlatform") && CommonFunctions.isBlank(params, "destPlatform")){
			msgWrong.append("参数[srcPlatform]和[destPlatform]至少需要一个，请检查！");
		}else if(CommonFunctions.isBlank(params, "srcPlatform")){
			params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		}else if(CommonFunctions.isBlank(params, "destPlatform")){
			params.put("destPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		}
		
		if(msgWrong.length() > 0){
			logger.error(msgWrong.toString());
		}
	}
	
	/**
	 * 事件格式化输出
	 * @param eventMap
	 */
	private void formateEventOut(Map<String, Object> eventMap){
		if(eventMap != null){
			Long eventId = -1L;
			
			if(CommonFunctions.isNotBlank(eventMap, "EVENT_ID")){
				String eventIdStr = eventMap.get("EVENT_ID").toString();
				
				try{
					eventId = Long.valueOf(eventIdStr);
				}catch(NumberFormatException e){
					eventId = -1L;
				}
				if(eventId > 0){
					InvolvedPeople involvedPeople = new InvolvedPeople();
					involvedPeople.setBizId(eventId);
					involvedPeople.setBizType(InvolvedPeople.BIZ_TYPE.ZHSQ_EVENT.getBizType());
					List<InvolvedPeople> involvedPeopleList = involvedPeopleService.findInvolvedPeopleList(involvedPeople);
					
					if(involvedPeopleList!=null && involvedPeopleList.size()>0){
						eventMap.put("INVOLVED_PEOPLE_LIST", involvedPeopleList);
					}
				}
			}
			if(CommonFunctions.isNotBlank(eventMap, "CREATOR_ID")){
				String creatorIdStr = eventMap.get("CREATOR_ID").toString();
				Long creatorId = -1L;
				
				try{
					creatorId = Long.valueOf(creatorIdStr);
				}catch(NumberFormatException e){
					creatorId = -1L;
				}
				
				UserBO user = userManageService.getUserInfoByUserId(creatorId);
				
				if(user != null){
					eventMap.put("CREATOR_NAME", user.getPartyName());
				}
			}
			if(CommonFunctions.isNotBlank(eventMap, "STATUS")) {
				String status = eventMap.get("STATUS").toString();
				if(ConstantValue.EVENT_STATUS_ARCHIVE.equals(status) || ConstantValue.EVENT_STATUS_END.equals(status)) {
					String instanceIdStr = eventDisposalWorkflowService.capInstanceIdByEventId(eventId);
					if(StringUtils.isNotBlank(instanceIdStr)) {
						List<Map<String, Object>> workflowTaskList = eventDisposalWorkflowService.queryProInstanceDetail(Long.valueOf(instanceIdStr), IEventDisposalWorkflowService.SQL_ORDER_DESC);
						for(Map<String, Object> task : workflowTaskList) {
							if(CLOSE_TASK_NAME.equals(task.get("TASK_NAME"))) {
								if(CommonFunctions.isNotBlank(task, "TRANSACTOR_NAME")){
									eventMap.put("CLOSER_NAME", task.get("TRANSACTOR_NAME"));
								}
								if(CommonFunctions.isNotBlank(task, "REMARKS")){
									eventMap.put("REMARKS", task.get("REMARKS"));
								}
								break;//取首个非当前环节的“结案”环节的办理人员
							}
						}
					}
				}
				
			}
			
			changeDate(eventMap, "HAPPEN_TIME");
			changeDate(eventMap, "CREATE_TIME");
			changeDate(eventMap, "FIN_TIME");
			changeDate(eventMap, "UPDATE_TIME");
			changeDate(eventMap, "HANDLE_DATE");
			changeDate(eventMap, "END_TIME");
		}
	}

	/**
	 * 格式化事件输出数据
	 * @param eventMapList
	 */
	private void formateEventOut(List<Map<String, Object>> eventMapList){
		if(eventMapList!=null && eventMapList.size()>0){
			for(Map<String, Object> eventMap : eventMapList){
				formateEventOut(eventMap);
			}
		}
		
	}
	
	/**
	 * 格式化任务输出数据
	 * @param taskInfoList
	 */
	private void formatTaskOut(List<Map<String, Object>> taskInfoList){
		if(taskInfoList!=null && taskInfoList.size()>0){
			for(Map<String, Object> taskInfo : taskInfoList){
				if(CommonFunctions.isNotBlank(taskInfo, "ORG_ID")){
					String orgIdStr = taskInfo.get("ORG_ID").toString();
					try{
						Long orgId = Long.valueOf(orgIdStr);
						OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
						if(orgInfo != null){
							taskInfo.put("ORG_CODE", orgInfo.getOrgCode());
							if(CommonFunctions.isBlank(taskInfo, "ORG_NAME")){
								taskInfo.put("ORG_NAME", orgInfo.getOrgName());
							}
						}
					}catch(NumberFormatException e){
						
					}
				}
				changeTIMESTAMP(taskInfo, "START_TIME");
				changeTIMESTAMP(taskInfo, "END_TIME");
			}
		}
	}
	
	/**
	 * 将TIMESTAMP字段转换为Date和String类型
	 * @param param
	 * @param key
	 */
	private void changeTIMESTAMP(Map<String, Object> param, String key){
		if(CommonFunctions.isNotBlank(param, key)){
			Timestamp startTimeStamp = null;
			try {
				TIMESTAMP startTimeStampSql = (TIMESTAMP)param.get(key);
				startTimeStamp = startTimeStampSql.timestampValue();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				startTimeStamp = (Timestamp)param.get(key);
			}
			String dateKey = key;
			String dateStrKey = key + "_STR";
			try {
				Date startTime = new Date(startTimeStamp.getTime());
				String startTimeStr = DateUtils.convertDateToString(startTime);
				
				param.put(dateKey, startTime);
				param.put(dateStrKey, startTimeStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将Date字段转换为String类型
	 * @param param
	 * @param dateKey
	 */
	private void changeDate(Map<String, Object> param, String dateKey){
		if(CommonFunctions.isNotBlank(param, dateKey)){
			Date date = (Date)param.get(dateKey);
			String dateStrKey = dateKey + "_STR";
			try {
				String dateStr = DateUtils.convertDateToString(date);
				param.put(dateStrKey, dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
}
