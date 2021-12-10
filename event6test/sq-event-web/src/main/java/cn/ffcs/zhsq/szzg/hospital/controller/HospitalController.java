package cn.ffcs.zhsq.szzg.hospital.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;

import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.szzg.hospital.HospitalBO;
import cn.ffcs.zhsq.szzg.hospital.service.HospitalService;
import cn.ffcs.zhsq.szzg.resource.service.ZgResourceInfoService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import cn.ffcs.system.publicUtil.EUDGPagination;

@Controller
@RequestMapping(value="/zhsq/szzg/hospital")
public class HospitalController extends ZZBaseController {

	@Autowired
	private HospitalService hospitalService;


	@Autowired
	private IBaseDictionaryService dictionaryService;

	@Autowired
	private IResMarkerService resMarkerService;

	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;

	@Autowired
	private ZgResourceInfoService zgResourceInfoService;


	@Autowired
	private IMixedGridInfoService iMixedGridInfoService;

	private final static String REAL_PATH = "/szzg/";
	// 模块名称
	private final static String SUB_MAIN = "hospital";

	/**
	 * 前端首页
	 *
	 * @return
	 */
	@RequestMapping(value = "/showIndex")
	public String index(HttpServletRequest request,HttpSession session, ModelMap model)throws Exception {

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		model.addAttribute("orgCode",userInfo.getInfoOrgCodeStr());

		List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S008001", userInfo.getOrgCode());
		model.addAttribute("list",list);

		return REAL_PATH + SUB_MAIN + "/jmjy.ftl";
	}

	@RequestMapping(value="/findHospitalMark",method=RequestMethod.POST)
	@ResponseBody
	public List<HospitalBO> findHospitalMark(HttpServletRequest request){
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("xmin",request.getParameter("xmin"));
		params.put("xmax",request.getParameter("xmax"));
		params.put("ymin",request.getParameter("ymin"));
		params.put("ymax",request.getParameter("ymax"));

		List<HospitalBO> list = hospitalService.findHospitalMark(params);

		return list;
	}

	@RequestMapping(value="index")
	public String index(HttpSession session, ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type","S008001");
		List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S008001", userInfo.getOrgCode());
		map.put("orgCode",userInfo.getOrgCode());
		map.put("orgName", userInfo.getOrgName());
		map.put("orgId", userInfo.getOrgId());
		map.addAttribute("type",list);

		return "/szzg/hospital/index_hospital.ftl";
	}

	@RequestMapping(value="listData")
	@ResponseBody
	public EUDGPagination listData(@RequestParam(value = "page") int page,
								   @RequestParam(value = "rows") int rows,
								   HospitalBO hospitalBO){
		Map<String, Object> params = new HashMap<String, Object>();
		if(hospitalBO.getAddress()!=null){
			params.put("address", hospitalBO.getAddress());
		}
		if(hospitalBO.getHospitalName()!=null){
			params.put("hospitalName", hospitalBO.getHospitalName());
		}
		if(hospitalBO.getType()!=null){
			params.put("type", hospitalBO.getType());
		}
		if(hospitalBO.getOrgCode()!=null){
			params.put("orgCode", hospitalBO.getOrgCode());
		}
		EUDGPagination eudgPagination =  hospitalService.findPageListByCriteria(params, page, rows);
		return eudgPagination;
	}

	@RequestMapping(value="searchData")
	@ResponseBody
	public List<HospitalBO>  searchPointData(HttpServletRequest request,HttpSession session,HospitalBO hospitalBO){
		Map<String, Object> params = new HashMap<String, Object>();
		if(hospitalBO.getHospitalMType()!=null&hospitalBO.getHospitalMType()!="")
		{
			params.put("hospitalMType",hospitalBO.getHospitalMType());
		}
		if(hospitalBO.getOrgCode()!=null&hospitalBO.getOrgCode()!="")
		{
			params.put("orgCode",hospitalBO.getOrgCode());
		}
		List<HospitalBO> hospitalBOS =  hospitalService.findHospitalMark(params);
		return hospitalBOS;
	}

	@RequestMapping(value="/add")
	public String add(HttpSession session,ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		map.addAttribute("orgCode",userInfo.getOrgCode());
		params.put("S008001", "S008001");
		List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S008001", userInfo.getOrgCode());
		map.put("type", list);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));

		map.addAttribute("markerOperation", ConstantValue.ADD_MARKER); // 添加标注操作


		return "/szzg/hospital/add_hospital.ftl";
	}

	@RequestMapping(value="/detail")
	public String detail(HttpSession session,ModelMap map,@RequestParam(value = "seqId") Long seqId,String show){

		HospitalBO hospitalBO = hospitalService.findHospitalById(seqId);
		if(hospitalBO.getIsNcmsFixedUnit()==null)
		{
			hospitalBO.setIsNcmsFixedUnit("0");
		}else if(hospitalBO.getIsNcmsFixedUnit().equals("0"))
		{
			hospitalBO.setIsNcmsFixedUnit("0");
		}else
		{
			hospitalBO.setIsNcmsFixedUnit("1");
		}

		if(hospitalBO.getIsUebmiFixedUnit()==null)
		{
			hospitalBO.setIsUebmiFixedUnit("0");
		}else if(hospitalBO.getIsUebmiFixedUnit().equals("0"))
		{
			hospitalBO.setIsUebmiFixedUnit("0");
		}else
		{
			hospitalBO.setIsUebmiFixedUnit("1");
		}
		if(show!=null)
		{
			map.addAttribute("show",show);
		}

		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.addAttribute("id", seqId);
		map.addAttribute("markerOperation", ConstantValue.WATCH_MARKER); // 添加标注操作
		map.addAttribute("hospitalBO",hospitalBO);
		return "/szzg/hospital/detail_hospital.ftl";
	}

	@RequestMapping(value="/edit")
	public String edit(HttpSession session,@RequestParam(value="seqId")String seqId,
					   ModelMap map){
		Long id = Long.parseLong(seqId);
		HospitalBO hospitalBO = hospitalService.findHospitalById(id);
		map.addAttribute("hospitalBO",hospitalBO);

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		map.addAttribute("orgCode",userInfo.getOrgCode());
		params.put("codetype", "S008001");
		List<BaseDataDict> list = dictionaryService.getDataDictListOfSinglestage("S008001", userInfo.getOrgCode());
		map.put("type", list);

		ResMarker resMarker=new ResMarker();
		if(hospitalBO.getX()!=null&&hospitalBO.getX()!="")
		{
			resMarker.setX(hospitalBO.getX());
		}


		if(hospitalBO.getY()!=null&&hospitalBO.getY()!="")
		{
			resMarker.setY(hospitalBO.getY());
		}

		resMarker.setMarkerId(id);
		resMarker.setMarkerType("HOSPITAL_MARK");
		hospitalBO.setResMarker(resMarker);

		map.addAttribute("markerOperation", ConstantValue.EDIT_MARKER); // 编辑标注操作
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));

		return "/szzg/hospital/edit_hospital.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate")
	public Map<String, Object> save(HttpSession session, ModelMap map,
									@ModelAttribute(value = "hospitalBO") HospitalBO hospitalBO,String module) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String typeCode="02071102";
		Map<String, Object> insertOrUpdateResult = new HashMap<String, Object>();
		if(module!=null)
		{
			hospitalBO.getResMarker().setMarkerType(module);
		}

		hospitalBO.getResMarker().setCatalog("03");


		if(hospitalBO.getResMarker().getX()!=null)
		{
			hospitalBO.setX(hospitalBO.getResMarker().getX());
		}


		if(hospitalBO.getResMarker().getY()!=null)
		{
			hospitalBO.setY(hospitalBO.getResMarker().getY());
		}


		boolean result = false;
		Long seqid = hospitalBO.getSeqId();

		if(hospitalBO.getIsNcmsFixedUnit()==null)
		{
			hospitalBO.setIsNcmsFixedUnit("0");
		}else
		{
			hospitalBO.setIsNcmsFixedUnit("1");
		}

		if(hospitalBO.getIsUebmiFixedUnit()==null)
		{
			hospitalBO.setIsUebmiFixedUnit("0");
		}else
		{
			hospitalBO.setIsUebmiFixedUnit("1");
		}

		hospitalBO.setStatus("1");



		Map<String, Object> params = new HashMap<String, Object>();



		if (seqid != null && seqid > 0L) { // --更新
			hospitalBO.setUpdateUserId(userInfo.getUserId());
			hospitalBO.setUpdateTime(new Date());
			hospitalBO.getResMarker().setResourcesId(seqid);
			resMarkerService.saveOrUpdateResMarker(hospitalBO.getResMarker());
			result = hospitalService.save(hospitalBO);


			//同步插入事件资源
			Map<String, Object> param = new HashMap<String, Object>();


			param.put("resTypeCode", typeCode);



			if (hospitalBO.getOrgCode() != null) {

				param.put("orgCode", hospitalBO.getOrgCode());


			}
			if (hospitalBO.getUpdateUserId() != null) {

				param.put("updateUserId", hospitalBO.getUpdateUserId());


			}
			if (hospitalBO.getHospitalName() != null && !hospitalBO.getHospitalName() .equals("")) {
				param.put("resName", hospitalBO.getHospitalName() );
			}

			if (hospitalBO.getSeqId() != null) {
				param.put("resTableId",hospitalBO.getSeqId() );
			}
			if (hospitalBO.getX() != null && !hospitalBO.getX().equals("")) {
				param.put("lng", hospitalBO.getX());
			}
			if (hospitalBO.getY() != null && !hospitalBO.getY().equals("")) {
				param.put("lat", hospitalBO.getY());
			}
			insertOrUpdateResult = zgResourceInfoService.update(param);


			resultMap.put("msg", Msg.EDIT.getMsg(result));


		} else { // --新增
			hospitalBO.setCreateUserId(userInfo.getUserId());
			hospitalBO.setCreateTime(new Date());
			result = hospitalService.insert(hospitalBO);
			result = hospitalBO.getSeqId() > 0;


			//同步插入事件资源
			Map<String, Object> param = new HashMap<String, Object>();


			param.put("resTypeCode", typeCode);



			if (hospitalBO.getOrgCode() != null) {

					param.put("orgCode", hospitalBO.getOrgCode());


			}
			if (hospitalBO.getCreateUserId() != null) {

				param.put("createUserId", hospitalBO.getCreateUserId());


			}
			if (hospitalBO.getHospitalName() != null && !hospitalBO.getHospitalName() .equals("")) {
				param.put("resName", hospitalBO.getHospitalName() );
			}

			if (hospitalBO.getSeqId() != null) {
				param.put("resTableId",hospitalBO.getSeqId() );
			}
			if (hospitalBO.getX() != null && !hospitalBO.getX().equals("")) {
				param.put("lng", hospitalBO.getX());
			}
			if (hospitalBO.getY() != null && !hospitalBO.getY().equals("")) {
				param.put("lat", hospitalBO.getY());
			}
			insertOrUpdateResult = zgResourceInfoService.insert(param);



			hospitalBO.getResMarker().setResourcesId(hospitalBO.getSeqId());
			resMarkerService.saveOrUpdateResMarker(hospitalBO.getResMarker());
			resultMap.put("msg", Msg.ADD.getMsg(result));

		}

		resultMap.put("result", result);
		resultMap.put("result1", insertOrUpdateResult);
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> del(HttpSession session, HttpServletRequest request,
								   @RequestParam(value = "seqid") Long seqid) {
		String typeCode="02071102";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean isSuccess = hospitalService.delete(seqid);
		List<Long> idList=new ArrayList<Long>();
		idList.add(seqid);
		Map<String, Object> deleteResult = new HashMap<String, Object>();

		for (Long id : idList) {

			HospitalBO hospitalBO = hospitalService.findHospitalById(id);
			if (hospitalBO != null) {

				//同步删除事件资源
				Map<String, Object> params = new HashMap<String, Object>();


				params.put("resTypeCode", typeCode);


				if (hospitalBO.getHospitalId() != null) {
					params.put("resTableId", hospitalBO.getHospitalId());
				}

				deleteResult = zgResourceInfoService.delete(params);


			}

		}

			Map<String, Object> resultMap = new HashMap<String, Object>();
		if (isSuccess) {
			resultMap.put("result", 1);
		} else {
			resultMap.put("result", 0);
		}

		resultMap.put("msg", Msg.DELETE.getMsg(isSuccess));
			resultMap.put("msg1",deleteResult);
		return resultMap;
	}


	@RequestMapping(value = "/getChartData")
	@ResponseBody
	public Map<String,Object> getChartData(HttpSession session, HttpServletRequest reques,String type,String orgCode){

		Map<String,Object> param = new HashMap<String, Object>();
		param.put("type",type);
		param.put("orgCode",orgCode);
		param.put("dataList",hospitalService.getHospitalCharts(param));
		return param;
	}
}
