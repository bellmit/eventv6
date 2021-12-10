package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 民生信息 必填参数 decisionService 指定实现类
 * workflowDecisionMaking4SBREGansuService curOrgId 当前用户组织id curUserId 当前用户id
 * curNodeCode 当前环节节点编码
 * 
 * @author youwj
 *
 */
@Service(value = "workflowDecisionMaking4PeopleLivelihoodService")
public class WorkflowDecisionMaking4PeopleLivelihoodServiceImpl extends ApplicationObjectSupport
		implements IWorkflowDecisionMakingService<String> {

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;

	private static final String DECISION_MAKING_NODE_CODE = "decision1"; // 决策1环节节点编码
	
	private static final String START_NODE_CODE = "start";		//流程启动
	private static final String COLLECT_NODE_CODE = "task1";	//采集处理环节
	
	private static final String COMMUNITY_UNIT_HANDLE_NODE_CODE = "task2";	//村社区处理
	private static final String TOWNDISPOSAl_UNIT_HANDLE_NODE_CODE = "task3";	//乡镇街道处理
	private static final String TOWNDISPOSAl_DEPT_HANDLE_NODE_CODE = "task4";	//街道职能部门办理
	private static final String DISTRICT_UNIT_HANDLE_NODE_CODE = "task5";	//县区办理
	private static final String DISTRICT_DEPT_HANDLE_NODE_CODE = "task6";	//县区职能部门办理
	private static final String CITY_UNIT_HANDLE_NODE_CODE = "task7";	//市级办理
	private static final String CITY_DEPT_HANDLE_NODE_CODE = "task8";	//市职能部门办理

    private static final String END_NODE_CODE = "end1";			//归档环节

	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		Long curOrgId = -1L;
		String curNodeCode = "", nextNodeCode = "", orgChiefLevel = "", orgType = "",
				ORG_TYPE_UNIT = String.valueOf(ConstantValue.ORG_TYPE_UNIT), // 组织类型
																				// 单位
				ORG_TYPE_DEPARTMENT = String.valueOf(ConstantValue.ORG_TYPE_DEPT); // 组织类型
																					// 部门

		if (CommonFunctions.isNotBlank(params, "curOrgId")) {
			try {
				curOrgId = Long.valueOf(params.get("curOrgId").toString());
			} catch (NumberFormatException e) {
				throw new NumberFormatException("参数[curOrgId]：" + params.get("curOrgId") + " 不能转换为Long型！");
			}

			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(curOrgId);

			if (orgInfo != null) {
				orgChiefLevel = orgInfo.getChiefLevel();
				orgType = orgInfo.getOrgType();
			} else {
				throw new Exception("参数[curOrgId]：" + params.get("curOrgId") + " 没有对应的组织信息！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curOrgId]，请检查！");
		}

		if (CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
		}

		if (DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) {
			if (ORG_TYPE_UNIT.equals(orgType)) {//组织类型为单位
				switch (orgChiefLevel) {
				case ConstantValue.COMMUNITY_ORG_LEVEL://村社区级别
					nextNodeCode = COMMUNITY_UNIT_HANDLE_NODE_CODE;
					break;
				default:
					break;
				}
			} else if (ORG_TYPE_DEPARTMENT.equals(orgType)) {//组织类型为部门
				switch (orgChiefLevel) {
				case ConstantValue.MUNICIPAL_ORG_LEVEL://市级
					nextNodeCode = CITY_DEPT_HANDLE_NODE_CODE;
					break;
				case ConstantValue.DISTRICT_ORG_LEVEL://县区级
					nextNodeCode = DISTRICT_DEPT_HANDLE_NODE_CODE;
					break;
				case ConstantValue.STREET_ORG_LEVEL://乡镇街道级
					nextNodeCode = TOWNDISPOSAl_DEPT_HANDLE_NODE_CODE;
					break;
				default:
					break;
				}
			}
		}

		if (StringUtils.isBlank(nextNodeCode)) {
			throw new Exception("没有可用的下一环节！");
		}

		return nextNodeCode;
	}

}
