package cn.ffcs.zhsq.event.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserInfoOutService;
import cn.ffcs.workflow.om.Forms;
import cn.ffcs.workflow.om.MyTask;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.om.Workflow;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 鼓楼网格工作流使用接口实现
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflow4GLGridService")
public class EventDisposalWorkflow4GLGridServiceImpl extends
	EventDisposalWorkflowForNCHServiceImpl {
	
	@Autowired
	private HessianFlowService hessianFlowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService; 
	
	@Autowired
	private UserInfoOutService userInfoService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//鼓楼网格工作流决策类
	private final String GLGRID_DECISION_SERVICE = "workflowGLGridDecisionMakingService";
	
	//组织类型
	private final String ORG_TYPE_UNIT = "1";	//组织类型——单位
	//网格采集员职位
	private final String GRID_COLLECTOR_POSITION = "网格采集员";
	//鼓楼网格流程图名称
	private final String EVENT_WORKFLOW_NAME_GL = "鼓楼事件流程图";
	
	@Override
	public String startFlowByWorkFlow(Long workFlowId, Integer eventId,
			String wftypeId, UserBO user, OrgSocialInfoBO org,
			Map<String, Object> userMap) throws Exception {
		String instanceId = null;
		Map<String, Object> resultMap = null;
		
		if(eventId == null || eventId < 0) {
			throw new IllegalArgumentException("缺少有效的事件id！");
		}
		
		instanceId = this.capInstanceIdByEventId(Long.valueOf(eventId));
		
		if(StringUtils.isBlank(instanceId)) {
			if(isUserAbleToStartWorkflow(org, user.getUserId())){
				resultMap = hessianFlowService.startNewEventWorkflow(workFlowId, Long.valueOf(eventId), user, org, userMap);
				if(resultMap != null) {
					if(CommonFunctions.isNotBlank(resultMap, "instanceId")) {
						instanceId = resultMap.get("instanceId").toString();
					}
					
					this.updateEventStatus(Long.valueOf(eventId), user.getUserId(), org, resultMap);
				}
			}
		}
		
		return instanceId;
	}
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String wftypeId = ConstantValue.EVENT_WORKFLOW_WFTYPEID,
			   orgType = "",
			   orgProfessionCode = "",
			   instanceId = "";
		UserBO user = new UserBO();
		OrgSocialInfoBO orgInfo = null;
		Map<String, Object> userMap = null;
		Integer formId = 0;
		Long orgId = -1L,
			 userId = -1L;
		
		if(userInfo != null){
			orgId = userInfo.getOrgId();
			userId = userInfo.getUserId();
			user.setUserId(userId);
			user.setPartyName(userInfo.getPartyName());
			orgInfo = orgSocialInfoService.findByOrgId(orgId);
			
			if(orgInfo != null) {
				orgType = orgInfo.getOrgType();
				orgProfessionCode = orgInfo.getProfessionCode();
			}
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
		
		if(ORG_TYPE_UNIT.equals(orgType) && (StringUtils.isBlank(orgProfessionCode) || ConstantValue.GOV_PROFESSION_CODE.equals(orgProfessionCode))) {
			userMap = new HashMap<String, Object>();
			userMap.put("orgCode", orgInfo.getOrgCode()); 
			userMap.put("orgType", orgType); //组织类型
			userMap.put("toClose", toClose);
			userMap.put("isNewOrganization", eventOrgInfoService.isNewOrganizationForString(orgInfo));
			userMap.put("orgChiefLevel", orgInfo.getChiefLevel());
			userMap.put("isGovernment", eventOrgInfoService.isGovernmentForString(orgInfo));
			userMap.put("decisionService", GLGRID_DECISION_SERVICE);
			
			if(extraParam != null) {
				userMap.putAll(extraParam);
			}
			
			instanceId = this.startFlowByWorkFlow(workFlowId, formId, wftypeId, user, orgInfo, userMap);
		}
		
		return instanceId;
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
				workflowName = EVENT_WORKFLOW_NAME_GL;
			}
			
			Workflow workflow = hessianFlowService.getWorkflowByWfTypeIdAndOrgIdCircle(workflowName, ConstantValue.EVENT_WORKFLOW_WFTYPEID, String.valueOf(orgId));
			if(workflow != null){
				workflowId = workflow.getWorkFlowId();
			}
		}
		
		return workflowId;
	}
	
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		Long workFlowId = this.capWorkflowId(instanceId, null, userInfo, null);
		List<Node> taskNodes = null;
		Map<String, Object> resultMap = new HashMap<String, Object>(),
							initResultMap = new HashMap<String, Object>();
		
		if(params == null) {
			params = new HashMap<String, Object>();
		}
		
		params.put("curOrgId", userInfo.getOrgId());//当前用户的组织
		params.put("curUserId", userInfo.getUserId());//当前用户的编码
		
		initResultMap = hessianFlowService.initNewFlowInfoForEvent(taskId, instanceId, workFlowId, params);

		taskId = (String) initResultMap.get("taskId");
		List<OperateBean> operateLists = (List<OperateBean>) initResultMap.get("operateLists");
		//工作流发布实例
		Integer deploymentId = (Integer) initResultMap.get("deploymentId");
		Node curNode = (Node) initResultMap.get("curNode");
		String workflowCtx = hessianFlowService.getWorkflowCtx();
		
		//获取流程实例
		ProInstance proInstance = (ProInstance) initResultMap.get("proInstance");
		Forms curForm = (Forms) initResultMap.get("curForm");
		// 是否跨域
		String formUrl = (String) initResultMap.get("formUrl");
		//任务描述
		String taskDesc = (String) initResultMap.get("taskDesc");
		// 上一个节点
		MyTask backTask = (MyTask) initResultMap.get("backTask");
		
		if(CommonFunctions.isNotBlank(initResultMap, "taskNodes")) {
			taskNodes = (List<Node>) initResultMap.get("taskNodes");
			
			if(taskNodes != null) {
				ProInstance pro = this.capProInstanceByInstanceId(instanceId);
				if(pro != null) {
					Long eventId = pro.getFormId();
					EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
					if(event != null) {
						String status = event.getStatus();
						String subTask = event.getSubStatus();
						
						//增添“处理中”办理环节
						if((StringUtils.isBlank(subTask) || ConstantValue.HANDLING_STATUS.equals(subTask) || ConstantValue.ACCEPT_STATUS.equals(subTask)) && (ConstantValue.EVENT_STATUS_REPORT.equals(status) || ConstantValue.EVENT_STATUS_DISTRIBUTE.equals(status))) {
							boolean isShowHnadlingTask = true;
							String isShowHandlingTaskStr = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.IS_SHOW_HANDLING_TASK, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
							
							if(StringUtils.isNotBlank(isShowHandlingTaskStr)) {
								isShowHnadlingTask = Boolean.valueOf(isShowHandlingTaskStr);
							}
							
							if(isShowHnadlingTask) {
								Node node = new Node();
								node.setNodeId(-1);
								node.setNodeName(ConstantValue.HANDLING_TASK_CODE);
								node.setNodeNameZH(ConstantValue.HANDLING_TASK_NAME);
								node.setDynamicSelect("1");
								node.setNodeType("1");
								node.setTransitionCode("__C0__");//设置为采集人办理
								
								taskNodes.add(node);
								
								resultMap.put("taskNodes", taskNodes);
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
	public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {
		int resultCode = 0;
		String wrongMsg = "当前组织不存在，请先检验！";
		
		if(orgSocialInfo != null){
			String chiefLevel = orgSocialInfo.getChiefLevel();
			String orgType = orgSocialInfo.getOrgType();
			boolean isGridCollector = true;
			
			//判断用户在该组织下是否具有网格采集员职位		
			isGridCollector = userInfoService.cheakUserForParas(userId, orgSocialInfo.getOrgId(), GRID_COLLECTOR_POSITION) > 0;
			
			if(!ORG_TYPE_UNIT.equals(orgType)) {
				resultCode = -1;
				wrongMsg = "当前组织的组织类型不是单位！";
			} else if(!ConstantValue.GRID_ORG_LEVEL.equals(chiefLevel) && !ConstantValue.COMMUNITY_ORG_LEVEL.equals(chiefLevel) && !ConstantValue.STREET_ORG_LEVEL.equals(chiefLevel)) {
				resultCode = -1;
				wrongMsg = "当前组织的组织层级不是网格、社区/村或者街道/乡镇！";
			} else if(!isGridCollector) {
				resultCode = -1;
				wrongMsg = "当前用户不具有[" + GRID_COLLECTOR_POSITION + "]职位！";
			} else {
				resultCode = 1;
				wrongMsg = "";
			}
		}
		
		if(StringUtils.isNotBlank(wrongMsg)) {
			throw new Exception(wrongMsg);
		}
		
		return resultCode;
	}
}
