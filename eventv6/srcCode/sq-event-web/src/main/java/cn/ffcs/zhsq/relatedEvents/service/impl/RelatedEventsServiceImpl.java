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
	//护路护线接口
	@Autowired
	private ICareRoadService careRoadService;
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private INumberConfigureService numberConfigureService;//编码配置服务
	@Autowired
	private IInvolvedPeopleService involvedPeopleService;//涉及人员服务
	@Autowired
	private IResMarkerService resMarkerService;
	
	public static final String IS_DETECTION_Y = "1";//是否破案 是
	public static final String BIZ_TYPE = "1";//护路护线
	private static final String HOMICIDE_BIZ_TYPE = "4";//命案防控
	
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
			
			//设置案(事)件编码
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
	 * 增添额外查询条件
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
			
			if(gridId!=null && gridId>0){//优先使用gridId
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
	 * 对象格式化 向内
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
	 * 对象格式化 向外
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
			
			//是否破案
			if(!StringUtils.isBlank(relatedEvents.getIsDetection())) {
				relatedEvents.setIsDetectionName(IS_DETECTION_Y.equals(relatedEvents.getIsDetection())?"是":"否");
			} else {
				relatedEvents.setIsDetectionName("否");
			}
			
			//发生时间
			if(relatedEvents.getOccuDate() != null){
				relatedEvents.setOccuDateStr(DateUtils.formatDate(relatedEvents.getOccuDate(), datePattern));
			}
			
			//侦查终结日期
			if(relatedEvents.getSpyEndDate() != null) {
				relatedEvents.setSpyEndDateStr(DateUtils.formatDate(relatedEvents.getSpyEndDate(), datePattern));;
			}
			
			//创建时间
			if(relatedEvents.getCreatedTime() != null){
				relatedEvents.setCreatedTimeStr(DateUtils.formatDate(relatedEvents.getCreatedTime(), DateUtils.PATTERN_24TIME));
			}
			
			//更新时间
			if(relatedEvents.getUpdateTime() != null){
				relatedEvents.setCreatedTimeStr(DateUtils.formatDate(relatedEvents.getUpdateTime(), DateUtils.PATTERN_24TIME));
			}
		}
	}
	
	/**
	 * 对象格式化 向外
	 * @param relatedEventsList
	 */
	private void formateRelatedEventsOut(List<RelatedEvents> relatedEventsList, String orgCode) {
		List<BaseDataDict> prisonersDocTypeList = dictionaryService.getDataDictListOfSinglestage(ConstantValue.PRISONERS_DOC_TYPE, orgCode);
		List<BaseDataDict> natureList = dictionaryService.getDataDictListOfSinglestage(ConstantValue.NATURE, orgCode);
		
		if(relatedEventsList!=null && relatedEventsList.size()>0){
			for(RelatedEvents re : relatedEventsList){
				
				formateRelatedEventsOut(re);
				
				if(!StringUtils.isBlank(re.getPrisonersDocType())){//证件类型
					for(int index = 0; index < prisonersDocTypeList.size(); index++){
						if(re.getPrisonersDocType().equals(prisonersDocTypeList.get(index).getDictGeneralCode())){
							String prisonersDocTypeName = String.valueOf(prisonersDocTypeList.get(index).getDictName());
							re.setPrisonersDocTypeName(prisonersDocTypeName);
							break;
						}
					}
				}
				
				if(!StringUtils.isBlank(re.getNature())){//案件类型
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
	 * 对外接口采集
	 * 
	 */	
	
//	保存
	public ResultData saverelated(RelatedEvents relatedEvents){
		ResultData resultData = new ResultData();
		try {
			if( !"1".equals(relatedEvents.getBizType())){
				resultData.setResultMsg("案件类型[bizType]类型应为1");
				return resultData;
			}
//			if(relatedEvents.getCreateUserId()==null || "".equals(relatedEvents.getCreateUserId())){
//				resultData.setResultMsg("创建人[createUserId]不能为空");
//				return resultData;
//			}
			if(relatedEvents.getReName()==null || "".equals(relatedEvents.getReName())){
				resultData.setResultMsg("案件名称[reName]不能为空");
				return resultData;
			}
			
			if(relatedEvents.getBizId()==null || "".equals(relatedEvents.getBizId())){
				resultData.setResultMsg("所在地段[bizId]不能为空");
				return resultData;
			}
			if(relatedEvents.getOccuAddr()==null ||  "".equals(relatedEvents.getOccuAddr())){
				resultData.setResultMsg("案发地点[occuAddr]不能为空");
				return resultData;
			}
			if(relatedEvents.getSituation()==null || "".equals(relatedEvents.getSituation())){
				resultData.setResultMsg("案(事)件情况[situation]不能为空");
				return resultData;
			}
			if(relatedEvents.getDetectedOverview()==null || "".equals(relatedEvents.getDetectedOverview())){
				resultData.setResultMsg("案件侦破情况[detectedOverview]不能为空");
				return resultData;
			}
			if(relatedEvents.getPrisonersName()==null || "".equals(relatedEvents.getPrisonersName())){
				resultData.setResultMsg("主犯(嫌疑人)姓名[prisonersName]不能为空");
				return resultData;
			}
			if(relatedEvents.getPrisonersDocNo()==null || "".equals(relatedEvents.getPrisonersDocNo())){
				resultData.setResultMsg("犯(嫌疑人)证件号码[prisonersDocNo]不能为空");
				return resultData;
			}
			//新增
			if(relatedEvents.getReId()==null || "".equals(relatedEvents.getReId())){
				Long insertRelatedEvents = insertRelatedEvents(relatedEvents);
				if(insertRelatedEvents > 0){
					resultData.setResultCode(1);
					resultData.setPrimaryKey(insertRelatedEvents);
					resultData.setResultMsg("新增涉及线路案件成功！");
				}else{
					resultData.setResultMsg("新增涉及线路案件失败！");
				}
			}
			//编辑
			else{
				boolean bool = updateRelatedEvents(relatedEvents);
				if(bool){
					resultData.setPrimaryKey(relatedEvents.getReId());
					resultData.setResultCode(1);
					resultData.setResultMsg("保存涉及线路案件成功！");
				}else{
					resultData.setResultMsg("保存涉及线路案件失败！");
				}
			}
			return resultData;
		}catch(Exception e){
			resultData.setResultMsg("保存失败," + e.getMessage());
		}
		return resultData;
	}
	
//	删除
	public ResultData deleterelated(String reId){
		ResultData resultData = new ResultData();
		try{
			if(reId == null || "".equals(reId)){
				resultData.setResultMsg("主键[reId]不能为空!");
				return resultData;
			}
			String[] idstr = reId.split(",");
			//根据id的数目判断是否批量删除
			if(idstr.length == 1){
				Long id = Long.parseLong(reId);
				boolean bool = deleteRelatedEventsById(id);
				if(bool){
					resultData.setResultCode(1);
					resultData.setResultMsg("删除主键为："+ reId +"的涉及线路案件成功！");
				}else{
					resultData.setResultMsg("删除主键为："+ reId +"的涉及线路案件失败！");
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
					resultData.setResultMsg("删除涉及线路案件成功" + result);
				}else{
					resultData.setResultMsg("批量删除涉及线路案件失败");
				}
			}
			return resultData;
		}catch (Exception e) {
			resultData.setResultMsg("删除失败，" + e.getMessage());
		}
		return resultData;
	}
//	详情
	public ResultData detailrelated(Long reId, String orgCode){
		ResultData resultData = new ResultData();
		try{
			if(reId == null || "".equals(reId)){
				resultData.setResultMsg("主键[reId]不能为空!");
				return resultData;
			}
			if(orgCode == null || "".equals(orgCode)){
				resultData.setResultMsg("orgCode不能为空");
				return resultData;
			}
			RelatedEvents relatedEvents = findRelatedEventsById(reId,orgCode);
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("result",relatedEvents);
			resultData.setResultData(map);
			resultData.setResultCode(1);
			resultData.setResultMsg("请求成功");
		}catch (Exception e) {
			resultData.setResultMsg("获取"+ reId + "案件失败，" + e.getMessage());
		}
		return resultData;
	}

	public ResultData detailrelatedByPeopleId(Map<String,Object> params){
		Long peopleId = Long.parseLong(params.get("peopleId").toString());
		ResultData resultData = new ResultData();
		try{
			if(peopleId == null || "".equals(peopleId)){
				resultData.setResultMsg("主键[peopleId]不能为空!");
				return resultData;
			}
			RelatedEvents relatedEvents = relatedEventsMapper.findByPeopleId(peopleId);
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("result",relatedEvents);
			resultData.setResultData(map);
			resultData.setResultCode(1);
			resultData.setResultMsg("请求成功");
		}catch (Exception e) {
			resultData.setResultMsg("获取受害人或嫌疑人"+peopleId+"对应的案件失败，" + e.getMessage());
		}
		return resultData;
	}
	
//	列表
	public ResultData listrelated(int pageNo,int pageSize, Map<String, Object> params, String orgCode){
		ResultData resultData = new ResultData();
		try{
			if (StringUtil.isBlank(params.get("gridId").toString())) {
				resultData.setResultMsg("网格ID不能为空!");
				return resultData;
			}
			if (StringUtil.isBlank(params.get("gridCode").toString())) {
				resultData.setResultMsg("网格编码不能为空!");
				return resultData;		
			}
			if (StringUtil.isBlank(params.get("bizType").toString())) {
				resultData.setResultMsg("案件类型不能为空!");
				return resultData;
			}
			if (!"1".equals(params.get("bizType"))) {
				resultData.setResultMsg("案件类型为非涉及线路案件!");
				return resultData;
			}else{
				EUDGPagination eUDGPagination  = findRelatedEventsPagination(pageNo,pageSize,params,orgCode);
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("total", eUDGPagination.getTotal());
				map.put("result", eUDGPagination.getRows());
				resultData.setResultData(map);
				resultData.setResultCode(1);
				resultData.setResultMsg("请求成功");
			}
			return resultData;
		}catch (Exception e) {
			resultData.setResultMsg("获取列表失败," + e.getMessage());
		}
		return resultData;
		
	}

	// 命案防控对外接口 （新增，更新）
		public ResultData save(RelatedEvents relatedEvents, String orgCode) {
			ResultData resultData = new ResultData();
			// 1.验证基本数据是否正确。
			try {
				// 1.1 先验证基本信息。
				CommonValidator.validate(relatedEvents);
				/*
				 * if(record.getCareGroupMembers()!=null &&
				 * record.getCareGroupMembers().size()>0){ for (CareGroupMember
				 * careGroupMember:record.getCareGroupMembers()) {
				 * CommonValidator.validate(careGroupMember); } }
				 */
				// 验证案件信息（没有就新增）
				if( !"4".equals(relatedEvents.getBizType())){
					resultData.setResultMsg("案件类型[bizType]类型应为命案防控");
					return resultData;
				}
				if (StringUtil.isBlank(relatedEvents.getReName())) {
					resultData.setResultMsg("案件名称不能为空!");
					return resultData;
				}
				if (orgCode == null || "".equals(orgCode)) {
					resultData.setResultMsg("保存命案防控失败！网格编码不可为空：" + orgCode);
					return resultData;
				}
				if (relatedEvents.getReName() == null
						|| "".equals(relatedEvents.getReName())) {
					resultData.setResultMsg("保存命案防控信息失败！案件名称不可为空：" + relatedEvents.getReName());
					return resultData;
				}
				
				MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(orgCode);
				if (gridInfo == null) {
					resultData.setResultMsg("保存命案防控信息失败！找不到该网格：" + gridInfo);
					return resultData;
				}
				if(relatedEvents.getReId()==null){
					Long insertRelatedEvents = insertRelatedEvents(relatedEvents);
					if(insertRelatedEvents>0){
						resultData.setPrimaryKey(insertRelatedEvents);
						resultData.setResultCode(1);
						resultData.setResultMsg("保存命案防控信息成功！");
					}else{
						resultData.setResultMsg("保存命案防控信息失败！");
					}
				
				}else{
					boolean updateRelatedEvents = updateRelatedEvents(relatedEvents);
					if(updateRelatedEvents){
						resultData.setPrimaryKey(relatedEvents.getReId());
						resultData.setResultCode(1);
						resultData.setResultMsg("更新命案防控信息成功！");
					}else{
						resultData.setResultMsg("更新命案防控信息成功！");
					}
				}
			
				/*
				 * //2. 查询该人员是否存在 //2.1不存在新增 //2.2存在修改 Long cirsId =
				 * getCiRsId(record); //3. 通过身份证查询 是否有该人员的精神病信息。 //3.1没有新增 //3.2有修改
				 * Long gridId = null; if(gridInfo!=null){ gridId =
				 * gridInfo.getGridId(); } Long miId =
				 * saveMentalIllenss(record,cirsId,gridId); if(miId>0){
				 * resultData.setPrimaryKey(miId); resultData.setResultCode(1);
				 * resultData.setResultMsg("保存重大案件信息成功！"); }else{
				 * resultData.setResultMsg("保存重大案件信息成功！"); }
				 */
				return resultData;
				// } catch (DataFormatException e) {
			} catch (Exception e) {
				resultData.setResultMsg(e.getMessage());
			}
			return resultData;
		}
		// 对外命案防控删除接口
				public ResultData delete(Map<String,String> params) {
					String bizType = "4"; //命案防控案件
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
								resultData.setResultMsg("请求全量删除成功!");
								return resultData;
							} else {
								resultData.setResultMsg("请求全量删除失败 sql运行出现错误!");
								return resultData;
							}
						}catch (Exception e) {
							e.printStackTrace();
							resultData.setResultMsg("请求删除失败!");
							return resultData;
						}
					}else {
						String[] ids = idStr.split(",");
						List<Long> involvedIdList = new ArrayList<Long>();
						if (StringUtils.isEmpty(idStr)) {
							resultData.setResultMsg("主键id不能为空");
							return resultData;
						}
						try {
							if (ids.length == 1) {
								boolean deleteRelatedEventsById = delRelatedEventsByIdAndGirdCode(Long
										.parseLong(ids[0]),gridCode,bizType);
								if (deleteRelatedEventsById){
									resultData.setResultCode(1);
									resultData.setResultMsg("请求删除成功!");
									return resultData;
								}else {
									resultData.setResultMsg("请求删除失败! 可能原因：本区域内无此条记录、此记录已被删除");
									return resultData;
								}
							} else {
								for (int index = 0, length = ids.length; index < length; index++) {
									long involvedId = Long.parseLong(ids[index]);
									involvedIdList.add(involvedId);
								}
								int iniSize = involvedIdList.size(); //批量删除原始长度

								int deleteRelatedEventsByIds = delRelatedEventsByIdsAndGridCode(involvedIdList,gridCode,bizType);
								if (deleteRelatedEventsByIds>0){
									resultData.setResultCode(1);
									resultData.setResultMsg("请求删除成功："+deleteRelatedEventsByIds+"条，失败："+(iniSize-deleteRelatedEventsByIds)+"条。");
									return resultData;
								}else {
									resultData.setResultMsg("请求删除失败! sql语句执行出现错误(可能无此条数据或此数据已经被删除)");
									return resultData;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							resultData.setResultMsg("删除命案防控信息失败");
							return resultData;
						}
					}
				}
				/*
				 * 9+X命案防控接口（详情）
				 */
				@Override
				public ResultData detail(Long id,String orgcode){
					ResultData resultData = new ResultData();
					
				  if(StringUtils.isEmpty(orgcode)){
						resultData.setResultMsg("地域编码不能为空" );
						return resultData;  
				  }
				  if(id==0||id==null){
						resultData.setResultMsg("主键id不能为空" );
						return resultData;    
				  }
					try{
						RelatedEvents findRelatedEventsById = findRelatedEventsById(id,orgcode);
						Map<String, Object> map = new HashMap<String,Object>();
						map.put("result",findRelatedEventsById);
						resultData.setResultMsg("请求成功!");
						resultData.setResultCode(1);
						resultData.setResultData(map);
				       } catch (Exception e) {
				    		e.printStackTrace();
				    		resultData.setResultMsg("请求失败!");
				       }
					
				  return resultData;
				}

		
				
				
				//9+x命案防控嫌疑人（受害人） 保存和更新
				public ResultData savePeople(InvolvedPeople involvedPeople) {

					ResultData resultData = new ResultData();
					// 1.验证基本数据是否正确。
					try {
						// 1.1 先验证基本信息。
						CommonValidator.validate(involvedPeople);
						/*
						 * if(record.getCareGroupMembers()!=null &&
						 * record.getCareGroupMembers().size()>0){ for (CareGroupMember
						 * careGroupMember:record.getCareGroupMembers()) {
						 * CommonValidator.validate(careGroupMember); } }
						 */
						// 验证案件信息（没有就新增）
						if (involvedPeople.getBizId() == null || "".equals(involvedPeople.getBizId())) {
							resultData.setResultMsg("命案防控id不可为：" + involvedPeople.getBizId());
							return resultData;
						}
						if (involvedPeople.getBizType() == null
								|| "".equals(involvedPeople.getBizType())) {
							resultData.setResultMsg("保存命案嫌疑人（受害人）信息失败！业务类型不可为空：" + involvedPeople.getBizType());
							return resultData;
						}
						if( (!"04".equals(involvedPeople.getBizType().toString()))&& !"03".equals(involvedPeople.getBizType().toString())){
							resultData.setResultMsg("业务类型非命案防控受害人(嫌疑人)");
							return resultData;
						}
						if (StringUtil.isBlank(involvedPeople.getName())) {
							resultData.setResultMsg("姓名不能为空!");
							return resultData;
						}
						if (StringUtil.isBlank(involvedPeople.getCardType())) {
							resultData.setResultMsg("证件类型不能为空!");
							return resultData;
						}
						if (StringUtil.isBlank(involvedPeople.getIdCard())) {
							resultData.setResultMsg("证件号码不能为空!");
							return resultData;
						}
						if (StringUtil.isBlank(involvedPeople.getSex())) {
							resultData.setResultMsg("性别不能为空!");
							return resultData;
						}
						if (involvedPeople.getBirthdayStr()==null) {
							resultData.setResultMsg("出生日期不能为空!");
							return resultData;
						}
						if(StringUtil.isBlank(involvedPeople.getReOrgCode())){
							resultData.setResultMsg("户籍地不能为空!");
							return resultData;
						}
						if(involvedPeople.getIpId()==null){
							Long insertInvolvedPeople = involvedPeopleService.insertInvolvedPeople(involvedPeople);
							if(insertInvolvedPeople>0){
								resultData.setPrimaryKey(insertInvolvedPeople);
								resultData.setResultCode(1);
								resultData.setResultMsg("保存命案防控嫌疑人（受害人）信息成功！");
							}else{
								resultData.setResultMsg("保存命案防控嫌疑人（受害人）信息成功！");
							}
						
						}else{
							boolean updateInvolvedPeople = involvedPeopleService.updateInvolvedPeople(involvedPeople);
							if(updateInvolvedPeople){
								resultData.setPrimaryKey(involvedPeople.getIpId());
								resultData.setResultCode(1);
								resultData.setResultMsg("更新命案防控信息成功！");
							}else{
								resultData.setResultMsg("更新命案防控信息成功！");
							}
						}
					
						/*
						 * //2. 查询该人员是否存在 //2.1不存在新增 //2.2存在修改 Long cirsId =
						 * getCiRsId(record); //3. 通过身份证查询 是否有该人员的精神病信息。 //3.1没有新增 //3.2有修改
						 * Long gridId = null; if(gridInfo!=null){ gridId =
						 * gridInfo.getGridId(); } Long miId =
						 * saveMentalIllenss(record,cirsId,gridId); if(miId>0){
						 * resultData.setPrimaryKey(miId); resultData.setResultCode(1);
						 * resultData.setResultMsg("保存重大案件信息成功！"); }else{
						 * resultData.setResultMsg("保存重大案件信息成功！"); }
						 */
						return resultData;
						// } catch (DataFormatException e) {
					} catch (Exception e) {
						resultData.setResultMsg(e.getMessage());
					}
					return resultData;
				
					
				}
				//9+x命案防控嫌疑人（受害人）详情
				
				public ResultData detailPeople(Long ipId){
					ResultData resultData = new ResultData();
					try{
						if(ipId == null || "".equals(ipId)){
							resultData.setResultMsg("主键[id]不能为空!");
							return resultData;
						}
						
						InvolvedPeople findInvolvedPeopleById = involvedPeopleService.findInvolvedPeopleById(ipId);
						Map<String, Object> map = new HashMap<String,Object>();
						map.put("result",findInvolvedPeopleById);
						resultData.setResultData(map);
						resultData.setResultCode(1);
						resultData.setResultMsg("请求成功");
					}catch (Exception e) {
						resultData.setResultMsg("获取"+ ipId + "受害人（嫌疑人）信息失败" + e.getMessage());
					}
					return resultData;
				}
                 //9+x命案防控嫌疑人（受害人）删除
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
						resultData.setResultMsg("此接口不支持全量删除");
						return resultData;
					}else {
						String[] ids = ipId.split(",");
						List<Long> involvedIdList = new ArrayList<Long>();
						if (StringUtils.isEmpty(ipId)) {
							resultData.setResultMsg("主键[idStr]不能为空!");
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
								resultData.setResultMsg("请求删除成功："+deleteInvolvedPeopleById+"条，失败："+(ipIdSize-deleteInvolvedPeopleById)+"条。");
								return resultData;
							}else {
								resultData.setResultMsg("请求删除失败! sql已经运行：可能由于数据之前已经为删除标识或此人未挂在案件上");
								return resultData;
							}
						} catch (Exception e) {
							resultData.setResultMsg("删除失败，" + e.getMessage());
						}
						return resultData;
					}
				}
				//9+x命案防控嫌疑人（受害人）列表
				public ResultData listpeople( InvolvedPeople involvedPeople){
					ResultData resultData = new ResultData();
					try{
						
						if (StringUtil.isBlank(involvedPeople.getBizId().toString())) {
							resultData.setResultMsg("命案防控ID不能为空!");
							return resultData;
						}
						if (StringUtil.isBlank(involvedPeople.getBizType().toString())) {
							resultData.setResultMsg("业务类型不能为空!");
							return resultData;		
						}
					
						if( (!"04".equals(involvedPeople.getBizType().toString()))&& !"03".equals(involvedPeople.getBizType().toString())){
							resultData.setResultMsg("业务类型非命案防控受害人(嫌疑人)");
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
						resultData.setResultMsg("获取列表失败," + e.getMessage());
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
