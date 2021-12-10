package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.zzgl.domain.db.ImportUnit;
import cn.ffcs.shequ.zzgl.service.main.IImportUnitService;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IKuangXuanStatService;
import cn.ffcs.zhsq.mybatis.persistence.map.zhoubian.ZhouBianStatMapper;

@Service(value="kuangXuanStatOfImportUnit")
public class KuangXuanStatOfImportUnitServiceImpl implements IKuangXuanStatService {
	
	@Autowired 
	protected IGisInfoService gisInfoService;
	@Autowired 
	protected ZhouBianStatMapper zhouBianStatMapper;
	@Autowired
	private IImportUnitService importUnitService;//重点单位
	
	@Override
	public int statOfKuangXuan(Map<String, Object> params) {
		String mapType = (String)params.get("mapType");
		String geoString = (String)params.get("geoString");
//		Long startGridId = (Long)params.get("startGridId");
//		String infoOrgCode = (String)params.get("infoOrgCode");statOfOrgTeamKuangXuanListPage
		int GridadminCount = gisInfoService.getImportUnitListboxChose(params);
		return GridadminCount;
	}

	@Override
	public EUDGPagination statOfKuangXuanList(int pageNo, int pageSize, Map<String, Object> params) {
		EUDGPagination pagination = new EUDGPagination();
//		String markerType = ConstantValue.NON_PUBLIC_ORG_MARKER_TYPE;
//		params.put("markerType", markerType);
		if (pageNo == 1) {
			pagination.setTotal(gisInfoService.getImportUnitListboxChose(params));
		}
		List<Map<String, Object>> list = gisInfoService.statOfImportUnitKuangXuanListPage(pageNo, pageSize, params);
		formatImportUnit(list);
		pagination.setRows(list);
		return pagination;
	}

	private void formatImportUnit(List<Map<String, Object>> importUnitlist){
		if(importUnitlist!=null && importUnitlist.size()>0){
			for(Map<String, Object> map : importUnitlist){
				Object importUnitIdObj = map.get("IMPORT_UNIT_ID");
				if(importUnitIdObj != null){
					Long importUnitId = Long.valueOf(importUnitIdObj.toString());
					ImportUnit importUnit = importUnitService.getImportUnitById(importUnitId);
					if(importUnit != null){
						map.put("TYPE_LABEL", importUnit.getTypeLabel());
					}
				}
			}
		}
	}
	
	@Override
	public String getKuangXuanPagePath(String kuangXuanType) {
		return "/map/arcgis/standardmappage/kuangxuan/kuangxuan_importUnit.ftl";
	}
}
