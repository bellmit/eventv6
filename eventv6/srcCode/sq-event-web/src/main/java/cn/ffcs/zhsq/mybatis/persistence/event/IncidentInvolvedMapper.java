package cn.ffcs.zhsq.mybatis.persistence.event;

import cn.ffcs.zhsq.base.dao.MyBatisBaseMapper;
import cn.ffcs.zhsq.mybatis.domain.event.IncidentInvolved;

/**
 * 事件.涉案线路事件接口
 *
 */
public interface IncidentInvolvedMapper extends MyBatisBaseMapper<IncidentInvolved>{

	public IncidentInvolved findIncidentInvolvedById(Long eventId);
	
	//public int insert(IncidentInvolved incidentInvolved);
}
