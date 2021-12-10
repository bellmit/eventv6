package cn.ffcs.zhsq.mybatis.persistence.map.arcgis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.GisStatContConfig;

public interface GisStatContConfigMapper extends MyBatisBaseMapper<GisStatContConfig> {
	public List<GisStatContConfig> getGisStatContConfigs(@Param(value = "statCfgId") long statCfgId);

	public List<GisStatContConfig> getNoObjClassGisStatContConfigs(@Param(value = "statCfgId") long statCfgId);

	public List<GisStatContConfig> getGisStatContConfigs(@Param(value = "statCfgId") long statCfgId,
			@Param(value = "objClass") String objClass);

	public int deleteByStatCfgId(Map<String, Object> params);

	public int deleteGisStatContConfigs(@Param(value = "statCfgId") long statCfgId);
}
