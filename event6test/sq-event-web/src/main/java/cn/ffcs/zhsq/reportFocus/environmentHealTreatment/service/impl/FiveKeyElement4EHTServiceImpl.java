package cn.ffcs.zhsq.reportFocus.environmentHealTreatment.service.impl;

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
 * @Description:环境卫生问题处置流程人员选择接口
 * @Author: zhangtc
 * @Date: 2021/7/26 17:49
 */
@Service(value = "fiveKeyElement4EHTService")
public class FiveKeyElement4EHTServiceImpl extends FiveKeyElement4TwoVioPreServiceImpl {

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
        String reportType = ReportTypeEnum.environmentHealTreatment.getReportTypeIndex(),
                dataSource = null;

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
        reportFocusMap = reportIntegrationService.findReportFocusByUUID(null, userInfo, reportParams);

        if(CommonFunctions.isNotBlank(reportFocusMap, "dataSource")) {
            dataSource = String.valueOf(reportFocusMap.get("dataSource"));
        }

        nodeCodeHandler = this.createNodeCodeHandle(nodeCode);

        if(CommonFunctions.isNotBlank(reportFocusMap, "regionCode")) {
            OrgSocialInfoBO orgInfo = null,userOrgInfo = null;
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

                if(ConstantValue.GRID_ORG_LEVEL.equals(orgChiefLevel) || ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)) {
                    UserInfo userInfoTmp = new UserInfo();
                    int toLevel = nodeCodeHandler.getToLevel();

                    userInfoTmp.setOrgId(orgInfo.getOrgId());
                    userInfoTmp.setOrgName(orgInfo.getOrgName());
                    userInfoTmp.setOrgCode(orgInfo.getOrgCode());

                    //配置到组织办理
                    if(nodeCodeHandler.isOrganization()) {
                        //获取功能配置中组织信息
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
                        //判断所属区域层级
                        //当前环节为 task2 并且所属区域层级大于网格层级 进行组织、所属区域选择
                        if(FocusReportNode361Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)
                                && orgEntityInLevel.compareTo(ConstantValue.GRID_ORG_LEVEL) < 0){
                            //进行组织人员选择
                            isUserRadioStyle = false;
                            isSelectOrg = true;
                            resultMap.put("eventNodeCode", nodeCodeHandler);
                        }else{
                            //所属区域层级到网格
                            userInfoList = super.getUserInfoList(orgInfo.getOrgId(), nodeCode, params);
                        }
                    } else if(toLevel == INodeCodeHandler.COMMUNITY) {
                        //网格层级获取主送人
                        if(ConstantValue.GRID_ORG_LEVEL.equals(orgChiefLevel)){
                            userInfoList = super.getUserInfoList(orgInfo.getParentOrgId(), nodeCode, params);
                        }else if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)){
                            //村社区层级获取主送人
                            userInfoList = super.getUserInfoList(orgInfo.getOrgId(), nodeCode, params);
                        }
                    } else if(toLevel > 0 && toLevel < INodeCodeHandler.COMMUNITY) {
                        //需要进行组织选择
                        if(FocusReportNode361Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                                && FocusReportNode361Enum.ADMINISTRATION_BUREAU_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
                            isUserRadioStyle = false;
                            isSelectOrg = true;
                            resultMap.put("eventNodeCode", nodeCodeHandler);
                        }else{
                            //发生地址为网格层级
                            String nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.GRID + INodeCodeHandler.OPERATE_REPORT + (INodeCodeHandler.GRID - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);

                            //发生地址为村社区层级
                            if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)){
                                nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.COMMUNITY + INodeCodeHandler.OPERATE_REPORT + (INodeCodeHandler.COMMUNITY - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
                            }/*else if(ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel)){
                                //发生地址为乡镇街道层级
                                if(StringUtils.isNotBlank(orgInfoType)){
                                    if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgInfoType)){
                                        nodeCodeTmp = INodeCodeHandler.ORG_UINT + INodeCodeHandler.COUNTY + INodeCodeHandler.OPERATE_FLOW + (INodeCodeHandler.STREET - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
                                    }else{
                                        nodeCodeTmp = INodeCodeHandler.ORG_DEPT + INodeCodeHandler.COUNTY + INodeCodeHandler.OPERATE_FLOW + (INodeCodeHandler.STREET - nodeCodeHandler.getToLevel()) + nodeCode.substring(4);
                                    }
                                }else{
                                    throw new ZhsqEventException("监测信息所属区域对应的组织信息不存在！");
                                }
                            }*/

                            List<GdZTreeNode> treeNodeList = this.getTreeForEvent(userInfoTmp, null, nodeCodeTmp, null, null, params);

                            if(treeNodeList != null && treeNodeList.size() > 0) {
                                userInfoList = new ArrayList<UserInfo>();

                                for(GdZTreeNode treeNode : treeNodeList) {
                                    //当前环节为 镇级处置（task4）下一环节为 城市管理局处置（task5）时，启动额外的角色配置信息过滤办理人
                                    if((FocusReportNode361Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)
                                            && FocusReportNode361Enum.ADMINISTRATION_BUREAU_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName))
                                    ){
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
                                userSet.add(userSetKey.toString());

                                userIds.append(",").append(userId);
                                orgIds.append(",").append(userOrgId);
                                userNames.append(",").append(userInfoTmp.getPartyName());
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

            String adviceNote = null;
            Map<String, Object> adviceNoteMap = new HashMap<String, Object>();

            adviceNoteMap.putAll(params);
            adviceNoteMap.putAll(resultMap);
            adviceNoteMap.putAll(reportFocusMap);
            adviceNote = capAdviceNote(proInstance, userInfo, adviceNoteMap);

            Map<String, List<UserBO>> cfgMap = null;
            Map<String, Object> cfgParams = new HashMap<String, Object>();
            String wfEndNodeName = nodeName;

            if(FocusReportNode361Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
                if(FocusReportNode361Enum.FIND_NODE_NAME.getTaskNodeName().equals(curnodeName)) {
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
                if(cfgMap.containsKey(ccTypeSelect)) {
                    Map<String, Object> selectUser = changUserBO2Map(cfgMap.get(ccTypeSelect));
                    selectUser.put("isSelectUser", true);

                    resultMap.put("selectUser", selectUser);
                }
            }

            boolean isShowText = false, isShowDict = false, isShowAddress = false,
                    isShowRegion = false, isBeforeNode = false, isShowMemberChoose = false, isShowTextExtra = false;
            String textLabelName = "", dictLabelName = "", addressLabelName = "", regionLabelName = "", textLabelNameExtra = "";
            Map<String, Object> dynamicContentMap = new HashMap<String, Object>();
            Integer adviceNoteIndex = null;

            if(FocusReportNode361Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
                if(FocusReportNode361Enum.COMMUNITY_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
                    if(FocusReportNode361Enum.END_NODE_NAME.getTaskNodeName().equals(nodeName)){

                        String dynamicDictJson = "[{'name':'问题情况不属实', 'value':'0'},{'name':'问题已整改到位', 'value':'1'}]";

                        isShowDict = true;
                        dictLabelName = "核实结果";

                        dynamicContentMap.put("dynamicDictJson", dynamicDictJson);

                    }
                }else if(FocusReportNode361Enum.STREET_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
                    if(FocusReportNode361Enum.ADMINISTRATION_BUREAU_HANDLE_NODE_NAME.getTaskNodeName().equals(nodeName)){
                        isShowText = true;
                        textLabelName = "原因";
                    }
                }
            }

            if(StringUtils.isNotBlank(adviceNote)) {
                String splitStr = "#OR#";//当一个消息模板中包含有多种执行情况时，使用#OR#进行分割

                if(adviceNote.contains(splitStr)) {
                    String[] adviceNoteArray = adviceNote.split(splitStr);

                    if(adviceNoteIndex != null) {
                        resultMap.put("adviceNote", adviceNoteArray[adviceNoteIndex]);
                    } else {
                        for(int index = 0, len = adviceNoteArray.length; index < len; index++) {
                            dynamicContentMap.put("adviceNote_" + index, adviceNoteArray[index]);
                        }
                    }
                } else {
                    resultMap.put("adviceNote", adviceNote);
                }
            }

            //当下一环节为归档时，增加处理后图片必填约束
            if(FocusReportNode361Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)){
                //当前环节不是发现环节、任务派发环节 处理中图片必填约束
                if(!FocusReportNode361Enum.FIND_NODE_NAME.getTaskNodeName().equals(curnodeName)
                        && !FocusReportNode361Enum.DISTRUBUTE_TASK_NODE_NAME.getTaskNodeName().equals(curnodeName)
                        && !FocusReportNode361Enum.RELEVANT_UNIT_HANDLE_NODE_NAME.getTaskNodeName().equals(curnodeName)){
                    resultMap.put("isUploadHandlingPic", true);
                    resultMap.put("picTypeName", "处理中");
                }
            }

            dynamicContentMap.put("isShowText", isShowText);
            dynamicContentMap.put("textLabelName", textLabelName);
            dynamicContentMap.put("isShowTextExtra", isShowTextExtra);
            dynamicContentMap.put("textLabelNameExtra", textLabelNameExtra);
            dynamicContentMap.put("isShowMemberChoose", isShowMemberChoose);
            dynamicContentMap.put("isShowDict", isShowDict);
            dynamicContentMap.put("isBeforeNode", isBeforeNode);
            dynamicContentMap.put("isShowAddress", isShowAddress);
            dynamicContentMap.put("isShowRegion", isShowRegion);
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

        if(CommonFunctions.isNotBlank(params, "nodeId")) {
            Integer nodeId = Integer.valueOf(params.get("nodeId").toString());
            node = eventDisposalWorkflowService.findNodeById(nodeId);
        }

        //下一环节为 城市管理局处置（task5）时，进行组织节点构造
        if(node != null && FocusReportNode361Enum.ADMINISTRATION_BUREAU_HANDLE_NODE_NAME.toString().equals(node.getNodeName())) {

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

                //if(nodeId > 0) {
                    node = eventDisposalWorkflowService.findNodeById(nodeId);
                    nodeActorsMap = eventDisposalWorkflowService.findNodeActorsById(Integer.valueOf(nodeId));
                //}
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
                //下一环节为 城市管理局处置(task5) 时 进行变更 nodeActorsMap，因为流程图节点指定多组织办理时 此处的 nodeActorsMap依然会查询出所有组织信息下的所有人
                if(null != node && FocusReportNode361Enum.ADMINISTRATION_BUREAU_HANDLE_NODE_NAME.toString().equals(node.getNodeName())){
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
        String formTypeIdStr = null, curTaskName = null,nodeName = null,orgProCode = null;
        Long curUserOrgId = null;
        OrgSocialInfoBO curUserOrgInfo = null;

        if(proInstance != null) {
            curTaskName = proInstance.getCurNode();
            formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
        }
        if(CommonFunctions.isNotBlank(params, "nodeName")){
            nodeName = String.valueOf(params.get("nodeName"));
        }
        if(CommonFunctions.isNotBlank(params, "curUserOrgId")){
            curUserOrgInfo = orgSocialInfoService.findByOrgId(Long.valueOf(String.valueOf(params.get("curUserOrgId"))));
        }
        if(null != curUserOrgInfo){
            orgProCode = curUserOrgInfo.getProfessionCode();
        }

        trigCondition.append(ConstantValue.ENVIRONMENTAL_HEALTH_TREATMENT_NOTE).append("-").append(proInstance.getProName()).append("-").append(curTaskName);

        if(FocusReportNode361Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
            if(FocusReportNode361Enum.FIND_NODE_NAME.getTaskNodeName().equals(curTaskName)){
                trigCondition.append("-").append(nodeName).append("-").append(params.get("dataSource"));
            }else{
                trigCondition.append("-").append(nodeName);
            }
        } else if(StringUtils.isNotBlank(nodeName)) {
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

        String nodeName = null;
        String curNodeName = null;

        if(CommonFunctions.isNotBlank(params,"nodeName")){
            nodeName = String.valueOf(params.get("nodeName"));
        }
        if(CommonFunctions.isNotBlank(params,"curnodeName")){
            curNodeName = String.valueOf(params.get("curnodeName"));
        }

        if(StringUtils.isNotBlank(adviceNote)) {

            if(adviceNote.contains("@problemTypeName@") && CommonFunctions.isNotBlank(params, "problemTypeName")) {
                if(CommonFunctions.isNotBlank(params, "problemType")
                        && String.valueOf(params.get("problemType")).contains("99")
                        && CommonFunctions.isNotBlank(params, "problemDesc")){
                    adviceNote = adviceNote.replace("@problemTypeName@", params.get("problemTypeName").toString() + "(" + params.get("problemDesc") +")");
                }else{
                    adviceNote = adviceNote.replace("@problemTypeName@", params.get("problemTypeName").toString());
                }
            }

            if(adviceNote.contains("@discoveryStaff@") && CommonFunctions.isNotBlank(params, "discoveryStaff")) {
                adviceNote = adviceNote.replace("@discoveryStaff@", params.get("discoveryStaff").toString());
            }

            if(adviceNote.contains("@feedbackTime@") && CommonFunctions.isNotBlank(params, "feedbackTimeStr")) {
                adviceNote = adviceNote.replace("@feedbackTime@", params.get("feedbackTimeStr").toString());
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
