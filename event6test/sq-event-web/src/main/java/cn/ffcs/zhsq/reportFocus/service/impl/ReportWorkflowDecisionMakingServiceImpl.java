package cn.ffcs.zhsq.reportFocus.service.impl;

import java.util.Map;

import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.elastic.util.CommonFunctions;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;

/**
 * @Description: 重点关注上报节点扭转决策类
 * 必填参数
 * 		instanceId	流程实例id 
 * @ClassName:   ReportWorkflowDecisionMakingServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年9月16日 上午9:43:21
 */
@Service(value = "reportWorkflowDecisionMakingService")
public class ReportWorkflowDecisionMakingServiceImpl extends ApplicationObjectSupport
		implements IWorkflowDecisionMakingService<String> {

	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		IWorkflowDecisionMakingService<String> reportWorkflowDecisionMakingService = capReportWorkflowDecisionMakingService(params);
		
		return reportWorkflowDecisionMakingService.makeDecision(params);
	}
	
	/**
	 * 获取上报节点决策类
	 * @param params
	 * 			reportType	上报类型，1 两违防治；
	 * @return
	 * @throws Exception 
	 */
	private IWorkflowDecisionMakingService<String> capReportWorkflowDecisionMakingService(Map<String, Object> params) throws Exception {
		String reportType = null;
		IWorkflowDecisionMakingService<String> reportWorkflowDecisionMakingService = null;
		ReportServiceAgent<IWorkflowDecisionMakingService<String>> reportServiceAgent = null;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		
		reportServiceAgent = new ReportServiceAgent<IWorkflowDecisionMakingService<String>>(reportType, ReportServiceAgent.serviceTypeEnum.reportNodeDecision.getServiceTypeIndex());
		
		reportWorkflowDecisionMakingService = reportServiceAgent.capService();
		
		return reportWorkflowDecisionMakingService;
	}

}
