package cn.ffcs.zhsq.eventCase.controller;

import cn.ffcs.common.message.bo.MsgSendMid;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IMsgSendMidOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventCase.service.IEventCaseService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 案件 江西省罗坊镇
 * @author zhangls
 *
 */
@Controller
@RequestMapping("/zhsq/eventCase")
public class EventCaseController extends ZZBaseController {
	@Autowired
	private IEventCaseService eventCaseService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;

	@Autowired
	private IMsgSendMidOutService msgSendMidService;
	
	private String FORM_TYPE = "eventCase";//表单类型
	
	/**
	 * 跳转案件列表
	 * @param session
	 * @param listType	列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toList(HttpSession session, 
			@RequestParam(value = "listType", required=false, defaultValue="1") Integer listType,
			ModelMap map) {
		map.addAttribute("listType", listType);
		
		return "/zzgl/eventCase/list_eventCase.ftl";
	}
	
	/**
	 * 跳转案件新增/编辑页面
	 * @param session
	 * @param caseId	案件id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(HttpSession session, 
			@RequestParam(value = "caseId", required = false) Long caseId, 
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> eventCase = null;
		int markerOperation =  ConstantValue.ADD_MARKER;// 添加标注操作
		
		if(caseId != null && caseId > 0) {
			Map<String, Object> params = new HashMap<String, Object>(),
								resultMap = new HashMap<String, Object>();
			
			params.put("isResMarkerNeeded", true);
			params.put("isInvolvedPeopleNeeded", true);
			
			resultMap = eventCaseService.findEventCaseById(caseId, params, userInfo.getOrgCode());
			
			map.addAllAttributes(resultMap);
			
			markerOperation = ConstantValue.EDIT_MARKER; // 编辑标注操作
		} else {
			eventCase = new HashMap<String, Object>();
			
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			String gridId = defaultGridInfo.get(KEY_START_GRID_ID).toString(),
				   infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString(),
				   gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString();
			
			eventCase.put("gridId", Long.valueOf(gridId));
			eventCase.put("infoOrgCode", infoOrgCode);
			eventCase.put("gridName", gridName);
			eventCase.put("happenTimeStr", DateUtils.getNow());
			eventCase.put("contactUser", userInfo.getPartyName());
			eventCase.put("tel", userInfo.getVerifyMobile());
			
			map.addAttribute("eventCase", eventCase);
		}
		
		map.addAttribute("bigTypePcode", ConstantValue.BIG_TYPE_PCODE);
		map.addAttribute("urgencyDegreePcode", ConstantValue.URGENCY_DEGREE_PCODE);
		map.addAttribute("influenceDegreePcode", ConstantValue.INFLUENCE_DEGREE_PCODE);
		map.addAttribute("sourcePcode", ConstantValue.SOURCE_PCODE);
		map.addAttribute("involvedNumPcode", ConstantValue.INVOLVED_NUM_PCODE);
		map.addAttribute("attachmentType", ConstantValue.EVENT_CASE_ATTACHMENT_TYPE);
		map.addAttribute("mapModuleCode", ConstantValue.EVENT_CASE_MODULE_CODE);
		map.addAttribute("maxHappenTime", DateUtils.getNow());//案发时间上限时间 yyyy-mm-dd hh:mi:ss，当happenTimeStr为空时，使用
		map.addAttribute("markerOperation", markerOperation);
		
		return "/zzgl/eventCase/add_eventCase.ftl";
	}
	
	/**
	 * 跳转案件详情/办理页面
	 * @param session
	 * @param caseId	案件id
	 * @param listType	列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toDetail")
	public String toDetail(HttpSession session, 
			@RequestParam(value = "caseId") Long caseId,
						   @RequestParam(value = "toPage", required = false) String toPage,
						   @RequestParam(value = "listType", required=false, defaultValue="1") Integer listType,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		map.addAllAttributes(capDetailData(caseId, listType, userInfo));
		map.addAttribute("listType", listType);
		map.addAttribute("attachmentType", ConstantValue.EVENT_CASE_ATTACHMENT_TYPE);
		map.addAttribute("mapModuleCode", ConstantValue.EVENT_CASE_MODULE_CODE);
		map.addAttribute("markerOperation", ConstantValue.TITLE_MARKER); // 查看地图标注操作
		if(StringUtils.isNotBlank(toPage) && toPage.equals("luofang")){
			return "/zzgl/eventCase/indexPage/detail_eventCase.ftl";
		}
		return "/zzgl/eventCase/detail_eventCase.ftl";
	}
	
	/**
	 * 获取案件详情信息
	 * @param caseId	案件id
	 * @param listType	列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * @param userInfo
	 * @return
	 */
	private Map<String, Object> capDetailData(Long caseId, Integer listType, UserInfo userInfo) {
		Map<String, Object> params = new HashMap<String, Object>(),
							resultMap = new HashMap<String, Object>();
		
		params.put("isResMarkerNeeded", true);//需要获取地图定位信息
		
		resultMap = eventCaseService.findEventCaseById(caseId, params, userInfo.getOrgCode());

		if(CommonFunctions.isBlank(resultMap, "eventCase")) {
			resultMap.put("eventCase", new HashMap<String, Object>());
		}

		try {
			resultMap.putAll(this.capWorkflowRelativeData(caseId, listType, userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	/**
	 * 案件详情/办理页面
	 * @param session
	 * @param caseId	案件id
	 * @param listType	列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/case/detailData")
	public Map<String, Object> detailData(HttpSession session,
										@RequestParam(value = "caseId") Long caseId,
										@RequestParam(value = "listType", required=false, defaultValue="1") Integer listType,
										ModelMap map) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap.putAll(this.capDetailData(caseId,listType,userInfo));

		Long instanceId = eventCaseService.capInstanceId(caseId, userInfo);
		List<Map<String, Object>> queryTaskList = eventDisposalWorkflowService.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
		resultMap.put("taskList", queryTaskList);
		
		return resultMap;
	}
	
	/**
	 * 跳转督办/催办操作页面
	 * @param session
	 * @param caseId		案件id			
	 * @param operateType	操作类型
	 * 				0		催办
	 * 				1		督办
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddUrgeOrRemind")
	public String toAddUrgeOrRemind(HttpSession session, 
			@RequestParam(value = "caseId") Long caseId,
			@RequestParam(value = "operateType", required = false, defaultValue = "0") Integer operateType,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Long instanceId = -1L, taskId = -1L;
		
		try {
			instanceId = eventCaseService.capInstanceId(caseId, userInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		taskId = eventDisposalWorkflowService.curNodeTaskId(instanceId);
		
		map.addAttribute("formId", caseId);
		map.addAttribute("instanceId",instanceId);
		map.addAttribute("taskId",taskId);
		map.addAttribute("operateType", operateType);
		
		return "/zzgl/event/workflow/basic/add_urge_base.ftl";
	}
	
	/**
	 * 添加督办/催办记录
	 * @param session
	 * @param taskId			当前任务id
	 * @param remarks			办理意见
	 * @param instanceId		实例id
	 * @param otherMobileNums	接收短信信息手机号码
	 * @param smsContent		短信内容
	 * @param operateType		操作类型
	 * 				0			催办
	 * 				1			督办
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addUrgeOrRemind", method = RequestMethod.POST)
	public Map<String, Object> addUrgeOrRemind(
			HttpSession session,
			@RequestParam(value = "taskId") Long taskId,
			@RequestParam(value = "remarks") String remarks,//催办意见
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam(value = "otherMobileNums", required = false) String otherMobileNums,
			@RequestParam(value = "smsContent", required = false) String smsContent,
			@RequestParam(value = "operateType", required = false, defaultValue = "0") Integer operateType) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String userIds = eventDisposalWorkflowService.curNodeUserIds(instanceId);//获取当前办理人员userId
		boolean result = false;
		
		switch(operateType) {
			case 0: {//催办
				String userNames = eventDisposalWorkflowService.curNodeUserNames(userIds);//获取当前办理人员姓名
				
				if(StringUtils.isNotBlank(userIds)){
					result = eventDisposalWorkflowService.addRemind(instanceId, taskId, userInfo, remarks, userIds, userNames);
				}
				
				break;
			}
			case 1: {//督办
				result = eventDisposalWorkflowService.supervise(instanceId, taskId, userInfo, remarks, "", "");
				
				break;
			}
		}
		
		if(result){
			//发送短信
			if(!StringUtils.isEmpty(smsContent)){
				//获取当前任务办理人员userId，以英文逗号相连
				eventDisposalWorkflowService.sendSms(userIds, otherMobileNums, smsContent, userInfo);

			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("operateType", operateType);
		resultMap.put("result", result);
		
		return resultMap;
	}
	
	/**
	 * 保存案件信息
	 * @param session
	 * @param eventCase	案件信息
	 * @param params
	 * 			isStart	true启动流程
	 * 			isClose	true案件扭转至评价环节
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveEventCase")
	public Map<String, Object> saveEventCase(HttpSession session, 
			@RequestParam(value = "caseId", required = false, defaultValue = "-1") Long caseId,
			@RequestParam Map<String, Object> eventCase,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean result = false;
		ResultObj resultObj = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(caseId != null && caseId > 0) {//更新信息
			try {
				result = eventCaseService.updateEventCase(eventCase, userInfo);
				
				resultObj = Msg.EDIT.getResult(result);
			} catch (Exception e) {
				e.printStackTrace();
				resultObj = Msg.EDIT.getResult(e);
			}
		} else {//新增信息
			try {
				caseId = eventCaseService.saveEventCase(eventCase, userInfo);
				
				result = caseId > 0;
				
				resultObj = Msg.ADD.getResult(result);
			} catch (Exception e) {
				e.printStackTrace();
				resultObj = Msg.ADD.getResult(e);
			}
		}
		
		resultMap.put("success", resultObj.isSuccess());
		resultMap.put("tipMsg", resultObj.getTipMsg());
		resultMap.put("caseId", caseId);
		
		return resultMap;
	}
	
	/**
	 * 删除案件信息
	 * @param session
	 * @param caseId	案件id
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delEventCase")
	public ResultObj delEventCase(HttpSession session, 
			@RequestParam(value = "caseId") Long caseId, 
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;
		boolean result = eventCaseService.delEventCaseById(caseId, userInfo.getUserId());
		
		resultObj = Msg.DELETE.getResult(result);
		
		return resultObj;
	}

	/**
	 * 获取案件总量
	 * @param session
	 * @param params
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/caseCount")
	public int caseCount(HttpSession session,
								  @RequestParam Map<String, Object> params){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		int eventCaseCount = eventCaseService.findEventCaseCount(params, userInfo);
		return eventCaseCount;
	}

	/**
	 * 加载案件列表数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param params
	 * 			listType	列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * 			infoOrgCode	地域编码
	 * 			type		案件分类
	 * 			keyWord		关键字
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listData")
	public EUDGPagination listData(HttpSession session, 
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, 
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		params.put("startInfoOrgCode", this.getDefaultInfoOrgCode(session));
		
		EUDGPagination eventCasePagination = eventCaseService.findEventCasePagination(page, rows, params, userInfo);
		
		return eventCasePagination;
	}
	
	/**
	 * 启动案件流程
	 * @param session
	 * @param caseId	案件id
	 * @param params
	 * 			advice	办理意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/startWorkflow4Case", method = RequestMethod.POST)
	public ResultObj startWorkflow4Case(HttpSession session,
			@RequestParam(value = "caseId") Long caseId,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;
		
		try {
			boolean result = eventCaseService.startWorkflow4Case(caseId, userInfo, params);
			
			resultObj = Msg.OPERATE.getResult(result);
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(e);
			e.printStackTrace();
		}
		
		return resultObj;
	}
	
	/**
	 * 提交案件
	 * @param session
	 * @param caseId	案件id
	 * @param nextNodeName		下一环节名称
	 * @param params
	 * 			nextUserIds 	下一环节办理人员，以英文逗号分隔 
	 * 			nextOrgIds 		下一环节办理组织，以英文逗号分隔，与nextUserIds一一对应 
	 * 			advice 			办理意见
	 * 			evaContent		评价意见
	 * 			evaLevel		评价等级
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/subWorkflow4Case", method = RequestMethod.POST)
	public ResultObj subWorkflow4Case(HttpSession session,
			@RequestParam(value = "formId") Long caseId,
			@RequestParam(value = "nextNodeName") String nextNodeName,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj result = null;

		try {
			boolean flag = eventCaseService.subWorkflow4Case(caseId, nextNodeName, null, userInfo, params);
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
	 * @param caseId			案件id
	 * @param rejectToNodeName	驳回环节的名称
	 * @param params
	 * 			advice 			驳回意见
	 * 			evaContent		评价意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rejectWorkflow4Case", method = RequestMethod.POST)
	public ResultObj rejectWorkflow4Case(HttpSession session,
			@RequestParam(value = "formId") Long caseId,
			@RequestParam(value = "rejectToNodeName", required=false) String rejectToNodeName,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj result = null;
		
		try {
			boolean flag = eventCaseService.rejectWorkflow4Case(caseId, rejectToNodeName, userInfo, params);
			result = Msg.OPERATE.getResult(flag);
		} catch (Exception e) {
			result = Msg.OPERATE.getResult(e);
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 获取案件额外属性
	 * @param session
	 * @param caseId	案件id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchEventExtraAttr")
	public Map<String, Object> fetchEventExtraAttr(
			HttpSession session,
			@RequestParam(value = "caseId") Long caseId) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 获取评价列表
		List<Map<String, Object>> evaResultList = eventCaseService.findEvaListByCaseId(caseId, userInfo.getOrgCode());
		
		//获取被督办列表
		List<Map<String, Object>> superviseResultList = eventCaseService.findRemindListByCaseId(caseId);
		
		if(evaResultList != null) {
			resultMap.put("evaResultList", evaResultList);
		}
		
		if(superviseResultList != null) {
			resultMap.put("superviseResultList", superviseResultList);
		}
		
		return resultMap;
	}
	
	/**
	 * 获取案件相关工作流信息
	 * @param caseId	案件id
	 * @param listType	列表类型
	 * 			1 草稿
	 * 			2 待办
	 * 			3 经办
	 * 			4 我发起的
	 * 			5 辖区所有
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> capWorkflowRelativeData(Long caseId, Integer listType, UserInfo userInfo) throws Exception {
		Map<String, Object> curTaskData = null,
							resultData = new HashMap<String, Object>();
		Long taskId = -1L;
		
		Long instanceId = eventCaseService.capInstanceId(caseId, userInfo);
		
		if(instanceId > 0) {
			curTaskData = eventCaseService.findCurTaskData(caseId, userInfo);
			
			if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
				taskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
			}
			
			List<Map<String, Object>> participantMapList = eventCaseService.findParticipantsByTaskId(taskId);
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
				
				resultData.put("taskPersonStr", taskPersonStr.substring(1));
				
				if(listType == 2) {//待办
					resultData.put("isCurHandler", eventCaseService.isCurTaskPaticipant(participantMapList, userInfo, null));
					
					if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
						resultData.put("curNodeName", curTaskData.get("NODE_NAME"));
					}
					if(CommonFunctions.isNotBlank(curTaskData, "NODE_ID")) {
						Integer nodeId = Integer.valueOf(curTaskData.get("NODE_ID").toString());
						resultData.put("operateList", eventCaseService.findOperateByNodeId(nodeId));
					}
					
					resultData.put("nextTaskNodes", eventCaseService.findNextTaskNodes(caseId, userInfo, null));
					resultData.put("evaLevelDict", baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.EVALUATE_LEVEL_PCODE, userInfo.getOrgCode()));
				}
			}
			
			if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
				resultData.put("curTaskName", curTaskData.get("WF_ACTIVITY_NAME_"));
			}
			
			resultData.put("formId", caseId);
			resultData.put("formType", FORM_TYPE);
			resultData.put("instanceId", instanceId);
		}
		
		return resultData;
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
			instanceId = eventCaseService.capInstanceId(bizId, userInfo);
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
}
