package cn.ffcs.zhsq.szzg.event.controller;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.dispute.service.IMediationNaService;
import cn.ffcs.zhsq.szzg.event.service.IEventAndReportJsonpService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map; 

/**
 * 	事件&入格	Jsonp接口
 * @author huangwenbin
 *	2021-1-7 16:42:17
 */

@Controller
@RequestMapping("/zhsq/eventAndReportJsonp")
public class EventAndReportJsonpController extends ZZBaseController{
	
	@Autowired
	private IEventAndReportJsonpService eventAndReportJsonpService;
	@Autowired
	private IMediationNaService mediationNaService; //注入南安矛盾纠纷模块服务
	
	@ResponseBody
	@RequestMapping(value = "/getReportFocusTypeList")
	public List<Map<String, String>>  getReportFocusTypeList(@RequestParam Map<String, Object> params) {
		return eventAndReportJsonpService.getReportFocusTypeList(params);
	}
	@ResponseBody
	@RequestMapping(value = "/initReportFocusType")
	public String  initReportFocusType(@RequestParam Map<String, Object> params) {
		eventAndReportJsonpService.initReportFocusType(params);
		return "事件工程ReportFocusType重刷成功！";
	}
	
	/**
	 *	  在办事件+入格
	 *  doing 查询 重大事件+入格
	 *  keyWord 关键字查询
	 *  infoOrgCode 网格编码
	 *  superviseMark 督办事件
	 *  timeOut 超时事件
	 *  
	 *   
	 */
	@ResponseBody
	@RequestMapping(value = {"/eventDoing4Jsonp","/eventAndReportList4Jsonp"})
	public String eventDoing4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("infoOrgCode", userInfo.getOrgCode());
		} 
		int page = 1;
		int rows = 20;
		if(CommonFunctions.isNotBlank(params, "page")) {
			page = Integer.parseInt(params.get("page").toString()) ;
		} 
		if(CommonFunctions.isNotBlank(params, "rows")) {
			 rows=Integer.parseInt(params.get("rows").toString());
		} 
		
		if(CommonFunctions.isNotBlank(params, "eventTypeList")) {
			String eventTypeList = params.get("eventTypeList").toString();
			params.put("eventTypeList", eventTypeList.split(","));
		} 
		
		EUDGPagination resultMap = eventAndReportJsonpService.findDoingEventData(params, page, rows);
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
	 *	按日期统计事件+入格 数量
	 *  infoOrgCode 网格编码
	 *  startDate	开始日期 
	 *  endDate		结束日期 
	 *  
	 */
	@ResponseBody
	@RequestMapping(value = "/eventTypeDayTotal4Jsonp")
	public String eventTypeDayTotal4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("infoOrgCode", userInfo.getOrgCode());
		} 
		Map<String, List<Map<String, Object>>> resultMap = eventAndReportJsonpService.eventTypeDayTotal(params);
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
	 *	按日期统计事件+入格  区域数量
	 *  parentGridId 网格父id
	 *  infoOrgCode 网格编码
	 *  startDate	开始日期 
	 *  endDate		结束日期 
	 *  
	 */
	@ResponseBody
	@RequestMapping(value = "/eventTypeGridTotal4Jsonp")
	public String eventTypeGridTotal4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("infoOrgCode", userInfo.getOrgCode());
		} 
		Map<String, List<Map<String, Object>>> resultMap = eventAndReportJsonpService.eventTypeGridTotal(params);
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
	 *	事件统计分析  title统计
	 *  infoOrgCode 网格编码
	 *  type_	0事件 ; 1 两违防治业务；2 房屋安全隐患；3 企业安全隐患；4 疫情防控；5 流域水质， 6 三会一课 7 扶贫走访
	 *  
	 */
	@ResponseBody
	@RequestMapping(value = "/eventAnalysisTotal4Jsonp")
	public String eventAnalysisTotal4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("infoOrgCode", userInfo.getOrgCode());
		} 
		Map<String, Object> resultMap = eventAndReportJsonpService.findEventAnalysisTotal(params);
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
	 *	事件统计分析  --下级网格
	 *  infoOrgCode 网格编码
	 *  parentGridId 网格父id
	 *  type_	0事件 ; 1 两违防治业务；2 房屋安全隐患；3 企业安全隐患；4 疫情防控；5 流域水质， 6 三会一课 7 扶贫走访
	 *  
	 */
	@ResponseBody
	@RequestMapping(value = "/eventAnalysisGrid4Jsonp")
	public String eventAnalysisGrid4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("infoOrgCode", userInfo.getOrgCode());
		} 
		Map<String, Object> resultMap = eventAndReportJsonpService.findEventAnalysisGrid(params);
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
	 *	事件统计分析  --年月
	 *  infoOrgCode 网格编码
	 *  type_	0事件 ; 1 两违防治业务；2 房屋安全隐患；3 企业安全隐患；4 疫情防控；5 流域水质， 6 三会一课 7 扶贫走访
	 *  
	 */
	@ResponseBody
	@RequestMapping(value = "/eventAnalysisDate4Jsonp")
	public String eventAnalysisDate4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("infoOrgCode", userInfo.getOrgCode());
		} 
		Map<String, Object> resultMap = eventAndReportJsonpService.findEventAnalysisDate(params);
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
	 *	南安工作台事件、入格、矛盾纠纷趋势统计分析
	 *  
	 */
	@ResponseBody
	@RequestMapping(value = "/trendData4Jsonp")
	public String trendData4Jsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String jsonpcallback = "";
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		if(CommonFunctions.isBlank(params, "userId")) {
			params.put("userId", userInfo.getUserId());
		}
		if(CommonFunctions.isBlank(params, "orgId")) {
			params.put("orgId", userInfo.getOrgId());
		}
		if(CommonFunctions.isBlank(params, "orgCode")) {
			params.put("orgCode", userInfo.getOrgCode());
		}
		
		if(CommonFunctions.isBlank(params, "trendType")) {
			params.put("trendType", "month");
		}
		
		
		//事件趋势分布
		List<Map<String, Object>> eventList = eventAndReportJsonpService.getEventTrend(params);

		if(!"default".equals(params.get("city"))){ //通用事件趋势分布（不计算矛盾纠纷与入格）
			//矛盾纠纷趋势分布
			List<Map<String, Object>> mediationNaList = mediationNaService.getDataTime(params);
			//入格趋势分布
			List<Map<String, Object>> reportFocusList = eventAndReportJsonpService.getReportFocusTrend(params);
			int total = 0;
			//循环一次就好，3个集合长度一样，且已排序
			for (int i=0,l=eventList.size(); i<l; i++) {
				total = 0;
				if(eventList.get(i) != null && eventList.get(i).get("eventTotal")!=null) {
					total += Integer.parseInt(eventList.get(i).get("eventTotal").toString());
				}
				if(mediationNaList.get(i) != null && mediationNaList.get(i).get("TOTAL")!=null) {
					total += Integer.parseInt(mediationNaList.get(i).get("TOTAL").toString());
				}
				if(reportFocusList.get(i) !=null && reportFocusList.get(i).get("eventTotal")!=null) {
					total += Integer.parseInt(reportFocusList.get(i).get("eventTotal").toString());
				}
				eventList.get(i).put("eventTotal", total);
			}
		}

		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(eventList) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(eventList);
		}
		
		return jsonpcallback;
	}
	
	
	/**
	 *	南安入格趋势统计分析
	 *  
	 */
	@ResponseBody
	@RequestMapping(value = "/trendData4ReportFocusJsonp")
	public String trendData4ReportFocusJsonp(HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String jsonpcallback = "";
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		if(CommonFunctions.isBlank(params, "userId")) {
			params.put("userId", userInfo.getUserId());
		}
		if(CommonFunctions.isBlank(params, "orgId")) {
			params.put("orgId", userInfo.getOrgId());
		}
		if(CommonFunctions.isBlank(params, "orgCode")) {
			params.put("orgCode", userInfo.getOrgCode());
		}
		
		if(CommonFunctions.isBlank(params, "trendType")) {
			params.put("trendType", "month");
		}
		
		
		List<Map<String, Object>> reportFocusList = eventAndReportJsonpService.getReportFocusTrend(params);
		
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(reportFocusList) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(reportFocusList);
		}
		
		return jsonpcallback;
	}
	
	
	/**
	 *	入格待办列表
	 *  keyWord 关键字查询
	 *  infoOrgCode 网格编码
	 *  kf.v6.aishequ.org:8057/zhsq_event/zhsq/eventAndReportJsonp/reportFocusDoingList.jhtml
	 */
	@RequestMapping(value = "/reportFocusDoingList")
	public String reportFocusDoingList(HttpSession session, @RequestParam Map<String, Object> params,
			ModelMap map) {
		map.addAllAttributes(params);
		String gridName = "";
		if(CommonFunctions.isNotBlank(params, "eRegionCode")) {
			byte[] decodedData = Base64.decode(params.get("eRegionCode").toString());
			MixedGridInfo grids = mixedGridInfoService.getDefaultGridByOrgCode(new String(decodedData));
			gridName = grids.getGridName();
		}
		
		map.addAttribute("reportTypeList", eventAndReportJsonpService.getReportFocusTypeList(null));
		map.addAttribute("gridName", gridName);
		return "/zzgl/eventAndReportFocus/list_reportFocus.ftl";
	}
	
	/**
	 * 加载上报列表记录
	 * @param session
	 * @param page	页码
	 * @param rows	每页记录数
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listReportFocusData")
	public EUDGPagination listReportFocusData(HttpSession session, 
			@RequestParam Map<String, Object> params) {
		EUDGPagination reportFocusPagination = null;
		if(CommonFunctions.isBlank(params, "eRegionCode")) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}else {
			byte[] decodedData = Base64.decode(params.get("eRegionCode").toString());
			params.put("infoOrgCode", new String(decodedData));
		}
		try {
			int page = 1;
			int rows = 20;
			if(CommonFunctions.isNotBlank(params, "page")) {
				page = Integer.parseInt(params.get("page").toString()) ;
			} 
			if(CommonFunctions.isNotBlank(params, "rows")) {
				 rows=Integer.parseInt(params.get("rows").toString());
			} 
			reportFocusPagination = eventAndReportJsonpService.getReportFocusList(params, page, rows);
		} catch (Exception e) {
			reportFocusPagination = new EUDGPagination();
			e.printStackTrace();
		}
		
		return reportFocusPagination;
	}
	
	/**
	 *	入格初始化页面
	 */
	@RequestMapping(value = "/initReportFocusPage")
	public String initReportFocusPage(HttpSession session, @RequestParam Map<String, Object> params,
			ModelMap map) {
		return "/zzgl/eventAndReportFocus/initReportFocusPage.ftl";
	}
}
