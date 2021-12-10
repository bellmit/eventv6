package cn.ffcs.zhsq.reportFocus.poorSupportVisit.service.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportFocusWorkflow4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 扶贫走访(Poor Support Visit)工作流相关接口
 * @ClassName:   ReportFocusWorkflow4PSVServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年12月4日 上午10:20:00
 */
@Service(value = "reportFocusWorkflow4PSVService")
public class ReportFocusWorkflow4PSVServiceImpl extends ReportFocusWorkflow4TwoVioPreServiceImpl {
	@Override
	public boolean rejectWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false, isRejectByParent = true, isBack2Draft = false;
		String formTypeIdStr = null, curNodeName = null;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
			curNodeName = extraParam.get("curNodeName").toString();
		}
		if(CommonFunctions.isNotBlank(extraParam, "formTypeId")) {
			formTypeIdStr = extraParam.get("formTypeId").toString();
		}
		
		if(FocusReportNode356Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode356Enum.FILL_REPORT_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isRejectByParent = false;
				isBack2Draft = true;
			} else if(FocusReportNode356Enum.DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isRejectByParent = true;
				isBack2Draft = true;
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
	
	@Override
	public Map<String, Object> initWorkflow4Handle(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		Map<String, Object> initMap = super.initWorkflow4Handle(instanceId, userInfo, params);
		String curNodeName = null, formTypeIdStr = null;
		boolean isEditableNode = false;

		if(CommonFunctions.isBlank(params, "reportType")) {
			initMap.put("reportType", ReportTypeEnum.poorSupportVisit.getReportTypeIndex());
		}
		if(CommonFunctions.isNotBlank(initMap, "curNodeName")) {
			curNodeName = initMap.get("curNodeName").toString();
		}
		if(CommonFunctions.isNotBlank(initMap, "formTypeId")) {
			formTypeIdStr = initMap.get("formTypeId").toString();
		}

		//当前环节为 挂钩帮扶责任人核实反馈 或者 第一网格长（驻村领导）核实反馈 节点时，基本信息可编辑
		if(CommonFunctions.isNotBlank(initMap, "isAble2Handle")
				&& Boolean.valueOf(initMap.get("isAble2Handle").toString())
				&& FocusReportNode356Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
				&& (FocusReportNode356Enum.HRP_VERIFY_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode356Enum.COMMUNITY_VERIFY_NODE_NAME.getTaskNodeName().equals(curNodeName))
		) {
			isEditableNode = true;
			
			initMap.put("isEditable", true);
		}
		
		initMap.put("isEditableNode", isEditableNode);

		return initMap;
	}
	
	/**
	 * 判断是否需要添加子流程信息
	 * @param formTypeIdStr	表单ID
	 * @param taskName		可添加子流程的节点名称
	 * @param listType		列表类型
	 * @param params
	 * @return
	 */
	protected boolean isAble2ShowSubPro(String formTypeIdStr, String taskName, String listType, Map<String, Object> params) {
		return false;
	}

	@Override
	public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> resultMapList = super.capHandledTaskInfoMap(instanceId, order, params);

		if(resultMapList != null && resultMapList.size() > 0
				&& IWorkflow4BaseService.SQL_ORDER_DESC.equals(order)) {
			Map<String, Object> taskMap = resultMapList.get(0);

			if(CommonFunctions.isBlank(taskMap, "IS_CURRENT_TASK")) {
				Map<String, Object> endTaskMap = new HashMap<String, Object>();

				endTaskMap.put("TASK_NAME", FocusReportNode356Enum.END_NODE_NAME.getTaskNodeNameZH());

				resultMapList.add(0, endTaskMap);
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
		if(ReportTypeEnum.poorSupportVisit.getReportTypeIndex().equals(reportType)) {
			workflowName = "南安扶贫走访流程";
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
		if(ReportTypeEnum.poorSupportVisit.getReportTypeIndex().equals(reportType)) {
			wfTypeId = "focus_report";
		}
		
		return wfTypeId;
	}
	
	protected List<Map<String, Object>> findNextTaskNodes(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> nextTaskNodeMapList = super.findNextTaskNodes(instanceId, userInfo, params);
		boolean isDecisionMaking = false;
		params = params == null ? new HashMap<String, Object>() : params;
		String curNodeName = null;
		
		if(CommonFunctions.isNotBlank(params, "curNodeName")) {
			curNodeName = params.get("curNodeName").toString();
		}
		
		isDecisionMaking = FocusReportNode356Enum.DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName);
		
		params.put("isDecisionMaking", isDecisionMaking);
		
		nextTaskNodeMapList = super.findNextTaskNodes(instanceId, userInfo, params);
		
		if(nextTaskNodeMapList != null) {
			
		}
		
		return nextTaskNodeMapList;
	}
	
}
