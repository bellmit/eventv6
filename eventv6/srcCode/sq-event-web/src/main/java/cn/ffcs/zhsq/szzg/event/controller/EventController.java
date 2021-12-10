package cn.ffcs.zhsq.szzg.event.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.ffcs.bi.report.service.gridEvent.IGridEventService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.common.FileUtils;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventDay.IEventDayService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.szzg.event.service.IEventDisposal4JsonpService;
import cn.ffcs.zhsq.szzg.statistics.service.IZgStatisticsService;
import cn.ffcs.zhsq.utils.Base64;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 事件
 * @author huangwenbin
 *
 */
@Controller
@RequestMapping("/zhsq/szzg/eventController")
public class EventController extends ZZBaseController{
	@Autowired
	private IGridEventService gridEventService;
	@Autowired
	private IZgStatisticsService statisticsService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	//@Autowired
	//private CacheService cacheService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IEventDisposal4JsonpService eventDisposal4JsonpService;
	
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request,HttpSession session, ModelMap map) {
		Calendar c = Calendar.getInstance();
		String date = request.getParameter("date");
		if(date == null	) {
			map.addAttribute("currentDate", c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+(c.get(Calendar.DAY_OF_MONTH)));
			c.add(Calendar.MONTH, -1);
			map.addAttribute("lastDate", c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+(c.get(Calendar.DAY_OF_MONTH)));
		}else {
			String[] dates = date.split("-");
			int year = Integer.parseInt(dates[0]);
			int startMonth = 0,endMonth = 0;
			int day = 0;
			if(year == c.get(Calendar.YEAR)) {//当前年
				if(dates.length>1) {//有月份
					startMonth = endMonth = Integer.parseInt(dates[1]);
					if(endMonth == c.get(Calendar.MONTH)+1) {//当前月
						day =c.get(Calendar.DAY_OF_MONTH);
					}else {
						c.set(Calendar.MONTH, endMonth-1);
						day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
					}
				}else {//无月份
					day =c.get(Calendar.DAY_OF_MONTH); 
					startMonth = 1;
					endMonth = c.get(Calendar.MONTH)+1;
				}
				
			}else {
				c.set(Calendar.YEAR, year);
				if(dates.length>1) {//有月份
					startMonth = endMonth = Integer.parseInt(dates[1]);
					c.set(Calendar.MONTH, endMonth-1);
					day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
				}else {
					startMonth =1; endMonth=12;day=31;
				}
			}
			map.addAttribute("lastDate", year+"-"+startMonth+"-1");
			map.addAttribute("currentDate", year+"-"+endMonth+"-"+day);
		
		}
		String autoPlay = request.getParameter("autoPlay");
		map.addAttribute("autoPlay",autoPlay!=null?autoPlay:"true" );
		return "/szzg/event/index_event.ftl";
	}	
	
	/**
	 * 辖区事件数量分布图
	 * @param infoOrgCode 地区编码
	 * @param occuDate 时间月份,格式YYYY-MM
	 */
	@ResponseBody
	@RequestMapping(value = "/findEventNum")
	public Map<String,Object> findEventNum(HttpSession session, 
			@RequestParam(value = "orgCode",required=false) String orgCode,
			@RequestParam(value = "date") String date) throws Exception {
		return gridEventService.getEventNum(this.getDefaultInfoOrgCode(session), date);
	}
	/** 办结率仪表图
	* @param infoOrgCode 地区编码
	* @param occuDate 时间月份,格式YYYY-MM
	*/
	@ResponseBody
	@RequestMapping(value = "/findComplete")
	public Map<String,Object> findComplete(HttpSession session, 
			@RequestParam(value = "orgCode",required=false) String orgCode,
			@RequestParam(value = "date") String date) throws Exception {
		return gridEventService.getComplete(this.getDefaultInfoOrgCode(session), date);
	}
	
	/**
	 * 辖区事件类型占比图
	 * @param infoOrgCode 地区编码
	 * @param orgCode 登录用户组织编码
	 * @param occuDate 时间月份,格式:YYYY-MM
	 * @param eventType 事件字典的业务编码
	 * @param dictCode 不传默认事件字典:A001093199
	 * @return
	 * @throws Exception
	 */

	@ResponseBody
	@RequestMapping(value = "/findEventType")
	public List<Map<String, Object>> findEventType(HttpSession session, 
			@RequestParam(value = "infoOrgCode",required=false) String infoOrgCode,
			@RequestParam(value = "orgCode",required=false) String orgCode,
			@RequestParam(value = "eventType",required=false) String eventType,
			@RequestParam(value = "dictCode",required=false) String dictCode,
			@RequestParam(value = "date") String date) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		return gridEventService.getEventType(this.getDefaultInfoOrgCode(session),  userInfo.getOrgCode(), date, null, null);
	}
	
	/**
	 *获取热力图数据 
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping(value = "/findEventHeatData")
	public Map<String,Object> findEventHeatData(HttpSession session, ModelMap map,	
			@RequestParam(value = "beginTime") String beginTime,
			@RequestParam(value = "month") Integer month,
			@RequestParam(value = "endTime") String endTime,
			@RequestParam(value = "orgCode", required=false) String orgCode,
			@RequestParam(value = "eventType", required=false) String eventType) throws Exception {
		Map<String,Object> param = new HashMap<String, Object>();
		if(StringUtils.isEmpty(orgCode) || !StringUtils.isNumber(orgCode)) {
			orgCode = getDefaultInfoOrgCode(session);
		}
		param.put("orgCode", orgCode);
		param.put("beginTime", beginTime);
		param.put("month", month);
		param.put("endTime", endTime);
		param.put("eventType", eventType);
		return statisticsService.findEventHeatData(param) ;
	}

	/**
	 *获取热力图数据
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/findEventHeatDataByJsonp")
	public String findEventHeatDataByJsonp(HttpSession session, ModelMap map,
											@RequestParam(value = "month") Integer month,
											@RequestParam(value = "beginTime") String beginTime,
											@RequestParam(value = "endTime") String endTime,
											@RequestParam(value = "orgCode", required=false) String orgCode,
											@RequestParam(value = "jsonpcallback") String jsonpcallback,
											@RequestParam(value = "eventType", required=false) String eventType) throws Exception {
		Map<String,Object> param = this.findEventHeatData(session,map,beginTime,month,endTime,orgCode,eventType);
		String data = JSONObject.fromObject(param).toString();
		return jsonpcallback + "(" + data +")";
	}

		/**
		 *跳转框选展示页面
		 */
		@RequestMapping(value = "/kuangxuanPage")
		public String kuangxuanPage(HttpServletRequest request,HttpSession session, ModelMap map,
				@RequestParam(value = "minID") Integer minID,
				@RequestParam(value = "points") String points,
				@RequestParam(value = "lastDateNo",required=false) String lastDateNo,
				@RequestParam(value = "maxID") Integer maxID) {
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("maxID", maxID);
			param.put("minID", minID);
			param.put("points", points);
			String orgCode = getDefaultInfoOrgCode(session);
			param.put("orgCode", orgCode);
			map.put("orgCode", orgCode);
			map.put("minID", minID);
			map.put("maxID", maxID);
			map.put("points", points);
			map.put("area", request.getParameter("area") );
			long dateNo = System.currentTimeMillis();
			map.put("dateNo", dateNo);
			param.put("dateNo", dateNo);
			if(lastDateNo != null &&StringUtils.isNumber(lastDateNo) ) {//删除框选旧数据防止表过大
				param.put("lastDateNo", lastDateNo);
				statisticsService.delLastKXData(param);
			}
			map.put("eventCount", statisticsService.findEventCount(param));
			param.put("dictGeneralCode", "EVENT_DENSITY_LEVEL");
			map.put("scope", baseDictionaryService.findDataDictListByCodes(param));
			return "/szzg/event/index_kuangxuan.ftl";
		}
		
		/**
		 *跳转框选数据展示页面
		 */
		@RequestMapping(value = "/kuangxuanListPage")
		public String kuangxuanListPage(HttpServletRequest request,HttpSession session, ModelMap map,
				@RequestParam(value = "dateNo") String dateNo,
				@RequestParam(value = "eventCount") Integer eventCount) {
			Map<String,Object> param = new HashMap<String, Object>();
			String orgCode = getDefaultInfoOrgCode(session);
			param.put("orgCode", orgCode);
			param.put("dateNo", dateNo);
			
			map.put("orgCode", orgCode);
			map.put("dateNo", dateNo);
			map.put("eventCount", eventCount);
			map.put("top5", statisticsService.findEventTop5(param));
			map.put("dataDict", baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.STATUS_PCODE, orgCode));
			return "/szzg/event/index_kuangxuanList.ftl";
		}
		
		/**
		 *获取框选数据 
		 */
		@ResponseBody
		@RequestMapping(value = "/findEventList")
		public EUDGPagination findEventList(HttpServletRequest request,HttpSession session, ModelMap map,
				@RequestParam(value = "page") int page,
				   @RequestParam(value = "rows") int rows,
				   @RequestParam(value = "orgCode") String orgCode
				 , @RequestParam(value = "dateNo") String dateNo
				   ,@RequestParam(value = "keyWord",required=false) String keyWord,
				   @RequestParam(value = "type",required=false) String type
				   ) {
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("orgCode", orgCode);
			param.put("dateNo", dateNo);
			param.put("keyWord", request.getParameter("keyWord"));
			param.put("type", request.getParameter("type"));
			return statisticsService.findEventList(page, rows,param) ;
		}
		
		
		@RequestMapping(value = "/indexEvent")
	public String index2(HttpServletRequest request,HttpSession session, ModelMap map,
			@RequestParam(value = "date", required = false) String date, 
			@RequestParam(value = "code", required = false,defaultValue = "") String code, 
			@RequestParam(value = "beginTime", required = false) String beginTime, 
			@RequestParam(value = "endTime", required = false) String endTime, 
			@RequestParam(value = "status", required = false) String status, 
			@RequestParam(value = "type", required = false)String type,
			@RequestParam(value = "handleDateFlag", required = false)String handleDateFlag) {
		map.put("type",type);
		map.put("code",code);
		map.put("date",date);
		map.put("status",status);
		if(code.startsWith("320903")) { //盐都区 省略事发时间  省略江苏省盐城市盐都区
			map.put("closeEventHappenTime", true);
		}
		if(date != null   && !"".equals(date)) {
			if(date.length() == 4) {
				map.put("beginTime",date+"-01-01");
				map.put("endTime", date+"-12-31 23:59:59");
			}else if(date.length() == 6) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6))-1, 1);
				map.put("beginTime",sdf.format(cal.getTime()));
				cal.add(Calendar.MONTH, 1);
				map.put("endTime",sdf.format(cal.getTime()));
			}
		}else if(beginTime != null   && endTime != null) {
			map.put("beginTime",beginTime);
			map.put("endTime", endTime);
		}
		//逾期事件
		if(!StringUtils.isBlank(handleDateFlag)) {
			map.put("handleDateFlag", handleDateFlag);
		}
		
		return "/szzg/event/eventList.ftl";
	}	
	
	@RequestMapping(value = "/getEventInfo")
	public String getEventInfo(HttpSession session, ModelMap map,
			@RequestParam(value = "eventId", required = false, defaultValue = "-1") Long eventId, 
			@RequestParam(value = "nofull", required = false) String nofull, 
			@RequestParam(value = "caseId", required = false, defaultValue = "-1") Long caseId) throws Exception {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		EventDisposal event = new EventDisposal();
		if(eventId != null && eventId > 0) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			Long instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
			list =  eventDisposalWorkflowService.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
			event = eventDisposalService.findEventById(eventId, userInfo.getOrgCode());
			List<Attachment> attlist =  attachmentService.findByBizId(eventId, "ZHSQ_EVENT", null);
			List<Attachment> imgs = new ArrayList<Attachment>();
			List<Attachment> sounds = new ArrayList<Attachment>();
			List<Attachment> videos = new ArrayList<Attachment>();
			String filePath="";
			String imgType="";
			for (Attachment att : attlist) {
				filePath=att.getFilePath();
				imgType = FileUtils.getFileExtension(filePath).toLowerCase();
				if(PIC_PATTERN.matcher(imgType).find()) {
					imgs.add(att);
				}else if(SOUND_PATTERN.matcher(imgType).find()) {
					sounds.add(att);
				}else if(VIDEO_PATTERN.matcher(imgType).find()) {
					videos.add(att);
				}
			}
			map.put("imgs" ,imgs);
			map.put("sounds" ,sounds);
			map.put("videos" ,videos);
		}
		map.put("event" ,event);
		map.put("nofull" ,nofull);
		map.put("process" ,list);
		return "/szzg/event/eventInfo.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/findGridCountByToday")
	public String findGridCountByToday(
			HttpSession session,
			@RequestParam(value = "jsonpcallback", required = false) String jsonpcallback, 
			@RequestParam(value = "infoOrgCode") String infoOrgCode) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("infoOrgCode", infoOrgCode);
		List<Map<String, Object>> gridList =statisticsService.findGridCountByToday(param);
		if(null != jsonpcallback) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(gridList) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(gridList);
		}
		
		return jsonpcallback;
	}
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	private IAttachmentService attachmentService;
	@Autowired
	private IEventDayService eventDayService;
	
	public static final Pattern PIC_PATTERN = Pattern.compile("jpg|png|bmp|gif|jpeg|webp");
	public static final Pattern VIDEO_PATTERN = Pattern.compile("mp4");
	public static final Pattern SOUND_PATTERN = Pattern.compile("amr|mp3");
	
	/**
	 * "查询 V_EVENT_DAY 盐都事件列表
	 * 
	 * add by os.wuzhj
	 * */
	@ResponseBody
	@RequestMapping(value = "/fetchEventDayJsonp")
	public String fetchEventDayJsonp(
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "orgCode",required=false) String orgCode,
			@RequestParam Map<String, Object> params) {
		
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String jsonpcallback = "";
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(StringUtils.isEmpty(orgCode)) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			orgCode = userInfo.getInfoOrgCodeStr();
		}
		
		params.put("orgCode", orgCode);
		
		EUDGPagination result = eventDayService.findEventDayPagination(page, rows, params);
		
		resultMap.put("total", result.getTotal());
		resultMap.put("list", result.getRows());
		
		if(!StringUtils.isEmpty(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}

	/**
	 * 3357982GIS上看网格员整体工作情况 网格员的事件列表  add by wuzhj
	 *
	 * param 
	 * 
	 * 		initiatorId 用户id
	 * 		eventType 事件列表类型（辖区所有，我发起的 ...） 这个需求是 我发起的  值为 my
	 * 		model 列表模式  值为 l
	 * 		createTimeStart 采集时间开始 格式为 "YYYY-MM-DD"
	 * 		createTimeEnd 采集时间结束  格式为"YYYY-MM-DD"
	 * 
	 * */
	
	@ResponseBody
	@RequestMapping(value = "/toEventList")
	public ModelAndView toEventList(
			HttpSession session,
			@RequestParam(value="initiatorId",required=false)Long initiatorId,
			@RequestParam(value="initiatorOrgId",required=false)Long initiatorOrgId,
			@RequestParam(value="infoOrgCode",required=false)String infoOrgCode,
			@RequestParam(value="gridName",required=false)String gridName,
			@RequestParam(value="eventType",required=true)String eventType,
			@RequestParam(value="model",required=true)String model,
			@RequestParam(value="createTimeStart",required=false)String createTimeStart,
			@RequestParam(value="createTimeEnd",required=false)String createTimeEnd,
			ModelMap map) {
		
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("defaultInfoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("orgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_ENCRYPT_INFO_ORG_CODE));
		map.addAttribute("gridName", defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("startGridCodeName", defaultGridInfo.get(KEY_START_GRID_CODE_NAME));
		
		if(!StringUtils.isBlank(gridName)) {
			try {
				gridName = java.net.URLDecoder.decode(gridName,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(!StringUtils.isBlank(infoOrgCode)) {
			map.put("eInfoOrgCodeForOut", Base64.encode(infoOrgCode));
		}
		
		//eventType 为my时 所取数据为我发起的列表
		//          为all时所取数据为辖区所有列表
		if("my".equals(eventType)) {
			map.put("map", true);
			map.put("initiatorId", initiatorId);
			map.put("initiatorOrgId", initiatorOrgId);
		}
		map.put("eventType", eventType);
		map.put("model", model);
		map.put("gridNameForOut", gridName);
		map.put("createTimeStart", createTimeStart);
		map.put("createTimeEnd", createTimeEnd);
		return new ModelAndView("/zzgl/event/yanduqu/list_event.ftl",map);
	}
	
	
	
	/**
	 * "查询 V_SUP_EVENT 盐城大屏任务提醒事件
	 * 
	 * add by os.wuzhj
	 * */
	@ResponseBody
	@RequestMapping(value = "/fetchEventJsonp")
	public String fetchEventJsonp(
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "orgCode",required=false) String orgCode,
			@RequestParam Map<String, Object> params) {
		
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String jsonpcallback = "";
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(StringUtils.isEmpty(orgCode)) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			orgCode = userInfo.getInfoOrgCodeStr();
		}
		
		params.put("orgCode", orgCode);
		
		EUDGPagination result = eventDayService.findEventPagination(page, rows, params);
		
		resultMap.put("total", result.getTotal());
		resultMap.put("list", result.getRows());
		
		if(!StringUtils.isEmpty(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	
	
	@RequestMapping(value = "/toSpecifiedAddPage")
	public String listEvent(HttpSession session,
			@RequestParam(value = "trigger", required = false) String trigger,
			@RequestParam(value = "eventAttrTrigger", required = false) String eventAttrTrigger,
			@RequestParam(value = "extraParams", required = false) String extraParams,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String privateFolderName = funConfigurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.PRIVATE_FOLDER_NAME4_EVENT + "List", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY),
			   forwardUrl = "/zzgl/event" + privateFolderName + "/add_event_specified.ftl";
		
		return forwardUrl;
	}
	
	
	
	
	/**
	 * 跳转简约列表页面
	 */
	@RequestMapping("/toSimpleList")
	public Object toSimpleList(HttpServletRequest request, HttpSession session, ModelMap map
			,@RequestParam Map<String,Object> params) {
		
		map.putAll(params);
		
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("defaultInfoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("gridCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("gridName", defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("treStyle","top");
		
		
		return "/zzgl/event/list_event_simple.ftl";
	}
	
	
	/**
	 * 跳转简约列表页面
	 */
	@RequestMapping("/toSimpleEventDetail")
	public Object toSimpleEventDetail(HttpServletRequest request, HttpSession session, ModelMap map
			,@RequestParam Map<String,Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAllAttributes(params);
		
		map.addAttribute("event",eventDisposal4JsonpService.searchEventByIdSimple(Long.valueOf(params.get("eventId").toString()), userInfo));
		
		return "/zzgl/event/detail_event_simple.ftl";
	}
	
	
	
	/**
	 * 跳转简约列表页面
	 */
	@RequestMapping("/getSimpleListData")
	@ResponseBody
	public EUDGPagination getSimpleListData(HttpServletRequest request, HttpSession session, ModelMap map
			,@RequestParam Map<String,Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		params.put("userId", userInfo.getUserId());
		params.put("orgId", userInfo.getOrgId());
		params.put("orgCode", userInfo.getOrgCode());
		
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		}
		
		EUDGPagination eventSimpleList = eventDisposal4JsonpService.getEventSimpleList(params, userInfo);
		
		return eventSimpleList;
	}
	
	
	
	/**
	 * 获取相似事件列表
	 */
	@RequestMapping("/getSimilarEventList")
	@ResponseBody
	public EUDGPagination getSimilarEventList(HttpServletRequest request, HttpSession session, ModelMap map
			,@RequestParam Map<String,Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		params.put("userId", userInfo.getUserId());
		params.put("orgId", userInfo.getOrgId());
		params.put("orgCode", userInfo.getOrgCode());
		
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		}
		
		EUDGPagination eventSimpleList = eventDisposal4JsonpService.getSimilarEventList(params, userInfo);
		
		return eventSimpleList;
	}
	
	
	/**
	 * 获取相似事件列表数量
	 */
	@RequestMapping("/getSimilarEventCount")
	@ResponseBody
	public Long getSimilarEventCount(HttpServletRequest request, HttpSession session, ModelMap map
			,@RequestParam Map<String,Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		params.put("userId", userInfo.getUserId());
		params.put("orgId", userInfo.getOrgId());
		params.put("orgCode", userInfo.getOrgCode());
		
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		}
		
		Long similarEventCount = eventDisposal4JsonpService.getSimilarEventCount(params, userInfo);
		
		return similarEventCount;
	}
	
}
