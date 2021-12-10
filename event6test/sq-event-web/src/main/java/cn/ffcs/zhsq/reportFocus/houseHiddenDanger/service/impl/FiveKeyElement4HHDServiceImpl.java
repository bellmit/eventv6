package cn.ffcs.zhsq.reportFocus.houseHiddenDanger.service.impl;

import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.FiveKeyElement4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 房屋安全隐患(House Hidden Danger)人员选择
 * @ClassName:   FiveKeyElement4HHDServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月17日 下午5:55:58
 */
@Service(value = "fiveKeyElement4HHDService")
public class FiveKeyElement4HHDServiceImpl extends FiveKeyElement4TwoVioPreServiceImpl {
	@Autowired
	private IReportIntegrationService reportIntegrationService;

	@Autowired
	private IBaseWorkflowService baseWorkflowService;

	@Autowired
	private OrgEntityInfoOutService orgEntityInfoService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		String reportType = ReportTypeEnum.houseHiddenDanger.getReportTypeIndex();
		Map<String, Object> resultMap = null,reportMap = null,reportParams = new HashMap<String, Object>();
		params = params == null ? new HashMap<String, Object>() : params;
		INodeCodeHandler nodeCodeHandler = null;
		boolean isUserRadioStyle = true, isSelectOrg = false;
		int toLevel = -1;
		Long reportId = null, instanceId = null;
		
		if(CommonFunctions.isBlank(params, "reportType")) {
			params.put("reportType", reportType);
		}

		nodeCodeHandler = this.createNodeCodeHandle(nodeCode);

		if(null != nodeCodeHandler){
			toLevel = nodeCodeHandler.getToLevel();
		}
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

		reportParams.putAll(params);
		reportParams.put("reportId", reportId);
		reportParams.put("reportType", reportType);
		reportMap = reportIntegrationService.findReportFocusByUUID(null, userInfo, reportParams);

		//分送选送人员获取开启开始节点配置
		params.put("enableStartNode",true);
		params.put("wfStartNodeName",curnodeName);

		resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);

		boolean isShowText = false, isShowDict = false, isShowRegion = false;
		String textLabelName = "", dictLabelName = "", regionLabelName = "";
		Map<String, Object> dynamicContentMap = new HashMap<String, Object>();
		String formTypeIdStr = null;
		String dataSource = null;
		
		if(CommonFunctions.isNotBlank(params, "formTypeId")) {
			formTypeIdStr = params.get("formTypeId").toString();
		}

		if(CommonFunctions.isNotBlank(params, "dataSource")) {
			dataSource = params.get("dataSource").toString();
		} else if(CommonFunctions.isNotBlank(reportMap, "dataSource")) {
			dataSource = reportMap.get("dataSource").toString();
		}
		
		if(FocusReportNode35301Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode35301Enum.FIND_HHD_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				
				if(HHDDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)) {
					String dynamicDictJson = "[{'name':'疑似存在安全隐患', 'value':'0'},{'name':'疑似存在人员回流', 'value':'1'}]";
					
					isShowDict = true;
					dictLabelName = "房屋情况";
					
					dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
				} else if(HHDDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(dataSource)) {
					isShowText = true;
					textLabelName = "交办部门";
				} else if(HHDDataSourceEnum.FIRE_APPROVAL.getDataSource().equals(dataSource)) {
					String dynamicDictJson = "[{'name':'改变原有使用功能', 'value':'0'},{'name':'改变结构受力体系', 'value':'1'}]";
					
					isShowDict = true;
					dictLabelName = "安全隐患";
					
					dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
				}
			} else if(FocusReportNode35301Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)
					&& FocusReportNode35301Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)) {
				String dynamicDictJson = "[{'name':'不属实', 'value':'0'},{'name':'未发现安全隐患', 'value':'1'}]";

				isUserRadioStyle=false;
				dictLabelName = "隐患情况";
				isShowDict = true;

				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
			} else if(FocusReportNode35301Enum.HHD_COGNIZANCE_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				String dynamicDictJson = "[{'name':'" + HHDRiskTypeEnum.COMMON.getRiskTypeName() + "', 'value':'" + HHDRiskTypeEnum.COMMON.getRiskType() + "'},{'name':'" + HHDRiskTypeEnum.GREAT.getRiskTypeName() + "', 'value':'" + HHDRiskTypeEnum.GREAT.getRiskType() + "'}]";
				
				isShowDict = true;
				dictLabelName = "隐患类型";
				
				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
			} else if(FocusReportNode35301Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				isUserRadioStyle=false;
				isShowDict = true;
				dictLabelName = "处置类型";
				dynamicContentMap.put("dictPcode", HHDDictEnum.MANAGE_TYPE.getDictCode());
			}else if(FocusReportNode35301Enum.MASSES_REPORT_NODE_NAME.getTaskNodeName().equals(curnodeName)
					|| FocusReportNode35301Enum.ASSIGNED_SUPERIOR_NODE_NAME.getTaskNodeName().equals(curnodeName)
					|| FocusReportNode35301Enum.FIREPROTECTION_NODE_NAME.getTaskNodeName().equals(curnodeName)
					|| FocusReportNode35301Enum.BUSINESS_LICENSE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
				//无法明确到村社区、网格的情况 在乡镇街道节点打开组织选择；重新选择所属地域，限制到村社区、网格级
				List<OrgEntityInfoBO> orgEntityInfoList = null;
				String startDivisionCode = "-1";

				if(toLevel == INodeCodeHandler.COMMUNITY) {
					isUserRadioStyle = false;
					isSelectOrg = true;
					resultMap.put("eventNodeCode", nodeCodeHandler);
				}

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
				dynamicContentMap.put("isShowRegion", isShowRegion);
				dynamicContentMap.put("regionLabelName", regionLabelName);
				dynamicContentMap.put("startDivisionCode", startDivisionCode);

				if(HHDDataSourceEnum.FIRE_APPROVAL.getDataSource().equals(dataSource)) {
					String dynamicDictJson = "[{'name':'改变原有使用功能', 'value':'0'},{'name':'改变结构受力体系', 'value':'1'}]";

					isShowDict = true;
					dictLabelName = "安全隐患";

					dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
				}
			}
		} else if(FocusReportNode3530101Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode3530101Enum.DECOMPOSE_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				isShowText = true;
				textLabelName = "认定结论";
			} else if(FocusReportNode3530101Enum.GRID_ROUTINE_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				isShowDict = true;
				dictLabelName = "隐患状态";
				dynamicContentMap.put("dictPcode", HHDDictEnum.HIDDEN_DANGER_STATUS.getDictCode());
			} else if(FocusReportNode3530101Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
				String dynamicDictJson = "[{'name':'正在进行', 'value':'0'},{'name':'已完成', 'value':'1'}]";
				//其中 正在进行对应字典 HHDDictEnum.HANDLE_TYPE.getDictCode()
				dynamicContentMap.put("handleTypeDict",HHDDictEnum.HANDLE_TYPE.getDictCode());
				//其中 已完成对应字典 HHDDictEnum.MANAGE_TYPE.getDictCode()
				dynamicContentMap.put("manageTypeDict",HHDDictEnum.MANAGE_TYPE.getDictCode());

				isShowDict = true;
				dictLabelName = "处置状态";
				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
			}
		} else if(FocusReportNode3530102Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode3530102Enum.DECOMPOSE_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				isShowText = true;
				textLabelName = "认定结论";
			} else if(FocusReportNode3530102Enum.GRID_ROUTINE_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				isShowDict = true;
				dictLabelName = "隐患状态";
				dynamicContentMap.put("dictPcode", HHDDictEnum.HIDDEN_DANGER_STATUS.getDictCode());
			}else if(FocusReportNode3530102Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
				String dynamicDictJson = "[{'name':'正在进行', 'value':'0'},{'name':'已完成', 'value':'1'}]";
				//其中 正在进行对应字典 HHDDictEnum.HANDLE_TYPE.getDictCode()
				dynamicContentMap.put("handleTypeDict",HHDDictEnum.HANDLE_TYPE.getDictCode());
				//其中 已完成对应字典 HHDDictEnum.MANAGE_TYPE.getDictCode()
				dynamicContentMap.put("manageTypeDict",HHDDictEnum.MANAGE_TYPE.getDictCode());

				isShowDict = true;
				dictLabelName = "处置状态";
				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
			}
		} else if(FocusReportNode35302Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			//发现突发房屋安全隐患
			//当前环节为 task1
			if(FocusReportNode35302Enum.FIND_HHD_NODE_NAME.getTaskNodeName().equals(curnodeName)){
				if(FocusReportNode35302Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)
						|| FocusReportNode35302Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
					//下一环节为：乡镇(街道)办事处(城市建设局)
					String dynamicDictJson = "[{'name':'大排查大整治', 'value':'0'},{'name':'新认定', 'value':'1'}]";
					isShowDict = true;
					dictLabelName = "隐患来源";
					dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
				}
			}else if(FocusReportNode35302Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)
					|| FocusReportNode35302Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName) ){
				//无法明确到村社区、网格的情况（地域编码层级为乡镇街道层级） 在乡镇街道节点打开组织选择；重新选择所属地域，限制到村社区、网格级
				List<OrgEntityInfoBO> orgEntityInfoList = null;OrgEntityInfoBO regionInfo = null;
				String regionCode = null,regionLevel = null,startDivisionCode = "-1";

				if(CommonFunctions.isNotBlank(reportMap,"regionCode")){
					regionCode = String.valueOf(reportMap.get("regionCode"));
				}
				if(StringUtils.isNotBlank(regionCode)){
					regionInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(regionCode);
				}

				if(null != regionInfo){
					regionLevel = regionInfo.getChiefLevel();
				}

				//当前环节为:第一网格长(驻村领导)处置 判断该信息地域层级 层级大于网格时 需要重新选择所属地域
				if(FocusReportNode35302Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
					isShowRegion = regionLevel.compareTo(ConstantValue.GRID_ORG_LEVEL) < 0;

					//下一环节为归档时隐藏主送人员获取标签
					if(FocusReportNode35302Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
						isUserRadioStyle=false;

						isShowDict = true;
						dictLabelName = "处置类型";
						dynamicContentMap.put("dictPcode", HHDDictEnum.MANAGE_TYPE_SUB.getDictCode());
					}else if(FocusReportNode35302Enum.GRID_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
						//isUserRadioStyle userNames  userIds
						//isUserRadioStyle = false;
						//限制地域层级职能选择到网格 不然子流程二级网格选不到人
						resultMap.put("isChooseToGrid",true);
						isUserRadioStyle = false;
						isSelectOrg = true;
						resultMap.put("eventNodeCode", nodeCodeHandler);
					}

				}else if(FocusReportNode35302Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
					isShowRegion = true;
					//增加隐患来源选择项
					//下一环节为：乡镇(街道)任务派发
					String dynamicDictJson = "[{'name':'大排查大整治', 'value':'0'},{'name':'新认定', 'value':'1'}]";
					isShowDict = true;
					dictLabelName = "隐患来源";
					dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
				}

				if(toLevel == INodeCodeHandler.COMMUNITY) {
					isUserRadioStyle = false;
					isSelectOrg = true;
					resultMap.put("eventNodeCode", nodeCodeHandler);
				}

				if(isShowRegion){
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
					dynamicContentMap.put("isShowRegion", isShowRegion);
					dynamicContentMap.put("regionLabelName", regionLabelName);
					dynamicContentMap.put("startDivisionCode", startDivisionCode);
				}
			}
		} else if(FocusReportNode3530201Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode3530201Enum.GRID_ROUTINE_NODE_NAME.getTaskNodeName().equals(curnodeName)) {

				//上传实地相片
				resultMap.put("isUploadSitePic", true);

				isShowDict = true;
				dictLabelName = "隐患状态";
				dynamicContentMap.put("dictPcode", HHDDictEnum.HIDDEN_DANGER_STATUS.getDictCode());
			}else if(FocusReportNode3530201Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)
					&& FocusReportNode3530201Enum.GRID_ROUTINE_NODE_NAME.getTaskNodeName().equals(nodeName)){
				//处置状态 (下一环节为：二级网格隐患跟踪)
				String dynamicDictJson = "[{'name':'正在进行', 'value':'0'},{'name':'已完成', 'value':'1'}]";
				//其中 正在进行对应字典 HHDDictEnum.HANDLE_TYPE_SUB.getDictCode()
				dynamicContentMap.put("handleTypeDict",HHDDictEnum.HANDLE_TYPE_SUB.getDictCode());
				//其中 已完成对应字典 HHDDictEnum.MANAGE_TYPE_SUB.getDictCode()
				dynamicContentMap.put("manageTypeDict",HHDDictEnum.MANAGE_TYPE_SUB.getDictCode());

				isShowDict = true;
				dictLabelName = "处置状态";
				dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
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

		//当下一环节为归档时，增加处理后图片必填约束
		if(FocusReportNode35301Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)
				|| FocusReportNode35302Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName) ){
			//2021-04-29 全部入格改为非必填
			resultMap.put("isUploadHandledPic", false);
			resultMap.put("picTypeName", "处理后");
		}

		resultMap.put("isSelectOrg", isSelectOrg);
		resultMap.put("isUserRadioStyle", isUserRadioStyle);
		
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
		String formTypeIdStr = null, curTaskName = null,regionCode = null,regionChiefLevel = null;
		
		if(proInstance != null) {
			curTaskName = proInstance.getCurNode();
			formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
		}

		if(CommonFunctions.isNotBlank(params,"regionCode")){
			regionCode = String.valueOf(params.get("regionCode"));
		}
		if(StringUtils.isNotBlank(regionCode)){
			OrgEntityInfoBO regionInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(regionCode);
			if(null != regionInfo){
				regionChiefLevel = regionInfo.getChiefLevel();
			}
		}


		trigCondition.append(ConstantValue.HOUSE_HIDDEN_DANGER_NOTE).append("-").append(proInstance.getProName()).append("-").append(curTaskName);
		
		if(FocusReportNode35301Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& FocusReportNode35301Enum.FIND_HHD_NODE_NAME.getTaskNodeName().equals(curTaskName)) {
			//无法明确到村社区的情形 需要在指定节点拼接下一环节名称来获取消息模板
			if(StringUtils.isNotBlank(regionChiefLevel) && ConstantValue.STREET_ORG_LEVEL.equals(regionChiefLevel)){
				//群众举报 02
				if(HHDDataSourceEnum.MASSES_REPORT.getDataSource().equals(params.get("dataSource"))){
					if(CommonFunctions.isNotBlank(params, "nodeName")) {
						trigCondition.append("-").append(params.get("nodeName"));
					}
				}else if(HHDDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(params.get("dataSource"))){
					//上级交办 03 task7
					if(CommonFunctions.isNotBlank(params, "nodeName")) {
						trigCondition.append("-").append(params.get("nodeName"));
					}
				}else if(HHDDataSourceEnum.FIRE_APPROVAL.getDataSource().equals(params.get("dataSource"))){
					//消防手续审批 05 task15
					if(CommonFunctions.isNotBlank(params, "nodeName")) {
						trigCondition.append("-").append(params.get("nodeName"));
					}
				}else if(HHDDataSourceEnum.BUSSINESS_LICENSE_PROCESS.getDataSource().equals(params.get("dataSource"))){
					//营业执照办理 07 task15
					if(CommonFunctions.isNotBlank(params, "nodeName")) {
						trigCondition.append("-").append(params.get("nodeName"));
					}
				}
			}

			if(CommonFunctions.isNotBlank(params, "dataSource")) {
				trigCondition.append("-").append(params.get("dataSource"));
			}
		}else if(CommonFunctions.isNotBlank(params, "nodeName")) {
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
			if(adviceNote.contains("@riskDescribeName@") && CommonFunctions.isNotBlank(params, "riskDescribeName")) {
				adviceNote = adviceNote.replace("@riskDescribeName@", params.get("riskDescribeName").toString());
			}
			if(adviceNote.contains("@hhdTypeName@") && CommonFunctions.isNotBlank(params, "hhdTypeName")) {
				adviceNote = adviceNote.replace("@hhdTypeName@", params.get("hhdTypeName").toString());
			}
			if(adviceNote.contains("@buildingName@") && CommonFunctions.isNotBlank(params, "buildingName")) {
				adviceNote = adviceNote.replace("@buildingName@", params.get("buildingName").toString());
			}
			if(adviceNote.contains("@enterpriseName@")) {
				if(CommonFunctions.isNotBlank(params, "enterpriseName")){
					adviceNote = adviceNote.replace("@enterpriseName@", params.get("enterpriseName").toString());
				}else{
					adviceNote = adviceNote.replace("@enterpriseName@", "");
				}
			}
			//当下一环节为突发流程的 task99时，若网格层级已经确定到网格级，用网格名称替换变量
			if(adviceNote.contains("@dynamicContent@") && CommonFunctions.isNotBlank(params, "regionCode")
					&& CommonFunctions.isNotBlank(params,"nodeName") && FocusReportNode35302Enum.GRID_HANDLE_NODE_NAME.getTaskNodeName().equals(params.get("nodeName"))) {
				OrgEntityInfoBO regionInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(params.get("regionCode").toString());
				if(null != regionInfo && regionInfo.getChiefLevel().equals(ConstantValue.GRID_ORG_LEVEL)){
					adviceNote = adviceNote.replace("@dynamicContent@", regionInfo.getOrgName());
				}
			}
		}

		return adviceNote;
	}
}
