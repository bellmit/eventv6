package cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl;

import cn.ffcs.file.mybatis.domain.attachment.Attachment;
import cn.ffcs.file.service.IAttachmentService;
import cn.ffcs.gis.base.service.IResMarkerService;
import cn.ffcs.gis.mybatis.domain.base.ResMarker;
import cn.ffcs.shequ.zzgl.service.holidayinfo.IHolidayInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.twoViolationPre.ReportTwoViolationPreMapper;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportFocusExtendServiceImpl;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportNumberCfgBizCodeEnum;
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
 * @Description: 两违防治处理
 * 				 相关表T_EVENT_TWO_VIOLATION_PRE
 * @ClassName:   ReportTwoViolationPreServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年9月14日 下午3:15:22
 */
@Service("reportTwoViolationPreService")
public class ReportTwoViolationPreServiceImpl extends ReportFocusExtendServiceImpl {
	@Autowired
	private ReportTwoViolationPreMapper reportTwoViolationPreMapper;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoService;
	
	@Autowired
	private IHolidayInfoService holidayInfoService;
	
	@Autowired
	private IAttachmentService<Attachment> attachmentService;
	
	@Autowired
	private IResMarkerService resGisMarkerService;
	@Autowired
	private IWorkflow4BaseService workflow4BaseService;
	
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
				put(9, "4StatusTrack");
				put(10, "4StatusTrack");
			}
		};
	}
	
	@Override
	public String saveReportExtendInfo(Map<String, Object> twoViolationPre, UserInfo userInfo) throws Exception {
		if(twoViolationPre == null || twoViolationPre.isEmpty()) {
			throw new IllegalArgumentException("缺少需要操作的两违信息！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		Long userId = userInfo.getUserId();
		String twoVioUUID = null;
		
		if(CommonFunctions.isBlank(twoViolationPre, "creator")) {
			twoViolationPre.put("creator", userId);
		}
		if(CommonFunctions.isBlank(twoViolationPre, "updator")) {
			twoViolationPre.put("updator", userId);
		}
		if(CommonFunctions.isNotBlank(twoViolationPre, "feedbackTime")) {
			Object feedbackTimeObj = twoViolationPre.get("feedbackTime");
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
			
			twoViolationPre.put("feedbackTime", feedbackTime);
		} else if(CommonFunctions.isBlank(twoViolationPre, "feedbackTime") && CommonFunctions.isNotBlank(twoViolationPre, "feedbackTimeStr")) {
			Date feedbackTime = null;
			String feedbackTimeStr = twoViolationPre.get("feedbackTimeStr").toString();
			
			try {
				feedbackTime = DateUtils.convertStringToDate(feedbackTimeStr, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(feedbackTime != null) {
				twoViolationPre.put("feedbackTime", feedbackTime);
			}
		}
		
		if(CommonFunctions.isNotBlank(twoViolationPre, "twoVioUUID")) {
			if(reportTwoViolationPreMapper.update(twoViolationPre) > 0) {
				twoVioUUID = twoViolationPre.get("twoVioUUID").toString();
			}
		} else {
			if(CommonFunctions.isBlank(twoViolationPre, "reportUUID")) {
				throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
			}
			
			//为了保证记录的唯一性
			Map<String, Object> twoVioPreMap = reportTwoViolationPreMapper.findByIdSimple(twoViolationPre);
			
			if(twoVioPreMap != null && !twoVioPreMap.isEmpty()) {
				Map<String, Object> twoVioPreMapAfter = new HashMap<String, Object>(),
									twoVioPreMapRemain = new HashMap<String, Object>();
				twoVioPreMapAfter.putAll(twoVioPreMap);
				twoVioPreMapAfter.putAll(twoViolationPre);
				
				for(String key : twoVioPreMapAfter.keySet()) {
					if(CommonFunctions.isBlank(twoVioPreMap, key) 
							|| !twoVioPreMapAfter.get(key).equals(twoVioPreMap.get(key))) {
						twoVioPreMapRemain.put(key, twoVioPreMapAfter.get(key));
					}
				}
				
				if(!twoVioPreMapRemain.isEmpty()) {
					twoVioUUID = twoVioPreMap.get("twoVioUUID").toString();
					twoVioPreMapRemain.put("twoVioUUID", twoVioUUID);
					
					if(reportTwoViolationPreMapper.update(twoVioPreMapRemain) > 0) {
						twoVioUUID = twoVioPreMap.get("twoVioUUID").toString();
					}
				} else {
					twoVioUUID = twoVioPreMap.get("twoVioUUID").toString();
				}
			} else if(reportTwoViolationPreMapper.insert(twoViolationPre) > 0) {
				if(CommonFunctions.isNotBlank(twoViolationPre, "twoVioUUID")) {
					twoVioUUID = twoViolationPre.get("twoVioUUID").toString();
				}
			}
			
			saveReportExtraInfo(twoViolationPre);
		}
		
		return twoVioUUID;
	}

	@Override
	public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo,
			Map<String, Object> params) {
		Map<String, Object> twoViolationPreParam = new HashMap<String, Object>(), twoViolationPreMap = null;
		boolean isWithReportFocus = true;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
			isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
		}
		
		if(StringUtils.isNotBlank(reportUUID)) {
			twoViolationPreParam.put("reportUUID", reportUUID);
		} else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
			twoViolationPreParam.put("reportUUID", params.get("reportUUID"));
		} else if(CommonFunctions.isNotBlank(params, "twoVioUUID")) {
			twoViolationPreParam.put("twoVioUUID", params.get("twoVioUUID"));
		} else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
			twoViolationPreParam.put("reportId", params.get("reportId"));
		}
		
		if(twoViolationPreParam.isEmpty()) {
			throw new IllegalArgumentException("缺失唯一性条件，不可查询两违信息！");
		}
		
		if(isWithReportFocus) {
			twoViolationPreMap = reportTwoViolationPreMapper.findById(twoViolationPreParam);
			
			if(twoViolationPreMap != null && !twoViolationPreMap.isEmpty()) {
				List<Map<String, Object>> twoVioPreMapList = new ArrayList<Map<String, Object>>();
				
				twoVioPreMapList.add(twoViolationPreMap);
				
				if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
					params.put("userOrgCode", userInfo.getOrgCode());
				}
				
				formatDataOut(twoVioPreMapList, params);
			}
		} else {
			twoViolationPreMap = reportTwoViolationPreMapper.findByIdSimple(twoViolationPreParam);
		}
		
		findReportExtraInfo(twoViolationPreMap, params);
		
		return twoViolationPreMap;
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
		return ReportNumberCfgBizCodeEnum.twoVioPre.getBizCode();
	}
	
	@Override
	public void formatParamIn4Report(Map<String, Object> params) throws Exception {
		isRegionSatisfied(params);
	}
	
	@Override
	public String capSmsContent(String smsContent, Map<String, Object> params, UserInfo userInfo) throws Exception {
		if(StringUtils.isBlank(smsContent)) {
			throw new IllegalArgumentException("缺少短信模板信息！");
		}
		
		if(userInfo == null) {
			throw new IllegalArgumentException("");
		}
		
		if(smsContent.contains("@reporterName@")) {
			String reporterName = "";
			
			if(CommonFunctions.isNotBlank(params, "reporterName")) {
				reporterName = params.get("reporterName").toString();
			}
			
			smsContent = smsContent.replace("@reporterName@", reporterName);
		}
		
		if(smsContent.contains("@reportCode@")) {
			String reportCode = "";
			
			if(CommonFunctions.isNotBlank(params, "reportCode")) {
				reportCode = params.get("reportCode").toString();
			}
			
			smsContent = smsContent.replace("@reportCode@", reportCode);
		}
		
		if(smsContent.contains("@curUserName@")) {
			String curUserName = userInfo.getPartyName();
			
			smsContent = smsContent.replace("@curUserName@", curUserName);
		}
		
		if(smsContent.contains("@curOrgName@")) {
			String curOrgName = userInfo.getOrgName();
			
			smsContent = smsContent.replace("@curOrgName@", curOrgName);
		}
		
		return smsContent;
	}
	
	/**
	 * 判断地域编码是否符合要求
	 * @param params
	 * 			regionCode	地域编码
	 * @return
	 * @throws Exception
	 */
	protected boolean isRegionSatisfied(Map<String, Object> params) throws Exception {
		boolean flag = true, isCheckRegion = true;
		
		if(CommonFunctions.isNotBlank(params, "isCheckRegion")) {
			isCheckRegion = Boolean.valueOf(params.get("isCheckRegion").toString());
		}
		
		if(isCheckRegion && CommonFunctions.isNotBlank(params, "regionCode")) {
			OrgEntityInfoBO orgEntityInfo = null;
			String regionCode = null;

			regionCode = params.get("regionCode").toString();
			
			orgEntityInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(regionCode);
			
			if(orgEntityInfo != null) {
				flag = isRegionSatisfied(orgEntityInfo, params);
			}
		}
		
		return flag;
	}
	
	/**
	 * 判断地域信息是否符合要求
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
		if(TwoVioPreDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource) || isEditableNode) {
			if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)) {
				if(logger.isErrorEnabled()) {
					logger.error("地域编码【" + regionCode + "】不是网格层级！");
				}
				
				throw ZhsqEventExceptionEnum.NOT_GRID_LEVEL_REGION_EXCEPTION.getZhsqEventException();
			}
		} else {
			//来源不是 01 的，地址控件需要选择到网格层级或村社区层级
			if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel) && !ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)){
				throw new ZhsqEventException("地域编码【" + regionCode + "】既不是网格层级，也不是村/社区层级！");
			}
		}
		
		return flag;
	}
	
	protected ReportTwoViolationPreMapper capReportExtendMapper() {
		return reportTwoViolationPreMapper;
	}
	
	protected String capBizType4Attachment() {
		return "TWO_VIO_PRE";
	}
	
	protected String capBizType4ResmarkerInfo() {
		return "TWO_VIO_PRE";
	}
	
	@SuppressWarnings("unused")
	private void formatParamIn4StatusTrack(Map<String, Object> params) {
		int listType = 0, subFormTypeId = 0, routineInterval = 0;
		Date today = null, routineDayStart = null, routineDayEnd = null;
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
		
		switch(listType) {
			case 8: {
				subFormTypeId = Integer.valueOf(FocusReportNode3500Enum.FORM_TYPE_ID.toString());
				routineInterval = 0;
				break;
			}
			case 9: {
				subFormTypeId = Integer.valueOf(FocusReportNode3501Enum.FORM_TYPE_ID.toString());
				routineInterval = 2;
				break;
			}
			case 10: {
				subFormTypeId = Integer.valueOf(FocusReportNode3502Enum.FORM_TYPE_ID.toString());
				routineInterval = 0;
				break;
			}
		}
		
		//传入的间隔需要减1，因为无法确认传入的间隔是否减了1，因此该操作有调用方完成
		//传入的间隔需要减1是因为语句中简单的+1无法将节假日剔除
		if(CommonFunctions.isNotBlank(params, "routineInterval")) {
			try {
				routineInterval = Integer.valueOf(params.get("routineInterval").toString());
			} catch(NumberFormatException e) {}
		}
		
		routineDayStart = holidayInfoService.getWorkDateByAfterWorkDay(routineDayEnd, -routineInterval);
		
		params.put("subFormTypeId", subFormTypeId);
		params.put("routineDayStart", routineDayStart);
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
		
		total = reportTwoViolationPreMapper.findCount4StatusTrack(params);
		
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
		
		total = reportTwoViolationPreMapper.findCount4StatusTrack(params);
		
		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			reportList = reportTwoViolationPreMapper.findList4StatusTrack(params, rowBounds);
			
			formatDataOut(reportList, params);
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination reportPagination = new EUDGPagination(total, reportList);
		
		return reportPagination;
	}
	
	/**
	 * 保存两违额外信息
	 * @param params	额外参数
	 * 			reportId	上报id，如果没有，通过外部总入口进行调整，不要内部再次通过对外接口进行信息转换获取
	 */
	protected void saveReportExtraInfo(Map<String, Object> params) {
		Long reportId = null;
		
		if(CommonFunctions.isNotBlank(params, "reportId")) {
			reportId = Long.valueOf(params.get("reportId").toString());
		}
		
		if(reportId != null && reportId > 0) {
			saveAttrInfo(reportId, params);
			saveResmarkerInfo(reportId, params);
		}
	}

	/**
	 * 获取两违额外信息
	 * @param twoVioPreMap	两违信息
	 * @param params		额外参数
	 * 			reportId	上报id，如果没有，通过外部总入口进行调整，不要内部再次通过对外接口进行信息转换获取
	 */
	protected void findReportExtraInfo(Map<String, Object> twoVioPreMap, Map<String, Object> params) {
		Long reportId = null;
		
		if(CommonFunctions.isNotBlank(twoVioPreMap, "reportId")) {
			reportId = Long.valueOf(twoVioPreMap.get("reportId").toString());
		}
		
		if(reportId != null && reportId > 0) {
			findResmarkerInfo(reportId, twoVioPreMap, params);
		}
	}
	
	/**
	 * 保存两违附件信息
	 * @param reportId	上报id
	 * @param params	额外参数
	 * 			isSaveAttrInfo		是否保存附件信息，默认为false
	 * 			attachmentId		附件id，支持类型为Long[]、String
	 * 			outerAttachemntId	外部传入附件id，支持类型为String[]、String，会将传入的附件复制一份给当前记录
	 */
	private void saveAttrInfo(Long reportId, Map<String, Object> params) {
		boolean isSaveAttrInfo = false;
		
		if(CommonFunctions.isNotBlank(params, "isSaveAttrInfo")) {
			isSaveAttrInfo = Boolean.valueOf(params.get("isSaveAttrInfo").toString());
		}
		
		//保存附件信息
		if(isSaveAttrInfo) {
			Long[] attachmentIdArray = null;
			
			if(CommonFunctions.isNotBlank(params, "attachmentId")) {
				Object attachmentIdObj = params.get("attachmentId");
				
				if(attachmentIdObj instanceof Long[]) {
					attachmentIdArray = (Long[]) attachmentIdObj;
				} else if(attachmentIdObj instanceof String) {
					String[] attachmentIdStrArray = attachmentIdObj.toString().split(",");
					Long attachmentIdL = null;
					List<Long> attachmentIdList = new ArrayList<Long>();
					
					for(String attachmentIdStr : attachmentIdStrArray) {
						attachmentIdL = null;
						
						try {
							attachmentIdL = Long.valueOf(attachmentIdStr);
						} catch(NumberFormatException e) {}
						
						if(attachmentIdL != null && attachmentIdL > 0) {
							attachmentIdList.add(attachmentIdL);
						}
					}
					
					if(attachmentIdList.size() > 0) {
						attachmentIdArray = attachmentIdList.toArray(new Long[attachmentIdList.size()]);
					}
					
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "outerAttachmentId")) {
				Object outerAttachmentIdObj = params.get("outerAttachmentId");
				String outerAttachmentIdStr = null;
				List<Attachment> outerAttachmentList = null,
								 attachmentList = null;
				
				if(outerAttachmentIdObj instanceof String[]) {
					outerAttachmentIdStr = String.join(",", (String[]) outerAttachmentIdObj);
				} else if(outerAttachmentIdObj instanceof String) {
					outerAttachmentIdStr = params.get("outerAttachmentId").toString();
				}
				
				if(StringUtils.isNotBlank(outerAttachmentIdStr)) {
					outerAttachmentList = attachmentService.findByIds(outerAttachmentIdStr);
				}
				
				if(outerAttachmentList != null) {
					attachmentList = new ArrayList<Attachment>();
					
					for(Attachment attachment : outerAttachmentList) {
						if(attachment != null) {
							attachment.setAttachmentId(null);
							attachment.setAttachmentUUID(null);
							attachment.setBizId(reportId);
							attachment.setAttachmentType(capBizType4Attachment());
							
							attachmentList.add(attachment);
						}
					}
					
					if(attachmentList.size() > 0) {
						attachmentService.saveAttachment(attachmentList);
					}
				}
			}
			
			if(attachmentIdArray != null && attachmentIdArray.length > 0) {
				attachmentService.updateBizId(reportId, capBizType4Attachment(), attachmentIdArray);
			}
		}
	}
	
	/**
	 * 保存两违定位信息
	 * @param reportId	上报id
	 * @param params	额外信息
	 * 			isSaveRemarkInfo	是否保存定位信息，默认为false
	 * 			remark.x		经度
	 * 			remark.y		纬度
	 * 			remark.mapType	地图类型
	 */
	private void saveResmarkerInfo(Long reportId, Map<String, Object> params) {
		boolean isSaveResMarkerInfo = false;
		
		if(CommonFunctions.isNotBlank(params, "isSaveResMarkerInfo")) {
			isSaveResMarkerInfo = Boolean.valueOf(params.get("isSaveResMarkerInfo").toString());
		}
		
		if(isSaveResMarkerInfo) {
			String resMarkerLongitude = null, resMarkerLatitude = null, resMarkerMapType = null;
			
			if(CommonFunctions.isNotBlank(params, "resMarker.x")) {
				resMarkerLongitude = params.get("resMarker.x").toString();
			} else if(CommonFunctions.isNotBlank(params, "resMarker.longitude")) {
				resMarkerLongitude = params.get("resMarker.longitude").toString();
			}
			if(CommonFunctions.isNotBlank(params, "resMarker.y")) {
				resMarkerLatitude = params.get("resMarker.y").toString();
			} else if(CommonFunctions.isNotBlank(params, "resMarker.latitude")) {
				resMarkerLatitude = params.get("resMarker.latitude").toString();
			}
			if(CommonFunctions.isNotBlank(params, "resMarker.mapType")) {
				resMarkerMapType = params.get("resMarker.mapType").toString();
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
	}
	
	/**
	 * 获取两违地图定位信息
	 * @param reportId		上报id
	 * @param twoVioPreMap	两违信息
	 * @param params	额外参数
	 * 			resMarker.mapType	地图类型，默认为5			
	 */
	private void findResmarkerInfo(Long reportId, Map<String, Object> twoVioPreMap, Map<String, Object> params) {
		boolean isCapResMarkerInfo = false;
		
		if(CommonFunctions.isNotBlank(params, "isCapResMarkerInfo")) {
			isCapResMarkerInfo = Boolean.valueOf(params.get("isCapResMarkerInfo").toString());
		}
		
		if(isCapResMarkerInfo) {
			String resMarkerMapType = "5";//默认使用二维地图
			ResMarker resMarker = null;
			
			if(CommonFunctions.isNotBlank(params, "resMarker.mapType")) {
				resMarkerMapType = params.get("resMarker.mapType").toString();
			}
			
			resMarker = resGisMarkerService.findResMarkerByResId(capBizType4ResmarkerInfo(), reportId, resMarkerMapType);
			
			if(resMarker != null) {
				twoVioPreMap.put("resMarker", resMarker);
				twoVioPreMap.put("resMarker.x", resMarker.getX());
				twoVioPreMap.put("resMarker.y", resMarker.getY());
				twoVioPreMap.put("resMarker.mapType", resMarker.getMapType());
			}
		}
	}

	/**
	 * 格式化输入参数
	 * @param params
	 * @throws ZhsqEventException
	 */
	protected void formatParamIn(Map<String, Object> params) throws ZhsqEventException {
		super.formatParamIn(params);
		if(CommonFunctions.isNotBlank(params, "dataSourceArray")) {
			params.put("dataSourceArray", params.get("dataSourceArray").toString().split(","));
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
			List<BaseDataDict> constructionStatusDictList = null,
							   buildingUsageDictList = null,
							   reportStatusDictList = null,
							   identificationResultList = null;
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
				constructionStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(TwoVioPreDictEnum.CONSTRUCTION_STATUS.getDictCode(), userOrgCode);
				buildingUsageDictList = baseDictionaryService.getDataDictListOfSinglestage(TwoVioPreDictEnum.BUILDING_USAGE.getDictCode(), userOrgCode);
				identificationResultList = baseDictionaryService.getDataDictListOfSinglestage(TwoVioPreDictEnum.IDENTIFICATION_RESULT.getDictCode(), userOrgCode);
				
				if(listType == 1) {
					reportStatusDictList = new ArrayList<BaseDataDict>();
					//草稿不增添字典，因为只有草稿列表会用到
					BaseDataDict draftDict = new BaseDataDict();
					draftDict.setDictGeneralCode(TwoVioPreReportStatusEnum.DRAFT_STATUS.getReportStatus());
					draftDict.setDictName(TwoVioPreReportStatusEnum.DRAFT_STATUS.getReportStatusName());
					reportStatusDictList.add(draftDict);
				} else {
					//上报状态
					reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(TwoVioPreDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
				}
			}
			
			for(Map<String, Object> reportMap : reportList) {
				if(CommonFunctions.isNotBlank(reportMap, "feedbackTime")) {
					reportMap.put("feedbackTimeStr", DateUtils.formatDate((Date) reportMap.get("feedbackTime"), DateUtils.PATTERN_24TIME));
				}
				DataDictHelper.setDictValueForField(reportMap, "buildingUsage", "buildingUsageName", buildingUsageDictList);
				DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
				DataDictHelper.setDictValueForField(reportMap, "constructionStatus", "constructionStatusName", constructionStatusDictList);
				DataDictHelper.setDictValueForField(reportMap, "identificationResult", "identificationResultName", identificationResultList);
				if(5==listType){
					if(CommonFunctions.isNotBlank(reportMap,"verifyStatus")){
						if("1".equals(reportMap.get("verifyStatus"))){
							reportMap.put("verifyStatusName","属于两违");
						}else if("2".equals(reportMap.get("verifyStatus"))){
							reportMap.put("verifyStatusName","不属两违");
						}else if("3".equals(reportMap.get("verifyStatus"))){
							reportMap.put("verifyStatusName","不属本村");
						}else{
							reportMap.put("verifyStatusName","属于两违");
						}
					}
				}
				if(CommonFunctions.isNotBlank(reportMap,"constructionStatus")){
					if("99".equals(reportMap.get("constructionStatus")) && CommonFunctions.isNotBlank(reportMap,"conStatusDescribe")){
						reportMap.put("constructionStatusName",reportMap.get("conStatusDescribe"));
					}
				}
			}
		}
	}

	@Override
	public List<Map<String, Object>> findList4ReportFocus(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> reportList = null;
		int listType = 0;
		String METHOD_FORMAT_IN = "formatParamIn", METHOD_LIST = "findList";

		params = params == null ? new HashMap<String, Object>() : params;

		formatParamIn(params);

		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {}
		}

		if(capMethodMap().containsKey(listType)) {
			String formatMethodName = METHOD_FORMAT_IN + capMethodMap().get(listType);
			try {
				//getMethod () 获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法。
				this.getClass().getMethod(formatMethodName, new Class[] {Map.class}).invoke(this, params);
			} catch(NoSuchMethodException e) {
				//getDeclaredMethod() 获取的是类自身声明的所有方法，包含public、protected和private方法。
				this.getClass().getDeclaredMethod(formatMethodName, new Class[] {Map.class}).invoke(this, params);
			}

			String listMethodName = METHOD_LIST + capMethodMap().get(listType);
			try {
				reportList = (List<Map<String, Object>>) this.getClass().getMethod(listMethodName, new Class[] {Map.class}).invoke(this, params);
			} catch(NoSuchMethodException e) {
				reportList = (List<Map<String, Object>>) this.getClass().getDeclaredMethod(listMethodName, new Class[] {Map.class}).invoke(this, params);
			}
		}

		return reportList;
	}


	@Override
	public List<Map<String, Object>> capTaskAdvice(List<Map<String, Object>> reportList,Map<String,Object> params) {
		Long instanceId = null;
		List<Map<String, Object>> taskMapList = null;
		String  formTypeId = null,
				task1Advice = null,//报告内容（报告人发布内容）
				task3Advice = null,//第一副网格长核实
				task8Advice = null,//镇级处置情况（镇级分管领导处置报告内容）
				task6Advice = null,//网格处置情况（第一网格长处置报告内容）
				task10Advice = null,//市直部门查处
				task11Advice = null,//市直部门核验
				task7Advice = null;//市两违办监督检查结果（市两违办报告内容）
		if(null != reportList && reportList.size() > 0){
			for (Map<String,Object> reportFocus : reportList) {
				task1Advice = null;
				task3Advice = null;
				task8Advice = null;
				task6Advice = null;
				task10Advice = null;
				task11Advice = null;
				task7Advice = null;
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
										||StringUtils.isBlank(task8Advice) ||StringUtils.isBlank(task6Advice)||StringUtils.isBlank(task7Advice)
										||StringUtils.isBlank(task10Advice)||StringUtils.isBlank(task11Advice))
						){
							taskCode = String.valueOf(task.get("TASK_CODE"));
							//南安疫情防控信息流程
							if(FocusReportNode350Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeId)){
								//报告内容task1
//								if(FocusReportNode350Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(taskCode) && StringUtils.isBlank(task1Advice)){
//									task1Advice = String.valueOf(task.get("REMARKS"));
//									reportFocus.put("task1Advice",task1Advice);
//								}
								//第一副网格长核实task3
								if((FocusReportNode350Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(taskCode))
										&& StringUtils.isBlank(task3Advice)){
									task3Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task3Advice",task3Advice);
								}
								//网格处置情况（第一网格长处置报告内容）task6
								if((FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(taskCode))
										&& StringUtils.isBlank(task6Advice)){
									task6Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task6Advice",task6Advice);
								}
								//镇级处置情况（镇级分管领导处置报告内容）task8
								if(FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(taskCode) && StringUtils.isBlank(task8Advice)){
									task8Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task8Advice",task8Advice);
								}
								//市直部门查处情况（部门办结报告内容）task10
								if((FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName().equals(taskCode))
										&& StringUtils.isBlank(task10Advice)){
									task10Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task10Advice",task10Advice);
								}
								//市直部门核验情况（市直业务部门核验反馈报告内容）task11
								if((FocusReportNode350Enum.DEPARTMENT_CHECK_TASK_NODE_NAME.getTaskNodeName().equals(taskCode))
										&& StringUtils.isBlank(task11Advice)){
									task11Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task11Advice",task11Advice);
								}

								//市两违办监督检查结果（市两违办报告内容）task7
								if((FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName().equals(taskCode))
										&& StringUtils.isBlank(task7Advice)){
									task7Advice = String.valueOf(task.get("REMARKS"));
									reportFocus.put("task7Advice",task7Advice);
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
