package cn.ffcs.zhsq.eventExpand.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;

@Service(value = "eventDisposalExpandForNCServiceImpl")
public class EventDisposalExpandForNCServiceImpl extends EventDisposalExpandBaseServiceImpl {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//事件扩展信息
	@Autowired
	private IEventExtendService eventExtendService;

	@Override
	public boolean updateEventDisposal(EventDisposal event, Map<String, Object> params,UserInfo userInfo) {

		//更新事件额外信息-巡防类型，点位信息
		Map<String,Object> eventExtend=new HashMap<String,Object>();
		eventExtend.put("patrolType", params.get("patrolType"));
		eventExtend.put("pointSelection", params.get("pointSelection"));
		if(CommonFunctions.isNotBlank(params, "isNotice")) {
			eventExtend.put("isNotice", params.get("isNotice"));
		}
		
		try {
			Map<String, Object> findEventExtendByEventId = eventExtendService.findEventExtendByEventId(event.getEventId());
			if(CommonFunctions.isNotBlank(findEventExtendByEventId, "extendId")){
				eventExtend.put("extendId", findEventExtendByEventId.get("extendId"));
				eventExtendService.updateEventExtendById(eventExtend);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean result = super.updateEventDisposal(event,params,userInfo);

		return result;
	}

	

}
