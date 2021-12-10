package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.accountabilityEnforcement.service.IAccountabilityEnforcementService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 新疆纪委扶贫问题决策类
 * 必填参数
 * 		decisionService	指定实现类 workflowDecisionMaking4AccountEnforceService
 * 		curOrgId		当前用户组织id
 * 		curNodeCode		当前环节节点编码
 * 
 * 非必填参数
 * 		instanceId		流程实例id
 * 		curUserId		当前用户id
 * 
 * @author zhangls
 *
 */
@Service(value = "workflowDecisionMaking4AccountEnforceService")
public class WorkflowDecisionMaking4AccountEnforceServiceImpl extends
		ApplicationObjectSupport implements
		IWorkflowDecisionMakingService<String> {
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IAccountabilityEnforcementService accountEnforceService;
	
	private static final String DECISION_MAKING_NODE_CODE = "decision1";	//决策1环节节点编码
	private static final String COLLECT_NODE_CODE = "task1";				//采集事件环节节点编码
	private static final String PROVINCE_NODE_CODE = "task2";				//自治区纪委处理环节
	private static final String MUNICIPAL_NODE_CODE = "task3";				//地市州纪委处理环节
	private static final String DISTRICT_NODE_CODE = "task4";				//区县纪委处理环节
	private static final String STREET_NODE_CODE = "task5";					//街道纪委处理环节
	
	private static final String DECISION_MAKING_NODE_CODE_2 = "decision2";	//决策2环节节点编码
	
	private static final String DECISION_MAKING_NODE_CODE_3 = "decision3";	//决策3环节节点编码
	private static final String LEADER_AUDIT_NODE_CODE = "task11";			//领导审核环节节点编码
	
	private static final String END_NODE_CODE = "end1";						//归档环节节点编码
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		Long curUserId = -1L, curOrgId = -1L, instanceId = -1L;
		String curNodeCode = "", nextNodeCode = "",
			   orgChiefLevel = "", orgType = "",
			   ORG_TYPE_UNIT = String.valueOf(ConstantValue.ORG_TYPE_UNIT);			//组织类型 单位
		StringBuffer msgWrong = new StringBuffer("");
		
		if(CommonFunctions.isNotBlank(params, "curOrgId")) {
			try {
				curOrgId = Long.valueOf(params.get("curOrgId").toString());
			} catch(NumberFormatException e) {
				msgWrong.append("参数[curOrgId]：").append(params.get("curOrgId")).append(" 不能转换为Long型！");
			}
			
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(curOrgId);
			
			if(orgInfo != null) {
				orgChiefLevel = orgInfo.getChiefLevel();
				orgType = orgInfo.getOrgType();
			} else {
				msgWrong.append("参数[curOrgId]：").append(params.get("curOrgId")).append(" 没有对应的组织信息！");
			}
		} else {
			msgWrong.append("缺失参数[curOrgId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			msgWrong.append("缺失参数[curNodeCode]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {
				msgWrong.append("参数[instanceId]：").append(params.get("instanceId")).append(" 不能转换为Long型！");
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "curUserId")) {
			try {
				curUserId = Long.valueOf(params.get("curUserId").toString());
			} catch(NumberFormatException e) {
				msgWrong.append("参数[curUserId]：").append(params.get("curUserId")).append(" 不能转换为Long型！");
			}
		}
		
		if(msgWrong.length() > 0) {
			throw new Exception(msgWrong.toString());
		}
		
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)
		|| DECISION_MAKING_NODE_CODE_3.equals(curNodeCode) || LEADER_AUDIT_NODE_CODE.equals(curNodeCode)) {
			if(ORG_TYPE_UNIT.equals(orgType)) {
				if(ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel)) {
					nextNodeCode = STREET_NODE_CODE;
				} else if(ConstantValue.DISTRICT_ORG_LEVEL.equals(orgChiefLevel)) {
					nextNodeCode = DISTRICT_NODE_CODE;
				} else if(ConstantValue.MUNICIPAL_ORG_LEVEL.equals(orgChiefLevel)) {
					nextNodeCode = MUNICIPAL_NODE_CODE;
				} else if(ConstantValue.PROVINCE_ORG_LEVEL.equals(orgChiefLevel)) {
					nextNodeCode = PROVINCE_NODE_CODE;
				}
			}
		} else if(DECISION_MAKING_NODE_CODE_2.equals(curNodeCode)) {
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			
			nextNodeCode = LEADER_AUDIT_NODE_CODE;
			
			if(proInstance != null) {
				Long probId = proInstance.getFormId(), 
					 creatorId = proInstance.getUserId(),
					 creatorOrgId = proInstance.getOrgId();
				
				if(creatorId.equals(curUserId) && creatorOrgId.equals(curOrgId)) {
					nextNodeCode = END_NODE_CODE;
				} else {
					String procType = null,			//处置类型
						   HANDLE_DIRECT = "3";		//直办
					Map<String, Object> probMap = accountEnforceService.findByProbId(probId, params);
					
					if(CommonFunctions.isNotBlank(probMap, "procType")) {
						procType = probMap.get("procType").toString();
					}
					
					if(HANDLE_DIRECT.equals(procType)) {
						nextNodeCode = END_NODE_CODE;
					}
				}
			}
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		return nextNodeCode;
	}

}
