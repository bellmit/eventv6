package cn.ffcs.zhsq.map.arcgis.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IKuangXuanStatService;

@Service(value="kuangXuanStatOfLds")
public class KuangXuanStatOfLdsServiceImpl implements IKuangXuanStatService {
	
	@Autowired 
	protected IGisInfoService gisInfoService;
	
	/**
	 * 2014-11-10 liushi 
	 * 根据params的参数获取流动人口的统计信息
	 * @param params
	 * @return
	 */
	@Override
	public int statOfKuangXuan(Map<String, Object> params) {
		String mapType = (String)params.get("mapType");
		String geoString = (String)params.get("geoString");
		String infoOrgCode = (String)params.get("infoOrgCode");
		int ldsCount = gisInfoService.getCZSorLDSListboxChose(geoString,mapType,"流动人口",infoOrgCode,null);//流动人口
		return ldsCount;
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
