package cn.ffcs.zhsq.reportFocus.threeOneTreatment.service.impl;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.IReportMsgCCCfgService;
import cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.service.impl.ReportMsgCCTypeEnum;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.FiveKeyElement4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description:三合一整治流程（THREE_ONE_TREATMENT）人员选择
 * @Author: zhangtc
 * @Date: 2021/8/16 10:24
 */
@Service(value = "fiveKeyElement4TOTService")
public class FiveKeyElement4TOTServiceImpl extends FiveKeyElement4TwoVioPreServiceImpl {

    @Autowired
    private IReportIntegrationService reportIntegrationService;

    @Autowired
    private IReportMsgCCCfgService reportMsgCCCfgService;

    @Autowired
    private IBaseWorkflowService baseWorkflowService;

    @Autowired
    private IFunConfigurationService funConfigurationService;

    @Autowired
    private OrgSocialInfoOutService orgSocialInfoService;

    @Autowired
    private OrgEntityInfoOutService orgEntityInfoService;

    @Autowired
    private UserManageOutService userManageService;

    @Autowired
    private IEventDisposalWorkflowService eventDisposalWorkflowService;

    @Autowired
    private IEventOrgInfoService eventOrgInfoService;

    @Override
    public Map<String, Object> getNodeInfoForEvent(
            UserInfo userInfo,
            String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
            throws Exception {
        INodeCodeHandler nodeCodeHandler = null;
        Long reportId = null, instanceId = null;
        Map<String, Object> reportParams = new HashMap<String, Object>(),
                reportFocusMap = null,
                resultMap = new HashMap<String, Object>();
        ProInstance proInstance = null;
        String formTypeIdStr = null, workflowName = null;
        boolean isUserRadioStyle = true, isSelectOrg = false;
        String reportType = ReportTypeEnum.threeOneTreatment.getReportTypeIndex(),
                dataSource = null, disposalResult = null;

        if(CommonFunctions.isNotBlank(params, "formId")) {
            try {
                reportId = Long.valueOf(params.get("formId").toString());
            } catch(NumberFormatException e) {}
        }

        if(CommonFunctions.isNotBlank(params, "instanceId")) {
            try {
                instanceId = Long.valueOf(params.get("instanceId").toString());
            } catch(NumberFormatException e) {}
        }

        if(CommonFunctions.isNotBlank(params, "reportType")) {
            reportType = params.get("reportType").toString();
        }

        if(instanceId != null && instanceId > 0) {
            proInstance = baseWorkflowService.findProByInstanceId(instanceId);
        }

        if(proInstance != null) {
            if(reportId == null || reportId<= 0) {
                reportId = proInstance.getFormId();
            }

            formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
            workflowName = proInstance.getProName();
        }

        reportParams.putAll(params);
        reportParams.put("reportId", reportId);
        reportParams.put("reportType", reportType);

        //核实环节获取地理标注信息
        if(FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                && FocusReportNode364Enum.VERIFY_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curnodeName)
                && FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
            reportParams.put("isCapResMarkerInfo", true);
        }
        reportFocusMap = reportIntegrationService.findReportFocusByUUID(null, userInfo, reportParams);

        if(CommonFunctions.isNotBlank(reportFocusMap, "dataSource")) {
            dataSource = reportFocusMap.get("dataSource").toString();
        }
        //一级网格处置结果
        if(CommonFunctions.isNotBlank(reportFocusMap, "disposalResult")) {
            disposalResult = reportFocusMap.get("disposalResult").toString();
        }

        nodeCodeHandler = this.createNodeCodeHandle(nodeCode);

        if(CommonFunctions.isNotBlank(reportFocusMap, "regionCode")) {
            OrgSocialInfoBO orgInfo = null;
            String orgInfoType = null,orgEntityInLevel = null;
            Map<String, Object> orgParam = new HashMap<String, Object>();
            List<OrgSocialInfoBO> orgInfoList = null;
            OrgEntityInfoBO orgEntityInfoBO = null;

            orgParam.put("regionCode", reportFocusMap.get("regionCode").toString());
            orgParam.put("orgType", ConstantValue.ORG_TYPE_UNIT);
            orgParam.put("professionCode", ConstantValue.GOV_PROFESSION_CODE);

            orgInfoList = orgSocialInfoService.findOrgSocialListByPara(orgParam);

            orgEntityInfoBO = orgEntityInfoService.selectOrgEntityInfoByOrgCode(reportFocusMap.get("regionCode").toString());

            if(orgInfoList != null && orgInfoList.size() > 0) {
                orgInfo = orgInfoList.get(0);
            }

            if(null != orgEntityInfoBO){
                orgEntityInLevel = orgEntityInfoBO.getChiefLevel();
            }

            if(nodeCodeHandler.isToBegin() && proInstance != null) {
                Long proCreateUserId = proInstance.getUserId();
                String proCreateUserName = null;

                if(proCreateUserId != null && proCreateUserId > 0) {
                    UserBO userBO = userManageService.getUserInfoByUserId(proCreateUserId);
                    if(userBO != null) {
                        proCreateUserName = userBO.getPartyName();
                    }
                }

                resultMap.put("userIds", proCreateUserId);
                resultMap.put("userNames", proCreateUserName);
                resultMap.put("orgIds", proInstance.getOrgId());
            } else {
                String orgChiefLevel = null;
                List<UserInfo> userInfoList = null;

                if(orgInfo != null) {
                    orgChiefLevel = orgInfo.getChiefLevel();
                    orgInfoType = orgInfo.getOrgType();
                }

                //add ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel) by ztc 20210422 需求单号：5434653
                if(ConstantValue.GRID_ORG_LEVEL.equals(orgChiefLevel) || ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)) {
                    UserInfo userInfoTmp = new UserInfo();
                    int toLevel = nodeCodeHandler.getToLevel();

                    userInfoTmp.setOrgId(orgInfo.getOrgId());
                    userInfoTmp.setOrgName(orgInfo.getOrgName());
                    userInfoTmp.setOrgCode(orgInfo.getOrgCode());

                    if(nodeCodeHandler.isOrganization()) {
                        String appointedOrgCode = funConfigurationService.turnCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, workflowName + "-" + nodeName, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
                        Long appointedOrgId = null;

                        if(StringUtils.isNotBlank(appointedOrgCode)) {
                            OrgSocialInfoBO orgInfoBO = orgSocialInfoService.selectOrgSocialInfoByOrgCode(appointedOrgCode);
                            if(orgInfoBO != null) {
                                appointedOrgId = orgInfoBO.getOrgId();
                            }
                        }

                        if(appointedOrgId != null && appointedOrgId > 0) {
                            userInfoList = this.getUserInfoList(appointedOrgId, nodeCode, params);
                        } else {
                            userInfoList = super.getUserInfoList(orgInfo.getOrgId(), nodeCode, params);
                        }
                    } else if(nodeCodeHandler.isToGrid()) {
                        //子流程 当前环节为 分解跟踪任务  下一环节为 定期报告隐患状态时 所属地域层级小于网格 需要进行组织选择
                        if(FocusReportNode36401Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                                && FocusReportNode36401Enum.DECOMPOSITION_TRACKING_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)
                                && FocusReportNode36401Enum.PERIODIC_REPORTS_HDSTATUS_NODE_NAME.getTaskNodeName().equals(nodeName)
                                && orgEntityInLevel.compareTo(ConstantValue.GRID_ORG_LEVEL) < 0) {
                            //发生地址为网格层级
                            String nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.GRID + INodeCodeHandler.OPERATE_FLOW + (INodeCodeHandler.GRID - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
                            //发生地址为村社区层级
                            if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)){
                                nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.COMMUNITY + INodeCodeHandler.OPERATE_SEND + (INodeCodeHandler.GRID - INodeCodeHandler.COMMUNITY) + nodeCode.substring(4);
                            }
                            nodeCodeHandler = this.createNodeCodeHandle(nodeCodeTmp);

                            isUserRadioStyle = false;
                            isSelectOrg = true;
                            resultMap.put("eventNodeCode", nodeCodeHandler);
                        }else{
                            userInfoList = super.getUserInfoList(orgInfo.getOrgId(), nodeCode, params);
                        }
                    } else if(toLevel == INodeCodeHandler.COMMUNITY) {
                        //主流程 所属区域层级为村社区 当前环节为核实反馈属实：下一环节一级网格处置 进行组织选择
                        if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)
                                && FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                                && FocusReportNode364Enum.VERIFY_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curnodeName)
                                && FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName) ){
                            isUserRadioStyle = false;
                            isSelectOrg = true;
                            resultMap.put("eventNodeCode", nodeCodeHandler);
                        }else{
                            //网格层级获取主送人
                            if(ConstantValue.GRID_ORG_LEVEL.equals(orgChiefLevel)){
                                userInfoList = super.getUserInfoList(orgInfo.getParentOrgId(), nodeCode, params);
                            }else if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)){
                                //村社区层级获取主送人
                                userInfoList = super.getUserInfoList(orgInfo.getOrgId(), nodeCode, params);
                            }
                        }
                    } else if(toLevel > 0 && toLevel < INodeCodeHandler.COMMUNITY) {
                        //需要进行组织选择
                        if(false && FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                                && FocusReportNode364Enum.VERIFY_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curnodeName)
                                && FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)) {
                            isUserRadioStyle = false;
                            isSelectOrg = true;
                            resultMap.put("eventNodeCode", nodeCodeHandler);
                        } else {
                            //发生地址为网格层级
                            String nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.GRID + INodeCodeHandler.OPERATE_REPORT + (INodeCodeHandler.GRID - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
                            //发生地址为村社区层级
                            if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)){
                                nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.COMMUNITY + INodeCodeHandler.OPERATE_REPORT + (INodeCodeHandler.COMMUNITY - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
                            }
                            List<GdZTreeNode> treeNodeList = super.getTreeForEvent(userInfoTmp, null, nodeCodeTmp, null, null, params);

                            if(treeNodeList != null && treeNodeList.size() > 0) {
                                userInfoList = new ArrayList<UserInfo>();

                                for(GdZTreeNode treeNode : treeNodeList) {
                                    //当前环节为 镇级处置（task5）下一环节为 市级处置（task6）、组织验收（task7）时，启动额外的角色配置信息过滤办理人
                                    if((FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)
                                            && (FocusReportNode364Enum.MUNICIPAL_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)
                                                || FocusReportNode364Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(nodeName))
                                    )){
                                        params.put("trigCondition",workflowName + "-" + curnodeName + "-" + nodeName);
                                        //获取额外配置的角色过滤信息
                                        params.put("capExtraRoleId",true);
                                    }
                                    userInfoList.addAll(super.getUserInfoList(Long.valueOf(treeNode.getId()), nodeCodeTmp, params));
                                }
                            } else {
                                throw new ZhsqEventException("缺少办理组织信息！");
                            }
                        }
                    } else if(toLevel < 0) {
                        isUserRadioStyle = false;
                        resultMap = super.getNodeInfoForEvent(userInfoTmp, curnodeName, nodeName, nodeCode, nodeId, params);
                    }
                } else {
                    throw new ZhsqEventException("发生地址没有匹配到网格、村社区层级的组织信息！");
                }

                //三合一整治办验收不通过时，流程回到镇级处置，此时的镇级办理人为镇级提交到组织验收环节的人
                Long streetOrgId = null,handleUserId = null;
                Boolean isNoPassed = false;
                if(FocusReportNode364Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(curnodeName)
                        && FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                    isNoPassed = true;
                    //获取上一环节（镇级处置办理人）
                    List<Map<String,Object>> taskListMap =  baseWorkflowService.capHandledTaskInfoMap(instanceId, IWorkflow4BaseService.SQL_ORDER_DESC,params);
                    if(null != taskListMap && taskListMap.size() > 0){
                        Map<String,Object> streetHandleTask = taskListMap.get(0);
                        if(CommonFunctions.isNotBlank(streetHandleTask,"ORG_ID")){
                            streetOrgId = Long.valueOf(String.valueOf(streetHandleTask.get("ORG_ID")));
                        }
                        if(CommonFunctions.isNotBlank(streetHandleTask,"TRANSACTOR_ID")){
                            handleUserId = Long.valueOf(String.valueOf(streetHandleTask.get("TRANSACTOR_ID")));
                        }
                    }
                }

                if(isUserRadioStyle) {
                    if(userInfoList != null && userInfoList.size() > 0) {
                        StringBuffer userIds = new StringBuffer(""),
                                orgIds = new StringBuffer(""),
                                userNames = new StringBuffer(""),
                                userSetKey = new StringBuffer("");
                        Long userId = null, userOrgId = null;
                        Set<String> userSet = new HashSet<String>();

                        for(UserInfo userInfoTmp : userInfoList) {
                            userId = userInfoTmp.getUserId();
                            userOrgId = userInfoTmp.getOrgId();
                            userSetKey = new StringBuffer("");

                            userSetKey.append(userId).append("-").append(userOrgId);

                            if(!userSet.contains(userSetKey.toString())) {//用户判重
                                //userSet.add(userSetKey.toString());
                                //下一办理人信息
                                //组织验收不通过时 回镇级办理人员处置
                                if(isNoPassed){
                                    if(streetOrgId.equals(userOrgId) && handleUserId.equals(userId)){
                                        userSet.add(userSetKey.toString());

                                        userIds.append(",").append(userId);
                                        orgIds.append(",").append(userOrgId);
                                        userNames.append(",").append(userInfoTmp.getPartyName());
                                    }
                                }else{
                                    userSet.add(userSetKey.toString());

                                    userIds.append(",").append(userId);
                                    orgIds.append(",").append(userOrgId);
                                    userNames.append(",").append(userInfoTmp.getPartyName());
                                }
                            }
                        }

                        if(userIds.length() > 0) {
                            resultMap.put("userIds", userIds.substring(1));
                        }
                        if(userNames.length() > 0) {
                            resultMap.put("userNames", userNames.substring(1));
                        }
                        if(orgIds.length() > 0) {
                            resultMap.put("orgIds", orgIds.substring(1));
                        }
                    } else {
                        throw new ZhsqEventException("缺少办理人员信息！");
                    }
                }
            }

            resultMap.put("isSelectUser", false);
            resultMap.put("isSelectOrg", isSelectOrg);
            //主送人员只能单选
            resultMap.put("isUserRadioStyle", isUserRadioStyle);

            if(isUserRadioStyle && CommonFunctions.isNotBlank(resultMap, "userIds")) {
                String userIds = resultMap.get("userIds").toString();
                //只有当主送为一个人时，默认选中该人员
                resultMap.put("isUserDefaulCheck", userIds.split(",").length == 1);
            }

            String adviceNoteInitial = null, adviceNote = null;
            Map<String, Object> adviceNoteMap = new HashMap<String, Object>();

            adviceNoteMap.putAll(params);
            adviceNoteMap.putAll(resultMap);
            adviceNoteMap.putAll(reportFocusMap);
            adviceNoteMap.put("isReplaceAdviceNote", false);

            adviceNoteInitial = capAdviceNote(proInstance, userInfo, adviceNoteMap);
            adviceNote = capAdviceNote(adviceNoteInitial, userInfo, adviceNoteMap);

            Map<String, List<UserBO>> cfgMap = null;
            Map<String, Object> cfgParams = new HashMap<String, Object>();
            String wfEndNodeName = nodeName;

            if(FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
                if(FocusReportNode364Enum.FIND_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
                    if(StringUtils.isNotBlank(dataSource)) {
                        wfEndNodeName += "-" + dataSource;
                    }
                }
            }

            cfgParams.put("workflowName", workflowName);
            cfgParams.put("wfEndNodeName", wfEndNodeName);
            cfgParams.put("wfStartNodeName", curnodeName);
            cfgParams.put("ccType", ReportMsgCCTypeEnum.CCTYPE_DISTRIBUTE.getCcType() + "," + ReportMsgCCTypeEnum.CCTYPE_SELECT.getCcType());

            if(CommonFunctions.isNotBlank(params, "isAlterBenchmark") && CommonFunctions.isNotBlank(params, "benchmarkRegionCode")) {
                if(Boolean.valueOf(params.get("isAlterBenchmark").toString())) {
                    orgParam = new HashMap<String, Object>();
                    orgInfo = null;

                    orgParam.put("regionCode", params.get("benchmarkRegionCode").toString());
                    orgParam.put("orgType", ConstantValue.ORG_TYPE_UNIT);
                    orgParam.put("professionCode", ConstantValue.GOV_PROFESSION_CODE);

                    orgInfoList = orgSocialInfoService.findOrgSocialListByPara(orgParam);

                    if(orgInfoList != null && orgInfoList.size() > 0) {
                        orgInfo = orgInfoList.get(0);
                    }
                }
            }

            if(orgInfo != null) {
                cfgMap = reportMsgCCCfgService.findCfg4User(cfgParams, orgInfo);
            }

            if(cfgMap != null && cfgMap.size() > 0) {
                String ccTypeDistribute = ReportMsgCCTypeEnum.CCTYPE_DISTRIBUTE.getCcType(),
                        ccTypeSelect = ReportMsgCCTypeEnum.CCTYPE_SELECT.getCcType();

                //分送人员
                if(cfgMap.containsKey(ccTypeDistribute)) {
                    Map<String, Object> distributeUser = null;
                    List<UserBO> distributeUserList = cfgMap.get(ccTypeDistribute);
                    UserBO creatorUser = findExtraCCUser(curnodeName, nodeName, proInstance, params);

                    if(creatorUser != null) {
                        distributeUserList = distributeUserList == null ? new ArrayList<UserBO>() : distributeUserList;
                        distributeUserList.add(creatorUser);
                    }

                    distributeUser = changUserBO2Map(distributeUserList);

                    distributeUser.put("isDisplayUser", true);

                    resultMap.put("distributeUser", distributeUser);
                }

                //选送人员
                /*if(cfgMap.containsKey(ccTypeSelect)) {
                    Map<String, Object> selectUser = changUserBO2Map(cfgMap.get(ccTypeSelect));
                    selectUser.put("isSelectUser", true);

                    resultMap.put("selectUser", selectUser);
                }*/
            }
            //选送人员
            //全流程（主、子流程）选送人员通过组织进行选择
            if(FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                    || FocusReportNode36401Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)){
                Map<String, Object> selectUser = new HashMap<>();
                selectUser.put("isSelectUser", false);
                selectUser.put("isSelectOrg", true);

                //获取南安orgId
                OrgSocialInfoBO nananOrgInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode("350583");
                if(null != nananOrgInfo){
                    selectUser.put("nananOrgId", nananOrgInfo.getOrgId());
                }

                resultMap.put("selectUser", selectUser);
            }

            boolean isShowText = false,isShowText2 = false, isShowDict = false, isShowAddress = false, isShowRegion = false, isShowInput=false, isRevertOccuredNote=false;
            String textLabelName = "",textLabelName2 = "", dictLabelName = "", addressLabelName = "", regionLabelName = "", inputName = "";
            Map<String, Object> dynamicContentMap = new HashMap<String, Object>();
            Integer adviceNoteIndex = null;

            if(FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
                //当前环节为核实反馈
                if(FocusReportNode364Enum.VERIFY_FEEDBACK_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
                    //下一环节为一级网格长处置 启动子流程
                    if(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)) {
                        //在页面还原地址消息模板
                        isRevertOccuredNote = true;
                        dynamicContentMap.put("isRevertOccuredNote", isRevertOccuredNote);
                        //发生地址
                        List<OrgEntityInfoBO> orgEntityInfoList = null;
                        isShowAddress = true;
                        addressLabelName = "发生地址";
                        String startDivisionCode = "-1";
                        //获取当前登录账号下所管理的所有网格
                        orgEntityInfoList = userInfo.getInfoOrgList();
                        if (null != orgEntityInfoList && orgEntityInfoList.size() > 0) {
                            StringBuffer orgCodeSb = new StringBuffer("");
                            String infoOrgCode = null;

                            for (OrgEntityInfoBO orgEntityInfo : orgEntityInfoList) {
                                infoOrgCode = orgEntityInfo.getOrgCode();

                                if(StringUtils.isNotBlank(infoOrgCode)) {
                                    orgCodeSb.append(",").append(infoOrgCode);
                                }
                            }

                            if(orgCodeSb.length() > 0) {
                                startDivisionCode = orgCodeSb.substring(1);//标准地址库控件起始网格
                            }
                        }
                        dynamicContentMap.put("startDivisionCode", startDivisionCode);
                        resultMap.put("reportFocus", reportFocusMap);

                        //隐患类型
                        String dictPcode = "B210015003";
                        isShowDict = true;
                        dictLabelName = "隐患类型";
                        dynamicContentMap.put("dictPcode", dictPcode);

                        //住宿人数
                        isShowInput = true;
                        inputName = "住宿人数";
                        dynamicContentMap.put("isShowInput", isShowInput);
                        dynamicContentMap.put("inputName", inputName);

                        resultMap.put("isUploadHandlingPic", true);
                        resultMap.put("picTypeName", "处理中");
                    }
                }else if(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
                    if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                        isShowText = true;
                        textLabelName = "处置措施";
                        dynamicContentMap.put("isShowText", isShowText);
                        dynamicContentMap.put("textLabelName", textLabelName);

                        //处理后图片
                        resultMap.put("isUploadHandledPic", true);
                    }else if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                        String dynamicDictJson = null;
                        //网格员(01) 12345(02)
                        if(TOTDataSourceEnum.GRID_INSPECT.getDataSource().equals(dataSource)
                                || TOTDataSourceEnum.PLATFORM_12345.getDataSource().equals(dataSource)){
                            dynamicDictJson = "[{'name':'申请延期', 'value':'1'},{'name':'提请镇级处置', 'value':'2'}]";
                        }else if(TOTDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(dataSource)
                                || TOTDataSourceEnum.UNANNOUNCED_VISIT.getDataSource().equals(dataSource)
                                || TOTDataSourceEnum.MASSES_REPORT.getDataSource().equals(dataSource)){
                            //03 04 群众05
                            dynamicDictJson = "[{'name':'已完成整治', 'value':'0'},{'name':'申请延期', 'value':'1'},{'name':'提请镇级处置', 'value':'2'}]";
                        }
                        isShowDict = true;
                        dictLabelName = "处置结果";
                        dynamicContentMap.put("dynamicDictJson", dynamicDictJson);

                        isShowText = true;
                        textLabelName = "原因";
                        dynamicContentMap.put("isShowText", isShowText);
                        dynamicContentMap.put("textLabelName", textLabelName);

                        isShowText2 = true;
                        textLabelName2 = "处置情况";
                        dynamicContentMap.put("isShowText2", isShowText2);
                        dynamicContentMap.put("textLabelName2", textLabelName2);
                    }

                    resultMap.put("isUploadHandlingPic", true);
                    resultMap.put("picTypeName", "处理中");
                }else if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
                    if(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                        //市级采集的 一级网格（上一环节）完成整治 当前环节验收不通过
                        if(TOTDisposalResultEnum.REMEDIATION_COMPLETE.getDisposalResult().equals(disposalResult)
                                && (TOTDataSourceEnum.SUPERIOR_ASSIGN.getDataSource().equals(dataSource)
                                || TOTDataSourceEnum.UNANNOUNCED_VISIT.getDataSource().equals(dataSource)
                                || TOTDataSourceEnum.MASSES_REPORT.getDataSource().equals(dataSource))){
                            isShowText = true;
                            textLabelName = "未完成情况";
                            dynamicContentMap.put("isShowText", isShowText);
                            dynamicContentMap.put("textLabelName", textLabelName);
                        }else if(TOTDisposalResultEnum.APPLY_EXTESION.getDisposalResult().equals(disposalResult)){
                            //延期申请的
                            String dynamicDictJson = "[{'name':'同意延期', 'value':'0'},{'name':'不同意延期', 'value':'1'}]";
                            isShowDict = true;
                            dictLabelName = "处置结果";
                            dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
                            dynamicContentMap.put("disposalResult", disposalResult);

                            isShowText = true;
                            textLabelName = "原因";
                            dynamicContentMap.put("isShowText", isShowText);
                            dynamicContentMap.put("textLabelName", textLabelName);
                        }
                    }else if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                        if(TOTDisposalResultEnum.STREET_HANDLE.getDisposalResult().equals(disposalResult)){
                            isShowText = true;
                            textLabelName = "处置情况";
                            dynamicContentMap.put("isShowText", isShowText);
                            dynamicContentMap.put("textLabelName", textLabelName);
                        }

                        //处理后图片
                        resultMap.put("isUploadHandledPic", true);
                    }else if(FocusReportNode364Enum.MUNICIPAL_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                        //一级网格办理时未完成整治 2
                        if(TOTDisposalResultEnum.STREET_HANDLE.getDisposalResult().equals(disposalResult)){
                            //网格、12345来源的、市级、群众 一级网格处置时都有可能选择未完成整治 所以这里不区分来源
                            isShowText = true;
                            textLabelName = "处置情况";
                            dynamicContentMap.put("isShowText", isShowText);
                            dynamicContentMap.put("textLabelName", textLabelName);

                            isShowText2 = true;
                            textLabelName2 = "具体原因";
                            dynamicContentMap.put("isShowText2", isShowText2);
                            dynamicContentMap.put("textLabelName2", textLabelName2);
                        }
                    }
                    //处理中图片必填
                    resultMap.put("isUploadHandlingPic", true);
                    resultMap.put("picTypeName", "处理中");
                }else if(FocusReportNode364Enum.MUNICIPAL_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
                    resultMap.put("isUploadHandledPic", true);
                    resultMap.put("picTypeName", "处理后");

                    if(FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                        //会同部门
                        isShowText = true;
                        textLabelName = "会同部门";
                        dynamicContentMap.put("isShowText", isShowText);
                        dynamicContentMap.put("textLabelName", textLabelName);

                        //行政处罚
                        isShowText2 = true;
                        textLabelName2 = "行政处罚";
                        dynamicContentMap.put("isShowText2", isShowText2);
                        dynamicContentMap.put("textLabelName2", textLabelName2);

                        //整治日期
                        dynamicContentMap.put("isShowRectifyDate", true);
                    }
                }else if(FocusReportNode364Enum.ACCEPTANCE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
                    if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                        isShowText = true;
                        textLabelName = "加强措施";
                        dynamicContentMap.put("isShowText", isShowText);
                        dynamicContentMap.put("textLabelName", textLabelName);
                    }
                }
            } else if(FocusReportNode36401Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
                if(FocusReportNode36401Enum.PERIODIC_REPORTS_HDSTATUS_NODE_NAME.getTaskNodeName().equals(curnodeName)
                        && ConstantValue.HANDLING_TASK_CODE.equals(nodeName)){
                    //延期申请的
                    String dynamicDictJson = "[{'name':'已完成整治', 'value':'0'},{'name':'整治中', 'value':'1'},{'name':'申请延期', 'value':'2'},{'name':'尚未整治', 'value':'3'}]";
                    isShowDict = true;
                    dictLabelName = "跟踪情况";
                    dynamicContentMap.put("dynamicDictJson", dynamicDictJson);
                    dynamicContentMap.put("disposalResult", disposalResult);

                    isShowText = true;
                    textLabelName = "原因";
                    dynamicContentMap.put("isShowText", isShowText);
                    dynamicContentMap.put("textLabelName", textLabelName);
                }
            }

            if(StringUtils.isNotBlank(adviceNote)) {
                String splitStr = "#OR#";//当一个消息模板中包含有多种执行情况时，使用#OR#进行分割

                if(adviceNote.contains(splitStr)) {
                    String[] adviceNoteArray = adviceNote.split(splitStr),
                            adviceNoteInitialArray = adviceNoteInitial.split(splitStr);

                    if(adviceNoteIndex != null) {
                        resultMap.put("adviceNote", adviceNoteArray[adviceNoteIndex]);
                    } else {
                        for(int index = 0, len = adviceNoteArray.length; index < len; index++) {
                            dynamicContentMap.put("adviceNote_" + index, adviceNoteArray[index]);
                            dynamicContentMap.put("adviceNoteInitial_" + index, adviceNoteInitialArray[index]);
                        }
                    }
                } else {
                    resultMap.put("adviceNote", adviceNote);
                    resultMap.put("adviceNoteInitial", adviceNoteInitial);
                }
            }

            //当下一环节为归档时，增加处理后图片必填约束
            /*if(FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                    && FocusReportNode364Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){
                //变更需求单：5434653 要求所有环节处理后图片非必填 2021-04-26
                resultMap.put("isUploadHandledPic", false);
                resultMap.put("picTypeName", "处理后");
            }*/

            dynamicContentMap.put("isShowText", isShowText);
            dynamicContentMap.put("isShowDict", isShowDict);
            dynamicContentMap.put("isShowAddress", isShowAddress);
            dynamicContentMap.put("isShowRegion", isShowRegion);
            dynamicContentMap.put("textLabelName", textLabelName);
            dynamicContentMap.put("dictLabelName", dictLabelName);
            dynamicContentMap.put("addressLabelName", addressLabelName);
            dynamicContentMap.put("regionLabelName", regionLabelName);
            resultMap.put("dynamicContentMap", dynamicContentMap);
        } else {
            resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
        }

        return resultMap;
    }

    @Override
    public List<GdZTreeNode> getTreeForEvent(
            UserInfo userInfo, Long orgId,
            String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception {
        INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
        Long rootOrgId = orgId;
        Node node = null;
        List<Map<String, Object>> nodeActorsMap = null;

        //节点指定组织办理 单独获取组织信息 构造组织点击效果
        if(CommonFunctions.isNotBlank(params, "nodeId")) {
            Integer nodeId = Integer.valueOf(params.get("nodeId").toString());
            node = eventDisposalWorkflowService.findNodeById(nodeId);
        }

        //下一环节为 （task5）时，进行组织节点构造
        if(node != null && FocusReportNode364Enum.MUNICIPAL_HANDLE_NODE_NAME.toString().equals(node.getNodeName())) {

            boolean isFromCommunityGridAdmin = false;
            nodeActorsMap = eventDisposalWorkflowService.findNodeActorsById(node.getNodeId());

            List<GdZTreeNode> nodes = new ArrayList<GdZTreeNode>();
            List<OrgSocialInfoBO> socialInfoBOs = new ArrayList<>();
            OrgSocialInfoBO orgInfo = null;

            if(nodeActorsMap != null && nodeActorsMap.size() > 0) {
                for(Map<String, Object> nodeActor : nodeActorsMap) {
                    orgInfo = null;
                    if(CommonFunctions.isNotBlank(nodeActor, "ORG_ID")) {
                        orgInfo = orgSocialInfoService.findByOrgId(Long.valueOf(nodeActor.get("ORG_ID").toString()));
                    }
                    if(orgInfo != null && orgInfo.getOrgId() != null) {
                        socialInfoBOs.add(orgInfo);
                    }
                }
            }
            for (OrgSocialInfoBO socialOrg : socialInfoBOs) {
                nodes.add(CommonFunctions.transSocialOrgToZTreeNodeA(socialOrg, socialOrg.isLeaf()));
            }

            super.addClickableAttr2Node(nodeCodeHandler, nodes, isFromCommunityGridAdmin);

            return nodes;
        }

        return super.getTreeForEvent(userInfo, rootOrgId, nodeCode, level, professionCode, params);
    }

    @Override
    public List<UserInfo> getUserInfoList(Long orgId, String nodeCode, Long removeUserId, Map<String, Object> extraParam) throws Exception {
        List<UserInfo> userInfos = new ArrayList<UserInfo>();
        if (nodeCode != null) {
            INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
            List<UserInfo> users = new ArrayList<UserInfo>();//获取组织下的所有用户
            List<Map<String, Object>> nodeActorsMap = null;
            List<UserBO> userBOList = new ArrayList<UserBO>();
            String partyName = "", userOrgName = null,orgCode = null,trigCondition = null;
            boolean isActor4Org = false;//判断指定节点是否为组织配置，可以使用这种方法的前提是，工作流节点中组织、角色、职位、人员的配置只能选择其中一个
            Node node = null;

            if(orgId != null && orgId > 0) {
                OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
                if(orgInfo != null) {
                    userOrgName = orgInfo.getOrgName();
                    orgCode = orgInfo.getOrgCode();
                }
            }
            if(CommonFunctions.isNotBlank(extraParam, "partyName")) {
                partyName = extraParam.get("partyName").toString();
            }

            if(CommonFunctions.isNotBlank(extraParam, "trigCondition")) {
                trigCondition = extraParam.get("trigCondition").toString();
            }

            if(CommonFunctions.isNotBlank(extraParam, "nodeId")) {
                Integer nodeId = Integer.valueOf(extraParam.get("nodeId").toString());

                //因现网出现nodeId < 0 情况，暂时去除判断条件 nodeId > 0
                if(nodeId != null /*&& nodeId > 0*/) {
                    node = eventDisposalWorkflowService.findNodeById(nodeId);
                    nodeActorsMap = eventDisposalWorkflowService.findNodeActorsById(Integer.valueOf(nodeId));
                }
            }

            if(nodeActorsMap != null && nodeActorsMap.size() > 0) {
                Long roleId = -1L, positionId = -1L;
                Map<Long, UserBO> userBOMap = new HashMap<Long, UserBO>();
                String actorName = null;
                StringBuffer roleName = new StringBuffer(""),
                        positionName = new StringBuffer(""),
                        orgName = new StringBuffer("");
                String extraRoleId = null;

                //当节点指定组织办理时 无法在节点配置额外的角色配置 使用功能配置额外配置角色信息进行过滤
                if(CommonFunctions.isNotBlank(extraParam,"capExtraRoleId") && Boolean.valueOf(String.valueOf(extraParam.get("capExtraRoleId")))){
                    extraRoleId = funConfigurationService.turnCodeToValue(ConstantValue.ENTER_GRID_SCENARIO_APPLICATION, trigCondition,
                            IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);
                }
                //下一环节为 市级处置(task6)、组织验收（task7） 时
                // 进行变更 nodeActorsMap，因为流程图节点指定多组织办理时 此处的 nodeActorsMap依然会查询出所有组织信息下的所有人
                if(null != node
                        && (FocusReportNode364Enum.MUNICIPAL_HANDLE_NODE_NAME.toString().equals(node.getNodeName())
                        || FocusReportNode364Enum.ACCEPTANCE_NODE_NAME.toString().equals(node.getNodeName()) )){
                    Map<String, Object> nodeActorTemp = null;

                    for(Map<String, Object> nodeActor : nodeActorsMap){
                        if(CommonFunctions.isNotBlank(nodeActor,"ORG_ID")){
                            if(orgId.equals(Long.valueOf(String.valueOf(nodeActor.get("ORG_ID"))))){
                                nodeActorTemp = nodeActor;
                                break;
                            }
                        }
                    }

                    if(null != nodeActorTemp){
                        nodeActorsMap = new ArrayList<Map<String, Object>>();
                        nodeActorsMap.add(nodeActorTemp);
                    }
                }

                for(Map<String, Object> nodeActor : nodeActorsMap) {
                    if(CommonFunctions.isNotBlank(nodeActor, "ACTOR_NAME")) {
                        actorName = nodeActor.get("ACTOR_NAME").toString();
                    }

                    if(CommonFunctions.isNotBlank(nodeActor, "ORG_ID")) {

                        if(StringUtils.isNotBlank(extraRoleId)){
                            try {
                                roleId = Long.valueOf(extraRoleId);
                            } catch (NumberFormatException e) {
                                throw new NumberFormatException("参数[extraRoleId]："+ extraRoleId +" 不能转换为Long型，请检查功能配置【ENTER_GRID_SCENARIO_APPLICATION->" + trigCondition+"】！");
                            }
                        }

                        isActor4Org = true;
                        userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(roleId > 0 ?roleId:null, null, Long.valueOf(nodeActor.get("ORG_ID").toString())));//roleId positionId orgId
                        orgName.append(",").append(actorName);
                    } else if(CommonFunctions.isNotBlank(nodeActor, "ROLE_ID")) {
                        roleId = Long.valueOf(nodeActor.get("ROLE_ID").toString());
                        userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(roleId, null, orgId));//roleId positionId orgId
                        roleName.append(",").append(actorName);
                    } else if(CommonFunctions.isNotBlank(nodeActor, "POSITION_ID")) {
                        positionId = Long.valueOf(nodeActor.get("POSITION_ID").toString());
                        userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, positionId, orgId));//roleId positionId orgId
                        positionName.append(",").append(actorName);
                    }
                }

                if(userBOList != null && userBOList.size() > 0) {
                    String userPartyName = null;

                    for(UserBO userBO : userBOList) {
                        userPartyName = userBO.getPartyName();

                        if(StringUtils.isBlank(partyName) || (StringUtils.isNotBlank(userPartyName) && userPartyName.contains(partyName))) {
                            userBOMap.put(userBO.getUserId(), userBO);
                        }
                    }

                    userBOList.clear();//清除原有的人员信息

                    if(!userBOMap.isEmpty()) {
                        for(Long userId : userBOMap.keySet()) {
                            userBOList.add(userBOMap.get(userId));
                        }
                    }
                } else {
                    String msgWrong = "该环节缺少如下配置的人员，";
                    if(orgName.length() > 0) {
                        msgWrong += "组织名称为 [" + orgName.substring(1) + "] ";
                    }
                    if(roleName.length() > 0) {
                        msgWrong += "角色名称为 [" + roleName.substring(1) + "] ";
                    }
                    if(positionName.length() > 0) {
                        msgWrong += "职位名称为 [" + positionName.substring(1) + "] ";
                    }

                    throw new Exception(msgWrong);
                }
            } else {
                /**
                 * orgId		组织id
                 * type			type为1 则可以查询到当前组织以及下级所有组织的用户； 其他值 则只能查询当前组织上的用户
                 * partyName	姓名  模糊查询
                 * userType 	用户类型  用户类型 001网站群注册，002后台注册  003超级管理员 004开发人员 005cpsp用户
                 */
                userBOList = userManageService.queryUserListByCondition(orgId, null, partyName, null);
            }

            if(userBOList != null) {
                UserInfo userInfoTmp = null;
                for(UserBO userBO : userBOList) {
                    userInfoTmp = new UserInfo();

                    BeanUtils.copyProperties(userBO, userInfoTmp);

                    if(isActor4Org) {
                        userInfoTmp.setOrgId(Long.valueOf(userBO.getSocialOrgId()));
                    } else {
                        userInfoTmp.setOrgId(orgId);
                        userInfoTmp.setOrgName(userOrgName);
                    }
                    userInfoTmp.setUserName(userBO.getRegisValue());

                    users.add(userInfoTmp);
                }

                if (nodeCodeHandler.isToGrid()) {//下派到网格、上报到网格
                    Long userId = -1L;
                    for (UserInfo userInfo : users) {
                        userId = userInfo.getUserId();
                        if(this.isUserIdGridAdmin(orgId, userId)){
                            userInfos.add(userInfo);
                        }
                    }
                } else if (nodeCodeHandler.isSendToCommunity() || (nodeCodeHandler.isReport() && nodeCodeHandler.isToCommunity())) {
                    //下派到社区 或者 网格员上报给社区
                    MixedGridInfo mixedGridInfo = eventOrgInfoService.findGridInfoBySocialInfoOrgId(orgId);
                    String gridAdminDuty = capGridAdminDuty(orgId);

                    if(mixedGridInfo != null) {
                        for (UserInfo userInfo : users) {
                            if(StringUtils.isNotBlank(gridAdminDuty)) {
                                if(isUserIdGridAdminExists(mixedGridInfo.getGridId(), userInfo.getUserId(), gridAdminDuty)) {
                                    userInfos.add(userInfo);
                                }
                            } else {
                                if(!this.isUserIdGridAdmin(orgId, userInfo.getUserId())){
                                    userInfos.add(userInfo);
                                }
                            }
                        }
                    }
                } else {
                    if (users != null && users.size() > 0) {
                        userInfos = users;
                    }
                }
            }
        } else {
            throw new Exception("环节编码不能为空！");
        }

        return userInfos;
    }

    /**
     * 获取消息模板
     * @param proInstance	流程实例
     * @param userInfo		操作用户
     * @param twoVioPreMap	两位防治信息
     * @return
     */
    @Override
    protected String capAdviceNote(ProInstance proInstance, UserInfo userInfo, Map<String, Object> twoVioPreMap) {

        if(CommonFunctions.isBlank(twoVioPreMap,"curUserOrgId")){
            twoVioPreMap.put("curUserOrgId",userInfo.getOrgId());
        }

        StringBuffer trigCondition = capAdviceNoteCondition(proInstance, twoVioPreMap);
        String adviceNote = null;

        //获取办理意见模板
        adviceNote = funConfigurationService.turnCodeToValue(ConstantValue.SMS_NOTE_TYPE, trigCondition.toString(), IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);

        adviceNote = capAdviceNote(adviceNote, userInfo, twoVioPreMap);

        return adviceNote;
    }

    /**
     * 获取消息模板触发条件
     * @param proInstance
     * @param params
     * @return
     */
    @Override
    protected StringBuffer capAdviceNoteCondition(ProInstance proInstance, Map<String, Object> params) {
        StringBuffer trigCondition = new StringBuffer("");
        String formTypeIdStr = null, curTaskName = null,nodeName = null;

        if(proInstance != null) {
            curTaskName = proInstance.getCurNode();
            formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
        }
        if(CommonFunctions.isNotBlank(params, "nodeName")){
            nodeName = String.valueOf(params.get("nodeName"));
        }

        trigCondition.append(ConstantValue.THREE_ONE_TREATMENT_NOTE).append("-").append(proInstance.getProName()).append("-").append(curTaskName);

        if(FocusReportNode364Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
            if(FocusReportNode364Enum.FIND_NODE_NAME.getTaskNodeName().equals(curTaskName)){
                if(FocusReportNode364Enum.FIND_NODE_NAME.getTaskNodeName().equals(curTaskName)){
                    trigCondition.append("-").append(nodeName).append("-").append(params.get("dataSource"));
                }
            }else if(FocusReportNode364Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(curTaskName)){
                if(FocusReportNode364Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                    trigCondition.append("-").append(nodeName).append("-").append(params.get("disposalResult"));
                }else{
                    trigCondition.append("-").append(nodeName);
                }
            }else{
                trigCondition.append("-").append(nodeName);
            }
        }else if(StringUtils.isNotBlank(nodeName)) {
            trigCondition.append("-").append(nodeName);
        }

        return trigCondition;
    }

    /**
     * 消息模板内容构造
     * @param adviceNote
     * @param userInfo
     * @param params
     * @return
     */
    @Override
    protected String capAdviceNote(String adviceNote, UserInfo userInfo, Map<String, Object> params) {
        adviceNote = super.capAdviceNote(adviceNote, userInfo, params);

        if(StringUtils.isNotBlank(adviceNote)) {
            if(adviceNote.contains("@hiddenDangerTypeName@") && CommonFunctions.isNotBlank(params, "hiddenDangerTypeName")) {
                if(CommonFunctions.isNotBlank(params, "hiddenDangerType")
                        && String.valueOf(params.get("hiddenDangerType")).contains("99")
                        && CommonFunctions.isNotBlank(params, "hdtDescribe")){
                    adviceNote = adviceNote.replace("@hiddenDangerTypeName@", params.get("hiddenDangerTypeName").toString() + "(" + params.get("hdtDescribe") +")");
                }else {
                    adviceNote = adviceNote.replace("@hiddenDangerTypeName@", params.get("hiddenDangerTypeName").toString());
                }
            }

            if(adviceNote.contains("@feedbackTime@") && CommonFunctions.isNotBlank(params, "feedbackTimeStr")) {
                adviceNote = adviceNote.replace("@feedbackTime@", params.get("feedbackTimeStr").toString());
            }
            if(adviceNote.contains("@extensionDateStr@") && CommonFunctions.isNotBlank(params, "extensionDateStr")) {
                adviceNote = adviceNote.replace("@extensionDateStr@", params.get("extensionDateStr").toString());
            }
            if(adviceNote.contains("@communityDuedate@") && CommonFunctions.isNotBlank(params, "communityDuedateStr")) {
                adviceNote = adviceNote.replace("@communityDuedate@", params.get("communityDuedateStr").toString());
            }
            if(adviceNote.contains("@streetDuedate@") && CommonFunctions.isNotBlank(params, "streetDuedateStr")) {
                adviceNote = adviceNote.replace("@streetDuedate@", params.get("streetDuedateStr").toString());
            }
            if(adviceNote.contains("@discoveryStaff@") && CommonFunctions.isNotBlank(params, "discoveryStaff")) {
                adviceNote = adviceNote.replace("@discoveryStaff@", params.get("discoveryStaff").toString());
            }

            if(adviceNote.contains("@reporterOrgName@")) {
                String reporterOrgName = "";

                if(CommonFunctions.isNotBlank(params, "reporterOrgName")) {
                    reporterOrgName = params.get("reporterOrgName").toString();
                } else if(CommonFunctions.isNotBlank(params, "reporterOrgId")) {
                    Long reporterOrgId = null;
                    OrgSocialInfoBO orgInfo = null;

                    try {
                        reporterOrgId = Long.valueOf(params.get("reporterOrgId").toString());
                    } catch(NumberFormatException e) {}

                    if(reporterOrgId != null && reporterOrgId > 0) {
                        orgInfo = orgSocialInfoService.findByOrgId(reporterOrgId);

                        if(orgInfo != null) {
                            reporterOrgName = orgInfo.getOrgName();
                        }
                    }
                }

                adviceNote = adviceNote.replace("@reporterOrgName@", reporterOrgName);
            }
        }

        return adviceNote;
    }
}
