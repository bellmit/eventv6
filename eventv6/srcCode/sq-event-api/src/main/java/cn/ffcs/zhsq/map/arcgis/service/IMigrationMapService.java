package cn.ffcs.zhsq.map.arcgis.service;

import java.util.List;
import java.util.Map;

public interface IMigrationMapService {

	/**
	 * 获取迁徙数据
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> findMigrationData(Map<String, Object> params);
	
}
