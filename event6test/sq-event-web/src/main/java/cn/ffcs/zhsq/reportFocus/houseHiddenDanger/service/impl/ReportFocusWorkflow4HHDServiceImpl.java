package cn.ffcs.zhsq.reportFocus.houseHiddenDanger.service.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportFocusWorkflow4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 房屋安全隐患(House Hidden Danger)工作流相关接口
 * @ClassName:   ReportFocusWorkflow4HHDServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月17日 下午5:14:09
 */
@Service(value = "reportFocusWorkflow4HHDService")
public class ReportFocusWorkflow4HHDServiceImpl extends ReportFocusWorkflow4TwoVioPreServiceImpl {

	@Autowired
	private IReportFocusService reportFocusService;
	
	@Autowired
	private IWorkflow4BaseService workflow4BaseService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Override
	public Map<String, Object> initWorkflow4Handle(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		Map<String, Object> initMap = new HashMap<>();
		String curNodeName = null, formTypeIdStr = null;
		boolean isEditableNode = false;

		if(null == instanceId && CommonFunctions.isNotBlank(params,"instanceId")){
			instanceId = Long.valueOf(String.valueOf(params.get("instanceId")));
		}


		initMap = super.initWorkflow4Handle(instanceId, userInfo, params);


		if(CommonFunctions.isBlank(params, "reportType")) {
			initMap.put("reportType", ReportTypeEnum.houseHiddenDanger.getReportTypeIndex());
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
				&& (FocusReportNode35301Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
				&& FocusReportNode35301Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName))
		) {
			isEditableNode = true;
			
			initMap.put("isEditable", true);
		}else if(FocusReportNode35302Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)){
			//当流程为突发房屋安全隐患时，当前环节为 第一网格长(驻村领导)处置
			if(FocusReportNode35302Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
				//判断是否存在子流程（状态反馈跟踪）存在：下一环节为归档；不存在：下一环节为构造的虚拟环节（二级网格长定期报告安全隐患状态），并启动子流程
				Map<String, Object> instanceParams = new HashMap<String, Object>();
				List<Map<String, Object>> instanceMapList = null;
				List<Node> nextTaskNodes = new ArrayList<>();
				Node node = new Node();

				instanceParams.put("parentInstanceId", instanceId);
				instanceParams.put("instanceStatus", "1");

				instanceMapList = baseWorkflowService.findProInstanceList(instanceParams);

				if(instanceMapList == null || instanceMapList.size() == 0) {
					node.setNodeName(FocusReportNode35302Enum.GRID_HANDLE_NODE_NAME.getTaskNodeName());
					node.setNodeNameZH(FocusReportNode35302Enum.GRID_HANDLE_NODE_NAME.getTaskNodeNameZH());
					node.setNodeCode("U6");
					node.setTransitionCode("U5S1U6");
					node.setNodeType("1");
					nextTaskNodes.add(node);

					if(nextTaskNodes.size() > 0){
						initMap.put("nextTaskNodes", nextTaskNodes);
						//只有当下一可办理环节为一个时，默认选中
						initMap.put("isChooseDefaultRadio", nextTaskNodes != null && nextTaskNodes.size() == 1);
					}
				}
			}
		}
		
		initMap.put("isEditableNode", isEditableNode);

		return initMap;
	}
	
	@Override
	public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		String curNodeName = null, formTypeIdStr = null, preCurTaskId = null;
		Long reportId = null;


		if(instanceId != null && instanceId > 0) {
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			
			if(proInstance != null) {
				curNodeName = proInstance.getCurNode();
				reportId = proInstance.getFormId();
				formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
			}
		}
		
		if(
				(FocusReportNode35301Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
						&& FocusReportNode35301Enum.HHD_COGNIZANCE_NODE_NAME.getTaskNodeName().equals(curNodeName))
				|| (FocusReportNode35302Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
						&& FocusReportNode35302Enum.FIND_HHD_NODE_NAME.getTaskNodeName().equals(curNodeName))) {
			Map<String, Object> preCurTaskData = workflow4BaseService.findCurTaskData4Base(instanceId);
			
			if(CommonFunctions.isNotBlank(preCurTaskData, "WF_DBID_")) {
				preCurTaskId = preCurTaskData.get("WF_DBID_").toString();
			}
		}

		//只启动子流程“南安房屋安全隐患信息流程_突发_定期报告”，因下一环节为虚拟环节，主流程不流转，
		if(FocusReportNode35302Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& FocusReportNode35302Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)
				&& FocusReportNode35302Enum.GRID_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
			Map<String, Object> subProParams = new HashMap<String, Object>();
			UserInfo nextUserInfo = new UserInfo();


			if(nextUserInfoList != null && nextUserInfoList.size() > 0) {
				nextUserInfo = nextUserInfoList.get(0);
			} else if(CommonFunctions.isNotBlank(extraParam, "nextUserIds")
					&& CommonFunctions.isNotBlank(extraParam, "nextOrgIds")) {
				Long nextUserId = Long.valueOf(extraParam.get("nextUserIds").toString().split(",")[0]),
						nextOrgId = Long.valueOf(extraParam.get("nextOrgIds").toString().split(",")[0]);
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(nextOrgId);
				UserBO userBO = userManageService.getUserInfoByUserId(nextUserId);

				if(userBO != null && userBO.getUserId() != null) {
					nextUserInfo.setUserId(userBO.getUserId());
					nextUserInfo.setPartyName(userBO.getPartyName());
				}

				if(orgInfo != null && orgInfo.getOrgId() != null) {
					nextUserInfo.setOrgId(orgInfo.getOrgId());
					nextUserInfo.setOrgCode(orgInfo.getOrgCode());
					nextUserInfo.setOrgName(orgInfo.getOrgName());
				}
			}

			subProParams.putAll(extraParam);
			subProParams.put("parentId", instanceId);
			subProParams.put("taskId", preCurTaskId);
			subProParams.put("isConvertProinstance", false);
			subProParams.remove("instanceId");//若不移除，会影响子流程实例的生成

			//启动子流程“南安房屋安全隐患信息流程_突发_定期报告”
			subProParams.put("formTypeId", FocusReportNode3530201Enum.FORM_TYPE_ID.toString());

			flag = startWorkflow4Report(reportId, userInfo, subProParams);

			//子流程启动成功 发送mq消息
			if(flag){
				sendSubFlowRMQmMsg(reportId,formTypeIdStr,FocusReportNode35302Enum.FORM_TYPE_ID.toString(),instanceId,subProParams.get("instanceId"));
			}
		}else {
			flag = super.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);
		}

		if(flag) {
			if(FocusReportNode35301Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				if(FocusReportNode35301Enum.HHD_COGNIZANCE_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode35301Enum.MASSES_REPORT_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode35301Enum.ASSIGNED_SUPERIOR_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode35301Enum.FIREPROTECTION_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode35301Enum.BUSINESS_LICENSE_NODE_NAME.getTaskNodeName().equals(curNodeName) ) {
					String riskType = null;
					String regionCode = null;
					
					if(reportId != null && reportId > 0) {
						Map<String, Object> reportFocus = new HashMap<String, Object>();

						if(CommonFunctions.isNotBlank(extraParam, "riskType")) {
							riskType = extraParam.get("riskType").toString();
							
							reportFocus.put("reportId", reportId);
							reportFocus.put("riskType", riskType);
						}

						if(CommonFunctions.isNotBlank(extraParam, "regionCode")) {
							regionCode = extraParam.get("regionCode").toString();
							reportFocus.put("regionCode", regionCode);
						}
						
						if(!reportFocus.isEmpty()) {
							String reportType = null;


							if(CommonFunctions.isNotBlank(extraParam, "reportType")) {
								reportType = extraParam.get("reportType").toString();
							} else {
								reportType = ReportTypeEnum.houseHiddenDanger.getReportTypeIndex();
							}
							
							reportFocus.put("reportType", reportType);
							reportFocus.put("reportId", reportId);

							reportFocusService.saveReportFocus(reportFocus, userInfo);
						}
					}

					//当前环节为 task4 时 开启子流程
					if(FocusReportNode35301Enum.HHD_COGNIZANCE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
						if(HHDRiskTypeEnum.COMMON.getRiskType().equals(riskType)
								|| HHDRiskTypeEnum.GREAT.getRiskType().equals(riskType)) {
							Map<String, Object> subProParams = new HashMap<String, Object>();

							subProParams.putAll(extraParam);
							subProParams.put("parentId", instanceId);
							subProParams.put("taskId", preCurTaskId);
							subProParams.put("isConvertProinstance", false);
							subProParams.remove("instanceId");//若不移除，会影响子流程实例的生成

							if(HHDRiskTypeEnum.COMMON.getRiskType().equals(riskType)) {
								//启动子流程“南安房屋安全隐患信息流程_常规_一般”
								subProParams.put("formTypeId", FocusReportNode3530101Enum.FORM_TYPE_ID.toString());
							} else if(HHDRiskTypeEnum.GREAT.getRiskType().equals(riskType)) {
								//启动子流程“南安房屋安全隐患信息流程_常规_重大”
								subProParams.put("formTypeId", FocusReportNode3530102Enum.FORM_TYPE_ID.toString());
							}

							boolean subFlowResult = startWorkflow4Report(reportId, userInfo, subProParams);

							//子流程启动成功 发送mq消息
							if(subFlowResult){
								sendSubFlowRMQmMsg(reportId,formTypeIdStr,String.valueOf(subProParams.get("formTypeId")),instanceId,subProParams.get("instanceId"));
							}
						}
					}
				} else if(FocusReportNode35301Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
					//将历史的“南安房屋安全隐患信息流程_常规_一般”子流程均强制结束
					endAllSubProInstance(instanceId, FocusReportNode3530101Enum.FORM_TYPE_ID.toString(),null);
					
					//将历史的“南安房屋安全隐患信息流程_常规_重大”子流程均强制结束
					endAllSubProInstance(instanceId, FocusReportNode3530102Enum.FORM_TYPE_ID.toString(),null);
				}
			} else if(FocusReportNode35302Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				//当前环节为task2 task3 更新上报信息 regionCode
				if(FocusReportNode35302Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode35302Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
					Map<String, Object> reportFocus = new HashMap<String, Object>();

					if(reportId != null && reportId > 0) {
						String reportType = null;
						String regionCode = null;

						if(CommonFunctions.isNotBlank(extraParam, "reportType")) {
							reportType = extraParam.get("reportType").toString();
						} else {
							reportType = ReportTypeEnum.houseHiddenDanger.getReportTypeIndex();
						}

						if(CommonFunctions.isNotBlank(extraParam, "regionCode")) {
							regionCode = extraParam.get("regionCode").toString();
						}

						reportFocus.put("regionCode", regionCode);
						reportFocus.put("reportType", reportType);
						reportFocus.put("reportId", reportId);

						reportFocusService.saveReportFocus(reportFocus, userInfo);
					}

				}
				if(FocusReportNode35302Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
					//将历史的“南安房屋安全隐患信息流程_突发_定期报告”子流程均强制结束
					endAllSubProInstance(instanceId, FocusReportNode3530201Enum.FORM_TYPE_ID.toString(),null);
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
		
		if(FocusReportNode35301Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode35301Enum.FIND_HHD_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isRejectByParent = false;
				isBack2Draft = true;
			} else if(FocusReportNode35301Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isRejectByParent = true;
				isBack2Draft = true;
			}
		} else if(FocusReportNode35302Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode35302Enum.FIND_HHD_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isRejectByParent = false;
				isBack2Draft = true;
			} else if(FocusReportNode35302Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isRejectByParent = true;
				isBack2Draft = true;
			}
		}
		
		extraParam.put("isRejectByParent", isRejectByParent);
		extraParam.put("isBack2Draft", isBack2Draft);
		
		flag = super.rejectWorkflow4Report(instanceId, userInfo, extraParam);
		
		if(flag) {
			//为了获取最新的当前办理节点名称，因此不能使用extraParam中携带的实例对象信息
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			Long reportId = null;
			
			if(proInstance != null) {
				curNodeName = proInstance.getCurNode();
				formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
				reportId = proInstance.getFormId();
			}
			
			if(FocusReportNode35301Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&& FocusReportNode35301Enum.HHD_COGNIZANCE_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				Map<String, Object> reportFocus = new HashMap<String, Object>();
				
				if(reportId != null && reportId > 0) {
					String reportType = null;
					
					if(CommonFunctions.isNotBlank(extraParam, "reportType")) {
						reportType = extraParam.get("reportType").toString();
					} else {
						reportType = ReportTypeEnum.houseHiddenDanger.getReportTypeIndex();
					}
					
					reportFocus.put("reportType", reportType);
					reportFocus.put("riskType", HHDRiskTypeEnum.INIT.getRiskType());
					reportFocus.put("reportId", reportId);
					
					reportFocusService.saveReportFocus(reportFocus, userInfo);
				}
				
				//将历史的“南安房屋安全隐患信息流程_常规_一般”子流程均强制结束
				endAllSubProInstance(instanceId, FocusReportNode3530101Enum.FORM_TYPE_ID.toString(),null);
				
				//将历史的“南安房屋安全隐患信息流程_常规_重大”子流程均强制结束
				endAllSubProInstance(instanceId, FocusReportNode3530102Enum.FORM_TYPE_ID.toString(),null);
			} else if(FocusReportNode35302Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&& FocusReportNode35302Enum.FIND_HHD_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				//将历史的“南安房屋安全隐患信息流程_突发_定期报告”子流程均强制结束
				endAllSubProInstance(instanceId, FocusReportNode3530201Enum.FORM_TYPE_ID.toString(),null);
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean recallWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false, isAble2Recall = false;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(instanceId == null || instanceId < 0) {
			throw new IllegalArgumentException("缺少有效的流程实例id！");
		}
		
		if(CommonFunctions.isNotBlank(extraParam, "isAble2Recall")) {
			isAble2Recall = Boolean.valueOf(extraParam.get("isAble2Recall").toString());
		} else {
			ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
			
			if(proInstance == null) {
				throw new IllegalArgumentException("实例id没有找到有效的实例信息！");
			}
			
			String formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
			
			isAble2Recall = FocusReportNode35301Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					|| FocusReportNode35302Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr);
		}
		
		extraParam.put("isAble2Recall", isAble2Recall);
		
		flag = super.recallWorkflow4Report(instanceId, userInfo, extraParam);
		
		return flag;
	}
	
	/**
	 * 判断是否需要添加子流程信息
	 * @param formTypeIdStr	表单ID
	 * @param taskName		可添加子流程的节点名称
	 * @param listType		列表类型
	 * @param params
	 * @return
	 */
	protected boolean isAble2ShowSubPro(String formTypeIdStr, String taskName, String listType, Map<String, Object> params) {
		String LIST_TYPE_TODO = "2";//待办列表
		
		return CommonFunctions.isNotBlank(params, "TASK_ID") 
				&& (FocusReportNode35301Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) || FocusReportNode35302Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr))
				&& (!LIST_TYPE_TODO.equals(listType) 
						&& (FocusReportNode35301Enum.HHD_COGNIZANCE_NODE_NAME.getTaskNodeName().equals(taskName)
								|| FocusReportNode35302Enum.FIND_HHD_NODE_NAME.getTaskNodeName().equals(taskName)));
	}

	@Override
	public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params) throws Exception {
		List<Map<String, Object>> resultMapList = super.capHandledTaskInfoMap(instanceId, order, params);

		if(resultMapList != null && resultMapList.size() > 0
				&& IWorkflow4BaseService.SQL_ORDER_DESC.equals(order)) {
			Map<String, Object> taskMap = resultMapList.get(0);

			if(CommonFunctions.isBlank(taskMap, "IS_CURRENT_TASK") && CommonFunctions.isBlank(taskMap,"PARENT_PRO_TASK_ID")) {
				Map<String, Object> endTaskMap = new HashMap<String, Object>();

				endTaskMap.put("TASK_NAME", FocusReportNode35301Enum.END_NODE_NAME.getTaskNodeNameZH());

				resultMapList.add(0, endTaskMap);
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
		String reportType = null, workflowName = null, riskType = null;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		
		if(CommonFunctions.isNotBlank(params, "riskType")) {
			riskType = params.get("riskType").toString();
		} else if(reportId != null && reportId > 0) {
			Map<String, Object> reportParams = new HashMap<String, Object>(),
					reportMap = null;
			reportParams.putAll(params);
			reportParams.put("reportId", reportId);
			
			reportMap = reportFocusService.findReportFocusByUUID(null, userInfo, reportParams);
			
			if(CommonFunctions.isNotBlank(reportMap, "riskType")) {
				riskType = reportMap.get("riskType").toString();
			}
		}
		
		//后续转功能配置
		if(ReportTypeEnum.houseHiddenDanger.getReportTypeIndex().equals(reportType)) {
			String formTypeId = null;
			
			if(CommonFunctions.isNotBlank(params, "formTypeId")) {
				formTypeId = params.get("formTypeId").toString();
			}
			
			if(HHDRiskTypeEnum.SUDDEN.getRiskType().equals(riskType)) {
				if(FocusReportNode3530201Enum.FORM_TYPE_ID.toString().equals(formTypeId)) {
					workflowName = "南安房屋安全隐患信息流程_突发_定期报告";
				} else {
					workflowName = "南安房屋安全隐患信息流程_突发";
				}
			} else {
				if(FocusReportNode3530101Enum.FORM_TYPE_ID.toString().equals(formTypeId)) {
					workflowName = "南安房屋安全隐患信息流程_常规_一般";
				} else if(FocusReportNode3530102Enum.FORM_TYPE_ID.toString().equals(formTypeId)) {
					workflowName = "南安房屋安全隐患信息流程_常规_重大";
				} else {
					workflowName = "南安房屋安全隐患信息流程_常规";
				}
			}
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
		if(ReportTypeEnum.houseHiddenDanger.getReportTypeIndex().equals(reportType)) {
			wfTypeId = "focus_report";
		}
		
		return wfTypeId;
	}
	
	protected List<Map<String, Object>> findNextTaskNodes(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		boolean isDecisionMaking = false;
		String curNodeName = null, formTypeIdStr = null;
		ProInstance proInstance = null;
		List<Map<String, Object>> nextTaskNodeMapList = null;

		if(CommonFunctions.isNotBlank(params, "proInstance")) {
			proInstance = (ProInstance) params.get("proInstance");

			if(proInstance != null) {
				formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
				curNodeName = proInstance.getCurNode();
			}
		}

		//当前环节为 task1 时，进入决策类
		isDecisionMaking = (FocusReportNode35301Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) && (FocusReportNode35301Enum.FIND_HHD_NODE_NAME.getTaskNodeName().equals(curNodeName)))
				||(FocusReportNode35302Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) && (FocusReportNode35302Enum.FIND_HHD_NODE_NAME.getTaskNodeName().equals(curNodeName)));

		params.put("proInstance", proInstance);
		params.put("isDecisionMaking", isDecisionMaking);

		nextTaskNodeMapList = super.findNextTaskNodes(instanceId, userInfo, params);

		if(nextTaskNodeMapList != null) {
			
			if(FocusReportNode35301Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&& FocusReportNode35301Enum.VERIFY_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				String nodeName = null,
					   IS_TRUE_ZH = "隐患信息属实",
					   NOT_TRUE_ZH = "隐患信息不属实";
				for(Map<String, Object> nextTaskNodeMap : nextTaskNodeMapList) {
					nodeName = null;
					
					if(CommonFunctions.isNotBlank(nextTaskNodeMap, "nodeName")) {
						nodeName = nextTaskNodeMap.get("nodeName").toString();
					}
					
					if(FocusReportNode35301Enum.HHD_COGNIZANCE_NODE_NAME.getTaskNodeName().equals(nodeName)) {
						nextTaskNodeMap.put("nodeNameZH", IS_TRUE_ZH);
					} else if(FocusReportNode35301Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)) {
						nextTaskNodeMap.put("nodeNameZH", NOT_TRUE_ZH);
					}
				}
			} else if(FocusReportNode3530101Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					|| FocusReportNode3530102Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					|| FocusReportNode3530201Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
				List<Map<String, Object>> removeNodeMapList = new ArrayList<Map<String, Object>>();
				String nodeName = null;
				
				for(Map<String, Object> nextTaskNodeMap : nextTaskNodeMapList) {
					nodeName = null;
					
					if(CommonFunctions.isNotBlank(nextTaskNodeMap, "nodeName")) {
						nodeName = nextTaskNodeMap.get("nodeName").toString();
					}
					
					if(FocusReportNode3530101Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)) {
						removeNodeMapList.add(nextTaskNodeMap);
					}
				}
				
				if(removeNodeMapList.size() > 0) {
					nextTaskNodeMapList.removeAll(removeNodeMapList);
				}
			}
		}
		
		return nextTaskNodeMapList;
	}
	
}
