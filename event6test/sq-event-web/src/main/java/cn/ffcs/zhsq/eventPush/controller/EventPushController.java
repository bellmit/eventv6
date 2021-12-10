package cn.ffcs.zhsq.eventPush.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.eventPush.service.IEventPushService;
import cn.ffcs.zhsq.mybatis.domain.eventPush.EventPush;
import cn.ffcs.zhsq.utils.ConstantValue;

/**   
 * @Description: 事件推送模块控制器
 * @Author: os.wuzhj
 * @Date: 06-10 09:13:18
 * @Copyright: 2019 福富软件
 */ 
@Controller("eventPushController")
@RequestMapping("/zhsq/eventPush")
public class EventPushController {

	@Autowired
	private IEventPushService eventPushService; //注入事件推送模块服务
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
		return "/zzgl/eventPush/event_push_list.ftl";
	}

	/**
	 * 列表数据
	 * 
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, HttpSession session, ModelMap map,
		@RequestParam(value="page")int page, 
		@RequestParam(value="rows")int rows,
		@RequestParam(value="infoOrgCode",required=false)String infoOrgCode,
		@RequestParam(value="eventName",required=false)String eventName,
		@RequestParam(value="code",required=false)String code,
		@RequestParam(value="pushName",required=false)String pushName,
		@RequestParam(value="createTimeStart",required=false)String createTimeStart,
		@RequestParam(value="createTimeEnd",required=false)String createTimeEnd,
		Map<String, Object> params) {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);  //替换成本地常量
		if(StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = userInfo.getInfoOrgCodeStr();
		}
		
		params.put("userId", userInfo.getUserId());
		params.put("infoOrgCode", infoOrgCode);
		params.put("eventName", eventName);
		params.put("code", code);
		params.put("pushName", pushName);
		params.put("createTimeStart", createTimeStart);
		params.put("createTimeEnd", createTimeEnd);
		
		return eventPushService.searchList(page, rows, params);
	}
	
	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		Long[] ids,Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);  //替换成本地常量
		Map<String, Object> resultMap = new HashMap<>();
		Integer insert = 0;
		
		String re = "fail";
		params.put("eventIds", ids);
		List<EventPush> eventNames = eventPushService.searchByEventIds(params);
		
		if(eventNames !=null && eventNames.size() > 0) {
			resultMap.put("eventNames",eventNames);
		}else {
			params.put("creator", userInfo.getUserId());
			params.put("createName", userInfo.getUserName());
			params.put("ids", ids);
			insert = eventPushService.insert(params);
			if(insert > 0) {
				re = "success";
			}
			resultMap.put("rows", insert);
		}
		
		resultMap.put("result", re);
		
		return resultMap;
	}
	

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		EventPush bo) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION); //替换成本地常量
		bo.setUpdator(userInfo.getUserId());  //设置更新人
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = eventPushService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}

}