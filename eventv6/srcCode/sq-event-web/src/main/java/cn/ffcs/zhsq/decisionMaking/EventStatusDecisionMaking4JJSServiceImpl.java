package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 晋江市(JJS)事件状态变更决策类
 * 流程图名称：新版通用事件_晋江市
 * 必填参数
 * 		decisionService 指定实现类 eventStatusDecisionMaking4JJSService
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
@Service(value = "eventStatusDecisionMaking4JJSService")
public class EventStatusDecisionMaking4JJSServiceImpl extends
		EventStatusDecisionMakingServiceImpl {
	private static final String STREET_DEPARTMENT_SIGN_NODE_CODE = "task11";		//街道职能部门(会签)办理
	private static final String DISTRICT_DEPARTMENT_SIGN_NODE_CODE = "task12";	//区职能部门(会签)办理
	
	/**
	 * 初始化事件状态Map
	 */
	protected Map<String, Object> initStatusMap(String chiefLevel) {
		Map<String, Object> statusMap = super.initStatusMap(chiefLevel);
		
		//上报、越级上报
		statusMap.put(STREET_DEPARTMENT_SIGN_NODE_CODE+"-"+STREET_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(DISTRICT_DEPARTMENT_SIGN_NODE_CODE+"-"+DISTRICT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		//分流、下派
		statusMap.put(STREET_NODE_CODE+"-"+STREET_DEPARTMENT_SIGN_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(STREET_DEPARTMENT_SIGN_NODE_CODE+"-"+COMMUNITY_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		statusMap.put(DISTRICT_NODE_CODE+"-"+DISTRICT_DEPARTMENT_SIGN_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_DEPARTMENT_SIGN_NODE_CODE+"-"+COMMUNITY_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_DEPARTMENT_SIGN_NODE_CODE+"-"+STREET_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		return statusMap;
	}
}
