package cn.ffcs.zhsq.reportFocus.martyrsFacility.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.Punycode;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.gis.base.service.IResMarkerService;
import cn.ffcs.gis.mybatis.domain.base.ResMarker;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.martyrsFacility.ReportMarFacMapper;
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
 * @Description: 信访人员稳控，相关表：T_EVENT_PETITION_PERSON
 * @ClassName:   reportPetPerServiceImpl   
 * @author:      郭昱丹(guoyd)
 * @date:        2021年5月7日 上午10:16:15
 */
@Service("reportMarFacService")
public class ReportMarFacServiceImpl extends ReportTwoViolationPreServiceImpl {

	@Autowired
	private ReportMarFacMapper reportMarFacMapper;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IResMarkerService resGisMarkerService;
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
	public String saveReportExtendInfo(Map<String, Object> martyrsFacility, UserInfo userInfo) throws Exception {
		if(martyrsFacility == null || martyrsFacility.isEmpty()) {
			throw new IllegalArgumentException("缺少需要操作的烈士纪念设施！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		Long userId = userInfo.getUserId();
		String optUUID = null;
		
		if(CommonFunctions.isBlank(martyrsFacility, "creator")) {
			martyrsFacility.put("creator", userId);
		}
		if(CommonFunctions.isBlank(martyrsFacility, "updator")) {
			martyrsFacility.put("updator", userId);
		}
		
		if(CommonFunctions.isNotBlank(martyrsFacility, "feedbackTime")) {
			Object feedbackTimeObj = martyrsFacility.get("feedbackTime");
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
			
			martyrsFacility.put("feedbackTime", feedbackTime);
		} else if(CommonFunctions.isBlank(martyrsFacility, "feedbackTime") && CommonFunctions.isNotBlank(martyrsFacility, "feedbackTimeStr")) {
			Date feedbackTime = null;
			String feedbackTimeStr = martyrsFacility.get("feedbackTimeStr").toString();
			
			try {
				feedbackTime = DateUtils.convertStringToDate(feedbackTimeStr, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(feedbackTime != null) {
				martyrsFacility.put("feedbackTime", feedbackTime);
			}
		}
		
		if(CommonFunctions.isNotBlank(martyrsFacility, "optUUID")) {
			if(reportMarFacMapper.update(martyrsFacility) > 0) {
				optUUID = martyrsFacility.get("optUUID").toString();
			}
		} else {
			if(CommonFunctions.isBlank(martyrsFacility, "reportUUID")) {
				throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
			}
			
			//为了保证记录的唯一性
			Map<String, Object> ffpMap = reportMarFacMapper.findByIdSimple(martyrsFacility);
			
			if(ffpMap != null && !ffpMap.isEmpty()) {
				Map<String, Object> ffpMapAfter = new HashMap<String, Object>(),
									ffpMapAfterRemain = new HashMap<String, Object>();
				ffpMapAfter.putAll(ffpMap);
				ffpMapAfter.putAll(martyrsFacility);
				
				for(String key : ffpMapAfter.keySet()) {
					if(CommonFunctions.isBlank(ffpMap, key) 
							|| !ffpMapAfter.get(key).equals(ffpMap.get(key))) {
						ffpMapAfterRemain.put(key, ffpMapAfter.get(key));
					}
				}
				
				if(!ffpMapAfterRemain.isEmpty()) {
					optUUID = ffpMap.get("optUUID").toString();
					ffpMapAfterRemain.put("optUUID", optUUID);
					
					if(reportMarFacMapper.update(ffpMapAfterRemain) > 0) {
						optUUID = ffpMap.get("optUUID").toString();
					}
				} else {
					optUUID = ffpMap.get("optUUID").toString();
				}
			} else if(reportMarFacMapper.insert(martyrsFacility) > 0) {
				if(CommonFunctions.isNotBlank(martyrsFacility, "optUUID")) {
					optUUID = martyrsFacility.get("optUUID").toString();
				}
			}
			
			saveReportExtraInfo(martyrsFacility);
			saveReportExtraInfo_ext(martyrsFacility);
		}
		
		return optUUID;
	}
	
	protected void saveReportExtraInfo_ext(Map<String, Object> params) {
		Long reportId = null;
		
		if(CommonFunctions.isNotBlank(params, "reportId")) {
			reportId = Long.valueOf(params.get("reportId").toString());
		}
		
		if(reportId != null && reportId > 0) {
			saveResmarkerInfo_ext(reportId, params);
		}
	}
	
	private void saveResmarkerInfo_ext(Long reportId, Map<String, Object> params) {
		String resMarkerLongitude = null, resMarkerLatitude = null, resMarkerMapType = null;
		
		if(CommonFunctions.isNotBlank(params, "resMarker_occurred.x")) {
			resMarkerLongitude = params.get("resMarker_occurred.x").toString();
		} else if(CommonFunctions.isNotBlank(params, "resMarker_occurred.longitude")) {
			resMarkerLongitude = params.get("resMarker_occurred.longitude").toString();
		}
		if(CommonFunctions.isNotBlank(params, "resMarker_occurred.y")) {
			resMarkerLatitude = params.get("resMarker_occurred.y").toString();
		} else if(CommonFunctions.isNotBlank(params, "resMarker_occurred.latitude")) {
			resMarkerLatitude = params.get("resMarker_occurred.latitude").toString();
		}
		if(CommonFunctions.isNotBlank(params, "resMarker_occurred.mapType")) {
			resMarkerMapType = params.get("resMarker_occurred.mapType").toString();
		}
		
		if (StringUtils.isNotBlank(resMarkerLongitude)
				&& StringUtils.isNotBlank(resMarkerLatitude)
				&& StringUtils.isNotBlank(resMarkerMapType) 
				&& !"0.0".equals(resMarkerLongitude)
				&& !"0.0".equals(resMarkerLatitude)) {
			ResMarker resMarker = new ResMarker();
			
			resMarker.setResourcesId(reportId);
			resMarker.setX(resMarkerLongitude);
			resMarker.setY(resMarkerLatitude);
			resMarker.setCatalog("03");
			resMarker.setMapType(resMarkerMapType);
			resMarker.setMarkerType(capBizType4ResmarkerInfo());
			
			resGisMarkerService.saveOrUpdateResMarker(resMarker);
		}
	}
	
	@Override
	public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo,
			Map<String, Object> params) {
		Map<String, Object> martyrsFacility = new HashMap<String, Object>(), martyrsFacilityMap = null;
		boolean isWithReportFocus = true;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
			isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
		}
		
		if(StringUtils.isNotBlank(reportUUID)) {
			martyrsFacility.put("reportUUID", reportUUID);
		} else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
			martyrsFacility.put("reportUUID", params.get("reportUUID"));
		} else if(CommonFunctions.isNotBlank(params, "optUUID")) {
			martyrsFacility.put("optUUID", params.get("optUUID"));
		} else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
			martyrsFacility.put("reportId", params.get("reportId"));
		}
		
		if(martyrsFacility.isEmpty()) {
			throw new IllegalArgumentException("缺失唯一性条件，不可查询烈士纪念设施！");
		}
		
		if(isWithReportFocus) {
			martyrsFacilityMap = reportMarFacMapper.findById(martyrsFacility);
			
			if(martyrsFacilityMap != null && !martyrsFacilityMap.isEmpty()) {
				List<Map<String, Object>> ffpMapList = new ArrayList<Map<String, Object>>();
				
				ffpMapList.add(martyrsFacilityMap);
				
				if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
					params.put("userOrgCode", userInfo.getOrgCode());
				}
				
				formatDataOut(ffpMapList, params);
			}
		} else {
			martyrsFacilityMap = reportMarFacMapper.findByIdSimple(martyrsFacility);
		}
		
		findReportExtraInfo(martyrsFacilityMap, params);
		
		return martyrsFacilityMap;
	}
	
	
	@Override
	public String capNumberCfgBizCode() {
		return ReportNumberCfgBizCodeEnum.martyrsFacility.getBizCode();
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
		if(MarFacDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource) || isEditableNode) {
			if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)){
				if(logger.isErrorEnabled()) {
					logger.error("地域编码【" + regionCode + "】不是网格层级！");
				}

				throw ZhsqEventExceptionEnum.NOT_GRID_LEVEL_REGION_EXCEPTION.getZhsqEventException();
			}
		} else if(MarFacDataSourceEnum.DEPARTMENT_INSPECTION.getDataSource().equals(dataSource)
				||MarFacDataSourceEnum.TIP_OFFS.getDataSource().equals(dataSource)
				||MarFacDataSourceEnum.NEWS_MEDIA.getDataSource().equals(dataSource)
				||MarFacDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource)){
			//来源是 03.04.05 的，地址控件需要选择到村/社区层级
			if(!ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)
					&&!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)){
				if(logger.isErrorEnabled()) {
					logger.error("地域编码【" + regionCode + "】至少需要到村/社区层级！");
				}

				throw new ZhsqEventException("地域编码【" + regionCode + "】至少需要村/社区层级！");
			}
		}
		
		
		return flag;
	}
	
	protected ReportTwoViolationPreMapper capReportExtendMapper() {
		return reportMarFacMapper;
	}
	
	protected String capBizType4Attachment() {
		return "MARTYRS_FACILITY";
	}
	
	protected String capBizType4ResmarkerInfo() {
		return "MARTYRS_FACILITY";
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
					damageModeDictList = null,	feedbackTypeDictList = null,
							doTypeDictList = null,acceptanceResultDictList = null;
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
				
				damageModeDictList = baseDictionaryService.getDataDictListOfSinglestage(MarFacDictEnum.DAMAGE_MODE.getDictCode(), userOrgCode);
				feedbackTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(MarFacDictEnum.FEEDBACK_TYPE.getDictCode(), userOrgCode);
				doTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(MarFacDictEnum.DO_TYPE.getDictCode(), userOrgCode);
				acceptanceResultDictList = baseDictionaryService.getDataDictListOfSinglestage(MarFacDictEnum.ACCEPTANCE_RESULT.getDictCode(), userOrgCode);
				
				if(listType == 1) {
					reportStatusDictList = new ArrayList<BaseDataDict>();
					//草稿不增添字典，因为只有草稿列表会用到
					BaseDataDict draftDict = new BaseDataDict();
					draftDict.setDictGeneralCode(MarFacReportStatusEnum.DRAFT_STATUS.getReportStatus());
					draftDict.setDictName(MarFacReportStatusEnum.DRAFT_STATUS.getReportStatusName());
					reportStatusDictList.add(draftDict);
				} else {
					reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(MarFacDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
				}
			}
			
			for(Map<String, Object> reportMap : reportList) {
				if(CommonFunctions.isNotBlank(reportMap, "feedbackTime")) {
					reportMap.put("feedbackTimeStr", DateUtils.formatDate((Date) reportMap.get("feedbackTime"), DateUtils.PATTERN_DATE));
				}
				if(CommonFunctions.isNotBlank(reportMap, "isDamaged")) {
					String isDamaged = cn.ffcs.common.utils.StringUtils.toString(reportMap.get("isDamaged"));
					if("1".equals(isDamaged)) {
						reportMap.put("isDamagedStr", "是");
					}else if("0".equals(isDamaged)) {
						reportMap.put("isDamagedStr", "否");
					}
				}
				if(CommonFunctions.isNotBlank(reportMap, "dataSource")) {
					reportMap.put("dataSourceName", MarFacDataSourceEnum.getValue(cn.ffcs.common.utils.StringUtils.toString(reportMap.get("dataSource"))));
				}
				
				DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
				DataDictHelper.setDictValueForField(reportMap, "damageMode", "damageModeName", damageModeDictList);
				DataDictHelper.setDictValueForField(reportMap, "feedbackType", "feedbackTypeName", feedbackTypeDictList);
				DataDictHelper.setDictValueForField(reportMap, "doType", "doTypeName", doTypeDictList);
				DataDictHelper.setDictValueForField(reportMap, "acceptanceResult", "acceptanceResultName", acceptanceResultDictList);
			}
		}
	}
	
	/**
	 * 传入的间隔需要减1，因为无法确认传入的间隔是否减了1，因此该操作有调用方完成
	 * 传入的间隔需要减1是因为语句中简单的+1无法将节假日剔除
	 * @param params
	 */
	@SuppressWarnings("unused")
	private void formatParamIn4StatusTrack(Map<String, Object> params) {
		
		formatParamIn4Todo(params);
	}
	
	@Override
	public EUDGPagination findPagination4ReportExtend(int pageNo, int pageSize, Map<String, Object> params) throws Exception {
		EUDGPagination reportPagination = null;
		int listType = 0;
		String METHOD_FORMAT_IN = "formatParamIn", METHOD_LIST = "findPagination";

		params = params == null ? new HashMap<String, Object>() : params;
		
		formatParamIn(params);
		
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {}
		}
		
		if(capMethodMap().containsKey(listType)) {
			try {
				//getMethod () 获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法。
				this.getClass().getMethod(METHOD_FORMAT_IN + capMethodMap().get(listType), new Class[] {Map.class}).invoke(this, params);
			} catch(NoSuchMethodException e) {
				//getDeclaredMethod() 获取的是类自身声明的所有方法，包含public、protected和private方法。
				this.getClass().getDeclaredMethod(METHOD_FORMAT_IN + capMethodMap().get(listType), new Class[] {Map.class}).invoke(this, params);
			}
			
			try {
				reportPagination = (EUDGPagination) this.getClass().getMethod(METHOD_LIST + capMethodMap().get(listType), new Class[] {int.class, int.class, Map.class}).invoke(this, pageNo, pageSize, params);
			} catch(NoSuchMethodException e) {
				reportPagination = (EUDGPagination) this.getClass().getDeclaredMethod(METHOD_LIST + capMethodMap().get(listType), new Class[] {int.class, int.class, Map.class}).invoke(this, pageNo, pageSize, params);
			}
		}
		
		return reportPagination;
	}
	
	@Override
	public int findCount4ReportExtend(Map<String, Object> params) throws Exception {
		int total = 0, listType = 0;
		String METHOD_FORMAT_IN = "formatParamIn", METHOD_COUNT = "findCount";
		
		params = params == null ? new HashMap<String, Object>() : params;
		
		formatParamIn(params);
		
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {}
		}
		
		if(capMethodMap().containsKey(listType)) {
			try {
				this.getClass().getMethod(METHOD_FORMAT_IN + capMethodMap().get(listType), new Class[] {Map.class}).invoke(this, params);
			} catch(NoSuchMethodException e) {
				this.getClass().getDeclaredMethod(METHOD_FORMAT_IN + capMethodMap().get(listType), new Class[] {Map.class}).invoke(this, params);
			}
			
			try {
				total = (int) this.getClass().getMethod(METHOD_COUNT + capMethodMap().get(listType), new Class[] {Map.class}).invoke(this, params);
			} catch(NoSuchMethodException e) {
				total = (int) this.getClass().getDeclaredMethod(METHOD_COUNT + capMethodMap().get(listType), new Class[] {Map.class}).invoke(this, params);
			}
		}
		
		return total;
	}
	
	/**
	 * 获取待办列表数量
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unused")
	private int findCount4StatusTrack(Map<String, Object> params) {
		int total = 0;
		
		total = reportMarFacMapper.findCount4StatusTrack(params);
		
		return total;
	}
	
	/**
	 * 分页获取待办列表记录
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unused")
	private EUDGPagination findPagination4StatusTrack(int pageNo, int pageSize, Map<String, Object> params) {
		int total = 0;
		List<Map<String, Object>> reportList = null;
		
		total = reportMarFacMapper.findCount4StatusTrack(params);
		
		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			reportList = reportMarFacMapper.findList4StatusTrack(params, rowBounds);
			
			formatDataOut(reportList, params);
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination reportPagination = new EUDGPagination(total, reportList);
		
		return reportPagination;
	}
	
}
