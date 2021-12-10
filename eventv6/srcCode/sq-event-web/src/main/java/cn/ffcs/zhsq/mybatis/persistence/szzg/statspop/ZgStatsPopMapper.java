package cn.ffcs.zhsq.mybatis.persistence.szzg.statspop;



import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.szzg.statspop.ZgStatsPop;

/**
 * 人口
 * @author huangwenbin
 *
 */
public interface ZgStatsPopMapper extends MyBatisBaseMapper<ZgStatsPop> {

	/**
	 * 条件查询
	 * @param orgCode
	 * @param stype
	 * @param syear
	 * @return
	 */
	public List<ZgStatsPop> findListByParams(Map<String,Object> param);
	/**
	 * 条件查询是否存在
	 * @param orgCode
	 * @param stype
	 * @param syear
	 * @return
	 */
	public int findCount(Map<String,Object> param);
	
	public int insertByList(List<ZgStatsPop> list);
	public int updateByList(List<ZgStatsPop> list);
	public int deleteByParam(Map<String,Object> param);
	
	/**
	 * 报表
	 * @param stype 类型
	 * @param orgCode 区域
	 * @param syear 年份
	 */
	public List<Map<String,Object>> findChartByJB(Map<String,Object> param);
	
	/**
	 * 查询人口所有年份
	 * @param orgCode
	 * @param stype
	 * @param syear
	 * @return
	 */
	public List<ZgStatsPop> findPersonAllYear(Map<String,Object> param);
	/**
	 * 重点人群 查询年份
	 */
	public List<ZgStatsPop> findChartByZDYear(Map<String,Object> param);
	/**
	 * 重点人群 查询年龄段
	 */
	public List<ZgStatsPop> findChartByZDAge(Map<String,Object> param);
	/**
	 * 重点人群 地域分布
	 * @return
	 */
	public List<ZgStatsPop> findChartByZDOrgCode(Map<String,Object> param);
	/**
	 *低保 金额 人数
	 * @return
	 */
	public List<ZgStatsPop> findChartByDB(Map<String,Object> param);
	/**
	 * 低保table
	 * @return
	 */
	public List<Map<String,Object>> findTableByDB(Map<String,Object> param);
	/**
	 *残疾
	 * @return
	 */
	public List<ZgStatsPop> findChartByCJ(Map<String,Object> param);
	/**
	 * 残疾table
	 * @return
	 */
	public List<Map<String,Object>> findTableByCJ(Map<String,Object> param);
	
	/**
	 * 	居家养老  多维度图表
	 * @return
	 */
	public List<Map<String,Object>> findMultipleByJJYL(Map<String,Object> param);
	/**
	 *	居家养老  年龄分布
	 * @return
	 */
	public List<ZgStatsPop> findAgeByJJYL(Map<String,Object> param);
	/**
	 * 居家养老 table
	 * @return
	 */
	public List<Map<String,Object>> findTableByJJYL(Map<String,Object> param);
	/**
	 * 人口趋势
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findFRateByQS(Map<String,Object> param);
	public List<Map<String,Object>> findRateByQS(Map<String,Object> param);
	public int delete(Map<String, Object> param);
	
	public Map<String,Object> findDataDict(Map<String,Object> param);
}
