package cn.ffcs.zhsq.reportFocus.enterpriseHiddenDanger.service.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportFocusWorkflow4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 企业安全隐患工作流相关接口
 * @ClassName:   ReportFocusWorkflow4EHDServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月6日 下午4:12:43
 */
@Service(value = "reportFocusWorkflow4EHDService")
public class ReportFocusWorkflow4EHDServiceImpl extends ReportFocusWorkflow4TwoVioPreServiceImpl {
	@Autowired
	private IBaseWorkflowService baseWorkflowService;

	@Autowired
	private IReportFocusService reportFocusService;

	@Autowired
	private IReportIntegrationService reportIntegrationService;

	
	@Override
	public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = super.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);
		
		if(flag && FocusReportNode351Enum.COMMUNITY_END_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
			flag = baseWorkflowService.endWorkflow4Base(instanceId, userInfo, null);
		}

		String curNodeName = null;
		if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
			curNodeName = extraParam.get("curNodeName").toString();
		}

		//保存流程流转过程中 保存上报信息基本信息
		if(flag && FocusReportNode351Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)){
			Map<String, Object> reportFocus = new HashMap<String, Object>();
			Long reportId = null;


			Map<String, Object> reportParams = new HashMap<String, Object>();

			try {
				if(CommonFunctions.isNotBlank(extraParam, "formId")) {
					reportId = Long.valueOf(extraParam.get("formId").toString());
				} else if(CommonFunctions.isNotBlank(extraParam, "reportId")) {
					reportId = Long.valueOf(extraParam.get("reportId").toString());
				}
			} catch(NumberFormatException e) {}

			reportParams.putAll(extraParam);
			reportParams.put("reportId", reportId);
			reportParams.put("reportType", ReportTypeEnum.enterpriseHiddenDanger.getReportTypeIndex());
			reportFocus = reportIntegrationService.findReportFocusByUUID(null, userInfo, reportParams);

			if(!reportFocus.isEmpty()){
				//当前环节为task3 冗余保存是否属实
				if(FocusReportNode351Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)){
					if(FocusReportNode351Enum.JOINT_CONSULTATION_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
						reportFocus.put("isTrue","1");
					}else if(FocusReportNode351Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
						reportFocus.put("isTrue","0");
					}
				}

				reportFocus.put("reportType", ReportTypeEnum.enterpriseHiddenDanger.getReportTypeIndex());
				reportFocus.put("reportId", reportId);
				reportFocusService.saveReportFocus(reportFocus, userInfo);
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean rejectWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false, isRejectByParent = true, isBack2Draft = false;
		String formTypeId = null, curNodeName = null;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
			curNodeName = extraParam.get("curNodeName").toString();
		}
		if(CommonFunctions.isNotBlank(extraParam, "formTypeId")) {
			formTypeId = extraParam.get("formTypeId").toString();
		}
		
		if(StringUtils.isBlank(formTypeId) || StringUtils.isBlank(curNodeName)) {
			if(instanceId == null || instanceId < 0) {
				throw new IllegalArgumentException("缺少有效的流程实例id！");
			}
			
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			
			if(proInstance != null) {
				Long reportId = proInstance.getFormId();
				formTypeId = String.valueOf(proInstance.getFormTypeId());
				curNodeName = proInstance.getCurNode();
				
				extraParam.put("reportId", reportId);
			}
		}
		
		if(FocusReportNode351Enum.FORM_TYPE_ID.toString().equals(formTypeId)) {
			if(FocusReportNode351Enum.FIND_EHD_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isBack2Draft = true;
				isRejectByParent = false;
			} else if(FocusReportNode351Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isBack2Draft = true;
				isRejectByParent = true;
			}
		}
		
		extraParam.put("isRejectByParent", isRejectByParent);
		extraParam.put("isBack2Draft", isBack2Draft);
		
		flag = super.rejectWorkflow4Report(instanceId, userInfo, extraParam);
		
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
	public Map<String, Object> initWorkflow4Handle(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		Map<String, Object> initMap = super.initWorkflow4Handle(instanceId, userInfo, params);
		boolean isEditableNode = false;
		
		if(CommonFunctions.isBlank(params, "reportType")) {
			initMap.put("reportType", ReportTypeEnum.enterpriseHiddenDanger.getReportTypeIndex());
		}


		if(CommonFunctions.isNotBlank(initMap,"curNodeName")
				&&(FocusReportNode351Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(initMap.get("curNodeName"))
					||FocusReportNode351Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(initMap.get("curNodeName"))
				)
		){
			//nextTaskNodes nodeName nodeNameZH
			List<Map<String,Object>> nextTaskNodes = new ArrayList<>();
			//当前环节
			String curNodeName = String.valueOf(initMap.get("curNodeName"));
			//下一环节
			String nodeName = null;
			if(CommonFunctions.isNotBlank(initMap,"nextTaskNodes")){
				nextTaskNodes = (List<Map<String, Object>>) initMap.get("nextTaskNodes");
			}
			if(nextTaskNodes.size()>0){
				for(Map<String, Object> taskMap:nextTaskNodes){
					if(CommonFunctions.isNotBlank(taskMap,"nodeName")){
						nodeName = String.valueOf(taskMap.get("nodeName"));

						//当前环节为：第一副网格长(驻村工作队长)核实时，下一环节的联合会商改为属实，归档改为不属实
						if(FocusReportNode351Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)){
							if(FocusReportNode351Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
								taskMap.put("nodeNameZH","不属实");
							}else if(FocusReportNode351Enum.JOINT_CONSULTATION_NODE_NAME.getTaskNodeName().equals(nodeName)){
								taskMap.put("nodeNameZH","属实");
							}
						}else if (FocusReportNode351Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
							//当前环节为：立案查处，下一环节的归档改为不予立案，行政处罚改为给予立案
							if(FocusReportNode351Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
								taskMap.put("nodeNameZH","不予立案");
							}else if(FocusReportNode351Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(nodeName)){
								taskMap.put("nodeNameZH","给予立案");
							}
						}

					}
				}
			}
			//当前环节为 “第一副网格长(驻村工作队长)核实” 节点时，企业安全隐患信息可编辑
			if(CommonFunctions.isNotBlank(initMap, "isAble2Handle")
					&& Boolean.valueOf(initMap.get("isAble2Handle").toString())
					&& FocusReportNode351Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isEditableNode = true;
				
				initMap.put("isEditable", true);
			}
			
			initMap.put("isEditableNode", isEditableNode);
		}
		
		return initMap;
	}

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
				
				if(!FocusReportNode351Enum.END_NODE_NAME.getTaskNodeNameZH().equals(taskNameZH)) {
					Map<String, Object> endTaskMap = new HashMap<String, Object>();
					
					endTaskMap.put("TASK_NAME", FocusReportNode351Enum.END_NODE_NAME.getTaskNodeNameZH());

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
		if(ReportTypeEnum.enterpriseHiddenDanger.getReportTypeIndex().equals(reportType)) {
			workflowName = "南安企业隐患信息流程";
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
		if(ReportTypeEnum.enterpriseHiddenDanger.getReportTypeIndex().equals(reportType)) {
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
	protected List<Map<String, Object>> findNextTaskNodes(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		String curNodeName = null;
		ProInstance proInstance = null;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if((instanceId == null || instanceId <= 0) && CommonFunctions.isNotBlank(params, "instanceId")) {
			instanceId = Long.valueOf(params.get("instanceId").toString());
		}
		
		if(instanceId == null || instanceId <= 0) {
			throw new IllegalArgumentException("缺少有效的流程实例id！");
		}
		
		if(CommonFunctions.isNotBlank(params, "proInstance")) {
			proInstance = (ProInstance) params.get("proInstance");
		} else {
			proInstance = this.baseWorkflowService.findProByInstanceId(instanceId);
		}
		
		if(proInstance == null) {
			throw new IllegalArgumentException("流程实例id【"+ instanceId +"】没有找到有效的流程信息！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curNodeName")) {
			curNodeName = params.get("curNodeName").toString();
		} else {
			curNodeName = proInstance.getCurNode();
		}
		
		if(CommonFunctions.isBlank(params, "proInstance")) {
			params.put("proInstance", proInstance);
		}
		params.put("isDecisionMaking", FocusReportNode351Enum.FIND_EHD_NODE_NAME.getTaskNodeName().equals(curNodeName));
		
		return super.findNextTaskNodes(instanceId, userInfo, params);
	}
	
}
