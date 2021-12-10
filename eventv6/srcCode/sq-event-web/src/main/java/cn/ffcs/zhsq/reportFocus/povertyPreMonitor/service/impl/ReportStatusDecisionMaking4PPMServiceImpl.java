package cn.ffcs.zhsq.reportFocus.povertyPreMonitor.service.impl;

import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportStatusDecisionMaking4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * 必填参数
 * 		curNodeName		当前环节名称
 * 		nextNodeName	下一环节名称
 * 		reportId/reportUUID		上报id/上报UUID
 *
 * 选填参数
 * 		operateUserInfo	操作用户，类型为cn.ffcs.uam.bo.UserInfo
 * 		instanceId		流程实例id
 * @Author: zhangtc
 * @Date: 2021/6/1 16:45
 */
@Service(value = "reportStatusDecisionMaking4PPMService")
public class ReportStatusDecisionMaking4PPMServiceImpl extends ReportStatusDecisionMaking4TwoVioPreServiceImpl {

    @Autowired
    private IBaseWorkflowService baseWorkflowService;

    @Autowired
    private IEventDisposalWorkflowService eventDisposalWorkflowService;

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
                if(CommonFunctions.isNotBlank(curDataMap,"NODE_NAME")){
                    if(FocusReportNode360Enum.SCREENING_FEEDBACK_NODE_NAME.getTaskNodeName().equals(String.valueOf(curDataMap.get("NODE_NAME")))){
                        params.put("nextNodeName",FocusReportNode360Enum.SCREENING_FEEDBACK_NODE_NAME.getTaskNodeName());
                    }else if(FocusReportNode360Enum.DETERMINE_OBJECT_NODE_NAME.getTaskNodeName().equals(String.valueOf(curDataMap.get("NODE_NAME")))){
                        params.put("nextNodeName",FocusReportNode360Enum.DETERMINE_OBJECT_NODE_NAME.getTaskNodeName());
                    }
                }
            }

            if(FocusReportNode360Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {

                if(PRO_STATUS_END.equals(proStatus)) {
                    nextNodeName = FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName();
                } else if(!isRejectOperate && !isRecallOperate && CommonFunctions.isNotBlank(params, "nextNodeName")) {
                    nextNodeName = params.get("nextNodeName").toString();
                }

                if(FocusReportNode360Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
                    statusMap = initReportStatusMap();
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

        statusMap.put(FocusReportNode360Enum.START_NODE_NAME.getTaskNodeName(), PPMReportStatusEnum.DRAFT_STATUS.getReportStatus());
        statusMap.put(FocusReportNode360Enum.FIND_NODE_NAME.getTaskNodeName(), PPMReportStatusEnum.DRAFT_STATUS.getReportStatus());

        statusMap.put(FocusReportNode360Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName(), PPMReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());

        statusMap.put(FocusReportNode360Enum.GRID_FEEDBACK_NODE_NAME.getTaskNodeName(), PPMReportStatusEnum.FEEDBACK_STATUS.getReportStatus());

        statusMap.put(FocusReportNode360Enum.COMMUNITY_COMMENT_NODE_NAME.getTaskNodeName(), PPMReportStatusEnum.COMMUNITY_COMMENT_STATUS.getReportStatus());

        statusMap.put(FocusReportNode360Enum.STREET_AUDIT_NODE_NAME.getTaskNodeName(), PPMReportStatusEnum.STREET_AUDIT_COMMENT_STATUS.getReportStatus());

        statusMap.put(FocusReportNode360Enum.MUNICIPAL_SEND_NODE_NAME.getTaskNodeName(), PPMReportStatusEnum.MUNICIPAL_SEND_STATUS.getReportStatus());

        statusMap.put(FocusReportNode360Enum.SCREENING_FEEDBACK_NODE_NAME.getTaskNodeName(), PPMReportStatusEnum.SCREENING_FEEDBACK_STATUS.getReportStatus());

        statusMap.put(FocusReportNode360Enum.DETERMINE_OBJECT_NODE_NAME.getTaskNodeName(), PPMReportStatusEnum.DETERMINE_OBJECT_STATUS.getReportStatus());

        statusMap.put(FocusReportNode360Enum.DE_MONITORING_NODE_NAME.getTaskNodeName(), PPMReportStatusEnum.DE_MONITORING_STATUS.getReportStatus());

        statusMap.put(ConstantValue.HANDLING_TASK_CODE, PPMReportStatusEnum.CONTINUOUS_MONITORING_STATUS.getReportStatus());

        statusMap.put(FocusReportNode360Enum.END_NODE_NAME.getTaskNodeName(), PPMReportStatusEnum.END_STATUS.getReportStatus());

        return statusMap;
    }

}
