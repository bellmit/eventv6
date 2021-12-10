package cn.ffcs.zhsq.mybatis.persistence.event;

import cn.ffcs.shequ.mybatis.domain.zzgl.event.EventInter;
import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposalEs;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface EventDisposalMapper extends MyBatisBaseMapper<EventDisposal>{
	/**
	 * 增添交互表数据
	 * @param params
	 * @return
	 */
	public int eventInterInsert(EventInter inter);
	
	/**
	 * 查询交互表信息
	 * @param inter
	 * @return
	 */
	public List<EventInter> findEventInterList(EventInter inter);
	
	/**
	 * 更新交互表数据
	 * @param inter
	 * @return
	 */
	public int updateEventInter(EventInter inter);
	
	/**
	 * 更新交互表数据 oppoSideBusiCode为空
	 * @param inter
	 * @return
	 */
	public int updateEventInterOppoSideBusiCode(EventInter inter);
	
	/**
	 * 关联中间表T_SJ_EVENT_INTER
	 * @param param
	 * @return
	 */
	public int findCountWithEventInter(Map<String, Object> param);
	
	/**
	 * 关联中间表T_SJ_EVENT_INTER查询已结案事件
	 * @param param 事件状态为03 04
	 * @return
	 */
	public List<Map<String, Object>> findClosedEventListWithEventInter(Map<String, Object> param);

	public Map<String, Object> findEvent4Year(Map<String, Object> param);
	public List<Map<String, Object>> findGrid4Month(Map<String, Object> param);
	public List<Map<String, Object>> findTop10OfEventType4Month(Map<String, Object> param);

	/**
	 * 关联中间表T_SJ_EVENT_INTER查询已上报事件
	 * @param param OPPO_SIDE_BUSI_CODE 为空
	 * @return
	 */
	public List<Map<String, Object>> findReportEventListWithEventInter(Map<String, Object> param);
	
	/**
	 * 根据条件统计记录数
	 * @param param 参数
	 * @return
	 */
	public int findCountHistoryViewByCriteria(Map<String, Object> param);

	/**
	 * 根据条件搜索实现分页
	 * @param param 参数
	 * @param bounds 分页信息
	 * @return
	 */
	public List<EventDisposal> findPageHistoryViewByCriteria(Map<String, Object> param, RowBounds bounds);

	/**
	 * 根据条件统计记录数
	 * @param param 参数
	 * @return
	 */
	public int findTodoCountViewByCriteria(Map<String, Object> param);

	public int findCountByEventType(Map<String, Object> param);

	/**
	 * 根据条件搜索实现分页
	 * @param param 参数
	 * @param bounds 分页信息
	 * @return
	 */
	public List<EventDisposal> findPageTodoViewByCriteria(Map<String, Object> param, RowBounds bounds);
	
	public int countEventDataForCommon(Map<String, Object> param);
	
	public List<EventDisposal> findEventDataForCommon(Map<String, Object> param, RowBounds bounds);
	
	/**
	 * 依据涉及人员信息统计事件记录
	 * @param param
	 * 			infoOrgCode		地域编码
	 * 			idCard				身份证号
	 * 			eventStatusList 事件状态 String[]
	 * 			eventName		事件标题，支持模糊查询
	 * 			eventType			事件分类，支持大类查询
	 * @return
	 */
	public int countEventByInvolvedPeople(Map<String, Object> param);
	/**
	 * 依据涉及人员信息获取事件列表
	 * @param param
	 * 			infoOrgCode		地域编码
	 * 			idCard				身份证号
	 * 			eventStatusList 事件状态 String[]
	 * 			eventName		事件标题，支持模糊查询
	 * 			eventType			事件分类，支持大类查询
	 * @param bounds
	 * @return
	 */
	public List<EventDisposal> findEventByInvolvedPeople(Map<String, Object> param, RowBounds bounds);

	/**
	 * 删除事件(新)
	 * 			事件id	事件主键
	 * 			更新人id
	 * @return
	 */
	public int delete(@Param(value="id") Long id,@Param(value="updatorId") Long updatorId);

	/**
	 * 查找EventDisposalEs对象，用来存在es里面
	 * */
	public EventDisposalEs findEventDisposalEsById(@Param(value="eventId")Long eventId);
	
	/**
	 * 依据事件id获取事件记录
	 * @param params
	 * 			eventId			事件id
	 * 			isIgnoreStatus	是否忽略事件状态，true为忽略
	 * @return
	 */
	public EventDisposal findById(Map<String, Object> params);
	
}
