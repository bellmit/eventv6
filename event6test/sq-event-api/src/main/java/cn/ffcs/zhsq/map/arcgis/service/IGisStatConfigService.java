package cn.ffcs.zhsq.map.arcgis.service;

import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.GisStatConfig;

/**
 * 框选可配置
 * 
 * @author huangmw
 * 
 */
public interface IGisStatConfigService {

	/**
	 * <p>
	 * 保存框选可配置信息
	 * </p>
	 * 
	 * @param gisStatConfig
	 * @return
	 */
	public Long saveGisStatConfig(GisStatConfig gisStatConfig) throws Exception;

	/**
	 * <p>
	 * 更新框选可配置信息
	 * </p>
	 * 
	 * @param gisStatConfig
	 * @return
	 */
	public boolean updateGisStatConfig(GisStatConfig gisStatConfig) throws Exception;

	/**
	 * <p>
	 * 删除框选可配置信息
	 * </p>
	 * 
	 * @param gisStatConfig
	 * @return
	 */
	public boolean deleteGisStatConfig(GisStatConfig gisStatConfig) throws Exception;

	/**
	 * <p>
	 * 获取框选可配置
	 * <p>
	 * 
	 * @param statType
	 *            统计方式，0：框选；1：周边资源检索
	 * @param bizType
	 *            业务编码，保持与地图首页类型一致
	 * @param regionCode
	 *            地域编码
	 * @return 框选可配置信息
	 */
	public GisStatConfig getGisStatConfig(String statType, String bizType, String regionCode) throws Exception;

	/**
	 * <p>
	 * 获取框选可配置分类
	 * </p>
	 * 
	 * @param statCfgId
	 * @return 统计分类，不为空如：人,地,事,物,情（英文逗号分隔），则子类进行分类显示，如果为空，则子类不进行分类显示
	 */
	public String getCatagories(long statCfgId) throws Exception;

	/**
	 * <p>
	 * 获取框选或者周边可配置信息
	 * </p>
	 * 
	 * @param statCfgId
	 *            主键
	 * @return 框选或者周边可配置信息
	 */
	public GisStatConfig getGisStatConfigById(long statCfgId) throws Exception;

	/**
	 * <p>
	 * 框选周边可配置分页
	 * </p>
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 *            statType:0框选;1周边
	 * @return
	 */
	public EUDGPagination findGisStatConfigsPagination(int pageNo, int pageSize, Map<String, Object> params)
			throws Exception;

	/**
	 * <p>
	 * 判断同一个地域下是否存在相同的框选周边配置
	 * </p>
	 * 
	 * @param params
	 *            statType:统计方式，0：框选；1：周边资源检索 regionCode:地域编码
	 *            bizType:业务编码，保持与地图首页类型一致
	 * @return
	 * @throws Exception
	 */
	public int repeatCountForBizType(Map<String, Object> params) throws Exception;
}
