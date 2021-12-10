package cn.ffcs.zhsq.rent.service;

import cn.ffcs.zhsq.mybatis.domain.event.EventRent;


public interface IEventRentService {

	public Long saveEventRentReturnId(EventRent eventRent);
	
	public EventRent findEventRentByEventId(Long eventId);
	
}
