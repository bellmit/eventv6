package cn.ffcs.zhsq.event.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.common.Constants;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.api.enums.BizPlatfromEnum;
import cn.ffcs.zhsq.decisionMaking.EventStatusDecisionMaking4JXPlatformServiceImpl;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * 江西省平台工作流处理类 
 * @author youwj
 *
 */
@Service(value = "eventDisposalWorkflowForJXPlatformService")
public class EventDisposalWorkflowForJXPlatformServiceImpl extends EventDisposalWorkflow4YCHSHServiceImpl {
	@Autowired
	private IEventDisposalService eventDisposalService;

	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;

	@Autowired
	private IEvent4WorkflowService event4WorkflowService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Resource(name = "eventStatusDecisionMaking4JXPlatformService")
	private EventStatusDecisionMaking4JXPlatformServiceImpl eventStatusDecisionMaking4JXPlatformService;
	
	private static final String OPERATE_TYPE_REJECT = "2";//驳回操作
	private static final String OPERATE_TYPE_RECALL = "5";//撤回操作
	private static final String TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE = "task7";	//乡镇街道综治中心办理
	private static final String TOWNDISPOSAl_UNIT_ACCEPT_NODE_CODE = "task6";	//乡镇街道综治中心受理
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		EventDisposal event = null;
		String instanceId = null;
		
		if(userInfo == null || userInfo.getUserId() == null || userInfo.getOrgId() == null) {
			throw new IllegalArgumentException("缺少有效的用户信息！");
		}
		
		if(eventId == null || eventId < 0) {
			if(CommonFunctions.isNotBlank(extraParam, "eventId")) {
				eventId = Long.valueOf(extraParam.get("eventId").toString());
			} else if(CommonFunctions.isNotBlank(extraParam, "formId")) {
				eventId = Long.valueOf(extraParam.get("formId").toString());
			}
		}
	
		if(eventId != null && eventId > 0) {
			event = eventDisposalService.findEventByIdSimple(eventId);
		} else {
			throw new IllegalArgumentException("缺少有效的事件id！");
		}
		
		if(event == null) {
			throw new IllegalArgumentException("事件id【" + eventId + "】未能找到有效的事件信息！");
		} else if(!BizPlatfromEnum.JX_PAZYZ.getValue().equals(event.getBizPlatform())) {
			Map<String, Object> eventCountParam = new HashMap<String, Object>();
			Date createDateTimeEnd = new Date(),
				 createDateTimeStart = new Date(createDateTimeEnd.getTime() - 3600 * 1000);
			
			eventCountParam.put("listType", 6);//我发起的列表
			eventCountParam.put("initiatorId", userInfo.getUserId());
			eventCountParam.put("initiatorOrgId", userInfo.getOrgId());
			eventCountParam.put("infoOrgCodeAccurate", event.getGridCode());
			eventCountParam.put("eventNameAccurate", event.getEventName());
			eventCountParam.put("eliminateEventIdList", eventId.toString());
			eventCountParam.put("createDateTimeStart", DateUtils.formatDate(createDateTimeStart, DateUtils.PATTERN_24TIME));
			eventCountParam.put("createDateTimeEnd", DateUtils.formatDate(createDateTimeEnd, DateUtils.PATTERN_24TIME));
			
			if(event4WorkflowService.findEventCount(eventCountParam) > 0) {
				throw new ZhsqEventException("事件标题重复！");
			}
		}
	
	    instanceId = super.startFlowByWorkFlow(eventId, workFlowId, toClose, userInfo, extraParam);
	
	    try {
			//自己采集提交的事件无需经过环节受理环节，应该直接处于环节的办理环节
			if(null != instanceId && Long.valueOf(instanceId) > 0){
				Long instanceIdL = Long.valueOf(instanceId);
			    Map<String,Object> curDataMap = curNodeData(instanceIdL);
			    String curCodeName = "";
			    String taskId = "";
	
			    if(CommonFunctions.isNotBlank(curDataMap,"NODE_NAME")){
			    	curCodeName = curDataMap.get("NODE_NAME").toString();
			    }
			    if(CommonFunctions.isNotBlank(curDataMap,"WF_DBID_")){
			        taskId = String.valueOf(curDataMap.get("WF_DBID_"));
			    }
			    
			    //获取下一环节
			    String nextNode = init4NodeMappingConstruct().get(curCodeName);
			    boolean subWorkFlowForEndingAndEvaluate = true;
			    
			    if(StringUtils.isNotBlank(nextNode)) {
			    	//自动受理
			    	subWorkFlowForEndingAndEvaluate = this.subWorkFlowForEndingAndEvaluate(instanceIdL,taskId,nextNode,userInfo.getUserId().toString(),userInfo.getOrgId().toString(),"采集环节自动受理。",userInfo,null,new HashMap<String,Object>());
			    }
			    
			    if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose) && subWorkFlowForEndingAndEvaluate) {//提交成功，如果是结案操作，则再进行结案操作
		    		Map<String,Object> handleDataMap = curNodeData(instanceIdL);
				    String END_NODE_CODE = "end1", advice="", handleTaskId = "";

				    if(CommonFunctions.isNotBlank(handleDataMap,"WF_DBID_")){
				    	handleTaskId = String.valueOf(handleDataMap.get("WF_DBID_"));
				    }
				    
				    if(CommonFunctions.isNotBlank(extraParam, "advice")) {
				    	advice = String.valueOf(extraParam.get("advice"));
				    }
				    
				    subWorkFlowForEndingAndEvaluate(instanceIdL, handleTaskId, END_NODE_CODE, null, null, advice, userInfo, null, extraParam);
		    	}
	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    return instanceId;
	}

	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{
		boolean flag=false, isUseParent = false;
		//如果当前环节是处理办理环节，并且下一个环节不是归档
		//那么此时会流转到某个部门手上办理，改为调用工作流底层接口
		String curCodeName="";
	    
		if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
			curCodeName = extraParam.get("curNodeName").toString();
		} else if(instanceId != null && instanceId > 0) {
			Map<String,Object> curDataMap = eventDisposalWorkflowService.curNodeData(instanceId);
			
		    if(CommonFunctions.isNotBlank(curDataMap,"NODE_NAME")) {
		    	curCodeName = curDataMap.get("NODE_NAME").toString();
		    }
		}
		
		if(CommonFunctions.isNotBlank(extraParam, "isUseParent")) {
			isUseParent = Boolean.valueOf(extraParam.get("isUseParent").toString());
		} else {
			String userConstructType = capUserConstructType(curCodeName, nextNodeName),
				   CONSTRUCT_TYPE_ORG = "2";
			
			isUseParent = !"end1".equals(nextNodeName) && !ConstantValue.HANDLING_TASK_CODE.equals(nextNodeName) && CONSTRUCT_TYPE_ORG.equals(userConstructType);
		}
		
		if(isUseParent) {
			List<UserInfo> nextUserInfoList=new ArrayList<UserInfo>();
			UserInfo user=new UserInfo();
			user.setUserId(Long.valueOf(nextStaffId));
			user.setOrgId(Long.valueOf(nextStaffId));
			nextUserInfoList.add(user);
			
			Map<String,Object> otherParams=new HashMap<String,Object>();
			otherParams.put("userType", "1");
			otherParams.put("currentTaskId", taskId);
			otherParams.put("advice", advice);
			
			flag = baseWorkflowService.subWorkflow4Base(instanceId, nextNodeName, nextUserInfoList, userInfo, otherParams);
			
			if(flag) {
				Long eventId = null;
				Map<String,Object> backParams = new HashMap<String,Object>();
				Map<String, Object> curNodeMap = curNodeData(instanceId);
				Date taskDueDate = null;
				LinkedHashMap<String, Date> dueDate = new LinkedHashMap<String, Date>();
				
				if(CommonFunctions.isNotBlank(curNodeMap, "DUEDATE_")) {
					taskDueDate = (Date) curNodeMap.get("DUEDATE_");
				}
				
				try {
					if(CommonFunctions.isNotBlank(extraParam, "eventId")) {
						eventId = Long.valueOf(extraParam.get("eventId").toString());
					} else if(CommonFunctions.isNotBlank(extraParam, "formId")) {
						eventId = Long.valueOf(extraParam.get("formId").toString());
					} else if(instanceId != null && instanceId > 0) {
						ProInstance pro = this.capProInstanceByInstanceId(instanceId);
						
						if(pro != null) {
							eventId = pro.getFormId();
						}
					}
					
					dueDate.put(curCodeName + "_" + nextNodeName, taskDueDate);
					
					backParams.put("duedate", dueDate);
					
					updateEventStatus(eventId, userInfo.getUserId(), userInfo.getOrgId(), backParams);
				} catch(Exception e) {}
			}
		} else {
			flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		}
		
		return flag;
	}
	
	@Override
	public boolean rejectWorkFlow(Map<String, Object> params, UserInfo userInfo) throws Exception {
		ProInstance proInstance = null;
		boolean flag = false;
		Long instanceId = null, eventId = null;
		Map<String, Object> rejectParams = new HashMap<String, Object>();
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			instanceId = Long.valueOf(params.get("instanceId").toString());
			proInstance = capProInstanceByInstanceId(instanceId);
		} else if(CommonFunctions.isNotBlank(params, "eventId")) {
			eventId = Long.valueOf(params.get("eventId").toString());
			proInstance = capProInstanceByEventId(eventId);
		} else if(CommonFunctions.isNotBlank(params, "formId")) {
			eventId = Long.valueOf(params.get("formId").toString());
			proInstance = capProInstanceByEventId(eventId);
		}
		
		if(proInstance != null) {
			eventId = proInstance.getFormId();
			instanceId = proInstance.getInstanceId();
		} else {
			throw new IllegalArgumentException("未能找到有效的流程实例信息！");
		}
		
		rejectParams.putAll(params);
		rejectParams.put("instanceId", instanceId);
		
		flag = baseWorkflowService.rejectWorkflow4Base(null, null, eventId, null, userInfo, rejectParams);
		
		if(flag) {
			String curNodeName = null,
				   userConstructType = null,
				   CONSTRUCT_TYPE_ORG = "2";
			
			proInstance = capProInstanceByInstanceId(instanceId);
			curNodeName = proInstance.getCurNode();
			userConstructType = capUserConstructType(curNodeName, null);
			
			if(CONSTRUCT_TYPE_ORG.equals(userConstructType)) {
				rejectParams = new HashMap<String, Object>();
				
				rejectParams.putAll(params);
				rejectParams.put("instanceId", instanceId);
				rejectParams.put("isCheckCurrentUser", false);
				
				flag = baseWorkflowService.rejectWorkflow4Base(null, null, eventId, null, userInfo, rejectParams);
			}
		}
		
		if(flag) {
			List<Map<String, Object>> taskMapList = baseWorkflowService.capHandledTaskInfoMap(instanceId, "DESC", null);
			String curNodeName = null, nextNodeName = null,
				   COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE = "task22";//八员队伍办理
			Date taskDueDate = null;
			
			if(taskMapList != null) {
				String operateType = null, taskIdStr = null, parentTaskIdStr = null;
				Map<String, Object> curNodeMap = curNodeData(instanceId);
				
				if(CommonFunctions.isNotBlank(curNodeMap, "NODE_NAME")) {
					curNodeName = curNodeMap.get("NODE_NAME").toString();
				}
				if(CommonFunctions.isNotBlank(curNodeMap, "DUEDATE_")) {
					taskDueDate = (Date) curNodeMap.get("DUEDATE_");
				}
				
				for(Map<String, Object> taskMap : taskMapList) {
					operateType = null;
					taskIdStr = null;
					
					if(CommonFunctions.isNotBlank(taskMap, "OPERATE_TYPE")) {
						operateType = taskMap.get("OPERATE_TYPE").toString();
					}
					if(CommonFunctions.isNotBlank(taskMap, "TASK_ID")) {
						taskIdStr = taskMap.get("TASK_ID").toString();
					}
					
					if(parentTaskIdStr == null && CommonFunctions.isNotBlank(taskMap, "PRE_TASK_ID")) {
						parentTaskIdStr = taskMap.get("PRE_TASK_ID").toString();
					}
					
					if(parentTaskIdStr.equals(taskIdStr)) {
						if(OPERATE_TYPE_REJECT.equals(operateType) || OPERATE_TYPE_RECALL.equals(operateType)) {
							if(CommonFunctions.isNotBlank(taskMap, "PRE_TASK_ID")) {
								parentTaskIdStr = taskMap.get("PRE_TASK_ID").toString();
							}
						} else {
							if(COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE.equals(curNodeName)) {
								if(CommonFunctions.isNotBlank(taskMap, "TASK_CODE")) {
									nextNodeName = taskMap.get("TASK_CODE").toString();
								}
							} else if(CommonFunctions.isNotBlank(taskMap, "PRE_TASK_CODE")) {
								nextNodeName = taskMap.get("PRE_TASK_CODE").toString();
							}
							break;
						}
					}
				}
			}
			
			if(StringUtils.isNotBlank(curNodeName) && StringUtils.isNotBlank(nextNodeName)) {
				Map<String, Object> decisionParams = new HashMap<String, Object>();
				LinkedHashMap<String, Date> dueDate = new LinkedHashMap<String, Date>();
				Map<String, String> nodeMapping = init4NodeMappingConstruct();
				
				if(nodeMapping.containsValue(curNodeName)) {
					if(nodeMapping.containsValue(curNodeName)) {
						for(String acceptNodeName : nodeMapping.keySet()) {
							if(curNodeName.equals(nodeMapping.get(acceptNodeName))) {
								curNodeName = acceptNodeName;
								break;
							}
						}
					}
				}
				
				dueDate.put(nextNodeName + "_" + curNodeName, taskDueDate);
				
				decisionParams.put("duedate", dueDate);
				
				updateEventStatus(eventId, userInfo.getUserId(), userInfo.getOrgId(), decisionParams);
			}
		}
		
		return flag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		Map<String, Object> initResultMap = super.initFlowInfo(taskId, instanceId, userInfo, params);
		Long eventId = null;
		String curNodeName = null;
		
		if(CommonFunctions.isNotBlank(initResultMap, "proInstance")) {
			ProInstance proInstance = (ProInstance) initResultMap.get("proInstance");
			eventId = proInstance.getFormId();
		}
		
		if(CommonFunctions.isNotBlank(initResultMap, "curNodeName")) {
			curNodeName = initResultMap.get("curNodeName").toString();
		}
		
		if(eventId != null && eventId > 0 && CommonFunctions.isNotBlank(initResultMap, "operateLists")) {
			List<OperateBean> operateLists = (List<OperateBean>) initResultMap.get("operateLists");
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			OperateBean rejectOperate = null;
			String REJECT_OPERATE_EVENT = "reject";
			
			if(event != null) {
				String status = event.getStatus();
				if(ConstantValue.EVENT_STATUS_RECEIVED.equals(status) 
						|| (
								BizPlatfromEnum.JX_PAZYZ.getValue().equals(event.getBizPlatform())
								&& (
										TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE.equals(curNodeName)
										|| TOWNDISPOSAl_UNIT_ACCEPT_NODE_CODE.equals(curNodeName)
									)
							)
				) {//平安志愿者事件的乡镇街道处理环节不能驳回
					for (OperateBean operateBean : operateLists) {
						if(REJECT_OPERATE_EVENT.equals(operateBean.getOperateEvent())) {
							rejectOperate = operateBean;
							break;
						}
					}
				}
				
				initResultMap.put("event4Simple", event);
			}
			
			if(rejectOperate != null) {
				operateLists.remove(rejectOperate);
				
				initResultMap.put("operateLists", operateLists);
			}
		}
		
		//判断当前环节是否是受理环节
		if(init4NodeMappingConstruct().containsKey(curNodeName)) {
			initResultMap.put("acceptNode", "true");
		}
		
		//如果当前环节是乡镇综治中心受理
		if(TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE.equals(curNodeName)) {
			initResultMap.put("showSimilarList", "true");
		}
		
		return initResultMap;
	}
	
	@Override
	public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {
		int result = -1;
		StringBuffer msgWrong = new StringBuffer("");
		String orgLevel="";
		
		if(orgSocialInfo != null) {
			orgLevel = orgSocialInfo.getChiefLevel();
			
			if(StringUtils.isBlank(orgLevel)) {
				msgWrong.append("当前组织层级不存在，请先检验！");
			}
			
		} else {
			msgWrong.append("当前组织不存在，请先检验！");
		}
		
		if(msgWrong.length() > 0) {
			throw new ZhsqEventException(msgWrong.toString());
		} else {
			result = 1;
		}
		
		return result;
	}
	
	@Override
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int sqlOrder, UserInfo userInfo){
		List<Map<String, Object>> taskList = super.queryProInstanceDetail(instanceId, sqlOrder, userInfo);
	    Long eventId = -1L;
	    ProInstance pro = capProInstanceByInstanceId(instanceId);
	    String eventBizplatform = null;
	    EventDisposal event = null;
	    
	    if(pro!=null) {
	    	eventId = pro.getFormId();
	    }
	    
	    if(eventId != null && eventId > 0) {
	    	event = eventDisposalService.findEventByIdSimple(eventId);
	    }
	    
	    if(event != null) {
	    	eventBizplatform = event.getBizPlatform();
	    }
	    
	    //如果是平安志愿者的事件那么流程中采集环节后两步要抹除
	    List<Map<String, Object>> result=new ArrayList<Map<String,Object>>();
	    if(BizPlatfromEnum.JX_PAZYZ.getValue().equals(eventBizplatform)) {
	    	
	    	String order = "";
	    	
	    	switch(sqlOrder){
	    	case IEventDisposalWorkflowService.SQL_ORDER_ASC:{
	    		order = Constants.SQL_ORDER_ASC;break;
	    	}
	    	case IEventDisposalWorkflowService.SQL_ORDER_DESC:{
	    		order = Constants.SQL_ORDER_DESC;break;
	    	}
	    	default:{
	    		order = Constants.SQL_ORDER_ASC;break;
	    	}
	    	}
	    	
	    	Integer flag=-1;
	    	if(order.equals(Constants.SQL_ORDER_ASC)) {
	    		//正序就按顺序遍历
	    		for (int i=0,j=taskList.size();i<j;i++) {
	    			
	    			if(flag<0||flag==2) {
	    				result.add(taskList.get(i));
	    			}
	    			
	    			if("task1".equals(taskList.get(i).get("TASK_CODE").toString())) {
	    				flag=0;
	    			}else if(flag==0||flag==1) {
	    				flag+=1;
	    			}
	    			
	    		}
	    	}else if(order.equals(Constants.SQL_ORDER_DESC)) {
	    		
	    		for (int i=0,j=taskList.size();i<j;i++) {
	    			
	    			if("task1".equals(taskList.get(i).get("TASK_CODE").toString())) {
	    				flag=i;
	    			}
	    		}
	    		
	    		
	    		for (int i=0,j=taskList.size();i<j;i++) {
	    			
	    			if(!(i==(flag-1)||i==(flag-2))) {
	    				result.add(taskList.get(i));
	    			}
	    		}
	    		
	    	}
	    }else {
	    	result=taskList;
	    }
	    
		return result;
	}
	
	@Override
	public Map<String, Object> findRmqInitInfo(Map<String, Object> params,String orgCode) {
		Long instanceId = -1L;
		Long eventId = -1L;
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if(instanceId < 0 && CommonFunctions.isNotBlank(params, "eventId")) {
			try {
				eventId = Long.valueOf(params.get("eventId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
			if(eventId > 0) {
				instanceId = this.capInstanceIdByEventIdForLong(eventId);
			}
		}
		
		List<Map<String, Object>> hisflow=new ArrayList<Map<String, Object>>();
		try {
			hisflow = eventDisposalWorkflowService.queryProInstanceDetail(instanceId,
			            IEventDisposalWorkflowService.SQL_ORDER_ASC);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("historyFlow", hisflow);
		
		return result;
	}
	
	/**
	 * 节点映射关系构建
	 * @return
	 */
	protected Map<String, String> init4NodeMappingConstruct() {
		Map<String, String> nodeMapping = new HashMap<String, String>();
		
		nodeMapping.put("task2", "task3");
		nodeMapping.put("task4", "task5");
		nodeMapping.put("task6", "task7");
		nodeMapping.put("task8", "task9");
		nodeMapping.put("task10", "task11");
		nodeMapping.put("task12", "task13");
		nodeMapping.put("task14", "task15");
		nodeMapping.put("task16", "task17");
		nodeMapping.put("task18", "task19");
		nodeMapping.put("task20", "task21");
		
		return nodeMapping;
	}
	
	/**
	 * 构造节点人员选择类型
	 * @return
	 * 		1 选择人员；
	 * 		2 选择组织；
	 */
	protected Map<String, String> init4TaskUserConstruct() {
		Map<String, String> initMap = new HashMap<String, String>();
		
		initMap.put("task2", "2");
		initMap.put("task4", "2");
		initMap.put("task6", "2");
		initMap.put("task8", "2");
		initMap.put("task10", "2");
		initMap.put("task12", "2");
		initMap.put("task14", "2");
		initMap.put("task16", "2");
		initMap.put("task18", "2");
		initMap.put("task20", "2");
		
		initMap.put("task3", "1");
		initMap.put("task5", "1");
		initMap.put("task7", "1");
		initMap.put("task9", "1");
		initMap.put("task11", "1");
		initMap.put("task13", "1");
		initMap.put("task15", "1");
		initMap.put("task17", "1");
		initMap.put("task19", "1");
		initMap.put("task21", "1");
		
		return initMap;
	}
	
	/**
	 * 获取节点人员选择类型
	 * 
	 * 优先获取下一节点对应的人员选择类型
	 * @param curNodeName	当前环节节点名称
	 * @param nextNodeName	下一环节节点名称
	 * @return
	 * 		1 选择人员；
	 * 		2 选择组织；
	 */
	private String capUserConstructType(String curNodeName, String nextNodeName) {
		Map<String, String> initMap = init4TaskUserConstruct();
		String userConstructType = null;
		
		if(CommonFunctions.isNotBlank(initMap, curNodeName) && CommonFunctions.isNotBlank(initMap, nextNodeName)) {
			userConstructType = initMap.get(nextNodeName);
		} else if(CommonFunctions.isNotBlank(initMap, curNodeName)) {
			userConstructType = initMap.get(curNodeName);
		} else {
			String nodeKey = curNodeName + "-" + nextNodeName;
			
			if(CommonFunctions.isNotBlank(initMap, nodeKey)) {
				userConstructType = initMap.get(nodeKey);
			}
		}
		
		return userConstructType;
	}

}
