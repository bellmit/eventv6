package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 雄安新区容东县(RongDongXian)事件流程决策类
 * @ClassName:   WorkflowEventDecisionMaking4RDXServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年5月15日 下午2:12:18
 */
@Service(value = "workflowEventDecisionMaking4RDXService")
public class WorkflowEventDecisionMaking4RDXServiceImpl extends WorkflowEventDecisionMakingServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		Long curOrgId = -1L;		//当前用户所属组织
		String curNodeCode = "";	//当前环节节点编码
		String nextNodeCode = "";	//下一环节节点编码
		String chiefLevl = "";		//当前组织层级
		OrgSocialInfoBO orgInfo = null;	//当前组织信息
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curOrgId")) {
			try {
				curOrgId = Long.valueOf(params.get("curOrgId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[curOrgId]："+params.get("curOrgId")+" 不能转换为Long型！");
			}
			
			orgInfo = orgSocialInfoService.findByOrgId(curOrgId);
			if(orgInfo == null || orgInfo.getOrgId() == null) {
				throw new Exception("参数[curOrgId]："+params.get("curOrgId")+" 没有对应的组织信息！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curOrgId]，请检查！");
		}
		
		if(orgInfo != null) {
			chiefLevl = orgInfo.getChiefLevel();
		}
		
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) {//决策1
			if(ConstantValue.UNITED_PREVENTION_GROUP_ORG_LEVEL.equals(chiefLevl)) {
				nextNodeCode = UNITED_PREVENTION_GROUP_NODE_CODE;
			} else if(ConstantValue.GRID_ORG_LEVEL.equals(chiefLevl)) {
				nextNodeCode = GRID_ADMIN_NODE_CODE;
			} else if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(chiefLevl)) {
				nextNodeCode = COMMUNITY_NODE_CODE;
			} else if(ConstantValue.STREET_ORG_LEVEL.equals(chiefLevl)) {
				nextNodeCode = STREET_NODE_CODE;
			} else if(ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevl)) {
				nextNodeCode = DISTRICT_NODE_CODE;
			} else if(ConstantValue.MUNICIPAL_ORG_LEVEL.equals(chiefLevl)) {
				nextNodeCode = MUNICIPAL_NODE_CODE;
			}
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		return nextNodeCode;
	}

}
