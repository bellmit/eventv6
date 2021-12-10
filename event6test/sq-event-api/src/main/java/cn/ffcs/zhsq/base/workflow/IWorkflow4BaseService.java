package cn.ffcs.zhsq.base.workflow;

import java.util.List;
import java.util.Map;
import cn.ffcs.uam.bo.UserInfo;

/**
 * 工作流基本接口
 * @author zhangls
 *
 */
public interface IWorkflow4BaseService {
	public static final String SQL_ORDER_ASC = "ASC";//升序排列
	public static final String SQL_ORDER_DESC = "DESC";//降序排列
	
	/**
	 * 启动流程
	 * @param formId	表单id
	 * @param wfName	流程图名称
	 * @param wfType	流程图类型
	 * @param userInfo	用户信息
	 * @param extraParam
	 * 			advice		办理意见
	 * 			isClose		true，自动流转至评价节点
	 * @return
	 * @throws Exception
	 */
	public boolean startWorkflow4Base(Long formId, String wfName, String wfType, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 提交流程
	 * @param formId			表单id
	 * @param wfName			流程图名称
	 * @param wfType			流程图类型
	 * @param nextNodeName		下一环节名称
	 * @param nextUserInfoList	下一环节办理人员
	 * @param userInfo
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
	public boolean subWorkflow4Base(Long formId, String wfName, String wfType, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 流程驳回
	 * @param formId			表单id
	 * @param wfName			流程图名称
	 * @param wfType			流程图类型
	 * @param rejectToNodeName	驳回指定的节点名称，为空时，驳回上一环节
	 * @param userInfo
	 * @param extraParam
	 * 			advice			驳回意见
	 * @return
	 * @throws Exception
	 */
	public boolean rejectWorkflow4Base(Long formId, String wfName, String wfType, String rejectToNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 依据表单id获取流程实例
	 * @param formId	表单id
	 * @param wfName	流程图名称
	 * @param wfType	流程图类型
	 * @param userInfo	用户信息
	 * @return
	 * 		instanceId	流程实例id
	 * @throws Exception
	 */
	public Map<String, Object> capInstance4Base(Long formId, String wfName, String wfType, UserInfo userInfo) throws Exception;
	
	/**
	 * 依据表单id获取当前办理任务信息
	 * @param formId	表单id
	 * @param wfName	流程图名称
	 * @param wfType	流程图类型
	 * @param userInfo	用户信息
	 * @return
	 * 		WF_ORGID 			当前办理人员组织id，多个值以英文逗号连接，有去重
	 * 		WF_ORGID_ALL		当前办理人员组织id，多个值以英文逗号连接，没有去重
	 * 		WF_ORGNAME 			当前办理人员组织名称，多个值以英文逗号连接，有去重
	 * 		WF_ORGNAME_ALL		当前办理人员组织名称，多个值以英文逗号连接，没有去重
	 * 		WF_USERID  			当前办理人员用户Id，多个值以英文逗号连接，有去重
	 * 		WF_USERID_ALL		当前办理人员用户Id，多个值以英文逗号连接，没有去重
	 * 		WF_USERNAME  		当前办理人员partyName，多个值以英文逗号连接，有去重
	 * 		WF_USERNAME_ALL		当前办理人员partyName，多个值以英文逗号连接，没有去重
	 * 		WF_DBID_  			当前节点任务
	 * 		WF_ACTIVITY_NAME_	当前节点任务中文名称
	 * 		NODE_NAME 			当前节点编码
	 * 		NODE_ID				当前节点id
	 * @throws Exception
	 */
	public Map<String, Object> findCurTaskData4Base(Long formId, String wfName, String wfType, UserInfo userInfo) throws Exception;
	
	/**
	 * 依据流程实例id获取当前办理任务信息
	 * @param instanceId	流程实例id
	 * @return
	 * 		WF_ORGID 			当前办理人员组织id，多个值以英文逗号连接，有去重
	 * 		WF_ORGID_ALL		当前办理人员组织id，多个值以英文逗号连接，没有去重
	 * 		WF_ORGNAME 			当前办理人员组织名称，多个值以英文逗号连接，有去重
	 * 		WF_ORGNAME_ALL		当前办理人员组织名称，多个值以英文逗号连接，没有去重
	 * 		WF_USERID  			当前办理人员用户Id，多个值以英文逗号连接，有去重
	 * 		WF_USERID_ALL		当前办理人员用户Id，多个值以英文逗号连接，没有去重
	 * 		WF_USERNAME  		当前办理人员partyName，多个值以英文逗号连接，有去重
	 * 		WF_USERNAME_ALL		当前办理人员partyName，多个值以英文逗号连接，没有去重
	 * 		WF_DBID_  			当前节点任务
	 * 		WF_ACTIVITY_NAME_	当前节点任务中文名称
	 * 		NODE_NAME 			当前节点编码
	 * 		NODE_ID				当前节点id
	 * @throws Exception
	 */
	public Map<String, Object> findCurTaskData4Base(Long instanceId) throws Exception;
	
	/**
	 * 依据流程实例id获取当前办理任务信息
	 * @param instanceId		流程实例id
	 * @param params			额外参数
	 * 			isCapCurUserInfo是否获取当前办理人员的用户信息，true为获取，默认为false
	 * @return
	 * 		WF_ORGID 			当前办理人员组织id，多个值以英文逗号连接，有去重
	 * 		WF_ORGID_ALL		当前办理人员组织id，多个值以英文逗号连接，没有去重
	 * 		WF_ORGNAME 			当前办理人员组织名称，多个值以英文逗号连接，有去重
	 * 		WF_ORGNAME_ALL		当前办理人员组织名称，多个值以英文逗号连接，没有去重
	 * 		WF_USERID  			当前办理人员用户Id，多个值以英文逗号连接，有去重
	 * 		WF_USERID_ALL		当前办理人员用户Id，多个值以英文逗号连接，没有去重
	 * 		WF_USERNAME  		当前办理人员partyName，多个值以英文逗号连接，有去重
	 * 		WF_USERNAME_ALL		当前办理人员partyName，多个值以英文逗号连接，没有去重
	 * 		WF_DBID_  			当前节点任务
	 * 		WF_ACTIVITY_NAME_	当前节点任务中文名称
	 * 		NODE_NAME 			当前节点编码
	 * 		NODE_ID				当前节点id
	 * 		curUserInfoMapList	当前办理人员用户信息，类型为List<Map<String, Object>>，isCapCurUserInfo为true时，获取
	 * 			userId			用户id
	 * 			userName		用户账号
	 * 			partyName		用户姓名
	 * 			verifyMobile	用户联系电话
	 * @throws Exception
	 */
	public Map<String, Object> findCurTaskData4Base(Long instanceId, Map<String, Object> params) throws Exception;
	
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
	 * @param formId	表单id
	 * @param wfName	流程图名称
	 * @param wfType	流程图类型
	 * @param userInfo	用户信息
	 * @param extraParam
	 * @return
	 * @throws Exception
	 */
	public boolean isCurTaskPaticipant4Base(Long formId, String wfName, String wfType, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
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
	public boolean isCurTaskPaticipant4Base(List<Map<String, Object>> participantMapList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
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
	 * @param formId	表单id
	 * @param wfName	流程图名称
	 * @param wfType	流程图类型
	 * @param userInfo	用户信息
	 * @param params
	 * 			instanceId	流程实例id
	 * 			curTaskId	当前任务id
	 * 			curTaskName	当前节点名称
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
	public List<Map<String, Object>> findNextTaskNodes4Base(Long formId, String wfName, String wfType, UserInfo userInfo, Map<String, Object> params) throws Exception;
	
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
	 * 
	 * 		PARENT_TASK_ID	上一任务id
	 * 		TASK_ID			任务id
	 * 		TASK_TYPE		任务类型
	 * 		TASK_NAME		环节中文名称
	 * 		TASK_CODE		环节编码
	 * 		PRE_TASK_CODE	上一环节编码
	 * 		OPERATE_TYPE	操作类型
	 * 		TRANSACTOR_ID	任务办理人员id
	 * 		TRANSACTOR_NAME	任务办理人员姓名
	 * 		ORG_NAME		任务办理人员组织名称
	 * 		ORG_ID			任务办理人员组织id
	 * 		REMARKS			任务办理意见
	 * 		START_TIME		任务开始时间
	 * 		END_TIME		任务办结时间
	 */
	public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params);
	
	/**
	 * 新增催办/督办记录
	 * @param params	催办/督办信息
	 * 			instanceId			流程实例id
	 * 			remindRemark		催办/督办意见
	 * 			otherMobileNums		额外接收短信的号码，多个值使用英文逗号进行分割
	 * 			isSendSmsContent	是否发送短信，默认为true
	 * 			smsContent			短信内容
	 * 			category			记录类别，1 催办；2 督办；
	 * @param userInfo	操作用户信息
	 * @return
	 * @throws Exception
	 */
	public boolean addUrgeOrRemind(Long instanceId, Map<String, Object> params, UserInfo userInfo) throws Exception;
	
	/**
	 * 获取催办/督办记录
	 * @param params
	 * 必填参数
	 * 		instanceId	流程实例id
	 * 
	 * 非必填参数
	 * 		taskId		任务id
	 * 		category	记录类型，1 获取催办记录；2 获取督办记录；
	 * @return
	 * 		remindDate		催办/督办时间，类型为：java.util.Date
	 * 		remindDateStr	催办/督办时间文本，格式为：yyyy-mm-dd hh24:mi:ss
	 * 		remindUserName	催办/督办操作人员姓名
	 * 		supervisionType	督办类型，1：督办-黄牌；2：督办-红牌；3：督办-黑牌；
	 * 		category		记录类别，1 获取催办记录；2 获取督办记录；
	 * 		remarks			催办/督办意见
	 * @throws Exception 
	 */
	public List<Map<String, Object>> findUrgeOrRemindList(Map<String, Object> params) throws Exception;
	
}
