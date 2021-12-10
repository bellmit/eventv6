package cn.ffcs.zhsq.firegrid.map.arcgis.service;

import java.util.List;

import net.sf.json.JSONObject;
import cn.ffcs.shequ.base.domain.db.CatalogType;
import cn.ffcs.shequ.base.domain.db.OrgExtraInfo;
import cn.ffcs.shequ.base.domain.db.OrgLocationInfo;
import cn.ffcs.shequ.grid.domain.db.OrgEntityInfo;

public interface IOrgConfigService {
	
	/**
	 * 获取组织详情
	 * @param orgId
	 * @return
	 */
	public OrgEntityInfo queryEntityByPK(long orgId);

	/**
	 * 获取所有的catalogType
	 * */
	public List<CatalogType> getCatalogTypeList();
	
	/**
	 * 根据类型及组织ID
	 * 取组织
	 * */
	public List<OrgEntityInfo> getOrgEntityInfoList(Long catalogId,Long orgId);
	
	public OrgEntityInfo getOrgEntityInfo(Long catalogId,String locationCode);
	
	/**
	 * 根据父结点ID
	 * 取组织
	 * */
	public List<OrgEntityInfo> getOrgEntityInfoList(Long parentOrgId);
	
	public OrgEntityInfo getOrgEntityInfo(Long orgId);
	
	public OrgEntityInfo getOrgEntityInfoByCatalogIdandParentOrgId(int catalogId,Long parentOrgId);
	
	public List<OrgEntityInfo> getOrgEntityInfoListByCatalogIdandOrgCode(int catalogId,String orgCode);
	
	public List<OrgEntityInfo> getOrgEntityInfoListByCatalogIdandLocationCode(int catalogId,String orgCode);
	
	public int deleteOrgEntityInfo(Long orgId);
	
	public boolean batchUpdateOrgLocationInfo(Long orgId,List<OrgLocationInfo> orgLocationInfoList);
	/**
	 * 根据ORG
	 * 取中心点组织配置
	 * */
	public OrgExtraInfo getOrgExtraInfo(Long orgId) ;
	
	public OrgExtraInfo getOrgExtraInfo(Long orgId,String mapType) ;
	
	
	
	/**
	 * 取扩展信息，不包含下级节点信息
	 * @param orgId
	 * @return
	 */
	public OrgExtraInfo getOrgExtraInfoNoSub(Long orgId);
	
	/**
	 * 保存更新组织本置
	 * */
	public boolean saveOrUpdate(OrgExtraInfo orgExtraInfo);
	
	public int getOrgEntityMaxChiefLevel(long catalogId);
	/**组织层级类型*/
	public List<JSONObject> getAllOrgLayerTypeList();
	
	/**
	 * 更新
	 * */
	public boolean updateOrgExtraInfo(OrgExtraInfo gi);
	
	/**
	 * 插入
	 * */
	public boolean insertOrgExtraInfo(OrgExtraInfo gi);
	
	/**根据父结点ID获取最大的orgCode*/
	public Long getMaxOrgCodeByParentOrgId(Long catalogId,Long parentOrgId);
	
	/**
	 * 获取上级的组织节点
	 * @param subOrgId 开始的组织ID
	 * @param expectLayerType 预期的组织层级类型
	 * @return
	 */
	public Long getParentOrgId(Long subOrgId, String expectLayerType);
}
