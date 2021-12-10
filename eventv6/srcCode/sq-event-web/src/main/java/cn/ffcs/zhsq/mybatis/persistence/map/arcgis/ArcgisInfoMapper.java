package cn.ffcs.zhsq.mybatis.persistence.map.arcgis;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.map.arcgis.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.trajectory.LocateInfo;

/**
 * 2014-05-22 liushi add 地图配置信息
 * 
 * @author Administrator
 *
 */
public interface ArcgisInfoMapper extends MyBatisBaseMapper<ArcgisMapConfigInfo> {
	/**
	 * 2014-05-22 liushi add 根据infoOrgCode获取地图配置信息
	 * 
	 * @param infoOrgCode
	 * @param engineType
	 * @return
	 */
	public List<ArcgisMapConfigInfo> findGisMapConfigInfoByOrgCode(@Param(value = "infoOrgCode") String infoOrgCode,
			@Param(value = "engineType") String engineType);

	/**
	 * 2014-06-05 liushi add 根据查询条件判断符合条件的网格轮廓数据是否存在
	 * 
	 * @param wid
	 *            关联网格id
	 * @param mapt
	 *            当前地图类型
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridForIsExist(@Param(value = "wid") Long wid,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 2014-10-09 huangmw add 根据查询条件判断符合条件的片区网格轮廓数据是否存在
	 * 
	 * @param wid
	 *            关联网格id
	 * @param mapt
	 *            当前地图类型
	 * @return
	 */
	public List<ArcgisInfoOfSegmentGrid> getArcgisDataOfSegmentGridForIsExist(@Param(value = "wid") Long wid,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 2014-06-05 liushi add 修改网格轮廓数据信息
	 * 
	 * @param arcgisInfoOfGrid
	 * @return
	 */
	public int updateArcgisDataOfGrid(ArcgisInfoOfGrid arcgisInfoOfGrid);

	/**
	 * 2017-03-28 sulch add 修改网格轮廓数据信息
	 * 
	 * @param params
	 * @return
	 */
	public void updateArcgisDataOfGridMap(Map<String, Object> params);

	/**
	 * 2017-11-13 sulch add 修改网格轮廓数据信息
	 * 
	 * @param params
	 * @return
	 */
	public void updateArcgisDataOfGridSDO(Map<String, Object> params);

	/**
	 * 2014-06-05 liushi add 修改网格轮廓数据信息
	 * 
	 * @param arcgisInfoOfGrid
	 * @return
	 */
	public int updateArcgisDataOfGridSDOMap(ArcgisInfoOfGrid arcgisInfoOfGrid);

	/**
	 * 2014-10-09 huangmw add 修改片区网格轮廓数据信息
	 * 
	 * @param arcgisInfoOfSegmentGrid
	 * @return
	 */
	public int updateArcgisDataOfSegmentGrid(ArcgisInfoOfSegmentGrid arcgisInfoOfSegmentGrid);

	public int insertArcgisDataOfSegmentGrid(ArcgisInfoOfSegmentGrid arcgisInfoOfSegmentGrid);

	public int deleteArcgisDataOfSegmentGrid(ArcgisInfoOfSegmentGrid arcgisInfoOfSegmentGrid);

	public List<ArcgisInfoOfSegmentGrid> getArcgisDataOfSegmentGrid(@Param(value = "segmentGridId") Long segmentGridId,
			@Param(value = "parentGridId") Long parentGridId, @Param(value = "mapt") Integer mapt);

	/**
	 * 2014-06-05 liushi add 新增插入网格轮廓数据信息
	 * 
	 * @param arcgisInfoOfGrid
	 * @return
	 */
	public int insertArcgisDataOfGrid(ArcgisInfoOfGrid arcgisInfoOfGrid);

	public int deleteArcgisDataOfGrid(ArcgisInfoOfGrid arcgisInfoOfGrid);

	public List<ArcgisInfo> getArcgisDataOfBuildForIsExist(@Param(value = "wid") Long wid,
			@Param(value = "mapt") Integer mapt);

	public int updateArcgisDataOfBuild(ArcgisInfoOfBuild arcgisInfoOfBuild);

	public int insertArcgisDataOfBuild(ArcgisInfoOfBuild arcgisInfoOfBuild);

	public int deleteArcgisDataOfBuild(ArcgisInfoOfBuild arcgisInfoOfBuild);

	/**
	 * 2014-06-09 liushi add
	 * 
	 * @param gridId
	 * @return
	 */
	public Long getParentGridId(@Param(value = "gridId") Long gridId);

	/**
	 * 2014-06-09 liushi add 画地图轮廓的时候需要同时显示周边的网格以用作对比 获取其父节点以及父节点下面子节点的数据
	 * 
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDrawDataOfGrids(@Param(value = "parentGridId") Long parentGridId,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 2014-11-04 liushi add 根据gridId和mapt查询本身轮廓数据
	 * 
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDrawDataOfSelfGrids(@Param(value = "gridId") Long gridId,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 2014-11-04 liushi add 根据gridId和mapt查询父节点轮廓数据
	 * 
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDrawDataOfParentGrids(@Param(value = "gridId") Long gridId,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 2014-11-04 liushi add 根据gridId和mapt查询本级（自己除外）轮廓数据
	 * 
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDrawDataOfSameLevelGrids(@Param(value = "gridId") Long gridId,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 2014-11-04 liushi add 根据gridId和mapt查询下级轮廓数据
	 * 
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDrawDataOfLowerGrids(@Param(value = "gridId") Long gridId,
			@Param(value = "mapt") Integer mapt);

	public List<ArcgisInfoOfBuild> getArcgisDrawDataOfBuilds(@Param(value = "gridId") Long gridId,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 2015-07-08 liushi add 根据网格id获取其下属的所有楼宇id
	 * 
	 * @param gridId
	 * @return
	 */
	public List<ArcgisInfoOfBuild> getArcgisDrawDataIdsOfBuilds(@Param(value = "gridId") Long gridId);

	public List<ArcgisInfoOfBuild> getArcgisDataOfBuildsPoints(@Param(value = "gridId") Long gridId,
			@Param(value = "mapt") Integer mapt);

	public List<ArcgisInfoOfBuild> getDataOfBuildsByBuildingId(@Param(value = "buildingId") Long buildingId,
			@Param(value = "mapt") Integer mapt);

	public List<ArcgisInfoOfBuild> getArcgisDrawDataOfBuildsByGridId(@Param(value = "gridId") Long gridId,
			@Param(value = "mapt") Integer mapt);

	public List<ArcgisInfoOfGrid> getArcgisDataOfGrids(@Param(value = "gridCode") String gridCode,
			@Param(value = "gridLevel") Long gridLevel, @Param(value = "mapt") Integer mapt);

	public List<ArcgisInfoOfGrid> getArcgisDataIdsOfGrids(@Param(value = "gridId") Long gridId,
			@Param(value = "gridLevel") Long gridLevel);

	public List<ArcgisInfoOfGrid> getArcgisDataOfGridsByGridId(@Param(value = "gridId") Long gridId,
			@Param(value = "gridLevel") Long gridLevel, @Param(value = "mapt") Integer mapt);

	public List<ArcgisInfoOfGrid> getArcgisDataOfGridsListByIds(@Param(value = "ids") String ids,
			@Param(value = "mapt") Integer mapt);

	public List<ArcgisInfoOfGrid> getArcgisDataOfGridsByLevels(@Param(value = "gridCode") String gridCode,
			@Param(value = "gridLevel") String gridLevels, @Param(value = "mapt") Integer mapt);

	public List<ArcgisInfoOfHlhx> getArcgisDataOfHlhxListByIds(@Param(value = "ids") String ids,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 2014-06-13 liushi add 获取单条的网格轮廓数据
	 * 
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDataOfGrid(@Param(value = "gridId") Long gridId,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 2014-06-13 liushi add 获取单挑的楼宇轮廓数据
	 * 
	 * @param buildingId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfBuild> getArcgisDataOfBuild(@Param(value = "buildingId") Long buildingId,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 2014-06-26 liushi 获取市政设施的相关定位信息
	 * 
	 * @param resourcesId
	 * @param markerType
	 * @param catalog
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDataOfMarkerType(@Param(value = "resourcesId") Long resourcesId,
			@Param(value = "markerType") String markerType, @Param(value = "catalog") String catalog,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 2014-07-17 liushi add 根据市政设施的id获取所在网格的地图定位信息
	 * 
	 * @param resourcesId
	 * @param markerType
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDataOfGridByResId(@Param(value = "resourcesId") Long resourcesId,
			@Param(value = "markerType") String markerType, @Param(value = "mapt") Integer mapt);

	/**
	 * 2014-06-26 liushi 插入市政设施定位数据
	 * 
	 * @param arcgisInfoOfPublic
	 * @return
	 */
	public int insertArcgisDataOfMarkerType(ArcgisInfoOfPublic arcgisInfoOfPublic);

	/**
	 * 2014-06-26 liushi 修改市政设施定位
	 * 
	 * @param arcgisInfoOfPublic
	 * @return
	 */
	public int updateArcgisDataOfMarkerType(ArcgisInfoOfPublic arcgisInfoOfPublic);

	/**
	 * 2014-08-06 liushi add 根据网格code获取适配的网格配置信息
	 * 
	 * @param gridCode
	 * @return
	 */
	public List<ArcgisConfigInfo> findArcgisConfigInfoByGridCode(@Param(value = "gridCode") String gridCode, @Param(value = "engineName") String engineName);

	/**
	 * 2014-08-06 liushi add 根据网格地图信息配置id获取配置的地图服务信息
	 * 
	 * @param arcgisConfigInfoId
	 * @return
	 */
	public List<ArcgisServiceInfo> findArcgisServiceInfoByConfigId(
			@Param(value = "arcgisConfigInfoId") Integer arcgisConfigInfoId);

	/**
	 * 2014-08-06 liushi add 根据网格地图信息配置id获取配置的地图标尺信息
	 * 
	 * @param arcgisConfigInfoId
	 * @return
	 */
	public List<ArcgisScalenInfo> findArcgisScalenInfoByConfigId(
			@Param(value = "arcgisConfigInfoId") Integer arcgisConfigInfoId);

	/**
	 * 2014-08-11 liushi add 获取网格员轨迹
	 * 
	 * @param imsi
	 * @param locateTimeBegin
	 * @param locateTimeEnd
	 * @param mobileTelephone
	 * @param userId
	 * @return
	 */
	List<ArcgisInfoOfTrajectory> getGridAdminTrajectoryList(@Param(value = "imsi") String imsi,
			@Param(value = "locateTimeBegin") String locateTimeBegin,
			@Param(value = "locateTimeEnd") String locateTimeEnd,
			@Param(value = "mobileTelephone") String mobileTelephone, @Param(value = "userId") Long userId,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 2014-08-11 sulch add 获取网格员轨迹
	 * 
	 * @param userName
	 * @param locateTimeBegin
	 * @param locateTimeEnd
	 * @param mobileTelephone
	 * @param userId
	 * @return
	 */
	List<ArcgisInfoOfTrajectory> getGridAdminTrajectoryListByUserName(@Param(value = "userName") String userName,
			@Param(value = "locateTimeBegin") String locateTimeBegin,
			@Param(value = "locateTimeEnd") String locateTimeEnd,
			@Param(value = "mobileTelephone") String mobileTelephone, @Param(value = "userId") Long userId,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 队员轨迹数据查询
	 * 
	 * @param imsi
	 * @param locateTimeBegin
	 * @param locateTimeEnd
	 * @param userId
	 * @param mapt
	 * @return
	 */
	List<ArcgisInfoOfTrajectory> getTeamMemberTrajectoryList(@Param(value = "memberId") String memberId,
			@Param(value = "imsi") String imsi, @Param(value = "locateTimeBegin") String locateTimeBegin,
			@Param(value = "locateTimeEnd") String locateTimeEnd, @Param(value = "userId") Long userId,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 渔船设备轨迹
	 * 
	 * @param imsi
	 * @param locateTimeBegin
	 * @param locateTimeEnd
	 * @param mobileTelephone
	 * @param userId
	 * @return
	 */
	List<LocateInfo> getTrajectoryList(@Param(value = "locateTimeBegin") String locateTimeBegin,
			@Param(value = "locateTimeEnd") String locateTimeEnd, @Param(value = "appDeviceId") Long appDeviceId);

	/**
	 * 2015-03-06 huangmw add 护路护线队员轨迹数据查询
	 * 
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	List<ArcgisInfoOfTrajectory> getCareRoadMemberTrajectoryList(@Param(value = "memberId") String memberId,
			@Param(value = "imsi") String imsi, @Param(value = "locateTimeBegin") String locateTimeBegin,
			@Param(value = "locateTimeEnd") String locateTimeEnd,
			@Param(value = "mobileTelephone") String mobileTelephone, @Param(value = "userId") Long userId,
			@Param(value = "mapt") Integer mapt, @Param(value = "bizType") String bizType);

	/**
	 * 2015-01-22 liushi add 资源轮廓数据修改
	 * 
	 * @param arcgisInfoOfPublic
	 * @return
	 */
	public int updateArcgisDataOfResources(ArcgisInfoOfPublic arcgisInfoOfPublic);

	/**
	 * 2015-01-22 liushi add 资源轮廓数据新增
	 * 
	 * @param arcgisInfoOfPublic
	 * @return
	 */
	public int insertArcgisDataOfResources(ArcgisInfoOfPublic arcgisInfoOfPublic);

	/**
	 * 2015-01-22 liushi add 资源轮廓数据逻辑删除
	 * 
	 * @param arcgisInfoOfPublic
	 * @return
	 */
	public int deleteArcgisDataOfResources(ArcgisInfoOfPublic arcgisInfoOfPublic);

	/**
	 * 2015-02-10 liushi add 根据网格id和mapts查询网格数据
	 * 
	 * @param mapts
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridForOuter(@Param(value = "mapts") String mapts,
			@Param(value = "gridId") Long gridId);

	/**
	 * 2015-08-28 liushi add 获取网格的轮廓数据（单个的网格）
	 * 
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public ArcgisInfoOfGrid getArcgisGridLocateData(@Param(value = "gridId") Long gridId,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 2017-07-11 sulch add 向上钻取父级网格轮廓数据
	 * 
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDataOfParentgrids(@Param(value = "gridId") Long gridId,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 根据参数获取轨迹信息
	 * 
	 * @param params
	 * @return
	 */
	List<ArcgisInfoOfTrajectory> getTrajectoryListByParams(Map<String, Object> params);

	/**
	 * 获取重點人群热力图定位数据
	 * 
	 * @param params
	 * @return
	 */
	public List<ArcgisHeatMapInfo> getSpectialPopulationHeatMapStat(Map<String, Object> params);

	/**
	 * 获取人口热力图定位数据
	 * 
	 * @param params
	 * @return
	 */
	public List<ArcgisHeatMapInfo> getPopulationHeatMapStat(Map<String, Object> params);

	/**
	 * 2017-06-26 sulch add 获取父节点下面子节点的数据
	 * 
	 * @param parentGridId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDataOfChildrenGridsByParentId(
			@Param(value = "parentGridId") Long parentGridId, @Param(value = "mapt") Integer mapt);
	
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridsById(@Param(value = "gridId") Long gridId, @Param(value = "mapt") Integer mapt);

	/**
	 * 根据参数获取历史轨迹信息
	 * 
	 * @param params
	 * @return
	 */
	List<ArcgisInfoOfDayTraj> getDayTrajListByParams(Map<String, Object> params);
	/**
	 * 根据参数获取没有分割的原始历史轨迹信息
	 * @param params
	 * @return
	 */
	List<ArcgisInfoOfDayTraj> getDayTrajListForOri(Map<String, Object> params);

	/**
	 * 根据用户id获取当天轨迹信息
	 * 
	 * @param params
	 * @return
	 */
	List<ArcgisInfoOfTrajectory> getTrajectoryListByUserId(Map<String, Object> params);

	/**
	 * 根据用户名获取当天轨迹信息
	 * 
	 * @param params
	 * @return
	 */
	List<ArcgisInfoOfTrajectory> getTrajectoryListByUserName(Map<String, Object> params);

	/**
	 * 根据参数获取用户最后定位信息
	 * 
	 * @param params
	 *            mapt 地图类型：新地图二维5，新地图三维30 orgId 组织id userName 用户名
	 * @return
	 */
	List<ArcgisInfoOfUser> getUserLastLocateByParams(Map<String, Object> params);

	/**
	 * 根据楼宇ids获取楼宇的轮廓信息
	 * 
	 * @param buildingIds
	 * @param mapt
	 *            地图类型：新地图二维5，新地图三维30
	 * @return
	 */
	public List<ArcgisInfoOfBuild> getArcgisDatasOfBuilds(@Param(value = "buildingIds") String buildingIds,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 更新楼宇地址经纬度
	 * 
	 * @param arcgisInfoOfBuild
	 * @return
	 */
	public int updateBuildAddressXY(ArcgisInfoOfBuild arcgisInfoOfBuild);
	
	
	public List<ArcgisInfoOfPoint> getArcgisDataOfPoint(@Param(value = "wid") Long wid,
			@Param(value = "mapt") Integer mapt,@Param(value = "type") String type);
	
	public List<ArcgisInfoOfPoint> getArcgisDrawDataOfPointsByGridId(@Param(value = "infoOrgCode") String infoOrgCode,
			@Param(value = "mapt") Integer mapt);
	
	public List<ArcgisInfo> IsExistArcgisData(@Param(value = "wid") Long wid,
			@Param(value = "mapt") Integer mapt,@Param(value = "type") String type);
	
	public int updateArcgisDataInType(ArcgisInfoOfPoint arcgisInfoOfPoint);
	
	public int insertArcgisDataInType(ArcgisInfoOfPoint arcgisInfoOfPoint);
	
	public int deleteArcgisDataInType(ArcgisInfoOfPoint arcgisInfoOfPoint);
	
	public List getEventChildData(Map<String,Object> params);
	
	public int findAreaGridAdminCount(Map<String, Object> params);
	
	public List<Map<String,Object>> findAreaGridAdminList(Map<String, Object> params, RowBounds rowBounds);
	
	public List<ArcgisInfo> getArcgisCommonHs(@Param(value = "wid") Long wid,@Param(value = "type") String type,@Param(value = "mapt") Integer mapt);

	Long checkUsersHasTrajectoryByLocate(CheckUsersHasTrajectoryParam item);

	Long checkUsersHasTrajectoryByStat(Map<String, Object> item);
}
