package cn.ffcs.zhsq.event.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.NodeTransition;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.om.Remind;
import cn.ffcs.workflow.om.TaskReceive;
import cn.ffcs.workflow.om.Workflow;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;

public interface IEventDisposalWorkflowService {
	public static final int SQL_ORDER_ASC = 0;//升序排列
	public static final int SQL_ORDER_DESC = 1;//降序排列
	
	public static final String ACTOR_TYPE_ORG = "1";		//1表示ACTOR_ID为组织ID
	public static final String ACTOR_TYPE_ROLE = "2";		//2表示ACTOR_ID为角色ID
	public static final String ACTOR_TYPE_USER = "3";		//3表示ACTOR_ID为用户ID
	public static final String ACTOR_TYPE_POSITION = "4";	//4表示ACTOR_ID为职位ID

	/**
	 * 督促类别
	 * URGE 催办
	 * SUPERVISE 督办
	 *
	 * @author zhangls
	 *
	 */
	public static enum REMIND_CATEGORY {
		URGE("1","催办"),
		SUPERVISE("2","督办");

		private String category, categoryName;

		private REMIND_CATEGORY(String category, String categoryName) {
			this.category = category;
			this.categoryName = categoryName;
		}
		public String getCategory() {
			return category;
		}
		public String getCategoryName() {
			return categoryName;
		}
	}
	
	/**
	 * 流程启动
	 * @param workFlowId
	 * @param formId
	 * @param wftypeId
	 * @param user
	 * @param org
	 * @param userMap
	 * 					parentInstanceId	主流程实例id
	 * 					instanceTaskId		启动子流程的任务id
	 * @return
	 * @throws Exception 
	 */
	public String startFlowByWorkFlow(Long workFlowId, Integer formId, String wftypeId, UserBO user, OrgSocialInfoBO org, Map<String, Object> userMap) throws Exception;
	
	/**
	 * 流程启动
	 * @param eventId
	 * @param workFlowId 不知道多少就传-1L或者null
	 * @param toClose
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo user) throws Exception;
	
	/**
	 * 流程启动
	 * @param eventId
	 * @param workFlowId 不知道多少就传-1L或者null
	 * @param toClose
	 * @param user
	 * @param extraParma 额外参数信息
	 * 					advice 					办理意见 
	 * 					parentInstanceId	主流程实例id
	 * 					instanceTaskId		启动子流程的任务id
	 * @return
	 * @throws Exception 
	 */
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo user, Map<String, Object> extraParma) throws Exception;
	
	/**
	 * 流程启动并归档
	 * @param eventId
	 * @param workFlowId
	 * @param toClose
	 * @param userInfo
	 * @return
	 * @throws Exception 
	 */
	public String startEndWorkflowForEvent(Long eventId, Long workFlowId, UserInfo userInfo) throws Exception;
	/**
	 * 流程启动并归档
	 * @param eventMap
	 * @return
	 * @throws Exception 
	 */
	public String startEndWorkflowForEvent(Map<String, Object> eventMap) throws Exception;
	
	/**
	 * 初始化办理窗口
	 * @param taskId
	 * @param instanceId
	 * @param userInfo
	 * @param params 用于扩展的参数
	 * @return
	 * taskId 			当前任务ID
	 * operateLists 	操作按钮，类型为List<cn.ffcs.workflow.om.OperateBean>
	 * instanceId 		实例ID
	 * taskNodes 		任务节点
	 * deploymentId 	流程部署ID
	 * curNodeId 		当前节点ID
	 * curNodeName 		当前节点名称
	 * curNodeFormTypeId 
	 * curNode 			当前节点，类型为cn.ffcs.workflow.om.Node
	 * workflowCtx 		工作流域名访问前缀
	 * workFlowId		流程id
	 * backTask 
	 * proInstance 		流程实例信息，可用于获取事件发起人
	 * curForm 
	 * formUrl 
	 * taskDesc 
	 * event4Simple		事件对象(简化版)，类型为cn.ffcs.zhsq.mybatis.domain.event.EventDisposal
	 *  
	 */
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId, UserInfo userInfo, Map<String, Object> params);
	
	/**
	 * 工作流提交办理
	 * 可被如下接口替换
	 * subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception;
	 * @param instanceId 	实例Id
	 * @param taskId 		任务Id
	 * @param nextNodeName 	下一办理节点
	 * @param nextStaffId 	下一办理人ID
	 * @param curOrgIds		下一办理人组织ID
	 * @param advice 		办理意见
	 * @param userInfo
	 * @return
	 * @throws Exception 
	 */
	@Deprecated
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo) throws Exception;
	/**
	 * 工作流提交办理
	 * @param instanceId 	实例Id
	 * @param taskId 		任务Id
	 * @param nextNodeName 	下一办理节点
	 * @param nextStaffId 	下一办理人ID
	 * @param curOrgIds 	下一办理人组织ID
	 * @param advice 		办理意见
	 * @param userInfo 		操作用户
	 * @param smsContent	短信内容
	 * @param extraParam
	 * 			assistOrgNames	协办单位名称，多个值使用英文逗号连接
	 * 			nodeCode		办理环节节点编码，如U6R1U5
	 * 			isEvaluate		传入该参数并且为true时,增加评价信息；否则不增加评价信息
	 * @return
	 * @throws Exception
	 */
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception;
	/**
	 * 工作流提交办理
	 * @param params
	 * 			eventId			事件id
	 * 			instanceId 		流程实例id
	 * 			curTaskId		当前任务id
	 * 			nextNodeName 	下一办理节点
	 * 			nextUserIds 	下一办理人用户id，以英文逗号相连，与nextOrgIds一一对应
	 * 			nextOrgIds 		下一办理人组织id，以英文逗号相连，与nextUserIds一一对应
	 * 
	 * 			evaLevel		评价等级
	 * 			advice 			办理意见
	 * 			smsContent 		短信内容
	 * @param userInfo			当前登入用户信息
	 * @return
	 * @throws Exception 
	 */
	public boolean subWorkFlowForEndingAndEvaluate(Map<String, Object> params, UserInfo userInfo) throws Exception;
	
	/**
	 * 任务驳回，返回到上一办理步骤，而不是整体流程的上一步
	 * @param taskId
	 * @param instanceId
	 * @param advice 办理意见
	 * @param userInfo
	 * @return
	 * @throws Exception 
	 */
	public boolean rejectWorkFlow(String taskId, String instanceId, String advice, UserInfo userInfo) throws Exception;
	public boolean rejectWorkFlow(String taskId, String instanceId, String advice) throws Exception; 
	/**
	 * 任务驳回，返回到上一办理步骤，而不是整体流程的上一步
	 * @param params
	 * 			eventId			事件id
	 * 			instanceId		流程实例id
	 * 			curTaskId		当前任务id
	 * 
	 * 			nextNodeName	需要驳回到的节点名称
	 * 			advice			办理意见
	 * 			evaLevel		评价等级
	 * @param userInfo			当前登入用户
	 * @return
	 * @throws Exception 
	 */
	public boolean rejectWorkFlow(Map<String, Object> params, UserInfo userInfo) throws Exception;
	
	/**
	 * 流程环节撤回
	 * 要求撤回操作的用户是当前任务的上一任务办理人员，不只是经办人员
	 * 当前环节没有办理过，即没有被提交、驳回、反馈、处理中填写等，只是查看仍可撤回
	 * 
	 * @param instanceId	流程实例id
	 * @param userInfo		撤回操作用户
	 * @param params		额外参数
	 * 						eventId	事件id
	 * @return
	 * @throws Exception
	 */
	public boolean recallWorkflow(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception;
	
	/**
	 * 异步发送短信
	 * @param userIds 需要发送的用户id
	 * @param otherMobiles 额外发送短信的电话号码
	 * @param smsContent 短信内容
	 * @param userInfo
	 * @return
	 */
	public void sendSms(String userIds, String otherMobiles, String smsContent, UserInfo userInfo);
	/**
	 * 异步发送短信
	 * @param userIds 需要发送的用户id
	 * @param otherMobiles 额外发送短信的电话号码
	 * @param nextNodeName 下一环节名称
	 * @param instanceId 工作流实例id
	 * @param smsContent 短信内容
	 * @param userInfo
	 */
	public void sendSms(String userIds, String otherMobiles, String nextNodeName, Long instanceId, String smsContent, UserInfo userInfo);
	
	/**
	 * 异步发送短信
	 * @param userIds		短信接收人员
	 * @param otherMobiles	短信接收手机号码
	 * @param smsContent	短信内容
	 * @param userInfo		操作用户
	 * @param params		额外参数
	 * 			nextNodeName	下一环节名称
	 * 			instanceId		流程实例id
	 */
	public void sendSms(String userIds, String otherMobiles, String smsContent, UserInfo userInfo, Map<String, Object> params);
	
	/**
	 * 短信发送接口 同步发送
	 * @param userIds 需要发送的用户id
	 * @param otherMobiles 额外发送短信的电话号码
	 * @param smsContent 短信内容
	 * @param userInfo
	 */
	public boolean sendSmsSyn(String userIds, String otherMobiles, String smsContent, UserInfo userInfo);
	
	/**
	 * 获取短信发送内容
	 * @param taskId 当前任务id
	 * @param noteType 短信模板编号
	 * @param advice 办理意见
	 * @param event 事件对象
	 * @param userInfo 当前登入用户信息
	 * @return
	 */
	public String capSmsContent(String taskId, String noteType, String advice, EventDisposal event, UserInfo userInfo);
	
	/**
	 * 获取短信发送相关信息
	 * @param curNodeName				当前环节名称
	 * @param nextNodeName				下一环节名称
	 * @param params					其他信息
	 * 			eventId					事件id
	 * 			formId					表单id
	 * 			taskId					任务id
	 * 			instanceId				实例id
	 * 			receiverIds				短信接收人员id，以英文逗号连接
	 * 			receiverOrgIds			短信接收人员组织id，以英文逗号连接
	 * 			receiverMobilePhones	短信接收人员手机号码，以英文逗号连接
	 * 			isCapRemindedUser		是否获取被督办人员，true为是，默认为false
	 * 			isCapReceiverId			是否获取短信接收人员id，默认为false
	 * 			isCapReceiverMobilePhone 是否获取短信接收手机号码，默认为true
	 * @param userInfo					用户信息
	 * 			orgCode					组织编码
	 * @return
	 * 			smsContent				短信内容
	 * 			receiverMobilePhones	接收短信人员手机号码，以英文逗号连接
	 * 			remindedUserName		被督办人员姓名，isCapRemindedUser为true时返回；当被督办人员为组织时，返回的是组织名称
	 * 			receiverIds				短信接收人员id，以英文逗号连接，isCapReceiverId为true时返回，默认为false
	 * 			receiverMobilePhones	短信接收手机号码，以英文逗号连接，isCapReceiverMobilePhone为true时返回，默认为true
	 * @throws Exception 
	 */
	public Map<String, Object> capInfo4SMS(String curNodeName, String nextNodeName, Map<String, Object> params, UserInfo userInfo) throws Exception;
	
	/**
	 * 获取处理环节详情
	 * @param instanceId 工作流实例ID
	 * @return
	 *  催办列表(督办列表)		remindList(督办人：REMIND_USER_NAME；督办时间：REMIND_DATE_；督办意见：REMARKS)
	 *  子任务列表(会签任务、处理中任务) subTaskList(详见querySubTaskDetails)
		任务ID				TASK_ID
		任务名称				TASK_NAME
		任务类型				TASK_TYPE
		操作类型				OPERATE_TYPE('0' 开始流程, '1' 审核通过, '2' 驳回, '3' 下一步, '4' 暂存)
		办理意见				REMARKS
		任务开始时间			START_TIME 
		任务结束时间			END_TIME
		办理人ID				TRANSACTOR_ID
		办理人名字				TRANSACTOR_NAME
		办理部门				ORG_NAME
		任务办理时长			INTER_TIME
		业务表单				FORM_ID
		是否超时				ISTIMEOUT ('0'未超时, '1'超时）
		是否超时名称			ISTIMEOUTNAME
		子任务条数				SUB_COUNT
	 */
	@Deprecated
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId);
	/**
	 * 按照指定顺序获取处理环节详情
	 * @param instanceId 工作流实例ID
	 * @param order 指定查询顺序 0 SQL_ORDER_ASC 升序；1 SQL_ORDER_DESC 降序；默认升序
	 * @return
	 * 		PARENT_TASK_ID	上一任务id
	 * 		TASK_ID			任务id
	 * 		TASK_TYPE		任务类型
	 * 		TASK_NAME		环节中文名称
	 * 		TASK_CODE		环节编码，如task1、task2
	 * 		NODE_CODE		节点编码，如JA、PJ、U6
	 * 		PRE_TASK_CODE	上一环节编码
	 * 		OPERATE_TYPE	操作类型；1 提交；2 驳回
	 * 		TRANSACTOR_ID	任务办理人员id
	 * 		TRANSACTOR_NAME	任务办理人员姓名
	 * 		ORG_NAME		任务办理人员组织名称
	 * 		ORG_ID			任务办理人员组织id
	 * 		NODE_ID			任务对应的节点id
	 * 		REMARKS			任务办理意见
	 * 		START_TIME		任务开始时间文本，格式为yyyy-MM-dd HH:mm:ss
	 * 		END_TIME		任务办结时间文本，格式为yyyy-MM-dd HH:mm:ss
	 */
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int order);
	/**
	 * 按照指定顺序获取处理环节详情
	 * @param instanceId 工作流实例ID
	 * @param order 指定查询顺序 0 SQL_ORDER_ASC 升序；1 SQL_ORDER_DESC 降序；默认升序
	 * @param userInfo 当前登入用户信息
	 * @return
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
	 * 
	 */
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int order, UserInfo userInfo);
	
	/**
	 * 根据taskId查询MyTask的子信息
	 * @param taskId
	 * @param order 为null时，表示降序
	 * @param userInfo
	 * @return
	 * 
	 * PARENT_TASK_ID		父任务ID
	 * TASK_ID				子任务ID
	 * TASK_TYPE			任务类型('2' 会签任务；'3' 处理中任务)
	 * TASK_NAME			任务名称
	 * OPERATE_TYPE			操作类型
	 * TRANSACTOR_ID		办理人ID
	 * TRANSACTOR_NAME		办理人姓名
	 * ORG_NAME				办理部门
	 * ORG_ID				办理部门ID
	 * REMARKS				办理意见
	 * START_TIME			任务开始时间
	 * END_TIME				任务结束时间
	 */
	public List<Map<String, Object>> querySubTaskDetails(String taskId, int sqlOrder, UserInfo userInfo);
	
	/**
	 * 新增催办信息
	 * @param instanceId
	 * @param taskId
	 * @param userInfo
	 * @param remarks 催办意见
	 * @param userIds
	 * @param userNames
	 * @return
	 */
	public boolean addRemind(Long instanceId, Long taskId, UserInfo userInfo, String remarks, String userIds, String userNames);
	/**
	 * 新增催办信息
	 * @param instanceId
	 * @param taskId
	 * @param userInfo
	 * @param remarks 催办意见
	 * @param userIds
	 * @return
	 */
	public boolean addRemind(Long instanceId, Long taskId, UserInfo userInfo, String remarks, String userIds);

	/**
	 * 增添催办/督办记录
	 * @param instanceId	流程实例id
	 * @param userInfo		操作用户信息
	 * @param params		额外参数
	 * 	必填参数
	 * 					eventId						事件id
	 * 					category					记录类型，IEventDisposalWorkflowService.REMIND_CATEGORY
	 * 					remindUserId				操作用户id，为空时，使用userInfo中的userId
	 * 					remindUserPartyName			操作用户姓名，为空时，使用userInfo中的userPartyName
	 *
	 * category为IEventDisposalWorkflowService.REMIND_CATEGORY.URGE时的必填参数
	 * 					curTaskId					当前任务id，为空时使用instanceId进行转换
	 * 					remindedUserId				被催办人员id，多个值使用英文逗号分隔，为空时使用curTaskId转换
	 * 					remindedUserPartyName		被催办人员姓名，多个值使用英文逗号分隔，为空时使用curTaskId转换
	 * 
	 * 非必填参数
	 * 					formId						事件id
	 * 					supervisionType				督办类型,3：督办-普通-黑牌；1：督办-黄牌；2：督办-红牌。字典：A001093091；默认为 2
	 * 					remindRemark				催办/督办意见
	 * 					remarks						催办/督办意见，为了兼容历史，remarks优先remindRemark生效
	 * 					isAddAttention				是否添加关注信息，true为是，默认为true
	 * 					smsContent					短信内容，不为空时表示需要发送短信
	 * 					otherMobileNums				其他短信接收手机号码，多个值使用因为逗号进行分隔
	 * 					
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean addRemind(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception;
	
	/**
	 * 依据任务Id获取催办信息列表
	 * @param taskId
	 * @return
	 */
	public List<Remind> queryRemindListByTaskId(Long taskId);
	
	/**
	 * 新增督办信息
	 * @param instanceId
	 * @param taskId 任务ID，非必传项，可传null
	 * @param userInfo
	 * @param remarks 督办意见
	 * @param userIds
	 * @param userNames
	 * @return
	 */
	@Deprecated
	public boolean supervise(Long instanceId, Long taskId, UserInfo userInfo, String remarks, Long[] userIds, String[] userNames);
	/**
	 * 新增督办信息
	 * @param instanceId
	 * @param taskId 任务ID，非必传项，可传null
	 * @param userInfo
	 * @param remarks 督办信息
	 * @param userIds
	 * @param userNames
	 * @return
	 */
	@Deprecated
	public boolean supervise(Long instanceId, Long taskId, UserInfo userInfo, String remarks, String userIds, String userNames);
	/**
	 * 新增督办信息
	 * @param instanceId
	 * @param taskId 任务ID，非必传项，可传null
	 * @param userInfo
	 * @param remarks 督办意见
	 * @param userIds
	 * @return
	 */
	@Deprecated
	public boolean supervise(Long instanceId, Long taskId, UserInfo userInfo, String remarks, String userIds);
	
	/**
	 * 添加关注信息
	 * @param eventId
	 * @param formTypeId
	 * @param userInfo
	 * @param date
	 * @return
	 */
	public boolean addAttention(Long eventId, String formTypeId, UserInfo userInfo, Date date);
	/**
	 * 添加关注信息
	 * @param eventId
	 * @param userInfo
	 * @return
	 */
	public boolean addAttention(Long eventId, UserInfo userInfo);
	
	/**
	 * 查询当前环节的办理人员信息
	 * @param taskId 当前环境任务ID
	 * @return 
	 * 		USER_TYPE	用户类型，1 办理人员为组织；3 办理人员为用户;
	 * 		USER_ID 	用户ID
	 * 		USER_NAME	用户姓名
	 * 		ORG_ID 		用户所在组织ID
	 * 		ORG_NAME 	用户所在组织名称
	 * 		ORG_CODE	用户所在组织编码
	 */
	public List<Map<String, Object>> queryMyTaskParticipation(String taskId);
	
	/**
	 * 获取当前节点的信息
	 * @param instanceId 实例Id
	 * @return 
	 * WF_ORGID 			当前办理人员组织id，多个值以英文逗号连接，有去重
	 * WF_ORGID_ALL			当前办理人员组织id，多个值以英文逗号连接，没有去重
	 * WF_ORGNAME 			当前办理人员组织名称，多个值以英文逗号连接，有去重
	 * WF_ORGNAME_ALL		当前办理人员组织名称，多个值以英文逗号连接，没有去重
	 * WF_USERID  			当前办理人员用户Id，多个值以英文逗号连接，有去重
	 * WF_USERID_ALL		当前办理人员用户Id，多个值以英文逗号连接，没有去重
	 * WF_USERNAME  		当前办理人员partyName，多个值以英文逗号连接，有去重
	 * WF_USERNAME_ALL		当前办理人员partyName，多个值以英文逗号连接，没有去重
	 * WF_DBID_  			当前节点任务
	 * WF_ACTIVITY_NAME_	当前节点任务中文名称
	 * NODE_NAME 			当前节点编码
	 */
	public Map<String, Object> curNodeData(Long instanceId);
	
	/**
	 * 获取当前节点的当前办理人userId，如果当前用户为组织id，则获取该组织下的所有用户
	 * @param instanceId 实例Id
	 * @return
	 */
	public String curNodeUserIds(String taskId);
	public String curNodeUserIds(Long instanceId);
	public String curNodeUserIds(List<Map<String, Object>> taskPerson);
	
	/**
	 * 获取当前节点的当前办理人姓名
	 * @param instanceId 实例ID
	 * @return
	 */
	public String curNodeUserNames(Long instanceId);

	public String curNodeUserNames(String userIds);
	
	/**
	 * 获取当前节点的taskId
	 * @param instanceId 实例ID
	 * @return
	 */
	public Long curNodeTaskId(Long instanceId);
	
	/**
	 * 获取当前节点的任务中文名称
	 * 流程结束，当前环节为空时，抛出异常
	 * @param instanceId 实例ID
	 * @return
	 * @throws Exception 
	 */
	public String curNodeTaskName(Long instanceId) throws Exception;
	
	/**
	 * 针对事件流程，判断当前节点和下一个节点之间是属于什么关系类型
	 * @param curnodeName 当前节点名称
	 * @param nextNodeName 下一节点名称
	 * @param userInfo 当前登入用户信息
	 * @return 0-分流， 1-上报，2-越级
	 */
	public Map<String, Object> capTurnLevel(String curnodeName, String nextNodeName, UserInfo userInfo);
	
	/**
	 * 依据当前节点名称，获取下一节点名称
	 * @param curNodeName 当前节点名称
	 * @param level 0-分流， 1-上报，2-越级
	 * @return 下一节点名称
	 */
	public String capNextNodeName(String curNodeName, int level);
	
	/**
	 * 获取工作流id
	 * @param instanceId	流程实例id
	 * @param eventId		事件Id
	 * @param userInfo		用户信息
	 * @param extraParam	额外参数
	 * 						workflowName	流程图名称
	 * @return 获取失败返回-1，否则为正常的工作流id
	 */
	public Long capWorkflowId(Long instanceId, Long eventId, UserInfo userInfo, Map<String, Object> extraParam);
	
	/**
	 * 获取工作流流程图名称
	 * @param eventId		事件id
	 * @param eventType		事件类别
	 * @param userInfo		用户信息
	 * @return
	 */
	public String capWorkflowName(Long eventId, String eventType, UserInfo userInfo);

	/**
	 * 获取事件工作流办理页面
	 * @param eventId		事件id
	 * @param eventType		事件类别
	 * @param userInfo		用户信息
	 * @param extraParam
	 * @return
	 */
	public String capWorkflowHandlePage(Long eventId, String eventType, UserInfo userInfo, Map<String, Object> extraParam);
	
	/**
	 * 通过instanceId判断是否启用新工作流
	 * @param instanceId
	 * @return
	 */
	public boolean isNewWorkflow(Long instanceId);
	public boolean isNewWorkflow(String instanceId);
	
	/**
	 * 新增或更新事件工作流任务
	 * 对于处理中任务(type不为空时)，taskId是作为该条任务的parentTaskId使用的
	 * 当type为空时，taskId不为空，则是对该任务进行更新操作
	 * @param params
	 * @return 新增成功返回任务id，否则返回-1
	 */
	public Long saveOrUpdateTask(Map<String, Object> params);
	
	/**
	 * 判断用户是否是当前办理人员
	 * 如果taskId不是当前任务，则直接返回false
	 * 如果instanceId不能转为有效的Long数值，则直接返回false
	 * @param taskId		任务id
	 * @param instanceId	流程实例id
	 * @param userInfo		用户信息
	 * @return
	 * @throws Exception 
	 */
	public boolean isUserInfoCurrentUser(String taskId, String instanceId, UserInfo userInfo) throws Exception;
	
	/**
	 * 判断用户是否是当前办理人员
	 * 如果taskId不是当前任务，则直接返回false
	 * @param taskId		任务id
	 * @param instanceId	流程实例id
	 * @param userInfo		用户信息
	 * @return
	 * @throws Exception 
	 */
	public boolean isUserInfoCurrentUser(String taskId, Long instanceId, UserInfo userInfo) throws Exception;
	
	/**
	 * 判断当前用户是否可启动工作流
	 * @param orgSocialInfo
	 * @param userId
	 * @return 当组织层级为6且用户不为网格员时，返回false
	 * @throws Exception 
	 */
	public boolean isUserAbleToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception;
	/**
	 * 判断当前用户是否可启动工作流
	 * @param orgId	组织id
	 * @param userId	用户id
	 * @return
	 * @throws Exception
	 */
	public boolean isUserAbleToStartWorkflow(Long orgId, Long userId) throws Exception;
	
	/**
	 * 判断当前用户是否可启动工作流
	 * @param orgSocialInfo
	 * @param userId
	 * @return -1 当前组织类型不是单位，也不是部门；-2 当前用户不是网格员；-3 当前组织类型不是单位；-4当前组织类型不是部门；0 当前组织不存在；1 正常
	 */
	public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception;
	/**
	 * 判断当前用户是否可启动工作流
	 * @param orgId		组织id
	 * @param userId	用户id
	 * @return
	 * @throws Exception
	 */
	public int checkUserToStartWorkflow(Long orgId, Long userId) throws Exception;
	
	/**
	 * 对已启动的事件进行，结案并归档操作
	 * 如果事件下一环节中存在节点编码为JA的节点，则优先结案，而后在归档，归档节点的节点编码为end1
	 * 
	 * @param params
	 * 			eventId			事件id
	 * 			taskId			当前任务id
	 * 			nextNodeName	下一办理环节名称，为空时，则下一环节为JA节点
	 * 			userInfo		用户信息，类型为cn.ffcs.uam.bo.UserInfo
	 * 			closerOrgId		结案操作人员组织id
	 * 			closerUserId	结案操作人员id
	 * 
	 * 必填参数
	 * 			instanceId		流程实例id
	 * 			closerName		结案操作人员姓名，为空时，使用userInfo的partyName属性
	 * 			closerOrgName	结案操作人员组织名称，为空时，使用userInfo的orgName属性
	 * 			
	 * @return
	 */
	public boolean archiveAndEndWorkflowForEvent(Map<String, Object> params);
	
	/**
	 * 对已启动的事件进行，结案并归档操作
	 * 如果事件下一环节中存在节点编码为JA的节点，则优先结案，而后在归档，归档节点的节点编码为end1
	 * 
	 * @param eventId		事件id
	 * @param userInfo		办理用户信息
	 * @param advice		办理意见
	 * @param extraParam	额外参数
	 * 			instanceId	流程实例id
	 * 			taskId		当前任务id
	 * 			nextNodeName下一办理环节名称
	 * 			closerUserId结案操作人员id
	 * 			closerOrgId	结案操作人员组织id
	 * @return
	 */
	public boolean archiveAndEndWorkflowForEvent(Long eventId, UserInfo userInfo, String advice, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 对已启动的事件进行，结案并归档操作
	 * 如果事件下一环节中存在节点编码为JA的节点，则优先结案，而后在归档，归档节点的节点编码为end1
	 * 
	 * @param instanceId	实例id
	 * @param taskId		当前任务id
	 * @param userName		办理人员姓名
	 * @param orgName		办理人员组织名称
	 * @param advice		办理意见
	 * @param extraParam	额外参数
	 * 			nextNodeName下一办理环节名称
	 * 			closerUserId结案操作人员id
	 * 			closerOrgId	结案操作人员组织id
	 * @return
	 */
	public boolean archiveAndEndWorkflowForEvent(Long instanceId, String taskId, String userName, String orgName, String advice, Map<String, Object> extraParam);
	
	/**
	 * 依据节点id查找相关节点连线信息
	 * @param nodeId	节点ID
	 * @return
	 * 		ACTOR_TYPE	类型("1", "组织机构";"2", "角色";"3", "用户";"4", "职位";"5", "职能";"6", "专业")
     *      ACTOR_ID	ID
     *      ACTOR_NAME	名称   
     *      ORG_ID		组织id，ACTOR_TYPE为1时
     *      ROLE_ID		角色id，ACTOR_TYPE为2时
     *      USER_ID		用户id，ACTOR_TYPE为3时
     *      POSITION_ID	职位id，ACTOR_TYPE为4时
	 */
	public List<Map<String, Object>> findNodeActorsById(Integer nodeId);
	
	/**
	 * 依据节点id查找相关节点信息
	 * @param nodeId	节点id
	 * @return
	 */
	public Node findNodeById(Integer nodeId);
	/**
	 * 依据实例、节点名称查找相关节点信息
	 * @param instanceId	实例id
	 * @param nodeName		节点名称
	 * @return
	 */
	public Node findNodeById(Long instanceId, String nodeName);
	
	/**
	 * 通过eventId获取工作流(主)实例id
	 * @param eventId
	 * @return
	 */
	public String capInstanceIdByEventId(Long eventId);
	/**
	 * 通过eventId获取工作流实例id
	 * @param eventId	事件id
	 * @param params
	 * 						eventType			todo 待办，如果有启动子流程，则返回子流程实例id；否则返回主流程实例id
	 * @return
	 */
	public String capInstanceIdByEventId(Long eventId, Map<String, Object> params);
	
	/**
	 * 通过eventId获取工作流(主)实例id
	 * @param eventId
	 * @return 获取失败时，返回-1；否则，返回Long的结果
	 */
	public Long capInstanceIdByEventIdForLong(Long eventId);
	/**
	 * 通过eventId获取工作流实例id
	 * @param eventId
	 * @param params
	 * 						eventType			todo 待办，如果有启动子流程，则返回子流程实例id；否则返回主流程实例id
	 * @return 获取失败时，返回-1；否则，返回Long的结果
	 */
	public Long capInstanceIdByEventIdForLong(Long eventId, Map<String, Object> params);
	
	/**
	 * 通过事件id获取(主)流程实例
	 * @param eventId
	 * @return 获取失败时，返回null
	 */
	public ProInstance capProInstanceByEventId(Long eventId);
	/**
	 * 通过事件id获取流程实例
	 * @param eventId
	 * @param params
	 * 						eventType			todo 待办，如果有启动子流程，则返回子流程实例；否则返回主流程实例
	 * @return
	 */
	public ProInstance capProInstanceByEventId(Long eventId, Map<String, Object> params);
	
	/**
	 * 通过流程实例id获取流程实例
	 * @param instanceId
	 * @return　获取失败时，返回null
	 */
	public ProInstance capProInstanceByInstanceId(Long instanceId);
	
	/**
	 * 通过任务id获取表单信息
	 * @param taskId	任务id
	 * @param params	额外参数
	 * 			taskType	任务类型：0 待办任务；1 历史任务；未设置该属性时，优先转换待办任务；
	 * @return
	 * 		formId		表单id
	 * 		formTypeId	表单类型
	 * 		instanceId	实例id
	 * 		taskType	任务类型
	 * @throws Exception
	 */
	public Map<String, Object> findFormByTaskId(Long taskId, Map<String, Object> params) throws Exception;
	
	/**
	 * 获取某个经办任务的信息
	 * @param instanceId 流程实例
	 * @param taskNodeCode 环节节点编码
	 * @return 返回null表示该流程实例没有经过此环节
	 * map.get("tasks") List<MyTask>类型
	 */
	public Map<String, Object> capDoneTaskInfo(Long instanceId, String taskNodeCode);
	
	/**
	 * 获取最近办理的任务信息
	 * 依据endTime降序排列
	 * @param instanceId	流程实例id
	 * @param taskNodeCode	环节节点编码
	 * @return 未找到相关任务时，返回null
	 * 		taskId			任务id
	 * 		taskNodeCode	环节节点编码
	 * 		operateType		操作类型名称 1、通过、2、驳回、3、返回上一步，值为中文，可通过OperateType.getIndexByName转换为对应的操作类型
	 * 		orgId			办理人员组织id
	 * 		orgName			办理人员组织名称
	 * 		transactorId	办理人员id
	 * 		transactorName	办理人员姓名
	 */
	public Map<String, Object> findDoneTaskInfoLatest(Long instanceId, String taskNodeCode);
	
	/**
	 * 任务转派，更改当前任务办理人员
	 * 更改后，以前的当前任务办理人员全部移除，替换为现在的办理人员
	 * @param params
	 * 	taskId		当前taskId
	 * 	instanceId	流程实例id
	 * 	userIds		转派人员id串，以英文逗号分隔
	 * 	userNames	转派人员name串，以英文逗号分隔
	 * 	orgIds		转派人员组织id串，以英文逗号分隔
	 * 	orgNames	转派人员组织name串，以英文逗号分隔
	 * 	userId		当前办理人
	 * 	userName	当前办理人名称
	 * 	remarks		备注
	 * @return
	 * 	result: true,false
	 * 	msg: true时候msg为"",false时候msg为异常信息
	 */
	public Map<String, Object> turnTodoPerson(Map<String, Object> params);
	
	/**
	 * 所有流程实例
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * 		userId	当前系统用户Id(必填)
	 * 		userName	(发起人名字可为null)
	 * 		curUserId	任务办理人Id
	 * 		curOrgId	任务办理人组织id
	 * 		status		（流程状态可为null)
	 * 		eventTable	 --ConstantValue.DATABASETABLESPACE + ".T_TASKMNG"（数据库名.表名）
	 * 		eventTablesql --"E.TASK_ID, E.TASK_NAME, E.TASK_SN, E.BSTYPE, E.PRIORITY, E.INDICATORS, E.DESC_, E.USERID"（自己的表单在列表上返回的对象）
	 * 		eventTableRelevance--"E.TASK_ID"（关联工作流的表单字段）
	 * 		formTypeId	表单类型
	 * 		eventTableSelectsql--" AND E.USERID=184 AND E.TASK_NAME LIKE '%dasdas%' AND E.BSTYPE = '1'"（自己表单的查询sql）
	 * 		attentionType	关注状态("1"表示过滤至关注状态)
	 * 		orderSQL	字段 --"E.TASK_ID DESC,E.TASK_NAME DESC", 可以为null表示按催办时间 升序,流程状态升序,任务到达时间 降序
	 * @return
	 * 		WF_INSTANCE_ID,REMIND_MARK,SUPERVISE_MARK,WF_WORKFLOW_ID,WF_FORM_ID,WF_STATUS,WF_PROC_NAME,WF_USER_NAME,WF_ORG_NAME,
	 * 		WF_REMIND_DATE,WF_REMIND_STATUS,WF_MAX_DBID_,WF_CREATE_ ,WF_END_,WF_USER_ORG,WF_TIME_LIMIT_STATUS,WF_ATTENTION_STATUS,
	 * 		WF_USERNAME,WF_ORGNAME,WF_ACTIVITY_NAME_,业务表单字段
	 */
	public EUDGPagination queryTaskMngList(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 添加任务接收时间
	 * 判重条件：taskId、userId、orgId
	 * @param taskReceive
	 * 必填属性:
	 * 			taskId:		任务Id
	 * 			instanceId:	流程Id
	 * 			userId:		用户Id
	 * 			userName:	用户名
	 * 			orgId:		组织Id
	 * 			orgName:	组织名
	 * 			receiveTime:接收时间
	 * 非必填属性:
	 * 			remark:接收意见(预留接口)
	 * @param userInfo		用户信息
	 * @param extraParam	额外参数
	 * 			eventId		事件Id
	 * @return true：接收成功；false：接收失败
	 */
	public boolean receiveTask(TaskReceive taskReceive, UserInfo userInfo, Map<String, Object> extraParam);
	
	/**
	 * 依据任务id和实例id获取任务接收列表
	 * @param taskId		任务id
	 * @param instanceId	实例id
	 * @param extraParam	额外参数
	 * 			eventId		事件id
	 * @return
	 */
	public List<TaskReceive> findTaskReceivedList(Long taskId, Long instanceId, Map<String, Object> extraParam);
	
	/**
	 * 获取流程信息
	 * @param workflowId	流程id，为null时，表示查找所有的流程信息
	 * @return
	 */
	public Workflow queryWorkflowById(Long workflowId);
	/**
	 * 获取流程信息
	 * @param workflow	流程信息设置，为null时，表示查找所有的流程信息
	 * @return
	 * 			默认返回流程状态为1的流程信息
	 */
	public List<Workflow> queryWorkflows(Workflow workflow);
	
	/**
	 * 获取流程节点
	 * @param workflowId	流程id
	 * @return
	 */
	public List<Node> queryNodes(Long workflowId);
	/**
	 * 获取流程节点
	 * @param workflowId	流程Id
	 * @param deploymentId	部署id
	 * @return
	 */
	public List<Node> queryNodes(Long workflowId, Integer deploymentId);
	/**
	 * 获取流程节点
	 * @param node，为null时，表示查找所有的流程节点信息
	 * @return
	 */
	public List<Node> queryNodes(Node node);
	
	/**
	 * 获取两点之间的线属性
	 * @param curNodeId		当前节点id
	 * @param nextNodeId	下一节点id
	 * @return
	 */
	public NodeTransition findNodeTransition(Integer curNodeId, Integer nextNodeId);
	
	/**
	 * 获取督促记录
	 * @param params
	 * 			instanceId	流程实例id，Long
	 * 			taskId		任务id，Long
	 * 			category	记录类型，1 催办；2 督办；String。可使用枚举对象IEventDisposalWorkflowService.REMIND_CATEGORY
	 * 			userOrgCode	组织编码
	 * @return
	 * 		Long remindId			主键，督促id
	 * 		Long instanceId			流程实例id
	 * 		Long taskId				任务ID
	 * 		Long remindUserId		督促人员id
	 * 		String remindUserName	督促人员姓名
	 * 		Long remindOrgId		督促组织id
	 * 		String remindOrgName	督促组织名称
	 * 		Date remindDate			督促时间
	 * 		String remindDateStr	督促时间文本，格式为：yyyy-mm-dd hh:mm:ss
	 * 		Date endRemindDate		督促结束时间
	 * 		String category			督促类型，1 催办；2 督办；
	 * 		String remarks			督促意见
	 * 		Long remindedUserId		被督促人员id
	 * 		String remindedUserName	被督促人员姓名
	 * 		Long remindedOrgId		被督促组织id
	 * 		String remindedOrgName	被督促组织名称
	 * @throws Exception
	 */
	public List<Map<String, Object>> findRemindList(Map<String, Object> params) throws Exception;
	
	/**
	 * 删除督促记录
	 * @param params
	 * 必填参数
	 * 			instanceId		流程实例id，Long
	 * 			taskId			任务id，Long，category为1时必填
	 * 			category		记录类型，1 催办；2 督办；String。可使用枚举对象IEventDisposalWorkflowService.REMIND_CATEGORY
	 * 
	 * 非必填参数
	 * 			eventId			事件id，Long，可转换得到instanceId，taskId
	 * 			reason			删除原因，String
	 * 			updaterId		删除人员id，Long，为空时，使用userInfo中的userId
	 * 			updaterName		删除人员姓名，String，为空时，使用userInfo中的partyName
	 * 			
	 * 			isDel4Own		是否只删除自己有关的督促记录，true为是；默认为false
	 * 			remindUserId	督促办理人员id，isDel4Own为true时生效
	 * @param userInfo		操作用户信息
	 * @return 成功删除的记录数
	 * @throws Exception
	 */
	public int delRemind(Map<String, Object> params, UserInfo userInfo) throws Exception;
	
	/**
	 * 获取督促废除记录
	 * @param params
	 * 			instanceId	流程实例id，Long
	 * 			taskId		任务id，Long
	 * 			category	记录类型，1 催办；2 督办；String。可使用枚举对象IEventDisposalWorkflowService.REMIND_CATEGORY
	 * 			userOrgCode	组织编码
	 * @return
	 * 		Long remindId			主键，督促id
	 * 		Long instanceId			流程实例id
	 * 		Long taskId				任务ID
	 * 		Long remindUserId		督促人员id
	 * 		String remindUserName	督促人员姓名
	 * 		Long remindOrgId		督促组织id
	 * 		String remindOrgName	督促组织名称
	 * 		Date remindDate			督促时间
	 * 		String remindDateStr	督促时间文本，格式为：yyyy-mm-dd hh:mm:ss
	 * 		Date endRemindDate		督促结束时间
	 * 		String category			督促类型，1 催办；2 督办；
	 * 		String remarks			督促意见
	 * 		Long remindedUserId		被督促人员id
	 * 		String remindedUserName	被督促人员姓名
	 * 		Long remindedOrgId		被督促组织id
	 * 		String remindedOrgName	被督促组织名称
	 * 		String reason			取消督促原因
	 * 		String validStatus		记录状态，1 有效；0 无效；
	 * 		Long removeUserId		督促记录移除人员id
	 * 		String removeUserName	督促记录移除人员姓名
	 * 		Long removeOrgId		督促记录移除组织id
	 * 		String removeOrgName	督促记录移除组织名称
	 * 		Date removeDate			督促记录移除时间
	 * 		String removeDateStr	督促记录移除时间文本，格式为：yyyy-mm-dd hh:mm:ss
	 * @throws Exception 
	 */
	public List<Map<String, Object>> findRemindInvalidList(Map<String, Object> params) throws Exception;
	
	/**
	 * 获取推送流程信息的mq初始值
	 * @param 查询参数		
	 * @return
	 */
	public Map<String,Object> findRmqInitInfo(Map<String,Object> params,String orgCode);

	/**
	 * 新增处理中办理过程
	 * @param eventId
	 * @param instanceId
	 * @param taskName
	 * @param advice
	 * @param userInfo
	 * @return taskId duedate
	 */
	public Map<String, Object> saveHandlingTask(Long eventId, Long instanceId, String taskName, String advice, UserInfo userInfo, Map<String, Object> extraParam);
}
