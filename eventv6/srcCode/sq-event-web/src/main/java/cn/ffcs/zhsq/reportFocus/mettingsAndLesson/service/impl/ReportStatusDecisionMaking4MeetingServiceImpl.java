package cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl;

import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportStatusDecisionMaking4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 流域水质状态决策类
 * 必填参数
 * 		curNodeName		当前环节名称
 * 		nextNodeName	下一环节名称
 * 		reportId/reportUUID		上报id/上报UUID
 *
 * 选填参数
 * 		operateUserInfo	操作用户，类型为cn.ffcs.uam.bo.UserInfo
 * 		instanceId		流程实例id
 * @ClassName:   ReportStatusDecisionMaking4MeetingServiceImpl
 * @Author: zhangtc
 * @Date: 2020/12/4 10:23
 */
@Service(value = "reportStatusDecisionMaking4MeetingService")
public class ReportStatusDecisionMaking4MeetingServiceImpl extends ReportStatusDecisionMaking4TwoVioPreServiceImpl {
    @Autowired
    private IBaseWorkflowService baseWorkflowService;

    /**
     * 获取上报状态
     */
    protected String capReportStaus(Map<String, Object> params) {
        String curNodeName = null, reportStatus = null;
        Map<String, String> statusMap = null;

        if(CommonFunctions.isNotBlank(params, "instanceId")) {
            Long instanceId = null;
            String formTypeIdStr = null, proStatus = null, PRO_STATUS_END = "2";
            boolean isRejectOperate = false, isRecallOperate = false;

            try {
                instanceId = Long.valueOf(params.get("instanceId").toString());
            } catch(NumberFormatException e) {}

            if(CommonFunctions.isNotBlank(params, "isRejectOperate")) {
                isRejectOperate = Boolean.valueOf(params.get("isRejectOperate").toString());
            }
            
            if(CommonFunctions.isNotBlank(params, "isRecallOperate")) {
            	isRecallOperate = Boolean.valueOf(params.get("isRecallOperate").toString());
            }

            if(instanceId != null && instanceId > 0) {
                ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);

                if(proInstance != null) {
                    formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
                    curNodeName = proInstance.getCurNode();
                    proStatus = proInstance.getStatus();
                }
            }

            if(PRO_STATUS_END.equals(proStatus)) {
                curNodeName = FocusReportNode355Enum.END_NODE_NAME.getTaskNodeName();
            } else if(!isRejectOperate && !isRecallOperate && CommonFunctions.isNotBlank(params, "nextNodeName")) {
                curNodeName = params.get("nextNodeName").toString();
            }

            if(FocusReportNode355Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
                statusMap = initReportStatusMap();
            }

            if(CommonFunctions.isNotBlank(statusMap, curNodeName)) {
                reportStatus = statusMap.get(curNodeName);
            }
        }

        return reportStatus;
    }

    /**
     * 南安三会一课流程-网格员
     * 构造上报初始化状态
     * @return
     */
    @Override
    protected Map<String, String> initReportStatusMap() {
        Map<String, String> statusMap = new HashMap<String, String>();

        statusMap.put(FocusReportNode355Enum.START_NODE_NAME.getTaskNodeName(), MeetingReportStatusEnum.DRAFT_STATUS.getReportStatus());
        statusMap.put(FocusReportNode355Enum.COLLECT_MEETING.getTaskNodeName(), MeetingReportStatusEnum.DRAFT_STATUS.getReportStatus());

        statusMap.put(FocusReportNode355Enum.GRID_REPORT.getTaskNodeName(), MeetingReportStatusEnum.DRAFT_STATUS.getReportStatus());
        statusMap.put(FocusReportNode355Enum.COMMUNITY_REPORT.getTaskNodeName(), MeetingReportStatusEnum.DRAFT_STATUS.getReportStatus());
        statusMap.put(FocusReportNode355Enum.STREET_COMMITTEE_REPORT.getTaskNodeName(), MeetingReportStatusEnum.DRAFT_STATUS.getReportStatus());
        statusMap.put(FocusReportNode355Enum.STREET_GROUPLEADER_REPORT.getTaskNodeName(), MeetingReportStatusEnum.DRAFT_STATUS.getReportStatus());
        statusMap.put(FocusReportNode355Enum.CITY_COMMITTEE_TYPE.getTaskNodeName(), MeetingReportStatusEnum.DRAFT_STATUS.getReportStatus());
        statusMap.put(FocusReportNode355Enum.CITY_GROUPLEADER_TYPE.getTaskNodeName(), MeetingReportStatusEnum.DRAFT_STATUS.getReportStatus());

        statusMap.put(FocusReportNode355Enum.END_NODE_NAME.getTaskNodeName(), MeetingReportStatusEnum.END_STATUS.getReportStatus());

        statusMap.put(FocusReportNode355Enum.PASS_NODE_TASK2.getTaskNodeName(), MeetingReportStatusEnum.ACCEPTANCE_CHECK_STATUS.getReportStatus());
        statusMap.put(FocusReportNode355Enum.PASS_NODE_TASK3.getTaskNodeName(), MeetingReportStatusEnum.ACCEPTANCE_CHECK_STATUS.getReportStatus());
        statusMap.put(FocusReportNode355Enum.PASS_NODE_TASK4.getTaskNodeName(), MeetingReportStatusEnum.ACCEPTANCE_CHECK_STATUS.getReportStatus());
        statusMap.put(FocusReportNode355Enum.PASS_NODE_TASK5.getTaskNodeName(), MeetingReportStatusEnum.ACCEPTANCE_CHECK_STATUS.getReportStatus());
        statusMap.put(FocusReportNode355Enum.PASS_NODE_TASK6.getTaskNodeName(), MeetingReportStatusEnum.ACCEPTANCE_CHECK_STATUS.getReportStatus());
        statusMap.put(FocusReportNode355Enum.PASS_NODE_TASK7.getTaskNodeName(), MeetingReportStatusEnum.ACCEPTANCE_CHECK_STATUS.getReportStatus());

        return statusMap;
    }
}
