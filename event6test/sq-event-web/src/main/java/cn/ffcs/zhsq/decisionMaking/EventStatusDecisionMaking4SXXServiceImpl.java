package cn.ffcs.zhsq.decisionMaking;

import cn.ffcs.zhsq.utils.ConstantValue;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description:松溪县城管局事件流程状态回写接口
 * @Author: zhangtc
 * @Date: 2018/12/26 16:07
 */
@Service(value = "eventStatusDecisionMaking4SXXService")
public class EventStatusDecisionMaking4SXXServiceImpl extends EventStatusDecisionMakingServiceImpl{

    private static final String STREET_MONITOR_HANDLE = "task2";//街道监督员处理
    private static final String DISTRICT_MONITOR_HANDLE = "task3";//区县监督员处理
    private static final String SUPERVISION_CENTER_DISTRIBUTE = "task4";//监督中心核实派发
    private static final String SUPERVISION_CENTER_RECORD = "task5";//监督中心立案
    private static final String EVENT_INVALID = "task6";//事件作废
    private static final String COMMAND_CENTER_DISTRIBUTE = "task12";//指挥中心派发
    private static final String STREET_DEPARTMENT_HANDLE = "task7";//街道专业部门处理
    private static final String DISTRICT_DEPARTMENT_HANDLE = "task8";//区县专业部门处理
    private static final String MONITOR_CENTER_DISTRIBUTE = "task9";//监督中心核查派发
    private static final String DISTRICT_MONITOR_CHECK = "task11";//县区监督员核查
    private static final String STREET_MONITOR_CHECK = "task10";//街道监督员核查
    private static final String COMMAND_CENTER = "task13";//指挥中心处理

    @Override
    protected Map<String, Object> initStatusMap(String chiefLevel) {

        Map<String, Object> statusMap = super.initStatusMap(chiefLevel);

        //上报
        statusMap.put(STREET_MONITOR_HANDLE +"-"+ SUPERVISION_CENTER_DISTRIBUTE, ConstantValue.EVENT_STATUS_REPORT);//2-4
        statusMap.put(DISTRICT_MONITOR_HANDLE +"-"+ SUPERVISION_CENTER_DISTRIBUTE, ConstantValue.EVENT_STATUS_REPORT);//3-4
        statusMap.put(STREET_DEPARTMENT_HANDLE +"-"+ COMMAND_CENTER, ConstantValue.EVENT_STATUS_REPORT);//7-13
        statusMap.put(DISTRICT_DEPARTMENT_HANDLE +"-"+ COMMAND_CENTER, ConstantValue.EVENT_STATUS_REPORT);//8-13

        //分流
        statusMap.put(SUPERVISION_CENTER_DISTRIBUTE +"-"+ SUPERVISION_CENTER_RECORD, ConstantValue.EVENT_STATUS_DISTRIBUTE);//4-5
        statusMap.put(SUPERVISION_CENTER_DISTRIBUTE +"-"+ EVENT_INVALID, ConstantValue.EVENT_STATUS_DISTRIBUTE);//4-6
        statusMap.put(SUPERVISION_CENTER_RECORD +"-"+ COMMAND_CENTER_DISTRIBUTE, ConstantValue.EVENT_STATUS_DISTRIBUTE);//5-12
        statusMap.put(COMMAND_CENTER_DISTRIBUTE +"-"+ STREET_DEPARTMENT_HANDLE, ConstantValue.EVENT_STATUS_DISTRIBUTE);//12-7
        statusMap.put(COMMAND_CENTER_DISTRIBUTE +"-"+ DISTRICT_DEPARTMENT_HANDLE, ConstantValue.EVENT_STATUS_DISTRIBUTE);//12-8
        statusMap.put(MONITOR_CENTER_DISTRIBUTE +"-"+ STREET_MONITOR_CHECK, ConstantValue.EVENT_STATUS_DISTRIBUTE);//9-10
        statusMap.put(MONITOR_CENTER_DISTRIBUTE +"-"+ DISTRICT_MONITOR_CHECK, ConstantValue.EVENT_STATUS_DISTRIBUTE);//9-11
        statusMap.put(COMMAND_CENTER +"-"+ STREET_DEPARTMENT_HANDLE, ConstantValue.EVENT_STATUS_DISTRIBUTE);//13-7
        statusMap.put(COMMAND_CENTER +"-"+ DISTRICT_DEPARTMENT_HANDLE, ConstantValue.EVENT_STATUS_DISTRIBUTE);//13-8
        statusMap.put(COMMAND_CENTER +"-"+ MONITOR_CENTER_DISTRIBUTE, ConstantValue.EVENT_STATUS_DISTRIBUTE);//13-9


        return statusMap;
    }
}
