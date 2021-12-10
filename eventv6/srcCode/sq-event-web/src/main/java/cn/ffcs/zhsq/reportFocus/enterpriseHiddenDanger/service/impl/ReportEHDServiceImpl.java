package cn.ffcs.zhsq.reportFocus.enterpriseHiddenDanger.service.impl;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.enterpriseHiddenDanger.ReportEHDMapper;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.twoViolationPre.ReportTwoViolationPreMapper;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportNumberCfgBizCodeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportTwoViolationPreServiceImpl;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.data.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 企业安全隐患，相关涉及表：T_EVENT_ENTER_HIDDEN_DANGER
 * @ClassName:   ReportEHDServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月6日 上午11:10:25
 */
@Service("reportEHDService")
public class ReportEHDServiceImpl extends ReportTwoViolationPreServiceImpl {
	@Autowired
	private ReportEHDMapper reportEHDMapper;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private IWorkflow4BaseService workflow4BaseService;
	
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
				put(50, "4JurisdictionSimplify");
				put(51,"4JurisdictionWithMarker");
				put(52,"4JurisdictionWithRegionPath");
				put(6, "4Archived");
				put(7, "4MsgReading");
	        }
	    };
	}
	
	@Override
	public String saveReportExtendInfo(Map<String, Object> ehdParam, UserInfo userInfo) {
		if(ehdParam == null || ehdParam.isEmpty()) {
			throw new IllegalArgumentException("缺少需要操作的企业安全隐患！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		Long userId = userInfo.getUserId();
		String ehdUUID = null;
		
		if(CommonFunctions.isBlank(ehdParam, "creator")) {
			ehdParam.put("creator", userId);
		}
		if(CommonFunctions.isBlank(ehdParam, "updator")) {
			ehdParam.put("updator", userId);
		}
		
		if(CommonFunctions.isNotBlank(ehdParam, "feedbackTime")) {
			Object feedbackTimeObj = ehdParam.get("feedbackTime");
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
			
			ehdParam.put("feedbackTime", feedbackTime);
		} else if(CommonFunctions.isBlank(ehdParam, "feedbackTime") && CommonFunctions.isNotBlank(ehdParam, "feedbackTimeStr")) {
			Date feedbackTime = null;
			String feedbackTimeStr = ehdParam.get("feedbackTimeStr").toString();
			
			try {
				feedbackTime = DateUtils.convertStringToDate(feedbackTimeStr, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(feedbackTime != null) {
				ehdParam.put("feedbackTime", feedbackTime);
			}
		}
		
		if(CommonFunctions.isNotBlank(ehdParam, "ehdUUID")) {
			if(reportEHDMapper.update(ehdParam) > 0) {
				ehdUUID = ehdParam.get("ehdUUID").toString();
			}
		} else {
			if(CommonFunctions.isBlank(ehdParam, "reportUUID")) {
				throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
			}
			
			//为了保证记录的唯一性
			Map<String, Object> ehdMap = reportEHDMapper.findByIdSimple(ehdParam);
			
			if(ehdMap != null && !ehdMap.isEmpty()) {
				Map<String, Object> ehdMapAfter = new HashMap<String, Object>(),
									ehdMapAfterRemain = new HashMap<String, Object>();
				ehdMapAfter.putAll(ehdMap);
				ehdMapAfter.putAll(ehdParam);
				
				for(String key : ehdMapAfter.keySet()) {
					if(CommonFunctions.isBlank(ehdMap, key) 
							|| !ehdMapAfter.get(key).equals(ehdMap.get(key))) {
						ehdMapAfterRemain.put(key, ehdMapAfter.get(key));
					}
				}
				
				if(!ehdMapAfterRemain.isEmpty()) {
					ehdUUID = ehdMap.get("ehdUUID").toString();
					ehdMapAfterRemain.put("ehdUUID", ehdUUID);
					
					if(reportEHDMapper.update(ehdMapAfterRemain) > 0) {
						ehdUUID = ehdMap.get("ehdUUID").toString();
					}
				} else {
					ehdUUID = ehdMap.get("ehdUUID").toString();
				}
			} else if(reportEHDMapper.insert(ehdParam) > 0) {
				if(CommonFunctions.isNotBlank(ehdParam, "ehdUUID")) {
					ehdUUID = ehdParam.get("ehdUUID").toString();
				}
			}
			
			saveReportExtraInfo(ehdParam);
		}
		
		return ehdUUID;
	}

	@Override
	public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo,
			Map<String, Object> params) {
		Map<String, Object> ehdParam = new HashMap<String, Object>(), ehdParamMap = null;
		boolean isWithReportFocus = true;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
			isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
		}
		
		if(StringUtils.isNotBlank(reportUUID)) {
			ehdParam.put("reportUUID", reportUUID);
		} else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
			ehdParam.put("reportUUID", params.get("reportUUID"));
		} else if(CommonFunctions.isNotBlank(params, "ehdUUID")) {
			ehdParam.put("ehdUUID", params.get("ehdUUID"));
		} else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
			ehdParam.put("reportId", params.get("reportId"));
		}
		
		if(ehdParam.isEmpty()) {
			throw new IllegalArgumentException("缺失唯一性条件，不可查询企业安全隐患！");
		}
		
		if(isWithReportFocus) {
			ehdParamMap = reportEHDMapper.findById(ehdParam);
			
			if(ehdParamMap != null && !ehdParamMap.isEmpty()) {
				List<Map<String, Object>> ehdMapList = new ArrayList<Map<String, Object>>();
				
				ehdMapList.add(ehdParamMap);
				
				if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
					params.put("userOrgCode", userInfo.getOrgCode());
				}
				
				formatDataOut(ehdMapList, params);
			}
		} else {
			ehdParamMap = reportEHDMapper.findByIdSimple(ehdParam);
		}
		
		findReportExtraInfo(ehdParamMap, params);
		
		return ehdParamMap;
	}
	
	@Override
	public String capNumberCfgBizCode() {
		return ReportNumberCfgBizCodeEnum.enterpriseHiddenDanger.getBizCode();
	}
	
	/**
	 * 判断地域编码是否符合要求
	 * @param params
	 * 			regionCode		地域编码
	 * 			curNodeName		当前环节编码
	 * 			nextNodeName	下一环节编码
	 * @return
	 * @throws Exception
	 */
	protected boolean isRegionSatisfied(Map<String, Object> params) throws Exception {
		boolean isCheckRegion = true;
		String curNodeName = null, nextNodeName = null;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isBlank(params, "isCheckRegion")) {
			if(CommonFunctions.isNotBlank(params, "curNodeName")) {
				curNodeName = params.get("curNodeName").toString();
			}
			
			if(CommonFunctions.isNotBlank(params, "nextNodeName")) {
				nextNodeName = params.get("nextNodeName").toString();
			}
			
			if(FocusReportNode351Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
					&& FocusReportNode351Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
				isCheckRegion = false;
			}
			
			params.put("isCheckRegion", isCheckRegion);
		}
		
		return super.isRegionSatisfied(params);
	}
	
	protected ReportTwoViolationPreMapper capReportExtendMapper() {
		return reportEHDMapper;
	}
	
	protected String capBizType4Attachment() {
		return "ENTERPRISE_HIDDEN_DANGER";
	}
	
	protected String capBizType4ResmarkerInfo() {
		return "ENTERPRISE_HIDDEN_DANGER";
	}
	
	/**
	 * 格式化输入参数
	 * @param params
	 * @throws ZhsqEventException 
	 */
	protected void formatParamIn(Map<String, Object> params) throws ZhsqEventException {
		super.formatParamIn(params);
		
		if(CommonFunctions.isNotBlank(params, "hiddenDangerType")) {
			String hiddenDangerType = params.get("hiddenDangerType").toString();
			
			if(hiddenDangerType.contains(",")) {
				params.put("hiddenDangerTypeArray", hiddenDangerType.split(","));
				params.remove("hiddenDangerType");
			}
		}
	}
	
	/**
	 * 数据格式化输出
	 * @param reportList
	 * @param params
	 */
	protected void formatDataOut(List<Map<String, Object>> reportList, Map<String, Object> params) {
		if(reportList != null && reportList.size() > 0) {
			String userOrgCode = null;
			boolean isDictTransfer = true;
			List<BaseDataDict> reportStatusDictList = null, hdtDictList = null;
			int listType = 0;
			
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
				if(listType == 1) {
					reportStatusDictList = new ArrayList<BaseDataDict>();
					//草稿不增添字典，因为只有草稿列表会用到
					BaseDataDict draftDict = new BaseDataDict();
					draftDict.setDictGeneralCode(EHDReportStatusEnum.DRAFT_STATUS.getReportStatus());
					draftDict.setDictName(EHDReportStatusEnum.DRAFT_STATUS.getReportStatusName());
					reportStatusDictList.add(draftDict);
				} else {
					reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(EHDDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
				}
				
				hdtDictList = baseDictionaryService.getDataDictListOfSinglestage(EHDDictEnum.HIDDEN_DANGER_TYPE.getDictCode(), userOrgCode);
			}
			
			String hiddenDangerType = null, OTHER_HDT = "99";
			
			for(Map<String, Object> reportMap : reportList) {
				hiddenDangerType = null;
				
				if(CommonFunctions.isNotBlank(reportMap, "feedbackTime")) {
					reportMap.put("feedbackTimeStr", DateUtils.formatDate((Date) reportMap.get("feedbackTime"), DateUtils.PATTERN_24TIME));
				}
				
				if(CommonFunctions.isNotBlank(reportMap, "hiddenDangerType")) {
					hiddenDangerType = reportMap.get("hiddenDangerType").toString();
				}
				
				DataDictHelper.setDictValueForField(reportMap, "hiddenDangerType", "hiddenDangerTypeName", hdtDictList);
				
				if(OTHER_HDT.equals(hiddenDangerType) && CommonFunctions.isNotBlank(reportMap, "hdtDescribe")) {
					reportMap.put("hiddenDangerTypeName", reportMap.get("hdtDescribe"));
				}
				//是否存在隐患
				if(CommonFunctions.isNotBlank(reportMap, "isHiddenDanger")) {
					String isHiddenDanger = reportMap.get("isHiddenDanger").toString();
					if("1".equals(isHiddenDanger)){
						reportMap.put("isHiddenDangerName", "是");
					}else if("0".equals(isHiddenDanger)){
						reportMap.put("isHiddenDangerName", "否");
					}
				}
				//是否属实
				if(CommonFunctions.isNotBlank(reportMap, "isTrue")) {
					String isTrue = reportMap.get("isTrue").toString();
					if("1".equals(isTrue)){
						reportMap.put("isTrueName", "是");
					}else if("0".equals(isTrue)){
						reportMap.put("isTrueName", "否");
					}
				}
				DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
			}
		}
	}


	@Override
	public List<Map<String, Object>> capTaskAdvice(List<Map<String, Object>> reportList,Map<String,Object> params) {
		Long instanceId = null;
		List<Map<String, Object>> taskMapList = null;
		String  formTypeId = null,
				task1Advice = null,//报告内容（报告人发布内容）
				task3Advice = null,//核实情况（第一副网格长(驻村工作队长)核实）
				//隐患认定
				task5Advice = null,//第一副网格长查处
				task6Advice = null,//乡镇查处
				task9Advice = null,//立案查处
				task10Advice = null;//行政处罚
		if(null != reportList && reportList.size() > 0){
			for (Map<String,Object> reportFocus : reportList) {
				task1Advice = null;
				task3Advice = null;
				task5Advice = null;
				task6Advice = null;
				task9Advice = null;
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
								(StringUtils.isBlank(task1Advice) || StringUtils.isBlank(task3Advice)
										||StringUtils.isBlank(task5Advice) ||StringUtils.isBlank(task6Advice)
										||StringUtils.isBlank(task9Advice) ||StringUtils.isBlank(task10Advice))
						){
							taskCode = String.valueOf(task.get("TASK_CODE"));
							//南安疫情防控信息流程
							if(FocusReportNode351Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeId)){
								//报告内容task1
								if(FocusReportNode351Enum.FIND_EHD_NODE_NAME.getTaskNodeName().equals(taskCode) && StringUtils.isBlank(task1Advice)){
									task1Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task1Advice",task1Advice);
								}
								//第一副网格长核实task3
								if((FocusReportNode351Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(taskCode))
										&& StringUtils.isBlank(task3Advice)){
									task3Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task3Advice",task3Advice);
								}
								//处置情况 第一副网格长查处报告内容task5
								if(FocusReportNode351Enum.VICE_COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(taskCode) && StringUtils.isBlank(task5Advice)){
									task5Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task5Advice",task5Advice);
								}
								//处置情况，乡镇查处报告内容task6
								if((FocusReportNode351Enum.STEET_HANDLE_NODE_NAME.getTaskNodeName().equals(taskCode))
										&& StringUtils.isBlank(task6Advice)){
									task6Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task6Advice",task6Advice);
								}
								//立案查处task9
								if((FocusReportNode351Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(taskCode))
										&& StringUtils.isBlank(task9Advice)){
									task9Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task9Advice",task9Advice);
								}
								//处置情况，行政处罚task10
								if((FocusReportNode351Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(taskCode))
										&& StringUtils.isBlank(task10Advice)){
									task10Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task10Advice",task10Advice);
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
