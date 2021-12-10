package cn.ffcs.zhsq.event.service;

import cn.ffcs.shequ.wap.exception.ServiceException;
import cn.ffcs.zhsq.mybatis.domain.event.IncidentInvolved;

public interface IIncidentInvolvedService {
	
	public void insertIncidentInvolvedByOwnSideEventCode(IncidentInvolved incidentInvolved)throws ServiceException;

	public IncidentInvolved findIncidentInvolvedById(Long eventId);

	public void updateInvolvedPeople(IncidentInvolved incidentInvolved);

}
