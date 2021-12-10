package cn.ffcs.zhsq.reportFocus.environmentHealTreatment.service.impl;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportServiceAgent;
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
 * @Description:
 * @Author: zhangtc
 * @Date: 2021/7/26 16:49
 */
@Service(value = "reportFocusWorkflow4EHTService")
public class ReportFocusWorkflow4EHTServiceImpl extends ReportFocusWorkflow4TwoVioPreServiceImpl {

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

    @Autowired
    private IEventDisposalWorkflowService eventDisposalWorkflowService;

    @Autowired
    private HessianFlowService hessianFlowService;

    @Autowired
    private IReportIntegrationService reportIntegrationService;

    @Override
    public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {

        boolean flag = false;
        String curNodeName = null,formTypeIdStr = null,advice = null;
        Map<String, Object> reportFocus = new HashMap<String, Object>();
        Long reportId = null;

        if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
            curNodeName = extraParam.get("curNodeName").toString();
        }
        if(CommonFunctions.isNotBlank(extraParam, "formTypeId")) {
            formTypeIdStr = extraParam.get("formTypeId").toString();
        } else if(CommonFunctions.isNotBlank(extraParam, "proInstance")) {
            ProInstance proInstance = (ProInstance) extraParam.get("proInstance");

            formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
        }
        if(CommonFunctions.isNotBlank(extraParam,"advice")){
            advice = String.valueOf(extraParam.get("advice"));
        }
        try {
            if(CommonFunctions.isNotBlank(extraParam, "formId")) {
                reportId = Long.valueOf(extraParam.get("formId").toString());
            } else if(CommonFunctions.isNotBlank(extraParam, "reportId")) {
                reportId = Long.valueOf(extraParam.get("reportId").toString());
            }
        } catch(NumberFormatException e) {}

        //当前环节为 task2时，下一环节为task3时 所属区域至少要精确到网格层级
        /*if(FocusReportNode360Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
                && FocusReportNode360Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
                && FocusReportNode360Enum.GRID_FEEDBACK_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
            OrgEntityInfoBO orgEntityInfo = null;
            String orgLevel = null;
            Map<String, Object> reportParams = new HashMap<String, Object>();
            reportParams.putAll(extraParam);
            reportParams.put("reportId", reportId);
            reportParams.put("reportType", ReportTypeEnum.povertyPreMonitor.getReportTypeIndex());
            reportFocus = reportIntegrationService.findReportFocusByUUID(null, userInfo, reportParams);

            //当参数regionCode为空 校验地域层级是否是网格层级
            if(CommonFunctions.isBlank(extraParam, "regionCode") && CommonFunctions.isNotBlank(reportFocus, "regionCode")){
                orgEntityInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(reportFocus.get("regionCode").toString());
            }else if(CommonFunctions.isNotBlank(extraParam, "regionCode")){
                //不为空时 判断参数值是否到网格层级
                reportFocus.put("regionCode", extraParam.get("regionCode").toString());
                orgEntityInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(extraParam.get("regionCode").toString());
            }
            if(null != orgEntityInfo){
                orgLevel = orgEntityInfo.getChiefLevel();

                if(StringUtils.isBlank(orgLevel) || orgLevel.compareTo(ConstantValue.GRID_ORG_LEVEL) < 0){
                    throw new ZhsqEventException("所属区域当前层级为【"+ orgLevel +"】，请精确到网格层级！");
                }
            }else{
                throw new ZhsqEventException("所属区域查询失败，请检查！");
            }
        }*/

        //提交
        flag = super.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);

        if(flag) {

            if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
                curNodeName = extraParam.get("curNodeName").toString();
            }

            if(reportId != null && reportId > 0) {

                if(FocusReportNode361Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {}

                if(!reportFocus.isEmpty()) {
                    String reportType = null;

                    if(CommonFunctions.isNotBlank(extraParam, "reportType")) {
                        reportType = extraParam.get("reportType").toString();
                    } else {
                        reportType = ReportTypeEnum.environmentHealTreatment.getReportTypeIndex();
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
            extraParam.put("reportType", ReportTypeEnum.environmentHealTreatment.getReportTypeIndex());
        }

        if(FocusReportNode361Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
            if(FocusReportNode361Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)
                    || FocusReportNode361Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
                isRejectByParent = false;
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
        extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;

        extraParam.put("isAble2Recall", true);

        flag = super.recallWorkflow4Report(instanceId, userInfo, extraParam);

        return flag;
    }

    @Override
    @SuppressWarnings({ "unchecked", "serial" })
    public Map<String, Object> initWorkflow4Handle(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {

        Map<String, Object> initMap = super.initWorkflow4Handle(instanceId, userInfo, params);
        String curNodeName = null, formTypeIdStr = null,dataSource = "01";
        boolean isEditableNode = false,
                isAble2Handle = false,	//是否可办理案件
                isChooseDefaultRadio = true,//是否默认选中办理环节
                userSelectedLimit=true;//人员选择是否限制选择人数

        if(CommonFunctions.isBlank(params, "reportType")) {
            initMap.put("reportType", ReportTypeEnum.environmentHealTreatment.getReportTypeIndex());
        }

        if(CommonFunctions.isNotBlank(initMap, "curNodeName")) {
            curNodeName = initMap.get("curNodeName").toString();
        }

        if(CommonFunctions.isNotBlank(initMap, "formTypeId")) {
            formTypeIdStr = initMap.get("formTypeId").toString();
        }

        if(CommonFunctions.isNotBlank(params,"reportFocusMap")){//dataSource
            Map<String,Object> reportFocusMap = (Map<String, Object>) params.get("reportFocusMap");
            if(CommonFunctions.isNotBlank(reportFocusMap,"dataSource")){
                dataSource = String.valueOf(reportFocusMap.get("dataSource"));
            }
        }

        if(CommonFunctions.isNotBlank(initMap, "isChooseDefaultRadio")) {
            isChooseDefaultRadio = Boolean.valueOf(String.valueOf(initMap.get("isChooseDefaultRadio")));
        }
        if(CommonFunctions.isNotBlank(initMap, "isAble2Handle")) {
            isAble2Handle = Boolean.valueOf(String.valueOf(initMap.get("isAble2Handle")));
        }

        if(isAble2Handle && FocusReportNode361Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)){
            List<Map<String, Object>> nextNodeMapList = null;
            String nodeName = null;

            if(CommonFunctions.isNotBlank(initMap, "nextTaskNodes")) {
                nextNodeMapList = (List<Map<String, Object>>) initMap.get("nextTaskNodes");
            }

            if(FocusReportNode361Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                List<Map<String, Object>> nextNodeMapListRemain = new ArrayList<>();
                Map<String,Object> taskMap = null;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (int i=0;i<nextNodeMapList.size();i++){
                        taskMap = nextNodeMapList.get(i);

                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }

                        //数据来源为 网格员发现上报01 下一环节保留：一级网格处置 task2；归档end1
                        if(EHTDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)){
                            if(FocusReportNode361Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                nextNodeMapListRemain.add(taskMap);
                            }
                            if(FocusReportNode361Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                taskMap.put("nodeNameZH","归档(现场完成处置)");
                                nextNodeMapListRemain.add(taskMap);
                            }
                        }else if(EHTDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource)
                                || EHTDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(dataSource)
                                || EHTDataSourceEnum.MASSES_REPORT.getDataSource().equals(dataSource)){
                            //数据来源为 02 03 04 的 下一环节只有：任务派发 task3 环节；
                            if(FocusReportNode361Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                nextNodeMapListRemain.add(taskMap);
                            }
                        }
                    }
                }
                nextNodeMapList = nextNodeMapListRemain;
                if(nextNodeMapList != null && nextNodeMapList.size() == 1){
                    isChooseDefaultRadio = true;
                }
                initMap.put("nextTaskNodes",nextNodeMapList);
            }else if(FocusReportNode361Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                isChooseDefaultRadio = false;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (Map<String, Object> taskMap:nextNodeMapList){
                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }
                        if(FocusReportNode361Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","镇级处置(提请镇级处置)");
                        }else if(FocusReportNode361Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","归档(不属实/整改到位)");
                        }
                    }
                }
            }else if(FocusReportNode361Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                isChooseDefaultRadio = false;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (Map<String, Object> taskMap:nextNodeMapList){
                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }
                        if(FocusReportNode361Enum.ADMINISTRATION_BUREAU_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","城市管理局处置(提请市城管局处置)");
                        }else if(FocusReportNode361Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","归档(整改到位)");
                        }
                    }
                }
            }else if(FocusReportNode361Enum.ADMINISTRATION_BUREAU_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                isChooseDefaultRadio = false;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (Map<String, Object> taskMap:nextNodeMapList){
                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }
                        if(FocusReportNode361Enum.RELEVANT_UNIT_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","市直单位处置(提请市直单位处置)");
                        }else if(FocusReportNode361Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","归档(整改到位)");
                        }
                    }
                }
            }else if(FocusReportNode361Enum.RELEVANT_UNIT_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                isChooseDefaultRadio = true;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (Map<String, Object> taskMap:nextNodeMapList){
                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }
                        if(FocusReportNode361Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","归档(整改到位)");
                        }
                    }
                }
            }
        }

        initMap.put("isEditableNode", isEditableNode);
        initMap.put("isChooseDefaultRadio", isChooseDefaultRadio);
        initMap.put("userSelectedLimit", userSelectedLimit);

        return initMap;
    }

    @Override
    public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params) throws Exception {

        List<Map<String, Object>> resultMapList = super.capHandledTaskInfoMap(instanceId, order, params);

        return resultMapList;
    }

    /**
     * 获取流程图名称
     * @param reportId	上报id
     * @param userInfo	操作用户
     * @param params	额外参数
     * @return
     */
    @Override
    protected String findWorkflowName(Long reportId, UserInfo userInfo, Map<String, Object> params) {

        String reportType = null, workflowName = null, formTypeIdStr = null;

        if(CommonFunctions.isNotBlank(params, "reportType")) {
            reportType = params.get("reportType").toString();
        } else {
            throw new IllegalArgumentException("缺少上报类型【reportType】！");
        }
        if(CommonFunctions.isNotBlank(params, "formTypeId")) {
            formTypeIdStr = params.get("formTypeId").toString();
        }

        //后续转功能配置
        if(ReportTypeEnum.environmentHealTreatment.getReportTypeIndex().equals(reportType)) {
            workflowName = "环境卫生问题处置流程";
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
    @Override
    protected String findWfTypeId(Long reportId, UserInfo userInfo, Map<String, Object> params) {
        String reportType = null, wfTypeId = null;

        if(CommonFunctions.isNotBlank(params, "reportType")) {
            reportType = params.get("reportType").toString();
        } else {
            throw new IllegalArgumentException("缺少上报类型【reportType】！");
        }

        //后续转功能配置
        if(ReportTypeEnum.environmentHealTreatment.getReportTypeIndex().equals(reportType)) {
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
    @Override
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

        //进入决策类判断（致贫返贫监测暂时未启用决策）
        isDecisionMaking = FocusReportNode361Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) && false;

        params.put("proInstance", proInstance);
        params.put("isDecisionMaking", isDecisionMaking);

        nodeMapList = super.findNextTaskNodes(instanceId, userInfo, params);

        return nodeMapList;
    }

    /**
     * 更新上报状态
     * @param reportId
     * @param instanceId
     * @param userInfo
     * @param params
     * @throws Exception
     */
    private void updateReportStatus(Long reportId, Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
        String reportType = null;
        ReportServiceAgent<IWorkflowDecisionMakingService<String>> reportServiceAgent = null;
        IWorkflowDecisionMakingService<String> reportStatusDecisionMakingService = null;

        params = params == null ? new HashMap<String, Object>() : params;

        if(CommonFunctions.isBlank(params, "reportId") && reportId != null && reportId > 0) {
            params.put("reportId", reportId);
        }
        if(CommonFunctions.isBlank(params, "instanceId") && instanceId != null && instanceId > 0) {
            params.put("instanceId", instanceId);
        }
        if(CommonFunctions.isNotBlank(params, "reportType")) {
            reportType = params.get("reportType").toString();
        }
        params.put("operateUserInfo", userInfo);

        reportServiceAgent = new ReportServiceAgent<IWorkflowDecisionMakingService<String>>(reportType, ReportServiceAgent.serviceTypeEnum.reportStatusDecison.getServiceTypeIndex());

        reportStatusDecisionMakingService = reportServiceAgent.capService();

        reportStatusDecisionMakingService.makeDecision(params);
    }
}
