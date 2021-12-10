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
 * 两违防治工作流相关接口
 * @Description: 
 * @ClassName:   ReportFocusWorkflow4TwoVioPreServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年9月14日 上午9:19:44
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
	
	//可移除的环节编码 流程开始
	private final String[] REMOVE_NODE_CODE = {"KS"};
	private final String HANDLING_PRO_STATUS = "1";	//流程状态——处理中
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean startWorkflow4Report(Long reportId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String workflowName = this.findWorkflowName(reportId, userInfo, extraParam), 
			   wfTypeId = this.findWfTypeId(reportId, userInfo, extraParam);
		boolean flag = false;
		Map<String, Object> reportFocusMap = null;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(StringUtils.isBlank(workflowName)) {
			throw new IllegalArgumentException("没有找到配置的流程图名称！");
		}
		if(StringUtils.isBlank(wfTypeId)) {
			throw new IllegalArgumentException("没有找到配置的流程图类型！");
		}
		
		if(reportId == null || reportId <= 0) {
			//不添加转换异常捕获
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
				throw new ZhsqEventException("没有找到有效的两违信息！");
			}
			
			if(CommonFunctions.isNotBlank(reportFocusMap, "reportId")) {
				//不添加转换异常捕获
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
			throw new IllegalArgumentException("缺少有效的上报id！");
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
			throw new IllegalArgumentException("缺少可办理的下一环节！");
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
			//归档时，如果没有设置办理人员，则默认当前办理人员进行处理
			if(FocusReportNode350Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName) 
					&& userInfo != null) {
				nextUserInfoList = new ArrayList<UserInfo>();
				nextUserInfoList.add(userInfo);
			} else {
				throw new IllegalArgumentException("缺少下一环节的可办理人员！");
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
				throw new IllegalArgumentException("流程实例id【"+ instanceId +"】没有找到有效的流程信息！");
			}
		} else {
			throw new IllegalArgumentException("缺少有效的流程实例id！");
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
			//构造启动子流程用户信息
			String nodeCodeTmp = null;
			List<GdZTreeNode> treeNodeList = null;
			Node node = null;
			userInfoList = new ArrayList<UserInfo>();

			//获取第一副网格长(驻村工作队)，用于启动子流程“南安两违防治事件_跟踪两违状态”
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
			//启动子流程的同时主流程也向下流转
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

					//不是当前用户才可启动子流程
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
							subProParams.remove("instanceId");//若不移除，会影响子流程实例的生成
							
							if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel) && orgTypeUnit.equals(orgType)) {
								//启动“南安两违防治事件-跟踪两违状态”子流程
								subProParams.put("formTypeId", FocusReportNode3500Enum.FORM_TYPE_ID.toString());
								
								removeUserInfoList.add(nextUserInfo);
								
								subFlowResult = this.startWorkflow4Report(proInstance.getFormId(), nextUserInfo, subProParams);

								//子流程启动成功 发送mq消息
								if(subFlowResult){
									sendSubFlowRMQmMsg(reportId,FocusReportNode350Enum.FORM_TYPE_ID.toString(),FocusReportNode3500Enum.FORM_TYPE_ID.toString(),instanceId,subProParams.get("instanceId"));
								}
							}
						}
					}
				}
				//子流程启动用户发送短信
				if(subFlowResult && CommonFunctions.isNotBlank(extraParam,"advice")){
					String advice = null;
					advice = String.valueOf(extraParam.get("advice"));
					advice = this.transAdviceContent(advice,userInfo,subProParams);
					this.sendSMS(advice, userInfo, removeUserInfoList);
				}

				//移除子流程启动用户，剩余办理人员推送给主流程下一环节
				nextUserInfoList.removeAll(removeUserInfoList);
			}
		} else if(false && FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeId)
				&& FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
				&& FocusReportNode350Enum.ASSIGN_TASK_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
			//弃用
			//用于启动子流程“南安两违防治事件_报告整治进展情况”
			nextUserInfoList.add(userInfo);
			//启动子流程的同时主流程也向下流转
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
						subProParams.remove("instanceId");//若不移除，会影响子流程实例的生成
						
						if(ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel) && orgTypeUnit.equals(orgType)) {
							//启动“南安两违防治事件_报告整治进展情况”子流程
							subProParams.put("formTypeId", FocusReportNode3501Enum.FORM_TYPE_ID.toString());
							
							removeUserInfoList.add(nextUserInfo);
							
							boolean subFlowResult = this.startWorkflow4Report(proInstance.getFormId(), nextUserInfo, subProParams);

							if(subFlowResult){
								sendSubFlowRMQmMsg(reportId,formTypeId,FocusReportNode3501Enum.FORM_TYPE_ID.toString(),instanceId,subProParams.get("instanceId"));
							}
						}
					}
				}
				
				//移除子流程启动用户，剩余办理人员推送给主流程下一环节
				nextUserInfoList.removeAll(removeUserInfoList);
			}
		} else if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeId)
				&& FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
				&& FocusReportNode350Enum.STREET_FEEDBACK_TASK_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
			//“南安两违防治事件-交办部门查处”子流程
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
			//只有该部门不存在未完结的“南安两违防治事件-交办部门查处”子流程，才可以启动该子流程
			instanceParams = new HashMap<String, Object>();
			instanceParams.put("formTypeId", FocusReportNode3502Enum.FORM_TYPE_ID.toString());
			instanceParams.put("instanceTaskId", curTaskId);
			instanceParams.put("parentInstanceId", instanceId);
			instanceParams.put("instanceStatus", HANDLING_PRO_STATUS);
			instanceParams.put("isHandledUser", true);
			instanceParams.put("handledUserId", nextUserId);
			instanceParams.put("handledOrgId", nextOrgId);

			if(baseWorkflowService.findProInstanceCount(instanceParams) == 0) {
				//启动“南安两违防治事件-交办部门查处”子流程
				subProParams = new HashMap<String, Object>();

				subProParams.putAll(extraParam);
				subProParams.put("formTypeId", FocusReportNode3502Enum.FORM_TYPE_ID.toString());
				subProParams.put("parentId", instanceId);
				subProParams.put("taskId", curTaskId);
				subProParams.put("isConvertProinstance", false);
				subProParams.remove("instanceId");//若不移除，会影响子流程实例的生成

				//将主流程的下一环节的办理人 置为子流程的启动人
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
				//子流程启动成功 发送mq消息
				if(subFlowResult){
					sendSubFlowRMQmMsg(reportId,formTypeId,FocusReportNode3502Enum.FORM_TYPE_ID.toString(),instanceId,subProParams.get("instanceId"));
				}
			}
			//忽略子流程启动情况，直接返回成功
			flag = true;
			//主流程提交 下一办理人 变更为当前环节办理人
			extraParam.put("nextOrgIds",userInfo.getOrgId());
			extraParam.put("nextUserIds",userInfo.getUserId());

			if(nextUserInfoList != null && nextUserInfoList.size() > 0){
				nextUserInfoList.clear();
				nextUserInfoList.add(userInfo);
			}
		}else if(FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeId)
				&& FocusReportNode350Enum.STREET_FEEDBACK_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
				&& ConstantValue.HANDLING_TASK_CODE.equals(nextNodeName)){
			//当前环节为镇级反馈 下一环节为处理中(持续推动整治) 主流程不流转 还在当前办理人手上 增加处理中环节
			isSubByParent = false;

			String advice = null;
			if(CommonFunctions.isNotBlank(extraParam,"advice")){
				advice = String.valueOf(extraParam.get("advice"));
			}
			Map<String,Object> resultMap = eventDisposalWorkflowService.saveHandlingTask(reportId, instanceId, nextNodeName, advice, userInfo,extraParam);
			flag = resultMap != null;
			//更新当前环节办理期限 每提交一次 在当前日期+3工作日天
			//当前环节办理期限
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
				//判断子流程是否都完成
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
			//南安两违防治事件_跟踪两违状态 子流程可能存在多条 只要其中一条开始提交办理 其余的自动归档
			isSubByParent = false;
			if(CommonFunctions.isBlank(extraParam, "instanceId")) {
				extraParam.put("instanceId", instanceId);
			}

			flag = workflow4BaseService.subWorkflow4Base(null, null, null, nextNodeName, nextUserInfoList, userInfo, extraParam);

			//其中一条开始提交办理 其余的自动归档
			if(flag){
				//判断子流程是否都完成
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
				//主流程结束后，强制完结各子流程
				endAllSubProInstance(instanceId, null,null);
				
				Map<String, Object> updateStatusParams = new HashMap<String, Object>();
				updateStatusParams.putAll(extraParam);
				updateStatusParams.put("nextNodeName", FocusReportNode350Enum.END_NODE_NAME.getTaskNodeName());
				updateStatusParams.put("instanceId", instanceId);
				
				updateReportStatus(null, instanceId, userInfo, updateStatusParams);
			}
		}
		
		if(flag && CommonFunctions.isBlank(extraParam, "msgSendMidSendType")) {
			String TWO_VIO_PRE_SEND_TYPE = "twoVioPre";//两违防治消息发送类型
			
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
		
		//前后两次isRejectByParent判断不能合并，一个是用于判断是否需要使用驳回逻辑判断；一个是用于判断是否需要进行驳回操作
		if(isRejectByParent) {
			if(instanceId == null || instanceId < 0) {
				throw new IllegalArgumentException("缺少有效的流程实例id！");
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
					//判断子流程是否都完成
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
						throw new ZhsqEventException("当前环节尚有未完结的协同任务！");
					}
				}
			} else if(
					(FocusReportNode3500Enum.FORM_TYPE_ID.toString().equals(formTypeId)
							&& FocusReportNode3500Enum.DISASSEMBLE_NODE_NAME.getTaskNodeName().equals(curNodeName))
					|| (FocusReportNode3501Enum.FORM_TYPE_ID.toString().equals(formTypeId)
							&& FocusReportNode3501Enum.ASSIGN_TASK_NODE_NAME.toString().equals(curNodeName))
					|| (FocusReportNode3502Enum.FORM_TYPE_ID.toString().equals(formTypeId)
							&& FocusReportNode3502Enum.DEAL_WITH_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName))) {
				//子流程采集节点(task1)驳回操作时，废除子流程
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
							//将历史的“南安两违防治事件_跟踪两违状态”子流程均强制结束
							endAllSubProInstance(instanceId, FocusReportNode3500Enum.FORM_TYPE_ID.toString(),null);
						} else if(FocusReportNode350Enum.ASSIGN_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
							//将历史的“南安两违防治事件_报告整治进展情况”子流程均强制结束
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
			throw new IllegalArgumentException("缺少有效的流程实例id！");
		}
		
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		
//		if(CommonFunctions.isNotBlank(extraParam, "reportId")) {
//			reportId = Long.valueOf(extraParam.get("reportId").toString());
//		}
		
		if(CommonFunctions.isNotBlank(extraParam, "isAble2Recall")) {
			isAble2Recall = Boolean.valueOf(extraParam.get("isAble2Recall").toString());
		} else {
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			
			if(proInstance == null) {
				throw new IllegalArgumentException("实例id没有找到有效的实例信息！");
			}
			
			String formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
			
			isAble2Recall = FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr);
		}
		
		//只有主流程才可以进行撤回操作
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
					throw new ZhsqEventException("该记录无法进行撤回操作！");
				}
			}
		} else {
			throw new ZhsqEventException("该记录无法进行撤回操作！");
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
		boolean isAble2Handle = false,	//是否可办理案件
				isEditable = false,		//是否可待办编辑
				isEditableNode = false,	//是否为可编辑节点，目前主要用于所属地域层级是否需要选择到村二级的判断，true为需要选择到村二级
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
				throw new IllegalArgumentException("流程实例id【" + instanceId + "】没有找到有效的流程信息！");
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
				
				//是否可编辑事件基本信息（核实节点 task3可编辑）
				if(FocusReportNode350Enum.FORM_TYPE_ID.getTaskNodeName().equals(String.valueOf(proInstance.getFormTypeId()))
						&& FocusReportNode350Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
					isEditable = true;
					isEditableNode = true;
				}
				
				if(CommonFunctions.isNotBlank(curTaskData, "NODE_ID")) {
					nodeId = Integer.valueOf(curTaskData.get("NODE_ID").toString());

					//因现网出现nodeId < 0 情况，暂时去除判断条件 nodeId > 0
					if(nodeId != null /*&& nodeId > 0*/) {
						List<Map<String, Object>> operateMapList = workflow4BaseService.findOperateByNodeId(nodeId);
						String reportStatus = null;
						
						if(CommonFunctions.isNotBlank(params, "reportFocusMap")) {
							Map<String, Object> reportFocusMap = (Map<String, Object>) params.get("reportFocusMap");
							
							if(CommonFunctions.isNotBlank(reportFocusMap, "reportStatus")) {
								reportStatus = reportFocusMap.get("reportStatus").toString();
							}
						}
						
						//该方法是无法区分出主流程还是子流程的，因此满足如下条件的子流程也会出现待办可编辑、删除操作；
						//目前入格中，暂未出现在满足如下条件中，仍存在有效子流程的情况
						if(TwoVioPreReportStatusEnum.DRAFT_STATUS.getReportStatus().equals(reportStatus) && operateMapList != null) {
							Map<String, Object> operateMap = new HashMap<String, Object>();
							String DELETE_EVENT = "delRecord", DELETE_NAME = "删除";
							
							//该按钮尚未增添到工作流“操作管理”中，因为目前剔除的情况远多于包含的情况，且尚未推广
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
				//只有当下一可办理环节为一个时，默认选中
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
				
				//如果传入null，无法区分出是因为没有查找督办记录，还是查找出的督办记录为空
				superviseList = superviseList == null ? new ArrayList<Map<String, Object>>() : superviseList;
				
				params.put("superviseList", superviseList);
			}
			
			if(CommonFunctions.isNotBlank(params, "isCapTaskList")) {
				isCapTaskList = Boolean.valueOf(params.get("isCapTaskList").toString());
			}
			
			if(isCapTaskList) {
				initMap.put("taskList", capHandledTaskInfoMap(instanceId, IWorkflow4BaseService.SQL_ORDER_DESC, params));
			}

			//是否需要上传处理后图片
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
		String END_PRO_STATUS = "2",		//流程实例状态——结束
			   ABOLISH_PRO_STATUS = "4";	//流程实例状态——废除
			   //LIST_TYPE_TODO = "2";		//待办列表
		
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
			proMap.put("instanceStatus", HANDLING_PRO_STATUS + "," + END_PRO_STATUS);// 1 办理中；2 归档；
			
			msgCCSetResultMap = findMsgCCSetByInstanceId(instanceId);
			
			//构建可移除的环节
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
			
			//分两步移除，是为了避免抛出java.util.ConcurrentModificationException异常
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
					curNodeMap.put("IS_CURRENT_TASK", true);//用于判断是否是当前任务
					curNodeMap.put("INSTANCE_ID", instanceId);
					if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
						curNodeMap.put("TASK_CODE", curTaskData.get("NODE_NAME"));
					}
					if(CommonFunctions.isNotBlank(curTaskData, "ISTIMEOUT")) {
						curNodeMap.put("ISTIMEOUT", curTaskData.get("ISTIMEOUT"));
					}
					
					taskMapList.add(0, curNodeMap);//增添当前环节展示
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
				
				//只有非待办详情才获取子流程信息
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
				//处理中环节
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
	 * 获取下一可办理环节
	 * @param instanceId	流程实例id
	 * @param userInfo		用户信息
	 * @param params		额外参数
	 * 			instanceId	流程实例id
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
			throw new IllegalArgumentException("缺少有效的流程实例id！");
		}
		
		if(CommonFunctions.isNotBlank(params, "proInstance")) {
			proInstance = (ProInstance) params.get("proInstance");
		} else {
			proInstance = this.baseWorkflowService.findProByInstanceId(instanceId);
		}
		
		if(proInstance == null) {
			throw new IllegalArgumentException("流程实例id【"+ instanceId +"】没有找到有效的流程信息！");
		}
		
		if(CommonFunctions.isBlank(params, "proInstance")) {
			params.put("proInstance", proInstance);
		}
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		
		if(CommonFunctions.isNotBlank(params, "isDecisionMaking")) {
			isDecisionMaking = Boolean.valueOf(params.get("isDecisionMaking").toString());
		}
		
		if(isDecisionMaking && CommonFunctions.isBlank(params, "decisionTaskName")) {
			//获取决策后节点名称
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
					//add 20211123 增加 再核实 环节
					nodeNameZHMap.put(FocusReportNode350Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName(),"再核实");
				} else if(FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode350Enum.STREET_FEEDBACK_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
					nodeNameZHMap.put(FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName(), "任务申请办结");
				} else if(FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
					nodeNameZHMap.put(FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName(), "验收不通过");
					nodeNameZHMap.put(FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName(), "验收不通过");
					nodeNameZHMap.put(FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName(), "验收不通过");
					nodeNameZHMap.put(FocusReportNode350Enum.DEAL_WITH_DEPARTMENT_TASK_NODE_NAME.getTaskNodeName(), "验收不通过");
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
					
					//为了兼容历史数据，但也导致了无法滤除操作失败的情况
					if(StringUtils.isNotBlank(verifyStatus) && !DEFAULT_VERIFY_STATUS.equals(verifyStatus)) {
						removeNodeMap.put(TwoVioPreVerifyStatusEnum.BELONG_TWO_VIO.getVerifyStatus(), FocusReportNode350Enum.HANDLE_TWO_VIO_TASK_NODE_NAME.getTaskNodeName());
						removeNodeMap.put(TwoVioPreVerifyStatusEnum.NOT_BELONG_TWO_VIO.getVerifyStatus(), FocusReportNode350Enum.CHECK_TASK_NODE_NAME.getTaskNodeName());
						removeNodeMap.put(TwoVioPreVerifyStatusEnum.BEYOND_JURISDICTION.getVerifyStatus(), FocusReportNode350Enum.STREET_DISTRIBUTE_TASK_NODE_NAME.getTaskNodeName());
						
						removeNodeMap.remove(verifyStatus);
					}
				}

				//当前环节为镇级反馈 增加处理中（task0）环节
				if(FocusReportNode350Enum.STREET_FEEDBACK_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)){
					//构造处理中虚拟节点
					Map<String, Object> node = new HashMap<>();
					node.put("nodeId",-1);
					node.put("nodeName",ConstantValue.HANDLING_TASK_CODE);
					node.put("nodeNameZH","整治跟踪反馈");
					node.put("dynamicSelect",null);//提交时不需要选择办理人员
					node.put("nodeType","1");
					node.put("transitionCode","__E0__");

					nextNodeMapList.add(node);
				}

				//变更环节名称
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
	 * 获取流程图名称
	 * @param reportId	上报id
	 * @param userInfo	操作用户
	 * @param params	额外参数
	 * @return
	 */
	protected String findWorkflowName(Long reportId, UserInfo userInfo, Map<String, Object> params) {
		String reportType = null, workflowName = null, formTypeId = null;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		if(CommonFunctions.isNotBlank(params, "formTypeId")) {
			formTypeId = params.get("formTypeId").toString();
		}
		
		//后续转功能配置
		if(ReportTypeEnum.twoVioPre.getReportTypeIndex().equals(reportType)) {
			workflowName = "南安两违防治事件";
			
			if(FocusReportNode3500Enum.FORM_TYPE_ID.toString().equals(formTypeId)) {
				workflowName = "南安两违防治事件_跟踪两违状态";
			} else if(FocusReportNode3501Enum.FORM_TYPE_ID.toString().equals(formTypeId)) {
				workflowName = "南安两违防治事件_报告整治进展情况";
			} else if(FocusReportNode3502Enum.FORM_TYPE_ID.toString().equals(formTypeId)) {
				workflowName = "南安两违防治事件_交办部门查处";
			}
		}
		
		return workflowName;
	}
	
	/**
	 * 获取流程类型
	 * @param reportId	上报id
	 * @param userInfo	操作用户
	 * @param params	额外参数
	 * @return
	 */
	protected String findWfTypeId(Long reportId, UserInfo userInfo, Map<String, Object> params) {
		String reportType = null, wfTypeId = null;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		
		//后续转功能配置
		if(ReportTypeEnum.twoVioPre.getReportTypeIndex().equals(reportType)) {
			wfTypeId = "focus_report";
		}
		
		return wfTypeId;
	}
	
	/**
	 * 判断是否需要添加子流程信息
	 * @param formTypeIdStr	表单ID
	 * @param taskName		可添加子流程的节点名称
	 * @param listType		列表类型
	 * @param params
	 * @return
	 */
	protected boolean isAble2ShowSubPro(String formTypeIdStr, String taskName, String listType, Map<String, Object> params) {
		String LIST_TYPE_TODO = "2";//待办列表
		
		return CommonFunctions.isNotBlank(params, "TASK_ID")
				&& FocusReportNode350Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
				&& (!LIST_TYPE_TODO.equals(listType) 
				&& (FocusReportNode350Enum.IDENTIFY_TWO_VIO_TASK_NODE_NAME.getTaskNodeName().equals(taskName)
						|| FocusReportNode350Enum.STREET_DEAL_TASK_NODE_NAME.getTaskNodeName().equals(taskName)
						|| FocusReportNode350Enum.ASSIGN_TASK_NODE_NAME.getTaskNodeName().equals(taskName)))
				|| (LIST_TYPE_TODO.equals(listType) && FocusReportNode350Enum.ASSIGN_TASK_NODE_NAME.getTaskNodeName().equals(taskName));
	}
	
	/**
	 * 将所有子流程均强制归档
	 * @param parentInstanceId	父级实例id
	 * @param excludeSubInstanceId	子实例id（该子流程不进行强制归档）
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

					//子流程结束成功 发送mq消息
					if(subFlowEndResult && Boolean.valueOf(RMQ_NA_REPORTFOCUS_OPEN)){
						Map<String,Object> mqParams=new HashMap<String,Object>();
						//子流程
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
	 * 将上报记录状态设置为草稿
	 * @param reportId		上报记录id
	 * @param userInfo		操作用户信息
	 * @param extraParam	额外参数
	 * @return 更新成功返回true，否则返回false
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
			
			//为了防止
			extraParam.put("isRestoreRportStatus", false);
		}
		
		return flag;
	}
	
	/**
	 * 更新上报状态
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
	 * 依据流程实例id获取历史任务分送、选送人员
	 * @param instanceId	流程实例id
	 * @return
	 * 		taskId : {
	 * 			msgCCSetMapList				已配送的人员信息
	 * 			msgCCSetDistributeMapLisst	已配送的分送人员信息
	 * 			msgCCSetSelectMapList		已配送的选送人员信息
	 * 			distributeUserNames			分送人员姓名
	 * 			distributeOrgNames			分送人员组织名称
	 * 			distributeUserOrgNames		分送人员组织姓名信息
	 * 			selectUserNames				选送人员姓名
	 * 			selectOrgNames				选送组织姓名
	 * 			selectUserOrgNames			选送组织姓名信息
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
	 * 提交至指定的下一环节
	 * @param userInfo		操作用户
	 * @param params		
	 * 			proInstance			流程实例
	 * 			instanceId			流程实例id
	 * 			targetNextNodeName	下一环节名称，要求该环节在当前环节的可办理下一环节中，且该环节为人员办理
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
			throw new ZhsqEventException("缺少有效的流程实例信息！");
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
			throw new ZhsqEventException("无法提交至下一环节【" + targetNextNodeName + "】！");
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
				throw new ZhsqEventException("未找到有效的表单类型！");
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
			//子流程
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

			dynamicContent.append("\r\n").append("【");

			if(userInfo != null){
				dynamicContent.append("报告人：").append(userInfo.getPartyName()).append("(").append(userInfo.getOrgName()).append("),");
			}
			if(CommonFunctions.isNotBlank(curDataMap,"DUEDATE_")){
				Date dueDate = (Date)curDataMap.get("DUEDATE_");

				String dueDateStr = DateUtils.formatDate(dueDate,DateUtils.PATTERN_24TIME);

				dynamicContent.append("处置时限：").append(dueDateStr);
			}

			dynamicContent.append("】");

			if(StringUtils.isNotBlank(moduleName)){
				dynamicContent.append("场景应用：").append(moduleName);
			}

			advice = String.valueOf(dynamicContent.insert(0,advice));

		}
		return advice;
	}
	/**
	 * 发送短信信息
	 * @param smsContent		短信内容
	 * @param operateUserInfo	操作用户信息
	 * @param userInfoList		短信接收用户信息
	 * @return
	 * 		短信发送成功返回true；否则返回false
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
