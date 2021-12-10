package cn.ffcs.zhsq.event.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.wap.exception.ServiceException;
import cn.ffcs.zhsq.event.service.IIncidentInvolvedService;
import cn.ffcs.zhsq.mybatis.domain.event.IncidentInvolved;
import cn.ffcs.zhsq.mybatis.persistence.event.IncidentInvolvedMapper;
@Service(value="incidentInvolvedServiceImpl")
public class IncidentInvolvedServiceImpl implements IIncidentInvolvedService {
	
	@Autowired
	private IncidentInvolvedMapper incidentInvolvedMapper;

	@Override
	public void insertIncidentInvolvedByOwnSideEventCode(
			IncidentInvolved incidentInvolved) throws ServiceException {
		incidentInvolvedMapper.insert(incidentInvolved);

	}

	@Override
	public IncidentInvolved findIncidentInvolvedById(Long eventId) throws ServiceException{
		IncidentInvolved incidentInvolved = incidentInvolvedMapper.findIncidentInvolvedById(eventId);
		formatData(incidentInvolved);
		return incidentInvolved;
	}
	
	public void formatData(IncidentInvolved incidentInvolved) {
		//是否破案
		if(incidentInvolved != null) {
			if(!StringUtils.isBlank(incidentInvolved.getIsDetection())) {
				incidentInvolved.setIsDetectionName(incidentInvolved.getIsDetection().equals("1")?"是":"否");
			} else {
				incidentInvolved.setIsDetectionName("否");
			}
		}
	}

	@Override
	public void updateInvolvedPeople(IncidentInvolved incidentInvolved) {
		incidentInvolvedMapper.update(incidentInvolved);
		
	}
}
