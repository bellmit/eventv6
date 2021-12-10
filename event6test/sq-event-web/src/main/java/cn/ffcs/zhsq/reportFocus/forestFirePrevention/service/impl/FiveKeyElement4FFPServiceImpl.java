package cn.ffcs.zhsq.reportFocus.forestFirePrevention.service.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.FiveKeyElement4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 森林防灭火人员选择
 * @ClassName:   FiveKeyElement4FFPServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年3月31日 上午10:36:55
 */
@Service(value = "fiveKeyElement4FFPService")
public class FiveKeyElement4FFPServiceImpl extends FiveKeyElement4TwoVioPreServiceImpl {
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private IReportFocusService reportFocusService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String reportType = ReportTypeEnum.forestFirePrevention.getReportTypeIndex(), formTypeIdStr = null;
		Long instanceId = null, reportId = null;
		ProInstance proInstance = null;
		StringBuffer wfEndNodeName = new StringBuffer("");
		
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isBlank(params, "reportType")) {
			params.put("reportType", reportType);
		}
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {}
		}
		
		if(instanceId != null && instanceId > 0) {
			proInstance = baseWorkflowService.findProByInstanceId(instanceId);
		}
		
		if(proInstance != null) {
			formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
			reportId = proInstance.getFormId();
		}
		
		wfEndNodeName.append(curnodeName).append("->");
		
		if(FocusReportNode358Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& FocusReportNode358Enum.FIND_NODE_NAME.getTaskNodeName().equals(curnodeName)
				&& FocusReportNode358Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName().equals(nodeName)) {
			String dataSource = null;
			
			if(CommonFunctions.isNotBlank(params, "dataSource")) {
				dataSource = params.get("dataSource").toString();
			} else if(reportId != null && reportId > 0) {
				Map<String, Object> reportParams = new HashMap<String, Object>(),
									reportFocusMap = null;
				
				reportParams.putAll(params);
				reportParams.put("reportId", reportId);
				
				reportFocusMap = reportFocusService.findReportFocusByUUID(null, userInfo, reportParams);
				
				if(CommonFunctions.isNotBlank(reportFocusMap, "dataSource")) {
					dataSource = reportFocusMap.get("dataSource").toString();
				}
			}
			
			wfEndNodeName.append(nodeName).append("-").append(dataSource);
		} else {
			wfEndNodeName.append(nodeName);
		}
		
		params.put("wfEndNodeName", wfEndNodeName.toString());
		resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		
		if(FocusReportNode358Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			boolean isShowText = false, isShowDict = false, isShowNumInput = false;
			String textLabelName = "", dictLabelName = "", numInputLabelName = "";
			Map<String, Object> dynamicContentMap = new HashMap<String, Object>();
			
			if(CommonFunctions.isNotBlank(resultMap, "dynamicContentMap")) {
				dynamicContentMap.putAll((Map<String, String>) resultMap.get("dynamicContentMap"));
			}
			
			if(FocusReportNode358Enum.VERIFY_NODE_NAME.getTaskNodeName().equals(curnodeName)
					&& FocusReportNode358Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)) {
				String dynamicDictJson = "[{'name':'不属实', 'value':'0'},{'name':'已扑灭', 'value':'1'}]";
				
				isShowDict = true;
				dictLabelName = "火灾情况";
				
				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
			} else if(FocusReportNode358Enum.DISTRICT_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)
					&& FocusReportNode358Enum.STREET_ARCHIVE_NODE_NAME.getTaskNodeName().equals(nodeName)) {
				isShowNumInput = true;
				numInputLabelName = "扑火队数";
			}
			
			dynamicContentMap.put("isShowText", isShowText);
			dynamicContentMap.put("isShowDict", isShowDict);
			dynamicContentMap.put("isShowNumInput", isShowNumInput);
			dynamicContentMap.put("textLabelName", textLabelName);
			dynamicContentMap.put("dictLabelName", dictLabelName);
			dynamicContentMap.put("numInputLabelName", numInputLabelName);
			
			resultMap.put("dynamicContentMap", dynamicContentMap);
		}
		
		return resultMap;
	}
	
	/**
	 * 获取消息模板触发条件
	 * @param proInstance
	 * @param params
	 * @return
	 */
	protected StringBuffer capAdviceNoteCondition(ProInstance proInstance, Map<String, Object> params) {
		StringBuffer trigCondition = new StringBuffer("");
		String curTaskName = null;
		
		if(proInstance != null) {
			curTaskName = proInstance.getCurNode();
		}
		
		trigCondition.append(ConstantValue.FOREST_FIRE_PREVENTION_NOTE).append("-").append(proInstance.getProName()).append("-").append(curTaskName);
		
		if(FocusReportNode358Enum.FIND_NODE_NAME.getTaskNodeName().equals(curTaskName)) {
			if(CommonFunctions.isNotBlank(params, "dataSource")) {
				trigCondition.append("-").append(params.get("dataSource"));
			}
		}else if(CommonFunctions.isNotBlank(params, "nodeName")) {
			trigCondition.append("-").append(params.get("nodeName"));
		}
		
		return trigCondition;
	}
	
	protected String capAdviceNote(String adviceNote, UserInfo userInfo, Map<String, Object> params) {
		adviceNote = super.capAdviceNote(adviceNote, userInfo, params);
		
		if(adviceNote.contains("@collectSourceName@")) {
			String collectSourceName = "";
			
			if(CommonFunctions.isNotBlank(params, "collectSourceName")){
				collectSourceName = params.get("collectSourceName").toString();
			}
			
			adviceNote = adviceNote.replace("@collectSourceName@", collectSourceName);
		}
		
		return adviceNote;
	}
}
