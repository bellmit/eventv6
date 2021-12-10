package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 事件通用工作流节点跳转决策类
 * 必填参数
 * decisionService	指定实现类 workflowEventDecisionMakingService
 * curUserId		当前用户
 * curOrgId			当前用户所属组织
 * curNodeCode		当前环节节点编码
 * @author zhangls
 *
 */
@Service(value = "workflowEventDecisionMakingService")
public class WorkflowEventDecisionMakingServiceImpl extends
		ApplicationObjectSupport implements IWorkflowDecisionMakingService<String> {
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService;
	
	//决策1
	protected static final String DECISION_MAKING_NODE_CODE = "decision1";	//决策1环节节点编码
	protected static final String COLLECT_NODE_CODE = "task1";				//采集事件环节节点编码
	protected static final String UNITED_PREVENTION_GROUP_NODE_CODE = "task12";//联防组网格
	protected static final String GRID_ADMIN_NODE_CODE = "task2";				//市政管理员[网格员]处理环节
	protected static final String COMMUNITY_NODE_CODE = "task3";				//片长(队长)[村(社区)]处理环节
	protected static final String STREET_NODE_CODE = "task4";					//部门经理[乡镇(街道)]处理环节
	protected static final String DISTRICT_NODE_CODE = "task5";				//分管领导[县(区)]处理环节
	private static final String STREET_DEPARTMENT_NODE_CODE = "task6";		//街道职能部门处理
	protected static final String DISTRICT_DEPARTMENT_NODE_CODE = "task7";	//区职能部门处理
	protected static final String MUNICIPAL_NODE_CODE = "task10";				//市级处理环节
	private static final String MUNICIPAL_DEPARTMENT_NODE_CODE = "task11";	//市职能部门处理
	
	//组织类型
	private final String ORG_TYPE_UNIT = "1";			//组织类型 单位
	protected final String ORG_TYPE_DEPARTMENT = "0";	// 组织类型 部门
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		Long curUserId = -1L;		//当前用户
		Long curOrgId = -1L;		//当前用户所属组织
		String curNodeCode = "";	//当前环节节点编码
		String nextNodeCode = "";	//下一环节节点编码
		String chiefLevl = "";		//当前组织层级
		OrgSocialInfoBO orgInfo = null;	//当前组织信息
		
		boolean isOrgTypeUnit = false;	//是否是单位组织类型
		boolean isNewOrg = false;	//是否启用新组织
		boolean isGovernment = false;	//是否是政府管理专业
		
		if(CommonFunctions.isNotBlank(params, "curUserId")) {
			try {
				curUserId = Long.valueOf(params.get("curUserId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[curUserId]："+params.get("curUserId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curUserId]，请检查！");
		}
		
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
			
			isOrgTypeUnit = ORG_TYPE_UNIT.equals(orgInfo.getOrgType());
			isNewOrg = eventOrgInfoService.isNewOrganization(orgInfo);
			isGovernment = eventOrgInfoService.isGovernment(orgInfo.getProfessionCode());
		}
		
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) {//决策1
			boolean isGridAdmin = fiveKeyElementService.isUserIdGridAdmin(curOrgId, curUserId);
			
			if(isGridAdmin) {
				nextNodeCode = GRID_ADMIN_NODE_CODE;
			} else {
				if((isNewOrg && !isGovernment) || (!isNewOrg && !isOrgTypeUnit)) {//新组织职能部门 旧组织职能部门
					if(ConstantValue.STREET_ORG_LEVEL.equals(chiefLevl)) {
						nextNodeCode = STREET_DEPARTMENT_NODE_CODE;
					} else if(ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevl)) {
						nextNodeCode = DISTRICT_DEPARTMENT_NODE_CODE;
					} else if(ConstantValue.MUNICIPAL_ORG_LEVEL.equals(chiefLevl)) {
						nextNodeCode = MUNICIPAL_DEPARTMENT_NODE_CODE;
					}
				} else {
					if(ConstantValue.UNITED_PREVENTION_GROUP_ORG_LEVEL.equals(chiefLevl)) {
						nextNodeCode = UNITED_PREVENTION_GROUP_NODE_CODE;
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
			}
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		return nextNodeCode;
	}

}
