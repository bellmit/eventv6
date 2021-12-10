package cn.ffcs.zhsq.event.service.stub.jiangyin.XunfangApp;/**
 * Created by Administrator on 2018/3/6.
 */

import cn.ffcs.common.FileUtils;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.utils.Base64;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import sun.misc.BASE64Encoder;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 巡防app
 *
 * @author zhongshm
 * @create 2018-03-06 9:07
 **/
public class QuartzJob implements Job {
    private final Logger logger = LoggerFactory.getLogger("dailyRollingFile");
    private IDataExchangeStatusService dataExchangeStatusService;
    private MessagePushStub messagePushStub;
    private IFunConfigurationService funConfigurationService;
    private UserManageOutService userManageService;
    private IEventDisposalWorkflowService eventDisposalWorkflowService;
    private IResMarkerService resMarkerService;
    private OrgSocialInfoOutService orgSocialInfoService;

    private IAttachmentService attachmentService;

    //业务编码
    private final String BIZ_PLATFORM = "042";
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private String jiangyin_img_uri = "";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SchedulerContext schCtx = null;
        try {
            schCtx = context.getScheduler().getContext();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        ApplicationContext appCtx = (ApplicationContext) schCtx.get("applicationContext");
        this.attachmentService = (IAttachmentService) appCtx.getBean("attachmentService");
        this.dataExchangeStatusService = (IDataExchangeStatusService) appCtx.getBean("dataExchangeStatusServiceImpl");
        this.messagePushStub = (MessagePushStub) appCtx.getBean("messagePushStub");
        this.userManageService = (UserManageOutService) appCtx.getBean("userManageService");
        this.funConfigurationService = (IFunConfigurationService) appCtx.getBean("funConfigurationService");
        this.eventDisposalWorkflowService = (IEventDisposalWorkflowService) appCtx.getBean("eventDisposalWorkflowServiceImpl");
        this.resMarkerService = (IResMarkerService) appCtx.getBean("resMarkerService");
        this.orgSocialInfoService = (OrgSocialInfoOutService) appCtx.getBean("orgSocialInfoService");


        jiangyin_img_uri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_IMG_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        String xfappRoleId = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "XFAPP_ROLE_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        if(StringUtils.isBlank(xfappRoleId)){
            logger.error("功能配置-巡防APP对接角色【XFAPP_ROLE_ID】未配置！");return;
        }
        List<UserBO> listByUserExampleParamsOut = userManageService.getUserListByUserExampleParamsOut(Long.valueOf(xfappRoleId), null, null);
        if(listByUserExampleParamsOut.size() > 1000){
            logger.error("功能配置-巡防APP对接角色【XFAPP_ROLE_ID】配置用户超过1000！");return;
        }
        List<Long> userIds = new ArrayList<Long>();
        for(int i=0;i<listByUserExampleParamsOut.size();i++){
            userIds.add(listByUserExampleParamsOut.get(i).getUserId());
        }
//        userIds.add(184L);
        rejectEvt(userIds);
        startEvt(userIds);//上报
        closeEvt();

    }

    private void rejectEvt(List<Long> userIds){
        logger.info("======================rejectEvt=======================");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userIds", userIds);
        params.put("bizPlatform", BIZ_PLATFORM);
        List<Map<String, Object>> rejectEvent = this.dataExchangeStatusService.findRejectEvent(params);
        if(rejectEvent != null && rejectEvent.size() > 0){
            for(Map<String, Object> reject : rejectEvent){
                System.out.println(reject);
                logger.info(reject.toString());

                String operateType = reject.get("OPERATE_TYPE").toString();
                if(StringUtils.isNotBlank(operateType) && operateType.equals("1")){//下派

                    String taskId = reject.get("TASK_ID").toString();
                    String eventId = reject.get("EVENT_ID").toString();
                    String gridCode = reject.get("GRID_CODE").toString();
                    String eventName = reject.get("EVENT_NAME").toString();
                    String occurred = reject.get("OCCURRED").toString();
                    String content = reject.get("CONTENT_").toString();
                    String influenceDegree = reject.get("INFLUENCE_DEGREE").toString();
                    String urgencyDegree = reject.get("URGENCY_DEGREE").toString();
                    String tel = reject.get("TEL").toString();
                    String contactUser = reject.get("CONTACT_USER").toString();
//                    String source = reject.get("SOURCE").toString();
                    String remarks = reject.get("REMARKS").toString();
                    String orgId = reject.get("ORG_ID").toString();
                    String instanceId = reject.get("TRANSACTOR_ID").toString();

                    String happenTimeStr = "";
                    if(reject.get("HAPPEN_TIME")!=null){
                        Timestamp happenTime = (Timestamp)reject.get("HAPPEN_TIME");
                        happenTimeStr = df.format(happenTime);
                    }
                    String handleDateStr = "";
                    if(reject.get("HANDLE_DATE")!=null){
                        Timestamp handleDate = (Timestamp)reject.get("HANDLE_DATE");
                        handleDateStr = df.format(handleDate);
                    }

                    String createTimeStr = "";
                    if(reject.get("CREATE_TIME") != null){
                        Timestamp createTime = (Timestamp)reject.get("CREATE_TIME");
                        createTimeStr = df.format(createTime);
                    }

                    String orgCode = "";
                    OrgSocialInfoBO socialInfoBO = orgSocialInfoService.findByOrgId(Long.valueOf(orgId));
                    if(null != socialInfoBO){
                        orgCode = socialInfoBO.getOrgCode();
                    }

                    //获取经纬度
                    ResMarker resMarker = resMarkerService.findResMarkerByResId("0301", Long.valueOf(eventId), "5");
                    String x = "";
                    String y = "";
                    if(resMarker != null){
                        x = resMarker.getX();
                        y = resMarker.getY();
                    }

                    StringBuffer dataStr = new StringBuffer();
                    dataStr.append("<data>");
                    dataStr.append("<auth><username></username><password></password></auth>");
                    dataStr.append("<event>");
                    dataStr.append("<eventId>").append(eventId).append("</eventId>");
                    dataStr.append("<gridCode>").append(gridCode).append("</gridCode>");
                    dataStr.append("<orgCode>").append(orgCode).append("</orgCode>");
                    dataStr.append("<eventName>").append(eventName).append("</eventName>");
                    dataStr.append("<happenTimeStr>").append(happenTimeStr).append("</happenTimeStr>");
                    dataStr.append("<occurred>").append(occurred).append("</occurred>");
                    dataStr.append("<content>").append(content).append("</content>");
                    dataStr.append("<handleDate>").append(handleDateStr).append("</handleDate>");
                    dataStr.append("<oppoSideBusiCode>").append(eventId).append("</oppoSideBusiCode>");
                    dataStr.append("<bizPlatform>").append(BIZ_PLATFORM).append("</bizPlatform>");
                    dataStr.append("<eventType>").append("1").append("</eventType>");
                    dataStr.append("<longitude>").append(x).append("</longitude>");
                    dataStr.append("<latitude>").append(y).append("</latitude>");
                    dataStr.append("<urgency>").append(urgencyDegree).append("</urgency>");
                    dataStr.append("<influence>").append(influenceDegree).append("</influence>");
                    dataStr.append("<source>").append("").append("</source>");
                    dataStr.append("<creatorName>").append("").append("</creatorName>");
                    dataStr.append("<contactUserName>").append(contactUser).append("</contactUserName>");
                    dataStr.append("<contactTel>").append(tel).append("</contactTel>");
                    dataStr.append("<registerTimeStr>").append(createTimeStr).append("</registerTimeStr>");
                    dataStr.append("<advice>").append(remarks).append("</advice>");

                    //========================================附件======================================================
                    List<Attachment> attachments = attachmentService.findByBizId(Long.valueOf(eventId), ConstantValue.EVENT_ATTACHMENT_TYPE);
                    if(attachments!=null && attachments.size()> 0){
                        String img_domain = funConfigurationService.changeCodeToValue(ConstantValue.APP_DOMAIN, "$IMG_DOMAIN", IFunConfigurationService.CFG_TYPE_FACT_VAL);
                        logger.info(img_domain);
                        dataStr.append("<attrs>");
                        for(Attachment attachment : attachments){
                            if(attachment.getFileName().indexOf(".png") > 0 ||attachment.getFileName().indexOf(".jpg") > 0){
                                dataStr.append("<attr>");
                                String fileName = attachment.getFileName();
                                String attrBiz = fileName.substring(fileName.indexOf("."), fileName.length());
                                String path = "http://172.16.65.5:9900"+attachment.getFilePath();
                                if(StringUtils.isNotBlank(jiangyin_img_uri)){
                                    path = jiangyin_img_uri+attachment.getFilePath();
                                }
                                String encode = "";
                                try {
                                    logger.info(path);
                                    URL url = new URL(path);
                                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                                    conn.setConnectTimeout(3*1000);
                                    //防止屏蔽程序抓取而返回403错误
                                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                                    //得到输入流
                                    InputStream inputStream = conn.getInputStream();
                                    //获取自己数组
                                    byte[] getData = FileUtils.readInputStream(inputStream);
                                    BASE64Encoder encoder = new BASE64Encoder();
                                    encode = encoder.encode(getData);
                                }catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                                dataStr.append("<attrURL>"+path+"</attrURL>");
                                dataStr.append("<attrBase64>"+encode+"</attrBase64>");
                                dataStr.append("<attrType>"+attrBiz +"</attrType>");
                                dataStr.append("<attrName>"+fileName +"</attrName>");
                                dataStr.append("</attr>");
                            }
                        }
                        dataStr.append("</attrs>");
                    }
                    //========================================附件======================================================
                    dataStr.append("</event>");
                    dataStr.append("</data>");
                    String startEvt = exchange("startEvt", dataStr.toString());

                    DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                    dataExchangeStatus.setOppoSideBizCode(taskId);
                    dataExchangeStatus.setExchangeFlag("1");
                    dataExchangeStatus.setOwnSideBizCode(taskId);
                    dataExchangeStatus.setStatus("1");
                    dataExchangeStatus.setXmlData(startEvt + "|" + dataStr.toString());
                    dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                    dataExchangeStatus.setSrcPlatform("000");
                    dataExchangeStatus.setOppoSideBizType("3");
                    dataExchangeStatus.setOwnSideBizType("3");
                    this.dataExchangeStatusService.save(dataExchangeStatus);

                }else if(StringUtils.isNotBlank(operateType) && operateType.equals("2")){//驳回
                    String eventId = reject.get("EVENT_ID").toString();
                    String remarks = reject.get("REMARKS").toString();
                    String taskId = reject.get("TASK_ID").toString();

                    StringBuffer dataStr = new StringBuffer();
                    dataStr.append("<data>");
                    dataStr.append("<auth><username></username><password></password></auth>");
                    dataStr.append("<event>");
                    dataStr.append("<eventId>").append(eventId).append("</eventId>");
                    dataStr.append("<advice>").append(remarks).append("</advice>");
                    dataStr.append("<bizPlatform>").append(BIZ_PLATFORM).append("</bizPlatform>");
                    //========================================附件======================================================
                    List<Attachment> attachments = attachmentService.findByBizId(Long.valueOf(eventId), ConstantValue.EVENT_ATTACHMENT_TYPE);
                    if(attachments!=null && attachments.size()> 0){
                        String img_domain = funConfigurationService.changeCodeToValue(ConstantValue.APP_DOMAIN, "$IMG_DOMAIN", IFunConfigurationService.CFG_TYPE_FACT_VAL);
                        logger.info(img_domain);
                        dataStr.append("<attrs>");
                        for(Attachment attachment : attachments){
                            if(attachment.getFileName().indexOf(".png") > 0 ||attachment.getFileName().indexOf(".jpg") > 0){
                                dataStr.append("<attr>");
                                String fileName = attachment.getFileName();
                                String attrBiz = fileName.substring(fileName.indexOf("."), fileName.length());
                                String path = "http://172.16.65.5:9900"+attachment.getFilePath();
                                if(StringUtils.isNotBlank(jiangyin_img_uri)){
                                    path = jiangyin_img_uri+attachment.getFilePath();
                                }
                                String encode = "";
                                try {
                                    logger.info(path);
                                    URL url = new URL(path);
                                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                                    conn.setConnectTimeout(3*1000);
                                    //防止屏蔽程序抓取而返回403错误
                                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                                    //得到输入流
                                    InputStream inputStream = conn.getInputStream();
                                    //获取自己数组
                                    byte[] getData = FileUtils.readInputStream(inputStream);
                                    BASE64Encoder encoder = new BASE64Encoder();
                                    encode = encoder.encode(getData);
                                }catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                                dataStr.append("<attrURL>"+path+"</attrURL>");
                                dataStr.append("<attrBase64>"+encode+"</attrBase64>");
                                dataStr.append("<attrType>"+attrBiz +"</attrType>");
                                dataStr.append("<attrName>"+fileName +"</attrName>");
                                dataStr.append("</attr>");
                            }
                        }
                        dataStr.append("</attrs>");
                    }
                    //========================================附件======================================================
                    dataStr.append("</event>");
                    dataStr.append("</data>");
                    String rejectEvt = exchange("rejectEvt", dataStr.toString());

                    DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                    dataExchangeStatus.setOppoSideBizCode(taskId);
                    dataExchangeStatus.setExchangeFlag("1");
                    dataExchangeStatus.setOwnSideBizCode(taskId);
                    dataExchangeStatus.setStatus("1");
                    dataExchangeStatus.setXmlData(rejectEvt + "|" + dataStr.toString());
                    dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                    dataExchangeStatus.setSrcPlatform("000");
                    dataExchangeStatus.setOppoSideBizType("3");
                    dataExchangeStatus.setOwnSideBizType("3");
                    this.dataExchangeStatusService.save(dataExchangeStatus);
                }

            }
        }
        logger.info("======================rejectEvt=======================");
    }

    private void syncRejectEvt(String inputData){
        MessagePushStub.RejectEvt rejectEvt = new MessagePushStub.RejectEvt();
        rejectEvt.setIn0(inputData);
        try {
            messagePushStub.startrejectEvt(rejectEvt, new MessagePushCallbackHandlerImpl());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void closeEvt(){
        logger.info("======================closeEvt=======================");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("srcPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
        params.put("descPlatform", BIZ_PLATFORM);
        params.put("eventSrcPlatform", BIZ_PLATFORM);
        params.put("eventDescPlatform", IDataExchangeStatusService.PLATFORM_EVENT_NEW);
        List<Map<String, Object>> closeEvent = this.dataExchangeStatusService.findCloseEvent(params);
        if(closeEvent != null && closeEvent.size() > 0){
            for(Map<String, Object> close : closeEvent){
                //[{CREATE_TIME=2017-10-20 11:30:52.0, SRC_PLATFORM=202, GRID_CODE=350701001008,
                // STATUS=04, DEST_PLATFORM=000, OWN_SIDE_BIZ_CODE=9722, RESULT=ccc,
                // TEL=15111111111, GRID_ID=13519, CONTENT_=的维权二群翁无群二, BIZ_PLATFORM=202,
                // INTER_ID=1208, OPPO_SIDE_BIZ_TYPE=2, EXCHANGE_FLAG=1, URGENCY_DEGREE=01,
                // EVENT_NAME=的萨达, INVOLVED_MONEY=0, CODE_=20171020_0701_00020, USER_ID=30003191,
                // CONTACT_USER=十大, OWN_SIDE_BIZ_TYPE=2, UPDATE_TIME=2017-10-20 15:34:53.0, TYPE_=0701,
                // HANDLE_DATE_FLAG=1, HAPPEN_TIME=2017-09-15 18:41:45.0, ATTR_FLAG=0,, EVENT_ID=9722,
                // OPPO_SIDE_BIZ_CODE=56, FIN_TIME=2017-10-20 14:46:11.0, STATUS_=1,
                // HANDLE_DATE=2017-10-30 11:06:46.0}]
                logger.info(close.toString());
                String eventId = close.get("EVENT_ID").toString();
                String oppoSideBizCode = close.get("OPPO_SIDE_BIZ_CODE").toString();
                String result = close.get("RESULT").toString();

                String finTimeStr = "";
                if(CommonFunctions.isNotBlank(close, "FIN_TIME")){
                    Date finTime = (Date)close.get("FIN_TIME");
                    if(finTime != null){
                        try {
                            finTimeStr = DateUtils.convertDateToString(finTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(StringUtils.isBlank(finTimeStr)){
                    finTimeStr = DateUtils.getNow();
                }

                StringBuffer dataStr = new StringBuffer();
                dataStr.append("<data>");
                dataStr.append("<auth><username></username><password></password></auth>");
                dataStr.append("<event>");
                dataStr.append("<eventId>").append(eventId).append("</eventId>");
                dataStr.append("<closeOrgName>").append("部门处理").append("</closeOrgName>");
                dataStr.append("<closeDate>").append(finTimeStr).append("</closeDate>");
                dataStr.append("<advice>").append(result).append("</advice>");
                dataStr.append("<bizPlatform>").append(BIZ_PLATFORM).append("</bizPlatform>");
                //========================================附件======================================================
                List<Attachment> attachments = attachmentService.findByBizId(Long.valueOf(eventId), ConstantValue.EVENT_ATTACHMENT_TYPE, "3");
                if(attachments!=null && attachments.size()> 0){
                    String img_domain = funConfigurationService.changeCodeToValue(ConstantValue.APP_DOMAIN, "$IMG_DOMAIN", IFunConfigurationService.CFG_TYPE_FACT_VAL);
                    logger.info(img_domain);
                    dataStr.append("<attrs>");
                    for(Attachment attachment : attachments){
                        if(attachment.getFileName().indexOf(".png") > 0 ||attachment.getFileName().indexOf(".jpg") > 0){
                            dataStr.append("<attr>");
                            String fileName = attachment.getFileName();
                            String attrBiz = fileName.substring(fileName.indexOf("."), fileName.length());
                            String path = "http://172.16.65.5:9900"+attachment.getFilePath();
                            if(StringUtils.isNotBlank(jiangyin_img_uri)){
                                path = jiangyin_img_uri+attachment.getFilePath();
                            }
                            String encode = "";
                            try {
                                logger.info(path);
                                URL url = new URL(path);
                                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                                conn.setConnectTimeout(3*1000);
                                //防止屏蔽程序抓取而返回403错误
                                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                                //得到输入流
                                InputStream inputStream = conn.getInputStream();
                                //获取自己数组
                                byte[] getData = FileUtils.readInputStream(inputStream);
                                BASE64Encoder encoder = new BASE64Encoder();
                                encode = encoder.encode(getData);
                            }catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                            dataStr.append("<attrURL>"+path+"</attrURL>");
                            dataStr.append("<attrBase64>"+encode+"</attrBase64>");
                            dataStr.append("<attrType>"+attrBiz +"</attrType>");
                            dataStr.append("<attrName>"+fileName +"</attrName>");
                            dataStr.append("</attr>");
                        }
                    }
                    dataStr.append("</attrs>");
                }
                //========================================附件======================================================
                dataStr.append("</event>");
                dataStr.append("</data>");




                String closeEvt = this.exchange("closeEvt", dataStr.toString());

                DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                dataExchangeStatus.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
                dataExchangeStatus.setOppoSideBizType("2");
                dataExchangeStatus.setOppoSideBizCode(eventId);
                dataExchangeStatus.setOwnSideBizType("2");
                dataExchangeStatus.setOwnSideBizCode(eventId);
                dataExchangeStatus.setStatus("1");
                dataExchangeStatus.setXmlData(closeEvt +"|"+dataStr.toString() + "|" + close.toString());
                dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                dataExchangeStatus.setSrcPlatform("000");
                this.dataExchangeStatusService.save(dataExchangeStatus);
            }
        }
        logger.info("======================closeEvt=======================");
    }

    private void syncCloseEvt(String inputData){
        MessagePushStub.CloseEvt closeEvt =  new MessagePushStub.CloseEvt();
        closeEvt.setIn0(inputData);
        try {
            messagePushStub.startcloseEvt(closeEvt, new MessagePushCallbackHandlerImpl());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void startEvt(List<Long> userIds){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userIds", userIds);
        params.put("bizPlatform", BIZ_PLATFORM);
        List<Map<String, Object>> reportEvent = this.dataExchangeStatusService.findReportEvent(params);
        if(reportEvent.size() > 0){
            for(Map<String, Object> event : reportEvent){
                System.out.println("======================对接数据=======================");
                System.out.println(event);
                System.out.println("======================对接数据=======================");
                String eventId = event.get("EVENT_ID").toString();
                String gridCode = event.get("GRID_CODE").toString();
                String eventName = event.get("EVENT_NAME").toString();
                String occurred = event.get("OCCURRED").toString();
                String content = event.get("CONTENT_").toString();
                String influenceDegree = "";
                if(event.get("INFLUENCE_DEGREE")!=null){
                    influenceDegree = event.get("INFLUENCE_DEGREE").toString();
                }
                String urgencyDegree = "";
                if(event.get("URGENCY_DEGREE")!=null){
                    urgencyDegree = event.get("URGENCY_DEGREE").toString();
                }
                String tel = event.get("TEL").toString();
                String contactUser = event.get("CONTACT_USER").toString();

                String source = "";
                if(event.get("SOURCE")!=null){
                    source = event.get("SOURCE").toString();
                }

                String instanceId = event.get("INSTANCE_ID").toString();

                Timestamp happenTime = (Timestamp)event.get("HAPPEN_TIME");
                String happenTimeStr = df.format(happenTime);
                Timestamp handleDate = (Timestamp)event.get("HANDLE_DATE");
                String handleDateStr = df.format(handleDate);
                Timestamp createTime = (Timestamp)event.get("CREATE_TIME");
                String createTimeStr = df.format(createTime);

                //获取办理意见
                String remark = "";
                String orgCode = "";

                List<Map<String, Object>> taskList = eventDisposalWorkflowService.queryProInstanceDetail(Long.valueOf(instanceId), IEventDisposalWorkflowService.SQL_ORDER_DESC);
                for(Map<String ,Object> task : taskList){
                    logger.info(task.toString());
                }
                if(taskList.size() > 2){
                    if(taskList.get(1).get("REMARKS")!=null){
                        remark = taskList.get(1).get("REMARKS").toString();
                    }
                    if(taskList.get(0).get("TASK_ID") != null){
                        String taskId = taskList.get(0).get("TASK_ID").toString();
                        List<Map<String, Object>> myTaskParticipation = eventDisposalWorkflowService.queryMyTaskParticipation(taskId);
                        logger.info(myTaskParticipation.toString());
                        if(myTaskParticipation.get(0).get("ORG_CODE")!=null){
                            orgCode = myTaskParticipation.get(0).get("ORG_CODE").toString();
                            logger.info("orgCode:"+orgCode);
                        }
                    }
                }
                //获取经纬度
                ResMarker resMarker = resMarkerService.findResMarkerByResId("0301", Long.valueOf(eventId), "5");
                String x = "";
                String y = "";
                if(resMarker != null){
                    x = resMarker.getX();
                    y = resMarker.getY();
                }

                StringBuffer dataStr = new StringBuffer();
                dataStr.append("<data>");
                dataStr.append("<auth><username></username><password></password></auth>");
                dataStr.append("<event>");
                dataStr.append("<gridCode>").append(gridCode).append("</gridCode>");
                dataStr.append("<orgCode>").append(orgCode).append("</orgCode>");
                dataStr.append("<eventName>").append(eventName).append("</eventName>");
                dataStr.append("<happenTimeStr>").append(happenTimeStr).append("</happenTimeStr>");
                dataStr.append("<occurred>").append(occurred).append("</occurred>");
                dataStr.append("<content>").append(content).append("</content>");
                dataStr.append("<handleDate>").append(handleDateStr).append("</handleDate>");
                dataStr.append("<oppoSideBusiCode>").append(eventId).append("</oppoSideBusiCode>");
                dataStr.append("<bizPlatform>").append(BIZ_PLATFORM).append("</bizPlatform>");
                dataStr.append("<eventType>").append("1").append("</eventType>");
                dataStr.append("<longitude>").append(x).append("</longitude>");
                dataStr.append("<latitude>").append(y).append("</latitude>");
                dataStr.append("<urgency>").append(urgencyDegree).append("</urgency>");
                dataStr.append("<influence>").append(influenceDegree).append("</influence>");
                dataStr.append("<source>").append(source).append("</source>");
                dataStr.append("<creatorName>").append("").append("</creatorName>");
                dataStr.append("<contactUserName>").append(contactUser).append("</contactUserName>");
                dataStr.append("<contactTel>").append(tel).append("</contactTel>");
                dataStr.append("<registerTimeStr>").append(createTimeStr).append("</registerTimeStr>");
                dataStr.append("<advice>").append(remark).append("</advice>");

                //========================================附件======================================================
                List<Attachment> attachments = attachmentService.findByBizId(Long.valueOf(eventId), ConstantValue.EVENT_ATTACHMENT_TYPE, "1");
                if(attachments!=null && attachments.size()> 0){
                    String img_domain = funConfigurationService.changeCodeToValue(ConstantValue.APP_DOMAIN, "$IMG_DOMAIN", IFunConfigurationService.CFG_TYPE_FACT_VAL);
                    logger.info(img_domain);
                    dataStr.append("<attrs>");
                    for(Attachment attachment : attachments){
                        if(attachment.getFileName().indexOf(".png") > 0 ||attachment.getFileName().indexOf(".jpg") > 0){
                            dataStr.append("<attr>");
                            String fileName = attachment.getFileName();
                            String attrBiz = fileName.substring(fileName.indexOf("."), fileName.length());
                            String path = "http://172.16.65.5:9900"+attachment.getFilePath();
                            if(StringUtils.isNotBlank(jiangyin_img_uri)){
                                path = jiangyin_img_uri+attachment.getFilePath();
                            }
                            String encode = "";
                            try {
                                logger.info(path);
                                URL url = new URL(path);
                                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                                conn.setConnectTimeout(3*1000);
                                //防止屏蔽程序抓取而返回403错误
                                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                                //得到输入流
                                InputStream inputStream = conn.getInputStream();
                                //获取自己数组
                                byte[] getData = FileUtils.readInputStream(inputStream);
                                BASE64Encoder encoder = new BASE64Encoder();
                                encode = encoder.encode(getData);
                            }catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                            dataStr.append("<attrURL>"+path+"</attrURL>");
                            dataStr.append("<attrBase64>"+encode+"</attrBase64>");
                            dataStr.append("<attrType>"+attrBiz +"</attrType>");
                            dataStr.append("<attrName>"+fileName +"</attrName>");
                            dataStr.append("</attr>");
                        }
                    }
                    dataStr.append("</attrs>");
                }
                //========================================附件======================================================
                dataStr.append("</event>");
                dataStr.append("</data>");
                String startEvt = exchange("startEvt", dataStr.toString());

                //保存中间表
                DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                dataExchangeStatus.setExchangeFlag("1");
                dataExchangeStatus.setOwnSideBizCode(eventId);
                dataExchangeStatus.setOppoSideBizCode(eventId);
                dataExchangeStatus.setStatus("1");
                dataExchangeStatus.setXmlData(dataStr.toString() + "|" + startEvt);
                dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                dataExchangeStatus.setSrcPlatform("000");
                dataExchangeStatus.setOppoSideBizType("2");
                dataExchangeStatus.setOwnSideBizType("2");
                this.dataExchangeStatusService.save(dataExchangeStatus);
            }
        }
    }



    private void syncStartEvt(String inputData){
        MessagePushStub.StartEvt startEvt = new MessagePushStub.StartEvt();
        startEvt.setIn0(inputData);
        try {
            messagePushStub.startstartEvt(startEvt, new MessagePushCallbackHandler() {
                @Override
                public void receiveResultstartEvt(MessagePushStub.StartEvtResponse result) {
                    System.out.println("=======================receiveResultstartEvt==========================");
                }
            });

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public String exchange(String method, Object input){
        logger.info(input.toString());
        RPCServiceClient serviceClient = null;
        try {
            serviceClient = new RPCServiceClient();
            Options options = serviceClient.getOptions();
            //指定调用WebService的URL
            //http://1967j47e67.51mypc.cn:10101/jygaxf/services/messagePush
            //http://58.241.46.14:12010/jygaxf/services/messagePush
            //http://49.65.126.155:10101/jygaxf/services/messagePush
            EndpointReference targetEPR = new EndpointReference("http://58.241.46.14:12010/jygaxf/services/messagePush");
            options.setTo(targetEPR);
            //指定sayHello方法的参数值
            Object[] opAddEntryArgs = new Object[] {input};
            //指定sayHello方法返回值的数据类型的Class对象
            Class[] classes = new Class[] {String.class};
            //指定要调用的sayHello方法及WSDL文件的命名空间
            QName opAddEntry = new QName("http://util.mbl.com", method);
            //调用sayHello方法并输出该方法的返回值

            String result = serviceClient.invokeBlocking(opAddEntry, opAddEntryArgs, classes)[0].toString();

            serviceClient.cleanupTransport();  //为了防止连接超时
            logger.info(result);
            return result;
        } catch (AxisFault e) {
            e.printStackTrace();
            logger.error(e.toString());
            return "";
        }
    }

//    @org.junit.Test
//    public void test(){
//        String xml = "<data><auth><username></username><password></password></auth><event><gridCode>320281108</gridCode><eventName>测试数据</eventName><happenTimeStr>2018-03-23 11:03:10</happenTimeStr><occurred>测试数据</occurred><content>测试数据</content><handleDate>2018-04-03 11:03:23</handleDate><oppoSideBusiCode>44862</oppoSideBusiCode><bizPlatform>042</bizPlatform><eventType>1</eventType><longitude>31.84801248083496</longitude><latitude>120.39973122827148</latitude><urgency>01</urgency><influence>01</influence><source>01</source><creatorName></creatorName><contactUserName>徐洁</contactUserName><contactTel>13812532333</contactTel><registerTimeStr>2018-03-23 11:03:23</registerTimeStr><advice>测试数据</advice></event></data>";
//        exchange("startEvt", xml);
//    }
}
