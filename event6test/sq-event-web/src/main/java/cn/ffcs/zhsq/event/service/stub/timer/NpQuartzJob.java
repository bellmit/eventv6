package cn.ffcs.zhsq.event.service.stub.timer;/**
 * Created by Administrator on 2017/12/1.
 */

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.shequ.zzgl.service.grid.IGridAdminService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dataExchange.service.IBasDataExchangeService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 福建省数字政通环保事件推送定时器
 *
 * @author zhongshm
 * @create 2017-12-01 14:27
 **/
public class NpQuartzJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private IMixedGridInfoService mixedGridInfoService;
    private IEventDisposalService eventDisposalService;
    private IFunConfigurationService funConfigurationService;
    private IDataExchangeStatusService dataExchangeStatusService;
    private IBasDataExchangeService basDataExchangeService;
    private IGridAdminService gridAdminService;
    private final String BIZ_PLATFORM = "034";

    private String gridadminUrl ="";
    private String url ="";
    private String fjsOrgCode ="";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        SchedulerContext schCtx = null;
        try {
            schCtx = context.getScheduler().getContext();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        ApplicationContext appCtx = (ApplicationContext) schCtx.get("applicationContext");
        this.eventDisposalService = (IEventDisposalService) appCtx.getBean("eventDisposalServiceImpl");
        this.funConfigurationService = (IFunConfigurationService) appCtx.getBean("funConfigurationService");
        this.basDataExchangeService = (IBasDataExchangeService) appCtx.getBean("basDataExchangeServiceImpl");
        this.dataExchangeStatusService = (IDataExchangeStatusService) appCtx.getBean("dataExchangeStatusServiceImpl");
        this.gridAdminService = (IGridAdminService) appCtx.getBean("gridAdminService");
        this.mixedGridInfoService = (IMixedGridInfoService) appCtx.getBean("mixedGridInfoService");
        logger.info("=========================环保省环保厅数据推送==========================");
        url = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "fjs_url", IFunConfigurationService.CFG_TYPE_FACT_VAL);
        fjsOrgCode = funConfigurationService.changeCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, "fjs_org_code", IFunConfigurationService.CFG_TYPE_FACT_VAL);


        Map<String, Object> timerParams = new HashMap<String, Object>();
        timerParams.put("exchangeType","004");//省厅
        BasDataExchange basDataExchange = basDataExchangeService.findBasDataExchange(timerParams);
        if(basDataExchange == null) return;

        java.util.Calendar cal = java.util.Calendar.getInstance();
        Date timeStamp = basDataExchange.getTimeStamp();
        String startTime = DateUtils.formatDate(timeStamp, DateUtils.PATTERN_24TIME);
        cal.setTime(timeStamp);
        long startCal = cal.getTimeInMillis();
        Date now = new Date();
        cal.setTime(now);
        long endCal = cal.getTimeInMillis();
        long between_days=(endCal-startCal)/(1000*3600);
        String endTime = DateUtils.formatDate(now, DateUtils.PATTERN_24TIME);
        if(between_days > 24){
            cal.setTime(timeStamp);
            cal.set(java.util.Calendar.DATE, cal.get(java.util.Calendar.DATE) + 1);
            endTime = DateUtils.formatDate(cal.getTime(), DateUtils.PATTERN_24TIME);
        }

        System.out.println("startTime:"+startTime);
        System.out.println("endTime:"+endTime);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("createTimeStart", startTime);
        params.put("createTimeEnd", endTime);
        params.put("type", "31");
        params.put("infoOrgCode", "3507");
        EUDGPagination eventDisposalPagination = eventDisposalService.findEventDisposalPagination(1, 1000, params);
        List<EventDisposal> rows = (List<EventDisposal>) eventDisposalPagination.getRows();
        for(EventDisposal eventDisposal : rows){
            System.out.println(eventDisposal.getEventId());
            syncData(eventDisposal);
        }
        BasDataExchange nextBasDataExchange = new BasDataExchange();
        nextBasDataExchange.setExchangeId(basDataExchange.getExchangeId());
        nextBasDataExchange.setTimeStamp(cal.getTime());
        this.basDataExchangeService.updateBasDataExchange(nextBasDataExchange);
    }

    private String getRegionCode(String regionCode){
        int len = 12 - regionCode.length();
        if(regionCode.length() < 12){
            for(int i=0;i<len;i++){
                regionCode = regionCode + "0";
            }
        }
        return regionCode;
    }

    private void syncData(EventDisposal eventDisposal){
        MixedGridInfo mixedGridInfo = this.mixedGridInfoService.findMixedGridInfoById(eventDisposal.getGridId(), false);
        String regionCode = getRegionCode(mixedGridInfo.getInfoOrgCode());

        logger.debug("regionCode="+regionCode);
        System.out.println(regionCode);

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskNum", eventDisposal.getCode());
        jsonObject.put("sourceID", eventDisposal.getEventId());

        jsonObject.put("regionCode",regionCode);
        jsonObject.put("mainType","1");
//        jsonObject.put("coordinateX","20161104_60010101_00003");
//        jsonObject.put("coordinateX","20161104_60010101_00003");
        jsonObject.put("eventDesc", eventDisposal.getContent());
        jsonObject.put("createTime", eventDisposal.getCreateTimeStr());
        jsonObject.put("address", eventDisposal.getOccurred());
        jsonObject.put("patrolID", eventDisposal.getCreatorId());
        jsonObject.put("patrolName", eventDisposal.getContactUser());
        jsonObject.put("archive", eventDisposal.getResult());
        jsonObject.put("archiveTime", eventDisposal.getFinTimeStr());//结案时间
        jsonArray.add(jsonObject);

        JSONObject params = new JSONObject();
        if(mixedGridInfo.getInfoOrgCode().indexOf("3502") > -1){//厦门
            params.put("cityCode","02");//城市编码 厦门02
            params.put("token","FJB01");//第三方提供的token
        }else if(mixedGridInfo.getInfoOrgCode().indexOf("3506") > -1){//漳州
            params.put("cityCode","03");//城市编码 漳州03
            params.put("token","FJC01");//第三方提供的token
        }else if(mixedGridInfo.getInfoOrgCode().indexOf("3507") > -1){//南平
            params.put("cityCode","07");//城市编码 南平07
            params.put("token","FJG01");//第三方提供的token
        }
        params.put("uploadKey",System.currentTimeMillis());//随机值
        params.put("list",jsonArray.toString());

        System.out.println(params.toString());

        if(StringUtils.isNotBlank(url)){
            String s = HttpUtil.jsonPost(url, params.toString());
            System.out.println(s);

            DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
            dataExchangeStatus.setExchangeFlag("1");
            dataExchangeStatus.setOppoSideBizType("2");
            dataExchangeStatus.setOppoSideBizCode("");
            dataExchangeStatus.setOwnSideBizType("2");
            dataExchangeStatus.setOwnSideBizCode(eventDisposal.getEventId().toString());
            dataExchangeStatus.setStatus("1");
            dataExchangeStatus.setXmlData(params.toString() + "|" + s);
            dataExchangeStatus.setDestPlatform(this.BIZ_PLATFORM);
            dataExchangeStatus.setSrcPlatform("000");
            this.dataExchangeStatusService.save(dataExchangeStatus);
        }

    }


}
