package cn.ffcs.zhsq.mybatis.persistence.map.organizationarcgis;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.shequ.base.domain.db.OrgExtraInfo;
import cn.ffcs.shequ.base.domain.db.OrgLocationInfo;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfo;

/**
 * 2014-05-22 liushi add
 * 地图配置信息
 * @author Administrator
 *
 */
public interface OrganizationArcgisMapper{
	
	/**
	 * 2014-09-02 liushi add 
	 * 获取组织信息t_dc_org_info
	 * @param orgId
	 * @return
	 */
	public OrgExtraInfo getOrgExtraInfo(@Param(value="orgId")Long orgId);
	
	public OrgExtraInfo getOrgEntityInfo(@Param(value="orgId")Long orgId);
	
	/**
	 * 2014-09-02 liushi add 
	 * 获取子节点的信息
	 * @param orgId
	 * @return
	 */
	public List<OrgExtraInfo> getSubOrgExtraInfoList(@Param(value="orgId")Long orgId);
	
	/**
	 * 2014-09-02 liushi add 
	 * 根据orgId获取对应的区域描点信息
	 * @param orgId
	 * @return
	 */
	public List<OrgLocationInfo> getOrglocationinfoList(@Param(value="orgId")Long orgId);
	
	/**
	 * 2014-09-05 liushi
	 * 删除
	 * @param orgId
	 * @return
	 */
	public int deleteOrgLocationInfo(@Param(value="orgId")Long orgId,@Param(value="mapt")Integer mapt);
	
	/**
	 * 插入组织定位信息
	 * @param orgLocationInfo
	 */
	public int insertOrgLocationInfo(@Param(value="orgId")Long orgId,@Param(value="mapt")Integer mapt,@Param(value="mapOrder")Integer mapOrder, @Param(value="x")Double x,@Param(value="y")Double y);
	
	/**
	 * 2014-09-17 liushi add 
	 * 根据组织id和mapt获取与其同级的的组织的轮廓信息
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	public List<OrgExtraInfo> getArcgisDrawDataOfOrgs(@Param(value="orgId")Long orgId,@Param(value="mapt")Integer mapt);
	/**
	 * 2014-09-19 liushi add
	 * 根据组织id和mapt获取组织（包括中心点）信息
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	public OrgExtraInfo getArcgisDataOfOrg(@Param(value="orgId")Long orgId,@Param(value="mapt")Integer mapt);
	/**
	 * 2014-09-19 liushi add
	 * 根据组织id和mapt获取下级组织（包括中心点）信息
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	public List<OrgExtraInfo> getArcgisDataOfSubOrgs(@Param(value="orgId")Long orgId,@Param(value="mapt")Integer mapt);
	/**
	 * 2014-09-17 liushi add 
	 * 根据orgId获取对应的区域描点信息带有mapt
	 * @param orgId
	 * @return
	 */
	public List<OrgLocationInfo> getOrglocationinfoListByMapt(@Param(value="orgId")Long orgId,@Param(value="mapt")Integer mapt);
	
	/**
	 * 2014-09-17 liushi add 
	 * 获取组织的中心点位
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	public OrgExtraInfo getArcgisDataCenterAndLevel(@Param(value="orgId")Long orgId,@Param(value="mapt")Integer mapt);
	
	/**
	 * 2014-09-17 liushi add 
	 * 修改原有的组织中心点
	 * @param orgId
	 * @param mapt
	 * @param x
	 * @param y
	 * @return
	 */
	public int updateArcgisDataOfOrgCenter(@Param(value="orgId")Long orgId,@Param(value="mapt")Integer mapt, @Param(value="x")Double x,@Param(value="y")Double y);
	public int updateOrgExtraInfo(@Param(value="orgId")Long orgId, @Param(value="x")Double x,@Param(value="y")Double y);
	/**
	 * 2014-09-17 liushi add
	 * @param orgId
	 * @param mapt
	 * @param x
	 * @param y
	 * @return
	 */
	public int insertArcgisDataOfOrgCenter(@Param(value="orgId")Long orgId,@Param(value="mapt")Integer mapt, @Param(value="x")Double x,@Param(value="y")Double y);
	public int insertOrgExtraInfo(@Param(value="orgId")Long orgId,@Param(value="orgName")String orgName, @Param(value="x")Double x,@Param(value="y")Double y);
}
