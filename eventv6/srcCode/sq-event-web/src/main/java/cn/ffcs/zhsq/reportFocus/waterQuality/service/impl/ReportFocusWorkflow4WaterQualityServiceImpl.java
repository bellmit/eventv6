package cn.ffcs.zhsq.reportFocus.waterQuality.service.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportFocusWorkflow4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 流域水质工作流相关接口
 * @ClassName:   ReportFocusWorkflow4WaterQualityServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月16日 下午5:30:44
 */
@Service(value = "reportFocusWorkflow4WaterQualityService")
public class ReportFocusWorkflow4WaterQualityServiceImpl extends ReportFocusWorkflow4TwoVioPreServiceImpl {

	@Autowired
	private IReportFocusService reportFocusService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Override
	public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = super.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);
		
		if(flag && FocusReportNode35401Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
			Map<String, Object> reportFocusMap = new HashMap<String, Object>();
			
			if(CommonFunctions.isNotBlank(extraParam, "administrativeSactionType")) {
				reportFocusMap.put("administrativeSactionType", extraParam.get("administrativeSactionType"));
			}
			
			if(CommonFunctions.isNotBlank(extraParam, "administrativeSactionDes")) {
				reportFocusMap.put("administrativeSactionDes", extraParam.get("administrativeSactionDes"));
			}
			
			if(!reportFocusMap.isEmpty()) {
				String reportType = ReportTypeEnum.waterQuality.getReportTypeIndex();
				Long reportId = null;
				
				if(CommonFunctions.isNotBlank(extraParam, "reportType")) {
					reportType = extraParam.get("reportType").toString();
				}
				
				if(CommonFunctions.isNotBlank(extraParam, "formId")) {
					try {
						reportId = Long.valueOf(extraParam.get("formId").toString());
					} catch(NumberFormatException e) {}
				} else if(CommonFunctions.isNotBlank(extraParam, "proInstance")) {
					ProInstance proInstance = (ProInstance) extraParam.get("proInstance");
					
					reportId = proInstance.getFormId();
				} else if(instanceId != null && instanceId > 0) {
					ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
					
					if(proInstance != null) {
						reportId = proInstance.getFormId();
					}
				}
				
				if(reportId != null && reportId > 0) {
					reportFocusMap.put("reportType", reportType);
					reportFocusMap.put("reportId", reportId);
					
					reportFocusService.saveReportFocus(reportFocusMap, userInfo);
				}
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean rejectWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false, isRejectByParent = true, isBack2Draft = false;
		String formTypeIdStr = null, curNodeName = null;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
			curNodeName = extraParam.get("curNodeName").toString();
		}
		if(CommonFunctions.isNotBlank(extraParam, "formTypeId")) {
			formTypeIdStr = extraParam.get("formTypeId").toString();
		}
		
		if(FocusReportNode35401Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode35401Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isRejectByParent = false;
				isBack2Draft = true;
			} else if(FocusReportNode35401Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isRejectByParent = true;
				isBack2Draft = true;
			}
		} else if(FocusReportNode35402Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode35402Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isRejectByParent = false;
				isBack2Draft = true;
			}
		} else if(FocusReportNode35403Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode35403Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isRejectByParent = false;
				isBack2Draft = true;
			} else if(FocusReportNode35403Enum.BEE_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isRejectByParent = true;
				isBack2Draft = true;
			}
		}
		
		extraParam.put("isRejectByParent", isRejectByParent);
		extraParam.put("isBack2Draft", isBack2Draft);
		
		flag = super.rejectWorkflow4Report(instanceId, userInfo, extraParam);
		
		return flag;
	}
	
	@Override
	public boolean recallWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		
		extraParam.put("isAble2Recall", true);
		
		flag = super.recallWorkflow4Report(instanceId, userInfo, extraParam);
		
		return flag;
	}
	
	@Override
	public Map<String, Object> initWorkflow4Handle(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		Map<String, Object> initMap = super.initWorkflow4Handle(instanceId, userInfo, params);
		String curNodeName = null, formTypeIdStr = null;
		boolean isEditableNode = false;
		
		if(CommonFunctions.isBlank(params, "reportType")) {
			initMap.put("reportType", ReportTypeEnum.waterQuality.getReportTypeIndex());
		}

		if(CommonFunctions.isNotBlank(initMap, "curNodeName")) {
			curNodeName = initMap.get("curNodeName").toString();
		}

		if(CommonFunctions.isNotBlank(initMap, "formTypeId")) {
			formTypeIdStr = initMap.get("formTypeId").toString();
		}

		//当前环节为 第一副网格长(驻村工作队长)核实 节点时，基本信息可编辑
		if(CommonFunctions.isNotBlank(initMap, "isAble2Handle")
				&& Boolean.valueOf(initMap.get("isAble2Handle").toString())
				&& (FocusReportNode35401Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
						&& FocusReportNode35401Enum.VERIFY_TASK_GRID_NODE_NAME.getTaskNodeName().equals(curNodeName))
				|| (FocusReportNode35402Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr) 
						&& FocusReportNode35402Enum.BEE_SQUADRON_VERIFY_NODE_NAME.getTaskNodeName().equals(curNodeName))
		) {
			isEditableNode = true;
			
			initMap.put("isEditable", true);
		}
		
		initMap.put("isEditableNode", isEditableNode);
		
		return initMap;
	}
	
	@Override
	public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> taskInfoMapList = super.capHandledTaskInfoMap(instanceId, order, params);
		
		if(taskInfoMapList != null) {
			String formTypeIdStr = null;
			
			if(CommonFunctions.isNotBlank(params, "formTypeId")) {
				formTypeIdStr = params.get("formTypeId").toString();
			} else if(instanceId != null && instanceId > 0) {
				ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
				if(proInstance != null) {
					formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
				}
			}
			
			//南安流域水质信息流程_非网格员 将自构建的环节“生态环境局处理”隐藏处理
			if(FocusReportNode35402Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				String taskCode = null;
				List<Map<String, Object>> removeTaskInfoList = new ArrayList<Map<String, Object>>();
				
				for(Map<String, Object> taskInfoMap : taskInfoMapList) {
					if(CommonFunctions.isNotBlank(taskInfoMap, "TASK_CODE")) {
						taskCode = taskInfoMap.get("TASK_CODE").toString();
					}
					
					if(FocusReportNode35402Enum.BEE_HANDLE_NODE_NAME.getTaskNodeName().equals(taskCode)) {
						removeTaskInfoList.add(taskInfoMap);
					}
				}
				
				taskInfoMapList.removeAll(removeTaskInfoList);
			}

			//归档事件增加虚拟归档环节
			if(taskInfoMapList.size() > 0 && IWorkflow4BaseService.SQL_ORDER_DESC.equals(order)) {
				Map<String, Object> taskMap = taskInfoMapList.get(0);

				if(CommonFunctions.isBlank(taskMap, "IS_CURRENT_TASK")) {
					Map<String, Object> endTaskMap = new HashMap<String, Object>();

					endTaskMap.put("TASK_NAME", FocusReportNode35401Enum.END_NODE_NAME.getTaskNodeNameZH());

					taskInfoMapList.add(0, endTaskMap);
				}
			}

		}
		
		return taskInfoMapList;
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
		if(ReportTypeEnum.waterQuality.getReportTypeIndex().equals(reportType)) {
			String dataSource = null, matterType = null;
			
			if(CommonFunctions.isNotBlank(params, "dataSource")) {
				dataSource = params.get("dataSource").toString();
			}
			
			if(CommonFunctions.isNotBlank(params, "matterType")) {
				matterType = params.get("matterType").toString();
			}
			
			if((StringUtils.isBlank(dataSource) || StringUtils.isBlank(matterType)) && reportId != null && reportId > 0) {
				Map<String, Object> reportParams = new HashMap<String, Object>(),
									reportMap = null;
				reportParams.putAll(params);
				reportParams.put("reportId", reportId);
				
				reportMap = reportFocusService.findReportFocusByUUID(null, userInfo, reportParams);
				
				if(StringUtils.isBlank(dataSource) && CommonFunctions.isNotBlank(reportMap, "dataSource")) {
					dataSource = reportMap.get("dataSource").toString();
				}
				
				if(StringUtils.isBlank(matterType) && CommonFunctions.isNotBlank(reportMap, "matterType")) {
					matterType = reportMap.get("matterType").toString();
				}
			}
			
			if(WaterQualityMatterTypeEnum.SUDDEN.getMatterType().equals(matterType)) {
				workflowName = "南安流域水质信息流程_突发";
			} else {
				if(WaterQualityDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)) {
					workflowName = "南安流域水质信息流程_网格员";
				} else {
					workflowName = "南安流域水质信息流程_非网格员";
				}
			}
		}
		
		//后续转功能配置
		if(ReportTypeEnum.epidemicPreControl.getReportTypeIndex().equals(reportType)) {
			workflowName = "南安疫情防控信息流程";
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
		if(ReportTypeEnum.waterQuality.getReportTypeIndex().equals(reportType)) {
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
	protected List<Map<String, Object>> findNextTaskNodes(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> nextNodeMapList = super.findNextTaskNodes(instanceId, userInfo, params);
		ProInstance proInstance = null;
		String formTypeIdStr = null;
		
		if(CommonFunctions.isNotBlank(params, "proInstance")) {
			proInstance = (ProInstance) params.get("proInstance");
			formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
		}
		
		if(FocusReportNode35402Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) && nextNodeMapList != null) {
			String nextNodeName = null;
			
			for(Map<String, Object> nextNodeMap : nextNodeMapList) {
				nextNodeName = null;
				
				if(CommonFunctions.isNotBlank(nextNodeMap, "nodeName")) {
					nextNodeName = nextNodeMap.get("nodeName").toString();
				}
				
				if(FocusReportNode35402Enum.BEE_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
					nextNodeMapList.remove(nextNodeMap);
					break;
				}
			}
		}
		
		return nextNodeMapList;
	}
}
