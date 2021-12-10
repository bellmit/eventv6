package cn.ffcs.zhsq.reportFocus.waterQuality.service.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.FiveKeyElement4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 流域水质(Water Quality)人员选择
 * @ClassName:   FiveKeyElement4WaterQualityServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月19日 下午3:43:30
 */
@Service(value = "fiveKeyElement4WaterQualityService")
public class FiveKeyElement4WaterQualityServiceImpl extends FiveKeyElement4TwoVioPreServiceImpl {
	@Autowired
	private IReportIntegrationService reportIntegrationService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		String reportType = ReportTypeEnum.waterQuality.getReportTypeIndex(), formTypeIdStr = null;
		Map<String, Object> resultMap = null;
		params = params == null ? new HashMap<String, Object>() : params;
		boolean isUseSelfAnalysis = false;
		Long reportId = null;
		
		if(CommonFunctions.isNotBlank(params, "formId")) {
			try {
				reportId = Long.valueOf(params.get("formId").toString());
			} catch(NumberFormatException e) {}
		}
		
		if(CommonFunctions.isNotBlank(params, "formTypeId")) {
			formTypeIdStr = params.get("formTypeId").toString();
		} else if(CommonFunctions.isNotBlank(params, "instanceId")) {
			Long instanceId = Long.valueOf(params.get("instanceId").toString());
			ProInstance proInstance = null;
			
			if(instanceId != null && instanceId > 0) {
				proInstance = baseWorkflowService.findProByInstanceId(instanceId);
				
				if(proInstance != null) {
					formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
					
					if(reportId == null || reportId <= 0) {
						reportId = proInstance.getFormId();
					}
				}
			}
		}
		
		if(CommonFunctions.isBlank(params, "reportType")) {
			params.put("reportType", reportType);
		}
		
		isUseSelfAnalysis = 
				(nodeCodeHandler.isFromDept() && nodeCodeHandler.isSend() && nodeCodeHandler.isToDept())
				|| (FocusReportNode35402Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
						&& FocusReportNode35402Enum.BEE_BRIGADE_DISTRIBUTE_NODE_NAME.getTaskNodeName().equals(nodeName));
		
		if(isUseSelfAnalysis) {
			params.put("isAnalysisOperateUser", false);
		}
		
		if(FocusReportNode35402Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& FocusReportNode35402Enum.BEE_BRIGADE_DISTRIBUTE_NODE_NAME.getTaskNodeName().equals(nodeName)) {
			Map<String, Object> reportParams = new HashMap<String, Object>(), reportMap = null;
			String dataSource = null, wfEndNodeName = null;
			
			reportParams.putAll(params);
			reportParams.put("reportId", reportId);
			
			reportMap = reportIntegrationService.findReportFocusByUUID(null, userInfo, reportParams);
			
			if(CommonFunctions.isNotBlank(reportMap, "dataSource")) {
				dataSource = reportMap.get("dataSource").toString();
			}

			if(CommonFunctions.isBlank(params, "wfEndNodeName")) {
				wfEndNodeName = nodeName + "-" + dataSource;
			}
			
			params.put("wfEndNodeName", wfEndNodeName);
		}
		
		resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		
		if(nodeCodeHandler.isFromDept() && nodeCodeHandler.isSend() && nodeCodeHandler.isToDept()) {
			resultMap.put("isSelectOrg", true);
			resultMap.put("isUserRadioStyle", false);
			resultMap.put("eventNodeCode", nodeCodeHandler);
		} else if(FocusReportNode35402Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& FocusReportNode35402Enum.FIND_NODE_NAME.getTaskNodeName().equals(curnodeName)
				&& FocusReportNode35402Enum.BEE_BRIGADE_DISTRIBUTE_NODE_NAME.getTaskNodeName().equals(nodeName)) {
			resultMap.put("isSelectOrg", false);
			resultMap.put("isUserRadioStyle", true);
			resultMap.putAll(capBeeBrigadeOperator(null, userInfo, params));
		}
		
		boolean isShowText = false, isShowDict = false;
		String textLabelName = "", dictLabelName = "";
		Map<String, Object> dynamicContentMap = new HashMap<String, Object>();
		
		if(FocusReportNode35401Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(
					(FocusReportNode35401Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)
							&& FocusReportNode35401Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName))
					|| (FocusReportNode35401Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)
							&& FocusReportNode35401Enum.BEE_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName))
					|| (FocusReportNode35401Enum.BEE_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)
							&& FocusReportNode35401Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(nodeName))) {
				String dynamicDictJson = "[{'name':'水质正常', 'value':'0'},{'name':'水质异常', 'value':'1'}]";
				
				isShowDict = true;
				dictLabelName = "水质情况";
				
				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
				
				if(FocusReportNode35401Enum.BEE_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)
						&& FocusReportNode35401Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					isShowText = true;
					textLabelName = "配合部门";
				}
			} else if(FocusReportNode35401Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				isShowDict = true;
				isShowText = true;
				textLabelName = "补充描述";
				dictLabelName = "行政处罚";
				
				dynamicContentMap.put("dictPcode", WaterQualityDictEnum.ADMINISTRTIVE_SACTION.getDictCode());
				dynamicContentMap.put("isMultiSelect", true);
			}
		} else if(FocusReportNode35402Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode35402Enum.FIND_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				String dataSource = null;
				
				if(CommonFunctions.isNotBlank(params, "dataSource")) {
					dataSource = params.get("dataSource").toString();
				}
				
				if(WaterQualityDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(dataSource)) {
					isShowText = true;
					textLabelName = "交办部门";
				}
			} else if(FocusReportNode35402Enum.BEE_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)
					&& FocusReportNode35402Enum.BEE_BRIGADE_DISTRIBUTE_NODE_NAME.getTaskNodeName().equals(nodeName)) {
				resultMap.putAll(findNextUserByOrgId(userInfo.getOrgId(), nodeCode, params));
			} else if(FocusReportNode35402Enum.BEE_BRIGADE_DISTRIBUTE_NODE_NAME.getTaskNodeName().equals(curnodeName)
					&& FocusReportNode35402Enum.BEE_SQUADRON_VERIFY_NODE_NAME.getTaskNodeName().equals(nodeName)) {
				resultMap.put("nodeCode", nodeCode);
				resultMap.put("eventNodeCode", this.createNodeCodeHandle(nodeCode));
				resultMap.put("isSelectUser", false);
				resultMap.put("isSelectOrg", true);
				resultMap.put("isUserRadioStyle", false);
			} else if(
					(FocusReportNode35402Enum.BEE_SQUADRON_VERIFY_NODE_NAME.getTaskNodeName().equals(curnodeName)
							&& FocusReportNode35402Enum.BEE_BRIGADE_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName))
					|| (FocusReportNode35402Enum.BEE_BRIGADE_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)
							&& FocusReportNode35402Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(nodeName))) {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
				
				if(orgInfo != null) {
					resultMap.putAll(findNextUserInfoByOrgId(orgInfo.getParentOrgId(), nodeCode, params));
				}
				
				if(FocusReportNode35402Enum.BEE_SQUADRON_VERIFY_NODE_NAME.getTaskNodeName().equals(curnodeName)
						&& FocusReportNode35402Enum.BEE_BRIGADE_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					String dynamicDictJson = "[{'name':'我单位', 'value':'0'},{'name':'非我单位', 'value':'1'}]";
					
					isShowDict = true;
					isShowText = true;
					textLabelName = "转办部门";
					dictLabelName = "职责归属";
					
					dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
				}
			} else if(FocusReportNode35402Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(curnodeName)
					&& FocusReportNode35402Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(nodeName)) {
				resultMap.putAll(findNextUserInfoByOrgId(userInfo.getOrgId(), nodeCode, params));
			} else if(FocusReportNode35402Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				isShowDict = true;
				isShowText = true;
				textLabelName = "补充描述";
				dictLabelName = "行政处罚";
				
				dynamicContentMap.put("dictPcode", WaterQualityDictEnum.ADMINISTRTIVE_SACTION.getDictCode());
				dynamicContentMap.put("isMultiSelect", true);
			}
		} else if(FocusReportNode35403Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode35403Enum.BEE_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)
					&& FocusReportNode35403Enum.DISTRICT_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)) {
				String dynamicDictJson = "[{'name':'需立案查处', 'value':'0'},{'name':'需协调解决', 'value':'1'}]";
				
				isShowDict = true;
				dictLabelName = "处置结果";
				
				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
			} else if(FocusReportNode35403Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				String dynamicDictJson = "[{'name':'水质正常', 'value':'0'},{'name':'水质异常', 'value':'1'}]";
				
				isShowDict = true;
				dictLabelName = "水质情况";
				
				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
			} else if(FocusReportNode35403Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				isShowDict = true;
				isShowText = true;
				textLabelName = "补充描述";
				dictLabelName = "行政处罚";
				
				dynamicContentMap.put("dictPcode", WaterQualityDictEnum.ADMINISTRTIVE_SACTION.getDictCode());
				dynamicContentMap.put("isMultiSelect", true);
			}
		}
		
		dynamicContentMap.put("isShowText", isShowText);
		dynamicContentMap.put("isShowDict", isShowDict);
		dynamicContentMap.put("textLabelName", textLabelName);
		dynamicContentMap.put("dictLabelName", dictLabelName);
		
		Map<String, Object> dynamicContentMapTmp = new HashMap<String, Object>();
		
		if(CommonFunctions.isNotBlank(resultMap, "dynamicContentMap")) {
			dynamicContentMapTmp.putAll((Map<String, Object>) resultMap.get("dynamicContentMap"));
		}
		
		dynamicContentMapTmp.putAll(dynamicContentMap);
		
		resultMap.put("dynamicContentMap", dynamicContentMapTmp);

		//判断是否上传
		// 立案决定书:当前环节 立案查处
		// 		35401 新增一般 task7-task8；
		// 		35402 新增群众 task6-task7；
		// 		35403 突发 task4-task5；
		// 行政处罚决定书:当前环节 行政处罚
		// 		35401 新增一般 task8-end1；
		// 		35402 新增群众 task7-end1；
		// 		35403 突发 task5-end1；
		Boolean isUploadCasePic = false;
		Boolean isUploadPuniPic = false;
		Boolean isUploadHandledPic = false;
		String picTypeName = "";
		if(
				( FocusReportNode35401Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
						&&FocusReportNode35401Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(curnodeName)
						&&FocusReportNode35401Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(nodeName))
			|| (FocusReportNode35402Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
						&&FocusReportNode35402Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(curnodeName)
						&&FocusReportNode35402Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(nodeName)
				)
			|| (
				FocusReportNode35403Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
						&&FocusReportNode35403Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(curnodeName)
						&&FocusReportNode35403Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(nodeName)
				)
		){
			isUploadCasePic = true;
			picTypeName = "佐证材料";
		}else if(
				( FocusReportNode35401Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
						&&FocusReportNode35401Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(curnodeName)
						&&FocusReportNode35401Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)
				)
			|| (
				 FocusReportNode35402Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
						 &&FocusReportNode35402Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(curnodeName)
						 &&FocusReportNode35402Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)
				)
			|| (
				FocusReportNode35403Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
						&&FocusReportNode35403Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(curnodeName)
						&&FocusReportNode35403Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)
				)
		){
			isUploadPuniPic = true;
			picTypeName = "佐证材料";
		}else if(
				( FocusReportNode35401Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
						&&!FocusReportNode35401Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(curnodeName)
						&&!FocusReportNode35401Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(curnodeName)
						&&FocusReportNode35401Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)
				)
				|| (
						FocusReportNode35402Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
								&&!FocusReportNode35402Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(curnodeName)
								&&!FocusReportNode35402Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(curnodeName)
								&&FocusReportNode35402Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)
				)
				|| (
						FocusReportNode35403Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
								&&!FocusReportNode35403Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(curnodeName)
								&&!FocusReportNode35403Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(curnodeName)
								&&FocusReportNode35403Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)
				)
		){
			isUploadHandledPic = true;
			picTypeName = "处理后";
		}
		resultMap.put("isUploadCasePic", isUploadCasePic);
		resultMap.put("isUploadPuniPic", isUploadPuniPic);
		resultMap.put("isUploadHandledPic", false);
		resultMap.put("picTypeName", picTypeName);
		
		if(CommonFunctions.isNotBlank(resultMap, "isUserRadioStyle")) {
			boolean isUserRadioStyle = Boolean.valueOf(resultMap.get("isUserRadioStyle").toString());
			
			if(isUserRadioStyle && CommonFunctions.isNotBlank(resultMap, "userIds")) {
				String userIds = resultMap.get("userIds").toString();
				//只有当主送为一个人时，默认选中该人员
				resultMap.put("isUserDefaulCheck", userIds.split(",").length == 1);
			}
		}
		
		return resultMap;
	}
	
	@Override
	public List<GdZTreeNode> getTreeForEvent(
			UserInfo userInfo, Long orgId,
			String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception {
		List<GdZTreeNode> nodes = new ArrayList<GdZTreeNode>();
		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		
		if(nodeCodeHandler.isFromDept() && nodeCodeHandler.isToDept() && nodeCodeHandler.isSend()) {
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
			
			if(orgInfo != null) {
				List<OrgSocialInfoBO> orgInfoList = new ArrayList<OrgSocialInfoBO>();
				
				orgInfoList.add(orgInfo);
				
				findDeptByFilterProffession(orgInfoList, nodes, params, userInfo.getOrgId());
			}
		} else {
			nodes = super.getTreeForEvent(userInfo, orgId, nodeCode, level, professionCode, params);
		}
		
		return nodes;
	}
	
	/**
	 * 获取消息模板触发条件
	 * @param proInstance
	 * @param params
	 * @return
	 */
	protected StringBuffer capAdviceNoteCondition(ProInstance proInstance, Map<String, Object> params) {
		StringBuffer trigCondition = new StringBuffer("");
		String formTypeIdStr = null, curTaskName = null;
		
		if(proInstance != null) {
			curTaskName = proInstance.getCurNode();
			formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
		}
		
		trigCondition.append(ConstantValue.WATER_QUALITY_NOTE).append("-").append(proInstance.getProName()).append("-").append(curTaskName);
		
		if(FocusReportNode35402Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& FocusReportNode35402Enum.FIND_NODE_NAME.getTaskNodeName().equals(curTaskName)) {
			if(CommonFunctions.isNotBlank(params, "dataSource")) {
				trigCondition.append("-").append(params.get("dataSource"));
			}
		} else if(CommonFunctions.isNotBlank(params, "nodeName")) {
			trigCondition.append("-").append(params.get("nodeName"));
		}
		
		return trigCondition;
	}
	
	/**
	 * 消息模板内容构造
	 * @param adviceNote
	 * @param userInfo
	 * @param params
	 * @return
	 */
	protected String capAdviceNote(String adviceNote, UserInfo userInfo, Map<String, Object> params) {
		adviceNote = super.capAdviceNote(adviceNote, userInfo, params);
		
		if(StringUtils.isNotBlank(adviceNote)) {
			if(adviceNote.contains("@ildischargeEnterpriseName@") || adviceNote.contains("@personInvolved@")) {
				String ildischargeEnterpriseName = "", personInvolved = "";
				
				if(adviceNote.contains("@ildischargeEnterpriseName@") && CommonFunctions.isNotBlank(params, "ildischargeEnterpriseName")) {
					ildischargeEnterpriseName = params.get("ildischargeEnterpriseName").toString();
				} else if(adviceNote.contains("@personInvolved@") && CommonFunctions.isNotBlank(params, "personInvolved")) {
					personInvolved = params.get("personInvolved").toString();
				}
				
				adviceNote = adviceNote.replace("@ildischargeEnterpriseName@", ildischargeEnterpriseName)
			   			               .replace("@personInvolved@", personInvolved);
			}
		}
		
		return adviceNote;
	}
	
	/**
	 * 依据组织id获取下一环节可办理人员信息
	 * @param orgId		基准组织id
	 * @param nodeCode	节点操作编码
	 * @param params	额外参数
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> findNextUserByOrgId(Long orgId, String nodeCode, Map<String, Object> params) throws Exception {
		OrgSocialInfoBO userOrgInfo = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(orgId != null && orgId > 0) {
			userOrgInfo = orgSocialInfoService.findByOrgId(orgId);
		}
		
		if(userOrgInfo != null) {
			Map<Long, OrgSocialInfoBO> orgInfoMap = null;
			List<OrgSocialInfoBO> orgInfoList = new ArrayList<OrgSocialInfoBO>();
			
			orgInfoList.add(userOrgInfo);
			orgInfoMap = findDeptByFilterProffession(orgInfoList, null, params, null);
			
			if(orgInfoMap != null && !orgInfoMap.isEmpty()) {
				OrgSocialInfoBO orgInfo = orgInfoMap.get(orgInfoMap.keySet().toArray()[0]);
				
				if(orgInfo != null) {
					resultMap.putAll(findNextUserInfoByOrgId(orgInfo.getOrgId(), nodeCode, params));
				}
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 依据组织id获取下一环节可办理人员信息
	 * @param orgId		组织id
	 * @param nodeCode	节点操作编码
	 * @param params	额外参数
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> findNextUserInfoByOrgId(Long orgId, String nodeCode, Map<String, Object> params) throws Exception {
		List<UserInfo> nextUserInfoList = getUserInfoList(orgId, nodeCode, null, params);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(nextUserInfoList != null && nextUserInfoList.size() > 0) {
			StringBuffer userIds = new StringBuffer(""),
				         orgIds = new StringBuffer(""),
				         userNames = new StringBuffer("");
			
			for(UserInfo nextUser : nextUserInfoList) {
				userIds.append(",").append(nextUser.getUserId());
				orgIds.append(",").append(nextUser.getOrgId());
				userNames.append(",").append(nextUser.getPartyName());
			}
			
			if(userIds.length() > 0) {
				resultMap.put("userIds", userIds.substring(1));
			}
			if(userNames.length() > 0) {
				resultMap.put("userNames", userNames.substring(1));
			}
			if(orgIds.length() > 0) {
				resultMap.put("orgIds", orgIds.substring(1));
			}
			
			resultMap.put("isSelectUser", false);
			resultMap.put("isSelectOrg", false);
			resultMap.put("isUserRadioStyle", true);
		}
		
		return resultMap;
	}
	
	/**
	 * 获取生态环境局监测大队办理人员信息
	 * @param proInstance
	 * @param userInfo
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> capBeeBrigadeOperator(ProInstance proInstance, UserInfo userInfo, Map<String, Object> params) throws Exception {
		Map<String, Object> fiveKeyParams = null, fiveKeyMap = null, resultMap = new HashMap<String, Object>();
		UserInfo nextUserInfo = null;
		
		fiveKeyParams = new HashMap<String, Object>();
		fiveKeyParams.putAll(params);
		fiveKeyParams.put("isAnalysisOperateUser", true);
		
		//获取生态环境局组织下的用户
		fiveKeyMap = preCapNodeOperator(FocusReportNode35402Enum.FIND_NODE_NAME.getTaskNodeName(), FocusReportNode35402Enum.BEE_HANDLE_NODE_NAME.getTaskNodeName(), "CJR0D3", proInstance, userInfo, fiveKeyParams);
		
		if(CommonFunctions.isNotBlank(fiveKeyMap, "userIds") && CommonFunctions.isNotBlank(fiveKeyMap, "orgIds")) {
			String[] userIdArray = fiveKeyMap.get("userIds").toString().split(","),
					 orgIdArray = fiveKeyMap.get("orgIds").toString().split(",");
			nextUserInfo = new UserInfo();
			
			nextUserInfo.setUserId(Long.valueOf(userIdArray[0]));
			nextUserInfo.setOrgId(Long.valueOf(orgIdArray[0]));
		}
		
		//获取生态环境局大队组织下的办理人员
		fiveKeyParams.put("isAnalysisOperateUser", false);
		fiveKeyMap = preCapNodeOperator(FocusReportNode35402Enum.BEE_HANDLE_NODE_NAME.getTaskNodeName(), FocusReportNode35402Enum.BEE_BRIGADE_DISTRIBUTE_NODE_NAME.getTaskNodeName(), "D3S1D4", proInstance, nextUserInfo, fiveKeyParams);
		
		if(CommonFunctions.isNotBlank(fiveKeyMap, "userIds") && CommonFunctions.isNotBlank(fiveKeyMap, "orgIds")) {
			resultMap.put("userIds", fiveKeyMap.get("userIds"));
			resultMap.put("orgIds", fiveKeyMap.get("orgIds"));
			resultMap.put("userNames", fiveKeyMap.get("userNames"));
		}
		
		return resultMap;
	}
	
	/**
	 * 预先获取节点办理人员信息
	 * @param curNodeName	当前环节
	 * @param nextNodeName	下一环节
	 * @param nodeCode		节点操作编码
	 * @param proInstance	流程实例
	 * @param userInfo		操作用户信息
	 * @param params		额外参数
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> preCapNodeOperator(String curNodeName, String nextNodeName, String nodeCode, ProInstance proInstance, UserInfo userInfo, Map<String, Object> params) throws Exception {
		Map<String, Object> fiveKeyParams = new HashMap<String, Object>(), fiveKeyMap = null;
		String wfTypeId = null, workflowName = null;
		Node nextNodeInfo = null;
		Long reportId = null;
		
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isBlank(params, "formType")) {
			params.put("formType", FocusReportNode35402Enum.FORM_TYPE.toString());
		}
		
		if(proInstance == null) {
			Long instanceId = null;
			
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {}
			
			if(instanceId != null && instanceId > 0) {
				proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			}
		}
		
		if(proInstance != null) {
			reportId = proInstance.getFormId();
			workflowName = proInstance.getProName();
			
			wfTypeId = FocusReportNode35402Enum.WORKFLOW_TYPE_ID.toString();
		}
		
		fiveKeyParams.putAll(params);
		
		nextNodeInfo = baseWorkflowService.capNodeInfo(workflowName, wfTypeId, reportId, nextNodeName, userInfo);
		
		if(nextNodeInfo != null) {
			String nodeId = String.valueOf(nextNodeInfo.getNodeId());
			
			fiveKeyParams.put("nodeId", nodeId);
			
			fiveKeyMap = getNodeInfoForEvent(userInfo, curNodeName, nextNodeName, nodeCode, nodeId, fiveKeyParams);
		}
		
		return fiveKeyMap;
	}
}
