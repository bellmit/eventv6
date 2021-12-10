package cn.ffcs.zhsq.event.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.event.service.IEventRelaService;
import cn.ffcs.zhsq.mybatis.domain.event.EventRela;
import cn.ffcs.zhsq.mybatis.persistence.event.EventRelaMapper;

@Service(value = "eventRelaServiceImpl")
public class EventRelaServiceImpl implements IEventRelaService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EventRelaMapper eventRelaMapper;
	
	@Override
	public EUDGPagination findPatrolPagination(int pageNo, int pageSize,
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertEventRela(EventRela eventRela) {
		int record = eventRelaMapper.insert(eventRela);
		return record;
	}
}
