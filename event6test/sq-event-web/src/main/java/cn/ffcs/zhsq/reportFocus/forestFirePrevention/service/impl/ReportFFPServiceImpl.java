package cn.ffcs.zhsq.reportFocus.forestFirePrevention.service.impl;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.forestFirePrevention.ReportFFPMapper;
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
 * @Description: 森林防灭火，相关表：T_EVENT_FOREST_FIRE_PREVENTION
 * @ClassName:   ReportFFPServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年3月31日 上午10:47:41
 */
@Service("reportFFPService")
public class ReportFFPServiceImpl extends ReportTwoViolationPreServiceImpl {
	@Autowired
	private ReportFFPMapper reportFFPMapper;
	
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
	public String saveReportExtendInfo(Map<String, Object> ffpParam, UserInfo userInfo) throws Exception {
		if(ffpParam == null || ffpParam.isEmpty()) {
			throw new IllegalArgumentException("缺少需要操作的森林防灭火！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		Long userId = userInfo.getUserId();
		String ffpUUID = null;
		
		if(CommonFunctions.isBlank(ffpParam, "creator")) {
			ffpParam.put("creator", userId);
		}
		if(CommonFunctions.isBlank(ffpParam, "updator")) {
			ffpParam.put("updator", userId);
		}
		
		if(CommonFunctions.isNotBlank(ffpParam, "verifyTime")) {
			Object verifyTimeObj = ffpParam.get("verifyTime");
			Date verifyTime = null;
			
			if(verifyTimeObj instanceof String) {
				try {
					verifyTime = DateUtils.convertStringToDate(verifyTimeObj.toString(), DateUtils.PATTERN_24TIME);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if(verifyTimeObj instanceof Date) {
				verifyTime = (Date) verifyTimeObj;
			}
			
			ffpParam.put("verifyTime", verifyTime);
		} else if(CommonFunctions.isBlank(ffpParam, "verifyTime") && CommonFunctions.isNotBlank(ffpParam, "verifyTimeStr")) {
			Date verifyTime = null;
			String verifyTimeStr = ffpParam.get("verifyTimeStr").toString();
			
			try {
				verifyTime = DateUtils.convertStringToDate(verifyTimeStr, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(verifyTime != null) {
				ffpParam.put("verifyTime", verifyTime);
			}
		}
		
		if(CommonFunctions.isNotBlank(ffpParam, "ffpUUID")) {
			if(reportFFPMapper.update(ffpParam) > 0) {
				ffpUUID = ffpParam.get("ffpUUID").toString();
			}
		} else {
			if(CommonFunctions.isBlank(ffpParam, "reportUUID")) {
				throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
			}
			
			//为了保证记录的唯一性
			Map<String, Object> ffpMap = reportFFPMapper.findByIdSimple(ffpParam);
			
			if(ffpMap != null && !ffpMap.isEmpty()) {
				Map<String, Object> ffpMapAfter = new HashMap<String, Object>(),
									ffpMapAfterRemain = new HashMap<String, Object>();
				ffpMapAfter.putAll(ffpMap);
				ffpMapAfter.putAll(ffpParam);
				
				for(String key : ffpMapAfter.keySet()) {
					if(CommonFunctions.isBlank(ffpMap, key) 
							|| !ffpMapAfter.get(key).equals(ffpMap.get(key))) {
						ffpMapAfterRemain.put(key, ffpMapAfter.get(key));
					}
				}
				
				if(!ffpMapAfterRemain.isEmpty()) {
					ffpUUID = ffpMap.get("ffpUUID").toString();
					ffpMapAfterRemain.put("ffpUUID", ffpUUID);
					
					if(reportFFPMapper.update(ffpMapAfterRemain) > 0) {
						ffpUUID = ffpMap.get("ffpUUID").toString();
					}
				} else {
					ffpUUID = ffpMap.get("ffpUUID").toString();
				}
			} else if(reportFFPMapper.insert(ffpParam) > 0) {
				if(CommonFunctions.isNotBlank(ffpParam, "ffpUUID")) {
					ffpUUID = ffpParam.get("ffpUUID").toString();
				}
			}
			
			saveReportExtraInfo(ffpParam);
		}
		
		return ffpUUID;
	}

	@Override
	public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo,
			Map<String, Object> params) {
		Map<String, Object> ffpParam = new HashMap<String, Object>(), ffpParamMap = null;
		boolean isWithReportFocus = true;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
			isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
		}
		
		if(StringUtils.isNotBlank(reportUUID)) {
			ffpParam.put("reportUUID", reportUUID);
		} else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
			ffpParam.put("reportUUID", params.get("reportUUID"));
		} else if(CommonFunctions.isNotBlank(params, "ffpUUID")) {
			ffpParam.put("ffpUUID", params.get("ffpUUID"));
		} else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
			ffpParam.put("reportId", params.get("reportId"));
		}
		
		if(ffpParam.isEmpty()) {
			throw new IllegalArgumentException("缺失唯一性条件，不可查询森林防灭火！");
		}
		
		if(isWithReportFocus) {
			ffpParamMap = reportFFPMapper.findById(ffpParam);
			
			if(ffpParamMap != null && !ffpParamMap.isEmpty()) {
				List<Map<String, Object>> ffpMapList = new ArrayList<Map<String, Object>>();
				
				ffpMapList.add(ffpParamMap);
				
				if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
					params.put("userOrgCode", userInfo.getOrgCode());
				}
				
				formatDataOut(ffpMapList, params);
			}
		} else {
			ffpParamMap = reportFFPMapper.findByIdSimple(ffpParam);
		}
		
		findReportExtraInfo(ffpParamMap, params);
		
		return ffpParamMap;
	}
	
	@Override
	public String capNumberCfgBizCode() {
		return ReportNumberCfgBizCodeEnum.forestFirePrevention.getBizCode();
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

		//dataSource = "01" 时 所属区域才需要精确到网格层级或者当前环节为核实环节
		if(FFPDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource) || isEditableNode) {
			if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)){
				if(logger.isErrorEnabled()) {
					logger.error("地域编码【" + regionCode + "】不是网格层级！");
				}

				throw ZhsqEventExceptionEnum.NOT_GRID_LEVEL_REGION_EXCEPTION.getZhsqEventException();
			}
		} else {
			//来源不是 01 的，地址控件需要选择到网格层级或村社区层级
			if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel) && !ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)){
				if(logger.isErrorEnabled()) {
					logger.error("地域编码【" + regionCode + "】既不是网格层级，也不是村/社区层级！");
				}

				throw new ZhsqEventException("地域编码【" + regionCode + "】既不是网格层级，也不是村/社区层级！");
			}
		}
		
		return flag;
	}
	
	protected ReportTwoViolationPreMapper capReportExtendMapper() {
		return reportFFPMapper;
	}
	
	protected String capBizType4Attachment() {
		return "FOREST_FIRE_PREVENTION";
	}
	
	protected String capBizType4ResmarkerInfo() {
		return "FOREST_FIRE_PREVENTION";
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
				collectSourceDictList = baseDictionaryService.getDataDictListOfSinglestage(FFPDictEnum.COLLECT_SOURCE.getDictCode(), userOrgCode);
				
				if(listType == 1) {
					reportStatusDictList = new ArrayList<BaseDataDict>();
					//草稿不增添字典，因为只有草稿列表会用到
					BaseDataDict draftDict = new BaseDataDict();
					draftDict.setDictGeneralCode(FFPReportStatusEnum.DRAFT_STATUS.getReportStatus());
					draftDict.setDictName(FFPReportStatusEnum.DRAFT_STATUS.getReportStatusName());
					reportStatusDictList.add(draftDict);
				} else {
					reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(FFPDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
				}
			}
			
			for(Map<String, Object> reportMap : reportList) {
				if(CommonFunctions.isNotBlank(reportMap, "verifyTime")) {
					reportMap.put("verifyTimeStr", DateUtils.formatDate((Date) reportMap.get("verifyTime"), DateUtils.PATTERN_24TIME));
				}
				
				DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
				DataDictHelper.setDictValueForField(reportMap, "collectSource", "collectSourceName", collectSourceDictList);
			}
		}
	}

}
