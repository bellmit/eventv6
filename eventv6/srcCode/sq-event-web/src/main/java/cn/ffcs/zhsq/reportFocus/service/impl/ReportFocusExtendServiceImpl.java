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
	 * 获取操作mapper
	 * @return
	 */
	protected abstract ReportTwoViolationPreMapper capReportExtendMapper();
	
	/**
	 * 获取附件业务类型
	 * @return
	 */
	protected abstract String capBizType4Attachment();
	
	/**
	 * 获取定位业务类型
	 * @return
	 */
	protected abstract String capBizType4ResmarkerInfo();
	
	/**
	 * 草稿入参格式化
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
	 * 获取草稿列表数量
	 * @param params
	 * @return
	 */
	public int findCount4Draft(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4Draft(params);
		
		return total;
	}
	
	/**
	 * 分页获取草稿列表记录
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
	 * 待办列表入参格式化
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
	 * 获取待办列表数量
	 * @param params
	 * @return
	 */
	public int findCount4Todo(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4Todo(params);
		
		return total;
	}
	
	/**
	 * 分页获取待办列表记录
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
	 * 经办列表入参格式化
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
	 * 获取经办列表数量
	 * @param params
	 * @return
	 */
	public int findCount4Handled(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4Handled(params);
		
		return total;
	}
	
	/**
	 * 分页获取经办列表记录
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
	 * 我发起的列表入参格式化
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
	 * 获取我发起的列表数量
	 * @param params
	 * @return
	 */
	public int findCount4Initiator(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4Initiator(params);
		
		return total;
	}
	
	/**
	 * 分页获取我发起的列表记录
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
	 * 辖区所有列表(精简)入参格式化
	 * @param params
	 */
	public void formatParamIn4JurisdictionSimplify(Map<String, Object> params) {
		formatParamIn4Jurisdiction(params);
	}
	
	/**
	 * 获取辖区所有列表(精简)数量
	 * @param params
	 * @return
	 */
	public int findCount4JurisdictionSimplify(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4JurisdictionSimplify(params);
		
		return total;
	}
	
	/**
	 * 分页获取辖区所有列表(精简)记录
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
	 * 辖区所有列表入参格式化
	 * @param params
	 */
	public void formatParamIn4Jurisdiction(Map<String, Object> params) {
		if(CommonFunctions.isBlank(params, "regionCode") && CommonFunctions.isNotBlank(params, "startRegionCode")) {
			params.put("regionCode", params.get("startRegionCode"));
		}
	}
	
	/**
	 * 获取辖区所有列表数量
	 * @param params
	 * @return
	 */
	public int findCount4Jurisdiction(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4Jurisdiction(params);
		
		return total;
	}
	
	/**
	 * 分页获取辖区所有列表记录
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
	 * 辖区所有列表(附带经纬度)入参格式化
	 * @param params
	 */
	public void formatParamIn4JurisdictionWithMarker(Map<String, Object> params) {
		formatParamIn4Jurisdiction(params);
	}

	/**
	 * 辖区所有列表(附带经纬度)入参格式化
	 * @param params
	 */
	public void formatParamIn4JurisdictionWithRegionPath(Map<String, Object> params) {
		formatParamIn4Jurisdiction(params);
	}
	
	/**
	 * 获取辖区所有列表(附带经纬度)数量
	 * @param params
	 * @return
	 */
	public int findCount4JurisdictionWithMarker(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4JurisdictionWithMarker(params);
		
		return total;
	}
	
	/**
	 * 分页获取辖区所有列表(附带经纬度)记录
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
	 * 获取辖区所有列表(附带地域全路径)数量
	 * @param params
	 * @return
	 */
	public int findCount4JurisdictionWithRegionPath(Map<String, Object> params) {
		int total = 0;

		total = capReportExtendMapper().findCount4JurisdictionWithRegionPath(params);

		return total;
	}

	/**
	 * 分页获取辖区所有列表(附带地域全路径)记录
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
	 * 不分页获取辖区所有列表记录
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
	 * 获取环节办理意见，此处不作业务处理，具体业务处理放到每个子类中
	 * */
	public List<Map<String, Object>> capTaskAdvice(List<Map<String, Object>> reportList,Map<String, Object> params) {
		return reportList;
	}
	/**
	 * 导出逻辑公共处理方法
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

				//当前层级为 网格
				if(ConstantValue.GRID_ORG_LEVEL.equals(orgLevel)){
					reportFocus.put("gridName",orgEntityInfoBO.getOrgName());

					//所属村社区
					communityEntityBo = orgEntityInfoService.findByOrgId(parentOrgId);
				}else if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgLevel)){
					//当前层级为 村、社区
					reportFocus.put("communityName",orgEntityInfoBO.getOrgName());

					streetEntityBo = orgEntityInfoService.findByOrgId(parentOrgId);
				}else if(ConstantValue.STREET_ORG_LEVEL.equals(orgLevel)){
					//当前层级为 乡镇、街道
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
     * 辖区归档列表入参格式化
     * @param params
     */
    public void formatParamIn4Archived(Map<String, Object> params) {
        formatParamIn4Jurisdiction(params);

        params.put("reportStatus", MeetingReportStatusEnum.END_STATUS.getReportStatus());
    }

    /**
     * 获取辖区归档列表数量
     * @param params
     * @return
     */
    public int findCount4Archived(Map<String, Object> params) {
    	int total = 0;

        total = capReportExtendMapper().findCount4Archived(params);

        return total;
    }

    /**
     * 分页获取辖区归档列表记录
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
	 * 我的阅办列表入参格式化
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
	 * 我的阅办列表数量
	 * @param params
	 * @return
	 */
	public int findCount4MsgReading(Map<String, Object> params) {
		int total = 0;
		
		total = capReportExtendMapper().findCount4MsgReading(params);
		
		return total;
	}
	
	/**
	 * 分页获取我的阅办列表记录
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
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
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
			throw new ZhsqEventException("未能找到有效的流程信息！");
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
			String TABLE_TYPE = "GRID_USER";//网格员
				   //GRID_ADMIN_DUTY = "8,9,10,11,12,13,14,15";
			String[] GRID_ADMIN_DUTY_NAME = new String[] {"第一网格长", "第一副网格长", "一级网格长", "一级副网格长", "一级网格员", "二级网格长", "二级网格员", "网格专干"};
			boolean rewardPointFlag = false;
			Map<String, Object> gridAdminParam = new HashMap<String, Object>();
			int gridAdminCount = 0;
			int //RECORD_TYPE_BONUS = 1, 	//加分
				RECORD_TYPE_REDUCT = 2;	//减分
			
			gridAdminParam.put("userId", creatorId);
			gridAdminParam.put("infoOrgCode", regionCode);
			gridAdminParam.put("notRecursive", true);
			//gridAdminParam.put("duty", GRID_ADMIN_DUTY);
			gridAdminParam.put("userDutys", GRID_ADMIN_DUTY_NAME);
			
			gridAdminCount = gridAdminService.findCountGridAdmin(gridAdminParam);
			
			if(gridAdminCount > 0) {
				switch(recordType) {
					case 1: {//提交
						String TIME_OUT = "1", isTimeOut = null;
						
						ruleType = "01";//加分
						interalReason = "上报操作";
						
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
							interalReason = "办理超时";
							recordType = RECORD_TYPE_REDUCT;
							subModularCode += "002";
						} else {
							if(CommonFunctions.isBlank(params, "nextNodeName")) {
								throw new IllegalArgumentException("缺少下一办理环节信息【nextNodeName】！");
							}
							
							nextNodeName = params.get("nextNodeName").toString();
							
							subModularCode += nextNodeName + "001";
						}
						
						break;
					}
					case 2: {//督办
						Long userOrgId = userInfo.getOrgId();
						
						interalReason = "督办操作";
						
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
						throw new IllegalArgumentException("记录类型【" + reportType + "】无法识别！");
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
						logger.error("积分添加失败！参数如下：" + JsonUtils.beanToJson(idRecord));
					}
				} else if(logger.isErrorEnabled()) {
					logger.error("未满足积分添加条件！参数如下：reportId : " + reportId + "  " + JsonUtils.mapToJson(gridAdminParam));
				}
			} else if(logger.isErrorEnabled()) {
				logger.error("未满足积分添加条件！参数如下：reportId : " + reportId + "  " + JsonUtils.mapToJson(gridAdminParam));
			}
		}
		
		return flag;
	}
	
	/**
	 * 格式化输入参数
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
					throw new ZhsqEventException("报告开始时间[reportDayStart]，不是如下时间格式：" + DateUtils.PATTERN_DATE);
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
					throw new ZhsqEventException("报告结束时间[reportDayEnd]，不是如下时间格式：" + DateUtils.PATTERN_DATE);
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
					throw new ZhsqEventException("办结开始时间[instanceEndDayStart]，不是如下时间格式：" + DateUtils.PATTERN_DATE);
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
					throw new ZhsqEventException("办结结束时间[instanceEndDayEnd]，不是如下时间格式：" + DateUtils.PATTERN_DATE);
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
	 * 数据格式化输出
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
	 * 我的阅办数据格式化输出
	 * @param reportList
	 * @param params
	 */
	protected void formatDataOut4MsgReading(List<Map<String, Object>> reportList, Map<String, Object> params) {
		if(reportList != null && reportList.size() > 0) {
			Map<String, String> msgReceiveStatusMap = new HashMap<String, String>();
			msgReceiveStatusMap.put("0", "未读");
			msgReceiveStatusMap.put("1", "已读");
			
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
