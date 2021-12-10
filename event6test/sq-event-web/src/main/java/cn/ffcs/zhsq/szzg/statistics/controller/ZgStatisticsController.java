package cn.ffcs.zhsq.szzg.statistics.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.gmis.attractInvestment.service.IAttractInvestmentService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.szzg.statistics.ZgStatistics;
import cn.ffcs.zhsq.szzg.statistics.service.IZgStatisticsService;
import cn.ffcs.zhsq.szzg.statspop.service.IZgStatsPopService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**
 * 城市经济运行
 * @author huangwenbin
 *
 */
@Controller
@RequestMapping("/zhsq/szzg/zgStatisticsController")
public class ZgStatisticsController extends ZZBaseController{
	@Autowired
	private IZgStatisticsService statisticsService;
	@Autowired
	private IZgStatsPopService statsPopService;
	@Autowired
	private IAttractInvestmentService attractInvestmentService; 
	
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request,HttpSession session, ModelMap map) {
		
		//获取默认网格信息
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("gridId",defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("gridCode",defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("gridName",defaultGridInfo.get(KEY_START_GRID_NAME));
		
		String stype = request.getParameter("stype");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("dictGeneralCode", stype);
		param.put("dictLevel", 3);
		param = statsPopService.findDataDict(param);
		map.addAttribute("dictName", param!=null?param.get("DICT_NAME").toString():"");
		param.put("stype", stype+"_title");
		map.addAttribute("title", statisticsService.findTitle(param));
		map.addAttribute("stype", stype );
		Calendar c = Calendar.getInstance();
		map.addAttribute("currentYear", c.get(Calendar.YEAR));
		map.addAttribute("currentMonth", c.get(Calendar.MONTH)+1);
		
	
		//switch(stype){
		//case "s9": return "/szzg/statistics/list_statistics_year.ftl";
      // }
		
		
		
		return "/szzg/statistics/list_statistics.ftl";
	}	
	
	
	
	
	
	
	
	@RequestMapping(value = "/toAdd")
	public String toAdd(HttpSession session,ModelMap map,
			@RequestParam(value = "stype") String stype,	
			@RequestParam(value = "operation") String operation,	
			@RequestParam(value = "smonth",required=false) String smonth,
			@RequestParam(value = "syear",required=false) String syear) {
		Map<String,Object> param = new HashMap<String, Object>();
		//获取默认网格信息
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("gridId",defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("gridCode",defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("gridName",defaultGridInfo.get(KEY_START_GRID_NAME));
		param.put("stype", stype+"_title");
		map.addAttribute("title", statisticsService.findTitle(param));
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("userId",userInfo.getUserId());
		if(syear == null){
			Calendar c = Calendar.getInstance();
			syear = c.get(Calendar.YEAR)+"";
			smonth =(c.get(Calendar.MONTH)+1)+"";
		}
		map.addAttribute("syear", syear);
		map.addAttribute("smonth", smonth);
		map.addAttribute("operation", operation);
		map.addAttribute("stype", stype);
		//switch(stype){
		//case "s9": return "/szzg/statistics/add_statistics_year.ftl";
       //}
		return "/szzg/statistics/add_statistics.ftl";
	}
	@RequestMapping(value = "/detail")
	public String detail(HttpSession session,
			@RequestParam(value = "stype") String stype,	
			@RequestParam(value = "smonth") String smonth,
			@RequestParam(value = "syear") String syear,
			ModelMap map) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("stype", stype+"_title");
		map.addAttribute("title", statisticsService.findTitle(param));
		map.addAttribute("syear", syear);
		map.addAttribute("smonth", smonth);
		map.addAttribute("stype", stype);
		return "/szzg/statistics/detail_statistics.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/listData")
	public Map<String,Object> listData(HttpSession session,	
			@RequestParam(value = "smonth") String smonth,
			@RequestParam(value = "syear") String syear,
			@RequestParam(value = "stype") String stype,
			@RequestParam(value = "join",required=false) String join) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("syear", syear);
		param.put("stype", stype);
		param.put("smonth", smonth);
		param.put("join", join==null?"left":join);
		List<ZgStatistics> list = statisticsService.findByParam(param);
		param.put("rows",list);
		param.put("total", list.size());
		return param;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/delete")
	public ResultObj del(HttpSession session, ModelMap map,	
			@RequestParam(value = "smonth") String smonth,
			@RequestParam(value = "syear") String syear,
			@RequestParam(value = "stype") String stype) {
		Map<String,Object> param = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		param.put("updateId", userInfo.getUserId());
		param.put("syear", syear);
		param.put("stype", stype);
		param.put("smonth", smonth);
		return Msg.DELETE.getResult(statisticsService.deleteByParam(param));
	}
	
	
	/**
	 * 批量 更新或保存
	 * @param session
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdateByList")
	public ResultObj saveOrUpdateByList(HttpSession session, ModelMap map,@RequestBody ZgStatistics[] arr)  {
		Msg msg = Msg.OPERATE;
		if(arr == null || arr.length == 0){
			return msg.getResult(false, "请求无数据!");
		}
		
		List<ZgStatistics> list = Arrays.asList(arr);
		int num = 0;
		if(list.get(0).getSeqId() == null){//新增
			msg = Msg.ADD;
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("smonth", list.get(0).getSmonth());
			param.put("syear", list.get(0).getSyear());
			param.put("stype", list.get(0).getStype());
			int count  = statisticsService.findCount(param);
			if(count > 0){
				return msg.getResult(false, param.get("syear").toString()+"年 "
			+param.get("smonth").toString() +"　已经存在记录");
			}
			num = statisticsService.insertByList(list);
		}else{//修改
			msg = Msg.EDIT;
			num = statisticsService.updateByList(list);
		}
		return msg.getResult(num);
	}
	
	@ResponseBody
	@RequestMapping(value = "/findCount")
	public long findCount(HttpSession session, ModelMap map,	
			@RequestParam(value = "smonth") String smonth,
			@RequestParam(value = "syear") String syear,
			@RequestParam(value = "stype") String stype) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("syear", syear);
		param.put("stype", stype);
		param.put("smonth", smonth);
		return statisticsService.findCount(param);
	}
	
	/**
	 * 首页展示跳转
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/showIndex")
	public String showIndex(HttpServletRequest request,HttpSession session, ModelMap map) {
		map.addAttribute("currentDate", Calendar.getInstance().get(Calendar.YEAR)+"-"+(Calendar.getInstance().get(Calendar.MONTH)+1));
		return "/szzg/statistics/index_statistics.ftl";
	}
	
	
	
	/**
	 * 首页展示跳转
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/showIndex_year")
	public String showIndex2(HttpServletRequest request,HttpSession session, ModelMap map) {
		map.addAttribute("currentDate", Calendar.getInstance().get(Calendar.YEAR));
		return "/szzg/statistics/index_statistics_year.ftl";
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/findTreeTable")
	public Map<String,Object> findTreeTable(HttpSession session, ModelMap map,	
			@RequestParam(value = "smonth",required=false) String smonth,
			@RequestParam(value = "syear") String syear,
			@RequestParam(value = "level") String level,
			@RequestParam(value = "stype") String stype) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("syear", syear);
		param.put("stype", stype);
		param.put("smonth", smonth);
		param.put("level", level);
		param.put("list", statisticsService.findTreeTable(param));
		param.put("monthlist", statisticsService.findChart12Month(param));
		param.put("stype", stype+"_title");
		param.put("title", statisticsService.findTitle(param));
		return param ;
	}
	
	/**
	 * 静态展示跳转
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/showJT")
	public String showJT(HttpServletRequest request,HttpSession session, ModelMap map) {
		map.addAttribute("orgCode", request.getParameter("orgCode"));
		return "/szzg/statistics/"+request.getParameter("page")+".ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/findGDPYearAndMonth")
	public Map<String,Object> findGDPYearAndMonth(HttpSession session, ModelMap map,	
			@RequestParam(value = "table") String table,
			@RequestParam(value = "type",required=false) String type,
			@RequestParam(value = "month") String month,
			@RequestParam(value = "year") String year) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("year", year);
		param.put("month", month);
		param.put("type", type);
		param.put("table", "zg_ecno_"+table);
		param.put("list", statisticsService.findGDPYearAndMonth(param));
		return param ;
	}
	@ResponseBody
	@RequestMapping(value = "/findStatisticsDate")
	public Map<String,Object> findStatisticsDate(HttpSession session, ModelMap map,
			@RequestParam(value = "table") String table,
			@RequestParam(value = "type",required=false) String type) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("table", "zg_ecno_"+table);
		param.put("type", type);
		param.put("list", statisticsService.findStatisticsDate(param));
		return param ;
	}
	@ResponseBody
	@RequestMapping(value = "/findGeneralDate")
	public Map<String,Object> findGeneralDate(HttpSession session, ModelMap map,
			@RequestParam(value = "type",required=false) String type,
			@RequestParam(value = "year",required=false) String year) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("type", type);
		param.put("year", year);
		if(year == null ){
			param.put("list", statisticsService.findGeneralDate(param));//有多少年数据
		}else{
			param.put("list", statisticsService.findGeneralYear(param));//当年所有数据
		}
		return param ;
	}
	@ResponseBody
	@RequestMapping(value = "/findGeneralData")
	public Map<String,Object> findGeneralByParam(HttpSession session, ModelMap map,
			@RequestParam(value = "type",required=false) String type,
			@RequestParam(value = "orgCode",required=false) String orgCode,
			@RequestParam(value = "year",required=false) String year) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("orgCode", orgCode);//某区域历史数据
		//年,类型所有区域对比
		param.put("type", type);
		param.put("year", year);
		param.put("list", statisticsService.findGeneralByParam(param));
		return param ;
	}
	
	@ResponseBody
	@RequestMapping(value = "/findGDPDate")
	public Map<String,Object> findGDPDate(HttpSession session, ModelMap map,	
			@RequestParam(value = "orgCode") String orgCode,@RequestParam(value = "table") String table,
			@RequestParam(value = "month",required=false) String month,
			@RequestParam(value = "type",required=false) String type,
			@RequestParam(value = "year",required=false) String year) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("orgCode", orgCode);
		param.put("year", year);
		param.put("month", month);
		param.put("type", type);
		param.put("table", "zg_ecno_"+table);
		if(year == null && month == null){
			param.put("list", statisticsService.findGDPYears(param));
		}else{
			param.put("list", statisticsService.findGDPYearOrMonth(param));
		}
		return param ;
	}
	
	
	@RequestMapping(value = "/showZSYZ")
	public String showJT1(HttpServletRequest request,HttpSession session, ModelMap map) {
		map.addAttribute("elementsCollectionStr", request.getParameter("elementsCollectionStr"));
		return "/szzg/zsyz/zsyz.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/ZSYZlistData")
	public Object ZSYZlistData(HttpServletRequest request, HttpSession session, ModelMap map,
		int page, int rows,@RequestParam(value = "pName", required = false) String pName
		,@RequestParam(value = "pType", required = false) String pType) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pName", pName);
		params.put("pType", pType);
		return attractInvestmentService.searchList(page, rows, params);
	}
	
	/**
	 * 城管事件展示跳转
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/showUrbanManagementIndex")
	public String showUrbanManagementIndex(HttpServletRequest request,HttpSession session, ModelMap map) {
		map.addAttribute("currentDate", Calendar.getInstance().get(Calendar.YEAR)+"-"+(Calendar.getInstance().get(Calendar.MONTH)+1));
		return "/szzg/statistics/index_statistics_urmanager.ftl";
	}
	
	/**
	 * 政务审核展示跳转
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/showGovernmentAudit")
	public String showGovernmentAuditIndex(HttpServletRequest request,HttpSession session, ModelMap map) {
		map.addAttribute("currentDate", Calendar.getInstance().get(Calendar.YEAR)+"-"+(Calendar.getInstance().get(Calendar.MONTH)+1));
		return "/szzg/statistics/index_statistics_governmentaudit.ftl";
	}
	
}
