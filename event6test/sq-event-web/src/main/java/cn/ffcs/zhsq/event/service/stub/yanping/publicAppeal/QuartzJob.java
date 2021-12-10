package cn.ffcs.zhsq.event.service.stub.yanping.publicAppeal;/**
 * Created by Administrator on 2017/8/2.
 */

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.domain.publicAppeal.PublicAppeal;
import cn.ffcs.zhsq.publicAppeal.service.PublicAppealService;
import cn.ffcs.zhsq.utils.ConstantValue;
import oracle.sql.TIMESTAMP;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongshm
 * @create 2017-08-02 8:56
 * 延平区 诉求 事件对接
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
    private PublicAppealService publicAppealService; //注入公众诉求模块服务
    private IEventDisposalWorkflowService eventDisposalWorkflowService;

    //业务编码
    private final String BIZ_PLATFORM = "202";
    private String uri = "http://221.228.70.21:90/mobile/zhzf/sjsb/saveExtEReport.jsp";
    private String lingwu_role_id = "";
    private String jiangyin_user_id = "";
    private String jiangyin_img_uri = "";

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("========================");
        logger.info("延平区 诉求 事件对接");
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
            this.publicAppealService = (PublicAppealService) appCtx.getBean("publicAppealServiceImpl");
            this.eventDisposalWorkflowService = (IEventDisposalWorkflowService) appCtx.getBean("eventDisposalWorkflowServiceImpl");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        String jiangyin_uri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        String jiangyin_img_uri = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_IMG_URI", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        String lingwu_role_id = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_ROLE_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        String jiangyin_user_id = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "LINGWU_USER_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL);

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

        reportTask();
        closeEvent();

    }

    private void closeEvent(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eventSrcPlatform", BIZ_PLATFORM);
        params.put("eventDescPlatform", "000");
        params.put("srcPlatform", "000");
        params.put("descPlatform", BIZ_PLATFORM);

        List<Map<String, Object>> closeEvent = dataExchangeStatusService.findCloseEvent(params);
        System.out.println(closeEvent);
        //[{CREATE_TIME=2017-10-20 11:30:52.0, SRC_PLATFORM=202, GRID_CODE=350701001008, STATUS=04, DEST_PLATFORM=000, OWN_SIDE_BIZ_CODE=9722, RESULT=ccc, TEL=15111111111, GRID_ID=13519, CONTENT_=的维权二群翁无群二, BIZ_PLATFORM=202, INTER_ID=1208, OPPO_SIDE_BIZ_TYPE=2, EXCHANGE_FLAG=1, URGENCY_DEGREE=01, EVENT_NAME=的萨达, INVOLVED_MONEY=0, CODE_=20171020_0701_00020, USER_ID=30003191, CONTACT_USER=十大, OWN_SIDE_BIZ_TYPE=2, UPDATE_TIME=2017-10-20 15:34:53.0, TYPE_=0701, HANDLE_DATE_FLAG=1, HAPPEN_TIME=2017-09-15 18:41:45.0, ATTR_FLAG=0,, EVENT_ID=9722, OPPO_SIDE_BIZ_CODE=56, FIN_TIME=2017-10-20 14:46:11.0, STATUS_=1, HANDLE_DATE=2017-10-30 11:06:46.0}]
        if(closeEvent!=null && closeEvent.size() > 0){
            for(Map<String, Object> event : closeEvent){
                String event_id = event.get("EVENT_ID").toString();
                PublicAppeal publicAppeal = this.publicAppealService.searchByEventId(Long.valueOf(event_id));
                if(event.get("RESULT")!=null){
                    String  result = event.get("RESULT").toString();
                    publicAppeal.setHandleRs(result);
                }
                publicAppeal.setHandleSit(ConstantValue.PUBLIC_APPEAL_HANDLESIT_03);
                boolean update = publicAppealService.update(publicAppeal);

                DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                dataExchangeStatus.setExchangeFlag("1");
                dataExchangeStatus.setOppoSideBizCode(publicAppeal.getOutId().toString());
                dataExchangeStatus.setOppoSideBizType("2");
                dataExchangeStatus.setOwnSideBizCode(event_id);
                dataExchangeStatus.setOwnSideBizType("2");
                dataExchangeStatus.setStatus("1");
                dataExchangeStatus.setXmlData("");
                dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                dataExchangeStatus.setSrcPlatform("000");
                this.dataExchangeStatusService.save(dataExchangeStatus);
            }
        }
    }

    private void reportTask(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("srcPlatform", BIZ_PLATFORM);
        params.put("descPlatform", "000");
        params.put("operateType", "1");
        List<Map<String, Object>> subTaskAppointed = this.dataExchangeStatusService.findTaskEvent(params);

        if(subTaskAppointed!=null&&subTaskAppointed.size()>0){
            for (Map<String, Object> task : subTaskAppointed){
                System.out.println(task);
                //{PRE_USER_ID=30003190, OPERATE_TYPE=1, IS_WORKFLOW_TASK=1, QCSJ_C000000001200000=56, TRANSACTOR_ID=30003190, INSTANCE_ID=15231440, ORG_NAME=延平区, TRANSACTOR_NAME=延平区, RN=1, TASK_ID=15231458, START_TIME=oracle.sql.TIMESTAMP@2dcc4892, ISTIMEOUT=0, PRE_TASK_ID=15231461, ORG_ID=1255, PRE_TASK_NAME=start, PRE_USER_NAME=延平区, PRE_EVENT_STATUS=00, EVENT_ID=9722, TASK_TYPE=1, TASK_NAME=task1, NODE_ID=463219, END_TIME=oracle.sql.TIMESTAMP@3eb080bb}

                String task_name = task.get("TASK_NAME").toString();
                if(!task_name.equals("task13")){

                    String event_id = task.get("EVENT_ID").toString();
                    String instance_id = task.get("INSTANCE_ID").toString();
                    String task_id = task.get("TASK_ID").toString();
                    String org_name = task.get("ORG_NAME").toString();
                    PublicAppeal publicAppeal = this.publicAppealService.searchByEventId(Long.valueOf(event_id));

                    if(task.get("REMARKS")!=null){
//                        publicAppeal.setHandleRs(task.get("REMARKS").toString());//处理结果
                    }

                    TIMESTAMP end_time = (TIMESTAMP)task.get("END_TIME");
                    String end_time_str = null;
                    try {
                        end_time_str = df.format(end_time.dateValue());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    publicAppeal.setRep_time(end_time_str);
                    publicAppeal.setHandleSit(ConstantValue.PUBLIC_APPEAL_HANDLESIT_02);

                    Map<String, Object> stringObjectMap = eventDisposalWorkflowService.curNodeData(Long.valueOf(instance_id));
                    System.out.println(stringObjectMap);
                    //{WF_ORGNAME=延平区食药监, NODE_ID=3934563, WF_START_TIME=2017-10-21 00:52:33, NODE_NAME=task11, NODE_CODE=D3, WF_USERID=30067520, WF_USERNAME=延平区食药监, WF_DBID_=442, WF_ACTIVITY_NAME_=区处置单位处理, INSTANCEID=412, WF_ORGID=433781}
                    //WF_ACTIVITY_NAME_
                    if(stringObjectMap.get("WF_ACTIVITY_NAME_")!=null){
                        org_name = stringObjectMap.get("WF_ACTIVITY_NAME_").toString();
                    }
                    publicAppeal.setRep_org_name(org_name);//处理组织
                    boolean update = publicAppealService.update(publicAppeal);

                    DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
                    dataExchangeStatus.setExchangeFlag("1");
                    dataExchangeStatus.setOppoSideBizCode(publicAppeal.getOutId().toString());
                    dataExchangeStatus.setOppoSideBizType("3");
                    dataExchangeStatus.setOwnSideBizCode(task_id);
                    dataExchangeStatus.setOwnSideBizType("3");
                    dataExchangeStatus.setStatus("1");
                    dataExchangeStatus.setXmlData("");
                    dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
                    dataExchangeStatus.setSrcPlatform("000");
                    this.dataExchangeStatusService.save(dataExchangeStatus);
                }
            }
        }
    }

}
