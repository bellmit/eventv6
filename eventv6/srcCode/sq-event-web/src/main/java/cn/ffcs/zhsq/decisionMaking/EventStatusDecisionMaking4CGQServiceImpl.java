package cn.ffcs.zhsq.decisionMaking;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 甘肃省兰州市城关区(CGQ)新版事件通用 事件状态变更决策类
 * 必填参数
 * 		decisionService 指定实现类 eventStatusDecisionMaking4CGQService
 * 		eventId			事件id
 * 		curNodeCode		当前节点编码
 *		nextNodeCode	下一节点编码
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
@Service(value = "eventStatusDecisionMaking4CGQService")
public class EventStatusDecisionMaking4CGQServiceImpl extends
		EventStatusDecisionMakingServiceImpl {
	private static final String DIGITAL_CITY_MANAGEMENT_NODE_CODE = "task13";	//数字城管处理
	
	protected Map<String, Object> initStatusMap(String chiefLevel) {
		Map<String, Object> statusMap = new HashMap<String, Object>();
		
		statusMap.putAll(super.initStatusMap(chiefLevel));
		
		//上报
		statusMap.put(DIGITAL_CITY_MANAGEMENT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		return statusMap;
	}
}
