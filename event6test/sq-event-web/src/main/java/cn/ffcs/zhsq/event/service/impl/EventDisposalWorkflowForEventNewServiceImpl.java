package cn.ffcs.zhsq.event.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.Forms;
import cn.ffcs.workflow.om.MyTask;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 事件通用 工作流使用接口实现 对应新的流程图
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflowForEventNewService")
public class EventDisposalWorkflowForEventNewServiceImpl extends
		EventDisposalWorkflowForNCHServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private HessianFlowService hessianFlowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService; 
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	//事件通用工作流决策类
	private final String EVENT_DECISION_SERVICE = "workflowEventDecisionMakingService";
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String wftypeId = ConstantValue.EVENT_WORKFLOW_WFTYPEID;
		UserBO user = new UserBO();
		OrgSocialInfoBO orgInfo = null;
		Map<String, Object> userMap = null;
		Integer formId = 0;
		Long orgId = -1L;
		Long userId = -1L;
		String chiefLevel = "",//组织层级
			   decisionMakingService = null;//决策类
		boolean isSendEvaUser = false, isGridAdmin = false;
		
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
		if(eventId != null && eventId > 0) {
			formId = Integer.valueOf(eventId.toString());
		}
		if(StringUtils.isBlank(toClose)){
			toClose = ConstantValue.WORKFLOW_IS_NO_CLOSE;
		}
		if(orgInfo != null) {
			chiefLevel = orgInfo.getChiefLevel();//组织层级
			userMap = new HashMap<String, Object>();
			userMap.put("orgCode", orgInfo.getOrgCode()); 
			userMap.put("orgType", orgInfo.getOrgType()); //组织类型
			
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
			
			StringBuffer parentOrgIdBuffer = new StringBuffer("");
			Map<String, String> gridOrgUser = new HashMap<String, String>();
			Long parentOrgId = -1L;
			//不限制为只在结案时传入，是为了采集并结案分步操作时，也能得到对应的评价人员
			isGridAdmin = fiveKeyElementService.isUserIdGridAdmin(orgId, userId);
			isSendEvaUser = (isGridAdmin ||				//当当前用户为网格员时，传递可评价人员信息
					 		 chiefLevel.equals(ConstantValue.UNITED_PREVENTION_GROUP_ORG_LEVEL));	//当上级为网格员办理时，需要过滤出上级为网格员的用户，目前只有联防组网格才有此操作
			
			for(OrgSocialInfoBO orgSocialInfoBO : parentOrgInfo) {
				if(orgSocialInfoBO != null) {
					parentOrgId = orgSocialInfoBO.getOrgId();
					parentOrgIdBuffer.append(",").append(parentOrgId);
					
					if(isSendEvaUser) {
						try {
							List<UserBO> userBoList = fiveKeyElementService.findCollectToCloseUserList(parentOrgId);
							
							if(chiefLevel.equals(ConstantValue.UNITED_PREVENTION_GROUP_ORG_LEVEL)) {
								userBoList = fiveKeyElementService.filterGridAdmin(parentOrgId, userBoList, false);
							}
							
							if(userBoList!=null && userBoList.size()>0){
								StringBuffer gridUserIds = new StringBuffer("");
								
								for(UserBO userBO : userBoList){
									gridUserIds.append(",").append(userBO.getUserId());
								}
								
								if(gridUserIds.length() > 0) {
									gridOrgUser.put(String.valueOf(parentOrgId), gridUserIds.substring(1));
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if(parentOrgIdBuffer.length() > 0) {
				parentOrgIdBuffer = new StringBuffer(parentOrgIdBuffer.substring(1));
			}
			
			if(isSendEvaUser) {
				if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose) && gridOrgUser.isEmpty()) {
					throw new Exception("缺少可评价人员！");
				} else {
					userMap.put("gridOrgUser", gridOrgUser);//可评价人员信息 Map<orgId, userIds>
				}
			} else {
				userMap.put("parentOrgId", parentOrgIdBuffer.toString());//上级组织id，以英文逗号相连接
			}
			
			userMap.put("orgChiefLevel", chiefLevel);
			userMap.put("isGovernment", eventOrgInfoService.isGovernmentForString(orgInfo));
			
			if(eventId != null && eventId > 0) {
				EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
				if(event != null) {
					decisionMakingService = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.DECISION_MAKING_SERVICE + "_event_" + event.getType(), IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, orgInfo.getOrgCode(), IFunConfigurationService.DIRECTION_UP_FUZZY);
				}
			}
		}
		
		if(StringUtils.isBlank(decisionMakingService)) {
			decisionMakingService = EVENT_DECISION_SERVICE;
		}
		
		userMap.put("toClose", toClose);
		userMap.put("isGridAdmin", fiveKeyElementService.isUserIdGridAdminForString(isGridAdmin));
		userMap.put("isNewOrganization", eventOrgInfoService.isNewOrganizationForString(orgInfo));
		userMap.put("decisionService", decisionMakingService);
		
		if(extraParam != null) {
			userMap.putAll(extraParam);
		}
		
		return startFlowByWorkFlow(workFlowId, formId, wftypeId, user, orgInfo, userMap);
	}
	
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
								
								resultMap.put("event4Simple", event);
							}
							
							resultMap.put("formTypeId", proInstance.getFormTypeId());
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
	public boolean recallWorkflow(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		boolean flag = false;
		Map<String, Object> preRecallNodeData = this.curNodeData(instanceId);
		
		flag = super.recallWorkflow(instanceId, userInfo, params);
		
		if(flag) {
			Long eventId = null;
			OrgSocialInfoBO orgInfo = null;
			Map<String, Object> statusMap = new HashMap<String, Object>(),
					                          afterRecallNodeData =  this.curNodeData(instanceId);
			String preRecallTaskName = "", afterRecallTaskName = "";
			Date afterRecallTaskDueDate = null;
			
			if(CommonFunctions.isNotBlank(params, "eventId")) {
				eventId = Long.valueOf(params.get("eventId").toString());
			}
			
			if(eventId == null || eventId < 0) {
				ProInstance pro = this.capProInstanceByInstanceId(instanceId);
				if(pro != null) {
					eventId = pro.getFormId();
				}
			}
			
			if(userInfo != null) {
				String userOrgCode = userInfo.getOrgCode();
				Long userOrgId = userInfo.getOrgId();
				
				if(StringUtils.isNotBlank(userOrgCode)) {
					orgInfo = new OrgSocialInfoBO();
					
					orgInfo.setOrgId(userOrgId);
					orgInfo.setOrgCode(userInfo.getOrgCode());
					orgInfo.setOrgName(userInfo.getOrgName());
				} else {
					orgInfo = orgSocialInfoService.findByOrgId(userOrgId);
				}
			}
			
			if(CommonFunctions.isNotBlank(preRecallNodeData, "NODE_NAME")) {
				preRecallTaskName = preRecallNodeData.get("NODE_NAME").toString();
			}
			if(CommonFunctions.isNotBlank(afterRecallNodeData, "NODE_NAME")) {
				afterRecallTaskName = preRecallNodeData.get("NODE_NAME").toString();
			}
			if(CommonFunctions.isNotBlank(afterRecallNodeData, "DUEDATE_")) {
				afterRecallTaskDueDate = (Date) afterRecallNodeData.get("DUEDATE_");
			}
			
			statusMap.putAll(params);
			statusMap.put("isRecall", true);
			
			LinkedHashMap<String, Date> dueDate = new LinkedHashMap<String, Date>();
			dueDate.put(preRecallTaskName + "_" + afterRecallTaskName, afterRecallTaskDueDate);
			
			statusMap.put("duedate", dueDate);
			
			this.updateEventStatus(eventId, userInfo.getUserId(), orgInfo, statusMap);
		}
		
		return flag;
	}
}
