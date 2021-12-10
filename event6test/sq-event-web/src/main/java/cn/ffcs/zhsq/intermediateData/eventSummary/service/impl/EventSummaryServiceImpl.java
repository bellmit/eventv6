package cn.ffcs.zhsq.intermediateData.eventSummary.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.intermediateData.eventSummary.service.IEventSummaryService;
import cn.ffcs.zhsq.mybatis.persistence.intermediateData.eventSummary.EventSummaryMapper;

@Service("eventSummaryService")
public class EventSummaryServiceImpl implements IEventSummaryService{
	
	@Autowired
	private EventSummaryMapper eventSummaryMapper;

	@Override
	public Long saveEventSummary(Map<String, Object> eventSummary) {
		return eventSummaryMapper.insert(eventSummary);
	}

	@Override
	public boolean deleteEventSummaryById(Map<String, Object> eventSummary) {
		return eventSummaryMapper.delete(eventSummary);
	}

	@Override
	public int findEventSummaryById(Map<String, Object> eventSummary) {
		return eventSummaryMapper.findEventSummaryById(eventSummary);
	}

}
