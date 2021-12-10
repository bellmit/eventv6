package cn.ffcs.zhsq.reportFocus.businessProblem.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.businessProblem.ReportBusProMapper;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.twoViolationPre.ReportTwoViolationPreMapper;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportNumberCfgBizCodeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportTwoViolationPreServiceImpl;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventExceptionEnum;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.data.DateUtils;

/**
 * @Description: 营商问题，相关表：T_EVENT_BUSINESS_PROBLEM
 * @ClassName:   ReportBusProServiceImpl   
 * @author:      郭昱丹(guoyd)
 * @date:        2021年5月7日 上午10:16:15
 */
@Service("reportBusProService")
public class ReportBusProServiceImpl extends ReportTwoViolationPreServiceImpl {

	@Autowired
	private ReportBusProMapper reportBusProMapper;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
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
	public String saveReportExtendInfo(Map<String, Object> busProParam, UserInfo userInfo) throws Exception {
		if(busProParam == null || busProParam.isEmpty()) {
			throw new IllegalArgumentException("缺少需要操作的营商问题！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		Long userId = userInfo.getUserId();
		String optUUID = null;
		
		if(CommonFunctions.isBlank(busProParam, "creator")) {
			busProParam.put("creator", userId);
		}
		if(CommonFunctions.isBlank(busProParam, "updator")) {
			busProParam.put("updator", userId);
		}
		
		if(CommonFunctions.isNotBlank(busProParam, "feedbackTime")) {
			Object feedbackTimeObj = busProParam.get("feedbackTime");
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
			
			busProParam.put("feedbackTime", feedbackTime);
		} else if(CommonFunctions.isBlank(busProParam, "feedbackTime") && CommonFunctions.isNotBlank(busProParam, "feedbackTimeStr")) {
			Date feedbackTime = null;
			String feedbackTimeStr = busProParam.get("feedbackTimeStr").toString();
			
			try {
				feedbackTime = DateUtils.convertStringToDate(feedbackTimeStr, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(feedbackTime != null) {
				busProParam.put("feedbackTime", feedbackTime);
			}
		}
		
		if(CommonFunctions.isNotBlank(busProParam, "doDate")) {
			Object doDateObj = busProParam.get("doDate");
			Date doDate = null;
			
			if(doDateObj instanceof String) {
				try {
					doDate = DateUtils.convertStringToDate(doDateObj.toString(), DateUtils.PATTERN_24TIME);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if(doDateObj instanceof Date) {
				doDate = (Date) doDateObj;
			}
			
			busProParam.put("doDate", doDate);
		} else if(CommonFunctions.isBlank(busProParam, "doDate") && CommonFunctions.isNotBlank(busProParam, "doDateStr")) {
			Date doDate = null;
			String doDateStr = busProParam.get("doDateStr").toString();
			
			try {
				doDate = DateUtils.convertStringToDate(doDateStr, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(doDate != null) {
				busProParam.put("doDate", doDate);
			}
		}
		
		if(CommonFunctions.isNotBlank(busProParam, "task3ReceiveTime")) {
			String task3ReceiveTime = cn.ffcs.common.utils.StringUtils.toString(busProParam.get("task3ReceiveTime"));
			if(!cn.ffcs.common.utils.StringUtils.isBlank(task3ReceiveTime)) {
				busProParam.put("task3ReceiveTime", new Date());
			}
		}
		
		if(CommonFunctions.isNotBlank(busProParam, "task3SubTime")) {
			String task3SubTime = cn.ffcs.common.utils.StringUtils.toString(busProParam.get("task3SubTime"));
			if(!cn.ffcs.common.utils.StringUtils.isBlank(task3SubTime)) {
				busProParam.put("task3SubTime", new Date());
			}
		}
		
		if(CommonFunctions.isNotBlank(busProParam, "optUUID")) {
			if(reportBusProMapper.update(busProParam) > 0) {
				optUUID = busProParam.get("optUUID").toString();
			}
		} else {
			if(CommonFunctions.isBlank(busProParam, "reportUUID")) {
				throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
			}
			
			//为了保证记录的唯一性
			Map<String, Object> ffpMap = reportBusProMapper.findByIdSimple(busProParam);
			
			if(ffpMap != null && !ffpMap.isEmpty()) {
				Map<String, Object> ffpMapAfter = new HashMap<String, Object>(),
									ffpMapAfterRemain = new HashMap<String, Object>();
				ffpMapAfter.putAll(ffpMap);
				ffpMapAfter.putAll(busProParam);
				
				for(String key : ffpMapAfter.keySet()) {
					if(CommonFunctions.isBlank(ffpMap, key) 
							|| !ffpMapAfter.get(key).equals(ffpMap.get(key))) {
						ffpMapAfterRemain.put(key, ffpMapAfter.get(key));
					}
				}
				
				if(!ffpMapAfterRemain.isEmpty()) {
					optUUID = ffpMap.get("optUUID").toString();
					ffpMapAfterRemain.put("optUUID", optUUID);
					
					if(reportBusProMapper.update(ffpMapAfterRemain) > 0) {
						optUUID = ffpMap.get("optUUID").toString();
					}
				} else {
					optUUID = ffpMap.get("optUUID").toString();
				}
			} else if(reportBusProMapper.insert(busProParam) > 0) {
				if(CommonFunctions.isNotBlank(busProParam, "optUUID")) {
					optUUID = busProParam.get("optUUID").toString();
				}
			}
			
			saveReportExtraInfo(busProParam);
		}
		
		return optUUID;
	}
	
	
	@Override
	public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo,
			Map<String, Object> params) {
		Map<String, Object> busProParam = new HashMap<String, Object>(), busProParamMap = null;
		boolean isWithReportFocus = true;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
			isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
		}
		
		if(StringUtils.isNotBlank(reportUUID)) {
			busProParam.put("reportUUID", reportUUID);
		} else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
			busProParam.put("reportUUID", params.get("reportUUID"));
		} else if(CommonFunctions.isNotBlank(params, "optUUID")) {
			busProParam.put("optUUID", params.get("optUUID"));
		} else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
			busProParam.put("reportId", params.get("reportId"));
		}
		
		if(busProParam.isEmpty()) {
			throw new IllegalArgumentException("缺失唯一性条件，不可查询营商问题！");
		}
		
		if(isWithReportFocus) {
			busProParamMap = reportBusProMapper.findById(busProParam);
			
			if(busProParamMap != null && !busProParamMap.isEmpty()) {
				List<Map<String, Object>> ffpMapList = new ArrayList<Map<String, Object>>();
				
				ffpMapList.add(busProParamMap);
				
				if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
					params.put("userOrgCode", userInfo.getOrgCode());
				}
				
				formatDataOut(ffpMapList, params);
			}
		} else {
			busProParamMap = reportBusProMapper.findByIdSimple(busProParam);
		}
		
		findReportExtraInfo(busProParamMap, params);
		
		return busProParamMap;
	}
	
	@Override
	public String capNumberCfgBizCode() {
		return ReportNumberCfgBizCodeEnum.businessProblem.getBizCode();
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
		boolean flag = true,isEditableNode = false;
		String regionChiefLevel = null, regionCode = null,dataSource = null;
		
		if(orgEntityInfo != null) {
			regionCode = orgEntityInfo.getOrgCode();
			regionChiefLevel = orgEntityInfo.getChiefLevel();
		}

		if(CommonFunctions.isNotBlank(params,"dataSource")){
			dataSource = String.valueOf(params.get("dataSource"));
		}

		if(CommonFunctions.isNotBlank(params,"isEditableNode")){
			isEditableNode = Boolean.valueOf(String.valueOf(params.get("isEditableNode")));
		}

		//dataSource = "01" 时 所属区域才需要精确到网格层级
		if(BusProDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource) || isEditableNode) {
			if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)){
				if(logger.isErrorEnabled()) {
					logger.error("地域编码【" + regionCode + "】不是网格层级！");
				}

				throw ZhsqEventExceptionEnum.NOT_GRID_LEVEL_REGION_EXCEPTION.getZhsqEventException();
			}
		} else if(BusProDataSourceEnum.ENTERPRISE_SPECIALIST.getDataSource().equals(dataSource)
				||BusProDataSourceEnum.ENTERPRISE_THREE.getDataSource().equals(dataSource)
				||BusProDataSourceEnum.MUNICIPAL_DEPARTMENTS.getDataSource().equals(dataSource)
				||BusProDataSourceEnum.INDUSTRY_ASSOCIATION.getDataSource().equals(dataSource)
				||BusProDataSourceEnum.OFFICIAL_ACCOUNT.getDataSource().equals(dataSource)
				){
			//来源不是 03~06 的，地址控件需要选择到村社区层级
			if(!ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)
					&&!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)){
				if(logger.isErrorEnabled()) {
					logger.error("地域编码【" + regionCode + "】至少需要村/社区层级！");
				}

				throw new ZhsqEventException("地域编码【" + regionCode + "】至少需要村/社区层级！");
			}
		}else if(BusProDataSourceEnum.CHAMBER_COMMERCE.getDataSource().equals(dataSource)){
			//来源不是 07 的，地址控件需要选择到街镇层级
			if(!ConstantValue.STREET_ORG_LEVEL.equals(regionChiefLevel)
					&&!ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)
					&&!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)){
				if(logger.isErrorEnabled()) {
					logger.error("地域编码【" + regionCode + "】至少需要街镇层级！");
				}

				throw new ZhsqEventException("地域编码【" + regionCode + "】至少需要街镇层级！");
			}
		}
		
		
		return flag;
	}
	
	protected ReportTwoViolationPreMapper capReportExtendMapper() {
		return reportBusProMapper;
	}
	
	protected String capBizType4Attachment() {
		return "BUSINESS_PROBLEM";
	}
	
	protected String capBizType4ResmarkerInfo() {
		return "BUSINESS_PROBLEM";
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
			List<BaseDataDict> reportStatusDictList = null,
					doConditionDictList = null,
							doConditionDictListV2 = null;
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
				
				doConditionDictList = baseDictionaryService.getDataDictListOfSinglestage(BusProDictEnum.HANDLE_TYPE.getDictCode(), userOrgCode);
				doConditionDictListV2 = baseDictionaryService.getDataDictListOfSinglestage(BusProDictEnum.HANDLE_TYPE_V2.getDictCode(), userOrgCode);
				
				if(listType == 1) {
					reportStatusDictList = new ArrayList<BaseDataDict>();
					//草稿不增添字典，因为只有草稿列表会用到
					BaseDataDict draftDict = new BaseDataDict();
					draftDict.setDictGeneralCode(BusProReportStatusEnum.DRAFT_STATUS.getReportStatus());
					draftDict.setDictName(BusProReportStatusEnum.DRAFT_STATUS.getReportStatusName());
					reportStatusDictList.add(draftDict);
				} else {
					reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(BusProDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
				}
			}
			
			for(Map<String, Object> reportMap : reportList) {
				if(CommonFunctions.isNotBlank(reportMap, "feedbackTime")) {
					reportMap.put("feedbackTimeStr", DateUtils.formatDate((Date) reportMap.get("feedbackTime"), DateUtils.PATTERN_DATE));
				}
				if(CommonFunctions.isNotBlank(reportMap, "doDate")) {
					reportMap.put("doDateStr", DateUtils.formatDate((Date) reportMap.get("doDate"), DateUtils.PATTERN_DATE));
				}
				if(CommonFunctions.isNotBlank(reportMap, "reportObj")) {
					String reportObj = cn.ffcs.common.utils.StringUtils.toString(reportMap.get("reportObj"));
					if("1".equals(reportObj)) {
						reportMap.put("reportObjStr", "企业");
					}else if("2".equals(reportObj)) {
						reportMap.put("reportObjStr", "群众");
					}else if("3".equals(reportObj)) {
						reportMap.put("reportObjStr", "市直部门");
					}else if("4".equals(reportObj)) {
						reportMap.put("reportObjStr", "各级巡视巡察");
					}
				}
				if(CommonFunctions.isNotBlank(reportMap, "isProblem")) {
					String isProblem = cn.ffcs.common.utils.StringUtils.toString(reportMap.get("isProblem"));
					if("1".equals(isProblem)) {
						reportMap.put("isProblemStr", "是");
					}else if("0".equals(isProblem)) {
						reportMap.put("isProblemStr", "否");
					}
				}
				String feedbackType = cn.ffcs.common.utils.StringUtils.toString(reportMap.get("feedbackType"));
				if(CommonFunctions.isNotBlank(reportMap, "feedbackType")) {
					if("1".equals(feedbackType)) {
						reportMap.put("feedbackTypeStr", "中期报告");
					}else if("2".equals(feedbackType)) {
						reportMap.put("feedbackTypeStr", "限期内反馈");
					}else if("3".equals(feedbackType)) {
						reportMap.put("feedbackTypeStr", "限期外反馈");
					}
				}
				if(CommonFunctions.isNotBlank(reportMap, "isOrgScope")) {
					String isOrgScope = cn.ffcs.common.utils.StringUtils.toString(reportMap.get("isOrgScope"));
					if("1".equals(isOrgScope)) {
						reportMap.put("isOrgScopeStr", "是");
					}else if("2".equals(isOrgScope)) {
						reportMap.put("isOrgScopeStr", "否");
					}
				}
				
				DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
				if("1".equals(feedbackType)) {
					DataDictHelper.setDictValueForField(reportMap, "doCondition", "doConditionName", doConditionDictList);
				}else if("2".equals(feedbackType)) {
					DataDictHelper.setDictValueForField(reportMap, "doCondition", "doConditionName", doConditionDictListV2);
				}
				
				if(CommonFunctions.isNotBlank(reportMap, "isDelay")) {
					String isDelay = cn.ffcs.common.utils.StringUtils.toString(reportMap.get("isDelay"));
					if("1".equals(isDelay)) {
						reportMap.put("isDelayStr", "同意");
					}else if("2".equals(isDelay)) {
						reportMap.put("isDelayStr", "不同意");
					}
				}
				if(CommonFunctions.isNotBlank(reportMap, "task3ReceiveTime")) {
					reportMap.put("task3ReceiveTimeStr", DateUtils.formatDate((Date) reportMap.get("task3ReceiveTime"), DateUtils.PATTERN_DATE));
				}
				if(CommonFunctions.isNotBlank(reportMap, "task3SubTime")) {
					reportMap.put("task3SubTimeStr", DateUtils.formatDate((Date) reportMap.get("task3SubTime"), DateUtils.PATTERN_DATE));
				}
				
			}
		}
	}
}
