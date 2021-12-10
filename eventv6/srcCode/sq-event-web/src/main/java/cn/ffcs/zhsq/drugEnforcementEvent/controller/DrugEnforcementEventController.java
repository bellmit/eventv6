package cn.ffcs.zhsq.drugEnforcementEvent.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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

import cn.ffcs.common.DictPcode;
import cn.ffcs.common.message.bo.MsgSendMid;
import cn.ffcs.common.message.bo.MsgSendMidSub;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.DrugRecord;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.crowd.IDrugRecordService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IMsgSendMidOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.drugEnforcementEvent.service.IDrugEnforcementEventService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.drugEnforcementEvent.DrugEnforcementEvent;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**
 * 禁毒事件
 * @author zhangls
 *
 */
@Controller
@RequestMapping(value="/zhsq/drugEnforcementEvent")
public class DrugEnforcementEventController extends ZZBaseController {
	@Autowired
	private IDrugEnforcementEventService drugEnforcementEventService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IDrugRecordService drugRecordService;
	
	@Autowired
	private IMsgSendMidOutService msgSendMidService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
    private IMixedGridInfoService gridInfoService;
	
	private final String FORM_TYPE = "drugEnforcementEvent";//表单类型
	private final String ATTACHMENT_TYPE = "DRUG_ENFORCEMENT_EVENT";//附件类型
	
	/**
	 * 跳转禁毒事件新增/编辑页面
	 * drugEnforcementId有效时，转入编辑页面
	 * @param session
	 * @param drugEnforcementId	禁毒事件id
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toAdd")
    public String toAdd(HttpSession session,
    		@RequestParam(value = "drugEnforcementId", required = false) Long drugEnforcementId,
    		ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		DrugEnforcementEvent drugEnforcementEvent = null;
		String orgCode = userInfo.getOrgCode();
		Map<String, Object> gridMap = this.getDefaultGridInfo(session);
		
		if(drugEnforcementId != null && drugEnforcementId > 0) {
			drugEnforcementEvent = drugEnforcementEventService.findDrugEnforcementEventById(drugEnforcementId, orgCode);
		}
		if(drugEnforcementEvent == null) {
			drugEnforcementEvent = new DrugEnforcementEvent();
			drugEnforcementEvent.setContactUser(userInfo.getPartyName());
			drugEnforcementEvent.setContactTel(userInfo.getVerifyMobile());
		}
		
		map.addAttribute("drugEnforcementEvent", drugEnforcementEvent);
		map.addAttribute("startGridId", gridMap.get(KEY_START_GRID_ID));
		map.addAttribute("drugSocailSituationDict", baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.DRUG_SOCIAL_SITUATION, orgCode));
		map.addAttribute("drugEventContentDict", baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.DRUG_EVENT_CONTENT, orgCode));
		map.addAttribute("ATTACHMENT_TYPE", ATTACHMENT_TYPE);
		map.put("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		
		return "/zzgl/drugEnforcementEvent/add_drugEnforcementEvent.ftl";
	}
	
	/**
	 * 跳转禁毒事件详情页面
	 * @param session
	 * @param drugEnforcementId	禁毒事件id
	 * @param listType			列表类型
	 * 			1	草稿
	 * 			2	待办
	 * 			3	辖区所有
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toDetail")
    public String toDetail(HttpSession session,
    		@RequestParam(value = "drugEnforcementId") Long drugEnforcementId,
    		@RequestParam(value = "listType", required=false, defaultValue="1") Integer listType,
    		ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String orgCode = userInfo.getOrgCode();
		DrugEnforcementEvent drugEnforcementEvent = drugEnforcementEventService.findDrugEnforcementEventById(drugEnforcementId, orgCode);
		
		if(drugEnforcementEvent == null) {
			drugEnforcementEvent = new DrugEnforcementEvent();
		}
		
		try {
			this.capWorkflowRelativeData(drugEnforcementId, listType, userInfo, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		map.addAttribute("drugEnforcementEvent", drugEnforcementEvent);
		map.addAttribute("listType", listType);
		map.addAttribute("drugEventContentDict", baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.DRUG_EVENT_CONTENT, orgCode));
		map.addAttribute("ATTACHMENT_TYPE", ATTACHMENT_TYPE);
		map.put("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		
		return "/zzgl/drugEnforcementEvent/detail_drugEnforcementEvent.ftl";
	}
	
	/**
	 * 跳转禁毒事件列表页面
	 * @param session
	 * @param listType			列表类型
	 * 			1	草稿
	 * 			2	待办
	 * 			3	辖区所有
	 * @param params
	 * 			status			状态
	 * 			handleStatus	处置状态 01 未超时；02 已超时
	 * 			infoOrgCode		地域编码
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toList")
    public String toList(HttpSession session, 
    		@RequestParam(value = "listType", required=false, defaultValue="1") Integer listType,
    		@RequestParam Map<String, Object> params,
    		ModelMap map) {
		Map<String, String> statusMap = new HashMap<String, String>();
		
		for(IDrugEnforcementEventService.STATUS status : IDrugEnforcementEventService.STATUS.values()) {
			statusMap.put(status.getStatus(), status.getStatusName());
		}
		
		if(CommonFunctions.isNotBlank(params, "infoOrgCode")) {
			String infoOrgCode = params.get("infoOrgCode").toString();
			MixedGridInfo gridInfo = gridInfoService.getDefaultGridByOrgCode(infoOrgCode);
			
			if(gridInfo != null) {
				map.addAttribute("startGridId", gridInfo.getGridId());
				map.addAttribute("startGridName", gridInfo.getGridName());
			}
		}
		
		map.addAttribute("statusMap", statusMap);
		map.addAllAttributes(params);
		
		return "/zzgl/drugEnforcementEvent/list_drugEnforcementEvent.ftl";
	}
	
	/**
	 * 保存禁毒事件记录
	 * @param session
	 * @param drugEnforcementEvent
	 * 			drugEnforcementId 有效时，更新；否则为新增
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveData", method = RequestMethod.POST)
	public Map<String, Object> saveData(HttpSession session,
			@ModelAttribute(value = "drugEnforcementEvent") DrugEnforcementEvent drugEnforcementEvent,
			@RequestParam(value = "attachmentId", required = false) Long[] attachmentIds) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Long drugEnforcementId = drugEnforcementEvent.getDrugEnforcementId();
		Msg result = Msg.ADD;
		Map<String, Object> resultMap = new HashMap<String, Object>(),
							saveMap = new HashMap<String, Object>();
		
		if(drugEnforcementId != null && drugEnforcementId > 0) {
			result = Msg.EDIT;
		}
		
		if(attachmentIds != null && attachmentIds.length > 0) {
			saveMap.put("attachmentIds", attachmentIds);
		}
		
		drugEnforcementId = drugEnforcementEventService.saveOrUpdateDrugEnforcementEvent(drugEnforcementEvent, userInfo, saveMap);
		ResultObj resultObj = result.getResult(drugEnforcementId > 0);
		
		resultMap.put("success", resultObj.isSuccess());
		resultMap.put("tipMsg", resultObj.getTipMsg());
		resultMap.put("drugEnforcementId", drugEnforcementId);
		
		return resultMap;
	}
	
	/**
	 * 提交禁毒事件
	 * @param session
	 * @param drugEnforcementId	禁毒事件id
	 * @param nextNodeName		下一环节名称
	 * @param params
	 * 			nextUserIds 	下一环节办理人员，以英文逗号分隔 
	 * 			nextOrgIds 		下一环节办理组织，以英文逗号分隔，与nextUserIds一一对应 
	 * 			advice 			办理意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/subWorkflowData", method = RequestMethod.POST)
	public ResultObj subWorkflowData(HttpSession session,
			@RequestParam(value = "formId") Long drugEnforcementId,
			@RequestParam(value = "nextNodeName") String nextNodeName,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj result = null;
		
		try {
			boolean flag = drugEnforcementEventService.subWorkflow(drugEnforcementId, nextNodeName, null, userInfo, params);
			result = Msg.OPERATE.getResult(flag);
		} catch (Exception e) {
			result = Msg.OPERATE.getResult(e);
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 驳回上一步
	 * @param session
	 * @param drugEnforcementId	禁毒事件id
	 * @param rejectToNodeName	驳回环节的名称
	 * @param params
	 * 			advice 			驳回意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rejectWorkflowData", method = RequestMethod.POST)
	public ResultObj rejectWorkflowData(HttpSession session,
			@RequestParam(value = "formId") Long drugEnforcementId,
			@RequestParam(value = "rejectToNodeName", required=false) String rejectToNodeName,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj result = null;
		
		try {
			boolean flag = drugEnforcementEventService.rejectWorkflow(drugEnforcementId, rejectToNodeName, userInfo, params);
			result = Msg.OPERATE.getResult(flag);
		} catch (Exception e) {
			result = Msg.OPERATE.getResult(e);
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 删除禁毒事件
	 * @param session
	 * @param drugEnforcementId	禁毒事件id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delData", method = RequestMethod.POST)
	public ResultObj delData(HttpSession session,
			@RequestParam(value = "drugEnforcementId") Long drugEnforcementId) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		boolean flag = drugEnforcementEventService.deleteDrugEnforcementEventById(drugEnforcementId, userInfo.getUserId());
		
		return Msg.DELETE.getResult(flag);
	}
	
	/**
	 * 加载禁毒事件记录
	 * @param session
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 			 listType		列表类型 1 草稿；2 待办；3 辖区所有；默认为1 
	 * 			 userOrgCode	组织编码，为空时，使用userInfo中的orgCode 
	 * 			 workflowName 	流程图名称，默认为 禁毒事件流程 
	 * 			 wfTypeId 		流程图类型编码，默认为 drug_enforcement_event
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listData", method = RequestMethod.POST)
	public EUDGPagination listData(HttpSession session,
			@RequestParam(value = "page") int pageNo,
			@RequestParam(value = "rows") int pageSize,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		params.put("startInfoOrgCode", this.getDefaultInfoOrgCode(session));
		
		EUDGPagination pagination = drugEnforcementEventService.findDrugEnforcementEventPagination(pageNo, pageSize, userInfo, params);
		
		return pagination;
	}
	
	/**
	 * 依据吸毒人员id/人口id查找相关的吸毒人员信息
	 * @param session
	 * @param ciRsId	人口id
	 * @param drugId	吸毒人员id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findDrugInfoById", method = RequestMethod.POST)
	public DrugRecord findDrugInfoById(HttpSession session,
			@RequestParam(value = "ciRsId", required=false) Long ciRsId,
			@RequestParam(value = "drugId", required=false) Long drugId) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String orgCode = userInfo.getOrgCode();
		DrugRecord drugRecord = null;
		
		if(drugId != null && drugId > 0) {
			drugRecord = drugRecordService.findDrugRecordById(drugId);
		} else if(ciRsId != null && ciRsId > 0) {
			drugRecord = drugRecordService.findByCiRsId(ciRsId, orgCode);
		}
		
		if(drugRecord != null) {
			String drugVar = drugRecord.getDrugVar();
			
			if(StringUtils.isNotBlank(drugVar)) {
				drugRecord.setDrugVar(baseDictionaryService.changeCodeToName(DictPcode.DRUG_VAR, drugVar, orgCode));
			}
		}
		
		return drugRecord;
	}
	
	/**
	 * 启动禁毒事件流程
	 * @param session
	 * @param drugEnforcementId 禁毒事件id
	 * @param params
	 * 			isClose	1 采集并归档禁毒事件
	 * 			advice	办理意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/startWorkflow4DrugEnforcement", method = RequestMethod.POST)
	public ResultObj startWorkflow4DrugEnforcement(HttpSession session,
			@RequestParam(value = "drugEnforcementId") Long drugEnforcementId,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;
		
		try {
			boolean result = drugEnforcementEventService.reportDrugEnforcementEvent(drugEnforcementId, userInfo, params);
			resultObj = Msg.OPERATE.getResult(result);
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(e);
			e.printStackTrace();
		}
		
		return resultObj;
	}
	
	/**
	 * 获取短信内容
	 * @param session
	 * @param drugEnforcementId	禁毒事件id
	 * @param curNodeName		当前环节名称
	 * @param nextNodeName		下一环节名称
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/capSmsContent", method = RequestMethod.POST)
	public Map<String, Object> capSmsContent(HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "formId") Long drugEnforcementId,
			@RequestParam(value = "curNodeName") String curNodeName,
			@RequestParam(value = "nextNodeName") String nextNodeName,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(CommonFunctions.isBlank(params, "gridPath")) {
			MixedGridInfo gridInfo = this.getMixedGridInfo(session, request);
			if(gridInfo != null) {
				params.put("gridPath", gridInfo.getGridPath());
			}
		}
		
		try {
			resultMap.put("smsContent", drugEnforcementEventService.capSMSContent(drugEnforcementId, curNodeName, nextNodeName, userInfo, params));
		} catch (Exception e) {
			resultMap.put("msgWrong", e.getMessage());
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	/**
	 * 跳转到已发送消息页面
	 * @param session
	 * @param bizId			业务id
	 * @param bizType		业务类型
	 * @param targetTypes	消息类型 SMS APP
	 * @param map
	 * @return
	 */
	/**
	 * 跳转到已发送消息页面
	 * @param session
	 * @param bizId			业务id
	 * @param bizType		业务类型
	 * @param targetTypes	消息类型 SMS APP
	 * @param params
	 * 			taskBizType	任务业务类型
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toEventMsgSendMidList")
	public String toEventMsgSendMidList(HttpSession session, 
			@RequestParam(value = "bizId") Long bizId,
			@RequestParam(value = "bizType") String bizType,
			@RequestParam(value = "targetTypes", required = false) String targetTypes,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		map.addAllAttributes(params);
		map.addAttribute("bizId", bizId);
		
		map.addAttribute("GMIS_URL", App.GMIS.getDomain(session));
		
		return "/zzgl/drugEnforcementEvent/list_msgSendMid.ftl";
	}
	
	/**
	 * 获取消息发送记录
	 * @param session
	 * @param bizId			业务id
	 * @param bizType		业务类型
	 * @param targetTypes	消息类型 SMS APP
	 * @param params
	 * 			taskBizType	任务业务类型
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listMsgSendMidData", method = RequestMethod.POST)
	public EUDGPagination listMsgSendMidData(
			HttpSession session,
			@RequestParam(value = "bizId") Long bizId,
			@RequestParam(value = "bizType") String bizType,
			@RequestParam(value = "targetTypes", required = false) String targetTypes,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		EUDGPagination pagination = new EUDGPagination();
		Map<String, Object> msgSendParams = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		List<Map<String, Object>> bizMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> bizMap = new HashMap<String, Object>();
		bizMap.put("bizId", bizId);
		bizMap.put("bizType", bizType);
		bizMapList.add(bizMap);
		
		//获取环节相关短信记录
		Long instanceId = -1L;
		
		try {
			instanceId = drugEnforcementEventService.capInstanceId(bizId, userInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Map<String, Object>> taskMapList = eventDisposalWorkflowService.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
		if(taskMapList != null) {
			String taskBizType = "";
			
			if(CommonFunctions.isNotBlank(params, "taskBizType")) {
				taskBizType = params.get("taskBizType").toString();
			}
			
			for(Map<String, Object> taskMap : taskMapList) {
				if(CommonFunctions.isNotBlank(taskMap, "TASK_ID")) {
					bizMap = new HashMap<String, Object>();
					bizMap.put("bizId", taskMap.get("TASK_ID"));
					bizMap.put("bizType", taskBizType);
					bizMapList.add(bizMap);
				}
			}
		}
		
		msgSendParams.put("bizMapList", bizMapList);
		msgSendParams.put("isSendCp", MsgSendMid.COMPLETE_STATUS.COMPLETE.toString());
		msgSendParams.put("targetTypes", targetTypes);
		msgSendParams.put("userOrgCode", userInfo.getOrgCode());
		
		List<MsgSendMid> msgSendMidList = msgSendMidService.findMsgSendMidList(msgSendParams);
		
		if(msgSendMidList != null) {
			pagination.setRows(msgSendMidList);
			pagination.setTotal(msgSendMidList.size());
		}
		
		return pagination;
	}
	
	/**
	 * 依据sendId获取消息中间表子表信息
	 * @param session
	 * @param sendId		消息中间表发送信息
	 * @param targetType	消息发送类型 为APP、SMS和MSG中的一种
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listMsgSendMidSubData", method = RequestMethod.POST)
	public EUDGPagination listMsgSendMidSubData(
			HttpSession session,
			@RequestParam(value = "sendId") Long sendId,
			@RequestParam(value = "targetType", required = false) String targetType,
			ModelMap map) {
		EUDGPagination pagination = new EUDGPagination();
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("sendId", sendId);
		params.put("targetType", targetType);
		
		List<MsgSendMidSub> msgSendMidSubList = msgSendMidService.findMsgSendMidSubList(params);
		
		if(msgSendMidSubList != null) {
			pagination.setRows(msgSendMidSubList);
			pagination.setTotal(msgSendMidSubList.size());
		}
		
		return pagination;
	}
	
	/**
	 * 获取禁毒事件相关工作流信息
	 * @param drugEnforcementId
	 * @param listType
	 * @param userInfo
	 * @param map
	 * @throws Exception
	 */
	private void capWorkflowRelativeData(Long drugEnforcementId, Integer listType, UserInfo userInfo, ModelMap map) throws Exception {
		Map<String, Object> curTaskData = null;
		Long taskId = -1L;
		
		Long instanceId = drugEnforcementEventService.capInstanceId(drugEnforcementId, userInfo);
		
		if(instanceId > 0) {
			curTaskData = drugEnforcementEventService.findCurTaskData(drugEnforcementId, userInfo);
			
			if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
				taskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
			}
			
			List<Map<String, Object>> participantMapList = drugEnforcementEventService.findParticipantsByTaskId(taskId);
			if(participantMapList != null) {
				StringBuffer taskPersonStr = new StringBuffer(";");
				
				for(Map<String, Object> participant : participantMapList){
					Object orgNameObj = participant.get("ORG_NAME");
					
					if(CommonFunctions.isNotBlank(participant, "USER_NAME")){
						taskPersonStr.append(participant.get("USER_NAME"));
						if(orgNameObj != null){
							taskPersonStr.append("(").append(orgNameObj).append(");");
						}
					}else if(orgNameObj != null){
						taskPersonStr.append(orgNameObj).append(";");
					}
				}
				
				map.addAttribute("taskPersonStr", taskPersonStr.substring(1));
				
				if(listType == 2) {//待办
					map.addAttribute("isCurHandler", drugEnforcementEventService.isCurTaskPaticipant(participantMapList, userInfo, null));
				}
			}
			
			if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
				map.addAttribute("curTaskName", curTaskData.get("WF_ACTIVITY_NAME_"));
			}
			if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
				map.addAttribute("curNodeName", curTaskData.get("NODE_NAME"));
			}
			if(CommonFunctions.isNotBlank(curTaskData, "NODE_ID")) {
				Integer nodeId = Integer.valueOf(curTaskData.get("NODE_ID").toString());
				map.addAttribute("operateList", drugEnforcementEventService.findOperateByNodeId(nodeId));
			}
			
			map.addAttribute("nextTaskNodes", drugEnforcementEventService.findNextTaskNodes(drugEnforcementId, userInfo));
			map.addAttribute("formId", drugEnforcementId);
			map.addAttribute("formType", FORM_TYPE);
			map.addAttribute("instanceId", instanceId);
		}
	}
}
