package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportFocusWorkflow4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 疫情防控信息工作流相关接口
 * @ClassName:   ReportFocusWorkflow4EPCServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年11月11日 下午2:23:48
 */
@Service(value = "reportFocusWorkflow4EPCService")
public class ReportFocusWorkflow4EPCServiceImpl extends ReportFocusWorkflow4TwoVioPreServiceImpl {

	@Autowired
	private IReportFocusService reportFocusService;
	
	@Autowired
	private IWorkflow4BaseService workflow4BaseService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Override
	public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {

		boolean flag = false;
		String curNodeName = null;
		Map<String, Object> reportFocus = new HashMap<String, Object>();
		String formTypeIdStr = null;

		if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
			curNodeName = extraParam.get("curNodeName").toString();
		}
		if(CommonFunctions.isNotBlank(extraParam, "formTypeId")) {
			formTypeIdStr = extraParam.get("formTypeId").toString();
		} else if(CommonFunctions.isNotBlank(extraParam, "proInstance")) {
			ProInstance proInstance = (ProInstance) extraParam.get("proInstance");

			formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
		}

		//当前环节为 task13 task14 task15时，下一环节为task2时 所属区域至少要精确到村社区层级
		if(FocusReportNode352Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
				&& FocusReportNode352Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nextNodeName)
				&&	(FocusReportNode352Enum.FOREIGN_AFFAIRS_GROUP_NODE_NAME.getTaskNodeName().equals(curNodeName)
				|| FocusReportNode352Enum.EPC_GROUP_NODE_NAME.getTaskNodeName().equals(curNodeName)
				|| FocusReportNode352Enum.BIG_DATA_GROUP_NODE_NAME.getTaskNodeName().equals(curNodeName)
				|| FocusReportNode352Enum.NOT_THE_GRID_NODE_NAME.getTaskNodeName().equals(curNodeName)
				|| FocusReportNode352Enum.NOT_THE_STREET_NODE_NAME.getTaskNodeName().equals(curNodeName))
		){
			OrgEntityInfoBO orgEntityInfo = null;
			String orgLevel = null;


			if(CommonFunctions.isNotBlank(extraParam,"nextOrgIds")){
				OrgSocialInfoBO nextOrgInfo = orgSocialInfoService.findByOrgId(Long.valueOf(String.valueOf(extraParam.get("nextOrgIds"))));

				if(null != nextOrgInfo){
					orgEntityInfo = orgEntityInfoService.findByOrgId(nextOrgInfo.getLocationOrgId());

					if(null != orgEntityInfo){
						orgLevel = orgEntityInfo.getChiefLevel();
						reportFocus.put("regionCode", orgEntityInfo.getOrgCode());
						extraParam.put("regionCode", orgEntityInfo.getOrgCode());
					}
				}else{
					throw new ZhsqEventException("下一环节办理人所属组织对应的地域信息为空，请检查！");
				}
			}else if(CommonFunctions.isNotBlank(extraParam, "regionCode")){
				reportFocus.put("regionCode", extraParam.get("regionCode").toString());
				orgEntityInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(extraParam.get("regionCode").toString());
			}

			if(null != orgEntityInfo){
				orgLevel = orgEntityInfo.getChiefLevel();

				if(StringUtils.isBlank(orgLevel) || orgLevel.compareTo(ConstantValue.STREET_ORG_LEVEL) <= 0){
					throw new ZhsqEventException("所属区域当前层级为【"+ orgLevel +"】，至少要精确到村社区层级！");
				}
			}else{
				throw new ZhsqEventException("所属区域查询失败，请检查！");
			}
		}


		flag = super.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);

		if(flag) {
			Long reportId = null;

			if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
				curNodeName = extraParam.get("curNodeName").toString();
			}
			
			try {
				if(CommonFunctions.isNotBlank(extraParam, "formId")) {
					reportId = Long.valueOf(extraParam.get("formId").toString());
				} else if(CommonFunctions.isNotBlank(extraParam, "reportId")) {
					reportId = Long.valueOf(extraParam.get("reportId").toString());
				}
			} catch(NumberFormatException e) {}
			
			if(reportId != null && reportId > 0) {

				if(FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
					if(FocusReportNode352Enum.VERIFY_TASK_OTHER_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
						if(CommonFunctions.isNotBlank(extraParam, "verifyStatus")) {
							reportFocus.put("verifyStatus", extraParam.get("verifyStatus"));
						}
					} else if(FocusReportNode352Enum.VERIFY_TASK_GRID_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
						String verifyStatus = null;
						
						if(FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
							verifyStatus = "1";//属实
						} else if(FocusReportNode352Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
							verifyStatus = "2";//不属实
						}
						
						if(StringUtils.isNotBlank(verifyStatus)) {
							reportFocus.put("verifyStatus", verifyStatus);
						}
					} else if(
							(FocusReportNode352Enum.NOT_THE_GRID_NODE_NAME.getTaskNodeName().equals(curNodeName)
									|| FocusReportNode352Enum.NOT_THE_STREET_NODE_NAME.getTaskNodeName().equals(curNodeName))
							&& FocusReportNode352Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nextNodeName) 
							&& (CommonFunctions.isNotBlank(extraParam, "occurred") 
									|| CommonFunctions.isNotBlank(extraParam, "regionCode"))) {
						reportFocus.put("reportId", reportId);
						
						if(CommonFunctions.isNotBlank(extraParam, "occurred") ) {
							reportFocus.put("occurred", extraParam.get("occurred").toString());
						}
						
						if(CommonFunctions.isNotBlank(extraParam, "regionCode")) {
							reportFocus.put("regionCode", extraParam.get("regionCode").toString());
						}
					} else if(FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
						String disposalMode = null;
						
						if(FocusReportNode352Enum.VICE_COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
							//需居家隔离的重点管控人员
							disposalMode = EPCDisposalModeEnum.HOME_QUARANTINE.getDisposalMode();
						} else if(FocusReportNode352Enum.CENTRALIZED_ISOLATION_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
							//需集中隔离的重点管控人员
							disposalMode = EPCDisposalModeEnum.CENTRALIZED_ISOLATION.getDisposalMode();
						} else if(FocusReportNode352Enum.NUCLEIC_ACID_DETECTION_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
							//只需核酸检测的重点管控人员
							disposalMode = EPCDisposalModeEnum.NUCLEIC_ACID_DETECTION.getDisposalMode();
						}
						
						if(StringUtils.isNotBlank(disposalMode)) {
							reportFocus.put("disposalMode", disposalMode);
						}
					}
				} else if(FocusReportNode35201Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
					if(FocusReportNode35201Enum.DISTRICT_HOSPITAL_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
						String kpcSituation = null;
						
						if(FocusReportNode35201Enum.DISTRICT_NAD_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
							kpcSituation = EPCKpcSituationEnum.SUSPECT.getKpcSituation();
						} else if(FocusReportNode35201Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
							kpcSituation = EPCKpcSituationEnum.EXCULDE.getKpcSituation();
						}
						
						if(StringUtils.isNotBlank(kpcSituation)) {
							if(CommonFunctions.isNotBlank(extraParam, "collectSource")) {
								reportFocus.put("collectSource", extraParam.get("collectSource"));
							} else {
								Map<String, Object> reportFocusMap = null,
													reportFocusParam = new HashMap<String, Object>();
								
								reportFocusParam.put("reportId", reportId);
								
								if(CommonFunctions.isNotBlank(extraParam, "reportType")) {
									reportFocusParam.put("reportType", extraParam.get("reportType"));
								}
								
								reportFocusMap = reportFocusService.findReportFocusByUUID(null, userInfo, reportFocusParam);
								
								if(CommonFunctions.isNotBlank(reportFocusMap, "collectSource")) {
									reportFocus.put("collectSource", reportFocusMap.get("collectSource"));
								}
							}
							
							reportFocus.put("kpcSituation", kpcSituation);
						}
					}
				}
				
				if(!reportFocus.isEmpty()) {
					String reportType = null;
					
					if(CommonFunctions.isNotBlank(extraParam, "reportType")) {
						reportType = extraParam.get("reportType").toString();
					} else {
						reportType = ReportTypeEnum.epidemicPreControl.getReportTypeIndex();
					}
					
					reportFocus.put("reportType", reportType);
					reportFocus.put("reportId", reportId);
					
					reportFocusService.saveReportFocus(reportFocus, userInfo);
				}
			}
		}
		
		if(flag && FocusReportNode352Enum.NOT_VERIFIED_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
			UserInfo nextUserInfo = new UserInfo();
			Map<String, Object> archivedParam = new HashMap<String, Object>();
			
			archivedParam.putAll(extraParam);
			
			if(CommonFunctions.isNotBlank(archivedParam, "nextUserIds")) {
				Long userId = Long.valueOf(archivedParam.get("nextUserIds").toString().split(",")[0]);
				UserBO userBO = userManageService.getUserInfoByUserId(userId);
				
				if(userBO != null && userBO.getUserId() != null) {
					nextUserInfo.setUserId(userBO.getUserId());
					nextUserInfo.setPartyName(userBO.getPartyName());
				}
			}
			
			if(CommonFunctions.isNotBlank(archivedParam, "nextOrgIds")) {
				Long orgId = Long.valueOf(archivedParam.get("nextOrgIds").toString().split(",")[0]);
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
				
				if(orgInfo != null && orgInfo.getOrgId() != null) {
					nextUserInfo.setOrgId(orgId);
					nextUserInfo.setOrgCode(orgInfo.getOrgCode());
					nextUserInfo.setOrgName(orgInfo.getOrgName());
				}
			}
			
			extraParam.remove("nextUserIds");
			extraParam.remove("nextOrgIds");
			archivedParam.remove("advice");
			
			this.subWorkflow4Report(instanceId, FocusReportNode352Enum.END_NODE_NAME.getTaskNodeName(), null, nextUserInfo, archivedParam);
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
		if(CommonFunctions.isBlank(extraParam, "reportType")) {
			extraParam.put("reportType", ReportTypeEnum.epidemicPreControl.getReportTypeIndex());
		}
		
		if(FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode352Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isRejectByParent = false;
				isBack2Draft = true;
			} else if(FocusReportNode352Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				isRejectByParent = true;
				isBack2Draft = true;
			}
		}
		
		extraParam.put("isRejectByParent", isRejectByParent);
		extraParam.put("isBack2Draft", isBack2Draft);
		
		flag = super.rejectWorkflow4Report(instanceId, userInfo, extraParam);
		
		if(flag) {
			Map<String, Object> curDataMap = workflow4BaseService.findCurTaskData4Base(instanceId);
			
			if(CommonFunctions.isNotBlank(curDataMap, "NODE_NAME")) {
				curNodeName = curDataMap.get("NODE_NAME").toString();
			}
			
			if(FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					&& FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				Long reportId = null;
				Map<String, Object> reportFocus = new HashMap<String, Object>();
				
				if(CommonFunctions.isNotBlank(extraParam, "formId")) {
					try {
						reportId = Long.valueOf(extraParam.get("formId").toString());
					} catch(NumberFormatException e) {}
				}
				
				if(reportId != null && reportId > 0) {
					String reportType = ReportTypeEnum.epidemicPreControl.getReportTypeIndex();
					
					if(CommonFunctions.isNotBlank(extraParam, "reportType")) {
						reportType = extraParam.get("reportType").toString();
					}
					
					reportFocus.put("reportType", reportType);
					reportFocus.put("reportId", reportId);
					reportFocus.put("disposalMode", EPCDisposalModeEnum.INIT_MODE.getDisposalMode());
					
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
	
	@SuppressWarnings({ "unchecked", "serial" })
	@Override
	public Map<String, Object> initWorkflow4Handle(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		Map<String, Object> initMap = super.initWorkflow4Handle(instanceId, userInfo, params);
		String curNodeName = null, formTypeIdStr = null;
		boolean isEditableNode = false;
		
		if(CommonFunctions.isBlank(params, "reportType")) {
			initMap.put("reportType", ReportTypeEnum.epidemicPreControl.getReportTypeIndex());
		}
		
		if(CommonFunctions.isNotBlank(initMap, "curNodeName")) {
			curNodeName = initMap.get("curNodeName").toString();
		}
		
		if(CommonFunctions.isNotBlank(initMap, "formTypeId")) {
			formTypeIdStr = initMap.get("formTypeId").toString();
		}
		
		if(FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& (FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)
				|| FocusReportNode352Enum.NOT_THE_GRID_NODE_NAME.getTaskNodeName().equals(curNodeName)
				|| FocusReportNode352Enum.NOT_THE_STREET_NODE_NAME.getTaskNodeName().equals(curNodeName))) {
			List<Map<String, Object>> nextNodeMapList = null;
			
			if(CommonFunctions.isNotBlank(initMap, "nextTaskNodes")) {
				nextNodeMapList = (List<Map<String, Object>>) initMap.get("nextTaskNodes");
			}
			
			if(nextNodeMapList != null) {
				String nodeName = null, nodeNameZH = null, kpcType = null, verifyStatus = null;

				//环节名称变更映射
				Map<String, String> NODE_MAP = new HashMap<String, String>() {
					{
						put(FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode352Enum.NOT_VERIFIED_NODE_NAME.getTaskNodeName(), EPCDisposalModeEnum.KEY_CONTROL_PERSON_UNTRUE.getDisposalModeName());
						put(FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode352Enum.NOT_THE_GRID_NODE_NAME.getTaskNodeName(), EPCDisposalModeEnum.KEY_CONTROL_PERSON_NOT_GRID.getDisposalModeName());
						put(FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode352Enum.VICE_COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName(), EPCDisposalModeEnum.HOME_QUARANTINE.getDisposalModeName());
						put(FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode352Enum.CENTRALIZED_ISOLATION_NODE_NAME.getTaskNodeName(), EPCDisposalModeEnum.CENTRALIZED_ISOLATION.getDisposalModeName());
						put(FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode352Enum.NUCLEIC_ACID_DETECTION_NODE_NAME.getTaskNodeName(), EPCDisposalModeEnum.NUCLEIC_ACID_DETECTION.getDisposalModeName());
						
						put(FocusReportNode352Enum.NOT_THE_GRID_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode352Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName(), EPCDisposalModeEnum.BELONG_STREET.getDisposalModeName());
						put(FocusReportNode352Enum.NOT_THE_GRID_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode352Enum.NOT_THE_STREET_NODE_NAME.getTaskNodeName(), EPCDisposalModeEnum.NOT_STREET.getDisposalModeName());
						
						put(FocusReportNode352Enum.NOT_THE_STREET_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode352Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName(), EPCDisposalModeEnum.BELONG_CITY.getDisposalModeName());
						put(FocusReportNode352Enum.NOT_THE_STREET_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode352Enum.END_NODE_NAME.getTaskNodeName(), EPCDisposalModeEnum.NOT_CITY.getDisposalModeName());
						//add by ztc 5860011:疫情防控场景应用 不属实和无法核实流程优化
						put(FocusReportNode352Enum.NOT_THE_STREET_NODE_NAME.getTaskNodeName() + "-" + FocusReportNode352Enum.NOT_VERIFIED_NODE_NAME.getTaskNodeName(), EPCDisposalModeEnum.EXCLUDE_CONTROL.getDisposalModeName());
					}
				};

				String trueInterval = "-" + EPCVerifyStatusEnum.IS_TRUE.getVerifyStatus() + "-";

				//下一可办理环节（根据下一环节+核实状态+管控类型 映射）集合：所有可能情况穷举
				Set<String> REMAIN_NODE_SET = new HashSet<String>() {
					{
						//重点管控人员无法核实
						//add(FocusReportNode352Enum.NOT_VERIFIED_NODE_NAME.getTaskNodeName() + "-" + EPCVerifyStatusEnum.UN_TRUE.getVerifyStatus());
						//add by ztc 5860011:疫情防控场景应用 不属实和无法核实流程优化
						//原管控人员无法核实的 直接归档（乡镇(街道)疫情防控指挥部处置）task12 现改为 无法核实的 到乡镇(街道)疫情防控指挥部处置 task6
						add(FocusReportNode352Enum.NOT_THE_GRID_NODE_NAME.getTaskNodeName() + "-" + EPCVerifyStatusEnum.UN_TRUE.getVerifyStatus());

						//重点管控人员不在本网格 不属实
						add(FocusReportNode352Enum.NOT_THE_GRID_NODE_NAME.getTaskNodeName() + "-" + EPCVerifyStatusEnum.NOT_TRUE.getVerifyStatus());
						
						//需居家隔离的重点管控人员
						add(FocusReportNode352Enum.VICE_COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName() + trueInterval + EPCKpcTypeEnum.IMMIGRATION_PERSONNEL.getKpcType());
						add(FocusReportNode352Enum.VICE_COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName() + trueInterval + EPCKpcTypeEnum.KEY_AREA_RETURN.getKpcType());
						add(FocusReportNode352Enum.VICE_COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName() + trueInterval + EPCKpcTypeEnum.CLOSE_CONTACTS.getKpcType());
						add(FocusReportNode352Enum.VICE_COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName() + trueInterval + EPCKpcTypeEnum.NOT_ISOLATED_HOME.getKpcType());
						add(FocusReportNode352Enum.VICE_COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName() + trueInterval + EPCKpcTypeEnum.SECONDARY_ASYMPTOMATIC.getKpcType());
						
						//需集中隔离的重点管控人员
						add(FocusReportNode352Enum.CENTRALIZED_ISOLATION_NODE_NAME.getTaskNodeName() + trueInterval + EPCKpcTypeEnum.IMMIGRATION_PERSONNEL.getKpcType());
						add(FocusReportNode352Enum.CENTRALIZED_ISOLATION_NODE_NAME.getTaskNodeName() + trueInterval + EPCKpcTypeEnum.KEY_AREA_RETURN.getKpcType());
						add(FocusReportNode352Enum.CENTRALIZED_ISOLATION_NODE_NAME.getTaskNodeName() + trueInterval + EPCKpcTypeEnum.CLOSE_CONTACTS.getKpcType());
						add(FocusReportNode352Enum.CENTRALIZED_ISOLATION_NODE_NAME.getTaskNodeName() + trueInterval + EPCKpcTypeEnum.NOT_ISOLATED_HOME.getKpcType());
						
						//只需核酸加测的重点管控人员(20210914 名称变更 只需核酸加测的重点管控人员--> 需居家健康监测并核酸检测的重点管控人员)
						add(FocusReportNode352Enum.NUCLEIC_ACID_DETECTION_NODE_NAME.getTaskNodeName() + trueInterval + EPCKpcTypeEnum.IMMIGRATION_PERSONNEL.getKpcType());
						add(FocusReportNode352Enum.NUCLEIC_ACID_DETECTION_NODE_NAME.getTaskNodeName() + trueInterval + EPCKpcTypeEnum.KEY_AREA_RETURN.getKpcType());
						add(FocusReportNode352Enum.NUCLEIC_ACID_DETECTION_NODE_NAME.getTaskNodeName() + trueInterval + EPCKpcTypeEnum.CLOSE_CONTACTS.getKpcType());
						add(FocusReportNode352Enum.NUCLEIC_ACID_DETECTION_NODE_NAME.getTaskNodeName() + trueInterval + EPCKpcTypeEnum.OUTSIDE_PROVINCE.getKpcType());
						add(FocusReportNode352Enum.NUCLEIC_ACID_DETECTION_NODE_NAME.getTaskNodeName() + trueInterval + EPCKpcTypeEnum.OTHER_KEY_TYPE.getKpcType());
					}
				};
				
				if(FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)
						&& CommonFunctions.isNotBlank(params, "reportFocusMap")) {
					Object reportFocusMapObj = params.get("reportFocusMap");
					
					if(reportFocusMapObj instanceof Map) {
						Map<String, Object> reportFocusMap = (Map<String, Object>) params.get("reportFocusMap");
						
						if(CommonFunctions.isNotBlank(reportFocusMap, "kpcType")) {
							kpcType = reportFocusMap.get("kpcType").toString();
						}
						
						if(CommonFunctions.isNotBlank(reportFocusMap, "verifyStatus")) {
							verifyStatus = reportFocusMap.get("verifyStatus").toString();
						}
					}
				}
				
				List<Map<String, Object>> remainNodeMapList = new ArrayList<Map<String, Object>>();
				
				for(Map<String, Object> nextNodeMap : nextNodeMapList) {
					if(CommonFunctions.isNotBlank(nextNodeMap, "nodeName")) {
						nodeName = nextNodeMap.get("nodeName").toString();
					}
					
					nodeNameZH = NODE_MAP.get(curNodeName + "-" + nodeName);
					
					if(StringUtils.isNotBlank(nodeNameZH)) {
						nextNodeMap.put("nodeNameZH", nodeNameZH);
					}
					
					if(REMAIN_NODE_SET.contains(nodeName + "-" + verifyStatus) || REMAIN_NODE_SET.contains(nodeName + "-" + verifyStatus + "-" + kpcType)) {
						remainNodeMapList.add(nextNodeMap);
					}
				}
				
				if(StringUtils.isNotBlank(kpcType)) {
					initMap.put("nextTaskNodes", remainNodeMapList);
					//只有当下一可办理环节为一个时，默认选中
					initMap.put("isChooseDefaultRadio", remainNodeMapList != null && remainNodeMapList.size() == 1);
				}
			}
		}
		//当前环节为 网格长(驻村工作队长)核实 节点时，疫情防控信息可编辑
		if(CommonFunctions.isNotBlank(initMap, "isAble2Handle")
				&& Boolean.valueOf(initMap.get("isAble2Handle").toString())
				&& FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
				&& (FocusReportNode352Enum.VERIFY_TASK_GRID_NODE_NAME.getTaskNodeName().equals(curNodeName)
						|| FocusReportNode352Enum.VERIFY_TASK_OTHER_NODE_NAME.getTaskNodeName().equals(curNodeName))) {
			isEditableNode = true;
			
			initMap.put("isEditable", true);
		}
		
		initMap.put("isEditableNode", isEditableNode);
		
		return initMap;
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
				
				if(!FocusReportNode352Enum.END_NODE_NAME.getTaskNodeNameZH().equals(taskNameZH)) {
					Map<String, Object> endTaskMap = new HashMap<String, Object>();
					
					endTaskMap.put("TASK_NAME", FocusReportNode352Enum.END_NODE_NAME.getTaskNodeNameZH());
					
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
		String reportType = null, workflowName = null, formTypeIdStr = null,
			   collectSource = null;
		
		if(CommonFunctions.isNotBlank(params, "reportType")) {
			reportType = params.get("reportType").toString();
		} else {
			throw new IllegalArgumentException("缺少上报类型【reportType】！");
		}
		
		if(CommonFunctions.isNotBlank(params, "collectSource")) {
			collectSource = params.get("collectSource").toString();
		}
		if(CommonFunctions.isNotBlank(params, "formTypeId")) {
			formTypeIdStr = params.get("formTypeId").toString();
		}
		
		//后续转功能配置
		if(ReportTypeEnum.epidemicPreControl.getReportTypeIndex().equals(reportType)) {
			workflowName = "南安疫情防控信息流程";
			
			if(FocusReportNode35201Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
					|| EPCCollectSourceEnum.SMALL_MEDICAL_INSTITUTION.getCollectSource().equals(collectSource)) {
				workflowName = "南安疫情防控信息流程_小型医疗机构";
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
		if(ReportTypeEnum.epidemicPreControl.getReportTypeIndex().equals(reportType)) {
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

		//当前环节为task1 task2时，进入决策类
		isDecisionMaking = FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) 
				&& (FocusReportNode352Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
					 || FocusReportNode352Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName));
		
		params.put("proInstance", proInstance);
		params.put("isDecisionMaking", isDecisionMaking);
		
		nodeMapList = super.findNextTaskNodes(instanceId, userInfo, params);
		
		if(FocusReportNode352Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode352Enum.VERIFY_TASK_GRID_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				if(nodeMapList != null) {
					String nextNodeName = null,
						   BELONG_TO_ZH = "信息属实",
						   NOT_BELONG_TO_ZH = "信息不属实";
					
					for(Map<String, Object> nextNodeMap : nodeMapList) {
						nextNodeName = null;
						
						if(CommonFunctions.isNotBlank(nextNodeMap, "nodeName")) {
							nextNodeName = nextNodeMap.get("nodeName").toString();
						}
						
						if(FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
							nextNodeMap.put("nodeNameZH", BELONG_TO_ZH);
						} else if(FocusReportNode352Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
							nextNodeMap.put("nodeNameZH", NOT_BELONG_TO_ZH);
						}
					}
				}
			} else if(FocusReportNode352Enum.VICE_COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				Long reportId = proInstance.getFormId();
				String regionCode = null, regionChiefLevel = null;
				
				if(reportId != null && reportId > 0) {
					Map<String, Object> reportParam = new HashMap<String, Object>(),
										reportMap = null;
					
					reportParam.put("reportId", reportId);
					
					reportMap = reportFocusService.findReportFocusByUUIDSimple(null, userInfo, reportParam);
					
					if(CommonFunctions.isNotBlank(reportMap, "regionCode")) {
						regionCode = reportMap.get("regionCode").toString();
					}
				}
				
				if(StringUtils.isNotBlank(regionCode)) {
					OrgEntityInfoBO regionInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(regionCode);
					if(regionInfo != null) {
						regionChiefLevel = regionInfo.getChiefLevel();
					}
				}
				
				if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(regionChiefLevel)) {
					if(nodeMapList != null) {
						String nextNodeName = null;
						List<Map<String, Object>> removeNodeMapList = new ArrayList<Map<String, Object>>();
						
						for(Map<String, Object> nextNodeMap : nodeMapList) {
							nextNodeName = null;
							
							if(CommonFunctions.isNotBlank(nextNodeMap, "nodeName")) {
								nextNodeName = nextNodeMap.get("nodeName").toString();
							}
							
							if(FocusReportNode352Enum.GRID_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
								removeNodeMapList.add(nextNodeMap);
							}
						}
						
						if(removeNodeMapList.size() > 0) {
							nodeMapList.removeAll(removeNodeMapList);
						}
					}
				}
			}
		} else if(FocusReportNode35201Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode35201Enum.DISTRICT_HOSPITAL_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
				if(nodeMapList != null) {
					String nextNodeName = null,
						   SUSPECT_ZH = "怀疑新冠肺炎",
						   NOT_SUSPECT_ZH = "排除新冠肺炎";
					
					for(Map<String, Object> nextNodeMap : nodeMapList) {
						nextNodeName = null;
						
						if(CommonFunctions.isNotBlank(nextNodeMap, "nodeName")) {
							nextNodeName = nextNodeMap.get("nodeName").toString();
						}
						
						if(FocusReportNode35201Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
							nextNodeMap.put("nodeNameZH", NOT_SUSPECT_ZH);
						} else if(FocusReportNode35201Enum.DISTRICT_NAD_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
							nextNodeMap.put("nodeNameZH", SUSPECT_ZH);
						}
					}
				}
			}
		}
		
		return nodeMapList;
	}
	
}
