package cn.ffcs.zhsq.decisionMaking;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 鼓楼网格(GLGrid) 事件状态变更决策类
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
@Service(value = "eventStatusGLGridDecisionMakingService")
public class EventStatusGLGridDecisionMakingServiceImpl extends
			EventStatusSimingDecisionMakingServiceImpl {

	@Autowired
	private IEventDisposalService eventDisposalService;
	
	//环节编码
	private static final String START_NODE_CODE = "start";					//事件流程启动
	private static final String COLLECT_NODE_CODE = "task1";				//事件采集处理环节
	private static final String GRID_ADMIN_NODE_CODE = "task2";				//网格员处理环节
	private static final String COMMUNITY_NODE_CODE_COLLECT = "task3";		//社区处理环节
	private static final String STREET_NODE_CODE_COLLECT = "task4";			//街道处理环节
	private static final String DISTRICT_COMMAND_CENTER_NODE_CODE = "task5";//区网格指挥中心受理处理环节
	private static final String DISTRICT_DEPARTMENT_NODE_CODE = "task6";	//区直处置单位办理
	private static final String COMMUNITY_NODE_CODE_SEND = "task7";			//社区处理处理环节
	private static final String STREET_NODE_CODE_SEND = "task8";			//街道处理处理环节
	private static final String DISTRICT_COMMAND_CENTER_NODE_CODE_CHECK = "task9";	//区指挥中心核查、结案处理环节
	private static final String COLLECTOR_NODE_CODE = "task10";				//网格采集员核查反馈处理环节
	private static final String END_NODE_CODE = "end1";						//归档环节
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		Long eventId = -1L,			//事件id
			 userId = -1L,			//事件操作人员Id
			 orgId = -1L;			//事件操作人员所属组织
		
		String eventStatus = "",	//事件状态
			   curNodeCode = "",	//当前环节节点编码
			   nextNodeCode = "";	//下一环节节点编码
		
		Date handleDate = null;		//环节办理时限
		
		if(CommonFunctions.isNotBlank(params, "eventId")) {
			try{
				eventId = Long.valueOf(params.get("eventId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[eventId]："+params.get("eventId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[eventId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "nextNodeCode")) {
			nextNodeCode = params.get("nextNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[nextNodeCode]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "userId")) {
			try{
				userId = Long.valueOf(params.get("userId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[userId]："+params.get("userId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[userId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			try{
				orgId = Long.valueOf(params.get("orgId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[orgId]："+params.get("orgId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[orgId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "handleDate")) {
			handleDate = (Date)params.get("handleDate");
		}
		
		eventStatus = this.updateEventStatus(eventId, userId, orgId, "", curNodeCode, nextNodeCode, handleDate, params);
		
		return eventStatus;
	}

	/**
	 * 初始化事件状态Map
	 */
	protected Map<String, Object> initStatusMap(String chiefLevel) {
		Map<String, Object> statusMap = new HashMap<String, Object>();
		
		statusMap.put(START_NODE_CODE+"-"+COLLECT_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);		//事件启动流程
		statusMap.put(COLLECT_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);		//事件采集
		
		//上报、越级上报
		statusMap.put(GRID_ADMIN_NODE_CODE+"-"+DISTRICT_COMMAND_CENTER_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(COMMUNITY_NODE_CODE_COLLECT+"-"+DISTRICT_COMMAND_CENTER_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(STREET_NODE_CODE_COLLECT+"-"+DISTRICT_COMMAND_CENTER_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		statusMap.put(GRID_ADMIN_NODE_CODE+"-"+DISTRICT_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(COMMUNITY_NODE_CODE_COLLECT+"-"+DISTRICT_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(STREET_NODE_CODE_COLLECT+"-"+DISTRICT_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		statusMap.put(COMMUNITY_NODE_CODE_SEND+"-"+DISTRICT_COMMAND_CENTER_NODE_CODE_CHECK, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(STREET_NODE_CODE_SEND+"-"+DISTRICT_COMMAND_CENTER_NODE_CODE_CHECK, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(DISTRICT_DEPARTMENT_NODE_CODE+"-"+DISTRICT_COMMAND_CENTER_NODE_CODE_CHECK, ConstantValue.EVENT_STATUS_REPORT);
		
		statusMap.put(COLLECTOR_NODE_CODE+"-"+DISTRICT_COMMAND_CENTER_NODE_CODE_CHECK, ConstantValue.EVENT_STATUS_REPORT);
		
		//分流、下派
		statusMap.put(DISTRICT_COMMAND_CENTER_NODE_CODE+"-"+COMMUNITY_NODE_CODE_SEND, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_COMMAND_CENTER_NODE_CODE+"-"+STREET_NODE_CODE_SEND, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_COMMAND_CENTER_NODE_CODE+"-"+DISTRICT_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		statusMap.put(DISTRICT_COMMAND_CENTER_NODE_CODE_CHECK+"-"+COLLECTOR_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		//事件归档
		statusMap.put(END_NODE_CODE, ConstantValue.EVENT_STATUS_END);			
		
		return statusMap;
	}
}
