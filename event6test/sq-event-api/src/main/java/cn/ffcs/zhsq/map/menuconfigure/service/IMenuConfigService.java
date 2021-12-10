package cn.ffcs.zhsq.map.menuconfigure.service;

import java.util.List;
import java.util.Map;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisOrgAcc;

/**
 * 2014-10-09 liushi add 地图菜单配置服务接口
 * 
 * @author Administrator
 * 
 */
public interface IMenuConfigService {
	/**
	 * 2014-10-09 liushi add 根据pid查询其子节点
	 * 
	 * @param gdcPid
	 * @return
	 */
	public List<GisDataCfg> getGisDataCfgListByPid(Long gdcPid);
	
	public List<GisDataCfg> getAllGisDataCfgsByPid(Long gdcPid);

	public List<GisDataCfg> getAllGisDataCfgListByPid(Long gdcPid);
	
	/**
	 * 根据pid与关键字查询
	 */
	public List<GisDataCfg> getGisDataCfgListByPidAndKeywords(Long gdcPid, String keywords);

	/**
	 * 2014-10-11 liushi add 根据pid查询子节点的数量
	 * 
	 * @param gdcPid
	 * @return
	 */
	public Integer getGisDataCfgCountByPid(Long gdcPid);

	/**
	 * 2014-10-10 liushi add 根据id查询图层菜单信息
	 * 
	 * @param gdcId
	 * @return
	 */
	public GisDataCfg getGisDataCfgById(Long gdcId);

	/**
	 * 根据gdcId获取菜单根节点
	 * 
	 * @param gdcId
	 * @return
	 */
	public GisDataCfg getRootGisDataCfgById(Long gdcId);

	/**
	 * 2014-10-10 liushi add 对图层菜单进行插入操作
	 * 
	 * @param gisDataCfg
	 * @return
	 */
	public int insertGisDataCfg(GisDataCfg gisDataCfg);

	/**
	 * 同一节点下菜单名称是否唯一
	 * 
	 * @param params
	 * @return
	 */
	public int checkMenuName(Map<String, Object> params);

	/**
	 * 2014-10-10 liushi add 对图层菜单进行修改操作
	 * 
	 * @param gisDataCfg
	 * @return
	 */
	public int updateGisDataCfg(GisDataCfg gisDataCfg);

	/**
	 * 2014-10-11 liushi add 根据id删除对应的图层菜单
	 * 
	 * @param gdcId
	 * @return
	 */
	public int deleteGisDataCfg(Long gdcId);

	/**
	 * 2014-10-10 liushi add 保存图层菜单与infoOrgCode的关系
	 * 
	 * @param gdcIdsStr
	 * @param org
	 * @return
	 */
	public Boolean saveGisOrgAcc(String gdcIdsStr, String orgCode);

	/**
	 * 2015-03-31 liush add 保存图层菜单与infoOrgCode的关系（还带有首页类型）
	 * 
	 * @param gdcIdsStr
	 * @param orgCode
	 * @return
	 */
	public Boolean saveGisOrgAccVersionNoe(String gdcIdsStr, String orgCode, String homePageType);

	/**
	 * 2014-10-13 liushi add 根据机构编码orgCode获取配置的id信息
	 * 
	 * @param orgCode
	 * @return
	 */
	public List<GisOrgAcc> getGisOrgAccListByOrgCode(String orgCode);

	/**
	 * 2015-03-30 liushi add 根据机构编码 orgCode和homePageType 获取配置的id信息
	 * 
	 * @param orgCode
	 * @return
	 */
	public List<GisOrgAcc> getGisOrgAccListByOrgCodeVersionNoe(String orgCode, String homePageType);

	/**
	 * 2014-10-17 liushi add 查询机构关联树用于页面首页提取菜单
	 * 
	 * @param orgCode
	 * @param gdcId
	 * @return
	 */
	public GisDataCfg getGisDataCfgRelationTree(String orgCode, Long gdcId);

	/**
	 * liushi add 根据组织、首页类型、父节点id查询图层菜单
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @param gdcId
	 * @return
	 */
	public GisDataCfg getGisDataCfgRelationTreeVersionNoe(String orgCode, String homePageType, Long gdcId);
	
	public GisDataCfg getGisDataCfgsByPid(Long gdcPid);

	/**
	 * liushi add 2015-10-19 查询一级图层数据
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @param gdcId
	 * @return
	 */
	public GisDataCfg getGisDataCfgRelationTreeVersionTwo(String orgCode, String homePageType, Long gdcId,
			Integer isRootSearch);

	/**
	 * LIUSHI ADD 2015-12-3根据服务名称查询框选的图层配置参数
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @param serviceName
	 * @return
	 */
	public GisDataCfg getGisDataCfgByKXServiceName(String orgCode, String homePageType, String serviceName);

	/**
	 * LIUSHI ADD 2015-12-3根据服务名称查询周边的图层配置参数
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @param serviceName
	 * @return
	 */
	public GisDataCfg getGisDataCfgByZBServiceName(String orgCode, String homePageType, String serviceName);

	/**
	 * 2015-06-05 liushi add 根据组织、首页类型、父节点id查询图层名称
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @param gdcId
	 * @return
	 */
	public String getGisDataLayerNameVersionNoe(String orgCode, String homePageType, Long gdcId);
	
	/**
	 * 根据组织，首页类型查询首页显示风格
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @return displayStyle 显示风格，0：平铺；1：树型
	 */
	public String getDisplayStyle(String orgCode, String homePageType);
	
	/**
	 * 根据组织，首页类型查询配置的图层菜单
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @return
	 */
	public List<GisDataCfg> getGisDataCfgTree(String orgCode, String homePageType);
	
	/**
	 * 排序
	 * 
	 * @param ids
	 * @return
	 */
	public boolean saveSort(List<String> ids);
	
	public GisDataCfg getGisDataCfgByCode(String menuCode, String orgCode);
	
	/**
	 * 2019-09-26 CaiJP add 获取天梯图初始化数据
	 * 
	 * @param infoOrgCode
	 * @param map
	 * @return
	 */
	public EUDGPagination findLadderDiagrmData(Map<String, Object> map);
	
	/**
	 * 2019-09-26 CaiJP add 保存天梯图初始化数据
	 * 
	 * @param map
	 * @return
	 */
	public Boolean saveLadderDiagrm(Map<String, Object> map);
	
	/**
	 * 2019-09-26 CaiJP add 删除天梯图初始化数据
	 * 
	 * @param id 业务标识
	 * @return
	 */
	public int deleteLadderDiagrm(Long id);

}
