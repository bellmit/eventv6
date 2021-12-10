package cn.ffcs.zhsq.rent.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.mybatis.domain.event.EventRent;
import cn.ffcs.zhsq.mybatis.persistence.rent.EventRentMapper;
import cn.ffcs.zhsq.rent.service.IEventRentService;
@Service(value="eventRentServiceImpl")
public class EventRentServiceImpl implements IEventRentService{

	@Autowired
	private EventRentMapper eventRentMapper;
	
	@Override
	public EventRent findEventRentByEventId(Long eventId) {
		return eventRentMapper.findEventRentByEventId(eventId);
	}

	@Override
	public Long saveEventRentReturnId(EventRent eventRent) {
		int row = eventRentMapper.insert(eventRent);
		return row>0?eventRent.getErId():-1L;
	}

}
