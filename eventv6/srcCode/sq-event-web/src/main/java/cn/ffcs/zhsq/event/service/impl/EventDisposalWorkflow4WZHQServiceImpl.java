package cn.ffcs.zhsq.event.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.reportFocus.reportMsgCCSet.IReportMsgCCSetService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.JsonUtils;

/**
 * @Description: 重庆万州区(WanZHouQu) 工作流使用接口实现
 * @ClassName:   EventDisposalWorkflow4WZHQServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年4月23日 上午11:13:32
 */
@Service(value = "eventDisposalWorkflow4WZHQService")
public class EventDisposalWorkflow4WZHQServiceImpl extends EventDisposalWorkflowForEventNewServiceImpl {
	@Autowired
	private IReportMsgCCSetService reportMsgCCSetService;
	
	@Autowired
	private IEventExtendService eventExtendService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	private static final String CLOSE_NODE_CODE = "task8";
	private static final String STREET_LEADER_ASSIGNMENT_NODE_CODE = "task12";
	private static final String STREET_LEADER_DISTRIBUTE_NODE_CODE = "task2";//子流程领导下派
	private static final String HANDLING_PRO_STATUS = "1", END_PRO_STATUS = "2";
	
	@Override
	public String capWorkflowHandlePage(Long eventId, String eventType, UserInfo userInfo, Map<String, Object> extraParam) {
		String handlePage = null, curNodeName = null;
		
		if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
			curNodeName = extraParam.get("curNodeName").toString();
		} else if(CommonFunctions.isNotBlank(extraParam, "proInstance")) {
			Object proInstanceObj = extraParam.get("proInstance");
			
			if(proInstanceObj instanceof ProInstance) {
				ProInstance proInstance = (ProInstance) proInstanceObj;
				
				curNodeName = proInstance.getCurNode();
			}
		} else if(eventId != null && eventId > 0) {
			ProInstance proInstance = capProInstanceByEventId(eventId);
			
			if(proInstance != null) {
				curNodeName = proInstance.getCurNode();
			}
		}
		
		if(STREET_LEADER_ASSIGNMENT_NODE_CODE.equals(curNodeName)) {
			handlePage = "handle_wanzhouqu.ftl";
		} else {
			handlePage = super.capWorkflowHandlePage(eventId, eventType, userInfo, extraParam);
		}
		
		return handlePage;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		Map<String, Object> initResultMap = super.initFlowInfo(taskId, instanceId, userInfo, params);
		String curNodeName = null, STREET_LEADER_ASSIGNMENT_NODE_CODE = "task12";
		
		if(CommonFunctions.isNotBlank(initResultMap, "curNodeName")) {
			curNodeName = initResultMap.get("curNodeName").toString();
		}
		
		if(STREET_LEADER_ASSIGNMENT_NODE_CODE.equals(curNodeName)) {
			if(CommonFunctions.isNotBlank(initResultMap, "taskNodes")) {
				List<Node> taskNodes = (List<Node>) initResultMap.get("taskNodes");
				String transitionCode = "U4G0U0";
				Node lastNode = taskNodes.get(taskNodes.size() - 1), node = new Node();
				
				if(ConstantValue.HANDLING_TASK_CODE.equals(lastNode.getNodeName())) {
					taskNodes.remove(lastNode);
				}
				
				node.setNodeId(-1);
				node.setNodeName(ConstantValue.COORDINATE_TASK_CODE);
				node.setNodeNameZH("领导下派");//环节中文名称
				node.setDynamicSelect("1");//动态跳转
				node.setNodeType("1");
				node.setTransitionCode(transitionCode);
				
				taskNodes.add(node);
				
				initResultMap.put("taskNodes", taskNodes);
			}
		}
		
		return initResultMap;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice, UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{
		boolean flag = false;
		String curNodeName = null;
		
		if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
			curNodeName = extraParam.get("curNodeName").toString();
		}
		
		if(StringUtils.isBlank(advice) && CommonFunctions.isNotBlank(extraParam, "advice")) {
			advice = extraParam.get("advice").toString();
		}
		
		if(STREET_LEADER_ASSIGNMENT_NODE_CODE.equals(curNodeName)) {
			Long eventId = null;
			
			try {
				if(CommonFunctions.isNotBlank(extraParam, "eventId")) {
					eventId = Long.valueOf(extraParam.get("eventId").toString());
				} else if(CommonFunctions.isNotBlank(extraParam, "formId")) {
					eventId = Long.valueOf(extraParam.get("formId").toString());
				}
			} catch(NumberFormatException e) {}
			
			if(ConstantValue.COORDINATE_TASK_CODE.equals(nextNodeName)) {
				if(CommonFunctions.isNotBlank(extraParam, "nextHandlerJson")) {
					Map<String, Object> nextHandlerMap = JsonUtils.json2Map(extraParam.get("nextHandlerJson").toString()),
										workflowParam = new HashMap<String, Object>(),
										startParam = new HashMap<String, Object>();
					List<Map<String, Object>> hanlerMapList = null;
					Long workflowId = null;
					String subProInstanceIdStr = null, 
						   nextUserId = null, nextOrgId = null,
						   nextUserName = null,
						   SUB_WORKFLOW_NAME = "重庆万州事件子流程_4";
					StringBuffer subAdvice = new StringBuffer("");
					
					workflowParam.put("workflowName", SUB_WORKFLOW_NAME);
					workflowId = capWorkflowId(null, null, userInfo, workflowParam);
					
					for(String userType : nextHandlerMap.keySet()) {
						hanlerMapList = JsonUtils.json2List(nextHandlerMap.get(userType).toString(), Map.class);
						
						for(Map<String, Object> handlerMap : hanlerMapList) {
							startParam.clear();
							nextUserId = null;
							nextUserName = null;
							nextOrgId = null;
							subAdvice.setLength(0);
							
							if(CommonFunctions.isNotBlank(handlerMap, "userId")) {
								nextUserId = handlerMap.get("userId").toString();
							}
							if(CommonFunctions.isNotBlank(handlerMap, "userName")) {
								nextUserName = handlerMap.get("userName").toString();
							}
							if(CommonFunctions.isNotBlank(handlerMap, "orgId")) {
								nextOrgId = handlerMap.get("orgId").toString();
							}
							if(CommonFunctions.isNotBlank(handlerMap, "advice")) {
								subAdvice.append("批示意见：").append(advice).append("；").append(handlerMap.get("advice").toString());
							}
							
							startParam.put("parentInstanceId", instanceId);
							startParam.put("instanceTaskId", taskId);
							startParam.put("isConvertProinstance", false);//强制启动流程，不校验
							
							subProInstanceIdStr = startFlowByWorkFlow(eventId, workflowId, ConstantValue.WORKFLOW_IS_NO_CLOSE, userInfo, startParam);
							
							if(StringUtils.isNotBlank(subProInstanceIdStr)) {
								Long subProInstanceId = Long.valueOf(subProInstanceIdStr);
								Map<String, Object> subTaskMap = baseWorkflowService.findCurTaskData(subProInstanceId),
													subParam = new HashMap<String, Object>();
								String subTaskId = null;
								
								if(CommonFunctions.isNotBlank(subTaskMap, "WF_DBID_")) {
									subTaskId = subTaskMap.get("WF_DBID_").toString();
								}
								
								subParam.put("eventId", eventId);
								subParam.put("formId", eventId);
								
								flag = super.subWorkFlowForEndingAndEvaluate(subProInstanceId, subTaskId, STREET_LEADER_DISTRIBUTE_NODE_CODE, nextUserId, nextOrgId, subAdvice.toString(), userInfo, null, subParam);
								
								if(flag) {
									Map<String, Object> msgCCSetMap = new HashMap<String, Object>();
									
									subTaskMap = baseWorkflowService.findCurTaskData(subProInstanceId);
									if(CommonFunctions.isNotBlank(subTaskMap, "WF_DBID_")) {
										subTaskId = subTaskMap.get("WF_DBID_").toString();
									}
									
									msgCCSetMap.put("taskId", subTaskId);
									msgCCSetMap.put("userId", nextUserId);
									msgCCSetMap.put("userName", nextUserName);
									msgCCSetMap.put("orgId", nextOrgId);
									msgCCSetMap.put("ccType", userType);
									
									reportMsgCCSetService.saveMsgCCSet(msgCCSetMap, userInfo);
								}
							}
						}
					}
				}
				
				Map<String, Object> eventExtend = new HashMap<String, Object>();
				if(CommonFunctions.isNotBlank(extraParam, "planType")) {
					eventExtend.put("planType", extraParam.get("planType"));
				}
				if(CommonFunctions.isNotBlank(extraParam, "planLevel")) {
					eventExtend.put("planLevel", extraParam.get("planLevel"));
				}
				if(CommonFunctions.isNotBlank(extraParam, "influenceLevel")) {
					eventExtend.put("influenceLevel", extraParam.get("influenceLevel"));
				}
				if(CommonFunctions.isNotBlank(extraParam, "isPlan")) {
					eventExtend.put("isPlan", extraParam.get("isPlan"));
				}
				if(CommonFunctions.isNotBlank(extraParam, "planConfigId")) {
					eventExtend.put("planConfigId", extraParam.get("planConfigId"));
				}
				
				if(!eventExtend.isEmpty()) {
					eventExtend.put("eventId", eventId);
					
					//为了防止扩展信息保存失败导致流程提交失败
					try {
						eventExtendService.saveEventExtend(eventExtend);
					} catch(Exception e) {
						e.printStackTrace();
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
			}
		} else {
			flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		}
		
		return flag;
	}
	
	@Override
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int sqlOrder, UserInfo userInfo) {
		List<Map<String, Object>> taskMapList = super.queryProInstanceDetail(instanceId, sqlOrder, userInfo),
								  resultMapList = new ArrayList<Map<String, Object>>();
		ProInstance proInstance = null;
		
		if(instanceId != null && instanceId > 0) {
			proInstance = baseWorkflowService.findProByInstanceId(instanceId);
		}
		
		if(proInstance != null && ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID.equals(proInstance.getFormTypeId())) {
			//子流程直接转换为父级流程展示
			//instanceId = proInstance.getParentId();
		}
		
		taskMapList = super.queryProInstanceDetail(instanceId, sqlOrder, userInfo);
		
		if(taskMapList != null) {
			Map<String, Object> proMap = new HashMap<String, Object>();
			List<Map<String, Object>> proMapList = null, subProTaskMapList = null;
			String taskName = null;
			List<BaseDataDict> planRoleDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.EMERGENC_PLAN_ROLE, userInfo.getOrgCode());
			Map<String, String> userTypeDictMap = new HashMap<String, String>();
			
			proMap.put("parentInstanceId", instanceId);
			proMap.put("formTypeId", ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID);
			proMap.put("instanceStatus", HANDLING_PRO_STATUS + "," + END_PRO_STATUS);// 1 办理中；2 归档；4 废除
			
			if(planRoleDictList != null) {
				for(BaseDataDict planRoleDict : planRoleDictList) {
					userTypeDictMap.put(planRoleDict.getDictGeneralCode(), planRoleDict.getDictName());
				}
			}
			
			for(Map<String, Object> taskMap : taskMapList) {
				taskName = null;
				
				if(CommonFunctions.isNotBlank(taskMap, "TASK_CODE")) {
					taskName = taskMap.get("TASK_CODE").toString();
				}
				
				if((STREET_LEADER_ASSIGNMENT_NODE_CODE.equals(taskName)) && 
						CommonFunctions.isNotBlank(taskMap, "TASK_ID")) {
					proMap.put("instanceTaskId", taskMap.get("TASK_ID"));
					proMapList = baseWorkflowService.findProInstanceList(proMap);
					
					if(proMapList != null) {
						taskMap.put("SUB_PRO_COUNT", proMapList.size());
						
						for(Map<String, Object> proMapTmp : proMapList) {
							if(CommonFunctions.isNotBlank(proMapTmp, "INSTANCE_ID")) {
								subProTaskMapList = super.queryProInstanceDetail(Long.valueOf(proMapTmp.get("INSTANCE_ID").toString()), sqlOrder, userInfo);
								
								if(subProTaskMapList != null) {
									String subTaskName = null;
									Map<String, Object> ccSetParam = new HashMap<String, Object>();
									
									for(Map<String, Object> subProTaskMap : subProTaskMapList) {
										ccSetParam.clear();
										subTaskName = null;
										
										if(CommonFunctions.isBlank(subProTaskMap, "PARENT_PRO_TASK_ID")) {
											subProTaskMap.put("PARENT_PRO_TASK_ID", taskMap.get("TASK_ID"));
										}
										
										if(CommonFunctions.isNotBlank(subProTaskMap, "TRANSACTOR_NAME") || CommonFunctions.isNotBlank(subProTaskMap, "HANDLE_PERSON")) {
											if(CommonFunctions.isNotBlank(subProTaskMap, "TASK_CODE")) {
												subTaskName = subProTaskMap.get("TASK_CODE").toString();
											}
											
											if(STREET_LEADER_DISTRIBUTE_NODE_CODE.equals(subTaskName)) {
												if(CommonFunctions.isNotBlank(subProTaskMap, "TASK_ID")) {
													ccSetParam.put("taskId", subProTaskMap.get("TASK_ID"));
												}
											}
											
											if(!subProTaskMap.isEmpty()) {
												List<Map<String, Object>> ccSetMapList = null;
												
												if(!ccSetParam.isEmpty()) {
													ccSetMapList = reportMsgCCSetService.findMsgCCSetByParams(ccSetParam);
												}
												
												if(ccSetMapList != null) {
													Map<String, String> ccUserTypeMap = new HashMap<String, String>();
													
													for(Map<String, Object> ccSetMap : ccSetMapList) {
														if(CommonFunctions.isNotBlank(ccSetMap, "userId") 
																&& CommonFunctions.isNotBlank(ccSetMap, "orgId")
																&& CommonFunctions.isNotBlank(ccSetMap, "ccType")) {
															ccUserTypeMap.put(ccSetMap.get("userId") + "-" + ccSetMap.get("orgId"), ccSetMap.get("ccType").toString());
														}
													}
													
													if(CommonFunctions.isNotBlank(subProTaskMap, "TRANSACTOR_ID") && CommonFunctions.isNotBlank(subProTaskMap, "ORG_ID")) {
														String[] userIdArray = subProTaskMap.get("TRANSACTOR_ID").toString().split(","),
																 userNameArray = null,
																 orgIdArray = subProTaskMap.get("ORG_ID").toString().split(",");
														String userKey = null, userType = null, userNameKey = null, userNameSeperator = null;
														
														if(CommonFunctions.isNotBlank(subProTaskMap, "HANDLE_PERSON")) {
															userNameKey = "HANDLE_PERSON";
															userNameSeperator = ",";
															userNameArray = subProTaskMap.get("HANDLE_PERSON").toString().split(",");
														} else if(CommonFunctions.isNotBlank(subProTaskMap, "TRANSACTOR_NAME")) {
															userNameKey = "TRANSACTOR_NAME";
															userNameSeperator = ";";
															userNameArray = subProTaskMap.get("TRANSACTOR_NAME").toString().split(";");
														}
														for(int index = 0, len = userIdArray.length; index < len; index++) {
															userKey = userIdArray[index] + "-" + orgIdArray[index];
															userType = null;
															
															if(ccUserTypeMap.containsKey(userKey)) {
																userType = ccUserTypeMap.get(userKey);
															}
															
															if(userTypeDictMap.containsKey(userType)) {
																userNameArray[index] = "【" + userTypeDictMap.get(userType) +"】" + userNameArray[index];
															}
														}
														
														subProTaskMap.put(userNameKey, StringUtils.join(userNameArray, userNameSeperator));
													}
												}
											}
											
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
	
	//方案确定后，该方法需要删除
	public List<Map<String, Object>> queryProInstanceDetail1(Long instanceId, int sqlOrder, UserInfo userInfo) {
		List<Map<String, Object>> taskMapList = super.queryProInstanceDetail(instanceId, sqlOrder, userInfo),
								  resultMapList = new ArrayList<Map<String, Object>>();
		
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
				
				if((STREET_LEADER_ASSIGNMENT_NODE_CODE.equals(taskName)) && 
						CommonFunctions.isNotBlank(taskMap, "TASK_ID")) {
					proMap.put("instanceTaskId", taskMap.get("TASK_ID"));
					proMapList = baseWorkflowService.findProInstanceList(proMap);
					
					if(proMapList != null) {
						//taskMap.put("SUB_PRO_COUNT", proMapList.size());
						Map<String, Map<String, Object>> subTaskMap = new HashMap<String, Map<String, Object>>();
						Map<String, Object> subTaskMapTmp = null;
						List<Map<String, Object>> subTaskList = null;
						String subTaskName = null;
						
						for(Map<String, Object> proMapTmp : proMapList) {
							if(CommonFunctions.isNotBlank(proMapTmp, "INSTANCE_ID")) {
								subProTaskMapList = super.queryProInstanceDetail(Long.valueOf(proMapTmp.get("INSTANCE_ID").toString()), sqlOrder, userInfo);
								
								if(subProTaskMapList != null) {
									List<Map<String, Object>> timeAndRemarkList = null;
									Map<String, Object> timeAndRemarkMap = null;
									
									for(Map<String, Object> subProTaskMap : subProTaskMapList) {
										subTaskName = null;
										
										if(CommonFunctions.isNotBlank(subProTaskMap, "TASK_NAME")) {
											subTaskName = subProTaskMap.get("TASK_NAME").toString();
											
											if(subTaskMap.containsKey(subTaskName)) {
												subTaskMapTmp = subTaskMap.get(subTaskName);
											} else {
												subTaskMapTmp = new HashMap<String, Object>();
												subTaskMapTmp.putAll(subProTaskMap);
											}
											
											if(subTaskMapTmp.containsKey("timeAndRemarkList")) {
												timeAndRemarkList = (List<Map<String, Object>>) subTaskMapTmp.get("timeAndRemarkList");
											} else {
												timeAndRemarkList = new ArrayList<Map<String, Object>>();
											}
											
											timeAndRemarkMap = new HashMap<String, Object>();
											timeAndRemarkMap.putAll(subProTaskMap);
											timeAndRemarkList.add(timeAndRemarkMap);
											
											if(CommonFunctions.isNotBlank(subTaskMapTmp, "HANDLE_PERSON") && CommonFunctions.isNotBlank(subProTaskMap, "HANDLE_PERSON")) {
												subTaskMapTmp.put("HANDLE_PERSON", subTaskMapTmp.get("HANDLE_PERSON").toString() + subProTaskMap.get("HANDLE_PERSON").toString());
											}
											
											subTaskMapTmp.put("timeAndRemarkList", timeAndRemarkList);
											
											subTaskMap.put(subTaskName, subTaskMapTmp);
										}
									}
									
									//resultMapList.addAll(subProTaskMapList);
									
									//taskMap.put("subTaskList", subProTaskMapList);
								}
							}
						}
						
						if(subTaskMap != null && !subTaskMap.isEmpty()) {
							Map<String, Object> subProTaskMap = null;
							for(String key : subTaskMap.keySet()) {
								subProTaskMap = subTaskMap.get(key);
								
								if(CommonFunctions.isNotBlank(subProTaskMap, "timeAndRemarkList")) {
									subProTaskMap.put("subAndReceivedTaskList", subProTaskMap.get("timeAndRemarkList"));
									subProTaskMap.remove("timeAndRemarkList");
								}
								
								resultMapList.add(subProTaskMap);
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
		Map<String, Object> resultMap = super.capInfo4SMS(curNodeName, nextNodeName, params, userInfo);
		
		if(CommonFunctions.isNotBlank(resultMap, "smsContent")) {
			String smsContent = resultMap.get("smsContent").toString();
			
			if(smsContent.contains("@planTypeName@")) {
				String planTypeName = "";
				
				if(CommonFunctions.isNotBlank(params, "planTypeName")) {
					planTypeName = params.get("planTypeName").toString();
				}
				
				smsContent = smsContent.replaceAll("@planTypeName@", planTypeName);
			}
			if(smsContent.contains("@planLevelName@")) {
				String planLevelName = "";
				
				if(CommonFunctions.isNotBlank(params, "planLevelName")) {
					planLevelName = params.get("planLevelName").toString();
				}
				
				smsContent = smsContent.replaceAll("@planLevelName@", planLevelName);
			}
			
			resultMap.put("smsContent", smsContent);
		}
		
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void sendSms(String userIds, String otherMobiles, String smsContent, UserInfo userInfo, Map<String, Object> params) {
		Long instanceId = null;
		String nextNodeName = null;
		StringBuffer userIdBuffer = new StringBuffer("");
		
		if(StringUtils.isNotBlank(userIds)) {
			userIdBuffer.append(",").append(userIds);
		}
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {}
		}
		if(CommonFunctions.isNotBlank(params, "nextNodeName")) {
			nextNodeName = params.get("nextNodeName").toString();
		}
		if(CommonFunctions.isNotBlank(params, "receiverIds")) {
			userIdBuffer.append(",").append(params.get("receiverIds"));
		}
		
		if(CommonFunctions.isNotBlank(params, "nextHandlerJson")) {
			Map<String, Object> nextHandlerMap = JsonUtils.json2Map(params.get("nextHandlerJson").toString());
			List<Map<String, Object>> hanlerMapList = null;
			
			for(String userType : nextHandlerMap.keySet()) {
				hanlerMapList = JsonUtils.json2List(nextHandlerMap.get(userType).toString(), Map.class);
				
				for(Map<String, Object> handlerMap : hanlerMapList) {
					
					if(CommonFunctions.isNotBlank(handlerMap, "userId")) {
						userIdBuffer.append(",").append(handlerMap.get("userId"));
					}
				}
			}
		}
		
		if(userIdBuffer.length() > 0) {
			userIds = userIdBuffer.substring(1);
		}
		
		sendSms(userIds, otherMobiles, nextNodeName, instanceId, smsContent, userInfo);
	}
}
