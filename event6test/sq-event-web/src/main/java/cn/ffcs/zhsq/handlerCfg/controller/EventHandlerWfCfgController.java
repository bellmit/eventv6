package cn.ffcs.zhsq.handlerCfg.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.workflow.om.Workflow;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.handlerCfg.service.IEventHandlerWfCfgService;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfCfgPO;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**
 * 业务配置
 * @author zhangls
 *
 */
@Controller
@RequestMapping("/zhsq/event/eventHandlerWfCfg")
public class EventHandlerWfCfgController extends ZZBaseController {
	@Autowired
	private IEventHandlerWfCfgService eventHandlerWfCfgService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	/**
	 * 跳转业务配置页面
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toList(HttpSession session, ModelMap map) {
		return "/zzgl/handlerCfg/list_wf_cfg.ftl";
	}
	
	/**
	 * 跳转新增页面
	 * @param session
	 * @param wfcId		主键
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(HttpSession session, 
			@RequestParam(value = "wfcId", required = false) Long wfcId,
			ModelMap map) {
		EventHandlerWfCfgPO handlerWfCfg = null;
		
		if(wfcId != null && wfcId > 0) {
			handlerWfCfg = eventHandlerWfCfgService.findHandlerWfCfgById(wfcId);
		}
		
		if(handlerWfCfg == null) {
			handlerWfCfg = new EventHandlerWfCfgPO();
		}
		
		List<Map<String, Object>> bizTypeMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> bizTypeMap = null;
		EventHandlerWfCfgPO.BIZ_TYPE[] bizTypes = EventHandlerWfCfgPO.BIZ_TYPE.values();
		for(EventHandlerWfCfgPO.BIZ_TYPE bizType : bizTypes) {
			bizTypeMap = new HashMap<String, Object>();
			bizTypeMap.put("name", bizType.getName());
			bizTypeMap.put("value", bizType.getCode());
			bizTypeMapList.add(bizTypeMap);
		}
		map.addAttribute("bizTypeMapList", bizTypeMapList);
		
		map.addAttribute("handlerWfCfg", handlerWfCfg);
		
		return "/zzgl/handlerCfg/add_wf_cfg.ftl";
	}
	
	/**
	 * 新增/编辑业务配置信息
	 * @param session
	 * @param wfCfg
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveWfCfg", method = RequestMethod.POST)
	public ResultObj saveWfCfg(
			HttpSession session,
			@ModelAttribute(value = "wfCfg") EventHandlerWfCfgPO wfCfg, 
			ModelMap map) {
		Long wfcId = wfCfg.getWfcId();
		boolean result = false;
		ResultObj resultObj = null;
		Msg msg = null;
		
		try {
			if(wfcId != null && wfcId > 0) {
				msg = Msg.EDIT;
				
				result = eventHandlerWfCfgService.updateHandlerWfCfgById(wfCfg);
			} else {
				msg = Msg.ADD;
				
				result = eventHandlerWfCfgService.saveHandlerWfCfg(wfCfg) > 0;
			}
			
			resultObj = msg.getResult(result);
		} catch(Exception e) {
			e.printStackTrace();
			resultObj = msg.getResult(e);
		}
		
		return resultObj;
	}
	
	/**
	 * 依据组件删除业务配置信息
	 * @param session
	 * @param wfcId		业务配置id
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delWfCfg", method = RequestMethod.POST)
	public ResultObj delWfCfg(
			HttpSession session,
			@RequestParam(value = "wfcId") Long wfcId,
			ModelMap map) {
		boolean result = false;
		ResultObj resultObj = null;
		
		if(wfcId != null && wfcId > 0) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			
			result = eventHandlerWfCfgService.delHandlerWfCfgById(wfcId, userInfo.getUserId());
		}
		
		resultObj = Msg.DELETE.getResult(result);
		
		return resultObj;
	}
	
	/**
	 * 加载业务配置信息
	 * @param session
	 * @param page		页码
	 * @param rows		每页显示记录条数
	 * @param params
	 * 			
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listWfCfgData", method = RequestMethod.POST)
	public EUDGPagination listWfCfgData(HttpSession session, 
			@RequestParam(value = "page") int page, 
			@RequestParam(value = "rows") int rows, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		
		return eventHandlerWfCfgService.findHandlerWfCfgPagination(page, rows, params);
	}
	
	/**
	 * 依据bizType获取对应的业务配置信息
	 * @param session
	 * @param bizType	业务类型 00 工作流
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchWfCfgList", method = RequestMethod.POST)
	public List<Map<String, Object>> fetchWfCfgList(HttpSession session, 
			@RequestParam(value = "bizType") String bizType,
			ModelMap map) {
		List<Map<String, Object>> wfCfgList = new ArrayList<Map<String, Object>>();
		Map<String, Object> wfCfgMap = null;
		
		if(EventHandlerWfCfgPO.BIZ_TYPE.WORKFLOW.getCode().equals(bizType)) {//工作流配置
			List<Workflow> workflowList = eventDisposalWorkflowService.queryWorkflows(null);
			if(workflowList != null) {
				String wfCfgName = "", respectOrgName = "";
				
				for(Workflow workflow : workflowList) {
					wfCfgMap = new HashMap<String, Object>();
					wfCfgName = workflow.getFlowName();
					respectOrgName = workflow.getRespectOrgName();
					
					if(StringUtils.isNotBlank(respectOrgName)) {
						wfCfgName += "-" + respectOrgName;
					}
					
					wfCfgMap.put("name", wfCfgName);
					wfCfgMap.put("value", workflow.getWorkFlowId());
					
					wfCfgList.add(wfCfgMap);
				}
			}
		}
		
		return wfCfgList;
	}
}
