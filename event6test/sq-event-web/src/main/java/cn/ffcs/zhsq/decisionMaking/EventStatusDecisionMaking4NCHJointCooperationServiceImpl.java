package cn.ffcs.zhsq.decisionMaking;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 南昌市大联动事件 事件状态变更决策类
 * 必填参数
 * 		decisionService 指定实现类 eventStatusDecisionMaking4NCHJointCooperationService
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
@Service(value = "eventStatusDecisionMaking4NCHJointCooperationService")
public class EventStatusDecisionMaking4NCHJointCooperationServiceImpl extends
		EventStatusDecisionMakingServiceImpl {
	@Autowired
	private IEventExtendService eventExtendService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	private final String JOINT_HANDLE_NODE = "task14",		//联合办理
			   	   	 	 JOINT_OPERATE_NODE = "task15",		//联席交办
			   	   	 	 CITIZEN_HOTLINE_NODE = "task16";	//市民热线
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		return super.makeDecision(params);
	}
	
	/**
	 * 初始化事件状态Map
	 */
	protected Map<String, Object> initStatusMap(String chiefLevel) {
		Map<String, Object> statusMap = super.initStatusMap(chiefLevel);
		
		//事件下派
		statusMap.put(STREET_NODE_CODE+"-"+JOINT_HANDLE_NODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_NODE_CODE+"-"+JOINT_HANDLE_NODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(MUNICIPAL_NODE_CODE+"-"+JOINT_HANDLE_NODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		statusMap.put(DISTRICT_NODE_CODE+"-"+JOINT_OPERATE_NODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(MUNICIPAL_NODE_CODE+"-"+JOINT_OPERATE_NODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		//事件上报
		statusMap.put(GRID_ADMIN_NODE_CODE+"-"+CITIZEN_HOTLINE_NODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(COMMUNITY_NODE_CODE+"-"+CITIZEN_HOTLINE_NODE, ConstantValue.EVENT_STATUS_REPORT);
		
		return statusMap;
	}
	
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
		String eventStatus = super.updateEventStatus(eventId, userId, orgId, chiefLevel, curNodeCode, nextNodeCode, handleDate, extraParam);
		
		if(CommonFunctions.isNotBlank(extraParam, "isReject")) {//判断是否是驳回操作
			boolean isReject = Boolean.valueOf(extraParam.get("isReject").toString());
			if(isReject) {
				nextNodeCode = REJECT_NODE_CODE;
			}
		}
		
		//联席交办 联合办理 调整事件扩展信息
		if(JOINT_HANDLE_NODE.equals(nextNodeCode) || JOINT_OPERATE_NODE.equals(nextNodeCode)
		|| ((JOINT_HANDLE_NODE.equals(curNodeCode) || JOINT_OPERATE_NODE.equals(curNodeCode)) && REJECT_NODE_CODE.equals(nextNodeCode))) {
			Map<String, Object> eventExtend = new HashMap<String, Object>();
			String JOINT_HANDLED_YES = "1", JOINT_HANDLED_NO = "0",
				   JOINT_OPERATE_YES = "1", JOINT_OPERATE_NO = "0";
			
			eventExtend.put("eventId", eventId);
			eventExtend.put("creatorId", userId);
			eventExtend.put("updaterId", userId);
			
			if(JOINT_HANDLE_NODE.equals(nextNodeCode)) {
				eventExtend.put("isJointHandled", JOINT_HANDLED_YES);
				eventExtend.put("isJointOperated", JOINT_OPERATE_NO);
			} else if(JOINT_OPERATE_NODE.equals(nextNodeCode)) {
				eventExtend.put("isJointHandled", JOINT_HANDLED_NO);
				eventExtend.put("isJointOperated", JOINT_OPERATE_YES);
			} else if(REJECT_NODE_CODE.equals(nextNodeCode)) {//驳回操作时，去除联合办理、联席交办标识
				if(JOINT_HANDLE_NODE.equals(curNodeCode)) {
					eventExtend.put("isJointHandled", JOINT_HANDLED_NO);
				} else if(JOINT_OPERATE_NODE.equals(curNodeCode)) {
					eventExtend.put("isJointOperated", JOINT_OPERATE_NO);
				}
			}
			
			eventExtendService.saveEventExtend(eventExtend);
		} else if(CITIZEN_HOTLINE_NODE.equals(nextNodeCode)) {
			//提交给“市民热线”节点时，清除事件办理时限，并将办理状态设置为正常
			EventDisposal event = new EventDisposal();
			event.setEventId(eventId);
			event.setHandleDate(null);
			event.setHandleDateStr("");
			event.setHandleDateFlag(ConstantValue.LIMIT_TIME_STATUS_NORMAL);
			
			eventDisposalService.updateEventDisposalById(event);
		}
		
		return eventStatus;
	}
	
}
