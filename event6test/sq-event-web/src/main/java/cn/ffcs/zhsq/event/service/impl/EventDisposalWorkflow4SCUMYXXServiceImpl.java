package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 智慧城管(Smart City Urban Manage)尤溪县(You Xi Xian) 工作流处理类
 * @ClassName:   EventDisposalWorkflow4SCUMYXXServiceImpl
 * @author:      张天辞(zhangtc)
 * @date:        2021/5/25 8:43
 */
@Service(value = "eventDisposalWorkflow4SCUMYXXService")
public class EventDisposalWorkflow4SCUMYXXServiceImpl  extends EventDisposalWorkflow4SCUMYPQServiceImpl{

    @Autowired
    private OrgSocialInfoOutService orgSocialInfoService;

    @Autowired
    private IFiveKeyElementService fiveKeyElementService;

    @Autowired
    private IEventOrgInfoService eventOrgInfoService;

    @Autowired
    private IEventDisposalService eventDisposalService;

    @Autowired
    private IFunConfigurationService funConfigurationService;

    //事件通用工作流决策类
    private final String EVENT_DECISION_SERVICE = "workflowEventDecisionMakingService";

    @Override
    public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
        String wftypeId = ConstantValue.EVENT_WORKFLOW_WFTYPEID;
        UserBO user = new UserBO();
        OrgSocialInfoBO orgInfo = null;
        Map<String, Object> userMap = null;
        Integer formId = 0;
        Long orgId = -1L;
        Long userId = -1L;
        String chiefLevel = "",//组织层级
                decisionMakingService = null;//决策类
        boolean isSendEvaUser = false, isGridAdmin = false;

        if(userInfo != null){
            orgId = userInfo.getOrgId();
            userId = userInfo.getUserId();
            user.setUserId(userId);
            user.setPartyName(userInfo.getPartyName());
            orgInfo = orgSocialInfoService.findByOrgId(orgId);
        }

        if(workFlowId == null || workFlowId < 0) {
            workFlowId = this.capWorkflowId(null, eventId, userInfo, null);
        }
        if(eventId != null && eventId > 0) {
            formId = Integer.valueOf(eventId.toString());
        }
        //不允许采集并结案操作，无论是否是采集并结案 均置为 不结案
        if(StringUtils.isBlank(toClose) || ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)){
            toClose = ConstantValue.WORKFLOW_IS_NO_CLOSE;
        }
        if(orgInfo != null) {
            chiefLevel = orgInfo.getChiefLevel();//组织层级
            userMap = new HashMap<String, Object>();
            userMap.put("orgCode", orgInfo.getOrgCode());
            userMap.put("orgType", orgInfo.getOrgType()); //组织类型

            //查找父级
            List<OrgSocialInfoBO> parentOrgInfo = new ArrayList<OrgSocialInfoBO>();
            Map<String, Object> parentOrgExtraParam = new HashMap<String, Object>();
            parentOrgExtraParam.put("eventId", eventId);

            try {
                parentOrgInfo = fiveKeyElementService.findOrgByLevel(orgId, IFiveKeyElementService.PARENT_LEVEL_ONE, ConstantValue.GOV_PROFESSION_CODE, "", userInfo, parentOrgExtraParam);
            } catch (Exception e) {
                parentOrgInfo = new ArrayList<OrgSocialInfoBO>();
                e.printStackTrace();
            }

            StringBuffer parentOrgIdBuffer = new StringBuffer("");
            Map<String, String> gridOrgUser = new HashMap<String, String>();
            Long parentOrgId = -1L;
            //不限制为只在结案时传入，是为了采集并结案分步操作时，也能得到对应的评价人员
            isGridAdmin = fiveKeyElementService.isUserIdGridAdmin(orgId, userId);
            isSendEvaUser = (isGridAdmin ||				//当当前用户为网格员时，传递可评价人员信息
                    chiefLevel.equals(ConstantValue.UNITED_PREVENTION_GROUP_ORG_LEVEL));	//当上级为网格员办理时，需要过滤出上级为网格员的用户，目前只有联防组网格才有此操作

            for(OrgSocialInfoBO orgSocialInfoBO : parentOrgInfo) {
                if(orgSocialInfoBO != null) {
                    parentOrgId = orgSocialInfoBO.getOrgId();
                    parentOrgIdBuffer.append(",").append(parentOrgId);

                    if(isSendEvaUser) {
                        try {
                            List<UserBO> userBoList = fiveKeyElementService.findCollectToCloseUserList(parentOrgId);

                            if(chiefLevel.equals(ConstantValue.UNITED_PREVENTION_GROUP_ORG_LEVEL)) {
                                userBoList = fiveKeyElementService.filterGridAdmin(parentOrgId, userBoList, false);
                            }

                            if(userBoList!=null && userBoList.size()>0){
                                StringBuffer gridUserIds = new StringBuffer("");

                                for(UserBO userBO : userBoList){
                                    gridUserIds.append(",").append(userBO.getUserId());
                                }

                                if(gridUserIds.length() > 0) {
                                    gridOrgUser.put(String.valueOf(parentOrgId), gridUserIds.substring(1));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if(parentOrgIdBuffer.length() > 0) {
                parentOrgIdBuffer = new StringBuffer(parentOrgIdBuffer.substring(1));
            }

            if(isSendEvaUser) {
                if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose) && gridOrgUser.isEmpty()) {
                    throw new Exception("缺少可评价人员！");
                } else {
                    userMap.put("gridOrgUser", gridOrgUser);//可评价人员信息 Map<orgId, userIds>
                }
            } else {
                userMap.put("parentOrgId", parentOrgIdBuffer.toString());//上级组织id，以英文逗号相连接
            }

            userMap.put("orgChiefLevel", chiefLevel);
            userMap.put("isGovernment", eventOrgInfoService.isGovernmentForString(orgInfo));

            if(eventId != null && eventId > 0) {
                EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
                if(event != null) {
                    decisionMakingService = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.DECISION_MAKING_SERVICE + "_event_" + event.getType(), IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, orgInfo.getOrgCode(), IFunConfigurationService.DIRECTION_UP_FUZZY);
                }
            }
        }

        if(StringUtils.isBlank(decisionMakingService)) {
            decisionMakingService = EVENT_DECISION_SERVICE;
        }

        userMap.put("toClose", toClose);
        userMap.put("isGridAdmin", fiveKeyElementService.isUserIdGridAdminForString(isGridAdmin));
        userMap.put("isNewOrganization", eventOrgInfoService.isNewOrganizationForString(orgInfo));
        userMap.put("decisionService", decisionMakingService);

        if(extraParam != null) {
            userMap.putAll(extraParam);
        }

        return startFlowByWorkFlow(workFlowId, formId, wftypeId, user, orgInfo, userMap);
    }

}
