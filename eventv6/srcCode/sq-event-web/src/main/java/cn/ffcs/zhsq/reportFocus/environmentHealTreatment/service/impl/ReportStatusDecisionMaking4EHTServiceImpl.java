package cn.ffcs.zhsq.reportFocus.environmentHealTreatment.service.impl;

import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportStatusDecisionMaking4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
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
@Service(value = "reportStatusDecisionMaking4EHTService")
public class ReportStatusDecisionMaking4EHTServiceImpl extends ReportStatusDecisionMaking4TwoVioPreServiceImpl {

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
            }

            if(FocusReportNode361Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {

                if(PRO_STATUS_END.equals(proStatus)) {
                    nextNodeName = FocusReportNode361Enum.END_NODE_NAME.getTaskNodeName();
                } else if(!isRejectOperate && !isRecallOperate && CommonFunctions.isNotBlank(params, "nextNodeName")) {
                    nextNodeName = params.get("nextNodeName").toString();
                }

                if(FocusReportNode361Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
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

        statusMap.put(FocusReportNode361Enum.START_NODE_NAME.getTaskNodeName(), EHTReportStatusEnum.DRAFT_STATUS.getReportStatus());
        statusMap.put(FocusReportNode361Enum.FIND_NODE_NAME.getTaskNodeName(), EHTReportStatusEnum.DRAFT_STATUS.getReportStatus());

        statusMap.put(FocusReportNode361Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName(), EHTReportStatusEnum.DISTRUBUTE_STATUS.getReportStatus());

        statusMap.put(FocusReportNode361Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName(), EHTReportStatusEnum.COMMUNITY_HANDLE_STATUS.getReportStatus());

        statusMap.put(FocusReportNode361Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName(), EHTReportStatusEnum.STREET_HANDLE_STATUS.getReportStatus());

        statusMap.put(FocusReportNode361Enum.ADMINISTRATION_BUREAU_HANDLE_NODE_NAME.getTaskNodeName(), EHTReportStatusEnum.ADMINISTRATION_BUREAU_HANDLE_STATUS.getReportStatus());

        statusMap.put(FocusReportNode361Enum.RELEVANT_UNIT_HANDLE_NODE_NAME.getTaskNodeName(), EHTReportStatusEnum.RELEVANT_UNIT_HANDLE_STATUS.getReportStatus());

        //statusMap.put(ConstantValue.HANDLING_TASK_CODE, EHTReportStatusEnum.CONTINUOUS_MONITORING_STATUS.getReportStatus());

        statusMap.put(FocusReportNode361Enum.END_NODE_NAME.getTaskNodeName(), EHTReportStatusEnum.END_STATUS.getReportStatus());

        return statusMap;
    }
}
