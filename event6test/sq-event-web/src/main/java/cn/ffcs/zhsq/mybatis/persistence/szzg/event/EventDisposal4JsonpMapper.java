package cn.ffcs.zhsq.mybatis.persistence.szzg.event;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;


public interface EventDisposal4JsonpMapper extends MyBatisBaseMapper<Map<String, Object>>{
	
	
	public List<Map<String, Object>> getEventTotalTypeByCondition(Map<String, Object> params);
	
	public List<Map<String, Object>> getEventTotalTypeByConditionToday(Map<String, Object> params);
	
	public List<Map<String, Object>> getEventTotalSourceByCondition(Map<String, Object> params);
	
	public List<Map<String, Object>> getEventTotalSourceByConditionToday(Map<String, Object> params);
	
	//部件查询start
	public List<Map<String, Object>> getMapComponentList(Map<String, Object> params);
	
	public List<Map<String, Object>> getMapComponentList(Map<String, Object> params,RowBounds bounds);
	
	public Long getMapComponentListCount(Map<String, Object> params);
	//部件查询end
	
	public Map<String, Object> getEventTotalByStatistics(Map<String, Object> params);
	
	public List<Map<String, Object>> getEventAreaDistributionByStatistics(Map<String, Object> params);
	
	public List<Map<String, Object>> getEventAreaDistributionByActual(Map<String, Object> params);
	
	public List<Map<String, Object>> getEventTypeProportionByActual(Map<String, Object> params);
	
	public List<Map<String, Object>> getEventTypeProportionByStatistics(Map<String, Object> params);
	
	public List<Map<String, Object>> getEventTrendByActual(Map<String, Object> params);
	
	public List<Map<String, Object>> getEventTrendByStatistics(Map<String, Object> params);
	
	public List<Map<String, Object>> getSpecialEventTypeProportionByActual(Map<String, Object> params);
	
	public List<Map<String, Object>> getSpecialEventTypeAreaByActual(Map<String, Object> params);
	
	/**
	 * 职位事件相关统计信息
	 * @param params
	 * 			statMonthStr	统计月份，格式为：YYYYMM
	 * 			infoOrgCode		地域编码
	 * @return
	 */
	public List<Map<String, Object>> findPositionEventByStatistics(Map<String, Object> params);
	
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
	public List<Map<String, Object>> findUserEventByStatistics(Map<String, Object> params);
	
}
