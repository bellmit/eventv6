package cn.ffcs.zhsq.event.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;

/**
 * 事件对接接口
 * @author zhangls
 *
 */
public interface IEventDisposalDockingService {
	/**
	 * 关联T_SJ_EVENT_INTER无分页获取迪爱斯结案事件列表信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findClosedEventWithEventInter(Map<String, Object> params);
	
	/**
	 * 关联T_SJ_EVENT_INTER无分页获取上报到迪爱斯事件列表信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findReportEventWithEventInter(Map<String, Object> params);
	
	/**
	 * 事件上报
	 * @param event
	 * @param userInfo
	 * @param advice 办理意见
	 * @return eventId 事件Id;instanceId 流程实例Id;userIds 下一节点办理人员userId;result 办理结果 true为办理成功、false为失败；
	 */
	@Deprecated
	public Map<String, Object> saveEventDisposalAndReport(EventDisposal event, UserInfo userInfo, String advice);
	
	/**
	 * 事件上报
	 * @param event		事件信息
	 * @param params	
	 * 			advice	办理意见
	 * 			isInvolvedPeopleAltered 值为true时参数peopleListJson生效
	 * 			peopleListJson 涉及人员信息（json格式）
	 *							name  		涉及人员姓名
	 *							cardType  	涉及人员证件类型(证件类型字典编码(D030001))
	 *							idCard      涉及人员证件号码
	 *							tel			涉及人员联系方式
	 *							ciRsId		涉及人员人口id
	 *			involvedNumInt;//涉及人数(目前供晋江手动输入涉案人数用) INVOLVED_NUM_INT
	 *
	 * @param userInfo	操作用户
	 * @return
	 * 		eventId		事件Id;
	 * 		instanceId 	流程实例Id;
	 * 		userIds 	下一节点办理人员userId;
	 * 		result 		办理结果 true为办理成功、false为失败；
	 */
	public Map<String, Object> saveEventDisposalAndReport(EventDisposal event, Map<String, Object> params, UserInfo userInfo);
	
	/**
	 * 事件上报
	 * @param eventJson json串参数
	 * @return
	 * eventId 事件ID
	 * instanceId 实例ID
	 * userIds 下一节点办理人员
	 * result 办理结果 成功为true，否则为false
	 */
	public Map<String, Object> saveEventDisposalAndReport(String eventJson);
	public Map<String, Object> saveEventDisposalAndReport(Map<String, Object> eventMap);
	
	/**
	 * 事件分流
	 * @param eventJson eventId为空时，新增并分流；否则值进行分流操作
	 * @return eventId instanceId result result为true时操作成功；否则为操作失败
	 */
	public Map<String, Object> saveEventDisposalAndShunt(String eventJson);
	public Map<String, Object> saveEventDisposalAndShunt(Map<String, Object> eventMap);
	
	/**
	 * 事件新增并结案
	 * @param eventJson
	 * @return
	 */
	public Map<String, Object> saveEventDisposalAndClose(String eventJson);
	public Map<String, Object> saveEventDisposalAndClose(Map<String, Object> eventMap);
	
	/**
	 * 事件新增并归档
	 * @param event
	 * @param advice
	 * @return
	 */
	public Map<String, Object> saveEventDisposalAndEvaluate(EventDisposal event, UserInfo userInfo, String advice);
	public Map<String, Object> saveEventDisposalAndEvaluate(String eventJson);
	
	/**
	 * 事件驳回操作
	 * @param eventJson  eventId advice
	 * @return resultMap中的result为true时，驳回成功；否则为驳回失败
	 */
	public Map<String, Object> rejectWorkFlow(String eventJson);
	public Map<String, Object> rejectWorkFlow(Map<String, Object> eventMap);
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
	 */
	public boolean rejectWorkFlow(Map<String, Object> params, UserInfo userInfo);
	
	/**
	 * 更新交互表REC_STATUS为已接收(1)
	 * @param eventJson
	 * @return
	 */
	public Map<String, Object> feedBackEventInter(String eventJson);
	
	/**
	 * 通过事件json，转换为相应事件对象
	 * @param eventJson
	 * @return
	 */
	public EventDisposal jsonToEvent(String eventJson);
	public EventDisposal jsonToEvent(Map<String, Object> eventMap);

	/**
	 * 通过json，转换为相应事件对象、用户对象
	 * 事件对象 event；用户对象 userInfo
	 * @param eventJson
	 * @return
	 */
	public Map<String, Object> eventJsonToMap(String eventJson);
	public Map<String, Object> eventJsonToMap(Map<String, Object> eventMap);
	
	/**
	 * 提交事件，自动扭转到下一步
	 * 下一步需要人员选择操作的(如上报、下派)，需要设置nextUserIds和nextOrgIds，否则会将下一环节办理人员设置为当前办理人员，可能导致后续操作异常
	 * @param eventMap
	 * 			eventId			事件id
	 * 			instanceId		流程实例id
	 * 			curTaskId		当前任务id
	 * 			curNodeName		当前环节名称
	 * 			nextNodeName	下一环节名称，为空时，选择当前环节中可扭转的任一下一环节
	 * 			nextUserIds		下一环节办理人员id，以英文逗号相连，其与nextOrgIds一一对应
	 * 			nextOrgIds		下一环节办理人员所属组织id，以英文逗号相连，其与nextUserIds一一对应
	 * 			smsContent		短信内容
	 * 			advice			办理意见，为空时，且evaContent不为空时，使用evaContent作为办理意见
	 * 
	 * 			evaLevel		评价等级
	 * 			evaContent		评价内容
	 * 
	 * 			cn.ffcs.uam.bo.UserInfo userInfo	当前登入用户信息
	 * 
	 * @return
	 * 		result	事件扭转结果，true扭转成功；false扭转失败，不包含评价信息的处理结果
	 * 		msg		异常信息
	 */
	public Map<String, Object> subWorkflow(Map<String, Object> eventMap);
}
