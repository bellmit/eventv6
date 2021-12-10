package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 泉州市南安市(NanAnShi)决策类
 * @ClassName:   WorkflowEventDecisionMaking4NASServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年5月19日 下午2:16:53
 * 必填参数
 * 	decisionService	指定实现类 workflowEventDecisionMakingService
 * 	curUserId		当前用户
 * 	curOrgId		当前用户所属组织
 * 	curNodeCode		当前环节节点编码
 */
@Service(value = "workflowEventDecisionMaking4NASService")
public class WorkflowEventDecisionMaking4NASServiceImpl extends WorkflowEventDecisionMakingServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoService;
	
	private static final String DISTRICT_DEPARTMENT_BRIGADE_NODE_CODE = "task13";	//区职能部门执法大队处理
	private static final String DISTRICT_DEPARTMENT_SQUADRON_NODE_CODE = "task14";	//区职能部门执法中队处理
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		Long curOrgId = -1L;		//当前用户所属组织
		String chiefLevel = "";		//当前组织层级
		String regionCheiefLevel = "";	//当前地域层级
		String curNodeCode = "";	//当前环节节点编码
		String nextNodeCode = "";	//下一环节节点编码
		OrgSocialInfoBO orgInfo = null;	//当前组织信息
		boolean isOrgTypeUnit = false;	//是否是单位组织类型
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
		}
		
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) {//决策1
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
			
			if(orgInfo != null) {
				Long locationId = orgInfo.getLocationOrgId();
				OrgEntityInfoBO orgEntityInfo = orgEntityInfoService.findByOrgId(locationId);
				
				chiefLevel = orgInfo.getChiefLevel();
				
				if(orgEntityInfo != null && orgEntityInfo.getOrgId() != null) {
					regionCheiefLevel = orgEntityInfo.getChiefLevel();
				}
				
				isOrgTypeUnit = String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgInfo.getOrgType());
			}
			
			if(!isOrgTypeUnit && ConstantValue.DISTRICT_ORG_LEVEL.equals(regionCheiefLevel)) {//旧组织职能部门
				if(ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevel)) {
					nextNodeCode = DISTRICT_DEPARTMENT_NODE_CODE;
				} else if(ConstantValue.STREET_ORG_LEVEL.equals(chiefLevel)) {
					nextNodeCode = DISTRICT_DEPARTMENT_BRIGADE_NODE_CODE;
				} else if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(chiefLevel)) {
					nextNodeCode = DISTRICT_DEPARTMENT_SQUADRON_NODE_CODE;
				}
			}
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			nextNodeCode = super.makeDecision(params);
		}
		
		return nextNodeCode;
	}
}
