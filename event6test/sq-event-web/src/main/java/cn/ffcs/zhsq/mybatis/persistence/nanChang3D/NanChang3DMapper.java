package cn.ffcs.zhsq.mybatis.persistence.nanChang3D;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPoint;

public interface NanChang3DMapper {
	
	/**
	 * 查找网格数据
	 * */
	public List<Map<String,Object>> findGridData(Map<String,Object> params);
	
	/**
	 * 查找多发区县TOP10数据 年月
	 * */
	public List<Map<String,Object>> findMultipleCountiesData(Map<String,Object> params);
	
	/**
	 * 查找多发区县TOP10数据 天
	 * */
	public List<Map<String,Object>> findMultipleCountiesDataByDay(Map<String,Object> params);
	
	/**
	 * 查找多发区县TOP10数据 当天实时
	 * */
	public List<Map<String,Object>> findMultipleCountiesDataByDayAndRealTime(Map<String,Object> params);
	
	/**
	 * 查找多发点位TOP10数据 年月
	 * */
	public List<Map<String,Object>> findPointData(Map<String,Object> params);
	
	/**
	 * 查找多发点位TOP10数据 天
	 * */
	public List<Map<String,Object>> findPointDataByDay(Map<String,Object> params);

	/**
	 * 查找多发点位TOP10数据 当天实时
	 * */
	public List<Map<String,Object>> findPointDataByDayRealTime(Map<String,Object> params);
	
	/**
	 * 获取区域点位责任人
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> findCivilizedPerInfoData(Map<String,Object> params);
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
	 * 点位列表
	 * @param params
	 * @return
	 */
	public List<ArcgisInfoOfPoint> getPointGisByOrgCode(Map<String,Object> params,RowBounds rowBounds);
	/**
	 * 点位列表数量
	 * @param params
	 * @return
	 */
	public Integer countPointGisByOrgCode(Map<String,Object> params);
	/**
	 * 查询热力图权重(日)
	 * */
	public List<Map<String,Object>> findHeatWeightsDay(Map<String,Object> params);
	
	
	/**
	 * 查询热力图权重(年月)
	 * */
	public List<Map<String,Object>> findHeatWeights(Map<String,Object> params);
	
	
	
	/**
	 * 查询热力图权重(当日)
	 * */
	public List<Map<String,Object>> findHeatWeightSrealTime (Map<String,Object> params);
	
	/**
	 * 查询街道下点位坐标
	 * */
	public List<Map<String,Object>> findPointGisBy(Map<String,Object> params);
	
	
	/**
	 * 事件统计(年月)
	 * @param params
	 * @return
	 */
	public  Map<String, Object> findEventStat(Map<String, Object> params) ;
	/**
	 * 事件统计(天)
	 * @param params
	 * @return
	 */
	public  Map<String, Object> findEventStatByDay(Map<String, Object> params) ;
	/**
	 * 事件统计(当天)
	 * @param params
	 * @return
	 */
	public  Map<String, Object> findEventStatByDayAndRealTime(Map<String, Object> params) ;
	
	
	/**
	 * 查询3d柱状图数据(年月)
	 * */
	public List<Map<String,Object>> findGeometryData(Map<String,Object> params);
	
	
	/**
	 * 查询3d柱状图数据Max(年月)
	 * */
	public Map<String,Object> findGeometryMax(Map<String,Object> params);
	
	/**
	 * 查询3d柱状图数据(日)
	 * */
	public List<Map<String,Object>> findGeometryDataDay(Map<String,Object> params);
	
	/**
	 * 查询3d柱状图数据Max(日)
	 * */
	public Map<String,Object> findGeometryMaxDay(Map<String,Object> params);
	
	
	/**
	 * 查询3d柱状图数据(当日)
	 * */
	public List<Map<String,Object>> findGeometryDataSrealTime(Map<String,Object> params);
	
	
	/**
	 * 查询辖区坐标
	 * */
	public List<Map<String,Object>> findDCGridXY(Map<String,Object> params);

	/**
	 * 根据日期区间查询 最大最小主键
	 * @param param
	 * @return
	 */
	public Map<String,Object> findEventIdByDate(Map<String,Object> param);
	
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
	 * 待办记录统计
	 * @param param
	 * 			curUserId	办理人id
	 * 			curOrgId	办理人所属组织id
	 * @return
	 */
	public int findCount4Todo(Map<String, Object> param);

	/**
	 * 待办列表记录
	 * @param param 参数
	 * 			curUserId	办理人id
	 * 			curOrgId	办理人所属组织id
	 * @param bounds 分页信息
	 * @return
	 */
	public List<Map<String, Object>> findPageList4Todo(Map<String, Object> param, RowBounds bounds);

	public List<ArcgisInfoOfPoint> findFieldWorkData(Map<String, Object> params);
	
	
 
}
