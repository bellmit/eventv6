package cn.ffcs.zhsq.mybatis.persistence.szzg.event;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

public interface EventAndReportJsonpMapper { 

	public int findDoingEventCount(Map<String, Object> params);
	
	public List<Map<String, Object>> findDoingEventData(Map<String, Object> params, RowBounds rowBounds);

	public List<Map<String, Object>> eventTypeDayTotal(Map<String, Object> params);
	
	public List<Map<String, Object>> startDay2EndDay(Map<String, Object> params);
	public List<Map<String, Object>> startMonth2EndMonth(Map<String, Object> params);
	
	public List<Map<String, Object>> eventTypeGridTotal(Map<String, Object> params);

	public List<Map<String, Object>> findGridInfoByParentId(Map<String, Object> params);
	public List<Map<String, Object>> findEventGridTotal(Map<String, Object> params);
	public List<Map<String, Object>> findReportGridTotal(Map<String, Object> params);
	
	
	public List<Map<String, Object>> findEventDateTotal(Map<String, Object> params);
	public List<Map<String, Object>> findReportDateTotal(Map<String, Object> params);

	public Map<String, Object> findEventTotal(Map<String, Object> params);
	
	public Map<String, Object> findReportTotal(Map<String, Object> params);
	
	//public int eventAndReportList4JsonpCount(Map<String, Object> params);
	
	//public List<Map<String, Object>> eventAndReportList4JsonpData(Map<String, Object> params, RowBounds rowBounds);
	
	//查询所有在办事件列表（南安）
	public List<Map<String, Object>> findAllDoingEventForNananList(Map<String, Object> params, RowBounds rowBounds);
	
	//查询所有在办事件数量（南安）
	public Long findAllDoingEventForNananCount(Map<String, Object> params);
	
	//入格趋势查询
	public List<Map<String, Object>> getReportFocusTrend(Map<String, Object> params);
	//入格趋势查询（月份）
	public List<Map<String, Object>> getReportFocusTrendForMonth(Map<String, Object> params);
	
	//列表数据统计
	public Long getReportFocusCount(Map<String, Object> params);
	
	//列表数据
	public List<Map<String, Object>> getReportFocusList(Map<String, Object> params, RowBounds rowBounds);
	
	//事件趋势查询
	public List<Map<String, Object>> getEventTrend(Map<String, Object> params);
	
	//各入格数量统计
	public List<Map<String, Object>> countDoingReportFocusList(Map<String, Object> params);
	
	public List<Map<String, String>> getReportFocusTypeList(Map<String, Object> params);
}
