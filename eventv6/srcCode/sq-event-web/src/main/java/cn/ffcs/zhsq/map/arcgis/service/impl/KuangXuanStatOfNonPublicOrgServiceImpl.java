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
 * 框选新经济组织服务
 * @author sulch
 * 2015年12月4日 上午10:59:54
 */
@Service(value="kuangXuanStatOfNonPublicOrg")
public class KuangXuanStatOfNonPublicOrgServiceImpl implements IKuangXuanStatService {
	
	@Autowired 
	protected IGisInfoService gisInfoService;
	
	/**
	 * 根据params的参数获取新经济组织的统计信息
	 */
	@Override
	public int statOfKuangXuan(Map<String, Object> params) {
		String markerType = ConstantValue.NON_PUBLIC_ORG_MARKER_TYPE;
		params.put("markerType", markerType);
		int rentRoomCount = gisInfoService.getNonPublicOrgListboxChose(params);//新经济组织
		return rentRoomCount;
	}

	@Override
	public EUDGPagination statOfKuangXuanList(int pageNo, int pageSize, Map<String, Object> params) {
		EUDGPagination pagination = new EUDGPagination();
		String markerType = ConstantValue.NON_PUBLIC_ORG_MARKER_TYPE;
		params.put("markerType", markerType);
		if (pageNo == 1) {
			pagination.setTotal(gisInfoService.getNonPublicOrgListboxChose(params));
		}
		List<Map<String, Object>> list = gisInfoService.statOfNonPublicOrgsKuangXuanListPage(pageNo, pageSize, params);
		pagination.setRows(list);
		return pagination;
	}

	@Override
	public String getKuangXuanPagePath(String kuangXuanType) {
		return "/map/arcgis/standardmappage/kuangxuan/kuangxuan_nonPublicOrg.ftl";
	}
}
