package cn.ffcs.zhsq.event.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IUserService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventProcessService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.EventProcess;
import cn.ffcs.zhsq.mybatis.persistence.event.EventProcessMapper;
import cn.ffcs.zhsq.utils.ConstantValue;

@Service(value = "eventProcessServiceImpl")
public class EventProcessServiceImpl implements IEventProcessService {
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private EventProcessMapper eventProcessMapper;
	
	@Override
	public String findEventCloserByEventId(Long eventId){
		String closerName = "";
		
		if(eventId!=null && eventId>0){
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			if(event != null){
				String eventStatus = event.getStatus();
				if(ConstantValue.EVENT_STATUS_ARCHIVE.equals(eventStatus) || ConstantValue.EVENT_STATUS_END.equals(eventStatus)){
					EventProcess eventProcess = new EventProcess();
					eventProcess.setStatus(ConstantValue.EVENT_STATUS_ARCHIVE);
					eventProcess.setEventId(eventId);
					List<EventProcess> eventProcessList = eventProcessMapper.findEventProcessByCondition(eventProcess);
					if(eventProcessList!=null && eventProcessList.size()>0){
						Long closerId = eventProcessList.get(0).getUserId();
						
						UserInfo userInfo = userService.getUserExtraInfoByUserId(closerId, null);
						
						if(userInfo != null){
							closerName = userInfo.getPartyName();
						}
					}
				}
			}
		}
		
		return closerName;
	}
}
