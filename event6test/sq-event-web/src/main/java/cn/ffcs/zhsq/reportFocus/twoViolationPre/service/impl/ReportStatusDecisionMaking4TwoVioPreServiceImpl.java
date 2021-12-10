package cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportStatusDecisionMakingServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 两违防治上报状态决策类
 * 必填参数
 * 		curNodeName		当前环节名称
 * 		nextNodeName	下一环节名称
 * 		reportId/reportUUID		上报id/上报UUID
 * 
 * 选填参数
 * 		operateUserInfo	操作用户，类型为cn.ffcs.uam.bo.UserInfo
 * 		instanceId		流程实例id
 * @ClassName:   ReportStatusDecisionMaking4TwoVioPreServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年9月15日 上午11:15:47
 */
@Service(value = "reportStatusDecisionMaking4TwoVioPreService")
public class ReportStatusDecisionMaking4TwoVioPreServiceImpl extends ReportStatusDecisionMakingServiceImpl {
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	/**
	 * 获取上报状态
	 * @param params
	 * @return
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
			
			if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				if(PRO_STATUS_END.equals(proStatus)) {
					curNodeName = FocusReportNode350Enum.END_NODE_NAME.getTaskNodeName();
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
		
		statusMap.put(FocusReportNode350Enum.START_NODE_NAME.getTaskNodeName(), TwoVioPreReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode350Enum.FIND_TWO_VIO_NODE_NAME.getTaskNodeName(), TwoVioPreReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode350Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode350Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreReportStatusEnum.VERIFY_STATUS.getReportStatus());
		statusMap.put(FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreReportStatusEnum.IDENTIFY_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreReportStatusEnum.MANAGE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode350Enum.STREET_DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreReportStatusEnum.MANAGE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreReportStatusEnum.MANAGE_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode350Enum.ASSIGN_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreReportStatusEnum.ASSIGN_STATUS.getReportStatus());
		statusMap.put(FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreReportStatusEnum.DEPARTMENT_MANAGE_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreReportStatusEnum.CHECK_STATUS.getReportStatus());
		statusMap.put(FocusReportNode350Enum.DEPARTMENT_CHECK_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreReportStatusEnum.CHECK_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode350Enum.END_NODE_NAME.getTaskNodeName(), TwoVioPreReportStatusEnum.END_STATUS.getReportStatus());
		
		return statusMap;
	}

}
