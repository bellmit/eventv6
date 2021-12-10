package cn.ffcs.zhsq.dataExchange.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.common.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;

public interface IDataExchangeStatusService {
	public static final String BIZ_TYPE_EVENT_OLD = "0";		//业务类型  旧事件
	public static final String BIZ_TYPE_TASK_OLD = "1";			//业务类型  旧事件任务
	public static final String BIZ_TYPE_EVENT_NEW = "2";		//业务类型  新事件
	public static final String BIZ_TYPE_TASK_NEW = "3";			//业务类型  新事件任务
	public static final String BIZ_TYPE_RECEIVE_NEW = "4";		//业务类型  新事件接收
	public static final String BIZ_TYPE_EVENT_ICT = "5";		//业务类型  ICT事件
	public static final String BIZ_TYPE_TASK_ICT = "6";			//业务类型  ICT事件任务
	
	public static final String PLATFORM_EVENT_NEW = "000";			//新事件平台
	public static final String PLATFORM_SERVICE_OUTLET = "004";		//便民服务
	public static final String PLATFORM_SERVICE_OUTLET_NW = "007";	//南威 泉州市级平台
	public static final String PLATFORM_XIBIN_ICT = "008";			//西滨ICT事件平台
	
	public static final String EXCHANGE_FLAG_UNEXCHANGE = "0";	//未交换
	public static final String EXCHANGE_FLAG_EXCHANGED = "1";	//已交换
	
	public static final String STATUS_VALIDATE = "1";			//有效
	public static final String STATUS_INVALIDATE = "0";			//无效
	
	/**
	 * 新增数据交换状态记录
	 * @param dataExchangeStatus
	 * @return 新增成功则返回对应的interId，新增失败返回-1， 重复新增返回0
	 */
	public Long saveDataExchangeStatus(DataExchangeStatus dataExchangeStatus);
	public Long save(DataExchangeStatus dataExchangeStatus);

	Long saveTaskExchangeFromThree(String ownSideBizCode, String oppoSideBizCode, String bizPlatform, String xmlData);

	Long saveTaskExchangeToThree(String ownSideBizCode, String oppoSideBizCode, String bizPlatform, String xmlData);

	Long saveEventExchangeFromThree(String ownSideBizCode, String oppoSideBizCode, String bizPlatform, String xmlData);

	Long saveEventExchangeToThree(String ownSideBizCode, String oppoSideBizCode, String bizPlatform, String xmlData);

	/**
	 * 新增新事件的数据交换状态记录
	 * @param srcPlatform 事件来源平台 默认为000
	 * @param destPlatform 事件目标平台 默认为000
	 * @param oppoSideBizCode 第三方对应的事件编号
	 * @param ownSideBizCode 我方事件编号
	 * @return 新增成功则返回对应的interId，否则返回-1
	 */
	public Long saveDataExchangeStatusForEventNew(String srcPlatform, String destPlatform, String oppoSideBizCode, String ownSideBizCode);
	
	/**
	 * 新增工作流任务的数据交换状态记录
	 * @param srcPlatform 任务来源平台 默认为000
	 * @param destPlatform 任务目标平台 默认为000
	 * @param oppoSideBizCode 第三方对应的任务编号
	 * @param ownSideBizCode 我方工作流任务编号
	 * @return 新增成功则返回对应的interId，否则返回-1
	 */
	public Long saveDataExchangeStatusForTaskNew(String srcPlatform, String destPlatform, String oppoSideBizCode, String ownSideBizCode);
	public Long saveDataExchangeStatusForTaskNew(DataExchangeStatus dataExchangeStatus);
	
	/**
	 * 依据interId更新记录
	 * @param dataExchangeStatus
	 * @return 更新成功返回true，否则返回false
	 */
	public boolean updateDataExchangeStatusById(DataExchangeStatus dataExchangeStatus);
	
	/**
	 * 依据interId更新记录，更新非重点属性
	 * @param dataExchangeStatus
	 * @return
	 */
	public boolean updateDataExchangeStatusByInterId(DataExchangeStatus dataExchangeStatus);
	
	/**
	 * 依据interId删除记录
	 * @param interId
	 * @return 删除成功返回true，否则返回false
	 */
	public boolean deleteDataExchangeStatusById(Long interId);
	
	/**
	 * 统计记录数量
	 * @param dataExchangeStatus
	 * @return
	 */
	public int findDataExchangeCount(DataExchangeStatus dataExchangeStatus);
	
	/**
	 * 获取记录列表
	 * @param dataExchangeStatus
	 * @return
	 */
	public List<DataExchangeStatus> findDataExchangeList(DataExchangeStatus dataExchangeStatus);
	
	/**
	 * 查找需要反馈的新事件 事件为已结案和已归档事件
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findEventNewToFeedback(Map<String, Object> params);
	
	/**
	 * 查找需要反馈的新事件 事件为未接收的
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findReceiveNewToFeedback(Map<String, Object> params);
	
	/**
	 * 查询需要反馈的工作流任务
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findTaskNewToFeedback(Map<String, Object> params);
	
	/**
	 * 查询可上报的新事件
	 * @param params
	 * @return
	 */
	public List<DataExchangeStatus> findEventNewToReport(Map<String, Object> params);
	
	/**
	 * 查找被驳回的事件 事件当前环节为指定环节
	 * @param params activityName和bizPlatform两个参数二选一，activityName优先生效
	 * @return
	 */
	public List<Map<String, Object>> findEventNewRejected(Map<String, Object> params);
	
	
	/**
	 * 为指定事件的任务新增中间记录，为任务反馈做好铺垫；
	 * 当任务的taskId或者remarks有一个为空时，则该任务不反馈
	 * @param eventId  事件编号
	 * @param destPlatform  目标平台
	 * @param oppoSideBizCode 第三方对应任务编码
	 * @return
	 */
	public boolean isEventTaskFeedback(Long eventId, String destPlatform, String oppoSideBizCode);
	public boolean isEventTaskFeedback(Long eventId, DataExchangeStatus dataExchangeStatus);

	public List<DataExchangeStatus> findDataExchange(
			DataExchangeStatus dataExchangeStatus);

	List<Map<String, Object>> findRejectEvent(Map<String, Object> params);

	List<Map<String, Object>> findTaskEvent(Map<String, Object> params);

	List<Map<String, Object>> findEvaEvent(Map<String, Object> params);

	List<Map<String, Object>> findCloseEvent(Map<String, Object> params);

	EUDGPagination findDataExchangePagination(int pageNo, int pageSize,
											  Map<String, Object> params);

	/**
	 * 查询上报事件
	 * @param params
	 * @return
     */
	List<Map<String, Object>> findReportEvent(Map<String, Object> params);
	List<Map<String, Object>> findCheckEvent(Map<String, Object> params);
	public List<Map<String, Object>> findCloseCheckEvent(Map<String, Object> params);
	Long findEventIdByOppCode(int pageNo, int pageSize,
			Map<String, Object> params);
	List<Map<String, Object>> findFileEvent(Map<String, Object> params);

	Map<String, Object> findMatterEvent(Map<String, Object> params);
	Long countScore(Map<String, Object> params);
	
	
	public List<Map<String, Object>> findNewTask(Map<String, Object> params);

	public Map<String, Object> getCloseEvt(Map<String, Object> params);
	public List<Map<String, Object>> findLikeEventVerify(Map<String, Object> params);
	//根据任务id查询两天内是否有对应审核记录上报成事件，且事件还在处理中
	public int findCountEvent4NcHuaWei(Map<String, Object> params);
}
