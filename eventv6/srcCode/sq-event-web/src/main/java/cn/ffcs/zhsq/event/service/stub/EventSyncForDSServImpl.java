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
 * 迪艾斯便民事件同步实现
 * @author zhongshm
 * @create 2015-9-25上午11:29:57
 */
@Service(value = "eventSyncForDSServImpl")
public class EventSyncForDSServImpl implements IEventSyncService {

	@Autowired
	private IDataExchangeStatusTwoWayService dataExchangeStatusTwoWayService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	private static String BIZPLATFORM = "004";
//	private static String URI = "http://10.45.6.60:8080/ds12345-web/ws/dailyIncidentService?wsdl";
	
	@Override
	public String syncData() {
		String dsIndex = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "DS_WS_INDEX", IFunConfigurationService.CFG_TYPE_FACT_VAL);
		if(StringUtils.isNotBlank(dsIndex)){
			for (int i = 0; i < Integer.valueOf(dsIndex); i++) {
//				reportEvent();
//				sendEvent();
				closeEvent();
//				sendTask();
			}
		}
		return "ok!";
	}
	
	public String getUri(){
		String uri = "http://10.45.6.60:8080/ds12345-web/ws/dailyIncidentService?wsdl";
		String configUri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DS_WS_URI, IFunConfigurationService.CFG_TYPE_FACT_VAL);
		if(StringUtils.isNotBlank(configUri)){
			uri = configUri;
		}
		return uri;
	}
	
	private void reportEvent(){
		System.out.println("sendEvent DS!");
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("activityNameTrigger", ConstantValue.DEFAULT_CURRENT_ACTIVITY_NAME_TRIGGER + "_" + BIZPLATFORM);
//		params.put("userIdTrigger", ConstantValue.DEFAULT_CURRENT_USER_ID_TRIGGER + "_" + BIZPLATFORM);
//		params.put("groupIdTrigger", ConstantValue.DEFAULT_CURRENT_GROUP_ID_TRIGGER + "_" + BIZPLATFORM);
//		params.put("orgIdTrigger", ConstantValue.DEFAULT_CURRENT_ORG_ID_TRIGGER + "_" + BIZPLATFORM);
//		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
//		params.put("destPlatform", BIZPLATFORM);
//		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
//		List<Map<String,Object>> dataExchangeStatuss = dataExchangeStatusTwoWayService.findEventAppointed(params);

		System.out.println("sendTask DS!");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("destPlatform", BIZPLATFORM);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		params.put("destPlatformTask", BIZPLATFORM);
		params.put("ownSideBizTypeTask", IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
		List<Map<String, Object>> srcTasks = dataExchangeStatusTwoWayService.findTaskNewAppointed(params);
		
		
	}
	
	private void sendTask(){
		System.out.println("sendTask DS!");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("destPlatform", BIZPLATFORM);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		params.put("destPlatformTask", BIZPLATFORM);
		params.put("ownSideBizTypeTask", IDataExchangeStatusService.BIZ_TYPE_TASK_NEW);
		List<Map<String, Object>> srcTasks = dataExchangeStatusTwoWayService.findTaskNewAppointed(params);
		if(srcTasks.size() < 1) return;
		Map<String, Object> srcTask = srcTasks.get(0);
		JSONArray json = JSONArray.fromObject(srcTask);
		System.out.println(json.toString());
		
		ReportedFeedback reportedFeedback = new ReportedFeedback();
		reportedFeedback.setCommandCode(srcTask.get("OPPO_SIDE_BIZ_CODE").toString());//对方标识
		reportedFeedback.setFeedbackSource(5);
		reportedFeedback.setFeedbackType("0");//0：正常反馈，1：退单，4：办结反馈
		reportedFeedback.setContent(srcTask.get("REMARKS").toString());
		reportedFeedback.setFeedbackPerson(srcTask.get("TRANSACTOR_NAME").toString());
//		reportedFeedback.setEditPerson("测试填写人姓名");
//		reportedFeedback.setFeedbackDate("2015-06-01 11:41:16");
		reportedFeedback.setFeedbackSourceCode(srcTask.get("OWN_SIDE_BIZ_CODE").toString());//网格平台编号
//		reportedFeedback.setFeedbackedIncidentType("");//反馈回来的事件类型
		
		JaxbBinder reportedFeedbackJaxbBinder = new JaxbBinder(ReportedFeedback.class);
    	String reportedFeedbackresult =reportedFeedbackJaxbBinder.toXml(reportedFeedback, "UTF-8");
    	System.out.println("reportedFeedbackresult->"+reportedFeedbackresult);
    	String ds_ws_sendTask = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "ds_ws_sendTask", IFunConfigurationService.CFG_TYPE_FACT_VAL);
		if(!"true".equals(ds_ws_sendTask)) return;
    	
    	DailyIncidentServiceStub stub = null;
		try {
			stub = new DailyIncidentServiceStub(getUri());
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DailyIncidentServiceStub.CommonRequestE commonRequestE= new DailyIncidentServiceStub.CommonRequestE();
		DailyIncidentServiceStub.CommonRequest commonRequest= new DailyIncidentServiceStub.CommonRequest();
		
		commonRequest.setREQXML(reportedFeedbackresult);
		commonRequestE.setCommonRequest(commonRequest);
		DailyIncidentServiceStub.CommonResponseE commonResponseE = null;
		try {
			commonResponseE = stub.process(commonRequestE);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ERRCD:"+commonResponseE.getCommonResponse().getCommonResult().getERRCD());
		System.out.println("RESXML:"+commonResponseE.getCommonResponse().getCommonResult().getRESXML());
		
		String RESXML = commonResponseE.getCommonResponse().getCommonResult().getRESXML();
		RESXML = RESXML.replace("<innerFeedbackCode>", "").replace("</innerFeedbackCode >", "").replace("</innerFeedbackCode>", "");
		
		DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
		dataExchangeResult.setInterId(Long.valueOf(srcTask.get("INTER_ID").toString()));
		dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
		boolean record = dataExchangeStatusService.updateDataExchangeStatusByInterId(dataExchangeResult);
	}

	private void sendEvent(){
		System.out.println("sendEvent DS!");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("activityNameTrigger", ConstantValue.DEFAULT_CURRENT_ACTIVITY_NAME_TRIGGER + "_" + BIZPLATFORM);
		params.put("userIdTrigger", ConstantValue.DEFAULT_CURRENT_USER_ID_TRIGGER + "_" + BIZPLATFORM);
		params.put("groupIdTrigger", ConstantValue.DEFAULT_CURRENT_GROUP_ID_TRIGGER + "_" + BIZPLATFORM);
		params.put("orgIdTrigger", ConstantValue.DEFAULT_CURRENT_ORG_ID_TRIGGER + "_" + BIZPLATFORM);
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("destPlatform", BIZPLATFORM);
		params.put("ownSideBizType", IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		
		List<Map<String,Object>> dataExchangeStatuss = dataExchangeStatusTwoWayService.findEventNewAppointed(params);
		JSONArray json = JSONArray.fromObject(dataExchangeStatuss);
		System.out.println(json.toString());
		if(null == dataExchangeStatuss || dataExchangeStatuss.size() < 1) return;
		Map<String,Object> dataExchange = dataExchangeStatuss.get(0);
		String eventId = dataExchange.get("EVENT_ID").toString();
		EventDisposal event = eventDisposalService.findEventById(Long.valueOf(eventId), null);
		
		ReportedIncident reportedIncident = new ReportedIncident();
		reportedIncident.setIncidentName(event.getEventName());
		reportedIncident.setIncidentType("109999");//事件类型 字典
		reportedIncident.setRegionCode("350582000000");//行政区域
		reportedIncident.setReportOrgCode("DW-00000001");//上报单位
		reportedIncident.setIncidentSource(5);
		reportedIncident.setIncidentSourceCode(eventId);
		reportedIncident.setDescription(event.getContent());
		reportedIncident.setOccurPlace(event.getOccurred());
		reportedIncident.setReportPerson(event.getContactUser());
		reportedIncident.setReportMobile(event.getTel());
		reportedIncident.setOccurDate(event.getHappenTimeStr());
		JaxbBinder jaxbBinder = new JaxbBinder(ReportedIncident.class);
    	String result =jaxbBinder.toXml(reportedIncident, "UTF-8");
		System.out.println("XMLresult:"+result);
    	String ds_ws_sendEvent = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "ds_ws_sendEvent", IFunConfigurationService.CFG_TYPE_FACT_VAL);
		if(!"true".equals(ds_ws_sendEvent)) return;
    	
		DailyIncidentServiceStub.CommonRequestE commonRequestE= new DailyIncidentServiceStub.CommonRequestE();
		DailyIncidentServiceStub.CommonRequest commonRequest= new DailyIncidentServiceStub.CommonRequest();
		commonRequest.setFUNC("reportDailyIncident");
		commonRequest.setSPID("S5");
		commonRequest.setSPPWD("grid@ffcs.cn");
		commonRequest.setVER("1.0");
		commonRequest.setREQXML(result);
		commonRequestE.setCommonRequest(commonRequest);

		DailyIncidentServiceStub stub = null;
		try {
			stub = new DailyIncidentServiceStub(getUri());
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DailyIncidentServiceStub.CommonResponseE commonResponseE = null;
		try {
			commonResponseE = stub.process(commonRequestE);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ERRCD:"+commonResponseE.getCommonResponse().getCommonResult().getERRCD());
		System.out.println("RESXML:"+commonResponseE.getCommonResponse().getCommonResult().getRESXML());
		String RESXML = commonResponseE.getCommonResponse().getCommonResult().getRESXML();
		RESXML = RESXML.replace("<innerIncidentCode>", "").replace("</innerIncidentCode>", "");
		
		DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
		dataExchangeResult.setOppoSideBizCode(RESXML);
		dataExchangeResult.setOppoSideBizType("2");
		dataExchangeResult.setOwnSideBizCode(eventId);
		dataExchangeResult.setOwnSideBizType("2");
		dataExchangeResult.setExchangeFlag("1");
		dataExchangeResult.setDestPlatform(BIZPLATFORM);
		dataExchangeResult.setSrcPlatform("000");
		dataExchangeStatusService.saveDataExchangeStatus(dataExchangeResult);
	}
	
	private void closeEvent(){
		System.out.println("closeEventDS!");
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
		Map<String, Object> resultMap = closeEvents.get(0);
		System.out.println(JSONArray.fromObject(closeEvents).toString());

//		return;
		String oppoSideBizCode = resultMap.get("OPPO_SIDE_BIZ_CODE").toString();
		String ownSideBizCode = resultMap.get("OWN_SIDE_BIZ_CODE").toString();
		
		ReportedFeedback reportedFeedback = new ReportedFeedback();
		reportedFeedback.setCommandCode(oppoSideBizCode);//
		reportedFeedback.setFeedbackSource(5);
		reportedFeedback.setFeedbackType("4");//0：正常反馈，1：退单，4：办结反馈
		reportedFeedback.setContent(resultMap.get("RESULT").toString());
		reportedFeedback.setFeedbackPerson(resultMap.get("CONTACT_USER").toString());
//		reportedFeedback.setEditPerson("测试填写人姓名");
//		reportedFeedback.setFeedbackDate("2015-06-01 11:41:16");
		reportedFeedback.setFeedbackSourceCode(ownSideBizCode);//网格平台编号
//		reportedFeedback.setFeedbackedIncidentType("");//反馈回来的事件类型
		
		JaxbBinder reportedFeedbackJaxbBinder = new JaxbBinder(ReportedFeedback.class);
    	String reportedFeedbackresult =reportedFeedbackJaxbBinder.toXml(reportedFeedback, "UTF-8");
    	System.out.println("XMLresult:"+reportedFeedbackresult);
    	String ds_ws_closeEvent = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "ds_ws_closeEvent", IFunConfigurationService.CFG_TYPE_FACT_VAL);
		if(!"true".equals(ds_ws_closeEvent)) return;
    	
		DailyIncidentServiceStub.CommonRequestE commonRequestE= new DailyIncidentServiceStub.CommonRequestE();
		DailyIncidentServiceStub.CommonRequest commonRequest= new DailyIncidentServiceStub.CommonRequest();
		commonRequest.setFUNC("reportFeedback");
		commonRequest.setSPID("S5");
		commonRequest.setSPPWD("grid@ffcs.cn");
		commonRequest.setVER("1.0");
		commonRequest.setREQXML(reportedFeedbackresult);
		commonRequestE.setCommonRequest(commonRequest);

		DailyIncidentServiceStub stub = null;
		try {
			stub = new DailyIncidentServiceStub(getUri());
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DailyIncidentServiceStub.CommonResponseE commonResponseE = null;
		try {
			commonResponseE = stub.process(commonRequestE);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ERRCD:"+commonResponseE.getCommonResponse().getCommonResult().getERRCD());
		System.out.println("RESXML:"+commonResponseE.getCommonResponse().getCommonResult().getRESXML());
		
		String RESXML = commonResponseE.getCommonResponse().getCommonResult().getRESXML();
		RESXML = RESXML.replace("<innerFeedbackCode>", "").replace("</innerFeedbackCode >", "").replace("</innerFeedbackCode>", "");
		
		DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
		dataExchangeResult.setInterId(Long.valueOf(resultMap.get("INTER_ID").toString()));
		dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
		dataExchangeResult.setDestPlatform(IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
		dataExchangeResult.setSrcPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		dataExchangeResult.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeResult.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
		dataExchangeResult.setOppoSideBizCode(RESXML);
		boolean record = dataExchangeStatusService.updateDataExchangeStatusByInterId(dataExchangeResult);
		if(!record)
			System.out.println("更新中间表失败！");
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
