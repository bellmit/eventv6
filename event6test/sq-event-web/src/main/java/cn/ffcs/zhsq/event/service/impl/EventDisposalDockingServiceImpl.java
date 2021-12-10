package cn.ffcs.zhsq.event.service.impl;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import cn.ffcs.cache.redis.service.ManipulateRedisService;
import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.gis.mybatis.domain.base.ResMarker;
import cn.ffcs.resident.bo.PartyIndividual;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IUserService;
import cn.ffcs.shequ.crypto.HashIdManager;
import cn.ffcs.shequ.eventReportRecord.domain.EventReportRecordInfo;
import cn.ffcs.shequ.eventReportRecord.service.IEventReportRecordService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EvaResult;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EventInter;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.event.IEvaResultService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.eventExpand.service.IEventDisposal4ExpandService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.map.coordinateconversion.service.IBaseConversionService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.JsonUtils;

/**
 * 事件对接接口实现
 * @author zhangls
 *
 */
public class EventDisposalDockingServiceImpl implements
		IEventDisposalDockingService {
	// 工作流调用接口
	@Autowired
	private HessianFlowService hessianFlowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	//组织域信息接口
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	//信息域接口
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;
		
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private UserInfoOutService userInfoService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	
	@Autowired
	private IEventReportRecordService eventReportRecordService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private IAttachmentService attachmentService;
	
	//事件评价
	@Autowired
	private IEvaResultService evaResultService;
	
	@Autowired
	private IBaseConversionService baseConversionService;
	
	@Autowired
	private ManipulateRedisService manipulateRedisService;
	
	@Autowired
	private IInvolvedPeopleService involvedPeopleService;
	
	//事件扩展信息接口
    @Autowired
    private IEventDisposal4ExpandService eventDisposalExpandService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private int DS_SJLY = 5;//迪爱斯事件来源
	
	private final String VALIDATE_LENGTH_EVENT_NAME = "eventName";	//数据长度字段验证 事件标题
	private final String VALIDATE_LENGTH_CONTENT = "content";		//数据长度字段验证 事件内容
	private final String VALIDATE_LENGTH_OCCURRED = "occurred";		//数据长度字段验证 发送地点
	private final String VALIDATE_LENGTH_RESULT = "result";			//数据长度字段验证 处理结果
	
	// -- 文件上传目录
	private static final String FILE_UPLOAD_EXTERIOR = "attachment";
	//事件地图标注模块编码
	private static final String EVENT_MODULE_CODE = "EVENT_V1";
	
	@Override
	public List<Map<String, Object>> findClosedEventWithEventInter(Map<String, Object> params){
		params = modifyParams(params);
		
		List<Map<String, Object>> list = eventDisposalService.findClosedEventListWithEventInter(params);
		
		return list;
	}
	
	@Override
	public List<Map<String, Object>> findReportEventWithEventInter(Map<String, Object> params){
		params = modifyParams(params);
		
		List<Map<String, Object>> list = eventDisposalService.findReportEventListWithEventInter(params);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		if(list!=null && list.size()>0){
			Map<String, Object> result = null;
			UserInfo userInfo = null;
			Long creatorId = -1L;
			String eventSrc = "";
			String dictPcode = "";
			String userOrgCode = "";
			String reportUnitId = "";
			
			for(Map<String, Object> map : list){
				result = new HashMap<String, Object>();
				
				creatorId = Long.valueOf(map.get("CREATOR_ID").toString());
				eventSrc = map.get("EVENT_SRC").toString();
				userInfo = userService.getUserExtraInfoByUserId(creatorId, null);
				userOrgCode = userInfo.getOrgCode();
				dictPcode = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DICT_PCODE_TRIGGER+"_"+eventSrc, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
				
				if(StringUtils.isNotBlank(userOrgCode) && userOrgCode.length()>10){
					userOrgCode = userOrgCode.substring(0, 9);
				}
				if(StringUtils.isNotBlank(dictPcode)){
					reportUnitId = baseDictionaryService.changeCodeToName(dictPcode, userOrgCode, userOrgCode);
				}
				
				result.put("SJBH", map.get("EVENT_ID"));		//事件编号
				result.put("SBRY", map.get("CONTACT_USER"));	//上报人
				result.put("SBRDH", map.get("TEL"));			//上报人电话
				result.put("SJMS", map.get("CONTENT_"));		//事件描述
				result.put("SFDZ", map.get("OCCURRED"));		//事发地点
				result.put("SFSJ", map.get("HAPPEN_TIME_STR"));	//事发时间
				result.put("SBSJ", map.get("UPDATE_TIME_STR"));	//上报时间
				result.put("SBDW", reportUnitId);				//上报单位
				result.put("SBLY", DS_SJLY);					//来源
				
				resultList.add(result);
			}
		}
		
		return resultList;
	}
	
	@Override
	public Map<String, Object> saveEventDisposalAndReport(EventDisposal event, UserInfo userInfo, String advice) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(StringUtils.isNotBlank(advice)) {
			params.put("advice", advice);
		}
		
		return this.saveEventDisposalAndReport(event, params, userInfo);
	}
	
	@Override
	public Map<String, Object> saveEventDisposalAndReport(EventDisposal event, Map<String, Object> params, UserInfo userInfo){
		Map<String, Object> resultMap = new HashMap<String, Object>();

		StringBuffer userIds = new StringBuffer("");//获取下一环境办理人
		StringBuffer orgIds = new StringBuffer("");//获取下一环节办理人所属组织id
		String instanceId = "", eventCode = null;
		
		boolean result = false; 
		Long eventId = -1L;
		
		if(event != null){
			eventId = event.getEventId();
		}
		
		if(eventId!=null && eventId>0){
			eventDisposalService.updateEventDisposal(event, params);//编辑事件
		}else if(event!=null && userInfo!=null){
			event = eventDisposalService.defaultEvent(event, userInfo);//设置事件默认值
			eventId = eventDisposalService.saveEventDisposalReturnId(event, params, userInfo);//保存事件
			eventCode = event.getCode();
		}
		
		if(eventId!=null && eventId>0){
			Long workflowId = eventDisposalWorkflowService.capWorkflowId(null, eventId, userInfo, null);
			boolean isReportEvent = true;//是否上报事件
			
			//启动流程
			try {
				instanceId = eventDisposalWorkflowService.startFlowByWorkFlow(eventId, workflowId, ConstantValue.WORKFLOW_IS_NO_CLOSE, userInfo, params);
			} catch (Exception e) {
				resultMap.put("msgWrong", e.getMessage());
				e.printStackTrace();
			}
			
			if(CommonFunctions.isNotBlank(params, "isReportEvent")) {
				isReportEvent = Boolean.valueOf(params.get("isReportEvent").toString());
			}
			
			result = StringUtils.isNotBlank(instanceId);
			
			if(isReportEvent && result) {
				Long instanceIdL = Long.valueOf(instanceId);
				Long taskIdL = eventDisposalWorkflowService.curNodeTaskId(instanceIdL);
				String taskId = taskIdL.toString();
				
				//获取上报环节
				Node reportNode = null;
				
				Map<String, Object> initResultMap = eventDisposalWorkflowService.initFlowInfo(taskId, instanceIdL, userInfo, null);
				if(CommonFunctions.isNotBlank(initResultMap, "taskNodes")) {
					List<Node> taskNodes = (List<Node>) initResultMap.get("taskNodes");
					if(taskNodes != null && taskNodes.size() > 0) {
						for(Node taskNode : taskNodes) {
							if(INodeCodeHandler.OPERATE_REPORT.equals(taskNode.getType()) &&
									"1".equals(taskNode.getLevel())) {
								reportNode = taskNode;
								break;
							}
						}
					}
				}
				
				if(reportNode != null) {//能够上报
					String nextNodeName = "", nodeCode = "";
					List<GdZTreeNode> orgParentList = null;
					Long orgParentId = -1L;
					List<UserBO> users = null;
					List<UserInfo> userInfoList = new ArrayList<UserInfo>(), parentUserInfoList = null;
					Map<String, Object> reportParams = new HashMap<String, Object>();
					
					nodeCode = reportNode.getTransitionCode();
					nextNodeName = reportNode.getNodeName();
					
					try {
						orgParentList = fiveKeyElementService.getTreeForEvent(userInfo, null, nodeCode, null, null, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					reportParams.put("nodeId", reportNode.getNodeId());
					
					for(GdZTreeNode orgParent : orgParentList) {
						if(orgParent != null){
							orgParentId = Long.valueOf(orgParent.getId());
							
							try {
								parentUserInfoList = fiveKeyElementService.getUserInfoList(orgParentId, nodeCode, reportParams);
							} catch (Exception e) {
								if(logger.isErrorEnabled()) {
									logger.error("事件上报，获取父级组织人员信息失败：nodeCode：【】，nodeId：【】", e);
								}
							}
							
							if(parentUserInfoList != null) {
								userInfoList.addAll(parentUserInfoList);
							}
						}
					}
					
					if(parentUserInfoList != null) {
						for(UserInfo parentUserInfo : parentUserInfoList) {
							userIds.append(",").append(parentUserInfo.getUserId());
							orgIds.append(",").append(orgParentId);
						}
					}
					
					if(userIds.length() > 0){
						String advice = "";
						
						userIds = new StringBuffer(userIds.substring(1));
						orgIds = new StringBuffer(orgIds.substring(1));//orgIds个数与userIds个数保持一致，接口需求
						
						if(CommonFunctions.isNotBlank(params, "advice")) {
							advice = params.get("advice").toString();
						} else {
							//获取默认上报办理意见
							advice = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_REPORT_ADVICE_TRIGGER, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
						}
						
						try {
							result = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(instanceIdL, taskId, nextNodeName, userIds.toString(), orgIds.toString(), advice, userInfo, null, params);
						} catch (Exception e) {
							resultMap.put("msgWrong",e.getMessage());
							e.printStackTrace();
						}
					} else {
						resultMap.put("msgWrong", "没有可上报的人员！");
						logger.error("没有可上报的人员！");
					}
				} else {
					resultMap.put("msgWrong", "没有可上报的环节！");
					logger.error("没有可上报的环节！");
				}
				
			}
		}

		resultMap.put("eventId", eventId);//事件ID
		resultMap.put("instanceId", instanceId);//实例ID
		resultMap.put("result", result);//办理结果
		if(StringUtils.isNotBlank(eventCode)) {
			resultMap.put("eventCode", eventCode);
		}
		if(userIds.length() > 0) {
			resultMap.put("userIds", userIds.toString());//下一节点办理人员，转换为字符串是为了方便json串的转换
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> saveEventDisposalAndReport(Map<String, Object> eventMap){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(eventMap != null) {
			boolean isAble2Operate = true;
			
			if(CommonFunctions.isNotBlank(eventMap, "redisKey")) {
				String redisKey = eventMap.get("redisKey").toString();
				String redisValue = manipulateRedisService.valueGet(redisKey, ConstantValue.JEDIS_DB_EVENT_ADDRESS);
				
				if(StringUtils.isNotBlank(redisValue)) {
					isAble2Operate = Boolean.valueOf(redisValue);
				}
			}
			
			if(isAble2Operate) {
				eventMap = this.eventJsonToMap(eventMap);
				resultMap = saveEventAndReport(eventMap);
			} else {
				resultMap.putAll(eventMap);
				resultMap.put("eventId", -1L);
				resultMap.put("result", false);
				resultMap.put("msg", "该记录已上报为事件，无需重复上报！");
			}
		}
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> saveEventDisposalAndReport(String eventJson){
		Map<String, Object> resultMap = null;
		
		if(StringUtils.isNotBlank(eventJson) && StringUtils.isNotBlank(eventJson.trim())){
			Map<String, Object> eventMap = this.eventJsonToMap(eventJson);//将json串转换相应的对象
			resultMap = this.saveEventAndReport(eventMap);
		}
		
		return resultMap;
	}
	
	private Map<String, Object> saveEventAndReport(Map<String, Object> eventMap){
		Map<String, Object> resultMap = null;
		
		if(eventMap!=null && CommonFunctions.isBlank(eventMap, "msg")){
			UserInfo userInfo = null;//办理事件用户信息
			String advice = "";//办理意见，默认为事件上报
			String isSendSms = "";//是否发送短信，默认为true
			Long eventId = -1L;//事件编号
			boolean sendSmsFlag = true;//是否发送短信 由isSendSms转换 默认发送短信
			EventDisposal event = null;//事件对象
			String orgCode = null;
			
			if(CommonFunctions.isNotBlank(eventMap, "event")){//事件对象
				event = (EventDisposal)eventMap.get("event");
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "userInfo")){//用户对象
				userInfo = (UserInfo)eventMap.get("userInfo");
				orgCode = userInfo.getOrgCode();
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "advice")){//获取流程办理意见
				advice = eventMap.get("advice").toString();
			}else{
				advice = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_REPORT_ADVICE_TRIGGER, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "isSendSms")){//是否发送短信，默认发送
				isSendSms = eventMap.get("isSendSms").toString();
				sendSmsFlag = Boolean.parseBoolean(isSendSms);
			}
			
			if(event!=null && userInfo!=null){
				resultMap = this.saveEventDisposalAndReport(event, eventMap, userInfo);//保存并上报事件
			}
			
			if(CommonFunctions.isNotBlank(resultMap, "eventId")){
				eventId = Long.valueOf(resultMap.get("eventId").toString());
			}
			
			if(CommonFunctions.isNotBlank(resultMap, "result") && sendSmsFlag){//上报结果：true为上报成功
				boolean result = Boolean.valueOf(resultMap.get("result").toString());
				if(result && CommonFunctions.isNotBlank(resultMap, "userIds")){//发送短信
					event = eventDisposalService.findEventById(eventId, orgCode);
					if(event != null){
						String userIds = resultMap.get("userIds").toString();//下一环节办理人员
						String smsContent = eventDisposalWorkflowService.capSmsContent(null, ConstantValue.REPORT_NOTE, advice, event, userInfo);//获取短信内容
						if(StringUtils.isNotBlank(userIds) && StringUtils.isNotBlank(smsContent)){
							eventDisposalWorkflowService.sendSms(userIds, null, smsContent, userInfo);//异步发送短信
						}
					}
				}
			}
		}else{
			resultMap = eventMap;
		}
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> saveEventDisposalAndShunt(String eventJson){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(StringUtils.isNotBlank(eventJson) && StringUtils.isNotBlank(eventJson.trim())){
			Map<String, Object> eventMap = this.eventJsonToMap(eventJson);//将json串转换相应的对象
			resultMap = saveEventAndShunt(eventMap);
		}
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> saveEventDisposalAndShunt(Map<String, Object> eventMap){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(eventMap != null){
			eventMap = this.eventJsonToMap(eventMap);
			resultMap = saveEventAndShunt(eventMap);
		}
		
		return resultMap;
	}
	
	private Map<String, Object> saveEventAndShunt(Map<String, Object> eventMap){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long eventId = -1L;//事件编号
		String instanceId = "";//流程实例id
		boolean result = false;//办理结果
		
		if(eventMap!=null && CommonFunctions.isBlank(eventMap, "msg")){
			EventDisposal event = new EventDisposal();//事件对象
			UserInfo userInfo = null;
			boolean sendSmsFlag = true;
			String isSendSms = "";
			String advice = "";
			Long workflowId = -1L;
			String userOrgCode = null;
			
			if(CommonFunctions.isNotBlank(eventMap, "event")){//json转换事件对象
				event = (EventDisposal)eventMap.get("event");
				if(event != null){
					eventId = event.getEventId();
				}
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "userInfo")){//获取用户信息
				userInfo = (UserInfo)eventMap.get("userInfo");
				userOrgCode = userInfo.getOrgCode();
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "isSendSms")){//是否发送短信，默认发送
				isSendSms = eventMap.get("isSendSms").toString();
				sendSmsFlag = Boolean.parseBoolean(isSendSms);
			}
			
			if(eventId==null || eventId<0){//新增事件
				event = eventDisposalService.defaultEvent(event, userInfo);//设置事件默认值
				eventId = eventDisposalService.saveEventDisposalReturnId(event, userInfo);//保存事件
			}
			
			workflowId = eventDisposalWorkflowService.capWorkflowId(null, eventId, userInfo, null);//获取流程图id
			//工作流启动
			try {
				instanceId = eventDisposalWorkflowService.startFlowByWorkFlow(Long.valueOf(eventId), workflowId, ConstantValue.WORKFLOW_IS_NO_CLOSE, userInfo);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(StringUtils.isNotBlank(instanceId)){
				if(CommonFunctions.isNotBlank(eventMap, "advice")){//获取办理意见
					advice = eventMap.get("advice").toString();
				}else{
					//获取默认分流办理意见
					advice = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_SHUNT_ADVICE_TRIGGER, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
				}
				
				StringBuffer orgIds = new StringBuffer("");
				StringBuffer staffIds = new StringBuffer("");
				String nextNodeName = "";
				
				if(CommonFunctions.isNotBlank(eventMap, "nextOrgCode")){//获取办理意见
					String nextOrgCode = eventMap.get("nextOrgCode").toString();
					String[] orgCodes = nextOrgCode.split(",");
					if(orgCodes!=null && orgCodes.length>0){
						Long orgId = -1L;
						OrgSocialInfoBO orgSocialInfo = null;
						
						for(String orgCode : orgCodes){
							orgCode = orgCode.trim();
							if(StringUtils.isNotBlank(orgCode)){
								orgSocialInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(orgCode);
								if(orgSocialInfo != null){
									orgId = orgSocialInfo.getOrgId();
									ProInstance proInstance = hessianFlowService.getProByInstanceId(Long.valueOf(instanceId));//获取流程实例
									String curNodeName = proInstance.getCurNode();//当前节点，如果是开始节点-->[县(区)处理]
									if(curNodeName.startsWith("[")){//去除[
										curNodeName = curNodeName.substring(curNodeName.indexOf("[") + 1);
									}
									if(curNodeName.endsWith("]")){//去除]
										curNodeName = curNodeName.substring(0, curNodeName.lastIndexOf("]"));
									}
									if(eventMap.get("nextNodeName") == null){
										nextNodeName = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, curNodeName, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
									}else{
										nextNodeName = eventMap.get("nextNodeName").toString();
									}
									Map<String, Object> levelAndPostionName = eventDisposalWorkflowService.capTurnLevel(curNodeName, nextNodeName, userInfo);
									String positionName = (String) levelAndPostionName.get("positionName");
									//获取该组织下的职位为positionName的用户
									List<UserBO> users = (ArrayList<UserBO>)userManageService.queryUserListByPositionAndOrg(positionName, null,"0", orgId, null);
									if(users!=null && users.size()>0){
										for(UserBO user : users){
											staffIds.append(user.getUserId()).append(",");
											orgIds.append(orgId).append(",");
										}
									}
								}
							}
						}
						
						if(orgIds.length() > 0){
							orgIds = new StringBuffer(orgIds.substring(0, orgIds.length() - 1));//下一环节办理人员所属组织
						}
						if(staffIds.length() > 0){
							staffIds = new StringBuffer(staffIds.substring(0, staffIds.length() - 1));//下一环节办理人员id
						}
					}
				}
				
				if(staffIds.length() > 0){
					Map<String, Object> initResultMap = eventDisposalWorkflowService.initFlowInfo("", Long.valueOf(instanceId), userInfo, null);
					String taskId = initResultMap.get("taskId").toString();//当前任务id
					try {
						result = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(Long.valueOf(instanceId),taskId, nextNodeName, staffIds.toString(), orgIds.toString(), advice, userInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(result && sendSmsFlag){//分流结果：true为分流成功
						event = eventDisposalService.findEventById(eventId, userOrgCode);
						if(event != null){
							String smsContent = eventDisposalWorkflowService.capSmsContent(null, ConstantValue.SHUNT_NOTE, advice, event, userInfo);
							if(StringUtils.isNotBlank(smsContent)){
								eventDisposalWorkflowService.sendSms(staffIds.toString(), null, smsContent, userInfo);//异步发送短信
							}
						}
					}
				}
				
				if(result || 1==1){
					if(CommonFunctions.isNotBlank(eventMap, "bizPlatform")){//获取对方的关联字段
						String bizPlatform = eventMap.get("bizPlatform").toString();
						
						bizPlatform = baseDictionaryService.changeCodeToName(ConstantValue.BIZ_PLATFORM_PCODE, bizPlatform, userInfo.getOrgCode());
						if(StringUtils.isNotBlank(bizPlatform)){
							
							if(CommonFunctions.isNotBlank(eventMap, "oppoSideBizCode")){//获取对方的关联字段
								String oppoSideBizCode = eventMap.get("oppoSideBizCode").toString();
								
								if(StringUtils.isNotBlank(oppoSideBizCode)){
									dataExchangeStatusService.saveDataExchangeStatusForEventNew(bizPlatform, null, oppoSideBizCode, String.valueOf(eventId));
								}
							}
						}
					}
				}
			}
				
		}else if(eventMap != null){
			resultMap = eventMap;
		}
		
		resultMap.put("eventId", eventId);//事件编号
		resultMap.put("instanceId", instanceId);//流程实例id
		resultMap.put("result", result);//办理结果
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> saveEventDisposalAndClose(String eventJson){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(StringUtils.isNotBlank(eventJson) && StringUtils.isNotBlank(eventJson.trim())){
			Map<String, Object> eventMap = this.eventJsonToMap(eventJson);
			resultMap = saveEventAndClose(eventMap);
		}
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> saveEventDisposalAndClose(Map<String, Object> eventMap){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(eventMap != null){
			eventMap = this.eventJsonToMap(eventMap);
			resultMap = saveEventAndClose(eventMap);
		}
		
		return resultMap;
	}
	
	private Map<String, Object> saveEventAndClose(Map<String, Object> eventMap){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long eventId = -1L;//事件编号
		String instanceId = "";//工作流实例ID
		boolean result = false;//办理结果
		StringBuffer msg = new StringBuffer("");//错误信息
		
		if(eventMap!=null && CommonFunctions.isBlank(eventMap, "msg")){
			UserInfo userInfo = null;//办理事件用户信息
			EventDisposal event = null;//事件对象
			String advice = "";
			
			if(CommonFunctions.isNotBlank(eventMap, "event")){//json转换事件对象
				event = (EventDisposal)eventMap.get("event");
				if(event != null){
					eventId = event.getEventId();
				}
			}
			
			
			if(CommonFunctions.isNotBlank(eventMap, "userInfo")){//json转换用户对象
				userInfo = (UserInfo)eventMap.get("userInfo");
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "advice")) {
				advice = eventMap.get("advice").toString();
			}
			
			if(eventId!=null && eventId>0){
				//eventDisposalService.updateEventDisposal(event);//编辑事件
				
				instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(eventId);
				
				Map<String, Object> initResultMap = eventDisposalWorkflowService.initFlowInfo("", Long.valueOf(instanceId), userInfo, null);
				List<Node> taskNodes = (List<Node>) initResultMap.get("taskNodes");
				String taskId = initResultMap.get("taskId").toString(),
					   closeNodeName = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.CLOSE_NODE_NAME_TRIGGER, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0),
					   closeNodeNameZH = "";
				
				if(taskNodes!=null && taskNodes.size()>0){//下一环节中，有结案环节时，才能结案
					boolean isCloseAble = false;
					
					for(Node taskNode : taskNodes){
						if(closeNodeName.equals(taskNode.getNodeNameZH())){
							closeNodeNameZH = taskNode.getNodeNameZH();
							isCloseAble = true;
							break;
						}
					}
					
					if(isCloseAble){//可结案时，事件才能进行结案操作
						if(CommonFunctions.isNotBlank(eventMap, "bizPlatform")){//第三方结案对接，直接结案并归档
							eventMap.put("instanceId", instanceId);
							eventMap.put("taskId", taskId);
							result = eventDisposalWorkflowService.archiveAndEndWorkflowForEvent(eventMap);
						}else{
							//人员为当前办理人员时，才可继续办理事件
							boolean isCurrentUser = false;
							
							try {
								isCurrentUser = eventDisposalWorkflowService.isUserInfoCurrentUser(taskId, instanceId, userInfo);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							if(isCurrentUser){
								boolean isSendSms = true;//是否发送短信，默认为true
								
								try {
									result = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(Long.valueOf(instanceId),taskId, closeNodeName, "", "", advice, userInfo, null, null);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								if(result){
									if(CommonFunctions.isNotBlank(eventMap, "isSendSms")){//是否发送短信
										isSendSms = Boolean.valueOf(eventMap.get("isSendSms").toString());
									}
									
									if(isSendSms){//在结案成功后，并可发送短信时
										String smsContent = eventDisposalWorkflowService.capSmsContent(taskId, ConstantValue.CLOSE_NOTE, advice, event, userInfo);
										eventDisposalWorkflowService.sendSms("", "", closeNodeNameZH, Long.valueOf(instanceId), smsContent, userInfo);
									}
									
									if(eventId!=null && eventId>0){//将办理意见更新至事件result
										EventDisposal eventTmp = new EventDisposal();
										eventTmp.setEventId(eventId);
										eventTmp.setResult(advice);
										eventDisposalService.updateEventDisposalById(eventTmp);
									}
								}
								
							}else{
								msg.append("归属组织为"+userInfo.getOrgName()+"的用户"+userInfo.getPartyName()+" 不是事件"+eventId+"的当前办理人！");
							}
						}
					}else{
						msg.append("事件："+eventId+"没有结案环节，不能结案！");
					}
				}
			}else{
				event = eventDisposalService.defaultEvent(event, userInfo);//设置事件默认值
				event.setResult(advice);
				eventId = eventDisposalService.saveEventDisposalReturnId(event, userInfo);//保存事件
				
				if (CommonFunctions.isNotBlank(eventMap, "peopleListJson")) {
					String peopleListJson = (String) eventMap.get("peopleListJson");
					List<InvolvedPeople> list = JSON.parseArray(peopleListJson, InvolvedPeople.class);

					//设置event.involvedPersion属性，涉事人员姓名，用","隔开
					StringBuffer involvedPersion = new StringBuffer();
					for (InvolvedPeople involvedPeople : list) {
						involvedPeople.setName(involvedPeople.getName().trim());
						involvedPeople.setBizId(eventId);
						involvedPeople.setBizType(InvolvedPeople.BIZ_TYPE.ZHSQ_EVENT.getBizType());

						//设置event.involvedPersion属性，涉事人员姓名，用","隔开
						involvedPersion.append(",").append(involvedPeople.getName());

						try {
							involvedPeopleService.insertInvolvedPeople(involvedPeople);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
				
				Long workflowId = eventDisposalWorkflowService.capWorkflowId(null, eventId, userInfo, null);
				if(workflowId!=null && workflowId>0){
					Map<String, Object> extraParam = new HashMap<String, Object>();
					extraParam.put("advice", advice);
					
					try {
						instanceId = eventDisposalWorkflowService.startFlowByWorkFlow(eventId, workflowId, ConstantValue.WORKFLOW_IS_TO_COLSE, userInfo, extraParam);
					} catch (Exception e) {
						resultMap.put("msg", e.getMessage());
						e.printStackTrace();
					}

					result = StringUtils.isNotBlank(instanceId);

				}
			}
			
			if(result){
				if(CommonFunctions.isNotBlank(eventMap, "bizPlatform")){//获取对方的关联字段
					String bizPlatform = eventMap.get("bizPlatform").toString();
					
					bizPlatform = baseDictionaryService.changeCodeToName(ConstantValue.BIZ_PLATFORM_PCODE, bizPlatform, userInfo.getOrgCode());
					if(StringUtils.isNotBlank(bizPlatform)){
						
						if(CommonFunctions.isNotBlank(eventMap, "oppoSideBizCode")){//获取对方的关联字段
							String oppoSideBizCode = eventMap.get("oppoSideBizCode").toString();
							
							if(StringUtils.isNotBlank(oppoSideBizCode)){
								dataExchangeStatusService.saveDataExchangeStatusForEventNew(bizPlatform, null, oppoSideBizCode, String.valueOf(eventId));
							}
						}
					}
				}
			}
		}else if(eventMap != null){
			resultMap = eventMap;
		}
		
		resultMap.put("eventId", eventId);//事件ID
		resultMap.put("instanceId", instanceId);//实例ID
		resultMap.put("result", result);//办理结果
		
		if(msg.length() > 0){
			resultMap.put("msg", msg.toString());
		}
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> saveEventDisposalAndEvaluate(EventDisposal event, UserInfo userInfo, String advice){
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Long eventId = -1L;//事件编号
		Long workflowId = -1L;
		String instanceId = "";//工作流实例ID
		boolean result = false;//办理结果
		
		event = eventDisposalService.defaultEvent(event, userInfo);//设置事件默认值
		eventId = eventDisposalService.saveEventDisposalReturnId(event, userInfo);//保存事件
		
		workflowId = eventDisposalWorkflowService.capWorkflowId(null, eventId, userInfo, null);
		
		if(workflowId!=null && workflowId>0 && eventId!=null && eventId>0){
			try {
				instanceId = eventDisposalWorkflowService.startEndWorkflowForEvent(eventId, workflowId, userInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			result = StringUtils.isNotBlank(instanceId);
		}
					
		resultMap.put("eventId", eventId);//事件ID
		resultMap.put("instanceId", instanceId);//实例ID
		resultMap.put("result", result);//办理结果
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> saveEventDisposalAndEvaluate(String eventJson){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long eventId = -1L;
		String instanceId = "";//工作流实例ID
		boolean result = false;//办理结果
		
		if(StringUtils.isNotBlank(eventJson) && StringUtils.isNotBlank(eventJson.trim())){
			Map<String, Object> eventMap = this.eventJsonToMap(eventJson);
			UserInfo userInfo = null;//办理事件用户信息
			EventDisposal event = null;//事件对象
			Long workflowId = -1L;
			
			if(eventMap!=null && CommonFunctions.isBlank(eventMap, "msg")){//json串有效时，该map不为空 
				if(CommonFunctions.isNotBlank(eventMap, "event")){//json转换事件对象
					event = (EventDisposal)eventMap.get("event");
				}
				
				if(CommonFunctions.isNotBlank(eventMap, "userInfo")){//json转换用户对象
					userInfo = (UserInfo)eventMap.get("userInfo");
				}
				
				if(event != null){
					event = eventDisposalService.defaultEvent(event, userInfo);//设置事件默认值
					eventId = eventDisposalService.saveEventDisposalReturnId(event, userInfo);//保存事件
				}
				
				if(eventId!=null && eventId>0){
					workflowId = eventDisposalWorkflowService.capWorkflowId(null, eventId, userInfo, null);
					
					if(workflowId!=null && workflowId>0){
						eventMap.put("eventId", eventId);
						eventMap.put("workFlowId", workflowId);
						try {
							instanceId = eventDisposalWorkflowService.startEndWorkflowForEvent(eventMap);
						} catch (Exception e) {
							e.printStackTrace();
						}
						result = StringUtils.isNotBlank(instanceId);
					}
				}
			}else{
				resultMap = eventMap;
			}
		}
		
		resultMap.put("eventId", eventId);//事件ID
		resultMap.put("instanceId", instanceId);//实例ID
		resultMap.put("result", result);//办理结果
		return resultMap;
	}
	
	@Override
	public Map<String, Object> rejectWorkFlow(String eventJson){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(StringUtils.isNotBlank(eventJson) && StringUtils.isNotBlank(eventJson.trim())){
			Map<String, Object> eventMap = this.eventJsonToMap(eventJson);//将json串转换相应的对象
			resultMap = rejectWorkflow(eventMap);
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> rejectWorkFlow(Map<String, Object> eventMap){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(eventMap != null){
			eventMap = this.eventJsonToMap(eventMap);
			resultMap = rejectWorkflow(eventMap);
		}
		
		return resultMap;
	}
	
	private Map<String, Object> rejectWorkflow(Map<String, Object> eventMap){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		EventDisposal event = null;
		UserInfo userInfo = null;//办理事件用户信息
		Long eventId = -1L;
		String taskId = "";//当前任务id
		Long taskIdL = -1L;
		String advice = "";//办理意见
		Long instanceIdL = -1L;
		String instanceId = "";//流程实例id
		boolean result = false;//办理结果
		StringBuffer msgWrong = new StringBuffer("");
		String orgCode = null;
		
		if(eventMap!=null && CommonFunctions.isBlank(eventMap, "msg")){
			if(CommonFunctions.isNotBlank(eventMap, "userInfo")){//json转换用户对象
				userInfo = (UserInfo)eventMap.get("userInfo");
				orgCode = userInfo.getOrgCode();
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "eventId")){//获取事件id
				eventId = Long.valueOf(eventMap.get("eventId").toString());
				if(eventId > 0){
					event = eventDisposalService.findEventByIdSimple(eventId);
					instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(eventId);
				}
			}
			
			if(event != null){
				String eventStatus = event.getStatus();
				if(ConstantValue.EVENT_STATUS_RECEIVED.equals(eventStatus) || ConstantValue.EVENT_STATUS_REPORT.equals(eventStatus) || ConstantValue.EVENT_STATUS_DISTRIBUTE.equals(eventStatus) || ConstantValue.EVENT_STATUS_ARCHIVE.equals(eventStatus)){
					//可进行驳回操作的有效事件状态
				}else{
					msgWrong.append("当前事件[").append(eventId).append("]的状态为：[").append(event.getStatusName()).append("]，其不能进行驳回操作！");
				}
			}
			
			if(StringUtils.isNotBlank(instanceId)){
				try{
					instanceIdL = Long.valueOf(instanceId);
				}catch(NumberFormatException e){
					msgWrong.append("当前工作流实例编码[").append(instanceId).append("]不能转换为Long型数据！");
					e.printStackTrace();
				}
			}
			
			if(StringUtils.isBlank(instanceId) || instanceIdL<0){
				msgWrong.append("当前事件[").append(eventId).append("]没有对应的工作流实例编码！");
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "taskId")){//获取当前任务id
				taskId = eventMap.get("taskId").toString();
			}else if(StringUtils.isNotBlank(instanceId)){
				taskId = String.valueOf(eventDisposalWorkflowService.curNodeTaskId(instanceIdL));
			}
			
			if(StringUtils.isNotBlank(taskId)){
				try{
					taskIdL = Long.valueOf(taskId);
				}catch(NumberFormatException e){
					msgWrong.append("当前任务编码[").append(taskId).append("]不能转换为Long型数据！");
					e.printStackTrace();
				}
			}
			
			if(StringUtils.isBlank(taskId) || taskIdL<0){
				msgWrong.append("当前事件[").append(eventId).append("]没有未完成的任务！");
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "advice")){//驳回办理意见
				advice = eventMap.get("advice").toString();
			}
			
			if(msgWrong.length()==0 && userInfo!=null){
				boolean isAbleReject = true;
				
				Map<String, Object> initMap = eventDisposalWorkflowService.initFlowInfo(taskId, Long.valueOf(instanceId), userInfo, null);
				String nodeName = "";
				
				if(CommonFunctions.isNotBlank(initMap, "taskNodes")){
					List<Node> taskNodes = (List<Node>) initMap.get("taskNodes");
					if(taskNodes!=null && taskNodes.size()==1){
						Node node = taskNodes.get(0);
						isAbleReject = StringUtils.isNotBlank(node.getTransitionCode());
					}
				}
				
				if(isAbleReject){
					try {
						result = eventDisposalWorkflowService.rejectWorkFlow(taskId, instanceId, advice);
					} catch (Exception e) {
						msgWrong.append(e.getMessage());
						e.printStackTrace();
					}
				}else{
					if(CommonFunctions.isNotBlank(initMap, "curNode")){
						Node curNode = (Node) initMap.get("curNode");
						nodeName = curNode.getNodeName();
					}
					
					msgWrong.append("事件[").append(eventId).append("]的当前步骤[").append(nodeName).append("]不能进行驳回操作！");
				}
			}
			
			if(msgWrong.length() > 0){
				resultMap.put("msg", msgWrong.toString());
			}
		}else{
			resultMap = eventMap;
		}
		
		resultMap.put("result", result);//驳回操作结果
		return resultMap;
	}
	
	@Override
	public boolean rejectWorkFlow(Map<String, Object> params, UserInfo userInfo) {
		boolean result = false;
		
		if(userInfo == null && CommonFunctions.isNotBlank(params, "userInfo")) {
			userInfo = (UserInfo)params.get("userInfo");
		}
		
		if(CommonFunctions.isBlank(params, "advice") && CommonFunctions.isNotBlank(params, "evaContent")) {
			params.put("advice", params.get("evaContent"));
		}
		
		if(userInfo == null || (userInfo.getUserId() == null && userInfo.getOrgId() == null)) {//为有设置当前办理人员信息时，将其指定为当前任务的可办理人员中的一个
			Long eventId = -1L, instanceId = -1L, curTaskId = -1L;
			userInfo = new UserInfo();//防止userInfo为null的情况
			
			if(CommonFunctions.isNotBlank(params, "eventId")) {
				try {
					eventId = Long.valueOf(params.get("eventId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				try {
					instanceId = Long.valueOf(params.get("instanceId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			if(CommonFunctions.isNotBlank(params, "curTaskId")) {
				try {
					curTaskId = Long.valueOf(params.get("curTaskId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			if(instanceId < 0 && eventId > 0) {
				instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
				params.put("instanceId", instanceId);
			}
			if(curTaskId < 0 && instanceId > 0) {
				curTaskId = eventDisposalWorkflowService.curNodeTaskId(instanceId);
				params.put("curTaskId", curTaskId);
			}
			
			if(curTaskId > 0) {
				List<Map<String, Object>> taskPerson = eventDisposalWorkflowService.queryMyTaskParticipation(curTaskId.toString());
				if(taskPerson.size() > 0) {
					Map<String, Object> taskPersonMap = taskPerson.get(0);
					
					if(CommonFunctions.isNotBlank(taskPersonMap, "USER_NAME")) {//有用户名称，证明用户id有效
						if(CommonFunctions.isNotBlank(taskPersonMap, "USER_ID")) {
							userInfo.setUserId(Long.valueOf(taskPersonMap.get("USER_ID").toString()));
						}
						userInfo.setPartyName(taskPersonMap.get("USER_NAME").toString());
					}
					if(CommonFunctions.isNotBlank(taskPersonMap, "ORG_NAME")) {//有组织名称，证明组织id有效
						if(CommonFunctions.isNotBlank(taskPersonMap, "ORG_ID")) {
							Long orgId = Long.valueOf(taskPersonMap.get("ORG_ID").toString());
							
							OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
							if(orgInfo != null) {
								userInfo.setOrgId(orgId);
								userInfo.setOrgName(orgInfo.getOrgName());
								userInfo.setOrgCode(orgInfo.getOrgCode());
							}
						}
					}
				}
			}
		}
		
		try {
			result = eventDisposalWorkflowService.rejectWorkFlow(params, userInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> feedBackEventInter(String eventJson){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String ownSideEventCode = "";//事件编号
		String oppoSideBusiCode = "";
		String eventSrc = "";
		StringBuffer msg = new StringBuffer("");
		boolean result = false;//办理结果
		
		if(StringUtils.isNotBlank(eventJson) && StringUtils.isNotBlank(eventJson.trim())){
			Map<String, Object> eventMap = this.eventJsonToMap(eventJson);//将json串转换相应的对象
			EventInter inter = new EventInter();
			
			if(eventMap!=null && CommonFunctions.isBlank(eventMap, "msg")){
				if(CommonFunctions.isNotBlank(eventMap, "eventId")){//事件编号
					ownSideEventCode = eventMap.get("eventId").toString();
				}
				if(CommonFunctions.isNotBlank(eventMap, "oppoSideBusiCode")){//接入方业务编号
					oppoSideBusiCode = eventMap.get("oppoSideBusiCode").toString();
				}
				if(CommonFunctions.isNotBlank(eventMap, "eventSrc")){//接入方事件来源
					eventSrc = eventMap.get("eventSrc").toString();
				}
				
				if(StringUtils.isNotBlank(ownSideEventCode)){
					inter.setOwnSideEventCode(ownSideEventCode);
				}else{
					msg.append("缺少参数[eventId]，请检查！");
				}
				
				if(StringUtils.isNotBlank(oppoSideBusiCode)){
					inter.setOppoSideBusiCode(oppoSideBusiCode);
				}else{
					msg.append("缺少参数[oppoSideBusiCode]，请检查！");
				}
				
				if(StringUtils.isNotBlank(eventSrc)){
					inter.setEventSrc(eventSrc);
				}else{
					msg.append("缺少参数[eventSrc]，请检查！");
				}
				
				if(msg.length() > 0){
					result = false;
					resultMap.put("msg", msg.toString());
				}else{
					inter.setRecStatus("1");
					result = eventDisposalService.updateEventInter(inter);
					if(!result){
						result = eventDisposalService.updateEventInterOppoSideBusiCode(inter);
					}
				}
			}else{
				resultMap = eventMap;
			}
		}
		
		resultMap.put("result", result);
		return resultMap;
	}
	
	@Override
	public EventDisposal jsonToEvent(String eventJson) {
		EventDisposal event = new EventDisposal();
		if(!StringUtils.isBlank(eventJson) && !StringUtils.isBlank(eventJson.trim())){
			Map<String, Object> eventMap = null;
			try{
				eventMap = JsonUtils.json2Map(eventJson);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(eventMap != null){
				event = this.jsonToEvent(eventMap);
			}
		}
		
		return event;
	}
	
	@Override
	public EventDisposal jsonToEvent(Map<String, Object> eventMap) {
		EventDisposal event = new EventDisposal();
		String orgCode = null;
		
		if(eventMap != null){
			Long eventId = -1L;
			StringBuffer msg = new StringBuffer("");
			String DEFAULT_EVENT_SEQ = "1";
			boolean isAble2Operate = true;
			
			if(CommonFunctions.isNotBlank(eventMap, "redisKey")) {
				String redisKey = eventMap.get("redisKey").toString();
				String redisValue = manipulateRedisService.valueGet(redisKey, ConstantValue.JEDIS_DB_EVENT_ADDRESS);
				
				if(StringUtils.isNotBlank(redisValue)) {
					isAble2Operate = Boolean.valueOf(redisValue);
				}
			}
			
			if(!isAble2Operate) {
				eventMap.put("msg", "该记录已上报为事件，无需重复上报！");
				return new EventDisposal();
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "orgCode")) {
				orgCode = eventMap.get("orgCode").toString();
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "event")){//优先使用传递的事件对象中是属性
				event = (EventDisposal)eventMap.get("event");
			}else if(CommonFunctions.isNotBlank(eventMap, "eventId")){
				try{
					eventId = Long.valueOf(eventMap.get("eventId").toString());
				}catch(NumberFormatException e){
					eventId = -1L;
					eventMap.remove("eventId");
					msg.append(("参数[eventId]："+eventId+" 不能转换为Long型数据，请检查！"));
				}
				
				event = eventDisposalService.findEventById(eventId, orgCode);
				
				if(event == null){//当eventId无效时，则删除eventId
					eventMap.remove("eventId");
					msg.append("参数[eventId]："+eventId+" 没有对应的有效事件，请检查！");
				}
			}
			
			//事件属性优先使用json中传递过来的
			if(CommonFunctions.isNotBlank(eventMap, "type")){
				String type = eventMap.get("type").toString();
				event.setType(type);
			}
			if(CommonFunctions.isNotBlank(eventMap, "eventName")){
				String eventName = eventMap.get("eventName").toString();
				event.setEventName(eventName);
			}
			if(CommonFunctions.isNotBlank(eventMap, "content")){
				String content = eventMap.get("content").toString();
				event.setContent(content);
			}else if(StringUtils.isNotBlank(event.getContent())){//设置事件的事件内容，防止字段验证时受阻
				eventMap.put("content", event.getContent());
			}
			if(CommonFunctions.isNotBlank(eventMap, "occurred")){
				String occurred = eventMap.get("occurred").toString();
				event.setOccurred(occurred);
			}else if(StringUtils.isNotBlank(event.getOccurred())){//设置事件的事发地址，防止字段验证时受阻
				eventMap.put("occurred", event.getOccurred());
			}
			if(CommonFunctions.isNotBlank(eventMap, "happenTimeStr")){
				String happenTimeStr = eventMap.get("happenTimeStr").toString();
				Date happendTime = null;
				
				try {
					happendTime = DateUtils.convertStringToDate(happenTimeStr, DateUtils.PATTERN_24TIME);
				} catch (ParseException e) {
					happendTime = null;
					msg.append("参数[happenTimeStr]：").append(happenTimeStr).append(" 不是有效的时间参数，请检查！");
					e.printStackTrace();
				}
				if(happendTime != null){
					event.setHappenTimeStr(happenTimeStr);
					event.setHappenTime(happendTime);
				}
			}else{
				event.setHappenTimeStr(DateUtils.getNow());
			}
			if(CommonFunctions.isNotBlank(eventMap, "handleDateStr")){
				String handleDateStr = eventMap.get("handleDateStr").toString();
				Date handleDate = null;
				
				try {
					handleDate = DateUtils.convertStringToDate(handleDateStr, DateUtils.PATTERN_24TIME);
				} catch (ParseException e) {
					handleDate = null;
					msg.append("参数[handleDateStr]：").append(handleDateStr).append(" 不是有效的时间参数，请检查！");
					e.printStackTrace();
				}
				if(handleDate != null){
					event.setHandleDateStr(handleDateStr);
					event.setHandleDate(handleDate);
				}
			}
			if(CommonFunctions.isNotBlank(eventMap, "tel")){
				String tel = eventMap.get("tel").toString();
				event.setTel(tel);
			}
			if(CommonFunctions.isNotBlank(eventMap, "contactUser")){
				String contactUser = eventMap.get("contactUser").toString();
				event.setContactUser(contactUser);
			}
			if(CommonFunctions.isNotBlank(eventMap, "gridCode")){
				String gridCode = eventMap.get("gridCode").toString();
				event.setGridCode(gridCode);
			}else if(eventId > 0){
				eventMap.put("gridCode", event.getGridCode());
			}
			if(CommonFunctions.isNotBlank(eventMap, "gridId")){
				Long gridId = Long.valueOf(eventMap.get("gridId").toString());
				event.setGridId(gridId);
			}else if(eventId > 0){
				eventMap.put("gridId", event.getGridId());
			}
			if(CommonFunctions.isNotBlank(eventMap, "gridName")){
				String gridName = eventMap.get("gridName").toString();
				event.setGridName(gridName);
			}else if(eventId > 0){
				eventMap.put("gridName", event.getGridName());
			}
			if(CommonFunctions.isNotBlank(eventMap, "source")){
				String source = eventMap.get("source").toString();
				event.setSource(source);
			}
			if(CommonFunctions.isNotBlank(eventMap, "collectWay")){
				String collectWay = eventMap.get("collectWay").toString();
				event.setCollectWay(collectWay);
			}else{
				event.setCollectWay(ConstantValue.COLLECT_WAY_PC);
			}
			if(CommonFunctions.isNotBlank(eventMap, "involvedNum")){
				String involvedNum = eventMap.get("involvedNum").toString();
				event.setInvolvedNum(involvedNum);
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "involvedNumInt")){
				Long involvedNumInt = 0L;
				
				try {
					involvedNumInt = Long.valueOf(eventMap.get("involvedNumInt").toString());
				} catch(NumberFormatException e) {}
				
				event.setInvolvedNumInt(involvedNumInt);
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "involvedPersion")){
				String involvedPersion = eventMap.get("involvedPersion").toString();
				event.setInvolvedPersion(involvedPersion);
			}
			if(CommonFunctions.isNotBlank(eventMap, "eventInvolvedPeople")){//数据格式为：证件类型，姓名1，身份证号1；证件类型，姓名2，身份证号2；
				String eventInvolvedPeople = eventMap.get("eventInvolvedPeople").toString();
				event.setEventInvolvedPeople(eventInvolvedPeople);
			}
			if(CommonFunctions.isNotBlank(eventMap, "influenceDegree")){
				String influenceDegree = eventMap.get("influenceDegree").toString();
				event.setInfluenceDegree(influenceDegree);
			}
			if(CommonFunctions.isNotBlank(eventMap, "urgencyDegree")){
				String urgencyDegree = eventMap.get("urgencyDegree").toString();
				event.setUrgencyDegree(urgencyDegree);
			}
			if(CommonFunctions.isNotBlank(eventMap, "status")){
				String status = eventMap.get("status").toString();
				event.setStatus(status);
			}else{
				event.setStatus(ConstantValue.EVENT_STATUS_DRAFT);
			}
			if(CommonFunctions.isNotBlank(eventMap, "result")){//办理结果
				String result = eventMap.get("result").toString();
				event.setResult(result);
			}
			if(CommonFunctions.isNotBlank(eventMap, "remark")){
				String remark = eventMap.get("remark").toString();
				event.setRemark(remark);
			}
			if(CommonFunctions.isNotBlank(eventMap, "updateTimeStr")){
				String updateTimeStr = eventMap.get("updateTimeStr").toString();
				Date updateTime = null;
				
				try {
					updateTime = DateUtils.convertStringToDate(updateTimeStr, DateUtils.PATTERN_24TIME);
				} catch (ParseException e) {
					updateTime = null;
					msg.append("参数[updateTimeStr]：").append(updateTimeStr).append(" 不是有效的时间参数，请检查！");
					e.printStackTrace();
				}
				if(updateTime != null){
					event.setUpdateTime(updateTime);
				}
			}
			if(CommonFunctions.isNotBlank(eventMap, "creatorId")){
				Long creatorId = -1L;
				String creatorIdStr = eventMap.get("creatorId").toString();
				try{
					creatorId = Long.valueOf(creatorIdStr);
				}catch(NumberFormatException e){
					creatorId = -1L;
					msg.append("参数[creatorId]：").append(creatorIdStr).append(" 不能转换为Long型数据！");
				}
				if(creatorId > 0){
					UserBO createUser = userManageService.getUserInfoByUserId(creatorId);
					if(createUser != null){
						event.setCreatorId(creatorId);
						event.setCreatorName(createUser.getPartyName());
					}else{
						msg.append("参数[creatorId]：").append(creatorIdStr).append(" 没有对应的用户信息，请检查！");
					}
				}
			}
			if(CommonFunctions.isNotBlank(eventMap, "updatorId")){
				Long updatorId = -1L;
				String updatorIdStr = eventMap.get("updatorId").toString();
				try{
					updatorId = Long.valueOf(updatorIdStr);
				}catch(NumberFormatException e){
					updatorId = -1L;
					msg.append("参数[updatorId]：").append(updatorIdStr).append(" 不能转换为Long型数据！");
				}
				if(updatorId > 0){
					if(event.getCreatorId()!=null &&event.getCreatorId()>0 && updatorId == event.getCreatorId()) {
						event.setUpdatorId(updatorId);
					}else {
						UserBO createUser = userManageService.getUserInfoByUserId(updatorId);
						if(createUser != null){
							event.setUpdatorId(updatorId);
						}else{
							msg.append("参数[updatorId]：").append(updatorIdStr).append(" 没有对应的用户信息，请检查！");
						}
					}
				}
			}
			if(CommonFunctions.isNotBlank(eventMap, "createTimeStr")){
				String createTimeStr = eventMap.get("createTimeStr").toString();
				Date createTime = null;
				
				try {
					createTime = DateUtils.convertStringToDate(createTimeStr, DateUtils.PATTERN_24TIME);
				} catch (ParseException e) {
					createTime = null;
					msg.append("参数[createTimeStr]：").append(createTimeStr).append(" 不是有效的时间参数，请检查！");
					e.printStackTrace();
				}
				if(createTime != null){
					event.setCreateTime(createTime);
					event.setCreateTimeStr(createTimeStr);
				}
			}
			if(CommonFunctions.isNotBlank(eventMap, "finTimeStr")){
				String finTimeStr = eventMap.get("finTimeStr").toString();
				Date finTime = null;
				
				try {
					finTime = DateUtils.convertStringToDate(finTimeStr, DateUtils.PATTERN_24TIME);
				} catch (ParseException e) {
					finTime = null;
					msg.append("参数[finTimeStr]：").append(finTimeStr).append(" 不是有效的时间参数，请检查！");
					e.printStackTrace();
				}
				if(finTime != null){
					event.setFinTime(finTime);
					event.setFinTimeStr(finTimeStr);
				}
			}
			if(CommonFunctions.isNotBlank(eventMap, "bizPlatform")){
				String bizPlatform = eventMap.get("bizPlatform").toString();
				event.setBizPlatform(bizPlatform);
			}else if(StringUtils.isNotBlank(event.getBizPlatform())){//设置事件对接平台，防止字段有效性验证受阻
				eventMap.put("bizPlatform", event.getBizPlatform());
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "eventReportRecordInfo")){
				String eventReportRecordInfoStr = eventMap.get("eventReportRecordInfo").toString();
				Map<String, Object> reportRecordMap = null;
				try{
					reportRecordMap = JsonUtils.json2Map(eventReportRecordInfoStr);
				}catch(Exception e){
					e.printStackTrace();
				}
				if(reportRecordMap != null){
					EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();
					Long bizId = -1L;
					boolean isDuplicatedCheck = false;
					
					if(CommonFunctions.isNotBlank(reportRecordMap, "eventId")){
						String eventIdForReportStr = reportRecordMap.get("eventId").toString();
						Long eventIdForReport = -1L;
						try{
							eventIdForReport = Long.valueOf(eventIdForReportStr);
						}catch(NumberFormatException e){
							eventIdForReport = -1L;
							msg.append("参数[eventReportRecordInfo->eventId]：").append(eventIdForReportStr).append(" 不能转换为Long型数据！");
						}
						if(eventIdForReport > 0){
							eventReportRecordInfo.setEventId(eventIdForReport);
						}
					}
					if(CommonFunctions.isNotBlank(reportRecordMap, "bizType")){
						String bizType = reportRecordMap.get("bizType").toString();
						eventReportRecordInfo.setBizType(bizType);
					}
					
					if(CommonFunctions.isNotBlank(reportRecordMap, "bizHashId") 
							&& CommonFunctions.isBlank(reportRecordMap, "bizId")) {
						reportRecordMap.put("bizId", HashIdManager.encrypt(reportRecordMap.get("bizHashId")));
					}
					
					if(CommonFunctions.isNotBlank(reportRecordMap, "bizId")){
						String bizIdStr = reportRecordMap.get("bizId").toString();
						try{
							bizId = Long.valueOf(bizIdStr);
						}catch(NumberFormatException e){
							bizId = -1L;
							msg.append("参数[eventReportRecordInfo->bizId]：").append(bizIdStr).append(" 不能转换为Long型数据！");
						}
						if(bizId > 0){
							eventReportRecordInfo.setBizId(bizId);
						}
					}else{
						msg.append("缺少业务编码参数，请检查[eventReportRecordInfo->bizId]！");
					}
					
					if(CommonFunctions.isNotBlank(reportRecordMap, "isDuplicatedCheck")) {
						isDuplicatedCheck = Boolean.valueOf(reportRecordMap.get("isDuplicatedCheck").toString());
					}
					
					if(isDuplicatedCheck) {
						int duplictedCount = eventReportRecordService.findCount4EventReportRecordInfo(eventReportRecordInfo);
						
						if(duplictedCount > 0) {
							msg.append("该记录已上报事件，无需重复上报！");
						}
					}
					
					if(CommonFunctions.isNotBlank(reportRecordMap, "procStatus")){
						String procStatus = reportRecordMap.get("procStatus").toString();
						eventReportRecordInfo.setProcStatus(procStatus);
					}
					if(CommonFunctions.isNotBlank(reportRecordMap, "serviceName")){
						String serviceName = reportRecordMap.get("serviceName").toString();
						eventReportRecordInfo.setServiceName(serviceName);
					}
					
					if(msg.length()==0 && bizId>0){
						event.setEventReportRecordInfo(eventReportRecordInfo);
					}
				}
			}
			//设置标注信息
			if(CommonFunctions.isNotBlank(eventMap, "resMarker")) {
				String resMarkerStr = eventMap.get("resMarker").toString();
				Map<String, Object> resMarkerMap = JsonUtils.json2Map(resMarkerStr);
				if(resMarkerMap != null && !resMarkerMap.isEmpty()) {
					ResMarker resMarker = new ResMarker();
					String longitude = "", latitude = "";
					
					if(CommonFunctions.isNotBlank(resMarkerMap, "x")) {
						longitude = resMarkerMap.get("x").toString();
						resMarker.setX(longitude);
					} else {
						msg.append("缺少经度参数，请检查[resMarker->x]！");
					}
					if(CommonFunctions.isNotBlank(resMarkerMap, "y")) {
						latitude = resMarkerMap.get("y").toString();
						resMarker.setY(latitude);
					} else {
						msg.append("缺少纬度参数，请检查[resMarker->y]！");
					}
					if(CommonFunctions.isNotBlank(resMarkerMap, "mapType")) {
						resMarker.setMapType(resMarkerMap.get("mapType").toString());
					} else {
						Map<String, Object> maptMap = baseConversionService.getMaptAndConversionXY(event.getGridId(), orgCode, EVENT_MODULE_CODE, longitude, latitude);
						if(CommonFunctions.isNotBlank(maptMap, "mapt")) {
							resMarker.setMapType(maptMap.get("mapt").toString());
						} else {
							msg.append("缺少地图类型参数，请检查[resMarker->mapType]！");
						}
					}
					if(CommonFunctions.isNotBlank(resMarkerMap, "markerType")) {
						resMarker.setMarkerType(resMarkerMap.get("markerType").toString());
					}
					
					event.setResMarker(resMarker);
				}
				
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "attachment")) {//attachment格式[{'fileName':'', 'eventSeq':'', 'filePath':''}]
				String attachmentStr = eventMap.get("attachment").toString();
				String[] attachmentArray = JsonUtils.json2StringArray(attachmentStr);
				Map<String, Object> attachmentMap = null;
				String fileName = "", eventSeq = "", filePath = "",
					   DEFAULT_FILE_NAME = "TEST_"+ Math.random() +".jpg";
				StringBuffer attachmentIds = new StringBuffer("");
				byte[] buffer = null;
				
				for(String attachemntObjStr : attachmentArray) {
					attachmentMap = JsonUtils.json2Map(attachemntObjStr);
					
					if(CommonFunctions.isNotBlank(attachmentMap, "fileName")) {
						fileName = attachmentMap.get("fileName").toString();
					} else {
						fileName = DEFAULT_FILE_NAME;
					}
					
					if(CommonFunctions.isNotBlank(attachmentMap, "eventSeq")) {
						eventSeq = attachmentMap.get("eventSeq").toString();
					} else {
						eventSeq = DEFAULT_EVENT_SEQ;
					}
					
					if(CommonFunctions.isNotBlank(attachmentMap, "filePath")) {
						InputStream netFileInputStream = null;
						filePath = attachmentMap.get("filePath").toString();
						buffer = new byte[1024];  
						
						try {
							URL url = new URL(filePath);
							URLConnection urlConn = url.openConnection();
							netFileInputStream = urlConn.getInputStream();
							
							if(netFileInputStream != null) {
								String picUrl = "";
						        int len = 0;  
						        
						        //将获取到的附件转换为字节数组
						        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
						        while((len = netFileInputStream.read(buffer)) != -1) {  
						            bos.write(buffer, 0, len);  
						        }  
						        bos.close();  
						        
						        byte[] bytes = bos.toByteArray();  
						        
								if(bytes.length > 0) {//上传附件
									picUrl = fileUploadService.uploadSingleFile(fileName, bytes, ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_EXTERIOR);
								}
								
								if(StringUtils.isNotBlank(picUrl)) {
									Attachment att = new Attachment();
									
									Image imgOri = javax.imageio.ImageIO.read(url.openStream());
									if(imgOri != null) {//获取图片的高度、宽度，不是图片的附件，imgOri为null
										Integer imgWidthOri = imgOri.getWidth(null);
										Integer imgHeightOri = imgOri.getHeight(null);
										att.setImgWidth(imgWidthOri + "");
										att.setImgHeight(imgHeightOri + "");
									}
									
									if(DEFAULT_FILE_NAME.equals(fileName) && picUrl.lastIndexOf("/") >= 0) {//如果使用的是默认附件名称，则最后保留的附件名称为上传成功后自动生成的文件名称
										fileName = picUrl.substring(picUrl.lastIndexOf("/") + 1);
									}
									
									att.setBizId(event.getEventId());
									att.setAttachmentType(ConstantValue.EVENT_ATTACHMENT_TYPE);
									att.setFilePath(picUrl);
									att.setFileName(fileName);
									att.setFileSize(String.valueOf(bytes.length));
									att.setEventSeq(eventSeq);
									att.setCreatorId(event.getCreatorId());
									att.setCreatorName(event.getCreatorName());
									//将附件保存入库
									Long attachmentId = attachmentService.saveAttachment(att);
									
									if (attachmentId > 0) {
										att.setAttachmentId(attachmentId);
										attachmentIds.append(",").append(attachmentId);
									}
								}
							}
						} catch (IOException e) {
							logger.error("读取：" + filePath + " 失败！");
							e.printStackTrace();
						} finally {
							try {
								if (netFileInputStream != null) {
									netFileInputStream.close();
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
					}
				}
				
				if(attachmentIds.length() > 0) {
					event.setAttachmentIds(attachmentIds.substring(1));
				}
				
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "outerAttachmentIds")) {//附件id，以英文逗号分隔，没有进行判重处理
				String outerAttachmentIds = eventMap.get("outerAttachmentIds").toString();
				String[] outerAttachmentIdArray = outerAttachmentIds.split(",");
				Long attachmentId = -1L, attachmentIdAfter = -1L;
				Attachment attachment = null;
				StringBuffer attachmentIds = new StringBuffer("");
				String eventSeq = "";
				
				for(String outerAttachmentId : outerAttachmentIdArray) {
					attachmentId = -1L;
					
					if(StringUtils.isNotBlank(outerAttachmentId)) {
						try {
							attachmentId = Long.valueOf(outerAttachmentId);
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
					}
					
					if(attachmentId > 0) {
						attachment = attachmentService.findById(attachmentId);
						
						if(attachment != null) {
							eventSeq = attachment.getEventSeq();
							
							if(StringUtils.isBlank(eventSeq)) {
								attachment.setEventSeq(DEFAULT_EVENT_SEQ);
							}
							attachment.setAttachmentId(null);
							attachment.setAttachmentType(ConstantValue.EVENT_ATTACHMENT_TYPE);
							attachmentIdAfter = attachmentService.saveAttachment(attachment);
							
							if(attachmentIdAfter > 0) {
								attachmentIds.append(",").append(attachmentIdAfter);
							}
						}
					}
				}
				
				if(attachmentIds.length() > 0) {
					event.setAttachmentIds(attachmentIds.substring(1));
				}
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "outerAttachmentList")) {//附件列表，类型为List<Attachment>
				Object outerAttachmentListObj = eventMap.get("outerAttachmentList");
				Long attachmentIdAfter = -1L;
				StringBuffer attachmentIds = new StringBuffer("");
				
				if(outerAttachmentListObj instanceof List) {
					List<Attachment> outerAttachmentList = (List<Attachment>)outerAttachmentListObj;
					
					if(outerAttachmentList != null) {
						String eventSeq = "";
						
						for(Attachment attachment : outerAttachmentList) {
							eventSeq = attachment.getEventSeq();
							
							if(StringUtils.isBlank(eventSeq)) {
								attachment.setEventSeq(DEFAULT_EVENT_SEQ);
							}
							attachment.setAttachmentId(null);
							attachment.setAttachmentType(ConstantValue.EVENT_ATTACHMENT_TYPE);
							attachmentIdAfter = attachmentService.saveAttachment(attachment);
							
							if(attachmentIdAfter > 0) {
								attachmentIds.append(",").append(attachmentIdAfter);
							}
						}
					}
				}
				
				if(attachmentIds.length() > 0) {
					event.setAttachmentIds(attachmentIds.substring(1));
				}
			}
			
			if(msg.length() > 0) {
				eventMap.put("msg", msg.toString());
				logger.error("有如下报错信息："+msg);
			}
		}
	
		event = eventDisposalService.formatData(event, orgCode);
		
		return event;
	}
	
	@Override
	public Map<String, Object> eventJsonToMap(String eventJson){
		Map<String, Object> eventMap = null;
		
		if(StringUtils.isNotBlank(eventJson) && StringUtils.isNotBlank(eventJson.trim())){
			try{
				eventMap = JsonUtils.json2Map(eventJson);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		eventMap = this.eventJsonToMap(eventMap);
		
		return eventMap;
	}
	
	/********************************************************************************
	 * 人员所属组织优先使用orgId；然后使用orgCode；再使用为事件对接配置的默认组织信息；最后使用session中的组织信息
	 * 事件所属网格优先使用gridId获取；然后是使用gridCode；最后使用所属组织获取
	 * 事件办理人员优先使用userId；然后使用session中的人员信息；在事件对接中，使用默认配置的接收人员；最后使用所属组织获取
	 */
	@Override
	public Map<String, Object> eventJsonToMap(Map<String, Object> eventMap){
		StringBuffer msg = new StringBuffer("");
		MixedGridInfo mixedGridInfo = null;
		UserInfo  userInfo = null;
		OrgSocialInfoBO orgSocialInfo = null;//用户组织域信息
		enactDefaultValue(eventMap);//如果有传递事件对象时，设置必要参数
		boolean validateFlag = checkParamValidate(eventMap);
		boolean isClear4Wrong = true;//是否因为有转换异常而将转换结果清空，默认为会清空
		
		if(validateFlag && eventMap != null){
			if(CommonFunctions.isNotBlank(eventMap, "gridId")){//依据网格编号获取网格信息
				Long gridId = Long.valueOf(eventMap.get("gridId").toString());
				mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
			}else if(CommonFunctions.isNotBlank(eventMap, "gridCode")){//依据网格编码获取网格信息
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("gridCode", eventMap.get("gridCode"));
				List<MixedGridInfo> mixedGridInfolist = mixedGridInfoService.findByGridCode(params);
				
				if(mixedGridInfolist!=null && mixedGridInfolist.size()>0){
					mixedGridInfo = mixedGridInfolist.get(0);
				}
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "userId")){//json转换用户信息
				Long userId = Long.valueOf(eventMap.get("userId").toString());
				userInfo = userService.getUserExtraInfoByUserId(userId, null);
			}else if(CommonFunctions.isNotBlank(eventMap, "userInfo")){
				userInfo = (UserInfo)eventMap.get("userInfo");
			}else{
				userInfo = new UserInfo();
			}
			
			if(userInfo != null){
				String bizPlatform = "";//事件归属平台 事件对接时使用
				if(CommonFunctions.isNotBlank(eventMap, "bizPlatform")){
					bizPlatform = eventMap.get("bizPlatform").toString();
//					//验证bizPlatform的有效性
//					bizPlatform = baseDictionaryService.changeCodeToName(ConstantValue.BIZ_PLATFORM_PCODE, bizPlatform, userInfo.getOrgCode());
//					if(StringUtils.isBlank(bizPlatform)){
//						msg.append("参数[bizPlatform]无效，请检查！");
//					}
				}

				if(CommonFunctions.isNotBlank(eventMap, "orgId")){//当用户有多个组织时，需要设定该值，否则取值可能不正确
					Long orgId = Long.valueOf(eventMap.get("orgId").toString());
					orgSocialInfo = orgSocialInfoService.findByOrgId(orgId);
				}else if(StringUtils.isNotBlank(bizPlatform) && !ConstantValue.DEFAULT_EVENT_BIZ_PLATFORM.equals(bizPlatform)){//获取配置的默认组织
					String orgCode = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_RECEIVE_ORG_TRIGGER+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
					
					if(StringUtils.isNotBlank(orgCode)) {
						orgSocialInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(orgCode);
					}
				}
				
				if(CommonFunctions.isNotBlank(eventMap, "orgCode")){
					String orgCode = eventMap.get("orgCode").toString();
					orgSocialInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(orgCode);
				} else if(orgSocialInfo == null && userInfo.getOrgId() != null) {
					Long orgId = userInfo.getOrgId();
					orgSocialInfo = orgSocialInfoService.findByOrgId(orgId);
				}
				
				if(orgSocialInfo!=null && orgSocialInfo.getOrgId()!=null){//设置用户组织信息，其会影响到短信模板获取，上报人员获取等
					userInfo.setOrgId(orgSocialInfo.getOrgId());
					userInfo.setOrgCode(orgSocialInfo.getOrgCode());
					userInfo.setOrgName(orgSocialInfo.getOrgName());
					
					//当用户id不存在时，如有配置默认接收人员，其优先使用；否则默认取当前组织下的首个用户；但bizPlatform不为空且不为0时，优先使用默认接收人员
					if(userInfo.getUserId()==null && StringUtils.isNotBlank(bizPlatform) && !ConstantValue.DEFAULT_EVENT_BIZ_PLATFORM.equals(bizPlatform)){
						
						UserBO user = null;
						String userRegisterValue = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_RECEIVE_USER_TRIGGER+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
						if(StringUtils.isNotBlank(userRegisterValue)){
							user = userManageService.getSimpleUserInfoByRegistValue(userRegisterValue);
						}else{
							String positionName = funConfigurationService.turnCodeToValue(ConstantValue.POSITION_NAME_FUNCODE, bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgSocialInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
							List<UserBO> users = (ArrayList<UserBO>)userManageService.queryUserListByPositionAndOrg(positionName, null,"0", orgSocialInfo.getOrgId(), null);
							if(users!=null && users.size()>0){
								user = users.get(0);
							}
						}
						
						if(user != null){
							userInfo.setUserId(user.getUserId());
							userInfo.setPartyId(user.getPartyId());
							userInfo.setPartyName(user.getPartyName());
							userInfo.setVerifyMobile(String.valueOf(user.getVerifyMobile()));
						}
					}
					
					if(mixedGridInfo == null){//通过组织域获取所属网格
						Long inforOrgId = orgSocialInfo.getLocationOrgId();//所属地域
						OrgEntityInfoBO orgEntityInfo = orgEntityInfoOutService.findByOrgId(inforOrgId);
						if(orgEntityInfo != null){
							if(StringUtils.isNotBlank(orgEntityInfo.getOrgCode())){
								mixedGridInfo = mixedGridInfoService.getDefaultGridByOrgCode(orgEntityInfo.getOrgCode());
							}
						}else{
							msg.append("组织名称为"+orgSocialInfo.getOrgName()+"，组织编号为"+orgSocialInfo.getOrgCode()+"的组织缺少“所属地域”配置，请检查！");
						}
					}
				}else{
					msg.append("缺少组织域参数，请检查[orgId]，[orgCode]！");
				}
				
				if(CommonFunctions.isBlank(eventMap, "type")){//设置事件的默认类别
					String eventDefaultType = "";
					
					if(CommonFunctions.isNotBlank(eventMap, "trigger")){
						String trigger = eventMap.get("trigger").toString();
						
						if(StringUtils.isNotBlank(trigger)){//trigger生效时，需要配置默认事件类别，否则为空
							eventDefaultType =  funConfigurationService.changeCodeToValue(ConstantValue.TYPES, trigger+ConstantValue.DEFAULT_TYPE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
						}
					}else{//获取对接使用的默认事件类别
						eventDefaultType = funConfigurationService.turnCodeToValue(ConstantValue.THREE_PART_JOINT_UNOIN, ConstantValue.DEFAULT_EVENT_TYPE_TRIGGER+"_"+bizPlatform, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
					}
					
					if(StringUtils.isBlank(eventDefaultType)){
						eventDefaultType = ConstantValue.DEFAULT_SMALL_TYPE;
					}
					
					eventMap.put("type", eventDefaultType);
				}
				
				eventMap.put("userInfo", userInfo);
			}else{
				msg.append("缺少当前用户参数，请检查[userId]！");
			}
			
			if(mixedGridInfo != null){//获取网格信息
				eventMap.put("gridId", mixedGridInfo.getGridId());
				eventMap.put("gridCode", mixedGridInfo.getInfoOrgCode());//使用网格对应的信息域编码
				eventMap.put("gridName", mixedGridInfo.getGridName());
			}else{
				msg.append("缺少网格参数，请检查[gridId]，[gridCode]！");
			}
			
			eventMap.put("event", this.jsonToEvent(eventMap));
			
			if(CommonFunctions.isBlank(eventMap, "content")){
				msg.append("缺少参数[content]，请检查！");
			}
			
			if(CommonFunctions.isBlank(eventMap, "occurred")){
				msg.append("缺少参数[occurred]，请检查！");
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "msg")){
				msg.append(eventMap.get("msg").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "isClear4Wrong")) {
				isClear4Wrong = Boolean.valueOf(eventMap.get("isClear4Wrong").toString());
			}
			
			if(isClear4Wrong && CommonFunctions.isBlank(eventMap, "eventId") && msg.length() > 0) {//传递eventId无效时，才保留字段验证信息
				eventMap = new HashMap<String, Object>();//有报错信息时，清空已有的存放数据
				eventMap.put("msg", msg.toString());//存放参数错误信息，正常时，该参数为空
				logger.info("有如下报错信息："+msg);
			}
		}
		
		return eventMap;
	}
	
	@Override
	public Map<String, Object> subWorkflow(Map<String, Object> eventMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long eventId = -1L, instanceId = -1L, curTaskId = -1L,
			 eventIdChecked = -1L, instanceIdChecked = -1L;
		String evaLevel = "", evaContent = "", evaUserPartyName = "", smsContent = "", 
			   advice = "", nextNodeName = "", nextUserIds = "", nextOrgIds = "",
			   INSTANCE_ARCHIVED_STATUS = "2";
		StringBuffer msgWrong = new StringBuffer("");
		UserInfo userInfo = new UserInfo();
		ProInstance pro = null;
		boolean result = false;
		
		eventMap = this.eventJsonToMap(eventMap);
		
		if(CommonFunctions.isNotBlank(eventMap, "eventId")) {
			try {
				eventId = Long.valueOf(eventMap.get("eventId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(eventMap, "instanceId")) {
			try {
				instanceId = Long.valueOf(eventMap.get("instanceId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(eventId > 0 && instanceId < 0) {
			instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
		} else if(eventId < 0 && instanceId > 0) {
			pro = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
			if(pro != null) {
				eventId = pro.getFormId();
			}
		}
		if(eventId > 0) {
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			if(event == null) {
				msgWrong.append("未找到与 [").append(eventId).append("] 相对应的有效的事件信息 ！");
			} else {
				eventIdChecked = eventId;
			}
		}
		if(instanceId > 0) {
			if(pro == null) {
				pro = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
			}
			if(pro == null) {
				msgWrong.append("未找到与 [").append(instanceId).append("] 相对应的有效的流程实例信息 ！");
			} else if(INSTANCE_ARCHIVED_STATUS.equals(pro.getStatus())) {
				msgWrong.append("与 [").append(instanceId).append("] 相对应的流程实例已结束 ！");
			} else {
				instanceIdChecked = instanceId;
			}
		}
		if(CommonFunctions.isNotBlank(eventMap, "curTaskId")) {
			try {
				curTaskId = Long.valueOf(eventMap.get("curTaskId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(eventMap, "nextUserIds")) {
			nextUserIds = eventMap.get("nextUserIds").toString();
		}
		if(CommonFunctions.isNotBlank(eventMap, "nextOrgIds")) {
			nextOrgIds = eventMap.get("nextOrgIds").toString();
		}
		if(CommonFunctions.isNotBlank(eventMap, "smsContent")) {
			smsContent = eventMap.get("smsContent").toString();
		}
		if(CommonFunctions.isNotBlank(eventMap, "evaLevel")) {
			evaLevel = eventMap.get("evaLevel").toString();
		}
		if(CommonFunctions.isNotBlank(eventMap, "evaContent")) {
			evaContent = eventMap.get("evaContent").toString();
		}
		if(CommonFunctions.isNotBlank(eventMap, "evaUserPartyName")) {
			evaUserPartyName = eventMap.get("evaUserPartyName").toString();
		}
		if(CommonFunctions.isNotBlank(eventMap, "userInfo")) {
			userInfo = (UserInfo)eventMap.get("userInfo");
		}
		
		if(CommonFunctions.isNotBlank(eventMap, "advice")) {
			advice = eventMap.get("advice").toString();
		} else if(StringUtils.isNotBlank(evaContent)) {
			advice = evaContent;
		}
		if(CommonFunctions.isNotBlank(eventMap, "nextNodeName")) {
			nextNodeName = eventMap.get("nextNodeName").toString();
		} else if(instanceIdChecked > 0) {
			Map<String, Object> initResultMap = eventDisposalWorkflowService.initFlowInfo("", instanceId, userInfo, null);
			if(CommonFunctions.isNotBlank(initResultMap, "taskNodes")) {
				List<Node> taskNodes = (List<Node>) initResultMap.get("taskNodes");
				if(taskNodes.size() > 0) {
					nextNodeName = taskNodes.get(0).getNodeName();
				}
			}
		}
		
		if(eventId < 0 && instanceId < 0) {
			msgWrong.append("参数[eventId]和[instanceId]必须至少一个！");
		} else {
			if(curTaskId < 0) {
				curTaskId = eventDisposalWorkflowService.curNodeTaskId(instanceId);
			}
		}
		
		if((userInfo == null || (userInfo.getUserId() == null && userInfo.getOrgId() == null)) && curTaskId > 0) {//为有设置当前办理人员信息时，将其指定为当前任务的可办理人员中的一个
			List<Map<String, Object>> taskPerson = eventDisposalWorkflowService.queryMyTaskParticipation(curTaskId.toString());
			userInfo = new UserInfo();//防止userInfo为null的情况
			Long userId = -1L;
			
			if(taskPerson.size() > 0) {
				Map<String, Object> taskPersonMap = taskPerson.get(0);
				
				if(CommonFunctions.isNotBlank(taskPersonMap, "USER_NAME")) {//有用户名称，证明用户id有效
					if(CommonFunctions.isNotBlank(taskPersonMap, "USER_ID")) {
						userId = Long.valueOf(taskPersonMap.get("USER_ID").toString());
						userInfo.setUserId(userId);
					}
					
					userInfo.setPartyName(taskPersonMap.get("USER_NAME").toString());
				}
				if(CommonFunctions.isNotBlank(taskPersonMap, "ORG_NAME")) {//有组织名称，证明组织id有效
					if(CommonFunctions.isNotBlank(taskPersonMap, "ORG_ID")) {
						Long orgId = Long.valueOf(taskPersonMap.get("ORG_ID").toString());
						
						OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
						if(orgInfo != null) {
							if(userId == null || userId <= 0) {
								List<cn.ffcs.uam.bo.UserInfo> userInfoList = userInfoService.findUserInfoByOrgId(orgId);
								if(userInfoList != null && userInfoList.size() > 0) {
									cn.ffcs.uam.bo.UserInfo _userInfo = userInfoList.get(0);
									userId = _userInfo.getUserId();
									userInfo.setUserId(userId);
									userInfo.setPartyName(_userInfo.getPartyName());
									userInfo.setPartyId(_userInfo.getPartyId());
								}
							}
							
							userInfo.setOrgId(orgId);
							userInfo.setOrgName(orgInfo.getOrgName());
							userInfo.setOrgCode(orgInfo.getOrgCode());
						}
					}
				}
			}
			
			if(userId < 0) {
				msgWrong.append("缺少可办理的人员！");
			}
		}
		
		if(StringUtils.isBlank(nextNodeName)) {
			msgWrong.append("缺少可办理的下一环节！");
		}
		
		if(msgWrong.length() > 0) {
			resultMap.put("msg", msgWrong.toString());
		} else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(eventMap);
			
			//保存评价信息
//			if(eventId > 0 && StringUtils.isNotBlank(evaLevel) && StringUtils.isNotBlank(evaContent)){
//				if(StringUtils.isBlank(evaUserPartyName)) {
//					evaUserPartyName = userInfo.getPartyName();
//				}
//				EvaResult evaResult = new EvaResult();
//				evaResult.setObjectId(eventId);
//				evaResult.setEvaObj(ConstantValue.EVA_OBJ_NEW_EVENT);
//				evaResult.setEvaLevel(evaLevel);
//				evaResult.setEvaContent(evaContent);
//				evaResult.setCreateUser(userInfo.getUserId());
//				evaResult.setCreatorName(evaUserPartyName);
//				evaResult.setUpdateUser(userInfo.getUserId());
//				evaResult.setUpdaterName(evaUserPartyName);
//	
//				evaResultService.saveEvaResult(evaResult);
//				
//			}
			
			params.put("instanceId", instanceId);
			params.put("curTaskId", curTaskId);
			params.put("nextNodeName", nextNodeName);
			params.put("nextUserIds", nextUserIds);
			params.put("nextOrgIds", nextOrgIds);
			params.put("advice", advice);
			params.put("smsContent", smsContent);
			
			try {
				result = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(params, userInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		resultMap.put("result", result);
		
		return resultMap;
	}
	
	/**
	 * 参数调整
	 * @param params
	 * @return
	 */
	private Map<String, Object> modifyParams(Map<String, Object> params){
		if(CommonFunctions.isNotBlank(params, "types")){
			String types = params.get("types").toString();
			StringBuffer bigTypes = new StringBuffer("");
			StringBuffer smallTypes = new StringBuffer("");
			String[] typeArray = types.split(",");
			
			for(int index = 0, len = typeArray.length; index < len; index++){
				String tmp = typeArray[index];
				if(StringUtils.isNotBlank(tmp)){
					if(tmp.length() == 2){
						bigTypes.append(tmp).append(",");
					}else if(tmp.length() > 2){
						smallTypes.append(tmp).append(",");
					}
				}
			}
			
			if(bigTypes.length()>0 || smallTypes.length()>0){
				if(bigTypes.length() > 0){
					params.put("bigTypes", bigTypes.substring(0, bigTypes.length()-1));
				}
				
				if(smallTypes.length() > 0){
					params.put("smallTypes", smallTypes.substring(0, smallTypes.length()-1));
				}
			}
		}
		
		return params;
	}
	
	/**
	 * 验证字段的有效性
	 * @param params
	 * @return
	 */
	private boolean checkParamValidate(Map<String, Object> params){
		boolean validateFlag = true;
		
		if(params != null){
			int eventNameLength = 100;
			int contentLength = 4000;
			int occurredLength = 255;
			int resultLength = 1000;
			
			String eventNameLengthStr = funConfigurationService.turnCodeToValue(ConstantValue.VALIDATE_LENGTH_FUNCTION_CODE, VALIDATE_LENGTH_EVENT_NAME, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			String contentLengthStr = funConfigurationService.turnCodeToValue(ConstantValue.VALIDATE_LENGTH_FUNCTION_CODE, VALIDATE_LENGTH_CONTENT, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			String occurredLengthStr = funConfigurationService.turnCodeToValue(ConstantValue.VALIDATE_LENGTH_FUNCTION_CODE, VALIDATE_LENGTH_OCCURRED, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			String resultLengthStr = funConfigurationService.turnCodeToValue(ConstantValue.VALIDATE_LENGTH_FUNCTION_CODE, VALIDATE_LENGTH_RESULT, IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			
			if(StringUtils.isNotBlank(eventNameLengthStr)){
				try{
					eventNameLength = Integer.valueOf(eventNameLengthStr);
				}catch(NumberFormatException e){
					logger.info("eventName长度配置获取失败，因为"+eventNameLengthStr+"不能转换为数字！");
				}
			}
			if(StringUtils.isNotBlank(contentLengthStr)){
				try{
					contentLength = Integer.valueOf(contentLengthStr);
				}catch(NumberFormatException e){
					logger.info("content长度配置获取失败，因为"+contentLengthStr+"不能转换为数字！");
				}
			}
			if(StringUtils.isNotBlank(occurredLengthStr)){
				try{
					occurredLength = Integer.valueOf(occurredLengthStr);
				}catch(NumberFormatException e){
					logger.info("occurred长度配置获取失败，因为"+occurredLengthStr+"不能转换为数字！");
				}
			}
			if(StringUtils.isNotBlank(resultLengthStr)){
				try{
					resultLength = Integer.valueOf(resultLengthStr);
				}catch(NumberFormatException e){
					logger.info("result长度配置获取失败，因为"+resultLengthStr+"不能转换为数字！");
				}
			}
			
			validateFlag = CommonFunctions.isLengthValidate(params, VALIDATE_LENGTH_EVENT_NAME, eventNameLength)
						&& CommonFunctions.isLengthValidate(params, VALIDATE_LENGTH_CONTENT, contentLength)
						&& CommonFunctions.isLengthValidate(params, VALIDATE_LENGTH_OCCURRED, occurredLength)
						&& CommonFunctions.isLengthValidate(params, VALIDATE_LENGTH_RESULT, resultLength);
		}
		
		return validateFlag;
	}
	
	/**
	 * 对于有赋默认值和必填项，优先使用传入的事件对象中的属性
	 * @param eventMap
	 */
	private void enactDefaultValue(Map<String, Object> eventMap){
		if(CommonFunctions.isNotBlank(eventMap, "event")){
			EventDisposal event = (EventDisposal)eventMap.get("event");
			
			if(CommonFunctions.isBlank(eventMap, "gridId") && event.getGridId()!=null && event.getGridId()>0){
				eventMap.put("gridId", event.getGridId());
			}
			
			if(CommonFunctions.isBlank(eventMap, "type") && StringUtils.isNotBlank(event.getType())){
				eventMap.put("type", event.getType());
			}
			
			if(CommonFunctions.isBlank(eventMap, "content") && StringUtils.isNotBlank(event.getContent())){
				eventMap.put("content", event.getContent());
			}
			
			if(CommonFunctions.isBlank(eventMap, "occurred") && StringUtils.isNotBlank(event.getOccurred())){
				eventMap.put("occurred", event.getOccurred());
			}
			
			if(CommonFunctions.isBlank(eventMap, "collectWay") && StringUtils.isNotBlank(event.getCollectWay())){
				eventMap.put("collectWay", event.getCollectWay());
			}
			
			if(CommonFunctions.isBlank(eventMap, "happenTimeStr") && StringUtils.isNotBlank(event.getHappenTimeStr())){
				eventMap.put("happenTimeStr", event.getHappenTimeStr());
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "handleDateStr") && StringUtils.isNotBlank(event.getHandleDateStr())){
				eventMap.put("handleDateStr", event.getHandleDateStr());
			}
			
		}
	}
}
