package cn.ffcs.zhsq.reportFocus.ruralHousing.service.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.FiveKeyElement4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 农村建房人员选择
 * @ClassName:   FiveKeyElement4RuralHousingServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年3月8日 下午7:48:59
 */
@Service(value = "fiveKeyElement4RuralHousingService")
public class FiveKeyElement4RuralHousingServiceImpl extends FiveKeyElement4TwoVioPreServiceImpl {
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		INodeCodeHandler nodeCodeHandler = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String reportType = ReportTypeEnum.ruralHousing.getReportTypeIndex(), formTypeIdStr = null;
		Long instanceId = null;
		ProInstance proInstance = null;
		
		params = params == null ? new HashMap<String, Object>() : params;
		
		nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		
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
		}
		
		params.put("wfEndNodeName", curnodeName + "->" + nodeName);
		resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		
		if(nodeCodeHandler.isEquality()) {
			resultMap.put("userIds", userInfo.getUserId());
			resultMap.put("userNames", userInfo.getPartyName());
			resultMap.put("orgIds", userInfo.getOrgId());
			resultMap.put("orgNames", userInfo.getOrgName());
			
			resultMap.put("isSelectUser", false);
			resultMap.put("isSelectOrg", false);
			resultMap.put("isUserRadioStyle", true);
		} else if(nodeCodeHandler.isToBegin()) {
			if(proInstance != null) {
				resultMap.put("userIds", proInstance.getUserId());
				resultMap.put("userNames", proInstance.getUserName());
				resultMap.put("orgIds", proInstance.getOrgId());
				resultMap.put("orgNames", proInstance.getOrgName());
				
				resultMap.put("isSelectUser", false);
				resultMap.put("isSelectOrg", false);
				resultMap.put("isUserRadioStyle", true);
			}
		}
		
		if(FocusReportNode357Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			boolean isShowText = false, isShowDict = false, isShowNumInput = false;
			String textLabelName = "", dictLabelName = "", numInputLabelName = "", numInputOption = "";
			Map<String, Object> dynamicContentMap = new HashMap<String, Object>();
			
			if(CommonFunctions.isNotBlank(resultMap, "dynamicContentMap")) {
				dynamicContentMap.putAll((Map<String, String>) resultMap.get("dynamicContentMap"));
			}
			
			if(FocusReportNode357Enum.LOFTING_START_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				String dynamicDictJson = "[{'name':'申请建筑放样', 'value':'0'},{'name':'巡查发现已达放样节点', 'value':'1'}]";
				
				isShowDict = true;
				dictLabelName = "提请类型";
				
				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
			} else if(FocusReportNode357Enum.LINE_CHECK_START_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				String dynamicDictJson = "[{'name':'申请基槽验线', 'value':'0'},{'name':'巡查发现已达基槽验线节点', 'value':'1'}]";
				
				isShowDict = true;
				dictLabelName = "提请类型";
				
				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
			} else if(FocusReportNode357Enum.LINE_CHECK_ORGANIZE_NODE_NAME.getTaskNodeName().equals(curnodeName)
					&& FocusReportNode357Enum.LINE_CHECK_REFORM_NODE_NAME.getTaskNodeName().equals(nodeName)) {
				isShowText = true;
				textLabelName = "存在问题";
			} else if(FocusReportNode357Enum.CONSTRUCTION_ACCEPTANCE_START_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				String dynamicDictJson = "[{'name':'申请楼面施工节点验收', 'value':'0'},{'name':'巡查发现已达楼面施工验收节点', 'value':'1'}]";
				
				isShowDict = true;
				isShowNumInput = true;
				dictLabelName = "提请类型";
				numInputLabelName = "验收楼层";
				
				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
			} else if(FocusReportNode357Enum.CONSTRUCTION_ACCEPTANCE_ORGANIZE_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				isShowNumInput = true;
				numInputLabelName = "验收楼层";
				numInputOption = "{'attrId': 'storey', 'isEditable': false}";
				
				if(FocusReportNode357Enum.CONSTRUCTION_ACCEPTANCE_REFORM_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					isShowText = true;
					textLabelName = "存在问题";
				}
				
				dynamicContentMap.put("numInputOption", numInputOption);
			} else if(FocusReportNode357Enum.CONSTRUCTION_ACCEPTANCE_REFORM_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				isShowNumInput = true;
				numInputLabelName = "验收楼层";
				numInputOption = "{'attrId': 'storey', 'isEditable': false}";
				
				dynamicContentMap.put("numInputOption", numInputOption);
			} else if(FocusReportNode357Enum.COMPLETION_ACCEPTANCE_START_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				String dynamicDictJson = "[{'name':'申请竣工验收', 'value':'0'},{'name':'巡查发现已达竣工验收节点', 'value':'1'}]";
				
				isShowDict = true;
				dictLabelName = "提请类型";
				
				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
			} else if(FocusReportNode357Enum.COMPLETION_ACCEPTANCE_ORGANIZE_NODE_NAME.getTaskNodeName().equals(curnodeName)
					&& FocusReportNode357Enum.COMPLETION_ACCEPTANCE_REFORM_NODE_NAME.getTaskNodeName().equals(nodeName)) {
				isShowText = true;
				textLabelName = "存在问题";
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
		
		trigCondition.append(ConstantValue.RURAL_HOUSING_NOTE).append("-").append(proInstance.getProName()).append("-").append(curTaskName);
		
		if(CommonFunctions.isNotBlank(params, "nodeName")) {
			trigCondition.append("-").append(params.get("nodeName"));
		}
		
		return trigCondition;
	}
	
	protected String capAdviceNote(String adviceNote, UserInfo userInfo, Map<String, Object> params) {
		adviceNote = super.capAdviceNote(adviceNote, userInfo, params);
		
		if(adviceNote.contains("@householder@")) {
			String householder = "";
			
			if(CommonFunctions.isNotBlank(params, "householder")){
				householder = params.get("householder").toString();
			}
			
			adviceNote = adviceNote.replace("@householder@", householder);
		}
		
		if(adviceNote.contains("@rcpCode@")) {
			String rcpCode = "";
			
			if(CommonFunctions.isNotBlank(params, "rcpCode")){
				rcpCode = params.get("rcpCode").toString();
			}
			
			adviceNote = adviceNote.replace("@rcpCode@", rcpCode);
		}
		
		if(adviceNote.contains("@rhaCode@")) {
			String rhaCode = "";
			
			if(CommonFunctions.isNotBlank(params, "rhaCode")){
				rhaCode = params.get("rhaCode").toString();
			}
			
			adviceNote = adviceNote.replace("@rhaCode@", rhaCode);
		}
		
		if(adviceNote.contains("@storey@") || adviceNote.contains("@nextStorey@")) {
			Integer storey = 0;
			
			if(CommonFunctions.isNotBlank(params, "storey")) {
				try {
					storey = Integer.valueOf(params.get("storey").toString());
				} catch(NumberFormatException e) {}
			}
			
			adviceNote = adviceNote.replace("@storey@", storey.toString());
			adviceNote = adviceNote.replace("@nextStorey@", String.valueOf(storey + 1));
		}
		
		return adviceNote;
	}
}
