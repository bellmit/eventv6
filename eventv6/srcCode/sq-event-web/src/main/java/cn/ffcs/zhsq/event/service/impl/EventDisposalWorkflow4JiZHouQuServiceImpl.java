package cn.ffcs.zhsq.event.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.PositionInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.PositionInfoOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 江西省吉安市吉州区(Ji ZHou Qu) 工作流使用接口实现
 * @ClassName:   EventDisposalWorkflow4JiZHouQuServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年8月24日 下午4:13:55
 */
@Service(value = "eventDisposalWorkflow4JiZHouQuService")
public class EventDisposalWorkflow4JiZHouQuServiceImpl extends EventDisposalWorkflowForJXPlatformServiceImpl {
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private PositionInfoOutService positionInfoService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventExtendService eventExtendService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	private static final String GRID_ADMIN_HANDLE_NODE_CODE = "task3";
	private static final String DISTRICT_UNIT_ACCEPT_NODE_CODE = "task10";	//县区综治中心受理
	private static final String COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE = "task22";//八员队伍办理
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE.equals(nextNodeName)) {
			extraParam.put("isUseParent", false);
		}
		
		flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		
		if(flag) {
			if(COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE.equals(nextNodeName)) {
				Node nextNode = findNodeById(instanceId, COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE);
				List<Map<String, Object>> nodeActorMapList = null;
				ProInstance proInstance = capProInstanceByInstanceId(instanceId);
				
				if(nextNode != null) {
					nodeActorMapList = findNodeActorsById(nextNode.getNodeId());
				}
				
				if(nodeActorMapList != null) {
					String positionName = null, trigCondition = null, emtType = null;
					Map<String, Object> positionParam = new HashMap<String, Object>();
					List<PositionInfoBO> positionList = null;
					String[] nextUserIdArray = nextStaffId.split(","),
							 nextOrgIdArray = curOrgIds.split(",");
					int arrayLength = nextUserIdArray.length;
					
					nodeActorLabel:
					for(Map<String, Object> nodeActorMap : nodeActorMapList) {
						if(CommonFunctions.isNotBlank(nodeActorMap, "POSITION_ID") 
								&& CommonFunctions.isNotBlank(nodeActorMap, "ACTOR_NAME")) {
							positionName = nodeActorMap.get("ACTOR_NAME").toString();
						}
						
						if(StringUtils.isNotBlank(positionName)) {
							for(int index = 0; index < arrayLength; index++) {
								positionParam.put("orgId", nextOrgIdArray[index]);
								positionParam.put("userId", nextUserIdArray[index]);
								positionParam.put("positionName", positionName);
								
								positionList = positionInfoService.queryPositionByPara(positionParam);
								
								if(positionList != null && positionList.size() > 0) {
									break nodeActorLabel;
								}
							}
						}
						
						positionName = null;
					}
					
					//获取八员队伍人员类型
					trigCondition = "EMT-" + proInstance.getProName() + "-" + COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE + "-" + positionName;
					
					emtType = funConfigurationService.turnCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, trigCondition, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
					
					if(StringUtils.isNotBlank(emtType)) {
						Map<String, Object> eventExtend = new HashMap<String, Object>();
						Long eventId = proInstance.getFormId();
						
						eventExtend.put("eventId", eventId);
						eventExtend.put("emtType", emtType);
						eventExtend.put("creatorId", userInfo.getUserId());
						eventExtend.put("creatorOrgId", userInfo.getOrgId());
						
						try {
							eventExtendService.saveEventExtend(eventExtend);
						} catch (Exception e) {
							if(logger.isErrorEnabled()) {
								logger.error("提交事件，八员队伍扩展信息增添失败：事件id【" + eventId + "】，八员队伍类型【" + emtType + "】！");
							}
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		return flag;
	}
	
	@Override
	public String capWorkflowName(Long eventId, String eventType, UserInfo userInfo) {
		String eventSource = null,
			   workflowName = null,
			   WORKFLOW_NAME_4_CODE = "江西省吉州区码上办事件流程",//由于已是个性流程，因此不通过修改功能配置规则进行配置
			   SOURCE_4_CODE = "16";//码上办事件采集来源
		
		if(eventId != null && eventId > 0) {
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			
			if(event != null) {
				eventSource = event.getSource();
			}
		}
		
		if(SOURCE_4_CODE.equals(eventSource)) {
			workflowName = WORKFLOW_NAME_4_CODE;
		} else {
			workflowName = super.capWorkflowName(eventId, eventType, userInfo);
		}
		
		return workflowName;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		Map<String, Object> resultMap = super.initFlowInfo(taskId, instanceId, userInfo, params);
		String curNodeName = null, eventStatus = null;
		EventDisposal event = null;
		
		if(CommonFunctions.isNotBlank(resultMap, "curNodeName")) {
			curNodeName = resultMap.get("curNodeName").toString();
		}
		
		if(CommonFunctions.isNotBlank(resultMap, "event4Simple")) {
			event = (EventDisposal) resultMap.get("event4Simple");
		} else {
			Long eventId = null;
			
			try {
				if(CommonFunctions.isNotBlank(resultMap, "proInstance")) {
					ProInstance proInstance = (ProInstance) resultMap.get("proInstance");
					
					eventId = proInstance.getFormId();
				} else if(CommonFunctions.isNotBlank(params, "eventId")) {
					eventId = Long.valueOf(params.get("eventId").toString());
				} else if(CommonFunctions.isNotBlank(params, "formId")) {
					eventId = Long.valueOf(params.get("formId").toString());
				}
			} catch(Exception e) {}
			
			if(eventId != null && eventId > 0) {
				event = eventDisposalService.findEventByIdSimple(eventId);
			}
		}
		
		if(event != null) {
			eventStatus = event.getStatus();
		}
		
		if(COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE.equals(curNodeName)) {
			//网格员上报给八员队伍处理时，八员队伍不可进行驳回操作
			if(ConstantValue.EVENT_STATUS_REPORT.equals(eventStatus) 
					&& CommonFunctions.isNotBlank(resultMap, "operateLists")) {
				String taskName = null;
				List<Map<String, Object>> taskMapList = baseWorkflowService.capHandledTaskInfoMap(instanceId, "DESC", params);
				
				//获取首个非对接流程中的“通过”操作节点
				if(taskMapList != null && taskMapList.size() > 0) {
					String operateType = null, OPERATE_TYPE_PASS = "1";
					
					for(Map<String, Object> taskMap : taskMapList) {
						operateType = null;
						
						if(CommonFunctions.isNotBlank(taskMap, "NODE_ID")) {
							if(CommonFunctions.isNotBlank(taskMap, "OPERATE_TYPE")) {
								operateType = taskMap.get("OPERATE_TYPE").toString();
							}
						}
						
						if(OPERATE_TYPE_PASS.equals(operateType)) {
							if(CommonFunctions.isNotBlank(taskMap, "TASK_CODE")) {
								taskName = taskMap.get("TASK_CODE").toString();
								break;
							}
						}
					}
				}
				
				if(GRID_ADMIN_HANDLE_NODE_CODE.equals(taskName)) {
					List<OperateBean> operateList = (List<OperateBean>) resultMap.get("operateLists");
					OperateBean rejectOperate = null;
					String REJECT_OPERATE_EVENT = "reject";
					
					for(OperateBean operateBean : operateList) {
						if(REJECT_OPERATE_EVENT.equals(operateBean.getOperateEvent())) {
							rejectOperate = operateBean;
							break;
						}
					}
					
					if(rejectOperate != null) {
						operateList.remove(rejectOperate);
						
						resultMap.put("operateLists", operateList);
					}
				}
			}
			
			if(CommonFunctions.isNotBlank(resultMap, "taskNodes")) {
				List<Node> taskNodeList = (List<Node>) resultMap.get("taskNodes");
				String DISTRICT_UNIT_ACCEPT_ALIAS = "管辖申述";//县级综治中心受理节点别名
				
				for(Node taskNode : taskNodeList) {
					if(DISTRICT_UNIT_ACCEPT_NODE_CODE.equals(taskNode.getNodeName())) {
						taskNode.setNodeNameZH(DISTRICT_UNIT_ACCEPT_ALIAS);
						break;
					}
				}
				
				resultMap.put("taskNodes", taskNodeList);
			}
		}
		
		return resultMap;
	}
	
	@Override
	public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {
		int resultCode = 0;
		String wrongMsg = "当前组织不存在，请先检验！";
		
		if(orgSocialInfo != null && orgSocialInfo.getOrgId() != null) {
			String chiefLevel = orgSocialInfo.getChiefLevel();
			
			if(StringUtils.isBlank(chiefLevel)) {
				resultCode = -1;
				wrongMsg = "当前组织的组织层级为空，请先检验！";
			} else {
				if(ConstantValue.DISTRICT_ORG_LEVEL.compareTo(chiefLevel) > 0) {
					resultCode = -1;
					wrongMsg = "当前组织的组织层级超过了【区/县】！";
				} else {
					resultCode = 1;
					wrongMsg = "";
				}
			}
		}
		
		if(StringUtils.isNotBlank(wrongMsg)) {
			throw new Exception(wrongMsg);
		}
		
		return resultCode;
	}
	
	/**
	 * 构造节点人员选择类型
	 * @return
	 * 		1 选择人员；
	 * 		2 选择组织；
	 */
	protected Map<String, String> init4TaskUserConstruct() {
		Map<String, String> initMap = super.init4TaskUserConstruct();
		
		initMap.put(COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE, "1");
		
		return initMap;
	}
}
