package cn.ffcs.zhsq.reportFocus.enterpriseHiddenDanger.service.impl;

import java.util.Map;
import org.springframework.stereotype.Service;

import cn.ffcs.elastic.util.CommonFunctions;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;

/**
 * @Description: 企业安全隐患节点扭转决策类
 * @ClassName:   ReportWorkflowDecisionMaking4EHDServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年4月30日 下午8:20:11
 */
@Service(value = "reportWorkflowDecisionMaking4EHDService")
public class ReportWorkflowDecisionMaking4EHDServiceImpl implements IWorkflowDecisionMakingService<String> {

	@SuppressWarnings("unchecked")
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		String curNodeName = null,
			   isHiddenDanger = null,
			   HIDDEN_DANGER_NOT_EXIST = "0",
			   nextNodeName = null;
		
		if(CommonFunctions.isNotBlank(params, "curNodeName")) {
			curNodeName = params.get("curNodeName").toString();
		}
		
		if(CommonFunctions.isNotBlank(params, "reportFocusMap")) {
			Map<String, Object> reportFocusMap = (Map<String, Object>) params.get("reportFocusMap");
			
			if(CommonFunctions.isNotBlank(reportFocusMap, "isHiddenDanger")) {
				isHiddenDanger = reportFocusMap.get("isHiddenDanger").toString();
			}
		} else if(CommonFunctions.isNotBlank(params, "isHiddenDanger")) {
			isHiddenDanger = params.get("isHiddenDanger").toString();
		}
		
		if(FocusReportNode351Enum.FIND_EHD_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
			if(HIDDEN_DANGER_NOT_EXIST.equals(isHiddenDanger)) {
				nextNodeName = FocusReportNode351Enum.COMMUNITY_END_NODE_NAME.getTaskNodeName();
			} else {
				nextNodeName = FocusReportNode351Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName();
			}
		}
		
		return nextNodeName;
	}

}
