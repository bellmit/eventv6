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
                throw new IllegalArgumentException("????????????id???"+ instanceId +"???????????????????????????????????????");
            }
        } else {
            throw new IllegalArgumentException("???????????????????????????id???");
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

        //????????????????????????????????????????????????????????? "?????????????????????_??????????????????"???
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

        //??????????????????????????????????????????????????????????????????
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

        //?????????: ??????????????? task1 ??????????????? task2 ?????????????????????????????????
        if(FocusReportNode36401Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
                && FocusReportNode36401Enum.DECOMPOSITION_TRACKING_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)
                && FocusReportNode36401Enum.PERIODIC_REPORTS_HDSTATUS_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
            OrgEntityInfoBO orgEntityInfo = null;
            String orgLevel = null;

            //?????????regionCode?????? ???????????????????????????????????????
            if(CommonFunctions.isBlank(extraParam, "regionCode") && CommonFunctions.isNotBlank(reportFocus, "regionCode")){
                orgEntityInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(reportFocus.get("regionCode").toString());
            }else if(CommonFunctions.isNotBlank(extraParam, "regionCode")){
                //???????????? ????????????????????????????????????
                reportFocus.put("regionCode", extraParam.get("regionCode").toString());
                orgEntityInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(extraParam.get("regionCode").toString());
            }
            if(null != orgEntityInfo){
                orgLevel = orgEntityInfo.getChiefLevel();

                if(StringUtils.isBlank(orgLevel) || orgLevel.compareTo(ConstantValue.GRID_ORG_LEVEL) < 0){
                    //????????????????????????????????????????????? ??????????????????????????????????????????????????????????????????
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
                            throw new ZhsqEventException("???????????????????????????????????????????????????????????????????????????");
                        }
                    }
                }
                if(StringUtils.isBlank(orgLevel) || orgLevel.compareTo(ConstantValue.GRID_ORG_LEVEL) < 0){
                    throw new ZhsqEventException("??????????????????????????????"+ orgLevel +"?????????????????????????????????");
                }
            }else{
                throw new ZhsqEventException("???????????????????????????????????????");
            }
        }


        //??????????????? task3?????????????????????task4??? ??????????????????????????????????????????
        if(FocusReportNode364Enum.FORM_TYPE_ID.getTaskNodeName().equals(formTypeIdStr)
                && FocusReportNode364Enum.VERIFY_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curNodeName)
                && FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
            OrgEntityInfoBO orgEntityInfo = null;
            String orgLevel = null;

            //?????????regionCode?????? ???????????????????????????????????????
            if(CommonFunctions.isBlank(extraParam, "regionCode") && CommonFunctions.isNotBlank(reportFocus, "regionCode")){
                orgEntityInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(reportFocus.get("regionCode").toString());
            }else if(CommonFunctions.isNotBlank(extraParam, "regionCode")){
                //???????????? ????????????????????????????????????
                reportFocus.put("regionCode", extraParam.get("regionCode").toString());
                orgEntityInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(extraParam.get("regionCode").toString());
            }
            if(null != orgEntityInfo){
                orgLevel = orgEntityInfo.getChiefLevel();

                if(StringUtils.isBlank(orgLevel) || orgLevel.compareTo(ConstantValue.GRID_ORG_LEVEL) < 0){
                    //????????????????????????????????????????????? ??????????????????????????????????????????????????????????????????
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
                            throw new ZhsqEventException("???????????????????????????????????????????????????????????????????????????");
                        }
                    }
                }

                if(StringUtils.isBlank(orgLevel) || orgLevel.compareTo(ConstantValue.GRID_ORG_LEVEL) < 0){
                    throw new ZhsqEventException("??????????????????????????????"+ orgLevel +"?????????????????????????????????");
                }
            }else{
                throw new ZhsqEventException("???????????????????????????????????????");
            }
        }

        //??????????????? ??????????????? ????????????-????????????????????????????????? 01 02??????????????????-????????????????????????????????? 03-05???
        if(FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                && (
                        (FocusReportNode364Enum.VERIFY_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curNodeName)
                        && FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName))
                     || (FocusReportNode364Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)
                                && FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)
                                )
                )
        ){
            //????????????????????????????????????????????????
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
                        subProParams.remove("instanceId");//????????????????????????????????????????????????
                        subProParams.put("formTypeId", FocusReportNode36401Enum.FORM_TYPE_ID.toString());

                        removeUserInfoList.add(nextUserInfo);
                        result = this.startWorkflow4Report(proInstance.getFormId(), nextUserInfo, subProParams);

                        //????????????????????? ??????mq??????
                        if(result){
                            sendSubFlowRMQmMsg(reportId,formTypeIdStr,FocusReportNode36401Enum.FORM_TYPE_ID.toString(),instanceId,subProParams.get("instanceId"));
                        }
                    }
                }

                //??????????????????????????????????????????????????????????????????????????????
                nextUserInfoList.removeAll(removeUserInfoList);
            }
        }

        //?????????????????????
        if(FocusReportNode36401Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)){

            if(FocusReportNode36401Enum.DECOMPOSITION_TRACKING_TASK_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                isSubByParent = false;
                if(CommonFunctions.isBlank(extraParam, "instanceId")) {
                    extraParam.put("instanceId", instanceId);
                }
                flag = workflow4BaseService.subWorkflow4Base(null, null, null, nextNodeName, nextUserInfoList, userInfo, extraParam);
            }else if(FocusReportNode36401Enum.PERIODIC_REPORTS_HDSTATUS_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                isSubByParent = false;
                //??????????????? ???????????????????????? ?????????????????? ??????????????????????????????????????????????????????????????????
                Map<String,Object> resultMap = eventDisposalWorkflowService.saveHandlingTask(reportId, instanceId, nextNodeName, advice, userInfo,extraParam);
                flag = resultMap != null;
                //?????????????????????????????? ??????????????? ???????????????+3???
                //????????????????????????

                //???????????????????????????
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
            //??????
            flag = super.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);

            //??????????????????????????? ??????????????????????????? ????????????????????????
            if(flag && FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                    && (FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName))) {
                //?????????????????????????????????????????????
                endAllSubProInstance(instanceId, null,null);

                Map<String, Object> updateStatusParams = new HashMap<String, Object>();
                updateStatusParams.putAll(extraParam);
                updateStatusParams.put("nextNodeName", FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName());
                updateStatusParams.put("instanceId", instanceId);

                updateReportStatus(null, instanceId, userInfo, updateStatusParams);
            }
        }

        //??????????????????????????? ????????????????????????
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
                        //???????????????????????????????????????????????????
                        if(FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                                && CommonFunctions.isNotBlank(extraParam, "extensionDate")) {
                            reportFocus.put("extensionDate", extraParam.get("extensionDate").toString());
                        }

                        //??????????????????????????? ????????????????????????
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
                            //????????????????????????
                            reportFocus.put("isSaveResMarkerInfo", true);

                            //????????????
                            if(CommonFunctions.isNotBlank(extraParam, "dynamicDictId")) {
                                hiddenDangerType = extraParam.get("dynamicDictId").toString();
                            }
                        }else if(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                            if(CommonFunctions.isNotBlank(extraParam,"dataSource")){
                                dataSource = String.valueOf(extraParam.get("dataSource"));
                            }else if(CommonFunctions.isNotBlank(reportFocus,"dataSource")) {
                                dataSource = String.valueOf(reportFocus.get("dataSource"));
                            }
                            //????????????????????????
                            //?????? 01???12345 02????????? 05
                            if(TOTDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)
                                    ||TOTDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource)
                                    ||TOTDataSourceEnum.MASSES_REPORT.getDataSource().equals(dataSource)){

                                if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
                                    if(CommonFunctions.isNotBlank(extraParam, "dynamicDictId")) {
                                        disposalResult =  extraParam.get("dynamicDictId").toString();
                                    }
                                }else if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
                                    //????????????????????? -???????????????
                                    disposalResult = TOTDisposalResultEnum.REMEDIATION_COMPLETE.getDisposalResult();
                                    //reportFocus.put("disposalResult", "0");
                                }
                            }else{
                                if(CommonFunctions.isNotBlank(extraParam, "dynamicDictId")) {
                                    disposalResult =  extraParam.get("dynamicDictId").toString();
                                }
                            }
                            if(StringUtils.isBlank(disposalResult)){
                                throw new IllegalArgumentException("??????????????????????????????disposalResult??????????????????");
                            }else /*if(TOTDisposalResultEnum.APPLY_EXTESION.getDisposalResult().equals(disposalResult)
                                    ||TOTDisposalResultEnum.REMEDIATION_COMPLETE.getDisposalResult().equals(disposalResult))*/{
                                //????????????????????????????????????????????????????????? ????????????????????????
                                if(CommonFunctions.isNotBlank(extraParam,"communityDuedate")){
                                    reportFocus.put("communityDuedate", DateUtils.convertStringToDate(String.valueOf(extraParam.get("communityDuedate")),DateUtils.PATTERN_DATE));
                                }else{
                                    throw new IllegalArgumentException("??????????????????????????????????????????communityDuedate??????????????????");
                                }
                            }
                            reportFocus.put("disposalResult", disposalResult);
                        }else if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                            if(CommonFunctions.isNotBlank(extraParam,"streetDuedate")){
                                reportFocus.put("streetDuedate", DateUtils.convertStringToDate(String.valueOf(extraParam.get("streetDuedate")),DateUtils.PATTERN_DATE));
                            }
                        }else if(FocusReportNode364Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                            //???????????? 1
                            if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
                                reportFocus.put("checkAccept",TOTDisposalResultEnum.CHECK_PASSED.getDisposalResult());
                            }else if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nextNodeName)){
                                //??????????????? 0
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

        //????????????????????? ??????????????????????????? ????????????????????????
        if(flag) {
            //?????????????????????????????????????????????
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
                isAble2Handle = false,	//?????????????????????
                isChooseDefaultRadio = true,//??????????????????????????????
                userSelectedLimit=true;//????????????????????????????????????

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

            //???????????? ????????????????????????
            if(StringUtils.isBlank(dataSource)){
                throw new IllegalArgumentException("??????????????????????????????dataSource????????????????????????");
            }

            //???????????? ??????????????????????????????????????????(task4-task7) ??????????????????
            if(FocusReportNode364Enum.FIND_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                List<Map<String, Object>> nextNodeMapListRemain = new ArrayList<>();
                Map<String,Object> taskMap = null;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (int i=0 ,len = nextNodeMapList.size(); i<len; i++){
                        taskMap = nextNodeMapList.get(i);

                        if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }

                        //??????????????? ?????????????????????01 ??????????????????????????????????????? task2?????????end1
                        if(TOTDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)){
                            if(FocusReportNode364Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                nextNodeMapListRemain.add(taskMap);
                            }
                            if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                nextNodeMapListRemain.add(taskMap);
                            }
                        }else if(TOTDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource)){
                            //??????????????? 02 ??? ????????????????????????????????? task3 ?????????
                            if(FocusReportNode364Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                nextNodeMapListRemain.add(taskMap);
                            }
                        }else if( TOTDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(dataSource)
                                || TOTDataSourceEnum.UNANNOUNCED_VISIT.getDataSource().equals(dataSource)
                                || TOTDataSourceEnum.MASSES_REPORT.getDataSource().equals(dataSource)){
                            //??????????????? 03 04 05 ??? ??????????????????????????????????????? task4 ?????????
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
                            taskMap.put("nodeNameZH","??????????????????(??????)");
                        }else if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","??????(?????????)");
                        }
                    }
                }
            }else if(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                List<Map<String, Object>> nextNodeMapListRemain = new ArrayList<>();
                Map<String,Object> taskMap = null;
                //????????????
                if(StringUtils.isNotBlank(dataSource)){
                    if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                        for (int i=0 ,len = nextNodeMapList.size(); i<len; i++){
                            taskMap = nextNodeMapList.get(i);

                            if(CommonFunctions.isNotBlank(taskMap,"nodeName")) {
                                nodeName = String.valueOf(taskMap.get("nodeName"));
                            }

                            //??????????????? ?????????????????????01???12345 ?????????????????????????????????task5 ??????end1
                            if(TOTDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)
                                    || TOTDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource)){
                                if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    taskMap.put("nodeNameZH","????????????(???????????????)");
                                    nextNodeMapListRemain.add(taskMap);
                                }else if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    taskMap.put("nodeNameZH","??????(???????????????)");
                                    nextNodeMapListRemain.add(taskMap);
                                }
                            }else if(TOTDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(dataSource)
                                    || TOTDataSourceEnum.UNANNOUNCED_VISIT.getDataSource().equals(dataSource)
                                    || TOTDataSourceEnum.MASSES_REPORT.getDataSource().equals(dataSource)){
                                //??????????????? 03 04 05 ??? ????????????????????????????????? task5 ?????????
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
                    throw new ZhsqEventException("???????????????????????????????????????????????????");
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

                            //???????????????????????????0?????????????????????1??????????????????2?????????????????????
                            //??????????????????????????????????????????-???????????????
                            if(TOTDisposalResultEnum.REMEDIATION_COMPLETE.getDisposalResult().equals(disposalResult)){
                                //???????????????
                                //03 04 05 ?????????????????????????????? ????????????????????????????????????????????? ????????? ???????????????
                                // (??????????????????????????????????????????????????????????????????????????????????????????????????????????????????)
                                if(TOTDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(dataSource)
                                        || TOTDataSourceEnum.UNANNOUNCED_VISIT.getDataSource().equals(dataSource)
                                        || TOTDataSourceEnum.MASSES_REPORT.getDataSource().equals(dataSource)){
                                    if(FocusReportNode364Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                        taskMap.put("nodeNameZH","????????????(???????????????)");
                                        nextNodeMapListRemain.add(taskMap);
                                    }else if(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)
                                            && StringUtils.isBlank(checkAccept)){
                                        //?????????????????????????????????-???????????????????????????????????????-?????????????????????????????????????????????????????????????????????
                                        taskMap.put("nodeNameZH","??????????????????(???????????????)");
                                        nextNodeMapListRemain.add(taskMap);
                                    }
                                }
                            }else if(TOTDisposalResultEnum.APPLY_EXTESION.getDisposalResult().equals(disposalResult)){
                                //????????????
                                //????????????????????????????????? ???????????? ????????????????????????????????????
                                if(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    taskMap.put("nodeNameZH","??????????????????");
                                    nextNodeMapListRemain.add(taskMap);
                                }
                            }else if(TOTDisposalResultEnum.STREET_HANDLE.getDisposalResult().equals(disposalResult)
                                    || (StringUtils.isNotBlank(checkAccept) && TOTDisposalResultEnum.CHECK_NOPASSED.getDisposalResult().equals(checkAccept))){
                                //????????????????????????????????????
                                //????????????????????????????????????????????????-??????????????????????????????????????????????????????????????????
                                //???????????????????????????????????????????????????task6
                                if(FocusReportNode364Enum.MUNICIPAL_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                    //??????????????????
                                    taskMap.put("nodeNameZH","????????????(??????????????????,??????????????????)");
                                    nextNodeMapListRemain.add(taskMap);
                                }
                                //??????????????? ?????????????????????01???12345??????????????????
                                //???????????????????????????end1
                                if(TOTDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)
                                        || TOTDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource)){
                                   if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                        taskMap.put("nodeNameZH","??????(???????????????)");
                                        nextNodeMapListRemain.add(taskMap);
                                    }
                                }else if(TOTDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(dataSource)
                                        || TOTDataSourceEnum.UNANNOUNCED_VISIT.getDataSource().equals(dataSource)
                                        || TOTDataSourceEnum.MASSES_REPORT.getDataSource().equals(dataSource)){
                                    //??????????????? 03 04 05??????????????????
                                    if(FocusReportNode364Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                                        taskMap.put("nodeNameZH","????????????(???????????????)");
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
                //??????????????????????????????????????????????????????????????????
                List<Map<String, Object>> nextNodeMapListRemain = new ArrayList<>();
                Map<String,Object> taskMap = null;

                if(nextNodeMapList != null && nextNodeMapList.size() > 0) {
                    for (int i = 0, len = nextNodeMapList.size(); i < len; i++) {
                        taskMap = nextNodeMapList.get(i);

                        if (CommonFunctions.isNotBlank(taskMap, "nodeName")) {
                            nodeName = String.valueOf(taskMap.get("nodeName"));
                        }

                        if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","????????????(????????????)");
                            nextNodeMapListRemain.add(taskMap);
                        }else if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                            taskMap.put("nodeNameZH","????????????(???????????????)");
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

                //???????????????????????????
                Map<String, Object> node = new HashMap<>();
                node.put("nodeId",-1);
                node.put("nodeName",ConstantValue.HANDLING_TASK_CODE);
                node.put("nodeNameZH",ConstantValue.HANDLING_TASK_NAME + "(??????????????????)");
                node.put("dynamicSelect",null);//????????????????????????????????????
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

        //???????????????????????????????????????????????????????????? ?????? ?????????????????????
        List<Map<String, Object>> resultMapList = super.capHandledTaskInfoMap(instanceId, order, params);

        //??????????????????????????????
        if(resultMapList != null && resultMapList.size() > 0 && IWorkflow4BaseService.SQL_ORDER_DESC.equals(order)){
            for(Map<String,Object> task:resultMapList){

                List<Map<String, Object>> subAndReceivedTaskList = hessianFlowService.queryTaskDetails(String.valueOf(task.get("TASK_ID")), order),
                        timeAndRemarkList = new ArrayList<Map<String, Object>>();
                Map<String, Object> timeAndRemarkMap = new HashMap<String, Object>();

                //???????????????????????? ????????????????????????????????????
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
     * ???????????????????????????????????????
     * @param formTypeIdStr	??????ID
     * @param taskName		?????????????????????????????????
     * @param listType		????????????
     * @param params
     * @return
     */
    @Override
    protected boolean isAble2ShowSubPro(String formTypeIdStr, String taskName, String listType, Map<String, Object> params) {
        String LIST_TYPE_TODO = "2";//????????????

        return CommonFunctions.isNotBlank(params, "TASK_ID")
                && FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                && (!LIST_TYPE_TODO.equals(listType)
                        && (FocusReportNode364Enum.FIND_NODE_NAME.getTaskNodeName().equals(taskName)
                        || FocusReportNode364Enum.VERIFY_FEEDBACK_NODE_NAME.getTaskNodeName().equals(taskName))
                    )
                || (LIST_TYPE_TODO.equals(listType) && FocusReportNode364Enum.FIND_NODE_NAME.getTaskNodeName().equals(taskName));
    }

    /**
     * ?????????????????????
     * @param reportId	??????id
     * @param userInfo	????????????
     * @param params	????????????
     * @return
     */
    @Override
    protected String findWorkflowName(Long reportId, UserInfo userInfo, Map<String, Object> params) {

        String reportType = null, workflowName = null, formTypeIdStr = null;

        if(CommonFunctions.isNotBlank(params, "reportType")) {
            reportType = params.get("reportType").toString();
        } else {
            throw new IllegalArgumentException("?????????????????????reportType??????");
        }
        if(CommonFunctions.isNotBlank(params, "formTypeId")) {
            formTypeIdStr = params.get("formTypeId").toString();
        }

        //?????????????????????
        if(ReportTypeEnum.threeOneTreatment.getReportTypeIndex().equals(reportType)) {
            workflowName = "?????????????????????";

            if(FocusReportNode36401Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
                workflowName = "?????????????????????_??????????????????";
            }
        }

        return workflowName;
    }

    /**
     * ??????????????????
     * @param reportId	??????id
     * @param userInfo	????????????
     * @param params	????????????
     * @return
     */
    @Override
    protected String findWfTypeId(Long reportId, UserInfo userInfo, Map<String, Object> params) {
        String reportType = null, wfTypeId = null;

        if(CommonFunctions.isNotBlank(params, "reportType")) {
            reportType = params.get("reportType").toString();
        } else {
            throw new IllegalArgumentException("?????????????????????reportType??????");
        }

        //?????????????????????
        if(ReportTypeEnum.threeOneTreatment.getReportTypeIndex().equals(reportType)) {
            wfTypeId = "focus_report";
        }

        return wfTypeId;
    }

    /**
     * ???????????????????????????
     * @param instanceId	????????????id
     * @param userInfo		????????????
     * @param params		????????????
     * 			instanceId	????????????id
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
            throw new IllegalArgumentException("???????????????????????????id???");
        }

        proInstance = baseWorkflowService.findProByInstanceId(instanceId);

        if(proInstance == null) {
            throw new IllegalArgumentException("????????????id???"+ instanceId +"???????????????????????????????????????");
        }

        formTypeIdStr = String.valueOf(proInstance.getFormTypeId());

//        if(CommonFunctions.isNotBlank(params, "curNodeName")) {
//            curNodeName = params.get("curNodeName").toString();
//        }

        //??????????????????????????????????????????????????????????????????
        isDecisionMaking = FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr) && false;

        params.put("proInstance", proInstance);
        params.put("isDecisionMaking", isDecisionMaking);

        nodeMapList = super.findNextTaskNodes(instanceId, userInfo, params);

        return nodeMapList;
    }

    /**
     * ??????????????????
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
