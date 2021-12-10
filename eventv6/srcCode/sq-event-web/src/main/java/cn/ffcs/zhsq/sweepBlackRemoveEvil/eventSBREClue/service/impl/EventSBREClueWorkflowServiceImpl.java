package cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREClue.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREClue.service.IEventSBREClueWorkflowService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

public class EventSBREClueWorkflowServiceImpl implements IEventSBREClueWorkflowService {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Resource(name="eventStatusDecisionMaking4SBREClueService")  
	private IWorkflowDecisionMakingService<Boolean> statusDecisionMaking;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IWorkflow4BaseService workflow4BaseService;
	
	private final String CLUE_WORKFLOW_NAME = "扫黑除恶_线索管理流程",//线索流程图名称
	  		 			 CLUE_WFTYPE_ID = "sweep_black_remove_evil_clue",//线索流程类型
	  		 			 START_NODE_NAME = "start";//流程开始节点名称
	  		 
	@Override
	public boolean startWorkflow4Clue(Long clueId, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		
		if(isUserAbleToStart(userInfo)) {
			flag = workflow4BaseService.startWorkflow4Base(clueId, CLUE_WORKFLOW_NAME, CLUE_WFTYPE_ID, userInfo, extraParam);
			
			if(flag) {
				alterClueStatus(clueId, START_NODE_NAME, START_NODE_NAME, userInfo);//为了防止提交失败而导致状态仍保留在草稿状态
			}
		}
		
		return flag;
	}

	@Override
	public boolean subWorkflow4Clue(Long clueId, String nextNodeName,
			List<UserInfo> nextUserInfoList, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		boolean flag = workflow4BaseService.subWorkflow4Base(clueId, CLUE_WORKFLOW_NAME, CLUE_WFTYPE_ID, nextNodeName, nextUserInfoList, userInfo, extraParam);
		
		if(flag) {
			String curNodeName = null;
			
			if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
				curNodeName = extraParam.get("curNodeName").toString();
			}
			
			this.alterClueStatus(clueId, curNodeName, nextNodeName, userInfo);
		}
		
		return flag;
	}

	@Override
	public boolean rejectWorkflow4Clue(Long clueId, String rejectToNodeName,
			UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		
		if(clueId != null && clueId > 0) {
			flag = workflow4BaseService.rejectWorkflow4Base(clueId, CLUE_WORKFLOW_NAME, CLUE_WFTYPE_ID, rejectToNodeName, userInfo, extraParam);
			
			if(flag) {
				String curNodeName = this.capCurNodeName(clueId, userInfo);
				
				if(StringUtils.isNotBlank(curNodeName)) {
					alterClueStatus(clueId, null, curNodeName, userInfo);
				}
			}
		}
		
		return flag;
	}

	@Override
	public Long capInstanceId(Long clueId, UserInfo userInfo) throws Exception {
		Long instanceId = -1L;
		
		if(clueId != null && clueId > 0) {
			Map<String, Object> resultMap = workflow4BaseService.capInstance4Base(clueId, CLUE_WORKFLOW_NAME, CLUE_WFTYPE_ID, userInfo);
			
			if(CommonFunctions.isNotBlank(resultMap, "instanceId")) {
				instanceId = Long.valueOf(resultMap.get("instanceId").toString());
			}
		}
		
		return instanceId;
	}

	@Override
	public Map<String, Object> findCurTaskData(Long clueId, UserInfo userInfo)
			throws Exception {
		Map<String, Object> curTaskData = null;
		
		if(clueId != null && clueId > 0) {
			curTaskData = workflow4BaseService.findCurTaskData4Base(clueId, CLUE_WORKFLOW_NAME, CLUE_WFTYPE_ID, userInfo);
		}
		
		return curTaskData;
	}

	@Override
	public List<Map<String, Object>> findParticipantsByTaskId(Long taskId) {
		List<Map<String, Object>> participantMapList = null;
		
		if(taskId != null && taskId > 0){
			participantMapList = workflow4BaseService.findParticipantsByTaskId(taskId);
		}
		
		return participantMapList;
	}

	@Override
	public boolean isCurTaskPaticipant(Long clueId, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		boolean flag  = workflow4BaseService.isCurTaskPaticipant4Base(clueId, CLUE_WORKFLOW_NAME, CLUE_WFTYPE_ID, userInfo, extraParam);
		
		return flag;
	}

	@Override
	public boolean isCurTaskPaticipant(
			List<Map<String, Object>> participantMapList, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		boolean flag = workflow4BaseService.isCurTaskPaticipant4Base(participantMapList, userInfo, extraParam);
		
		return flag;
	}

	@Override
	public List<Map<String, Object>> findOperateByNodeId(Integer nodeId) {
		List<Map<String, Object>> operateMapList = null;
		
		if(nodeId != null && nodeId > 0) {
			operateMapList = workflow4BaseService.findOperateByNodeId(nodeId);
		}
		
		return operateMapList;
	}

	@Override
	public List<Map<String, Object>> findNextTaskNodes(Long clueId,
			UserInfo userInfo, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> nextNodeMapList = null;
		
		if(clueId != null && clueId > 0) {
			nextNodeMapList = workflow4BaseService.findNextTaskNodes4Base(clueId, CLUE_WORKFLOW_NAME, CLUE_WFTYPE_ID, userInfo, params);
		}
		
		return nextNodeMapList;
	}
	
	@Override
	public boolean isHandledPerson(Long clueId, Long instanceId, Long userId, Long orgId) {
		boolean flag = false;
		
		//用户id及组织id均有效时，才进行该人员是否为线索任务经办人员的判断
		if(userId != null && userId > 0 && orgId != null && orgId > 0) {
			List<Map<String, Object>> taskMapList = null;
			
			if((instanceId == null || instanceId < 0) && (clueId != null && clueId > 0)) {
				UserInfo userInfo = new UserInfo();
				
				userInfo.setUserId(userId);
				userInfo.setOrgId(orgId);
				
				try {
					instanceId = this.capInstanceId(clueId, userInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			taskMapList = workflow4BaseService.capHandledTaskInfoMap(instanceId, IWorkflow4BaseService.SQL_ORDER_DESC, null);
			
			if(taskMapList != null) {
				String transactorId = null, transactorOrgId = null;
				
				for(Map<String, Object> taskMap : taskMapList) {
					if(CommonFunctions.isNotBlank(taskMap, "TRANSACTOR_ID")) {
						transactorId = taskMap.get("TRANSACTOR_ID").toString();
					}
					if(CommonFunctions.isNotBlank(taskMap, "ORG_ID")) {
						transactorOrgId = taskMap.get("ORG_ID").toString();
					}
					
					if(userId.toString().equals(transactorId) && orgId.toString().equals(transactorOrgId)) {
						flag = true; break;
					}
				}
			}
		}
		
		return flag;
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
						   ORG_TYPE_UNIT = String.valueOf(ConstantValue.ORG_TYPE_UNIT),			//组织类型 单位
						   ORG_TYPE_DEPARTMENT = String.valueOf(ConstantValue.ORG_TYPE_DEPT);	//组织类型 部门
					
					if(ORG_TYPE_UNIT.equals(orgType)) {
						msgWrong = "该用户的组织类型不是部门！";
					} else if(ORG_TYPE_DEPARTMENT.equals(orgType)) {
						if(ConstantValue.STREET_ORG_LEVEL.compareTo(chiefLevel) < 0) {
							msgWrong = "该用户的组织层级需要为街道级及其以上！";
						}
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
	 * 更新线索状态
	 * @param clueId		线索id
	 * @param curNodeName	当前环节名称
	 * @param nextNodeName	下一环节名称
	 * @param userInfo		用户信息
	 * @return
	 * @throws Exception
	 */
	private boolean alterClueStatus(Long clueId, String curNodeName, String nextNodeName, UserInfo userInfo) throws Exception {
		boolean result = false;
		Map<String, Object> statusMap = new HashMap<String, Object>();
		
		if(clueId != null && clueId > 0) {
			statusMap.put("clueId", clueId);
		}
		
		if(StringUtils.isNotBlank(curNodeName)) {
			statusMap.put("curNodeName", curNodeName);
		}
		
		if(StringUtils.isNotBlank(nextNodeName)) {
			statusMap.put("nextNodeName", nextNodeName);
		}
		
		if(userInfo != null) {
			statusMap.put("updaterId", userInfo.getUserId());
		}
		
		if(!statusMap.isEmpty()) {
			result = statusDecisionMaking.makeDecision(statusMap);
		}
		
		return result;
	}
	
	/**
	 * 获取当前环节名称
	 * @param clueId	线索id
	 * @param userInfo	用户信息
	 * @return
	 * @throws Exception
	 */
	private String capCurNodeName(Long clueId, UserInfo userInfo) throws Exception {
		Map<String, Object> curTaskData = this.findCurTaskData(clueId, userInfo);
		String curNodeName = null;
		
		if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
			curNodeName = curTaskData.get("NODE_NAME").toString();
		}
		
		return curNodeName;
	}
	
}
