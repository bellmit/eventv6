package cn.ffcs.zhsq.event.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.api.enums.BizPlatfromEnum;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 江苏省盐城市 盐都区(YDQ) 工作流使用接口实现
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflow4YDQService")
public class EventDisposalWorkflow4YDQServiceImpl extends EventDisposalWorkflow4YCHSHServiceImpl {
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private IEvent4WorkflowService event4WorkflowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private ITimeApplicationService timeApplicationService;
	
	private String COLLECT_NODE_CODE = "task1";	//事件采集处理环节
	private String EVALUATE_NODE_CODE = "task9";//评价环节
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{
		boolean flag = false;
		
		if(ConstantValue.PREPRESS_TASK_CODE.equals(nextNodeName)) {
			//构建事件预处理记录
			Map<String, Object> timeApplication = new HashMap<String, Object>();
			Long eventId = -1L;
			//盐都区审核回调接口
			String TIME_APPLICATION_CALLBACK_SERVICE_NAME = "timeApplicationCallback4YDQEventService";
			
			if(CommonFunctions.isNotBlank(extraParam, "formId")) {
				try {
					eventId = Long.valueOf(extraParam.get("formId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			} else if(CommonFunctions.isNotBlank(extraParam, "eventId")) {
				try {
					eventId = Long.valueOf(extraParam.get("eventId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			} else {
				ProInstance pro = this.capProInstanceByInstanceId(instanceId);
				if(pro != null) {
					eventId = pro.getFormId();
				}
			}
			
			timeApplication.put("applicationType", ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_PREPRESS.toString());
			timeApplication.put("businessId", instanceId);
			timeApplication.put("businessKeyId", eventId);
			timeApplication.put("creatorId", userInfo.getUserId());
			timeApplication.put("isDuplicatedCheck", false);
			timeApplication.put("isAutoAudit", true);
			timeApplication.put("auditorType", ITimeApplicationCheckService.AUDITOR_TYPE.AUDITOR_NONE.toString());
			timeApplication.put("serviceName", TIME_APPLICATION_CALLBACK_SERVICE_NAME);
			timeApplication.put("reason", advice);
			timeApplication.put("userOrgCode", userInfo.getOrgCode());
			
			timeApplicationService.saveTimeApplication(timeApplication);
			
			flag = true;
		} else {
			flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		}
		
		return flag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		Map<String, Object> initResultMap = super.initFlowInfo(taskId, instanceId, userInfo, params);
		
		if(CommonFunctions.isNotBlank(initResultMap, "taskNodes")) {
			ProInstance proInstance = null;
			boolean is2Prepress = false,//是否进行预处理
					isExistInitCheck = false,//是否存在待审核的预处理事件审核记录
					isExistSuccessCheck = false;//是否存在审核通过的预处理事件审核记录
			Long eventId = -1L;
			
			if(CommonFunctions.isNotBlank(initResultMap, "proInstance")) {
				proInstance = (ProInstance) initResultMap.get("proInstance");
			}
				
			if(proInstance != null) {
				eventId = proInstance.getFormId();
				
				EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
				
				if(event != null) {
					if(ConstantValue.EVENT_STATUS_RECEIVED.equals(event.getStatus())) {
						//判断流程发起人员是否为网格员
						boolean isGridAdmin = fiveKeyElementService.isUserIdGridAdmin(proInstance.getOrgId(), proInstance.getUserId());
						
						if(isGridAdmin) {
							Map<String, Object> timeApplication = new HashMap<String, Object>();
							List<Map<String, Object>> timeApplicationMapList = null;
							
							timeApplication.put("applicationType", ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_PREPRESS.toString());
							timeApplication.put("businessId", instanceId);
							timeApplication.put("businessKeyId", eventId);
							timeApplication.put("creatorId", userInfo.getUserId());
							
							timeApplicationMapList = timeApplicationService.findTimeApplicationList(timeApplication);
							
							if(timeApplicationMapList != null) {
								String timeAppCheckStatus = null;
								
								for(Map<String, Object> timeApplicationMap : timeApplicationMapList) {
									if(CommonFunctions.isNotBlank(timeApplicationMap, "timeAppCheckStatus")) {
										timeAppCheckStatus = timeApplicationMap.get("timeAppCheckStatus").toString();
									}
									
									if(ITimeApplicationCheckService.CHECK_STATUS.STATUS_INIT.toString().equals(timeAppCheckStatus)) {
										isExistInitCheck = true; break;
									} else if(ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.toString().equals(timeAppCheckStatus)) {
										isExistSuccessCheck = true; break;
									}
								}
							}
							
							if(!isExistInitCheck && !isExistSuccessCheck) {
								Map<String, Object> eventQueryMap = new HashMap<String, Object>();
								//当前事件网格内，经纬度10米范围内，当前操作时间3天内的有效事件
								eventQueryMap.put("listType", 5);//辖区所有事件列表
								eventQueryMap.put("userId", userInfo.getUserId());
								eventQueryMap.put("orgId", userInfo.getOrgId());
								eventQueryMap.put("isMapDistanceSearch4SelfEvent", true);
								eventQueryMap.put("selfEventId", eventId);
								
								try {
									is2Prepress = event4WorkflowService.findEventCount(eventQueryMap) > 0;
								} catch (ZhsqEventException e) {
									e.printStackTrace();
								}
							} else {
								is2Prepress = true;
							}
						}
					}
				}
			}
			
			if(is2Prepress) {
				List<Node> nextTaskNodeList = new ArrayList<Node>();
				
				if(isExistInitCheck) {//如果存在未审核的事件预处理信息，则清除所有下一环节
					nextTaskNodeList = new ArrayList<Node>();
					//下一环节为空时的提示信息
					initResultMap.put("emptyTip4NextTaskNode", "案件正在审核中，请联系对应专班。");
				} else if(isExistSuccessCheck) {//如果存在审核通过的事件预处理信息，则保留所有下一环节
					nextTaskNodeList = (List<Node>) initResultMap.get("taskNodes");
				} else {
					List<Node> taskNodes = (List<Node>) initResultMap.get("taskNodes");
					Node nextTaskNode = new Node();
					String CLOSE_NODE_CODE = "JA";//结案环节节点编码
					
					//构建预处理环节，该环节无需人员选择
					nextTaskNode.setNodeId(-1);
					nextTaskNode.setNodeName(ConstantValue.PREPRESS_TASK_CODE);
					nextTaskNode.setNodeNameZH(ConstantValue.PREPRESS_TASK_NAME);//环节中文名称
					nextTaskNode.setNodeType("1");
					nextTaskNode.setTransitionCode(INodeCodeHandler.ORG_UINT + INodeCodeHandler.GRID + "__" + INodeCodeHandler.ORG_UINT + INodeCodeHandler.STREET);
					
					nextTaskNodeList.add(nextTaskNode);
					
					for(Node nextNode : taskNodes) {
						if(CLOSE_NODE_CODE.equals(nextNode.getNodeCode())) {
							nextTaskNodeList.add(nextNode);
							break;
						}
					}
				}
				
				initResultMap.put("taskNodes", nextTaskNodeList);
			}
		}
		
		return initResultMap;
	}
	
	@SuppressWarnings("serial")
	@Override
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int sqlOrder) {
		List<Map<String, Object>> workflowTaskList = super.queryProInstanceDetail(instanceId, sqlOrder);
		boolean isDockedFrom12345 = false;//是否从12345对接而来
		
		if(workflowTaskList != null && workflowTaskList.size() > 0 && instanceId != null && instanceId > 0) {
			ProInstance proInstance = this.capProInstanceByInstanceId(instanceId);
			
			if(proInstance != null) {
				Long eventId = proInstance.getFormId();
				EventDisposal event = this.eventDisposalService.findEventByIdSimple(eventId);
				if(event != null) {
					isDockedFrom12345 = BizPlatfromEnum.YD_SYN_12345.getValue().equals(event.getBizPlatform());
				}
			}
		}
		
		//从12345对接而来的事件，不展示事件采集、评价环节
		if(isDockedFrom12345) {
			List<Map<String, Object>> removeTasks = new ArrayList<Map<String, Object>>();
			Set<String> removeTaskNameSet = new HashSet<String>() {
				{
					add(COLLECT_NODE_CODE);
					add(EVALUATE_NODE_CODE);
				}
			};
			
			//构建可移除的环节
			for(Map<String, Object> mapTemp : workflowTaskList) {
				if(CommonFunctions.isNotBlank(mapTemp, "TASK_CODE")) {
					if(removeTaskNameSet.contains(mapTemp.get("TASK_CODE").toString())) {
						removeTasks.add(mapTemp);
					}
				}
			}
			
			workflowTaskList.removeAll(removeTasks);
		}
		
		return workflowTaskList;
	}
}
