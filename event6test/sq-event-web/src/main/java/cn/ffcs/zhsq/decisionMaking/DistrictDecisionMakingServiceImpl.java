package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 思明区 区级流程图决策类
 * decisionService	指定实现类 districtDecisionMakingService
 * curNodeCode		当前环节节点编码
 * curOrgId			当前环节办理人员所属组织
 * createOrgId		当前事件的采集人员所属组织
 * instanceId		流程实例id
 * @author zhangls
 *
 */
@Service(value = "districtDecisionMakingService")
public class DistrictDecisionMakingServiceImpl extends ApplicationObjectSupport
		implements IWorkflowDecisionMakingService<String> {

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	private static final String STREET_CONFIRM_NODE_CODE = "task9";//街道党政办确认
	
	//决策1
	private static final String DECISION_MAKING_NODE_CODE = "decision1";			//决策1环节节点编码
	private static final String DISTRICT_DEPARTMENT_HANDLE_NODE_CODE = "task1";		//区部门办理
	
	//决策2
	private static final String DISTRICT_HANDLE_NODE_CODE = "task2";					//区指挥中心办理环节节点编码
	private static final String DISTRICT_H_END_NODE_CODE = "end1";						//办结节点
	private static final String DISTRICT_H_DISTRICT_DEPARTMENT_EVA_NODE_CODE = "task3";	//区部门评价
	
	//决策3
	private static final String DISTRICT_CONFIRM_NODE_CODE = "task10";//区指挥中心确认环节节点编码
	
	//决策4
	private static final String DISTRICT_DEPARTMENT_EVA_NODE_CODE = "task11";	//区部门评价环节节点
	private static final String DISTRICT_EVA_NODE_CODE = "task12";				//区指挥中心评价环节节点
	private static final String DISTRICT_E_STREET_EVA_NODE_CODE = "task13";		//街道党政办评价
	private static final String DISTRICT_E_END_NODE_CODE = "end2";				//办结节点
	
	//决策5
	private static final String DISTRICT_DEPARTMENT_NODE_CODE = "task14";	//区部门办理环节节点编码
	private static final String DISTRICT_D_END_NODE_CODE = "end3";			//办结节点
	
	//决策6
	private static final String DISTRICT_D_CONFIRM_NODE_CODE = "task15";				//区指挥中心确认环节节点编码
	private static final String DISTRICT_D_DISTRICT_EVA_NODE_CODE = "task17";			//区指挥中心评价
	private static final String DISTRICT_D_DISTRICT_DEPARTMENT_EVA_NODE_CODE = "task16";//区部门评价
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		String curNodeCode = "";	//当前环节节点编码
		String nextNodeCode = "";	//下一环节节点编码
		String curOrgId	= "";		//当前办理人员所属组织
		String createOrgId = "";	//事件采集人员所属组织
		Long instanceId = -1L;		//流程实例id
		String createProfessionCode = "";	//事件采集人员组织专业编码
		boolean isFromGov = false;			//采集人员是否为区指挥中心办理
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
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[instanceId]："+params.get("instanceId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[instanceId]，请检查！");
		}
		
		isFromGov = ConstantValue.GOV_PROFESSION_CODE.equals(createProfessionCode);
		isTheSame = curOrgId.equals(createOrgId);
		
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode)) {//决策1
			if(isFromGov) {
				nextNodeCode = DISTRICT_HANDLE_NODE_CODE;
			} else {
				nextNodeCode = DISTRICT_DEPARTMENT_HANDLE_NODE_CODE;
			}
		} else if(DISTRICT_HANDLE_NODE_CODE.equals(curNodeCode)) {//决策2
			if(isTheSame) {
				nextNodeCode = DISTRICT_H_END_NODE_CODE;
			} else {
				nextNodeCode = DISTRICT_H_DISTRICT_DEPARTMENT_EVA_NODE_CODE;
			}
		} else if(DISTRICT_CONFIRM_NODE_CODE.equals(curNodeCode)) {//决策3
			if(isFromGov) {
				nextNodeCode = DISTRICT_EVA_NODE_CODE;
			} else {
				nextNodeCode = DISTRICT_DEPARTMENT_EVA_NODE_CODE;
			}
		} else if(DISTRICT_DEPARTMENT_EVA_NODE_CODE.equals(curNodeCode) || DISTRICT_EVA_NODE_CODE.equals(curNodeCode)) {//决策4
			//事件办理过程中是否经过“街道党政办确认”环节
			Map<String, Object> taskInfoMap = eventDisposalWorkflowService.capDoneTaskInfo(instanceId, STREET_CONFIRM_NODE_CODE);
			if(taskInfoMap != null) {
				nextNodeCode = DISTRICT_E_STREET_EVA_NODE_CODE;
			} else {
				nextNodeCode = DISTRICT_E_END_NODE_CODE;
			}
		} else if(DISTRICT_DEPARTMENT_NODE_CODE.equals(curNodeCode)) {//决策5
			if(isTheSame) {
				nextNodeCode = DISTRICT_D_END_NODE_CODE;
			} else {
				nextNodeCode = DISTRICT_D_CONFIRM_NODE_CODE;
			}
		} else if(DISTRICT_D_CONFIRM_NODE_CODE.equals(curNodeCode)) {//决策6
			if(isFromGov) {
				nextNodeCode = DISTRICT_D_DISTRICT_EVA_NODE_CODE;
			} else {
				nextNodeCode = DISTRICT_D_DISTRICT_DEPARTMENT_EVA_NODE_CODE;
			}
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		return nextNodeCode;
	}

}
