package cn.ffcs.zhsq.reportFocus.businessProblem.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.reportFocus.businessProblem.service.impl.BusProReportStatusEnum;
import cn.ffcs.zhsq.reportFocus.businessProblem.service.impl.FocusReportNode359Enum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportStatusDecisionMaking4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 营商状态决策类
 * 必填参数
 * 		curNodeName		当前环节名称
 * 		nextNodeName	下一环节名称
 * 		reportId/reportUUID		上报id/上报UUID
 * 
 * 选填参数
 * 		operateUserInfo	操作用户，类型为cn.ffcs.uam.bo.UserInfo
 * 		instanceId		流程实例id 
 * @ClassName:   ReportStatusDecisionMaking4BusProServiceImpl   
 * @author:      郭昱丹(guoyd)
 * @date:        2021年5月7日 上午10:16:15
 */
@Service(value = "reportStatusDecisionMaking4BusProService")
public class ReportStatusDecisionMaking4BusProServiceImpl extends ReportStatusDecisionMaking4TwoVioPreServiceImpl {

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
			
			if(FocusReportNode359Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				if(PRO_STATUS_END.equals(proStatus)) {
					curNodeName = FocusReportNode359Enum.END_NODE_NAME.getTaskNodeName();
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
		statusMap.put(FocusReportNode359Enum.START_NODE_NAME.getTaskNodeName(), BusProReportStatusEnum.DRAFT_STATUS.getReportStatus());
		statusMap.put(FocusReportNode359Enum.FIND_NODE_NAME.getTaskNodeName(), BusProReportStatusEnum.DRAFT_STATUS.getReportStatus());
		
		//任务派发
		statusMap.put(FocusReportNode359Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName(), BusProReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());
		
		//牵头部门处置
		statusMap.put(FocusReportNode359Enum.LEAD_NODE_NAME.getTaskNodeName(), BusProReportStatusEnum.LEAD_STATUS.getReportStatus());
		
		//市直部门处置
		statusMap.put(FocusReportNode359Enum.MUNICIPAL_NODE_NAME.getTaskNodeName(), BusProReportStatusEnum.MUNICIPAL_STATUS.getReportStatus());
		
		//市招商办处置
		statusMap.put(FocusReportNode359Enum.OFFICE_NODE_NAME.getTaskNodeName(), BusProReportStatusEnum.OFFICE_STATUS.getReportStatus());
		
		//办结归档
		statusMap.put(FocusReportNode359Enum.END_NODE_NAME.getTaskNodeName(), BusProReportStatusEnum.END_STATUS.getReportStatus());
		
		//延期意见反馈
		statusMap.put(FocusReportNode359Enum.DELAY_NODE_NAME.getTaskNodeName(), BusProReportStatusEnum.DELAY_STATUS.getReportStatus());
		
		return statusMap;
	}
}
