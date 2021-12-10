package cn.ffcs.zhsq.event.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.om.TaskReceive;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * @Description: 智慧城管(Smart City Urban Manage)延平区(Yan Ping Qu) 工作流处理类
 * @ClassName:   EventDisposalWorkflow4SCUMYPQServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年6月8日 下午5:00:52
 */
@Service(value = "eventDisposalWorkflow4SCUMYPQService")
public class EventDisposalWorkflow4SCUMYPQServiceImpl extends EventDisposalWorkflowForEventNewServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private ITimeApplicationService timeApplicationService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private IEventExtendService eventExtendService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	private static final String RECEPTIONIST_NODE_CODE = "task5";			//受理员审核
	private static final String DEPARTMENT_NODE_CODE = "task7";				//专业部门处理
	private static final String END_NODE_CODE = "end1";
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String instanceId = super.startFlowByWorkFlow(eventId, workFlowId, toClose, userInfo, extraParam);
		
		if(StringUtils.isNotBlank(instanceId)) {
			Long instanceIdL = Long.valueOf(instanceId);
			Map<String, Object> curTaskData = this.curNodeData(instanceIdL);
			String curNodeName = null, curTaskId = null,
				   nextUserIds = null, nextOrgIds = null,
				   nextNodeName = null, 
				   advice = null;
			Map<String, Object> nextTaskParams = new HashMap<String, Object>();
			List<Node> nextTaskNodeList = null;
			boolean subFlag = false;
			
			nextTaskParams.put("instanceId", instanceIdL);
			
			nextTaskNodeList = baseWorkflowService.findNextTaskNodes(null, null, null, userInfo, nextTaskParams);
			
			if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
				curNodeName = curTaskData.get("NODE_NAME").toString();
			}
			if(nextTaskNodeList != null && nextTaskNodeList.size() > 0) {
				Node nextNode = nextTaskNodeList.get(0);
				Map<String, Object> node4EventParams = new HashMap<String, Object>(),
									eventNodeMap = null;
				String nextNodeId = null, nodeCode = null;
				Boolean isSelectUser = false, isSelectOrg = false;
				
				nextNodeName = nextNode.getNodeName();
				nextNodeId = String.valueOf(nextNode.getNodeId());
				nodeCode = nextNode.getTransitionCode();
				
				node4EventParams.put("instanceId", instanceIdL);
				node4EventParams.put("eventId", eventId);
				node4EventParams.put("formId", eventId);
				node4EventParams.put("nodeId", nextNodeId);
				
				//考虑到有节点配置指定人员办理的情况，故需要先调用该接口，否则可省略该接口
				eventNodeMap = fiveKeyElementService.getNodeInfoForEvent(userInfo, curNodeName, nextNodeName, nodeCode, nextNodeId, node4EventParams);
				
				if(CommonFunctions.isNotBlank(eventNodeMap, "isSelectUser")) {
					isSelectUser = Boolean.valueOf(eventNodeMap.get("isSelectUser").toString());
				}
				if(CommonFunctions.isNotBlank(eventNodeMap, "isSelectOrg")) {
					isSelectOrg = Boolean.valueOf(eventNodeMap.get("isSelectOrg").toString());
				}
				
				if(isSelectUser) {
					if(CommonFunctions.isNotBlank(eventNodeMap, "userIds")) {
						nextUserIds = eventNodeMap.get("userIds").toString();
					}
					if(CommonFunctions.isNotBlank(eventNodeMap, "orgIds")) {
						nextOrgIds = eventNodeMap.get("orgIds").toString();
					}
				} else if(isSelectOrg) {
					//获取下一节点的可办理组织，该写法只使用于上报、分流操作，对于下派取值有误
					List<GdZTreeNode> eventNodeTreeList = fiveKeyElementService.getTreeForEvent(userInfo, null, nodeCode, null, null, node4EventParams);
					
					if(eventNodeTreeList != null && eventNodeTreeList.size() > 0) {
						Map<String, Object> nodeUserParams = new HashMap<String, Object>();
						List<UserInfo> handlerUserList = null;
						
						nodeUserParams.put("nodeId", nextNodeId);
						
						handlerUserList = fiveKeyElementService.getUserInfoList(Long.valueOf(eventNodeTreeList.get(0).getId()), nodeCode, nodeUserParams);
						
						if(handlerUserList != null && handlerUserList.size() > 0) {
							StringBuffer handlerUserIds = new StringBuffer(""),
										 handlerOrgIds = new StringBuffer("");
							
							for(UserInfo evaUser : handlerUserList) {
								handlerUserIds.append(",").append(evaUser.getUserId());
								handlerOrgIds.append(",").append(evaUser.getOrgId());
							}
							
							nextUserIds = handlerUserIds.substring(1);
							nextOrgIds = handlerOrgIds.substring(1);
						}
					}
				}
			}
			
			if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
				curTaskId = curTaskData.get("WF_DBID_").toString();
			}
			if(CommonFunctions.isNotBlank(extraParam, "advice")) {
				advice = extraParam.get("advice").toString();
			}
			
			//提交给“受理员处理”
			subFlag = subWorkFlowForEndingAndEvaluate(instanceIdL, curTaskId, nextNodeName, nextUserIds, nextOrgIds, advice, userInfo, null, extraParam);
			
			if(subFlag) {
				curTaskData = this.curNodeData(instanceIdL);
				nextOrgIds = null;
				nextUserIds = null;
				userInfo = new UserInfo();//用于变更操作人员，将其调整为下一办理环节中的办理人员
				
				if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
					curTaskId = curTaskData.get("WF_DBID_").toString();
				}
				
				if(CommonFunctions.isNotBlank(curTaskData, "WF_ORGID_ALL")) {
					nextOrgIds = curTaskData.get("WF_ORGID_ALL").toString().split(",")[0];
					
					if(StringUtils.isNotBlank(nextOrgIds)) {
						Long orgId = Long.valueOf(nextOrgIds);
						
						OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
						
						if(orgInfo != null) {
							userInfo.setOrgId(orgInfo.getOrgId());
							userInfo.setOrgCode(orgInfo.getOrgCode());
							userInfo.setOrgName(orgInfo.getOrgName());
						}
					}
				}
				if(CommonFunctions.isNotBlank(curTaskData, "WF_USERID_ALL")) {
					nextUserIds = curTaskData.get("WF_USERID_ALL").toString().split(",")[0];
					
					if(StringUtils.isNotBlank(nextUserIds)) {
						Long userId = Long.valueOf(nextUserIds.split(",")[0]);
						
						UserBO userBO = userManageService.getUserInfoByUserId(userId);
						
						if(userBO != null) {
							userInfo.setUserId(userBO.getUserId());
							userInfo.setPartyName(userBO.getPartyName());
							userInfo.setVerifyMobile(String.valueOf(userBO.getVerifyMobile()));
						}
					}
				}
				
				//采集并结案，默认设置评价信息
				if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)) {
					Map<String, Object> evaluateParams = new HashMap<String, Object>();
					nextNodeName = "end1";//结束节点编码
					
					evaluateParams.putAll(extraParam);
					evaluateParams.put("isEvaluate", true);
					evaluateParams.put("eventId", eventId);
					evaluateParams.put("evaLevel", "02");//默认满意评价等级
					evaluateParams.put("evaContent", "满意");
					
					subFlag = this.subWorkFlowForEndingAndEvaluate(instanceIdL, curTaskId, nextNodeName, nextUserIds, nextOrgIds, advice, userInfo, null, evaluateParams);
				}
			}
		}
		
		return instanceId;
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception {
		boolean flag = false, isForce2End = false;
		String applicationType = null, intervalUnit = null;
		Integer interval = 0;
		Long eventId = null;
		
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
	    if(CommonFunctions.isNotBlank(extraParam, "isForce2End")) {
	    	isForce2End = Boolean.valueOf(extraParam.get("isForce2End").toString());
	    }
	    
	    if(CommonFunctions.isNotBlank(extraParam, "eventId")) {
			try {
				eventId = Long.valueOf(extraParam.get("eventId").toString());
			} catch(NumberFormatException e) {}
		} else if(CommonFunctions.isNotBlank(extraParam, "formId")) {
			try {
				eventId = Long.valueOf(extraParam.get("formId").toString());
			} catch(NumberFormatException e) {}
		} else {
			ProInstance proInstance = this.capProInstanceByInstanceId(instanceId);
			
			eventId = proInstance.getFormId();
		}
	    
	    if(DEPARTMENT_NODE_CODE.equals(nextNodeName)) {
			StringBuffer sponsor = new StringBuffer(""), cosponsor = new StringBuffer("");
			String[] nextStaffNameArray = null, nextOrgNameArray = null;
			String mainNextStaffId="";
			String mainCurOrgIds="";
			
			if(StringUtils.isNotBlank(nextStaffId)) {
				mainNextStaffId = nextStaffId.split(",")[0]; 
			}
			if(StringUtils.isNotBlank(curOrgIds)) {
				mainCurOrgIds = curOrgIds.split(",")[0]; 
			}
			
			try {
				//将主办人员存入当前事件中间表中
				if(StringUtils.isNotBlank(mainNextStaffId) && StringUtils.isNotBlank(mainCurOrgIds)) {
					Map<String,Object> eventExtend = new HashMap<String,Object>();
					
					eventExtend.put("isLeadUser", "1");
					eventExtend.put("eventId", eventId);
					eventExtend.put("creatorId", Long.valueOf(mainNextStaffId));
					eventExtend.put("creatorOrgId", Long.valueOf(mainCurOrgIds));
					
					eventExtendService.saveEventExtend(eventExtend);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(CommonFunctions.isNotBlank(extraParam, "nextStaffNames")) {
				nextStaffNameArray = extraParam.get("nextStaffNames").toString().split(",");
			}
			
			if(CommonFunctions.isNotBlank(extraParam, "nextOrgNames")) {
				nextOrgNameArray = extraParam.get("nextOrgNames").toString().split(",");
			}
			
			if(nextStaffNameArray != null && nextOrgNameArray != null) {
				int nextStaffNameArrayLen = nextStaffNameArray.length,
					nextOrgNameArrayLen = nextOrgNameArray.length;
				
				if(nextStaffNameArrayLen > 0 && nextOrgNameArrayLen > 0) {
					sponsor.append(nextStaffNameArray[0]).append("(").append(nextOrgNameArray[0]).append(")");
				}
				
				if(nextStaffNameArrayLen > 1 && nextOrgNameArrayLen > 1) {
					for(int index = 1; index < nextStaffNameArrayLen; index++) {
						cosponsor.append(";").append(nextStaffNameArray[index]);
						
						if(index < nextOrgNameArrayLen) {
							cosponsor.append("(").append(nextOrgNameArray[index]).append(")");
						}
					}
				}
			}
			
			if(cosponsor.length() > 0) {
				cosponsor.deleteCharAt(0);
			} else {
				cosponsor.append("暂无");
			}
			
			advice += " 主办人员为【" + sponsor + "】，协办人员为【" + cosponsor + "】";
		}
		
	    if(isForce2End && END_NODE_CODE.equals(nextNodeName)) {
	    	if(CommonFunctions.isNotBlank(extraParam, "caseId")) {
	    		StringBuffer sceventAdvice = new StringBuffer("事件已转案件处理");
	    		
	    		if(CommonFunctions.isNotBlank(extraParam, "caseNumCode")) {
	    			sceventAdvice.append("，案件立案号为：").append(extraParam.get("caseNumCode")).append("。");
	    		} else {
	    			sceventAdvice.append("。");
	    		}
	    		
	    		extraParam.put("advice", sceventAdvice.toString());
	    	}
	    	
	    	flag = baseWorkflowService.endWorkflow4Base(instanceId, userInfo, extraParam);
	    	
	    	if(flag) {
	    		LinkedHashMap<String, Date> duedate = new LinkedHashMap<String, Date>();
	    		Map<String, Object> statusParam = new HashMap<String, Object>();
	    		
                duedate.put(END_NODE_CODE + "_" + END_NODE_CODE, new Date());
                
                statusParam.putAll(extraParam);
                statusParam.put("duedate", duedate);
                
	    		updateEventStatus(eventId, userInfo.getUserId(), userInfo.getOrgId(), statusParam);
	    	}
	    } else {
	    	flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
	    }
		
		if(flag && RECEPTIONIST_NODE_CODE.equals(nextNodeName)) {
			UserInfo receptUserInfo = new UserInfo();
			Long receptUserId = Long.valueOf(nextStaffId.split(",")[0]),
				 receptOrgId = Long.valueOf(curOrgIds.split(",")[0]);
			UserBO receptUserBO = null;
			OrgSocialInfoBO receptOrgInfo = null;
			Map<String, Object> nextTaskParams = new HashMap<String, Object>();
			List<Node> nextNodeList = null;
			Integer nextNodeId = null;
			
			receptUserBO = userManageService.getUserInfoByUserId(receptUserId);
			receptOrgInfo = orgSocialInfoService.findByOrgId(receptOrgId);
			
			if(receptUserBO != null && receptUserBO.getUserId() != null) {
				receptUserInfo.setUserId(receptUserId);
				receptUserInfo.setPartyName(receptUserBO.getPartyName());
				receptUserInfo.setVerifyMobile(String.valueOf(receptUserBO.getVerifyMobile()));
			}
			if(receptOrgInfo != null && receptOrgInfo.getOrgId() != null) {
				receptUserInfo.setOrgId(receptOrgInfo.getOrgId());
				receptUserInfo.setOrgName(receptOrgInfo.getOrgName());
				receptUserInfo.setOrgCode(receptOrgInfo.getOrgCode());
			}
			
			nextTaskParams.put("instanceId", instanceId);
			nextTaskParams.put("curTaskName", RECEPTIONIST_NODE_CODE);
			
			nextNodeList = baseWorkflowService.findNextTaskNodes(null, null, null, receptUserInfo, nextTaskParams);
			
			if(nextNodeList != null) {
				for(Node nextNode : nextNodeList) {
					if(DEPARTMENT_NODE_CODE.equals(nextNode.getNodeName())) {
						nextNodeId = nextNode.getNodeId();
						break;
					}
				}
			}
			
			if(nextNodeId != null) {
				Map<String, Object> fiveKeyParams = new HashMap<String, Object>(),
									resultMap = null;
				String nextUserIds = null, nextOrgIds = null, nextAdvice = "请尽快办理！",
					   //U3f1D0
					   nodeCode = INodeCodeHandler.ORG_UINT + INodeCodeHandler.COUNTY + INodeCodeHandler.OPERATE_FLOW_GLOABAL + "1" + INodeCodeHandler.ORG_DEPT + "0";
				Long curTaskId = this.curNodeTaskId(instanceId);
				
				fiveKeyParams.putAll(extraParam);
				fiveKeyParams.put("instanceId", instanceId);
				
				resultMap = fiveKeyElementService.getNodeInfoForEvent(receptUserInfo, RECEPTIONIST_NODE_CODE, DEPARTMENT_NODE_CODE, nodeCode, nextNodeId.toString(), fiveKeyParams);
				
				if(CommonFunctions.isNotBlank(resultMap, "userIds")) {
					nextUserIds = resultMap.get("userIds").toString();
				}
				if(CommonFunctions.isNotBlank(resultMap, "orgIds")) {
					nextOrgIds = resultMap.get("orgIds").toString();
				}
				
				extraParam.putAll(resultMap);
				extraParam.put("advice", nextAdvice);
				extraParam.put("nodeId", nextNodeId);
				extraParam.put("curTaskId", curTaskId);
				
				if(CommonFunctions.isNotBlank(resultMap, "userNames")) {
					extraParam.put("nextStaffNames", resultMap.get("userNames"));
				}
				
				if(CommonFunctions.isNotBlank(resultMap, "orgNames")) {
					extraParam.put("nextOrgNames", resultMap.get("orgNames"));
				}
				
				if(StringUtils.isNotBlank(nextUserIds) && StringUtils.isNotBlank(nextOrgIds)
						&& CommonFunctions.isNotBlank(extraParam, "interval")
						&& CommonFunctions.isNotBlank(extraParam, "intervalUnit")) {
					flag = this.subWorkFlowForEndingAndEvaluate(instanceId, String.valueOf(curTaskId), DEPARTMENT_NODE_CODE, nextUserIds, nextOrgIds, nextAdvice, receptUserInfo, null, extraParam);
				}
			}
		} else if(flag && DEPARTMENT_NODE_CODE.equals(nextNodeName)) {
			if(CommonFunctions.isNotBlank(extraParam, "interval")) {
				try {
					interval = Integer.valueOf(extraParam.get("interval").toString());
				} catch(NumberFormatException e) {}
			}
			
			if(CommonFunctions.isNotBlank(extraParam, "intervalUnit")) {
				intervalUnit = extraParam.get("intervalUnit").toString();
			}
			
			if(interval > 0) {
				applicationType = ITimeApplicationService.APPLICATION_TYPE.EVENT_SEND.getValue();
			}
		}
		
		if(StringUtils.isNotBlank(applicationType)) {
			Map<String, Object> extendMap = new HashMap<String, Object>(),
								applicationMap = new HashMap<String, Object>();
			
			extendMap.put("interval", interval);
			extendMap.put("intervalUnit", intervalUnit);
			extendMap.put("isAutoAudit", true);
			extendMap.put("timeAppCheckStatus", ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.getValue());
			
			applicationMap = timeApplicationService.initTimeApplication4Event(eventId, applicationType, userInfo, extendMap);
			
			timeApplicationService.saveTimeApplication(applicationMap);
		}
		
		return flag;
	}
	
	@Override
	public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {
		int resultCode = 0;
		String wrongMsg = "";
		
		if(orgSocialInfo != null) {
			String orgType = orgSocialInfo.getOrgType(),
				   chiefLevel = orgSocialInfo.getChiefLevel();
			
			if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgType) 
					|| String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType)) {
				if((String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgType)
						&& ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevel))
					||
					(String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType)
							&& (ConstantValue.STREET_ORG_LEVEL.equals(chiefLevel)
									|| ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevel)))) {
					resultCode = 1;
					wrongMsg = "";
				} else {
					resultCode = -2;
					wrongMsg = "当前组织层级没有启动事件流程权限！";
				}
			} else {
				resultCode = -1;
				wrongMsg = "当前组织的组织类型有误：既不是单位，也不是部门。请先修正！";
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
	
	@Override
	public Map<String, Object> capInfo4SMS(String curNodeName, String nextNodeName, Map<String, Object> params, UserInfo userInfo) throws Exception {
		Map<String, Object> resultMap = null;
		
		if(DEPARTMENT_NODE_CODE.equals(nextNodeName)) {
			if(CommonFunctions.isNotBlank(params, "nextStaffId") && CommonFunctions.isBlank(params, "receiverIds")) {
				params.put("receiverIds", params.get("nextStaffId"));
			}
		}
		
		resultMap = super.capInfo4SMS(curNodeName, nextNodeName, params, userInfo);
		
		if(DEPARTMENT_NODE_CODE.equals(nextNodeName) && CommonFunctions.isNotBlank(resultMap, "smsContent")) {
			String smsContent = resultMap.get("smsContent").toString();
			StringBuffer sponsor = new StringBuffer(""), cosponsor = new StringBuffer("");
			String[] nextStaffNameArray = null, nextOrgNameArray = null;
			
			if(CommonFunctions.isNotBlank(params, "nextStaffNames")) {
				nextStaffNameArray = params.get("nextStaffNames").toString().split(",");
			}
			
			if(CommonFunctions.isNotBlank(params, "nextOrgNames")) {
				nextOrgNameArray = params.get("nextOrgNames").toString().split(",");
			}
			
			if(nextStaffNameArray != null && nextOrgNameArray != null) {
				int nextStaffNameArrayLen = nextStaffNameArray.length,
					nextOrgNameArrayLen = nextOrgNameArray.length;
				
				if(nextStaffNameArrayLen > 0 && nextOrgNameArrayLen > 0) {
					sponsor.append(nextStaffNameArray[0]).append("(").append(nextOrgNameArray[0]).append(")");
				}
				
				if(nextStaffNameArrayLen > 1 && nextOrgNameArrayLen > 1) {
					for(int index = 1; index < nextStaffNameArrayLen; index++) {
						cosponsor.append(";").append(nextStaffNameArray[index]);
						
						if(index < nextOrgNameArrayLen) {
							cosponsor.append("(").append(nextOrgNameArray[index]).append(")");
						}
					}
				}
			}
			
			if(cosponsor.length() > 0) {
				cosponsor.deleteCharAt(0);
			} else {
				cosponsor.append("暂无");
			}
			
			if(smsContent.contains("@sponsor")) {
				smsContent = smsContent.replace("@sponsor", sponsor.toString());
			}
			if(smsContent.contains("@cosponsor")) {
				smsContent = smsContent.replace("@cosponsor", cosponsor.toString());
			}
			
			resultMap.put("smsContent", smsContent);
		}
		
		return resultMap;
	}
	
	@Override
	public String capSmsContent(String taskId, String noteType, String advice, EventDisposal event, UserInfo userInfo) {
		String smsContent = null;
		
		if(ConstantValue.DEPARTMENT_HANDLE_NODE.equals(noteType)) {
			//发送短信内容
			smsContent = funConfigurationService.turnCodeToValue(ConstantValue.SMS_NOTE_TYPE, noteType, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			
			if(StringUtils.isNotBlank(event.getCode()) && smsContent.contains("@eventCode")) {
				smsContent = smsContent.replace("@eventCode", event.getCode());
			}
			if(StringUtils.isNotBlank(event.getEventClass()) && smsContent.contains("@eventClass")) {
				smsContent = smsContent.replace("@eventClass", event.getEventClass());
			}
		} else {
			smsContent = super.capSmsContent(taskId, noteType, advice, event, userInfo);
		}
		
		return smsContent;
	}
	
	@Override
	public Map<String, Object> capTurnLevel(String curnodeName, String nextNodeName, UserInfo userInfo) {
		Map<String, Object> result = null;
		
		if(DEPARTMENT_NODE_CODE.equals(nextNodeName)) {
			result = new HashMap<String, Object>();
			
			result.put("level", ConstantValue.EVENT_SHUNT);
			result.put("noteType", ConstantValue.DEPARTMENT_HANDLE_NODE);
		} else {
			result = super.capTurnLevel(curnodeName, nextNodeName, userInfo);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		Map<String, Object> resultMap = super.initFlowInfo(taskId, instanceId, userInfo, params);
		String curNodeName = null;
		
		if(CommonFunctions.isNotBlank(resultMap, "curNodeName")) {
			curNodeName = resultMap.get("curNodeName").toString();
		}
		
		if(DEPARTMENT_NODE_CODE.equals(curNodeName)) {
			Map<String, Object> eventExtendMap = null;
			String mainHandleUserId = "", mainHandleUserOrgId = "", IS_LEADER_USER = "1";
			Long eventId = null;
			
			if(CommonFunctions.isNotBlank(resultMap, "proInstance")) {
				ProInstance proInstance = (ProInstance) resultMap.get("proInstance");
				eventId = proInstance.getFormId();
			}
			
			eventExtendMap = eventExtendService.findEventExtendByEventId(eventId);
			if(CommonFunctions.isNotBlank(eventExtendMap, "isLeadUser")
					&& IS_LEADER_USER.equals(eventExtendMap.get("isLeadUser").toString())) {
				if(CommonFunctions.isNotBlank(eventExtendMap, "creatorId")) {
					mainHandleUserId = eventExtendMap.get("creatorId").toString();
				}
				if(CommonFunctions.isNotBlank(eventExtendMap, "creatorOrgId")) {
					mainHandleUserOrgId = eventExtendMap.get("creatorOrgId").toString();
				}
			}
				
			if(userInfo != null 
					&& (!mainHandleUserId.equals(String.valueOf(userInfo.getUserId()))
							|| !mainHandleUserOrgId.equals(String.valueOf(userInfo.getOrgId())))) {
				List<Node> taskNodes = new ArrayList<Node>();
				Node node = new Node();
				
				if(CommonFunctions.isNotBlank(resultMap, "operateLists")) {
					String OPERATE_EVENT_SUB = "subTask";
					List<OperateBean> operateReminList = null,
									  operateList = (List<OperateBean>) resultMap.get("operateLists");
					
					for(OperateBean operateBean : operateList) {
						if(OPERATE_EVENT_SUB.equals(operateBean.getOperateEvent())) {
							operateReminList = new ArrayList<OperateBean>();
							operateReminList.add(operateBean);
							break;
						}
					}
					
					resultMap.put("operateLists", operateReminList);
				}
				
				node.setNodeId(-1);
				node.setNodeName(ConstantValue.HANDLING_TASK_CODE);
				node.setNodeNameZH(ConstantValue.HANDLING_TASK_NAME);//环节中文名称
				node.setDynamicSelect("1");//动态跳转
				node.setNodeType("1");
				node.setTransitionCode(null);//设置为采集人办理
				
				taskNodes.add(node);
				
				resultMap.put("taskNodes", taskNodes);
			}
		}
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int sqlOrder, UserInfo userInfo){
		List<Map<String, Object>> workflowList = this.queryProInstanceDetail(instanceId, sqlOrder),
								  taskList = new ArrayList<Map<String, Object>>(),
								  subTaskList = null,
								  subAndReceivedTaskList = null,
								  subAndReceivedTaskListTmp = null,
								  timeAndRemarkList = null;
		
		/**
		 * PARENT_TASK_ID
		 * TASK_ID，
		 * TASK_TYPE，
		 * TASK_NAME，
		 * OPERATE_TYPE，
		 * TRANSACTOR_ID，
		 * TRANSACTOR_NAME，
		 * ORG_NAME，ORG_ID，
		 * REMARKS，START_TIME，END_TIME
		 */
		
		if(workflowList != null) {//新增子任务（会签任务和处理中任务）
			Long taskId = -1L;
			List<TaskReceive> taskReceivedList = null;
			
			ProInstance pro = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
			EventDisposal findEventByIdSimple = eventDisposalService.findEventByIdSimple(pro.getFormId());
			if("04".equals(findEventByIdSimple.getStatus())) {
				Map<String,Object> endNode=new HashMap<String,Object>();
				endNode.put("TASK_TYPE", "1");
				endNode.put("TASK_NAME", "结案");
				taskList.add(endNode);
			}
			
			for(Map<String, Object> taskMap : workflowList) {
				
				
				if(CommonFunctions.isNotBlank(taskMap, "TASK_ID")) {
					int subCount = 0;
					boolean isCurrentTask = false;
					
					if(CommonFunctions.isNotBlank(taskMap, "SUB_COUNT")) {
						try {
							subCount = Integer.valueOf(taskMap.get("SUB_COUNT").toString());
						} catch(NumberFormatException e) {
							subCount = 0;
						}
					}
					
					if(CommonFunctions.isNotBlank(taskMap, "IS_CURRENT_TASK")) {//判断是否是当前任务
						isCurrentTask = Boolean.valueOf(taskMap.get("IS_CURRENT_TASK").toString());
					}
					
					if(subCount > 0) {//构建会签环节的办理人员信息
						if(CommonFunctions.isNotBlank(taskMap, "TRANSACTOR_NAME") && CommonFunctions.isNotBlank(taskMap, "ORG_NAME")) {
							String[] transactorNames = taskMap.get("TRANSACTOR_NAME").toString().split(",");
							String[] orgNames = taskMap.get("ORG_NAME").toString().split(",");
							StringBuffer subTaskPerson = new StringBuffer("");
							
							for(int index = 0, len = transactorNames.length; index < len; index++) {
								subTaskPerson.append(transactorNames[index]).append("(").append(orgNames[index]).append(");");
							}
							
							taskMap.put("SUB_HANDLE_PERSON", subTaskPerson);
						}
					}
					
					subAndReceivedTaskList = new ArrayList<Map<String, Object>>();
					//获取任务接收时间列表
					taskId  = Long.valueOf(taskMap.get("TASK_ID").toString());
					taskReceivedList = this.findTaskReceivedList(taskId, instanceId, null);
					
					if(taskReceivedList != null && taskReceivedList.size() > 0) {
						Map<String, Object> taskReceiveMap = null;
						for(TaskReceive taskReceive : taskReceivedList) {
							taskReceiveMap = new HashMap<String, Object>();
							taskReceiveMap.put("TASK_ID", taskReceive.getTaskId());
							taskReceiveMap.put("TRANSACTOR_ID", taskReceive.getUserId());
							taskReceiveMap.put("TRANSACTOR_NAME", taskReceive.getUserName());
							taskReceiveMap.put("ORG_ID", taskReceive.getOrgId());
							taskReceiveMap.put("ORG_NAME", taskReceive.getOrgName());
							try {
								taskReceiveMap.put("RECEIVE_TIME", DateUtils.convertDateToString(taskReceive.getReceiveTime()));
							} catch (ParseException e) {
								e.printStackTrace();
							}
							
							subAndReceivedTaskList.add(taskReceiveMap);
						}
					}
					
					if(subCount > 0 || isCurrentTask) {//子任务不为空或者是当前任务时，才查找子任务
						subTaskList = this.querySubTaskDetails(taskMap.get("TASK_ID").toString(), sqlOrder, userInfo);
						
						if(subTaskList != null && subTaskList.size() > 0) {
							for(Map<String, Object> subTask : subTaskList) {
								if(CommonFunctions.isNotBlank(subTask, "TASK_NAME")) {//清除处理中的环节名称
									if(ConstantValue.HANDLING_TASK_CODE.equals(subTask.get("TASK_NAME"))) {
										subTask.remove("TASK_NAME");
									}
								}
								
								subAndReceivedTaskList.add(subTask);
							}
							
							taskMap.put("subTaskList", subTaskList);
						}
					}
					
					if(subAndReceivedTaskList.size() > 0) {
						subAndReceivedTaskListTmp = new ArrayList<Map<String, Object>>();
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
						
						taskMap.put("subAndReceivedTaskList", subAndReceivedTaskListTmp);
					}
				}
				
				taskList.add(taskMap);
			}
		}
		
		return taskList;
	}
	
}
