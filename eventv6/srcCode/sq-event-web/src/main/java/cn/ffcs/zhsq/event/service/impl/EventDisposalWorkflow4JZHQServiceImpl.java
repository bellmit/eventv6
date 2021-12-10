package cn.ffcs.zhsq.event.service.impl;

import java.util.Date;
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
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.api.enums.BizPlatfromEnum;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * @Description: 江西省吉安市吉州区(Ji ZHou Qu) 工作流使用接口实现，被替换为EventDisposalWorkflow4JiZHouQuServiceImpl
 * @ClassName:   EventDisposalWorkflow4JZHQServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年7月27日 上午10:25:56
 */
@Service(value = "eventDisposalWorkflow4JZHQService")
@Deprecated
public class EventDisposalWorkflow4JZHQServiceImpl extends EventDisposalWorkflowForEventNewServiceImpl {
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEvent4WorkflowService event4WorkflowService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private PositionInfoOutService positionInfoService;
	
	@Autowired
	private IEventExtendService eventExtendService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
    public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		EventDisposal event = null;
		String instanceId = null;
		
		if(userInfo == null || userInfo.getUserId() == null || userInfo.getOrgId() == null) {
			throw new IllegalArgumentException("缺少有效的用户信息！");
		}
		
		if(eventId == null || eventId < 0) {
			if(CommonFunctions.isNotBlank(extraParam, "eventId")) {
				eventId = Long.valueOf(extraParam.get("eventId").toString());
			} else if(CommonFunctions.isNotBlank(extraParam, "formId")) {
				eventId = Long.valueOf(extraParam.get("formId").toString());
			}
		}

		if(eventId != null && eventId > 0) {
			event = eventDisposalService.findEventByIdSimple(eventId);
		} else {
			throw new IllegalArgumentException("缺少有效的事件id！");
		}
		
		if(event == null) {
			throw new IllegalArgumentException("事件id【" + eventId + "】未能找到有效的事件信息！");
		} else if(!BizPlatfromEnum.JX_PAZYZ.getValue().equals(event.getBizPlatform())) {
			Map<String, Object> eventCountParam = new HashMap<String, Object>();
			Date createDateTimeEnd = new Date(),
				 createDateTimeStart = new Date(createDateTimeEnd.getTime() - 3600 * 1000);
			
			eventCountParam.put("listType", 6);//我发起的列表
			eventCountParam.put("initiatorId", userInfo.getUserId());
			eventCountParam.put("initiatorOrgId", userInfo.getOrgId());
			eventCountParam.put("infoOrgCodeAccurate", event.getGridCode());
			eventCountParam.put("eventNameAccurate", event.getEventName());
			eventCountParam.put("eliminateEventIdList", eventId.toString());
			eventCountParam.put("createDateTimeStart", DateUtils.formatDate(createDateTimeStart, DateUtils.PATTERN_24TIME));
			eventCountParam.put("createDateTimeEnd", DateUtils.formatDate(createDateTimeEnd, DateUtils.PATTERN_24TIME));
			
			if(event4WorkflowService.findEventCount(eventCountParam) > 0) {
				throw new ZhsqEventException("事件标题重复！");
			}
		}

        instanceId = super.startFlowByWorkFlow(eventId, workFlowId, ConstantValue.WORKFLOW_IS_NO_CLOSE, userInfo, extraParam);
        
        if(StringUtils.isNotBlank(instanceId) && ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)) {
        	String END_NODE_CODE = "end1", advice = null;
        	Long instanceIdL = Long.valueOf(instanceId),
        		 curTaskId = this.curNodeTaskId(instanceIdL);
        	
        	if(CommonFunctions.isNotBlank(extraParam, "advice")) {
        		advice = extraParam.get("advice").toString();
        	}
        	
        	if(curTaskId != null) {
        		subWorkFlowForEndingAndEvaluate(instanceIdL, curTaskId.toString(), END_NODE_CODE, null, null, advice, userInfo, null, extraParam);
        	}
        }
        
        return instanceId;
    }
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception {
		boolean flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		
		if(flag) {
			String COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE = "task12";//八员队伍办理
			
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
}
