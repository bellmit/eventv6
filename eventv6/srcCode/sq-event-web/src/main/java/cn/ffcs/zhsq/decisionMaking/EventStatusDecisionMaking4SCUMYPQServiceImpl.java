package cn.ffcs.zhsq.decisionMaking;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 智慧城管(Smart City Urban Manage)延平区(Yan Ping Qu) 事件状态变更决策类
 * @ClassName:   EventStatusDecisionMaking4SCUMYPQServiceImpl   
 *  * 必填参数
 * 		decisionService 指定实现类 eventStatusDecisionMaking4SCUMYPQService
 * 		eventId			事件id
 * 		curNodeCode		当前节点编码
 *		nextNodeCode	下一节点编码
 * 		userId			办理人员id
 * 		orgId			办理人员所属组织
 * 非必填参数
 * 		handleDate		环节处理时限，办结时为结案时间
 * 		isReject		true时，表示当前操作为驳回操作
 * 		isRecall		true时，表示当前操作为撤回操作
 * 		preNodeCode		上一节点编码，当isReject/isRecall为true时，传入
 * 
 * @author:      张联松(zhangls)
 * @date:        2020年6月8日 下午2:35:15
 */
@Service(value = "eventStatusDecisionMaking4SCUMYPQService")
public class EventStatusDecisionMaking4SCUMYPQServiceImpl extends EventStatusDecisionMakingServiceImpl {
	//环节编码
	private static final String START_NODE_CODE = "start";					//流程开始
	private static final String COLLECT_NODE_CODE = "task1";				//事件采集
	private static final String STREET_DEPARTMENT_NODE_CODE = "task2";		//街道专业部门采集
	private static final String DISTRICT_DEPARTMENT_NODE_CODE = "task3";	//区专业部门采集
	private static final String DISTRICT_NODE_CODE = "task4";				//指挥中心采集
	private static final String RECEPTIONIST_NODE_CODE = "task5";			//受理员审核
	private static final String SHIFT_LEADER_NODE_CODE = "task6";			//值班长审核
	private static final String DEPARTMENT_NODE_CODE = "task7";				//专业部门处理
	private static final String END_NODE_CODE = "end1";						//归档环节
	
	/**
	 * 初始化事件状态Map
	 */
	protected Map<String, Object> initStatusMap(String chiefLevel) {
		Map<String, Object> statusMap = new HashMap<String, Object>();
		
		statusMap.put(START_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);		//事件启动流程
		
		//事件采集
		statusMap.put(COLLECT_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);		
		statusMap.put(STREET_DEPARTMENT_NODE_CODE+"-"+RECEPTIONIST_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);
		statusMap.put(DISTRICT_DEPARTMENT_NODE_CODE+"-"+RECEPTIONIST_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);
		statusMap.put(DISTRICT_NODE_CODE+"-"+RECEPTIONIST_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);
		
		//上报
		statusMap.put(RECEPTIONIST_NODE_CODE+"-"+SHIFT_LEADER_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(DEPARTMENT_NODE_CODE+"-"+RECEPTIONIST_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		//下派
		statusMap.put(RECEPTIONIST_NODE_CODE+"-"+DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(SHIFT_LEADER_NODE_CODE+"-"+RECEPTIONIST_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		//事件归档
		statusMap.put(END_NODE_CODE, ConstantValue.EVENT_STATUS_END);			
		
		return statusMap;
	}

}
