package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.epidemicPreControl.ReportEPCMapper;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.twoViolationPre.ReportTwoViolationPreMapper;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportNumberCfgBizCodeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportTwoViolationPreServiceImpl;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventExceptionEnum;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.data.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 疫情防控信息，相关涉及表：T_EVENT_EPIDEMIC_PRE_CONTROL
 * @ClassName:   ReportEPCServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月11日 下午2:22:09
 */
@Service("reportEPCService")
public class ReportEPCServiceImpl extends ReportTwoViolationPreServiceImpl {
	@Autowired
	private ReportEPCMapper reportEPCMapper;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private IWorkflow4BaseService workflow4BaseService;

	@Autowired
	private IBaseWorkflowService baseWorkflowService;

	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 获取列表方法映射
	 * @return
	 */
	@SuppressWarnings("serial")
	protected Map<Integer, String> capMethodMap() {
		return new HashMap<Integer, String>() {
	        {
	        	put(1, "4Draft");
				put(2, "4Todo");
				put(3, "4Handled");
				put(4, "4Initiator");
				put(5, "4Jurisdiction");
				put(50,"4JurisdictionSimplify");
				put(51,"4JurisdictionWithMarker");
				put(52,"4JurisdictionWithRegionPath");
				put(6, "4Archived");
				put(7, "4MsgReading");
	        }
	    };
	}
	
	@Override
	public String saveReportExtendInfo(Map<String, Object> epcParam, UserInfo userInfo) {
		if(epcParam == null || epcParam.isEmpty()) {
			throw new IllegalArgumentException("缺少需要操作的疫情防控！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		Long userId = userInfo.getUserId();
		String epcUUID = null;
		
		if(CommonFunctions.isBlank(epcParam, "creator")) {
			epcParam.put("creator", userId);
		}
		if(CommonFunctions.isBlank(epcParam, "updator")) {
			epcParam.put("updator", userId);
		}
		if(CommonFunctions.isNotBlank(epcParam, "feedbackTime")) {
			Object feedbackTimeObj = epcParam.get("feedbackTime");
			Date feedbackTime = null;
			
			if(feedbackTimeObj instanceof String) {
				try {
					feedbackTime = DateUtils.convertStringToDate(feedbackTimeObj.toString(), DateUtils.PATTERN_24TIME);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if(feedbackTimeObj instanceof Date) {
				feedbackTime = (Date) feedbackTimeObj;
			}
			
			epcParam.put("feedbackTime", feedbackTime);
		} else if(CommonFunctions.isBlank(epcParam, "feedbackTime") && CommonFunctions.isNotBlank(epcParam, "feedbackTimeStr")) {
			Date feedbackTime = null;
			String feedbackTimeStr = epcParam.get("feedbackTimeStr").toString();
			
			try {
				feedbackTime = DateUtils.convertStringToDate(feedbackTimeStr, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(feedbackTime != null) {
				epcParam.put("feedbackTime", feedbackTime);
			}
		}
		
		if(CommonFunctions.isNotBlank(epcParam, "arriveTime")) {
			Object arriveTimeObj = epcParam.get("arriveTime");
			Date arriveTime = null;
			
			if(arriveTimeObj instanceof String) {
				try {
					arriveTime = DateUtils.convertStringToDate(arriveTimeObj.toString(), DateUtils.PATTERN_DATE);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if(arriveTimeObj instanceof Date) {
				arriveTime = (Date) arriveTimeObj;
			}
			
			epcParam.put("arriveTime", arriveTime);
		} else if(CommonFunctions.isBlank(epcParam, "arriveTime") && CommonFunctions.isNotBlank(epcParam, "arriveTimeStr")) {
			Date arriveTime = null;
			String arriveTimeStr = epcParam.get("arriveTimeStr").toString();
			
			try {
				arriveTime = DateUtils.convertStringToDate(arriveTimeStr, DateUtils.PATTERN_DATE);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(arriveTime != null) {
				epcParam.put("arriveTime", arriveTime);
			}
		}
		
		if(CommonFunctions.isNotBlank(epcParam, "epcUUID")) {
			if(reportEPCMapper.update(epcParam) > 0) {
				epcUUID = epcParam.get("epcUUID").toString();
			}
		} else {
			if(CommonFunctions.isBlank(epcParam, "reportUUID")) {
				throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
			}
			
			//为了保证记录的唯一性
			Map<String, Object> epcMap = reportEPCMapper.findByIdSimple(epcParam);
			
			if(epcMap != null && !epcMap.isEmpty()) {
				Map<String, Object> epcMapAfter = new HashMap<String, Object>(),
									epcMapAfterRemain = new HashMap<String, Object>();
				epcMapAfter.putAll(epcMap);
				epcMapAfter.putAll(epcParam);
				
				for(String key : epcMapAfter.keySet()) {
					if(CommonFunctions.isBlank(epcMap, key) 
							|| !epcMapAfter.get(key).equals(epcMap.get(key))) {
						epcMapAfterRemain.put(key, epcMapAfter.get(key));
					}
				}
				
				if(!epcMapAfterRemain.isEmpty()) {
					epcUUID = epcMap.get("epcUUID").toString();
					epcMapAfterRemain.put("epcUUID", epcUUID);
					
					if(reportEPCMapper.update(epcMapAfterRemain) > 0) {
						epcUUID = epcMap.get("epcUUID").toString();
					}
				} else {
					epcUUID = epcMap.get("epcUUID").toString();
				}
			} else if(reportEPCMapper.insert(epcParam) > 0) {
				if(CommonFunctions.isNotBlank(epcParam, "epcUUID")) {
					epcUUID = epcParam.get("epcUUID").toString();
				}
			}
			
			saveReportExtraInfo(epcParam);
		}
		
		return epcUUID;
	}

	@Override
	public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo,
			Map<String, Object> params) {
		Map<String, Object> epcParam = new HashMap<String, Object>(), epcParamMap = null;
		boolean isWithReportFocus = true;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
			isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
		}
		
		if(StringUtils.isNotBlank(reportUUID)) {
			epcParam.put("reportUUID", reportUUID);
		} else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
			epcParam.put("reportUUID", params.get("reportUUID"));
		} else if(CommonFunctions.isNotBlank(params, "epcUUID")) {
			epcParam.put("epcUUID", params.get("epcUUID"));
		} else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
			epcParam.put("reportId", params.get("reportId"));
		}
		
		if(epcParam.isEmpty()) {
			throw new IllegalArgumentException("缺失唯一性条件，不可查询疫情防控！");
		}
		
		if(isWithReportFocus) {
			epcParamMap = reportEPCMapper.findById(epcParam);
			
			if(epcParamMap != null && !epcParamMap.isEmpty()) {
				List<Map<String, Object>> epcMapList = new ArrayList<Map<String, Object>>();
				
				epcMapList.add(epcParamMap);
				
				if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
					params.put("userOrgCode", userInfo.getOrgCode());
				}
				
				formatDataOut(epcMapList, params);
			}
		} else {
			epcParamMap = reportEPCMapper.findByIdSimple(epcParam);
		}
		
		findReportExtraInfo(epcParamMap, params);
		
		return epcParamMap;
	}
	
	@Override
	public String capNumberCfgBizCode() {
		return ReportNumberCfgBizCodeEnum.epidemicPreControl.getBizCode();
	}
	
	@Override
	public void formatParamIn4Report(Map<String, Object> params) throws Exception {
		params = params == null ? new HashMap<String, Object>() : params;
		
		super.formatParamIn4Report(params);
		
		if(CommonFunctions.isBlank(params, "orderBy")) {
			String collectSource = null;
			Integer FIRST_ORDER_BY = 1;
			
			if(CommonFunctions.isNotBlank(params, "collectSource")) {
				collectSource = params.get("collectSource").toString();
			}
			
			if(EPCCollectSourceEnum.SMALL_MEDICAL_INSTITUTION.getCollectSource().equals(collectSource)) {
				String kpcSituation = null;
				
				if(CommonFunctions.isNotBlank(params, "kpcSituation")) {
					kpcSituation = params.get("kpcSituation").toString();
				}
				
				if(EPCKpcSituationEnum.SUSPECT.getKpcSituation().equals(kpcSituation)) {
					params.put("orderBy", FIRST_ORDER_BY);
				}
			}
		}
	}
	
	/**
	 * 保存疫情防控额外信息
	 * @param params	额外参数
	 * 			reportId	上报id，如果没有，通过外部总入口进行调整，不要内部再次通过对外接口进行信息转换获取
	 */
	protected void saveReportExtraInfo(Map<String, Object> params) {
		super.saveReportExtraInfo(params);
	}
	
	/**
	 * 判断地域信息是否符合要求
	 * @param orgEntityInfo	地域信息
	 * @param params
	 * 			dataSource		数据来源
	 * 			collectSource	采集方式
	 * 			isEditableNode	是否可编辑，true表示当前节点需要选择到村二级网格，默认为false
	 * @return
	 * @throws Exception
	 */
	protected boolean isRegionSatisfied(OrgEntityInfoBO orgEntityInfo, Map<String, Object> params) throws Exception {
		boolean flag = true, isEditableNode = false;
		String regionChiefLevel = null, collectSource = null, regionCode = null,verifyStatus=null;

		if(orgEntityInfo != null) {
			regionCode = orgEntityInfo.getOrgCode();
			regionChiefLevel = orgEntityInfo.getChiefLevel();
		}
		if(CommonFunctions.isNotBlank(params,"collectSource")){
			collectSource = String.valueOf(params.get("collectSource"));
		}
		if(CommonFunctions.isNotBlank(params,"isEditableNode")){
			isEditableNode = Boolean.valueOf(String.valueOf(params.get("isEditableNode")));
		}
		if(CommonFunctions.isNotBlank(params,"verifyStatus")){
			verifyStatus = String.valueOf(params.get("verifyStatus"));
		}

		//采集来源为 01 的，地域必须选择到网格层级
		//或者当前环节为第一副网格长核实环节时，无论哪个来源都需要选择到网格层级
		//add by ztc 无法核实的、核实后不属实的或者不在本网格/本村的，所属区域不需要到二级网格
		if( (EPCCollectSourceEnum.GRID_INSPECT.getCollectSource().equals(collectSource) || isEditableNode)) {

			if(StringUtils.isNotBlank(verifyStatus)){
				if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel) && EPCVerifyStatusEnum.IS_TRUE.getVerifyStatus().equals(verifyStatus)){
					if(logger.isErrorEnabled()) {
						logger.error("地域编码【" + regionCode + "】不是网格层级！");
					}

					throw ZhsqEventExceptionEnum.NOT_GRID_LEVEL_REGION_EXCEPTION.getZhsqEventException();
				}
			}else{
				if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)) {
					if(logger.isErrorEnabled()) {
						logger.error("地域编码【" + regionCode + "】不是网格层级！");
					}

					throw ZhsqEventExceptionEnum.NOT_GRID_LEVEL_REGION_EXCEPTION.getZhsqEventException();
				}
			}
		} else {
			//来源是市外事组02、市疫情防控组03、市大数据组的04，允许选择到乡镇街道
			if(EPCCollectSourceEnum.FOREIGN_AFFAIRS_SECTION.getCollectSource().equals(collectSource)
					|| EPCCollectSourceEnum.EPIDEMINC_PRE_CONTROL.getCollectSource().equals(collectSource)
					|| EPCCollectSourceEnum.BIG_DATA_GROUP.getCollectSource().equals(collectSource) ){
				if(regionChiefLevel.compareTo(ConstantValue.STREET_ORG_LEVEL) < 0){
					throw new ZhsqEventException("地域编码【" + regionCode + "】层级不能大于乡镇/街道层级！");
				}
			}else if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel) && !ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)){
				//采集来源不是 01 02 03 04 的，地域需要选择到网格层级或村社区层级
				throw new ZhsqEventException("地域编码【" + regionCode + "】既不是网格层级，也不是村/社区层级！");
			}
		}
		
		return flag;
	}
	
	protected ReportTwoViolationPreMapper capReportExtendMapper() {
		return reportEPCMapper;
	}
	
	protected String capBizType4Attachment() {
		return "EPIDEMIC_PRE_CONTROL";
	}
	
	protected String capBizType4ResmarkerInfo() {
		return "EPIDEMIC_PRE_CONTROL";
	}
	
	/**
	 * 格式化输入参数
	 * @param params
	 * @throws ZhsqEventException 
	 */
	protected void formatParamIn(Map<String, Object> params) throws ZhsqEventException {
		super.formatParamIn(params);
		
		if(CommonFunctions.isNotBlank(params, "kpcCardType")) {
			String kpcCardType = params.get("kpcCardType").toString();
			
			if(kpcCardType.contains(",")) {
				params.put("kpcCardTypeArray", kpcCardType.split(","));
				params.remove("kpcCardType");
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "disposalMode")) {
			String disposalMode = params.get("disposalMode").toString();
			
			if(disposalMode.contains(",")) {
				params.put("disposalModeArray", disposalMode.split(","));
				params.remove("disposalMode");
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "trafficMode")) {
			String trafficMode = params.get("trafficMode").toString();
			
			if(!trafficMode.startsWith(",")) {
				trafficMode = "," + trafficMode;
			}
			
			if(!trafficMode.endsWith(",")) {
				trafficMode += ",";
			}
			
			params.put("trafficMode", trafficMode);
		}

		if(CommonFunctions.isNotBlank(params, "collectSourceArray")) {
			String[] collectSourceArrays = params.get("collectSourceArray").toString().split(",");
			params.remove("collectSourceArray");
			params.put("collectSourceArray", collectSourceArrays);
		}

	}
	
	/**
	 * 数据格式化输出
	 * @param reportList
	 * @param params
	 */
	@Override
	protected void formatDataOut(List<Map<String, Object>> reportList, Map<String, Object> params) {
		if(reportList != null && reportList.size() > 0) {
			String userOrgCode = null, trafficModeStr = null;
			boolean isDictTransfer = true;
			List<BaseDataDict> reportStatusDictList = null,
							   kpcCardTypeDictList = null,
							   kpcTypeDictList = null,
					           dataSourceDictList = null,
							   disposalModeDictList = null,
							   kpcGoalDictList = null,
							   kpcSituationDictList = null,
							   kpcOriginDictList = null,
							   trafficModeDictList = null;
			int listType = 0;
			StringBuffer trafficModeBuffer = null;
			
			super.formatDataOut(reportList, params);
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			}
			if(CommonFunctions.isNotBlank(params, "isDictTransfer")) {
				isDictTransfer = Boolean.valueOf(params.get("isDictTransfer").toString());
			}
			if(CommonFunctions.isNotBlank(params, "listType")) {
				try {
					listType = Integer.valueOf(params.get("listType").toString());
				} catch(NumberFormatException e) {}
			}
			
			if(isDictTransfer && StringUtils.isNotBlank(userOrgCode)) {
				kpcCardTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(EPCDictEnum.KPC_CARD_TYPE.getDictCode(), userOrgCode);
				kpcTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(EPCDictEnum.KPC_TYPE.getDictCode(), userOrgCode);
				disposalModeDictList = baseDictionaryService.getDataDictListOfSinglestage(EPCDictEnum.DISPOSAL_MODE.getDictCode(), userOrgCode);
				dataSourceDictList = baseDictionaryService.getDataDictListOfSinglestage(EPCDictEnum.DATA_SOURCE.getDictCode(), userOrgCode);
				kpcGoalDictList = baseDictionaryService.getDataDictListOfSinglestage(EPCDictEnum.KPC_GOAL.getDictCode(), userOrgCode);
				kpcSituationDictList = baseDictionaryService.getDataDictListOfSinglestage(EPCDictEnum.KPC_SITUATION.getDictCode(), userOrgCode);
				kpcOriginDictList = baseDictionaryService.getDataDictListOfSinglestage(EPCDictEnum.KPC_ORIGIN.getDictCode(), userOrgCode);
				trafficModeDictList = baseDictionaryService.getDataDictListOfSinglestage(EPCDictEnum.TRAFFIC_MODE.getDictCode(), userOrgCode);
				
				if(listType == 1) {
					reportStatusDictList = new ArrayList<BaseDataDict>();
					//草稿不增添字典，因为只有草稿列表会用到
					BaseDataDict draftDict = new BaseDataDict();
					draftDict.setDictGeneralCode(EPCReportStatusEnum.DRAFT_STATUS.getReportStatus());
					draftDict.setDictName(EPCReportStatusEnum.DRAFT_STATUS.getReportStatusName());
					reportStatusDictList.add(draftDict);
				} else {
					reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(EPCDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
				}
			}
			
			for(Map<String, Object> reportMap : reportList) {
				if(CommonFunctions.isNotBlank(reportMap, "feedbackTime")) {
					reportMap.put("feedbackTimeStr", DateUtils.formatDate((Date) reportMap.get("feedbackTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(reportMap, "arriveTime")) {
					reportMap.put("arriveTimeStr", DateUtils.formatDate((Date) reportMap.get("arriveTime"), DateUtils.PATTERN_DATE));
				}
				
				if(CommonFunctions.isNotBlank(reportMap, "trafficMode") && trafficModeDictList != null) {
					trafficModeBuffer = new StringBuffer("");
					trafficModeStr = reportMap.get("trafficMode").toString();
					
					for(String trafficMode : trafficModeStr.split(",")) {
						if(StringUtils.isNotBlank(trafficMode)) {
							for(BaseDataDict trafficModeDict : trafficModeDictList) {
								if(trafficMode.equals(trafficModeDict.getDictGeneralCode())) {
									trafficModeBuffer.append("、").append(trafficModeDict.getDictName());
									break;
								}
							}
						}
					}
					
					if(trafficModeStr.startsWith(",")) {
						trafficModeStr = trafficModeStr.substring(1);
					}
					
					if(trafficModeStr.endsWith(",")) {
						trafficModeStr = trafficModeStr.substring(0, trafficModeStr.length() - 1);
					}
					
					reportMap.put("trafficMode", trafficModeStr);
					
					if(trafficModeBuffer.length() > 0) {
						reportMap.put("trafficModeName", trafficModeBuffer.substring(1));
					}
				}
				
				DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
				DataDictHelper.setDictValueForField(reportMap, "kpcCardType", "kpcCardTypeName", kpcCardTypeDictList);
				DataDictHelper.setDictValueForField(reportMap, "kpcType", "kpcTypeName", kpcTypeDictList);
				DataDictHelper.setDictValueForField(reportMap, "disposalMode", "disposalModeName", disposalModeDictList);
				DataDictHelper.setDictValueForField(reportMap, "dataSource", "dataSourceName", dataSourceDictList);
				DataDictHelper.setDictValueForField(reportMap, "kpcGoal", "kpcGoalName", kpcGoalDictList);
				DataDictHelper.setDictValueForField(reportMap, "kpcOrigin", "kpcOriginName", kpcOriginDictList);
				DataDictHelper.setDictValueForField(reportMap, "kpcSituation", "kpcSituationName", kpcSituationDictList);
			}
		}
	}

	@Override
	public List<Map<String, Object>> capTaskAdvice(List<Map<String, Object>> reportList,Map<String,Object> params) {
		Long instanceId = null;
		List<Map<String, Object>> taskMapList = null;
		String  formTypeId = null,
				task1Advice = null,//报告内容（报告人发布内容）
				task3_4Advice = null,//核实情况（第一副网格长(驻村工作队长)核实）
				task5Advice = null,//处置情况（第一网格长(驻村领导)处置）
				task6_9_10Advice = null;//处置情况（乡镇(街道)疫情防控指挥部处置）
		if(null != reportList && reportList.size() > 0){
			for (Map<String,Object> reportFocus : reportList) {
				task1Advice = null;
				task3_4Advice = null;
				task5Advice = null;
				task6_9_10Advice = null;

				//导出公共处理部分
				super.capExtraInfoForExport(reportFocus,params);

				if(CommonFunctions.isNotBlank(reportFocus,"instanceId")){
					instanceId = Long.valueOf(String.valueOf(reportFocus.get("instanceId")));
				}
				if(CommonFunctions.isNotBlank(reportFocus,"formTypeId")){
					formTypeId = String.valueOf(reportFocus.get("formTypeId"));
				}
				if(null != instanceId){
					taskMapList = workflow4BaseService.capHandledTaskInfoMap(instanceId, IWorkflow4BaseService.SQL_ORDER_DESC, params);
				}
				if(null != taskMapList && taskMapList.size() > 0){
					String taskCode = null;
					for (Map<String,Object> task:taskMapList){

						//当需要获取的某一环节的办理意见不为空时 进行值的获取 否则跳出循环
						if(CommonFunctions.isNotBlank(task,"TASK_CODE") && CommonFunctions.isNotBlank(task,"REMARKS") &&
								(StringUtils.isBlank(task1Advice) || StringUtils.isBlank(task3_4Advice)
								||StringUtils.isBlank(task5Advice) ||StringUtils.isBlank(task6_9_10Advice))
						){
							taskCode = String.valueOf(task.get("TASK_CODE"));
							//南安疫情防控信息流程
							if(FocusReportNode352Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeId)){
								if(FocusReportNode352Enum.FIND_NODE_NAME.getTaskNodeName().equals(taskCode) && StringUtils.isBlank(task1Advice)){
									task1Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task1Advice",task1Advice);
								}
								if((FocusReportNode352Enum.VERIFY_TASK_GRID_NODE_NAME.getTaskNodeName().equals(taskCode)||FocusReportNode352Enum.VERIFY_TASK_OTHER_NODE_NAME.getTaskNodeName().equals(taskCode))
										&& StringUtils.isBlank(task3_4Advice)){
									task3_4Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task3_4Advice",task3_4Advice);
								}
								if(FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(taskCode) && StringUtils.isBlank(task5Advice)){
									task5Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task5Advice",task5Advice);
								}
								if((FocusReportNode352Enum.NOT_THE_GRID_NODE_NAME.getTaskNodeName().equals(taskCode)
										||FocusReportNode352Enum.CENTRALIZED_ISOLATION_NODE_NAME.getTaskNodeName().equals(taskCode)
										||FocusReportNode352Enum.NUCLEIC_ACID_DETECTION_NODE_NAME.getTaskNodeName().equals(taskCode))
										&& StringUtils.isBlank(task6_9_10Advice)){
									task6_9_10Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task6_9_10Advice",task6_9_10Advice);
								}
							}else if(FocusReportNode35201Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeId)){
								//南安疫情防控信息流程_小型医疗机构

								if(FocusReportNode35201Enum.FIND_NODE_NAME.getTaskNodeName().equals(taskCode) && StringUtils.isBlank(task1Advice)){
									task1Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task1Advice",task1Advice);
								}
								//乡镇(街道)疫情防控指挥部处置
								if(FocusReportNode35201Enum.STREET_NAD_NODE_NAME.getTaskNodeName().equals(taskCode) && StringUtils.isBlank(task6_9_10Advice)){
									task6_9_10Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task6_9_10Advice",task6_9_10Advice);
								}
							}
						}else{
							continue;
						}
					}
				}
			}
		}

		return reportList;
	}
}
