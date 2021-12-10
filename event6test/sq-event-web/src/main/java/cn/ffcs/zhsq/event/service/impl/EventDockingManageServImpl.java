package cn.ffcs.zhsq.event.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDockingTaskService;
import cn.ffcs.zhsq.event.service.IEventExDsService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.persistence.dataExchange.DataExchangeMapper;

@Service(value = "eventDockingManageServImpl")
public class EventDockingManageServImpl implements IEventDockingTaskService{

	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;
	@Autowired
	private IDataExchangeStatusTwoWayService dataExchangeStatusTwoWayService;
	@Autowired
	private DataExchangeMapper dataExchangeMapper;
	@Autowired
	private IEventExDsService eventExDsService;
	
	@Override
	public void doDocking() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String,Object>> findDataExchangeEvent(Map<String, Object> params){
//		EUDGPagination pagination = eventDisposalService.findTodoEventWorkFlowPagination(page, rows, params);
//		List<EventDisposal> events = (List<EventDisposal>) pagination.getRows();
//		for (int i = 0; i < events.size(); i++) {
//			Map<String, Object> deParams = new HashMap<String, Object>();
//			deParams.put("ownSideBizCode", events.get(i).getEventId());
//			List<DataExchangeStatus> dataExchanges = dataExchangeMapper.findByDataExchang(deParams);
//			if(dataExchanges.size()>0){
//				events.get(i).setExchangeFlag("1");
//			}else{
//				events.get(i).setExchangeFlag("0");
//			}
//		}
//		pagination.setRows(events);
//		EUDGPagination eudgPagination = new EUDGPagination(pagination.getTotal(), events);
		

//		params.put("activityNameTrigger", ConstantValue.DEFAULT_CURRENT_ACTIVITY_NAME_TRIGGER + "_" + BIZPLATFORM_AH);
//		params.put("userIdTrigger", ConstantValue.DEFAULT_CURRENT_USER_ID_TRIGGER + "_" + BIZPLATFORM_AH);
//		params.put("groupIdTrigger", ConstantValue.DEFAULT_CURRENT_GROUP_ID_TRIGGER + "_" + BIZPLATFORM_AH);
//		params.put("orgIdTrigger", ConstantValue.DEFAULT_CURRENT_ORG_ID_TRIGGER + "_" + BIZPLATFORM_AH);
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
//		params.put("destPlatform", BIZPLATFORM_AH);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		List<Map<String,Object>> dataExchangeStatuss = dataExchangeStatusTwoWayService.findEventNewAppointed(params);
		return dataExchangeStatuss;
	}

	@Override
	public Long report(EventDisposal event){
		String[] result = eventExDsService.report(event);
		
		DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
//		dataExchangeResult.setInterId(dataExchangeStatus.getInterId());
		dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
		dataExchangeResult.setOppoSideBizCode(result[1]);
		dataExchangeResult.setOwnSideBizCode(String.valueOf(event.getEventId()));
		dataExchangeResult.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeResult.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeResult.setSrcPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		dataExchangeResult.setDestPlatform(IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
		dataExchangeResult.setXmlData(result[0]);
		Long record = dataExchangeStatusService.saveDataExchangeStatus(dataExchangeResult);
		return record;
	}
}
