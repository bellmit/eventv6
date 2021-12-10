package cn.ffcs.zhsq.servlet;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import cn.ffcs.shequ.utils.JaxbBinder;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.impl.BizException;
import cn.ffcs.zhsq.event.service.impl.DailyIncidentServiceStub;
import cn.ffcs.zhsq.event.service.impl.DailyIncidentServiceStub.CommonResponseE;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.docking.ReportedFeedback;
import cn.ffcs.zhsq.mybatis.domain.event.docking.ReportedIncident;
import cn.ffcs.zhsq.mybatis.domain.event.docking.Result;
import cn.ffcs.zhsq.smsTask.service.ISmsTaskService;
import cn.ffcs.zhsq.utils.ConstantValue;

@Component("EventDockingTaskServlet") 
public class EventDockingTaskServlet implements ApplicationListener<ContextRefreshedEvent> {
	//spring中已经内置的几种事件：
	//ContextClosedEvent、ContextRefreshedEvent、ontextStartedEvent、ContextStoppedEvent、RequestHandleEvent 
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String defaultPeriod = "180000";//默认设置时间间隔
	private String BIZPLATFORM = "004";
	private volatile AtomicBoolean isStart = new AtomicBoolean(false);//用于控制onApplicationEvent内部方法只执行一次
	
	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;
	
	@Autowired
	private IFunConfigurationService configurationService;

	@Autowired
	private IEventDisposalService eventDisposalService;
	
	/** 
	* 当一个ApplicationContext被初始化或刷新触发 
	*/ 
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			//需要启用时，设置为 !isStart.compareAndSet(false, true)
			if(!isStart.compareAndSet(false, false)){
				Long period = 0L;
				//获取系统默认设置的时间间隔，没有则使用默认配置
				String overduePeriod = configurationService.changeCodeToValue(ConstantValue.WILL_EXPIRE_EVENT_PERIOD, "", IFunConfigurationService.CFG_TYPE_FACT_VAL);
				Timer timer = new Timer(); 
				
				if(StringUtils.isBlank(overduePeriod)){
					overduePeriod = defaultPeriod;
				}
				
				try{
					period = Long.valueOf(overduePeriod);
					if(period < 0){
						period = Long.valueOf(defaultPeriod);
					}
				}catch(Exception e){
					period = Long.valueOf(defaultPeriod);
					e.printStackTrace();
				}
				
			    //timer.schedule(task, firstTime, period); 
				// firstTime为Date类型,period为long   
				// 从firstTime时刻开始，每隔period毫秒执行一次。 
			    timer.schedule(new TimerTask() {  
			        public void run() {  
			        	eventDockingTask();
			        }  
			    }, new Date(), period);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void eventDockingTask(){
//		eventReport();
//		rejected();
//		eventFeedback();
//		taskFeedback();
	}
	
	public void eventReport(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("bizPlatform", BIZPLATFORM);
		List<DataExchangeStatus> dataExchangeStatuss = dataExchangeStatusService.findEventNewToReport(params);
		System.out.println(dataExchangeStatuss.size());
		for(DataExchangeStatus dataExchangeStatus : dataExchangeStatuss){
			EventDisposal event = eventDisposalService.findEventById(Long.valueOf(dataExchangeStatus.getOwnSideBizCode()), null);
			DailyIncidentServiceStub stub = null;
			try {
				stub = new DailyIncidentServiceStub();
				DailyIncidentServiceStub.CommonRequestE commonRequestE= new DailyIncidentServiceStub.CommonRequestE();
				DailyIncidentServiceStub.CommonRequest commonRequest= new DailyIncidentServiceStub.CommonRequest();
				commonRequest.setFUNC("reportDailyIncident");
				commonRequest.setSPID("S5");
				commonRequest.setSPPWD("grid@ffcs.cn");
				commonRequest.setVER("1.0");
				
				ReportedIncident reportedIncident = new ReportedIncident();
				reportedIncident.setIncidentName(event.getEventName());
				reportedIncident.setIncidentType("109999");//事件类型
				reportedIncident.setRegionCode("350582000000");//行政区域
	//			reportedIncident.setIncidentCode("");
				reportedIncident.setReportOrgCode("DW-00000001");//上报单位
				reportedIncident.setIncidentSource(5);
				reportedIncident.setIncidentSourceCode(dataExchangeStatus.getOwnSideBizCode());
				reportedIncident.setDescription(event.getContent());
				reportedIncident.setOccurPlace(event.getOccurred());
				reportedIncident.setReportPerson(event.getContactUser());
				reportedIncident.setReportMobile(event.getTel());
				reportedIncident.setOccurDate(event.getHappenTimeStr());
				
				JaxbBinder jaxbBinder = new JaxbBinder(ReportedIncident.class);
		    	String result =jaxbBinder.toXml(reportedIncident, "UTF-8");
				
				commonRequest.setREQXML(result);
				System.out.println("XMLresult:"+result);
				commonRequestE.setCommonRequest(commonRequest);
				
				DailyIncidentServiceStub.CommonResponseE commonResponseE = stub.process(commonRequestE);
				System.out.println("ERRCD:"+commonResponseE.getCommonResponse().getCommonResult().getERRCD());
				System.out.println("RESXML:"+commonResponseE.getCommonResponse().getCommonResult().getRESXML());
//				CommonResponseE commonResponseE = stub.process(commonRequestE);
//				commonResponseE.getCommonResponse().g
				///...

//				JaxbBinder jaxbBinderResult = new JaxbBinder(Result.class);
//				Result callBackResult = jaxbBinderResult.fromXml(commonResponseE.getCommonResponse().getCommonResult().getRESXML());
//				System.out.println("callBackResult="+callBackResult.getInnerIncidentCode());
				String RESXML = commonResponseE.getCommonResponse().getCommonResult().getRESXML();
				RESXML = RESXML.replace("<innerIncidentCode>", "").replace("</innerIncidentCode >", "");
				if(commonResponseE.getCommonResponse().getCommonResult().getERRCD()==0){
					DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
					dataExchangeResult.setInterId(dataExchangeStatus.getInterId());
					dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
					dataExchangeResult.setOppoSideBizCode(RESXML);
					boolean record = dataExchangeStatusService.updateDataExchangeStatusById(dataExchangeResult);
					if(!record)
						System.out.println("更新中间表失败！");
				}
			} catch (AxisFault e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (BizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void rejected(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("bizPlatform", BIZPLATFORM);
		List<Map<String, Object>> result = dataExchangeStatusService.findEventNewRejected(params);
		for(Map<String, Object> resultMap : result){
			resultMap.get("REMARKS");
			DailyIncidentServiceStub stub = null;
			try {
				stub = new DailyIncidentServiceStub();
				DailyIncidentServiceStub.CommonRequestE commonRequestE= new DailyIncidentServiceStub.CommonRequestE();
				DailyIncidentServiceStub.CommonRequest commonRequest= new DailyIncidentServiceStub.CommonRequest();
				///...
				commonRequest.setFUNC("reportFeedback");
				commonRequest.setSPID("S5");
				commonRequest.setSPPWD("grid@ffcs.cn");
				commonRequest.setVER("1.0");
				ReportedFeedback reportedFeedback = new ReportedFeedback();
				reportedFeedback.setCommandCode(resultMap.get("OPPO_SIDE_BIZ_CODE").toString());//对方标识
				reportedFeedback.setFeedbackSource(5);
				reportedFeedback.setFeedbackType("1");//0：正常反馈，1：退单，4：办结反馈
				reportedFeedback.setContent(resultMap.get("REMARKS").toString());
				reportedFeedback.setFeedbackPerson(resultMap.get("TRANSACTOR_NAME").toString());
//				reportedFeedback.setEditPerson("测试填写人姓名");
//				reportedFeedback.setFeedbackDate("2015-06-01 11:41:16");
				reportedFeedback.setFeedbackSourceCode(resultMap.get("OWN_SIDE_BIZ_CODE").toString());//网格平台编号
//				reportedFeedback.setFeedbackedIncidentType("");//反馈回来的事件类型
				
				JaxbBinder reportedFeedbackJaxbBinder = new JaxbBinder(ReportedFeedback.class);
		    	String reportedFeedbackresult =reportedFeedbackJaxbBinder.toXml(reportedFeedback, "UTF-8");
				
				commonRequest.setREQXML(reportedFeedbackresult);
				commonRequestE.setCommonRequest(commonRequest);
//				DailyIncidentServiceStub.CommonResponseE commonResponseE = stub.process(commonRequestE);
//				System.out.println("ERRCD:"+commonResponseE.getCommonResponse().getCommonResult().getERRCD());
//				System.out.println("RESXML:"+commonResponseE.getCommonResponse().getCommonResult().getRESXML());
//				
//				String RESXML = commonResponseE.getCommonResponse().getCommonResult().getRESXML();
//				RESXML = RESXML.replace("<innerFeedbackCode>", "").replace("</innerFeedbackCode >", "").replace("</innerFeedbackCode>", "");
//				if(commonResponseE.getCommonResponse().getCommonResult().getERRCD()==0){
//					DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
//					dataExchangeResult.setInterId(Long.valueOf(resultMap.get("INTER_ID").toString()));
//					dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
//					dataExchangeResult.setOppoSideBizCode(RESXML);
//					boolean record = dataExchangeStatusService.updateDataExchangeStatusById(dataExchangeResult);
//					if(!record)
//						System.out.println("更新中间表失败！");
//				}
			} catch (AxisFault e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} 
//			catch (BizException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
	
	private void taskFeedback(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("bizPlatform", BIZPLATFORM);
		List<Map<String, Object>> result = dataExchangeStatusService.findTaskNewToFeedback(params);
		for(Map<String, Object> resultMap : result){
			resultMap.get("REMARKS");
			DailyIncidentServiceStub stub = null;
			try {
				stub = new DailyIncidentServiceStub();
				DailyIncidentServiceStub.CommonRequestE commonRequestE= new DailyIncidentServiceStub.CommonRequestE();
				DailyIncidentServiceStub.CommonRequest commonRequest= new DailyIncidentServiceStub.CommonRequest();
				///...
				commonRequest.setFUNC("reportFeedback");
				commonRequest.setSPID("S5");
				commonRequest.setSPPWD("grid@ffcs.cn");
				commonRequest.setVER("1.0");
				ReportedFeedback reportedFeedback = new ReportedFeedback();
				reportedFeedback.setCommandCode(resultMap.get("OPPO_SIDE_BIZ_CODE").toString());//对方标识
				reportedFeedback.setFeedbackSource(5);
				reportedFeedback.setFeedbackType("0");//0：正常反馈，1：退单，4：办结反馈
				reportedFeedback.setContent(resultMap.get("REMARKS").toString());
				reportedFeedback.setFeedbackPerson(resultMap.get("TRANSACTOR_NAME").toString());
//				reportedFeedback.setEditPerson("测试填写人姓名");
//				reportedFeedback.setFeedbackDate("2015-06-01 11:41:16");
				reportedFeedback.setFeedbackSourceCode(resultMap.get("OWN_SIDE_BIZ_CODE").toString());//网格平台编号
//				reportedFeedback.setFeedbackedIncidentType("");//反馈回来的事件类型
				
				JaxbBinder reportedFeedbackJaxbBinder = new JaxbBinder(ReportedFeedback.class);
		    	String reportedFeedbackresult =reportedFeedbackJaxbBinder.toXml(reportedFeedback, "UTF-8");
				
				commonRequest.setREQXML(reportedFeedbackresult);
				commonRequestE.setCommonRequest(commonRequest);
				DailyIncidentServiceStub.CommonResponseE commonResponseE = stub.process(commonRequestE);
				System.out.println("ERRCD:"+commonResponseE.getCommonResponse().getCommonResult().getERRCD());
				System.out.println("RESXML:"+commonResponseE.getCommonResponse().getCommonResult().getRESXML());
				
				String RESXML = commonResponseE.getCommonResponse().getCommonResult().getRESXML();
				RESXML = RESXML.replace("<innerFeedbackCode>", "").replace("</innerFeedbackCode >", "").replace("</innerFeedbackCode>", "");
				if(commonResponseE.getCommonResponse().getCommonResult().getERRCD()==0){
					DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
					dataExchangeResult.setInterId(Long.valueOf(resultMap.get("INTER_ID").toString()));
					dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
					dataExchangeResult.setOppoSideBizCode(RESXML);
					boolean record = dataExchangeStatusService.updateDataExchangeStatusById(dataExchangeResult);
					if(!record)
						System.out.println("更新中间表失败！");
				}
			} catch (AxisFault e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (BizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void eventFeedback(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_SERVICE_OUTLET);
		params.put("destPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
		params.put("bizPlatform", BIZPLATFORM);
		List<Map<String, Object>> result = dataExchangeStatusService.findEventNewToFeedback(params);
		for(Map<String, Object> resultMap : result){
			String oppoSideBizCode = resultMap.get("OPPO_SIDE_BIZ_CODE").toString();
			String ownSideBizCode = resultMap.get("OWN_SIDE_BIZ_CODE").toString();
			boolean initTaskFeedback = dataExchangeStatusService.isEventTaskFeedback(Long.valueOf(oppoSideBizCode), BIZPLATFORM, ownSideBizCode);
			if(initTaskFeedback) return;
			DailyIncidentServiceStub stub = null;
			try {
				stub = new DailyIncidentServiceStub();
				DailyIncidentServiceStub.CommonRequestE commonRequestE= new DailyIncidentServiceStub.CommonRequestE();
				DailyIncidentServiceStub.CommonRequest commonRequest= new DailyIncidentServiceStub.CommonRequest();
				///...
				commonRequest.setFUNC("reportFeedback");
				commonRequest.setSPID("S5");
				commonRequest.setSPPWD("grid@ffcs.cn");
				commonRequest.setVER("1.0");
				ReportedFeedback reportedFeedback = new ReportedFeedback();
				reportedFeedback.setCommandCode(oppoSideBizCode);//
				reportedFeedback.setFeedbackSource(5);
				reportedFeedback.setFeedbackType("4");//0：正常反馈，1：退单，4：办结反馈
				reportedFeedback.setContent(resultMap.get("RESULT").toString());
				reportedFeedback.setFeedbackPerson("测试反馈人姓名");
//				reportedFeedback.setEditPerson("测试填写人姓名");
//				reportedFeedback.setFeedbackDate("2015-06-01 11:41:16");
				reportedFeedback.setFeedbackSourceCode(ownSideBizCode);//网格平台编号
//				reportedFeedback.setFeedbackedIncidentType("");//反馈回来的事件类型
				
				JaxbBinder reportedFeedbackJaxbBinder = new JaxbBinder(ReportedFeedback.class);
		    	String reportedFeedbackresult =reportedFeedbackJaxbBinder.toXml(reportedFeedback, "UTF-8");
				
				commonRequest.setREQXML(reportedFeedbackresult);
				commonRequestE.setCommonRequest(commonRequest);
				
				DailyIncidentServiceStub.CommonResponseE commonResponseE = stub.process(commonRequestE);
				System.out.println("ERRCD:"+commonResponseE.getCommonResponse().getCommonResult().getERRCD());
				System.out.println("RESXML:"+commonResponseE.getCommonResponse().getCommonResult().getRESXML());
//				System.out.println(stub.process(commonRequestE));
				String RESXML = commonResponseE.getCommonResponse().getCommonResult().getRESXML();
				RESXML = RESXML.replace("<innerFeedbackCode>", "").replace("</innerFeedbackCode >", "").replace("</innerFeedbackCode>", "");
				if(commonResponseE.getCommonResponse().getCommonResult().getERRCD()==0){
					DataExchangeStatus dataExchangeResult = new DataExchangeStatus();
					dataExchangeResult.setInterId(Long.valueOf(resultMap.get("INTER_ID").toString()));
					dataExchangeResult.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
					dataExchangeResult.setOppoSideBizCode(RESXML);
					boolean record = dataExchangeStatusService.updateDataExchangeStatusById(dataExchangeResult);
					if(!record)
						System.out.println("更新中间表失败！");
				}
			} catch (AxisFault e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} 
			catch (BizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
