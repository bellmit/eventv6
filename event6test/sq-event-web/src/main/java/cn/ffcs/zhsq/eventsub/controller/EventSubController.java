package cn.ffcs.zhsq.eventsub.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.zhsq.eventsub.service.EventSubService;
import cn.ffcs.zhsq.mybatis.domain.eventsub.EventSub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.system.publicUtil.EUDGPagination;


/**   
 * @Description: event_na模块控制器
 * @Author: chenshikai
 * @Date: 07-03 14:26:31
 * @Copyright: 2020 福富软件
 */ 
@Controller("eventSubController")
@RequestMapping("/gmis/eventSub")
public class EventSubController {

	@Autowired
	private EventSubService eventSubService; //注入event_na模块服务
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		return "/gmis/eventSub/list_eventSub.ftl";
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

		EUDGPagination pagination = eventSubService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		EventSub bo = eventSubService.searchById(id);
		map.addAttribute("bo", bo);
		return "/gmis/eventSub/detail_eventSub.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		if (id != null) {
			EventSub bo = eventSubService.searchById(id);
			map.put("bo", bo);
		}
		return "/gmis/eventSub/form_eventSub.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		EventSub bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		if (bo.getTypeId() == null) { //新增
			Long id = eventSubService.insert(bo);
			if (id != null && id > 0) {
				result = "success";
			};
		} else { //修改
			boolean updateResult = eventSubService.update(bo);
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
		EventSub bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = true;
//		eventSubService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}

}