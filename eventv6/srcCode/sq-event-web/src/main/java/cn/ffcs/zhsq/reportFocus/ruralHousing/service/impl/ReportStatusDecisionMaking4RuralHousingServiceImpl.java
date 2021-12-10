package cn.ffcs.zhsq.reportFocus.ruralHousing.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportStatusDecisionMaking4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 农村建房状态决策类
 * 必填参数
 * 		curNodeName		当前环节名称
 * 		nextNodeName	下一环节名称
 * 		reportId/reportUUID		上报id/上报UUID
 * 
 * 选填参数
 * 		operateUserInfo	操作用户，类型为cn.ffcs.uam.bo.UserInfo
 * 		instanceId		流程实例id
 * @ClassName:   ReportStatusDecisionMaking4RuralHousingServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年3月8日 下午6:33:15
 */
@Service(value = "reportStatusDecisionMaking4RuralHousingService")
public class ReportStatusDecisionMaking4RuralHousingServiceImpl extends ReportStatusDecisionMaking4TwoVioPreServiceImpl {
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
			
			if(FocusReportNode357Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				if(PRO_STATUS_END.equals(proStatus)) {
					curNodeName = FocusReportNode357Enum.END_NODE_NAME.getTaskNodeName();
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
		
		//签订告知书
		statusMap.put(FocusReportNode357Enum.START_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode357Enum.SIGN_NOTIFICATION_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.DRAFT_STATUS.getReportStatus());
		
		//建筑放样到场
		statusMap.put(FocusReportNode357Enum.LOFTING_START_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.LOFTING_STATUS.getReportStatus());
		statusMap.put(FocusReportNode357Enum.LOFTING_ORGANIZE_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.LOFTING_STATUS.getReportStatus());
		
		//基槽验线到场
		statusMap.put(FocusReportNode357Enum.LINE_CHECK_START_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.LINE_CHECK_STATUS.getReportStatus());
		statusMap.put(FocusReportNode357Enum.LINE_CHECK_ORGANIZE_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.LINE_CHECK_STATUS.getReportStatus());
		statusMap.put(FocusReportNode357Enum.LINE_CHECK_REFORM_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.LINE_CHECK_STATUS.getReportStatus());
		
		//施工节点到场
		statusMap.put(FocusReportNode357Enum.CONSTRUCTION_ACCEPTANCE_START_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.CONSTRUCTION_STATUS.getReportStatus());
		statusMap.put(FocusReportNode357Enum.CONSTRUCTION_ACCEPTANCE_ORGANIZE_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.CONSTRUCTION_STATUS.getReportStatus());
		statusMap.put(FocusReportNode357Enum.CONSTRUCTION_ACCEPTANCE_REFORM_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.CONSTRUCTION_STATUS.getReportStatus());
		
		//竣工验收到场
		statusMap.put(FocusReportNode357Enum.COMPLETION_ACCEPTANCE_START_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.COMPLETION_STATUS.getReportStatus());
		statusMap.put(FocusReportNode357Enum.COMPLETION_ACCEPTANCE_ORGANIZE_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.COMPLETION_STATUS.getReportStatus());
		statusMap.put(FocusReportNode357Enum.COMPLETION_ACCEPTANCE_REFORM_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.COMPLETION_STATUS.getReportStatus());
		
		//资料移交
		statusMap.put(FocusReportNode357Enum.COMPLETION_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.TRANSFER_STATUS.getReportStatus());
		
		//办结销号
		statusMap.put(FocusReportNode357Enum.END_NODE_NAME.getTaskNodeName(), RuralHousingReportStatusEnum.END_STATUS.getReportStatus());
		
		return statusMap;
	}
}
