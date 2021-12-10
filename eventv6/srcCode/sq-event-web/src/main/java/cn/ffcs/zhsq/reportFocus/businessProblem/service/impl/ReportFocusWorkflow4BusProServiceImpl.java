package cn.ffcs.zhsq.reportFocus.businessProblem.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.common.utils.StringUtils;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.workflow.spring.WorkflowHolidayInfoService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.businessProblem.ReportBusProMapper;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportFocusWorkflow4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 营商问题工作流相关接口
 * @ClassName:   ReportFocusWorkflow4BusProServiceImpl   
 * @author:      郭昱丹(guoyd)
 * @date:        2021年5月7日 上午10:16:15
 */
@Service(value = "reportFocusWorkflow4BusProService")
public class ReportFocusWorkflow4BusProServiceImpl extends ReportFocusWorkflow4TwoVioPreServiceImpl {

	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	@Resource(name="eventDisposalWorkflowForEventService")
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	@Autowired
	private IReportFocusService reportFocusService;
	@Autowired
	private WorkflowHolidayInfoService workflowHolidayInfoService;
	@Autowired
	private ReportBusProMapper reportBusProMapper;
//	@Autowired
//	private UserManageOutService userManageService;
//	@Autowired
//	private ReportMsgCCSetMapper reportMsgCCSetMapper;
//	@Autowired
//	private IReportMsgCCSetService reportMsgCCSetService;
//	@Autowired
//	private MessageOutService messageService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> initWorkflow4Handle(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		Map<String, Object> initMap = super.initWorkflow4Handle(instanceId, userInfo, params);
		boolean isAble2Handle = false;
		
		if(CommonFunctions.isNotBlank(initMap, "isAble2Handle")) {
			isAble2Handle = Boolean.valueOf(initMap.get("isAble2Handle").toString());
		}
		
		if(isAble2Handle) {
			String formTypeIdStr = null, curNodeName = null;
			boolean isEditable = false, isEditableNode = false;
			
			if(CommonFunctions.isNotBlank(initMap, "isEditable")) {
				isEditable = Boolean.valueOf(initMap.get("isEditable").toString());
			}
			
			if(CommonFunctions.isNotBlank(initMap, "formTypeId")) {
				formTypeIdStr = initMap.get("formTypeId").toString();
			}
			
			if(CommonFunctions.isNotBlank(initMap, "curNodeName")) {
				curNodeName = initMap.get("curNodeName").toString();
			}
			
			if(CommonFunctions.isNotBlank(params, "reportFocusMap")) {
				Map<String, Object> reportFocusMap = new HashMap<String, Object>();
				reportFocusMap = (Map<String, Object>)params.get("reportFocusMap");
//				isProblem = StringUtils.toString(reportFocusMap.get("isProblem"));
			}
			
			if(userInfo!=null) {
				initMap.put("orgScopeName",userInfo.getOrgName());
			}
			
			if(CommonFunctions.isNotBlank(initMap, "nextTaskNodes")) {
				List<Map<String, Object>> nextTaskNodes = new ArrayList<Map<String, Object>>();
				nextTaskNodes = (List<Map<String, Object>>)initMap.get("nextTaskNodes");
				
				if(FocusReportNode359Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
						&&FocusReportNode359Enum.LEAD_NODE_NAME.getTaskNodeName().equals(curNodeName)){
					
					Map<String, Object> node = new HashMap<String, Object>();
					node.put("nodeName", FocusReportNode359Enum.DOING_NODE_NAME.getTaskNodeName());
					node.put("nodeNameZH", FocusReportNode359Enum.DOING_NODE_NAME.getTaskNodeNameZH());
					node.put("nodeCode", "D4");
					node.put("transitionCode", "D3S1D4");
					node.put("nodeType", "1");
					nextTaskNodes.add(node);
					
					if(nextTaskNodes.size() > 0){
						initMap.put("nextTaskNodes",nextTaskNodes);
						//只有当下一可办理环节为一个时，默认选中
						initMap.put("isChooseDefaultRadio", nextTaskNodes != null && nextTaskNodes.size() == 1);
					}
				}
			}
			
//			if(FocusReportNode359Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
//					&& (FocusReportNode359Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName().equals(curNodeName)
//					||FocusReportNode359Enum.LEAD_NODE_NAME.getTaskNodeName().equals(curNodeName)
//					||FocusReportNode359Enum.MUNICIPAL_NODE_NAME.getTaskNodeName().equals(curNodeName)
//					||FocusReportNode359Enum.DELAY_NODE_NAME.getTaskNodeName().equals(curNodeName)
//					||("1".equals(isProblem)&&FocusReportNode359Enum.OFFICE_NODE_NAME.getTaskNodeName().equals(curNodeName)))
//					) {
//				isEditable = true;
//			}
			
			
			initMap.put("isEditable", isEditable);
			initMap.put("isEditableNode", isEditableNode);
			
		}
		
		return initMap;
	}
	
	@SuppressWarnings("unused")
	@Override
	public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		String curNodeName = null, formTypeIdStr = null, curTaskId = null,formId = null,advice = null;
		 
		if(instanceId != null && instanceId > 0) {
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			if(proInstance != null) {
				curNodeName = proInstance.getCurNode();
				formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
				formId = String.valueOf(proInstance.getFormId());
			}
			Map<String, Object> curDataMap = baseWorkflowService.findCurTaskData(instanceId);
			if(CommonFunctions.isNotBlank(curDataMap, "WF_DBID_")) {
				curTaskId = curDataMap.get("WF_DBID_").toString();
			}
			if(CommonFunctions.isNotBlank(extraParam, "advice")) {
				advice = String.valueOf(extraParam.get("advice"));
			}
		}
		
		if(FocusReportNode359Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
			&& FocusReportNode359Enum.DOING_NODE_NAME.getTaskNodeName().equals(nextNodeName)
				&& FocusReportNode359Enum.LEAD_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
			
			Map<String, Object> resultMap = eventDisposalWorkflowService.saveHandlingTask(Long.parseLong(formId), instanceId, FocusReportNode359Enum.DOING_NODE_NAME.getTaskNodeName(), advice, userInfo,null);
			//主送人员
//			String MAIN_MSG_MODULE_CODE = "250101";
//			List<UserInfo> userInfoMainList = convert2UserInfo("nextUserIds", "nextOrgIds", extraParam);
//			
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("taskId", curTaskId);
//			params.put("ccType", ReportMsgCCTypeEnum.CCTYPE_MAIN.getCcType());
//			List<Map<String, Object>> reportMsgCCList = reportMsgCCSetMapper.findMsgCCSetByParams(params);
//			if(!reportMsgCCList.isEmpty()) {
////				Long newCurTaskId = System.currentTimeMillis();
//				Long newCurTaskId = instanceId;
//				saveMsgCCSet(newCurTaskId, ReportMsgCCTypeEnum.CCTYPE_MAIN.getCcType(), userInfoMainList);
//				sendMsg(MAIN_MSG_MODULE_CODE, newCurTaskId, advice, userInfo, userInfoMainList);
//			}
			
			flag = true;
		}else {
			if(FocusReportNode359Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
				&& FocusReportNode359Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				//更新所属区域
				String regionCode_ = "";
				if(CommonFunctions.isNotBlank(extraParam, "regionCode_")) {
					regionCode_ = StringUtils.toString(extraParam.get("regionCode_"));
				}
				if(!StringUtils.isBlank(regionCode_)&&!StringUtils.isBlank(formId)) {
					Map<String, Object> reportFocus = new HashMap<String, Object>();
					String reportType = null;

					if(CommonFunctions.isNotBlank(extraParam, "reportType")) {
						reportType = extraParam.get("reportType").toString();
					} else {
						reportType = ReportTypeEnum.businessProblem.getReportTypeIndex();
					}

					reportFocus.put("regionCode", regionCode_);
					reportFocus.put("reportType", reportType);
					reportFocus.put("reportId", formId);

					reportFocusService.saveReportFocus(reportFocus, userInfo);
				}
			}
			flag = super.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);
			
			String isDelay = "";
			String reportId = "";
			if(CommonFunctions.isNotBlank(extraParam, "_isDelay")) {
				isDelay = StringUtils.toString(extraParam.get("_isDelay"));
			}
			if(CommonFunctions.isNotBlank(extraParam, "isDelay")) {
				isDelay = StringUtils.toString(extraParam.get("isDelay"));
			}
			if(CommonFunctions.isNotBlank(extraParam, "formId")) {
				reportId = StringUtils.toString(extraParam.get("formId"));
			}
			
			if("2".equals(isDelay)&&FocusReportNode359Enum.DELAY_NODE_NAME.getTaskNodeName().equals(curNodeName)
					&& FocusReportNode359Enum.LEAD_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
				
				Map<String, Object> reportParams = new HashMap<String, Object>(),reportFocusMap = null;
				reportParams.put("reportId", reportId);
				reportParams.put("reportType", ReportTypeEnum.businessProblem.getReportTypeIndex());
				reportFocusMap = reportFocusService.findReportFocusByUUID(null, userInfo, reportParams);
				if(CommonFunctions.isNotBlank(reportFocusMap, "task3ReceiveTime")) {
					Date task3ReceiveTime = (Date) reportFocusMap.get("task3ReceiveTime");
					if(task3ReceiveTime!=null) {
						Date newDate = workflowHolidayInfoService.getWorkDateByInterval(task3ReceiveTime, 10);
						String curTaskId_ = "";
						Map<String, Object> curDataMap = baseWorkflowService.findCurTaskData(instanceId);
						if(CommonFunctions.isNotBlank(curDataMap, "WF_DBID_")) {
							curTaskId_ = curDataMap.get("WF_DBID_").toString();
							reportParams.put("newDate", newDate);
							reportParams.put("taskId", curTaskId_);
							reportBusProMapper.updateTaskTime(reportParams);
						}
					}
				}
			}
		}

		return flag;
	}
	
	/**
	 * 转换为用户信息
	 * @param userIdKey	用户id的key值
	 * @param orgIdKey	组织id的key值
	 * @param params	
	 * @return
	 */
//	private List<UserInfo> convert2UserInfo(String userIdKey, String orgIdKey, Map<String, Object> params) {
//		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
//		
//		if(CommonFunctions.isNotBlank(params, userIdKey) 
//				&& CommonFunctions.isNotBlank(params, orgIdKey)) {
//			Long userId = null, orgId = null;
//			String userIdStr = null, orgIdStr = null;
//			String[] distributeUserIdArray = params.get(userIdKey).toString().split(",");
//			String[] distributeOrgIdArray = params.get(orgIdKey).toString().split(",");
//			UserBO userBO = null;
//			UserInfo userInfo = null;
//			
//			for(int index = 0, userIdLen = distributeUserIdArray.length, orgIdLen = distributeOrgIdArray.length;
//					index < userIdLen && index < orgIdLen; index++) {
//				userId = null;
//				orgId = null;
//				userIdStr = distributeUserIdArray[index];
//				orgIdStr = distributeOrgIdArray[index];
//				
//				try {
//					userId = Long.valueOf(userIdStr);
//				} catch(NumberFormatException e) {}
//				
//				try {
//					orgId = Long.valueOf(orgIdStr);
//				} catch(NumberFormatException e) {}
//				
//				if(userId != null && userId > 0) {
//					userBO = userManageService.getUserInfoByUserId(userId);
//				}
//				
//				if(userBO != null && userBO.getUserId() != null) {
//					userInfo = new UserInfo();
//					
//					userInfo.setUserId(userBO.getUserId());
//					userInfo.setPartyName(userBO.getPartyName());
//					userInfo.setVerifyMobile(String.valueOf(userBO.getVerifyMobile()));
//					
//					if(orgId != null && orgId > 0) {
//						userInfo.setOrgId(orgId);
//					}
//					
//					userInfoList.add(userInfo);
//				}
//			}
//		}
//		
//		return userInfoList;
//	}
	
	@Override
	public boolean recallWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		extraParam.put("isAble2Recall", true);
		
		flag = super.recallWorkflow4Report(instanceId, userInfo, extraParam);
		
		return flag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> resultMapList = super.capHandledTaskInfoMap(instanceId, order, params);
		
		if(resultMapList != null && resultMapList.size() > 0 
				&& IWorkflow4BaseService.SQL_ORDER_DESC.equals(order)) {
			Map<String, Object> taskMap = resultMapList.get(0);
			
			if(CommonFunctions.isBlank(taskMap, "IS_CURRENT_TASK")) {
				String taskNameZH = null;
				
				if(CommonFunctions.isNotBlank(taskMap, "TASK_NAME")) {
					taskNameZH = taskMap.get("TASK_NAME").toString();
				}
				
				if(!FocusReportNode359Enum.END_NODE_NAME.getTaskNodeNameZH().equals(taskNameZH)) {
					Map<String, Object> endTaskMap = new HashMap<String, Object>();
					
					endTaskMap.put("TASK_NAME", FocusReportNode359Enum.END_NODE_NAME.getTaskNodeNameZH());
					
					resultMapList.add(0, endTaskMap);
				}
			}
			
			for(Map<String, Object> resultMap : resultMapList) {
				String TASK_CODE = null;
				if(CommonFunctions.isNotBlank(resultMap, "TASK_CODE")) {
					TASK_CODE = resultMap.get("TASK_CODE").toString();
				}
				String TASK_ID = null;
				if(CommonFunctions.isNotBlank(resultMap, "TASK_ID")) {
					TASK_ID = resultMap.get("TASK_ID").toString();
				}
				if(FocusReportNode359Enum.LEAD_NODE_NAME.getTaskNodeName().equals(TASK_CODE)) {
					List<Map<String, Object>> subAndReceivedTaskListTmp = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> timeAndRemarkList = null;
					List<Map<String, Object>> subAndReceivedTaskList = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> subTaskList = eventDisposalWorkflowService.querySubTaskDetails(TASK_ID, IEventDisposalWorkflowService.SQL_ORDER_DESC, null);
					if(subTaskList != null && subTaskList.size() > 0) {
						for(Map<String, Object> subTask : subTaskList) {
							if(CommonFunctions.isNotBlank(subTask, "TASK_NAME")) {//清除处理中的环节名称
								if(ConstantValue.HANDLING_TASK_CODE.equals(subTask.get("TASK_NAME"))) {
									subTask.remove("TASK_NAME");
								}
							}
							subAndReceivedTaskList.add(subTask);
						}
						resultMap.put("subTaskList", subTaskList);
					}
					if(subAndReceivedTaskList.size() > 0) {
						Map<String, Object> timeAndRemarkMap = null;
						boolean isSubAndReceivedBlank = true;
						
						for(Map<String, Object> subAndReceivedTaskMap : subAndReceivedTaskList) {
							timeAndRemarkList = new ArrayList<Map<String, Object>>();
							timeAndRemarkMap = new HashMap<String, Object>();
							isSubAndReceivedBlank = true;
							
							if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "START_TIME")) {
								timeAndRemarkMap.put("START_TIME", subAndReceivedTaskMap.get("START_TIME"));
							}
							if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "END_TIME")) {
								timeAndRemarkMap.put("END_TIME", subAndReceivedTaskMap.get("END_TIME"));
							}
							if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "RECEIVE_TIME")) {
								timeAndRemarkMap.put("RECEIVE_TIME", subAndReceivedTaskMap.get("RECEIVE_TIME"));
							}
							if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "REMARKS")) {
								timeAndRemarkMap.put("REMARKS", subAndReceivedTaskMap.get("REMARKS"));
							}
							
							for(Map<String, Object> subAndReceivedTaskTmpMap : subAndReceivedTaskListTmp) {
								//合并准则：1、存在TRANSACTOR_ID和ORG_ID，且二者均不为空时，二者同时相等者进行合并；
								if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "TRANSACTOR_ID") && CommonFunctions.isNotBlank(subAndReceivedTaskTmpMap, "TRANSACTOR_ID") && 
										subAndReceivedTaskMap.get("TRANSACTOR_ID").equals(subAndReceivedTaskTmpMap.get("TRANSACTOR_ID")) && 
										CommonFunctions.isNotBlank(subAndReceivedTaskMap, "ORG_ID") && CommonFunctions.isNotBlank(subAndReceivedTaskTmpMap, "ORG_ID") &&
										subAndReceivedTaskMap.get("ORG_ID").equals(subAndReceivedTaskTmpMap.get("ORG_ID"))) {
									if(CommonFunctions.isNotBlank(subAndReceivedTaskTmpMap, "timeAndRemarkList")) {
										timeAndRemarkList.addAll((List<Map<String, Object>>)subAndReceivedTaskTmpMap.get("timeAndRemarkList"));
									}
									
									if(!timeAndRemarkMap.isEmpty()) {
										timeAndRemarkList.add(timeAndRemarkMap);
										
										subAndReceivedTaskTmpMap.put("timeAndRemarkList", timeAndRemarkList);
									}
									
									isSubAndReceivedBlank = false;
									
									break;
								}
							}
							
							if(isSubAndReceivedBlank) {
								if(!timeAndRemarkMap.isEmpty()) {
									timeAndRemarkList.add(timeAndRemarkMap);
									
									subAndReceivedTaskMap.put("timeAndRemarkList", timeAndRemarkList);
								}
								
								subAndReceivedTaskListTmp.add(subAndReceivedTaskMap);
							}
						}
						
						resultMap.put("subAndReceivedTaskList", subAndReceivedTaskListTmp);
					}
				}
			}
		}
		
		return resultMapList;
	}
	
	/**
	 * 获取流程图名称
	 * @param reportId	上报id
	 * @param userInfo	操作用户
	 * @param params	额外参数
	 * @return
	 */
	protected String findWorkflowName(Long reportId, UserInfo userInfo, Map<String, Object> params) {
		String reportType = null, workflowName = null;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		
		//后续转功能配置
		if(ReportTypeEnum.businessProblem.getReportTypeIndex().equals(reportType)) {
			workflowName = "南安营商环境问题流程";
		}
		
		return workflowName;
	}
	
	/**
	 * 获取流程类型
	 * @param reportId	上报id
	 * @param userInfo	操作用户
	 * @param params	额外参数
	 * @return
	 */
	protected String findWfTypeId(Long reportId, UserInfo userInfo, Map<String, Object> params) {
		String reportType = null, wfTypeId = null;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		
		//后续转功能配置
		if(ReportTypeEnum.businessProblem.getReportTypeIndex().equals(reportType)) {
			wfTypeId = "focus_report";
		}
		
		return wfTypeId;
	}
	
	/**
	 * 获取下一可办理环节
	 * @param instanceId	流程实例id
	 * @param userInfo		用户信息
	 * @param params		额外参数
	 * 			instanceId	流程实例id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "serial" })
	protected List<Map<String, Object>> findNextTaskNodes(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> nodeMapList = null;
		boolean isDecisionMaking = false;
		ProInstance proInstance = null;
		String curNodeName = null, formTypeIdStr = null;
		
		params = params == null ? new HashMap<String, Object>() : params;
		
		if((instanceId == null || instanceId <= 0) && CommonFunctions.isNotBlank(params, "instanceId")) {
			instanceId = Long.valueOf(params.get("instanceId").toString());
		}
		
		if(instanceId == null || instanceId <= 0) {
			throw new IllegalArgumentException("缺少有效的流程实例id！");
		}
		
		proInstance = baseWorkflowService.findProByInstanceId(instanceId);
		
		if(proInstance == null) {
			throw new IllegalArgumentException("流程实例id【"+ instanceId +"】没有找到有效的流程信息！");
		}
		
		formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
		
		if(CommonFunctions.isNotBlank(params, "curNodeName")) {
			curNodeName = params.get("curNodeName").toString();
		}
		
		params.put("proInstance", proInstance);
		params.put("isDecisionMaking", isDecisionMaking);
		
		nodeMapList = super.findNextTaskNodes(instanceId, userInfo, params);
		
		if(nodeMapList != null && FocusReportNode359Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			//调整下一办理环节展示名称
			StringBuffer nodeName = new StringBuffer("");
			
			if(FocusReportNode359Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				Map<String, String> NODE_MAP = new HashMap<String, String>() {
					{
						//现场核实
						put(FocusReportNode359Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode359Enum.LEAD_NODE_NAME.getTaskNodeName(), "营商牵头部门反馈");
						put(FocusReportNode359Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode359Enum.MUNICIPAL_NODE_NAME.getTaskNodeName(), "市直部门反馈");
					}
				};
				
				for(Map<String, Object> nodeMap : nodeMapList) {
					nodeName.setLength(0);
					
					if(CommonFunctions.isNotBlank(nodeMap, "nodeName")) {
						nodeName.append(curNodeName).append("-").append(nodeMap.get("nodeName").toString());
					}
					
					if(CommonFunctions.isNotBlank(NODE_MAP, nodeName.toString())) {
						nodeMap.put("nodeNameZH", NODE_MAP.get(nodeName.toString()));
					}
				}
			}
		}
		
		return nodeMapList;
	}
	
	/**
	 * 存储人员配送信息
	 * @param taskId		任务id
	 * @param ccType		配送类型
	 * @param userInfoList	配送用户信息
	 */
//	private void saveMsgCCSet(Long taskId, String ccType, List<UserInfo> userInfoList) {
//		if(userInfoList != null) {
//			Map<String, Object> msgCCSetMap = null;
//			Long userId = null, orgId = null;
//			Set<String> userOrgSet = new HashSet<String>();
//			StringBuffer userOrgIdBuffer = null;
//			
//			for(UserInfo userInfo : userInfoList) {
//				msgCCSetMap = new HashMap<String, Object>();
//				userId = userInfo.getUserId();
//				orgId = userInfo.getOrgId();
//				userOrgIdBuffer = new StringBuffer("");
//				
//				if(userId != null && userId > 0) {
//					msgCCSetMap.put("userId", userId);
//					msgCCSetMap.put("userName", userInfo.getPartyName());
//					userOrgIdBuffer.append("-").append(userId);
//				}
//				if(orgId != null && orgId > 0) {
//					msgCCSetMap.put("orgId", orgId);
//					userOrgIdBuffer.append("-").append(orgId);
//				}
//				msgCCSetMap.put("taskId", taskId);
//				msgCCSetMap.put("ccType", ccType);
//				
//				if(!userOrgSet.contains(userOrgIdBuffer.toString())) {
//					try {
//						reportMsgCCSetService.saveMsgCCSet(msgCCSetMap, userInfo);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					
//					userOrgSet.add(userOrgIdBuffer.toString());
//				}
//			}
//		}
//	}
	
	/**
	 * 发送站内消息
	 * @param msgModuleCode		消息模块编码
	 * @param msgBizId			消息业务id
	 * @param msgContent		消息内容
	 * @param operateUserInfo	操作用户信息
	 * @param userInfoList		消息接收用户
	 */
//	private void sendMsg(String msgModuleCode, Long msgBizId, String msgContent, UserInfo operateUserInfo, List<UserInfo> userInfoList) {
//		if(StringUtils.isNotBlank(msgModuleCode)
//				&& msgBizId != null && msgBizId > 0
//				&& StringUtils.isNotBlank(msgContent)) {
//			List<Map<String, Long>> userOrgMapList = null;
//			
//			if(userInfoList != null && userInfoList.size() > 0) {
//				Long userId = null, orgId = null;
//				Map<String, Long> userOrgMap = null;
//				StringBuffer userOrgBuffer = null;
//				Set<String> userOrgSet = new HashSet<String>();
//				
//				userOrgMapList = new ArrayList<Map<String, Long>>();
//				
//				for(UserInfo userInfo : userInfoList) {
//					userOrgMap = new HashMap<String, Long>();
//					userOrgBuffer = new StringBuffer("");
//					userId = userInfo.getUserId();
//					orgId = userInfo.getOrgId();
//					
//					userOrgBuffer.append(userId).append("-").append(orgId);
//					
//					if(!userOrgSet.contains(userOrgBuffer.toString())) {
//						userOrgMap.put("userId", userId);
//						userOrgMap.put("orgId", orgId);
//						
//						userOrgMapList.add(userOrgMap);
//						
//						userOrgSet.add(userOrgBuffer.toString());
//					}
//				}
//			}
//			
//			if(userOrgMapList != null) {
//				ReceiverBO receiver = new ReceiverBO();
//				String REPORT_FOCUS_MSG_ACTION = "发送了";
//				
//				receiver.setUserOrgList(userOrgMapList);
//				
//				messageService.sendCommonMessageNA(operateUserInfo.getUserId(), operateUserInfo.getOrgId(), msgModuleCode, msgBizId, null, msgContent, REPORT_FOCUS_MSG_ACTION, receiver);
//			}
//		}
//	}
}
