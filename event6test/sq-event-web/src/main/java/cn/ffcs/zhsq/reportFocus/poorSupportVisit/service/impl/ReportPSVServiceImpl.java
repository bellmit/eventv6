package cn.ffcs.zhsq.reportFocus.poorSupportVisit.service.impl;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.poorSupportVisit.ReportPSVMapper;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.twoViolationPre.ReportTwoViolationPreMapper;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportNumberCfgBizCodeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportTwoViolationPreServiceImpl;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventExceptionEnum;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 扶贫走访，相关涉及表：T_EVENT_POOR_SUPPORT_VISIT
 * @ClassName:   ReportPSVServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年12月4日 上午10:45:25
 */
@Service("reportPSVService")
public class ReportPSVServiceImpl extends ReportTwoViolationPreServiceImpl {
	@Autowired
	private ReportPSVMapper reportPSVMapper;
	
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
				put(50, "4JurisdictionSimplify");
				put(51,"4JurisdictionWithMarker");
				put(52,"4JurisdictionWithRegionPath");
				put(6, "4Archived");
				put(7, "4MsgReading");
			}
		};
	}
	
	@Override
	public String saveReportExtendInfo(Map<String, Object> psvParam, UserInfo userInfo) {
		if(psvParam == null || psvParam.isEmpty()) {
			throw new IllegalArgumentException("缺少需要操作的扶贫走访信息！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		Long userId = userInfo.getUserId();
		String psvUUID = null;
		
		if(CommonFunctions.isBlank(psvParam, "creator")) {
			psvParam.put("creator", userId);
		}
		if(CommonFunctions.isBlank(psvParam, "updator")) {
			psvParam.put("updator", userId);
		}
		
		if(CommonFunctions.isNotBlank(psvParam, "psvUUID")) {
			if(reportPSVMapper.update(psvParam) > 0) {
				psvUUID = psvParam.get("psvUUID").toString();
			}
		} else {
			if(CommonFunctions.isBlank(psvParam, "reportUUID")) {
				throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
			}
			
			//为了保证记录的唯一性
			Map<String, Object> psvMap = reportPSVMapper.findByIdSimple(psvParam);
			
			if(psvMap != null && !psvMap.isEmpty()) {
				Map<String, Object> psvMapAfter = new HashMap<String, Object>(),
									psvMapAfterRemain = new HashMap<String, Object>();
				psvMapAfter.putAll(psvMap);
				psvMapAfter.putAll(psvParam);
				
				for(String key : psvMapAfter.keySet()) {
					if(CommonFunctions.isBlank(psvMap, key) 
							|| !psvMapAfter.get(key).equals(psvMap.get(key))) {
						psvMapAfterRemain.put(key, psvMapAfter.get(key));
					}
				}
				
				if(!psvMapAfterRemain.isEmpty()) {
					psvUUID = psvMap.get("psvUUID").toString();
					psvMapAfterRemain.put("psvUUID", psvUUID);
					
					if(reportPSVMapper.update(psvMapAfterRemain) > 0) {
						psvUUID = psvMap.get("psvUUID").toString();
					}
				} else {
					psvUUID = psvMap.get("psvUUID").toString();
				}
			} else if(reportPSVMapper.insert(psvParam) > 0) {
				if(CommonFunctions.isNotBlank(psvParam, "psvUUID")) {
					psvUUID = psvParam.get("psvUUID").toString();
				}
			}
			
			saveReportExtraInfo(psvParam);
		}
		
		return psvUUID;
	}

	@Override
	public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo,
			Map<String, Object> params) {
		Map<String, Object> psvParam = new HashMap<String, Object>(), psvParamMap = null;
		boolean isWithReportFocus = true;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
			isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
		}
		
		if(StringUtils.isNotBlank(reportUUID)) {
			psvParam.put("reportUUID", reportUUID);
		} else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
			psvParam.put("reportUUID", params.get("reportUUID"));
		} else if(CommonFunctions.isNotBlank(params, "psvUUID")) {
			psvParam.put("psvUUID", params.get("psvUUID"));
		} else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
			psvParam.put("reportId", params.get("reportId"));
		}
		
		if(psvParam.isEmpty()) {
			throw new IllegalArgumentException("缺失唯一性条件，不可查询扶贫走访信息！");
		}
		
		if(isWithReportFocus) {
			psvParamMap = reportPSVMapper.findById(psvParam);
			
			if(psvParamMap != null && !psvParamMap.isEmpty()) {
				List<Map<String, Object>> psvMapList = new ArrayList<Map<String, Object>>();
				
				psvMapList.add(psvParamMap);
				
				if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
					params.put("userOrgCode", userInfo.getOrgCode());
				}
				
				formatDataOut(psvMapList, params);
			}
		} else {
			psvParamMap = reportPSVMapper.findByIdSimple(psvParam);
		}
		
		findReportExtraInfo(psvParamMap, params);
		
		return psvParamMap;
	}
	
	@Override
	public String capNumberCfgBizCode() {
		return ReportNumberCfgBizCodeEnum.poorSupportVisit.getBizCode();
	}
	
	protected ReportTwoViolationPreMapper capReportExtendMapper() {
		return reportPSVMapper;
	}
	
	protected String capBizType4Attachment() {
		return "POOR_SUPPORT_VISIT";
	}
	
	protected String capBizType4ResmarkerInfo() {
		return "POOR_SUPPORT_VISIT";
	}

	/**
	 * 格式化输入参数
	 * @param params
	 * @throws ZhsqEventException
	 */
	@Override
	protected void formatParamIn(Map<String, Object> params) throws ZhsqEventException {
		super.formatParamIn(params);
		if(CommonFunctions.isNotBlank(params, "collectSource")) {
			params.put("collectSourceArray", params.get("collectSource").toString().split(","));
			params.remove("collectSource");
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
			List<BaseDataDict> reportStatusDictList = null,
							   collectSourceDictList = null;
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
				collectSourceDictList = baseDictionaryService.getDataDictListOfSinglestage(PSVDictEnum.COLLECT_SOURCE.getDictCode(), userOrgCode);
				
				if(listType == 1) {
					reportStatusDictList = new ArrayList<BaseDataDict>();
					//草稿不增添字典，因为只有草稿列表会用到
					BaseDataDict draftDict = new BaseDataDict();
					draftDict.setDictGeneralCode(PSVReportStatusEnum.DRAFT_STATUS.getReportStatus());
					draftDict.setDictName(PSVReportStatusEnum.DRAFT_STATUS.getReportStatusName());
					reportStatusDictList.add(draftDict);
				} else {
					reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(PSVDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
				}
			}
			
			for(Map<String, Object> reportMap : reportList) {
				DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
				DataDictHelper.setDictValueForField(reportMap, "collectSource", "collectSourceName", collectSourceDictList);
			}
		}
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
	@Override
	protected boolean isRegionSatisfied(OrgEntityInfoBO orgEntityInfo, Map<String, Object> params) throws Exception {
		boolean flag = true, isEditableNode = false;
		String regionChiefLevel = null, collectSource = null, regionCode = null;

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

		//采集渠道为 01 的，地址控件必须选择到网格层级
		//或者当前环节为第一副网格长核实环节时，无论哪个来源都需要选择到网格层级，因为启动子流程时村社区向下寻找二级网格长找不到 isEditableNode
		if(PSVCollectSourceEnum.GRID_REPORT.getCollectSource().equals(collectSource) || isEditableNode) {
			if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)) {
				if(logger.isErrorEnabled()) {
					logger.error("地域编码【" + regionCode + "】不是网格层级！");
				}
				
				throw ZhsqEventExceptionEnum.NOT_GRID_LEVEL_REGION_EXCEPTION.getZhsqEventException();
			}
		} else {
			//采集渠道不是 01 的，地址控件需要选择到网格层级或村社区层级
			if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel) && !ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)){
				throw new ZhsqEventException("地域编码【" + regionCode + "】既不是网格层级，也不是村/社区层级！");
			}
		}

		return flag;
	}

}
