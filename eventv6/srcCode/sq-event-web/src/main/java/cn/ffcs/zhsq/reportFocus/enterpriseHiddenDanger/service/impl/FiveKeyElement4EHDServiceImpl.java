package cn.ffcs.zhsq.reportFocus.enterpriseHiddenDanger.service.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.IReportMsgCCCfgService;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.service.impl.ReportMsgCCTypeEnum;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.FiveKeyElement4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 企业安全隐患人员选择
 * @ClassName:   FiveKeyElement4EHDServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月9日 上午10:11:32
 */
@Service(value = "fiveKeyElement4EHDService")
public class FiveKeyElement4EHDServiceImpl extends FiveKeyElement4TwoVioPreServiceImpl {
	@Autowired
	private IReportIntegrationService reportIntegrationService;
	
	@Autowired
	private IReportMsgCCCfgService reportMsgCCCfgService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
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
		String reportType = ReportTypeEnum.enterpriseHiddenDanger.getReportTypeIndex();
		//消息模板获取
		String adviceNoteInitial = null, adviceNote = null;
		Map<String, Object> adviceNoteMap = new HashMap<String, Object>();
		
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
		}

		reportParams.putAll(params);
		reportParams.put("reportId", reportId);
		reportParams.put("reportType", reportType);
		reportFocusMap = reportIntegrationService.findReportFocusByUUID(null, userInfo, reportParams);

		//获取消息模板
		adviceNoteMap.putAll(params);
		adviceNoteMap.putAll(resultMap);
		adviceNoteMap.putAll(reportFocusMap);
		adviceNoteMap.put("isReplaceAdviceNote", false);
		
		adviceNoteInitial = capAdviceNote(proInstance, userInfo, adviceNoteMap);
		adviceNote = capAdviceNote(adviceNoteInitial, userInfo, adviceNoteMap);

		Map<String, Object> dynamicContentMap = new HashMap<String, Object>();
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

		//根据隐患信息所属地域编码获取组织信息
		OrgSocialInfoBO orgInfo = null;
		if(CommonFunctions.isNotBlank(reportFocusMap, "regionCode")) {
			Map<String, Object> orgParam = new HashMap<String, Object>();
			List<OrgSocialInfoBO> orgInfoList = null;
			
			orgParam.put("regionCode", reportFocusMap.get("regionCode").toString());
			orgParam.put("orgType", ConstantValue.ORG_TYPE_UNIT);
			orgParam.put("professionCode", ConstantValue.GOV_PROFESSION_CODE);
			
			orgInfoList = orgSocialInfoService.findOrgSocialListByPara(orgParam);
			
			if(orgInfoList != null && orgInfoList.size() > 0) {
				orgInfo = orgInfoList.get(0);
			}
		}

		if(nodeCodeHandler.isFromDept() && nodeCodeHandler.isEquality() && nodeCodeHandler.isToDept()) {
			resultMap.put("userIds", userInfo.getUserId());
			resultMap.put("userNames", userInfo.getPartyName());
			resultMap.put("orgIds", userInfo.getOrgId());
			resultMap.put("isSelectUser", false);
			resultMap.put("isSelectOrg", false);
			resultMap.put("isDisplayUser", true);
			//单独获取分送选送人员
			resultMap.putAll(this.getUserInfoByCfg(workflowName,nodeName,orgInfo,curnodeName,proInstance,params));
		} else {
			if(null != orgInfo) {
				if(nodeCodeHandler.isToBegin() && proInstance != null) {
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
				} else {
					String orgChiefLevel = null;
					List<UserInfo> userInfoList = null;
					
					if(orgInfo != null) {
						orgChiefLevel = orgInfo.getChiefLevel();
					}
					
					if(ConstantValue.GRID_ORG_LEVEL.equals(orgChiefLevel) || ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)) {
						UserInfo userInfoTmp = new UserInfo();
						int toLevel = nodeCodeHandler.getToLevel();
						
						userInfoTmp.setOrgId(orgInfo.getOrgId());
						userInfoTmp.setOrgName(orgInfo.getOrgName());
						userInfoTmp.setOrgCode(orgInfo.getOrgCode());
						
						if(nodeCodeHandler.isToGrid()) {
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
							}

							//需要进行组织选择
							if(nodeCodeHandler.isToDept() 
									&& FocusReportNode351Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
									//&& FocusReportNode351Enum.DEPARTMENT_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)
									&& FocusReportNode351Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(nodeName)
								) {
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
					}
				}
				//办理页面是否展示选择人员或组织
				resultMap.put("isSelectUser", false);
				resultMap.put("isSelectOrg", isSelectOrg);

				//主送人员设置为单选
				resultMap.put("isUserRadioStyle", isUserRadioStyle);
				
				if(isUserRadioStyle && CommonFunctions.isNotBlank(resultMap, "userIds")) {
					String userIds = resultMap.get("userIds").toString();
					//只有当主送为一个人时，默认选中该人员
					resultMap.put("isUserDefaulCheck", userIds.split(",").length == 1);
				}
				
				//获取分送、选送人员
				
				if(CommonFunctions.isBlank(params, "dataSource") && CommonFunctions.isNotBlank(reportFocusMap, "dataSource")) {
					params.put("dataSource", reportFocusMap.get("dataSource"));
				}
				
				resultMap.putAll(this.getUserInfoByCfg(workflowName,nodeName,orgInfo,curnodeName,proInstance,params));
				
				boolean isShowText = false, isShowDict = false;
				String textLabelName = "", dictLabelName = "";
				
				if(FocusReportNode351Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
					if(FocusReportNode351Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
						//下一环节为归档
						//当前环节为-乡镇街道处理
						if(FocusReportNode351Enum.STEET_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
							String dynamicDictJson = "[{'name':'已整改到位', 'value':'0'},{'name':'不具备整改条件', 'value':'1'}]";

							isShowText = false;
							isShowDict = true;
							textLabelName = "整改内容";
							dictLabelName = "整改结果";
							dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
						}else if(FocusReportNode351Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(curnodeName)){
							//当前环节为-行政处罚
							isShowDict = true;
							isShowText = false;
							dictLabelName = "行政处罚";
							textLabelName = "处罚内容";
							dynamicContentMap.put("dictPcode", EHDDictEnum.ADMINISTRTIVE_SACTION.getDictCode());
						}

					}
				}
				
				dynamicContentMap.put("isShowText", isShowText);
				dynamicContentMap.put("isShowDict", isShowDict);
				dynamicContentMap.put("textLabelName", textLabelName);
				dynamicContentMap.put("dictLabelName", dictLabelName);
				resultMap.put("dynamicContentMap", dynamicContentMap);
			} else {
				resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
			}
		}
		//消息模板
		resultMap.put("adviceNote", adviceNote);
		//判断是否上传立案决定书task9-task10、行政处罚决定书task10-end1
		Boolean isUploadCasePic = false;
		Boolean isUploadPuniPic = false;
		//当下一环节为归档时，当前环节不能为“立案查处”或者“行政处罚”，增加处理后图片必填约束
		Boolean isUploadHandledPic = false;
		String picTypeName = "";
		if(FocusReportNode351Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(curnodeName)&&FocusReportNode351Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(nodeName)){
			isUploadCasePic = true;
			picTypeName = "立案决定书";
		}else if(FocusReportNode351Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(curnodeName)&&FocusReportNode351Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
			isUploadPuniPic = true;
			picTypeName = "行政处罚决定书";
		} else if(FocusReportNode351Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)
				&& !FocusReportNode351Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(curnodeName)
				&& !FocusReportNode351Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
			//当下一环节为归档时，当前环节不能为“立案查处”或者“行政处罚”，增加处理后图片必填约束
			isUploadHandledPic = true;
			picTypeName = "处理后";
		}

		resultMap.put("isUploadCasePic", isUploadCasePic);
		resultMap.put("isUploadPuniPic", isUploadPuniPic);
		resultMap.put("isUploadHandledPic", false);
		resultMap.put("picTypeName", picTypeName);

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
		
		trigCondition.append(ConstantValue.ENTERPRISE_HIDDEN_DANGER_NOTE).append("-").append(proInstance.getProName()).append("-").append(curTaskName);
		
		if(FocusReportNode351Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& FocusReportNode351Enum.FIND_EHD_NODE_NAME.getTaskNodeName().equals(curTaskName)) {
			if(CommonFunctions.isNotBlank(params, "nodeName")) {
				String nextNodeName = params.get("nodeName").toString();
				
				if(FocusReportNode351Enum.COMMUNITY_END_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
					trigCondition.append("-").append(nextNodeName);
				}
			}
			
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
			if(adviceNote.contains("@enterpriseName@") && CommonFunctions.isNotBlank(params, "enterpriseName")) {
				adviceNote = adviceNote.replace("@enterpriseName@", params.get("enterpriseName").toString());
			}
			if(adviceNote.contains("@hiddenDangerTypeName@") && CommonFunctions.isNotBlank(params, "hiddenDangerTypeName")) {
				adviceNote = adviceNote.replace("@hiddenDangerTypeName@", params.get("hiddenDangerTypeName").toString());
			}
		}
		
		return adviceNote;
	}

	@Override
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

		//获取配置外的选送人员信息 task5-task6、task5-end1、task6-task7、task6-end1、task7-task8、task8-task9 task9-task10 task9-end1 task10-end1
		//modify by ztc 2021-03-08 curnodeName-nodeName:task7-task8、task8-task9 => task7-task9（流程图去除牵头部门处理环节）
		if(FocusReportNode351Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
			&&(
				(FocusReportNode351Enum.VICE_COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curTaskName) && FocusReportNode351Enum.STEET_HANDLE_NODE_NAME.getTaskNodeName().equals(nextTaskName))
				||(FocusReportNode351Enum.VICE_COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curTaskName) && FocusReportNode351Enum.END_NODE_NAME.getTaskNodeName().equals(nextTaskName))
				||(FocusReportNode351Enum.STEET_HANDLE_NODE_NAME.getTaskNodeName().equals(curTaskName) && FocusReportNode351Enum.SAB_HANDLE_NODE_NAME.getTaskNodeName().equals(nextTaskName))
				||(FocusReportNode351Enum.STEET_HANDLE_NODE_NAME.getTaskNodeName().equals(curTaskName) && FocusReportNode351Enum.END_NODE_NAME.getTaskNodeName().equals(nextTaskName))
				/*||(FocusReportNode351Enum.SAB_HANDLE_NODE_NAME.getTaskNodeName().equals(curTaskName) && FocusReportNode351Enum.DEPARTMENT_HANDLE_NODE_NAME.getTaskNodeName().equals(nextTaskName))
				||(FocusReportNode351Enum.DEPARTMENT_HANDLE_NODE_NAME.getTaskNodeName().equals(curTaskName) && FocusReportNode351Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(nextTaskName))*/
				||(FocusReportNode351Enum.SAB_HANDLE_NODE_NAME.getTaskNodeName().equals(curTaskName) && FocusReportNode351Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(nextTaskName))
				||(FocusReportNode351Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(curTaskName) && FocusReportNode351Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(nextTaskName))
				||(FocusReportNode351Enum.FILE_CASE_NODE_NAME.getTaskNodeName().equals(curTaskName) && FocusReportNode351Enum.END_NODE_NAME.getTaskNodeName().equals(nextTaskName))
				||(FocusReportNode351Enum.ADMINISTRTIVE_SACTION_NODE_NAME.getTaskNodeName().equals(curTaskName) && FocusReportNode351Enum.END_NODE_NAME.getTaskNodeName().equals(nextTaskName))
			)
		) {
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
	 * 获取分送人员distributeUser、选送人员selectUser
	 * */
	private Map<String,Object> getUserInfoByCfg(String workflowName,String nodeName,OrgSocialInfoBO orgInfo,
												String curnodeName,ProInstance proInstance,Map<String,Object> params)
	{

		Map<String, List<UserBO>> cfgMap = null;
		Map<String, Object> cfgParams = new HashMap<String, Object>();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		StringBuffer wfEndNodeName = new StringBuffer("");
		
		wfEndNodeName.append(nodeName);
		
		if(FocusReportNode351Enum.FIND_EHD_NODE_NAME.getTaskNodeName().equals(curnodeName) 
				&& (FocusReportNode351Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)
						|| FocusReportNode351Enum.COMMUNITY_END_NODE_NAME.getTaskNodeName().equals(nodeName))) {
			if(CommonFunctions.isNotBlank(params, "dataSource")) {
				wfEndNodeName.append("-").append(params.get("dataSource"));
			}
		}
		
		cfgParams.put("workflowName", workflowName);
		cfgParams.put("wfStartNodeName", curnodeName);
		cfgParams.put("wfEndNodeName", wfEndNodeName.toString());
		cfgParams.put("ccType", ReportMsgCCTypeEnum.CCTYPE_DISTRIBUTE.getCcType() + "," + ReportMsgCCTypeEnum.CCTYPE_SELECT.getCcType());

		cfgMap = reportMsgCCCfgService.findCfg4User(cfgParams, orgInfo);

		//获取配置中的人员配置信息
		//选送人员
		Map<String, Object> selectUser = null;
		if(cfgMap != null && cfgMap.size() > 0) {
			String ccTypeDistribute = ReportMsgCCTypeEnum.CCTYPE_DISTRIBUTE.getCcType(),
					ccTypeSelect = ReportMsgCCTypeEnum.CCTYPE_SELECT.getCcType();

			//分送人员
			if(cfgMap.containsKey(ccTypeDistribute)) {
				Map<String, Object> distributeUser = null;
				List<UserBO> distributeUserList = cfgMap.get(ccTypeDistribute);

				distributeUser = changUserBO2Map(distributeUserList);

				distributeUser.put("isDisplayUser", true);

				resultMap.put("distributeUser", distributeUser);
			}

			//选送人员
			if(cfgMap.containsKey(ccTypeSelect)) {
				selectUser = changUserBO2Map(cfgMap.get(ccTypeSelect));
				selectUser.put("isSelectUser", true);

				resultMap.put("selectUser", selectUser);
			}
		}

		//获取配置外的人员信息
		//获取配置外的选送人员信息 curnodeName-nodeName task5-task6、task5-end1、task6-task7、task6-end1、task7-task8、task8-task9 task9-task10 task9-end1 task10-end1
		//modify by ztc 2021-03-08 curnodeName-nodeName:task7-task8、task8-task9 => task7-task9（流程图去除牵头部门处理环节）
		List<UserBO> selectUserList = new ArrayList<UserBO>();
		UserBO creatorUser = findExtraCCUser(curnodeName, nodeName, proInstance, params);
		selectUser = selectUser == null? new HashMap<>() : selectUser;
		if(creatorUser != null) {
			selectUserList.add(creatorUser);
			selectUser.putAll(changUserBO2Map(selectUserList));
		}
		//发起人不为空才展示选送人员
		if(MapUtils.isNotEmpty(selectUser)){
			selectUser.put("isSelectUser", true);
			resultMap.put("selectUser", selectUser);
		}
		return resultMap;
	}
}
