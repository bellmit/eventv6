package cn.ffcs.zhsq.mybatis.persistence.szzg.aqi;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.szzg.aqi.ZgAQI;

public interface ZgAQIMapper extends MyBatisBaseMapper<ZgAQI>{
	
	 int findCountByStatioId(Map<String, Object> params);
	 public List<ZgAQI> findList(Map<String, Object> params);
	 public List<Map<String,Object>> getHour(@Param(value="statioId")String statioId);
	 public List<Map<String,Object>> getDay(@Param(value="statioId")String statioId);
	 /**
	 * 根据城市站点获取站点列表
	 * @param statioId
	 * @return
	 */
	public List<Map<String,Object>>  getStationListByCity(String statioId);
}
