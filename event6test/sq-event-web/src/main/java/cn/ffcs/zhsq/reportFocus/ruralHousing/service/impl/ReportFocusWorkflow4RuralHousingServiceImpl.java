package cn.ffcs.zhsq.reportFocus.ruralHousing.service.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportFocusWorkflow4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 农村建房工作流相关接口
 * @ClassName:   ReportFocusWorkflow4RuralHousingServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年3月8日 下午6:51:21
 */
@Service(value = "reportFocusWorkflow4RuralHousingService")
public class ReportFocusWorkflow4RuralHousingServiceImpl extends ReportFocusWorkflow4TwoVioPreServiceImpl {

	@Autowired
	private IReportFocusService reportFocusService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Override
	public boolean startWorkflow4Report(Long reportId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		Long userOrgId = userInfo.getOrgId();
		
		if(userOrgId != null && userOrgId > 0) {
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userOrgId);
			
			if(orgInfo != null) {
				if(!ConstantValue.STREET_ORG_LEVEL.equals(orgInfo.getChiefLevel())) {
					throw new ZhsqEventException("当前操作用户组织层级不是【街道/乡镇】！");
				}
				
				if(!String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgInfo.getOrgType())) {
					throw new ZhsqEventException("当前操作用户组织类型不是【部门】！");
				}
			} else {
				throw new ZhsqEventException("组织id【" + userOrgId + "】没有找到有效的组织信息！");
			}
		} else {
			throw new ZhsqEventException("缺少操作用户组织信息！");
		}
		
		flag = super.startWorkflow4Report(reportId, userInfo, extraParam);
		
		return flag;
	}
	
	@Override
	public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = super.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);
		
		if(flag) {
			Long reportId = null;
			
			try {
				if(CommonFunctions.isNotBlank(extraParam, "formId")) {
					reportId = Long.valueOf(extraParam.get("formId").toString());
				} else if(CommonFunctions.isNotBlank(extraParam, "reportId")) {
					reportId = Long.valueOf(extraParam.get("reportId").toString());
				}
			} catch(NumberFormatException e) {}
			
			if(reportId != null && reportId > 0) {
				Map<String, Object> reportFocus = new HashMap<String, Object>();
				String formTypeIdStr = null;
				
				if(CommonFunctions.isNotBlank(extraParam, "formTypeId")) {
					formTypeIdStr = extraParam.get("formTypeId").toString();
				} else if(CommonFunctions.isNotBlank(extraParam, "proInstance")) {
					ProInstance proInstance = (ProInstance) extraParam.get("proInstance");
					
					formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
				}
				
				if(FocusReportNode357Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
					
				}
				
				if(!reportFocus.isEmpty()) {
					String reportType = null;
					
					if(CommonFunctions.isNotBlank(extraParam, "reportType")) {
						reportType = extraParam.get("reportType").toString();
					} else {
						reportType = ReportTypeEnum.ruralHousing.getReportTypeIndex();
					}
					
					reportFocus.put("reportType", reportType);
					reportFocus.put("reportId", reportId);
					
					reportFocusService.saveReportFocus(reportFocus, userInfo);
				}
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean recallWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		extraParam.put("isAble2Recall", true);
		
		flag = super.recallWorkflow4Report(instanceId, userInfo, extraParam);
		
		return flag;
	}
	
	@SuppressWarnings("serial")
	@Override
	public Map<String, Object> initWorkflow4Handle(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		Map<String, Object> resultMap = super.initWorkflow4Handle(instanceId, userInfo, params);
		boolean isAble2Handle = false;
		
		if(CommonFunctions.isNotBlank(resultMap, "isAble2Handle")) {
			isAble2Handle = Boolean.valueOf(resultMap.get("isAble2Handle").toString());
		}
		
		if(isAble2Handle) {
			String curNodeName = null;
			int actionableAttachmentType = 0;
			Map<String, Integer> ACTIONABLE_ATTACHMENT_TYPE = new HashMap<String, Integer>() {
				{
					//签订告知书
					put(FocusReportNode357Enum.SIGN_NOTIFICATION_NODE_NAME.getTaskNodeName(), 1);
					//组织放样
					put(FocusReportNode357Enum.LOFTING_ORGANIZE_NODE_NAME.getTaskNodeName(), 2);
					//组织验线
					put(FocusReportNode357Enum.LINE_CHECK_ORGANIZE_NODE_NAME.getTaskNodeName(), 3);
					//组织施工节点验收
					put(FocusReportNode357Enum.CONSTRUCTION_ACCEPTANCE_ORGANIZE_NODE_NAME.getTaskNodeName(), 4);
					//组织竣工验收
					put(FocusReportNode357Enum.COMPLETION_ACCEPTANCE_ORGANIZE_NODE_NAME.getTaskNodeName(), 5);
					//办结归档
					put(FocusReportNode357Enum.COMPLETION_NODE_NAME.getTaskNodeName(), 6);
				}
			};
			
			if(CommonFunctions.isNotBlank(resultMap, "NODE_NAME")) {
				curNodeName = resultMap.get("NODE_NAME").toString();
			}
			
			if(CommonFunctions.isNotBlank(ACTIONABLE_ATTACHMENT_TYPE, curNodeName)) {
				actionableAttachmentType = ACTIONABLE_ATTACHMENT_TYPE.get(curNodeName);
			}
			
			if(actionableAttachmentType > 0) {
				resultMap.put("actionableAttachmentType", actionableAttachmentType);
			}
		}
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> resultMapList = super.capHandledTaskInfoMap(instanceId, order, params);
		
		if(resultMapList != null && resultMapList.size() > 0 
				&& IWorkflow4BaseService.SQL_ORDER_DESC.equals(order)) {
			Map<String, Object> taskMap = resultMapList.get(0);
			
			if(CommonFunctions.isBlank(taskMap, "IS_CURRENT_TASK")) {
				String taskNameZH = null;
				
				if(CommonFunctions.isNotBlank(taskMap, "TASK_NAME")) {
					taskNameZH = taskMap.get("TASK_NAME").toString();
				}
				
				if(!FocusReportNode357Enum.END_NODE_NAME.getTaskNodeNameZH().equals(taskNameZH)) {
					Map<String, Object> endTaskMap = new HashMap<String, Object>();
					
					endTaskMap.put("TASK_NAME", FocusReportNode357Enum.END_NODE_NAME.getTaskNodeNameZH());
					
					resultMapList.add(0, endTaskMap);
				}
			}
		}
		
		return resultMapList;
	}
	
	/**
	 * 获取流程图名称
	 * @param reportId	上报id
	 * @param userInfo	操作用户
	 * @param params	额外参数
	 * @return
	 */
	protected String findWorkflowName(Long reportId, UserInfo userInfo, Map<String, Object> params) {
		String reportType = null, workflowName = null;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		
		//后续转功能配置
		if(ReportTypeEnum.ruralHousing.getReportTypeIndex().equals(reportType)) {
			workflowName = "南安农村建房流程";
		}
		
		return workflowName;
	}
	
	/**
	 * 获取流程类型
	 * @param reportId	上报id
	 * @param userInfo	操作用户
	 * @param params	额外参数
	 * @return
	 */
	protected String findWfTypeId(Long reportId, UserInfo userInfo, Map<String, Object> params) {
		String reportType = null, wfTypeId = null;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		
		//后续转功能配置
		if(ReportTypeEnum.ruralHousing.getReportTypeIndex().equals(reportType)) {
			wfTypeId = "focus_report";
		}
		
		return wfTypeId;
	}
	
	/**
	 * 获取下一可办理环节
	 * @param instanceId	流程实例id
	 * @param userInfo		用户信息
	 * @param params		额外参数
	 * 			instanceId	流程实例id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("serial")
	protected List<Map<String, Object>> findNextTaskNodes(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> nodeMapList = null;
		boolean isDecisionMaking = false;
		ProInstance proInstance = null;
		String curNodeName = null, formTypeIdStr = null;
		
		params = params == null ? new HashMap<String, Object>() : params;
		
		if((instanceId == null || instanceId <= 0) && CommonFunctions.isNotBlank(params, "instanceId")) {
			instanceId = Long.valueOf(params.get("instanceId").toString());
		}
		
		if(instanceId == null || instanceId <= 0) {
			throw new IllegalArgumentException("缺少有效的流程实例id！");
		}
		
		proInstance = baseWorkflowService.findProByInstanceId(instanceId);
		
		if(proInstance == null) {
			throw new IllegalArgumentException("流程实例id【"+ instanceId +"】没有找到有效的流程信息！");
		}
		
		formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
		
		if(CommonFunctions.isNotBlank(params, "curNodeName")) {
			curNodeName = params.get("curNodeName").toString();
		}
		
		params.put("proInstance", proInstance);
		params.put("isDecisionMaking", isDecisionMaking);
		
		nodeMapList = super.findNextTaskNodes(instanceId, userInfo, params);
		
		if(nodeMapList != null && FocusReportNode357Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			//调整下一办理环节展示名称
			StringBuffer nodeName = new StringBuffer("");
			
			if(FocusReportNode357Enum.LINE_CHECK_ORGANIZE_NODE_NAME.getTaskNodeName().equals(curNodeName)
					|| FocusReportNode357Enum.CONSTRUCTION_ACCEPTANCE_ORGANIZE_NODE_NAME.getTaskNodeName().equals(curNodeName)
					|| FocusReportNode357Enum.COMPLETION_ACCEPTANCE_ORGANIZE_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				Map<String, String> NODE_MAP = new HashMap<String, String>() {
					{
						//组织验线
						put(FocusReportNode357Enum.LINE_CHECK_ORGANIZE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode357Enum.LINE_CHECK_REFORM_NODE_NAME.getTaskNodeName(), "验线不合格");
						put(FocusReportNode357Enum.LINE_CHECK_ORGANIZE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode357Enum.CONSTRUCTION_ACCEPTANCE_START_NODE_NAME.getTaskNodeName(), "验线合格");
						
						//组织施工节点验收
						put(FocusReportNode357Enum.CONSTRUCTION_ACCEPTANCE_ORGANIZE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode357Enum.CONSTRUCTION_ACCEPTANCE_REFORM_NODE_NAME.getTaskNodeName(), "验收不合格");
						
						//组织竣工验收
						put(FocusReportNode357Enum.COMPLETION_ACCEPTANCE_ORGANIZE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode357Enum.COMPLETION_ACCEPTANCE_REFORM_NODE_NAME.getTaskNodeName(), "验收不合格");
						put(FocusReportNode357Enum.COMPLETION_ACCEPTANCE_ORGANIZE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode357Enum.COMPLETION_NODE_NAME.getTaskNodeName(), "验收合格");
					}
				};
				
				for(Map<String, Object> nodeMap : nodeMapList) {
					nodeName.setLength(0);
					
					if(CommonFunctions.isNotBlank(nodeMap, "nodeName")) {
						nodeName.append(curNodeName).append("-").append(nodeMap.get("nodeName").toString());
					}
					
					if(CommonFunctions.isNotBlank(NODE_MAP, nodeName.toString())) {
						nodeMap.put("nodeNameZH", NODE_MAP.get(nodeName.toString()));
					}
				}
			}
		}
		
		return nodeMapList;
	}
	
}
