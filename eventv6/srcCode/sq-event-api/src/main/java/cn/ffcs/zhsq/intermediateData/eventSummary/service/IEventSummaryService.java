package cn.ffcs.zhsq.intermediateData.eventSummary.service;

import java.util.List;
import java.util.Map;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;

public interface IEventSummaryService {
	/**
	 * 新增事件汇总
	 * @param eventSummary
	 * @return 新增成功返回成功条数，否则返回-1
	 */
	public Long saveEventSummary(Map<String, Object> eventSummary);


	/**
	 * 依据来源平台标识及事件Id，删除事件汇总
	 * @param id	事件id
	 * @param sourcePlatform 来源平台
	 * @return 删除成功返回true，否则返回false
	 */
	public boolean deleteEventSummaryById(Map<String, Object> eventSummary);

	/**
	 * 依据来源平台标识及事件Id，查询事件汇总
	 * @param id	事件id
	 * @param sourcePlatform 来源平台
	 * @return 查询到返回1，否则返回false
	 */
	public int findEventSummaryById(Map<String, Object> eventSummary);
	
}
