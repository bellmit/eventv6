package cn.ffcs.zhsq.map.zhoubian.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;

/**
 * 2015-03-12 周边资源统计统一接口
 * @author liush
 *
 */
public interface IZhouBianStatService {
	/**
	 * 2015-03-12 liushi add 统计周边对应的业务ids（有可能是业务本身的id，也有可能是关联业务的id）
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码;  mapType 地图类型
	 * @return
	 */
	public String statOfZhouBianIds(Map<String, Object> params);
	
	/**
	 * 2015-03-12 liushi add 统计周边对应的统计数量
	 * @param params  x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码;  mapType 地图类型
	 * @return
	 */
	public int statOfZhouBianCount(Map<String, Object> params);
	
	/**
	 * 2015-03-12 liushi add 统计周边对应的统计列表
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; pageNo 分页号; pageSize 分页大小; mapType 地图类型
	 * @return
	 */
	public EUDGPagination statOfZhouBianList(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 2015-03-12 liushi add 周边资源对应的地图定位数据
	 * @param params markerType 资源类的查询还有识别码; ids定位数据的业务id集合;  mapType 地图类型
	 * @return
	 */
	public List<ArcgisInfoOfPublic> statOfZhouBianMapInfoList(Map<String, Object> params);
	
	public String getZhouBianPagePath(String zhoubianType);
}
