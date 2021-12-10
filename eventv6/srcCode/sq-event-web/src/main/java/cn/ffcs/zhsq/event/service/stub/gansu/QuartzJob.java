package cn.ffcs.zhsq.event.service.stub.gansu;/**
 * Created by Administrator on 2017/6/27.
 */

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IUserService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.time.ITimerManageService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import net.sf.json.JSONObject;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.rmi.RemoteException;
import oracle.sql.TIMESTAMP;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongshm
 * @create 2017-06-27 15:55
 **/
public class QuartzJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private IDataExchangeStatusService dataExchangeStatusService;
    private IDataExchangeStatusTwoWayService dataExchangeStatusTwoWayService;
    private IFunConfigurationService funConfigurationService;
    private IUserService userService;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //业务编码
    private final String BIZ_PLATFORM = "016";
    private String uri = "";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("========="+DateUtils.getNow()+ " start 甘肃事件对接==========");
        IDataExchangeStatusService dataExchangeStatusService = null;
        try {
            SchedulerContext schCtx = context.getScheduler().getContext();
            ApplicationContext appCtx = (ApplicationContext)schCtx.get("applicationContext");
            this.dataExchangeStatusService = (IDataExchangeStatusService) appCtx.getBean("dataExchangeStatusServiceImpl");
            this.dataExchangeStatusTwoWayService = (IDataExchangeStatusTwoWayService) appCtx.getBean("dataExchangeStatusTwoWayServiceImpl");
            this.funConfigurationService = (IFunConfigurationService) appCtx.getBean("funConfigurationService");
            this.userService = (IUserService) appCtx.getBean("userService");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        String GANSU_uri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "GANSU_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        String GANSU_org_id = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "GANSU_ORG_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        String GANSU_user_id = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "GANSU_USER_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        if(StringUtils.isBlank(GANSU_uri)){
            logger.error("功能配置【GANSU_URI】未配置！");
            return;
        }else
            this.uri = GANSU_uri;
        if(StringUtils.isBlank(GANSU_org_id)){
            logger.error("功能配置【GANSU_ORG_ID】未配置！");
            return;
        }
        if(StringUtils.isBlank(GANSU_user_id)){
            logger.error("功能配置【GANSU_USER_ID】未配置！");
            return;
        }

        //获取上报事件
        reportEvent(GANSU_user_id, GANSU_org_id);
        //获取过程、结案事件
        reportTask();
        //获取驳回事件
        reject();
        //获取归档事件
        archive();
    }

    private void archive(){
        System.out.println("获取归档事件");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("descPlatform", BIZ_PLATFORM);
        params.put("srcPlatform", "000");
        List<Map<String, Object>> evaEvent = this.dataExchangeStatusService.findEvaEvent(params);
        if(evaEvent!=null&&evaEvent.size()>0) {
            for (Map<String, Object> eva : evaEvent) {
                System.out.println(eva);
                String oppo_side_biz_code = (String)eva.get("OPPO_SIDE_BIZ_CODE");
                String event_id = eva.get("ER_ID").toString();
                String remarks = (String)eva.get("EVA_CONTENT");
                java.sql.Timestamp end_time = (java.sql.Timestamp)eva.get("CREATE_DATE");
                String create_time_str = df.format(end_time);

                StringBuffer sb = new StringBuffer();
                sb.append("<request>");
                sb.append("<params>");
                sb.append("<RecID>"+oppo_side_biz_code+"</RecID>");
                sb.append("<TransOpinion>"+remarks+"</TransOpinion>");
                sb.append("<TransTime>"+create_time_str+"</TransTime>");
                sb.append("</params>");
                sb.append("</request>");
//                String result = "<?xml version='1.0' encoding='utf-8'?><request><commonResult><errorCode>0</errorCode><errorDesc>办结反馈成功！</errorDesc></commonResult></request>";
                String result = archiveCase(sb.toString());
                if(StringUtils.isNotBlank(result)){
//                    String errorCode = result.substring(result.indexOf("<errorCode>") + 11, result.indexOf("</errorCode>"));
//                    if(errorCode.equals("0")){
                        DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                        dataExchangeStatus.setExchangeFlag("1");
                        dataExchangeStatus.setOppoSideBizCode(oppo_side_biz_code);
                        dataExchangeStatus.setOppoSideBizType("4");
                        dataExchangeStatus.setOwnSideBizCode(event_id);
                        dataExchangeStatus.setOwnSideBizType("4");
                        dataExchangeStatus.setStatus("1");
                        dataExchangeStatus.setXmlData(sb.toString() + "|" +result);
                        dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                        dataExchangeStatus.setSrcPlatform("000");
                        this.dataExchangeStatusService.save(dataExchangeStatus);
//                    }
                }
            }
        }
    }

    private String archiveCase(String xml){
        StringBuffer sb = new StringBuffer();
        sb.append("<request>");
        sb.append("<params>");
        sb.append("<RecID>790680</RecID>");
        sb.append("<TransOpinion>满意</TransOpinion>");
        sb.append("<TransTime>"+df.format(new Date())+"</TransTime>");
        sb.append("</params>");
        sb.append("</request>");


        java.lang.String url = "http://61.178.32.80:8888/LZCGDTI/services/cityCasesReceive?wsdl";
        try {
            SendDataServiceStub stub = new SendDataServiceStub(uri);

            SendDataServiceStub.ArchiveCase archiveCase = new SendDataServiceStub.ArchiveCase();

            org.apache.axis2.databinding.types.soapencoding.String requestXml = new org.apache.axis2.databinding.types.soapencoding.String();
            System.out.println(xml);
            requestXml.setString(xml);

            archiveCase.setRequestXml(requestXml);

            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(3000000);//设置超时(单位是毫秒)
            stub._getServiceClient().setOptions(options);

            String result = stub.archiveCase(archiveCase).getArchiveCaseReturn().toString();
            System.out.println(result);

            return result;
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void reject(){
        System.out.println("获取驳回事件");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("descPlatform", BIZ_PLATFORM);
        params.put("srcPlatform", "000");
        params.put("operateType", "2");
        List<Map<String, Object>> subTaskAppointed = this.dataExchangeStatusService.findTaskEvent(params);
        if(subTaskAppointed!=null&&subTaskAppointed.size()>0) {
            for (Map<String, Object> task : subTaskAppointed) {
                System.out.println(task);
                String oppo_side_biz_code = (String)task.get("OPPO_SIDE_BIZ_CODE");
                String event_id = task.get("EVENT_ID").toString();
                String remarks = (String)task.get("REMARKS");
                java.math.BigDecimal task_id = (java.math.BigDecimal)task.get("TASK_ID");
                TIMESTAMP end_time = (TIMESTAMP)task.get("END_TIME");
                String end_time_str = null;
                try {
                    end_time_str = df.format(end_time.dateValue());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                StringBuffer sb = new StringBuffer();
                sb.append("<request>");
                sb.append("<params>");
                sb.append("<RecID>"+oppo_side_biz_code+"</RecID>");
                sb.append("<TransOpinion>"+remarks+"</TransOpinion>");
                sb.append("<TransTime>"+end_time_str+"</TransTime>");
                sb.append("</params>");
                sb.append("</request>");

                String result = callbackCase(sb.toString());
                if(StringUtils.isNotBlank(result)){
                    String errorCode = result.substring(result.indexOf("<errorCode>") + 11, result.indexOf("</errorCode>"));
                    if(errorCode.equals("0")){
                        DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                        dataExchangeStatus.setExchangeFlag("1");
                        dataExchangeStatus.setOppoSideBizCode(oppo_side_biz_code);
                        dataExchangeStatus.setOppoSideBizType("3");
                        dataExchangeStatus.setOwnSideBizCode(task_id.toString());
                        dataExchangeStatus.setOwnSideBizType("3");
                        dataExchangeStatus.setStatus("1");
                        dataExchangeStatus.setXmlData(result);
                        dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                        dataExchangeStatus.setSrcPlatform("000");
                        this.dataExchangeStatusService.save(dataExchangeStatus);
                    }
                }
            }
        }
    }

    private void reportTask(){
        System.out.println("获取过程、结案事件");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("srcPlatform", BIZ_PLATFORM);
        params.put("descPlatform", "000");
        params.put("operateType", "1");
        List<Map<String, Object>> subTaskAppointed = this.dataExchangeStatusService.findTaskEvent(params);
        if(subTaskAppointed!=null&&subTaskAppointed.size()>0){
            for (Map<String, Object> task : subTaskAppointed){
                System.out.println(task);
                String event_id = task.get("EVENT_ID").toString();
                String taskName = (String)task.get("TASK_NAME");
                String oppo_side_biz_code = (String)task.get("OPPO_SIDE_BIZ_CODE");
                String remarks = (String)task.get("REMARKS");
                String pre_user_id = task.get("PRE_USER_ID").toString();
                TIMESTAMP end_time = (TIMESTAMP)task.get("END_TIME");
                java.math.BigDecimal task_id = (java.math.BigDecimal)task.get("TASK_ID");
                String end_time_str = null;
                try {
                    end_time_str = df.format(end_time.dateValue());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if(!taskName.equals("task8")){//过程
                    System.out.println("过程");
                    StringBuffer sb = new StringBuffer();
                    sb.append("<request>");
                    sb.append("<params>");
                    sb.append("<RecID>"+oppo_side_biz_code+"</RecID>");
                    sb.append("<TransOpinion>"+remarks+"</TransOpinion>");
                    sb.append("<TransTime>"+end_time_str+"</TransTime>");
                    sb.append("<transStatus>6</transStatus>");
                    sb.append("</params>");
                    sb.append("</request>");
                    String result = feedbackResult(sb.toString());
                    String errorCode = result.substring(result.indexOf("<errorCode>") + 11, result.indexOf("</errorCode>"));
//                    if(errorCode.equals("0")){
                        DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                        dataExchangeStatus.setExchangeFlag("1");
                        dataExchangeStatus.setOppoSideBizCode(oppo_side_biz_code);
                        dataExchangeStatus.setOppoSideBizType("3");
                        dataExchangeStatus.setOwnSideBizCode(task_id.toString());
                        dataExchangeStatus.setOwnSideBizType("3");
                        dataExchangeStatus.setStatus("1");
                        dataExchangeStatus.setXmlData(result);
                        dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                        dataExchangeStatus.setSrcPlatform("000");
                        this.dataExchangeStatusService.save(dataExchangeStatus);
//                    }
                }else{//结案
                    System.out.println("结案");
                    String reportCloseUserName = "";
                    UserInfo userExtraInfoByUserId = this.userService.getUserExtraInfoByUserId(Long.valueOf(pre_user_id), null);
                    if(userExtraInfoByUserId != null){
                        reportCloseUserName = userExtraInfoByUserId.getPartyName();
                    }

                    StringBuffer sb = new StringBuffer();
                    sb.append("<request>");
                    sb.append("<params>");
                    sb.append("<RecID>"+oppo_side_biz_code+"</RecID>");
                    sb.append("<CloseTime>"+end_time_str+"</CloseTime>");
                    sb.append("<ReportCloseDesc>"+remarks+"</ReportCloseDesc>");
                    sb.append("<ReportCloseUserName>"+reportCloseUserName+"</ReportCloseUserName>");
                    sb.append("</params>");
                    sb.append("</request>");
                    String result = closeCaseResult(sb.toString());
                    String errorCode = result.substring(result.indexOf("<errorCode>") + 11, result.indexOf("</errorCode>"));
                    DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                    dataExchangeStatus.setExchangeFlag("1");
                    dataExchangeStatus.setOppoSideBizCode(oppo_side_biz_code);
                    dataExchangeStatus.setOppoSideBizType("3");
                    dataExchangeStatus.setOwnSideBizCode(task_id.toString());
                    dataExchangeStatus.setOwnSideBizType("3");
                    dataExchangeStatus.setStatus("1");
                    dataExchangeStatus.setXmlData(result);
                    dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                    dataExchangeStatus.setSrcPlatform("000");
                    this.dataExchangeStatusService.save(dataExchangeStatus);
                }
            }
        }
    }



    private void reportEvent(String GANSU_user_id, String GANSU_org_id){
        System.out.println("获取上报事件");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", GANSU_user_id);
        params.put("orgId", GANSU_org_id);
        params.put("bizPlatform", BIZ_PLATFORM);
        List<Map<String, Object>> reportEvent = this.dataExchangeStatusService.findReportEvent(params);
        if(reportEvent!=null&&reportEvent.size()>0){
            for(Map<String, Object> map : reportEvent){
                System.out.println(map);
                String event_name = (String)map.get("EVENT_NAME");
                String contact_user = (String)map.get("CONTACT_USER");
                String tel = (String)map.get("TEL");
                String occurred = (String)map.get("OCCURRED");
                String content_ = (String)map.get("CONTENT_");
                java.sql.Timestamp create_time = (java.sql.Timestamp)map.get("CREATE_TIME");
                String create_time_str = df.format(create_time);

                String event_id = map.get("EVENT_ID").toString();
                StringBuffer sb = new StringBuffer();
                sb.append("<request>");
                sb.append("<params>");
                sb.append("<RecID>"+event_id+"</RecID>");
                sb.append("<EventTypeName>事件</EventTypeName>");
                sb.append("<RecTypeName>城市管理类</RecTypeName>");
                sb.append("<MainTypeName>市容环境</MainTypeName>");
                sb.append("<SubTypeName>其它市容环境问题</SubTypeName>");
                sb.append("<EventSrcName>社会公众举报</EventSrcName>");

                sb.append("<ReportName>"+contact_user+"</ReportName>");
                sb.append("<ReportContact>"+tel+"</ReportContact>");
                sb.append("<EventDesc>"+content_+"</EventDesc>");
                sb.append("<Address>"+occurred+"</Address>");
                sb.append("<CoordinateX>-1</CoordinateX>");
                sb.append("<CoordinateY>-1</CoordinateY>");

                sb.append("<DistrictName>兰州市</DistrictName>");
                sb.append("<StreetName>兰州市</StreetName>");
                sb.append("<CellCode>620102</CellCode>");
//                sb.append("<CommunitName>畅家巷社区</CommunitName>");
                sb.append("<AcceptTime>"+create_time_str+"</AcceptTime>");
                sb.append("</params>");
                sb.append("</request>");
//                String result = "<request><commonResult><errorCode>782507</errorCode><errorDesc>上报案卷到城管成功！</errorDesc></commonResult></request>";
                String result = process(sb.toString());
                String outEventId = result.substring(result.indexOf("<errorCode>") + 11, result.indexOf("</errorCode>"));
                System.out.println(outEventId);
                if(!outEventId.equals("0")){
                    DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                    dataExchangeStatus.setExchangeFlag("1");
                    dataExchangeStatus.setOppoSideBizCode(outEventId);
                    dataExchangeStatus.setOppoSideBizType("2");
                    dataExchangeStatus.setOwnSideBizCode(event_id);
                    dataExchangeStatus.setOwnSideBizType("2");
                    dataExchangeStatus.setStatus("1");
                    dataExchangeStatus.setXmlData(result);
                    dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                    dataExchangeStatus.setSrcPlatform("000");
                    this.dataExchangeStatusService.save(dataExchangeStatus);
                }
            }
        }
    }

    public String closeCaseResult(String xml){
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
            SendDataServiceStub stub = new SendDataServiceStub(uri);

            SendDataServiceStub.CloseCaseResult closeCaseResult = new SendDataServiceStub.CloseCaseResult();

            org.apache.axis2.databinding.types.soapencoding.String requestXml = new org.apache.axis2.databinding.types.soapencoding.String();

            System.out.println(xml);
            requestXml.setString(xml);

            closeCaseResult.setRequestXml(requestXml);

            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(3000000);//设置超时(单位是毫秒)
            stub._getServiceClient().setOptions(options);

            SendDataServiceStub.CloseCaseResultResponse closeCaseResultResponse = stub.closeCaseResult(closeCaseResult);

            System.out.println(closeCaseResultResponse.getCloseCaseResultReturn());
            return closeCaseResultResponse.getCloseCaseResultReturn().toString();
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String feedbackResult(String xml){
        StringBuffer sb = new StringBuffer();
        sb.append("<request>");
        sb.append("<params>");
        sb.append("<RecID>786563</RecID>");
        sb.append("<TransOpinion>综治平台处理意见</TransOpinion>");
        sb.append("<TransTime>2017-06-29 16:47:23</TransTime>");
        sb.append("<transStatus>6</transStatus>");
        sb.append("</params>");
        sb.append("</request>");


        try {
            SendDataServiceStub stub = new SendDataServiceStub(uri);

            SendDataServiceStub.FeedbackResult feedbackResult = new SendDataServiceStub.FeedbackResult();

            org.apache.axis2.databinding.types.soapencoding.String requestXml = new org.apache.axis2.databinding.types.soapencoding.String();
            System.out.println(xml);
            requestXml.setString(xml);

            feedbackResult.setRequestXml(requestXml);

            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(3000000);//设置超时(单位是毫秒)
            stub._getServiceClient().setOptions(options);

            SendDataServiceStub.FeedbackResultResponse feedbackResultResponse = stub.feedbackResult(feedbackResult);

            System.out.println(feedbackResultResponse.getFeedbackResultReturn());
            return feedbackResultResponse.getFeedbackResultReturn().toString();
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String callbackCase(String xml){
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
            System.out.println(xml);
            requestXml.setString(xml);

            callbackCase.setRequestXml(requestXml);

            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(3000000);//设置超时(单位是毫秒)
            stub._getServiceClient().setOptions(options);

            String callbackCaseReturn = stub.callbackCase(callbackCase).getCallbackCaseReturn().toString();
            System.out.println(callbackCaseReturn);
            return callbackCaseReturn;
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String process(String xml){
        StringBuffer sb = new StringBuffer();
        sb.append("<request>");
        sb.append("<params>");
        sb.append("<RecID>13267</RecID>");
        sb.append("<EventTypeName>事件</EventTypeName>");
        sb.append("<RecTypeName>城市管理类</RecTypeName>");
        sb.append("<MainTypeName>市容环境</MainTypeName>");
        sb.append("<SubTypeName>其它市容环境问题</SubTypeName>");
        sb.append("<EventSrcName>社会公众举报</EventSrcName>");

        sb.append("<EventDesc>乱倒垃圾</EventDesc>");
        sb.append("<Address>街道</Address>");
        sb.append("<CoordinateX>-1</CoordinateX>");
        sb.append("<CoordinateY>-1</CoordinateY>");

        sb.append("<DistrictName>兰州市</DistrictName>");
        sb.append("<StreetName>兰州市城管区酒泉路街道</StreetName>");
        sb.append("<CellCode>620102</CellCode>");
        sb.append("<CommunitName>畅家巷社区</CommunitName>");
        sb.append("<AcceptTime>2015-05-04 09:06:03</AcceptTime>");
        sb.append("</params>");
        sb.append("</request>");

        try {
            SendDataServiceStub stub = new SendDataServiceStub(uri);
            //端口映射
//            stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,Boolean.FALSE);
            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(3000000);//设置超时(单位是毫秒)
            stub._getServiceClient().setOptions(options);
            SendDataServiceStub.Process process = new SendDataServiceStub.Process();
            org.apache.axis2.databinding.types.soapencoding.String requestXml = new org.apache.axis2.databinding.types.soapencoding.String();
            System.out.println(xml);
            requestXml.setString(xml);
            process.setRequestXml(requestXml);
            SendDataServiceStub.ProcessResponse processResponse = stub.process(process);
            System.out.println(processResponse.getProcessReturn());
            org.apache.axis2.databinding.types.soapencoding.String result = processResponse.getProcessReturn();


            return result.toString();

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

}
