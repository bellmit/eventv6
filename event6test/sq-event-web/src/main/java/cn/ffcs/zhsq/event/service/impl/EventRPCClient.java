package cn.ffcs.zhsq.event.service.impl;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HttpTransportProperties.ProxyProperties;

import cn.ffcs.shequ.utils.JaxbBinder;
import cn.ffcs.shequ.utils.JaxbUtil;
import cn.ffcs.zhsq.mybatis.domain.event.XmlDataResult;
import cn.ffcs.zhsq.mybatis.domain.event.XmlEventResult;
import cn.ffcs.zhsq.mybatis.domain.event.XmlTaskResult;

public class EventRPCClient{
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
//	public static void main(String[] args1) throws AxisFault { 
//		EventDisposal event = new EventDisposal();
//		event.setEventName("zhongshm");
//        
//        //  使用RPC方式调用WebService        
//        RPCServiceClient serviceClient = new RPCServiceClient();
//        Options options = serviceClient.getOptions();
//        //  指定调用WebService的URL
//        EndpointReference targetEPR = new EndpointReference(
//                "http://localhost:8086/sq-event-web/services/eventDisposalServImpl");
//        options.setTo(targetEPR);
//        //  指定getGreeting方法的参数值
//        Object[] opAddEntryArgs = new Object[] {event};
//        //  指定getGreeting方法返回值的数据类型的Class对象
//        Class[] classes = new Class[] {String.class};
//        //  指定要调用的getGreeting方法及WSDL文件的命名空间
//        QName opAddEntry = new QName("http://impl.service.event.zhsq.ffcs.cn", "sayhello");
//        //  调用getGreeting方法并输出该方法的返回值
//        System.out.println(serviceClient.invokeBlocking(opAddEntry, opAddEntryArgs, classes)[0]);
//        //  下面是调用getPrice方法的代码，这些代码与调用getGreeting方法的代码类似
////        classes = new Class[] {int.class};
////        opAddEntry = new QName("http://ws.apache.org/axis2", "getPrice");
////        System.out.println(serviceClient.invokeBlocking(opAddEntry, new Object[]{}, classes)[0]);
//    } 
	
	private static EndpointReference targetEPR = new EndpointReference(  
            "http://localhost:8086/sq-event-web/services/eventDisposalServImpl");  
  
    public void getResult() throws Exception  
    {  
        ServiceClient sender = new ServiceClient();  
        sender.addHeader(getHeader());
        sender.setOptions(buildOptions());  
        OMElement result = sender.sendReceive(buildParam());  
        System.out.println(result);
    }  
    
	public static OMElement getHeader() {
		OMFactory factory = OMAbstractFactory.getOMFactory();
		OMNamespace SecurityElementNamespace = factory.createOMNamespace(
				"http://impl.service.event.zhsq.ffcs.cn", "wsse");
		OMElement authenticationOM = factory.createOMElement("Authentication",
				SecurityElementNamespace);
		OMElement usernameOM = factory.createOMElement("Username",
				SecurityElementNamespace);
		OMElement passwordOM = factory.createOMElement("Password",
				SecurityElementNamespace);
		usernameOM.setText("admin");
		passwordOM.setText("111111");
		authenticationOM.addChild(usernameOM);
		authenticationOM.addChild(passwordOM);
		return authenticationOM;
	}
  
    private static OMElement buildParam()  
    {  
        OMFactory fac = OMAbstractFactory.getOMFactory();  
        OMNamespace omNs = fac.createOMNamespace("http://impl.service.event.zhsq.ffcs.cn", "");  
        OMElement data = fac.createOMElement("save", omNs);  
        OMElement eventName = fac.createOMElement("eventName", omNs);
        eventName.setText("对接测试事件：" + sdf.format(new Date()));
        data.addChild(eventName); 
        
        OMElement happenTimeStr = fac.createOMElement("happenTimeStr", omNs);
        happenTimeStr.setText("2015-05-18 11:06:27");
        data.addChild(happenTimeStr);
        
        OMElement occurred = fac.createOMElement("occurred", omNs);
        occurred.setText("对接测试事件地址");
        data.addChild(occurred);

        OMElement content = fac.createOMElement("content", omNs);
        content.setText("对接测试事件内容");
        data.addChild(content);

        OMElement nextOrgCode = fac.createOMElement("nextOrgCode", omNs);
        nextOrgCode.setText("350205002001");
        data.addChild(nextOrgCode);

        OMElement isSendSms = fac.createOMElement("isSendSms", omNs);
        isSendSms.setText("false");
        data.addChild(isSendSms);

        OMElement advice = fac.createOMElement("advice", omNs);
        advice.setText("对接测试事件办理意见");
        data.addChild(advice);

        OMElement oppoSideBusiCode = fac.createOMElement("oppoSideBusiCode", omNs);
        oppoSideBusiCode.setText("000000");
        data.addChild(oppoSideBusiCode);

        return data;  
    }  
  
    private static Options buildOptions()  
    {  
        Options options = new Options();  
        options.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);  
        options.setAction("http://localhost:8086/sq-event-web/services/eventDisposalServImpl/save");  
        options.setTo(targetEPR);  
        //options.setProperty 如果不是通过代理上网，此句可省  
        //options.setProperty(HTTPConstants.PROXY, buildProxy());  
        options.setTransportInProtocol(Constants.TRANSPORT_HTTP);  
        return options;  
    }  
  
    /** 
     * 本机采用代理服务器上网时，需要设置代理 
     * @return 
     */  
    public static ProxyProperties buildProxy()  
    {  
        ProxyProperties proxyProperties = new ProxyProperties();  
        proxyProperties.setProxyName("代理名称");  
        proxyProperties.setProxyPort(8080);  
        return proxyProperties;  
    }  
  
    public static void main(String[] args) throws Exception  
    {  
//    	EventRPCClient s = new EventRPCClient();  
//        s.getResult();  
//    	String xml = "<data><auth><username>admin</username><password>111111</password></auth><tasks><task><nextOrgCode>350205002</nextOrgCode><eventId>2544</eventId><taskName>taskName</taskName><transactorId></transactorId><transactorName>transactorName</transactorName><transactOrgID></transactOrgID><transactOrgName>transactOrgName</transactOrgName><results>results</results><createTime>2015-05-28 15:53:00</createTime><createUserID></createUserID><createUserName>createUserName</createUserName><readTime>2015-05-28 15:53:01</readTime><readUserID></readUserID><readUserName>readUserName</readUserName><endTime>2015-05-28 15:53:02</endTime><taskName>taskName</taskName><taskType>1</taskType><optType>1</optType><oppoSideParentBizCode></oppoSideParentBizCode><oppoSideParentBizType>3</oppoSideParentBizType><oppoSideBizCode>2544</oppoSideBizCode><oppoSideBizType>3</oppoSideBizType><srcPlatform>004</srcPlatform></task><task><nextOrgCode>350205002</nextOrgCode><eventId>2544</eventId><taskName>taskName</taskName><transactorId></transactorId><transactorName>transactorName</transactorName><transactOrgID></transactOrgID><transactOrgName>transactOrgName</transactOrgName><results>results</results><createTime>2015-05-28 15:53:00</createTime><createUserID></createUserID><createUserName>createUserName</createUserName><readTime>2015-05-28 15:53:01</readTime><readUserID></readUserID><readUserName>readUserName</readUserName><endTime>2015-05-28 15:53:02</endTime><taskName>taskName</taskName><taskType>1</taskType><optType>1</optType><oppoSideParentBizCode></oppoSideParentBizCode><oppoSideParentBizType>3</oppoSideParentBizType><oppoSideBizCode>2544</oppoSideBizCode><oppoSideBizType>3</oppoSideBizType><srcPlatform>004</srcPlatform></task></tasks></data>";
//    	XmlDataResult result = JaxbUtil.converyToJavaBean(xml, XmlDataResult.class);
//    	
    	
    	XmlDataResult xmlDataResult = new XmlDataResult();
    	XmlEventResult event = new XmlEventResult();

    	List<XmlTaskResult> xmlTaskResults = new ArrayList<XmlTaskResult>();
    	XmlTaskResult task = new XmlTaskResult();
    	task.setTaskName("aaa");
    	xmlTaskResults.add(task);
//    	event.setTasks(xmlTaskResults);
    	event.setTasks(xmlTaskResults);
    	
    	event.setEventId("111");
    	xmlDataResult.setEvent(event);
//    	xmlDataResult.setTasks(xmlTaskResults);
    	
    	
    	JaxbBinder jaxbBinder = new JaxbBinder(XmlDataResult.class);
    	String xml = jaxbBinder.toXml(xmlDataResult, "UTF-8");
//    	XmlDataResult result =jaxbBinder.fromXml(xml);
//		JSONObject jsonObj = JSONObject.fromObject(result);
    	System.out.println(xml);
    }
}
