package cn.ffcs.zhsq.mybatis.persistence.rent;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.event.EventRent;

public interface EventRentMapper extends MyBatisBaseMapper<EventRent>{
	
	public EventRent findEventRentByEventId(Long eventId);

}
