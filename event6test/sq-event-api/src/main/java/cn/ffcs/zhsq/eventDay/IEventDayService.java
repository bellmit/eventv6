package cn.ffcs.zhsq.eventDay;

import java.util.Map;

import cn.ffcs.system.publicUtil.EUDGPagination;

public interface IEventDayService {
	/*
	 *  "盐都事件列表jsonp
	 *   params 
	 *   	page   页数
	 *   	rows   每页的记录数
	 * 		orgCode 组织编码
	 * 
	 *   return 
	 *   	eventId  事件id
	 *   	orgCode  组织编码
	 *   	createTime 创建时间
	 *   	createTimeStr
	 *   	eventName 事件标题
	 * 		type 
	 * */	
	public EUDGPagination findEventDayPagination(int page,
			int rows, Map<String, Object> params);
	
	/*
	 *  "盐城任务提醒事件jsonp
	 *   params 
	 *   	page   页数
	 *   	rows   每页的记录数
	 * 		orgCode 组织编码
	 * 
	 *   return 
	 *   	T.EVENT_ID "eventId",  事件id
  	 *		T.EVENT_NAME "eventName", 事件名称
  	 *		T.CONTENT_ "content", 事件内容
	 *		T.INSTANCEID "instanceId", 实例id
	 *		T.WORKFLOWID "workflowId", 
	 *		T.INFOORGCODE "infoOrgCode" 
	 * */	
	public EUDGPagination findEventPagination(int page,
			int rows, Map<String, Object> params);
}
