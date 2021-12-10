package cn.ffcs.zhsq.szzg.event.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;

public interface IEventAndReportJsonpService {

	
	public EUDGPagination findDoingEventData(Map<String, Object> params,int page, int rows);

	public Map<String, List<Map<String, Object>>> eventTypeDayTotal(Map<String, Object> params);
	
	public Map<String, List<Map<String, Object>>> eventTypeGridTotal(Map<String, Object> params);

	public Map<String, Object> findEventAnalysisTotal(Map<String, Object> params);
	public Map<String, Object> findEventAnalysisGrid(Map<String, Object> params);
	public Map<String, Object> findEventAnalysisDate(Map<String, Object> params);
	
	public EUDGPagination eventAndReportList4Jsonp(Map<String, Object> params,int page, int rows);

	/**
	 *   入格趋势查询
	 * @return
	 */
	public List<Map<String, Object>> getReportFocusTrend(Map<String, Object> params);
	/**
	 * 	列表数据
	 */
	public EUDGPagination getReportFocusList(Map<String, Object> params,int page, int rows) throws ZhsqEventException;
	
	/**
	 *   事件趋势查询
	 * @return
	 */
	public List<Map<String, Object>> getEventTrend(Map<String, Object> params);
	
	/**
	 * 	统计在办事件+入格数量
	 */
	public int countDoingEventData(Map<String, Object> params);

	/**
	 *  各入格数量统计
	 * @return
	 */
	public List<Map<String, Object>> countDoingReportFocusList(Map<String, Object> params);
	
	/*
	 * 获取所有入格类型
	 */
	public List<Map<String, String>> getReportFocusTypeList(Map<String, Object> params);
	
	public void initReportFocusType(Map<String, Object> params);
	
}
