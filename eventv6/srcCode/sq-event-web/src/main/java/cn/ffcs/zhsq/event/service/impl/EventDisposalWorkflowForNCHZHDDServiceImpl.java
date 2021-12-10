package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.common.message.bo.ReceiverBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.CoordinateInverseQuery.service.ICoordinateInverseQueryService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.CoordinateInverseQuery.CoordinateInverseQueryGridInfo;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventExceptionEnum;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.data.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:南昌市指挥调度工作流处理类
 * @Author: zhangtc
 * @Date: 2019/7/6 10:33
 */
@Service(value = "eventDisposalWorkflowForNCHZHDDService")
public class EventDisposalWorkflowForNCHZHDDServiceImpl extends
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

    @Resource(name = "workflowNCHZHDDDecisionMakingService")
    private IWorkflowDecisionMakingService<String> workflowDecisionMakingService;

    @Autowired
    private ICoordinateInverseQueryService coordinateInverseQueryService;

    @Autowired
    private IEventDisposalService eventDisposalService;
    
    @Autowired
	private IEvent4WorkflowService event4WorkflowService;

    //南昌市指挥调度事件工作流决策类
    private final String NCHZHDD_DECISION_SERVICE = "workflowNCHZHDDDecisionMakingService";

    private static final String TOWNDISPOSAl_NODE_CODE = "task4";	        			//乡镇处理环节
    private static final String DISTRICT_NODE_CODE = "task5";	            				//县级办理环节
    private static final String DISTRICT_DEPARTMENT_NODE_CODE = "task7";		//县区职能部门办理
    protected static final String CLOSE_NODE_CODE = "task8";								//结案环节节点编码
    private static final String MUNICIPAL_NODE_CODE = "task10";							//市级处理环节
    private static final String MUNICIPAL_DEPARTMENT_NODE_CODE = "task11";	//市职能部门处理
    private static final String NC_ZONGZHI_NODE_CODE = "task12";						//南昌市综治平台
    private static final String NC_12345_NODE_CODE = "task15";						//12345热线
    private static final String MUNICIPAL_ADMIN = "市级管理员";			    				//南昌市市级管理员账号姓名
    private static final String NC_ZZPT_ORG_NODE = "南昌市综治平台";					//南昌市综治平台组织节点名称
    private static final String NC_12345_ORG_NODE = "12345热线";					//12345热线组织节点名称

    //巡防类型
    private static final String CROSSING_PATROL_TYPE = "1";
    private static final String SELF_PATROL_TYPE = "2";
    private static final String EVENT_MEG_MODULE_CODE = "02";//事件消息所属模块编码

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {

        UserBO user = new UserBO();
        Map<String, Object> userMap = new HashMap<>();
        String wftypeId = ConstantValue.EVENT_WORKFLOW_WFTYPEID;
        Integer formId = 0;
        OrgSocialInfoBO orgInfo = null;
        Long orgId = -1L;
        Long userId = -1L;
        String instanceId = null;
        StringBuffer msgWrong = new StringBuffer();

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

        userMap.put("decisionService", NCHZHDD_DECISION_SERVICE);

        if(extraParam != null) {
            userMap.putAll(extraParam);
        }
        
		// 获取事件的巡防类型
		String patrolType = "", // 巡防类型
				orgLevel = "", // 组织层级
				orgType = "";// 组织类型

		//获取事件的巡访类型
		if (CommonFunctions.isNotBlank(extraParam, "patrolType")) {
			patrolType = String.valueOf(extraParam.get("patrolType"));
		} else {
			Map<String, Object> eventMap = eventExtendService.findEventExtendByEventId(eventId);
			if (CommonFunctions.isNotBlank(eventMap, "patrolType")) {
				patrolType = String.valueOf(eventMap.get("patrolType"));
			}
			extraParam.put("patrolType", patrolType);
		}
		
		// 如果巡防类型是交叉巡防，则需限制每个人每月上报数不超过10条
		if (patrolType.equals(CROSSING_PATROL_TYPE)) {
			Map<String, Object> searchParams = new HashMap<String, Object>();
			
			//获取当前月份的第一天和最后一天
			Map<String, Object> firstLastDayByMonth = DateUtils.getFirstLastDayByMonth(new Date(), DateUtils.PATTERN_DATE, false);
			String firstDayOfMonth=firstLastDayByMonth.get("first").toString();
			String lastDayOfMonth=firstLastDayByMonth.get("last").toString();
			
			//searchParams.put("infoOrgCode", orgInfo.getOrgCode());
			searchParams.put("orgCode", orgInfo.getOrgCode());
			searchParams.put("listType", "6");
			searchParams.put("initiatorId", userId);
			searchParams.put("initiatorOrgId", -1);
			searchParams.put("happenTimeStart", firstDayOfMonth);
			searchParams.put("happenTimeEnd", lastDayOfMonth);
			searchParams.put("patrolType", CROSSING_PATROL_TYPE);

			int count = event4WorkflowService.findEventCount(searchParams);
			if (count >= 10) {
				throw new ZhsqEventException(ZhsqEventExceptionEnum.OVERREPORTING_EXCEPTION.getCode(),ZhsqEventExceptionEnum.OVERREPORTING_EXCEPTION.getMessage());
			}
		}

        instanceId = this.startFlowByWorkFlow(workFlowId, formId, wftypeId, user, orgInfo, userMap);

        //如果巡防类型不存在，则不进行自动流转操作
        if(StringUtils.isBlank(patrolType)) {
        	throw new Exception("未查询到事件的巡访类型！");
        }
        
        if(null != orgInfo){
            orgLevel = orgInfo.getChiefLevel();
            orgType = orgInfo.getOrgType();
        }


        //组织层级为县区级别的职能部门时，进行事件的自动提交；县区职能部门不止巡防团
        if(null != instanceId && Long.valueOf(instanceId) > 0 && String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType) && ConstantValue.DISTRICT_ORG_LEVEL.equals(orgLevel)){

            //获取当前环节信息
            //当前环节信息
            Map<String,Object> curDataMap = eventDisposalWorkflowService.curNodeData(Long.valueOf(instanceId));
            String taskId = null;
            String nextNodeName = MUNICIPAL_NODE_CODE;
            String nextStaffId = null;
            //当前环节办理人员orgId
            String curOrgIds = null;
            String advice = "";
            String smsContent = null;
            //查找指定账号
            List<UserInfo> userInfoList = new ArrayList<>();
            Boolean result = false;

            if(CommonFunctions.isNotBlank(curDataMap,"WF_DBID_")){
                taskId = String.valueOf(curDataMap.get("WF_DBID_"));
            }

            //巡防类型为交叉寻访
            if(CROSSING_PATROL_TYPE.equals(patrolType)){
                //获取南昌市org_id,U3R1U2
                advice = "交叉巡防事件自动上报到南昌市处理。";
                Long nanChangOrgId = -1L;
                List<OrgSocialInfoBO> parentOrgInfoList = fiveKeyElementService.findOrgByLevel(userInfo.getOrgId(),IFiveKeyElementService.PARENT_LEVEL_TWO,null,null,userInfo,extraParam);

                if(null != parentOrgInfoList && parentOrgInfoList.size() > 0){
                    nanChangOrgId = parentOrgInfoList.get(0).getOrgId();
                }

                UserInfo userAdmin = null;

                //查找自动派发时南昌市办理人员-账号名字为:"市级管理员"
                if(nanChangOrgId > 0){
                    userInfoList =  fiveKeyElementService.getUserInfoList(nanChangOrgId, MUNICIPAL_NODE_CODE,extraParam);
                }
                if(userInfoList.size() > 0){
                    for(UserInfo u:userInfoList){
                        if(MUNICIPAL_ADMIN.equals(u.getPartyName())){
                            userAdmin = u;
                        }
                    }
                } else {
                    //南昌市组织下未配置市级管理员账号，请检查
                    msgWrong.append("南昌市组织下未配置市级管理员账号，请检查！");
                }
                //提交到市级处理
                if(null != userAdmin){
                    //下一办理环节人员id
                    nextStaffId = String.valueOf(userAdmin.getUserId());
                    //下一办理人组织id
                    curOrgIds = String.valueOf(userAdmin.getOrgId());
                    result = this.subWorkFlowForEndingAndEvaluate(Long.valueOf(instanceId),taskId,nextNodeName,nextStaffId,curOrgIds,advice,userInfo,smsContent,extraParam);
                }

                //提交至南昌市办理成功后，再次提交至南昌市综治平台环节（修改为自动提交到12345热线）
                if(result){
                    //南昌市综治平台接收账号
                    UserInfo userZZAdmin = null;
                    userInfoList = null;
                    curDataMap = null;
                    orgId = -1L;
                    result = false;
                    //构造南昌市综治平台环节编码（改为构造12345热线环节编码）
                    String ZZPT_NODE_CODE = INodeCodeHandler.ORG_UINT + ConstantValue.MUNICIPAL_ORG_LEVEL + INodeCodeHandler.OPERATE_FLOW
                            + "0" + INodeCodeHandler.ORG_DEPT + ConstantValue.MUNICIPAL_ORG_LEVEL;
                    //获取当前环节--市级办理环节
                    curDataMap = eventDisposalWorkflowService.curNodeData(Long.valueOf(instanceId));

                    if(null != curDataMap && CommonFunctions.isNotBlank(curDataMap,"WF_DBID_")){
                        taskId = String.valueOf(curDataMap.get("WF_DBID_"));
                    }

                    extraParam.put("nodeId","");
                    extraParam.put("nodeCode",ZZPT_NODE_CODE);
                    extraParam.put("formId",eventId);

                    List<GdZTreeNode> treeNodeList = fiveKeyElementService.getTreeForEvent(userAdmin,null,ZZPT_NODE_CODE,null,null,extraParam);
                    //获取综治平台办理人员信息
                    GdZTreeNode NC_ZZPT_NODE = null;

                    if(null != treeNodeList && treeNodeList.size() > 0){
                        for(GdZTreeNode zTreeNode:treeNodeList){
                            if(NC_12345_ORG_NODE.equals(zTreeNode.getName())){
                                NC_ZZPT_NODE = zTreeNode;
                            }
                        }
                    }
                    if(null != NC_ZZPT_NODE && Long.valueOf(NC_ZZPT_NODE.getId())>0){
                        orgId = Long.valueOf(NC_ZZPT_NODE.getId());
                        extraParam.put("id",NC_ZZPT_NODE.getId());

                        userInfoList = fiveKeyElementService.getUserInfoList(orgId,ZZPT_NODE_CODE,null,extraParam);
                        //南昌市综治平台组织下只配置一个账号
                        if(null != userInfoList && userInfoList.size() == 1){
                            userZZAdmin = userInfoList.get(0);
                        }else {
                            //南昌市综治平台组织下需配置且只能配置一个南昌市综治平台账号，请检查
                            //msgWrong.append("南昌市综治平台组织下需配置且只能配置一个南昌市综治平台账号，请检查！");
                            msgWrong.append("12345热线组织下需配置且只能配置一个12345热线账号，请检查！");
                        }
                    }
                    //nextNodeName = NC_ZONGZHI_NODE_CODE;
                    nextNodeName = NC_12345_NODE_CODE;//改为自动流转到12345平台
                    if(null != userZZAdmin){
                        //下一办理环节人员id
                        nextStaffId = String.valueOf(userZZAdmin.getUserId());
                        //下一办理人组织id
                        curOrgIds = String.valueOf(userZZAdmin.getOrgId());
                        //advice = "南昌市自动将事件派发到南昌市综治平台。";
                        advice = "南昌市自动将事件派发到12345热线。";
                    }

                    //提交到市级节点成功后，再获取下一办理环节，自动提交到南昌综治平台节点
                    result = this.subWorkFlowForEndingAndEvaluate(Long.valueOf(instanceId),taskId,nextNodeName,nextStaffId,curOrgIds,advice,userAdmin,smsContent,extraParam);
                    if(!result){
                        //南昌市自动将事件派发到南昌市综治平台失败，请检查！
                        //msgWrong.append("南昌市自动将事件派发到南昌市综治平台失败，请检查！");
                        msgWrong.append("南昌市自动将事件派发到12345热线失败，请检查！");
                    }
                }else {
                    //事件自动上报到南昌市失败，请检查！
                    msgWrong.append("事件自动上报到南昌市失败，请检查！");
                }
            }
            //自身寻访自动提交:根据事件经纬度，定位到事件所属县区，自动提交
            if(SELF_PATROL_TYPE.equals(patrolType)){

                Map<String,Object> params = new HashMap<>();
                List<CoordinateInverseQueryGridInfo> gridInfoList = new ArrayList<>();
                //县区办理人员，根据职位过滤
                List<UserBO> userBOList = null;
                UserBO userBO = null;
                //构造县区环节编码：D3F0U3
                String DISTRICT_NODE_CODE_ = INodeCodeHandler.ORG_DEPT + ConstantValue.DISTRICT_ORG_LEVEL + INodeCodeHandler.OPERATE_FLOW
                        + "0" + INodeCodeHandler.ORG_UINT + ConstantValue.DISTRICT_ORG_LEVEL;
                //县区下职位名称为："巡防管理员"
                String POSITION_NAME = "巡防管理员";
                //当前环节
                curDataMap = eventDisposalWorkflowService.curNodeData(Long.valueOf(instanceId));

                if(null != curDataMap && CommonFunctions.isNotBlank(curDataMap,"WF_DBID_")){
                    taskId = String.valueOf(curDataMap.get("WF_DBID_"));
                }

                extraParam.put("nodeCode",DISTRICT_NODE_CODE_);

                /*extraParam.put("longitude", "116.02837647265638");
                extraParam.put("latitude", "28.5613901191406");*/
                if(CommonFunctions.isNotBlank(extraParam,"longitude") && CommonFunctions.isNotBlank(extraParam,"latitude")){
                    params.put("x", extraParam.get("longitude"));
                    params.put("y", extraParam.get("latitude"));
                }else {
                    //经纬度传参为空时调用事件接口获取事件经纬度
                    EventDisposal event = eventDisposalService.findEventByIdAndMapt(eventId,null,userInfo.getOrgCode());
                    if(null != event && null != event.getResMarker() && null != event.getResMarker().getX() && null != event.getResMarker().getY()){
                        params.put("x", event.getResMarker().getX());
                        params.put("y", event.getResMarker().getY());
                    }
                }

                if(CommonFunctions.isNotBlank(params,"x") && CommonFunctions.isNotBlank(params,"y")){
                    params.put("gridLevel",ConstantValue.DISTRICT_ORG_LEVEL);
                    gridInfoList = coordinateInverseQueryService.findGridInfo(params);
                }else {
                    msgWrong.append("自身巡防事件缺失经纬度，请检查！");
                }

                if(gridInfoList.size() > 0){
                    CoordinateInverseQueryGridInfo gridInfo = gridInfoList.get(0);
                    //获取网格所属地域
                    String infoOrgCode = gridInfo.getInfoOrgCode();
                    OrgSocialInfoBO orgSocialInfoBO = null;

                    //获取地域下组织
                    if(StringUtils.isNotBlank(infoOrgCode)){
                        orgSocialInfoBO =  orgSocialInfoService.getOrgIdByLocationCode(infoOrgCode);
                    }

                    if(null != orgSocialInfoBO){
                        INodeCodeHandler nodeCodeHandler = fiveKeyElementService.createNodeCodeHandle(DISTRICT_NODE_CODE_);
                        if(null != nodeCodeHandler){
                            userBOList = fiveKeyElementService.getReportUserList(nodeCodeHandler, POSITION_NAME, null, null,  orgSocialInfoBO.getOrgId(),  infoOrgCode);
                        }
                    }

                    if(null != userBOList && userBOList.size() > 0){
                        userBO = userBOList.get(0);
                        if(null != userBO){
                            //下一办理环节人员id
                            nextStaffId = String.valueOf(userBO.getUserId());
                            //下一办理人组织id
                            curOrgIds = String.valueOf(userBO.getSocialOrgId());
                            nextNodeName = DISTRICT_NODE_CODE;

                            /*userAdmin.setUserId(userBO.getUserId());
                            userAdmin.setOrgId(Long.valueOf(userBO.getSocialOrgId()));
                            userAdmin.setUserName(userBO.getPartyName());
                            userAdmin.setOrgCode(userBO.getOrgCode());
                            userAdmin.setOrgName(userBO.getOrgName());*/

                            advice = "自身巡防事件自动上报到到县区处理。";
                        }
                    }
                    //提交到市级节点成功后，再获取下一办理环节，自动提交到南昌综治平台节点
                    result = this.subWorkFlowForEndingAndEvaluate(Long.valueOf(instanceId),taskId,nextNodeName,nextStaffId,curOrgIds,advice,userInfo,smsContent,extraParam);

                    if(!result){
                        msgWrong.append("自身巡防事件自动上报到到县区处理失败，请检查！");
                    }

                }else {
                    msgWrong.append("自身巡防事件所属县区网格转换失败，请检查县区网格是否配置！");
                }

            }


        }



        if(StringUtils.isNotBlank(msgWrong.toString())){
            throw new ZhsqEventException(msgWrong.toString());
        }
        return instanceId;
    }

    @Override
    public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{

        //县级、市级采集并结案操作，评价人员是他自己
        boolean result = false;
        if(super.isUserInfoCurrentUser(taskId, instanceId, userInfo)) {
            Map<String, Object> resultMap = null;
            OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
            UserBO user = new UserBO();

            user.setUserId(userInfo.getUserId());
            user.setPartyName(userInfo.getPartyName());

            //结案操作
            if(CLOSE_NODE_CODE.equals(nextNodeName)) {
                nextStaffId = String.valueOf(userInfo.getUserId());
                curOrgIds = String.valueOf(userInfo.getOrgId());

                resultMap = hessianFlowService.subWorkFlowForEvent(taskId, nextNodeName, nextStaffId, curOrgIds, advice, user, orgInfo, smsContent);
                result = resultMap != null;

                if(result){
                    Map<String,Object> curDataMap = eventDisposalWorkflowService.curNodeData(Long.valueOf(instanceId));
                    if(CommonFunctions.isNotBlank(curDataMap,"WF_DBID_")){
                        taskId = String.valueOf(curDataMap.get("WF_DBID_"));
                    }
                    //调用决策类，判断下一办理环节
                    extraParam.put("curNodeCode", nextNodeName);
                    extraParam.put("decisionService", NCHZHDD_DECISION_SERVICE);
                    nextNodeName = workflowDecisionMakingService.makeDecision(extraParam);

                    //交叉巡防事件结案成功后自动提交至评价或复核节点,评价复核办理人是事件发起人
                    if(StringUtils.isNotBlank(nextNodeName)){

                        ProInstance proInstance = baseWorkflowService.findProByInstanceId(Long.valueOf(instanceId));
                        if(null != proInstance && null != proInstance.getUserId()){
                            nextStaffId = String.valueOf(proInstance.getUserId());
                        }
                        if(null != proInstance && null != proInstance.getOrgId()){
                            curOrgIds = String.valueOf(proInstance.getOrgId());
                        }
                        

                        result = super.subWorkFlowForEndingAndEvaluate(instanceId,taskId,nextNodeName,nextStaffId,curOrgIds,advice,userInfo,smsContent,extraParam);
                    }else {
                        throw new Exception("结案成功，但是没有可用的下一环节！");
                    }
                }


			}else {
				
				//县区分派事件给乡镇街道或县区职能部门时，可设置处理时间
				if((TOWNDISPOSAl_NODE_CODE.equals(nextNodeName)||DISTRICT_DEPARTMENT_NODE_CODE.equals(nextNodeName))) {
					
					if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
						String curNodeName=extraParam.get("curNodeName").toString();
						if(DISTRICT_NODE_CODE.equals(curNodeName)) {
							extraParam.put("isSetInterval", "1");
						}
					}
				}
				
                result = super.subWorkFlowForEndingAndEvaluate(instanceId,taskId,nextNodeName,nextStaffId,curOrgIds,advice,userInfo,smsContent,extraParam);
            } 
            
            
        }

        if(result) {
        	sendMsg4Event(instanceId, null, nextStaffId, nextNodeName, userInfo);
        }
        
        return result;
    }
    
    @Override
	public boolean rejectWorkFlow(Map<String, Object> params, UserInfo userInfo) throws Exception {
		boolean result = false;
		
		result = super.rejectWorkFlow(params, userInfo);
		
		if(result) {
			Long eventId = -1L, instanceId = -1L;
			
			if(CommonFunctions.isNotBlank(params, "eventId")) {
				eventId = Long.valueOf(params.get("eventId").toString());
			}
			
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			}
			
			sendMsg4Event(instanceId, eventId, null, null, userInfo);
		}
		
		return result;
	}

    @Override
    public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {

        int result = -1;
        StringBuffer msgWrong = new StringBuffer("");

        if (null != orgSocialInfo) {

            String orgLevel = orgSocialInfo.getChiefLevel();

            if (StringUtils.isNotBlank(orgLevel)) {
                if(ConstantValue.MUNICIPAL_ORG_LEVEL.compareTo(orgLevel) > 0 || ConstantValue.DISTRICT_ORG_LEVEL.compareTo(orgLevel) < 0){
                    msgWrong.append("组织层级不可超过市级，不能低于县区级，请先检验！");
                }
            }else {
                msgWrong.append("当前组织层级不存在，请先检验！");
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int sqlOrder, UserInfo userInfo){

        List<Map<String, Object>> taskMapList = super.queryProInstanceDetail(instanceId, sqlOrder, userInfo);
        Map<String,Object> curDataMap = eventDisposalWorkflowService.curNodeData(Long.valueOf(instanceId));

        OrgSocialInfoBO orgInfo = null;
        Long orgId = -1L;
        Long userId = -1L;
        Long collectorOrgId = -1L;
        Long collectorId = -1L;
        //当前环节办理人id
        String curNodeTransactorId = "";
        //当前环节办理人组织id
        String curNodeTransactorOrgId = "";
        Long eventId = -1L;
        String  patrolType = "",//巡防类型
                orgLevel = "";//组织层级
        Boolean isCollector = false;

        if(null != userInfo){
            orgId = userInfo.getOrgId();
            userId = userInfo.getUserId();
            orgInfo = orgSocialInfoService.findByOrgId(orgId);
        }
        if(null != orgInfo){
            orgLevel = orgInfo.getChiefLevel();
        }
        if(null != instanceId){
            ProInstance proInstance = baseWorkflowService.findProByInstanceId(Long.valueOf(instanceId));
            if(null != proInstance && null != proInstance.getFormId()){
                eventId = proInstance.getFormId();
                //判断当前用户是否是事件采集人员，是的话不对采集环节做处理
                collectorOrgId = proInstance.getOrgId();
                collectorId = proInstance.getUserId();
                if(userId.equals(collectorId) && orgId.equals(collectorOrgId)){
                    isCollector = true;
                }
            }
            Map<String,Object> eventMap = eventExtendService.findEventExtendByEventId(eventId);
            if(CommonFunctions.isNotBlank(eventMap,"patrolType")){
                patrolType = String.valueOf(eventMap.get("patrolType"));
            }
        }
        if(null != curDataMap){
            if(CommonFunctions.isNotBlank(curDataMap,"WF_USERID")){
                curNodeTransactorId = (String.valueOf(curDataMap.get("WF_USERID")));
            }
            if(CommonFunctions.isNotBlank(curDataMap,"WF_ORGID")){
                curNodeTransactorOrgId = (String.valueOf(curDataMap.get("WF_ORGID")));
            }
        }
        //交叉巡防事件-当用户的组织层级为市级，不对事件环节信息作处理；低于市级，或者是非采集人 隐藏 采集、开始、复核、评价环节
        if(!isCollector && CROSSING_PATROL_TYPE.equals(patrolType) && ConstantValue.MUNICIPAL_ORG_LEVEL.compareTo(orgLevel) < 0){
            if(null != taskMapList && taskMapList.size() > 0){
                //办理人id
                String TRANSACTOR_ID = "";
                //办理人组织id
                String TRANSACTOR_ORG_ID = "";
                String[] curNodeTransactorIdArr = null;
                String[] curNodeTransactorOrgIdArr = null;

                if(StringUtils.isNotBlank(curNodeTransactorId)){
                    curNodeTransactorIdArr = curNodeTransactorId.split(",");
                }
                if(StringUtils.isNotBlank(curNodeTransactorOrgId)){
                    curNodeTransactorOrgIdArr = curNodeTransactorOrgId.split(",");
                }

                for(Map<String,Object> taskMap:taskMapList){
                    if(CommonFunctions.isNotBlank(taskMap,"TRANSACTOR_ID")){
                        TRANSACTOR_ID = String.valueOf(taskMap.get("TRANSACTOR_ID"));
                    }
                    if(CommonFunctions.isNotBlank(taskMap,"ORG_ID")){
                        TRANSACTOR_ORG_ID = String.valueOf(taskMap.get("ORG_ID"));
                    }
                    //隐藏当前环节
                    if(CommonFunctions.isNotBlank(taskMap,"HANDLE_PERSON")
                        && null != curNodeTransactorIdArr && Arrays.asList(curNodeTransactorIdArr).contains(String.valueOf(collectorId))
                        && null != curNodeTransactorOrgIdArr && Arrays.asList(curNodeTransactorOrgId).contains(String.valueOf(collectorOrgId))){

                        taskMap.put("HANDLE_PERSON","******(******);");

                        //隐藏子任务信息
                        if(CommonFunctions.isNotBlank(taskMap,"subAndReceivedTaskList")){
                            List<Map<String, Object>> subAndReceivedTaskList = new ArrayList<>() ;

                            try {
                                subAndReceivedTaskList = (List) taskMap.get("subAndReceivedTaskList");
                            } catch (ClassCastException e) {
                                e.printStackTrace();
                            }

                            if(null != subAndReceivedTaskList && subAndReceivedTaskList.size() > 0){
                                for(Map<String,Object> map:subAndReceivedTaskList){
                                    if(CommonFunctions.isNotBlank(map,"TRANSACTOR_NAME")){
                                        map.put("TRANSACTOR_NAME","******");
                                    }
                                    if(CommonFunctions.isNotBlank(map,"ORG_NAME")){
                                        map.put("ORG_NAME","******");
                                    }
                                }
                            }
                        }
                    }
                    //隐藏历史环节
                    if(String.valueOf(collectorId).equals(TRANSACTOR_ID) && String.valueOf(collectorOrgId).equals(TRANSACTOR_ORG_ID)){
                        taskMap.put("TRANSACTOR_NAME","******");
                        taskMap.put("ORG_NAME","******");

                        if(CommonFunctions.isNotBlank(taskMap,"SUB_HANDLE_PERSON")){
                            taskMap.put("SUB_HANDLE_PERSON","******(******)");
                        }

                        //隐藏子任务信息
                        if(CommonFunctions.isNotBlank(taskMap,"subAndReceivedTaskList")){
                            List<Map<String, Object>> subAndReceivedTaskList = new ArrayList<>() ;

                            try {
                                subAndReceivedTaskList = (List) taskMap.get("subAndReceivedTaskList");
                            } catch (ClassCastException e) {
                                e.printStackTrace();
                            }

                            if(null != subAndReceivedTaskList && subAndReceivedTaskList.size() > 0){
                                for(Map<String,Object> map:subAndReceivedTaskList){
                                    if(CommonFunctions.isNotBlank(map,"TRANSACTOR_NAME")){
                                        map.put("TRANSACTOR_NAME","******");
                                    }
                                    if(CommonFunctions.isNotBlank(map,"ORG_NAME")){
                                        map.put("ORG_NAME","******");
                                    }
                                }
                            }
                        }
                    }
                    //对环节的子任务和催办信息进行隐藏
                    //隐藏催办信息
                    if(CommonFunctions.isNotBlank(taskMap,"remindList")){
                        List<Map<String, Object>> remindList = new ArrayList<>() ;
                        try {
                            remindList = (List) taskMap.get("remindList");
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                        }

                        if(null != remindList && remindList.size() > 0){
                            for(Map<String,Object> map:remindList){
                                //隐藏催办人信息
                                if(CommonFunctions.isNotBlank(map,"REMIND_USER_NAME")){
                                    map.put("REMIND_USER_NAME","******");
                                }
                            }
                        }
                    }

                }
            }
        }
        
        return taskMapList;
    }
	
    /**
	 * 发送事件消息
	 * @param instanceId	实例id
	 * @param eventId		事件id
	 * @param nextUserIdStr	下一办理人员，多个值以英文逗号连接，为空时，使用当前办理人员
	 * @param userInfo		办理用户信息
	 */
	private void sendMsg4Event(Long instanceId, Long eventId, String nextUserIdStr, String nextTaskName, UserInfo userInfo) {
		if(instanceId != null && instanceId > 0 && (eventId == null || eventId < 0)) {
			ProInstance pro = this.capProInstanceByInstanceId(instanceId);
			
			if(pro != null) {
				eventId = pro.getFormId();
			}
		} else if(eventId != null && eventId > 0 && (instanceId == null || instanceId < 0)) {
			instanceId = this.capInstanceIdByEventIdForLong(eventId);
		}
		
		if(instanceId != null && instanceId > 0 && eventId != null && eventId > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("bizId", eventId);
	    	params.put("moduleCode", EVENT_MEG_MODULE_CODE);
	    	
			//将历史消息设置为已读
	    	messageService.batchReadMessage(params);
	    	
	    	if(StringUtils.isBlank(nextTaskName)) {
	    		Map<String, Object> curNodeDataMap = this.curNodeData(instanceId);
	    		
	    		if(CommonFunctions.isNotBlank(curNodeDataMap, "NODE_NAME")) {
	    			nextTaskName = curNodeDataMap.get("NODE_NAME").toString();
	    		}
	    	}
	    	
			if(TOWNDISPOSAl_NODE_CODE.equals(nextTaskName)
					|| DISTRICT_NODE_CODE.equals(nextTaskName) || DISTRICT_DEPARTMENT_NODE_CODE.equals(nextTaskName)
					|| MUNICIPAL_NODE_CODE.equals(nextTaskName) || MUNICIPAL_DEPARTMENT_NODE_CODE.equals(nextTaskName)) {
				EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
		    	
				if(event != null) {
					if(StringUtils.isBlank(nextUserIdStr)) {
						nextUserIdStr = this.curNodeUserIds(instanceId);
					}
					
					if(StringUtils.isNotBlank(nextUserIdStr)) {
						String[] nextUserIdArray = nextUserIdStr.split(",");
						List<Long> nextUserIdList = new ArrayList<Long>();
						ReceiverBO receiver = new ReceiverBO();
						String EVENT_VIEW_LINK = "$EVENT_DOMAIN/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=todo&eventId=",//事件消息查看详情链接
							   EVENT_MSG_ACTION = "发送了",//消息发送操作
							   viewLink = null, 
							   msgContent = "您有一条待办事件，事件编号为：@eventCode，请尽快处理";//消息内容
						
						viewLink = EVENT_VIEW_LINK + eventId + "&instanceId=" + instanceId;
						msgContent = msgContent.replaceAll("@eventCode", event.getCode());
						
						for(String nextUserId : nextUserIdArray) {
							nextUserIdList.add(Long.valueOf(nextUserId));
						}
						
						receiver.setUserIdList(nextUserIdList);
				    	
						messageService.sendCommonMessageA(userInfo.getUserId(), userInfo.getOrgId(), EVENT_MEG_MODULE_CODE, eventId, viewLink, msgContent, EVENT_MSG_ACTION, receiver);
					}
				}
			}
		} else {
			logger.error("未发送事件消息，流程实例id为[" + instanceId + "]， 事件id为[" + eventId + "]！");
		}
	}
	

}
