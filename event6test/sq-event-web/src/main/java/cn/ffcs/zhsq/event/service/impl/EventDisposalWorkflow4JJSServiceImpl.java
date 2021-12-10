package cn.ffcs.zhsq.event.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.api.enums.BizPlatfromEnum;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 晋江市(JJS) 工作流处理
 * 对应流程图为：新版通用事件_晋江市
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflow4JJSService")
public class EventDisposalWorkflow4JJSServiceImpl extends
	EventDisposalWorkflowForEventNewServiceImpl {
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private HessianFlowService hessianFlowService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	private String CLOSE_NODE_CODE = "JA",				//结案环节编码
						  COMMUNITY_NODE_CODE = "task3",	//村(社区)处理环节
						  STREET_NODE_NAME = "task4", 			//乡镇(街道)处理
						  DISTRICT_NODE_NAME = "task5",		//市级处理
						  CLOSE_NODE_NAME = "task8",			//结案环节名称，新版
						  CLOSE_NODE_NAME_OLD = "结案",		//结案环节名称，旧版
						  HANDLING_PRO_STATUS = "1",			//流程实例状态——处理中
						  END_PRO_STATUS = "2",						//流程实例状态——结束
						  ABOLISH_PRO_STATUS = "4";				//流程实例状态——废除
	
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
			Map<String, Object> resultMap = null;
			
			if(userMap == null) {
				userMap = new HashMap<String, Object>();
			}
			
			userMap.put("parentId", userMap.get("parentInstanceId"));
			userMap.put("taskId", userMap.get("instanceTaskId"));
			userMap.remove("parentInstanceId");
			userMap.remove("instanceTaskId");
			
			resultMap = hessianFlowService.startEventWorkflow(workFlowId, Long.valueOf(eventId), user, org, userMap);
			
			if(CommonFunctions.isNotBlank(resultMap, "instanceId")) {
				instanceId = resultMap.get("instanceId").toString();
			}
		}
		
		return instanceId;
	}
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String instanceId = null;
		
		if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)) {
			throw new IllegalArgumentException("不可进行自采自结操作！");
		} else {
			instanceId = super.startFlowByWorkFlow(eventId, workFlowId, toClose, userInfo, extraParam);
		}
		
		return instanceId;
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		//由于传入的taskId有可能不是当前任务id，故而需要重新获取当前任务信息
		Map<String, Object> curTaskData = this.curNodeData(instanceId);//主流程的当前任务信息
		Long curTaskId = -1L;
		Map<String, Object> instanceMap = new HashMap<String, Object>();
		
		if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
			curTaskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
		}
		
		instanceMap.put("parentInstanceId", instanceId);
		instanceMap.put("instanceTaskId", curTaskId);
		instanceMap.put("formTypeId", ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID);
		instanceMap.put("instanceStatus", HANDLING_PRO_STATUS);
		
		if(ConstantValue.COORDINATE_TASK_CODE.equals(nextNodeName)) {
			Map<String, String> nextUserMap = new HashMap<String, String>();
			
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
						
						msgWrong.append("用户【").append(userPartyName).append("-").append(orgName).append("】参与的协同任务尚未完结！");
					}
				}
				
				if(msgWrong.length() > 0) {
					throw new ZhsqEventException(msgWrong.toString());
				}
			}
			
			if(!nextUserMap.isEmpty()) {
				Map<String, Object> workflowMap = new HashMap<String, Object>();
				Long workflowId = null,
						 eventId = null,
						 orgId = userInfo.getOrgId();
				
				if(CommonFunctions.isNotBlank(extraParam, "formId")) {
					try {
						eventId = Long.valueOf(extraParam.get("formId").toString());
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
				} else if(instanceId != null && instanceId > 0) {
					ProInstance pro = this.capProInstanceByInstanceId(instanceId);
					if(pro != null) {
						eventId = pro.getFormId();
					}
				}
				
				if(orgId != null && orgId > 0) {
					OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
					if(orgInfo != null) {
						workflowMap.put("workflowName", "晋江事件子流程_" + orgInfo.getChiefLevel());
					}
				}
				
				if(!workflowMap.isEmpty()) {
					workflowId = this.capWorkflowId(null, null, userInfo, workflowMap);
					
					if(workflowId != null && workflowId > 0) {
						String subInstanceIdStr = null;
						
						if(extraParam == null) {
							extraParam = new HashMap<String, Object>();
						}
						
						extraParam.put("parentInstanceId", instanceId);
						extraParam.put("instanceTaskId", taskId);
						
						for(String userOrgId : nextUserMap.keySet()) {
							subInstanceIdStr = this.startFlowByWorkFlow(eventId, workflowId, null, userInfo, extraParam);
							
							if(StringUtils.isNotBlank(subInstanceIdStr)) {
								Long subInstanceId = Long.valueOf(subInstanceIdStr),
										 subCurTaskId = this.curNodeTaskId(subInstanceId);
								
								if(subCurTaskId != null) {//自动扭转至下一节点，从而使得选择的人员成为事件的待办人员
									//职能部门协同办理环节名称，包含 市职能部门协同办理、街道职能部门协同办理
									String DEPARTMENT_COORDINATE_NODE_NAME = "task2";
									
									this.subWorkFlowForEndingAndEvaluate(subInstanceId, subCurTaskId.toString(), DEPARTMENT_COORDINATE_NODE_NAME, nextUserMap.get(userOrgId), userOrgId, advice, userInfo, null, extraParam);
								}
							}
						}
						
						flag = true;//不判断子流程启动、办理情况，直接返回true
					} else {
						throw new IllegalArgumentException("没有找到流程图【" + workflowMap.get("workflowName") + "】！");
					}
				}
				
			}
		} else {
			String curNodeName = null;
			boolean isHandlingSubPro = false;
			
			if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
				curNodeName = curTaskData.get("NODE_NAME").toString();
			}
			
			//只有当当前环节为街道处理或者市级处理时，才需要判断当前任务的子流程情况
			if(STREET_NODE_NAME.equals(curNodeName) || DISTRICT_NODE_NAME.equals(curNodeName)) {
				isHandlingSubPro = baseWorkflowService.findProInstanceCount(instanceMap) > 0;
			}
			
			if(isHandlingSubPro) {
				String curTaskNameZH = "";
				
				if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
					curTaskNameZH = " 【" + curTaskData.get("WF_ACTIVITY_NAME_").toString() + "】 ";
				}
				
				throw new ZhsqEventException("当前环节" + curTaskNameZH + "尚有未完结的协同任务！");
			} else {
				//采集人员不可进行结案操作
				if(CLOSE_NODE_NAME.equals(nextNodeName) || CLOSE_NODE_NAME_OLD.equals(nextNodeName)) {
					ProInstance proInstance = this.capProInstanceByInstanceId(instanceId);
					if(proInstance != null) {
						Long collectUserId = proInstance.getUserId(), collectOrgId = proInstance.getOrgId();
						
						if(collectUserId != null && collectUserId > 0 
								&& collectOrgId != null && collectOrgId > 0
								&& collectOrgId.equals(userInfo.getUserId()) 
								&& collectOrgId.equals(userInfo.getOrgId())) {
							throw new ZhsqEventException("事件采集人员不可进行结案操作！");
						}
					}
				}
				
				flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
			}
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
		
		//只有当当前环节为街道处理或者市级处理时，才需要判断当前任务的子流程情况
		if(ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID.equals(String.valueOf(formTypeId))) {
			curTaskData = this.curNodeData(instanceIdL);//当前任务信息
			
			if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
				curNodeName = curTaskData.get("NODE_NAME").toString();
			}
			
			if(STREET_NODE_NAME.equals(curNodeName) || DISTRICT_NODE_NAME.equals(curNodeName)) {
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
			
			throw new ZhsqEventException("当前环节" + curTaskNameZH + "尚有未完结的协同任务！");
		} else {
			flag = super.rejectWorkFlow(taskId, instanceId, advice, userInfo);
		}
		
		if(flag) {
			//子流程驳回至第一个节点时，作废该流程
			if(ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID.equals(formTypeId)) {
				curTaskData = this.curNodeData(instanceIdL);//子流程当前任务
				if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
					String SUB_COLLECT_NODE_NAME = "task1",									//子流程采集节点名称
							  curTaskName = curTaskData.get("NODE_NAME").toString();	//当前任务名称
					if(SUB_COLLECT_NODE_NAME.equals(curTaskName)) {
						baseWorkflowService.invalidInstance(instanceIdL, null);
					}
				}
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean recallWorkflow(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		boolean flag = false, isSubInstanceInvalid = false;
		ProInstance pro = null;
		
		if(instanceId != null && instanceId > 0) {
			pro = this.capProInstanceByInstanceId(instanceId);
		}
		
		if(pro != null) {
			Integer formTypeId = pro.getFormTypeId();
			
			//主流程
			if(ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID.equals(String.valueOf(formTypeId))) {
				Map<String, Object> curTaskData = this.curNodeData(instanceId);//当前任务信息
				String curNodeName = null;
				Long curTaskId = -1L;
				
				if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
					curNodeName = curTaskData.get("NODE_NAME").toString();
				}
				if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
					curTaskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
				}
				
				//当当前环节为街道处理或者市级处理时，如有存在子流程，则不可进行撤回操作
				if(STREET_NODE_NAME.equals(curNodeName) || DISTRICT_NODE_NAME.equals(curNodeName)) {
					Long subProInstanceId = null;
					Long userOrgId = userInfo.getOrgId();
					String orgType = null;
					
					if(userOrgId != null && userOrgId > 0) {
						OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userOrgId);
						
						if(orgInfo != null) {
							orgType = orgInfo.getOrgType();
						}
					}
					
					//若是职能部门，优先获取子流程信息
					if(String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType)) {
						Map<String, Object> instanceParam = new HashMap<String, Object>();
						Long userId = userInfo.getUserId();
						List<Map<String, Object>> proInstanceMapList = null;
						
						//获取当前办理环节的子流程，否则可能会找到多条
						instanceParam.put("parentInstanceId", instanceId);
						instanceParam.put("instanceTaskId", curTaskId);
						instanceParam.put("formTypeId", ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID);
						instanceParam.put("instanceStatus", HANDLING_PRO_STATUS);
						instanceParam.put("isPaticipant", true);
						instanceParam.put("handlingUserType", IEventDisposalWorkflowService.ACTOR_TYPE_USER);
						instanceParam.put("handlingUserId", userId);
						instanceParam.put("handlingOrgId", userOrgId);
						instanceParam.put("handledUserType", IEventDisposalWorkflowService.ACTOR_TYPE_USER);
						instanceParam.put("handledUserId", userId);
						instanceParam.put("handledOrgId", userOrgId);
						
						proInstanceMapList = baseWorkflowService.findProInstanceList(instanceParam);
						
						//现有业务要求，每个人手上最多一条待办理的子流程信息
						if(proInstanceMapList != null && proInstanceMapList.size() == 1) {
							Map<String, Object> proInstanceMap = proInstanceMapList.get(0);
							
							if(CommonFunctions.isNotBlank(proInstanceMap, "INSTANCE_ID")) {
								//将主流程实例id更换为子流程实例id
								subProInstanceId = Long.valueOf(proInstanceMap.get("INSTANCE_ID").toString());
							}
						}
					}
					
					if(subProInstanceId != null && subProInstanceId > 0) {
						instanceId = subProInstanceId;
					} else {
						Map<String, Object> instanceParam = new HashMap<String, Object>();
						List<Map<String, Object>> instanceMapList = null;
						List<Long> subInstanceIdList = new ArrayList<Long>();
						boolean isNotAble2InvalidSubInstance = false, isCurrentUser = false;
						
						instanceParam.put("parentInstanceId", instanceId);
						instanceParam.put("instanceTaskId", curTaskId);
						instanceParam.put("formTypeId", ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID);
						
						try {
							isCurrentUser = isUserInfoCurrentUser(curTaskId.toString(), instanceId, userInfo);
						} catch(Exception e) {}
						
						if(isCurrentUser) {
							instanceMapList = baseWorkflowService.findProInstanceList(instanceParam);
							
							if(instanceMapList != null) {
								List<Map<String, Object>> taskDetailMapList = null;
								Long subInstanceId = null;
								String instanceStatus = null;
								
								for(Map<String, Object> instanceMap : instanceMapList) {
									if(CommonFunctions.isNotBlank(instanceMap, "STATUS")) {
										instanceStatus = instanceMap.get("STATUS").toString();
									}
									
									if(HANDLING_PRO_STATUS.equals(instanceStatus)) {
										if(CommonFunctions.isNotBlank(instanceMap, "INSTANCE_ID")) {
											subInstanceId = Long.valueOf(instanceMap.get("INSTANCE_ID").toString());
											
											if(subInstanceId != null && subInstanceId > 0) {
												subInstanceIdList.add(subInstanceId);
												
												taskDetailMapList = this.queryProInstanceDetail(subInstanceId, IEventDisposalWorkflowService.SQL_ORDER_ASC, userInfo);
												if(taskDetailMapList != null && taskDetailMapList.size() > 1) {
													isNotAble2InvalidSubInstance = true; break;
												}
											}
										}
									} else if(END_PRO_STATUS.equals(instanceStatus)) {//只要存在一条已完结的子流程，则主流程不可撤回
										isNotAble2InvalidSubInstance = true; break;
									}
								}
							}
						} else {
							//非当前办理人员撤回操作，只要有子流程，就不可撤回
							isNotAble2InvalidSubInstance = baseWorkflowService.findProInstanceCount(instanceParam) > 0;
						}
						
						if(isNotAble2InvalidSubInstance) {
							String curTaskNameZH = "";
							
							if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
								curTaskNameZH = " 【" + curTaskData.get("WF_ACTIVITY_NAME_").toString() + "】 ";
							}
							
							throw new ZhsqEventException("当前环节" + curTaskNameZH + "已有办理中协同任务，不可进行撤回操作！");
						} else if(subInstanceIdList.size() > 0) {
							//废除子流程
							for(Long instanceId2Invalid : subInstanceIdList) {
								baseWorkflowService.invalidInstance(instanceId2Invalid, null);
							}
							
							isSubInstanceInvalid = true;
						}
						
					}
				}
			} else if(ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID.equals(formTypeId)) {//子流程
				//如果是未开始的子流程，则子流程操作人员不可进行撤回操作
				List<Map<String, Object>> taskDetailMapList = this.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_ASC, userInfo);
				if(taskDetailMapList != null && taskDetailMapList.size() == 1) {//只有“启动部门协同处理”环节
					throw new ZhsqEventException("当前协同任务尚未办理，无法进行撤回操作！");
				}
			}
		} else {
			throw new IllegalArgumentException("流程实例id【" + instanceId + "】没有找到有效的流程信息！");
		}
		
		if(isSubInstanceInvalid) {
			//子流程撤回后，不再撤回主流程
			flag = true;
		} else {
			flag = super.recallWorkflow(instanceId, userInfo, params);
		}
		
		return flag;
	}
	
	@Override
	public String capInstanceIdByEventId(Long eventId, Map<String, Object> params) {
		String parentInstanceId = "", 
				  instanceId = "",
				  eventType = null,
				  HANDLING_EVENT_TYPE = "todo";	//待办事件
		
		parentInstanceId = this.capInstanceIdByEventId(eventId);
		
		if(CommonFunctions.isNotBlank(params, "eventType")) {
			eventType = params.get("eventType").toString();
		}
		
		if(StringUtils.isBlank(parentInstanceId)) {
			
		} else {
			Map<String, Object> curTaskData = this.curNodeData(Long.valueOf(parentInstanceId));
			String curTaskName = null;
			
			if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
				curTaskName = curTaskData.get("NODE_NAME").toString();
			}
			if(HANDLING_EVENT_TYPE.equals(eventType) && (STREET_NODE_NAME.equals(curTaskName) || DISTRICT_NODE_NAME.equals(curTaskName))) {
				Map<String, Object> instanceMap = new HashMap<String, Object>();
				Long curTaskId = -1L;
				
				if(CommonFunctions.isNotBlank(params, "instanceTaskId")) {
					curTaskId = Long.valueOf(params.get("instanceTaskId").toString());
				} else if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
					curTaskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
				}
				
				if(curTaskId != null && curTaskId > 0) {
					instanceMap.put("parentInstanceId", parentInstanceId);
					instanceMap.put("instanceTaskId", this.curNodeTaskId(Long.valueOf(parentInstanceId)));
					instanceMap.put("formTypeId", ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID);
					instanceMap.put("instanceStatus", HANDLING_PRO_STATUS);
					instanceMap.put("isHandlingUser", true);
					
					if(CommonFunctions.isNotBlank(params, "handlingUserId")) {
						instanceMap.put("handlingUserId", params.get("handlingUserId"));
					} else {
						throw new IllegalArgumentException("缺少属性当前办理用户id 【handlingUserId】！");
					}
					
					if(CommonFunctions.isNotBlank(params, "handlingOrgId")) {
						instanceMap.put("handlingOrgId", params.get("handlingOrgId"));
					} else {
						throw new IllegalArgumentException("缺少属性当前办理组织id 【handlingOrgId】");
					}
					
					List<Map<String, Object>> subProMapList = baseWorkflowService.findProInstanceList(instanceMap);
					if(subProMapList != null && subProMapList.size() > 0) {
						Map<String, Object> subProMap = subProMapList.get(0);
						
						if(CommonFunctions.isNotBlank(subProMap, "INSTANCE_ID")) {
							instanceId = subProMap.get("INSTANCE_ID").toString();
						}
					}
				}
			}
			
			if(StringUtils.isBlank(instanceId)) {
				instanceId = parentInstanceId;
			}
		}
		
		return instanceId;
	}
	
	@Override
	public Long capInstanceIdByEventIdForLong(Long eventId, Map<String, Object> params) {
		Long instanceId = -1L;
		String instanceIdStr = this.capInstanceIdByEventId(eventId, params);
		
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
	public ProInstance capProInstanceByEventId(Long eventId, Map<String, Object> params) {
		ProInstance pro = null;
		
		try{
			String instanceId = this.capInstanceIdByEventId(eventId, params);
			pro = this.capProInstanceByInstanceId(Long.valueOf(instanceId));
		} catch(NumberFormatException e) {
			pro = null;
		}
		
		return pro;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		Map<String, Object> resultMap = super.initFlowInfo(taskId, instanceId, userInfo, params);
		boolean isCommunityHandleEvent = false;
		
		if(CommonFunctions.isNotBlank(resultMap, "curNodeName")) {
			isCommunityHandleEvent = COMMUNITY_NODE_CODE.equals(resultMap.get("curNodeName").toString());
		}
		
		if(isCommunityHandleEvent && CommonFunctions.isNotBlank(resultMap, "proInstance")) {
			ProInstance proInstance = (ProInstance) resultMap.get("proInstance");
			Long eventId = proInstance.getFormId();
			EventDisposal event = null;
			
			if(eventId != null && eventId > 0) {
				event = eventDisposalService.findEventByIdSimple(eventId);
			}
			
			if(event != null) {
				//晋江微信上报事件，事件分类为空，社区调整后，下次待办页面初始化时，会更新事件编号，再下次打开事件页面时，才可看到变更后的事件编号
				if(BizPlatfromEnum.JJ_WECHAT.getValue().equals(event.getBizPlatform())) {
					boolean isAble2EditHandleEvent = StringUtils.isBlank(event.getType());
					
					resultMap.put("isDetail2Edit", isAble2EditHandleEvent);
				}
			}
		}
		
		if(CommonFunctions.isNotBlank(resultMap, "taskNodes")) {
			String curNodeName = null;
			Node closeNode = null;
			List<Node> taskNodes = (List<Node>) resultMap.get("taskNodes");
			
			if(CommonFunctions.isNotBlank(resultMap, "curNodeName")) {
				curNodeName = resultMap.get("curNodeName").toString();
			}
			
			if(COMMUNITY_NODE_CODE.equals(curNodeName)) {
				//判断是否可越级上报给市级处理
				boolean isNotAble2District = true;
				Map<String, Object> lastTaskInfoMap = hessianFlowService.queryLastTaskInfo(instanceId);
				
				if(lastTaskInfoMap != null && !lastTaskInfoMap.isEmpty()) {
					String operateType = null,
							  taskCode = null,
							  OPERATE_TYPE_RECALL = "5";//撤回操作
					
					if(CommonFunctions.isNotBlank(lastTaskInfoMap, "OPERATE_TYPE")) {
						operateType = lastTaskInfoMap.get("OPERATE_TYPE").toString();
					}
					if(CommonFunctions.isNotBlank(lastTaskInfoMap, "TASK_NAME")) {
						taskCode = lastTaskInfoMap.get("TASK_NAME").toString();
					}
					
					if(OPERATE_TYPE_RECALL.equals(operateType) && STREET_NODE_NAME.equals(taskCode)) {
						if(CommonFunctions.isNotBlank(lastTaskInfoMap, "INTER_TIME")) {
							String timeIntervalStr = lastTaskInfoMap.get("INTER_TIME").toString(), INTERVAL_UNIT = "天";
							int MAX_INTERVAL_STR = 3, timeIntervalDay = 0;
							
							if(timeIntervalStr.contains(INTERVAL_UNIT)) {
								//任务结束时间比开始时间早时，timeIntervalStr的格式是：超3天
								try {
									timeIntervalDay = Integer.valueOf(timeIntervalStr.substring(0, timeIntervalStr.indexOf(INTERVAL_UNIT)));
								} catch(NumberFormatException e) {}
							}
							//办理耗时超过3天(包含3天)，则可以越级上报市级处理
							isNotAble2District = timeIntervalDay < MAX_INTERVAL_STR;
						}
					}
				}
				
				if(isNotAble2District) {
					for(Node node : taskNodes) {
						if(DISTRICT_NODE_NAME.equals(node.getNodeName())) {
							taskNodes.remove(node);
							break;
						}
					}
				}
				
			} else if(STREET_NODE_NAME.equals(curNodeName) || DISTRICT_NODE_NAME.equals(curNodeName)) {
				
				Map<String, Object> instanceMap = new HashMap<String, Object>();
				Node node = new Node();
				String transitionCode = null;
				Long orgId = userInfo.getOrgId();
				
				instanceMap.put("parentInstanceId", instanceId);
				instanceMap.put("instanceTaskId", taskId);
				instanceMap.put("formTypeId", ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID);
				instanceMap.put("instanceStatus", HANDLING_PRO_STATUS);
				
				//启动了子流程，则将下一办理环节清空
				if(baseWorkflowService.findProInstanceCount(instanceMap) > 0) {
					taskNodes.clear();
				}
				if(orgId != null && orgId > 0) {
					OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
					if(orgInfo != null) {
						transitionCode = INodeCodeHandler.ORG_UINT + orgInfo.getChiefLevel() + INodeCodeHandler.OPERATE_FLOW + "0" + INodeCodeHandler.ORG_DEPT + orgInfo.getChiefLevel();
					}
				}
				
				if(transitionCode != null) {//添加协同处理环节
					node.setNodeId(-1);
					node.setNodeName(ConstantValue.COORDINATE_TASK_CODE);
					node.setNodeNameZH(ConstantValue.COORDINATE_TASK_NAME);//环节中文名称
					node.setDynamicSelect("1");//动态跳转
					node.setNodeType("1");
					node.setTransitionCode(transitionCode);
					
					taskNodes.add(node);
				}
			}
			
			if(!COMMUNITY_NODE_CODE.equals(curNodeName)) {
				//查找结案操作环节
				for(Node node : taskNodes) {
					if(CLOSE_NODE_CODE.equals(node.getNodeCode())) {
						closeNode = node;
						break;
					}
				}
				
				if(closeNode != null) {
					boolean isCollect2Close = false;
					ProInstance proInstance = null;
					
					if(CommonFunctions.isNotBlank(resultMap, "proInstance")) {
						proInstance = (ProInstance) resultMap.get("proInstance");
					}
					
					if(proInstance != null) {
						Long collectUserId = proInstance.getUserId(), collectOrgId = proInstance.getOrgId();
						
						if(collectUserId != null && collectUserId > 0 && collectOrgId != null && collectOrgId > 0) {
							isCollect2Close = collectUserId.equals(userInfo.getUserId()) && collectOrgId.equals(userInfo.getOrgId());
						}
					}
					
					if(isCollect2Close) {
						taskNodes.remove(closeNode);
					}
				}
			}
			
			resultMap.put("taskNodes", taskNodes);
		}
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int sqlOrder, UserInfo userInfo) {
		List<Map<String, Object>> taskMapList = super.queryProInstanceDetail(instanceId, sqlOrder, userInfo),
													resultMapList = new ArrayList<Map<String, Object>>();
		boolean isRemoveDistrictNode = false;
		
		if(taskMapList != null && taskMapList.size() > 1) {
			if(instanceId != null && instanceId > 0) {
				ProInstance proInstance = this.capProInstanceByInstanceId(instanceId);
				Integer formTypeId = null;
				Long eventId = null;
				
				if(proInstance != null) {
					formTypeId = proInstance.getFormTypeId();
					
					eventId = proInstance.getFormId();
				}
				
				if(ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID.equals(String.valueOf(formTypeId))) {
					EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
					if(event != null) {
						String bizPlatform = event.getBizPlatform();
						
						isRemoveDistrictNode = BizPlatfromEnum.JJ_BMFWZX.getValue().equals(bizPlatform)
											|| BizPlatfromEnum.JJ_GZ_WECHAT.getValue().equals(bizPlatform);
					}
				}
			}
			
			if(isRemoveDistrictNode) {
				Map<String, Object> districtMap = null;
				
				switch(sqlOrder){
					case IEventDisposalWorkflowService.SQL_ORDER_ASC:{
						districtMap = taskMapList.get(1); break;
					}
					case IEventDisposalWorkflowService.SQL_ORDER_DESC:{
						districtMap = taskMapList.get(taskMapList.size() - 2); break;
					}
					default:{
						districtMap = taskMapList.get(1); break;
					}
				}
				
				if(DISTRICT_NODE_NAME.equals(districtMap.get("TASK_CODE"))) {
					taskMapList.remove(districtMap);
				}
			}
		}
		
		if(taskMapList != null) {
			Map<String, Object> proMap = new HashMap<String, Object>();
			List<Map<String, Object>> proMapList = null, subProTaskMapList = null;
			String taskName = null;
			
			proMap.put("parentInstanceId", instanceId);
			proMap.put("formTypeId", ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID);
			proMap.put("instanceStatus", HANDLING_PRO_STATUS + "," + END_PRO_STATUS + "," + ABOLISH_PRO_STATUS);// 1 办理中；2 归档；4 废除
			
			for(Map<String, Object> taskMap : taskMapList) {
				taskName = null;
				
				if(CommonFunctions.isNotBlank(taskMap, "TASK_CODE")) {
					taskName = taskMap.get("TASK_CODE").toString();
				}
				
				if((STREET_NODE_NAME.equals(taskName) || DISTRICT_NODE_NAME.equals(taskName)) && 
						CommonFunctions.isNotBlank(taskMap, "TASK_ID")) {
					proMap.put("instanceTaskId", taskMap.get("TASK_ID"));
					proMapList = baseWorkflowService.findProInstanceList(proMap);
					
					if(proMapList != null) {
						for(Map<String, Object> proMapTmp : proMapList) {
							if(CommonFunctions.isNotBlank(proMapTmp, "INSTANCE_ID")) {
								subProTaskMapList = super.queryProInstanceDetail(Long.valueOf(proMapTmp.get("INSTANCE_ID").toString()), sqlOrder, userInfo);
								if(subProTaskMapList != null) {
									resultMapList.addAll(subProTaskMapList);
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
	public Map<String, Object> capInfo4SMS(String curNodeName, String nextNodeName, Map<String, Object> params, UserInfo userInfo) throws Exception {
		Map<String, Object> resultMap = null;
		String closeNodeCode = null;
		
		if(CommonFunctions.isNotBlank(params, "nodeId")) {
			Integer nodeId = -1;
			
			try {
				nodeId = Integer.valueOf(params.get("nodeId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
			if(nodeId > 0) {
				Node closeNode = this.findNodeById(nodeId);
				if(closeNode != null) {
					closeNodeCode = closeNode.getNodeCode();
				}
			}
		}
		
		if(CLOSE_NODE_CODE.equals(closeNodeCode)) {
			Long eventId = -1L, 
				 curUserId = -1L, curOrgId = -1L,
				 createUserId = -1L, createOrgId = -1L;
			
			if(CommonFunctions.isNotBlank(params, "eventId")) {
				eventId = Long.valueOf(params.get("eventId").toString());
			}
			if(userInfo != null) {
				curUserId = userInfo.getUserId();
				curOrgId = userInfo.getOrgId();
			}
			
			if(eventId > 0) {
				ProInstance pro = this.capProInstanceByEventId(eventId);
				if(pro != null) {
					createUserId = pro.getUserId();
					createOrgId = pro.getOrgId();
				}
			}
			
			//采集者与结案操作者为相同人员时
			if(curUserId > 0 && curOrgId > 0 && createUserId > 0 && createOrgId > 0) {
				if(curUserId.equals(createUserId) && curOrgId.equals(createOrgId)) {
					OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(createOrgId);
					Long parentOrgId = -1L;
					
					if(orgInfo != null) {//该方法只适用于旧组织
						parentOrgId = orgInfo.getParentOrgId();
						
						if(fiveKeyElementService.isUserIdGridAdmin(createOrgId, createUserId)) {
							List<UserBO> userBoList = fiveKeyElementService.findCollectToCloseUserList(parentOrgId);
							if(userBoList != null) {
								Set<String> receiverMobilePhoneSet = new HashSet<String>();
								Long verifyMobile = null;
								
								for(UserBO userBO : userBoList) {
									verifyMobile = userBO.getVerifyMobile();
									
									if(verifyMobile != null && verifyMobile > 0) {
										receiverMobilePhoneSet.add(verifyMobile.toString());
									}
								}
								
								if(receiverMobilePhoneSet.size() > 0) {
									params.put("receiverMobilePhones", StringUtils.join(receiverMobilePhoneSet, ","));
								}
							}
						} else {
							params.put("receiverOrgIds", parentOrgId);
						}
					}
				} else {
					params.put("receiverIds", createUserId);
				}
			}
		}
		
		resultMap = super.capInfo4SMS(curNodeName, nextNodeName, params, userInfo);
		
		return resultMap;
	}
	
	@Override
	public void sendSms(String userIds, String otherMobiles,
			String nextNodeName, Long instanceId, String smsContent, UserInfo userInfo) {
		if(StringUtils.isNotBlank(otherMobiles)) {
			this.sendSms(null, otherMobiles, smsContent, userInfo);
		} else {
			super.sendSms(userIds, otherMobiles, nextNodeName, instanceId, smsContent, userInfo);
		}
	}
	
	@Override
	public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {
		int resultCode = 0;
		String wrongMsg = "";
		
		if(orgSocialInfo != null){
			String chiefLevel = orgSocialInfo.getChiefLevel(),
				   orgType = orgSocialInfo.getOrgType();
			
			if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgType)) {
				if(ConstantValue.DISTRICT_ORG_LEVEL.compareTo(chiefLevel) > 0) {
					wrongMsg = "当前组织类型为【单位】，组织层级不得超过【区/县】！";
				}
			} else if(String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType)) {
				if(!ConstantValue.STREET_ORG_LEVEL.equals(chiefLevel) && !ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevel)) {
					wrongMsg = "当前组织类型为【部门】，但组织层级既不是【街道/乡镇】也不是【区/县】！";
				}
			}
		}
		
		if(StringUtils.isNotBlank(wrongMsg)) {
			throw new Exception(wrongMsg);
		} else {
			resultCode = super.checkUserToStartWorkflow(orgSocialInfo, userId);
		}
		
		return resultCode;
	}
	
	/**
	 * 更新事件状态
	 * @param eventId
	 * @param userId
	 * @param orgId
	 * @param chiefLevel
	 * @param resultMap
	 */
	protected void updateEventStatus(Long eventId, Long userId, OrgSocialInfoBO orgInfo, Map<String, Object> resultMap) {
		Long instanceId = -1L;
		ProInstance pro = null;
		
		if(CommonFunctions.isNotBlank(resultMap, "instanceId")) {
			try {
				instanceId = Long.valueOf(resultMap.get("instanceId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if(instanceId != null && instanceId > 0) {
			pro = this.capProInstanceByInstanceId(instanceId);
			
			if(pro != null && ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID.equals(String.valueOf(pro.getFormTypeId()))) {
				super.updateEventStatus(eventId, userId, orgInfo, resultMap);
			}
		}
	}
	
}
