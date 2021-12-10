package cn.ffcs.zhsq.mybatis.persistence.survey;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.event.EventSurvey;

public interface EventSurveyMapper extends MyBatisBaseMapper<EventSurvey>{

	public EventSurvey findEventSurveyByEventId(Long eventId);
	
}
