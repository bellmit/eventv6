package cn.ffcs.zhsq.survey;

import cn.ffcs.zhsq.mybatis.domain.event.EventSurvey;


public interface IEventSurveyService {

	public EventSurvey findEventSurveyByEventId(Long eventId);
	
	public Long saveEventSurveyReturnId(EventSurvey eventSurvey);
	
}
