package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.CoordinateInverseQuery.service.ICoordinateInverseQueryService;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:南昌市问题上报工作流处理类
 * @Author: zhangtc
 * @Date: 2020/7/15 17:19
 */
@Service(value = "eventDisposalWorkflowForNCHWTSBService")
public class EventDisposalWorkflowForNCHWTSBServiceImpl extends
        EventDisposalWorkflowForEventNewServiceImpl{

    @Autowired
    private OrgSocialInfoOutService orgSocialInfoService;

    @Autowired
    private MessageOutService messageService;

    @Autowired
    private IEventDisposalWorkflowService eventDisposalWorkflowService;

    @Autowired
    private IFiveKeyElementService fiveKeyElementService;

    @Autowired
    private HessianFlowService hessianFlowService;

    @Autowired
    private IBaseWorkflowService baseWorkflowService;

    @Autowired
    private IEventExtendService eventExtendService;

    @Autowired
    private ICoordinateInverseQueryService coordinateInverseQueryService;

    @Autowired
    private IEventDisposalService eventDisposalService;

    @Autowired
    private IEvent4WorkflowService event4WorkflowService;

    @Autowired
    private UserInfoOutService userInfoService;

    @Autowired
    private UserManageOutService userManageService;

    @Autowired
    private OrgSocialInfoOutService orgSocialInfoOutService;

    @Autowired
    private IFunConfigurationService funConfigurationService;
    //南昌市问题上报事件工作流决策类
    private final String NCHWTSB_DECISION_SERVICE = "workflowEventDecisionMakingService";

    private static final String DISTRICT_NODE_CODE = "task5";	            	//县级办理环节
    protected static final String CLOSE_NODE_CODE = "task8";					//结案环节节点编码
    private static final String POSITION_NAME = "管理员";			    		//进行流程流转得职位名称
    private static final String WTSB_BIZPLATFORM = "3601017";					//南昌市问题上报事件对接平台编码

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param extraParam
     *          infoOrgCode 事件所属街道地域编码 必填
     * */
    @Override
    public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
        StringBuffer msgWrong = new StringBuffer();
        String instanceId = null;
        //禁止采集并结案操作
        if(ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)){
            msgWrong.append("问题上报事件不允许采集并结案操作，请检查！");
        }else {
            UserBO user = new UserBO();
            Map<String, Object> userMap = new HashMap<String, Object>();
            String wftypeId = ConstantValue.EVENT_WORKFLOW_WFTYPEID;
            Integer formId = 0;
            OrgSocialInfoBO orgInfo = null;
            Long orgId = -1L;
            Long userId = -1L;

            if(eventId != null){
                formId = Integer.valueOf(eventId.toString());
            }
            if(userInfo != null){
                orgId = userInfo.getOrgId();
                userId = userInfo.getUserId();
                user.setUserId(userId);
                user.setPartyName(userInfo.getPartyName());
                orgInfo = orgSocialInfoService.findByOrgId(orgId);
            }
            if(extraParam != null) {
                userMap.putAll(extraParam);
            }
            userMap.put("decisionService",NCHWTSB_DECISION_SERVICE);

            //获取评价环节指定的办理人员信息
            //获取评价人员 数据格式 orgCode1:userName11,userName12;orgCode2:userName2;
            Map<String, String> gridOrgUser = new HashMap<String, String>();
            String userNameOrgCodes = funConfigurationService.turnCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.EVALUATE_USER_ORG_ID, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
            StringBuffer userIds = new StringBuffer("");
            if(StringUtils.isNotBlank(userNameOrgCodes)) {
                String[] userNameOrgCodeArray = userNameOrgCodes.split(";");//分割各用户id和组织id组合
                String[] nameCodeArray = new String[]{};
                String regisValues = "";
                String[] regisValueArray = new String[]{};
                String userOrgCode = "";
                OrgSocialInfoBO orgSocialInfo = null;
                UserBO userBo = null;

                for(String userNameOrgCode : userNameOrgCodeArray) {
                    userIds = new StringBuffer("");
                    if(StringUtils.isNotBlank(userNameOrgCode)) {
                        nameCodeArray = userNameOrgCode.split(":");//分割用户id和组织id
                        if(nameCodeArray.length == 2) {
                            userOrgCode = nameCodeArray[0];
                            regisValues = nameCodeArray[1];

                            orgSocialInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(userOrgCode);
                            if(orgSocialInfo != null && orgSocialInfo.getOrgId() != null) {
                                if(StringUtils.isNotBlank(regisValues)) {
                                    regisValueArray = regisValues.split(",");
                                    for(String regisValue : regisValueArray) {
                                        if(StringUtils.isNotBlank(regisValue)) {
                                            userBo = userManageService.getUserInfoByRegistValue(regisValue);
                                            if(userBo != null) {
                                                userIds.append(",").append(userBo.getUserId());
                                            } else {
                                                logger.error("用户名：" + regisValue + " 没有对应的用户！");
                                            }
                                        }
                                    }
                                }
                                if(userIds.length() > 0) {
                                    gridOrgUser.put(orgSocialInfo.getOrgId().toString(), userIds.substring(1));
                                }
                            }
                        } else {
                            logger.error(userNameOrgCode + " 格式不正确！正确格式为：orgCode:regisValue;。");
                        }
                    }
                }
            } else {
                msgWrong.append("缺少评价人员配置信息，功能编码为：").append(ConstantValue.WORKFLOW_ATTRIBUTE).append("；触发条件为：").append(ConstantValue.EVALUATE_USER_ORG_ID).append("；配置类型为：").append(IFunConfigurationService.CFG_TYPE_FACT_VAL);
            }
            if(gridOrgUser.keySet().size() == 0) {
                msgWrong.append(userNameOrgCodes).append(" 没有有效的评价人员！");
            }
            if(!gridOrgUser.isEmpty()) {
                userMap.put("gridOrgUser", gridOrgUser);//可评价人员信息 Map<orgId, userId>
            }
            //启动流程
            instanceId = startFlowByWorkFlow(workFlowId, formId, wftypeId, user, orgInfo, userMap);

            // 事件所属街道地域编码 360121001
            String infoOrgCode = "";
            /*extraParam.put("infoOrgCode",infoOrgCode);*/
            //获取所属地域编码（小程序传参：街道层级地域编码）
            if (CommonFunctions.isNotBlank(extraParam, "infoOrgCode")) {
                infoOrgCode = String.valueOf(extraParam.get("infoOrgCode"));
            } else {
                msgWrong.append("事件所属地域编码为空，请检查小程序端是否正常传参！");
            }
            //根据事件所属地域编码获取对应县区数据参数，进行事件自动提交
            //根据街道地域编码查找对应的街道组织
            OrgSocialInfoBO orgSocialInfoBO_street = null;
            if(StringUtils.isNotBlank(infoOrgCode)){
                orgSocialInfoBO_street = orgSocialInfoOutService.getOrgIdByLocationCode(infoOrgCode);
            }
            //根据街道组织获取对应的县区组织
            Long districtOrgId = -1L;
            OrgSocialInfoBO orgSocialInfoBO_district = null;
            List<OrgSocialInfoBO> parentOrgInfoList = new ArrayList<>();
            if(null != orgSocialInfoBO_street){
                parentOrgInfoList = fiveKeyElementService.findOrgByLevel(orgSocialInfoBO_street.getOrgId(),IFiveKeyElementService.PARENT_LEVEL_ONE,null,null,userInfo,extraParam);
            }else {
                msgWrong.append("街道地域编码对应组织为空，请检查！");
            }
            //获取对应县区org_id,市职能部门-县区 D2S1U3；市级-县区 U2S1U3
            if(null != parentOrgInfoList && parentOrgInfoList.size() > 0){
                for(OrgSocialInfoBO socialInfo:parentOrgInfoList){
                    if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(socialInfo.getOrgType())){
                        orgSocialInfoBO_district = socialInfo;
                        districtOrgId = socialInfo.getOrgId();
                        break;
                    }
                }
            }else {
                msgWrong.append("街道所对应的县区组织为空，请检查！");
            }
            //根据小程序所传事件所属网格编码（街道网格编码），将事件提交到对应的县区职位为管理员得人员办理
            if(null != instanceId && Long.valueOf(instanceId) > 0 && districtOrgId > 0){
                //获取当前环节信息
                //当前环节信息
                Map<String,Object> curDataMap = eventDisposalWorkflowService.curNodeData(Long.valueOf(instanceId));
                String taskId = null;
                String nodeCode = null;
                String nextNodeName = DISTRICT_NODE_CODE;
                String nextStaffId = null;
                //当前环节办理人员orgId
                String curOrgIds = null;
                //下一环节参数，获取指定职位人员需要使用
                Map<String, Object> nextTaskParams = new HashMap<String, Object>();
                List<Node> nextTaskNodeList = null;
                String nextNodeCode = null;
                String advice = "";
                String smsContent = null;
                //查找指定账号
                List<UserInfo> userInfoList = new ArrayList<>();
                Boolean result = false;

                if(CommonFunctions.isNotBlank(curDataMap,"WF_DBID_")){
                    taskId = String.valueOf(curDataMap.get("WF_DBID_"));
                }

                //下一环节节点id：为了获取下一节点配置的职位id
                nextTaskParams.put("instanceId", instanceId);
                nextTaskNodeList = baseWorkflowService.findNextTaskNodes(null, null, null, userInfo, nextTaskParams);
                if(nextTaskNodeList != null && nextTaskNodeList.size() > 0) {
                    Node nextNode = null;
                    //获取县区节点
                    for(Node node:nextTaskNodeList){
                        if(StringUtils.isNotBlank(node.getNodeName()) && DISTRICT_NODE_CODE.equals(node.getNodeName())){
                            nextNode = node;
                            break;
                        }
                    }
                    String nextNodeId = null;
                    if(null != nextNode){
                        nextNodeId = String.valueOf(nextNode.getNodeId());
                        nextNodeCode = nextNode.getNodeCode();
                        nodeCode = nextNode.getTransitionCode();
                        extraParam.put("nodeId",nextNodeId);
                    }else {
                        msgWrong.append("下一办理环节中无县区节点，请检查！");
                    }
                }else {
                    msgWrong.append("缺少可办理的下一环节，请检查！");
                }
                //当前环节节点编码
                if(StringUtils.isBlank(nodeCode)){
                    if(CommonFunctions.isNotBlank(curDataMap,"NODE_CODE")){
                        nodeCode = String.valueOf(curDataMap.get("NODE_CODE"));
                    }
                    //构造节点编码,目前只有市、市职能部门可以采集事件，编码后四位固定 S1U3
                    if(StringUtils.isNotBlank(nodeCode)){
                        nodeCode += "S1U3";
                    }
                }
                //获取对应县区org_id
                advice = "问题上报事件自动派发到"+ orgSocialInfoBO_district.getOrgName() +"处理。";

                UserInfo userAdmin = null;
                //查找自动派发时对应县区办理人员西信息（职位为：管理员 已经在流程图节点中配置）
                userInfoList =  fiveKeyElementService.getUserInfoList(districtOrgId, nodeCode,extraParam);
                if(userInfoList.size() > 0){
                    userAdmin = userInfoList.get(0);
                } else {
                    //南昌市组织下未配置市级管理员账号，请检查
                    msgWrong.append(orgSocialInfoBO_district.getOrgName() + "组织下未配置职位为管理员的账号，请检查！");
                }
                //提交到对应县区处理
                if(null != userAdmin){
                    //下一办理环节人员id
                    nextStaffId = String.valueOf(userAdmin.getUserId());
                    //下一办理人组织id
                    curOrgIds = String.valueOf(userAdmin.getOrgId());
                    result = this.subWorkFlowForEndingAndEvaluate(Long.valueOf(instanceId),taskId,nextNodeName,nextStaffId,curOrgIds,advice,userInfo,smsContent,extraParam);
                }
                if(!result){
                    //事件自动上报到对应县区失败，请检查！
                    msgWrong.append("事件自动派发到"+ orgSocialInfoBO_district.getOrgName() +"失败，请检查！");
                }
            }
        }
        if(StringUtils.isNotBlank(msgWrong.toString())){
            throw new ZhsqEventException(msgWrong.toString());
        }
        return instanceId;
    }

    /*@Override
    public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{

        //判断事件是否是问题上报事件，若是，则不允许采集并结案操作
        boolean result = false;
        if(super.isUserInfoCurrentUser(taskId, instanceId, userInfo)) {
            Map<String, Object> resultMap = null;
            OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
            UserBO user = new UserBO();

            user.setUserId(userInfo.getUserId());
            user.setPartyName(userInfo.getPartyName());

            //结案操作时，到市级指定职位（考核员）的人进行评价
            if(CLOSE_NODE_CODE.equals(nextNodeName)) {
                //下一环节参数，获取指定职位人员需要使用
                Map<String, Object> nextTaskParams = new HashMap<String, Object>();
                List<Node> nextTaskNodeList = null;
                String nodeCode = null;
                List<OrgSocialInfoBO> municipalOrgInfos = orgSocialInfoService.findBySelfIdAndLevel(userInfo.getOrgId(),Integer.valueOf(ConstantValue.MUNICIPAL_ORG_LEVEL));
                StringBuffer nextUserIds = new StringBuffer(),
                             nextOrgIds = new StringBuffer();
                //结案
                nextStaffId = String.valueOf(userInfo.getUserId());
                curOrgIds = String.valueOf(userInfo.getOrgId());
                resultMap = hessianFlowService.subWorkFlowForEvent(taskId, nextNodeName, nextStaffId, curOrgIds, advice, user, orgInfo, smsContent);
                result = resultMap != null;
                //结案成功，再自动提交一步至评价环节
                if(result){
                    //当前环节
                    Map<String,Object> curDataMap = eventDisposalWorkflowService.curNodeData(Long.valueOf(instanceId));
                    if(CommonFunctions.isNotBlank(curDataMap,"WF_DBID_")){
                        taskId = String.valueOf(curDataMap.get("WF_DBID_"));
                    }
                    //下一环节节点id：为了获取下一节点配置的职位id
                    nextTaskParams.put("instanceId", instanceId);
                    nextTaskNodeList = baseWorkflowService.findNextTaskNodes(null, null, null, userInfo, nextTaskParams);
                    if(nextTaskNodeList != null && nextTaskNodeList.size() > 0) {
                        Node nextNode = null;
                        for(Node node:nextTaskNodeList){
                            if(StringUtils.isNotBlank(node.getNodeName()) && EVA_NODE_CODE.equals(node.getNodeName())){
                                nextNode = node;
                                break;
                            }
                        }
                        String nextNodeId = null;
                        if(null != nextNode){
                            nextNodeId = String.valueOf(nextNode.getNodeId());
                            nodeCode = nextNode.getTransitionCode();
                            nextNodeName = nextNode.getNodeName();
                            extraParam.put("nodeId",nextNodeId);
                        }else {
                            throw new Exception("下一办理环节不是评价环节，请检查！");
                        }
                    }else {
                        throw new Exception("缺少可办理的下一环节，请检查！");
                    }
                    List<UserInfo> userInfoList =  null;
                    if(null != municipalOrgInfos && municipalOrgInfos.size() > 0){
                        userInfoList = fiveKeyElementService.getUserInfoList(municipalOrgInfos.get(0).getOrgId(), nodeCode,extraParam);
                    }
                    if(null != userInfoList && userInfoList.size() > 0){
                        for(UserInfo u:userInfoList){
                            nextUserIds.append(u.getUserId()).append(",");
                            nextOrgIds.append(u.getOrgId()).append(",");
                        }
                    }
                    nextStaffId = String.valueOf(nextUserIds).substring(0, nextUserIds.length() - 1);
                    curOrgIds = String.valueOf(nextOrgIds).substring(0, nextOrgIds.length() - 1);
                    resultMap = hessianFlowService.subWorkFlowForEvent(taskId, nextNodeName, nextStaffId, curOrgIds, advice, user, orgInfo, smsContent);
                    result = resultMap != null;
                }
            }else {
                result = super.subWorkFlowForEndingAndEvaluate(instanceId,taskId,nextNodeName,nextStaffId,curOrgIds,advice,userInfo,smsContent,extraParam);
            }
        }
        return result;
    }
*/
    /**
     * 校验用户是否可以启动流程
     * 问题上报启动流程人员必须是市级，且职位是管理员职位
     * */
    @Override
    public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {

        int result = -1;
        StringBuffer msgWrong = new StringBuffer("");

        if (null != orgSocialInfo) {

            String orgLevel = orgSocialInfo.getChiefLevel();
            boolean isAdminPosition = true;

            //判断用户在该组织下是否具有管理员职位
            isAdminPosition = userInfoService.cheakUserForParas(userId, orgSocialInfo.getOrgId(), POSITION_NAME) > 0;

            if (StringUtils.isNotBlank(orgLevel)) {
                if(!ConstantValue.MUNICIPAL_ORG_LEVEL.equals(orgLevel)){
                    msgWrong.append("组织层级必须是市级，当前用户组织层级不是市级，请先检验！");
                }
            }else {
                msgWrong.append("当前组织层级不存在，请先检验！");
            }

            if (!isAdminPosition) {
                msgWrong.append("当前用户不具有[" + POSITION_NAME + "]职位！");
            }
        }else {
            msgWrong.append("当前组织不存在，请先检验！");
        }

        if(msgWrong.length() > 0) {
            throw new Exception(msgWrong.toString());
        } else {
            result = 1;
        }

        return result;
    }
}
