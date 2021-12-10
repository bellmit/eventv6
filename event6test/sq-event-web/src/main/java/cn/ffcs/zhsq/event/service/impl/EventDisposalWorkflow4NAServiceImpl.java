package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.common.message.bo.ReceiverBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.decisionMaking.EventStatusDecisionMaking4NASServiceImpl;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.eventExpand.service.IEventDisposal4ExpandService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 泉州市南安市(NanAnShi) 工作流接口
 * @ClassName:   EventDisposalWorkflow4NAServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年6月29日 下午4:30:50
 */
@Service(value = "eventDisposalWorkflow4NAService")
public class EventDisposalWorkflow4NAServiceImpl extends EventDisposalWorkflowForEventNewServiceImpl {
	@Autowired
	private MessageOutService messageService;

	@Autowired
	private IEventDisposalService eventDisposalService;

	@Autowired
	private IBaseWorkflowService baseWorkflowService;

	@Autowired
	private EventStatusDecisionMaking4NASServiceImpl eventStatusDecisionMaking4NASService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;

	//事件扩展信息接口
	@Autowired
	private IEventDisposal4ExpandService eventDisposalExpandService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	private static final String CLOSE_NODE_CODE = "task8";//结案环节编码
	private static final String DISTRICT_ARCHIVE_BUREAU_NODE_CODE = "task15";//档案局处理
	private static final String END_NODE_CODE = "end1";//归档环节节点编码
	private static final String ARCHIVE_COLLECT_TYPE = "24";
	//反诈骗事件类型、森林防火宣传、档案征集 字典编码值集合
	private static final List<String> EVENT_TYPE_LIST = new ArrayList<>(Arrays.asList("22", "23", ARCHIVE_COLLECT_TYPE));

	@SuppressWarnings("unchecked")
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String instanceId = null, type = null;
		EventDisposal event = null;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(CommonFunctions.isNotBlank(extraParam, "type")){
			type = String.valueOf(extraParam.get("type"));
		} else {
			if(null != eventId && eventId > 0){
				event = eventDisposalService.findEventByIdSimple(eventId);
			}
			if(null != event && StringUtils.isNotBlank(event.getType())){
				type = event.getType();
			}
		}
		
		instanceId = super.startFlowByWorkFlow(eventId, workFlowId, toClose, userInfo, extraParam);
		
		if(StringUtils.isNotBlank(instanceId)) {
			if(ARCHIVE_COLLECT_TYPE.equals(type)) {
				//档案征集自动扭转至下一环节
				Long instanceIdL = Long.valueOf(instanceId);
				Long curTaskId = this.curNodeTaskId(instanceIdL);
				String curTaskIdStr = String.valueOf(curTaskId);
				Map<String, Object> initMap = null, initParams = new HashMap<String, Object>();
				Node archiveNode = null;
				
				initParams.putAll(extraParam);
				initParams.put("event", event);
				initParams.put("eventId", eventId);
				
				initMap = initFlowInfo(curTaskIdStr, instanceIdL, userInfo, initParams);
				
				if(CommonFunctions.isNotBlank(initMap, "taskNodes")) {
					List<Node> taskNodesList = (List<Node>) initMap.get("taskNodes");
					
					if(taskNodesList.size() == 1) {
						archiveNode = taskNodesList.get(0);
					}
				}
				
				if(archiveNode != null) {
					Map<String, Object> fiveKeyParam = new HashMap<String, Object>();
					List<GdZTreeNode> treeNodeList = null;
					Long treeOrgId = null;
					List<UserInfo> nextUserInfoList = null;
					StringBuffer nextUserId = new StringBuffer(""),
								 nextOrgId = new StringBuffer("");
					
					fiveKeyParam.putAll(extraParam);
					fiveKeyParam.put("nodeId", archiveNode.getNodeId());
					
					treeNodeList = fiveKeyElementService.getTreeForEvent(userInfo, null, archiveNode.getTransitionCode(), null, null, fiveKeyParam);
					if(treeNodeList != null) {
						treeOrgId = Long.valueOf(treeNodeList.get(0).getId());
					}
					
					if(treeOrgId != null && treeOrgId > 0) {
						nextUserInfoList = fiveKeyElementService.getUserInfoList(treeOrgId, archiveNode.getTransitionCode(), fiveKeyParam);
					}
					
					if(nextUserInfoList != null) {
						for(UserInfo nextUserInfo : nextUserInfoList) {
							nextUserId.append(",").append(nextUserInfo.getUserId());
							nextOrgId.append(",").append(nextUserInfo.getOrgId());
						}
						
						if(nextUserId.length() > 0 && nextOrgId.length() > 0) {
							String advice = null;
							
							if(CommonFunctions.isNotBlank(extraParam, "advice")) {
								advice = extraParam.get("advice").toString();
							}
							
							this.subWorkFlowForEndingAndEvaluate(instanceIdL, curTaskIdStr, archiveNode.getNodeName(), nextUserId.substring(1), nextOrgId.substring(1), advice, userInfo, null, extraParam);
						}
					}
				}
				
			} else if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose) && EVENT_TYPE_LIST.contains(type)) {
				//反诈事件 森林防火宣传事件 采集并结案时，直接归档
				this.endEventAndEva(eventId,Long.valueOf(instanceId),null,type,userInfo,extraParam);
			}
		}

		return instanceId;
	}

	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception {
		boolean result = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		
		if(result) {
			//当前环节为处理中时，不做消息提醒
			if(!ConstantValue.HANDLING_TASK_CODE.equals(nextNodeName)) {
				sendMsg4Event(instanceId, null, nextStaffId, curOrgIds, userInfo);
			}

			//反诈骗事件，下一环节为结案，结案成功后直接归档
			if(CLOSE_NODE_CODE.equals(nextNodeName)){
				this.endEventAndEva(null,instanceId,nextNodeName,null,userInfo,extraParam);
			}
			
			if(CommonFunctions.isNotBlank(extraParam, "isAdopted")) {
				String isAdopted = extraParam.get("isAdopted").toString();
				Long eventId = null;
				
				try {
					if(CommonFunctions.isNotBlank(extraParam, "eventId")) {
						eventId = Long.valueOf(extraParam.get("eventId").toString());
					} else if(CommonFunctions.isNotBlank(extraParam, "formId")) {
						eventId = Long.valueOf(extraParam.get("formId").toString());
					}
				} catch(NumberFormatException e) {}
				
				if(eventId != null && eventId > 0) {
					EventDisposal event = new EventDisposal();
					event.setEventId(eventId);
					event.setIsAdopted(isAdopted);
					event.setUpdatorId(userInfo.getUserId());
					
					eventDisposalService.updateEventDisposalById(event);
				}
			}
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId, UserInfo userInfo, Map<String, Object> params) {
		Map<String,Object> resultMap = super.initFlowInfo(taskId, instanceId, userInfo, params);
		EventDisposal event = null;
		//事件类型为 全民反诈行动 时，下一环节只显示 结案
		String type = null, eventStatus = null, eventSubStatus = null;
		
		if(CommonFunctions.isNotBlank(params, "event")) {
			event = (EventDisposal) params.get("event");
		} else if(CommonFunctions.isNotBlank(params,"eventId")) {
			Long eventId = null;
			
			try {
				eventId = Long.valueOf(String.valueOf(params.get("eventId")));
			} catch(NumberFormatException e) {}
			
			if(null != eventId && eventId > 0){
				event = eventDisposalService.findEventByIdSimple(eventId);
			}
		}
		
		if(CommonFunctions.isNotBlank(params,"type")){
			type = String.valueOf(params.get("type"));
		} else if(null != event && StringUtils.isNotBlank(event.getType())) {
			type = event.getType();
		}
		
		if(event != null) {
			eventStatus = event.getStatus();
			eventSubStatus = event.getSubStatus();
		}
		
		if(ConstantValue.EVENT_STATUS_RECEIVED.equals(eventStatus)
				//&& (ConstantValue.REJECT_SUB_STATUS.equals(eventSubStatus) || ConstantValue.RECALL_SUB_STATUS.equals(eventSubStatus))
				&& CommonFunctions.isNotBlank(resultMap, "operateLists")) {
			List<OperateBean> operateLists = (List<OperateBean>) resultMap.get("operateLists");
			OperateBean operateBean = new OperateBean();
			String DELETE_EVENT = "delRecord", DELETE_NAME = "删除";
			
			operateBean.setOperateEvent(DELETE_EVENT);
			operateBean.setAnotherName(DELETE_NAME);
			
			operateLists.add(operateBean);
			
			resultMap.put("isHandleEdit", true);
			resultMap.put("operateLists", operateLists);
		}
		
		if(CommonFunctions.isNotBlank(resultMap,"taskNodes")) {
			List<Node> taskNodesList = null;
			
			if(StringUtils.isNotBlank(type)) {
				List<Node> taskNodes = new ArrayList<>();
				String ARCHIVE_COLLECT_TYPE = "24";//档案征集
				Map<String, Node> taskNodeMap = new HashMap<String, Node>();
				
				taskNodesList = (List<Node>) resultMap.get("taskNodes");
				
				for(Node taskNode : taskNodesList) {
					taskNodeMap.put(taskNode.getNodeName(), taskNode);
				}
				
				if(taskNodeMap.containsKey(DISTRICT_ARCHIVE_BUREAU_NODE_CODE)) {
					Node archiveCollectNode = taskNodeMap.get(DISTRICT_ARCHIVE_BUREAU_NODE_CODE);
					
					if(ARCHIVE_COLLECT_TYPE.equals(type)) {
						taskNodes.add(archiveCollectNode);
					} else {
						taskNodesList.remove(archiveCollectNode);
					}
				}
				
				if(taskNodeMap.containsKey(CLOSE_NODE_CODE)) {
					if(EVENT_TYPE_LIST.contains(type) && taskNodes.isEmpty()) {
						taskNodes.add(taskNodeMap.get(CLOSE_NODE_CODE));
					}
				}
				
				if(taskNodes.size() > 0) {
					taskNodesList = taskNodes;
				}
			}
			
			resultMap.put("taskNodes", taskNodesList);
		}
		
		if(CommonFunctions.isNotBlank(resultMap, "curNodeName")) {
			String curNodeName = resultMap.get("curNodeName").toString();
			
			resultMap.put("isShowAdoptedOption", DISTRICT_ARCHIVE_BUREAU_NODE_CODE.equals(curNodeName));
		}

		return resultMap;
	}

	@Override
	public boolean rejectWorkFlow(Map<String, Object> params, UserInfo userInfo) throws Exception {
		boolean result = super.rejectWorkFlow(params, userInfo);
		
		if(result) {
			Long eventId = -1L, instanceId = -1L;
			
			if(CommonFunctions.isNotBlank(params, "eventId")) {
				eventId = Long.valueOf(params.get("eventId").toString());
			}
			
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			}
			
			sendMsg4Event(instanceId, eventId, null, null, userInfo);
		}
		
		return result;
	}
	
	/**
	 * 发送事件消息
	 * @param instanceId	实例id
	 * @param eventId		事件id
	 * @param nextUserIdStr	下一办理人员，多个值以英文逗号连接，为空时，使用当前办理人员
	 * @param nextOrgIdStr	下一办理人员组织，多个值以英文逗号连接，nextUserIdStr为空时使用
	 * @param userInfo		办理用户信息
	 */
	private void sendMsg4Event(Long instanceId, Long eventId, String nextUserIdStr, String nextOrgIdStr, UserInfo userInfo) {
		if(instanceId != null && instanceId > 0 && (eventId == null || eventId < 0)) {
			ProInstance pro = this.capProInstanceByInstanceId(instanceId);
			
			if(pro != null) {
				eventId = pro.getFormId();
			}
		} else if(eventId != null && eventId > 0 && (instanceId == null || instanceId < 0)) {
			instanceId = this.capInstanceIdByEventIdForLong(eventId);
		}
		
		if(instanceId != null && instanceId > 0 && eventId != null && eventId > 0) {
			ReceiverBO receiver = null;
			
			if(StringUtils.isBlank(nextUserIdStr) && StringUtils.isBlank(nextOrgIdStr)) {
				nextUserIdStr = this.curNodeUserIds(instanceId);
			}
			
			if(StringUtils.isNotBlank(nextUserIdStr)) {
				String[] nextUserIdArray = nextUserIdStr.split(",");
				List<Long> nextUserIdList = new ArrayList<Long>();
				receiver = new ReceiverBO();
				
				for(String nextUserId : nextUserIdArray) {
					nextUserIdList.add(Long.valueOf(nextUserId));
				}
				
				receiver.setUserIdList(nextUserIdList);
				
				
			} else if(StringUtils.isNotBlank(nextOrgIdStr)) {
				String[] nextOrgIdArray = nextOrgIdStr.split(",");
				List<Long> nextOrgIdList = new ArrayList<Long>();
				receiver = new ReceiverBO();
				
				for(String nextOrgId : nextOrgIdArray) {
					nextOrgIdList.add(Long.valueOf(nextOrgId));
				}
				
				receiver.setOrgIdList(nextOrgIdList);
			}
			
			if(receiver != null) {
				String EVENT_MEG_MODULE_CODE = "02",//事件消息所属模块编码
					   EVENT_MSG_ACTION = "发送了",//消息发送操作
					   viewLink = null, 
					   msgContent = "您有一条待办事件，请尽快处理";//消息内容
				
				messageService.sendCommonMessageA(userInfo.getUserId(), userInfo.getOrgId(), EVENT_MEG_MODULE_CODE, eventId, viewLink, msgContent, EVENT_MSG_ACTION, receiver);
			}
			
		} else {
			logger.error("未发送事件消息，流程实例id为[" + instanceId + "]， 事件id为[" + eventId + "]！");
		}
	}

	/**
	 * 反诈事件采集并结案时，直接进行归档评价（未结案的先进行结案）
	 * @param eventId 		事件id
	 * @param instanceId 	实例Id
	 * @param nextNodeName 	下一办理节点
	 * @param type 			事件类型
	 * @param userInfo 		操作用户
	 * @param extraParam
	 * 			eventId		事件id
	 * 			formId		事件id
	 * 			advice		办理意见
	 * @return
	 * @throws Exception
	 */
	private void endEventAndEva(Long eventId,Long instanceId,String nextNodeName,String type,UserInfo userInfo,Map<String,Object> extraParam) throws Exception {
		if(null == eventId || eventId < 0){
			if(CommonFunctions.isNotBlank(extraParam,"eventId")){
				eventId = Long.valueOf(String.valueOf(extraParam.get("eventId")));
			}else if(CommonFunctions.isNotBlank(extraParam,"formId")){
				eventId = Long.valueOf(String.valueOf(extraParam.get("formId")));
			}else if(null != instanceId && instanceId > 0){
				ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
				if(proInstance != null){
					eventId = proInstance.getFormId();
				}
			}
		}

		if(null != eventId && eventId > 0 && StringUtils.isBlank(type)){
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);

			if(null != event && StringUtils.isNotBlank(event.getType())){
				type = event.getType();
			}
		}

		if(StringUtils.isNotBlank(type) && EVENT_TYPE_LIST.contains(type)){
			//事件进行归档
			if (baseWorkflowService.endWorkflow4Base(instanceId,userInfo,extraParam)){

				Map<String,Object> params = new HashMap<>();
				params.put("decisionService","eventStatusDecisionMaking4NASService");
				params.put("eventId",eventId);
				params.put("curNodeCode",END_NODE_CODE);
				params.put("nextNodeCode",END_NODE_CODE);
				params.put("userId",userInfo.getUserId());
				params.put("orgId",userInfo.getOrgId());
				//更新事件状态为结束
				eventStatusDecisionMaking4NASService.makeDecision(params);

				//采集并结案的事件默认添加评价信息，评价等级为：满意
				String evaLevel = "02";
				String evaContent = "满意";
				try {
					eventDisposalExpandService.saveOrUpdateEventEvaluate(userInfo,eventId,evaLevel,evaContent,params);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
}
