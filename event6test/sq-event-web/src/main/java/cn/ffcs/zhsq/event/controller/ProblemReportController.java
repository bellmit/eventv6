package cn.ffcs.zhsq.event.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExpand.service.IEventLabelRelaService;
import cn.ffcs.zhsq.eventExpand.service.IEventLabelService;
import cn.ffcs.zhsq.eventExpand.service.impl.EventLabelServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

@Controller
@RequestMapping("/zhsq/event/problemReportController")
public class ProblemReportController extends EventDisposalController {
	
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	
	@Autowired
	private IEventLabelService eventLabelService;
	
	@Autowired
	private IEventLabelRelaService eventLabelRelaService;
	
	
	

	
	/**
	 * 事件列表展示
	 * @param session
	 * @param eventType			列表类型
	 * @param model
	 * @param trigger				事件类别过滤功能配置触发条件，功能配置编码为：types
	 * @param eventAttrTrigger	事件基本属性过滤触发条件，功能配置编码为：EVENT_ATTRIBUTE
	 * @param extraParams		额外属性设置，格式为json字符串
	 * 							extraParams={isEnableDefaultCreatTime:true,exhibitParam:{isEditEventCreateTime:false,isShowEventType:false}}
	 * 							exhibitParam为列表页面元素展示、可编辑性等设置，格式为json字符串
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/listEvent")
	public String listEvent(HttpSession session,
			@RequestParam(value = "t") String eventType,
			@RequestParam(value = "model") String model,
			@RequestParam(value = "trigger", required = false) String trigger,
			@RequestParam(value = "eventAttrTrigger", required = false) String eventAttrTrigger,
			@RequestParam(value = "extraParams", required = false) String extraParams,
			ModelMap map) {
		String forward = super.listEvent(session, eventType, model, trigger, eventAttrTrigger, extraParams, map);
		
		//获取标签类型
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Map<String,Object> searchParams=new HashMap<String,Object>();
		searchParams.put("labelModel", ConstantValue.LABEL_EVENT_PROBLEM_REPORT);
		searchParams.put("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.put("eventLabelDict", eventLabelService.searchListByParam(searchParams));
		
		String replaceForward = forward.replace("/zzgl/event", "/zzgl/event/problemReport");
		return replaceForward;
	}

	
	
	
	
	@RequestMapping(value = "/detailEvent")
	public String detailEvent(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "instanceId", required=false) String instanceId,
			@RequestParam(value = "workFlowId", required=false) String workFlowId,
			@RequestParam(value = "taskId", required=false) String taskId, 
			@RequestParam(value = "position", required=false) String position, 
			@RequestParam(value = "eventType", required=false) String eventType,
			@RequestParam(value = "source", required=false) String source,
			@RequestParam(value = "model", required=false,defaultValue="l") String model,
			@RequestParam(value = "mapt", required=false) String mapt,
			@RequestParam(value = "showNotice", required=false) String showNotice,
			@RequestParam(value = "mapEngineType", required=false) String mapEngineType,
			@RequestParam Map<String, Object> extraParams,
			ModelMap map) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Pattern pattern = Pattern.compile("[0-9]*");
	    
		if (StringUtils.isBlank(instanceId) || "null".equals(instanceId)) {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("eventType", eventType);
			params.put("handlingUserId", userInfo.getUserId());
			params.put("handlingOrgId", userInfo.getOrgId());
			params.put("userOrgCode", userInfo.getOrgCode());
			
			instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(eventId, params);
		}
		if(StringUtils.isNotBlank(workFlowId) && !pattern.matcher(workFlowId.trim()).matches()){
			workFlowId = null;
		}
		return this.detailEventType(session, request, eventId, instanceId, workFlowId, position, eventType, source, model, mapt, showNotice, mapEngineType, extraParams, map);
	}

	/**
	 * 跳转事件详情页面
	 * @param session
	 * @param request
	 * @param eventId
	 * @param instanceId
	 * @param workFlowId
	 * @param position
	 * @param eventType
	 * @param source
	 * @param model
	 * @param mapt
	 * @param showNotice
	 * @param mapEngineType
	 * @param params
	 * 			isIgnoreStatus	是否忽略事件状态，true忽略；为true时，不可办理事件
	 * 			mapType			地图类型，为空时，mapt不为空时，使用mapt
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/detailEvent/{type}")
	public String detailEventType(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "instanceId", required=false) String instanceId,
			@RequestParam(value = "workFlowId", required=false) String workFlowId,
			@RequestParam(value = "position", required=false) String position, 
			@RequestParam(value = "eventType", required=false) String eventType,
			@RequestParam(value = "source", required=false) String source,
			@RequestParam(value = "model", required=false,defaultValue="l") String model,
			@RequestParam(value = "mapt", required=false) String mapt,
			@RequestParam(value = "showNotice", required=false) String showNotice,
			@RequestParam(value = "mapEngineType", required=false) String mapEngineType,
			@RequestParam Map<String, Object> params,
			ModelMap map) throws Exception {
		String forward = super.detailEventType(session, request, eventId, instanceId, workFlowId, position, eventType, source, model, mapt, showNotice, mapEngineType, params, map);
		Map<String,Object> eventLabelMap=new HashMap<String,Object>();
		try {
			Map<String,Object> searchLabelParams=new HashMap<String,Object>();
			searchLabelParams.put("labelModel", ConstantValue.LABEL_EVENT_PROBLEM_REPORT);
			List<Map<String, Object>> eventLabelList = eventLabelService.searchListByParam(searchLabelParams);
			for (Map<String, Object> labelMap : eventLabelList) {
				eventLabelMap.put(labelMap.get("labelId").toString(), labelMap.get("labelName").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(eventLabelMap.size()>0) {
			String eventLabelName="";
			String eventLabelIds="";
			try {
				List<Map<String, Object>> eventLabelRelaList = eventLabelRelaService.searchByEventId(eventId, new HashMap<String,Object>());
				for (int i = 0, j = eventLabelRelaList.size();i<j; i++) {
					String key=eventLabelRelaList.get(i).get("LABEL_ID").toString();
					if(eventLabelMap.containsKey(key)) {
						if(i==(j-1)) {
							eventLabelName+=eventLabelMap.get(key);
							eventLabelIds+=key;
						}else {
							eventLabelName+=eventLabelMap.get(key)+",";
							eventLabelIds+=key+",";
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			map.put("eventLabelName", eventLabelName);
			map.put("eventLabelIds", eventLabelIds);
		}
		
		String replaceForward = forward.replace("/zzgl/event", "/zzgl/event/problemReport");
		return replaceForward;
	}
	
}
