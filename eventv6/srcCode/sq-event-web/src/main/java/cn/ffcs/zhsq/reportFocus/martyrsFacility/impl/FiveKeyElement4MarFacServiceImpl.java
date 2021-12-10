package cn.ffcs.zhsq.reportFocus.martyrsFacility.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.common.utils.StringUtils;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.PositionInfoBO;
import cn.ffcs.uam.bo.ProfessionBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.PositionInfoOutService;
import cn.ffcs.uam.service.ProfessionOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.businessProblem.ReportBusProMapper;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.martyrsFacility.ReportMarFacMapper;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.petitionPerson.ReportPetPerMapper;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.reportFocus.businessProblem.service.impl.FocusReportNode359Enum;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.IReportMsgCCCfgService;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.service.impl.ReportMsgCCTypeEnum;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.FiveKeyElement4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.FocusReportNode350Enum;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.data.DateUtils;

/**
 * @Description: 营商问题人员选择
 * @ClassName:   FiveKeyElement4BusProServiceImpl   
 * @author:      郭昱丹(guoyd)
 * @date:        2021年5月7日 上午10:16:15
 */
@Service(value = "fiveKeyElement4MarFacService")
public class FiveKeyElement4MarFacServiceImpl extends FiveKeyElement4TwoVioPreServiceImpl {

	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private IReportFocusService reportFocusService;
	
	@Resource(name="eventDisposalWorkflowForEventService")
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private PositionInfoOutService positionInfoOutService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private ProfessionOutService professionService;
	
	@Autowired
	private IReportIntegrationService reportIntegrationService;
	
	@Autowired
	private IReportMsgCCCfgService reportMsgCCCfgService;
	
	@Autowired
	private ReportMarFacMapper reportMarFacMapper;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String reportType = ReportTypeEnum.martyrsFacility.getReportTypeIndex(), formTypeIdStr = null;
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
		wfEndNodeName.append(nodeName);
		if(FocusReportNode363Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
				&&(FocusReportNode363Enum.FIND_NODE_NAME.getTaskNodeName().equals(curnodeName)
						||FocusReportNode363Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(curnodeName))) {
			
			String dataSource = null;
			if(CommonFunctions.isNotBlank(params, "dataSource")) {
				dataSource = params.get("dataSource").toString();
			} else if(reportId != null && reportId > 0) {
				Map<String, Object> reportParams = new HashMap<String, Object>(),reportFocusMap = null;
				reportParams.putAll(params);
				reportParams.put("reportId", reportId);
				reportFocusMap = reportFocusService.findReportFocusByUUID(null, userInfo, reportParams);
				if(CommonFunctions.isNotBlank(reportFocusMap, "dataSource")) {
					dataSource = reportFocusMap.get("dataSource").toString();
				}
			}
			if(FocusReportNode363Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
				if(MarFacDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource)) {
					wfEndNodeName.append("-").append(dataSource);
				}
			}else{
				wfEndNodeName.append("-").append(dataSource);
			}
		}
		params.put("wfEndNodeName", wfEndNodeName.toString());
		
		if(FocusReportNode363Enum.FIND_NODE_NAME.getTaskNodeName().equals(curnodeName)
				&&FocusReportNode363Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
				&&FocusReportNode363Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)) {
			//不自动取人
			params.put("isAnalysisOperateUser", false);
			resultMap = getNodeInfoForEventV2(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
			
//			//第一网格长：	dywgz1
			PositionInfoBO positionInfoBO = positionInfoOutService.queryPositionByCode("dywgz1");
			if(positionInfoBO!=null) {
				OrgSocialInfoBO userOrgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
				List<UserBO> userList = userManageService.getUserListByUserExampleParamsOut(null, positionInfoBO.getPositionId(), userOrgInfo.getParentOrgId());
				StringBuffer nextUserId = new StringBuffer(""),
					     nextuserName = new StringBuffer(""),
					     nextOrgId = new StringBuffer(""),
					     nextOrgName = new StringBuffer("");
				for(UserBO userBO:userList) {
					nextUserId.append(",").append(userBO.getUserId());
					nextuserName.append(",").append(userBO.getPartyName());
					nextOrgId.append(",").append(userBO.getSocialOrgId());
					nextOrgName.append(",").append(userBO.getOrgName());
				}
				
				if(nextUserId.length() > 0) {
					resultMap.put("userIds", nextUserId.substring(1));
					resultMap.put("userNames", nextuserName.substring(1));
					resultMap.put("orgIds", nextOrgId.substring(1));
					resultMap.put("orgNames", nextOrgName.substring(1));
				}
			}
			resultMap.put("isSelectOrg", false);
			resultMap.put("isUserRadioStyle", true);
			resultMap.put("isSelectUser", false);
		}else if(FocusReportNode363Enum.FEEDBACK_NODE_NAME.getTaskNodeName().equals(curnodeName)
				&&FocusReportNode363Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
				&&FocusReportNode363Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
			//不自动取人
			params.put("isAnalysisOperateUser", false);
			resultMap = getNodeInfoForEventV2(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
			
//			//第一网格长：	dywgz1
			PositionInfoBO positionInfoBO = positionInfoOutService.queryPositionByCode("dywgz1");
			if(positionInfoBO!=null) {
				List<UserBO> userList = userManageService.getUserListByUserExampleParamsOut(null, positionInfoBO.getPositionId(), userInfo.getOrgId());
				StringBuffer nextUserId = new StringBuffer(""),
					     nextuserName = new StringBuffer(""),
					     nextOrgId = new StringBuffer(""),
					     nextOrgName = new StringBuffer("");
				for(UserBO userBO:userList) {
					nextUserId.append(",").append(userBO.getUserId());
					nextuserName.append(",").append(userBO.getPartyName());
					nextOrgId.append(",").append(userBO.getSocialOrgId());
					nextOrgName.append(",").append(userBO.getOrgName());
				}
				
				if(nextUserId.length() > 0) {
					resultMap.put("userIds", nextUserId.substring(1));
					resultMap.put("userNames", nextuserName.substring(1));
					resultMap.put("orgIds", nextOrgId.substring(1));
					resultMap.put("orgNames", nextOrgName.substring(1));
				}
			}
			resultMap.put("isSelectOrg", false);
			resultMap.put("isUserRadioStyle", true);
			resultMap.put("isSelectUser", false);
		}else if((FocusReportNode363Enum.TODO_NODE_NAME.getTaskNodeName().equals(curnodeName)
				||FocusReportNode363Enum.TODO_NODE_NAME2.getTaskNodeName().equals(curnodeName))
				&&FocusReportNode363Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
				&&FocusReportNode363Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(nodeName)){
			//不自动取人
			params.put("isAnalysisOperateUser", false);
			resultMap = getNodeInfoForEventV2(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
			
//			//业务科室负责人：	szbmfzr
			PositionInfoBO positionInfoBO = positionInfoOutService.queryPositionByCode("szbmfzr");
			if(positionInfoBO!=null) {
				List<UserBO> userList = userManageService.queryUserListByPositionAndOrg("", positionInfoBO.getPositionId(),"", null, "350583295001");
				StringBuffer nextUserId = new StringBuffer(""),
					     nextuserName = new StringBuffer(""),
					     nextOrgId = new StringBuffer(""),
					     nextOrgName = new StringBuffer("");
				for(UserBO userBO:userList) {
					nextUserId.append(",").append(userBO.getUserId());
					nextuserName.append(",").append(userBO.getPartyName());
					nextOrgId.append(",").append(userBO.getSocialOrgId());
					nextOrgName.append(",").append(userBO.getOrgName());
				}
				
				if(nextUserId.length() > 0) {
					resultMap.put("userIds", nextUserId.substring(1));
					resultMap.put("userNames", nextuserName.substring(1));
					resultMap.put("orgIds", nextOrgId.substring(1));
					resultMap.put("orgNames", nextOrgName.substring(1));
				}
			}
			resultMap.put("isSelectOrg", false);
			resultMap.put("isUserRadioStyle", true);
			resultMap.put("isSelectUser", false);
		}else if(FocusReportNode363Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(curnodeName)
				&&FocusReportNode363Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)){
			//不自动取人
			params.put("isAnalysisOperateUser", false);
			resultMap = getNodeInfoForEventV2(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
			
			Map<String, Object> temp_params = new HashMap<String, Object>();
			temp_params.put("instanceId", instanceId);
			List<Map<String, Object>> myTaskList = reportMarFacMapper.findToDoOrgId(temp_params);
			if(!myTaskList.isEmpty()) {
				Map<String, Object> beforeMap = myTaskList.get(0);
				resultMap.put("userIds", StringUtils.toString(beforeMap.get("TRANSACTOR_ID")));
				resultMap.put("userNames", StringUtils.toString(beforeMap.get("TRANSACTOR_NAME")));
				resultMap.put("orgIds", StringUtils.toString(beforeMap.get("ORG_ID")));
				resultMap.put("orgNames", StringUtils.toString(beforeMap.get("ORG_NAME")));
			}
			resultMap.put("isSelectOrg", false);
			resultMap.put("isUserRadioStyle", true);
			resultMap.put("isSelectUser", false);
		}else {
			resultMap = getNodeInfoForEventV2(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		}
		
		if(CommonFunctions.isNotBlank(resultMap, "isUserRadioStyle")) {
			boolean isUserRadioStyle = Boolean.valueOf(resultMap.get("isUserRadioStyle").toString());
			
			if(isUserRadioStyle && CommonFunctions.isNotBlank(resultMap, "userIds")) {
				String userIds = resultMap.get("userIds").toString();
				//只有当主送为一个人时，默认选中该人员
				resultMap.put("isUserDefaulCheck", userIds.split(",").length == 1);
			}
		}
		
		if(FocusReportNode363Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			boolean isShowText = false, isShowDict = false, isShowNumInput = false;
			String textLabelName = "", dictLabelName = "", numInputLabelName = "";
			String dynamicDictValue = "",dynamicDictName = "";
			Map<String, Object> dynamicContentMap = new HashMap<String, Object>();
			
			if(CommonFunctions.isNotBlank(resultMap, "dynamicContentMap")) {
				dynamicContentMap.putAll((Map<String, String>) resultMap.get("dynamicContentMap"));
			}
			
			dynamicContentMap.put("isShowText", isShowText);
			dynamicContentMap.put("isShowDict", isShowDict);
			dynamicContentMap.put("isShowNumInput", isShowNumInput);
			dynamicContentMap.put("textLabelName", textLabelName);
			dynamicContentMap.put("dictLabelName", dictLabelName);
			dynamicContentMap.put("numInputLabelName", numInputLabelName);
			dynamicContentMap.put("dynamicDictValue", dynamicDictValue);
			dynamicContentMap.put("dynamicDictName", dynamicDictName);
			
			String startDivisionCode = "-1";
			
			//获取当前登录账号下所管理的所有网格
			List<OrgEntityInfoBO> orgEntityInfoList = userInfo.getInfoOrgList();

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
		
		trigCondition.append(ConstantValue.MARTYRS_FACILITY_NOTE).append("-").append(proInstance.getProName()).append("-").append(curTaskName);
		
		if(FocusReportNode363Enum.FIND_NODE_NAME.getTaskNodeName().equals(curTaskName)) {
			if(CommonFunctions.isNotBlank(params, "dataSource")) {
				if(MarFacDataSourceEnum.GRID_INSPECT.getDataSource().equals(StringUtils.toString(params.get("dataSource")))) {
					trigCondition.append("-").append(params.get("nodeName"));
				}else {
					trigCondition.append("-").append(params.get("dataSource"));
				}
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
		
		if(adviceNote.contains("@dataSourceName@")) {
			String dataSourceName = "";
			if(CommonFunctions.isNotBlank(params, "dataSource")){
				dataSourceName = MarFacDataSourceEnum.getValue(params.get("dataSource").toString());
			}
			adviceNote = adviceNote.replace("@dataSourceName@", dataSourceName);
		}
		
		if(adviceNote.contains("@damageModeName@")) {
			String dataSourceName = "";
			if(CommonFunctions.isNotBlank(params, "damageModeName")){
				dataSourceName = params.get("damageModeName").toString();
			}
			adviceNote = adviceNote.replace("@damageModeName@", dataSourceName);
		}
		
		if(adviceNote.contains("@departmentName@")) {
			String dataSourceName = "";
			if(CommonFunctions.isNotBlank(params, "departmentName")){
				dataSourceName = params.get("departmentName").toString();
			}
			adviceNote = adviceNote.replace("@departmentName@", dataSourceName);
		}
		
		if(adviceNote.contains("@occurred2@")) {
			adviceNote = adviceNote.replace("@occurred2@", "@occurred@");
		}
		
		return adviceNote;
	}
	
	
	public Map<String, Object> getNodeInfoForEventV2(
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
		String reportType = ReportTypeEnum.martyrsFacility.getReportTypeIndex();
		String new_regionCode = null;
		
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
		
		if(CommonFunctions.isNotBlank(params, "new_regionCode")) {
			try {
				new_regionCode = params.get("new_regionCode").toString();
			} catch(NumberFormatException e) {}
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
		
		if(CommonFunctions.isNotBlank(reportFocusMap, "dataSource")) {
			String dataSource = reportFocusMap.get("dataSource").toString();
			params.put("dataSource", dataSource);
		}
		
		if(CommonFunctions.isNotBlank(reportFocusMap, "regionCode")) {
			boolean isAnalysisOperateUser = true;
			OrgSocialInfoBO orgInfo = null;
			//当前用户所属组织
			OrgSocialInfoBO userOrgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
			Map<String, Object> orgParam = new HashMap<String, Object>();
			List<OrgSocialInfoBO> orgInfoList = null;
			//变更所属区域
			orgParam.put("regionCode", reportFocusMap.get("regionCode").toString());
			if(!StringUtils.isBlank(new_regionCode)) {
				orgParam.put("regionCode", new_regionCode);
			}
			
			orgParam.put("orgType", ConstantValue.ORG_TYPE_UNIT);
			orgParam.put("professionCode", ConstantValue.GOV_PROFESSION_CODE);
			
			orgInfoList = orgSocialInfoService.findOrgSocialListByPara(orgParam);
			
			if(orgInfoList != null && orgInfoList.size() > 0) {
				orgInfo = orgInfoList.get(0);
			} else {
				throw new ZhsqEventException("所属地域对应的组织不存在");
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
				List<Map<String, Object>> cfgMapList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> tempCfgMapList = reportMsgCCCfgService.findCfg4List(cfgParams);
				
				String orgChiefLevel = orgInfo.getChiefLevel();
				for(Map<String, Object> tempCfgMap : tempCfgMapList) {
					//过滤向下查 小于所属区域层级的情况
					if(tempCfgMap.containsKey("orgChiefLevel")
							&&orgChiefLevel.compareTo(StringUtils.toString(tempCfgMap.get("orgChiefLevel")))<0) {
						continue;
					}else {
						cfgMapList.add(tempCfgMap);
					}
				}
				cfgMap = reportMsgCCCfgService.findCfg4User(params, orgInfo, cfgMapList);
			}
			
			if(cfgMap != null && cfgMap.size() > 0) {
				String ccTypeDistribute = ReportMsgCCTypeEnum.CCTYPE_DISTRIBUTE.getCcType(),
					   ccTypeSelect = ReportMsgCCTypeEnum.CCTYPE_SELECT.getCcType();
				
				//分送人员
				if(cfgMap.containsKey(ccTypeDistribute)) {
					Map<String, Object> distributeUser = null;
					List<UserBO> distributeUserList = cfgMap.get(ccTypeDistribute);
					params.put("curUserInfo", userInfo);
					List<UserBO> extraUserList = findExtraCCUserV2(curnodeName, nodeName, proInstance, params);
					
					if(extraUserList != null) {
						distributeUserList = distributeUserList == null ? new ArrayList<UserBO>() : distributeUserList;
						distributeUserList.addAll(extraUserList);
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
	 * 获取配置体系外的消息配送人员信息
	 * @param curTaskName	当前环节名称
	 * @param nextTaskName	下一办理环节名称
	 * @param proInstance	流程实例信息
	 * @param params
	 * @return
	 */
	private List<UserBO> findExtraCCUserV2(String curTaskName, String nextTaskName, ProInstance proInstance, Map<String, Object> params) {
		String formTypeIdStr = null;
		String dataSource = null;
		List<UserBO> allList = new ArrayList<UserBO>();
		UserInfo curUserInfo = null;
		if(params.containsKey("curUserInfo")) {
			curUserInfo = (UserInfo) params.get("curUserInfo");
		}
		if(params.containsKey("dataSource")) {
			dataSource = params.get("dataSource").toString();
		}
		
		if(proInstance != null) {
			formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
		}
		
		return allList;
	}
	
	private UserBO findExtraCCUserV2Creator(ProInstance proInstance, Map<String, Object> params) {
		
		Long creatorId = null, creatorOrgId = null;
		String creatorName = null, creatorOrgName = null;
		UserBO creatorBO = null;
		
		if(proInstance != null) {
			creatorId = proInstance.getUserId();
			creatorName = proInstance.getUserName();
			creatorOrgId = proInstance.getOrgId();
			creatorOrgName = proInstance.getOrgName();
		}
		
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
		
		if(creatorId != null && creatorId > 0 && creatorOrgId != null && creatorOrgId > 0) {
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
		
		return creatorBO;
	}
}
