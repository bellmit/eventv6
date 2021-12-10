package cn.ffcs.zhsq.map.arcgis.service;


import java.util.List;
import java.util.Map;

import cn.ffcs.common.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.*;

/**
 * 2014-05-16 liushi add 
 * arcgis相关信息获取的接口
 * @author liushi
 * //@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
 */
public interface IArcgisInfoService {
	// 地图标注使用的地图维度（二维、三维）
	public static final String MAP_TYPE_CODE = "MAP_TYPE_CODE";
	// 新地图
	public static final String NEW_MAP_ENGINE = "005";
	// 旧地图
	public static final String OLD_MAP_ENGINE = "004";
	// 二维
	public static final String TWO_DIMENSION = "2";
	// 三维
	public static final String THREE_DIMENSION = "3";
	// 新地图二维mapt
	public static final String TWO_DIMENSION_MAPT_OF_NEWMAP = "5";
	// 新地图三维mapt
	public static final String THREE_DIMENSION_MAPT_OF_NEWMAP = "30";
	// 旧地图二维mapt
	public static final String TWO_DIMENSION_MAPT_OF_OLDMAP = "2";
	// 旧地图三维mapt
	public static final String THREE_DIMENSION_MAPT_OF_OLDMAP = "20";
	
	/**
	 * 2014-05-22 liushi add
	 * 根据infoOrgCode获取地图配置信息
	 * @param infoOrgCode
	 * @param engineType
	 * @return
	 */
	public ArcgisMapConfigInfo findGisMapConfigInfoByOrgCode(String infoOrgCode, String engineType);
	
	/**
	 * 2014-06-04 liushi add
	 * 保存网格轮廓信息
	 * 1、若数据库中已经存在该条数据那么进行修改操作
	 * 2、若数据库中不存在该条数据那么进行插入操作
	 * @param arcgisInfo
	 * @return
	 */
	public boolean saveArcgisDrawAreaOfGrid(ArcgisInfoOfGrid arcgisInfoOfGrid);
	public boolean deleteArcgisDrawAreaOfGrid(ArcgisInfoOfGrid arcgisInfoOfGrid);
	/**
	 * 2014-06-13 liushi add
	 * 保存楼宇轮廓数据
	 * 1、若数据库已经存在该条数据则进行修改操作
	 * 2、若数据库不存在该条数据则进行插入操作
	 * @param arcgisInfoOfBuild
	 * @return
	 */
	public boolean saveArcgisDrawAreaOfBuild(ArcgisInfoOfBuild arcgisInfoOfBuild);
	public boolean updateBuildAddressXY(ArcgisInfoOfBuild arcgisInfoOfBuild);
	public boolean deleteArcgisDrawAreaOfBuild(ArcgisInfoOfBuild arcgisInfoOfBuild);
	
	/**
	 * 保存片区网格轮廓数据
	 * 1、若数据库已经存在该条数据则进行修改操作
	 * 2、若数据库不存在该条数据则进行插入操作
	 * @param arcgisInfoOfSegmentGrid
	 * @return
	 */
	public boolean saveArcgisDrawAreaOfSegmentGrid(ArcgisInfoOfSegmentGrid arcgisInfoOfSegmentGrid);
	public boolean deleteArcgisDrawAreaOfSegmentGrid(ArcgisInfoOfSegmentGrid arcgisInfoOfSegmentGrid);
	public List<ArcgisInfoOfSegmentGrid> getArcgisDataOfSegmentGrid(Long segmentGridId, Long parentGridId, Integer mapt);
	
	/**
	 * 2014-06-09 liushi add 画地图轮廓的时候需要同时显示周边的网格以用作对比
	 * 获取其父节点以及父节点下面子节点的数据
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDrawDataOfGrids(Long gridId, Integer mapt);
	
	/**
	 * 2015-02-10 liushi add 根据传递的gridId 和mapt（非正常的mapt ）
	 * @param gridId
	 * @param mapt 2 二维地图 ，3 三维地图  ，null传回两条当前玩个使用的所有地图类型数据
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridForOuter(Long gridId, String mapt);
	/**
	 * 2014-11-03 liushi add 画地图轮廓的时候需要同时显示周边的网格以用作对比
	 * 根据showType分别查询父级、自身、本级、下级的网格轮廓数据
	 * showType：0 代表自身轮廓数据查询  1代表父级轮廓数据查询  2代表本级（自己除外）轮廓数据查询 3、代表下级轮廓数据查询
	 * @param gridId
	 * @param mapt
	 * @param showType
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDrawDataOfGridsByLevel(Long gridId, Integer mapt,String showType);
	
	/**
	 * 2014-06-10 zhanh add 画地图轮廓的时候需要同时显示周边的网格以用作对比
	 * 获取其父节点以及父节点下面子节点的数据
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfBuild> getArcgisDataOfBuilds(Long gridId, Integer mapt);
	
	/**
	 * 2015-07-08 liushi add
	 * 根据网格id获取其下属的所有楼宇id
	 * @param gridId
	 * @return
	 */
	public String getArcgisDataIdsOfBuilds(Long gridId);
	
	public List<ArcgisInfoOfBuild> getArcgisDataOfBuildsPoints(Long gridId, Integer mapt);
	public List<ArcgisInfoOfBuild> getArcgisDrawDataOfBuildsByBuildingId(Long buildingId, Integer mapt);
	public List<ArcgisInfoOfBuild> getArcgisDrawDataOfBuildsByGridId(Long gridId, Integer mapt);
	public List<ArcgisInfoOfPoint> getArcgisDrawDataOfPointsByCode(String infoOrgCode, Integer mapt);
	public List<ArcgisInfoOfGrid> getArcgisDataOfGrids(String gridCode,Long gridLevel, Integer mapt);
	public List<ArcgisInfoOfGrid> getArcgisDataOfGrids(Long gridId,Long gridLevel, Integer mapt);
	/**
	 * 2015-07-08 liushi add
	 * 根据网格id和网格层级查询符合条件的gridId集合
	 * @param gridId
	 * @param gridLevel
	 * @return
	 */
	public String getArcgisDataIdsOfGrids(Long gridId,Long gridLevel);
	
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridsListByIds(String ids, Integer mapt);
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridsByLevels(String gridCode,String gridLevels, Integer mapt);
	
	public List<ArcgisInfoOfHlhx> getArcgisDataOfHlhxListByIds(String ids, Integer mapt);
	/**
	 * 2014-06-13 liushi add
	 * 获取单条的网格轮廓数据
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDataOfGrid(Long gridId, Integer mapt);

	/**
	 * 2014-06-13 liushi add
	 * 获取单挑的楼宇轮廓数据
	 * @param buildingId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfBuild> getArcgisDataOfBuild(Long buildingId, Integer mapt);
	
	/**
	 * 2014-06-26 liushi 
	 * 获取市政设施的相关定位信息
	 * @param resourcesId
	 * @param markerType
	 * @param catalog
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDataOfMarkerType(Long resourcesId, String markerType, String catalog, Integer mapt);
	
	/**
	 * 2014-07-17 liushi
	 * 根据市政设施的id获取所在网格的地图定位信息
	 * @param resourcesId
	 * @param markerType
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDataOfGridByResId(Long resourcesId, String markerType, Integer mapt);
	
	/**
	 * 2014-06-26 liushi 
	 * 保存市政设施的相关定位信息
	 * 先进行验证看表中是否有同一条记录，若有则进行修改操作，若没有则进行插入操作
	 * @param arcgisInfoOfPublic
	 * @return
	 */
	public boolean saveArcgisDataOfMarkerType(ArcgisInfoOfPublic arcgisInfoOfPublic);
	
	/**
	 * 2014-08-06 liushi
	 * 获取arcgis地图配置信息
	 * 1、获取地图配置信息
	 * 2、获取对应的地图服务信息
	 * 3、获取对应的地图标尺信息
	 * @param gridCode
	 * @return
	 */
	public List<ArcgisConfigInfo> findArcgisConfigInfoByGridCode(String gridCode, String engineName);
	
	/**
	 * 2014-08-11 liushi add
	 * 获取网格员轨迹
	 * @param imsi
	 * @param locateTimeBegin
	 * @param locateTimeEnd
	 * @param mobileTelephone
	 * @param userId
	 * @return
	 */
	List<ArcgisInfoOfTrajectory> getGridAdminTrajectoryList(String imsi,String locateTimeBegin,String locateTimeEnd,String mobileTelephone,Long userId,Integer mapt);

	/**
	 * 2014-08-11 sulch add
	 * 获取网格员轨迹
	 * @param userName
	 * @param locateTimeBegin
	 * @param locateTimeEnd
	 * @param mobileTelephone
	 * @param userId
	 * @return
	 */
	List<ArcgisInfoOfTrajectory> getGridAdminTrajectoryListByUserName(String userName,String locateTimeBegin,String locateTimeEnd,String mobileTelephone,Long userId,Integer mapt);

	/**
	 * 2015-03-06 huangmw add
	 * 护路护线队员轨迹数据查询
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	List<ArcgisInfoOfTrajectory> getCareRoadMemberTrajectoryList(String memberId, String imsi,String locateTimeBegin,String locateTimeEnd,String mobileTelephone,Long userId,Integer mapt,String bizType);
	
	/**
	 * 2015-01-22 liushi add 保存（含有新增跟修改的操作）资源轮廓数据
	 * @param arcgisInfoOfPublic
	 * @return
	 */
	public boolean saveArcgisDrawAreaOfResources(ArcgisInfoOfPublic arcgisInfoOfPublic);
	
	/**
	 * 2015-01-22 liushi add 逻辑删除资源轮廓数据
	 * @param arcgisInfoOfPublic
	 * @return
	 */
	public boolean deleteArcgisDrawAreaOfResources(ArcgisInfoOfPublic arcgisInfoOfPublic);
	
	/**
	 * 2016-01-05 liushi add
	 * 获取所有的外援楼宇轮廓数据
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDataOfBuildBinding(Long id);
	
	public int updateArcgisDataOfBuildBinding(Long id,String ids,String names);
	
	/**
	 * <p>Description:根据地图引擎和模块类型获取模块地图配置的是几维地图（二维还是三维）</p>
	 * @param mapEngineType 地图引擎类型
	 * @param module 模块类型代码
	 * @param infoOrgCode 信息域编码
	 * @return
	 * add sulch
	 * 2016年1月25日 上午11:08:38
	 */
	public String getMaptByMapEngineTypeAndModule(String mapEngineType, String module, String infoOrgCode);
	
	/**
	 * <p>Description:获取该模块的地图标注功能使用的是哪种地图类型</p>
	 * @param modularCode
	 * @param infoOrgCode
	 * @return
	 * add sulch
	 * 2016年1月25日 下午2:26:00
	 */
	public String getMapTypeByModule(String modularCode, String infoOrgCode);

	/**
	 * 获取业务IDs
	 * @param infoOrgCode
	 * @param type
	 * @return
	 */
	public String getBizIds(String infoOrgCode, String type);

	/**
	 * 队员轨迹数据查询
	 * @param memberId
	 * @param imsi
	 * @param locateTimeBegin
	 * @param locateTimeEnd
	 * @param userId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfTrajectory> getTeamMemberTrajectoryList(String memberId, String imsi, String locateTimeBegin, String locateTimeEnd, Long userId, Integer mapt);

	/**
	 * 根据登陆账号获取所属组织所挂的网格轮廓数据
	 * @param regisValue	登陆账号
	 * @param mapType	地图类型：2-》二维，3-》三维，null-》为空时默认查询所有的
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridsListByRegisValue(String regisValue, Integer mapType);

	/**
	 * 根据组织code获取网格轮廓数据
	 * @param orgCode	组织code
	 * @param mapType	地图类型：2-》二维，3-》三维，null-》为空时默认查询所有的
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridsListByOrgCode(String orgCode, Integer mapType);

	/**
	 * 2017-07-11 sulch add
	 * 向上钻取最接近且有网格轮廓数据的父级网格轮廓数据
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public ArcgisInfoOfGrid getArcgisDataOfLastParentgrid(Long gridId, Integer mapt);

	/**
	 * 根据参数获取轨迹信息
	 * @param params
	 * imsi imsi号
	 * userId 用户ID
	 * userName 用户名
	 * locateTimeBegin 轨迹开始时间
	 * locateTimeEnd 	轨迹结束时间
	 * @return
	 */
	List<ArcgisInfoOfTrajectory> getTrajectoryListByParams(Map<String, Object> params);

	/**
	 * 根据参数获取轨迹信息
	 * @param params
	 * imsi imsi号
	 * userId 用户ID
	 * userName 用户名
	 * locateTimeBegin 轨迹开始时间
	 * locateTimeEnd 	轨迹结束时间
	 * @return
	 */
	List<ArcgisInfoOfTrajectory> getDayTrajectoryListByParams(Map<String, Object> params);

	/**
	 * sulch add
	 * 获取父节点下面子节点的数据
	 * @param parentGridId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfGrid> getArcgisDataOfChildrenGridsByParentId(Long parentGridId, Integer mapt);

	/**
	 * 根据参数获取用户最后定位信息
	 * @param params
	 * mapt 地图类型：新地图二维5，新地图三维30
	 * userName 用户名
	 * orgId 组织id
	 * locateTimeBegin 开始时间
	 * locateTimeEnd 	结束时间
	 * @return
	 */
	List<ArcgisInfoOfUser> getUserLastLocateByParams(Map<String, Object> params);

	/**
	 * 根据楼宇ids获取楼宇的轮廓信息
	 * @param buildingIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfBuild> getArcgisDatasOfBuilds(String buildingIds, Integer mapt);
	
	/**
	 * 获取其父节点以及父节点下面子节点的数据
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPoint> getArcgisDataOfPoint(Long wid, Integer mapt);

	public boolean saveArcgisDrawPoint(ArcgisInfoOfPoint arcgisInfoOfPoint);
	
	public boolean deleteArcgisDrawAreaOfPoint(ArcgisInfoOfPoint arcgisInfoOfPoint);
	/**
	 * GIS事件数据获取
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getEventChildData(Map<String,Object> params);
	
	/**
	 * 随机楼栋撒点（按网格）
	 * @return
	 */
	public boolean saveRandomBuild(List<Long> gridIds);
	
	/**
	 * 第三方保存网格轮廓
	 * @return
	 */
	public Map<String, Object> saveGridOutLine(Map<String, Object> params);
	/**
	 * 第三方获取网格轮廓
	 * @param params
	 * @return
	 */
	public Map<String, Object> apiGetGridOutLine(Map<String, Object> params);
	/**
	 * 获取地图手机屏幕范围内的网格员
	 * @param param
	 * infoOrgCode:信息域编码
	 * duty:网格员类型
	 * hs:经纬度范围(逗号隔开，例:111.1111111,22,22222,111.2222222,23.23232323)
	 * mapt:地图类型，一般传5
	 * @return
	 */
	public EUDGPagination findAreaGridAdmin(int pageNo, int pageSize,Map<String, Object> params);
	
	/**
	 * 获取通用的MAP_WG_GIS表轮廓
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisCommonHs(Long wid,String type,Integer mapt);

	public List<Map<String, Object>> checkUsersHasTrajectory(List<CheckUsersHasTrajectoryParam> param);
}
