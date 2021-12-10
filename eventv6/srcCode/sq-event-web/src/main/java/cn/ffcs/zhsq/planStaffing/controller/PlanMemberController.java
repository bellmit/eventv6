package cn.ffcs.zhsq.planStaffing.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.domain.planStaffing.PlanMember;
import cn.ffcs.zhsq.planStaffing.IPlanMemberService;
import cn.ffcs.zhsq.utils.ConstantValue;
import net.sf.json.JSONObject;

/**   
 * @Description: 应急预案人员模块控制器
 * @Author: LINZHU
 * @Date: 04-28 16:17:29
 * @Copyright: 2021 福富软件
 */ 
@Controller("planMemberController")
@RequestMapping("/gmis/planMember")
public class PlanMemberController {

	@Autowired
	private IPlanMemberService planMemberService; //注入应急预案人员模块服务
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		return "/gmis/planMember/list_planMember.ftl";
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
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planConfigId", request.getParameter("planConfigId"));
		params.put("planRole", request.getParameter("planRole"));
		EUDGPagination pagination = planMemberService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		String id) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		PlanMember bo = planMemberService.searchById(id,userInfo.getOrgCode());
		map.addAttribute("bo", bo);
		return "/gmis/planMember/detail_planMember.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/add")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
			String id) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if (id != null) {
			PlanMember bo = planMemberService.searchById(id,userInfo.getOrgCode());
			map.put("bo", bo);
		}
		return "/gmis/planMember/form_planMember.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		PlanMember bo, @RequestBody JSONObject[] json) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		if (bo.getPlanMemberId() == null) { //新增
			String id = planMemberService.insert(bo);
			if (StringUtils.isNotBlank(id)) {
				result = "success";
			};
		} else { //修改
			boolean updateResult = planMemberService.update(bo);
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
		PlanMember bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = planMemberService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}

}