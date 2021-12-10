package cn.ffcs.zhsq.szzg.event.service.impl;

import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.mybatis.persistence.szzg.event.EventAndReportJsonpMapper;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.szzg.event.service.IEventAndReportJsonpService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.data.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(value="eventAndReportJsonpService")
public class EventAndReportJsonpServiceImpl implements IEventAndReportJsonpService{
	
	
	public final static DateFormat dateFormat_Year = new SimpleDateFormat("yyyy");
	public final static DateFormat dateFormat_Month = new SimpleDateFormat("yyyy-MM");
	public final static DateFormat dateFormat_Day = new SimpleDateFormat("yyyy-MM-dd");
	
	@SuppressWarnings("serial")
	public final static Map<String, Map<String,Object>> TrendFormat = new HashMap<String, Map<String,Object>>() {
		{
			put("year", new HashMap<String, Object>() {
				{
					put("format", dateFormat_Year);
					put("trend", Calendar.YEAR);
				}
			});
			put("month", new HashMap<String, Object>() {
				{
					put("format", dateFormat_Month);
					put("trend", Calendar.MONTH);
				}
			});
			put("day", new HashMap<String, Object>() {
				{
					put("format", dateFormat_Day);
					put("trend", Calendar.DATE);
				}
			});
		}
	};
	public  static Map<Long, String> ORG_NAME_MAP = new HashMap<Long, String>() ;
		
	@Autowired
	private EventAndReportJsonpMapper eventAndReportJsonpMapper;

	@Autowired
	private IBaseDictionaryService dictionaryService;
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	
	/**
	 * 	统计在办事件+入格数量
	 */
	@Override
	public int countDoingEventData(Map<String, Object> params) {
		return eventAndReportJsonpMapper.findDoingEventCount(params);
	}
	
	/**
	 * 	查询在办事件+入格
	 */
	@Override
	public EUDGPagination findDoingEventData(Map<String, Object> params,int page, int rows) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		int count = eventAndReportJsonpMapper.findDoingEventCount(params);
		List<Map<String, Object>> list = null;
		if(count > 0 ) {
			list = eventAndReportJsonpMapper.findDoingEventData(params,rowBounds);
			formatData(list,params.get("infoOrgCode").toString());
		}else {
			list = new ArrayList<>();
		}
		return new EUDGPagination(count,list);
	}

	/**
	 * 	字典文本转换
	 * @param list
	 * @param infoOrgCode 
	 */
	private void formatData(List<Map<String, Object>> list, String infoOrgCode) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, String>> reportList = this.getReportFocusTypeList(null);
		Map<String, String> reportMap = new HashMap<String, String>();
		for (Map<String, String> map : reportList) {
			reportMap.put(map.get("REPORT_TYPE")+"_DICT", map.get("DICT_CODE"));
			reportMap.put(map.get("REPORT_STR"), map.get("DICT_CODE"));
			reportMap.put(map.get("REPORT_TYPE"), map.get("REPORT_TITLE"));
		}
		for (Map<String, Object> map : list) {
			if(null != map.get("STATUS")) { 
				//if(map.get("EVENT_ID") != null) {map.put("EVENT_ID", map.get("EVENT_ID").toString());map.put("eventId", map.get("eventId").toString());}
				//if(map.get("INSTANCE_ID") != null) {map.put("INSTANCE_ID", map.get("INSTANCE_ID").toString());map.put("instanceId", map.get("instanceId").toString());}
				String statusStr = map.get("STATUS").toString();
				if("0".equals(map.get("TYPE_").toString())){//0：事件，1：入格
					statusStr =  dictionaryService.changeCodeToName( ConstantValue.STATUS_PCODE, statusStr, infoOrgCode);
				}else {
					statusStr = dictionaryService.changeCodeToName(reportMap.get(map.get("EVENT_TYPE").toString()+"_DICT"), statusStr, infoOrgCode);
				}
				map.put("STATUS_STR",statusStr);
			}
			
			map.put("handleDateFlag", map.get("HANDLE_DATE_FLAG"));
			if( null != map.get("HANDLE_DATE") ) {
				map.put("HANDLE_DATE", sdf.format(map.get("HANDLE_DATE")));
			}
			if( null != map.get("CUR_ORG_ID") ) { 
				Long orgId = Long.parseLong(map.get("CUR_ORG_ID").toString());
				String orgName= "";
				if(ORG_NAME_MAP.containsKey(orgId)) {
					orgName = ORG_NAME_MAP.get(orgId);
				}else {
					OrgSocialInfoBO org =orgSocialInfoOutService.findByOrgId(orgId);
					if(org!=null) {
						orgName = org.getOrgName();
						ORG_NAME_MAP.put(orgId,orgName);
					}
				}
				map.put("curOrgName", orgName);
				map.put("CUR_ORG", orgName);
			}
			if(null != map.get("URGENCY_DEGREE")) {
				map.put("URGENCY_DEGREE_STR", dictionaryService.changeCodeToName( ConstantValue.URGENCY_DEGREE_PCODE, map.get("URGENCY_DEGREE").toString(), infoOrgCode));
			}
			if(null != map.get("CREATE_TIME")) {
				map.put("CREATE_TIME", sdf.format(map.get("CREATE_TIME")));
			}
			
			//事件类型字典翻译
			if ("0".equals(map.get("TYPE_"))) {	//事件
				Map<String, Object> dictMap = new HashMap<String, Object>();
				dictMap.put("orgCode", infoOrgCode);
				dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
				List<BaseDataDict> eventTypeDict = dictionaryService.findDataDictListByCodes(dictMap);
				// 设置事件分类全路径
				if(eventTypeDict != null) {
					Map<String, Object> eventTypeMap = DataDictHelper.capMultilevelDictInfo(map.get("EVENT_TYPE").toString(), ConstantValue.BIG_TYPE_PCODE, eventTypeDict);
					
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictName")) {
						map.put("typeName", eventTypeMap.get("dictName"));
					}
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
						map.put("eventClass", eventTypeMap.get("dictFullPath"));
					}
				}
			} else {	//入格
				if(null != map.get("EVENT_TYPE")) {
					map.put("eventClass", reportMap.get(map.get("EVENT_TYPE").toString()));
				}
			}
		}
		
	}
	
	/*@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Timestamp getOracleTimestamp(Object value) {
	    try {
	        Class clz = value.getClass();
	        Method m = clz.getMethod("timestampValue"); 
	        return (Timestamp) m.invoke(value);
	    } catch (Exception e) {
	    	System.out.println("getOracleTimestamp:"+value);
	    	e.printStackTrace();
	        return null;
	    }
	}*/

	/**
	 * 
	 */
	//@Override
	@SuppressWarnings("unchecked")
	public Map<String, List<Map<String, Object>>> eventTypeDayTotal(Map<String, Object> params) {
		List<Map<String, Object>> list = eventAndReportJsonpMapper.eventTypeDayTotal(params);
		params.put("initNum", "true");
		List<Map<String, Object>> dayList = eventAndReportJsonpMapper.startDay2EndDay(params);
		Map<String, Map<String, Object>> typeMap = new HashMap<String, Map<String, Object>>();
		String type_ = "";
		
		for (Map<String, Object> map : list) {//type_:{date1:{TOTAL:0,DOING:0},date2:{TOTAL:0,DOING:0}}
			type_ = map.get("TYPE_").toString();
			if(null == typeMap.get(type_)) {
				typeMap.put(type_, new HashMap<String, Object>());
			}
			typeMap.get(type_).put(map.get("DATE_").toString(), map);
		}
		Map<String, List<Map<String, Object>>> returnMap =  new HashMap<String, List<Map<String, Object>>>();
		List<Map<String,Object>> dateList;
		List<String> typeArr = new ArrayList<String>();
		typeArr.add("0");
		List<Map<String, String>>  rList = this.getReportFocusTypeList(null);
		for (Map<String, String> map : rList) {
			typeArr.add(map.get("REPORT_TYPE"));
		}
		for (String num : typeArr) {
			dateList = new ArrayList<Map<String,Object>>();
			Map<String, Object> dateMap = typeMap.get(num);//{date1:{TOTAL:0,DOING:0},date2:{TOTAL:0,DOING:0}}
			boolean hasNum = null == dateMap;
			
			for (Map<String, Object> map : dayList) {
				if(hasNum || null == dateMap.get(map.get("DATE_").toString()) ) {
					dateList.add(map);
				}else {
					dateList.add((Map<String, Object>) dateMap.get(map.get("DATE_").toString()));
				}
			}
			returnMap.put(num, dateList);
		}
		return returnMap;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, List<Map<String, Object>>> eventTypeGridTotal(Map<String, Object> params) {
		List<Map<String, Object>> list = eventAndReportJsonpMapper.eventTypeGridTotal(params);
		List<Map<String, Object>> dayList = eventAndReportJsonpMapper.findGridInfoByParentId(params);
		Map<String, Map<String, Object>> typeMap = new HashMap<String, Map<String, Object>>();
		String type_ = "";
		
		for (Map<String, Object> map : list) {//type_:{INFO_ORG_CODE1:{TOTAL:0 },INFO_ORG_CODE2:{TOTAL:0 }}
			type_ = map.get("TYPE_").toString();
			if(null == typeMap.get(type_)) {
				typeMap.put(type_, new HashMap<String, Object>());
			}
			typeMap.get(type_).put(map.get("INFO_ORG_CODE").toString(), map);
		}
		
		Map<String, List<Map<String, Object>>> returnMap =  new HashMap<String, List<Map<String, Object>>>();
		List<Map<String,Object>> dateList;
		List<String> typeArr = new ArrayList<String>();
		typeArr.add("0");
		List<Map<String, String>>  rList = this.getReportFocusTypeList(null);
		for (Map<String, String> map : rList) {
			typeArr.add(map.get("REPORT_TYPE"));
		}
		for (String num : typeArr) {
			dateList = new ArrayList<Map<String,Object>>();
			Map<String, Object> dateMap = typeMap.get(num);//{date1:{TOTAL:0,DOING:0},date2:{TOTAL:0,DOING:0}}
			boolean hasNum = null == dateMap;
			
			for (Map<String, Object> map : dayList) {
				if(hasNum || null == dateMap.get(map.get("INFO_ORG_CODE").toString()) ) {
					dateList.add(map);
				}else {
					dateList.add((Map<String, Object>) dateMap.get(map.get("INFO_ORG_CODE").toString()));
				}
			}
			returnMap.put(num, dateList);
		}
		return returnMap;
	}
	
	public Map<String, Object> findEventAnalysisTotal(Map<String, Object> params){
		Map<String, Object> returnMap =  new HashMap<String, Object>();
		if("0".equals(params.get("type_").toString())) {
			returnMap = eventAndReportJsonpMapper.findEventTotal(params);
		}else {
			returnMap = eventAndReportJsonpMapper.findReportTotal(params);
		}
		return returnMap;
	}
	
	public Map<String, Object> findEventAnalysisGrid(Map<String, Object> params){
		Map<String, Object> returnMap =  new HashMap<String, Object>();
		List<Map<String, Object>> list =new ArrayList<Map<String,Object>>(); 
		String type_ = params.get("type_").toString();
		if("0".equals(type_)) {
			list = eventAndReportJsonpMapper.findEventGridTotal(params);
		}else {
			list = eventAndReportJsonpMapper.findReportGridTotal(params);
		}
		String infoOrgCode = "";
		for (Map<String, Object> map : list) {
			infoOrgCode = map.get("INFO_ORG_CODE").toString();
			params.put(infoOrgCode, map.get("TOTAL").toString());
			params.put(infoOrgCode+"_RATE", map.get("RATE").toString());
		}
		List<Map<String, Object>> gridList = eventAndReportJsonpMapper.findGridInfoByParentId(params);
		for (Map<String, Object> map : gridList) {
			infoOrgCode = map.get("INFO_ORG_CODE").toString();
			if(null != params.get(infoOrgCode)) {
				map.put("TOTAL", params.get(infoOrgCode));
				map.put("RATE", params.get(infoOrgCode+"_RATE"));
			}else {
				map.put("RATE", "0");
			}
		}
		List<Map<String, Object>> rateList = new ArrayList<Map<String,Object>>();
		rateList.addAll(gridList);
		Collections.sort(gridList, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Integer name1 = Integer.valueOf(o1.get("TOTAL").toString());
				Integer name2 = Integer.valueOf(o2.get("TOTAL").toString());
				return name2.compareTo(name1);
			}
		});
		Collections.sort(rateList, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Double name1 = Double.parseDouble(o1.get("RATE").toString());
				Double name2 = Double.parseDouble(o2.get("RATE").toString());
				return name2.compareTo(name1);
			}
		});
		returnMap.put("gridList", gridList);
		returnMap.put("rateList", rateList);
		return returnMap;
	}
	
	public Map<String, Object> findEventAnalysisDate(Map<String, Object> params){
		Map<String, Object> returnMap =  new HashMap<String, Object>();
		List<Map<String, Object>> listMonth =new ArrayList<Map<String,Object>>(); 
		List<Map<String, Object>> listday =new ArrayList<Map<String,Object>>(); 
		String type_ = params.get("type_").toString();
		if("0".equals(type_)) {
			params.put("date", "year");
			listMonth = eventAndReportJsonpMapper.findEventDateTotal(params);
			params.put("date", "month");
			listday = eventAndReportJsonpMapper.findEventDateTotal(params);
		}else {
			params.put("date", "year");
			listMonth = eventAndReportJsonpMapper.findReportDateTotal(params);
			params.put("date", "month");
			listday = eventAndReportJsonpMapper.findReportDateTotal(params);
		}
		for (Map<String, Object> map : listMonth) {
			params.put( map.get("DATE_").toString(), map.get("TOTAL").toString());
			params.put( map.get("DATE_").toString() + "_end", map.get("ENDTOTAL").toString());
		}
		for (Map<String, Object> map : listday) {
			params.put( map.get("DATE_").toString(), map.get("TOTAL").toString());
			params.put( map.get("DATE_").toString() + "_end", map.get("ENDTOTAL").toString());
		}
		Calendar c =Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		params.put("endDate", sdf.format(c.getTime()));
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		params.put("startDate", sdf.format(c.getTime()));
		params.put("initNum", "true");
		List<Map<String, Object>> dayList = eventAndReportJsonpMapper.startDay2EndDay(params);
		String date_="";
		for (Map<String, Object> map : dayList) {
			date_ = map.get("DATE_").toString();
			if(null != params.get(date_) ) {
				map.put("TOTAL", params.get(date_).toString());
				map.put("ENDTOTAL", params.get(date_ + "_end").toString());
			}
		}
		List<Map<String, Object>> monthList = eventAndReportJsonpMapper.startMonth2EndMonth(params);
		for (Map<String, Object> map : monthList) {
			date_ = map.get("MONTH_").toString();
			if(null != params.get(date_) ) {
				map.put("TOTAL", params.get(date_).toString());
				map.put("ENDTOTAL", params.get(date_ + "_end").toString());
			}
		}
		returnMap.put("dayList", dayList);
		returnMap.put("monthList", monthList);
		return returnMap;
	}

	@Override
	public EUDGPagination eventAndReportList4Jsonp(Map<String, Object> params, int page, int rows) {
		/*
		 * RowBounds rowBounds = new RowBounds((page - 1) * rows, rows); int count =
		 * eventAndReportJsonpMapper.eventAndReportList4JsonpCount(params);
		 * List<Map<String, Object>> list = null; if(count > 0 ) { list =
		 * eventAndReportJsonpMapper.eventAndReportList4JsonpData(params,rowBounds);
		 * formatData(list,params.get("infoOrgCode").toString()); }else { list = new
		 * ArrayList<>(); }
		
		return new EUDGPagination(count,list); */
		return null;
	}

	@Override
	public List<Map<String, Object>> getReportFocusTrend(Map<String, Object> params) {
		if(CommonFunctions.isBlank(params, "createTimeStart") ||
				CommonFunctions.isBlank(params, "createTimeEnd") ||
				CommonFunctions.isBlank(params, "trendType")) {
			return new ArrayList<Map<String,Object>>();
		}
		boolean iSearchSettlesTotal = CommonFunctions.isBlank(params, "searchSettlesTotal")?false:true;
		
		if(CommonFunctions.isNotBlank(params, "reportTypeList")) {
			String reportTypeList = params.get("reportTypeList").toString();
			params.put("reportTypeList", reportTypeList.split(","));
		} 

		if(CommonFunctions.isNotBlank(params, "trendType")
				&&params.get("trendType").toString().equals("month")) {
			//月份进行个性化查询
			List<String> trendTime = this.getTrendTime(params.get("createTimeStart").toString(),
					params.get("createTimeEnd").toString(),
					"month");
			List<Map<String, Object>> dayList=new ArrayList<Map<String, Object>>();
			for (String string : trendTime) {
				Map<String,Object> temp=new HashMap<String,Object>();
				temp.put("trendDate", string);
				temp.put("eventTotal", "0");
				if(iSearchSettlesTotal) {
					temp.put("settlesTotal", "0");
				}
				dayList.add(temp);
			}
			
			
			List<Map<String, Object>> reportFocusTrendForMonth = eventAndReportJsonpMapper.getReportFocusTrendForMonth(params);
			
			Map<String,Object> dataList=new HashMap<String,Object>();
			
			
			for (Map<String, Object> map : reportFocusTrendForMonth) {
				if(CommonFunctions.isNotBlank(map, "trendDate")) {
					dataList.put(map.get("trendDate").toString(), map);
				}
			}
			
			
			for (Map<String, Object> map : dayList) {
				if(dataList.containsKey(map.get("trendDate").toString())) {
					Map<String, Object> object = (Map<String, Object>) dataList.get(map.get("trendDate").toString());
					map.put("eventTotal", object.get("eventTotal"));
					if(iSearchSettlesTotal) {
						map.put("settlesTotal", object.get("settlesTotal"));
					}
					
				}
			}
			
			return dayList;
			
		}
		
		
		params.put("startDate", params.get("createTimeStart").toString());
		params.put("endDate", params.get("createTimeEnd").toString());
		List<Map<String, Object>> dayList = eventAndReportJsonpMapper.startDay2EndDay(params);

		List<Map<String, Object>> dataList = eventAndReportJsonpMapper.getReportFocusTrend(params);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		for (Map<String, Object> map : dataList) {// 先保存所有日期有值
			dataMap.put(map.get("trendDate").toString(), map.get("eventTotal").toString());
			if(iSearchSettlesTotal) {
				dataMap.put(map.get("trendDate").toString()+"settlesTotal", map.get("settlesTotal").toString());
			}
		}
		String date_ = "";
		// 每个日期存入数量
		for (Map<String, Object> map : dayList) {
			date_ = map.get("DATE_").toString();
			map.put("trendDate", date_);
			map.put("eventTotal", dataMap.get(date_) != null ? dataMap.get(date_).toString() : "0");
			if(iSearchSettlesTotal) {
				map.put("settlesTotal", dataMap.get(date_+"settlesTotal") != null ? dataMap.get(date_+"settlesTotal").toString() : "0");
			}
		}
		return dayList;
	}
	
	/***
     * 获取两个时间段的年份/月份/天
     * @param startTime
     * @param endTime
     * @param trendType
     * 				year
     * 				month
     * 				day
     * @return
     */
    public  List<String> getTrendTime(String startTime, String endTime,String trendType) {
        List<String> res=new ArrayList<String>();
        try {
        	if(TrendFormat.containsKey(trendType)) {
        		
        		Date start = dateFormat_Day.parse(startTime);
        		Date end = dateFormat_Day.parse(endTime);
        		Calendar tempStart = Calendar.getInstance();
        		tempStart.setTime(start);
        		Calendar tempEnd = Calendar.getInstance();
        		tempEnd.setTime(end);
        		DateFormat thisFormat=(DateFormat) TrendFormat.get(trendType).get("format");
        		Integer thisTrend=(Integer) TrendFormat.get(trendType).get("trend");
        		while (tempStart.before(tempEnd)||tempStart.equals(tempEnd)) {
        			String thisTemp=thisFormat.format(tempStart.getTime());
        			if(res.size()<100) {
        				res.add(thisTemp);
        			}else {
        				break;
        			}
        			tempStart.add(thisTrend,1);
        		}
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    
    /**
	 * 格式化输入参数
	 * @param params
	 * @throws ZhsqEventException 
	 */
	private void formatParamIn(Map<String, Object> params) throws ZhsqEventException {
		if(CommonFunctions.isNotBlank(params, "eRegionCode")) {
			byte[] decodedData = Base64.decode(params.get("eRegionCode").toString());
			params.put("infoOrgCode", new String(decodedData));
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
	}
	
	/**
	 * 	列表数据
	 * @throws ZhsqEventException 
	 */
	@Override
	public EUDGPagination getReportFocusList(Map<String, Object> params,int page, int rows) throws ZhsqEventException {
		formatParamIn(params);
		if (CommonFunctions.isNotBlank(params,"jurisdiction")&&"all".equals(params.get("jurisdiction").toString())) {
			params.put("jurisdiction","0");
		}
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		long count = eventAndReportJsonpMapper.getReportFocusCount(params);
		List<Map<String, Object>> list = null;
		if(count > 0 ) {
			list = eventAndReportJsonpMapper.getReportFocusList(params,rowBounds);
			formatData(list,params.get("infoOrgCode").toString());
		}else {
			list = new ArrayList<>();
		}
		return new EUDGPagination(count,list);
	}
	
	@Override
	public List<Map<String, Object>> getEventTrend(Map<String, Object> params) {
		if(CommonFunctions.isBlank(params, "createTimeStart") ||
				CommonFunctions.isBlank(params, "createTimeEnd") ||
				CommonFunctions.isBlank(params, "trendType")) {
			return new ArrayList<Map<String,Object>>();
		} 
		
		params.put("startDate", params.get("createTimeStart").toString());
		params.put("endDate", params.get("createTimeEnd").toString());
		List<Map<String, Object>> dayList = eventAndReportJsonpMapper.startDay2EndDay(params);

		List<Map<String, Object>> dataList = eventAndReportJsonpMapper.getEventTrend(params);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		for (Map<String, Object> map : dataList) {// 先保存所有日期有值
			dataMap.put(map.get("trendDate").toString(), map.get("eventTotal").toString());
		}
		String date_ = "";
		// 每个日期存入数量
		for (Map<String, Object> map : dayList) {
			date_ = map.get("DATE_").toString();
			map.put("trendDate", date_);
			map.put("eventTotal", dataMap.get(date_) != null ? dataMap.get(date_).toString() : "0");

		}
		return dayList;
	}
	
	/**
	 * 	统计入格
	 */
	@Override
	public List<Map<String, Object>> countDoingReportFocusList(Map<String, Object> params) {
		return eventAndReportJsonpMapper.countDoingReportFocusList(params);
	}

	
	public static List<Map<String, String>> REPORT_FOCUS_TYPE_LIST = new ArrayList<Map<String,String>>();
	//初始化
	static boolean INIT_REPORT = true;
	@Override
	public List<Map<String, String>> getReportFocusTypeList(Map<String, Object> params) {
		if(INIT_REPORT) {
			REPORT_FOCUS_TYPE_LIST= eventAndReportJsonpMapper.getReportFocusTypeList(params);
			INIT_REPORT = false;
		}
	 return REPORT_FOCUS_TYPE_LIST;
	}

	@Override
	public void initReportFocusType(Map<String, Object> params) {
		INIT_REPORT = true;
		this.getReportFocusTypeList(params);
	}
}
