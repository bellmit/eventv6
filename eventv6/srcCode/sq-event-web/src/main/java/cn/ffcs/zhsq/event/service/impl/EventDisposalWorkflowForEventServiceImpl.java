package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.sms.bo.SendResult;
import cn.ffcs.sms.service.SendSmsService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.common.Constants;
import cn.ffcs.workflow.om.Forms;
import cn.ffcs.workflow.om.MyTask;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.NodeTransition;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.om.Remind;
import cn.ffcs.workflow.om.TaskReceive;
import cn.ffcs.workflow.om.Workflow;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExpand.service.IEventDisposal4ExpandService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(value = "eventDisposalWorkflowForEventService")
public class EventDisposalWorkflowForEventServiceImpl implements IEventDisposalWorkflowService {

	@Autowired
	private HessianFlowService hessianFlowService;
	
	//组织域信息接口
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	//短信发送
	@Autowired
	private SendSmsService sendSmsService;
	
	//功能配置接口
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService; 
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;

	@Autowired
	private IBaseWorkflowService baseWorkflowService;

	//事件扩展信息接口
	@Autowired
	private IEventDisposal4ExpandService eventDisposalExpandService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final String VALIDATE_LENGTH_TASK_ADVICE = "taskAdvice";	//数据字段长度验证 任务办理意见
	private final String GRID_INFO_ORG_CHIEF_LEVEL = "6";//组织 网格层级
	
	public static final String TASK_TYPE_ORDINARY = "1";				//普通任务
	public static final String TASK_TYPE_COUNTERSIGNATURE = "2";		//会签任务
	public static final String TASK_OPERATE_TYPE_PASS = "1";			//通过操作
	public static final String TASK_OPERATE_TYPE_REJECT = "2";			//驳回操作
	private final String ORG_TYPE_UNIT = "1";							//组织类型 单位
	private final String ORG_TYPE_DEPARTMENT = "0";						// 组织类型 部门
	
	//可移除的环节编码 流程开始、结案
	private final String[] REMOVE_NODE_CODE = {"KS", "JA"};
	
	@Override
	public String startFlowByWorkFlow(Long workFlowId, Integer eventId,
			String wftypeId, UserBO user, OrgSocialInfoBO org,
			Map<String, Object> userMap) throws Exception {
		String instanceId = null;
		
		if(eventId == null || eventId < 0) {
			throw new IllegalArgumentException("缺少有效的事件id！");
		}
		
		instanceId = this.capInstanceIdByEventId(Long.valueOf(eventId));
		
		if(StringUtils.isBlank(instanceId)) {
			if(isUserAbleToStartWorkflow(org, user.getUserId())){
				instanceId = hessianFlowService.startFlowByWorkFlow(workFlowId, eventId, wftypeId, user, org, userMap);
			}
		}
		
		return instanceId;
	}

	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo) throws Exception {
		return this.startFlowByWorkFlow(eventId, workFlowId, toClose, userInfo, null);
	}
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String wftypeId = ConstantValue.EVENT_WORKFLOW_WFTYPEID;
		UserBO user = new UserBO();
		OrgSocialInfoBO orgInfo = null;
		Map<String, Object> userMap = null;
		Integer formId = 0;
		Long orgId = -1L;
		Long userId = -1L;
		boolean isSendEvaUser = false;
		
		if(userInfo != null){
			orgId = userInfo.getOrgId();
			userId = userInfo.getUserId();
			user.setUserId(userId);
			user.setPartyName(userInfo.getPartyName());
			orgInfo = orgSocialInfoService.findByOrgId(orgId);
		}
		
		if(workFlowId==null || workFlowId.equals(-1L)){
			workFlowId = this.capWorkflowId(null, eventId, userInfo, null);
		}
		if(eventId != null){
			formId = Integer.valueOf(eventId.toString());
		}
		if(StringUtils.isBlank(toClose)){
			toClose = ConstantValue.WORKFLOW_IS_NO_CLOSE;
		}
		if(orgInfo != null){
			userMap = new HashMap<String, Object>();
			userMap.put("orgCode", orgInfo.getOrgCode()); 
			userMap.put("orgType", orgInfo.getOrgType()); //组织类型
			
			StringBuffer parentOrgId = new StringBuffer("");
			Map<String, String> gridOrgUser = new HashMap<String, String>();
			//查找父级
			List<OrgSocialInfoBO> parentOrgInfo = new ArrayList<OrgSocialInfoBO>();
			Map<String, Object> parentOrgExtraParam = new HashMap<String, Object>();
			parentOrgExtraParam.put("eventId", eventId);
			
			try {
				parentOrgInfo = fiveKeyElementService.findOrgByLevel(orgId, IFiveKeyElementService.PARENT_LEVEL_ONE, ConstantValue.GOV_PROFESSION_CODE, "", userInfo, parentOrgExtraParam);
			} catch (Exception e) {
				parentOrgInfo = new ArrayList<OrgSocialInfoBO>();
				e.printStackTrace();
			}
			
			isSendEvaUser = fiveKeyElementService.isUserIdGridAdmin(orgId, userId);//当当前用户为网格员时，传递可评价人员信息
			
			for(OrgSocialInfoBO orgSocialInfoBO : parentOrgInfo) {
				if(orgSocialInfoBO != null){
					parentOrgId.append(",").append(orgSocialInfoBO.getOrgId());
					
					if(isSendEvaUser){//当当前用户为网格员时，传递可评价人员信息
						try {
							List<UserBO> userBoList = fiveKeyElementService.findCollectToCloseUserList(orgSocialInfoBO.getOrgId());
							if(userBoList!=null && userBoList.size()>0){
								StringBuffer gridUserIds = new StringBuffer("");
								
								for(UserBO userBO : userBoList){
									gridUserIds.append(userBO.getUserId()).append(",");
								}
								
								if(gridUserIds.length() > 0){
									gridUserIds = new StringBuffer(gridUserIds.subSequence(0, gridUserIds.length()-1));
									gridOrgUser.put(String.valueOf(orgSocialInfoBO.getOrgId()), gridUserIds.toString());
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if(parentOrgId.length() > 0) {
				parentOrgId = new StringBuffer(parentOrgId.substring(1));
			}
			
			if(isSendEvaUser) {
				userMap.put("gridOrgUser", gridOrgUser);//可评价人员信息 Map<orgId, userId>
			} else {
				userMap.put("parentOrgId", parentOrgId.toString());//上级组织id，以英文逗号相连接
			}
		}
		
		userMap.put("toClose", toClose);
		userMap.put("isGridAdmin", fiveKeyElementService.isUserIdGridAdminForString(isSendEvaUser));
		userMap.put("isNewOrganization", eventOrgInfoService.isNewOrganizationForString(orgInfo));
		userMap.put("orgChiefLevel", orgInfo.getChiefLevel());
		userMap.put("isGovernment", eventOrgInfoService.isGovernmentForString(orgInfo));
		
		if(extraParam != null) {
			userMap.putAll(extraParam);
		}
		
		return this.startFlowByWorkFlow(workFlowId, formId, wftypeId, user, orgInfo, userMap);
	}
	
	@Override
	public String startEndWorkflowForEvent(Long eventId, Long workFlowId, UserInfo userInfo) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		OrgSocialInfoBO orgInfo = null;
		UserBO user = new UserBO();
		String instanceId = "";
		Long orgId = -1L;
		Long userId = -1L;
		
		if(workFlowId==null || workFlowId.equals(-1L)){
			workFlowId = this.capWorkflowId(null, eventId, userInfo, null);
		}
		if(userInfo != null){
			orgId = userInfo.getOrgId();
			userId = userInfo.getUserId();
			user.setUserId(userId);
			user.setPartyName(userInfo.getPartyName());
			orgInfo = orgSocialInfoService.findByOrgId(orgId);
		}
		if(orgInfo != null){
			params.put("orgCode", orgInfo.getOrgCode()); 
			params.put("orgType", orgInfo.getOrgType());
			
			StringBuffer parentOrgId = new StringBuffer("");
			//查找父级
			List<OrgSocialInfoBO> orgInfoList = new ArrayList<OrgSocialInfoBO>();
			Map<String, Object> extraParam = new HashMap<String, Object>();
			extraParam.put("eventId", eventId);
			
			try {
				orgInfoList = fiveKeyElementService.findOrgByLevel(orgId, IFiveKeyElementService.PARENT_LEVEL_ONE, ConstantValue.GOV_PROFESSION_CODE, "", userInfo, extraParam);
			} catch (Exception e) {
				orgInfoList = new ArrayList<OrgSocialInfoBO>();
				e.printStackTrace();
			}
			
			for(OrgSocialInfoBO orgSocialInfoBO : orgInfoList) {
				if(orgSocialInfoBO != null){
					parentOrgId.append(",").append(orgSocialInfoBO.getOrgId());
				}
			}
			
			if(parentOrgId.length() > 0){
				params.put("parentOrgId", parentOrgId.substring(1));
			}
			
			params.put("org", orgInfo);
		}
		
		params.put("toClose", ConstantValue.WORKFLOW_IS_TO_COLSE);
		params.put("workFlowId", workFlowId);
		params.put("formId", eventId);
		params.put("wftypeId", ConstantValue.EVENT_WORKFLOW_WFTYPEID);
		params.put("user", user);
		params.put("isGridAdmin", fiveKeyElementService.isUserIdGridAdminForString(orgId, userId));
		params.put("isNewOrganization", eventOrgInfoService.isNewOrganizationForString(orgInfo));
		params.put("orgChiefLevel", orgInfo.getChiefLevel());
		params.put("orgType", orgInfo.getOrgType());
		params.put("isGovernment", eventOrgInfoService.isGovernmentForString(orgInfo));
		
		if(isUserAbleToStartWorkflow(orgInfo, userId)){
			instanceId = hessianFlowService.startEndWorkflowForEvent(params);
		}
		
		return instanceId;
	}
	
	@Override
	public String startEndWorkflowForEvent(Map<String, Object> eventMap) throws Exception{
		UserInfo userInfo = null;
		Long workFlowId = -1L;
		Long eventId = -1L;
		
		if(CommonFunctions.isNotBlank(eventMap, "userInfo")){//json转换用户对象
			userInfo = (UserInfo)eventMap.get("userInfo");
		}
		if(CommonFunctions.isNotBlank(eventMap, "eventId")){//json转换用户对象
			eventId = Long.valueOf(eventMap.get("eventId").toString());
		}
		if(CommonFunctions.isNotBlank(eventMap, "workFlowId")){//json转换用户对象
			workFlowId = Long.valueOf(eventMap.get("workFlowId").toString());
		}else{
			workFlowId = this.capWorkflowId(null, eventId, userInfo, null);
		}
		
		return this.startEndWorkflowForEvent(eventId, workFlowId, userInfo);
	}
	
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		Long workFlowId = this.capWorkflowId(instanceId, null, userInfo, null);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> initResultMap = hessianFlowService.initFlowInfo(taskId, instanceId, workFlowId, null);
		
		if(initResultMap != null && !initResultMap.isEmpty()) {
			taskId = (String) initResultMap.get("taskId");
			List<OperateBean> operateLists = (List<OperateBean>) initResultMap.get("operateLists");
			List<Node> taskNodes = null;
			//工作流发布实例
			Integer deploymentId = (Integer) initResultMap.get("deploymentId");
			Node curNode = (Node) initResultMap.get("curNode");
			String workflowCtx = hessianFlowService.getWorkflowCtx();
			//workflowCtx = "http://192.168.22.25:8080/workflow";
			//获取流程实例
			ProInstance proInstance = (ProInstance) initResultMap.get("proInstance");
			Forms curForm = (Forms) initResultMap.get("curForm");
			// 是否跨域
			String formUrl = (String) initResultMap.get("formUrl");
			//任务描述
			String taskDesc = (String) initResultMap.get("taskDesc");
			// 上一个节点
			MyTask backTask = (MyTask) initResultMap.get("backTask");
			
			//移除不可见的办理环节
			/*if(proInstance != null && taskNodes!=null && taskNodes.size()>0){
				Long eventId = proInstance.getFormId();
				EventDisposal event = eventDisposalService.findEventById(eventId);
				if(event != null){
					String bizPlatform = event.getBizPlatform();
					bizPlatform = StringUtils.isBlank(bizPlatform) ? "" : bizPlatform; 
					String isRemoveNameStr = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.IS_REMOVE_NODE_NAME_TRIGGER+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
					String removeNodeNames = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.REMOVE_NODE_NAME_TRIGGER+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
					boolean isRemoveName = StringUtils.isBlank(isRemoveNameStr) || Boolean.valueOf(isRemoveNameStr);
					
					if(StringUtils.isBlank(removeNodeNames)){
						removeNodeNames = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.REMOVE_NODE_NAME_TRIGGER+"_", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
					}
					
					if(isRemoveName && StringUtils.isNotBlank(removeNodeNames)){
						String[] removeNodeNameAttr = removeNodeNames.split(",");
						String removeNodeName = "";
						
						for(int index = 0, len = removeNodeNameAttr.length; index < len; index++){
							removeNodeName = removeNodeNameAttr[index];
							for(Node node : taskNodes){
								if(removeNodeName.equals(node.getNodeName())){
									taskNodes.remove(node);break;
								}
							}
						}
					}
				}
			}*/
			
			if(CommonFunctions.isNotBlank(initResultMap, "taskNodes")) {
				boolean isShowHnadlingTask = true;//默认展示“处理中”办理环节
				String isShowHandlingTaskStr = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.IS_SHOW_HANDLING_TASK, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
				
				taskNodes = (List<Node>) initResultMap.get("taskNodes");
				
				if(StringUtils.isNotBlank(isShowHandlingTaskStr)) {
					isShowHnadlingTask = Boolean.valueOf(isShowHandlingTaskStr);
				}
				
				if(isShowHnadlingTask) {
					if(taskNodes != null) {
						if(proInstance != null) {
							Long eventId = proInstance.getFormId();
							EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
							if(event != null) {
								String status = event.getStatus();
								
								//增添“处理中”办理环节
								if(ConstantValue.EVENT_STATUS_REPORT.equals(status) || ConstantValue.EVENT_STATUS_DISTRIBUTE.equals(status)) {
									Node node = new Node();
									node.setNodeId(-1);
									node.setNodeName(ConstantValue.HANDLING_TASK_CODE);
									node.setNodeNameZH(ConstantValue.HANDLING_TASK_NAME);//环节中文名称
									node.setDynamicSelect("1");//动态跳转
									node.setNodeType("1");
									node.setTransitionCode(null);//设置为采集人办理
									
									taskNodes.add(node);
								}
							}
						}
					}
				}
			}
			
			resultMap.put("taskId", taskId);
			resultMap.put("operateLists", operateLists);
			resultMap.put("instanceId", instanceId);
			resultMap.put("taskNodes", taskNodes);
			resultMap.put("deploymentId", deploymentId);
			resultMap.put("curNodeId", curNode.getNodeId());
			resultMap.put("curNodeName", curNode.getNodeName());//当前办理环节名称
			resultMap.put("curNodeFormTypeId", curNode.getFormTypeId());
			resultMap.put("curNode", curNode);
			resultMap.put("workflowCtx", workflowCtx);//工作流域名访问前缀
			resultMap.put("workFlowId", workFlowId);
			resultMap.put("backTask", backTask);
			
			resultMap.put("proInstance", proInstance);//获取事件发起人
			resultMap.put("curForm", curForm);
			resultMap.put("formUrl", formUrl+"&isCrossdomain=0");
			resultMap.put("taskDesc", taskDesc);
		}
		
		return resultMap;
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo) throws Exception{
		return eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, "false", null);
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{
		boolean result = false;
		
		if(this.isUserInfoCurrentUser(taskId, instanceId, userInfo)) {
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
			UserBO user = new UserBO();
			Long eventId = -1L;
			
			user.setUserId(userInfo.getUserId());
			user.setPartyName(userInfo.getPartyName());
			
			ProInstance proInstance = hessianFlowService.getProByInstanceId(instanceId);
			if(proInstance != null){
				eventId = proInstance.getFormId();
			}
			
			if(ConstantValue.HANDLING_TASK_CODE.equals(nextNodeName)) {
				/**
				 * taskId 任务ID
				 * duedate 环节处理时限
				 */
				Map<String, Object> resultMap = saveHandlingTask(eventId, instanceId, nextNodeName, advice, userInfo,extraParam);
				
				result = resultMap != null;
				
			} else {
				result = hessianFlowService.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, null, advice, user, orgInfo, false, smsContent);
			}
			
			if(result){
				//保存办理意见
				if(eventId !=null && eventId > 0 && StringUtils.isNotBlank(advice)) {
					EventDisposal eventTmp = new EventDisposal();
					eventTmp.setEventId(eventId);
					eventTmp.setResult(advice);
					
					eventDisposalService.updateEventDisposalById(eventTmp);
				}

				//保存评价信息
				this.saveOrUpdateEventEvaluate(eventId,proInstance,userInfo,extraParam);
			}
		}
		
		return result;
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Map<String, Object> params, UserInfo userInfo) throws Exception {
		Long eventId = -1L, instanceId = -1L, curTaskId = -1L;
		String nextNodeName = "", nextUserIds = "", nextOrgIds = "", 
			   advice = "", smsContent = "";
		boolean flag = false;
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(params, "curTaskId")) {
			try {
				curTaskId = Long.valueOf(params.get("curTaskId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(params, "nextNodeName")) {
			nextNodeName = params.get("nextNodeName").toString();
		}
		if(CommonFunctions.isNotBlank(params, "nextUserIds")) {
			nextUserIds = params.get("nextUserIds").toString();
		}
		if(CommonFunctions.isNotBlank(params, "nextOrgIds")) {
			nextOrgIds = params.get("nextOrgIds").toString();
		}
		if(CommonFunctions.isNotBlank(params, "advice")) {
			advice = params.get("advice").toString();
		}
		if(CommonFunctions.isNotBlank(params, "smsContent")) {
			smsContent = params.get("smsContent").toString();
		}
		
		if(instanceId < 0 && CommonFunctions.isNotBlank(params, "eventId")) {
			try {
				eventId = Long.valueOf(params.get("eventId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
			if(eventId > 0) {
				instanceId = this.capInstanceIdByEventIdForLong(eventId);
			}
		}
		if(curTaskId < 0 && instanceId > 0) {
			curTaskId = this.curNodeTaskId(instanceId);
		}
		
		if(instanceId > 0 && StringUtils.isNotBlank(nextNodeName)) {
			flag = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(instanceId, curTaskId.toString(), nextNodeName, nextUserIds, nextOrgIds, advice, userInfo, smsContent, params);
		}
		
		return flag;
	}
	
	@Override
	public boolean archiveAndEndWorkflowForEvent(Map<String, Object> params){
		boolean flag = false;
		Long eventId = -1L;
		String instanceIdStr = "";
		Long instanceId = -1L;
		String taskIdStr = "";
		Long taskId = -1L;
		String closerName = "";
		String closerOrgName = "";
		String advice = "";
		StringBuffer wrongMsg = new StringBuffer("");
		UserInfo userInfo = null;
		
		if(CommonFunctions.isNotBlank(params, "userInfo")){//json转换用户对象
			userInfo = (UserInfo)params.get("userInfo");
		}
		
		if(CommonFunctions.isNotBlank(params, "eventId")){
			String eventIdStr = params.get("eventId").toString();
			try{
				eventId = Long.valueOf(eventIdStr);
			}catch(NumberFormatException e){
				wrongMsg.append("参数[eventId]："+eventId+" 不能转换为Long型数据，请检查！");
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "instanceId")){
			instanceIdStr = params.get("instanceId").toString();
			
		}else if(eventId > 0){
			instanceIdStr = this.capInstanceIdByEventId(instanceId);
		}
		
		if(StringUtils.isNotBlank(instanceIdStr)){
			try{
				instanceId = Long.valueOf(instanceIdStr);
			}catch(NumberFormatException e){
				wrongMsg.append("参数[instanceId]："+instanceId+" 不能转换为Long型数据，请检查！");
			}
		}else{
			wrongMsg.append("缺少参数[instanceId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "taskId")){
			taskIdStr = params.get("taskId").toString();
			try{
				taskId = Long.valueOf(taskIdStr);
			}catch(NumberFormatException e){
				wrongMsg.append("参数[taskId]："+taskId+" 不能转换为Long型数据，请检查！");
			}
		}else if(instanceId > 0){
			taskId = this.curNodeTaskId(instanceId);
		}else{
			wrongMsg.append("缺少参数[taskId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "closerName")){
			closerName = params.get("closerName").toString();
		}else if(userInfo != null){
			closerName = userInfo.getPartyName();
		}
		
		if(StringUtils.isBlank(closerName)){
			wrongMsg.append("缺少参数[closerName]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "closerOrgName")){
			closerOrgName = params.get("closerOrgName").toString();
		}else if(userInfo != null){
			closerOrgName = userInfo.getOrgName();
		}
		
		if(StringUtils.isBlank(closerOrgName)){
			wrongMsg.append("缺少参数[closerOrgName]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "advice")){
			advice = params.get("advice").toString();
		}
		
		if(wrongMsg.length() > 0){
			logger.info("有如下报错信息："+wrongMsg);
		}else{
			Map<String, Object> extraParam = new HashMap<String, Object>();
			flag = archiveAndEndWorkflowForEvent(instanceId, taskIdStr, closerName, closerOrgName, advice, extraParam);
		}
		
		return flag;
	}
	
	@Override
	public boolean archiveAndEndWorkflowForEvent(Long eventId, UserInfo userInfo, String advice, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		Long instanceId = null;
		
		if(CommonFunctions.isNotBlank(extraParam, "instanceId")) {
			try {
				instanceId = Long.valueOf(extraParam.get("instanceId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		} 
		
		if(instanceId == null || instanceId < 0) {
			instanceId = this.capInstanceIdByEventIdForLong(eventId);
		}
		
		if(instanceId != null && instanceId > 0){
			Long taskId = this.curNodeTaskId(instanceId);
			
			if(instanceId>0 && taskId>0) {
				if(extraParam == null) {
					extraParam = new HashMap<String, Object>();
				}
				
				if(userInfo != null) {
					if(CommonFunctions.isBlank(extraParam, "closerUserId")) {
						extraParam.put("closerUserId", userInfo.getUserId());
					}
					if(CommonFunctions.isBlank(extraParam, "closerOrgId")) {
						extraParam.put("closerOrgId", userInfo.getOrgId());
					}
				}
				
				flag = archiveAndEndWorkflowForEvent(instanceId, String.valueOf(taskId), userInfo.getPartyName(), userInfo.getOrgName(), advice, extraParam);

				if(flag){
					this.saveOrUpdateEventEvaluate(eventId,null,userInfo,extraParam);
				}

			}
		} else {
			logger.error("事件id【" + eventId + "】未能找到有效的流程实例信息！");
		}
		
		return flag;
	}
	
	@Override
	public boolean archiveAndEndWorkflowForEvent(Long instanceId, String taskId, String userName, String orgName, String advice, Map<String, Object> extraParam){
		boolean flag = false;
		StringBuffer wrongMsg = new StringBuffer("");
		
		if(instanceId==null || instanceId<0){
			wrongMsg.append("参数[instanceId]，输入值无效，请检查！");
		}else if(StringUtils.isBlank(taskId)){
			wrongMsg.append("参数[taskId]为必填参数，请检查！");
		}else if(StringUtils.isBlank(userName)){
			wrongMsg.append("参数[userName]为必填参数，请检查！");
		}else if(StringUtils.isBlank(orgName)){
			wrongMsg.append("参数[orgName]为必填参数，请检查！");
		}
		
		if(wrongMsg.length() > 0){
			logger.error("有如下报错信息："+wrongMsg);
		}else{
			flag = hessianFlowService.archiveAndEndWorkflowForEvent(instanceId, taskId, userName, orgName, advice, extraParam);
		}
		
		return flag;
	}
	
	@Override
	public boolean rejectWorkFlow(String taskId, String instanceId,
			String advice, UserInfo userInfo) throws Exception {
		boolean result = false;
		
		if(this.isUserInfoCurrentUser(taskId, instanceId, userInfo)) {
			OrgSocialInfoBO org = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
	
			UserBO user = new UserBO();
			user.setUserId(userInfo.getUserId());
			user.setPartyName(userInfo.getPartyName());
			
			result = hessianFlowService.rejectWorkFlow(taskId, advice,instanceId, user, org);
		}
		
		return result;
	}
	
	@Override
	public boolean rejectWorkFlow(Map<String, Object> params, UserInfo userInfo) throws Exception {
		boolean result = false;
		String advice = "";
		Long eventId = -1L, instanceId = -1L, curTaskId = -1L;
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(params, "curTaskId")) {
			try {
				curTaskId = Long.valueOf(params.get("curTaskId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(params, "advice")) {
			advice = params.get("advice").toString();
		}
		
		if(instanceId < 0 && CommonFunctions.isNotBlank(params, "eventId")) {
			try {
				eventId = Long.valueOf(params.get("eventId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
			if(eventId > 0) {
				instanceId = this.capInstanceIdByEventIdForLong(eventId);
			}
		}
		if(curTaskId < 0 && instanceId > 0) {
			curTaskId = this.curNodeTaskId(instanceId);
		}
		
		if(curTaskId > 0 && instanceId > 0) {
			result = this.rejectWorkFlow(curTaskId.toString(), instanceId.toString(), advice, userInfo);
		}
		
		return result;
	}
	
	@Override
	public boolean rejectWorkFlow(String taskId, String instanceId,
			String advice) throws Exception {
		boolean result = false;
		Map<String, Object> taskPerson = null;
		UserBO user = null;
		OrgSocialInfoBO org = null;
		List<Map<String, Object>> taskPersonMap = this.queryMyTaskParticipation(taskId);
		StringBuffer msgWrong = new StringBuffer("");
		String userIds = "";//当前办理人用户id
		
		if(taskPersonMap!=null && taskPersonMap.size()>0){
			taskPerson = taskPersonMap.get(0);
			
			if(taskPerson != null){
				Object orgIdObj = taskPerson.get("ORG_ID");
				Long orgId = -1L;
				
				if(orgIdObj != null){
					try{
						orgId = Long.valueOf(orgIdObj.toString());
						org = orgSocialInfoService.findByOrgId(orgId);
					}catch(NumberFormatException e){
						msgWrong.append("组织编码[").append(orgIdObj.toString()).append("]不能转换为Long数据！");
						e.printStackTrace();
					}
				}
			}
			
			userIds = curNodeUserIds(taskPersonMap);
		}
		
		if(StringUtils.isNotBlank(userIds)){
			String[] userIdArray = userIds.split(",");
			if(userIdArray.length > 0){
				Long userId = -1L;
				
				for(String userIdStr : userIdArray){
					try{
						userId = Long.valueOf(userIdStr);
						break;
					}catch(NumberFormatException e){
						msgWrong.append("用户编码[").append(userIdStr).append("]不能转换为Long数据！");
						e.printStackTrace();
					}
				}
				
				if(userId > 0){
					user = userManageService.getUserInfoByUserId(userId);
				}
			}
		}else{
			msgWrong.append("当前事件没有办理人员，当前任务编码为[").append(taskId).append("]！");
		}
		
		if(user!=null && org!=null){
			UserInfo userInfo = new UserInfo();
			userInfo.setUserId(user.getUserId());
			userInfo.setPartyName(user.getPartyName());
			userInfo.setOrgCode(org.getOrgCode());
			userInfo.setOrgId(org.getOrgId());
			userInfo.setOrgName(org.getOrgName());
			
			result = this.rejectWorkFlow(taskId, instanceId, advice, userInfo);
		}else if(msgWrong.length() > 0){
			logger.info("有如下报错信息："+msgWrong);
		}
		
		return result;
	}

	@Override
	public boolean recallWorkflow(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		boolean flag = false, isAble2Recall = false;
		Long eventId = null;
		String eventStatus = null, preNodeCode = null;
		ProInstance pro = null;
		Integer formTypeId = null;
		
		if(CommonFunctions.isNotBlank(params, "eventId")) {
			try {
				eventId = Long.valueOf(params.get("eventId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if(instanceId != null && instanceId > 0) {
			pro = this.capProInstanceByInstanceId(instanceId);
		} else if(eventId != null && eventId > 0) {
			pro = this.capProInstanceByEventId(eventId);
		}
		
		if(pro != null) {
			Node preNode = baseWorkflowService.capNodeInfo(pro.getProName(), ConstantValue.EVENT_WORKFLOW_WFTYPEID, pro.getFormId(), pro.getCurNode(), userInfo);
			instanceId = pro.getInstanceId();
			eventId = pro.getFormId();
			formTypeId = pro.getFormTypeId();
			
			if(preNode != null) {
				preNodeCode = preNode.getNodeCode();
			}
		}
		
		if(instanceId == null || instanceId < 0) {
			throw new IllegalArgumentException("缺少有效的实例id！");
		}
		
		//主流程时，才可使用事件状态进行判断
		if(ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID.equals(String.valueOf(formTypeId))) {
			if(eventId != null && eventId > 0) {
				EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
				
				if(event != null) {
					eventStatus = event.getStatus();
				} else {
					throw new IllegalArgumentException("流程实例id【"+ instanceId +"】对应事件id【" + eventId + "】没有找到有效的事件！");
				}
				
				isAble2Recall = !ConstantValue.EVENT_STATUS_DRAFT.equals(eventStatus) && !ConstantValue.EVENT_STATUS_RECEIVED.equals(eventStatus) && !ConstantValue.EVENT_STATUS_END.equals(eventStatus);
			}
		} else {
			isAble2Recall = true;
		}
		
		if(isAble2Recall) {
			flag = baseWorkflowService.recallWorkflow4Base(instanceId, userInfo, params);
		} else {
			throw new ZhsqEventException("该事件无需撤回！");
		}
		
		if(flag) {
			//当前环节有变化，需要重新获取实例信息
			pro = this.capProInstanceByInstanceId(instanceId);
			
			if(pro != null) {
				Node curNode = baseWorkflowService.capNodeInfo(pro.getProName(), ConstantValue.EVENT_WORKFLOW_WFTYPEID, pro.getFormId(), pro.getCurNode(), userInfo);
				String CLOSE_TASK_CODE = "JA",	//结案环节编码
						  EVA_TASK_CODE = "PJ";		//评价环节编码
				
				//如果遇到评价环节撤回到结案环节，需要再次撤回
				if(curNode != null && CLOSE_TASK_CODE.equals(curNode.getNodeCode()) && EVA_TASK_CODE.equals(preNodeCode)) {
					flag = baseWorkflowService.recallWorkflow4Base(instanceId, userInfo, params);
				}
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean sendSmsSyn(String userIds, String otherMobiles,
			String smsContent, UserInfo userInfo) {
		Set<String> mobilePhoneSet = new HashSet<String>();//为了去除重复的手机号码
		String[] userIdStr = null;		//选择的办理人的手机号码
		String[] phoneNum = null;		//额外电话号码
		String[] mobilePhones = null;	//所有的手机号码，为userIdStr和phoneNum之和，且有去重
		int mobilePhonesLength = 0;		//手机号码实际条数
		boolean flag = false;			//短信是否发送成功标识
		
		if(StringUtils.isNotBlank(userIds)){
			userIdStr = userIds.split(",");
		}
		
		if(StringUtils.isNotBlank(otherMobiles)){
			phoneNum = otherMobiles.split(",");
		}
		
		if(userIdStr != null) {
			UserBO userBO = null;
			String userId = null;
			
			for (int i = 0, length = userIdStr.length; i < length; i++) {
				userId = userIdStr[i];
				
				if(StringUtils.isNotBlank(userId)) {
					userBO = userManageService.getUserInfoByUserId(Long.parseLong(userId));
				}
				
				if(userBO != null) {
					Long verifyMobile = userBO.getVerifyMobile();
					if(verifyMobile != null) {
						mobilePhoneSet.add(verifyMobile.toString());
					}
				}
			}
		}
		if(phoneNum != null){
			for (int i = 0, length = phoneNum.length; i < length; i++) {
				String mobile = phoneNum[i];
				if(StringUtils.isNotBlank(mobile)){
					mobilePhoneSet.add(mobile);
				}
			}
		}
		
		mobilePhonesLength = mobilePhoneSet.size();
		if(mobilePhonesLength>0 && StringUtils.isNotBlank(smsContent)){
			mobilePhones = mobilePhoneSet.toArray(new String[mobilePhonesLength]);
			try{
				SendResult result = sendSmsService.sendSms(ConstantValue.SMS_PLATFORM_FLAG,
										userInfo.getUserId(), userInfo.getOrgCode(),
										mobilePhones, smsContent, SendSmsService.TYPE_SMS, null,
										null);
				if(result != null){
					flag = result.getCode() == 0;
				}
			}catch(Exception e){
				e.printStackTrace();
				flag = false;
			}
		}
		return flag;
	}

	@Override
	public void sendSms(String userIds, String otherMobiles,
			String smsContent, UserInfo userInfo) {
		final String userIdsF = userIds;
		final String otherMobilesF = otherMobiles;
		final String smsContentF = smsContent;
		final UserInfo userInfoF = userInfo;
		
		new Thread(){
			@Override
			public void run(){
				sendSmsSyn(userIdsF, otherMobilesF, smsContentF, userInfoF);
			}
		}.start();
	}
	
	@Override
	public void sendSms(String userIds, String otherMobiles,
			String nextNodeName, Long instanceId, String smsContent, UserInfo userInfo) {
		String receiveUserIds = userIds;
		
		if(instanceId!=null && instanceId>0){
			if ("结案".equals(nextNodeName)) {//结案时短信发送至事件发起人
				Map<String, Object> proInstance = hessianFlowService.queryWfIntByInstanceId(instanceId);
				receiveUserIds = String.valueOf(proInstance.get("USER_ID"));
			} else if("归档".equals(nextNodeName)) {
				receiveUserIds = "";
				// 评价时短信发送至事件流程环节参与人
				List<Map<String, Object>> tasks = (ArrayList<Map<String,Object>>)hessianFlowService.getTaskListByInstanceId(instanceId);
				if (tasks!=null && tasks.size()!=0) {
					for (int i = 0; i < tasks.size(); i++) {
						Map<String, Object> taskTemp = (HashMap<String, Object>)tasks.get(i);
						String taskUserId = taskTemp.get("TRANSACTOR_ID").toString();
						if(receiveUserIds.indexOf(taskUserId) == -1) {
							receiveUserIds = taskUserId + "," + receiveUserIds;
						}
					}
					receiveUserIds = receiveUserIds.substring(0,receiveUserIds.length()-1);
				}
			}
		}
		
		//发送短信
		if((StringUtils.isNotBlank(receiveUserIds)||StringUtils.isNotBlank(otherMobiles)) && StringUtils.isNotBlank(smsContent)){
			this.sendSms(receiveUserIds, otherMobiles, smsContent, userInfo);
		}
	}
	
	@Override
	public void sendSms(String userIds, String otherMobiles, String smsContent, UserInfo userInfo, Map<String, Object> params) {
		Long instanceId = null;
		String nextNodeName = null;
		userIds = StringUtils.isNotBlank(userIds) ? userIds : "";
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {}
		}
		if(CommonFunctions.isNotBlank(params, "nextNodeName")) {
			nextNodeName = params.get("nextNodeName").toString();
		}
		if(CommonFunctions.isNotBlank(params, "receiverIds")) {
			userIds += "," + params.get("receiverIds").toString();
		}
		
		sendSms(userIds, otherMobiles, nextNodeName, instanceId, smsContent, userInfo);
	}
	
	@Override
	public String capSmsContent(String taskId, String noteType, String advice, EventDisposal event, UserInfo userInfo){
		boolean validateFlag = true;//判断参数有效性
		String smsContentTemplate = "";//获取的短信内容
		
		if(StringUtils.isBlank(noteType) || userInfo==null){
			validateFlag = false;
		}
		
		if(validateFlag){
			smsContentTemplate = funConfigurationService.turnCodeToValue(ConstantValue.SMS_NOTE_TYPE, noteType, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			if(StringUtils.isNotBlank(smsContentTemplate)){
				if(userInfo != null) {
					String userPartyName = userInfo.getPartyName();
					
					if(smsContentTemplate.contains("@operator")){
						smsContentTemplate = smsContentTemplate.replace("@operator", userPartyName);
					}
					if(smsContentTemplate.contains("@reporter")){
						smsContentTemplate = smsContentTemplate.replace("@reporter", userPartyName);
					}
					if(smsContentTemplate.contains("@closer")){
						smsContentTemplate = smsContentTemplate.replace("@closer", userPartyName);
					}
					if(smsContentTemplate.contains("@operateOrgName")) {
						smsContentTemplate = smsContentTemplate.replace("@operateOrgName", userInfo.getOrgName());
					}
				}
				
				if(event != null){
					if(StringUtils.isNotBlank(event.getOccurred()) && smsContentTemplate.contains("@happenAddr")){
						smsContentTemplate = smsContentTemplate.replace("@happenAddr", event.getOccurred());
					}
					
					if(StringUtils.isNotBlank(event.getHappenTimeStr()) && smsContentTemplate.contains("@happenTime")){
						smsContentTemplate = smsContentTemplate.replace("@happenTime", event.getHappenTimeStr());
					}
					
					if(StringUtils.isNotBlank(event.getUrgencyDegreeName()) && smsContentTemplate.contains("@urgencyDegree")){
						smsContentTemplate = smsContentTemplate.replace("@urgencyDegree", event.getUrgencyDegreeName());
					}
					
					if(StringUtils.isNotBlank(event.getContent()) && smsContentTemplate.contains("@content")){
						smsContentTemplate = smsContentTemplate.replace("@content", event.getContent());
					}
					
					if(StringUtils.isNotBlank(event.getEventName()) && smsContentTemplate.contains("@eventTitle")){
						smsContentTemplate = smsContentTemplate.replace("@eventTitle", event.getEventName());
					}
					
					if(StringUtils.isNotBlank(event.getCode()) && smsContentTemplate.contains("@eventCode")){
						smsContentTemplate = smsContentTemplate.replace("@eventCode", event.getCode());
					}
					
					if(StringUtils.isNotBlank(event.getCreatorName()) && smsContentTemplate.contains("@eventCreatorName")){
						smsContentTemplate = smsContentTemplate.replace("@eventCreatorName", event.getCreatorName());
					}
					
					if(StringUtils.isNotBlank(event.getTypeName()) && smsContentTemplate.contains("@eventTypeName")){
						smsContentTemplate = smsContentTemplate.replace("@eventTypeName", event.getTypeName());
					}
					
					if(smsContentTemplate.contains("@handleDate")){
						if(StringUtils.isNotBlank(event.getHandleDateStr())){
							smsContentTemplate = smsContentTemplate.replace("@handleDate", event.getHandleDateStr());
						}else if(event.getHandleDate() != null){
							String handleDateStr = "";
							try {
								handleDateStr = DateUtils.convertDateToString(event.getHandleDate());
							} catch (ParseException e) {
								handleDateStr = "";
								e.printStackTrace();
							}
							smsContentTemplate = smsContentTemplate.replace("@handleDate", handleDateStr);
						}
					}
					
					if(smsContentTemplate.contains("@handleTime")){
						if(StringUtils.isBlank(taskId)){//获取当前任务id
							Long eventId = event.getEventId();
							if(eventId!=null && !eventId.equals(-1L)){
								String instanceId = this.capInstanceIdByEventId(eventId);
								if(StringUtils.isNotBlank(instanceId)){
									taskId = String.valueOf(this.curNodeTaskId(Long.valueOf(instanceId)));
								}
							}
						}
						if(StringUtils.isNotBlank(taskId)){//获取处理时限
							Map<String, Object> taskInfo = hessianFlowService.getStartTimeAndTimeLimitByTaskId(taskId);
							String startTime = (String) taskInfo.get("START_TIME");
							Integer limitTime = Integer.parseInt(taskInfo.get("LIMIT_TIME").toString());
							if (StringUtils.isNotBlank(startTime) && limitTime != null) {
								try {
									String handleTime= DateUtils.convertDateToString(DateUtils.addInterval(DateUtils
											.convertStringToDate(startTime, "yyyy-MM-dd HH:mm:ss"), limitTime, "00"));
									if(StringUtils.isNotBlank(handleTime)){
										smsContentTemplate = smsContentTemplate.replace("@handleTime", handleTime);
									}
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
						}
					}
					
					if(event.getHandleDateInterval()!=null && event.getHandleDateInterval()!=0 && smsContentTemplate.contains("@timeInterval")){
						smsContentTemplate = smsContentTemplate.replace("@timeInterval", String.valueOf(event.getHandleDateInterval()));
					}
					
					if(smsContentTemplate.contains("@finTime")){
						if(StringUtils.isNotBlank(event.getFinTimeStr())){
							smsContentTemplate = smsContentTemplate.replace("@finTime", event.getFinTimeStr());
						}else if(event.getFinTime() != null){
							String finTimeStr = "";
							try {
								finTimeStr = DateUtils.convertDateToString(event.getFinTime());
							} catch (ParseException e) {
								finTimeStr = "";
								e.printStackTrace();
							}
							smsContentTemplate = smsContentTemplate.replace("@finTime", finTimeStr);
						}
					}
				}
				
				if(StringUtils.isNotBlank(advice) && smsContentTemplate.contains("@advice")){
					smsContentTemplate = smsContentTemplate.replace("@advice", advice);
				}
				
				if(smsContentTemplate.contains("@currentTime")){
					String currDateTime = DateUtils.getNow();
					smsContentTemplate = smsContentTemplate.replace("@currentTime", currDateTime);
				}
			}
		}
		
		return smsContentTemplate;
	}
	
	@Override
	public Map<String, Object> capInfo4SMS(String curNodeName, String nextNodeName, Map<String, Object> params, UserInfo userInfo) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(),
							turnLevel = null;
		String taskId = null, 
			   noteType = null, 	//短信模板
			   advice = null,
			   orgCode = userInfo.getOrgCode(),
			   smsContent = "";		//短信内容
		Long eventId = null;
		EventDisposal event = null;
		Set<String> receiverMobilePhoneSet = new HashSet<String>();
		Set<Long> receiverIdSet = new HashSet<Long>();
		boolean isCapRemindedUser = false, isCapReceiverId = false, isCapReceiverMobilePhone = true;
		
		if(CommonFunctions.isNotBlank(params, "taskId")) {//任务id
			taskId = params.get("taskId").toString();
		}
		if(CommonFunctions.isNotBlank(params, "advice")) {//办理意见
			advice = params.get("advice").toString();
		}
		if(CommonFunctions.isNotBlank(params, "eventId")) {
			eventId = Long.valueOf(params.get("eventId").toString());
		} else if(CommonFunctions.isNotBlank(params, "formId")) {
			eventId = Long.valueOf(params.get("formId").toString());
		}
		if(eventId != null && eventId > 0) {
			event = eventDisposalService.findEventById(eventId, orgCode);
		}
		
		if(CommonFunctions.isNotBlank(params, "isCapRemindedUser")) {
			isCapRemindedUser = Boolean.valueOf(params.get("isCapRemindedUser").toString());
		}
		
		if(CommonFunctions.isNotBlank(params, "isCapReceiverId")) {
			isCapReceiverId = Boolean.valueOf(params.get("isCapReceiverId").toString());
		}
		
		if(CommonFunctions.isNotBlank(params, "isCapReceiverMobilePhone")) {
			isCapReceiverMobilePhone = Boolean.valueOf(params.get("isCapReceiverMobilePhone").toString());
		}
		
		if(isCapRemindedUser) {
			Long instanceId = null;
			
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				try {
					instanceId = Long.valueOf(params.get("instanceId").toString());
				} catch(NumberFormatException e) {}
			} else if(eventId != null && eventId > 0) {
				ProInstance proInstance = capProInstanceByEventId(eventId);
				
				if(proInstance != null) {
					instanceId = proInstance.getInstanceId();
				}
			}
			
			if(instanceId != null && instanceId > 0) {
				Map<String, Object> curDataMap = this.curNodeData(instanceId);
				
				if(CommonFunctions.isNotBlank(curDataMap, "WF_USERNAME")) {
					resultMap.put("remindedUserName", curDataMap.get("WF_USERNAME"));
				} else if(CommonFunctions.isNotBlank(curDataMap, "WF_ORGNAME")) {
					resultMap.put("remindedUserName", curDataMap.get("WF_ORGNAME"));
				}
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "receiverOrgIds")) {
			String[] receiverOrgIdArray = params.get("receiverOrgIds").toString().split(",");
			Long receiverOrgId = -1L, verifyMobile = null;
			List<UserBO> receiverBOList = new ArrayList<UserBO>();
			
			for(String receiverOrgIdStr : receiverOrgIdArray) {
				if(StringUtils.isNotBlank(receiverOrgIdStr)) {
					receiverOrgId = Long.valueOf(receiverOrgIdStr);
					receiverBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, null, receiverOrgId));
				}
			}
			
			for(UserBO receiverBO : receiverBOList) {
				verifyMobile = receiverBO.getVerifyMobile();
				
				if(verifyMobile != null && verifyMobile > 0) {
					if(isCapReceiverId) {
						receiverIdSet.add(receiverBO.getUserId());
					}
					
					if(isCapReceiverMobilePhone) {
						receiverMobilePhoneSet.add(verifyMobile.toString());
					}
				}
			}
		}
		if(CommonFunctions.isNotBlank(params, "receiverIds")) {
			String[] receiverIdArray = params.get("receiverIds").toString().split(",");
			Long verififyMobile = null, receiverId = null;
			UserBO userBO = null;
			
			for(String receiverIdStr : receiverIdArray) {
				receiverId = Long.valueOf(receiverIdStr);
				
				if(isCapReceiverId) {
					receiverIdSet.add(receiverId);
				}
				
				if(isCapReceiverMobilePhone) {
					userBO = userManageService.getUserInfoByUserId(receiverId);
					
					if(userBO != null) {
						verififyMobile = userBO.getVerifyMobile();
						
						if(verififyMobile != null) {
							receiverMobilePhoneSet.add(verififyMobile.toString());
						}
					}
				}
			}
		}
		if(CommonFunctions.isNotBlank(params, "receiverMobilePhones")) {
			String[] receiverMobilePhoneArray = params.get("receiverMobilePhones").toString().split(",");
			for(String receiverMobilePhone :  receiverMobilePhoneArray) {
				if(StringUtils.isNotBlank(receiverMobilePhone)) {
					receiverMobilePhoneSet.add(receiverMobilePhone);
				}
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "noteType")) {
			noteType = params.get("noteType").toString();
		} else {
			turnLevel = this.capTurnLevel(curNodeName, nextNodeName, userInfo);
			
			noteType = turnLevel.get("noteType").toString();
		}
		
		smsContent = this.capSmsContent(taskId, noteType, advice, event, userInfo);
		
		if(isCapReceiverId) {
			resultMap.put("receiverIds", StringUtils.join(receiverIdSet, ","));
		}
		
		if(isCapReceiverMobilePhone && receiverMobilePhoneSet.size() > 0) {//接收人员手机号码
			resultMap.put("receiverMobilePhones", StringUtils.join(receiverMobilePhoneSet, ","));
		}
		
		resultMap.put("smsContent", smsContent);//短信内容
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId) {
		List<Map<String, Object>> queryTaskList = null;
		if(instanceId != null){
			queryTaskList = hessianFlowService.queryProInstanceDetail(instanceId);
			Object isTimeOut = "";
			String isTimeOutName = "正常";
			for(int index = 0, length = queryTaskList.size(); index < length; index++){
				isTimeOut = queryTaskList.get(index).get("ISTIMEOUT");
				if(isTimeOut != null){
					String isTimeOutStr = isTimeOut.toString();
					if("1".equals(isTimeOutStr)){
						isTimeOutName = "已超时";
					}
					queryTaskList.get(index).put("ISTIMEOUTNAME", isTimeOutName);
				}
			}
		}
		return queryTaskList;
	}

	@Override
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int sqlOrder) {
		String order = "";
		
		switch(sqlOrder){
			case IEventDisposalWorkflowService.SQL_ORDER_ASC:{
				order = Constants.SQL_ORDER_ASC;break;
			}
			case IEventDisposalWorkflowService.SQL_ORDER_DESC:{
				order = Constants.SQL_ORDER_DESC;break;
			}
			default:{
				order = Constants.SQL_ORDER_ASC;break;
			}
		}
		
		List<Map<String, Object>> workflowTaskList =  hessianFlowService.queryProInstanceDetailByOrder(instanceId, order);
		if(IEventDisposalWorkflowService.SQL_ORDER_DESC == sqlOrder) {
			ProInstance pro = this.capProInstanceByInstanceId(instanceId);
			String ABOLISH_PRO_STATUS = "4";//实例废除状态
			
			if(pro != null && !ABOLISH_PRO_STATUS.equals(pro.getStatus())) {
				Map<String, Object> curTaskData = this.curNodeData(instanceId);
				
				if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
					Map<String, Object> curNodeMap = new HashMap<String, Object>();
					List<Map<String, Object>> taskPerson = null;
					StringBuffer handlePerson = new StringBuffer(""),
								 userIdBuffer = new StringBuffer(""),
								 orgIdBuffer = new StringBuffer("");
					Long currentTaskId = -1L;
					
					if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
						try {
							currentTaskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
						
						if(currentTaskId != null && currentTaskId > 0) {
							taskPerson = this.queryMyTaskParticipation(currentTaskId.toString());
						}
					}
					
					if(taskPerson != null) {
						for(Map<String, Object> mapTemp : taskPerson){
							if(CommonFunctions.isNotBlank(mapTemp, "USER_NAME")){
								handlePerson.append(mapTemp.get("USER_NAME"));
							}
							
							if(CommonFunctions.isNotBlank(mapTemp, "ORG_NAME")) {
								handlePerson.append("(").append(mapTemp.get("ORG_NAME")).append(")").append(";");
							}
							
							if(CommonFunctions.isNotBlank(mapTemp, "USER_ID")) {
								userIdBuffer.append(",").append(mapTemp.get("USER_ID"));
							}
							
							if(CommonFunctions.isNotBlank(mapTemp, "ORG_ID")) {
								orgIdBuffer.append(",").append(mapTemp.get("ORG_ID"));
							}
						}
					}
					
					curNodeMap.put("TASK_ID", currentTaskId);
					curNodeMap.put("TASK_NAME", curTaskData.get("WF_ACTIVITY_NAME_"));
					curNodeMap.put("HANDLE_PERSON", handlePerson.toString());
					curNodeMap.put("IS_CURRENT_TASK", true);//用于判断是否是当前任务
					if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
						curNodeMap.put("TASK_CODE", curTaskData.get("NODE_NAME"));
					}
					if(userIdBuffer.length() > 0) {
						curNodeMap.put("TRANSACTOR_ID", userIdBuffer.substring(1));
					}
					if(orgIdBuffer.length() > 0) {
						curNodeMap.put("ORG_ID", orgIdBuffer.substring(1));
					}
	
					if(currentTaskId > 0) {
						//获取催办列表
						List<Remind> remindResultList = this.queryRemindListByTaskId(currentTaskId);
						
						if(remindResultList != null && remindResultList.size() > 0) {
							List<Map<String, Object>> remindList = new ArrayList<Map<String, Object>>();
							Map<String, Object> remindMap = null;
							
							for(Remind remind : remindResultList) {
								remindMap = new HashMap<String, Object>();
								remindMap.put("REMIND_USER_NAME", remind.getRemindUserName());
								remindMap.put("REMINDED_USER_NAME", remind.getRemindedUserName());
								try {
									remindMap.put("REMIND_DATE_", DateUtils.convertDateToString(remind.getRemindDate()));
								} catch (ParseException e) {
									e.printStackTrace();
								}
								remindMap.put("REMARKS", remind.getRemarks());
								
								remindList.add(remindMap);
							}
							
							curNodeMap.put("remindList", remindList);
						}
					}
					
					workflowTaskList.add(0, curNodeMap);//增添当前环节展示
				}
			}
		}
		
		List<Map<String, Object>> removeTasks = new ArrayList<Map<String, Object>>();
		
		//构建可移除的环节
		for(Map<String, Object> mapTemp : workflowTaskList) {
			if(CommonFunctions.isNotBlank(mapTemp, "NODE_CODE")) {
				for(String nodeCode : REMOVE_NODE_CODE) {
					if(nodeCode.equals(mapTemp.get("NODE_CODE"))) {
						removeTasks.add(mapTemp);
					}
				}
			}
		}
		
		//分两步移除，是为了避免抛出java.util.ConcurrentModificationException异常
		workflowTaskList.removeAll(removeTasks);
		
		return workflowTaskList;
	}

	@Override
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int sqlOrder, UserInfo userInfo){
		List<Map<String, Object>> workflowList = this.queryProInstanceDetail(instanceId, sqlOrder),
								  taskList = new ArrayList<Map<String, Object>>(),
								  subTaskList = null,
								  subAndReceivedTaskList = null,
								  subAndReceivedTaskListTmp = null,
								  timeAndRemarkList = null;
		
		/**
		 * PARENT_TASK_ID
		 * TASK_ID，
		 * TASK_TYPE，
		 * TASK_NAME，
		 * OPERATE_TYPE，
		 * TRANSACTOR_ID，
		 * TRANSACTOR_NAME，
		 * ORG_NAME，ORG_ID，
		 * REMARKS，START_TIME，END_TIME
		 */
		
		if(workflowList != null) {//新增子任务（会签任务和处理中任务）
			Long taskId = -1L;
			List<TaskReceive> taskReceivedList = null;
			
			for(Map<String, Object> taskMap : workflowList) {
				taskList.add(taskMap);
				
				if(CommonFunctions.isNotBlank(taskMap, "TASK_ID")) {
					int subCount = 0;
					boolean isCurrentTask = false;
					
					if(CommonFunctions.isNotBlank(taskMap, "SUB_COUNT")) {
						try {
							subCount = Integer.valueOf(taskMap.get("SUB_COUNT").toString());
						} catch(NumberFormatException e) {
							subCount = 0;
						}
					}
					
					if(CommonFunctions.isNotBlank(taskMap, "IS_CURRENT_TASK")) {//判断是否是当前任务
						isCurrentTask = Boolean.valueOf(taskMap.get("IS_CURRENT_TASK").toString());
					}
					
					if(subCount > 0) {//构建会签环节的办理人员信息
						if(CommonFunctions.isNotBlank(taskMap, "TRANSACTOR_NAME") && CommonFunctions.isNotBlank(taskMap, "ORG_NAME")) {
							String[] transactorNames = taskMap.get("TRANSACTOR_NAME").toString().split(",");
							String[] orgNames = taskMap.get("ORG_NAME").toString().split(",");
							StringBuffer subTaskPerson = new StringBuffer("");
							
							for(int index = 0, len = transactorNames.length; index < len; index++) {
								subTaskPerson.append(transactorNames[index]).append("(").append(orgNames[index]).append(");");
							}
							
							taskMap.put("SUB_HANDLE_PERSON", subTaskPerson);
						}
					}
					
					subAndReceivedTaskList = new ArrayList<Map<String, Object>>();
					//获取任务接收时间列表
					taskId  = Long.valueOf(taskMap.get("TASK_ID").toString());
					taskReceivedList = this.findTaskReceivedList(taskId, instanceId, null);
					
					if(taskReceivedList != null && taskReceivedList.size() > 0) {
						Map<String, Object> taskReceiveMap = null;
						for(TaskReceive taskReceive : taskReceivedList) {
							taskReceiveMap = new HashMap<String, Object>();
							taskReceiveMap.put("TASK_ID", taskReceive.getTaskId());
							taskReceiveMap.put("TRANSACTOR_ID", taskReceive.getUserId());
							taskReceiveMap.put("TRANSACTOR_NAME", taskReceive.getUserName());
							taskReceiveMap.put("ORG_ID", taskReceive.getOrgId());
							taskReceiveMap.put("ORG_NAME", taskReceive.getOrgName());
							try {
								taskReceiveMap.put("RECEIVE_TIME", DateUtils.convertDateToString(taskReceive.getReceiveTime()));
							} catch (ParseException e) {
								e.printStackTrace();
							}
							
							subAndReceivedTaskList.add(taskReceiveMap);
						}
					}
					
					if(subCount > 0 || isCurrentTask) {//子任务不为空或者是当前任务时，才查找子任务
						subTaskList = this.querySubTaskDetails(taskMap.get("TASK_ID").toString(), sqlOrder, userInfo);
						
						if(subTaskList != null && subTaskList.size() > 0) {
							for(Map<String, Object> subTask : subTaskList) {
								if(CommonFunctions.isNotBlank(subTask, "TASK_NAME")) {//清除处理中的环节名称
									if(ConstantValue.HANDLING_TASK_CODE.equals(subTask.get("TASK_NAME"))) {
										subTask.remove("TASK_NAME");
									}
								}
								
								subAndReceivedTaskList.add(subTask);
							}
							
							taskMap.put("subTaskList", subTaskList);
						}
					}
					
					if(subAndReceivedTaskList.size() > 0) {
						subAndReceivedTaskListTmp = new ArrayList<Map<String, Object>>();
						Map<String, Object> timeAndRemarkMap = null;
						boolean isSubAndReceivedBlank = true;
						
						for(Map<String, Object> subAndReceivedTaskMap : subAndReceivedTaskList) {
							timeAndRemarkList = new ArrayList<Map<String, Object>>();
							timeAndRemarkMap = new HashMap<String, Object>();
							isSubAndReceivedBlank = true;
							
							if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "START_TIME")) {
								timeAndRemarkMap.put("START_TIME", subAndReceivedTaskMap.get("START_TIME"));
							}
							if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "END_TIME")) {
								timeAndRemarkMap.put("END_TIME", subAndReceivedTaskMap.get("END_TIME"));
							}
							if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "RECEIVE_TIME")) {
								timeAndRemarkMap.put("RECEIVE_TIME", subAndReceivedTaskMap.get("RECEIVE_TIME"));
							}
							if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "REMARKS")) {
								timeAndRemarkMap.put("REMARKS", subAndReceivedTaskMap.get("REMARKS"));
							}
							
							for(Map<String, Object> subAndReceivedTaskTmpMap : subAndReceivedTaskListTmp) {
								//合并准则：1、存在TRANSACTOR_ID和ORG_ID，且二者均不为空时，二者同时相等者进行合并；
								if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "TRANSACTOR_ID") && CommonFunctions.isNotBlank(subAndReceivedTaskTmpMap, "TRANSACTOR_ID") && 
										subAndReceivedTaskMap.get("TRANSACTOR_ID").equals(subAndReceivedTaskTmpMap.get("TRANSACTOR_ID")) && 
										CommonFunctions.isNotBlank(subAndReceivedTaskMap, "ORG_ID") && CommonFunctions.isNotBlank(subAndReceivedTaskTmpMap, "ORG_ID") &&
										subAndReceivedTaskMap.get("ORG_ID").equals(subAndReceivedTaskTmpMap.get("ORG_ID"))) {
									if(CommonFunctions.isNotBlank(subAndReceivedTaskTmpMap, "timeAndRemarkList")) {
										timeAndRemarkList.addAll((List<Map<String, Object>>)subAndReceivedTaskTmpMap.get("timeAndRemarkList"));
									}
									
									if(!timeAndRemarkMap.isEmpty()) {
										timeAndRemarkList.add(timeAndRemarkMap);
										
										subAndReceivedTaskTmpMap.put("timeAndRemarkList", timeAndRemarkList);
									}
									
									isSubAndReceivedBlank = false;
									
									break;
								}
							}
							
							if(isSubAndReceivedBlank) {
								if(!timeAndRemarkMap.isEmpty()) {
									timeAndRemarkList.add(timeAndRemarkMap);
									
									subAndReceivedTaskMap.put("timeAndRemarkList", timeAndRemarkList);
								}
								
								subAndReceivedTaskListTmp.add(subAndReceivedTaskMap);
							}
						}
						
						taskMap.put("subAndReceivedTaskList", subAndReceivedTaskListTmp);
					}
				}
			}
		}
		
		return workflowList;
	}
	
	@Override
	public List<Map<String, Object>> querySubTaskDetails(String taskId, int sqlOrder, UserInfo userInfo){
		List<Map<String, Object>> subTaskDetails = null;
		
		if(StringUtils.isNotBlank(taskId)) {
			String order = "";
			
			switch(sqlOrder){
				case IEventDisposalWorkflowService.SQL_ORDER_ASC:{
					order = Constants.SQL_ORDER_ASC;break;
				}
				case IEventDisposalWorkflowService.SQL_ORDER_DESC:{
					order = Constants.SQL_ORDER_DESC;break;
				}
				default:{
					order = Constants.SQL_ORDER_DESC;break;
				}
			}
			
			subTaskDetails = hessianFlowService.queryTaskDetails(taskId, order);
		}
		
		return subTaskDetails;
	}
	
	@Override
	public boolean addRemind(Long instanceId, Long taskId, UserInfo userInfo,
			String remarks, String userIds, String userNames) {
		return hessianFlowService.addRemind(instanceId, String.valueOf(taskId), userInfo.getUserId(), userInfo.getPartyName(), remarks, userIds, userNames);
	}
	
	@Override
	public boolean addRemind(Long instanceId, Long taskId, UserInfo userInfo,
			String remarks, String userIds) {
		String userNames = "";
		
		if(StringUtils.isNotBlank(userIds)) {
			String[] userIdArray = userIds.split(",");
			
			if(userIdArray!=null && userIdArray.length>0) {
				UserBO userBO = null;
				StringBuffer userNameBuffer = new StringBuffer("");
				
				for(String userId : userIdArray){
					userBO = userManageService.getUserInfoByUserId(Long.parseLong(userId));
					
					if(userBO != null) {
						userNameBuffer.append(",").append(userBO.getPartyName());
					}
				}
				
				userNames = userNameBuffer.substring(1);
			}
		}
		
		return this.addRemind(instanceId, taskId, userInfo, remarks, userIds, userNames);
	}

	@Override
	public boolean addRemind(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		String category = null,
			   supervisionType = null,//2：督办-红牌
			   remarks = null;
		boolean remindFlag = false;
		StringBuffer msgWrong = new StringBuffer("");
		Long eventId = null;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作人员信息！");
		}
		
		if(CommonFunctions.isBlank(params, "category")) {
			throw new IllegalArgumentException("缺少属性【category】！");
		} else {
			category = params.get("category").toString();
			
			if(IEventDisposalWorkflowService.REMIND_CATEGORY.SUPERVISE.getCategory().equals(category)) {
				supervisionType = "2";//督办-红牌
			} else if(IEventDisposalWorkflowService.REMIND_CATEGORY.URGE.getCategory().equals(category)) {
				//do nothing
			} else {
				throw new IllegalArgumentException("传入了不可识别的【category】：" + params.get("category") + "！");
			}
		}
		
		if(CommonFunctions.isNotBlank(params,"eventId")) {
			eventId = Long.valueOf(params.get("eventId").toString());
		} else if(CommonFunctions.isNotBlank(params,"formId")) {
			eventId = Long.valueOf(params.get("formId").toString());
		}
		
		if((null == instanceId || instanceId < 0) && (eventId != null && eventId > 0)){
			//根据事件id获取实例id
			instanceId = this.capInstanceIdByEventIdForLong(eventId);
		} else if((eventId == null || eventId < 0) && (instanceId != null && instanceId > 0)) {
			ProInstance proInstance = capProInstanceByInstanceId(instanceId);
			
			if(proInstance != null) {
				eventId = proInstance.getFormId();
			}
		}

		if(null != instanceId && instanceId > 0){
			params.put("instanceId",instanceId);
		} else {
			msgWrong.append("请检查属性【instanceId】的有效性！");
		}
		
		if(eventId == null || eventId < 0) {
			msgWrong.append("缺少有效的事件id！");
		}
		
		Map<String, Object> curNodeDataMap = curNodeData(instanceId);
		String curUserId = null, curUserName = null;
		
		if(CommonFunctions.isBlank(params, "curTaskId") && CommonFunctions.isNotBlank(curNodeDataMap, "WF_DBID_")) {
			params.put("curTaskId", curNodeDataMap.get("WF_DBID_"));
		}
		
		if(CommonFunctions.isNotBlank(curNodeDataMap, "WF_USERID")) {
			curUserId = curNodeDataMap.get("WF_USERID").toString();
			
			if(CommonFunctions.isNotBlank(curNodeDataMap, "WF_USERNAME")) {
				curUserName = curNodeDataMap.get("WF_USERNAME").toString();
			}
		} else if(CommonFunctions.isNotBlank(curNodeDataMap, "WF_ORGID")) {
			String[] curOrgIdArray = curNodeDataMap.get("WF_ORGID").toString().split(",");
			List<UserBO> userBOList = new ArrayList<UserBO>();
			LinkedHashSet<String> userIdSet = new LinkedHashSet<String>();
			LinkedHashSet<String> userNameSet = new LinkedHashSet<String>();
			Long curUserBOId = null;
			
			for(String curOrgIdStr : curOrgIdArray) {
				userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, null, Long.valueOf(curOrgIdStr)));
			}
			
			for(UserBO userBO : userBOList) {
				curUserBOId = userBO.getUserId();
				
				if(curUserBOId != null && curUserBOId > 0) {
					userIdSet.add(String.valueOf(curUserBOId));
					userNameSet.add(userBO.getPartyName());
				}
			}
			
			curUserId = String.join(",", userIdSet);
			curUserName = String.join(",", userNameSet);
		}
		
		if(CommonFunctions.isNotBlank(params, "category")) {
			category = params.get("category").toString();
		}
		if(CommonFunctions.isNotBlank(params, "supervisionType")) {
			supervisionType = params.get("supervisionType").toString();
		}
		if(CommonFunctions.isNotBlank(params, "remarks")) {
			remarks = params.get("remarks").toString();
		} else if(CommonFunctions.isNotBlank(params, "remindRemark")) {
			remarks = params.get("remindRemark").toString();
		}
		
		if(StringUtils.isNotBlank(supervisionType)) {
			params.put("supervisionType", supervisionType);
		}
		
		params.put("category", category);
		params.put("eventId", eventId);
		params.put("remindRemark", remarks);
		params.put("remindUserId", userInfo.getUserId());
		params.put("remindUserPartyName", userInfo.getPartyName());
		
		if(IEventDisposalWorkflowService.REMIND_CATEGORY.URGE.getCategory().equals(category)) {
			if(CommonFunctions.isBlank(params, "remindedUserId") && StringUtils.isNotBlank(curUserId)) {
				params.put("remindedUserId", curUserId);
			}
			
			if(CommonFunctions.isBlank(params, "remindedUserPartyName") && StringUtils.isNotBlank(curUserName)) {
				params.put("remindedUserPartyName", curUserName);
			}
			
			if(CommonFunctions.isBlank(params, "remindedUserId")) {
				throw new IllegalArgumentException("缺少属性【remindedUserId】！");
			}
			
			if(CommonFunctions.isBlank(params, "remindedUserPartyName")) {
				throw new IllegalArgumentException("缺少属性【remindedUserPartyName】！");
			}
		}
		
		if(msgWrong.length() > 0) {
			throw new Exception(msgWrong.toString());
		} else {
			remindFlag = baseWorkflowService.addRemind(params);
		}
		
		if(remindFlag) {
			boolean isAddAttention = IEventDisposalWorkflowService.REMIND_CATEGORY.SUPERVISE.getCategory().equals(category);
			
			if(CommonFunctions.isNotBlank(params, "isAddAttention")) {
				isAddAttention = Boolean.valueOf(params.get("isAddAttention").toString());
			}
			
			if(isAddAttention) {
				addAttention(eventId, userInfo);
			}
			
			//发送短信
			if(CommonFunctions.isNotBlank(params, "smsContent")) {
				String smsContent = params.get("smsContent").toString(),
					   otherMobileNums = null;
				
				if(CommonFunctions.isNotBlank(params, "otherMobileNums")) {
					otherMobileNums = params.get("otherMobileNums").toString();
				}
				
				eventDisposalWorkflowService.sendSms(curUserId, otherMobileNums, smsContent, userInfo);
			}

			//发送mq消息
            eventDisposalService.pushRemindMsg(instanceId,userInfo.getInfoOrgCodeStr(),params);
		}

		return remindFlag;
	}

	@Override
	public List<Remind> queryRemindListByTaskId(Long taskId) {
		List<Remind> remindList = null;
		
		if(taskId != null && taskId > 0) {
			remindList = hessianFlowService.queryRemindListByTaskId(taskId);
		}
		
		return remindList;
	}
	
	@Override
	public List<Map<String, Object>> queryMyTaskParticipation(String taskId){
		List<Map<String, Object>> taskPerson = null;
		
		if(StringUtils.isNotBlank(taskId) && !"null".equalsIgnoreCase(taskId)){
			taskPerson = hessianFlowService.queryMyTaskParticipation(Long.valueOf(taskId));
		}
		if(taskPerson!=null && taskPerson.size()>0){
			for(Map<String, Object> person : taskPerson){
				Object userTypeObj = person.get("USER_TYPE");
				if(userTypeObj != null){
					String userType = userTypeObj.toString();
					if(IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userType)){
						Object userIdObj = person.get("USER_ID");
						if(userIdObj != null){
							Long userId = Long.valueOf(userIdObj.toString());
							UserBO user = userManageService.getUserInfoByUserId(userId);
							
							if(user != null) {
								person.put("USER_NAME", user.getPartyName());
							}
						}
					}
				}
				
				Object orgIdObj = person.get("ORG_ID");
				if(orgIdObj != null){
					Long orgId = Long.valueOf(orgIdObj.toString());
					OrgSocialInfoBO orgEntity = orgSocialInfoService.findByOrgId(orgId);
					if(orgEntity != null){
						person.put("ORG_NAME", orgEntity.getOrgName());
						person.put("ORG_CODE", orgEntity.getOrgCode());
					}
				}
			}
		}
		return taskPerson;
	}

	@Override
	public boolean supervise(Long instanceId, Long taskId, UserInfo userInfo,
			String remarks, String userIds, String userNames) {
		String[] userIdArray = null;
		String[] userNameArray = null;
		boolean result = false;
		
		if(StringUtils.isNotBlank(userIds)){
			userIdArray = userIds.split(",");
		}
		if(StringUtils.isNotBlank(userNames)){
			userNameArray = userNames.split(",");
		}
		
		if(userIdArray!=null && userIdArray.length>0 && userNameArray!=null && userNameArray.length>0){
			int userNameArrayLen = userNameArray.length;
			List<Long> userIdList = new ArrayList<Long>();
			List<String> userNameList = new ArrayList<String>();
			Long userId = null;
			
			for(int index = 0, len = userIdArray.length; index < len; index++) {
				userId = null;
				
				try {
					userId = Long.valueOf(userIdArray[index]);
				} catch(NumberFormatException e) {}
				
				if(userId != null && userId > 0) {
					userIdList.add(userId);
					if(userNameArrayLen > index) {
						userNameList.add(userNameArray[index]);
					}
				}
			}
			
			result = this.supervise(instanceId, taskId, userInfo, remarks, userIdList.toArray(new Long[userIdList.size()]), userNameList.toArray(new String[userNameList.size()]));
		}else{
			result = this.supervise(instanceId, taskId, userInfo, remarks, userIds);
		}
		return result;
	}

	@Override
	public boolean supervise(Long instanceId, Long taskId, UserInfo userInfo,
			String remarks, String userIds) {
		Long[] userIdArray = null;
		String[] userNameArray = null;
		
		if(StringUtils.isNotBlank(userIds)){
			String[] userIdStrArray = userIds.split(",");
			
			if(userIdStrArray!=null && userIdStrArray.length>0){
				UserBO userBO = null;
				List<Long> userIdList = new ArrayList<Long>();
				List<String> userNameList = new ArrayList<String>();
				
				for(String userId : userIdStrArray){
					userBO = userManageService.getUserInfoByUserId(Long.parseLong(userId));
					
					if(userBO != null) {
						if(!userIdList.contains(userBO.getUserId())) {
							userIdList.add(userBO.getUserId());
							userNameList.add(userBO.getPartyName());
						}
					}
				}
				
				if(userIdList != null && userIdList.size() > 0) {
					userIdArray = userIdList.toArray(new Long[userIdList.size()]);
					userNameArray = userNameList.toArray(new String[userNameList.size()]);
				}
			}
		}
		
		return this.supervise(instanceId, taskId, userInfo, remarks, userIdArray, userNameArray);
	}

	@Override
	public boolean supervise(Long instanceId, Long taskId, UserInfo userInfo,
			String remarks, Long[] userIds, String[] userNames) {
		return hessianFlowService.supervise(instanceId, taskId, userInfo.getUserId(), userInfo.getPartyName(), remarks, userIds, userNames);
	}

	@Override
	public boolean addAttention(Long eventId, String formTypeId,
			UserInfo userInfo, Date date) {
		Map<String, Object> params = new HashMap<String, Object>();
		String ATTENTIONTYPE_FORM = "2";//表单关注
		
		params.put("beginTime", date);
		params.put("attentionType", ATTENTIONTYPE_FORM);
		
		return hessianFlowService.addAttention(eventId, formTypeId, userInfo.getUserId(), params);
	}

	@Override
	public boolean addAttention(Long eventId, UserInfo userInfo) {
		return this.addAttention(eventId, ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID, userInfo, new Date());
	}
	
	@Override
	public Map<String, Object> curNodeData(Long instanceId){
		Map<String, Object> curNodeData = null;
		
		if(instanceId!=null && instanceId > 0){
			curNodeData = hessianFlowService.getData(instanceId.toString());
		}
		return curNodeData;
	}
	
	@Override
	public String curNodeUserIds(String taskId){
		String userIdsStr = "";
		
		List<Map<String, Object>> taskPerson = this.queryMyTaskParticipation(taskId);
		
		userIdsStr = curNodeUserIds(taskPerson);
		
		return userIdsStr;
	}
	
	@Override
	public String curNodeUserIds(Long instanceId){
		String userIdsStr = "";
		Long taskId = this.curNodeTaskId(instanceId);
		List<Map<String, Object>> taskPerson = this.queryMyTaskParticipation(String.valueOf(taskId));
		
		userIdsStr = curNodeUserIds(taskPerson);
		
		return userIdsStr;
	}

	@Override
	public String curNodeUserIds(List<Map<String, Object>> taskPerson){
		Set<String> userIds = new HashSet<String>();
		String userIdsStr = "";
		
		if(taskPerson!=null && taskPerson.size()>0){
			Object userIdObj = null;
			Object userTypeObj = null;//1表示USER_ID为组织ID；3表示USER_ID为用户ID
			String userType = "";//1表示USER_ID为组织ID；3表示USER_ID为用户ID
			
			for(Map<String, Object> mapTemp : taskPerson){
				userIdObj = mapTemp.get("USER_ID");
				
				if(userIdObj != null) {
					userTypeObj = mapTemp.get("USER_TYPE");//1表示USER_ID为组织ID；3表示USER_ID为用户ID
					userType = "";
					
					if(userTypeObj != null){
						userType = userTypeObj.toString();
					}
					
				if(IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userType)){
						userIds.add(userIdObj.toString());
				}else if(IEventDisposalWorkflowService.ACTOR_TYPE_ORG.equals(userType)){//获取该组织下的所有用户
						List<UserBO> userBOList = userManageService.getUserListByUserExampleParamsOut(null, null, Long.valueOf(userIdObj.toString()));
						
						if(userBOList!=null && userBOList.size()>0){
							for(UserBO userBO : userBOList){
								userIds.add(String.valueOf(userBO.getUserId()));
							}
						}
					}
				}
			}
			
			if(userIds.size() > 0){
				userIdsStr = StringUtils.join(userIds, ",");
			}
		}
		
		return userIdsStr;
	}
	
	@Override
	public String curNodeUserNames(Long instanceId){
		String userIdStr = this.curNodeUserIds(instanceId);
		StringBuffer userNames = new StringBuffer("");
		String userNameStr = "";
		
		if(StringUtils.isNotBlank(userIdStr)){
			String[] userIds = userIdStr.split(",");
			String userIdTmp = "";
			UserBO userBo = null;
					
			for(int index = 0, length = userIds.length; index < length; index++){
				userIdTmp = userIds[index];
				if(StringUtils.isNotBlank(userIdTmp)){
					userBo = userManageService.getUserInfoByUserId(Long.valueOf(userIdTmp));
					if(userBo != null){
						userNames.append(",").append(userBo.getPartyName());
					}
				}
			}
			
			if(userNames.length() > 0){
				userNameStr = userNames.substring(1);
			}
		}
		
		return userNameStr;
	}
	
	@Override
	public String curNodeUserNames(String userIdStr){
		StringBuffer userNames = new StringBuffer("");
		String userNameStr = "";
		
		if(StringUtils.isNotBlank(userIdStr)){
			String[] userIds = userIdStr.split(",");
			String userIdTmp = "";
			UserBO userBo = null;
					
			for(int index = 0, length = userIds.length; index < length; index++){
				userIdTmp = userIds[index];
				if(StringUtils.isNotBlank(userIdTmp)){
					userBo = userManageService.getUserInfoByUserId(Long.valueOf(userIdTmp));
					if(userBo != null){
						userNames.append(",").append(userBo.getPartyName());
					}
				}
			}
			
			if(userNames.length() > 0){
				userNameStr = userNames.substring(1);
			}
		}
		
		return userNameStr;
	}
	
	@Override
	public Long curNodeTaskId(Long instanceId) {
		Map<String, Object> curNodeData = this.curNodeData(instanceId);
		Long taskId = -1L;
		
		if(curNodeData != null){
			Object taskIdObj = curNodeData.get("WF_DBID_");
			if(taskIdObj != null){
				String taskIdStr = taskIdObj.toString();
				if(StringUtils.isNotBlank(taskIdStr)){
					taskId = Long.valueOf(taskIdStr);
				}
			}
		}
		return taskId;
	}
	
	@Override
	public String curNodeTaskName(Long instanceId) throws Exception {
		Map<String, Object> curNodeData = this.curNodeData(instanceId);
		String taskName = "";
		
		if(curNodeData != null) {
			Object taskNameObj = curNodeData.get("WF_ACTIVITY_NAME_");
			if(taskNameObj != null){
				taskName = taskNameObj.toString();
			}
		}
		
		if(StringUtils.isBlank(taskName)) {
			logger.error("任务已完结！无法获取当前任务名称！");
			throw new Exception("任务已完结！");
		}
		
		return taskName;
	}
	
	@Override
	public Map<String, Object> capTurnLevel(String curnodeName, String nextNodeName, UserInfo userInfo) {
		Map<String, Object> result = new HashMap<String, Object>();
		String noteType = ConstantValue.DEFAULT_NOTE; //短信模板编号
		int level = 0;	//当前操作等级 0 分流；1 上报；2 越级上报； 3 结案； 5 督办； 6 催办；
		
		if("事件采集".equals(curnodeName)) {
			level = ConstantValue.EVENT_START;
		}
		
		// 上报
		else if("网格员处理".equals(curnodeName) && "村(社区)处理".equals(nextNodeName)) {
			level = ConstantValue.EVENT_REPORT;
			result.put("positionName", funConfigurationService.turnCodeToValue(ConstantValue.EVENT_WORFLOW_POSITIONNAME1, ConstantValue.EVENT_WORFLOW_POSITIONNAME1, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0));
		} else if("村(社区)处理".equals(curnodeName) && "乡镇(街道)处理".equals(nextNodeName)) {
			level = ConstantValue.EVENT_REPORT;
			result.put("positionName", funConfigurationService.turnCodeToValue(ConstantValue.EVENT_WORFLOW_POSITIONNAME2, ConstantValue.EVENT_WORFLOW_POSITIONNAME2, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0));
		} else if("乡镇(街道)处理".equals(curnodeName) && "县(区)处理".equals(nextNodeName)) {
			level = ConstantValue.EVENT_REPORT;
			result.put("positionName", funConfigurationService.turnCodeToValue(ConstantValue.EVENT_WORFLOW_POSITIONNAME3, ConstantValue.EVENT_WORFLOW_POSITIONNAME3, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0));
		}
		// 越级
		else if("网格员处理".equals(curnodeName) && "乡镇(街道)处理".equals(nextNodeName)) {
			level = ConstantValue.EVENT_UP_REPORT;
			result.put("positionName", funConfigurationService.turnCodeToValue(ConstantValue.EVENT_WORFLOW_POSITIONNAME4, ConstantValue.EVENT_WORFLOW_POSITIONNAME4, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0));
		}
		else if("村(社区)处理".equals(curnodeName) && "县(区)处理".equals(nextNodeName)) {
			level = ConstantValue.EVENT_UP_REPORT;
			result.put("positionName", funConfigurationService.turnCodeToValue(ConstantValue.EVENT_WORFLOW_POSITIONNAME5, ConstantValue.EVENT_WORFLOW_POSITIONNAME5, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0));
		} 
		//结案
		else if("结案".equals(nextNodeName)){
			level = ConstantValue.EVENT_CLOSE;
			result.put("positionName", "");
		}
		//归档
		else if("归档".equals(nextNodeName)){
			level = ConstantValue.EVENT_END;
			result.put("positionName", "");
		}
		//督办
		else if("督办".equals(nextNodeName)){
			level = ConstantValue.EVENT_SUPERVISE;
			result.put("positionName", "");
		}
		//催办
		else if("催办".equals(nextNodeName)){
			level = ConstantValue.EVENT_URGE;
			result.put("positionName", "");
		}
		// 其他
		else {
			level = ConstantValue.EVENT_SHUNT;
			result.put("positionName", funConfigurationService.turnCodeToValue(ConstantValue.EVENT_WORFLOW_POSITIONNAME6, ConstantValue.EVENT_WORFLOW_POSITIONNAME6, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0));
		}
		
		switch(level){
			case 0: noteType = ConstantValue.SHUNT_NOTE;break;
			case 1:
			case 2: noteType = ConstantValue.REPORT_NOTE;break;
			case 3: noteType = ConstantValue.CLOSE_NOTE;break;
			case 5: noteType = ConstantValue.REMIND_NOTE;break;
			case 6: noteType = ConstantValue.URGE_NOTE;break;
			default: noteType = ConstantValue.DEFAULT_NOTE;break;
		}
		
		result.put("level", level);
		result.put("noteType", noteType);
		
		return result;
	}
	
	@Override
	public String capNextNodeName(String curNodeName, int level){
		String nextNodeName = "";
		switch(level){
			case 1:{//上报
				if("网格员处理".equals(curNodeName)) {
					nextNodeName = "村(社区)处理";
				} else if("村(社区)处理".equals(curNodeName)) {
					nextNodeName = "乡镇(街道)处理";
				} else if("乡镇(街道)处理".equals(curNodeName)) {
					nextNodeName = "县(区)处理";
				} else if("县(区)处理".equals(curNodeName)) {
					nextNodeName = "市级处理";
				}
				break;
			}
		}
		
		return nextNodeName;
	}
	
	@Override
	public Long capWorkflowId(Long instanceId, Long eventId, UserInfo userInfo, Map<String, Object> extraParam){
		Long workflowId = -1L;
		Long orgId = userInfo.getOrgId();//用户所在组织编号
		ProInstance pro = null;
		
		if(instanceId != null && instanceId > 0) {
			pro = this.capProInstanceByInstanceId(instanceId);
		}
		
		if(pro != null) {
			workflowId = pro.getWorkFlowId();
		} else if(orgId != null && orgId > 0){//启动流程时，会使用到
			String workflowName = this.capWorkflowName(eventId, null, userInfo);
			if(StringUtils.isBlank(workflowName)) {
				workflowName = ConstantValue.EVENT_WORKFLOW_NAME;
			}
			
			Workflow workflow = hessianFlowService.getWorkflowByWfTypeIdAndOrgIdCircle(workflowName, ConstantValue.EVENT_WORKFLOW_WFTYPEID, String.valueOf(orgId));
			if(workflow != null){
				workflowId = workflow.getWorkFlowId();
			}
		}
		
		return workflowId;
	}

	@Override
	public String capWorkflowName(Long eventId, String eventType, UserInfo userInfo) {
		String workflowName = "";
		StringBuffer trigCondition = new StringBuffer("");
			
		/*if(StringUtils.isBlank(eventType) && (eventId != null && eventId > 0)) {
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			if(event != null) {
				eventType = event.getType();
			}
		}
		
		if(StringUtils.isNotBlank(eventType)) {
			trigCondition += "_" + eventType;
		}
		
		workflowName = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, trigCondition, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.DIRECTION_UP_FUZZY);
		*/
		if(eventId != null && eventId > 0) {
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			if(event != null) {
				String bizPlatform = event.getBizPlatform();

				eventType = event.getType();

				if(StringUtils.isNotBlank(eventType)) {
					trigCondition.append("[");

					trigCondition.append(ConstantValue.WORKFLOW_NAME).append("_").append(eventType);

					if(StringUtils.isNotBlank(bizPlatform) && !ConstantValue.DEFAULT_EVENT_BIZ_PLATFORM.equals(bizPlatform)) {
						trigCondition.append("|").append(ConstantValue.WORKFLOW_NAME).append("_").append(bizPlatform).append("_").append(eventType);
					}

					trigCondition.append("]");
				}
			}
		}

		if(trigCondition.length() == 0) {
			trigCondition.append(ConstantValue.WORKFLOW_NAME);
		}

		workflowName = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, trigCondition.toString(), IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.DIRECTION_UP_REGEXP);

		return workflowName;
	}
	
	@Override
	public String capWorkflowHandlePage(Long eventId, String eventType, UserInfo userInfo, Map<String, Object> extraParam) {
		String handlePage = "",
			   workflowName = null,
			   HANDLE_PAGE_TEMPLATE = ConstantValue.HANDLE_EVENT_PAGE + "@workflowName@@eventType@",
			   handlePageTrig = null;
		StringBuffer trigCondition = new StringBuffer("");
		
		if(CommonFunctions.isNotBlank(extraParam, "workflowName")) {
			workflowName = extraParam.get("workflowName").toString();
		} else if(eventId != null && eventId > 0) {
			ProInstance proInstance = capProInstanceByEventId(eventId);
			
			if(proInstance != null) {
				workflowName = proInstance.getProName();
			}
		}
		
		if(StringUtils.isBlank(eventType) && (eventId != null && eventId > 0)) {
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			if(event != null) {
				eventType = event.getType();
			}
		}
		
		eventType = StringUtils.isBlank(eventType) ? "" : "_" + eventType;
		
		handlePageTrig = HANDLE_PAGE_TEMPLATE.replace("@eventType@", eventType);
		
		trigCondition.append("[").append(handlePageTrig.replace("@workflowName@", ""));
		
		if(StringUtils.isNotBlank(workflowName)) {
			trigCondition.append("|").append(handlePageTrig.replace("@workflowName@", "-" + workflowName));
		}
		
		trigCondition.append("]");
		
		handlePage = funConfigurationService.turnCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, trigCondition.toString(), IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_REGEXP);
		
		if(StringUtils.isBlank(handlePage)) {
			handlePage = "handle_event_node.ftl";//新的事件办理页面
		}
		
		return handlePage;
	}
	
	@Override
	public boolean isNewWorkflow(Long instanceId) {
		boolean isNewWorkflow = false;
		
		if(instanceId!=null && instanceId>0){
			isNewWorkflow = hessianFlowService.isNewVersion(instanceId);
		}
		
		return isNewWorkflow;
	}

	@Override
	public boolean isNewWorkflow(String instanceId) {
		boolean isNewWorkflow = false;
		Long instanceIdL = -1L;
		
		if(StringUtils.isNotBlank(instanceId)){
			try{
				instanceIdL = Long.valueOf(instanceId);
			}catch(Exception e){
				instanceIdL = -1L;
				e.printStackTrace();
			}
		}
		
		if(instanceIdL > 0){
			isNewWorkflow = this.isNewWorkflow(instanceIdL);
		}
		
		return isNewWorkflow;
	}
	
	@Override
	public Long saveOrUpdateTask(Map<String, Object> params){
		Long resultTaskId = -1L;								//结果返回任务id
		Long taskId = null;										//任务Id,null表示新插入一个task,不为null时表示更新已有task
		Long eventId = null;									//事件id 必填
		Long instanceId = null;									//流程实例Id
		String taskName = "";									//任务名称 必填
		Long transactorId = null;								//任务办理人id
		String transactOrgId = null;							//办理组织
		String transactOrgName = "";							//办理组织名称 必填
		String transactorName = "";								//任务办理人姓名 为空时，默认成办理组织名称
		String taskAdvice = "";									//办理意见
		String taskCreateTime = null;							//任务创建时间  为null时，使用工作流所在服务器时间
		Long taskCreateUserId = null;							//任务创建用户
		String taskCreateUserName = "";							//任务创建用户姓名
		String taskReadTime = "";								//任务接收时间
		Long taskReadUserId = null;								//任务接收用户
		String taskReadUserName = "";							//任务接收用户姓名
		String taskEndTime = null;								//任务结束时间 任务办理时间 为null时，使用工作流所在服务器时间
		String taskType = "";									//任务类型:1、普通任务 2、 会签任务 默认为1
		String taskOperateType = "";							//操作类型:1、通过  默认为1
		Map<String, Object> extraParam = new HashMap<String, Object>();	//额外的参数
		
		StringBuffer msgWrong = new StringBuffer("");			//错误信息
		StringBuffer msgAlert = new StringBuffer("");			//警告信息
		String instanceIdStr = "";								//流程实例id字符串
		
		if(CommonFunctions.isNotBlank(params, "taskId")){
			String taskIdStr = params.get("taskId").toString();
			if(StringUtils.isNotBlank(taskIdStr)){
				try{
					taskId = Long.valueOf(taskIdStr);
				}catch(NumberFormatException e){
					msgWrong.append("参数[taskId]").append(taskIdStr).append("不能转换为Long型数据！");
					e.printStackTrace();
				}
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "eventId")) {
			String eventIdStr = params.get("eventId").toString();
			if(StringUtils.isNotBlank(eventIdStr)){
				try{
					eventId = Long.valueOf(eventIdStr);
				}catch(NumberFormatException e){
					msgWrong.append("参数[eventId]：").append(eventIdStr).append("不能转换为Long型数据！");
					e.printStackTrace();
				}
			}
		}else{
			msgWrong.append("缺少参数[eventId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "instanceId")){
			instanceIdStr = params.get("instanceId").toString();
		}else if(eventId != null){
			instanceIdStr = this.capInstanceIdByEventId(eventId);
		}
		
		if(StringUtils.isNotBlank(instanceIdStr)) {
			try{
				instanceId = Long.valueOf(instanceIdStr);
			}catch(NumberFormatException e){
				msgWrong.append("请检查参数[instanceId]！");
				e.printStackTrace();
			}
		}
		
		if(instanceId == null || instanceId < 0) {
			msgWrong.append("缺少有效的流程id[instanceId]！");
		}
		
		if(CommonFunctions.isNotBlank(params, "taskName")){
			taskName = params.get("taskName").toString();
		}else{
			msgWrong.append("缺少参数[taskName]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "transactorId")){
			String transactorIdStr = params.get("transactorId").toString();
			if(StringUtils.isNotBlank(transactorIdStr)){
				try{
					transactorId = Long.valueOf(transactorIdStr);
				}catch(NumberFormatException e){
					msgAlert.append("参数[transactorId]：").append(transactorIdStr).append("不能转换为Long型数据！");
					e.printStackTrace();
				}
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "transactOrgId")){
			transactOrgId = params.get("transactOrgId").toString();
		}
		
		if(CommonFunctions.isNotBlank(params, "transactOrgName")){
			transactOrgName = params.get("transactOrgName").toString();
		}else if(StringUtils.isNotBlank(transactOrgId)){
			Long transactOrgIdL = -1L;
			try{
				transactOrgIdL = Long.valueOf(transactOrgId);
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
			
			if(transactOrgIdL > 0){
				OrgSocialInfoBO orgSocialInfo = orgSocialInfoService.findByOrgId(transactOrgIdL);
				if(orgSocialInfo != null){
					transactOrgName = orgSocialInfo.getOrgName();
				}
			}
		}else{
			msgWrong.append("缺少参数[transactOrgName]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "transactorName")){
			transactorName = params.get("transactorName").toString();
		}else if(transactorId != null){
			UserBO userInfo = userManageService.getUserInfoByUserId(transactorId);
			if(userInfo != null){
				transactorName = userInfo.getPartyName();
			}
		}else if(taskId == null && StringUtils.isNotBlank(transactOrgName)){
			transactorName = transactOrgName;
		}
		
		if(CommonFunctions.isNotBlank(params, "taskAdvice")){
			int taskAdviceLength = 2048;
			String taskAdviceLengthStr = funConfigurationService.turnCodeToValue(ConstantValue.VALIDATE_LENGTH_FUNCTION_CODE, VALIDATE_LENGTH_TASK_ADVICE, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			
			if(StringUtils.isNotBlank(taskAdviceLengthStr)){
				try{
					taskAdviceLength = Integer.valueOf(taskAdviceLengthStr);
				}catch(NumberFormatException e){
					msgAlert.append("taskAdviceLength长度配置获取失败，因为").append(taskAdviceLengthStr).append("不能转换为数字！");
				}
			}
			
			if(CommonFunctions.isLengthValidate(params, VALIDATE_LENGTH_TASK_ADVICE, taskAdviceLength)){
				taskAdvice = params.get("taskAdvice").toString();
			}else{
				msgWrong.append(params.get("msg").toString());
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "taskCreateTime")){
			taskCreateTime = params.get("taskCreateTime").toString();
			try {
				DateUtils.convertStringToDate(taskCreateTime, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				msgWrong.append("参数[taskCreateTime]：不符合时间格式").append(DateUtils.PATTERN_24TIME);
				e.printStackTrace();
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "taskCreateUserId")){
			String taskCreateUserIdStr = params.get("taskCreateUserId").toString();
			if(StringUtils.isNotBlank(taskCreateUserIdStr)){
				try{
					taskCreateUserId = Long.valueOf(taskCreateUserIdStr);
				}catch(NumberFormatException e){
					msgAlert.append("参数[taskCreateUserId]：").append(taskCreateUserIdStr).append("不能转换为Long型数据！");
					e.printStackTrace();
				}
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "taskCreateUserName")){
			taskCreateUserName = params.get("taskCreateUserName").toString();
		}else if(taskCreateUserId != null){
			UserBO userInfo = userManageService.getUserInfoByUserId(taskCreateUserId);
			if(userInfo != null){
				taskCreateUserName = userInfo.getPartyName();
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "taskReadTime")){
			taskReadTime = params.get("taskReadTime").toString();
			try {
				DateUtils.convertStringToDate(taskReadTime, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				msgWrong.append("参数[taskReadTime]：不符合时间格式").append(DateUtils.PATTERN_24TIME);
				e.printStackTrace();
			}
		}else if(taskId == null) {
			try {
				taskReadTime = DateUtils.convertDateToString(new Date());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "taskReadUserId")){
			String taskReadUserIdStr = params.get("taskReadUserId").toString();
			if(StringUtils.isNotBlank(taskReadUserIdStr)){
				try{
					taskReadUserId = Long.valueOf(taskReadUserIdStr);
				}catch(NumberFormatException e){
					msgAlert.append("参数[taskReadUserId]：").append(taskReadUserIdStr).append("不能转换为Long型数据！");
					e.printStackTrace();
				}
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "taskReadUserName")){
			taskReadUserName = params.get("taskReadUserName").toString();
		}else if(taskReadUserId != null){
			UserBO userInfo = userManageService.getUserInfoByUserId(taskReadUserId);
			if(userInfo != null){
				taskReadUserName = userInfo.getPartyName();
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "taskEndTime")){
			taskEndTime = params.get("taskEndTime").toString();
			try {
				DateUtils.convertStringToDate(taskEndTime, DateUtils.PATTERN_24TIME);
			} catch (ParseException e) {
				msgWrong.append("参数[taskEndTime]：不符合时间格式").append(DateUtils.PATTERN_24TIME);
				e.printStackTrace();
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "taskType")){
			taskType = params.get("taskType").toString();
			if(TASK_TYPE_ORDINARY.equals(taskType)){
				
			}else if(TASK_TYPE_COUNTERSIGNATURE.equals(taskType)){
				
			}else{
				taskType = TASK_TYPE_ORDINARY;
			}
		} else if(taskId == null) {
			taskType = TASK_TYPE_ORDINARY;
		}
		
		if(CommonFunctions.isNotBlank(params, "taskOperateType")){
			taskOperateType = params.get("taskOperateType").toString();
			if(TASK_OPERATE_TYPE_PASS.equals(taskOperateType)){
				
			}else if(TASK_OPERATE_TYPE_REJECT.equals(taskOperateType)){
				
			}else{
				taskOperateType = TASK_OPERATE_TYPE_PASS;
			}
		} else if(taskId == null) {
			taskOperateType = TASK_OPERATE_TYPE_PASS;
		}
		
		//额外参数设置
		if(CommonFunctions.isNotBlank(params, "type")) {
			extraParam.put("type", params.get("type"));
		}
		
		if(msgAlert.length() > 0){
			logger.info("新增工作流任务，有如下警告信息："+msgAlert);
		}
		if(msgWrong.length() > 0){
			logger.info("新增工作流任务，有如下报错信息："+msgWrong);
		}else{
			resultTaskId = hessianFlowService.saveOrUpdateTask(taskId, instanceId, eventId, taskName, transactorId, 
					transactorName, transactOrgId, transactOrgName, taskAdvice, taskCreateTime, taskCreateUserId, 
					taskCreateUserName, taskReadTime, taskReadUserId, taskReadUserName, taskEndTime, taskType, taskOperateType, extraParam);
			
			if(resultTaskId!=null && resultTaskId>0){
				if(CommonFunctions.isNotBlank(params, "bizPlatform")){//获取对方的关联字段
					String bizPlatform = params.get("bizPlatform").toString();
					
					bizPlatform = baseDictionaryService.changeCodeToName(ConstantValue.BIZ_PLATFORM_PCODE, bizPlatform, null);
					if(StringUtils.isNotBlank(bizPlatform)){
						
						if(CommonFunctions.isNotBlank(params, "oppoSideBizCode")){//获取对方的关联字段
							String oppoSideBizCode = params.get("oppoSideBizCode").toString();
							
							if(StringUtils.isNotBlank(oppoSideBizCode)){
								dataExchangeStatusService.saveDataExchangeStatusForTaskNew(bizPlatform, null, oppoSideBizCode, String.valueOf(resultTaskId));
							}
						}
					}
				}
			}
		}
		
		return resultTaskId==null ? -1L : resultTaskId;
	}
	
	@Override
	public boolean isUserInfoCurrentUser(String taskId, String instanceId, UserInfo userInfo) throws Exception {
		boolean isCurrentUser = false;//判断当前登录人员是否在为当前办理人
		Long instanceIdL = -1L;
		
		if(StringUtils.isNotBlank(instanceId)) {
			instanceIdL = Long.valueOf(instanceId);
		}
		
		if(instanceIdL > 0) {
			isCurrentUser = this.isUserInfoCurrentUser(taskId, instanceIdL, userInfo);
		}
		
		return isCurrentUser;
	}
	
	@Override
	public boolean isUserInfoCurrentUser(String taskId, Long instanceId, UserInfo userInfo) throws Exception {
		boolean isCurrentUser = false;//判断当前登录人员是否在为当前办理人
		
		if(StringUtils.isNotBlank(taskId) && userInfo != null) {
			Long curTaskId = this.curNodeTaskId(instanceId);
			
			if(curTaskId != null && curTaskId > 0 && taskId.equals(curTaskId.toString())) {//当传入的taskId为当前任务id时，才继续
				List<Map<String, Object>> taskPerson = this.queryMyTaskParticipation(taskId);
				
				if(taskPerson != null && taskPerson.size() > 0) {
					String userType = null;//1表示USER_ID为组织ID；3表示USER_ID为用户ID
					Long participantUserId = -1L, participantOrgId = -1L, 
						 userId = userInfo.getUserId(), userOrgId = userInfo.getOrgId();
					
					for(Map<String, Object> mapTemp : taskPerson) {
						userType = null;
						participantUserId = -1L;
						participantOrgId = -1L;
						
						if(CommonFunctions.isNotBlank(mapTemp, "USER_TYPE")) {
							userType = mapTemp.get("USER_TYPE").toString();
							
							if(CommonFunctions.isNotBlank(mapTemp, "USER_ID")) {
								participantUserId = Long.valueOf(mapTemp.get("USER_ID").toString());
							}
							
							if(participantUserId > 0) {
								if(IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userType) && participantUserId.equals(userId)) {
									if(CommonFunctions.isNotBlank(mapTemp, "ORG_ID")) {
										participantOrgId = Long.valueOf(mapTemp.get("ORG_ID").toString());
									}
									
									if(participantOrgId > 0) {
										isCurrentUser = participantOrgId.equals(userOrgId);
									}
								} else if(IEventDisposalWorkflowService.ACTOR_TYPE_ORG.equals(userType) && participantUserId.equals(userOrgId)) {
									isCurrentUser = true;
								}
								
								if(isCurrentUser) {
									break;
								}
							}
						}
					}
					
				}
			}
			
			if(!isCurrentUser) {
				String msgWrong = "用户["+ userInfo.getOrgName() +"-"+ userInfo.getPartyName() +"] 不是当前环节[" + this.curNodeTaskName(Long.valueOf(instanceId)) + "] 的办理人员！";
				throw new Exception(msgWrong.toString());
			}
		}
		
		return isCurrentUser;
	}
	
	@Override
	public boolean isUserAbleToStartWorkflow(Long orgId, Long userId) throws Exception{
		boolean flag = false;
		
		int resultCode = checkUserToStartWorkflow(orgId, userId);
		
		flag = resultCode == 1;
		
		return flag;
	}
	
	@Override
	public boolean isUserAbleToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception{
		boolean flag = false;
		
		int resultCode = checkUserToStartWorkflow(orgSocialInfo, userId);
		
		flag = resultCode == 1;
		
		return flag;
	}
	
	@Override
	public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {
		int resultCode = 0;
		String wrongMsg = "当前组织不存在，请先检验！";
		
		if(orgSocialInfo != null){
			String chiefLevel = orgSocialInfo.getChiefLevel();
			String orgType = orgSocialInfo.getOrgType();
			boolean isGridAdmin = fiveKeyElementService.isUserIdGridAdmin(orgSocialInfo.getOrgId(), userId);
			
			if(ORG_TYPE_UNIT.equals(orgType) || ORG_TYPE_DEPARTMENT.equals(orgType)){
				if(!isGridAdmin && GRID_INFO_ORG_CHIEF_LEVEL.equals(chiefLevel)){
					resultCode = -2;
					wrongMsg = "当前用户不是网格员，请先配置！";
				}else{
					resultCode = 1;
					wrongMsg = "";
				}
			}else{
				resultCode = -1;
				wrongMsg = "当前组织的组织类型有误：既不是单位，也不是部门。请先修正！";
			}
		}
		
		if(StringUtils.isNotBlank(wrongMsg)) {
			throw new Exception(wrongMsg);
		}
		
		return resultCode;
	}
	
	@Override
	public int checkUserToStartWorkflow(Long orgId, Long userId) throws Exception {
		int resultCode = 0;
		
		if(orgId!=null && orgId>0){
			OrgSocialInfoBO orgSocialInfo = orgSocialInfoService.findByOrgId(orgId);
			//为了能够转换到各自实现的方法，如果直接调用checkUserToStartWorkflow，只会调用到自身的
			resultCode = eventDisposalWorkflowService.checkUserToStartWorkflow(orgSocialInfo, userId);
		}
		
		return resultCode;
	}
	
	@Override
	public List<Map<String, Object>> findNodeActorsById(Integer nodeId){
		List<Map<String, Object>> nodeActors = hessianFlowService.getNodeActors(nodeId);
		if(nodeActors!=null && nodeActors.size()>0){
			String actorId = "", actorType = "";
			
			for(Map<String, Object> nodeActor : nodeActors){
				actorId = "";
				actorType = "";
				
				if(CommonFunctions.isNotBlank(nodeActor, "ACTOR_TYPE")){
					actorType = nodeActor.get("ACTOR_TYPE").toString();
				}
				
				if(CommonFunctions.isNotBlank(nodeActor, "ACTOR_ID")){
					actorId = nodeActor.get("ACTOR_ID").toString();
				}
				
				if(ACTOR_TYPE_ORG.equals(actorType)){
					nodeActor.put("ORG_ID", actorId);
				}else if(ACTOR_TYPE_USER.equals(actorType)){
					nodeActor.put("USER_ID", actorId);
				} else if(ACTOR_TYPE_POSITION.equals(actorType)) {
					nodeActor.put("POSITION_ID", actorId);
				}  else if(ACTOR_TYPE_ROLE.equals(actorType)) {
					nodeActor.put("ROLE_ID", actorId);
				}
			}
		}
		
		return nodeActors;
	}
	
	@Override
	public Node findNodeById(Integer nodeId){
		Node node = null;
		
		if(nodeId!=null /*&& nodeId>0*/){
			node = hessianFlowService.getNodeByNodeId(nodeId);
		}
		
		return node;
	}
	
	@Override
	public Node findNodeById(Long instanceId, String nodeName){
		Node node = null;
		
		if(instanceId != null && instanceId > 0 && StringUtils.isNotBlank(nodeName)) {
			node = hessianFlowService.getNode(instanceId, nodeName);
		}
		
		return node;
	}
	
	@Override
	public String capInstanceIdByEventId(Long eventId){
		String instanceId = "";
		
		if(eventId != null && eventId > 0) {
			Map<String, Object> wfMap = hessianFlowService.queryWfInstanceByformId(ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID, eventId.toString());
			if(wfMap != null){
				Object instanceIdObj = wfMap.get("INSTANCE_ID");//获取流程实例id
				if(instanceIdObj != null){
					instanceId = instanceIdObj.toString();
				}
			}
		} else {
			throw new IllegalArgumentException("缺少有效的事件id！");
		}
		
		return instanceId;
	}
	
	@Override
	public String capInstanceIdByEventId(Long eventId, Map<String, Object> params) {
		String instanceId = capInstanceIdByEventId(eventId);
		
		return instanceId;
	}
	
	@Override
	public Long capInstanceIdByEventIdForLong(Long eventId) {
		Long instanceId = -1L;
		String instanceIdStr = this.capInstanceIdByEventId(eventId);
		
		if(StringUtils.isNotBlank(instanceIdStr)) {
			try {
				instanceId = Long.valueOf(instanceIdStr);
			} catch(NumberFormatException e) {
				instanceId = -1L;
				e.printStackTrace();
			}
		}
		
		return instanceId;
	}
	
	@Override
	public Long capInstanceIdByEventIdForLong(Long eventId, Map<String, Object> params) {
		Long instanceId = capInstanceIdByEventIdForLong(eventId);
		
		return instanceId;
	}
	
	@Override
	public ProInstance capProInstanceByEventId(Long eventId) {
		ProInstance pro = null;
		
		try{
			String instanceId = this.capInstanceIdByEventId(eventId);
			pro = this.capProInstanceByInstanceId(Long.valueOf(instanceId));
		} catch(NumberFormatException e) {
			pro = null;
		}
		
		return pro;
	}
	
	@Override
	public ProInstance capProInstanceByEventId(Long eventId, Map<String, Object> params) {
		ProInstance pro = capProInstanceByEventId(eventId);
		
		return pro;
	}
	
	@Override
	public ProInstance capProInstanceByInstanceId(Long instanceId) {
		ProInstance pro = null;
		
		if(instanceId != null && instanceId > 0) {
			pro = hessianFlowService.getProByInstanceId(instanceId);
		}
		
		return pro;
	}
	
	@Override
	public Map<String, Object> findFormByTaskId(Long taskId, Map<String, Object> params) throws Exception {
		
		return baseWorkflowService.findFormByTaskId(taskId, params);
	}
	
	@Override
	public Map<String, Object> capDoneTaskInfo(Long instanceId, String taskNodeCode) {
		Map<String, Object> taskInfoMap = null;
		
		if(instanceId != null && instanceId > 0 && StringUtils.isNotBlank(taskNodeCode)) {
			taskInfoMap = hessianFlowService.getDoneTaskInfo(instanceId, taskNodeCode);
		}
		
		return taskInfoMap;
	}
	
	@Override
	public Map<String, Object> findDoneTaskInfoLatest(Long instanceId, String taskNodeCode) {
		Map<String, Object> taskMap = null;
		
		if(instanceId != null && instanceId > 0 && StringUtils.isNotBlank(taskNodeCode)) {
			MyTask task = hessianFlowService.findDoneTaskInfoLatest(instanceId, taskNodeCode);
			if(task != null) {
				taskMap = new HashMap<String, Object>();
				
				taskMap.put("taskId", task.getTaskId());
				taskMap.put("taskName", task.getTaskName());
				taskMap.put("operateType", task.getOperateType());
				taskMap.put("orgId", task.getOrgId());
				taskMap.put("orgName", task.getOrgName());
				taskMap.put("transactorId", task.getTransactorId());
				taskMap.put("transactorName", task.getTransactorName());
			}
		}
		
		return taskMap;
	}

	@Override
	public Map<String, Object> turnTodoPerson(Map<String, Object> params) {
		Map<String, Object> resulMap = new HashMap<String, Object>();
		
		if(params != null && !params.isEmpty()) {
			resulMap = hessianFlowService.turnTodoPerson(params);
		}
		
		return resulMap;
	}

	@Override
	public EUDGPagination queryTaskMngList(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo<1 ? 1 : pageNo;
		pageSize = pageSize<1 ? 10 : pageSize;
		
		return hessianFlowService.queryTaskMngList(pageNo, pageSize, params);
	}
	
	@Override
	public boolean receiveTask(TaskReceive taskReceive, UserInfo userInfo,
			Map<String, Object> extraParam) {
		Long userId = taskReceive.getUserId(), 
			 instanceId = taskReceive.getInstanceId(),
			 orgId = taskReceive.getOrgId();
		String userName = taskReceive.getUserName(),
			   orgName = taskReceive.getOrgName();
		Date receiveTime = taskReceive.getReceiveTime();
		
		if(userId == null || userId < 0) {
			userId = userInfo.getUserId();
			taskReceive.setUserId(userId);
		}
		if(orgId == null || orgId < 0) {
			orgId = userInfo.getOrgId();
			taskReceive.setOrgId(orgId);
		}
		if(instanceId == null || instanceId < 0) {
			if(CommonFunctions.isNotBlank(extraParam, "eventId")) {
				Long eventId = -1L;
				try {
					eventId = Long.valueOf(extraParam.get("eventId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if(eventId != null && eventId > 0) {
					instanceId = this.capInstanceIdByEventIdForLong(eventId);
					if(instanceId != null && instanceId > 0) {
						taskReceive.setInstanceId(instanceId);
					}
				}
			}
		}
		if(StringUtils.isBlank(userName)) {
			userName = userInfo.getPartyName();
			taskReceive.setUserName(userName);
		}
		if(StringUtils.isBlank(orgName)) {
			orgName = userInfo.getOrgName();
			taskReceive.setOrgName(orgName);
		}
		if(receiveTime == null) {
			taskReceive.setReceiveTime(new Date());
		}
		
		hessianFlowService.addTaskReceive(taskReceive);
		
		return true;
	}
	
	@Override
	public List<TaskReceive> findTaskReceivedList(Long taskId, Long instanceId,
			Map<String, Object> extraParam) {
		List<TaskReceive> taskReceivedList = null;
		
		if(instanceId == null || instanceId < 0) {
			if(CommonFunctions.isNotBlank(extraParam, "eventId")) {
				Long eventId = -1L;
				try {
					eventId = Long.valueOf(extraParam.get("eventId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if(eventId != null && eventId > 0) {
					instanceId = this.capInstanceIdByEventIdForLong(eventId);
				}
			}
		}
		
		if(taskId != null && taskId > 0 && instanceId != null && instanceId > 0) {
			taskReceivedList = hessianFlowService.getTaskReceives(taskId, instanceId);
		}
		
		return taskReceivedList;
	}
	
	@Override
	public Workflow queryWorkflowById(Long workflowId) {
		Workflow workflow = null, resultWorkflow = null;
		
		if(workflowId != null && workflowId > 0) {
			workflow = new Workflow();
			
			workflow.setWorkFlowId(workflowId);
		}
		
		List<Workflow> workflowList = queryWorkflows(workflow);
		
		if(workflowList != null && workflowList.size() > 0) {
			resultWorkflow = workflowList.get(0);
		}
		
		return resultWorkflow;
	}

	@Override
	public List<Workflow> queryWorkflows(Workflow workflow) {
		String status = "";
		
		if(workflow == null) {
			workflow = new Workflow();
		} else {
			status = workflow.getStatus();
		}
		
		if(StringUtils.isBlank(status)) {//默认设置有效状态
			status = "1";
		}
		
		workflow.setStatus(status);
		
		return hessianFlowService.queryWorkflows(workflow);
	}

	@Override
	public List<Node> queryNodes(Long workflowId, Integer deploymentId) {
		Node node = new Node();
		
		if(workflowId != null && workflowId > 0) {
			node.setWorkFlowId(workflowId);
		}
		
		if(deploymentId != null && deploymentId > 0) {
			node.setDeploymentId(deploymentId);
		}
		
		return queryNodes(node);
	}
	
	@Override
	public List<Node> queryNodes(Long workflowId) {
		Integer deploymentId = 0;
		
		if(workflowId != null && workflowId > 0) {
			Workflow workflow = this.queryWorkflowById(workflowId);
			
			if(workflow != null) {
				deploymentId = workflow.getDeploymentId();
			}
		}
		
		return queryNodes(workflowId, deploymentId);
	}

	@Override
	public List<Node> queryNodes(Node node) {
		
		return hessianFlowService.queryNodes(node);
	}
	
	/**
	 * 新增处理中办理过程
	 * @param eventId
	 * @param instanceId
	 * @param taskName
	 * @param advice
	 * @param userInfo
	 * @return taskId duedate
	 */
	@Override
	public Map<String, Object> saveHandlingTask(Long eventId, Long instanceId, String taskName, String advice, UserInfo userInfo, Map<String, Object> extraParam) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> resultMap = null;
		Long taskId = -1L;
		
		if(eventId == null && instanceId != null) {
			ProInstance pro = this.capProInstanceByInstanceId(instanceId);
			if(pro != null) {
				eventId = pro.getFormId();
			}
		} else if(eventId != null && instanceId == null) {
			instanceId = this.capInstanceIdByEventIdForLong(eventId);
		}
		
		params.put("taskId", this.curNodeTaskId(instanceId));//处理中任务的parentTaskId
		params.put("eventId", eventId);
		params.put("instanceId", instanceId);
		params.put("taskName", taskName);
		params.put("transactorId", userInfo.getUserId());
		params.put("taskactorName", userInfo.getPartyName());
		params.put("transactOrgId", userInfo.getOrgId());
		params.put("transactOrgName", userInfo.getOrgName());
		params.put("taskAdvice", advice);
		params.put("type", ConstantValue.HANDLING_TASK_TYPE);
		
		taskId = this.saveOrUpdateTask(params);
		
		if(taskId != null && taskId > 0) {
			LinkedHashMap<String, Date> duedate = new LinkedHashMap<String, Date>();
			duedate.put(taskName+"_"+taskName, null);
			
			resultMap = new HashMap<String, Object>();
			resultMap.put("taskId", taskId);
			resultMap.put("duedate", duedate);
		}
		
		return resultMap;
	}
	
	@Override
	public NodeTransition findNodeTransition(Integer curNodeId, Integer nextNodeId) {
		NodeTransition nodeTransition = null;
		
		if(curNodeId != null /*&& curNodeId > 0*/ && nextNodeId != null /*&& nextNodeId > 0*/) {
			nodeTransition = hessianFlowService.getNodeTransition(curNodeId, nextNodeId);
		}
		
		return nodeTransition;
	}
	
	@Override
	public int delRemind(Map<String, Object> params, UserInfo userInfo) throws Exception {
		StringBuffer msgWrong = new StringBuffer("");
		Long eventId = null, instanceId = null, taskId = null,
			 userId = null, userOrgId = null;
		String category = null, userPartyName = null, userOrgName = null;
		int successTotal = 0;
		boolean isDel4Own = false;
		
		if(CommonFunctions.isNotBlank(params, "category")) {
			category = params.get("category").toString();
		} else {
			msgWrong.append("缺少属性[category]！");
		}
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {}
		}
		if(CommonFunctions.isNotBlank(params, "eventId")) {
			try {
				eventId = Long.valueOf(params.get("eventId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
			if((instanceId == null || instanceId < 0) && eventId != null && eventId > 0) {
				instanceId = this.capInstanceIdByEventIdForLong(eventId);
			}
		}
		if(CommonFunctions.isNotBlank(params, "isDel4Own")) {
			isDel4Own = Boolean.valueOf(params.get("isDel4Own").toString());
		}
		if(userInfo != null) {
			userId = userInfo.getUserId();
			userOrgId = userInfo.getOrgId();
			userPartyName = userInfo.getPartyName();
			userOrgName = userInfo.getOrgName();
		}
		
		if(IEventDisposalWorkflowService.REMIND_CATEGORY.URGE.getCategory().equals(category)) {
			if(CommonFunctions.isNotBlank(params, "taskId")) {
				try {
					taskId = Long.valueOf(params.get("taskId").toString());
				} catch(NumberFormatException e) {}
				
				if((taskId == null || taskId < 0) && instanceId != null && instanceId > 0) {
					taskId = this.curNodeTaskId(instanceId);
				}
				
				if(taskId != null && taskId > 0) {
					params.put("taskId", taskId);
				} else {
					msgWrong.append("删除催办记录，缺少有效属性[taskId]!");
				}
			}
		} else if(IEventDisposalWorkflowService.REMIND_CATEGORY.SUPERVISE.getCategory().equals(category)) {
			if(instanceId == null || instanceId < 0) {
				msgWrong.append("删除督办记录，缺少有效属性[instanceId]!");
			}
		} else {
			msgWrong.append("请输入有效的[category]：【").append(params.get("category")).append("】！设置值参见：IEventDisposalWorkflowService.REMIND_CATEGORY！");
		}
		
		if(userId != null && userId > 0) {
			if(isDel4Own && CommonFunctions.isBlank(params, "remindUserId")) {
				params.put("remindUserId", userId);
			}
			if(CommonFunctions.isBlank(params, "updaterId")) {
				params.put("updaterId", userId);
			}
		}
		
		if(userOrgId != null && userOrgId > 0) {
			if(isDel4Own && CommonFunctions.isBlank(params, "remindOrgId")) {
				params.put("remindOrgId", userOrgId);
			}
			if(CommonFunctions.isBlank(params, "updaterOrgId")) {
				params.put("updaterOrgId", userOrgId);
			}
		}
		
		if(CommonFunctions.isBlank(params, "updaterName") && StringUtils.isNotBlank(userPartyName)) {
			params.put("updaterName", userPartyName);
		}
		
		if(CommonFunctions.isBlank(params, "updaterOrgName") && StringUtils.isNotBlank(userOrgName)) {
			params.put("updaterOrgName", userOrgName);
		}
		
		if(instanceId != null && instanceId > 0) {
			params.put("instanceId", instanceId);
		}
		
		successTotal = baseWorkflowService.delRemind(params);

		//取消督办记录成功后，取消关注记录
		if(successTotal > 0 && CommonFunctions.isNotBlank(params,"eventId")){
			try {
				hessianFlowService.cancleAttention(Long.valueOf(params.get("eventId").toString()), ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID, userId);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		return successTotal;
	}
	
	@Override
	public List<Map<String, Object>> findRemindList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> remindMapList = null;
		
		if(params != null && !params.isEmpty()) {
			StringBuffer msgWrong = new StringBuffer("");
			String category = null;
			Long eventId = null, instanceId = null, taskId = null;
			
			if(CommonFunctions.isNotBlank(params, "category")) {
				category = params.get("category").toString();
			} else {
				msgWrong.append("缺少属性[category]！");
			}
			
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				try {
					instanceId = Long.valueOf(params.get("instanceId").toString());
				} catch(NumberFormatException e) {}
			}
			if(CommonFunctions.isNotBlank(params, "eventId")) {
				try {
					eventId = Long.valueOf(params.get("eventId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if((instanceId == null || instanceId < 0) && eventId != null && eventId > 0) {
					instanceId = this.capInstanceIdByEventIdForLong(eventId);
				}
			}
			
			if(instanceId != null && instanceId > 0) {
				params.put("instanceId", instanceId);
			}
			
			if(IEventDisposalWorkflowService.REMIND_CATEGORY.URGE.getCategory().equals(category)) {
				//如果产生了需要获取实例下的所有催办记录，则将taskId必填约束条件去除
				if(CommonFunctions.isNotBlank(params, "taskId")) {
					try {
						taskId = Long.valueOf(params.get("taskId").toString());
					} catch(NumberFormatException e) {}
					
					if((taskId == null || taskId < 0) && instanceId != null && instanceId > 0) {
						taskId = this.curNodeTaskId(instanceId);
					}
					
					if(taskId != null && taskId > 0) {
						params.put("taskId", taskId);
					} else {
						msgWrong.append("获取催办记录，缺少有效属性[taskId]!");
					}
				}
			} else if(IEventDisposalWorkflowService.REMIND_CATEGORY.SUPERVISE.getCategory().equals(category)) {
				if(instanceId == null || instanceId < 0) {
					msgWrong.append("获取督办记录，缺少有效属性[instanceId]!");
				}
			} else {
				msgWrong.append("请输入有效的[category]：【").append(params.get("category")).append("】！设置值参见：IEventDisposalWorkflowService.REMIND_CATEGORY！");
			}
			
			if(msgWrong.length() > 0) {
				throw new Exception(msgWrong.toString());
			} else {
				remindMapList = baseWorkflowService.findRemindList(params);
			}
		}
		
		return remindMapList;
	}
	
	@Override
	public List<Map<String, Object>> findRemindInvalidList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> remindInvalidMapList = null;
		
		if(params != null && !params.isEmpty()) {
			StringBuffer msgWrong = new StringBuffer("");
			String category = null;
			Long eventId = null, instanceId = null, taskId = null;
			
			if(CommonFunctions.isNotBlank(params, "category")) {
				category = params.get("category").toString();
			} else {
				msgWrong.append("缺少属性[category]！");
			}
			
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				try {
					instanceId = Long.valueOf(params.get("instanceId").toString());
				} catch(NumberFormatException e) {}
			}
			if(CommonFunctions.isNotBlank(params, "eventId")) {
				try {
					eventId = Long.valueOf(params.get("eventId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if((instanceId == null || instanceId < 0) && eventId != null && eventId > 0) {
					instanceId = this.capInstanceIdByEventIdForLong(eventId);
				}
			}
			
			if(instanceId != null && instanceId > 0) {
				params.put("instanceId", instanceId);
			}
			
			if(IEventDisposalWorkflowService.REMIND_CATEGORY.URGE.getCategory().equals(category)) {
				if(CommonFunctions.isNotBlank(params, "taskId")) {
					try {
						taskId = Long.valueOf(params.get("taskId").toString());
					} catch(NumberFormatException e) {}
					
					if((taskId == null || taskId < 0) && instanceId != null && instanceId > 0) {
						taskId = this.curNodeTaskId(instanceId);
					}
					
					if(taskId != null && taskId > 0) {
						params.put("taskId", taskId);
					} else {
						msgWrong.append("删除催办记录，缺少有效属性[taskId]!");
					}
				}
			} else if(IEventDisposalWorkflowService.REMIND_CATEGORY.SUPERVISE.getCategory().equals(category)) {
				if(instanceId == null || instanceId < 0) {
					msgWrong.append("删除督办记录，缺少有效属性[instanceId]!");
				}
			} else {
				msgWrong.append("请输入有效的[category]：【").append(params.get("category")).append("】！设置值参见：IEventDisposalWorkflowService.REMIND_CATEGORY！");
			}
			
			if(msgWrong.length() > 0) {
				throw new Exception(msgWrong.toString());
			} else {
				remindInvalidMapList = baseWorkflowService.findRemindInvalidList(params);
			}
		}
		
		return remindInvalidMapList;
	}
	
	@Override
	public Map<String, Object> findRmqInitInfo(Map<String, Object> params,String orgCode) {
		//默认不对rmq的信息进行初始化
		return new HashMap<String,Object>();
	}
	

	private void saveOrUpdateEventEvaluate(Long eventId,ProInstance proInstance,UserInfo userInfo,Map<String, Object> extraParam) throws Exception{

		if(null == proInstance && null != eventId && eventId > 0){
			proInstance = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
		}
		//保存评价信息
		String PRO_STATUS_END = "2";//流程结束
		//默认不增加评价信息，当传入参数isEvaluate并且为true时，增加评价信息
		if(null != proInstance && PRO_STATUS_END.equals(proInstance.getStatus()) && CommonFunctions.isNotBlank(extraParam,"isEvaluate") && Boolean.valueOf(String.valueOf(extraParam.get("isEvaluate")))){
			String evaLevel = "";
			String evaContent = "";

			if(CommonFunctions.isNotBlank(extraParam,"evaLevel")){
				evaLevel = String.valueOf(extraParam.get("evaLevel"));
			}
			if(CommonFunctions.isNotBlank(extraParam,"evaContent")){
				evaContent = String.valueOf(extraParam.get("evaContent"));
			}
			eventDisposalExpandService.saveOrUpdateEventEvaluate(userInfo,eventId,evaLevel,evaContent,extraParam);
		}
	}

}
