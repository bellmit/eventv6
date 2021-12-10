package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IKuangXuanStatService;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 框选现役消防队服务
 */
@Service(value = "kuangXuanStatOfActiveFireTeamService")
public class KuangXuanStatOfActiveFireTeamServiceImpl implements IKuangXuanStatService {

	@Autowired
	protected IGisInfoService gisInfoService;

	@Override
	public int statOfKuangXuan(Map<String, Object> params) {
		params.put("markerType", ConstantValue.MARKER_TYPE_FIRE_XF_XY);
		int count = gisInfoService.getFireTeamListBoxChoseCount(params);
		return count;
	}

	@Override
	public EUDGPagination statOfKuangXuanList(int pageNo, int pageSize, Map<String, Object> params) {
		params.put("markerType", ConstantValue.MARKER_TYPE_FIRE_XF_XY);
		EUDGPagination pagination = new EUDGPagination();
		if (pageNo == 1) {
			pagination.setTotal(gisInfoService.getFireTeamListBoxChoseCount(params));
		}
		List<Map<String, Object>> list = gisInfoService.statOfFireTeamKuangXuanListPage(pageNo, pageSize, params);
		pagination.setRows(list);
		return pagination;
	}

	@Override
	public String getKuangXuanPagePath(String kuangXuanType) {
		return "/map/arcgis/standardmappage/kuangxuan/kuangxuan_activeFireTeam.ftl";
	}
}
