package cn.ffcs.zhsq.event.service.stub.lingwu;/**
 * Created by Administrator on 2017/8/2.
 */

import cn.ffcs.common.FileUtils;
import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.bo.PartyUserCertifyBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.uam.service.UserRoleOutService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.utils.ConstantValue;
import net.sf.json.JSONObject;
import oracle.sql.TIMESTAMP;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongshm
 * @create 2017-08-02 8:56
 * 领悟事件对接
 **/
public class QuartzJob implements Job {

    private final Logger logger = LoggerFactory.getLogger("dailyRollingFile");

    private IDataExchangeStatusService dataExchangeStatusService;
    private IDataExchangeStatusTwoWayService iDataExchangeStatusTwoWayService;
    private IFunConfigurationService funConfigurationService;
    private IAttachmentService attachmentService;
    protected IMixedGridInfoService mixedGridInfoService;
    private UserManageOutService userManageService;
    private IResMarkerService resMarkerService;
//    private FileUploadService fileUploadService;

    //业务编码
    private final String BIZ_PLATFORM = "030";
    private String uri = "http://221.228.70.21:90/mobile/zhzf/sjsb/saveExtEReport.jsp";
    private String lingwu_role_id = "";
    private String jiangyin_user_id = "";
    private String jiangyin_img_uri = "";
    private String cgddUserId = "184";
    String jiangyin_uri = "";

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("===================================================");
        logger.info("领悟事件对接");
        logger.info("===================================================");
        try {
            SchedulerContext schCtx = context.getScheduler().getContext();
            ApplicationContext appCtx = (ApplicationContext) schCtx.get("applicationContext");
            String applicationName = appCtx.getApplicationName();
            logger.info(applicationName);
            this.dataExchangeStatusService = (IDataExchangeStatusService) appCtx.getBean("dataExchangeStatusServiceImpl");
            this.iDataExchangeStatusTwoWayService = (IDataExchangeStatusTwoWayService) appCtx.getBean("dataExchangeStatusTwoWayServiceImpl");
            this.funConfigurationService = (IFunConfigurationService) appCtx.getBean("funConfigurationService");
            this.attachmentService = (IAttachmentService) appCtx.getBean("attachmentService");
            this.mixedGridInfoService = (IMixedGridInfoService) appCtx.getBean("mixedGridInfoService");
            this.userManageService = (UserManageOutService) appCtx.getBean("userManageService");
            this.resMarkerService = (IResMarkerService) appCtx.getBean("resMarkerService");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        jiangyin_uri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        jiangyin_img_uri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_IMG_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        lingwu_role_id = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_ROLE_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        jiangyin_user_id = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_USER_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
//        cgddUserId = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "CGDD_USER_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
//        jiangyinUserId
//        jiangyin_user_id = "";//30003111



        /*if(StringUtils.isBlank(jiangyin_uri)){
            logger.error("功能配置【LINGWU_URI】未配置！");
            return;
        }else{
            this.uri = jiangyin_uri;
        }
        if(StringUtils.isBlank(lingwu_role_id)){
            logger.error("功能配置【LINGWU_ROLE_ID】未配置！");
            return;
        }else{
            this.lingwu_role_id = lingwu_role_id;
        }
        if(StringUtils.isBlank(jiangyin_user_id)){
            logger.error("功能配置【LINGWU_USER_ID】未配置！");
            return;
        }else{
            this.jiangyin_user_id = jiangyin_user_id;
        }
        if(StringUtils.isBlank(jiangyin_img_uri)){
            logger.error("功能配置【LINGWU_IMG_URI】未配置！");
            return;
        }else{
            this.jiangyin_img_uri = jiangyin_img_uri;
        }*/

        reportCG();//上报城管大队
        report();//上报综合执法
        reject();//上报综合执法
        close();//结案
    }

    private void reject(){
        List<Long> userIds = new ArrayList<Long>();
        List<UserBO> listByUserExampleParamsOut = userManageService.getUserListByUserExampleParamsOut(Long.valueOf(lingwu_role_id), null, null);
        for(int i=0;i<listByUserExampleParamsOut.size();i++){
            userIds.add(listByUserExampleParamsOut.get(i).getUserId());
        }
        System.out.println(userIds);
        //获取上报事件
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userIds", userIds);
        params.put("bizPlatform", BIZ_PLATFORM);
        List<Map<String, Object>> reportEvent = this.dataExchangeStatusService.findRejectEvent(params);
        if (reportEvent != null) {
            for (Map<String, Object> map : reportEvent) {
                //{EVENT_NAME=事件名称4, INVOLVED_MONEY=0, CODE_=20170620_0701_00007, CREATE_TIME=2017-06-20 20:41:03.0, CONTACT_USER=匿名, GRID_CODE=620102, START_TIME=2017-06-20 20:02:43.0, UPDATE_TIME=2017-06-20 20:41:51.0, OCCURRED=陈埭镇坊脚村委会盛兴路一工地, STATUS=02, HANDLE_DATE_FLAG=3, TYPE_=0701, HAPPEN_TIME=2017-07-31 03:47:50.0, ATTR_FLAG=0,, EVENT_ID=8769, TEL=15359469224, GRID_ID=423137, BIZ_PLATFORM=016, CONTENT_=报称：在陈埭坊脚盛兴路一工地，老板拖欠其5000元工资。, URGENCY_DEGREE=01, HANDLE_DATE=2017-06-30 20:02:43.0}

                BigDecimal taskId = (BigDecimal)map.get("TASK_ID");

                String xmlData = snycData(map, null);
                logger.info("请求数据：");
                String exchange = exchange(jiangyin_uri,xmlData.toString());
                logger.info("返回数据："+exchange);
                DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                dataExchangeStatus.setExchangeFlag("1");
                dataExchangeStatus.setOppoSideBizType("3");
                dataExchangeStatus.setOppoSideBizCode("");
                dataExchangeStatus.setOwnSideBizType("3");
                dataExchangeStatus.setOwnSideBizCode(String.valueOf(taskId));
                dataExchangeStatus.setStatus("1");
                dataExchangeStatus.setXmlData(xmlData.toString() + "|" +exchange);
                dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                dataExchangeStatus.setSrcPlatform("000");
                this.dataExchangeStatusService.save(dataExchangeStatus);
            }
        }
    }

    private void reportCG(){
        if(StringUtils.isNotBlank(cgddUserId)){
            List<Long> userIds = new ArrayList<Long>();
            userIds.add(Long.valueOf(cgddUserId));//城管大队
            reportEvent(userIds, "cg");
        }
    }

    private void close(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("srcPlatform", "000");
        params.put("descPlatform", "030");
        params.put("eventSrcPlatform", "030");
        params.put("eventDescPlatform", "000");
        List<Map<String, Object>> closeEvent = this.dataExchangeStatusService.findCloseEvent(params);
        if(closeEvent!=null&&closeEvent.size()>0){
            String eventId;
            String result = "空";

            for (Map<String, Object> event : closeEvent){
                System.out.println(event);
                eventId = event.get("EVENT_ID").toString();
                String oppo_side_biz_code = event.get("OPPO_SIDE_BIZ_CODE").toString();
                String partyName = "未知";
                if(event.get("USER_ID")!=null){
                    String user_id = event.get("USER_ID").toString();
                    UserBO userInfo = this.userManageService.getUserInfoByUserId(Long.valueOf(user_id));
                    partyName = userInfo.getPartyName();
                }

                if(event.get("RESULT")!=null){
                    result = event.get("RESULT").toString();
                }
                java.sql.Timestamp fin_time = (java.sql.Timestamp)event.get("FIN_TIME");
                String fin_time_str = "";
                if(fin_time!=null){
                    fin_time_str = df.format(fin_time);
                }


                StringBuffer xmlData = new StringBuffer();
                xmlData.append("<data>");
                xmlData.append("<reportId>").append(oppo_side_biz_code).append("</reportId>");
                xmlData.append("<handleName>").append(partyName).append("</handleName>");
                xmlData.append("<handleDate>").append(fin_time_str).append("</handleDate>");
                xmlData.append("<advice>").append(result).append("</advice>");
                xmlData.append("</data>");

//                String xmlData = "<data><reportId>"+oppo_side_biz_code+
//                        "</reportId><handleName>"+contact_user+
//                        "</handleName><handleDate>"+fin_time_str
//                        +"</handleDate><advice>"+result
//                        +"</advice></data>";


                String url = "http://2.18.220.149:90/mobile/zhzf/sjsb/saveFkReport.jsp";
                String exchange = exchange(url,xmlData.toString());

                DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                dataExchangeStatus.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
                dataExchangeStatus.setOppoSideBizType("2");
                dataExchangeStatus.setOppoSideBizCode(oppo_side_biz_code);
                dataExchangeStatus.setOwnSideBizType("2");
                dataExchangeStatus.setOwnSideBizCode(eventId);
                dataExchangeStatus.setStatus("1");
                dataExchangeStatus.setXmlData(xmlData.toString() + "|" + exchange);
                dataExchangeStatus.setDestPlatform("030");
                dataExchangeStatus.setSrcPlatform("000");
                this.dataExchangeStatusService.save(dataExchangeStatus);
            }
        }
    }

    private String snycData(Map<String, Object> map, String type){
        String code_ = (String)map.get("CODE_");

        String gridCode = (String)map.get("GRID_CODE");
        if(StringUtils.isNotBlank(type) && type.equals("cg")){
            System.out.println("上报城管大队");
            gridCode = "3202810cg000";
        }else if(gridCode.startsWith("320281403")){//利港街道
            gridCode = gridCode.replace("320281403","320281116");
        }else if(gridCode.startsWith("320281401")){//夏港街道
            gridCode = gridCode.replace("320281401","320281115");
        }
        String event_name = (String)map.get("EVENT_NAME");
        String contact_user = (String)map.get("CONTACT_USER");
        String tel = (String)map.get("TEL");
        String occurred = (String)map.get("OCCURRED");
        String content_ = (String)map.get("CONTENT_");
        String urgency_degree = (String)map.get("URGENCY_DEGREE");
        String influence_degree = (String)map.get("INFLUENCE_DEGREE");

        String create_time_str = "";
        if(map.get("CREATE_TIME")!=null){
            Timestamp create_time = (Timestamp)map.get("CREATE_TIME");
            create_time_str = df.format(create_time);
        }
        Timestamp handle_date = (Timestamp)map.get("HANDLE_DATE");
        String handle_date_str = df.format(handle_date);

        String event_id = map.get("EVENT_ID").toString();

        MixedGridInfo defaultGridByOrgCode = mixedGridInfoService.getDefaultGridByOrgCode(gridCode);

        ResMarker resMarkerByResId = resMarkerService.findResMarkerByResId("0301", Long.valueOf(event_id), "5");

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<data>");
        sb.append("<auth>");
        sb.append("<username>username</username>");
        sb.append("<password>password</password>");
        sb.append("</auth>");
        sb.append("<event>");
        if(map.get("OPPO_SIDE_BIZ_CODE") != null ){
                sb.append("<reportId>"+event_id+"</reportId>");
        }
        sb.append("<eventCode>"+code_+"</eventCode>");
        sb.append("<gridCode>"+gridCode+"</gridCode>");
        if(defaultGridByOrgCode!=null)
            sb.append("<gridName>"+defaultGridByOrgCode.getGridName()+"</gridName>");
        sb.append("<eventName>"+event_name+"</eventName>");
        sb.append("<happenTimeStr>"+create_time_str+"</happenTimeStr>");
        sb.append("<occurred>"+occurred+"</occurred>");
        if(resMarkerByResId!=null){
            sb.append("<x>"+resMarkerByResId.getX()+"</x>");
            sb.append("<y>"+resMarkerByResId.getY()+"</y>");
        }
        sb.append("<content>"+content_+"</content>");
        sb.append("<handleDate>"+handle_date_str+"</handleDate>");
        sb.append("<oppoSideBusiCode>"+event_id+"</oppoSideBusiCode>");
        sb.append("<urgency>"+urgency_degree+"</urgency>");
        sb.append("<influence>"+influence_degree+"</influence>");
        sb.append("<contactUserName>"+contact_user+"</contactUserName>");
        sb.append("<contactTel>"+tel+"</contactTel>");
        sb.append("<creatorName>"+contact_user+"</creatorName>");
        sb.append("<registerTimeStr>"+create_time_str+"</registerTimeStr>");
        if(map.get("REMARKS")!=null){
            sb.append("<advice>"+map.get("REMARKS")+"</advice>");
        }
        sb.append("<advice></advice>");

        List<Attachment> attachments = attachmentService.findByBizId(Long.valueOf(event_id), ConstantValue.EVENT_ATTACHMENT_TYPE);
        if(attachments!=null && attachments.size()> 0){
            logger.info(event_id);
            String img_domain = funConfigurationService.changeCodeToValue(ConstantValue.APP_DOMAIN, "$IMG_DOMAIN", IFunConfigurationService.CFG_TYPE_FACT_VAL);
            logger.info(img_domain);
            sb.append("<attrs>");
            for(Attachment attachment : attachments){
                logger.error(attachment.getFileName());
                if(attachment.getFileName().indexOf(".png") > 0
                        ||attachment.getFileName().indexOf(".jpg") > 0){
                    sb.append("<attr>");
                    String fileName = attachment.getFileName();
                    String attrBiz = fileName.substring(fileName.indexOf("."), fileName.length());

                    String path = "http://172.16.65.5:9900"+attachment.getFilePath();
                    if(StringUtils.isNotBlank(jiangyin_img_uri)){
                        path = jiangyin_img_uri+attachment.getFilePath();
                    }

//                        String path = "http://event.bug.aishequ.org/zhsq_event/images/link_17.png";
                    String encode = "";
//                        byte[] bytes = fileUploadService.downloadFile(attachment.getFilePath());
//                        logger.info(bytes);
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
                        encode = "iVBORw0KGgoAAAANSUhEUgAAABoAAAAbCAYAAABiFp9rAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJ\n" +
                                "bWFnZVJlYWR5ccllPAAAAopJREFUeNqkVltIVFEUXd7CXlP2IBkKBEXoIeHH9IAwMIUYIQIDCab8\n" +
                                "7sePJAhqvgqCIOwBffUVFIIYWlRo9JFhVA7ZE8sgkgIjpAxtyh7ItHZzcs45987jzl2whss+c+46\n" +
                                "e5+7HyWpVApziEeQA2vJMFlBTim+IZNz/zg5nHXzfOTGJnKfYnWW/9whb5GXlLgnSrJ4JCfvIGMo\n" +
                                "HCJygjxPz2btRcdjw1byqU8RQZk63E0euCyf0B5yQHlkYukqYM16YGM9ULU5/eyNKDlEsdXeoYtH\n" +
                                "5D4ekCFj27o6oLaJt7WLx7LO9e0L8OQG8LAz/WziPtnIMP7OCMUjpXwecV143QGgqT1/0L5/BXqO\n" +
                                "A6OD9soZCh3WQ9fmEokeKkxEsGQF0HoOqGmwV9roRGVaKB5ZJIEzlre1ADta4Rux08y2Dbql9P+7\n" +
                                "xaNmcqVx6Y0HUTS27LUtzXRmnqO+Eu3jbkmHIohQeZVuEScaRMisOzU7ERiSAlZuOqqGaWkXDi7k\n" +
                                "fkeFozI6g4Wh4EILFtuWkOMqhD+TwYV+/bAtSREaN0vjp+BC7nd8EKFnhmnkbnChVwO2ZdhRvSSD\n" +
                                "533A7J/iRR5fAybe6ZZJ6Vki1Gvc0+f3lO4oXihx1bb0Sn9y+DPzr1npGOoGBi/7F+k6xht/rVuk\n" +
                                "cp/SiyqLFMaMTf0skn1nCxOYmQausEi/uG2vXKAjb/31o9qot0CiB3jUBUxP2KvykW1XEXPNDNJh\n" +
                                "u1XVNbGsnEdg2VoeTuea8ONoNh/Fi3qKjOcaTmRmuO7ZzgtDP7mfIpP5hpMEyaEAnX7TlDxC7rZF\n" +
                                "co1b+lwXU3NdZRaBeyoXL1JgqtgB8iV5VLFaFWB9Uh1TCZkXfwUYALK5vFPP354bAAAAAElFTkSu\n" +
                                "QmCC";
                        e.printStackTrace();
                    }
                    sb.append("<attrURL>"+path+"</attrURL>");
                    sb.append("<attrBASE64>"+encode+"</attrBASE64>");
                    sb.append("<attrBiz>"+attrBiz +"</attrBiz>");
                    sb.append("</attr>");
                }

            }
            sb.append("</attrs>");
        }


        sb.append("</event>");
        sb.append("</data>");
        String xmlData = "";
        try {
            xmlData = URLEncoder.encode(sb.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return xmlData;
    }

    private void reportEvent(List<Long> userIds, String type){
        System.out.println(userIds);
        //获取上报事件
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userIds", userIds);
        params.put("bizPlatform", BIZ_PLATFORM);
        List<Map<String, Object>> reportEvent = this.dataExchangeStatusService.findReportEvent(params);
        if (reportEvent != null) {
            for (Map<String, Object> map : reportEvent) {
                //{EVENT_NAME=事件名称4, INVOLVED_MONEY=0, CODE_=20170620_0701_00007, CREATE_TIME=2017-06-20 20:41:03.0, CONTACT_USER=匿名, GRID_CODE=620102, START_TIME=2017-06-20 20:02:43.0, UPDATE_TIME=2017-06-20 20:41:51.0, OCCURRED=陈埭镇坊脚村委会盛兴路一工地, STATUS=02, HANDLE_DATE_FLAG=3, TYPE_=0701, HAPPEN_TIME=2017-07-31 03:47:50.0, ATTR_FLAG=0,, EVENT_ID=8769, TEL=15359469224, GRID_ID=423137, BIZ_PLATFORM=016, CONTENT_=报称：在陈埭坊脚盛兴路一工地，老板拖欠其5000元工资。, URGENCY_DEGREE=01, HANDLE_DATE=2017-06-30 20:02:43.0}

                String xmlData = snycData(map, type);

                logger.info("请求数据：");
                String exchange = exchange(jiangyin_uri,xmlData.toString());
                logger.info("返回数据："+exchange);
                String event_id = map.get("EVENT_ID").toString();
                DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                dataExchangeStatus.setExchangeFlag("1");
                dataExchangeStatus.setOppoSideBizType("2");
                dataExchangeStatus.setOppoSideBizCode(event_id);
                dataExchangeStatus.setOwnSideBizType("2");
                dataExchangeStatus.setOwnSideBizCode(event_id);
                dataExchangeStatus.setStatus("1");
                dataExchangeStatus.setXmlData(xmlData.toString() +"|" +exchange);
                dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                dataExchangeStatus.setSrcPlatform("000");
                this.dataExchangeStatusService.save(dataExchangeStatus);
            }
        }
    }

    private void report(){
        logger.info("report");
        List<Long> userIds = new ArrayList<Long>();
        List<UserBO> listByUserExampleParamsOut = userManageService.getUserListByUserExampleParamsOut(Long.valueOf(lingwu_role_id), null, null);
        for(int i=0;i<listByUserExampleParamsOut.size();i++){
            userIds.add(listByUserExampleParamsOut.get(i).getUserId());
            //每500条提交一次
            if(i%500==0){
                reportEvent(userIds, null);
                userIds = new ArrayList<Long>();
            }
        }
        reportEvent(userIds, null);
    }


    private String exchange(String url, String xml){
        String doPost = HttpUtil.sendPost(url,"data="+xml);
        return doPost;
    }

}
