package cn.ffcs.zhsq.map.firegrid.arcgis.service.impl;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.base.domain.db.CatalogType;
import cn.ffcs.shequ.base.domain.db.OrgExtraInfo;
import cn.ffcs.shequ.base.domain.db.OrgLocationInfo;
import cn.ffcs.shequ.grid.domain.db.OrgEntityInfo;
import cn.ffcs.zhsq.base.map.dao.IOrgConfigDao;
import cn.ffcs.zhsq.firegrid.map.arcgis.service.IOrgConfigService;
import cn.ffcs.zhsq.utils.ConstantValue;


@Service(value="orgConfigServiceImpl")
public class OrgConfigServiceImpl implements IOrgConfigService{
	@Autowired
	private IOrgConfigDao daoImpl;

	@Override
	public OrgEntityInfo queryEntityByPK(long orgId) {
		return daoImpl.queryEntityByPK(orgId);
	}
	
	public List<CatalogType> getCatalogTypeList() {
		return daoImpl.getCatalogTypeList();
	}
	
	/**根据catalogId及父结点Id，取得对应的子结点*/
	public List<OrgEntityInfo> getOrgEntityInfoList(Long catalogId,Long parentOrgId) {
		List<OrgEntityInfo> orgEntityInfoList = daoImpl.getOrgEntityInfoList(catalogId,parentOrgId);
		for(OrgEntityInfo orgEntityInfo:orgEntityInfoList) {
			List<OrgEntityInfo> subOrgEntityInfoList = daoImpl.getSubOrgEntityInfoList(catalogId,orgEntityInfo.getOrgId());
			
			if(null != subOrgEntityInfoList && subOrgEntityInfoList.size()>0) {
				orgEntityInfo.setLeaf(false);
			
			}else {
				orgEntityInfo.setLeaf(true);
			}
		}
		return orgEntityInfoList;
	}
	
	public OrgEntityInfo getOrgEntityInfo(Long catalogId,String locationCode) {
		return daoImpl.getOrgEntityInfo(catalogId,locationCode);
	}
	
	public OrgEntityInfo getOrgEntityInfo(Long orgId) {
		return daoImpl.getOrgEntityInfo(orgId);
	}
	
	public OrgEntityInfo getOrgEntityInfoByCatalogIdandParentOrgId(int catalogId,Long parentOrgId) {
		return daoImpl.getOrgEntityInfoByCatalogIdandParentOrgId( catalogId, parentOrgId);
	}
	
	public List<OrgEntityInfo> getOrgEntityInfoListByCatalogIdandOrgCode(int catalogId,String orgCode) {
		return daoImpl.getOrgEntityInfoListByCatalogIdandOrgCode( catalogId, orgCode);
	}
	
	public List<OrgEntityInfo> getOrgEntityInfoListByCatalogIdandLocationCode(int catalogId,String locationCode) {
		return daoImpl.getOrgEntityInfoListByCatalogIdandLocationCode( catalogId, locationCode);
	}
	
	public int deleteOrgEntityInfo(Long orgId) {
		return daoImpl.deleteOrgEntityInfo(orgId);
	}
	
	/**根据父结点ID获取最大的orgCode*/
	public Long getMaxOrgCodeByParentOrgId(Long catalogId,Long parentOrgId) {
		return daoImpl.getMaxOrgCodeByParentOrgId( catalogId,parentOrgId);
	}
	
	public boolean batchUpdateOrgLocationInfo(Long orgId,List<OrgLocationInfo> orgLocationInfoList) {
		boolean result = true;
		try {
			//删除旧的
			daoImpl.deleteOrgLocationInfo(orgId);
			//添加新的
			daoImpl.batchUpdateOrgLocationInfo(orgLocationInfoList);	
		}catch(Exception e) {
			result = false;
		}
		return result;
	}
	public List<OrgEntityInfo> getOrgEntityInfoList(Long parentOrgId) {
		List<OrgEntityInfo> orgEntityInfoList = daoImpl.getSubOrgEntityInfoList(parentOrgId);
		
		if(orgEntityInfoList!=null && orgEntityInfoList.size()>0) {
			for(OrgEntityInfo orgEntityInfo:orgEntityInfoList) {
				List<OrgEntityInfo> subOrgEntityInfoList = daoImpl.getSubOrgEntityInfoList(orgEntityInfo.getOrgId());
				if(null != subOrgEntityInfoList && subOrgEntityInfoList.size()>0) {
					//有子结点
					orgEntityInfo.setLeaf(false);
				}else {
					//无子结点
					orgEntityInfo.setLeaf(true);
				}
			}			
		}
		return orgEntityInfoList;
	}
	/**
	 * 根据ORG
	 * 取中心点组织配置
	 * */
	public OrgExtraInfo getOrgExtraInfo(Long orgId) {
		OrgExtraInfo orgExtraInfo = daoImpl.getOrgExtraInfo(orgId);
		if(null != orgExtraInfo) {
			//取围点坐标
			List<OrgLocationInfo> orgLocationInfoList = daoImpl.getOrgLocationInfoList(orgId);			
			orgExtraInfo.setOrglocationinfoList(orgLocationInfoList);
			if(null != orgLocationInfoList && orgLocationInfoList.size()>0) {
				JSONArray array = new JSONArray();
				for(OrgLocationInfo orgLocationInfo:orgLocationInfoList) {
					JSONObject obj = new JSONObject();
					obj.put("orgLocationId", orgLocationInfo.getOrgLocationId());
					obj.put("mapOrder", null != orgLocationInfo.getMapOrder()?orgLocationInfo.getMapOrder():"");
					obj.put("latitude", orgLocationInfo.getLatitude());
					obj.put("longitude", orgLocationInfo.getLongitude());					
					array.add(obj);
				}
				orgExtraInfo.setOrgLocationInfoJSONString(array.toString());
			}
			
			//取子中心点位
			List<OrgExtraInfo> subOrgExtraInfoList = daoImpl.getSubOrgExtraInfoList(orgId);
			orgExtraInfo.setSubOrgExtraInfoList(subOrgExtraInfoList);
			JSONArray subOrgExtraInfoArray = new JSONArray();
			for(OrgExtraInfo subOrgExtraInfo:subOrgExtraInfoList) {
				JSONObject obj = new JSONObject();
				obj.put("orgId", subOrgExtraInfo.getOrgId());
				obj.put("orgName", subOrgExtraInfo.getOrgName());
				obj.put("orgContent", subOrgExtraInfo.getOrgContent());
				obj.put("longitude", subOrgExtraInfo.getLongitude());
				obj.put("latitude", subOrgExtraInfo.getLatitude());
				JSONArray sublocationarray = new JSONArray();
				List<OrgLocationInfo> subOrgLocationInfoList = daoImpl.getOrgLocationInfoList(subOrgExtraInfo.getOrgId());	
				for(OrgLocationInfo subOrgLocationInfo:subOrgLocationInfoList) {
					JSONObject subObj = new JSONObject();
					subObj.put("orgLocationId", subOrgLocationInfo.getOrgLocationId());
					subObj.put("mapOrder", null != subOrgLocationInfo.getMapOrder()?subOrgLocationInfo.getMapOrder():"");
					subObj.put("latitude", subOrgLocationInfo.getLatitude());
					subObj.put("longitude", subOrgLocationInfo.getLongitude());					
					sublocationarray.add(subObj);
				}
				obj.put("subOrgLocationInfo", sublocationarray);
				subOrgExtraInfoArray.add(obj);
			}
			orgExtraInfo.setSubOrgExtraInfoStr(subOrgExtraInfoArray.toString());
		}
		return orgExtraInfo;
	}
	public OrgExtraInfo getOrgExtraInfo(Long orgId,String mapType) {
		OrgExtraInfo orgExtraInfo = daoImpl.getOrgExtraInfo(orgId,mapType);
		if(null != orgExtraInfo) {
			//取围点坐标
			List<OrgLocationInfo> orgLocationInfoList = daoImpl.getOrgLocationInfoList(orgId);			
			orgExtraInfo.setOrglocationinfoList(orgLocationInfoList);
			if(null != orgLocationInfoList && orgLocationInfoList.size()>0) {
				JSONArray array = new JSONArray();
				for(OrgLocationInfo orgLocationInfo:orgLocationInfoList) {
					JSONObject obj = new JSONObject();
					obj.put("orgLocationId", orgLocationInfo.getOrgLocationId());
					obj.put("mapOrder", null != orgLocationInfo.getMapOrder()?orgLocationInfo.getMapOrder():"");
					obj.put("latitude", orgLocationInfo.getLatitude());
					obj.put("longitude", orgLocationInfo.getLongitude());					
					array.add(obj);
				}
				orgExtraInfo.setOrgLocationInfoJSONString(array.toString());
			}
			
			//取子中心点位
			List<OrgExtraInfo> subOrgExtraInfoList = daoImpl.getSubOrgExtraInfoList(orgId,mapType);
			orgExtraInfo.setSubOrgExtraInfoList(subOrgExtraInfoList);
			JSONArray subOrgExtraInfoArray = new JSONArray();
			for(OrgExtraInfo subOrgExtraInfo:subOrgExtraInfoList) {
				JSONObject obj = new JSONObject();
				obj.put("orgId", subOrgExtraInfo.getOrgId());
				obj.put("orgName", subOrgExtraInfo.getOrgName());
				obj.put("orgContent", subOrgExtraInfo.getOrgContent());
				obj.put("longitude", subOrgExtraInfo.getLongitude());
				obj.put("latitude", subOrgExtraInfo.getLatitude());
				JSONArray sublocationarray = new JSONArray();
				List<OrgLocationInfo> subOrgLocationInfoList = daoImpl.getOrgLocationInfoList(subOrgExtraInfo.getOrgId());	
				for(OrgLocationInfo subOrgLocationInfo:subOrgLocationInfoList) {
					JSONObject subObj = new JSONObject();
					subObj.put("orgLocationId", subOrgLocationInfo.getOrgLocationId());
					subObj.put("mapOrder", null != subOrgLocationInfo.getMapOrder()?subOrgLocationInfo.getMapOrder():"");
					subObj.put("latitude", subOrgLocationInfo.getLatitude());
					subObj.put("longitude", subOrgLocationInfo.getLongitude());					
					sublocationarray.add(subObj);
				}
				obj.put("subOrgLocationInfo", sublocationarray);
				subOrgExtraInfoArray.add(obj);
			}
			orgExtraInfo.setSubOrgExtraInfoStr(subOrgExtraInfoArray.toString());
		}
		return orgExtraInfo;
	}
	/**
	 * 根据ORG
	 * 取中心点组织配置
	 * */
	public OrgExtraInfo getOrgExtraInfoNoSub(Long orgId) {
		OrgExtraInfo orgExtraInfo = daoImpl.getOrgExtraInfo(orgId);
		return orgExtraInfo;
	}
	
	/**
	 * 保存更新组织本置
	 * */
	public boolean saveOrUpdate(OrgExtraInfo orgExtraInfo) {
		List<OrgLocationInfo> orgLocationInfoList = orgExtraInfo.getOrglocationinfoList(); 
		boolean flag = false;
		OrgExtraInfo currentOrgExtraInfo = daoImpl.getOrgExtraInfo(orgExtraInfo.getOrgId());
		//删除旧的描点
		daoImpl.deleteOrgLocationInfo(orgExtraInfo.getOrgId());	
		//插入新的描点
		daoImpl.insertOrgLocationInfo(orgLocationInfoList);
		if(currentOrgExtraInfo!=null) {
			//更新
			flag = daoImpl.updateOrgExtraInfo(orgExtraInfo);
		}else {
			//插入			
			orgExtraInfo.setCreateTime(new Date());
			flag = daoImpl.insertOrgExtraInfo(orgExtraInfo);
			
		}
		return flag;
	}
	
	/*public List<CatalogType> getAllCatalogType() {
		return daoImpl.getAllCatalogType();
	}
	
	public CatalogType getCatalogType(long catalogId) {
		return daoImpl.getCatalogType(catalogId);
	}*/
	
	public int getOrgEntityMaxChiefLevel(long catalogId) {
		return daoImpl.getOrgEntityMaxChiefLevel(catalogId);
	}
	/**组织层级类型*/
	public List<JSONObject> getAllOrgLayerTypeList() {
		return daoImpl.getAllOrgLayerTypeList();
	}
	
	@Override
	public boolean updateOrgExtraInfo(OrgExtraInfo gi) {
		return daoImpl.updateOrgExtraInfo(gi);
	}

	@Override
	public boolean insertOrgExtraInfo(OrgExtraInfo gi) {
		gi.setCreateTime(new Date());
		gi.setStatus(ConstantValue.STATUS_DEFAULT);
		return daoImpl.insertOrgExtraInfo(gi);
	}

	@Override
	public Long getParentOrgId(Long subOrgId, String expectLayerType) {
		return daoImpl.getParentOrgId(subOrgId, expectLayerType);
	}
	
}
