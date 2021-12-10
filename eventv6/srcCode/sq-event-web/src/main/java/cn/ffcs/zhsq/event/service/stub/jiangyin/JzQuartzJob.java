package cn.ffcs.zhsq.event.service.stub.jiangyin;/**
 * Created by Administrator on 2017/7/4.
 */

import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
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
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class JzQuartzJob implements Job {

    private final Logger logger = LoggerFactory.getLogger("dailyRollingFile");
    private IDataExchangeStatusService dataExchangeStatusService;
    private IFunConfigurationService funConfigurationService;
    private IAttachmentService attachmentService;
    protected IMixedGridInfoService mixedGridInfoService;
    private UserManageOutService userManageService;
    private OrgEntityInfoOutService orgEntityInfoOutService;
    private OrgSocialInfoOutService orgSocialInfoService;
    private ThirdService_NewStub stub;
    private IEventDisposalWorkflowService eventDisposalWorkflowService;


    //业务编码
    private final String BIZ_PLATFORM = "038";

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String URI = "http://58.214.254.6:9696/12345/WebService/ThirdService2.asmx?wsdl";
    private String FILE_URI = "http://58.214.254.6:9696/12345/WebService/FileService.asmx?wsdl";


    private void report(){
        String jz12345RoleId = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "JZ12345_ROLE_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        if(StringUtils.isBlank(jz12345RoleId)){
            logger.error("功能配置-街镇12345对接角色【JZ12345_ROLE_ID】未配置！");
            return;
        }
        List<Long> userIds = new ArrayList<Long>();
//        userIds.add(30010135L);
        List<UserBO> listByUserExampleParamsOut = userManageService.getUserListByUserExampleParamsOut(Long.valueOf(jz12345RoleId), null, null);
        for(int i=0;i<listByUserExampleParamsOut.size();i++){
            userIds.add(listByUserExampleParamsOut.get(i).getUserId());
            //每500条提交一次
            if(i%500==0){
                reportEvent(userIds);
                rejectEvent(userIds);
                userIds = new ArrayList<Long>();
            }
        }
        reportEvent(userIds);
        rejectEvent(userIds);
    }

    private void rejectEvent(List<Long> userIds){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userIds", userIds);
        params.put("bizPlatform", BIZ_PLATFORM);
        List<Map<String, Object>> reportEvent = this.dataExchangeStatusService.findRejectEvent(params);
        //[{PRE_USER_ID=30014218, OPERATE_TYPE=1, IS_WORKFLOW_TASK=1, TRANSACTOR_ID=30010698,
        // INSTANCE_ID=722726, ORG_NAME=青阳镇, REMARKS=测试12345, TRANSACTOR_NAME=陈煜晴,
        // ISTIMEOUT=0, TASK_ID=722843, START_TIME=oracle.sql.TIMESTAMP@621b591f, RK=1,
        // PRE_TASK_ID=722796, ORG_ID=145469, PRE_TASK_NAME=task12, PRE_USER_NAME=青阳镇12345平台,
        // EVENT_ID=31831, TASK_TYPE=1, TASK_NAME=task4, OPPO_SIDE_BIZ_CODE=20180124083302210_06408107a7e7464e8a06b3bd2712572c,
        // NODE_ID=558705, END_TIME=oracle.sql.TIMESTAMP@7178348a, TASK_NUM=1}]
        if(reportEvent != null && reportEvent.size() > 0){
            for (Map<String, Object> map : reportEvent) {

            }
        }

        System.out.println(reportEvent);
    }

    private void snycData(Map<String, Object> map) throws RemoteException {
        String eventName = (String)map.get("EVENT_NAME");
        String gridCode = (String)map.get("GRID_CODE");
        MixedGridInfo defaultGridByOrgCode = mixedGridInfoService.getDefaultGridByOrgCode(gridCode);
        String gridPath = "";
        String gridName = "";
        if(defaultGridByOrgCode!=null){
            gridPath = defaultGridByOrgCode.getGridPath();
            gridName = defaultGridByOrgCode.getGridName();
        }
        String event_id = map.get("EVENT_ID").toString();
        String contactUser = (String)map.get("CONTACT_USER");
        String userid_ = map.get("USERID_").toString();
        String groupid_ = map.get("GROUPID_").toString();
        System.out.println("groupid_="+groupid_);

        OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoService.findByOrgId(Long.valueOf(groupid_));
        OrgEntityInfoBO entityInfoBO = orgEntityInfoOutService.findByOrgId(orgSocialInfoBO.getLocationOrgId());
        System.out.println("entityInfoBO---"+entityInfoBO.getOrgName());

        String tel = (String)map.get("TEL");
        String occurred = (String)map.get("OCCURRED");
        String content = (String)map.get("CONTENT_");
        Timestamp createTime = (Timestamp)map.get("CREATE_TIME");
        String createTimeStr = df.format(createTime);
        Timestamp happenTime = (Timestamp)map.get("HAPPEN_TIME");
        String happenTimeStr = df.format(happenTime);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ThirdNumber", "");
        jsonObject.put("CaseSourceID", 18);
        jsonObject.put("CaseTypeID", 6);
        jsonObject.put("WantPerson", contactUser);
        jsonObject.put("WantPersonTel", tel);
        jsonObject.put("Title", eventName);
        jsonObject.put("Village", "320281");
        jsonObject.put("Address", gridPath);
        jsonObject.put("WantContent", "于"+happenTimeStr+",在"+occurred+"发生"+content);
        //提交日期
        jsonObject.put("WantDate", createTimeStr);
        jsonObject.put("State", 0);
        jsonObject.put("SourceNumber", event_id);
        jsonObject.put("Result", "");
        jsonObject.put("FinishDate", "");
        jsonObject.put("ReplyContent", "");

        List<Attachment> attachments = attachmentService.findByBizId(Long.valueOf(event_id), ConstantValue.EVENT_ATTACHMENT_TYPE);
        if (attachments != null && attachments.size() > 0) {
            JSONArray jsonArray = sandFile(attachments);
            jsonObject.put("Attachs",jsonArray);
        }

        ThirdService_NewStub.GetOrgs getOrgs = new ThirdService_NewStub.GetOrgs();
        ThirdService_NewStub.GetOrgsResponse getOrgsResponse = stub.getOrgs(getOrgs);

        String getOrgsResult = getOrgsResponse.getGetOrgsResult();
        System.out.println("=====getOrgs=====");
        System.out.println(getOrgsResult);
        System.out.println("=====getOrgs=====");
//{ "Success":true, "Data":[{ "OrgNumber":"39de318c-3304-499e-b98c-d61e23462d41", "OrgName":"澄江街道" },{ "OrgNumber":"fcb41c8e-d496-4ef0-b014-b126ba6a69c4", "OrgName":"城东街道" },{ "OrgNumber":"6091b817-a113-4ee0-bc15-df1a1f3d059d", "OrgName":"徐霞客镇" },{ "OrgNumber":"20171011085038033_63ac467cae64448081c427972e386982", "OrgName":"夏港街道" },{ "OrgNumber":"20171011085138873_8d58ab24b9cf4b5ca8ae55fb0ec22f86", "OrgName":"申港街道" },{ "OrgNumber":"20171011085157812_c2a00f874b5e41488aedaf2f251e9c67", "OrgName":"利港街道" },{ "OrgNumber":"e2053491-9b80-4c95-939c-552bd04ed3e8", "OrgName":"璜土镇" },{ "OrgNumber":"d67a2bfa-717c-4073-8f49-df120c1e6e59", "OrgName":"月城镇" },{ "OrgNumber":"0e2d45d9-bf23-4c59-988e-dd0231915ad4", "OrgName":"青阳镇" },{ "OrgNumber":"dff630af-12cc-46da-a05a-8d481bfd12ea", "OrgName":"南闸街道" },{ "OrgNumber":"92a4050a-d005-4a8a-aefe-6df1766db5ac", "OrgName":"云亭街道" },{ "OrgNumber":"eb84bf07-0ef8-4cd1-8631-c84799987520", "OrgName":"华士镇" },{ "OrgNumber":"0dfb1f40-7350-4610-a877-a7157cffba84", "OrgName":"周庄镇" },{ "OrgNumber":"1741f380-c6cf-472a-b8f5-4abaa882c076", "OrgName":"长泾镇" },{ "OrgNumber":"78cd54c8-0edd-4d01-be92-a9040878a4ea", "OrgName":"顾山镇" },{ "OrgNumber":"bc1887b4-e09b-499d-94d4-1d5644b0325c", "OrgName":"祝塘镇" },{ "OrgNumber":"6891c172-af52-4b7d-9d75-82272f7d2192", "OrgName":"新桥镇" }] }

        String orgNumber = "";
        JSONObject fromObject = JSONObject.fromObject(getOrgsResult);
        JSONArray jsonArray = JSONArray.fromObject(fromObject.get("Data"));
        for(Object obj : jsonArray){
            JSONObject orgJsonObj = (JSONObject) obj;
            if(orgJsonObj.get("OrgName").equals(entityInfoBO.getOrgName())){
                orgNumber = orgJsonObj.get("OrgNumber").toString();
            }
        }

        logger.info(jsonObject.toString());
        ThirdService_NewStub.SubmitThirdToSub submitThirdToSub = new ThirdService_NewStub.SubmitThirdToSub();
        submitThirdToSub.setCaseSourceID(18);
        submitThirdToSub.setSessionID("V0dIMTg=");
        submitThirdToSub.setCaseSourceName("WGH");
        submitThirdToSub.setOrgNumber(orgNumber);
        submitThirdToSub.setStrThird(jsonObject.toString());

        ThirdService_NewStub.SubmitThirdToSubResponse submitThirdToSubResponse = stub.submitThirdToSub(submitThirdToSub);
        String result = submitThirdToSubResponse.getSubmitThirdToSubResult();
        System.out.println("=====submitThirdToSubResponse=====");
        System.out.println(result);
        System.out.println("=====submitThirdToSubResponse=====");

        JSONObject resultObj = JSONObject.fromObject(result);
        Boolean success = (Boolean)resultObj.get("Success");
        DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
        if(success){
            String outEventId = (String)resultObj.get("Number");
            dataExchangeStatus.setOppoSideBizCode(outEventId);
        }
        dataExchangeStatus.setExchangeFlag("1");
        dataExchangeStatus.setOwnSideBizCode(event_id);
        dataExchangeStatus.setStatus("1");
        dataExchangeStatus.setXmlData(result+"|"+jsonObject);
        dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
        dataExchangeStatus.setSrcPlatform("000");
        dataExchangeStatus.setOppoSideBizType("2");
        dataExchangeStatus.setOwnSideBizType("2");
        this.dataExchangeStatusService.save(dataExchangeStatus);
    }

    /**
     * 上报事件
     * @param userIds
     */
    private void reportEvent(List<Long> userIds){
        //获取上报事件
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userIds", userIds);
        params.put("bizPlatform", BIZ_PLATFORM);
        List<Map<String, Object>> reportEvent = this.dataExchangeStatusService.findReportEvent(params);
        if (reportEvent != null) {
            for (Map<String, Object> map : reportEvent) {
                String eventName = (String)map.get("EVENT_NAME");
                String gridCode = (String)map.get("GRID_CODE");
                MixedGridInfo defaultGridByOrgCode = mixedGridInfoService.getDefaultGridByOrgCode(gridCode);
                String gridPath = "";
                String gridName = "";
                if(defaultGridByOrgCode!=null){
                    gridPath = defaultGridByOrgCode.getGridPath();
                    gridName = defaultGridByOrgCode.getGridName();
                }
                String event_id = map.get("EVENT_ID").toString();
                String contactUser = (String)map.get("CONTACT_USER");
                String userid_ = map.get("USERID_").toString();
                String groupid_ = map.get("GROUPID_").toString();
                System.out.println("groupid_="+groupid_);

                OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoService.findByOrgId(Long.valueOf(groupid_));
                OrgEntityInfoBO entityInfoBO = orgEntityInfoOutService.findByOrgId(orgSocialInfoBO.getLocationOrgId());
                System.out.println("entityInfoBO---"+entityInfoBO.getOrgName());

                BigDecimal instanceId = (BigDecimal)map.get("INSTANCE_ID");
                String remark = "";
                List<Map<String, Object>> taskList = eventDisposalWorkflowService.queryProInstanceDetail(instanceId.longValue(), IEventDisposalWorkflowService.SQL_ORDER_DESC);
                for(Map<String, Object> task : taskList){
                    System.out.println(task);
                }
                if(taskList.get(1).get("REMARKS")!=null){
                    remark = taskList.get(1).get("REMARKS").toString();
                }

                Timestamp handleDate = (Timestamp)map.get("HANDLE_DATE");
                String handleDateStr = df.format(handleDate);
                String tel = (String)map.get("TEL");
                String occurred = (String)map.get("OCCURRED");
                String content = (String)map.get("CONTENT_");
                Timestamp createTime = (Timestamp)map.get("CREATE_TIME");
                String createTimeStr = df.format(createTime);
                Timestamp happenTime = (Timestamp)map.get("HAPPEN_TIME");
                String happenTimeStr = df.format(happenTime);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ThirdNumber", "");
                jsonObject.put("CaseSourceID", 18);
                jsonObject.put("CaseTypeID", 6);
                jsonObject.put("WantPerson", contactUser);
                jsonObject.put("WantPersonTel", tel);
                jsonObject.put("Title", eventName);
                jsonObject.put("Village", "320281");
                jsonObject.put("Address", gridPath);
                jsonObject.put("WantContent", "于"+happenTimeStr+",在"+occurred+"发生"+content);
                //提交日期
                jsonObject.put("WantDate", createTimeStr);
                jsonObject.put("State", 0);
                jsonObject.put("SourceNumber", event_id);
                jsonObject.put("Result", "");
                jsonObject.put("remark", "办理意见"+remark+",办理截止时间:【"+handleDateStr+"】");//办理意见*****,办理截止时间:【yyyy-MM-dd hh:mm】
                jsonObject.put("FinishDate", "");
                jsonObject.put("ReplyContent", "");


                List<Attachment> attachments = attachmentService.findByBizId(Long.valueOf(event_id), ConstantValue.EVENT_ATTACHMENT_TYPE);
                if (attachments != null && attachments.size() > 0) {
                    JSONArray jsonArray = sandFile(attachments);
                    jsonObject.put("Attachs",jsonArray);
                }


                try {
//                    ThirdService_NewStub stub = new ThirdService_NewStub(URI);
                    ThirdService_NewStub.GetOrgs getOrgs = new ThirdService_NewStub.GetOrgs();
                    ThirdService_NewStub.GetOrgsResponse getOrgsResponse = stub.getOrgs(getOrgs);

                    String getOrgsResult = getOrgsResponse.getGetOrgsResult();
                    System.out.println("=====getOrgs=====");
                    System.out.println(getOrgsResult);
                    System.out.println("=====getOrgs=====");

//{ "Success":true, "Data":[{ "OrgNumber":"39de318c-3304-499e-b98c-d61e23462d41", "OrgName":"澄江街道" },{ "OrgNumber":"fcb41c8e-d496-4ef0-b014-b126ba6a69c4", "OrgName":"城东街道" },{ "OrgNumber":"6091b817-a113-4ee0-bc15-df1a1f3d059d", "OrgName":"徐霞客镇" },{ "OrgNumber":"20171011085038033_63ac467cae64448081c427972e386982", "OrgName":"夏港街道" },{ "OrgNumber":"20171011085138873_8d58ab24b9cf4b5ca8ae55fb0ec22f86", "OrgName":"申港街道" },{ "OrgNumber":"20171011085157812_c2a00f874b5e41488aedaf2f251e9c67", "OrgName":"利港街道" },{ "OrgNumber":"e2053491-9b80-4c95-939c-552bd04ed3e8", "OrgName":"璜土镇" },{ "OrgNumber":"d67a2bfa-717c-4073-8f49-df120c1e6e59", "OrgName":"月城镇" },{ "OrgNumber":"0e2d45d9-bf23-4c59-988e-dd0231915ad4", "OrgName":"青阳镇" },{ "OrgNumber":"dff630af-12cc-46da-a05a-8d481bfd12ea", "OrgName":"南闸街道" },{ "OrgNumber":"92a4050a-d005-4a8a-aefe-6df1766db5ac", "OrgName":"云亭街道" },{ "OrgNumber":"eb84bf07-0ef8-4cd1-8631-c84799987520", "OrgName":"华士镇" },{ "OrgNumber":"0dfb1f40-7350-4610-a877-a7157cffba84", "OrgName":"周庄镇" },{ "OrgNumber":"1741f380-c6cf-472a-b8f5-4abaa882c076", "OrgName":"长泾镇" },{ "OrgNumber":"78cd54c8-0edd-4d01-be92-a9040878a4ea", "OrgName":"顾山镇" },{ "OrgNumber":"bc1887b4-e09b-499d-94d4-1d5644b0325c", "OrgName":"祝塘镇" },{ "OrgNumber":"6891c172-af52-4b7d-9d75-82272f7d2192", "OrgName":"新桥镇" }] }

                    String orgNumber = "";
                    JSONObject fromObject = JSONObject.fromObject(getOrgsResult);
                    JSONArray jsonArray = JSONArray.fromObject(fromObject.get("Data"));
                    for(Object obj : jsonArray){
                        JSONObject orgJsonObj = (JSONObject) obj;
                        if(orgJsonObj.get("OrgName").equals(entityInfoBO.getOrgName())){
                            orgNumber = orgJsonObj.get("OrgNumber").toString();
                        }
                    }

                    logger.info(jsonObject.toString());
                    ThirdService_NewStub.SubmitThirdToSub submitThirdToSub = new ThirdService_NewStub.SubmitThirdToSub();
                    submitThirdToSub.setCaseSourceID(18);
                    submitThirdToSub.setSessionID("V0dIMTg=");
                    submitThirdToSub.setCaseSourceName("WGH");
                    submitThirdToSub.setOrgNumber(orgNumber);
                    submitThirdToSub.setStrThird(jsonObject.toString());

                    ThirdService_NewStub.SubmitThirdToSubResponse submitThirdToSubResponse = stub.submitThirdToSub(submitThirdToSub);
                    String result = submitThirdToSubResponse.getSubmitThirdToSubResult();
                    System.out.println("=====submitThirdToSubResponse=====");
                    System.out.println(result);
                    System.out.println("=====submitThirdToSubResponse=====");

                    JSONObject resultObj = JSONObject.fromObject(result);
                    Boolean success = (Boolean)resultObj.get("Success");
                    DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                    if(success){
                        String outEventId = (String)resultObj.get("Number");
                        dataExchangeStatus.setOppoSideBizCode(outEventId);
                    }
                    dataExchangeStatus.setExchangeFlag("1");
                    dataExchangeStatus.setOwnSideBizCode(event_id);
                    dataExchangeStatus.setStatus("1");
                    dataExchangeStatus.setXmlData(result+"|"+jsonObject);
                    dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                    dataExchangeStatus.setSrcPlatform("000");
                    dataExchangeStatus.setOppoSideBizType("2");
                    dataExchangeStatus.setOwnSideBizType("2");
                    this.dataExchangeStatusService.save(dataExchangeStatus);
                } catch (AxisFault axisFault) {
                    axisFault.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 发送图片
     * @param attachments
     * @return
     */
    private JSONArray sandFile(List<Attachment> attachments){
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i< attachments.size();i++){
            Attachment attachment = attachments.get(i);
            Long attachmentId = attachment.getAttachmentId();
            String fileName = attachment.getFileName();
            String filePath = attachment.getFilePath();
            String fileResult = exchangeFile(fileName,filePath);
            if(StringUtils.isNotBlank(fileResult)){
                DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                dataExchangeStatus.setExchangeFlag("1");
                dataExchangeStatus.setOppoSideBizCode("");
                dataExchangeStatus.setOwnSideBizCode(attachmentId.toString());
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
        return jsonArray;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("=========" + DateUtils.getNow() + " start 江阴 街镇12345事件对接==========");
        try {
            SchedulerContext schCtx = context.getScheduler().getContext();
            ApplicationContext appCtx = (ApplicationContext) schCtx.get("applicationContext");
            this.attachmentService = (IAttachmentService) appCtx.getBean("attachmentService");
            this.dataExchangeStatusService = (IDataExchangeStatusService) appCtx.getBean("dataExchangeStatusServiceImpl");
            this.funConfigurationService = (IFunConfigurationService) appCtx.getBean("funConfigurationService");
            this.mixedGridInfoService = (IMixedGridInfoService) appCtx.getBean("mixedGridInfoService");
            this.userManageService = (UserManageOutService) appCtx.getBean("userManageService");
            this.orgEntityInfoOutService = (OrgEntityInfoOutService) appCtx.getBean("orgEntityInfoOutService");
            this.eventDisposalWorkflowService = (IEventDisposalWorkflowService) appCtx.getBean("eventDisposalWorkflowServiceImpl");
            this.orgSocialInfoService = (OrgSocialInfoOutService) appCtx.getBean("orgSocialInfoService");
            this.stub = (ThirdService_NewStub) appCtx.getBean("thirdService_NewStub");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        FILE_URI = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "JIANGYIN_FILE_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        URI = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "JIANGYIN_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL);

        report();

        logger.info("=========" + DateUtils.getNow() + " end 江阴 街镇12345事件对接==========");
    }

    private String exchangeFile(String fileName, String filePath){
        String result = "";
        String img_domain = funConfigurationService.changeCodeToValue(ConstantValue.APP_DOMAIN, "$IMG_DOMAIN", IFunConfigurationService.CFG_TYPE_URL);
        logger.error(img_domain);
        System.out.println(img_domain);
        try {
            FileServiceStub stub = new FileServiceStub(FILE_URI);
//            stub.
            FileServiceStub.UploadThirdFile uploadThirdFile = new FileServiceStub.UploadThirdFile();
            uploadThirdFile.setCaseSourceID(18);
            uploadThirdFile.setSessionID("V0dIMTg=");
            uploadThirdFile.setCaseSourceName("WGH");
            uploadThirdFile.setFileName(fileName);
            uploadThirdFile.setFilepath(img_domain+filePath);
            System.out.println(img_domain+filePath);
            Options options = stub._getServiceClient().getOptions();
            options.setTimeOutInMilliSeconds(30000);//设置超时(单位是毫秒)
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
