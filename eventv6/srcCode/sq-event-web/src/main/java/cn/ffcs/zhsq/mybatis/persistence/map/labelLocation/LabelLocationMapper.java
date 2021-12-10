package cn.ffcs.zhsq.mybatis.persistence.map.labelLocation;

import cn.ffcs.zhsq.mybatis.domain.map.labelLocation.BuildingLLInfo;
import cn.ffcs.zhsq.mybatis.domain.map.labelLocation.ComponentsLLInfo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 2014-05-22 liushi add 地图配置信息
 * 
 * @author Administrator
 * 
 */
public interface LabelLocationMapper {
	/**
	 * 楼宇定位信息列表查询
	 * @param param
	 * @param bounds
	 * @return
	 */
	public List<BuildingLLInfo> findPageBuildingCriteria(Map<String, Object> param, RowBounds bounds);

	/**
	 * 楼宇定位信息查询总数
	 * @param param
	 * @return
	 */
	public int findCountBuildingCriteria(Map<String, Object> param);

	/**
	 * 部件定位信息列表查询
	 * @param param
	 * @param bounds
	 * @return
	 */
	public List<ComponentsLLInfo> findPageComponentsCriteria(Map<String, Object> param, RowBounds bounds);

	/**
	 * 部件定位信息查询总数
	 * @param param
	 * @return
	 */
	public int findCountComponentsCriteria(Map<String, Object> param);

	/**
	 * 获取楼宇地理位置信息
	 * @param param
	 * @return
	 */
	public BuildingLLInfo findBuildingLLInfo(Map<String, Object> param);
}
