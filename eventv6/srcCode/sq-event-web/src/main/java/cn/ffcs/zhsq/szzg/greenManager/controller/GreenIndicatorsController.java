package cn.ffcs.zhsq.szzg.greenManager.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.zhsq.mybatis.domain.szzg.greenManager.StatsGreenBO;
import cn.ffcs.zhsq.szzg.greenManager.service.GreenIndicatorsService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import cn.ffcs.system.publicUtil.EUDGPagination;

@Controller
@RequestMapping(value="/zhsq/szzg/greenindicators")
public class GreenIndicatorsController {


	@Autowired
	private GreenIndicatorsService greenIndicatorsService;

	@Autowired
	private IResMarkerService resMarkerService;
	// 模块路径
	private final static String REAL_PATH = "/szzg/";
	// 模块名称
	private final static String SUB_MAIN = "greenManager/greenStat";
	
	@RequestMapping(value="/index")
	public String index(HttpServletRequest request,HttpSession session,ModelMap map)
	{
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("orgCode",userInfo.getInfoOrgCodeStr());
		map.addAttribute("orgName",userInfo.getOrgName());

		return "/szzg/greenManager/greenStat/index_greenStat.ftl";
	}




	/**
	 * 前端首页
	 *
	 * @return
	 */
	@RequestMapping(value = "/showIndex")
	public String showIndex(HttpSession session, ModelMap model)throws Exception {

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		model.addAttribute("orgCode",userInfo.getInfoOrgCodeStr());
		model.addAttribute("orgName",userInfo.getOrgName());
		model.addAttribute("yearStr", Calendar.getInstance().get(Calendar.YEAR));

		return REAL_PATH + SUB_MAIN + "/lhzh_lhqs.ftl";
	}


	/**
	 * 分页查询
	 *
	 * @param statsGreenBO
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/listData")
	@ResponseBody
	public EUDGPagination listData(StatsGreenBO statsGreenBO,
								   @RequestParam(value = "page") int page,
								   @RequestParam(value = "rows") int rows) {

		if (page <= 0)
			page = 1;


		Map<String, Object> params = new HashMap<String, Object>();

		if (statsGreenBO.getSyear() != null) {
			params.put("syear", statsGreenBO.getSyear());
		}
		if (statsGreenBO.getOrgCode() != null) {
			params.put("orgCode", statsGreenBO.getOrgCode());
		}
		EUDGPagination eudgPagination = greenIndicatorsService.findPageListByCriteria(params, page, rows);
		return eudgPagination;
	}

	@RequestMapping(value="/add")
	public String add(HttpSession session,ModelMap model){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		model.addAttribute("orgId",userInfo.getOrgId());
		model.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		model.addAttribute("markerOperation", ConstantValue.ADD_MARKER); // 编辑标注操作
		return "/szzg/greenManager/greenStat/add_greenStat.ftl";
	}

	@RequestMapping(value = "searchData")
	@ResponseBody
	public List<StatsGreenBO> searchData(StatsGreenBO statsGreenBO){
		Map<String,Object > params=new HashMap<String,Object>();

		if(statsGreenBO.getSyear()!=null&&statsGreenBO.getSyear()!="")
		{
			params.put("syear",statsGreenBO.getSyear());
		}

		if(statsGreenBO.getOrgCode()!=null&&statsGreenBO.getOrgCode()!="")
		{
			params.put("orgCode",statsGreenBO.getOrgCode());
		}
		List<StatsGreenBO> greenBOS =  greenIndicatorsService.findStatsGreenByParams(params);
		return greenBOS;
	}
	
	@RequestMapping(value="/edit")
	public String edit(HttpSession session,@RequestParam(value="seqid")String seqid, ModelMap map){
		Long id = Long.parseLong(seqid);
		StatsGreenBO statsGreen = greenIndicatorsService.findGreenIndicatorsById(id);


		ResMarker resMarker=new ResMarker();
		if(statsGreen.getX()!=null&&statsGreen.getX()!="")
		{
			resMarker.setX(statsGreen.getX());
		}


		if(statsGreen.getY()!=null&&statsGreen.getY()!="")
		{
			resMarker.setY(statsGreen.getY());
		}

		resMarker.setMarkerId(id);
		resMarker.setMarkerType("GREEN_MARK1");
		statsGreen.setResMarker(resMarker);
		map.addAttribute("id",id);
		map.addAttribute("markerOperation", ConstantValue.EDIT_MARKER); // 编辑标注操作
		map.addAttribute("statsGreen", statsGreen);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		return "/szzg/greenManager/greenStat/edit_greenStat.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate")
	public Map<String, Object> save(HttpSession session, ModelMap map,
									@ModelAttribute(value = "statsGreenBO") StatsGreenBO statsGreenBO,String module) {

		if(module!=null)
		{
			statsGreenBO.getResMarker().setMarkerType(module);
		}

		statsGreenBO.getResMarker().setCatalog("03");


		if(statsGreenBO.getResMarker().getX()!=null)
		{
			statsGreenBO.setX(statsGreenBO.getResMarker().getX());
		}


		if(statsGreenBO.getResMarker().getY()!=null)
		{
			statsGreenBO.setY(statsGreenBO.getResMarker().getY());
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean result = false;
		Long seqid = statsGreenBO.getSeqid();

		statsGreenBO.setStatus("1");

		Map<String, Object> params = new HashMap<String, Object>();

		if (statsGreenBO.getSyear() != null) {
			params.put("syear", statsGreenBO.getSyear());
		}
		if (statsGreenBO.getOrgCode() != null) {
			params.put("orgCode", statsGreenBO.getOrgCode());
		}
		List<StatsGreenBO> statsGreenBOS = greenIndicatorsService.findStatsGreenByParams(params);


		if (seqid != null && seqid > 0L) { // --更新
				statsGreenBO.setUpdateTime(new Date());
				statsGreenBO.getResMarker().setResourcesId(seqid);
				resMarkerService.saveOrUpdateResMarker(statsGreenBO.getResMarker());
				result = greenIndicatorsService.update(statsGreenBO);
				resultMap.put("msg", Msg.EDIT.getMsg(result));

		} else { // --新增
			if (statsGreenBOS!=null&&statsGreenBOS.size()!=0) {
				resultMap.put("msg", "同一年份同一区域只允许一条数据");

			} else {
				statsGreenBO.setOpdate(new Date());
				result = greenIndicatorsService.insert(statsGreenBO);
				result = statsGreenBO.getSeqid() > 0;
				statsGreenBO.getResMarker().setResourcesId(statsGreenBO.getSeqid());
				resMarkerService.saveOrUpdateResMarker(statsGreenBO.getResMarker());
				resultMap.put("msg", Msg.ADD.getMsg(result));
			}
		}

		resultMap.put("result", result);
		return resultMap;
	}

	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request,HttpSession session,ModelMap map,@RequestParam(value = "seqid") Long seqid)
	{
		StatsGreenBO statsGreenBO = greenIndicatorsService.findGreenIndicatorsById(seqid);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.addAttribute("id", seqid);
		map.addAttribute("markerOperation", ConstantValue.WATCH_MARKER); // 添加标注操作
		map.addAttribute("statsGreen",statsGreenBO);
		return "/szzg/greenManager/greenStat/detail_greenStat.ftl";
	}


	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> delete(HttpSession session, HttpServletRequest request,
									  @RequestParam(value = "seqid") Long seqid) {
		boolean isSuccess = greenIndicatorsService.del(seqid);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (isSuccess) {
			resultMap.put("result", 1);
		} else {
			resultMap.put("result", 0);
		}

		resultMap.put("msg", Msg.DELETE.getMsg(isSuccess));
		return resultMap;
	}



	@RequestMapping(value = "/find_statsGreenByYear")
	@ResponseBody
	public Map<String, Object> find_statsGreenByYear(String syear,String orgCode,
			ModelMap model) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("syear", syear);
		params.put("orgCode",orgCode);
		List<StatsGreenBO> statsGreenBOs = greenIndicatorsService.findStatsGreenByParams(params);
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		if(statsGreenBOs!=null&&statsGreenBOs.size()!=0)
		{

			Map<String, Object> typeMap = new HashMap<String, Object>();
			typeMap.put("name", "建成区面积(平方公里)");
			typeMap.put("value", statsGreenBOs.get(0).getBuiltupArea());
			dataList.add(typeMap);
			Map<String, Object> typeMap1 = new HashMap<String, Object>();
			typeMap1.put("name", "城市绿化面积(公顷)");
			typeMap1.put("value", statsGreenBOs.get(0).getgArea());
			dataList.add(typeMap1);
			Map<String, Object> typeMap2 = new HashMap<String, Object>();
			typeMap2.put("name", "公园绿地面积(公顷)");
			typeMap2.put("value", statsGreenBOs.get(0).getgParkarea());
			dataList.add(typeMap2);
			Map<String, Object> typeMap3 = new HashMap<String, Object>();
			typeMap3.put("name", "森林面积(公顷)");
			typeMap3.put("value", statsGreenBOs.get(0).getForestArea());
			dataList.add(typeMap3);
			Map<String, Object> typeMap4 = new HashMap<String, Object>();
			typeMap4.put("name", "建成区人口(万人)");
			typeMap4.put("value", statsGreenBOs.get(0).getPopu());
			dataList.add(typeMap4);
			Map<String, Object> typeMap5= new HashMap<String, Object>();
			typeMap5.put("name", "城市绿地率(%)");
			typeMap5.put("value", statsGreenBOs.get(0).getgRate());
			dataList.add(typeMap5);
			Map<String, Object> typeMap6 = new HashMap<String, Object>();
			typeMap6.put("name", "人均公园绿地面积(平方米)");
			typeMap6.put("value", statsGreenBOs.get(0).getgPerparkarea());
			dataList.add(typeMap6);
			Map<String, Object> typeMap7 = new HashMap<String, Object>();
			typeMap7.put("name", "绿化覆盖面积(公顷)");
			typeMap7.put("value", statsGreenBOs.get(0).getgCoverarea());
			dataList.add(typeMap7);
			Map<String, Object> typeMap8 = new HashMap<String, Object>();
			typeMap8.put("name", "区域面积(平方公里)");
			typeMap8.put("value", statsGreenBOs.get(0).getRegionalArea());
			dataList.add(typeMap8);
			Map<String, Object> typeMap9 = new HashMap<String, Object>();
			typeMap9.put("name", "森林覆盖率(%)");
			typeMap9.put("value", statsGreenBOs.get(0).getForestCoverarea());
			dataList.add(typeMap9);
			map.put("statsGreenBO", statsGreenBOs.get(0));
		}


		map.put("dataList", dataList);
		return map;
	}


	@RequestMapping(value = "findDataByQs")
	@ResponseBody
	public Map<String,Object> findDataByQs(HttpSession session, HttpServletRequest request,String orgCode)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgCode",orgCode);
		Map<String,Object>mapList=greenIndicatorsService.findChartDataByQs(params);

		map.put("dataList",mapList);
		return map;
	}
}
