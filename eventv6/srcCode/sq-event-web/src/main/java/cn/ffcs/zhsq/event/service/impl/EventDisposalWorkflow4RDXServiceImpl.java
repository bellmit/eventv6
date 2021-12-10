package cn.ffcs.zhsq.event.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 雄安新区容东县(RongDongXian) 工作流使用接口实现
 * @ClassName:   EventDisposalWorkflow4RDXServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年5月15日 上午10:57:25
 */
@Service(value = "eventDisposalWorkflow4RDXService")
public class EventDisposalWorkflow4RDXServiceImpl extends EventDisposalWorkflowForEventNewServiceImpl {
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService; 
	
	@Autowired
	private IEventExtendService eventExtendService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	//事件通用工作流决策类
	private final String EVENT_DECISION_SERVICE = "workflowEventDecisionMakingService";
	
	private static final String COMMUNITY_NODE_CODE = "task3";	//村(社区)处理环节
	private static final String STREET_NODE_CODE = "task4";		//乡镇(街道)处理环节
	private static final String CLOSE_NODE_CODE = "task8";		//结案节点
	private final String END_NODE_CODE = "end1";				//归档节点
	private static final String DEPARTMENT_HANDLE_NODE_CODE = "task2";//子流程职能部门处理
	private static final String HANDLING_PRO_STATUS = "1", END_PRO_STATUS = "2";
	
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
		boolean isGridAdmin = false;
		
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
			Long parentOrgId = -1L;
			
			for(OrgSocialInfoBO orgSocialInfoBO : parentOrgInfo) {
				if(orgSocialInfoBO != null) {
					parentOrgId = orgSocialInfoBO.getOrgId();
					parentOrgIdBuffer.append(",").append(parentOrgId);
				}
			}
			if(parentOrgIdBuffer.length() > 0) {
				userMap.put("parentOrgId", parentOrgIdBuffer.substring(1));//上级组织id，以英文逗号相连接
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
	public String startFlowByWorkFlow(Long workFlowId, Integer eventId,
			String wftypeId, UserBO user, OrgSocialInfoBO org,
			Map<String, Object> userMap) throws Exception {
		String instanceId = null;
		
		if(eventId == null || eventId < 0) {
			throw new IllegalArgumentException("缺少有效的事件id！");
		}
		
		if(CommonFunctions.isBlank(userMap, "parentInstanceId")) {
			instanceId = super.startFlowByWorkFlow(workFlowId, eventId, wftypeId, user, org, userMap);
		} else if(CommonFunctions.isNotBlank(userMap, "instanceTaskId")) {
			UserInfo userInfo = new UserInfo();
			Long instanceIdL = null;
			
			if(userMap == null) {
				userMap = new HashMap<String, Object>();
			}
			
			userInfo.setUserId(user.getUserId());
			userInfo.setPartyName(user.getPartyName());
			userInfo.setOrgCode(org.getOrgCode());
			userInfo.setOrgId(org.getOrgId());
			userInfo.setOrgName(org.getOrgName());
			
			userMap.put("parentId", userMap.get("parentInstanceId"));
			userMap.put("taskId", userMap.get("instanceTaskId"));
			userMap.put("workflowId", workFlowId);
			userMap.remove("parentInstanceId");
			userMap.remove("instanceTaskId");
			
			instanceIdL = baseWorkflowService.startWorkflow4Base(null, null, Long.valueOf(eventId), userInfo, userMap);
			
			if(instanceIdL != null) {
				instanceId = instanceIdL.toString();
			}
		}
		
		return instanceId;
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice, UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{
		boolean flag = false;
		String curNodeName = null, formTypeIdStr = null;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(CommonFunctions.isNotBlank(extraParam, "formTypeId")) {
			formTypeIdStr = extraParam.get("formTypeId").toString();
		} else if(instanceId != null && instanceId > 0) {
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			if(proInstance != null) {
				formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
			}
		}
		
		if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
			curNodeName = extraParam.get("curNodeName").toString();
		}
		
		if(StringUtils.isBlank(advice) && CommonFunctions.isNotBlank(extraParam, "advice")) {
			advice = extraParam.get("advice").toString();
		}
		
		if(ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID.equals(formTypeIdStr) 
				&& (COMMUNITY_NODE_CODE.equals(curNodeName) || STREET_NODE_CODE.equals(curNodeName))) {
			Long eventId = null, curTaskId = null;
			
			try {
				if(CommonFunctions.isNotBlank(extraParam, "eventId")) {
					eventId = Long.valueOf(extraParam.get("eventId").toString());
				} else if(CommonFunctions.isNotBlank(extraParam, "formId")) {
					eventId = Long.valueOf(extraParam.get("formId").toString());
				}
			} catch(NumberFormatException e) {}
			
			if(ConstantValue.COORDINATE_TASK_CODE.equals(nextNodeName)) {
				Map<String, String> nextUserMap = new HashMap<String, String>();
				Map<String, Object> instanceMap = new HashMap<String, Object>(),
									curTaskData = this.curNodeData(instanceId),
									startParam = new HashMap<String, Object>();
				
				if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
					curTaskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
				}
				
				instanceMap.put("parentInstanceId", instanceId);
				instanceMap.put("instanceTaskId", curTaskId);
				instanceMap.put("formTypeId", ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID);
				instanceMap.put("instanceStatus", HANDLING_PRO_STATUS);
				
				if(StringUtils.isNotBlank(nextStaffId) && StringUtils.isNotBlank(curOrgIds)) {//将办理人员以组织进行分组，每个组织启动一个子流程
					String[] nextUserIdArray = nextStaffId.split(","),
								nextOrgIdArray = curOrgIds.split(",");
					String nextOrgId = null, nextUserId = null, userPartyName = null, orgName = null;
					StringBuffer msgWrong = new StringBuffer("");
					UserBO userBO = null;
					OrgSocialInfoBO orgInfo = null;
					
					instanceMap.put("isPaticipant", true);
					instanceMap.put("handlingUserType", IEventDisposalWorkflowService.ACTOR_TYPE_USER);
					instanceMap.put("handledUserType", IEventDisposalWorkflowService.ACTOR_TYPE_USER);
					
					for(int index = 0, len = nextOrgIdArray.length; index < len; index++) {
						nextOrgId = nextOrgIdArray[index];
						nextUserId = nextUserIdArray[index];
						
						instanceMap.put("handlingUserId", nextUserId);
						instanceMap.put("handlingOrgId", nextOrgId);
						instanceMap.put("handledUserId", nextUserId);
						instanceMap.put("handledOrgId", nextOrgId);
						
						if(baseWorkflowService.findProInstanceCount(instanceMap) == 0) {//只有分配的人员未有参与未完结的子流程，才可以重新启动子流程
							if(nextUserMap.containsKey(nextOrgId)) {
								nextUserMap.put(nextOrgId, nextUserMap.get(nextOrgId) + "," + nextUserId);
							} else {
								nextUserMap.put(nextOrgId, nextUserId);
							}
						} else {//如果nextUserId、nextOrgId有传入非数字型内容，不捕获异常，直接中断抛出
							userBO = userManageService.getUserInfoByUserId(Long.valueOf(nextUserId));
							orgInfo = orgSocialInfoService.findByOrgId(Long.valueOf(nextOrgId));
							userPartyName = "";
							orgName = "";
							
							if(userBO != null && userBO.getUserId() != null) {
								userPartyName = userBO.getPartyName();
							}
							if(orgInfo != null && orgInfo.getOrgId() != null) {
								orgName = orgInfo.getOrgName();
							}
							
							msgWrong.append("用户【").append(userPartyName).append("-").append(orgName).append("】参与的联合办理任务尚未完结！");
						}
					}
					
					if(msgWrong.length() > 0) {
						throw new ZhsqEventException(msgWrong.toString());
					}
				}
				
				if(!nextUserMap.isEmpty()) {
					Map<String, Object> workflowMap = new HashMap<String, Object>();
					Long workflowId = null,
						 orgId = userInfo.getOrgId();
					
					if(orgId != null && orgId > 0) {
						OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
						if(orgInfo != null) {
							workflowMap.put("workflowName", "容东县事件子流程");
						}
					}
					
					if(!workflowMap.isEmpty()) {
						workflowId = this.capWorkflowId(null, null, userInfo, workflowMap);
						
						if(workflowId != null && workflowId > 0) {
							String subInstanceIdStr = null;
							Map<String, Object> subParam = new HashMap<String, Object>(); 
							
							startParam.put("parentInstanceId", instanceId);
							startParam.put("instanceTaskId", taskId);
							startParam.put("isConvertProinstance", false);//强制启动流程，不校验
							
							subParam.put("eventId", eventId);
							subParam.put("formId", eventId);
							
							for(String userOrgId : nextUserMap.keySet()) {
								subInstanceIdStr = startFlowByWorkFlow(eventId, workflowId, null, userInfo, startParam);
								
								if(StringUtils.isNotBlank(subInstanceIdStr)) {
									Long subInstanceId = Long.valueOf(subInstanceIdStr),
										 subCurTaskId = this.curNodeTaskId(subInstanceId);
									
									if(subCurTaskId != null) {//自动扭转至下一节点，从而使得选择的人员成为事件的待办人员
										subWorkFlowForEndingAndEvaluate(subInstanceId, subCurTaskId.toString(), DEPARTMENT_HANDLE_NODE_CODE, nextUserMap.get(userOrgId), userOrgId, advice, userInfo, null, subParam);
									}
								}
							}
							
							flag = true;//不判断子流程启动、办理情况，直接返回true
						} else {
							throw new IllegalArgumentException("没有找到流程图【" + workflowMap.get("workflowName") + "】！");
						}
					}
					
				}
			} else if(CLOSE_NODE_CODE.equals(nextNodeName)) {
				Map<String, Object> proMap = new HashMap<String, Object>();
				List<Map<String, Object>> subProMapList = null;
				
				proMap.put("parentInstanceId", instanceId);
				proMap.put("formTypeId", ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID);
				proMap.put("instanceStatus", HANDLING_PRO_STATUS);// 1 办理中；
				proMap.put("instanceTaskId", taskId);
				
				subProMapList = baseWorkflowService.findProInstanceList(proMap);
				
				if(subProMapList != null) {
					Long subInstanceId = null;
					
					for(Map<String, Object> subProMap : subProMapList) {
						subInstanceId = null;
						
						if(CommonFunctions.isNotBlank(subProMap, "INSTANCE_ID")) {
							subInstanceId = Long.valueOf(subProMap.get("INSTANCE_ID").toString());
						}
						
						if(subInstanceId != null) {
							//将子流程均强制归档
							baseWorkflowService.endWorkflow4Base(subInstanceId, userInfo, null);
						}
					}
				}
				
				flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
			} else {
				Map<String, Object> proMap = new HashMap<String, Object>();
				
				proMap.put("parentInstanceId", instanceId);
				proMap.put("formTypeId", ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID);
				proMap.put("instanceStatus", HANDLING_PRO_STATUS);// 1 办理中；
				proMap.put("instanceTaskId", taskId);
				
				if(baseWorkflowService.findProInstanceCount(proMap) > 0) {
					throw new ZhsqEventException("当前环节尚有未完结的联合办理任务！");
				} else {
					flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
				}
			}
		} else {
			flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		}
		
		if(flag && END_NODE_CODE.equals(nextNodeName)) {
			saveOrUpdateEventExtend(nextNodeName, userInfo, extraParam);
		}
		
		return flag;
	}
	
	@Override
	public boolean rejectWorkFlow(String taskId, String instanceId,
			String advice, UserInfo userInfo) throws Exception {
		boolean flag = false, isHandlingSubPro = false;
		Long instanceIdL = Long.valueOf(instanceId);
		Map<String, Object> curTaskData = null;//当前任务信息
		String curNodeName = null;
		Integer formTypeId = null;
		ProInstance pro = this.capProInstanceByInstanceId(instanceIdL);
		
		if(pro != null) {
			formTypeId = pro.getFormTypeId();
		}
		
		//只有当当前环节为社区级网格处理或者社区指挥中心受理时，才需要判断当前任务的子流程情况
		if(ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID.equals(String.valueOf(formTypeId))) {
			curTaskData = this.curNodeData(instanceIdL);//当前任务信息
			
			if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
				curNodeName = curTaskData.get("NODE_NAME").toString();
			}
			
			if(COMMUNITY_NODE_CODE.equals(curNodeName) || STREET_NODE_CODE.equals(curNodeName)) {
				Map<String, Object> instanceMap = new HashMap<String, Object>();
				
				instanceMap.put("parentInstanceId", instanceId);
				if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
					instanceMap.put("instanceTaskId", curTaskData.get("WF_DBID_"));
				}
				instanceMap.put("formTypeId", ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID);
				instanceMap.put("instanceStatus", HANDLING_PRO_STATUS);
				
				isHandlingSubPro = baseWorkflowService.findProInstanceCount(instanceMap) > 0;
			}
		}
		
		if(isHandlingSubPro) {
			String curTaskNameZH = "";
			
			if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
				curTaskNameZH = " 【" + curTaskData.get("WF_ACTIVITY_NAME_").toString() + "】 ";
			}
			
			throw new ZhsqEventException("当前环节" + curTaskNameZH + "尚有未完结的联合办理任务！");
		} else {
			flag = super.rejectWorkFlow(taskId, instanceId, advice, userInfo);
		}
		
		if(flag) {
			//子流程驳回至第一个节点时，作废该流程
			if(ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID.equals(formTypeId)) {
				curTaskData = this.curNodeData(instanceIdL);//子流程当前任务
				if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
					String SUB_COLLECT_NODE_NAME = "task1",							//子流程采集节点名称
						   curTaskName = curTaskData.get("NODE_NAME").toString();	//当前任务名称
					if(SUB_COLLECT_NODE_NAME.equals(curTaskName)) {
						baseWorkflowService.invalidInstance(instanceIdL, null);
					}
				}
			}
		}
		
		return flag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		Map<String, Object> resultMap = super.initFlowInfo(taskId, instanceId, userInfo, params);
		String curNodeName = null;
		
		if(CommonFunctions.isNotBlank(resultMap, "curNodeName")) {
			curNodeName = resultMap.get("curNodeName").toString();
		}
		
		if(COMMUNITY_NODE_CODE.equals(curNodeName) || STREET_NODE_CODE.equals(curNodeName)) {
			if(CommonFunctions.isNotBlank(resultMap, "taskNodes")) {
				List<Node> taskNodes = (List<Node>) resultMap.get("taskNodes");
				String transitionCode = "U5F0D5";
				Map<String, Object> instanceMap = new HashMap<String, Object>();
				Node lastNode = taskNodes.get(taskNodes.size() - 1), node = new Node();
				
				instanceMap.put("parentInstanceId", instanceId);
				instanceMap.put("instanceTaskId", taskId);
				instanceMap.put("formTypeId", ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID);
				instanceMap.put("instanceStatus", HANDLING_PRO_STATUS);
				
				if(STREET_NODE_CODE.equals(curNodeName)) {
					transitionCode = "U4F0D4";
				}
				
				//启动了子流程，则将下一办理环节清空
				if(baseWorkflowService.findProInstanceCount(instanceMap) > 0) {
					taskNodes.clear();
					
					if(CommonFunctions.isNotBlank(resultMap, "operateLists")) {
						List<OperateBean> operateList = (List<OperateBean>) resultMap.get("operateLists");
						String operateEvent = null,
							   REJECT_OPERATE_EVENT = "reject";
						OperateBean removeOperate = null;
						
						for(OperateBean operate : operateList) {
							operateEvent = operate.getOperateEvent();
							
							if(REJECT_OPERATE_EVENT.equals(operateEvent)) {
								removeOperate = operate;
								break;
							}
						}
						
						if(removeOperate != null) {
							operateList.remove(removeOperate);
						}
						
						resultMap.put("operateLists", operateList);
					}
				} else if(ConstantValue.HANDLING_TASK_CODE.equals(lastNode.getNodeName())) {
					taskNodes.remove(lastNode);
				}
				
				node.setNodeId(-1);
				node.setNodeName(ConstantValue.COORDINATE_TASK_CODE);
				node.setNodeNameZH("联合办理");//环节中文名称
				node.setDynamicSelect("1");//动态跳转
				node.setNodeType("1");
				node.setTransitionCode(transitionCode);
				
				taskNodes.add(node);
				
				resultMap.put("taskNodes", taskNodes);
			}
		}
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int sqlOrder, UserInfo userInfo) {
		List<Map<String, Object>> taskMapList = super.queryProInstanceDetail(instanceId, sqlOrder, userInfo),
								  resultMapList = new ArrayList<Map<String, Object>>();
		
		taskMapList = super.queryProInstanceDetail(instanceId, sqlOrder, userInfo);
		
		if(taskMapList != null) {
			Map<String, Object> proMap = new HashMap<String, Object>();
			List<Map<String, Object>> proMapList = null, subProTaskMapList = null;
			String taskName = null;
			
			proMap.put("parentInstanceId", instanceId);
			proMap.put("formTypeId", ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID);
			proMap.put("instanceStatus", HANDLING_PRO_STATUS + "," + END_PRO_STATUS);// 1 办理中；2 归档；4 废除
			
			for(Map<String, Object> taskMap : taskMapList) {
				taskName = null;
				
				if(CommonFunctions.isNotBlank(taskMap, "TASK_CODE")) {
					taskName = taskMap.get("TASK_CODE").toString();
				}
				
				if((COMMUNITY_NODE_CODE.equals(taskName) || STREET_NODE_CODE.equals(taskName)) && 
						CommonFunctions.isNotBlank(taskMap, "TASK_ID")) {
					proMap.put("instanceTaskId", taskMap.get("TASK_ID"));
					proMapList = baseWorkflowService.findProInstanceList(proMap);
					
					if(proMapList != null) {
						taskMap.put("SUB_PRO_COUNT", proMapList.size());
						
						for(Map<String, Object> proMapTmp : proMapList) {
							if(CommonFunctions.isNotBlank(proMapTmp, "INSTANCE_ID")) {
								subProTaskMapList = super.queryProInstanceDetail(Long.valueOf(proMapTmp.get("INSTANCE_ID").toString()), sqlOrder, userInfo);
								
								if(subProTaskMapList != null) {
									for(Map<String, Object> subProTaskMap : subProTaskMapList) {
										if(CommonFunctions.isBlank(subProTaskMap, "PARENT_PRO_TASK_ID")) {
											subProTaskMap.put("PARENT_PRO_TASK_ID", taskMap.get("TASK_ID"));
										}
									}
									
									resultMapList.addAll(subProTaskMapList);
									
									taskMap.put("subTaskList", subProTaskMapList);
								}
							}
						}
					}
				}
				
				resultMapList.add(taskMap);
			}
		}
		
		return resultMapList;
	}
	
	@Override
	public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {
		int resultCode = -1;
		String wrongMsg = "当前组织的组织类型不是单位！";
		
		if(orgSocialInfo != null && orgSocialInfo.getOrgId() != null) {
			String chiefLevel = orgSocialInfo.getChiefLevel(),
				   orgType = orgSocialInfo.getOrgType(),
				   ORG_TYPE_UNIT = String.valueOf(ConstantValue.ORG_TYPE_UNIT);
			
			if(ORG_TYPE_UNIT.equals(orgType)) {
				if(StringUtils.isNotBlank(chiefLevel)) {
					if(ConstantValue.STREET_ORG_LEVEL.compareTo(chiefLevel) > 0) {
						wrongMsg = "当前组织的组织层级超过了【街道/乡镇】层级！";
					} else {
						resultCode = 1;
						wrongMsg = "";
					}
				} else {
					wrongMsg = "当前组织的组织缺少地域层级设置！";
				}
			} else {
				wrongMsg = "当前组织的组织类型不是单位！";
			}
		}
		
		if(StringUtils.isNotBlank(wrongMsg)) {
			throw new Exception(wrongMsg);
		}
		
		return resultCode;
	}
	
	/**
	 * 新增/更新事件扩展信息
	 * @param nextNodeName	下一环节名称
	 * @param userInfo		用户信息
	 * @param extraParam	额外信息
	 * 			eventId			事件id
	 * 			instanceId		流程实例id
	 * 			isBonusPoint	是否给予积分，true为给予；默认为false
	 * 			bonusPoint		给予的积分，当isBonusPoint为true时使用
	 */
	private void saveOrUpdateEventExtend(String nextNodeName, UserInfo userInfo, Map<String, Object> extraParam) {
		boolean isBonusPoint = false,
				isAlterExtend = false;

		if(CommonFunctions.isNotBlank(extraParam, "isBonusPoint")) {
			isBonusPoint = Boolean.valueOf(extraParam.get("isBonusPoint").toString());
		}

		isAlterExtend = END_NODE_CODE.equals(nextNodeName) && CommonFunctions.isNotBlank(extraParam, "isBonusPoint");

		if(isAlterExtend) {
			Long eventId = null;

			if(CommonFunctions.isNotBlank(extraParam, "eventId")) {
				try {
					eventId = Long.valueOf(extraParam.get("eventId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			} else if(CommonFunctions.isNotBlank(extraParam, "formId")) {
				try {
					eventId = Long.valueOf(extraParam.get("formId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			} else if(CommonFunctions.isNotBlank(extraParam, "instanceId")) {
				Long instanceId = null;
				
				try {
					instanceId = Long.valueOf(extraParam.get("instanceId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if(instanceId != null && instanceId > 0) {
					ProInstance pro = this.capProInstanceByInstanceId(instanceId);

					if(pro != null) {
						eventId = pro.getFormId();
					}
				}
			}

			if(eventId != null && eventId > 0) {
				Map<String, Object> eventExtend = new HashMap<String, Object>();
				Long userId = userInfo.getUserId();

				if(END_NODE_CODE.equals(nextNodeName)) {
					Integer bonusPoint = 0;
					String IS_TO_GIVE = "1",	//给予积分
						   NOT_TO_GIVE = "2";	//不予积分

					if(isBonusPoint) {
						if(CommonFunctions.isNotBlank(extraParam, "bonusPoint")) {
							try {
								bonusPoint = Integer.valueOf(extraParam.get("bonusPoint").toString());
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
						}
						eventExtend.put("isBonusPoint", IS_TO_GIVE);
					} else {//不予积分
						eventExtend.put("isBonusPoint", NOT_TO_GIVE);
					}

					eventExtend.put("bonusPoint", bonusPoint);
				}

				eventExtend.put("eventId", eventId);

				if(CommonFunctions.isBlank(eventExtend, "creatorId")) {
					eventExtend.put("creatorId", userId);
				}
				if(CommonFunctions.isBlank(eventExtend, "updaterId")) {
					eventExtend.put("updaterId", userId);
				}

				try {//不要因为事件扩展表增添/更新失败，影响提交的后续操作
					eventExtendService.saveEventExtend(eventExtend);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
