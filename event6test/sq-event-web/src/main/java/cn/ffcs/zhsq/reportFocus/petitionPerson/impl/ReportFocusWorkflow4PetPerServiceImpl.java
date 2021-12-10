package cn.ffcs.zhsq.reportFocus.petitionPerson.impl;

import cn.ffcs.common.utils.StringUtils;
import cn.ffcs.resident.bo.CiRs;
import cn.ffcs.resident.bo.PartyIndividual;
import cn.ffcs.resident.service.CiRsService;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.PetitionRecord;
import cn.ffcs.shequ.zzgl.service.crowd.IPetitionRecordService;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.workflow.spring.WorkflowHolidayInfoService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.petitionPerson.ReportPetPerMapper;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.service.impl.ReportMsgCCTypeEnum;
import cn.ffcs.zhsq.reportFocus.reportMsgCCSet.IReportMsgCCSetService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportFocusWorkflow4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 信访人员稳控工作流相关接口
 * @ClassName:   ReportFocusWorkflow4PetPerServiceImpl   
 * @author:      郭昱丹(guoyd)
 * @date:        2021年5月7日 上午10:16:15
 */
@Service(value = "reportFocusWorkflow4PetPerService")
public class ReportFocusWorkflow4PetPerServiceImpl extends ReportFocusWorkflow4TwoVioPreServiceImpl {

	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	@Resource(name="eventDisposalWorkflowForEventService")
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	@Autowired
	private IReportFocusService reportFocusService;
	@Autowired
	private WorkflowHolidayInfoService workflowHolidayInfoService;
	@Autowired
	private ReportPetPerMapper reportPetPerMapper;
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	@Autowired
	private UserManageOutService userManageService;
	@Autowired
	private IWorkflow4BaseService workflow4BaseService;
	@Autowired
	private CiRsService ciRsService;
	@Autowired
	private IReportMsgCCSetService reportMsgCCSetService;
	@Autowired
	private IPetitionRecordService petitionRecordService;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> initWorkflow4Handle(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		Map<String, Object> initMap = super.initWorkflow4Handle(instanceId, userInfo, params);
		boolean isAble2Handle = false;
		
		if(CommonFunctions.isNotBlank(initMap, "isAble2Handle")) {
			isAble2Handle = Boolean.valueOf(initMap.get("isAble2Handle").toString());
		}
		
		if(isAble2Handle) {
			String formTypeIdStr = null, curNodeName = null;
			boolean isEditable = false, isEditableNode = false;
			
			if(CommonFunctions.isNotBlank(initMap, "isEditable")) {
				isEditable = Boolean.valueOf(initMap.get("isEditable").toString());
			}
			
			if(CommonFunctions.isNotBlank(initMap, "formTypeId")) {
				formTypeIdStr = initMap.get("formTypeId").toString();
			}
			
			if(CommonFunctions.isNotBlank(initMap, "curNodeName")) {
				curNodeName = initMap.get("curNodeName").toString();
			}
			
			String dataSource = "";
			if(CommonFunctions.isNotBlank(params, "reportFocusMap")) {
				Map<String, Object> reportFocusMap = new HashMap<String, Object>();
				reportFocusMap = (Map<String, Object>)params.get("reportFocusMap");
				dataSource = StringUtils.toString(reportFocusMap.get("dataSource"));
			}
			
			if(CommonFunctions.isNotBlank(initMap, "nextTaskNodes")) {
				List<Map<String, Object>> nextTaskNodes = new ArrayList<Map<String, Object>>();
				nextTaskNodes = (List<Map<String, Object>>)initMap.get("nextTaskNodes");
				
				if(FocusReportNode362Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)){
					if(FocusReportNode362Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)) {//发现问题
						String taskNode = "";
						if(PetPerDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)) {
							taskNode = FocusReportNode362Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName();
						}else if(PetPerDataSourceEnum.PUBLIC_SECURITY.getDataSource().equals(dataSource)) {
							taskNode = FocusReportNode362Enum.DISTRIBUTE_NODE_NAME2.getTaskNodeName();
						}else if(PetPerDataSourceEnum.OFFICE_SPECIALIST.getDataSource().equals(dataSource)) {
							taskNode = FocusReportNode362Enum.DISTRIBUTE_NODE_NAME3.getTaskNodeName();
						}
						if(!StringUtils.isBlank(taskNode)) {
							for(int i = nextTaskNodes.size() - 1; i >= 0; i--) {
								if(!StringUtils.toString(nextTaskNodes.get(i).get("nodeName")).equals(taskNode)) {
									nextTaskNodes.remove(i);
								}
							}
						}
					}
					
					if(FocusReportNode362Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName().equals(curNodeName)
						||FocusReportNode362Enum.FEEDBACK_NODE_NAME.getTaskNodeName().equals(curNodeName)
						||FocusReportNode362Enum.DISTRIBUTE_NODE_NAME2.getTaskNodeName().equals(curNodeName)
						){
						//处理中拼接
						Map<String, Object> node = new HashMap<String, Object>();
						node.put("nodeName", FocusReportNode362Enum.DOING_NODE_NAME.getTaskNodeName());
						node.put("nodeNameZH", FocusReportNode362Enum.DOING_NODE_NAME.getTaskNodeNameZH());
						node.put("nodeCode", "D4");
						node.put("transitionCode", "U3R0U4");
						node.put("nodeType", "1");
						nextTaskNodes.add(node);
					}
				}else if(FocusReportNode36201Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
					if(FocusReportNode36201Enum.FEEDBACK_NODE_NAME.getTaskNodeName().equals(curNodeName)){
						//处理中拼接
						Map<String, Object> node = new HashMap<String, Object>();
						node.put("nodeName", FocusReportNode362Enum.DOING_NODE_NAME.getTaskNodeName());
						node.put("nodeNameZH", FocusReportNode362Enum.DOING_NODE_NAME.getTaskNodeNameZH());
						node.put("nodeCode", "D4");
						node.put("transitionCode", "U6R2U4");
						node.put("nodeType", "1");
						nextTaskNodes.add(node);
					}
				}
				if(nextTaskNodes.size() > 0){
					initMap.put("nextTaskNodes",nextTaskNodes);
				}
			}
			
//			isEditable = true;
			
			initMap.put("isEditable", isEditable);
			initMap.put("isEditableNode", isEditableNode);
			
		}
		
		return initMap;
	}
	
	@SuppressWarnings("unused")
	@Override
	public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		String curNodeName = null, formTypeIdStr = null, curTaskId = null,formId = null,advice = null,preCurTaskId = null;
		Long reportId = null;
		
		if(instanceId != null && instanceId > 0) {
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			if(proInstance != null) {
				curNodeName = proInstance.getCurNode();
				formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
				formId = String.valueOf(proInstance.getFormId());
				reportId = proInstance.getFormId();
			}
			Map<String, Object> curDataMap = baseWorkflowService.findCurTaskData(instanceId);
			if(CommonFunctions.isNotBlank(curDataMap, "WF_DBID_")) {
				curTaskId = curDataMap.get("WF_DBID_").toString();
			}
			if(CommonFunctions.isNotBlank(extraParam, "advice")) {
				advice = String.valueOf(extraParam.get("advice"));
			}
		}
		
		if((FocusReportNode362Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				||FocusReportNode36201Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr))
			&& FocusReportNode362Enum.DOING_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
			
			Map<String, Object> resultMap = eventDisposalWorkflowService.saveHandlingTask(Long.parseLong(formId), instanceId, FocusReportNode362Enum.DOING_NODE_NAME.getTaskNodeName(), advice, userInfo,null);
			flag = true;
		}else {
			if(FocusReportNode362Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
					&& FocusReportNode362Enum.FEEDBACK_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
				preCurTaskId = curTaskId;//新增子流程
				
				Map<String, Object> subProParams = new HashMap<String, Object>();
				UserInfo nextUserInfo = new UserInfo();

				if(nextUserInfoList != null && nextUserInfoList.size() > 0) {
					nextUserInfo = nextUserInfoList.get(0);
				} else if(CommonFunctions.isNotBlank(extraParam, "nextUserIds")
						&& CommonFunctions.isNotBlank(extraParam, "nextOrgIds")) {
					Long nextUserId = Long.valueOf(extraParam.get("nextUserIds").toString().split(",")[0]),
							nextOrgId = Long.valueOf(extraParam.get("nextOrgIds").toString().split(",")[0]);
					OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(nextOrgId);
					UserBO userBO = userManageService.getUserInfoByUserId(nextUserId);

					if(userBO != null && userBO.getUserId() != null) {
						nextUserInfo.setUserId(userBO.getUserId());
						nextUserInfo.setPartyName(userBO.getPartyName());
					}

					if(orgInfo != null && orgInfo.getOrgId() != null) {
						nextUserInfo.setOrgId(orgInfo.getOrgId());
						nextUserInfo.setOrgCode(orgInfo.getOrgCode());
						nextUserInfo.setOrgName(orgInfo.getOrgName());
					}
				}

				subProParams.putAll(extraParam);
				subProParams.put("parentId", instanceId);
				subProParams.put("taskId", preCurTaskId);
				subProParams.put("isConvertProinstance", false);
				subProParams.remove("instanceId");//若不移除，会影响子流程实例的生成

				//启动子流程“南安信访人员稳控情况反馈子流程”
				subProParams.put("formTypeId", FocusReportNode36201Enum.FORM_TYPE_ID.toString());

				flag = startWorkflow4Report(reportId, userInfo, subProParams);

				//子流程启动成功 发送mq消息
				if(flag){
					sendSubFlowRMQmMsg(reportId,formTypeIdStr, FocusReportNode36201Enum.FORM_TYPE_ID.toString(),instanceId,subProParams.get("instanceId"));
				}
			}else if(FocusReportNode362Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
					&& FocusReportNode362Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)
					&&FocusReportNode362Enum.FEEDBACK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				
				endAllSubProInstance(instanceId, FocusReportNode36201Enum.FORM_TYPE_ID.toString(),null);
			}
			
			if(FocusReportNode362Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&&FocusReportNode362Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
				//TODO  同步数据人口数据
				try {
					String controlResult = setValue("controlResult", extraParam);
					//稳控到位
					if("04".equals(controlResult)) {
						PartyIndividual partyIndividual = new PartyIndividual();
						partyIndividual.setPartyName(setValue("partyName_", extraParam));
						partyIndividual.setCertType(setValue("certType", extraParam));
						partyIndividual.setIdentityCard(setValue("identityCard", extraParam));
						
						CiRs ciRs = new CiRs();
						ciRs.setResidenceAddr(setValue("iResidenceAddr", extraParam));
						ciRs.setOrgCode(setValue("residenceAddrNo", extraParam));
						ciRs.setType(setValue("partyType", extraParam));
						
						Map<String,Object> ciRsMap = ciRsService.addPartyIndividualAndCiRs(partyIndividual, ciRs, userInfo);
						if(CommonFunctions.isNotBlank(ciRsMap, "partyId")) {
							PetitionRecord petitionRecord = new PetitionRecord();
							petitionRecord.setPartyId(Long.parseLong(StringUtils.toString(ciRsMap.get("partyId"))));
							/*petitionRecord.setControlType(setValue("controlType", extraParam));
							petitionRecord.setPetitionPartyType(setValue("petitionPartyType", extraParam));
							petitionRecord.setPetitionLevel(setValue("petitionTypes", extraParam));
							
							petitionRecordService.createOrUpdatePetitionRecord_NanAn(petitionRecord, userInfo);*/
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(FocusReportNode36201Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&&(FocusReportNode36201Enum.FEEDBACK_NODE_NAME.getTaskNodeName().equals(nextNodeName)
							||FocusReportNode362Enum.DOING_NODE_NAME.getTaskNodeName().equals(nextNodeName))) {
				//更新处理时间
				Map<String, Object> reportParams = new HashMap<String, Object>(),reportFocusMap = null;
				reportParams.put("reportId", reportId);
				reportParams.put("reportType", "12");
				reportFocusMap = reportFocusService.findReportFocusByUUID(null, userInfo, reportParams);
				
				if(CommonFunctions.isNotBlank(reportFocusMap, "controlType")) {
					String controlType = reportFocusMap.get("controlType").toString();
					int date = 0;
					Date newDate = new Date();
					if("01".equals(controlType)&&CommonFunctions.isNotBlank(reportFocusMap, "petitionPartyType")) {//日常稳控
						String petitionPartyType = reportFocusMap.get("petitionPartyType").toString();
						if("01".equals(petitionPartyType)) {//信访重点人员
							date = 3;
						}else if("02".equals(petitionPartyType)) {//信访关注人员
							date = 7;
						}
						newDate = workflowHolidayInfoService.getWorkDateByInterval(new Date(), date);
					}else if("02".equals(controlType)) {//重要节点稳控
						date = 12;//半天
						newDate = workflowHolidayInfoService.getNaturalHourByInterval(new Date(), date);
					}
					
					String curTaskId_ = "";
					Map<String, Object> curDataMap = baseWorkflowService.findCurTaskData(instanceId);
					if(CommonFunctions.isNotBlank(curDataMap, "WF_DBID_")) {
						curTaskId_ = curDataMap.get("WF_DBID_").toString();
						reportParams.put("newDate", newDate);
						reportParams.put("taskId", curTaskId_);
						reportPetPerMapper.updateTaskTime(reportParams);
					}
				}
			}
			
			flag = super.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);
		}

		return flag;
	}
	
	private String setValue(String name,Map<String, Object> extraParam) {
		String temp = "";
		if(CommonFunctions.isNotBlank(extraParam, "_"+name)) {
			temp = StringUtils.toString(extraParam.get("_"+name));
		}
		if(StringUtils.isBlank(temp)
				&&CommonFunctions.isNotBlank(extraParam, name)) {
			temp = StringUtils.toString(extraParam.get(name));
		}
		
		return temp;
	}
	
	/**
	 * 转换为用户信息
	 * @param userIdKey	用户id的key值
	 * @param orgIdKey	组织id的key值
	 * @param params	
	 * @return
	 */
//	private List<UserInfo> convert2UserInfo(String userIdKey, String orgIdKey, Map<String, Object> params) {
//		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
//		
//		if(CommonFunctions.isNotBlank(params, userIdKey) 
//				&& CommonFunctions.isNotBlank(params, orgIdKey)) {
//			Long userId = null, orgId = null;
//			String userIdStr = null, orgIdStr = null;
//			String[] distributeUserIdArray = params.get(userIdKey).toString().split(",");
//			String[] distributeOrgIdArray = params.get(orgIdKey).toString().split(",");
//			UserBO userBO = null;
//			UserInfo userInfo = null;
//			
//			for(int index = 0, userIdLen = distributeUserIdArray.length, orgIdLen = distributeOrgIdArray.length;
//					index < userIdLen && index < orgIdLen; index++) {
//				userId = null;
//				orgId = null;
//				userIdStr = distributeUserIdArray[index];
//				orgIdStr = distributeOrgIdArray[index];
//				
//				try {
//					userId = Long.valueOf(userIdStr);
//				} catch(NumberFormatException e) {}
//				
//				try {
//					orgId = Long.valueOf(orgIdStr);
//				} catch(NumberFormatException e) {}
//				
//				if(userId != null && userId > 0) {
//					userBO = userManageService.getUserInfoByUserId(userId);
//				}
//				
//				if(userBO != null && userBO.getUserId() != null) {
//					userInfo = new UserInfo();
//					
//					userInfo.setUserId(userBO.getUserId());
//					userInfo.setPartyName(userBO.getPartyName());
//					userInfo.setVerifyMobile(String.valueOf(userBO.getVerifyMobile()));
//					
//					if(orgId != null && orgId > 0) {
//						userInfo.setOrgId(orgId);
//					}
//					
//					userInfoList.add(userInfo);
//				}
//			}
//		}
//		
//		return userInfoList;
//	}
	
	@Override
	public boolean recallWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		extraParam.put("isAble2Recall", true);
		
		flag = super.recallWorkflow4Report(instanceId, userInfo, extraParam);
		
		return flag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> taskMapList = workflow4BaseService.capHandledTaskInfoMap(instanceId, order, params),
								  resultMapList = new ArrayList<Map<String, Object>>();
		ProInstance proInstance = this.baseWorkflowService.findProByInstanceId(instanceId);
		String END_PRO_STATUS = "2",		//流程实例状态——结束
			   ABOLISH_PRO_STATUS = "4",	//流程实例状态——废除
			   LIST_TYPE_TODO = "2";		//待办列表
		
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
			proMap.put("instanceStatus", "1,2");// 1 办理中；2 归档；
			
			msgCCSetResultMap = findMsgCCSetByInstanceId(instanceId);
			
			String[] REMOVE_NODE_CODE = {"KS"};
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
				if(CommonFunctions.isNotBlank(taskMap,"TASK_ID") && CommonFunctions.isNotBlank(taskMap,"TASK_CODE")
	                      && FocusReportNode362Enum.TODO_NODE_NAME.getTaskNodeName().equals(taskMap.get("TASK_CODE"))
	              ){
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
				
				if(CommonFunctions.isNotBlank(taskMap, "TASK_ID") 
						&& CommonFunctions.isNotBlank(superviseMap, taskMap.get("TASK_ID").toString())) {
					taskMap.put("IS_SUPERVISED", true);
				}
				
				resultMapList.add(taskMap);
			}
			
			if(FocusReportNode362Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
					&& resultMapList != null && resultMapList.size() > 0
					&& IWorkflow4BaseService.SQL_ORDER_DESC.equals(order)) {
				Map<String, Object> taskMap = resultMapList.get(0);

				if(CommonFunctions.isBlank(taskMap, "IS_CURRENT_TASK")) {
					String taskNameZH = null;
					
					if(CommonFunctions.isNotBlank(taskMap, "TASK_NAME")) {
						taskNameZH = taskMap.get("TASK_NAME").toString();
					}
					
					if(!FocusReportNode362Enum.END_NODE_NAME.getTaskNodeNameZH().equals(taskNameZH)) {
						Map<String, Object> endTaskMap = new HashMap<String, Object>();
						
						endTaskMap.put("TASK_NAME", FocusReportNode362Enum.END_NODE_NAME.getTaskNodeNameZH());

						resultMapList.add(0, endTaskMap);
					}
				}
			}
		}

		for(Map<String, Object> resultMap : resultMapList) {
			String TASK_CODE = null;
			if(CommonFunctions.isNotBlank(resultMap, "TASK_CODE")) {
				TASK_CODE = resultMap.get("TASK_CODE").toString();
			}
			String TASK_ID = null;
			if(CommonFunctions.isNotBlank(resultMap, "TASK_ID")) {
				TASK_ID = resultMap.get("TASK_ID").toString();
			}
			if(FocusReportNode362Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName().equals(TASK_CODE)
					||FocusReportNode362Enum.FEEDBACK_NODE_NAME.getTaskNodeName().equals(TASK_CODE)
					||FocusReportNode362Enum.DISTRIBUTE_NODE_NAME2.getTaskNodeName().equals(TASK_CODE)
					||FocusReportNode36201Enum.FEEDBACK_NODE_NAME.getTaskNodeName().equals(TASK_CODE)) {
				List<Map<String, Object>> subAndReceivedTaskListTmp = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> timeAndRemarkList = null;
				List<Map<String, Object>> subAndReceivedTaskList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> subTaskList = eventDisposalWorkflowService.querySubTaskDetails(TASK_ID, IEventDisposalWorkflowService.SQL_ORDER_DESC, null);
				if(subTaskList != null && subTaskList.size() > 0) {
					for(Map<String, Object> subTask : subTaskList) {
						if(CommonFunctions.isNotBlank(subTask, "TASK_NAME")) {//清除处理中的环节名称
							if(ConstantValue.HANDLING_TASK_CODE.equals(subTask.get("TASK_NAME"))) {
								subTask.remove("TASK_NAME");
							}
						}
						subAndReceivedTaskList.add(subTask);
					}
					resultMap.put("subTaskList", subTaskList);
				}
				if(subAndReceivedTaskList.size() > 0) {
					Map<String, Object> timeAndRemarkMap = null;
					boolean isSubAndReceivedBlank = true;
					
					for(Map<String, Object> subAndReceivedTaskMap : subAndReceivedTaskList) {
						timeAndRemarkList = new ArrayList<Map<String, Object>>();
						timeAndRemarkMap = new HashMap<String, Object>();
						isSubAndReceivedBlank = true;
						
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
						
						for(Map<String, Object> subAndReceivedTaskTmpMap : subAndReceivedTaskListTmp) {
							//合并准则：1、存在TRANSACTOR_ID和ORG_ID，且二者均不为空时，二者同时相等者进行合并；
							if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "TRANSACTOR_ID") && CommonFunctions.isNotBlank(subAndReceivedTaskTmpMap, "TRANSACTOR_ID") && 
									subAndReceivedTaskMap.get("TRANSACTOR_ID").equals(subAndReceivedTaskTmpMap.get("TRANSACTOR_ID")) && 
									CommonFunctions.isNotBlank(subAndReceivedTaskMap, "ORG_ID") && CommonFunctions.isNotBlank(subAndReceivedTaskTmpMap, "ORG_ID") &&
									subAndReceivedTaskMap.get("ORG_ID").equals(subAndReceivedTaskTmpMap.get("ORG_ID"))) {
								if(CommonFunctions.isNotBlank(subAndReceivedTaskTmpMap, "timeAndRemarkList")) {
									timeAndRemarkList.addAll((List<Map<String, Object>>)subAndReceivedTaskTmpMap.get("timeAndRemarkList"));
								}
								
								if(!timeAndRemarkMap.isEmpty()) {
									timeAndRemarkList.add(timeAndRemarkMap);
									
									subAndReceivedTaskTmpMap.put("timeAndRemarkList", timeAndRemarkList);
								}
								
								isSubAndReceivedBlank = false;
								
								break;
							}
						}
						
						if(isSubAndReceivedBlank) {
							if(!timeAndRemarkMap.isEmpty()) {
								timeAndRemarkList.add(timeAndRemarkMap);
								
								subAndReceivedTaskMap.put("timeAndRemarkList", timeAndRemarkList);
							}
							
							subAndReceivedTaskListTmp.add(subAndReceivedTaskMap);
						}
					}
					
					resultMap.put("subAndReceivedTaskList", subAndReceivedTaskListTmp);
				}
			}
		}
		return resultMapList;
			
	}
	
	/**
	 * 获取流程图名称
	 * @param reportId	上报id
	 * @param userInfo	操作用户
	 * @param params	额外参数
	 * @return
	 */
	protected String findWorkflowName(Long reportId, UserInfo userInfo, Map<String, Object> params) {
		String reportType = null, workflowName = null;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		
		//后续转功能配置
		if(ReportTypeEnum.petitionPerson.getReportTypeIndex().equals(reportType)) {
			String formTypeId = null;
			if(CommonFunctions.isNotBlank(params, "formTypeId")) {
				formTypeId = params.get("formTypeId").toString();
			}
			if(FocusReportNode36201Enum.FORM_TYPE_ID.toString().equals(formTypeId)) {
				workflowName = "南安信访人员稳控情况反馈子流程";
			}else {
				workflowName = "南安信访人员稳控流程";
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
		if(ReportTypeEnum.petitionPerson.getReportTypeIndex().equals(reportType)) {
			wfTypeId = "focus_report";
		}
		
		return wfTypeId;
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
	@SuppressWarnings({ "serial" })
	protected List<Map<String, Object>> findNextTaskNodes(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> nodeMapList = null;
		boolean isDecisionMaking = false;
		ProInstance proInstance = null;
		String curNodeName = null, formTypeIdStr = null;
		
		params = params == null ? new HashMap<String, Object>() : params;
		
		if((instanceId == null || instanceId <= 0) && CommonFunctions.isNotBlank(params, "instanceId")) {
			instanceId = Long.valueOf(params.get("instanceId").toString());
		}
		
		if(instanceId == null || instanceId <= 0) {
			throw new IllegalArgumentException("缺少有效的流程实例id！");
		}
		
		proInstance = baseWorkflowService.findProByInstanceId(instanceId);
		
		if(proInstance == null) {
			throw new IllegalArgumentException("流程实例id【"+ instanceId +"】没有找到有效的流程信息！");
		}
		
		formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
		
		if(CommonFunctions.isNotBlank(params, "curNodeName")) {
			curNodeName = params.get("curNodeName").toString();
		}
		
		params.put("proInstance", proInstance);
		params.put("isDecisionMaking", isDecisionMaking);
		
		nodeMapList = super.findNextTaskNodes(instanceId, userInfo, params);
		
		if(nodeMapList != null && FocusReportNode362Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			//调整下一办理环节展示名称
			StringBuffer nodeName = new StringBuffer("");
			
			if(FocusReportNode362Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
//				Map<String, String> NODE_MAP = new HashMap<String, String>() {
//					{
//						//现场核实
//						put(FocusReportNode362Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode362Enum.LEAD_NODE_NAME.getTaskNodeName(), "营商牵头部门反馈");
//						put(FocusReportNode362Enum.DISTRIBUTE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode362Enum.MUNICIPAL_NODE_NAME.getTaskNodeName(), "市直部门反馈");
//					}
//				};
//				
//				for(Map<String, Object> nodeMap : nodeMapList) {
//					nodeName.setLength(0);
//					
//					if(CommonFunctions.isNotBlank(nodeMap, "nodeName")) {
//						nodeName.append(curNodeName).append("-").append(nodeMap.get("nodeName").toString());
//					}
//					
//					if(CommonFunctions.isNotBlank(NODE_MAP, nodeName.toString())) {
//						nodeMap.put("nodeNameZH", NODE_MAP.get(nodeName.toString()));
//					}
//				}
			}
		}
		
		return nodeMapList;
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
}
