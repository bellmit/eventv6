package cn.ffcs.zhsq.dispute.controller;

import cn.ffcs.shequ.commons.util.StringUtils;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.dispute.service.IMediationNaService;
import cn.ffcs.zhsq.mybatis.domain.dispute.MediationNa;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


/**   
 * @Description: 南安矛盾纠纷模块控制器
 * @Author: 黄文斌
 * @Date: 06-03 09:05:22
 * @Copyright: 2020 福富软件
 */ 
@Controller
@RequestMapping("/zhsq/event/mediationNa")
public class MediationNaController  extends ZZBaseController {

	@Autowired
	private IMediationNaService mediationNaService; //注入南安矛盾纠纷模块服务
	//功能配置
	@Autowired
	private IFunConfigurationService configurationService;


	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session,ModelMap map) {
		//UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.put("startGridId" ,defaultGridInfo.get(KEY_START_GRID_ID));
		map.put("startGridName" ,defaultGridInfo.get(KEY_START_GRID_NAME));
		map.put("InfoOrgCode" ,defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		return "/zzgl/dispute/mediation_na/index_dispute.ftl";
	}

	/**
	 * 矛盾纠纷类型列表页面
	 */
	@RequestMapping("/toDisputeList")
	public Object toDisputeList(HttpServletRequest request, HttpSession session,ModelMap map) {
		//UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String typecode = request.getParameter("typecode");
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.put("startGridId" ,defaultGridInfo.get(KEY_START_GRID_ID));
		map.put("startGridName" ,defaultGridInfo.get(KEY_START_GRID_NAME));
		map.put("InfoOrgCode" ,defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.put("typecode",typecode);
		return "/zzgl/dispute/mediation_na/dispute_list.ftl";
	}


	/**
	 * 跳转南安司法矛盾纠纷列表
	 */
	@RequestMapping("/toMdjfList")
	public Object toMdjfList(HttpServletRequest request, HttpSession session,ModelMap map) {
		String typecode = request.getParameter("typecode");
		String orgCode = request.getParameter("orgCode");
		String createTimeStart = request.getParameter("createTimeStart");
		String createTimeEnd = request.getParameter("createTimeEnd");
		String status = request.getParameter("status");
		String orgCodeName = null;
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.put("createTimeStart" ,createTimeStart);
		map.put("createTimeEnd" ,createTimeEnd);
		map.put("startGridId" ,defaultGridInfo.get(KEY_START_GRID_ID));
		map.put("startGridName" ,defaultGridInfo.get(KEY_START_GRID_NAME));
		map.put("InfoOrgCode" ,defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.put("status",status);
		map.put("typecode",typecode);
		map.put("orgCode",orgCode);
		if(StringUtils.isNotBlank(orgCode)){
			MixedGridInfo gridInfo = getGridInfoByOrgCode(orgCode);
			orgCodeName = gridInfo.getGridName();
			map.put("startGridId" ,gridInfo.getGridId());
		}
		map.put("orgCodeName",orgCodeName);
		return "/zzgl/dispute/mediation_na/mdjf_list.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public EUDGPagination gta(HttpSession session, HttpServletRequest request,
								   @RequestParam(value="page") int page,
								   @RequestParam(value="rows") int rows, ModelMap map,
								   @RequestParam(value="no",required = false) String no,
								   @RequestParam(value="status",required = false) String status,
								   @RequestParam(value="type_",required = false) String type_,
								   @RequestParam(value="infoOrgCode") String infoOrgCode,
							       @RequestParam(value="startDateStr",required = false) String startDateStr,
							       @RequestParam(value="endDateStr",required = false) String endDateStr
							  )
	{
		if(page <= 0) {
			page = ConstantValue.DEFAULT_PAGE_SIZE;
		}
		Map<String, Object> params = new HashMap<String, Object>();

		if(StringUtils.isNotBlank(status)){
			params.put("status", status);
		}
		if(!StringUtils.isBlank(no)){
			params.put("no", no);
		}

		if(StringUtils.isNotBlank(type_)){
			params.put("type_", type_);
		}
		params.put("infoOrgCode", infoOrgCode);
		if(StringUtils.isNotBlank(startDateStr)){
			params.put("createTimeStart",startDateStr);
		}
		if(StringUtils.isNotBlank(endDateStr)){
			params.put("createTimeEnd",endDateStr);
		}
		EUDGPagination pagination = mediationNaService.searchList(page, rows, params);
		return   pagination;
	}
	/**
	 * 详情页面
	 */
	@RequestMapping("/view")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
					   @RequestParam(value = "id") Long id,@RequestParam Map<String, Object> param) {
        //调用远程接口
		String url = configurationService.turnCodeToValue("MediationNa_Detail_URL","nanan_url", IFunConfigurationService.CFG_TYPE_FACT_VAL, null);
		String body = mediationNaService.detail(id,url);
		String gridname = mediationNaService.searchGrid(id);
		map.put("gridname",gridname);
		JSONObject json = JSONObject.parseObject(body);
		map.put("bo", json);
		JSONObject data = json.getJSONObject("row");

		JSONArray attachment = data.getJSONArray("attachment");
		map.put("attachment",attachment);
		JSONArray party = data.getJSONArray("party");
		map.put("party",party);

		JSONArray log = data.getJSONArray("log");
		map.put("log",log);
		if(CommonFunctions.isNotBlank(param, "pageType")) {
			return "/zzgl/dispute/mediation_na/show_dispute_"+param.get("pageType").toString()+".ftl";
		}
		return "/zzgl/dispute/mediation_na/show_dispute.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		if (id != null) {
			MediationNa bo = mediationNaService.searchById(id);
			map.put("bo", bo);
		}
		return "/gmis/mediationNa/mediationNa_form.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map) {
		List<MediationNa> csk  = new ArrayList<MediationNa>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long id = mediationNaService.insertAll(csk);
		System.out.println(id);
		return resultMap;
	}


	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		MediationNa bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = mediationNaService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 采集近12个月趋势
	 */

	@ResponseBody
	@RequestMapping(value = "/fetchAcquisitionTrendData4Jsonp")
	public String fetchAcquisitionTrendData4Jsonp(HttpSession session,
												  @RequestParam Map<String, Object> params,
												  @RequestParam(value="infoOrgCode",required = false) String infoOrgCode) {
		String jsonpcallback = "";
		if(StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = getDefaultInfoOrgCode(session);
		}
		params.put("infoOrgCode", infoOrgCode);
		List<Map<String, Object>> resultMap = mediationNaService.findAcquisitionTrendData(params);

		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}

		if(org.apache.commons.lang.StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}

		return jsonpcallback;
	}

	/**
	 * top5案件类型
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchHotEventData4Jsonp")
	public String fetchHotEventData4Jsonp(HttpSession session,
										  @RequestParam Map<String, Object> params,
										  @RequestParam(value="infoOrgCode",required = false) String infoOrgCode) {
		String jsonpcallback = "";
		if(StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = getDefaultInfoOrgCode(session);
		}
		params.put("infoOrgCode", infoOrgCode);

		List<Map<String, Object>> resultMap = mediationNaService.findHotEventData(params);

		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}

		if(org.apache.commons.lang.StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}

	/**
	 * 获取排查数，化解数，化解率
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchEventData4Jsonp")
	public String fetchEventData4Jsonp(HttpSession session,
									   @RequestParam Map<String, Object> params,
									   @RequestParam(value="infoOrgCode",required = false) String infoOrgCode) {
		String jsonpcallback = "";
		if(StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = ((UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION)).getOrgCode();
		}
		params.put("infoOrgCode", infoOrgCode);

		Map<String, Object> resultMap = mediationNaService.searchNum(params);

		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}

		if(org.apache.commons.lang.StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}


	/**
	 * 南安化解率排名
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchResolutioncount4Jsonp")
	public String fetchResolutioncount4Jsonp(HttpSession session,
											 @RequestParam Map<String, Object> params,
											 @RequestParam(value="infoOrgCode",required = false) String infoOrgCode) {
		String jsonpcallback = "";
		if(StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = getDefaultInfoOrgCode(session);
		}
		params.put("infoOrgCode", infoOrgCode);

		List<Map<String, Object>> resultMap = mediationNaService.searchPaiming(params);
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}


	/**
	 * 矛盾纠纷汇聚
	 */

	@ResponseBody
	@RequestMapping(value = "/DisputesConfluenceJsonp")
	public String DisputesConfluenceJsonp(HttpSession session,
											 @RequestParam Map<String, Object> params,
											 @RequestParam(value="infoOrgCode",required = false) String infoOrgCode,
										     @RequestParam(value="type_",required = false) String type_,
										     @RequestParam(value="no",required = false) String no
										  ) {
		String jsonpcallback = "";
		if(StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = getDefaultInfoOrgCode(session);
		}
		params.put("infoOrgCode", infoOrgCode);
		if(StringUtils.isNotBlank(type_)){
			params.put("type_", type_);
		}
		if(StringUtils.isNotBlank(no)){
			params.put("no", no);
		}
		Map<String,Object> map = new HashMap<>();
		List<Map<String, Object>> resultMap = mediationNaService.DisputesConfluence(params);
		map.put("data",resultMap);

		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}

		if(org.apache.commons.lang.StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(map) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(map);
		}
		return jsonpcallback;
	}

	/**
	 * 矛盾纠纷撒点查询
	 * 
	 */

	@ResponseBody
	@RequestMapping(value = "/DisputesDotsJsonp")
	public String DisputesDotsJsonp(HttpSession session,
										  @RequestParam Map<String, Object> params,
										  @RequestParam(value="infoOrgCode",required = false) String infoOrgCode,
									      @RequestParam(value="page") int page,
									      @RequestParam(value="rows") int rows,
									      @RequestParam(value="type_",required = false) String type_,
									      @RequestParam(value="no",required = false) String no
									) {
		String jsonpcallback = "";
		if(StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = getDefaultInfoOrgCode(session);
		}
		params.put("infoOrgCode", infoOrgCode);
		if(StringUtils.isNotBlank(type_)){
			params.put("type_", type_);
		}
		if(StringUtils.isNotBlank(no)){
			params.put("no", no);
		}
		if(page <= 0) {
			page = ConstantValue.DEFAULT_PAGE_SIZE;
		}

		EUDGPagination resultMap = mediationNaService.DisputesDotsJsonp(page, rows, params);


		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}

		if(org.apache.commons.lang.StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}


	/**
	 * 矛盾纠纷状态类型数量统计
	 */

	@ResponseBody
	@RequestMapping(value = "/fetchEventStatus4Jsonp")
	public String fetchEventStatus4Jsonp(HttpSession session,
											 @RequestParam Map<String, Object> params,
											 @RequestParam(value="infoOrgCode",required = false) String infoOrgCode) {
		String jsonpcallback = "";
		if(StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = getDefaultInfoOrgCode(session);
		}
		params.put("infoOrgCode", infoOrgCode);

		List<Map<String, Object>> resultMap = mediationNaService.findStatusData(params);

		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}

		if(org.apache.commons.lang.StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}

	/**
	 * 矛盾纠七对接类型数量统计
	 */

	@ResponseBody
	@RequestMapping(value = "/fetchStatus4Jsonp")
	public String fetchStatus4Jsonp(HttpSession session,
										 @RequestParam Map<String, Object> params,
										 @RequestParam(value="infoOrgCode",required = false) String infoOrgCode) {
		String jsonpcallback = "";
		if(StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = getDefaultInfoOrgCode(session);
		}
		params.put("infoOrgCode", infoOrgCode);

		List<Map<String, Object>> resultMap = mediationNaService.findStatusNum(params);
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
	 * 矛盾纠纷类型数量统计
	 */

	@ResponseBody
	@RequestMapping(value = "/fetchType4Jsonp")
	public String fetchType4Jsonp(HttpSession session,
									@RequestParam Map<String, Object> params,
									@RequestParam(value="infoOrgCode",required = false) String infoOrgCode) {
		String jsonpcallback = "";
		if(StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = getDefaultInfoOrgCode(session);
		}
		params.put("infoOrgCode", infoOrgCode);

		Map<String, Object> resultMap = mediationNaService.getTypeNum(params);
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
	 * 矛盾纠纷类型数量统计,获取两个时间段的年份/月份/天
	 */

	@ResponseBody
	@RequestMapping(value = "/fetchDataTime4Jsonp")
	public String fetchDataTime4Jsonp(HttpSession session,
								  @RequestParam Map<String, Object> params,
								  @RequestParam(value="infoOrgCode",required = false) String infoOrgCode) {
		String jsonpcallback = "";
		if(StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = getDefaultInfoOrgCode(session);
		}
		params.put("infoOrgCode", infoOrgCode);

		List<Map<String, Object>> resultMap = mediationNaService.getDataTime(params);
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
	 * 矛盾纠纷 区间每日汇总
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchEventDay4Jsonp")
	public String fetchEventDay4Jsonp(HttpSession session,
									@RequestParam Map<String, Object> params,
									@RequestParam(value="infoOrgCode",required = false) String infoOrgCode) {
		String jsonpcallback = "";
		if(StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = getDefaultInfoOrgCode(session);
		}
		params.put("infoOrgCode", infoOrgCode);

		List<Map<String, Object>> resultMap = mediationNaService.fetchEventDay4Jsonp(params);
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString() + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}

	/**
	 * 列表数据jsonp接口
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchEventListDataJsonp")
	public String fetchEventListDataJsonp(HttpSession session,
										  @RequestParam(value="page") int page,
										  @RequestParam(value="rows") int rows,
										  @RequestParam Map<String, Object> params) {
		String jsonpcallback = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(page <= 0) {
			page = ConstantValue.DEFAULT_PAGE_SIZE;
		}
		if(CommonFunctions.isBlank(params, "status")) {
			params.put("status", 0);
		}
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("infoOrgCode", getDefaultInfoOrgCode(session));
		}
		EUDGPagination pagination = mediationNaService.searchList(page, rows, params);

		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		resultMap.put("total", pagination.getTotal());
		resultMap.put("list", pagination.getRows());
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		return jsonpcallback;
	}

}