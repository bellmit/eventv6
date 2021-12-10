package cn.ffcs.zhsq.event.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.shequ.web.interceptor.csrf.VerifyCSRFToken;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 对外提供事件数据
 * @author zhangls
 *
 */
@Controller
@RequestMapping("/zhsq/event/eventDisposal4OuterController")
public class EventDisposal4OuterController extends ZZBaseController {
	@Autowired
	private IEvent4WorkflowService event4WorkflowService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	/**
	 * 获取辖区所有事件列表数据
	 * @param session
	 * @param page		页数
	 * @param rows		每页记录数
	 * @param params
	 * 			orgCode			组织域编码
	 * 			infoOrgCode		地域编码
	 * 			evaLevelList	评价等级 01 不满意；02 满意；03 非常满意；04 基本满意
	 * 			handleDateFlag	时限状态 1 正常；2 将到期；3 已过期
	 * 			jsonpcallback	jsop回调
	 * 			eventType		列表类别 all 辖区所有；todo 待办列表；默认为all
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchEventData4Jsonp")
	public String fetchEventData4Jsonp(
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String jsonpcallback = "", eventType = "all";
		EUDGPagination result = new EUDGPagination();
		int listType = 0;
		
		if(CommonFunctions.isNotBlank(params, "eventType")) {
			eventType = params.get("eventType").toString();
		}
		
		if("todo".equals(eventType)) {//待办
			listType = 1;
		} else if("all".equals(eventType)) {//辖区所有
			listType = 5;
		}
		
		if(CommonFunctions.isBlank(params, "listType")) {
			params.put("listType", listType);
		}
		
		fetchDefaultValue(session, params);
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		try {
			result = event4WorkflowService.findEventListPagination(page, rows, params);
		} catch (Exception e) {
			//resultMap.put("msgWrong", e.getMessage());
            resultMap.put("msgWrong", "接口查询失败！");
			e.printStackTrace();
		}
		
		resultMap.put("total", result.getTotal());
		resultMap.put("list", result.getRows());
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	/**
	 * 获取辖区所有事件记录统计值
	 * 该方法可被fetchDataCount4Jsonp替换，其中listType为5
	 * @param session
	 * @param params
	 * 			orgCode			组织编码
	 * 			infoOrgCode		地域编码
	 * 			evaLevelList	评价等级 01 不满意；02 满意；03 非常满意；04 基本满意
	 * 			handleDateFlag	时限状态 1 正常；2 将到期；3 已过期
	 * 			jsonpcallback	jsonp回调
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping(value = "/fetchEventCount4Jsonp")
	public String fetchEventCount4Jsonp(
			HttpSession session,
			@RequestParam Map<String, Object> params) {
		params.put("listType", 5);

		return this.fetchDataCount4Jsonp(session, params);
	}
	
	/**
	 * 获取待办事件记录统计值
	 * 该方法可被fetchDataCount4Jsonp替换，其中listType为1
	 * @param session
	 * @param params
	 * 			trigger	事件类别过滤触发条件，功能配置编码为：types
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping(value = "/fetchTodoEventCount4Jsonp")
	public String fetchTodoEventCount4Jsonp(
			HttpSession session,
			@RequestParam Map<String, Object> params) {
		params.put("listType", 1);
		
		return this.fetchDataCount4Jsonp(session, params);
	}
	
	/**
	 * 获取事件记录总数
	 * @param session
	 * @param params
	 * 			listType		事件列表类型
	 * 			orgCode			组织编码
	 * 			infoOrgCode		地域编码
	 * 			evaLevelList	评价等级 01 不满意；02 满意；03 非常满意；04 基本满意
	 * 			handleDateFlag	时限状态 1 正常；2 将到期；3 已过期
	 * 			trigger			事件类别过滤触发条件，功能配置编码为：types
	 * 			
	 * 			jsonpcallback	jsonp回调
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchDataCount4Jsonp")
	public String fetchDataCount4Jsonp(
			HttpSession session,
			@RequestParam Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String jsonpcallback = "";
		int total = 0;
		
		fetchDefaultValue(session, params);
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		try {
			total = event4WorkflowService.findEventCount(params);
		} catch (Exception e) {
			//resultMap.put("msgWrong", e.getMessage());
            resultMap.put("msgWrong", "接口查询失败！");
            e.printStackTrace();
		}
		
		resultMap.put("total", total);
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	/**
	 * 设定默认值
	 * @param session
	 * @param params
	 * 			orgId		组织id
	 * 			orgCode		组织编码
	 * 			userId		用户id
	 * 			infoOrgCode	地域编码
	 * 			listType	列表类型
	 * 				1 待办；2 经办；3 辖区所有(支持办理人员查询)；
	 * 				4 归档列表；5 辖区所有；
	 * 				6 我发起的；7 我的关注；8 辖区内需要督办；
	 */
	private void fetchDefaultValue(HttpSession session, Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		int listType = 0;
		boolean isCapDefaultUser = true, isCapDefaultInfoOrgCode = true;
		
		
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if(CommonFunctions.isBlank(params, "orgCode")) {
			params.put("orgCode", userInfo.getOrgCode());
		}
		
		switch(listType) {
			case 1: {}//待办事件列表
			case 2: {}//经办事件列表
			case 6: {}//我发起的事件列表
			case 7: {//我的关注列表
				isCapDefaultInfoOrgCode = false;
				break;
			}
			case 3: {//辖区所有(支持办理人员查询)
				isCapDefaultUser = false;
				break;
			}
		}
		
		if(isCapDefaultUser) {
			if(CommonFunctions.isBlank(params, "orgId")) {
				params.put("orgId", userInfo.getOrgId());
			}
			if(CommonFunctions.isBlank(params, "userId")) {
				params.put("userId", userInfo.getUserId());
			}
		}
		
		try {
			if(CommonFunctions.isNotBlank(params, "isCapDefaultInfoOrgCode")||CommonFunctions.isNotBlank(params, "gridId")) {
				isCapDefaultInfoOrgCode=Boolean.valueOf(params.get("isCapDefaultInfoOrgCode").toString());
			}
		} catch (Exception e1) {
		}
		
		if(isCapDefaultInfoOrgCode) {
			if(CommonFunctions.isBlank(params, "infoOrgCode")) {
				params.put("infoOrgCode", this.getDefaultInfoOrgCode(session));
			}
		}
	}
	
	
	/**
	 * 获取事件详情信息
	 * @param session
	 * @param params
	 * 			isCapMapInfo	是否获取地图信息，Boolean类型，true为获取，默认为false
	 * 			isCapExtendInfo	是否获取事件扩展信息，Boolean类型，true为获取，默认为false
	 * 			isCapEntryMatter是否获取入格事项相关信息，Boolean类型，true为获取，默认为false
	 * 							为true时，会强制将isCapExtendInfo设置为true
	 * 			mapType			事件地图类型，String类型，isCapMapInfo为true时生效
	 * 			userOrgCode		组织编码，String类型，不为空时进行事件字典相关属性转换
	 * 			isIgnoreStatus	是否忽略事件状态，Boolean类型，true为忽略；默认为false
	 * @return
	 * 		event			事件对象，类型为cn.ffcs.zhsq.mybatis.domain.event.EventDisposal
	 * 		eventExtend		isCapExtendInfo为true时获取，事件扩展信息，类型为Map<String, Object>
	 * 		entryMatterCode	入格事项编码
	 * 		entryMatter		isCapEntryMatter为true时获取，入格事项信息，类型为为Map<String, Object>
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchEventDetail4Jsonp")
	@VerifyCSRFToken(onlyReferer = true)
	public String fetchEventDetail4Jsonp(
			HttpSession session,
			@RequestParam Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String jsonpcallback = "";
		Long eventId = -1L;
		
		if(CommonFunctions.isNotBlank(params, "eventId")) {
			try {
				eventId = Long.valueOf(params.get("eventId").toString());
			} catch(NumberFormatException e) {
				resultMap.put("msgWrong", "事件id【eventId】：【" + params.get("eventId") + "】不是有效的数值！");
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		if(eventId != null && eventId > 0) {
			try {
				resultMap.putAll(eventDisposalService.findEventByMap(eventId, params));
			} catch (Exception e) {
				resultMap.put("msgWrong", e.getMessage());
				
			}
		} else if(CommonFunctions.isBlank(resultMap, "msgWrong")) {
			resultMap.put("msgWrong", "【eventId】：【" + eventId + "】不是有效的事件id！");
		}
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	
}
