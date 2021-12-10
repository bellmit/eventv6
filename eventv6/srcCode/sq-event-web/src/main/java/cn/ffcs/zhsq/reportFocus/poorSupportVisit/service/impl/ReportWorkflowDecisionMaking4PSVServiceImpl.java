package cn.ffcs.zhsq.reportFocus.poorSupportVisit.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 扶贫走访节点扭转决策类
 * @ClassName:   ReportWorkflowDecisionMaking4PSVServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年12月4日 上午10:08:48
 */
@Service(value = "reportWorkflowDecisionMaking4PSVService")
public class ReportWorkflowDecisionMaking4PSVServiceImpl implements IWorkflowDecisionMakingService<String> {
	@Autowired
	private IReportFocusService reportFocusService;
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		String curNodeName = null, nextNodeName = null, collectSource = null;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "curNodeName")) {
			curNodeName = params.get("curNodeName").toString();
		}
		
		if(CommonFunctions.isNotBlank(params, "collectSource")) {
			collectSource = params.get("collectSource").toString();
		} else if(CommonFunctions.isNotBlank(params, "reportId") || CommonFunctions.isNotBlank(params, "reportUUID")) {
			Map<String, Object> reportMap = null;
			
			if(CommonFunctions.isBlank(params, "reportType")) {
				params.put("reportType", ReportTypeEnum.poorSupportVisit.getReportTypeIndex());
			}
			
			reportMap = reportFocusService.findReportFocusByUUID(null, null, params);
			
			if(CommonFunctions.isNotBlank(reportMap, "collectSource")) {
				collectSource = reportMap.get("collectSource").toString();
			}
		}
		
		if(FocusReportNode356Enum.DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
				|| FocusReportNode356Enum.DECISION_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
			if(PSVCollectSourceEnum.GRID_REPORT.getCollectSource().equals(collectSource)
					|| PSVCollectSourceEnum.COMMUNITY_REPORT.getCollectSource().equals(collectSource)) {
				nextNodeName = FocusReportNode356Enum.HRP_VERIFY_NODE_NAME.getTaskNodeName();
			} else {
				nextNodeName = FocusReportNode356Enum.COMMUNITY_VERIFY_NODE_NAME.getTaskNodeName();
			}
		}
		
		if(StringUtils.isBlank(nextNodeName)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		return nextNodeName;
	}

}
