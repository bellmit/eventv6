package cn.ffcs.zhsq.szzg.event.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.persistence.event.Event4WorkflowForMapMapper;
import cn.ffcs.zhsq.mybatis.persistence.szzg.event.EventAndReportJsonpMapper;
import cn.ffcs.zhsq.mybatis.persistence.szzg.event.EventDisposal4JsonpMapper;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.szzg.event.service.IEventDisposal4JsonpService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;


@Service(value="eventDisposal4JsonpService")
public class EventDisposal4JsonpServiceImpl implements IEventDisposal4JsonpService {
	
	@Autowired
	private EventDisposal4JsonpMapper eventDisposal4JsonpMapper;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private Event4WorkflowForMapMapper event4WorkflowForMapMapper;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	
	@Autowired
	private IReportIntegrationService reportIntegrationService;
	
	@Autowired
	private EventAndReportJsonpMapper eventAndReportJsonpMapper;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	/**
	 * 相关存储过程：PKG_BI_EVENT.PROC_BUILD_EVENT_STAT
	 * 相关表：T_BI_EVENT_STAT_DAY、T_BI_EVENT_STAT
	 */
	public final static Map<String, String> StatisticsMapping = new HashMap<String, String>() {
	    {
	        put("eventTotal", "TOTAL_");//事件总数
	        put("settlesTotal", "SETTLES_TOTAL_");//办结量--03,04
	        put("timeoutTotal", "TIMEOUT_TOTAL_");//超时数
	        put("doingTotal", "DOING_TOTAL_");//在办量--00,01,02
	        put("archiveTotal", "ARCHIVE_TOTAL_");//归档量--04
	        put("settlesIntimeTotal", "SETTLES_INTIME_TOTAL_");//规定时间内办结数
	        put("focalTotal", "FOCAL_TOTAL_");//关注数
	        put("superviseTotal", "SUPERVISE_TOTAL_");//督办数
	        put("satiSettlesTotal", "SATI_SETTLES_TOTAL_");//满意办结事件数
	        put("evaTotal", "EVA_TOTAL_");//有评价的事件总数
	        put("emtTotal", "EMT_TOTAL_");//八员队伍事件总量
	        put("emtSettlesTotal", "EMT_SETTLES_TOTAL_");//八员队伍办结事件量
	        put("emtWfTimeoutTotal", "EMT_WF_TIMEOUT_TOTAL_");//八员队伍逾期事件量
	        put("wfTimeoutTotal", "WF_TIMEOUT_TOTAL_");//逾期事件量
	    }
	};
	
	/**
	 * 相关存储过程：PKG_BI_EVENT.PROC_BI_POSI_USERS
	 * 相关表：T_BI_POSI_USERS
	 */
	public final static Map<String, String> POSITION_STATISTICS_MAPPING = new HashMap<String, String>() {
	    {
	        put("userNums", "USER_NUMS");//用户数量
	        put("handledEvents", "CREATE_EVENTS");//已办事件量
	        put("settlesEvents", "SETTLES_EVENTS");//已办结事件量
	    }
	};
	
	
	public final static Map<String, String> UrbanObjStatusMapping = new HashMap<String, String>() {
		{
			put("01", "完好");
			put("02", "破损");
			put("03", "丢失");
			put("04", "废弃");
			put("05", "移除");
		}
	};
	
	public final static DateFormat dateFormat_Year = new SimpleDateFormat("yyyy");
	public final static DateFormat dateFormat_Month = new SimpleDateFormat("yyyy-MM");
	public final static DateFormat dateFormat_Day = new SimpleDateFormat("yyyy-MM-dd");
	
	
	
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

	@Override
	public List<Map<String, Object>> getEventTotalTypeByCondition(Map<String, Object> params,Integer pageNo,Integer pageSize) {
		List<Map<String, Object>> result=new ArrayList<Map<String,Object>>();
		//判断是否查询当天的数据
		if(CommonFunctions.isNotBlank(params, "searchCur")) {
			result = eventDisposal4JsonpMapper.getEventTotalTypeByConditionToday(params);
		}else {
			result = eventDisposal4JsonpMapper.getEventTotalTypeByCondition(params);
		}
		return result;
		
	}
	

	@Override
	public List<Map<String, Object>> getEventTotalSourceByCondition(Map<String, Object> params, Integer pageNo,
			Integer pageSize) {
		List<Map<String, Object>> result=new ArrayList<Map<String,Object>>();
		Date createTimeStart = null, createTimeEnd = null;
		List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "eInfoOrgCode")) {//有加密的地域编码
			String eInfoOrgCode = params.get("eInfoOrgCode").toString();
			
			if(CommonFunctions.isBase64(eInfoOrgCode)) {
				byte[] decodedData = Base64.decode(eInfoOrgCode);
				params.put("infoOrgCode", new String(decodedData));
			} else {
				throw new IllegalArgumentException("参数【eInfoOrgCode】：【" + eInfoOrgCode + "】不符合加密方式！");
			}
		}
		if(CommonFunctions.isNotBlank(params, "eUserOrgCode")) {
			String eUserOrgCode = params.get("eUserOrgCode").toString();
			
			if(CommonFunctions.isBase64(eUserOrgCode)) {
				byte[] decodedData = Base64.decode(eUserOrgCode);
				params.put("userOrgCode", new String(decodedData));
			} else {
				throw new IllegalArgumentException("参数【eUserOrgCode】：【" + eUserOrgCode + "】不符合加密方式！");
			}
		}
		
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			throw new IllegalArgumentException("缺少地域编码【infoOrgCode】！");
		}
		if(CommonFunctions.isBlank(params, "userOrgCode")) {
			throw new IllegalArgumentException("缺少组织编码【userOrgCode】！");
		}
		if(CommonFunctions.isNotBlank(params, "createTimeStart")) {
			Object createTimeStartObj = params.get("createTimeStart");
			
			try {
				if(createTimeStartObj instanceof String) {
					createTimeStart = DateUtils.convertStringToDate(createTimeStartObj.toString(), DateUtils.PATTERN_DATE);
				} else if(createTimeStartObj instanceof Date) {
					createTimeStart = DateUtils.convertStringToDate(DateUtils.formatDate((Date) createTimeStartObj, DateUtils.PATTERN_DATE), DateUtils.PATTERN_DATE);
				}
			} catch (ParseException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("采集开始时间不满足如下时间格式：【" + DateUtils.PATTERN_DATE + "】！");
			}
		}
		
		if(createTimeStart != null) {
			params.put("createTimeStart", createTimeStart);
		}
		
		if(CommonFunctions.isNotBlank(params, "createTimeEnd")) {
			Object createTimeEndObj = params.get("createTimeEnd");
			
			try {
				if(createTimeEndObj instanceof String) {
					createTimeEnd = DateUtils.convertStringToDate(createTimeEndObj.toString(), DateUtils.PATTERN_DATE);
				} else if(createTimeEndObj instanceof Date) {
					createTimeEnd = DateUtils.convertStringToDate(DateUtils.formatDate((Date) createTimeEndObj, DateUtils.PATTERN_DATE), DateUtils.PATTERN_DATE);
				}
			} catch (ParseException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("采集结束时间不满足如下时间格式：【" + DateUtils.PATTERN_DATE + "】！");
			}
		}
		
		if(createTimeEnd != null) {
			params.put("createTimeEnd", createTimeEnd);
		}
		
		//是否获取实时统计数据
		if(CommonFunctions.isNotBlank(params, "isSearchCur") && Boolean.valueOf(params.get("isSearchCur").toString())) {
			result = eventDisposal4JsonpMapper.getEventTotalSourceByConditionToday(params);
		}else {
			result = eventDisposal4JsonpMapper.getEventTotalSourceByCondition(params);
		}
		
		List<BaseDataDict> sourceDictList = null;
		String userOrgCode = params.get("userOrgCode").toString();
		LinkedHashMap<String, Map<String, Object>> sourceMap = new LinkedHashMap<String, Map<String, Object>>();
		Map<String, Object> dictMap = null;
		
		sourceDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SOURCE_PCODE, userOrgCode);
		
		if(sourceDictList != null) {
			for(BaseDataDict sourceDict : sourceDictList) {
				dictMap = new HashMap<String, Object>();
				dictMap.put("SOURCE", sourceDict.getDictGeneralCode());
				dictMap.put("SOURCE_NAME", sourceDict.getDictName());
				dictMap.put("TOTAL", 0);
				
				sourceMap.put(sourceDict.getDictGeneralCode(), dictMap);
			}
			
			if(result != null && result.size() > 0) {
				String source = null;
				
				for(Map<String,Object> dataMap : result) {
					source = null;
					dictMap = null;
					
					if(CommonFunctions.isNotBlank(dataMap, "SOURCE")) {
						source = dataMap.get("SOURCE").toString();
					}
					
					if(sourceMap.containsKey(source)) {
						dictMap = new HashMap<String, Object>();
						
						dictMap.putAll(sourceMap.get(source));
						dictMap.putAll(dataMap);
						
						sourceMap.remove(source);
						resultMapList.add(dictMap);
					}
				}
			}
			
			for(String eventSource : sourceMap.keySet()) {
				resultMapList.add(sourceMap.get(eventSource));
			}
		}
		
		return resultMapList;
	}
	
	
	public void formatDataOut(List<Map<String,Object>> dataMapList,String orgCode) {
		if(dataMapList != null&&dataMapList.size() > 0) {
			Map<String,Object> dictMap=new HashMap<String,Object>();
			dictMap.put("orgCode", orgCode);
			dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
			
			List<BaseDataDict> typeDictList = baseDictionaryService.findDataDictListByCodes(dictMap);
			List<BaseDataDict> sourceDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SOURCE_PCODE, orgCode);
			
			for(Map<String,Object> dataMap : dataMapList) {
				DataDictHelper.setDictValueForField(dataMap, "TYPE_", "TYPE_NAME", typeDictList);
				DataDictHelper.setDictValueForField(dataMap, "SOURCE", "SOURCE_NAME", sourceDictList);
			}
		}
	}
	

	@Override
	public EUDGPagination getEventSimpleList(Map<String, Object> params, UserInfo userInfo) {
		Integer page=1;
		Integer rows=20;
		if(CommonFunctions.isNotBlank(params, "page")) {
			page=Integer.valueOf(params.get("page").toString());
		}
		if(CommonFunctions.isNotBlank(params, "rows")) {
			rows=Integer.valueOf(params.get("rows").toString());
		}
		page=page>0?page:1;
		rows=rows>0?rows:20;
		
		EUDGPagination eventPagination=new EUDGPagination();
		
		try {
			this.formatParamIn(params);
			RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
			Long count = 0L;
			List<Map<String, Object>> eventList = null;
			
			count = event4WorkflowForMapMapper.getEventCountSimple(params);
			
			if(count > 0) {
				eventList = event4WorkflowForMapMapper.getEventListSimple(params, rowBounds);
			} else {
				eventList = new ArrayList<Map<String, Object>>();
			}
			
			formatMapDataOut(eventList, params);
			
			eventPagination = new EUDGPagination(count, eventList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return eventPagination;
	}
    
	@Override
	public Long getEventSimpleCount(Map<String, Object> params, UserInfo userInfo) {
		Long result=0L;
		try {
			this.formatParamIn(params);
			result=event4WorkflowForMapMapper.getEventCountSimple(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	

	@Override
	public Map<String, Object> searchEventByIdSimple(Long eventId, UserInfo userInfo) {
		Map<String, Object> eventDetail = event4WorkflowForMapMapper.getEventDetail(eventId);
		List<Map<String,Object>> eventList=new ArrayList<Map<String,Object>>();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("orgCode", userInfo.getOrgCode());
		eventList.add(eventDetail);
		this.formatMapDataOut(eventList, params);
		return eventDetail;
	}

	@Override
	public EUDGPagination getSimilarEventList(Map<String, Object> params, UserInfo userInfo) {
		Integer page=1;
		Integer rows=20;
		if(CommonFunctions.isNotBlank(params, "page")) {
			page=Integer.valueOf(params.get("page").toString());
		}
		if(CommonFunctions.isNotBlank(params, "rows")) {
			rows=Integer.valueOf(params.get("rows").toString());
		}
		page=page>0?page:1;
		rows=rows>0?rows:20;
		
		EUDGPagination eventPagination=new EUDGPagination();
		
		try {
			this.formatParamInForSimilar(params,userInfo);
			RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
			Long count = 0L;
			List<Map<String, Object>> eventList = null;
			
			count = event4WorkflowForMapMapper.getSimilarEventCount(params);
			
			if(count > 0) {
				eventList = event4WorkflowForMapMapper.getSimilarEventList(params, rowBounds);
			} else {
				eventList = new ArrayList<Map<String, Object>>();
			}
			
			formatMapDataOut(eventList, params);
			
			eventPagination = new EUDGPagination(count, eventList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return eventPagination;
	}
	
	
	@Override
	public Long getSimilarEventCount(Map<String, Object> params, UserInfo userInfo) {
		Long result=0L;
		
		try {
			this.formatParamInForSimilar(params,userInfo);
			result=event4WorkflowForMapMapper.getSimilarEventCount(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	

	@Override
	public Map<String, Object> getMapEventList(Map<String, Object> params,UserInfo userInfo) {
		Map<String, Object> result=new HashMap<String,Object>();
		// 获取总数
		try {
			if(CommonFunctions.isBlank(params, "withDetail")) {
				params.put("withDetail", 1);//默认查询事件详情
			}
			
			String eventType="";
			String listType="0";
			if(CommonFunctions.isNotBlank(params, "eventType")) {
				eventType=params.get("eventType").toString();
			}
			if(CommonFunctions.isNotBlank(params, "listType")) {
				listType=params.get("listType").toString();
			}
			
			if(eventType.equals("todo")) {
				params.put("listType", "1");
				listType="1";
			}else if(eventType.equals("all")) {
				params.put("listType", "5");
				listType="5";
			}else if(eventType.equals("proruption")) {//紧急程度为突发
				params.put("listType", "5");
				params.put("urgencyDegree", "02");
				listType="5";
			}else if(eventType.equals("sensitive")) {//紧急程度为敏感
				params.put("listType", "5");
				params.put("urgencyDegree", "03");
				listType="5";
			}else if(eventType.equals("twoViolation")) {//两违建筑
				params.put("listType", "5");
				params.put("type", "92");
				listType="5";
			}else if(eventType.equals("eco")) {//生态事件
				params.put("listType", "5");
				params.put("type", "31");
				listType="5";
			}else if(eventType.equals("epo")) {//环保事件
				params.put("listType", "5");
				params.put("type", "31");
				listType="5";
			}else if(eventType.equals("IllegalLandUse")) {//违法用地
				params.put("listType", "5");
				params.put("type", "9201");
				listType="5";
			}else if(eventType.equals("IllegalConstruction")) {//违法建筑
				params.put("listType", "5");
				params.put("type", "9202");
				listType="5";
			}else if(eventType.equals("spotCase")) {//卫星图斑案件
				params.put("listType", "5");
				params.put("type", "9203");
				listType="5";
			}else if(eventType.equals("natural")) {//自然灾害事件
				params.put("listType", "5");
				params.put("type", "0804");
				listType="5";
			}
			else if(eventType.startsWith("reportType_")) {
				params.put("listType", "51");
				params.put("reportType", eventType.split("_")[1]);
				if(CommonFunctions.isNotBlank(params, "createTimeStart")) {
					params.put("reportDayStart", params.get("createTimeStart"));
				}
				if(CommonFunctions.isNotBlank(params, "createTimeEnd")) {
					params.put("reportDayEnd", params.get("createTimeEnd"));
				}
				if(CommonFunctions.isNotBlank(params, "keyRemarkWord")) {
					params.put("keyWord", params.get("keyRemarkWord"));
				}
				if(CommonFunctions.isNotBlank(params, "code")) {
					params.put("reportCode", params.get("code"));
				}
				if(CommonFunctions.isNotBlank(params, "infoOrgCode")) {
					params.put("regionCode", params.get("infoOrgCode"));
				}
				listType = "reportType";
			}
			else if(eventType.equals("doing_all")) {//辖区所有在办(南安)
				listType="1_all";
				if(CommonFunctions.isNotBlank(params, "keyRemarkWord")) {
					params.put("keyWord", params.get("keyRemarkWord"));
				}
			}
			
			formatParamIn(params);
			switch (listType) {
			case "1"://待办
				result.put("total", event4WorkflowForMapMapper.findCount4Todo(params));
				break;
			case "5"://辖区所有
				result.put("total", event4WorkflowForMapMapper.findCount4Jurisdiction(params));
				break;
			case "reportType"://入格
				result.put("total", reportIntegrationService.findCount4ReportFocus(params));
				break;
			case "1_all"://辖区所有在办(南安)
				result.put("total", eventAndReportJsonpMapper.findAllDoingEventForNananCount(params));
				break;
			default:
				result.put("total", 0);
				break;
			}
			List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
			if (CommonFunctions.isNotBlank(params, "page") && CommonFunctions.isNotBlank(params, "rows")) {
				//传入分页参数使用分页查询
				//构造分页对象
				
				Integer page=1;
				Integer rows=10;
				if(CommonFunctions.isNotBlank(params, "page")) {
					page=Integer.valueOf(params.get("page").toString());
				}
				if(CommonFunctions.isNotBlank(params, "rows")) {
					rows=Integer.valueOf(params.get("rows").toString());
				}
				
				page = page < 1 ? 1 : page;
				rows = rows < 1 ? 10 : rows;
				
				RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
				
				switch (listType) {
				case "1"://待办
					data=event4WorkflowForMapMapper.findPageList4Todo(params,rowBounds);
					break;
				case "5"://辖区所有
					data=event4WorkflowForMapMapper.findPageList4Jurisdiction(params,rowBounds);
					break;
				case "reportType"://入格
					data= (List<Map<String, Object>>) reportIntegrationService.findPagination4ReportFocus(page, rows, params).getRows();
					break;
				case "1_all"://辖区所有在办(南安)
					data=eventAndReportJsonpMapper.findAllDoingEventForNananList(params, rowBounds);
					break;
				default:
					data=new ArrayList<Map<String,Object>>();
					break;
				}
			} else {
				//未传入分页参数则查询满足条件的所有事件
				switch (listType) {
				case "1"://待办
					data=event4WorkflowForMapMapper.findPageList4Todo(params);
					break;
				case "5"://辖区所有
					data=event4WorkflowForMapMapper.findPageList4Jurisdiction(params);
					break;
				case "reportType"://入格
					data=(List<Map<String, Object>>) reportIntegrationService.findPagination4ReportFocus(1, 10000, params).getRows();
					break;
				case "1_all"://辖区所有在办(南安)
					data=eventAndReportJsonpMapper.findAllDoingEventForNananList(params, new RowBounds((1 - 1) * 10000, 10000));
					break;
				default:
					data=new ArrayList<Map<String,Object>>();
					break;
				}
			}
			
			formatMapDataOut(data,params);
			result.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	@Override
	public Map<String, Object> getMapEventListCount(Map<String, Object> params,UserInfo userInfo) {
		Map<String, Object> result=new HashMap<String,Object>();
		// 获取总数
		try {
			
			String eventType="";
			String listType="0";
			if(CommonFunctions.isNotBlank(params, "eventType")) {
				eventType=params.get("eventType").toString();
			}
			if(CommonFunctions.isNotBlank(params, "listType")) {
				listType=params.get("listType").toString();
			}
			
			if(eventType.equals("todo")) {
				params.put("listType", "1");
				listType="1";
			}else if(eventType.equals("all")) {
				params.put("listType", "5");
				listType="5";
			}else if(eventType.equals("proruption")) {//紧急程度为突发
				params.put("listType", "5");
				params.put("urgencyDegree", "02");
				listType="5";
			}else if(eventType.equals("sensitive")) {//紧急程度为敏感
				params.put("listType", "5");
				params.put("urgencyDegree", "03");
				listType="5";
			}else if(eventType.equals("twoViolation")) {//两违建筑
				params.put("listType", "5");
				params.put("type", "92");
				listType="5";
			}else if(eventType.equals("eco")) {//生态事件
				params.put("listType", "5");
				params.put("type", "31");
				listType="5";
			}else if(eventType.equals("epo")) {//环保事件
				params.put("listType", "5");
				params.put("type", "31");
				listType="5";
			}else if(eventType.equals("IllegalLandUse")) {//违法用地
				params.put("listType", "5");
				params.put("type", "9201");
				listType="5";
			}else if(eventType.equals("IllegalConstruction")) {//违法建筑
				params.put("listType", "5");
				params.put("type", "9202");
				listType="5";
			}else if(eventType.equals("spotCase")) {//卫星图斑案件
				params.put("listType", "5");
				params.put("type", "9203");
				listType="5";
			}else if(eventType.equals("natural")) {//自然灾害事件
				params.put("listType", "5");
				params.put("type", "0804");
				listType="5";
			}else if(eventType.startsWith("reportType_")) {//入格
				params.put("listType", "51");
				params.put("reportType", eventType.split("_")[1]);
				if(CommonFunctions.isNotBlank(params, "createTimeStart")) {
					params.put("reportDayStart", params.get("createTimeStart"));
				}
				if(CommonFunctions.isNotBlank(params, "createTimeEnd")) {
					params.put("reportDayEnd", params.get("createTimeEnd"));
				}
				if(CommonFunctions.isNotBlank(params, "keyRemarkWord")) {
					params.put("keyWord", params.get("keyRemarkWord"));
				}
				if(CommonFunctions.isNotBlank(params, "code")) {
					params.put("reportCode", params.get("code"));
				}
				if(CommonFunctions.isNotBlank(params, "infoOrgCode")) {
					params.put("regionCode", params.get("infoOrgCode"));
				}
				listType="reportType";
			} else if(eventType.equals("doing_all")) {//辖区所有在办(南安)
				listType="1_all";
				if(CommonFunctions.isNotBlank(params, "keyRemarkWord")) {
					params.put("keyWord", params.get("keyRemarkWord"));
				}
			}
			
			formatParamIn(params);
			switch (listType) {
			case "1"://待办
				result.put("total", event4WorkflowForMapMapper.findCount4Todo(params));
				break;
			case "5"://辖区所有
				result.put("total", event4WorkflowForMapMapper.findCount4Jurisdiction(params));
				break;
			case "reportType"://入格
				result.put("total", reportIntegrationService.findCount4ReportFocus(params));
				break;
			case "1_all"://辖区所有在办(南安)
				result.put("total", eventAndReportJsonpMapper.findAllDoingEventForNananCount(params));
				break;
			default:
				result.put("total", 0);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	@Override
	public Map<String, Object> getMapComponentList(Map<String, Object> params,UserInfo userInfo) {
		Map<String, Object> result=new HashMap<String,Object>();
		result.put("total", 0);
		result.put("data", new ArrayList<Map<String,Object>>());
		// 获取总数
		try {
			
			if(CommonFunctions.isNotBlank(params, "classCode")) {
				String influenceDegree = params.get("classCode").toString();
				if(influenceDegree.contains(",")) {
					params.put("classCodeArray", influenceDegree.split(","));
				}
			}
			
			result.put("total", eventDisposal4JsonpMapper.getMapComponentListCount(params));
			List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
			if (CommonFunctions.isNotBlank(params, "page") && CommonFunctions.isNotBlank(params, "rows")) {
				//传入分页参数使用分页查询
				//构造分页对象
				
				Integer page=1;
				Integer rows=10;
				if(CommonFunctions.isNotBlank(params, "page")) {
					page=Integer.valueOf(params.get("page").toString());
				}
				if(CommonFunctions.isNotBlank(params, "rows")) {
					rows=Integer.valueOf(params.get("rows").toString());
				}
				
				page = page < 1 ? 1 : page;
				rows = rows < 1 ? 10 : rows;
				
				RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
				
				data=eventDisposal4JsonpMapper.getMapComponentList(params,rowBounds);
			} else {
				//未传入分页参数则查询满足条件的所有事件
				data=eventDisposal4JsonpMapper.getMapComponentList(params);
			}
			
			//字典翻译D005011
			Map<String, Object> dictMap = new HashMap<String, Object>();
			dictMap.put("orgCode", userInfo.getOrgCode());
			dictMap.put("dictPcode", ConstantValue.URBAN_TYPE_PCODE);
			List<BaseDataDict> urbanTypeDict = baseDictionaryService.findDataDictListByCodes(dictMap);
			for (Map<String, Object> temp : data) {
				// 设置部件分类全路径
				if(CommonFunctions.isNotBlank(temp, "classCode") && temp != null) {
					Map<String, Object> eventTypeMap = DataDictHelper.capMultilevelDictInfo(temp.get("classCode").toString(), ConstantValue.URBAN_TYPE_PCODE, urbanTypeDict);
					
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictName")) {
						temp.put("className", eventTypeMap.get("dictName"));
					}
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
						temp.put("classFullName", eventTypeMap.get("dictFullPath"));
					}
				}
				
				if(CommonFunctions.isNotBlank(temp, "objStatus")) {
					if(UrbanObjStatusMapping.containsKey(temp.get("objStatus").toString())) {
						temp.put("objStatusStr", UrbanObjStatusMapping.get(temp.get("objStatus").toString()));
					}
				}
				
			}
			
			result.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	@Override
	public Map<String, Object> getMapComponentListCount(Map<String, Object> params,UserInfo userInfo) {
		Map<String, Object> result=new HashMap<String,Object>();
		result.put("total", 0);
		// 获取总数
		try {
			
			if(CommonFunctions.isNotBlank(params, "classCode")) {
				String influenceDegree = params.get("classCode").toString();
				if(influenceDegree.contains(",")) {
					params.put("classCodeArray", influenceDegree.split(","));
				}
			}
			
			result.put("total", eventDisposal4JsonpMapper.getMapComponentListCount(params));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	private void formatMapDataOut(List<Map<String, Object>> eventList, Map<String, Object> params) {
		if(eventList != null && eventList.size() > 0) {
			Map<String, Object> dictMap = new HashMap<String, Object>();
			String userOrgCode = null;
			Integer listType = 0;
			List<BaseDataDict> influenceDegreeDictList = null,
							   urgencyDegreeDictList = null,
							   statusDictList = null,
							   subStatusDictList = null,
							   eventTypeDict = null,
							   evaLevelDictList = null,
							   sourceDictList = null,
							   patrolTypeDictList=null,
							   collectWayDictList = null; 
			List<Node> workflowNodeList = null;
			boolean isCapCurNodeInfo = false,
					      isCapCurHandlerName = false;
			
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
			sourceDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SOURCE_PCODE, userOrgCode);
			
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
					
					collectWayDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.COLLECT_WAY_PCODE, userOrgCode);
					patrolTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.PATROL_TYPE_PCODE, userOrgCode);
					
					if(CommonFunctions.isNotBlank(params, "isCapCurHandlerName")) {
						isCapCurHandlerName = Boolean.valueOf(params.get("isCapCurHandlerName").toString());
					}
					
					break;
				}
			}
			
			if(isCapCurNodeInfo) {
				Long workflowId = null;//为了让queryNodes找到对应的方法
				
				workflowNodeList = eventDisposalWorkflowService.queryNodes(workflowId);
			}
			String reportType = null;
			if(params.get("reportType")!=null){
				reportType ="reportType_"+ params.get("reportType").toString();
			}
			for(Map<String, Object> eventMap : eventList) {
				userOrgCode = null; 
				if(reportType!=null) {
					eventMap.put("reportType", reportType);
				}
				// 设置事件分类全路径
				if(CommonFunctions.isNotBlank(eventMap, "type") && eventTypeDict != null) {
					Map<String, Object> eventTypeMap = DataDictHelper.capMultilevelDictInfo(eventMap.get("type").toString(), ConstantValue.BIG_TYPE_PCODE, eventTypeDict);
					
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictName")) {
						eventMap.put("typeName", eventTypeMap.get("dictName"));
					}
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
						eventMap.put("eventClass", eventTypeMap.get("dictFullPath"));
					}
				}
				if(CommonFunctions.isNotBlank(eventMap, "curNodeName")) {
					if(workflowNodeList != null) {
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
					
					//用当前环节名称是否存在，来判断流程是否已归档，已归档事件不进行人员转换
					if(isCapCurHandlerName) {
						Long instanceId = null;
						Map<String, Object> curDataMap = null;
						StringBuffer curHandlerName = new StringBuffer("");
						String[] userNameArray = null, orgNameArray = null;
						
						if(CommonFunctions.isNotBlank(eventMap, "instanceId")) {
							instanceId = Long.valueOf(eventMap.get("instanceId").toString());
						}
						
						if(instanceId != null && instanceId > 0) {
							curDataMap = eventDisposalWorkflowService.curNodeData(instanceId);
							
							if(CommonFunctions.isNotBlank(curDataMap, "WF_USERNAME_ALL")) {
								userNameArray = curDataMap.get("WF_USERNAME_ALL").toString().split(",");
							}
							
							if(CommonFunctions.isNotBlank(curDataMap, "WF_ORGNAME_ALL")) {
								orgNameArray = curDataMap.get("WF_ORGNAME_ALL").toString().split(",");
							}
						}
						
						if(userNameArray != null && orgNameArray != null) {
							for(int index = 0, userNameLen = userNameArray.length, orgNameLen = orgNameArray.length; index < userNameLen; index++) {
								curHandlerName.append(userNameArray[index]);
								
								if(orgNameLen > index) {
									curHandlerName.append("(").append(orgNameArray[index]).append(");");
								}
							}
						} else if(orgNameArray != null) {
							for(String orgName : orgNameArray) {
								curHandlerName.append("(").append(orgName).append(");");
							}
						}
						
						if(curHandlerName.length() > 0) {
							eventMap.put("curHandlerName", curHandlerName.toString());
						}
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
				
				// 事件巡访类型
				DataDictHelper.setDictValueForField(eventMap, "patrolType", "patrolTypeName", patrolTypeDictList);
				
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
				if(CommonFunctions.isNotBlank(eventMap, "reporterName")) {
					if(CommonFunctions.isBlank(eventMap, "eventName")) {
						eventMap.put("eventName", eventMap.get("reporterName"));
					}
				}
				if(CommonFunctions.isNotBlank(eventMap, "reportCode")) {
					if(CommonFunctions.isBlank(eventMap, "eventId")) {
						eventMap.put("eventId", eventMap.get("reportCode"));
					}
				}
				if(CommonFunctions.isNotBlank(eventMap, "reportTime")) {
					eventMap.put("reportTimeStr", DateUtils.formatDate((Date)eventMap.get("reportTime"), DateUtils.PATTERN_24TIME));
					if(CommonFunctions.isBlank(eventMap, "createTime")) {
						eventMap.put("createTime", eventMap.get("reportTime"));
						eventMap.put("createTimeStr", eventMap.get("reportTimeStr"));
					}
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


	private void formatParamInForSimilar(Map<String, Object> params,UserInfo userInfo) throws ZhsqEventException {
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
			defaultStatusList.add(ConstantValue.EVENT_STATUS_RECEIVED);
			defaultStatusList.add(ConstantValue.EVENT_STATUS_REPORT);
			defaultStatusList.add(ConstantValue.EVENT_STATUS_DISTRIBUTE);
			defaultStatusList.add(ConstantValue.EVENT_STATUS_ARCHIVE);
			
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
				case 5: {//辖区所有事件
					defaultStatusList.add(ConstantValue.EVENT_STATUS_END);
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
			if(CommonFunctions.isNotBlank(params, "subStatus")) {
				String subStatus = params.get("subStatus").toString();
				if(subStatus.contains(",")) {
					params.put("subStatusArray", subStatus.split(","));
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
			if(CommonFunctions.isNotBlank(params, "eventLabel")) {
				Object evaLevelListObj = params.get("eventLabel");
				if(evaLevelListObj instanceof String) {
					params.put("eventLabelList", Arrays.asList(evaLevelListObj.toString().split(",")));
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
			if(CommonFunctions.isNotBlank(params, "eventAttrTrigger")) {
				String eventAttrTrigger = params.get("eventAttrTrigger").toString(),
					   eventAttrName = "",
					   orgCode = "";
				
				if(CommonFunctions.isNotBlank(params, "eventAttrOrgCode")){
					orgCode = params.get("eventAttrOrgCode").toString();
				} else if(CommonFunctions.isNotBlank(params, "orgCode")) {
					orgCode = params.get("orgCode").toString();
				}
				
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
					String trigger = params.get("trigger").toString(),
						   orgCode = "";
					
					if(CommonFunctions.isNotBlank(params, "orgCode")) {
						orgCode = params.get("orgCode").toString();
					}
					
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
			
			
			//相似事件相关
			//获取相似事件参数规则
			Integer similarCreateTime=1;//默认查询前后一天的范围内的相似事件
			try {
				String similarCreateTimeStr=funConfigurationService.turnCodeToValue(ConstantValue.SIMILAR_EVENT_RULE, ConstantValue.SIMILAR_CREATE_TIME, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
				if(StringUtils.isNotBlank(similarCreateTimeStr)) {
					similarCreateTime=Integer.valueOf(similarCreateTimeStr);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(CommonFunctions.isNotBlank(params, "eliminateEventIdList")) {
				List<Long> curEvent=(List<Long>) params.get("eliminateEventIdList");
				EventDisposal findEventByIdSimple = eventDisposalService.findEventByIdSimple(curEvent.get(0));
				Date createTime = findEventByIdSimple.getCreateTime();
				params.put("createTimeStart", DateUtils.formatDate(DateUtils.addInterval(createTime, -similarCreateTime, "00"),DateUtils.PATTERN_DATE));
				params.put("createTimeEnd", DateUtils.formatDate(DateUtils.addInterval(createTime, similarCreateTime, "00"),DateUtils.PATTERN_DATE));
			}
			
		}
	}
	
	
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
			defaultStatusList.add(ConstantValue.EVENT_STATUS_RECEIVED);
			defaultStatusList.add(ConstantValue.EVENT_STATUS_REPORT);
			defaultStatusList.add(ConstantValue.EVENT_STATUS_DISTRIBUTE);
			defaultStatusList.add(ConstantValue.EVENT_STATUS_ARCHIVE);
			
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
				case 5: {//辖区所有事件
					defaultStatusList.add(ConstantValue.EVENT_STATUS_END);
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
			if(CommonFunctions.isNotBlank(params, "subStatus")) {
				String subStatus = params.get("subStatus").toString();
				if(subStatus.contains(",")) {
					params.put("subStatusArray", subStatus.split(","));
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
			if(CommonFunctions.isNotBlank(params, "eventLabel")) {
				Object evaLevelListObj = params.get("eventLabel");
				if(evaLevelListObj instanceof String) {
					params.put("eventLabelList", Arrays.asList(evaLevelListObj.toString().split(",")));
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
			if(CommonFunctions.isNotBlank(params, "eventAttrTrigger")) {
				String eventAttrTrigger = params.get("eventAttrTrigger").toString(),
					   eventAttrName = "",
					   orgCode = "";
				
				if(CommonFunctions.isNotBlank(params, "eventAttrOrgCode")){
					orgCode = params.get("eventAttrOrgCode").toString();
				} else if(CommonFunctions.isNotBlank(params, "orgCode")) {
					orgCode = params.get("orgCode").toString();
				}
				
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
					String trigger = params.get("trigger").toString(),
						   orgCode = "";
					
					if(CommonFunctions.isNotBlank(params, "orgCode")) {
						orgCode = params.get("orgCode").toString();
					}
					
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


	@Override
	public Map<String, Object> getEventTotalByStatistics(Map<String, Object> params, UserInfo userInfo) {
		this.formatStatisticsParamsSearch(params,userInfo);
		return eventDisposal4JsonpMapper.getEventTotalByStatistics(params);
	}


	@Override
	public List<Map<String, Object>> getEventAreaDistributionByStatistics(Map<String, Object> params, UserInfo userInfo) {
		this.formatStatisticsParamsSearch(params,userInfo);
		if(CommonFunctions.isNotBlank(params, "infoOrgCode")) {
			//将传入的code转成id
			MixedGridInfo gridInfo = mixedGridInfoService.getGridInfoByInfoOrgCode(params.get("infoOrgCode").toString());
			if(gridInfo!=null) {
				params.put("gridId", gridInfo.getGridId());
			}
		}
		return eventDisposal4JsonpMapper.getEventAreaDistributionByStatistics(params);
	}
	
	private void formatStatisticsParamsSearch(Map<String, Object> params,UserInfo userInfo) {
		formatStatisticsParamsSearch(params, StatisticsMapping, userInfo);
	}
	
	/**
	 * 统计项格式化
	 * @param params			需要格式化的参数
	 * @param statisticsMapping	统计项映射文件
	 * @param userInfo			操作用户信息
	 */
	private void formatStatisticsParamsSearch(Map<String, Object> params, Map<String, String> statisticsMapping, UserInfo userInfo) {
		//格式化查询字段
		try {
			Set<String> searchSet=new HashSet<String>();
			
			if(CommonFunctions.isNotBlank(params, "fields")) {
				String[] fieldSplit = params.get("fields").toString().split(",");
				List<Map<String,Object>> fieldArray=new ArrayList<Map<String,Object>>();
				for (String filed : fieldSplit) {
					Map<String,Object> temp=new HashMap<String,Object>();
					if(statisticsMapping.containsKey(filed)) {
						temp.put("fieldName", statisticsMapping.get(filed));
						temp.put("exportName", filed);
						searchSet.add(filed);
						fieldArray.add(temp);
					}
				}
				if(fieldArray.size()>0) {
					params.put("fieldArray", fieldArray);
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "sortFields")) {
				
				String[] sortFieldSplit = params.get("sortFields").toString().split(",");
				List<Map<String,Object>> sortFieldArray=new ArrayList<Map<String,Object>>();
				for (String sortField : sortFieldSplit) {
					String[] tempSplit = sortField.split("_");
					Map<String,Object> temp=new HashMap<String,Object>();
					if(searchSet.contains(tempSplit[0])) {
						temp.put("sortFieldName", statisticsMapping.get(tempSplit[0]));
						if(tempSplit.length>1&&StringUtils.isNotBlank(tempSplit[1])) {
							if(tempSplit[1].equals("D")) {
								temp.put("sortType", "DESC");
							}else {
								temp.put("sortType", "ASC");
							}
						}else {
							temp.put("sortType", "ASC");
						}
						sortFieldArray.add(temp);
					}
				}
				if(sortFieldArray.size()>0) {
					params.put("sortFieldArray", sortFieldArray);
				}
				
			}
			
			if(CommonFunctions.isNotBlank(params, "type")) {
				String bizPlatform = params.get("type").toString();
				
				if(bizPlatform.contains(",")) {
					params.put("typeArray", bizPlatform.split(","));
					params.remove("type");
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "pType")) {
				String bizPlatform = params.get("pType").toString();
				
				if(bizPlatform.contains(",")) {
					params.put("pTypeArray", bizPlatform.split(","));
					params.remove("pType");
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "source")) {
				String bizPlatform = params.get("source").toString();
				
				if(bizPlatform.contains(",")) {
					params.put("sourceArray", bizPlatform.split(","));
					params.remove("source");
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "urgencyDegree")) {
				String bizPlatform = params.get("urgencyDegree").toString();
				
				if(bizPlatform.contains(",")) {
					params.put("urgencyDegreeArray", bizPlatform.split(","));
					params.remove("urgencyDegree");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(CommonFunctions.isNotBlank(params, "isEmtTypeEvent")) {
			params.put("isEmtTypeEvent", Boolean.valueOf(params.get("isEmtTypeEvent").toString()));
		}
	}

	@Override
	public List<Map<String, Object>> getEventAreaDistributionByActual(Map<String, Object> params, UserInfo userInfo) {
		//格式化输入参数
		try {
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
			if(CommonFunctions.isNotBlank(params, "bizPlatform")) {
				String bizPlatform = params.get("bizPlatform").toString();
				
				if(bizPlatform.contains(",")) {
					params.put("bizPlatformArray", bizPlatform.split(","));
					params.remove("bizPlatform");
				}
			}
			if(CommonFunctions.isNotBlank(params, "status")) {
				String status = params.get("status").toString();
				
				if(status.contains(",")) {
					params.put("statusArray", status.split(","));
					params.remove("status");
				}
			}
			if(CommonFunctions.isNotBlank(params, "infoOrgCode")) {
				//将传入的code转成ID
				MixedGridInfo gridInfo = mixedGridInfoService.getGridInfoByInfoOrgCode(params.get("infoOrgCode").toString());
				if(gridInfo!=null) {
					params.put("gridId", gridInfo.getGridId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventDisposal4JsonpMapper.getEventAreaDistributionByActual(params);
	}
	
	
	@Override
	public List<Map<String, Object>> getEventTypeProportionByActual(Map<String, Object> params, UserInfo userInfo) {
		//格式化输入参数
		try {
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
			if(CommonFunctions.isNotBlank(params, "bizPlatform")) {
				String bizPlatform = params.get("bizPlatform").toString();
				
				if(bizPlatform.contains(",")) {
					params.put("bizPlatformArray", bizPlatform.split(","));
					params.remove("bizPlatform");
				}
			}
			if(CommonFunctions.isNotBlank(params, "status")) {
				String status = params.get("status").toString();
				
				if(status.contains(",")) {
					params.put("statusArray", status.split(","));
					params.remove("status");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventDisposal4JsonpMapper.getEventTypeProportionByActual(params);
	}


	@Override
	public List<Map<String, Object>> getEventTypeProportionByStatistics(Map<String, Object> params, UserInfo userInfo) {
		this.formatStatisticsParamsSearch(params, userInfo);
		return eventDisposal4JsonpMapper.getEventTypeProportionByStatistics(params);
	}


	@Override
	public List<Map<String, Object>> getEventTrendByActual(Map<String, Object> params, UserInfo userInfo) {
		if(CommonFunctions.isNotBlank(params, "type")) {
			String typesStr = params.get("type").toString();
			if(typesStr.contains(",")) {
				String searchType=typesStr.replaceAll(",", "|");
				params.put("types", searchType);
			}
		}
		if(CommonFunctions.isNotBlank(params, "status")) {
				params.put("status", params.get("status").toString().split(","));
		}
		this.formatStatisticsParamsSearch(params, userInfo);
		String createTimeStart="";
		String createTimeEnd="";
		String trendType="";
		if(CommonFunctions.isNotBlank(params, "createTimeStart")) {
			createTimeStart=params.get("createTimeStart").toString();
		}
		if(CommonFunctions.isNotBlank(params, "createTimeEnd")) {
			createTimeEnd=params.get("createTimeEnd").toString();
		}
		if(CommonFunctions.isNotBlank(params, "trendType")) {
			trendType=params.get("trendType").toString();
		}
		List<String> trendTime = this.getTrendTime(createTimeStart,createTimeEnd,trendType);
		if(trendTime.size()>0 ) {
			if(trendTime.size()<=62) {
				params.put("trendTimeArray", trendTime);
			}else {
				Map<String,Object> msgMap=new HashMap<String,Object>();
				msgMap.put("msg", "目前仅支持查询的项数不能大于62，请修改查询参数");
				List<Map<String, Object>> arrayList = new ArrayList<Map<String,Object>>();
				arrayList.add(msgMap);
				return arrayList;
			}
		}else {
			return new ArrayList<Map<String,Object>>();
		}
		
		List<String> searchFieldList=new ArrayList<String>();
		
		try {
			if(CommonFunctions.isNotBlank(params, "fields")) {
				String fieldStr = params.get("fields").toString();
				if(fieldStr.contains(",")) {
					
					String[] fieldStrSplit = fieldStr.split(",");
					for (String tp : fieldStrSplit) {
						searchFieldList.add(tp);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String,Object> searchFieldMap=new HashMap<String,Object>();
		if(searchFieldList.size()>0) {
			for (String string : searchFieldList) {
				searchFieldMap.put(string, 0);
				params.put(string+"Search", "1");
			}
		}else {
			searchFieldMap.put("eventTotal", 0);
		}
		
		List<Map<String, Object>> result = eventDisposal4JsonpMapper.getEventTrendByActual(params);
		Map<String,Map<String,Object>> resultMap=new HashMap<String,Map<String,Object>>();
		for (Map<String, Object> map : result) {
			resultMap.put(map.get("trendDate").toString(), map);
		}
		
		
		
		
		
		List<Map<String, Object>> thisResult = new ArrayList<Map<String,Object>>();
		
		for (String temp : trendTime) {
			Map<String,Object> tempMap=new HashMap<String,Object>();
			if(resultMap.containsKey(temp)) {
				tempMap.putAll(resultMap.get(temp));
			}else {
				tempMap.put("trendDate",temp);
				tempMap.putAll(searchFieldMap);
			}
			thisResult.add(tempMap);
		}
		
		
		return thisResult;
	}


	@Override
	public List<Map<String, Object>> getEventTrendByStatistics(Map<String, Object> params, UserInfo userInfo) {
		this.formatStatisticsParamsSearch(params, userInfo);
		String createTimeStart="";
		String createTimeEnd="";
		String trendType="";
		if(CommonFunctions.isNotBlank(params, "createTimeStart")) {
			createTimeStart=params.get("createTimeStart").toString();
		}
		if(CommonFunctions.isNotBlank(params, "createTimeEnd")) {
			createTimeEnd=params.get("createTimeEnd").toString();
		}
		if(CommonFunctions.isNotBlank(params, "trendType")) {
			trendType=params.get("trendType").toString();
		}
		List<String> trendTime = this.getTrendTime(createTimeStart,createTimeEnd,trendType);
		if(trendTime.size()>0 ) {
			if(trendTime.size()<=62) {
				params.put("trendTimeArray", trendTime);
			}else {
				Map<String,Object> msgMap=new HashMap<String,Object>();
				msgMap.put("msg", "目前仅支持查询的项数不能大于62，请修改查询参数");
				List<Map<String, Object>> arrayList = new ArrayList<Map<String,Object>>();
				arrayList.add(msgMap);
				return arrayList;
			}
		}else {
			return new ArrayList<Map<String,Object>>();
		}
		
		return eventDisposal4JsonpMapper.getEventTrendByStatistics(params);
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


	@Override
	public List<Map<String, Object>> getSpecialEventTypeProportionByActual(Map<String, Object> params,
			UserInfo userInfo) {
		//格式化输入参数
		List<String> searchTypeList=new ArrayList<String>();
		List<String> searchFieldList=new ArrayList<String>();
		
		try {
			if(CommonFunctions.isNotBlank(params, "types")) {
				String typesStr = params.get("types").toString();
				if(typesStr.contains(",")) {
					String searchType=typesStr.replaceAll(",", "|");
					params.put("types", searchType);
					
					String[] split = typesStr.split(",");
					for (String tp : split) {
						searchTypeList.add(tp);
					}
				}
			}
			if(CommonFunctions.isNotBlank(params, "fields")) {
				String fieldStr = params.get("fields").toString();
				if(fieldStr.contains(",")) {
					
					String[] fieldStrSplit = fieldStr.split(",");
					for (String tp : fieldStrSplit) {
						searchFieldList.add(tp);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String,Object> searchFieldMap=new HashMap<String,Object>();
		if(searchFieldList.size()>0) {
			for (String string : searchFieldList) {
				searchFieldMap.put(string, 0);
				params.put(string+"Search", "1");
			}
		}else {
			searchFieldMap.put("eventTotal", 0);
		}
		
		//获取查询结果
		List<Map<String, Object>> result = eventDisposal4JsonpMapper.getSpecialEventTypeProportionByActual(params);
		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		for (Map<String, Object> map : result) {
			if(CommonFunctions.isNotBlank(map, "eventType")) {
				resultMap.put(map.get("eventType").toString(), map);
			}
		}
		
		List<Map<String, Object>> backResult=new ArrayList<Map<String, Object>>();
		
		//获取字典
		List<BaseDataDict> eventTypeDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.BIG_TYPE_PCODE, userInfo.getOrgCode());
		if(searchTypeList.size()>0) {
			//用户传入了指定的事件类型进行查询
			for (BaseDataDict baseDataDict : eventTypeDict) {
				if(searchTypeList.contains(baseDataDict.getDictGeneralCode())) {
					Map<String,Object> thisTemp=new HashMap<String,Object>();
					thisTemp.put("dictName", baseDataDict.getDictName());
					if(resultMap.containsKey(baseDataDict.getDictGeneralCode())) {
						thisTemp.putAll((Map<String,Object>) resultMap.get(baseDataDict.getDictGeneralCode()));
					}else {
						thisTemp.put("eventType", baseDataDict.getDictGeneralCode());
						thisTemp.putAll(searchFieldMap);
					}
					backResult.add(thisTemp);
				}
			}
		}else {
			//否则查询所有的事件类型
			for (BaseDataDict baseDataDict : eventTypeDict) {
				Map<String,Object> thisTemp=new HashMap<String,Object>();
				thisTemp.put("dictName", baseDataDict.getDictName());
				if(resultMap.containsKey(baseDataDict.getDictGeneralCode())) {
					thisTemp.putAll((Map<String,Object>) resultMap.get(baseDataDict.getDictGeneralCode()));
				}else {
					thisTemp.put("eventType", baseDataDict.getDictGeneralCode());
					thisTemp.putAll(searchFieldMap);
				}
				backResult.add(thisTemp);
			}
		}
		
		
		return backResult;
	}


	@Override
	public List<Map<String, Object>> getSpecialEventTypeAreaByActual(Map<String, Object> params, UserInfo userInfo) {
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		//获取下级所有地域
		if(CommonFunctions.isNotBlank(params, "infoOrgCode")) {
			List<MixedGridInfo> curGrid = mixedGridInfoService.findGridInfoByInfoOrgCode(params.get("infoOrgCode").toString());
			if(curGrid!=null&&curGrid.size()>0) {
				List<MixedGridInfo> childGrid=mixedGridInfoService.getMixedGridListByParentId(curGrid.get(0).getGridId(), false, false);
				for (MixedGridInfo mixedGridInfo : childGrid) {
					//查询各个类型的事件总量
					Map<String,Object> searchParams=new HashMap<String,Object>();
					searchParams.putAll(params);
					searchParams.put("infoOrgCode", mixedGridInfo.getInfoOrgCode());
					List<Map<String, Object>> specialEventTypeProportionByActual = this.getSpecialEventTypeProportionByActual(searchParams, userInfo);
					Map<String,Object> tempMap=new HashMap<String,Object>();
					tempMap.put("infoOrgCode", mixedGridInfo.getInfoOrgCode());
					tempMap.put("gridName", mixedGridInfo.getGridName());
					tempMap.put("typeTotal", specialEventTypeProportionByActual);
					result.add(tempMap);
				}
				
			}
		}
		
		return result;
	}
	
	@Override
	public List<Map<String, Object>> findPositionEventByStatistics(Map<String, Object> params) throws Exception {
		List<Map<String,Object>> resultMapList = null;
		params = params == null ? new HashMap<String, Object>() : params;
		
		formatStatisticsParamsSearch(params, POSITION_STATISTICS_MAPPING, new UserInfo());
		
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			throw new IllegalArgumentException("缺少地域编码【infoOrgCode】！");
		}
		
		if(CommonFunctions.isBlank(params, "statMonthStr") 
				&& CommonFunctions.isBlank(params, "statStartMonthStr")
				&& CommonFunctions.isBlank(params, "statEndMonthStr")) {
			throw new IllegalArgumentException("缺少时间范围查询条件！");
		}
		
		String PATTERN_MONTH = "yyyyMM";
		
		if(CommonFunctions.isNotBlank(params, "statMonthStr")) {
			Object statMonthStrObj = params.get("statMonthStr");
			
			if(statMonthStrObj instanceof String) {
				try {
					params.put("statMonthStr", DateUtils.formatDate(DateUtils.convertStringToDate(statMonthStrObj.toString(), DateUtils.PATTERN_MONTH), PATTERN_MONTH));
				} catch(ParseException e) {
					throw new ZhsqEventException("【statMonthStr】：【" + statMonthStrObj +"】不满足如下时间格式：" + DateUtils.PATTERN_MONTH + "！");
				}
			}
		}
		if(CommonFunctions.isNotBlank(params, "statStartMonthStr")) {
			Object statStartMonthStrObj = params.get("statStartMonthStr");
			
			if(statStartMonthStrObj instanceof String) {
				try {
					params.put("statStartMonthStr", DateUtils.formatDate(DateUtils.convertStringToDate(statStartMonthStrObj.toString(), DateUtils.PATTERN_MONTH), PATTERN_MONTH));
				} catch(ParseException e) {
					throw new ZhsqEventException("【statStartMonthStr】：【" + statStartMonthStrObj +"】不满足如下时间格式：" + DateUtils.PATTERN_MONTH + "！");
				}
			}
		}
		if(CommonFunctions.isNotBlank(params, "statEndMonthStr")) {
			Object statEndMonthStrObj = params.get("statEndMonthStr");
			
			if(statEndMonthStrObj instanceof String) {
				try {
					params.put("statEndMonthStr", DateUtils.formatDate(DateUtils.convertStringToDate(statEndMonthStrObj.toString(), DateUtils.PATTERN_MONTH), PATTERN_MONTH));
				} catch(ParseException e) {
					throw new ZhsqEventException("【statEndMonthStr】：【" + statEndMonthStrObj +"】不满足如下时间格式：" + DateUtils.PATTERN_MONTH + "！");
				}
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "positionName")) {
			String positionName = params.get("positionName").toString();
			
			if(positionName.contains(",")) {
				params.put("positionNameArray", positionName.split(","));
				params.remove("positionName");
			}
				
		}
		
		resultMapList = eventDisposal4JsonpMapper.findPositionEventByStatistics(params);
		
		return resultMapList;
	}
	
	@Override
	public List<Map<String, Object>> findUserEventByStatistics(Map<String, Object> params) throws Exception {
		List<Map<String,Object>> resultMapList = null;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			throw new IllegalArgumentException("缺少地域编码【infoOrgCode】！");
		}
		
		if(CommonFunctions.isBlank(params, "statMonthStr") 
				&& CommonFunctions.isBlank(params, "statStartMonthStr")
				&& CommonFunctions.isBlank(params, "statEndMonthStr")) {
			throw new IllegalArgumentException("缺少时间范围查询条件！");
		}
		
		String PATTERN_MONTH = "yyyyMM";
		
		if(CommonFunctions.isNotBlank(params, "statMonthStr")) {
			Object statMonthStrObj = params.get("statMonthStr");
			
			if(statMonthStrObj instanceof String) {
				try {
					params.put("statMonthStr", DateUtils.formatDate(DateUtils.convertStringToDate(statMonthStrObj.toString(), DateUtils.PATTERN_MONTH), PATTERN_MONTH));
				} catch(ParseException e) {
					throw new ZhsqEventException("【statMonthStr】：【" + statMonthStrObj +"】不满足如下时间格式：" + DateUtils.PATTERN_MONTH + "！");
				}
			}
		}
		if(CommonFunctions.isNotBlank(params, "statStartMonthStr")) {
			Object statStartMonthStrObj = params.get("statStartMonthStr");
			
			if(statStartMonthStrObj instanceof String) {
				try {
					params.put("statStartMonthStr", DateUtils.formatDate(DateUtils.convertStringToDate(statStartMonthStrObj.toString(), DateUtils.PATTERN_MONTH), PATTERN_MONTH));
				} catch(ParseException e) {
					throw new ZhsqEventException("【statStartMonthStr】：【" + statStartMonthStrObj +"】不满足如下时间格式：" + DateUtils.PATTERN_MONTH + "！");
				}
			}
		}
		if(CommonFunctions.isNotBlank(params, "statEndMonthStr")) {
			Object statEndMonthStrObj = params.get("statEndMonthStr");
			
			if(statEndMonthStrObj instanceof String) {
				try {
					params.put("statEndMonthStr", DateUtils.formatDate(DateUtils.convertStringToDate(statEndMonthStrObj.toString(), DateUtils.PATTERN_MONTH), PATTERN_MONTH));
				} catch(ParseException e) {
					throw new ZhsqEventException("【statEndMonthStr】：【" + statEndMonthStrObj +"】不满足如下时间格式：" + DateUtils.PATTERN_MONTH + "！");
				}
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "statisticsUserId")) {
			try {
				params.put("statisticsUserId", Long.valueOf(params.get("statisticsUserId").toString()));
			} catch(NumberFormatException e) {
				throw new IllegalArgumentException("【statisticsUserId】：【" + params.get("statisticsUserId") + "】不是有效数值！");
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "statisticsOrgId")) {
			try {
				params.put("statisticsOrgId", Long.valueOf(params.get("statisticsOrgId").toString()));
			} catch(NumberFormatException e) {
				throw new IllegalArgumentException("【statisticsOrgId】：【" + params.get("statisticsOrgId") + "】不是有效数值！");
			}
		}
		
		resultMapList = eventDisposal4JsonpMapper.findUserEventByStatistics(params);
		
		return resultMapList;
	}

}
