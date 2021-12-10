package cn.ffcs.zhsq.survey.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.mybatis.domain.event.EventSurvey;
import cn.ffcs.zhsq.mybatis.persistence.survey.EventSurveyMapper;
import cn.ffcs.zhsq.survey.IEventSurveyService;

@Service(value="eventSurveyServiceImpl")
public class EventSurveyServiceImpl implements IEventSurveyService {

	@Autowired
	private EventSurveyMapper eventSurveyMapper;
	
	@Override
	public Long saveEventSurveyReturnId(EventSurvey eventSurvey) {
		int row = eventSurveyMapper.insert(eventSurvey);
		return row>0?eventSurvey.getSrId():-1L;
	}

	@Override
	public EventSurvey findEventSurveyByEventId(Long eventId) {
		EventSurvey eventSurvey = eventSurveyMapper.findEventSurveyByEventId(eventId);
		formatData(eventSurvey);
		return eventSurvey;
	}
	
	private void formatData(EventSurvey eventSurvey) {
		
		if(eventSurvey!=null){
			//是否整改isRefit
			if(!StringUtils.isBlank(eventSurvey.getIsRefit())){
				eventSurvey.setIsRefitName(eventSurvey.getIsRefit().equals("1")?"是":"否");
			}else{
				eventSurvey.setIsRefitName("否");
			}
			
			//是否有老弱病残孕isElderlyPregnancy
			if(!StringUtils.isBlank(eventSurvey.getIsElderlyPregnancy())){
				eventSurvey.setIsElderlyPregnancyName(eventSurvey.getIsElderlyPregnancy().equals("1")?"有":"无");
			}else{
				eventSurvey.setIsElderlyPregnancyName("无");
			}
		}
		
	}

}
