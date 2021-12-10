package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.map.arcgis.service.IGisStatConfigService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.GisStatConfig;
import cn.ffcs.zhsq.mybatis.persistence.map.arcgis.GisStatConfigMapper;

@Service(value = "gisStatConfigServiceImpl")
public class GisStatConfigServiceImpl implements IGisStatConfigService {
	@Autowired
	private GisStatConfigMapper gisStatConfigMapper;

	@Autowired
	private IMixedGridInfoService mixedGridInfoService;

	@Autowired
	private IBaseDictionaryService baseDictionaryService;

	@Override
	public Long saveGisStatConfig(GisStatConfig gisStatConfig) {
		int result = gisStatConfigMapper.insert(gisStatConfig);
		return result > 0 ? gisStatConfig.getStatCfgId() : -1L;
	}

	@Override
	public boolean updateGisStatConfig(GisStatConfig gisStatConfig) {
		int result = gisStatConfigMapper.update(gisStatConfig);
		return result > 0;
	}

	@Override
	public boolean deleteGisStatConfig(GisStatConfig gisStatConfig) {
		int result = gisStatConfigMapper.delete(gisStatConfig);
		return result > 0;
	}

	@Override
	public GisStatConfig getGisStatConfig(String statType, String bizType, String regionCode) {
		GisStatConfig gisStatConfig = null;

		List<GisStatConfig> gisStatConfigs = gisStatConfigMapper.getGisStatConfig(statType, bizType, regionCode);

		if (gisStatConfigs != null && gisStatConfigs.size() > 0) {
			gisStatConfig = gisStatConfigs.get(0);
		}

		return gisStatConfig;
	}

	@Override
	public String getCatagories(long statCfgId) {
		return gisStatConfigMapper.getCatagories(statCfgId);
	}

	@Override
	public GisStatConfig getGisStatConfigById(long statCfgId) {
		String homePageType = "";
		MixedGridInfo mixedGridInfo = null;

		GisStatConfig gisStatConfig = gisStatConfigMapper.getGisStatConfigById(statCfgId);
		mixedGridInfo = mixedGridInfoService.getDefaultGridByOrgCode(gisStatConfig.getRegionCode());

		if (mixedGridInfo.getParentGridId() != null) {
			gisStatConfig.setGridName(mixedGridInfo.getGridName());
		} else {
			gisStatConfig.setGridName("");
		}

		List<BaseDataDict> homePageTypes = baseDictionaryService.getDataDictListOfSinglestage("B559", null);

		homePageType = gisStatConfig.getBizType();

		if (!homePageType.isEmpty()) {
			for (BaseDataDict bdd : homePageTypes) {
				if (homePageType.equals(bdd.getDictGeneralCode())) {
					gisStatConfig.setBizName(bdd.getDictName());
				}
			}
		}

		return gisStatConfig;
	}

	@Override
	public EUDGPagination findGisStatConfigsPagination(int pageNo, int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = gisStatConfigMapper.findCountByCriteria(params);
		List<GisStatConfig> gisStatConfigs = gisStatConfigMapper.findPageListByCriteria(params, rowBounds);

		String homePageType = "";
		MixedGridInfo mixedGridInfo = null;
		List<BaseDataDict> homePageTypes = baseDictionaryService.getDataDictListOfSinglestage("B559", null);

		for (GisStatConfig gisStatConfig : gisStatConfigs) {
			mixedGridInfo = mixedGridInfoService.getDefaultGridByOrgCode(gisStatConfig.getRegionCode());

			if (mixedGridInfo.getParentGridId() != null) {
				gisStatConfig.setGridName(mixedGridInfo.getGridName());
			} else {
				gisStatConfig.setGridName("");
			}

			homePageType = gisStatConfig.getBizType();

			if (!homePageType.isEmpty()) {
				for (BaseDataDict bdd : homePageTypes) {
					if (homePageType.equals(bdd.getDictGeneralCode())) {
						gisStatConfig.setBizType(bdd.getDictName());
					}
				}
			}
		}

		EUDGPagination eudgPagination = new EUDGPagination(count, gisStatConfigs);
		return eudgPagination;
	}

	@Override
	public int repeatCountForBizType(Map<String, Object> params) {
		return gisStatConfigMapper.repeatCountForBizType(params);
	}

}
