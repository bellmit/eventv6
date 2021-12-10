package cn.ffcs.zhsq.map.organizationarcgis.service;



import java.util.List;

import cn.ffcs.shequ.base.domain.db.OrgExtraInfo;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfo;
/**
 * 2014-09-03 liushi add
 * @author Administrator
 *
 */
public interface IOrganizationArcgisService {
	/**
	 * 2014-09-03 liushi add
	 * 1、查询当前组织机构的基本信息、以及轮廓定位信息
	 * 2、查询当前组织机构的子节点基本信息以及子节点对应的轮廓定位信息
	 * @param orgId
	 * @return
	 */
	public OrgExtraInfo getOrgExtraInfo(Long orgId);
	public OrgExtraInfo getOrgEntityInfo(Long orgId);
	/**
	 * 2014-09-17 liushi add 
	 * 根据组织id和mapt获取与其同级的的组织的轮廓信息
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisDrawDataOfOrgs(Long orgId,Integer mapt);
	
	/**
	 * 2014-09-19 liushi add
	 * 获取本身组织的定位轮廓信息
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisDataOfOrg(Long orgId,Integer mapt);
	
	/**
	 * 2014-09-19 liushi add
	 * 获取本身组织的定位中心点信息
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisDataOfOrgCenter(Long orgId,Integer mapt);
	
	/**
	 * 2014-09-19 liushi add
	 * 获取下级组织的定位轮廓信息
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisDataOfSubOrgs(Long orgId,Integer mapt);
	
	/**
	 * 2014-09-19 liushi add
	 * 获取下级组织的定位中心点信息
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	public List<ArcgisInfo> getArcgisDataOfSubOrgsCenter(Long orgId,Integer mapt);
	
	/**
	 * 2014-09-17 liushi add 
	 * 获取中心点位
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	public OrgExtraInfo getArcgisDataCenterAndLevel(Long orgId,Integer mapt);
	
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
	public boolean saveArcgisDataOfOrg(Long orgId,Integer mapt,Double x,Double y,String hs);
}
