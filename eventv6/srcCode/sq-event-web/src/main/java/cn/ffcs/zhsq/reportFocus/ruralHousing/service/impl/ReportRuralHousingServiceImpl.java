package cn.ffcs.zhsq.reportFocus.ruralHousing.service.impl;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.ruralHousing.ReportRuralHousingMapper;
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
 * @Description: 农村建房，相关表：T_EVENT_RURAL_HOUSING
 * @ClassName:   ReportRuralHousingServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年3月8日 下午6:10:33
 */
@Service("reportRuralHousingService")
public class ReportRuralHousingServiceImpl extends ReportTwoViolationPreServiceImpl {
	@Autowired
	private ReportRuralHousingMapper reportRuralHousingMapper;
	
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
	public String saveReportExtendInfo(Map<String, Object> rhParam, UserInfo userInfo) throws Exception {
		if(rhParam == null || rhParam.isEmpty()) {
			throw new IllegalArgumentException("缺少需要操作的农村建房！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		Long userId = userInfo.getUserId();
		String rhUUID = null;
		
		if(CommonFunctions.isBlank(rhParam, "creator")) {
			rhParam.put("creator", userId);
		}
		if(CommonFunctions.isBlank(rhParam, "updator")) {
			rhParam.put("updator", userId);
		}
		
		if(CommonFunctions.isNotBlank(rhParam, "rhUUID")) {
			if(reportRuralHousingMapper.update(rhParam) > 0) {
				rhUUID = rhParam.get("rhUUID").toString();
			}
		} else {
			if(CommonFunctions.isBlank(rhParam, "reportUUID")) {
				throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
			}
			
			//为了保证记录的唯一性
			Map<String, Object> rhMap = reportRuralHousingMapper.findByIdSimple(rhParam);
			
			if(rhMap != null && !rhMap.isEmpty()) {
				Map<String, Object> rhMapAfter = new HashMap<String, Object>(),
									rhMapAfterRemain = new HashMap<String, Object>();
				rhMapAfter.putAll(rhMap);
				rhMapAfter.putAll(rhParam);
				
				for(String key : rhMapAfter.keySet()) {
					if(CommonFunctions.isBlank(rhMap, key) 
							|| !rhMapAfter.get(key).equals(rhMap.get(key))) {
						rhMapAfterRemain.put(key, rhMapAfter.get(key));
					}
				}
				
				if(!rhMapAfterRemain.isEmpty()) {
					rhUUID = rhMap.get("rhUUID").toString();
					rhMapAfterRemain.put("rhUUID", rhUUID);
					
					if(reportRuralHousingMapper.update(rhMapAfterRemain) > 0) {
						rhUUID = rhMap.get("rhUUID").toString();
					}
				} else {
					rhUUID = rhMap.get("rhUUID").toString();
				}
			} else if(reportRuralHousingMapper.insert(rhParam) > 0) {
				if(CommonFunctions.isNotBlank(rhParam, "rhUUID")) {
					rhUUID = rhParam.get("rhUUID").toString();
				}
			}
			
			saveReportExtraInfo(rhParam);
		}
		
		return rhUUID;
	}

	@Override
	public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo,
			Map<String, Object> params) {
		Map<String, Object> rhParam = new HashMap<String, Object>(), rhParamMap = null;
		boolean isWithReportFocus = true;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
			isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
		}
		
		if(StringUtils.isNotBlank(reportUUID)) {
			rhParam.put("reportUUID", reportUUID);
		} else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
			rhParam.put("reportUUID", params.get("reportUUID"));
		} else if(CommonFunctions.isNotBlank(params, "rhUUID")) {
			rhParam.put("rhUUID", params.get("rhUUID"));
		} else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
			rhParam.put("reportId", params.get("reportId"));
		}
		
		if(rhParam.isEmpty()) {
			throw new IllegalArgumentException("缺失唯一性条件，不可查询农村建房！");
		}
		
		if(isWithReportFocus) {
			rhParamMap = reportRuralHousingMapper.findById(rhParam);
			
			if(rhParamMap != null && !rhParamMap.isEmpty()) {
				List<Map<String, Object>> rhMapList = new ArrayList<Map<String, Object>>();
				
				rhMapList.add(rhParamMap);
				
				if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
					params.put("userOrgCode", userInfo.getOrgCode());
				}
				
				formatDataOut(rhMapList, params);
			}
		} else {
			rhParamMap = reportRuralHousingMapper.findByIdSimple(rhParam);
		}
		
		findReportExtraInfo(rhParamMap, params);
		
		return rhParamMap;
	}
	
	@Override
	public String capNumberCfgBizCode() {
		return ReportNumberCfgBizCodeEnum.ruralHousing.getBizCode();
	}
	
	@Override
	public void formatParamIn4Report(Map<String, Object> params) throws Exception {
		isDuplicated(params);
		super.formatParamIn4Report(params);
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
		boolean flag = true;
		String regionChiefLevel = null, regionCode = null;
		
		if(orgEntityInfo != null) {
			regionCode = orgEntityInfo.getOrgCode();
			regionChiefLevel = orgEntityInfo.getChiefLevel();
		}

		if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)) {
			if(logger.isErrorEnabled()) {
				logger.error("地域编码【" + regionCode + "】不是网格层级！");
			}
			
			throw ZhsqEventExceptionEnum.NOT_GRID_LEVEL_REGION_EXCEPTION.getZhsqEventException();
		}
		
		return flag;
	}
	
	protected ReportTwoViolationPreMapper capReportExtendMapper() {
		return reportRuralHousingMapper;
	}
	
	protected String capBizType4Attachment() {
		return "RURAL_HOUSING";
	}
	
	protected String capBizType4ResmarkerInfo() {
		return "RURAL_HOUSING";
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
							   rhCardTypeDictList = null;
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
				rhCardTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(RuralHousingDictEnum.RH_CARD_TYPE.getDictCode(), userOrgCode);
				
				if(listType == 1) {
					reportStatusDictList = new ArrayList<BaseDataDict>();
					//草稿不增添字典，因为只有草稿列表会用到
					BaseDataDict draftDict = new BaseDataDict();
					draftDict.setDictGeneralCode(RuralHousingReportStatusEnum.DRAFT_STATUS.getReportStatus());
					draftDict.setDictName(RuralHousingReportStatusEnum.DRAFT_STATUS.getReportStatusName());
					reportStatusDictList.add(draftDict);
				} else {
					reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(RuralHousingDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
				}
			}
			
			for(Map<String, Object> reportMap : reportList) {
				
				DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
				DataDictHelper.setDictValueForField(reportMap, "rhCardType", "rhCardTypeName", rhCardTypeDictList);
			}
		}
	}
	
	/**
	 * 农村建房判重
	 * 1、在全平台(包含草稿)判断rcpCode(规划许可证编号)是否重复
	 * 2、在全平台(包含草稿)判断rhaCode(宅基地批准书编号)是否重复
	 * @param rhParam
	 * 			rcpCode	规划许可证编号
	 * 			rhaCode	宅基地批准书编号
	 * @return true则记录重复；false则不重复
	 * @throws ZhsqEventException
	 */
	private boolean isDuplicated(Map<String, Object> rhParam) throws ZhsqEventException {
		boolean flag = false;
		String DRAFT_VALID = "2";
		Map<String, Object> params = new HashMap<String, Object>(),
							draftParams = new HashMap<String, Object>();
		
		if(CommonFunctions.isNotBlank(rhParam, "rcpCode")) {
			params.clear();
			draftParams.clear();
			params.put("rcpCode", rhParam.get("rcpCode"));
			draftParams.putAll(params);
			draftParams.put("reportValid", DRAFT_VALID);
			
			if(isDuplicated4Draft(rhParam, draftParams) || isDuplicated4Jurisdiction(rhParam, params)) {
				throw new ZhsqEventException("规划许可证编号【" + rhParam.get("rcpCode") + "】已存在！");
			}
		}
		
		if(CommonFunctions.isNotBlank(rhParam, "rhaCode")) {
			params.clear();
			draftParams.clear();
			params.put("rhaCode", rhParam.get("rhaCode"));
			draftParams.putAll(params);
			draftParams.put("reportValid", DRAFT_VALID);
			
			if(isDuplicated4Draft(rhParam, draftParams) || isDuplicated4Jurisdiction(rhParam, params)) {
				throw new ZhsqEventException("宅基地批准书编号【" + rhParam.get("rhaCode") + "】已存在！");
			}
		}
		
		return flag;
	}
	
	/**
	 * 草稿农村建房数据判重
	 * @param rhParam
	 * @param params
	 * @return
	 */
	private boolean isDuplicated4Draft(Map<String, Object> rhParam, Map<String, Object> params) {
		boolean flag = false;
		String rhUUID = null, reportUUID = null;
		Long reportId = null;
		List<Map<String, Object>> rhMapList = null;
		
		if(CommonFunctions.isNotBlank(rhParam, "rhUUID")) {
			rhUUID = rhParam.get("rhUUID").toString();
		}
		
		if(CommonFunctions.isNotBlank(rhParam, "reportUUID")) {
			reportUUID = rhParam.get("reportUUID").toString();
		}
		
		if(CommonFunctions.isNotBlank(rhParam, "reportId")) {
			try {
				reportId = Long.valueOf(rhParam.get("reportId").toString());
			} catch(NumberFormatException e) {}
		}
		
		rhMapList = reportRuralHousingMapper.findList4Draft(params);
		
		if(rhMapList != null) {
			if(rhMapList.size() == 1) {
				Map<String, Object> rhMap = rhMapList.get(0);
				String rhUUIDT = null, reportUUIDT = null;
				Long reportIdT = null;
				
				if(CommonFunctions.isNotBlank(rhMap, "rhUUID")) {
					rhUUIDT = rhMap.get("rhUUID").toString();
				}
				
				if(CommonFunctions.isNotBlank(rhMap, "reportUUID")) {
					reportUUIDT = rhMap.get("reportUUID").toString();
				}
				
				if(CommonFunctions.isNotBlank(rhMap, "reportId")) {
					try {
						reportIdT = Long.valueOf(rhMap.get("reportId").toString());
					} catch(NumberFormatException e) {}
				}
				
				if(StringUtils.isNotBlank(rhUUIDT) && rhUUIDT.equals(rhUUID)
						|| StringUtils.isNotBlank(reportUUIDT) && reportUUIDT.equals(reportUUID)
						|| reportIdT != null && reportIdT.equals(reportId)) {
					flag = false;
				} else {
					flag = true;
				}
			} else if(rhMapList.size() > 1) {
				flag = true;
			}
		}
		
		if(flag && logger.isErrorEnabled()) {
			logger.error("该记录于草稿记录中有重复！");
		}
		
		return flag;
	}
	
	/**
	 * 辖区所有农村建房数据判重
	 * @param rhParam
	 * @param params
	 * @return
	 */
	private boolean isDuplicated4Jurisdiction(Map<String, Object> rhParam, Map<String, Object> params) {
		boolean flag = false;
		String rhUUID = null, reportUUID = null;
		Long reportId = null;
		List<Map<String, Object>> rhMapList = null;
		
		if(CommonFunctions.isNotBlank(rhParam, "rhUUID")) {
			rhUUID = rhParam.get("rhUUID").toString();
		}
		
		if(CommonFunctions.isNotBlank(rhParam, "reportUUID")) {
			reportUUID = rhParam.get("reportUUID").toString();
		}
		
		if(CommonFunctions.isNotBlank(rhParam, "reportId")) {
			try {
				reportId = Long.valueOf(rhParam.get("reportId").toString());
			} catch(NumberFormatException e) {}
		}
		
		rhMapList = reportRuralHousingMapper.findList4JurisdictionSimplify(params);
		
		if(rhMapList != null) {
			if(rhMapList.size() == 1) {
				Map<String, Object> rhMap = rhMapList.get(0);
				String rhUUIDT = null, reportUUIDT = null;
				Long reportIdT = null;
				
				if(CommonFunctions.isNotBlank(rhMap, "rhUUID")) {
					rhUUIDT = rhMap.get("rhUUID").toString();
				}
				
				if(CommonFunctions.isNotBlank(rhMap, "reportUUID")) {
					reportUUIDT = rhMap.get("reportUUID").toString();
				}
				
				if(CommonFunctions.isNotBlank(rhMap, "reportId")) {
					try {
						reportIdT = Long.valueOf(rhMap.get("reportId").toString());
					} catch(NumberFormatException e) {}
				}
				
				if(StringUtils.isNotBlank(rhUUIDT) && rhUUIDT.equals(rhUUID)
						|| StringUtils.isNotBlank(reportUUIDT) && reportUUIDT.equals(reportUUID)
						|| reportIdT != null && reportIdT.equals(reportId)) {
					flag = false;
				} else {
					flag = true;
				}
			} else if(rhMapList.size() > 1) {
				flag = true;
			}
		}
		
		if(flag && logger.isErrorEnabled()) {
			logger.error("该记录于辖区所有记录中有重复！");
		}
		
		return flag;
	}

}
