package cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl;

import java.util.Map;
import org.springframework.stereotype.Service;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;

/**
 * @Description: 两违防治节点扭转决策类
 * 必填参数
 * 		instanceId	流程实例id
 * @ClassName:   ReportWorkflowDecisionMaking4TwoVioPreServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年9月15日 下午4:24:56
 */
@Service(value = "reportWorkflowDecisionMaking4TwoVioPreService")
public class ReportWorkflowDecisionMaking4TwoVioPreServiceImpl implements IWorkflowDecisionMakingService<String> {

	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		return null;
	}
}
