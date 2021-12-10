package cn.ffcs.zhsq.szzg.greenManager.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.greenManager.StatsGreenBO;

public interface GreenIndicatorsService {
	
	/**
	 * 按年份查询最近5年的绿化指标数据
	 * @return
	 */
	public List<StatsGreenBO> findStatsGreen();
	
	/**
	 * 绿化指标数据分页查询
	 * @param params
	 * @param page
	 * @param rows
	 * @return
	 */
	public EUDGPagination findPageListByCriteria(Map<String, Object> params, int page, int rows);
	
	/**
	 * 根据id查询
	 * @param seqid
	 * @return
	 */
	public StatsGreenBO findGreenIndicatorsById(Long seqid);
	
	/**
	 * 更新数据
	 * @param statsGreen
	 * @return
	 */
	public Boolean update(StatsGreenBO statsGreen);
	
	/**
	 * 删除数据
	 * @param seqid
	 * @return
	 */
	public Boolean del(Long seqid);
	
	/**
	 * 条件查询
	 * @param params
	 * @return
	 */
	public List<StatsGreenBO> findStatsGreenByParams(Map<String, Object> params);
	
	/**
	 * 新增
	 * @param statsGreen
	 * @return
	 */
	public Boolean insert(StatsGreenBO statsGreen);


	/**
	 * 查询趋势数据
	 */
	public Map<String,Object> findChartDataByQs(Map<String, Object> params);
}
