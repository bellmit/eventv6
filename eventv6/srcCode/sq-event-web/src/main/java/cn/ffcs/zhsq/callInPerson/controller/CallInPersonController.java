package cn.ffcs.zhsq.callInPerson.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.callInPerson.service.CallInPersonService;
import cn.ffcs.zhsq.mybatis.domain.callInPerson.CallInPerson;

/**   
 * @Description: 主叫人员管理模块控制器
 * @Author: linwd
 * @Date: 04-12 14:55:55
 * @Copyright: 2018 福富软件
 */ 
@Controller("callInPersonController")
@RequestMapping("/zhsq/callInPerson")
public class CallInPersonController {

	@Autowired
	private CallInPersonService callInPersonService; //注入主叫人员管理模块服务
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		return "ypms/callInPerson/list_callInPerson.ftl";
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
		params.put("cpId", request.getParameter("cpId"));
		params.put("cpName", request.getParameter("cpName"));
		params.put("cpMobile", request.getParameter("cpMobile"));
		params.put("status", request.getParameter("status"));
		EUDGPagination pagination = callInPersonService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	/*@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		CallInPerson bo = callInPersonService.searchById(id);
		map.addAttribute("bo", bo);
		return "/gmis/callInPerson/detail_callInPerson.ftl";
	}*/

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		if (id != null) {
			CallInPerson bo = callInPersonService.searchById(id);
			map.put("bo", bo);
		}
		return "/ypms/callInPerson/form_callInPerson.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		CallInPerson bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		Map<String, Object> params = new HashMap<>();
		if (bo.getCpId() == null) { //新增
			params.put("cpMobile",bo.getCpMobile());
			List<CallInPerson> callinPerson = callInPersonService.searchByParams(params);
			if (callinPerson == null || callinPerson.size() > 0) {
				bo.setCreater(userInfo.getUserId());
				Long id = callInPersonService.insert(bo);
				if (id != null && id > 0) {
					result = "success";
				};				
			} else {
				result = "mobileExist";
			}
		} else { //修改
			bo.setUpdater(userInfo.getUserId());
			boolean updateResult = callInPersonService.update(bo);
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
		CallInPerson bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		bo.setStatus("0");
		boolean delResult = callInPersonService.update(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}

}