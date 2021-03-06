package cn.ffcs.zhsq.relatedEvents.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.persistence.event.InvolvedPeopleMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.common.validator.validation.CommonValidator;
import cn.ffcs.shequ.crypto.HashIdManager;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CareRoad;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.legal.ResultData;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.grid.ICareRoadService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.INumberConfigureService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.domain.event.docking.Result;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEvents;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEventsForSearch;
import cn.ffcs.zhsq.mybatis.persistence.relatedEvents.RelatedEventsMapper;
import cn.ffcs.zhsq.relatedEvents.service.IRelatedEventsService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.EventNoUtil;

@Service(value="relatedEventsServiceServiceImpl")
public class RelatedEventsServiceImpl implements IRelatedEventsService {
	
	@Autowired
	private RelatedEventsMapper relatedEventsMapper;

	@Autowired
	private IBaseDictionaryService dictionaryService;
	@Autowired
	private InvolvedPeopleMapper involvedPeopleMapper;
	//??????????????????
	@Autowired
	private ICareRoadService careRoadService;
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private INumberConfigureService numberConfigureService;//??????????????????
	@Autowired
	private IInvolvedPeopleService involvedPeopleService;//??????????????????
	@Autowired
	private IResMarkerService resMarkerService;
	
	public static final String IS_DETECTION_Y = "1";//???????????? ???
	public static final String BIZ_TYPE = "1";//????????????
	private static final String HOMICIDE_BIZ_TYPE = "4";//????????????
	
	@Override
	public Long insertRelatedEvents(RelatedEvents relatedEvents) {
		int result = 0;
		String bizType = relatedEvents.getBizType();
		String reNo = relatedEvents.getReNo();
		
		formateRelatedEventsIn(relatedEvents);
		
		if(StringUtils.isBlank(reNo)) {
			if(HOMICIDE_BIZ_TYPE.equals(bizType)) {
				try {
					reNo = numberConfigureService.getNumber("", ConstantValue.HOMICIDE_CASE_BIZ_CODE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					reNo = EventNoUtil.getEventNO(null, ConstantValue.SEQ_RE_NO, "");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			//?????????(???)?????????
			relatedEvents.setReNo(reNo);
		}
		
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
		
		Object relatedEventsObj = params.get("relatedEventsForSearch");
		if(relatedEventsObj!=null && relatedEventsObj instanceof RelatedEventsForSearch){
			RelatedEventsForSearch relatedEventsForSearch = (RelatedEventsForSearch)relatedEventsObj;
			params.putAll(addExtParams(relatedEventsForSearch));
		}
		
		int count = relatedEventsMapper.findCountByCriteria(params);
		List<RelatedEvents> list = relatedEventsMapper.findPageListByCriteria(params, rowBounds);
		formateRelatedEventsOut(list, orgCode);
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}

	@Override
	public Long findRelatedEventsCountByCriteria(Map<String, Object> params) {

		if(CommonFunctions.isNotBlank(params, "relatedEvents")) {
			Object relatedEventsObj = params.get("relatedEvents");
			if(relatedEventsObj!=null && relatedEventsObj instanceof RelatedEvents){
				RelatedEvents relatedEvents = (RelatedEvents)relatedEventsObj;
				params.putAll(addExtParams(relatedEvents));
			}
		} else if(CommonFunctions.isNotBlank(params, "relatedEventsForSearch")) {
			Object relatedEventsForSearchObj = params.get("relatedEventsForSearch");
			if(relatedEventsForSearchObj!=null && relatedEventsForSearchObj instanceof RelatedEventsForSearch){
				RelatedEventsForSearch relatedEventsForSearch = (RelatedEventsForSearch)relatedEventsForSearchObj;
				params.putAll(addExtParams(relatedEventsForSearch));
			}
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
		
		if(CommonFunctions.isNotBlank(params, "relatedEvents")) {
			Object relatedEventsObj = params.get("relatedEvents");
			if(relatedEventsObj!=null && relatedEventsObj instanceof RelatedEvents){
				RelatedEvents relatedEvents = (RelatedEvents)relatedEventsObj;
				params.putAll(addExtParams(relatedEvents));
			}
		} else if(CommonFunctions.isNotBlank(params, "relatedEventsForSearch")) {
			Object relatedEventsForSearchObj = params.get("relatedEventsForSearch");
			if(relatedEventsForSearchObj!=null && relatedEventsForSearchObj instanceof RelatedEventsForSearch){
				RelatedEventsForSearch relatedEventsForSearch = (RelatedEventsForSearch)relatedEventsForSearchObj;
				params.putAll(addExtParams(relatedEventsForSearch));
			}
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
		}
		return params;
	}
	
	private Map<String, Object> addExtParams(RelatedEventsForSearch relatedEventsForSearch){
		Map<String, Object> params = new HashMap<String, Object>();
		if(relatedEventsForSearch != null){
			String gridCode = relatedEventsForSearch.getGridCode();
			Long gridId = relatedEventsForSearch.getGridId();
			String occuDateStartStr = relatedEventsForSearch.getOccuDateStartStr(),
				   occuDateEndStr = relatedEventsForSearch.getOccuDateEndStr();
			String spyEndDateStartStr = relatedEventsForSearch.getSpyEndDateStartStr(),
				   spyEndDateEndStr = relatedEventsForSearch.getSpyEndDateEndStr();
			
			params.putAll(addExtParams((RelatedEvents)relatedEventsForSearch));
			
			if(gridId!=null && gridId>0){//????????????gridId
				params.put("gridId", gridId);
			}else if(StringUtils.isNotBlank(gridCode)){
				params.put("gridCode", gridCode);
			}
			String nature = relatedEventsForSearch.getNature();
			if(StringUtils.isNotBlank(nature)) {
				params.put("nature", nature);
			}
			
			if(StringUtils.isNotBlank(occuDateStartStr)) {
				params.put("occuDateStartStr", occuDateStartStr);
			}
			
			if(StringUtils.isNotBlank(occuDateEndStr)) {
				params.put("occuDateEndStr", occuDateEndStr);
			}
			
			if(StringUtils.isNotBlank(spyEndDateStartStr)) {
				params.put("spyEndDateStartStr", spyEndDateStartStr);
			}
			
			if(StringUtils.isNotBlank(spyEndDateEndStr)) {
				params.put("spyEndDateEndStr", spyEndDateEndStr);
			}
		}
		return params;
	}
	
	/**
	 * ??????????????? ??????
	 * @param relatedEvents
	 */
	private void formateRelatedEventsIn(RelatedEvents relatedEvents){
		if(relatedEvents != null) {
			String bizType = relatedEvents.getBizType();
			String occuDateStr = relatedEvents.getOccuDateStr();
			String spyEndDateStr = relatedEvents.getSpyEndDateStr();
			Date occuDate = null, spyEndDate = null;
			String datePattern = DateUtils.PATTERN_24TIME;
			
			if(HOMICIDE_BIZ_TYPE.equals(bizType)) {
				datePattern = DateUtils.PATTERN_DATE;
			}
			
			if(StringUtils.isNotBlank(occuDateStr)){
				try {
					occuDate = DateUtils.convertStringToDate(occuDateStr, datePattern);
				} catch (ParseException e) {
					occuDate = null;
					e.printStackTrace();
				}
				
				if(occuDate != null){
					relatedEvents.setOccuDate(occuDate);
				}
			}
			
			if(StringUtils.isNotBlank(spyEndDateStr)) {
				try {
					spyEndDate = DateUtils.convertStringToDate(spyEndDateStr, datePattern);
				} catch (ParseException e) {
					spyEndDate = null;
					e.printStackTrace();
				}
				
				if(spyEndDate != null) {
					relatedEvents.setSpyEndDate(spyEndDate);
				}
			}
		}
	}
	
	/**
	 * ??????????????? ??????
	 * @param relatedEvents
	 */
	private void formateRelatedEventsOut(RelatedEvents relatedEvents) {
		if(relatedEvents != null) {
			String bizType = relatedEvents.getBizType();
			String datePattern = DateUtils.PATTERN_24TIME;
			
			if(StringUtils.isNotBlank(bizType) && BIZ_TYPE.equals(bizType)){
				Long bizId = relatedEvents.getBizId();
				CareRoad careRoad = careRoadService.findCareRoadById(bizId);
				if(careRoad != null){
					relatedEvents.setBizName(careRoad.getLotName());
				}
			}
			
			if(HOMICIDE_BIZ_TYPE.equals(bizType)) {
				datePattern = DateUtils.PATTERN_DATE;
			}
			
			//????????????
			if(!StringUtils.isBlank(relatedEvents.getIsDetection())) {
				relatedEvents.setIsDetectionName(IS_DETECTION_Y.equals(relatedEvents.getIsDetection())?"???":"???");
			} else {
				relatedEvents.setIsDetectionName("???");
			}
			
			//????????????
			if(relatedEvents.getOccuDate() != null){
				relatedEvents.setOccuDateStr(DateUtils.formatDate(relatedEvents.getOccuDate(), datePattern));
			}
			
			//??????????????????
			if(relatedEvents.getSpyEndDate() != null) {
				relatedEvents.setSpyEndDateStr(DateUtils.formatDate(relatedEvents.getSpyEndDate(), datePattern));;
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
		List<BaseDataDict> prisonersDocTypeList = dictionaryService.getDataDictListOfSinglestage(ConstantValue.PRISONERS_DOC_TYPE, orgCode);
		List<BaseDataDict> natureList = dictionaryService.getDataDictListOfSinglestage(ConstantValue.NATURE, orgCode);
		
		if(relatedEventsList!=null && relatedEventsList.size()>0){
			for(RelatedEvents re : relatedEventsList){
				
				formateRelatedEventsOut(re);
				
				if(!StringUtils.isBlank(re.getPrisonersDocType())){//????????????
					for(int index = 0; index < prisonersDocTypeList.size(); index++){
						if(re.getPrisonersDocType().equals(prisonersDocTypeList.get(index).getDictGeneralCode())){
							String prisonersDocTypeName = String.valueOf(prisonersDocTypeList.get(index).getDictName());
							re.setPrisonersDocTypeName(prisonersDocTypeName);
							break;
						}
					}
				}
				
				if(!StringUtils.isBlank(re.getNature())){//????????????
					for(int index = 0; index < natureList.size(); index++){
						if(re.getNature().equals(natureList.get(index).getDictGeneralCode())){
							String natureName = String.valueOf(natureList.get(index).getDictName());
							re.setNatureName(natureName);
							break;
						}
					}
				}
			re.setHashId(HashIdManager.encrypt(re.getReId()));
			//re.setReId(0l);
			}
		}
	}
	/**
	 * ??????????????????
	 * 
	 */	
	
//	??????
	public ResultData saverelated(RelatedEvents relatedEvents){
		ResultData resultData = new ResultData();
		try {
			if( !"1".equals(relatedEvents.getBizType())){
				resultData.setResultMsg("????????????[bizType]????????????1");
				return resultData;
			}
//			if(relatedEvents.getCreateUserId()==null || "".equals(relatedEvents.getCreateUserId())){
//				resultData.setResultMsg("?????????[createUserId]????????????");
//				return resultData;
//			}
			if(relatedEvents.getReName()==null || "".equals(relatedEvents.getReName())){
				resultData.setResultMsg("????????????[reName]????????????");
				return resultData;
			}
			
			if(relatedEvents.getBizId()==null || "".equals(relatedEvents.getBizId())){
				resultData.setResultMsg("????????????[bizId]????????????");
				return resultData;
			}
			if(relatedEvents.getOccuAddr()==null ||  "".equals(relatedEvents.getOccuAddr())){
				resultData.setResultMsg("????????????[occuAddr]????????????");
				return resultData;
			}
			if(relatedEvents.getSituation()==null || "".equals(relatedEvents.getSituation())){
				resultData.setResultMsg("???(???)?????????[situation]????????????");
				return resultData;
			}
			if(relatedEvents.getDetectedOverview()==null || "".equals(relatedEvents.getDetectedOverview())){
				resultData.setResultMsg("??????????????????[detectedOverview]????????????");
				return resultData;
			}
			if(relatedEvents.getPrisonersName()==null || "".equals(relatedEvents.getPrisonersName())){
				resultData.setResultMsg("??????(?????????)??????[prisonersName]????????????");
				return resultData;
			}
			if(relatedEvents.getPrisonersDocNo()==null || "".equals(relatedEvents.getPrisonersDocNo())){
				resultData.setResultMsg("???(?????????)????????????[prisonersDocNo]????????????");
				return resultData;
			}
			//??????
			if(relatedEvents.getReId()==null || "".equals(relatedEvents.getReId())){
				Long insertRelatedEvents = insertRelatedEvents(relatedEvents);
				if(insertRelatedEvents > 0){
					resultData.setResultCode(1);
					resultData.setPrimaryKey(insertRelatedEvents);
					resultData.setResultMsg("?????????????????????????????????");
				}else{
					resultData.setResultMsg("?????????????????????????????????");
				}
			}
			//??????
			else{
				boolean bool = updateRelatedEvents(relatedEvents);
				if(bool){
					resultData.setPrimaryKey(relatedEvents.getReId());
					resultData.setResultCode(1);
					resultData.setResultMsg("?????????????????????????????????");
				}else{
					resultData.setResultMsg("?????????????????????????????????");
				}
			}
			return resultData;
		}catch(Exception e){
			resultData.setResultMsg("????????????," + e.getMessage());
		}
		return resultData;
	}
	
//	??????
	public ResultData deleterelated(String reId){
		ResultData resultData = new ResultData();
		try{
			if(reId == null || "".equals(reId)){
				resultData.setResultMsg("??????[reId]????????????!");
				return resultData;
			}
			String[] idstr = reId.split(",");
			//??????id?????????????????????????????????
			if(idstr.length == 1){
				Long id = Long.parseLong(reId);
				boolean bool = deleteRelatedEventsById(id);
				if(bool){
					resultData.setResultCode(1);
					resultData.setResultMsg("??????????????????"+ reId +"??????????????????????????????");
				}else{
					resultData.setResultMsg("??????????????????"+ reId +"??????????????????????????????");
				}
			}else{
				List<Long> idList = new ArrayList<Long>();
				for(int index=0; index < idstr.length; index++){
					Long id = Long.parseLong(idstr[index]);
					idList.add(id);
				}
				int result = deleteRelatedEventsByIds(idList);
				if(result > 0){
					resultData.setResultCode(1);
					resultData.setResultMsg("??????????????????????????????" + result);
				}else{
					resultData.setResultMsg("????????????????????????????????????");
				}
			}
			return resultData;
		}catch (Exception e) {
			resultData.setResultMsg("???????????????" + e.getMessage());
		}
		return resultData;
	}
//	??????
	public ResultData detailrelated(Long reId, String orgCode){
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
			resultData.setResultMsg("??????"+ reId + "???????????????" + e.getMessage());
		}
		return resultData;
	}

	public ResultData detailrelatedByPeopleId(Map<String,Object> params){
		Long peopleId = Long.parseLong(params.get("peopleId").toString());
		ResultData resultData = new ResultData();
		try{
			if(peopleId == null || "".equals(peopleId)){
				resultData.setResultMsg("??????[peopleId]????????????!");
				return resultData;
			}
			RelatedEvents relatedEvents = relatedEventsMapper.findByPeopleId(peopleId);
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("result",relatedEvents);
			resultData.setResultData(map);
			resultData.setResultCode(1);
			resultData.setResultMsg("????????????");
		}catch (Exception e) {
			resultData.setResultMsg("???????????????????????????"+peopleId+"????????????????????????" + e.getMessage());
		}
		return resultData;
	}
	
//	??????
	public ResultData listrelated(int pageNo,int pageSize, Map<String, Object> params, String orgCode){
		ResultData resultData = new ResultData();
		try{
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
			if (!"1".equals(params.get("bizType"))) {
				resultData.setResultMsg("????????????????????????????????????!");
				return resultData;
			}else{
				EUDGPagination eUDGPagination  = findRelatedEventsPagination(pageNo,pageSize,params,orgCode);
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("total", eUDGPagination.getTotal());
				map.put("result", eUDGPagination.getRows());
				resultData.setResultData(map);
				resultData.setResultCode(1);
				resultData.setResultMsg("????????????");
			}
			return resultData;
		}catch (Exception e) {
			resultData.setResultMsg("??????????????????," + e.getMessage());
		}
		return resultData;
		
	}

	// ???????????????????????? ?????????????????????
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
				// ???????????????????????????????????????
				if( !"4".equals(relatedEvents.getBizType())){
					resultData.setResultMsg("????????????[bizType]????????????????????????");
					return resultData;
				}
				if (StringUtil.isBlank(relatedEvents.getReName())) {
					resultData.setResultMsg("????????????????????????!");
					return resultData;
				}
				if (orgCode == null || "".equals(orgCode)) {
					resultData.setResultMsg("??????????????????????????????????????????????????????" + orgCode);
					return resultData;
				}
				if (relatedEvents.getReName() == null
						|| "".equals(relatedEvents.getReName())) {
					resultData.setResultMsg("????????????????????????????????????????????????????????????" + relatedEvents.getReName());
					return resultData;
				}
				
				MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(orgCode);
				if (gridInfo == null) {
					resultData.setResultMsg("??????????????????????????????????????????????????????" + gridInfo);
					return resultData;
				}
				if(relatedEvents.getReId()==null){
					Long insertRelatedEvents = insertRelatedEvents(relatedEvents);
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
		// ??????????????????????????????
				public ResultData delete(Map<String,String> params) {
					String bizType = "4"; //??????????????????
					String idStr="";
					String gridCode="";
					String delAll="";
					ResultData resultData = new ResultData();
					if (CommonFunctions.isNotBlank(params,"idStr")){
						idStr = params.get("idStr");
					}
					if (CommonFunctions.isNotBlank(params,"gridCode")){
						gridCode = params.get("gridCode");
					}
					if (CommonFunctions.isNotBlank(params,"delAll")){
						delAll = params.get("delAll");
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
							resultData.setResultMsg("??????id????????????");
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
									resultData.setResultMsg("??????????????????! ??????????????????????????????????????????????????????????????????");
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
							resultData.setResultMsg("??????????????????????????????");
							return resultData;
						}
					}
				}
				/*
				 * 9+X??????????????????????????????
				 */
				@Override
				public ResultData detail(Long id,String orgcode){
					ResultData resultData = new ResultData();
					
				  if(StringUtils.isEmpty(orgcode)){
						resultData.setResultMsg("????????????????????????" );
						return resultData;  
				  }
				  if(id==0||id==null){
						resultData.setResultMsg("??????id????????????" );
						return resultData;    
				  }
					try{
						RelatedEvents findRelatedEventsById = findRelatedEventsById(id,orgcode);
						Map<String, Object> map = new HashMap<String,Object>();
						map.put("result",findRelatedEventsById);
						resultData.setResultMsg("????????????!");
						resultData.setResultCode(1);
						resultData.setResultData(map);
				       } catch (Exception e) {
				    		e.printStackTrace();
				    		resultData.setResultMsg("????????????!");
				       }
					
				  return resultData;
				}

		
				
				
				//9+x???????????????????????????????????? ???????????????
				public ResultData savePeople(InvolvedPeople involvedPeople) {

					ResultData resultData = new ResultData();
					// 1.?????????????????????????????????
					try {
						// 1.1 ????????????????????????
						CommonValidator.validate(involvedPeople);
						/*
						 * if(record.getCareGroupMembers()!=null &&
						 * record.getCareGroupMembers().size()>0){ for (CareGroupMember
						 * careGroupMember:record.getCareGroupMembers()) {
						 * CommonValidator.validate(careGroupMember); } }
						 */
						// ???????????????????????????????????????
						if (involvedPeople.getBizId() == null || "".equals(involvedPeople.getBizId())) {
							resultData.setResultMsg("????????????id????????????" + involvedPeople.getBizId());
							return resultData;
						}
						if (involvedPeople.getBizType() == null
								|| "".equals(involvedPeople.getBizType())) {
							resultData.setResultMsg("??????????????????????????????????????????????????????????????????????????????" + involvedPeople.getBizType());
							return resultData;
						}
						if( (!"04".equals(involvedPeople.getBizType().toString()))&& !"03".equals(involvedPeople.getBizType().toString())){
							resultData.setResultMsg("????????????????????????????????????(?????????)");
							return resultData;
						}
						if (StringUtil.isBlank(involvedPeople.getName())) {
							resultData.setResultMsg("??????????????????!");
							return resultData;
						}
						if (StringUtil.isBlank(involvedPeople.getCardType())) {
							resultData.setResultMsg("????????????????????????!");
							return resultData;
						}
						if (StringUtil.isBlank(involvedPeople.getIdCard())) {
							resultData.setResultMsg("????????????????????????!");
							return resultData;
						}
						if (StringUtil.isBlank(involvedPeople.getSex())) {
							resultData.setResultMsg("??????????????????!");
							return resultData;
						}
						if (involvedPeople.getBirthdayStr()==null) {
							resultData.setResultMsg("????????????????????????!");
							return resultData;
						}
						if(StringUtil.isBlank(involvedPeople.getReOrgCode())){
							resultData.setResultMsg("?????????????????????!");
							return resultData;
						}
						if(involvedPeople.getIpId()==null){
							Long insertInvolvedPeople = involvedPeopleService.insertInvolvedPeople(involvedPeople);
							if(insertInvolvedPeople>0){
								resultData.setPrimaryKey(insertInvolvedPeople);
								resultData.setResultCode(1);
								resultData.setResultMsg("?????????????????????????????????????????????????????????");
							}else{
								resultData.setResultMsg("?????????????????????????????????????????????????????????");
							}
						
						}else{
							boolean updateInvolvedPeople = involvedPeopleService.updateInvolvedPeople(involvedPeople);
							if(updateInvolvedPeople){
								resultData.setPrimaryKey(involvedPeople.getIpId());
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
				//9+x??????????????????????????????????????????
				
				public ResultData detailPeople(Long ipId){
					ResultData resultData = new ResultData();
					try{
						if(ipId == null || "".equals(ipId)){
							resultData.setResultMsg("??????[id]????????????!");
							return resultData;
						}
						
						InvolvedPeople findInvolvedPeopleById = involvedPeopleService.findInvolvedPeopleById(ipId);
						Map<String, Object> map = new HashMap<String,Object>();
						map.put("result",findInvolvedPeopleById);
						resultData.setResultData(map);
						resultData.setResultCode(1);
						resultData.setResultMsg("????????????");
					}catch (Exception e) {
						resultData.setResultMsg("??????"+ ipId + "????????????????????????????????????" + e.getMessage());
					}
					return resultData;
				}
                 //9+x??????????????????????????????????????????
				public ResultData deletePeople(Map<String,String> params){
					String ipId = "";
					String gridCode="";
					String delAll="";
					if (CommonFunctions.isNotBlank(params,"ipId")){
						ipId = params.get("ipId");
					}
					if (CommonFunctions.isNotBlank(params,"gridCode")){
						gridCode = params.get("gridCode");
					}
					if (CommonFunctions.isNotBlank(params,"delAll")){
						delAll = params.get("delAll");
					}
					ResultData resultData = new ResultData();
					if ("3".equals(delAll)){
						resultData.setResultMsg("??????????????????????????????");
						return resultData;
					}else {
						String[] ids = ipId.split(",");
						List<Long> involvedIdList = new ArrayList<Long>();
						if (StringUtils.isEmpty(ipId)) {
							resultData.setResultMsg("??????[idStr]????????????!");
							return resultData;
						}
						try {
							Map<String,Object> sqlParams = new HashMap<>();
							for (int index = 0, length = ids.length; index < length; index++) {
								long involvedId = Long.parseLong(ids[index]);
								involvedIdList.add(involvedId);
							}
							sqlParams.put("ipIdList",involvedIdList);
							sqlParams.put("gridCode",gridCode);
							int ipIdSize = involvedIdList.size();
							int deleteInvolvedPeopleById = involvedPeopleMapper.deleteByIpIdsAndGridCode(sqlParams);
							if (deleteInvolvedPeopleById>0){
								resultData.setResultCode(1);
								resultData.setResultMsg("?????????????????????"+deleteInvolvedPeopleById+"???????????????"+(ipIdSize-deleteInvolvedPeopleById)+"??????");
								return resultData;
							}else {
								resultData.setResultMsg("??????????????????! sql???????????????????????????????????????????????????????????????????????????????????????");
								return resultData;
							}
						} catch (Exception e) {
							resultData.setResultMsg("???????????????" + e.getMessage());
						}
						return resultData;
					}
				}
				//9+x??????????????????????????????????????????
				public ResultData listpeople( InvolvedPeople involvedPeople){
					ResultData resultData = new ResultData();
					try{
						
						if (StringUtil.isBlank(involvedPeople.getBizId().toString())) {
							resultData.setResultMsg("????????????ID????????????!");
							return resultData;
						}
						if (StringUtil.isBlank(involvedPeople.getBizType().toString())) {
							resultData.setResultMsg("????????????????????????!");
							return resultData;		
						}
					
						if( (!"04".equals(involvedPeople.getBizType().toString()))&& !"03".equals(involvedPeople.getBizType().toString())){
							resultData.setResultMsg("????????????????????????????????????(?????????)");
							return resultData;
						}
						
						List<InvolvedPeople> findInvolvedPeopleList = involvedPeopleService.findInvolvedPeopleList(involvedPeople);
					
							Map<String, Object> map = new HashMap<String,Object>();
							map.put("total", findInvolvedPeopleList.size());
							map.put("rows", findInvolvedPeopleList);
							resultData.setResultData(map);
							resultData.setResultCode(1);
						return resultData;
					}catch (Exception e) {
						resultData.setResultMsg("??????????????????," + e.getMessage());
					}
					return resultData;
					
				}

				@Override
				public Result list(Map<String, Object> params, int page,
						int rows, String orgCode) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public EUDGPagination findRelatedEventsPageListByCriteriaHom(
						int pageNo, int pageSize, Map<String, Object> params,
						String orgCode) {
					// TODO Auto-generated method stub
					return null;
				}
}
