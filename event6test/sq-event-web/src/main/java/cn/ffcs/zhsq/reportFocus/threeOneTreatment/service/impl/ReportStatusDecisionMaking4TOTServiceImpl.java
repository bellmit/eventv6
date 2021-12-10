package cn.ffcs.zhsq.reportFocus.threeOneTreatment.service.impl;

import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportStatusDecisionMaking4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: zhangtc
 * @Date: 2021/8/17 16:19
 */
@Service(value = "reportStatusDecisionMaking4TOTService")
public class ReportStatusDecisionMaking4TOTServiceImpl extends ReportStatusDecisionMaking4TwoVioPreServiceImpl {

    @Autowired
    private IBaseWorkflowService baseWorkflowService;

    @Autowired
    private IEventDisposalWorkflowService eventDisposalWorkflowService;

    @Autowired
    private IReportIntegrationService reportIntegrationService;

    /**
     * 获取上报状态
     */
    @Override
    protected String capReportStaus(Map<String, Object> params) {
        String nextNodeName = null, reportStatus = null;
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
                Map<String, Object> curDataMap = eventDisposalWorkflowService.curNodeData(instanceId);

                if(proInstance != null) {
                    formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
                    nextNodeName = proInstance.getCurNode();
                    proStatus = proInstance.getStatus();
                }
            }

            if(FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                    || FocusReportNode36401Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {

                if(PRO_STATUS_END.equals(proStatus)) {
                    nextNodeName = FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName();
                } else if(!isRejectOperate && !isRecallOperate && CommonFunctions.isNotBlank(params, "nextNodeName")) {
                    nextNodeName = params.get("nextNodeName").toString();
                }

                if(FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
                    statusMap = initReportStatusMap();
                }else if(FocusReportNode36401Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)){
                    Map<String, Object> reportParams = new HashMap<String, Object>();
                    reportParams.putAll(params);
                    if(CommonFunctions.isNotBlank(params,"formId")){
                        reportParams.put("reportId", params.get("formId"));
                    }
                    reportParams.put("reportType", ReportTypeEnum.threeOneTreatment.getReportTypeIndex());
                    Map<String,Object> reportFocus = reportIntegrationService.findReportFocusByUUID(null, null, reportParams);

                    if(CommonFunctions.isNotBlank(reportFocus,"reportStatus")){
                        reportStatus = String.valueOf(reportFocus.get("reportStatus"));
                    }else{
                        statusMap = initReportStatusMap_36401();
                    }
                }

                if(CommonFunctions.isNotBlank(statusMap, nextNodeName)) {
                    reportStatus = statusMap.get(nextNodeName);
                }
            }
        }

        return reportStatus;
    }

    /**
     * 构造上报初始化状态
     * @return
     */
    @Override
    protected Map<String, String> initReportStatusMap() {

        Map<String, String> statusMap = new HashMap<String, String>();

        statusMap.put(FocusReportNode364Enum.START_NODE_NAME.getTaskNodeName(), TOTReportStatusEnum.DRAFT_STATUS.getReportStatus());
        statusMap.put(FocusReportNode364Enum.FIND_NODE_NAME.getTaskNodeName(), TOTReportStatusEnum.DRAFT_STATUS.getReportStatus());

        statusMap.put(FocusReportNode364Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName(), TOTReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());

        statusMap.put(FocusReportNode364Enum.VERIFY_FEEDBACK_NODE_NAME.getTaskNodeName(), TOTReportStatusEnum.VERIFY_FEEDBACK_STATUS.getReportStatus());

        statusMap.put(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName(), TOTReportStatusEnum.COMMUNITY_HANDLE_STATUS.getReportStatus());

        statusMap.put(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName(), TOTReportStatusEnum.STREET_HANDLE_STATUS.getReportStatus());

        statusMap.put(FocusReportNode364Enum.MUNICIPAL_HANDLE_NODE_NAME.getTaskNodeName(), TOTReportStatusEnum.MUNICIPAL_HANDLE_STATUS.getReportStatus());

        statusMap.put(FocusReportNode364Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName(), TOTReportStatusEnum.ACCEPTANCE_STATUS.getReportStatus());

        statusMap.put(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName(), TOTReportStatusEnum.END_STATUS.getReportStatus());

        return statusMap;
    }

    /**
     * 南安三合一整治_子流程
     * @return
     */
    private Map<String, String> initReportStatusMap_36401() {
        Map<String, String> statusMap = new HashMap<String, String>();

        statusMap.put(FocusReportNode36401Enum.START_NODE_NAME.getTaskNodeName(), TOTReportStatusEnum.DRAFT_STATUS.getReportStatus());
        statusMap.put(FocusReportNode36401Enum.DECOMPOSITION_TRACKING_TASK_NODE_NAME.getTaskNodeName(), TOTReportStatusEnum.COMMUNITY_HANDLE_STATUS.getReportStatus());
        statusMap.put(FocusReportNode36401Enum.PERIODIC_REPORTS_HDSTATUS_NODE_NAME.getTaskNodeName(), TOTReportStatusEnum.COMMUNITY_HANDLE_STATUS.getReportStatus());

        statusMap.put(FocusReportNode36401Enum.END_NODE_NAME.getTaskNodeName(), TOTReportStatusEnum.END_STATUS.getReportStatus());

        return statusMap;
    }

}
