package cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl;

import cn.ffcs.sms.bo.SendResult;
import cn.ffcs.sms.service.SendSmsService;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.workflow.spring.WorkflowHolidayInfoService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.threeOneTreatment.ReportTOTMapper;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl.FocusReportNode35201Enum;
import cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl.FocusReportNode352Enum;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.service.impl.ReportMsgCCTypeEnum;
import cn.ffcs.zhsq.reportFocus.reportMsgCCSet.IReportMsgCCSetService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportFocusWorkflowServiceImpl;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportServiceAgent;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * ?????????????????????????????????
 * @Description: 
 * @ClassName:   ReportFocusWorkflow4TwoVioPreServiceImpl   
 * @author:      ?????????(zhangls)
 * @date:        2020???9???14??? ??????9:19:44
 */
@Service(value = "reportFocusWorkflow4TwoVioPreService")
public class ReportFocusWorkflow4TwoVioPreServiceImpl extends ReportFocusWorkflowServiceImpl {

	@Autowired
	private IReportFocusService reportFocusService;
	
	@Autowired
	private IReportMsgCCSetService reportMsgCCSetService;
	
	@Autowired
	private IWorkflow4BaseService workflow4BaseService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;

	@Autowired
	private IFiveKeyElementService fiveKeyElementService;

	@Autowired
	private IReportIntegrationService reportIntegrationService;

	@Autowired
	private SendSmsService sendSmsService;

	@Autowired
	private UserManageOutService userManageOutService;

	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;

	@Autowired
	private WorkflowHolidayInfoService workflowHolidayInfoService;

	@Autowired
	private ReportTOTMapper reportTOTMapper;

	@Autowired
	private HessianFlowService hessianFlowService;
	
	//???????????????????????? ????????????
	private final String[] REMOVE_NODE_CODE = {"KS"};
	private final String HANDLING_PRO_STATUS = "1";	//???????????????????????????
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean startWorkflow4Report(Long reportId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String workflowName = this.findWorkflowName(reportId, userInfo, extraParam), 
			   wfTypeId = this.findWfTypeId(reportId, userInfo, extraParam);
		boolean flag = false;
		Map<String, Object> reportFocusMap = null;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(StringUtils.isBlank(workflowName)) {
			throw new IllegalArgumentException("???????????????????????????????????????");
		}
		if(StringUtils.isBlank(wfTypeId)) {
			throw new IllegalArgumentException("???????????????????????????????????????");
		}
		
		if(reportId == null || reportId <= 0) {
			//???????????????????????????
			if(CommonFunctions.isNotBlank(extraParam, "reportId")) {
				reportId = Long.valueOf(extraParam.get("reportId").toString());
			} else if(CommonFunctions.isNotBlank(extraParam, "formId")) {
				reportId = Long.valueOf(extraParam.get("formId").toString());
			}
		}
		
		if(reportId != null && reportId > 0 && CommonFunctions.isBlank(extraParam, "reportId")) {
			extraParam.put("reportId", reportId);
		}
		
		if((reportId == null || reportId <= 0) || CommonFunctions.isBlank(extraParam, "reportUUID")) {
			Map<String, Object> reportFocusParams = new HashMap<String, Object>();
			
			reportFocusParams.putAll(extraParam);
			
			if(CommonFunctions.isBlank(reportFocusParams, "reportType")) {
				reportFocusParams.put("reportType", ReportTypeEnum.twoVioPre.getReportTypeIndex());
			}
			
			reportFocusMap = reportFocusService.findReportFocusByUUID(null, userInfo, reportFocusParams);
			
			if(reportFocusMap == null || reportFocusMap.isEmpty()) {
				throw new ZhsqEventException("????????????????????????????????????");
			}
			
			if(CommonFunctions.isNotBlank(reportFocusMap, "reportId")) {
				//???????????????????????????
				reportId = Long.valueOf(reportFocusMap.get("reportId").toString());
			}
		}
		
		if(reportId != null && reportId > 0) {
			if(CommonFunctions.isBlank(extraParam, "reportUUID") && CommonFunctions.isNotBlank(reportFocusMap, "reportUUID")) {
				extraParam.put("reportUUID", reportFocusMap.get("reportUUID"));
			}
			
			if(CommonFunctions.isBlank(extraParam, "reportId")) {
				extraParam.put("reportId", reportId);
			}
			
			flag = workflow4BaseService.startWorkflow4Base(reportId, workflowName, wfTypeId, userInfo, extraParam);
		} else {
			throw new IllegalArgumentException("?????????????????????id???");
		}
		
		if(flag) {
			flag = subWorkflow2AppointedNextNode(userInfo, extraParam);
		}
		
		return flag;
	}
	
	@SuppressWarnings("serial")
	@Override
	public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false, isSubByParent = true;
		String formTypeId = null, 
			   curNodeName = null,
			   nextUserIds = null, 
			   nextOrgIds = null,
			   workflowName = null;
		ProInstance proInstance = null;
		Long reportId = null;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if((instanceId == null || instanceId < 0) && CommonFunctions.isNotBlank(extraParam, "instanceId")) {
			try {
				instanceId = Long.valueOf(extraParam.get("instanceId").toString());
			} catch(NumberFormatException e) {}
		}
		if(StringUtils.isBlank(nextNodeName)) {
			throw new IllegalArgumentException("?????????????????????????????????");
		}
		if(nextUserInfoList == null || nextUserInfoList.size() == 0) {
			nextUserInfoList = new ArrayList<UserInfo>();
			
			if(CommonFunctions.isNotBlank(extraParam, "nextUserIds")) {
				nextUserIds = extraParam.get("nextUserIds").toString();
			}
			if(CommonFunctions.isNotBlank(extraParam, "nextOrgIds")) {
				nextOrgIds = extraParam.get("nextOrgIds").toString();
			}
			if(StringUtils.isNotBlank(nextUserIds) && StringUtils.isNotBlank(nextOrgIds)) {
				String[] nextUserArray = nextUserIds.split(","),
						 nextOrgIdArray = nextOrgIds.split(",");
				String nextUserIdStr = null, nextOrgIdStr = null;
				Long nextUserId = -1L, nextOrgId = -1L;
				UserInfo nextUserInfo = null;
				Set<String> nextUserInfoSet = new HashSet<String>();
				StringBuffer nextUserInfoKey = new StringBuffer("");
				
				for(int index = 0, len = nextUserArray.length; index < len; index++) {
					nextUserIdStr = nextUserArray[index];
					nextOrgIdStr = nextOrgIdArray[index];
					
					if(StringUtils.isNotBlank(nextUserIdStr)) {
						nextUserId = Long.valueOf(nextUserIdStr);
					}
					if(StringUtils.isNotBlank(nextOrgIdStr)) {
						nextOrgId = Long.valueOf(nextOrgIdStr);
					}
					if(nextUserId > 0 && nextOrgId > 0) {
						nextUserInfoKey = new StringBuffer("");
						nextUserInfoKey.append(nextUserId).append("-").append(nextOrgId);
						
						if(!nextUserInfoSet.contains(nextUserInfoKey.toString())) {
							nextUserInfoSet.add(nextUserInfoKey.toString());
							
							nextUserInfo = new UserInfo();
							nextUserInfo.setUserId(nextUserId);
							nextUserInfo.setOrgId(nextOrgId);
							nextUserInfoList.add(nextUserInfo);
						}
					}
				}
			} else if(StringUtils.isNotBlank(nextOrgIds)) {
				String[] nextOrgIdArray = nextOrgIds.split(",");
				String nextOrgIdStr = null;
				Long nextOrgId = -1L;
				UserInfo nextUserInfo = null;
				Set<Long> nextOrgSet = new HashSet<Long>();
				
				for(int index = 0, len = nextOrgIdArray.length; index < len; index++) {
					nextOrgIdStr = nextOrgIdArray[index];
					
					if(StringUtils.isNotBlank(nextOrgIdStr)) {
						nextOrgId = Long.valueOf(nextOrgIdStr);
					}
					if(nextOrgId > 0) {
						if(!nextOrgSet.contains(nextOrgId)) {
							nextOrgSet.add(nextOrgId);
							
							nextUserInfo = new UserInfo();
							nextUserInfo.setOrgId(nextOrgId);
							nextUserInfoList.add(nextUserInfo);
						}
					}
				}
			}
		}
		
		if(nextUserInfoList.size() == 0) {
			//????????????????????????????????????????????????????????????????????????????????????
			if(FocusReportNode350Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName) 
					&& userInfo != null) {
				nextUserInfoList = new ArrayList<UserInfo>();
				nextUserInfoList.add(userInfo);
			} else {
				throw new IllegalArgumentException("???????????????????????????????????????");
			}
		}
		
		if(instanceId != null && instanceId > 0) {
			proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			if(proInstance != null) {
				formTypeId = String.valueOf(proInstance.getFormTypeId());
				curNodeName = proInstance.getCurNode();
				
				if(CommonFunctions.isBlank(extraParam, "proInstance")) {
					extraParam.put("proInstance", proInstance);
				}
			} else {
				throw new IllegalArgumentException("????????????id???"+ instanceId +"???????????????????????????????????????");
			}
		} else {
			throw new IllegalArgumentException("???????????????????????????id???");
		}
		if(proInstance != null) {
			if(reportId == null || reportId<= 0) {
				reportId = proInstance.getFormId();
			}
			if(StringUtils.isBlank(workflowName)){
				workflowName = proInstance.getProName();
			}
		}
		OrgSocialInfoBO orgInfoTmp = null;
		Map<String, Object> reportParams = new HashMap<String, Object>(),
				reportFocusMap = null;
		List<UserInfo> userInfoList = null;

		reportParams.putAll(extraParam);
		reportParams.put("reportId", reportId);
		
		if(CommonFunctions.isBlank(reportParams, "reportType")) {
			reportParams.put("reportType", ReportTypeEnum.twoVioPre.getReportTypeIndex());
		}
		
		reportFocusMap = reportIntegrationService.findReportFocusByUUID(null, userInfo, reportParams);

		if(CommonFunctions.isNotBlank(reportFocusMap, "regionCode")) {
			Map<String, Object> orgParam = new HashMap<String, Object>();
			List<OrgSocialInfoBO> orgInfoList = null;
			
			orgParam.put("regionCode", reportFocusMap.get("regionCode").toString());
			orgParam.put("orgType", ConstantValue.ORG_TYPE_UNIT);
			orgParam.put("professionCode", ConstantValue.GOV_PROFESSION_CODE);
			
			orgInfoList = orgSocialInfoService.findOrgSocialListByPara(orgParam);
			
			if(orgInfoList != null && orgInfoList.size() > 0) {
				orgInfoTmp = orgInfoList.get(0);
			}
		}

		UserInfo userInfoTmp = new UserInfo();
		if(null != orgInfoTmp){
			userInfoTmp.setOrgId(orgInfoTmp.getOrgId());
			userInfoTmp.setOrgName(orgInfoTmp.getOrgName());
			userInfoTmp.setOrgCode(orgInfoTmp.getOrgCode());
		}

		if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeId)
				&& FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
				&& FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
			//?????????????????????????????????
			String nodeCodeTmp = null;
			List<GdZTreeNode> treeNodeList = null;
			Node node = null;
			userInfoList = new ArrayList<UserInfo>();

			//????????????????????????(???????????????)???????????????????????????????????????????????????_?????????????????????
			node = baseWorkflowService.capNodeInfo(workflowName, FocusReportNode350Enum.WORKFLOW_TYPE_ID.toString(), reportId, FocusReportNode350Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName(), userInfoTmp);
			if(node != null) {
				extraParam.put("nodeId", node.getNodeId());
				nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.GRID + INodeCodeHandler.OPERATE_REPORT + (INodeCodeHandler.GRID - INodeCodeHandler.COMMUNITY) + INodeCodeHandler.ORG_UINT + INodeCodeHandler.COMMUNITY;
				treeNodeList = fiveKeyElementService.getTreeForEvent(userInfoTmp, null, nodeCodeTmp, null, null, extraParam);

				if(treeNodeList != null) {
					for(GdZTreeNode treeNode : treeNodeList) {
						userInfoList.addAll(fiveKeyElementService.getUserInfoList(Long.valueOf(treeNode.getId()), nodeCodeTmp, extraParam));
					}
				}
			}
			nextUserInfoList.addAll(userInfoList);
			//????????????????????????????????????????????????
			if(nextUserInfoList != null && nextUserInfoList.size() > 0) {
				Long nextUserOrgId = null, nextUserId = null,
					 remainUserId = userInfo.getUserId(), remainUserOrgId = userInfo.getOrgId();
				OrgSocialInfoBO orgInfo = null;
				String orgChiefLevel = null, orgType = null,
					   curTaskId = null, orgTypeUnit = String.valueOf(ConstantValue.ORG_TYPE_UNIT);
				Map<String, Object> subProParams = null,
									curTaskData = workflow4BaseService.findCurTaskData4Base(instanceId);
				List<UserInfo> removeUserInfoList = new ArrayList<UserInfo>();
				boolean subFlowResult = false;
				
				if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
					curTaskId = curTaskData.get("WF_DBID_").toString();
				}
				
				for(UserInfo nextUserInfo : nextUserInfoList) {
					nextUserId = nextUserInfo.getUserId();
					nextUserOrgId = nextUserInfo.getOrgId();

					//???????????????????????????????????????
					if(!remainUserId.equals(nextUserId) || !remainUserOrgId.equals(nextUserOrgId)) {
						orgInfo = orgSocialInfoService.findByOrgId(nextUserOrgId);
						
						if(orgInfo != null) {
							orgChiefLevel = orgInfo.getChiefLevel();
							orgType = orgInfo.getOrgType();
							subProParams = new HashMap<String, Object>();
							
							subProParams.putAll(extraParam);
							subProParams.put("parentId", instanceId);
							subProParams.put("taskId", curTaskId);
							subProParams.put("isConvertProinstance", false);
							subProParams.remove("instanceId");//????????????????????????????????????????????????
							
							if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel) && orgTypeUnit.equals(orgType)) {
								//?????????????????????????????????-??????????????????????????????
								subProParams.put("formTypeId", FocusReportNode3500Enum.FORM_TYPE_ID.toString());
								
								removeUserInfoList.add(nextUserInfo);
								
								subFlowResult = this.startWorkflow4Report(proInstance.getFormId(), nextUserInfo, subProParams);

								//????????????????????? ??????mq??????
								if(subFlowResult){
									sendSubFlowRMQmMsg(reportId,FocusReportNode350Enum.FORM_TYPE_ID.toString(),FocusReportNode3500Enum.FORM_TYPE_ID.toString(),instanceId,subProParams.get("instanceId"));
								}
							}
						}
					}
				}
				//?????????????????????????????????
				if(subFlowResult && CommonFunctions.isNotBlank(extraParam,"advice")){
					String advice = null;
					advice = String.valueOf(extraParam.get("advice"));
					advice = this.transAdviceContent(advice,userInfo,subProParams);
					this.sendSMS(advice, userInfo, removeUserInfoList);
				}

				//??????????????????????????????????????????????????????????????????????????????
				nextUserInfoList.removeAll(removeUserInfoList);
			}
		} else if(false && FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeId)
				&& FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
				&& FocusReportNode350Enum.ASSIGN_TASK_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
			//??????
			//????????????????????????????????????????????????_???????????????????????????
			nextUserInfoList.add(userInfo);
			//????????????????????????????????????????????????
			if(nextUserInfoList != null && nextUserInfoList.size() > 0) {
				Long nextUserOrgId = null;
				OrgSocialInfoBO orgInfo = null;
				String orgChiefLevel = null, orgType = null,
					   curTaskId = null, orgTypeUnit = String.valueOf(ConstantValue.ORG_TYPE_UNIT);
				Map<String, Object> subProParams = null,
									curTaskData = workflow4BaseService.findCurTaskData4Base(instanceId);
				List<UserInfo> removeUserInfoList = new ArrayList<UserInfo>();
				
				if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
					curTaskId = curTaskData.get("WF_DBID_").toString();
				}
				
				for(UserInfo nextUserInfo : nextUserInfoList) {
					nextUserOrgId = nextUserInfo.getOrgId();
					
					orgInfo = orgSocialInfoService.findByOrgId(nextUserOrgId);
					
					if(orgInfo != null) {
						orgChiefLevel = orgInfo.getChiefLevel();
						orgType = orgInfo.getOrgType();
						subProParams = new HashMap<String, Object>();
						
						subProParams.putAll(extraParam);
						subProParams.put("parentId", instanceId);
						subProParams.put("taskId", curTaskId);
						subProParams.put("isConvertProinstance", false);
						subProParams.remove("instanceId");//????????????????????????????????????????????????
						
						if(ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel) && orgTypeUnit.equals(orgType)) {
							//?????????????????????????????????_????????????????????????????????????
							subProParams.put("formTypeId", FocusReportNode3501Enum.FORM_TYPE_ID.toString());
							
							removeUserInfoList.add(nextUserInfo);
							
							boolean subFlowResult = this.startWorkflow4Report(proInstance.getFormId(), nextUserInfo, subProParams);

							if(subFlowResult){
								sendSubFlowRMQmMsg(reportId,formTypeId,FocusReportNode3501Enum.FORM_TYPE_ID.toString(),instanceId,subProParams.get("instanceId"));
							}
						}
					}
				}
				
				//??????????????????????????????????????????????????????????????????????????????
				nextUserInfoList.removeAll(removeUserInfoList);
			}
		} else if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeId)
				&& FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
				&& FocusReportNode350Enum.STREET_FEEDBACK_TASK_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
			//???????????????????????????-??????????????????????????????
			isSubByParent = true;
			
			Map<String, Object> curTaskData = workflow4BaseService.findCurTaskData4Base(instanceId),
								subProParams = null,
								instanceParams = null;
			String curTaskId = null, nextOrgName = null;
			Long nextUserId = null, nextOrgId = null;
			OrgSocialInfoBO orgInfo = null;
			StringBuffer msgBuffer = new StringBuffer("");
			int failCount = 0;

			if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
				curTaskId = curTaskData.get("WF_DBID_").toString();
			}

			nextUserId = userInfo.getUserId();
			nextOrgId = userInfo.getOrgId();
			//???????????????????????????????????????????????????????????????-????????????????????????????????????????????????????????????
			instanceParams = new HashMap<String, Object>();
			instanceParams.put("formTypeId", FocusReportNode3502Enum.FORM_TYPE_ID.toString());
			instanceParams.put("instanceTaskId", curTaskId);
			instanceParams.put("parentInstanceId", instanceId);
			instanceParams.put("instanceStatus", HANDLING_PRO_STATUS);
			instanceParams.put("isHandledUser", true);
			instanceParams.put("handledUserId", nextUserId);
			instanceParams.put("handledOrgId", nextOrgId);

			if(baseWorkflowService.findProInstanceCount(instanceParams) == 0) {
				//?????????????????????????????????-??????????????????????????????
				subProParams = new HashMap<String, Object>();

				subProParams.putAll(extraParam);
				subProParams.put("formTypeId", FocusReportNode3502Enum.FORM_TYPE_ID.toString());
				subProParams.put("parentId", instanceId);
				subProParams.put("taskId", curTaskId);
				subProParams.put("isConvertProinstance", false);
				subProParams.remove("instanceId");//????????????????????????????????????????????????

				//??????????????????????????????????????? ???????????????????????????
				//nextUserIds nextOrgIds
				UserInfo subUserInfo = new UserInfo();
				if(CommonFunctions.isNotBlank(extraParam,"nextUserIds")){
					try {
						subUserInfo.setOrgId(Long.valueOf(String.valueOf(extraParam.get("nextOrgIds"))));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				if(CommonFunctions.isNotBlank(extraParam,"nextOrgIds")){
					try {
						subUserInfo.setUserId(Long.valueOf(String.valueOf(extraParam.get("nextUserIds"))));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				boolean subFlowResult = this.startWorkflow4Report(proInstance.getFormId(), subUserInfo, subProParams);
				//????????????????????? ??????mq??????
				if(subFlowResult){
					sendSubFlowRMQmMsg(reportId,formTypeId,FocusReportNode3502Enum.FORM_TYPE_ID.toString(),instanceId,subProParams.get("instanceId"));
				}
			}
			//????????????????????????????????????????????????
			flag = true;
			//??????????????? ??????????????? ??????????????????????????????
			extraParam.put("nextOrgIds",userInfo.getOrgId());
			extraParam.put("nextUserIds",userInfo.getUserId());

			if(nextUserInfoList != null && nextUserInfoList.size() > 0){
				nextUserInfoList.clear();
				nextUserInfoList.add(userInfo);
			}
		}else if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeId)
				&& FocusReportNode350Enum.STREET_FEEDBACK_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
				&& ConstantValue.HANDLING_TASK_CODE.equals(nextNodeName)){
			//??????????????????????????? ????????????????????????(??????????????????) ?????????????????? ??????????????????????????? ?????????????????????
			isSubByParent = false;

			String advice = null;
			if(CommonFunctions.isNotBlank(extraParam,"advice")){
				advice = String.valueOf(extraParam.get("advice"));
			}
			Map<String,Object> resultMap = eventDisposalWorkflowService.saveHandlingTask(reportId, instanceId, nextNodeName, advice, userInfo,extraParam);
			flag = resultMap != null;
			//?????????????????????????????? ??????????????? ???????????????+3????????????
			//????????????????????????
			Map<String,Object> curTaskMap = baseWorkflowService.findCurTaskData(instanceId);
			String curTaskId_ = "";
			if(CommonFunctions.isNotBlank(curTaskMap,"DUEDATE_")){
				Date curTaskDueDate = (Date) curTaskMap.get("DUEDATE_"),
						currentDate = new Date();

				if(null != curTaskDueDate){
					currentDate = workflowHolidayInfoService.getWorkDateByInterval(curTaskDueDate, 3);
				}

				curTaskId_ = curTaskMap.get("WF_DBID_").toString();
				reportParams.put("newDate", currentDate);
				reportParams.put("taskId", curTaskId_);
				reportTOTMapper.updateTaskTime(reportParams);
			}
		} else if(FocusReportNode3502Enum.FORM_TYPE_ID.toString().equals(formTypeId)
				&& FocusReportNode3502Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
			isSubByParent = false;
			
			if(CommonFunctions.isBlank(extraParam, "instanceId")) {
				extraParam.put("instanceId", instanceId);
			}
			
			flag = workflow4BaseService.subWorkflow4Base(null, null, null, nextNodeName, nextUserInfoList, userInfo, extraParam);
			
			if(flag) {
				//??????????????????????????????
				Map<String, Object> instanceParams = new HashMap<String, Object>();
				Long parentInstanceId = proInstance.getParentId(),
					 parentInstanceTaskId = proInstance.getTaskId();
				
				instanceParams.put("parentInstanceId", parentInstanceId);
				instanceParams.put("instanceTaskId", parentInstanceTaskId);
				instanceParams.put("instanceStatus", HANDLING_PRO_STATUS);
				
				if(baseWorkflowService.findProInstanceCount(instanceParams) == 0) {
					Map<String, Object> curDataMap = null;
					
					try {
						curDataMap = this.workflow4BaseService.findCurTaskData4Base(parentInstanceId);
					} catch(Exception e) {}
					
					if(curDataMap != null && !curDataMap.isEmpty()) {
						List<UserInfo> nextUserInfoList4Parent = new ArrayList<UserInfo>();
						UserInfo userInfo4Parent = new UserInfo();
						Map<String, Object> subMap4Parent = new HashMap<String, Object>();
						
						if(CommonFunctions.isNotBlank(curDataMap, "WF_USERID_ALL")) {
							userInfo4Parent.setUserId(Long.valueOf(curDataMap.get("WF_USERID_ALL").toString().split(",")[0]));
						}
						if(CommonFunctions.isNotBlank(curDataMap, "WF_USERNAME_ALL")) {
							userInfo4Parent.setPartyName(curDataMap.get("WF_USERNAME_ALL").toString().split(",")[0]);
						}
						if(CommonFunctions.isNotBlank(curDataMap, "WF_ORGID_ALL")) {
							Long userOrgId4Parent = Long.valueOf(curDataMap.get("WF_ORGID_ALL").toString().split(",")[0]);
							OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userOrgId4Parent);
							
							if(orgInfo != null) {
								userInfo4Parent.setOrgId(userOrgId4Parent);
								userInfo4Parent.setOrgCode(orgInfo.getOrgCode());
								userInfo4Parent.setOrgName(orgInfo.getOrgName());
							}
						}
						
						nextUserInfoList4Parent.add(userInfo4Parent);
						subMap4Parent.putAll(extraParam);
						subMap4Parent.put("instanceId", parentInstanceId);
						
						flag = subWorkflow4Report(parentInstanceId, FocusReportNode3501Enum.END_NODE_NAME.getTaskNodeName(), nextUserInfoList4Parent, userInfo4Parent, subMap4Parent);
						
						if(flag) {
							endAllSubProInstance(parentInstanceId, null,null);
							
							Map<String, Object> updateStatusParams = new HashMap<String, Object>();
							updateStatusParams.putAll(extraParam);
							updateStatusParams.put("nextNodeName", FocusReportNode350Enum.END_NODE_NAME.getTaskNodeName());
							updateStatusParams.put("instanceId", parentInstanceId);
							
							updateReportStatus(null, parentInstanceId, userInfo, updateStatusParams);
						}
					}
				}
			}
		}else if(FocusReportNode3500Enum.FORM_TYPE_ID.toString().equals(formTypeId)
				&& FocusReportNode3500Enum.DISASSEMBLE_NODE_NAME.getTaskNodeName().equals(curNodeName)
				&& FocusReportNode3500Enum.REGULAR_REPORT_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
			//????????????????????????_?????????????????? ??????????????????????????? ???????????????????????????????????? ?????????????????????
			isSubByParent = false;
			if(CommonFunctions.isBlank(extraParam, "instanceId")) {
				extraParam.put("instanceId", instanceId);
			}

			flag = workflow4BaseService.subWorkflow4Base(null, null, null, nextNodeName, nextUserInfoList, userInfo, extraParam);

			//?????????????????????????????? ?????????????????????
			if(flag){
				//??????????????????????????????
				Long parentInstanceId = proInstance.getParentId();

				endAllSubProInstance(parentInstanceId, FocusReportNode3500Enum.FORM_TYPE_ID.toString(),instanceId);
			}
		}
		
		if(isSubByParent) {
			if(CommonFunctions.isBlank(extraParam, "instanceId")) {
				extraParam.put("instanceId", instanceId);
			}
			
			flag = workflow4BaseService.subWorkflow4Base(null, null, null, nextNodeName, nextUserInfoList, userInfo, extraParam);
			
			if(flag && FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeId)
					&& (FocusReportNode350Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName))) {
				//?????????????????????????????????????????????
				endAllSubProInstance(instanceId, null,null);
				
				Map<String, Object> updateStatusParams = new HashMap<String, Object>();
				updateStatusParams.putAll(extraParam);
				updateStatusParams.put("nextNodeName", FocusReportNode350Enum.END_NODE_NAME.getTaskNodeName());
				updateStatusParams.put("instanceId", instanceId);
				
				updateReportStatus(null, instanceId, userInfo, updateStatusParams);
			}
		}
		
		if(flag && CommonFunctions.isBlank(extraParam, "msgSendMidSendType")) {
			String TWO_VIO_PRE_SEND_TYPE = "twoVioPre";//??????????????????????????????
			
			extraParam.put("msgSendMidSendType", TWO_VIO_PRE_SEND_TYPE);
		}
		
		if(flag && proInstance != null) {
			Map<String, Object> pointParams = new HashMap<String, Object>();
			int RECORD_TYPE_SUB = 1;
			
			pointParams.putAll(extraParam);
			pointParams.put("proInstance", proInstance);
			
			try {
				reportFocusService.recordPoint(RECORD_TYPE_SUB, userInfo, pointParams);
			} catch(Exception e) {
				if(logger.isErrorEnabled()) {
					logger.error(e.getMessage());
				}
			}
			
			if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeId)) {
				Map<String, Object> reportFocus = new HashMap<String, Object>();
				boolean isSaveReportFocusExtend = false;
				
				if(FocusReportNode350Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
						&& FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
					if(extraParam.containsKey("belongRegionName")) {
						isSaveReportFocusExtend = true;
						reportFocus.put("belongRegionName", extraParam.get("belongRegionName"));
					}
					
					if(extraParam.containsKey("verifyStatus")) {
						isSaveReportFocusExtend = true;
						reportFocus.put("verifyStatus", extraParam.get("verifyStatus"));
					}
				} else if(FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
						&& FocusReportNode350Enum.STREET_DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
					if(extraParam.containsKey("belongRegionName")) {
						isSaveReportFocusExtend = true;
						reportFocus.put("belongRegionName", extraParam.get("belongRegionName"));
					}
				} else if(FocusReportNode350Enum.STREET_DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
						&& FocusReportNode350Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
					if(CommonFunctions.isNotBlank(extraParam, "regionCode")) {
						reportFocus.put("regionCode", extraParam.get("regionCode"));
					}
				} else if((FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode350Enum.STREET_FEEDBACK_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName))
						&& FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
					Map<String, String> identificationResultMap = new HashMap<String, String>() {
						{
							put(FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreIdentificationResultEnum.NOT_BELONG_TWO_VIO.getIdentificationResult());
							put(FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreIdentificationResultEnum.COMMUNITY_CLOSE.getIdentificationResult());
							put(FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreIdentificationResultEnum.STREET_CLOSE.getIdentificationResult());
							put(FocusReportNode350Enum.STREET_FEEDBACK_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreIdentificationResultEnum.STREET_CLOSE.getIdentificationResult());
							put(FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreIdentificationResultEnum.MUNICIPAL_DEPARTMENT_CLOSE.getIdentificationResult());
						}
					};
					
					isSaveReportFocusExtend = true;
					reportFocus.put("closeTime", new Date());
					reportFocus.put("closeUserId", userInfo.getUserId());
					reportFocus.put("closeOrgId", userInfo.getOrgId());
					reportFocus.put("identificationResult", identificationResultMap.get(curNodeName));
				}
				
				if(!reportFocus.isEmpty()) {
					String reportType = ReportTypeEnum.twoVioPre.getReportTypeIndex();
					
					if(CommonFunctions.isNotBlank(extraParam, "reportType")) {
						reportType = extraParam.get("reportType").toString();
					}
					
					reportFocus.put("reportId", proInstance.getFormId());
					reportFocus.put("isSaveReportFocusExtend", isSaveReportFocusExtend);
					reportFocus.put("reportType", reportType);
					
					try {
						reportFocusService.saveReportFocus(reportFocus, userInfo);
					} catch(Exception e) {
						e.printStackTrace();
						if(logger.isErrorEnabled()) {
							logger.error(e.getMessage());
						}
					}
				}
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean rejectWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false, isRejectByParent = true, isBack2Draft = false;
//		Long reportId = null;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(CommonFunctions.isNotBlank(extraParam, "isRejectByParent")) {
			isRejectByParent = Boolean.valueOf(extraParam.get("isRejectByParent").toString());
		}
		
		if(CommonFunctions.isNotBlank(extraParam, "isBack2Draft")) {
			isBack2Draft = Boolean.valueOf(extraParam.get("isBack2Draft").toString());
		}
		
		//????????????isRejectByParent????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		if(isRejectByParent) {
			if(instanceId == null || instanceId < 0) {
				throw new IllegalArgumentException("???????????????????????????id???");
			}
			
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			String formTypeId = null, curNodeName = null;
			
			if(proInstance != null) {
				formTypeId = String.valueOf(proInstance.getFormTypeId());
				curNodeName = proInstance.getCurNode();
//				reportId = proInstance.getFormId();
				
				if(CommonFunctions.isBlank(extraParam, "proInstance")) {
					extraParam.put("proInstance", proInstance);
				}
			}
			
			if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeId)) {
				if(FocusReportNode350Enum.FIND_TWO_VIO_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
					flag = true;
					isBack2Draft = true;
					isRejectByParent = false;
				} else if(FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
					//??????????????????????????????
					Map<String, Object> instanceMap = new HashMap<String, Object>(), curTaskData = null;
					
					curTaskData = this.workflow4BaseService.findCurTaskData4Base(instanceId);
					
					instanceMap.put("parentInstanceId", instanceId);
					if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
						instanceMap.put("instanceTaskId", curTaskData.get("WF_DBID_"));
					}
					instanceMap.put("formTypeId", formTypeId);
					instanceMap.put("instanceStatus", HANDLING_PRO_STATUS);
					
					if(baseWorkflowService.findProInstanceCount(instanceMap) > 0) {
						isRejectByParent = false;
						throw new ZhsqEventException("?????????????????????????????????????????????");
					}
				}
			} else if(
					(FocusReportNode3500Enum.FORM_TYPE_ID.toString().equals(formTypeId)
							&& FocusReportNode3500Enum.DISASSEMBLE_NODE_NAME.getTaskNodeName().equals(curNodeName))
					|| (FocusReportNode3501Enum.FORM_TYPE_ID.toString().equals(formTypeId)
							&& FocusReportNode3501Enum.ASSIGN_TASK_NODE_NAME.toString().equals(curNodeName))
					|| (FocusReportNode3502Enum.FORM_TYPE_ID.toString().equals(formTypeId)
							&& FocusReportNode3502Enum.DEAL_WITH_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName))) {
				//?????????????????????(task1)?????????????????????????????????
				isRejectByParent = false;
				flag = baseWorkflowService.invalidInstance(instanceId, null);
			}
			
			if(isRejectByParent) {
				flag = workflow4BaseService.rejectWorkflow4Base(null, null, null, null, userInfo, extraParam);
				
				if(flag) {
					if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeId)) {
						if(FocusReportNode350Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
							proInstance = baseWorkflowService.findProByInstanceId(instanceId);
							
							if(proInstance != null && FocusReportNode350Enum.FIND_TWO_VIO_NODE_NAME.getTaskNodeName().equals(proInstance.getCurNode())) {
								isBack2Draft = true;
							}
						} else if(FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
							//???????????????????????????????????????_?????????????????????????????????????????????
							endAllSubProInstance(instanceId, FocusReportNode3500Enum.FORM_TYPE_ID.toString(),null);
						} else if(FocusReportNode350Enum.ASSIGN_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
							//???????????????????????????????????????_???????????????????????????????????????????????????
							endAllSubProInstance(instanceId, FocusReportNode3501Enum.FORM_TYPE_ID.toString(),null);
						}
					}
				}
			}
		}
		
		if(isBack2Draft) {
			//flag = back2Draft(reportId, userInfo, extraParam);
		}
		
		return flag;
	}
	
	@Override
	public boolean recallWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false, isAble2Recall = false;
		//Long reportId = null;
		
		if(instanceId == null || instanceId < 0) {
			throw new IllegalArgumentException("???????????????????????????id???");
		}
		
		if(userInfo == null) {
			throw new IllegalArgumentException("???????????????????????????");
		}
		
//		if(CommonFunctions.isNotBlank(extraParam, "reportId")) {
//			reportId = Long.valueOf(extraParam.get("reportId").toString());
//		}
		
		if(CommonFunctions.isNotBlank(extraParam, "isAble2Recall")) {
			isAble2Recall = Boolean.valueOf(extraParam.get("isAble2Recall").toString());
		} else {
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			
			if(proInstance == null) {
				throw new IllegalArgumentException("??????id????????????????????????????????????");
			}
			
			String formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
			
			isAble2Recall = FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr);
		}
		
		//??????????????????????????????????????????
		if(isAble2Recall) {
			List<Map<String, Object>> taskMapList = workflow4BaseService.capHandledTaskInfoMap(instanceId, IWorkflow4BaseService.SQL_ORDER_DESC, extraParam);
			if(taskMapList != null) {
				Map<String, Object> taskMap = taskMapList.get(0);
				String taskCode = null;
				
				if(CommonFunctions.isNotBlank(taskMap, "TASK_CODE")) {
					taskCode = taskMap.get("TASK_CODE").toString();
				}
				
				if(FocusReportNode350Enum.FIND_TWO_VIO_NODE_NAME.getTaskNodeName().equals(taskCode)) {
					flag = baseWorkflowService.recallWorkflow4Base(instanceId, userInfo, extraParam);
				} else {
					throw new ZhsqEventException("????????????????????????????????????");
				}
			}
		} else {
			throw new ZhsqEventException("????????????????????????????????????");
		}
		
		//if(flag) {
			 //flag = back2Draft(reportId, userInfo, extraParam);
		//}
		
		return flag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> initWorkflow4Handle(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		Map<String, Object> curTaskData = null, initMap = new HashMap<String, Object>();
		boolean isAble2Handle = false,	//?????????????????????
				isEditable = false,		//?????????????????????
				isEditableNode = false,	//?????????????????????????????????????????????????????????????????????????????????????????????????????????true???????????????????????????
				isQuerySuperviseList = false;
		params = params == null ? new HashMap<String, Object>() : params;
		ProInstance proInstance = null;
		List<Map<String, Object>> superviseList = null;
		
		if(instanceId == null || instanceId <= 0) {
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				try {
					instanceId = Long.valueOf(params.get("instanceId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			} else {
				Map<String, Object> proMap = capProInstance(null, userInfo, params);
				
				if(CommonFunctions.isNotBlank(proMap, "instanceId")) {
					instanceId = Long.valueOf(proMap.get("instanceId").toString());
				}
			}
		}
		
		if(instanceId != null && instanceId > 0) {
			Long reportId = null;
			boolean isCapTaskList = true;
			String curNodeName = null;
			
			if(proInstance == null) {
				proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			}
			
			if(proInstance == null) {
				throw new IllegalArgumentException("????????????id???" + instanceId + "???????????????????????????????????????");
			}
			
			reportId = proInstance.getFormId();
			
			params.put("instanceId", instanceId);
			params.put("proInstance", proInstance);
			params.put("formId", reportId);
			
			curTaskData = workflow4BaseService.findCurTaskData4Base(instanceId);
			
			initMap.putAll(curTaskData);
			initMap.put("instanceId", instanceId);
			initMap.put("formTypeId", proInstance.getFormTypeId());
			
			if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
				curNodeName = curTaskData.get("NODE_NAME").toString();
				
				initMap.put("curNodeName", curNodeName);
				
				if(CommonFunctions.isBlank(params, "curNodeName")) {
					params.put("curNodeName", curNodeName);
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "isAble2Handle")) {
				isAble2Handle = Boolean.valueOf(params.get("isAble2Handle").toString());
			} else {
				if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
					Long taskId = null;
					
					try {
						taskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
					
					if(taskId != null && taskId > 0) {
						List<Map<String, Object>> participantMapList = workflow4BaseService.findParticipantsByTaskId(taskId);
						if(participantMapList != null) {
							String operateUserIdStr = String.valueOf(userInfo.getUserId()),
								   operateUserOrgIdStr = String.valueOf(userInfo.getOrgId());
							
							for(Map<String, Object> participantMap : participantMapList) {
								if(CommonFunctions.isNotBlank(participantMap, "USER_ID") && CommonFunctions.isNotBlank(participantMap, "ORG_ID")) {
									if(operateUserIdStr.equals(participantMap.get("USER_ID").toString()) 
											&& operateUserOrgIdStr.equals(participantMap.get("ORG_ID").toString())) {
										isAble2Handle = true;
										break;
									}
								}
							}
						}
						
						initMap.put("currentTaskId", taskId);
					}
				}
			}
			
			if(isAble2Handle) {
				Integer nodeId = null;
				List<Map<String, Object>> nextTaskNodeList = null;
				
				//???????????????????????????????????????????????? task3????????????
				if(FocusReportNode350Enum.FORM_TYPE_ID.getTaskNodeName().equals(String.valueOf(proInstance.getFormTypeId()))
						&& FocusReportNode350Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
					isEditable = true;
					isEditableNode = true;
				}
				
				if(CommonFunctions.isNotBlank(curTaskData, "NODE_ID")) {
					nodeId = Integer.valueOf(curTaskData.get("NODE_ID").toString());

					//???????????????nodeId < 0 ????????????????????????????????? nodeId > 0
					if(nodeId != null /*&& nodeId > 0*/) {
						List<Map<String, Object>> operateMapList = workflow4BaseService.findOperateByNodeId(nodeId);
						String reportStatus = null;
						
						if(CommonFunctions.isNotBlank(params, "reportFocusMap")) {
							Map<String, Object> reportFocusMap = (Map<String, Object>) params.get("reportFocusMap");
							
							if(CommonFunctions.isNotBlank(reportFocusMap, "reportStatus")) {
								reportStatus = reportFocusMap.get("reportStatus").toString();
							}
						}
						
						//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
						//??????????????????????????????????????????????????????????????????????????????????????????
						if(TwoVioPreReportStatusEnum.DRAFT_STATUS.getReportStatus().equals(reportStatus) && operateMapList != null) {
							Map<String, Object> operateMap = new HashMap<String, Object>();
							String DELETE_EVENT = "delRecord", DELETE_NAME = "??????";
							
							//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
							operateMap.put("operateEvent", DELETE_EVENT);
							operateMap.put("anotherName", DELETE_NAME);
							
							isEditable = true;
							operateMapList.add(operateMap);
						}
						
						initMap.put("operateList", operateMapList);
					}
				}
				
				nextTaskNodeList = findNextTaskNodes(instanceId, userInfo, params);
				
				initMap.put("nextTaskNodes", nextTaskNodeList);
				//?????????????????????????????????????????????????????????
				initMap.put("isChooseDefaultRadio", nextTaskNodeList != null && nextTaskNodeList.size() == 1);
			}
			
			if(CommonFunctions.isNotBlank(params, "isQuerySuperviseList")) {
				isQuerySuperviseList = Boolean.valueOf(params.get("isQuerySuperviseList").toString());
			}
			
			if(isQuerySuperviseList) {
				Map<String, Object> superviceParam = new HashMap<String, Object>();
				String CATEGORY_REMIND = "2";
				
				superviceParam.put("instanceId", instanceId);
				superviceParam.put("category", CATEGORY_REMIND);
				
				superviseList = workflow4BaseService.findUrgeOrRemindList(superviceParam);
				
				//????????????null??????????????????????????????????????????????????????????????????????????????????????????
				superviseList = superviseList == null ? new ArrayList<Map<String, Object>>() : superviseList;
				
				params.put("superviseList", superviseList);
			}
			
			if(CommonFunctions.isNotBlank(params, "isCapTaskList")) {
				isCapTaskList = Boolean.valueOf(params.get("isCapTaskList").toString());
			}
			
			if(isCapTaskList) {
				initMap.put("taskList", capHandledTaskInfoMap(instanceId, IWorkflow4BaseService.SQL_ORDER_DESC, params));
			}

			//?????????????????????????????????
			if(StringUtils.isNotBlank(curNodeName)
					&& (FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
					|| FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
					|| FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
					|| FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
					)
			){
				initMap.put("isUploadHandledPic", false);
			}
		}
		
		initMap.put("isEditable", isEditable);
		initMap.put("isEditableNode", isEditableNode);
		initMap.put("isAble2Handle", isAble2Handle);
		
		if(CommonFunctions.isBlank(initMap, "reportType")) {
			initMap.put("reportType", ReportTypeEnum.twoVioPre.getReportTypeIndex());
		}
		
		return initMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> taskMapList = workflow4BaseService.capHandledTaskInfoMap(instanceId, order, params),
								  resultMapList = new ArrayList<Map<String, Object>>();
		ProInstance proInstance = this.baseWorkflowService.findProByInstanceId(instanceId);
		String END_PRO_STATUS = "2",		//??????????????????????????????
			   ABOLISH_PRO_STATUS = "4";	//??????????????????????????????
			   //LIST_TYPE_TODO = "2";		//????????????
		
		if(taskMapList != null && taskMapList.size() > 0) {
			Map<String, Object> proMap = new HashMap<String, Object>(), msgCCSetMap = null;
			List<Map<String, Object>> proMapList = null, subProTaskMapList = null, 
									  removeTasks = new ArrayList<Map<String, Object>>();
			Map<String, Map<String, Object>> msgCCSetResultMap = null;
			Map<String, Map<String, Object>> superviseMap = new HashMap<String, Map<String, Object>>();
			String taskName = null, 
				   formTypeIdStr = null,
				   proStatus = null,
				   listType = null,
				   taskId = null;
			Object parentProTaskId = null;
			boolean isQuerySuperviseList = true;
			
			if(proInstance != null) {
				formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
				proStatus = proInstance.getStatus();
			}
			
			if(CommonFunctions.isNotBlank(params, "listType")) {
				listType = params.get("listType").toString();
			}
			
			if(CommonFunctions.isNotBlank(params, "isQuerySuperviseList")) {
				isQuerySuperviseList = Boolean.valueOf(params.get("isQuerySuperviseList").toString());
			}
			
			if(isQuerySuperviseList) {
				List<Map<String, Object>> superviseList = null;
				
				if(CommonFunctions.isNotBlank(params, "superviseList")) {
					superviseList = (List<Map<String, Object>>) params.get("superviseList");
				} else {
					Map<String, Object> superviceParam = new HashMap<String, Object>();
					String CATEGORY_REMIND = "2";
					
					superviceParam.put("instanceId", instanceId);
					superviceParam.put("category", CATEGORY_REMIND);
					
					superviseList = workflow4BaseService.findUrgeOrRemindList(superviceParam);
				}
				
				if(superviseList != null && superviseList.size() > 0) {
					String taskIdStr = null;
					
					for(Map<String, Object> supervise : superviseList) {
						if(CommonFunctions.isNotBlank(supervise, "taskId")) {
							taskIdStr = supervise.get("taskId").toString();
							
							superviseMap.put(taskIdStr, supervise);
						}
					}
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "PARENT_PRO_TASK_ID")) {
				parentProTaskId = params.get("PARENT_PRO_TASK_ID");
			}
			
			proMap.put("parentInstanceId", instanceId);
			proMap.put("instanceStatus", HANDLING_PRO_STATUS + "," + END_PRO_STATUS);// 1 ????????????2 ?????????
			
			msgCCSetResultMap = findMsgCCSetByInstanceId(instanceId);
			
			//????????????????????????
			for(Map<String, Object> mapTemp : taskMapList) {
				if(CommonFunctions.isNotBlank(mapTemp, "NODE_CODE")) {
					for(String nodeCode : REMOVE_NODE_CODE) {
						if(nodeCode.equals(mapTemp.get("NODE_CODE"))) {
							removeTasks.add(mapTemp);
						} else if(CommonFunctions.isNotBlank(mapTemp, "TASK_ID")) {
							taskId = mapTemp.get("TASK_ID").toString();
							
							if(CommonFunctions.isNotBlank(msgCCSetResultMap, taskId)) {
								msgCCSetMap = msgCCSetResultMap.get(taskId);
							}
							
							if(CommonFunctions.isNotBlank(msgCCSetMap, "distributeUserOrgNames")) {
								mapTemp.put("DISTRIBUTE_USER_ORG_NAMES", msgCCSetMap.get("distributeUserOrgNames").toString());
							}
							
							if(CommonFunctions.isNotBlank(msgCCSetMap, "selectUserOrgNames")) {
								mapTemp.put("SELECT_USER_ORG_NAMES", msgCCSetMap.get("selectUserOrgNames").toString());
							}
						}
					}
				}
			}
			
			//???????????????????????????????????????java.util.ConcurrentModificationException??????
			taskMapList.removeAll(removeTasks);
			
			if(IWorkflow4BaseService.SQL_ORDER_DESC.equals(order) && !ABOLISH_PRO_STATUS.equals(proStatus)) {
				Map<String, Object> curTaskData = workflow4BaseService.findCurTaskData4Base(instanceId);
				
				if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
					Map<String, Object> curNodeMap = new HashMap<String, Object>();
					List<Map<String, Object>> taskPerson = null;
					StringBuffer handlePerson = new StringBuffer("");
					Long currentTaskId = -1L;
					
					if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
						try {
							currentTaskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
						
						if(currentTaskId != null && currentTaskId > 0) {
							taskPerson = workflow4BaseService.findParticipantsByTaskId(currentTaskId);
						}
					}
					
					if(taskPerson != null) {
						for(Map<String, Object> mapTemp : taskPerson){
							if(CommonFunctions.isNotBlank(mapTemp, "USER_NAME")){
								handlePerson.append(mapTemp.get("USER_NAME"));
							}
							
							if(CommonFunctions.isNotBlank(mapTemp, "ORG_NAME")) {
								handlePerson.append("(").append(mapTemp.get("ORG_NAME")).append(")").append(";");
							}
						}
					}
					
					curNodeMap.putAll(curTaskData);
					curNodeMap.put("TASK_ID", currentTaskId);
					curNodeMap.put("TASK_NAME", curTaskData.get("WF_ACTIVITY_NAME_"));
					curNodeMap.put("HANDLE_PERSON", handlePerson.toString());
					curNodeMap.put("IS_CURRENT_TASK", true);//?????????????????????????????????
					curNodeMap.put("INSTANCE_ID", instanceId);
					if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
						curNodeMap.put("TASK_CODE", curTaskData.get("NODE_NAME"));
					}
					if(CommonFunctions.isNotBlank(curTaskData, "ISTIMEOUT")) {
						curNodeMap.put("ISTIMEOUT", curTaskData.get("ISTIMEOUT"));
					}
					
					taskMapList.add(0, curNodeMap);//????????????????????????
				}
			}
			
			for(Map<String, Object> taskMap : taskMapList) {
				taskName = null;
				
				if(CommonFunctions.isNotBlank(taskMap, "TASK_CODE")) {
					taskName = taskMap.get("TASK_CODE").toString();
				}
				
				if(parentProTaskId != null) {
					taskMap.put("PARENT_PRO_TASK_ID", parentProTaskId);
				}
				
				//?????????????????????????????????????????????
				if(isAble2ShowSubPro(formTypeIdStr, taskName, listType, taskMap)) {
					proMap.put("instanceTaskId", taskMap.get("TASK_ID"));
					proMapList = baseWorkflowService.findProInstanceList(proMap);
					
					if(proMapList != null) {
						taskMap.put("SUB_PRO_COUNT", proMapList.size());
						
						for(Map<String, Object> proMapTmp : proMapList) {
							if(CommonFunctions.isNotBlank(proMapTmp, "INSTANCE_ID")) {
								params.put("PARENT_PRO_TASK_ID", taskMap.get("TASK_ID"));
								subProTaskMapList = capHandledTaskInfoMap(Long.valueOf(proMapTmp.get("INSTANCE_ID").toString()), order, params);
								if(subProTaskMapList != null) {
									resultMapList.addAll(subProTaskMapList);
								}
							}
						}
					}
				}
				//???????????????
				List<Map<String, Object>> subAndReceivedTaskList = hessianFlowService.queryTaskDetails(String.valueOf(taskMap.get("TASK_ID")), order),
						timeAndRemarkList = new ArrayList<Map<String, Object>>();
				Map<String, Object> timeAndRemarkMap = new HashMap<String, Object>();
				for(Map<String, Object> subAndReceivedTaskMap : subAndReceivedTaskList) {
					timeAndRemarkList = new ArrayList<>();
					timeAndRemarkMap = new HashMap<String, Object>();

					if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "START_TIME")) {
						timeAndRemarkMap.put("START_TIME", subAndReceivedTaskMap.get("START_TIME"));
					}
					if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "END_TIME")) {
						timeAndRemarkMap.put("END_TIME", subAndReceivedTaskMap.get("END_TIME"));
					}
					if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "RECEIVE_TIME")) {
						timeAndRemarkMap.put("RECEIVE_TIME", subAndReceivedTaskMap.get("RECEIVE_TIME"));
					}
					if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "REMARKS")) {
						timeAndRemarkMap.put("REMARKS", subAndReceivedTaskMap.get("REMARKS"));
					}
					timeAndRemarkList.add(timeAndRemarkMap);
					subAndReceivedTaskMap.put("timeAndRemarkList",timeAndRemarkList);
				}
				taskMap.put("subAndReceivedTaskList",subAndReceivedTaskList);


				if(CommonFunctions.isNotBlank(taskMap, "TASK_ID") 
						&& CommonFunctions.isNotBlank(superviseMap, taskMap.get("TASK_ID").toString())) {
					taskMap.put("IS_SUPERVISED", true);
				}
				
				resultMapList.add(taskMap);
			}
			
			if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
					&& resultMapList != null && resultMapList.size() > 0
					&& IWorkflow4BaseService.SQL_ORDER_DESC.equals(order)) {
				Map<String, Object> taskMap = resultMapList.get(0);

				if(CommonFunctions.isBlank(taskMap, "IS_CURRENT_TASK")) {
					String taskNameZH = null;
					
					if(CommonFunctions.isNotBlank(taskMap, "TASK_NAME")) {
						taskNameZH = taskMap.get("TASK_NAME").toString();
					}
					
					if(!FocusReportNode350Enum.END_NODE_NAME.getTaskNodeNameZH().equals(taskNameZH)) {
						Map<String, Object> endTaskMap = new HashMap<String, Object>();
						
						endTaskMap.put("TASK_NAME", FocusReportNode350Enum.END_NODE_NAME.getTaskNodeNameZH());

						resultMapList.add(0, endTaskMap);
					}
				}
			}
		}
		
		return resultMapList;
	}
	
	@Override
	public Map<String, Object> capProInstance(Long reportId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		ProInstance proInstance = null;
		Map<String, Object> proMap = null;
		
		if(reportId == null || reportId < 0) {
			try {
				if(CommonFunctions.isNotBlank(params, "reportId")) {
					reportId = Long.valueOf(params.get("reportId").toString());
				} else if(CommonFunctions.isNotBlank(params, "formId")) {
					reportId = Long.valueOf(params.get("formId").toString());
				} else if(CommonFunctions.isNotBlank(params, "reportUUID")) {
					String reportUUID = params.get("reportUUID").toString();
					Map<String, Object> reportFocusMap = reportFocusService.findReportFocusByUUIDSimple(reportUUID, userInfo, params);
					
					if(CommonFunctions.isNotBlank(reportFocusMap, "reportId")) {
						reportId = Long.valueOf(reportFocusMap.get("reportId").toString());
					}
				}
			} catch(NumberFormatException e) {}
		}
		
		if(reportId != null && reportId > 0) {
			String wfTypeId = null,
				   workflowName = null;
			
			if(CommonFunctions.isNotBlank(params, "wfTypeId")) {
				wfTypeId = params.get("wfTypeId").toString();
			} else {
				wfTypeId = findWfTypeId(reportId, userInfo, params);
			}
			
			if(CommonFunctions.isNotBlank(params, "workflowName")) {
				workflowName = params.get("workflowName").toString();
			} else {
				workflowName = findWorkflowName(reportId, userInfo, params);
			}
			
			proInstance = baseWorkflowService.capProInstance(workflowName, wfTypeId, reportId, userInfo);
		}
		
		if(proInstance != null) {
			proMap = new HashMap<String, Object>();
			
			proMap.put("instanceId", proInstance.getInstanceId());
			proMap.put("curNodeName", proInstance.getCurNode());
			proMap.put("curOrgName", proInstance.getCurOrg());
			proMap.put("curUserName", proInstance.getCurUser());
			proMap.put("proName", proInstance.getProName());
			
			proMap.put("creatorId", proInstance.getUserId());
			proMap.put("creatorName", proInstance.getUserName());
			proMap.put("creatorOrgId", proInstance.getOrgId());
			proMap.put("creatorOrgName", proInstance.getOrgName());
			
			proMap.put("startTime", proInstance.getStartTime());
			proMap.put("endTime", proInstance.getEndTime());
			
			proMap.put("status", proInstance.getStatus());
		}
		
		return proMap;
	}

	/**
	 * ???????????????????????????
	 * @param instanceId	????????????id
	 * @param userInfo		????????????
	 * @param params		????????????
	 * 			instanceId	????????????id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> findNextTaskNodes(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> nextNodeMapList = null;
		String reportType = null;
		ProInstance proInstance = null;
		boolean isDecisionMaking = false;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if((instanceId == null || instanceId <= 0) && CommonFunctions.isNotBlank(params, "instanceId")) {
			instanceId = Long.valueOf(params.get("instanceId").toString());
		}
		
		if(instanceId == null || instanceId <= 0) {
			throw new IllegalArgumentException("???????????????????????????id???");
		}
		
		if(CommonFunctions.isNotBlank(params, "proInstance")) {
			proInstance = (ProInstance) params.get("proInstance");
		} else {
			proInstance = this.baseWorkflowService.findProByInstanceId(instanceId);
		}
		
		if(proInstance == null) {
			throw new IllegalArgumentException("????????????id???"+ instanceId +"???????????????????????????????????????");
		}
		
		if(CommonFunctions.isBlank(params, "proInstance")) {
			params.put("proInstance", proInstance);
		}
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("?????????????????????reportType??????");
		}
		
		if(CommonFunctions.isNotBlank(params, "isDecisionMaking")) {
			isDecisionMaking = Boolean.valueOf(params.get("isDecisionMaking").toString());
		}
		
		if(isDecisionMaking && CommonFunctions.isBlank(params, "decisionTaskName")) {
			//???????????????????????????
			ReportServiceAgent<IWorkflowDecisionMakingService<String>> reportServiceAgent = null;
			IWorkflowDecisionMakingService<String> reportWorkflowDecisionService = null;
			String nextNodeName = null;
			
			reportServiceAgent = new ReportServiceAgent<IWorkflowDecisionMakingService<String>>(reportType, ReportServiceAgent.serviceTypeEnum.reportNodeDecision.getServiceTypeIndex());
			
			reportWorkflowDecisionService = reportServiceAgent.capService();
			
			if(reportWorkflowDecisionService != null) {
				nextNodeName = reportWorkflowDecisionService.makeDecision(params);
				
				if(StringUtils.isNotBlank(nextNodeName)) {
					params.put("decisionTaskName", nextNodeName);
				}
			}
		}
		
		nextNodeMapList = workflow4BaseService.findNextTaskNodes4Base(null, null, null, userInfo, params);
		
		if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
			String curNodeName = proInstance.getCurNode(),
				   formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
			List<Map<String, Object>> removeNodeMapList = new ArrayList<Map<String, Object>>();
			
			if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				String nextNodeName = null;
				Map<String, String> nodeNameZHMap = new HashMap<String, String>(),
									removeNodeMap = new HashMap<String, String>();
				
				if(FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
					nodeNameZHMap.put(FocusReportNode350Enum.STREET_DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreVerifyStatusEnum.BEYOND_JURISDICTION.getVerifyStatusName());
					nodeNameZHMap.put(FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreVerifyStatusEnum.BELONG_TWO_VIO.getVerifyStatusName());
					nodeNameZHMap.put(FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName(), TwoVioPreVerifyStatusEnum.NOT_BELONG_TWO_VIO.getVerifyStatusName());
					//add 20211123 ?????? ????????? ??????
					nodeNameZHMap.put(FocusReportNode350Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName(),"?????????");
				} else if(FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode350Enum.STREET_FEEDBACK_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
					nodeNameZHMap.put(FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName(), "??????????????????");
				} else if(FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
					nodeNameZHMap.put(FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName(), "???????????????");
					nodeNameZHMap.put(FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName(), "???????????????");
					nodeNameZHMap.put(FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName(), "???????????????");
					nodeNameZHMap.put(FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName(), "???????????????");
				}
				
				if(FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
					String identificationResult = null;
					
					removeNodeMap.put(TwoVioPreIdentificationResultEnum.NOT_BELONG_TWO_VIO.getIdentificationResult(), FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName());
					removeNodeMap.put(TwoVioPreIdentificationResultEnum.COMMUNITY_CLOSE.getIdentificationResult(), FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName());
					removeNodeMap.put(TwoVioPreIdentificationResultEnum.STREET_CLOSE.getIdentificationResult(), FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName());
					removeNodeMap.put(TwoVioPreIdentificationResultEnum.MUNICIPAL_DEPARTMENT_CLOSE.getIdentificationResult(), FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName());
					
					if(CommonFunctions.isNotBlank(params, "identificationResult")) {
						identificationResult = params.get("identificationResult").toString();
					} else if(CommonFunctions.isNotBlank(params, "reportFocusMap")) {
						Map<String, Object> reportFocusMap = (Map<String, Object>) params.get("reportFocusMap");
						
						if(CommonFunctions.isNotBlank(reportFocusMap, "identificationResult")) {
							identificationResult = reportFocusMap.get("identificationResult").toString();
						}
					}
					
					removeNodeMap.remove(identificationResult);
				} else if(FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
					String verifyStatus = null,
						   DEFAULT_VERIFY_STATUS = "0";
					
					if(CommonFunctions.isNotBlank(params, "verifyStatus")) {
						verifyStatus = params.get("verifyStatus").toString();
					} else if(CommonFunctions.isNotBlank(params, "reportFocusMap")) {
						Map<String, Object> reportFocusMap = (Map<String, Object>) params.get("reportFocusMap");
						
						if(CommonFunctions.isNotBlank(reportFocusMap, "verifyStatus")) {
							verifyStatus = reportFocusMap.get("verifyStatus").toString();
						}
					}
					
					//???????????????????????????????????????????????????????????????????????????
					if(StringUtils.isNotBlank(verifyStatus) && !DEFAULT_VERIFY_STATUS.equals(verifyStatus)) {
						removeNodeMap.put(TwoVioPreVerifyStatusEnum.BELONG_TWO_VIO.getVerifyStatus(), FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName());
						removeNodeMap.put(TwoVioPreVerifyStatusEnum.NOT_BELONG_TWO_VIO.getVerifyStatus(), FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName());
						removeNodeMap.put(TwoVioPreVerifyStatusEnum.BEYOND_JURISDICTION.getVerifyStatus(), FocusReportNode350Enum.STREET_DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName());
						
						removeNodeMap.remove(verifyStatus);
					}
				}

				//??????????????????????????? ??????????????????task0?????????
				if(FocusReportNode350Enum.STREET_FEEDBACK_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)){
					//???????????????????????????
					Map<String, Object> node = new HashMap<>();
					node.put("nodeId",-1);
					node.put("nodeName",ConstantValue.HANDLING_TASK_CODE);
					node.put("nodeNameZH","??????????????????");
					node.put("dynamicSelect",null);//????????????????????????????????????
					node.put("nodeType","1");
					node.put("transitionCode","__E0__");

					nextNodeMapList.add(node);
				}

				//??????????????????
				if(!nodeNameZHMap.isEmpty() || !removeNodeMap.isEmpty()) {
					for(Map<String, Object> nextNodeMap : nextNodeMapList) {
						nextNodeName = null;
						
						if(CommonFunctions.isNotBlank(nextNodeMap, "nodeName")) {
							nextNodeName = nextNodeMap.get("nodeName").toString();
						}
						
						if(removeNodeMap.containsValue(nextNodeName)) {
							removeNodeMapList.add(nextNodeMap);
						} else if(nodeNameZHMap.containsKey(nextNodeName)) {
							nextNodeMap.put("nodeNameZH", nodeNameZHMap.get(nextNodeName));
						}
					}
				}
			} else if(
					(FocusReportNode3500Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
							&& FocusReportNode3500Enum.HANDLE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName))
					|| (FocusReportNode3501Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
							&& FocusReportNode3501Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName))) {
				String nextNodeName = null;
				
				for(Map<String, Object> nextNodeMap : nextNodeMapList) {
					nextNodeName = null;
					
					if(CommonFunctions.isNotBlank(nextNodeMap, "nodeName")) {
						nextNodeName = nextNodeMap.get("nodeName").toString();
					}
					
					if(FocusReportNode3500Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
						removeNodeMapList.add(nextNodeMap);
						break;
					}
				}
			}
			
			nextNodeMapList.removeAll(removeNodeMapList);
		}
		
		return nextNodeMapList;
	}
	
	/**
	 * ?????????????????????
	 * @param reportId	??????id
	 * @param userInfo	????????????
	 * @param params	????????????
	 * @return
	 */
	protected String findWorkflowName(Long reportId, UserInfo userInfo, Map<String, Object> params) {
		String reportType = null, workflowName = null, formTypeId = null;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("?????????????????????reportType??????");
		}
		if(CommonFunctions.isNotBlank(params, "formTypeId")) {
			formTypeId = params.get("formTypeId").toString();
		}
		
		//?????????????????????
		if(ReportTypeEnum.twoVioPre.getReportTypeIndex().equals(reportType)) {
			workflowName = "????????????????????????";
			
			if(FocusReportNode3500Enum.FORM_TYPE_ID.toString().equals(formTypeId)) {
				workflowName = "????????????????????????_??????????????????";
			} else if(FocusReportNode3501Enum.FORM_TYPE_ID.toString().equals(formTypeId)) {
				workflowName = "????????????????????????_????????????????????????";
			} else if(FocusReportNode3502Enum.FORM_TYPE_ID.toString().equals(formTypeId)) {
				workflowName = "????????????????????????_??????????????????";
			}
		}
		
		return workflowName;
	}
	
	/**
	 * ??????????????????
	 * @param reportId	??????id
	 * @param userInfo	????????????
	 * @param params	????????????
	 * @return
	 */
	protected String findWfTypeId(Long reportId, UserInfo userInfo, Map<String, Object> params) {
		String reportType = null, wfTypeId = null;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("?????????????????????reportType??????");
		}
		
		//?????????????????????
		if(ReportTypeEnum.twoVioPre.getReportTypeIndex().equals(reportType)) {
			wfTypeId = "focus_report";
		}
		
		return wfTypeId;
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param formTypeIdStr	??????ID
	 * @param taskName		?????????????????????????????????
	 * @param listType		????????????
	 * @param params
	 * @return
	 */
	protected boolean isAble2ShowSubPro(String formTypeIdStr, String taskName, String listType, Map<String, Object> params) {
		String LIST_TYPE_TODO = "2";//????????????
		
		return CommonFunctions.isNotBlank(params, "TASK_ID")
				&& FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
				&& (!LIST_TYPE_TODO.equals(listType) 
				&& (FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(taskName)
						|| FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(taskName)
						|| FocusReportNode350Enum.ASSIGN_TASK_NODE_NAME.getTaskNodeName().equals(taskName)))
				|| (LIST_TYPE_TODO.equals(listType) && FocusReportNode350Enum.ASSIGN_TASK_NODE_NAME.getTaskNodeName().equals(taskName));
	}
	
	/**
	 * ?????????????????????????????????
	 * @param parentInstanceId	????????????id
	 * @param excludeSubInstanceId	?????????id???????????????????????????????????????
	 */
	protected void endAllSubProInstance(Long parentInstanceId, String formTypeIdStr,Long excludeSubInstanceId) {
		List<Map<String, Object>> instanceMapList = null;
		Map<String, Object> instanceParams = new HashMap<String, Object>();
		
		instanceParams.put("parentInstanceId", parentInstanceId);
		instanceParams.put("instanceStatus", HANDLING_PRO_STATUS);
		
		if(StringUtils.isNotBlank(formTypeIdStr)) {
			instanceParams.put("formTypeId", formTypeIdStr);
		}
		
		instanceMapList = baseWorkflowService.findProInstanceList(instanceParams);
		
		if(instanceMapList != null) {
			Long instanceId = null,formId = null;
			Map<String, Object> curTaskMap = null;
			UserInfo operateUserInfo = null;
			boolean subFlowEndResult = false;

			for(Map<String, Object> instanceMap : instanceMapList) {
				instanceId = null;
				curTaskMap = null;
				
				try {
					if(CommonFunctions.isNotBlank(instanceMap, "INSTANCE_ID")) {
						instanceId = Long.valueOf(instanceMap.get("INSTANCE_ID").toString());
					}
					if(CommonFunctions.isNotBlank(instanceMap, "FORM_ID")) {
						formId = Long.valueOf(instanceMap.get("FORM_ID").toString());
					}
					
					if(instanceId != null && instanceId > 0 && !instanceId.equals(excludeSubInstanceId)) {
						curTaskMap = workflow4BaseService.findCurTaskData4Base(instanceId);
						operateUserInfo = new UserInfo();
						
						if(CommonFunctions.isNotBlank(curTaskMap, "WF_USERID_ALL")) {
							operateUserInfo.setUserId(Long.valueOf(curTaskMap.get("WF_USERID_ALL").toString().split(",")[0]));
						}
						if(CommonFunctions.isNotBlank(curTaskMap, "WF_USERNAME_ALL")) {
							operateUserInfo.setPartyName(curTaskMap.get("WF_USERNAME_ALL").toString().split(",")[0]);
						}
						if(CommonFunctions.isNotBlank(curTaskMap, "WF_ORGID_ALL")) {
							operateUserInfo.setOrgId(Long.valueOf(curTaskMap.get("WF_ORGID_ALL").toString().split(",")[0]));
						}
						if(CommonFunctions.isNotBlank(curTaskMap, "WF_ORGNAME_ALL")) {
							operateUserInfo.setOrgName(curTaskMap.get("WF_ORGNAME_ALL").toString().split(",")[0]);
						}
					}
					if(!instanceId.equals(excludeSubInstanceId)){
						subFlowEndResult = baseWorkflowService.endWorkflow4Base(instanceId, operateUserInfo, null);
					}

					//????????????????????? ??????mq??????
					if(subFlowEndResult && Boolean.valueOf(RMQ_NA_REPORTFOCUS_OPEN)){
						Map<String,Object> mqParams=new HashMap<String,Object>();
						//?????????
						mqParams.put("eventFlag","endSub");

						Map<String,Object> afterElement=new HashMap<String,Object>();
						afterElement.put("eventId", formId);
						afterElement.put("formTypeId", formTypeIdStr);
						afterElement.put("parentInstanceId", parentInstanceId);
						afterElement.put("instanceId", instanceId);
						mqParams.put("afterElement", afterElement);
						try {
							sendFlowRmqMsg(mqParams);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch(Exception e) {}
			}
		}
	}
	
	/**
	 * ????????????????????????????????????
	 * @param reportId		????????????id
	 * @param userInfo		??????????????????
	 * @param extraParam	????????????
	 * @return ??????????????????true???????????????false
	 * @throws Exception
	 */
	protected boolean back2Draft(Long reportId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false, isBack2Draft = true;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(CommonFunctions.isNotBlank(extraParam, "isBack2Draft")) {
			isBack2Draft = Boolean.valueOf(extraParam.get("isBack2Draft").toString());
		}
		
		if(reportId == null || reportId <= 0) {
			if(CommonFunctions.isNotBlank(extraParam, "reportId")) {
				reportId = Long.valueOf(extraParam.get("reportId").toString());
			} else if(CommonFunctions.isNotBlank(extraParam, "formId")) {
				reportId = Long.valueOf(extraParam.get("formId").toString());
			} else if(CommonFunctions.isNotBlank(extraParam, "proInstance")) {
				ProInstance proInstance = (ProInstance) extraParam.get("proInstance");
				
				reportId = proInstance.getFormId();
			} else if(CommonFunctions.isNotBlank(extraParam, "instanceId")) {
				Long instanceId = Long.valueOf(extraParam.get("instanceId").toString());
				ProInstance proInstance = null;
				
				if(instanceId != null && instanceId > 0) {
					proInstance = this.baseWorkflowService.findProByInstanceId(instanceId);
					
					if(proInstance != null) {
						reportId = proInstance.getFormId();
					}
				}
			}
		}
		
		if(isBack2Draft && reportId != null && reportId > 0) {
			Map<String, Object> reportFocus = new HashMap<String, Object>();
			String reportType = ReportTypeEnum.twoVioPre.getReportTypeIndex(),
				   VALID_STATUS = "1";
			
			if(CommonFunctions.isNotBlank(extraParam, "reportType")) {
				reportType = extraParam.get("reportType").toString();
			}
			
			reportFocus.put("reportId", reportId);
			reportFocus.put("reportStatus", TwoVioPreReportStatusEnum.DRAFT_STATUS.getReportStatus());
			reportFocus.put("reportValid", VALID_STATUS);
			reportFocus.put("reportType", reportType);
			reportFocus.put("isSaveReportFocusExtend", false);
			
			flag = reportFocusService.saveReportFocus(reportFocus, userInfo) > 0;
			
			//????????????
			extraParam.put("isRestoreRportStatus", false);
		}
		
		return flag;
	}
	
	/**
	 * ??????????????????
	 * @param reportId
	 * @param instanceId
	 * @param userInfo
	 * @param params
	 * @throws Exception
	 */
	private void updateReportStatus(Long reportId, Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		String reportType = null;
		ReportServiceAgent<IWorkflowDecisionMakingService<String>> reportServiceAgent = null;
		IWorkflowDecisionMakingService<String> reportStatusDecisionMakingService = null;
		
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isBlank(params, "reportId") && reportId != null && reportId > 0) {
			params.put("reportId", reportId);
		}
		if(CommonFunctions.isBlank(params, "instanceId") && instanceId != null && instanceId > 0) {
			params.put("instanceId", instanceId);
		}
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		}
		params.put("operateUserInfo", userInfo);
		
		reportServiceAgent = new ReportServiceAgent<IWorkflowDecisionMakingService<String>>(reportType, ReportServiceAgent.serviceTypeEnum.reportStatusDecison.getServiceTypeIndex());
		
		reportStatusDecisionMakingService = reportServiceAgent.capService();
		
		reportStatusDecisionMakingService.makeDecision(params);
	}
	
	/**
	 * ??????????????????id???????????????????????????????????????
	 * @param instanceId	????????????id
	 * @return
	 * 		taskId : {
	 * 			msgCCSetMapList				????????????????????????
	 * 			msgCCSetDistributeMapLisst	??????????????????????????????
	 * 			msgCCSetSelectMapList		??????????????????????????????
	 * 			distributeUserNames			??????????????????
	 * 			distributeOrgNames			????????????????????????
	 * 			distributeUserOrgNames		??????????????????????????????
	 * 			selectUserNames				??????????????????
	 * 			selectOrgNames				??????????????????
	 * 			selectUserOrgNames			????????????????????????
	 * 		}
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Map<String, Object>> findMsgCCSetByInstanceId(Long instanceId) {
		List<Map<String, Object>> msgCCSetInfoList = reportMsgCCSetService.findMsgCCSetByInstanceId(instanceId, null);
		Map<String, Map<String, Object>> resultMap = null;
		
		if(msgCCSetInfoList != null) {
			String taskId = null, ccType = null, userName = null, orgName = null;
			Map<String, Object> msgCCSetInfo = null;
			StringBuffer distributeUserNames = null, distributeOrgNames = null, distributeUserOrgNames = null,
						 selectUserNames = null, selectOrgNames = null, selectUserOrgNames = null;
			List<Map<String, Object>> msgCCSetMapList = null, msgCCSetDistributeMapList = null, msgCCSetSelectMapList = null;
			
			resultMap = new HashMap<String, Map<String, Object>>();
			
			for(Map<String, Object> msgCCSetMap : msgCCSetInfoList) {
				taskId = null;
				userName = null;
				orgName = null;
				
				if(CommonFunctions.isNotBlank(msgCCSetMap, "taskId")) {
					taskId = msgCCSetMap.get("taskId").toString();
				}
				if(CommonFunctions.isNotBlank(msgCCSetMap, "ccType")) {
					ccType = msgCCSetMap.get("ccType").toString();
				}
				if(CommonFunctions.isNotBlank(msgCCSetMap, "userName")) {
					userName = msgCCSetMap.get("userName").toString();
				}
				if(CommonFunctions.isNotBlank(msgCCSetMap, "orgName")) {
					orgName = msgCCSetMap.get("orgName").toString();
				}
				
				if(resultMap.containsKey(taskId)) {
					msgCCSetInfo = resultMap.get(taskId);
					
					msgCCSetMapList = (List<Map<String, Object>>) msgCCSetInfo.get("msgCCSetMapList");
					msgCCSetDistributeMapList = (List<Map<String, Object>>) msgCCSetInfo.get("msgCCSetDistributeMapList");
					msgCCSetSelectMapList = (List<Map<String, Object>>) msgCCSetInfo.get("msgCCSetSelectMapList");
					
					distributeUserNames = (StringBuffer) msgCCSetInfo.get("distributeUserNames");
					distributeOrgNames = (StringBuffer) msgCCSetInfo.get("distributeOrgNames");
					distributeUserOrgNames = (StringBuffer) msgCCSetInfo.get("distributeUserOrgNames");
					
					selectUserNames = (StringBuffer) msgCCSetInfo.get("selectUserNames");
					selectOrgNames = (StringBuffer) msgCCSetInfo.get("selectOrgNames");
					selectUserOrgNames = (StringBuffer) msgCCSetInfo.get("selectUserOrgNames");
				} else {
					msgCCSetInfo = new HashMap<String, Object>();
					msgCCSetMapList = new ArrayList<Map<String, Object>>();
					msgCCSetDistributeMapList = new ArrayList<Map<String, Object>>();
					msgCCSetSelectMapList = new ArrayList<Map<String, Object>>();
					distributeUserNames = new StringBuffer("");
					distributeOrgNames = new StringBuffer("");
					distributeUserOrgNames = new StringBuffer("");
					selectUserNames = new StringBuffer("");
					selectOrgNames = new StringBuffer("");
					selectUserOrgNames = new StringBuffer("");
					
					msgCCSetInfo.put("msgCCSetMap", msgCCSetMap);
				}
				
				msgCCSetMapList.add(msgCCSetMap);
				
				if(ReportMsgCCTypeEnum.CCTYPE_DISTRIBUTE.getCcType().equals(ccType)) {
					msgCCSetDistributeMapList.add(msgCCSetMap);
					
					if(StringUtils.isNotBlank(userName) || StringUtils.isNotBlank(orgName)) {
						if(distributeUserOrgNames.length() > 0) {
							distributeUserOrgNames.append(",");
						}
					}
					
					if(StringUtils.isNotBlank(userName)) {
						if(distributeUserNames.length() > 0) {
							distributeUserNames.append(",");
						}
						
						distributeUserNames.append(userName);
						distributeUserOrgNames.append(userName);
					}
					
					if(StringUtils.isNotBlank(orgName)) {
						if(distributeOrgNames.length() > 0) {
							distributeOrgNames.append(",");
						}
						
						distributeOrgNames.append(orgName);
						distributeUserOrgNames.append("(").append(orgName).append(")");
					}
				} else if(ReportMsgCCTypeEnum.CCTYPE_SELECT.getCcType().equals(ccType)) {
					msgCCSetSelectMapList.add(msgCCSetMap);
					
					if(StringUtils.isNotBlank(userName) || StringUtils.isNotBlank(orgName)) {
						if(selectUserOrgNames.length() > 0) {
							selectUserOrgNames.append(",");
						}
					}
					
					if(StringUtils.isNotBlank(userName)) {
						if(selectUserNames.length() > 0) {
							selectUserNames.append(",");
						}
						
						selectUserNames.append(userName);
						selectUserOrgNames.append(userName);
					}
					
					if(StringUtils.isNotBlank(orgName)) {
						if(selectOrgNames.length() > 0) {
							selectOrgNames.append(",");
						}
						
						selectOrgNames.append(orgName);
						selectUserOrgNames.append("(").append(orgName).append(")");
					}
				}
				
				msgCCSetInfo.put("msgCCSetMapList", msgCCSetMapList);
				msgCCSetInfo.put("msgCCSetDistributeMapList", msgCCSetDistributeMapList);
				msgCCSetInfo.put("msgCCSetSelectMapList", msgCCSetSelectMapList);
				msgCCSetInfo.put("distributeUserNames", distributeUserNames);
				msgCCSetInfo.put("distributeOrgNames", distributeOrgNames);
				msgCCSetInfo.put("distributeUserOrgNames", distributeUserOrgNames);
				msgCCSetInfo.put("selectUserNames", selectUserNames);
				msgCCSetInfo.put("selectOrgNames", selectOrgNames);
				msgCCSetInfo.put("selectUserOrgNames", selectUserOrgNames);
				
				resultMap.put(taskId, msgCCSetInfo);
			}
		}
		
		return resultMap;
	}
	
	/**
	 * ??????????????????????????????
	 * @param userInfo		????????????
	 * @param params		
	 * 			proInstance			????????????
	 * 			instanceId			????????????id
	 * 			targetNextNodeName	????????????????????????????????????????????????????????????????????????????????????????????????????????????
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "serial" })
	private boolean subWorkflow2AppointedNextNode(UserInfo userInfo, Map<String, Object> params) throws Exception {
		Long instanceId = null;
		ProInstance proInstance = null;
		boolean flag = true;
		String targetNextNodeName = null;
		
		if(CommonFunctions.isNotBlank(params, "targetNextNodeName")) {
			targetNextNodeName = params.get("targetNextNodeName").toString();
		} else {
			return flag;
		}
		
		if(CommonFunctions.isNotBlank(params, "proInstance")) {
			proInstance = (ProInstance) params.get("proInstance");
		} else if(CommonFunctions.isNotBlank(params, "instanceId")) {
			instanceId = Long.valueOf(params.get("instanceId").toString());
			
			if(instanceId > 0) {
				proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			}
		}
		
		if(proInstance == null) {
			throw new ZhsqEventException("????????????????????????????????????");
		}
		
		String nextNodeName = null, curNodeName = null;
		List<Map<String, Object>> nextTaskMapList = null;
		Map<String, Object> nextTaskMap = null;
		
		if(StringUtils.isNotBlank(targetNextNodeName) && instanceId != null && instanceId > 0) {
			Map<String, Object> curDataMap = baseWorkflowService.findCurTaskData(instanceId);
			
			nextTaskMapList = findNextTaskNodes(instanceId, userInfo, params);
			
			if(CommonFunctions.isNotBlank(curDataMap, "NODE_NAME")) {
				curNodeName = curDataMap.get("NODE_NAME").toString();
			}
		}
		
		if(nextTaskMapList != null) {
			for(Map<String, Object> nextTask : nextTaskMapList) {
				if(CommonFunctions.isNotBlank(nextTask, "nodeName")) {
					nextNodeName = nextTask.get("nodeName").toString();
				}
				
				if(targetNextNodeName.equals(nextNodeName)) {
					nextTaskMap = nextTask;
					break;
				}
				
				nextNodeName = null;
			}
		}
		
		if(StringUtils.isNotBlank(targetNextNodeName) && StringUtils.isBlank(nextNodeName)) {
			throw new ZhsqEventException("??????????????????????????????" + targetNextNodeName + "??????");
		}
		
		if(nextTaskMap != null && !nextTaskMap.isEmpty()) {
			String nodeId = null, nodeCode = null,
				   formType = null,
				   formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
			Map<String, Object> fiveKeyMap = null, subParam = new HashMap<String, Object>();
			boolean isUserRadioStyle = false;
			Long reportId = proInstance.getFormId();
			Map<String, String> FORM_TYPE_MAP = new HashMap<String, String>() {
				{
					put(FocusReportNode352Enum.FORM_TYPE_ID.toString(), FocusReportNode352Enum.FORM_TYPE.toString());
					put(FocusReportNode35201Enum.FORM_TYPE_ID.toString(), FocusReportNode352Enum.FORM_TYPE.toString());
				}
			};
			
			if(CommonFunctions.isNotBlank(params, "formType")) {
				formType = params.get("formType").toString();
			} else {
				formType = FORM_TYPE_MAP.get(formTypeIdStr);
			}
			
			if(StringUtils.isBlank(formType)) {
				throw new ZhsqEventException("?????????????????????????????????");
			}
			
			if(CommonFunctions.isNotBlank(nextTaskMap, "nodeId")) {
				nodeId = nextTaskMap.get("nodeId").toString();
			}
			
			if(CommonFunctions.isNotBlank(nextTaskMap, "transitionCode")) {
				nodeCode = nextTaskMap.get("transitionCode").toString();
			}
			
			subParam.putAll(params);
			subParam.put("formType", formType);
			subParam.put("nodeCode", nodeCode);
			subParam.put("nodeId", nodeId);
			subParam.put("formId", reportId);
			subParam.put("reportId", reportId);
			subParam.put("formTypeId", formTypeIdStr);
			
			fiveKeyMap = fiveKeyElementService.getNodeInfoForEvent(userInfo, curNodeName, nextNodeName, nodeCode, nodeId, subParam);
			
			if(CommonFunctions.isNotBlank(fiveKeyMap, "isUserRadioStyle")) {
				isUserRadioStyle = Boolean.valueOf(fiveKeyMap.get("isUserRadioStyle").toString());
			}
			
			if(isUserRadioStyle) {
				boolean isCapDistributeUser = true, isCapSelectUser = false;
				
				if(CommonFunctions.isNotBlank(params, "isCapDistributeUser")) {
					isCapDistributeUser = Boolean.valueOf(params.get("isCapDistributeUser").toString());
				}
				
				if(CommonFunctions.isNotBlank(params, "isCapSelectUser")) {
					isCapSelectUser = Boolean.valueOf(params.get("isCapSelectUser").toString());
				}
				
				if(CommonFunctions.isNotBlank(fiveKeyMap, "userIds")) {
					subParam.put("nextUserIds", fiveKeyMap.get("userIds"));
				}
				
				if(CommonFunctions.isNotBlank(fiveKeyMap, "orgIds")) {
					subParam.put("nextOrgIds", fiveKeyMap.get("orgIds"));
				}
				
				if(isCapDistributeUser && CommonFunctions.isNotBlank(fiveKeyMap, "distributeUser")) {
					Map<String, Object> distributeUser = (Map<String, Object>) fiveKeyMap.get("distributeUser");
					boolean isDisplayUser = false;
					
					if(CommonFunctions.isNotBlank(distributeUser, "isDisplayUser")) {
						isDisplayUser = Boolean.valueOf(distributeUser.get("isDisplayUser").toString());
					}
					
					if(isDisplayUser) {
						if(CommonFunctions.isNotBlank(distributeUser, "userIds")) {
							subParam.put("distributeUserIds", distributeUser.get("userIds"));
						}
						
						if(CommonFunctions.isNotBlank(distributeUser, "orgIds")) {
							subParam.put("distributeOrgIds", distributeUser.get("orgIds"));
						}
					}
				}
				
				if(isCapSelectUser && CommonFunctions.isNotBlank(fiveKeyMap, "selectUser")) {
					Map<String, Object> selectUser = (Map<String, Object>) fiveKeyMap.get("selectUser");
					boolean isSelectUser = false;
					
					if(CommonFunctions.isNotBlank(selectUser, "isSelectUser")) {
						isSelectUser = Boolean.valueOf(selectUser.get("isSelectUser").toString());
					}
					
					if(isSelectUser) {
						if(CommonFunctions.isNotBlank(selectUser, "userIds")) {
							subParam.put("selectUserIds", selectUser.get("userIds"));
						}
						
						if(CommonFunctions.isNotBlank(selectUser, "orgIds")) {
							subParam.put("selectOrgIds", selectUser.get("orgIds"));
						}
					}
				}
				
				if(CommonFunctions.isNotBlank(fiveKeyMap, "adviceNote")) {
					subParam.put("advice", fiveKeyMap.get("adviceNote"));
				}
				
				flag = reportIntegrationService.subWorkflow4Report(instanceId, nextNodeName, null, userInfo, subParam);
			}
		}
		
		return flag;
	}

	protected void sendSubFlowRMQmMsg(Long reportId,String parentFormTypeId,String formTypeId,Long parentInstanceId,Object instanceId){
		if (Boolean.valueOf(RMQ_NA_REPORTFOCUS_OPEN)) {
			Map<String,Object> mqParams=new HashMap<String,Object>();
			//?????????
			mqParams.put("eventFlag","startSub");

			Map<String,Object> afterElement=new HashMap<String,Object>();
			afterElement.put("eventId", reportId);
			afterElement.put("formTypeId", formTypeId);
			afterElement.put("instanceId", instanceId);
			afterElement.put("parentInstanceId", parentInstanceId);
			mqParams.put("afterElement", afterElement);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private String transAdviceContent(String advice,UserInfo userInfo,Map<String, Object> params){
		if(StringUtils.isNotBlank(advice)){
			StringBuffer dynamicContent = new StringBuffer();
			String reportType = null,moduleName = null;
			Map<String,Object> curDataMap = new HashMap<>();

			if(CommonFunctions.isNotBlank(params,"curDataMap")){
				curDataMap = (Map<String, Object>) params.get("curDataMap");
			}else if(CommonFunctions.isNotBlank(params,"instanceId")){
				try {
					curDataMap = workflow4BaseService.findCurTaskData4Base(Long.valueOf(String.valueOf(params.get("instanceId"))));
				} catch (Exception e) {

				}
			}

			if(CommonFunctions.isNotBlank(params,"reportType")){
				reportType = String.valueOf(params.get("reportType"));

				moduleName = ReportTypeEnum.getReportTypeName(reportType);

				if(StringUtils.isBlank(moduleName) && CommonFunctions.isNotBlank(params,"workflowName")){
					moduleName = String.valueOf(params.get("workflowName"));
				}
			}

			dynamicContent.append("\r\n").append("???");

			if(userInfo != null){
				dynamicContent.append("????????????").append(userInfo.getPartyName()).append("(").append(userInfo.getOrgName()).append("),");
			}
			if(CommonFunctions.isNotBlank(curDataMap,"DUEDATE_")){
				Date dueDate = (Date)curDataMap.get("DUEDATE_");

				String dueDateStr = DateUtils.formatDate(dueDate,DateUtils.PATTERN_24TIME);

				dynamicContent.append("???????????????").append(dueDateStr);
			}

			dynamicContent.append("???");

			if(StringUtils.isNotBlank(moduleName)){
				dynamicContent.append("???????????????").append(moduleName);
			}

			advice = String.valueOf(dynamicContent.insert(0,advice));

		}
		return advice;
	}
	/**
	 * ??????????????????
	 * @param smsContent		????????????
	 * @param operateUserInfo	??????????????????
	 * @param userInfoList		????????????????????????
	 * @return
	 * 		????????????????????????true???????????????false
	 */
	private boolean sendSMS(String smsContent, UserInfo operateUserInfo, List<UserInfo> userInfoList) {
		boolean flag = false;

		if(StringUtils.isNotBlank(smsContent)) {
			Set<String> mobilePhoneSet = null;

			if(userInfoList != null) {
				mobilePhoneSet = new HashSet<String>();

				for(UserInfo userInfo : userInfoList) {
					if(StringUtils.isNotBlank(userInfo.getVerifyMobile())){
						mobilePhoneSet.add(userInfo.getVerifyMobile());
					}else {
						UserBO userBO = userManageOutService.getUserInfoByUserId(userInfo.getUserId());
						if(null != userBO){
							mobilePhoneSet.add(String.valueOf(userBO.getVerifyMobile()));
						}
					}
				}
			}

			if(mobilePhoneSet != null) {
				String[] mobilePhones = mobilePhoneSet.toArray(new String[mobilePhoneSet.size()]);

				try {
					SendResult result = sendSmsService.sendSms(ConstantValue.SMS_PLATFORM_FLAG,
							operateUserInfo.getUserId(), operateUserInfo.getOrgCode(),
							mobilePhones, smsContent, SendSmsService.TYPE_SMS, null,
							null);
					flag = result != null && result.getCode() == 0;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

		return flag;
	}
}
