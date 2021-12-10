package cn.ffcs.zhsq.reportFocus.povertyPreMonitor.service.impl;

import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
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
import cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl.FocusReportNode352Enum;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportServiceAgent;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportFocusWorkflow4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 致贫返贫信息工作流相关接口
 * @Author: zhangtc
 * @Date: 2021/6/1 15:45
 */
@Service(value = "reportFocusWorkflow4PPMService")
public class ReportFocusWorkflow4PPMServiceImpl extends ReportFocusWorkflow4TwoVioPreServiceImpl {

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
        if(FocusReportNode360Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
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
                    //外部传参均不能精确到网格层级时 使用下一环节办理人对应的组织信息获取地域信息
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
                    }
                }
                if(StringUtils.isBlank(orgLevel) || orgLevel.compareTo(ConstantValue.GRID_ORG_LEVEL) < 0){
                    throw new ZhsqEventException("所属区域当前层级为【"+ orgLevel +"】，请精确到网格层级！");
                }
            }else{
                throw new ZhsqEventException("所属区域查询失败，请检查！");
            }
        }
        //下一环节为 筛查反馈时 校验该节点配置的指定组织是否全部选择且仅选了一个人
        if(FocusReportNode360Enum.MUNICIPAL_SEND_NODE_NAME.getTaskNodeName().equals(curNodeName)
                && FocusReportNode360Enum.SCREENING_FEEDBACK_NODE_NAME.getTaskNodeName().equals(nextNodeName) ){
            Integer nodeId = null;
            List<Map<String, Object>> nodeActorsMap = new ArrayList<>();

            if(CommonFunctions.isNotBlank(extraParam,"nodeId")){
                nodeId = Integer.valueOf(String.valueOf(extraParam.get("nodeId")));
            }
            if(null != nodeId /*&& nodeId > 0*/){
                nodeActorsMap = eventDisposalWorkflowService.findNodeActorsById(nodeId);

                if(nodeActorsMap != null && nodeActorsMap.size() > 0) {
                    List<String> nextOrgIds = new ArrayList<>();
                    if(CommonFunctions.isNotBlank(extraParam,"nextOrgIds")){
                        nextOrgIds = Arrays.asList(String.valueOf(extraParam.get("nextOrgIds")).split(","));
                    }

                    if(nodeActorsMap.size() == nextOrgIds.size()){
                        String orgId = null;
                        String orgName = null;
                        StringBuffer msg = new StringBuffer();
                        Boolean contains = false;

                        for(Map<String, Object> nodeActor : nodeActorsMap){
                            contains = false;

                            if(CommonFunctions.isNotBlank(nodeActor,"ORG_ID")){
                                orgId = String.valueOf(nodeActor.get("ORG_ID"));
                            }
                            if(CommonFunctions.isNotBlank(nodeActor,"ACTOR_NAME")){
                                orgName = String.valueOf(nodeActor.get("ACTOR_NAME"));
                            }
                            if(StringUtils.isNotBlank(orgId) && StringUtils.isNotBlank(orgName)){
                                for(String nextOrgId:nextOrgIds){
                                    if(orgId.equals(nextOrgId)){
                                        contains = true;
                                        break;
                                    }
                                }
                            }
                            if(!contains){
                                msg.append("【" + orgName +"】");
                            }
                        }

                        if(StringUtils.isNotBlank(String.valueOf(msg))){
                            throw new ZhsqEventException(String.valueOf(msg.append("未进行办理人员的选择，下一办理组织中必须选择且只能选择一个办理人，请检查！")));
                        }
                    }else{
                        throw new ZhsqEventException("提交失败，下一办理组织中必须选择且只能选择一个办理人，请检查！");
                    }
                }
            }else{
                throw new IllegalArgumentException("提交失败，参数下一环节id【nodeId】缺失，请检查！");
            }
        }

        //下一环节为 处理中（持续跟踪监测） 时，流程不做变更，保存处理中环节信息
        if(ConstantValue.HANDLING_TASK_CODE.equals(nextNodeName)){
            Map<String,Object> resultMap = eventDisposalWorkflowService.saveHandlingTask(reportId, instanceId, nextNodeName, advice, userInfo,extraParam);
            flag = resultMap != null;

            if(flag){
                updateReportStatus(reportId,instanceId,userInfo,extraParam);
            }
        }else{
            flag = super.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);
        }

        if(flag && FocusReportNode360Enum.SCREENING_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curNodeName)){
            //task7为会签节点
            //下一环节为 对象确定（task8）时，判断当前办理人是否为空 WF_USERID，是的话，强制流转向一个节点
            //hessianFlowService.
            if(FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
                //下一环节为 归档 end1 强制归档整个流程
                flag = baseWorkflowService.endWorkflow4Base(instanceId, userInfo, extraParam);
            }
        }

        if(flag) {

            if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
                curNodeName = extraParam.get("curNodeName").toString();
            }

            if(reportId != null && reportId > 0) {

                if(FocusReportNode360Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
                    if(FocusReportNode360Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {

                        String isHonmura = null;

                        if(FocusReportNode360Enum.GRID_FEEDBACK_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
                            isHonmura = "1";//属于本村
                        } else if(FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
                            isHonmura = "0";//不属于本村
                        }
                        if(StringUtils.isNotBlank(isHonmura)) {
                            reportFocus.put("isHonmura", isHonmura);
                        }
                        //保存所属区域
                        if(CommonFunctions.isNotBlank(extraParam, "regionCode")) {
                            reportFocus.put("regionCode", extraParam.get("regionCode").toString());
                        }
                    }else if(FocusReportNode360Enum.COMMUNITY_COMMENT_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                        if(FocusReportNode360Enum.STREET_AUDIT_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
                            if(CommonFunctions.isNotBlank(extraParam, "partyMember")) {
                                reportFocus.put("partyMember", extraParam.get("partyMember").toString());
                            }
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

        /*if(flag && FocusReportNode352Enum.NOT_VERIFIED_NODE_NAME.getTaskNodeName().equals(nextNodeName)) {
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
        }*/

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
            extraParam.put("reportType", ReportTypeEnum.povertyPreMonitor.getReportTypeIndex());
        }

        if(FocusReportNode360Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
            if(FocusReportNode360Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
                isRejectByParent = false;
                isBack2Draft = true;
            } else if(FocusReportNode360Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
                isRejectByParent = true;
                isBack2Draft = true;
            }
        }

        extraParam.put("isRejectByParent", isRejectByParent);
        extraParam.put("isBack2Draft", isBack2Draft);

        flag = super.rejectWorkflow4Report(instanceId, userInfo, extraParam);

        /*if(flag) {
            Map<String, Object> curDataMap = workflow4BaseService.findCurTaskData4Base(instanceId);

            if(CommonFunctions.isNotBlank(curDataMap, "NODE_NAME")) {
                curNodeName = curDataMap.get("NODE_NAME").toString();
            }

            if(FocusReportNode360Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                    && FocusReportNode352Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
                Long reportId = null;
                Map<String, Object> reportFocus = new HashMap<String, Object>();

                if(CommonFunctions.isNotBlank(extraParam, "formId")) {
                    try {
                        reportId = Long.valueOf(extraParam.get("formId").toString());
                    } catch(NumberFormatException e) {}
                }

                if(reportId != null && reportId > 0) {
                    String reportType = ReportTypeEnum.povertyPreMonitor.getReportTypeIndex();

                    if(CommonFunctions.isNotBlank(extraParam, "reportType")) {
                        reportType = extraParam.get("reportType").toString();
                    }

                    reportFocus.put("reportType", reportType);
                    reportFocus.put("reportId", reportId);
                    reportFocus.put("disposalMode", EPCDisposalModeEnum.INIT_MODE.getDisposalMode());

                    //reportFocusService.saveReportFocus(reportFocus, userInfo);
                }
            }
        }*/

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
        String curNodeName = null, formTypeIdStr = null,dataSource = null,ppmRisk = null;
        boolean isEditableNode = false,
                isAble2Handle = false,	//是否可办理案件
                isChooseDefaultRadio = true,//是否默认选中办理环节
                userSelectedLimit=true;//人员选择是否限制选择人数

        if(CommonFunctions.isBlank(params, "reportType")) {
            initMap.put("reportType", ReportTypeEnum.povertyPreMonitor.getReportTypeIndex());
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
            if(CommonFunctions.isNotBlank(reportFocusMap,"ppmRisk")){
                ppmRisk = String.valueOf(reportFocusMap.get("ppmRisk"));
            }
        }

        if(CommonFunctions.isNotBlank(initMap, "isChooseDefaultRadio")) {
            isChooseDefaultRadio = Boolean.valueOf(String.valueOf(initMap.get("isChooseDefaultRadio")));
        }
        if(CommonFunctions.isNotBlank(initMap, "isAble2Handle")) {
            isAble2Handle = Boolean.valueOf(String.valueOf(initMap.get("isAble2Handle")));
        }

        if(isAble2Handle && FocusReportNode360Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)){
            List<Map<String, Object>> nextNodeMapList = null;
            String nodeName = null;

            if(StringUtils.isBlank(dataSource)){
                if(CommonFunctions.isNotBlank(params,"dataSource")){
                    dataSource = String.valueOf(params.get("dataSource"));
                }
            }

            if(StringUtils.isBlank(dataSource)){
                throw new ZhsqEventException("接口调用失败，致贫返贫信息数据来源【dataSource】为空，请检查！");
            }

            if(CommonFunctions.isNotBlank(initMap, "nextTaskNodes")) {
                nextNodeMapList = (List<Map<String, Object>>) initMap.get("nextTaskNodes");
            }

            if(FocusReportNode360Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                List<Map<String, Object>> nextNodeMapListRemain = new ArrayList<>();
                Map<String,Object> taskMap = null;
                String EXIST_PPM_RISK = "1",NO_PPM_RISK = "0";

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (int i=0,len = nextNodeMapList.size();i<len;i++){
                        taskMap = nextNodeMapList.get(i);

                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }

                        //数据来源为 网格员巡查发现01
                        // 下一环节 保留 归档end1 村级评议 task4
                        // 下一环节 保留 归档end1 村级评议 task4
                        if(PPMDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)){
                            //是否存在致贫返贫风险为空时 保留原逻辑
                            if(StringUtils.isBlank(ppmRisk)){
                                if(FocusReportNode360Enum.COMMUNITY_COMMENT_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    nextNodeMapListRemain.add(taskMap);
                                }
                                if(FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    taskMap.put("nodeNameZH","归档(未发现返贫风险)");
                                    nextNodeMapListRemain.add(taskMap);
                                }
                            }else{
                                //存在致贫返贫风险 保留 村级评议
                                if(EXIST_PPM_RISK.equals(ppmRisk) && FocusReportNode360Enum.COMMUNITY_COMMENT_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    nextNodeMapListRemain.add(taskMap);
                                }else if(NO_PPM_RISK.equals(ppmRisk) && FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    //不存在致贫返贫风险 归档(未发现返贫风险)
                                    taskMap.put("nodeNameZH","归档(未发现返贫风险)");
                                    nextNodeMapListRemain.add(taskMap);
                                }
                            }
                        }else if(PPMDataSourceEnum.INVOLVED_UNIT_DEPT.getDataSource().equals(dataSource)
                                || PPMDataSourceEnum.REPORT_POVERTY_HOTLINE.getDataSource().equals(dataSource)
                                || PPMDataSourceEnum.HIGNER_DEPT_FOUND_FEEDBACK.getDataSource().equals(dataSource)){
                            //数据来源为 02 03 04 的 下一环节只有 一级网格长任务派发 task2 环节
                            if(FocusReportNode360Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                nextNodeMapListRemain.add(taskMap);
                            }
                        }else if(PPMDataSourceEnum.BELOW_DIVISION_DEPT_VISITED.getDataSource().equals(dataSource)
                                || PPMDataSourceEnum.BELOW_DEPT_VISITED.getDataSource().equals(dataSource) ){
                            //数据来源为 05 06 下一环节保存 一级网格长任务派发task2  归档end1 remove 20211113
                            /*if(FocusReportNode360Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                nextNodeMapListRemain.add(taskMap);
                            }*/
                            if(StringUtils.isBlank(ppmRisk)){
                                //modify 数据来源为 05 06 下一环节保存 村级评议 task4  归档end1
                                if(FocusReportNode360Enum.COMMUNITY_COMMENT_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    nextNodeMapListRemain.add(taskMap);
                                }
                                if(FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    taskMap.put("nodeNameZH","归档(未发现返贫风险)");
                                    nextNodeMapListRemain.add(taskMap);
                                }
                            }else{
                                if(EXIST_PPM_RISK.equals(ppmRisk) && FocusReportNode360Enum.COMMUNITY_COMMENT_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    nextNodeMapListRemain.add(taskMap);
                                }else if(NO_PPM_RISK.equals(ppmRisk) && FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    taskMap.put("nodeNameZH","归档(未发现返贫风险)");
                                    nextNodeMapListRemain.add(taskMap);
                                }
                            }
                        }
                    }
                }
                nextNodeMapList = nextNodeMapListRemain;
                if(nextNodeMapList != null && nextNodeMapList.size() == 1){
                    isChooseDefaultRadio = true;
                }
                initMap.put("nextTaskNodes",nextNodeMapList);
            }else if(FocusReportNode360Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                isChooseDefaultRadio = false;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (Map<String, Object> taskMap:nextNodeMapList){
                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }
                        if(FocusReportNode360Enum.GRID_FEEDBACK_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","二级网格长反馈情况(属于本村)");
                        }else if(FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","归档(不属于本村)");
                        }
                    }
                }
            }else if(FocusReportNode360Enum.GRID_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                isChooseDefaultRadio = false;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (Map<String, Object> taskMap:nextNodeMapList){
                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }
                        if(FocusReportNode360Enum.COMMUNITY_COMMENT_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","村级评议(符合监测对象)");
                        }else if(FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","归档(不符合监测对象)");
                        }
                    }
                }
            }else if(FocusReportNode360Enum.COMMUNITY_COMMENT_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                isChooseDefaultRadio = false;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (Map<String, Object> taskMap:nextNodeMapList){
                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }
                        if(FocusReportNode360Enum.STREET_AUDIT_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","镇级审核(符合监测对象)");
                        }else if(FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","归档(不符合监测对象)");
                        }
                    }
                }
            }else if(FocusReportNode360Enum.STREET_AUDIT_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                isChooseDefaultRadio = false;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (Map<String, Object> taskMap:nextNodeMapList){
                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }
                        if(FocusReportNode360Enum.MUNICIPAL_SEND_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","市扶贫办任务派发(符合监测对象)");
                        }else if(FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","归档(不符合监测对象)");
                        }
                    }
                }
            }else if(FocusReportNode360Enum.MUNICIPAL_SEND_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                //当前环节为 task6 时 主送的选择人数不进行限制
                userSelectedLimit = false;
                //关闭默认节点选择
                //isChooseDefaultRadio = true;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (Map<String, Object> taskMap:nextNodeMapList){
                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }
                        if(FocusReportNode360Enum.DETERMINE_OBJECT_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","对象确定(符合监测对象)");
                        }else if(FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","归档(不符合监测对象)");
                        }
                    }
                }

            }else if(FocusReportNode360Enum.SCREENING_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                //关闭默认节点选择
                isChooseDefaultRadio = false;
                //获取当前登录用户组织信息
                OrgSocialInfoBO userOrgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
                String orgProCode = null;
                String trigCondition = curNodeName + "-" + nodeName;

                Map<String,String> NODE_MAP = new HashMap<String, String>(){
                    {
                        put(FocusReportDeptProCodeEnum.FINANCE_BUREAU_BUDGET_SECTION.getProfessionCode() + FocusReportDeptProCodeEnum.EXIST.getProfessionCode(),"归档(存在财政供养)");
                        put(FocusReportDeptProCodeEnum.FINANCE_BUREAU_BUDGET_SECTION.getProfessionCode() + FocusReportDeptProCodeEnum.DOES_NOT_EXIST.getProfessionCode(),"对象确定(不存在财政供养)");
                        put(FocusReportDeptProCodeEnum.NATURAL_RESOURCES_REGISTRATION_CENTER.getProfessionCode() + FocusReportDeptProCodeEnum.EXIST.getProfessionCode(),"归档(存在商品房/店面)");
                        put(FocusReportDeptProCodeEnum.NATURAL_RESOURCES_REGISTRATION_CENTER.getProfessionCode() + FocusReportDeptProCodeEnum.DOES_NOT_EXIST.getProfessionCode(),"对象确定(不存在商品房/店面)");
                        put(FocusReportDeptProCodeEnum.MARKET_APPROVAL_SECTION.getProfessionCode() + FocusReportDeptProCodeEnum.EXIST.getProfessionCode(),"归档(存在工商注册企业)");
                        put(FocusReportDeptProCodeEnum.MARKET_APPROVAL_SECTION.getProfessionCode() + FocusReportDeptProCodeEnum.DOES_NOT_EXIST.getProfessionCode(),"对象确定(无工商注册企业)");
                        put(FocusReportDeptProCodeEnum.TRAFFIC_POLICE_BRIGADE.getProfessionCode() + FocusReportDeptProCodeEnum.EXIST.getProfessionCode(),"归档(存在私家用车)");
                        put(FocusReportDeptProCodeEnum.TRAFFIC_POLICE_BRIGADE.getProfessionCode() + FocusReportDeptProCodeEnum.DOES_NOT_EXIST.getProfessionCode(),"对象确定(无车辆信息)");
                    }
                };

                if(null != userOrgInfo){
                    orgProCode = userOrgInfo.getProfessionCode();
                }
                //获取功能配置信息
                if(StringUtils.isNotBlank(orgProCode)){
                    if(NODE_MAP.containsKey(orgProCode + FocusReportDeptProCodeEnum.EXIST.getProfessionCode())
                            || NODE_MAP.containsKey(orgProCode + FocusReportDeptProCodeEnum.DOES_NOT_EXIST.getProfessionCode())){
                        if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                            for (Map<String, Object> taskMap:nextNodeMapList){
                                if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                                    nodeName = String.valueOf(taskMap.get("nodeName"));
                                }
                                if(FocusReportNode360Enum.DETERMINE_OBJECT_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    taskMap.put("nodeNameZH",NODE_MAP.get(orgProCode + FocusReportDeptProCodeEnum.DOES_NOT_EXIST.getProfessionCode()));
                                }else if(FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    taskMap.put("nodeNameZH",NODE_MAP.get(orgProCode + FocusReportDeptProCodeEnum.EXIST.getProfessionCode()));
                                }
                            }
                        }
                    }else{
                        //当前办理人所属组织 专业编码不在 枚举类中
                        throw new ZhsqEventException("当前环节【筛查反馈】的办理人组织专业编码不在固定配置中，请检查！");
                    }
                }else{
                    throw new ZhsqEventException("当前环节【筛查反馈】的办理人组织专业编码为空，请检查！");
                }
            }else if(FocusReportNode360Enum.DE_MONITORING_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                //关闭默认节点选择
                isChooseDefaultRadio = false;
                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (Map<String, Object> taskMap:nextNodeMapList){
                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }
                        if(FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","归档(解除监测)");
                        }
                    }
                    //构造继续监测虚拟节点
                    Map<String, Object> node = new HashMap<>();
                    node.put("nodeId",-1);
                    node.put("nodeName",ConstantValue.HANDLING_TASK_CODE);
                    node.put("nodeNameZH",ConstantValue.HANDLING_TASK_NAME + "(未解除监测)");
                    node.put("dynamicSelect",null);//提交时不需要选择办理人员
                    node.put("nodeType","1");
                    node.put("transitionCode","__E0__");

                    nextNodeMapList.add(node);
                }
            }

        }

        //当前环节为 二级网格长反馈情况 节点时，监测基本信息可编辑
        if(CommonFunctions.isNotBlank(initMap, "isAble2Handle")
                && Boolean.valueOf(initMap.get("isAble2Handle").toString())
                && FocusReportNode360Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                && FocusReportNode360Enum.GRID_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curNodeName)) {

            isEditableNode = true;
            initMap.put("isEditable", true);
            initMap.put("isEditOccOnly", true);
        }

        initMap.put("isEditableNode", isEditableNode);
        initMap.put("isChooseDefaultRadio", isChooseDefaultRadio);
        initMap.put("userSelectedLimit", userSelectedLimit);

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

                if(!FocusReportNode360Enum.END_NODE_NAME.getTaskNodeNameZH().equals(taskNameZH)) {
                    Map<String, Object> endTaskMap = new HashMap<String, Object>();

                    endTaskMap.put("TASK_NAME", FocusReportNode360Enum.END_NODE_NAME.getTaskNodeNameZH());

                    resultMapList.add(0, endTaskMap);
                }
            }

            //获取会签节点环节详情信息
            for(Map<String,Object> task:resultMapList){
                if(CommonFunctions.isNotBlank(task,"TASK_ID") && CommonFunctions.isNotBlank(task,"TASK_CODE")
                        && (FocusReportNode360Enum.SCREENING_FEEDBACK_NODE_NAME.getTaskNodeName().equals(task.get("TASK_CODE"))
                        || FocusReportNode360Enum.DE_MONITORING_NODE_NAME.getTaskNodeName().equals(task.get("TASK_CODE")) )
                ){

                    List<Map<String, Object>> subAndReceivedTaskList = hessianFlowService.queryTaskDetails(String.valueOf(task.get("TASK_ID")), order),
                            timeAndRemarkList = new ArrayList<Map<String, Object>>();
                    Map<String, Object> timeAndRemarkMap = new HashMap<String, Object>();

                    //会签节点 task7 去除父任务节点的办理意见
                    if(FocusReportNode360Enum.SCREENING_FEEDBACK_NODE_NAME.getTaskNodeName().equals(task.get("TASK_CODE")) && CommonFunctions.isNotBlank(task,"REMARKS")){
                        task.remove("REMARKS");
                    }

                    for(Map<String, Object> subAndReceivedTaskMap : subAndReceivedTaskList) {
                        timeAndRemarkList = new ArrayList<>();
                        timeAndRemarkMap = new HashMap<String, Object>();

                        if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "START_TIME")) {
                            timeAndRemarkMap.put("START_TIME", subAndReceivedTaskMap.get("START_TIME"));
                        }
                        if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "END_TIME")) {
                            timeAndRemarkMap.put("END_TIME", subAndReceivedTaskMap.get("END_TIME"));
                        }
                        if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "RECEIVE_TIME")) {
                            timeAndRemarkMap.put("RECEIVE_TIME", subAndReceivedTaskMap.get("RECEIVE_TIME"));
                        }
                        if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "REMARKS")) {
                            timeAndRemarkMap.put("REMARKS", subAndReceivedTaskMap.get("REMARKS"));
                        }
                        timeAndRemarkList.add(timeAndRemarkMap);
                        subAndReceivedTaskMap.put("timeAndRemarkList",timeAndRemarkList);
                    }

                    task.put("subAndReceivedTaskList",subAndReceivedTaskList);
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
        if(ReportTypeEnum.povertyPreMonitor.getReportTypeIndex().equals(reportType)) {
            workflowName = "致贫返贫监测流程";
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
        if(ReportTypeEnum.povertyPreMonitor.getReportTypeIndex().equals(reportType)) {
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
        isDecisionMaking = FocusReportNode360Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) && false;

        params.put("proInstance", proInstance);
        params.put("isDecisionMaking", isDecisionMaking);

        nodeMapList = super.findNextTaskNodes(instanceId, userInfo, params);

        /*if(FocusReportNode360Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
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
            }
        }*/

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
