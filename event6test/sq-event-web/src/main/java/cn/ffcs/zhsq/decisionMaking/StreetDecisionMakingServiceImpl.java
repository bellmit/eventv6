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
 * 思明区 街道级流程图决策类
 * 必填参数
 * decisionService	指定实现类 streetDecisionMakingService
 * curNodeCode		当前环节节点编码
 * curOrgId			当前环节办理人员所属组织
 * createOrgId		当前事件的采集人员所属组织
 * @author zhangls
 *
 */
@Service(value = "streetDecisionMakingService")
public class StreetDecisionMakingServiceImpl extends ApplicationObjectSupport
		implements IWorkflowDecisionMakingService<String> {

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	
	private static final String PARTY_PROFESSION_CODE = "dj";	//党政办专业
	
	//决策1
	private static final String DECISION_MAKING_NODE_CODE = "decision1";			//决策1环节节点编码
	private static final String START_STREET_DEPARTMENT_HANDLE_NODE_CODE = "task2";	//街道部门办理环节节点编码
	
	//决策2
	private static final String STREET_PARTY_NODE_CODE = "task1";			//街道党政办办理环节节点编码
	private static final String STREET_DEPARTMENT_EVA_NODE_CODE = "task3";	//街道部门评价
	private static final String STREET_END_NODE_CODE = "end1";				//流程结束
	
	//决策3
	private static final String STREET_DEPARTMENT_HANDLE_NODE_CODE = "task7";	//街道部门办理环节节点编码
	private static final String STREET_D_STREET_CONFIRM_NODE_CODE = "task6";	//街道党政办确认
	private static final String STREET_D_END_NODE_CODE = "end2";				//流程结束
	
	//决策4
	private static final String DISTRICT_CONFIRM_NODE_CODE = "task12";					//区指挥中心确认环节节点编码
	private static final String DISTRICT_C_STREET_DEPARTMENT_EVA_NODE_CODE = "task13";	//街道部门评价
	private static final String DISTRICT_C_STREET_PARTY_EVA_NODE_CODE = "task14";		//街道党政办评价
	
	//决策5
	private static final String DISTRICT_HANDLE_NODE_CODE = "task10";					//区指挥中心办理环节节点编码
	private static final String DISTRICT_H_STREET_DEPARTMENT_EVA_NODE_CODE = "task16";	//街道部门评价
	private static final String DISTRICT_H_STREET_PARTY_EVA_NODE_CODE = "task17";		//街道党政办评价
	
	//决策6
	private static final String STREET_CONFIRM_NODE_CODE = "task6";					//街道党政办确认环节节点编码
	private static final String STREET_C_STREET_PARTY_EVA_NODE_CODE = "task9";		//街道党政办评价
	private static final String STREET_C_STREET_DEPARTMENT_EVA_NODE_CODE = "task8";	//街道部门评价
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		String curNodeCode = "";	//当前环节节点编码
		String nextNodeCode = "";	//下一环节节点编码
		String curOrgId	= "";		//当前办理人员所属组织
		String createOrgId = "";	//事件采集人员所属组织
		String createProfessionCode = "";	//事件采集人员组织专业编码
		boolean isFromParty = false;		//采集人员是否为街道党政办
		boolean isFromDepartment = false;	//采集人员是否为街道部门
		boolean isTheSame = false;			//采集人员和办理人员是否为相同部门
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curOrgId")) {
			curOrgId = params.get("curOrgId").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curOrgId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "createOrgId")) {
			createOrgId = params.get("createOrgId").toString();
			Long createOrgIdL = -1L;
			
			try {
				createOrgIdL = Long.valueOf(createOrgId);
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[createOrgId]："+createOrgId+" 不能转换为Long型！");
			}
			
			OrgSocialInfoBO orgInfo = orgSocialInfoOutService.findByOrgId(createOrgIdL);
			if(orgInfo != null) {
				createProfessionCode = orgInfo.getProfessionCode();
			} else {
				throw new Exception("参数[createOrgIdL]："+createOrgIdL+" 没有对应的组织信息！");
			}
			
		} else {
			throw new IllegalArgumentException("缺失参数[createOrgId]，请检查！");
		}
		
		isFromParty = PARTY_PROFESSION_CODE.equals(createProfessionCode);
		isFromDepartment = !isFromParty && !ConstantValue.GOV_PROFESSION_CODE.equals(createProfessionCode);
		isTheSame = curOrgId.equals(createOrgId);
		
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode)) {//决策1
			if(isFromDepartment) {
				nextNodeCode = START_STREET_DEPARTMENT_HANDLE_NODE_CODE;
			} else if(isFromParty) {
				nextNodeCode = STREET_PARTY_NODE_CODE;
			}
		} else if(STREET_PARTY_NODE_CODE.equals(curNodeCode)) {//决策2
			if(isFromDepartment) {
				nextNodeCode = STREET_DEPARTMENT_EVA_NODE_CODE;
			} else if(isFromParty) {
				nextNodeCode = STREET_END_NODE_CODE;
			}
		} else if(DISTRICT_CONFIRM_NODE_CODE.equals(curNodeCode)) {//决策3
			if(isFromDepartment) {
				nextNodeCode = DISTRICT_C_STREET_DEPARTMENT_EVA_NODE_CODE;
			} else if(isFromParty) {
				nextNodeCode = DISTRICT_C_STREET_PARTY_EVA_NODE_CODE;
			}
		} else if(DISTRICT_HANDLE_NODE_CODE.equals(curNodeCode)) {//决策4
			if(isFromDepartment) {
				nextNodeCode = DISTRICT_H_STREET_DEPARTMENT_EVA_NODE_CODE;
			} else if(isFromParty) {
				nextNodeCode = DISTRICT_H_STREET_PARTY_EVA_NODE_CODE;
			}
		} else if(STREET_DEPARTMENT_HANDLE_NODE_CODE.equals(curNodeCode)) {//决策5
			if(isTheSame) {
				nextNodeCode = STREET_D_END_NODE_CODE;
			} else {
				nextNodeCode = STREET_D_STREET_CONFIRM_NODE_CODE;
			}
		} else if(STREET_CONFIRM_NODE_CODE.equals(curNodeCode)) {//决策6
			if(isFromDepartment) {
				nextNodeCode = STREET_C_STREET_DEPARTMENT_EVA_NODE_CODE;
			} else if(isFromParty) {
				nextNodeCode = STREET_C_STREET_PARTY_EVA_NODE_CODE;
			}
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		return nextNodeCode;
	}

}
