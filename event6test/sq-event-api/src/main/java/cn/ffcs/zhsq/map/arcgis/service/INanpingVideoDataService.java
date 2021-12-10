package cn.ffcs.zhsq.map.arcgis.service;


import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;

public interface INanpingVideoDataService {
	
	/**
	 * 获取南平视频大屏的区域数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findData(Map<String, Object> params);
	
	
	/**
	 * 获取南平视频大屏的全球眼、消防、网格员、派出所数据
	 * @param params
	 * @return
	 */
	public Map<String, Object> findOtherData(Map<String, Object> params);
	
	/**
	 * 获取南平视频大屏的周边网格员
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> gridListData(Map<String, Object> params);
	
	/**
	 * 获取南平视频大屏的周边全球眼
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> globalsEyeList(Map<String, Object> params);
	
	
	/**
	 * 紧急待办事件列表 不分页
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findListByUrgencyDegree(Map<String, Object> params);
	
	/**
	 * 紧急待办事件列表 分页
	 * @param params
	 * @return
	 */
	public EUDGPagination findPagListByUrgencyDegree(int page, int rows, Map<String, Object> params); 
	
	

}
