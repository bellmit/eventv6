package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 江西省吉安市吉州区(Ji ZHou Qu) 事件状态变更决策类，被替换为EventStatusDecisionMaking4JiZHouQuServiceImpl
 * @ClassName:   EventStatusDecisionMaking4JZHQServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年7月27日 上午10:36:23
 */
@Service(value = "eventStatusDecisionMaking4JZHQService")
@Deprecated
public class EventStatusDecisionMaking4JZHQServiceImpl extends EventStatusDecisionMakingServiceImpl {
	private static final String COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE = "task12";//八员队伍办理
	
	/**
	 * 初始化事件状态Map
	 */
	protected Map<String, Object> initStatusMap(String chiefLevel) {
		Map<String, Object> statusMap = super.initStatusMap(chiefLevel);
		
		//上报、越级上报
		statusMap.put(GRID_ADMIN_NODE_CODE+"-"+COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE+"-"+COMMUNITY_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE+"-"+STREET_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		//分流、下派
		statusMap.put(COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE+"-"+GRID_ADMIN_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(COMMUNITY_NODE_CODE+"-"+COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(STREET_NODE_CODE+"-"+COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		return statusMap;
	}
}
