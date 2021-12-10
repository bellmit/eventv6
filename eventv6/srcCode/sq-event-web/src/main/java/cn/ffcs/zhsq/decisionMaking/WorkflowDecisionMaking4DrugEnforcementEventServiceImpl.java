package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 禁毒事件工作流跳转决策类
 * 必填参数
 * 		decisionService	指定实现类 workflowDecisionMaking4DrugEnforcementEventService
 * 		curOrgId		当前用户组织id
 * 		curUserId		当前用户id
 * 		curNodeCode		当前环节节点编码
 * 
 * @author zhangls
 *
 */
@Service(value = "workflowDecisionMaking4DrugEnforcementEventService")
public class WorkflowDecisionMaking4DrugEnforcementEventServiceImpl extends
		ApplicationObjectSupport implements IWorkflowDecisionMakingService<String> {

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	private static final String DECISION_MAKING_NODE_CODE = "decision1";	//决策1环节节点编码
	private static final String COLLECT_NODE_CODE = "task1";				//采集事件环节节点编码
	private static final String GRID_NODE_CODE = "task2";					//网格员办理环节
	private static final String COMMUNITY_NODE_CODE = "task3";				//社区(村)办理环节
	private static final String STREET_NARCOTICS_CONTROL_COMMISSION_NODE_CODE = "task4";	//乡镇(街道)禁毒委办理环节
	private static final String DISTRICT_NARCOTICS_CONTROL_COMMISSION_NODE_CODE = "task5";	//县级市禁毒委办理环节
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		Long curOrgId = -1L, curUserId = -1L;
		String curNodeCode = "", nextNodeCode = "",
			   orgChiefLevel = "", orgType = "",
			   ORG_TYPE_UNIT = String.valueOf(ConstantValue.ORG_TYPE_UNIT),			//组织类型 单位
			   ORG_TYPE_DEPARTMENT = String.valueOf(ConstantValue.ORG_TYPE_DEPT);	//组织类型 部门
		
		if(CommonFunctions.isNotBlank(params, "curOrgId")) {
			try {
				curOrgId = Long.valueOf(params.get("curOrgId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[curOrgId]："+params.get("curOrgId")+" 不能转换为Long型！");
			}
			
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(curOrgId);
			
			if(orgInfo != null) {
				orgChiefLevel = orgInfo.getChiefLevel();
				orgType = orgInfo.getOrgType();
			} else {
				throw new Exception("参数[curOrgId]："+params.get("curOrgId")+" 没有对应的组织信息！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curOrgId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curUserId")) {
			try {
				curUserId = Long.valueOf(params.get("curUserId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[curUserId]："+params.get("curUserId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curUserId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
		}
		
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) {
			boolean isGridAdmin = fiveKeyElementService.isUserIdGridAdmin(curOrgId, curUserId);
			
			if(isGridAdmin) {
				nextNodeCode = GRID_NODE_CODE;
			} else if(ORG_TYPE_UNIT.equals(orgType)) {
				if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)) {
					nextNodeCode = COMMUNITY_NODE_CODE;
				}
			} else if(ORG_TYPE_DEPARTMENT.equals(orgType)) {
				if(ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel)) {
					nextNodeCode = STREET_NARCOTICS_CONTROL_COMMISSION_NODE_CODE;
				} else if(ConstantValue.DISTRICT_ORG_LEVEL.equals(orgChiefLevel)) {
					nextNodeCode = DISTRICT_NARCOTICS_CONTROL_COMMISSION_NODE_CODE;
				}
			}
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		return nextNodeCode;
	}

}
