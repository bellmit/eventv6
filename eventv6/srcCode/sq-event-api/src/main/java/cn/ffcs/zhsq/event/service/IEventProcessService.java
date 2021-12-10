package cn.ffcs.zhsq.event.service;

public interface IEventProcessService {
	
	/**
	 * 获取事件的结案人姓名
	 * @param eventId
	 * @return
	 */
	public String findEventCloserByEventId(Long eventId);
}
