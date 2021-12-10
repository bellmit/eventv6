package cn.ffcs.zhsq.mybatis.persistence.eventDay;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;


public interface EventDayMapper{

	/*
	 *   查询 V_EVENT_DAY 数量  
	 *   params orgCode
	 * */
	public Integer findCountEventDayPagination(Map<String, Object> params);
	
	
	/*
	 *   查询 V_EVENT_DAY list
	 *   params orgCode
	 * */
	public List<Map<String, Object>> findListEventDayPagination(Map<String, Object> params, RowBounds bounds);
	
	
	
	/*
	 *   查询 V_SUP_EVENT list
	 *   params orgCode
	 * */
	public Integer findEventPaginationCount(Map<String, Object> params);
	
	
	/*
	 *   查询 V_SUP_EVENT list
	 *   params orgCode
	 * */
	public List<Map<String, Object>> findEventPaginationList(Map<String, Object> params,RowBounds bounds);
}
