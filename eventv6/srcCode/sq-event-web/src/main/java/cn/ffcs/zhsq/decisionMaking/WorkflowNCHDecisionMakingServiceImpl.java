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

/**
 * 南昌(NCH) 工作流节点跳转决策类
 * 必填参数
 * decisionService	指定实现类 workflowNCHDecisionMakingService
 * curUserId		当前用户
 * createUserId		当前事件的采集人员
 * curOrgId			当前用户所属组织
 * curNodeCode		当前环节节点编码
 * @author zhangls
 *
 */
@Service(value = "workflowNCHDecisionMakingService")
public class WorkflowNCHDecisionMakingServiceImpl extends
		ApplicationObjectSupport implements IWorkflowDecisionMakingService<String> {

	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	//决策1
	private static final String DECISION_MAKING_NODE_CODE = "decision1";	//决策1环节节点编码
	private static final String COLLECT_NODE_CODE = "task1";				//采集事件环节节点编码
	private static final String GRID_ADMIN_NODE_CODE = "task2";				//网格员处理环节
	private static final String COMMUNITY_NODE_CODE = "task3";				//村(社区)处理环节
	private static final String STREET_NODE_CODE = "task4";					//乡镇(街道)处理环节
	private static final String DISTRICT_NODE_CODE = "task5";				//县(区)处理环节
	
	//决策2
	private static final String DECISION_MAKING_NODE_CODE_2 = "decision2";				//决策2环节节点编码
	private static final String CONFIRM_NODE_CODE = "task9";				//确认环节
	private static final String EVALUATE_NODE_CODE = "task10";				//评价环节
	
	//组织层级
	private static final String COMMUNITY_ORG_LEVEL = "5";	//社区组织层级
	private static final String STREET_ORG_LEVEL = "4";		//街道组织层级
	private static final String DISTRICT_ORG_LEVEL = "3";	//区组织层级
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		Long curUserId = -1L;		//当前用户
		Long createUserId = -1L;	//当前事件的采集人员
		Long curOrgId = -1L;		//当前用户所属组织
		String curNodeCode = "";	//当前环节节点编码
		String nextNodeCode = "";	//下一环节节点编码
		String chiefLevl = "";		//当前组织层级
		
		if(CommonFunctions.isNotBlank(params, "curUserId")) {
			try {
				curUserId = Long.valueOf(params.get("curUserId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[curUserId]："+params.get("curUserId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curUserId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "createUserId")) {
			try {
				createUserId = Long.valueOf(params.get("createUserId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[createUserId]："+params.get("createUserId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[createUserId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curOrgId")) {
			try {
				curOrgId = Long.valueOf(params.get("curOrgId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[curOrgId]："+params.get("curOrgId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curOrgId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
		}
		
		OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(curOrgId);
		if(orgInfo != null) {
			chiefLevl = orgInfo.getChiefLevel();
		}
		
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) {//决策1
			boolean isGridAdmin = fiveKeyElementService.isUserIdGridAdmin(curOrgId, curUserId);
			
			if(isGridAdmin) {
				nextNodeCode = GRID_ADMIN_NODE_CODE;
			} else {
				if(COMMUNITY_ORG_LEVEL.equals(chiefLevl)) {
					nextNodeCode = COMMUNITY_NODE_CODE;
				} else if(STREET_ORG_LEVEL.equals(chiefLevl)) {
					nextNodeCode = STREET_NODE_CODE;
				} else if(DISTRICT_ORG_LEVEL.equals(chiefLevl)) {
					nextNodeCode = DISTRICT_NODE_CODE;
				}
			}
		} else if(DECISION_MAKING_NODE_CODE_2.equals(curNodeCode)) {//决策2
			if(curUserId.equals(createUserId)) {
				nextNodeCode = EVALUATE_NODE_CODE;
			} else {
				nextNodeCode = CONFIRM_NODE_CODE;
			}
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		return nextNodeCode;
	}
}
