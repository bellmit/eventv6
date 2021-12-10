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
 * @Description: 智慧城管(Smart City Urban Manage)延平区(Yan Ping Qu) 工作流节点跳转决策类
 * @ClassName:   WorkflowSCUMYPQDecisionMakingServiceImpl   
 * 必填参数
 * 		decisionService	指定实现类 workflowSCUMYPQDecisionMakingService
 * 		curUserId		当前用户
 * 		createUserId	当前事件的采集人员
 * 		curOrgId		当前用户所属组织
 * 		curNodeCode		当前环节节点编码
 * 		instanceId		流程实例id
 * @author:      张联松(zhangls)
 * @date:        2020年6月8日 下午3:10:53
 */
@Service(value = "workflowSCUMYPQDecisionMakingService")
public class WorkflowSCUMYPQDecisionMakingServiceImpl extends ApplicationObjectSupport
		implements IWorkflowDecisionMakingService<String> {

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	private static final String COLLECT_NODE_CODE = "task1";				//采集事件环节节点编码
	private static final String DECISION_MAKING_NODE_CODE = "decision1";	//决策1环节节点编码
	private static final String STREET_DEPARTMENT_NODE_CODE = "task2";		//街道专业部门采集
	private static final String DISTRICT_DEPARTMENT_NODE_CODE = "task3";	//区专业部门采集
	private static final String DISTRICT_NODE_CODE = "task4";				//指挥中心采集
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		Long curOrgId = null;
		String curNodeCode = null, nextNodeCode = null, 
			   curOrgChiefLevel = null, curOrgType = null;
		
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
			
			if(curOrgId > 0) {
				OrgSocialInfoBO curOrgInfo = orgSocialInfoService.findByOrgId(curOrgId);
				if(curOrgInfo != null) {
					curOrgChiefLevel = curOrgInfo.getChiefLevel();
					curOrgType = curOrgInfo.getOrgType();
				}
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curOrgId]，请检查！");
		}
		
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) {//决策1
			if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(curOrgType)) {
				if(ConstantValue.DISTRICT_ORG_LEVEL.equals(curOrgChiefLevel)) {
					nextNodeCode = DISTRICT_NODE_CODE;
				}
			} else if(String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(curOrgType)) {
				if(ConstantValue.STREET_ORG_LEVEL.equals(curOrgChiefLevel)) {
					nextNodeCode = STREET_DEPARTMENT_NODE_CODE;
				} else if(ConstantValue.DISTRICT_ORG_LEVEL.equals(curOrgChiefLevel)) {
					nextNodeCode = DISTRICT_DEPARTMENT_NODE_CODE;
				}
			}
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		return nextNodeCode;
	}

}
