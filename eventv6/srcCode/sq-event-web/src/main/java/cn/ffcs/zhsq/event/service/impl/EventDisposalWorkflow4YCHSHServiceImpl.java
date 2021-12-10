package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.common.message.bo.ReceiverBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 江苏省盐城市(YCHSH) 工作流使用接口实现
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflow4YCHSHService")
public class EventDisposalWorkflow4YCHSHServiceImpl extends EventDisposalWorkflowForEventNewServiceImpl {
	@Autowired
	private MessageOutService messageService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	private static final String EVENT_MEG_MODULE_CODE = "02";	//事件消息所属模块编码
	private static final String CLOSE_NODE_CODE = "task8";			//结案环节编码
	
	@Override
	public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		String instanceId = super.startFlowByWorkFlow(eventId, workFlowId, toClose, userInfo, extraParam);
		
		//采集并结案时，发送消息
		if(StringUtils.isNotBlank(instanceId) && ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose)) {
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
		}
		
		return instanceId;
	}
	
	@Override
	public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice,UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception{
		boolean flag = super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo, smsContent, extraParam);
		
		if(flag) {
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
			if(!ConstantValue.HANDLING_TASK_CODE.equals(nextNodeName)) {
				batchReadMessage(eventId);
				
				if(StringUtils.isBlank(nextStaffId)) {
					EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
					
					if(event != null && ConstantValue.EVENT_STATUS_ARCHIVE.equals(event.getStatus())) {
						nextStaffId = this.curNodeUserIds(instanceId);
					}
				}
				
				if(StringUtils.isNotBlank(nextStaffId)) {
					String[] nextUserIdArray = nextStaffId.split(",");
					List<UserInfo> nextUserInfoList = new ArrayList<UserInfo>();
					Long nextUserId = null;
					UserInfo nextUserInfo = null;
					
					for(String nextUserIdStr : nextUserIdArray) {
						nextUserId = null;
						
						try {
							nextUserId = Long.valueOf(nextUserIdStr);
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
						
						if(nextUserId != null && nextUserId > 0) {
							nextUserInfo = new UserInfo();
							nextUserInfo.setUserId(nextUserId);
							nextUserInfoList.add(nextUserInfo);
						}
					}
					
					if(nextUserInfoList.size() > 0) {
						this.sendMessage(eventId, nextUserInfoList, userInfo);
					}
				}
				
				if(CLOSE_NODE_CODE.equals(nextNodeName)) {
					sendClosedMessage(eventId, userInfo);
				}
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean rejectWorkFlow(String taskId, String instanceId,
			String advice, UserInfo userInfo) throws Exception {
		boolean flag = super.rejectWorkFlow(taskId, instanceId, advice, userInfo);
		
		if(flag) {
			Map<String, Object> curTaskData = null;
			List<UserInfo> curUserInfoList = null;
			Long instanceIdL = Long.valueOf(instanceId);
			ProInstance proInstance = this.capProInstanceByInstanceId(instanceIdL);
			Long eventId = null;
			
			if(proInstance != null) {
				eventId = proInstance.getFormId();
			}
			
			if(eventId != null && eventId > 0) {
				curTaskData = this.curNodeData(instanceIdL);
				
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
				
				batchReadMessage(eventId);
				
				sendMessage(eventId, curUserInfoList, userInfo);
			}
		}
		
		return flag;
	}
	
	@Override
	public int checkUserToStartWorkflow(OrgSocialInfoBO orgSocialInfo, Long userId) throws Exception {
		int result = -1;
		StringBuffer msgWrong = new StringBuffer("");
		
		if(orgSocialInfo != null) {
			String orgLevel = orgSocialInfo.getChiefLevel(),
					  orgType = orgSocialInfo.getOrgType();
			
			if(StringUtils.isNotBlank(orgLevel)) {
				if(ConstantValue.DISTRICT_ORG_LEVEL.compareTo(orgLevel) > 0) {
					msgWrong.append("组织层级不可超过县区级，请先检验！");
				}
			} else {
				msgWrong.append("当前组织层级不存在，请先检验！");
			}
			
			if(StringUtils.isNotBlank(orgType)) {
				if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgType)) {
					//do nothing
				} else if(String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType)) {
					msgWrong.append("组织类型不可为部门，请先检验！");
				} else {
					msgWrong.append("当前用户的组织类型有误：既不是单位，也不是部门。请先修正！");
				}
			} else {
				msgWrong.append("当前组织类型不存在，请先检验！");
			}
			
		} else {
			msgWrong.append("当前组织不存在，请先检验！");
		}
		
		if(msgWrong.length() > 0) {
			throw new Exception(msgWrong.toString());
		} else {
			result = super.checkUserToStartWorkflow(orgSocialInfo, userId);
		}
		
		return result;
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
						try {
							messageService.sendCommonMessageA(userInfo.getUserId(), userInfo.getOrgId(), EVENT_MEG_MODULE_CODE, eventId, viewLink, msgContent, EVENT_MSG_ACTION, receiver);
						}catch (Exception e) {
							System.out.println("eventDisposalWorkflow4YCHSHService:"+eventId);
							e.printStackTrace();
						}
					
					}
				}
			}
    	}
    }
    
    /**
     * 发送结案消息给事件发起人员
     * @param eventId	事件id
     * @param userInfo	操作用户信息
     */
    private void sendClosedMessage(Long eventId, UserInfo userInfo) {
    	EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
    	
    	if(event != null) {
			String EVENT_VIEW_LINK = "$EVENT_DOMAIN/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=todo&eventId=", //消息查看详情链接
					  EVENT_MSG_ACTION = "发送了", // 消息发送操作
					  viewLink = null, msgContent = "您有一条事件已被结案，单号为：@code。",//消息内容
					  eventStatus = event.getStatus(), 
					  code = event.getCode();
			
			//结案待评(03)时，给发起人发送结案消息提醒
			if (ConstantValue.EVENT_STATUS_ARCHIVE.equals(eventStatus)) {
				ProInstance pro = this.capProInstanceByEventId(eventId);
				Long creatorId = null, operateUserId = null;
				
				if(pro != null) {
					creatorId = pro.getUserId();
				}
				
				if(userInfo != null) {
					operateUserId = userInfo.getUserId();
				}
				
				//只要判断是不是同一个用户，无需判断组织是否一致
				if(creatorId != null && operateUserId != null && !creatorId.equals(operateUserId)) {
					ReceiverBO receiver = new ReceiverBO();
					List<Long> msgReceiverUserIdList = new ArrayList<Long>();
					
					viewLink = EVENT_VIEW_LINK + eventId;
					msgContent = msgContent.replaceAll("@code",code);
					msgReceiverUserIdList.add(creatorId);
					
					receiver.setUserIdList(msgReceiverUserIdList);
					
					messageService.sendCommonMessageA(userInfo.getUserId(), userInfo.getOrgId(), EVENT_MEG_MODULE_CODE, eventId, viewLink, msgContent, EVENT_MSG_ACTION, receiver);
				}
			}
    	}
    }
    
}
