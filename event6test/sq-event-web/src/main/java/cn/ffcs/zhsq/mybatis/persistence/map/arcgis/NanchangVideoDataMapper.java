package cn.ffcs.zhsq.mybatis.persistence.map.arcgis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.shequ.mybatis.domain.zzgl.globalEyes.MonitorBO;

public interface NanchangVideoDataMapper {

	/**
	 *  获取监控点列表
	 * 
	 * @param params
	 * @return
	 */
	public List<MonitorBO> findCamlistByParams(Map<String, Object> params, RowBounds rowBounds);
	
	/**
	 * 获取监控点的数量
	 * 
	 * @param params
	 * @return
	 */
	public int countCamlistByParams(Map<String, Object> params);
	
	/**
	 * 获取监控点坐标信息
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findCamPointlistByParams(Map<String, Object> params);
	
	/**
	 * 获取监控点坐标数量
	 * 
	 * @param params
	 * @return
	 */
	public int countCamPointlistByParams(Map<String, Object> params);
}
