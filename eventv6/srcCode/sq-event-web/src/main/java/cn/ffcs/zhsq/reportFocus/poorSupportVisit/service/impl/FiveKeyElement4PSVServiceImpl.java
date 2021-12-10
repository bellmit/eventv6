package cn.ffcs.zhsq.reportFocus.poorSupportVisit.service.impl;

import cn.ffcs.gbp.poorhold.domain.HelpInfo;
import cn.ffcs.gbp.poorhold.service.IPoorHouseholdService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.FiveKeyElement4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.data.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 扶贫走访(Poor Support Visit)人员选择
 * @ClassName:   FiveKeyElement4PSVServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年12月4日 上午10:17:43
 */
@Service(value = "fiveKeyElement4PSVService")
public class FiveKeyElement4PSVServiceImpl extends FiveKeyElement4TwoVioPreServiceImpl {
	@Autowired
	private IReportIntegrationService reportIntegrationService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private HessianFlowService hessianFlowService;
	
	@Autowired
	private IPoorHouseholdService poorHouseholdService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		Map<String, Object> resultMap = null, reportMap = null;
		params = params == null ? new HashMap<String, Object>() : params;
		boolean isUseSelfAnalysis = nodeCodeHandler.isFromDept() && nodeCodeHandler.isSplitFlow() && nodeCodeHandler.isToDept();
		String reportType = ReportTypeEnum.poorSupportVisit.getReportTypeIndex(),
			   wfEndNodeName = null, formTypeIdStr = null, collectSource = null;
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
		
		if(isUseSelfAnalysis) {
			params.put("isAnalysisOperateUser", false);
		}
		
		if(FocusReportNode356Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& !FocusReportNode356Enum.HRP_VERIFY_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
			Map<String, Object> reportParams = new HashMap<String, Object>();
			
			reportParams.putAll(params);
			reportParams.put("reportId", reportId);
			
			reportMap = reportIntegrationService.findReportFocusByUUID(null, userInfo, reportParams);
			
			if(CommonFunctions.isNotBlank(reportMap, "collectSource")) {
				collectSource = reportMap.get("collectSource").toString();
			}

			if(CommonFunctions.isBlank(params, "wfEndNodeName")) {
				wfEndNodeName = nodeName + "-" + collectSource;
			}
			
			params.put("wfEndNodeName", wfEndNodeName);
		}
		
		resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);

		if(isUseSelfAnalysis) {
			resultMap.put("isSelectOrg", true);
			resultMap.put("isUserRadioStyle", false);
			resultMap.put("eventNodeCode", nodeCodeHandler);
		} else if(FocusReportNode356Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& FocusReportNode356Enum.DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)
				&& FocusReportNode356Enum.HRP_VERIFY_NODE_NAME.getTaskNodeName().equals(nodeName)) {
			//获取贫困户的帮扶责任人
			//如果帮扶责任人中包含有泉州市挂钩帮扶责任人，则保留原有的办理人员，
			//否则，直接替换为对应否帮扶责任人
			boolean isUserRadioStyle = false;
			
			if(CommonFunctions.isNotBlank(resultMap, "isUserRadioStyle")) {
				isUserRadioStyle = Boolean.valueOf(resultMap.get("isUserRadioStyle").toString());
			}
			
			if(isUserRadioStyle) {
				capPoorHelperUser(reportMap, userInfo, resultMap, false);
				capUnrepeatedOperateUser(resultMap);
			}
			
		}
		
		if(FocusReportNode356Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			boolean isAttachHelpers = true;
			if(FocusReportNode356Enum.FILL_REPORT_NODE_NAME.getTaskNodeName().equals(curnodeName)
					&& FocusReportNode356Enum.DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)) {
				isAttachHelpers = PSVCollectSourceEnum.GRID_REPORT.getCollectSource().equals(collectSource)
						|| PSVCollectSourceEnum.COMMUNITY_REPORT.getCollectSource().equals(collectSource);
			} else if(FocusReportNode356Enum.HRP_VERIFY_NODE_NAME.getTaskNodeName().equals(curnodeName)
					|| FocusReportNode356Enum.HRP_VERIFY_NODE_NAME.getTaskNodeName().equals(nodeName)) {
				isAttachHelpers = false;
			}
			
			if(isAttachHelpers) {
				Map<String, Object> distributeUser = new HashMap<String, Object>();
				StringBuffer userIds = new StringBuffer(""),
						     userNames = new StringBuffer(""),
						     orgIds = new StringBuffer(""),
						     orgNames = new StringBuffer("");
				boolean isDisplayUser = true;
				
				if(CommonFunctions.isNotBlank(resultMap, "distributeUser")) {
					distributeUser = (Map<String, Object>) resultMap.get("distributeUser");
					
					if(CommonFunctions.isNotBlank(distributeUser, "isDisplayUser")) {
						isDisplayUser = Boolean.valueOf(distributeUser.get("isDisplayUser").toString());
					}
					
					if(CommonFunctions.isNotBlank(distributeUser, "userIds")) {
						userIds.append(",").append(distributeUser.get("userIds").toString());
					}
					
					if(CommonFunctions.isNotBlank(distributeUser, "userNames")) {
						userNames.append(",").append(distributeUser.get("userNames").toString());
					}
					
					if(CommonFunctions.isNotBlank(distributeUser, "orgIds")) {
						orgIds.append(",").append(distributeUser.get("orgIds").toString());
					}
					
					if(CommonFunctions.isNotBlank(distributeUser, "orgNames")) {
						orgNames.append(",").append(distributeUser.get("orgNames").toString());
					}
				}
				
				Map<String, Object> psvParams = new HashMap<String, Object>();
				psvParams.putAll(params);
				psvParams.putAll(distributeUser);
				//分送人员中添加帮扶人员，需要对人员进行去重处理
				capPoorHelperUser(reportMap, userInfo, psvParams, true);
				
				if(CommonFunctions.isNotBlank(psvParams, "userIds")) {
					userIds.append(",").append(psvParams.get("userIds"));
				}
				
				if(CommonFunctions.isNotBlank(psvParams, "orgIds")) {
					orgIds.append(",").append(psvParams.get("orgIds"));
				}
				
				if(CommonFunctions.isNotBlank(psvParams, "userNames")) {
					userNames.append(",").append(psvParams.get("userNames"));
				}

				if(CommonFunctions.isNotBlank(psvParams, "orgNames")) {
					orgNames.append(",").append(psvParams.get("orgNames"));
				}
				
				if(userIds.length() > 0 && orgIds.length() > 0) {
					if(userIds.length() > 0) {
						distributeUser.put("userIds", userIds.substring(1));
					}
					if(userNames.length() > 0) {
						distributeUser.put("userNames", userNames.substring(1));
					}
					if(orgIds.length() > 0) {
						distributeUser.put("orgIds", orgIds.substring(1));
					}
					if(orgNames.length() > 0) {
						distributeUser.put("orgNames", orgNames.substring(1));
					}
					
					capUnrepeatedOperateUser(distributeUser);
					
					distributeUser.put("isDisplayUser", isDisplayUser);
					
					resultMap.put("distributeUser", distributeUser);
				}
			}
		}
		
		boolean isShowText = false, isShowDict = false, isShowRadio = false;
		String textLabelName = "", dictLabelName = "", radioLabelName = "";
		Map<String, Object> dynamicContentMap = new HashMap<String, Object>();
		
		if(FocusReportNode356Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode356Enum.FILL_REPORT_NODE_NAME.getTaskNodeName().equals(curnodeName)
					&& FocusReportNode356Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)) {
				//do nothing
			} else {
				isShowDict = true;
				dictLabelName =  PSVDictEnum.DIFFICULTY_TYPE.getDictName();
				dynamicContentMap.put("dictPcode", PSVDictEnum.DIFFICULTY_TYPE.getDictCode());
			}
			
			if(FocusReportNode356Enum.FILL_REPORT_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				if(PSVCollectSourceEnum.STREET_REPORT.getCollectSource().equals(collectSource)
						|| PSVCollectSourceEnum.COUNTY_DEPARTMENT_REPORT.getCollectSource().equals(collectSource)
						|| PSVCollectSourceEnum.MUNICIPAL_REPORT.getCollectSource().equals(collectSource)) {
					if(FocusReportNode356Enum.DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)) {
						isShowText = true;
						textLabelName = "解决困难";
					} else if(FocusReportNode356Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)) {
						String dynamicRadioJson = "[{'name':'问题未发现', 'value':'0'},{'name':'问题已解决', 'value':'1'}]";
						
						isShowRadio = true;
						isShowText = true;
						isShowDict = true;
						radioLabelName = "处置情况";
						textLabelName = "帮扶措施";
						dictLabelName =  PSVDictEnum.DIFFICULTY_TYPE.getDictName();
						
						dynamicContentMap.put("dictPcode", PSVDictEnum.DIFFICULTY_TYPE.getDictCode());
						dynamicContentMap.put("dynamicRadioJson", dynamicRadioJson);
					}
				}
			} else if(
					(FocusReportNode356Enum.HRP_VERIFY_NODE_NAME.getTaskNodeName().equals(curnodeName) 
							|| FocusReportNode356Enum.COMMUNITY_VERIFY_NODE_NAME.getTaskNodeName().equals(curnodeName))
					&& FocusReportNode356Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)) {
				String dynamicRadioJson = "[{'name':'问题不存在', 'value':'0'},{'name':'问题已解决', 'value':'1'}]";
				
				isShowRadio = true;
				isShowText = true;
				radioLabelName = "处置情况";
				textLabelName = "帮扶措施";
				
				dynamicContentMap.put("dynamicRadioJson", dynamicRadioJson);
			} else if(FocusReportNode356Enum.STREET_HANDLE_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)
					&& FocusReportNode356Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)) {
				isShowText = true;
				textLabelName = "帮扶措施";
			} else if(
					(FocusReportNode356Enum.HRP_VERIFY_NODE_NAME.getTaskNodeName().equals(curnodeName)
							&& FocusReportNode356Enum.STREET_HANDLE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName))
					|| (FocusReportNode356Enum.COMMUNITY_VERIFY_NODE_NAME.getTaskNodeName().equals(curnodeName)
							&& FocusReportNode356Enum.STREET_HANDLE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName))
					|| (FocusReportNode356Enum.STREET_HANDLE_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)
							&& FocusReportNode356Enum.COUNTY_HANDLE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName))
					|| (FocusReportNode356Enum.COUNTY_HANDLE_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)
							&& FocusReportNode356Enum.COUNTY_DEPARTMENT_HANDLE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName))
					) {
				isShowText = true;
				textLabelName = "解决困难";
			}
		}

		//判断是否上传处理后图片
		if(FocusReportNode356Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& FocusReportNode356Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
			resultMap.put("isUploadHandledPic", false);
			resultMap.put("picTypeName", "处理后");
		}

		dynamicContentMap.put("isShowRadio", isShowRadio);
		dynamicContentMap.put("isShowText", isShowText);
		dynamicContentMap.put("isShowDict", isShowDict);
		dynamicContentMap.put("radioLabelName", radioLabelName);
		dynamicContentMap.put("textLabelName", textLabelName);
		dynamicContentMap.put("dictLabelName", dictLabelName);
		
		Map<String, Object> dynamicContentMapTmp = new HashMap<String, Object>();
		
		if(CommonFunctions.isNotBlank(resultMap, "dynamicContentMap")) {
			dynamicContentMapTmp.putAll((Map<String, Object>) resultMap.get("dynamicContentMap"));
		}
		
		dynamicContentMapTmp.putAll(dynamicContentMap);
		
		resultMap.put("dynamicContentMap", dynamicContentMapTmp);
		
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
		String formTypeIdStr = null, curTaskName = null;
		
		if(proInstance != null) {
			curTaskName = proInstance.getCurNode();
			formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
		}
		
		trigCondition.append(ConstantValue.POOR_SUPPORT_VISIT_NOTE).append("-").append(proInstance.getProName()).append("-").append(curTaskName);
		
		if(CommonFunctions.isNotBlank(params, "nodeName")) {
			trigCondition.append("-").append(params.get("nodeName"));
		}
		
		if(FocusReportNode356Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& CommonFunctions.isNotBlank(params, "collectSource")) {
			trigCondition.append("-").append(params.get("collectSource"));
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
			if(adviceNote.contains("@poorVillagerName@") && CommonFunctions.isNotBlank(params, "poorVillagerName")) {
				adviceNote = adviceNote.replace("@poorVillagerName@", params.get("poorVillagerName").toString());
			}
			
			if(adviceNote.contains("@todayMonthDayZH@")) {
				adviceNote = adviceNote.replace("@todayMonthDayZH@", DateUtils.getToday("MM月dd日"));
			}
			
		}
		
		return adviceNote;
	}
	
	/**
	 * 获取贫困户帮扶责任人对应用户信息
	 * @param reportMap	入格事件内容
	 * @param userInfo	操作用户信息
	 * @param params	额外参数
	 * @param isTransferMunicipalPoorHelper	是否将泉州市帮扶责任人转换为第一副网格长，true为转换，false则使用params中传递的办理用户信息
	 * @throws Exception
	 */
	private void capPoorHelperUser(Map<String, Object> reportMap, UserInfo userInfo, Map<String, Object> params, boolean isTransferMunicipalPoorHelper) throws Exception {
		Long poorVillagerId = null;
		boolean isExistMunicipalPoorHelper = false;
		StringBuffer orgIds = new StringBuffer(""),
				     userIds = new StringBuffer(""),
				     userNames = new StringBuffer(""),
				     orgNames = new StringBuffer(""),
				     poorHelperUserId = new StringBuffer(""),
			         poorHelperOrgId = new StringBuffer(""),
			         poorHelperUserName = new StringBuffer(""),
			         poorHelperOrgName = new StringBuffer("");
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(reportMap, "poorVillagerId")) {
			try {
				poorVillagerId = Long.valueOf(reportMap.get("poorVillagerId").toString());
			} catch(NumberFormatException e) {}
		}
		
		if(poorVillagerId != null && poorVillagerId > 0) {
			Map<String, Object> poorHelperParams = new HashMap<String, Object>();
			List<HelpInfo> poorHelpInfoList = null;
			String HELP_TYPE_WRIGHT = "00",//帮扶责任人通过手动输入获取
				   HELP_TYPE_SELECT = "01";//帮扶责任人通过人员选择获取
			Long helpPeopleId = null, helpPeopleOrgId = null;
			String helpPeopleName = null, helpPeopleOrgName = null, helpType = null;
			
			poorHelperParams.put("bizId", poorVillagerId);
			
			poorHelpInfoList = poorHouseholdService.searchHelpList(poorHelperParams);
			
			if(poorHelpInfoList != null) {
				for(HelpInfo poorHelpInfo : poorHelpInfoList) {
					helpPeopleId = null;
					helpPeopleOrgId = null;
					helpPeopleName = null;
					helpPeopleOrgName = null;
					helpType = null;
					
					if(poorHelpInfo != null) {
						helpType = poorHelpInfo.getHelpType();
						
						if(HELP_TYPE_SELECT.equals(helpType)) {
							helpPeopleId = poorHelpInfo.getHelpPeopleId();
							helpPeopleOrgId = poorHelpInfo.getHelpPeopleOrgId();
							helpPeopleName = poorHelpInfo.getHelpPeopleName();
							helpPeopleOrgName = poorHelpInfo.getHelpPeopleOrgName();
						} else if(HELP_TYPE_WRIGHT.equals(helpType)) {
							isExistMunicipalPoorHelper = isExistMunicipalPoorHelper || true;
						}
						
						if(helpPeopleId != null && helpPeopleId > 0 
								&& helpPeopleOrgId != null && helpPeopleOrgId > 0) {
							poorHelperUserId.append(",").append(helpPeopleId);
							poorHelperOrgId.append(",").append(helpPeopleOrgId);
							poorHelperUserName.append(",").append(helpPeopleName);
							poorHelperOrgName.append(",").append(helpPeopleOrgName);
						}
					}
				}
			}
		}
		
		//如果是泉州市的帮扶责任人，则增添上通过节点转换而出的第一副网格长
		if(isExistMunicipalPoorHelper) {
			if(isTransferMunicipalPoorHelper) {
				ProInstance proInstance = null;
				Integer nodeId = null;
				
				if(CommonFunctions.isNotBlank(params, "proInstance")) {
					proInstance = (ProInstance) params.get("proInstance");
				} else if(CommonFunctions.isNotBlank(params, "instanceId")) {
					Long instanceId = null;
					
					try {
						instanceId = Long.valueOf(params.get("instanceId").toString());
					} catch(NumberFormatException e) {}
					
					proInstance = baseWorkflowService.findProByInstanceId(instanceId);
				}
				
				if(proInstance != null) {
					Node nodeInfo = hessianFlowService.getNodeByNodeNameAndDeploymentId(proInstance.getDeploymentId(), FocusReportNode356Enum.HRP_VERIFY_NODE_NAME.getTaskNodeName());
					
					if(nodeInfo != null) {
						nodeId = nodeInfo.getNodeId();
					}
				}
				
				if(nodeId != null) {
					Map<String, Object> resultMap = null, psvParams = new HashMap<String, Object>();
					
					psvParams.putAll(params);
					psvParams.put("nodeId", nodeId);
					psvParams.put("isAnalysisOperateUser", true);
					
					resultMap = super.getNodeInfoForEvent(userInfo, FocusReportNode356Enum.DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName(), FocusReportNode356Enum.HRP_VERIFY_NODE_NAME.getTaskNodeName(), "D4S1U5", nodeId.toString(), psvParams);
					
					if(CommonFunctions.isNotBlank(resultMap, "orgIds")) {
						orgIds.append(",").append(resultMap.get("orgIds").toString());
					}
					
					if(CommonFunctions.isNotBlank(resultMap, "userIds")) {
						userIds.append(",").append(resultMap.get("userIds").toString());
					}
					
					if(CommonFunctions.isNotBlank(resultMap, "userNames")) {
						userNames.append(",").append(resultMap.get("userNames").toString());
					}
					
					if(CommonFunctions.isNotBlank(resultMap, "orgNames")) {
						orgNames.append(",").append(resultMap.get("orgNames").toString());
					}
				}
			} else {
				if(CommonFunctions.isNotBlank(params, "orgIds")) {
					orgIds.append(",").append(params.get("orgIds").toString());
				}
				
				if(CommonFunctions.isNotBlank(params, "userIds")) {
					userIds.append(",").append(params.get("userIds").toString());
				}
				
				if(CommonFunctions.isNotBlank(params, "userNames")) {
					userNames.append(",").append(params.get("userNames").toString());
				}
				
				if(CommonFunctions.isNotBlank(params, "orgNames")) {
					orgNames.append(",").append(params.get("orgNames").toString());
				}
			}
		}
		
		//帮扶责任人对应的操作用户
		if(poorHelperUserId.length() > 0 && poorHelperOrgId.length() > 0) {
			userIds.append(poorHelperUserId);
			orgIds.append(poorHelperOrgId);
			userNames.append(poorHelperUserName);
			orgNames.append(poorHelperOrgName);
		}
		
		String nextUserIds = "", nextOrgIds = "", nextuserNames = "", nextOrgNames = "";
		
		if(userIds.length() > 0) {
			nextUserIds = userIds.substring(1);
		}
		
		if(orgIds.length() > 0) {
			nextOrgIds = orgIds.substring(1);
		}
		
		if(userNames.length() > 0) {
			nextuserNames = userNames.substring(1);
		}
		
		if(orgNames.length() > 0) {
			nextOrgNames = orgNames.substring(1);
		}
		
		params.put("userIds", nextUserIds);
		params.put("orgIds", nextOrgIds);
		params.put("userNames", nextuserNames);
		params.put("orgNames", nextOrgNames);
	}
	
	/**
	 * 对办理用户信息进行去重处理
	 * @param params
	 */
	private void capUnrepeatedOperateUser(Map<String, Object> params) {
		String nextUserIds = "", nextOrgIds = "", nextUserNames = "", nextOrgNames = "";
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "userIds")) {
			nextUserIds = params.get("userIds").toString();
		}
		if(CommonFunctions.isNotBlank(params, "orgIds")) {
			nextOrgIds = params.get("orgIds").toString();
		}
		if(CommonFunctions.isNotBlank(params, "userNames")) {
			nextUserNames = params.get("userNames").toString();
		}
		if(CommonFunctions.isNotBlank(params, "orgNames")) {
			nextOrgNames = params.get("orgNames").toString();
		}
		
		//用户进行判重处理
		if(StringUtils.isNotBlank(nextUserIds) 
				&& StringUtils.isNotBlank(nextOrgIds) 
				&& StringUtils.isNotBlank(nextUserNames)) {
			String[] nextUserArray = nextUserIds.split(","),
					 nextOrgArray = nextOrgIds.split(","),
					 nextUserNameArray = nextUserNames.split(","),
					 nextOrgNameArray = nextOrgNames.split(",");
			
			int nextUserLen = nextUserArray.length,
				nextOrgLen = nextOrgArray.length,
				nextUserNameLen = nextUserNameArray.length,
				nextOrgNameLen = nextOrgNameArray.length;
			StringBuffer userIdBuffer = new StringBuffer(""),
					     orgIdBuffer = new StringBuffer(""),
					     userNameBuffer = new StringBuffer(""),
					     orgNameBuffer = new StringBuffer(""),
					     userSetKey = new StringBuffer("");
			Set<String> userSet = new HashSet<String>();
			String nextUserIdStr = null, nextOrgIdStr = null, nextUserPartyName = null, nextOrgName = null;
			
			for(int index = 0; index < nextUserLen; index++) {
				nextUserIdStr = nextUserArray[index];
				userSetKey = new StringBuffer("");
				
				if(index < nextUserNameLen) {
					nextUserPartyName = nextUserNameArray[index];
				}
				if(index < nextOrgNameLen) {
					nextOrgName = nextOrgNameArray[index];
				}
				
				if(index < nextOrgLen) {
					nextOrgIdStr = nextOrgArray[index];
					
					userSetKey.append(nextUserIdStr).append("-").append(nextOrgIdStr);
				}
				
				if(!userSet.contains(userSetKey.toString())) {//用户判重
					userIdBuffer.append(",").append(nextUserIdStr);
					orgIdBuffer.append(",").append(nextOrgIdStr);
					
					if(StringUtils.isNotBlank(nextUserPartyName)) {
						userNameBuffer.append(",").append(nextUserPartyName);
					}
					if(StringUtils.isNotBlank(nextOrgName)) {
						orgNameBuffer.append(",").append(nextOrgName);
					}
				}
			}
			
			if(userIdBuffer.length() > 0) {
				nextUserIds = userIdBuffer.substring(1);
			}
			
			if(orgIdBuffer.length() > 0) {
				nextOrgIds = orgIdBuffer.substring(1);
			}
			
			if(userNameBuffer.length() > 0) {
				nextUserNames = userNameBuffer.substring(1);
			}
			
			if(orgNameBuffer.length() > 0) {
				nextOrgNames = orgNameBuffer.substring(1);
			}
		}
		
		params.put("userIds", nextUserIds);
		params.put("orgIds", nextOrgIds);
		params.put("userNames", nextUserNames);
		params.put("orgNames", nextOrgNames);
	}
}
