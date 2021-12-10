package cn.ffcs.zhsq.event.service.stub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.event.service.IEventSyncService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 梅林街道事件同步实现
 * @author zhongshm
 * @create 2015-9-25上午11:29:57
 */
@Service(value = "eventSyncForMLServImpl")
public class EventSyncForMLServImpl implements IEventSyncService {

	@Autowired
	private IDataExchangeStatusTwoWayService dataExchangeStatusTwoWayService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;
	
	private static String BIZPLATFORM_ML = "010";
//	private static String URI = "http://59.56.249.132:9020";
	
	@Override
	public String syncData() {
		sendEvent();
		closeEvent();
		return "ok!";
	}
	
	public String getUri(){
		String uri = "http://192.168.2.2:9020";
		String configUri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.ML_WS_URI, IFunConfigurationService.CFG_TYPE_FACT_VAL);
		if(StringUtils.isNotBlank(configUri))
			uri = configUri;
		return uri;
	}

	private void sendEvent(){
		System.out.println("sendEvent!");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("activityNameTrigger", ConstantValue.DEFAULT_CURRENT_ACTIVITY_NAME_TRIGGER + "_" + BIZPLATFORM_ML);
		params.put("userIdTrigger", ConstantValue.DEFAULT_CURRENT_USER_ID_TRIGGER + "_" + BIZPLATFORM_ML);
		params.put("groupIdTrigger", ConstantValue.DEFAULT_CURRENT_GROUP_ID_TRIGGER + "_" + BIZPLATFORM_ML);
		params.put("orgIdTrigger", ConstantValue.DEFAULT_CURRENT_ORG_ID_TRIGGER + "_" + BIZPLATFORM_ML);
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("destPlatform", BIZPLATFORM_ML);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		
		List<Map<String,Object>> dataExchangeStatuss = dataExchangeStatusTwoWayService.findEventNewAppointed(params);
		JSONArray json = JSONArray.fromObject(dataExchangeStatuss);
		System.out.println(json.toString());
		if(null == dataExchangeStatuss || dataExchangeStatuss.size() < 1) return;
		Map<String,Object> dataExchange = dataExchangeStatuss.get(0);
		String url = getUri() + "/eventAction!sendEvent.action?usr=zhihuizhongxin&psw=72ce6c1f54d94454b33287c147109022";
		StringBuffer param = new StringBuffer();
		String eventId = dataExchange.get("EVENT_ID").toString();
		param.append("&eventId=" + eventId);
		param.append("&happenTime=" + dataExchange.get("HAPPEN_TIME_STR"));
		param.append("&type=" + dataExchange.get("TYPE_"));
		param.append("&occurred=" + dataExchange.get("OCCURRED"));
		param.append("&content=" + dataExchange.get("CONTENT_"));
		param.append("&reporter=" + dataExchange.get("CONTACT_USER"));
		param.append("&reporterTel=" + dataExchange.get("TEL"));
		param.append("&results=");
		
		JSONObject obj = HttpUtil.doBodyPost(url, param.toString());
		String oppoSideBizCode = "0";
		if(null != obj && null != obj.get("code") && obj.get("code").equals("1"))
			oppoSideBizCode = obj.get("result").toString();
		
		DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
		dataExchangeResult.setOppoSideBizCode(oppoSideBizCode);
		dataExchangeResult.setOppoSideBizType("2");
		dataExchangeResult.setOwnSideBizCode(eventId);
		dataExchangeResult.setOwnSideBizType("2");
		dataExchangeResult.setExchangeFlag("1");
		dataExchangeResult.setDestPlatform(BIZPLATFORM_ML);
		dataExchangeResult.setSrcPlatform("000");
		dataExchangeStatusService.saveDataExchangeStatus(dataExchangeResult);
	}
	
	private void closeEvent(){
		System.out.println("closeEvent!");
		Map<String, Object> params = new HashMap<String, Object>();
		String[] eventStatuss = {"03","04"};
		params.put("eventStatuss", eventStatuss);
		params.put("srcPlatform", BIZPLATFORM_ML);
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
		String url = getUri() + "/eventAction!closeEvent.action?usr=zhihuizhongxin&psw=72ce6c1f54d94454b33287c147109022";
		Long interId = dataExchangeStatus.getInterId();
		StringBuffer param = new StringBuffer();
		param.append("&eventId=" + dataExchangeStatus.getOppoSideBizCode());
		param.append("&result=" + (null != closeEvent.get("REMARKS") ? closeEvent.get("REMARKS").toString(): ""));
		param.append("&closeTime=" + (null != closeEvent.get("FIN_TIME")?closeEvent.get("FIN_TIME").toString():""));

		JSONObject obj = HttpUtil.doBodyPost(url, param.toString());
		System.out.println(obj.get("code") + "-" + interId + "-" + obj.get("code").toString().equals("1"));
		if(null != obj.get("code")){
			DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
			dataExchangeResult.setInterId(interId);
			dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
			boolean record = dataExchangeStatusService.updateDataExchangeStatusByInterId(dataExchangeResult);
		}
	}
	
	/**
	 * 取中间表信息
	 * @param eventId
	 * @return
	 */
	private DataExchangeStatus getDataExchange(String eventId){
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
