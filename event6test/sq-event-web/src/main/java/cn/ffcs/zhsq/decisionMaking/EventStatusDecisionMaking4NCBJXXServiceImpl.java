package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description:南昌指挥调度事件状态决策类
 * @Author: zhangtc
 * @Date: 2019/7/12 15:41
 */
@Service(value = "eventStatusDecisionMaking4NCBJXXService")
public class EventStatusDecisionMaking4NCBJXXServiceImpl extends EventStatusDecisionMakingServiceImpl {
	
	private static final String START_NODE_CODE = "start";		//事件流程启动
	private static final String COLLECT_NODE_CODE = "task1";	//事件采集处理环节

    private static final String TOWNDISPOSAl_STREET_LEADER_CODE = "task2";		//乡镇街巷长处理
    private static final String TOWNDISPOSAl_DEPARTMENT_STREET_LEADER_CODE = "task3";		//乡镇部门街巷长处理
    
    //需要设置事件办结时限的环节
    private static final String TOWNDISPOSAl_NODE_CODE = "task4";	//乡镇（街巷长管理员）处理环节
    private static final String DISTRICT_NODE_CODE = "task5";	//县区（街巷长管理员）处理环节
    private static final String CITY_NODE_CODE = "task6";	//市级（街巷长管理员）处理环节
    
    private static final String END_NODE_CODE = "end1";			//归档环节

    @Override
    protected Map<String, Object> initStatusMap(String chiefLevel) {
        Map<String, Object> statusMap = super.initStatusMap(chiefLevel);
        
        statusMap.put(START_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);		//事件启动流程
		statusMap.put(COLLECT_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);		//事件采集
        
        statusMap.put(TOWNDISPOSAl_STREET_LEADER_CODE + "-" + TOWNDISPOSAl_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(TOWNDISPOSAl_DEPARTMENT_STREET_LEADER_CODE + "-" + TOWNDISPOSAl_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(TOWNDISPOSAl_NODE_CODE + "-" + DISTRICT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(DISTRICT_NODE_CODE + "-" + CITY_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(CITY_NODE_CODE + "-" + DISTRICT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);

        statusMap.put(END_NODE_CODE, ConstantValue.EVENT_STATUS_END);
        
        return statusMap;
    }
   
}
