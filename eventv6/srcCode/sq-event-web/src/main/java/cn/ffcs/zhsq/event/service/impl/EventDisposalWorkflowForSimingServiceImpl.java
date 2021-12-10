package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.Forms;
import cn.ffcs.workflow.om.MyTask;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.om.Workflow;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 思明区工作流使用接口实现
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflowForSimingService")
public class EventDisposalWorkflowForSimingServiceImpl extends
		EventDisposalWorkflowForEventServiceImpl {

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private HessianFlowService hessianFlowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Resource(name = "workflowDecisionMakingService")
	private IWorkflowDecisionMakingService<String> workflowDecisionMakingService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService; 
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	private final String GRID_INFO_ORG_CHIEF_LEVEL = "6";//组织 网格层级
	private final String COMMUNITY_ORG_CHIEF_LEVEL = "5";//组织 社区层级
	private final String STREET_ORG_CHIEF_LEVEL = "4";//组织 街道层级
	private final String DISTRICT_ORG_CHIEF_LEVEL = "3";//组织 区层级
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final String COMMUNITY_DECISION_SERVICE = "communityDecisionMakingService";	//社区流程图决策类
	private final String STREET_DECISION_SERVICE = "streetDecisionMakingService";		//街道流程图决策类
	private final String DISTRICT_DECISION_SERVICE = "districtDecisionMakingService";	//区流程图决策类
	
	private final String EVENT_STATUS_DECISION_SERVICE = "eventStatusSimingDecisionMakingService";//事件状态决策类
	
	@Override
	public String startFlowByWorkFlow(Long workFlowId, Integer eventId,
			String wftypeId, UserBO user, OrgSocialInfoBO org,
			Map<String, Object> userMap) throws Exception {
		String instanceId = null, toClose = "";
		Map<String, Object> resultMap = null;
		
		if(eventId == null || eventId < 0) {
			throw new IllegalArgumentException("缺少有效的事件id！");
		}
		
		instanceId = this.capInstanceIdByEventId(Long.valueOf(eventId));
		
		if(StringUtils.isBlank(instanceId)) {
			if(isUserAbleToStartWorkflow(org, user.getUserId())){
				if(CommonFunctions.isNotBlank(userMap, "toClose")) {
					toClose = userMap.get("toClose").toString();
				}
				
				if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)) {
					UserInfo userInfo = new UserInfo();
					userInfo.setPartyName(user.getPartyName());
					userInfo.setUserId(user.getUserId());
					userInfo.setOrgCode(org.getOrgCode());
					userInfo.setOrgId(org.getOrgId());
					//采集并结案 已有更新事件状态
					resultMap = this.startEndWorkflowForMap(Long.valueOf(eventId), workFlowId, userInfo, userMap);
				} else {
					resultMap = hessianFlowService.startEventWorkflow(workFlowId, Long.valueOf(eventId), user, org, userMap);
					if(resultMap != null) {
						this.updateEventStatus(Long.valueOf(eventId), user.getUserId(), org, resultMap);
					}
				}
			}
			
			if(CommonFunctions.isNotBlank(resultMap, "instanceId")) {
				instanceId = resultMap.get("instanceId").toString();
			}
		}
		
		return instanceId;
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
		String chiefLevel = "";//组织层级
		String decisionService = "";//流程图决策类
		boolean isSendEvaUser = false;
		
		if(userInfo != null){
			orgId = userInfo.getOrgId();
			userId = userInfo.getUserId();
			user.setUserId(userId);
			user.setPartyName(userInfo.getPartyName());
			orgInfo = orgSocialInfoService.findByOrgId(orgId);
		}
		
		if(workFlowId == null || workFlowId < 0) {
			workFlowId = this.capWorkflowId(null, eventId, userInfo, null);
		}
		if(eventId != null){
			formId = Integer.valueOf(eventId.toString());
		}
		if(StringUtils.isBlank(toClose)){
			toClose = ConstantValue.WORKFLOW_IS_NO_CLOSE;
		}
		if(orgInfo != null){
			chiefLevel = orgInfo.getChiefLevel();//组织层级
			userMap = new HashMap<String, Object>();
			userMap.put("orgCode", orgInfo.getOrgCode()); 
			userMap.put("orgType", orgInfo.getOrgType()); //组织类型
			
			//查找父级
			List<OrgSocialInfoBO> parentOrgInfo = fiveKeyElementService.findOrgByLevel(orgId, IFiveKeyElementService.PARENT_LEVEL_ONE, ConstantValue.GOV_PROFESSION_CODE, new UserInfo());
			StringBuffer parentOrgId = new StringBuffer("");
			Map<String, String> gridOrgUser = new HashMap<String, String>();
			
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
			
			userMap.put("orgChiefLevel", chiefLevel);
			userMap.put("isGovernment", eventOrgInfoService.isGovernmentForString(orgInfo));
		}
		
		userMap.put("toClose", toClose);
		userMap.put("isGridAdmin", fiveKeyElementService.isUserIdGridAdminForString(isSendEvaUser));
		userMap.put("isNewOrganization", eventOrgInfoService.isNewOrganizationForString(orgInfo));
		
		userMap.put("automatic", false);//用于判断是否需要自动跳转到"事件采集环节"，为空时，自动跳转；否则，不自动跳转。
		
		if(GRID_INFO_ORG_CHIEF_LEVEL.equals(chiefLevel) || COMMUNITY_ORG_CHIEF_LEVEL.equals(chiefLevel)) {
			decisionService = COMMUNITY_DECISION_SERVICE;
		} else if(STREET_ORG_CHIEF_LEVEL.equals(chiefLevel)) {
			decisionService = STREET_DECISION_SERVICE;
		} else if(DISTRICT_ORG_CHIEF_LEVEL.equals(chiefLevel)) {
			decisionService = DISTRICT_DECISION_SERVICE;
		}
		
		userMap.put("decisionService", decisionService);
		
		if(extraParam != null) {
			userMap.putAll(extraParam);
		}
		
		return this.startFlowByWorkFlow(workFlowId, formId, wftypeId, user, orgInfo, userMap);
	}
	
	@Override
	public String startEndWorkflowForEvent(Long eventId, Long workFlowId, UserInfo userInfo) throws Exception{
		String instanceId = "";//流程实例id
		
		Map<String, Object> resultMap = startEndWorkflowForMap(eventId, workFlowId, userInfo, null);
		if(CommonFunctions.isNotBlank(resultMap, "instanceId")) {
			instanceId = resultMap.get("instanceId").toString();
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
			String instanceIdStr = this.capInstanceIdByEventId(eventId);
			if(StringUtils.isNotBlank(instanceIdStr)) {
				workFlowId = this.capWorkflowId(Long.valueOf(instanceIdStr), eventId, userInfo, null);
			}
		}
		
		
		return this.startEndWorkflowForEvent(eventId, workFlowId, userInfo);
	}
	
	/**
	 * 采集并办理事件
	 * @param eventId
	 * @param workFlowId
	 * @param userInfo
	 * @return
	 *  instanceId 流程实例ID
	 *  duedate 下一环节办理期限
	 * @throws Exception 
	 */
	private Map<String, Object> startEndWorkflowForMap(Long eventId, Long workFlowId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		OrgSocialInfoBO orgInfo = null;
		UserBO user = new UserBO();
		String instanceId = "";
		Long orgId = -1L;
		Long userId = -1L;
		Map<String, Object> resultMap = null;
		
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
			
			//查找父级
			List<OrgSocialInfoBO> orgInfoList = fiveKeyElementService.findOrgByLevel(orgId, IFiveKeyElementService.PARENT_LEVEL_ONE, ConstantValue.GOV_PROFESSION_CODE, new UserInfo());
			StringBuffer parentOrgId = new StringBuffer("");
			
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
		
		if(extraParam != null) {
			params.putAll(extraParam);
		}
		
		if(isUserAbleToStartWorkflow(orgInfo, userId)){
			/**
			 * 采集并结束流程(针对事件)
			 * @param workFlowId	流程id
			 * @param formId		表单id
			 * @param user			当前用户
			 * @param org			当前用户所在组织
			 * @param params		其他个性化参数(参数可以参考新事件)
			 * @return
			 * instanceId 流程实例ID
			 * duedate  Map<String, Date>下一环节办理期限
			 */
			resultMap = hessianFlowService.startAndEndWorkflowForEvent(workFlowId, eventId, user, orgInfo, params);
			
			if(CommonFunctions.isNotBlank(resultMap, "instanceId")) {
				instanceId = resultMap.get("instanceId").toString();
				
				if(StringUtils.isNotBlank(instanceId)) {
					this.updateEventStatus(eventId, userId, orgInfo, resultMap);
				}
			}
		}
		
		return resultMap;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		Long workFlowId = this.capWorkflowId(instanceId, null, userInfo, null);
		
		if(params == null) {
			params = new HashMap<String, Object>();
		}
		
		params.put("curOrgId", userInfo.getOrgId());//当前用户的组织
		params.put("curUserId", userInfo.getUserId());//当前用户的编码
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> initResultMap = hessianFlowService.initFlowInfoForEvent(taskId, instanceId, workFlowId, params);

		taskId = (String) initResultMap.get("taskId");
		List<OperateBean> operateLists = (List<OperateBean>) initResultMap.get("operateLists");
		List<Node> taskNodes = (List<Node>) initResultMap.get("taskNodes");
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
		
		resultMap.put("taskId", taskId);
		resultMap.put("operateLists", operateLists);
		resultMap.put("instanceId", instanceId);
		resultMap.put("taskNodes", taskNodes);
		resultMap.put("deploymentId", deploymentId);
		resultMap.put("curNodeId", curNode.getNodeId());
		resultMap.put("curNodeName", curNode.getNodeName());
		resultMap.put("curNodeFormTypeId", curNode.getFormTypeId());
		resultMap.put("curNode", curNode);
		resultMap.put("workflowCtx", workflowCtx);//工作流域名访问前缀
		resultMap.put("workFlowId", workFlowId);
		resultMap.put("backTask", backTask);
		
		resultMap.put("proInstance", proInstance);//获取事件发起人
		resultMap.put("curForm", curForm);
		resultMap.put("formUrl", formUrl+"&isCrossdomain=0");
		resultMap.put("taskDesc", taskDesc);
		
		return resultMap;
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{
		boolean result = false;
		
		if(this.isUserInfoCurrentUser(taskId, instanceId, userInfo)) {
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
			UserBO user = new UserBO();
			
			user.setUserId(userInfo.getUserId());
			user.setPartyName(userInfo.getPartyName());
			//获取事件当前环节的节点编码
			
			/**
			 *  提交流程
			 * @param taskId		任务ID
			 * @param nextNodeName	下一个节点编码
			 * @param userIds		动态分配人员ID，可为“id1,id2,id3...”
			 * @param curOrgIds		动态分配人员用户ID，可为“id1,id2,id3...”
			 * @param remarks		办理意见
			 * @param user			当前用户
			 * @param org			当前用户组织机构
			 * @param sendSms		是否发送短信
			 */
			Map<String, Object> resultMap = hessianFlowService.subWorkFlowForEvent(taskId, nextNodeName, nextStaffId, curOrgIds, advice, user, orgInfo, smsContent);
			result = resultMap != null;
			
			if(result){
				ProInstance proInstance = hessianFlowService.getProByInstanceId(instanceId);
				if(proInstance != null){
					Long eventId = proInstance.getFormId();
					//保存办理意见
					if(eventId != null && eventId > 0) {
						this.updateEventStatus(eventId, userInfo.getUserId(), orgInfo, resultMap);
						
						if(StringUtils.isNotBlank(advice)) {
							EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
							if(event != null) {
								if(ConstantValue.EVENT_STATUS_ARCHIVE.equals(event.getStatus())) {
									EventDisposal eventTmp = new EventDisposal();
									eventTmp.setEventId(eventId);
									eventTmp.setResult(advice);
									
									eventDisposalService.updateEventDisposalById(eventTmp);
								}
							}
						}
					}
				}
			}
		}
		
		return result;
	}
	
	@Override
	public Long capWorkflowId(Long instanceId, Long eventId, UserInfo userInfo, Map<String, Object> extraParam){
		Long workflowId = -1L;
		Long orgId = userInfo.getOrgId();//用户所在组织编号
		
		if(instanceId != null && instanceId > 0) {
			ProInstance pro = this.capProInstanceByInstanceId(instanceId);
			if(pro != null) {
				workflowId = pro.getWorkFlowId();
			}
		} else if(orgId!=null && orgId>0){
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
			
			if(orgInfo != null) {
				Map<String, String> params = new HashMap<String, String>();
				String chiefLevel = orgInfo.getChiefLevel();
				if(GRID_INFO_ORG_CHIEF_LEVEL.equals(chiefLevel)) {
					chiefLevel = COMMUNITY_ORG_CHIEF_LEVEL;
				}
				params.put("level", chiefLevel);
				Workflow workflow = hessianFlowService.getWorkflowForEvent(params);
				if(workflow != null){
					workflowId = workflow.getWorkFlowId();
				}
			}
		}
		
		if(workflowId == null || workflowId < 0) {
			logger.error("获取workflowId失败！");
		}
		
		return workflowId;
	}
	
	@Override
	public boolean rejectWorkFlow(String taskId, String instanceId,
			String advice, UserInfo userInfo) throws Exception {
		Long instanceIdL = -1L;
		String fromNodeCode = "";
		String toNodeCode = "";
		
		try {
			instanceIdL = Long.valueOf(instanceId);
		} catch(NumberFormatException e) {}
		
		fromNodeCode = capCurNodeCode(instanceIdL);
		
		boolean result = super.rejectWorkFlow(taskId, instanceId, advice, userInfo);
		
		if(result && instanceIdL > 0) {
			toNodeCode = capCurNodeCode(instanceIdL);
			ProInstance pro = this.capProInstanceByInstanceId(instanceIdL);
			
			if(pro != null) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				LinkedHashMap<String, Date> duedate = new LinkedHashMap<String, Date>();
				duedate.put(fromNodeCode+"_"+toNodeCode, null);
				resultMap.put("duedate", duedate);
				resultMap.put("isReject", true);
				
				this.updateEventStatus(pro.getFormId(), userInfo.getUserId(), userInfo.getOrgId(), resultMap);
			}
		}
		
		return result;
	}
	
	/**
	 * 获取当前环节的节点编码
	 * @param instanceId 流程实例id
	 * @return
	 */
	private String capCurNodeCode(Long instanceId) {
		String nodeCode = "";
		
		if(instanceId != null && instanceId > 0) {
			Map<String, Object> taskMap = this.curNodeData(instanceId);
			if(CommonFunctions.isNotBlank(taskMap, "NODE_NAME")) {
				nodeCode = taskMap.get("NODE_NAME").toString();
			}
		}
		
		return nodeCode;
	}
	
	/**
	 * 更新事件状态
	 * @param eventId
	 * @param userId
	 * @param orgId
	 * @param chiefLevel
	 * @param resultMap
	 */
	@SuppressWarnings("unchecked")
	protected void updateEventStatus(Long eventId, Long userId, OrgSocialInfoBO orgInfo, Map<String, Object> resultMap) {
		LinkedHashMap<String, Date> handleDateMap = null;
		Map<String, Object> params = new HashMap<String, Object>();
		String workflowName = null, bizPlatform = null,
			   EVENT_STATUS_TEMPLATE = ConstantValue.EVENT_STATUS + "@workflowName@@bizPlatform@@eventType@",
			   eventStatusTrig = null, eventType = null;
		StringBuffer trigCondition = new StringBuffer("");
		
		if(resultMap != null && !resultMap.isEmpty()) {
			params.putAll(resultMap);
		}
		
		params.put("eventId", eventId);
		params.put("userId", userId);
		params.put("orgId", orgInfo.getOrgId());
		params.put("chiefLevel", orgInfo.getChiefLevel());
		
		if(eventId != null && eventId > 0) {
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			
			if(CommonFunctions.isNotBlank(resultMap, "workflowName")) {
				workflowName = resultMap.get("workflowName").toString();
			} else {
				ProInstance proInstance = null;
				
				if(CommonFunctions.isNotBlank(resultMap, "instanceId")) {
					Long instanceId = null;
					
					try {
						instanceId = Long.valueOf(resultMap.get("instanceId").toString());
					} catch(NumberFormatException e) {}
					
					if(instanceId != null && instanceId > 0) {
						proInstance = capProInstanceByInstanceId(instanceId);
					}
				} else {
					proInstance = capProInstanceByEventId(eventId);
				}
				
				if(proInstance != null) {
					workflowName = proInstance.getProName();
				}
			}
			
			if(event != null) {
				String eventBizPlatform = event.getBizPlatform();
				
				eventType = event.getType();
				
				if(StringUtils.isNotBlank(eventType) && StringUtils.isNotBlank(eventBizPlatform) && !ConstantValue.DEFAULT_EVENT_BIZ_PLATFORM.equals(eventBizPlatform)) {
					bizPlatform = eventBizPlatform;
				}
			}
		}
		
		trigCondition.append("[");
		
		eventType = StringUtils.isBlank(eventType) ? "" : "_" + eventType;
		bizPlatform = StringUtils.isBlank(bizPlatform) ? "" : "_" + bizPlatform;
		eventStatusTrig = EVENT_STATUS_TEMPLATE.replace("@eventType@", eventType);
		
		trigCondition.append(eventStatusTrig.replace("@workflowName@", "").replace("@bizPlatform@", ""));
		
		eventStatusTrig = eventStatusTrig.replace("@bizPlatform@", bizPlatform);
		if(StringUtils.isNotBlank(bizPlatform)) {
			trigCondition.append("|").append(eventStatusTrig.replace("@workflowName@", ""));
		}
		if(StringUtils.isNotBlank(workflowName)) {
			trigCondition.append("|").append(eventStatusTrig.replace("@workflowName@", "-" + workflowName));
		}
		
		trigCondition.append("]");
		
		String eventStatusDecision = funConfigurationService.turnCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, trigCondition.toString(), IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, orgInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_REGEXP);
		if(StringUtils.isBlank(eventStatusDecision)) {
			eventStatusDecision = EVENT_STATUS_DECISION_SERVICE;
		}
		
		params.put("decisionService", eventStatusDecision);
		
		if(CommonFunctions.isNotBlank(resultMap, "isReject")) {//用于判断是否是驳回操作
			params.put("isReject", resultMap.get("isReject"));
		}
		
		if(CommonFunctions.isNotBlank(resultMap, "duedate")) {
			try {
				handleDateMap = (LinkedHashMap<String, Date>)resultMap.get("duedate");
				if(handleDateMap != null) {
					Set<String> handleDateSet = handleDateMap.keySet();
					String[] nodeCode = null;
					
					for(String handleDateKey : handleDateSet) {
						if(StringUtils.isNotBlank(handleDateKey)) {
							nodeCode = handleDateKey.split("_");
							params.put("curNodeCode", nodeCode[0]);
							params.put("nextNodeCode", nodeCode[1]);
							params.put("handleDate", handleDateMap.get(handleDateKey));
						}
						
						try {
							workflowDecisionMakingService.makeDecision(params);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch(ClassCastException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 更新事件状态
	 * @param eventId
	 * @param userId
	 * @param orgId
	 * @param resultMap
	 */
	protected void updateEventStatus(Long eventId, Long userId, Long orgId, Map<String, Object> resultMap) {
		OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
		
		if(orgInfo != null) {
			this.updateEventStatus(eventId, userId, orgInfo, resultMap);
		}
	}
}
