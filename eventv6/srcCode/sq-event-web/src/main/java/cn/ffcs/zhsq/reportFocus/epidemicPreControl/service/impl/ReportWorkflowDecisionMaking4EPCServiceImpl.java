package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 疫情防控节点扭转决策类
 * @ClassName:   ReportWorkflowDecisionMaking4EPCServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月12日 下午2:33:39
 */
@Service(value = "reportWorkflowDecisionMaking4EPCService")
public class ReportWorkflowDecisionMaking4EPCServiceImpl implements IWorkflowDecisionMakingService<String> {
	@Autowired
	private IReportFocusService reportFocusService;

	@Autowired
	private OrgEntityInfoOutService orgEntityInfoService;
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		String curNodeName = null, nextNodeName = null, collectSource = null;
		params = params == null ? new HashMap<String, Object>() : params;
		Map<String, Object> reportMap = null;
		
		if(CommonFunctions.isNotBlank(params, "curNodeName")) {
			curNodeName = params.get("curNodeName").toString();
		}
		
		if(CommonFunctions.isNotBlank(params, "collectSource")) {
			collectSource = params.get("collectSource").toString();
		} else if(CommonFunctions.isNotBlank(params, "reportId") || CommonFunctions.isNotBlank(params, "reportUUID")) {
			
			if(CommonFunctions.isBlank(params, "reportType")) {
				params.put("reportType", ReportTypeEnum.epidemicPreControl.getReportTypeIndex());
			}

			reportMap = reportFocusService.findReportFocusByUUID(null, null, params);
			
			if(CommonFunctions.isNotBlank(reportMap, "collectSource")) {
				collectSource = reportMap.get("collectSource").toString();
			}
		}

		if(FocusReportNode352Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
				|| FocusReportNode352Enum.VERIFY_DECISION_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
			if(EPCCollectSourceEnum.GRID_INSPECT.getCollectSource().equals(collectSource)) {
				nextNodeName = FocusReportNode352Enum.VERIFY_TASK_GRID_NODE_NAME.getTaskNodeName();
			} else {
				nextNodeName = FocusReportNode352Enum.VERIFY_TASK_OTHER_NODE_NAME.getTaskNodeName();
			}
		}else if(FocusReportNode352Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)
				|| FocusReportNode352Enum.VERIFY_DECISION_NODE_NAME2.getTaskNodeName().equals(curNodeName)){
			//决策2
			//判断所属区域编码层级：如果是到村社区及以下的，下一节点为 task2 村社区处理，否则为对应的 task13 14 15
			OrgSocialInfoBO orgInfo = null;
			OrgEntityInfoBO orgEntityInfo = null;
			String orgLevel = null;
			if(CommonFunctions.isNotBlank(reportMap, "regionCode")){
				orgEntityInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(reportMap.get("regionCode").toString());
			}else if(CommonFunctions.isNotBlank(params, "regionCode")){
				orgEntityInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(params.get("regionCode").toString());
			}
			if(null != orgEntityInfo){
				orgLevel = orgEntityInfo.getChiefLevel();
			}

			if(StringUtils.isNotBlank(orgLevel)){
				if(orgLevel.compareTo(ConstantValue.STREET_ORG_LEVEL) <= 0){
					//来源是市外事组02 task13
					if(EPCCollectSourceEnum.FOREIGN_AFFAIRS_SECTION.getCollectSource().equals(collectSource)) {
						nextNodeName = FocusReportNode352Enum.FOREIGN_AFFAIRS_GROUP_NODE_NAME.getTaskNodeName();
					} else if(EPCCollectSourceEnum.EPIDEMINC_PRE_CONTROL.getCollectSource().equals(collectSource)){
						//市疫情防控组03 task14
						nextNodeName = FocusReportNode352Enum.EPC_GROUP_NODE_NAME.getTaskNodeName();
					}else if(EPCCollectSourceEnum.BIG_DATA_GROUP.getCollectSource().equals(collectSource)){
						//市大数据组的04 task15
						nextNodeName = FocusReportNode352Enum.BIG_DATA_GROUP_NODE_NAME.getTaskNodeName();
					}
				}else {
					nextNodeName = FocusReportNode352Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName();
				}
			}else{
				nextNodeName = FocusReportNode352Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName();
			}
		}
		
		if(StringUtils.isBlank(nextNodeName)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		return nextNodeName;
	}

}
