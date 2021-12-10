package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IKuangXuanStatService;

@Service(value = "kuangXuanStatOfFireHydrant")
public class KuangXuanStatOfFireHydrantServiceImpl implements IKuangXuanStatService {

	@Autowired
	protected IGisInfoService gisInfoService;

	/**
	 * 根据params的参数获取新消防栓的统计信息
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public int statOfKuangXuan(Map<String, Object> params) {
		params.put("resTypeCode", "'0601','0602'");
		int count = gisInfoService.getFireResourceCount(params);// 消防栓
		return count;
	}

	@Override
	public EUDGPagination statOfKuangXuanList(int pageNo, int pageSize, Map<String, Object> params) {
		params.put("resTypeCode", "'0601','0602'");
		EUDGPagination pagination = new EUDGPagination();
		if (pageNo == 1) {
			pagination.setTotal(gisInfoService.getFireResourceCount(params));
		}
		cn.ffcs.common.EUDGPagination fireResourcePagination = this.gisInfoService.getFireResourceList(pageNo, pageSize, params);
		Long listTotal = fireResourcePagination.getTotal();
		
		pagination.setRows(fireResourcePagination.getRows());
		pagination.setTotal(listTotal.intValue());
		
		return pagination;
	}

	@Override
	public String getKuangXuanPagePath(String kuangXuanType) {
		return "/map/arcgis/standardmappage/kuangxuan/kuangxuan_newResource.ftl";
	}
}
