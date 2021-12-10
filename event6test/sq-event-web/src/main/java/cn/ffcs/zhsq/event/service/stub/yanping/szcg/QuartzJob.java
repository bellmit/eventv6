package cn.ffcs.zhsq.event.service.stub.yanping.szcg;/**
 * Created by Administrator on 2017/8/2.
 */

import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongshm
 * @create 2017-08-02 8:56
 * 延平区 数字城管 事件对接
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
    private IBaseDictionaryService dictionaryService;
    private SXDataPoolWebServiceStub sXDataPoolWebServiceStub;
//    private FileUploadService fileUploadService;

    //业务编码
    private final String BIZ_PLATFORM = "060";
    private String uri = "http://zhcg.iyanping.org.cn:8082/webservice/SXDataPoolWebService?wsdl";
    private String lingwu_role_id = "";
    private String jiangyin_user_id = "";
    private String jiangyin_img_uri = "";
    private String szcg_user_id = "";

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("========================");
        logger.info("延平区 数字城管 事件对接");
        logger.info("========================");
        try {
            SchedulerContext schCtx = context.getScheduler().getContext();
            ApplicationContext appCtx = (ApplicationContext) schCtx.get("applicationContext");
            String applicationName = appCtx.getApplicationName();
            this.dataExchangeStatusService = (IDataExchangeStatusService) appCtx.getBean("dataExchangeStatusServiceImpl");
            this.iDataExchangeStatusTwoWayService = (IDataExchangeStatusTwoWayService) appCtx.getBean("dataExchangeStatusTwoWayServiceImpl");
            this.funConfigurationService = (IFunConfigurationService) appCtx.getBean("funConfigurationService");
            this.attachmentService = (IAttachmentService) appCtx.getBean("attachmentService");
            this.mixedGridInfoService = (IMixedGridInfoService) appCtx.getBean("mixedGridInfoService");
            this.userManageService = (UserManageOutService) appCtx.getBean("userManageService");
            this.resMarkerService = (IResMarkerService) appCtx.getBean("resMarkerService");
            this.dictionaryService = (IBaseDictionaryService) appCtx.getBean("baseDictionaryService");
            this.sXDataPoolWebServiceStub = (SXDataPoolWebServiceStub) appCtx.getBean("sXDataPoolWebServiceStub");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        String jiangyin_uri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        String jiangyin_img_uri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_IMG_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        String lingwu_role_id = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_ROLE_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        szcg_user_id = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "SZCG_USER_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);

//        jiangyin_user_id = "";//30003111



//        if(StringUtils.isBlank(jiangyin_uri)){
//            logger.error("功能配置【LINGWU_URI】未配置！");
//            return;
//        }else{
//            this.uri = jiangyin_uri;
//        }
//        if(StringUtils.isBlank(lingwu_role_id)){
//            logger.error("功能配置【LINGWU_ROLE_ID】未配置！");
//            return;
//        }else{
//            this.lingwu_role_id = lingwu_role_id;
//        }
//        if(StringUtils.isBlank(jiangyin_user_id)){
//            logger.error("功能配置【LINGWU_USER_ID】未配置！");
//            return;
//        }else{
//            this.jiangyin_user_id = jiangyin_user_id;
//        }
//        if(StringUtils.isBlank(jiangyin_img_uri)){
//            logger.error("功能配置【LINGWU_IMG_URI】未配置！");
//            return;
//        }else{
//            this.jiangyin_img_uri = jiangyin_img_uri;
//        }

        report();
//        close();

    }

    private void report(){
        //获取上报事件
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eventType", "0701");

        List<Long> userIds = new ArrayList<Long>();
        if(StringUtils.isNotBlank(szcg_user_id)){
            userIds.add(Long.valueOf(szcg_user_id));
        }
        userIds.add(30067524L);
        params.put("userIds", userIds);

        params.put("bizPlatform", BIZ_PLATFORM);
        List<Map<String, Object>> reportEvent = this.dataExchangeStatusService.findReportEvent(params);
        System.out.println(reportEvent);
        //[{EVENT_NAME=的萨达, INVOLVED_MONEY=0, CODE_=20171020_0701_00016,
        // CREATE_TIME=2017-10-20 11:07:07.0, CONTACT_USER=十大, GRID_CODE=350701001008,
        // START_TIME=2017-10-20 10:42:55.0, UPDATE_TIME=2017-10-20 17:01:28.0, STATUS=02,
        // HANDLE_DATE_FLAG=1, TYPE_=0702, HAPPEN_TIME=2017-09-15 18:41:45.0, ATTR_FLAG=0,,
        // EVENT_ID=9718, TEL=15111111111, GRID_ID=13519, BIZ_PLATFORM=202, CONTENT_=的维权二群翁无群二,
        // URGENCY_DEGREE=01, HANDLE_DATE=2017-10-30 10:42:55.0}]

        if(reportEvent!=null && reportEvent.size()>0){
            for(Map<String, Object> report : reportEvent){
                String event_id = report.get("EVENT_ID").toString();
                String eventType = report.get("TYPE_").toString();
                String occurred = "空";
                if(report.get("OCCURRED")!=null){
                    occurred = report.get("OCCURRED").toString();
                }
                String content_ = report.get("CONTENT_").toString();
                String typeName = "";
                String typeClass = "";
                String x = "";
                String y = "";


                ResMarker resMarkerByResId = resMarkerService.findResMarkerByResId("0301", Long.valueOf(event_id), "5");
                if(resMarkerByResId != null){
                    x = resMarkerByResId.getX();
                    y = resMarkerByResId.getY();
                }

                Map<String, Object> dictMap = new HashMap<String, Object>();
//                dictMap.put("orgCode", orgCode);
                dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
                List<BaseDataDict> eventTypeDict = dictionaryService.findDataDictListByCodes(dictMap);

                if(StringUtils.isNotBlank(eventType) && eventTypeDict != null) {
                    StringBuffer eventClass = new StringBuffer("");
                    String bigType = eventType, bigTypeName = "", bigDictCode = null;

                    do {
                        bigTypeName = "";

                        for(BaseDataDict dataDict : eventTypeDict) {
                            if((StringUtils.isNotBlank(bigDictCode) && !ConstantValue.BIG_TYPE_PCODE.equals(bigDictCode) && bigDictCode.equals(dataDict.getDictCode()))
                                    ||
                                    (StringUtils.isNotBlank(bigType) && bigType.equals(dataDict.getDictGeneralCode()))) {
                                bigTypeName = dataDict.getDictName();
                                bigDictCode = dataDict.getDictPcode();
                                bigType = null;
                                break;
                            }
                        }

                        if(StringUtils.isNotBlank(bigTypeName)) {
                            eventClass.insert(0, bigTypeName).insert(0, "-");
                        }
                    } while(StringUtils.isNotBlank(bigTypeName));

                    if(eventClass.length() > 0) {
                        typeName = eventClass.substring(eventClass.lastIndexOf("-") + 1);
                        typeClass = eventClass.substring(1);
                    }
                }


                try {
//                    SXDataPoolWebServiceStub stub = new SXDataPoolWebServiceStub(uri);

                    SXDataPoolWebServiceStub.SendToCenterE sendToCenterE = new SXDataPoolWebServiceStub.SendToCenterE();

                    SXDataPoolWebServiceStub.SendToCenter sendToCenter = new SXDataPoolWebServiceStub.SendToCenter();


                    StringBuffer requestParams = new StringBuffer();
                    requestParams.append("<request>");
                    requestParams.append("<params>");
                    requestParams.append("<Event>");
                    requestParams.append("<TaskNum>"+event_id+"</TaskNum>");
                    requestParams.append("<LocationX>"+x+"</LocationX>");
                    requestParams.append("<LocationY>"+y+"</LocationY>");
                    requestParams.append("<EventType>02</EventType>");
                    requestParams.append("<MainType>"+typeClass+"</MainType>");
                    requestParams.append("<SubType>"+typeName+"</SubType>");
                    requestParams.append("<EventAddr>"+occurred+"</EventAddr>");
                    requestParams.append("<EventDescription>"+content_+"</EventDescription>");
                    requestParams.append("<picData num='0'></picData>");
                    requestParams.append("<wavData num='0'></wavData>");
                    requestParams.append("</Event>");
                    requestParams.append("</params>");
                    requestParams.append("</request>");

                    System.out.println(requestParams.toString());
                    sendToCenter.setArg0(requestParams.toString());



                    sendToCenterE.setSendToCenter(sendToCenter);

                    SXDataPoolWebServiceStub.SendToCenterResponseE sendToCenterResponseE = sXDataPoolWebServiceStub.sendToCenter(sendToCenterE);
                    String aReturn = sendToCenterResponseE.getSendToCenterResponse().get_return();
                    System.out.println(aReturn);

                    saveEventDataExchangeStatus(BIZ_PLATFORM, "0", event_id, aReturn);
                } catch (AxisFault axisFault) {
                    axisFault.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (ExceptionException e) {
                    e.printStackTrace();
                }

            }
        }



    }


    private Boolean saveEventDataExchangeStatus(String bizPlatform, String oppo, String own, String xmlData){
        DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
        dataExchangeStatus.setDestPlatform(IDataExchangeStatusService.PLATFORM_EVENT_NEW);
        dataExchangeStatus.setSrcPlatform(bizPlatform);
        dataExchangeStatus.setOppoSideBizCode(oppo);
        dataExchangeStatus.setOppoSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
        dataExchangeStatus.setOwnSideBizCode(own);
        dataExchangeStatus.setOwnSideBizType(IDataExchangeStatusService.BIZ_TYPE_EVENT_NEW);
        dataExchangeStatus.setExchangeFlag(IDataExchangeStatusService.EXCHANGE_FLAG_EXCHANGED);
        dataExchangeStatus.setStatus(IDataExchangeStatusService.STATUS_VALIDATE);
        dataExchangeStatus.setXmlData(xmlData);
        return dataExchangeStatusService.saveDataExchangeStatus(dataExchangeStatus) > 0;
    }

}
