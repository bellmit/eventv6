package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IKuangXuanStatService;

/**
 * 框选出租屋服务
 * @author sulch
 * 2015年12月3日 下午3:01:08
 */
@Service(value="kuangXuanStatOfRentRoom")
public class KuangXuanStatOfRentRoomServiceImpl implements IKuangXuanStatService {
	
	@Autowired 
	protected IGisInfoService gisInfoService;
	
	/**
	 * 根据params的参数获取出租屋的统计信息
	 */
	@Override
	public int statOfKuangXuan(Map<String, Object> params) {
		int rentRoomCount = gisInfoService.getRentRoomListboxChose(params);//出租屋
		return rentRoomCount;
	}

	@Override
	public EUDGPagination statOfKuangXuanList(int pageNo, int pageSize, Map<String, Object> params) {
		EUDGPagination pagination = new EUDGPagination();
		if (pageNo == 1) {
			pagination.setTotal(gisInfoService.getRentRoomListboxChose(params));
		}
		List<Map<String, Object>> list = gisInfoService.statOfRentRoomsKuangXuanListPage(pageNo, pageSize, params);
		pagination.setRows(list);
		return pagination;
	}

	@Override
	public String getKuangXuanPagePath(String kuangXuanType) {
		return "/map/arcgis/standardmappage/kuangxuan/kuangxuan_rentRoom.ftl";
	}
}
