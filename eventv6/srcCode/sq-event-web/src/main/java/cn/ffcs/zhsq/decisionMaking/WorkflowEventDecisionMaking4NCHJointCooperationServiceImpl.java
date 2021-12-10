package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 南昌大联动(NCHJointCooperation)流程决策类
 * 必填参数
 * 	decisionService	指定实现类 workflowEventDecisionMaking4NCHJointCooperationService
 * 	curUserId		当前用户
 * 	curOrgId			当前用户所属组织
 * 	curNodeCode		当前环节节点编码
 * 
 * 非必填参数
 * 	instanceId		流程实例id
 * @author zhangls
 *
 */
@Service(value = "workflowEventDecisionMaking4NCHJointCooperationService")
public class WorkflowEventDecisionMaking4NCHJointCooperationServiceImpl extends
		WorkflowEventDecisionMakingServiceImpl {
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	//决策2节点名称
	private static final String DECISION_MAKING_NODE_CODE_2 = "decision2";
	//市民热线节点名称
	private static final String CITIZEN_HOTLINE_NODE_CODE = "task16";
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		Long eventId = null;
		String curNodeCode = null, nextNodeCode = null,
			   LIVELIHOOD_TYPE = "05";//民生服务类别
		boolean isLiveliHoodType = false;
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			Long instanceId = null;
			
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
			if(instanceId != null && instanceId > 0) {
				ProInstance pro = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
				if(pro != null) {
					eventId = pro.getFormId();
				}
			}
		}
		
		if(eventId > 0) {
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			if(event != null) {
				String eventType = event.getType();
				
				isLiveliHoodType = StringUtils.isNotBlank(eventType) && eventType.startsWith(LIVELIHOOD_TYPE);
			}
		}
		
		if(isLiveliHoodType && (DECISION_MAKING_NODE_CODE_2.equals(curNodeCode) || 
				COMMUNITY_NODE_CODE.equals(curNodeCode) || 
				GRID_ADMIN_NODE_CODE.equals(curNodeCode))) {
			nextNodeCode = CITIZEN_HOTLINE_NODE_CODE;
		} else {
			nextNodeCode = super.makeDecision(params);
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		return nextNodeCode;
	}
}
