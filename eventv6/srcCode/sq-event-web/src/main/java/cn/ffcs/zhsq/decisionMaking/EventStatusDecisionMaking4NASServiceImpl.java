package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 南安市(NanAnShi) 事件状态变更决策类
 * 必填参数
 * 		decisionService 指定实现类 eventStatusDecisionMaking4NASService
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
 * @author zhangls
 *
 */
@Service(value = "eventStatusDecisionMaking4NASService")
public class EventStatusDecisionMaking4NASServiceImpl extends EventStatusDecisionMakingServiceImpl {
	private static final String DISTRICT_DEPARTMENT_BRIGADE_NODE_CODE = "task13";	//区职能部门执法大队处理
	private static final String DISTRICT_DEPARTMENT_SQUADRON_NODE_CODE = "task14";	//区职能部门执法中队处理
	private static final String DISTRICT_ARCHIVE_BUREAU_NODE_CODE = "task15";//档案局处理
	
	protected Map<String, Object> initStatusMap(String chiefLevel) {
		Map<String, Object> statusMap = super.initStatusMap(chiefLevel);
		
		//上报
		statusMap.put(GRID_ADMIN_NODE_CODE+"-"+DISTRICT_ARCHIVE_BUREAU_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(COMMUNITY_NODE_CODE+"-"+DISTRICT_ARCHIVE_BUREAU_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(STREET_NODE_CODE+"-"+DISTRICT_ARCHIVE_BUREAU_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(STREET_DEPARTMENT_NODE_CODE+"-"+DISTRICT_ARCHIVE_BUREAU_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		//下派
		statusMap.put(DISTRICT_DEPARTMENT_NODE_CODE+"-"+DISTRICT_DEPARTMENT_BRIGADE_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_DEPARTMENT_BRIGADE_NODE_CODE+"-"+DISTRICT_DEPARTMENT_SQUADRON_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_DEPARTMENT_BRIGADE_NODE_CODE+"-"+STREET_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_DEPARTMENT_SQUADRON_NODE_CODE+"-"+STREET_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		statusMap.put(DISTRICT_NODE_CODE+"-"+DISTRICT_ARCHIVE_BUREAU_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_DEPARTMENT_NODE_CODE+"-"+DISTRICT_ARCHIVE_BUREAU_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		return statusMap;
	}
}
