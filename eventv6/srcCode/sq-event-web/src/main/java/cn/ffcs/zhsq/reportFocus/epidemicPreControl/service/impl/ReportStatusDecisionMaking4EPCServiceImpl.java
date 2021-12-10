package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportStatusDecisionMaking4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 企业安全隐患状态决策类
 * 必填参数
 * 		curNodeName		当前环节名称
 * 		nextNodeName	下一环节名称
 * 		reportId/reportUUID		上报id/上报UUID
 * 
 * 选填参数
 * 		operateUserInfo	操作用户，类型为cn.ffcs.uam.bo.UserInfo
 * 		instanceId		流程实例id
 * @ClassName:   ReportWorkflowDecisionMaking4EHDServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月6日 上午10:40:19
 */
@Service(value = "reportStatusDecisionMaking4EPCService")
public class ReportStatusDecisionMaking4EPCServiceImpl extends ReportStatusDecisionMaking4TwoVioPreServiceImpl {
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
			
			if(FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					|| FocusReportNode35201Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				if(PRO_STATUS_END.equals(proStatus)) {
					curNodeName = FocusReportNode352Enum.END_NODE_NAME.getTaskNodeName();
				} else if(!isRejectOperate && !isRecallOperate && CommonFunctions.isNotBlank(params, "nextNodeName")) {
					curNodeName = params.get("nextNodeName").toString();
				}
				
				if(FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
					statusMap = initReportStatusMap();
				} else if(FocusReportNode35201Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
					statusMap = initReportStatusMap_35201();
				}
				
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
		
		statusMap.put(FocusReportNode352Enum.START_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode352Enum.FIND_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode352Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode352Enum.VERIFY_TASK_GRID_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.VERIFY_STATUS.getReportStatus());
		statusMap.put(FocusReportNode352Enum.VERIFY_TASK_OTHER_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.VERIFY_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode352Enum.VICE_COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode352Enum.NOT_THE_GRID_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode352Enum.VICE_COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode352Enum.CENTRALIZED_ISOLATION_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode352Enum.NUCLEIC_ACID_DETECTION_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode352Enum.NOT_THE_STREET_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.HANDLE_STATUS.getReportStatus());

		statusMap.put(FocusReportNode352Enum.FOREIGN_AFFAIRS_GROUP_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode352Enum.EPC_GROUP_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode352Enum.BIG_DATA_GROUP_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.HANDLE_STATUS.getReportStatus());

		statusMap.put(FocusReportNode352Enum.GRID_HANDLE_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.FOLLOW_SUPERVISE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode352Enum.END_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.END_STATUS.getReportStatus());
		
		return statusMap;
	}
	
	/**
	 * 南安疫情防控信息_小型医疗机构
	 * @return
	 */
	private Map<String, String> initReportStatusMap_35201() {
		Map<String, String> statusMap = new HashMap<String, String>();
		
		statusMap.put(FocusReportNode35201Enum.START_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35201Enum.FIND_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35201Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode35201Enum.STREET_NAD_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35201Enum.STREET_HOSPITAL_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35201Enum.DISTRICT_HOSPITAL_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35201Enum.DISTRICT_NAD_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.HANDLE_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode352Enum.END_NODE_NAME.getTaskNodeName(), EPCReportStatusEnum.END_STATUS.getReportStatus());
		
		return statusMap;
	}
}
