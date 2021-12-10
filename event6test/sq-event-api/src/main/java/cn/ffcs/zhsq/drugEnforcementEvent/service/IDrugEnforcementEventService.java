package cn.ffcs.zhsq.drugEnforcementEvent.service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.drugEnforcementEvent.DrugEnforcementEvent;

import java.util.List;
import java.util.Map;

/**
 * @Description: 禁毒事件，序列：SEQ_DRUG_ENFORCEMENT_ID模块服务
 * @Author: zhangls
 * @Date: 07-05 15:43:49
 * @Copyright: 2017 福富软件
 */
public interface IDrugEnforcementEventService {
	/**
	 * 状态
	 * @author zhangls
	 *
	 */
	public enum STATUS {	
		DRAFT("001", "草稿"),		
		HANDLING("002", "处理中"),
		HANDLED("004", "已处理");	
		
		private String status;
		private String statusName;
		
		private STATUS(String status, String statusName) {
			this.status = status;
			this.statusName = statusName;
		}
		
		public String getStatus() {
			return status;
		}
		
		public String getStatusName() {
			return this.statusName;
		}
		
		@Override
		public String toString() {
			return this.status;
		}
	}
	/**
	 * 新增或者更新禁毒事件
	 * drugEnforcementId为空时，新增；否则，进行更新
	 * @param drugEnforcementEvent
	 * @param userInfo
	 * @param extraParam
	 * 			attachmentIds	附件主键id，类型为Long[]
	 * @return
	 */
	public Long saveOrUpdateDrugEnforcementEvent(DrugEnforcementEvent drugEnforcementEvent, UserInfo userInfo, Map<String, Object> extraParam);

	/**
	 * 依据主键删除禁毒事件
	 * @param drugEnforcementId	主键
	 * @param delUserId			删除操作用户
	 * @return
	 */
	public boolean deleteDrugEnforcementEventById(Long drugEnforcementId, Long delUserId);

	/**
	 * 依据主键获取有效的禁毒事件
	 * @param drugEnforcementId
	 * @param orgCode	组织编码
	 * @return
	 */
	public DrugEnforcementEvent findDrugEnforcementEventById(Long drugEnforcementId, String orgCode);
	
	/**
	 * 获取禁毒事件记录总数
	 * @param userInfo
	 * 			userId	
	 * 			orgId
	 * 			orgCode
	 * @param params
	 * 			listType		列表类型 1 草稿；2 待办；3 辖区所有；默认为1
	 * 			userOrgCode		组织编码，为空时，使用userInfo中的orgCode
	 * 			workflowName	流程图名称，默认为 禁毒事件流程
	 * 			wfTypeId		流程图类型编码，默认为 drug_enforcement_event
	 * 			statusArray		状态，类型为String[]，优先于status生效，该参数只对经办列表、辖区所有列表有效
	 * 			status			状态，该参数只对经办列表、辖区所有列表有效
	 * 			handleDateStatus处置情况，01 正常；02 超时
	 * 			createDateStart	采集开始时间，字符串，格式为：yyyy-mm-dd
	 * 			createDateEnd	采集结束时间，字符串，格式为：yyyy-mm-dd
	 * @return
	 */
	public int findDrugEnforcementEventCount(Map<String, Object> params, UserInfo userInfo);
	
	/**
	 * 分页获取禁毒事件记录
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param userInfo
	 * 			userId	
	 * 			orgId
	 * 			orgCode
	 * @param params
	 * 			listType		列表类型 1 草稿；2 待办；3 辖区所有；默认为1
	 * 			userOrgCode		组织编码，为空时，使用userInfo中的orgCode
	 * 			workflowName	流程图名称，默认为 禁毒事件流程
	 * 			wfTypeId		流程图类型编码，默认为 drug_enforcement_event
	 * 			statusArray		状态，类型为String[]，优先于status生效，该参数只对经办列表、辖区所有列表有效
	 * 			status			状态，该参数只对经办列表、辖区所有列表有效
	 * 			handleDateStatus处置情况，01 正常；02 超时
	 * 			createDateStart	采集开始时间，字符串，格式为：yyyy-mm-dd
	 * 			createDateEnd	采集结束时间，字符串，格式为：yyyy-mm-dd
	 * @return
	 */
	public EUDGPagination findDrugEnforcementEventPagination(int pageNo, int pageSize, UserInfo userInfo, Map<String, Object> params);
	
	/**
	 * 上报禁毒事件
	 * @param drugEnforcementId	禁毒事件id
	 * @param userInfo
	 * @param extraParam
	 * 			isClose	是否归档，1 归档
	 * @return 上报成功，返回true；否则返回false
	 * @throws Exception
	 */
	public boolean reportDrugEnforcementEvent(Long drugEnforcementId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 办理禁毒事件
	 * @param drugEnforcementId	禁毒事件id
	 * @param nextNodeName		下一环节名称
	 * @param nextUserInfoList	下一环节办理人员 userId orgId，为空时，且userInfo不为空时，使用userInfo中的人员
	 * @param userInfo			当前用户信息
	 * @param extraParam
	 * 				nextUserIds		下一环节办理人员，以英文逗号分隔
	 * 				nextOrgIds		下一环节办理组织，以英文逗号分隔，与nextUserIds一一对应
	 * 				advice			办理意见
	 * 				isAutoSendSms	是否自动发送短信，true为自动发送，false不自动发送；默认为false
	 * @return
	 * @throws Exception
	 */
	public boolean subWorkflow(Long drugEnforcementId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 驳回禁毒事件至指定环节
	 * @param drugEnforcementId	禁毒事件id
	 * @param rejectToNodeName	驳回到的节点名称，为空时，驳回上一环节
	 * @param userInfo
	 * @param extraParam
	 * 				advice		驳回意见
	 * @return
	 * @throws Exception
	 */
	public boolean rejectWorkflow(Long drugEnforcementId, String rejectToNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 获取可办理下一环节
	 * @param drugEnforcementId	禁毒事件id
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
	public List<Map<String, Object>> findNextTaskNodes(Long drugEnforcementId, UserInfo userInfo, Map<String, Object> params) throws Exception;
	
	/**
	 * 获取可办理下一环节
	 * @param drugEnforcementId	禁毒事件id
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
	public List<Map<String, Object>> findNextTaskNodes(Long drugEnforcementId, UserInfo userInfo) throws Exception;
	
	/**
	 * 获取当前环节数据
	 * @param drugEnforcementId	禁毒事件id
	 * @param userInfo			用户信息
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findCurTaskData(Long drugEnforcementId, UserInfo userInfo) throws Exception;
	
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
	 * @param drugEnforcementId	禁毒事件id
	 * @param userInfo			用户信息
	 * @param extraParam
	 * @return 是当前办理人员，返回true；否则返回false
	 * @throws Exception 
	 */
	public boolean isCurTaskPaticipant(Long drugEnforcementId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
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
	 * @param drugEnforcementId	禁毒事件id
	 * @param userInfo			用户信息
	 * @return
	 * @throws Exception
	 */
	public Long capInstanceId(Long drugEnforcementId, UserInfo userInfo) throws Exception;
	
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
	 * @param drugEnforcementId	禁毒事件id
	 * @param userInfo			用户信息
	 * @return
	 * 		instanceId	流程实例id
	 * 		userId		流程创建用户id
	 * 		userName	流程创建用户姓名
	 * 		orgId		流程创建组织id
	 * 		orgName		流程创建组织名称
	 * @throws Exception
	 */
	public Map<String, Object> capProInstance(Long drugEnforcementId, UserInfo userInfo) throws Exception;
	
	/**
	 * 依据节点id获取该环节操作按钮
	 * @param nodeId	节点id
	 * @return
	 * 		operateEvent	按钮操作事件
	 * 		anotherName		按钮名称
	 */
	public List<Map<String, Object>> findOperateByNodeId(Integer nodeId);
	
	/**
	 * 获取短信内容
	 * @param drugEnforcementId	禁毒事件id
	 * @param curNodeName		当前环节名称
	 * @param nextTaskName		下一环节名称
	 * @param userInfo
	 * @param extraParam
	 * 			userOrgCode	组织编码，userInfo中未能获取组织编码时，使用该属性
	 * 			gridPath	网格路径，该属性为空时，使用禁毒事件中吸毒人员的所属网格路径
	 * @return
	 * @throws Exception 
	 */
	public String capSMSContent(Long drugEnforcementId, String curNodeName, String NextNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception;
	
	/**
	 * 发送短信
	 * @param userInfo
	 * @param params
	 * 			isAutoSendSms		是否自动发送短信，true为自动发送，false不自动发送；默认为false
	 * 			smsContent			短信内容，为空时，isAutoSendSms为true，自动获取短信内容
	 * 			formId				禁毒事件id
	 * 			curNodeName			当前环节名称
	 * 			nextNodeName		下一环节名称
	 * 			userOrgCode			组织编码，优先使用userInfo中的orgCode
	 * 			smsReceiveUserIds	接收短信的人员id，以英文逗号分隔
	 * 			otherMobileNums		接收短信的手机号码，以英文逗号分隔
	 * 			isCheckDuplication	消息中间记录新增时，是否进行判重；true 进行判重；false 不进行判重；默认为false
	 * 			msgSendMidBizId		消息中间表业务id，默认为使用formId
	 * 			msgSendMidBizType	消息中间表业务类型，默认为DRUG_ENFORCEMENT_EVENT
	 * 			msgSendMidSendType	消息中间表发送类型，默认为短信模板触发条件
	 * 			isMsgSendMidSubValid短信是否发送；true 发送；false 不发送；默认为true
	 * @throws Exception
	 */
	public boolean sendSMS(UserInfo userInfo, Map<String, Object> params) throws Exception;
	
}