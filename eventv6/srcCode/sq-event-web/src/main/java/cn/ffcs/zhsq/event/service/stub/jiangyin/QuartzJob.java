package cn.ffcs.zhsq.event.service.stub.jiangyin;/**
 * Created by Administrator on 2017/7/4.
 */

import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongshm
 * @create 2017-07-04 15:28
 **/
public class QuartzJob implements Job {

    private final Logger logger = LoggerFactory.getLogger("dailyRollingFile");
    private IDataExchangeStatusService dataExchangeStatusService;
    private IDataExchangeStatusTwoWayService iDataExchangeStatusTwoWayService;
    private IFunConfigurationService funConfigurationService;
    private IAttachmentService attachmentService;
    protected IMixedGridInfoService mixedGridInfoService;
    private IEventDisposalWorkflowService eventDisposalWorkflowService;

    //业务编码
    private final String BIZ_PLATFORM = "018";

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String URI = "http://58.214.254.6:9696/12345/WebService/ThirdService2.asmx?wsdl";
    private String FILE_URI = "http://58.214.254.6:9696/12345/WebService/FileService.asmx?wsdl";
    private String jiangyin_org_id = "";
    private String jiangyin_user_id = "";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("=========" + DateUtils.getNow() + " start 江阴事件对接==========");
        IDataExchangeStatusService dataExchangeStatusService = null;
        try {
            SchedulerContext schCtx = context.getScheduler().getContext();
            ApplicationContext appCtx = (ApplicationContext) schCtx.get("applicationContext");
            this.attachmentService = (IAttachmentService) appCtx.getBean("attachmentService");
            this.dataExchangeStatusService = (IDataExchangeStatusService) appCtx.getBean("dataExchangeStatusServiceImpl");
            this.iDataExchangeStatusTwoWayService = (IDataExchangeStatusTwoWayService) appCtx.getBean("dataExchangeStatusTwoWayServiceImpl");
            this.funConfigurationService = (IFunConfigurationService) appCtx.getBean("funConfigurationService");
            this.mixedGridInfoService = (IMixedGridInfoService) appCtx.getBean("mixedGridInfoService");
            this.eventDisposalWorkflowService = (IEventDisposalWorkflowService) appCtx.getBean("eventDisposalWorkflowServiceImpl");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        String jiangyin_uri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "JIANGYIN_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        FILE_URI = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "JIANGYIN_FILE_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        jiangyin_org_id = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "JIANGYIN_ORG_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        jiangyin_user_id = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "JIANGYIN_USER_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);

        if(StringUtils.isBlank(jiangyin_uri)){
            logger.error("功能配置【JIANGYIN_URI】未配置！");
            return;
        }else
            this.URI = jiangyin_uri;
        if(StringUtils.isBlank(jiangyin_org_id)){
            logger.error("功能配置【JIANGYIN_ORG_ID】未配置！");
            return;
        }
        if(StringUtils.isBlank(jiangyin_user_id)){
            logger.error("功能配置【JIANGYIN_USER_ID】未配置！");
            return;
        }

        //驳回上报事件
        rejectEvent();

        //获取上报事件
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", jiangyin_user_id);
        params.put("orgId", jiangyin_org_id);
        params.put("bizPlatform", BIZ_PLATFORM);
        List<Map<String, Object>> reportEvent = this.dataExchangeStatusService.findReportEvent(params);
        if (reportEvent != null) {
            for (Map<String, Object> map : reportEvent) {
                System.out.println(map);
                //{EVENT_NAME=事件名称4, INVOLVED_MONEY=0, CODE_=20170620_0701_00007, CREATE_TIME=2017-06-20 20:41:03.0, CONTACT_USER=匿名, GRID_CODE=620102, START_TIME=2017-06-20 20:02:43.0, UPDATE_TIME=2017-06-20 20:41:51.0, OCCURRED=陈埭镇坊脚村委会盛兴路一工地, STATUS=02, HANDLE_DATE_FLAG=3, TYPE_=0701, HAPPEN_TIME=2017-07-31 03:47:50.0, ATTR_FLAG=0,, EVENT_ID=8769, TEL=15359469224, GRID_ID=423137, BIZ_PLATFORM=016, CONTENT_=报称：在陈埭坊脚盛兴路一工地，老板拖欠其5000元工资。, URGENCY_DEGREE=01, HANDLE_DATE=2017-06-30 20:02:43.0}
                String event_name = (String)map.get("EVENT_NAME");
                String grid_code = (String)map.get("GRID_CODE");
                BigDecimal instanceId = (BigDecimal)map.get("INSTANCE_ID");
                MixedGridInfo defaultGridByOrgCode = mixedGridInfoService.getDefaultGridByOrgCode(grid_code);
                String gridPath = "";
                if(defaultGridByOrgCode!=null){
                    gridPath = defaultGridByOrgCode.getGridPath();
                }

                String remark = "";
                List<Map<String, Object>> taskList = eventDisposalWorkflowService.queryProInstanceDetail(instanceId.longValue(), IEventDisposalWorkflowService.SQL_ORDER_DESC);
                for(Map<String, Object> task : taskList){
                    System.out.println(task);
                }
                if(taskList.get(1).get("REMARKS")!=null){
                    remark = taskList.get(1).get("REMARKS").toString();
                }


                String thirdNumber = "";
//                if(map.get("OWN_SIDE_BIZ_CODE")!= null){
//                    thirdNumber = map.get("OWN_SIDE_BIZ_CODE").toString();
//                }

                String contact_user = (String)map.get("CONTACT_USER");
                String tel = (String)map.get("TEL");
                String occurred = (String)map.get("OCCURRED");
                String content_ = (String)map.get("CONTENT_");
                Timestamp create_time = (Timestamp)map.get("CREATE_TIME");
                String create_time_str = df.format(create_time);
                Timestamp handleDate = (Timestamp)map.get("HANDLE_DATE");
                String handleDateStr = df.format(handleDate);
                Timestamp happen_time = (Timestamp)map.get("HAPPEN_TIME");
                String happen_time_str = df.format(happen_time);
                String event_id = map.get("EVENT_ID").toString();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ThirdNumber", thirdNumber);
                jsonObject.put("CaseSourceID", 18);
                jsonObject.put("CaseTypeID", 6);
                jsonObject.put("WantPerson", contact_user);
                jsonObject.put("WantPersonTel", tel);
                jsonObject.put("Title", event_name);
                jsonObject.put("Village", "320281");
                jsonObject.put("Address", gridPath);
                jsonObject.put("WantContent", "于"+happen_time_str+",在"+occurred+"发生"+content_);
                jsonObject.put("WantDate", create_time_str);//提交日期
                jsonObject.put("State", 0);
                jsonObject.put("SourceNumber", event_id);
                jsonObject.put("Result", "");
                jsonObject.put("FinishDate", "");
                jsonObject.put("ReplyContent", "");
                jsonObject.put("remark", "办理意见"+remark+",办理截止时间:【"+handleDateStr+"】");//办理意见*****,办理截止时间:【yyyy-MM-dd hh:mm】

                //附件
                if(StringUtils.isNotBlank(FILE_URI)){
                    List<Attachment> attachments = attachmentService.findByBizId(Long.valueOf(event_id), ConstantValue.EVENT_ATTACHMENT_TYPE);
                    if(attachments!=null && attachments.size()> 0){
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i< attachments.size();i++){
                            Attachment attachment = attachments.get(i);
                            String fileName = attachment.getFileName();
                            String filePath = attachment.getFilePath();
                            String fileResult = exchangeFile(fileName,filePath);
                            if(StringUtils.isNotBlank(fileResult)){
                                DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                                dataExchangeStatus.setExchangeFlag("1");
                                dataExchangeStatus.setOppoSideBizCode("");
                                dataExchangeStatus.setOwnSideBizCode(event_id);
                                dataExchangeStatus.setStatus("1");
                                dataExchangeStatus.setXmlData(fileResult);
                                dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                                dataExchangeStatus.setSrcPlatform("000");
                                dataExchangeStatus.setOppoSideBizType("0");
                                dataExchangeStatus.setOwnSideBizType("0");
                                this.dataExchangeStatusService.save(dataExchangeStatus);
                            }

                            JSONObject fileJsonObject = new JSONObject();
                            fileJsonObject.put("FileNumber",fileResult);
                            fileJsonObject.put("FileName",fileName);
                            fileJsonObject.put("ThirdNumber","");
                            fileJsonObject.put("AttachNumber","");
                            fileJsonObject.put("OrderValue",i + 1);
                            fileJsonObject.put("UploadDate",attachment.getCreateTimeStr());
                            jsonArray.add(fileJsonObject);
                        }
                        jsonObject.put("Attachs",jsonArray);
                    }
                }

                System.out.println(jsonObject.toString());
//                String result = "{\"Success\":false,\"Number\":\"{1}\"}";
                String result = exchange(jsonObject.toString());
                JSONObject resultObj = JSONObject.fromObject(result);
                Boolean success = (Boolean)resultObj.get("Success");
                if(success){
                    String outEventId = (String)resultObj.get("Number");
                    DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                    dataExchangeStatus.setExchangeFlag("1");
                    dataExchangeStatus.setOppoSideBizCode(outEventId);
                    dataExchangeStatus.setOwnSideBizCode(event_id);
                    dataExchangeStatus.setStatus("1");
                    dataExchangeStatus.setXmlData(result+"|"+jsonObject);
                    dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                    dataExchangeStatus.setSrcPlatform("000");

                    dataExchangeStatus.setOppoSideBizType("2");
                    dataExchangeStatus.setOwnSideBizType("2");
                    this.dataExchangeStatusService.save(dataExchangeStatus);
                }else{
                    DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                    dataExchangeStatus.setExchangeFlag("1");
                    dataExchangeStatus.setOppoSideBizCode("");
                    dataExchangeStatus.setOwnSideBizCode(event_id);
                    dataExchangeStatus.setStatus("1");
                    dataExchangeStatus.setXmlData(result+"|"+jsonObject);
                    dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                    dataExchangeStatus.setSrcPlatform("000");
                    dataExchangeStatus.setOppoSideBizType("2");
                    dataExchangeStatus.setOwnSideBizType("2");
                    this.dataExchangeStatusService.save(dataExchangeStatus);
                }

            }
        }
        logger.info("=========" + DateUtils.getNow() + " end 江阴事件对接==========");
    }

    private void rejectEvent(){
        List<Long> userIds = new ArrayList<Long>();
        Map<String, Object> params = new HashMap<String, Object>();
        userIds.add(Long.valueOf(jiangyin_user_id));
        params.put("userIds", userIds);
        params.put("bizPlatform", BIZ_PLATFORM);
        List<Map<String, Object>> reportEvent = this.dataExchangeStatusService.findRejectEvent(params);
        System.out.println(reportEvent);

        if (reportEvent != null) {
            for (Map<String, Object> map : reportEvent) {
                System.out.println(map);
                BigDecimal taskId = (BigDecimal)map.get("TASK_ID");
                String event_name = (String)map.get("EVENT_NAME");
                String grid_code = (String)map.get("GRID_CODE");
                MixedGridInfo defaultGridByOrgCode = mixedGridInfoService.getDefaultGridByOrgCode(grid_code);
                String gridPath = "";
                if(defaultGridByOrgCode!=null){
                    gridPath = defaultGridByOrgCode.getGridPath();
                }

                String thirdNumber = "";
                String remarks = "";
                if(map.get("OWN_SIDE_BIZ_CODE")!= null){
                    thirdNumber = map.get("OWN_SIDE_BIZ_CODE").toString();
                    remarks = map.get("REMARKS").toString();
                }

                String contact_user = (String)map.get("CONTACT_USER");
                String tel = (String)map.get("TEL");
                String occurred = (String)map.get("OCCURRED");
                String content_ = (String)map.get("CONTENT_");
                Timestamp create_time = (Timestamp)map.get("CREATE_TIME");
                String create_time_str = df.format(create_time);
                Timestamp happen_time = (Timestamp)map.get("HAPPEN_TIME");
                String happen_time_str = df.format(happen_time);
                String event_id = map.get("EVENT_ID").toString();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ThirdNumber", "");
                jsonObject.put("CaseSourceID", 18);
                jsonObject.put("CaseTypeID", 6);
                jsonObject.put("WantPerson", contact_user);
                jsonObject.put("WantPersonTel", tel);
                jsonObject.put("Title", event_name);
                jsonObject.put("Village", "320281");
                jsonObject.put("Address", gridPath);
                jsonObject.put("WantContent", "于"+happen_time_str+",在"+occurred+"发生"+content_);
                jsonObject.put("WantDate", create_time_str);//提交日期
                jsonObject.put("State", 0);
                jsonObject.put("SourceNumber", event_id);
                jsonObject.put("Result", "");
                jsonObject.put("Remark", remarks);
                jsonObject.put("FinishDate", "");
                jsonObject.put("ReplyContent", "");
                jsonObject.put("ReplyContent", "");

                //附件
                if(StringUtils.isNotBlank(FILE_URI)){
                    List<Attachment> attachments = attachmentService.findByBizId(Long.valueOf(event_id), ConstantValue.EVENT_ATTACHMENT_TYPE);
                    if(attachments!=null && attachments.size()> 0){
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i< attachments.size();i++){
                            Attachment attachment = attachments.get(i);
                            String fileName = attachment.getFileName();
                            String filePath = attachment.getFilePath();
                            String fileResult = exchangeFile(fileName,filePath);
                            if(StringUtils.isNotBlank(fileResult)){
                                DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                                dataExchangeStatus.setExchangeFlag("1");
                                dataExchangeStatus.setOppoSideBizCode("");
                                dataExchangeStatus.setOwnSideBizCode(event_id);
                                dataExchangeStatus.setStatus("1");
                                dataExchangeStatus.setXmlData(fileResult);
                                dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                                dataExchangeStatus.setSrcPlatform("000");
                                dataExchangeStatus.setOppoSideBizType("0");
                                dataExchangeStatus.setOwnSideBizType("0");
                                this.dataExchangeStatusService.save(dataExchangeStatus);
                            }

                            JSONObject fileJsonObject = new JSONObject();
                            fileJsonObject.put("FileNumber",fileResult);
                            fileJsonObject.put("FileName",fileName);
                            fileJsonObject.put("ThirdNumber","");
                            fileJsonObject.put("AttachNumber","");
                            fileJsonObject.put("OrderValue",i + 1);
                            fileJsonObject.put("UploadDate",attachment.getCreateTimeStr());
                            jsonArray.add(fileJsonObject);
                        }
                        jsonObject.put("Attachs",jsonArray);
                    }
                }
                System.out.println(jsonObject.toString());
//                String result = "{\"Success\":false,\"Number\":\"{1}\"}";
                String result = exchange(jsonObject.toString());
                JSONObject resultObj = JSONObject.fromObject(result);
                Boolean success = (Boolean)resultObj.get("Success");
                DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                if(success){
                    String outEventId = (String)resultObj.get("Number");
                    dataExchangeStatus.setOppoSideBizCode(outEventId);
                }
                dataExchangeStatus.setExchangeFlag("1");
                dataExchangeStatus.setOwnSideBizCode(String.valueOf(taskId.longValue()));
                dataExchangeStatus.setStatus("1");
                dataExchangeStatus.setXmlData(result+"|"+jsonObject);
                dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                dataExchangeStatus.setSrcPlatform("000");
                dataExchangeStatus.setOppoSideBizType("3");
                dataExchangeStatus.setOwnSideBizType("3");
                this.dataExchangeStatusService.save(dataExchangeStatus);

            }
        }
    }

    private String exchangeFile(String fileName, String filePath){
        String result = "";
        String img_domain = funConfigurationService.changeCodeToValue(ConstantValue.APP_DOMAIN, "$IMG_DOMAIN", IFunConfigurationService.CFG_TYPE_URL);
        logger.error(img_domain);
        System.out.println(img_domain);
        try {
            FileServiceStub stub = new FileServiceStub(FILE_URI);
            FileServiceStub.UploadThirdFile uploadThirdFile = new FileServiceStub.UploadThirdFile();
            uploadThirdFile.setCaseSourceID(18);
            uploadThirdFile.setSessionID("V0dIMTg=");
            uploadThirdFile.setCaseSourceName("WGH");
            uploadThirdFile.setFileName(fileName);
            uploadThirdFile.setFilepath(img_domain+filePath);
            System.out.println(img_domain+filePath);
            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(3000000);//设置超时(单位是毫秒)
            stub._getServiceClient().setOptions(options);

            //端口映射
            stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,Boolean.FALSE);

            FileServiceStub.UploadThirdFileResponse uploadThirdFileResponse = stub.uploadThirdFile(uploadThirdFile);
            System.out.println(uploadThirdFileResponse.getUploadThirdFileResult());
            result =uploadThirdFileResponse.getUploadThirdFileResult();
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
        return  result;
    }

    private String exchange(String strThird) {
        logger.info(strThird);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("ThirdNumber", "");
//        jsonObject.put("CaseSourceID", 18);
//        jsonObject.put("CaseTypeID", 6);
//        jsonObject.put("WantPerson", "街道");
//        jsonObject.put("WantPersonTel ", "18321300820");
//        jsonObject.put("Title", "江阴测试数据");
//        jsonObject.put("Village", "320281");
//        jsonObject.put("Address", "江阴测试数据地址");
//        jsonObject.put("WantContent", "江阴测试数据描述");
//        jsonObject.put("WantDate", "2017-06-30 09:58:08");//提交日期
//        jsonObject.put("State", 0);
//        jsonObject.put("SourceNumber", "111864");
//        jsonObject.put("Result", "");
//        jsonObject.put("FinishDate", "");
//        jsonObject.put("ReplyContent", "");
//        System.out.println(jsonObject.toString());
        String result = "";
        try {
            ThirdService2Stub stub = new ThirdService2Stub(URI);
            ThirdService2Stub.SubmitThird submitThird = new ThirdService2Stub.SubmitThird();
            submitThird.setCaseSourceID(18);
            submitThird.setSessionID("V0dIMTg=");
            submitThird.setCaseSourceName("WGH");
            submitThird.setStrThird(strThird);

            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(3000000);//设置超时(单位是毫秒)
            stub._getServiceClient().setOptions(options);

            //端口映射
            stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);

            ThirdService2Stub.SubmitThirdResponse submitThirdResponse = stub.submitThird(submitThird);
            logger.info(submitThirdResponse.getSubmitThirdResult());

            result = submitThirdResponse.getSubmitThirdResult();

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }
}
