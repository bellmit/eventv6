package cn.ffcs.zhsq.mybatis.persistence.map.arcgis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;



public interface NanpingVideoDataMapper {
	
	/**
	 * 2017-11-13 获取网格层级数量
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findGridByParams(Map<String, Object> params);
	
	/**
	 * 2017-11-13 获取全球眼的数量
	 * 
	 * @param params
	 * @return
	 */
	public int findGlobalByParams(Map<String, Object> params);
	
	
	/**
	 * 2017-11-13 获取派出所数量
	 * 
	 * @param params
	 * @return
	 */
	public int findPoliceByParams(Map<String, Object> params);
	
	/**
	 * 2017-11-13 获取消防数量
	 * 
	 * @param params
	 * @return
	 */
	public int findFireByParams(Map<String, Object> params);
	
	/**
	 * 2017-11-13 获取网格员数量
	 * 
	 * @param params
	 * @return
	 */
	public int findHospitalByParams(Map<String, Object> params);
	
	/**
	 * 2017-11-13 获取医院数量
	 * 
	 * @param params
	 * @return
	 */
	public int finGridMemberByParams(Map<String, Object> params);
	
	/**
	 * 2017-11-13 获取事件总数数量
	 * 
	 * @param params
	 * @return
	 */
	public int findSumEventByParams(Map<String, Object> params);
	
	/**
	 * 2017-11-13 获取事件待办数量
	 * 
	 * @param params
	 * @return
	 */
	public int findBacklogByParams(Map<String, Object> params);
	
	/**
	 * 2017-11-13 获取事件办结数量
	 * 
	 * @param params
	 * @return
	 */
	public int findEndByParams(Map<String, Object> params);
	
	/**
	 * 2017-11-13 获取超时事件数量
	 * 
	 * @param params
	 * @return
	 */
	public int findTimeoutByParams(Map<String, Object> params);
	
	/**
	 * 2017-11-13 获取周边网格员事件
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getGridListData(Map<String, Object> params);
	
	/**
	 * 2017-11-13 获取周边全球眼事件
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getGlobalsEyeList(Map<String, Object> params);
	

	/**
	 * 紧急待办事件列表
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findListByUrgencyDegree(Map<String, Object> params);
	/**
	 * 人员所在组织辖区紧急待办
	 * @param param
	 * @param bounds
	 * @return
	 */
	public List<Map<String, Object>> findPagListByUrgencyDegree(Map<String, Object> param, RowBounds bounds);
	
	
	public List<Map<String, Object>> findSocialListByOrgCode(Map<String, Object> params);
	
	

}
