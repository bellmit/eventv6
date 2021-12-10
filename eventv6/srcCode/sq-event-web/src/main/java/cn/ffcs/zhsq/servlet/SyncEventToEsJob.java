package cn.ffcs.zhsq.servlet;

import cn.ffcs.zhsq.event.service.IEventDisposalEsService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description:同步事件到Es，新增和修改
 * @Author: ztc
 * @Date: 2018/9/12 17:06
 */
@Component("syncEventToEsJob")
public class SyncEventToEsJob implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger("dailyRollingFile");

    @Autowired
    private IEventDisposalEsService eventDisposalEsService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long DEFAULT_PERIOD = 3600000L;//默认设置时间间隔
    private static boolean isRunning = true;//用于判断当次轮询是否完成
    private static boolean isStart = false;//用于控制onApplicationEvent内部方法只执行一次

    @Override
    public void onApplicationEvent(ContextRefreshedEvent eventDisposalEs) {
        logger.info("===================================================start===================================================");

        //获取事件辖区所有数据，默认一小时执行一次：事件更新时间段为：  数据库系统时间-1 到 当前时间   sysDate-1 ~ sysDate
        //1.获取事件辖区所有数据，状态00，01，02，03，04：jdbc查询
        //暂时不用此定时器，将isStart改为false，用的时候再改为true
        isStart = false;
        try {

            if (isStart){
                //事件有效状态
                final StringBuffer eventStatus = new StringBuffer();
                eventStatus.append(ConstantValue.EVENT_STATUS_RECEIVED).append(",");
                eventStatus.append(ConstantValue.EVENT_STATUS_REPORT).append(",");
                eventStatus.append(ConstantValue.EVENT_STATUS_DISTRIBUTE).append(",");
                eventStatus.append(ConstantValue.EVENT_STATUS_ARCHIVE).append(",");
                eventStatus.append(ConstantValue.EVENT_STATUS_END);

                //事件删除状态
                final StringBuffer eventDelStatus = new StringBuffer();
                eventDelStatus.append(ConstantValue.STATUS_DEL_AND_RETURN_BACK);

                Timer timer = new Timer();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (isRunning) {
                            isRunning = false;

                            //1.获取事件辖区所有数据，状态00，01，02，03，04
                            try {
                                getEventDisposalEsList(eventStatus.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //2.删除Es事件，状态06，主表已经删除的数据
                            try {
                                getEventDisposalEsList(eventDelStatus.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            isRunning = true;
                        }


                    }
                },new Date(),DEFAULT_PERIOD);
            }
            isStart = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 取数据库时间
     *推送更新时间在   startTime~endTime 的事件数据
     * @param status        事件状态
     * */
    private List<Map<String,Object>> getEventDisposalEsList(String status){


        List<Map<String,Object>> resultList = new ArrayList<>();
        StringBuffer queryListSql = new StringBuffer();
        int minRownum = 0,maxRownum = 200;

        //获取数据库系统时间
        String startTime = "";//数据库当前时间-1h
        String endTime = "";//数据库当前时间
        String queryDateSql = " SELECT TO_CHAR(SYSDATE,'yyyy-MM-dd HH24:mi:ss') CURTIME FROM dual ";

        Map<String,Object> curTimeMap = jdbcTemplate.queryForMap(queryDateSql);
        if (CommonFunctions.isNotBlank(curTimeMap,"CURTIME")) {
            endTime = curTimeMap.get("CURTIME").toString();
            //开始时间为数据库系统时间的前一个小时
            try {
                Date startTimeDate = DateUtils.convertStringToDate(endTime,DateUtils.PATTERN_24TIME);
                startTime = DateUtils.formatDate(new Date(startTimeDate.getTime() - 60*60*1000),DateUtils.PATTERN_24TIME);
                System.out.println("startTime" + startTime + ", endTime:" + endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("获取数据库系统当前时间出错，请检查！");
        }


        //查询需要推送的数据
        do {
            //清空queryListSql，防止queryListSql反复拼接
            queryListSql.setLength(0);

            queryListSql.append(" SELECT T.EVENT_ID ");
            queryListSql.append(" FROM ( ");
            queryListSql.append(" SELECT ROWNUM ROWNUM_, T1.EVENT_ID,T1.CREATE_TIME,T1.UPDATE_TIME ");
            queryListSql.append(" FROM T_EVENT T1 ");
            queryListSql.append(" WHERE ");
            queryListSql.append(" T1.UPDATE_TIME >= to_date(' ").append(startTime).append(" ','yyyy-MM-dd hh24:mi:ss') ");
            queryListSql.append(" AND T1.UPDATE_TIME <= to_date(' ").append(endTime).append(" ','yyyy-MM-dd hh24:mi:ss') ");
            queryListSql.append(" AND T1.STATUS IN( ").append(status).append(" ) ");
            queryListSql.append(" ORDER BY T1.UPDATE_TIME DESC");
            queryListSql.append(" )T");
            queryListSql.append(" WHERE ROWNUM_ > ").append(minRownum);
            queryListSql.append(" AND ROWNUM_ <= ").append(maxRownum);

            resultList = jdbcTemplate.queryForList(queryListSql.toString());

            for (Map<String,Object> eventmap:resultList) {
                Long eventId = Long.valueOf(eventmap.get("EVENT_ID").toString());
                System.out.println(eventId);

                if (ConstantValue.STATUS_DEL_AND_RETURN_BACK.equals(status)) {
                        //更新ES事件状态为删除状态
                        eventDisposalEsService.findAndUpdate(eventId);
                    } else {
                        //同步事件到es-新增
                        eventDisposalEsService.findAndSave(eventId);
                    }
            }


            minRownum = maxRownum;
            maxRownum += 200;

        }while (resultList.size() > 0);

        return resultList;
    }

}
