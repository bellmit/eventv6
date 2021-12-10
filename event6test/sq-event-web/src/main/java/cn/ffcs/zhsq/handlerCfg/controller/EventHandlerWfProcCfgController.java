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

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.handlerCfg.service.IEventHandlerWfActorCfgService;
import cn.ffcs.zhsq.handlerCfg.service.IEventHandlerWfCfgService;
import cn.ffcs.zhsq.handlerCfg.service.IEventHandlerWfProcCfgService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfActorCfgPO;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfCfgPO;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfProcCfgPO;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**
 * 流程环节配置
 * @author zhangls
 *
 */
@Controller
@RequestMapping("/zhsq/event/eventHandlerWfProcCfg")
public class EventHandlerWfProcCfgController extends ZZBaseController {
	@Autowired
	private IEventHandlerWfCfgService eventHandlerWfCfgService;
	
	@Autowired
	private IEventHandlerWfProcCfgService eventHandlerWfCProcfgService;
	
	@Autowired
	private IEventHandlerWfActorCfgService eventHandlerWfActorCfgService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;
	
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	
	/**
	 * 跳转流程环节配置列表
	 * @param session
	 * @param wfcId	业务配置id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toList(HttpSession session, 
			@RequestParam(value = "wfcId") Long wfcId,
			ModelMap map) {
		EventHandlerWfCfgPO handlerWfCfg = eventHandlerWfCfgService.findHandlerWfCfgById(wfcId);
		
		if(handlerWfCfg == null) {
			handlerWfCfg = new EventHandlerWfCfgPO();
		} else {
			String regionCode = handlerWfCfg.getRegionCode();
			
			if(StringUtils.isNotBlank(regionCode)) {
				MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(regionCode);
				if(gridInfo != null) {
					map.addAttribute("startGridId", gridInfo.getGridId());
				}
			}
		}
		
		map.addAttribute("handlerWfCfg", handlerWfCfg);
		map.addAttribute("nodeCode", "__"+INodeCodeHandler.OPERATE_GLOBAL+"___");//人工构建nodeCode，人员选择框使用
		map.addAttribute("eventTypePcode", ConstantValue.BIG_TYPE_PCODE);
		
		return "/zzgl/handlerCfg/list_wf_proc_cfg.ftl";
	}
	
	/**
	 * 跳转流程环节配置列表(测试版本)
	 * @param session
	 * @param wfcId	业务配置id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toTest")
	public String toTest(HttpSession session, 
			@RequestParam(value = "wfcId") Long wfcId,
			ModelMap map) {
		
		this.toList(session, wfcId, map);
		
		return "/zzgl/handlerCfg/list_wf_proc_cfg_test.ftl";
	}
	
	/**
	 * 跳转新增/编辑流程环节配置页面
	 * @param session
	 * @param wfcId		业务配置id
	 * @param wfpcId	流程环节配置id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(HttpSession session, 
			@RequestParam(value = "wfcId") Long wfcId,
			@RequestParam(value = "wfpcId", required = false) Long wfpcId,
			ModelMap map) {
		EventHandlerWfProcCfgPO wfProcCfg = null;
		
		if(wfpcId != null && wfpcId > 0) {
			wfProcCfg = eventHandlerWfCProcfgService.findHandlerWfProcCfgById(wfpcId);
		}
		
		if(wfProcCfg == null) {
			wfProcCfg = new EventHandlerWfProcCfgPO();
		}
		
		EventHandlerWfCfgPO handlerWfCfg = eventHandlerWfCfgService.findHandlerWfCfgById(wfcId);
		
		if(handlerWfCfg != null) {
			String regionCode = handlerWfCfg.getRegionCode();
			
			if(StringUtils.isNotBlank(regionCode)) {
				MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(regionCode);
				if(gridInfo != null) {
					map.addAttribute("startGridId", gridInfo.getGridId());
				}
			}
		}
		
		map.addAttribute("wfProcCfg", wfProcCfg);
		map.addAttribute("wfcId", wfcId);
		map.addAttribute("nodeCode", "__"+INodeCodeHandler.OPERATE_GLOBAL+"___");//人工构建nodeCode，人员选择框使用
		map.addAttribute("eventTypePcode", ConstantValue.BIG_TYPE_PCODE);
		
		return "/zzgl/handlerCfg/add_wf_proc_cfg.ftl";
	}
	
	/**
	 * 跳转环节信息配置详情页面
	 * @param session
	 * @param wfpcId	环节信息配置id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "toDetail")
	public String toDetail(HttpSession session, 
			@RequestParam(value = "wfpcId") Long wfpcId,
			ModelMap map) {
		EventHandlerWfProcCfgPO wfProcCfg = null;
		
		if(wfpcId != null && wfpcId > 0) {
			wfProcCfg = eventHandlerWfCProcfgService.findHandlerWfProcCfgById(wfpcId);
		}
		
		if(wfProcCfg == null) {
			wfProcCfg = new EventHandlerWfProcCfgPO();
		}
		
		map.addAttribute("wfProcCfg", wfProcCfg);
		
		return "/zzgl/handlerCfg/detail_wf_proc_cfg.ftl";
	}
	
	/**
	 *  新增/更新环节配置信息
	 * @param session
	 * @param wfProcCfg
	 * @param map
	 * @return
	 * 		success 操作结果，true 操作成功；false 操作失败
	 * 		tipMsg	操作提示信息
	 * 		wfpcId	流程环节配置id
	 */
	@ResponseBody
	@RequestMapping(value = "/saveWfProcCfg", method = RequestMethod.POST)
	public Map<String, Object> saveWfProcCfg(
			HttpSession session,
			@ModelAttribute(value = "wfProcCfg") EventHandlerWfProcCfgPO wfProcCfg, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		Long wfpcId = wfProcCfg.getWfpcId();
		boolean result = false;
		ResultObj resultObj = null;
		Msg msg = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			if(wfpcId != null && wfpcId > 0) {
				msg = Msg.EDIT;
				
				result = eventHandlerWfCProcfgService.updateHandlerWfProcCfgById(wfProcCfg);
			} else {
				msg = Msg.ADD;
				
				result = eventHandlerWfCProcfgService.saveHandlerWfProcCfg(wfProcCfg) > 0;
			}
			
			resultObj = msg.getResult(result);
		} catch(Exception e) {
			e.printStackTrace();
			resultObj = msg.getResult(e);
		}
		
		wfpcId = wfProcCfg.getWfpcId();
		
		if(result) {
			params.put("wfpcId", wfpcId);
			this.saveActorCfg(params);
		}
		
		resultMap.put("success", resultObj.isSuccess());
		resultMap.put("tipMsg", resultObj.getTipMsg());
		resultMap.put("wfpcId", wfpcId);
		
		return resultMap;
	}
	
	/**
	 * 依据主键删除环节配置信息
	 * @param session
	 * @param wfpcId	环节配置信息主键
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delProcCfg", method = RequestMethod.POST)
	public ResultObj delProcCfg(
			HttpSession session,
			@RequestParam(value = "wfpcId") Long wfpcId, 
			ModelMap map) {
		boolean result = false;
		ResultObj resultObj = null;
		
		result = eventHandlerWfCProcfgService.delHandlerWfProcCfgById(wfpcId);
		
		resultObj = Msg.DELETE.getResult(result);
		
		return resultObj;
	}
	
	/**
	 * 加载流程环节配置信息
	 * @param session
	 * @param page		页码
	 * @param rows		每页显示记录数量
	 * @param params
	 * 			regionCode	地域编码
	 * 			wfcId		业务配置主键
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listProcCfgData", method = RequestMethod.POST)
	public EUDGPagination listProcCfgData(HttpSession session, 
			@RequestParam(value = "page") int page, 
			@RequestParam(value = "rows") int rows, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		
		return eventHandlerWfCProcfgService.findHandlerWfProcCfgPagination(page, rows, params);
	}
	
	/**
	 * 获取流程环节信息
	 * @param session
	 * @param wfcId		业务配置id
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchTaskCodes", method = RequestMethod.POST)
	public List<Map<String, Object>> fetchTaskCodes(HttpSession session, 
			@RequestParam(value = "wfcId") Long wfcId,
			ModelMap map) {
		List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> resultMap = null;
		EventHandlerWfCfgPO handlerWfCfg = eventHandlerWfCfgService.findHandlerWfCfgById(wfcId);
		
		if(handlerWfCfg != null) {
			String bizType = handlerWfCfg.getBizType();
			Long wfCfgId = handlerWfCfg.getWfCfgId();
			
			if(EventHandlerWfCfgPO.BIZ_TYPE.WORKFLOW.getCode().equals(bizType)) {
				List<Node> nodeList = eventDisposalWorkflowService.queryNodes(wfCfgId);
				if(nodeList != null) {
					for(Node node : nodeList) {
						resultMap = new HashMap<String, Object>();
						resultMap.put("taskCode", node.getNodeName());
						resultMap.put("taskCodeName", node.getNodeNameZH());
						resultMapList.add(resultMap);
					}
				}
			}
		}
		
		return resultMapList;
	}
	
	/**
	 * 无分页获取环节配置信息
	 * @param session
	 * @param params
	 * 			regionCode	地域编码
	 * 			searchType	eq：地域编码使用精确查找
	 * 			wfcId		业务配置id
	 * 			eventCode	事件类别
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchCfgTaskCodes", method = RequestMethod.POST)
	public List<Map<String, Object>> fetchCfgTaskCodes(HttpSession session, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> resultMap = null;
		
		List<EventHandlerWfProcCfgPO> wfProcCfgList = eventHandlerWfCProcfgService.findHandlerWfProcCfgList(params);
		if(wfProcCfgList != null) {
			for(EventHandlerWfProcCfgPO wfProcCfg : wfProcCfgList) {
				resultMap = new HashMap<String, Object>();
				resultMap.put("wfpcId", wfProcCfg.getWfpcId());
				resultMap.put("taskCode", wfProcCfg.getTaskCode());
				resultMap.put("taskCodeName", wfProcCfg.getTaskCodeName());
				resultMap.put("eventCodes", wfProcCfg.getEventCodes());
				
				resultMapList.add(resultMap);
			}
		}
		
		return resultMapList;
	}
	
	/**
	 * 获取环节人员配置
	 * @param session
	 * @param params
	 * 			regionCode	地域编码
	 * 			searchType	eq：地域编码使用精确查找
	 * 			wfcId		业务配置id
	 * 			eventCode	事件类别
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchCfgActor", method = RequestMethod.POST)
	public List<Map<String, Object>> fetchCfgActor(HttpSession session, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> resultMap = null;
		List<EventHandlerWfProcCfgPO> wfProcCfgList = null;
		
		if(CommonFunctions.isNotBlank(params, "wfpcId")) {
			Long wfpcId = -1L;
			
			try {
				wfpcId = Long.valueOf(params.get("wfpcId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
			if(wfpcId > 0) {
				EventHandlerWfProcCfgPO wfProc = eventHandlerWfCProcfgService.findHandlerWfProcCfgById(wfpcId);
				if(wfProc != null) {
					wfProcCfgList = new ArrayList<EventHandlerWfProcCfgPO>();
					wfProcCfgList.add(wfProc);
				}
			}
		} else {
			wfProcCfgList = eventHandlerWfCProcfgService.findHandlerWfProcCfgList(params);
		}
		
		if(wfProcCfgList != null) {
			List<EventHandlerWfActorCfgPO> actorList = null;
			Map<String, Object> actorParam = null;
			String transactorType = "";
			
			for(EventHandlerWfProcCfgPO wfProcCfg : wfProcCfgList) {
				actorParam = new HashMap<String, Object>();
				actorParam.put("wfpcId", wfProcCfg.getWfpcId());
				
				actorList = eventHandlerWfActorCfgService.findHandlerWfActorCfgList(actorParam);
				if(actorList != null) {
					for(EventHandlerWfActorCfgPO actor : actorList) {
						transactorType = actor.getTransactorType();
						
						if(EventHandlerWfActorCfgPO.TRANSACTOR_TYPE.PERSON.getCode().equals(transactorType)) {
							resultMap = new HashMap<String, Object>();
							
							resultMap.put("userId", actor.getTransactorId());
							resultMap.put("userName", actor.getTransactorName());
							resultMap.put("orgId", actor.getTransactorOrgId());
							resultMap.put("orgName", actor.getTransactorOrgName());
							
							resultMapList.add(resultMap);
						}
					}
				}
			}
		}
		
		return resultMapList;
	}
	
	/**
	 * 新增环节人员配置
	 * @param params
	 * 			wfpcId	
	 * 			transactorIds
	 * 			transactorOrgIds
	 * 			transactorType
	 * @return
	 */
	private int saveActorCfg(Map<String, Object> params) {
		int result = 0;
		
		if(params != null && !params.isEmpty()) {
			Long wfpcId = -1L;
			String transactorIds = "", transactorOrgIds = "", transactorType = "";
			
			if(CommonFunctions.isNotBlank(params, "wfpcId")) {
				wfpcId = Long.valueOf(params.get("wfpcId").toString());
			}
			if(CommonFunctions.isNotBlank(params, "transactorIds")) {
				transactorIds = params.get("transactorIds").toString();
			}
			if(CommonFunctions.isNotBlank(params, "transactorOrgIds")) {
				transactorOrgIds = params.get("transactorOrgIds").toString();
			}
			if(CommonFunctions.isNotBlank(params, "transactorType")) {
				transactorType = params.get("transactorType").toString();
			}
			
			try {
				result = eventHandlerWfActorCfgService.saveHandlerWfActorCfg(wfpcId, transactorIds, transactorOrgIds, transactorType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
}
