package cn.ffcs.zhsq.handlerCfg.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.handlerCfg.service.IEventHandlerCfgService;
import cn.ffcs.zhsq.mybatis.domain.handlerCfg.EventHandlerWfActorCfgPO;
import cn.ffcs.zhsq.mybatis.domain.event.EventNodeHandler;
import cn.ffcs.zhsq.mybatis.persistence.handlerCfg.EventHandlerCfgMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 获取人员配置信息
 * @author zhangls
 *
 */
@Service(value="eventHandlerCfgService")
public class EventHandlerCfgServiceImpl implements IEventHandlerCfgService {
	
	@Autowired
	private EventHandlerCfgMapper eventHandlerCfgMapper;

	@Override
	public List<EventNodeHandler> findEventNodeHandlers(Long wfId, String eventType, String nodeCode, String infoOrgCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("wfId", wfId);
		params.put("eventType", eventType);
		params.put("nodeCode", nodeCode);
		params.put("infoOrgCode", infoOrgCode);
		List<EventHandlerWfActorCfgPO> wfActorCfgPOs = this.eventHandlerCfgMapper.findEeventHandlerWfActorCfgPOs(params);
		List<EventNodeHandler> handlers = new ArrayList<EventNodeHandler>();
		for (EventHandlerWfActorCfgPO po : wfActorCfgPOs) {
			EventNodeHandler handler = new EventNodeHandler(po.getTransactorId(), po.getTransactorOrgId());
			handlers.add(handler);
		}
		return handlers;
	}

	@Override
	public List<EventNodeHandler> findEventNodeHandlers(Long wfId, String eventType, String nodeCode,
			String infoOrgCode, int gridLevel) {
		List<EventNodeHandler> handlers = new ArrayList<EventNodeHandler>();
		int l = CommonFunctions.getGridLevel(infoOrgCode);
		if (l > 0 && l >= gridLevel) {
			infoOrgCode = CommonFunctions.getInfoOrgCode(infoOrgCode, gridLevel);
			if (StringUtils.isNotBlank(infoOrgCode)) {
				handlers = this.findEventNodeHandlers(wfId, eventType, nodeCode, infoOrgCode);
			}
		}
		return handlers;
	}
	
}
