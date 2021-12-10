package cn.ffcs.zhsq.reportFocus.petitionPerson.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.petitionPerson.ReportPetPerMapper;
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
@Service("reportPetPerService")
public class ReportPetPerServiceImpl extends ReportTwoViolationPreServiceImpl {

	@Autowired
	private ReportPetPerMapper reportPetPerMapper;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IResMarkerService resGisMarkerService;
	
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
				put(8, "4StatusTrack");
	        }
	    };
	}
	
	@Override
	public String saveReportExtendInfo(Map<String, Object> petitionPerson, UserInfo userInfo) throws Exception {
		if(petitionPerson == null || petitionPerson.isEmpty()) {
			throw new IllegalArgumentException("缺少需要操作的信访人员稳控！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		Long userId = userInfo.getUserId();
		String optUUID = null;
		
		if(CommonFunctions.isBlank(petitionPerson, "creator")) {
			petitionPerson.put("creator", userId);
		}
		if(CommonFunctions.isBlank(petitionPerson, "updator")) {
			petitionPerson.put("updator", userId);
		}
		
		if(CommonFunctions.isNotBlank(petitionPerson, "feedbackTime")) {
			Object feedbackTimeObj = petitionPerson.get("feedbackTime");
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
			
			petitionPerson.put("feedbackTime", feedbackTime);
		} else if(CommonFunctions.isBlank(petitionPerson, "feedbackTime") && CommonFunctions.isNotBlank(petitionPerson, "feedbackTimeStr")) {
			Date feedbackTime = null;
			String feedbackTimeStr = petitionPerson.get("feedbackTimeStr").toString();
			
			try {
				feedbackTime = DateUtils.convertStringToDate(feedbackTimeStr, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(feedbackTime != null) {
				petitionPerson.put("feedbackTime", feedbackTime);
			}
		}
		
		if(CommonFunctions.isNotBlank(petitionPerson, "endTime")) {
			Object endTimeObj = petitionPerson.get("endTime");
			Date endTime = null;
			
			if(endTimeObj instanceof String) {
				try {
					endTime = DateUtils.convertStringToDate(endTimeObj.toString(), DateUtils.PATTERN_24TIME);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if(endTimeObj instanceof Date) {
				endTime = (Date) endTimeObj;
			}
			
			petitionPerson.put("endTime", endTime);
		} else if(CommonFunctions.isBlank(petitionPerson, "endTime") && CommonFunctions.isNotBlank(petitionPerson, "endTimeStr")) {
			Date endTime = null;
			String endTimeStr = petitionPerson.get("endTimeStr").toString();
			
			try {
				endTime = DateUtils.convertStringToDate(endTimeStr, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(endTime != null) {
				petitionPerson.put("endTime", endTime);
			}
		}
		
		if(CommonFunctions.isNotBlank(petitionPerson, "optUUID")) {
			if(reportPetPerMapper.update(petitionPerson) > 0) {
				optUUID = petitionPerson.get("optUUID").toString();
			}
		} else {
			if(CommonFunctions.isBlank(petitionPerson, "reportUUID")) {
				throw new IllegalArgumentException("缺少属性上报UUID【reportUUID】！");
			}
			
			//为了保证记录的唯一性
			Map<String, Object> ffpMap = reportPetPerMapper.findByIdSimple(petitionPerson);
			
			if(ffpMap != null && !ffpMap.isEmpty()) {
				Map<String, Object> ffpMapAfter = new HashMap<String, Object>(),
									ffpMapAfterRemain = new HashMap<String, Object>();
				ffpMapAfter.putAll(ffpMap);
				ffpMapAfter.putAll(petitionPerson);
				
				for(String key : ffpMapAfter.keySet()) {
					if(CommonFunctions.isBlank(ffpMap, key) 
							|| !ffpMapAfter.get(key).equals(ffpMap.get(key))) {
						ffpMapAfterRemain.put(key, ffpMapAfter.get(key));
					}
				}
				
				if(!ffpMapAfterRemain.isEmpty()) {
					optUUID = ffpMap.get("optUUID").toString();
					ffpMapAfterRemain.put("optUUID", optUUID);
					
					if(reportPetPerMapper.update(ffpMapAfterRemain) > 0) {
						optUUID = ffpMap.get("optUUID").toString();
					}
				} else {
					optUUID = ffpMap.get("optUUID").toString();
				}
			} else if(reportPetPerMapper.insert(petitionPerson) > 0) {
				if(CommonFunctions.isNotBlank(petitionPerson, "optUUID")) {
					optUUID = petitionPerson.get("optUUID").toString();
				}
			}
			
			saveReportExtraInfo(petitionPerson);
			
			saveReportExtraInfo_ext(petitionPerson);
		}
		
		return optUUID;
	}
	
	private void saveReportExtraInfo_ext(Map<String, Object> params) {
		Long reportId = null;
		
		if(CommonFunctions.isNotBlank(params, "reportId")) {
			reportId = Long.valueOf(params.get("reportId").toString());
		}
		
		if(reportId != null && reportId > 0) {
			saveResmarkerInfo_ext(reportId, params);
		}
	}
	
	private void saveResmarkerInfo_ext(Long reportId, Map<String, Object> params) {
		boolean isSaveResMarkerInfo = false;
		
		if(CommonFunctions.isNotBlank(params, "isSaveResMarkerInfo")) {
			isSaveResMarkerInfo = Boolean.valueOf(params.get("isSaveResMarkerInfo").toString());
			if(isSaveResMarkerInfo) {
				initSaveResmarkerInfo_ext(reportId, params,capBizType4ResmarkerIResidenceAddrInfo(),"resMarker_iResidenceAddr");
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "petitionAddr")) {
			String petitionAddr = params.get("petitionAddr").toString();
			if(!cn.ffcs.common.utils.StringUtils.isBlank(petitionAddr)) {
				initSaveResmarkerInfo_ext(reportId, params,capBizType4ResmarkerPetitionAddrInfo(),"resMarker_petitionAddr");
			}
		}
	}
	
	private void initSaveResmarkerInfo_ext(Long reportId, Map<String, Object> params,String markerType,String resMarkerName) {
		String resMarkerLongitude = null, resMarkerLatitude = null, resMarkerMapType = null;
		
		if(CommonFunctions.isNotBlank(params, resMarkerName+".x")) {
			resMarkerLongitude = params.get(resMarkerName+".x").toString();
		} else if(CommonFunctions.isNotBlank(params, resMarkerName+".longitude")) {
			resMarkerLongitude = params.get(resMarkerName+".longitude").toString();
		}
		if(CommonFunctions.isNotBlank(params, resMarkerName+".y")) {
			resMarkerLatitude = params.get(resMarkerName+".y").toString();
		} else if(CommonFunctions.isNotBlank(params, resMarkerName+".latitude")) {
			resMarkerLatitude = params.get(resMarkerName+".latitude").toString();
		}
		if(CommonFunctions.isNotBlank(params, resMarkerName+".mapType")) {
			resMarkerMapType = params.get(resMarkerName+".mapType").toString();
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
			resMarker.setMarkerType(markerType);
			
			resGisMarkerService.saveOrUpdateResMarker(resMarker);
		}
	}
	
	
	@Override
	public Map<String, Object> findReportExtendInfoByReportUUID(String reportUUID, UserInfo userInfo,
			Map<String, Object> params) {
		Map<String, Object> petitionPerson = new HashMap<String, Object>(), petitionPersonMap = null;
		boolean isWithReportFocus = true;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "isWithReportFocus")) {
			isWithReportFocus = Boolean.valueOf(params.get("isWithReportFocus").toString());
		}
		
		if(StringUtils.isNotBlank(reportUUID)) {
			petitionPerson.put("reportUUID", reportUUID);
		} else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
			petitionPerson.put("reportUUID", params.get("reportUUID"));
		} else if(CommonFunctions.isNotBlank(params, "optUUID")) {
			petitionPerson.put("optUUID", params.get("optUUID"));
		} else if(isWithReportFocus && CommonFunctions.isNotBlank(params, "reportId")) {
			petitionPerson.put("reportId", params.get("reportId"));
		}
		
		if(petitionPerson.isEmpty()) {
			throw new IllegalArgumentException("缺失唯一性条件，不可查询信访人员稳控！");
		}
		
		if(isWithReportFocus) {
			petitionPersonMap = reportPetPerMapper.findById(petitionPerson);
			
			if(petitionPersonMap != null && !petitionPersonMap.isEmpty()) {
				List<Map<String, Object>> ffpMapList = new ArrayList<Map<String, Object>>();
				
				ffpMapList.add(petitionPersonMap);
				
				if(CommonFunctions.isBlank(params, "userOrgCode") && userInfo != null) {
					params.put("userOrgCode", userInfo.getOrgCode());
				}
				
				formatDataOut(ffpMapList, params);
			}
		} else {
			petitionPersonMap = reportPetPerMapper.findByIdSimple(petitionPerson);
		}
		
		findReportExtraInfo(petitionPersonMap, params);
		findReportExtraInfo_ext(petitionPersonMap, params);
		
		return petitionPersonMap;
	}
	
	private void findReportExtraInfo_ext(Map<String, Object> twoVioPreMap, Map<String, Object> params) {
		
		Long reportId = null;
		
		if(CommonFunctions.isNotBlank(twoVioPreMap, "reportId")) {
			reportId = Long.valueOf(twoVioPreMap.get("reportId").toString());
		}
		
		if(reportId != null && reportId > 0) {
			findResmarkerInfo_ext(reportId, twoVioPreMap, params);
		}
	}
	
	/**
	 * 获取两违地图定位信息
	 * @param reportId		上报id
	 * @param twoVioPreMap	两违信息
	 * @param params	额外参数
	 * 			resMarker.mapType	地图类型，默认为5			
	 */
	private void findResmarkerInfo_ext(Long reportId, Map<String, Object> twoVioPreMap, Map<String, Object> params) {
		boolean isCapResMarkerInfo = false;
		
		if(CommonFunctions.isNotBlank(params, "isCapResMarkerInfo")) {
			isCapResMarkerInfo = Boolean.valueOf(params.get("isCapResMarkerInfo").toString());
		}
		
		if(isCapResMarkerInfo) {
			String resMarkerMapType = "5";//默认使用二维地图
			ResMarker resMarker = null;
			
			if(CommonFunctions.isNotBlank(params, "resMarker_iResidenceAddr.mapType")) {
				resMarkerMapType = params.get("resMarker_iResidenceAddr.mapType").toString();
			}
			
			resMarker = resGisMarkerService.findResMarkerByResId(capBizType4ResmarkerIResidenceAddrInfo(), reportId, resMarkerMapType);
			
			if(resMarker != null) {
				twoVioPreMap.put("resMarker_iResidenceAddr", resMarker);
				twoVioPreMap.put("resMarker_iResidenceAddr.x", resMarker.getX());
				twoVioPreMap.put("resMarker_iResidenceAddr.y", resMarker.getY());
				twoVioPreMap.put("resMarker_iResidenceAddr.mapType", resMarker.getMapType());
			}
			
			resMarker = null;
			resMarkerMapType = "5";//默认使用二维地图
			if(CommonFunctions.isNotBlank(params, "resMarker_petitionAddr.mapType")) {
				resMarkerMapType = params.get("resMarker_petitionAddr.mapType").toString();
			}
			
			resMarker = resGisMarkerService.findResMarkerByResId(capBizType4ResmarkerPetitionAddrInfo(), reportId, resMarkerMapType);
			
			if(resMarker != null) {
				twoVioPreMap.put("resMarker_petitionAddr", resMarker);
				twoVioPreMap.put("resMarker_petitionAddr.x", resMarker.getX());
				twoVioPreMap.put("resMarker_petitionAddr.y", resMarker.getY());
				twoVioPreMap.put("resMarker_petitionAddr.mapType", resMarker.getMapType());
			}
		}
	}
	
	@Override
	public String capNumberCfgBizCode() {
		return ReportNumberCfgBizCodeEnum.petitionPerson.getBizCode();
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
		if(PetPerDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource) || isEditableNode) {
			if(!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)){
				if(logger.isErrorEnabled()) {
					logger.error("地域编码【" + regionCode + "】不是网格层级！");
				}

				throw ZhsqEventExceptionEnum.NOT_GRID_LEVEL_REGION_EXCEPTION.getZhsqEventException();
			}
		} else if(PetPerDataSourceEnum.PUBLIC_SECURITY.getDataSource().equals(dataSource)
				||PetPerDataSourceEnum.OFFICE_SPECIALIST.getDataSource().equals(dataSource)){
			//来源是 02.03 的，地址控件需要选择到街镇层级
			if(!ConstantValue.STREET_ORG_LEVEL.equals(regionChiefLevel)
					&&!ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)
					&&!ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)){
				if(logger.isErrorEnabled()) {
					logger.error("地域编码【" + regionCode + "】至少需要街镇层级！");
				}

				throw new ZhsqEventException("地域编码【" + regionCode + "】至少需要街镇层级！");
			}
		}
		
		
		return flag;
	}
	
	protected ReportTwoViolationPreMapper capReportExtendMapper() {
		return reportPetPerMapper;
	}
	
	protected String capBizType4Attachment() {
		return "PETITION_PERSON";
	}
	
	protected String capBizType4ResmarkerInfo() {
		return "PETITION_PERSON";
	}
	
	protected String capBizType4ResmarkerIResidenceAddrInfo() {
		return "PETITION_PERSON_IRESIDENCEADDR";
	}
	
	protected String capBizType4ResmarkerPetitionAddrInfo() {
		return "PETITION_PERSON_PETITIONADDR";
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
					controlTypeDictList = null,	petitionpartyTypeDictList = null,
					controlResultDictList = null,feedbackResultDictList = null,
					certTypeDictList = null,partyTypeDictList = null,
					petitionTypeDictList = null;
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
				
				controlTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(PetPerDictEnum.CONTROL_TYPE.getDictCode(), userOrgCode);
				petitionpartyTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(PetPerDictEnum.PETITIONPARTY_TYPE.getDictCode(), userOrgCode);
				controlResultDictList = baseDictionaryService.getDataDictListOfSinglestage(PetPerDictEnum.CONTROL_RESULT.getDictCode(), userOrgCode);
				feedbackResultDictList = baseDictionaryService.getDataDictListOfSinglestage(PetPerDictEnum.FEEDBACK_RESULT.getDictCode(), userOrgCode);
				certTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(PetPerDictEnum.CERT_TYPE.getDictCode(), userOrgCode);
				partyTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(PetPerDictEnum.PARTY_TYPE.getDictCode(), userOrgCode);
				petitionTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(PetPerDictEnum.PETITION_TYPE.getDictCode(), userOrgCode);
				
				if(listType == 1) {
					reportStatusDictList = new ArrayList<BaseDataDict>();
					//草稿不增添字典，因为只有草稿列表会用到
					BaseDataDict draftDict = new BaseDataDict();
					draftDict.setDictGeneralCode(PetPerReportStatusEnum.DRAFT_STATUS.getReportStatus());
					draftDict.setDictName(PetPerReportStatusEnum.DRAFT_STATUS.getReportStatusName());
					reportStatusDictList.add(draftDict);
				} else {
					reportStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(PetPerDictEnum.REPORT_STATUS.getDictCode(), userOrgCode);
				}
			}
			
			for(Map<String, Object> reportMap : reportList) {
				if(CommonFunctions.isNotBlank(reportMap, "feedbackTime")) {
					reportMap.put("feedbackTimeStr", DateUtils.formatDate((Date) reportMap.get("feedbackTime"), DateUtils.PATTERN_DATE));
				}
				if(CommonFunctions.isNotBlank(reportMap, "endTime")) {
					reportMap.put("endTimeStr", DateUtils.formatDate((Date) reportMap.get("endTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(reportMap, "isRightOrg")) {
					String isRightOrg = cn.ffcs.common.utils.StringUtils.toString(reportMap.get("isRightOrg"));
					if("1".equals(isRightOrg)) {
						reportMap.put("isRightOrgStr", "是");
					}else if("0".equals(isRightOrg)) {
						reportMap.put("isRightOrgStr", "否");
					}
				}
				
				DataDictHelper.setDictValueForField(reportMap, "reportStatus", "reportStatusName", reportStatusDictList);
				DataDictHelper.setDictValueForField(reportMap, "petitionPartyType", "petitionPartyTypeName", petitionpartyTypeDictList);
				DataDictHelper.setDictValueForField(reportMap, "controlResult", "controlResultName", controlResultDictList);
				DataDictHelper.setDictValueForField(reportMap, "controlType", "controlTypeName", controlTypeDictList);
				DataDictHelper.setDictValueForField(reportMap, "feedbackResult", "feedbackResultName", feedbackResultDictList);
				DataDictHelper.setDictValueForField(reportMap, "certType", "certTypeName", certTypeDictList);
				DataDictHelper.setDictValueForField(reportMap, "partyType", "partyTypeName", partyTypeDictList);
				DataDictHelper.setDictValueForField(reportMap, "petitionTypes", "petitionTypesName", petitionTypeDictList);
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
		
		total = reportPetPerMapper.findCount4StatusTrack(params);
		
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
		
		total = reportPetPerMapper.findCount4StatusTrack(params);
		
		if(total > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 10 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			reportList = reportPetPerMapper.findList4StatusTrack(params, rowBounds);
			
			formatDataOut(reportList, params);
		} else {
			reportList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination reportPagination = new EUDGPagination(total, reportList);
		
		return reportPagination;
	}
	
}
