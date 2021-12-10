package cn.ffcs.zhsq.mybatis.persistence.map.menuconfigure;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisOrgAcc;

/**
 * 2014-05-22 liushi add 地图配置信息
 * 
 * @author Administrator
 * 
 */
public interface MenuConfigMapper {
	/**
	 * 2014-10-10 liushi add 根据父节点id查询子节点列表
	 * 
	 * @param gdcPid
	 * @return
	 */
	public List<GisDataCfg> getGisDataCfgListByPid(@Param(value = "gdcPid") Long gdcPid);
	
	/**
	 * 根据gdcId获取菜单根节点
	 * 
	 * @param gdcPid
	 * @return
	 */
	public GisDataCfg getRootGisDataCfgById(@Param(value = "gdcPid") Long gdcPid);
	
	public List<GisDataCfg> getAllGisDataCfgsByPid(@Param(value = "gdcPid") Long gdcPid);

	public List<GisDataCfg> getAllGisDataCfgListByPid(@Param(value = "gdcPid") Long gdcPid);

	/**
	 * 根据关键字查询
	 * 
	 * @param gdcPid
	 * @param keywords
	 *            菜单名称、菜单显示名称、菜单编码
	 * 
	 * @return
	 */
	public List<GisDataCfg> getGisDataCfgListByPidAndKeywords(@Param(value = "gdcPid") Long gdcPid,
			@Param(value = "keywords") String keywords);

	/**
	 * 2014-10-11 liushi add 根据pid查询子节点的数量
	 * 
	 * @param gdcPid
	 * @return
	 */
	public Integer getGisDataCfgCountByPid(@Param(value = "gdcPid") Long gdcPid);

	/**
	 * 2014-10-10 liushi add 根据id查询图层菜单信息
	 * 
	 * @param gdcId
	 * @return
	 */
	public GisDataCfg getGisDataCfgById(@Param(value = "gdcId") Long gdcId);

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
	public int deleteGisDataCfg(@Param(value = "gdcId") Long gdcId);

	/**
	 * 2014-10-10 liushi add 插入图层菜单与infoOrgCode的关系
	 * 
	 * @param gdcIdsStr
	 * @param org
	 * @return
	 */
	public int insertGisOrgAcc(@Param(value = "gdcId") Long gdcId, @Param(value = "orgCode") String orgCode);

	public int insertGisOrgAccVersionNoe(@Param(value = "gdcId") Long gdcId, @Param(value = "orgCode") String orgCode,
			@Param(value = "homePageType") String homePageType);

	/**
	 * 2014-10-10 liushi add 删除图层菜单与infoOrgCode的关系
	 * 
	 * @param gdcId
	 * @param orgCode
	 * @return
	 */
	public int deleteGisOrgAcc(@Param(value = "gdcId") Long gdcId, @Param(value = "orgCode") String orgCode);

	public int deleteGisOrgAccVersionNoe(@Param(value = "gdcId") Long gdcId, @Param(value = "orgCode") String orgCode,
			@Param(value = "homePageType") String homePageType);

	/**
	 * 2014-10-10 liushi add 查询图层菜单机构编码关联关系列表
	 * 
	 * @param orgCode
	 * @return
	 */
	public List<GisOrgAcc> getGisOrgAccList(@Param(value = "orgCode") String orgCode);

	/**
	 * 2015-03-31 liushi add 查询图层菜单组织编码地图首页类型关联关系表
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @return
	 */
	public List<GisOrgAcc> getGisOrgAccListVersionNoe(@Param(value = "orgCode") String orgCode,
			@Param(value = "homePageType") String homePageType);

	/**
	 * 2014-10-17 liushi add 根据orgCode跟gdcPid查询父节点下属的被选中节点列表
	 * 
	 * @param orgCode
	 * @param gdcPid
	 * @return
	 */
	public List<GisOrgAcc> getGisOrgAccListByOrgCodeAndGdcPid(@Param(value = "orgCode") String orgCode,
			@Param(value = "gdcPid") Long gdcPid);

	/**
	 * 2014-10-10 liushi add 查询与当前网格距离最近的上级（或本级）关联的根节点，确定当前使用的网格orgCode以及根节点
	 * 
	 * @param gdcPid
	 * @return
	 */
	public List<GisDataCfg> getGisDataCfgRelationRootList(@Param(value = "orgCode") String orgCode,
			@Param(value = "gdcId") Long gdcId);

	public List<GisDataCfg> getGisDataCfgRelationRootListVersionNoe(@Param(value = "orgCode") String orgCode,
			@Param(value = "homePageType") String homePageType, @Param(value = "gdcId") Long gdcId);

	/**
	 * 2014-10-10 liushi add 根据父节点id和关联orgCode查询子节点被选中的列表
	 * 
	 * @param gdcPid
	 * @return
	 */
	public List<GisDataCfg> getGisDataCfgRelationListByPid(@Param(value = "orgCode") String orgCode,
			@Param(value = "gdcPid") Long gdcPid);

	/**
	 * liushi add 根据组织、首页类型、父节点id查询图层菜单
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @param gdcId
	 * @return
	 */
	public List<GisDataCfg> getGisDataCfgRelationListByPidVersionNoe(@Param(value = "orgCode") String orgCode,
			@Param(value = "homePageType") String homePageType, @Param(value = "gdcPid") Long gdcPid);

	/**
	 * 2015-06-05 liushi add 根据组织、首页类型、父节点id查询图层名称
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @param gdcId
	 * @return
	 */
	public List<GisDataCfg> getGisDataLayerNameVersionNoe(@Param(value = "orgCode") String orgCode,
			@Param(value = "homePageType") String homePageType, @Param(value = "gdcPid") Long gdcPid);

	/**
	 * LIUSHI ADD 2015-12-3根据服务名称查询框选的图层配置参数
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @param serviceName
	 * @return
	 */
	public List<GisDataCfg> getGisDataCfgByKXServiceName(@Param(value = "orgCode") String orgCode,
			@Param(value = "homePageType") String homePageType, @Param(value = "serviceName") String serviceName);

	/**
	 * LIUSHI ADD 2015-12-3根据服务名称查询周边的图层配置参数
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @param serviceName
	 * @return
	 */
	public List<GisDataCfg> getGisDataCfgByZBServiceName(@Param(value = "orgCode") String orgCode,
			@Param(value = "homePageType") String homePageType, @Param(value = "serviceName") String serviceName);

	/**
	 * YangCQ add 根据组织、首页类型查询图层ROOT菜单
	 * @param orgCode
	 * @param homePageType
	 */
	public List<GisDataCfg> getNewGisDataCfgListForRoot(@Param(value = "orgCode") String orgCode,
			@Param(value = "homePageType") String homePageType);

	/**
	 * YangCQ add 根据组织、首页类型、父节点id查询图层菜单
	 * @param orgCode
	 * @param homePageType
	 * @param gdcId
	 * @return
	 */
	public List<GisDataCfg> getNewGisDataCfgListByPid(@Param(value = "orgCode") String orgCode,
			@Param(value = "homePageType") String homePageType, @Param(value = "gdcPid") Long gdcPid);

	/**
	 * 根据组织，首页类型查询首页显示风格
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @return displayStyle 显示风格，0：平铺；1：树型
	 */
	public String getDisplayStyle(@Param(value = "orgCode") String orgCode,
			@Param(value = "homePageType") String homePageType);
	
	/**
	 * 根据组织，首页类型查询图层配置菜单
	 * 
	 * @param orgCode
	 * @param homePageType
	 * @return List<GisDataCfg> 
	 */
	public List<GisDataCfg> getGisDataCfgTree(@Param(value = "orgCode") String orgCode,
			@Param(value = "homePageType") String homePageType);
	
	public GisDataCfg getGisDataCfgByCode(@Param(value = "menuCode") String menuCode);
	
	/**
	 * 2019-09-26 CaiJP add 获取天梯图初始化数据
	 * 
	 * @param infoOrgCode
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getLadderDiagrmData(Map<String, Object> map);
	
	/**
	 * 2019-09-26 CaiJP add 保存天梯图初始化数据
	 * 初始化ARCGIS_CONFIG_INFO表信息
	 * @param map
	 * @return
	 */
	public boolean saveLadderDiagrmAcoi(Map<String, Object> map);
	
	/**
	 * 初始化ARCGIS_SERVICE_INFO表信息
	 */
	public boolean saveLadderDiagrmAsei(Map<String, Object> map);
	
	/**
	 * 初始化ARCGIS_SCALE_INFO表信息
	 */
	public boolean saveLadderDiagrmAsci();
	
	
	/**
	 * 2019-09-26 CaiJP add 删除天梯图初始化数据
	 * 删除ARCGIS_CONFIG_INFO表信息
	 * @param id 业务标识
	 * @return
	 */
	public int deleteLadderDiagrmAcoi(Long id);
	
	/**
	 * 删除ARCGIS_SERVICE_INFO表信息
	 */
	public int deleteLadderDiagrmAsei(Long id);
	
	/**
	 * 删除ARCGIS_SCALE_INFO表信息
	 */
	public int deleteLadderDiagrmAAsci(Long id);
}
