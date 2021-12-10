package cn.ffcs.zhsq.eventCase.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.uam.bo.UserInfo;

/**
 * 案件 工作流接口
 * @author zhangls
 *
 */
public interface IEventCaseWorkflowService {
	/**
	 * 启动流程
	 * @param caseId		案件id
	 * @param userInfo		用户信息
	 * @param extraParam
	 * 			advice		办理意见
	 * 			isClose		true，自动流转至评价节点
	 * @return
	 * @throws Exception
	 */
	public boolean startWorkflow4Case(Long caseId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 提交流程
	 * @param caseId			案件id
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
	public boolean subWorkflow4Case(Long caseId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 流程驳回
	 * @param caseId			案件id
	 * @param rejectToNodeName	驳回指定的节点名称
	 * @param userInfo			用户信息
	 * @param extraParam
	 * 			advice			驳回意见
	 * @return
	 * @throws Exception
	 */
	public boolean rejectWorkflow4Case(Long caseId, String rejectToNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 依据案件id获取流程实例
	 * @param caseId	案件id
	 * @param userInfo
	 * @return 查找成功返回instanceId，否则返回-1
	 * @throws Exception
	 */
	public Long capInstanceId(Long caseId, UserInfo userInfo) throws Exception;
	
	/**
	 * 依据案件id获取当前办理任务信息
	 * @param caseId	案件id
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
	public Map<String, Object> findCurTaskData(Long caseId, UserInfo userInfo) throws Exception;
	
	/**
	 * 依据任务id获取任务办理人员信息
	 * @param taskId	任务id
	 * @return
	 * 		USER_TYPE	用户类型，3 办理人员为人员；1 办理人员为组织
	 * 		USER_ID		办理人员id，用户类型为3时，其为用户id；用户类型为1时，其为组织id
	 * 		USER_NAME	办理人员姓名
	 * 		ORG_ID		办理人员组织id
	 * 		ORG_NAME	办理人员组织名称
	 */
	public List<Map<String, Object>> findParticipantsByTaskId(Long taskId);
	
	/**
	 * 判断用户是否为当前任务的办理人员
	 * @param caseId		案件id
	 * @param userInfo		用户信息
	 * @param extraParam
	 * @return
	 * @throws Exception
	 */
	public boolean isCurTaskPaticipant(Long caseId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
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
	 * 依据节点id获取该节点的操作按钮
	 * @param nodeId	节点id
	 * @return
	 * 		operateEvent	按钮点击事件名称
	 * 		anotherName		按钮名称
	 */
	public List<Map<String, Object>> findOperateByNodeId(Integer nodeId);
	
	/**
	 * 获取下一步可办理环节
	 * @param caseId	案件id
	 * @param userInfo	用户信息
	 * @param params
	 * @return
	 * 		nodeId			节点id
	 * 		nodeName		节点名称
	 * 		nodeNameZH		节点中文名称
	 * 		nodeCode		节点编码
	 * 		nodeType		节点类型
	 * 		transitionCode	节点线属性
	 * 		dynamicSelect	节点是否动态分配
	 * @throws Exception
	 */
	public List<Map<String, Object>> findNextTaskNodes(Long caseId, UserInfo userInfo, Map<String, Object> params) throws Exception;
	
}
