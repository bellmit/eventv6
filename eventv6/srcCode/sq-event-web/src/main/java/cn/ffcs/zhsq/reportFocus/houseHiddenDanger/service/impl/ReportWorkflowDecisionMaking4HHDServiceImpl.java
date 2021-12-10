package cn.ffcs.zhsq.reportFocus.houseHiddenDanger.service.impl;

import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:房屋安全隐患节点扭转决策类
 * @Author: zhangtc
 * @Date: 2021/4/27 15:28
 */
@Service(value = "reportWorkflowDecisionMaking4HHDService")
public class ReportWorkflowDecisionMaking4HHDServiceImpl implements IWorkflowDecisionMakingService<String> {

    @Autowired
    private IReportFocusService reportFocusService;

    @Autowired
    private IBaseWorkflowService baseWorkflowService;

    @Autowired
    private OrgEntityInfoOutService orgEntityInfoService;

    @Override
    public String makeDecision(Map<String, Object> params) throws Exception {
        String curNodeName = null, nextNodeName = null, dataSource = null,riskType = null,regionCode = null,formTypeIdStr = null;;
        params = params == null ? new HashMap<String, Object>() : params;
        Map<String, Object> reportMap = null;
        OrgEntityInfoBO orgEntityInfo = null;
        String orgLevel = null;
        ProInstance proInstance = null;

        if(CommonFunctions.isNotBlank(params, "curNodeName")) {
            curNodeName = params.get("curNodeName").toString();
        }

        if(CommonFunctions.isNotBlank(params, "riskType")) {
            riskType = params.get("riskType").toString();
        }
        if(CommonFunctions.isNotBlank(params, "dataSource")) {
            dataSource = params.get("dataSource").toString();
        }
        if(CommonFunctions.isNotBlank(params, "regionCode")) {
            regionCode = params.get("regionCode").toString();
        }
        if(CommonFunctions.isNotBlank(params, "proInstance")) {
            proInstance = (ProInstance) params.get("proInstance");

            if(proInstance != null) {
                formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
            }
        }else if(CommonFunctions.isNotBlank(params, "instanceId")){
            proInstance = baseWorkflowService.findProByInstanceId(Long.valueOf(String.valueOf(params.get("instanceId"))));
        }else {
            throw new IllegalArgumentException("缺少有效的流程实例id！");
        }

        if(StringUtils.isBlank(riskType) || StringUtils.isBlank(dataSource) || StringUtils.isBlank(regionCode)){
            if(CommonFunctions.isNotBlank(params, "reportId") || CommonFunctions.isNotBlank(params, "reportUUID")) {

                if(CommonFunctions.isBlank(params, "reportType")) {
                    params.put("reportType", ReportTypeEnum.houseHiddenDanger.getReportTypeIndex());
                }

                reportMap = reportFocusService.findReportFocusByUUID(null, null, params);

                if(StringUtils.isBlank(dataSource) && CommonFunctions.isNotBlank(reportMap, "dataSource")) {
                    dataSource = reportMap.get("dataSource").toString();
                }
                if(StringUtils.isBlank(riskType) && CommonFunctions.isNotBlank(reportMap, "riskType")) {
                    riskType = reportMap.get("riskType").toString();
                }
                if(StringUtils.isBlank(regionCode) && CommonFunctions.isNotBlank(reportMap, "regionCode")) {
                    regionCode = reportMap.get("regionCode").toString();
                }
            }
        }

        if(StringUtils.isNotBlank(regionCode)){
            orgEntityInfo = orgEntityInfoService.selectOrgEntityInfoByOrgCode(regionCode);
        }
        if(null != orgEntityInfo){
            orgLevel = orgEntityInfo.getChiefLevel();
        }else {
            throw new IllegalArgumentException("缺少有效的地域信息！");
        }

        if(FocusReportNode35301Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)){
            if(FocusReportNode35301Enum.FIND_HHD_NODE_NAME.getTaskNodeName().equals(curNodeName)
                    || FocusReportNode35301Enum.VERIFY_DECISION_NODE_NAME.getTaskNodeName().equals(curNodeName)) {
                if(StringUtils.isNotBlank(orgLevel)){
                    //无法明确房屋所在网格的情形(所属区域层级为 乡镇街道)
                    if(ConstantValue.STREET_ORG_LEVEL.equals(orgLevel)){
                        //来源是群众举报 02 task6
                        if(HHDDataSourceEnum.MASSES_REPORT.getDataSource().equals(dataSource)) {
                            nextNodeName = FocusReportNode35301Enum.MASSES_REPORT_NODE_NAME.getTaskNodeName();
                        } else if(HHDDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(dataSource)){
                            //上级交办 03 task7
                            nextNodeName = FocusReportNode35301Enum.ASSIGNED_SUPERIOR_NODE_NAME.getTaskNodeName();
                        }else if(HHDDataSourceEnum.FIRE_APPROVAL.getDataSource().equals(dataSource)){
                            //消防手续审批 05 task8
                            nextNodeName = FocusReportNode35301Enum.FIREPROTECTION_NODE_NAME.getTaskNodeName();
                        }else if(HHDDataSourceEnum.BUSSINESS_LICENSE_PROCESS.getDataSource().equals(dataSource)){
                            //营业执照办理 07 task9
                            nextNodeName = FocusReportNode35301Enum.BUSINESS_LICENSE_NODE_NAME.getTaskNodeName();
                        }
                    }else {
                        nextNodeName = FocusReportNode35301Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName();
                    }
                }else{
                    nextNodeName = FocusReportNode35301Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName();
                }
            }
        }else if(FocusReportNode35302Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)){
            if(FocusReportNode35302Enum.FIND_HHD_NODE_NAME.getTaskNodeName().equals(curNodeName)
                    || FocusReportNode35302Enum.VERIFY_DECISION_NODE_NAME.getTaskNodeName().equals(curNodeName)){
                //房屋安全隐患 突发
                if(StringUtils.isNotBlank(orgLevel)){
                    //未精确到村社区、网格 02 task6
                    if(ConstantValue.STREET_ORG_LEVEL.equals(orgLevel)){
                        nextNodeName = FocusReportNode35302Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName();
                    }else{
                        nextNodeName = FocusReportNode35302Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName();
                    }
                }else {
                    nextNodeName = FocusReportNode35302Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName();
                }
            }
        }


        if(StringUtils.isBlank(nextNodeName)) {
            throw new Exception("没有可用的下一环节！");
        }

        return nextNodeName;
    }
}
