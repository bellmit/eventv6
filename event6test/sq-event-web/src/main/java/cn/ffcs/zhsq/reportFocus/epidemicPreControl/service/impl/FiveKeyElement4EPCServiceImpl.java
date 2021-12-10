package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
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
 * @Description: 疫情防控(Epidemic Prevention and Control)人员选择
 * @ClassName:   FiveKeyElement4EPCServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月11日 下午2:20:03
 */
@Service(value = "fiveKeyElement4EPCService")
public class FiveKeyElement4EPCServiceImpl extends FiveKeyElement4TwoVioPreServiceImpl {
	@Autowired
	private IReportIntegrationService reportIntegrationService;
	
	@Autowired
	private IReportMsgCCCfgService reportMsgCCCfgService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		INodeCodeHandler nodeCodeHandler = null;
		Long reportId = null, instanceId = null;
		Map<String, Object> reportParams = new HashMap<String, Object>(),
							reportFocusMap = null,
							resultMap = new HashMap<String, Object>();
		ProInstance proInstance = null;
		String formTypeIdStr = null, workflowName = null;
		boolean isUserRadioStyle = true, isSelectOrg = false;
		String reportType = ReportTypeEnum.epidemicPreControl.getReportTypeIndex(),
			   collectSource = null;
		
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
		
		if(CommonFunctions.isNotBlank(reportFocusMap, "collectSource")) {
			collectSource = reportFocusMap.get("collectSource").toString();
		}
		
		if(FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& FocusReportNode352Enum.NOT_VERIFIED_NODE_NAME.getTaskNodeName().equals(nodeName)
				&& EPCCollectSourceEnum.GRID_INSPECT.getCollectSource().equals(collectSource)
				&& StringUtils.isNotBlank(nodeCode)) {
			nodeCode = nodeCode.replace(INodeCodeHandler.OPERATE_BEGIN, INodeCodeHandler.OPERATE_REPORT_X);
		}
		
		nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		
		if(CommonFunctions.isNotBlank(reportFocusMap, "regionCode")) {
			OrgSocialInfoBO orgInfo = null;
			String orgInfoType = null;
			Map<String, Object> orgParam = new HashMap<String, Object>();
			List<OrgSocialInfoBO> orgInfoList = null;
			
			orgParam.put("regionCode", reportFocusMap.get("regionCode").toString());
			orgParam.put("orgType", ConstantValue.ORG_TYPE_UNIT);
			orgParam.put("professionCode", ConstantValue.GOV_PROFESSION_CODE);
			
			orgInfoList = orgSocialInfoService.findOrgSocialListByPara(orgParam);
			
			if(orgInfoList != null && orgInfoList.size() > 0) {
				orgInfo = orgInfoList.get(0);
			}
			
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
					orgInfoType = orgInfo.getOrgType();
				}

				//add ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel) by ztc 20210422 需求单号：5434653
				if(ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel) ||ConstantValue.GRID_ORG_LEVEL.equals(orgChiefLevel) || ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)) {
					UserInfo userInfoTmp = new UserInfo();
					int toLevel = nodeCodeHandler.getToLevel();
					
					userInfoTmp.setOrgId(orgInfo.getOrgId());
					userInfoTmp.setOrgName(orgInfo.getOrgName());
					userInfoTmp.setOrgCode(orgInfo.getOrgCode());
					
					if(nodeCodeHandler.isOrganization()) {
						String appointedOrgCode = funConfigurationService.turnCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, workflowName + "-" + nodeName, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
						Long appointedOrgId = null;
						
						if(StringUtils.isNotBlank(appointedOrgCode)) {
							OrgSocialInfoBO orgInfoBO = orgSocialInfoService.selectOrgSocialInfoByOrgCode(appointedOrgCode);
							if(orgInfoBO != null) {
								appointedOrgId = orgInfoBO.getOrgId();
							}
						}
						
						if(appointedOrgId != null && appointedOrgId > 0) {
							userInfoList = this.getUserInfoList(appointedOrgId, nodeCode, params);
						} else {
							userInfoList = super.getUserInfoList(orgInfo.getOrgId(), nodeCode, params);
						}
					} else if(nodeCodeHandler.isToGrid()) {
						userInfoList = super.getUserInfoList(orgInfo.getOrgId(), nodeCode, params);
					} else if(toLevel == INodeCodeHandler.COMMUNITY) {
						//需要进行组织选择
						if(FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
								&& (FocusReportNode352Enum.NOT_THE_GRID_NODE_NAME.getTaskNodeName().equals(curnodeName)
										|| FocusReportNode352Enum.NOT_THE_STREET_NODE_NAME.getTaskNodeName().equals(curnodeName)
										|| FocusReportNode352Enum.FOREIGN_AFFAIRS_GROUP_NODE_NAME.getTaskNodeName().equals(curnodeName)
										|| FocusReportNode352Enum.EPC_GROUP_NODE_NAME.getTaskNodeName().equals(curnodeName)
										|| FocusReportNode352Enum.BIG_DATA_GROUP_NODE_NAME.getTaskNodeName().equals(curnodeName))) {
							isUserRadioStyle = false;
							isSelectOrg = true;
							resultMap.put("eventNodeCode", nodeCodeHandler);
						} else {
							//网格层级获取主送人
							if(ConstantValue.GRID_ORG_LEVEL.equals(orgChiefLevel)){
								userInfoList = super.getUserInfoList(orgInfo.getParentOrgId(), nodeCode, params);
							}else if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)){
								//村社区层级获取主送人
								userInfoList = super.getUserInfoList(orgInfo.getOrgId(), nodeCode, params);
							}
						}
					} else if(toLevel > 0 && toLevel < INodeCodeHandler.COMMUNITY) {
						//由于需求反复，暂留下面的代码，功能稳定后，需要删除 20210127
						if(false && FocusReportNode35201Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
								&& FocusReportNode35201Enum.STREET_HOSPITAL_NODE_NAME.getTaskNodeName().equals(curnodeName)
								&& FocusReportNode35201Enum.DISTRICT_HOSPITAL_NODE_NAME.getTaskNodeName().equals(nodeName)) {
							isUserRadioStyle = false;
							isSelectOrg = true;
							resultMap.put("eventNodeCode", nodeCodeHandler);
						} else {
							//发生地址为网格层级
							String nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.GRID + INodeCodeHandler.OPERATE_REPORT + (INodeCodeHandler.GRID - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
							//发生地址为村社区层级
							if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)){
								nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.COMMUNITY + INodeCodeHandler.OPERATE_REPORT + (INodeCodeHandler.COMMUNITY - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
							}else if(ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel)){
								//发生地址为乡镇街道层级（疫情防控部门）
								if(StringUtils.isNotBlank(orgInfoType)){
									if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgInfoType)){
										nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.COUNTY + INodeCodeHandler.OPERATE_FLOW + (INodeCodeHandler.STREET - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
									}else{
										nodeCodeTmp = INodeCodeHandler.ORG_DEPT + INodeCodeHandler.COUNTY + INodeCodeHandler.OPERATE_FLOW + (INodeCodeHandler.STREET - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
									}
								}else{
									throw new ZhsqEventException("疫情防控信息所属区域对应的组织信息不存在！");
								}
							}
							List<GdZTreeNode> treeNodeList = super.getTreeForEvent(userInfoTmp, null, nodeCodeTmp, null, null, params);

							if(treeNodeList != null && treeNodeList.size() > 0) {
								userInfoList = new ArrayList<UserInfo>();
								for(GdZTreeNode treeNode : treeNodeList) {
									userInfoList.addAll(super.getUserInfoList(Long.valueOf(treeNode.getId()), nodeCodeTmp, params));
								}
							} else {
								throw new ZhsqEventException("缺少办理组织信息！");
							}
						}
					} else if(toLevel < 0) {
						isUserRadioStyle = false;
						resultMap = super.getNodeInfoForEvent(userInfoTmp, curnodeName, nodeName, nodeCode, nodeId, params);
					}
				} else {
					throw new ZhsqEventException("发生地址没有匹配到网格、村社区或乡镇街道层级的组织信息！");
				}
				
				if(isUserRadioStyle) {
					if(userInfoList != null && userInfoList.size() > 0) {
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
					} else {
						throw new ZhsqEventException("缺少办理人员信息！");
					}
				}
			}
			
			resultMap.put("isSelectUser", false);
			resultMap.put("isSelectOrg", isSelectOrg);
			//主送人员只能单选
			resultMap.put("isUserRadioStyle", isUserRadioStyle);
			
			if(isUserRadioStyle && CommonFunctions.isNotBlank(resultMap, "userIds")) {
				String userIds = resultMap.get("userIds").toString();
				//只有当主送为一个人时，默认选中该人员
				resultMap.put("isUserDefaulCheck", userIds.split(",").length == 1);
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
			
			if(FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				if(FocusReportNode352Enum.FIND_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
					if(StringUtils.isNotBlank(collectSource)) {
						wfEndNodeName += "-" + collectSource;
					}
				}
			}
			
			cfgParams.put("workflowName", workflowName);
			cfgParams.put("wfEndNodeName", wfEndNodeName);
			cfgParams.put("wfStartNodeName", curnodeName);
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
			
			boolean isShowText = false, isShowDict = false, isShowAddress = false, isShowRegion = false;
			String textLabelName = "", dictLabelName = "", addressLabelName = "", regionLabelName = "";
			Map<String, Object> dynamicContentMap = new HashMap<String, Object>();
			Integer adviceNoteIndex = null;
			
			if(FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				if(FocusReportNode352Enum.VERIFY_TASK_GRID_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
					if(FocusReportNode352Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)) {
						String kpcType = null;
						
						adviceNoteIndex = 0;
						
						if(CommonFunctions.isNotBlank(reportFocusMap, "kpcType")) {
							kpcType = reportFocusMap.get("kpcType").toString();
						}
						
						if(EPCKpcTypeEnum.OUTSIDE_PROVINCE.getKpcType().equals(kpcType)) {
							adviceNoteIndex = 1;
						}
					}
				} else if(FocusReportNode352Enum.VERIFY_TASK_OTHER_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
					String dynamicDictJson = "[{'name':'信息属实', 'value':'0'},{'name':'信息不属实', 'value':'1'},{'name':'信息无法核实', 'value':'2'}]";
					
					isShowText = true;
					isShowDict = true;
					textLabelName = "实际地址";
					dictLabelName = "属实情况";
					
					dynamicContentMap.put("textLabelName_2", "困难所在");
					dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
				} else if(FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
					if(FocusReportNode352Enum.NOT_THE_GRID_NODE_NAME.getTaskNodeName().equals(nodeName)) {
						String dynamicDictJson = "[{'name':'本乡镇', 'value':'0'},{'name':'非本乡镇', 'value':'1'}]";
						
						isShowText = true;
						isShowDict = true;
						textLabelName = "实际地址";
						dictLabelName = "人员所属";
						
						dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
					} else if(FocusReportNode352Enum.NOT_VERIFIED_NODE_NAME.getTaskNodeName().equals(nodeName)) {
						isShowText = true;
						textLabelName = "困难所在";
					}
				} else if(FocusReportNode352Enum.NOT_THE_GRID_NODE_NAME.getTaskNodeName().equals(curnodeName)
						|| FocusReportNode352Enum.NOT_THE_STREET_NODE_NAME.getTaskNodeName().equals(curnodeName)
						|| FocusReportNode352Enum.FOREIGN_AFFAIRS_GROUP_NODE_NAME.getTaskNodeName().equals(curnodeName)
						|| FocusReportNode352Enum.EPC_GROUP_NODE_NAME.getTaskNodeName().equals(curnodeName)
						|| FocusReportNode352Enum.BIG_DATA_GROUP_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
					if(FocusReportNode352Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)) {
						List<OrgEntityInfoBO> orgEntityInfoList = null;
						String startDivisionCode = "-1";
						
						isShowRegion = false;
						isShowAddress = false;
						addressLabelName = "发生地址";
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
					}else if(FocusReportNode352Enum.NOT_VERIFIED_NODE_NAME.getTaskNodeName().equals(nodeName)) {

					} else {
						isShowText = true;
						textLabelName = "实际地址";
					}
				} else if(FocusReportNode352Enum.GRID_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
					String dynamicDictJson = "[{'name':'措施已落实', 'value':'0'},{'name':'未发现异常', 'value':'1'},{'name':'擅自外出/接待访客', 'value':'2'},{'name':'隔离期满', 'value':'3'}]";
					
					isShowDict = true;
					dictLabelName = "人员情况";
					
					dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
				}
			} else if(FocusReportNode35201Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				if(FocusReportNode35201Enum.STREET_HOSPITAL_NODE_NAME.getTaskNodeName().equals(curnodeName)
						&& FocusReportNode35201Enum.DISTRICT_HOSPITAL_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					isShowText = true;
					textLabelName = "机构名称";
				} else if(FocusReportNode35201Enum.DISTRICT_HOSPITAL_NODE_NAME.getTaskNodeName().equals(curnodeName)
						&& FocusReportNode35201Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)) {
					String dynamicDictJson = "[{'name':'自行返回', 'value':'0'},{'name':'住院治疗', 'value':'1'}]";
					
					isShowDict = true;
					isShowText = true;
					textLabelName = "科室名称";
					dictLabelName = "病人情况";
					
					dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
				}
			}
			
			if(StringUtils.isNotBlank(adviceNote)) {
				String splitStr = "#OR#";//当一个消息模板中包含有多种执行情况时，使用#OR#进行分割
				
				if(adviceNote.contains(splitStr)) {
					String[] adviceNoteArray = adviceNote.split(splitStr),
							adviceNoteInitialArray = adviceNoteInitial.split(splitStr);
					
					if(adviceNoteIndex != null) {
						resultMap.put("adviceNote", adviceNoteArray[adviceNoteIndex]);
					} else {
						for(int index = 0, len = adviceNoteArray.length; index < len; index++) {
							dynamicContentMap.put("adviceNote_" + index, adviceNoteArray[index]);
							dynamicContentMap.put("adviceNoteInitial_" + index, adviceNoteInitialArray[index]);
						}
					}
				} else {
					resultMap.put("adviceNote", adviceNote);
					resultMap.put("adviceNoteInitial", adviceNoteInitial);
				}
			}

			//当下一环节为归档时，增加处理后图片必填约束
			if(FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&& FocusReportNode352Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
				//变更需求单：5434653 要求所有环节处理后图片非必填 2021-04-26
				resultMap.put("isUploadHandledPic", false);
				resultMap.put("picTypeName", "处理后");
			}
			
			dynamicContentMap.put("isShowText", isShowText);
			dynamicContentMap.put("isShowDict", isShowDict);
			dynamicContentMap.put("isShowAddress", isShowAddress);
			dynamicContentMap.put("isShowRegion", isShowRegion);
			dynamicContentMap.put("textLabelName", textLabelName);
			dynamicContentMap.put("dictLabelName", dictLabelName);
			dynamicContentMap.put("addressLabelName", addressLabelName);
			dynamicContentMap.put("regionLabelName", regionLabelName);
			resultMap.put("dynamicContentMap", dynamicContentMap);
		} else {
			resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		}
		
		return resultMap;
	}
	
	@Override
	public List<GdZTreeNode> getTreeForEvent(
			UserInfo userInfo, Long orgId,
			String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception {
		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		Long rootOrgId = orgId;
		
		//由于疫情防控外事组、大数据组等职能挂接在南安市疫情防控指挥部职能部门下，因此在进行在进行“在本市，不在本街道”时，强制将外事组、大数据组更换为南安市疫情防控指挥部
		if(rootOrgId == null 
				&& nodeCodeHandler.isFromDept() && nodeCodeHandler.isToUnit() 
				&& nodeCodeHandler.getFromLevel() == Integer.valueOf(ConstantValue.DISTRICT_ORG_LEVEL) 
				&& nodeCodeHandler.getToLevel() == Integer.valueOf(ConstantValue.COMMUNITY_ORG_LEVEL)) {
			Long userOrgId = userInfo.getOrgId();
			
			if(userOrgId != null && userOrgId > 0) {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userOrgId);
				
				if(orgInfo != null) {
					String orgChiefLevel = orgInfo.getChiefLevel(),
						   orgType = orgInfo.getOrgType(),
						   ORG_TYPE_DEPT = "0";
					
					if(ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel)
							&& ORG_TYPE_DEPT.equals(orgType)) {
						String formTypeIdStr = null, curNodeName = null, nextNodeName = null;
						
						if(CommonFunctions.isNotBlank(params, "formTypeId")) {
							formTypeIdStr = params.get("formTypeId").toString();
						}
						
						if(CommonFunctions.isNotBlank(params, "curNodeName")) {
							curNodeName = params.get("curNodeName").toString();
						}
						
						if(CommonFunctions.isNotBlank(params, "nextNodeName")) {
							nextNodeName = params.get("nextNodeName").toString();
						}
						
						if(FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
								&& FocusReportNode352Enum.NOT_THE_STREET_NODE_NAME.getTaskNodeName().equals(curNodeName)
								&& FocusReportNode352Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
							rootOrgId = orgInfo.getParentOrgId();
						}
					}
				}
			}
		}
		
		return super.getTreeForEvent(userInfo, rootOrgId, nodeCode, level, professionCode, params);
	}
	
	/**
	 * 获取消息模板触发条件
	 * @param proInstance
	 * @param params
	 * @return
	 */
	protected StringBuffer capAdviceNoteCondition(ProInstance proInstance, Map<String, Object> params) {
		StringBuffer trigCondition = new StringBuffer("");
		String formTypeIdStr = null, curTaskName = null,nodeName = null;
		
		if(proInstance != null) {
			curTaskName = proInstance.getCurNode();
			formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
		}
		if(CommonFunctions.isNotBlank(params, "nodeName")){
			nodeName = String.valueOf(params.get("nodeName"));
		}
		
		trigCondition.append(ConstantValue.EPIDEMIC_PRE_CONTROL_NOTE).append("-").append(proInstance.getProName()).append("-").append(curTaskName);
		
		if(FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& FocusReportNode352Enum.FIND_NODE_NAME.getTaskNodeName().equals(curTaskName)) {

			//无法明确到村社区的情形 需要在指定节点拼接下一环节名称来获取消息模板
			if(FocusReportNode352Enum.FOREIGN_AFFAIRS_GROUP_NODE_NAME.getTaskNodeName().equals(nodeName)
					|| FocusReportNode352Enum.EPC_GROUP_NODE_NAME.getTaskNodeName().equals(nodeName)
					|| FocusReportNode352Enum.BIG_DATA_GROUP_NODE_NAME.getTaskNodeName().equals(nodeName)){
				trigCondition.append("-").append(nodeName);
			}else if(CommonFunctions.isNotBlank(params, "collectSource")) {
				trigCondition.append("-").append(params.get("collectSource"));
			}

		} else if(StringUtils.isNotBlank(nodeName)) {
			trigCondition.append("-").append(nodeName);
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
			if(adviceNote.contains("@kpcName@")) {
				String kpcName = "";
				
				if(CommonFunctions.isNotBlank(params, "kpcName")) {
					kpcName = "【" + params.get("kpcName").toString() + "】";
				}
				
				adviceNote = adviceNote.replace("@kpcName@", kpcName);
			}
			if(adviceNote.contains("@kpcIdCard@") && CommonFunctions.isNotBlank(params, "kpcIdCard")) {
				adviceNote = adviceNote.replace("@kpcIdCard@", params.get("kpcIdCard").toString());
			}
			if(adviceNote.contains("@kpcTypeName@") && CommonFunctions.isNotBlank(params, "kpcTypeName")) {
				adviceNote = adviceNote.replace("@kpcTypeName@", params.get("kpcTypeName").toString());
			}
			if(adviceNote.contains("@reporterOrgName@")) {
				String reporterOrgName = "";
				
				if(CommonFunctions.isNotBlank(params, "reporterOrgName")) {
					reporterOrgName = params.get("reporterOrgName").toString();
				} else if(CommonFunctions.isNotBlank(params, "reporterOrgId")) {
					Long reporterOrgId = null;
					OrgSocialInfoBO orgInfo = null;
					
					try {
						reporterOrgId = Long.valueOf(params.get("reporterOrgId").toString());
					} catch(NumberFormatException e) {}
					
					if(reporterOrgId != null && reporterOrgId > 0) {
						orgInfo = orgSocialInfoService.findByOrgId(reporterOrgId);
						
						if(orgInfo != null) {
							reporterOrgName = orgInfo.getOrgName();
						}
					}
				}
				
				adviceNote = adviceNote.replace("@reporterOrgName@", reporterOrgName);
			}
		}
		
		return adviceNote;
	}
}
