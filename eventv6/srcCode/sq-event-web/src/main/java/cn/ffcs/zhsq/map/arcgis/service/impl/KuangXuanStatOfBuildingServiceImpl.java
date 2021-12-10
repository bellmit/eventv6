package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IKuangXuanStatService;
import cn.ffcs.zhsq.mybatis.persistence.map.zhoubian.ZhouBianStatMapper;

@Service(value="kuangXuanStatOfBuilding")
public class KuangXuanStatOfBuildingServiceImpl implements IKuangXuanStatService {
	@Autowired 
	protected ZhouBianStatMapper zhouBianStatMapper;
	@Autowired 
	protected IGisInfoService gisInfoService;
	
	@Override
	public int statOfKuangXuan(Map<String, Object> params) {
		String mapType = (String)params.get("mapType");
		String geoString = (String)params.get("geoString");
//		Long startGridId = (Long)params.get("startGridId");
//		String infoOrgCode = (String)params.get("infoOrgCode");
		int GridadminCount = gisInfoService.getBuildingListboxChose(params);
		return GridadminCount;
	}

	@Override
	public EUDGPagination statOfKuangXuanList(int pageNo, int pageSize, Map<String, Object> params) {
		EUDGPagination pagination = new EUDGPagination();
//		String markerType = ConstantValue.NON_PUBLIC_ORG_MARKER_TYPE;
//		params.put("markerType", markerType);
		if (pageNo == 1) {
			pagination.setTotal(gisInfoService.getBuildingListboxChose(params));
		}
		List<Map<String, Object>> list = gisInfoService.statOfBuildingKuangXuanListPage(pageNo, pageSize, params);
		pagination.setRows(list);
		return pagination;
	}

	@Override
	public String getKuangXuanPagePath(String kuangXuanType) {
		return "/map/arcgis/standardmappage/kuangxuan/kuangxuan_building.ftl";
	}
}
