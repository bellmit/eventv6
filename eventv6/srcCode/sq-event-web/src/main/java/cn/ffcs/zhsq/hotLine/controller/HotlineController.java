package cn.ffcs.zhsq.hotLine.controller;

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

import cn.ffcs.json.utils.JsonUtil;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.hotLine.service.HotlineService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.JsonUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**   
 * @Description: T_HOTLINE模块控制器
 * @Author: wuxq
 * @Date: 09-08 17:02:47
 * @Copyright: 2020 福富软件
 */ 
@Controller("hotlineController")
@RequestMapping("/zhsq/hotLine")
public class HotlineController extends ZZBaseController {

	@Autowired
	private HotlineService hotlineService; //注入T_HOTLINE模块服务
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		return "/zzgl/hotLine/list_hotline.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
		int page, int rows,@RequestParam Map<String, Object> params) {

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		if(CommonFunctions.isBlank(params, "defaultInfoOrgCode")) {
			params.put("defaultInfoOrgCode", this.getDefaultInfoOrgCode(session));
		}
		params.put("userOrgCode", userInfo.getOrgCode());
		
		EUDGPagination pagination = hotlineService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value = "caseNo", required = true) String caseNo) {
		Map<String, Object> bo = hotlineService.searchByCaseNo(caseNo);
		map.addAttribute("bo", bo);
		return "/zzgl/hotLine/detail_hotline.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value = "caseNo", required=false) String caseNo) {
		if (StringUtils.isNotBlank(caseNo)) {
			Map<String, Object> bo = hotlineService.searchByCaseNo(caseNo);
			map.addAttribute("bo", bo);
		}
		return "/zzgl/hotLine/form_hotline.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam Map<String, Object> hotLine) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		String caseNo = "";
		
		if(CommonFunctions.isNotBlank(hotLine, "caseNo")) {
			caseNo = hotLine.get("caseNo").toString();
		}
		
		if (StringUtils.isNotBlank(caseNo)) {//更新
			 if (hotlineService.update(hotLine)) {
				 result = "true";
			 }
		}else {//新增
			if (hotlineService.insert(hotLine)) {
				 result = "true";
			 }
		}
		
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/saveByList")
	public void saveByList(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam Map<String, Object> hotLine) {
		
		Map<String, Object> map11 = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		map11.put("caseNo", "2020091105");
		map11.put("contactUser", "Y");
		map11.put("contactTel", "Y");
		map11.put("tel", "Y");
		map11.put("sex", "Y");
		map11.put("age", "Y");
		map11.put("sources", "Y");
		map11.put("appealType", "Y");
		map11.put("callTime", "2020-09-10 12:12:21");
		map11.put("title", "Y");
		map11.put("content", "Y");
		map11.put("email", "Y");
		map11.put("isWeb", "Y");
		map11.put("occurred", "Y");
		map11.put("address", "Y");
		map11.put("underType", "Y");
		map11.put("urgency", "Y");
		map11.put("appealPublic", "Y");
		map11.put("returnType", "Y");
		
		list.add(map11);
		map11 = new HashMap<String, Object>();
		map11.put("caseNo", "2020091106");
		map11.put("contactUser", "L");
		map11.put("contactTel", "L");
		map11.put("tel", "L");
		map11.put("sex", "L");
		map11.put("age", "L");
		map11.put("sources", "L");
		map11.put("appealType", "L");
		map11.put("callTime", "2020-09-01 12:12:21");
		map11.put("title", "L");
		map11.put("content", "L");
		map11.put("email", "L");
		map11.put("isWeb", "L");
		map11.put("occurred", "L");
		map11.put("address", "L");
		map11.put("underType", "L");
		map11.put("urgency", "L");
		map11.put("appealPublic", "L");
		map11.put("returnType", "L");
		list.add(map11);
		Map<String, Object> map12 = new HashMap<String, Object>();
		map12.put("dataList", JsonUtils.listToJson(list));
		
		hotlineService.insertAll(map12);
		
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam Map<String, Object> bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = hotlineService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}

}