package cn.ffcs.zhsq.szzg.greenManager.controller;

import java.util.*;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;

import cn.ffcs.zhsq.mybatis.domain.szzg.greenManager.GreenBeltBO;
import cn.ffcs.zhsq.szzg.greenManager.service.GreenBeltService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping(value="/zhsq/szzg/green")
public class GreenBeltController {

	@Autowired
	private IBaseDictionaryService dictionaryService;

	@Autowired
	private GreenBeltService greenBeltService;


	@Autowired
	private IResMarkerService resMarkerService;

	// 模块路径
	private final static String REAL_PATH = "/szzg/";
	// 模块名称
	private final static String SUB_MAIN = "greenManager/green";

	/**
	 * 前端首页
	 *
	 * @return
	 */
	@RequestMapping(value = "/showIndex")
	public String index(HttpServletRequest request,HttpSession session, ModelMap model)throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S002002", userInfo.getOrgCode());

		model.addAttribute("gridId",userInfo.getOrgId());
		model.addAttribute("orgCode",userInfo.getInfoOrgCodeStr());
		model.addAttribute("orgName",userInfo.getOrgName());
		model.addAttribute("gType",list);
		model.addAttribute("yearStr", Calendar.getInstance().get(Calendar.YEAR));

		return REAL_PATH + SUB_MAIN + "/lhfb.ftl";
	}


	//
//	/**
//	 * 获取绿地分布
//	 * @param type
//	 * @param name
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value="getGreenBeltDistribution")
//	@ResponseBody
//	public Map<String, Object> getGreenBeltDistribution(String type, String name,
//			ModelMap model) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("codetype", Constants.GREEN_BELT_TYPE);
//		List<DictionaryBO> typeList = szzgTreeService.findDicAllType(params);
//		if(!StringUtil.isEmpty(name)){
//			params.put("gName", name);
//		}
//		if(!StringUtil.isEmpty(type)){
//			params.put("gType", type);
//		}
//		List<GreenBeltBO> greenList = greenBeltService.findGreenByParams(params);
//		map.put("typeList", typeList);
//		map.put("greenList", greenList);
//		return map;
//	}
//
	@RequestMapping(value="index")
	public String index(HttpSession session, ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codetype","S002002");
		List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S002002", userInfo.getOrgCode());

		map.addAttribute("orgCode",userInfo.getInfoOrgCodeStr());
		map.addAttribute("orgName",userInfo.getOrgName());
		map.addAttribute("gType",list);

		return "/szzg/greenManager/green/index_green.ftl";
	}

	@RequestMapping(value="listData")
	@ResponseBody
	public EUDGPagination listData(@RequestParam(value = "page") int page,
								   @RequestParam(value = "rows") int rows,
								   GreenBeltBO greenBelt){
		Map<String, Object> params = new HashMap<String, Object>();
		if(greenBelt.getgName()!=null){
			params.put("gName", greenBelt.getgName());
		}
		if(greenBelt.getLocation()!=null){
			params.put("location", greenBelt.getLocation());
		}
		if(greenBelt.getgType()!=null){
			params.put("gType", greenBelt.getgType());
		}
		if(greenBelt.getOrgCode()!=null){
			params.put("orgCode", greenBelt.getOrgCode());
		}
		EUDGPagination eudgPagination =  greenBeltService.findPageListByCriteria(params, page, rows);
		return eudgPagination;
	}

	@RequestMapping(value="searchData")
	@ResponseBody
	public List<GreenBeltBO> searchData(
								   GreenBeltBO greenBelt){
		Map<String, Object> params = new HashMap<String, Object>();

		if(greenBelt.getgType()!=null){
			params.put("gType", greenBelt.getgType());
		}
		if(greenBelt.getgName()!=null){
			params.put("gName", greenBelt.getgName());
		}

		if(greenBelt.getOrgCode()!=null){
			params.put("orgCode", greenBelt.getOrgCode());
		}
		List<GreenBeltBO> greenBeltBOS =  greenBeltService.findGreenByParams(params);
		return greenBeltBOS;
	}


	@RequestMapping(value="/add")
	public String add(HttpSession session,ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		map.addAttribute("orgId",userInfo.getOrgId());
		params.put("codetype", "S002002");
		List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S002002", userInfo.getOrgCode());
		map.put("gType", list);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.addAttribute("markerOperation", ConstantValue.ADD_MARKER); // 编辑标注操作
		return "/szzg/greenManager/green/add_green.ftl";
	}

	@RequestMapping(value="/edit")
	public String edit(HttpSession session,@RequestParam(value="seqid")String seqid,
					   ModelMap map){
		Long id = Long.parseLong(seqid);
		GreenBeltBO greenBeltBO = greenBeltService.findGreenBeltById(id);
		map.addAttribute("greenBeltBO",greenBeltBO);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.addAttribute("id","1");
		ResMarker resMarker=new ResMarker();
		if(greenBeltBO.getX()!=null&&greenBeltBO.getX()!="")
		{
			resMarker.setX(greenBeltBO.getX());
		}


		if(greenBeltBO.getY()!=null&&greenBeltBO.getY()!="")
		{
			resMarker.setY(greenBeltBO.getY());
		}

		resMarker.setMarkerId(id);
		resMarker.setMarkerType("GREEN_MARK");
		greenBeltBO.setResMarker(resMarker);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codetype", "S002002");
		List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S002002", userInfo.getOrgCode());
		map.put("greenType", list);
		map.addAttribute("id",id);
		map.addAttribute("markerOperation", ConstantValue.EDIT_MARKER); // 编辑标注操作

		return "/szzg/greenManager/green/edit_green.ftl";
	}



	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate")
	public Map<String, Object> save(HttpSession session, ModelMap map,
									@ModelAttribute(value = "greenBeltBO") GreenBeltBO greenBeltBO,String module) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(greenBeltBO.getStd().equals(""))
		{
			greenBeltBO.setStd("0");
		}

		if(module!=null)
		{
			greenBeltBO.getResMarker().setMarkerType(module);
		}

		greenBeltBO.getResMarker().setCatalog("03");


		if(greenBeltBO.getResMarker().getX()!=null)
		{
			greenBeltBO.setX(greenBeltBO.getResMarker().getX());
		}


		if(greenBeltBO.getResMarker().getY()!=null)
		{
			greenBeltBO.setY(greenBeltBO.getResMarker().getY());
		}

		boolean result = false;
		Long seqid = greenBeltBO.getSeqid();

		greenBeltBO.setStatus("1");




		if (seqid != null && seqid > 0L) { // --更新

			greenBeltBO.setUpdateTime(new Date());
			greenBeltBO.getResMarker().setResourcesId(seqid);
			resMarkerService.saveOrUpdateResMarker(greenBeltBO.getResMarker());
			result = greenBeltService.update(greenBeltBO);
			resultMap.put("msg", Msg.EDIT.getMsg(result));


		} else { // --新增

				greenBeltBO.setOpdate(new Date());
				result = greenBeltService.insert(greenBeltBO);
				result = greenBeltBO.getSeqid() > 0;
				greenBeltBO.getResMarker().setResourcesId(greenBeltBO.getSeqid());
				resMarkerService.saveOrUpdateResMarker(greenBeltBO.getResMarker());
				resultMap.put("msg", Msg.ADD.getMsg(result));

		}

		resultMap.put("result", result);
		return resultMap;
	}

	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request,HttpSession session,ModelMap map,@RequestParam(value = "seqid") Long seqid)
	{
		GreenBeltBO greenBeltBO = greenBeltService.findGreenBeltById(seqid);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.addAttribute("id", seqid);
		map.addAttribute("markerOperation", ConstantValue.WATCH_MARKER); // 添加标注操作
		map.addAttribute("greenBeltBO",greenBeltBO);
		return "/szzg/greenManager/green/detail_green.ftl";
	}
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> del(HttpSession session, HttpServletRequest request,
								   @RequestParam(value = "seqid") Long seqid) {
		boolean isSuccess = greenBeltService.del(seqid);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (isSuccess) {
			resultMap.put("result", 1);
		} else {
			resultMap.put("result", 0);
		}

		resultMap.put("msg", Msg.DELETE.getMsg(isSuccess));
		return resultMap;
	}

}
