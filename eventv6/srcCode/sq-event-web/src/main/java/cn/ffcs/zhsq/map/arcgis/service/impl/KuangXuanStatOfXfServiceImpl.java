package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IKuangXuanStatService;

@Service(value="kuangXuanStatOfXf")
public class KuangXuanStatOfXfServiceImpl implements IKuangXuanStatService {
	
	@Autowired 
	protected IGisInfoService gisInfoService;
	
	/**
	 * 2015-8-11 liushi 
	 * 根据params的参数获取信访人员的统计信息
	 * @param params
	 * @return
	 */
	@Override
	public int statOfKuangXuan(Map<String, Object> params) {
		String mapType = (String)params.get("mapType");
		String geoString = (String)params.get("geoString");
		String infoOrgCode = (String)params.get("infoOrgCode");
		int sfCount = gisInfoService.getZKSListboxChose(geoString, "T_ZZ_PETITIONERS", mapType, null,infoOrgCode);//上访数
		return sfCount;
	}

	@Override
	public EUDGPagination statOfKuangXuanList(int pageNo, int pageSize, Map<String, Object> params) {
		EUDGPagination pagination = new EUDGPagination();
		String mapType = (String)params.get("mapType");
		String geoString = (String)params.get("geoString");
		String infoOrgCode = (String)params.get("infoOrgCode");
		params.put("mapType", mapType);
		params.put("geoString", geoString);
		params.put("infoOrgCode", infoOrgCode);
		params.put("peopleTypeTableName", "T_ZZ_PETITIONERS");
		params.put("empWay", null);
		if (pageNo == 1) {
			pagination.setTotal(gisInfoService.getZKSListboxChose(geoString, "T_ZZ_PETITIONERS", mapType, null,infoOrgCode));
		}
		List<Map<String, Object>> list = gisInfoService.getZKSListboxChoseByOrgCodePageList(pageNo, pageSize, params);
		pagination.setRows(list);
		return pagination;
	}

	@Override
	public String getKuangXuanPagePath(String kuangXuanType) {
		return "/map/arcgis/standardmappage/kuangxuan/kuangxuan_people.ftl";
	}
}
