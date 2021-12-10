package cn.ffcs.zhsq.event.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.ConstantValue;

@Controller
@RequestMapping("/zhsq/event/openEventDisposalController")
public class OpenEventDisposalController extends ZZBaseController {
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEvent4WorkflowService event4WorkflowService;

	/**
	 * 事件数据json接口
	 * eventType为todo、my、done、all接口替换，目前找不到使用方，后续若有问题，再调整 2019-07-11 zhangls
	 * @param session
	 * @param page
	 * @param rows
	 * @param content
	 * @param bigType
	 * @param type
	 * @param gridId
	 * @param source
	 * @param influenceDegree
	 * @param urgencyDegree
	 * @return json
	 */
	@ResponseBody
	@RequestMapping(value = "/openlistData", method = RequestMethod.GET)
	public String listData(
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "eventType") String eventType,
			@RequestParam(value = "keyWord", required = false) String keyWord,
			@ModelAttribute(value = "event") EventDisposal event,
			@RequestParam(value="callback", required=false) String callback,
			@RequestParam(value = "eventStatus", required = false) String eventStatus) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String path = "http://" + request.getServerName() + ":" + request.getServerPort() + "" + request.getContextPath();
		if (page <= 0)
			page = 1;
		
		if(!StringUtils.isBlank(eventStatus)){
			eventStatus = eventStatus.trim();
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userInfo.getUserId());
		
		if(!StringUtils.isBlank(keyWord)){
			keyWord = keyWord.trim();
			params.put("keyWord", keyWord);
		}
		if (!StringUtils.isBlank(event.getType())){
			params.put("type", event.getType());
		}else{
			if (!StringUtils.isBlank(event.getBigType())){
				params.put("type", event.getBigType());
			}
		}
		if (!StringUtils.isBlank(event.getSource())){
			params.put("source", event.getSource());
		}
		if (!StringUtils.isBlank(event.getInfluenceDegree())){
			params.put("influenceDegree", event.getInfluenceDegree());
		}
		if (!StringUtils.isBlank(event.getUrgencyDegree())){
			params.put("urgencyDegree", event.getUrgencyDegree());
		}
		if (event.getGridId() != null){
			params.put("gridId", event.getGridId());
		}
		if(!StringUtils.isBlank(event.getHappenTimeStart())){
			params.put("happenTimeStart", event.getHappenTimeStart());
		}
		if(!StringUtils.isBlank(event.getHappenTimeEnd())){
			params.put("happenTimeEnd", event.getHappenTimeEnd());
		}
		if(!StringUtils.isBlank(event.getCreateTimeStart())){
			params.put("createTimeStart", event.getCreateTimeStart());
		}
		if(!StringUtils.isBlank(event.getCreateTimeEnd())){
			params.put("createTimeEnd", event.getCreateTimeEnd());
		}
		params.put("status", eventStatus);
		params.put("orgId", userInfo.getOrgId());
		params.put("userId", userInfo.getUserId());
		params.put("userOrgCode", userInfo.getOrgCode());//字典转换使用
		
		if(eventType.equals("todo")){//--待办
			Map<String, Object> resultMap = new HashMap<String, Object>();
			List<Map<String, Object>> eventDisposalMapList = new ArrayList<Map<String, Object>>();
			EUDGPagination pagination = null;
			int total = 0;
			
			params.put("listType", 1);
			
			try {
				pagination = event4WorkflowService.findEventListPagination(page, rows, params);
				total = pagination.getTotal();
			} catch (ZhsqEventException e) {
				e.printStackTrace();
			}
			
			resultMap.put("total", total);
			
			if(total > 0) {
				List<Map<String, Object>> eventMapList = (List<Map<String, Object>>) pagination.getRows();
				Map<String, Object> eventDisposalMap = null;
				
				for(Map<String, Object> eventMap : eventMapList) {
					eventDisposalMap = new HashMap<String, Object>();
					
					eventDisposalMap.put("eventId", eventMap.get("eventId"));
					eventDisposalMap.put("eventName", eventMap.get("eventName"));
					eventDisposalMap.put("content", eventMap.get("content"));
					eventDisposalMap.put("urgencyDegree", eventMap.get("urgencyDegree"));
					eventDisposalMap.put("urgencyDegreeName", eventMap.get("urgencyDegreeName"));
					eventDisposalMap.put("statusName", eventMap.get("statusName"));
					eventDisposalMap.put("Status", eventMap.get("status"));
					eventDisposalMap.put("eventUrl", path + "/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=workStation&eventId=" + eventMap.get("eventId") + "&model=l&source=oldWorkPlatform");
					eventDisposalMap.put("type", eventMap.get("type"));
					eventDisposalMap.put("typeName", eventMap.get("typeName"));
					eventDisposalMap.put("happenTime", eventMap.get("happenTimeStr"));
					eventDisposalMap.put("eventClass", eventMap.get("eventClass"));
					
					eventDisposalMapList.add(eventDisposalMap);
				}
			}
			
			resultMap.put("rows", eventDisposalMapList);
			JSONArray data = JSONArray.fromObject(resultMap);
			String results = data.toString();
			if(callback!=null&&!"".equals(callback)){
				results = callback + "(" + results + ")";
			}
			return results;
		}else if(eventType.equals("my")){//--我发起的
			params.put("listType", 6);
			JSONObject jsonObj = new JSONObject();
			
			try {
				jsonObj = JSONObject.fromObject(event4WorkflowService.findEventListPagination(page, rows, params));
			} catch (ZhsqEventException e) {
				e.printStackTrace();
			}
			
			return jsonObj.toString();
//			return eventDisposalService.findMyEventWorkFlowPagination(page, rows, params);
		}else if(eventType.equals("done")){//--经办
			Map<String, Object> resultMap = new HashMap<String, Object>();
			List<Map<String, Object>> eventDisposalMapList = new ArrayList<Map<String, Object>>();
			EUDGPagination pagination = null;
			int total = 0;
			
			params.put("listType", 2);
			
			try {
				pagination = event4WorkflowService.findEventListPagination(page, rows, params);
				total = pagination.getTotal();
			} catch (ZhsqEventException e) {
				e.printStackTrace();
			}
			
			resultMap.put("total", total);
			
			if(total > 0) {
				List<Map<String, Object>> eventMapList = (List<Map<String, Object>>) pagination.getRows();
				Map<String, Object> eventDisposalMap = null;
				
				for(Map<String, Object> eventMap : eventMapList) {
					eventDisposalMap = new HashMap<String, Object>();
					
					eventDisposalMap.put("eventId", eventMap.get("eventId"));
					eventDisposalMap.put("eventName", eventMap.get("eventName"));
					eventDisposalMap.put("content", eventMap.get("content"));
					eventDisposalMap.put("urgencyDegree", eventMap.get("urgencyDegree"));
					eventDisposalMap.put("urgencyDegreeName", eventMap.get("urgencyDegreeName"));
					eventDisposalMap.put("statusName", eventMap.get("statusName"));
					eventDisposalMap.put("Status", eventMap.get("status"));
					eventDisposalMap.put("eventUrl", path + "/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=workStation&eventId=" + eventMap.get("eventId") + "&model=l&source=oldWorkPlatform");
					eventDisposalMap.put("type", eventMap.get("type"));
					eventDisposalMap.put("typeName", eventMap.get("typeName"));
					eventDisposalMap.put("happenTime", eventMap.get("happenTimeStr"));
					eventDisposalMap.put("eventClass", eventMap.get("eventClass"));
					
					eventDisposalMapList.add(eventDisposalMap);
				}
			}
			
			resultMap.put("rows", eventDisposalMapList);
			JSONArray data = JSONArray.fromObject(resultMap);
			String results = data.toString();
			if(callback!=null&&!"".equals(callback)){
				results = callback + "(" + results + ")";
			}
			return results;
//			return eventDisposalService.findDoneEventWorkFlowPagination(page, rows, params);
		}else if(eventType.equals("history")){//--我的归档
			
			JSONObject jsonObj = JSONObject.fromObject(eventDisposalService.findHistoryEventWorkFlowPagination(page, rows, params));
			return jsonObj.toString();
//			return eventDisposalService.findHistoryEventWorkFlowPagination(page, rows, params);
		}else if(eventType.equals("draft")){//--草稿
			params.put("statusList", null);//清空状态列表
			params.put("status", ConstantValue.EVENT_STATUS_DRAFT);
			params.put("creatorId", userInfo.getUserId());//过滤创建人员
			
			JSONObject jsonObj = JSONObject.fromObject(eventDisposalService.findEventDisposalPagination(page, rows, params));
			return jsonObj.toString();
//			return eventDisposalService.findEventDisposalPagination(page, rows, params);
		}else if(eventType.equals("all")){//--辖区所有
			params.put("listType", 5);
			params.put("infoOrgCode", this.getDefaultInfoOrgCode(session));
			JSONObject jsonObj = new JSONObject();
			
			try {
				jsonObj = JSONObject.fromObject(event4WorkflowService.findEventListPagination(page, rows, params));
			} catch (ZhsqEventException e) {
				e.printStackTrace();
			}
			
			return jsonObj.toString();
//			return eventDisposalService.findAllEventWorkFlowPagination(page, rows, params);
		}
		return null;
	}
	
	
}
