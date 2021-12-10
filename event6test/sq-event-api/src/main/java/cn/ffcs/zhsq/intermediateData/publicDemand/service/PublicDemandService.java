package cn.ffcs.zhsq.intermediateData.publicDemand.service;

import cn.ffcs.uam.bo.UserInfo;

import java.util.List;
import java.util.Map;

public interface PublicDemandService {

    /**
     * 上报民众诉求事件
     * @param publicAppealId
     * @param userInfo
     * @return 上报成功，返回true；否则返回false
     * @throws Exception
     */
    public boolean reportPublicAppealEvent(Long publicAppealId, UserInfo userInfo) throws Exception;


    /**
     * 办理民众诉求事件
     * @param publicAppealId	民众诉求id
     * @param nextNodeName		下一环节名称
     * @param nextUserInfoList	下一环节办理人员 userId orgId，为空时，且userInfo不为空时，使用userInfo中的人员
     * @param userInfo			当前用户信息
     * @param extraParam
     * 				nextUserIds	下一环节办理人员，以英文逗号分隔
     * 				nextOrgIds	下一环节办理组织，以英文逗号分隔，与nextUserIds一一对应
     * 				advice		办理意见
     * @return
     * @throws Exception
     */
    public boolean subWorkflow(Long publicAppealId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;

    /**
     * 获取可办理下一环节
     * @param publicAppealId	民众诉求id
     * @param userInfo			用户信息
     * @param params
     * 			decisionService	决策实现serviceName
     * 			curOrgId		组织id
     * @return
     * 			nodeId			节点id
     * 			nodeName		节点名称
     * 			nodeNameZH		节点中文名称
     * 			nodeType		节点类型
     * 			transitionCode	扭转编码，如U5R1U4
     * 			dynamicSelect	是否动态跳转 1 是；0 否
     * @throws Exception
     */
    public List<Map<String, Object>> findNextTaskNodes(Long publicAppealId, UserInfo userInfo, Map<String, Object> params) throws Exception;

    /**
     * 获取可办理下一环节
     * @param publicAppealId	民众诉求id
     * @param userInfo			用户信息
     * @return
     * 			nodeId			节点id
     * 			nodeName		节点名称
     * 			nodeNameZH		节点中文名称
     * 			nodeType		节点类型
     * 			transitionCode	扭转编码，如U5R1U4
     * 			dynamicSelect	是否动态跳转 1 是；0 否
     * @throws Exception
     */
    public List<Map<String, Object>> findNextTaskNodes(Long publicAppealId, UserInfo userInfo) throws Exception;

    /**
     * 驳回民众诉求事件至指定环节
     * @param publicAppealId	民众诉求id
     * @param rejectToNodeName	驳回到的节点名称，为空时，驳回上一环节
     * @param userInfo
     * @param extraParam
     * 				advice		驳回意见
     * @return
     * @throws Exception
     */
    public boolean rejectWorkflow(Long publicAppealId, String rejectToNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;

    /**
     * 获取当前环节数据
     * @param publicAppealId	民众诉求id
     * @param userInfo			用户信息
     * @return
     * @throws Exception
     */
    public Map<String, Object> findCurTaskData(Long publicAppealId, UserInfo userInfo) throws Exception;

    /**
     * 依据任务id获取该任务的办理人员信息
     * @param taskId	任务id
     * @return
     * 		USER_ID		办理人员id
     * 		USER_NAME	办理人员姓名
     * 		USER_TYPE	办理人员类型 （1：组织，2：角色，3：人，4：职位，5：职能）
     * 		ORG_ID		办理人员组织id
     * 		ORG_NAME	办理人员组织名称
     */
    public List<Map<String, Object>> findParticipantsByTaskId(Long taskId);

    /**
     * 判断用户是否为当前任务办理人员
     * @param publicAppealId	民众诉求id
     * @param userInfo			用户信息
     * @param extraParam
     * @return 是当前办理人员，返回true；否则返回false
     * @throws Exception
     */
    public boolean isCurTaskPaticipant(Long publicAppealId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;

    /**
     * 判断用户是否为归属于指定办理人员
     * @param participantMapList	指定的办理人员
     * @param userInfo				用户信息
     * @param extraParam
     * @return 是归属于指定办理人员，返回true；否则返回false
     * @throws Exception
     */
    public boolean isCurTaskPaticipant(List<Map<String, Object>> participantMapList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;

    /**
     * 获取流程实例id
     * @param publicAppealId	民众诉求id
     * @param userInfo			用户信息
     * @return
     * @throws Exception
     */
    public Long capInstanceId(Long publicAppealId, UserInfo userInfo) throws Exception;

    /**
     * 依据实例id获取流程实例信息
     * @param instanceId	实例id
     * @return
     * 		instanceId	流程实例id
     * 		userId		流程创建用户id
     * 		userName	流程创建用户姓名
     * 		orgId		流程创建组织id
     * 		orgName		流程创建组织名称
     */
    public Map<String, Object> capProInstance(Long instanceId);

    /**
     * 获取流程实例信息
     * @param publicAppealId	民众诉求id
     * @param userInfo			用户信息
     * @return
     * 		instanceId	流程实例id
     * 		userId		流程创建用户id
     * 		userName	流程创建用户姓名
     * 		orgId		流程创建组织id
     * 		orgName		流程创建组织名称
     * @throws Exception
     */
    public Map<String, Object> capProInstance(Long publicAppealId, UserInfo userInfo) throws Exception;

    /**
     * 依据节点id获取该环节操作按钮
     * @param nodeId	节点id
     * @return
     * 		operateEvent	按钮操作事件
     * 		anotherName		按钮名称
     */
    public List<Map<String, Object>> findOperateByNodeId(Integer nodeId);
}
