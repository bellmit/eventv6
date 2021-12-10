package cn.ffcs.zhsq.decisionMaking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.PositionInfoBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.PositionInfoOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 江西省吉安市吉州区(Ji ZHou Qu) 工作流节点跳转决策类
 * 必填参数
 * decisionService	指定实现类 workflowEventDecisionMaking4JZHQService
 * instanceId		流程实例id
 * curUserId		当前用户
 * curOrgId			当前用户所属组织
 * curNodeCode		当前环节节点编码
 * 
 * @ClassName:   WorkflowEventDecisionMaking4JZHQServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年7月28日 下午2:45:29
 */
@Service(value = "workflowEventDecisionMaking4JZHQService")
public class WorkflowEventDecisionMaking4JZHQServiceImpl extends WorkflowEventDecisionMakingServiceImpl {
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private PositionInfoOutService positionInfoService;
	
	@Autowired
	private IEventExtendService eventExtendService;
	
	private static final String COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE = "task12";
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		String nextNodeName = "",	//下一环节节点编码
			   curNodeCode = null;
		
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺少当前办理环节【curNodeCode】！");
		}
		
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) {//决策1
			Long curOrgId = null;
			String orgChiefLevel = null;
			
			if(CommonFunctions.isNotBlank(params, "curOrgId")) {
				try {
					curOrgId = Long.valueOf(params.get("curOrgId").toString());
				} catch(NumberFormatException e) {}
			}
			
			if(curOrgId != null && curOrgId > 0) {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(curOrgId);
				if(orgInfo != null && orgInfo.getOrgId() != null) {
					orgChiefLevel = orgInfo.getChiefLevel();
					
					if(CommonFunctions.isBlank(params, "userOrgCode")) {
						params.put("userOrgCode", orgInfo.getOrgCode());
					}
				}
			}
			
			if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)) {
				if(isUserEMT(params)) {
					nextNodeName = COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE;
				}
			}
			
			if(StringUtils.isBlank(nextNodeName)) {
				nextNodeName = super.makeDecision(params);
			}
		}
		
		if(StringUtils.isBlank(nextNodeName)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		return nextNodeName;
	}
	
	/**
	 * 判断当前人员是否为八员队伍人员
	 * @param params
	 * 			instanceId	流程实例id
	 * 			curOrgId	办理人员组织id
	 * 			curUserId	办理人员用户id
	 * 			userOrgCode	组织编码
	 * @return
	 */
	private boolean isUserEMT(Map<String, Object> params) {
		boolean flag = false;
		Long instanceId = null;
		String curOrgId = null, curUserId = null, userOrgCode = null;
		
		if(CommonFunctions.isBlank(params, "instanceId")) {
			throw new IllegalArgumentException("缺少流程实例id【instanceId】！");
		}
		if(CommonFunctions.isBlank(params, "curOrgId")) {
			throw new IllegalArgumentException("缺少办理人员组织id【curOrgId】！");
		}
		if(CommonFunctions.isBlank(params, "curUserId")) {
			throw new IllegalArgumentException("缺少办理人员用户id【curUserId】！");
		}
		
		try {
			instanceId = Long.valueOf(params.get("instanceId").toString());
		} catch(NumberFormatException e) {}
		
		curOrgId = params.get("curOrgId").toString();
		curUserId = params.get("curUserId").toString();
		userOrgCode = params.get("userOrgCode").toString();
		
		if(instanceId != null && instanceId > 0) {
			Node nextNode = null;
			List<Map<String, Object>> nodeActorMapList = null;
			ProInstance proInstance = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
			
			if(proInstance == null) {
				throw new IllegalArgumentException("流程实例id【" + instanceId + "】没有找到有效的流程实例信息！");
			}
			
			nextNode = eventDisposalWorkflowService.findNodeById(instanceId, COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE);
			
			if(nextNode != null) {
				nodeActorMapList = eventDisposalWorkflowService.findNodeActorsById(nextNode.getNodeId());
			}
			
			if(nodeActorMapList != null) {
				String positionName = null, trigCondition = null, emtType = null;
				Map<String, Object> positionParam = new HashMap<String, Object>();
				List<PositionInfoBO> positionList = null;
				
				for(Map<String, Object> nodeActorMap : nodeActorMapList) {
					if(CommonFunctions.isNotBlank(nodeActorMap, "POSITION_ID") 
							&& CommonFunctions.isNotBlank(nodeActorMap, "ACTOR_NAME")) {
						positionName = nodeActorMap.get("ACTOR_NAME").toString();
					}
					
					if(StringUtils.isNotBlank(positionName)) {
						positionParam.put("orgId", curOrgId);
						positionParam.put("userId", curUserId);
						positionParam.put("positionName", positionName);
						
						positionList = positionInfoService.queryPositionByPara(positionParam);
						
						if(positionList != null && positionList.size() > 0) {
							break;
						}
					}
					
					positionName = null;
				}
				
				//获取八员队伍人员类型
				trigCondition = "EMT-" + proInstance.getProName() + "-" + COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE + "-" + positionName;
				
				emtType = funConfigurationService.turnCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, trigCondition, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
				
				if(StringUtils.isNotBlank(emtType)) {
					Map<String, Object> eventExtend = new HashMap<String, Object>();
					Long eventId = proInstance.getFormId();
					
					flag = true;
					
					eventExtend.put("eventId", eventId);
					eventExtend.put("emtType", emtType);
					eventExtend.put("creatorId", curUserId);
					eventExtend.put("creatorOrgId", curOrgId);
					
					try {
						eventExtendService.saveEventExtend(eventExtend);
					} catch (Exception e) {
						if(logger.isErrorEnabled()) {
							logger.error("事件决策，八员队伍扩展信息增添失败：事件id【" + eventId + "】，八员队伍类型【" + emtType + "】！");
						}
						e.printStackTrace();
					}
				}
			}
		}
		
		return flag;
	}
}
