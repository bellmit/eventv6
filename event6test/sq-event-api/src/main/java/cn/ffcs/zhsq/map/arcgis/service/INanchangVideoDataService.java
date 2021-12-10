package cn.ffcs.zhsq.map.arcgis.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;

public interface INanchangVideoDataService {

	public EUDGPagination listCams( Integer pageNo, Integer pageSize,
			Map<String, Object> params);
	
	public List<Map<String, Object>> findCamPointlistByParams(Map<String, Object> params);

}
