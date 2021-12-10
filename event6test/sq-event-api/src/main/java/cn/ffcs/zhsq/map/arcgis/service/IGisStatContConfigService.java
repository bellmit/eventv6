package cn.ffcs.zhsq.map.arcgis.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.map.arcgis.GisStatContConfig;

/**
 * 框选可配置内容
 * 
 * @author huangmw
 * 
 */
public interface IGisStatContConfigService {

	/**
	 * 获取所有大类下的小类
	 * 
	 * @param statCfgId
	 *            关联统计配置ID
	 * @param objClass
	 *            所属类别
	 * @return 如党员，网管员，消防栓等类别
	 */
	public List<GisStatContConfig> getGisStatContConfigs(long statCfgId, String objClass) throws Exception;

	/**
	 * 获取所有框选配置没有大类的小类
	 * 
	 * @param statCfgId
	 *            关联统计配置ID
	 * @return 如党员，网管员，消防栓等类别
	 */
	public List<GisStatContConfig> getNoObjClassGisStatContConfigs(long statCfgId) throws Exception;

	/**
	 * <p>
	 * 保存框选周边统计内容信息
	 * </p>
	 * 
	 * @param gisStatContConfig
	 * @return
	 */
	public Long saveGisStatContConfig(GisStatContConfig gisStatContConfig) throws Exception;

	/**
	 * <p>
	 * 更新框选周边统计内容信息
	 * </p>
	 * 
	 * @param gisStatContConfig
	 * @return
	 */
	public boolean updateGisStatContConfig(GisStatContConfig gisStatContConfig) throws Exception;

	/**
	 * <p>
	 * 删除框选周边统计内容信息
	 * </p>
	 * 
	 * @param gisStatContConfig
	 * @return
	 */
	public boolean deleteGisStatContConfig(Map<String, Object> params) throws Exception;

	/**
	 * <p>
	 * 根据statCfgId删除框选周边统计内容信息
	 * </p>
	 * 
	 * @param statCfgId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGisStatContConfigs(long statCfgId) throws Exception;
}
