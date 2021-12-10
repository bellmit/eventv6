package cn.ffcs.zhsq.mybatis.persistence.map.arcgis;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.map.arcgis.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.trajectory.LocateInfo;

/**
 * 2014-05-22 liushi add
 * 地图配置信息
 * @author Administrator
 *
 */
public interface ArcgisDataOfLocalMapper extends MyBatisBaseMapper<ArcgisMapConfigInfo>{
	/**
	 * 2015-04-9 huangmw add
	 * 根据userId 和mapt获取安全隐患定位信息
	 * @param ids buildingId集合
	 * @param mapt 地图类型
	 * @return 返回安全隐患地图定位信息数据
	 */
	public List<ArcgisInfoOfPublic> getArcgisRiskLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);
	
	/**
	 * 2014-06-13 liushi add
	 * 获取单挑的楼宇轮廓数据
	 * @param buildingId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPeople> getArcgisPopLocateDataListByUserIds(@Param(value="userIds")String userIds, @Param(value="mapt")Integer mapt, @Param(value="infoOrgCode")String infoOrgCode);
	
	/**
	 * 2016-04-14 qiudong add
	 * @param memberId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisPopLocateDataListByMemberId(@Param(value="memberId")String memberId, @Param(value="mapt")Integer mapt);
	
	/**
	 * 2015-08-25 liushi add 获取居民居住楼宇
	 * @param userId
	 * @return
	 */
	public List<Map<String,Object>> getBuildOfPopByUserId(@Param(value="userId")Long userId);
	
	/**
	 * 2019-03-04 CaiJP add
	 * 根据楼栋Id获取楼栋信息
	 * @param buildingId
	 * @return
	 */
	public List<Map<String,Object>> findBuildOfPopByBuildingId(@Param(value="buildingId")Long buildingId);
	
	/**
	 * 2015-02-25 huangmw add
	 * 根据id 和mapt获取消防安全管理员定位信息
	 * @param ids  ids1企业   ids2场所   ids3出租屋
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisSafetyPersonManageLocateDataListByIdsOne(@Param(value="ids1")String ids1, @Param(value="mapt")Integer mapt);
	public List<ArcgisInfoOfPublic> getArcgisSafetyPersonManageLocateDataListByIdsTwo(@Param(value="ids2")String ids2, @Param(value="mapt")Integer mapt);
	public List<ArcgisInfoOfPublic> getArcgisSafetyPersonManageLocateDataListByIdsThree(@Param(value="ids3")String ids3, @Param(value="mapt")Integer mapt);
	
	/**
	 * 2015-04-10 huangmw add
	 * 根据id 和mapt获取安全隐患定位信息
	 * @param ids  ids1企业   ids2场所   ids3出租屋
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisRiskLocateDataListByIdsOne(@Param(value="ids1")String ids1, @Param(value="mapt")Integer mapt);
	public List<ArcgisInfoOfPublic> getArcgisRiskLocateDataListByIdsTwo(@Param(value="ids2")String ids2, @Param(value="mapt")Integer mapt);
	public List<ArcgisInfoOfPublic> getArcgisRiskLocateDataListByIdsThree(@Param(value="ids3")String ids3, @Param(value="mapt")Integer mapt);
	
	/**
	 * 2017-11-24 tursh add
	 * 根据Ids 和mapt获取资源的定位信息
	 * @param resourceIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisPoliceLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt,@Param(value="markerType")String markerType);
	
	/**
	 * 2014-06-24 chenlf add
	 * 获取全球眼轮廓数据
	 * @param id
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisGlobalEyesLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt,@Param(value="orgCode")String orgCode);

	/**
	 * 获取福富全球眼轮廓数据
	 * @param deviceNums
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisGlobalEyesLocateDataListByDeviceNums(@Param(value="deviceNums")String deviceNums, @Param(value="mapt")Integer mapt);
	
	/**
	 * 根据typeCode 和mapt获取污染检测点、血库信息统计、通讯覆盖定位信息
	 * @param typeCode 污染检测点02070401、血库信息统计02070403、通讯覆盖02070402
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisGlobalEyesLocateDataListBypollution(@Param(value="typeCode")String typeCode, @Param(value="mapt")Integer mapt);
	
	public Map<String, Object> getGlobalListByDeviceNums(@Param(value="deviceNums")String deviceNums);


	/**
	 * 2015-01-30 huangmw add
	 * 根据id 和mapt获取水文监测点位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisSwjcLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);

	
	/**
	 * 2014-06-24 chenlf add
	 * 获取网格员轮廓数据
	 * @param id
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisGridAdminLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);

	/**
	 * 2017-08-30 sulch add
	 * 根据id 和mapt获取网格员定位信息(表关联通过用户名：江阴)
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisGridAdminLocateDataListByIdsUserName(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);

	/**
	 * 2015-02-6 huangmw add
	 * 根据id 和mapt获取护路护线队员的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisCareRoadMemberLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);

	/**
	 * 2014-07-01 chenlf add
	 * 获取房数据
	 * @param buildingIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisBuildingLocateDataListByIds(@Param(value="buildingIds")String buildingIds, @Param(value="mapt")Integer mapt);
	public List<ArcgisInfo> getBuildingLocateDataList(@Param(value="infoOrgCode")String infoOrgCode, @Param(value="mapt")Integer mapt);

	/**
	 * 2014-07-02 chenlf add
	 * 获取重点场所数据
	 * @param keyPlaceIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisKeyPlaceLocateDataListByIds(@Param(value="keyPlaceIds")String keyPlaceIds, @Param(value="mapt")Integer mapt);

	/**
	 * 2014-07-02 chenlf add
	 * 获取重点场所数据
	 * @param keyPlaceIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisKeyPlaceLocateDataListByIdsYD(@Param(value="keyPlaceIds")String keyPlaceIds, @Param(value="mapt")Integer mapt);

	public List<ArcgisInfoOfPublic> getKeyPlaceLocateDataListByIds(@Param(value="keyPlaceIds")String keyPlaceIds, @Param(value="mapt")Integer mapt,@Param(value="markerType")String markerType);
	
	/**
	 * 2014-06-26 zhongshm add
	 * 获取事件轮廓数据
	 * @param id
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisEventLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);
	
	/**
	 * 2014-08-21 chenlf add
	 * 根据id 和mapt获取事件调度模块事件信息（附带事件数据）
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisEventDisposalLocateDataListByIdsWithEvent(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);
	
	/**
	 * 2014-08-21 chenlf add
	 * 根据id 和mapt获取事件调度模块事件信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisEventDisposalLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);
	
	/**
	 * 2014-10-27 huangmw add
	 * 根据id 和mapt获取重点事件信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisImportantEventDisposalLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);
	
	
	/**
	 * 2014-07-04 huangmw add
	 * 获取企业数据
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisCorLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);
	public List<ArcgisInfoOfPublic> getCorLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt,@Param(value="markerType")String markerType);

	/**
	 * 环保企业
	 * @param ids
	 * @param mapt
	 * @param markerType
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getCorEnvLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt,@Param(value="markerType")String markerType);

	/**
	 * 2014-07-08 chenlf add
	 * 根据resourceIds 和mapt获取资源的定位信息
	 * @param resourceIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisResourceLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt,@Param(value="markerType")String markerType);

	/**
	 * 2017-06-23 sulch add
	 * 根据ids 和mapt获取资源的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getBuildLocateDataListByResIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);


	/**
	 * 2014-07-08 chenlf add
	 * 根据ids 和mapt获取出租屋的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisRentRoomLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);
	
	/**
	 * 2016-01-04 chenlf add
	 * 根据ids 和mapt获取出租屋的定位信息---海沧是通过手动标注不是关联楼宇
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisRentRoomLocateDataListByIdsHC(@Param(value="ids")String ids,@Param(value="markType")String markType ,@Param(value="mapt")Integer mapt);
	
	
	/**
	 * 2014-07-04 huangmw add
	 * 获取企业数据
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisPartyGroupLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt, @Param(value="markerType")String markerType);
	
	/**
	 * 2014-09-03 chenlf add
	 * 根据id 和mapt获取巡逻队、警务室、治保组织等社会组织信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisSocietyOrgLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt, @Param(value="markerType")String markerType);

	/**
	 * 2017-12-29 chenlf add
	 * 根据id 和mapt获取宜居罗坊定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisLivableLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt, @Param(value="markerType")String markerType);


	/**
	 * 寄递物流
	 * @param ids
	 * @param mapt
	 * @param markerType
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisLogisticsSafetyLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);
	
	/**
	 * 2014-09-03 chenlf add
	 * 根据id 和mapt获取案件警情信息
	 * @param ids
	 * @param mapt 
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisCasesLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);

	public List<ArcgisInfoOfPublic> getArcgisDisputeLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);
	
	/**
	 * 2014-09-03 chenlf add
	 * 根据id 和mapt获取隐患信息
	 * @param ids
	 * @param mapt 
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDangousLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);

	/**
	 * zhongshm
	 * 根据id 和mapt获取园林绿化信息
	 * @param ids
	 * @param type
	 * @param mapt 
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisLandscapeLocateDataListByIds(@Param(value="ids")String ids,@Param(value="type")String type ,@Param(value="mapt")Integer mapt);
	
	/**
	 * 2014-09-03 chenlf add
	 * 根据id 和mapt获取两新组织信息
	 * @param ids
	 * @param type 新经济组织 01B029
	 * @param mapt 
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisNewGroupLocateDataListByIds(@Param(value="ids")String ids,@Param(value="type")String type ,@Param(value="mapt")Integer mapt);
	
	/**
	 * 2014-12-19 huangmw add
	 * 根据id 和mapt获取新经济组织信息
	 * @param ids
	 * @param type 新社会组织 01B032
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisNewSocietyLocateDataListByIds(@Param(value="ids")String ids,@Param(value="type")String type ,@Param(value="mapt")Integer mapt);
	
	/**
	 * 2014-09-25 huangmw add
	 * 根据id 和mapt获取台江首页分类管理城市综合体信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisSegmentGridDataListByIds(@Param(value="ids")String ids,@Param(value="mapt")Integer mapt);
	
	/**
	 * 2014-10-31 chenlf add
	 * 根据id 和mapt获取防控片区信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisSegmentGridDataListByParentGridId(@Param(value="parentGridId")Long parentGridId,@Param(value="mapt")Integer mapt);
	
	/**
	 * 2015-01-20 liushi add 根据id查询本身的地图数据信息
	 * @param wid
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDataOfResourcesSelfById(@Param(value="wid")Long wid, @Param(value="mapt")Integer mapt,@Param(value="targetType")String targetType);
	
	/**
	 * 2015-01-20 liushi add 根据id查询资源对象本身同级的其他资源地图数据
	 * @param wid
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDataOfResourcesSamelevelById(@Param(value="wid")Long wid, @Param(value="mapt")Integer mapt,@Param(value="targetType")String targetType);
	
	/**
	 * 2015-01-20 liushi add 根据id查询资源对象父级节点（网格）地图数据
	 * @param wid
	 * @param mapt
	 * @param targetType
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDataOfResourcesParentById(@Param(value="wid")Long wid, @Param(value="mapt")Integer mapt,@Param(value="targetType")String targetType);
	
	/**
	 * 根据resourceIds 和mapt获取资源的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisNewResourceLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt, @Param(value="resTypeCode")String resTypeCode);
	
	/**
	 * 根据resourceIds 和mapt获取资源的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisCampusLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt, @Param(value="markType")String markType);

	public List<ArcgisInfoOfPublic> getArcgisFireTeamLocateDataListByIds(@Param(value = "ids") String ids, @Param(value = "mapt") Integer mapt, @Param(value = "markType") String markType);
	
	/**
	 * 晋江消防队（形式分类1.现役消防队2.义务消防队 3.志愿消防队）
	 * @param ids
	 * @param mapt
	 * @param catalog
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisFireTeamNewLocateDataListByIds(@Param(value = "ids") String ids, @Param(value = "mapt") Integer mapt, @Param(value = "markType") String markType);
	
	/**
	 * 2015-03-21 huangmw add
	 * 获取便民服务网点轮廓数据
	 * @param id
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDataOfServiceOutletsByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);
	
	/**
	 * 获取活动的轮廓数据
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisActivityLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt, @Param(value="markType")String markType);

	/**
	 * 获取重点单位的轮廓数据
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisImportUnitLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);

	/**
	 * 获取重点单位的轮廓数据
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisStoreLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);

	/**
	 * 根据resourceIds 和mapt获取安监队伍的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfHashId> getArcgisControlsafetyRanksLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt, @Param(value="markType")String markType);
	 
	/**
	 * 根据resourceIds 和mapt获取两车管理的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisParkingManageLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);

	/**
	 * 根据resourceIds 和mapt获取违法建设的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisCtIllegalLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);
	
	/**
	 * 2015-08-19 liushi add 九小场所地图标注信息获取
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisZzSmallPlaceLocateDataList(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);
	/**
	 * 2015-08-31 liushi add 根据模块编码查询颜色情况
	 * @param modularCode 模块编码
	 * @return
	 */
	public List<Map<String,Object>> getColorList(@Param(value="modularCode")String modularCode);
	
	/**
	 * 2016-01-05 liushi add 获取外援网格轮廓数据
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDataOfBuildBinding(@Param(value="id")Long id);
	public int updateArcgisDataOfBuildBinding(@Param(value="id")Long id,@Param(value="ids")String ids,@Param(value="names")String names);
	
	/**
	 * 根据resourcesIds、markerType 和mapt获取精准扶贫的定位信息
	 * @param resourcesIds 业务主键id拼接成的字符串
	 * @param mapt
	 * @param markerType 业务模块类型
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfPrecisionPovertyByIds(@Param(value="resourcesIds")String resourcesIds, @Param(value="mapt")Integer mapt, @Param(value="markerType")String markerType);

	/**
	 * 取市政道路标注点信息
	 * @param ids
	 * @param mapt
	 * @param markerType
	 * @return
	 */
	public List<ArcgisInfoOfPublic> findRoadMarkersByIds(@Param(value = "ids") String ids,
			@Param(value = "mapt") Integer mapt, @Param(value = "markerType") String markerType);

	/**
	 * 获取各视联网信息中标注点信息
	 * @param nicIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisNicLocateDataListByIds(@Param(value="nicIds")String nicIds, @Param(value="mapt")Integer mapt);


	/**
	 * 渔船定位
	 * @param ids
	 * @return
	 */
	public List<ArcgisInfo> getArcgisBoatLocateDataListByIds(@Param(value="ids") String ids);
	
	/**
	 * 获取海康设备标注点信息
	 * @param eqpIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisEqpLocateDataListByIds(@Param(value="eqpIds")String eqpIds, @Param(value="mapt")Integer mapt);
	/**
	 * 获取海康布控设备标注点信息
	 * @param eqpIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getIdssControlEqpLocateDataListByIds(@Param(value="eqpIds")String eqpIds, @Param(value="mapt")Integer mapt, @Param(value="controlTargetId")Long controlTargetId);
	
	public List<ArcgisInfoOfPublic> getArcgisEqpLocateDataList(Map<String,Object> params);

	public List<ArcgisInfo> getUrbanObjGisLocateDataList(@Param(value = "infoOrgCode") String infoOrgCode,
			@Param(value = "mapt") Integer mapt, @Param(value = "objCode") String objCode);

	public List<ArcgisInfo> getArcgisInfoList(@Param(value = "idStr") String idStr,
			@Param(value = "mapt") Integer mapt, @Param(value = "markerType") String markerType);
	
	public List<ArcgisInfo> getBizLocateInfoList(@Param(value = "bizType") String bizType);

	/**
	 * 2017-05-16 sulch add
	 * 获取信访责任人定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisPetitionLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);
	public List<ArcgisInfoOfPublic> getArcgisTwoMemberLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);

	List<ArcgisInfoOfPublic> getArcgisTwoStationLocateDataListByIds(@Param(value="ids")String ids,@Param(value="markType")String markType, @Param(value="mapt")Integer mapt);

	public List<ArcgisInfoOfPublic> getArcgisImportantUnitLocateDataListByIds(@Param(value="ids")String ids,@Param(value="markType")String markType, @Param(value="mapt")Integer mapt);

	public List<ArcgisHeatMapInfo> getTrafficAccidentHeatMapStat(@Param(value = "infoOrgCode") String infoOrgCode, @Param(value = "mapt") Integer mapt);
	
	
	/**
	 * 获取宣传阵地的地图点
	 * @param ids
	 * @param markType
	 * @param mapt
	 * @return
	 */
	List<ArcgisInfoOfPublic> getArcgisPropagandaPositionLocateDataListByIds(@Param(value="ids")String ids,@Param(value="markType")String markType, @Param(value="mapt")Integer mapt);

	/**
	 * 获取重点车辆定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisKeyVehiclesLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt);
	
	public List<ArcgisInfo> getArcgisPartyMemberDataList(@Param(value = "infoOrgCode") String infoOrgCode,
			@Param(value = "mapt") Integer mapt);
	
	
	/**
	 * 获取两站两员中隐患路段、隐患路口标注点信息
	 * @param nicIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDangerRoadLocateDataListByIds(@Param(value="drIds")String drIds, @Param(value="mapt")Integer mapt);


	/**
	 * 获取重点人员信息
	 * @param params x,y 查询的中心点 ; distance 查询距离（米）; markerType 资源类的查询还有识别码; mapType 地图类型
	 * @return
	 */
	public List<ArcgisInfoOfPublic> statOfZdPeopleZhouBianLocateData(Map<String, Object> params);

	public List<ArcgisInfo> getArcgisCorDataList(@Param(value = "infoOrgCode") String infoOrgCode,
			@Param(value = "mapt") Integer mapt);
	
	public List<ArcgisInfo> getNewPartyGroupXYList(@Param(value = "infoOrgCode") String infoOrgCode,
			@Param(value = "mapt") Integer mapt, @Param(value = "markerType") String markerType);
	
	public List<ArcgisInfo> getNewPartyGroupXYById(@Param(value = "ids") String ids,
			@Param(value = "mapt") Integer mapt, @Param(value = "markerType") String markerType);

	public List<ArcgisInfo> getArcgisGanSuDrugLocateDataListByUserIds(@Param(value="userIds")String userIds, @Param(value="mapt")Integer mapt);

	public List<ArcgisInfo> getArcgisRectifyDataList(@Param(value = "infoOrgCode") String infoOrgCode,
			@Param(value = "mapt") Integer mapt);

	public List<ArcgisInfo> getArcgisCampsDataList(@Param(value = "infoOrgCode") String infoOrgCode,
			@Param(value = "mapt") Integer mapt);

	public List<ArcgisInfo> getArcgisJiangxiPetitionDataList(@Param(value = "infoOrgCode") String infoOrgCode,
			@Param(value = "mapt") Integer mapt);

	/**
	 * 获取驰名商标定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisTrademarkLocateDataListByIds(@Param(value = "ids")String ids,
																		  @Param(value = "mapt")Integer mapt,
																		  @Param(value = "markType")String markType);

	/**
	 * 获取守重企业定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisEnterpriseLocateDataListByIds(@Param(value = "ids")String ids,
																		  @Param(value = "mapt")Integer mapt,
																		  @Param(value = "markType")String markType);

	/**
	 * 获取综合执法人员定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisLawEnforceOfficLocateDataListByIds(@Param(value = "ids")String ids, @Param(value = "mapt")Integer mapt, @Param(value = "socialOrgId")Integer socialOrgId);

	/**
	 * 根据信息域获取全球眼定位信息
	 * @param infoOrgCode
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisGlobalEyesDataList(@Param(value = "infoOrgCode") String infoOrgCode,
															 @Param(value = "mapt") Integer mapt);

	/**
	 * 获取法人定位信息
	 * @param infoOrgCode
	 * @param mapt
	 * @return
	 */
	public List<ArcgisBaseInfo> getArcgisLegalPersonLocateDataListByIds(@Param(value = "legalIds") String legalIds,
																@Param(value = "mapt") Integer mapt);
	
	/**
	 * 2017-11-24 wtl add
	 * 根据Ids 和mapt获取资源的定位信息
	 * @param resourceIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfPcManageByIds(@Param(value="resourcesIds")String resourcesIds, @Param(value="mapt")Integer mapt,@Param(value="markerType")String markerType);

	public List<ArcgisInfoOfPublic> getArcgisCorEnvNPLocateDataListByIds(@Param(value="ids")String ids, @Param(value="mapt")Integer mapt,@Param(value="markerType")String markerType);
	
}
