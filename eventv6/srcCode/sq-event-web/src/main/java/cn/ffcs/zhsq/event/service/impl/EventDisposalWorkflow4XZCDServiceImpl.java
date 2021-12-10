package cn.ffcs.zhsq.event.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.common.message.bo.ReceiverBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 西藏昌都 工作流接口
 * @ClassName:   EventDisposalWorkflow4XZCDServiceImpl   
 * @author:      youwj
 * @date:        2020年6月29日 下午4:30:50
 */
@Service(value = "eventDisposalWorkflow4XZCDService")
public class EventDisposalWorkflow4XZCDServiceImpl extends EventDisposalWorkflowForEventNewServiceImpl {
	@Autowired
	private MessageOutService messageService;

	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String EVENT_MEG_MODULE_CODE = "02";	//事件消息所属模块编码

	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception {
		boolean result = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		
		if(result) {
			//提交成功发送站内消息
			try {
				ProInstance pro = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
				batchReadMessage(pro.getFormId());
				sendMsg4Event(instanceId, null, nextStaffId, curOrgIds, userInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}


	@Override
	public boolean rejectWorkFlow(Map<String, Object> params, UserInfo userInfo) throws Exception {
		boolean result = super.rejectWorkFlow(params, userInfo);
		
		if(result) {
			Long eventId = -1L, instanceId = -1L;
			
			if(CommonFunctions.isNotBlank(params, "eventId")) {
				eventId = Long.valueOf(params.get("eventId").toString());
			}
			
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			}
			batchReadMessage(eventId);
			sendMsg4Event(instanceId, eventId, null, null, userInfo);
		}
		
		return result;
	}
	
	
	/**
	 * 发送事件消息
	 * @param instanceId	实例id
	 * @param eventId		事件id
	 * @param nextUserIdStr	下一办理人员，多个值以英文逗号连接，为空时，使用当前办理人员
	 * @param nextOrgIdStr	下一办理人员组织，多个值以英文逗号连接，nextUserIdStr为空时使用
	 * @param userInfo		办理用户信息
	 */
	private void sendMsg4Event(Long instanceId, Long eventId, String nextUserIdStr, String nextOrgIdStr, UserInfo userInfo) {
		if(instanceId != null && instanceId > 0 && (eventId == null || eventId < 0)) {
			ProInstance pro = this.capProInstanceByInstanceId(instanceId);
			
			if(pro != null) {
				eventId = pro.getFormId();
			}
		} else if(eventId != null && eventId > 0 && (instanceId == null || instanceId < 0)) {
			instanceId = this.capInstanceIdByEventIdForLong(eventId);
		}
		
		if(instanceId != null && instanceId > 0 && eventId != null && eventId > 0) {
			ReceiverBO receiver = null;
			
			if(StringUtils.isBlank(nextUserIdStr) && StringUtils.isBlank(nextOrgIdStr)) {
				nextUserIdStr = this.curNodeUserIds(instanceId);
			}
			
			if(StringUtils.isNotBlank(nextUserIdStr)) {
				String[] nextUserIdArray = nextUserIdStr.split(",");
				List<Long> nextUserIdList = new ArrayList<Long>();
				receiver = new ReceiverBO();
				
				for(String nextUserId : nextUserIdArray) {
					nextUserIdList.add(Long.valueOf(nextUserId));
				}
				
				receiver.setUserIdList(nextUserIdList);
				
				
			} else if(StringUtils.isNotBlank(nextOrgIdStr)) {
				String[] nextOrgIdArray = nextOrgIdStr.split(",");
				List<Long> nextOrgIdList = new ArrayList<Long>();
				receiver = new ReceiverBO();
				
				for(String nextOrgId : nextOrgIdArray) {
					nextOrgIdList.add(Long.valueOf(nextOrgId));
				}
				
				receiver.setOrgIdList(nextOrgIdList);
			}
			
			if(receiver != null) {
				EventDisposal findEventByIdSimple = eventDisposalService.findEventByIdSimple(eventId);
				String EVENT_VIEW_LINK = "$EVENT_DOMAIN/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=todo&eventId="; //消息查看详情链接
				String EVENT_MEG_MODULE_CODE = "02",//事件消息所属模块编码
					   EVENT_MSG_ACTION = "发送了",//消息发送操作
					   viewLink = EVENT_VIEW_LINK+eventId, 
					   msgContent = "您有一条待办事件，单号为:"+findEventByIdSimple.getCode()+",请尽快处理";//消息内容
				
				messageService.sendCommonMessageA(userInfo.getUserId(), userInfo.getOrgId(), EVENT_MEG_MODULE_CODE, eventId, viewLink, msgContent, EVENT_MSG_ACTION, receiver);
			}
			
			
		} else {
			logger.error("未发送事件消息，流程实例id为[" + instanceId + "]， 事件id为[" + eventId + "]！");
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
