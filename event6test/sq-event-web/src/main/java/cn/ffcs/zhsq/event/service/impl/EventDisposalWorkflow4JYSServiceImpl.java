package cn.ffcs.zhsq.event.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 江阴市(JYS) 工作流使用接口实现
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflow4JYSService")
public class EventDisposalWorkflow4JYSServiceImpl extends
		EventDisposalWorkflowForEventNewServiceImpl {
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService; 
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Resource(name = "workflowDecisionMakingService")
	private IWorkflowDecisionMakingService<String> workflowDecisionMakingService;
	
	//江阴市工作流决策类
	private final String EVENT_DECISION_SERVICE = "workflowEventDecisionMaking4JYSService";
	private static final String DECISION_MAKING_NODE_CODE = "decision1";	//决策1环节节点编码
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String wftypeId = ConstantValue.EVENT_WORKFLOW_WFTYPEID,
			   chiefLevel = "",//组织层级
			   instanceId = null;
		UserBO user = new UserBO();
		OrgSocialInfoBO orgInfo = null;
		Map<String, Object> userMap = null;
		Map<String, String> gridOrgUser = null;
		Integer formId = 0;
		Long orgId = -1L, userId = -1L;
		boolean isGridAdmin = false;
		//下一环节节点
		Node nextNodeInfo = null;
		//下一环节办理人员信息
		StringBuffer nextOrgId = new StringBuffer(""), 
				 	 nextUserId = new StringBuffer("");
		
		if(userInfo != null){
			orgId = userInfo.getOrgId();
			userId = userInfo.getUserId();
			user.setUserId(userId);
			user.setPartyName(userInfo.getPartyName());
			orgInfo = orgSocialInfoService.findByOrgId(orgId);
		}
		
		if(workFlowId == null || workFlowId < 0) {
			workFlowId = this.capWorkflowId(null, eventId, userInfo, null);
		}
		if(eventId != null){
			formId = Integer.valueOf(eventId.toString());
		}
		if(StringUtils.isBlank(toClose)){
			toClose = ConstantValue.WORKFLOW_IS_NO_CLOSE;
		}
		if(orgInfo != null) {
			chiefLevel = orgInfo.getChiefLevel();//组织层级
			userMap = new HashMap<String, Object>();
			gridOrgUser = new HashMap<String, String>();
			Map<String, Object> decisionParams = new HashMap<String, Object>(),
								nextNodeMap = new HashMap<String, Object>();
			String curNodeName = null,
				   workflowName = this.capWorkflowName(eventId, null, userInfo);
			List<Node> nextNodeList = null;
				
			try {
				decisionParams.put("decisionService", EVENT_DECISION_SERVICE);
				decisionParams.put("curUserId", userId);
				decisionParams.put("curOrgId", orgId);
				decisionParams.put("curNodeCode", DECISION_MAKING_NODE_CODE);
				curNodeName = workflowDecisionMakingService.makeDecision(decisionParams);
				
				nextNodeMap.put("curTaskName", curNodeName);
				nextNodeList = baseWorkflowService.findNextTaskNodes(workflowName, ConstantValue.EVENT_WORKFLOW_WFTYPEID, eventId, userInfo, nextNodeMap);
				if(nextNodeList != null) {
					String nodeType = null, fromToCode = INodeCodeHandler.ORG_DEPT + INodeCodeHandler.ORG_UINT;
					
					for(Node nextNode : nextNodeList) {
						nodeType = nextNode.getType();
						
						if((INodeCodeHandler.OPERATE_REPORT.equals(nodeType) && "1".equals(nextNode.getLevel()))
								|| (INodeCodeHandler.OPERATE_FLOW.equals(nodeType) && fromToCode.equals(nextNode.getFromTo()))) {//上报一级、部门分流给单位
							nextNodeInfo = nextNode; break;
						}
					}
				}
				
				if(nextNodeInfo != null) {
					Map<String, Object> params = new HashMap<String, Object>();
					
					params.put("eventId", eventId);
					
					Map<String, Object> resultMap = fiveKeyElementService.getNodeInfoForEvent(userInfo, curNodeName, nextNodeInfo.getToCode(), nextNodeInfo.getTransitionCode(), nextNodeInfo.getNodeId().toString(), params);
					String[] userIdArray = null, orgIdArray = null;
					
					if(CommonFunctions.isNotBlank(resultMap, "userIds")) {
						userIdArray = resultMap.get("userIds").toString().split(",");
					}
					if(CommonFunctions.isNotBlank(resultMap, "orgIds")) {
						orgIdArray = resultMap.get("orgIds").toString().split(",");
					}
					
					if(userIdArray != null && orgIdArray != null) {
						String orgIdStr = null, userIdStr = null;
						
						for(int index = 0, len = userIdArray.length; index < len; index++) {
							orgIdStr = orgIdArray[index];
							userIdStr = userIdArray[index];
							nextOrgId.append(",").append(orgIdStr);
							nextUserId.append(",").append(userIdStr);
							
							if(gridOrgUser.containsKey(orgIdStr)) {
								gridOrgUser.put(orgIdStr, gridOrgUser.get(orgIdStr) + "," + userIdStr);
							} else {
								gridOrgUser.put(orgIdStr, userIdStr);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			userMap.put("orgCode", orgInfo.getOrgCode()); 
			userMap.put("orgType", orgInfo.getOrgType()); //组织类型
			userMap.put("gridOrgUser", gridOrgUser);//可评价人员信息 Map<orgId, userIds>
			
			userMap.put("orgChiefLevel", chiefLevel);
			userMap.put("isGovernment", eventOrgInfoService.isGovernmentForString(orgInfo));
		}
		
		if(gridOrgUser != null && !gridOrgUser.isEmpty()) {
			isGridAdmin = fiveKeyElementService.isUserIdGridAdmin(orgId, userId);
			userMap.put("toClose", toClose);
			userMap.put("isGridAdmin", fiveKeyElementService.isUserIdGridAdminForString(isGridAdmin));
			userMap.put("isNewOrganization", eventOrgInfoService.isNewOrganizationForString(orgInfo));
			userMap.put("decisionService", EVENT_DECISION_SERVICE);
			
			if(extraParam != null) {
				userMap.putAll(extraParam);
			}
			
			instanceId = this.startFlowByWorkFlow(workFlowId, formId, wftypeId, user, orgInfo, userMap);
			
			//如果是网格员进行提交操作，则上报至社区
			if(isGridAdmin && ConstantValue.WORKFLOW_IS_NO_CLOSE.equals(toClose) && nextNodeInfo != null && StringUtils.isNotBlank(instanceId)) {
				if(nextOrgId.length() > 0 && nextUserId.length() > 0) {
					Long instanceIdL = Long.valueOf(instanceId);
					Long taskId = this.curNodeTaskId(instanceIdL);
					
					if(taskId != null && taskId > 0) {
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("nodeCode ", nextNodeInfo.getNodeCode());
						
						subWorkFlowForEndingAndEvaluate(instanceIdL, taskId.toString(), nextNodeInfo.getNodeName(), nextUserId.substring(1), nextOrgId.substring(1), null, userInfo, null, params);
					}
				}
			}
			
		} else {
			instanceId = super.startFlowByWorkFlow(eventId, workFlowId, toClose, userInfo, extraParam);
		}
		
		return instanceId;
	}
	
	@Override
	public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {
		int resultCode = 1;
		String wrongMsg = null;
		
		if(orgSocialInfo != null) {
			String chiefLevel = orgSocialInfo.getChiefLevel(),
				   orgType = orgSocialInfo.getOrgType(),
				   ORG_TYPE_UNIT = String.valueOf(ConstantValue.ORG_TYPE_UNIT),
				   ORG_TYPE_DEPARTMENT = String.valueOf(ConstantValue.ORG_TYPE_DEPT);
			
			if(ORG_TYPE_UNIT.equals(orgType)) {
				boolean isGridAdmin = fiveKeyElementService.isUserIdGridAdmin(orgSocialInfo.getOrgId(), userId);
				
				if(!isGridAdmin && ConstantValue.GRID_ORG_LEVEL.equals(chiefLevel)) {
					resultCode = -2;
					wrongMsg = "当前用户不是网格管理员，请先配置！";
				}
			} else if(ORG_TYPE_DEPARTMENT.equals(orgType)) {
				if(ConstantValue.STREET_ORG_LEVEL.equals(chiefLevel)) {
					String professionCodes = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.PROFESSION_CODE_4_START, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgSocialInfo.getOrgCode());
					
					if(StringUtils.isNotBlank(professionCodes)) {
						String[] professionCodeArray = professionCodes.split(";"),
								 professionCodeItemArray = null;
						StringBuffer professionName = new StringBuffer("");
						String professionCode = orgSocialInfo.getProfessionCode();
						boolean flag = false;
						
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
							wrongMsg = "该用户的组织专业不在如下专业中：" + professionName;
						}
					}
				} else {
					wrongMsg = "当前用户的组织类型为部门，但组织层级不是街道级！";
				}
			} else {
				resultCode = -1;
				wrongMsg = "当前用户的组织类型有误：既不是单位，也不是部门。请先修正！";
			}
		} else {
			resultCode = 0;
			wrongMsg = "当前组织不存在，请先检验！";
		}
		
		if(StringUtils.isNotBlank(wrongMsg)) {
			throw new Exception(wrongMsg);
		}
		
		return resultCode;
	}
}
