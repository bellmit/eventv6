package cn.ffcs.zhsq.event.service.stub.gansu;/**
 * Created by Administrator on 2017/6/20.
 */

import cn.ffcs.shequ.utils.QuartzManager;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhongshm
 * @create 2017-06-20 14:55
 **/
public class GansuTest {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    @Test
    public void quertzTest(){
        try {
            SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
            Scheduler sche = gSchedulerFactory.getScheduler();
            String job_name = "动态任务调度";
            System.out.println("【系统启动】开始(每1秒输出一次)...");
            QuartzManager.addJob(sche, job_name, QuartzJob.class, "0/1 * * * * ?");

            Thread.sleep(3000);
            System.out.println("【修改时间】开始(每2秒输出一次)...");
            QuartzManager.modifyJobTime(sche, job_name, "10/2 * * * * ?");
            Thread.sleep(4000);
            System.out.println("【移除定时】开始...");
            QuartzManager.removeJob(sche, job_name);
            System.out.println("【移除定时】成功");

            System.out.println("【再次添加定时任务】开始(每10秒输出一次)...");
            QuartzManager.addJob(sche, job_name, QuartzJob.class, "*/10 * * * * ?");
            Thread.sleep(30000);
            System.out.println("【移除定时】开始...");
            QuartzManager.removeJob(sche, job_name);
            System.out.println("【移除定时】成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void archiveCase(){
        StringBuffer sb = new StringBuffer();
        sb.append("<request>");
        sb.append("<params>");
        sb.append("<RecID>790680</RecID>");
        sb.append("<TransOpinion>满意</TransOpinion>");
        sb.append("<TransTime>"+sdf.format(new Date())+"</TransTime>");
        sb.append("</params>");
        sb.append("</request>");


        java.lang.String url = "http://61.178.32.80:8888/LZCGDTI/services/cityCasesReceive?wsdl";
        try {
            SendDataServiceStub stub = new SendDataServiceStub(url);

            SendDataServiceStub.ArchiveCase archiveCase = new SendDataServiceStub.ArchiveCase();

            org.apache.axis2.databinding.types.soapencoding.String requestXml = new org.apache.axis2.databinding.types.soapencoding.String();
            System.out.println(sb.toString());
            requestXml.setString(sb.toString());

            archiveCase.setRequestXml(requestXml);

            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(3000000);//设置超时(单位是毫秒)
            stub._getServiceClient().setOptions(options);

            System.out.println(stub.archiveCase(archiveCase).getArchiveCaseReturn());
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void callbackCase(){
        StringBuffer sb = new StringBuffer();
        sb.append("<request>");
        sb.append("<params>");
        sb.append("<RecID>790680</RecID>");
        sb.append("<TransOpinion>驳回意见</TransOpinion>");
        sb.append("<TransTime>2017-06-30 18:58:43</TransTime>");
        sb.append("</params>");
        sb.append("</request>");


        java.lang.String url = "http://61.178.32.80:8888/LZCGDTI/services/cityCasesReceive?wsdl";
        try {
            SendDataServiceStub stub = new SendDataServiceStub(url);

            SendDataServiceStub.CallbackCase callbackCase = new SendDataServiceStub.CallbackCase();

            org.apache.axis2.databinding.types.soapencoding.String requestXml = new org.apache.axis2.databinding.types.soapencoding.String();
            System.out.println(sb.toString());
            requestXml.setString(sb.toString());

            callbackCase.setRequestXml(requestXml);

            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(3000000);//设置超时(单位是毫秒)
            stub._getServiceClient().setOptions(options);

            System.out.println(stub.callbackCase(callbackCase).getCallbackCaseReturn());
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void feedbackResult(){
        StringBuffer sb = new StringBuffer();
        sb.append("<request>");
        sb.append("<params>");
        sb.append("<RecID>786563</RecID>");
        sb.append("<TransOpinion>综治平台处理意见</TransOpinion>");
        sb.append("<TransTime>2017-06-29 16:47:23</TransTime>");
        sb.append("<transStatus>6</transStatus>");
        sb.append("</params>");
        sb.append("</request>");


        java.lang.String url = "http://61.178.32.80:8888/LZCGDTI/services/cityCasesReceive?wsdl";
        try {
            SendDataServiceStub stub = new SendDataServiceStub(url);

            SendDataServiceStub.FeedbackResult feedbackResult = new SendDataServiceStub.FeedbackResult();

            org.apache.axis2.databinding.types.soapencoding.String requestXml = new org.apache.axis2.databinding.types.soapencoding.String();
            System.out.println(sb.toString());
            requestXml.setString(sb.toString());

            feedbackResult.setRequestXml(requestXml);

            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(3000000);//设置超时(单位是毫秒)
            stub._getServiceClient().setOptions(options);

            SendDataServiceStub.FeedbackResultResponse feedbackResultResponse = stub.feedbackResult(feedbackResult);

            System.out.println(feedbackResultResponse.getFeedbackResultReturn());
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void closeCaseResult(){
        StringBuffer sb = new StringBuffer();
        sb.append("<request>");
        sb.append("<params>");
        sb.append("<RecID>784523</RecID>");
        sb.append("<CloseTime>2017-06-30 09:02:23</CloseTime>");
        sb.append("<ReportCloseDesc>第二次结案</ReportCloseDesc>");
        sb.append("<ReportCloseUserName>csqu</ReportCloseUserName>");
        sb.append("</params>");
        sb.append("</request>");


        java.lang.String url = "http://61.178.32.80:8888/LZCGDTI/services/cityCasesReceive?wsdl";
        try {
            SendDataServiceStub stub = new SendDataServiceStub(url);

            SendDataServiceStub.CloseCaseResult closeCaseResult = new SendDataServiceStub.CloseCaseResult();

            org.apache.axis2.databinding.types.soapencoding.String requestXml = new org.apache.axis2.databinding.types.soapencoding.String();

            System.out.println(sb.toString());
            requestXml.setString(sb.toString());

            closeCaseResult.setRequestXml(requestXml);

            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(3000000);//设置超时(单位是毫秒)
            stub._getServiceClient().setOptions(options);

            SendDataServiceStub.CloseCaseResultResponse closeCaseResultResponse = stub.closeCaseResult(closeCaseResult);

            System.out.println(closeCaseResultResponse.getCloseCaseResultReturn());
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void process(){

        StringBuffer sb = new StringBuffer();
        sb.append("<request>");
        sb.append("<params>");
        sb.append("<RecID>111882</RecID>");
        sb.append("<EventTypeName>事件</EventTypeName>");
        sb.append("<RecTypeName>城市管理类</RecTypeName>");
        sb.append("<MainTypeName>市容环境</MainTypeName>");
        sb.append("<SubTypeName>其它市容环境问题</SubTypeName>");
        sb.append("<EventSrcName>社会公众举报</EventSrcName>");


        sb.append("<EventDesc>测试描述0703c</EventDesc>");
        sb.append("<Address>测试地址0703c</Address>");
        sb.append("<AcceptTime>"+sdf.format(new Date())+"</AcceptTime>");
        sb.append("<CoordinateX>-1</CoordinateX>");
        sb.append("<CoordinateY>-1</CoordinateY>");

        sb.append("<DistrictName></DistrictName>");
        sb.append("<StreetName></StreetName>");
        sb.append("<CellCode></CellCode>");
        sb.append("<CommunitName></CommunitName>");
        sb.append("</params>");
        sb.append("</request>");


        java.lang.String url = "http://61.178.32.80:8888/LZCGDTI/services/cityCasesReceive?wsdl";
        try {
            SendDataServiceStub stub = new SendDataServiceStub(url);
            //端口映射
//            stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,Boolean.FALSE);

            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(3000000);//设置超时(单位是毫秒)
            stub._getServiceClient().setOptions(options);
            SendDataServiceStub.Process process = new SendDataServiceStub.Process();

            org.apache.axis2.databinding.types.soapencoding.String requestXml = new org.apache.axis2.databinding.types.soapencoding.String();

            System.out.println(sb.toString());
            requestXml.setString(sb.toString());

            process.setRequestXml(requestXml);

            SendDataServiceStub.ProcessResponse processResponse = stub.process(process);

            System.out.println(processResponse.getProcessReturn());
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
