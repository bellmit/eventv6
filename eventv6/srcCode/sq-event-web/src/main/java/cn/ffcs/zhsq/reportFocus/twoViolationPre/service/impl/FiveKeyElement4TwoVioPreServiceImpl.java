package cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl;

import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.keyelement.impl.FiveKeyElementForEventServiceImpl;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.IReportMsgCCCfgService;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.service.impl.ReportMsgCCTypeEnum;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.data.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 两违防治主送
 * @ClassName:   FiveKeyElement4TwoVioPreServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年9月16日 上午11:12:39
 */
@Service(value = "fiveKeyElement4TwoVioPreService")
public class FiveKeyElement4TwoVioPreServiceImpl extends FiveKeyElementForEventServiceImpl {
	@Autowired
	private IReportIntegrationService reportIntegrationService;
	
	@Autowired
	private IReportMsgCCCfgService reportMsgCCCfgService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		Long reportId = null, instanceId = null;
		Map<String, Object> reportParams = new HashMap<String, Object>(),
							reportFocusMap = null,
							resultMap = new HashMap<String, Object>();
		ProInstance proInstance = null;
		String formTypeIdStr = null, workflowName = null;
		boolean isUserRadioStyle = true, isSelectOrg = false;
		String reportType = ReportTypeEnum.twoVioPre.getReportTypeIndex();
		
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "formId")) {
			try {
				reportId = Long.valueOf(params.get("formId").toString());
			} catch(NumberFormatException e) {}
		}
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {}
		}
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		}
		
		if(instanceId != null && instanceId > 0) {
			proInstance = baseWorkflowService.findProByInstanceId(instanceId);
		}
		
		if(proInstance != null) {
			if(reportId == null || reportId<= 0) {
				reportId = proInstance.getFormId();
			}
			
			formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
			workflowName = proInstance.getProName();
			
			if(CommonFunctions.isBlank(params, "formTypeId")) {
				params.put("formTypeId", formTypeIdStr);
			}
		}
		
		reportParams.putAll(params);
		reportParams.put("reportId", reportId);
		reportParams.put("reportType", reportType);
		reportFocusMap = reportIntegrationService.findReportFocusByUUID(null, userInfo, reportParams);
		
		if(CommonFunctions.isNotBlank(reportFocusMap, "regionCode")) {
			boolean isAnalysisOperateUser = true;
			OrgSocialInfoBO orgInfo = null;
			//当前用户所属组织
			OrgSocialInfoBO userOrgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
			Map<String, Object> orgParam = new HashMap<String, Object>();
			List<OrgSocialInfoBO> orgInfoList = null;
			
			orgParam.put("regionCode", reportFocusMap.get("regionCode").toString());
			orgParam.put("orgType", ConstantValue.ORG_TYPE_UNIT);
			orgParam.put("professionCode", ConstantValue.GOV_PROFESSION_CODE);
			
			orgInfoList = orgSocialInfoService.findOrgSocialListByPara(orgParam);
			
			if(orgInfoList != null && orgInfoList.size() > 0) {
				orgInfo = orgInfoList.get(0);
			}
			
			if(CommonFunctions.isNotBlank(params, "isAnalysisOperateUser")) {
				isAnalysisOperateUser = Boolean.valueOf(params.get("isAnalysisOperateUser").toString());
			}
			
			if(isAnalysisOperateUser) {
				if(nodeCodeHandler.isEquality()) {
					List<UserInfo> nextUserInfoList = getUserInfoList(userInfo.getOrgId(), nodeCode, params);
					
					if(nextUserInfoList != null) {
						resultMap = new HashMap<String, Object>();
						StringBuffer userIdBuffer = new StringBuffer(""),
									 userNameBuffer = new StringBuffer(""),
									 orgIdBuffer = new StringBuffer(""),
									 orgNameBuffer = new StringBuffer("");
						
						for(UserInfo nextUserInfo : nextUserInfoList) {
							userIdBuffer.append(",").append(nextUserInfo.getUserId());
							userNameBuffer.append(",").append(nextUserInfo.getPartyName());
							orgIdBuffer.append(",").append(nextUserInfo.getOrgId());
							orgNameBuffer.append(",").append(nextUserInfo.getOrgName());
						}
						
						if(userIdBuffer.length() > 0) {
							resultMap.put("userIds", userIdBuffer.substring(1));
						}
						if(userNameBuffer.length() > 0) {
							resultMap.put("userNames", userNameBuffer.substring(1));
						}
						if(orgIdBuffer.length() > 0) {
							resultMap.put("orgIds", orgIdBuffer.substring(1));
						}
						if(orgNameBuffer.length() > 0) {
							resultMap.put("orgNames", orgNameBuffer.substring(1));
						}
					}
				} else if(nodeCodeHandler.isToBegin() && proInstance != null) {
					Long proCreateUserId = proInstance.getUserId();
					String proCreateUserName = null;
					
					if(proCreateUserId != null && proCreateUserId > 0) {
						UserBO userBO = userManageService.getUserInfoByUserId(proCreateUserId);
						if(userBO != null) {
							proCreateUserName = userBO.getPartyName();
						}
					}
					
					resultMap.put("userIds", proCreateUserId);
					resultMap.put("userNames", proCreateUserName);
					resultMap.put("orgIds", proInstance.getOrgId());
					resultMap.put("orgNames", proInstance.getOrgName());
				} else if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
						&& nodeCodeHandler.isSend()) {
					isUserRadioStyle = false;
					isSelectOrg = true;
					resultMap.put("eventNodeCode", nodeCodeHandler);
				} else if((nodeCodeHandler.isPlaceFile() || nodeCodeHandler.isPerson())
						&& FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
					String closeUserIdStr = null, closeOrgIdStr = null;
					
					if(CommonFunctions.isNotBlank(reportFocusMap, "closeUserId")) {
						closeUserIdStr = reportFocusMap.get("closeUserId").toString();
					}
					
					if(CommonFunctions.isNotBlank(reportFocusMap, "closeOrgId")) {
						closeOrgIdStr = reportFocusMap.get("closeOrgId").toString();
					}
					
					if(StringUtils.isNotBlank(closeUserIdStr) && StringUtils.isNotBlank(closeOrgIdStr)) {
						String[] closeUserIdArray = closeUserIdStr.split(","),
								 closeOrgIdArray = closeOrgIdStr.split(",");
						String userId = null, orgId = null;
						UserBO userBO = null;
						OrgSocialInfoBO orgInfoBO = null;
						StringBuffer nextUserId = new StringBuffer(""),
								     nextuserName = new StringBuffer(""),
								     nextOrgId = new StringBuffer(""),
								     nextOrgName = new StringBuffer("");
						
						for(int index = 0, userLen = closeUserIdArray.length, orgLen = closeOrgIdArray.length; index < userLen; index++) {
							userId = closeUserIdArray[index];
							orgId = null;
							
							if(index < orgLen) {
								orgId = closeOrgIdArray[index];
							}
							
							if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(orgId)) {
								userBO = userManageService.getUserInfoByUserId(Long.valueOf(userId));
								orgInfoBO = orgSocialInfoService.findByOrgId(Long.valueOf(orgId));
								
								if(userBO != null && orgInfoBO != null) {
									nextUserId.append(",").append(userBO.getUserId());
									nextuserName.append(",").append(userBO.getPartyName());
									nextOrgId.append(",").append(orgInfoBO.getOrgId());
									nextOrgName.append(",").append(orgInfoBO.getOrgName());
								}
							}
						}
						
						if(nextUserId.length() > 0) {
							resultMap.put("userIds", nextUserId.substring(1));
							resultMap.put("userNames", nextuserName.substring(1));
							resultMap.put("orgIds", nextOrgId.substring(1));
							resultMap.put("orgNames", nextOrgName.substring(1));
						}
					}
				} else {
					String orgChiefLevel = null,userOrgType = null;
					List<UserInfo> userInfoList = null;
					
					if(orgInfo != null) {
						orgChiefLevel = orgInfo.getChiefLevel();
					}
					//当前用户所属组织组织类型
					if(userOrgInfo != null){
						userOrgType = userOrgInfo.getOrgType();
					}

					if(ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel) || ConstantValue.GRID_ORG_LEVEL.equals(orgChiefLevel) || ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)) {
						UserInfo userInfoTmp = new UserInfo();
						int toLevel = nodeCodeHandler.getToLevel();

						userInfoTmp.setOrgId(orgInfo.getOrgId());
						userInfoTmp.setOrgName(orgInfo.getOrgName());
						userInfoTmp.setOrgCode(orgInfo.getOrgCode());

						if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
								&& (FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)
										|| FocusReportNode350Enum.ASSIGN_TASK_NODE_NAME.getTaskNodeName().equals(nodeName))) {
							String nodeCodeTmp = null;
							List<GdZTreeNode> treeNodeList = null;
							Node node = null;
							userInfoList = new ArrayList<UserInfo>();

							//获取市两违办
							node = baseWorkflowService.capNodeInfo(workflowName, FocusReportNode350Enum.WORKFLOW_TYPE_ID.toString(), reportId, FocusReportNode350Enum.ASSIGN_TASK_NODE_NAME.getTaskNodeName(), userInfoTmp);
							if(node != null) {
								params.put("nodeId", node.getNodeId());
								nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.GRID + INodeCodeHandler.OPERATE_REPORT + (INodeCodeHandler.GRID - INodeCodeHandler.COUNTY) + INodeCodeHandler.ORG_DEPT + INodeCodeHandler.COUNTY;
								treeNodeList = super.getTreeForEvent(userInfoTmp, null, nodeCodeTmp, null, null, params);

								if(treeNodeList != null) {
									for(GdZTreeNode treeNode : treeNodeList) {
										userInfoList.addAll(super.getUserInfoList(Long.valueOf(treeNode.getId()), nodeCodeTmp, params));
									}
								}
							}

							//用于启动子流程“南安两违防治事件_报告整治进展情况”
							//userInfoList.add(userInfo);
						} else if(nodeCodeHandler.isToGrid()) {
							userInfoList = super.getUserInfoList(orgInfo.getOrgId(), nodeCode, params);
						} else if(toLevel == INodeCodeHandler.COMMUNITY) {
							//网格层级获取主送人
							if(ConstantValue.GRID_ORG_LEVEL.equals(orgChiefLevel)){
								userInfoList = super.getUserInfoList(orgInfo.getParentOrgId(), nodeCode, params);
							}else if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)){
								//村社区层级获取主送人
								userInfoList = super.getUserInfoList(orgInfo.getOrgId(), nodeCode, params);
							}
						} else if(toLevel > 0 && toLevel < INodeCodeHandler.COMMUNITY) {
							//发生地址为网格层级
							String nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.GRID + INodeCodeHandler.OPERATE_REPORT + (INodeCodeHandler.GRID - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
							//发生地址为村社区层级
							if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)){
								nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.COMMUNITY + INodeCodeHandler.OPERATE_REPORT + (INodeCodeHandler.COMMUNITY - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
							}else if(ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel)){
								//发生地址为乡镇街道（目前只适配了房屋安全隐患流程）
								if(StringUtils.isNotBlank(userOrgType)){
									if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(userOrgType)){
										nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.COUNTY + INodeCodeHandler.OPERATE_SEND + (INodeCodeHandler.STREET - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
									}else {
										nodeCodeTmp = INodeCodeHandler.ORG_DEPT + INodeCodeHandler.COUNTY + INodeCodeHandler.OPERATE_SEND + (INodeCodeHandler.STREET - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
									}
								}else {
									nodeCodeTmp = INodeCodeHandler.ORG_DEPT + INodeCodeHandler.COUNTY + INodeCodeHandler.OPERATE_SEND + (INodeCodeHandler.STREET - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
								}
							}
							//需要进行组织选择
							if(nodeCodeHandler.isToDept() 
									&& FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
									&& (FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName().equals(nodeName) 
											|| FocusReportNode350Enum.DEPARTMENT_CHECK_TASK_NODE_NAME.getTaskNodeName().equals(nodeName))) {
								isUserRadioStyle = false;
								isSelectOrg = true;
								resultMap.put("eventNodeCode", nodeCodeHandler);
							}else if(nodeCodeHandler.isToDept()
									&& FocusReportNode3502Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
									&& FocusReportNode3502Enum.HANDLE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName) ){
								nodeCodeTmp = INodeCodeHandler.ORG_DEPT + INodeCodeHandler.COUNTY + INodeCodeHandler.OPERATE_FLOW + (INodeCodeHandler.COUNTY - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
								nodeCodeHandler = this.createNodeCodeHandle(nodeCodeTmp);
								isUserRadioStyle = false;
								isSelectOrg = true;
								resultMap.put("eventNodeCode", nodeCodeHandler);
							} else {
								List<GdZTreeNode> treeNodeList = super.getTreeForEvent(userInfoTmp, null, nodeCodeTmp, null, null, params);
								
								if(treeNodeList != null) {
									userInfoList = new ArrayList<UserInfo>();
									for(GdZTreeNode treeNode : treeNodeList) {
										userInfoList.addAll(super.getUserInfoList(Long.valueOf(treeNode.getId()), nodeCodeTmp, params));
									}
								}
							}
						} else if(toLevel < 0) {
							isUserRadioStyle = false;
							resultMap = super.getNodeInfoForEvent(userInfoTmp, curnodeName, nodeName, nodeCode, nodeId, params);
						}
					} else {
						throw new ZhsqEventException("发生地址没有匹配到网格或村社区层级的组织信息！");
					}
					
					if(userInfoList != null) {
						StringBuffer userIds = new StringBuffer(""),
									 orgIds = new StringBuffer(""),
									 userNames = new StringBuffer(""),
									 orgNames = new StringBuffer(""),
									 userSetKey = new StringBuffer("");
						Long userId = null, userOrgId = null;
						Set<String> userSet = new HashSet<String>();
						
						for(UserInfo userInfoTmp : userInfoList) {
							userId = userInfoTmp.getUserId();
							userOrgId = userInfoTmp.getOrgId();
							userSetKey = new StringBuffer("");
							
							userSetKey.append(userId).append("-").append(userOrgId);
							
							if(!userSet.contains(userSetKey.toString())) {//用户判重
								userSet.add(userSetKey.toString());
								
								userIds.append(",").append(userId);
								orgIds.append(",").append(userOrgId);
								userNames.append(",").append(userInfoTmp.getPartyName());
								orgNames.append(",").append(userInfoTmp.getOrgName());
							}
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
						if(orgNames.length() > 0) {
							resultMap.put("orgNames", orgNames.substring(1));
						}
					}
				}
				
				resultMap.put("isSelectUser", false);
				resultMap.put("isSelectOrg", isSelectOrg);
				//主送人员设置为单选
				resultMap.put("isUserRadioStyle", isUserRadioStyle);
				
				if(isUserRadioStyle && CommonFunctions.isNotBlank(resultMap, "userIds")) {
					String userIds = resultMap.get("userIds").toString();
					//只有当主送为一个人时，默认选中该人员
					resultMap.put("isUserDefaulCheck", userIds.split(",").length == 1);
				}
			}
			
			String adviceNoteInitial = null, adviceNote = null;
			Map<String, Object> adviceNoteMap = new HashMap<String, Object>();
			
			adviceNoteMap.putAll(params);
			adviceNoteMap.putAll(resultMap);
			adviceNoteMap.putAll(reportFocusMap);
			adviceNoteMap.put("isReplaceAdviceNote", false);
			
			adviceNoteInitial = capAdviceNote(proInstance, userInfo, adviceNoteMap);
			adviceNote = capAdviceNote(adviceNoteInitial, userInfo, adviceNoteMap);
			
			Map<String, List<UserBO>> cfgMap = null;
			Map<String, Object> cfgParams = new HashMap<String, Object>();
			String wfEndNodeName = nodeName;
			
			if(CommonFunctions.isNotBlank(params, "wfEndNodeName")) {
				wfEndNodeName = params.get("wfEndNodeName").toString();
			}
			
			if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&& CommonFunctions.isBlank(params, "wfNodeNameList")) {
				List<Map<String, Object>> wfNodeNameList = new ArrayList<Map<String, Object>>();
				Map<String, Object> wfNodeName = new HashMap<String, Object>();
				
				wfNodeName.put("wfStartNodeName", curnodeName);
				wfNodeName.put("wfEndNodeName", wfEndNodeName);
				wfNodeNameList.add(wfNodeName);
				
				if(CommonFunctions.isNotBlank(reportFocusMap, "dataSource")) {
					wfNodeName = new HashMap<String, Object>();
					wfEndNodeName = nodeName + "-" + reportFocusMap.get("dataSource");
					
					wfNodeName.put("wfStartNodeName", curnodeName);
					wfNodeName.put("wfEndNodeName", wfEndNodeName);
					wfNodeNameList.add(wfNodeName);
				}
				
				cfgParams.put("wfNodeNameList", wfNodeNameList);
			}

			cfgParams.put("workflowName", workflowName);
			cfgParams.put("wfEndNodeName", wfEndNodeName);
			//启用开始节点配置参数获取人员配置信息
			if(CommonFunctions.isNotBlank(params, "wfStartNodeName")) {
				cfgParams.put("wfStartNodeName", params.get("wfStartNodeName").toString());
			} else {
				cfgParams.put("wfStartNodeName", curnodeName);
			}
			cfgParams.put("ccType", ReportMsgCCTypeEnum.CCTYPE_DISTRIBUTE.getCcType() + "," + ReportMsgCCTypeEnum.CCTYPE_SELECT.getCcType());
			
			if(CommonFunctions.isNotBlank(params, "isAlterBenchmark") && CommonFunctions.isNotBlank(params, "benchmarkRegionCode")) {
				if(Boolean.valueOf(params.get("isAlterBenchmark").toString())) {
					orgParam = new HashMap<String, Object>();
					orgInfo = null;
					
					orgParam.put("regionCode", params.get("benchmarkRegionCode").toString());
					orgParam.put("orgType", ConstantValue.ORG_TYPE_UNIT);
					orgParam.put("professionCode", ConstantValue.GOV_PROFESSION_CODE);
					
					orgInfoList = orgSocialInfoService.findOrgSocialListByPara(orgParam);
					
					if(orgInfoList != null && orgInfoList.size() > 0) {
						orgInfo = orgInfoList.get(0);
					}
				}
			}
			
			if(orgInfo != null) {
				cfgMap = reportMsgCCCfgService.findCfg4User(cfgParams, orgInfo);
			}
			
			if(cfgMap != null && cfgMap.size() > 0) {
				String ccTypeDistribute = ReportMsgCCTypeEnum.CCTYPE_DISTRIBUTE.getCcType(),
					   ccTypeSelect = ReportMsgCCTypeEnum.CCTYPE_SELECT.getCcType();
				
				//分送人员
				if(cfgMap.containsKey(ccTypeDistribute)) {
					Map<String, Object> distributeUser = null;
					List<UserBO> distributeUserList = cfgMap.get(ccTypeDistribute);
					UserBO creatorUser = findExtraCCUser(curnodeName, nodeName, proInstance, params);
					
					if(creatorUser != null) {
						distributeUserList = distributeUserList == null ? new ArrayList<UserBO>() : distributeUserList;
						distributeUserList.add(creatorUser);
					}
					
					distributeUser = changUserBO2Map(distributeUserList);
					
					distributeUser.put("isDisplayUser", true);
					
					resultMap.put("distributeUser", distributeUser);
				}
				
				//选送人员
				if(cfgMap.containsKey(ccTypeSelect)) {
					Map<String, Object> selectUser = changUserBO2Map(cfgMap.get(ccTypeSelect));
					selectUser.put("isSelectUser", true);
					
					resultMap.put("selectUser", selectUser);
				}
			}
			
			boolean isShowText = false, isShowDict = false, isShowRegion = false, isShowNumInput = false;
			String textLabelName = "", dictLabelName = "", regionLabelName = "", numInputLabelName = "";
			Map<String, Object> dynamicContentMap = new HashMap<String, Object>();
			
			if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&& FocusReportNode350Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				String dynamicDictJson = "[{'name':'存在违法占地情况', 'value':'0'},{'name':'存在违法建设情况', 'value':'1'},{'name':'存在“两违”情况', 'value':'2'},{'name':'不存在“两违”情况', 'value':'3'},{'name':'不属本村', 'value':'4'}]";
				
				isShowDict = true;
				isShowText = true;
				textLabelName = "建设情况";
				dictLabelName = "两违情况";
				
				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
			} else if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
					&& FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				if(FocusReportNode350Enum.STREET_DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					isShowText = true;
					textLabelName = "所属村";
				} else if(FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					isShowText = true;
					textLabelName = "两违情况";
				}
			} else if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&& FocusReportNode350Enum.STREET_DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				List<OrgEntityInfoBO> orgEntityInfoList = null;
				String startDivisionCode = "-1";
				
				isShowRegion = true;
				regionLabelName = "所属区域";
				
				//获取当前登录账号下所管理的所有网格
				orgEntityInfoList = userInfo.getInfoOrgList();

				if (null != orgEntityInfoList && orgEntityInfoList.size() > 0) {
					StringBuffer orgCodeSb = new StringBuffer("");
					String infoOrgCode = null;
					
					for (OrgEntityInfoBO orgEntityInfo : orgEntityInfoList) {
						infoOrgCode = orgEntityInfo.getOrgCode();
						
						if(StringUtils.isNotBlank(infoOrgCode)) {
							orgCodeSb.append(",").append(infoOrgCode);
						}
					}
					
					if(orgCodeSb.length() > 0) {
						startDivisionCode = orgCodeSb.substring(1);//标准地址库控件起始网格
					}
				}
				
				dynamicContentMap.put("startDivisionCode", startDivisionCode);
			}  else if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&& FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				if(FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					isShowText = true;
					textLabelName = "整治情况";
				} else if(FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					String textJson = "[{'name':'整治情况'},{'name':'困难所在'}]";
					isShowText = true;
					
					dynamicContentMap.put("textJson", textJson);
				}
			}else if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&& FocusReportNode350Enum.STREET_FEEDBACK_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				if(FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					isShowText = true;
					textLabelName = "整治情况";
				} else if(FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					String textJson = "[{'name':'整治情况'},{'name':'困难所在'}]";
					isShowText = true;

					dynamicContentMap.put("textJson", textJson);
				}else if(ConstantValue.HANDLING_TASK_CODE.equals(nodeName)){
					//orgNames orgIds userIds
					//持续反馈时 下一办理人强制变更为当前环节办理人 默认选中
					resultMap.put("orgNames",userInfo.getOrgName());
					resultMap.put("orgIds",userInfo.getOrgId());
					resultMap.put("userIds",userInfo.getUserId());
					resultMap.put("userNames",userInfo.getPartyName());
					resultMap.put("isUserDefaulCheck", true);

					String textJson = "[{'name':'整治情况'},{'name':'困难所在'}]";
					isShowText = true;

					dynamicContentMap.put("textJson", textJson);
				}
			} else if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&& FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				if(FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)
						|| FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)
						|| FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)
						|| FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					isShowText = true;
					textLabelName = "处置内容";
				} else if(FocusReportNode350Enum.DEPARTMENT_CHECK_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					String dynamicDictJson = "[{'name':'是否需要复耕复绿', 'value':'0'},{'name':'是否存在超审批建设行为', 'value':'1'}]",
						   numInputOption = "{'min': 1, 'max': 99}";
					
					isShowDict = true;
					isShowNumInput = true;
					dictLabelName = "任务分解";
					numInputLabelName = "建筑楼层";
					
					dynamicContentMap.put("numInputOption", numInputOption);
					dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
				}
			} else if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
					&& FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				if(FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					isShowText = true;
					textLabelName = "处置情况";
				} else if(FocusReportNode350Enum.ASSIGN_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					isShowText = true;
					textLabelName = "处置情况";
				}else if(FocusReportNode350Enum.STREET_FEEDBACK_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)){
					String textJson = "[{'name':'整治情况'},{'name':'困难所在'}]";
					isShowText = true;

					dynamicContentMap.put("textJson", textJson);
				}
			} else if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
					&& FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				isShowText = true;
				textLabelName = "处理结果";
			} else if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
					&& FocusReportNode350Enum.DEPARTMENT_CHECK_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				String dynamicDictJson = "[{'name':'涉及田地', 'value':'0'},{'name':'不涉及田地', 'value':'1'},{'name':'存在超审批范围行为', 'value':'2'},{'name':'不存在超审批范围行为', 'value':'3'}]";
				
				isShowDict = true;
				dictLabelName = "核验结果";
				
				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
			} else if(
					(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&& ((FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)
							&& FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName().equals(nodeName))))
					|| (FocusReportNode3502Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
							&& FocusReportNode3502Enum.CHECK_TASK_NODE_NAME.getTaskNodeName().equals(nodeName))
					|| (FocusReportNode3501Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
							&& FocusReportNode3501Enum.ASSIGN_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName))) {
				isShowDict = true;
				dictLabelName = "处置情况";
				dynamicContentMap.put("dictPcode", TwoVioPreDictEnum.DISPOSAL_SITUATION.getDictCode());
			}else if(FocusReportNode3500Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				if(FocusReportNode3500Enum.DISASSEMBLE_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
					isShowText = true;
					textLabelName = "两违情况";
				} else if(FocusReportNode3500Enum.REGULAR_REPORT_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
					isShowDict = true;
					dictLabelName = "实地情况";
					dynamicContentMap.put("dictPcode", TwoVioPreDictEnum.FIELD_SITUATION.getDictCode());
				}
			}else if(FocusReportNode3502Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&& FocusReportNode3502Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
				isShowText = true;
				textLabelName = "处理结果";
			}
			
			if(StringUtils.isNotBlank(adviceNote)) {
				String splitStr = "#OR#";//当一个消息模板中包含有多种执行情况时，使用#OR#进行分割
				
				if(adviceNote.contains(splitStr)) {
					String[] adviceNoteArray = adviceNote.split(splitStr),
							 adviceNoteInitialArray = adviceNoteInitial.split(splitStr);
					
					for(int index = 0, len = adviceNoteArray.length; index < len; index++) {
						dynamicContentMap.put("adviceNote_" + index, adviceNoteArray[index]);
						dynamicContentMap.put("adviceNoteInitial_" + index, adviceNoteInitialArray[index]);
					}
				} else {
					resultMap.put("adviceNote", adviceNote);
					resultMap.put("adviceNoteInitial", adviceNoteInitial);
				}
			}
			
			dynamicContentMap.put("isShowText", isShowText);
			dynamicContentMap.put("isShowDict", isShowDict);
			dynamicContentMap.put("isShowRegion", isShowRegion);
			dynamicContentMap.put("isShowNumInput", isShowNumInput);
			dynamicContentMap.put("textLabelName", textLabelName);
			dynamicContentMap.put("dictLabelName", dictLabelName);
			dynamicContentMap.put("regionLabelName", regionLabelName);
			dynamicContentMap.put("numInputLabelName", numInputLabelName);
			resultMap.put("dynamicContentMap", dynamicContentMap);
		} else {
			resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
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
		String formTypeIdStr = null, curTaskName = null;
		
		if(proInstance != null) {
			curTaskName = proInstance.getCurNode();
			formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
		}
		
		trigCondition.append(ConstantValue.TWO_VIO_PRE_NOTE).append("-").append(proInstance.getProName()).append("-").append(curTaskName);
		
		if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& FocusReportNode350Enum.FIND_TWO_VIO_NODE_NAME.getTaskNodeName().equals(curTaskName)) {
			if(CommonFunctions.isNotBlank(params, "dataSource")) {
				trigCondition.append("-").append(params.get("dataSource"));
			}
		} else if(CommonFunctions.isNotBlank(params, "nodeName")) {
			trigCondition.append("-").append(params.get("nodeName"));
		}
		
		return trigCondition;
	}
	
	/**
	 * 获取消息模板
	 * @param proInstance	流程实例
	 * @param userInfo		操作用户
	 * @param twoVioPreMap	两位防治信息
	 * @return
	 */
	protected String capAdviceNote(ProInstance proInstance, UserInfo userInfo, Map<String, Object> twoVioPreMap) {
		StringBuffer trigCondition = capAdviceNoteCondition(proInstance, twoVioPreMap);
		String adviceNote = null;
		boolean isReplaceAdviceNote = true;
		
		//获取办理意见模板
		adviceNote = funConfigurationService.turnCodeToValue(ConstantValue.SMS_NOTE_TYPE, trigCondition.toString(), IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);
		
		if(CommonFunctions.isNotBlank(twoVioPreMap, "isReplaceAdviceNote")) {
			isReplaceAdviceNote = Boolean.valueOf(twoVioPreMap.get("isReplaceAdviceNote").toString());
		}
		
		if(isReplaceAdviceNote) {
			adviceNote = capAdviceNote(adviceNote, userInfo, twoVioPreMap);
		}
		
		return adviceNote;
	}
	
	/**
	 * 消息模板内容构造
	 * @param adviceNote
	 * @param userInfo
	 * @param params
	 * @return
	 */
	protected String capAdviceNote(String adviceNote, UserInfo userInfo, Map<String, Object> params) {
		if(StringUtils.isNotBlank(adviceNote)) {
			if(adviceNote.contains("@reportUUID@") && CommonFunctions.isNotBlank(params, "reportUUID")) {
				adviceNote = adviceNote.replace("@reportUUID@", params.get("reportUUID").toString());
			}
			
			if(adviceNote.contains("@reportCode@") && CommonFunctions.isNotBlank(params, "reportCode")) {
				String reportCode = params.get("reportCode").toString();
				
				if(!adviceNote.contains("@mapNum@") && CommonFunctions.isNotBlank(params, "mapNum")) {
					reportCode = "报告编号：" + reportCode + "；图斑编号：" + params.get("mapNum");
				}
				
				adviceNote = adviceNote.replace("@reportCode@", reportCode);
			}
			
			if(adviceNote.contains("@reporterName@") && CommonFunctions.isNotBlank(params, "reporterName")) {
				adviceNote = adviceNote.replace("@reporterName@", params.get("reporterName").toString());
			}

			if(adviceNote.contains("@curUserOrgName@")) {
				adviceNote = adviceNote.replace("@curUserOrgName@", userInfo.getOrgName());
			}
			
			if(adviceNote.contains("@reportDayZH@") && CommonFunctions.isNotBlank(params, "reportTime")) {
				adviceNote = adviceNote.replace("@reportDayZH@", DateUtils.formatDate((Date) params.get("reportTime"), "yyyy年MM月dd日"));
			}
			
			if(adviceNote.contains("@reportTimeMinZH@")) {
				if(CommonFunctions.isNotBlank(params, "reportTime")) {
					adviceNote = adviceNote.replace("@reportTimeMinZH@", DateUtils.formatDate((Date) params.get("reportTime"), "yyyy年MM月dd日HH时mm分"));
				}
			}
			
			if(adviceNote.contains("@nowDayZH@")) {
				adviceNote = adviceNote.replace("@nowDayZH@", DateUtils.getToday("yyyy年MM月dd日"));
			}
			
			if(adviceNote.contains("@feedbackDay@")) {
				String feedbackDayStr = "";
				
				if(CommonFunctions.isNotBlank(params, "feedbackTime")){
					Date feedbackTime = (Date) params.get("feedbackTime");
					feedbackDayStr = DateUtils.formatDate(feedbackTime, DateUtils.PATTERN_DATE);
				}
				
				adviceNote = adviceNote.replace("@feedbackDay@", feedbackDayStr);
			}
			
			if(adviceNote.contains("@feedbackDayZH@")) {
				String feedbackDayZH = "";
				
				if(CommonFunctions.isNotBlank(params, "feedbackTime")){
					feedbackDayZH = DateUtils.formatDate((Date) params.get("feedbackTime"), "yyyy年MM月dd日");
				}
				
				adviceNote = adviceNote.replace("@feedbackDayZH@", feedbackDayZH);
			}
			
			if(adviceNote.contains("@feedbackHourZH@")) {
				if(CommonFunctions.isNotBlank(params, "feedbackTime")) {
					adviceNote = adviceNote.replace("@feedbackHourZH@", DateUtils.formatDate((Date) params.get("feedbackTime"), "yyyy年MM月dd日HH时"));
				}
			}
			
			if(adviceNote.contains("@feedbackMinZH@")) {
				if(CommonFunctions.isNotBlank(params, "feedbackTime")) {
					adviceNote = adviceNote.replace("@feedbackMinZH@", DateUtils.formatDate((Date) params.get("feedbackTime"), "yyyy年MM月dd日HH时MM分"));
				}
			}
			
			if(adviceNote.contains("@occurred@")) {
				String occurred = "";
				
				if(CommonFunctions.isNotBlank(params, "occurred")) {
					occurred = params.get("occurred").toString();
				}
				
				adviceNote = adviceNote.replace("@occurred@", occurred);
			}
			
			if(adviceNote.contains("@mapNum@") && CommonFunctions.isNotBlank(params, "mapNum")) {
				adviceNote = adviceNote.replace("@mapNum@", params.get("mapNum").toString());
			}
			
			if(adviceNote.contains("@tipoffContent@") && CommonFunctions.isNotBlank(params, "tipoffContent")) {
				adviceNote = adviceNote.replace("@tipoffContent@", params.get("tipoffContent").toString());
			}
			
			if(adviceNote.contains("@areaCovered@") && CommonFunctions.isNotBlank(params, "areaCovered")) {
				adviceNote = adviceNote.replace("@areaCovered@", params.get("areaCovered").toString());
			}
			
			if(adviceNote.contains("@cultivableLandArea@") && CommonFunctions.isNotBlank(params, "cultivableLandArea")) {
				adviceNote = adviceNote.replace("@cultivableLandArea@", params.get("cultivableLandArea").toString());
			}
			
			if(adviceNote.contains("@personInvolved@")) {
				String personInvolved = "";
				
				if(CommonFunctions.isNotBlank(params, "personInvolved")) {
					personInvolved = params.get("personInvolved").toString();
				}
				
				adviceNote = adviceNote.replace("@personInvolved@", personInvolved);
			}
			
			if(adviceNote.contains("@buildingUsageName@")) {
				String buildingUsageName = "";
				
				if(CommonFunctions.isNotBlank(params, "buildingUsageName")) {
					buildingUsageName = params.get("buildingUsageName").toString();
				}
				
				adviceNote = adviceNote.replace("@buildingUsageName@", buildingUsageName);
			}
			
			if(adviceNote.contains("@gridRegionName@") || adviceNote.contains("@communityRegionName@") || adviceNote.contains("@streetRegionName@") 
					&& CommonFunctions.isNotBlank(params, "regionCode")) {
				OrgEntityInfoBO regionInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(params.get("regionCode").toString());
				String gridRegionName = "", communityRegionName = "", streetRegionName = "", regionChiefLevel = null;
				
				while(regionInfo != null) {
					regionChiefLevel = regionInfo.getChiefLevel();
					
					if(ConstantValue.GRID_ORG_LEVEL.equals(regionChiefLevel)) {
						gridRegionName = regionInfo.getOrgName();
					} else if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)) {
						communityRegionName = regionInfo.getOrgName();
					} else if(ConstantValue.STREET_ORG_LEVEL.equals(regionChiefLevel)) {
						streetRegionName = regionInfo.getOrgName();
					} else {
						break;
					}
					
					regionInfo = orgEntityInfoService.findByOrgId(regionInfo.getParentOrgId());
				}
				
				adviceNote = adviceNote.replace("@gridRegionName@", gridRegionName)
									   .replace("@communityRegionName@", communityRegionName)
									   .replace("@streetRegionName@", streetRegionName);
			}
			
			if(adviceNote.contains("@nextUserNames@") && CommonFunctions.isNotBlank(params, "userNames")) {
				adviceNote = adviceNote.replace("@nextUserNames@", params.get("userNames").toString());
			}
			
			if(adviceNote.contains("@curOrgName@") && userInfo != null) {
				adviceNote = adviceNote.replace("@curOrgName@", userInfo.getOrgName());
			}
			
			if(adviceNote.contains("@identificationResultName@")) {
				String identificationResultName = "";
				
				if(CommonFunctions.isNotBlank(params, "identificationResultName")) {
					identificationResultName = params.get("identificationResultName").toString();
				}
				
				adviceNote = adviceNote.replace("@identificationResultName@", identificationResultName);
			}
		}
		
		return adviceNote;
	}
	
	/**
	 * 获取配置体系外的消息配送人员信息
	 * @param curTaskName	当前环节名称
	 * @param nextTaskName	下一办理环节名称
	 * @param proInstance	流程实例信息
	 * @param params
	 * @return
	 */
	protected UserBO findExtraCCUser(String curTaskName, String nextTaskName, ProInstance proInstance, Map<String, Object> params) {
		Long creatorId = null, creatorOrgId = null;
		String creatorName = null, creatorOrgName = null,
			   formTypeIdStr = null;
		UserBO creatorBO = null;
		boolean isAble2CC = false;
		
		if(proInstance != null) {
			creatorId = proInstance.getUserId();
			creatorName = proInstance.getUserName();
			creatorOrgId = proInstance.getOrgId();
			creatorOrgName = proInstance.getOrgName();
			formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
		}
		
		if(
				(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
						&& (FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName().equals(nextTaskName)
								|| FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(curTaskName)
								|| FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName().equals(nextTaskName)))
				|| (FocusReportNode3502Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
						&& FocusReportNode3502Enum.CHECK_TASK_NODE_NAME.getTaskNodeName().equals(nextTaskName))) {
			isAble2CC = true;
		}
		
		if(isAble2CC) {
			if(creatorId != null && creatorId > 0 && StringUtils.isBlank(creatorName)) {
				UserBO userBO = userManageService.getUserInfoByUserId(creatorId);
				
				if(userBO != null && userBO.getUserId() != null) {
					creatorName = userBO.getPartyName();
				} else {
					creatorId = null;
				}
			}
			
			if(creatorOrgId != null && creatorOrgId > 0 && StringUtils.isBlank(creatorOrgName)) {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(creatorOrgId);
				if(orgInfo != null && orgInfo.getOrgId() != null) {
					creatorOrgName = orgInfo.getOrgName();
				} else {
					creatorOrgId = null;
				}
			}
			
			if(creatorId != null && creatorId > 0
					&& creatorOrgId != null && creatorOrgId > 0) {
				creatorBO = new UserBO();
				
				if(creatorId != null && creatorId > 0) {
					creatorBO.setUserId(creatorId);
					creatorBO.setPartyName(creatorName);
				}
				
				if(creatorOrgId != null && creatorOrgId > 0) {
					creatorBO.setSocialOrgId(creatorOrgId.toString());
					creatorBO.setOrgName(creatorOrgName);
				}
			}
		}
		
		return creatorBO;
	}
	
	/**
	 * 将用户信息转换为map
	 * @param userList
	 * @return
	 * 		userIds		用户id，多个值使用英文逗号连接
	 * 		userNames	用户姓名，多个值使用英文逗号连接
	 * 		orgIds		组织id，多个值使用英文逗号连接
	 * 		orgNames	组织名称，多个值使用英文逗号连接
	 */
	protected Map<String, Object> changUserBO2Map(List<UserBO> userList) {
		Map<String, Object> userMap = null;
		
		if(userList != null) {
			StringBuffer userIds = new StringBuffer(""),
						 userNames = new StringBuffer(""),
						 userOrgNames = new StringBuffer(""),
						 orgIds = new StringBuffer(""),
						 orgNames = new StringBuffer(""),
						 userOrgBuffer = null;
			userMap = new HashMap<String, Object>();
			Set<String> userOrgIdSet = new HashSet<String>();
			Long userId = null;
			String orgIdStr = null;
			
			for(UserBO userBO : userList) {
				userOrgBuffer = new StringBuffer("");
				userId = userBO.getUserId();
				orgIdStr = userBO.getSocialOrgId();
				
				userOrgBuffer.append(userId).append("-").append(orgIdStr);
				
				if(!userOrgIdSet.contains(userOrgBuffer.toString())) {
					userIds.append(",").append(userId);
					userNames.append(",").append(userBO.getPartyName());
					userOrgNames.append(",").append(userBO.getPartyName()).append("(").append(userBO.getOrgName()).append(")");
					orgIds.append(",").append(orgIdStr);
					orgNames.append(",").append(userBO.getOrgName());
					
					userOrgIdSet.add(userOrgBuffer.toString());
				}
			}
			
			if(userIds.length() > 0) {
				userMap.put("userIds", userIds.substring(1));
			}
			if(userNames.length() > 0) {
				userMap.put("userNames", userNames.substring(1));
			}
			if(userOrgNames.length() > 0) {
				userMap.put("userOrgNames", userOrgNames.substring(1));
			}
			if(orgIds.length() > 0) {
				userMap.put("orgIds", orgIds.substring(1));
			}
			if(orgNames.length() > 0) {
				userMap.put("orgNames", orgNames.substring(1));
			}
		}
		
		return userMap;
	}
}
