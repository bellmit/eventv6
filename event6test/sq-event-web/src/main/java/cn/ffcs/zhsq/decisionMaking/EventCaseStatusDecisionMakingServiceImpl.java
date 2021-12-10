package cn.ffcs.zhsq.decisionMaking;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventCase.service.IEventCaseService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 案件 状态变更决策类
 * 必填参数
 * 		caseId			案件id
 * 		curNodeName		当前环节名称
 * 		nextNodeName	下一环节名称
 * 非必填参数
 * 		updaterId		更新操作用户id
 * 		updaterOrgId	更新操作组织id
 * @author zhangls
 *
 */
@Service(value = "eventCaseStatusDecisionMakingService")
public class EventCaseStatusDecisionMakingServiceImpl extends ApplicationObjectSupport
		implements IWorkflowDecisionMakingService<Boolean> {
	@Autowired
	private IEventCaseService eventCaseService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	private final String EVA_NODE_NAME = "task4";//案件评价
	protected static final String REJECT_NODE_CODE = "reject";	//虚拟的驳回环节节点，流程图中不存在
	
	@Override
	public Boolean makeDecision(Map<String, Object> params) throws Exception {
		String curNodeName = null, nextNodeName = null;
		Long caseId = -1L, updaterId = -1L, updaterOrgId = -1L;
		StringBuffer msgWrong = new StringBuffer("");
		Boolean result = false;
		
		if(CommonFunctions.isNotBlank(params, "caseId")) {
			caseId = Long.valueOf(params.get("caseId").toString());
		} else {
			msgWrong.append("缺少参数[caseId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curNodeName")) {
			curNodeName = params.get("curNodeName").toString();
		} else {
			msgWrong.append("缺少参数[curNodeName]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "nextNodeName")) {
			nextNodeName = params.get("nextNodeName").toString();
		} else {
			msgWrong.append("缺少参数[nextNodeName]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "updaterId")) {
			try {
				updaterId = Long.valueOf(params.get("updaterId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "updaterOrgId")) {
			try {
				updaterOrgId = Long.valueOf(params.get("updaterOrgId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if(msgWrong.length() > 0) {
			throw new Exception(msgWrong.toString());
		} else {
			String status = null,
				   endTimeStr = null;
			UserInfo userInfo = new UserInfo();
			
			userInfo.setUserId(updaterId);
			userInfo.setOrgId(updaterOrgId);
			
			if(REJECT_NODE_CODE.equals(nextNodeName)) {
				
				Map<String, Object> curDataMap = null;	//当前环节信息
				String preNodeCode = null;		//驳回操作前，上一环节的环节编码
				Long instanceId = -1L;
				
				endTimeStr = "";//驳回时，清空结案时间
				
				//当前环节信息
				curDataMap = eventCaseService.findCurTaskData(caseId, userInfo);
				
				if(CommonFunctions.isNotBlank(curDataMap, "NODE_NAME")) {//获取当前环节编码
					curNodeName = curDataMap.get("NODE_NAME").toString();
				}
				if(CommonFunctions.isNotBlank(curDataMap, "INSTANCEID")) {
					try {
						instanceId = Long.valueOf(curDataMap.get("INSTANCEID").toString());
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
				}
				
				//获取驳回前的环节编码
				if(CommonFunctions.isNotBlank(params, "preNodeCode")) {
					preNodeCode = params.get("preNodeCode").toString();
				} else if(instanceId != null && instanceId > 0) {
					List<Map<String, Object>> taskMapList = eventDisposalWorkflowService.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
					
					if(taskMapList != null && taskMapList.size() > 1) {
						taskMapList.remove(0);//移除当前环节的任务信息
						
						Map<String, Object> taskMap = taskMapList.get(0);
						
						if(CommonFunctions.isNotBlank(taskMap, "PRE_TASK_ID")) {
							Object pretaskIdObj = taskMap.get("PRE_TASK_ID");
							
							for(Map<String, Object> taskMapTmp : taskMapList) {
								if(CommonFunctions.isNotBlank(taskMapTmp, "TASK_ID") && pretaskIdObj.equals(taskMapTmp.get("TASK_ID"))) {
									if(CommonFunctions.isNotBlank(taskMapTmp, "TASK_CODE")) {
										preNodeCode = taskMapTmp.get("TASK_CODE").toString();
									}
									
									break;
								}
							}
						}
					}
				}
				
				status = capCaseStatus(preNodeCode, curNodeName);
			} else {
				status = capCaseStatus(curNodeName, nextNodeName);
			}
			
			if(StringUtils.isNotBlank(status) && caseId > 0) {
				Map<String, Object> eventCase = new HashMap<String, Object>();
				
				eventCase.put("caseId", caseId);
				eventCase.put("updaterId", updaterId);
				eventCase.put("status", status);
				if(endTimeStr != null) {
					eventCase.put("endTimeStr", endTimeStr);
				}

				if(EVA_NODE_NAME.equals(nextNodeName)) {
					eventCase.put("endTime", new Date());
				}

				result = eventCaseService.updateEventCase(eventCase, userInfo);
			}
		}
		
		return result;
	}
	
	/**
	 * 获取变更后的状态
	 * @param curNodeName	当前节点名称
	 * @param nextNodeName	下一节点名称
	 * @return
	 */
	private String capCaseStatus(String curNodeName, String nextNodeName) {
		String status = null;
		Map<String, String> statusMap = initStatusMap();
		
		status = statusMap.get(curNodeName + "-" + nextNodeName);
		
		if(StringUtils.isBlank(status)) {
			status = statusMap.get(curNodeName);
		}
		
		if(StringUtils.isBlank(status)) {
			status = statusMap.get(nextNodeName);
		}
		
		return status;
	}
	
	/**
	 * 初始化状态
	 * @return
	 */
	private Map<String, String> initStatusMap() {
		String START_NODE = "start",				//流程开始
			   COLLECT_NODE = "task1",				//案件采集
			   HANDLE_DEPARTMENT_NODE = "task2",	//执法部门处理
			   CASE_CENTER_NODE = "task3",			//案件中心处理
			   END_NODE = "end1";					//归档
		Map<String, String> statusMap = new HashMap<String, String>();
		
		statusMap.put(START_NODE, ConstantValue.EVENT_STATUS_RECEIVED);
		statusMap.put(COLLECT_NODE, ConstantValue.EVENT_STATUS_RECEIVED);
		statusMap.put(HANDLE_DEPARTMENT_NODE+"-"+CASE_CENTER_NODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(CASE_CENTER_NODE+"-"+HANDLE_DEPARTMENT_NODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(EVA_NODE_NAME, ConstantValue.EVENT_STATUS_ARCHIVE);
		statusMap.put(EVA_NODE_NAME+"-"+END_NODE, ConstantValue.EVENT_STATUS_END);
		statusMap.put(END_NODE, ConstantValue.EVENT_STATUS_END);
		
		return statusMap;
	}

}
