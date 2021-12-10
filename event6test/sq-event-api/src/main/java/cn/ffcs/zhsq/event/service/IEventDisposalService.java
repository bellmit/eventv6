package cn.ffcs.zhsq.event.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EventInter;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.EventProcess;

/**
 * 事件Servcie
 * @author zhongshm
 *
 */
public interface IEventDisposalService {

	/**
	 * 批量删除
	 * Aug 14, 2014
	 * 3:30:38 PM
	 * @param eventIdList
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public int deleteEventDisposalByIds(List<Long> eventIdList,Long updatorId);

	/**
	 * 根据事件id获取事件对象，不进行属性格式化
	 * @param eventId	事件id
	 * @return
	 */
	public EventDisposal findEventByIdSimple(Long eventId);
	
	/**
	 * 根据ID获取对象
	 * @param eventId
	 * @param orgCode 组织编码
	 * @return
	 */
	public EventDisposal findEventById(Long eventId, String orgCode);
	
	/**
	 * 根据ID获取对象
	 * @param eventId
	 * @param mapType
	 * @param orgCode 组织编码
	 * @return
	 */
	public EventDisposal findEventByIdAndMapt(Long eventId, String mapType, String orgCode);
	
	/**
	 * 依据事件id获取事件相关信息
	 * @param eventId			事件id，必填参数
	 * @param params
	 * 			isCapMapInfo	是否获取地图信息，Boolean类型，true为获取，默认为false
	 * 			isCapExtendInfo	是否获取事件扩展信息，Boolean类型，true为获取，默认为false
	 * 			isCapEventLabelInfo	是否获取事件标签信息，Boolean类型，true为获取，默认为false
	 * 			mapType			事件地图类型，String类型，isCapMapInfo为true时生效
	 * 			userOrgCode		组织编码，String类型，不为空时进行事件字典相关属性转换
	 * 			isIgnoreStatus	是否忽略事件状态，Boolean类型，true为忽略；默认为false
	 * @return
	 * 		event		事件对象，类型为cn.ffcs.zhsq.mybatis.domain.event.EventDisposal
	 * 		eventExtend	isCapExtendInfo为true时获取，事件扩展信息，类型为Map<String, Object>
	 * @throws Exception 
	 */
	public Map<String, Object> findEventByMap(Long eventId, Map<String, Object> params) throws Exception;
	
	/**
	 * 获取事件记录数（待办数、结案数、结案率、处理中数、超时未结案数、个人待办数）
	 * Aug 14, 2014
	 * 3:36:37 PM
	 * @param params 
	 * @return
	 */
	public Map<String, Object> findEventCount(Map<String, Object> params);
	
	/**
	 * 获取事件记录数（总待办量、月待办量、月采集量、月办结量、完成率、督办件）
	 * Aug 14, 2014
	 * 3:36:37 PM
	 * @param params 
	 * @return
	 */
	public Map<String, Object> findEventCountA(Map<String, Object> params) throws Exception;

	/**
	 * 根据时间区段（startTime-endTime）、事件类型（type）、网格ID（gridId）
	 * 获取网格本级与下级的事件记录数（采集量CJ、办结量BJ、待办量DB）
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findEventCountB(Map<String, Object> params) throws Exception;

	/**
	 * 分页获取事件列表数据
	 * Aug 14, 2014
	 * 3:39:27 PM
	 * @param pageNo 当前页
	 * @param pageSize 页面大小
	 * @param params 查询参数
	 * @return
	 */
	public EUDGPagination findEventDisposalPagination(int pageNo, int pageSize,
			Map<String, Object> params);

	int findCountByCriteria(Map<String, Object> params);

	Map<String, Object> findEvent4Year(Map<String, Object> params);

	List<Map<String, Object>> findTop10OfEventType4Month(Map<String, Object> params);

	List<Map<String, Object>> findGrid4Month(Map<String, Object> params);

	int findCountByEventType(Map<String, Object> params);

	/**
	 * 保存事件信息 gridCode存放的是网格的地域编码，如果该字段为空或者事件为对接事件，则会依据gridId进行转换
	 * @param event 事件信息
	 * @param userInfo 当前登入用户信息
	 * @return 主键ID
	 */
	public Long saveEventDisposalReturnId(EventDisposal event, UserInfo userInfo);

	/**
	 * 保存事件
	 * @param event
	 * @param params
	 * 			parentEventId	父级事件id
	 * @param userInfo
	 * @return
	 */
	public Long saveEventDisposalReturnId(EventDisposal event, Map<String, Object> params, UserInfo userInfo);
	
	/**
	 * 更新事件信息
	 * Aug 14, 2014
	 * 3:41:27 PM
	 * @param event
	 * @return
	 */
	public boolean updateEventDisposal(EventDisposal event);

	/**
	 * 更新事件信息（方法重写）
	 * @param event
	 * @param params
	 * @return
	 * */
	public boolean updateEventDisposal(EventDisposal event,Map<String,Object> params);

	/**
	 * 
	 * Aug 14, 2014
	 * 3:41:44 PM
	 * @param event
	 * @return
	 */
	public Long updateEventDisposalById(EventDisposal event);

	/**
	 * 
	 * Aug 14, 2014
	 * 3:56:39 PM
	 * @param inter
	 * @return
	 */
	public List<EventInter> findEventInterList(EventInter inter);

	/**
	 * 
	 * @param inter
	 * @return
	 */
	public Long saveEventInterAndReturnId(EventInter inter);
	
	/**
	 * 更新交互表数据
	 * @param inter
	 * @return
	 */
	public boolean updateEventInter(EventInter inter);
	
	public boolean updateEventInterOppoSideBusiCode(EventInter inter);
	
	/**
	 * 关联中间表T_SJ_EVENT_INTER查询已结案事件
	 * @param param 事件状态为03 04
	 * @return
	 */
	public List<Map<String, Object>> findClosedEventListWithEventInter(Map<String, Object> param);
	
	/**
	 * 关联中间表T_SJ_EVENT_INTER查询已上报事件
	 * @param param OPPO_SIDE_BUSI_CODE 为空
	 * @return
	 */
	public List<Map<String, Object>> findReportEventListWithEventInter(Map<String, Object> param);
	
	/**
	 * 
	 * Aug 14, 2014
	 * 3:56:39 PM
	 * @param inter
	 * @return
	 */
	public Long updateEventForWorkflow(Long eventId, String eventStatus,
			String handleDateStr, String finTimeStr, Long userId, Long orgId);

	/**
	 * 保存事件处理步骤
	 * @param eventProcess
	 * @return 保存成功返回processId, 否则返回-1
	 */
	public Long saveEventProcess(EventProcess eventProcess);

	/**
	 * 我的归档
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 			orderByField	排序字段，格式为“字段 升序/降序”如CREATE_TIME ASC
	 * @return
	 */
	public EUDGPagination findHistoryEventWorkFlowPagination(int pageNo, int pageSize,
			Map<String, Object> params);

	/**
	 * 
	 * Aug 14, 2014
	 * 3:56:39 PM
	 * @param inter
	 * @return
	 */
	public EUDGPagination findAllTodoEventWorkFlowPagination(int pageNo, int pageSize,
			Map<String, Object> params);

	/**
	 * 统计辖区所有事件数量
	 * @param params
	 * @return
	 */
	public int findAllEventWorkFlowCount(Map<String, Object> params);
	
	/**
	 * 获取事件及当前任务信息
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findEventTaskMapPagination(int pageNo,
			int pageSize, Map<String, Object> params);
	
	/**
	 * 督办列表
	 * 该接口替换为：cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService
	 * public List<Map<String, Object>> findRemindInvalidList(Map<String, Object> params) throws Exception
	 * 
	 * Aug 14, 2014
	 * 3:56:39 PM
	 * @param inter
	 * @return
	 */
	@Deprecated
	public EUDGPagination findSupervisePagination(int pageNo, int pageSize,
			Map<String, Object> params);
	
	/**
	 * 被督办列表
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findSupervisedPagination(int pageNo, int pageSize,
			Map<String, Object> params);
	
	/**
	 * 被催办列表
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findRemindedPagination(int pageNo, int pageSize,
			Map<String, Object> params);

	/**
	 * 被督办事件列表
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 			orderByField	排序字段，格式为“字段 升序/降序”如CREATE_TIME ASC
	 * @return
	 */
	public EUDGPagination findRemindEventWorkFlowPagination(int pageNo, int pageSize,
			Map<String, Object> params);
	
	/**
	 * 经办历史事件列表/经办归档事件列表
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 			orderByField	排序字段，格式为“字段 升序/降序”如CREATE_TIME ASC
	 * @return
	 */
	public EUDGPagination findDoneHistoryEventWorkFlowPagination(int pageNo,
			int pageSize, Map<String, Object> params);

	public EUDGPagination findHistoryViewPagination(int pageNo, int pageSize,
			Map<String, Object> params);
	
	public EUDGPagination findEventDataForCommon(int pageNo, int pageSize, Map<String, Object> params);

	/**
	 * 新旧事件-待办（视图）
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public EUDGPagination findTodoViewPagination(int pageNo, int pageSize,
			Map<String, Object> params);
	
	/**
	 * 依据涉及人员信息获取事件列表
	 * @param pageNo		页码
	 * @param pageSize		每页记录数
	 * @param params
	 * 必填参数
	 * 			infoOrgCode		地域编码
	 * 			idCard				身份证号码 用于精确查找
	 * 			orgCode			组织编码，用于字典转换
	 * 非必填参数
	 * 			eventStatusList 事件状态 String[]，优先参数eventStatus生效
	 * 			eventStatus		事件状态
	 * 			eventName		事件标题，支持模糊查询
	 * 			eventType			事件分类，支持大类查询
	 * @return 
	 * @throws Exception 
	 * 				IllegalArgumentException
	 */
	public EUDGPagination findEventByInvolvedPeoplePagination(int pageNo,
			int pageSize, Map<String, Object> params) throws Exception;
	
	/**
	 * 为事件设置默认属性
	 * @param event
	 * @param userInfo
	 */
	public EventDisposal defaultEvent(EventDisposal event, UserInfo userInfo);
	
	/**
	 * 格式化事件
	 * @param event
	 * @return
	 */
	public EventDisposal formatData(EventDisposal event, String orgCode);
	
	/**
	 * 判断是否启用新的事件办理页面
	 * 1、当前组织启用新组织并且使用新的工作流
	 * 2、当前组织未启用新组织，当前组织启用新的事件办理页面，并且使用新的工作流
	 * 3、当前组织未启用新组织，当前组织启用新的事件办理页面，配置的新的事件办理页面启用时间，并且使用新的工作流，则在配置的时间(不包括设定的时间)之后，使用新的事件办理页面；在配置的时间(包括设定的时间)之前，仍使用旧的事件办理页面
	 * @param eventId 事件id
	 * @param userInfo 当前登入用户信息
	 * @return true，则使用新的事件办理页面；否则不使用
	 */
	@Deprecated
	public boolean isUserNewHandleEvent(Long eventId, UserInfo userInfo);

    /**
     * 添加督办发送mq消息
     * @param instanceId
     * @param infoOrgCode
     * @return
     */
    public boolean pushRemindMsg(Long instanceId,String infoOrgCode,Map<String, Object> params);
}
