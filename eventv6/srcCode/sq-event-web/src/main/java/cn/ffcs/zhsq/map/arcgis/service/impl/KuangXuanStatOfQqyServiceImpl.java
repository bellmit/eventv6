package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.wap.domain.pojo.ResMarkedOM;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IKuangXuanStatService;

@Service(value="kuangXuanStatOfQqy")
public class KuangXuanStatOfQqyServiceImpl implements IKuangXuanStatService {
	
	@Autowired 
	protected IGisInfoService gisInfoService;
	
	/**
	 * 2014-11-10 liushi 
	 * 根据params的参数获取全球眼的统计信息
	 * @param params
	 * @return
	 */
	@Override
	public int statOfKuangXuan(Map<String, Object> params) {
		String mapType = (String)params.get("mapType");
		String geoString = (String)params.get("geoString");
		String infoOrgCode = (String)params.get("infoOrgCode");
		int qqyCount = gisInfoService.getMarkedListboxChose(geoString, ResMarkedOM.MEGAEYES, ResMarkedOM.RESOURCES, mapType,infoOrgCode);//全球眼
		return qqyCount;
	}

	@Override
	public EUDGPagination statOfKuangXuanList(int pageNo, int pageSize, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKuangXuanPagePath(String kuangXuanType) {
		// TODO Auto-generated method stub
		return null;
	}
}
