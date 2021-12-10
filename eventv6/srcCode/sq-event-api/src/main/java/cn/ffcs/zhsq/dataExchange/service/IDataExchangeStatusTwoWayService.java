package cn.ffcs.zhsq.dataExchange.service;

import java.util.List;
import java.util.Map;


public interface IDataExchangeStatusTwoWayService {

	/**
	 * 查找已对接的事件
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findEventNewDocking(Map<String, Object> params);
	
	/**
	 * 查找指定的事件  指定当前环节 指定当前办理人  指定当前办理组织
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findEventNewAppointed(Map<String, Object> params);
	public List<Map<String, Object>> findSubTaskAppointed(Map<String, Object> params);
	
	/**
	 * 查找需要对接的任务
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findTaskNewAppointed(Map<String, Object> params);

	public List<Map<String, Object>> findEventAppointed(Map<String, Object> params);

	public List<Map<String, Object>> findCloseEventAppointed(Map<String, Object> params);
}
