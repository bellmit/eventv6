package cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
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
 * @Description:三会两课人员选择
 * @Author: zhangtc
 * @Date: 2020/12/4 9:21
 */
@Service(value = "fiveKeyElement4MeetingService")
public class FiveKeyElement4MeetingServiceImpl extends FiveKeyElement4TwoVioPreServiceImpl {
    @Autowired
    private OrgSocialInfoOutService orgSocialInfoService;

    @Autowired
    private UserManageOutService userManageService;

    @Autowired
    private IBaseWorkflowService baseWorkflowService;

    @Autowired
    private IReportIntegrationService reportIntegrationService;

    @Autowired
    private IReportMsgCCCfgService reportMsgCCCfgService;

    @Override
    public Map<String, Object> getNodeInfoForEvent(
            UserInfo userInfo,
            String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
            throws Exception {
        INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
        Long reportId = null, instanceId = null;
        Map<String, Object> reportParams = new HashMap<String, Object>(),
                reportFocusMap = null,
                resultMap = new HashMap<String, Object>();
        ProInstance proInstance = null;
        String formTypeIdStr = null, workflowName = null;
        boolean isUserRadioStyle = true, isSelectOrg = false;
        String reportType = ReportTypeEnum.meetingAndLesson.getReportTypeIndex();

        params = params == null ? new HashMap<String, Object>() : params;

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

            if(CommonFunctions.isBlank(params, "formTypeId")) {
                params.put("formTypeId", formTypeIdStr);
            }
        }

        reportParams.putAll(params);
        reportParams.put("reportId", reportId);
        reportParams.put("reportType", reportType);
        reportFocusMap = reportIntegrationService.findReportFocusByUUID(null, userInfo, reportParams);

        if(CommonFunctions.isNotBlank(reportFocusMap, "regionCode")) {
            boolean isAnalysisOperateUser = true;
            OrgSocialInfoBO orgInfo = null;
            Map<String, Object> orgParam = new HashMap<String, Object>();
			List<OrgSocialInfoBO> orgInfoList = null;
			
			orgParam.put("regionCode", reportFocusMap.get("regionCode").toString());
			orgParam.put("orgType", ConstantValue.ORG_TYPE_UNIT);
			orgParam.put("professionCode", ConstantValue.GOV_PROFESSION_CODE);
			
			orgInfoList = orgSocialInfoService.findOrgSocialListByPara(orgParam);
			
			if(orgInfoList != null && orgInfoList.size() > 0) {
				orgInfo = orgInfoList.get(0);
			}
			
            if(CommonFunctions.isNotBlank(params, "isAnalysisOperateUser")) {
                isAnalysisOperateUser = Boolean.valueOf(params.get("isAnalysisOperateUser").toString());
            }

            if(isAnalysisOperateUser) {
                if(nodeCodeHandler.isEquality()) {
                    List<UserInfo> nextUserInfoList = getUserInfoList(userInfo.getOrgId(), nodeCode, params);

                    if(nextUserInfoList != null) {
                        resultMap = new HashMap<String, Object>();
                        StringBuffer userIdBuffer = new StringBuffer(""),
                                userNameBuffer = new StringBuffer(""),
                                orgIdBuffer = new StringBuffer("");

                        for(UserInfo nextUserInfo : nextUserInfoList) {
                            userIdBuffer.append(",").append(nextUserInfo.getUserId());
                            userNameBuffer.append(",").append(nextUserInfo.getPartyName());
                            orgIdBuffer.append(",").append(nextUserInfo.getOrgId());
                        }

                        if(userIdBuffer.length() > 0) {
                            resultMap.put("userIds", userIdBuffer.substring(1));
                        }
                        if(userNameBuffer.length() > 0) {
                            resultMap.put("userNames", userNameBuffer.substring(1));
                        }
                        if(orgIdBuffer.length() > 0) {
                            resultMap.put("orgIds", orgIdBuffer.substring(1));
                        }
                    }
                } else if(nodeCodeHandler.isToBegin() && proInstance != null) {
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
                    }

                    //modify by ztc 20211029 市直部门采集的所属区域可以选择到南安市
                    if(ConstantValue.GRID_ORG_LEVEL.equals(orgChiefLevel)
                            || ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)
                            || ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel)
                            || ConstantValue.DISTRICT_ORG_LEVEL.equals(orgChiefLevel)) {
                        UserInfo userInfoTmp = new UserInfo();
                        int toLevel = nodeCodeHandler.getToLevel();

                        userInfoTmp.setOrgId(orgInfo.getOrgId());
                        userInfoTmp.setOrgName(orgInfo.getOrgName());
                        userInfoTmp.setOrgCode(orgInfo.getOrgCode());

                        if(toLevel == INodeCodeHandler.COMMUNITY) {
                            //二级网格长上报至网格层级获取主送人
                            userInfoList = super.getUserInfoList(orgInfo.getParentOrgId(), nodeCode, params);
                        } else if(toLevel > 0 && toLevel < INodeCodeHandler.COMMUNITY) {
                            String nodeCodeTmp = nodeCode;
                            List<GdZTreeNode> treeNodeList = super.getTreeForEvent(userInfo, null, nodeCodeTmp, null, null, params);
                            if(treeNodeList != null) {
                                userInfoList = new ArrayList<UserInfo>();
                                for(GdZTreeNode treeNode : treeNodeList) {
                                    userInfoList.addAll(super.getUserInfoList(Long.valueOf(treeNode.getId()), nodeCodeTmp, params));
                                }
                            }
                        } else if(toLevel < 0) {
                            isUserRadioStyle = false;
                            resultMap = super.getNodeInfoForEvent(userInfoTmp, curnodeName, nodeName, nodeCode, nodeId, params);
                        }
                    } else {
                        throw new ZhsqEventException("发生地址没有匹配到网格或村社区层级的组织信息！");
                    }

                    if(userInfoList != null) {
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
                    }
                }

                resultMap.put("isSelectUser", false);
                resultMap.put("isSelectOrg", isSelectOrg);
                //主送人员设置为单选
                resultMap.put("isUserRadioStyle", isUserRadioStyle);
                
                if(isUserRadioStyle && CommonFunctions.isNotBlank(resultMap, "userIds")) {
					String userIds = resultMap.get("userIds").toString();
					//只有当主送为一个人时，默认选中该人员
					resultMap.put("isUserDefaulCheck", userIds.split(",").length == 1);
				}
            }

            String adviceNoteInitial = null, adviceNote = null;
            Map<String, Object> adviceNoteMap = new HashMap<String, Object>();

            adviceNoteMap.putAll(params);
            adviceNoteMap.putAll(resultMap);
            adviceNoteMap.putAll(reportFocusMap);
            adviceNoteMap.put("isReplaceAdviceNote", false);
            //获取模板
            adviceNoteInitial = capAdviceNote(proInstance, userInfo, adviceNoteMap);
            adviceNote = capAdviceNote(adviceNoteInitial, userInfo, adviceNoteMap);

            Map<String, List<UserBO>> cfgMap = null;
            Map<String, Object> cfgParams = new HashMap<String, Object>();
            List<Map<String, Object>> cfgMapList = null;
            OrgSocialInfoBO benchmarkOrgInfo = orgInfo;

            cfgParams.put("workflowName", workflowName);
            cfgParams.put("wfEndNodeName", nodeName);
            cfgParams.put("ccType", ReportMsgCCTypeEnum.CCTYPE_DISTRIBUTE.getCcType() + "," + ReportMsgCCTypeEnum.CCTYPE_SELECT.getCcType());
            
            cfgMapList = reportMsgCCCfgService.findCfg4List(cfgParams);
            
            if(cfgMapList != null && userInfo != null) {
            	String benchmarkOrgCodeReplace = userInfo.getOrgCode();
            	boolean isBenchmarkOrgCodeReplaced = false;
            	
            	for(Map<String, Object> cfgMapT : cfgMapList) {
            		if(CommonFunctions.isNotBlank(cfgMapT, "benchmarkOrgCode")) {
            			cfgMapT.put("benchmarkOrgCode", benchmarkOrgCodeReplace);
            			isBenchmarkOrgCodeReplaced = true;
            		}
            	}
            	
            	if(isBenchmarkOrgCodeReplaced) {
            		Long userOrgId = userInfo.getOrgId();
            		
            		benchmarkOrgInfo = orgSocialInfoService.findByOrgId(userOrgId);
            	}
            }
            //分送人员
            cfgMap = reportMsgCCCfgService.findCfg4User(cfgParams, benchmarkOrgInfo, cfgMapList);

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
                    //封装分送人员
                    resultMap.put("distributeUser", distributeUser);
                }

                //选送人员
                if(cfgMap.containsKey(ccTypeSelect)) {
                    Map<String, Object> selectUser = changUserBO2Map(cfgMap.get(ccTypeSelect));
                    selectUser.put("isSelectUser", true);

                    resultMap.put("selectUser", selectUser);
                }
            }

            Map<String, Object> dynamicContentMap = new HashMap<String, Object>();

            if(StringUtils.isNotBlank(adviceNote)) {
                String splitStr = "#OR#";//当一个消息模板中包含有多种执行情况时，使用#OR#进行分割

                if(adviceNote.contains(splitStr)) {
                    String[] adviceNoteArray = adviceNote.split(splitStr),
                    		 adviceNoteInitialArray = adviceNoteInitial.split(splitStr);

                    for(int index = 0, len = adviceNoteArray.length; index < len; index++) {
                        dynamicContentMap.put("adviceNote_" + index, adviceNoteArray[index]);
                        dynamicContentMap.put("adviceNoteInitial_" + index, adviceNoteInitialArray[index]);
                    }
                } else {
                    resultMap.put("adviceNote", adviceNote);
                    resultMap.put("adviceNoteInitial", adviceNoteInitial);
                }
            }

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
        List<GdZTreeNode> nodes = super.getTreeForEvent(userInfo, orgId, nodeCode, level, professionCode, params);
        return nodes;
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
        String formTypeIdStr = null, curTaskName = null;

        if(proInstance != null) {
            curTaskName = proInstance.getCurNode();
            formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
        }

        trigCondition.append(ConstantValue.MEETING_NOTE).append("-").append(proInstance.getProName()).append("-").append(curTaskName);

        if(CommonFunctions.isNotBlank(params, "meetingType")) {
            trigCondition.append("-").append(params.get("meetingType"));
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
            if(adviceNote.contains("@partyGroupName@") && CommonFunctions.isNotBlank(params, "partyGroupName")) {
                adviceNote = adviceNote.replace("@partyGroupName@", params.get("partyGroupName").toString());
            }
            if(adviceNote.contains("@meetingTypeName@") && CommonFunctions.isNotBlank(params, "meetingTypeName")) {
                adviceNote = adviceNote.replace("@meetingTypeName@", params.get("meetingTypeName").toString());
            }
            if(adviceNote.contains("@shouldAttriveNum@") && CommonFunctions.isNotBlank(params, "shouldAttriveNum")) {
                adviceNote = adviceNote.replace("@shouldAttriveNum@", params.get("shouldAttriveNum").toString());
            }
            if(adviceNote.contains("@actualNumber@") && CommonFunctions.isNotBlank(params, "actualNumber")) {
                adviceNote = adviceNote.replace("@actualNumber@", params.get("actualNumber").toString());
            }
            if(adviceNote.contains("@meetingTimeZH@") && CommonFunctions.isNotBlank(params, "meetingTime")) {
                adviceNote = adviceNote.replace("@meetingTimeZH@", DateUtils.formatDate((Date) params.get("meetingTime"), "yyyy年MM月dd日"));
            }
        }

        return adviceNote;
    }

    /**
     * 依据组织id获取下一环节可办理人员信息
     * @param orgId		基准组织id
     * @param nodeCode	节点操作编码
     * @param params	额外参数
     * @return
     * @throws Exception
     */
    private Map<String, Object> findNextUserByOrgId(Long orgId, String nodeCode, Map<String, Object> params) throws Exception {
        OrgSocialInfoBO userOrgInfo = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if(orgId != null && orgId > 0) {
            userOrgInfo = orgSocialInfoService.findByOrgId(orgId);
        }

        if(userOrgInfo != null) {
            Map<Long, OrgSocialInfoBO> orgInfoMap = null;
            List<OrgSocialInfoBO> orgInfoList = new ArrayList<OrgSocialInfoBO>();

            orgInfoList.add(userOrgInfo);
            orgInfoMap = findDeptByFilterProffession(orgInfoList, null, params, null);

            if(orgInfoMap != null && !orgInfoMap.isEmpty()) {
                OrgSocialInfoBO orgInfo = orgInfoMap.get(orgInfoMap.keySet().toArray()[0]);

                if(orgInfo != null) {
                    resultMap.putAll(findNextUserInfoByOrgId(orgInfo.getOrgId(), nodeCode, params));
                }
            }
        }

        return resultMap;
    }

    /**
     * 依据组织id获取下一环节可办理人员信息
     * @param orgId		组织id
     * @param nodeCode	节点操作编码
     * @param params	额外参数
     * @return
     * @throws Exception
     */
    private Map<String, Object> findNextUserInfoByOrgId(Long orgId, String nodeCode, Map<String, Object> params) throws Exception {
        List<UserInfo> nextUserInfoList = getUserInfoList(orgId, nodeCode, null, params);
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if(nextUserInfoList != null && nextUserInfoList.size() > 0) {
            StringBuffer userIds = new StringBuffer(""),
                    orgIds = new StringBuffer(""),
                    userNames = new StringBuffer("");

            for(UserInfo nextUser : nextUserInfoList) {
                userIds.append(",").append(nextUser.getUserId());
                orgIds.append(",").append(nextUser.getOrgId());
                userNames.append(",").append(nextUser.getPartyName());
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

            resultMap.put("isSelectUser", false);
            resultMap.put("isSelectOrg", false);
            resultMap.put("isUserRadioStyle", true);
        }

        return resultMap;
    }
}
