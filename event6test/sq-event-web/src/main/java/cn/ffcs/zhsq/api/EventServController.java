package cn.ffcs.zhsq.api;/**
							* Created by Administrator on 2017/8/19.
							*/

import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExpand.service.IEventLabelService;
import cn.ffcs.zhsq.intermediateData.eventSummary.service.IEventSummaryService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.szzg.event.service.IEventDisposal4JsonpService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.SpringContextUtil;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/service/event")
public class EventServController extends ApiBaseServController {

	@Autowired
	private IEventDisposalService eventDisposalService;

	@Autowired
	private IEventSummaryService eventSummaryService;
	
	@Autowired
	private IEventDisposal4JsonpService eventDisposal4JsonpService;
	
	@Autowired
	private IEvent4WorkflowService event4WorkflowService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventLabelService eventLabelService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private static JedisConnectionFactory jedisConnectionFactory;
	@Autowired
	private  IBaseDictionaryService baseDictionaryService;

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//private static String APP_KEY = "07a5069a05fe4f9329ec595cdfc25009";// 接口凭证

	// 事件总数
	@ResponseBody
	@RequestMapping(value = "/total", produces = "application/json;charset=UTF-8")
	public String total(HttpServletRequest request, HttpSession session, ModelMap map) {

		String infoOrgCode = request.getParameter("infoOrgCode");

		JSONObject resultJson = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("infoOrgCode", infoOrgCode);
		// params.put("status", infoOrgCode);
		try {
			int count = eventDisposalService.findCountByCriteria(params);
			resultJson.put("status", 1);
			resultJson.put("desc", "操作成功");
			resultJson.put("data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultJson.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/totalOfType", produces = "application/json;charset=UTF-8")
	public String totalOfType(HttpServletRequest request, HttpSession session, ModelMap map) {

		String infoOrgCode = request.getParameter("infoOrgCode");

		JSONObject resultJson = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("infoOrgCode", infoOrgCode);
		// params.put("status", infoOrgCode);
		try {
		int count = eventDisposalService.findCountByEventType(params);

		resultJson.put("status", 1);
		resultJson.put("desc", "操作成功");
		resultJson.put("data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson.toString();
	}

	/**
	 * 本年事件满意度
	 * 
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/satisfaction", produces = "application/json;charset=UTF-8")
	public String satisfaction(HttpServletRequest request, HttpSession session, ModelMap map) {

		Map<String, Object> params = new HashMap<String, Object>();
		String infoOrgCode = request.getParameter("infoOrgCode");
		params.put("infoOrgCode", infoOrgCode);

		JSONObject resultJson = new JSONObject();
		// params.put("status", infoOrgCode);
		//int count = eventDisposalService.findCountByCriteria(params);

		resultJson.put("status", 1);
		resultJson.put("desc", "操作成功");
		resultJson.put("data", "100%");
		return resultJson.toString();
	}

	/**
	 * 当前年度每月事件量
	 * 
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/totalForMonth", produces = "application/json;charset=UTF-8")
	public String total4month(HttpServletRequest request, HttpSession session, ModelMap map) {
		JSONObject resultJson = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		String year = DateUtils.getToday("YYYY");
		params.put("year", year);
		String infoOrgCode = request.getParameter("infoOrgCode");
		params.put("infoOrgCode", infoOrgCode);
		try {
		Map<String, Object> event4Month = eventDisposalService.findEvent4Year(params);

		resultJson.put("status", 1);
		resultJson.put("desc", "操作成功");
		resultJson.put("data", event4Month);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/gridOfMonth", produces = "application/json;charset=UTF-8")
	public String gridOfMonth(HttpServletRequest request, HttpSession session, ModelMap map) {
		JSONObject resultJson = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		String month = DateUtils.getToday("YYYY-MM");
		params.put("month", month);
		String infoOrgCode = request.getParameter("infoOrgCode");
		params.put("infoOrgCode", infoOrgCode);
		try {
		List<Map<String, Object>> result = eventDisposalService.findGrid4Month(params);
		resultJson.put("status", 1);
		resultJson.put("desc", "操作成功");
		resultJson.put("data", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/top10OfMonth", produces = "application/json;charset=UTF-8")
	public String top10OfMonth(HttpServletRequest request, HttpSession session, ModelMap map) {
		JSONObject resultJson = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		String month = DateUtils.getToday("YYYY-MM");
		params.put("month", month);
		String infoOrgCode = request.getParameter("infoOrgCode");
		params.put("infoOrgCode", infoOrgCode);
		try {
		List<Map<String, Object>> result = eventDisposalService.findTop10OfEventType4Month(params);
		resultJson.put("status", 1);
		resultJson.put("desc", "操作成功");
		resultJson.put("data", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson.toString();
	}

	/**
	 * 保存 南昌综治推送归档事件 至大数据平台事件汇总 来源平台 01:大数据平台,02:综治平台,03:城管,04:12345
	 * 
	 * @param id
	 *            sourcePlatform
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveEventSummary", produces = "application/json;charset=UTF-8")
	public boolean saveEventSummary(HttpServletRequest request, HttpSession session, ModelMap map) {
		boolean flag = false;

		String id = request.getParameter("id");
		String sourcePlatform = request.getParameter("sourcePlatform");
		if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(sourcePlatform)) {

			Map<String, Object> eventSummary = new HashMap<String, Object>();
			eventSummary.put("id", id);
			eventSummary.put("sourcePlatform", sourcePlatform);
			if (eventSummaryService.findEventSummaryById(eventSummary) > 0) {
				return true;
			} else {
				try {
					Date date = null;
					if (StringUtils.isNotBlank(request.getParameter("createTime"))) {
						date = df.parse(request.getParameter("createTime"));
					}
					eventSummary.put("name", request.getParameter("name"));
					eventSummary.put("content", request.getParameter("content"));
					eventSummary.put("regionCode", request.getParameter("regionCode"));
					eventSummary.put("createTime", date);
					eventSummary.put("happendAddr", request.getParameter("happendAddr"));
					eventSummary.put("x", request.getParameter("x"));
					eventSummary.put("y", request.getParameter("y"));
					if (eventSummaryService.saveEventSummary(eventSummary) > 0) {
						flag = true;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
		}
		return flag;
	}

	/**
	 * 删除 南昌综治推送归档事件 至大数据平台事件汇总 来源平台 01:大数据平台,02:综治平台,03:城管,04:12345
	 * 
	 * @param id
	 *            sourcePlatform
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delEventSummary", produces = "application/json;charset=UTF-8")
	public boolean delEventSummary(HttpServletRequest request, HttpSession session, ModelMap map) {
		boolean flag = false;
		String id = request.getParameter("id");
		String sourcePlatform = request.getParameter("sourcePlatform");
		if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(sourcePlatform)) {
			Map<String, Object> eventSummary = new HashMap<String, Object>();
			eventSummary.put("id", id);
			eventSummary.put("sourcePlatform", sourcePlatform);
			try {
			if (eventSummaryService.deleteEventSummaryById(eventSummary)) {
				flag = true;
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	@ResponseBody
    @RequestMapping(value="/searchEventProcess")
    public String searchEventProcess(HttpServletRequest request, HttpSession session, ModelMap map,
    		@RequestParam Map<String, Object> params) throws JSONException, IOException {
		JSONObject resultJson = new JSONObject();
		String msg="";
		Integer status=0;
		List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
		//校验查询用户
		if(CommonFunctions.isNotBlank(params, "account")&&CommonFunctions.isNotBlank(params, "password")) {
			if(!params.get("account").toString().equals("ffcs")
					||!params.get("password").toString().equals("ffcs1234")) {
				msg="查询失败，请用户名或密码错误。";
			}
		}else {
			msg="查询失败，请传入有效的账号密码。";
		}
		
		if(StringUtils.isBlank(msg)) {//为空则表明查询用户校验成功，进行流程查询
			
			try {
				UserInfo userInfo = new UserInfo();
				if(CommonFunctions.isNotBlank(params, "userId")) {
					userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
				}
				if(CommonFunctions.isNotBlank(params, "orgId")) {
					userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
				}
				if(CommonFunctions.isNotBlank(params, "orgCode")) {
					userInfo.setOrgCode(params.get("orgCode").toString());
				}
				Long instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(Long.valueOf(params.get("eventId").toString()));
				data =  eventDisposalWorkflowService.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
			} catch (Exception e) {
				msg="查询失败，"+"查询异常";
				e.printStackTrace();
			}
		}
		
		if(StringUtils.isBlank(msg)) {
			status=1;
			msg="查询成功";
		}

		resultJson.put("status", status);
		resultJson.put("msg", msg);
		resultJson.put("data", data);

        return resultJson.toString();
    }
	

	/**
	 *   地图事件列表查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMapEventList")
	public String getMapEventList(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		
		UserInfo userInfo = new UserInfo();
		if(CommonFunctions.isNotBlank(params, "userId")) {
			userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userInfo.setOrgCode(params.get("orgCode").toString());
		}
		try {
		resultMap.put("result", eventDisposal4JsonpService.getMapEventList(params, userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	
	/**
	 *   地图事件总数查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMapEventListCount")
	public String getMapEventListCount(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		
		UserInfo userInfo = new UserInfo();
		if(CommonFunctions.isNotBlank(params, "userId")) {
			userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userInfo.setOrgCode(params.get("orgCode").toString());
		}
		try {
		resultMap.put("result", eventDisposal4JsonpService.getMapEventListCount(params, userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	
	/**
	 *   地图部件列表查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMapComponentList")
	public String getMapComponentList(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		
		UserInfo userInfo = new UserInfo();
		if(CommonFunctions.isNotBlank(params, "userId")) {
			userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userInfo.setOrgCode(params.get("orgCode").toString());
		}
		try {
		resultMap.put("result", eventDisposal4JsonpService.getMapComponentList(params, userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	/**
	 *   地图部件总数查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMapComponentListCount")
	public String getMapComponentListCount(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		
		UserInfo userInfo = new UserInfo();
		if(CommonFunctions.isNotBlank(params, "userId")) {
			userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userInfo.setOrgCode(params.get("orgCode").toString());
		}
		try {
		resultMap.put("result", eventDisposal4JsonpService.getMapComponentListCount(params, userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	
	/**
	 * 获取事件记录总数
	 * @param session
	 * @param params
	 * 			listType		事件列表类型
	 * 			orgCode			组织编码
	 * 			infoOrgCode		地域编码
	 * 			evaLevelList	评价等级 01 不满意；02 满意；03 非常满意；04 基本满意
	 * 			handleDateFlag	时限状态 1 正常；2 将到期；3 已过期
	 * 			trigger			事件类别过滤触发条件，功能配置编码为：types
	 * 			
	 * 			jsonpcallback	jsonp回调
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchDataCount4Jsonp")
	public String fetchDataCount4Jsonp(
			HttpSession session,
			@RequestParam Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String jsonpcallback = "";
		int total = 0;
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		try {
			total = event4WorkflowService.findEventCount(params);
		} catch (Exception e) {
			resultMap.put("msgWrong", "查询异常");
			e.printStackTrace();
		}
		
		resultMap.put("total", total);
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	
	
	/**
	 * 获取辖区所有事件列表数据
	 * @param session
	 * @param page		页数
	 * @param rows		每页记录数
	 * @param params
	 * 			orgCode			组织域编码
	 * 			infoOrgCode		地域编码
	 * 			evaLevelList	评价等级 01 不满意；02 满意；03 非常满意；04 基本满意
	 * 			handleDateFlag	时限状态 1 正常；2 将到期；3 已过期
	 * 			jsonpcallback	jsop回调
	 * 			eventType		列表类别 all 辖区所有；todo 待办列表；默认为all
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchEventData4Jsonp")
	public String fetchEventData4Jsonp(
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String jsonpcallback = "";
		EUDGPagination result = new EUDGPagination();
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		try {
			result = event4WorkflowService.findEventListPagination(page, rows, params);
		} catch (Exception e) {
			resultMap.put("msgWrong", "查询异常");
			e.printStackTrace();
		}
		
		resultMap.put("total", result.getTotal());
		resultMap.put("list", result.getRows());
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	
	
	
	/********************************数据清洗相关jsonp接口整合start*******************************/
	/**
	 * 事件总量相关数据清洗查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventTotalByStatistics")
	public String getEventTotalByStatistics(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		UserInfo userInfo = new UserInfo();
		if(CommonFunctions.isNotBlank(params, "userId")) {
			userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userInfo.setOrgCode(params.get("orgCode").toString());
		}
		try {
		resultMap.put("result", eventDisposal4JsonpService.getEventTotalByStatistics(params, userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	/**
	 * 事件区域分布清洗表查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventAreaDistributionByStatistics")
	public String getEventAreaDistributionByStatistics(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		UserInfo userInfo = new UserInfo();
		if(CommonFunctions.isNotBlank(params, "userId")) {
			userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userInfo.setOrgCode(params.get("orgCode").toString());
		}
	try {
		resultMap.put("result", eventDisposal4JsonpService.getEventAreaDistributionByStatistics(params, userInfo));
	} catch (Exception e) {
		e.printStackTrace();
	}
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	/********************************数据清洗相关jsonp接口整合end*******************************/
	
	/**
	 * 事件区域分布实时查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventAreaDistributionByActual")
	public String getEventAreaDistributionByActual(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		UserInfo userInfo = new UserInfo();
		if(CommonFunctions.isNotBlank(params, "userId")) {
			userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userInfo.setOrgCode(params.get("orgCode").toString());
		}
	try {
		resultMap.put("result", eventDisposal4JsonpService.getEventAreaDistributionByActual(params, userInfo));
	} catch (Exception e) {
		e.printStackTrace();
	}
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	/**
	 * 事件类型占比实时查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventTypeProportionByActual")
	public String getEventTypeProportionByActual(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("total", 0);
		resultMap.put("data", new ArrayList<Map<String,Object>>());
		UserInfo userInfo = new UserInfo();
		if(CommonFunctions.isNotBlank(params, "userId")) {
			userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userInfo.setOrgCode(params.get("orgCode").toString());
		}
		
		if(CommonFunctions.isNotBlank(params, "typeLevel")) {
			String str = params.get("typeLevel").toString();
			if(str.equals("2")) {
				params.put("typeBit", 4);
			}else if(str.equals("3")) {
				params.put("typeBit", 6);
			}
		}else {
			params.put("typeBit", 2);
		}
		
		//获取事件总数
		try {
			params.put("listType", "5");
			resultMap.put("total", event4WorkflowService.findEventCount(params));
			resultMap.put("data", eventDisposal4JsonpService.getEventTypeProportionByActual(params, userInfo));
		} catch (ZhsqEventException e) {
			e.printStackTrace();
		}
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	
	/**
	 * 事件类型占比实时查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventTypeProportionByStatistics")
	public String getEventTypeProportionByStatistics(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("total", 0);
		resultMap.put("data", new ArrayList<Map<String,Object>>());
		UserInfo userInfo = new UserInfo();
		if(CommonFunctions.isNotBlank(params, "userId")) {
			userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userInfo.setOrgCode(params.get("orgCode").toString());
		}
		
		if(CommonFunctions.isNotBlank(params, "typeLevel")) {
			String str = params.get("typeLevel").toString();
			if(str.equals("2")) {
				params.put("typeBit", 4);
			}else if(str.equals("3")) {
				params.put("typeBit", 6);
			}
		}else {
			params.put("typeBit", 2);
		}
		
		//获取事件总数
		try {
			params.put("listType", "5");
			resultMap.put("total", eventDisposal4JsonpService.getEventTotalByStatistics(params, userInfo));
			resultMap.put("data", eventDisposal4JsonpService.getEventTypeProportionByStatistics(params, userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	/**
	 * 事件趋势实时查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventTrendByActual")
	public String getEventTrendByActual(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = new UserInfo();
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", new ArrayList<Map<String,Object>>());
		if(CommonFunctions.isNotBlank(params, "userId")) {
			userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userInfo.setOrgCode(params.get("orgCode").toString());
		}
		
		if(CommonFunctions.isBlank(params, "trendType")) {
			params.put("trendType", "month");
		}
		
		//获取事件总数
		try {
			resultMap.put("data", eventDisposal4JsonpService.getEventTrendByActual(params, userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	/**
	 * 事件趋势清洗查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventTrendByStatistics")
	public String getEventTrendByStatistics(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", new ArrayList<Map<String,Object>>());
		UserInfo userInfo = new UserInfo();
		if(CommonFunctions.isNotBlank(params, "userId")) {
			userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userInfo.setOrgCode(params.get("orgCode").toString());
		}
		
		if(CommonFunctions.isBlank(params, "trendType")) {
			params.put("trendType", "month");
		}
		
		//获取事件总数
		try {
			resultMap.put("data", eventDisposal4JsonpService.getEventTrendByStatistics(params, userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	
	/**
	 * 事件特定类型实时查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSpecialEventTypeProportionByActual")
	public String getSpecialEventTypeProportionByActual(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", new ArrayList<Map<String,Object>>());
		UserInfo userInfo = new UserInfo();
		if(CommonFunctions.isNotBlank(params, "userId")) {
			userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userInfo.setOrgCode(params.get("orgCode").toString());
		}
		
		//获取事件总数
		try {
			resultMap.put("data", eventDisposal4JsonpService.getSpecialEventTypeProportionByActual(params, userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	
	/**
	 * 事件地域，特定类型实时查询jsonp接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSpecialEventTypeAreaByActual")
	public String getSpecialEventTypeAreaByActual(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", new ArrayList<Map<String,Object>>());
		UserInfo userInfo = new UserInfo();
		if(CommonFunctions.isNotBlank(params, "userId")) {
			userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
		}
		
		if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userInfo.setOrgCode(params.get("orgCode").toString());
		}
		
		//获取事件总数
		try {
			resultMap.put("data", eventDisposal4JsonpService.getSpecialEventTypeAreaByActual(params, userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}
	
	
	
	// 跳转到不文明现象页面
	@RequestMapping(value = "/toUncivilizedPage")
	public String toEventVerifyInfo(HttpSession session,
			@RequestParam Map<String, Object> params,
			ModelMap map) throws Exception {
		try {
			Map<String,Object> searchLabelParams=new HashMap<String,Object>();
			searchLabelParams.put("labelModel", "002");
			List<Map<String, Object>> eventLabelList = eventLabelService.searchListByParam(searchLabelParams);
			map.addAttribute("eventLabelDict", eventLabelList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//获取近30天
		Date today = new Date();
        String endDate = DateUtils.convertDateToString(today); //当前日期
        //获取三十天前日期
        Calendar theCa = Calendar. getInstance ();
        theCa .setTime( today );
        theCa .add( theCa . DATE , -30); //最后一个数字30可改，30天的意思
        Date start = theCa .getTime();
        String startDate =  DateUtils.convertDateToString(start); //三十天之前日期
        map.put("createTimeStart", startDate);
        map.put("createTimeEnd", endDate);
        map.put("infoOrgCode", params.get("infoOrgCode").toString());
        String uiDomain=funConfigurationService.getAppDomain("$UI_OUT_DOMAIN");
        String IMG_URL=funConfigurationService.getAppDomain("$IMG_OUT_DOMAIN");

        map.put("uiDomain", uiDomain);
        map.put("IMG_URL", IMG_URL);
		
		
		return "/map/arcgis/arcgis_3d/uncivilized_page_common.ftl";	
	}
	
	
	@RequestMapping(value = "/toImgGalleryPage")
	public Object toImgPage(HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> params, ModelMap map){
		map.putAll(params);
		String uiDomain=funConfigurationService.getAppDomain("$UI_OUT_DOMAIN");
        String IMG_URL=funConfigurationService.getAppDomain("$IMG_OUT_DOMAIN");

		map.put("uiDomain", uiDomain);
        map.put("IMG_URL", IMG_URL);
		return "/map/arcgis/arcgis_3d/show_img_page.ftl";
	}
	/**
	 *明溪智慧城管APP获取类型前十
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventTypeTopTen")
	public String getEventTypeTop10(HttpSession session,
											@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", new ArrayList<Map<String,Object>>());
		UserInfo userInfo = new UserInfo();
		if(CommonFunctions.isNotBlank(params, "userId")) {
			userInfo.setUserId(Long.valueOf(params.get("userId").toString()));
		}

		if(CommonFunctions.isNotBlank(params, "orgId")) {
			userInfo.setOrgId(Long.valueOf(params.get("orgId").toString()));
		}

		if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userInfo.setOrgCode(params.get("orgCode").toString());
		}

		if(CommonFunctions.isBlank(params, "trendType")) {
			params.put("trendType", "month");
		}

		//获取事件总数
		try {
			jedisConnectionFactory = (JedisConnectionFactory) SpringContextUtil.getBean("jedisConnectionFactory");
			Jedis jedis = jedisConnectionFactory.getShardInfo().createResource();
			jedis.select(5);
			String typeTopTen = "";
			if(CommonFunctions.isBlank(params, "orgCode")||StringUtils.isBlank(params.get("orgCode").toString())) {
				resultMap.put("data","");
			}else {
				typeTopTen = jedis.hget("topTen_"+params.get("orgCode"),"5");
				String[] typeTopTenArray = typeTopTen.split(",");
				List<Map<String,Object>> typeTopTenList = new ArrayList<>();
				for (int i = 0,j = typeTopTenArray.length; i < j; i++) {
					Map<String,Object> typeMap = new HashMap<>();
					typeMap.put("type",typeTopTenArray[i]);
					typeTopTenList.add(typeMap);
				}
				Map<String, Object> dictMap = new HashMap<String, Object>();
				dictMap.put("orgCode", params.get("orgCode"));
				dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
				List<BaseDataDict> eventTypeDict = baseDictionaryService.findDataDictListByCodes(dictMap);
				// 设置事件分类全路径
				for (int i = 0,j = typeTopTenList.size(); i < j ; i++) {
					Map<String, Object> eventMap = typeTopTenList.get(i);
					if(CommonFunctions.isNotBlank(eventMap, "type") && eventTypeDict != null) {
						Map<String, Object> eventTypeMap = DataDictHelper.capMultilevelDictInfo(eventMap.get("type").toString(), ConstantValue.BIG_TYPE_PCODE, eventTypeDict);
						if(CommonFunctions.isNotBlank(eventTypeMap, "dictName")) {
							eventMap.put("typeName", eventTypeMap.get("dictName"));
						}
						if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
							eventMap.put("eventClass", eventTypeMap.get("dictFullPath"));
						}
					}
				}
				resultMap.put("data",typeTopTenList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}

}
