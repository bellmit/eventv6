package cn.ffcs.zhsq.reportFocus.service.impl;

import cn.ffcs.elastic.util.CommonFunctions;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.INumberConfigureService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.ReportFocusExtendMapper;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.ReportFocusMapper;
import cn.ffcs.zhsq.reportFocus.IReportFocusExtendService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.data.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 南安重点关注上报信息操作接口实现
 * 相关表：T_EVENT_REPORT_FOCUS
 * @Description: 
 * @ClassName:   ReportFocusServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年9月14日 下午2:45:36
 */
@Service("reportFocusService")
public class ReportFocusServiceImpl extends
		ApplicationObjectSupport implements IReportFocusService {
	
	@Autowired
	private ReportFocusMapper reportFocusMapper;
	
	@Autowired
	private ReportFocusExtendMapper reportFocusExtendMapper;
	
	@Autowired
	private INumberConfigureService numberConfigureService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Override
	public Long saveReportFocus(Map<String, Object> reportFocus, UserInfo userInfo) throws Exception {
		Long reportId = null;
		Long userId = null, userOrgId = null;
		String reporterTel = null;
		
		if(reportFocus == null || reportFocus.isEmpty()) {
			throw new IllegalArgumentException("缺少需要处理的信息！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		
		userId = userInfo.getUserId();
		userOrgId = userInfo.getOrgId();
		reporterTel = userInfo.getVerifyMobile();
		
		if(CommonFunctions.isNotBlank(reportFocus, "reportId")) {
			try {
				reportId = Long.valueOf(reportFocus.get("reportId").toString());
			} catch(NumberFormatException e) {}
		}
		if(CommonFunctions.isBlank(reportFocus, "creator")) {
			reportFocus.put("creator", userId);
		}
		if(CommonFunctions.isBlank(reportFocus, "reporterOrgId")) {
			reportFocus.put("reporterOrgId", userOrgId);
		}
		if(CommonFunctions.isBlank(reportFocus, "updator")) {
			reportFocus.put("updator", userId);
		}
		if(CommonFunctions.isBlank(reportFocus, "reporterTel")) {
			reportFocus.put("reporterTel", reporterTel);
		}
		
		if(reportId != null && reportId > 0) {
			updateReportFocus(reportFocus, userInfo);
		} else {
			if(saveReportFocusInfo(reportFocus, userInfo)) {
				if(CommonFunctions.isNotBlank(reportFocus, "reportId")) {
					reportId = Long.valueOf(reportFocus.get("reportId").toString());
				}
			}
		}
		
		return reportId;
	}

	@Override
	public Map<String, Object> findReportFocusByUUIDSimple(String reportUUID, UserInfo userInfo, Map<String, Object> params) {
		Map<String, Object> reportFocusParam = new HashMap<String, Object>(), reportFocusMap = null;

		if(StringUtils.isNotBlank(reportUUID)) {
			reportFocusParam.put("reportUUID", reportUUID);
		} else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
			reportFocusParam.put("reportUUID", params.get("reportUUID"));
		} else if(CommonFunctions.isNotBlank(params, "reportId")) {
			reportFocusParam.put("reportId", params.get("reportId"));
		}
		
		if(reportFocusParam.isEmpty()) {
			throw new IllegalArgumentException("缺失reportUUID/reportId，不可查询信息！");
		} else {
			reportFocusMap = reportFocusMapper.findById(reportFocusParam);
		}
		
		return reportFocusMap;
	}
	
	@Override
	public Map<String, Object> findReportFocusByUUID(String reportUUID, UserInfo userInfo, Map<String, Object> params) {
		IReportFocusExtendService reportFocusExtendService = capReportExtendService(params);

		return reportFocusExtendService.findReportExtendInfoByReportUUID(reportUUID, userInfo, params);
	}
	
	@Override
	public int delReportFocusByUUID(String reportUUID, UserInfo userInfo, Map<String, Object> params) {
		Map<String, Object> delParams = new HashMap<String, Object>();
		Long reportId = null, delUserId = null;
		
		if(StringUtils.isBlank(reportUUID) && CommonFunctions.isNotBlank(params, "reportUUID")) {
			reportUUID = params.get("reportUUID").toString();
		}
		if(CommonFunctions.isNotBlank(params, "delUserId")) {
			try {
				delUserId = Long.valueOf(params.get("delUserId").toString());
			} catch(NumberFormatException e) {}
		}
		if(CommonFunctions.isNotBlank(params, "reportId")) {
			try {
				reportId = Long.valueOf(params.get("reportId").toString());
			} catch(NumberFormatException e) {}
		}
		
		if(StringUtils.isNotBlank(reportUUID)) {
			delParams.put("reportUUID", reportUUID);
		}
		if(delUserId != null && delUserId > 0) {
			delParams.put("delUserId", delUserId);
		}
		if(reportId != null && reportId > 0) {
			delParams.put("reportId", reportId);
		}
		
		if(CommonFunctions.isBlank(delParams, "reportUUID") && CommonFunctions.isBlank(delParams, "reportId")) {
			throw new IllegalArgumentException("缺少有效的上报id【reportUUID/reportId】！");
		}
		
		return reportFocusMapper.delete(delParams);
	}
	
	@Override
	public List<Map<String, Integer>> findCount4IntegrationTodo(Map<String, Object> params) throws Exception {
		List<Map<String, Integer>> resultMapList = null;
		
		if(CommonFunctions.isBlank(params, "curUserId") && CommonFunctions.isNotBlank(params, "operateUserId")) {
			params.put("curUserId", params.get("operateUserId"));
		}
		
		if(CommonFunctions.isBlank(params, "curOrgId") && CommonFunctions.isNotBlank(params, "operateOrgId")) {
			params.put("curOrgId", params.get("operateOrgId"));
		}
		
		if(CommonFunctions.isBlank(params, "curUserId") || CommonFunctions.isBlank(params, "curOrgId")) {
			throw new ZhsqEventException("缺少办理用户信息！");
		}
		
		resultMapList = reportFocusExtendMapper.findCount4IntegrationTodo(params);
		
		return resultMapList;
	}
	
	@Override
	public int findCount4IntegrationMsgReading(Map<String, Object> params) throws Exception {
		formatParamIn4MsgReading(params);
		
		return reportFocusExtendMapper.findCount4IntegrationMsgReading(params);
	}
	
	@Override
	public EUDGPagination findPaginationIntegrationMsgReading(int pageNo, int pageSize, Map<String, Object> params) throws Exception {
		int total = 0;
		List<Map<String, Object>> reportList = null;
		
		formatParamIn4MsgReading(params);
		
		total = reportFocusExtendMapper.findCount4IntegrationMsgReading(params);
		
		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			reportList = reportFocusExtendMapper.findList4IntegrationMsgReading(params, rowBounds);
			
			formatDataOut4MsgReading(reportList, params);
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination reportPagination = new EUDGPagination(total, reportList);
		
		return reportPagination;
	}
	
	@Override
	public int findCount4ReportFocus(Map<String, Object> params) throws Exception {
		IReportFocusExtendService reportFocusExtendService = capReportExtendService(params);
		
		return reportFocusExtendService.findCount4ReportExtend(params);
	}

	@Override
	public EUDGPagination findPagination4ReportFocus(int pageNo, int pageSize, Map<String, Object> params)
			throws Exception {
		IReportFocusExtendService reportFocusExtendService = capReportExtendService(params);
		
		return reportFocusExtendService.findPagination4ReportExtend(pageNo, pageSize, params);
	}

	@Override
	public List<Map<String,Object>> findList4ReportFocus(Map<String, Object> params)
			throws Exception {
		IReportFocusExtendService reportFocusExtendService = capReportExtendService(params);

		return reportFocusExtendService.findList4ReportFocus(params);
	}
	
	@Override
	public String capSmsContent(Map<String, Object> params, UserInfo userInfo) throws Exception {
		String smsTemplateName = null, userOrgCode = null, smsContent = null;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isBlank(params, "smsTemplateName")) {
			throw new IllegalArgumentException("缺少短信模板名称【smsTemplateName】！");
		}
		
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		
		userOrgCode = userInfo.getOrgCode();
		smsTemplateName = params.get("smsTemplateName").toString();
		
		smsContent = funConfigurationService.turnCodeToValue(ConstantValue.SMS_NOTE_TYPE, smsTemplateName, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);
		
		Long reportId = null;
		Map<String, Object> reportFocusMap = new HashMap<String, Object>(),
							reportResultMap = null;
		IReportFocusExtendService reportFocusExtendService = capReportExtendService(params);
		
		if(CommonFunctions.isNotBlank(params, "reportId")) {
			reportId = Long.valueOf(params.get("reportId").toString());
		} else if(CommonFunctions.isNotBlank(params, "formId")) {
			reportId = Long.valueOf(params.get("formId").toString());
		} else if(CommonFunctions.isNotBlank(params, "instanceId")) {
			Long instanceId = Long.valueOf(params.get("instanceId").toString());
			
			if(instanceId != null && instanceId > 0) {
				ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
				
				if(proInstance != null) {
					reportId = proInstance.getFormId();
				}
			}
		}
		
		reportFocusMap.put("reportId", reportId);
		
		reportResultMap = findReportFocusByUUIDSimple(null, userInfo, reportFocusMap);
		
		params.putAll(reportResultMap);
		
		//如果需要进行模板内容动态替换，则分解至各模块中个性处理
		smsContent = reportFocusExtendService.capSmsContent(smsContent, params, userInfo);
		
		return smsContent;
	}
	
	@Override
	public boolean recordPoint(int recordType, UserInfo userInfo, Map<String, Object> params) throws Exception {
		IReportFocusExtendService reportFocusExtendService = capReportExtendService(params);
		
		return reportFocusExtendService.recordPoint(recordType, userInfo, params);
	}
	
	/**
	 * 保存重点关注上报信息
	 * @param reportFocus	重点关注上报信息
	 * @param userInfo		操作用户信息
	 * @return
	 * @throws ZhsqEventException 
	 */
	private boolean saveReportFocusInfo(Map<String, Object> reportFocus, UserInfo userInfo) throws Exception {
		boolean flag = false, isSaveReportFocusExtend = true;
		IReportFocusExtendService reportFocusExtendService = capReportExtendService(reportFocus);
		String regionCode = null, reportCode = null;
		
		reportFocusExtendService.formatParamIn4Report(reportFocus);
		formatParamIn(reportFocus);
		
		if(CommonFunctions.isBlank(reportFocus, "reporterName") && userInfo != null) {
			reportFocus.put("reporterName", userInfo.getPartyName());
		}
		
		if(CommonFunctions.isNotBlank(reportFocus, "isSaveReportFocusExtend")) {
			isSaveReportFocusExtend = Boolean.valueOf(reportFocus.get("isSaveReportFocusExtend").toString());
		}
		
		if(CommonFunctions.isNotBlank(reportFocus, "regionCode")) {
			regionCode = reportFocus.get("regionCode").toString();
		}
		
		if(CommonFunctions.isNotBlank(reportFocus, "reportCode")) {
			reportCode = reportFocus.get("reportCode").toString();
		} else {
			reportCode = numberConfigureService.getNumber(regionCode, reportFocusExtendService.capNumberCfgBizCode());
		}
		
		reportFocus.put("reportCode", reportCode);
		
		flag = reportFocusMapper.insert(reportFocus) > 0;
		
		if(flag && isSaveReportFocusExtend) {
			Map<String, Object> reportFocusMap = this.findReportFocusByUUIDSimple(null, userInfo, reportFocus);
			
			if(CommonFunctions.isNotBlank(reportFocusMap, "reportUUID")) {
				String reportUUID = reportFocusMap.get("reportUUID").toString();
				
				reportFocus.put("reportUUID", reportUUID);
				String extendUUID = reportFocusExtendService.saveReportExtendInfo(reportFocus, userInfo);
				flag = StringUtils.isNotBlank(extendUUID);
			}
		}
		
		return flag;
	}
	
	/**
	 * 更新重点关注上报信息
	 * @param reportFocus	重点关注上报信息
	 * @param userInfo		操作用户信息
	 * @return
	 * @throws ZhsqEventException 
	 */
	private boolean updateReportFocus(Map<String, Object> reportFocus, UserInfo userInfo) throws Exception {
		boolean flag = false, isSaveReportFocusExtend = true;
		IReportFocusExtendService reportFocusExtendService = capReportExtendService(reportFocus);
		
		reportFocusExtendService.formatParamIn4Report(reportFocus);
		formatParamIn(reportFocus);
		
		flag = reportFocusMapper.update(reportFocus) > 0;
		
		if(CommonFunctions.isNotBlank(reportFocus, "isSaveReportFocusExtend")) {
			isSaveReportFocusExtend = Boolean.valueOf(reportFocus.get("isSaveReportFocusExtend").toString());
		}
		
		if(flag && isSaveReportFocusExtend) {
			String reportUUID = null;
			
			if(CommonFunctions.isBlank(reportFocus, "reportUUID")) {
				Map<String, Object> reportFocusMap = this.findReportFocusByUUIDSimple(null, userInfo, reportFocus);
				if(CommonFunctions.isNotBlank(reportFocusMap, "reportUUID")) {
					reportUUID = reportFocusMap.get("reportUUID").toString();
				}
			} else {
				reportUUID = reportFocus.get("reportUUID").toString();
			}
			
			if(StringUtils.isNotBlank(reportUUID)) {
				String extendUUID = null;
				
				reportFocus.put("reportUUID", reportUUID);
				
				extendUUID = reportFocusExtendService.saveReportExtendInfo(reportFocus, userInfo);
				
				flag = StringUtils.isNotBlank(extendUUID);
			}
		}
		
		return flag;
	}
	
	/**
	 * 格式化输入参数
	 * @param reportFocus
	 * @throws Exception 
	 */
	private void formatParamIn(Map<String, Object> reportFocus) throws Exception {
		if(CommonFunctions.isNotBlank(reportFocus, "reportTime")) {
			Object reportTimeObj = reportFocus.get("reportTime");
			Date reportTime = null;
			
			if(reportTimeObj instanceof String) {
				try {
					reportTime = DateUtils.convertStringToDate(reportTimeObj.toString(), DateUtils.PATTERN_24TIME);
				} catch (ParseException e) {
					throw new ZhsqEventException("报告时间【" + reportTimeObj.toString() + "】不满足如下时间格式：" + DateUtils.PATTERN_24TIME + "！");
				}
			} else if(reportTimeObj instanceof Date) {
				reportTime = (Date) reportTimeObj;
			}
			
			reportFocus.put("reportTime", reportTime);
		} else if(CommonFunctions.isBlank(reportFocus, "reportTime") && CommonFunctions.isNotBlank(reportFocus, "reportTimeStr")) {
			Date reportTime = null;
			String reportTimeStr = reportFocus.get("reportTimeStr").toString();
			
			try {
				reportTime = DateUtils.convertStringToDate(reportTimeStr, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				throw new ZhsqEventException("报告时间【" + reportTimeStr + "】不满足如下时间格式：" + DateUtils.PATTERN_24TIME + "！");
			}
			
			if(reportTime != null) {
				reportFocus.put("reportTime", reportTime);
			}
		}

		if(CommonFunctions.isNotBlank(reportFocus, "meetingTime")) {
			Object meetingTimeObj = reportFocus.get("meetingTime");
			Date meetingTime = null;

			if(meetingTimeObj instanceof String) {
				try {
					meetingTime = DateUtils.convertStringToDate(meetingTimeObj.toString(), DateUtils.PATTERN_24TIME);
				} catch (ParseException e) {
					throw new ZhsqEventException("会议时间【" + meetingTimeObj.toString() + "】不满足如下时间格式：" + DateUtils.PATTERN_24TIME + "！");
				}
			} else if(meetingTimeObj instanceof Date) {
				meetingTime = (Date) meetingTimeObj;
			}

			reportFocus.put("meetingTime", meetingTime);
		} else if(CommonFunctions.isBlank(reportFocus, "meetingTime") && CommonFunctions.isNotBlank(reportFocus, "meetingTimeStr")) {
			Date meetingTime = null;
			String meetingTimeStr = reportFocus.get("meetingTimeStr").toString();

			try {
				meetingTime = DateUtils.convertStringToDate(meetingTimeStr, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				throw new ZhsqEventException("会议时间【" + meetingTimeStr + "】不满足如下时间格式：" + DateUtils.PATTERN_24TIME + "！");
			}

			if(meetingTime != null) {
				reportFocus.put("meetingTime", meetingTime);
			}
		}
		
		if(CommonFunctions.isNotBlank(reportFocus, "closeTime")) {
			Object closeTimeObj = reportFocus.get("closeTime");
			Date closeTime = null;
			
			if(closeTimeObj instanceof String) {
				try {
					closeTime = DateUtils.convertStringToDate(closeTimeObj.toString(), DateUtils.PATTERN_24TIME);
				} catch (ParseException e) {
					throw new ZhsqEventException("办结时间【" + closeTimeObj.toString() + "】不满足如下时间格式：" + DateUtils.PATTERN_24TIME + "！");
				}
			} else if(closeTimeObj instanceof Date) {
				closeTime = (Date) closeTimeObj;
			}
			
			reportFocus.put("closeTime", closeTime);
		} else if(CommonFunctions.isBlank(reportFocus, "closeTime") && CommonFunctions.isNotBlank(reportFocus, "closeTimeStr")) {
			Date closeTime = null;
			String closeTimeStr = reportFocus.get("closeTimeStr").toString();
			
			try {
				closeTime = DateUtils.convertStringToDate(closeTimeStr, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				throw new ZhsqEventException("办结时间【" + closeTimeStr + "】不满足如下时间格式：" + DateUtils.PATTERN_24TIME + "！");
			}
			
			if(closeTime != null) {
				reportFocus.put("closeTime", closeTime);
			}
		}
		if(CommonFunctions.isNotBlank(reportFocus, "closeUserId")) {//强制转换为String，否则会导致ORA-00932: 数据类型不一致: 应为 NUMBER, 但却获得 CHAR
			reportFocus.put("closeUserId", reportFocus.get("closeUserId").toString());
		}
		if(CommonFunctions.isNotBlank(reportFocus, "closeOrgId")) {
			reportFocus.put("closeOrgId", reportFocus.get("closeOrgId").toString());
		}
	}
	
	/**
	 * 我的阅办格式化输入参数
	 * @param params
	 * @throws ZhsqEventException
	 */
	private void formatParamIn4MsgReading(Map<String, Object> params) throws ZhsqEventException {
		if(CommonFunctions.isNotBlank(params, "eRegionCode")) {
			byte[] decodedData = Base64.decode(params.get("eRegionCode").toString());
			params.put("regionCode", new String(decodedData));
		}else if (CommonFunctions.isNotBlank(params, "startRegionCode")) {
			params.put("regionCode", params.get("startRegionCode"));
		}
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			String reportType = params.get("reportType").toString();
			
			if(reportType.contains(",")) {
				params.put("reportTypeArray", reportType.split(","));
				params.remove("reportType");
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "msgModuleCode")) {
			String msgModuleCode = params.get("msgModuleCode").toString();
			
			if(msgModuleCode.contains(",")) {
				params.put("msgModuleCodeArray", msgModuleCode.split(","));
				params.remove("msgModuleCode");
			}
		}
		
		if(CommonFunctions.isBlank(params, "msgReceiveUserId")
				&& CommonFunctions.isNotBlank(params, "operateUserId")) {
			params.put("msgReceiveUserId", params.get("operateUserId"));
		}
		if(CommonFunctions.isBlank(params, "msgReceiveOrgId")
				&& CommonFunctions.isNotBlank(params, "operateOrgId")) {
			params.put("msgReceiveOrgId", params.get("operateOrgId"));
		}
		
		if(CommonFunctions.isNotBlank(params, "msgSendDayStart")) {
			Object msgSendDayStartObj = params.get("msgSendDayStart");
			Date msgSendDayStart = null;
			
			if(msgSendDayStartObj instanceof String) {
				try {
					msgSendDayStart = DateUtils.convertStringToDate(msgSendDayStartObj.toString(), DateUtils.PATTERN_DATE);
				} catch (ParseException e) {
					throw new ZhsqEventException("报告开始时间[msgSendDayStart]，不是如下时间格式：" + DateUtils.PATTERN_DATE);
				}
			} else if(msgSendDayStartObj instanceof Date) {
				msgSendDayStart = (Date) msgSendDayStartObj;
			}
			
			params.put("msgSendDayStart", msgSendDayStart);
		} else {
			params.remove("msgSendDayStart");
		}
		
		if(CommonFunctions.isNotBlank(params, "msgSendDayEnd")) {
			Object msgSendDayEndObj = params.get("msgSendDayEnd");
			Date msgSendDayEnd = null;
			
			if(msgSendDayEndObj instanceof String) {
				try {
					msgSendDayEnd = DateUtils.convertStringToDate(msgSendDayEndObj.toString(), DateUtils.PATTERN_DATE);
				} catch (ParseException e) {
					throw new ZhsqEventException("报告结束时间[msgSendDayEnd]，不是如下时间格式：" + DateUtils.PATTERN_DATE);
				}
			} else if(msgSendDayEndObj instanceof Date) {
				msgSendDayEnd = (Date) msgSendDayEndObj;
			}
			
			params.put("msgSendDayEnd", msgSendDayEnd);
		} else {
			params.remove("msgSendDayEnd");
		}
	}
	
	/**
	 * 我的阅办数据格式化输出
	 * @param reportList
	 * @param params
	 */
	private void formatDataOut4MsgReading(List<Map<String, Object>> reportList, Map<String, Object> params) {
		if(reportList != null && reportList.size() > 0) {
			Map<String, String> msgReceiveStatusMap = new HashMap<String, String>(),
								reportTypeMap = new HashMap<String, String>();
			msgReceiveStatusMap.put("0", "未读");
			msgReceiveStatusMap.put("1", "已读");
			
			for(ReportTypeEnum reportTypeEnum : ReportTypeEnum.values()) {
				reportTypeMap.put(reportTypeEnum.getReportTypeIndex(), reportTypeEnum.getReportTypeName());
			}
			
			for(Map<String, Object> reportMap : reportList) {
				if(CommonFunctions.isNotBlank(reportMap, "msgSendTime")) {
					reportMap.put("msgSendTimeStr", DateUtils.formatDate((Date) reportMap.get("msgSendTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(reportMap, "msgReceiveStatus")) {
					reportMap.put("msgReceiveStatusName", msgReceiveStatusMap.get(reportMap.get("msgReceiveStatus")));
				}
				if(CommonFunctions.isNotBlank(reportMap, "reportType")) {
					reportMap.put("reportTypeName", reportTypeMap.get(reportMap.get("reportType")));
				}
			}
		}
	}
	
	/**
	 * 获取上报扩展信息实现接口
	 * @param reportFocus
	 * @return
	 */
	private IReportFocusExtendService capReportExtendService(Map<String, Object> reportFocus) {
		String reportType = null;
		IReportFocusExtendService reportFocusExtendService = null;
		ReportServiceAgent<IReportFocusExtendService> reportServiceAgent = null;
		
		if(CommonFunctions.isNotBlank(reportFocus, "reportType")) {
			reportType = reportFocus.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		
		reportServiceAgent = new ReportServiceAgent<IReportFocusExtendService>(reportType, ReportServiceAgent.serviceTypeEnum.reportExtendService.getServiceTypeIndex());
		
		try {
			reportFocusExtendService = reportServiceAgent.capService();
		} catch (Exception e) {
			throw new IllegalArgumentException("上报类型【" + reportType + "】缺少业务扩展信息接口！");
		}
		
		return reportFocusExtendService;
	}
}
