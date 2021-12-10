package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.common.message.bo.ReceiverBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.common.Constants;
import cn.ffcs.workflow.om.Forms;
import cn.ffcs.workflow.om.MyTask;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.OperateBean;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.om.TaskReceive;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.decisionMaking.EventStatusDecisionMaking4CQTLServiceImpl;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExpand.service.IEventDisposal4ExpandService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:重庆铜梁区事件个性化处理类
 * @Author: youwj
 * @Date: 2021/1/6 10:33
 */
@Service(value = "eventDisposalWorkflowForCQTLService")
public class EventDisposalWorkflowForCQTLServiceImpl extends
        EventDisposalWorkflowForEventNewServiceImpl{
	
	@Autowired
	private IEventDisposalService eventDisposalService;

	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;

	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	
	@Autowired
	private IEventDisposal4ExpandService eventDisposalExpandService;
	
	@Autowired
	private HessianFlowService hessianFlowService;
	
	@Autowired
	private IEventDisposalDockingService eventDisposalDockingService;

	@Autowired
	private MessageOutService messageService;
	
	@Resource(name = "eventStatusDecisionMaking4CQTLService")
	private EventStatusDecisionMaking4CQTLServiceImpl eventStatusDecisionMaking4CQTLService;
	
	
	private static final String DISTRICT_DEPT_HANDLE_NODE_CODE = "task6";	//群工系统处理
	private static final String INSPECTION_NODE_CODE = "task7";	//督导组抽查
	
	private static final String CLOSE_NODE_CODE = "task8";	//结案
	private static final String EVALUATE_NODE_CODE = "task9";	//评价
	
	//驳回节点编码
	protected static final String REJECT_NODE_CODE = "reject";	//虚拟的驳回环节节点，流程图中不存在
	//撤回节点编码
	private static final String RECALL_NODE_CODE = "recall";	//虚拟的撤回环节节点，流程图中不存在
	
	private static final String OPERATE_TYPE_REJECT = "2";//驳回操作
	private static final String OPERATE_TYPE_RECALL = "5";//撤回操作
	private static final String EVENT_MEG_MODULE_CODE = "02";	//事件消息所属模块编码
	
	@Override
    public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {

        

        String instanceId = super.startFlowByWorkFlow(eventId,workFlowId,  "0", userInfo, extraParam);

        //结案环节比较特殊，手动调用结案
        
        if(instanceId!=null&&Long.valueOf(instanceId)>0) {
        	
        	if("1".equals(toClose)) {//表明是结案操作
        		
        		//获取当前环节
        		Long curTaskId = eventDisposalWorkflowService.curNodeTaskId(Long.valueOf(instanceId));
        		
        		String advice="";
			    if(CommonFunctions.isNotBlank(extraParam, "advice")) {
			    	advice=String.valueOf(extraParam.get("advice"));
			    }
        		
        		this.subWorkFlowForEndingAndEvaluate(
        				Long.valueOf(instanceId),
        				curTaskId.toString(),
        				CLOSE_NODE_CODE,
        				userInfo.getUserId().toString(),
        				userInfo.getOrgId().toString(),
        				advice,
        				userInfo,
        				"",
        				extraParam);
				//如果是采集并结案操作，先清除一遍消息
				batchReadMessage(eventId);
        	}
			//发送消息
			String userIdStr = this.curNodeUserIds(Long.valueOf(instanceId));

			if(StringUtils.isNotBlank(userIdStr)) {
				String[] userIdArray = userIdStr.split(",");
				List<UserInfo> msgReceiveUserInfoList = new ArrayList<UserInfo>();
				UserInfo msgReceiveUserInfo = null;

				for(String userId : userIdArray) {
					msgReceiveUserInfo = new UserInfo();

					msgReceiveUserInfo.setUserId(Long.valueOf(userId));

					msgReceiveUserInfoList.add(msgReceiveUserInfo);
				}
				this.sendMessage(eventId, msgReceiveUserInfoList, userInfo);
			}
			//将办结期限调整为流程启动时间
			ProInstance proInstance = this.capProInstanceByInstanceId(Long.valueOf(instanceId));
			if(null != proInstance && null != proInstance.getStartTime()){
				EventDisposal event =  eventDisposalService.findEventByIdSimple(eventId);

				if(null != event){
					event.setHandleDate(DateUtils.addInterval(proInstance.getStartTime(),14,"00"));
					eventDisposalExpandService.updateEventDisposal(event,extraParam,userInfo);
				}
			}
        }
			    		
        
        return instanceId;
    }
	
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{
		boolean flag=false;
		
		String curCodeName="";
		Map<String,Object> curDataMap = eventDisposalWorkflowService.curNodeData(Long.valueOf(instanceId));
	    

	    if(CommonFunctions.isNotBlank(curDataMap,"NODE_NAME")){
	    	curCodeName = curDataMap.get("NODE_NAME").toString();
	    }
		
		//如果用户选择的下一环节是结案，那么需要调用工作流底层接口分步提交来动态指定评价人员
		if(CLOSE_NODE_CODE.equals(nextNodeName)) {
			//先执行结案操作
			List<UserInfo> nextUserInfoList=new ArrayList<UserInfo>();
			nextUserInfoList.add(userInfo);//结案人还是自己办理
			
			Map<String,Object> otherParams=new HashMap<String,Object>();
			otherParams.put("userType", "3");
			otherParams.put("currentTaskId", taskId);
			otherParams.put("advice", advice);
			
			flag = baseWorkflowService.subWorkflow4Base(instanceId, nextNodeName, nextUserInfoList, userInfo, otherParams);
			
			if(flag) {//如果结案成功那么在进行提交到评价环节的操作
				//先手工模拟工作流回调
				ProInstance pro = this.capProInstanceByInstanceId(instanceId);
				Map<String,Object> backParams=new HashMap<String,Object>();
				backParams.put("curNodeCode", curCodeName);
				backParams.put("nextNodeCode", nextNodeName);
				//Long eventId, Long userId, Long orgId, String chiefLevel, String curNodeCode, String nextNodeCode, Date handleDate, Map<String, Object> extraParam
				OrgSocialInfoBO findByOrgId = orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());
				eventStatusDecisionMaking4CQTLService.updateEventStatus(pro.getFormId(), userInfo.getUserId(), findByOrgId.getOrgId(),findByOrgId.getChiefLevel().toString(),curCodeName,nextNodeName,null, backParams);
				
				
				
				//获取当前环节
				Long closeTaskId = eventDisposalWorkflowService.curNodeTaskId(Long.valueOf(instanceId));
				
				//再手动提交到评价环节
				//获取事件的发起人信息
				Long reportEventUserId=pro.getUserId();
				Long reportEventOrgId=pro.getOrgId();
				
				OrgSocialInfoBO reportOrg = orgSocialInfoOutService.findByOrgId(reportEventOrgId);
				
				if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(reportOrg.getOrgType())
						&&ConstantValue.DISTRICT_ORG_LEVEL.equals(reportOrg.getChiefLevel())) {
					//如果事件的发起人处于区级单位，那么他不能找到上级，流转到自己手上进行评价办理
					//获取评价环节负责人
					List<UserInfo> evaUserInfoList=new ArrayList<UserInfo>();
					UserInfo evaUser=new UserInfo();
					evaUser.setUserId(reportEventUserId);
					evaUser.setOrgId(reportEventOrgId);
					evaUserInfoList.add(evaUser);
					
					Map<String,Object> evaParams=new HashMap<String,Object>();
					evaParams.put("userType", "3");
					evaParams.put("currentTaskId", closeTaskId);
					evaParams.put("advice", advice);
					
					flag = baseWorkflowService.subWorkflow4Base(instanceId, EVALUATE_NODE_CODE, evaUserInfoList, userInfo, evaParams);
				}else if(DISTRICT_DEPT_HANDLE_NODE_CODE.equals(curCodeName)) {
					//如果区职能部门结案，而且采集人是自己（即自采自结案）
					//那么无需找到评价人系统自动评价满意归档
					
					if(reportEventUserId.toString().equals(userInfo.getUserId().toString())
							&&reportEventOrgId.toString().equals(userInfo.getOrgId().toString())) {
						
						
						OrgSocialInfoBO parentOrg = orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());//结案人即为区职能部门的人
						
						OrgSocialInfoBO thisParentOrg=orgSocialInfoOutService.findByOrgId(parentOrg.getParentOrgId());
						
						List<UserInfo> evaUserInfoList=new ArrayList<UserInfo>();
						UserInfo evaUser=new UserInfo();
						evaUser.setUserId(99L);
						evaUser.setPartyName("系统");
						evaUser.setOrgId(parentOrg.getParentOrgId());
						evaUserInfoList.add(evaUser);
						
						Map<String,Object> evaParams=new HashMap<String,Object>();
						evaParams.put("userType", "3");
						evaParams.put("currentTaskId", closeTaskId);
						evaParams.put("advice", advice);
						
						flag = baseWorkflowService.subWorkflow4Base(instanceId, EVALUATE_NODE_CODE, evaUserInfoList, userInfo, evaParams);
						
						if(flag) {
							//顺利提交到评价环节，那么继续提交到下一环节
							Long curTaskId = eventDisposalWorkflowService.curNodeTaskId(Long.valueOf(instanceId));
							Map<String,Object> curEvaParams=new HashMap<String,Object>();
							curEvaParams.put("taskId", curTaskId);
							curEvaParams.put("evaLevel", "04");
							curEvaParams.put("isEvaluate", true);
							curEvaParams.put("evaContent", "区职能部门自采自结，系统默认满意归档。");
							curEvaParams.put("createorName", thisParentOrg.getOrgName()+"-系统");
							curEvaParams.put("advice", "区职能部门自采自结，系统默认满意归档。");
							
							flag=this.subWorkFlowForEndingAndEvaluate(instanceId, String.valueOf(curTaskId), INSPECTION_NODE_CODE, "", "", "区职能部门自采自结，系统默认满意归档。", evaUser, "", curEvaParams);
							return flag;

						}
					}else {
						
						//区职能部门结案，那么需要转到区指挥中心评价
						//找到上级组织
						OrgSocialInfoBO parentOrg = orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());//结案人即为区职能部门的人
						
						List<UserInfo> evaUserInfoList=new ArrayList<UserInfo>();
						UserInfo evaUser=new UserInfo();
						evaUser.setUserId(parentOrg.getParentOrgId());
						evaUser.setOrgId(parentOrg.getParentOrgId());
						evaUserInfoList.add(evaUser);
						
						Map<String,Object> evaParams=new HashMap<String,Object>();
						evaParams.put("userType", "1");
						evaParams.put("currentTaskId", closeTaskId);
						evaParams.put("advice", advice);
						
						flag = baseWorkflowService.subWorkflow4Base(instanceId, EVALUATE_NODE_CODE, evaUserInfoList, userInfo, evaParams);
					}

					
				}else if(reportEventUserId.toString().equals(userInfo.getUserId().toString())
						&&reportEventOrgId.toString().equals(userInfo.getOrgId().toString())) {
					//如果结案人和事件发起人相同，那么需要转到上级组织评价
					//找到上级组织
					OrgSocialInfoBO parentOrg = orgSocialInfoOutService.findByOrgId(reportEventOrgId);
					
					List<UserInfo> evaUserInfoList=new ArrayList<UserInfo>();
					UserInfo evaUser=new UserInfo();
					evaUser.setUserId(parentOrg.getParentOrgId());
					evaUser.setOrgId(parentOrg.getParentOrgId());
					evaUserInfoList.add(evaUser);
					
					Map<String,Object> evaParams=new HashMap<String,Object>();
					evaParams.put("userType", "1");
					evaParams.put("currentTaskId", closeTaskId);
					evaParams.put("advice", advice);
					
					flag = baseWorkflowService.subWorkflow4Base(instanceId, EVALUATE_NODE_CODE, evaUserInfoList, userInfo, evaParams);
					
				}else{
					//如果结案人和事件发起人不相同，那么需要转到采集人评价
					List<UserInfo> evaUserInfoList=new ArrayList<UserInfo>();
					UserInfo evaUser=new UserInfo();
					evaUser.setUserId(reportEventUserId);
					evaUser.setOrgId(reportEventOrgId);
					evaUserInfoList.add(evaUser);
					
					Map<String,Object> evaParams=new HashMap<String,Object>();
					evaParams.put("userType", "3");
					evaParams.put("currentTaskId", closeTaskId);
					evaParams.put("advice", advice);
					
					flag = baseWorkflowService.subWorkflow4Base(instanceId, EVALUATE_NODE_CODE, evaUserInfoList, userInfo, evaParams);
					
				}
				
				
				if(flag) {
					Map<String,Object> thisBackParam=new HashMap<String,Object>();
					thisBackParam.put("curNodeCode", CLOSE_NODE_CODE);
					thisBackParam.put("nextNodeCode", EVALUATE_NODE_CODE);
					//Long eventId, Long userId, Long orgId, String chiefLevel, String curNodeCode, String nextNodeCode, Date handleDate, Map<String, Object> extraParam
					eventStatusDecisionMaking4CQTLService.updateEventStatus(pro.getFormId(), userInfo.getUserId(), findByOrgId.getOrgId(),findByOrgId.getChiefLevel().toString(),CLOSE_NODE_CODE,EVALUATE_NODE_CODE,null, thisBackParam);
				}
				
			}
			
		}else if(EVALUATE_NODE_CODE.equals(nextNodeName)) {
			//下一环节是评价需要动态获取评价人员
			ProInstance pro = this.capProInstanceByInstanceId(instanceId);
			OrgSocialInfoBO findByOrgId = orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());
			//获取当前环节
			Long closeTaskId = eventDisposalWorkflowService.curNodeTaskId(Long.valueOf(instanceId));
			
			//再手动提交到评价环节
			//获取事件的发起人信息
			Long reportEventUserId=pro.getUserId();
			Long reportEventOrgId=pro.getOrgId();
			
			OrgSocialInfoBO reportOrg = orgSocialInfoOutService.findByOrgId(reportEventOrgId);
			
			if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(reportOrg.getOrgType())
					&&ConstantValue.DISTRICT_ORG_LEVEL.equals(reportOrg.getChiefLevel())) {
				//如果事件的发起人处于区级单位，那么他不能找到上级，流转到自己手上进行评价办理
				//获取评价环节负责人
				List<UserInfo> evaUserInfoList=new ArrayList<UserInfo>();
				UserInfo evaUser=new UserInfo();
				evaUser.setUserId(reportEventUserId);
				evaUser.setOrgId(reportEventOrgId);
				evaUserInfoList.add(evaUser);
				
				Map<String,Object> evaParams=new HashMap<String,Object>();
				evaParams.put("userType", "3");
				evaParams.put("currentTaskId", closeTaskId);
				evaParams.put("advice", advice);
				
				flag = baseWorkflowService.subWorkflow4Base(instanceId, EVALUATE_NODE_CODE, evaUserInfoList, userInfo, evaParams);
			}else if(DISTRICT_DEPT_HANDLE_NODE_CODE.equals(curCodeName)) {
				//如果区职能部门结案，而且采集人是自己（即自采自结案）
				//那么无需找到评价人系统自动评价满意归档
				
				if(reportEventUserId.toString().equals(userInfo.getUserId().toString())
						&&reportEventOrgId.toString().equals(userInfo.getOrgId().toString())) {
					
					
					OrgSocialInfoBO parentOrg = orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());//结案人即为区职能部门的人
					
					OrgSocialInfoBO thisParentOrg=orgSocialInfoOutService.findByOrgId(parentOrg.getParentOrgId());
					
					List<UserInfo> evaUserInfoList=new ArrayList<UserInfo>();
					UserInfo evaUser=new UserInfo();
					evaUser.setUserId(99L);
					evaUser.setPartyName("系统");
					evaUser.setOrgId(parentOrg.getParentOrgId());
					evaUserInfoList.add(evaUser);
					
					Map<String,Object> evaParams=new HashMap<String,Object>();
					evaParams.put("userType", "3");
					evaParams.put("currentTaskId", closeTaskId);
					evaParams.put("advice", advice);
					
					flag = baseWorkflowService.subWorkflow4Base(instanceId, EVALUATE_NODE_CODE, evaUserInfoList, userInfo, evaParams);
					
					if(flag) {
						//顺利提交到评价环节，那么继续提交到下一环节
						Long curTaskId = eventDisposalWorkflowService.curNodeTaskId(Long.valueOf(instanceId));
						Map<String,Object> curEvaParams=new HashMap<String,Object>();
						curEvaParams.put("taskId", curTaskId);
						curEvaParams.put("evaLevel", "04");
						curEvaParams.put("isEvaluate", true);
						curEvaParams.put("evaContent", "区职能部门自采自结，系统默认满意归档。");
						curEvaParams.put("createorName", thisParentOrg.getOrgName()+"-系统");
						curEvaParams.put("advice", "区职能部门自采自结，系统默认满意归档。");
						
						flag=this.subWorkFlowForEndingAndEvaluate(instanceId, String.valueOf(curTaskId), INSPECTION_NODE_CODE, "", "", "区职能部门自采自结，系统默认满意归档。", evaUser, "", curEvaParams);
						return flag;
					}
				}else {
					
					//区职能部门结案，那么需要转到区指挥中心评价
					//找到上级组织
					OrgSocialInfoBO parentOrg = orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());//结案人即为区职能部门的人
					
					List<UserInfo> evaUserInfoList=new ArrayList<UserInfo>();
					UserInfo evaUser=new UserInfo();
					evaUser.setUserId(parentOrg.getParentOrgId());
					evaUser.setOrgId(parentOrg.getParentOrgId());
					evaUserInfoList.add(evaUser);
					
					Map<String,Object> evaParams=new HashMap<String,Object>();
					evaParams.put("userType", "1");
					evaParams.put("currentTaskId", closeTaskId);
					evaParams.put("advice", advice);
					
					flag = baseWorkflowService.subWorkflow4Base(instanceId, EVALUATE_NODE_CODE, evaUserInfoList, userInfo, evaParams);
				}
				
			}else if(reportEventUserId.toString().equals(userInfo.getUserId().toString())
					&&reportEventOrgId.toString().equals(userInfo.getOrgId().toString())) {
				//如果结案人和事件发起人相同，那么需要转到上级组织评价
				//找到上级组织
				OrgSocialInfoBO parentOrg = orgSocialInfoOutService.findByOrgId(reportEventOrgId);
				
				List<UserInfo> evaUserInfoList=new ArrayList<UserInfo>();
				UserInfo evaUser=new UserInfo();
				evaUser.setUserId(parentOrg.getParentOrgId());
				evaUser.setOrgId(parentOrg.getParentOrgId());
				evaUserInfoList.add(evaUser);
				
				Map<String,Object> evaParams=new HashMap<String,Object>();
				evaParams.put("userType", "1");
				evaParams.put("currentTaskId", closeTaskId);
				evaParams.put("advice", advice);
				
				flag = baseWorkflowService.subWorkflow4Base(instanceId, EVALUATE_NODE_CODE, evaUserInfoList, userInfo, evaParams);
				
			}else{
				//如果结案人和事件发起人不相同，那么需要转到采集人评价
				List<UserInfo> evaUserInfoList=new ArrayList<UserInfo>();
				UserInfo evaUser=new UserInfo();
				evaUser.setUserId(reportEventUserId);
				evaUser.setOrgId(reportEventOrgId);
				evaUserInfoList.add(evaUser);
				
				Map<String,Object> evaParams=new HashMap<String,Object>();
				evaParams.put("userType", "3");
				evaParams.put("currentTaskId", closeTaskId);
				evaParams.put("advice", advice);
				
				flag = baseWorkflowService.subWorkflow4Base(instanceId, EVALUATE_NODE_CODE, evaUserInfoList, userInfo, evaParams);
				
			}
			
			
			if(flag) {
				Map<String,Object> thisBackParam=new HashMap<String,Object>();
				thisBackParam.put("curNodeCode", CLOSE_NODE_CODE);
				thisBackParam.put("nextNodeCode", EVALUATE_NODE_CODE);
				//Long eventId, Long userId, Long orgId, String chiefLevel, String curNodeCode, String nextNodeCode, Date handleDate, Map<String, Object> extraParam
				eventStatusDecisionMaking4CQTLService.updateEventStatus(pro.getFormId(), userInfo.getUserId(), findByOrgId.getOrgId(),findByOrgId.getChiefLevel().toString(),CLOSE_NODE_CODE,EVALUATE_NODE_CODE,null, thisBackParam);
			}
			
			
		}else if(INSPECTION_NODE_CODE.equals(nextNodeName)) {
			//如果下一环节是督导组抽查，则需要动态将环节推送至空用户手中，同时保存评价信息
			
			//保存评价信息
			//默认不增加评价信息，当传入参数isEvaluate并且为true时，增加评价信息
			String evaLevel = "";
			String evaContent = "";
			ProInstance pro = this.capProInstanceByInstanceId(instanceId);
			Long eventId = pro.getFormId();
			if((eventId ==null || eventId < 0) && CommonFunctions.isNotBlank(extraParam,"eventId")){
				eventId = Long.valueOf(String.valueOf(extraParam.get("eventId")));
			}
			if(CommonFunctions.isNotBlank(extraParam,"evaLevel")){
				evaLevel = String.valueOf(extraParam.get("evaLevel"));
			}
			if(CommonFunctions.isNotBlank(extraParam,"evaContent")){
				evaContent = String.valueOf(extraParam.get("evaContent"));
			}
			eventDisposalExpandService.saveOrUpdateEventEvaluate(userInfo,eventId,evaLevel,evaContent,extraParam);
			
			//随后推送环节
			//构造下一环节办理人(默认推送到用户id为99，组织id为99的用户下)
			flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, "99", "99", advice, userInfo, smsContent, extraParam);
			if(flag) {
				try {
					//推送成功就表示事件已经归档了
					//因此手动改事件状态为04
					EventDisposal thisEvent=new EventDisposal();
					thisEvent.setEventId(eventId);
					thisEvent.setStatus("04");
					eventDisposalService.updateEventDisposalById(thisEvent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}else {
			flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		}

		if(flag){
			Long eventId = -1L;

			if(CommonFunctions.isNotBlank(extraParam, "formId")) {
				try {
					eventId = Long.valueOf(extraParam.get("formId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}

			if(eventId < 0) {
				ProInstance pro = this.capProInstanceByInstanceId(instanceId);
				if(pro != null) {
					eventId = pro.getFormId();
				}
			}

			//当前环节为处理中时，不做消息提醒
			if(!ConstantValue.HANDLING_TASK_CODE.equals(nextNodeName)){
				batchReadMessage(eventId);

				if(StringUtils.isBlank(nextStaffId)) {
					EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);

					if(event != null && ConstantValue.EVENT_STATUS_ARCHIVE.equals(event.getStatus())) {
						nextStaffId = this.curNodeUserIds(instanceId);
					}
				}
				if(StringUtils.isNotBlank(nextStaffId)) {
					String[] userIdArray = nextStaffId.split(",");
					List<UserInfo> msgReceiveUserInfoList = new ArrayList<UserInfo>();
					UserInfo msgReceiveUserInfo = null;

					for(String userId : userIdArray) {
						msgReceiveUserInfo = new UserInfo();

						msgReceiveUserInfo.setUserId(Long.valueOf(userId));

						msgReceiveUserInfoList.add(msgReceiveUserInfo);
					}

					this.sendMessage(eventId, msgReceiveUserInfoList, userInfo);
				}
			}
		}
		return flag;
	}
	
	
	@Override
	public boolean rejectWorkFlow(Map<String, Object> params, UserInfo userInfo) throws Exception {
		boolean result = false;
		Long instanceId = Long.valueOf(params.get("instanceId").toString());
		
		//获取当前环节curTaskId
		Map<String,Object> curDataMap = eventDisposalWorkflowService.curNodeData(instanceId);
	    
	    String curCodeName = "";

	    if(CommonFunctions.isNotBlank(curDataMap,"NODE_NAME")){
	    	curCodeName = curDataMap.get("NODE_NAME").toString();
	    }
	    
		if(INSPECTION_NODE_CODE.equals(curCodeName)) {
			
			List<Map<String, Object>> taskMapList = baseWorkflowService.capHandledTaskInfoMap(instanceId, Constants.SQL_ORDER_DESC, null);
		    String preTaskName="";//回退1个环节
		    String preTaskName_2="";//回退两个环节
		    String preTaskName_3="";//回退三个环节
		    Integer flag=0;
		    if(taskMapList!=null&&taskMapList.size()>0) {
		    	for (Map<String, Object> taskmap : taskMapList) {
		    		String operateType="";
		    		if(CommonFunctions.isNotBlank(taskmap, "OPERATE_TYPE")) {
		    			operateType=taskmap.get("OPERATE_TYPE").toString();
		    		}
					if((!OPERATE_TYPE_REJECT.equals(operateType)) && ( !OPERATE_TYPE_RECALL.equals(operateType))) {
						if(flag>0) {
							flag+=-1;
						}else {
							
							if(StringUtils.isBlank(preTaskName)) {
								preTaskName=taskmap.get("TASK_CODE").toString();
								continue;	
							}
							if(StringUtils.isBlank(preTaskName_2)) {
								preTaskName_2=taskmap.get("TASK_CODE").toString();
								continue;	
							}
							if(StringUtils.isBlank(preTaskName_3)) {
								preTaskName_3=taskmap.get("TASK_CODE").toString();
								continue;	
							}
						}
					}else {
						flag+=1;
					}
				}
		    }
			
			
			//如果当前环节是督导组抽查
			//则需要驳回到结案人手上进行重新办理
			Map<String,Object> rejectParams=new HashMap<String,Object>();
			rejectParams.put("eventId", params.get("eventId"));
			rejectParams.put("advice", "督导组检查驳回，请重新办理。");
			rejectParams.put("instanceId", Long.valueOf(params.get("instanceId").toString()));
			rejectParams.put("userType", "3");
			rejectParams.put("isCheckCurrentUser", "false");
			
			//当前环节的前一个环节是评价--preTaskName
			//再前一个环节是结案--preTaskName_2
			//再前一个环节是交付结案的环节，因此要驳回到交付结案环节的人手上--preTaskName_3
			
			result = baseWorkflowService.rejectWorkflow4Base("重庆铜梁区事件流程", "fa", Long.valueOf(params.get("eventId").toString()), preTaskName_3, userInfo, rejectParams);
			if(result) {
				//督导组驳回的事件统一设置为已分流
				
				EventDisposal updateEvent =new EventDisposal();
				updateEvent.setEventId(Long.valueOf(params.get("eventId").toString()));
				updateEvent.setStatus(ConstantValue.EVENT_STATUS_DISTRIBUTE);
				eventDisposalService.updateEventDisposalById(updateEvent);
			
			}
		}else {
			result=super.rejectWorkFlow(params, userInfo);
		}

		//读取消息、发送消息
		if(result){
			Map<String, Object> curTaskData = null;
			List<UserInfo> curUserInfoList = null;
			ProInstance proInstance = this.capProInstanceByInstanceId(instanceId);
			Long eventId = null;

			if(proInstance != null) {
				eventId = proInstance.getFormId();
			}

			if(eventId != null && eventId > 0) {
				batchReadMessage(eventId);

				curTaskData = this.curNodeData(instanceId);

				if(CommonFunctions.isNotBlank(curTaskData, "WF_USERID")) {
					String[] curUserIdArray = curTaskData.get("WF_USERID").toString().split(",");
					curUserInfoList = new ArrayList<UserInfo>();
					UserInfo curUserInfo = null;
					Long curUserId = null;

					for(String curUserIdStr : curUserIdArray) {
						try {
							curUserId = Long.valueOf(curUserIdStr);
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}

						if(curUserId != null && curUserId > 0) {
							curUserInfo = new UserInfo();

							curUserInfo.setUserId(curUserId);

							curUserInfoList.add(curUserInfo);
						}
					}
				}
				sendMessage(eventId, curUserInfoList, userInfo);
			}
			//如果当前环节是评价环节，发生驳回操作后,事件办结期限增加7自然日
			if(EVALUATE_NODE_CODE.equals(curCodeName)){
				EventDisposal event =  eventDisposalService.findEventByIdSimple(eventId);
				if(null != event && null != event.getHandleDate()){
					event.setHandleDate(DateUtils.addInterval(new Date(), 7, "00"));
					eventDisposalExpandService.updateEventDisposal(event,params,userInfo);
				}
			}

		}
		
		return result;
	}
	
	
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		Long workFlowId = this.capWorkflowId(instanceId, null, userInfo, null);
		
		if(params == null) {
			params = new HashMap<String, Object>();
		}
		
		params.put("curOrgId", userInfo.getOrgId());//当前用户的组织
		params.put("curUserId", userInfo.getUserId());//当前用户的编码
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> initResultMap = hessianFlowService.initFlowInfoForEvent(taskId, instanceId, workFlowId, params);
		
		if(initResultMap != null && !initResultMap.isEmpty()) {
			taskId = (String) initResultMap.get("taskId");
			List<OperateBean> operateLists = (List<OperateBean>) initResultMap.get("operateLists");
			List<Node> taskNodes = null;
			//工作流发布实例
			Integer deploymentId = (Integer) initResultMap.get("deploymentId");
			Node curNode = (Node) initResultMap.get("curNode");
			String workflowCtx = hessianFlowService.getWorkflowCtx();
			//workflowCtx = "http://192.168.22.25:8080/workflow";
			//获取流程实例
			ProInstance proInstance = (ProInstance) initResultMap.get("proInstance");
			Forms curForm = (Forms) initResultMap.get("curForm");
			// 是否跨域
			String formUrl = (String) initResultMap.get("formUrl");
			//任务描述
			String taskDesc = (String) initResultMap.get("taskDesc");
			// 上一个节点
			MyTask backTask = (MyTask) initResultMap.get("backTask");
			
			if(CommonFunctions.isNotBlank(initResultMap, "taskNodes")) {
				
				taskNodes = (List<Node>) initResultMap.get("taskNodes");
				
				for (Node node : taskNodes) {
					if(node.getNodeName().endsWith(INSPECTION_NODE_CODE)) {
						//督导组抽查在页面上要显示为归档
						node.setNodeNameZH("归档");
					}
				}
				
			}
			
			resultMap.put("taskId", taskId);
			resultMap.put("operateLists", operateLists);
			resultMap.put("instanceId", instanceId);
			resultMap.put("taskNodes", taskNodes);
			resultMap.put("deploymentId", deploymentId);
			resultMap.put("curNodeId", curNode.getNodeId());
			resultMap.put("curNodeName", curNode.getNodeName());//当前办理环节名称
			resultMap.put("curNodeFormTypeId", curNode.getFormTypeId());
			resultMap.put("curNode", curNode);
			resultMap.put("workflowCtx", workflowCtx);//工作流域名访问前缀
			resultMap.put("workFlowId", workFlowId);
			resultMap.put("backTask", backTask);
			
			resultMap.put("proInstance", proInstance);//获取事件发起人
			resultMap.put("curForm", curForm);
			resultMap.put("formUrl", formUrl+"&isCrossdomain=0");
			resultMap.put("taskDesc", taskDesc);
		}
		
		return resultMap;
	}
	

	@Override
	public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {
		int result = -1;
		StringBuffer msgWrong = new StringBuffer("");
		String orgType="";
		String orgLevel="";
		
		if(orgSocialInfo != null) {
			orgLevel = orgSocialInfo.getChiefLevel();
			orgType = orgSocialInfo.getOrgType();
			
			if(StringUtils.isBlank(orgLevel)) {
				msgWrong.append("当前组织层级不存在，请先检验！");
			}
			
		} else {
			msgWrong.append("当前组织不存在，请先检验！");
		}
		
		//目前只有网格员，村社区、乡镇街道、区级的单位可以采集事件
		Map<String,Object> isUserAbleToStartMap=new HashMap<String,Object>();
		//key值为：组织层级_组织类型
		isUserAbleToStartMap.put("6_1", true);
		isUserAbleToStartMap.put("5_1", true);
		isUserAbleToStartMap.put("4_1", true);
		isUserAbleToStartMap.put("3_1", true);
		isUserAbleToStartMap.put("3_0", true);
		
		if(CommonFunctions.isBlank(isUserAbleToStartMap, orgLevel+"_"+orgType)) {
			msgWrong.append("当前用户无法启动流程。能启动流程的用户必须为网格层级、村社区层级，乡镇街道层级以及区级的单位人员,或是区级的部门人员。");
		}
		
		if(msgWrong.length() > 0) {
			throw new ZhsqEventException(msgWrong.toString());
		} else {
			result = 1;
		}
		
		return result;
	}
	
	
	@Override
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int sqlOrder, UserInfo userInfo){
		List<Map<String, Object>> workflowList = this.queryProInstanceDetail(instanceId, sqlOrder),
								  taskList = new ArrayList<Map<String, Object>>(),
								  subTaskList = null,
								  subAndReceivedTaskList = null,
								  subAndReceivedTaskListTmp = null,
								  timeAndRemarkList = null;
		
		/**
		 * PARENT_TASK_ID
		 * TASK_ID，
		 * TASK_TYPE，
		 * TASK_NAME，
		 * OPERATE_TYPE，
		 * TRANSACTOR_ID，
		 * TRANSACTOR_NAME，
		 * ORG_NAME，ORG_ID，
		 * REMARKS，START_TIME，END_TIME
		 */
		
		if(workflowList != null) {//新增子任务（会签任务和处理中任务）
			Long taskId = -1L;
			List<TaskReceive> taskReceivedList = null;
			
			for(Map<String, Object> taskMap : workflowList) {
				
				if(CommonFunctions.isNotBlank(taskMap, "TASK_CODE")
						&&taskMap.get("TASK_CODE").toString().equals(INSPECTION_NODE_CODE)
						&&CommonFunctions.isNotBlank(taskMap, "IS_CURRENT_TASK")
						&&Boolean.valueOf(taskMap.get("IS_CURRENT_TASK").toString())) {
					//单前环节是处理督导组处理，则表明该事件已经归档，此环节无需展示，留着备用
					continue;
				}
				
		
				
				if(CommonFunctions.isNotBlank(taskMap, "TASK_ID")) {
					int subCount = 0;
					boolean isCurrentTask = false;
					
					if(CommonFunctions.isNotBlank(taskMap, "SUB_COUNT")) {
						try {
							subCount = Integer.valueOf(taskMap.get("SUB_COUNT").toString());
						} catch(NumberFormatException e) {
							subCount = 0;
						}
					}
					
					if(CommonFunctions.isNotBlank(taskMap, "IS_CURRENT_TASK")) {//判断是否是当前任务
						isCurrentTask = Boolean.valueOf(taskMap.get("IS_CURRENT_TASK").toString());
					}
					
					if(subCount > 0) {//构建会签环节的办理人员信息
						if(CommonFunctions.isNotBlank(taskMap, "TRANSACTOR_NAME") && CommonFunctions.isNotBlank(taskMap, "ORG_NAME")) {
							String[] transactorNames = taskMap.get("TRANSACTOR_NAME").toString().split(",");
							String[] orgNames = taskMap.get("ORG_NAME").toString().split(",");
							StringBuffer subTaskPerson = new StringBuffer("");
							
							for(int index = 0, len = transactorNames.length; index < len; index++) {
								subTaskPerson.append(transactorNames[index]).append("(").append(orgNames[index]).append(");");
							}
							
							taskMap.put("SUB_HANDLE_PERSON", subTaskPerson);
						}
					}
					
					subAndReceivedTaskList = new ArrayList<Map<String, Object>>();
					//获取任务接收时间列表
					taskId  = Long.valueOf(taskMap.get("TASK_ID").toString());
					taskReceivedList = this.findTaskReceivedList(taskId, instanceId, null);
					
					if(taskReceivedList != null && taskReceivedList.size() > 0) {
						Map<String, Object> taskReceiveMap = null;
						for(TaskReceive taskReceive : taskReceivedList) {
							taskReceiveMap = new HashMap<String, Object>();
							taskReceiveMap.put("TASK_ID", taskReceive.getTaskId());
							taskReceiveMap.put("TRANSACTOR_ID", taskReceive.getUserId());
							taskReceiveMap.put("TRANSACTOR_NAME", taskReceive.getUserName());
							taskReceiveMap.put("ORG_ID", taskReceive.getOrgId());
							taskReceiveMap.put("ORG_NAME", taskReceive.getOrgName());
							try {
								taskReceiveMap.put("RECEIVE_TIME", DateUtils.convertDateToString(taskReceive.getReceiveTime()));
							} catch (ParseException e) {
								e.printStackTrace();
							}
							
							subAndReceivedTaskList.add(taskReceiveMap);
						}
					}
					
					if(subCount > 0 || isCurrentTask) {//子任务不为空或者是当前任务时，才查找子任务
						subTaskList = this.querySubTaskDetails(taskMap.get("TASK_ID").toString(), sqlOrder, userInfo);
						
						if(subTaskList != null && subTaskList.size() > 0) {
							for(Map<String, Object> subTask : subTaskList) {
								if(CommonFunctions.isNotBlank(subTask, "TASK_NAME")) {//清除处理中的环节名称
									if(ConstantValue.HANDLING_TASK_CODE.equals(subTask.get("TASK_NAME"))) {
										subTask.remove("TASK_NAME");
									}
								}
								
								subAndReceivedTaskList.add(subTask);
							}
							
							taskMap.put("subTaskList", subTaskList);
						}
					}
					
					if(subAndReceivedTaskList.size() > 0) {
						subAndReceivedTaskListTmp = new ArrayList<Map<String, Object>>();
						Map<String, Object> timeAndRemarkMap = null;
						boolean isSubAndReceivedBlank = true;
						
						for(Map<String, Object> subAndReceivedTaskMap : subAndReceivedTaskList) {
							timeAndRemarkList = new ArrayList<Map<String, Object>>();
							timeAndRemarkMap = new HashMap<String, Object>();
							isSubAndReceivedBlank = true;
							
							if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "START_TIME")) {
								timeAndRemarkMap.put("START_TIME", subAndReceivedTaskMap.get("START_TIME"));
							}
							if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "END_TIME")) {
								timeAndRemarkMap.put("END_TIME", subAndReceivedTaskMap.get("END_TIME"));
							}
							if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "RECEIVE_TIME")) {
								timeAndRemarkMap.put("RECEIVE_TIME", subAndReceivedTaskMap.get("RECEIVE_TIME"));
							}
							if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "REMARKS")) {
								timeAndRemarkMap.put("REMARKS", subAndReceivedTaskMap.get("REMARKS"));
							}
							
							for(Map<String, Object> subAndReceivedTaskTmpMap : subAndReceivedTaskListTmp) {
								//合并准则：1、存在TRANSACTOR_ID和ORG_ID，且二者均不为空时，二者同时相等者进行合并；
								if(CommonFunctions.isNotBlank(subAndReceivedTaskMap, "TRANSACTOR_ID") && CommonFunctions.isNotBlank(subAndReceivedTaskTmpMap, "TRANSACTOR_ID") && 
										subAndReceivedTaskMap.get("TRANSACTOR_ID").equals(subAndReceivedTaskTmpMap.get("TRANSACTOR_ID")) && 
										CommonFunctions.isNotBlank(subAndReceivedTaskMap, "ORG_ID") && CommonFunctions.isNotBlank(subAndReceivedTaskTmpMap, "ORG_ID") &&
										subAndReceivedTaskMap.get("ORG_ID").equals(subAndReceivedTaskTmpMap.get("ORG_ID"))) {
									if(CommonFunctions.isNotBlank(subAndReceivedTaskTmpMap, "timeAndRemarkList")) {
										timeAndRemarkList.addAll((List<Map<String, Object>>)subAndReceivedTaskTmpMap.get("timeAndRemarkList"));
									}
									
									if(!timeAndRemarkMap.isEmpty()) {
										timeAndRemarkList.add(timeAndRemarkMap);
										
										subAndReceivedTaskTmpMap.put("timeAndRemarkList", timeAndRemarkList);
									}
									
									isSubAndReceivedBlank = false;
									
									break;
								}
							}
							
							if(isSubAndReceivedBlank) {
								if(!timeAndRemarkMap.isEmpty()) {
									timeAndRemarkList.add(timeAndRemarkMap);
									
									subAndReceivedTaskMap.put("timeAndRemarkList", timeAndRemarkList);
								}
								
								subAndReceivedTaskListTmp.add(subAndReceivedTaskMap);
							}
						}
						
						taskMap.put("subAndReceivedTaskList", subAndReceivedTaskListTmp);
					}
				}
				
				taskList.add(taskMap);
			}
		}
		
		return taskList;
	}

	/**
	 * 消息发送
	 * 1、给消息中心发送
	 * 2、发送给手机短信
	 *
	 * @param eventId					事件id
	 * @param msgReceiveUserInfoList	消息接收人员
	 * @param userInfo					操作用户信息
	 */
	private void sendMessage(Long eventId, List<UserInfo> msgReceiveUserInfoList, UserInfo userInfo) {
		EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);

		if(event != null) {
			String EVENT_VIEW_LINK = "$EVENT_DOMAIN/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=todo&eventId=", //消息查看详情链接
					EVENT_MSG_ACTION = "发送了", // 消息发送操作
					viewLink = null, msgContent = "您有一条待办事件，单号为：@code，请尽快处理",//消息内容
					eventStatus = event.getStatus(),
					code = event.getCode();

			if (msgReceiveUserInfoList != null && msgReceiveUserInfoList.size() > 0) {
				// 事件状态 为 受理(00，驳回操作时)、上报(01)、下派(02)、结案待评(03)时，进行消息提醒
				if (ConstantValue.EVENT_STATUS_RECEIVED.equals(eventStatus)
						|| ConstantValue.EVENT_STATUS_REPORT.equals(eventStatus)
						|| ConstantValue.EVENT_STATUS_DISTRIBUTE.equals(eventStatus)
						|| ConstantValue.EVENT_STATUS_ARCHIVE.equals(eventStatus)) {
					List<Long> msgReceiveUserIdList = new ArrayList<Long>(),
							msgReceiveOrgIdList = new ArrayList<Long>();
					Long receiveUserId = null, receiveOrgId = null;

					viewLink = EVENT_VIEW_LINK + eventId;

					msgContent = msgContent.replaceAll("@code",code);

					for(UserInfo nextUser : msgReceiveUserInfoList) {
						receiveUserId = nextUser.getUserId();
						receiveOrgId = nextUser.getOrgId();

						if(receiveUserId != null && receiveUserId > 0) {
							msgReceiveUserIdList.add(receiveUserId);
						}
						if(receiveOrgId != null && receiveOrgId > 0) {
							msgReceiveOrgIdList.add(receiveOrgId);
						}
					}

					if(msgReceiveUserIdList.size() > 0 || msgReceiveOrgIdList.size() > 0) {
						ReceiverBO receiver = new ReceiverBO();

						receiver.setUserIdList(msgReceiveUserIdList);
						receiver.setOrgIdList(msgReceiveOrgIdList);

						messageService.sendCommonMessageA(userInfo.getUserId(), userInfo.getOrgId(), EVENT_MEG_MODULE_CODE, eventId, viewLink, msgContent, EVENT_MSG_ACTION, receiver);
					}
				}
			}
		}
	}

	/**
	 * 批量读取事件相关消息
	 * @param eventId	事件id
	 */
	private void batchReadMessage(Long eventId) {
		if(eventId != null && eventId > 0) {
			Map<String, Object> params = new HashMap<String, Object>();

			params.put("bizId", eventId);
			params.put("moduleCode", EVENT_MEG_MODULE_CODE);

			messageService.batchReadMessage(params);
		}
	}

}
