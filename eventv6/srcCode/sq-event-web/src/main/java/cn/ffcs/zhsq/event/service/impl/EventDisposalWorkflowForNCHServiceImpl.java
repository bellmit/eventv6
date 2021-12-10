package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.om.Workflow;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExpand.service.IEventDisposal4ExpandService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 江西省南昌市南昌县工作流使用接口实现
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflowForNCHService")
public class EventDisposalWorkflowForNCHServiceImpl extends
	EventDisposalWorkflowForSimingServiceImpl {
	
	@Autowired
	private HessianFlowService hessianFlowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService; 
	
	@Autowired
	private UserManageOutService userManageService;

	//事件扩展信息接口
	@Autowired
	private IEventDisposal4ExpandService eventDisposalExpandService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//南昌县工作流决策类
	private final String NCH_DECISION_SERVICE = "workflowNCHDecisionMakingService";
	
	//组织类型
	private final String ORG_TYPE_UNIT = "1";	//组织类型——单位
	
	@Override
	public String startFlowByWorkFlow(Long workFlowId, Integer eventId,
			String wftypeId, UserBO user, OrgSocialInfoBO org,
			Map<String, Object> userMap) throws Exception {
		String instanceId = null;
		Map<String, Object> resultMap = null;
		
		if(eventId == null || eventId < 0) {
			throw new IllegalArgumentException("缺少有效的事件id！");
		}
		
		instanceId = this.capInstanceIdByEventId(Long.valueOf(eventId));
		
		if(StringUtils.isBlank(instanceId)) {
			if(isUserAbleToStartWorkflow(org, user.getUserId())){
				resultMap = hessianFlowService.startEventWorkflow(workFlowId, Long.valueOf(eventId), user, org, userMap);
				if(resultMap != null) {
					if(CommonFunctions.isNotBlank(resultMap, "instanceId")) {
						instanceId = resultMap.get("instanceId").toString();
					}
					
					this.updateEventStatus(Long.valueOf(eventId), user.getUserId(), org, resultMap);
				}
			}
		}
		
		return instanceId;
	}
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String wftypeId = ConstantValue.EVENT_WORKFLOW_WFTYPEID;
		UserBO user = new UserBO();
		OrgSocialInfoBO orgInfo = null;
		Map<String, Object> userMap = null;
		Integer formId = 0;
		Long orgId = -1L;
		Long userId = -1L;
		String orgType = "";
		String orgProfessionCode = "";
		String instanceId = "";
		StringBuffer wrongMsg = new StringBuffer("");
		
		if(userInfo != null){
			orgId = userInfo.getOrgId();
			userId = userInfo.getUserId();
			user.setUserId(userId);
			user.setPartyName(userInfo.getPartyName());
			orgInfo = orgSocialInfoService.findByOrgId(orgId);
			
			if(orgInfo != null) {
				orgType = orgInfo.getOrgType();
				orgProfessionCode = orgInfo.getProfessionCode();
			}
		}
		
		if(workFlowId==null || workFlowId.equals(-1L)){
			workFlowId = this.capWorkflowId(null, eventId, userInfo, null);
		}
		if(eventId != null && eventId > 0){
			formId = Integer.valueOf(eventId.toString());
		}
		if(StringUtils.isBlank(toClose)){
			toClose = ConstantValue.WORKFLOW_IS_NO_CLOSE;
		}
		
		if(ORG_TYPE_UNIT.equals(orgType) && (StringUtils.isBlank(orgProfessionCode) || ConstantValue.GOV_PROFESSION_CODE.equals(orgProfessionCode))) {
			userMap = new HashMap<String, Object>();
			userMap.put("orgCode", orgInfo.getOrgCode()); 
			userMap.put("orgType", orgType); //组织类型
			
			StringBuffer parentOrgId = new StringBuffer("");
			Map<String, String> gridOrgUser = new HashMap<String, String>();
			//获取评价人员 数据格式 orgCode1:userName11,userName12;orgCode2:userName2;
			String userNameOrgCodes = funConfigurationService.turnCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.EVALUATE_USER_ORG_ID, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			StringBuffer userIds = new StringBuffer("");
			
			if(StringUtils.isNotBlank(userNameOrgCodes)) {
				String[] userNameOrgCodeArray = userNameOrgCodes.split(";");//分割各用户id和组织id组合
				String[] nameCodeArray = new String[]{};
				String regisValues = "";
				String[] regisValueArray = new String[]{};
				String userOrgCode = "";
				OrgSocialInfoBO orgSocialInfo = null;
				UserBO userBo = null;
				
				for(String userNameOrgCode : userNameOrgCodeArray) {
					userIds = new StringBuffer("");
					
					if(StringUtils.isNotBlank(userNameOrgCode)) {
						nameCodeArray = userNameOrgCode.split(":");//分割用户id和组织id
						if(nameCodeArray.length == 2) {
							userOrgCode = nameCodeArray[0];
							regisValues = nameCodeArray[1];
							
							orgSocialInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(userOrgCode);
							if(orgSocialInfo != null && orgSocialInfo.getOrgId() != null) {
								if(StringUtils.isNotBlank(regisValues)) {
									regisValueArray = regisValues.split(",");
									for(String regisValue : regisValueArray) {
										if(StringUtils.isNotBlank(regisValue)) {
											userBo = userManageService.getUserInfoByRegistValue(regisValue);
											if(userBo != null) {
												userIds.append(",").append(userBo.getUserId());
											} else {
												logger.error("用户名：" + regisValue + " 没有对应的用户！");
											}
										}
									}
								}
								
								if(userIds.length() > 0) {
									gridOrgUser.put(orgSocialInfo.getOrgId().toString(), userIds.substring(1));
								} 
							}
						} else {
							logger.error(userNameOrgCode + " 格式不正确！正确格式为：orgCode:regisValue;。");
						}
					}
				} 
			} else {
				wrongMsg.append("缺少评价人员配置信息，功能编码为：").append(ConstantValue.WORKFLOW_ATTRIBUTE).append("；触发条件为：").append(ConstantValue.EVALUATE_USER_ORG_ID).append("；配置类型为：").append(IFunConfigurationService.CFG_TYPE_FACT_VAL);
			}
			
			if(gridOrgUser.keySet().size() == 0) {
				wrongMsg.append(userNameOrgCodes).append(" 没有有效的评价人员！");
			}
			
			//查找父级
			List<OrgSocialInfoBO> parentOrgInfo = new ArrayList<OrgSocialInfoBO>();
			Map<String, Object> parentOrgExtraParam = new HashMap<String, Object>();
			parentOrgExtraParam.put("eventId", eventId);
			
			try {
				parentOrgInfo = fiveKeyElementService.findOrgByLevel(orgId, IFiveKeyElementService.PARENT_LEVEL_ONE, ConstantValue.GOV_PROFESSION_CODE, "", userInfo, parentOrgExtraParam);
			} catch (Exception e) {
				parentOrgInfo = new ArrayList<OrgSocialInfoBO>();
				e.printStackTrace();
			}
			
			for(OrgSocialInfoBO orgSocialInfoBO : parentOrgInfo) {
				if(orgSocialInfoBO != null){
					parentOrgId.append(",").append(orgSocialInfoBO.getOrgId());
				}
			}
			
			if(!gridOrgUser.isEmpty()) {
				userMap.put("gridOrgUser", gridOrgUser);//可评价人员信息 Map<orgId, userId>
			} else {
				userMap.put("parentOrgId", parentOrgId.toString());//上级组织id，以英文逗号相连接
			}
			
			userMap.put("toClose", toClose);
			userMap.put("isGridAdmin", fiveKeyElementService.isUserIdGridAdminForString(orgId, userId));
			userMap.put("isNewOrganization", eventOrgInfoService.isNewOrganizationForString(orgInfo));
			userMap.put("orgChiefLevel", orgInfo.getChiefLevel());
			userMap.put("orgType", orgType);
			userMap.put("isGovernment", eventOrgInfoService.isGovernmentForString(orgInfo));
			userMap.put("decisionService", NCH_DECISION_SERVICE);
			
			if(extraParam != null) {
				userMap.putAll(extraParam);
			}
			
			if(wrongMsg.length() > 0) {//打印相关错误信息
				logger.error(wrongMsg.toString());
			} else {
				instanceId = this.startFlowByWorkFlow(workFlowId, formId, wftypeId, user, orgInfo, userMap);
			}
		}
		
		return instanceId;
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{
		boolean result = false;
		
		if(this.isUserInfoCurrentUser(taskId, instanceId, userInfo)) {
			Map<String, Object> resultMap = null;
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
			UserBO user = new UserBO();
			Long eventId = -1L;
			
			user.setUserId(userInfo.getUserId());
			user.setPartyName(userInfo.getPartyName());
			//获取事件当前环节的节点编码
			
			ProInstance proInstance = hessianFlowService.getProByInstanceId(instanceId);
			if(proInstance != null){
				eventId = proInstance.getFormId();
			}
			
			if(ConstantValue.HANDLING_TASK_CODE.equals(nextNodeName) || ConstantValue.ACCEPT_TASK_CODE.equals(nextNodeName)) {
				/**
				 * taskId 任务ID
				 * duedate 环节处理时限
				 */
				resultMap = saveHandlingTask(eventId, instanceId, nextNodeName, advice, userInfo,extraParam);
				
			} else {
				String msgWrong = null;
				
				if(CommonFunctions.isNotBlank(extraParam, "nodeCode")) {
					INodeCodeHandler nodeCodeHandler = fiveKeyElementService.createNodeCodeHandle(extraParam.get("nodeCode").toString());
					
					if(nodeCodeHandler != null) {
						if(nodeCodeHandler.isClose()) {//结案操作判断是否有评价人员
							List<Map<String, Object>> queryTaskList = this.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC);
							
							//采集并结案才进行评价人员的判断，以下判断有风险，如果移除的任务变更，则以下判断需要相应调整
							if(queryTaskList != null && queryTaskList.size() == 2) {
								List<OrgSocialInfoBO> parentOrgInfoList = new ArrayList<OrgSocialInfoBO>();
								List<UserBO> userList = new ArrayList<UserBO>();
								Map<String, Object> parentOrgExtraParam = new HashMap<String, Object>();
								
								parentOrgExtraParam.put("eventId", eventId);
								
								//查找父级
								try {
									parentOrgInfoList = fiveKeyElementService.findOrgByLevel(userInfo.getOrgId(), IFiveKeyElementService.PARENT_LEVEL_ONE, ConstantValue.GOV_PROFESSION_CODE, "", userInfo, parentOrgExtraParam);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								if(parentOrgInfoList != null) {
									for(OrgSocialInfoBO parentOrgInfo : parentOrgInfoList) {
										userList.addAll(fiveKeyElementService.findCollectToCloseUserList(parentOrgInfo.getOrgId()));
									}
								}
								
								if(userList.size() == 0) {
									msgWrong = "缺少可评价人员！";
								}
							}
						}
					}
				}
				
				if(StringUtils.isBlank(msgWrong)) {
					/**
					 *  提交流程
					 * @param taskId		任务ID
					 * @param nextNodeName	下一个节点编码
					 * @param userIds		动态分配人员ID，可为“id1,id2,id3...”
					 * @param curOrgIds		动态分配人员用户ID，可为“id1,id2,id3...”
					 * @param remarks		办理意见
					 * @param user			当前用户
					 * @param org			当前用户组织机构
					 * @param sendSms		短信内容
					 */
					resultMap = hessianFlowService.subWorkFlowForEvent(taskId, nextNodeName, nextStaffId, curOrgIds, advice, user, orgInfo, smsContent);
				} else {
					throw new Exception(msgWrong);
				}
			}
			
			result = resultMap != null;
			
			if(result) {
				//保存办理意见
				if(eventId != null && eventId > 0) {
					if(CommonFunctions.isBlank(resultMap, "instanceId")) {
						resultMap.put("instanceId", instanceId);
					}
					
					if(CommonFunctions.isNotBlank(extraParam, "isSetInterval")) {
						
						if("1".equals(extraParam.get("isSetInterval").toString())) {
							
							if(CommonFunctions.isNotBlank(extraParam, "eventHandleInterval")) {
								resultMap.put("eventHandleInterval", extraParam.get("eventHandleInterval"));
							}else {
								String eventDefaultHandleInterval = funConfigurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.EVENT_DEFAULT_HANDLE_INTERVAL, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
								if(StringUtils.isNotBlank(eventDefaultHandleInterval)) {
									resultMap.put("eventHandleInterval", eventDefaultHandleInterval);
								}
							}
						}
						
					}
					
					this.updateEventStatus(eventId, userInfo.getUserId(), orgInfo, resultMap);
					
					if(StringUtils.isNotBlank(advice)) {
						EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
						if(event != null) {
							String eventResult = event.getResult(),
								      eventStatus = event.getStatus(),
								      eventType = event.getType(),
								      eventCode = event.getCode();
							boolean isDetail2Edit = false;
							
							if(CommonFunctions.isNotBlank(extraParam, "isDetail2Edit")) {//更新事件属性
								isDetail2Edit = Boolean.valueOf(extraParam.get("isDetail2Edit").toString());
							}
							
							if(isDetail2Edit || ConstantValue.EVENT_STATUS_ARCHIVE.equals(eventStatus) || (StringUtils.isBlank(eventResult) && ConstantValue.EVENT_STATUS_END.equals(eventStatus))) {
								EventDisposal eventTmp = new EventDisposal();
								eventTmp.setEventId(eventId);
								eventTmp.setResult(advice);
								eventTmp.setUpdatorId(userInfo.getUserId());
								
								if(StringUtils.isNotBlank(eventType)  && StringUtils.isNotBlank(eventCode) && eventCode.contains("_null_")) {
									eventTmp.setCode(eventCode.replaceFirst("_null_", "_" + eventType + "_"));
								}
								
								eventDisposalService.updateEventDisposalById(eventTmp);
							}
						}
					}
					//保存评价信息
					String END_NODE_CODE = "end1";//流程结束
					//默认不增加评价信息，当传入参数isEvaluate并且为true时，增加评价信息
					if(null != nextNodeName && END_NODE_CODE.equals(nextNodeName) && CommonFunctions.isNotBlank(extraParam,"isEvaluate") && Boolean.valueOf(String.valueOf(extraParam.get("isEvaluate")))){
						String evaLevel = "";
						String evaContent = "";
						if((eventId ==null || eventId < 0) && CommonFunctions.isNotBlank(extraParam,"eventId")){
							eventId = Long.valueOf(String.valueOf(extraParam.get("eventId")));
						}
						if(CommonFunctions.isNotBlank(extraParam,"evaLevel")){
							evaLevel = String.valueOf(extraParam.get("evaLevel"));
						}
						if(CommonFunctions.isNotBlank(extraParam,"evaContent")){
							evaContent = String.valueOf(extraParam.get("evaContent"));
						}
						eventDisposalExpandService.saveOrUpdateEventEvaluate(userInfo,eventId,evaLevel,evaContent,extraParam);
					}
				}
			}
		}
		
		return result;
	}
	
	@Override
	public boolean rejectWorkFlow(String taskId, String instanceId,
			String advice, UserInfo userInfo) throws Exception {
		Long instanceIdL = -1L;
		OrgSocialInfoBO orgInfo = null;
		UserBO user = null;
		boolean result = false;
		
		if(this.isUserInfoCurrentUser(taskId, instanceId, userInfo)) {
			try {
				instanceIdL = Long.valueOf(instanceId);
			} catch(NumberFormatException e) {}
			
			if(userInfo != null) {
				Long orgId = userInfo.getOrgId();
				Long userId = userInfo.getUserId();
				
				if(orgId != null && orgId > 0) {
					orgInfo = orgSocialInfoService.findByOrgId(orgId);
				}
				
				if(userId != null && userId > 0) {
					user = userManageService.getUserInfoByUserId(userId);
				}
			}
			
			/**
			 * 针对新事件的驳回接口，回调的交给新事件自己处理
			 * @param taskId	当前任务id
			 * @param remarks	备注
			 * @param instanceId	流程实例ID
			 * @param user	当前用户
			 * @param org	当前用户组织
			 * @return
			 *  duedate		下一环节办理期限
			 * 	如果启动失败,返回map为null
			 */
			Map<String, Object> resultMap = hessianFlowService.rejectWorkFlowForEvent(taskId, advice, instanceId, user, orgInfo);
			
			result = resultMap != null;
			
			if(result && instanceIdL > 0) {
				ProInstance pro = this.capProInstanceByInstanceId(instanceIdL);
				
				if(pro != null) {
					resultMap.put("isReject", true);
					
					if(CommonFunctions.isBlank(resultMap, "instanceId")) {
						resultMap.put("instanceId", instanceIdL);
					}
					
					this.updateEventStatus(pro.getFormId(), user.getUserId(), orgInfo, resultMap);
				}
			}
		}
		
		return result;
	}
	
	@Override
	public Long capWorkflowId(Long instanceId, Long eventId, UserInfo userInfo, Map<String, Object> extraParam){
		Long workflowId = -1L;
		Long orgId = userInfo.getOrgId();//用户所在组织编号
		ProInstance pro = null;
		
		if(instanceId != null && instanceId > 0) {
			pro = this.capProInstanceByInstanceId(instanceId);
		}
		
		if(pro != null) {
			workflowId = pro.getWorkFlowId();
		} else if(orgId != null && orgId > 0){//启动流程时，会使用到
			String workflowName = null;
			
			if(CommonFunctions.isNotBlank(extraParam, "workflowName")) {
				workflowName = extraParam.get("workflowName").toString();
			} else {
				workflowName = this.capWorkflowName(eventId, null, userInfo);
			}
			
			if(StringUtils.isBlank(workflowName)) {
				workflowName = ConstantValue.EVENT_WORKFLOW_NAME_NCH;
			}
			
			Workflow workflow = hessianFlowService.getWorkflowByWfTypeIdAndOrgIdCircle(workflowName, ConstantValue.EVENT_WORKFLOW_WFTYPEID, String.valueOf(orgId));
			if(workflow != null){
				workflowId = workflow.getWorkFlowId();
			}
		}
		
		return workflowId;
	}
	
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		Map<String, Object> resultMap = super.initFlowInfo(taskId, instanceId, userInfo, params);
		
		if(CommonFunctions.isNotBlank(resultMap, "taskNodes")) {
			List<Node> taskNodes = (List<Node>) resultMap.get("taskNodes");
			
			if(taskNodes != null) {
				ProInstance pro = this.capProInstanceByInstanceId(instanceId);
				if(pro != null) {
					Long eventId = pro.getFormId();
					EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
					if(event != null) {
						String status = event.getStatus(),
							   subStatus = event.getSubStatus();
						
						//增添“处理中”办理环节
						if((StringUtils.isBlank(subStatus) || ConstantValue.HANDLING_STATUS.equals(subStatus)) && (ConstantValue.EVENT_STATUS_REPORT.equals(status) || ConstantValue.EVENT_STATUS_DISTRIBUTE.equals(status))) {
							Node node = new Node();
							node.setNodeId(-1);
							node.setNodeName(ConstantValue.HANDLING_TASK_CODE);
							node.setNodeNameZH(ConstantValue.HANDLING_TASK_NAME);
							node.setDynamicSelect("1");
							node.setNodeType("1");
							node.setTransitionCode("__C0__");//设置为采集人办理
							
							taskNodes.add(node);
							
							resultMap.put("taskNodes", taskNodes);
						}
					}
				}
			}
		}
		
		return resultMap;
	}
}
