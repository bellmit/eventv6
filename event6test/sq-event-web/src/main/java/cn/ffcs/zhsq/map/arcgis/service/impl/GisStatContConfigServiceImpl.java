package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.map.arcgis.service.IGisStatContConfigService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.GisStatContConfig;
import cn.ffcs.zhsq.mybatis.persistence.map.arcgis.GisStatContConfigMapper;

@Service(value = "gisStatContConfigServiceImpl")
public class GisStatContConfigServiceImpl implements IGisStatContConfigService {
	@Autowired
	private GisStatContConfigMapper gisStatContConfigMapper;
	
	@Override
	public List<GisStatContConfig> getGisStatContConfigs(long statCfgId, String objClass) {
		return gisStatContConfigMapper.getGisStatContConfigs(statCfgId, objClass);
	}
	
	@Override
	public List<GisStatContConfig> getNoObjClassGisStatContConfigs(long statCfgId) {
		return gisStatContConfigMapper.getNoObjClassGisStatContConfigs(statCfgId);
	}
	
	@Override
	public Long saveGisStatContConfig(GisStatContConfig gisStatContConfig) {
		int result = gisStatContConfigMapper.insert(gisStatContConfig);
		return result > 0 ? gisStatContConfig.getStatCfgId() : -1L;
	}

	@Override
	public boolean updateGisStatContConfig(GisStatContConfig gisStatContConfig) {
		int result = gisStatContConfigMapper.update(gisStatContConfig);
		return result > 0;
	}

	@Override
	public boolean deleteGisStatContConfig(Map<String, Object> params) {
		int result = gisStatContConfigMapper.deleteByStatCfgId(params);
		return result > 0;
	}
	
	@Override
	public boolean deleteGisStatContConfigs(long statCfgId) {
		int result = gisStatContConfigMapper.deleteGisStatContConfigs(statCfgId);
		return result > 0;
	}

}
