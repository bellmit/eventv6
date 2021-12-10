package cn.ffcs.zhsq.reportFocus.threeOneTreatment.service.impl;

import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.workflow.spring.WorkflowHolidayInfoService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.threeOneTreatment.ReportTOTMapper;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportServiceAgent;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportFocusWorkflow4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.data.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Author: zhangtc
 * @Date: 2021/8/17 15:25
 */
@Service(value = "reportFocusWorkflow4TOTService")
public class ReportFocusWorkflow4TOTServiceImpl extends ReportFocusWorkflow4TwoVioPreServiceImpl {
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

//    @Autowired
//    private UserManageOutService userManageService;

    @Autowired
    private IEventDisposalWorkflowService eventDisposalWorkflowService;

    @Autowired
    private HessianFlowService hessianFlowService;

    @Autowired
    private IReportIntegrationService reportIntegrationService;

    @Autowired
    private ReportTOTMapper reportTOTMapper;

    @Autowired
    private WorkflowHolidayInfoService workflowHolidayInfoService;

    @Override
    public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {

        boolean flag = false, isSubByParent = true;
        String curNodeName = null,
                formTypeIdStr = null,
                nextUserIds = null,
                nextOrgIds = null,
                advice = null,
                dataSource=null;
        Map<String, Object> reportFocus = new HashMap<String, Object>();
        Long reportId = null;
        //List<UserInfo> userInfoList = null;
        ProInstance proInstance = null;


        if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
            curNodeName = extraParam.get("curNodeName").toString();
        }
        if(instanceId != null && instanceId > 0) {
            proInstance = baseWorkflowService.findProByInstanceId(instanceId);
            if(proInstance != null) {
                formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
                curNodeName = proInstance.getCurNode();

                if(CommonFunctions.isBlank(extraParam, "proInstance")) {
                    extraParam.put("proInstance", proInstance);
                }
            } else {
                throw new IllegalArgumentException("流程实例id【"+ instanceId +"】没有找到有效的流程信息！");
            }
        } else {
            throw new IllegalArgumentException("缺少有效的流程实例id！");
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

        //下一环节办理人（构造启动子流程用户信息 "三合一整治流程_隐患状态跟踪"）
        if(nextUserInfoList == null || nextUserInfoList.size() == 0) {
            nextUserInfoList = new ArrayList<UserInfo>();

            if(CommonFunctions.isNotBlank(extraParam, "nextUserIds")) {
                nextUserIds = extraParam.get("nextUserIds").toString();
            }
            if(CommonFunctions.isNotBlank(extraParam, "nextOrgIds")) {
                nextOrgIds = extraParam.get("nextOrgIds").toString();
            }
            if(StringUtils.isNotBlank(nextUserIds) && StringUtils.isNotBlank(nextOrgIds)) {
                String[] nextUserArray = nextUserIds.split(","),
                        nextOrgIdArray = nextOrgIds.split(",");
                String nextUserIdStr = null, nextOrgIdStr = null;
                Long nextUserId = -1L, nextOrgId = -1L;
                UserInfo nextUserInfo = null;
                Set<String> nextUserInfoSet = new HashSet<String>();
                StringBuffer nextUserInfoKey = new StringBuffer("");

                for(int index = 0, len = nextUserArray.length; index < len; index++) {
                    nextUserIdStr = nextUserArray[index];
                    nextOrgIdStr = nextOrgIdArray[index];

                    if(StringUtils.isNotBlank(nextUserIdStr)) {
                        nextUserId = Long.valueOf(nextUserIdStr);
                    }
                    if(StringUtils.isNotBlank(nextOrgIdStr)) {
                        nextOrgId = Long.valueOf(nextOrgIdStr);
                    }
                    if(nextUserId > 0 && nextOrgId > 0) {
                        nextUserInfoKey = new StringBuffer("");
                        nextUserInfoKey.append(nextUserId).append("-").append(nextOrgId);

                        if(!nextUserInfoSet.contains(nextUserInfoKey.toString())) {
                            nextUserInfoSet.add(nextUserInfoKey.toString());

                            nextUserInfo = new UserInfo();
                            nextUserInfo.setUserId(nextUserId);
                            nextUserInfo.setOrgId(nextOrgId);
                            nextUserInfoList.add(nextUserInfo);
                        }
                    }
                }
            } else if(StringUtils.isNotBlank(nextOrgIds)) {
                String[] nextOrgIdArray = nextOrgIds.split(",");
                String nextOrgIdStr = null;
                Long nextOrgId = -1L;
                UserInfo nextUserInfo = null;
                Set<Long> nextOrgSet = new HashSet<Long>();

                for(int index = 0, len = nextOrgIdArray.length; index < len; index++) {
                    nextOrgIdStr = nextOrgIdArray[index];

                    if(StringUtils.isNotBlank(nextOrgIdStr)) {
                        nextOrgId = Long.valueOf(nextOrgIdStr);
                    }
                    if(nextOrgId > 0) {
                        if(!nextOrgSet.contains(nextOrgId)) {
                            nextOrgSet.add(nextOrgId);

                            nextUserInfo = new UserInfo();
                            nextUserInfo.setOrgId(nextOrgId);
                            nextUserInfoList.add(nextUserInfo);
                        }
                    }
                }
            }
        }

        //需要保存基本信息的环节才进行查询一遍基本信息
        /*if(FocusReportNode364Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)){
            if( (FocusReportNode364Enum.VERIFY_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curNodeName)
                    && FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName))
                || (FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName))
                || (FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName))
                || (FocusReportNode364Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(curNodeName))
            ){*/
                Map<String, Object> reportParams = new HashMap<String, Object>();
                reportParams.putAll(extraParam);
                reportParams.put("reportId", reportId);
                reportParams.put("reportType", ReportTypeEnum.threeOneTreatment.getReportTypeIndex());
                reportFocus = reportIntegrationService.findReportFocusByUUID(null, userInfo, reportParams);
           /* }
        }*/

        //子流程: 当前环节为 task1 下一环节为 task2 所属区域精确到网格层级
        if(FocusReportNode36401Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
                && FocusReportNode36401Enum.DECOMPOSITION_TRACKING_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
                && FocusReportNode36401Enum.PERIODIC_REPORTS_HDSTATUS_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
            OrgEntityInfoBO orgEntityInfo = null;
            String orgLevel = null;

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


        //当前环节为 task3时，下一环节为task4时 所属区域至少要精确到网格层级
        if(FocusReportNode364Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
                && FocusReportNode364Enum.VERIFY_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curNodeName)
                && FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
            OrgEntityInfoBO orgEntityInfo = null;
            String orgLevel = null;

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

        //启动子流程 当前环节为 核实反馈-一级网格处置（数据来源 01 02）、发现环节-一级网格处置（数据来源 03-05）
        if(FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                && (
                        (FocusReportNode364Enum.VERIFY_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curNodeName)
                        && FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName))
                     || (FocusReportNode364Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)
                                && FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)
                                )
                )
        ){
            //启动子流程的同时主流程也向下流转
            if(nextUserInfoList != null && nextUserInfoList.size() > 0) {
                Long nextUserOrgId = null;
                OrgSocialInfoBO orgInfo = null;
                String curTaskId = null;//,orgChiefLevel = null, orgType = null,
                        // orgTypeUnit = String.valueOf(ConstantValue.ORG_TYPE_UNIT);
                Map<String, Object> subProParams = null,
                        curTaskData = workflow4BaseService.findCurTaskData4Base(instanceId);
                List<UserInfo> removeUserInfoList = new ArrayList<UserInfo>();
                Boolean result = false;

                if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
                    curTaskId = curTaskData.get("WF_DBID_").toString();
                }

                for(UserInfo nextUserInfo : nextUserInfoList) {
                    nextUserOrgId = nextUserInfo.getOrgId();

                    orgInfo = orgSocialInfoService.findByOrgId(nextUserOrgId);

                    if(orgInfo != null) {
                       // orgChiefLevel = orgInfo.getChiefLevel();
                       // orgType = orgInfo.getOrgType();
                        subProParams = new HashMap<String, Object>();

                        subProParams.putAll(extraParam);
                        subProParams.put("parentId", instanceId);
                        subProParams.put("taskId", curTaskId);
                        subProParams.put("isConvertProinstance", false);
                        subProParams.remove("instanceId");//若不移除，会影响子流程实例的生成
                        subProParams.put("formTypeId", FocusReportNode36401Enum.FORM_TYPE_ID.toString());

                        removeUserInfoList.add(nextUserInfo);
                        result = this.startWorkflow4Report(proInstance.getFormId(), nextUserInfo, subProParams);

                        //子流程启动成功 发送mq消息
                        if(result){
                            sendSubFlowRMQmMsg(reportId,formTypeIdStr,FocusReportNode36401Enum.FORM_TYPE_ID.toString(),instanceId,subProParams.get("instanceId"));
                        }
                    }
                }

                //移除子流程启动用户，剩余办理人员推送给主流程下一环节
                nextUserInfoList.removeAll(removeUserInfoList);
            }
        }

        //子流程状态跟踪
        if(FocusReportNode36401Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)){

            if(FocusReportNode36401Enum.DECOMPOSITION_TRACKING_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                isSubByParent = false;
                if(CommonFunctions.isBlank(extraParam, "instanceId")) {
                    extraParam.put("instanceId", instanceId);
                }
                flag = workflow4BaseService.subWorkflow4Base(null, null, null, nextNodeName, nextUserInfoList, userInfo, extraParam);
            }else if(FocusReportNode36401Enum.PERIODIC_REPORTS_HDSTATUS_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                isSubByParent = false;
                //当前环节为 定期报告隐患状态 流程不做变更 保存处理信息（需要向第一网格长发送阅办消息）
                Map<String,Object> resultMap = eventDisposalWorkflowService.saveHandlingTask(reportId, instanceId, nextNodeName, advice, userInfo,extraParam);
                flag = resultMap != null;
                //更新当前环节办理期限 每提交一次 在当前日期+3天
                //当前环节办理期限

                //获取子流程当前环节
                Map<String,Object> curTaskMap = baseWorkflowService.findCurTaskData(instanceId);
                String curTaskId_ = "";
                if(CommonFunctions.isNotBlank(curTaskMap,"DUEDATE_")){
                    Date curTaskDueDate = (Date) curTaskMap.get("DUEDATE_"),
                         currentDate = new Date();

                    if(null != curTaskDueDate){
                        currentDate = workflowHolidayInfoService.getWorkDateByInterval(curTaskDueDate, 3);
                    }

                    curTaskId_ = curTaskMap.get("WF_DBID_").toString();
                    reportParams.put("newDate", currentDate);
                    reportParams.put("taskId", curTaskId_);
                    reportTOTMapper.updateTaskTime(reportParams);
                }
            }
        }
        if(isSubByParent){
            //提交
            flag = super.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);

            //主流程归档提交成功 判断子流程是否存在 存在的话全部归档
            if(flag && FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                    && (FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName))) {
                //主流程结束后，强制完结各子流程
                endAllSubProInstance(instanceId, null,null);

                Map<String, Object> updateStatusParams = new HashMap<String, Object>();
                updateStatusParams.putAll(extraParam);
                updateStatusParams.put("nextNodeName", FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName());
                updateStatusParams.put("instanceId", instanceId);

                updateReportStatus(null, instanceId, userInfo, updateStatusParams);
            }
        }

        //保存流程流转过程中 上报信息基本信息
        if(flag) {

            if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
                curNodeName = extraParam.get("curNodeName").toString();
            }

            if(reportId != null && reportId > 0) {

                if(FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                        || FocusReportNode36401Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {

                    if(!reportFocus.isEmpty()) {
                        String reportType = null,occurred = null,hdtDescribe = null,hiddenDangerType = null,disposalResult = null;

                        if(CommonFunctions.isNotBlank(extraParam, "reportType")) {
                            reportType = extraParam.get("reportType").toString();
                        } else {
                            reportType = ReportTypeEnum.threeOneTreatment.getReportTypeIndex();
                        }
                        if(CommonFunctions.isNotBlank(extraParam, "occurred")) {
                            occurred = extraParam.get("occurred").toString();
                        }
                        if(CommonFunctions.isNotBlank(extraParam, "hiddenDangerType")) {
                            hiddenDangerType = extraParam.get("hiddenDangerType").toString();
                        }
                        if(CommonFunctions.isNotBlank(extraParam, "hdtDescribe")) {
                            hdtDescribe = extraParam.get("hdtDescribe").toString();
                        }
                        if(CommonFunctions.isNotBlank(extraParam, "disposalResult")) {
                            disposalResult = extraParam.get("disposalResult").toString();
                        }
                        //延期日期（只有主流程更新日期时间）
                        if(FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                                && CommonFunctions.isNotBlank(extraParam, "extensionDate")) {
                            reportFocus.put("extensionDate", extraParam.get("extensionDate").toString());
                        }

                        //当前环节为核实环节 属实保存定位信息
                        if(FocusReportNode364Enum.VERIFY_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curNodeName)
                                && FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
                            if(CommonFunctions.isNotBlank(extraParam, "latitude")) {
                                reportFocus.put("resMarker.x", extraParam.get("latitude").toString());
                            }
                            if(CommonFunctions.isNotBlank(extraParam, "longitude")) {
                                reportFocus.put("resMarker.y", extraParam.get("longitude").toString());
                            }
                            if(CommonFunctions.isNotBlank(extraParam, "maptype")) {
                                reportFocus.put("resMarker.mapType", extraParam.get("maptype").toString());
                            }
                            if(CommonFunctions.isNotBlank(reportFocus,"totUUID") && CommonFunctions.isNotBlank(reportFocus,"reportUUID")){
                                reportFocus.remove("totUUID");
                            }
                            //保存地理标注信息
                            reportFocus.put("isSaveResMarkerInfo", true);

                            //隐患类型
                            if(CommonFunctions.isNotBlank(extraParam, "dynamicDictId")) {
                                hiddenDangerType = extraParam.get("dynamicDictId").toString();
                            }
                        }else if(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                            if(CommonFunctions.isNotBlank(extraParam,"dataSource")){
                                dataSource = String.valueOf(extraParam.get("dataSource"));
                            }else if(CommonFunctions.isNotBlank(reportFocus,"dataSource")) {
                                dataSource = String.valueOf(reportFocus.get("dataSource"));
                            }
                            //一级网格处置结果
                            //网格 01；12345 02；群众 05
                            if(TOTDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)
                                    ||TOTDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource)
                                    ||TOTDataSourceEnum.MASSES_REPORT.getDataSource().equals(dataSource)){

                                if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
                                    if(CommonFunctions.isNotBlank(extraParam, "dynamicDictId")) {
                                        disposalResult =  extraParam.get("dynamicDictId").toString();
                                    }
                                }else if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
                                    //下一环节为归档 -已完成处置
                                    disposalResult = TOTDisposalResultEnum.REMEDIATION_COMPLETE.getDisposalResult();
                                    //reportFocus.put("disposalResult", "0");
                                }
                            }else{
                                if(CommonFunctions.isNotBlank(extraParam, "dynamicDictId")) {
                                    disposalResult =  extraParam.get("dynamicDictId").toString();
                                }
                            }
                            if(StringUtils.isBlank(disposalResult)){
                                throw new IllegalArgumentException("参数：处置结果缺失【disposalResult】，请检查！");
                            }else /*if(TOTDisposalResultEnum.APPLY_EXTESION.getDisposalResult().equals(disposalResult)
                                    ||TOTDisposalResultEnum.REMEDIATION_COMPLETE.getDisposalResult().equals(disposalResult))*/{
                                //一级网格处置结果为已完成整治、申请延期 保存网格处置期限
                                if(CommonFunctions.isNotBlank(extraParam,"communityDuedate")){
                                    reportFocus.put("communityDuedate", DateUtils.convertStringToDate(String.valueOf(extraParam.get("communityDuedate")),DateUtils.PATTERN_DATE));
                                }else{
                                    throw new IllegalArgumentException("参数：缺失一级网格办理期限【communityDuedate】，请检查！");
                                }
                            }
                            reportFocus.put("disposalResult", disposalResult);
                        }else if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                            if(CommonFunctions.isNotBlank(extraParam,"streetDuedate")){
                                reportFocus.put("streetDuedate", DateUtils.convertStringToDate(String.valueOf(extraParam.get("streetDuedate")),DateUtils.PATTERN_DATE));
                            }
                        }else if(FocusReportNode364Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                            //验收通过 1
                            if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
                                reportFocus.put("checkAccept",TOTDisposalResultEnum.CHECK_PASSED.getDisposalResult());
                            }else if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
                                //验收不通过 0
                                reportFocus.put("checkAccept",TOTDisposalResultEnum.CHECK_NOPASSED.getDisposalResult());
                            }
                        }

                        reportFocus.put("reportType", reportType);
                        reportFocus.put("reportId", reportId);
                        reportFocus.put("occurred", occurred);
                        reportFocus.put("hiddenDangerType", hiddenDangerType);
                        reportFocus.put("hdtDescribe", hdtDescribe);

                        reportFocusService.saveReportFocus(reportFocus, userInfo);
                    }
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
            extraParam.put("reportType", ReportTypeEnum.threeOneTreatment.getReportTypeIndex());
        }

        if(FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
            if(FocusReportNode364Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
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

        //主流程撤回成功 判断子流程是否存在 存在的话全部归档
        if(flag) {
            //主流程结束后，强制完结各子流程
            endAllSubProInstance(instanceId, null,null);
        }

        return flag;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public Map<String, Object> initWorkflow4Handle(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {

        Map<String, Object> initMap = super.initWorkflow4Handle(instanceId, userInfo, params);
        String curNodeName = null, formTypeIdStr = null,dataSource = null, disposalResult=null, checkAccept = null;
        boolean isEditableNode = false,
                isAble2Handle = false,	//是否可办理案件
                isChooseDefaultRadio = true,//是否默认选中办理环节
                userSelectedLimit=true;//人员选择是否限制选择人数

        if(CommonFunctions.isBlank(params, "reportType")) {
            initMap.put("reportType", ReportTypeEnum.threeOneTreatment.getReportTypeIndex());
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
            if(CommonFunctions.isNotBlank(reportFocusMap,"disposalResult")){
                disposalResult = String.valueOf(reportFocusMap.get("disposalResult"));
            }
            if(CommonFunctions.isNotBlank(reportFocusMap,"checkAccept")){
                checkAccept = String.valueOf(reportFocusMap.get("checkAccept"));
            }
        }

        if(StringUtils.isBlank(dataSource) && CommonFunctions.isNotBlank(params,"dataSource")){
            dataSource = String.valueOf(params.get("dataSource"));
        }
        if(CommonFunctions.isNotBlank(initMap, "isChooseDefaultRadio")) {
            isChooseDefaultRadio = Boolean.valueOf(String.valueOf(initMap.get("isChooseDefaultRadio")));
        }
        if(CommonFunctions.isNotBlank(initMap, "isAble2Handle")) {
            isAble2Handle = Boolean.valueOf(String.valueOf(initMap.get("isAble2Handle")));
        }

        if(isAble2Handle && FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)){
            List<Map<String, Object>> nextNodeMapList = null;
            String nodeName = null;

            if(CommonFunctions.isNotBlank(initMap, "nextTaskNodes")) {
                nextNodeMapList = (List<Map<String, Object>>) initMap.get("nextTaskNodes");
            }

            //待办数据 数据来源不能为空
            if(StringUtils.isBlank(dataSource)){
                throw new IllegalArgumentException("流程初始化时，参数【dataSource】缺失，请检查！");
            }

            //过滤环节 整治处置部分结合具体业务区分(task4-task7) 串流程时再做
            if(FocusReportNode364Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                List<Map<String, Object>> nextNodeMapListRemain = new ArrayList<>();
                Map<String,Object> taskMap = null;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (int i=0 ,len = nextNodeMapList.size(); i<len; i++){
                        taskMap = nextNodeMapList.get(i);

                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }

                        //数据来源为 网格员发现上报01 下一环节保留：一级网格处置 task2；归档end1
                        if(TOTDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)){
                            if(FocusReportNode364Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                nextNodeMapListRemain.add(taskMap);
                            }
                            if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                nextNodeMapListRemain.add(taskMap);
                            }
                        }else if(TOTDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource)){
                            //数据来源为 02 的 下一环节只有：任务派发 task3 环节；
                            if(FocusReportNode364Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                nextNodeMapListRemain.add(taskMap);
                            }
                        }else if( TOTDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(dataSource)
                                || TOTDataSourceEnum.UNANNOUNCED_VISIT.getDataSource().equals(dataSource)
                                || TOTDataSourceEnum.MASSES_REPORT.getDataSource().equals(dataSource)){
                            //数据来源为 03 04 05 的 下一环节只有：一级网格处置 task4 环节；
                            if(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
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
            }else if(FocusReportNode364Enum.VERIFY_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                isChooseDefaultRadio = false;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (Map<String, Object> taskMap:nextNodeMapList){
                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }
                        if(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","一级网格处置(属实)");
                        }else if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","归档(不属实)");
                        }
                    }
                }
            }else if(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                List<Map<String, Object>> nextNodeMapListRemain = new ArrayList<>();
                Map<String,Object> taskMap = null;
                //采集渠道
                if(StringUtils.isNotBlank(dataSource)){
                    if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                        for (int i=0 ,len = nextNodeMapList.size(); i<len; i++){
                            taskMap = nextNodeMapList.get(i);

                            if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                                nodeName = String.valueOf(taskMap.get("nodeName"));
                            }

                            //数据来源为 网格员发现上报01、12345 下一环节保留：镇级处置task5 归档end1
                            if(TOTDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)
                                    || TOTDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource)){
                                if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    taskMap.put("nodeNameZH","镇级处置(未完成整治)");
                                    nextNodeMapListRemain.add(taskMap);
                                }else if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    taskMap.put("nodeNameZH","归档(已完成整治)");
                                    nextNodeMapListRemain.add(taskMap);
                                }
                            }else if(TOTDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(dataSource)
                                    || TOTDataSourceEnum.UNANNOUNCED_VISIT.getDataSource().equals(dataSource)
                                    || TOTDataSourceEnum.MASSES_REPORT.getDataSource().equals(dataSource)){
                                //数据来源为 03 04 05 的 下一环节只有：镇级处置 task5 环节；
                                if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
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
                }else {
                    throw new ZhsqEventException("上报信息缺失数据来源字段，请检查！");
                }
            }else if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                List<Map<String, Object>> nextNodeMapListRemain = new ArrayList<>();
                Map<String,Object> taskMap = null;

                if(StringUtils.isNotBlank(disposalResult)){
                    if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                        for (int i=0 ,len = nextNodeMapList.size(); i<len; i++){
                            taskMap = nextNodeMapList.get(i);

                            if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                                nodeName = String.valueOf(taskMap.get("nodeName"));
                            }

                            //一级网格处置结果，0：已完成整治，1：申请延期，2：提请镇级处置
                            //对应上一环节标准字段④的内容-已完成整治
                            if(TOTDisposalResultEnum.REMEDIATION_COMPLETE.getDisposalResult().equals(disposalResult)){
                                //已完成整治
                                //03 04 05 市级采集、群众举报的 上一环节（一级网格）办理意见为 第四点 已完成整治
                                // (如果是镇级已经处置过，且上报到市级组织验收不通过再回来的，下一环节无网格处置)
                                if(TOTDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(dataSource)
                                        || TOTDataSourceEnum.UNANNOUNCED_VISIT.getDataSource().equals(dataSource)
                                        || TOTDataSourceEnum.MASSES_REPORT.getDataSource().equals(dataSource)){
                                    if(FocusReportNode364Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                        taskMap.put("nodeNameZH","组织验收(已完成整治)");
                                        nextNodeMapListRemain.add(taskMap);
                                    }else if(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)
                                            && StringUtils.isBlank(checkAccept)){
                                        //一级网格（一网成整治）-镇级处置（提交到组织验收）-组织验收（不通过）验收过的不再出现一级网格环节
                                        taskMap.put("nodeNameZH","一级网格处置(验收不通过)");
                                        nextNodeMapListRemain.add(taskMap);
                                    }
                                }
                            }else if(TOTDisposalResultEnum.APPLY_EXTESION.getDisposalResult().equals(disposalResult)){
                                //申请延期
                                //对应上一环节申请延期的 页面进行 同意延期、不同意延期区别
                                if(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    taskMap.put("nodeNameZH","一级网格处置");
                                    nextNodeMapListRemain.add(taskMap);
                                }
                            }else if(TOTDisposalResultEnum.STREET_HANDLE.getDisposalResult().equals(disposalResult)
                                    || (StringUtils.isNotBlank(checkAccept) && TOTDisposalResultEnum.CHECK_NOPASSED.getDisposalResult().equals(checkAccept))){
                                //未完成整治，提请镇级处置
                                //所有来源的，上一环节网格处置结果-未完成整治（内容③），或验收环节验收不通过的
                                //共有下一环节：市级三合一整治办处置task6
                                if(FocusReportNode364Enum.MUNICIPAL_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    //尚未完成整治
                                    taskMap.put("nodeNameZH","市级处置(尚未完成整治,提请市级处置)");
                                    nextNodeMapListRemain.add(taskMap);
                                }
                                //数据来源为 网格员发现上报01、12345的已完成整治
                                //下一环节保留：归档end1
                                if(TOTDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)
                                        || TOTDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource)){
                                   if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                        taskMap.put("nodeNameZH","归档(已完成整治)");
                                        nextNodeMapListRemain.add(taskMap);
                                    }
                                }else if(TOTDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(dataSource)
                                        || TOTDataSourceEnum.UNANNOUNCED_VISIT.getDataSource().equals(dataSource)
                                        || TOTDataSourceEnum.MASSES_REPORT.getDataSource().equals(dataSource)){
                                    //数据来源为 03 04 05的已完成整治
                                    if(FocusReportNode364Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                        taskMap.put("nodeNameZH","组织验收(已完成整治)");
                                        nextNodeMapListRemain.add(taskMap);
                                    }
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
            }else if(FocusReportNode364Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                //只有市级采集、群众举报的才会走到组织验收环节
                List<Map<String, Object>> nextNodeMapListRemain = new ArrayList<>();
                Map<String,Object> taskMap = null;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (int i = 0, len = nextNodeMapList.size(); i < len; i++) {
                        taskMap = nextNodeMapList.get(i);

                        if (CommonFunctions.isNotBlank(taskMap, "nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }

                        if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","办结归档(验收通过)");
                            nextNodeMapListRemain.add(taskMap);
                        }else if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","镇级处置(验收不通过)");
                            nextNodeMapListRemain.add(taskMap);
                        }
                    }
                }

                nextNodeMapList = nextNodeMapListRemain;
                if(nextNodeMapList != null && nextNodeMapList.size() == 1){
                    isChooseDefaultRadio = true;
                }
                initMap.put("nextTaskNodes",nextNodeMapList);
            }
        }else if(isAble2Handle && FocusReportNode36401Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)){
            if(FocusReportNode36401Enum.PERIODIC_REPORTS_HDSTATUS_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                List<Map<String, Object>> nextNodeMapList = new ArrayList<>();

                //构造处理中虚拟节点
                Map<String, Object> node = new HashMap<>();
                node.put("nodeId",-1);
                node.put("nodeName",ConstantValue.HANDLING_TASK_CODE);
                node.put("nodeNameZH",ConstantValue.HANDLING_TASK_NAME + "(报告隐患状态)");
                node.put("dynamicSelect",null);//提交时不需要选择办理人员
                node.put("nodeType","1");
                node.put("transitionCode","__E0__");

                nextNodeMapList.add(node);
                initMap.put("nextTaskNodes",nextNodeMapList);
                isChooseDefaultRadio = true;
            }
        }

        initMap.put("isEditableNode", isEditableNode);
        initMap.put("isChooseDefaultRadio", isChooseDefaultRadio);
        initMap.put("userSelectedLimit", userSelectedLimit);

        return initMap;
    }

    @Override
    public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params) throws Exception {

        params.put("isQuerySuperviseList",true);

        //获取处理环节信息（子流程的展示要在此获取 拼接 展示在主流程）
        List<Map<String, Object>> resultMapList = super.capHandledTaskInfoMap(instanceId, order, params);

        //获取环节办理详情信息
        if(resultMapList != null && resultMapList.size() > 0 && IWorkflow4BaseService.SQL_ORDER_DESC.equals(order)){
            for(Map<String,Object> task:resultMapList){

                List<Map<String, Object>> subAndReceivedTaskList = hessianFlowService.queryTaskDetails(String.valueOf(task.get("TASK_ID")), order),
                        timeAndRemarkList = new ArrayList<Map<String, Object>>();
                Map<String, Object> timeAndRemarkMap = new HashMap<String, Object>();

                //开启子流程的节点 去除父任务节点的办理意见
                /*if(FocusReportNode364Enum.SCREENING_FEEDBACK_NODE_NAME.getTaskNodeName().equals(task.get("TASK_CODE")) && CommonFunctions.isNotBlank(task,"REMARKS")){
                    task.remove("REMARKS");
                }*/

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

        return resultMapList;
    }

    /**
     * 判断是否需要添加子流程信息
     * @param formTypeIdStr	表单ID
     * @param taskName		可添加子流程的节点名称
     * @param listType		列表类型
     * @param params
     * @return
     */
    @Override
    protected boolean isAble2ShowSubPro(String formTypeIdStr, String taskName, String listType, Map<String, Object> params) {
        String LIST_TYPE_TODO = "2";//待办列表

        return CommonFunctions.isNotBlank(params, "TASK_ID")
                && FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                && (!LIST_TYPE_TODO.equals(listType)
                        && (FocusReportNode364Enum.FIND_NODE_NAME.getTaskNodeName().equals(taskName)
                        || FocusReportNode364Enum.VERIFY_FEEDBACK_NODE_NAME.getTaskNodeName().equals(taskName))
                    )
                || (LIST_TYPE_TODO.equals(listType) && FocusReportNode364Enum.FIND_NODE_NAME.getTaskNodeName().equals(taskName));
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
        if(ReportTypeEnum.threeOneTreatment.getReportTypeIndex().equals(reportType)) {
            workflowName = "三合一整治流程";

            if(FocusReportNode36401Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
                workflowName = "三合一整治流程_隐患状态跟踪";
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
    @Override
    protected String findWfTypeId(Long reportId, UserInfo userInfo, Map<String, Object> params) {
        String reportType = null, wfTypeId = null;

        if(CommonFunctions.isNotBlank(params, "reportType")) {
            reportType = params.get("reportType").toString();
        } else {
            throw new IllegalArgumentException("缺少上报类型【reportType】！");
        }

        //后续转功能配置
        if(ReportTypeEnum.threeOneTreatment.getReportTypeIndex().equals(reportType)) {
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
        String  formTypeIdStr = null;//curNodeName = null,

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

//        if(CommonFunctions.isNotBlank(params, "curNodeName")) {
//            curNodeName = params.get("curNodeName").toString();
//        }

        //进入决策类判断（致贫返贫监测暂时未启用决策）
        isDecisionMaking = FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) && false;

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
