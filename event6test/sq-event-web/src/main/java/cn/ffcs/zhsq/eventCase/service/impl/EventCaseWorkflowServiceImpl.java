package cn.ffcs.zhsq.eventCase.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EvaResult;
import cn.ffcs.shequ.zzgl.service.event.IEvaResultService;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventCase.service.IEventCaseWorkflowService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

@Service(value = "eventCaseWorkflowService")
public class EventCaseWorkflowServiceImpl implements IEventCaseWorkflowService {
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Resource(name="eventCaseStatusDecisionMakingService")  
	private IWorkflowDecisionMakingService<Boolean> statusDecisionMaking;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IEvaResultService evaResultService;
	
	private final String CASE_WORKFLOW_NAME = "江西省罗坊镇案件",//案件流程图名称
	 			  		 CASE_WFTYPE_ID = "jxlf_case",//案件流程类型
	 			  		 START_NODE_NAME = "start",//流程开始节点名称
	 			  		 REJECT_NODE_NAME = "reject",//驳回节点名称，虚拟节点名称
	 			  		 EVA_NODE_CODE = "PJ";//评价节点编码
	
	@Override
	public boolean startWorkflow4Case(Long caseId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		Long instanceId = -1L;
		
		boolean flag = isUserAbleToStart(userInfo);
		
		if(flag && caseId != null && caseId > 0) {
			instanceId = baseWorkflowService.startWorkflow4Base(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, caseId, userInfo, null);
			
			if(instanceId != null && instanceId > 0) {
				String curNodeName = null, nextNodeName = null;
				
				alterCaseStatus(caseId, START_NODE_NAME, START_NODE_NAME, userInfo);//为了防止提交失败而导致状态仍保留在草稿状态
				
				List<Map<String, Object>> nextNodes = findNextTaskNodes(caseId, userInfo, null);
				if(nextNodes != null && nextNodes.size() > 0) {
					Map<String, Object> nextNodeMap = nextNodes.get(0);
					
					if(CommonFunctions.isNotBlank(nextNodeMap, "nodeName")) {
						nextNodeName = nextNodeMap.get("nodeName").toString();
					}
				}
				
				if(StringUtils.isNotBlank(nextNodeName)) {
					boolean isClose = false;
					Map<String, Object> subMap = new HashMap<String, Object>();
					String advice = null;
					curNodeName = START_NODE_NAME;
					
					if(CommonFunctions.isNotBlank(extraParam, "isClose")) {
						isClose = Boolean.valueOf(extraParam.get("isClose").toString());
					}

					subMap.putAll(extraParam);
					
					if(isClose && CommonFunctions.isNotBlank(subMap, "advice")) {
						advice = subMap.get("advice").toString();
						
						subMap.remove("advice");
					}
					
					subMap.put("curNodeName", curNodeName);
					
					flag = this.subWorkflow4Case(caseId, nextNodeName, null, userInfo, subMap);
					
					if(flag && isClose) {//跳转到评价节点
						nextNodes = findNextTaskNodes(caseId, userInfo, null);
						curNodeName = nextNodeName;
						
						if(nextNodes != null) {
							nextNodeName = null;
							
							for(Map<String, Object> nextNodeMap : nextNodes) {
								if(CommonFunctions.isNotBlank(nextNodeMap, "nodeCode")) {
									if(EVA_NODE_CODE.equals(nextNodeMap.get("nodeCode").toString())) {
										nextNodeName = nextNodeMap.get("nodeName").toString();
									}
								}
								
							}
							
							if(nextNodeName != null) {
								Long parentOrgId = -1L;
								
								if(CommonFunctions.isNotBlank(extraParam, "parentOrgId")) {
									try {
										parentOrgId = Long.valueOf(extraParam.get("parentOrgId").toString());
									} catch(NumberFormatException e) {
										e.printStackTrace();
									}
								}
								if(parentOrgId < 0 && userInfo != null) {
									//由于使用的是旧组织，故可以这样取父级组织
									OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
									
									if(orgInfo != null) {
										parentOrgId = orgInfo.getParentOrgId();
									}
								}
								
								if(parentOrgId > 0) {
									List<UserInfo> nextUserInfoList = new ArrayList<UserInfo>();
									UserInfo userInfoTmp = new UserInfo();
									
									subMap.clear();
									
									subMap.put("curNodeName", curNodeName);
									subMap.put("advice", advice);
									
									userInfoTmp.setOrgId(parentOrgId);
									nextUserInfoList.add(userInfoTmp);
									
									flag = this.subWorkflow4Case(caseId, nextNodeName, nextUserInfoList, userInfo, subMap);
								}
							}
						}
					}
				}
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean subWorkflow4Case(Long caseId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		String nextUserIds = null, nextOrgIds = null;
		
		if(caseId == null || caseId < 0) {
			throw new IllegalArgumentException("缺少案件id！");
		}
		if(StringUtils.isBlank(nextNodeName)) {
			throw new IllegalArgumentException("缺少可办理的下一环节！");
		}
		if(nextUserInfoList == null) {
			nextUserInfoList = new ArrayList<UserInfo>();
		}
		if(CommonFunctions.isNotBlank(extraParam, "nextUserIds")) {
			nextUserIds = extraParam.get("nextUserIds").toString();
		}
		if(CommonFunctions.isNotBlank(extraParam, "nextOrgIds")) {
			nextOrgIds = extraParam.get("nextOrgIds").toString();
		}
		if(StringUtils.isNotBlank(nextUserIds) && StringUtils.isNotBlank(nextOrgIds)) {
			String[] nextUserArray = nextUserIds.split(","),
					 nextOrgIdArray = nextOrgIds.split(",");
			String nextUserIdStr = null, nextOrgIdStr = null;
			Long nextUserId = -1L, nextOrgId = -1L;
			UserInfo nextUserInfo = null;
			
			for(int index = 0, len = nextUserArray.length; index < len; index++) {
				nextUserIdStr = nextUserArray[index];
				nextOrgIdStr = nextOrgIdArray[index];
				
				if(StringUtils.isNotBlank(nextUserIdStr)) {
					nextUserId = Long.valueOf(nextUserIdStr);
				}
				if(StringUtils.isNotBlank(nextOrgIdStr)) {
					nextOrgId = Long.valueOf(nextOrgIdStr);
				}
				if(nextUserId > 0 && nextOrgId > 0) {
					nextUserInfo = new UserInfo();
					nextUserInfo.setUserId(nextUserId);
					nextUserInfo.setOrgId(nextOrgId);
					nextUserInfoList.add(nextUserInfo);
				}
			}
		} else if(StringUtils.isNotBlank(nextOrgIds)) {
			String[] nextOrgIdArray = nextOrgIds.split(",");
			String nextOrgIdStr = null;
			Long nextOrgId = -1L;
			UserInfo nextUserInfo = null;
			
			for(int index = 0, len = nextOrgIdArray.length; index < len; index++) {
				nextOrgIdStr = nextOrgIdArray[index];
				
				if(StringUtils.isNotBlank(nextOrgIdStr)) {
					nextOrgId = Long.valueOf(nextOrgIdStr);
				}
				if(nextOrgId > 0) {
					nextUserInfo = new UserInfo();
					nextUserInfo.setOrgId(nextOrgId);
					nextUserInfoList.add(nextUserInfo);
				}
			}
		}
		
		if(nextUserInfoList.size() == 0) {
			if(userInfo != null) {
				nextUserInfoList = new ArrayList<UserInfo>();
				nextUserInfoList.add(userInfo);
			} else {
				throw new IllegalArgumentException("缺少下一环节的可办理人员！");
			}
		}
		
		if(CommonFunctions.isBlank(extraParam, "advice") && CommonFunctions.isNotBlank(extraParam, "evaContent")) {
			extraParam.put("advice", extraParam.get("evaContent"));
		}
		
		if(isCurTaskPaticipant(caseId, userInfo, null)) {
			flag = baseWorkflowService.subWorkflow4Base(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, caseId, nextNodeName, nextUserInfoList, userInfo, extraParam);
		} else {
			String msgWrong = "用户["+ userInfo.getOrgName() +"-"+ userInfo.getPartyName() +"] 不是当前环节[" + this.capCurTaskName(caseId, userInfo) + "] 的办理人员！";
			
			throw new Exception(msgWrong);
		}
		
		if(flag) {
			String curNodeName = null;
			
			if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
				curNodeName = extraParam.get("curNodeName").toString();
			}
			
			this.alterCaseStatus(caseId, curNodeName, nextNodeName, userInfo);
			
			//新增评价信息
			if(CommonFunctions.isNotBlank(extraParam, "evaContent") && CommonFunctions.isNotBlank(extraParam, "evaLevel")) {
				EvaResult evaResult = new EvaResult();
				evaResult.setObjectId(caseId);
				evaResult.setEvaObj(ConstantValue.EVA_OBJ_CASE);
				evaResult.setEvaLevel(extraParam.get("evaLevel").toString());
				evaResult.setEvaContent(extraParam.get("evaContent").toString());
				
				if(userInfo != null) {
					evaResult.setCreateUser(userInfo.getUserId());
					evaResult.setCreatorName(userInfo.getPartyName());
					evaResult.setUpdateUser(userInfo.getUserId());
					evaResult.setUpdaterName(userInfo.getPartyName());
				}
				
				evaResultService.saveEvaResult(evaResult);
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean rejectWorkflow4Case(Long caseId, String rejectToNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		
		if(caseId == null || caseId < 0) {
			throw new IllegalArgumentException("缺少案件id！");
		}
		
		if(CommonFunctions.isBlank(extraParam, "advice") && CommonFunctions.isNotBlank(extraParam, "evaContent")) {
			extraParam.put("advice", extraParam.get("evaContent"));
		}
		
		if(isCurTaskPaticipant(caseId, userInfo, null)) {
			flag = baseWorkflowService.rejectWorkflow4Base(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, caseId, rejectToNodeName, userInfo, extraParam);
			
			if(flag) {
				alterCaseStatus(caseId, REJECT_NODE_NAME, REJECT_NODE_NAME, userInfo);
			}
			
		} else {
			String msgWrong = "用户["+ userInfo.getOrgName() +"-"+ userInfo.getPartyName() +"] 不是当前环节[" + this.capCurTaskName(caseId, userInfo) + "] 的办理人员！";
			
			throw new Exception(msgWrong);
		}
		
		return flag;
	}
	
	@Override
	public Long capInstanceId(Long caseId, UserInfo userInfo) throws Exception {
		Long instanceId = -1L;
		
		if(caseId != null && caseId > 0) {
			instanceId = baseWorkflowService.capInstanceId(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, caseId, userInfo);
		}
		
		return instanceId;
	}
	
	@Override
	public Map<String, Object> findCurTaskData(Long caseId, UserInfo userInfo) throws Exception {
		Map<String, Object> curTaskData = null;
		
		if(caseId != null && caseId > 0) {
			curTaskData = baseWorkflowService.findCurTaskData(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, caseId, userInfo);
		}
		
		return curTaskData;
	}
	
	@Override
	public List<Map<String, Object>> findParticipantsByTaskId(Long taskId) {
		List<Map<String, Object>> participantMapList = null;
		
		if(taskId != null && taskId > 0){
			participantMapList = baseWorkflowService.findParticipantsByTaskId(taskId);
		}
		
		if(participantMapList != null && participantMapList.size() > 0) {
			Long userId = -1L, orgId = -1L;
			OrgSocialInfoBO orgInfo = null;
			UserBO user = null;
			
			for(Map<String, Object> participantMap : participantMapList) {
				if(CommonFunctions.isNotBlank(participantMap, "USER_TYPE")) {
					String userType = participantMap.get("USER_TYPE").toString();
					
					if(IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userType)) {
						if(CommonFunctions.isNotBlank(participantMap, "USER_ID")) {
							userId = Long.valueOf(participantMap.get("USER_ID").toString());
							user = userManageService.getUserInfoByUserId(userId);
							
							if(user != null) {
								participantMap.put("USER_NAME", user.getPartyName());
							}
						}
					}
				}
				
				if(CommonFunctions.isNotBlank(participantMap, "ORG_ID")) {
					orgId = Long.valueOf(participantMap.get("ORG_ID").toString());
					orgInfo = orgSocialInfoService.findByOrgId(orgId);
					
					if(orgInfo != null) {
						participantMap.put("ORG_NAME", orgInfo.getOrgName());
					}
				}
			}
		}
		
		return participantMapList;
	}
	
	@Override
	public boolean isCurTaskPaticipant(Long caseId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		Long taskId = -1L;
		
		Map<String, Object>	curTaskData = baseWorkflowService.findCurTaskData(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, caseId, userInfo);
		
		if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
			taskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
		}
		
		if(taskId > 0) {
			List<Map<String, Object>> participantMapList = this.findParticipantsByTaskId(taskId);
			
			flag = this.isCurTaskPaticipant(participantMapList, userInfo, extraParam);
		}
		
		return flag;
	}
	
	@Override
	public boolean isCurTaskPaticipant(List<Map<String, Object>> participantMapList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		
		if(participantMapList != null && participantMapList.size() > 0) {
			Long userId = -1L, orgId = -1L,
				 curTaskUserId = -1L, curTaskOrgId = -1L;
			String userType = "";//1表示USER_ID为组织ID；3表示USER_ID为用户ID
			
			if(userInfo != null) {
				userId = userInfo.getUserId();
				orgId = userInfo.getOrgId();
			}
			
			for(Map<String, Object> participantMap : participantMapList) {
				curTaskUserId = -1L;
				curTaskOrgId = -1L;
				userType = "";
				
				if(CommonFunctions.isNotBlank(participantMap, "USER_ID")) {
					curTaskUserId = Long.valueOf(participantMap.get("USER_ID").toString());
				}
				if(CommonFunctions.isNotBlank(participantMap, "USER_TYPE")) {
					userType = participantMap.get("USER_TYPE").toString();
				}
				if(CommonFunctions.isNotBlank(participantMap, "ORG_ID")) {
					curTaskOrgId =  Long.valueOf(participantMap.get("ORG_ID").toString());
				}
				
				//当前办理人要相同的用户和相同的组织
				if(IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userType)) {
					flag = userId.equals(curTaskUserId) && orgId.equals(curTaskOrgId);
				} else if(IEventDisposalWorkflowService.ACTOR_TYPE_ORG.equals(userType)) {
					flag = orgId.equals(curTaskUserId);
				}
				
				if(flag) {
					break;
				}
				
			}
		}
		
		return flag;
	}
	
	@Override
	public List<Map<String, Object>> findOperateByNodeId(Integer nodeId) {
		List<OperateBean> operateList = null;
		List<Map<String, Object>> operateMapList = null;
		
		if(nodeId != null && nodeId > 0) {
			operateList = baseWorkflowService.findOperateByNodeId(nodeId);
		}
		
		if(operateList != null && operateList.size() > 0) {
			operateMapList = new ArrayList<Map<String, Object>>();
			Map<String, Object> operateMap = null;
			
			for(OperateBean operate : operateList) {
				operateMap = new HashMap<String, Object>();
				
				operateMap.put("operateEvent", operate.getOperateEvent());
				operateMap.put("anotherName", operate.getAnotherName());
				
				operateMapList.add(operateMap);
			}
		}
		
		return operateMapList;
	}
	
	@Override
	public List<Map<String, Object>> findNextTaskNodes(Long caseId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		List<Node> nextNodeList = null;
		List<Map<String, Object>> nextNodeMapList = null;
		
		if(caseId == null || caseId < 0) {
			throw new IllegalArgumentException("缺少案件id！");
		}
		
		nextNodeList = baseWorkflowService.findNextTaskNodes(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, caseId, userInfo, params);
		
		if(nextNodeList != null) {
			nextNodeMapList = new ArrayList<Map<String, Object>>();
			Map<String, Object> nextNodeMap = null;
			
			for(Node nextNode : nextNodeList) {
				nextNodeMap = new HashMap<String, Object>();
				
				nextNodeMap.put("nodeId", nextNode.getNodeId());
				nextNodeMap.put("nodeName", nextNode.getNodeName());
				nextNodeMap.put("nodeNameZH", nextNode.getNodeNameZH());
				nextNodeMap.put("nodeCode", nextNode.getNodeCode());
				nextNodeMap.put("nodeType", nextNode.getNodeType());
				nextNodeMap.put("transitionCode", nextNode.getTransitionCode());
				nextNodeMap.put("dynamicSelect", nextNode.getDynamicSelect());
				
				nextNodeMapList.add(nextNodeMap);
			}
		}
		
		return nextNodeMapList;
	}
	
	/**
	 * 判断用户是否可以启动流程
	 * @param userInfo
	 * @throws Exception
	 */
	private boolean isUserAbleToStart(UserInfo userInfo) throws Exception {
		boolean flag = false;
		
		if(userInfo != null) {
			Long orgId = userInfo.getOrgId();
			
			if(orgId != null && orgId > 0) {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
				if(orgInfo != null) {
					String chiefLevel = orgInfo.getChiefLevel(),
						   orgType = orgInfo.getOrgType(),
						   msgWrong = null,
						   ORG_TYPE_DEPARTMENT = String.valueOf(ConstantValue.ORG_TYPE_DEPT);	//组织类型 部门
					
					if(ORG_TYPE_DEPARTMENT.equals(orgType)) {
						if(!ConstantValue.STREET_ORG_LEVEL.equals(chiefLevel)) {
							msgWrong = "该用户的组织层级不是街道级！";
						}
					} else {
						msgWrong = "该用户的组织类型不是部门！";
					}
					
					if(StringUtils.isNotBlank(msgWrong)) {
						throw new Exception("该用户不能提交流程！" + msgWrong);
					} else {
						flag = true;
					}
				}
			}
		}
		
		return flag;
	}
	
	/**
	 * 更新案件状态
	 * @param caseId		案件id
	 * @param curNodeName	当前环节名称
	 * @param nextNodeName	下一环节名称
	 * @param userInfo		用户信息
	 * @return
	 * @throws Exception
	 */
	private boolean alterCaseStatus(Long caseId, String curNodeName, String nextNodeName, UserInfo userInfo) throws Exception {
		boolean result = false;
		Map<String, Object> statusMap = new HashMap<String, Object>();
		
		if(caseId != null && caseId > 0) {
			statusMap.put("caseId", caseId);
		}
		
		if(StringUtils.isNotBlank(curNodeName)) {
			statusMap.put("curNodeName", curNodeName);
		}
		
		if(StringUtils.isNotBlank(nextNodeName)) {
			statusMap.put("nextNodeName", nextNodeName);
		}
		
		if(userInfo != null) {
			statusMap.put("updaterId", userInfo.getUserId());
			statusMap.put("updaterOrgId", userInfo.getOrgId());
		}
		
		if(!statusMap.isEmpty()) {
			result = statusDecisionMaking.makeDecision(statusMap);
		}
		
		return result;
	}
	
	/**
	 * 获取当前环节中文名称
	 * @param caseId	案件id
	 * @param userInfo	用户信息
	 * @return
	 * @throws Exception
	 */
	private String capCurTaskName(Long caseId, UserInfo userInfo) throws Exception {
		Map<String, Object> curTaskData = this.findCurTaskData(caseId, userInfo);
		String curTaskName = null;
		
		if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
			curTaskName = curTaskData.get("WF_ACTIVITY_NAME_").toString();
		}
		
		if(curTaskName == null) {
			throw new Exception("任务已完结！");
		}
		
		return curTaskName;
	}
}
