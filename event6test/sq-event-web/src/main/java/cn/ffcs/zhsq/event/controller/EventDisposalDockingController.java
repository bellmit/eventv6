package cn.ffcs.zhsq.event.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.JsonUtils;

/**
 * 事件对接controller
 * @author zhangls
 *
 */
@Controller
@RequestMapping("/zhsq/event/eventDisposalDocking")
public class EventDisposalDockingController extends ZZBaseController {
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventDisposalDockingService eventDisposalDockingService;
	
	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;
	
	/**
	 * 同域事件上报接口
	 * @param session
	 * @param eventJson
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toReportEventByJson")
	public Map<String, Object> toReportEventByJson(HttpSession session, 
			@RequestParam(value = "eventJson", required = false) String eventJson,
			ModelMap map){
		
		return eventDisposalDockingService.saveEventDisposalAndReport(eventJson);
	}
	
	/**
	 * 跨域事件上报接口连接中jsonpCallBack必须设置，必须使用jsonp格式
	 * @param session
	 * @param request
	 * @param eventJson
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toReportEventByJsonp")
	public String toReportEventByJsonp(HttpSession session, 
			HttpServletRequest request,
			@RequestParam(value = "eventJson", required = false) String eventJson,
			ModelMap map){
		Map<String, Object> resultMap = this.toReportEventByJson(session, eventJson, map);
		String restultMapJsonStr = JsonUtils.mapToJson(resultMap);
		String jsonpCallBack = request.getParameter("jsonpCallBack");//jsonp回调方法
		//String jsonpCallBack = eventMap.get("jsonpCallBack").toString();//使用Map传递此参数时，回调后，页面不可读取
		
		if(StringUtils.isNotBlank(jsonpCallBack)){
			restultMapJsonStr = jsonpCallBack + "(" + restultMapJsonStr + ")";
		}
			
		return restultMapJsonStr;
	}
	
	@ResponseBody
	@RequestMapping(value = "/toShuntEventByJsonp")
	public String toShuntEventByJsonp(HttpSession session, 
			HttpServletRequest request,
			@RequestParam(value = "eventJson") String eventJson,
			ModelMap map){
		Map<String, Object> resultMap = eventDisposalDockingService.saveEventDisposalAndShunt(eventJson);
		String restultMapJsonStr = JsonUtils.mapToJson(resultMap);
		String jsonpCallBack = request.getParameter("jsonpCallBack");//jsonp回调方法
		
		if(StringUtils.isNotBlank(jsonpCallBack)){
			restultMapJsonStr = jsonpCallBack + "(" + restultMapJsonStr + ")";
		}
		
		return restultMapJsonStr;
	}
	
	/**
	 * 采集并结案接口
	 * @param session
	 * @param eventJson
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toCloseEventByJsonp")
	public String toCloseEventByJsonp(HttpSession session, 
			HttpServletRequest request,
			@RequestParam(value = "eventJson") String eventJson,
			ModelMap map){
		Map<String, Object> resultMap = eventDisposalDockingService.saveEventDisposalAndClose(eventJson);
		String restultMapJsonStr = JsonUtils.mapToJson(resultMap);
		String jsonpCallBack = request.getParameter("jsonpCallBack");//jsonp回调方法
		
		if(StringUtils.isNotBlank(jsonpCallBack)){
			restultMapJsonStr = jsonpCallBack + "(" + restultMapJsonStr + ")";
		}
		
		return restultMapJsonStr;
	}
	
	@ResponseBody
	@RequestMapping(value = "/toEvaluateEventByJsonp")
	public String toEvaluateEventByJsonp(HttpSession session, 
			HttpServletRequest request,
			@RequestParam(value = "eventJson") String eventJson,
			ModelMap map){
		Map<String, Object> resultMap = eventDisposalDockingService.saveEventDisposalAndEvaluate(eventJson);
		
		String restultMapJsonStr = JsonUtils.mapToJson(resultMap);
		String jsonpCallBack = request.getParameter("jsonpCallBack");//jsonp回调方法
		
		if(StringUtils.isNotBlank(jsonpCallBack)){
			restultMapJsonStr = jsonpCallBack + "(" + restultMapJsonStr + ")";
		}
		
		return restultMapJsonStr;
	}
	
	/**
	 * 事件驳回操作
	 * @param session
	 * @param request
	 * @param eventJson
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toRejectEventByJsonp")
	public String toRejectEventByJsonp(HttpSession session, 
			HttpServletRequest request,
			@RequestParam(value = "eventJson") String eventJson,
			ModelMap map){
		Map<String, Object> resultMap = eventDisposalDockingService.rejectWorkFlow(eventJson);
		String restultMapJsonStr = JsonUtils.mapToJson(resultMap);
		String jsonpCallBack = request.getParameter("jsonpCallBack");//jsonp回调方法
		
		if(StringUtils.isNotBlank(jsonpCallBack)){
			restultMapJsonStr = jsonpCallBack + "(" + restultMapJsonStr + ")";
		}
		
		return restultMapJsonStr;
	}
	
	/**
	 * 获取结案办理结果
	 * @param session
	 * @param request
	 * @param eventSrc
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toGetClosedResultByJsonp")
	public String toGetClosedResultByJsonp(HttpSession session, 
			HttpServletRequest request,
			@RequestParam(value = "bizPlatform") String bizPlatform,
			ModelMap modelMap){
		List<String> statusList=new ArrayList<String>();
		statusList.add(ConstantValue.EVENT_STATUS_ARCHIVE );//结案
		statusList.add(ConstantValue.EVENT_STATUS_END);//归档
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eventSrc", bizPlatform);
		params.put("statusList", statusList);
		
		List<Map<String, Object>> resultMapList = eventDisposalDockingService.findClosedEventWithEventInter(params);
		String restultMapJsonStr = JsonUtils.listToJson(resultMapList);
		String jsonpCallBack = request.getParameter("jsonpCallBack");//jsonp回调方法
		
		if(StringUtils.isNotBlank(jsonpCallBack)){
			restultMapJsonStr = jsonpCallBack + "(" + restultMapJsonStr + ")";
		}
		
		return restultMapJsonStr;
	}
	
	/**
	 * 更新交互表
	 * @param session
	 * @param request
	 * @param eventJson
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toFeedBackEventInterByJsonp")
	public String toFeedBackEventInterByJsonp(HttpSession session, 
			HttpServletRequest request,
			@RequestParam(value = "eventJson") String eventJson,
			ModelMap modelMap){
		Map<String, Object> resultMap = eventDisposalDockingService.feedBackEventInter(eventJson);
		String restultMapJsonStr = JsonUtils.mapToJson(resultMap);
		String jsonpCallBack = request.getParameter("jsonpCallBack");//jsonp回调方法
		
		if(StringUtils.isNotBlank(jsonpCallBack)){
			restultMapJsonStr = jsonpCallBack + "(" + restultMapJsonStr + ")";
		}
		
		return restultMapJsonStr;
	}
	
	/**
	 * 获取上报到迪爱斯的事件
	 * @param session
	 * @param request
	 * @param eventSrc
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toGetReportEventByJsonp")
	public String toGetReportEventByJsonp(HttpSession session, 
			HttpServletRequest request,
			@RequestParam(value = "bizPlatform") String bizPlatform,
			ModelMap modelMap){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eventSrc", bizPlatform);
		List<Map<String, Object>> resultMapList = eventDisposalDockingService.findReportEventWithEventInter(params);
		String restultMapJsonStr = JsonUtils.listToJson(resultMapList);
		String jsonpCallBack = request.getParameter("jsonpCallBack");//jsonp回调方法
		
		if(StringUtils.isNotBlank(jsonpCallBack)){
			restultMapJsonStr = jsonpCallBack + "(" + restultMapJsonStr + ")";
		}
		
		return restultMapJsonStr;
	}
	
	/**
	 * 迪爱斯事件上报
	 * @param session
	 * @param request
	 * @param eventId
	 * @param destPlatform 事件来源     与 bizPlatform对应      004为迪爱斯对接事件
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toAddEventInter")
	public Map<String, Object> toAddEventInter(HttpSession session, 
			HttpServletRequest request,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "destPlatform") String destPlatform,
			ModelMap modelMap){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
		
		Long interId = -1L;
		
		if(event != null){
			String eventStatus = event.getStatus();
			
			if(ConstantValue.EVENT_STATUS_RECEIVED.equals(eventStatus) || ConstantValue.EVENT_STATUS_DISTRIBUTE.equals(eventStatus) || ConstantValue.EVENT_STATUS_REPORT.equals(eventStatus)){//00 01 02事件可以上报
				DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
				dataExchangeStatus.setDestPlatform(destPlatform);
				dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
				dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
				dataExchangeStatus.setOwnSideBizCode(String.valueOf(eventId));
				dataExchangeStatus.setCreateUserId(userInfo.getUserId());
				
				interId = dataExchangeStatusService.saveDataExchangeStatus(dataExchangeStatus);
			}
		}
		
		resultMap.put("result", interId);
		return resultMap;
	}
}
