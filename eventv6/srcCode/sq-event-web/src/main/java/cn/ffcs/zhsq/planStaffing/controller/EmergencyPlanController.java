package cn.ffcs.zhsq.planStaffing.controller;

import java.util.HashMap;
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

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.domain.planStaffing.EmergencyPlan;
import cn.ffcs.zhsq.planStaffing.IEmergencyPlanService;
import cn.ffcs.zhsq.utils.ConstantValue;

/** 
* @Description: 应急预案管理模块控制器
* @Author: LINZHU
* @Date: 04-22 16:31:15
* @Copyright: 2021 福富软件
*/ 
@Controller("emergencyPlanController")
@RequestMapping("/zhsq/zzgl/emergencyPlan")
public class EmergencyPlanController {

	@Autowired
	private IEmergencyPlanService emergencyPlanService; //注入应急预案管理模块服务

	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		
		return "/zzgl/planStaffing/list_emergencyPlan.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
		int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planName", request.getParameter("planName"));
		params.put("planType", request.getParameter("planType"));
		params.put("isValid", request.getParameter("isValid"));
		params.put("regionCode", request.getParameter("regionCode"));
		params.put("userOrgCode", userInfo.getOrgCode());
		EUDGPagination pagination = emergencyPlanService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		String id) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		EmergencyPlan bo = emergencyPlanService.searchById(id,userInfo.getOrgCode());
		map.addAttribute("bo", bo);
		return "/zzgl/planStaffing/detail_emergencyPlan.ftl";
	}
  
	/**
	 * 表单页面
	 */
	@RequestMapping("/add")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
			String id) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if (id != null) {
			EmergencyPlan bo = emergencyPlanService.searchById(id,userInfo.getOrgCode());
			map.put("bo", bo);
		}
		return "/zzgl/planStaffing/add_emergencyPlan.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		EmergencyPlan bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		if (StringUtils.isBlank(bo.getPlanId())) { //新增
			String id = emergencyPlanService.insert(bo);
			if (id != null) {
				result = "success";
			};
		} else { //修改
			boolean updateResult = emergencyPlanService.update(bo);
			if (updateResult) {
				result = "success";
			}
		}
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		EmergencyPlan bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = emergencyPlanService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	
	/**
	 * 根据条件查询条数
	 * @param request
	 * @param session
	 * @param map
	 * @param params
	 * regionCodeAll  完全匹配      planType：类型   excludeId:排查的id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/countList")
	public long countList(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam Map<String, Object> params) {
		return emergencyPlanService.countList(params);
	}

}