package cn.ffcs.zhsq.mybatis.persistence.dataExchange;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;

public interface DataExchangeStatusTwoWayMapper extends MyBatisBaseMapper<DataExchangeStatus> {
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
	 * 查找已对接的事件
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findEventNewDocking(Map<String, Object> params);
	
	/**
	 * 查找指定的事件 指定环节 指定办理人
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findEventNewAppointed(Map<String, Object> params);

	public List<Map<String, Object>> findSubTaskAppointed(Map<String, Object> params);

	public List<Map<String, Object>> findCloseEventAppointed(Map<String, Object> params);

	public List<Map<String, Object>> findEventAppointed(Map<String, Object> params);
	
	/**
	 * 查找需要对接的任务
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findTaskNewAppointed(Map<String, Object> params);
}
