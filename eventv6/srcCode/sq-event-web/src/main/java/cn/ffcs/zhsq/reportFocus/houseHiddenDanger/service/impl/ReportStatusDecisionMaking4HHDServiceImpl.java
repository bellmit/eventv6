package cn.ffcs.zhsq.reportFocus.houseHiddenDanger.service.impl;

import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportStatusDecisionMaking4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 房屋安全隐患(House Hidden Danger)状态决策类
 * 必填参数
 * 		curNodeName		当前环节名称
 * 		nextNodeName	下一环节名称
 * 		reportId/reportUUID		上报id/上报UUID
 * 
 * 选填参数
 * 		operateUserInfo	操作用户，类型为cn.ffcs.uam.bo.UserInfo
 * 		instanceId		流程实例id
 * @ClassName:   ReportStatusDecisionMaking4HHDServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月17日 下午5:53:48
 */
@Service(value = "reportStatusDecisionMaking4HHDService")
public class ReportStatusDecisionMaking4HHDServiceImpl extends ReportStatusDecisionMaking4TwoVioPreServiceImpl {
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
			
			if(PRO_STATUS_END.equals(proStatus)) {
				curNodeName = FocusReportNode35301Enum.END_NODE_NAME.getTaskNodeName();
			} else if(!isRejectOperate && !isRecallOperate && CommonFunctions.isNotBlank(params, "nextNodeName")) {
				curNodeName = params.get("nextNodeName").toString();
			}
			
			if(FocusReportNode35301Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				statusMap = initReportStatusMap();
			} else if(FocusReportNode35302Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				statusMap = initReportStatusMap_35302();
			}
			
			if(CommonFunctions.isNotBlank(statusMap, curNodeName)) {
				reportStatus = statusMap.get(curNodeName);
			}
		}
		
		return reportStatus;
	}
	
	/**
	 * 南安房屋安全隐患信息流程_常规 节点状态初始化构造
	 * @return
	 */
	protected Map<String, String> initReportStatusMap() {
		Map<String, String> statusMap = new HashMap<String, String>();
		
		statusMap.put(FocusReportNode35301Enum.START_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35301Enum.FIND_HHD_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35301Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35301Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.VERIFY_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode35301Enum.HHD_COGNIZANCE_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.HHD_COGNIZANCE.getReportStatus());
		
		statusMap.put(FocusReportNode35301Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.HANDLE_STATUS.getReportStatus());

		statusMap.put(FocusReportNode35301Enum.MASSES_REPORT_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35301Enum.ASSIGNED_SUPERIOR_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35301Enum.FIREPROTECTION_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35301Enum.BUSINESS_LICENSE_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());

		statusMap.put(FocusReportNode35301Enum.END_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.END_STATUS.getReportStatus());
		
		return statusMap;
	}
	
	/**
	 * 南安房屋安全隐患信息流程_突发 节点状态初始化构造
	 * @return
	 */
	private Map<String, String> initReportStatusMap_35302() {
		Map<String, String> statusMap = new HashMap<String, String>();
		
		statusMap.put(FocusReportNode35302Enum.START_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35302Enum.FIND_HHD_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35302Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.HANDLE_STATUS.getReportStatus());

		statusMap.put(FocusReportNode35302Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());

		statusMap.put(FocusReportNode35302Enum.END_NODE_NAME.getTaskNodeName(), HHDReportStatusEnum.END_STATUS.getReportStatus());
		
		return statusMap;
	}
	
}
