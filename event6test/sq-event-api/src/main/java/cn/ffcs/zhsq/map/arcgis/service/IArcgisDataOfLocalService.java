package cn.ffcs.zhsq.map.arcgis.service;


import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.map.arcgis.*;
import cn.ffcs.zhsq.mybatis.domain.trajectory.LocateInfo;

/**
 * 2014-05-16 liushi add 
 * arcgis人员定位信息获取接口
 * @author liushi
 * //@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
 */
public interface IArcgisDataOfLocalService {
	/**
	 * 2015-04-9 huangmw add
	 * 根据userId 和mapt获取安全隐患定位信息
	 * @param ids buildingId集合
	 * @param mapt 地图类型
	 * @return 返回安全隐患地图定位信息数据
	 */
	public List<ArcgisInfoOfPublic> getArcgisRiskLocateDataListByIds(String ids,Integer mapt);
	
	/**
	 * 2014-06-23 liushi add
	 * 根据userId 和mapt获取人员定位信息
	 * @param userIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPeople> getArcgisPopLocateDataListByUserIds(String userIds, Integer mapt, String infoOrgCode);
	
	/**
	 * 2016-04-14 qiudong add
	 * 根据memberId 和和mapt获取人员定位信息
	 */
	public List<ArcgisInfoOfPublic> getArcgisPopLocateDataListByMemberId(String memberId, Integer mapt);
	
	/**
	 * 2015-8-25 liushi add
	 * 获取居民居住楼宇
	 * @param userId
	 * @return
	 */
	public List<Map<String,Object>> getBuildOfPopByUserId(Long userId);
	/**
	 * 2015-02-25 huangmw add
	 * 根据id 和mapt获取消防安全管理员定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisSafetyPersonManageLocateDataListByIds(String ids,Integer mapt);
	
	/**
	 * 2014-06-24 chenlf add
	 * 根据id 和mapt获取全球眼定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisGlobalEyesLocateDataListByIds(String ids,Integer mapt,String orgCode);

	/**
	 * 2014-06-24 chenlf add
	 * 根据id 和mapt获取全球眼定位信息
	 * @param deviceNums
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisGlobalEyesLocateDataListByDeviceNums(String deviceNums,Integer mapt);
	
	/**
	 * 根据typeCode 和mapt获取污染检测点、血库信息统计、通讯覆盖定位信息
	 * @param typeCode 污染检测点02070401、血库信息统计02070403、通讯覆盖02070402
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisGlobalEyesLocateDataListBypollution(String typeCode,Integer mapt);
	
	
	/**
	 * 2015-01-30 huangmw add
	 * 根据id 和mapt获取水文监测点位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisSwjcLocateDataListByIds(String ids,Integer mapt);
	
	/**
	 * 2014-06-25 chenlf add
	 * 根据id 和mapt获取网格员定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisGridAdminLocateDataListByIds(String ids,Integer mapt);

	/**
	 * 2017-08-30 sulch add
	 * 根据id 和mapt获取网格员定位信息(表关联通过用户名：江阴)
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisGridAdminLocateDataListByIdsUserName(String ids,Integer mapt, boolean userNameFlag);


	/**
	 * 2015-02-6 huangmw add
	 * 根据id 和mapt获取护路护线队员的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisCareRoadMemberLocateDataListByIds(String ids,Integer mapt);

	/**
	 * 2014-07-1 chenlf add
	 * 根据buildingIds 和mapt获取房定位信息
	 * @param buildingIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisBuildingLocateDataListByIds(String buildingIds,Integer mapt);
	public List<ArcgisInfo> getBuildingLocateDataList(String infoOrgCode, Integer mapt);

	/**
	 * 2014-07-02 chenlf add
	 * 根据keyPlaceIds 和mapt获取重点场所定位信息
	 * @param keyPlaceIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisKeyPlaceLocateDataListByIds(String keyPlaceIds,Integer mapt);

	/**
	 * 2014-07-02 chenlf add
	 * 根据keyPlaceIds 和mapt获取重点场所定位信息
	 * @param keyPlaceIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisKeyPlaceLocateDataListByIdsYD(String keyPlaceIds,Integer mapt);

	public List<ArcgisInfoOfPublic> getKeyPlaceLocateDataListByIds(String keyPlaceIds,Integer mapt, String markerType);

	/**
	 * 2014-06-26 zhongshm add
	 * 根据id 和mapt获取事件信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisEventLocateDataListByIds(String ids, Integer mapt);
	
	/**
	 * 2014-10-27 huangmw add
	 * 根据id 和mapt获取事件信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisImportantEventDisposalLocateDataListByIds(String ids, Integer mapt);
	
	/**
	 * 2014-08-21 chenlf add
	 * 根据id 和mapt获取事件调度模块事件信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisEventDisposalLocateDataListByIds(String ids, Integer mapt);

	/**
	 * 2015-01-19 zhongshm
	 * 根据id 和mapt获取事件调度模块事件信息（带事件信息）
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisEventDisposalLocateDataListByIdsWithEvent(String ids, Integer mapt);
	
	/**
	 * 2014-07-04 huangmw add
	 * 根据id 和mapt获取企业信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisCorLocateDataListByIds(String ids, Integer mapt);
	public List<ArcgisInfoOfPublic> getArcgisCorLocateDataListByIds(String ids, Integer mapt, String markerType);

	/**
	 * 环保企业企业
	 * @param ids
	 * @param mapt
	 * @param markerType
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisCorEnvLocateDataListByIds(String ids, Integer mapt, String markerType);

	/**
	 * 2014-07-8 chenlf add
	 * 根据resourceIds 和mapt获取资源的定位信息
	 * @param resourceIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisResourceLocateDataListByIds(String resourceIds, Integer mapt,String resTypeCode);

	/**
	 * 2014-07-8 chenlf add
	 * 根据resourceIds 和mapt获取资源的定位信息
	 * @param resourceIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getBuildLocateDataListByResIds(String resourceIds, Integer mapt);

	/**
	 * 根据resourceIds 和mapt获取资源的定位信息
	 * @param resourceIds
	 * @param mapt
	 * @param resTypeCode
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisNewResourceLocateDataListByIds(String resourceIds, Integer mapt, String resTypeCode);
	
	/**
	 * 2014-07-9 chenlf add
	 * 根据ids 和mapt获取出租屋的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisRentRoomLocateDataListByIds(String ids, Integer mapt);
	
	/**
	 * 2016-01-04 sulch add
	 * 根据ids 和mapt获取出租屋的定位信息---海沧是通过手动标注不是关联楼宇
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisRentRoomLocateDataListByIdsHC(String ids, Integer mapt);
	
	
	/**
	 * 2014-07-09 huangmw add
	 * 根据id 和mapt获取党组织信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisPartyGroupLocateDataListByIds(String ids, Integer mapt,String markerType);
	
	

	/**
	 * 2014-09-03 chenlf add
	 * 根据id 和mapt获取巡逻队、警务室、治保组织等社会组织信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisSocietyOrgLocateDataListByIds(String ids, Integer mapt,String markerType);


	/**
	 * 2014-09-03 chenlf add
	 * 根据id 和mapt获取宜居罗坊定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisLivableLocateDataListByIds(String ids, Integer mapt,String markerType);






	/**
	 * 物流安监安全管理
	 * 
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisLogisticsSafetyLocateDataListByIds(String ids,Integer mapt);
	
	
	/**
	 * 2014-09-03 chenlf add
	 * 根据id 和mapt获取案件警情信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisCasesLocateDataListByIds(String ids, Integer mapt);

	public List<ArcgisInfoOfPublic> getArcgisDisputeLocateDataListByIds(String ids, Integer mapt);
	
	/**
	 * 2014-09-03 chenlf add
	 * 根据id 和mapt获取隐患信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDangousLocateDataListByIds(String ids, Integer mapt);
	
	/**
	 * 2014-09-20 chenlf add
	 * 根据id 和mapt获取两新组织信息信息
	 * @param ids
	 * @param mapt
	 * @param type 新经济组织 01B029 
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisNewGroupLocateDataListByIds(String ids,String type, Integer mapt);
	
	public List<ArcgisInfoOfPublic> getArcgisLandscapeLocateDataListByIds(String ids,String type, Integer mapt);
	
	/**
	 * 2014-12-19 huangmw add
	 * 根据id 和mapt获取新经济组织信息
	 * @param ids
	 * @param type 新社会组织 01B032
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisNewSocietyLocateDataListByIds(String ids,String type, Integer mapt);
	
	/**
	 * 2014-09-25 huangmw add
	 * 根据id 和mapt获取台江首页分类管理城市综合体信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisSegmentGridDataListByIds(String ids, Integer mapt);
	
	/**
	 * 2014-10-31 chenlf add
	 * 根据id 和mapt获取防控片区信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisSegmentGridDataListByParentGridId(Long parentGridId, Integer mapt);
	
	/**
	 * 2015-01-20 liushi add 根据id查询本身的地图数据信息
	 * @param wid
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDataOfResourcesSelfById(Long wid, Integer mapt,String targetType);
	
	/**
	 * 2015-01-20 liushi add 根据id查询资源对象本身同级的其他资源地图数据
	 * @param wid
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDataOfResourcesSamelevelById(Long wid, Integer mapt, String targetType);
	
	/**
	 * 2015-01-20 liushi add 根据id查询资源对象父级节点（网格）地图数据
	 * @param wid
	 * @param mapt
	 * @param targetType
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDataOfResourcesParentById(Long wid, Integer mapt, String targetType);
	
	/**
	 * 根据resourceIds 和mapt获取校园周边的定位信息
	 * @param resourceIds
	 * @param mapt
	 * @param resTypeCode
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisCampusLocateDataListByIds(String resourceIds, Integer mapt, String resTypeCode);
	
	public List<ArcgisInfoOfPublic> getArcgisFireTeamLocateDataListByIds(String ids, Integer mapt, String markType);
	
	/**
	 * 晋江消防队（形式分类1.现役消防队2.义务消防队 3.志愿消防队）
	 * 
	 * @param ids
	 * @param mapt
	 * @param catalog
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisFireTeamNewLocateDataListByIds(String ids, Integer mapt, String catalog);
	
	public List<ArcgisInfoOfPublic> getArcgisServiceOutletsLocateDataListByIds(String ids, Integer mapt);
	
	/**
	 * 根据ids查询重点单位地图数据信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisImportUnitLocateDataListByIds(String ids,Integer mapt);

	public List<ArcgisInfoOfPublic> getArcgisActivityLocateDataListByIds(String ids,Integer mapt, String resTypeCode);

	List<ArcgisInfoOfPublic> getArcgisTwoStationLocateDataListByIds(
			String resourceIds, Integer mapt, String resTypeCode);

	List<ArcgisInfoOfPublic> getArcgisTwoStationLocateDataListByIds(
			String resourceIds, Integer mapt, String markType, Map<String, Object> params);

	List<ArcgisInfoOfPublic> getArcgisTwoMemberLocateDataListByIds(String ids, Integer mapt, Map<String, Object> params);

	/**
	 * 宣传阵地
	 * @param resourceIds
	 * @param mapt
	 * @param markType
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisPropagandaPositionLocateDataListByIds(String resourceIds, Integer mapt, String markType);
	/**
	 * 根据resourceIds 和mapt获取安监队伍的定位信息
	 * @param resourceIds
	 * @param mapt
	 * @param resTypeCode
	 * @return
	 */
	public List<ArcgisInfoOfHashId> getArcgisControlsafetyRanksLocateDataListByIds(String resourceIds, Integer mapt, String resTypeCode);

	/**
	 * 
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisStoreLocateDataListByIds(String ids,
			Integer mapt);
	
	
	public List<ArcgisInfoOfPublic>  getArcgisParkingManageLocateDataListByIds(String ids,
			Integer mapt);
	
	/**
	 * 2015-05-22 sulch add
	 * 根据id 和mapt获取违法建设定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisCtIllegalLocateDataListByIds(String ids, Integer mapt);
	
	/**
	 * 2015-08-19 liushi add 九小场所地图标注信息获取
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisZzSmallPlaceLocateDataList(String ids,Integer mapt);
	
	/**
	 * 2015-08-28 liushi add 获取网格的轮廓数据（单个的网格）
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	public ArcgisInfoOfGrid getArcgisGridLocateData(Long gridId,Integer mapt);
	
	/**
	 * 2015-08-31 liushi add 根据模块编码查询颜色情况
	 * @param modularCode 模块编码
	 * @return
	 */
	public List<Map<String,Object>> getColorList(String modularCode);
	
	/**
	 * 根据resourcesIds、markerType 和mapt获取资源的定位信息
	 * @param resourcesIds 业务主键id拼接成的字符串
	 * @param mapt
	 * @param markerType 业务模块类型
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfPrecisionPovertyByIds(String resourcesIds, Integer mapt, String markerType);

	public List<ArcgisInfoOfPublic> findRoadMarkersByIds(String ids, Integer mapt, String resTypeCode);
	
	/**
	 * 各视联网信息中心获取地图标注信息
	 * @param nicIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisNicLocateDataListByIds(String nicIds,Integer mapt);
	
	/**
	 * 海康设备获取地图标注信息
	 * @param eqpIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisEqpLocateDataListByIds(String eqpIds,Integer mapt);
	
	/**
	 * 海康布控设备获取地图标注信息
	 * @param eqpIds
	 * @param mapt
	 * @param controlTargetId 布控对象id
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getIdssControlEqpLocateDataListByIds(String eqpIds,Integer mapt, Long controlTargetId);
	
	/**
	 * 2016-06-13 qiud
	 * 渔船路径记录
	 * @param locateTimeBegin
	 * @param locateTimeEnd
	 * @param appDeviceId
	 * @return
	 */
	public List<LocateInfo> getTrajectoryList(String locateTimeBegin,String locateTimeEnd,Long appDeviceId);

	/**
	 * 2016-06-14 qiud
	 * 渔船定位
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisBoatLocateDataListByIds(String ids);

	List<ArcgisInfoOfPublic> getArcgisEqpLocateDataList(
			Map<String, Object> params);

	public List<ArcgisInfo> getUrbanObjGisLocateDataList(String infoOrgCode, Integer mapt, String objCode);
	
	public List<ArcgisInfo> getArcgisInfoList(List<Long> ids, Integer mapt, String markerType);

	public Map<String, Object> getGlobalListByDeviceNums(String deviceNums);
	
	public List<ArcgisInfo> getBizLocateInfoList(String bizType);

	/**
	 * 2017-05-16 sulch add
	 * 根据id 和mapt获取信访责任人定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisPetitionLocateDataListByIds(String ids,Integer mapt);

	List<ArcgisInfoOfPublic> getArcgisTwoMemberLocateDataListByIds(String ids, Integer mapt);

	List<ArcgisInfoOfPublic> getArcgisImportantUnitLocateDataListByIds(
			String resourceIds, Integer mapt, String markType);

	public List<ArcgisHeatMapInfo> getTrafficAccidentEvents(String infoOrgCode, Integer mapt);

	/**
	 * 根据id 和mapt获取重点车辆定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDataListOfKeyVehiclesByIds(String ids,Integer mapt);

	/**
	 * 党员图层撒点
	 * @param infoOrgCode
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisPartyMemberDataList(String infoOrgCode,Integer mapt);

	/**
	 * 根据id 和mapt获取两站两员隐患路口、隐患路段定位信息
	 * @param drIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDangerRoadLocateDataListByIds(String drIds,Integer mapt);

	public List<ArcgisInfo> getArcgisCorDataList(String infoOrgCode, Integer mapt);
	/**
	 * 根据id 和mapt获取驰名商标定位信息
	 * @param ids
	 * @param mapt
	 * @param markType
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisTrademarkLocateDataListByIds(String ids,Integer mapt,String markType);
	/*
	 * 根据id 和mapt获取守重企业定位信息
	 * @param ds
	 * @param mapt
	 * @return
	*/
	public List<ArcgisInfoOfPublic> getArcgisEnterpriseLocateDataListByIds(String ids,Integer mapt,String markType);

	/**
	 * 根据地域取党组织点位信息
	 * @param infoOrgCode
	 * @param mapt
	 * @param markerType
	 * @return
	 */
	public List<ArcgisInfo> getNewPartyGroupXYList(String infoOrgCode, Integer mapt, String markerType);
	
	public List<ArcgisInfo> getNewPartyGroupXYById(String ids, Integer mapt, String markerType);

	public List<ArcgisInfo> getArcgisGanSuDrugLocateDataListByUserIds(String userIds,Integer mapt);

	public List<ArcgisInfo> getArcgisRectifyDataList(String infoOrgCode, Integer mapt);

	public List<ArcgisInfo> getArcgisCampsDataList(String infoOrgCode, Integer mapt);

	public List<ArcgisInfo> getArcgisJiangxiPetitionDataList(
			String infoOrgCode, Integer mapt);
	public List<ArcgisInfoOfPublic> getArcgisPoliceLocateDataListByIds(
			String ids, Integer mapt, String string);

	/**
	 * 2018-03-14 sulch add
	 * 根据id 和mapt获取综合执法人员定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisLawEnforceOfficLocateDataListByIds(String ids,Integer mapt, Integer socialOrgId);

	/**
	 * 根据所属网格获取全球眼定位信息
	 * @param infoOrgCode
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisGlobalEyesDataList(String infoOrgCode, Integer mapt);

	/**
	 * 获取法人定位数据
	 *
	 * @param ids
	 * @param mapt
	 * @param catalog
	 * @return
	 */
	public List<ArcgisBaseInfo> getArcgisLegalPersonLocateDataListByIds(String ids, Integer mapt);

	/**
	 * 2019-03-04 CaiJP add
	 * 根据楼栋Id获取楼栋信息
	 * @param buildingId
	 * @return
	 */
	List<Map<String, Object>> getBuildOfPopByBuildingId(Long buildingId);
	

	/**
	 * 2018-03-14 wtl
	 * 根据id 和mapt获取民警管理定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfPcManageByIds(String resourcesIds,Integer mapt, String markerType);

	public List<ArcgisInfoOfPublic> getArcgisCorEnvNPLocateDataListByIds(String ids, Integer mapt, String string);

}
