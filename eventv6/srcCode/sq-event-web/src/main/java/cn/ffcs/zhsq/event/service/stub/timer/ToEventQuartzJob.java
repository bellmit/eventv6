package cn.ffcs.zhsq.event.service.stub.timer;/**
 * Created by Administrator on 2017/8/2.
 */

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.dispute.DisputeMediation;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.dispute.IDisputeMediationService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserExBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
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
 *
 */
public class ToEventQuartzJob implements Job {

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

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("start 自动===================================================");
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
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("disputeStatus", "4");
        params.put("infoOrgCode", "36");
        cn.ffcs.common.EUDGPagination disputePagination = disputeMediationService.findDisputePagination(0, 1, params);
        List<DisputeMediation> list = (List<DisputeMediation>)disputePagination.getRows();
        if(list.size()>0){
            for(DisputeMediation disputeMediation : list){
                Long creatorId = disputeMediation.getCreatorId();
                UserBO userInfoByUserId = userManageService.getUserInfoByUserId(creatorId);
                List<UserExBO> userExBoForUserId = userManageService.findUserExBoForUserId(creatorId);
                UserInfo userInfo  = new UserInfo();
                userInfo.setUserId(creatorId);
                logger.info(String.valueOf(userExBoForUserId.size()));
                userInfo.setOrgCode(userExBoForUserId.get(0).getOrgCode());
                userInfo.setOrgId(Long.valueOf(userExBoForUserId.get(0).getSocialOrgId()));

                //report event
                EventDisposal event = new EventDisposal();
                if(!"1".equals(disputeMediation.getDisputeStatus())){
                    disputeMediation.setMediationCode(disputeMediationService.getMediationCode());
                }
                String disputeEventType= disputeMediationService.getDisputeEventType(userInfo, disputeMediation.getDisputeType2());//获取该组织的矛盾纠纷事件类型
                if(!StringUtils.isBlank(disputeEventType)){
                    event.setType(disputeEventType);
                }

                event.setContent(disputeMediation.getDisputeCondition());
                event.setGridId(Long.parseLong(disputeMediation.getGridId()));
                event.setEventName(disputeMediation.getDisputeEventName());
                event.setHappenTimeStr(disputeMediation.getHappenTimeStr());
                event.setOccurred(disputeMediation.getHappenAddr());
                Map<String, Object> result = eventDisposalDockingService.saveEventDisposalAndReport(event, userInfo, "矛盾纠纷上报事件！");
                logger.info("eventId="+result.get("eventId").toString());
                disputeMediation.setEventId((Long)result.get("eventId"));
                disputeMediation.setDisputeStatus("2");
                int recordId = disputeMediationService.updateByPrimaryKeySelective(disputeMediation);
                //保存关联关系
                if(disputeMediation.getEventId() != null && disputeMediation.getMediationId() != null){
                    disputeMediationService.saveEventReport(disputeMediation, userInfo);
                }
            }
        }else{
            System.out.println("==================================================");
            System.out.println("==================================================");
            System.out.println("========================无数据==========================");
            System.out.println("==================================================");
            System.out.println("==================================================");
        }

        logger.info("end 自动===================================================");
    }

}
