package cn.ffcs.zhsq.workflow.controller;

import cn.ffcs.shequ.base.service.IUserService;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作流zzgrid统一调用
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/zhsq/workflow/workflowController")
public class WorkflowController extends ZZBaseController {
	
	@Autowired
	private HessianFlowService hessianFlowService;

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	//多图片上传
	@Autowired
	private IAttachmentService attachmentService;
	
	private static int level = 0;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 启动流程
	 * @param session
	 * @param formId 		事件ID
	 * @param workFlowId	工作流ID
	 * @param toClose 		1 直接结案；0 非保存后直接结案
	 * @param advice		办理意见
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/startFlow")
	public Map<String, Object> startWorkFlow(HttpSession session,
			@RequestParam(value = "formId") Integer formId,
			@RequestParam(value = "workFlowId") Long workFlowId,
			@RequestParam(value = "toClose", required = false, defaultValue="0") String toClose,
			@RequestParam(value = "advice", defaultValue="", required=false) String advice) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>(),
							extraParam = new HashMap<String, Object>();
		String instanceId = null, msgWrong = null;
		boolean result = true;
		
		extraParam.put("advice", advice);
		
		try {
			instanceId = eventDisposalWorkflowService.startFlowByWorkFlow(Long.valueOf(formId), workFlowId, toClose, userInfo, extraParam);
		} catch (Exception e) {
			msgWrong = e.getMessage();
			logger.error("事件流程启动失败！事件id【{}】，流程图id【{}】，是否结案【{}】。", formId, workFlowId, toClose, e);
		}
		
		if(StringUtils.isNotBlank(msgWrong)) {
			result = false;
			resultMap.put("msgWrong", msgWrong);
		}
		
		resultMap.put("eventId", formId);
		resultMap.put("instanceId", instanceId);
		resultMap.put("result", result);
		resultMap.put("toClose", toClose);
		
		return resultMap;
	}
	
	/**
	 * 跨域流程启动
	 * @param session
	 * @param formId 		事件ID
	 * @param workFlowId	工作流ID
	 * @param toClose 		1 直接结案；0 非保存后直接结案
	 * @param advice		办理意见
	 * @param jsonpCallback	jsonp回调方法
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/startFlow4Jsonp")
	public String startFlow4Jsonp(HttpSession session,
			@RequestParam(value = "formId") Integer formId,
			@RequestParam(value = "workFlowId") Long workFlowId,
			@RequestParam(value = "toClose", required = false) String toClose,
			@RequestParam(value = "advice", defaultValue="", required=false) String advice,
			@RequestParam(value = "jsonpCallback") String jsonpCallback) {
		Map<String, Object> resultMap = this.startWorkFlow(session, formId, workFlowId, toClose, advice);
		StringBuffer json = new StringBuffer("");
		
		if(resultMap != null && !resultMap.isEmpty()) {
			for(String key : resultMap.keySet()) {
				json.append(",\"").append(key).append("\":\"").append(resultMap.get(key)).append("\"");
			}
			
			if(json.length() > 0) {
				json = new StringBuffer(json.substring(1));
			}
		}
		
		return jsonpCallback+"({"+json+"})";
	}
	
	/**
	 * 初始化弹窗参数（下一步提交）
	 * @param session
	 * @param request
	 * @param instanceId 实例ID
	 * @param eventId 事件ID
	 * @param taskId 当前任务ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/jumpPage")
	public String jumpPage(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam(value = "formId") Long eventId,
			@RequestParam(value = "taskId", required = false) String taskId,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = eventDisposalWorkflowService.initFlowInfo(taskId, instanceId, userInfo, null);
		EventDisposal event = eventDisposalService.findEventById(eventId, userInfo.getOrgCode());
		String type = "";
		
		if(event != null){
			type = event.getType();
		}
		
		map.put("type", type);
		map.addAttribute("EVENT_ATTACHMENT_TYPE", ConstantValue.EVENT_ATTACHMENT_TYPE);//上传附件的类型
		map.addAllAttributes(resultMap);
		map.addAttribute("formId", eventId);
		map.addAttribute("event", event);
		map.addAttribute("updomain", App.TOP.getDomain(session));
		
		String isUploadHandlingPic = funConfigurationService.turnCodeToValue(ConstantValue.IS_UPLOAD_HANDLING_PIC, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUploadHandlingPic", Boolean.valueOf(isUploadHandlingPic));
		
		String formUrl = eventDisposalWorkflowService.capWorkflowHandlePage(eventId, type, userInfo, null);
		
		return "/zzgl/event/workflow/" + formUrl;
	}
	
	/**
	 * 任务提交办理
	 * @param session
	 * @param taskId
	 * @param advice 办理意见
	 * @param nextNode 下一节点
	 * @param nextStaffId 下一办理人，短信发送的主送人
	 * @param deploymentId 流程发布ID
	 * @param sendSms 是否发送短信
	 * @param smsContent 短信内容
	 * @param otherMobileNums 额外的电话号码
	 * @param instanceId 实例ID
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/submitInstanceForEventPC")
	public Map<String, Object> submitInstanceForEventPC(
			HttpSession session,
			@RequestParam(value = "taskId") String taskId,
			@RequestParam(value = "advice") String advice,
			@RequestParam(value = "nextNode") String nextNode,
			@RequestParam(value = "nextStaffId", required = false) String nextStaffId,
			@RequestParam(value = "deploymentId", required = false) Integer deploymentId,
			@RequestParam(value = "smsContent", required = false) String smsContent,
			@RequestParam(value = "otherMobileNums", required = false) String otherMobileNums,
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam(value = "curOrgIds", required=false) String curOrgIds,
			@RequestParam(value = "formId", required=false) Long eventId,
			@RequestParam(value = "evaLevel", required=false) String evaLevel,
			@RequestParam(value = "evaContent", required=false) String evaContent,
			@RequestParam(value = "attachmentId", required = false) String attachmentIds,
			@ModelAttribute(value = "event") EventDisposal event,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isDetail2Edit = false,
				isHandleEdit = false;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "isDetail2Edit")) {//更新事件属性
			isDetail2Edit = Boolean.valueOf(params.get("isDetail2Edit").toString());
		}
		
		if(CommonFunctions.isNotBlank(params, "isHandleEdit")) {//待办可编辑
			isHandleEdit = Boolean.valueOf(params.get("isHandleEdit").toString());
		}
		
		if(isDetail2Edit || isHandleEdit) {
			eventDisposalService.updateEventDisposal(event, params);
		}
		
		try {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

			UserBO user = new UserBO();
			user.setUserId(userInfo.getUserId());
			user.setPartyName(userInfo.getUserName());

			Boolean result = false;

			//构造评价参数
			if(eventId!=null && !eventId.equals(-1L) && !StringUtils.isEmpty(evaLevel) && !StringUtils.isEmpty(evaContent)){
				params.put("isEvaluate",true);
				params.put("eventId",eventId);
				params.put("evaLevel",evaLevel);
				params.put("evaContent",evaContent);
			}
			// 执行节点
			result = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(instanceId,taskId, nextNode, nextStaffId, curOrgIds, advice, userInfo, smsContent, params);
			if (result) {
				/*//保存评价信息
				if(eventId!=null && !eventId.equals(-1L) && !StringUtils.isEmpty(evaLevel) && !StringUtils.isEmpty(evaContent)){
					EvaResult evaResult = new EvaResult();
					evaResult.setObjectId(eventId);
					evaResult.setEvaObj(ConstantValue.EVA_OBJ_NEW_EVENT);
					evaResult.setEvaLevel(evaLevel);
					evaResult.setEvaContent(evaContent);
					evaResult.setCreateUser(userInfo.getUserId());
					evaResult.setCreatorName(userInfo.getOrgName()+"-"+userInfo.getPartyName());//思明区事件需要获取评价人部门  组织名称-人员名称
					evaResult.setUpdateUser(userInfo.getUserId());
					evaResult.setUpdaterName(userInfo.getPartyName());

					evaResultService.saveEvaResult(evaResult);
				}*/
				
				//保存图片
				if(eventId!=null && !eventId.equals(-1L) && !StringUtils.isEmpty(attachmentIds)){
					boolean b = attachmentService.updateBizId(eventId, ConstantValue.EVENT_ATTACHMENT_TYPE, attachmentIds);
				}
				
				//异步发送短信
				if(CommonFunctions.isBlank(params, "nextNodeName") && StringUtils.isNotBlank(nextNode)) {
					params.put("nextNodeName", nextNode);
				}
				
				eventDisposalWorkflowService.sendSms(nextStaffId, otherMobileNums, smsContent, userInfo, params);
				
				resultMap.put("result", "success");
			} else {
				resultMap.put("result", "error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "error");
			resultMap.put("msgWrong", e.getMessage());
		}
		return resultMap;
	}
	
	/**
	 * 任务驳回，返回到上一办理步骤，而不是整体流程的上一步
	 * @param session
	 * @param taskId
	 * @param advice		办理意见
	 * @param instanceId	实例ID
	 * @param params
	 * 			eventId			事件id
	 * 			attachmentIds	附件id
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rejectInstance")
	public Map<String, Object> rejectInstance(HttpSession session,
			@RequestParam(value = "taskId") String taskId,
			@RequestParam(value = "advice") String advice,
			@RequestParam(value = "instanceId") String instanceId,
			@RequestParam Map<String, Object> params,
			ModelMap map) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

			boolean result = eventDisposalWorkflowService.rejectWorkFlow(params, userInfo);

			if (result) {
				//保存图片
				if(CommonFunctions.isNotBlank(params, "eventId") && CommonFunctions.isNotBlank(params, "attachmentIds")) {
					Long eventId = -1L;
					String attachmentIds = params.get("attachmentIds").toString();
					
					try {
						eventId = Long.valueOf(params.get("eventId").toString());
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
					
					if(eventId > 0 && StringUtils.isNotBlank(attachmentIds)){
						attachmentService.updateBizId(eventId, ConstantValue.EVENT_ATTACHMENT_TYPE, attachmentIds);
					}
				}
				
				resultMap.put("result", "success");
			}else {
				resultMap.put("result", "error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "error");
			resultMap.put("msgWrong", e.getMessage());
		}
		return resultMap;
	}

	/**
	 * 流程环节撤回
	 * 要求撤回操作的用户是当前任务的上一任务办理人员，不只是经办人员
	 * 当前环节没有办理过，即没有被提交、驳回、反馈、处理中填写等，只是查看仍可撤回
	 * 
	 * @param session
	 * @param instanceId	流程实例id
	 * @param params		额外参数
	 * 
	 * @param map
	 * @return
	 * 			result			撤回结果，撤回成功，返回true；否则返回false
	 * 			msgWrong	撤回失败原因
	 */
	@ResponseBody
	@RequestMapping(value = "/recallWorkflow")
	public Map<String, Object> recallWorkflow(HttpSession session,
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean result = false;
		
		try {
			result = eventDisposalWorkflowService.recallWorkflow(instanceId, userInfo, params);
		} catch (Exception e) {
			resultMap.put("msgWrong", e.getMessage());
			e.printStackTrace();
		}
		
		resultMap.put("result", result);
		
		return resultMap;
	}
	
	/**
	 * 获取处理环节数据
	 * @param session
	 * @param instanceId
	 * @param map
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/flowData")
	public Map<String, Object> flowData(HttpSession session,
							 @RequestParam(value = "instanceId") Long instanceId,
							 ModelMap map){

		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<Map<String, Object>> queryTaskList = eventDisposalWorkflowService.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
		resultMap.put("taskList", queryTaskList);
		return resultMap;
	}

	/**
	 * 获取处理环节详情
	 * @param session
	 * @param instanceId 实例ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/flowDetail")
	public String flowDetail(HttpSession session,
			@RequestParam(value = "instanceId") Long instanceId, 
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean isTaskAllUnfolded = false;
		String isTaskAllUnfoldedStr = null;
		
		List<Map<String, Object>> queryTaskList = eventDisposalWorkflowService.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
		
		isTaskAllUnfoldedStr = funConfigurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.IS_TASK_ALL_UNFOLDED, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(isTaskAllUnfoldedStr)) {
			isTaskAllUnfolded = Boolean.valueOf(isTaskAllUnfoldedStr);
		}
		
		map.addAttribute("taskList", queryTaskList);
		map.addAttribute("isTaskAllUnfolded", isTaskAllUnfolded);
		
		return "/zzgl/event/workflow/detail_workflow.ftl";
	}
	
	/**
	 * 获取短信发送信息
	 * @param session
	 * @param eventId		事件id
	 * @param taskId		任务id
	 * @param curNodeName	当前环节名称
	 * @param nextNodeName	下一环节名称
	 * @param params		额外参数
	 * 			receiverIds	短信接收人员id，以英文逗号分隔
	 * @param map
	 * @return
	 * 		smsContent		短信内容
	 * 		msgWrong		异常信息
	 */
	@ResponseBody
	@RequestMapping(value = "/eventNoteContent", method = RequestMethod.POST)
	public Map<String, Object> eventNoteContent(HttpSession session,
			@RequestParam(value = "formId", required=false) Long eventId,
			@RequestParam(value = "taskId", required=false) String taskId,
			@RequestParam(value = "curnodeName", required=false) String curNodeName,
			@RequestParam(value = "nodeName", required=false) String nextNodeName,
			@RequestParam Map<String, Object> params,
			ModelMap map){
		UserInfo userInfo = (UserInfo)session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(CommonFunctions.isBlank(params, "eventId")) {
			params.put("eventId", eventId);
		}
		
		try {
			resultMap = eventDisposalWorkflowService.capInfo4SMS(curNodeName, nextNodeName, params, userInfo);
		} catch (Exception e) {
			resultMap.put("msgWrong", e.getMessage());
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	/**
	 * 提交下一节点
	 * @param session
	 * @param taskId
	 * @param advice
	 * @param nextNode
	 * @param nextStaffId
	 * @param deploymentId
	 * @param freeJumpCheck
	 * @param decision
	 * @param decisionNextNode
	 * @param forkUserIds
	 * @param sendSms
	 * @param notifyUserIds
	 * @param notifyUserNames
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/submitInstance")
	public Map<String, Object> submitInstance(
			HttpSession session,
			@RequestParam(value = "taskId") String taskId,
			@RequestParam(value = "advice") String advice,
			@RequestParam(value = "nextNode") String nextNode,
			@RequestParam(value = "nextStaffId", required = false) String nextStaffId,

			@RequestParam(value = "deploymentId") Integer deploymentId,
			@RequestParam(value = "freeJumpCheck") String freeJumpCheck,

			@RequestParam(value = "decision") String decision,
			@RequestParam(value = "decisionNextNode") String decisionNextNode,

			@RequestParam(value = "forkUserIds", required = false) String forkUserIds,
			@RequestParam(value = "sendSms") String sendSms,
			
			@RequestParam(value = "notifyUserIds", required = false) String notifyUserIds,
			@RequestParam(value = "notifyUserNames", required = false) String notifyUserNames,
			ModelMap map) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			OrgSocialInfoBO org = orgSocialInfoService.findByOrgId(userInfo.getOrgId());

			UserBO user = new UserBO();
			user.setUserId(userInfo.getUserId());
			user.setPartyName(userInfo.getUserName());

			Boolean result = false;

			if (decision.equals("1") && !decisionNextNode.equals("")) {
				// 决策节点
				result = hessianFlowService.subWorkFlow(taskId, nextNode,
						nextStaffId, null, null, null, advice, user, org, true,sendSms,notifyUserIds,notifyUserNames);
			} else {
				if (freeJumpCheck.equals("checked")) {
					// 动态跳转
					result = hessianFlowService.subWorkFlow(taskId, nextNode,
							nextStaffId, null, null, null, advice, user, org, true,sendSms,notifyUserIds,notifyUserNames);
				} else {
					// 执行节点
					result = hessianFlowService.subWorkFlow(taskId, nextNode,
							nextStaffId, null, forkUserIds, null, advice, user, org, false,sendSms,notifyUserIds,notifyUserNames);
				}
			}
			if (result) {
				resultMap.put("result", "success");
				return resultMap;
			} else {
				resultMap.put("result", "error");
				return resultMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "error");
			return resultMap;
		}
	}
	
	/**
	 * 根据流程定义ID和环节名称获取环节信息
	 * @param session
	 * @param request
	 * @param deploymentId
	 * @param nodeName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getNode")
	public Map<String, Object> getNode(
			@RequestParam(value = "deploymentId") Integer deploymentId,
			@RequestParam(value = "nodeName") String nodeName) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Node node = hessianFlowService.getNodeByNodeNameAndDeploymentId(deploymentId, nodeName);
			resultMap.put("dynamicSelect", node.getDynamicSelect());
			resultMap.put("nodeId", node.getNodeId());
			resultMap.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
		}
		return resultMap;
	}

	/**
	 * 根据流程定义ID和节点名称获取节点参与者列表
	 * @param session
	 * @param request
	 * @param deploymentId
	 * @param nodeName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/hasNodeActor")
	public Map<String, Object> hasNodeActor(
			@RequestParam(value = "deploymentId") Integer deploymentId,
			@RequestParam(value = "nodeName") String nodeName) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Boolean flag = hessianFlowService.hasNodeActorByNodeNameAndDeploymentId(deploymentId,nodeName);
			resultMap.put("result", flag + "");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "error");
		}

		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "/getNodeActor")
	public String getNodeActor(HttpServletRequest request,
			@RequestParam(value = "nodeId") Integer nodeId,
			@RequestParam(value = "opType", required = false) String opType) {

		try {
			List<Map<String, Object>> resultMap = null;
			if(opType!=null&&opType.equals("notify")){
				UserInfo userInfo = (UserInfo) request.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
				OrgSocialInfoBO org = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
				if(org!=null&&org.getParentOrgId()!=null&&org.getParentOrgId()!=-1){
					org = orgSocialInfoService.findByOrgId(org.getParentOrgId());
				}
				if(org!=null){
					resultMap = new ArrayList<Map<String,Object>>();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("ACTOR_TYPE", "1");
					map.put("ACTOR_ID", org.getOrgId());
					map.put("ACTOR_NAME", org.getOrgName());
					
					resultMap.add(map);
				}
			}else{
				resultMap = hessianFlowService.getNodeActors(nodeId);
			}
			return hessianFlowService.getJsonFromObjectToString(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/getOrgTree")
	public String getOrgTree(@RequestParam(value = "id", required = false) Long id) {

		String orgs = "";
		try {
			orgs = hessianFlowService.orgTree(id);
		} catch (Exception e) {
			orgs = "";
			e.printStackTrace();
		}
		return orgs;
	}

	@ResponseBody
	@RequestMapping(value = "/getUserList")
	public EUDGPagination getUserList(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "type") String type) {

		try {
			return hessianFlowService.userList(id, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getNodeActorForEvent")
	public String getNodeActorForEvent(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "nodeId") String nodeId,
			@RequestParam(value = "curnodeName") String curnodeName,
			@RequestParam(value = "nodeName") String nodeName) {

		List<Map<String, Object>> resultMap = null;
		String actors = null;
		try {
			resultMap = new ArrayList<Map<String,Object>>();
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			OrgSocialInfoBO org = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
			// 根据分流，上报，越级来决定组织机构
			Map<String, Object> LevelAndPositonName = getTurnLevel(curnodeName, nodeName, userInfo);
			WorkflowController.level = (Integer) LevelAndPositonName.get("level");
			if (level == 1) {
				OrgSocialInfoBO tempOrg = org;
				org = orgSocialInfoService.findByOrgId(org.getParentOrgId());
				if(org == null){
					System.out.println("============================不存在父组织，只能取当前组织");
					org = tempOrg;
				}
				String positionName = (String) LevelAndPositonName.get("positionName");
				
				List<UserBO> users = (ArrayList<UserBO>)userManageService.queryUserListByPositionAndOrg(positionName, null, "0", org.getOrgId(), null);
				
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("ACTOR_TYPE", "4");
				tempMap.put("ACTOR_ID", positionName);
				tempMap.put("ACTOR_NAME", positionName);
				/*tempMap.put("ACTOR_TYPE", "1");
				tempMap.put("ACTOR_ID", org.getOrgId());
				tempMap.put("ACTOR_NAME", org.getOrgName());*/
				resultMap.add(tempMap);
			} else if (level == 0) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("ACTOR_TYPE", "1");
				tempMap.put("ACTOR_ID", org.getOrgId());
				tempMap.put("ACTOR_NAME", org.getOrgName());
				resultMap.add(tempMap);
			} else if (level == 2) {
				OrgSocialInfoBO tempOrg = org;
				org = orgSocialInfoService.findByOrgId(org.getParentOrgId());
				if(org != null){
					tempOrg = org;
					org = orgSocialInfoService.findByOrgId(org.getParentOrgId());
					if(org == null){
						org = tempOrg;
					}
				}else{
					org = tempOrg;
				}
				
				Map<String, Object> tempMap = new HashMap<String, Object>();
//				tempMap.put("ACTOR_TYPE", "4");
//				UserCookie userCookie = (UserCookie) session.getAttribute(ConstantValue.USER_COOKIE_IN_SESSION);
//				// 职位ID需要根据“综治员”职位ID再更改
//				Long positionId = userCookie.getPositionId();
//				tempMap.put("ACTOR_ID", positionId);
//				tempMap.put("ACTOR_NAME", "综治员");
				tempMap.put("ACTOR_TYPE", "1");
				tempMap.put("ACTOR_ID", org.getOrgId());
				tempMap.put("ACTOR_NAME", org.getOrgName());
				resultMap.add(tempMap);
			} else {
				org = orgSocialInfoService.findByOrgId(org.getParentOrgId());
				
				resultMap = hessianFlowService.getNodeActors(Integer.parseInt(nodeId));
			}
//			org =  orgSocialInfoService.findById(org.getParentOrgId());
			
//			resultMap = hessianFlowService.getNodeActors(Integer
//					.parseInt(nodeId));
			actors = hessianFlowService.getJsonFromObjectToString(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actors;
	}
	
	/**
	 * 针对事件流程，判断当前节点和下一个节点之间是属于什么关系类型
	 * @param curnodeName
	 * @param nodeName
	 * @return 0-分流， 1-上报，2-越级
	 */
	private Map<String, Object> getTurnLevel(String curnodeName, String nextNodeName, UserInfo userInfo) {
		return eventDisposalWorkflowService.capTurnLevel(curnodeName, nextNodeName, userInfo);
	}
	
	@ResponseBody
	@RequestMapping("/searchUser")
	public List<UserInfo> searchUser(HttpSession session,
			@RequestParam(value = "inputName") String inputName,
			@RequestParam(value = "curnodeName") String curnodeName,
			@RequestParam(value = "nodeName") String nodeName) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String searchOrgCode = userInfo.getOrgCode();
		// 根据分流，上报，越级来决定组织机构
		int level = (Integer) getTurnLevel(curnodeName, nodeName, userInfo).get("level");
		if (level == 1) {
				OrgSocialInfoBO socialOrg = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
				if (Integer.parseInt(socialOrg.getChiefLevel()) > 1) {
					socialOrg = orgSocialInfoService.findByOrgId(socialOrg.getParentOrgId());
					searchOrgCode = socialOrg.getOrgCode();
				}
		} else if (level == 2) {
			OrgSocialInfoBO socialOrg = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
			if (Integer.parseInt(socialOrg.getChiefLevel()) > 1) {
				socialOrg = orgSocialInfoService.findByOrgId(socialOrg.getParentOrgId());
				searchOrgCode = socialOrg.getOrgCode();
			}
			if (Integer.parseInt(socialOrg.getChiefLevel()) > 1) {
				socialOrg = orgSocialInfoService.findByOrgId(socialOrg.getParentOrgId());
				searchOrgCode = socialOrg.getOrgCode();
			}
		}
		List<UserInfo> userList = userService.searchUsersByName(searchOrgCode, inputName);
		if(userList!=null && userList.size()>10) {
			userList = userList.subList(0, 10);
		}
		return userList;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getOrgTreeForEvent")
	public List<Map<String, Object>> getOrgTreeForEvent(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "curnodeName") String curnodeName,
			@RequestParam(value = "nodeName") String nodeName) {
		UserInfo userInfo = (UserInfo)session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<Map<String, Object>> resultList = null;
		String orgs = "";
		
		// 根据分流，上报，越级来决定组织机构
		if(id != null){
			int level = (Integer) getTurnLevel(curnodeName, nodeName, userInfo).get("level");
			if (level == 0) {
				try {
					orgs = hessianFlowService.orgTree(id);
				} catch (Exception e) {
					orgs = "";
					e.printStackTrace();
				}
				
				if(StringUtils.isNotBlank(orgs)) {
					resultList = JsonHelper.getObjectList(orgs, Map.class);//转换为list是为了修复返回类型为String时的中文乱码问题
				}
			} 
		}
		
		return resultList;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getUserListForEvent")
	public EUDGPagination getUserListForEvent(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "type") String type) {

		EUDGPagination pagination = null;
		
		if (id != null) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			OrgSocialInfoBO org = getOrgByLevel(userInfo, level);
					//orgSocialInfoService.findById(userInfo.getOrgId());
			String positionName = funConfigurationService.turnCodeToValue(ConstantValue.EVENT_WORFLOW_POSITIONNAME, ConstantValue.EVENT_WORFLOW_POSITIONNAME, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			if (level == 1 || level == 2) {
				List<UserBO> users = (ArrayList<UserBO>)userManageService.queryUserListByPositionAndOrg(positionName, null,"0", org.getOrgId(), null);
				pagination = new EUDGPagination(users.size(), users);
			} else {
				try {
					pagination = hessianFlowService.userList(id, type);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return pagination;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getUsersForEvent")
	public Map<String, Object> getUsersForEvent(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "curnodeName") String curnodeName,
			@RequestParam(value = "nodeName") String nodeName) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> levelAndPostionName = getTurnLevel(curnodeName, nodeName, userInfo);
		WorkflowController.level = (Integer) levelAndPostionName.get("level");
		
		if(level==1 || level==2){//level 1 上报； 2 越级上报
			// 当前用户及其组织
			OrgSocialInfoBO org = getOrgByLevel(userInfo, level);//level为1时，org为上级；level为2时，org为越级上级
			StringBuffer userIds = new StringBuffer("");
			StringBuffer userNames = new StringBuffer("");
			StringBuffer orgIds = new StringBuffer("");
			Long orgId = null;
			if (org != null) {
				orgId = org.getOrgId();
			}
			
			String positionName = (String) levelAndPostionName.get("positionName");
			List<UserBO> users = (ArrayList<UserBO>)userManageService.queryUserListByPositionAndOrg(positionName, null,"0", org.getOrgId(), null);
			if (users!=null && users.size()>0) {
				StringBuffer userIdTemp = new StringBuffer("");
				for (UserBO user : users) {
					userIdTemp = new StringBuffer("").append(user.getUserId()).append(",");
					if(userIds.indexOf(userIdTemp.toString()) == -1){//排除重复的用户
						userIds.append(userIdTemp);
						userNames.append(user.getPartyName()).append(",");
						orgIds.append(orgId).append(",");
					}
				}
				
				if (userNames!=null && userNames.length()>0) {
					userNames = new StringBuffer(userNames.substring(0, userNames.length() - 1));
				}			
				resultMap.put("level", level);
				resultMap.put("userIds", userIds);
				resultMap.put("userNames", userNames);
				resultMap.put("orgIds", orgIds);
				
			} else {
				resultMap.put("msg", "职位未配置相关人员");
			}
		}else if (level == 7) {
			resultMap.put("level", level);
			resultMap.put("userIds", userInfo.getUserId());
			resultMap.put("userNames", userInfo.getUserName());
			resultMap.put("orgIds", userInfo.getOrgId());
		} else { // 分流
			resultMap.put("level", "0");
		}
		
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getUserListByOrgId")
	public EUDGPagination getUserListByOrgId(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "id", required = false) Long id) {

		EUDGPagination pagination = null;
		try {
			pagination = hessianFlowService.userListByOrgId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}
	
	private OrgSocialInfoBO getOrgByLevel(UserInfo userInfo, int level) {
		// 当前用户及其组织
		OrgSocialInfoBO org = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
		if (level == 1) {// 上报
			// 父组织
			try {
				org = orgSocialInfoService.findByOrgId(org.getParentOrgId());
			} catch (Exception e) {
				System.out.println("============================不存在父组织，只能取当前组织");
				org = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
			}
			return org;
		} else if (level == 2) {// 越级 
			// 父组织
			try {
				org = orgSocialInfoService.findByOrgId(org.getParentOrgId());
				try {
					// 父组织的父组织
					org = orgSocialInfoService.findByOrgId(org.getParentOrgId());
				} catch (Exception e) {
					System.out.println("============================不存在父组织的父组织，只能取当前组织");
					org = orgSocialInfoService.findByOrgId(org.getOrgId());
				}
			} catch (Exception e) {
				System.out.println("============================不存在父组织，只能取当前组织");
				org = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
			}
			return org;
		} else { 
			return org;
		}
	}
	
	/**
	 * 办理于提交统一接口
	 * @param session
	 * @param request
	 * @param instanceId
	 * @param workFlowId
	 * @param eventType
	 * @param taskId
	 * @param sendSms
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/submitInstanceForEvent")
	public Map<String, Object> submitInstanceForEvent(
			HttpSession session, HttpServletRequest request,
			@RequestParam(value = "instanceId") String instanceId,
			@RequestParam(value = "workFlowId") Long workFlowId,
			@RequestParam(value = "eventType", required = false) String eventType,
			@RequestParam(value = "taskId", required = false) String taskId,
			@RequestParam(value = "sendSms", required = false) String sendSms) {
		
		// jumpPage
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(ConstantValue.USER_IN_SESSION);

		if(taskId==null||taskId.equals("")){
			List<Map<String, Object>> m = hessianFlowService.getTaskIdByInstanceId(instanceId);
			BigDecimal s = (BigDecimal) (m.get(0).get("DBID_"));
			taskId = s.toString();
		}
		
		Map<String, Object> tempMap = hessianFlowService.getDataByTaskId(taskId,workFlowId,instanceId);
		Node curNode = (Node) tempMap.get("curNode");
		String curnodeName = curNode.getNodeName();
		
		// subinstance
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			OrgSocialInfoBO org = orgSocialInfoService.findByOrgId(userInfo.getOrgId());

			UserBO user = new UserBO();
			user.setUserId(userInfo.getUserId());
			user.setPartyName(userInfo.getUserName());

			Boolean result = false;
			// 获取下一个办理节点
			String nextNode = getNextNameForEvent(curnodeName, 1);
			// 获取下一个办理节点办理人的组织
			OrgSocialInfoBO searchOrg = getOrgByLevel(userInfo, level);
			// 获取下一个节点办理人
			String forkUserIds = "";
			String positionName = funConfigurationService.turnCodeToValue(ConstantValue.EVENT_WORFLOW_POSITIONNAME, ConstantValue.EVENT_WORFLOW_POSITIONNAME, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			List<UserBO> users = (ArrayList<UserBO>)userManageService.queryUserListByPositionAndOrg(positionName, null,"0", searchOrg.getOrgId(), null);
			if (users!=null && users.size()!=0) {
				for (int i = 0; i < users.size(); i++) {
					UserBO u = users.get(i);
					forkUserIds = u.getUserId() + "," + forkUserIds;
				}
			} else {
				// 找不到人员就不进行下个环节办理
				resultMap.put("result", "error");
				return resultMap;
			}
			System.out.println("-------------------下个环节默认办理人------------------");
			System.out.println(forkUserIds);
			System.out.println("---------------------------------------------------");
			// 执行节点
			result = hessianFlowService.subWorkFlow(taskId, nextNode,
					forkUserIds, null, null, null , "", user, org, false, sendSms,null,null);
			if (result) {
				resultMap.put("result", "success");
				return resultMap;
			} else {
				resultMap.put("result", "error");
				return resultMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "error");
			return resultMap;
		}
	}
	
	// 根据当前办理节点名称和办理类型(1-上报，2-越级，0-分流)获取下个办理节点
	private String getNextNameForEvent(String curNodeName, int level) {
		return eventDisposalWorkflowService.capNextNodeName(curNodeName, level);
	}
	
	
	@ResponseBody
	@RequestMapping("/fastSearchUser")
	public List<UserInfo> fastSearchUser(HttpSession session,
			@RequestParam(value = "inputName") String inputName,
			@RequestParam(value = "searchIds") String searchIds,
			@RequestParam(value = "searchType") String searchType) {
		List<UserInfo> uList = new ArrayList<UserInfo>();
		if (!"".equals(searchIds.trim()) && !"".equals(searchType.trim())
				&& !"undefinded".equals(searchType) && !"undefinded".equals(searchIds)) {
			searchIds = searchIds.substring(0, (searchIds.length() - 1));
			String[] searchIdsArr = searchIds.split(",");
			if (searchType.equals("1")) {// 组织
				for (String searchId : searchIdsArr) {
					OrgSocialInfoBO org = orgSocialInfoService.findByOrgId(Long.parseLong(searchId));
					List<UserInfo> uTempList = (ArrayList<UserInfo>) userService.searchUsersByName(org.getOrgCode(), inputName);
					if (uTempList!=null && uTempList.size()!=0) {
						uList.addAll(uTempList);
					}
				}
			} else if (searchType.equals("2")) {// 角色
				
			} else if (searchType.equals("3")) {// 用户

			} else if (searchType.equals("4")) {// 职位
				
			} else {
				
			}
		}
		// 只展示10条记录
		if(uList!=null && uList.size()>10) {
			uList = uList.subList(0, 10);
		}
		return uList;
	}
}
