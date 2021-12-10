package cn.ffcs.zhsq.map.organizationarcgis.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.base.domain.db.OrgExtraInfo;
import cn.ffcs.shequ.base.domain.db.OrgLocationInfo;
import cn.ffcs.zhsq.map.organizationarcgis.service.IOrganizationArcgisService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfo;
import cn.ffcs.zhsq.mybatis.persistence.map.organizationarcgis.OrganizationArcgisMapper;


@Service(value="organizationArcgisServiceImpl")
public class OrganizationArcgisServiceImpl implements IOrganizationArcgisService{
	@Autowired
	private OrganizationArcgisMapper organizationArcgisMapper;

	/**
	 * 2014-09-03 liushi add
	 * 1、查询当前组织机构的基本信息
	 * @param orgId
	 * @return
	 */
	@Override
	public OrgExtraInfo getOrgExtraInfo(Long orgId) {
		OrgExtraInfo orgExtraInfo = this.organizationArcgisMapper.getOrgExtraInfo(orgId);
		return orgExtraInfo;
	}
	
	@Override
	public OrgExtraInfo getOrgEntityInfo(Long orgId) {
		OrgExtraInfo orgExtraInfo = this.organizationArcgisMapper.getOrgEntityInfo(orgId);
		return orgExtraInfo;
	}
	/**
	 * 2014-09-17 liushi add 
	 * 根据组织id和mapt获取与其同级的的组织的轮廓信息
	 * 1.获取同级的组织信息
	 * 2.获取每个组织的轮廓信息，然后进行数据组合
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisDrawDataOfOrgs(Long orgId,Integer mapt){
		List<ArcgisInfo> arcgisInfoList = new ArrayList<ArcgisInfo>();
		List<OrgExtraInfo> orgExtraInfoList = this.organizationArcgisMapper.getArcgisDrawDataOfOrgs(orgId, mapt);
		for(OrgExtraInfo orgExtraInfo:orgExtraInfoList) {
			if(orgExtraInfo.getLatitude() != null) {
				String orgLocationInfoStr = "";
				List<OrgLocationInfo> orgLocationInfoList = this.organizationArcgisMapper.getOrglocationinfoListByMapt(orgExtraInfo.getOrgId(),mapt);
				for(OrgLocationInfo orgLocationInfo : orgLocationInfoList) {
					orgLocationInfoStr = orgLocationInfoStr+","+orgLocationInfo.getLongitude()+","+orgLocationInfo.getLatitude();
				}
				ArcgisInfo arcgisInfo = new ArcgisInfo();
				arcgisInfo.setId(orgExtraInfo.getOrgId());
				arcgisInfo.setWid(orgExtraInfo.getOrgId());
				arcgisInfo.setName(orgExtraInfo.getOrgName());
				arcgisInfo.setX(orgExtraInfo.getLongitude());
				arcgisInfo.setY(orgExtraInfo.getLatitude());
				arcgisInfo.setHs(orgLocationInfoStr.substring(1, orgLocationInfoStr.length()));
				arcgisInfo.setNameColor("#ff0000");
				arcgisInfo.setLineColor("#ff0000");
				arcgisInfo.setLineWidth(2);
				if(orgExtraInfo.getOrgId().equals(orgId)) {
					arcgisInfo.setEditAble(true);
				}
				arcgisInfoList.add(arcgisInfo);
			}
			
		}
		
		return arcgisInfoList;
	}
	
	/**
	 * 2014-09-17 liushi add 
	 * 获取组织的中心点位
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	public OrgExtraInfo getArcgisDataCenterAndLevel(Long orgId,Integer mapt){
		OrgExtraInfo orgExtraInfo = this.organizationArcgisMapper.getArcgisDataCenterAndLevel(orgId, mapt);
		return orgExtraInfo;
	}
	/**
	 * 2014-09-17 liushi add
	 * 保存轮廓编辑信息
	 * @param orgId
	 * @param mapt
	 * @param x
	 * @param y
	 * @param hs
	 * @return
	 */
	public boolean saveArcgisDataOfOrg(Long orgId,Integer mapt,Double x,Double y,String hs){
		OrgExtraInfo orgExtraInfo1 = this.organizationArcgisMapper.getOrgExtraInfo(orgId);
		OrgExtraInfo orgEntityInfo = this.organizationArcgisMapper.getOrgEntityInfo(orgId);
		if(orgExtraInfo1 != null){
			this.organizationArcgisMapper.updateOrgExtraInfo(orgId,x,y);
		}else {
			this.organizationArcgisMapper.insertOrgExtraInfo(orgId,orgEntityInfo.getOrgName(),x,y);
		}
		OrgExtraInfo orgExtraInfo = this.organizationArcgisMapper.getArcgisDataCenterAndLevel(orgId, mapt);
		
		if(orgExtraInfo != null) {//进行修改
			this.organizationArcgisMapper.updateArcgisDataOfOrgCenter(orgId, mapt, x, y);
		}else {//进行新增插入
			this.organizationArcgisMapper.insertArcgisDataOfOrgCenter(orgId, mapt, x, y);
		}
		if(hs!=null && !"".equals(hs)){
			this.organizationArcgisMapper.deleteOrgLocationInfo(orgId,mapt);
			String[] xys = hs.split(",");
			for(int i=0;i*2<xys.length;i++){
				Double localx = Double.valueOf(xys[i*2]);
				Double localy = Double.valueOf(xys[i*2+1]);
				this.organizationArcgisMapper.insertOrgLocationInfo(orgId,mapt,i,localx,localy);
			}
		}
		return true;
	}
	/**
	 * 2014-09-19 liushi add
	 * 获取本身组织的定位轮廓信息
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfo> getArcgisDataOfOrg(Long orgId, Integer mapt) {
		List<ArcgisInfo> arcgisInfoList = new ArrayList<ArcgisInfo>();
		OrgExtraInfo orgExtraInfo = this.organizationArcgisMapper.getArcgisDataOfOrg(orgId, mapt);
		if(orgExtraInfo.getLatitude() != null) {
			String orgLocationInfoStr = "";
			List<OrgLocationInfo> orgLocationInfoList = this.organizationArcgisMapper.getOrglocationinfoListByMapt(orgExtraInfo.getOrgId(),mapt);
			for(OrgLocationInfo orgLocationInfo : orgLocationInfoList) {
				orgLocationInfoStr = orgLocationInfoStr+","+orgLocationInfo.getLongitude()+","+orgLocationInfo.getLatitude();
			}
			ArcgisInfo arcgisInfo = new ArcgisInfo();
			arcgisInfo.setId(orgExtraInfo.getOrgId());
			arcgisInfo.setWid(orgExtraInfo.getOrgId());
			//arcgisInfo.setName(orgExtraInfo.getOrgName());
			arcgisInfo.setX(orgExtraInfo.getLongitude());
			arcgisInfo.setY(orgExtraInfo.getLatitude());
			arcgisInfo.setHs(orgLocationInfoStr.substring(1, orgLocationInfoStr.length()));
			arcgisInfo.setNameColor("#ff0000");
			arcgisInfo.setLineColor("#ff0000");
			arcgisInfo.setLineWidth(2);
			if(orgExtraInfo.getOrgId() == orgId) {
				arcgisInfo.setEditAble(true);
			}
			arcgisInfoList.add(arcgisInfo);
		}
			
		return arcgisInfoList;
	}
	/**
	 * 2014-09-19 liushi add
	 * 获取本身组织的定位中心点信息
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfo> getArcgisDataOfOrgCenter(Long orgId, Integer mapt) {
		List<ArcgisInfo> arcgisInfoList = new ArrayList<ArcgisInfo>();
		OrgExtraInfo orgExtraInfo = this.organizationArcgisMapper.getArcgisDataOfOrg(orgId, mapt);
		ArcgisInfo arcgisInfo = new ArcgisInfo();
		arcgisInfo.setId(orgExtraInfo.getOrgId());
		arcgisInfo.setWid(orgExtraInfo.getOrgId());
		arcgisInfo.setName(orgExtraInfo.getOrgName());
		arcgisInfo.setX(orgExtraInfo.getLongitude());
		arcgisInfo.setY(orgExtraInfo.getLatitude());
		arcgisInfoList.add(arcgisInfo);
		return arcgisInfoList;
	}
	/**
	 * 2014-09-19 liushi add
	 * 获取下级组织的定位轮廓信息
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfo> getArcgisDataOfSubOrgs(Long orgId, Integer mapt) {
		List<ArcgisInfo> arcgisInfoList = new ArrayList<ArcgisInfo>();
		List<OrgExtraInfo> orgExtraInfoList = this.organizationArcgisMapper.getArcgisDataOfSubOrgs(orgId, mapt);
		for(OrgExtraInfo orgExtraInfo:orgExtraInfoList) {
			if(orgExtraInfo.getLatitude() != null) {
				String orgLocationInfoStr = "";
				List<OrgLocationInfo> orgLocationInfoList = this.organizationArcgisMapper.getOrglocationinfoListByMapt(orgExtraInfo.getOrgId(),mapt);
				for(OrgLocationInfo orgLocationInfo : orgLocationInfoList) {
					orgLocationInfoStr = orgLocationInfoStr+","+orgLocationInfo.getLongitude()+","+orgLocationInfo.getLatitude();
				}
				ArcgisInfo arcgisInfo = new ArcgisInfo();
				arcgisInfo.setId(orgExtraInfo.getOrgId());
				arcgisInfo.setWid(orgExtraInfo.getOrgId());
				//arcgisInfo.setName(orgExtraInfo.getOrgName());
				arcgisInfo.setX(orgExtraInfo.getLongitude());
				arcgisInfo.setY(orgExtraInfo.getLatitude());
				arcgisInfo.setHs(orgLocationInfoStr.substring(1, orgLocationInfoStr.length()));
				arcgisInfo.setNameColor("#ff0000");
				arcgisInfo.setLineColor("#ff0000");
				arcgisInfo.setLineWidth(2);
				arcgisInfoList.add(arcgisInfo);
			}
		}
		return arcgisInfoList;
	}
	/**
	 * 2014-09-19 liushi add
	 * 获取下级组织的定位中心点信息
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	@Override
	public List<ArcgisInfo> getArcgisDataOfSubOrgsCenter(Long orgId,
			Integer mapt) {
		List<ArcgisInfo> arcgisInfoList = new ArrayList<ArcgisInfo>();
		List<OrgExtraInfo> orgExtraInfoList = this.organizationArcgisMapper.getArcgisDataOfSubOrgs(orgId, mapt);
		for(OrgExtraInfo orgExtraInfo:orgExtraInfoList) {
			if(orgExtraInfo.getLatitude() != null) {
				ArcgisInfo arcgisInfo = new ArcgisInfo();
				arcgisInfo.setId(orgExtraInfo.getOrgId());
				arcgisInfo.setWid(orgExtraInfo.getOrgId());
				arcgisInfo.setName(orgExtraInfo.getOrgName());
				arcgisInfo.setX(orgExtraInfo.getLongitude());
				arcgisInfo.setY(orgExtraInfo.getLatitude());
				arcgisInfoList.add(arcgisInfo);
			}
		}
		return arcgisInfoList;
	}
}














