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
import cn.ffcs.uam.service.IFunConfigurationService;
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
@Service(value = "eventSyncForWXServImpl")
public class EventSyncForWXServImpl extends EventSyncServImpl implements IEventSyncService{

	@Autowired
	private IDataExchangeStatusTwoWayService dataExchangeStatusTwoWayService;
	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	private static String BIZPLATFORM = "011";

//	@Override
	public String getUri(){
		String wxUri = "http://jj.wx.ishequ.org";
		String configUri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.WX_WS_URI, IFunConfigurationService.CFG_TYPE_FACT_VAL);
		if(StringUtils.isNotBlank(configUri)){
			wxUri = configUri;
		}
		return wxUri;
	}
	
	@Override
	public String syncData() {
		System.out.println("WX! " + getUri());
		closeEvent();
		return "ok!";
	}
	
	private void closeEvent(){
		System.out.println("closeEvent!");
		Map<String, Object> params = new HashMap<String, Object>();
		String[] eventStatuss = {"03","04"};
		params.put("eventStatuss", eventStatuss);
		params.put("srcPlatform", BIZPLATFORM);
		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		params.put("exchangeFlag", IDataExchangeStatusService.EXCHANGE_FLAG_UNEXCHANGE);
		List<Map<String, Object>> closeEvents = dataExchangeStatusTwoWayService.findEventNewDocking(params);
		if(closeEvents.size() <1){
			System.out.println("no data!");
			return;
		}
		Map<String, Object> closeEvent = closeEvents.get(0);
		String eventId = closeEvent.get("EVENT_ID").toString();
		DataExchangeStatus dataExchangeStatus = getDataExchange(eventId);
		if(null == dataExchangeStatus){
			System.out.println("事件对应中间表不存在！");
			return;
		}
		String url = getUri() + "/wx/event.shtml?method=eventClose&eventId=" + dataExchangeStatus.getOwnSideBizCode();
		Long interId = dataExchangeStatus.getInterId();
		StringBuffer param = new StringBuffer();
		String obj = HttpUtil.doPost(url, param.toString());
		
		DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
		dataExchangeResult.setInterId(interId);
		dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
		boolean record = dataExchangeStatusService.updateDataExchangeStatusByInterId(dataExchangeResult);
	}
}
