package cn.ffcs.zhsq.event.service.stub.timer;/**
 * Created by Administrator on 2017/8/2.
 */

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
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
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.ConstantValue;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动结案
 */
public class AutoCloseEventQuartzJob implements Job {

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

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("start 事件自动结案===================================================");
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
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        String autoClose = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "AUTO_CLOSE", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        System.out.println(autoClose);
        String[] bizPlatforms = autoClose.split(",");
        for(String bizPlatform : bizPlatforms){
            System.out.println(bizPlatform);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("bizPlatform", bizPlatform);
            params.put("status", "03");
            EUDGPagination eventDisposalPagination = eventDisposalService.findEventDisposalPagination(1, 1, params);
            if(eventDisposalPagination.getTotal() > 0){
                List<EventDisposal> rows = (List<EventDisposal>)eventDisposalPagination.getRows();
                for(EventDisposal eventDisposal : rows){
                    System.out.println("eventId="+eventDisposal.getEventId());
                    Map<String, Object> eventMap = new HashMap<String, Object>();
                    eventMap.put("eventId",eventDisposal.getEventId());
                    eventMap.put("advice","归档");
                    Map<String, Object> stringObjectMap = eventDisposalDockingService.subWorkflow(eventMap);
                    logger.info(stringObjectMap.toString());

                }
            }
        }
        logger.info("end 事件自动结案===================================================");
    }

}
