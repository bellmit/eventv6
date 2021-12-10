package cn.ffcs.zhsq.reportFocus.poorSupportVisit.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportStatusDecisionMaking4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 扶贫走访状态决策类
 * 必填参数
 * 		curNodeName		当前环节名称
 * 		nextNodeName	下一环节名称
 * 		reportId/reportUUID		上报id/上报UUID
 * 
 * 选填参数
 * 		operateUserInfo	操作用户，类型为cn.ffcs.uam.bo.UserInfo
 * 		instanceId		流程实例id 
 * @ClassName:   ReportStatusDecisionMaking4PSVServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年12月4日 上午10:02:39
 */
@Service(value = "reportStatusDecisionMaking4PSVService")
public class ReportStatusDecisionMaking4PSVServiceImpl extends ReportStatusDecisionMaking4TwoVioPreServiceImpl {
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	/**
	 * 获取上报状态
	 */
	protected String capReportStaus(Map<String, Object> params) {
		String curNodeName = null, reportStatus = null;
		Map<String, String> statusMap = null;
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			Long instanceId = null;
			String formTypeIdStr = null, proStatus = null, PRO_STATUS_END = "2";
			boolean isRejectOperate = false, isRecallOperate = false;
			
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {}
			
			if(CommonFunctions.isNotBlank(params, "isRejectOperate")) {
				isRejectOperate = Boolean.valueOf(params.get("isRejectOperate").toString());
			}
			
			if(CommonFunctions.isNotBlank(params, "isRecallOperate")) {
				isRecallOperate = Boolean.valueOf(params.get("isRecallOperate").toString());
			}
			
			if(instanceId != null && instanceId > 0) {
				ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
				
				if(proInstance != null) {
					formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
					curNodeName = proInstance.getCurNode();
					proStatus = proInstance.getStatus();
				}
			}
			
			if(FocusReportNode356Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				if(PRO_STATUS_END.equals(proStatus)) {
					curNodeName = FocusReportNode356Enum.END_NODE_NAME.getTaskNodeName();
				} else if(!isRejectOperate && !isRecallOperate && CommonFunctions.isNotBlank(params, "nextNodeName")) {
					curNodeName = params.get("nextNodeName").toString();
				}
				
				statusMap = initReportStatusMap();
				
				if(CommonFunctions.isNotBlank(statusMap, curNodeName)) {
					reportStatus = statusMap.get(curNodeName);
				}
			}
		}
		
		return reportStatus;
	}
	
	/**
	 * 构造上报初始化状态
	 * @return
	 */
	protected Map<String, String> initReportStatusMap() {
		Map<String, String> statusMap = new HashMap<String, String>();
		
		statusMap.put(FocusReportNode356Enum.START_NODE_NAME.getTaskNodeName(), PSVReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode356Enum.FILL_REPORT_NODE_NAME.getTaskNodeName(), PSVReportStatusEnum.DRAFT_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode356Enum.DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName(), PSVReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode356Enum.HRP_VERIFY_NODE_NAME.getTaskNodeName(), PSVReportStatusEnum.VERIFY_STATUS.getReportStatus());
		statusMap.put(FocusReportNode356Enum.COMMUNITY_VERIFY_NODE_NAME.getTaskNodeName(), PSVReportStatusEnum.VERIFY_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode356Enum.STREET_HANDLE_TASK_NODE_NAME.getTaskNodeName(), PSVReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode356Enum.COUNTY_HANDLE_TASK_NODE_NAME.getTaskNodeName(), PSVReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode356Enum.COUNTY_DEPARTMENT_HANDLE_TASK_NODE_NAME.getTaskNodeName(), PSVReportStatusEnum.HANDLE_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode356Enum.END_NODE_NAME.getTaskNodeName(), PSVReportStatusEnum.END_STATUS.getReportStatus());
		
		return statusMap;
	}
}
