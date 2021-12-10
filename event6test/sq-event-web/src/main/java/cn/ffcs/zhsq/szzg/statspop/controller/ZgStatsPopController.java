package cn.ffcs.zhsq.szzg.statspop.controller;

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

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.szzg.statspop.ZgStatsPop;
import cn.ffcs.zhsq.szzg.statspop.service.IZgStatsPopService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**
 * 人口基本情况
 * @author huangwenbin
 *
 */
@Controller
@RequestMapping("/zhsq/szzg/zgStatsPopController")
public class ZgStatsPopController extends ZZBaseController{
	@Autowired
	private IZgStatsPopService statsPopService;
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;
	
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request,HttpSession session, ModelMap map) {
		String pcode=  request.getParameter("pcode");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("dictCode", pcode);
		param = statsPopService.findDataDict(param);
		map.addAttribute("pName",  param!=null?param.get("DICT_NAME").toString():"");
		map.addAttribute("pcode",  request.getParameter("pcode"));
		map.addAttribute("orgCode",this.getDefaultInfoOrgCode(session));
		map.addAttribute("orgName", this.getDefaultInfoOrgName(session));
		map.addAttribute("currentYear", Calendar.getInstance().get(Calendar.YEAR));
		return "/szzg/statsPop/list_statsPop.ftl";
	}	
	
	@RequestMapping(value = "/toAdd")
	public String toAdd(HttpSession session, @RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "pcode") String pcode,ModelMap map) {
		ZgStatsPop entity = new ZgStatsPop();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(id !=null){
			entity = statsPopService.findById(id);
		}else{
			entity.setSyear(Calendar.getInstance().get(Calendar.YEAR));
			entity.setOrgCode(this.getDefaultInfoOrgCode(session));
			entity.setOrgName(this.getDefaultInfoOrgName(session));
		}
		map.addAttribute("userId",userInfo.getUserId());
		map.addAttribute("entity",entity);
		map.addAttribute("pcode",pcode);
		return "/szzg/statsPop/add_statsPop.ftl";
	}
	
	@RequestMapping(value = "/detail")
	public String detail(HttpSession session, @RequestParam(value = "id") Long id,
			ModelMap map) {
		map.addAttribute("entity",statsPopService.findById(id));
		return "/szzg/statsPop/detail_statsPop.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/listData")
	public EUDGPagination listData(HttpSession session, @RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,	@RequestParam(value = "pcode") String pcode,
			@RequestParam(value = "orgCode", required = false) String orgCode,
			@RequestParam(value = "syear", required = false) String syear,
			@RequestParam(value = "stype", required = false) String stype) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("orgCode", orgCode);
		param.put("syear", syear);
		param.put("stype", stype);
		param.put("pcode", pcode);
		return statsPopService.findByPagination(page, rows, param);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/delete")
	public ResultObj del(HttpSession session, ModelMap map,
			@RequestParam(value="id")Long id) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("id", id);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		param.put("updateId", userInfo.getUserId());
		return Msg.DELETE.getResult(statsPopService.delete(param));
	}
	
	/**
	 * 更新或保存
	 * @param session
	 * @param map
	 * @param pt
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate")
	public ResultObj saveOrUpdate(HttpSession session, ModelMap map,ZgStatsPop entity)  {
		Msg msg = Msg.OPERATE;
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("orgCode", entity.getOrgCode());
		param.put("syear", entity.getSyear());
		param.put("stype", entity.getStype());
		param.put("seqId", entity.getSeqId());
		int count = statsPopService.findCount(param);
		
		if(count >0){
			return msg.getResult(false, "相同网格/年份/类型数据已经存在!");
		}
		msg = entity.getSeqId() != null?Msg.EDIT:Msg.ADD;
		return msg.getResult(statsPopService.addOrUpdate(entity));
	}
	
	@ResponseBody
	@RequestMapping(value = "/findCount")
	public long findCount(HttpSession session, ModelMap map,	
			@RequestParam(value = "orgCode") String orgCode,
			@RequestParam(value = "syear") String syear,
			@RequestParam(value = "seqId", required = false) String seqId,
			@RequestParam(value = "stype") String stype)  {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("orgCode", orgCode);
		param.put("syear", syear);
		param.put("stype", stype);
		param.put("seqId", seqId);
		return statsPopService.findCount(param);
	}
	
	/**
	 * 批量 更新或保存//暂时不用
	 * @param session
	 * @param map
	 * @param pt
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdateByList")
	public ResultObj saveOrUpdateByList(HttpSession session, ModelMap map,@RequestBody ZgStatsPop[] arr)  {
		Msg msg = Msg.OPERATE;
		if(arr == null || arr.length == 0){
			return msg.getResult(false, "请求无数据!");
		}
		
		List<ZgStatsPop> list = Arrays.asList(arr);
		int num = 0;
		if(list.get(0).getSeqId() == null){//新增
			msg = Msg.ADD;
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("orgCode", list.get(0).getOrgCode());
			param.put("syear", list.get(0).getSyear());
			List<ZgStatsPop> existList = statsPopService.findListByParams(param);
			if(existList.size() > 0){
				return msg.getResult(false, existList.get(0).getOrgName()+" "
			+existList.get(0).getSyear() +"　已经存在记录");
			}
			num = statsPopService.insertByList(list);
		}else{//修改
			msg = Msg.EDIT;
			num = statsPopService.updateByList(list);
		}
		return msg.getResult(num);
	}
	
	/**
	 * 人口首页展示跳转
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/showIndex")
	public String showIndex(HttpServletRequest request,HttpSession session, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String page = request.getParameter("page");
		map.addAttribute("currentYear", Calendar.getInstance().get(Calendar.YEAR));
		if("jiben".equals(page)||"qs".equals(page)){
			map.addAttribute("orgcode", this.getDefaultInfoOrgCode(session));
			map.addAttribute("orgname", this.getDefaultInfoOrgName(session));
			
			map.addAttribute("orgList",orgEntityInfoOutService.findOrgListbyOrgCode(userInfo.getOrgCode()));
		}
		map.addAttribute("BI_URL", App.BI.getDomain(session));
		return "/szzg/statsPop/"+page+"_statsPop.ftl";
	}
	
	/**
	 * 报表
	 * @param stype 类型
	 * @param orgCode 区域
	 * @param syear 年份
	 */
	@ResponseBody
	@RequestMapping(value = "/findChartByParams")
	public Map<String,Object> findChartByParams(HttpSession session, ModelMap map,
			@RequestParam(value = "orgCode", required = false) String orgCode,
			@RequestParam(value = "syear", required = false) String syear,
			@RequestParam(value = "stype", required = false) String stype,
			@RequestParam(value = "module", required = false) String module) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("orgCode", orgCode);
		param.put("syear", syear);
		if("jiben_1".equals(module)){//人口基本
			param.put("stype", "'S001001003','S001001000','S001001001'");//在校学生,农业,非农业
			param.put("ny", statsPopService.findChartByJB(param));
			param.put("stype", "'S001001004','S001001005'");//户籍人口,流动人口
			param.put("move", statsPopService.findChartByJB(param));
			param.put("stype", "'S001001006','S001001007'");//迁入人口,迁出人口
			param.put("outin", statsPopService.findChartByJB(param));
		}else if("jiben_2".equals(module)){
			param.put("stype", "'S001001008','S001001009','S001001010'");//人口总数
			param.put("list", statsPopService.findPersonAllYear(param));
		}else if("zd".equals(module)){//重点人员
			param.put("stype",stype);
			param = statsPopService.findChartByZDPerson(param);
		}else if("db".equals(module)){//低保
			param = statsPopService.findChartByDB(param);
		}else if("cj".equals(module)){//残疾
			param = statsPopService.findChartByCJ(param);
		}else if("jjyl_1".equals(module)){//居家养老
			param.put("multiple", "multiple");
			param = statsPopService.findChartByJJYL(param);
		}else if("jjyl_2".equals(module)){//居家养老
			param = statsPopService.findChartByJJYL(param);
		}else if("qs".equals(module)){//趋势
			param.put("stype", "S001003");
			param.put("qsCode", orgCode);
			param = statsPopService.findChartByQS(param);
		}
		return param;
	}
}
