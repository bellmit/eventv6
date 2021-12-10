package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 重庆铜梁工作流节点跳转决策类
 * 必填参数
 * decisionService	指定实现类 workflowEventDecisionMaking4CQTLService
 * curUserId		当前用户
 * curOrgId			当前用户所属组织
 * curNodeCode		当前环节节点编码
 * @author youwj
 *
 */
@Service(value = "workflowEventDecisionMaking4CQTLService")
public class WorkflowEventDecisionMaking4CQTLServiceImpl extends
		WorkflowEventDecisionMakingServiceImpl {
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoService;
	
	private static final String GRID_ADMIN_HANDLE_NODE_CODE = "task2";	//网格员处理
	
	private static final String COMMUNITY_UNIT_HANDLE_NODE_CODE = "task3";	//村社区处理
	
	private static final String TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE = "task4";	//乡镇街道处理
	
	private static final String DISTRICT_UNIT_HANDLE_NODE_CODE = "task5";	//区指挥中心处理
	
	private static final String DISTRICT_DEPARTMENT_HANDLE_NODE_CODE = "task6";	//区职能部门处理
	
	
	
	
	//组织类型
	protected final String ORG_TYPE_DEPARTMENT = "0";	// 组织类型 部门
	protected final String ORG_TYPE_UNIT = "1";	// 组织类型 单位

	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		
		Long curOrgId = -1L;		//当前用户所属组织
		String curNodeCode = "";	//当前环节节点编码
		String chiefLevl = "";		//当前组织层级
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
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
		}
		
		if(orgInfo != null) {
			Long locationId = orgInfo.getLocationOrgId();
			OrgEntityInfoBO orgEntityInfo = orgEntityInfoService.findByOrgId(locationId);
			
			if(orgEntityInfo != null && orgEntityInfo.getOrgId() != null) {//优先使用组织域中的所属地域的层级
				chiefLevl = orgEntityInfo.getChiefLevel();
			} else {
				chiefLevl = orgInfo.getChiefLevel();
			}
		}
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) {
			if(ConstantValue.GRID_ORG_LEVEL.equals(chiefLevl)&&ORG_TYPE_UNIT.equals(orgInfo.getOrgType())) {
				return GRID_ADMIN_HANDLE_NODE_CODE;
			}else if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(chiefLevl)&&ORG_TYPE_UNIT.equals(orgInfo.getOrgType())) {
				return COMMUNITY_UNIT_HANDLE_NODE_CODE;
			}else if(ConstantValue.STREET_ORG_LEVEL.equals(chiefLevl)&&ORG_TYPE_UNIT.equals(orgInfo.getOrgType())) {
				return TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE;
			}else if(ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevl)&&ORG_TYPE_UNIT.equals(orgInfo.getOrgType())) {
				return DISTRICT_UNIT_HANDLE_NODE_CODE;
			}else if(ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevl)&&ORG_TYPE_DEPARTMENT.equals(orgInfo.getOrgType())) {
				return DISTRICT_DEPARTMENT_HANDLE_NODE_CODE;
			}
		}
		return super.makeDecision(params);
	}
}
