package cn.ffcs.zhsq.dataExchange.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.common.EUDGPagination;
import cn.ffcs.shequ.base.service.IUserService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.event.service.IEventProcessService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.persistence.dataExchange.DataExchangeMapper;
import cn.ffcs.zhsq.mybatis.persistence.dataExchange.DataExchangeStatusMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;

@Service(value="dataExchangeStatusServiceImpl")
public class DataExchangeStatusServiceImpl implements
		IDataExchangeStatusService {
	//功能配置
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private DataExchangeStatusMapper dataExchangeStatusMapper;

	@Autowired
	private DataExchangeMapper dataExchangeMapper;
	
	@Autowired
	private IEventProcessService eventProcessService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public List<Map<String, Object>> findReportEvent(Map<String, Object> params){
		return dataExchangeStatusMapper.findReportEvent(params);
	}

	@Override
	public List<Map<String, Object>> findRejectEvent(Map<String, Object> params){
		return dataExchangeStatusMapper.findRejectEvent(params);
	}

	@Override
	public List<Map<String, Object>> findTaskEvent(Map<String, Object> params){
		return dataExchangeStatusMapper.findTaskEvent(params);
	}

	@Override
	public List<Map<String, Object>> findEvaEvent(Map<String, Object> params){
		return dataExchangeStatusMapper.findEvaEvent(params);
	}

	@Override
	public List<Map<String, Object>> findCloseEvent(Map<String, Object> params){
		return dataExchangeStatusMapper.findCloseEvent(params);
	}
	
	@Override
	public List<Map<String, Object>> findFileEvent(Map<String, Object> params){
		return dataExchangeStatusMapper.findFileEvent(params);
	}
	
	@Override
	public Map<String, Object> findMatterEvent(Map<String, Object> params){
		return dataExchangeStatusMapper.findMatterEvent(params);
	}
	
	
	@Override
	public Long countScore(Map<String, Object> params){
		return dataExchangeStatusMapper.countScore(params);
	}
	
	@Override
	public List<Map<String, Object>> findCloseCheckEvent(Map<String, Object> params){
		return dataExchangeStatusMapper.findCloseCheckEvent(params);
	}

	@Override
	public List<Map<String, Object>> findCheckEvent(Map<String, Object> params){
		return dataExchangeStatusMapper.findCheckEvent(params);
	}
	@Override
	public EUDGPagination findDataExchangePagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo<1?1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		int count = dataExchangeStatusMapper.findCountByCriteria(params);
		List<DataExchangeStatus> list = dataExchangeStatusMapper.findPageListByCriteria(params, rowBounds);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}

	@Override
	public Long findEventIdByOppCode(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo<1?1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		List<DataExchangeStatus> list = dataExchangeStatusMapper.findPageListByCriteria(params, rowBounds);
		if(list.size()==0||list==null){
			return -1L;
		}
		return Long.parseLong(list.get(0).getOwnSideBizCode());
	}

	@Override
	public Long save(DataExchangeStatus dataExchangeStatus) {
		int result = dataExchangeStatusMapper.insert(dataExchangeStatus);
		return result > 0 ? dataExchangeStatus.getInterId() : 0L;
	}
	
	@Override
	public Long saveDataExchangeStatus(DataExchangeStatus dataExchangeStatus) {
		Long interId = -1L;
		int result = 0;
		
		defaultDataExchange(dataExchangeStatus);//设置默认属性
		if(!isDataExchangeExists(dataExchangeStatus)){
			result = dataExchangeStatusMapper.insert(dataExchangeStatus);
		}else if(isDataExchangeValidate(dataExchangeStatus)){
			interId = 0L;
		}
		
		if(result > 0){
			interId = dataExchangeStatus.getInterId();
			dataExchangeStatusMapper.insertClob(dataExchangeStatus);
		}
		
		return interId;
	}


	@Override
	public Long saveTaskExchangeFromThree(String ownSideBizCode, String oppoSideBizCode, String bizPlatform, String xmlData){
		Long interId = -1L;
		DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
		dataExchangeStatus.setSrcPlatform(bizPlatform);
		dataExchangeStatus.setDestPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
		dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
		dataExchangeStatus.setOwnSideBizCode(ownSideBizCode);
		dataExchangeStatus.setOppoSideBizCode(oppoSideBizCode);
		dataExchangeStatus.setXmlData(xmlData);
		interId = this.saveDataExchangeStatus(dataExchangeStatus);
		return interId;
	}

	@Override
	public Long saveTaskExchangeToThree(String ownSideBizCode, String oppoSideBizCode, String bizPlatform, String xmlData){
		Long interId = -1L;
		DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
		dataExchangeStatus.setSrcPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		dataExchangeStatus.setDestPlatform(bizPlatform);
		dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
		dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
		dataExchangeStatus.setOwnSideBizCode(ownSideBizCode);
		dataExchangeStatus.setOppoSideBizCode(oppoSideBizCode);
		dataExchangeStatus.setXmlData(xmlData);
		interId = this.saveDataExchangeStatus(dataExchangeStatus);
		return interId;
	}


	@Override
	public Long saveEventExchangeFromThree(String ownSideBizCode, String oppoSideBizCode, String bizPlatform, String xmlData){
		Long interId = -1L;
		DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
		dataExchangeStatus.setSrcPlatform(bizPlatform);
		dataExchangeStatus.setDestPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeStatus.setOwnSideBizCode(ownSideBizCode);
		dataExchangeStatus.setOppoSideBizCode(oppoSideBizCode);
		dataExchangeStatus.setXmlData(xmlData);
		interId = this.saveDataExchangeStatus(dataExchangeStatus);
		return interId;
	}

	@Override
	public Long saveEventExchangeToThree(String ownSideBizCode, String oppoSideBizCode, String bizPlatform, String xmlData){
		Long interId = -1L;
		DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
		dataExchangeStatus.setSrcPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		dataExchangeStatus.setDestPlatform(bizPlatform);
		dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeStatus.setOwnSideBizCode(ownSideBizCode);
		dataExchangeStatus.setOppoSideBizCode(oppoSideBizCode);
		dataExchangeStatus.setXmlData(xmlData);
		interId = this.saveDataExchangeStatus(dataExchangeStatus);
		return interId;
	}

	@Override
	public Long saveDataExchangeStatusForEventNew(String srcPlatform, String destPlatform, String oppoSideBizCode, String ownSideBizCode){
		Long interId = -1L;
		
		DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
		dataExchangeStatus.setSrcPlatform(srcPlatform);
		dataExchangeStatus.setDestPlatform(destPlatform);
		dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeStatus.setOwnSideBizCode(ownSideBizCode);
		dataExchangeStatus.setOppoSideBizCode(oppoSideBizCode);
		
		interId = this.saveDataExchangeStatus(dataExchangeStatus);
		return interId;
	}
	
	@Override
	public Long saveDataExchangeStatusForTaskNew(String srcPlatform, String destPlatform, String oppoSideBizCode, String ownSideBizCode){
		Long interId = -1L;
		
		DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
		dataExchangeStatus.setSrcPlatform(srcPlatform);
		dataExchangeStatus.setDestPlatform(destPlatform);
		dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
		dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
		dataExchangeStatus.setOwnSideBizCode(ownSideBizCode);
		dataExchangeStatus.setOppoSideBizCode(oppoSideBizCode);
		
		interId = this.saveDataExchangeStatus(dataExchangeStatus);
		
		
		return interId;
	}
	
	@Override
	public Long saveDataExchangeStatusForTaskNew(DataExchangeStatus dataExchangeStatus){
		Long interId = -1L;
		
		dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
		dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
		
		interId = this.saveDataExchangeStatus(dataExchangeStatus);
		
		return interId;
	}
	
	@Override
	public boolean updateDataExchangeStatusById(DataExchangeStatus dataExchangeStatus) {
		int result = 0;
		//!isDataExchangeExists(dataExchangeStatus) && 
		if(dataExchangeStatus!=null && dataExchangeStatus.getInterId()!=null && dataExchangeStatus.getInterId()>0){
			result = dataExchangeStatusMapper.update(dataExchangeStatus);
		}
		
		return result > 0;
	}

	@Override
	public boolean updateDataExchangeStatusByInterId(DataExchangeStatus dataExchangeStatus){
		boolean successFlag = false;
		
		if(dataExchangeStatus != null){
			String srcPlatform = dataExchangeStatus.getSrcPlatform();
			String destPlatform = dataExchangeStatus.getDestPlatform();
			String oppoSideBizType = dataExchangeStatus.getOppoSideBizType();
			String ownSideBizType = dataExchangeStatus.getOwnSideBizType();
			String oppoSideBizCode = dataExchangeStatus.getOppoSideBizCode();
			String ownSideBizCode = dataExchangeStatus.getOwnSideBizCode();System.out.println("updateDataExchangeStatusByInterId");
			if(StringUtils.isNotBlank(srcPlatform) || StringUtils.isNotBlank(destPlatform) || StringUtils.isNotBlank(oppoSideBizType) ||
			   StringUtils.isNotBlank(ownSideBizType) || StringUtils.isNotBlank(oppoSideBizCode) || StringUtils.isNotBlank(ownSideBizCode)){
				successFlag = this.updateDataExchangeStatusById(dataExchangeStatus);
			}else if(dataExchangeStatus.getInterId()!=null && dataExchangeStatus.getInterId()>0){
				System.out.println("dataExchangeStatus.getInterId()-"+dataExchangeStatus.getInterId());
				successFlag = this.updateDataExchangeStatusById(dataExchangeStatus);
			}
		}
		
		return successFlag;
	}
	
	@Override
	public boolean deleteDataExchangeStatusById(Long interId) {
		int result = 0;
		
		if(interId!=null && interId>0){
			result = dataExchangeStatusMapper.delete(interId);
		}
		
		return result > 0;
	}
	
	@Override
	public int findDataExchangeCount(DataExchangeStatus dataExchangeStatus){
		int count = 0;
		defaultDataExchange(dataExchangeStatus);//设置默认属性

		count = dataExchangeStatusMapper.findCountByCriteria(dataExchangeStatus);
		
		return count;
	}

	@Override
	public List<DataExchangeStatus> findDataExchange(DataExchangeStatus dataExchangeStatus){
		return dataExchangeStatusMapper.findPageListByCriteria(dataExchangeStatus);
	}
	
	@Override
	public List<DataExchangeStatus> findDataExchangeList(DataExchangeStatus dataExchangeStatus){
		List<DataExchangeStatus> dataExchangeStatusList = null;
		
		if(dataExchangeStatus != null){
			if(StringUtils.isBlank(dataExchangeStatus.getExchangeFlag())){
				dataExchangeStatus.setExchangeFlag(EXCHANGE_FLAG_UNEXCHANGE);
			}
			
			if(StringUtils.isBlank(dataExchangeStatus.getStatus())){
				dataExchangeStatus.setStatus(STATUS_VALIDATE);
			}
			
			dataExchangeStatusList = dataExchangeStatusMapper.findPageListByCriteria(dataExchangeStatus);
		}
		
		return dataExchangeStatusList;
	}
	
	@Override
	public List<Map<String, Object>> findEventNewToFeedback(Map<String, Object> params){
		List<Map<String, Object>> resultMap = null;
		
		if(params != null){
			if(CommonFunctions.isBlank(params, "OWN_SIDE_BIZ_TYPE")){
				params.put("ownSideBizType", BIZ_TYPE_EVENT_NEW);
			}
			if(CommonFunctions.isBlank(params, "OPPO_SIDE_BIZ_TYPE")){
				params.put("oppoSideBizType", BIZ_TYPE_EVENT_NEW);
			}
			if(CommonFunctions.isBlank(params, "EXCHANGE_FLAG")){
				params.put("exchangeFlag", EXCHANGE_FLAG_UNEXCHANGE);
			}
			if(CommonFunctions.isBlank(params, "STATUS_")){
				params.put("status", STATUS_VALIDATE);
			}
			
			resultMap = dataExchangeStatusMapper.findEventNewToFeedback(params);
			
			if(resultMap!=null && resultMap.size()>0){
				for(Map<String, Object> result : resultMap){
					if(CommonFunctions.isNotBlank(result, "FIN_TIME")){
						Date finTime = (Date)result.get("FIN_TIME");
						if(finTime != null){
							try {
								String finTimeStr = DateUtils.convertDateToString(finTime);
								result.put("FIN_TIME_STR", finTimeStr);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
					
					if(CommonFunctions.isNotBlank(result, "EVENT_ID")){
						Long eventId = Long.valueOf(result.get("EVENT_ID").toString());
						String closerName = eventProcessService.findEventCloserByEventId(eventId);
						if(StringUtils.isNotBlank(closerName)){
							result.put("CLOSER", closerName);
						}
					}
				}
			}
		}
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> findReceiveNewToFeedback(Map<String, Object> params){
		List<Map<String, Object>> resultMap = null;
		
		if(params != null){
			if(CommonFunctions.isBlank(params, "OWN_SIDE_BIZ_TYPE")){
				params.put("ownSideBizType", BIZ_TYPE_EVENT_NEW);
			}
			if(CommonFunctions.isBlank(params, "OPPO_SIDE_BIZ_TYPE")){
				params.put("oppoSideBizType", BIZ_TYPE_RECEIVE_NEW);
			}
			if(CommonFunctions.isBlank(params, "EXCHANGE_FLAG")){
				params.put("exchangeFlag", EXCHANGE_FLAG_UNEXCHANGE);
			}
			if(CommonFunctions.isBlank(params, "STATUS_")){
				params.put("status", STATUS_VALIDATE);
			}
			
			resultMap = dataExchangeStatusMapper.findReceiveNewToFeedback(params);
		}
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> findTaskNewToFeedback(Map<String, Object> params){
		List<Map<String, Object>> resultMap = null;
		
		if(params != null){
			if(CommonFunctions.isBlank(params, "OWN_SIDE_BIZ_TYPE")){
				params.put("ownSideBizType", BIZ_TYPE_TASK_NEW);
			}
			if(CommonFunctions.isBlank(params, "OPPO_SIDE_BIZ_TYPE")){
				params.put("oppoSideBizType", BIZ_TYPE_TASK_NEW);
			}
			if(CommonFunctions.isBlank(params, "EXCHANGE_FLAG")){
				params.put("exchangeFlag", EXCHANGE_FLAG_UNEXCHANGE);
			}
			if(CommonFunctions.isBlank(params, "STATUS_")){
				params.put("status", STATUS_VALIDATE);
			}
			
			resultMap = dataExchangeStatusMapper.findTaskNewToFeedback(params);
		}
		
		return resultMap;
	}
	
	@Override
	public List<DataExchangeStatus> findEventNewToReport(Map<String, Object> params){
		List<DataExchangeStatus> dataExchangeStatusList = null;
		
		if(params != null){
			if(CommonFunctions.isBlank(params, "OWN_SIDE_BIZ_TYPE")){
				params.put("ownSideBizType", BIZ_TYPE_EVENT_NEW);
			}
			if(CommonFunctions.isBlank(params, "OPPO_SIDE_BIZ_TYPE")){
				params.put("oppoSideBizType", BIZ_TYPE_EVENT_NEW);
			}
			if(CommonFunctions.isBlank(params, "EXCHANGE_FLAG")){
				params.put("exchangeFlag", EXCHANGE_FLAG_UNEXCHANGE);
			}
			if(CommonFunctions.isBlank(params, "STATUS_")){
				params.put("status", STATUS_VALIDATE);
			}
			
			dataExchangeStatusList = dataExchangeStatusMapper.findEventNewToReport(params);
			
			if(dataExchangeStatusList!=null && dataExchangeStatusList.size()>0){
				for(DataExchangeStatus dataExchangeStatus : dataExchangeStatusList){
					Long creatUserId = dataExchangeStatus.getCreateUserId();
					Date createTime = dataExchangeStatus.getCreateTime();
					
					if(creatUserId!=null && creatUserId>0){
						UserInfo userInfo = userService.getUserExtraInfoByUserId(creatUserId, null);
						
						if(userInfo != null){
							dataExchangeStatus.setCreateUserName(userInfo.getPartyName());
						}
					}
					if(createTime != null){
						try {
							String createTimeStr = DateUtils.convertDateToString(createTime);
							dataExchangeStatus.setCreateTimeStr(createTimeStr);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		return dataExchangeStatusList;
	}
	
	@Override
	public List<Map<String, Object>> findEventNewRejected(Map<String, Object> params){
		List<Map<String, Object>> resultMap = null;
		
		if(params != null){
			String activityName = "";
			StringBuffer msgWrong = new StringBuffer("");
			List<Map<String, Object>> queryTaskList = null;
			String instanceId = "";
			Long eventId = -1L;
			
			if(CommonFunctions.isBlank(params, "EXCHANGE_FLAG")){
				params.put("exchangeFlag", EXCHANGE_FLAG_UNEXCHANGE);
			}
			if(CommonFunctions.isBlank(params, "STATUS_")){
				params.put("status", STATUS_VALIDATE);
			}
			
			if(CommonFunctions.isNotBlank(params, "activityName")){
				activityName = params.get("activityName").toString();
			}else if(CommonFunctions.isNotBlank(params, "bizPlatform")){
				String bizPlatform = params.get("bizPlatform").toString();
				activityName = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_REJECT_ACTIVITY_NAME_TRIGGER+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}else if(CommonFunctions.isNotBlank(params, "trigger")){
				String trigger = params.get("trigger").toString();
				activityName = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, trigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			}else{
				msgWrong.append("属性[activityName]、[bizPlatform]和[trigger]至少需要一个不为空，请检查！");
			}
			
			if(StringUtils.isBlank(activityName)){
				msgWrong.append("功能配置："+ConstantValue.THREE_PART_JOINT_UNOIN+"->"+ConstantValue.DEFAULT_REJECT_ACTIVITY_NAME_TRIGGER+"_"+"未配置配置值！");
				logger.info("有如下错误信息："+msgWrong);
			}
			
			params.put("activityName", activityName);
			resultMap = dataExchangeStatusMapper.findEventNewRejected(params);
			
			if(resultMap!=null && resultMap.size()>0){
				for(Map<String, Object> map : resultMap){
					if(CommonFunctions.isNotBlank(map, "EVENT_ID")){
						try{
							eventId = Long.valueOf(map.get("EVENT_ID").toString());
						}catch(NumberFormatException e){
							e.printStackTrace();
							logger.info("事件id为 "+map.get("EVENT_ID")+"的事件不能转换为Long型数据。");
						}
						if(eventId > 0){
							instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(eventId);
							if(StringUtils.isNotBlank(instanceId)){
								queryTaskList = eventDisposalWorkflowService.queryProInstanceDetail(Long.valueOf(instanceId), IEventDisposalWorkflowService.SQL_ORDER_DESC);
								if(queryTaskList!=null && queryTaskList.size()>0){
									map.putAll(queryTaskList.get(0));
								}
							}else{
								logger.info("事件id为："+eventId+" 的事件没有关联的工作流实例id(instanceId)");
							}
						}
					}
					
				}
			}
		}
		
		return resultMap;
	}
	
	
	@Override
	public boolean isEventTaskFeedback(Long eventId, String destPlatform, String oppoSideBizCode){
		boolean flag = true;
		
		if(eventId!=null && eventId>0){
			StringBuffer msgWrong = new StringBuffer("");
			String instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(eventId);
			
			if(StringUtils.isNotBlank(instanceId)){
				List<Map<String, Object>> queryTaskList = eventDisposalWorkflowService.queryProInstanceDetail(Long.valueOf(instanceId), IEventDisposalWorkflowService.SQL_ORDER_DESC);
				if(queryTaskList!=null && queryTaskList.size()>0){
					String taskId = "";
					String remarks = "";
					DataExchangeStatus dataExchangeStatus = null;
					int taskCount = 0;
					
					for(Map<String, Object> map : queryTaskList){
						Long interId = -1L;
						if(CommonFunctions.isNotBlank(map, "TASK_ID") && CommonFunctions.isNotBlank(map, "REMARKS")){
							remarks = map.get("REMARKS").toString();
							taskId = map.get("TASK_ID").toString();
							
							if(StringUtils.isNotBlank(taskId) && StringUtils.isNotBlank(remarks)){
								dataExchangeStatus = new DataExchangeStatus();
								dataExchangeStatus.setSrcPlatform(destPlatform);
								dataExchangeStatus.setOppoSideBizCode(oppoSideBizCode);
								dataExchangeStatus.setOwnSideBizCode(taskId);
								dataExchangeStatus.setExchangeFlag("");
								dataExchangeStatus.setOwnSideBizType(BIZ_TYPE_TASK_NEW);
								taskCount = this.findDataExchangeCount(dataExchangeStatus);
								
								if(taskCount == 0){//为了排除从第三方接入的任务再重新推送回去
									interId = this.saveDataExchangeStatusForTaskNew(null, destPlatform, oppoSideBizCode, taskId);
									if(interId < 0){
										msgWrong.append("事件：").append(eventId).append("的任务").append(taskId).append(" 中间记录新增失败！");
									}
								}
							}
						}
					}
				}
			}else{
				msgWrong.append("事件id为：").append(eventId).append(" 的事件没有关联的工作流实例id(instanceId)");
			}
			
			if(msgWrong.length() > 0){
				flag = false;
				logger.info("有如下错误信息："+msgWrong);
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean isEventTaskFeedback(Long eventId, DataExchangeStatus dataExchangeStatus){
		boolean flag = true;
		
		if(eventId!=null && eventId>0 && dataExchangeStatus!=null){
			StringBuffer msgWrong = new StringBuffer("");
			String instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(eventId);
			
			if(StringUtils.isNotBlank(instanceId)){
				List<Map<String, Object>> queryTaskList = eventDisposalWorkflowService.queryProInstanceDetail(Long.valueOf(instanceId), IEventDisposalWorkflowService.SQL_ORDER_DESC);
				if(queryTaskList!=null && queryTaskList.size()>0){
					String taskId = "";
					String remarks = "";
					DataExchangeStatus dataExchangeStatusTmp = null;
					int taskCount = 0;
					
					for(Map<String, Object> map : queryTaskList){
						Long interId = -1L;
						if(CommonFunctions.isNotBlank(map, "TASK_ID") && CommonFunctions.isNotBlank(map, "REMARKS")){
							remarks = map.get("REMARKS").toString();
							taskId = map.get("TASK_ID").toString();
							
							if(StringUtils.isNotBlank(taskId) && StringUtils.isNotBlank(remarks)){
								dataExchangeStatusTmp = new DataExchangeStatus();
								dataExchangeStatus.setInterId(null);//还原数据，清空上传操作的影响
								
								dataExchangeStatusTmp.setSrcPlatform(dataExchangeStatus.getDestPlatform());
								dataExchangeStatusTmp.setOppoSideBizCode(dataExchangeStatus.getOppoSideBizCode());
								dataExchangeStatusTmp.setOwnSideBizCode(taskId);
								dataExchangeStatusTmp.setExchangeFlag("");
								dataExchangeStatusTmp.setOwnSideBizType(BIZ_TYPE_TASK_NEW);
								taskCount = this.findDataExchangeCount(dataExchangeStatusTmp);
								
								if(taskCount == 0){//为了排除从第三方接入的任务再重新推送回去
									dataExchangeStatus.setOwnSideBizCode(taskId);
									interId = this.saveDataExchangeStatusForTaskNew(dataExchangeStatus);
									if(interId < 0){
										msgWrong.append("事件：").append(eventId).append("的任务").append(taskId).append(" 中间记录新增失败！");
									}
								}
							}
						}
					}
				}
			}else{
				msgWrong.append("事件id为：").append(eventId).append(" 的事件没有关联的工作流实例id(instanceId)");
			}
			
			if(msgWrong.length() > 0){
				flag = false;
				logger.info("有如下错误信息："+msgWrong);
			}
		}
		
		return flag;
	}
	
	/**
	 * 设置默认属性
	 * @param dataExchangeStatus
	 */
	private void defaultDataExchange(DataExchangeStatus dataExchangeStatus){
		String srcPlatform = dataExchangeStatus.getSrcPlatform();
		String destPlatform = dataExchangeStatus.getDestPlatform();
		String exchangeFlag = dataExchangeStatus.getExchangeFlag();
		String status = dataExchangeStatus.getStatus();
		
		if(exchangeFlag == null){
			dataExchangeStatus.setExchangeFlag(EXCHANGE_FLAG_UNEXCHANGE);
		}
		
		if(StringUtils.isBlank(status)){
			dataExchangeStatus.setStatus(STATUS_VALIDATE);
		}
		
		if(StringUtils.isBlank(srcPlatform) && StringUtils.isNotBlank(destPlatform)){
			dataExchangeStatus.setSrcPlatform(PLATFORM_EVENT_NEW);
		}else if(StringUtils.isNotBlank(srcPlatform) && StringUtils.isBlank(destPlatform)){
			dataExchangeStatus.setDestPlatform(PLATFORM_EVENT_NEW);
		}
	}
	
	/**
	 * 判断参数是否有效 当interId有效时，不作参数判断
	 * @param dataExchangeStatus
	 * @return 有效返回true，否则返回false
	 */
	private boolean isDataExchangeValidate(DataExchangeStatus dataExchangeStatus){
		boolean validateFlag = false;
		
		if(dataExchangeStatus != null){
			Long interId = dataExchangeStatus.getInterId();
			String msgWrong = "";
			
			if(interId==null || interId<0){
				String srcPlatform = dataExchangeStatus.getSrcPlatform();
				String destPlatform = dataExchangeStatus.getDestPlatform();
				String oppoSideBizType = dataExchangeStatus.getOppoSideBizType();
				String ownSideBizType = dataExchangeStatus.getOwnSideBizType();
				String oppoSideBizCode = dataExchangeStatus.getOppoSideBizCode();
				String ownSideBizCode = dataExchangeStatus.getOwnSideBizCode();
				
				if(StringUtils.isBlank(oppoSideBizType)){
					msgWrong = "缺少属性[oppoSideBizType]，请检查！";
				}else if(StringUtils.isBlank(ownSideBizType)){
					msgWrong = "缺少属性[ownSideBizType]，请检查！";
				}else if(StringUtils.isBlank(srcPlatform)){
					msgWrong = "缺少属性[srcPlatform]，请检查！";
				}else if(StringUtils.isBlank(destPlatform)){
					msgWrong = "缺少属性[destPlatform]，请检查！";
				}else if(StringUtils.isBlank(oppoSideBizCode) && StringUtils.isBlank(ownSideBizCode)){
					msgWrong = "属性[oppoSideBizCode]和[ownSideBizCode]至少需要一个不为空，请检查！";
				}
			}
			
			if(StringUtils.isNotBlank(msgWrong)){
				logger.info("有如下错误信息："+msgWrong);
				validateFlag = false;
			}else{
				validateFlag = true;
			}
		}
		
		return validateFlag;
	}
	
	/**
	 * 判断是否已存在有效的记录
	 * @param dataExchangeStatus
	 * @return 已存在则返回true，否则返回false
	 */
	private boolean isDataExchangeExists(DataExchangeStatus dataExchangeStatus){
		boolean isExists = true;
		
		if(isDataExchangeValidate(dataExchangeStatus)){
			int count = 0;
			Long interId = dataExchangeStatus.getInterId();
			
			DataExchangeStatus dataExchangeStatusTmp = new DataExchangeStatus();
			dataExchangeStatusTmp.setSrcPlatform(dataExchangeStatus.getSrcPlatform());
			dataExchangeStatusTmp.setOppoSideBizType(dataExchangeStatus.getOppoSideBizType());
			dataExchangeStatusTmp.setDestPlatform(dataExchangeStatus.getDestPlatform());
			dataExchangeStatusTmp.setOwnSideBizType(dataExchangeStatus.getOwnSideBizType());
			dataExchangeStatusTmp.setOppoSideBizCode(dataExchangeStatus.getOppoSideBizCode());
			dataExchangeStatusTmp.setOwnSideBizCode(dataExchangeStatus.getOwnSideBizCode());
			dataExchangeStatusTmp.setExchangeFlag("");
			dataExchangeStatusTmp.setStatus(STATUS_VALIDATE);
			
			if(interId!=null && interId>0){//更新操作的判重
				DataExchangeStatus dataExchange = null;
				List<DataExchangeStatus> dataExchangeStatusList = findDataExchangeList(dataExchangeStatusTmp);
				if(dataExchangeStatusList!=null && dataExchangeStatusList.size()==1){
					dataExchange = dataExchangeStatusList.get(0);
				}
				
				isExists = dataExchange==null || !interId.equals(dataExchange.getInterId());
			}else{//新增操作的判重
				count = findDataExchangeCount(dataExchangeStatusTmp);
				
				isExists = count > 0;
			}
		}
		
		return isExists;
	}
	
	
	@Override
	public List<Map<String, Object>> findNewTask(Map<String, Object> params) {
		return dataExchangeStatusMapper.findNewTask(params);
	}

	@Override
	public Map<String, Object> getCloseEvt(Map<String, Object> params) {
		return dataExchangeStatusMapper.getCloseEvt(params);
	}

	@Override
	public List<Map<String, Object>> findLikeEventVerify(Map<String, Object> params) {
		return dataExchangeStatusMapper.findLikeEventVerify(params);
	}

	@Override
	public int findCountEvent4NcHuaWei(Map<String, Object> params) {
		return dataExchangeStatusMapper.findCountEvent4NcHuaWei(params);
	}

}
