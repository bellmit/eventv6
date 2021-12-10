package cn.ffcs.zhsq.reportFocus.houseHiddenDanger.service.impl;

import cn.ffcs.shequ.zzgl.service.holidayinfo.IHolidayInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.houseHiddenDanger.ReportHHDMapper;
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
import org.apache.ibatis.session.RowBounds;
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
 * @Description: 房屋安全隐患，相关涉及表：T_EVENT_HOUSE_HIDDEN_DANGER
 * @ClassName:   ReportHHDServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月6日 上午11:10:25
 */
@Service("reportHHDService")
public class ReportHHDServiceImpl extends ReportTwoViolationPreServiceImpl {
	@Autowired
	private ReportHHDMapper reportHHDMapper;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IHolidayInfoService holidayInfoService;

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
				put(50, "4JurisdictionSimplify");
				put(51,"4JurisdictionWithMarker");
				put(52,"4JurisdictionWithRegionPath");
				put(6, "4Archived");
				put(7, "4MsgReading");
				put(8, "4StatusTrack");
	        }
	    };
	}
	
	@Override
	public String saveReportExtendInfo(Map<String, Object> hhdParam, UserInfo userInfo) {
		if(hhdParam == null || hhdParam.isEmpty()) {
			throw new IllegalArgumentException("缺少需要操作的房屋安全隐患！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		Long userId = userInfo.getUserId();
		String hhdUUID = null;
		
		if(CommonFunctions.isBlank(hhdParam, "creator")) {
			hhdParam.put("creator", userId);
		}
		if(CommonFunctions.isBlank(hhdParam, "updator")) {
			hhdParam.put("updator", userId);
		}
		if(CommonFunctions.isNotBlank(hhdParam, "hhdType")) {
			String hhdType = hhdParam.get("hhdType").toString();
			
			if(!hhdType.startsWith(",")) {
				hhdType = "," + hhdType;
			}
			
			if(!hhdType.endsWith(",")) {
				hhdType += ",";
			}
			
			hhdParam.put("hhdType", hhdType);
		}
		if(CommonFunctions.isNotBlank(hhdParam, "feedbackTime")) {
			Object feedbackTimeObj = hhdParam.get("feedbackTime");
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
			
			if(feedbackTime != null) {
				hhdParam.put("feedbackTime", feedbackTime);
			} else {
				hhdParam.remove("feedbackTime");
			}
		}
		
		if(CommonFunctions.isNotBlank(hhdParam, "hhdUUID")) {
			if(reportHHDMapper.update(hhdParam) > 0) {
				hhdUUID = hhdParam.get("hhdUUID").toString();
			}
		} else {
			if(CommonFunctions.isBlank(hhdParam, "reportUUID")) {
				throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
			}
			
			//为了保证记录的唯一性
			Map<String, Object> hhdMap = reportHHDMapper.findByIdSimple(hhdParam);
			
			if(hhdMap != null && !hhdMap.isEmpty()) {
				Map<String, Object> hhdMapAfter = new HashMap<String, Object>(),
									hhdMapAfterRemain = new HashMap<String, Object>();
				hhdMapAfter.putAll(hhdMap);
				hhdMapAfter.putAll(hhdParam);
				
				for(String key : hhdMapAfter.keySet()) {
					if(CommonFunctions.isBlank(hhdMap, key) 
							|| !hhdMapAfter.get(key).equals(hhdMap.get(key))) {
						hhdMapAfterRemain.put(key, hhdMapAfter.get(key));
					}
				}
				
				if(!hhdMapAfterRemain.isEmpty()) {
					hhdUUID = hhdMap.get("hhdUUID").toString();
					hhdMapAfterRemain.put("hhdUUID", hhdUUID);
					
					if(reportHHDMapper.update(hhdMapAfterRemain) > 0) {
						hhdUUID = hhdMap.get("hhdUUID").toString();
					}
				} else {
					hhdUUID = hhdMap.get("hhdUUID").toString();
				}
			} else if(reportHHDMapper.insert(hhdParam) > 0) {
				if(CommonFunctions.isNotBlank(hhdParam, "hhdUUID")) {
					hhdUUID = hhdParam.get("hhdUUID").toString();
				}
			}
			
			saveReportExtraInfo(hhdParam);
		}
		
		return hhdUUID;
	}

	@Override
	public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo,
			Map<String, Object> params) {
		Map<String, Object> hhdParam = new HashMap<String, Object>(), hhdParamMap = null;
		boolean isWithReportFocus = true;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
			isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
		}
		
		if(StringUtils.isNotBlank(reportUUID)) {
			hhdParam.put("reportUUID", reportUUID);
		} else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
			hhdParam.put("reportUUID", params.get("reportUUID"));
		} else if(CommonFunctions.isNotBlank(params, "hhdUUID")) {
			hhdParam.put("hhdUUID", params.get("hhdUUID"));
		} else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
			hhdParam.put("reportId", params.get("reportId"));
		}
		
		if(hhdParam.isEmpty()) {
			throw new IllegalArgumentException("缺失唯一性条件，不可查询房屋安全隐患！");
		}
		
		if(isWithReportFocus) {
			hhdParamMap = reportHHDMapper.findById(hhdParam);
			
			if(hhdParamMap != null && !hhdParamMap.isEmpty()) {
				List<Map<String, Object>> hhdMapList = new ArrayList<Map<String, Object>>();
				
				hhdMapList.add(hhdParamMap);
				
				if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
					params.put("userOrgCode", userInfo.getOrgCode());
				}
				
				formatDataOut(hhdMapList, params);
			}
		} else {
			hhdParamMap = reportHHDMapper.findByIdSimple(hhdParam);
		}
		
		findReportExtraInfo(hhdParamMap, params);
		
		return hhdParamMap;
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
	public String capNumberCfgBizCode() {
		return ReportNumberCfgBizCodeEnum.houseHiddenDanger.getBizCode();
	}
	
	protected ReportTwoViolationPreMapper capReportExtendMapper() {
		return reportHHDMapper;
	}
	
	protected String capBizType4Attachment() {
		return "HOUSE_HIDDEN_DANGER";
	}
	
	protected String capBizType4ResmarkerInfo() {
		return "HOUSE_HIDDEN_DANGER";
	}
	
	/**
	 * 传入的间隔需要减1，因为无法确认传入的间隔是否减了1，因此该操作有调用方完成
	 * 传入的间隔需要减1是因为语句中简单的+1无法将节假日剔除
	 * @param params
	 */
	@SuppressWarnings("unused")
	private void formatParamIn4StatusTrack(Map<String, Object> params) {
		int listType = 0, subFormTypeId = 0,
			routineInterval3530101 = 9,	//房屋安全隐患——常规——一般，时间间隔为10个工作日
			routineInterval3530102 = 0,		//房屋安全隐患——常规——重大，时间间隔为1个工作日
			routineInterval3530201 = 0;		//房屋安全隐患——突发，时间间隔为1个工作日
		Date today = null, routineDayEnd = null,
			 routineDayStart3530101 = null,
			 routineDayStart3530102 = null,
			 routineDayStart3530201 = null;
		try {
			today = DateUtils.convertStringToDate(DateUtils.getToday(), DateUtils.PATTERN_DATE);
		} catch (ParseException e) {}
		
		formatParamIn4Todo(params);
		
		if(CommonFunctions.isNotBlank(params, "listType")) {
			listType = Integer.valueOf(params.get("listType").toString());
		}
		
		routineDayEnd = holidayInfoService.getWorkDateByAfterWorkDay(today, -1);
		
		if(!today.equals(routineDayEnd)) {
			routineDayEnd = today;
		}
		
		if(CommonFunctions.isNotBlank(params, "routineInterval3530101")) {
			try {
				routineInterval3530101 = Integer.valueOf(params.get("routineInterval3530101").toString());
			} catch(NumberFormatException e) {}
		}
		
		if(CommonFunctions.isNotBlank(params, "routineInterval3530102")) {
			try {
				routineInterval3530102 = Integer.valueOf(params.get("routineInterval3530102").toString());
			} catch(NumberFormatException e) {}
		}
		
		if(CommonFunctions.isNotBlank(params, "routineInterval3530201")) {
			try {
				routineInterval3530201 = Integer.valueOf(params.get("routineInterval3530201").toString());
			} catch(NumberFormatException e) {}
		}
		
		routineDayStart3530101 = holidayInfoService.getWorkDateByAfterWorkDay(routineDayEnd, -routineInterval3530101);
		routineDayStart3530102 = holidayInfoService.getWorkDateByAfterWorkDay(routineDayEnd, -routineInterval3530102);
		routineDayStart3530201 = holidayInfoService.getWorkDateByAfterWorkDay(routineDayEnd, -routineInterval3530201);
		
		params.put("subFormTypeId", subFormTypeId);
		params.put("routineDayStart3530101", routineDayStart3530101);
		params.put("routineDayStart3530102", routineDayStart3530102);
		params.put("routineDayStart3530201", routineDayStart3530201);
		params.put("routineDayEnd", routineDayEnd);
	}
	
	/**
	 * 获取待办列表数量
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unused")
	private int findCount4StatusTrack(Map<String, Object> params) {
		int total = 0;
		
		total = reportHHDMapper.findCount4StatusTrack(params);
		
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
		
		total = reportHHDMapper.findCount4StatusTrack(params);
		
		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			reportList = reportHHDMapper.findList4StatusTrack(params, rowBounds);
			
			formatDataOut(reportList, params);
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination reportPagination = new EUDGPagination(total, reportList);
		
		return reportPagination;
	}
	
	/**
	 * 格式化输入参数
	 * @param params
	 * @throws ZhsqEventException 
	 */
	protected void formatParamIn(Map<String, Object> params) throws ZhsqEventException {
		super.formatParamIn(params);
		
		if(CommonFunctions.isNotBlank(params, "hhdType")) {
			String hhdType = params.get("hhdType").toString();
			
			if(!hhdType.startsWith(",")) {
				hhdType = "," + hhdType;
			}
			
			if(!hhdType.endsWith(",")) {
				hhdType += ",";
			}
			
			params.put("hhdType", hhdType);
		}
	}
	
	/**
	 * 数据格式化输出
	 * @param reportList
	 * @param params
	 */
	protected void formatDataOut(List<Map<String, Object>> reportList, Map<String, Object> params) {
		if(reportList != null && reportList.size() > 0) {
			String userOrgCode = null, hhdTypeStr = null, HHD_TYPE_OTHER = "99";
			boolean isDictTransfer = true;
			List<BaseDataDict> reportStatusDictList = null,
							   riskTypeDictList = null,
							   riskDescribeDictList = null;
			int listType = 0;
			StringBuffer hhdTypeBuffer = null;
			
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
				riskTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(HHDDictEnum.HIDDEN_DANGER_TYPE.getDictCode(), userOrgCode);
				riskDescribeDictList = baseDictionaryService.getDataDictListOfSinglestage(HHDDictEnum.HHD_TYPE.getDictCode(), userOrgCode);
				
				if(listType == 1) {
					reportStatusDictList = new ArrayList<BaseDataDict>();
					//草稿不增添字典，因为只有草稿列表会用到
					BaseDataDict draftDict = new BaseDataDict();
					draftDict.setDictGeneralCode(HHDReportStatusEnum.DRAFT_STATUS.getReportStatus());
					draftDict.setDictName(HHDReportStatusEnum.DRAFT_STATUS.getReportStatusName());
					reportStatusDictList.add(draftDict);
				} else {
					reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(HHDDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
				}
			}
			
			for(Map<String, Object> reportMap : reportList) {
				if(CommonFunctions.isNotBlank(reportMap, "feedbackTime")) {
					reportMap.put("feedbackTimeStr", DateUtils.formatDate((Date) reportMap.get("feedbackTime"), DateUtils.PATTERN_24TIME));
				}
				
				if(CommonFunctions.isNotBlank(reportMap, "hhdType")) {
					hhdTypeBuffer = new StringBuffer("");
					hhdTypeStr = reportMap.get("hhdType").toString();
					
					for(String hhdType : hhdTypeStr.split(",")) {
						if(StringUtils.isNotBlank(hhdType)) {
							if(HHD_TYPE_OTHER.equals(hhdType)) {
								if(CommonFunctions.isNotBlank(reportMap, "riskDescribe")) {
									hhdTypeBuffer.append("、").append(reportMap.get("riskDescribe"));
								}
							} else if(riskDescribeDictList != null) {
								for(BaseDataDict riskDescribeDict : riskDescribeDictList) {
									if(hhdType.equals(riskDescribeDict.getDictGeneralCode())) {
										hhdTypeBuffer.append("、").append(riskDescribeDict.getDictName());
										break;
									}
								}
							}
						}
					}
					
					if(hhdTypeStr.startsWith(",")) {
						hhdTypeStr = hhdTypeStr.substring(1);
					}
					
					if(hhdTypeStr.endsWith(",")) {
						hhdTypeStr = hhdTypeStr.substring(0, hhdTypeStr.length() - 1);
					}
					
					reportMap.put("hhdType", hhdTypeStr);
					
					if(hhdTypeBuffer.length() > 0) {
						reportMap.put("hhdTypeName", hhdTypeBuffer.substring(1));
					}
				}
				
				DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
				DataDictHelper.setDictValueForField(reportMap, "riskType", "riskTypeName", riskTypeDictList);
			}
		}
	}

	/**
	 * 判断地域信息是否符合要求（原判断是在两违处理类中，现需要先实现房屋安全隐患流程优化，故方法先抽出来，后期所有入格改完，再合并回去）
	 * @param orgEntityInfo	地域信息
	 * @param params
	 * 			dataSource		数据来源
	 * 			isEditableNode	是否可编辑，true表示当前节点需要选择到村二级网格，默认为false
	 * @return
	 * @throws Exception
	 */
	protected boolean isRegionSatisfied(OrgEntityInfoBO orgEntityInfo, Map<String, Object> params) throws Exception {
		boolean flag = true, isEditableNode = false;
		String regionChiefLevel = null, dataSource = null, regionCode = null;

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

		//来源为 01 的，地址控件必须选择到网格层级
		//或者当前环节为第一副网格长核实环节时，无论哪个来源都需要选择到网格层级，因为启动子流程时村社区向下寻找二级网格长找不到 isEditableNode
		if(isEditableNode) {
			if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)) {
				if(logger.isErrorEnabled()) {
					logger.error("地域编码【" + regionCode + "】不是网格层级！");
				}

				throw ZhsqEventExceptionEnum.NOT_GRID_LEVEL_REGION_EXCEPTION.getZhsqEventException();
			}
		} else {
			//来源不是 01 的，地址控件需要选择到网格层级或村社区层级
			if(regionChiefLevel.compareTo(ConstantValue.STREET_ORG_LEVEL) < 0
				/*&& !ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel) && !ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)*/){
				throw new ZhsqEventException("地域编码【" + regionCode + "】要在乡镇街道层级或以下层级，请检查！");
			}
		}

		return flag;
	}
	
}
