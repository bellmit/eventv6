package cn.ffcs.zhsq.relatedEvents.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.common.validator.validation.CommonValidator;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PlaceInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.legal.ResultData;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.util.FieldMaskUtils;
import cn.ffcs.shequ.zzgl.service.grid.ICampusInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IPlaceInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.INumberConfigureService;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEvents;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEventsForSearch;
import cn.ffcs.zhsq.mybatis.persistence.relatedEvents.RelatedEventsMapper;
import cn.ffcs.zhsq.relatedEvents.service.ICommonRelatedEventsService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;

@Service(value="commonRelatedEventsServiceImpl")
public class CommonRelatedEventsServiceImpl implements ICommonRelatedEventsService {

	@Autowired
	private RelatedEventsMapper relatedEventsMapper;
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IPlaceInfoService placeInfoservice;
	@Autowired
	private IBaseDictionaryService dictionaryService;
	
	@Autowired
	private INumberConfigureService numberConfigureService;

	@Autowired
	private ICampusInfoService campusInfoService;
	
	@Autowired
	private IResMarkerService resMarkerService;
	
	private String IS_DETECTION_Y = "1";//???????????? ???
	
	@Override
	public Long insertRelatedEvents(RelatedEvents relatedEvents, String infoOrgCode) throws Exception {
		int result = 0;
		
		formateRelatedEventsIn(relatedEvents);
		
		String reNo = "";
		//?????????(???)?????????
		if ("2".equals(relatedEvents.getBizType())) {//??????????????????
			reNo = numberConfigureService.getNumber(infoOrgCode, "03");
		} else if ("3".equals(relatedEvents.getBizType())) {//???????????????
			reNo = numberConfigureService.getNumber(infoOrgCode, "04");
		} else {//??????
			reNo = numberConfigureService.getNumber(infoOrgCode, "99");
		}
		if (StringUtils.isBlank(reNo)){
			throw new Exception("?????????????????????");
		}

		relatedEvents.setReNo(reNo);
		result = relatedEventsMapper.insert(relatedEvents);
		if (result > 0) {
			ResMarker resMarker = relatedEvents.getResMarker();
			if (resMarker != null && !StringUtils.isBlank(resMarker.getMapType())
					&& !StringUtils.isBlank(resMarker.getX()) && !StringUtils.isBlank(resMarker.getY())) {
				resMarker.setResourcesId(relatedEvents.getReId());
				resMarker.setMarkerType(ConstantValue.MAP_TYPE_SAFE_EVENT);
				resMarkerService.saveOrUpdateResMarker(resMarker);
			}
		}
		return result>0 ? relatedEvents.getReId():-1L;
	}
	
	@Override
	public boolean updateRelatedEvents(RelatedEvents relatedEvents) {
		int result = 0;
		
		formateRelatedEventsIn(relatedEvents);
		
		result = relatedEventsMapper.update(relatedEvents);
		if (result > 0) {
			ResMarker resMarker = relatedEvents.getResMarker();
			if (resMarker != null && !StringUtils.isBlank(resMarker.getMapType())
					&& !StringUtils.isBlank(resMarker.getX()) && !StringUtils.isBlank(resMarker.getY())) {
				resMarker.setResourcesId(relatedEvents.getReId());
				resMarker.setMarkerType(ConstantValue.MAP_TYPE_SAFE_EVENT);
				resMarkerService.saveOrUpdateResMarker(resMarker);
			}
		}
		return result > 0;
	}
	
	@Override
	public boolean deleteRelatedEventsById(Long reId) {
		int result = 0;
		if(reId != null){
			result = relatedEventsMapper.delete(reId);
		}
		return result > 0;
	}
	@Override
	public int deleteRelatedEventsByIds(List<Long> reIdList) {
		int result = 0;
		if(reIdList!=null && reIdList.size()>0){
			result = relatedEventsMapper.deleteByIds(reIdList);
		}
		return result;
	}
	public boolean delRelatedEventsByIdAndGirdCode(Long reId,String gridCode,String bizType) {
		Map<String,Object> params = new HashMap<>();
		params.put("reId",reId);
		params.put("gridCode",gridCode);
		params.put("bizType",bizType);
		int result = 0;
		if(reId != null){
			result = relatedEventsMapper.deleteByIdAndGridCode(params);
		}
		return result > 0;
	}
	public int delRelatedEventsByIdsAndGridCode(List<Long> reIdList,String gridCode,String bizType) {
		Map<String,Object> params = new HashMap<>();
		params.put("reIdList",reIdList);
		params.put("gridCode",gridCode);
		params.put("bizType",bizType);
		int result = 0;
		if(reIdList!=null && reIdList.size()>0){
			result = relatedEventsMapper.deleteByIdsAndGridCode(params);
		}
		return result;
	}
	@Override
	public int deleteRelatedEventsByBiz(Long bizId, String bizType) {
		int result = 0;
		if(bizId!=null && !bizId.equals(-1L) && !StringUtils.isBlank(bizType)){
			result = relatedEventsMapper.deleteByBiz(bizId, bizType);
		}
		return result;
	}
	
	@Override
	public Long findRelatedEventsCount(Map<String, Object> params) {
		
		Object relatedEventsObj = params.get("relatedEvents");
		if(relatedEventsObj!=null && relatedEventsObj instanceof RelatedEvents){
			RelatedEvents relatedEvents = (RelatedEvents)relatedEventsObj;
			params.putAll(addExtParams(relatedEvents));
		}
		
		int count = relatedEventsMapper.findCountByCriteria(params);
		return Long.valueOf(count);
	}

	@Override
	public EUDGPagination findRelatedEventsPagination(int pageNo,
			int pageSize, Map<String, Object> params, String orgCode) {
		pageNo = pageNo<1 ? 1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		RelatedEventsForSearch relatedEventsForSearch = null;
		Object relatedEventsObj = params.get("relatedEventsForSearch");
		if(relatedEventsObj!=null && relatedEventsObj instanceof RelatedEventsForSearch){
			relatedEventsForSearch = (RelatedEventsForSearch)relatedEventsObj;
			params.putAll(addExtParams(relatedEventsForSearch));
		}
		String bizType = "2";
		if (relatedEventsForSearch != null) {
			bizType = relatedEventsForSearch.getBizType();
		}
		if ("2".equals(bizType)) {
			int count = relatedEventsMapper.findCountForSchoolRE(params);
			List<RelatedEvents> list = relatedEventsMapper.findPageListForSchoolRE(params, rowBounds);
			formateRelatedEventsOut(list, orgCode);
			EUDGPagination eudgPagination = new EUDGPagination(count, list);
			return eudgPagination;
		}
		int count = relatedEventsMapper.findCountForCommonRE(params);
		List<RelatedEvents> list = relatedEventsMapper.findPageListForCommonRE(params, rowBounds);
		formateRelatedEventsOut(list, orgCode);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}

	@Override
	public Long findRelatedEventsCountByCriteria(Map<String, Object> params) {

		Object relatedEventsObj = params.get("relatedEvents");
		if(relatedEventsObj!=null && relatedEventsObj instanceof RelatedEvents){
			RelatedEvents relatedEvents = (RelatedEvents)relatedEventsObj;
			params.putAll(addExtParams(relatedEvents));
		}
		
		int count = relatedEventsMapper.findRelatedEventsCountByCriteria(params);
		return Long.valueOf(count);
	}

	@Override
	public EUDGPagination findRelatedEventsPageListByCriteria(int pageNo,
			int pageSize, Map<String, Object> params, String orgCode) {
		pageNo = pageNo<1 ? 1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		
		Object relatedEventsObj = params.get("relatedEvents");
		if(relatedEventsObj!=null && relatedEventsObj instanceof RelatedEvents){
			RelatedEvents relatedEvents = (RelatedEvents)relatedEventsObj;
			params.putAll(addExtParams(relatedEvents));
		}
		
		int count = relatedEventsMapper.findRelatedEventsCountByCriteria(params);
		List<RelatedEvents> list = relatedEventsMapper.findRelatedEventsPageListByCriteria(params, rowBounds);
		formateRelatedEventsOut(list, orgCode);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}
	
	@Override
	public RelatedEvents findRelatedEventsById(Long reId, String orgCode) {
		RelatedEvents relatedEvents = null;
		if(reId != null){
			relatedEvents = relatedEventsMapper.findById(reId);
			List<RelatedEvents> list = new ArrayList<RelatedEvents>();
			list.add(relatedEvents);
			formateRelatedEventsOut(list, orgCode);
			 FieldMaskUtils.translateConcealment("36", list,null);
			
		}
		return relatedEvents;
	}

	/**
	 * ????????????????????????
	 * @param relatedEvents
	 * @return
	 */
	private Map<String, Object> addExtParams(RelatedEvents relatedEvents){
		Map<String, Object> params = new HashMap<String, Object>();
		if(relatedEvents != null){
			String bizType = relatedEvents.getBizType();
			Long bizId = relatedEvents.getBizId();
			String reNo = relatedEvents.getReNo();
			String isDetection = relatedEvents.getIsDetection();
			
			if(StringUtils.isNotBlank(bizType)){
				params.put("bizType", bizType);
			}
			
			if(bizId != null){
				params.put("bizId", bizId);
			}
			
			if(StringUtils.isNotBlank(reNo)){
				params.put("reNo", reNo);
			}
			
			if(StringUtils.isNotBlank(isDetection)){
				params.put("isDetection", isDetection);
			}
			
			if(StringUtils.isNotBlank(relatedEvents.getEventType())){
				params.put("eventType", relatedEvents.getEventType());
			}
			
			if(StringUtils.isNotBlank(relatedEvents.getEventLevel())){
				params.put("eventLevel", relatedEvents.getEventLevel());
			}
		}
		return params;
	}
	
	private Map<String, Object> addExtParams(RelatedEventsForSearch relatedEventsForSearch){
		Map<String, Object> params = new HashMap<String, Object>();
		if(relatedEventsForSearch != null){
			String gridCode = relatedEventsForSearch.getGridCode();
			Long gridId = relatedEventsForSearch.getGridId();
			if (StringUtils.isNotBlank(relatedEventsForSearch.getOccuDateStartStr())) {
				params.put("occuDateStartStr", relatedEventsForSearch.getOccuDateStartStr());
			}
			if (StringUtils.isNotBlank(relatedEventsForSearch.getOccuDateEndStr())) {
				params.put("occuDateEndStr", relatedEventsForSearch.getOccuDateEndStr());
			}
			params.putAll(addExtParams((RelatedEvents)relatedEventsForSearch));
			
			if(gridId!=null && gridId>0){//????????????gridId
				params.put("gridId", gridId);
			}else if(StringUtils.isNotBlank(gridCode)){
				params.put("gridCode", gridCode);
			}
			String nature = relatedEventsForSearch.getNature();
			if (StringUtils.isNotBlank(nature)) {
				params.put("nature", nature);
			}
		}
		return params;
	}
	
	/**
	 * ??????????????? ??????
	 * @param relatedEvents
	 */
	private void formateRelatedEventsIn(RelatedEvents relatedEvents){
		if(!StringUtils.isBlank(relatedEvents.getOccuDateStr())){
			Date occuDate = null;
			try {
				occuDate = DateUtils.convertStringToDate(relatedEvents.getOccuDateStr(), DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				occuDate = null;
				e.printStackTrace();
			}
			if(occuDate != null){
				relatedEvents.setOccuDate(occuDate);
			}
		}
		if (StringUtils.isBlank(relatedEvents.getIsDetection())) {
			relatedEvents.setIsDetection("0");
		}
	}
	
	/**
	 * ??????????????? ??????
	 * @param relatedEvents
	 */
	private void formateRelatedEventsOut(RelatedEvents relatedEvents, String orgCode) {
		if(relatedEvents != null) {
			//????????????
			if(!StringUtils.isBlank(relatedEvents.getIsDetection())) {
				relatedEvents.setIsDetectionName(IS_DETECTION_Y.equals(relatedEvents.getIsDetection())?"???":"???");
			} else {
				relatedEvents.setIsDetectionName("???");
			}
			
			//????????????
			if(relatedEvents.getOccuDate() != null){
				relatedEvents.setOccuDateStr(DateUtils.formatDate(relatedEvents.getOccuDate(), DateUtils.PATTERN_24TIME));
			}
			
			//????????????
			if(relatedEvents.getCreatedTime() != null){
				relatedEvents.setCreatedTimeStr(DateUtils.formatDate(relatedEvents.getCreatedTime(), DateUtils.PATTERN_24TIME));
			}
			
			//????????????
			if(relatedEvents.getUpdateTime() != null){
				relatedEvents.setCreatedTimeStr(DateUtils.formatDate(relatedEvents.getUpdateTime(), DateUtils.PATTERN_24TIME));
			}
		}
	}
	
	/**
	 * ??????????????? ??????
	 * @param relatedEventsList
	 */
	private void formateRelatedEventsOut(List<RelatedEvents> relatedEventsList, String orgCode) {
		List<Long> ids = new ArrayList<Long>();
		if(relatedEventsList!=null && relatedEventsList.size()>0){
			for(RelatedEvents re : relatedEventsList) {
				if(null != re){
					formateRelatedEventsOut(re, orgCode);
					if ("2".equals(re.getBizType())) {
						if (re.getBizId() != null) {
							ids.add(re.getBizId());
						}
					}
					if (!StringUtils.isBlank(re.getPrisonersDocType())) {//????????????
						re.setPrisonersDocTypeName(dictionaryService.changeCodeToName(ConstantValue.PRISONERS_DOC_TYPE, re.getPrisonersDocType(), orgCode));
					}

					if (!StringUtils.isBlank(re.getNature())) {//????????????
						re.setNatureName(dictionaryService.changeCodeToName(ConstantValue.NATURE, re.getNature(), orgCode));
					}

					if (!StringUtils.isBlank(re.getEventType())) {// ????????????	
						re.setEventTypeCN(dictionaryService.changeCodeToName(ConstantValue.EVENT_TYPE_DC, re.getEventType(), orgCode));
					}

					if (!StringUtils.isBlank(re.getEventLevel())) {// ????????????
						re.setEventLevelCN(dictionaryService.changeCodeToName(ConstantValue.EVENT_LEVEL_DC, re.getEventLevel(), orgCode));
					}
					
				}
			}
		}
		if (ids.size() > 0) {
			List<PlaceInfo> placeInfos = campusInfoService.findByIds(ids.toArray(new Long[ids.size()]));
			for (RelatedEvents relatedEvents : relatedEventsList) {
				if (relatedEvents.getBizId() == null) continue;
				for (PlaceInfo placeInfo : placeInfos) {
					if (placeInfo.getPlaId() == null) continue;
					if (relatedEvents.getBizId().intValue() == placeInfo.getPlaId().intValue()) {
						relatedEvents.setBizName(placeInfo.getPlaName());
						break;
					}
				}
			}
		}
	}

	@Override
	public EUDGPagination findRelatedEventsForGis(int pageNo, int pageSize, Map<String, Object> params,
			String orgCode) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = relatedEventsMapper.findRelatedEventsForGisOfCount(params);
		List<Map<String, Object>> list = relatedEventsMapper.findRelatedEventsForGisOfPage(params, rowBounds);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}

	@Override
	public EUDGPagination findSchoolEventsForGis(int pageNo, int pageSize, Map<String, Object> params, String orgCode) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = relatedEventsMapper.findSchoolEventsForGisOfCount(params);
		List<Map<String, Object>> list = relatedEventsMapper.findSchoolEventsForGisOfPage(params, rowBounds);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}

	@Override
	public EUDGPagination findRoadEventsForGis(int pageNo, int pageSize, Map<String, Object> params, String orgCode) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = relatedEventsMapper.findRoadEventsForGisOfCount(params);
		List<Map<String, Object>> list = relatedEventsMapper.findRoadEventsForGisOfPage(params, rowBounds);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}
	/*
	 * 9+X???????????????????????????????????????
	 */
	// ????????????
		public ResultData save(RelatedEvents relatedEvents, String orgCode) {
			ResultData resultData = new ResultData();
			// 1.?????????????????????????????????
			try {
				// 1.1 ????????????????????????
				CommonValidator.validate(relatedEvents);
				/*
				 * if(record.getCareGroupMembers()!=null &&
				 * record.getCareGroupMembers().size()>0){ for (CareGroupMember
				 * careGroupMember:record.getCareGroupMembers()) {
				 * CommonValidator.validate(careGroupMember); } }
				 */
			
				if (orgCode == null || "".equals(orgCode)) {
					resultData.setResultMsg("????????????????????????????????????????????????????????????" + orgCode);
					return resultData;
				}
				if (!"3".equals(relatedEvents.getBizType())) {
					resultData.setResultMsg("??????????????????????????????!");
					return resultData;
				}
				if (relatedEvents.getReName() == null
						|| "".equals(relatedEvents.getReName())) {
					resultData.setResultMsg("????????????????????????????????????????????????????????????" + relatedEvents.getReName());
					return resultData;
				}
				if (relatedEvents.getOccuAddr() == null
						|| "".equals(relatedEvents.getOccuAddr())) {
					resultData.setResultMsg("???????????????????????????????????????????????????????????????" + relatedEvents.getOccuAddr());
					return resultData;
				}
				if (relatedEvents.getEventType() == null
						|| "".equals(relatedEvents.getEventType())) {
					resultData.setResultMsg("????????????????????????????????????????????????????????????" + relatedEvents.getEventType());
					return resultData;
				}
				if (relatedEvents.getEventLevel() == null
						|| "".equals(relatedEvents.getEventLevel())) {
					resultData.setResultMsg("????????????????????????????????????????????????????????????" + relatedEvents.getEventLevel());
					return resultData;
				}
				if (relatedEvents.getSituation() == null
						|| "".equals(relatedEvents.getSituation())) {
					resultData.setResultMsg("????????????????????????????????????????????????????????????" + relatedEvents.getSituation());
					return resultData;
				}
				MixedGridInfo gridInfo = mixedGridInfoService
						.getDefaultGridByOrgCode(orgCode);
				if (gridInfo == null) {
					resultData.setResultMsg("??????????????????????????????????????????????????????" + gridInfo);
					return resultData;
				}
				if(relatedEvents.getReId()==null){
					Long insertRelatedEvents = insertRelatedEvents(relatedEvents,orgCode);
					if(insertRelatedEvents>0){
						resultData.setPrimaryKey(insertRelatedEvents);
						resultData.setResultCode(1);
						resultData.setResultMsg("?????????????????????????????????");
					}else{
						resultData.setResultMsg("?????????????????????????????????");
					}
				
				}else{
					boolean updateRelatedEvents = updateRelatedEvents(relatedEvents);
					if(updateRelatedEvents){
						resultData.setPrimaryKey(relatedEvents.getReId());
						resultData.setResultCode(1);
						resultData.setResultMsg("?????????????????????????????????");
					}else{
						resultData.setResultMsg("?????????????????????????????????");
					}
				}
			
				/*
				 * //2. ??????????????????????????? //2.1??????????????? //2.2???????????? Long cirsId =
				 * getCiRsId(record); //3. ????????????????????? ??????????????????????????????????????? //3.1???????????? //3.2?????????
				 * Long gridId = null; if(gridInfo!=null){ gridId =
				 * gridInfo.getGridId(); } Long miId =
				 * saveMentalIllenss(record,cirsId,gridId); if(miId>0){
				 * resultData.setPrimaryKey(miId); resultData.setResultCode(1);
				 * resultData.setResultMsg("?????????????????????????????????"); }else{
				 * resultData.setResultMsg("?????????????????????????????????"); }
				 */
				return resultData;
				// } catch (DataFormatException e) {
			} catch (Exception e) {
				resultData.setResultMsg(e.getMessage());
			}
			return resultData;
		}
		/*
		 * 9+X??????????????????????????????????????????????????????
		 */
		
		public ResultData detail(Long reId, String orgCode){
			ResultData resultData = new ResultData();
			try{
				if(reId == null || "".equals(reId)){
					resultData.setResultMsg("??????[reId]????????????!");
					return resultData;
				}
				if(orgCode == null || "".equals(orgCode)){
					resultData.setResultMsg("orgCode????????????");
					return resultData;
				}
				RelatedEvents relatedEvents = findRelatedEventsById(reId,orgCode);
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("result",relatedEvents);
				resultData.setResultData(map);
				resultData.setResultCode(1);
				resultData.setResultMsg("????????????");
			}catch (Exception e) {
				resultData.setResultMsg("??????"+ reId + "???????????????????????????" + e.getMessage());
			}
			return resultData;
		}
		
		
		
	/*
	 *  ??????????????????????????????
	 *  ?????????T_ZZ_RELATED_EVENTS
	 *  bizType???1 ???????????? 2 ???????????? 3?????????
	 *  
	 */
	public ResultData delete(Map<String,String> params) {
		
		ResultData resultData = new ResultData();
		if (CommonFunctions.isBlank(params,"bizType")) {
			resultData.setResultMsg("bizType????????????????????????!");
			return resultData;
		}
		
		String bizType = params.get("bizType").toString();
		String idStr="";
		String gridCode="";
		String delAll="";
	
		if (CommonFunctions.isNotBlank(params,"idStr")){
			 idStr = params.get("idStr");
		}
		if (CommonFunctions.isNotBlank(params,"gridCode")){
			gridCode = params.get("gridCode");
		}
		if (CommonFunctions.isNotBlank(params,"delAll")){
			delAll = params.get("delAll");
		}
		
		if ("1".equals(bizType)) {
			
			List<Long> reIdList = new ArrayList<Long>();
			
			//????????????????????????????????????
			if (StringUtils.isBlank(delAll)) {
				String[] ids = idStr.split(",");
				for (int i = 0, length = ids.length; i < length; i++) {
					reIdList.add(Long.parseLong(ids[i]));
				}
			}
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("reIdList", reIdList);
			param.put("gridCode", gridCode);
			int re = relatedEventsMapper.deleteByIds4CareRoad(param);
			if (re > 0) {
				resultData.setResultCode(1);
				resultData.setResultMsg("????????????!");
				return resultData;
			} else {
				resultData.setResultMsg("????????????!");
				return resultData;
			}
			
		}
		
		if ("3".equals(delAll)){
			try {
				int re = relatedEventsMapper.deleteByBizAndGridCode(gridCode, bizType);
				if (re > 0) {
					resultData.setResultCode(1);
					resultData.setResultMsg("????????????????????????!");
					return resultData;
				} else {
					resultData.setResultMsg("???????????????????????? sql??????????????????!");
					return resultData;
				}
			}catch (Exception e) {
				e.printStackTrace();
				resultData.setResultMsg("??????????????????!");
				return resultData;
			}
		}else {
			String[] ids = idStr.split(",");
			List<Long> involvedIdList = new ArrayList<Long>();
			if (StringUtils.isEmpty(idStr)) {
				resultData.setResultMsg("?????????????????????????????????!");
				return resultData;
			}
			try {
				if (ids.length == 1) {
					boolean deleteRelatedEventsById = delRelatedEventsByIdAndGirdCode(Long
							.parseLong(ids[0]),gridCode,bizType);
					if (deleteRelatedEventsById){
						resultData.setResultCode(1);
						resultData.setResultMsg("??????????????????!");
						return resultData;
					}else {
						resultData.setResultMsg("??????????????????! sql????????????????????????(????????????????????????????????????????????????)");
						return resultData;
					}
				} else {
					for (int index = 0, length = ids.length; index < length; index++) {
						long involvedId = Long.parseLong(ids[index]);
						involvedIdList.add(involvedId);
					}
					int iniSize = involvedIdList.size(); //????????????????????????
					int deleteRelatedEventsByIds = delRelatedEventsByIdsAndGridCode(involvedIdList,gridCode,bizType);
					if (deleteRelatedEventsByIds>0){
						resultData.setResultCode(1);
						resultData.setResultMsg("?????????????????????"+deleteRelatedEventsByIds+"???????????????"+(iniSize-deleteRelatedEventsByIds)+"??????");
						return resultData;
					}else {
						resultData.setResultMsg("??????????????????! sql????????????????????????(????????????????????????????????????????????????)");
						return resultData;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultData.setResultMsg("??????????????????!sql????????????");
				return resultData;
			}
		}
	}
	
	/*
	 * 9+X??????????????????????????????
	 */
	@Override
	public ResultData list(Map<String, Object> params, int page,int rows,String orgCode){
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		
		ResultData resultData = new ResultData();
		if (StringUtil.isBlank(params.get("gridId").toString())) {
			resultData.setResultMsg("??????ID????????????!");
			return resultData;
		}
		if (StringUtil.isBlank(params.get("gridCode").toString())) {
			resultData.setResultMsg("????????????????????????!");
			return resultData;		
		}
		if (StringUtil.isBlank(params.get("bizType").toString())) {
			resultData.setResultMsg("????????????????????????!");
			return resultData;
		}
		Map<String, Object> result = new HashMap<String,Object>();
		if ("3".equals(params.get("bizType"))) {
			EUDGPagination findRelatedEventsPagination = findRelatedEventsPagination(page,rows,params,orgCode);
			result.put("total", findRelatedEventsPagination.getTotal());
			result.put("result", findRelatedEventsPagination.getRows());
			resultData.setResultData(result);
			resultData.setResultCode(1);
			resultData.setResultMsg("????????????????????????!");
			return resultData;
		} else {
			resultData.setResultMsg("??????????????????????????????!");
			return resultData;
		}
	}
	
	/*
	 * 9+X???????????????????????????????????????????????????
	 */
	@Override
	public ResultData saveRelatedEventsSchool(RelatedEvents bo, String  orgCode){
		ResultData resultData = new ResultData();
		
		
		if (!"2".equals(bo.getBizType())) {
			resultData.setResultMsg("??????????????????????????????????????????!");
			return resultData;
		}
		
		if (StringUtil.isBlank(bo.getReName())) {
			resultData.setResultMsg("????????????????????????!");
			return resultData;
		}
		if (bo.getBizId() == null) {
			resultData.setResultMsg("????????????????????????!");
			return resultData;	
		}
		if (StringUtil.isBlank(bo.getPrisonersName())) {
			resultData.setResultMsg("????????????????????????!");
			return resultData;	
		}
		if (StringUtil.isBlank(bo.getOccuAddr())) {
			resultData.setResultMsg("????????????????????????!");
			return resultData;	
		}
		if (StringUtil.isBlank(bo.getPrisonersDocType())) {
			resultData.setResultMsg("????????????????????????!");
			return resultData;	
		}
		if (StringUtil.isBlank(bo.getPrisonersDocNo())) {
			resultData.setResultMsg("????????????????????????!");
			return resultData;	
		}
		if (StringUtil.isBlank(bo.getNature())) {
			resultData.setResultMsg("????????????????????????!");
			return resultData;	
		}
		if (StringUtil.isBlank(bo.getSituation())) {
			resultData.setResultMsg("????????????????????????!");
			return resultData;	
		}
		if (StringUtil.isBlank(bo.getDetectedOverview())) {
			resultData.setResultMsg("????????????????????????!");
			return resultData;	
		}
		PlaceInfo placeInfo = placeInfoservice.findPlaceInfoById(bo.getBizId());
		if (placeInfo == null) {
			resultData.setResultMsg("???????????????????????????!");
			return resultData;
		}
		
		/*Date now = Calendar.getInstance().getTime();*/
		
			try {
				if (bo.getReId() == null) {
					
					Long insertRelatedEvents;
				insertRelatedEvents = insertRelatedEvents(bo, orgCode);
				if (insertRelatedEvents > 0) {
					resultData.setResultCode(1);
					resultData.setPrimaryKey(insertRelatedEvents);
					resultData.setResultMsg("????????????????????????????????????!id=" + insertRelatedEvents);
					return resultData;
				} else {
					resultData.setResultMsg("????????????????????????????????????!");
					return resultData;
				}
				} else {
					int cnt = relatedEventsMapper.update(bo);
					if (cnt > 0) {
						resultData.setResultCode(1);
						resultData.setPrimaryKey(bo.getReId());
						resultData.setResultMsg("????????????????????????????????????!");
						return resultData;
					} else {
						resultData.setResultMsg("????????????????????????????????????!");
						return resultData;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultData;
			
		
	}
	
	/*
	 * 9+X??????????????????????????????????????????
	 */
	@Override
	public ResultData deleteRelatedEventsSchool(RelatedEvents bo, Long userId){
		ResultData resultData = new ResultData();
		if (bo.getReId() == null) {
			resultData.setResultMsg("??????????????????!");
			return resultData;
		} else {
			int cnt = relatedEventsMapper.delete(bo.getReId());
			if (cnt > 0) {
				resultData.setResultCode(1);
				resultData.setResultMsg("???????????????????????????????????????" + cnt);
				return resultData;
			} else {
				resultData.setResultMsg("???????????????????????????????????????");
				return resultData;
			}
		}
		
	}
	
	/*
	 * 9+X??????????????????????????????????????????
	 */
	@Override
	public ResultData detailRelatedEventsSchool(RelatedEvents bo){
		ResultData resultData = new ResultData();
		Map<String, Object> result = new HashMap<String,Object>();
		
		if (bo.getReId() == null) {
			resultData.setResultMsg("??????????????????!");
			return resultData;
		} else {
			
			RelatedEvents relatedEvents = null;
			if (null != bo.getBizType() && bo.getBizType().equals("1")) {
				
				relatedEvents = relatedEventsMapper.findById4CareRoad(bo.getReId());
				
			}else if (null != bo.getBizType() && bo.getBizType().equals("2")) {
				
				relatedEvents = relatedEventsMapper.findById4School(bo.getReId());
				
			}else {
				relatedEvents = relatedEventsMapper.findById(bo.getReId());
			}
			
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("result",relatedEvents);
			resultData.setResultCode(1);
			resultData.setResultMsg("????????????!");
			resultData.setResultData(map);
			return resultData;
		}
	}
	
	/*
	 * 9+X??????????????????????????????????????????
	 */
	@Override
	public ResultData listRelatedEventsSchool(Map<String, Object> params, int page,int rows){
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		
		ResultData resultData = new ResultData();
		if (StringUtil.isBlank(params.get("gridId").toString())) {
			resultData.setResultMsg("??????ID????????????!");
			return resultData;
		}
		if (StringUtil.isBlank(params.get("gridCode").toString())) {
			resultData.setResultMsg("????????????????????????!");
			return resultData;		
		}
		if (StringUtil.isBlank(params.get("bizType").toString())) {
			resultData.setResultMsg("????????????????????????!");
			return resultData;
		}
		
		Map<String, Object> result = new HashMap<String,Object>();
		if ("2".equals(params.get("bizType"))) {
			int count = relatedEventsMapper.findCountForSchoolRE(params);
			List<RelatedEvents> list = relatedEventsMapper.findPageListForSchoolRE(params, rowBounds);
			formateRelatedEventsOut(list, params.get("gridCode").toString());
			EUDGPagination eudgPagination = new EUDGPagination(count, list);
			result.put("total", eudgPagination.getTotal());
			result.put("result", eudgPagination.getRows());
			resultData.setResultData(result);
			resultData.setResultCode(1);
			resultData.setResultMsg("????????????!");
			return resultData;
		} else {
			resultData.setResultMsg("??????????????????????????????????????????!");
			return resultData;
		}
	}

	@Override
	public EUDGPagination findRelatedEventsMajor(int pageNo, int pageSize,
			Map<String, Object> params, String orgCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EUDGPagination findRelatedEventsSchool(int pageNo, int pageSize,
			Map<String, Object> params, String orgCode) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
