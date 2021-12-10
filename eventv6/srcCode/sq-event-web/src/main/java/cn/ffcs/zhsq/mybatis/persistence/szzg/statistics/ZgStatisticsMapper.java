package cn.ffcs.zhsq.mybatis.persistence.szzg.statistics;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.szzg.statistics.ZgStatistics;

/**
 * 人口
 * @author huangwenbin
 *
 */
public interface ZgStatisticsMapper extends MyBatisBaseMapper<ZgStatistics> {

	/**
	 * 根据日期区间查询 最大最小主键
	 * @param param
	 * @return
	 */
	public Map<String,Object> findEventIdByDate(Map<String,Object> param);
	/**
	 * 根据年月查询
	 * @param param
	 * @return
	 */
	public List<ZgStatistics> findByParam(Map<String,Object> param);
	/**
	 * 查询标题
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findTitle(Map<String,Object> param);
	
	
	public int insertByList(List<ZgStatistics> list);
	
	public int updateByList(List<ZgStatistics> list);
	
	public int deleteByParam(Map<String,Object> param);
	/**
	 * 根据参数查询是否存在
	 * @param stype
	 * @param smonth
	 * @param syear
	 * @return
	 */
	public int findCount(Map<String,Object> param);
	
	/**
	 * 查询树形
	 * @param param
	 * @return
	 */
	public List<ZgStatistics> findTreeTable(Map<String,Object> param);
	/**
	 * 查询 12个月
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findChart12Month(Map<String,Object> param);
	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findGDPYearAndMonth(Map<String,Object> param);
	
	/**
	 * 查询 当年季度 或 季度每年趋势
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findGDPYearOrMonth(Map<String,Object> param);
	
	/**
	 * 区域 各年份
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findGDPYears(Map<String,Object> param);
	
	public List<Map<String,Object>> findStatisticsDate(Map<String,Object> param);
	
	public List<Map<String,Object>> findGeneralDate(Map<String,Object> param);
	
	public List<Map<String,Object>> findGeneralYear(Map<String,Object> param);
	public List<Map<String,Object>> findGeneralOrgCode(Map<String,Object> param);
	public List<Map<String,Object>> findGeneralTypeYear(Map<String,Object> param);
	
	/**
	 * 获取事件热力图
	 */
	public List<String> findEventHeatData(Map<String,Object> param);
	/**
	 * 框选 前5类
	 */
	public List<Map<String,Object>> findEventTop5(Map<String,Object> param);
	/**
	 * 框选列表
	 */
	public List<Map<String,Object>> findEventList(Map<String,Object> param, RowBounds bounds);
	/**
	 * 框选总数
	 */
	public int findEventCount(Map<String,Object> param);
	/**
	 * 查询事件字典顶级父类长度
	 */
	public int findEventDatadictLength(Map<String,Object> param);
	
	public int findEventKXCount(Map<String,Object> param);
	/**
	 * 招商引资-延平
	 * 字典：查询所有行业分类
	 */
	public List<String> findAttractInvestmentSort(Map<String,Object> param);
	
	/**
	 * 招商引资-延平
	 * 柱形图：签约及开工项目情况
	 */
	public Map<String,Object> findAttractInvestmentBarData(Map<String,Object> param);
	
	/**
	 * 招商引资-延平
	 * 饼图:查询该年份的行业签约或开工占比
	 */
	public List<Map<String,Object>> findAttractInvestmentByYearAndStatus(Map<String,Object> param);
	public void delLastKXData(Map<String, Object> param);
	
	public List<Map<String,Object>> findGridByParentCode(Map<String,Object> param);
	public List<Map<String,Object>> findGridCountByToday(Map<String,Object> param);
}
