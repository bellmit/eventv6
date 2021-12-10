package cn.ffcs.zhsq.base.map.dao;

import java.util.List;

import net.sf.json.JSONObject;
import cn.ffcs.shequ.base.domain.db.CatalogType;
import cn.ffcs.shequ.base.domain.db.OrgExtraInfo;
import cn.ffcs.shequ.base.domain.db.OrgLocationInfo;
import cn.ffcs.shequ.grid.domain.db.OrgEntityInfo;

/**
 * @author guohh
 *
 */
public interface IOrgConfigDao {
	
	/**
	 * 获取所有的CatalogType
	 * */	
	public List<CatalogType> getCatalogTypeList();
	
	/**
	 * 获取组织详情
	 * @param orgId
	 * @return
	 */
	public OrgEntityInfo queryEntityByPK(long orgId);
	
	/**
	 * 获取组织下的所有子节点
	 * @param parentOrgId
	 * @return
	 */
	public List<OrgEntityInfo> getSubOrgEntityInfoList(Long parentOrgId);
	
	public List<OrgEntityInfo> getSubOrgEntityInfoList(Long catalogId,Long parentOrgId);
	public int getSubOrgCount(Long catalogId,Long parentOrgId) ;
	
	public OrgEntityInfo getOrgEntityInfo(Long orgId);
	
	public OrgEntityInfo getOrgEntityInfoByCatalogIdandParentOrgId(int catalogId,Long parentOrgId) ;
	
	public List<OrgEntityInfo> getOrgEntityInfoListByCatalogIdandLocationCode(int catalogId,String LocationCode);
	
	public List<OrgEntityInfo> getOrgEntityInfoListByCatalogIdandOrgCode(int catalogId,String orgCode);
	
	public OrgEntityInfo getOrgEntityInfo(Long catalogId,String locationCode);
	
	public int deleteOrgEntityInfo(Long orgId);
	
	/**根据父结点ID获取最大的orgCode*/
	public Long getMaxOrgCodeByParentOrgId(Long catalogId,Long parentOrgId) ;
	
	/**
	 * 根据组织类型ID及组织ID
	 * 取组织
	 * */
	public List<OrgEntityInfo> getOrgEntityInfoList(Long catalogId, Long parentOrgId);
	
	/**
	 * 根据ORG
	 * 取中心点组织配置
	 * */
	public OrgExtraInfo getOrgExtraInfo(Long orgId);
	
	public OrgExtraInfo getOrgExtraInfo(Long orgId,String mapType);
	
	/**
	 * 删除组织对应的点位配置信息
	 * @param orgId
	 * @return
	 */
	public int deleteOrgLocationInfo(Long orgId);
	
	/**
	 * 保存描点配置
	 * @param orgLocationInfoList
	 */
	public void insertOrgLocationInfo(final List<OrgLocationInfo> orgLocationInfoList);
	
	/**
	 * 根据orgID取得中心点下的围点
	 * */
	public List<OrgLocationInfo> getOrgLocationInfoList(Long orgId);
	
	/**
	 * 取子中心点位
	 * */
	public List<OrgExtraInfo> getSubOrgExtraInfoList(Long orgId);
	
	public List<OrgExtraInfo> getSubOrgExtraInfoList(Long orgId,String mapType);
	
	/**
	 * 更新
	 * */
	public boolean updateOrgExtraInfo(OrgExtraInfo gi);
	
	/**
	 * 插入
	 * */
	public boolean insertOrgExtraInfo(OrgExtraInfo gi);
	
	//public PartyUserCertify getPartyUserCertify(long userId);
	
	//public List<CatalogType> getAllCatalogType();
	
	//public CatalogType getCatalogType(long catalogId);
	
	public int getOrgEntityMaxChiefLevel(long catalogId);
	
	/**组织层级类型*/
	public List<JSONObject> getAllOrgLayerTypeList();
	
	//public long updateOrgEntityInfo(OrgEntityInfo orgEntityInfo);
	
	//public long insertOrgEntityInfo(OrgEntityInfo orgEntityInfo) ;
	
	public boolean batchUpdateOrgLocationInfo(List<OrgLocationInfo> orgLocationInfoList);

	
	/**
	 * 获取上级的组织节点
	 * @param subOrgId 开始的组织ID
	 * @param expectLayerType 预期的组织层级类型
	 * @return
	 */
	public Long getParentOrgId(Long subOrgId, String expectLayerType);
}
