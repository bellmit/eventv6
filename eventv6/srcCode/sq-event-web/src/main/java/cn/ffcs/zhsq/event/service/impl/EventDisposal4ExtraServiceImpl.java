package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EvaResult;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.event.IEvaResultService;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposal4ExtraService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.persistence.event.EventDisposal4ExtraMapper;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.data.DateUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件额外信息处理接口
 * @author zhangls
 *
 */
@Service("eventDisposal4ExtraService")
public class EventDisposal4ExtraServiceImpl implements
		IEventDisposal4ExtraService {
	@Autowired
	private EventDisposal4ExtraMapper eventDisposal4ExtraMapper;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private IEvaResultService evaResultService;
	
	@Autowired
	private IAttachmentService attachmentService;
	
	@Autowired
	private MessageOutService messageService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Map<String, Object> archiveWorkflow4Event(String eventIdStr,
			UserInfo userInfo, Map<String, Object> params) throws Exception {
		if(StringUtils.isBlank(eventIdStr)) {
			throw new IllegalArgumentException("缺少需要归档处理的事件id！");
		}
		
		if(userInfo == null || StringUtils.isBlank(userInfo.getOrgCode()) || userInfo.getUserId() == null) {
			throw new IllegalArgumentException("缺少有效的办理用户信息！");
		}
		
		String[] eventIdArray = eventIdStr.split(",");
		List<Long> eventIdList = new ArrayList<Long>();
		StringBuffer msgWrong = new StringBuffer("");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		for(String eventId : eventIdArray) {
			if(StringUtils.isNotBlank(eventId)) {
				try {
					eventIdList.add(Long.valueOf(eventId));
				} catch(NumberFormatException e) {
					msgWrong.append("事件id【").append(eventId).append("】不是有效的数值！");
				}
			}
		}
		
		if(eventIdList.size() == 0) {
			throw new IllegalArgumentException("缺少有效的事件id！");
		}
		
		if(msgWrong.length() > 0) {
			resultMap.put("msgWrong", msgWrong.toString());
		} else {
			resultMap = archiveWorkflow4Event(eventIdList, userInfo, params);
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> archiveWorkflow4Event(List<Long> eventIdList,
			UserInfo userInfo, Map<String, Object> params) throws Exception {
		if(eventIdList == null || eventIdList.size() == 0) {
			throw new IllegalArgumentException("缺少需要归档处理的事件id！");
		}
		
		if(userInfo == null || StringUtils.isBlank(userInfo.getOrgCode()) || userInfo.getUserId() == null) {
			throw new IllegalArgumentException("缺少有效的办理用户信息！");
		}
		
		StringBuffer msgWrong = new StringBuffer("");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean archiveEventFlag = false;
		int successTotal = 0;
		
		for(Long eventId : eventIdList) {
			if(eventId != null && eventId > 0) {
				archiveEventFlag = false;
				
				try {
					archiveEventFlag = archiveEventDisposal(eventId, userInfo, params);
				} catch (Exception e) {
					msgWrong.append("事件id【").append(eventId).append("】归档失败：").append(e.getMessage());
				}
				
				if(archiveEventFlag) {
					successTotal++;
				}
			} else {
				msgWrong.append("事件id【").append(eventId).append("】不是有效的数值！");
			}
		}
		
		if(msgWrong.length() > 0) {
			resultMap.put("msgWrong", msgWrong.toString());
		}
		
		resultMap.put("successTotal", successTotal);
		
		return resultMap;
	}
	
	/**
	 * 对事件进行归档操作，并自动评价
	 * 需要事件状态为03，事件下一办理环节只有归档
	 * 
	 * @param eventId	事件id
	 * @param userInfo	办理用户信息
	 * @param params
	 * 			evaLevel	评价等级
	 * 			evaContent	评价内容
	 * @return
	 * @throws Exception
	 */
	private boolean archiveEventDisposal(Long eventId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		EventDisposal event = null;
		boolean result = false;
		
		event = eventDisposalService.findEventByIdSimple(eventId);
		
		if(event != null) {
			if(!ConstantValue.EVENT_STATUS_ARCHIVE.equals(event.getStatus())) {
				throw new ZhsqEventException("该事件事件状态为【"+ event.getStatus() +"】不可进行归档操作！");
			}
			
			Long instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
			Map<String, Object> extraParam = new HashMap<String, Object>();
			List<Node> nextNodeList = null;
			
			extraParam.put("instanceId", instanceId);
			
			nextNodeList = baseWorkflowService.findNextTaskNodes(null, null, eventId, userInfo, extraParam);
			
			if(nextNodeList != null) {
				Map<String, Object> curTaskData = eventDisposalWorkflowService.curNodeData(instanceId);
				String curNodeName = null;
				
				if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
					curNodeName = curTaskData.get("NODE_NAME").toString();
				}
				
				if(StringUtils.isNotBlank(curNodeName)) {
					Long curTaskId = null;
					
					if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
						try {
							curTaskId = Long.valueOf(curTaskData.get("WF_DBID_").toString());
						} catch(NumberFormatException e) {}
					}
					
					if(nextNodeList.size() == 1) {
						Node nextNode = nextNodeList.get(0);
						String nextNodeName = nextNode.getNodeName(),
							   nextUserId = null,
							   nextOrgId = null,
							   advice = null,
							   evaContent = null,
							   evaLevel = null;
						
						Map<String, Object> elementMap = fiveKeyElementService.getNodeInfoForEvent(userInfo, curNodeName, nextNodeName, nextNode.getTransitionCode(), String.valueOf(nextNode.getNodeId()), params);
						
						if(CommonFunctions.isNotBlank(elementMap, "userIds")) {
							nextUserId = elementMap.get("userIds").toString();
						}
						
						if(CommonFunctions.isNotBlank(elementMap, "orgIds")) {
							nextOrgId = elementMap.get("orgIds").toString();
						}
						
						if(CommonFunctions.isNotBlank(params, "evaContent")) {
							evaContent = params.get("evaContent").toString();
						}
						
						if(CommonFunctions.isNotBlank(params, "advice")) {
							advice = params.get("advice").toString();
						} else {
							advice = evaContent;
						}

						if(CommonFunctions.isNotBlank(params, "evaLevel")) {
							evaLevel = params.get("evaLevel").toString();
						}

						//构造评价所需参数
						if(StringUtils.isNotBlank(evaLevel) && StringUtils.isNotBlank(evaContent)) {
							params.put("isEvaluate", true);
							params.put("eventId", eventId);
							params.put("evaLevel", evaLevel);
							params.put("evaContent", evaContent);
						}
						result = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(instanceId, curTaskId.toString(), nextNodeName, nextUserId, nextOrgId, advice, userInfo, null, params);
						
						/*if(result) {
							ProInstance proInstance = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
							String PRO_STATUS_END = "2";//流程结束
							
							//流程结束后，生成评价信息
							if(PRO_STATUS_END.equals(proInstance.getStatus())) {
								evaLevel = null;
								
								if(CommonFunctions.isNotBlank(params, "evaLevel")) {
									evaLevel = params.get("evaLevel").toString();
								}
								
								if(StringUtils.isNotBlank(evaLevel) && StringUtils.isNotBlank(evaContent)) {
									EvaResult evaResult = new EvaResult();
									evaResult.setObjectId(eventId);
									evaResult.setEvaObj(ConstantValue.EVA_OBJ_NEW_EVENT);
									evaResult.setEvaLevel(evaLevel);
									evaResult.setEvaContent(evaContent);
									evaResult.setCreateUser(userInfo.getUserId());
									evaResult.setCreatorName(userInfo.getOrgName()+"-"+userInfo.getPartyName());//思明区事件需要获取评价人部门  组织名称-人员名称
									evaResult.setUpdateUser(userInfo.getUserId());
									evaResult.setUpdaterName(userInfo.getPartyName());
			
									Long erId = evaResultService.saveEvaResult(evaResult);
									
									if(erId == null || erId < 0) {
										throw new ZhsqEventException("事件id【"+ eventId +"】评价信息添加失败！");
									}
								}
							}
						}*/
					} else {
						throw new ZhsqEventException("事件id【"+ eventId +"】有多个下一环可选择！");
					}
				}
			}
		} else {
			throw new ZhsqEventException("事件id【"+ eventId +"】没有对应有效的事件记录！");
		}
		
		return result;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> subWorkflow2AppointedTask(Map<String, Object> params, UserInfo userInfo) throws Exception {
		List<Long> eventIdList = new ArrayList<Long>();
		String nextNodeName = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int total = 0, successTotal = 0, failTotal = 0;
		StringBuffer msgWrong = new StringBuffer("");
		
		if(CommonFunctions.isNotBlank(params, "eventId")) {
			Long eventId = Long.valueOf(params.get("eventId").toString());
			
			if(eventId > 0) {
				eventIdList.add(eventId);
			}
		} else if(CommonFunctions.isNotBlank(params, "eventIdStr")) {
			String eventIdStr = params.get("eventIdStr").toString();
			String[] eventIdArray = eventIdStr.split(",");
			Long eventIdL = null;
			
			for(String eventId : eventIdArray) {
				try {
					eventIdL = Long.valueOf(eventId);
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if(eventIdL != null && eventIdL > 0) {
					eventIdList.add(eventIdL);
				}
			}
			
			
		} else if(CommonFunctions.isNotBlank(params, "eventIdList")) {
			Object eventIdListObj = params.get("eventIdList");
			
			if(eventIdListObj instanceof List) {
				eventIdList = (List<Long>) eventIdListObj;
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "nextNodeName")) {
			nextNodeName = params.get("nextNodeName").toString();
		}
		
		if(StringUtils.isBlank(nextNodeName)) {
			throw new IllegalArgumentException("缺少需要扭转至的节点名称【nextNodeName】！");
		}
		
		total = eventIdList.size();
		
		if(total > 0) {
			boolean subFlag = false;
			
			for(Long eventId : eventIdList) {
				try {
					subFlag = this.subWorkflow2AppointedTask(eventId, nextNodeName, userInfo, params);
				} catch(Exception e) {
					msgWrong.append("事件【").append(eventId).append("】事件办理失败，失败原因如下：").append(e.getMessage());
				}
				
				if(subFlag) {
					successTotal++;
				} else {
					failTotal++;
				}
			}
		} else {
			throw new IllegalArgumentException("缺少有效的事件id！");
		}
		
		resultMap.put("total", total);
		resultMap.put("successTotal", successTotal);
		resultMap.put("failTotal", failTotal);
		if(msgWrong.length() > 0) {
			resultMap.put("msgWrong", msgWrong.toString());
		}
		
		return resultMap;
	}
	
	/**
	 * 事件提交到下一指定环节
	 * @param eventId		事件id
	 * @param nextNodeName	下一环节名称
	 * @param userInfo		操作用户信息
	 * @param extraParam
	 * 			advice		办理意见
	 * 			smsContent	短信发送内容
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private boolean subWorkflow2AppointedTask(Long eventId, String nextNodeName, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		if(StringUtils.isBlank(nextNodeName)) {
			throw new IllegalArgumentException("缺少下一办理环节名称！");
		}
		
		Node nextNode = null;
		Long instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
		List<Node> nextNodeList = null;
		boolean subFlag = false;
		Map<String, Object> initFlowMap = null;
		
		if(instanceId != null && instanceId > 0) {
			initFlowMap = eventDisposalWorkflowService.initFlowInfo(null, instanceId, userInfo, null);
		} else {
			throw new IllegalArgumentException("事件id【" + eventId + "】缺少有效的流程实例信息！");
		}
		
		if(CommonFunctions.isNotBlank(initFlowMap, "taskNodes")) {
			nextNodeList = (List<Node>)initFlowMap.get("taskNodes");
		}
		
		if(nextNodeList != null && nextNodeList.size() > 0) {
			for(Node nextNodeTmp : nextNodeList) {
				if(nextNodeName.equals(nextNodeTmp.getNodeName())) {
					nextNode = nextNodeTmp;
					break;
				}
			}
		}
		
		if(nextNode != null) {
			Map<String, Object> curTaskData = eventDisposalWorkflowService.curNodeData(instanceId),
								elementMap = null,
								nodeParam = null;
			String curTaskId = null,
				   curNodeName = null,
				   nextOrgIds = null, 
				   nextUserIds = null;
			
			if(CommonFunctions.isNotBlank(curTaskData, "WF_DBID_")) {
				curTaskId = curTaskData.get("WF_DBID_").toString();
			}
			if(CommonFunctions.isNotBlank(curTaskData, "NODE_NAME")) {
				curNodeName = curTaskData.get("NODE_NAME").toString();
			}
			
			elementMap = fiveKeyElementService.getNodeInfoForEvent(userInfo, curNodeName, nextNodeName, nextNode.getTransitionCode(), String.valueOf(nextNode.getNodeId()), nodeParam);
			
			if(CommonFunctions.isNotBlank(elementMap, "isSelectUser")) {
				boolean isSelectUser = Boolean.valueOf(elementMap.get("isSelectUser").toString());
				
				if(isSelectUser) {
					if(CommonFunctions.isNotBlank(elementMap, "orgIds")) {
						nextOrgIds = elementMap.get("orgIds").toString();
					}
					if(CommonFunctions.isNotBlank(elementMap, "userIds")) {
						nextUserIds = elementMap.get("userIds").toString();
					}
				}
			}
			
			if(StringUtils.isNotBlank(nextOrgIds) && StringUtils.isNotBlank(nextUserIds)) {
				String advice = null, smsContent = null;
				
				if(CommonFunctions.isNotBlank(extraParam, "advice")) {
					advice = extraParam.get("advice").toString();
				}
				if(CommonFunctions.isNotBlank(extraParam, "smsContent")) {
					smsContent = extraParam.get("smsContent").toString();
				}
				
				subFlag = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(instanceId, curTaskId, nextNodeName, nextUserIds, nextOrgIds, advice, userInfo, smsContent, extraParam);
			} else {
				throw new Exception("下一环节【" + nextNode.getNodeNameZH() + "】缺少可直接使用的办理人员！");
			}
		} else {
			throw new IllegalArgumentException("事件id【" + eventId + "】无法提交至节点【" + nextNodeName + "】！");
		}
		
		return subFlag;
	}
	
	@Override
	public EUDGPagination findMsgPagination(int pageNo, int pageSize, Map<String, Object> params) {
		return messageService.findMessageListPage(pageNo, pageSize, params);
	}
	
	@Override
	public Map<String, Object> fetchEventInfo4Dock(Long eventId, UserInfo userInfo, Map<String, Object> params) throws Exception {
		Map<String, Object> eventMap = null;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(eventId != null && eventId > 0) {
			EventDisposal event = null;
			boolean isCapMapInfo = false;
			Map<String, Object> resultMap = null;
			
			if(CommonFunctions.isNotBlank(params, "isCapMapInfo")) {
				isCapMapInfo = Boolean.valueOf(params.get("isCapMapInfo").toString());
			}
			
			if(isCapMapInfo) {
				if(CommonFunctions.isBlank(params, "mapType")) {
					String TWO_DIMENSION_MAP_TYPE = "5";
					
					params.put("mapType", TWO_DIMENSION_MAP_TYPE);
				}
			}
			
			resultMap = eventDisposalService.findEventByMap(eventId, params);
			
			if(CommonFunctions.isNotBlank(resultMap, "event")) {
				event = (EventDisposal) resultMap.get("event");
			}
			
			if(event != null) {
				boolean isCapAttachment = false;
				
				eventMap = new HashMap<String, Object>();
				
				eventMap.put("eventId", event.getEventId());
				eventMap.put("code", event.getCode());
				eventMap.put("happenTimeStr", event.getHappenTimeStr());
				eventMap.put("occurred", event.getOccurred());
				eventMap.put("content", event.getContent());
				eventMap.put("contactUser", event.getContactUser());
				eventMap.put("tel", event.getTel());
				eventMap.put("gridCode", event.getGridCode());
				
				if(CommonFunctions.isNotBlank(params, "isCapAttachment")) {
					isCapAttachment = Boolean.valueOf(params.get("isCapAttachment").toString());
				}
				
				if(isCapAttachment) {
					List<Attachment> attachmentList = attachmentService.findByBizId(eventId, ConstantValue.EVENT_ATTACHMENT_TYPE);
					
					if(attachmentList != null && attachmentList.size() > 0) {
						StringBuffer attachmentId = new StringBuffer("");
						Map<String, Object> attachmentMap = null;
						List<Map<String, Object>> attachmentMapList = new ArrayList<Map<String, Object>>();
						
						for(Attachment attachment : attachmentList) {
							attachmentMap = new HashMap<String, Object>();
							
							attachmentMap.put("attachmentName", attachment.getFileName());
							attachmentMap.put("attachmentPath", attachment.getFilePath());
							attachmentMap.put("attachmentSize", attachment.getFileSize());
							
							attachmentMapList.add(attachmentMap);
							attachmentId.append(",").append(attachment.getAttachmentId());
						}
						
						if(attachmentId.length() > 0) {
							eventMap.put("attachmentIds", attachmentId.substring(1));
							eventMap.put("attachmentMapList", attachmentMapList);
						}
					}
				}
			} else {
				throw new IllegalArgumentException("事件id【" + eventId + "】未能找到有效的事件信息！");
			}
		} else {
			throw new IllegalArgumentException("请提供有效的事件id！");
		}
		
		
		return eventMap;
	}

	@Override
	public EUDGPagination findJurisdictionEvent4EvaPagination(int pageNo, int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = 0;
		List<Map<String, Object>> eventList = null;
		
		formatParamIn(params);
		
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			throw new IllegalArgumentException("缺失地域编码[infoOrgCode]！");
		}
		
		count = eventDisposal4ExtraMapper.findCount4JurisdictionEventEva(params);
		
		if(count > 0) {
			eventList = eventDisposal4ExtraMapper.findPageList4JurisdictionEventEva(params, rowBounds);
			
			formatEventDataOut(eventList, params);
		} else {
			eventList = new ArrayList<Map<String, Object>>();
		}
		
		EUDGPagination eventPagination = new EUDGPagination(count, eventList);
		
		return eventPagination;
	}

	@SuppressWarnings("serial")
	@Override
	public Map<String, Object> evaEvent4Batch(String eventIdStr, UserInfo userInfo, Map<String, Object> params) throws ZhsqEventException {
		List<EvaResult> evaResultList = new ArrayList<EvaResult>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String evaObj = null, evaLevel = null, evaContent = null, userOrgLevel = null,
			   STREET_EVA_OBJ = "0301", DISTRICT_EVA_OBJ = "0302";
		Integer evaType = 0;
		OrgSocialInfoBO userOrgInfo = null;
		
		if(StringUtils.isBlank(eventIdStr)) {
			throw new IllegalArgumentException("缺失需要评价操作的事件！");
		}
		
		if(userInfo == null || userInfo.equals(new UserInfo())) {
			throw new IllegalArgumentException("缺少有效的操作用户信息！");
		}
		
		userOrgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
		
		if(userOrgInfo != null) {
			userOrgLevel = userOrgInfo.getChiefLevel();
		}
		
		if(CommonFunctions.isNotBlank(params, "evaType")) {
			evaType = Integer.valueOf(params.get("evaType").toString());//不进行移除转换移除捕获
		}
		
		switch(evaType) {
			case 1: {
				evaObj = STREET_EVA_OBJ;
				
				if(!ConstantValue.STREET_ORG_LEVEL.equals(userOrgLevel)) {
					throw new ZhsqEventException("该评价操作需要街道层级人员！");
				}
				
				break;
			}
			case 2 : {
				evaObj = DISTRICT_EVA_OBJ;
				
				if(!ConstantValue.DISTRICT_ORG_LEVEL.equals(userOrgLevel)) {
					throw new ZhsqEventException("该评价操作需要县区层级人员！");
				}
				
				break;
			}
		}
		
		if(evaObj == null) {
			if(CommonFunctions.isNotBlank(params, "evaObj")) {
				evaObj = params.get("evaObj").toString();
			} else {
				throw new IllegalArgumentException("缺少评价对象类型[evaObj]设置！");
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "evaLevel")) {
			evaLevel = params.get("evaLevel").toString();
		} else {
			throw new IllegalArgumentException("缺少评价等级[evaLevel]设置！");
		}
		
		if(CommonFunctions.isNotBlank(params, "evaContent")) {
			evaContent = params.get("evaContent").toString();
		}
		
		String[] eventIdArray = eventIdStr.split(",");
		Long eventId = null;
		EvaResult evaResult = null;
		Map<String, String> evaObjNameMap = new HashMap<String, String>() {
			{
				put(STREET_EVA_OBJ, "镇级评价");
				put(DISTRICT_EVA_OBJ, "区级复评");
			}
		};
		String evaObjName = "";
		boolean isDuplicateEva = false, isAble2Eva = true;
		Map<String, Object> evaParams = new HashMap<String, Object>();
		int evaluatedResultCount = 0;
		StringBuffer msgWrong = new StringBuffer("");
		
		if(CommonFunctions.isNotBlank(params, "isDuplicateEva")) {
			isDuplicateEva = Boolean.valueOf(params.get("isDuplicateEva").toString());
		}
		
		if(evaObjNameMap.containsKey(evaObj)) {
			evaObjName = "[" + evaObjNameMap.get(evaObj) + "]";
		}
		
		resultMap.put("total", eventIdArray.length);
		
		for(String eventIdTmp : eventIdArray) {
			eventId = null;
			evaluatedResultCount = 0;
			isAble2Eva = true;
			
			try {
				eventId = Long.valueOf(eventIdTmp);
			} catch(NumberFormatException e) {}
			
			if(eventId != null && eventId > 0) {
				if(!isDuplicateEva) {
					evaParams.clear();
					evaParams.put("evaObj", evaObj);
					evaParams.put("objectId", eventId);
					evaluatedResultCount = evaResultService.findCountEvaResult(evaParams);
					
					isAble2Eva = evaluatedResultCount == 0;//不可重复评价时，要求对于评价类型尚未评价
					
					if(!isAble2Eva) {
						msgWrong.append("事件[").append(eventId).append("]已有").append(evaObjName)
								.append(" ").append(evaluatedResultCount).append("条！");
					}
				}
				
				if(isAble2Eva) {
					if(evaType == 1) {
						evaParams.clear();
						evaParams.put("evaObj", ConstantValue.EVA_OBJ_NEW_EVENT);
						evaParams.put("objectId", eventId);
						evaluatedResultCount = evaResultService.findCountEvaResult(evaParams);
						
						isAble2Eva = evaluatedResultCount == 1;//镇级评价要求事件评价已完成
						
						if(!isAble2Eva) {
							msgWrong.append("事件[").append(eventId).append("]尚未完成事件评价操作！");
						}
					} else if(evaType == 2) {//区级复评要求先完成镇级评价
						evaParams.clear();
						evaParams.put("evaObj", STREET_EVA_OBJ);
						evaParams.put("objectId", eventId);
						evaluatedResultCount = evaResultService.findCountEvaResult(evaParams);
						
						isAble2Eva = evaluatedResultCount > 0;//为了兼容可重复评价，否则需要调整为evaluatedResultCount==1
						
						if(!isAble2Eva) {
							msgWrong.append("事件[").append(eventId).append("]尚未完成[").append(evaObjNameMap.get(STREET_EVA_OBJ)).append("]操作！");
						}
					}
				}
				
				if(isAble2Eva) {
					evaResult = new EvaResult();
					evaResult.setObjectId(eventId);
					evaResult.setEvaObj(evaObj);
					evaResult.setEvaLevel(evaLevel);
					evaResult.setEvaContent(evaContent);
					evaResult.setCreateUser(userInfo.getUserId());
					evaResult.setCreatorName(evaObjName + userInfo.getOrgName() + "-" + userInfo.getPartyName());
					evaResult.setUpdateUser(userInfo.getUserId());
					evaResult.setUpdaterName(userInfo.getPartyName());
					
					evaResultList.add(evaResult);
				}
			}
		}
		
		if(evaResultList != null && evaResultList.size() > 0) {
			resultMap.put("successTotal", evaResultService.batchSaveEvaResult(evaResultList));
		}
		
		if(msgWrong.length() > 0) {//批量操作时，不建议逐条失败原因均在页面展示，只在后台日志打印
			logger.error(msgWrong.toString());
		}
		
		return resultMap;
	}
	
	@Override
	public int findCount4Eva(Map<String, Object> params) {
		formatParamIn(params);
		
		return evaResultService.findCountEvaResult(params);
	}
	
	/**
	 * 格式化输入参数
	 * @param params
	 */
	private void formatParamIn(Map<String, Object> params) {
		int evaType = 0;
		String STREET_EVA_OBJ = "0301", DISTRICT_EVA_OBJ = "0302";
		
		if(CommonFunctions.isNotBlank(params, "evaType")) {
			try {
				evaType = Integer.valueOf(params.get("evaType").toString());
			} catch(NumberFormatException e) {}
		}
		
		switch(evaType) {
			case 1: {
				params.put("evaObj", STREET_EVA_OBJ);
				break;
			}
			case 2: {
				params.put("evaObj", DISTRICT_EVA_OBJ);
				break;
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "eInfoOrgCode")) {//有加密的地域编码
			byte[] decodedData = Base64.decode(params.get("eInfoOrgCode").toString());
			params.put("infoOrgCode", new String(decodedData));
		} else if(CommonFunctions.isBlank(params, "infoOrgCode") && CommonFunctions.isNotBlank(params, "startInfoOrgCode")) {
			params.put("infoOrgCode", params.get("startInfoOrgCode"));
		}
		
		if(CommonFunctions.isNotBlank(params, "evaLevel")) {
			String[] evaLevelArray = params.get("evaLevel").toString().split(",");
			
			if(evaLevelArray.length > 1) {
				params.put("evaLevelArray", evaLevelArray);
				params.remove("evaLevel");
			}
		}
	}
	
	/**
	 * 事件属性格式化输出
	 * @param eventList
	 * @param params
	 */
	private void formatEventDataOut(List<Map<String, Object>> eventList, Map<String, Object> params) {
		if(eventList != null) {
			for(Map<String, Object> eventMap : eventList) {
				if(CommonFunctions.isNotBlank(eventMap, "happenTime")) {
					eventMap.put("happenTimeStr", DateUtils.formatDate((Date)eventMap.get("happenTime"), DateUtils.PATTERN_24TIME));
				}
				
				if(CommonFunctions.isNotBlank(eventMap, "finTime")) {
					eventMap.put("finTimeStr", DateUtils.formatDate((Date)eventMap.get("finTime"), DateUtils.PATTERN_24TIME));
				}
			}
		}
	}
}
