package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.rmq.producer.RmqProducer;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.NodeTransition;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.om.Remind;
import cn.ffcs.workflow.om.TaskReceive;
import cn.ffcs.workflow.om.Workflow;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.ReadProperties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "eventDisposalWorkflowService")
public class EventDisposalWorkflowServiceImpl extends
		ApplicationObjectSupport implements IEventDisposalWorkflowService {

	//功能配置接口
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private HessianFlowService hessianFlowService;
	
	@Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;
    
    private String RMQ_IP = ReadProperties.javaLoadProperties("rmq.ip", "global.properties");//RMQ的ip地址
    private String RMQ_PORT = ReadProperties.javaLoadProperties("rmq.port", "global.properties");//RMQ的端口
    private String RMQ_VHOST_FLOW = ReadProperties.javaLoadProperties("rmq.vhost_flow", "global.properties");//RMQ监听流程变化虚拟主机
    private String RMQ_EXCHANGESTR_FLOW = ReadProperties.javaLoadProperties("rmq.exchangeStr_flow", "global.properties");//RMQ监听流程变化交换机标识
    private String RMQ_FLOW_OPEN = ReadProperties.javaLoadProperties("event.isOpenFlowMonitor", "global.properties");//是否开启mq事件环节推送
	
	@Override
	public String startFlowByWorkFlow(Long workFlowId, Integer eventId,
			String wftypeId, UserBO user, OrgSocialInfoBO org,
			Map<String, Object> userMap) throws Exception {
		//启动工作流无需推送处理前的流程环节
		Map<String, Object> params = new HashMap<String, Object>();
		IEventDisposalWorkflowService eventDisposalWorkflowService = null; 
		String result = null;
		
		params.put("workflowId", workFlowId);
		eventDisposalWorkflowService = this.capServiceImpl(params, org.getOrgCode());
		eventDisposalWorkflowService.startFlowByWorkFlow(workFlowId, eventId, wftypeId, user, org, userMap);
		
		if(StringUtils.isNotBlank(result)
				&&Long.valueOf(result)>0) {
			
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "start");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
			
			afterElement.put("eventId", eventId);
			afterElement.put("instanceId", result);
			mqParams.put("afterElement", afterElement);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo) throws Exception {
		//启动工作流无需推送处理前的流程环节
		Map<String, Object> params = new HashMap<String, Object>();
		IEventDisposalWorkflowService eventDisposalWorkflowService = null;
		String result = null;
		
		params.put("workflowId", workFlowId);
		
		eventDisposalWorkflowService = this.capServiceImpl(params, userInfo.getOrgCode());
		
		result = eventDisposalWorkflowService.startFlowByWorkFlow(eventId, workFlowId, toClose, userInfo);
		
		if(StringUtils.isNotBlank(result)
				&&Long.valueOf(result)>0) {
			
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "start");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
			
			afterElement.put("eventId", eventId);
			afterElement.put("instanceId", result);
			mqParams.put("afterElement", afterElement);
            Map<String,Object> workFlow=new HashMap<String,Object>();
            workFlow.put("userInfo",userInfo);
            mqParams.put("workFlow", workFlow);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		//启动工作流无需推送处理前的流程环节
		String orgCode = "", result = null;
		Map<String, Object> params = new HashMap<String, Object>();
		IEventDisposalWorkflowService eventDisposalWorkflowService = null;
		
		if(userInfo != null) {
			orgCode = userInfo.getOrgCode();
		}
		
		params.put("workflowId", workFlowId);
		
		eventDisposalWorkflowService = this.capServiceImpl(params, orgCode);
		result = eventDisposalWorkflowService.startFlowByWorkFlow(eventId, workFlowId, toClose, userInfo, extraParam);
		
		if(StringUtils.isNotBlank(result)
				&&Long.valueOf(result)>0) {
			
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "start");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
			
			afterElement.put("eventId", eventId);
			afterElement.put("instanceId", result);
			mqParams.put("afterElement", afterElement);
            Map<String,Object> workFlow=new HashMap<String,Object>();
            workFlow.put("userInfo",userInfo);
            workFlow.put("toClose",toClose);
            mqParams.put("workFlow", workFlow);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Override
	public String startEndWorkflowForEvent(Long eventId, Long workFlowId, UserInfo userInfo) throws Exception{
		//启动工作流无需推送处理前的流程环节
		Map<String, Object> params = new HashMap<String, Object>();
		IEventDisposalWorkflowService eventDisposalWorkflowService = null;
		String result = null;
		
		params.put("workflowId", workFlowId);
		eventDisposalWorkflowService = this.capServiceImpl(params, userInfo.getOrgCode());
		result = eventDisposalWorkflowService.startEndWorkflowForEvent(eventId, workFlowId, userInfo);
		
		if(StringUtils.isNotBlank(result)
				&&Long.valueOf(result)>0) {
			
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "start");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
			
			afterElement.put("eventId", eventId);
			afterElement.put("instanceId", result);
			mqParams.put("afterElement", afterElement);
            Map<String,Object> workFlow=new HashMap<String,Object>();
            workFlow.put("userInfo",userInfo);
            mqParams.put("workFlow", workFlow);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public String startEndWorkflowForEvent(Map<String, Object> eventMap) throws Exception{
		//启动工作流无需推送处理前的流程环节
		String orgCode = "";
		if(CommonFunctions.isNotBlank(eventMap, "userInfo")) {
			UserInfo userInfo = (UserInfo)eventMap.get("userInfo");
			orgCode = userInfo.getOrgCode();
		}
		
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl(eventMap, orgCode);
		String result = eventDisposalWorkflowService.startEndWorkflowForEvent(eventMap);
		
		if(StringUtils.isNotBlank(result)
				&&Long.valueOf(result)>0) {
			
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "start");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
            afterElement.put("eventId", eventMap.get("eventId"));
			afterElement.put("instanceId", result);
			mqParams.put("afterElement", afterElement);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> initFlowInfo(String taskId, Long instanceId,
			UserInfo userInfo, Map<String, Object> params) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl(formatParamIn(null, instanceId, params), userInfo.getOrgCode());
		
		return eventDisposalWorkflowService.initFlowInfo(taskId, instanceId, userInfo, params);
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo) throws Exception{
		Map<String,Object> searchParams=new HashMap<String,Object>(),
						   params = new HashMap<String, Object>();
		searchParams.put("instanceId", instanceId);
		Map<String, Object> beforeElement = this.findRmqInitInfo(searchParams,userInfo.getOrgCode());
		
		IEventDisposalWorkflowService eventDisposalWorkflowService = null;
		boolean result = false;
		
		params.put("instanceId", instanceId);
		eventDisposalWorkflowService = this.capServiceImpl(params, userInfo.getOrgCode());
		result = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo);
		
		if(result) {
			
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "submit");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
			
			afterElement.put("instanceId", instanceId);
			mqParams.put("afterElement", afterElement);
			mqParams.put("beforeElement", beforeElement);
            Map<String,Object> workFlow=new HashMap<String,Object>();
            workFlow.put("userInfo",userInfo);
            mqParams.put("workFlow", workFlow);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{
		Map<String,Object> searchParams=new HashMap<String,Object>();
		searchParams.put("instanceId", instanceId);
		searchParams.put("userInfo", userInfo);
		Map<String, Object> beforeElement = this.findRmqInitInfo(searchParams,userInfo.getOrgCode());
		String orgCode = "";
		Map<String, Object> params = new HashMap<String, Object>();

		if(userInfo != null) {
			orgCode = userInfo.getOrgCode();
		}
		
		params.put("instanceId", instanceId);
		
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl(params, orgCode);
		
		boolean result = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		if(result) {
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "submit");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
            afterElement.put("eventId", extraParam.get("eventId"));
			afterElement.put("instanceId", instanceId);
			mqParams.put("afterElement", afterElement);
			mqParams.put("beforeElement", beforeElement);
            Map<String,Object> workFlow=new HashMap<String,Object>();
            workFlow.put("nextStaffId", nextStaffId);
            workFlow.put("nextOrgIds", curOrgIds);
            workFlow.put("userInfo",userInfo);
            workFlow.put("nextNodeName",nextNodeName);
            mqParams.put("workFlow", workFlow);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Map<String, Object> params, UserInfo userInfo) throws Exception {
		Map<String, Object> beforeElement = this.findRmqInitInfo(params,userInfo.getOrgCode());
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl(params, userInfo.getOrgCode());
		boolean result = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(params, userInfo);
		
		if(result) {
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "submit");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
			
			afterElement.put("instanceId", params.get("instanceId"));
			afterElement.put("eventId", params.get("eventId"));
			mqParams.put("afterElement", afterElement);
			mqParams.put("beforeElement", beforeElement);
            Map<String,Object> workFlow=new HashMap<String,Object>();
            workFlow.put("userInfo",userInfo);
            mqParams.put("workFlow", workFlow);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Override
	public boolean archiveAndEndWorkflowForEvent(Map<String, Object> params){
		Map<String, Object> beforeElement = this.findRmqInitInfo(params,"");
		
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		boolean result = eventDisposalWorkflowService.archiveAndEndWorkflowForEvent(params);
		if(result) {
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "archive");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
			
			afterElement.put("instanceId", params.get("instanceId"));
			afterElement.put("eventId", params.get("eventId"));
			mqParams.put("afterElement", afterElement);
			mqParams.put("beforeElement", beforeElement);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Override
	public boolean archiveAndEndWorkflowForEvent(Long eventId, UserInfo userInfo, String advice, Map<String, Object> extraParam) throws Exception {
		
		Map<String,Object> searchParams=new HashMap<String,Object>();
		searchParams.put("eventId", eventId);
		searchParams.put("instanceId", extraParam.get("instanceId"));
		Map<String, Object> beforeElement = this.findRmqInitInfo(searchParams,"");
		
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");

		boolean result = eventDisposalWorkflowService.archiveAndEndWorkflowForEvent(eventId, userInfo, advice, extraParam);
		
		if(result) {
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "archive");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
			
			afterElement.put("instanceId", extraParam.get("instanceId"));
			afterElement.put("eventId", eventId);
			mqParams.put("afterElement", afterElement);
			mqParams.put("beforeElement", beforeElement);
            Map<String,Object> workFlow=new HashMap<String,Object>();
            workFlow.put("userInfo",userInfo);
            mqParams.put("workFlow", workFlow);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Override
	public boolean archiveAndEndWorkflowForEvent(Long instanceId, String taskId, String userName, String orgName, String advice, Map<String, Object> extraParam){
		Map<String,Object> searchParams=new HashMap<String,Object>();
		searchParams.put("eventId", extraParam.get("eventId"));
		searchParams.put("instanceId", instanceId);
		Map<String, Object> beforeElement = this.findRmqInitInfo(searchParams,"");
		
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		boolean result = eventDisposalWorkflowService.archiveAndEndWorkflowForEvent(instanceId, taskId, userName, orgName, advice, extraParam);
		
		if(result) {
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "archive");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
			
			afterElement.put("instanceId", instanceId);
			afterElement.put("eventId", extraParam.get("eventId"));
			mqParams.put("afterElement", afterElement);
			mqParams.put("beforeElement", beforeElement);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public boolean rejectWorkFlow(String taskId, String instanceId,
			String advice, UserInfo userInfo) throws Exception {
		Map<String,Object> searchParams=new HashMap<String,Object>(),
						   params = new HashMap<String, Object>();
		searchParams.put("instanceId", instanceId);
		Map<String, Object> beforeElement = this.findRmqInitInfo(searchParams,userInfo.getOrgCode());
		IEventDisposalWorkflowService eventDisposalWorkflowService = null;
		boolean result = false;
		
		params.put("instanceId", instanceId);
		
		eventDisposalWorkflowService = this.capServiceImpl(params, userInfo.getOrgCode());
		result = eventDisposalWorkflowService.rejectWorkFlow(taskId, instanceId, advice, userInfo);
		if(result) {
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "reject");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
			
			afterElement.put("instanceId", instanceId);
			mqParams.put("afterElement", afterElement);
			mqParams.put("beforeElement", beforeElement);
            Map<String,Object> workFlow=new HashMap<String,Object>();
            workFlow.put("userInfo",userInfo);
            mqParams.put("workFlow", workFlow);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Override
	public boolean rejectWorkFlow(String taskId, String instanceId,
			String advice) throws Exception {
		Map<String,Object> searchParams=new HashMap<String,Object>();
		searchParams.put("instanceId", instanceId);
		Map<String, Object> beforeElement = this.findRmqInitInfo(searchParams,"");
		
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		boolean result = eventDisposalWorkflowService.rejectWorkFlow(taskId, instanceId, advice);
		
		if(result) {
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "reject");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
			
			afterElement.put("instanceId", instanceId);
			mqParams.put("afterElement", afterElement);
			mqParams.put("beforeElement", beforeElement);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	@Override
	public boolean rejectWorkFlow(Map<String, Object> params, UserInfo userInfo) throws Exception {
		Map<String,Object> searchParams=new HashMap<String,Object>();
		searchParams.put("instanceId", params.get("instanceId"));
		searchParams.put("eventId", params.get("eventId"));
		Map<String, Object> beforeElement = this.findRmqInitInfo(searchParams,userInfo.getOrgCode());
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl(params, userInfo.getOrgCode());
		
		boolean result = eventDisposalWorkflowService.rejectWorkFlow(params, userInfo);
		if(result) {
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "reject");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
			
			afterElement.put("instanceId", params.get("instanceId"));
			afterElement.put("eventId", params.get("eventId"));
			mqParams.put("afterElement", afterElement);
			mqParams.put("beforeElement", beforeElement);
            Map<String,Object> workFlow=new HashMap<String,Object>();
            workFlow.put("userInfo",userInfo);
            mqParams.put("workFlow", workFlow);
            try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public boolean recallWorkflow(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		IEventDisposalWorkflowService eventDisposalWorkflowService = null;
		String userOrgCode = "";
		
		if(userInfo != null) {
			userOrgCode = userInfo.getOrgCode();
		}
		
		Map<String,Object> searchParams=new HashMap<String,Object>();
		searchParams.put("instanceId", instanceId);
		searchParams.put("eventId", params.get("eventId"));
		Map<String, Object> beforeElement = this.findRmqInitInfo(searchParams,userOrgCode);
		eventDisposalWorkflowService = this.capServiceImpl(formatParamIn(null, instanceId, params), userOrgCode);
		
		boolean result = eventDisposalWorkflowService.recallWorkflow(instanceId, userInfo, params);
		if(result) {
			Map<String,Object> mqParams=new HashMap<String,Object>();
			mqParams.put("eventFlag", "recall");
			
			Map<String,Object> afterElement=new HashMap<String,Object>();
			
			afterElement.put("instanceId", instanceId);
			afterElement.put("eventId", params.get("eventId"));
			mqParams.put("afterElement", afterElement);
			mqParams.put("beforeElement", beforeElement);
            Map<String,Object> workFlow=new HashMap<String,Object>();
            workFlow.put("userInfo",userInfo);
            mqParams.put("workFlow", workFlow);
			try {
				sendFlowRmqMsg(mqParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Override
	public boolean sendSmsSyn(String userIds, String otherMobiles,
			String smsContent, UserInfo userInfo) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.sendSmsSyn(userIds, otherMobiles, smsContent, userInfo);
	}

	@Override
	public void sendSms(String userIds, String otherMobiles,
			String smsContent, UserInfo userInfo) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		eventDisposalWorkflowService.sendSms(userIds, otherMobiles, smsContent, userInfo);
	}
	
	@Override
	public void sendSms(String userIds, String otherMobiles,
			String nextNodeName, Long instanceId, String smsContent, UserInfo userInfo) {
		Map<String, Object> params = new HashMap<String, Object>();
		IEventDisposalWorkflowService eventDisposalWorkflowService = null;
		
		params.put("instanceId", instanceId);
		eventDisposalWorkflowService = this.capServiceImpl(params, userInfo.getOrgCode());
		
		eventDisposalWorkflowService.sendSms(userIds, otherMobiles, nextNodeName, instanceId, smsContent, userInfo);
	}
	
	@Override
	public void sendSms(String userIds, String otherMobiles, String smsContent, UserInfo userInfo, Map<String, Object> params) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl(params, userInfo.getOrgCode());
		eventDisposalWorkflowService.sendSms(userIds, otherMobiles, smsContent, userInfo, params);
	}
	
	@Override
	public String capSmsContent(String taskId, String noteType, String advice, EventDisposal event, UserInfo userInfo){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.capSmsContent(taskId, noteType, advice, event, userInfo);
	}
	
	@Override
	public Map<String, Object> capInfo4SMS(String curNodeName, String nextNodeName, Map<String, Object> params, UserInfo userInfo) throws Exception {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl(params, userInfo.getOrgCode());
		
		return eventDisposalWorkflowService.capInfo4SMS(curNodeName, nextNodeName, params, userInfo);
	}
	
	@Override
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.queryProInstanceDetail(instanceId);
	}

	@Override
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int sqlOrder){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.queryProInstanceDetail(instanceId, sqlOrder);
	}
	
	@Override
	public List<Map<String, Object>> queryProInstanceDetail(Long instanceId, int sqlOrder, UserInfo userInfo){
		Map<String, Object> params = new HashMap<String, Object>();
		IEventDisposalWorkflowService eventDisposalWorkflowService = null;
		
		params.put("instanceId", instanceId);
		eventDisposalWorkflowService = this.capServiceImpl(params, userInfo.getOrgCode());
		
		return eventDisposalWorkflowService.queryProInstanceDetail(instanceId, sqlOrder, userInfo);
	}

	@Override
	public List<Map<String, Object>> querySubTaskDetails(String taskId, int sqlOrder, UserInfo userInfo){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl(userInfo.getOrgCode());
		
		return eventDisposalWorkflowService.querySubTaskDetails(taskId, sqlOrder, userInfo);
	}
	
	@Override
	public boolean addRemind(Long instanceId, Long taskId, UserInfo userInfo,
			String remarks, String userIds, String userNames) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl(userInfo.getOrgCode());
		
		return eventDisposalWorkflowService.addRemind(instanceId, taskId, userInfo, remarks, userIds, userNames);
	}
	
	@Override
	public boolean addRemind(Long instanceId, Long taskId, UserInfo userInfo,
			String remarks, String userIds) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl(userInfo.getOrgCode());
		
		return eventDisposalWorkflowService.addRemind(instanceId, taskId, userInfo, remarks, userIds);
	}

	@Override
	public boolean addRemind(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl(formatParamIn(null, instanceId, params), userInfo.getOrgCode());

		return eventDisposalWorkflowService.addRemind(instanceId,userInfo,params);
	}

	@Override
	public List<Remind> queryRemindListByTaskId(Long taskId) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.queryRemindListByTaskId(taskId);
	}
	
	@Override
	public List<Map<String, Object>> queryMyTaskParticipation(String taskId){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.queryMyTaskParticipation(taskId);
	}
	
	@Override
	public boolean supervise(Long instanceId, Long taskId, UserInfo userInfo,
			String remarks, String userIds, String userNames) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.supervise(instanceId, taskId, userInfo, remarks, userIds, userNames);
	}

	@Override
	public boolean supervise(Long instanceId, Long taskId, UserInfo userInfo,
			String remarks, String userIds) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.supervise(instanceId, taskId, userInfo, remarks, userIds);
	}

	@Override
	public boolean supervise(Long instanceId, Long taskId, UserInfo userInfo,
			String remarks, Long[] userIds, String[] userNames) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.supervise(instanceId, taskId, userInfo, remarks, userIds, userNames);
	}

	@Override
	public boolean addAttention(Long eventId, String formTypeId,
			UserInfo userInfo, Date date) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.addAttention(eventId, formTypeId, userInfo, date);
	}

	@Override
	public boolean addAttention(Long eventId, UserInfo userInfo) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.addAttention(eventId, userInfo);
	}
	
	@Override
	public Map<String, Object> curNodeData(Long instanceId){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.curNodeData(instanceId);
	}
	
	@Override
	public String curNodeUserIds(String taskId){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.curNodeUserIds(taskId);
	}
	
	@Override
	public String curNodeUserIds(Long instanceId){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.curNodeUserIds(instanceId);
	}

	@Override
	public String curNodeUserIds(List<Map<String, Object>> taskPerson){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.curNodeUserIds(taskPerson);
	}
	
	@Override
	public String curNodeUserNames(Long instanceId){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.curNodeUserNames(instanceId);
	}
	
	@Override
	public String curNodeUserNames(String userIdStr){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.curNodeUserNames(userIdStr);
	}
	
	@Override
	public Long curNodeTaskId(Long instanceId) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.curNodeTaskId(instanceId);
	}
	
	@Override
	public String curNodeTaskName(Long instanceId) throws Exception{
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.curNodeTaskName(instanceId);
	}
	
	@Override
	public Map<String, Object> capTurnLevel(String curnodeName, String nextNodeName, UserInfo userInfo) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.capTurnLevel(curnodeName, nextNodeName, userInfo);
	}
	
	@Override
	public String capNextNodeName(String curNodeName, int level){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.capNextNodeName(curNodeName, level);
	}
	
	@Override
	public Long capWorkflowId(Long instanceId, Long eventId, UserInfo userInfo, Map<String, Object> extraParam){
		String orgCode = userInfo.getOrgCode();
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("instanceId", instanceId);
		params.put("eventId", eventId);
		
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl(params, orgCode);
		
		return eventDisposalWorkflowService.capWorkflowId(instanceId, eventId, userInfo, extraParam);
	}

	@Override
	public String capWorkflowName(Long eventId, String eventType, UserInfo userInfo) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.capWorkflowName(eventId, eventType, userInfo);
	}
	
	@Override
	public String capWorkflowHandlePage(Long eventId, String eventType, UserInfo userInfo, Map<String, Object> extraParam) {
		String userOrgCode = null;
		IEventDisposalWorkflowService eventDisposalWorkflowService = null;
		
		if(userInfo != null) {
			userOrgCode = userInfo.getOrgCode();
		} else if(CommonFunctions.isNotBlank(extraParam, "userOrgCode")) {
			userOrgCode = extraParam.get("userOrgCode").toString();
		} else if(CommonFunctions.isNotBlank(extraParam, "orgCode")) {
			userOrgCode = extraParam.get("orgCode").toString();
		}
		
		eventDisposalWorkflowService = this.capServiceImpl(extraParam, userOrgCode);
		
		return eventDisposalWorkflowService.capWorkflowHandlePage(eventId, eventType, userInfo, extraParam);
	}
	
	@Override
	public boolean isNewWorkflow(Long instanceId) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.isNewWorkflow(instanceId);
	}

	@Override
	public boolean isNewWorkflow(String instanceId) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.isNewWorkflow(instanceId);
	}
	
	@Override
	public Long saveOrUpdateTask(Map<String, Object> params){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.saveOrUpdateTask(params);
	}
	
	@Override
	public boolean isUserInfoCurrentUser(String taskId, String instanceId, UserInfo userInfo) throws Exception{
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.isUserInfoCurrentUser(taskId, instanceId, userInfo);
	}
	
	@Override
	public boolean isUserInfoCurrentUser(String taskId, Long instanceId, UserInfo userInfo) throws Exception{
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.isUserInfoCurrentUser(taskId, instanceId, userInfo);
	}
	
	@Override
	public boolean isUserAbleToStartWorkflow(Long orgId, Long userId) throws Exception{
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.isUserAbleToStartWorkflow(orgId, userId);
	}
	
	@Override
	public boolean isUserAbleToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception{
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.isUserAbleToStartWorkflow(orgSocialInfo, userId);
	}
	
	@Override
	public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {
		String orgCode = "";
		if(orgSocialInfo != null) {
			orgCode = orgSocialInfo.getOrgCode();
		}
		
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl(orgCode);
		
		return eventDisposalWorkflowService.checkUserToStartWorkflow(orgSocialInfo, userId);
	}
	
	@Override
	public int checkUserToStartWorkflow(Long orgId, Long userId) throws Exception {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.checkUserToStartWorkflow(orgId, userId);
	}
	
	@Override
	public List<Map<String, Object>> findNodeActorsById(Integer nodeId){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.findNodeActorsById(nodeId);
	}
	
	@Override
	public Node findNodeById(Integer nodeId){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.findNodeById(nodeId);
	}
	
	@Override
	public Node findNodeById(Long instanceId, String nodeName){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.findNodeById(instanceId, nodeName);
	}
	
	@Override
	public String capInstanceIdByEventId(Long eventId){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.capInstanceIdByEventId(eventId);
	}
	
	@Override
	public String capInstanceIdByEventId(Long eventId, Map<String, Object> params) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = null;
		String userOrgCode = "";
		
		if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		}
		
		eventDisposalWorkflowService = this.capServiceImpl(userOrgCode);
		
		return eventDisposalWorkflowService.capInstanceIdByEventId(eventId, params);
	}
	
	@Override
	public Long capInstanceIdByEventIdForLong(Long eventId){
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
	}
	
	@Override
	public Long capInstanceIdByEventIdForLong(Long eventId, Map<String, Object> params){
		IEventDisposalWorkflowService eventDisposalWorkflowService = null;
		String userOrgCode = "";
		
		if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		}
		
		eventDisposalWorkflowService = this.capServiceImpl(userOrgCode);
		
		return eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId, params);
	}
	
	@Override
	public ProInstance capProInstanceByEventId(Long eventId) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.capProInstanceByEventId(eventId);
	}
	
	@Override
	public ProInstance capProInstanceByEventId(Long eventId, Map<String, Object> params) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = null;
		String userOrgCode = "";
		
		if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		}
		
		eventDisposalWorkflowService = this.capServiceImpl(userOrgCode);
		
		return eventDisposalWorkflowService.capProInstanceByEventId(eventId, params);
	}
	
	@Override
	public ProInstance capProInstanceByInstanceId(Long instanceId) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
	}
	
	@Override
	public Map<String, Object> findFormByTaskId(Long taskId, Map<String, Object> params) throws Exception {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.findFormByTaskId(taskId, params);
	}
	
	@Override
	public Map<String, Object> capDoneTaskInfo(Long instanceId, String taskName) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.capDoneTaskInfo(instanceId, taskName);
	}
	
	@Override
	public Map<String, Object> findDoneTaskInfoLatest(Long instanceId, String taskNodeCode) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.findDoneTaskInfoLatest(instanceId, taskNodeCode);
	}
	
	@Override
	public Map<String, Object> turnTodoPerson(Map<String, Object> params) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.turnTodoPerson(params);
	}

	@Override
	public EUDGPagination queryTaskMngList(int pageNo, int pageSize,
			Map<String, Object> params) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.queryTaskMngList(pageNo, pageSize, params);
	}
	
	@Override
	public boolean receiveTask(TaskReceive taskReceive, UserInfo userInfo, Map<String, Object> extraParam) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.receiveTask(taskReceive, userInfo, extraParam);
	}
	
	@Override
	public List<TaskReceive> findTaskReceivedList(Long taskId, Long instanceId,
			Map<String, Object> extraParam) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.findTaskReceivedList(taskId, instanceId, extraParam);
	}
	
	@Override
	public Workflow queryWorkflowById(Long workflowId) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.queryWorkflowById(workflowId);
	}

	@Override
	public List<Workflow> queryWorkflows(Workflow workflow) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.queryWorkflows(workflow);
	}

	@Override
	public List<Node> queryNodes(Long workflowId) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.queryNodes(workflowId);
	}
	
	@Override
	public List<Node> queryNodes(Long workflowId, Integer deploymentId) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.queryNodes(workflowId, deploymentId);
	}

	@Override
	public List<Node> queryNodes(Node node) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.queryNodes(node);
	}
	
	@Override
	public NodeTransition findNodeTransition(Integer curNodeId,
			Integer nextNodeId) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.findNodeTransition(curNodeId, nextNodeId);
	}
	
	@Override
	public int delRemind(Map<String, Object> params, UserInfo userInfo) throws Exception {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.delRemind(params, userInfo);
	}
	
	@Override
	public List<Map<String, Object>> findRemindList(Map<String, Object> params) throws Exception {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.findRemindList(params);
	}
	
	@Override
	public List<Map<String, Object>> findRemindInvalidList(Map<String, Object> params) throws Exception {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.findRemindInvalidList(params);
	}
	
	/**
	 * 获取推送流程信息的mq初始值
	 * @param 查询参数		
	 * @return
	 */
	@Override
	public Map<String,Object> findRmqInitInfo(Map<String, Object> params,String orgCode) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl(orgCode);
		
		return eventDisposalWorkflowService.findRmqInitInfo(params,orgCode);
	}
	
	/**
	 * 新增处理中办理过程
	 * @param eventId
	 * @param instanceId
	 * @param taskName
	 * @param advice
	 * @param userInfo
	 * @return taskId duedate
	 */
	@Override
	public Map<String, Object> saveHandlingTask(Long eventId, Long instanceId, String taskName, String advice, UserInfo userInfo, Map<String, Object> extraParam) {
		IEventDisposalWorkflowService eventDisposalWorkflowService = this.capServiceImpl("");
		
		return eventDisposalWorkflowService.saveHandlingTask(eventId, instanceId, taskName, advice, userInfo, extraParam);
	}

	/**
	 * 格式化输入参数
	 * @param eventId		事件id
	 * @param instanceId	流程实例id
	 * @param params
	 * @return
	 */
	private Map<String, Object> formatParamIn(Long eventId, Long instanceId, Map<String, Object> params) {
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isBlank(params, "eventId")) {
			if(eventId != null && eventId > 0) {
				params.put("eventId", eventId);
			} else if(CommonFunctions.isNotBlank(params, "formId")) {
				params.put("eventId", params.get("formId"));
			}
		}
		
		if(CommonFunctions.isBlank(params, "instanceId")) {
			if(instanceId != null && instanceId > 0) {
				params.put("instanceId", instanceId);
			}
		}
		
		return params;
	}
	
	/**
	 * 获取对应的实现类
	 * @param orgCode 组织编码
	 * @return
	 */
	private IEventDisposalWorkflowService capServiceImpl(String orgCode) {
		return capServiceImpl(null, orgCode);
	}
	
	
	/**
	 * 获取对应的实现类
	 * @param params
	 * 			eventDisposalWorkflowService	自定义的实现类，用于当orgCode为空的情况
	 * 			instanceId						流程实例id
	 * 			eventId							事件id
	 * @param orgCode 组织编码
	 * 
	 * @return
	 */
	private IEventDisposalWorkflowService capServiceImpl(Map<String, Object> params, String orgCode) {
		String serviceImpl = "";

		if(params != null && CommonFunctions.isNotBlank(params, "eventDisposalWorkflowService")) {
			serviceImpl = params.get("eventDisposalWorkflowService").toString();
		} else {
			Long instanceId = null, eventId = null;
			String //trigCondition = ConstantValue.EVENT_WORKFLOW,
				   //eventType = null,
				   workflowName = null;
			ProInstance pro = null;
			
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				try {
					instanceId = Long.valueOf(params.get("instanceId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			if(CommonFunctions.isNotBlank(params, "eventId")) {
				try {
					eventId = Long.valueOf(params.get("eventId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			if(instanceId != null && instanceId > 0) {
				pro = this.capProInstanceByInstanceId(instanceId);
			} else if(eventId != null && eventId > 0) {
				pro = this.capProInstanceByEventId(eventId);
			}
			
			if(pro != null) {
				if(eventId == null || eventId < 0) {
					eventId = pro.getFormId();
				}
				if(instanceId == null || instanceId < 0) {
					instanceId = pro.getInstanceId();
				}
				workflowName = pro.getProName();
			} else if(CommonFunctions.isNotBlank(params, "workflowId")) {
				Long workflowId = null;
				
				try {
					workflowId = Long.valueOf(params.get("workflowId").toString());
				} catch(NumberFormatException e) {}
				
				if(workflowId != null && workflowId > 0) {
					Workflow workflow = new Workflow();
					List<Workflow> workflowList = null;
					
					workflow.setWorkFlowId(workflowId);
					workflowList = hessianFlowService.queryWorkflows(workflow);
					
					if(workflowList != null && workflowList.size() > 0) {
						workflowName = workflowList.get(0).getFlowName();
					}
				}
			}
			
			if(ConstantValue.EVENT_WORKFLOW_NAME.equals(workflowName)) {
				serviceImpl = "eventDisposalWorkflowForEventService";
			} else {
				/*eventType = this.capEventType(instanceId, eventId, orgCode);
				
				if(StringUtils.isNotBlank(eventType)) {
					trigCondition += "_" + eventType; 
				}*/
				String trigCondition = this.capEventTrigCondition(instanceId, eventId, orgCode, workflowName);
				
				serviceImpl = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, trigCondition, IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, orgCode, IFunConfigurationService.DIRECTION_UP_REGEXP);
			}
		}
		
		if(StringUtils.isBlank(serviceImpl)) {
			serviceImpl = "eventDisposalWorkflowForEventService";
		}
		
		return (IEventDisposalWorkflowService) this.getApplicationContext().getBean(serviceImpl);
	}
	
	/**
	 * 构建工作流接口配置获取触发条件
	 * @param instanceId	流程实例id
	 * @param eventId		事件id
	 * @param orgCode		组织编码
	 * @param workflowName	流程图名称
	 * @return
	 */
	private String capEventTrigCondition(Long instanceId, Long eventId, String orgCode, String workflowName) {
		EventDisposal event = null;
		StringBuffer trigCondition = new StringBuffer("");
		ProInstance proInstance = null;
		String EVENT_WORKFLOW_TEMPLATE = ConstantValue.EVENT_WORKFLOW + "@workflowName@@bizPlatform@@eventType@",
			   eventWorkflowTrig = null,
			   eventType = null,
			   bizPlatform = null;
		
		if((instanceId != null && instanceId > 0) && (eventId == null || eventId < 0)) {
			proInstance = this.capProInstanceByInstanceId(instanceId);
			
			if(proInstance != null) {//通过instanceId查找事件类别
				eventId = proInstance.getFormId();
				workflowName = proInstance.getProName();
			}
		}
		
		if(eventId != null && eventId > 0) {//获取事件类别
			event = eventDisposalService.findEventByIdSimple(eventId);

			if(event != null) {
				String eventBizPlatform = event.getBizPlatform();
				
				eventType = event.getType();

				if(StringUtils.isNotBlank(eventType) && StringUtils.isNotBlank(eventBizPlatform) && !ConstantValue.DEFAULT_EVENT_BIZ_PLATFORM.equals(eventBizPlatform)) {
					bizPlatform = eventBizPlatform;
				}
			}
		}
		
		eventType = StringUtils.isBlank(eventType) ? "" : "_" + eventType;
		bizPlatform = StringUtils.isBlank(bizPlatform) ? "" : "_" + bizPlatform;
		eventWorkflowTrig = EVENT_WORKFLOW_TEMPLATE.replace("@eventType@", eventType);
		
		trigCondition.append("[").append(eventWorkflowTrig.replace("@workflowName@", "").replace("@bizPlatform@", ""));
		
		eventWorkflowTrig = eventWorkflowTrig.replace("@bizPlatform@", bizPlatform);
		
		if(StringUtils.isNotBlank(bizPlatform)) {
			trigCondition.append("|").append(eventWorkflowTrig.replace("@workflowName@", ""));
		}
		
		if(StringUtils.isNotBlank(workflowName)) {
			trigCondition.append("|").append(eventWorkflowTrig.replace("@workflowName@", "-" + workflowName));
		}
		
		trigCondition.append("]");

		return trigCondition.toString();
	}
	
	private Boolean sendFlowRmqMsg(Map<String,Object> params) throws Exception{
		
		if(StringUtils.isNotBlank(RMQ_FLOW_OPEN)
				&&Boolean.valueOf(RMQ_FLOW_OPEN)) {
			
			Object flowMonitorRmqProducer=null;
			try {
				flowMonitorRmqProducer = defaultListableBeanFactory.getBean("flowMonitorRmqProducer");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(flowMonitorRmqProducer==null) {
				//进行初始化
				logger.info("rmq推送流程初始化开始");
				RmqProducer thisProducer=new RmqProducer();
				if(StringUtils.isBlank(RMQ_IP)) {
					logger.error("流程同步失败，未配置rmq连接的IP地址，请在zk中配置rmq.ip后重新启动");
					return false;
				}else {
					thisProducer.setRmqServerIP(RMQ_IP);
				}
				if(StringUtils.isBlank(RMQ_PORT)) {
					logger.error("流程同步失败，未配置rmq连接的PORT端口，请在zk中配置rmq.port后重新启动");
					return false;
				}else {
					thisProducer.setRmqServerPort(Integer.valueOf(RMQ_PORT));
				}
				if(StringUtils.isBlank(RMQ_VHOST_FLOW)) {
					logger.error("流程同步失败，未配置rmq连接的虚拟主机，请在zk中配置rmq.vhost_flow后重新启动");
					return false;
				}else {
					thisProducer.setRmqVirtualHost(RMQ_VHOST_FLOW);
				}
				if(StringUtils.isBlank(RMQ_EXCHANGESTR_FLOW)) {
					logger.error("流程同步失败，未配置rmq连接的交换机，请在zk中配置rmq.exchangeStr_flow后重新启动");
					return false;
				}else {
					thisProducer.setExchangeStr(RMQ_EXCHANGESTR_FLOW);
				}
				
				flowMonitorRmqProducer=thisProducer;
				//将new出的对象放入Spring容器中
				defaultListableBeanFactory.registerSingleton("flowMonitorRmqProducer",thisProducer);
				//自动注入依赖
				beanFactory.autowireBean(thisProducer);
				
				//重连机制初始化
				thisProducer.afterPropertiesSet();
				
				logger.info("rmq推送流程初始化结束");
			}
			
			RmqProducer flowRmqProducer=(RmqProducer) flowMonitorRmqProducer;
			
			try {
				
				flowRmqProducer.sendMessage(RMQ_EXCHANGESTR_FLOW, params);
				
			} catch (Exception e) {
				
				logger.error("发送rmq信息出错，事件流程推送失败!");
				e.printStackTrace();
				
			}
		}
		
		
		return true;
	}
	
}
