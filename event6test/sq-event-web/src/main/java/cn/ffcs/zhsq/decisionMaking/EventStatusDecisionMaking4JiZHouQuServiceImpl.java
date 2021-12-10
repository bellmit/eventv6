package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 江西省吉安市吉州区(Ji ZHou Qu) 事件状态变更决策类
 * @ClassName:   EventStatusDecisionMaking4JiZHouQuServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年8月25日 上午8:56:43
 */
@Service(value = "eventStatusDecisionMaking4JiZHouQuService")
public class EventStatusDecisionMaking4JiZHouQuServiceImpl extends EventStatusDecisionMaking4JXPlatformServiceImpl {
	private static final String COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE = "task22";//八员队伍办理
	
	protected Map<String, Object> initStatusMap(String chiefLevel) {
		Map<String, Object> statusMap = super.initStatusMap(chiefLevel);
		
		statusMap.put(COMMUNITY_UNIT_ACCEPT_NODE_CODE + "-" + GRID_ADMIN_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
        statusMap.put(COMMUNITY_UNIT_ACCEPT_NODE_CODE + "-" + TOWNDISPOSAl_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(COMMUNITY_UNIT_ACCEPT_NODE_CODE + "-" + TOWNDISPOSAl_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(COMMUNITY_UNIT_ACCEPT_NODE_CODE + "-" + DISTRICT_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        statusMap.put(COMMUNITY_UNIT_ACCEPT_NODE_CODE + "-" + DISTRICT_DEPT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
        
		//上报、越级上报
		statusMap.put(GRID_ADMIN_HANDLE_NODE_CODE+"-"+COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE+"-"+COMMUNITY_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE+"-"+TOWNDISPOSAl_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE+"-"+DISTRICT_UNIT_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		//分流、下派
		statusMap.put(COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE+"-"+GRID_ADMIN_ACCEPT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(COMMUNITY_UNIT_HANDLE_NODE_CODE+"-"+COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE+"-"+COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		return statusMap;
	}
}
