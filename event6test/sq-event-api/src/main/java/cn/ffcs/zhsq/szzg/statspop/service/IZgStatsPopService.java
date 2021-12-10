package cn.ffcs.zhsq.szzg.statspop.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.statspop.ZgStatsPop;

public interface IZgStatsPopService {

	public ZgStatsPop findById(Long id);
	
	public int addOrUpdate(ZgStatsPop entity);
	
	public int delete(Long id);
	public int deleteByParam(Map<String, Object> param);
	
	/**
	 * 分页查询
	 * @param orgCode
	 * @param stype
	 * @param syear
	 * @return
	 */
	public EUDGPagination findByPagination(int pageNo, int pageSize, Map<String, Object> param);
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

	int insertByList(List<ZgStatsPop> list);
	int updateByList(List<ZgStatsPop> list);

	/**
	 * 报表
	 * @param stype 类型
	 * @param orgCode 区域
	 * @param syear 年份
	 */
	public List<Map<String, Object>> findChartByJB(Map<String, Object> param);
		/**
		 * 查询人口所有年份
		 * @param orgCode
		 * @param stype
		 * @param syear
		 * @return
		 */
	public List<ZgStatsPop> findPersonAllYear(Map<String, Object> param);

	/**
	 * 查询重点人群 
	 * @param orgCode
	 * @param stype
	 * @param syear
	 * @return
	 */
	public Map<String, Object> findChartByZDPerson(Map<String, Object> param);
	
	/**
	 * 查询低保
	 * @param orgCode
	 * @param stype
	 * @return
	 */
	public Map<String, Object> findChartByDB(Map<String, Object> param);
	
	/**
	 * 查询残疾
	 * @param orgCode
	 * @param stype
	 * @return
	 */
	public Map<String, Object> findChartByCJ(Map<String, Object> param);
	
	/**
	 * 查询 居家养老
	 * @param orgCode
	 * @param stype
	 * @return
	 */
	public Map<String, Object> findChartByJJYL(Map<String, Object> param);
	/**
	 * 人口趋势
	 * @param orgCode
	 * @param stype
	 * @return
	 */
	public Map<String, Object> findChartByQS(Map<String, Object> param);

	int delete(Map<String, Object> param);
	
	/**
	 * 根据参数查字典(可选参数)
	 * @param dictCode
	 * @param dictGeneralCode	
	 * @param dictLevel	
	 * @return 字典对象(map所有字段大写)
	 */
	public Map<String,Object> findDataDict(Map<String,Object> param);
}
