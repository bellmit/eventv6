package cn.ffcs.zhsq.event.service.stub.nanchang;/**
 * Created by Administrator on 2017/8/2.
 */

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.APIHttpClient;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.dispute.IDisputeMediationService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.utils.ConstantValue;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 南昌12345 事件对接定时器
 */
public class QuartzJob implements Job {

    private final Logger logger = LoggerFactory.getLogger("dailyRollingFile");

    private IDataExchangeStatusService dataExchangeStatusService;
    private IDataExchangeStatusTwoWayService iDataExchangeStatusTwoWayService;
    private IFunConfigurationService funConfigurationService;
    private IAttachmentService attachmentService;
    protected IMixedGridInfoService mixedGridInfoService;
    private UserManageOutService userManageService;
    private IResMarkerService resMarkerService;
    private IEventDisposalService eventDisposalService;
    private IEventDisposalDockingService eventDisposalDockingService;
    private IDisputeMediationService disputeMediationService;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final String BIZ_PLATFORM = "044";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("=====================================================start===================================================");
        try {
            SchedulerContext schCtx = context.getScheduler().getContext();
            ApplicationContext appCtx = (ApplicationContext) schCtx.get("applicationContext");
            this.dataExchangeStatusService = (IDataExchangeStatusService) appCtx.getBean("dataExchangeStatusServiceImpl");
            this.iDataExchangeStatusTwoWayService = (IDataExchangeStatusTwoWayService) appCtx.getBean("dataExchangeStatusTwoWayServiceImpl");
            this.funConfigurationService = (IFunConfigurationService) appCtx.getBean("funConfigurationService");
            this.attachmentService = (IAttachmentService) appCtx.getBean("attachmentService");
            this.mixedGridInfoService = (IMixedGridInfoService) appCtx.getBean("mixedGridInfoService");
            this.userManageService = (UserManageOutService) appCtx.getBean("userManageService");
            this.resMarkerService = (IResMarkerService) appCtx.getBean("resMarkerService");
            this.eventDisposalService = (IEventDisposalService) appCtx.getBean("eventDisposalServiceImpl");
            this.eventDisposalDockingService = (IEventDisposalDockingService) appCtx.getBean("eventDisposalDockingServiceImpl");
            this.disputeMediationService = (IDisputeMediationService) appCtx.getBean("disputeMediationService");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        String nc12345RXUserid = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "NC12345RX_USERID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        if(StringUtils.isBlank(nc12345RXUserid)){
            logger.error("[NC12345RX_USERID]南昌12345热线用户id，未配置！");
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", nc12345RXUserid);
        params.put("eventType", "05");
        params.put("bizPlatform", BIZ_PLATFORM);
        List<Map<String, Object>> reportEvent = this.dataExchangeStatusService.findReportEvent(params);
        if(reportEvent!=null && reportEvent.size()>0){
            for(Map<String, Object> event : reportEvent){
                System.out.println(event);
                //{GROUPID_=143292, USERID_=30003533, CREATE_TIME=2018-03-19 14:44:31.0,
                // GRID_CODE=3601, STATUS=00, SOURCE=01, COLLECT_WAY=02, TEL=13200000003, GRID_ID=407596,
                // CONTENT_=t0319_矛盾纠纷_a, URGENCY_DEGREE=01, EVENT_NAME=t0319_矛盾纠纷_a, CREATOR_ID=30003533,
                // INVOLVED_MONEY=0, CODE_=20180319_5006_00004, INSTANCE_ID=15920090, CONTACT_USER=南昌市, START_TIME=2018-03-19 14:44:28.0,
                // OCCURRED=t0319_矛盾纠纷_a, UPDATE_TIME=2018-03-19 14:45:15.0, INFLUENCE_DEGREE=01, TYPE_=5006, HANDLE_DATE_FLAG=3,
                // INVOLVED_NUM_INT=0, HAPPEN_TIME=2018-03-19 14:02:11.0,
                // IDENTITY_CARD=360121199007193870, ATTR_FLAG=0,, EVENT_ID=24943, INVOLVED_NUM=00, HANDLE_DATE=2018-04-02 14:44:28.0}
                String eventId = "";
                if(null != event.get("EVENT_ID")){
                    eventId = event.get("EVENT_ID").toString();
                }
                String eventName = "";
                if(null != event.get("EVENT_NAME")){
                    eventName = event.get("EVENT_NAME").toString();
                }
                String context_ = "";
                if(null != event.get("CONTENT_")){
                    context_ = event.get("CONTENT_").toString();
                }
                String occurred = "";
                if(null != event.get("OCCURRED")){
                    occurred = event.get("OCCURRED").toString();
                }
                String contactUser = "";
                if(null != event.get("CONTACT_USER")){
                    contactUser = event.get("CONTACT_USER").toString();
                }
                String tel_ = "";
                if(null != event.get("TEL")){
                    tel_ = event.get("TEL").toString();
                }
                String gridCode = "";
                String gridName = "";
                if(null != event.get("GRID_CODE")){
                    gridCode = event.get("GRID_CODE").toString();
                    MixedGridInfo defaultGridByOrgCode = mixedGridInfoService.getDefaultGridByOrgCode(gridCode);
                    System.out.println(defaultGridByOrgCode.getGridName());
                    gridName = defaultGridByOrgCode.getGridName();

                }
                String userId = "";
                String identityCard = "";
                String gender = "";
                if(null != event.get("USERID_")){
                    userId = event.get("USERID_").toString();
                    if(StringUtils.isNotBlank(userId)){
                        UserBO userInfoByUserId = userManageService.getUserInfoByUserId(Long.valueOf(userId));
                        identityCard = userInfoByUserId.getIdentityCard();
                        gender = userInfoByUserId.getGender();
                    }

                }
                String result = sync(context_, contactUser, tel_, occurred, gridName, identityCard, gender);

                //{"resData":{"returnInfo":{"formId":"2018050216334933492387087","operCode":"1","operMsg":"操作成功!"},"system":{"code":"1","msg":"调用成功!"}}}
                JSONObject jsonObject = JSONObject.fromObject(result);
                if(jsonObject.get("resData")!=null){
                    JSONObject resData = JSONObject.fromObject(jsonObject.get("resData"));
                    if(null != resData.get("returnInfo")){
                        JSONObject returnInfo = JSONObject.fromObject(resData.get("returnInfo"));
                        if(null != returnInfo.get("operCode") &&
                                returnInfo.get("operCode").toString().equals("1")){//操作成功
                            String formId = returnInfo.get("formId").toString();

                            DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                            dataExchangeStatus.setOppoSideBizCode(formId);
                            dataExchangeStatus.setExchangeFlag("1");
                            dataExchangeStatus.setOwnSideBizCode(eventId);
                            dataExchangeStatus.setStatus("1");
                            dataExchangeStatus.setXmlData(result);
                            dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                            dataExchangeStatus.setSrcPlatform("000");
                            dataExchangeStatus.setOppoSideBizType("2");
                            dataExchangeStatus.setOwnSideBizType("2");
                            this.dataExchangeStatusService.save(dataExchangeStatus);
                        }
                    }
                }
            }
        }
        logger.info("=====================================================end===================================================");
    }

    private String sync(String context_, String contactUser, String tel_,
                      String occurred, String gridName, String identityCard, String gender){

        String url = "http://12345.yw.nc.gov.cn/nc12345/inter/order/addNetWorkOrder.inter?action=addNetWorkOrder";
        APIHttpClient ac = new APIHttpClient(url);

        JSONObject paras = new JSONObject();
        paras.put("formOrigin", "综治网格化");//固定值
        paras.put("contentText", context_);//工单内容
        paras.put("cusType", "1");//客户类型
        paras.put("cusSex", StringUtils.isNotBlank(gender)?gender:"");//客户性别 事件创建人性别 无则显示（1）男
        paras.put("cusName", contactUser);//市民姓名
        paras.put("cusPhone", tel_);//市民联系电话
        paras.put("cusAddress", occurred);//cusAddress
        paras.put("idCardNum", identityCard);//身份证号码
        paras.put("whConPerInfo", "1");//是否公开市民信息 默认1（不公开）
        paras.put("areaCode", gridName);//区域名称

        JSONObject reqData = new JSONObject();
        reqData.put("paras",paras);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reqData",reqData);
        logger.info(jsonObject.toString());
        String result = ac.post(jsonObject.toString());
        //{"resData":{"returnInfo":{"formId":"2018050216334933492387087","operCode":"1","operMsg":"操作成功!"},"system":{"code":"1","msg":"调用成功!"}}}
        System.out.println("=====================================");
        System.out.println(result);
        System.out.println("=====================================");
        return result;

    }

}
