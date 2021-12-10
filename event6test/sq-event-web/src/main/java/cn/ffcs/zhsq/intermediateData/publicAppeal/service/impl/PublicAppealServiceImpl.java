package cn.ffcs.zhsq.intermediateData.publicAppeal.service.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.intermediateData.publicDemand.service.PublicDemandService;
import cn.ffcs.zhsq.mybatis.persistence.drugEnforcementEvent.DrugEnforcementEventMapper;


import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("publicDemandServiceImpl")
public class PublicAppealServiceImpl implements PublicDemandService {

    @Autowired
    private DrugEnforcementEventMapper drugEnforcementEventMapper;

    @Autowired
    private IBaseDictionaryService baseDictionaryService;

    @Autowired
    private IBaseWorkflowService baseWorkflowService;

    @Autowired
    private OrgSocialInfoOutService orgSocialInfoService;

    @Autowired
    private UserManageOutService userManageService;

    //@Autowired
	//@Qualifier("workflowDecisionMakingService")
    //private IWorkflowDecisionMakingService<Object> workflowDecisionMakingService;

    private final String PUBLIC_APPEAL_WORKFLOW_NAME = "民众诉求流程",
            PUBLIC_APPEAL_WFTYPE_ID = "public_appeal";


    /**
     * 上报民众诉求事件
     *
     * @param publicAppealId
     * @param userInfo
     * @return 上报成功，返回true；否则返回false
     * @throws Exception
     */
    @Override
    public boolean reportPublicAppealEvent(Long publicAppealId, UserInfo userInfo) throws Exception {
        Long instanceId = -1L, orgId = -1L;

        boolean flag = false;

        if (userInfo != null) {
            orgId = userInfo.getOrgId();

            if (orgId != null && orgId > 0) {
                OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
                if (orgInfo != null) {
                    String chiefLevel = orgInfo.getChiefLevel();
                    if (!ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevel)) {
                        throw new Exception("该用户不能提交流程！");
                    }
                }
            }


        }

        if (publicAppealId != null && publicAppealId > 0) {
            Map<String, Object> extraParam = new HashMap<String, Object>();


            instanceId = baseWorkflowService.startWorkflow4Base(PUBLIC_APPEAL_WORKFLOW_NAME, PUBLIC_APPEAL_WFTYPE_ID, publicAppealId, userInfo, extraParam);
            if (instanceId != null && instanceId > 0) {
//                DrugEnforcementEvent drugEnforcementEvent = new DrugEnforcementEvent();
//                drugEnforcementEvent.setDrugEnforcementId(publicAppealId);
//                drugEnforcementEvent.setUpdateUser(userInfo.getUserId());

                flag = true;



                if (flag) {//自动扭转过决策节点
                    String nextNodeName = null;

                    List<Map<String, Object>> nextNodes = findNextTaskNodes(publicAppealId, userInfo);
                    if (nextNodes != null && nextNodes.size() == 1) {
                        Map<String, Object> nextNodeMap = nextNodes.get(0);

                        if (CommonFunctions.isNotBlank(nextNodeMap, "nodeType")) {
                            if (CommonFunctions.isNotBlank(nextNodeMap, "nodeName")) {
                                nextNodeName = nextNodeMap.get("nodeName").toString();
                            }
                        }
                    }

                    if (StringUtils.isNotBlank(nextNodeName)) {
                        flag = this.subWorkflow(publicAppealId, nextNodeName, null, userInfo, extraParam);
                    }
                }
            }
        }

        return flag;



}


    @Override
    public List<Map<String, Object>> findNextTaskNodes(Long publicAppealId, UserInfo userInfo) throws Exception {
        List<Map<String, Object>> nextNodeMapList = null;
        Map<String, Object> param = new HashMap<String, Object>();
        Long orgId = -1L;

        if (publicAppealId == null || publicAppealId < 0) {
            throw new IllegalArgumentException("缺少民众诉求id！");
        }
        if (userInfo != null) {
            orgId = userInfo.getOrgId();
        }


        param.put("curOrgId", orgId);

        nextNodeMapList = this.findNextTaskNodes(publicAppealId, userInfo, param);

        return nextNodeMapList;
    }

    @Override
    public List<Map<String, Object>> findNextTaskNodes(Long publicAppealId, UserInfo userInfo, Map<String, Object> params) throws Exception {
        List<Node> nextNodeList = null;
        List<Map<String, Object>> nextNodeMapList = null;

        if (publicAppealId == null || publicAppealId < 0) {
            throw new IllegalArgumentException("缺少民众诉求id");
        }

        if (params == null || params.isEmpty()) {
            nextNodeMapList = this.findNextTaskNodes(publicAppealId, userInfo);
        } else {
            nextNodeList = baseWorkflowService.findNextTaskNodes(PUBLIC_APPEAL_WORKFLOW_NAME, PUBLIC_APPEAL_WFTYPE_ID, publicAppealId, userInfo, params);

            if (nextNodeList != null && nextNodeList.size() > 0) {
                nextNodeMapList = new ArrayList<Map<String, Object>>();
                Map<String, Object> nextNodeMap = null;

                for (Node nextNode : nextNodeList) {
                    nextNodeMap = new HashMap<String, Object>();

                    nextNodeMap.put("nodeId", nextNode.getNodeId());
                    nextNodeMap.put("nodeName", nextNode.getNodeName());
                    nextNodeMap.put("nodeNameZH", nextNode.getNodeNameZH());
                    nextNodeMap.put("nodeType", nextNode.getNodeType());
                    nextNodeMap.put("transitionCode", nextNode.getTransitionCode());
                    nextNodeMap.put("dynamicSelect", nextNode.getDynamicSelect());

                    nextNodeMapList.add(nextNodeMap);
                }
            }
        }

        return nextNodeMapList;
    }

    @Override
    public boolean subWorkflow(Long publicAppealId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
        boolean flag = false;
        String nextUserIds = null, nextOrgIds = null;

        if (publicAppealId == null || publicAppealId < 0) {
            throw new IllegalArgumentException("确实民众诉求id！");
        }
        if (StringUtils.isBlank(nextNodeName)) {
            throw new IllegalArgumentException("缺少可办理的下一环节！");
        }
        if (nextUserInfoList == null) {
            nextUserInfoList = new ArrayList<UserInfo>();
        }
        if (CommonFunctions.isNotBlank(extraParam, "nextUserIds")) {
            nextUserIds = extraParam.get("nextUserIds").toString();
        }
        if (CommonFunctions.isNotBlank(extraParam, "nextUserIds")) {
            nextOrgIds = extraParam.get("nextOrgIds").toString();
        }
        if (StringUtils.isNotBlank(nextUserIds) && StringUtils.isNotBlank(nextOrgIds)) {
            String[] nextUserArray = nextUserIds.split(","),
                    nextOrgIdArray = nextOrgIds.split(",");
            String nextUserIdStr = null, nextOrgIdStr = null;
            Long nextUserId = -1L, nextOrgId = -1L;
            UserInfo nextUserInfo = null;

            for (int index = 0, len = nextUserArray.length; index < len; index++) {
                nextUserIdStr = nextUserArray[index];
                nextOrgIdStr = nextOrgIdArray[index];

                if (StringUtils.isNotBlank(nextUserIdStr)) {
                    nextUserId = Long.valueOf(nextUserIdStr);
                }
                if (StringUtils.isNotBlank(nextOrgIdStr)) {
                    nextOrgId = Long.valueOf(nextOrgIdStr);
                }
                if (nextUserId > 0 || nextOrgId > 0) {
                    nextUserInfo = new UserInfo();
                    nextUserInfo.setUserId(nextUserId);
                    nextUserInfo.setOrgId(nextOrgId);
                    nextUserInfoList.add(nextUserInfo);
                }
            }
        }

        if (nextUserInfoList.size() == 0) {
            if (userInfo != null) {
                nextUserInfoList = new ArrayList<UserInfo>();
                nextUserInfoList.add(userInfo);
            } else {
                throw new IllegalArgumentException("缺少下一环节的可办理人员！");
            }
        }

        flag = baseWorkflowService.subWorkflow4Base(PUBLIC_APPEAL_WORKFLOW_NAME, PUBLIC_APPEAL_WFTYPE_ID, publicAppealId, nextNodeName, nextUserInfoList, userInfo, extraParam);

        return flag;
    }

    @Override
    public boolean rejectWorkflow(Long publicAppealId, String rejectToNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
        boolean flag = false;

        if(publicAppealId == null || publicAppealId < 0) {
            throw new IllegalArgumentException("缺少民众诉求事件id！");
        }

        flag = baseWorkflowService.rejectWorkflow4Base(PUBLIC_APPEAL_WORKFLOW_NAME, PUBLIC_APPEAL_WFTYPE_ID, publicAppealId, rejectToNodeName, userInfo, extraParam);

        return flag;
    }

    @Override
    public Map<String, Object> findCurTaskData(Long publicAppealId, UserInfo userInfo) throws Exception {
        Map<String, Object> curTaskData = null;

        if(publicAppealId != null && publicAppealId > 0) {
            curTaskData = baseWorkflowService.findCurTaskData(PUBLIC_APPEAL_WORKFLOW_NAME, PUBLIC_APPEAL_WFTYPE_ID, publicAppealId, userInfo);
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
            for(Map<String, Object> participantMap : participantMapList) {
                if(CommonFunctions.isNotBlank(participantMap, "USER_TYPE")) {
                    String userType = participantMap.get("USER_TYPE").toString();

                    if(IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userType)) {
                        Object userIdObj = participantMap.get("USER_ID");
                        if(userIdObj != null){
                            Long userId = Long.valueOf(userIdObj.toString());
                            UserBO user = userManageService.getUserInfoByUserId(userId);

                            if(user != null) {
                                participantMap.put("USER_NAME", user.getPartyName());
                            }
                        }
                    }
                }

                if(CommonFunctions.isNotBlank(participantMap, "ORG_ID")) {
                    Long orgId = Long.valueOf(participantMap.get("ORG_ID").toString());
                    OrgSocialInfoBO orgEntity = orgSocialInfoService.findByOrgId(orgId);
                    if(orgEntity != null) {
                        participantMap.put("ORG_NAME", orgEntity.getOrgName());
                    }
                }
            }
        }

        return participantMapList;
    }

    @Override
    public boolean isCurTaskPaticipant(Long publicAppealId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
        boolean flag = false;

        Map<String, Object>	curTaskData = baseWorkflowService.findCurTaskData(PUBLIC_APPEAL_WORKFLOW_NAME, PUBLIC_APPEAL_WFTYPE_ID, publicAppealId, userInfo);

        Long taskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());

        List<Map<String, Object>> participantMapList = this.findParticipantsByTaskId(taskId);

        flag = this.isCurTaskPaticipant(participantMapList, userInfo, extraParam);

        return flag;
    }

    @Override
    public boolean isCurTaskPaticipant(List<Map<String, Object>> participantMapList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
        boolean flag = false;

        if(participantMapList != null && participantMapList.size() > 0) {
            Long userId = -1L, orgId = -1L;

            if(userInfo != null) {
                userId = userInfo.getUserId();
                orgId = userInfo.getOrgId();
            }

            for(Map<String, Object> participantMap : participantMapList) {
                Long curTaskUserId = -1L, curTaskOrgId = -1L;
                String userType = "";//1表示USER_ID为组织ID；3表示USER_ID为用户ID

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
    public Long capInstanceId(Long publicAppealId, UserInfo userInfo) throws Exception {
        Long instanceId = -1L;

        if(publicAppealId != null && publicAppealId > 0) {
            instanceId = baseWorkflowService.capInstanceId(PUBLIC_APPEAL_WORKFLOW_NAME, PUBLIC_APPEAL_WFTYPE_ID, publicAppealId, userInfo);
        }

        return instanceId;
    }

    @Override
    public Map<String, Object> capProInstance(Long instanceId) {
        ProInstance pro = null;
        Map<String, Object> proMap = null;

        if(instanceId != null && instanceId > 0) {
            pro = baseWorkflowService.findProByInstanceId(instanceId);

            if(pro != null) {
                proMap = new HashMap<String, Object>();

                proMap.put("instanceId", pro.getInstanceId());
                proMap.put("userId", pro.getUserId());
                proMap.put("userName", pro.getUserName());
                proMap.put("orgId", pro.getOrgId());
                proMap.put("orgName", pro.getOrgName());
            }
        }

        return proMap;
    }

    @Override
    public Map<String, Object> capProInstance(Long publicAppealId, UserInfo userInfo) throws Exception {
        ProInstance pro = null;
        Map<String, Object> proMap = null;

        if(publicAppealId != null && publicAppealId > 0 && userInfo != null) {
            pro = baseWorkflowService.capProInstance(PUBLIC_APPEAL_WORKFLOW_NAME, PUBLIC_APPEAL_WFTYPE_ID, publicAppealId, userInfo);

            if(pro != null) {
                proMap = new HashMap<String, Object>();

                proMap.put("instanceId", pro.getInstanceId());
                proMap.put("userId", pro.getUserId());
                proMap.put("userName", pro.getUserName());
                proMap.put("orgId", pro.getOrgId());
                proMap.put("orgName", pro.getOrgName());
            }
        }

        return proMap;
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



}