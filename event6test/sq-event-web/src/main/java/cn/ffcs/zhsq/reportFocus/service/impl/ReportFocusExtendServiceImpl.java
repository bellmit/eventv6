package cn.ffcs.zhsq.reportFocus.service.impl;

import cn.ffcs.oa.entity.interalrule.IdRecord;
import cn.ffcs.oa.service.interalrule.IntegralRuleService;
import cn.ffcs.shequ.zzgl.service.grid.IGridAdminService;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.twoViolationPre.ReportTwoViolationPreMapper;
import cn.ffcs.zhsq.reportFocus.IReportFocusExtendService;
import cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl.MeetingReportStatusEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.TwoVioPreDictEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.TwoVioPreReportStatusEnum;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.JsonUtils;
import cn.ffcs.zhsq.utils.data.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ReportFocusExtendServiceImpl implements IReportFocusExtendService {
	@Autowired
	private IWorkflow4BaseService workflow4BaseService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IGridAdminService gridAdminService;
	
	@Autowired
	private IntegralRuleService integralRuleService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * ????????????mapper
	 * @return
	 */
	protected abstract ReportTwoViolationPreMapper capReportExtendMapper();
	
	/**
	 * ????????????????????????
	 * @return
	 */
	protected abstract String capBizType4Attachment();
	
	/**
	 * ????????????????????????
	 * @return
	 */
	protected abstract String capBizType4ResmarkerInfo();
	
	/**
	 * ?????????????????????
	 * @param params
	 */
	public void formatParamIn4Draft(Map<String, Object> params) {
		String INIT_VALID_STATUS = "2";
		
		if(CommonFunctions.isBlank(params, "createUserId") && CommonFunctions.isNotBlank(params, "operateUserId")) {
			params.put("createUserId", params.get("operateUserId"));
		}
		
		params.put("reportValid", INIT_VALID_STATUS);
		params.put("reportStatus", TwoVioPreReportStatusEnum.DRAFT_STATUS.getReportStatus());
	}
	
	/**
	 * ????????????????????????
	 * @param params
	 * @return
	 */
	public int findCount4Draft(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4Draft(params);
		
		return total;
	}
	
	/**
	 * ??????????????????????????????
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findPagination4Draft(int pageNo, int pageSize, Map<String, Object> params) {
		int total = 0;
		List<Map<String, Object>> reportList = null;
		
		total = capReportExtendMapper().findCount4Draft(params);
		
		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			reportList = capReportExtendMapper().findList4Draft(params, rowBounds);
			
			formatDataOut(reportList, params);
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination reportPagination = new EUDGPagination(total, reportList);
		
		return reportPagination;
	}
	
	/**
	 * ???????????????????????????
	 * @param params
	 */
	public void formatParamIn4Todo(Map<String, Object> params) {
		if(CommonFunctions.isBlank(params, "curUserId") && CommonFunctions.isNotBlank(params, "operateUserId")) {
			params.put("curUserId", params.get("operateUserId"));
		}
		
		if(CommonFunctions.isBlank(params, "curOrgId") && CommonFunctions.isNotBlank(params, "operateOrgId")) {
			params.put("curOrgId", params.get("operateOrgId"));
		}
	}
	
	/**
	 * ????????????????????????
	 * @param params
	 * @return
	 */
	public int findCount4Todo(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4Todo(params);
		
		return total;
	}
	
	/**
	 * ??????????????????????????????
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findPagination4Todo(int pageNo, int pageSize, Map<String, Object> params) {
		int total = 0;
		List<Map<String, Object>> reportList = null;
		
		total = capReportExtendMapper().findCount4Todo(params);
		
		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			reportList = capReportExtendMapper().findList4Todo(params, rowBounds);
			
			formatDataOut(reportList, params);
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination reportPagination = new EUDGPagination(total, reportList);
		
		return reportPagination;
	}
	
	/**
	 * ???????????????????????????
	 * @param params
	 */
	public void formatParamIn4Handled(Map<String, Object> params) {
		if(CommonFunctions.isBlank(params, "handledUserId") && CommonFunctions.isNotBlank(params, "operateUserId")) {
			params.put("handledUserId", params.get("operateUserId"));
		}
		
		if(CommonFunctions.isBlank(params, "handledOrgId") && CommonFunctions.isNotBlank(params, "operateOrgId")) {
			params.put("handledOrgId", params.get("operateOrgId"));
		}
	}
	
	/**
	 * ????????????????????????
	 * @param params
	 * @return
	 */
	public int findCount4Handled(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4Handled(params);
		
		return total;
	}
	
	/**
	 * ??????????????????????????????
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findPagination4Handled(int pageNo, int pageSize, Map<String, Object> params) {
		int total = 0;
		List<Map<String, Object>> reportList = null;
		
		total = capReportExtendMapper().findCount4Handled(params);
		
		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			reportList = capReportExtendMapper().findList4Handled(params, rowBounds);
			
			formatDataOut(reportList, params);
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination reportPagination = new EUDGPagination(total, reportList);
		
		return reportPagination;
	}
	
	/**
	 * ?????????????????????????????????
	 * @param params
	 */
	public void formatParamIn4Initiator(Map<String, Object> params) {
		if(CommonFunctions.isBlank(params, "initiatorId") && CommonFunctions.isNotBlank(params, "operateUserId")) {
			params.put("initiatorId", params.get("operateUserId"));
		}
		
		if(CommonFunctions.isBlank(params, "initiatorOrgId") && CommonFunctions.isNotBlank(params, "operateOrgId")) {
			params.put("initiatorOrgId", params.get("operateOrgId"));
		}
	}
	
	/**
	 * ??????????????????????????????
	 * @param params
	 * @return
	 */
	public int findCount4Initiator(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4Initiator(params);
		
		return total;
	}
	
	/**
	 * ????????????????????????????????????
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findPagination4Initiator(int pageNo, int pageSize, Map<String, Object> params) {
		int total = 0;
		List<Map<String, Object>> reportList = null;
		
		total = capReportExtendMapper().findCount4Initiator(params);
		
		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			reportList = capReportExtendMapper().findList4Initiator(params, rowBounds);
			
			formatDataOut(reportList, params);
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination reportPagination = new EUDGPagination(total, reportList);
		
		return reportPagination;
	}
	
	/**
	 * ??????????????????(??????)???????????????
	 * @param params
	 */
	public void formatParamIn4JurisdictionSimplify(Map<String, Object> params) {
		formatParamIn4Jurisdiction(params);
	}
	
	/**
	 * ????????????????????????(??????)??????
	 * @param params
	 * @return
	 */
	public int findCount4JurisdictionSimplify(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4JurisdictionSimplify(params);
		
		return total;
	}
	
	/**
	 * ??????????????????????????????(??????)??????
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findPagination4JurisdictionSimplify(int pageNo, int pageSize, Map<String, Object> params) {
		int total = 0;
		List<Map<String, Object>> reportList = null;
		
		total = capReportExtendMapper().findCount4JurisdictionSimplify(params);
		
		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			reportList = capReportExtendMapper().findList4JurisdictionSimplify(params, rowBounds);
			
			formatDataOut(reportList, params);
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination reportPagination = new EUDGPagination(total, reportList);
		
		return reportPagination;
	}
	
	/**
	 * ?????????????????????????????????
	 * @param params
	 */
	public void formatParamIn4Jurisdiction(Map<String, Object> params) {
		if(CommonFunctions.isBlank(params, "regionCode") && CommonFunctions.isNotBlank(params, "startRegionCode")) {
			params.put("regionCode", params.get("startRegionCode"));
		}
	}
	
	/**
	 * ??????????????????????????????
	 * @param params
	 * @return
	 */
	public int findCount4Jurisdiction(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4Jurisdiction(params);
		
		return total;
	}
	
	/**
	 * ????????????????????????????????????
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findPagination4Jurisdiction(int pageNo, int pageSize, Map<String, Object> params) {
		int total = 0;
		List<Map<String, Object>> reportList = null;
		
		total = capReportExtendMapper().findCount4Jurisdiction(params);
		
		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			reportList = capReportExtendMapper().findList4Jurisdiction(params, rowBounds);
			
			formatDataOut(reportList, params);
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination reportPagination = new EUDGPagination(total, reportList);
		
		return reportPagination;
	}
	
	/**
	 * ??????????????????(???????????????)???????????????
	 * @param params
	 */
	public void formatParamIn4JurisdictionWithMarker(Map<String, Object> params) {
		formatParamIn4Jurisdiction(params);
	}

	/**
	 * ??????????????????(???????????????)???????????????
	 * @param params
	 */
	public void formatParamIn4JurisdictionWithRegionPath(Map<String, Object> params) {
		formatParamIn4Jurisdiction(params);
	}
	
	/**
	 * ????????????????????????(???????????????)??????
	 * @param params
	 * @return
	 */
	public int findCount4JurisdictionWithMarker(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4JurisdictionWithMarker(params);
		
		return total;
	}
	
	/**
	 * ??????????????????????????????(???????????????)??????
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findPagination4JurisdictionWithMarker(int pageNo, int pageSize, Map<String, Object> params) {
		int total = 0;
		List<Map<String, Object>> reportList = null;
		
		total = capReportExtendMapper().findCount4JurisdictionWithMarker(params);
		
		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			reportList = capReportExtendMapper().findList4JurisdictionWithMarker(params, rowBounds);
			
			formatDataOut(reportList, params);
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination reportPagination = new EUDGPagination(total, reportList);
		
		return reportPagination;
	}

	/**
	 * ????????????????????????(?????????????????????)??????
	 * @param params
	 * @return
	 */
	public int findCount4JurisdictionWithRegionPath(Map<String, Object> params) {
		int total = 0;

		total = capReportExtendMapper().findCount4JurisdictionWithRegionPath(params);

		return total;
	}

	/**
	 * ??????????????????????????????(?????????????????????)??????
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findPagination4JurisdictionWithRegionPath(int pageNo, int pageSize, Map<String, Object> params) {
		int total = 0;
		List<Map<String, Object>> reportList = null;

		total = capReportExtendMapper().findCount4JurisdictionWithRegionPath(params);

		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);

			reportList = capReportExtendMapper().findList4JurisdictionWithRegionPath(params, rowBounds);

			formatDataOut(reportList, params);
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}

		EUDGPagination reportPagination = new EUDGPagination(total, reportList);

		return reportPagination;
	}

	/**
	 * ???????????????????????????????????????
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> findList4Jurisdiction(Map<String, Object> params) {
		int total = 0;
		List<Map<String, Object>> reportList = null;

		total = capReportExtendMapper().findCount4Jurisdiction(params);

		if(total > 0) {
			reportList = capReportExtendMapper().findList4JurisdictionNoPage(params);

			formatDataOut(reportList, params);

			if(CommonFunctions.isNotBlank(params,"isCapTaskAdvice") && Boolean.valueOf(String.valueOf(params.get("isCapTaskAdvice")))){
				capTaskAdvice(reportList,params);
			}
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}

		return reportList;
	}

	/**
	 * ?????????????????????????????????????????????????????????????????????????????????????????????
	 * */
	public List<Map<String, Object>> capTaskAdvice(List<Map<String, Object>> reportList,Map<String, Object> params) {
		return reportList;
	}
	/**
	 * ??????????????????????????????
	 * */
	public void capExtraInfoForExport(Map<String,Object> reportFocus,Map<String, Object> params){

		String regionCode = null;
		if(CommonFunctions.isNotBlank(reportFocus,"regionCode")){
			regionCode = String.valueOf(reportFocus.get("regionCode"));
		}
		if(StringUtils.isNotBlank(regionCode)){
			OrgEntityInfoBO orgEntityInfoBO = orgEntityInfoService.selectOrgEntityInfoByOrgCode(regionCode),
							communityEntityBo = null,
							streetEntityBo = null;
			String orgLevel = null;
			Long parentOrgId = null;

			if(null != orgEntityInfoBO){
				orgLevel = orgEntityInfoBO.getChiefLevel();
				parentOrgId = orgEntityInfoBO.getParentOrgId();

				//??????????????? ??????
				if(ConstantValue.GRID_ORG_LEVEL.equals(orgLevel)){
					reportFocus.put("gridName",orgEntityInfoBO.getOrgName());

					//???????????????
					communityEntityBo = orgEntityInfoService.findByOrgId(parentOrgId);
				}else if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgLevel)){
					//??????????????? ????????????
					reportFocus.put("communityName",orgEntityInfoBO.getOrgName());

					streetEntityBo = orgEntityInfoService.findByOrgId(parentOrgId);
				}else if(ConstantValue.STREET_ORG_LEVEL.equals(orgLevel)){
					//??????????????? ???????????????
					reportFocus.put("streetName",orgEntityInfoBO.getOrgName());
				}

				if(null != communityEntityBo && CommonFunctions.isBlank(reportFocus,"communityName")){
					reportFocus.put("communityName",communityEntityBo.getOrgName());

					streetEntityBo = orgEntityInfoService.findByOrgId(communityEntityBo.getParentOrgId());
				}
				if(null != streetEntityBo && CommonFunctions.isBlank(reportFocus,"streetName")){
					reportFocus.put("streetName",streetEntityBo.getOrgName());
				}
			}
		}
	}

	/**
     * ?????????????????????????????????
     * @param params
     */
    public void formatParamIn4Archived(Map<String, Object> params) {
        formatParamIn4Jurisdiction(params);

        params.put("reportStatus", MeetingReportStatusEnum.END_STATUS.getReportStatus());
    }

    /**
     * ??????????????????????????????
     * @param params
     * @return
     */
    public int findCount4Archived(Map<String, Object> params) {
    	int total = 0;

        total = capReportExtendMapper().findCount4Archived(params);

        return total;
    }

    /**
     * ????????????????????????????????????
     * @param pageNo
     * @param pageSize
     * @param params
     * @return
     */
    public EUDGPagination findPagination4Archived(int pageNo, int pageSize, Map<String, Object> params) {
    	int total = 0;
        List<Map<String, Object>> reportList = null;

        total = capReportExtendMapper().findCount4Archived(params);

        if(total > 0) {
            pageNo = pageNo < 1 ? 1 : pageNo;
            pageSize = pageSize < 1 ? 10 : pageSize;
            RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);

            reportList = capReportExtendMapper().findList4Archived(params, rowBounds);

            formatDataOut(reportList, params);
        } else {
            reportList = new ArrayList<Map<String, Object>>();
        }

        EUDGPagination reportPagination = new EUDGPagination(total, reportList);

        return reportPagination;
    }
    
    /**
	 * ?????????????????????????????????
	 * @param params
	 * @throws ZhsqEventException 
	 */
	public void formatParamIn4MsgReading(Map<String, Object> params) throws ZhsqEventException {
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
					throw new ZhsqEventException("??????????????????[msgSendDayStart]??????????????????????????????" + DateUtils.PATTERN_DATE);
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
					throw new ZhsqEventException("??????????????????[msgSendDayEnd]??????????????????????????????" + DateUtils.PATTERN_DATE);
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
	 * ????????????????????????
	 * @param params
	 * @return
	 */
	public int findCount4MsgReading(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4MsgReading(params);
		
		return total;
	}
	
	/**
	 * ????????????????????????????????????
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findPagination4MsgReading(int pageNo, int pageSize, Map<String, Object> params) {
		int total = 0;
		List<Map<String, Object>> reportList = null;
		
		total = capReportExtendMapper().findCount4MsgReading(params);
		
		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			reportList = capReportExtendMapper().findList4MsgReading(params, rowBounds);
			
			formatDataOut4MsgReading(reportList, params);
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination reportPagination = new EUDGPagination(total, reportList);
		
		return reportPagination;
	}
	
	@Override
	public boolean recordPoint(int recordType, UserInfo userInfo, Map<String, Object> params) throws Exception {
		boolean flag = false;
		ProInstance proInstance = null;
		String reportType = null, interalReason = null, ruleType = null,
			   subModularCode = "F-OA004REF",
			   nextNodeName = null;
		Integer formTypeId = null;
		Long reportId = null;
		boolean isAble2Record = true;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("?????????????????????reportType??????");
		}
		
		if(userInfo == null) {
			throw new IllegalArgumentException("???????????????????????????");
		}
		
		if(CommonFunctions.isNotBlank(params, "proInstance")) {
			Object proInstanceObj = params.get("proInstance");
			
			if(proInstanceObj instanceof ProInstance) {
				proInstance = (ProInstance) proInstanceObj;
			}
		} else if(CommonFunctions.isNotBlank(params, "instanceId")) {
			Long instanceId = Long.valueOf(params.get("instanceId").toString());
			
			proInstance = baseWorkflowService.findProByInstanceId(instanceId);
		}
		
		if(proInstance == null) {
			throw new ZhsqEventException("????????????????????????????????????");
		}
		
		formTypeId = proInstance.getFormTypeId();
		reportId = proInstance.getFormId();
		
		subModularCode += formTypeId;
			
		Long creatorId = proInstance.getUserId(), createOrgId = proInstance.getOrgId(),
			 operatorId = userInfo.getUserId(), operatorOrgId = userInfo.getOrgId(),
			 createRegionId = null;
		OrgSocialInfoBO orgInfo = null;
		String regionCode = null, creatorOrgCode = null;
		
		if(createOrgId != null && createOrgId > 0) {
			orgInfo = orgSocialInfoService.findByOrgId(createOrgId);
			
			if(orgInfo != null) {
				creatorOrgCode = orgInfo.getOrgCode();
				createRegionId = orgInfo.getLocationOrgId();
			}
		}
		
		if(createRegionId != null && createRegionId > 0) {
			OrgEntityInfoBO regionInfo = orgEntityInfoService.findByOrgId(createRegionId);
			
			if(regionInfo != null) {
				regionCode = regionInfo.getOrgCode();
			}
		}
		
		if(creatorId != null && creatorId > 0 && StringUtils.isNotBlank(regionCode)) {
			IdRecord idRecord = new IdRecord();
			String TABLE_TYPE = "GRID_USER";//?????????
				   //GRID_ADMIN_DUTY = "8,9,10,11,12,13,14,15";
			String[] GRID_ADMIN_DUTY_NAME = new String[] {"???????????????", "??????????????????", "???????????????", "??????????????????", "???????????????", "???????????????", "???????????????", "????????????"};
			boolean rewardPointFlag = false;
			Map<String, Object> gridAdminParam = new HashMap<String, Object>();
			int gridAdminCount = 0;
			int //RECORD_TYPE_BONUS = 1, 	//??????
				RECORD_TYPE_REDUCT = 2;	//??????
			
			gridAdminParam.put("userId", creatorId);
			gridAdminParam.put("infoOrgCode", regionCode);
			gridAdminParam.put("notRecursive", true);
			//gridAdminParam.put("duty", GRID_ADMIN_DUTY);
			gridAdminParam.put("userDutys", GRID_ADMIN_DUTY_NAME);
			
			gridAdminCount = gridAdminService.findCountGridAdmin(gridAdminParam);
			
			if(gridAdminCount > 0) {
				switch(recordType) {
					case 1: {//??????
						String TIME_OUT = "1", isTimeOut = null;
						
						ruleType = "01";//??????
						interalReason = "????????????";
						
						if(creatorId.equals(operatorId) && createOrgId.equals(operatorOrgId)) {
							List<Map<String, Object>> taskMapList = workflow4BaseService.capHandledTaskInfoMap(proInstance.getInstanceId(), IWorkflow4BaseService.SQL_ORDER_DESC, null);
							
							if(taskMapList != null && taskMapList.size() > 0) {
								Map<String, Object> taskMap = taskMapList.get(0);
								
								if(CommonFunctions.isNotBlank(taskMap, "ISTIMEOUT")) {
									isTimeOut = taskMap.get("ISTIMEOUT").toString();
								}
							}
						}
						
						if(TIME_OUT.equals(isTimeOut)) {
							interalReason = "????????????";
							recordType = RECORD_TYPE_REDUCT;
							subModularCode += "002";
						} else {
							if(CommonFunctions.isBlank(params, "nextNodeName")) {
								throw new IllegalArgumentException("?????????????????????????????????nextNodeName??????");
							}
							
							nextNodeName = params.get("nextNodeName").toString();
							
							subModularCode += nextNodeName + "001";
						}
						
						break;
					}
					case 2: {//??????
						Long userOrgId = userInfo.getOrgId();
						
						interalReason = "????????????";
						
						if(userOrgId != null && userOrgId > 0) {
							OrgSocialInfoBO userOrgInfo = orgSocialInfoService.findByOrgId(userOrgId);
							String orgChiefLevel = null;
							
							if(userOrgInfo != null) {
								orgChiefLevel = userOrgInfo.getChiefLevel();
							}
							
							if(ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel)
									|| ConstantValue.DISTRICT_ORG_LEVEL.equals(orgChiefLevel)) {
								if(ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel)) {
									subModularCode += "004003";
								} else if(ConstantValue.DISTRICT_ORG_LEVEL.equals(orgChiefLevel)) {
									subModularCode += "003003";
								}
							} else {
								isAble2Record = false;
							}
						}
						
						break;
					}
					default: {
						throw new IllegalArgumentException("???????????????" + reportType + "??????????????????");
					}
				}
				
				if(isAble2Record) {
					idRecord.setPartyMemberId(creatorId);
					idRecord.setOrgCode(creatorOrgCode);
					idRecord.setRegionCode(regionCode);
					idRecord.setCreator(operatorId);
					idRecord.setSubmodularCode(subModularCode);
					idRecord.setTableType(TABLE_TYPE);
					idRecord.setBizId(reportId);
					idRecord.setInteralReason(interalReason);
					
					switch(recordType) {
						case 1: {
							idRecord.setRuleType(ruleType);
							rewardPointFlag = integralRuleService.rewardPointsV2(idRecord, null);
							break;
						}
						case 2: {
							rewardPointFlag = integralRuleService.deductionPoints(idRecord, null);
							break;
						}
					}
					
					if(!rewardPointFlag && logger.isErrorEnabled()) {
						logger.error("????????????????????????????????????" + JsonUtils.beanToJson(idRecord));
					}
				} else if(logger.isErrorEnabled()) {
					logger.error("?????????????????????????????????????????????reportId : " + reportId + "  " + JsonUtils.mapToJson(gridAdminParam));
				}
			} else if(logger.isErrorEnabled()) {
				logger.error("?????????????????????????????????????????????reportId : " + reportId + "  " + JsonUtils.mapToJson(gridAdminParam));
			}
		}
		
		return flag;
	}
	
	/**
	 * ?????????????????????
	 * @param params
	 * @throws ZhsqEventException 
	 */
	protected void formatParamIn(Map<String, Object> params) throws ZhsqEventException {
		if(CommonFunctions.isNotBlank(params, "eRegionCode")) {
			byte[] decodedData = Base64.decode(params.get("eRegionCode").toString());
			params.put("regionCode", new String(decodedData));
		}
		
		if(CommonFunctions.isNotBlank(params, "reportStatus")) {
			String reportStatus = params.get("reportStatus").toString();
			
			if(reportStatus.contains(",")) {
				params.put("reportStatusArray", reportStatus.split(","));
				params.remove("reportStatus");
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "reportDayStart")) {
			Object reportDayStartObj = params.get("reportDayStart");
			Date reportDayStart = null;
			
			if(reportDayStartObj instanceof String) {
				try {
					reportDayStart = DateUtils.convertStringToDate(reportDayStartObj.toString(), DateUtils.PATTERN_DATE);
				} catch (ParseException e) {
					throw new ZhsqEventException("??????????????????[reportDayStart]??????????????????????????????" + DateUtils.PATTERN_DATE);
				}
			} else if(reportDayStartObj instanceof Date) {
				reportDayStart = (Date) reportDayStartObj;
			}
			
			params.put("reportDayStart", reportDayStart);
		} else {
			params.remove("reportDayStart");
		}
		
		if(CommonFunctions.isNotBlank(params, "reportDayEnd")) {
			Object reportDayEndObj = params.get("reportDayEnd");
			Date reportDayEnd = null;
			
			if(reportDayEndObj instanceof String) {
				try {
					reportDayEnd = DateUtils.convertStringToDate(reportDayEndObj.toString(), DateUtils.PATTERN_DATE);
				} catch (ParseException e) {
					throw new ZhsqEventException("??????????????????[reportDayEnd]??????????????????????????????" + DateUtils.PATTERN_DATE);
				}
			} else if(reportDayEndObj instanceof Date) {
				reportDayEnd = (Date) reportDayEndObj;
			}
			
			params.put("reportDayEnd", reportDayEnd);
		} else {
			params.remove("reportDayEnd");
		}
		
		if(CommonFunctions.isNotBlank(params, "instanceEndDayStart")) {
			Object instanceEndDayStartObj = params.get("instanceEndDayStart");
			Date instanceEndDayStart = null;
			
			if(instanceEndDayStartObj instanceof String) {
				try {
					instanceEndDayStart = DateUtils.convertStringToDate(instanceEndDayStartObj.toString(), DateUtils.PATTERN_DATE);
				} catch (ParseException e) {
					throw new ZhsqEventException("??????????????????[instanceEndDayStart]??????????????????????????????" + DateUtils.PATTERN_DATE);
				}
			} else if(instanceEndDayStartObj instanceof Date) {
				instanceEndDayStart = (Date) instanceEndDayStartObj;
			}
			
			params.put("instanceEndDayStart", instanceEndDayStart);
		} else {
			params.remove("instanceEndDayStart");
		}
		
		if(CommonFunctions.isNotBlank(params, "instanceEndDayEnd")) {
			Object instanceEndDayEndObj = params.get("instanceEndDayEnd");
			Date instanceEndDayEnd = null;
			
			if(instanceEndDayEndObj instanceof String) {
				try {
					instanceEndDayEnd = DateUtils.convertStringToDate(instanceEndDayEndObj.toString(), DateUtils.PATTERN_DATE);
				} catch (ParseException e) {
					throw new ZhsqEventException("??????????????????[instanceEndDayEnd]??????????????????????????????" + DateUtils.PATTERN_DATE);
				}
			} else if(instanceEndDayEndObj instanceof Date) {
				instanceEndDayEnd = (Date) instanceEndDayEndObj;
			}
			
			params.put("instanceEndDayEnd", instanceEndDayEnd);
		} else {
			params.remove("instanceEndDayEnd");
		}
	}
	
	/**
	 * ?????????????????????
	 * @param reportList
	 * @param params
	 */
	protected void formatDataOut(List<Map<String, Object>> reportList, Map<String, Object> params) {
		if(reportList != null && reportList.size() > 0) {
			String userOrgCode = null;
			boolean isDictTransfer = true;
			List<BaseDataDict> reportWayDictList = null;
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			}
			if(CommonFunctions.isNotBlank(params, "isDictTransfer")) {
				isDictTransfer = Boolean.valueOf(params.get("isDictTransfer").toString());
			}
			
			if(isDictTransfer && StringUtils.isNotBlank(userOrgCode)) {
				reportWayDictList = baseDictionaryService.getDataDictListOfSinglestage(TwoVioPreDictEnum.REPORT_WAY.getDictCode(), userOrgCode);
			}
			
			for(Map<String, Object> reportMap : reportList) {
				if(CommonFunctions.isNotBlank(reportMap, "reportTime")) {
					reportMap.put("reportTimeStr", DateUtils.formatDate((Date) reportMap.get("reportTime"), DateUtils.PATTERN_24TIME));
				}

				if(CommonFunctions.isNotBlank(reportMap, "meetingTime")) {
					reportMap.put("meetingTimeStr", DateUtils.formatDate((Date) reportMap.get("meetingTime"), DateUtils.PATTERN_24TIME));
				}
				
				if(CommonFunctions.isNotBlank(reportMap, "instanceEndTime")) {
					reportMap.put("instanceEndTimeStr", DateUtils.formatDate((Date) reportMap.get("instanceEndTime"), DateUtils.PATTERN_24TIME));
				}
				
				DataDictHelper.setDictValueForField(reportMap, "reportWay", "reportWayName", reportWayDictList);
			}
		}
	}
	
	/**
	 * ?????????????????????????????????
	 * @param reportList
	 * @param params
	 */
	protected void formatDataOut4MsgReading(List<Map<String, Object>> reportList, Map<String, Object> params) {
		if(reportList != null && reportList.size() > 0) {
			Map<String, String> msgReceiveStatusMap = new HashMap<String, String>();
			msgReceiveStatusMap.put("0", "??????");
			msgReceiveStatusMap.put("1", "??????");
			
			for(Map<String, Object> reportMap : reportList) {
				if(CommonFunctions.isNotBlank(reportMap, "msgSendTime")) {
					reportMap.put("msgSendTimeStr", DateUtils.formatDate((Date) reportMap.get("msgSendTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(reportMap, "msgReceiveStatus")) {
					reportMap.put("msgReceiveStatusName", msgReceiveStatusMap.get(reportMap.get("msgReceiveStatus")));
				}
			}
		}
	}
}
