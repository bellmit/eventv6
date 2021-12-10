package cn.ffcs.zhsq.event.service.stub.yanping.zhst;/**
 * Created by Administrator on 2017/8/2.
 */

import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongshm
 * @create 2017-08-02 8:56
 * 延平区 智慧生态 事件对接
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
    private final String BIZ_PLATFORM = "060";
    private String uri = "http://221.228.70.21:90/mobile/zhzf/sjsb/saveExtEReport.jsp";
    private String lingwu_role_id = "";
    private String jiangyin_user_id = "";
    private String jiangyin_img_uri = "";
    private String zhst_user_id = "";

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("========================");
        logger.info("延平区 智慧生态 事件对接");
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
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        String jiangyin_uri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        String jiangyin_img_uri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_IMG_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        String lingwu_role_id = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_ROLE_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        zhst_user_id = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "ZHST_USER_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);

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
        params.put("eventType", "0702");

        List<Long> userIds = new ArrayList<Long>();
        if(StringUtils.isNotBlank(zhst_user_id)){
            userIds.add(Long.valueOf(zhst_user_id));
        }
        userIds.add(30067524L);
        params.put("userIds", userIds);

        params.put("bizPlatform", BIZ_PLATFORM);
        List<Map<String, Object>> reportEvent = this.dataExchangeStatusService.findReportEvent(params);
        System.out.println(reportEvent);
        if (reportEvent != null) {
            for (Map<String, Object> map : reportEvent) {
                //{EVENT_NAME=事件名称4, INVOLVED_MONEY=0, CODE_=20170620_0701_00007, CREATE_TIME=2017-06-20 20:41:03.0, CONTACT_USER=匿名, GRID_CODE=620102, START_TIME=2017-06-20 20:02:43.0, UPDATE_TIME=2017-06-20 20:41:51.0, OCCURRED=陈埭镇坊脚村委会盛兴路一工地, STATUS=02, HANDLE_DATE_FLAG=3, TYPE_=0701, HAPPEN_TIME=2017-07-31 03:47:50.0, ATTR_FLAG=0,, EVENT_ID=8769, TEL=15359469224, GRID_ID=423137, BIZ_PLATFORM=016, CONTENT_=报称：在陈埭坊脚盛兴路一工地，老板拖欠其5000元工资。, URGENCY_DEGREE=01, HANDLE_DATE=2017-06-30 20:02:43.0}
                String code_ = (String)map.get("CODE_");
                String gridCode = (String)map.get("GRID_CODE");
                String event_name = (String)map.get("EVENT_NAME");
                String contact_user = (String)map.get("CONTACT_USER");
                String tel = (String)map.get("TEL");
                String occurred = (String)map.get("OCCURRED");
                String content_ = (String)map.get("CONTENT_");
                String urgency_degree = (String)map.get("URGENCY_DEGREE");
                String influence_degree = (String)map.get("INFLUENCE_DEGREE");

                String event_id = map.get("EVENT_ID").toString();


                ResMarker resMarkerByResId = resMarkerService.findResMarkerByResId("0301", Long.valueOf(event_id), "5");

                String result = "";
                try {
                    XmhbSjjhServiceStub stub = new XmhbSjjhServiceStub();
                    XmhbSjjhServiceStub.AddEvent addEvent = new XmhbSjjhServiceStub.AddEvent();
                    addEvent.setAddr(occurred);
                    addEvent.setDescription(content_);
                    if(resMarkerByResId != null){
                        String x = resMarkerByResId.getX();
                        String y = resMarkerByResId.getY();
                        addEvent.setLatitude(Double.valueOf(x));
                        addEvent.setLongitude(Double.valueOf(y));
                    }
                    XmhbSjjhServiceStub.AddEventResponse addEventResponse = stub.addEvent(addEvent);
                    result = addEventResponse.get_return();
                    System.out.println(result);
                } catch (AxisFault axisFault) {
                    axisFault.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


                DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                dataExchangeStatus.setExchangeFlag("1");
                dataExchangeStatus.setOppoSideBizType("2");
                dataExchangeStatus.setOppoSideBizCode("");
                dataExchangeStatus.setOwnSideBizType("2");
                dataExchangeStatus.setOwnSideBizCode(event_id);
                dataExchangeStatus.setStatus("1");
                dataExchangeStatus.setXmlData(result);
                dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                dataExchangeStatus.setSrcPlatform("000");
                this.dataExchangeStatusService.save(dataExchangeStatus);
            }
        }
    }


    private String exchange(String url, String xml){
        String doPost = HttpUtil.sendPost(url,"data="+xml);
        return doPost;
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}
