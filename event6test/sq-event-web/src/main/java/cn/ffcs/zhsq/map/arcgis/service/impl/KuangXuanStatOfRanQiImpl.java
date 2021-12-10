package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IKuangXuanStatService;

@Service(value = "kuangXuanStatOfRanQi")
public class KuangXuanStatOfRanQiImpl implements IKuangXuanStatService {
	@Autowired
	protected IGisInfoService gisInfoService;

	/**
	 * 根据params的参数获取燃气的统计信息
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public int statOfKuangXuan(Map<String, Object> params) {
		params.put("resTypeId", 11L);
		params.put("markerType", "020203");
		int policeCount = gisInfoService.getResObjectBoxChoseCount(params);
		return policeCount;
	}

	@Override
	public EUDGPagination statOfKuangXuanList(int pageNo, int pageSize, Map<String, Object> params) {
		EUDGPagination pagination = new EUDGPagination();
		params.put("resTypeId", 11L);
		params.put("markerType", "020203");
		if (pageNo == 1) {
			pagination.setTotal(gisInfoService.getResObjectBoxChoseCount(params));
		}
		cn.ffcs.common.EUDGPagination listPagination = gisInfoService.getResObjectBoxChosePageList(pageNo, pageSize, params);
		Long listTotal = listPagination.getTotal();
		
		pagination.setRows(listPagination.getRows());
		pagination.setTotal(listTotal.intValue());
		
		return pagination;
	}

	@Override
	public String getKuangXuanPagePath(String kuangXuanType) {
		return "/map/arcgis/standardmappage/kuangxuan/kuangxuan_res.ftl";
	}
}
