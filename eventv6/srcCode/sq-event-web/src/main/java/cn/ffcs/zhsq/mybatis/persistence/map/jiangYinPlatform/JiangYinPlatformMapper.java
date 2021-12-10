package cn.ffcs.zhsq.mybatis.persistence.map.jiangYinPlatform;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author sunjian
 *
 */
public interface JiangYinPlatformMapper {

	/******************************吴展杰 start*************************************/
	
	/**
  
     *	 采集趋势
     * @param map
     * 
     * 
     * @return
     */
    public List<Map<String,Object>> findAcquisitionTrendData(Map<String, Object> params);
    /**
  
     *	 办结趋势
     * @param map
     * 
     * 
     * @return
     */
    public List<Map<String,Object>> findAcquisitionTrendTotalAndCloseData(Map<String, Object> params);
	
	
    /**
     *	 区域排名TOP5
     * @param map
     * 
     * 
     * @return
     */
    public List<Map<String,Object>> findAreaRankTop5Data(Map<String, Object> params);
    
    /**
     *	 
     * @param map
     * 
     * 
     * @return
     */
    public Map<String,Object> findCumulativeData(Map<String, Object> params);
    
    /**
     *	 热点事件
     * @param map
     * 
     * 
     * @return
     */
    public List<Map<String,Object>> findHotEventData(Map<String, Object> params);
    
    
    /**
     *	 查找紧急程度事件数量
     *
     * @param map
     * 
     * 
     * @return
     */
    public List<Map<String,Object>> findUrgencyNum(Map<String, Object> params);
    
    /**
     *	 查找督办事件数量
     *
     * @param map
     * 
     * 
     * @return
     */
    public List<Map<String,Object>> findOverseeNum(Map<String, Object> params);
    
    /**
     *	 查找事件的经纬度
     *
     * @param map
     * 
     * 
     * @return
     */
    public List<Map<String, Object>> findEventXY(Map<String, Object> map);
    
    /******************************吴展杰 end**************************************/
	
	
	/******************************start2*************************************/
    /**
     *	根据事件类型 获取事件分组数量
     * @param map
     * @return
     */
    public List<Map<String, Object>> getEventNumGroupByType(Map<String, Object> map);


    /*******************************end2**************************************/
	
	
	/******************************start3*************************************/
    /**
     * 根据组织orgId返回地域编码
     * @param params
     * @return
     */
    List<Map<String, Object>> findInfoOrgCodesByOrgIds(Map<String, Object> params);
	/*******************************end3**************************************/
	
	
	/******************************start4*************************************/
	
	/*******************************end4**************************************/
	
	
	/******************************start5*************************************/
	
	/*******************************end5**************************************/
	
	
}
