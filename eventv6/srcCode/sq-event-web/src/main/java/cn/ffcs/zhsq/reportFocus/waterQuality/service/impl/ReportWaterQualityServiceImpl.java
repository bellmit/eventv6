package cn.ffcs.zhsq.reportFocus.waterQuality.service.impl;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.twoViolationPre.ReportTwoViolationPreMapper;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.waterQuality.ReportWaterQualityMapper;
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
 * @Description: 流域水质问题，相关涉及表：T_EVENT_WATER_QUALITY
 * @ClassName:   ReportWaterQualityServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月21日 下午4:31:37
 */
@Service("reportWaterQualityService")
public class ReportWaterQualityServiceImpl extends ReportTwoViolationPreServiceImpl {
	@Autowired
	private ReportWaterQualityMapper reportWaterQualityMapper;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
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
	public String saveReportExtendInfo(Map<String, Object> wqParam, UserInfo userInfo) {
		if(wqParam == null || wqParam.isEmpty()) {
			throw new IllegalArgumentException("缺少需要操作的流域水质信息！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		Long userId = userInfo.getUserId();
		String wqUUID = null;
		
		if(CommonFunctions.isBlank(wqParam, "creator")) {
			wqParam.put("creator", userId);
		}
		if(CommonFunctions.isBlank(wqParam, "updator")) {
			wqParam.put("updator", userId);
		}
		if(CommonFunctions.isNotBlank(wqParam, "administrativeSactionType")) {
			String administrativeSactionType = wqParam.get("administrativeSactionType").toString();
			
			if(!administrativeSactionType.startsWith(",")) {
				administrativeSactionType = "," + administrativeSactionType;
			}
			
			if(!administrativeSactionType.endsWith(",")) {
				administrativeSactionType += ",";
			}
			
			wqParam.put("administrativeSactionType", administrativeSactionType);
		}
		if(CommonFunctions.isNotBlank(wqParam, "feedbackTime")) {
			Object feedbackTimeObj = wqParam.get("feedbackTime");
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
			
			wqParam.put("feedbackTime", feedbackTime);
		} else if(CommonFunctions.isBlank(wqParam, "feedbackTime") && CommonFunctions.isNotBlank(wqParam, "feedbackTimeStr")) {
			Date feedbackTime = null;
			String feedbackTimeStr = wqParam.get("feedbackTimeStr").toString();
			
			try {
				feedbackTime = DateUtils.convertStringToDate(feedbackTimeStr, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(feedbackTime != null) {
				wqParam.put("feedbackTime", feedbackTime);
			}
		}
		
		if(CommonFunctions.isNotBlank(wqParam, "wqUUID")) {
			if(reportWaterQualityMapper.update(wqParam) > 0) {
				wqUUID = wqParam.get("wqUUID").toString();
			}
		} else {
			if(CommonFunctions.isBlank(wqParam, "reportUUID")) {
				throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
			}
			
			//为了保证记录的唯一性
			Map<String, Object> wqMap = reportWaterQualityMapper.findByIdSimple(wqParam);
			
			if(wqMap != null && !wqMap.isEmpty()) {
				Map<String, Object> wqMapAfter = new HashMap<String, Object>(),
									wqMapAfterRemain = new HashMap<String, Object>();
				wqMapAfter.putAll(wqMap);
				wqMapAfter.putAll(wqParam);
				
				for(String key : wqMapAfter.keySet()) {
					if(CommonFunctions.isBlank(wqMap, key) 
							|| !wqMapAfter.get(key).equals(wqMap.get(key))) {
						wqMapAfterRemain.put(key, wqMapAfter.get(key));
					}
				}
				
				if(!wqMapAfterRemain.isEmpty()) {
					wqUUID = wqMap.get("wqUUID").toString();
					wqMapAfterRemain.put("wqUUID", wqUUID);
					
					if(reportWaterQualityMapper.update(wqMapAfterRemain) > 0) {
						wqUUID = wqMap.get("wqUUID").toString();
					}
				} else {
					wqUUID = wqMap.get("wqUUID").toString();
				}
			} else if(reportWaterQualityMapper.insert(wqParam) > 0) {
				if(CommonFunctions.isNotBlank(wqParam, "wqUUID")) {
					wqUUID = wqParam.get("wqUUID").toString();
				}
			}
			
			saveReportExtraInfo(wqParam);
		}
		
		return wqUUID;
	}

	@Override
	public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo,
			Map<String, Object> params) {
		Map<String, Object> wqParam = new HashMap<String, Object>(), wqParamMap = null;
		boolean isWithReportFocus = true;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
			isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
		}
		
		if(StringUtils.isNotBlank(reportUUID)) {
			wqParam.put("reportUUID", reportUUID);
		} else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
			wqParam.put("reportUUID", params.get("reportUUID"));
		} else if(CommonFunctions.isNotBlank(params, "wqUUID")) {
			wqParam.put("wqUUID", params.get("wqUUID"));
		} else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
			wqParam.put("reportId", params.get("reportId"));
		}
		
		if(wqParam.isEmpty()) {
			throw new IllegalArgumentException("缺失唯一性条件，不可查询流域水质信息！");
		}
		
		if(isWithReportFocus) {
			wqParamMap = reportWaterQualityMapper.findById(wqParam);
			
			if(wqParamMap != null && !wqParamMap.isEmpty()) {
				List<Map<String, Object>> wqMapList = new ArrayList<Map<String, Object>>();
				
				wqMapList.add(wqParamMap);
				
				if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
					params.put("userOrgCode", userInfo.getOrgCode());
				}
				
				formatDataOut(wqMapList, params);
			}
		} else {
			wqParamMap = reportWaterQualityMapper.findByIdSimple(wqParam);
		}
		
		findReportExtraInfo(wqParamMap, params);
		
		return wqParamMap;
	}
	
	@Override
	public String capNumberCfgBizCode() {
		return ReportNumberCfgBizCodeEnum.waterQuality.getBizCode();
	}
	
	protected ReportTwoViolationPreMapper capReportExtendMapper() {
		return reportWaterQualityMapper;
	}
	
	protected String capBizType4Attachment() {
		return "WATER_QUALITY";
	}
	
	protected String capBizType4ResmarkerInfo() {
		return "WATER_QUALITY";
	}
	
	/**
	 * 格式化输入参数
	 * @param params
	 * @throws ZhsqEventException 
	 */
	protected void formatParamIn(Map<String, Object> params) throws ZhsqEventException {
		super.formatParamIn(params);
		
		if(CommonFunctions.isNotBlank(params, "administrativeSactionType")) {
			String administrativeSactionType = params.get("administrativeSactionType").toString();
			
			if(!administrativeSactionType.startsWith(",")) {
				administrativeSactionType = "," + administrativeSactionType;
			}
			
			if(!administrativeSactionType.endsWith(",")) {
				administrativeSactionType += ",";
			}
			
			params.put("administrativeSactionType", administrativeSactionType);
		}
	}
	
	/**
	 * 数据格式化输出
	 * @param reportList
	 * @param params
	 */
	protected void formatDataOut(List<Map<String, Object>> reportList, Map<String, Object> params) {
		if(reportList != null && reportList.size() > 0) {
			String userOrgCode = null, adsTypeStr = null, ADS_TYPE_OTHER = "99";
			boolean isDictTransfer = true;
			List<BaseDataDict> reportStatusDictList = null,
							   matterTypeDictList = null,
							   adsTypeDictList = null;
			int listType = 0;
			StringBuffer adsTypeBuffer = null;
			
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
				matterTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(WaterQualityDictEnum.MATTER_TYPE.getDictCode(), userOrgCode);
				adsTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(WaterQualityDictEnum.ADMINISTRTIVE_SACTION.getDictCode(), userOrgCode);
				
				if(listType == 1) {
					reportStatusDictList = new ArrayList<BaseDataDict>();
					//草稿不增添字典，因为只有草稿列表会用到
					BaseDataDict draftDict = new BaseDataDict();
					draftDict.setDictGeneralCode(WaterQualityReportStatusEnum.DRAFT_STATUS.getReportStatus());
					draftDict.setDictName(WaterQualityReportStatusEnum.DRAFT_STATUS.getReportStatusName());
					reportStatusDictList.add(draftDict);
				} else {
					reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(WaterQualityDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
				}
			}
			
			for(Map<String, Object> reportMap : reportList) {
				if(CommonFunctions.isNotBlank(reportMap, "feedbackTime")) {
					reportMap.put("feedbackTimeStr", DateUtils.formatDate((Date) reportMap.get("feedbackTime"), DateUtils.PATTERN_24TIME));
				}
				
				if(CommonFunctions.isNotBlank(reportMap, "administrativeSactionType")) {
					adsTypeBuffer = new StringBuffer("");
					adsTypeStr = reportMap.get("administrativeSactionType").toString();
					
					for(String asdType : adsTypeStr.split(",")) {
						if(StringUtils.isNotBlank(asdType)) {
							if(ADS_TYPE_OTHER.equals(asdType)) {
								if(CommonFunctions.isNotBlank(reportMap, "administrativeSactionDes")) {
									adsTypeBuffer.append("、").append(reportMap.get("administrativeSactionDes"));
								}
							} else if(adsTypeDictList != null) {
								for(BaseDataDict adsTypeDict : adsTypeDictList) {
									if(asdType.equals(adsTypeDict.getDictGeneralCode())) {
										adsTypeBuffer.append("、").append(adsTypeDict.getDictName());
										break;
									}
								}
							}
						}
					}
					
					if(adsTypeStr.startsWith(",")) {
						adsTypeStr = adsTypeStr.substring(1);
					}
					
					if(adsTypeStr.endsWith(",")) {
						adsTypeStr = adsTypeStr.substring(0, adsTypeStr.length() - 1);
					}
					
					reportMap.put("administrativeSactionType", adsTypeStr);
					
					if(adsTypeBuffer.length() > 0) {
						reportMap.put("administrativeSactionTypeName", adsTypeBuffer.substring(1));
					}
				}
				
				DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
				DataDictHelper.setDictValueForField(reportMap, "matterType", "matterTypeName", matterTypeDictList);
			}
		}
	}
	
}
