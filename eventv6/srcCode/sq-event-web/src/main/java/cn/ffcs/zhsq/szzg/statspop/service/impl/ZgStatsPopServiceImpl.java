package cn.ffcs.zhsq.szzg.statspop.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.statspop.ZgStatsPop;
import cn.ffcs.zhsq.mybatis.persistence.szzg.statspop.ZgStatsPopMapper;
import cn.ffcs.zhsq.szzg.statspop.service.IZgStatsPopService;

@Service(value="zgStatsPopServiceImpl")
public class ZgStatsPopServiceImpl implements IZgStatsPopService {

	@Autowired
	private ZgStatsPopMapper zgStatsPopMapper;
	
	@Override
	public ZgStatsPop findById(Long id) {
		return zgStatsPopMapper.findById(id);
	}

	@Override
	public int updateByList(List<ZgStatsPop> list) {
		return zgStatsPopMapper.updateByList(list);
	}
	@Override
	public int insertByList(List<ZgStatsPop> list) {
		return zgStatsPopMapper.insertByList(list);
	}

	@Override
	public int addOrUpdate(ZgStatsPop entity) {
		if(entity.getSeqId() !=null){
			return zgStatsPopMapper.update(entity);
		}
		return zgStatsPopMapper.insert(entity);
	}
	
	@Override
	public int delete(Long id) {
		return zgStatsPopMapper.delete(id);
	}

	@Override
	public EUDGPagination findByPagination(int pageNo, int pageSize,
			Map<String, Object> param) {
		pageNo = pageNo<1 ? 1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds bounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		int count  = zgStatsPopMapper.findCountByCriteria(param);
		List<ZgStatsPop>  list= zgStatsPopMapper.findPageListByCriteria(param, bounds);
		return new EUDGPagination(count, list);
	}

	/**
	 * 条件查询
	 * @param orgCode
	 * @param stype
	 * @param syear
	 * @return
	 */
	public List<ZgStatsPop> findListByParams(Map<String,Object> param){
		return zgStatsPopMapper.findListByParams(param);
	}
	
	/**
	 * 条件查询是否存在
	 * @param orgCode
	 * @param stype
	 * @param syear
	 * @return
	 */
	public int findCount(Map<String,Object> param){
		return zgStatsPopMapper.findCount(param);
	}

	@Override
	public int deleteByParam(Map<String, Object> param) {
		return zgStatsPopMapper.deleteByParam(param);
	}
	
	/**
	 * 报
	 * 表
	 * @param stype 类型
	 * @param orgCode 区域
	 * @param syear 年份
	 */@Override
	public List<Map<String,Object>> findChartByJB(Map<String,Object> param){
		return zgStatsPopMapper.findChartByJB(param);
	}
	 
	 /**
	 * 查询人口所有年份
	 * @param orgCode
	 * @param stype
	 * @param syear
	 * @return
	 */
	 @Override
	public List<ZgStatsPop> findPersonAllYear(Map<String,Object> param){
		return zgStatsPopMapper.findPersonAllYear(param);
	}
	 /**
	 * 查询重点人群 
	 * @param orgCode
	 * @param stype
	 * @return
	 */
	public Map<String, Object> findChartByZDPerson(Map<String, Object> param){
		param.put("age", zgStatsPopMapper.findChartByZDAge(param));
		param.put("year", zgStatsPopMapper.findChartByZDYear(param));
		param.put("orgcode", zgStatsPopMapper.findChartByZDOrgCode(param));
	return param;
	}
	/**
	 * 查询低保
	 * @param orgCode
	 * @param stype
	 * @return
	 */
	public Map<String, Object> findChartByDB(Map<String, Object> param){
		param.put("table", zgStatsPopMapper.findTableByDB(param));
		
		param.put("stype", "'S001006003','S001006006','S001006009'");
		param.put("money", zgStatsPopMapper.findChartByDB(param));
		
		param.put("stype", "'S001006002','S001006005','S001006008'");
		param.put("person", zgStatsPopMapper.findChartByDB(param));
		return param;
	}
	/**
	 * 查询残疾
	 * @param orgCode
	 * @param stype
	 * @return
	 */
	public Map<String, Object> findChartByCJ(Map<String, Object> param){
		param.put("table", zgStatsPopMapper.findTableByCJ(param));
		
		param.put("stype", "name");
		param.put("type", zgStatsPopMapper.findChartByCJ(param));
		
		param.put("stype", "level");
		param.put("level", zgStatsPopMapper.findChartByCJ(param));
		return param;
	}
	/**
	 * 查询 居家养老
	 * @param orgCode
	 * @param stype
	 * @return
	 */
	public Map<String, Object> findChartByJJYL(Map<String, Object> param){
		if(param.get("multiple")!=null){
			param.put("multiple", zgStatsPopMapper.findMultipleByJJYL(param));
		}else{
			param.put("age", zgStatsPopMapper.findAgeByJJYL(param));
			param.put("table", zgStatsPopMapper.findTableByJJYL(param));
		}
		return param;
	}

	@Override
	public Map<String, Object> findChartByQS(Map<String, Object> param) {
		param.put("rate", zgStatsPopMapper.findRateByQS(param));
		param.put("frate", zgStatsPopMapper.findFRateByQS(param));
		param.put("age", zgStatsPopMapper.findChartByZDAge(param));
		return param;
	}

	@Override
	public int delete(Map<String, Object> param) {
		return zgStatsPopMapper.delete(param);
	}


	public Map<String,Object> findDataDict(Map<String,Object> param){
		return zgStatsPopMapper.findDataDict(param);
	}
}
