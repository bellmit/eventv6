package cn.ffcs.zhsq.reportFocus.forestFirePrevention.service.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportFocusWorkflow4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 森林防灭火工作流相关接口
 * @ClassName:   ReportFocusWorkflow4FFPServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年3月31日 上午10:51:40
 */
@Service(value = "reportFocusWorkflow4FFPService")
public class ReportFocusWorkflow4FFPServiceImpl extends ReportFocusWorkflow4TwoVioPreServiceImpl {
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
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
			
			if(FocusReportNode358Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
					&& FocusReportNode358Enum.VERIFY_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isEditable = true;
				isEditableNode = true;
			}
			
			initMap.put("isEditable", isEditable);
			initMap.put("isEditableNode", isEditableNode);
		}
		
		return initMap;
	}
	
	@Override
	public boolean recallWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		extraParam.put("isAble2Recall", true);
		
		flag = super.recallWorkflow4Report(instanceId, userInfo, extraParam);
		
		return flag;
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
				
				if(!FocusReportNode358Enum.END_NODE_NAME.getTaskNodeNameZH().equals(taskNameZH)) {
					Map<String, Object> endTaskMap = new HashMap<String, Object>();
					
					endTaskMap.put("TASK_NAME", FocusReportNode358Enum.END_NODE_NAME.getTaskNodeNameZH());
					
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
		if(ReportTypeEnum.forestFirePrevention.getReportTypeIndex().equals(reportType)) {
			workflowName = "南安森林防灭火事件";
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
		if(ReportTypeEnum.forestFirePrevention.getReportTypeIndex().equals(reportType)) {
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
	@SuppressWarnings("serial")
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
		
		if(nodeMapList != null && FocusReportNode358Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			//调整下一办理环节展示名称
			StringBuffer nodeName = new StringBuffer("");
			
			if(FocusReportNode358Enum.VERIFY_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				Map<String, String> NODE_MAP = new HashMap<String, String>() {
					{
						//现场核实
						put(FocusReportNode358Enum.VERIFY_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode358Enum.END_NODE_NAME.getTaskNodeName(), "信息不属实");
						put(FocusReportNode358Enum.VERIFY_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode358Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName(), "信息属实");
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
	
}
