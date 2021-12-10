package cn.ffcs.zhsq.event.service.stub.tongan;

import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.APIHttpClient;
import cn.ffcs.shequ.utils.Base64;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.FunConfigureSetting;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dataExchange.service.IBasDataExchangeService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.BasDataExchange;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.ConstantValue;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

/**
 *
 */
public class QuartzJob implements Job {

    private final Logger logger = LoggerFactory.getLogger("dailyRollingFile");

    private IBasDataExchangeService basDataExchangeService;
    private IFunConfigurationService funConfigurationService;
    private IMixedGridInfoService mixedGridInfoService;
    private IEventDisposalDockingService eventDisposalDockingService;
    private IDataExchangeStatusService dataExchangeStatusService;
    private IEventDisposalWorkflowService eventDisposalWorkflowService;
    private FileUploadService fileUploadService;
    private IAttachmentService attachmentService;

    //业务编码
    private final String BIZ_PLATFORM = "021";
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("===================================================");
        logger.info("同安事件对接");
        logger.info("===================================================");
        try {
            SchedulerContext schCtx = context.getScheduler().getContext();
            ApplicationContext appCtx = (ApplicationContext) schCtx.get("applicationContext");
            this.basDataExchangeService = (IBasDataExchangeService) appCtx.getBean("basDataExchangeServiceImpl");
            this.funConfigurationService = (IFunConfigurationService) appCtx.getBean("funConfigurationService");
            this.mixedGridInfoService = (IMixedGridInfoService) appCtx.getBean("mixedGridInfoService");
            this.eventDisposalDockingService = (IEventDisposalDockingService) appCtx.getBean("eventDisposalDockingServiceImpl");
            this.dataExchangeStatusService = (IDataExchangeStatusService) appCtx.getBean("dataExchangeStatusServiceImpl");
            this.eventDisposalWorkflowService = (IEventDisposalWorkflowService) appCtx.getBean("eventDisposalWorkflowService");
            this.attachmentService = (IAttachmentService) appCtx.getBean("attachmentService");
            this.fileUploadService = (FileUploadService) appCtx.getBean("fileUploadService");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("exchangeType","002");//
        BasDataExchange basDataExchange = basDataExchangeService.findBasDataExchange(params);
        if (basDataExchange != null){
            Date timeStamp = basDataExchange.getTimeStamp();
            String startTime = DateUtils.formatDate(timeStamp, "yyyyMMddHHmmss");

            logger.info("startTime:"+startTime);
            String endTime = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");

            logger.info("endTime:"+endTime);
            int pageNo = 0;
            int pageSize = 200;

            String jsonStr = doGetData(pageNo, pageSize, startTime, endTime);
            logger.info(jsonStr);
            save(jsonStr);
			BasDataExchange nextBasDataExchange = new BasDataExchange();
			nextBasDataExchange.setExchangeId(basDataExchange.getExchangeId());
            Calendar cal = Calendar.getInstance();
			nextBasDataExchange.setTimeStamp(cal.getTime());
			this.basDataExchangeService.updateBasDataExchange(nextBasDataExchange);

        }else{
            logger.error("未查到上一有效时间。");
        }

    }

    private String doGetData(int pageNo, int pageSize, String startTime, String endTime) {
//        String url = "http://120.24.152.232/tadc/incident/list/06";
        String url = "http://120.24.152.232/tadc/incident/list/06";

        JSONObject params = new JSONObject();
        params.put("pageNo",pageNo);
        params.put("pageSize",pageSize);
		params.put("beginTime",startTime);
		params.put("endTime",endTime);
//        params.put("beginTime", "20170101000000");
//        params.put("endTime","20170101120000");
        APIHttpClient ac = new APIHttpClient(url);
        String result = ac.post(params.toString());
        return result;
    }

    private String getTask(String incidentNo){
        String url = "http://120.24.152.232/tadc/incident/log/"+incidentNo;
        APIHttpClient ac = new APIHttpClient(url);
        String result = ac.post("");
        logger.info("getTask---"+result);
        return result;
    }

    private void save(String jsonStr) {
        if(StringUtils.isBlank(jsonStr)) return;
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        JSONArray rows = jsonObject.getJSONArray("list");
        for (Object tempJsonObj : rows) {
            EventDisposal event = new EventDisposal();

            JSONObject jsonRows = (JSONObject) tempJsonObj;

            String incidentNo = jsonRows.get("incidentNo").toString();


            //事件所属区域
            String tonganGridCode = "350212";
            List<MixedGridInfo> listGrids = mixedGridInfoService.getMixedGridMappingListByOrgCode(tonganGridCode);
            event.setGridCode(tonganGridCode);
            event.setGridId(listGrids.get(0).getGridId());

            //事件类型
            if (jsonRows.get("incidentType") != null) {
                String incidentType = jsonRows.get("incidentType").toString();
                event.setType(incidentType);
            }

            //联系人
            if (jsonRows.get("fourthGridMan") != null) {
                String fourthGridMan = jsonRows.get("fourthGridMan").toString();
                event.setContactUser(fourthGridMan);
            }else{
                if(jsonRows.get("thirdhGridMan") != null){
                    String thirdhGridMan = jsonRows.get("thirdhGridMan").toString();
                    event.setContactUser(thirdhGridMan);
                }
            }

            //事件标题
            event.setEventName("同安区事件");
            //发生时间
            if (jsonRows.get("createTime") != null) {//发生时间
                String happenTimeRow = jsonRows.get("createTime").toString();
                Date date = DateUtils.formatDate(happenTimeRow, "yyyyMMddHHmmss");
                String happenTimeStr = DateUtils.formatDate(date, DateUtils.PATTERN_24TIME);
                event.setHappenTimeStr(happenTimeStr);
            }
            //事件类型
            event.setType("3199");
            //事发地点
            if (jsonRows.get("address") != null) {
                String occurred = jsonRows.get("address").toString();
                event.setOccurred(occurred);
            }
            //事发描述
            if (jsonRows.get("reportRemark") != null) {
                String content = jsonRows.get("reportRemark").toString();
                event.setContent(content);
                if(content.length()>60){
                    content = content.substring(0,60);
                    event.setEventName(content);
                }else{
                    event.setEventName(content);
                }
            }
            event.setSource("41");//同安
            //
//            event.setContactUser();


            UserInfo userinfo = getUserInfo();
            Map<String, Object> stringObjectMap = eventDisposalDockingService.saveEventDisposalAndEvaluate(event, userinfo, "");
            System.out.println(stringObjectMap);

            String eventId = stringObjectMap.get("eventId").toString();

            //图片
            if (jsonRows.get("finishPhotoUrls") != null) {
                String finishPhotoUrls = jsonRows.get("finishPhotoUrls").toString();
                byte[] decode = Base64.decode(finishPhotoUrls);
                Attachment attachment = new Attachment();
                attachment.setBizId(Long.valueOf(eventId));
                attachment.setFileName("temp.jpg");
                attachment.setAttachmentType(ConstantValue.EVENT_ATTACHMENT_TYPE);
                attachment.setEventSeq("3");
                String imgUrl = fileUploadService.uploadSingleFile("temp.jpg", decode, "ZHSQ_EVENT", "perfile");
                attachment.setFilePath(imgUrl);
                Long r = attachmentService.saveAttachment(attachment);
            }
            //图片
            if (jsonRows.get("reportPhotoUrls") != null) {
                String reportPhotoUrls = jsonRows.get("reportPhotoUrls").toString();
                byte[] decode = Base64.decode(reportPhotoUrls);
                Attachment attachment = new Attachment();
                attachment.setBizId(Long.valueOf(eventId));
                attachment.setFileName("temp.jpg");
                attachment.setAttachmentType(ConstantValue.EVENT_ATTACHMENT_TYPE);
                attachment.setEventSeq("1");
                String imgUrl = fileUploadService.uploadSingleFile("temp.jpg", decode, "ZHSQ_EVENT", "perfile");
                attachment.setFilePath(imgUrl);
                Long r = attachmentService.saveAttachment(attachment);
            }

            String task = getTask(incidentNo);
            JSONObject taskJsonObject = JSONObject.fromObject(task);
            JSONArray taskRows = taskJsonObject.getJSONArray("list");
            for (Object taskTemp : taskRows){
                JSONObject jsonTaskRows = (JSONObject) taskTemp;

                if(jsonTaskRows.get("deptName")!=null){
                    String transactOrgName = jsonTaskRows.get("deptName").toString();
                    if(!transactOrgName.equals("")){
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("eventId",stringObjectMap.get("eventId"));
                        params.put("transactOrgName",transactOrgName);
                        String remark = jsonTaskRows.get("remark").toString();
                        params.put("taskAdvice",remark);
                        String createUserName = jsonTaskRows.get("userName").toString();
                        params.put("createUserName",createUserName);
                        params.put("optType","1");
                        String oppoSideBizCode = jsonTaskRows.get("id").toString();
                        params.put("oppoSideBizCode",oppoSideBizCode);
                        params.put("oppoSideBizType","0");
                        String logTypeName = jsonTaskRows.get("logTypeName").toString();
                        params.put("taskName",logTypeName);

                        String createTime = jsonTaskRows.get("createTime").toString();
                        Date date = DateUtils.formatDate(createTime, "yyyyMMddHHmmss");
                        createTime = DateUtils.formatDate(date, DateUtils.PATTERN_24TIME);

                        params.put("taskCreateTime",createTime);
                        params.put("taskEndTime",createTime);
                        params.put("srcPlatform",this.BIZ_PLATFORM);
                        System.out.println("+++++++++++++++++++");
                        System.out.println(params);
                        Long record = eventDisposalWorkflowService.saveOrUpdateTask(params);
                        System.out.println("record->"+record);
                    }
                }
            }

            //事件编号
            String outEventId = "";
            if (jsonRows.get("incidentNo") != null) {
                outEventId = jsonRows.get("incidentNo").toString();
            }
            DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
            dataExchangeStatus.setExchangeFlag("1");
            dataExchangeStatus.setOppoSideBizCode(outEventId);
            dataExchangeStatus.setOwnSideBizCode(stringObjectMap.get("eventId").toString());
            dataExchangeStatus.setStatus("1");
            dataExchangeStatus.setXmlData(tempJsonObj.toString());
            dataExchangeStatus.setSrcPlatform(this.BIZ_PLATFORM);
            dataExchangeStatusService.save(dataExchangeStatus);
//            break;
        }
    }

    private UserInfo getUserInfo(){
        UserInfo userInfo = new UserInfo();
        List<FunConfigureSetting> env_event = funConfigurationService.findConfigureSettingLatestList("ENV_EVENT", null, null, null, null, 0);
        for(FunConfigureSetting setting : env_event){
            if(setting.getTrigCondition().equals("userId")){
                if(StringUtils.isNotBlank(setting.getCfgVal()))
                    userInfo.setUserId(Long.valueOf(setting.getCfgVal()));
            }
            if(setting.getTrigCondition().equals("partyId")){
                if(StringUtils.isNotBlank(setting.getCfgVal()))
                    userInfo.setPartyId(Long.valueOf(setting.getCfgVal()));
            }
            if(setting.getTrigCondition().equals("userName")){
                if(StringUtils.isNotBlank(setting.getCfgVal()))
                    userInfo.setUserName(setting.getCfgVal());
            }
            if(setting.getTrigCondition().equals("partyName")){
                if(StringUtils.isNotBlank(setting.getCfgVal()))
                    userInfo.setPartyName(setting.getCfgVal());
            }
            if(setting.getTrigCondition().equals("orgId")){
                if(StringUtils.isNotBlank(setting.getCfgVal()))
                    userInfo.setOrgId(Long.valueOf(setting.getCfgVal()));
            }
            if(setting.getTrigCondition().equals("catalogInfoId")){
                if(StringUtils.isNotBlank(setting.getCfgVal()))
                    userInfo.setCatalogInfoId(Long.valueOf(setting.getCfgVal()));
            }
            if(setting.getTrigCondition().equals("orgName")){
                if(StringUtils.isNotBlank(setting.getCfgVal()))
                    userInfo.setOrgName(setting.getCfgVal());
            }
            if(setting.getTrigCondition().equals("orgCode")){
                if(StringUtils.isNotBlank(setting.getCfgVal()))
                    userInfo.setOrgCode(setting.getCfgVal());
            }
        }
        return userInfo;
    }
}
