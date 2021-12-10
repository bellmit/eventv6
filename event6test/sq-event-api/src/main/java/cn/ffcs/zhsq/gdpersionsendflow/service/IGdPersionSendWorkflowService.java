package cn.ffcs.zhsq.gdpersionsendflow.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.ProInstance;

/**
 * @Description: 网格员协助送达流程-工作流服务封装
 * @Author: 张祯海
 * @Date: 03-14 15:54:40
 * @Copyright: 2019 福富软件
 */
public interface IGdPersionSendWorkflowService {

    /**
     * 启动流程
     * @param sendId		业务表ID
     * @param userInfo		用户信息
     * @param extraParam
     * 			advice		办理意见
     * @return
     * @throws Exception
     */
    boolean startWorkflow4Case(Long sendId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;

    /**
     * 提交流程
     * @param sendId			业务表ID
     * @param nextNodeName		下一环节名称
     * @param nextUserInfoList	下一环节办理人员
     * @param userInfo			用户信息
     * @param extraParam
     * 			advice			办理意见
     * 			evaContent		评价意见
     * 			evaLevel		评价等级
     * 			nextUserIds		下一环节办理人员id，以英文逗号分隔
     * 			nextOrgIds		下一环节办理人员组织id，以英文逗号分隔
     * 			curNodeName		当前环节名称
     * @return
     * @throws Exception
     */
    boolean subWorkflow4Case(Long sendId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;

    /**
     * 流程驳回
     * @param sendId			业务表ID
     * @param rejectToNodeName	驳回指定的节点名称
     * @param userInfo			用户信息
     * @param extraParam
     * 			advice			驳回意见
     * @return
     * @throws Exception
     */
    boolean rejectWorkflow4Case(Long sendId, String rejectToNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;

    /**
     * 依据任务ID获取流程实例
     * @param sendId	业务表ID
     * @param userInfo
     * @return 查找成功返回instanceId，否则返回-1
     * @throws Exception
     */
    public Long capInstanceId(Long sendId, UserInfo userInfo) throws Exception;

    /**
     * 依据任务ID获取当前办理任务信息
     * @param sendId	业务表ID
     * @param userInfo	用户信息
     * @return
     * 		WF_ORGID	当前办理人员组织Id
     * 		WF_ORGNAME	当前办理人员组织名称
     * 		WF_USERID	当前办理人员userId，以英文逗号连接
     * 		WF_USERNAME	当前办理人员姓名
     * 		WF_DBID_	当前节点任务
     * 		WF_ACTIVITY_NAME_	当前节点任务名称
     * 		NODE_NAME	当前节点编码
     * 		NODE_ID		当前节点id
     * @throws Exception
     */
    public Map<String, Object> findCurTaskData(Long sendId, UserInfo userInfo) throws Exception;

    /**
     * 判断用户是否为当前任务的办理人员
     * @param sendId		业务表ID
     * @param userInfo		用户信息
     * @param extraParam
     * @return
     * @throws Exception
     */
    public boolean isCurTaskPaticipant(Long sendId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
    
    /**
	 * 判断用户是否为当前任务的办理人员
	 * @param participantMapList	当前任务的办理人员
	 * 			USER_TYPE			用户类型，3 办理人员为人员；1 办理人员为组织
	 * 			USER_ID				用户id，用户类型为3时，其为用户id；用户类型为1时，其为组织id
	 * 			ORG_ID				组织id
	 * @param userInfo				用户信息
	 * @param extraParam
	 * @return
	 * @throws Exception
	 */
	public boolean isCurTaskPaticipant(List<Map<String, Object>> participantMapList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 依据工作流任务id获取任务办理人员信息
	 * @param wfTaskId	工作流任务id
	 * @return
	 * 		USER_TYPE	用户类型，3 办理人员为人员；1 办理人员为组织
	 * 		USER_ID		办理人员id，用户类型为3时，其为用户id；用户类型为1时，其为组织id
	 * 		USER_NAME	办理人员姓名
	 * 		ORG_ID		办理人员组织id
	 * 		ORG_NAME	办理人员组织名称
	 */
	public List<Map<String, Object>> findParticipantsByTaskId(Long wfTaskId);

    /**
     * 获取经办任务信息
     * @param instanceId	流程实例id
     * @param order			任务排序方式，默认为ASC
     * 						DESC：ORDER BY T.END_TIME DESC, T.TASK_ID DESC;
     * 						ASC ：ORDER BY T.END_TIME ASC
     * @param params
     * 			isQueryRemindList		是否获取任务催办列表；true为获取；默认为false
     * 			isQueryNotify			是否获取知会相关信息；true为获取；默认为false
     * 			isQueryEvaluateList		是否获取任务评价信息；true为获取；默认为false
     * 			isQueryTaskReceiveList	是否获取任务接收信息；true为获取；默认为false
     * @return
     * 		notify			知会相关信息
     * 			npList		知会列表
     * 		remindList		任务催办列表
     * 		evaluateList	任务评价信息
     * 		taskReceiveList	任务接收信息
     */
	public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params);

    /**
     * 获取流程实例
     * @param instanceId
     * @return
     * @throws Exception
     */
    public ProInstance getProInstanceById(Long sendId, UserInfo userInfo) throws Exception;

}
