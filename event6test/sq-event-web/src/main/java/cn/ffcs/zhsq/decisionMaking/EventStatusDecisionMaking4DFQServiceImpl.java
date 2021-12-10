package cn.ffcs.zhsq.decisionMaking;

import cn.ffcs.workflow.om.OperateType;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 江苏省盐城市 大丰区(DFQ) 事件状态变更决策类
 * @author zhangls
 *
 */
@Service(value = "eventStatusDecisionMaking4DFQService")
public class EventStatusDecisionMaking4DFQServiceImpl extends EventStatusDecisionMaking4JYSServiceImpl {
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	/**
	 * 更新事件状态，并新增中间记录
	 * @param eventId
	 * @param userId
	 * @param orgId
	 * @param chiefLevel
	 * @param curNodeCode
	 * @param nextNodeCode
	 * @param handleDate
	 * @return
	 * @throws Exception
	 */
	protected String updateEventStatus(Long eventId, Long userId, Long orgId, String chiefLevel, String curNodeCode, String nextNodeCode, Date handleDate, Map<String, Object> extraParam) throws Exception {
		Long instanceId = null;
		boolean isClosed = false;
		String eventStatus = super.updateEventStatus(eventId, userId, orgId, chiefLevel, curNodeCode, nextNodeCode, handleDate, extraParam);
		
		if(ConstantValue.EVENT_STATUS_RECEIVED.equals(eventStatus) 
				|| ConstantValue.EVENT_STATUS_REPORT.equals(eventStatus)
				|| ConstantValue.EVENT_STATUS_DISTRIBUTE.equals(eventStatus)
				|| ConstantValue.EVENT_STATUS_ARCHIVE.equals(eventStatus)) {
			if(CommonFunctions.isNotBlank(extraParam, "instanceId")) {
				instanceId = Long.valueOf(extraParam.get("instanceId").toString());
			} else {
				instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
			}
			
			if(instanceId != null && instanceId > 0) {
				Map<String, Object> doneTaskMap = eventDisposalWorkflowService.findDoneTaskInfoLatest(instanceId, CLOSE_NODE_CODE);
				String OPERATE_PASS = "1";//通过操作
				
				if(CommonFunctions.isNotBlank(doneTaskMap, "operateType")) {
					isClosed = OPERATE_PASS.equals(OperateType.getIndexByName(doneTaskMap.get("operateType").toString()));
				}
				
				if(isClosed && CommonFunctions.isBlank(extraParam, "isReject")) {//结案后，未有驳回操作时，将子状态设置为待核实
					EventDisposal eventTmp = new EventDisposal();
					
					eventStatus = ConstantValue.EVENT_STATUS_ARCHIVE;
					eventTmp.setEventId(eventId);
					eventTmp.setStatus(eventStatus);
					eventTmp.setSubStatus(ConstantValue.CONFIRMING_STATUS);
					
					eventDisposalService.updateEventDisposalById(eventTmp);
				}
			}
		}
		
		return eventStatus;
	}
}
