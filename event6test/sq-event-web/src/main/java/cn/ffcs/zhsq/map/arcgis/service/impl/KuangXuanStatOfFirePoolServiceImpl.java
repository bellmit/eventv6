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
 * 消防水池框选服务
 * 
 * @author huangmw
 * 
 */
@Service(value = "kuangXuanStatOfFirePoolService")
public class KuangXuanStatOfFirePoolServiceImpl implements IKuangXuanStatService {

	@Autowired
	protected IGisInfoService gisInfoService;

	@Override
	public int statOfKuangXuan(Map<String, Object> params) {
		params.put("markerType", ConstantValue.MARKER_TYPE_FIRE_POOL);
		int GridadminCount = gisInfoService.getFirePoolListboxChose(params);
		return GridadminCount;
	}

	@Override
	public EUDGPagination statOfKuangXuanList(int pageNo, int pageSize, Map<String, Object> params) {
		params.put("markerType", ConstantValue.MARKER_TYPE_FIRE_POOL);

		EUDGPagination pagination = new EUDGPagination();

		if (pageNo == 1) {
			pagination.setTotal(gisInfoService.getFirePoolListboxChose(params));
		}

		List<Map<String, Object>> list = gisInfoService.statOfFirePoolKuangXuanListPage(pageNo, pageSize, params);
		pagination.setRows(list);
		return pagination;
	}

	@Override
	public String getKuangXuanPagePath(String kuangXuanType) {
		return "/map/arcgis/standardmappage/kuangxuan/kuangxuan_newResource.ftl";
	}
}
