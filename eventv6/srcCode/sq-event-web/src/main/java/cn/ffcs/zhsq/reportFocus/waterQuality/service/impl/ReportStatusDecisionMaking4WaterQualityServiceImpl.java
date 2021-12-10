package cn.ffcs.zhsq.reportFocus.waterQuality.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportStatusDecisionMaking4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 流域水质状态决策类
 * 必填参数
 * 		curNodeName		当前环节名称
 * 		nextNodeName	下一环节名称
 * 		reportId/reportUUID		上报id/上报UUID
 * 
 * 选填参数
 * 		operateUserInfo	操作用户，类型为cn.ffcs.uam.bo.UserInfo
 * 		instanceId		流程实例id 
 * @ClassName:   ReportStatusDecisionMaking4WaterQualityServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月19日 下午3:22:45
 */
@Service(value = "reportStatusDecisionMaking4WaterQualityService")
public class ReportStatusDecisionMaking4WaterQualityServiceImpl extends ReportStatusDecisionMaking4TwoVioPreServiceImpl {
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
				curNodeName = FocusReportNode35401Enum.END_NODE_NAME.getTaskNodeName();
			} else if(!isRejectOperate && !isRecallOperate && CommonFunctions.isNotBlank(params, "nextNodeName")) {
				curNodeName = params.get("nextNodeName").toString();
			}
			
			if(FocusReportNode35401Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				statusMap = initReportStatusMap();
			} else if(FocusReportNode35402Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				statusMap = initReportStatusMap_35402();
			} else if(FocusReportNode35403Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				statusMap = initReportStatusMap_35403();
			}
			
			if(CommonFunctions.isNotBlank(statusMap, curNodeName)) {
				reportStatus = statusMap.get(curNodeName);
			}
		}
		
		return reportStatus;
	}
	
	/**
	 * 南安流域水质信息流程-网格员
	 * 构造上报初始化状态
	 * @return
	 */
	protected Map<String, String> initReportStatusMap() {
		Map<String, String> statusMap = new HashMap<String, String>();
		
		statusMap.put(FocusReportNode35401Enum.START_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35401Enum.FIND_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35401Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35401Enum.VERIFY_TASK_GRID_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.VERIFY_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode35401Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35401Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35401Enum.BEE_HANDLE_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.HANDLE_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode35401Enum.FILE_CASE_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.FILE_CASE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35401Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.ADMINISTRTIVE_SACTION_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode35401Enum.END_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.END_STATUS.getReportStatus());
		
		return statusMap;
	}
	
	/**
	 * 南安流域水质信息流程-非网格员
	 * 构造上报初始化状态
	 * @return
	 */
	private Map<String, String> initReportStatusMap_35402() {
		Map<String, String> statusMap = new HashMap<String, String>();
		
		statusMap.put(FocusReportNode35402Enum.START_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35402Enum.FIND_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35402Enum.BEE_BRIGADE_DISTRIBUTE_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35402Enum.BEE_SQUADRON_VERIFY_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.VERIFY_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode35402Enum.BEE_BRIGADE_HANDLE_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.HANDLE_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode35402Enum.FILE_CASE_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.FILE_CASE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35402Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.ADMINISTRTIVE_SACTION_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode35402Enum.END_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.END_STATUS.getReportStatus());
		
		return statusMap;
	}
	
	/**
	 * 南安流域水质信息流程-突发
	 * 构造上报初始化状态
	 * @return
	 */
	private Map<String, String> initReportStatusMap_35403() {
		Map<String, String> statusMap = new HashMap<String, String>();
		
		statusMap.put(FocusReportNode35403Enum.START_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35403Enum.FIND_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.DRAFT_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode35403Enum.BEE_HANDLE_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.HANDLE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35403Enum.BEE_HANDLE_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.HANDLE_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode35403Enum.FILE_CASE_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.FILE_CASE_STATUS.getReportStatus());
		statusMap.put(FocusReportNode35403Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.ADMINISTRTIVE_SACTION_STATUS.getReportStatus());
		
		statusMap.put(FocusReportNode35403Enum.END_NODE_NAME.getTaskNodeName(), WaterQualityReportStatusEnum.END_STATUS.getReportStatus());
		
		return statusMap;
	}
}
