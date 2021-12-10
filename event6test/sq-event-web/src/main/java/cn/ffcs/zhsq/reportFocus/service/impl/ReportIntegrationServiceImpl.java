package cn.ffcs.zhsq.reportFocus.service.impl;

import cn.ffcs.common.message.bo.MsgSendMid;
import cn.ffcs.common.message.bo.MsgSendMidSub;
import cn.ffcs.common.message.bo.ReceiverBO;
import cn.ffcs.sms.bo.SendResult;
import cn.ffcs.sms.service.SendSmsService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IMsgSendMidOutService;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.IReportMsgCCCfgService;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.service.impl.ReportMsgCCTypeEnum;
import cn.ffcs.zhsq.reportFocus.reportMsgCCSet.IReportMsgCCSetService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 重点关注上报整合接口，对外开放接口
 * @ClassName:   ReportIntegrationServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年9月15日 上午10:28:34
 */
@Service(value = "reportIntegrationService")
public class ReportIntegrationServiceImpl extends ReportFocusWorkflowServiceImpl implements IReportIntegrationService {
	@Autowired
	private IReportFocusService reportFocusService;
	
	@Autowired
	private IReportMsgCCCfgService reportMsgCCCfgService;
	
	@Autowired
	private IReportMsgCCSetService reportMsgCCSetService;
	
	@Autowired
	private IWorkflow4BaseService workflow4BaseService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService; 
	
	@Autowired
	private MessageOutService messageService;
	
	@Autowired
	private IMsgSendMidOutService msgSendMidService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private OrgSocialInfoOutService OrgSocialInfoService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;
	
	@Autowired
	private SendSmsService sendSmsService;

	@Autowired
	private DefaultListableBeanFactory defaultListableBeanFactory;

	@Autowired
	private AutowireCapableBeanFactory beanFactory;

	@Autowired
	private UserManageOutService userManageOutService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Map<String, Object> saveReportFocus(Map<String, Object> reportFocus, UserInfo userInfo,
			Map<String, Object> params) throws Exception {
		boolean result = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long reportId = reportFocusService.saveReportFocus(reportFocus, userInfo);
		
		if(reportId != null && reportId > 0) {
			boolean isStart = false;
			result = true;
			
			resultMap.put("reportId", reportId);
			
			if(CommonFunctions.isNotBlank(reportFocus, "reportUUID")) {
				resultMap.put("reportUUID", reportFocus.get("reportUUID"));
			}
			
			if(CommonFunctions.isNotBlank(params, "isStart")) {
				isStart = Boolean.valueOf(params.get("isStart").toString());
			}
			
			if(isStart) {
				result = this.startWorkflow4Report(reportId, userInfo, params);
				
				if(result && CommonFunctions.isNotBlank(params, "instanceId")) {
					resultMap.put("instanceId", params.get("instanceId"));
				}
			}
		}
		
		resultMap.put("result", result);
		
		return resultMap;
	}

	@Override
	public Map<String, Object> findReportFocusByUUIDSimple(String reportUUID, UserInfo userInfo, Map<String, Object> params) {
		return reportFocusService.findReportFocusByUUIDSimple(reportUUID, userInfo, params);
	}
	
	@Override
	public Map<String, Object> findReportFocusByUUID(String reportUUID, UserInfo userInfo, Map<String, Object> params) {
		Map<String,Object> reportFocusMap = reportFocusService.findReportFocusByUUID(reportUUID, userInfo, params);

		//是否进行所属网格中文名称的查询
		if(CommonFunctions.isNotBlank(params,"isCapRegionName") && Boolean.valueOf(String.valueOf(params.get("isCapRegionName")))){
			String regionCode = null;
			if(CommonFunctions.isNotBlank(reportFocusMap,"regionCode")){
				regionCode = String.valueOf(reportFocusMap.get("regionCode"));
			}
			if(StringUtils.isNotBlank(regionCode)){
				OrgEntityInfoBO orgEntityInfo = orgEntityInfoOutService.selectOrgEntityInfoByOrgCode(regionCode);

				if(null != orgEntityInfo && null != orgEntityInfo.getOrgName()){
					reportFocusMap.put("regionName", orgEntityInfo.getOrgName());
					reportFocusMap.put("regionPath", orgEntityInfo.getOrgEntityPath());
				}
			}
		}
		
		return reportFocusMap;
	}
	
	@Override
	public int delReportFocusByUUID(String reportUUID, UserInfo userInfo, Map<String, Object> params) {
		int result = 0;
		Map<String, Object> reportFocusMap = reportFocusService.findReportFocusByUUIDSimple(reportUUID, userInfo, params);
		if(reportFocusMap == null || !reportFocusMap.containsKey("reportId")) {
			return result;
		}
		String defaultInfoOrgCode = null;
		if(userInfo.getInfoOrgList()!=null && userInfo.getInfoOrgList().size()>0) {
			defaultInfoOrgCode = userInfo.getInfoOrgList().get(0).getOrgCode();
		}
		if(!(defaultInfoOrgCode!=null && reportFocusMap.containsKey("regionCode") &&
				reportFocusMap.get("regionCode").toString().startsWith(defaultInfoOrgCode))) {
			return result;
		}
		result = reportFocusService.delReportFocusByUUID(reportUUID, userInfo, params);

		//删除成功 发送mq消息
		if(result > 0 && Boolean.valueOf(RMQ_NA_REPORTFOCUS_OPEN)){
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag","delete");
			Map<String,Object> afterElement=new HashMap<String,Object>();
			afterElement.put("eventId", reportFocusMap.get("reportId").toString());
			mqParams.put("afterElement", afterElement);

			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public List<Map<String, Integer>> findCount4IntegrationTodo(Map<String, Object> params) throws Exception {
		return reportFocusService.findCount4IntegrationTodo(params);
	}
	
	@Override
	public int findCount4IntegrationMsgReading(Map<String, Object> params) throws Exception {
		return reportFocusService.findCount4IntegrationMsgReading(params);
	}

	@Override
	public EUDGPagination findPaginationIntegrationMsgReading(int pageNo, int pageSize, Map<String, Object> params)
			throws Exception {
		return reportFocusService.findPaginationIntegrationMsgReading(pageNo, pageSize, params);
	}
	
	@Override
	public int findCount4ReportFocus(Map<String, Object> params) throws Exception {
		return reportFocusService.findCount4ReportFocus(params);
	}
	
	@Override
	public EUDGPagination findPagination4ReportFocus(int pageNo, int pageSize, Map<String, Object> params) throws Exception {
		return reportFocusService.findPagination4ReportFocus(pageNo, pageSize, params);
	}

	@Override
	public List<Map<String,Object>> findList4ReportFocus(Map<String, Object> params) throws Exception {
		return reportFocusService.findList4ReportFocus(params);
	}
	
	@Override
	public boolean startWorkflow4Report(Long reportId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = super.startWorkflow4Report(reportId, userInfo, extraParam);
		
		if(flag) {
			Map<String, Object> decisionParams = new HashMap<String, Object>();

			decisionParams.putAll(extraParam);
			
			updateReportStatus(reportId, null, userInfo, decisionParams);
		}
		
		return flag;
	}
	
	@Override
	public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		String curTaskId = null, curNodeName = null;
		Map<String, Object> curDataMap = baseWorkflowService.findCurTaskData(instanceId);
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(CommonFunctions.isNotBlank(curDataMap, "WF_DBID_")) {
			curTaskId = curDataMap.get("WF_DBID_").toString();
		}
		
		if(CommonFunctions.isNotBlank(curDataMap, "NODE_NAME")) {
			curNodeName = curDataMap.get("NODE_NAME").toString();
		}
		
		flag = super.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);
		
		if(flag) {
			Map<String, Object> decisionParams = new HashMap<String, Object>();

			decisionParams.putAll(extraParam);
			decisionParams.put("nextNodeName", nextNodeName);
			
			updateReportStatus(null, instanceId, userInfo, decisionParams);

			//增加instanceId参数，供获取配置人员信息使用
			if(CommonFunctions.isBlank(extraParam,"instanceId")){
				extraParam.put("instanceId",instanceId);
			}
			if(CommonFunctions.isBlank(extraParam,"nextNodeName")){
				extraParam.put("nextNodeName",nextNodeName);
			}
			
			extraParam.put("curNodeName", curNodeName);

			if(CommonFunctions.isBlank(extraParam, "curDataMap")){
				extraParam.put("curDataMap", curDataMap);
			}
			
			try {
				sendCCMsg(Long.valueOf(curTaskId), nextUserInfoList, userInfo, extraParam);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean rejectWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = super.rejectWorkflow4Report(instanceId, userInfo, extraParam);
		
		if(flag) {
			boolean isRestoreRportStatus = true;
			
			if(CommonFunctions.isNotBlank(extraParam, "isRestoreRportStatus")) {
				isRestoreRportStatus = Boolean.valueOf(extraParam.get("isRestoreRportStatus").toString());
			}
			
			if(isRestoreRportStatus) {
				Map<String, Object> decisionParams = new HashMap<String, Object>();
	
				decisionParams.putAll(extraParam);
				decisionParams.put("isRejectOperate", true);
				
				updateReportStatus(null, instanceId, userInfo, decisionParams);
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean recallWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = super.recallWorkflow4Report(instanceId, userInfo, extraParam);
		
		if(flag) {
			boolean isRestoreRportStatus = true;
			
			if(CommonFunctions.isNotBlank(extraParam, "isRestoreRportStatus")) {
				isRestoreRportStatus = Boolean.valueOf(extraParam.get("isRestoreRportStatus").toString());
			}
			
			if(isRestoreRportStatus) {
				Map<String, Object> decisionParams = new HashMap<String, Object>();
	
				decisionParams.putAll(extraParam);
				decisionParams.put("isRecallOperate", true);
				
				updateReportStatus(null, instanceId, userInfo, decisionParams);
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean readMsgById(Long msgId, UserInfo userInfo) {
		boolean flag = false;
		Map<String, Object> resultMap = null;
		
		if(msgId == null || msgId <= 0) {
			throw new IllegalArgumentException("缺少有效的消息id！");
		}
		
		if(userInfo == null || userInfo.getUserId() == null) {
			throw new IllegalArgumentException("缺少有效的用户信息！");
		}
		
		resultMap = messageService.readMessage(msgId, userInfo.getUserId());
		
		if(CommonFunctions.isNotBlank(resultMap, "result")) {
			String SUCCESS_RESULT = "1";
			
			flag = SUCCESS_RESULT.equals(resultMap.get("result").toString());
		}
		
		return flag;
	}
	
	@Override
	public String capSmsContent(Map<String, Object> params, UserInfo userInfo) throws Exception {
		return reportFocusService.capSmsContent(params, userInfo);
	}
	
	@Override
	public boolean addUrgeOrRemind(Long instanceId, Map<String, Object> params, UserInfo userInfo) throws Exception {
		boolean flag = false;
		String remindUserPartyName = userInfo.getPartyName() + "(" + userInfo.getOrgName() + ")";
		params = params == null ? new HashMap<String, Object>() : params;
		
		params.put("remindUserPartyName", remindUserPartyName);
		
		flag = workflow4BaseService.addUrgeOrRemind(instanceId, params, userInfo);
		
		if(flag) {
			int RECORD_TYPE_REMIND = 2;//督办操作
			
			if(CommonFunctions.isBlank(params, "instanceId") && instanceId != null && instanceId > 0) {
				params.put("instanceId", instanceId);
			}
			
			try {
				reportFocusService.recordPoint(RECORD_TYPE_REMIND, userInfo, params);
			} catch(Exception e) {
				e.printStackTrace();
				if(logger.isErrorEnabled()) {
					logger.error(e.getMessage());
				}
			}

			//发送mq消息 formId
			if(Boolean.valueOf(RMQ_NA_REPORTFOCUS_OPEN)){
				Map<String,Object> mqParams=new HashMap<String,Object>();
				mqParams.put("eventFlag","remind");

				Map<String,Object> afterElement=new HashMap<String,Object>();
				if(CommonFunctions.isNotBlank(params, "formId")) {
					afterElement.put("eventId", params.get("formId"));
				}else{
					logger.error("入格事件主键【reportId】获取失败，请检查！");
				}
				if(CommonFunctions.isNotBlank(params, "instanceId") && instanceId != null && instanceId > 0) {
					afterElement.put("instanceId", instanceId);
				}else{
					logger.error("入格事件流程实例id【instanceId】获取失败，请检查！");
				}
				mqParams.put("afterElement", afterElement);

				try {
					sendFlowRmqMsg(mqParams);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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
	private void updateReportStatus(Long reportId, Long instanceId, UserInfo userInfo, Map<String, Object> params) {
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
		
		try {
			reportStatusDecisionMakingService = reportServiceAgent.capService();
			
			reportStatusDecisionMakingService.makeDecision(params);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 任务短信、消息发送
	 * @param curTaskIdStr	当前任务
	 * @param userInfo		操作用户
	 * @param params		额外参数
	 * 			advice		报告内容
	 * 			isSendTaskSMS	是否发送短信，默认为true
	 * 			isSendTaskMsg	是否发送站内消息，默认为true
	 * 			msgSendMidSendType	消息发送类型
	 * @throws Exception
	 */
	private void sendCCMsg(String curTaskIdStr, UserInfo userInfo, Map<String, Object> params) throws Exception {
		MsgSendMidSub msgSendMidSub = null;
		List<MsgSendMidSub> msgSendMidSubList = new ArrayList<MsgSendMidSub>();
		String REPORT_FOCUS_MSG_BIZ_TYPE = "REPORT_FOCUS_TASK",//重点关注任务消息类型
			   msgSendMidSendType = null, msgSendMidSubStatus = MsgSendMidSub.STATUS.INITIAL.toString(),
			   advice = null;
		boolean isCheckDuplication = true, isSendTaskSMS = true, isSendTaskMsg = true;
		String[] distributeUserIdArray = null, distributeOrgIdArray = null,
				 selectUserIdArray = null, selectOrgIdArray = null;
		Map<String, Map<String, String>> msgSendMidSubUserMap = new HashMap<String, Map<String, String>>();
		
		if(StringUtils.isBlank(curTaskIdStr)) {
			throw new IllegalArgumentException("缺少当前任务id！");
		}
		if(CommonFunctions.isNotBlank(params, "advice")) {
			advice = params.get("advice").toString();
		} else {
			throw new IllegalArgumentException("缺少发送内容【advice】！");
		}
		
		if(CommonFunctions.isNotBlank(params, "isSendTaskSMS")) {
			isSendTaskSMS = Boolean.valueOf(params.get("isSendTaskSMS").toString());
		}
		
		if(CommonFunctions.isNotBlank(params, "isSendTaskMsg")) {
			isSendTaskMsg = Boolean.valueOf(params.get("isSendTaskMsg").toString());
		}
		
		if(isSendTaskSMS || isSendTaskMsg) {
			if(CommonFunctions.isNotBlank(params, "distributeUserIds") 
					&& CommonFunctions.isNotBlank(params, "distributeOrgIds")) {
				String userOrgKey = null, userIdStr = null, orgIdStr = null;
				Map<String, String> userOrgMap = null;
				distributeUserIdArray = params.get("distributeUserIds").toString().split(",");
				distributeOrgIdArray = params.get("distributeOrgIds").toString().split(",");
				
				for(int index = 0, userIdLen = distributeUserIdArray.length, orgIdLen = distributeOrgIdArray.length; 
						index < userIdLen && index < orgIdLen; 
						index++) {
					userIdStr = distributeUserIdArray[index];
					orgIdStr = distributeOrgIdArray[index];
					userOrgKey = userIdStr + "-" + orgIdStr;
					userOrgMap = new HashMap<String, String>();
					
					if(!msgSendMidSubUserMap.containsKey(userOrgKey)) {
						userOrgMap.put("userId", userIdStr);
						userOrgMap.put("orgId", orgIdStr);
						msgSendMidSubUserMap.put(userOrgKey, userOrgMap);
					}
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "selectUserIds")
					&& CommonFunctions.isNotBlank(params, "selectOrgIds")) {
				String userOrgKey = null, userIdStr = null, orgIdStr = null;
				Map<String, String> userOrgMap = null;
				
				selectUserIdArray = params.get("selectUserIds").toString().split(",");
				selectOrgIdArray = params.get("selectOrgIds").toString().split(",");
				
				for(int index = 0, userIdLen = selectUserIdArray.length, orgIdLen = selectOrgIdArray.length; 
						index < userIdLen && index < orgIdLen; 
						index++) {
					userIdStr = selectUserIdArray[index];
					orgIdStr = selectOrgIdArray[index];
					userOrgKey = userIdStr + "-" + orgIdStr;
					userOrgMap = new HashMap<String, String>();
					
					if(!msgSendMidSubUserMap.containsKey(userOrgKey)) {
						userOrgMap.put("userId", userIdStr);
						userOrgMap.put("orgId", orgIdStr);
						msgSendMidSubUserMap.put(userOrgKey, userOrgMap);
					}
				}
			}
		}
		
		if(!msgSendMidSubUserMap.isEmpty()) {
			Map<String, String> userOrgMap = null;
			String userIdStr = null, orgIdStr = null, userName = null, orgCode = null;
			Long userId = null, orgId = null;
			UserBO userBO = null;
			OrgSocialInfoBO orgInfo = null;
			
			if(CommonFunctions.isNotBlank(params, "isMsgSendMidSubValid")) {
				boolean isMsgSendMidSubValid = Boolean.valueOf(params.get("isMsgSendMidSubValid").toString());
				
				if(!isMsgSendMidSubValid) {
					msgSendMidSubStatus = MsgSendMidSub.STATUS.INVALID.toString();
				}
			}
			
			for(String key : msgSendMidSubUserMap.keySet()) {
				userOrgMap = msgSendMidSubUserMap.get(key);
				userIdStr = userOrgMap.get("userId");
				orgIdStr = userOrgMap.get("orgId");
				userId = null;
				orgId = null;
				
				try {
					userId = Long.valueOf(userIdStr);
					orgId = Long.valueOf(orgIdStr);
				} catch(NumberFormatException e) {}
				
				if(userId != null && userId > 0
						&& orgId != null && orgId > 0) {
					userBO = userManageService.getUserInfoByUserId(userId);
					orgInfo = OrgSocialInfoService.findByOrgId(orgId);
					
					if(userBO != null && userBO.getUserId() != null
							&& orgInfo != null && orgInfo.getOrgId() != null) {
						userName = userBO.getPartyName();
						orgCode = orgInfo.getOrgCode();
						
						if(isSendTaskSMS) {
							//添加短信发送
							msgSendMidSub = new MsgSendMidSub();
							msgSendMidSub.setTargetType(MsgSendMidSub.TARGET_TYPE.SMS.toString());
							msgSendMidSub.setTargetFlag(String.valueOf(userBO.getVerifyMobile()));
							msgSendMidSub.setFlagType(MsgSendMidSub.FLAG_TYPE.MOBILE_PHONE_NUM.toString());
							msgSendMidSub.setSendMsg(advice);
							msgSendMidSub.setOrgCode(orgCode);
							msgSendMidSub.setTargetFlagName(userName);
							msgSendMidSub.setStatus(msgSendMidSubStatus);
							
							msgSendMidSubList.add(msgSendMidSub);
						}
						
						if(isSendTaskMsg) {
							//添加站内消息发送
							msgSendMidSub = new MsgSendMidSub();
							msgSendMidSub.setTargetType(MsgSendMidSub.TARGET_TYPE.MSG.toString());
							msgSendMidSub.setTargetFlag(String.valueOf(userBO.getUserId()));
							msgSendMidSub.setFlagType(MsgSendMidSub.FLAG_TYPE.USER_ID.toString());
							msgSendMidSub.setSendMsg(advice);
							msgSendMidSub.setOrgCode(orgCode);
							msgSendMidSub.setTargetFlagName(userName);
							msgSendMidSub.setStatus(msgSendMidSubStatus);
							
							msgSendMidSubList.add(msgSendMidSub);
						}
					}
				}
			}
		}
		
		if(msgSendMidSubList.size() > 0) {
			MsgSendMid msgSendMid = new MsgSendMid();
			Map<String, Object> extraParam = new HashMap<String, Object>();
			
			msgSendMid.setBizId(Long.valueOf(curTaskIdStr));
			msgSendMid.setBizType(REPORT_FOCUS_MSG_BIZ_TYPE);
			
			if(CommonFunctions.isNotBlank(params, "msgSendMidSendType")) {
				msgSendMidSendType = params.get("msgSendMidSendType").toString();
			} else {
				msgSendMidSendType = REPORT_FOCUS_MSG_BIZ_TYPE;
			}
			
			msgSendMid.setSendType(msgSendMidSendType);
			
			if(CommonFunctions.isNotBlank(params, "isCheckDuplication")) {
				isCheckDuplication = Boolean.valueOf(params.get("isCheckDuplication").toString());
			}
			
			extraParam.put("isCheckDuplication", isCheckDuplication);
			
			msgSendMidService.saveMsgSendMid(msgSendMid, msgSendMidSubList, extraParam);
		}
	}
	
	/**
	 *  发送抄送信息
	 * @param curTaskId			当前任务id
	 * @param nextUserInfoList	下一办理人员信息
	 * @param userInfo			操作用户信息
	 * @param params		额外参数
	 * 			reportId	上报id
	 * 			formId		上报id
	 * 			reportUUID	上报uuid
	 * 			advice		办理意见
	 */
	private void sendCCMsg(Long curTaskId, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> params) {
		String advice = null;
		boolean isSendTaskSMS = true;
		List<UserInfo> userInfoList = new ArrayList<UserInfo>(),
					   userInfoMainList = null, 
					   userInfoDistributeList = null, 
					   userInfoSelectList = null;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "advice")) {
			advice = params.get("advice").toString();
		}
		if(CommonFunctions.isNotBlank(params, "isSendTaskSMS")) {
			isSendTaskSMS = Boolean.valueOf(params.get("isSendTaskSMS").toString());
		}
		
		//主送人员
		String MAIN_MSG_MODULE_CODE = "250101";
		if(nextUserInfoList != null && nextUserInfoList.size() > 0) {
			userInfoMainList = nextUserInfoList;
		} else {
			userInfoMainList = convert2UserInfo("nextUserIds", "nextOrgIds", params);
		}
		
		//获取配送人员配置模块配置的主送人员信息
		params.put("ccType",ReportMsgCCTypeEnum.CCTYPE_MAIN.getCcType());

		//获取消息主送人员信息，该人员不参与环节扭转，只接收消息
		userInfoMainList.addAll(this.getPushedUser(userInfo,params));

		userInfoList.addAll(userInfoMainList);
		saveMsgCCSet(curTaskId, ReportMsgCCTypeEnum.CCTYPE_MAIN.getCcType(), userInfoMainList);
		sendMsg(MAIN_MSG_MODULE_CODE, curTaskId, advice, userInfo, userInfoMainList);
		
		//分送人员
		String DISTRIBUTE_MSG_MODULE_CODE = "250102";
		userInfoDistributeList = convert2UserInfo("distributeUserIds", "distributeOrgIds", params);
		//userInfoList.addAll(userInfoDistributeList);
		saveMsgCCSet(curTaskId, ReportMsgCCTypeEnum.CCTYPE_DISTRIBUTE.getCcType(), userInfoDistributeList);
		sendMsg(DISTRIBUTE_MSG_MODULE_CODE, curTaskId, advice, userInfo, userInfoDistributeList);
		
		//选送人员
		String SELECT_MSG_MODULE_CODE = "250103";
		userInfoSelectList = convert2UserInfo("selectUserIds", "selectOrgIds", params);
		userInfoList.addAll(userInfoSelectList);
		saveMsgCCSet(curTaskId, ReportMsgCCTypeEnum.CCTYPE_SELECT.getCcType(), userInfoSelectList);
		sendMsg(SELECT_MSG_MODULE_CODE, curTaskId, advice, userInfo, userInfoSelectList);
		
		//短信发送
		if(isSendTaskSMS) {
			//add ztc 20211022 需求：5876110 进行消息内容变更
			advice = this.transAdviceContent(advice,userInfo,params);
			this.sendSMS(advice, userInfo, userInfoList);
		}
	}
	
	/**
	 * 转换为用户信息
	 * @param userIdKey	用户id的key值
	 * @param orgIdKey	组织id的key值
	 * @param params	
	 * @return
	 */
	private List<UserInfo> convert2UserInfo(String userIdKey, String orgIdKey, Map<String, Object> params) {
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		
		if(CommonFunctions.isNotBlank(params, userIdKey) 
				&& CommonFunctions.isNotBlank(params, orgIdKey)) {
			Long userId = null, orgId = null;
			String userIdStr = null, orgIdStr = null;
			String[] distributeUserIdArray = params.get(userIdKey).toString().split(",");
			String[] distributeOrgIdArray = params.get(orgIdKey).toString().split(",");
			UserBO userBO = null;
			UserInfo userInfo = null;
			
			for(int index = 0, userIdLen = distributeUserIdArray.length, orgIdLen = distributeOrgIdArray.length;
					index < userIdLen && index < orgIdLen; index++) {
				userId = null;
				orgId = null;
				userIdStr = distributeUserIdArray[index];
				orgIdStr = distributeOrgIdArray[index];
				
				try {
					userId = Long.valueOf(userIdStr);
				} catch(NumberFormatException e) {}
				
				try {
					orgId = Long.valueOf(orgIdStr);
				} catch(NumberFormatException e) {}
				
				if(userId != null && userId > 0) {
					userBO = userManageService.getUserInfoByUserId(userId);
				}
				
				if(userBO != null && userBO.getUserId() != null) {
					userInfo = new UserInfo();
					
					userInfo.setUserId(userBO.getUserId());
					userInfo.setPartyName(userBO.getPartyName());
					userInfo.setVerifyMobile(String.valueOf(userBO.getVerifyMobile()));
					
					if(orgId != null && orgId > 0) {
						userInfo.setOrgId(orgId);
					}
					
					userInfoList.add(userInfo);
				}
			}
		}
		
		return userInfoList;
	}
	
	/**
	 * 存储人员配送信息
	 * @param taskId		任务id
	 * @param ccType		配送类型
	 * @param userInfoList	配送用户信息
	 */
	private void saveMsgCCSet(Long taskId, String ccType, List<UserInfo> userInfoList) {
		if(userInfoList != null) {
			Map<String, Object> msgCCSetMap = null;
			Long userId = null, orgId = null;
			Set<String> userOrgSet = new HashSet<String>();
			StringBuffer userOrgIdBuffer = null;
			
			for(UserInfo userInfo : userInfoList) {
				msgCCSetMap = new HashMap<String, Object>();
				userId = userInfo.getUserId();
				orgId = userInfo.getOrgId();
				userOrgIdBuffer = new StringBuffer("");
				
				if(userId != null && userId > 0) {
					msgCCSetMap.put("userId", userId);
					msgCCSetMap.put("userName", userInfo.getPartyName());
					userOrgIdBuffer.append("-").append(userId);
				}
				if(orgId != null && orgId > 0) {
					msgCCSetMap.put("orgId", orgId);
					userOrgIdBuffer.append("-").append(orgId);
				}
				msgCCSetMap.put("taskId", taskId);
				msgCCSetMap.put("ccType", ccType);
				
				if(!userOrgSet.contains(userOrgIdBuffer.toString())) {
					try {
						reportMsgCCSetService.saveMsgCCSet(msgCCSetMap, userInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					userOrgSet.add(userOrgIdBuffer.toString());
				}
			}
		}
	}
	
	/**
	 * 发送站内消息
	 * @param msgModuleCode		消息模块编码
	 * @param msgBizId			消息业务id
	 * @param msgContent		消息内容
	 * @param operateUserInfo	操作用户信息
	 * @param userInfoList		消息接收用户
	 */
	private void sendMsg(String msgModuleCode, Long msgBizId, String msgContent, UserInfo operateUserInfo, List<UserInfo> userInfoList) {
		if(StringUtils.isNotBlank(msgModuleCode)
				&& msgBizId != null && msgBizId > 0
				&& StringUtils.isNotBlank(msgContent)) {
			List<Map<String, Long>> userOrgMapList = null;
			
			if(userInfoList != null && userInfoList.size() > 0) {
				Long userId = null, orgId = null;
				Map<String, Long> userOrgMap = null;
				StringBuffer userOrgBuffer = null;
				Set<String> userOrgSet = new HashSet<String>();
				
				userOrgMapList = new ArrayList<Map<String, Long>>();
				
				for(UserInfo userInfo : userInfoList) {
					userOrgMap = new HashMap<String, Long>();
					userOrgBuffer = new StringBuffer("");
					userId = userInfo.getUserId();
					orgId = userInfo.getOrgId();
					
					userOrgBuffer.append(userId).append("-").append(orgId);
					
					if(!userOrgSet.contains(userOrgBuffer.toString())) {
						userOrgMap.put("userId", userId);
						userOrgMap.put("orgId", orgId);
						
						userOrgMapList.add(userOrgMap);
						
						userOrgSet.add(userOrgBuffer.toString());
					}
				}
			}
			
			if(userOrgMapList != null) {
				ReceiverBO receiver = new ReceiverBO();
				String REPORT_FOCUS_MSG_ACTION = "发送了";
				
				receiver.setUserOrgList(userOrgMapList);
				
				messageService.sendCommonMessageNA(operateUserInfo.getUserId(), operateUserInfo.getOrgId(), msgModuleCode, msgBizId, null, msgContent, REPORT_FOCUS_MSG_ACTION, receiver);
			}
		}
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
	/**
	 * 获取人员配置模块指定流程指定节点的人员配置信息
	 * @param params
	 * 			workflowName 流程图名称
	 * 			instanceId 流程实例id（当流程图名称为空时，根据流程实例id获取流程图相关信息）
	 * 			nextNodeName 流程图节点名称
	 * 			ccType 配置人员类型，配置值详见枚举类：ReportMsgCCTypeEnum
	 * */
	private List<UserInfo> getPushedUser(UserInfo userInfo,Map<String,Object> params){
		//获取配送人员配置模块配置的人员信息
		Map<String, List<UserBO>> mainUserMap = new HashMap<>();
		//人员配送类型
		String ccType = null, userOrgCode = userInfo.getOrgCode();
		
		if(null == params){
			params = new HashMap<>();
		}
		if(CommonFunctions.isNotBlank(params,"ccType")){
			ccType = String.valueOf(params.get("ccType"));
		}
		if(CommonFunctions.isBlank(params,"workflowName") && CommonFunctions.isNotBlank(params,"instanceId")){
			Long instanceId = Long.valueOf(String.valueOf(params.get("instanceId")));
			if(instanceId != null && instanceId > 0) {
				ProInstance pro = baseWorkflowService.findProByInstanceId(instanceId);
				if(pro != null && StringUtils.isNotBlank(pro.getProName())) {
					params.put("workflowName",pro.getProName());
				}
			}
		}
		if(CommonFunctions.isNotBlank(params, "curNodeName")) {
			params.put("wfStartNodeName", params.get("curNodeName"));
		}
		if(CommonFunctions.isNotBlank(params,"nextNodeName")) {
			params.put("wfEndNodeName", params.get("nextNodeName"));
		}
		
		if(CommonFunctions.isNotBlank(params,"workflowName") && CommonFunctions.isNotBlank(params,"wfEndNodeName")){
			mainUserMap = reportMsgCCCfgService.findCfg4User(params, userOrgCode);
			
			if(mainUserMap == null || mainUserMap.isEmpty()) {
				params.remove("wfStartNodeName");
			}
			
			mainUserMap = reportMsgCCCfgService.findCfg4User(params, userOrgCode);
		}
		//获取主送人员信息
		List<UserBO> mainUserBoList = new ArrayList<>();
		List<UserInfo> userInfoList = new ArrayList<>();
		if(CommonFunctions.isNotBlank(mainUserMap,ccType)){
			mainUserBoList = mainUserMap.get(ccType);
		}
		if(mainUserBoList.size() > 0){
			UserInfo user = null;
			for (UserBO userBO : mainUserBoList) {
				user = new UserInfo();
				user.setUserId(userBO.getUserId());
				if(StringUtils.isNotBlank(userBO.getSocialOrgId())){
					user.setOrgId(Long.valueOf(userBO.getSocialOrgId()));
				}
				user.setOrgCode(userBO.getOrgCode());
				user.setPartyName(userBO.getPartyName());
				user.setOrgName(userBO.getOrgName());
				user.setVerifyMobile(String.valueOf(userBO.getVerifyMobile()));
				userInfoList.add(user);
			}
		}
		return userInfoList;
	}

	private String transAdviceContent(String advice,UserInfo userInfo,Map<String, Object> params){
		if(StringUtils.isNotBlank(advice)){
			StringBuffer dynamicContent = new StringBuffer();
			String reportType = null,moduleName = null;
			Map<String,Object> curDataMap = new HashMap<>();

			if(CommonFunctions.isNotBlank(params,"curDataMap")){
				curDataMap = (Map<String, Object>) params.get("curDataMap");
			}
			if(CommonFunctions.isNotBlank(params,"instanceId") && CommonFunctions.isBlank(curDataMap,"DUEDATE_")){
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

}
