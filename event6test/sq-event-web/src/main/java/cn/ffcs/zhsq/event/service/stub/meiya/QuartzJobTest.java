package cn.ffcs.zhsq.event.service.stub.meiya;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.FunConfigureSetting;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dataExchange.service.IBasDataExchangeService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.BasDataExchange;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;

/**
 *美亚事件拉取定时器
 */
public class QuartzJobTest implements Job {

    private final Logger logger = LoggerFactory.getLogger("dailyRollingFile");

    private IBasDataExchangeService basDataExchangeService;
    private IFunConfigurationService funConfigurationService;
    private IMixedGridInfoService mixedGridInfoService;
    private IEventDisposalDockingService eventDisposalDockingService;
    private IDataExchangeStatusService dataExchangeStatusService;

    //业务编码
    private final String BIZ_PLATFORM = "020";
    private String uri = "http://221.228.70.21:90/mobile/zhzf/sjsb/saveExtEReport.jsp";
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("===================================================");
        logger.info("美亚事件拉取");
        logger.info("===================================================");
        try {
            SchedulerContext schCtx = context.getScheduler().getContext();
            ApplicationContext appCtx = (ApplicationContext) schCtx.get("applicationContext");
            this.basDataExchangeService = (IBasDataExchangeService) appCtx.getBean("basDataExchangeServiceImpl");
            this.funConfigurationService = (IFunConfigurationService) appCtx.getBean("funConfigurationService");
            this.mixedGridInfoService = (IMixedGridInfoService) appCtx.getBean("mixedGridInfoService");
            this.eventDisposalDockingService = (IEventDisposalDockingService) appCtx.getBean("eventDisposalDockingServiceImpl");
            this.dataExchangeStatusService = (IDataExchangeStatusService) appCtx.getBean("dataExchangeStatusServiceImpl");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("exchangeType","001");//
        BasDataExchange basDataExchange = basDataExchangeService.findBasDataExchange(params);
        if (basDataExchange != null){
            Calendar cal = Calendar.getInstance();

            Date timeStamp = basDataExchange.getTimeStamp();
            System.out.println(timeStamp);

            String startTime = DateUtils.formatDate(timeStamp, DateUtils.PATTERN_24TIME);
            cal.setTime(timeStamp);
            long startCal = cal.getTimeInMillis();

            Date now = new Date();
            cal.setTime(now);
            long endCal = cal.getTimeInMillis();

            long between_days=(endCal-startCal)/(1000*3600);
            logger.info("between_days:"+between_days);

            String endTime = DateUtils.formatDate(now, DateUtils.PATTERN_24TIME);
            if(between_days > 24){
                cal.setTime(timeStamp);
                cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
                endTime = DateUtils.formatDate(cal.getTime(), DateUtils.PATTERN_24TIME);
            }

            startTime = "2014-08-14 17:00:00";
            logger.info("startTime:"+startTime);
            logger.info("endTime:"+endTime);
            int pageNo = 0;
            int pageSize = 200;
            String jsonStr = doGetData(pageNo, pageSize, startTime, endTime);
//            logger.info(jsonStr);
//            BasDataExchange nextBasDataExchange = new BasDataExchange();
//            nextBasDataExchange.setExchangeId(basDataExchange.getExchangeId());
//            nextBasDataExchange.setTimeStamp(cal.getTime());
//            this.basDataExchangeService.updateBasDataExchange(nextBasDataExchange);
            save(jsonStr);
        }else{
            logger.error("未查到上一有效时间。");
        }

    }


    private String doGetData(int pageNo, int pageSize, String startTime, String endTime) {
        String url = "http://10.23.86.85:7009/GGAQ/03/XM.GOV.YZ.GGAQ.GGAQ.HBJGETDATAS";

        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "tableid", "XM_GGAQ_EVENTS" );
        map.put( "token", "b59cb246454347858a7215ff4111729c" );
        map.put( "limit", pageSize);
        map.put( "offset", pageNo);
        map.put( "startTime", startTime);
        map.put( "endTime", endTime);
        JSONObject jsonObject = JSONObject.fromObject(map);
//		String param = "query={\"startTime\":\"2016-06-01 23:59:59\",\"limit\":10,\"token\":\"b59cb246454347858a7215ff4111729c\",\"tableid\":\"XM_GGAQ_EVENTS\",\"endTime\":\"2017-01-01 23:59:59\",\"offset\":0}";
        String param = "query="+jsonObject.toString();
        logger.info(param);
        BufferedReader in = null;
        PrintWriter out = null;
        String result = "";
        URL realUrl = null;
        URLConnection connection = null;
        try {
            realUrl = new URL(url);
            connection = realUrl.openConnection();
            connection.setRequestProperty("Content-type" , "application/x-www-form-urlencoded");
            connection.setRequestProperty("COLLAGEN-REQUESTER_ID" , "GGAQ::HBJ");
            connection.setRequestProperty("COLLAGEN-AUTHORIZE_ID", "764e3dd0158c42888d0e4c345f5e242a");
            connection.setRequestProperty("COLLAGEN-PROXY_FLOW_ID", "GGAQ::01::FLOW_C3_CALL_RESTFUL_PROXY");
            connection.setRequestProperty("COLLAGEN-SESSION_ID", "no-session");
            connection.setRequestProperty("COLLAGEN-TIMEOUT", "40S");
            connection.setRequestProperty("COLLAGEN-DEBUG", "ON");
            connection.setRequestProperty("COLLAGEN-OUT_PARAMETERS", "_ALL_BUT_EXCEPTION_");
            connection.setReadTimeout(100000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(connection.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            logger.info("result->"+result);
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private void save(String jsonStr){
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        JSONArray rows = jsonObject.getJSONArray("rows");

        System.out.println(jsonObject.get("total"));
//        for (Object tempJsonObj : rows){
//            EventDisposal event = new EventDisposal();
//
//            JSONObject jsonRows = (JSONObject)tempJsonObj;
//            String ownCode = "3502";
//            if(jsonRows.get("evdt")!=null){//事件所属地域
//                String gridCode = jsonRows.get("evdt").toString();
//                if(gridCode.length() == 15){
//                    gridCode = gridCode.substring(0,12);
//                }
//                logger.info("evdt:"+gridCode);
//                String meiya_event_grid_code = funConfigurationService.changeCodeToValue("MEIYA_EVENT_GRID_CODE", gridCode, IFunConfigurationService.CFG_TYPE_FACT_VAL);
//
//                if(StringUtils.isNotBlank(meiya_event_grid_code)){
//                    ownCode = meiya_event_grid_code;
//                }else{
//                    gridCode = gridCode.substring(0,9)+"000";
//                    meiya_event_grid_code = funConfigurationService.changeCodeToValue("MEIYA_EVENT_GRID_CODE", gridCode, IFunConfigurationService.CFG_TYPE_FACT_VAL);
//                    if(StringUtils.isNotBlank(meiya_event_grid_code))
//                        ownCode = meiya_event_grid_code;
//                }
//                logger.info("ownCode:"+ownCode);
//            }
//            List<MixedGridInfo> listGrids = mixedGridInfoService.getMixedGridMappingListByOrgCode(ownCode);
//            event.setGridCode(ownCode);
//            event.setGridId(listGrids.get(0).getGridId());
//
//            if(jsonRows.get("itil")!=null){//事件标题
//                String eventName = jsonRows.get("itil").toString();
//                event.setEventName(eventName);
//            }
//            if(jsonRows.get("acte")!=null){//发生时间
//                String happenTimeStr = jsonRows.get("acte").toString();
//                happenTimeStr = happenTimeStr.replace("T"," ").replace("Z","");
//                event.setHappenTimeStr(happenTimeStr);
//            }
//            event.setType("3101");
//            if(jsonRows.get("evad")!=null){//事发地点
//                String occurred = jsonRows.get("evad").toString();
//                event.setOccurred(occurred);
//            }
//            if(jsonRows.get("ihdt")!=null){//事发描述
//                String content = jsonRows.get("ihdt").toString();
//                event.setContent(content);
//            }
//            if(jsonRows.get("styc")!=null){//事件类型
//                String styc = jsonRows.get("styc").toString();
//                logger.info("styc:"+styc);
//                String eventType = funConfigurationService.changeCodeToValue("MEIYA_EVENT_TYPE", styc, IFunConfigurationService.CFG_TYPE_FACT_VAL);
//                if(StringUtils.isNotBlank(eventType))
//                    event.setType(eventType);
//                else
//                    event.setType("9999");
//                logger.info("eventType:"+eventType);
//            }
//            if(jsonRows.get("dsco")!=null){//事件来源
//                String dsco = jsonRows.get("dsco").toString();
//                logger.info("dsco:"+dsco);
//                String source = funConfigurationService.changeCodeToValue("MEIYA_EVENT_SOURCE", dsco, IFunConfigurationService.CFG_TYPE_FACT_VAL);
//                if(StringUtils.isNotBlank(source))
//                    event.setSource(source);
//                logger.info("source:"+source);
//            }
////            UserInfo userinfo = getUserInfo();
////            Map<String, Object> stringObjectMap = eventDisposalDockingService.saveEventDisposalAndEvaluate(event, userinfo, "");
////            System.out.println(stringObjectMap);
////
////            String outEventId = "";
////            if(jsonRows.get("id")!=null) {//事件所属地域
////                outEventId = jsonRows.get("id").toString();
////            }
////            DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
////            dataExchangeStatus.setExchangeFlag("1");
////            dataExchangeStatus.setOppoSideBizCode(outEventId);
////            dataExchangeStatus.setOwnSideBizCode(stringObjectMap.get("eventId").toString());
////            dataExchangeStatus.setStatus("1");
////            dataExchangeStatus.setXmlData("");
////            dataExchangeStatus.setSrcPlatform("017");
////            dataExchangeStatusService.save(dataExchangeStatus);
//        }
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
