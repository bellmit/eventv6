package cn.ffcs.zhsq.reportFocus.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 重点关注上报状态决策类
 * 必填参数
 * 		curNodeName		当前环节名称
 * 		nextNodeName	下一环节名称
 * 		reportId/reportUUID		上报id/上报UUID
 * 		reportType		上报类型
 * 
 * 选填参数
 * 		operateUserInfo	操作用户，类型为cn.ffcs.uam.bo.UserInfo
 * 		instanceId		流程实例id
 * @ClassName:   ReportStatusDecisionMakingServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年9月16日 上午9:37:27
 */
@Service(value = "reportStatusDecisionMakingService")
public class ReportStatusDecisionMakingServiceImpl extends
		ApplicationObjectSupport implements IWorkflowDecisionMakingService<String> {
	@Autowired
	private IReportFocusService reportFocusService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		//为了减少无用属性的更新，故重新创建一个新的入参，而不直接使用params
		Map<String, Object> reportParams = new HashMap<String, Object>();
		
		if(CommonFunctions.isNotBlank(params, "reportId")) {
			reportParams.put("reportId", params.get("reportId"));
		} else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
			reportParams.put("reportUUID", params.get("reportUUID"));
		} else if(CommonFunctions.isNotBlank(params, "instanceId")) {
			Long instanceId = Long.valueOf(params.get("instanceId").toString());
			if(instanceId != null && instanceId > 0) {
				ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
				if(proInstance != null) {
					Long reportId = proInstance.getFormId();
					if(reportId != null && reportId > 0) {
						reportParams.put("reportId", reportId);
					}
				}
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportParams.put("reportType", params.get("reportType"));
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】，无法更新上报信息！");
		}
		
		if(CommonFunctions.isBlank(reportParams, "reportId") && CommonFunctions.isBlank(reportParams, "reportUUID")) {
			throw new IllegalArgumentException("缺少reportId/reportUUID，无法更新上报信息！");
		}
		
		String reportStatus = capReportStaus(params);
		
		if(StringUtils.isNotBlank(reportStatus)) {
			UserInfo userInfo = null;
			String VALID_STATUS = "1";
			
			if(CommonFunctions.isNotBlank(params, "operateUserInfo")) {
				Object operateUserInfo = params.get("operateUserInfo");
				
				if(operateUserInfo instanceof UserInfo) {
					userInfo = (UserInfo) operateUserInfo;
				}
			} else {
				userInfo = new UserInfo();
			}
			
			reportParams.put("reportStatus", reportStatus);
			reportParams.put("reportValid", VALID_STATUS);
			reportParams.put("isSaveReportFocusExtend", false);
			reportFocusService.saveReportFocus(reportParams, userInfo);
		}
		
		return reportStatus;
	}
	
	/**
	 * 获取上报状态
	 * @param params
	 * @return
	 */
	protected String capReportStaus(Map<String, Object> params) {
		IWorkflowDecisionMakingService<String> reportStatusDecisionMakingService = null;
		String reportStatus = null;
		
		try {
			reportStatusDecisionMakingService = capReportStatusDecisionMakingService(params);
			reportStatus = reportStatusDecisionMakingService.makeDecision(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return reportStatus;
	}

	/**
	 * 获取上报状态决策接口
	 * @param params
	 * 			reportType	上报类型，1 两违防治；
	 * @return
	 * @throws Exception 
	 */
	private IWorkflowDecisionMakingService<String> capReportStatusDecisionMakingService(Map<String, Object> params) throws Exception {
		String reportType = null;
		IWorkflowDecisionMakingService<String> reportStatusDecisionMakingService = null;
		ReportServiceAgent<IWorkflowDecisionMakingService<String>> reportServiceAgent = null;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		
		reportServiceAgent = new ReportServiceAgent<IWorkflowDecisionMakingService<String>>(reportType, ReportServiceAgent.serviceTypeEnum.reportStatusDecison.getServiceTypeIndex());
		
		reportStatusDecisionMakingService = reportServiceAgent.capService();
		
		return reportStatusDecisionMakingService;
	}
}
