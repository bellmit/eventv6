package cn.ffcs.zhsq.mybatis.persistence.map.zhoubian;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;

/**
 * 2015-03-12 周边统计
 * @author liush
 *
 */
public interface ZhouBianStatMapper {
	/**
	 * 2015-03-12 liushi add 根据markerType进行区分,以参数ids进行查询定位数据
	 * @param params markerType 资源类的查询还有识别码; ids定位数据的业务id集合;  mapType 地图类型
	 * @return
	 */
	public List<ArcgisInfoOfPublic> statOfResZhouBianListByIds(Map<String, Object> params);
	
	/* start: 周边重点场所相关接口 */
	/**
	 * 2015-03-17 yangcq add 根据markerType进行区分,周边资源查询（可通用所有的资源定位类型）
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; mapType 地图类型
	 * @return
	 */
	public int statOfKeyPlacesZhouBianCount(Map<String, Object> params);
	
	/**
	 * 2015-03-18 liushi add 网格员周边资源统计
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; mapType 地图类型
	 * @return
	 */
	public int statOfGridAdminZhouBianCount(Map<String, Object> params);
	public int statJYGridAdminZhouBianCount(Map<String, Object> params);

	/**
	 * 2015-03-18 liushi add 网格员周边资源统计
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; mapType 地图类型
	 * @return
	 */
	public List<Map<String,Object>> statOfGridAdminZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	public List<Map<String,Object>> statJYGridAdminZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	
	
	//不分页查询网格员
	public List<Map<String,Object>> statOfGridAdminZhouBianListWithoutPage(Map<String, Object> params);
	public List<Map<String,Object>> statJYGridAdminZhouBianListWithoutPage(Map<String, Object> params);

	public int statOfBuildingZhouBianCount(Map<String, Object> params);
	
	public List<Map<String,Object>> statOfBuildingZhouBianListPage(Map<String, Object> params, RowBounds bounds);

	public int statOfOrgTeamZhouBianCount(Map<String, Object> params);

	public List<Map<String, Object>> statOfOrgTeamZhouBianListPage(Map<String, Object> params, RowBounds rowBounds);

	public int statOfPrvetionTeamZhouBianCount(Map<String, Object> params);

	public List<Map<String, Object>> statOfPrvetionTeamZhouBianListPage(Map<String, Object> params, RowBounds rowBounds);

	/**
	 * 2015-03-17 yangcq add 根据markerType进行区分，周边资源分页查询定位数据
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; mapType 地图类型
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> statOfKeyPlacesZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	/* end: 周边重点场所相关接口 */
	/* start: 周边消防队相关接口 */
	/**
	 * 2015-03-12 liushi add 根据markerType进行区分,周边资源查询（可通用所有的资源定位类型）
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; mapType 地图类型
	 * @return
	 */
	public int statOfFireTeamZhouBianCount(Map<String, Object> params);
	
	/**
	 * 2015-03-12 liushi add 根据markerType进行区分，周边资源分页查询定位数据
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; mapType 地图类型
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> statOfFireTeamZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	/* end: 周边消防队相关接口 */
	
	/* start: 周边全球眼相关接口 */
	public int statOfGlobalEyesZhouBianCount(Map<String, Object> params);
	
	public List<Map<String, Object>> statOfGlobalEyesZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	
	//不带分页的查询
	public List<Map<String, Object>> statOfGlobalEyesZhouBianListWithoutPage(Map<String, Object> params);
	/* end: 周边全球眼相关接口 */
	
	/* start: 周边人口相关接口 */
	/**
	 * 2015-03-17 huangmw add 根据markerType进行区分,周边资源查询（可通用所有的资源定位类型）
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; mapType 地图类型
	 * @return
	 */
	public int statOfPeopleZhouBianCount(Map<String, Object> params);
	/* end: 周边人口相关接口 */
	
	/* start: 周边消防栓、自来水公司、天然水源相关接口 */
	/**
	 * 2015-03-19 sulch add 根据markerType进行区分,周边资源查询（可通用所有的资源定位类型）
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; mapType 地图类型
	 * @return
	 */
	public int statOfNewResourceZhouBianCount(Map<String, Object> params);
	public int statOfWaterFactoryZhouBianCount(Map<String, Object> params);
	public List<Map<String, Object>> statOfWaterFactoryZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	public int statOfWaterSourceZhouBianCount(Map<String, Object> params);
	public List<Map<String, Object>> statOfWaterSourceZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	
	public int getZKSListboxChoseByOrgCode(Map<String, Object> params);
	public List<Map<String, Object>> getZKSListboxChoseByOrgCodePageList(Map<String, Object> params, RowBounds bounds);
	
	/**
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; mapType 地图类型
	 * @return
	 */
	public int statOfResourceZhouBianCount(Map<String, Object> params);
	
	/**
	 * 2015-03-19 sulch add 根据markerType进行区分，周边资源分页查询定位数据
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; mapType 地图类型
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> statOfNewResourceZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	/* end: 周边消防栓、自来水公司、天然水源相关接口 */
	
	
	/* start: 周边校园周边相关接口 */
	/**
	 * 2015-03-19 sulch add 根据markerType进行区分,周边资源查询（可通用所有的资源定位类型）
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; mapType 地图类型
	 * @return
	 */
	public int statOfCampusZhouBianCount(Map<String, Object> params);
	
	/**
	 * 2015-03-19 sulch add 根据markerType进行区分，周边资源分页查询定位数据
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; mapType 地图类型
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> statOfCampusZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	/* end: 周边校园周边相关接口 */
	
	/* start: 周边重点单位相关接口  */
	public int statOfImportUnitZhouBianCount(Map<String, Object> params);
	public List<Map<String, Object>> statOfImportUnitZhouBianListPage(Map<String, Object> params);
	public List<Map<String, Object>> statOfImportUnitZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	/* end: 周边重点单位相关接口  */
	
	/**
	 * 2015-03-17 yangcq add 根据markerType进行区分,周边资源查询（可通用所有的资源定位类型）
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; mapType 地图类型
	 * @return
	 */
	public int statOfCorBaseZhouBianCount(Map<String, Object> params);
	
	/**
	 * 2015-03-17 yangcq add 根据markerType进行区分，周边资源分页查询定位数据
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; mapType 地图类型
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> statOfCorBaseZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	
	/* start: 周边出租屋相关接口 */
	/**
	 * 2015-12-04 sulch add 根据markerType进行区分,周边资源查询（可通用所有的资源定位类型）
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）;mapType 地图类型
	 * @return
	 */
	public int statOfRentRoomsZhouBianCount(Map<String, Object> params);
	
	/**
	 * 2015-12-04 sulch add 根据markerType进行区分，周边资源分页查询定位数据
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; mapType 地图类型
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> statOfRentRoomsZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	/* end: 周边出租屋相关接口 */
	
	/* start: 周边新经济组织相关接口 */
	/**
	 * 2015-12-07 sulch add 根据markerType进行区分,周边资源查询（可通用所有的资源定位类型）
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）;mapType 地图类型
	 * @return
	 */
	public int statOfNonPublicOrgsZhouBianCount(Map<String, Object> params);
	
	/**
	 * 2015-12-07 sulch add 根据markerType进行区分，周边资源分页查询定位数据
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; mapType 地图类型
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> statOfNonPublicOrgsZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	/* end: 周边新经济组织相关接口 */
	
	/* start: 周边新社会组织相关接口 */
	/**
	 * 2015-12-07 sulch add 根据markerType进行区分,周边资源查询（可通用所有的资源定位类型）
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）;mapType 地图类型
	 * @return
	 */
	public int statOfOrganizationsZhouBianCount(Map<String, Object> params);
	
	/**
	 * 2015-12-07 sulch add 根据markerType进行区分，周边资源分页查询定位数据
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; mapType 地图类型
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> statOfOrganizationsZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	/* end: 周边新社会组织相关接口 */
	
	/**
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; mapType 地图类型
	 * @return
	 */
	public int statOfZdPeopleZhouBianCount(Map<String, Object> params);
	
	/**
	 * 根据markerType进行区分，周边资源分页查询定位数据
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; mapType 地图类型
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> statOfZdPeopleZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	public List<Map<String, Object>> statOfResourceZhouBianCountPageList(Map<String, Object> params, RowBounds bounds);
	
	/**
	 * 根据markerType进行区分,周边资源查询（可通用所有的资源定位类型）
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; mapType 地图类型
	 * @return
	 */
	public int statOfFireTeamNewZhouBianCount(Map<String, Object> params);
	
	/**
	 * 根据markerType进行区分，周边资源分页查询定位数据
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; mapType 地图类型
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> statOfFireTeamNewZhouBianListPage(Map<String, Object> params, RowBounds bounds);
	
}
