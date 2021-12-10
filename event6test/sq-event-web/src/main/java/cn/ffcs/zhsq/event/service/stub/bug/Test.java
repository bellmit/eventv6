package cn.ffcs.zhsq.event.service.stub.bug;/**
 * Created by Administrator on 2017/6/30.
 */

import cn.ffcs.shequ.utils.APIHttpClient;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.zhsq.event.service.stub.EventDisposalServImplStub;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.axis2.AxisFault;

import java.rmi.RemoteException;

/**
 * @author zhongshm
 * @create 2017-06-30 10:05
 **/
public class Test {


    @org.junit.Test
    public void test1(){

        String url = "http://test.mysinosoft.com/tadc/incident/info/180419004";



        String s = HttpUtil.jsonPost(url, "");
        System.out.println(s);
    }

    @org.junit.Test
    public void fjsTest(){
        String url = "http://218.66.59.97:8181/gather/rec";
        JSONObject params = new JSONObject();
        params.put("cityCode","02");
        params.put("token","FJB01");
        params.put("uploadKey",System.currentTimeMillis());

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskNum","20161104_60010101_00003");
        jsonObject.put("sourceID","7225");
        jsonObject.put("regionCode","350200000000");
        jsonObject.put("mainType","1");
//        jsonObject.put("coordinateX","20161104_60010101_00003");
//        jsonObject.put("coordinateX","20161104_60010101_00003");
        jsonObject.put("eventDesc","问题描述");
        jsonObject.put("createTime","2016-11-04 10:48:24");
        jsonObject.put("address","详细地址");
        jsonObject.put("patrolID","30014792");
        jsonObject.put("patrolName","张三");
        jsonObject.put("archive","处理结果");
        jsonObject.put("archiveTime","2016-11-04 13:47:32");//结案时间
        jsonArray.add(jsonObject);
        params.put("list",jsonArray.toString());
        String p = "{\"cityCode\":\"03\",\"token\":\"FJC01\",\"uploadKey\":1512629040327,\"list\":[{\"taskNum\":\"20171205_0213_00570\",\"sourceID\":185512,\"regionCode\":\"350605005002\",\"mainType\":\"1\",\"eventDesc\":\"礤头旧村复垦项目工程施工现场\",\"createTime\":\"2017-12-05 23:33:19\",\"address\":\"礤头\",\"patrolName\":\"王辉赐\",\"archive\":\"复垦项目工程施工\",\"archiveTime\":\"2017-12-05 23:33:44\"}]}";

//        APIHttpClient ac = new APIHttpClient(url);
//        String result = ac.post(p);
//        System.out.println(result);


        String s = HttpUtil.jsonPost(url, p);
        System.out.println(s);
    }

    @org.junit.Test
    public void wsTest(){
        String url = "http://tianque.oicp.net:10953/webService/preventionTreatmentMemberWebService?wsdl";
        String params = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:hzt=\"http://www.hztianque.com\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <hzt:addPreventionTreatmentMember>\n" +
                "         <key>字符串</key>\n" +
                "         <userName>字符串</userName>\n" +
                "         <preventionTreatmentMember>字符串</preventionTreatmentMember>\n" +
                "      </hzt:addPreventionTreatmentMember>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        String s = HttpUtil.doPost(url, params);
        System.out.println(s);

    }

    @org.junit.Test
    public void startEvt(){
        String url = "http://gd.fjsq.org:8450/zhsq_event/services/eventDisposalServImpl?wsdl";

        try {
            EventDisposalServImplStub stub = new EventDisposalServImplStub(url);
            stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,Boolean.FALSE);
            EventDisposalServImplStub.StartEvt startEvt = new EventDisposalServImplStub.StartEvt();
            StringBuffer param = new StringBuffer();
            param.append("<data>");
            param.append("<auth>");
            param.append("<username>xmhb</username>");
            param.append("<password>38852f779ea7b1474ca55e6b5ff0888b</password>");
            param.append("</auth>");
            param.append("<event>");
            param.append("<gridCode>620102</gridCode>");
            param.append("<eventName>事件名称424</eventName>");
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

    @org.junit.Test
    public void testCloseEvt(){
        String url = "http://gd.fjsq.org:8450/zhsq_event/services/eventDisposalServImpl?wsdl";

        StringBuffer param = new StringBuffer();
//        param.append("<data>");
//        param.append("<auth>");
//        param.append("<username>xmhb</username>");
//        param.append("<password>38852f779ea7b1474ca55e6b5ff0888b</password>");
//        param.append("</auth>");
//        param.append("<event>");
//        param.append("<eventId>8769</eventId>");
//        param.append("<closeDate>2017-06-15 2019:47:50</closeDate>");
//        param.append("<closeOrgName>指挥中心</closeOrgName>");
//        param.append("<advice>结案了</advice>");
//        param.append("<bizPlatform>016</bizPlatform>");
//        param.append("</event>");
//        param.append("</data>");
        param.append("<data><auth><username>xmhb</username><password>38852f779ea7b1474ca55e6b5ff0888b</password></auth><event><eventId>111878</eventId><closerID></closerID><closerName>城管坐席员</closerName><closeDate>2017-07-03 00:00:00</closeDate><advice>该问题经采集员核查，问题已经处理好，同意结案。</advice><closeOrgID></closeOrgID><closeOrgName>城管坐席员</closeOrgName><bizPlatform>016</bizPlatform><isSendSms></isSendSms></event></data>");
//        param.append("<data><auth><username>jy12345admin</username><password>91227d999e8847191f6731c0f495f6a1</password></auth><event><eventId>111869</eventId><closerID /><closerName /><closeDate>2017-06-30 10:07:35</closeDate><advice>【澄江街道】处理意见111111【公安局】处理意见222323123</advice><closeOrgID /><closeOrgName>澄江街道 公安局 </closeOrgName><isSendSms /><attrs><attr><attrType /><attrName>新建 Microsoft Office Word 文档.docx</attrName><attrURL>http://58.214.254.6:9696/12345/GSMS/GSMS.Common/JsonHandles/DownFile.aspx?fileNumber=01cb97fe-7f83-4d6e-80e2-9b4b2c1723df</attrURL><attrSize /><attrBiz>1</attrBiz></attr></attrs></event></data>");

        try {
            EventDisposalServImplStub stub = new EventDisposalServImplStub();
            //端口映射
            stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,Boolean.FALSE);

            EventDisposalServImplStub.EvlEvt evlEvt = new EventDisposalServImplStub.EvlEvt();
            EventDisposalServImplStub.CloseEvt closeEvt = new EventDisposalServImplStub.CloseEvt();
            EventDisposalServImplStub.ShuntEvent shuntEvent = new EventDisposalServImplStub.ShuntEvent();
            EventDisposalServImplStub.CloseEvent closeEvent = new EventDisposalServImplStub.CloseEvent();
            EventDisposalServImplStub.StartEvt startEvt = new EventDisposalServImplStub.StartEvt();

            closeEvt.setXmlData(param.toString());

            System.out.println(stub.closeEvt(closeEvt).get_return());
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testCloseEvent(){
        String url = "http://gd.fjsq.org:8450/zhsq_event/services/eventDisposalServImpl?wsdl";

        StringBuffer param = new StringBuffer();
//        param.append("<data>");
//        param.append("<auth>");
//        param.append("<username>xmhb</username>");
//        param.append("<password>38852f779ea7b1474ca55e6b5ff0888b</password>");
//        param.append("</auth>");
//        param.append("<event>");
//        param.append("<eventId>8769</eventId>");
//        param.append("<closeDate>2017-06-15 2019:47:50</closeDate>");
//        param.append("<closeOrgName>指挥中心</closeOrgName>");
//        param.append("<advice>结案了</advice>");
//        param.append("<bizPlatform>016</bizPlatform>");
//        param.append("</event>");
//        param.append("</data>");
        param.append("<data><auth><username>xmhb</username><password>38852f779ea7b1474ca55e6b5ff0888b</password></auth><event><eventId>111871</eventId><closerID></closerID><closerName></closerName><closeDate>2017-06-30 00:00:00</closeDate><advice>该问题经采集员核查，问题已经处理好，同意结案。</advice><closeOrgID></closeOrgID><closeOrgName>城管坐席员</closeOrgName><bizPlatform>016</bizPlatform><isSendSms></isSendSms></event></data>");
//        param.append("<data><auth><username>jy12345admin</username><password>91227d999e8847191f6731c0f495f6a1</password></auth><event><eventId>111862</eventId><closerID /><closerName /><closeDate>2017-06-30 10:07:35</closeDate><advice>【澄江街道】处理意见111111【公安局】处理意见222323123</advice><closeOrgID /><closeOrgName>澄江街道 公安局 </closeOrgName><isSendSms /><attrs><attr><attrType /><attrName>新建 Microsoft Office Word 文档.docx</attrName><attrURL>http://58.214.254.6:9696/12345/GSMS/GSMS.Common/JsonHandles/DownFile.aspx?fileNumber=01cb97fe-7f83-4d6e-80e2-9b4b2c1723df</attrURL><attrSize /><attrBiz>1</attrBiz></attr></attrs></event></data>");

        try {
            EventDisposalServImplStub stub = new EventDisposalServImplStub();
            //端口映射
            stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,Boolean.FALSE);

            EventDisposalServImplStub.EvlEvt evlEvt = new EventDisposalServImplStub.EvlEvt();
            EventDisposalServImplStub.CloseEvt closeEvt = new EventDisposalServImplStub.CloseEvt();
            EventDisposalServImplStub.ShuntEvent shuntEvent = new EventDisposalServImplStub.ShuntEvent();
            EventDisposalServImplStub.CloseEvent closeEvent = new EventDisposalServImplStub.CloseEvent();
            EventDisposalServImplStub.StartEvt startEvt = new EventDisposalServImplStub.StartEvt();

            closeEvent.setXmlData(param.toString());

            EventDisposalServImplStub.CloseEventResponse closeEventResponse = stub.closeEvent(closeEvent);

            System.out.println(closeEventResponse.get_return());
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testFlowTask(){
        String url = "http://gd.fjsq.org:8450/zhsq_event/services/eventDisposalServImpl?wsdl";

        StringBuffer param = new StringBuffer();
//        param.append("<data>");
//        param.append("<auth>");
//        param.append("<username>xmhb</username>");
//        param.append("<password>38852f779ea7b1474ca55e6b5ff0888b</password>");
//        param.append("</auth>");
//        param.append("<event>");
//        param.append("<eventId>8769</eventId>");
//        param.append("<closeDate>2017-06-15 2019:47:50</closeDate>");
//        param.append("<closeOrgName>指挥中心</closeOrgName>");
//        param.append("<advice>结案了</advice>");
//        param.append("<bizPlatform>016</bizPlatform>");
//        param.append("</event>");
//        param.append("</data>");
        param.append("<data><auth><username>xmhb</username><password>38852f779ea7b1474ca55e6b5ff0888b</password></auth><tasks><task><taskId></taskId><eventId>13267</eventId><transactorId></transactorId><transactorName>曾欢</transactorName><transactOrgID></transactOrgID><transactOrgName>技术科</transactOrgName><results>请专业部门根据情况，妥善处理该问题，并在处理后反馈指挥中心。</results><createTime>2017-06-30 00:00:00</createTime><createUserID></createUserID><createUserName>曾欢</createUserName><readTime></readTime><readUserID></readUserID><readUserName></readUserName><endTime>2017-06-30 00:00:00</endTime><taskName>网格员处理</taskName><taskType></taskType><optType>1</optType><oppoSideParentBizCode></oppoSideParentBizCode><oppoSideParentBizType></oppoSideParentBizType><oppoSideBizCode>17063000581</oppoSideBizCode><oppoSideBizType>0</oppoSideBizType><srcPlatform>016</srcPlatform></task></tasks></data>");

        try {
            EventDisposalServImplStub stub = new EventDisposalServImplStub();
            //端口映射
            stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,Boolean.FALSE);

            EventDisposalServImplStub.FlowTask flowTask = new EventDisposalServImplStub.FlowTask();

            flowTask.setXmlData(param.toString());

            EventDisposalServImplStub.FlowTaskResponse flowTaskResponse = stub.flowTask(flowTask);

            System.out.println(flowTaskResponse.get_return());
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
