package cn.ffcs.zhsq.event.service.stub;

import cn.ffcs.shequ.utils.APIHttpClient;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.zhsq.event.service.stub.gansu.SendDataServiceStub;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.sf.json.JSONObject;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * @author zhongshm
 * @create 2017-06-13 8:41
 **/
public class Test {
    public static void main(String[] args) {
        closeEvt();
//        evlEvt();
//        startEvt();
//        process();
//        testHuli();
    }

    /**
     * 同安对接测试
     */
    private static void testHuli(){
        String url = "http://test.mysinosoft.com/tadc/incident/list/06";

//		String params = "pageNo=1&pageSize=100&beginTime=20170101000000&endTime=20170101120000";
//		params = "{\"pageNo\":1,\"pageSize\":100,\"beginTime\":\"20170101000000\",\"endTime\":\"20170101120000\"}";

        JSONObject params = new JSONObject();
        params.put("pageNo",1);
        params.put("pageSize",1);
        params.put("beginTime","20110101000000");
        params.put("endTime","20170621120000");
        APIHttpClient ac = new APIHttpClient(url);
        String result = ac.post(params.toString());
        System.out.println(result);
    }


    private static void startEvt(){
        try {
            EventDisposalServImplStub stub = new EventDisposalServImplStub();
//            stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,Boolean.FALSE);
            EventDisposalServImplStub.StartEvt startEvt = new EventDisposalServImplStub.StartEvt();
            StringBuffer param = new StringBuffer();
            param.append("<data>");
            param.append("<auth>");
            param.append("<username>xmhb</username>");
            param.append("<password>38852f779ea7b1474ca55e6b5ff0888b</password>");
            param.append("</auth>");
            param.append("<event>");
            param.append("<gridCode>350205100</gridCode>");
            param.append("<eventName>事件名称4</eventName>");
            param.append("<eventType>0701</eventType>");
            param.append("<happenTimeStr>2017-05-08 2019:47:50</happenTimeStr>");
            param.append("<occurred>陈埭镇坊脚村委会盛兴路一工地</occurred>");
            param.append("<content>报称：在陈埭坊脚盛兴路一工地，老板拖欠其5000元工资。</content>");
            param.append("<isSendSms>false</isSendSms>");
            param.append("<oppoSideBusiCode>1239331</oppoSideBusiCode>");
            param.append("<bizPlatform>016</bizPlatform>");
            param.append("<contactUserName>匿名</contactUserName>");
            param.append("<contactTel>15359469224</contactTel>");
            param.append("<registerTimeStr>2017-05-08 2019:49:36</registerTimeStr>");
            param.append("<advice>19时47分在陈埭镇坊脚村委会盛兴路一工地报称：在陈埭坊脚盛兴路一工地，老板拖欠其5000元工资。电话：15359469224。</advice>");
            param.append("</event>");
            param.append("</data>");
            System.out.println(param.toString());
            startEvt.setXmlData(param.toString());
            EventDisposalServImplStub.StartEvtResponse startEvtResponse = stub.startEvt(startEvt);
            System.out.println(startEvtResponse.get_return());
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void evlEvt(){
        String url = "http://gd.fjsq.org:8450/zhsq_event/services/eventDisposalServImpl?wsdl";

        StringBuffer param = new StringBuffer();
        param.append("<data>");
        param.append("<auth>");
        param.append("<username>xmhb</username>");
        param.append("<password>38852f779ea7b1474ca55e6b5ff0888b</password>");
        param.append("</auth>");
        param.append("<event>");
        param.append("<eventId>8769</eventId>");
        param.append("<closeDate>2017-06-15 2019:47:50</closeDate>");
        param.append("<closeOrgName>指挥中心</closeOrgName>");
        param.append("<advice>结案了</advice>");
        param.append("<bizPlatform>016</bizPlatform>");
        param.append("</event>");
        param.append("</data>");

        try {
            EventDisposalServImplStub stub = new EventDisposalServImplStub();
            //端口映射
            stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,Boolean.FALSE);

            EventDisposalServImplStub.EvlEvt evlEvt = new EventDisposalServImplStub.EvlEvt();

            EventDisposalServImplStub.CloseEvt closeEvt = new EventDisposalServImplStub.CloseEvt();
            EventDisposalServImplStub.ShuntEvent shuntEvent = new EventDisposalServImplStub.ShuntEvent();
            EventDisposalServImplStub.CloseEvent closeEvent = new EventDisposalServImplStub.CloseEvent();
            EventDisposalServImplStub.StartEvt startEvt = new EventDisposalServImplStub.StartEvt();

            evlEvt.setXmlData(param.toString());
            EventDisposalServImplStub.EvlEvtResponse evlEvtResponse = stub.evlEvt(evlEvt);

            System.out.println(evlEvtResponse.get_return());
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void closeEvt(){
        String url = "http://gd.fjsq.org:8450/zhsq_event/services/eventDisposalServImpl?wsdl";

        StringBuffer param = new StringBuffer();
//        param.append("<data>");
//        param.append("<auth>");
//        param.append("<username>xmhb</username>");
//        param.append("<password>38852f779ea7b1474ca55e6b5ff0888b</password>");
//        param.append("</auth>");
//        param.append("<event>");
//        param.append("<eventId>8732</eventId>");
//        param.append("<closeDate>2017-06-15 2019:47:50</closeDate>");
//        param.append("<closeOrgName>指挥中心</closeOrgName>");
//        param.append("<advice>结案了</advice>");
//        param.append("<bizPlatform>016</bizPlatform>");
//        param.append("</event>");
//        param.append("</data>");
        param.append("<data><auth><username>jy12345admin</username><password>91227d999e8847191f6731c0f495f6a1</password></auth><event><eventId>111862</eventId><closerID /><closerName /><closeDate>2017-06-30 10:07:35</closeDate><advice>【澄江街道】处理意见111111【公安局】处理意见222323123</advice><closeOrgID /><closeOrgName>澄江街道 公安局 </closeOrgName><isSendSms /><attrs><attr><attrType /><attrName>新建 Microsoft Office Word 文档.docx</attrName><attrURL>http://58.214.254.6:9696/12345/GSMS/GSMS.Common/JsonHandles/DownFile.aspx?fileNumber=01cb97fe-7f83-4d6e-80e2-9b4b2c1723df</attrURL><attrSize /><attrBiz>1</attrBiz></attr></attrs></event></data>");

        try {
            EventDisposalServImplStub stub = new EventDisposalServImplStub();
            //端口映射
            stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,Boolean.FALSE);

            EventDisposalServImplStub.CloseEvt closeEvt = new EventDisposalServImplStub.CloseEvt();
            EventDisposalServImplStub.ShuntEvent shuntEvent = new EventDisposalServImplStub.ShuntEvent();
            EventDisposalServImplStub.CloseEvent closeEvent = new EventDisposalServImplStub.CloseEvent();
            EventDisposalServImplStub.StartEvt startEvt = new EventDisposalServImplStub.StartEvt();

            closeEvt.setXmlData(param.toString());
            EventDisposalServImplStub.CloseEvtResponse closeEvtResponse = stub.closeEvt(closeEvt);

            System.out.println(closeEvtResponse.get_return());
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
