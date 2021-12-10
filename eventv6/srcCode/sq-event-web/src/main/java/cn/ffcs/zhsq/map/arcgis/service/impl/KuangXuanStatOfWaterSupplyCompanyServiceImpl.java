package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IKuangXuanStatService;
import cn.ffcs.zhsq.mybatis.persistence.map.zhoubian.ZhouBianStatMapper;

@Service(value="kuangXuanStatOfWaterCompany")
public class KuangXuanStatOfWaterSupplyCompanyServiceImpl implements IKuangXuanStatService {
	
	@Autowired 
	protected IGisInfoService gisInfoService;
	@Autowired 
	protected ZhouBianStatMapper zhouBianStatMapper;
	
	@Override
	public int statOfKuangXuan(Map<String, Object> params) {
		String mapType = (String)params.get("mapType");
		String geoString = (String)params.get("geoString");
		params.put("markerType", "0604");
		int GridadminCount = gisInfoService.getWaterCompanyListboxChose(params);
		return GridadminCount;
	}

	@Override
	public EUDGPagination statOfKuangXuanList(int pageNo, int pageSize, Map<String, Object> params) {
		EUDGPagination pagination = new EUDGPagination();
		params.put("markerType", "0604");
		if (pageNo == 1) {
			pagination.setTotal(gisInfoService.getWaterCompanyListboxChose(params));
		}
		List<Map<String, Object>> list = gisInfoService.statOfWaterCompanyKuangXuanListPage(pageNo, pageSize, params);
		pagination.setRows(list);
		return pagination;
	}

	@Override
	public String getKuangXuanPagePath(String kuangXuanType) {
		return "/map/arcgis/standardmappage/kuangxuan/kuangxuan_waterSupplyCompany.ftl";
	}
}
