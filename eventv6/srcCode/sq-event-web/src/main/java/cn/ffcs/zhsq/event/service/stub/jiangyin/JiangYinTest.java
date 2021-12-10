package cn.ffcs.zhsq.event.service.stub.jiangyin;

import cn.ffcs.zhsq.event.service.stub.jiangyin.XunfangApp.MessagePushCallbackHandler;
import cn.ffcs.zhsq.event.service.stub.jiangyin.XunfangApp.MessagePushCallbackHandlerImpl;
import cn.ffcs.zhsq.event.service.stub.jiangyin.XunfangApp.MessagePushStub;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.io.IOException;
import java.rmi.RemoteException;


/**
 * @author zhongshm
 * @create 2017-06-13 8:41
 **/


//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="classpath*:applicationContext-*.xml")
//@TransactionConfiguration(defaultRollback = false)
public class JiangYinTest {

    @Test
    public void jzTest(){

        String data = "<data><auth><username>jylwadmin</username><password>223249e12098c7f90ba9db90a3c26fa2</password></auth><event><orgCode>320281403</orgCode><gridCode>320281403</gridCode><targetOrgCode>320281403</targetOrgCode><eventName>道板缺失</eventName><bizPlatform>030</bizPlatform><oppoSideBusiCode>9dkh2ymqecd3</oppoSideBusiCode><contactUserName>马小亮</contactUserName><happenTimeStr>2018-02-27 08:54:36</happenTimeStr><registerTimeStr>2018-02-09 16:09:00</registerTimeStr><advice>该道板问题由利港街道建设管理科处理</advice><contactTel>17715686269</contactTel><content>利康路派出所对面路牙多处缺失</content><occurred>利港派出所</occurred><eventType>20026001001</eventType></event></data>";

        try {
            MessagePushStub stub = new MessagePushStub();
            MessagePushStub.StartEvt startEvt = new MessagePushStub.StartEvt();
            startEvt.setIn0("1");
            MessagePushCallbackHandlerImpl callbackHandler = new MessagePushCallbackHandlerImpl();
            stub.startstartEvt(startEvt, callbackHandler);
            synchronized (callbackHandler) {
                try {
                    callbackHandler.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws JSONException {
        System.out.println("111");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ThirdNumber","");
        jsonObject.put("CaseSourceID",18);
        jsonObject.put("CaseTypeID",6);
        jsonObject.put("WantPerson","街道");
        jsonObject.put("WantPersonTel ","18321300820");
        jsonObject.put("Title","江阴测试数据");
        jsonObject.put("Village","320281");
        jsonObject.put("Address","江阴测试数据地址");
        jsonObject.put("WantContent","江阴测试数据描述");
        jsonObject.put("WantDate","2017-06-30 09:58:08");//提交日期
        jsonObject.put("State",0);
        jsonObject.put("SourceNumber","111864");
        jsonObject.put("Result", "");
        jsonObject.put("FinishDate", "");
        jsonObject.put("ReplyContent", "");
        System.out.println(jsonObject.toString());


        try {
            ThirdService2Stub stub = new ThirdService2Stub("http://58.214.254.6:9696/12345/WebService/ThirdService2.asmx?wsdl");
            ThirdService2Stub.SubmitThird submitThird = new ThirdService2Stub.SubmitThird();
            submitThird.setCaseSourceID(18);
            submitThird.setSessionID("V0dIMTg=");
            submitThird.setCaseSourceName("WGH");
            submitThird.setStrThird(jsonObject.toString());

            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(3000000);//设置超时(单位是毫秒)
            stub._getServiceClient().setOptions(options);

            //端口映射
            stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,Boolean.FALSE);


            ThirdService2Stub.SubmitThirdResponse submitThirdResponse = stub.submitThird(submitThird);
            System.out.println(submitThirdResponse.getSubmitThirdResult());

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testFile() {
        try {
            FileServiceStub stub = new FileServiceStub("http://58.214.254.6:9696/12345/WebService/FileService.asmx?wsdl");
            FileServiceStub.UploadThirdFile uploadThirdFile = new FileServiceStub.UploadThirdFile();

            uploadThirdFile.setCaseSourceID(18);
            uploadThirdFile.setSessionID("V0dIMTg=");
            uploadThirdFile.setCaseSourceName("WGH");
            uploadThirdFile.setFileName("a.jpg");
            uploadThirdFile.setFilepath("http://event.bug.aishequ.org/zhsq_event/upFileServlet?method=down&attachmentId=8002");

            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(3000000);//设置超时(单位是毫秒)
            stub._getServiceClient().setOptions(options);

            //端口映射
            stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,Boolean.FALSE);


            FileServiceStub.UploadThirdFileResponse uploadThirdFileResponse = stub.uploadThirdFile(uploadThirdFile);
            System.out.println(uploadThirdFileResponse.getUploadThirdFileResult());

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}
