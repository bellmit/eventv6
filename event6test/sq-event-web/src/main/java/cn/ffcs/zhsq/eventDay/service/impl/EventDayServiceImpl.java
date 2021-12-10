package cn.ffcs.zhsq.eventDay.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.eventDay.IEventDayService;
import cn.ffcs.zhsq.mybatis.persistence.eventDay.EventDayMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.DateUtils;

@Service(value = "eventDayServiceImpl")
public class EventDayServiceImpl implements IEventDayService{
	
	@Autowired
	private EventDayMapper eventDayMapper;

	@Override
	public EUDGPagination findEventDayPagination(int page, int rows, Map<String, Object> params){
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<Map<String, Object>> eventList = new ArrayList<>();
		int count = eventDayMapper.findCountEventDayPagination(params);
		if(count > 0) {
			eventList = eventDayMapper.findListEventDayPagination(params, rowBounds);
			for(Map<String, Object> eventMap : eventList) {
				if(CommonFunctions.isNotBlank(eventMap, "createTime")) {
					eventMap.put("createTimeStr", DateUtils.formatDate((Date)eventMap.get("createTime"), DateUtils.PATTERN_DATE));
				}
			}
		}
		
		return new EUDGPagination(count,eventList);
	}
	
	
	@Override
	public EUDGPagination findEventPagination(int page, int rows, Map<String, Object> params){
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<Map<String, Object>> eventList = new ArrayList<>();
		int count = eventDayMapper.findEventPaginationCount(params);
		if(count > 0) {
			eventList = eventDayMapper.findEventPaginationList(params, rowBounds);
		}
		return new EUDGPagination(count,eventList);
	}

}
