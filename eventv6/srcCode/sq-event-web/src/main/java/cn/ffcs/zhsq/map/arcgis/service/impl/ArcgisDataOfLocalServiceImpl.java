package cn.ffcs.zhsq.map.arcgis.service.impl;

import cn.ffcs.shequ.mybatis.domain.zzgl.productSafety.SafetyCheckInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.productSafety.SafetyPersonManage;
import cn.ffcs.shequ.zzgl.service.productSafety.IRoutineExaminationService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.*;
import cn.ffcs.zhsq.mybatis.domain.trajectory.LocateInfo;
import cn.ffcs.zhsq.mybatis.persistence.map.arcgis.ArcgisDataOfLocalMapper;
import cn.ffcs.zhsq.mybatis.persistence.map.arcgis.ArcgisInfoMapper;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value="arcgisDataOfLocalService")
public class ArcgisDataOfLocalServiceImpl implements IArcgisDataOfLocalService {
	
	
	@Autowired
	private ArcgisDataOfLocalMapper arcgisDataOfLocalMapper;
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	private IRoutineExaminationService routineExaminationService;
	@Autowired
	private ArcgisInfoMapper arcgisInfoMapper;
	
	/**
	 * 2015-04-9 huangmw add
	 * 根据userId 和mapt获取安全隐患定位信息
	 * @param ids checkId集合
	 * @param mapt 地图类型
	 * @return 返回安全隐患地图定位信息数据
	 */
	@Override
	public List<ArcgisInfoOfPublic> getArcgisRiskLocateDataListByIds(String ids,Integer mapt){
		List<SafetyCheckInfo> list = new ArrayList<SafetyCheckInfo>();
		
		String[] idss = ids.split(",");
		
		for(int j=0;j<idss.length;j++){
			SafetyCheckInfo safetyCheckInfo = routineExaminationService.findSafetyCheckInfoById(Long.valueOf(idss[j]));
			list.add(safetyCheckInfo);
		}
		
		String ids1="";//企业
		String ids2="";//场所
		String ids3="";//出租屋
		
		for(SafetyCheckInfo safetyCheckInfo:list) {
			if(1 == safetyCheckInfo.getSiteType()){
				ids1 = "".equals(ids1)? String.valueOf(safetyCheckInfo.getCheckId()) : ids1 +","+String.valueOf(safetyCheckInfo.getCheckId());
			}else if(2 == safetyCheckInfo.getSiteType()) {
				ids2 = "".equals(ids2)? String.valueOf(safetyCheckInfo.getCheckId()) : ids2 +","+String.valueOf(safetyCheckInfo.getCheckId());
			}else if(3 == safetyCheckInfo.getSiteType()) {
				ids3 = "".equals(ids3)? String.valueOf(safetyCheckInfo.getCheckId()) : ids3 + ","+String.valueOf(safetyCheckInfo.getCheckId());
			}
		}
		
		List<ArcgisInfoOfPublic> list1 = new ArrayList<ArcgisInfoOfPublic>();
		List<ArcgisInfoOfPublic> list2 = new ArrayList<ArcgisInfoOfPublic>();
		List<ArcgisInfoOfPublic> list3 = new ArrayList<ArcgisInfoOfPublic>();
		
		if (!"".equals(ids1)) {
			list1 = this.arcgisDataOfLocalMapper.getArcgisRiskLocateDataListByIdsOne(ids1, mapt);
		}
		
		if (!"".equals(ids2)) {
			list2 = this.arcgisDataOfLocalMapper.getArcgisRiskLocateDataListByIdsTwo(ids2, mapt);
		}
		
		if (!"".equals(ids3)) {
			list3 = this.arcgisDataOfLocalMapper.getArcgisRiskLocateDataListByIdsThree(ids3, mapt);
		}
		
		List<ArcgisInfoOfPublic> resultList = new ArrayList<ArcgisInfoOfPublic>();
		resultList.addAll(list1);
		resultList.addAll(list2);
		resultList.addAll(list3);
		return resultList;
	}
	
	/**
	 * 2014-06-23 liushi add
	 * 根据userId 和mapt获取人员定位信息
	 * @param userIds
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfPeople> getArcgisPopLocateDataListByUserIds(String userIds, Integer mapt, String infoOrgCode) {
		if (StringUtils.isNotBlank(userIds)) {
			return this.arcgisDataOfLocalMapper.getArcgisPopLocateDataListByUserIds(userIds, mapt, infoOrgCode);
		}
		return null;
	}
	
	/**
	 * 2016-04-14 qiudong add
	 * 根据memberId 和mapt获取人员定位信息
	 * @param memberId
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfPublic> getArcgisPopLocateDataListByMemberId(String memberId, Integer mapt) {
		
		return this.arcgisDataOfLocalMapper.getArcgisPopLocateDataListByMemberId(memberId, mapt);
		
	}
	
	@Override
	public List<Map<String,Object>> getBuildOfPopByUserId(Long userId) {
		return this.arcgisDataOfLocalMapper.getBuildOfPopByUserId(userId);
	}
	
	/**
	 * 2019-03-04 CaiJP add
	 * 根据楼栋Id获取楼栋信息
	 * 解决根据现居住地址获取楼栋，同名楼栋不同网格得的数据全部出来
	 * 无法根据现居住地址判断是哪个网格，只能新增接口使用楼栋id获取楼栋信息
	 * @param buildingId
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getBuildOfPopByBuildingId(Long buildingId) {
		return this.arcgisDataOfLocalMapper.findBuildOfPopByBuildingId(buildingId);
	}
	
	/**
	 * 2015-02-25 huangmw add
	 * 根据id 和mapt获取消防安全管理员定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfPublic> getArcgisSafetyPersonManageLocateDataListByIds(String ids, Integer mapt) {
		List<SafetyPersonManage> list = routineExaminationService.findSafetyPersonListByIds(ids);
		String ids1="";//企业
		String ids2="";//场所
		String ids3="";//出租屋
		for(SafetyPersonManage obj:list) {
			if(1 == obj.getSiteType()){
				ids1 = "".equals(ids1)? String.valueOf(obj.getCorpStaffId()) : ids1 +","+String.valueOf(obj.getCorpStaffId());
			}else if(2 == obj.getSiteType()) {
				ids2 = "".equals(ids2)? String.valueOf(obj.getCorpStaffId()) : ids2 +","+String.valueOf(obj.getCorpStaffId());
			}else if(3 == obj.getSiteType()) {
				ids3 = "".equals(ids3)? String.valueOf(obj.getCorpStaffId()) : ids3 + ","+String.valueOf(obj.getCorpStaffId());
			}
		}
		
		List<ArcgisInfoOfPublic> list1 = new ArrayList<ArcgisInfoOfPublic>();
		List<ArcgisInfoOfPublic> list2 = new ArrayList<ArcgisInfoOfPublic>();
		List<ArcgisInfoOfPublic> list3 = new ArrayList<ArcgisInfoOfPublic>();
		
		if (!"".equals(ids1)) {
			list1 = this.arcgisDataOfLocalMapper.getArcgisSafetyPersonManageLocateDataListByIdsOne(ids1, mapt);
		}
		
		if (!"".equals(ids2)) {
			list2 = this.arcgisDataOfLocalMapper.getArcgisSafetyPersonManageLocateDataListByIdsTwo(ids2, mapt);
		}
		
		if (!"".equals(ids3)) {
			list3 = this.arcgisDataOfLocalMapper.getArcgisSafetyPersonManageLocateDataListByIdsThree(ids3, mapt);
		}
		
		List<ArcgisInfoOfPublic> resultList = new ArrayList<ArcgisInfoOfPublic>();
		resultList.addAll(list1);
		resultList.addAll(list2);
		resultList.addAll(list3);
		return resultList;
	}
	
	/**
	 * 2014-06-24 chenlf add
	 * 根据id 和mapt获取全球眼的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisGlobalEyesLocateDataListByIds(String ids,Integer mapt,String orgCode){
		return this.arcgisDataOfLocalMapper.getArcgisGlobalEyesLocateDataListByIds(ids, mapt, orgCode);
	}

	@Override
	public List<ArcgisInfo> getArcgisGlobalEyesLocateDataListByDeviceNums(String deviceNums, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisGlobalEyesLocateDataListByDeviceNums(deviceNums, mapt);
	}

	@Override
	public Map<String, Object> getGlobalListByDeviceNums(String deviceNums) {
		return this.arcgisDataOfLocalMapper.getGlobalListByDeviceNums(deviceNums);
	}

	/**
	 * 2015-01-30 huangmw add
	 * 根据id 和mapt获取水文监测点位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisSwjcLocateDataListByIds(String ids,Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisSwjcLocateDataListByIds(ids, mapt);
	}

	/**
	 * 2014-06-26 zhongshm add
	 * 根据id 和mapt获取全球眼的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfPublic> getArcgisEventLocateDataListByIds(String ids,Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisEventLocateDataListByIds(ids, mapt);
	}
	
	/**
	 * 2014-08-21 chenlf add
	 * 根据id 和mapt获取事件调度模块事件信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisEventDisposalLocateDataListByIds(String ids, Integer mapt){
		return this.arcgisDataOfLocalMapper.getArcgisEventDisposalLocateDataListByIds(ids, mapt);
	}

	/**
	 * 2015-01-19 zhongshm
	 * 根据id 和mapt获取事件调度模块事件信息（附带事件数据）
	 * @param ids
	 * @param mapt
	 * @returne
	 */
	public List<ArcgisInfoOfPublic> getArcgisEventDisposalLocateDataListByIdsWithEvent(String ids, Integer mapt){
		List<ArcgisInfoOfPublic> arcgisInfoList = this.arcgisDataOfLocalMapper.getArcgisEventDisposalLocateDataListByIdsWithEvent(ids, mapt);
		for(ArcgisInfoOfPublic arcgisInfoOfPublic : arcgisInfoList){
			EventDisposal event = eventDisposalService.findEventById(arcgisInfoOfPublic.getWid(), null);
			arcgisInfoOfPublic.setEventDisposal(event);
		}
		return arcgisInfoList;
	}
	
	public List<ArcgisInfoOfPublic> getArcgisImportantEventDisposalLocateDataListByIds(String ids, Integer mapt){
		return this.arcgisDataOfLocalMapper.getArcgisImportantEventDisposalLocateDataListByIds(ids, mapt);
		
	}
	
	
	/**
	 * 2014-06-25 chenlf add
	 * 根据id 和mapt获取网格员的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisGridAdminLocateDataListByIds(String ids,Integer mapt){

		return this.arcgisDataOfLocalMapper.getArcgisGridAdminLocateDataListByIds(ids, mapt);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisGridAdminLocateDataListByIdsUserName(String ids, Integer mapt, boolean userNameFlag) {
		if(userNameFlag){
			return this.arcgisDataOfLocalMapper.getArcgisGridAdminLocateDataListByIdsUserName(ids, mapt);
		}else{
			return this.arcgisDataOfLocalMapper.getArcgisGridAdminLocateDataListByIds(ids, mapt);
		}

	}

	/**
	 * 2015-02-6 huangmw add
	 * 根据id 和mapt获取护路护线队员的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisCareRoadMemberLocateDataListByIds(String ids,Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisCareRoadMemberLocateDataListByIds(ids, mapt);
	}
	
	/**
	 * 2014-07-01 chenlf add
	 * 根据buildingIds 和mapt获取网格员的定位信息
	 * @param buildingIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisBuildingLocateDataListByIds(String buildingIds,Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisBuildingLocateDataListByIds(buildingIds, mapt);
	}
	
	public List<ArcgisInfo> getBuildingLocateDataList(String infoOrgCode, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getBuildingLocateDataList(infoOrgCode, mapt);
	}
	
	/**
	 * 2014-07-01 chenlf add
	 * 根据keyPlaceIds 和mapt获取重点场所的定位信息
	 * @param keyPlaceIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisKeyPlaceLocateDataListByIds(String keyPlaceIds,Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisKeyPlaceLocateDataListByIds(keyPlaceIds, mapt);
	}

	/**
	 * 2014-07-01 chenlf add
	 * 根据keyPlaceIds 和mapt获取重点场所的定位信息
	 * @param keyPlaceIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisKeyPlaceLocateDataListByIdsYD(String keyPlaceIds,Integer mapt){

		return this.arcgisDataOfLocalMapper.getArcgisKeyPlaceLocateDataListByIdsYD(keyPlaceIds, mapt);
	}

	public List<ArcgisInfoOfPublic> getKeyPlaceLocateDataListByIds(String keyPlaceIds,Integer mapt,String markerType){
		
		return this.arcgisDataOfLocalMapper.getKeyPlaceLocateDataListByIds(keyPlaceIds, mapt, markerType);
	}

	
	/**
	 * 2014-07-8 chenlf add
	 * 根据resourceIds 和mapt获取资源的定位信息
	 * @param resourceIds
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisResourceLocateDataListByIds(String resourceIds,Integer mapt,String resTypeCode){
		
		return this.arcgisDataOfLocalMapper.getArcgisResourceLocateDataListByIds(resourceIds, mapt,resTypeCode);
	}

	/**
	 * 2017-06-23 sulch add
	 * 根据resourceIds 和mapt获取资源的定位信息
	 * @param resourceIds
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfPublic> getBuildLocateDataListByResIds(String resourceIds, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getBuildLocateDataListByResIds(resourceIds, mapt);
	}

	/**
	 * 2014-07-9 chenlf add
	 * 根据ids 和mapt获取出租屋的定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisRentRoomLocateDataListByIds(String ids,Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisRentRoomLocateDataListByIds(ids, mapt);
	}
	
	/**
	 * 2014-07-04 huangmw add
	 * 根据id 和mapt获取企业信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisCorLocateDataListByIds(String ids,Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisCorLocateDataListByIds(ids, mapt);
	}

	public List<ArcgisInfoOfPublic> getArcgisCorLocateDataListByIds(String ids,Integer mapt, String markerType){
		
		return this.arcgisDataOfLocalMapper.getCorLocateDataListByIds(ids, mapt, markerType);
	}

	public List<ArcgisInfoOfPublic> getArcgisCorEnvLocateDataListByIds(String ids,Integer mapt, String markerType){
		return this.arcgisDataOfLocalMapper.getCorEnvLocateDataListByIds(ids, mapt, markerType);
	}

	
	/**
	 * 2014-07-09 huangmw add
	 * 根据id 和mapt获取党组织信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisPartyGroupLocateDataListByIds(String ids,Integer mapt,String markerType){
		
		return this.arcgisDataOfLocalMapper.getArcgisPartyGroupLocateDataListByIds(ids, mapt, markerType);
	}
	
	/**
	 * 2017-11-24 tursh add
	 * 根据resourceIds 和mapt获取资源的定位信息
	 * @param Ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisPoliceLocateDataListByIds(String resourceIds,Integer mapt,String resTypeCode){
		
		return this.arcgisDataOfLocalMapper.getArcgisPoliceLocateDataListByIds(resourceIds, mapt,resTypeCode);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisLawEnforceOfficLocateDataListByIds(String ids, Integer mapt, Integer socialOrgId) {
		return this.arcgisDataOfLocalMapper.getArcgisLawEnforceOfficLocateDataListByIds(ids, mapt, socialOrgId);
	}

	/**
	 * 2014-09-03 chenlf add
	 * 根据id 和mapt获取巡逻队、警务室、治保组织等社会组织信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisSocietyOrgLocateDataListByIds(String ids,Integer mapt,String markerType){
		
		return this.arcgisDataOfLocalMapper.getArcgisSocietyOrgLocateDataListByIds(ids, mapt,markerType);
	}


	/**
	 * 2014-09-03 chenlf add
	 * 根据id 和mapt获取宜居罗坊定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */

	public List<ArcgisInfoOfPublic> getArcgisLivableLocateDataListByIds(String ids,Integer mapt,String markerType){

		return this.arcgisDataOfLocalMapper.getArcgisLivableLocateDataListByIds(ids, mapt,markerType);
	}


	public List<ArcgisInfoOfPublic> getArcgisLogisticsSafetyLocateDataListByIds(String ids,Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisLogisticsSafetyLocateDataListByIds(ids, mapt);
	}
	
	/**
	 * 2014-09-03 chenlf add
	 * 根据id 和mapt获取案件警情信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisCasesLocateDataListByIds(String ids,Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisCasesLocateDataListByIds(ids, mapt);
	}

	public List<ArcgisInfoOfPublic> getArcgisDisputeLocateDataListByIds(String ids,Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisDisputeLocateDataListByIds(ids, mapt);
	}
	
	
	/**
	 * 2014-09-03 chenlf add
	 * 根据id 和mapt获取隐患信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisDangousLocateDataListByIds(String ids,Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisDangousLocateDataListByIds(ids, mapt);
	}
	

	public List<ArcgisInfoOfPublic> getArcgisLandscapeLocateDataListByIds(String ids,String type, Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisLandscapeLocateDataListByIds(ids,type, mapt);
	}
	/**
	 * 2014-09-20 chenlf add
	 * 根据id 和mapt获取两新组织信息信息
	 * @param ids
	 * @param mapt
	 * @param type 新经济组织 01B029
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisNewGroupLocateDataListByIds(String ids,String type, Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisNewGroupLocateDataListByIds(ids,type, mapt);
	}
	
	
	/**
	 * 2014-12-19 huangmw add
	 * 根据id 和mapt获取新经济组织信息
	 * @param ids
	 * @param type 新社会组织 01B032
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisNewSocietyLocateDataListByIds(String ids,String type, Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisNewSocietyLocateDataListByIds(ids,type, mapt);
	}
	
	/**
	 * 2014-09-25 huangmw add
	 * 根据id 和mapt获取台江首页分类管理城市综合体信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisSegmentGridDataListByIds(String ids, Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisSegmentGridDataListByIds(ids, mapt);
	}
	
	/**
	 * 2014-10-31 chenlf add
	 * 根据id 和mapt获取防控片区信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfoOfPublic> getArcgisSegmentGridDataListByParentGridId(Long parentGridId, Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisSegmentGridDataListByParentGridId(parentGridId, mapt);
		
	}

	/**
	 * 2015-01-20 liushi add 根据id查询本身的地图数据信息
	 * @param wid
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfPublic> getArcgisDataOfResourcesSelfById(Long wid,
			Integer mapt, String targetType) {
		
		return this.arcgisDataOfLocalMapper.getArcgisDataOfResourcesSelfById(wid, mapt, targetType);
	}
	/**
	 * 2015-01-20 liushi add 根据id查询资源对象本身同级的其他资源地图数据
	 * @param wid
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfPublic> getArcgisDataOfResourcesSamelevelById(
			Long wid, Integer mapt, String targetType) {
		return this.arcgisDataOfLocalMapper.getArcgisDataOfResourcesSamelevelById(wid, mapt, targetType);
	}
	
	/**
	 * 2015-01-20 liushi add 根据id查询资源对象父级节点（网格）地图数据
	 * @param wid
	 * @param mapt
	 * @param targetType
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfPublic> getArcgisDataOfResourcesParentById(
			Long wid, Integer mapt, String targetType) {
		return this.arcgisDataOfLocalMapper.getArcgisDataOfResourcesParentById(wid, mapt, targetType);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisNewResourceLocateDataListByIds(
			String resourceIds, Integer mapt, String resTypeCode) {
		return this.arcgisDataOfLocalMapper.getArcgisNewResourceLocateDataListByIds(resourceIds, mapt, resTypeCode);
	}
	
	@Override
	public List<ArcgisInfoOfPublic> getArcgisFireTeamLocateDataListByIds(String ids, Integer mapt, String markType) {
		return this.arcgisDataOfLocalMapper.getArcgisFireTeamLocateDataListByIds(ids, mapt, markType);
	}
	
	@Override
	public List<ArcgisInfoOfPublic> getArcgisFireTeamNewLocateDataListByIds(String ids, Integer mapt, String markType) {
		return this.arcgisDataOfLocalMapper.getArcgisFireTeamNewLocateDataListByIds(ids, mapt, markType);
	}
	
	@Override
	public List<ArcgisInfoOfPublic> getArcgisServiceOutletsLocateDataListByIds(String ids,Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisDataOfServiceOutletsByIds(ids, mapt);
	}
	
	@Override
	public List<ArcgisInfoOfPublic> getArcgisImportUnitLocateDataListByIds(String ids,Integer mapt){
			
		return this.arcgisDataOfLocalMapper.getArcgisImportUnitLocateDataListByIds(ids, mapt);
	}
	
	@Override
	public List<ArcgisInfoOfPublic> getArcgisActivityLocateDataListByIds(String ids,Integer mapt, String resTypeCode){
			
		return this.arcgisDataOfLocalMapper.getArcgisActivityLocateDataListByIds(ids, mapt, resTypeCode);
	}
	
	@Override
	public List<ArcgisInfoOfPublic> getArcgisStoreLocateDataListByIds(String ids,Integer mapt){
		return this.arcgisDataOfLocalMapper.getArcgisStoreLocateDataListByIds(ids, mapt);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisCampusLocateDataListByIds(
			String resourceIds, Integer mapt, String resTypeCode) {
		return this.arcgisDataOfLocalMapper.getArcgisCampusLocateDataListByIds(resourceIds, mapt, resTypeCode);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisTwoStationLocateDataListByIds(
			String resourceIds, Integer mapt, String markType) {
		return this.arcgisDataOfLocalMapper.getArcgisTwoStationLocateDataListByIds(resourceIds, markType,mapt);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisTwoStationLocateDataListByIds(
			String resourceIds, Integer mapt, String markType, Map<String, Object> params) {
		return this.arcgisDataOfLocalMapper.getArcgisTwoStationLocateDataListByIds(resourceIds, markType,mapt);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisTwoMemberLocateDataListByIds(String ids, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisTwoMemberLocateDataListByIds(ids, mapt);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisTwoMemberLocateDataListByIds(String ids, Integer mapt, Map<String, Object> params) {
		return this.arcgisDataOfLocalMapper.getArcgisTwoMemberLocateDataListByIds(ids, mapt);
	}

	/**
	 * 宣传阵地
	 */
	@Override
	public List<ArcgisInfoOfPublic> getArcgisPropagandaPositionLocateDataListByIds(
			String resourceIds, Integer mapt, String markType) {
		return this.arcgisDataOfLocalMapper.getArcgisPropagandaPositionLocateDataListByIds(resourceIds, markType,mapt);
	}
	
	@Override
	public List<ArcgisInfoOfPublic> getArcgisImportantUnitLocateDataListByIds(
			String resourceIds, Integer mapt, String markType) {
		return this.arcgisDataOfLocalMapper.getArcgisImportantUnitLocateDataListByIds(resourceIds, markType,mapt);
	}

	@Override
	public List<ArcgisInfoOfHashId> getArcgisControlsafetyRanksLocateDataListByIds(
			String resourceIds, Integer mapt, String resTypeCode) {
		return this.arcgisDataOfLocalMapper.getArcgisControlsafetyRanksLocateDataListByIds(resourceIds, mapt, resTypeCode);
	}
	
	@Override
	public List<ArcgisInfoOfPublic> getArcgisParkingManageLocateDataListByIds(String ids,Integer mapt){
		return this.arcgisDataOfLocalMapper.getArcgisParkingManageLocateDataListByIds(ids, mapt);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisCtIllegalLocateDataListByIds(
			String ids, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisCtIllegalLocateDataListByIds(ids, mapt);
	}
	/**
	 * 2015-08-19 liushi add 九小场所地图标注信息获取
	 * @param ids
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfPublic> getArcgisZzSmallPlaceLocateDataList(String ids,Integer mapt){
		
		return this.arcgisDataOfLocalMapper.getArcgisZzSmallPlaceLocateDataList(ids, mapt);
	}
	/**
	 * 2015-08-28 liushi add 获取网格的轮廓数据（单个的网格）
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	@Override
	public ArcgisInfoOfGrid getArcgisGridLocateData(Long gridId,Integer mapt){
		return this.arcgisInfoMapper.getArcgisGridLocateData(gridId,mapt);
	}
	/**
	 * 2015-08-31 liushi add 根据模块编码查询颜色情况
	 * @param modularCode 模块编码
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getColorList(String modularCode){
		return this.arcgisDataOfLocalMapper.getColorList(modularCode);
	}

	/**
	 * 2016-01-04 sulch add
	 * 根据ids 和mapt获取出租屋的定位信息---海沧是通过手动标注不是关联楼宇
	 * @param ids
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfPublic> getArcgisRentRoomLocateDataListByIdsHC(
			String ids, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisRentRoomLocateDataListByIdsHC(ids, ConstantValue.MARKER_TYPE_ROOM_RENT, mapt);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfPrecisionPovertyByIds(
			String resourcesIds, Integer mapt, String markerType) {
		return this.arcgisDataOfLocalMapper.getArcgisLocateDataListOfPrecisionPovertyByIds(resourcesIds, mapt, markerType);
	}

	@Override
	public List<ArcgisInfoOfPublic> findRoadMarkersByIds(String ids, Integer mapt, String resTypeCode) {
		return arcgisDataOfLocalMapper.findRoadMarkersByIds(ids, mapt, resTypeCode);
	}
	
	@Override
	public List<ArcgisInfoOfPublic> getArcgisNicLocateDataListByIds(String nicIds,Integer mapt){
		return this.arcgisDataOfLocalMapper.getArcgisNicLocateDataListByIds(nicIds, mapt);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisEqpLocateDataListByIds(String eqpIds,Integer mapt){
		return this.arcgisDataOfLocalMapper.getArcgisEqpLocateDataListByIds(eqpIds, mapt);
	}
	
	@Override
	public List<ArcgisInfoOfPublic> getIdssControlEqpLocateDataListByIds(String eqpIds,Integer mapt, Long controlTargetId){
		return this.arcgisDataOfLocalMapper.getIdssControlEqpLocateDataListByIds(eqpIds, mapt, controlTargetId);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisEqpLocateDataList(Map<String,Object> params){
		return this.arcgisDataOfLocalMapper.getArcgisEqpLocateDataList(params);
	}
	
	@Override
	public List<LocateInfo> getTrajectoryList(String locateTimeBegin,
			String locateTimeEnd, Long appDeviceId) {
		List<LocateInfo> list = this.arcgisInfoMapper.getTrajectoryList(locateTimeBegin,locateTimeEnd,appDeviceId);
		return list;
	}

	@Override
	public List<ArcgisInfo> getArcgisBoatLocateDataListByIds(String ids) {
		return this.arcgisDataOfLocalMapper.getArcgisBoatLocateDataListByIds(ids);
	}

	@Override
	public List<ArcgisInfo> getUrbanObjGisLocateDataList(String infoOrgCode, Integer mapt, String objCode) {
		return this.arcgisDataOfLocalMapper.getUrbanObjGisLocateDataList(infoOrgCode, mapt, objCode);
	}

	@Override
	public List<ArcgisInfo> getArcgisInfoList(List<Long> ids, Integer mapt, String markerType) {
		List<ArcgisInfo> arcgisList = new ArrayList<ArcgisInfo>();
		if (ids != null && ids.size() > 0) {
			Set<String> set = new HashSet<String>();
			for (Long id : ids) {
				set.add(String.valueOf(id));
			}
			String idStr = StringUtils.join(set, ",");
			arcgisList = arcgisDataOfLocalMapper.getArcgisInfoList(idStr, mapt, markerType);
		}
		return arcgisList;
	}

	@Override
	public List<ArcgisInfo> getBizLocateInfoList(String bizType) {
		List<ArcgisInfo> arcgisInfos = this.arcgisDataOfLocalMapper.getBizLocateInfoList(bizType);
		return arcgisInfos;
	}

	/**
	 * 根据ids和mapt获取信访责任人定位信息
	 * @param ids
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfoOfPublic> getArcgisPetitionLocateDataListByIds(String ids, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisPetitionLocateDataListByIds(ids, mapt);
	}

	@Override
	public List<ArcgisHeatMapInfo> getTrafficAccidentEvents(String infoOrgCode, Integer mapt) {
		List<ArcgisHeatMapInfo> list = new ArrayList<ArcgisHeatMapInfo>();
		list = arcgisDataOfLocalMapper.getTrafficAccidentHeatMapStat(infoOrgCode,mapt);
		return list;
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisDataListOfKeyVehiclesByIds(String ids, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisKeyVehiclesLocateDataListByIds(ids, mapt);
	}

	@Override
	public List<ArcgisInfo> getArcgisPartyMemberDataList(String infoOrgCode, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisPartyMemberDataList(infoOrgCode, mapt);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisDangerRoadLocateDataListByIds(String drIds,Integer mapt){
		return this.arcgisDataOfLocalMapper.getArcgisDangerRoadLocateDataListByIds(drIds, mapt);
	}

	@Override
	public List<ArcgisInfo> getArcgisCorDataList(String infoOrgCode, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisCorDataList(infoOrgCode, mapt);
	}

	@Override
	public List<ArcgisInfo> getNewPartyGroupXYList(String infoOrgCode, Integer mapt, String markerType) {
		return this.arcgisDataOfLocalMapper.getNewPartyGroupXYList(infoOrgCode, mapt, markerType);
	}

	@Override
	public List<ArcgisInfo> getNewPartyGroupXYById(String ids, Integer mapt, String markerType) {
		return this.arcgisDataOfLocalMapper.getNewPartyGroupXYById(ids, mapt, markerType);
	}

	@Override
	public List<ArcgisInfo> getArcgisGanSuDrugLocateDataListByUserIds(String userIds, Integer mapt) {
		if (StringUtils.isNotBlank(userIds)) {
			return this.arcgisDataOfLocalMapper.getArcgisGanSuDrugLocateDataListByUserIds(userIds, mapt);
		}
		return null;
	}

	@Override
	public List<ArcgisInfo> getArcgisRectifyDataList(String infoOrgCode, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisRectifyDataList(infoOrgCode, mapt);
	}

	@Override
	public List<ArcgisInfo> getArcgisCampsDataList(String infoOrgCode, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisCampsDataList(infoOrgCode, mapt);
	}

	@Override
	public List<ArcgisInfo> getArcgisJiangxiPetitionDataList(
			String infoOrgCode, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisJiangxiPetitionDataList(infoOrgCode, mapt);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisTrademarkLocateDataListByIds(String ids, Integer mapt,String markType) {
		return this.arcgisDataOfLocalMapper.getArcgisTrademarkLocateDataListByIds(ids,mapt,markType);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisEnterpriseLocateDataListByIds(String ids, Integer mapt,String markType) {
		return this.arcgisDataOfLocalMapper.getArcgisEnterpriseLocateDataListByIds(ids,mapt,markType);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisGlobalEyesDataList(String infoOrgCode, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisGlobalEyesDataList(infoOrgCode, mapt);
	}

	@Override
	public List<ArcgisBaseInfo> getArcgisLegalPersonLocateDataListByIds(String ids, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisLegalPersonLocateDataListByIds(ids, mapt);
	}
	

	@Override
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfPcManageByIds(String resourcesIds, Integer mapt,
			String markerType) {
		return this.arcgisDataOfLocalMapper.getArcgisLocateDataListOfPcManageByIds(resourcesIds, mapt, markerType);
	}

	@Override
	public List<ArcgisInfoOfPublic> getArcgisCorEnvNPLocateDataListByIds(String ids, Integer mapt, String string) {
		return arcgisDataOfLocalMapper.getArcgisCorEnvNPLocateDataListByIds(ids, mapt, string);
	}

	/**
	 * 根据typeCode 和mapt获取定位信息
	 * @param typeCode 污染检测点02070401、血库信息统计02070403、通讯覆盖02070402
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfo> getArcgisGlobalEyesLocateDataListBypollution(String typeCode, Integer mapt) {
		return this.arcgisDataOfLocalMapper.getArcgisGlobalEyesLocateDataListBypollution(typeCode, mapt);
	}
}
