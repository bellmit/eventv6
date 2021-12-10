package cn.ffcs.zhsq.administrationPenalty.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.administrationPenalty.service.AdministrationPenaltyService;
import cn.ffcs.zhsq.mybatis.domain.administrationPenalty.AdministrationPenalty;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * Created by 张天慈 on 2017/12/5.
 */
@Controller("penaltyController")
@RequestMapping("/zhsq/szzg/zgPenaltyController")
public class AdministrationPenaltyController extends ZZBaseController {

	//注入行政处罚服务模块
	@Autowired
	private AdministrationPenaltyService penaltyService;

	/**
	 * 数据列表页面
	 */
	@RequestMapping(value = "/index")
	public Object index(HttpSession session, ModelMap map){
		//获取当前登录用户的信息
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		//获取默认网格信息
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("gridId",defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("gridCode",defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("gridName",defaultGridInfo.get(KEY_START_GRID_NAME));

		//BD工程域的域名
		map.addAttribute("IMPEXP_DOMAIN", App.IMPEXP.getDomain(session));
		map.put("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));

		return "/szzg/administrationPenalty/list_penalty.ftl";
	}

	/**
	 * 分页显示数据
	 */
	@ResponseBody
	@RequestMapping(value = "/listData",method = RequestMethod.POST)
	public Object listData(HttpSession session, AdministrationPenalty penalty, ModelMap map, int page, int rows){

		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;

		Map<String,Object> params = new HashedMap();
		params.put("gridCode",penalty.getGridCode());
		params.put("enterpriseName",penalty.getEnterpriseName());
		params.put("decisionInstrument",penalty.getDecisionInstrument());
		params.put("penaltyBases",penalty.getPenaltyBases());
		params.put("penaltyDate",penalty.getPenaltyDate());
		params.put("penaltyConclusion",penalty.getPenaltyConclusion());

		EUDGPagination pagination = penaltyService.searchList(page,rows,params);

		return pagination;
	}

	/**
	 * 新增行政处罚信息
	 */
	@RequestMapping("add")
	public Object addTrademark(HttpSession session,ModelMap map){

		AdministrationPenalty penalty = new AdministrationPenalty();


		//获取默认网格信息
		Map<String,Object> defaultGridInfo = this.getDefaultGridInfo(session);
		penalty.setGridId((Long)defaultGridInfo.get(KEY_START_GRID_ID));
		penalty.setGridCode((String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		penalty.setGridName((String)defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("penalty",penalty);

		return "/szzg/administrationPenalty/add_penalty.ftl";
	}

	/**
	 * 修改商标信息
	 */
	@RequestMapping("/edit")
	public Object update(ModelMap map,HttpSession session,Long id){
		AdministrationPenalty penalty = penaltyService.findById(id);
		map.addAttribute("penalty",penalty);

		return "/szzg/administrationPenalty/add_penalty.ftl";
	}

	/**
	 * 保存或更新数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, AdministrationPenalty penalty) {

		// 保存数据的结果信息，success OR fail
		Map<String, Object> resultMap = new HashMap<String, Object>();

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		penalty.setRegistrationId(request.getParameter("registrationId"));
		penalty.setEnterpriseName(request.getParameter("enterpriseName"));
		penalty.setDecisionInstrument(request.getParameter("decisionInstrument"));
		penalty.setPenaltyBases(request.getParameter("penaltyBases"));
		penalty.setPenaltyConclusion(request.getParameter("penaltyConclusion"));
		penalty.setPenaltyCause(request.getParameter("penaltyCause"));
		penalty.setRegionCode(request.getParameter("gridCode"));
		penalty.setPenaltyDateStr(request.getParameter("penaltyDate"));
		//统计年月
		String penaltyTime = request.getParameter("penaltyDate");
		if(penaltyTime != ""){
			String penaltyTimeArr[] = penaltyTime.split("-");
			penalty.setSyear(Long.parseLong(penaltyTimeArr[0]));
			penalty.setSmonth(Long.parseLong(penaltyTimeArr[1]));
		}


		String result = "fail";

		if (penalty.getPenaltyId() == null) {// 处罚信息不存在,新增该设备信息
			Boolean flag = penaltyService.insert(session,penalty); // 保存该设备信息
			if (flag == true) {
				result = "success";
			}

		} else {// 处罚信息存在,修改处罚信息

			Boolean flag = penaltyService.update(session,penalty);

			if (flag == true) {
				result = "success";
			}
		}
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 删除处罚信息
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(AdministrationPenalty penalty,HttpSession session){

		Map<String,Object> resultMap = new HashMap();
		String result = "fail";

		Boolean flag = penaltyService.delete(session,penalty);
		if(flag == true){
			result = "success";
		}
		resultMap.put("result",result);
		return resultMap;
	}

	/**
	 * 处罚信息详情页面
	 */
	@RequestMapping("/detail")
	public Object detail(HttpSession session,Long id,ModelMap map){

		if( id != null){
			AdministrationPenalty penalty = penaltyService.findById(id);
			map.addAttribute("penalty",penalty);
		}

		return "/szzg/administrationPenalty/detail_penalty.ftl";
	}

	//根据年份，月份统计处罚信息
	@ResponseBody
	@RequestMapping(value = "/findPenaltyByYM")
	public Object findPenaltyByYM(ModelMap map,
								  @RequestParam(value = "syear")String syear,
								  @RequestParam(value = "smonth")String smonth){

		Map<String,Object> param = new HashedMap();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//年记录条数
		param.put("syear",syear);
		Long yearCount = penaltyService.countList(param);
		//指定月份记录条数
		param.put("smonth",smonth);
		Long monthCount = penaltyService.countList(param);
		//指定月份处罚信息总数据
		List<AdministrationPenalty> list = penaltyService.findPenaltyByYM(param);

		//统计12个月
		resultMap.put("monthlist",penaltyService.findChart12Month(param));

		//指定月份前一个月记录条数
		Long currentYear =Long.parseLong(syear);
		Long currentMonth = Long.parseLong(smonth);//当前月份
		//如果当前月份是1月份，查询上一年12月份的记录条数
		Long preMonthCount = null;
		if(currentMonth == 1){
			currentYear = currentYear - 1;
			currentMonth = 12l;
			param.put("syear",currentYear.toString());
			param.put("smonth",currentMonth.toString());
			preMonthCount = penaltyService.countList(param);
		}else{
			currentMonth = currentMonth - 1;
			param.put("smonth",currentMonth.toString());
			preMonthCount = penaltyService.countList(param);
		}

		//本月比上月同期增长，减少
		Float changeRate = 0f;
		if(preMonthCount != 0){
			Long diff = Math.abs(monthCount - preMonthCount);
			changeRate = (diff*1.0f)/(preMonthCount*1.0f)*(100f);
		}else{
			changeRate = monthCount*(100f);
		}



		resultMap.put("yearCount",yearCount);
		resultMap.put("monthCount",monthCount);
		resultMap.put("preMonthCount",preMonthCount);
		resultMap.put("list",list);
		resultMap.put("changeRate",changeRate);

		return resultMap;
	}
}
