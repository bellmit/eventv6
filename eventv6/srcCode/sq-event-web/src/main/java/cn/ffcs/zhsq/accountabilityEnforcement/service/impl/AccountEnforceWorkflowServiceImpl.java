package cn.ffcs.zhsq.accountabilityEnforcement.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.NodeTransition;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.accountabilityEnforcement.service.IAccountEnforceWorkflowService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 新疆纪委扶贫问题 工作流相关接口实现
 * @author zhangls
 *
 */
@Service(value = "accountEnforceWorkflowService")
public class AccountEnforceWorkflowServiceImpl implements
		IAccountEnforceWorkflowService {

	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Resource(name = "workflowDecisionMaking4AccountEnforceService") 
	private IWorkflowDecisionMakingService<String> workflowDecisionMakingService;
	
	//纪委扶贫问责流程图名称
	private String ACCOUNT_ENFORCE_WORKFLOW_NAME = "新疆纪委扶贫问责流程图",			
	//纪委扶贫问责流程类型
				   ACCOUNT_ENFORCE_WFTYPE_ID = "poverty_alleviation_account",	
	//决策节点环节类型
				   DESCISION_NODE_TYPE = "2",
	//决策节点决策类
				   DECISION_MAKING_SERVICE = "workflowDecisionMaking4AccountEnforceService";
	
	@Override
	public boolean startWorkflow4AccountEn(Long probId, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		
		if(probId != null && probId > 0) {
			Long instanceId = -1L;
			Map<String, Object> startMap = new HashMap<String, Object>();
			
			startMap.put("decisionService", DECISION_MAKING_SERVICE);
			
			instanceId = baseWorkflowService.startWorkflow4Base(ACCOUNT_ENFORCE_WORKFLOW_NAME, ACCOUNT_ENFORCE_WFTYPE_ID, probId, userInfo, null);
			
			flag = instanceId != null && instanceId > 0;
			
			if(flag) {
				//自动扭转过决策节点
				String nextNodeName = null;
				
				List<Map<String, Object>> nextNodes = findNextTaskNodes(probId, userInfo, null);
				if(nextNodes != null && nextNodes.size() > 0) {
					Map<String, Object> nextNodeMap = nextNodes.get(0);
					
					if(CommonFunctions.isNotBlank(nextNodeMap, "nodeName")) {
						nextNodeName = nextNodeMap.get("nodeName").toString();
					}
				}
				
				if(StringUtils.isNotBlank(nextNodeName)) {
					boolean isClose = false;
					Map<String, Object> subMap = new HashMap<String, Object>();
					
					if(CommonFunctions.isNotBlank(extraParam, "isClose")) {
						isClose = Boolean.valueOf(extraParam.get("isClose").toString());
					}

					subMap.putAll(extraParam);
					
					if(isClose && CommonFunctions.isNotBlank(subMap, "advice")) {
						subMap.remove("advice");
					}
					
					flag = this.subWorkflow4AccountEn(probId, nextNodeName, null, userInfo, subMap);
					
					if(flag && isClose) {
						flag = endWorkflow4AccountEn(instanceId, userInfo, extraParam);
					}
				}
			}
		}
		
		return flag;
	}

	@Override
	public boolean subWorkflow4AccountEn(Long probId, String nextNodeName,
			List<UserInfo> nextUserInfoList, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		String nextUserIds = null, nextOrgIds = null;
		
		if(probId == null || probId < 0) {
			throw new IllegalArgumentException("缺少问题id！");
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
		
		if(isCurTaskPaticipant(probId, userInfo, null)) {
			flag = baseWorkflowService.subWorkflow4Base(ACCOUNT_ENFORCE_WORKFLOW_NAME, ACCOUNT_ENFORCE_WFTYPE_ID, probId, nextNodeName, nextUserInfoList, userInfo, extraParam);
		} else {
			String msgWrong = "用户["+ userInfo.getOrgName() +"-"+ userInfo.getPartyName() +"] 不是当前环节[" + this.capCurTaskName(probId, userInfo) + "] 的办理人员！";
			
			throw new Exception(msgWrong);
		}
		
		return flag;
	}

	@Override
	public boolean rejectWorkflow4AccountEn(Long probId,
			String rejectToNodeName, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		
		if(probId == null || probId < 0) {
			throw new IllegalArgumentException("缺少问题id！");
		}
		
		if(isCurTaskPaticipant(probId, userInfo, null)) {
			flag = baseWorkflowService.rejectWorkflow4Base(ACCOUNT_ENFORCE_WORKFLOW_NAME, ACCOUNT_ENFORCE_WFTYPE_ID, probId, rejectToNodeName, userInfo, extraParam);
		} else {
			String msgWrong = "用户["+ userInfo.getOrgName() +"-"+ userInfo.getPartyName() +"] 不是当前环节[" + this.capCurTaskName(probId, userInfo) + "] 的办理人员！";
			
			throw new Exception(msgWrong);
		}
		
		return flag;
	}

	@Override
	public boolean endWorkflow4AccountEn(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		
		if(instanceId == null || instanceId < 0) {
			if(CommonFunctions.isNotBlank(extraParam, "probId")) {
				Long probId = Long.valueOf(extraParam.get("probId").toString());
				
				if(probId > 0) {
					instanceId = this.capInstanceId(probId, userInfo);
				}
			}
		}
		
		if(instanceId != null && instanceId > 0) {
			flag = baseWorkflowService.endWorkflow4Base(instanceId, userInfo, extraParam);
		}
		
		return flag;
	}
	
	@Override
	public Long capInstanceId(Long probId, UserInfo userInfo) throws Exception {
		Long instanceId = -1L;
		
		if(probId != null && probId > 0) {
			instanceId = baseWorkflowService.capInstanceId(ACCOUNT_ENFORCE_WORKFLOW_NAME, ACCOUNT_ENFORCE_WFTYPE_ID, probId, userInfo);
		}
		
		return instanceId;
	}

	@Override
	public Map<String, Object> findCurTaskData(Long probId, UserInfo userInfo)
			throws Exception {
		Map<String, Object> curTaskData = null;
		
		if(probId != null && probId > 0) {
			curTaskData = baseWorkflowService.findCurTaskData(ACCOUNT_ENFORCE_WORKFLOW_NAME, ACCOUNT_ENFORCE_WFTYPE_ID, probId, userInfo);
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
	public boolean isCurTaskPaticipant(Long probId, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		Long taskId = -1L;
		
		Map<String, Object>	curTaskData = baseWorkflowService.findCurTaskData(ACCOUNT_ENFORCE_WORKFLOW_NAME, ACCOUNT_ENFORCE_WFTYPE_ID, probId, userInfo);
		
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
	public boolean isCurTaskPaticipant(
			List<Map<String, Object>> participantMapList, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
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
	public List<Map<String, Object>> findNextTaskNodes(Long probId,
			UserInfo userInfo, Map<String, Object> params) throws Exception {
		List<Node> nextNodeList = null, nextNodesList = null;
		List<Map<String, Object>> nextNodeMapList = null;
		
		if(probId == null || probId < 0) {
			throw new IllegalArgumentException("缺少问题id！");
		}
		
		nextNodeList = baseWorkflowService.findNextTaskNodes(ACCOUNT_ENFORCE_WORKFLOW_NAME, ACCOUNT_ENFORCE_WFTYPE_ID, probId, userInfo, params);
		
		if(nextNodeList != null) {
			Map<String, Object> nextNodeMap = null;
			Node nextTaskNode = null;
			nextNodesList = new ArrayList<Node>();
			nextNodeMapList = new ArrayList<Map<String, Object>>();
			
			for(Node nextNode : nextNodeList) {
				nextTaskNode = nextNode;
				
				if(DESCISION_NODE_TYPE.equals(nextNode.getNodeType())) {//下一节点为决策节点
					String nextNodeCode = "";
					
					if(params == null) {
						params = new HashMap<String, Object>();
					}
					
					params.put("curNodeCode", nextNode.getNodeName());
					
					if(CommonFunctions.isBlank(params, "curOrgId")) {
						params.put("curOrgId", userInfo.getOrgId());
					}
					if(CommonFunctions.isBlank(params, "curUserId")) {
						params.put("curUserId", userInfo.getUserId());
					}
					if(CommonFunctions.isBlank(params, "instanceId")) {
						params.put("instanceId", this.capInstanceId(probId, userInfo));
					}
					
					nextNodeCode = workflowDecisionMakingService.makeDecision(params);
					
					if(StringUtils.isNotBlank(nextNodeCode)) {
						nextTaskNode = baseWorkflowService.capNodeInfo(ACCOUNT_ENFORCE_WORKFLOW_NAME, ACCOUNT_ENFORCE_WFTYPE_ID, probId, nextNodeCode, userInfo);
						
						if(nextTaskNode != null) {
							NodeTransition nodeTransition = baseWorkflowService.capNodeTransition(nextNode.getNodeId(), nextTaskNode.getNodeId());
							if(nodeTransition != null) {
								nextTaskNode.setTransitionCode(nextNode.getFromCode() + nodeTransition.getType() + nodeTransition.getLevel() + nextTaskNode.getToCode());
							} else {
								nextTaskNode.setTransitionCode(nextNode.getFromCode() + "__" + nextTaskNode.getToCode());
							}
						}
					}
				}
				
				nextNodesList.add(nextTaskNode);
			}
			
			for(Node nextNode : nextNodesList) {
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
	 * 获取当前环节中文名称
	 * @param probId	问题id
	 * @param userInfo	用户信息
	 * @return
	 * @throws Exception
	 */
	private String capCurTaskName(Long probId, UserInfo userInfo) throws Exception {
		Map<String, Object> curTaskData = this.findCurTaskData(probId, userInfo);
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
