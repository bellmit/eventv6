package cn.ffcs.zhsq.timeApplication.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.common.message.bo.ReceiverBO;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventDuplication.service.IEventDuplicationService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCallbackService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 江苏省盐城市 盐都区(YDQ) 预处理事件审核回调
 * @author zhangls
 *
 */
@Service(value = "timeApplicationCallback4YDQEventService")
public class TimeApplicationCallback4YDQEventServiceImpl implements ITimeApplicationCallbackService {

	@Autowired
	private ITimeApplicationService timeApplicationService;
	
	@Autowired
	private ITimeApplicationCheckService timeApplicationCheckService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventDuplicationService eventDuplicationService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private MessageOutService messageService;
	
	private static final String EVENT_MEG_MODULE_CODE = "02";//事件消息所属模块编码
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void timeApplicationCallback(Map<String, Object> params) {
		String timeAppCheckStaus = null, eventSubStatus = null, applicationType = null;
		UserInfo userInfo = new UserInfo();
		Map<String, Object> timeApplication = new HashMap<String, Object>();
		Long eventId = -1L, oprateUserId = null, operateOrgId = null;
		
		if(CommonFunctions.isNotBlank(params, "timeAppCheckStatus")) {
			timeAppCheckStaus = params.get("timeAppCheckStatus").toString();
		}
		
		if(CommonFunctions.isNotBlank(params, "updaterId")) {
			try {
				oprateUserId = Long.valueOf(params.get("updaterId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		} else if(CommonFunctions.isNotBlank(params, "auditorId")) {
			try {
				oprateUserId = Long.valueOf(params.get("auditorId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(params, "updaterName")) {
			userInfo.setPartyName(params.get("updaterName").toString());
		}
		if(CommonFunctions.isNotBlank(params, "updaterOrgId")) {
			try {
				operateOrgId = Long.valueOf(params.get("updaterOrgId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		} else if(CommonFunctions.isNotBlank(params, "auditorOrgId")) {
			try {
				operateOrgId = Long.valueOf(params.get("auditorOrgId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(params, "updaterOrgName")) {
			userInfo.setOrgName(params.get("updaterOrgName").toString());
		}
		
		if(oprateUserId != null && oprateUserId > 0) {
			userInfo.setUserId(oprateUserId);
		}
		if(operateOrgId != null && operateOrgId > 0) {
			userInfo.setOrgId(operateOrgId);
		}
		if(CommonFunctions.isNotBlank(params, "applicationId")) {
			Long applicationId = Long.valueOf(params.get("applicationId").toString());
			
			timeApplication = timeApplicationService.findTimeAppliationById(applicationId, null);
			
			if(CommonFunctions.isNotBlank(timeApplication, "businessKeyId")) {
				try {
					eventId = Long.valueOf(timeApplication.get("businessKeyId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(CommonFunctions.isNotBlank(timeApplication, "applicationType")) {
			applicationType = timeApplication.get("applicationType").toString();
		}
		
		if(ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_PREPRESS.toString().equals(applicationType)) {
			//审核不通过时，增添当前任务的预处理子任务
			if(ITimeApplicationCheckService.CHECK_STATUS.STATUS_FAIL.getValue().equals(timeAppCheckStaus)) {
				Long instanceId = -1L;
				
				//事件预处理审核类型时，bussinessId为instanceId
				if(CommonFunctions.isNotBlank(timeApplication, "businessId")) {
					instanceId = Long.valueOf(timeApplication.get("businessId").toString());
				}
				
				if(instanceId != null && instanceId > 0) {
					Map<String, Object> taskMap = new HashMap<String, Object>();
					
					taskMap.put("taskId", eventDisposalWorkflowService.curNodeTaskId(instanceId));//事件预处理任务的parentTaskId
					taskMap.put("instanceId", instanceId);
					taskMap.put("taskName", ConstantValue.PREPRESS_TASK_CODE);
					taskMap.put("type", ConstantValue.HANDLING_TASK_TYPE);
					
					if(CommonFunctions.isNotBlank(params, "updaterId")) {
						taskMap.put("transactorId", params.get("updaterId"));
					}
					if(CommonFunctions.isNotBlank(params, "updaterName")) {
						taskMap.put("taskactorName", params.get("updaterName"));
					}
					if(CommonFunctions.isNotBlank(params, "updaterOrgId")) {
						taskMap.put("transactOrgId", params.get("updaterOrgId"));
					}
					if(CommonFunctions.isNotBlank(params, "updaterOrgName")) {
						taskMap.put("transactOrgName", params.get("updaterOrgName"));
					}
					if(CommonFunctions.isNotBlank(params, "checkAdvice")) {
						taskMap.put("taskAdvice", params.get("checkAdvice"));
					}
					
					Long taskId = eventDisposalWorkflowService.saveOrUpdateTask(taskMap);
					
					if(taskId == null || taskId < 0) {
						logger.error("添加预处理事件子任务失败！");
					}
				}
				
				eventSubStatus = ConstantValue.PREPRESS_REJECT_STATUS;
				
				
			} else if(ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.getValue().equals(timeAppCheckStaus)) {
				eventSubStatus = ConstantValue.PREPRESS_PASS_STATUS;
			}
			
			if(eventId != null && eventId > 0) {
				//调整事件子状态便于列表查看该事件的预处理结果
				if(StringUtils.isNotBlank(eventSubStatus)) {
					EventDisposal event = new EventDisposal();
					
					event.setEventId(eventId);
					event.setSubStatus(eventSubStatus);
					
					if(CommonFunctions.isNotBlank(params, "updaterId")) {
						try {
							event.setUpdatorId(Long.valueOf(params.get("updaterId").toString()));
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
					}
					
					eventDisposalService.updateEventDisposalById(event);
				}
				
				//增添重复事件关联信息
				if(CommonFunctions.isNotBlank(params, "duplicateEventId")) {
					Map<String, Object> eventDuplication = new HashMap<String, Object>();
					
					eventDuplication.put("leadEventId", eventId);
					
					eventDuplication.put("duplicateEventId", params.get("duplicateEventId"));
					
					eventDuplicationService.saveEventDuplication(eventDuplication, userInfo);
				}
				
			}
		} else if(ITimeApplicationService.APPLICATION_TYPE.EVENT_CHECK.getValue().equals(applicationType)) {
			Long checkId = null;
			
			if(ITimeApplicationCheckService.CHECK_STATUS.STATUS_FAIL.getValue().equals(timeAppCheckStaus)) {
				//核验不通过之后，将以往的待办等消息自动设置为已读
				batchReadMessage(eventId);
				
				EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
				if(event != null) {
					Long gridId = event.getGridId();
					ProInstance proInstance = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
					String eventCode = event.getCode(),
							  eventName = event.getEventName(),
							  timeAppCheckAdvice = "";
					
					if(CommonFunctions.isNotBlank(params, "checkAdvice")) {
						timeAppCheckAdvice = params.get("checkAdvice").toString();
					}
					
					if(proInstance != null) {
						//向事件发起人员发送核验不通过消息
						Long creatorId = proInstance.getUserId();
						
						if(creatorId != null && creatorId > 0) {
							String msgContent = "您发起的事件：【@eventCode】@eventTitle，核验不通过，核验意见为【@timeAppCheckAdvice】，请知晓";//消息内容
							List<Long> msgReceiveUserIdList = new ArrayList<Long>();
							
							msgContent = msgContent.replaceAll("@eventCode", eventCode)
																	  .replaceAll("@eventTitle", eventName)
																	  .replaceAll("@timeAppCheckAdvice", timeAppCheckAdvice);
							
							msgReceiveUserIdList.add(creatorId);
							
							sendMessage(eventId, msgReceiveUserIdList, msgContent, userInfo);
						}
					} else {
						throw new IllegalArgumentException("事件id【" + eventId + "】没有对应有效的流程实例信息！");
					}
					
					//向事件所属网格对应的社区、街道ABD账号发送核验不通过消息
					if(gridId != null && gridId > 0) {
						MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
						if(gridInfo != null) {
							Integer gridLevel = gridInfo.getGridLevel();
							
							if(gridLevel != null) {
								Long operateUserId = userInfo.getUserId(), orgId = null, communityGridId = null;
								String gridPath = "";
								OrgSocialInfoBO orgInfo = orgSocialInfoService.getOrgIdByLocationCode(gridInfo.getInfoOrgCode());
								List<UserInfo> msgReceiveUserList = new ArrayList<UserInfo>();
								
								if(orgInfo != null) {
									orgId = orgInfo.getOrgId();
								}
								
								if(orgId != null && orgId > 0) {
									switch(gridLevel) {
										case INodeCodeHandler.GRID : {//网格
											communityGridId = gridInfo.getParentGridId();
											gridPath = gridInfo.getGridName() + gridPath;
											
											msgReceiveUserList.addAll(this.fetchReceverUserInfo(gridLevel, INodeCodeHandler.COMMUNITY, operateUserId, orgInfo));
										}
										case  INodeCodeHandler.COMMUNITY : {//社区
											MixedGridInfo communityGridInfo = null;
											
											if(communityGridId == null) {
												communityGridId = gridInfo.getGridId();
											}
											
											if(communityGridId != null && communityGridId > 0) {
												communityGridInfo = mixedGridInfoService.findMixedGridInfoById(communityGridId, false);
												
												if(communityGridInfo != null) {
													gridPath = communityGridInfo.getGridName() + gridPath;
												}
											}
											
											msgReceiveUserList.addAll(this.fetchReceverUserInfo(gridLevel, INodeCodeHandler.STREET, operateUserId, orgInfo));
											
											break;
										}
									}
								} else {
									throw new IllegalArgumentException("网格id【"+ gridId +"】没有对应有效的组织信息！");
								}
								
								if(msgReceiveUserList != null) {
									String receiverUserName = null;
									Set<Long> receiverUserId = new HashSet<Long>();
									
									//为了总体去重，所以无法拆分判断
									for(UserInfo receiver : msgReceiveUserList) {
										receiverUserName = receiver.getUserName();
										if(StringUtils.isNotBlank(receiverUserName) && (receiverUserName.startsWith("A") || receiverUserName.startsWith("B") || receiverUserName.startsWith("D"))) {
											receiverUserId.add(receiver.getUserId());
										}
									}
									
									String msgContent = "您辖区内【@gridPath】的事件：【@eventCode】@eventTitle，核验不通过，核验意见为【@timeAppCheckAdvice】，请知晓";//消息内容
									
									msgContent = msgContent.replaceAll("@gridPath", gridPath)
																			  .replaceAll("@eventCode", eventCode)
																			  .replaceAll("@eventTitle", eventName)
																			  .replaceAll("@timeAppCheckAdvice", timeAppCheckAdvice);
									
									sendMessage(eventId, new ArrayList<Long>(receiverUserId), msgContent, userInfo);
								}
							} else {
								throw new IllegalArgumentException("网格id【"+ gridId +"】对应的网格信息缺少网格层级属性！");
							}
						} else {
							throw new IllegalArgumentException("网格id【"+ gridId +"】没有对应有效的所属网格信息！");
						}
					} else {
						throw new IllegalArgumentException("事件【"+ eventCode +"】没有所属网格信息！");
					}
					
					//事件核验不通过，则删除事件
					List<Long> eventIdList = new ArrayList<Long>();
					
					eventIdList.add(eventId);
					
					eventDisposalService.deleteEventDisposalByIds(eventIdList, userInfo.getUserId());
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "checkId")) {
				try {
					checkId = Long.valueOf(params.get("checkId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			//变更审核操作人员，方便后续的展示
			if(checkId != null && checkId > 0) {
				Map<String, Object> timeAppCheck = new HashMap<String, Object>();
				timeAppCheck.put("checkId", checkId);
				timeAppCheck.put("auditorId", userInfo.getUserId());
				timeAppCheck.put("auditorOrgId", userInfo.getOrgId());
				
				try {
					timeApplicationCheckService.updateTimeAppCheckById(timeAppCheck);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	/**
	 * 构建消息接收人员
	 * @param gridLevel			起始网格层级
	 * @param targetGridLevel	目标网格层级
	 * @param operateUserId		操作用户id
	 * @param rootOrgInfo		起始查找组织根节点
	 * @return
	 */
	private List<UserInfo> fetchReceverUserInfo(Integer gridLevel, Integer targetGridLevel, Long operateUserId, OrgSocialInfoBO rootOrgInfo) {
		String operatorNodeCode = INodeCodeHandler.ORG_UINT + gridLevel,
				  nodeCode = operatorNodeCode + INodeCodeHandler.OPERATE_REPORT + (gridLevel - targetGridLevel) + INodeCodeHandler.ORG_UINT  + targetGridLevel;
		List<GdZTreeNode> ztreeNodeList = null;
		List<UserInfo> msgReceiveUserList = new ArrayList<UserInfo>();
		Long rootOrgId = null;
		
		if(rootOrgInfo != null) {
			rootOrgId = rootOrgInfo.getOrgId();
		}
		
		if(rootOrgId != null && rootOrgId > 0) {
			UserInfo fiveKeyUserInfo = new UserInfo();
			
			fiveKeyUserInfo.setOrgId(rootOrgId);
			fiveKeyUserInfo.setOrgCode(rootOrgInfo.getOrgCode());
			
			try {
				ztreeNodeList = fiveKeyElementService.getTreeForEvent(fiveKeyUserInfo, rootOrgId, nodeCode, null, ConstantValue.GOV_PROFESSION_CODE, null);
				if(ztreeNodeList != null) {
					for(GdZTreeNode ztreeNode : ztreeNodeList) {
						msgReceiveUserList.addAll(fiveKeyElementService.getUserInfoList(Long.valueOf(ztreeNode.getId()), nodeCode, operateUserId, null));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return msgReceiveUserList;
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
	 * 发送站内消息，不给操作人员发送消息
	 * @param eventId			消息相关事件id
	 * @param receiverIdList	接收消息用户id
	 * @param msgContent	接收消息内容
	 * @param userInfo			操作用户信息
	 */
	private void sendMessage(Long eventId, List<Long> receiverIdList, String msgContent, UserInfo userInfo) {
		String EVENT_MSG_ACTION = "发送了", // 消息发送操作
				  viewLink = null;
		
		if(receiverIdList != null && receiverIdList.size() > 0) {
			Long operateUserId = userInfo.getUserId();
			ReceiverBO receiver = new ReceiverBO();
			
			if(operateUserId != null && operateUserId > 0) {
				receiverIdList.remove(operateUserId);
			}
			
			receiver.setUserIdList(receiverIdList);
			
			messageService.sendCommonMessageA(userInfo.getUserId(), userInfo.getOrgId(), EVENT_MEG_MODULE_CODE, eventId, viewLink, msgContent, EVENT_MSG_ACTION, receiver);
		}
	}

}
