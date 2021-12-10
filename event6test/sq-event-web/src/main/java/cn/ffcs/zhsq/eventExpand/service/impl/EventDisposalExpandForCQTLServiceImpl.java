package cn.ffcs.zhsq.eventExpand.service.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.mybatis.persistence.eventExtend.EventExtendMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:重庆铜梁区事件扩展接口
 * @Author: zhangtc
 * @Date: 2021/4/17 15:47
 */
@Service(value = "eventDisposalExpandForCQTLServiceImpl")
public class EventDisposalExpandForCQTLServiceImpl extends EventDisposalExpandBaseServiceImpl {

    @Autowired
    private EventExtendMapper eventExtendMapper;

    @Override
    public void expandFormatMapDataOut(Map<String, Object> eventMap, UserInfo userInfo, Map<String, Object> params) {
        //判断是否获取办结亮灯情况handleDate
        if(CommonFunctions.isNotBlank(params, "getHandleDateLight")) {
            if(Boolean.valueOf(params.get("getHandleDateLight").toString())) {
                //获取办结期限
                Date handleDate = new Date();
                String status = null;
                Long eventId = null;
                //超期天数
                Float daysInterval = 0f;

                if(CommonFunctions.isNotBlank(eventMap, "handleDate")){
                    handleDate = (Date) (eventMap.get("handleDate"));
                }
                if(CommonFunctions.isNotBlank(eventMap,"status")){
                    status = String.valueOf(eventMap.get("status"));
                }
                if(CommonFunctions.isNotBlank(eventMap,"eventId")){
                    eventId = Long.valueOf(String.valueOf(eventMap.get("eventId")));
                }
                if(CommonFunctions.isNotBlank(eventMap,"timeInterval")){
                    daysInterval = Float.valueOf(String.valueOf(eventMap.get("timeInterval")));
                }

                //判断事件是否发生过结案
                Map<String,Object> taskParams = new HashMap<>();
                taskParams.put("taskName","task8");
                taskParams.put("eventId",eventId);
                List<Map<String,Object>> endTaskList = eventExtendMapper.findRejectTaskByEventId(taskParams);

                //事件结案后发生驳回：一直蓝灯
                //事件结案后未发生驳回：结案时是什么灯 一直到归档一直显示该状态灯
                if(null != endTaskList && endTaskList.size() > 0) {
                    taskParams.put("taskName","task9");
                    taskParams.put("operateType","2");
                    List<Map<String,Object>> rejectTaskList = eventExtendMapper.findRejectTaskByEventId(taskParams);
                    //1.事件结案后发生过驳回操作：一直蓝灯
                    if(rejectTaskList != null && rejectTaskList.size() > 0){
                        eventMap.put("handleDateLight", "blue");
                    }else{
                        //2.事件结案后未发生驳回：结案时是什么灯 一直到归档一直显示该状态灯
                        if(handleDate != null){
                            //Long daysInterval = this.differDays(handleDate,new Date());
                            //7天内为正常绿灯
                            if(daysInterval<7){
                                eventMap.put("handleDateLight", "green");
                            }else if(7 <= daysInterval && daysInterval <= 14) {
                                eventMap.put("handleDateLight", "yellow");
                            }else if(daysInterval > 14){
                                eventMap.put("handleDateLight", "red");
                            }
                        }
                    }
                }else {
                   // Long daysInterval = this.differDays(handleDate,new Date());
                    //5天内为正常 绿灯
                    if(daysInterval<5){
                        eventMap.put("handleDateLight", "green");
                    }else if(5 <= daysInterval && daysInterval < 7){
                        //5-7天内为 绿黄灯
                        eventMap.put("handleDateLight", "greenAndYellow");
                    }else if(7 <= daysInterval && daysInterval < 12){
                        //7天-12天为 黄灯
                        eventMap.put("handleDateLight", "yellow");
                    }else if(12 <= daysInterval && daysInterval < 14){
                        ////12天-14天为 黄红灯
                        eventMap.put("handleDateLight", "yellowAndRed");
                    }else if(daysInterval >= 14){
                        //超过14天为 红灯
                        eventMap.put("handleDateLight", "red");
                    }
                }
            }
        }
    }
}
