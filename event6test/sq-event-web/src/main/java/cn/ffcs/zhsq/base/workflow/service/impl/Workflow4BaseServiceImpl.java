package cn.ffcs.zhsq.base.workflow.service.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.uam.service.WorkFlowService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.data.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 工作流基本接口实现
 * @author zhangls
 *
 */
@Service(value = "workflow4BaseService")
public class Workflow4BaseServiceImpl implements IWorkflow4BaseService {
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private WorkFlowService uamWorkFlowService;
	  		 
	@Override
	public boolean startWorkflow4Base(Long formId, String wfName, String wfType,
			UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		
		if(isParamValid(formId, wfName, wfType)) {
			Long instanceId = null;
			extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
			
			instanceId = baseWorkflowService.startWorkflow4Base(wfName, wfType, formId, userInfo, extraParam);
			
			if(instanceId != null && instanceId > 0) {
				flag = true;
				extraParam.put("instanceId", instanceId);
			}
		}
		
		return flag;
	}

	@Override
	public boolean subWorkflow4Base(Long formId, String wfName, String wfType, String nextNodeName,
			List<UserInfo> nextUserInfoList, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		boolean flag = false, isDuplicateRemove = true;
		String nextUserIds = null, nextOrgIds = null;
		
		if(CommonFunctions.isBlank(extraParam, "instanceId")) {
			isParamValid(formId, wfName, wfType);
		}
		
		if(StringUtils.isBlank(nextNodeName)) {
			throw new IllegalArgumentException("缺少可办理的下一环节！");
		}
		if(nextUserInfoList == null || nextUserInfoList.size() == 0) {
			nextUserInfoList = new ArrayList<UserInfo>();
			
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
				Set<String> nextUserSet = new HashSet<String>();
				StringBuffer userSetKey = new StringBuffer("");
				
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
						userSetKey = new StringBuffer("");
						userSetKey.append(nextUserId).append("-").append(nextOrgId);
						
						if(!nextUserSet.contains(userSetKey.toString())) {//用户判重
							nextUserSet.add(userSetKey.toString());
							
							nextUserInfo = new UserInfo();
							nextUserInfo.setUserId(nextUserId);
							nextUserInfo.setOrgId(nextOrgId);
							nextUserInfoList.add(nextUserInfo);
						}
					}
				}
			} else if(StringUtils.isNotBlank(nextOrgIds)) {
				String[] nextOrgIdArray = nextOrgIds.split(",");
				String nextOrgIdStr = null;
				Long nextOrgId = -1L;
				UserInfo nextUserInfo = null;
				Set<Long> nextOrgSet = new HashSet<Long>();
				
				for(int index = 0, len = nextOrgIdArray.length; index < len; index++) {
					nextOrgIdStr = nextOrgIdArray[index];
					
					if(StringUtils.isNotBlank(nextOrgIdStr)) {
						nextOrgId = Long.valueOf(nextOrgIdStr);
					}
					if(nextOrgId > 0) {
						if(!nextOrgSet.contains(nextOrgId)) {
							nextOrgSet.add(nextOrgId);
							
							nextUserInfo = new UserInfo();
							nextUserInfo.setOrgId(nextOrgId);
							nextUserInfoList.add(nextUserInfo);
						}
					}
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
		
		if(CommonFunctions.isNotBlank(extraParam, "isDuplicateRemove")) {
			isDuplicateRemove = Boolean.valueOf(extraParam.get("isDuplicateRemove").toString());
		}
		
		if(isDuplicateRemove) {
			String userType = IEventDisposalWorkflowService.ACTOR_TYPE_USER;
			List<UserInfo> nextUserInfoListTmp = new ArrayList<UserInfo>();
			Set<String> userIdSet = new HashSet<String>();
			StringBuffer userIdBuffer = null;
			boolean isUserOperate = true;
			
			if(CommonFunctions.isNotBlank(extraParam, "userType")) {
				userType = extraParam.get("userType").toString();
			}
			
			isUserOperate = IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userType);
			
			for(UserInfo nextUserInfo : nextUserInfoList) {
				userIdBuffer = new StringBuffer("");
				
				userIdBuffer.append(nextUserInfo.getUserId());
				
				if(isUserOperate) {
					userIdBuffer.append("-").append(nextUserInfo.getOrgId());
				}
				
				if(!userIdSet.contains(userIdBuffer.toString())) {
					userIdSet.add(userIdBuffer.toString());
					
					nextUserInfoListTmp.add(nextUserInfo);
				}
			}
			
			nextUserInfoList = nextUserInfoListTmp;
		}
		
		if(isCurTaskPaticipant4Base(formId, wfName, wfType, userInfo, extraParam)) {
			if(CommonFunctions.isNotBlank(extraParam, "advice") && CommonFunctions.isNotBlank(extraParam, "assistOrgNames")) {
				String assistOrgLabel = "办理单位",
					   advice = extraParam.get("advice").toString();
				
				if(CommonFunctions.isNotBlank(extraParam, "assistOrgLabel")) {
					assistOrgLabel = extraParam.get("assistOrgLabel").toString();
				}
				
				advice += " " + assistOrgLabel + "：" + extraParam.get("assistOrgNames").toString();
				
				extraParam.put("advice", advice);
			}
			
			flag = baseWorkflowService.subWorkflow4Base(wfName, wfType, formId, nextNodeName, nextUserInfoList, userInfo, extraParam);
		} else {
			StringBuffer msgWrongBuffer = new StringBuffer("");
			Long instanceId = null;
			
			msgWrongBuffer.append("用户["+ userInfo.getOrgName() +"-"+ userInfo.getPartyName() +"] 不是当前环节[");
			if(CommonFunctions.isNotBlank(extraParam, "instanceId")) {
				try {
					instanceId = Long.valueOf(extraParam.get("instanceId").toString());
				} catch(NumberFormatException e) {}
			}
			
			if(instanceId != null && instanceId > 0) {
				msgWrongBuffer.append(this.capCurTaskName(instanceId));
			} else {
				msgWrongBuffer.append(this.capCurTaskName(formId, wfName, wfType, userInfo));
			}
			
			msgWrongBuffer.append("] 的办理人员！");
			
			throw new Exception(msgWrongBuffer.toString());
		}
		
		return flag;
	}

	@Override
	public boolean rejectWorkflow4Base(Long formId, String wfName, String wfType, String rejectToNodeName,
			UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		
		if(CommonFunctions.isBlank(extraParam, "instanceId")) {
			isParamValid(formId, wfName, wfType);
		}
		
		if(isCurTaskPaticipant4Base(formId, wfName, wfType, userInfo, extraParam)) {
			flag = baseWorkflowService.rejectWorkflow4Base(wfName, wfType, formId, rejectToNodeName, userInfo, extraParam);
		} else {
			StringBuffer msgWrongBuffer = new StringBuffer("");
			Long instanceId = null;
			
			msgWrongBuffer.append("用户["+ userInfo.getOrgName() +"-"+ userInfo.getPartyName() +"] 不是当前环节[");
			if(CommonFunctions.isNotBlank(extraParam, "instanceId")) {
				try {
					instanceId = Long.valueOf(extraParam.get("instanceId").toString());
				} catch(NumberFormatException e) {}
			}
			
			if(instanceId != null && instanceId > 0) {
				msgWrongBuffer.append(this.capCurTaskName(instanceId));
			} else {
				msgWrongBuffer.append(this.capCurTaskName(formId, wfName, wfType, userInfo));
			}
			
			msgWrongBuffer.append("] 的办理人员！");
			
			throw new Exception(msgWrongBuffer.toString());
		}
		
		return flag;
	}

	@Override
	public Map<String, Object> capInstance4Base(Long formId, String wfName, String wfType, UserInfo userInfo) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(isParamValid(formId, wfName, wfType)) {
			Long instanceId = baseWorkflowService.capInstanceId(wfName, wfType, formId, userInfo);
			
			if(instanceId != null && instanceId > 0) {
				resultMap.put("instanceId", instanceId);
			}
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> findCurTaskData4Base(Long formId, String wfName, String wfType, UserInfo userInfo)
			throws Exception {
		Map<String, Object> curTaskData = null;
		
		if(isParamValid(formId, wfName, wfType)) {
			curTaskData = baseWorkflowService.findCurTaskData(wfName, wfType, formId, userInfo);
		}
		
		return curTaskData;
	}
	
	@Override
	public Map<String, Object> findCurTaskData4Base(Long instanceId) throws Exception {
		
		return findCurTaskData4Base(instanceId, null);
	}
	
	@Override
	public Map<String, Object> findCurTaskData4Base(Long instanceId, Map<String, Object> params) throws Exception {
		Map<String, Object> curTaskData = null;
		
		if(instanceId != null && instanceId > 0) {
			curTaskData = baseWorkflowService.findCurTaskData(instanceId);
		} else {
			throw new IllegalArgumentException("缺少有效的流程实例id！");
		}
		
		boolean isCapCurUserInfo = false;
		
		if(CommonFunctions.isNotBlank(params, "isCapCurUserInfo")) {
			isCapCurUserInfo = Boolean.valueOf(params.get("isCapCurUserInfo").toString());
		}
		
		if(isCapCurUserInfo) {
			List<UserInfo> curUserInfoList = null;
			List<Map<String, Object>> curUserInfoMapList = null;
			
			if(CommonFunctions.isNotBlank(curTaskData, "WF_USERID")) {
				curUserInfoList = uamWorkFlowService.findByUserIds(curTaskData.get("WF_USERID").toString());
			}
			
			if(curUserInfoList != null) {
				Map<String, Object> curUserInfoMap = null;
				curUserInfoMapList = new ArrayList<Map<String, Object>>();
				
				for(UserInfo curUserInfo : curUserInfoList) {
					curUserInfoMap = new HashMap<String, Object>();
					
					curUserInfoMap.put("userId", curUserInfo.getUserId());
					curUserInfoMap.put("userName", curUserInfo.getUserName());
					curUserInfoMap.put("partyName", curUserInfo.getPartyName());
					curUserInfoMap.put("verifyMobile", curUserInfo.getVerifyMobile());
					
					curUserInfoMapList.add(curUserInfoMap);
				}
				
				curTaskData.put("curUserInfoMapList", curUserInfoMapList);
			}
		}
		
		if(CommonFunctions.isNotBlank(curTaskData, "DUEDATE_")) {
			Date curTaskDueDate = (Date) curTaskData.get("DUEDATE_"),
				 currentDate = new Date();
			String isTimeOut = "0";
			
			if(currentDate.compareTo(curTaskDueDate) > 0) {
				isTimeOut = "1";
			}
			
			curTaskData.put("ISTIMEOUT", isTimeOut);
			curTaskData.put("DUEDATESTR_", DateUtils.formatDate(curTaskDueDate, DateUtils.PATTERN_24TIME));
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
	public boolean isCurTaskPaticipant4Base(Long formId, String wfName, String wfType, 
			UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		Long instanceId = null, taskId = -1L;
		Map<String, Object>	curTaskData = null;
		
		if(CommonFunctions.isNotBlank(extraParam, "instanceId")) {
			instanceId = Long.valueOf(extraParam.get("instanceId").toString());
		}
		
		if(instanceId != null && instanceId > 0) {
			curTaskData = baseWorkflowService.findCurTaskData(instanceId);
		} else {
			isParamValid(formId, wfName, wfType);
			curTaskData = baseWorkflowService.findCurTaskData(wfName, wfType, formId, userInfo);
		}
		
		if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
			taskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
		}
		
		if(taskId > 0) {
			List<Map<String, Object>> participantMapList = this.findParticipantsByTaskId(taskId);
			
			flag = this.isCurTaskPaticipant4Base(participantMapList, userInfo, extraParam);
		}
		
		return flag;
	}

	@Override
	public boolean isCurTaskPaticipant4Base(
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

		//因现网出现nodeId < 0 情况，暂时去除判断条件 nodeId > 0
		if(nodeId != null /*&& nodeId > 0*/) {
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
	public List<Map<String, Object>> findNextTaskNodes4Base(Long formId, String wfName, String wfType,
			UserInfo userInfo, Map<String, Object> params) throws Exception {
		List<Node> nextNodeList = null;
		List<Map<String, Object>> nextNodeMapList = null;
		
		if(CommonFunctions.isBlank(params, "instanceId")) {
			isParamValid(formId, wfName, wfType);
		}
		
		nextNodeList = baseWorkflowService.findNextTaskNodes(wfName, wfType, formId, userInfo, params);
		
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
				nextNodeMap.put("fromTo", nextNode.getFromTo());
				nextNodeMap.put("fromCode", nextNode.getFromCode());
				nextNodeMap.put("toCode", nextNode.getToCode());
				nextNodeMap.put("level", nextNode.getLevel());
				nextNodeMap.put("professionCode", nextNode.getProfessionCode());
				nextNodeMap.put("dynamicSelect", nextNode.getDynamicSelect());
				
				nextNodeMapList.add(nextNodeMap);
			}
		}
		
		return nextNodeMapList;
	}
	
	@Override
	public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params) {
		List<Map<String, Object>> taskMapList = null;
		
		if(instanceId != null && instanceId > 0) {
			taskMapList = baseWorkflowService.capHandledTaskInfoMap(instanceId, order, params);
		}
		
		return taskMapList;
	}
	
	@Override
	public boolean addUrgeOrRemind(Long instanceId, Map<String, Object> params, UserInfo userInfo) throws Exception {
		Map<String, Object> curTaskMap = null, remindParams = new HashMap<String, Object>();
		boolean result = false;
		Long taskId = null;
		String category = null,
			   curUserIds = null,
			   CATEGORY_URGE = "1",		//催办
			   CATEGORY_REMIND = "2";	//督办
		
		if(instanceId == null || instanceId <= 0) {
			throw new IllegalArgumentException("缺少有效的流程实例id！");
		}
		
		if(CommonFunctions.isBlank(params, "category")) {
			throw new IllegalArgumentException("缺少记录类型属性【category】！");
		}
		
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户新！");
		}
		
		category = params.get("category").toString();
		
		if(!CATEGORY_URGE.equals(category) && !CATEGORY_REMIND.equals(category)) {
			throw new ZhsqEventException("使用了不支持的记录类型【category】：" + category + "	！");
		}
		
		curTaskMap = baseWorkflowService.findCurTaskData(instanceId);
		
		if(CommonFunctions.isNotBlank(curTaskMap, "WF_DBID_")) {
			taskId = Long.valueOf(curTaskMap.get("WF_DBID_").toString());
		}
			
		if(taskId != null && taskId > 0) {
			String remindUserPartyName = userInfo.getPartyName();
			
			remindParams.put("curTaskId", taskId);
			remindParams.put("instanceId", instanceId);
			remindParams.put("category", category);
			remindParams.put("remindUserId", userInfo.getUserId());
			
			if(CommonFunctions.isNotBlank(params, "remindUserPartyName")) {
				remindUserPartyName = params.get("remindUserPartyName").toString();
			}
			
			if(CommonFunctions.isNotBlank(params, "remindRemark")) {
				remindParams.put("remindRemark", params.get("remindRemark").toString());
			}
			
			if(CommonFunctions.isNotBlank(curTaskMap, "WF_USERID_ALL")) {
				curUserIds = curTaskMap.get("WF_USERID_ALL").toString();
				
				remindParams.put("remindedUserId", curUserIds);
			}
			
			if(CommonFunctions.isNotBlank(curTaskMap, "WF_USERNAME_ALL")) {
				remindParams.put("remindedUserPartyName", curTaskMap.get("WF_USERNAME_ALL"));
			}
			
			remindParams.put("remindUserPartyName", remindUserPartyName);
			
			result = baseWorkflowService.addRemind(remindParams);
		}
		
		if(result) {
			boolean isSendSmsContent = true;
			
			if(CommonFunctions.isNotBlank(params, "isSendSmsContent")) {
				isSendSmsContent = Boolean.valueOf(params.get("isSendSmsContent").toString());
			}
			
			if(isSendSmsContent && CommonFunctions.isNotBlank(params, "smsContent")) {
				//发送短信
				String otherMobileNums = null;//额外接收短信的号码
				
				if(CommonFunctions.isNotBlank(params, "otherMobileNums")) {
					otherMobileNums = params.get("otherMobileNums").toString();
				}
				
				//获取当前任务办理人员userId，以英文逗号相连
				eventDisposalWorkflowService.sendSms(curUserIds, otherMobileNums, params.get("smsContent").toString(), userInfo);
			}
		}
		
		return result;
	}
	
	@Override
	public List<Map<String, Object>> findUrgeOrRemindList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> remindMapList = null;
		Long instanceId = null;
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			instanceId = Long.valueOf(params.get("instanceId").toString());
		}
		
		if(instanceId != null && instanceId > 0) {
			remindMapList = baseWorkflowService.findRemindList(params);
		} else {
			throw new ZhsqEventException("缺少有效流程实例id【instanceId】！");
		}
		
		return remindMapList;
	}
	
	/**
	 * 获取当前环节中文名称
	 * @param formId	表单id
	 * @param wfName	流程图名称
	 * @param wfType	流程图类型
	 * @param userInfo	用户信息
	 * @return
	 * @throws Exception
	 */
	private String capCurTaskName(Long formId, String wfName, String wfType, UserInfo userInfo) throws Exception {
		Map<String, Object> curTaskData = this.findCurTaskData4Base(formId, wfName, wfType, userInfo);
		String curTaskName = null;
		
		if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
			curTaskName = curTaskData.get("WF_ACTIVITY_NAME_").toString();
		}
		
		if(curTaskName == null) {
			throw new Exception("任务已完结！");
		}
		
		return curTaskName;
	}
	
	/**
	 * 获取当前环节中文名称
	 * @param instanceId	流程实例id
	 * @return
	 * @throws Exception
	 */
	private String capCurTaskName(Long instanceId) throws Exception {
		Map<String, Object> curTaskData = this.findCurTaskData4Base(instanceId);
		String curTaskName = null;
		
		if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
			curTaskName = curTaskData.get("WF_ACTIVITY_NAME_").toString();
		}
		
		if(curTaskName == null) {
			throw new Exception("任务已完结！");
		}
		
		return curTaskName;
	}
	
	/**
	 * 判断参数的有效性
	 * @param formId	表单id
	 * @param wfName	流程图名称
	 * @param wfType	流程图类型
	 * @return
	 * @throws Exception
	 */
	private boolean isParamValid(Long formId, String wfName, String wfType) throws Exception {
		boolean flag = true;
		StringBuffer msgWrong = new StringBuffer("");
		
		if(formId == null || formId < 0) {
			msgWrong.append("缺少有效的表单id！");
		}
		if(StringUtils.isBlank(wfName)) {
			msgWrong.append("缺少流程图名称！");
		}
		if(StringUtils.isBlank(wfType)) {
			msgWrong.append("缺少流程图类型！");
		}
		
		if(msgWrong.length() > 0) {
			flag = false;
			throw new IllegalArgumentException(msgWrong.toString());
		}
		
		return flag;
	}
}
