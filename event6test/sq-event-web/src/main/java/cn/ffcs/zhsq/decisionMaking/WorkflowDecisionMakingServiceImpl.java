package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.utils.CommonFunctions;

@Service(value = "workflowDecisionMakingService")
public class WorkflowDecisionMakingServiceImpl extends ApplicationObjectSupport
		implements IWorkflowDecisionMakingService<Object> {

	@Override
	public Object makeDecision(Map<String, Object> params) throws Exception {
		Object decision = null;
		
		if(CommonFunctions.isNotBlank(params, "decisionService")) {
			String decisionService = params.get("decisionService").toString();
			IWorkflowDecisionMakingService<Object> workflowDecisionMakingService = (IWorkflowDecisionMakingService<Object>)this.getApplicationContext().getBean(decisionService);
			decision = workflowDecisionMakingService.makeDecision(params);
		} else {
			throw new IllegalArgumentException("缺失参数[decisionService]，请检查！");
		}
		
		return decision;
	}

}
