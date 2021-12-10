package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 甘肃省兰州市城关区(CGQ)新版通用事件工作流节点跳转决策类 
 * 必填参数
 * decisionService	指定实现类 workflowEventDecisionMaking4CGQService
 * curUserId		当前用户
 * curOrgId			当前用户所属组织
 * curNodeCode		当前环节节点编码
 * instanceId		流程实例id
 * @author zhangls
 *
 */
@Service(value = "workflowEventDecisionMaking4CGQService")
public class WorkflowEventDecisionMaking4CGQServiceImpl extends
		WorkflowEventDecisionMakingServiceImpl {
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	private static final String DECISION_MAKING_NODE_CODE2 = "decision2";	//决策1环节节点编码
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		String curNodeCode = "",	//当前环节节点编码
			   nextNodeCode = "";	//下一环节节点编码
	
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
		}
		
		//决策2
		if(DECISION_MAKING_NODE_CODE2.equals(curNodeCode) || UNITED_PREVENTION_GROUP_NODE_CODE.equals(curNodeCode)
		   || GRID_ADMIN_NODE_CODE.equals(curNodeCode)
		   || COMMUNITY_NODE_CODE.equals(curNodeCode) || STREET_NODE_CODE.equals(curNodeCode)
		   || DISTRICT_NODE_CODE.equals(curNodeCode)) {
			Long curOrgId = -1L,		//当前用户组织id
				 instanceId = -1L;		//流程实例id
			OrgSocialInfoBO orgInfo = null;	//当前组织信息
			
			if(CommonFunctions.isNotBlank(params, "curOrgId")) {
				try {
					curOrgId = Long.valueOf(params.get("curOrgId").toString());
				} catch(NumberFormatException e) {
					throw new NumberFormatException("参数[curOrgId]："+params.get("curOrgId")+" 不能转换为Long型！");
				}
				
				orgInfo = orgSocialInfoService.findByOrgId(curOrgId);
				if(orgInfo == null) {
					throw new Exception("参数[curOrgId]："+params.get("curOrgId")+" 没有对应的组织信息！");
				}
			} else {
				throw new IllegalArgumentException("缺失参数[curOrgId]，请检查！");
			}
			
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				try {
					instanceId = Long.valueOf(params.get("instanceId").toString());
				} catch(NumberFormatException e) {
					throw new NumberFormatException("参数[instanceId]："+params.get("instanceId")+" 不能转换为Long型！");
				}
			} else {
				throw new IllegalArgumentException("缺失参数[instanceId]，请检查！");
			}
			
			if(instanceId != null && instanceId > 0) {
				ProInstance proInstance = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
				if(proInstance != null) {
					Long eventId = proInstance.getFormId();
					
					if(eventId != null && eventId > 0) {
						EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
						
						if(event != null) {
							nextNodeCode = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.DECISION_RESULT_TASK_NAME+"_"+proInstance.getProName()+"_event_"+event.getType(), IFunConfigurationService.CFG_TYPE_FACT_VAL, orgInfo.getOrgCode(), IFunConfigurationService.DIRECTION_UP_FUZZY);
						}
					}
				}
			}
		} else {
			nextNodeCode = super.makeDecision(params);
		}
		
		return nextNodeCode;
	}
}
