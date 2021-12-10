package cn.ffcs.zhsq.reportFocus.martyrsFacility.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.common.utils.StringUtils;
import cn.ffcs.resident.bo.CiRs;
import cn.ffcs.resident.bo.PartyIndividual;
import cn.ffcs.resident.service.CiRsService;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.workflow.spring.WorkflowHolidayInfoService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.martyrsFacility.ReportMarFacMapper;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.petitionPerson.ReportPetPerMapper;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.forestFirePrevention.service.impl.FocusReportNode358Enum;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.service.impl.ReportMsgCCTypeEnum;
import cn.ffcs.zhsq.reportFocus.reportMsgCCSet.IReportMsgCCSetService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportFocusWorkflow4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 信访人员稳控工作流相关接口
 * @ClassName:   ReportFocusWorkflow4MarFacServiceImpl   
 * @author:      郭昱丹(guoyd)
 * @date:        2021年5月7日 上午10:16:15
 */
@Service(value = "reportFocusWorkflow4MarFacService")
public class ReportFocusWorkflow4MarFacServiceImpl extends ReportFocusWorkflow4TwoVioPreServiceImpl {

	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	@Resource(name="eventDisposalWorkflowForEventService")
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	@Autowired
	private IReportFocusService reportFocusService;
	@Autowired
	private WorkflowHolidayInfoService workflowHolidayInfoService;
	@Autowired
	private ReportMarFacMapper reportMarFacMapper;
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	@Autowired
	private UserManageOutService userManageService;
	@Autowired
	private IWorkflow4BaseService workflow4BaseService;
	@Autowired
	private IReportMsgCCSetService reportMsgCCSetService;
	
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
			
			String dataSource = "";
			String isDamaged = "";
			if(CommonFunctions.isNotBlank(params, "reportFocusMap")) {
				Map<String, Object> reportFocusMap = new HashMap<String, Object>();
				reportFocusMap = (Map<String, Object>)params.get("reportFocusMap");
				dataSource = StringUtils.toString(reportFocusMap.get("dataSource"));
				initMap.put("dataSourceName", MarFacDataSourceEnum.getValue(dataSource));
				isDamaged = StringUtils.toString(reportFocusMap.get("isDamaged"));
			}
			
			if(CommonFunctions.isNotBlank(initMap, "nextTaskNodes")) {
				List<Map<String, Object>> nextTaskNodes = new ArrayList<Map<String, Object>>();
				nextTaskNodes = (List<Map<String, Object>>)initMap.get("nextTaskNodes");
				
				if(FocusReportNode363Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)){
					if(FocusReportNode363Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)) {//发现问题
						String taskNode = "";
						if(MarFacDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)
								&&"0".equals(isDamaged)) {
							taskNode = FocusReportNode363Enum.END_NODE_NAME.getTaskNodeName();
						}else {
							taskNode = FocusReportNode363Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName();
						}
						if(!StringUtils.isBlank(taskNode)) {
							for(int i = nextTaskNodes.size() - 1; i >= 0; i--) {
								if(!StringUtils.toString(nextTaskNodes.get(i).get("nodeName")).equals(taskNode)) {
									nextTaskNodes.remove(i);
								}
							}
						}
					}else if(FocusReportNode363Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(curNodeName)) {//组织验收
						Map<String, Object> temp_params = new HashMap<String, Object>();
						temp_params.put("instanceId", StringUtils.toString(params.get("instanceId")));
						List<Map<String, Object>> myTaskList = reportMarFacMapper.findToDoOrgId(temp_params);
						if(!myTaskList.isEmpty()) {
							Map<String, Object> beforeMap = myTaskList.get(0);
							String taskNode = StringUtils.toString(beforeMap.get("TASK_NAME"));
							if(!StringUtils.isBlank(taskNode)) {
								for(int i = nextTaskNodes.size() - 1; i >= 0; i--) {
									if(!StringUtils.toString(nextTaskNodes.get(i).get("nodeName")).equals(taskNode)
											&&!StringUtils.toString(nextTaskNodes.get(i).get("nodeName")).equals(FocusReportNode363Enum.END_NODE_NAME.getTaskNodeName())) {
										nextTaskNodes.remove(i);
									}
								}
							}
						}
					}
				}
				if(nextTaskNodes.size() > 0){
					initMap.put("nextTaskNodes",nextTaskNodes);
				}
			}
			
//			isEditable = true;
			
			initMap.put("isEditable", isEditable);
			initMap.put("isEditableNode", isEditableNode);
			
		}
		
		return initMap;
	}
	
	@SuppressWarnings("unused")
	@Override
	public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		String curNodeName = null, formTypeIdStr = null, curTaskId = null,formId = null,advice = null,preCurTaskId = null;
		Long reportId = null;
		
		if(instanceId != null && instanceId > 0) {
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			if(proInstance != null) {
				curNodeName = proInstance.getCurNode();
				formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
				formId = String.valueOf(proInstance.getFormId());
				reportId = proInstance.getFormId();
			}
			Map<String, Object> curDataMap = baseWorkflowService.findCurTaskData(instanceId);
			if(CommonFunctions.isNotBlank(curDataMap, "WF_DBID_")) {
				curTaskId = curDataMap.get("WF_DBID_").toString();
			}
			if(CommonFunctions.isNotBlank(extraParam, "advice")) {
				advice = String.valueOf(extraParam.get("advice"));
			}
		}
		
		flag = super.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);

		return flag;
	}
	
	
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
				
				if(!FocusReportNode363Enum.END_NODE_NAME.getTaskNodeNameZH().equals(taskNameZH)) {
					Map<String, Object> endTaskMap = new HashMap<String, Object>();
					
					endTaskMap.put("TASK_NAME", FocusReportNode363Enum.END_NODE_NAME.getTaskNodeNameZH());
					
					resultMapList.add(0, endTaskMap);
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
		if(ReportTypeEnum.martyrsFacility.getReportTypeIndex().equals(reportType)) {
			workflowName = "南安烈士纪念设施流程";
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
		if(ReportTypeEnum.martyrsFacility.getReportTypeIndex().equals(reportType)) {
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
		
//		if(nodeMapList != null && FocusReportNode363Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			//调整下一办理环节展示名称
//			StringBuffer nodeName = new StringBuffer("");
			
//			if(FocusReportNode363Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
//				Map<String, String> NODE_MAP = new HashMap<String, String>() {
//					{
//						//现场核实
//						put(FocusReportNode363Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode363Enum.LEAD_NODE_NAME.getTaskNodeName(), "营商牵头部门反馈");
//						put(FocusReportNode363Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode363Enum.MUNICIPAL_NODE_NAME.getTaskNodeName(), "市直部门反馈");
//					}
//				};
//				
//				for(Map<String, Object> nodeMap : nodeMapList) {
//					nodeName.setLength(0);
//					
//					if(CommonFunctions.isNotBlank(nodeMap, "nodeName")) {
//						nodeName.append(curNodeName).append("-").append(nodeMap.get("nodeName").toString());
//					}
//					
//					if(CommonFunctions.isNotBlank(NODE_MAP, nodeName.toString())) {
//						nodeMap.put("nodeNameZH", NODE_MAP.get(nodeName.toString()));
//					}
//				}
//			}
//		}
		
		return nodeMapList;
	}
	
	/**
	 * 依据流程实例id获取历史任务分送、选送人员
	 * @param instanceId	流程实例id
	 * @return
	 * 		taskId : {
	 * 			msgCCSetMapList				已配送的人员信息
	 * 			msgCCSetDistributeMapLisst	已配送的分送人员信息
	 * 			msgCCSetSelectMapList		已配送的选送人员信息
	 * 			distributeUserNames			分送人员姓名
	 * 			distributeOrgNames			分送人员组织名称
	 * 			distributeUserOrgNames		分送人员组织姓名信息
	 * 			selectUserNames				选送人员姓名
	 * 			selectOrgNames				选送组织姓名
	 * 			selectUserOrgNames			选送组织姓名信息
	 * 		}
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Map<String, Object>> findMsgCCSetByInstanceId(Long instanceId) {
		List<Map<String, Object>> msgCCSetInfoList = reportMsgCCSetService.findMsgCCSetByInstanceId(instanceId, null);
		Map<String, Map<String, Object>> resultMap = null;
		
		if(msgCCSetInfoList != null) {
			String taskId = null, ccType = null, userName = null, orgName = null;
			Map<String, Object> msgCCSetInfo = null;
			StringBuffer distributeUserNames = null, distributeOrgNames = null, distributeUserOrgNames = null,
						 selectUserNames = null, selectOrgNames = null, selectUserOrgNames = null;
			List<Map<String, Object>> msgCCSetMapList = null, msgCCSetDistributeMapList = null, msgCCSetSelectMapList = null;
			
			resultMap = new HashMap<String, Map<String, Object>>();
			
			for(Map<String, Object> msgCCSetMap : msgCCSetInfoList) {
				taskId = null;
				userName = null;
				orgName = null;
				
				if(CommonFunctions.isNotBlank(msgCCSetMap, "taskId")) {
					taskId = msgCCSetMap.get("taskId").toString();
				}
				if(CommonFunctions.isNotBlank(msgCCSetMap, "ccType")) {
					ccType = msgCCSetMap.get("ccType").toString();
				}
				if(CommonFunctions.isNotBlank(msgCCSetMap, "userName")) {
					userName = msgCCSetMap.get("userName").toString();
				}
				if(CommonFunctions.isNotBlank(msgCCSetMap, "orgName")) {
					orgName = msgCCSetMap.get("orgName").toString();
				}
				
				if(resultMap.containsKey(taskId)) {
					msgCCSetInfo = resultMap.get(taskId);
					
					msgCCSetMapList = (List<Map<String, Object>>) msgCCSetInfo.get("msgCCSetMapList");
					msgCCSetDistributeMapList = (List<Map<String, Object>>) msgCCSetInfo.get("msgCCSetDistributeMapList");
					msgCCSetSelectMapList = (List<Map<String, Object>>) msgCCSetInfo.get("msgCCSetSelectMapList");
					
					distributeUserNames = (StringBuffer) msgCCSetInfo.get("distributeUserNames");
					distributeOrgNames = (StringBuffer) msgCCSetInfo.get("distributeOrgNames");
					distributeUserOrgNames = (StringBuffer) msgCCSetInfo.get("distributeUserOrgNames");
					
					selectUserNames = (StringBuffer) msgCCSetInfo.get("selectUserNames");
					selectOrgNames = (StringBuffer) msgCCSetInfo.get("selectOrgNames");
					selectUserOrgNames = (StringBuffer) msgCCSetInfo.get("selectUserOrgNames");
				} else {
					msgCCSetInfo = new HashMap<String, Object>();
					msgCCSetMapList = new ArrayList<Map<String, Object>>();
					msgCCSetDistributeMapList = new ArrayList<Map<String, Object>>();
					msgCCSetSelectMapList = new ArrayList<Map<String, Object>>();
					distributeUserNames = new StringBuffer("");
					distributeOrgNames = new StringBuffer("");
					distributeUserOrgNames = new StringBuffer("");
					selectUserNames = new StringBuffer("");
					selectOrgNames = new StringBuffer("");
					selectUserOrgNames = new StringBuffer("");
					
					msgCCSetInfo.put("msgCCSetMap", msgCCSetMap);
				}
				
				msgCCSetMapList.add(msgCCSetMap);
				
				if(ReportMsgCCTypeEnum.CCTYPE_DISTRIBUTE.getCcType().equals(ccType)) {
					msgCCSetDistributeMapList.add(msgCCSetMap);
					
					if(StringUtils.isNotBlank(userName) || StringUtils.isNotBlank(orgName)) {
						if(distributeUserOrgNames.length() > 0) {
							distributeUserOrgNames.append(",");
						}
					}
					
					if(StringUtils.isNotBlank(userName)) {
						if(distributeUserNames.length() > 0) {
							distributeUserNames.append(",");
						}
						
						distributeUserNames.append(userName);
						distributeUserOrgNames.append(userName);
					}
					
					if(StringUtils.isNotBlank(orgName)) {
						if(distributeOrgNames.length() > 0) {
							distributeOrgNames.append(",");
						}
						
						distributeOrgNames.append(orgName);
						distributeUserOrgNames.append("(").append(orgName).append(")");
					}
				} else if(ReportMsgCCTypeEnum.CCTYPE_SELECT.getCcType().equals(ccType)) {
					msgCCSetSelectMapList.add(msgCCSetMap);
					
					if(StringUtils.isNotBlank(userName) || StringUtils.isNotBlank(orgName)) {
						if(selectUserOrgNames.length() > 0) {
							selectUserOrgNames.append(",");
						}
					}
					
					if(StringUtils.isNotBlank(userName)) {
						if(selectUserNames.length() > 0) {
							selectUserNames.append(",");
						}
						
						selectUserNames.append(userName);
						selectUserOrgNames.append(userName);
					}
					
					if(StringUtils.isNotBlank(orgName)) {
						if(selectOrgNames.length() > 0) {
							selectOrgNames.append(",");
						}
						
						selectOrgNames.append(orgName);
						selectUserOrgNames.append("(").append(orgName).append(")");
					}
				}
				
				msgCCSetInfo.put("msgCCSetMapList", msgCCSetMapList);
				msgCCSetInfo.put("msgCCSetDistributeMapList", msgCCSetDistributeMapList);
				msgCCSetInfo.put("msgCCSetSelectMapList", msgCCSetSelectMapList);
				msgCCSetInfo.put("distributeUserNames", distributeUserNames);
				msgCCSetInfo.put("distributeOrgNames", distributeOrgNames);
				msgCCSetInfo.put("distributeUserOrgNames", distributeUserOrgNames);
				msgCCSetInfo.put("selectUserNames", selectUserNames);
				msgCCSetInfo.put("selectOrgNames", selectOrgNames);
				msgCCSetInfo.put("selectUserOrgNames", selectUserOrgNames);
				
				resultMap.put(taskId, msgCCSetInfo);
			}
		}
		
		return resultMap;
	}
}
