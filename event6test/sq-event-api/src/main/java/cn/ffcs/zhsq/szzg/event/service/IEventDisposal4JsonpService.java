package cn.ffcs.zhsq.szzg.event.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;

public interface IEventDisposal4JsonpService {

	/**
	 *   事件总量相关type统计查询
	 *   参数
	 *   事件采集时间(起)：createTimeStart
	 *   事件采集时间(终)：createTimeEnd
	 *   事件更新时间(起)：updateTimeStart
	 *   事件更新时间(终)：updateTimeEnd
	 *   
	 * @return
	 */
	public List<Map<String,Object>> getEventTotalTypeByCondition(Map<String, Object> params,Integer pageNo,Integer pageSize);
	
	
	
	
	/**
	 *   事件总量相关source统计查询
	 *   参数
	 *   事件采集时间(起)：createTimeStart
	 *   事件采集时间(终)：createTimeEnd
	 *   事件更新时间(起)：updateTimeStart
	 *   事件更新时间(终)：updateTimeEnd
	 *   
	 * @return
	 */
	public List<Map<String,Object>> getEventTotalSourceByCondition(Map<String, Object> params,Integer pageNo,Integer pageSize);




	public EUDGPagination getEventSimpleList(Map<String, Object> params, UserInfo userInfo);
	
	public Long getEventSimpleCount(Map<String, Object> params, UserInfo userInfo);
	
	public Map<String,Object> searchEventByIdSimple(Long eventId, UserInfo userInfo);
	
	
	public EUDGPagination getSimilarEventList(Map<String, Object> params, UserInfo userInfo);
	
	public Long getSimilarEventCount(Map<String, Object> params, UserInfo userInfo);
	
	public Map<String, Object> getMapEventList(Map<String, Object> params, UserInfo userInfo);
	
	
	public Map<String, Object> getMapEventListCount(Map<String, Object> params, UserInfo userInfo);
	
	
	public Map<String, Object> getMapComponentList(Map<String, Object> params, UserInfo userInfo);
	
	
	public Map<String, Object> getMapComponentListCount(Map<String, Object> params, UserInfo userInfo);
	
	
	public Map<String, Object> getEventTotalByStatistics(Map<String, Object> params, UserInfo userInfo);
	
	
	/**
	 *   事件区域分布查询洗的表
	 *   必传参数
	 *   	infoOrgCode
	 * @return
	 */
	public List<Map<String, Object>> getEventAreaDistributionByStatistics(Map<String, Object> params, UserInfo userInfo);
	
	
	
	
	/**
	 *   事件区域分布实时查询
	 *   必传参数
	 *   	infoOrgCode
	 * @return
	 */
	public List<Map<String, Object>> getEventAreaDistributionByActual(Map<String, Object> params, UserInfo userInfo);
	
	
	/**
	 *   事件类型占比实时查询
	 *   必传参数
	 *   	infoOrgCode
	 * @return
	 */
	public List<Map<String, Object>> getEventTypeProportionByActual(Map<String, Object> params, UserInfo userInfo);
	
	
	
	/**
	 *   事件类型占比查询洗的表
	 *   必传参数
	 *   	infoOrgCode
	 * @return
	 */
	public List<Map<String, Object>> getEventTypeProportionByStatistics(Map<String, Object> params, UserInfo userInfo);
	
	
	
	/**
	 *   事件趋势图实时查询
	 *   必传参数
	 *   	infoOrgCode
	 * @return
	 */
	public List<Map<String, Object>> getEventTrendByActual(Map<String, Object> params, UserInfo userInfo);
	
	
	
	/**
	 *   事件趋势图清洗查询
	 *   必传参数
	 *   	infoOrgCode
	 * @return
	 */
	public List<Map<String, Object>> getEventTrendByStatistics(Map<String, Object> params, UserInfo userInfo);
	
	
	

	/**
	 *   事件类型（暂时支持大类）实时查询（支持传入特定事件类型集合）
	 *   必传参数
	 *   	infoOrgCode
	 * @return
	 */
	public List<Map<String, Object>> getSpecialEventTypeProportionByActual(Map<String, Object> params, UserInfo userInfo);

	
	


	/**
	 *   事件类型,地域分布实时查询
	 *   必传参数
	 *   	infoOrgCode
	 * @return
	 */
	public List<Map<String, Object>> getSpecialEventTypeAreaByActual(Map<String, Object> params, UserInfo userInfo);
	
	/**
	 * 职位的事件相关统计项
	 * @param params
	 * 			statMonthStr	统计月份，格式为：YYYYMM
	 * 			infoOrgCode		地域编码
	 * 			positionName	职位名称，使用英文逗号分隔
	 * @return
	 * 		positionName	职位名称
	 * 		positionNums	职位数量
	 * 		userNums		用户数量
	 * 		handledEvents	已办理量
	 * 		settlesEvents	已办结量
	 * @throws Exception
	 */
	public List<Map<String, Object>> findPositionEventByStatistics(Map<String, Object> params) throws Exception;
	
	/**
	 * 用户事件相关统计信息
	 * @param params
	 * 			statMonthStr	统计月份，格式为：YYYYMM
	 * 			infoOrgCode		地域编码
	 * 			statisticsUserId用户id
	 * 			statisticsOrgId	组织id
	 * @return
	 * 		userId			用户id
	 * 		partyName		用户姓名
	 * 		orgId			组织id
	 * 		orgName			组织名称
	 * 		settlesEvents	事件办结量
	 */
	public List<Map<String, Object>> findUserEventByStatistics(Map<String, Object> params) throws Exception;
}
