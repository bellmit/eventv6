package cn.ffcs.zhsq.nanChang3D.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPoint;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;

public interface INanChang3DService {
	
	/**
	 * 查找网格数据
	 * */
	public List<Map<String,Object>> findGridData(Map<String,Object> params);
	
	
	/**
	 * 查找多发区县TOP10数据
	 * */
	public List<Map<String,Object>> findMultipleCountiesData(Map<String,Object> params);
	
	
	/**
	 * 查找多发点位TOP10数据
	 * */
	public List<Map<String,Object>> findPointData(Map<String,Object> params);
	
	
	/**
	 * 获取区域点位责任人
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> findCivilizedPerInfoData(Map<String,Object> params);
	
	/**
	 *  排序方法
	 * @param params
	 * 		params list1
	 * 		params list2
	 * 		params key list1 和 list2 关联的字段
	 * 		PS:统计字段 统一取名 TOTAL_
	 * @return
	 */
	
	public List<Map<String,Object>> sortOrderByTotalDesc(List<Map<String,Object>> list1,List<Map<String,Object>> list2,String key);
	
	
	/**
	 * 视频数量统计
	 * @param params
	 * @return
	 */
	public  Map<String, Object> getMontiorStat(Map<String, Object> params) ;
	/**
	 * 点位数量统计
	 * @param params
	 * @return
	 */
	public  Map<String, Object> getPointStat(Map<String, Object> params) ;
	/**
	 * 点位撒点
	 * @param params
	 * @return
	 */
	public List<ArcgisInfoOfPoint> getPointGisByOrgCode(Map<String,Object> params);
	
	/**
	 * 点位列表
	 * @param params
	 * @return
	 */
	public EUDGPagination findPointListByParams( Integer pageNo, Integer pageSize,
			Map<String, Object> params);
	
	/**
	 * 查找热力图权重
	 * */
	public Map<String, Object> findHeatWeights(Map<String, Object> params);

	
	/**
	 * 查找3d柱状图(区县)
	 * */
	public Map<String, Object> findGeometryData(Map<String, Object> params);
	
	

	/**
	 * 查找3d柱状图（点位）
	 * */
	public Map<String, Object> findGeometryPoint(Map<String, Object> params);
	
	
	
	/**
	 * 事件统计
	 * @param params
	 * @return
	 */
	public  Map<String, Object> getEventStat(Map<String, Object> params) ;
	
	
	/**
	 * 查找3d柱状图数据max
	 * */
	public Map<String, Object> findGeometryMax(Map<String, Object> params);
	
	
	/**
	 * 获取点位经纬度
	 * @param params
	 * @return
	 */
	public Map<String,Object> getPointGis(Map<String,Object> params);
	
	/**
	 * 获取点位必检类型
	 * @param pointId
	 * @return
	 */
	public String getPointCheckTypeById(Long pointId);
	
	/**
	 * 分页获取待办事件（待办列表+审核列表），以创建时间排序
	 * @param params
	 * @return
	 */
	public EUDGPagination findLargeDataPlatformEvenListPagination(int pageNo,
			int pageSize, Map<String, Object> params) throws ZhsqEventException;
	
	
	public List<ArcgisInfoOfPoint> findFieldWorkData(Map<String,Object> params);
}
