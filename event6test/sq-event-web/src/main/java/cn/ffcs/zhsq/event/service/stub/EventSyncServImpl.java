package cn.ffcs.zhsq.event.service.stub;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.JaxbBinder;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventSyncService;
import cn.ffcs.zhsq.event.service.impl.BizException;
import cn.ffcs.zhsq.event.service.impl.DailyIncidentServiceStub;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.XmlTaskResult;
import cn.ffcs.zhsq.mybatis.domain.event.docking.ReportedFeedback;
import cn.ffcs.zhsq.mybatis.domain.event.docking.ReportedIncident;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 微信事件同步实现
 * @author zhongshm
 * @create 2015-9-25上午11:29:57
 */
@Service(value = "eventSyncServImpl")
public class EventSyncServImpl implements IEventSyncService {

	@Autowired
	private IDataExchangeStatusTwoWayService dataExchangeStatusTwoWayService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;
	
	@Override
	public String syncData() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
//		List<Map<String,Object>> dataExchangeStatuss = dataExchangeStatusTwoWayService.findEventAppointed(params);

//		System.out.println("sendTask DS!");
//		Map<String, Object> params = new HashMap<String, Object>();
////		params.put("destPlatform", BIZPLATFORM);
//		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
////		params.put("destPlatformTask", BIZPLATFORM);
//		params.put("ownSideBizTypeTask", IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
////		List<Map<String, Object>> srcTasks = dataExchangeStatusTwoWayService.findTaskNewAppointed(params);
		
		
		
		return null;
	}


	/**
	 * 取中间表信息
	 * @param eventId
	 * @return
	 */
	public DataExchangeStatus getDataExchange(String eventId){
		DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
		dataExchangeStatus.setOppoSideBizType("2");
		dataExchangeStatus.setOwnSideBizType("2");
		dataExchangeStatus.setDestPlatform("000");
		dataExchangeStatus.setOwnSideBizCode(eventId);
		dataExchangeStatus.setExchangeFlag("0");
		List<DataExchangeStatus> listDataExchangeStatus = dataExchangeStatusService.findDataExchangeList(dataExchangeStatus);
		if(listDataExchangeStatus.size() < 1)
			return null;
		return listDataExchangeStatus.get(0);
	}

}
