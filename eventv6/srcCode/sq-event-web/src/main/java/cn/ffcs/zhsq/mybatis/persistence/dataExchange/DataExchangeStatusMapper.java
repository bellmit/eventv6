package cn.ffcs.zhsq.mybatis.persistence.dataExchange;


import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;

public interface DataExchangeStatusMapper  extends MyBatisBaseMapper<DataExchangeStatus>{
	
	/**
	 * 中间表记录拆分大字段Clob参数
	 * @param dataExchangeStatus
	 * @return 
	 */
	public int insertClob(DataExchangeStatus dataExchangeStatus);
	/**
	 * 统计满足条件的记录数
	 * @param dataExchangeStatus
	 * @return
	 */
	public int findCountByCriteria(DataExchangeStatus dataExchangeStatus);
	
	/**
	 * 无分页获取满足条件的记录
	 * @param dataExchangeStatus
	 * @return
	 */
	public List<DataExchangeStatus> findPageListByCriteria(DataExchangeStatus dataExchangeStatus);
	
	/**
	 * 查找需要上报的事件 add by zhongshm
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findReportEvent(Map<String, Object> params);

	public List<Map<String, Object>> findRejectEvent(Map<String, Object> params);

	/**
	 * 查找事件过程 add by zhongshm
	 * @param params
	 * @return
     */
	public List<Map<String, Object>> findTaskEvent(Map<String, Object> params);
	public List<Map<String, Object>> findEvaEvent(Map<String, Object> params);
	public List<Map<String, Object>> findCloseEvent(Map<String, Object> params);

	/**
	 * 查找需要上报的事件
	 * @param params
	 * @return
	 */
	public List<DataExchangeStatus> findEventNewToReport(Map<String, Object> params);
	
	/**
	 * 查找需要反馈的新事件
	 * @param params eventStatus 事件状态 未传时，默认为03,04
	 * @return
	 */
	public List<Map<String, Object>> findEventNewToFeedback(Map<String, Object> params);
	
	/**
	 * 查找需要反馈的工作流历史任务
	 */
	public List<Map<String, Object>> findTaskNewToFeedback(Map<String, Object> params);
	
	/**
	 * 查找需要反馈的未接收
	 */
	public List<Map<String, Object>> findReceiveNewToFeedback(Map<String, Object> params);
	
	/**
	 * 查找被驳回的事件 事件当前环节为指定环节
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findEventNewRejected(Map<String, Object> params);

	public List<Map<String, Object>> findCheckEvent(Map<String, Object> params);

	public List<Map<String, Object>> findCloseCheckEvent(
			Map<String, Object> params);

	public List<Map<String, Object>> findFileEvent(Map<String, Object> params);
	
	public Map<String, Object> findMatterEvent(Map<String, Object> params);

	/**
	 * 获取完整度
	 * @param params
	 * @return
	 */
	public Long countScore(Map<String, Object> params);
	public List<Map<String, Object>> findNewTask(Map<String, Object> params);
	public Map<String, Object> getCloseEvt(Map<String, Object> params);
	public List<Map<String, Object>> findLikeEventVerify(Map<String, Object> params);
	//根据任务id查询两天内是否有对应审核记录上报成事件，且事件还在处理中
	public int findCountEvent4NcHuaWei(Map<String, Object> params);
	
}