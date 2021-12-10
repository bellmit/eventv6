package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 江阴市 工作流节点跳转决策类
 * 必填参数
 * decisionService	指定实现类 workflowEventDecisionMaking4JYSService
 * curUserId		当前用户
 * curOrgId			当前用户所属组织
 * curNodeCode		当前环节节点编码
 * @author zhangls
 *
 */
@Service(value = "workflowEventDecisionMaking4JYSService")
public class WorkflowEventDecisionMaking4JYSServiceImpl extends
		WorkflowEventDecisionMakingServiceImpl {
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		String curNodeCode = "",	//当前环节节点编码
			   nextNodeCode = "";	//下一环节节点编码
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
		}
		
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) {//决策1
			Long curOrgId = -1L;		//当前用户所属组织
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
			
			if(ConstantValue.STREET_ORG_LEVEL.equals(orgInfo.getChiefLevel()) && ORG_TYPE_DEPARTMENT.equals(orgInfo.getOrgType())) {//部门
				nextNodeCode = funConfigurationService.turnCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.DECISION_RESULT_TASK_NAME + "_" + orgInfo.getProfessionCode(), IFunConfigurationService.CFG_TYPE_FACT_VAL, orgInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			}
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			nextNodeCode = super.makeDecision(params);
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		return nextNodeCode;
	}
}
