package cn.ffcs.zhsq.mybatis.persistence.crowd;

import java.util.Map;

import cn.ffcs.zhsq.mybatis.domain.crowd.EventVisitRela;


public interface EventVisitRelaMapper {
	
	public int visitRelaInsert(Map<String,Object> params);
	
	public EventVisitRela findById(Long eventId);
}
