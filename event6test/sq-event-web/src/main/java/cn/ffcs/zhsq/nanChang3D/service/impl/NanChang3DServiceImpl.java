package cn.ffcs.zhsq.nanChang3D.service.impl;

import cn.ffcs.common.DictPcode;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.Base64;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPoint;
import cn.ffcs.zhsq.mybatis.persistence.nanChang3D.NanChang3DMapper;
import cn.ffcs.zhsq.nanChang3D.service.INanChang3DService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service(value="nanChang3DService")
public class NanChang3DServiceImpl implements INanChang3DService{
	
	@Autowired
	private NanChang3DMapper nanChang3DMapper;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	
	//@Autowired
	//private IBaseDictionaryService dictionaryService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;


	@Override
	public List<Map<String, Object>> findMultipleCountiesData(Map<String, Object> params) {
		
		//多发地区地区
		List<Map<String, Object>> findMultipleCountiesData = null;
		//多发地区当天数据
		List<Map<String, Object>> findMultipleCountiesDataByDayAndRealTime = null;
		
		String dateType = params.get("dateType").toString();
		String eventDateBegin = params.get("eventDateBegin").toString();
		String eventDateEnd = params.get("eventDateEnd").toString();
		
		
		if("year".equals(dateType)) {
			params.put("eventDateBegin", eventDateBegin.substring(0, 7));
			params.put("eventDateEnd", eventDateEnd.substring(0, 7));
			findMultipleCountiesData = nanChang3DMapper.findMultipleCountiesData(params);
			if(eventDateEnd.substring(0,4).equals(DateUtils.formatDate(new Date(), "yyyy"))) {
				params.put("eventDateBegin", eventDateEnd);
				params.put("eventDateEnd", eventDateEnd);
				findMultipleCountiesDataByDayAndRealTime = nanChang3DMapper.findMultipleCountiesDataByDayAndRealTime(params);
				if(findMultipleCountiesDataByDayAndRealTime.size() > 0) {
					return sortOrderByTotalDesc(findMultipleCountiesData, findMultipleCountiesDataByDayAndRealTime,"INFO_ORG_CODE");
				}
			}
		}else if("month".equals(dateType)) {
			params.put("eventDateBegin", eventDateBegin.substring(0, 7));
			params.put("eventDateEnd", eventDateEnd.substring(0, 7));
			findMultipleCountiesData = nanChang3DMapper.findMultipleCountiesData(params);
			if(eventDateEnd.substring(0,7).equals(DateUtils.formatDate(new Date(), "yyyy-MM"))) {
				params.put("eventDateBegin", eventDateEnd);
				params.put("eventDateEnd", eventDateEnd);
				findMultipleCountiesDataByDayAndRealTime = nanChang3DMapper.findMultipleCountiesDataByDayAndRealTime(params);
				if(findMultipleCountiesDataByDayAndRealTime.size() > 0) {
					return sortOrderByTotalDesc(findMultipleCountiesData, findMultipleCountiesDataByDayAndRealTime,"INFO_ORG_CODE");
				}
			}
		}else if("day".equals(dateType)) {
			findMultipleCountiesData = nanChang3DMapper.findMultipleCountiesDataByDay(params);
			if(eventDateEnd.equals(DateUtils.formatDate(new Date(), DateUtils.PATTERN_DATE))) {
				findMultipleCountiesDataByDayAndRealTime = nanChang3DMapper.findMultipleCountiesDataByDayAndRealTime(params);
				if(findMultipleCountiesDataByDayAndRealTime.size() > 0) {
					return sortOrderByTotalDesc(findMultipleCountiesData, findMultipleCountiesDataByDayAndRealTime,"INFO_ORG_CODE");
				}
			}
		}
		return findMultipleCountiesData;
	}
	
	@Override
	public List<Map<String, Object>> findPointData(Map<String, Object> params) {
		
		List<Map<String, Object>> findPointData = null;
		List<Map<String, Object>> findPointDataByDayRealTime = null;
		String dateType = params.get("dateType").toString();
		String date_ = params.get("date_").toString();
		
		if("year".equals(dateType)) {
			findPointData = nanChang3DMapper.findPointData(params);
			if(date_.equals(DateUtils.formatDate(new Date(), "yyyy"))) {
				params.put("date_", DateUtils.formatDate(new Date(), "yyyyMMdd"));
			    findPointDataByDayRealTime = nanChang3DMapper.findPointDataByDayRealTime(params);
				if(findPointDataByDayRealTime.size() > 0) {
					return sortOrderByTotalDesc(findPointData, findPointDataByDayRealTime,"POINT_ID");
				}
			}
		}else if("month".equals(dateType)) {
			findPointData = nanChang3DMapper.findPointData(params);
			if(date_.equals(DateUtils.formatDate(new Date(), "yyyyMM"))) {
				params.put("date_", DateUtils.formatDate(new Date(), "yyyyMMdd"));
			    findPointDataByDayRealTime = nanChang3DMapper.findPointDataByDayRealTime(params);
				if(findPointDataByDayRealTime.size() > 0) {
					return sortOrderByTotalDesc(findPointData, findPointDataByDayRealTime,"POINT_ID");
				}
			}
		}else if("day".equals(dateType)) {
			findPointData = nanChang3DMapper.findPointDataByDay(params);
			if(date_.equals(DateUtils.formatDate(new Date(), "yyyyMMdd"))) {
			    findPointDataByDayRealTime = nanChang3DMapper.findPointDataByDayRealTime(params);
				if(findPointDataByDayRealTime.size() > 0) {
					return sortOrderByTotalDesc(findPointData, findPointDataByDayRealTime,"POINT_ID");
				}
			}
		}
		return findPointData;
	}
	@Override
	public List<Map<String, Object>> findCivilizedPerInfoData(Map<String, Object> params) {
		return nanChang3DMapper.findCivilizedPerInfoData(params);
	}

	@Override
	public Map<String, Object> getMontiorStat(Map<String, Object> params) {
		return nanChang3DMapper.getMontiorStat(params);
	}

	@Override
	public Map<String, Object> getPointStat(Map<String, Object> params) {
		return nanChang3DMapper.getPointStat(params);
	}
	
	@Override
	public List<ArcgisInfoOfPoint> getPointGisByOrgCode(Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds(1, 100000);
		List<ArcgisInfoOfPoint> list=nanChang3DMapper.getPointGisByOrgCode(params,rowBounds);
		for(ArcgisInfoOfPoint p:list){
			changeCode(p);
		}
		return list;
	}
	

	
	@Override
	public EUDGPagination findPointListByParams( Integer pageNo, Integer pageSize,
			Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		EUDGPagination pagination = new EUDGPagination();
		Integer count =nanChang3DMapper.countPointGisByOrgCode(params);
		if(count>0) {
			List<ArcgisInfoOfPoint> list =nanChang3DMapper.getPointGisByOrgCode(params,rowBounds);
			for(ArcgisInfoOfPoint p:list){
				changeCode(p);
			}
			pagination = new EUDGPagination(count, list);
		}
		
		return pagination;
	}
	
	/**
	 * 字典翻译
	 * @return
	 */
	private ArcgisInfoOfPoint changeCode(ArcgisInfoOfPoint bo) {
		//点位类型
		List<BaseDataDict> pointTypeDC = baseDictionaryService.getDataDictTree(DictPcode.POINT_TYPE, null);
		if(!StringUtils.isBlank(bo.getPointType())){
			for(BaseDataDict b:pointTypeDC){
				if(bo.getPointType().equals(b.getDictGeneralCode()) && 5==b.getDictLevel()){
					bo.setPointTypeStr(b.getDictName());
				}
			}
		}
		return bo;
	}
	@Override
	public Map<String, Object> findHeatWeights(Map<String, Object> params) {
		String date_ = params.get("date_").toString();
		String dateType = params.get("dateType").toString();
		List<Map<String,Object>> list = new  ArrayList<Map<String,Object>>();
		if("day".equals(dateType)) {
			if(DateUtils.getToday("yyyyMMdd").equals(date_)) {	
				 list=	nanChang3DMapper.findHeatWeightSrealTime(params);
			}else {
				list=nanChang3DMapper.findHeatWeightsDay(params);
			}
		}else if("month".equals(dateType)) {
			list=nanChang3DMapper.findHeatWeights(params);
			if(DateUtils.getToday("yyyyMM").equals(date_)) {	
			List<Map<String,Object>> list2	=	nanChang3DMapper.findHeatWeightSrealTime(params);	
			if( list2!=null  &&  list2.size() > 0) {
				   list=statistics(list, list2, "POINT_ID","TOTAL");
				}
			}
		}else if("year".equals(dateType)) {
			list=nanChang3DMapper.findHeatWeights(params);
			if(DateUtils.getToday("yyyy").equals(date_)) {	
			   List<Map<String,Object>> list2=	nanChang3DMapper.findHeatWeightSrealTime(params);
			   if( list2!=null  &&  list2.size() > 0) {
				   list=statistics(list, list2, "POINT_ID","TOTAL");
				}
			}		
		}
		params.put("MAX", getMAX(list,"TOTAL"));
		return params;
	}
	
	@Override
	public Map<String, Object> findGeometryData(Map<String, Object> params) {
		
		String dateType = params.get("dateType").toString();
		List<Map<String,Object>> list = new  ArrayList<Map<String,Object>>();
		String beginTime = params.get("beginTime").toString();
		String endTime = params.get("endTime").toString();
		if("day".equals(dateType)) {
			if(DateUtils.getToday("yyyy-MM-dd").equals(endTime)) {	
				 list=	nanChang3DMapper.findDCGridXY(params);
				List<Map<String,Object>> list2= nanChang3DMapper.findGeometryDataSrealTime(params);
				for (int i = 0,z=list.size(); i < z; i++) {
					for (int j = 0,y = list2.size(); j < y ; j++) {
						if(list.get(i).get("INFO_ORG_CODE").equals(list2.get(j).get("INFO_ORG_CODE"))) {
							list.get(i).put("TOTAL", list2.get(j).get("TOTAL")==null?0:list2.get(j).get("TOTAL"));

						}
					}
				} 
				params.put("max", getMAX(list,"TOTAL"));
			}else {
				list=nanChang3DMapper.findGeometryDataDay(params);
				 params.put("max", nanChang3DMapper.findGeometryMaxDay(params).get("MAX"));
			}
		}else if("month".equals(dateType)) {
			params.put("beginTime", beginTime.substring(0, 7));
			params.put("endTime", endTime.substring(0, 7));
			list=nanChang3DMapper.findGeometryData(params);
			if(DateUtils.getToday("yyyy-MM").equals(endTime.substring(0, 7))) {	
				List<Map<String,Object>> list2= nanChang3DMapper.findGeometryDataSrealTime(params);
			if( list2!=null  &&  list2.size() > 0) {
				   list=statistics(list, list2, "INFO_ORG_CODE","TOTAL");
				   params.put("max", getMAX(list,"TOTAL"));
				}
			}
			 params.put("max",params.get("max")!=null?params.get("max"):nanChang3DMapper.findGeometryMax(params).get("MAX"));
			
		}else if("year".equals(dateType)) {
			params.put("beginTime", beginTime.substring(0, 7));
			params.put("endTime", endTime.substring(0, 7));
			list=nanChang3DMapper.findGeometryData(params);
			if(DateUtils.getToday("yyyy").equals(endTime.substring(0, 4))) {	
				List<Map<String,Object>> list2= nanChang3DMapper.findGeometryDataSrealTime(params);
			   if( list2!=null  &&  list2.size() > 0) {
				   list=statistics(list, list2, "INFO_ORG_CODE","TOTAL");
				   params.put("max", getMAX(list,"TOTAL"));
				}
			}	
			 params.put("max",params.get("max")!=null?params.get("max"):nanChang3DMapper.findGeometryMax(params).get("MAX"));
		}
		params.put("list", list);
		
		return params;
	}

	
	

	public Map<String, Object> findGeometryPoint(Map<String, Object> params){
		
		 List<Map<String,Object>> list=nanChang3DMapper.findPointGisBy(params);
		 List<Map<String, Object>> findPointData = new  ArrayList<Map<String,Object>>();
		 List<Map<String, Object>> findPointDataByDayRealTime = new  ArrayList<Map<String,Object>>();
			String dateType = params.get("dateType").toString();
			String date_ = params.get("date_").toString();
			if("year".equals(dateType)) {
				findPointData = nanChang3DMapper.findPointData(params);
				if(date_.equals(DateUtils.formatDate(new Date(), "yyyy"))) {
					params.put("date_", DateUtils.formatDate(new Date(), "yyyyMMdd"));
				    findPointDataByDayRealTime = nanChang3DMapper.findPointDataByDayRealTime(params);
					if(findPointDataByDayRealTime.size() > 0) {
						findPointData=statistics(findPointData, findPointDataByDayRealTime, "POINT_ID","TOTAL_");
					}
				}
			}else if("month".equals(dateType)) {
				 findPointData = nanChang3DMapper.findPointData(params);
				if(date_.equals(DateUtils.formatDate(new Date(), "yyyyMM"))) {
					params.put("date_", DateUtils.formatDate(new Date(), "yyyyMMdd"));
				    findPointDataByDayRealTime = nanChang3DMapper.findPointDataByDayRealTime(params);
					if(findPointDataByDayRealTime.size() > 0) {
					findPointData=statistics(findPointData, findPointDataByDayRealTime, "POINT_ID","TOTAL_");
					}
				}
			}else if("day".equals(dateType)) {
				findPointData = nanChang3DMapper.findPointDataByDay(params);
				if(date_.equals(DateUtils.formatDate(new Date(), "yyyyMMdd"))) {
				    findPointDataByDayRealTime = nanChang3DMapper.findPointDataByDayRealTime(params);
					if(findPointDataByDayRealTime.size() > 0) {
						findPointData=statistics(findPointData, findPointDataByDayRealTime, "POINT_ID","TOTAL_");
					}
				}
			}
		   params.put("max", getMAX(findPointData,"TOTAL_"));
		   params.put("list", pointCombine(list, findPointData, "POINT_ID"));
		return params;

	}
	
	
	
	
	
	@Override
	public Map<String, Object> getEventStat(Map<String, Object> params) {
		String dateType = params.get("dateType").toString();
		String eventDateBegin = params.get("eventDateBegin").toString();
		String eventDateEnd = params.get("eventDateEnd").toString();
		Map<String, Object> dataMap=null;
		Map<String, Object> dataMapByDayAndRealTime=null;
		if("year".equals(dateType)) {
			params.put("eventDateBegin", eventDateBegin.substring(0, 7));
			params.put("eventDateEnd", eventDateEnd.substring(0, 7));
			dataMap = nanChang3DMapper.findEventStat(params);
			if(eventDateEnd.substring(0,4).equals(DateUtils.formatDate(new Date(), "yyyy"))) {
				params.put("eventDateBegin", eventDateEnd);
				params.put("eventDateEnd", eventDateEnd);
				dataMapByDayAndRealTime = nanChang3DMapper.findEventStatByDayAndRealTime(params);
				if(dataMap!=null&&dataMapByDayAndRealTime!=null) {
					dataMap.put("TOTAL_", Integer.valueOf(dataMap.get("TOTAL_")+"")+ Integer.valueOf(dataMapByDayAndRealTime.get("TOTAL_")+""));
					dataMap.put("SETTLES_TOTAL_", Integer.valueOf(dataMap.get("SETTLES_TOTAL_")+"")+ Integer.valueOf(dataMapByDayAndRealTime.get("SETTLES_TOTAL_")+""));
					dataMap.put("DOING_TOTAL_", Integer.valueOf(dataMap.get("TOTAL_")+"")- Integer.valueOf(dataMap.get("SETTLES_TOTAL_")+""));
				}
			}
		}else if("month".equals(dateType)) {
			params.put("eventDateBegin", eventDateBegin.substring(0, 7));
			params.put("eventDateEnd", eventDateEnd.substring(0, 7));
			dataMap = nanChang3DMapper.findEventStat(params);
			if(eventDateEnd.substring(0,7).equals(DateUtils.formatDate(new Date(), "yyyy-MM"))) {
				params.put("eventDateBegin", eventDateEnd);
				params.put("eventDateEnd", eventDateEnd);
				dataMapByDayAndRealTime = nanChang3DMapper.findEventStatByDayAndRealTime(params);
				if(dataMap!=null&&dataMapByDayAndRealTime!=null) {
					dataMap.put("TOTAL_", Integer.valueOf(dataMap.get("TOTAL_")+"")+ Integer.valueOf(dataMapByDayAndRealTime.get("TOTAL_")+""));
					dataMap.put("SETTLES_TOTAL_", Integer.valueOf(dataMap.get("SETTLES_TOTAL_")+"")+ Integer.valueOf(dataMapByDayAndRealTime.get("SETTLES_TOTAL_")+""));
					dataMap.put("DOING_TOTAL_", Integer.valueOf(dataMap.get("TOTAL_")+"")- Integer.valueOf(dataMap.get("SETTLES_TOTAL_")+""));
				}
			}
		}else if("day".equals(dateType)) {
			dataMap = nanChang3DMapper.findEventStatByDay(params);
			if(eventDateEnd.equals(DateUtils.formatDate(new Date(), DateUtils.PATTERN_DATE))) {
				dataMapByDayAndRealTime = nanChang3DMapper.findEventStatByDayAndRealTime(params);
				if(dataMap!=null&&dataMapByDayAndRealTime!=null) {
					dataMap.put("TOTAL_", Integer.valueOf(dataMap.get("TOTAL_")+"")+ Integer.valueOf(dataMapByDayAndRealTime.get("TOTAL_")+""));
					dataMap.put("SETTLES_TOTAL_", Integer.valueOf(dataMap.get("SETTLES_TOTAL_")+"")+ Integer.valueOf(dataMapByDayAndRealTime.get("SETTLES_TOTAL_")+""));
					dataMap.put("DOING_TOTAL_", Integer.valueOf(dataMap.get("TOTAL_")+"")- Integer.valueOf(dataMap.get("SETTLES_TOTAL_")+""));
				}
			}
		}
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> findGridData(Map<String, Object> params) {
		return nanChang3DMapper.findGridData(params);
	}
	
	@Override
	public Map<String, Object> findGeometryMax(Map<String, Object> params) {
		
		if("day".equals(params.get("dateType").toString())) {
			return nanChang3DMapper.findGeometryMaxDay(params);
		}
		return nanChang3DMapper.findGeometryMax(params);
	}


	@Override
	public List<Map<String, Object>> sortOrderByTotalDesc(List<Map<String, Object>> list1,
			List<Map<String, Object>> list2,String key) {
		
		List<Map<String, Object>> resultListMap = new ArrayList<Map<String, Object>>();
		
		for (int i = 0,z=list1.size(); i < z; i++) {
			for (int j = 0,y = list2.size(); j < y ; j++) {
				if(list1.get(i).get(key).equals(list2.get(j).get(key))) {
					list1.get(i).put("TOTAL_", Integer.valueOf(list1.get(i).get("TOTAL_")+"") + Integer.valueOf(list2.get(j).get("TOTAL_")+""));
				}
			}
		}
		
		int listSize = list1.size();
		
		//存储数据在原先集合中的位置
		int[] arrayIndex = new int[listSize];
		//集合中数值
		int[] arrayData = new int[listSize];
		
		for (int i = 0; i < listSize; i++) {
			arrayIndex[i] = i;
			arrayData[i] = Integer.valueOf(list1.get(i).get("TOTAL_")+"");
		}
		
		for (int i = 0,l=arrayData.length; i < l; i++) {
			for (int j = 0,len=arrayData.length; j < len-i-1; j++) {
				if(arrayData[j] < arrayData[j + 1]){
					int temp = arrayData[j];
					arrayData[j] = arrayData[j + 1];
					arrayData[j + 1] = temp;
                   
					int index = arrayIndex[j];
                    arrayIndex[j] = arrayIndex[j + 1];
                    arrayIndex[j + 1] = index;
				}
			}
		}
		
		for(int i=0 ; i<listSize ; i++) {
			resultListMap.add(i, list1.get(arrayIndex[i]));
		}
		
		return resultListMap;
	}


	@Override
	public Map<String, Object> getPointGis(Map<String, Object> params) {
		return nanChang3DMapper.getPointGis(params);
	}

	@Override
	public String getPointCheckTypeById(Long pointId) {
		return nanChang3DMapper.getPointCheckTypeById(pointId);
	}
	
	 /**
     *  取最大值
     * @param str
     * @return
     */
    public static Integer getMAX(List<Map<String,Object>> list,String Field){
        
    	Integer i,max,value;
 
    	if(list==null  || list.size()==0 ) {
    		 return 0;
    	}
        max = Integer.valueOf(list.get(0).get(Field).toString());
        for(i=0;i<list.size();i++){
        	value=Integer.valueOf(list.get(i).get(Field).toString());
            if(value>max)   // 判断最大值
                max = value;
        }
        return max;
    }
	
    
    
    /**
     *  统计当天和以前的数据
     * @param 
     * @return
     */
    public static List<Map<String,Object>>  statistics( List<Map<String,Object>> list1,
    		    List<Map<String,Object>> list2,String key,String Field){
        
		for (int i = 0,z=list1.size(); i < z; i++) {
			for (int j = 0,y = list2.size(); j < y ; j++) {
				if(list1.get(i).get(key).equals(list2.get(j).get(key))) {
					list1.get(i).put(Field, Integer.valueOf(list1.get(i).get(Field)+"") + Integer.valueOf(list2.get(j).get(Field)+""));
				}
			}
		}
		return list1;
    }
	
    
    /**
     *  把点位坐标与点位事件数结合
     * @param 
     * @return
     */
    public static List<Map<String,Object>>  pointCombine( List<Map<String,Object>> list1,
    		    List<Map<String,Object>> list2,String key){
        
		for (int i = 0,z=list1.size(); i < z; i++) {
			for (int j = 0,y = list2.size(); j < y ; j++) {
				if(list1.get(i).get(key).equals(list2.get(j).get(key))) {
					list1.get(i).put("TOTAL", list2.get(j).get("TOTAL_"));
				}
			}
		}
		return list1;
    }
    
    
    @Override
	public EUDGPagination findLargeDataPlatformEvenListPagination(int pageNo, int pageSize, Map<String, Object> params)
			throws ZhsqEventException {
		Integer listType = 0;
		EUDGPagination eventPagination = null;
		
		formatParamIn(params);
		
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		switch(listType) {
			case 1: {//待办
				eventPagination = this.findEvent4TodoPagination(pageNo, pageSize, params); break;
			}
			default: {
				eventPagination = new EUDGPagination(0, new ArrayList<Map<String, Object>>()); break;
			}
		}
		
		return eventPagination;
	}
    
    

    /**
	 * 分页获取事件待办列表
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 					curUserId	当前办理人员
	 * 					curOrgId	当前办理组织
	 * @return
	 * @throws ZhsqEventException
	 */
	private EUDGPagination findEvent4TodoPagination(int pageNo,
			int pageSize, Map<String, Object> params) throws ZhsqEventException {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = 0;
		List<Map<String, Object>> eventList = null;
		
		count = nanChang3DMapper.findCount4Todo(params);
		
		if(count > 0) {
			eventList = nanChang3DMapper.findPageList4Todo(params, rowBounds);
		} else {
			eventList = new ArrayList<Map<String, Object>>();
		}
		
		formatMapDataOut(eventList, params);
		
		EUDGPagination eventPagination = new EUDGPagination(count, eventList);
		
		return eventPagination;
	}
	
	
	
	
	/**
	 * 格式化查询条件
	 * @param params
	 * @throws ZhsqEventException 
	 */
	@SuppressWarnings("unchecked")
	private void formatParamIn(Map<String, Object> params) throws ZhsqEventException {
		if(params != null && !params.isEmpty()) {
			//事件主表动态条件
			StringBuffer dynamicSql4Event = new StringBuffer("");
			List<String> defaultStatusList = new ArrayList<String>(),//列表默认状态
								statusList = null;
			Integer listType = 0;
			StringBuffer msgWrong = new StringBuffer("");
			
			if(CommonFunctions.isNotBlank(params, "listType")) {
				try {
					listType = Integer.valueOf(params.get("listType").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
			}
			
			switch(listType) {
				case 1: {//待办
					//curUserId、curOrgId查询时需要为字符类型
					if(CommonFunctions.isBlank(params, "curUserId") && CommonFunctions.isNotBlank(params, "userId")) {
						params.put("curUserId", params.get("userId").toString());
					}
					if(CommonFunctions.isBlank(params, "curOrgId") && CommonFunctions.isNotBlank(params, "orgId")) {
						params.put("curOrgId", params.get("orgId").toString());
					}
					
					if(CommonFunctions.isBlank(params, "curUserId")) {
						msgWrong.append("缺少当前办理人员id 【curUserId】！");
					}
					if(CommonFunctions.isBlank(params, "curOrgId")) {
						msgWrong.append("缺少当前办理组织id 【curOrgId】！");
					}
					break;
				}
			
			}
			
			//是否获取我督办过的事件
			if(CommonFunctions.isNotBlank(params, "isCapMySupervised")) {
				boolean isCapMySupervised = Boolean.valueOf(params.get("isCapMySupervised").toString());
				if(isCapMySupervised) {
					if(CommonFunctions.isBlank(params, "superviseUserId") && CommonFunctions.isNotBlank(params, "userId")) {
						params.put("superviseUserId", params.get("userId"));
					}
					
					if(CommonFunctions.isBlank(params, "superviseUserId")) {
						msgWrong.append("缺少事件督办操作人员用户id【superviseUserId】！");
					}
				}
				params.put("isCapMySupervised", isCapMySupervised);
			}
			
			if(msgWrong.length() > 0) {
				throw new ZhsqEventException(msgWrong.toString());
			}
			
			defaultStatusList.add(ConstantValue.EVENT_STATUS_RECEIVED);
			defaultStatusList.add(ConstantValue.EVENT_STATUS_REPORT);
			defaultStatusList.add(ConstantValue.EVENT_STATUS_DISTRIBUTE);
			defaultStatusList.add(ConstantValue.EVENT_STATUS_ARCHIVE);
			
			if(CommonFunctions.isNotBlank(params, "statusList")) {
				Object statusListObj = params.get("statusList");
				if(statusListObj instanceof List) {
					statusList = (List<String>) params.get("statusList");
				} else if(statusListObj instanceof String) {
					statusList = Arrays.asList(statusListObj.toString().split(","));
				}
			} else if(CommonFunctions.isNotBlank(params, "statusArray")) {
				Object statusArray = params.get("statusArray");
				if(statusArray instanceof String[]) {
					statusList = Arrays.asList((String[]) params.get("statusArray"));
				}
			} else if(CommonFunctions.isNotBlank(params, "status")) {
				statusList = Arrays.asList(params.get("status").toString().split(","));
			}
			
			if(statusList != null) {
				defaultStatusList.retainAll(statusList);
				params.put("statusList", defaultStatusList);
			} else {
				params.put("statusList", defaultStatusList);
			}
			if(CommonFunctions.isNotBlank(params, "collectWay")) {
				String collectWay = params.get("collectWay").toString();
				if(collectWay.contains(",")) {
					params.put("collectWayArray", collectWay.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "influenceDegree")) {
				String influenceDegree = params.get("influenceDegree").toString();
				if(influenceDegree.contains(",")) {
					params.put("influenceDegreeArray", influenceDegree.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "urgencyDegree")) {
				String urgencyDegree = params.get("urgencyDegree").toString();
				if(urgencyDegree.contains(",")) {
					params.put("urgencyDegreeArray", urgencyDegree.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "source")) {
				String source = params.get("source").toString();
				if(source.contains(",")) {
					params.put("sourceArray", source.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "patrolType")) {
				String patrolType = params.get("patrolType").toString();
				if(patrolType.contains(",")) {
					params.put("patrolTypeArray", patrolType.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "supervisionType")) {
				String supervisionType = params.get("supervisionType").toString();
				if(supervisionType.contains(",")) {
					params.put("supervisionTypeArray", supervisionType.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "attrFlag")) {
				String attrFlag = params.get("attrFlag").toString();
				
				if(!attrFlag.contains(",%")) {
					attrFlag = attrFlag.replaceAll(",", ",%");
				}
				
				params.put("attrFlag", attrFlag);
			}
			if(CommonFunctions.isNotBlank(params, "evaLevelList")) {
				Object evaLevelListObj = params.get("evaLevelList");
				if(evaLevelListObj instanceof String) {
					params.put("evaLevelList", Arrays.asList(evaLevelListObj.toString().split(","))); 
				}
			}
			if(CommonFunctions.isNotBlank(params, "bizPlatform")) {
				String bizPlatform = params.get("bizPlatform").toString();
				
				if(bizPlatform.contains(",")) {
					params.put("bizPlatformArray", bizPlatform.split(","));
					params.remove("bizPlatform");
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "bizplatformForQuery")) {
				String bizPlatform = params.get("bizplatformForQuery").toString();
				
				if(bizPlatform.contains(",")) {
					params.put("bizplatformForQueryArray", bizPlatform.split(","));
					params.remove("bizplatformForQuery");
				}
			}
			
			String orgCode = "";
			
			if(CommonFunctions.isNotBlank(params, "eventAttrOrgCode")){
				orgCode = params.get("eventAttrOrgCode").toString();
			} else if(CommonFunctions.isNotBlank(params, "orgCode")) {
				orgCode = params.get("orgCode").toString();
			}
			
			String removeBizplatform=funConfigurationService.changeCodeToValue(ConstantValue.EVENT_VERIFY_ATTRIBUTE,ConstantValue.REMOVE_BIZPLATFORM, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
			
			if(StringUtils.isNotBlank(removeBizplatform)) {
				
				params.put("removeBizplatformForVerifyArray", removeBizplatform.split(","));
			}
			
			if(CommonFunctions.isNotBlank(params, "eInfoOrgCode")) {//有加密的地域编码
				byte[] decodedData = Base64.decode(params.get("eInfoOrgCode").toString());
				params.put("infoOrgCode", new String(decodedData));
			}
			if(CommonFunctions.isNotBlank(params, "gridId") && CommonFunctions.isBlank(params, "infoOrgCode")) {
				Long gridId = null;
				
				try {
					gridId = Long.valueOf(params.get("gridId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if(gridId != null && gridId > 0) {
					MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
					if(gridInfo != null) {
						params.put("infoOrgCode", gridInfo.getInfoOrgCode());
					}
				}
			}
			if(CommonFunctions.isNotBlank(params, "eventIdList")) {
				Object eventIdListObj = params.get("eventIdList");
				String[] eventIdArray = null;
				List<Long> eventIdList = new ArrayList<Long>();
				
				if(eventIdListObj instanceof String) {
					eventIdArray = ((String) eventIdListObj).split(",");
				} if(eventIdListObj instanceof String[]) {
					eventIdArray = (String[])eventIdListObj;
				} else if(eventIdListObj instanceof List) {
					List<Object> eventIdObjList = (List<Object>)eventIdListObj;
					List<String> eventIdStrList = new ArrayList<String>();
					
					for(Object eventIdObj : eventIdObjList) {
						eventIdStrList.add(eventIdObj.toString());
					}
					
					eventIdArray = eventIdStrList.toArray(new String[eventIdStrList.size()]);
				}
				
				if(eventIdArray != null) {
					Long eventIdL = -1L;
					
					for(String eventId : eventIdArray) {
						if(StringUtils.isNotBlank(eventId)) {
							try {
								eventIdL = Long.valueOf(eventId);
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
							
							if(eventIdL != null && eventIdL > 0) {
								eventIdList.add(eventIdL);
							}
						}
					}
				}
				
				params.put("eventIdList", eventIdList);
			}
			if(CommonFunctions.isNotBlank(params, "eliminateEventIdList")) {
				Object eventIdListObj = params.get("eliminateEventIdList");
				String[] eventIdArray = null;
				List<Long> eventIdList = new ArrayList<Long>();
				
				if(eventIdListObj instanceof String) {
					eventIdArray = ((String) eventIdListObj).split(",");
				} if(eventIdListObj instanceof String[]) {
					eventIdArray = (String[])eventIdListObj;
				} else if(eventIdListObj instanceof List) {
					List<Object> eventIdObjList = (List<Object>)eventIdListObj;
					List<String> eventIdStrList = new ArrayList<String>();
					
					for(Object eventIdObj : eventIdObjList) {
						eventIdStrList.add(eventIdObj.toString());
					}
					
					eventIdArray = eventIdStrList.toArray(new String[eventIdStrList.size()]);
				}
				
				if(eventIdArray != null) {
					Long eventIdL = -1L;
					
					for(String eventId : eventIdArray) {
						if(StringUtils.isNotBlank(eventId)) {
							try {
								eventIdL = Long.valueOf(eventId);
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
							
							if(eventIdL != null && eventIdL > 0) {
								eventIdList.add(eventIdL);
							}
						}
					}
				}
				
				params.put("eliminateEventIdList", eventIdList);
			}
			//是否查询事件列表额外列
			if(CommonFunctions.isNotBlank(params, "isCapEventAdditionalColumn")) {
				params.put("isCapEventAdditionalColumn", Boolean.valueOf(params.get("isCapEventAdditionalColumn").toString()));
			}
			if(CommonFunctions.isNotBlank(params, "eventAttrTrigger")) {
				String eventAttrTrigger = params.get("eventAttrTrigger").toString(),
					   eventAttrName = "";
				
				eventAttrName = funConfigurationService.changeCodeToValue(ConstantValue.EVENT_ATTRIBUTE, eventAttrTrigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
				
				if(StringUtils.isNotBlank(eventAttrName)){
					String[] eventAttrNameArray = eventAttrName.split(",");
					String attrValue = "";
					
					for(String attrName : eventAttrNameArray){
						attrValue = funConfigurationService.changeCodeToValue(ConstantValue.EVENT_ATTRIBUTE, eventAttrTrigger+"_"+attrName, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
						
						if(StringUtils.isNotBlank(attrValue)){
							dynamicSql4Event.append(" AND T1.").append(attrName).append(" IN ('").append(attrValue.trim().replaceAll(",", "','")).append("')");
						} else {
							attrValue = funConfigurationService.changeCodeToValue(ConstantValue.EVENT_ATTRIBUTE, eventAttrTrigger+"_"+attrName+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
							
							if(StringUtils.isNotBlank(attrValue)) {
								dynamicSql4Event.append(" AND T1.").append(attrName).append(" NOT IN ('").append(attrValue.trim().replaceAll(",", "','")).append("')");
							}
						}
					}
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "type")) {
				String type = params.get("type").toString();
				
				if(type.contains(",")) {
					params.put("types", params.get("type"));
					params.remove("type");
				}
			}
			if(CommonFunctions.isNotBlank(params, "types") || CommonFunctions.isNotBlank(params, "trigger")) {
				boolean isRemoveTypes = false;
				String types = "";
				
				if(CommonFunctions.isNotBlank(params, "isRemoveTypes")) {//isRemoveTypes为true时，去除types中包含的事件类型；isRemoveTypes为false时，展示types中包含的事件类型
					isRemoveTypes = Boolean.valueOf(params.get("isRemoveTypes").toString());
				}
				
				if(CommonFunctions.isNotBlank(params, "types")) {
					types = params.get("types").toString().trim();
				} else if(CommonFunctions.isNotBlank(params, "trigger")) {
					String trigger = params.get("trigger").toString();
					
					types = funConfigurationService.changeCodeToValue(ConstantValue.TYPES, trigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
					
					if(StringUtils.isBlank(types)) {
						isRemoveTypes = true;
						types = funConfigurationService.changeCodeToValue(ConstantValue.TYPES, trigger+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
					}
				}
				
				if(StringUtils.isNotBlank(types)) {
					String[] typesArray = types.split(",");
					
					if(typesArray.length > 0) {
						if(isRemoveTypes) {
							for(String _type : typesArray) {
								if(StringUtils.isNotBlank(_type)) {
									dynamicSql4Event.append(" AND T1.TYPE_ NOT LIKE '").append(_type).append("%'");
								}
							}
						} else {
							dynamicSql4Event.append(" AND ( 1 != 1 ");
							for(String _type : typesArray) {
								if(StringUtils.isNotBlank(_type)) {
									dynamicSql4Event.append(" OR T1.TYPE_ LIKE '" + _type + "%'");
								}
							}
							dynamicSql4Event.append(") ");
						}
					}
				}
			}

			if(dynamicSql4Event.length() > 0) {
				params.put("dynamicSql4Event", dynamicSql4Event.toString());
			}
		}
	}
	
	
	
	/**
	 * 格式化输出数据
	 * @param eventList
	 * @param params
	 * 		listType	列表类型
	 * 			4		辖区归档列表
	 */
	private void formatMapDataOut(List<Map<String, Object>> eventList, Map<String, Object> params) {
		if(eventList != null && eventList.size() > 0) {
			Map<String, Object> dictMap = new HashMap<String, Object>();
			String userOrgCode = null, eventType = null;
			Integer listType = 0;
			List<BaseDataDict> influenceDegreeDictList = null,
							   urgencyDegreeDictList = null,
							   statusDictList = null,
							   subStatusDictList = null,
							   eventTypeDict = null,
							   evaLevelDictList = null,
							   sourceDictList = null,
							   collectWayDictList = null; 
			List<Node> workflowNodeList = null;
			boolean isCapCurNodeInfo = false;
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			} else if(CommonFunctions.isNotBlank(params, "orgCode")) {
				userOrgCode = params.get("orgCode").toString();
			}
			
			if(CommonFunctions.isNotBlank(params, "listType")) {
				try {
					listType = Integer.valueOf(params.get("listType").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			dictMap.put("orgCode", userOrgCode);
			dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
			
			influenceDegreeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.INFLUENCE_DEGREE_PCODE, userOrgCode);
			urgencyDegreeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.URGENCY_DEGREE_PCODE, userOrgCode);
			statusDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.STATUS_PCODE, userOrgCode);
			subStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SUB_STATUS_PCODE, userOrgCode);
			eventTypeDict = baseDictionaryService.findDataDictListByCodes(dictMap);
			
			switch(listType) {
				case 4: {//辖区归档事件列表
					evaLevelDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.EVALUATE_LEVEL_PCODE, userOrgCode);
					break;
				}
				case 5: {//辖区所有事件列表
					if(CommonFunctions.isNotBlank(params, "isCapCurNodeInfo")) {
						isCapCurNodeInfo = Boolean.valueOf(params.get("isCapCurNodeInfo").toString());
					}
					
					break;
				}
				case 9: {//辖区所有(展示当前环节)
					isCapCurNodeInfo = true;
					
					sourceDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SOURCE_PCODE, userOrgCode);
					collectWayDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.COLLECT_WAY_PCODE, userOrgCode);
					break;
				}
			}
			
			if(isCapCurNodeInfo) {
				Long workflowId = null;//为了让queryNodes找到对应的方法
				
				workflowNodeList = eventDisposalWorkflowService.queryNodes(workflowId);
			}
			
			for(Map<String, Object> eventMap : eventList) {
				userOrgCode = null; eventType = null; 
				
				// 设置事件分类的值 大类+小类
				if(CommonFunctions.isNotBlank(eventMap, "type")) {
					eventType = eventMap.get("type").toString();
				}
				if(CommonFunctions.isNotBlank(eventMap, "curNodeName") && workflowNodeList != null) {
					Long workflowId = -1L;
					String curNodeName = eventMap.get("curNodeName").toString();
					
					if(CommonFunctions.isNotBlank(eventMap, "workflowId")) {
						workflowId = Long.valueOf(eventMap.get("workflowId").toString());
					}
					
					//找到指定流程图中的节点，否则多张流程图中可能有同名的节点
					if(workflowId > 0) {
						for(Node workflowNode : workflowNodeList) {
							if(workflowId.equals(workflowNode.getWorkFlowId()) && curNodeName.equals(workflowNode.getNodeName())) {
								eventMap.put("curNodeNameZH", workflowNode.getNodeNameZH());
								break;
							}
						}
					}
				}
				
				if(StringUtils.isNotBlank(eventType) && eventTypeDict != null) {
					StringBuffer eventClass = new StringBuffer("");
					String bigType = eventType, bigTypeName = "", bigDictCode = null;
					
					do {//从最小类递归到最大类，中间使用"-"分隔
						bigTypeName = "";
						
						for(BaseDataDict dataDict : eventTypeDict) {
							if((StringUtils.isNotBlank(bigDictCode) 
									&& !ConstantValue.BIG_TYPE_PCODE.equals(bigDictCode) 
									&& bigDictCode.equals(dataDict.getDictCode()))
								||
								(StringUtils.isNotBlank(bigType) && bigType.equals(dataDict.getDictGeneralCode()))) {
								bigTypeName = dataDict.getDictName();
								bigDictCode = dataDict.getDictPcode();
								bigType = null;
								break;
							}
						}
						
						if(StringUtils.isNotBlank(bigTypeName)) {
							eventClass.insert(0, bigTypeName).insert(0, "-");
						}
					} while(StringUtils.isNotBlank(bigTypeName));
					
					if(eventClass.length() > 0) {
						eventMap.put("typeName", eventClass.substring(eventClass.lastIndexOf("-") + 1));
						eventMap.put("eventClass", eventClass.substring(1));
					}
				}
				
				// 影响范围
				DataDictHelper.setDictValueForField(eventMap, "influenceDegree", "influenceDegreeName", influenceDegreeDictList);
				
				// 紧急程度
				DataDictHelper.setDictValueForField(eventMap, "urgencyDegree", "urgencyDegreeName", urgencyDegreeDictList);
				
				// 事件状态
				DataDictHelper.setDictValueForField(eventMap, "status", "statusName", statusDictList);
				
				// 事件子状态
				DataDictHelper.setDictValueForField(eventMap, "subStatus", "subStatusName", subStatusDictList);
				
				if(CommonFunctions.isNotBlank(eventMap, "statusName") && CommonFunctions.isNotBlank(eventMap, "subStatusName")) {
					String subStatus = eventMap.get("subStatus").toString(),
						   subStatusName = eventMap.get("subStatusName").toString();
					
					if(ConstantValue.REJECT_SUB_STATUS.equals(subStatus)) {
						eventMap.put("statusName", subStatusName);
					} else if(CommonFunctions.isNotBlank(eventMap, "subStatusName")) {
						eventMap.put("statusName", eventMap.get("statusName") + "-" + subStatusName);
					}
				}
				
				// 评价等级
				DataDictHelper.setDictValueForField(eventMap, "evaLevel", "evaLevelName", evaLevelDictList);
				
				// 事件来源
				DataDictHelper.setDictValueForField(eventMap, "source", "sourceName", sourceDictList);
				
				// 采集方式
				DataDictHelper.setDictValueForField(eventMap, "collectWay", "collectWayName", collectWayDictList);
				
				if(CommonFunctions.isNotBlank(eventMap, "happenTime")) {
					eventMap.put("happenTimeStr", DateUtils.formatDate((Date)eventMap.get("happenTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(eventMap, "handleDate")) {
					eventMap.put("handleDateStr", DateUtils.formatDate((Date)eventMap.get("handleDate"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(eventMap, "finTime")) {
					eventMap.put("finTimeStr", DateUtils.formatDate((Date)eventMap.get("finTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(eventMap, "createTime")) {
					eventMap.put("createTimeStr", DateUtils.formatDate((Date)eventMap.get("createTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(eventMap, "remindMark") && CommonFunctions.isNotBlank(eventMap, "superviseMark")) {
					eventMap.put("remindStatus", "3");
				} else if(CommonFunctions.isNotBlank(eventMap, "superviseMark")) {
					eventMap.put("remindStatus", "2");
				} else if(CommonFunctions.isNotBlank(eventMap, "remindMark")) {
					eventMap.put("remindStatus", "1");
				}
				if(CommonFunctions.isNotBlank(eventMap, "wfAttentionStatus")) {
					eventMap.put("isAttention", "1".equals(eventMap.get("wfAttentionStatus").toString()));
				}
			}
		}
	}

	@Override
	public List<ArcgisInfoOfPoint> findFieldWorkData(Map<String, Object> params) {
		return nanChang3DMapper.findFieldWorkData(params);
	}

    
    
    
    
	
}

