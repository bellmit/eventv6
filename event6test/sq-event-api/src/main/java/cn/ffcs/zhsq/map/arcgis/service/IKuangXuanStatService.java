package cn.ffcs.zhsq.map.arcgis.service;


import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;

/**
 * 2014-05-16 liushi add 
 * arcgis关于框选统计的统一接口
 * @author liushi
 */
public interface IKuangXuanStatService {
	
	/**
	 * 2014-11-10 liushi 
	 * 根据params的参数获取统计信息
	 * @param params
	 * @return
	 */
	public int statOfKuangXuan(Map<String, Object> params);
	
	public EUDGPagination statOfKuangXuanList(int pageNo, int pageSize, Map<String, Object> params);
	
	public String getKuangXuanPagePath(String kuangXuanType);
}
