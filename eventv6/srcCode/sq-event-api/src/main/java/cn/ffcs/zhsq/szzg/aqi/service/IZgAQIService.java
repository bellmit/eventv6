package cn.ffcs.zhsq.szzg.aqi.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.zhsq.mybatis.domain.szzg.aqi.ZgAQI;

public interface IZgAQIService {
	/**
	 * 插入一条重信息
	 * @param RainPlanTrain
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public boolean insert(ZgAQI zgAQI);
	
	
	/**
	 * 查找
	 * @param statioid
	 * @param updateTime
	 * @return
	 */
	
	public boolean findCountByStatioId(Map<String, Object> params);
	
	
	public List<ZgAQI>  findList(Map<String, Object> params);
	
	public List<Map<String,Object>> getHour(String statioId);
	public List<Map<String,Object>> getDay(String statioId);
	/**
	 * 根据城市站点获取站点列表
	 * @param statioId
	 * @return
	 */
	public List<Map<String,Object>>  getStationListByCity(String statioId);
	
	
}
