package cn.ffcs.zhsq.mybatis.persistence.map.arcgis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.GisStatConfig;

public interface GisStatConfigMapper extends MyBatisBaseMapper<GisStatConfig> {
	public List<GisStatConfig> getGisStatConfig(@Param(value = "statType") String statType,
			@Param(value = "bizType") String bizType, @Param(value = "regionCode") String regionCode);

	public String getCatagories(@Param(value = "statCfgId") long statCfgId);

	public GisStatConfig getGisStatConfigById(@Param(value = "statCfgId") long statCfgId);

	public int delete(GisStatConfig gisStatConfig);

	public int repeatCountForBizType(Map<String, Object> params);
	
	public int hasClassification(Map<String, Object> params);
}
