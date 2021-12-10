package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.elastic.util.CommonFunctions;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 重庆万州区(WanZHouQu) 事件状态变更决策类
 * 必填参数
 * 		decisionService 指定实现类 eventStatusDecisionMaking4WZHQService
 * 		eventId			事件id
 * 		curNodeCode		当前节点编码
 *		nextNodeCode	下一节点编码
 * 		userId			办理人员id
 * 		orgId			办理人员所属组织
 * 非必填参数
 * 		handleDate		环节处理时限，办结时为结案时间
 * 		isReject		true时，表示当前操作为驳回操作
 * 		isRecall		true时，表示当前操作为撤回操作
 *      isRecovery		true时，表示只是恢复事件状态，不添加事件中间过程
 * 		preNodeCode		上一节点编码，当isReject/isRecall为true时，传入
 * 
 * @ClassName:   EventStatusDecisionMaking4WZHQServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年4月23日 上午10:55:09
 */
@Service(value = "eventStatusDecisionMaking4WZHQService")
public class EventStatusDecisionMaking4WZHQServiceImpl extends EventStatusDecisionMakingServiceImpl {
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	private static String STREET_LEADER_ASSIGNMENT_NODE_CODE = "task12";
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		String formTypeIdStr = null, eventStatus = null;
		
		if(CommonFunctions.isNotBlank(params, "formTypeId")) {
			formTypeIdStr = params.get("formTypeId").toString();
		} else if(CommonFunctions.isNotBlank(params, "instanceId")) {
			Long instanceId = null;
			ProInstance proInstance = null;
			
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {}
			
			if(instanceId != null && instanceId > 0) {
				proInstance = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
				
				if(proInstance != null) {
					formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
				}
			}
		} else if(CommonFunctions.isNotBlank(params, "eventId")) {
			Long eventId = null;
			ProInstance proInstance = null;
			
			try {
				eventId = Long.valueOf(params.get("eventId").toString());
			} catch(NumberFormatException e) {}
			
			if(eventId != null && eventId > 0) {
				proInstance = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
				
				if(proInstance != null) {
					formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
				}
			}
		}
		
		if(ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID.equals(formTypeIdStr)) {
			eventStatus = super.makeDecision(params);
		}
		
		return eventStatus;
	}
	
	protected Map<String, Object> initStatusMap(String chiefLevel) {
		Map<String, Object> statusMap = super.initStatusMap(chiefLevel);
		
		//上报
		statusMap.put(STREET_NODE_CODE+"-"+STREET_LEADER_ASSIGNMENT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		return statusMap;
	}
}
