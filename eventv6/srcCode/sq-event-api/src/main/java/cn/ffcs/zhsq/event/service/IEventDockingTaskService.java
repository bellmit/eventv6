package cn.ffcs.zhsq.event.service;

import java.util.List;
import java.util.Map;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;

public interface IEventDockingTaskService {

	public void doDocking();

	public List<Map<String,Object>> findDataExchangeEvent(Map<String, Object> params);

	public Long report(EventDisposal event);

}
