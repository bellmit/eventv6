package cn.ffcs.zhsq.event.service.stub.tianchuang;/**
 * Created by Administrator on 2017/6/27.
 */

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IUserService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.event.service.stub.gansu.SendDataServiceStub;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import oracle.sql.TIMESTAMP;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongshm
 * @create 2017-9-5 16:24:16
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
        System.out.println("========="+DateUtils.getNow()+ " start 天创结案==========");
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

        //获取上报事件
//        reportEvent(GANSU_user_id, GANSU_org_id);
        //获取过程、结案事件
        reportTask();
        //获取驳回事件
//        reject();
        //获取归档事件
//        archive();
    }

    private void reportTask(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("srcPlatform", BIZ_PLATFORM);
        params.put("descPlatform", "000");
        params.put("operateType", "1");
        params.put("taskName", "task9");
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
                String end_time_str = null;
                try {
                    end_time_str = df.format(end_time.dateValue());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
//
//                if(!taskName.equals("task8")){//过程
//                    StringBuffer sb = new StringBuffer();
//                    sb.append("<request>");
//                    sb.append("<params>");
//                    sb.append("<RecID>"+oppo_side_biz_code+"</RecID>");
//                    sb.append("<TransOpinion>"+remarks+"</TransOpinion>");
//                    sb.append("<TransTime>"+end_time_str+"</TransTime>");
//                    sb.append("<transStatus>6</transStatus>");
//                    sb.append("</params>");
//                    sb.append("</request>");
//                    String result = feedbackResult(sb.toString());
//                    String errorCode = result.substring(result.indexOf("<errorCode>") + 11, result.indexOf("</errorCode>"));
//                    if(errorCode.equals("0")){
//                        DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
//                        dataExchangeStatus.setExchangeFlag("1");
//                        dataExchangeStatus.setOppoSideBizCode(oppo_side_biz_code);
//                        dataExchangeStatus.setOppoSideBizType("3");
//                        dataExchangeStatus.setOwnSideBizCode(event_id);
//                        dataExchangeStatus.setOwnSideBizType("3");
//                        dataExchangeStatus.setStatus("1");
//                        dataExchangeStatus.setXmlData(result);
//                        dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
//                        dataExchangeStatus.setSrcPlatform("000");
//                        this.dataExchangeStatusService.save(dataExchangeStatus);
//                    }
//                }else{//结案
//                    String reportCloseUserName = "";
//                    UserInfo userExtraInfoByUserId = this.userService.getUserExtraInfoByUserId(Long.valueOf(pre_user_id), null);
//                    if(userExtraInfoByUserId != null){
//                        reportCloseUserName = userExtraInfoByUserId.getPartyName();
//                    }
//
//                    StringBuffer sb = new StringBuffer();
//                    sb.append("<request>");
//                    sb.append("<params>");
//                    sb.append("<RecID>"+oppo_side_biz_code+"</RecID>");
//                    sb.append("<CloseTime>"+end_time_str+"</CloseTime>");
//                    sb.append("<ReportCloseDesc>"+remarks+"</ReportCloseDesc>");
//                    sb.append("<ReportCloseUserName>"+reportCloseUserName+"</ReportCloseUserName>");
//                    sb.append("</params>");
//                    sb.append("</request>");
////                    String result = closeCaseResult(sb.toString());
//                    String result = "";
//                    String errorCode = result.substring(result.indexOf("<errorCode>") + 11, result.indexOf("</errorCode>"));
//                    if(errorCode.equals("0")){
//                        DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
//                        dataExchangeStatus.setExchangeFlag("1");
//                        dataExchangeStatus.setOppoSideBizCode(oppo_side_biz_code);
//                        dataExchangeStatus.setOppoSideBizType("3");
//                        dataExchangeStatus.setOwnSideBizCode(event_id);
//                        dataExchangeStatus.setOwnSideBizType("3");
//                        dataExchangeStatus.setStatus("1");
//                        dataExchangeStatus.setXmlData(result);
//                        dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
//                        dataExchangeStatus.setSrcPlatform("000");
//                        this.dataExchangeStatusService.save(dataExchangeStatus);
//                    }
//                }
            }
        }
    }

}
