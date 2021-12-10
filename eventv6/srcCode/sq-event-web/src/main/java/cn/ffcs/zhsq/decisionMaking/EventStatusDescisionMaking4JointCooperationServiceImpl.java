package cn.ffcs.zhsq.decisionMaking;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 大联动(JointCooperation) 事件状态变更决策类
 * 必填参数
 * 		decisionService 指定实现类 eventStatusGLGridDecisionMakingService
 * 		eventId			事件id
 * 		curNodeCode		当前节点名称
 * 		nextNodeCode	下一节点名称
 * 		userId			办理人员id
 * 		orgId			办理人员所属组织
 * 非必填参数
 * 		handleDate		环节处理时限，办结时为结案时间
 * 		isReject		true时，表示当前操作为驳回操作
 * 		preNodeCode		上一节点编码，当isReject为true时，传入
 * 
 * @author zhangls
 *
 */
@Service(value = "eventStatusDescisionMaking4JointCooperationService")
public class EventStatusDescisionMaking4JointCooperationServiceImpl extends
		EventStatusDecisionMakingServiceImpl {
	//环节编码
	private static final String START_NODE_CODE = "start";							//事件流程启动
	private static final String COLLECT_NODE_CODE = "task1";						//事件采集
	private static final String COMMUNITY_NODE_CODE_COLLECT = "task2";				//社区处理
	private static final String STREET_CENTER_NODE_CODE_COLLECT = "task3";			//街道指挥中心处理
	private static final String STREET_DEPARTMENT_NODE_CODE_COLLECT = "task4";		//街道处置单位处理
	private static final String DISTRICT_CENTER_NODE_CODE_COLLECT = "task5";		//区指挥中心处理
	private static final String DISTRICT_DEPARTMENT_NODE_CODE_COLLECT = "task6";	//区处置单位处理
	
	private static final String STREET_CENTER_NODE_CODE = "task7";					//街道指挥中心处理
	private static final String DISTRICT_CENTER_NODE_CODE = "task8";				//区指挥中心处理
	
	private static final String COMMUNITY_NODE_CODE_SEND = "task9";					//社区处理
	private static final String STREET_DEPARTMENT_NODE_CODE_SEND = "task10";		//街道处置单位处理
	private static final String DISTRICT_DEPARTMENT_NODE_CODE_SEND = "task11";		//区处置单位处理
	private static final String EVALUATE_NODE_CODE = "task12";						//核查评价
	private static final String END_NODE_CODE = "end1";								//归档环节
	
	/**
	 * 初始化事件状态Map
	 */
	protected Map<String, Object> initStatusMap(String chiefLevel) {
		Map<String, Object> statusMap = new HashMap<String, Object>();
		
		statusMap.put(START_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);		//事件启动流程
		statusMap.put(COLLECT_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);		//事件采集
		
		//上报、越级上报
		statusMap.put(COMMUNITY_NODE_CODE_COLLECT+"-"+STREET_CENTER_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(STREET_DEPARTMENT_NODE_CODE_COLLECT+"-"+STREET_CENTER_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		statusMap.put(COMMUNITY_NODE_CODE_COLLECT+"-"+DISTRICT_CENTER_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(STREET_DEPARTMENT_NODE_CODE_COLLECT+"-"+DISTRICT_CENTER_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		statusMap.put(DISTRICT_DEPARTMENT_NODE_CODE_COLLECT+"-"+DISTRICT_CENTER_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		statusMap.put(STREET_CENTER_NODE_CODE_COLLECT+"-"+DISTRICT_CENTER_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(STREET_CENTER_NODE_CODE+"-"+DISTRICT_CENTER_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		//分流、下派
		statusMap.put(STREET_CENTER_NODE_CODE+"-"+COMMUNITY_NODE_CODE_SEND, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(STREET_CENTER_NODE_CODE+"-"+STREET_DEPARTMENT_NODE_CODE_SEND, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		statusMap.put(DISTRICT_CENTER_NODE_CODE+"-"+DISTRICT_DEPARTMENT_NODE_CODE_SEND, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_CENTER_NODE_CODE+"-"+STREET_CENTER_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		statusMap.put(DISTRICT_CENTER_NODE_CODE_COLLECT+"-"+STREET_CENTER_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_CENTER_NODE_CODE+"-"+STREET_CENTER_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		//结案
		statusMap.put(EVALUATE_NODE_CODE, ConstantValue.EVENT_STATUS_ARCHIVE);	//事件评价 用于采集并结案
		
		//归档
		statusMap.put(END_NODE_CODE, ConstantValue.EVENT_STATUS_END);			//事件归档
		
		return statusMap;
	}
}
