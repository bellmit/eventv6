package cn.ffcs.zhsq.reportFocus;

import java.util.List;
import java.util.Map;
import cn.ffcs.uam.bo.UserInfo;

/**
 * @Description: 南安重点关注上报工作流相关接口
 * @ClassName:   IReportFocusWorkflowService   
 * @author:      张联松(zhangls)
 * @date:        2020年9月14日 上午8:45:11
 */
public interface IReportFocusWorkflowService {
	/**
	 * 启动流程
	 * @param reportId		上报id
	 * @param userInfo		操作用户
	 * @param extraParam	额外信息
	 * 			formTypeId			表单类型id
	 * 			isStart				是否启动流程，true为启动流程，默认为false
	 * 			targetNextNodeName	下一环节名称，需要该环节为当前环节的可用下一环节，且该环节为人员办理，办理模板只有一个
	 * 			formType			表单类型，为空时，会通过formTypeId获取，targetNextNodeName有效时，启动该属性
	 * 			isCapDistributeUser	是否获取分送人员，默认为true，targetNextNodeName有效时，启动该属性
	 * 			isCapSelectUser		是否获取选送人员，默认为false，targetNextNodeName有效时，启动该属性
	 * 			
	 * @return 启动成功返回true，否则返回false
	 * @throws Exception
	 */
	public boolean startWorkflow4Report(Long reportId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 提交流程，节点扭转
	 * @param instanceId		流程实例id，由于多流程，无法通过业务id获取具体的实例信息
	 * @param nextNodeName		下一办理环节名称
	 * @param nextUserInfoList	下一办理环节用户
	 * @param userInfo			操作用户
	 * @param extraParam		额外信息
	 * 			advice			办理意见
	 * 			nextUserIds		下一环节办理人员id，以英文逗号分隔
	 * 			nextOrgIds		下一环节办理人员组织id，以英文逗号分隔
	 * @return 提交成功返回true，否则返回false
	 * @throws Exception
	 */
	public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 驳回流程，节点驳回
	 * @param instanceId	流程实例id，由于多流程，无法通过业务id获取具体的实例信息
	 * @param userInfo		操作用户
	 * @param extraParam	额外信息
	 * 			advice			驳回意见
	 * @return 驳回成功返回true，否则返回false
	 * @throws Exception
	 */
	public boolean rejectWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 撤回流程，只有当当前环节的上一个环节为task1时，发起人员才可以进行流程撤回
	 * @param instanceId	流程实例
	 * @param userInfo		操作用户
	 * @param extraParam	额外信息
	 * 必填参数
	 * 		reportId		报告id
	 * 		reportType		上报类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean recallWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 工作流办理初始化
	 * @param instanceId
	 * @param userInfo	操作用户信息
	 * @param params	额外参数
	 * 			isAble2Handle	是否进行待办初始化，true为是，false为否；为空时，判断当前操作用户是否为待办人员
	 * 			isCapTaskList	是否获取流程办理信息，true为是，默认为true
	 * 			isQuerySuperviseList	是否获取督办记录；true为获取；默认为false
	 * @return
	 * 			instanceId		流程实例id，类型Long
	 * 			formId			案件id，类型Long
	 * 			isAble2Handle	操作用户是否为当前办理人员，true为是，false为否
	 * 
	 * 			operateList		List<Map<String, Object>>
	 * 			nextTaskNodes	List<Map<String, Object>>
	 * 			taskList		List<Map<String, Object>>，属性参照方法findTaskInfoDetail；isCapTaskList为true时返回
	 * 			superviseList	List<Map<String, Object>>，属性参照方法cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService.findUrgeOrRemindList；isQuerySuperviseList为true是返回
	 * 
	 * 			WF_ORGID		当前办理人员组织Id
	 * 			WF_ORGNAME		当前办理人员组织名称
	 * 			WF_USERID		当前办理人员userId，以英文逗号连接
	 * 			WF_USERNAME		当前办理人员姓名
	 * 			WF_DBID_		当前节点任务
	 * 			WF_ACTIVITY_NAME_	当前节点任务名称
	 * 			NODE_NAME		当前节点编码
	 * 			NODE_ID			当前节点id
	 * 			INSTANCEID		流程实例id
	 * @throws Exception
	 */
	public Map<String, Object> initWorkflow4Handle(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception;
	
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
	 * 			isQuerySuperviseList	是否获取督办记录；true为获取；默认为true
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
	 * 		SUB_PRO_COUNT	子流程数量
	 * 		PARENT_PRO_TASK_ID	父流程任务id
	 * 		IS_SUPERVISED	任务是否被督办，true为是，isQuerySuperviseList为true时返回该属性
	 * @throws Exception 
	 */
	public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params) throws Exception;
	
	/**
	 * 获取流程实例信息
	 * @param reportId	上报id
	 * @param userInfo	操作用户信息
	 * @param params	额外信息
	 * 必填参数
	 * 			reportType		上报类型
	 * 
	 * 非必填参数
	 * 			instanceId		流程实例id，如果为空通过其他条件默认获取的是主流程的实例id
	 * 			reportId		上报id
	 * 			formId			表单id
	 * 			reportUUID		上报UUID
	 * 			workflowName	流程图名称
	 * 			wfTypeId		流程图类型
	 * @return
	 * 		instanceId	流程实例id，类型为Long
	 * 		curNodeName	当前办理环节名称，类型为String
	 * 		curOrgName	当前办理组织名称，多个值使用英文逗号分隔，有去重
	 * 		curUserName	当前办理用户姓名，多个值使用英文逗号分隔，有去重
	 * 		proName		实例名称，类型为String，多数等同于流程图名称
	 * 		creatorId	实例发起人员id，类型为Long
	 * 		creatorName	实例发起人员姓名，类型为String
	 * 		creatorOrgId	实例发起组织id，类型为Long
	 * 		creatorOrgName	实例发起组织名称，类型为String
	 * 		startTime	实例起始时间，类型为java.util.Date
	 * 		endTime		实例结束时间，类型为java.util.Date
	 * 		status		实例状态，1 处理中；2 已完结；4 被废除；
	 * @throws Exception
	 */
	public Map<String, Object> capProInstance(Long reportId, UserInfo userInfo, Map<String, Object> params) throws Exception;
}
