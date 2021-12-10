package cn.ffcs.zhsq.reportFocus.forestFirePrevention.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportStatusDecisionMaking4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 森林防灭火状态决策类
 * 必填参数
 * 		curNodeName		当前环节名称
 * 		nextNodeName	下一环节名称
 * 		reportId/reportUUID		上报id/上报UUID
 * 
 * 选填参数
 * 		operateUserInfo	操作用户，类型为cn.ffcs.uam.bo.UserInfo
 * 		instanceId		流程实例id 
 * @ClassName:   ReportStatusDecisionMaking4FFPServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年3月31日 下午7:48:36
 */
@Service(value = "reportStatusDecisionMaking4FFPService")
public class ReportStatusDecisionMaking4FFPServiceImpl extends ReportStatusDecisionMaking4TwoVioPreServiceImpl {
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
			
			if(FocusReportNode358Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				if(PRO_STATUS_END.equals(proStatus)) {
					curNodeName = FocusReportNode358Enum.END_NODE_NAME.getTaskNodeName();
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
		
		//发现隐患
		statusMap.put(FocusReportNode358Enum.START_NODE_NAME.getTaskNodeName(), FFPReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode358Enum.FIND_NODE_NAME.getTaskNodeName(), FFPReportStatusEnum.DRAFT_STATUS.getReportStatus());
		
		//任务派发
		statusMap.put(FocusReportNode358Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName(), FFPReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());
		
		//现场核实
		statusMap.put(FocusReportNode358Enum.VERIFY_NODE_NAME.getTaskNodeName(), FFPReportStatusEnum.VERIFY_STATUS.getReportStatus());
		
		//一级网格处置
		statusMap.put(FocusReportNode358Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName(), FFPReportStatusEnum.COMMUNITY_HANDLE_STATUS.getReportStatus());
		
		//镇级增员处置
		statusMap.put(FocusReportNode358Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName(), FFPReportStatusEnum.STREET_HANDLE_STATUS.getReportStatus());
		
		//市级支援处置
		statusMap.put(FocusReportNode358Enum.DISTRICT_HANDLE_NODE_NAME.getTaskNodeName(), FFPReportStatusEnum.DISTRICT_HANDLE_STATUS.getReportStatus());
		
		//镇级处置
		statusMap.put(FocusReportNode358Enum.STREET_ARCHIVE_NODE_NAME.getTaskNodeName(), FFPReportStatusEnum.STREET_ARCHIVE_STATUS.getReportStatus());
		
		//办结归档
		statusMap.put(FocusReportNode358Enum.END_NODE_NAME.getTaskNodeName(), FFPReportStatusEnum.END_STATUS.getReportStatus());
		
		return statusMap;
	}
}
