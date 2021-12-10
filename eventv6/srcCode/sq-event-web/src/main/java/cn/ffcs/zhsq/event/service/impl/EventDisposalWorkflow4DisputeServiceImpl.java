package cn.ffcs.zhsq.event.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.om.Workflow;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 矛盾纠纷 工作流使用接口实现 对应新的流程图
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflow4DisputeServiceImpl")
public class EventDisposalWorkflow4DisputeServiceImpl extends
	EventDisposalWorkflowForEventNewServiceImpl {
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Resource(name = "workflowDecisionMakingService")
	private IWorkflowDecisionMakingService<String> workflowDecisionMakingService;
	
	@Autowired
	private HessianFlowService hessianFlowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService; 
	
	@Autowired
	private UserManageOutService userManageService;
	
	//旧版通用事件工作流接口
	@Resource(name = "eventDisposalWorkflowForEventService")
	private IEventDisposalWorkflowService eventDisposalWorkflowForEventService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final String UNSATISFY_LEVEL = "01";//不满意评价等级
	private static final String COLLECT_NODE_CODE = "task1";				//采集事件环节节点编码
	private static final String EVALUATE_NODE_CODE = "task9";				//评价环节节点编码
	//事件通用工作流决策类
	private final String EVENT_DECISION_SERVICE = "workflowEventDecisionMakingService";
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String wftypeId = ConstantValue.EVENT_WORKFLOW_WFTYPEID;
		UserBO user = new UserBO();
		OrgSocialInfoBO orgInfo = null;
		Map<String, Object> userMap = null;
		Integer formId = 0;
		Long orgId = -1L, userId = -1L;
		String chiefLevel = "",//组织层级
			   orgCode = "";
		boolean isSendEvaUser = false, isGridAdmin = false;
		
		if(userInfo != null){
			orgId = userInfo.getOrgId();
			userId = userInfo.getUserId();
			user.setUserId(userId);
			user.setPartyName(userInfo.getPartyName());
			orgInfo = orgSocialInfoService.findByOrgId(orgId);
		}
		
		if(workFlowId == null || workFlowId < 0) {
			workFlowId = this.capWorkflowId(null, eventId, userInfo, extraParam);
		}
		if(eventId != null){
			formId = Integer.valueOf(eventId.toString());
		}
		if(StringUtils.isBlank(toClose)){
			toClose = ConstantValue.WORKFLOW_IS_NO_CLOSE;
		}
		if(orgInfo != null){
			chiefLevel = orgInfo.getChiefLevel();//组织层级
			orgCode = orgInfo.getOrgCode();
			
			userMap = new HashMap<String, Object>();
			userMap.put("orgCode", orgCode); 
			userMap.put("orgType", orgInfo.getOrgType()); //组织类型
			
			//查找父级
			List<OrgSocialInfoBO> parentOrgInfo = fiveKeyElementService.findOrgByLevel(orgId, IFiveKeyElementService.PARENT_LEVEL_ONE, ConstantValue.GOV_PROFESSION_CODE, new UserInfo());
			StringBuffer parentOrgId = new StringBuffer("");
			Map<String, String> gridOrgUser = new HashMap<String, String>();
			
			isGridAdmin = fiveKeyElementService.isUserIdGridAdmin(orgId, userId);//当当前用户为网格员时，传递可评价人员信息
			isSendEvaUser = (isGridAdmin ||				//当当前用户为网格员时，传递可评价人员信息
			 		 chiefLevel.equals(ConstantValue.UNITED_PREVENTION_GROUP_ORG_LEVEL));	//当上级为网格员办理时，需要过滤出上级为网格员的用户，目前只有联防组网格才有此操作

			for(OrgSocialInfoBO orgSocialInfoBO : parentOrgInfo) {
				if(orgSocialInfoBO != null){
					parentOrgId.append(",").append(orgSocialInfoBO.getOrgId());
					
					if(isSendEvaUser){//当当前用户为网格员时，传递可评价人员信息
						try {
							List<UserBO> userBoList = fiveKeyElementService.findCollectToCloseUserList(orgSocialInfoBO.getOrgId());
							if(userBoList!=null && userBoList.size()>0){
								StringBuffer gridUserIds = new StringBuffer("");
								
								for(UserBO userBO : userBoList){
									gridUserIds.append(userBO.getUserId()).append(",");
								}
								
								if(gridUserIds.length() > 0){
									gridUserIds = new StringBuffer(gridUserIds.subSequence(0, gridUserIds.length()-1));
									gridOrgUser.put(String.valueOf(orgSocialInfoBO.getOrgId()), gridUserIds.toString());
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if(parentOrgId.length() > 0) {
				parentOrgId = new StringBuffer(parentOrgId.substring(1));
			}
			
			if(eventId != null && eventId > 0) {
				EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
				
				if(event != null) {
					String collectWay = event.getCollectWay();
					
					//采集渠道为微信的需要为评价环节设定指定的办理人员
					if(ConstantValue.COLLECT_WAY_WECHAT.equals(collectWay)) {
						Map<String, String> taskAppointedOrgUser = new HashMap<String, String>();
						//获取评价人员 数据格式 orgCode1:registerName11,registerName12;orgCode2:registerName2;
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
												taskAppointedOrgUser.put(orgSocialInfo.getOrgId().toString(), userIds.substring(1));
											} 
										}
									} else {
										logger.error(userNameOrgCode + " 格式不正确！正确格式为：orgCode:regisValue;。");
									}
								}
							} 
							
							if(!taskAppointedOrgUser.isEmpty()) {
								Map<String, Object> taskAppointedOrgUserMap = new HashMap<String, Object>();
								taskAppointedOrgUserMap.put(EVALUATE_NODE_CODE, taskAppointedOrgUser);
								
								userMap.put("taskAppointedOrgUser", taskAppointedOrgUserMap);
							}
						}
					}
				}
			}
			
			if(isSendEvaUser) {
				if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose) && gridOrgUser.isEmpty()) {
					throw new Exception("缺少可评价人员！");
				} else {
					userMap.put("gridOrgUser", gridOrgUser);//可评价人员信息 Map<orgId, userIds>
				}
			} else {
				userMap.put("parentOrgId", parentOrgId.toString());//上级组织id，以英文逗号相连接
			}
			
			userMap.put("orgChiefLevel", chiefLevel);
			userMap.put("isGovernment", eventOrgInfoService.isGovernmentForString(orgInfo));
		}
		
		userMap.put("toClose", toClose);
		userMap.put("isGridAdmin", fiveKeyElementService.isUserIdGridAdminForString(isGridAdmin));
		userMap.put("isNewOrganization", eventOrgInfoService.isNewOrganizationForString(orgInfo));
		
		userMap.put("decisionService", EVENT_DECISION_SERVICE);
		
		if(extraParam != null) {
			userMap.putAll(extraParam);
		}
		
		return this.startFlowByWorkFlow(workFlowId, formId, wftypeId, user, orgInfo, userMap);
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{
		boolean result = false;
		
		if(isNewWorkflow(instanceId, userInfo)) {
			result = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		} else {
			result = eventDisposalWorkflowForEventService.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		}
		
		return result;
	}
	
	@Override
	public boolean rejectWorkFlow(Map<String, Object> params, UserInfo userInfo) throws Exception {
		boolean result = false;
		String advice = "", evaLevel = "", nextNodeName = "";
		Long eventId = -1L, instanceId = -1L, curTaskId = -1L;
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(params, "curTaskId")) {
			try {
				curTaskId = Long.valueOf(params.get("curTaskId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(params, "advice")) {
			advice = params.get("advice").toString();
		}
		if(CommonFunctions.isNotBlank(params, "evaLevel")) {
			evaLevel = params.get("evaLevel").toString();
		}
		
		if(instanceId < 0 && CommonFunctions.isNotBlank(params, "eventId")) {
			try {
				eventId = Long.valueOf(params.get("eventId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
			if(eventId > 0) {
				instanceId = this.capInstanceIdByEventIdForLong(eventId);
			}
		}
		
		if(isNewWorkflow(instanceId, userInfo)) {
			Long currentTaskId = this.curNodeTaskId(instanceId);
			
			if(curTaskId < 0 && instanceId > 0) {
				curTaskId = currentTaskId;
			}
			
			if(curTaskId > 0 && instanceId > 0) {
				if(curTaskId.equals(currentTaskId)) {
					if(UNSATISFY_LEVEL.equals(evaLevel)) {//评价不满意时，驳回指定的环节
						ProInstance pro = this.capProInstanceByInstanceId(instanceId);
						
						if(pro != null) {
							Long proCreateUserId = pro.getUserId(), proCreateOrgId = -1L;
							
							proCreateOrgId = pro.getOrgId();
							
							Map<String, Object> decisionParams = new HashMap<String, Object>();
							decisionParams.put("decisionService", EVENT_DECISION_SERVICE);
							decisionParams.put("curUserId", proCreateUserId);
							decisionParams.put("curOrgId", proCreateOrgId);
							decisionParams.put("curNodeCode", COLLECT_NODE_CODE);
							
							try {
								nextNodeName = workflowDecisionMakingService.makeDecision(decisionParams);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							if(StringUtils.isNotBlank(nextNodeName)) {
								Map<String, Object> rejectParam = new HashMap<String, Object>(),
													resultMap = null;
								Long userId = userInfo.getUserId(), orgId = userInfo.getOrgId();
								String userPartyName = userInfo.getPartyName(),
									   orgName = userInfo.getOrgName();
								
								if(userId == null || userId < 0) {
									UserBO userBo = userManageService.getUserInfoByUserId(proCreateUserId);
									if(userBo != null) {
										userId = proCreateUserId;
										userPartyName = userBo.getPartyName();
									}
								}
								if(orgId == null || orgId < 0) {
									OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(proCreateOrgId);
									if(orgInfo != null) {
										orgId = proCreateOrgId;
										orgName = orgInfo.getOrgName();
									}
								}
								
								rejectParam.put("userId", userId);
								rejectParam.put("userPartyName", userPartyName);
								rejectParam.put("orgId", orgId);
								rejectParam.put("orgName", orgName);
								rejectParam.put("curTaskId", curTaskId);
								rejectParam.put("instanceId", instanceId);
								rejectParam.put("appointedTaskName", nextNodeName);
								rejectParam.put("remarks", advice);
								
								try {
									resultMap = hessianFlowService.rejectToAppointedTask(rejectParam);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								result = resultMap != null;
								
								if(result) {
									if(eventId < 0 && pro != null) {
										eventId = pro.getFormId();
									}
									
									resultMap.put("isReject", true);
									
									OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
									
									if(orgInfo != null) {
										this.updateEventStatus(eventId, userId, orgInfo, resultMap);
									}
								}
							}
						}
					} else {
						result = this.rejectWorkFlow(curTaskId.toString(), instanceId.toString(), advice, userInfo);
					}
				} else {
					throw new Exception("当前环节已变更，当前环节为["+ this.curNodeTaskName(instanceId) +"]！");
				}
			}
		} else {
			result = eventDisposalWorkflowForEventService.rejectWorkFlow(params, userInfo);
		}
		
		return result;
	}
	
	/**
	 * 判断是否使用新流程图
	 * 当当前的流程名称与配置中使用的流程名称一致时，认为其使用新的流程图，
	 * 因为默认认为旧流程图未启用流程图名称配置
	 * 同时此法也仅适用于使用了新流程图的组织下的旧流程图事件的办理
	 * @param instanceId	流程实例id
	 * @param userInfo		当前用户
	 * @return true 启用的新流程图
	 */
	private boolean isNewWorkflow(Long instanceId, UserInfo userInfo) {
		boolean result = false;
		
		if(instanceId != null && instanceId > 0) {
			ProInstance pro = this.capProInstanceByInstanceId(instanceId);
			if(pro != null) {
				Workflow workflow = this.queryWorkflowById(pro.getWorkFlowId());
				if(workflow != null) {
					String workflowNameNow = workflow.getFlowName();
					String workflowName = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.WORKFLOW_NAME, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
					
					result = workflowNameNow.equals(workflowName);
				}
			}
		}
		
		return result;
	}
}
