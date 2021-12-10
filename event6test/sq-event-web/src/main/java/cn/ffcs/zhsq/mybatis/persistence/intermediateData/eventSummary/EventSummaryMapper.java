package cn.ffcs.zhsq.mybatis.persistence.intermediateData.eventSummary;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;

/**
 * @Description: 事件汇总模块
 * @Author: wuxq
 * @Date: 10-17 11:05:05
 * @Copyright: 2019 福富软件
 */
public interface EventSummaryMapper {
	/**
	 * 新增事件汇总
	 * @param eventSummary
	 * @return 新增成功返回成功条数，否则返回-1
	 */
	public Long insert(Map<String, Object> eventSummary);


	/**
	 * 依据来源平台标识及事件Id，删除事件汇总
	 * @param id	事件id
	 * @param sourcePlatform 来源平台
	 * @return 删除成功返回true，否则返回false
	 */
	public boolean delete(Map<String, Object> eventSummary);
	
	/**
	 * 依据来源平台标识及事件Id，查询事件汇总
	 * @param id	事件id
	 * @param sourcePlatform 来源平台
	 * @return 查询到返回true，否则返回false
	 */
	public int findEventSummaryById(Map<String, Object> eventSummary);
}