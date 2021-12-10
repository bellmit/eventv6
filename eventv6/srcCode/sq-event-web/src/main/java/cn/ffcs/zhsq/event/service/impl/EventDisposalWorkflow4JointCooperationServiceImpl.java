package cn.ffcs.zhsq.event.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.gmis.mybatis.domain.workcircle.WorkCircle;
import cn.ffcs.gmis.workcircle.service.WorkCircleService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 延平大联动工作流使用接口实现 对应流程图为 大联动事件流程
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflow4JointCooperationService")
public class EventDisposalWorkflow4JointCooperationServiceImpl extends
		EventDisposalWorkflow4GLGridServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService; 
	
	@Autowired
	private WorkCircleService workCircleService;//工作圈接口
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	//延平区大联动工作流决策类
	private final String JOINT_COOPERATION_DECISION_SERVICE = "workflowDecisionMaking4JointCooperationService";
	
	private static final String STREET_CENTER_NODE_CODE_COLLECT = "task3";	//街道指挥中心处理
	private static final String STREET_CENTER_NODE_CODE = "task7";			//街道指挥中心处理
	private static final String DISTRICT_CENTER_NODE_CODE = "task8";		//县区指挥中心处理
	private static final String COMMUNITY_NODE_CODE_SEND = "task9";			//社区处理处理环节
	private static final String STREET_DEPARTMENT_NODE_CODE_SEND = "task10";//街道处置单位处理
	private static final String DISTRICT_DEPARTMENT_NODE_CODE_SEND = "task11";//区处置单位处理
	
	private static final String WORK_CIRCLE_WCTYPE_EVENT = "1";//工作圈类型 事件
	private static final String WORK_CIRCLE_OPTYPE_ACCEPT = "2";//工作圈操作类型 受理
	private static final String WORK_CIRCLE_OPTYPE_CLOSE = "3";//工作圈类型 结案
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String wftypeId = ConstantValue.EVENT_WORKFLOW_WFTYPEID,
			   instanceId = "",
			   orgType = "",
			   orgLevel = "",
			   orgCode = "";
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
				orgLevel = orgInfo.getChiefLevel();
				orgCode = orgInfo.getOrgCode();
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
		
		userMap = new HashMap<String, Object>();
		userMap.put("orgCode", orgCode); 
		userMap.put("orgType", orgType); //组织类型
		userMap.put("toClose", toClose);
		userMap.put("isNewOrganization", eventOrgInfoService.isNewOrganizationForString(orgInfo));
		userMap.put("orgChiefLevel", orgLevel);
		userMap.put("isGovernment", eventOrgInfoService.isGovernmentForString(orgInfo));
		userMap.put("decisionService", JOINT_COOPERATION_DECISION_SERVICE);
		
		if(extraParam != null) {
			userMap.putAll(extraParam);
		}
		
		//isContinuousDecision为true时，则自动扭转第二个决策节点；false则不自动扭转第二个决策节点
		if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgType) && (ConstantValue.DISTRICT_ORG_LEVEL.equals(orgLevel) || ConstantValue.STREET_ORG_LEVEL.equals(orgLevel))) {
			boolean isContinuousDecision = ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose);
			
			if(!isContinuousDecision && ConstantValue.STREET_ORG_LEVEL.equals(orgLevel)) {
				EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
				
				if(event != null) {
					String bizPlatform = event.getBizPlatform(),
						   decisionResultTaskName = null,
						   workflowName = this.capWorkflowName(eventId, event.getType(), userInfo);
					
					decisionResultTaskName = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.DECISION_RESULT_TASK_NAME+"_"+workflowName+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
					
					isContinuousDecision = StringUtils.isNotBlank(decisionResultTaskName);
				}
			}
			
			userMap.put("isContinuousDecision", isContinuousDecision);
		}
		
		instanceId = this.startFlowByWorkFlow(workFlowId, formId, wftypeId, user, orgInfo, userMap);
		
		if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose) && StringUtils.isNotBlank(instanceId)) {//采集并归档
			try {
				Long instanceIdL = Long.valueOf(instanceId);
				
				if(instanceIdL != null && instanceIdL > 0) {
					baseWorkflowService.endWorkflow4Base(instanceIdL, userInfo, null);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return instanceId;
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice, UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{
		boolean flag = false;
		Map<String, Object> curNodeMap = this.curNodeData(instanceId);
		
		flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		
		if(flag) {
			String curNodeName = "";
			
			if(CommonFunctions.isNotBlank(curNodeMap, "NODE_NAME")) {
				curNodeName = curNodeMap.get("NODE_NAME").toString();
			}
			
			if(ConstantValue.ACCEPT_TASK_CODE.equals(nextNodeName) || COMMUNITY_NODE_CODE_SEND.equals(curNodeName) || STREET_DEPARTMENT_NODE_CODE_SEND.equals(curNodeName) || DISTRICT_DEPARTMENT_NODE_CODE_SEND.equals(curNodeName)) {
				Long userId = -1L;
				String userName = "", orgCode = "", orgName = "";
				WorkCircle workCircle = new WorkCircle();
				
				if(userInfo != null) {
					userId = userInfo.getUserId();
					userName = userInfo.getPartyName();
					orgCode = userInfo.getOrgCode();
					orgName = userInfo.getOrgName();
				}
				
				workCircle.setWcType(WORK_CIRCLE_WCTYPE_EVENT);
				workCircle.setOpUserId(userId);
				workCircle.setOpUserName(userName);
				workCircle.setOpDeptCode(orgCode);
				workCircle.setOpDeptName(orgName);
				
				ProInstance pro = this.capProInstanceByInstanceId(instanceId);
				if(pro != null) {
					Long eventId = pro.getFormId();
					
					if(eventId != null && eventId > 0) {
						EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
						if(event != null) {
							workCircle.setEventId(eventId);
							workCircle.setEventTitle(event.getEventName());
							workCircle.setEventTime(event.getHappenTime());
							workCircle.setEventAddr(event.getOccurred());
						}
					}
				}
				
				if(ConstantValue.ACCEPT_TASK_CODE.equals(nextNodeName)) {
					//添加 受理 工作圈记录
					workCircle.setOpType(WORK_CIRCLE_OPTYPE_ACCEPT);
					if(userId != null) {
						workCircle.setNextUserId(userId.toString());
					}
					workCircle.setNextUserName(userName);
					workCircle.setNextDeptCode(orgCode);
					workCircle.setNextDeptName(orgName);
					
				} else if(COMMUNITY_NODE_CODE_SEND.equals(curNodeName) || STREET_DEPARTMENT_NODE_CODE_SEND.equals(curNodeName) || DISTRICT_DEPARTMENT_NODE_CODE_SEND.equals(curNodeName)) {
					//添加 结案 工作圈记录
					workCircle.setOpType(WORK_CIRCLE_OPTYPE_CLOSE);
					
					workCircle.setNextUserId(nextStaffId);
					
					if(StringUtils.isNotBlank(curOrgIds)) {
						String[] curOrgIdArray = curOrgIds.split(",");
						Long orgId = null;
						OrgSocialInfoBO orgInfo = null;
						StringBuffer orgCodeBuffer = new StringBuffer(""),
									 orgNameBuffer = new StringBuffer("");
						
						for(String curOrgId : curOrgIdArray) {
							if(StringUtils.isNotBlank(curOrgId)) {
								try {
									orgId = Long.valueOf(curOrgId);
								} catch(NumberFormatException e) {
									orgId = -1L;
									e.printStackTrace();
								}
								
								if(orgId != null && orgId > 0) {
									orgInfo = orgSocialInfoService.findByOrgId(orgId);
									if(orgInfo != null) {
										orgCodeBuffer.append(",").append(orgInfo.getOrgCode());
										orgNameBuffer.append(",").append(orgInfo.getOrgName());
									}
								}
							}
						}
						
						if(orgCodeBuffer.length() > 0) {
							workCircle.setNextDeptCode(orgCodeBuffer.substring(1));
							workCircle.setNextDeptName(orgNameBuffer.substring(1));
						}
					}
				}
				
				try {
					workCircleService.insert(workCircle);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return flag;
	}
	
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		Map<String, Object> resultMap = super.initFlowInfo(taskId, instanceId, userInfo, params);
		String curNodeName = "";
		
		if(CommonFunctions.isNotBlank(resultMap, "curNodeName")) {
			curNodeName = resultMap.get("curNodeName").toString();
		}
		
		if(STREET_CENTER_NODE_CODE_COLLECT.equals(curNodeName) || STREET_CENTER_NODE_CODE.equals(curNodeName) || DISTRICT_CENTER_NODE_CODE.equals(curNodeName)) {
			List<Map<String, Object>> taskMapList = queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
			boolean isNotAccepted = true;
			
			if(taskMapList != null) {//判断当前事件是否受理过
				Long mainTaskId = null;
				String mainTaskCode = "";
				
				mainTaskLabel:
				for(Map<String, Object> taskMap : taskMapList) {
					if(CommonFunctions.isNotBlank(taskMap, "TASK_CODE")) {
						mainTaskCode = taskMap.get("TASK_CODE").toString();
					} else {
						mainTaskCode = curNodeName;
					}
					
					if((STREET_CENTER_NODE_CODE_COLLECT.equals(mainTaskCode) || STREET_CENTER_NODE_CODE.equals(mainTaskCode) || DISTRICT_CENTER_NODE_CODE.equals(mainTaskCode)) && CommonFunctions.isNotBlank(taskMap, "TASK_ID")) {
						try {
							mainTaskId = Long.valueOf(taskMap.get("TASK_ID").toString());
						} catch(NumberFormatException e) {
							mainTaskId = -1L;
							e.printStackTrace();
						}
					}
					
					if(mainTaskId != null && mainTaskId > 0) {
						List<Map<String, Object>> subTaskMapList = this.querySubTaskDetails(mainTaskId.toString(), IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
						
						if(subTaskMapList != null) {
							for(Map<String, Object> subTaskMap : subTaskMapList) {
								if(CommonFunctions.isNotBlank(subTaskMap, "TASK_NAME") && ConstantValue.ACCEPT_TASK_CODE.equals(subTaskMap.get("TASK_NAME").toString())) {
									isNotAccepted = false; 
									break mainTaskLabel;
								}
							}
						}
					}
				}
			}
			
			if(isNotAccepted) {
				List<Node> taskNodes = new ArrayList<Node>();
				Node node = new Node();
				node.setNodeId(-1);
				node.setNodeName(ConstantValue.ACCEPT_TASK_CODE);
				node.setNodeNameZH(ConstantValue.ACCEPT_TASK_NAME);
				node.setDynamicSelect("1");
				node.setNodeType("1");
				node.setTransitionCode("__C0__");//设置为采集人办理
				
				taskNodes.add(node);
				
				List<OperateBean> operateLists = new ArrayList<OperateBean>();
				OperateBean operateBean = new OperateBean();
				operateBean.setOperateEvent("subTask");
				operateBean.setAnotherName("提交");
				operateLists.add(operateBean);
				
				resultMap.put("taskNodes", taskNodes);
				resultMap.put("operateLists", operateLists);
			}
		}
		
		return resultMap;
	}
	
	@Override
	public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {
		int resultCode = 0;
		String wrongMsg = "当前组织不存在，请先检验！";
		
		if(orgSocialInfo != null) {
			String chiefLevel = orgSocialInfo.getChiefLevel();
			
			if(!ConstantValue.COMMUNITY_ORG_LEVEL.equals(chiefLevel) && !ConstantValue.STREET_ORG_LEVEL.equals(chiefLevel) && !ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevel)) {
				resultCode = -1;
				wrongMsg = "当前组织的组织层级不是社区/村、街道/乡镇或者县/区！";
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
