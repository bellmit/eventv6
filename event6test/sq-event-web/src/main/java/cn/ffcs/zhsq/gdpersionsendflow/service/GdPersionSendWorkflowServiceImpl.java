package cn.ffcs.zhsq.gdpersionsendflow.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.uam.bo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.utils.CommonFunctions;

@Service("gdPersionSendWorkflowServiceImpl")
@Transactional
public class GdPersionSendWorkflowServiceImpl implements IGdPersionSendWorkflowService {

    @Autowired
    private IBaseWorkflowService baseWorkflowService;
    @Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
    @Autowired
	private UserManageOutService userManageService;
   
    public static final String ACTOR_TYPE_ORG = "1";		//1表示ACTOR_ID为组织ID
	public static final String ACTOR_TYPE_ROLE = "2";		//2表示ACTOR_ID为角色ID
	public static final String ACTOR_TYPE_USER = "3";		//3表示ACTOR_ID为用户ID
	public static final String ACTOR_TYPE_POSITION = "4";	//4表示ACTOR_ID为职位ID
	//流程图表单Id：815
    private final String CASE_WORKFLOW_NAME = "网格员协助送达流程",//任务流程图名称
            CASE_WFTYPE_ID = "gd_person_send",//任务流程类型
            START_NODE_NAME = "start",//流程开始节点名称
            DESCISION_NODE_TYPE = "2",//决策节点环节类型
            DECISION_MAKING_SERVICE = "workflowDecisionMaking4EventCaseService",//决策类
            REJECT_NODE_NAME = "reject",//驳回节点名称，虚拟节点名称
            EVA_NODE_NAME = "task6";//评价节点名称


   /**
    * 启动流程
    */
    @Override
    public boolean startWorkflow4Case(Long sendId, UserInfo userInfo, Map<String, Object> startParams) throws Exception {
        Long instanceId = baseWorkflowService.startWorkflow4Base(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, sendId,userInfo, startParams);
        return instanceId>0;
    }

    /**
     * 提交流程
     */
    @Override
    public boolean subWorkflow4Case(Long sendId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
    	boolean flag = false;
    	if(isCurTaskPaticipant(sendId, userInfo, null)) {
			flag = baseWorkflowService.subWorkflow4Base(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, sendId, nextNodeName, nextUserInfoList, userInfo, extraParam);
		} else {
			String msgWrong = "用户["+ userInfo.getOrgName() +"-"+ userInfo.getPartyName() +"] 不是当前环节[" + this.capCurTaskName(sendId, userInfo) + "] 的办理人员！";
			throw new Exception(msgWrong);
		}
        return flag;
    }

    /**
     * 流程驳回
     */
    @Override
    public boolean rejectWorkflow4Case(Long sendId, String rejectToNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
    	boolean flag = false;

		if(sendId == null || sendId < 0) {
			throw new IllegalArgumentException("缺少任务id！");
		}

		if(CommonFunctions.isBlank(extraParam, "advice") && CommonFunctions.isNotBlank(extraParam, "evaContent")) {
			extraParam.put("advice", extraParam.get("evaContent"));
		}

		if(isCurTaskPaticipant(sendId, userInfo, null)) {
			flag = baseWorkflowService.rejectWorkflow4Base(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, sendId, rejectToNodeName, userInfo, extraParam);
		} else {
			String msgWrong = "用户["+ userInfo.getOrgName() +"-"+ userInfo.getPartyName() +"] 不是当前环节[" + this.capCurTaskName(sendId, userInfo) + "] 的办理人员！";
			throw new Exception(msgWrong);
		}

		return flag;
    }

   /**
    * 依据任务ID获取流程实例
    */
    @Override
    public Long capInstanceId(Long sendId, UserInfo userInfo) throws Exception {
    	Long instanceId = -1L;
		if(sendId != null && sendId > 0) {
			instanceId = baseWorkflowService.capInstanceId(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, sendId, userInfo);
		}
		return instanceId;
    }
    /**
     * 依据任务ID获取流程实例
     */
    @Override
    public ProInstance getProInstanceById(Long sendId, UserInfo userInfo) throws Exception {
    	Long instanceId = capInstanceId(sendId, userInfo);
    	ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
    	return proInstance;
    }

    /**
     * 依据任务ID获取当前办理任务信息
     */
    @Override
    public Map<String, Object> findCurTaskData(Long sendId, UserInfo userInfo) throws Exception {
    	Map<String, Object> curTaskData = null;

		if(sendId != null && sendId > 0) {
			curTaskData = baseWorkflowService.findCurTaskData(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, sendId, userInfo);
		}

		return curTaskData;
    }

    /**
     * 判断是否是当前环节办理人
     */
    @Override
    public boolean isCurTaskPaticipant(Long sendId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
    	boolean flag = false;
    	Long wfTaskId = -1L;

		Map<String, Object>	curTaskData = baseWorkflowService.findCurTaskData(CASE_WORKFLOW_NAME, CASE_WFTYPE_ID, sendId, userInfo);

		if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
			wfTaskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
		}

		if(wfTaskId > 0) {
			List<Map<String, Object>> participantMapList = this.findParticipantsByTaskId(wfTaskId);

			flag = this.isCurTaskPaticipant(participantMapList, userInfo, extraParam);
		}
		
		
		return flag;
    }
    /**
     * 判断用户是否为当前任务的办理人员
     */
    @Override
	public boolean isCurTaskPaticipant(List<Map<String, Object>> participantMapList, UserInfo userInfo,
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
				if(this.ACTOR_TYPE_USER.equals(userType)) {
					flag = userId.equals(curTaskUserId) && orgId.equals(curTaskOrgId);
				} else if(this.ACTOR_TYPE_ORG.equals(userType)) {
					flag = orgId.equals(curTaskUserId);
				}

				if(flag) {
					break;
				}

			}
		}

		return flag;
	}

    /**
     * 依据工作流任务id获取任务办理人员信息
     */
	@Override
	public List<Map<String, Object>> findParticipantsByTaskId(Long wfTaskId) {
		List<Map<String, Object>> participantMapList = null;

		if(wfTaskId != null && wfTaskId > 0){
			participantMapList = baseWorkflowService.findParticipantsByTaskId(wfTaskId);
		}

		if(participantMapList != null && participantMapList.size() > 0) {
			Long userId = -1L, orgId = -1L;
			OrgSocialInfoBO orgInfo = null;
			UserBO user = null;

			for(Map<String, Object> participantMap : participantMapList) {
				if(CommonFunctions.isNotBlank(participantMap, "USER_TYPE")) {
					String userType = participantMap.get("USER_TYPE").toString();

					if(this.ACTOR_TYPE_USER.equals(userType)) {
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

	/**
	 * 获取经办任务信息
	 */
    @Override
    public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params) {
        List<Map<String, Object>> participantMapList = null;
        if(instanceId != null && instanceId > 0){
            participantMapList = baseWorkflowService.capHandledTaskInfoMap(instanceId,order,params);
        }
        return participantMapList;
    }

    /**
	 * 获取当前环节中文名称
	 * @param sendId	业务主键id
	 * @param userInfo	用户信息
	 * @return
	 * @throws Exception
	 */
	private String capCurTaskName(Long sendId, UserInfo userInfo) throws Exception {
		Map<String, Object> curTaskData = this.findCurTaskData(sendId, userInfo);
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
