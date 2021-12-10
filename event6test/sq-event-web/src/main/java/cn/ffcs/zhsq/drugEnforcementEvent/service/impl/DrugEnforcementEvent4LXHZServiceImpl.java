package cn.ffcs.zhsq.drugEnforcementEvent.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.common.DictPcode;
import cn.ffcs.common.message.bo.MsgSendMid;
import cn.ffcs.common.message.bo.MsgSendMidSub;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.crowd.DrugRecord;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.crowd.IDrugRecordService;
import cn.ffcs.shequ.zzgl.service.holidayinfo.IHolidayInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.IMsgSendMidOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.NodeTransition;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.drugEnforcementEvent.service.IDrugEnforcementEventService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.domain.drugEnforcementEvent.DrugEnforcementEvent;
import cn.ffcs.zhsq.mybatis.persistence.drugEnforcementEvent.DrugEnforcementEventMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * @Description: 甘肃省临夏回族自治州(LXHZ) 禁毒事件，序列：SEQ_DRUG_ENFORCEMENT_ID模块服务
 * @Author: zhangls
 * @Date: 07-05 15:43:49
 * @Copyright: 2017 福富软件
 */
@Service("drugEnforcementEvent4LXHZService")
public class DrugEnforcementEvent4LXHZServiceImpl implements IDrugEnforcementEventService {
	@Autowired
	private DrugEnforcementEventMapper drugEnforcementEventMapper;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService; 
	
	@Autowired
	private IDrugRecordService drugRecordService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IAttachmentService attachmentService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IMsgSendMidOutService msgSendMidService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Resource(name = "workflowDecisionMakingService")
	private IWorkflowDecisionMakingService<String> workflowDecisionMakingService;
	
	@Autowired
	private IHolidayInfoService holidayInfoService;
	
	private final String DRUG_ENFORCEMENT_EVENT_WORKFLOW_NAME = "禁毒事件流程",// 禁毒事件流程图名称
			   			 DRUG_ENFORCEMENT_EVENT_WFTYPE_ID = "drug_enforcement_event",//禁毒事件流程类型
			   			 DESCISION_NODE_TYPE = "2",//决策节点环节类型
			   			 DECISION_MAKING_SERVICE = "workflowDecisionMaking4DrugEnforcementEventService",
			   			 ATTACHMENT_TYPE = "DRUG_ENFORCEMENT_EVENT",//附件类型
			   			 MSG_SEND_MID_BIZ_TYPE = "DRUG_ENFORCEMENT_EVENT",//消息中间表业务类型
			   			 DRUG_ENFORCEMENT_EVENT_FORM_TYPE = "drugEnforcementEvent",//禁毒事件用于工作流配置的表单类型，不存在于工作流中的表单管理
			   			 END_NODE_CODE = "end1";//归档环节节点编码
	@Override
	public Long saveOrUpdateDrugEnforcementEvent(
			DrugEnforcementEvent drugEnforcementEvent, UserInfo userInfo, Map<String, Object> extraParam) {
		Long drugEnforcementId = -1L;
		
		if(drugEnforcementEvent != null) {
			int result = 0;
			
			drugEnforcementId = drugEnforcementEvent.getDrugEnforcementId();
			
			if(drugEnforcementId != null && drugEnforcementId > 0) {
				drugEnforcementEvent.setUpdateUser(userInfo.getUserId());
				result = drugEnforcementEventMapper.update(drugEnforcementEvent);
			} else {
				drugEnforcementEvent.setCreateUser(userInfo.getUserId());
				result = drugEnforcementEventMapper.insert(drugEnforcementEvent);
			}
			
			if(result > 0) {
				drugEnforcementId = drugEnforcementEvent.getDrugEnforcementId();
				
				if(drugEnforcementId > 0) {
					Long[] attachmentIds = null;
					
					if(CommonFunctions.isNotBlank(extraParam, "attachmentIds")) {//附件关联业务信息
						Object attachmentIdsObj = extraParam.get("attachmentIds");
						if(attachmentIdsObj instanceof Long[]) {
							attachmentIds = (Long[])attachmentIdsObj;
						} else if(attachmentIdsObj instanceof String) {
							String[] attachmentIdStr = attachmentIdsObj.toString().split(",");
							List<Long> attachmentIdList = new ArrayList<Long>();
							Long attachmentIdL = -1L;
							
							for(String attachmentId : attachmentIdStr) {
								try {
									attachmentIdL = Long.valueOf(attachmentId);
								} catch(NumberFormatException e) {
									e.printStackTrace();
								}
								
								if(attachmentIdL > 0) {
									attachmentIdList.add(attachmentIdL);
								}
							}
							
							if(attachmentIdList.size() > 0) {
								attachmentIds = attachmentIdList.toArray(new Long[attachmentIdList.size()]);
							}
						} else if(attachmentIdsObj instanceof List) {
							List<Object> attachmentIdList = (List<Object>)attachmentIdsObj;
							Long attachmentIdL = -1L;
							
							for(Object attachmentId : attachmentIdList) {
								try {
									attachmentIdL = Long.valueOf(attachmentId.toString());
								} catch(NumberFormatException e) {
									e.printStackTrace();
								}
								
								if(attachmentIdL > 0) {
									attachmentIdList.add(attachmentIdL);
								}
							}
							
							attachmentIds = attachmentIdList.toArray(new Long[attachmentIdList.size()]);
						}
					}
					
					if(attachmentIds != null && attachmentIds.length > 0) {
						attachmentService.updateBizId(drugEnforcementId, ATTACHMENT_TYPE, attachmentIds);
					}
				}
			} else {
				drugEnforcementId = -1L; 
			}
		}
		
		return drugEnforcementId;
	}

	@Override
	public boolean deleteDrugEnforcementEventById(Long drugEnforcementId, Long delUserId) {
		boolean flag = false;
		
		if(drugEnforcementId != null && drugEnforcementId > 0) {
			flag = drugEnforcementEventMapper.delete(drugEnforcementId, delUserId) > 0;
		}
		
		return flag;
	}

	@Override
	public DrugEnforcementEvent findDrugEnforcementEventById(
			Long drugEnforcementId, String orgCode) {
		DrugEnforcementEvent drugEnforcementEvent = null;
		
		if(drugEnforcementId != null && drugEnforcementId > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("drugEnforcementId", drugEnforcementId);
			params.put("statusArray", new String[] {IDrugEnforcementEventService.STATUS.DRAFT.getStatus(), IDrugEnforcementEventService.STATUS.HANDLING.getStatus(), IDrugEnforcementEventService.STATUS.HANDLED.getStatus()});
			
			drugEnforcementEvent = drugEnforcementEventMapper.findById(params);
			
			formatDataOut(drugEnforcementEvent, orgCode);
		}
		
		return drugEnforcementEvent;
	}

	@Override
	public int findDrugEnforcementEventCount(Map<String, Object> params, UserInfo userInfo) {
		Integer listType = 1;
		int count = 0;
		
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		this.findParams4DrugEnforcementEvent(params, userInfo);
		
		switch(listType) {
			case 1 : {//草稿
				count = drugEnforcementEventMapper.findCountByCriteria(params);
				
				break;
			}
			case 3 : {//辖区所有
				count = drugEnforcementEventMapper.findCountByCriteria(params);
				
				break;
			}
			case 2 : {//待办
				count = drugEnforcementEventMapper.findTodoCountByCriteria(params);
				
				break;
			}
			case 4 : {//经办
				count = drugEnforcementEventMapper.findHandledCountByCriteria(params);
				
				break;
			}
		}
		
		return count;
	}
	
	@Override
	public EUDGPagination findDrugEnforcementEventPagination(int pageNo,
			int pageSize, UserInfo userInfo, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		Integer listType = 1;
		int count = 0;
		List<DrugEnforcementEvent> drugEnforcementEventList = new ArrayList<DrugEnforcementEvent>();
		String userOrgCode = null;
		
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		this.findParams4DrugEnforcementEvent(params, userInfo);
		
		if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		}
		
		switch(listType) {
			case 1 : {//草稿
				count = drugEnforcementEventMapper.findCountByCriteria(params);
				if(count > 0) {
					drugEnforcementEventList = drugEnforcementEventMapper.findPageListByCriteria(params, rowBounds);
				}
				
				break;
			}
			case 3 : {//辖区所有
				count = drugEnforcementEventMapper.findCountByCriteria(params);
				if(count > 0) {
					drugEnforcementEventList = drugEnforcementEventMapper.findPageListByCriteria(params, rowBounds);
				}
				
				break;
			}
			case 2 : {//待办
				count = drugEnforcementEventMapper.findTodoCountByCriteria(params);
				if(count > 0) {
					drugEnforcementEventList = drugEnforcementEventMapper.findTodoListByCriteria(params, rowBounds);
				}
				
				break;
			}
			case 4 : {//经办
				count = drugEnforcementEventMapper.findHandledCountByCriteria(params);
				if(count > 0) {
					drugEnforcementEventList = drugEnforcementEventMapper.findHandledListByCriteria(params, rowBounds);
				}
				
				break;
			}
		}
		
		formatDataOut(drugEnforcementEventList, userOrgCode);
		
		EUDGPagination drugEnforcementEventPagination = new EUDGPagination(count, drugEnforcementEventList);
		
		return drugEnforcementEventPagination;
	}
	
	/**
	 * 构建禁毒事件列表查询条件
	 * 
	 * @param params
	 * 			listType		列表类型 1 草稿；2 待办；3 辖区所有；默认为1
	 * 			userOrgCode		组织编码，为空时，使用userInfo中的orgCode
	 * 			infoOrgCode		地域编码
	 * 			startInfoOrgCode默认地域编码，当前infoOrgCode为空时，辖区所有列表使用该参数
	 * 			workflowName	流程图名称，默认为 禁毒事件流程
	 * 			wfTypeId		流程图类型编码，默认为 drug_enforcement_event
	 * 			statusArray		状态，类型为String[]，优先于status生效，该参数只对经办列表、辖区所有列表有效
	 * 			status			状态，该参数只对经办列表、辖区所有列表有效
	 * 			handleDateStatus处置情况，01 正常；02 超时
	 * 			createDateStart	采集开始时间，字符串，格式为：yyyy-mm-dd
	 * 			createDateEnd	采集结束时间，字符串，格式为：yyyy-mm-dd
	 * 
	 * @param userInfo
	 * 			userId	
	 * 			orgId
	 * 			orgCode
	 * @return
	 */
	private Map<String, Object> findParams4DrugEnforcementEvent(Map<String, Object> params, UserInfo userInfo) {
		Integer listType = 1;
		String userOrgCode = "", orgCode = "";
		Long userId = -1L, orgId = -1L;
		String[] statusArray = null;
		
		if(userInfo != null) {
			userId = userInfo.getUserId();
			orgId = userInfo.getOrgId();
			orgCode = userInfo.getOrgCode();
		}
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		}
		if(StringUtils.isBlank(userOrgCode) && StringUtils.isNotBlank(orgCode)) {
			userOrgCode = orgCode;
		}
		if(CommonFunctions.isNotBlank(params, "statusArray")) {
			try {
				statusArray = (String[])params.get("statusArray");
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if(CommonFunctions.isNotBlank(params, "status")) {
			statusArray = new String[] {params.get("status").toString()};
		}
		
		if(statusArray != null) {
			params.put("statusArray", statusArray);
		}
		
		if(StringUtils.isNotBlank(userOrgCode)) {
			params.put("userOrgCode", userOrgCode);
		}
		
		switch(listType) {
			case 1 : {//草稿
				params.put("createUser", userId);
				params.put("statusArray", new String[] {IDrugEnforcementEventService.STATUS.DRAFT.getStatus()});
				
				break;
			}
			case 3 : {//辖区所有
				if(CommonFunctions.isBlank(params, "statusArray")) {
					params.put("statusArray", new String[] {IDrugEnforcementEventService.STATUS.HANDLING.getStatus(), IDrugEnforcementEventService.STATUS.HANDLED.getStatus()});
				}
				
				if(CommonFunctions.isBlank(params, "infoOrgCode") && CommonFunctions.isNotBlank(params, "startInfoOrgCode")) {
					params.put("infoOrgCode", params.get("startInfoOrgCode"));
				}
				
				break;
			}
			case 2 : {//待办
				Long curUserId = -1L, curOrgId = -1L;
				if(CommonFunctions.isNotBlank(params, "curUserId")) {
					try {
						curUserId = Long.valueOf(params.get("curUserId").toString());
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
				}
				if(CommonFunctions.isNotBlank(params, "curOrgId")) {
					try {
						curOrgId = Long.valueOf(params.get("curOrgId").toString());
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
				}
				
				if(curUserId < 0 && userId > 0) {
					curUserId = userId;
				}
				if(curOrgId < 0 && orgId > 0) {
					curOrgId = orgId;
				}
				
				params.put("statusArray", new String[] {IDrugEnforcementEventService.STATUS.HANDLING.getStatus()});
				params.put("curUserId", curUserId.toString());
				params.put("curOrgId", curOrgId.toString());
				params.put("workflowName", capWorkflowName4DrugEnforement(params, userInfo));
				
				if(CommonFunctions.isBlank(params, "wfTypeId")) {
					params.put("wfTypeId", DRUG_ENFORCEMENT_EVENT_WFTYPE_ID);
				}
				
				break;
			}
			case 4 : {//经办
				Long handledUserId = -1L, handledOrgId = -1L;
				
				if(CommonFunctions.isNotBlank(params, "handledUserId")) {
					try {
						handledUserId = Long.valueOf(params.get("handledUserId").toString());
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
				}
				if(CommonFunctions.isNotBlank(params, "handledOrgId")) {
					try {
						handledOrgId = Long.valueOf(params.get("handledOrgId").toString());
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
				}
				
				if(handledUserId < 0 && userId > 0) {
					handledUserId = userId;
				}
				if(handledOrgId < 0 && orgId > 0) {
					handledOrgId = orgId;
				}
				
				if(CommonFunctions.isBlank(params, "statusArray")) {
					params.put("statusArray", new String[] {IDrugEnforcementEventService.STATUS.HANDLING.getStatus(), IDrugEnforcementEventService.STATUS.HANDLED.getStatus()});
				}
				params.put("handledUserId", handledUserId);
				params.put("handledOrgId", handledOrgId);
				params.put("workflowName", capWorkflowName4DrugEnforement(params, userInfo));
				
				if(CommonFunctions.isBlank(params, "wfTypeId")) {
					params.put("wfTypeId", DRUG_ENFORCEMENT_EVENT_WFTYPE_ID);
				}
				
				break;
			}
		}
		
		return params;
	}
	
	@Override
	public boolean reportDrugEnforcementEvent(Long drugEnforcementId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		Long instanceId = -1L;
		int HANDLE_DATE_LIMIT = 15;//处置时限默认为15个工作日
		boolean flag = isUserAbleToStart(userInfo);
		
		if(flag && drugEnforcementId != null && drugEnforcementId > 0) {
			Map<String, Object> startParam = new HashMap<String, Object>();
			startParam.put("decisionService", capDecisionMakingService4DrugEnforement(extraParam, userInfo));
			
			instanceId = baseWorkflowService.startWorkflow4Base(capWorkflowName4DrugEnforement(extraParam, userInfo), DRUG_ENFORCEMENT_EVENT_WFTYPE_ID, drugEnforcementId, userInfo, startParam);
			if(instanceId != null && instanceId > 0) {
				DrugEnforcementEvent drugEnforcementEvent = new DrugEnforcementEvent();
				drugEnforcementEvent.setDrugEnforcementId(drugEnforcementId);
				drugEnforcementEvent.setUpdateUser(userInfo.getUserId());
				drugEnforcementEvent.setHandleDate(holidayInfoService.getWorkDateByAfterWorkDay(new Date(), HANDLE_DATE_LIMIT));
				
				flag = drugEnforcementEventMapper.reportDrugEnforcementEvent(drugEnforcementEvent) > 0;
				
				if(flag) {//自动扭转过决策节点
					String nextNodeName = null;
					
					List<Map<String, Object>> nextNodes = findNextTaskNodes(drugEnforcementId, userInfo);
					if(nextNodes != null && nextNodes.size() > 0) {
						Map<String, Object> nextNodeMap = nextNodes.get(0);
						
						if(CommonFunctions.isNotBlank(nextNodeMap, "nodeName")) {
							nextNodeName = nextNodeMap.get("nodeName").toString();
						}
					}
					
					if(StringUtils.isNotBlank(nextNodeName)) {
						boolean isClose = false;
						Map<String, Object> subMap = new HashMap<String, Object>();
						
						if(CommonFunctions.isNotBlank(extraParam, "isClose")) {
							isClose = ConstantValue.WORKFLOW_IS_TO_COLSE.equals(extraParam.get("isClose").toString());
						}

						subMap.putAll(extraParam);
						
						if(isClose && CommonFunctions.isNotBlank(subMap, "advice")) {
							subMap.remove("advice");
						}
						
						flag = this.subWorkflow(drugEnforcementId, nextNodeName, null, userInfo, subMap);
						
						if(flag && isClose) {
							flag = baseWorkflowService.endWorkflow4Base(instanceId, userInfo, extraParam);
							
							if(flag) {
								archiveDrugEnforcement(drugEnforcementId, userInfo, null);
							}
						}
					}
				}
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean subWorkflow(Long drugEnforcementId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		String nextUserIds = null, nextOrgIds = null;
		
		if(drugEnforcementId == null || drugEnforcementId < 0) {
			throw new IllegalArgumentException("缺少禁毒事件id！");
		}
		if(StringUtils.isBlank(nextNodeName)) {
			throw new IllegalArgumentException("缺少可办理的下一环节！");
		}
		if(!this.isCurTaskPaticipant(drugEnforcementId, userInfo, extraParam)) {
			Map<String, Object> curTaskData = this.findCurTaskData(drugEnforcementId, userInfo);
			String msgWrong = "";
			
			if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
				msgWrong = "用户["+ userInfo.getOrgName() +"-"+ userInfo.getPartyName() +"] 不是当前环节[" + curTaskData.get("WF_ACTIVITY_NAME_") + "] 的办理人员！";
			} else {
				msgWrong = "任务已完结！";
			}
			
			throw new Exception(msgWrong);
		}
		
		if(nextUserInfoList == null) {
			nextUserInfoList = new ArrayList<UserInfo>();
		}
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
			
			for(int index = 0, len = nextUserArray.length; index < len; index++) {
				nextUserIdStr = nextUserArray[index];
				nextOrgIdStr = nextOrgIdArray[index];
				
				if(StringUtils.isNotBlank(nextUserIdStr)) {
					nextUserId = Long.valueOf(nextUserIdStr);
				}
				if(StringUtils.isNotBlank(nextOrgIdStr)) {
					nextOrgId = Long.valueOf(nextOrgIdStr);
				}
				if(nextUserId > 0 || nextOrgId > 0) {
					nextUserInfo = new UserInfo();
					nextUserInfo.setUserId(nextUserId);
					nextUserInfo.setOrgId(nextOrgId);
					nextUserInfoList.add(nextUserInfo);
				}
			}
		}
		
		if(nextUserInfoList.size() == 0) {
			if(userInfo != null) {
				nextUserInfoList = new ArrayList<UserInfo>();
				nextUserInfoList.add(userInfo);
			} else {
				throw new IllegalArgumentException("缺少下一环节的可办理人员！");
			}
		}
		
		flag = baseWorkflowService.subWorkflow4Base(capWorkflowName4DrugEnforement(extraParam, userInfo), DRUG_ENFORCEMENT_EVENT_WFTYPE_ID, drugEnforcementId, nextNodeName, nextUserInfoList, userInfo, extraParam);
		
		if(flag) {//上报成功后，发送短信
			if(END_NODE_CODE.equals(nextNodeName)) {
				archiveDrugEnforcement(drugEnforcementId, userInfo, null);
			}
			
			this.sendSMS(userInfo, extraParam);
		}
		
		return flag;
	}
	
	@Override
	public boolean rejectWorkflow(Long drugEnforcementId, String rejectToNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		
		if(drugEnforcementId == null || drugEnforcementId < 0) {
			throw new IllegalArgumentException("缺少禁毒事件id！");
		}
		if(!this.isCurTaskPaticipant(drugEnforcementId, userInfo, extraParam)) {
			Map<String, Object> curTaskData = this.findCurTaskData(drugEnforcementId, userInfo);
			String msgWrong = "";
			
			if(CommonFunctions.isNotBlank(curTaskData, "WF_ACTIVITY_NAME_")) {
				msgWrong = "用户["+ userInfo.getOrgName() +"-"+ userInfo.getPartyName() +"] 不是当前环节[" + curTaskData.get("WF_ACTIVITY_NAME_") + "] 的办理人员！";
			} else {
				msgWrong = "任务已完结！";
			}
			
			throw new Exception(msgWrong);
		}
		
		flag = baseWorkflowService.rejectWorkflow4Base(capWorkflowName4DrugEnforement(extraParam, userInfo), DRUG_ENFORCEMENT_EVENT_WFTYPE_ID, drugEnforcementId, rejectToNodeName, userInfo, extraParam);
		
		return flag;
	}
	
	@Override
	public List<Map<String, Object>> findNextTaskNodes(Long drugEnforcementId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		List<Node> nextNodeList = null;
		List<Map<String, Object>> nextNodeMapList = null;
		
		if(drugEnforcementId == null || drugEnforcementId < 0) {
			throw new IllegalArgumentException("缺少禁毒事件id！");
		}
		
		if(params == null || params.isEmpty()) {
			nextNodeMapList = this.findNextTaskNodes(drugEnforcementId, userInfo);
		} else {
			String workflowName = capWorkflowName4DrugEnforement(params, userInfo);
			nextNodeList = baseWorkflowService.findNextTaskNodes(workflowName, DRUG_ENFORCEMENT_EVENT_WFTYPE_ID, drugEnforcementId, userInfo, params);
			
			if(nextNodeList != null) {
				nextNodeMapList = new ArrayList<Map<String, Object>>();
				Map<String, Object> nextNodeMap = null;
				
				if(nextNodeList.size() == 1) {
					Node nextNode = nextNodeList.get(0);
					Node nextTaskNode = nextNodeList.get(0);
					
					if(DESCISION_NODE_TYPE.equals(nextNode.getNodeType())) {//下一节点为决策节点
						String nextNodeCode = "";
						
						params.put("curNodeCode", nextNode.getNodeName());
						nextNodeCode = workflowDecisionMakingService.makeDecision(params);
						
						if(StringUtils.isNotBlank(nextNodeCode)) {
							nextTaskNode = baseWorkflowService.capNodeInfo(workflowName, DRUG_ENFORCEMENT_EVENT_WFTYPE_ID, drugEnforcementId, nextNodeCode, userInfo);
							
							if(nextTaskNode != null) {
								NodeTransition nodeTransition = baseWorkflowService.capNodeTransition(nextNode.getNodeId(), nextTaskNode.getNodeId());
								if(nodeTransition != null) {
									nextTaskNode.setTransitionCode(nextNode.getFromCode() + nodeTransition.getType() + nodeTransition.getLevel() + nextTaskNode.getToCode());
								}
							}
						}
					}
					
					nextNodeList.clear();
					
					nextNodeList.add(nextTaskNode);
				}
				
				for(Node nextNode : nextNodeList) {
					nextNodeMap = new HashMap<String, Object>();
					
					nextNodeMap.put("nodeId", nextNode.getNodeId());
					nextNodeMap.put("nodeName", nextNode.getNodeName());
					nextNodeMap.put("nodeNameZH", nextNode.getNodeNameZH());
					nextNodeMap.put("nodeType", nextNode.getNodeType());
					nextNodeMap.put("transitionCode", nextNode.getTransitionCode());
					nextNodeMap.put("dynamicSelect", nextNode.getDynamicSelect());
					
					nextNodeMapList.add(nextNodeMap);
				}
			}
		}
		
		return nextNodeMapList;
	}
	
	@Override
	public List<Map<String, Object>> findNextTaskNodes(Long drugEnforcementId, UserInfo userInfo) throws Exception {
		List<Map<String, Object>> nextNodeMapList = null;
		Map<String, Object> param = new HashMap<String, Object>();
		Long orgId = -1L, userId = -1L;
		
		if(drugEnforcementId == null || drugEnforcementId < 0) {
			throw new IllegalArgumentException("缺少禁毒事件id！");
		}
		if(userInfo != null) {
			orgId = userInfo.getOrgId();
			userId = userInfo.getUserId();
		}
		
		param.put("decisionService", capDecisionMakingService4DrugEnforement(null, userInfo));
		param.put("curOrgId", orgId);
		param.put("curUserId", userId);
		
		nextNodeMapList = this.findNextTaskNodes(drugEnforcementId, userInfo, param);
		
		return nextNodeMapList;
	}
	
	@Override
	public Map<String, Object> findCurTaskData(Long drugEnforcementId, UserInfo userInfo) throws Exception {
		Map<String, Object> curTaskData = null;
		
		if(drugEnforcementId != null && drugEnforcementId > 0) {
			curTaskData = baseWorkflowService.findCurTaskData(capWorkflowName4DrugEnforement(null, userInfo), DRUG_ENFORCEMENT_EVENT_WFTYPE_ID, drugEnforcementId, userInfo);
		}
		
		return curTaskData;
	}
	
	@Override
	public List<Map<String, Object>> findParticipantsByTaskId(Long taskId) {
		List<Map<String, Object>> participantMapList = null;
		
		if(taskId != null && taskId > 0){
			participantMapList = baseWorkflowService.findParticipantsByTaskId(taskId);
		}
		
		if(participantMapList != null && participantMapList.size() > 0) {
			for(Map<String, Object> participantMap : participantMapList) {
				if(CommonFunctions.isNotBlank(participantMap, "USER_TYPE")) {
					String userType = participantMap.get("USER_TYPE").toString();
					
					if(IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userType)) {
						Object userIdObj = participantMap.get("USER_ID");
						if(userIdObj != null){
							Long userId = Long.valueOf(userIdObj.toString());
							UserBO user = userManageService.getUserInfoByUserId(userId);
							
							if(user != null) {
								participantMap.put("USER_NAME", user.getPartyName());
							}
						}
					}
				}
				
				if(CommonFunctions.isNotBlank(participantMap, "ORG_ID")) {
					Long orgId = Long.valueOf(participantMap.get("ORG_ID").toString());
					OrgSocialInfoBO orgEntity = orgSocialInfoService.findByOrgId(orgId);
					if(orgEntity != null) {
						participantMap.put("ORG_NAME", orgEntity.getOrgName());
					}
				}
			}
		}
		
		return participantMapList;
	}
	
	@Override
	public boolean isCurTaskPaticipant(Long drugEnforcementId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		Long taskId = -1L;
		Map<String, Object>	curTaskData = this.findCurTaskData(drugEnforcementId, userInfo);
		
		if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
			taskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
			
			List<Map<String, Object>> participantMapList = this.findParticipantsByTaskId(taskId);
			
			flag = this.isCurTaskPaticipant(participantMapList, userInfo, extraParam);
		} else {
			throw new Exception("任务已完结！");
		}
		
		return flag;
	}
	
	@Override
	public boolean isCurTaskPaticipant(List<Map<String, Object>> participantMapList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		
		if(participantMapList != null && participantMapList.size() > 0) {
			Long userId = -1L, orgId = -1L;
			
			if(userInfo != null) {
				userId = userInfo.getUserId();
				orgId = userInfo.getOrgId();
			}
			
			for(Map<String, Object> participantMap : participantMapList) {
				Long curTaskUserId = -1L, curTaskOrgId = -1L;
				String userType = "";//1表示USER_ID为组织ID；3表示USER_ID为用户ID
				
				if(CommonFunctions.isNotBlank(participantMap, "USER_ID")) {
					curTaskUserId = Long.valueOf(participantMap.get("USER_ID").toString());
				}
				if(CommonFunctions.isNotBlank(participantMap, "USER_TYPE")) {
					userType = participantMap.get("USER_TYPE").toString();
				}
				if(CommonFunctions.isNotBlank(participantMap, "ORG_ID")) {
					curTaskOrgId =  Long.valueOf(participantMap.get("ORG_ID").toString());
				}
				
				//当前办理人要相同的用户和相同的组织
				if(IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userType)) {
					flag = userId.equals(curTaskUserId) && orgId.equals(curTaskOrgId);
				} else if(IEventDisposalWorkflowService.ACTOR_TYPE_ORG.equals(userType)) {
					flag = orgId.equals(curTaskUserId);
				}
				
				if(flag) {
					break;
				}
				
			}
		}
		
		return flag;
	}
	
	@Override
	public Long capInstanceId(Long drugEnforcementId, UserInfo userInfo) throws Exception {
		Long instanceId = -1L;
		
		if(drugEnforcementId != null && drugEnforcementId > 0) {
			instanceId = baseWorkflowService.capInstanceId(capWorkflowName4DrugEnforement(null, userInfo), DRUG_ENFORCEMENT_EVENT_WFTYPE_ID, drugEnforcementId, userInfo);
		}
		
		return instanceId;
	}
	
	@Override
	public Map<String, Object> capProInstance(Long instanceId) {
		ProInstance pro = null;
		Map<String, Object> proMap = null;
		
		if(instanceId != null && instanceId > 0) {
			pro = baseWorkflowService.findProByInstanceId(instanceId);
			
			if(pro != null) {
				proMap = new HashMap<String, Object>();
				
				proMap.put("instanceId", pro.getInstanceId());
				proMap.put("userId", pro.getUserId());
				proMap.put("userName", pro.getUserName());
				proMap.put("orgId", pro.getOrgId());
				proMap.put("orgName", pro.getOrgName());
			}
		}
		
		return proMap;
	}
	
	@Override
	public Map<String, Object> capProInstance(Long drugEnforcementId, UserInfo userInfo) throws Exception {
		ProInstance pro = null;
		Map<String, Object> proMap = null;
		
		if(drugEnforcementId != null && drugEnforcementId > 0 && userInfo != null) {
			pro = baseWorkflowService.capProInstance(capWorkflowName4DrugEnforement(null, userInfo), DRUG_ENFORCEMENT_EVENT_WFTYPE_ID, drugEnforcementId, userInfo);
			
			if(pro != null) {
				proMap = new HashMap<String, Object>();
				
				proMap.put("instanceId", pro.getInstanceId());
				proMap.put("userId", pro.getUserId());
				proMap.put("userName", pro.getUserName());
				proMap.put("orgId", pro.getOrgId());
				proMap.put("orgName", pro.getOrgName());
			}
		}
		
		return proMap;
	}
	
	@Override
	public List<Map<String, Object>> findOperateByNodeId(Integer nodeId) {
		List<OperateBean> operateList = null;
		List<Map<String, Object>> operateMapList = null;
		
		if(nodeId != null && nodeId > 0) {
			operateList = baseWorkflowService.findOperateByNodeId(nodeId);
		}
		
		if(operateList != null && operateList.size() > 0) {
			operateMapList = new ArrayList<Map<String, Object>>();
			Map<String, Object> operateMap = null;
			
			for(OperateBean operate : operateList) {
				operateMap = new HashMap<String, Object>();
				
				operateMap.put("operateEvent", operate.getOperateEvent());
				operateMap.put("anotherName", operate.getAnotherName());
				
				operateMapList.add(operateMap);
			}
		}
		
		return operateMapList;
	}
	
	@Override
	public String capSMSContent(Long drugEnforcementId, String curNodeName, String nextNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		DrugEnforcementEvent drugEnforcementEvent = null;
		String contentType = "", orgCode = null, gridPath = "";
		
		if(CommonFunctions.isNotBlank(extraParam, "gridPath")) {
			gridPath = extraParam.get("gridPath").toString();
		}
		
		if(drugEnforcementId != null && drugEnforcementId > 0) {
			if(userInfo != null) {
				orgCode = userInfo.getOrgCode();
			}
			
			if(StringUtils.isBlank(orgCode) && CommonFunctions.isNotBlank(extraParam, "userOrgCode")) {
				orgCode = extraParam.get("userOrgCode").toString();
			}
			
			drugEnforcementEvent = this.findDrugEnforcementEventById(drugEnforcementId, orgCode);
			
			if(drugEnforcementEvent == null) {
				drugEnforcementEvent = new DrugEnforcementEvent();
			}
			
			if(StringUtils.isBlank(gridPath)) {
				gridPath = drugEnforcementEvent.getAddictGridPath();
			}
		}
		
		if(CommonFunctions.isNotBlank(extraParam, "contentType")) {
			contentType = extraParam.get("contentType").toString();
		} else {
			contentType = capSMSContentType(curNodeName, nextNodeName);
		}
		
		String smsContent = funConfigurationService.changeCodeToValue(ConstantValue.SMS_NOTE_TYPE, contentType, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);//发送短信内容
		
		if(StringUtils.isNotBlank(smsContent)) {
			if(smsContent.contains("@daySpan")) {
				Long daySpan = null;
				
				if(CommonFunctions.isNotBlank(extraParam, "daySpan")) {
					daySpan = Long.valueOf(extraParam.get("daySpan").toString());
				} else {
					Node nextNode = baseWorkflowService.capNodeInfo(capWorkflowName4DrugEnforement(extraParam, userInfo), DRUG_ENFORCEMENT_EVENT_WFTYPE_ID, drugEnforcementId, nextNodeName, userInfo);
					
					if(nextNode != null) {
						daySpan = nextNode.getTimeLimit();
					}
				}
				
				if(daySpan != null) {
					smsContent = smsContent.replace("@daySpan", daySpan.toString());
				}
			}
			
			if(smsContent.contains("@advice")) {
				String advice = "";
				
				if(CommonFunctions.isNotBlank(extraParam, "advice")) {
					advice = extraParam.get("advice").toString();
				}
				
				smsContent = smsContent.replace("@advice", advice);
			}
			
			smsContent = smsContent.replace("@addictName", drugEnforcementEvent.getAddictName())
								   .replace("@gridPath", gridPath)
								   .replace("@addictGridPath", drugEnforcementEvent.getAddictGridPath());
			
		}
		
		return smsContent;
	}
	
	@Override
	public boolean sendSMS(UserInfo userInfo, Map<String, Object> params) throws Exception {
		boolean isAutoSendSms = false, reslutFlag = false, isCheckDuplication = false;
		String smsContent = null, curNodeName = null, nextNodeName = null,
			   orgCode = null, msgSendMidBizType = null;
		Long drugEnforcementId = -1L, msgSendMidBizId = -1L;
		
		if(userInfo != null) {
			orgCode = userInfo.getOrgCode();
		}
		if(CommonFunctions.isNotBlank(params, "isAutoSendSms")) {
			isAutoSendSms = Boolean.valueOf(params.get("isAutoSendSms").toString());
		}
		if(CommonFunctions.isNotBlank(params, "smsContent")) {
			smsContent = params.get("smsContent").toString();
		}
		if(CommonFunctions.isNotBlank(params, "formId")) {
			drugEnforcementId = Long.valueOf(params.get("formId").toString());
		}
		if(CommonFunctions.isNotBlank(params, "curNodeName")) {
			curNodeName = params.get("curNodeName").toString();
		}
		if(CommonFunctions.isNotBlank(params, "nextNodeName")) {
			nextNodeName = params.get("nextNodeName").toString();
		}
		if(StringUtils.isBlank(orgCode) && CommonFunctions.isNotBlank(params, "userOrgCode")) {
			orgCode = params.get("userOrgCode").toString();
		}
		if(CommonFunctions.isNotBlank(params, "isCheckDuplication")) {
			isCheckDuplication = Boolean.valueOf(params.get("isCheckDuplication").toString());
		}
		if(CommonFunctions.isNotBlank(params, "msgSendMidBizId")) {
			msgSendMidBizId = Long.valueOf(params.get("msgSendMidBizId").toString());
		} else {
			msgSendMidBizId = drugEnforcementId;
		}
		if(CommonFunctions.isNotBlank(params, "msgSendMidBizType")) {
			msgSendMidBizType = params.get("msgSendMidBizType").toString();
		} else {
			msgSendMidBizType = MSG_SEND_MID_BIZ_TYPE;
		}
		
		if(isAutoSendSms && StringUtils.isBlank(smsContent)) {
			smsContent = this.capSMSContent(drugEnforcementId, curNodeName, nextNodeName, userInfo, params);
		}
		
		if(StringUtils.isNotBlank(smsContent)) {
			List<MsgSendMidSub> msgSendMidSubList = new ArrayList<MsgSendMidSub>();
			String otherMobileNums = null;
			Set<String> phoneNumSet = new HashSet<String>();//接收短信的手机号码，用于去重
			Map<String, String> phoneNumMap = new HashMap<String, String>();//手机号码-人员姓名
			
			if(isAutoSendSms && CommonFunctions.isBlank(params, "smsReceiveUserIds")) {//自动发送短信，获取当前办理人员
				Map<String, Object> curTaskData = this.findCurTaskData(drugEnforcementId, userInfo);
				Long taskId = -1L;
				
				if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
					taskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
				}
				
				List<Map<String, Object>> participantMapList = this.findParticipantsByTaskId(taskId);
				if(participantMapList != null) {
					StringBuffer smsReceiveUserIds = new StringBuffer("");
					
					for(Map<String, Object> participant : participantMapList){
						if(CommonFunctions.isNotBlank(participant, "USER_ID")) {
							smsReceiveUserIds.append(",").append(participant.get("USER_ID"));
						}
					}
					
					if(smsReceiveUserIds.length() > 0) {
						params.put("smsReceiveUserIds", smsReceiveUserIds.substring(1));
					}
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "smsReceiveUserIds")) {
				String[] receiveUserIds = params.get("smsReceiveUserIds").toString().split(",");
				Long userId = null, verifyMobile = null;
				UserBO user = null;
				
				for(String receiveUserId : receiveUserIds) {
					try {
						userId = Long.valueOf(receiveUserId);
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
					if(userId != null && userId > 0) {
						user = userManageService.getUserInfoByUserId(userId);
						if(user != null) {
							verifyMobile = user.getVerifyMobile();
							if(verifyMobile != null) {
								phoneNumSet.add(verifyMobile.toString());
								phoneNumMap.put(verifyMobile.toString(), user.getPartyName());
							}
						}
					}
				}
			}
			if(CommonFunctions.isNotBlank(params, "otherMobileNums")) {
				otherMobileNums = params.get("otherMobileNums").toString();
				String[] phoneNumArray = otherMobileNums.split(",");
				
				for(String phoneNum : phoneNumArray) {
					phoneNumSet.add(phoneNum);
				}
			}
			
			if(!phoneNumSet.isEmpty()) {
				String msgSendMidSubStatus = null,
					   msgSendMidSendType = null;
				
				
				MsgSendMid msgSendMid = new MsgSendMid();
				MsgSendMidSub msgSendMidSub = null;
				
				msgSendMid.setBizId(msgSendMidBizId);
				msgSendMid.setBizType(msgSendMidBizType);
				
				if(CommonFunctions.isNotBlank(params, "msgSendMidSendType")) {
					msgSendMidSendType = params.get("msgSendMidSendType").toString();
				} else {
					msgSendMidSendType = this.capSMSContentType(curNodeName, nextNodeName);
				}
				
				msgSendMid.setSendType(msgSendMidSendType);
				
				if(CommonFunctions.isNotBlank(params, "isMsgSendMidSubValid")) {
					boolean isMsgSendMidSubValid = Boolean.valueOf(params.get("isMsgSendMidSubValid").toString());
					
					if(isMsgSendMidSubValid) {
						msgSendMidSubStatus = MsgSendMidSub.STATUS.INITIAL.toString();
					} else {
						msgSendMidSubStatus = MsgSendMidSub.STATUS.INVALID.toString();
					}
				}
				for(String phoneNum : phoneNumSet) {
					msgSendMidSub = new MsgSendMidSub();
					msgSendMidSub.setTargetType(MsgSendMidSub.TARGET_TYPE.SMS.toString());
					msgSendMidSub.setTargetFlag(phoneNum);
					msgSendMidSub.setFlagType(MsgSendMidSub.FLAG_TYPE.MOBILE_PHONE_NUM.toString());
					msgSendMidSub.setSendMsg(smsContent);
					msgSendMidSub.setOrgCode(orgCode);
					msgSendMidSub.setTargetFlagName(phoneNumMap.get(phoneNum));
					msgSendMidSub.setStatus(msgSendMidSubStatus);
					
					msgSendMidSubList.add(msgSendMidSub);
				}
				
				Map<String, Object> extraParam = new HashMap<String, Object>();
				extraParam.put("isCheckDuplication", isCheckDuplication);
				
				reslutFlag = msgSendMidService.saveMsgSendMid(msgSendMid, msgSendMidSubList, extraParam) > 0;
			}
		}
		
		return reslutFlag;
	}
	
	/**
	 * 判断用户是否可以启动流程
	 * @param userInfo
	 * @throws Exception
	 */
	protected boolean isUserAbleToStart(UserInfo userInfo) throws Exception {
		boolean flag = false;
		
		if(userInfo != null) {
			Long orgId = userInfo.getOrgId();
			
			if(orgId != null && orgId > 0) {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
				if(orgInfo != null) {
					String chiefLevel = orgInfo.getChiefLevel(),
						   orgType = orgInfo.getOrgType(),
						   orgCode = orgInfo.getOrgCode(),
						   professionCode = orgInfo.getProfessionCode(),
						   msgWrong = null,
						   ORG_TYPE_UNIT = String.valueOf(ConstantValue.ORG_TYPE_UNIT),			//组织类型 单位
						   ORG_TYPE_DEPARTMENT = String.valueOf(ConstantValue.ORG_TYPE_DEPT);	//组织类型 部门
					
					if(ORG_TYPE_UNIT.equals(orgType)) {
						if(!ConstantValue.COMMUNITY_ORG_LEVEL.equals(chiefLevel) && !ConstantValue.GRID_ORG_LEVEL.equals(chiefLevel)) {
							msgWrong = "该用户的组织层级既不是网格级也不是社区级！";
						} else if(ConstantValue.GRID_ORG_LEVEL.equals(chiefLevel) && !fiveKeyElementService.isUserIdGridAdmin(orgId, userInfo.getUserId())) {
							msgWrong = "该用户不是网格管理员！";
						}
					} else if(ORG_TYPE_DEPARTMENT.equals(orgType)) {
						if(ConstantValue.STREET_ORG_LEVEL.equals(chiefLevel) || ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevel)) {
							String professionCodes = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.PROFESSION_CODE_4_START, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
							if(StringUtils.isNotBlank(professionCodes)) {
								String[] professionCodeArray = professionCodes.split(";"),
										 professionCodeItemArray = null;
								StringBuffer professionName = new StringBuffer("");
								
								for(String professionCodeItem : professionCodeArray) {
									if(StringUtils.isNotBlank(professionCodeItem)) {
										professionCodeItemArray = professionCodeItem.split(",");
										if(professionCodeItemArray.length > 0 && professionCodeItemArray[0].equals(professionCode)) {
											flag = true; break;
										} else if(professionCodeItemArray.length > 1) {
											professionName.append(professionCodeItemArray[1]).append(";");
										}
									}
								}
								
								if(!flag) {
									msgWrong = "该用户的组织专业不在如下专业中：" + professionName;
								}
							}
						} else {
							msgWrong = "该用户的组织层级既不是街道级也不是区县级！";
						}
					}
					
					if(StringUtils.isNotBlank(msgWrong)) {
						throw new Exception("该用户不能提交流程！" + msgWrong);
					} else {
						flag = true;
					}
				}
			}
		}
		
		return flag;
	}
	
	/**
	 * 格式化输出数据
	 * @param drugEnforcementEvent
	 * @param orgCode	组织编码
	 */
	private void formatDataOut(DrugEnforcementEvent drugEnforcementEvent, String orgCode) {
		if(drugEnforcementEvent != null) {
			List<BaseDataDict> drugVarDict = baseDictionaryService.getDataDictListOfSinglestage(DictPcode.DRUG_VAR, orgCode);
			List<DrugEnforcementEvent> drugEnforcementEventList = new ArrayList<DrugEnforcementEvent>();
			String addictDrugVar = drugEnforcementEvent.getAddictDrugVar();
			Date handleDate = drugEnforcementEvent.getHandleDate(),
				 finDate = drugEnforcementEvent.getFinDate();
			
			drugEnforcementEventList.add(drugEnforcementEvent);
			
			this.formatDataOut(drugEnforcementEventList, orgCode);
			
			if(StringUtils.isNotBlank(addictDrugVar)) {
				try {
					DataDictHelper.setDictValueForField(drugEnforcementEvent, "addictDrugVar", "addictDrugVarName", drugVarDict);
				} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
					e.printStackTrace();
				}
			}
			if(handleDate != null) {
				drugEnforcementEvent.setHandleDateStr(DateUtils.formatDate(handleDate, DateUtils.PATTERN_24TIME));
			}
			if(finDate != null) {
				drugEnforcementEvent.setFinDateStr(DateUtils.formatDate(finDate, DateUtils.PATTERN_24TIME));
			}
		}
	}
	
	/**
	 * 格式化输出数据
	 * @param drugEnforcementEventList
	 * @param orgCode	组织编码
	 */
	private void formatDataOut(List<DrugEnforcementEvent> drugEnforcementEventList, String orgCode) {
		if(drugEnforcementEventList != null && drugEnforcementEventList.size() > 0) {
			String drugSocialSituation = null;
			Date reportDate = null;
			Long drugId = -1L;
			DrugRecord drugRecord = null;
			
			List<BaseDataDict> drugSocialSituationDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.DRUG_SOCIAL_SITUATION, orgCode);
			
			for(DrugEnforcementEvent drugEnforcementEvent : drugEnforcementEventList) {
				drugSocialSituation = drugEnforcementEvent.getDrugSocialSituation();
				reportDate = drugEnforcementEvent.getReportDate();
				drugId = drugEnforcementEvent.getDrugId();
				
				if(StringUtils.isNotBlank(drugSocialSituation)) {
					try {
						DataDictHelper.setDictValueForField(drugEnforcementEvent, "drugSocialSituation", "drugSocialSituationName", drugSocialSituationDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
				}
				
				if(reportDate != null) {
					drugEnforcementEvent.setReportDateStr(DateUtils.formatDate(reportDate, DateUtils.PATTERN_24TIME));
				}
				
				/*if(drugId != null && drugId > 0) {
					drugRecord = drugRecordService.findDrugRecordById(drugId, orgCode);
					
					if(drugRecord != null) {
						drugEnforcementEvent.setAddictName(drugRecord.getName());
						drugEnforcementEvent.setAddictIdCard(drugRecord.getIdentityCard());
						drugEnforcementEvent.setAddictGridPath(drugRecord.getGridPath());
						drugEnforcementEvent.setCiRsId(drugRecord.getCiRsId());
						
					}
				}*/
			}
		}
	}
	
	/**
	 * 禁毒事件归档
	 * @param drugEnforcementId
	 * @param userInfo
	 * @param extraParam
	 */
	private void archiveDrugEnforcement(Long drugEnforcementId, UserInfo userInfo, Map<String, Object> extraParam) {
		DrugEnforcementEvent drugEnforcementEvent = new DrugEnforcementEvent();
		
		drugEnforcementEvent.setDrugEnforcementId(drugEnforcementId);
		drugEnforcementEvent.setFinDate(new Date());
		drugEnforcementEvent.setStatus(IDrugEnforcementEventService.STATUS.HANDLED.getStatus());
		
		this.saveOrUpdateDrugEnforcementEvent(drugEnforcementEvent, userInfo, extraParam);
	}
	
	/**
	 * 获取短信类型
	 * @param curNodeName	当前环节名称
	 * @param nextNodeName	下一环节名称
	 * @return 默认为下派类型
	 */
	private String capSMSContentType(String curNodeName, String nextNodeName) {
		String contentType = "",
			   CONTENT_TYPE_REPORT = ConstantValue.DRUG_ENFORCEMENT_EVENT_REPORT_NOTE,	//上报
			   CONTENT_TYPE_SEND = ConstantValue.DRUG_ENFORCEMENT_EVENT_SEND_NOTE,		//下派
			   CONTENT_TYPE_IMPLEMENT = ConstantValue.DRUG_ENFORCEMENT_EVENT_SEND_IMPLEMENT_NOTE,//下派给部门实施
			   GRID_NODE_CODE = "task2",									//网格员办理环节
			   COMMUNITY_NODE_CODE = "task3",								//社区(村)办理环节
			   STREET_NARCOTICS_CONTROL_COMMISSION_NODE_CODE = "task4",		//乡镇(街道)禁毒委办理环节
			   DISTRICT_NARCOTICS_CONTROL_COMMISSION_NODE_CODE = "task5",	//县级市禁毒委办理环节
			   IMPLEMENT_NODE_CODE = "task6",								//实施办理环节
			   ASSESS_NODE_CODE = "task8";									//归档办理环节
		Map<String, String> contentTypeMap = new HashMap<String, String>();
		
		//上报
		contentTypeMap.put(GRID_NODE_CODE + "-" + STREET_NARCOTICS_CONTROL_COMMISSION_NODE_CODE, CONTENT_TYPE_REPORT);
		contentTypeMap.put(COMMUNITY_NODE_CODE + "-" + STREET_NARCOTICS_CONTROL_COMMISSION_NODE_CODE, CONTENT_TYPE_REPORT);
		contentTypeMap.put(STREET_NARCOTICS_CONTROL_COMMISSION_NODE_CODE + "-" + DISTRICT_NARCOTICS_CONTROL_COMMISSION_NODE_CODE, CONTENT_TYPE_REPORT);
		contentTypeMap.put(IMPLEMENT_NODE_CODE + "-" + ASSESS_NODE_CODE, CONTENT_TYPE_REPORT);
		
		//下派给部门实施
		contentTypeMap.put(DISTRICT_NARCOTICS_CONTROL_COMMISSION_NODE_CODE + "-" + IMPLEMENT_NODE_CODE, CONTENT_TYPE_IMPLEMENT);
		
		//归档
		contentTypeMap.put("-" + END_NODE_CODE, ConstantValue.DRUG_ENFORCEMENT_EVENT_ARCHIVE_NOTE);
		
		contentType = contentTypeMap.get(curNodeName + "-" + nextNodeName);
		
		if(StringUtils.isBlank(contentType)) {
			contentType = contentTypeMap.get("-" + nextNodeName);
		}
		
		if(StringUtils.isBlank(contentType)) {
			contentType = CONTENT_TYPE_SEND;
		}
		
		return contentType;
	}
	
	/**
	 * 获取禁毒事件流程名称
	 * @param params
	 * 			workflowName	流程名称
	 * 			userOrgCode		组织编码
	 * @param userInfo
	 * @return
	 */
	private String capWorkflowName4DrugEnforement(Map<String, Object> params, UserInfo userInfo) {
		String workflowName = "",
			   orgCode = "";
		
		if(CommonFunctions.isNotBlank(params, "workflowName")) {
			workflowName = params.get("workflowName").toString();
		} else {
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				orgCode = params.get("userOrgCode").toString();
			} else if(userInfo != null) {
				orgCode = userInfo.getOrgCode();
			}
			
			workflowName = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.WORKFLOW_NAME + "_" + DRUG_ENFORCEMENT_EVENT_FORM_TYPE, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
		}
		
		if(StringUtils.isBlank(workflowName)) {
			workflowName = DRUG_ENFORCEMENT_EVENT_WORKFLOW_NAME;
		}
		
		return workflowName;
	}
	
	/**
	 * 获取禁毒事件决策节点决策类
	 * @param params
	 * 			decisionMakingService	决策类
	 * 			userOrgCode				组织编码
	 * @param userInfo
	 * @return
	 */
	private String capDecisionMakingService4DrugEnforement(Map<String, Object> params, UserInfo userInfo) {
		String decisionMakingService = "",
			   orgCode = "";
		
		if(CommonFunctions.isNotBlank(params, "decisionMakingService")) {
			decisionMakingService = params.get("decisionMakingService").toString();
		} else {
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				orgCode = params.get("userOrgCode").toString();
			} else if(userInfo != null) {
				orgCode = userInfo.getOrgCode();
			}
			
			decisionMakingService = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.DECISION_MAKING_SERVICE + "_" + DRUG_ENFORCEMENT_EVENT_FORM_TYPE, IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, orgCode);
		}
		
		if(StringUtils.isBlank(decisionMakingService)) {
			decisionMakingService = DECISION_MAKING_SERVICE;
		}
		
		return decisionMakingService;
	}
}