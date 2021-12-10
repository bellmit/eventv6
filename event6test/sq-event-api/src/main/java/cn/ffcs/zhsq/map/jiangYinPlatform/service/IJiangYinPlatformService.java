package cn.ffcs.zhsq.map.jiangYinPlatform.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.communicationLog.CommunicationLog;
import cn.ffcs.zhsq.mybatis.domain.jiangyinPlatform.recentContact.RecentContact;

/**
 * 	江阴大屏地图接口服务
 *
 * @Author sunjian
 * @Date 2020-03-04 21:29
 */
public interface IJiangYinPlatformService {
    /**
     *	 获取网格分页信息
     * @param pageNo
     * @param pageSize
     * @param params
     * @return
     */
    public EUDGPagination findGridInfoPagination(int pageNo, int pageSize, Map<String, Object> params);
    
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
     *	 区域排名TOP5
     * @param map
     * 
     * 
     * @return
     */
    public List<Map<String,Object>> findAreaRankTop5Data(Map<String, Object> params);
    
    /**
     *	 累计采集，累计办结，满意率
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
     *	 查找督办的事件数量
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
    public List<Map<String, Object>> findEventXY(Map<String,Object> map);
    
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
	
	/*******************************end3**************************************/
	
	
	/******************************start4*************************************/
	
	/*******************************end4**************************************/
	
	
	/******************************start5*************************************/
	
    public boolean saveLogAndRecentContact(RecentContact reCon, CommunicationLog comLog);
	/*******************************end5**************************************/

	List<Map<String,Object>> findInfoOrgCodesByOrgIds(Map<String, Object> params);
    
}
