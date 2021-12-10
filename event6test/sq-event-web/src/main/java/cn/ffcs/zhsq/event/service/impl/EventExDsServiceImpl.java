package cn.ffcs.zhsq.event.service.impl;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.utils.JaxbBinder;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.event.service.IEventExDsService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.docking.ReportedIncident;

@Service(value = "eventExDsServiceImpl")
public class EventExDsServiceImpl implements IEventExDsService{

	@Override
	public String[] report(EventDisposal event){
		DailyIncidentServiceStub stub = null;
		String[] result = {};
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
			reportedIncident.setIncidentType("109999");//事件类型 字典
			reportedIncident.setRegionCode("350582000000");//行政区域
//			reportedIncident.setIncidentCode("");
			reportedIncident.setReportOrgCode("DW-00000001");//上报单位
			reportedIncident.setIncidentSource(5);
			reportedIncident.setIncidentSourceCode(String.valueOf(event.getEventId()));
			reportedIncident.setDescription(event.getContent());
			reportedIncident.setOccurPlace(event.getOccurred());
			reportedIncident.setReportPerson(event.getContactUser());
			reportedIncident.setReportMobile(event.getTel());
			reportedIncident.setOccurDate(event.getHappenTimeStr());
			
			JaxbBinder jaxbBinder = new JaxbBinder(ReportedIncident.class);
	    	String result1 =jaxbBinder.toXml(reportedIncident, "UTF-8");
	    	result[0] = result1;
			commonRequest.setREQXML(result1);
			commonRequestE.setCommonRequest(commonRequest);
			DailyIncidentServiceStub.CommonResponseE commonResponseE = stub.process(commonRequestE);
			System.out.println("ERRCD:"+commonResponseE.getCommonResponse().getCommonResult().getERRCD());
			System.out.println("RESXML:"+commonResponseE.getCommonResponse().getCommonResult().getRESXML());
			String RESXML = commonResponseE.getCommonResponse().getCommonResult().getRESXML();
			RESXML = RESXML.replace("<innerIncidentCode>", "").replace("</innerIncidentCode>", "");
			if(commonResponseE.getCommonResponse().getCommonResult().getERRCD()==0){
				result[1] = RESXML;
				return result;
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
		result[1] = "0";
		return result;
	}
}
