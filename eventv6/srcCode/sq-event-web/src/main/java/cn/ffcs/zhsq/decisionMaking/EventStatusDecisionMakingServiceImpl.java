package cn.ffcs.zhsq.decisionMaking;

import cn.ffcs.gmis.mybatis.domain.userscore.UserDetailScore;
import cn.ffcs.gmis.userscore.service.UserScoreService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.zzgl.service.holidayinfo.IHolidayInfoService;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.common.Constants;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.EventProcess;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新版事件通用 事件状态变更决策类
 * 必填参数
 * 		decisionService 指定实现类 eventStatusDecisionMakingService
 * 		eventId			事件id
 * 		curNodeCode		当前节点编码
 *		nextNodeCode	下一节点编码
 * 		userId			办理人员id
 * 		orgId			办理人员所属组织
 * 非必填参数
 * 		handleDate		环节处理时限，办结时为结案时间
 * 		isReject		true时，表示当前操作为驳回操作
 * 		isRecall		true时，表示当前操作为撤回操作
 *      isRecovery		true时，表示只是恢复事件状态，不添加事件中间过程
 * 		preNodeCode		上一节点编码，当isReject/isRecall为true时，传入
 * 
 * @author zhangls
 *
 */
@Service(value = "eventStatusDecisionMakingService")
public class EventStatusDecisionMakingServiceImpl extends
		ApplicationObjectSupport implements IWorkflowDecisionMakingService<String> {

	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private UserScoreService userScoreService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IHolidayInfoService holidayInfoService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	//环节编码
	protected static final String START_NODE_CODE = "start";		//事件流程启动
	protected static final String COLLECT_NODE_CODE = "task1";	//事件采集处理环节
	private static final String UNITED_PREVENTION_GROUP_NODE_CODE = "task12";//联防组网格
	protected static final String GRID_ADMIN_NODE_CODE = "task2";	//网格员处理环节
	protected static final String COMMUNITY_NODE_CODE = "task3";	//村(社区)处理环节
	protected static final String STREET_NODE_CODE = "task4";	//乡镇(街道)处理环节
	protected static final String DISTRICT_NODE_CODE = "task5";	//县(区)处理环节
	protected static final String MUNICIPAL_NODE_CODE = "task10";//市级处理环节
	protected static final String CLOSE_NODE_CODE = "task8";		//结案环节节点编码
	private static final String EVALUATE_NODE_CODE = "task9";	//评价环节
	
	protected static final String STREET_DEPARTMENT_NODE_CODE = "task6";		//街道职能部门办理
	protected static final String DISTRICT_DEPARTMENT_NODE_CODE = "task7";	//区职能部门办理
	private static final String MUNICIPAL_DEPARTMENT_NODE_CODE = "task11";	//市职能部门处理
	
	protected static final String END_NODE_CODE = "end1";			//归档环节
	
	//驳回节点编码
	protected static final String REJECT_NODE_CODE = "reject";	//虚拟的驳回环节节点，流程图中不存在
	//撤回节点编码
	private static final String RECALL_NODE_CODE = "recall";	//虚拟的撤回环节节点，流程图中不存在
	
	private static final String OPERATE_TYPE_REJECT = "2";//驳回操作
	private static final String OPERATE_TYPE_RECALL = "5";//撤回操作
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		Long eventId = -1L;			//事件id
		String eventStatus = "",	//事件状态
			   curNodeCode = "",	//当前环节节点编码
			   nextNodeCode = "",	//下一环节节点编码
			   formTypeIdStr = null;
		
		if(CommonFunctions.isNotBlank(params, "eventId")) {
			try{
				eventId = Long.valueOf(params.get("eventId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[eventId]："+params.get("eventId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[eventId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "formTypeId")) {
			formTypeIdStr = params.get("formTypeId").toString();
		} else if(CommonFunctions.isNotBlank(params, "instanceId")) {
			Long instanceId = null;
			ProInstance proInstance = null;
			
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {}
			
			if(instanceId != null && instanceId > 0) {
				proInstance = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
				
				if(proInstance != null) {
					formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
				}
			}
		} else if(eventId != null && eventId > 0) {
			ProInstance proInstance = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
			
			if(proInstance != null) {
				formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
			}
		}
		
		//只有主流程才进行事件状态相关更新
		if(ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID.equals(formTypeIdStr)) {
			Long userId = -1L,			//事件操作人员Id
			     orgId = -1L;			//事件操作人员所属组织
			Date handleDate = null;		//环节办理时限
			
			if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
				curNodeCode = params.get("curNodeCode").toString();
			} else {
				throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
			}
			
			if(CommonFunctions.isNotBlank(params, "nextNodeCode")) {
				nextNodeCode = params.get("nextNodeCode").toString();
			} else {
				throw new IllegalArgumentException("缺失参数[nextNodeCode]，请检查！");
			}
			
			if(CommonFunctions.isNotBlank(params, "userId")) {
				try{
					userId = Long.valueOf(params.get("userId").toString());
				} catch(NumberFormatException e) {
					throw new NumberFormatException("参数[userId]："+params.get("userId")+" 不能转换为Long型！");
				}
			} else {
				throw new IllegalArgumentException("缺失参数[userId]，请检查！");
			}
			
			if(CommonFunctions.isNotBlank(params, "orgId")) {
				try{
					orgId = Long.valueOf(params.get("orgId").toString());
				} catch(NumberFormatException e) {
					throw new NumberFormatException("参数[orgId]："+params.get("orgId")+" 不能转换为Long型！");
				}
			} else {
				throw new IllegalArgumentException("缺失参数[orgId]，请检查！");
			}
			
			if(CommonFunctions.isNotBlank(params, "handleDate")) {
				handleDate = (Date)params.get("handleDate");
			}
			
			eventStatus = this.updateEventStatus(eventId, userId, orgId, "", curNodeCode, nextNodeCode, handleDate, params);
			
			if(END_NODE_CODE.equals(nextNodeCode)) {//事件归档时，新增个人积分信息
				addUserDetailScore(eventId);
			}
		}
		
		return eventStatus;
	}
	
	/**
	 * 初始化事件状态Map
	 */
	protected Map<String, Object> initStatusMap(String chiefLevel) {
		Map<String, Object> statusMap = new HashMap<String, Object>();
		
		statusMap.put(START_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);		//事件启动流程
		statusMap.put(COLLECT_NODE_CODE, ConstantValue.EVENT_STATUS_RECEIVED);		//事件采集
		
		//上报、越级上报
		statusMap.put(UNITED_PREVENTION_GROUP_NODE_CODE+"-"+GRID_ADMIN_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(UNITED_PREVENTION_GROUP_NODE_CODE+"-"+COMMUNITY_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(UNITED_PREVENTION_GROUP_NODE_CODE+"-"+STREET_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(UNITED_PREVENTION_GROUP_NODE_CODE+"-"+DISTRICT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(UNITED_PREVENTION_GROUP_NODE_CODE+"-"+MUNICIPAL_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		statusMap.put(GRID_ADMIN_NODE_CODE+"-"+COMMUNITY_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(GRID_ADMIN_NODE_CODE+"-"+STREET_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(GRID_ADMIN_NODE_CODE+"-"+DISTRICT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(GRID_ADMIN_NODE_CODE+"-"+MUNICIPAL_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		statusMap.put(COMMUNITY_NODE_CODE+"-"+STREET_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(COMMUNITY_NODE_CODE+"-"+STREET_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(COMMUNITY_NODE_CODE+"-"+DISTRICT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(COMMUNITY_NODE_CODE+"-"+MUNICIPAL_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		statusMap.put(STREET_NODE_CODE+"-"+DISTRICT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(STREET_NODE_CODE+"-"+MUNICIPAL_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		statusMap.put(DISTRICT_NODE_CODE+"-"+MUNICIPAL_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		statusMap.put(STREET_DEPARTMENT_NODE_CODE+"-"+STREET_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(STREET_DEPARTMENT_NODE_CODE+"-"+DISTRICT_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(STREET_DEPARTMENT_NODE_CODE+"-"+MUNICIPAL_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		statusMap.put(DISTRICT_DEPARTMENT_NODE_CODE+"-"+DISTRICT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		statusMap.put(DISTRICT_DEPARTMENT_NODE_CODE+"-"+MUNICIPAL_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		statusMap.put(MUNICIPAL_DEPARTMENT_NODE_CODE+"-"+MUNICIPAL_NODE_CODE, ConstantValue.EVENT_STATUS_REPORT);
		
		//分流、下派
		statusMap.put(GRID_ADMIN_NODE_CODE+"-"+UNITED_PREVENTION_GROUP_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		statusMap.put(COMMUNITY_NODE_CODE+"-"+UNITED_PREVENTION_GROUP_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(COMMUNITY_NODE_CODE+"-"+GRID_ADMIN_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		statusMap.put(STREET_NODE_CODE+"-"+UNITED_PREVENTION_GROUP_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(STREET_NODE_CODE+"-"+COMMUNITY_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(STREET_NODE_CODE+"-"+GRID_ADMIN_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(STREET_NODE_CODE+"-"+STREET_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		statusMap.put(STREET_DEPARTMENT_NODE_CODE+"-"+UNITED_PREVENTION_GROUP_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(STREET_DEPARTMENT_NODE_CODE+"-"+COMMUNITY_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(STREET_DEPARTMENT_NODE_CODE+"-"+GRID_ADMIN_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		statusMap.put(DISTRICT_NODE_CODE+"-"+UNITED_PREVENTION_GROUP_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_NODE_CODE+"-"+STREET_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_NODE_CODE+"-"+COMMUNITY_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_NODE_CODE+"-"+GRID_ADMIN_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_NODE_CODE+"-"+STREET_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_NODE_CODE+"-"+DISTRICT_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		statusMap.put(DISTRICT_DEPARTMENT_NODE_CODE+"-"+UNITED_PREVENTION_GROUP_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_DEPARTMENT_NODE_CODE+"-"+STREET_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_DEPARTMENT_NODE_CODE+"-"+STREET_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_DEPARTMENT_NODE_CODE+"-"+COMMUNITY_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(DISTRICT_DEPARTMENT_NODE_CODE+"-"+GRID_ADMIN_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		statusMap.put(MUNICIPAL_NODE_CODE+"-"+UNITED_PREVENTION_GROUP_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(MUNICIPAL_NODE_CODE+"-"+DISTRICT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(MUNICIPAL_NODE_CODE+"-"+DISTRICT_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(MUNICIPAL_NODE_CODE+"-"+STREET_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(MUNICIPAL_NODE_CODE+"-"+STREET_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(MUNICIPAL_NODE_CODE+"-"+COMMUNITY_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(MUNICIPAL_NODE_CODE+"-"+GRID_ADMIN_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		statusMap.put(MUNICIPAL_NODE_CODE+"-"+MUNICIPAL_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		statusMap.put(MUNICIPAL_DEPARTMENT_NODE_CODE+"-"+DISTRICT_DEPARTMENT_NODE_CODE, ConstantValue.EVENT_STATUS_DISTRIBUTE);
		
		statusMap.put(CLOSE_NODE_CODE, ConstantValue.EVENT_STATUS_ARCHIVE);		//事件结案
		statusMap.put(EVALUATE_NODE_CODE, ConstantValue.EVENT_STATUS_ARCHIVE);	//事件评价 用于采集并结案
		
		statusMap.put(END_NODE_CODE, ConstantValue.EVENT_STATUS_END);			//事件归档
		
		return statusMap;
	}
	
	/**
	 * 获取对应的变更事件状态
	 * @param statusMap
	 * @param curNodeCode
	 * @param nextNodeCode
	 * @return
	 */
	protected String capEventStatus(Map<String, Object> statusMap, String curNodeCode, String nextNodeCode) {
		String eventStatus = "";
		String mapKey = curNodeCode + "-" + nextNodeCode;
		
		if((START_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) && CommonFunctions.isNotBlank(statusMap, curNodeCode)) {
			eventStatus = statusMap.get(curNodeCode).toString();
		} else if(CommonFunctions.isNotBlank(statusMap, mapKey)) {
			eventStatus = statusMap.get(mapKey).toString();
		} else if(CommonFunctions.isNotBlank(statusMap, nextNodeCode)) {
			eventStatus = statusMap.get(nextNodeCode).toString();
		}
		
		return eventStatus;
	}
	
	/**
	 * 更新事件状态，并新增中间记录
	 * @param eventId
	 * @param userId
	 * @param orgId
	 * @param chiefLevel
	 * @param curNodeCode
	 * @param nextNodeCode
	 * @param handleDate
	 * @return
	 * @throws Exception
	 */
	protected String updateEventStatus(Long eventId, Long userId, Long orgId, String chiefLevel, String curNodeCode, String nextNodeCode, Date handleDate, Map<String, Object> extraParam) throws Exception {
		String eventStatus = "";
		String eventNextStatus = "";
		EventDisposal event = new EventDisposal();
		ProInstance pro = null;
		Long instanceId = -1L;
		OrgSocialInfoBO orgInfo = null;
		boolean isRecovery = false;
		
		if(eventId > 0) {
			if(CommonFunctions.isNotBlank(extraParam, "isRecovery")) {
				isRecovery = Boolean.valueOf(extraParam.get("isRecovery").toString());
			}
			
			if(isRecovery) {
				Map<String, Object> eventParam = new HashMap<String, Object>(),
									eventMap = null;
				eventParam.put("isIgnoreStatus", true);
				
				eventMap = eventDisposalService.findEventByMap(eventId, eventParam);
				
				if(CommonFunctions.isNotBlank(eventMap, "event")) {
					event = (EventDisposal) eventMap.get("event");
				}
			} else {
				event = eventDisposalService.findEventByIdSimple(eventId);
			}
			if(event != null) {
				pro = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
				eventStatus = event.getStatus();
				
				if(pro != null) {
					instanceId = pro.getInstanceId();
				}
			} else {
				throw new Exception("参数[eventId]："+eventId+" 没有对应的有效事件！");
			}
		}
		
		if(CommonFunctions.isNotBlank(extraParam, "isReject")) {//判断是否是驳回操作
			boolean isReject = Boolean.valueOf(extraParam.get("isReject").toString());
			if(isReject) {
				nextNodeCode = REJECT_NODE_CODE;
			}
		} else if(CommonFunctions.isNotBlank(extraParam, "isRecall")) {
			boolean isRecall = Boolean.valueOf(extraParam.get("isRecall").toString());
			if(isRecall) {
				nextNodeCode = RECALL_NODE_CODE;
			}
		}
		
		eventNextStatus = capEventStatus(initStatusMap(chiefLevel), curNodeCode, nextNodeCode);
		
		if(StringUtils.isNotBlank(eventNextStatus)) {
			eventStatus = eventNextStatus;
		}
		
		//更新事件状态
		EventDisposal eventTmp = new EventDisposal();
		eventTmp.setEventId(event.getEventId());
		
		//优先使用事件配置的处理时限，未配置时，才使用工作流配置的处理时限
		if(StringUtils.isBlank(event.getHandleDateStr()) && START_NODE_CODE.equals(curNodeCode)) {//当前环节为start时，设置事件处理时限
			String urgencyDegree = event.getUrgencyDegree(),
				   eventType = event.getType();
			Date createTime = new Date(),
				 eventHandleDate = null;
			int intervalWeekDays = 0,	//工作日
				intervalNaturalDays = 0;//自然日
			
			eventTmp.setCreateTime(createTime);
			
			if(StringUtils.isNotBlank(urgencyDegree)) {
				orgInfo = orgSocialInfoService.findByOrgId(orgId);
				
				if(orgInfo != null) {
					String intervalDayStr = null,
						   orgCode = orgInfo.getOrgCode();
					
					intervalDayStr = funConfigurationService.changeCodeToValue(ConstantValue.HANDLE_DATE_INTERVAL, ConstantValue.EVENT_HANDLE_DATE_INTERVAL + eventType, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.DIRECTION_UP_FUZZY);
					
					if(StringUtils.isBlank(intervalDayStr)) {
						intervalDayStr = funConfigurationService.changeCodeToValue(ConstantValue.HANDLE_DATE_INTERVAL, urgencyDegree, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
					}
					
					if(StringUtils.isNotBlank(intervalDayStr)) {
						String intervalWeekdayStr = null,		//工作日
							   intervalNaturalDayStr = null,	//自然日
							   WEEK_DAY_INTERVAL = "w",			//工作日间隔类型
							   NATURAL_DAY_INTERVAL = "n";		//自然日间隔类型
						
						intervalDayStr = intervalDayStr.trim().toLowerCase();
						
						if(intervalDayStr.endsWith(WEEK_DAY_INTERVAL)) {
							intervalWeekdayStr = intervalDayStr.replace(WEEK_DAY_INTERVAL, "");
						} else if(intervalDayStr.endsWith(NATURAL_DAY_INTERVAL)) {
							intervalNaturalDayStr = intervalDayStr.replace(NATURAL_DAY_INTERVAL, "");
						} else {
							intervalWeekdayStr = intervalDayStr;
						}
						
						if(StringUtils.isNotBlank(intervalWeekdayStr)) {
							try {
								intervalWeekDays = Integer.valueOf(intervalWeekdayStr);
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
						} else if(StringUtils.isNotBlank(intervalNaturalDayStr)) {
							try {
								intervalNaturalDays = Integer.valueOf(intervalNaturalDayStr);
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
						}
					} 
				}
			}
			
			if(intervalWeekDays > 0) {
				eventHandleDate = holidayInfoService.getWorkDateByAfterWorkDay(createTime, intervalWeekDays);
			} else if(intervalNaturalDays > 0) {
				eventHandleDate = DateUtils.addInterval(createTime, intervalNaturalDays, "00");
			} else if(pro != null) {
				eventHandleDate = holidayInfoService.getWorkDateByAfterWorkDay(pro.getStartTime(), pro.getTimeLimit().intValue());
			}
			
			eventTmp.setHandleDate(eventHandleDate);
			eventTmp.setHandleDateFlag(ConstantValue.LIMIT_TIME_STATUS_NORMAL);
		} else if(ConstantValue.EVENT_STATUS_ARCHIVE.equals(eventStatus) && (event.getFinTime() == null && StringUtils.isBlank(event.getFinTimeStr()))) {//事件结案待评时，设置结案时间
			eventTmp.setFinTime(new Date());
		} else if(ConstantValue.EVENT_STATUS_END.equals(eventStatus)) {
			if(event.getFinTime() == null && StringUtils.isBlank(event.getFinTimeStr())) {
				eventTmp.setFinTime(new Date());
			}
		}
		
		/*--------------------------------设置事件子状态*/
		//置空事件子状态
		String subStatus = "";
		
		if(REJECT_NODE_CODE.equals(nextNodeCode) || RECALL_NODE_CODE.equals(nextNodeCode)) {
			eventTmp.setFinTime(null);//驳回时，清空结案时间
			eventTmp.setFinTimeStr("");//驳回时，清空结案时间
			subStatus = ConstantValue.REJECT_SUB_STATUS;
			
			Map<String, Object> curDataMap = null;	//当前环节信息
			String preNodeCode = null,		//驳回操作前，上一环节的环节编码
				   currentNodeCode = null;	//驳回操作后，当前环节的环节编码
			
			//当前环节信息
			curDataMap = eventDisposalWorkflowService.curNodeData(instanceId);
			
			if(RECALL_NODE_CODE.equalsIgnoreCase(nextNodeCode)) {
				subStatus = ConstantValue.RECALL_SUB_STATUS;
			}
			
			if(CommonFunctions.isNotBlank(curDataMap, "NODE_NAME")) {//获取当前环节编码
				currentNodeCode = curDataMap.get("NODE_NAME").toString();
			}
			
			//获取驳回前的环节编码
			if(CommonFunctions.isNotBlank(extraParam, "preNodeCode")) {
				preNodeCode = extraParam.get("preNodeCode").toString();
			} else {
				UserInfo userInfo = new UserInfo();
				
				if(orgInfo == null) {
					orgInfo = orgSocialInfoService.findByOrgId(orgId);
				}
				
				if(orgInfo != null) {
					userInfo.setOrgCode(orgInfo.getOrgCode());
					userInfo.setOrgName(orgInfo.getOrgName());
				}
				
				//获取所有历史环节
				List<Map<String, Object>> taskMapList = baseWorkflowService.capHandledTaskInfoMap(instanceId, Constants.SQL_ORDER_DESC, null);
				
				if(taskMapList != null && taskMapList.size() > 0) {
					Map<String, Object> taskMap = taskMapList.get(0);
					
					if(CommonFunctions.isNotBlank(taskMap, "PRE_TASK_ID")) {
						Object pretaskIdObj = taskMap.get("PRE_TASK_ID");
						String operateType = null;
						
						for(Map<String, Object> taskMapTmp : taskMapList) {
							operateType = "";
							
							if(CommonFunctions.isNotBlank(taskMapTmp, "OPERATE_TYPE")) {
								operateType = taskMapTmp.get("OPERATE_TYPE").toString();
							}
							
							if(CommonFunctions.isNotBlank(taskMapTmp, "TASK_ID") && pretaskIdObj.equals(taskMapTmp.get("TASK_ID"))) {
								if(OPERATE_TYPE_REJECT.equals(operateType) || OPERATE_TYPE_RECALL.equals(operateType)) {
									if(CommonFunctions.isNotBlank(taskMapTmp, "PRE_TASK_ID")) {
										pretaskIdObj = taskMapTmp.get("PRE_TASK_ID");
									}
								} else if(CommonFunctions.isNotBlank(taskMapTmp, "TASK_CODE")) {
									preNodeCode = taskMapTmp.get("TASK_CODE").toString();
									
									break;
								}
							}
						}
					}
				}
			}
			//获取驳回操作前的事件状态
			eventNextStatus = capEventStatus(initStatusMap(chiefLevel), preNodeCode, currentNodeCode);
			
			if(StringUtils.isNotBlank(eventNextStatus)) {
				eventStatus = eventNextStatus;
			}
		} else if(ConstantValue.HANDLING_TASK_CODE.equals(nextNodeCode)) {//处理中
			subStatus = ConstantValue.HANDLING_STATUS;
		} else if(ConstantValue.ACCEPT_TASK_CODE.equals(nextNodeCode)) {//已受理
			subStatus = ConstantValue.ACCEPT_STATUS;
		}
		eventTmp.setSubStatus(subStatus);
		/*--------------------------------设置事件子状态*/
		
		eventTmp.setStatus(eventStatus);
		eventTmp.setUpdatorId(userId);
		
		Long updateEventId = eventDisposalService.updateEventDisposalById(eventTmp);
		
		if(updateEventId > 0) {//事件更新成功，新增中间过程
			if(!isRecovery) {
				String signUserIKey = curNodeCode + "_" + nextNodeCode + "_signUserId",
					   signOrgIdKey = curNodeCode + "_" + nextNodeCode + "_signOrgId";
				List<Long> userIdList = new ArrayList<Long>(),
						   orgIdList = new ArrayList<Long>();
				EventProcess eventProcess = null;
				
				if(CommonFunctions.isNotBlank(extraParam, signUserIKey) && CommonFunctions.isNotBlank(extraParam, signOrgIdKey)) {
					String[] userIdArray = extraParam.get(signUserIKey).toString().split(","),
							 orgIdArray = extraParam.get(signOrgIdKey).toString().split(",");
					for(int index = 0, len = userIdArray.length; index < len; index++) {
						try {
							userIdList.add(Long.valueOf(userIdArray[index]));
							orgIdList.add(Long.valueOf(orgIdArray[index]));
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
					}
				} else {
					userIdList.add(userId);
					orgIdList.add(orgId);
				}
				
				for(int index = 0, len = userIdList.size(); index < len; index++) {
					eventProcess = new EventProcess();
					eventProcess.setEventId(updateEventId);
					if(handleDate != null) {
						eventProcess.setHandleDate(handleDate);//设置环节处理时限
					}
					eventProcess.setStatus(eventStatus);
					eventProcess.setUserId(userIdList.get(index));
					eventProcess.setOrgId(orgIdList.get(index));
					
					eventDisposalService.saveEventProcess(eventProcess);
				}
			}
		}
		
		return eventStatus;
	}
	
	/**
	 * 添加个人积分
	 * 业务信息必须存在：
	 * 		userId，userName，orgCode，createBy，
	 * 		scoreType 
	 * 			B1 事件处置 1分；事件采集人员与结案人员是相同组织、相同用户id的人员
	 * 			B2 事件处置 2分；事件采集人员与结案人员不是相同组织、相同用户id的人员
	 * 可选信息：dsTime（不存在值时默认当前时间），createTime（不存在值时默认当前时间）
	 * 不填写信息：dsId，updateBy，updateTime
	 */
	protected void addUserDetailScore(Long eventId) {
		UserDetailScore userDetailScore = new UserDetailScore();
		Long createUserId = -1L, createOrgId = -1L, closeUserId = -1L, closeOrgId = -1L, instanceId = -1L;
		String scoreType = null, createUserName = null;
		String SCORE_TYPE_1 = "B1", SCORE_TYPE_2 = "B2";
		
		if(eventId != null && eventId > 0) {
			ProInstance pro = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
			if(pro != null) {
				instanceId = pro.getInstanceId();
				createUserId = pro.getUserId();
				createUserName = pro.getUserName();
				createOrgId = pro.getOrgId();
			}
		}
		
		if(StringUtils.isBlank(createUserName)) {
			UserBO userBO = userManageService.getUserInfoByUserId(createUserId);
			if(userBO != null && userBO.getUserId() != null && userBO.getUserId() > 0) {
				createUserName = userBO.getPartyName();
			} else {
				createUserId = -1L;
			}
		}
		
		if(createOrgId != null && createOrgId > 0) {
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(createOrgId);
			if(orgInfo != null && orgInfo.getOrgId() != null && orgInfo.getOrgId() > 0) {
				userDetailScore.setOrgCode(orgInfo.getOrgCode());
			} else {
				createOrgId = -1L;
			}
		}
		
		if(instanceId != null && instanceId > 0) {
			Map<String, Object> taskMap = eventDisposalWorkflowService.findDoneTaskInfoLatest(instanceId, CLOSE_NODE_CODE);
			if(CommonFunctions.isNotBlank(taskMap, "transactorId")) {
				try {
					closeUserId = Long.valueOf(taskMap.get("transactorId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			if(CommonFunctions.isNotBlank(taskMap, "orgId")) {
				try {
					closeOrgId = Long.valueOf(taskMap.get("orgId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(createUserId != null && createUserId > 0 && createOrgId != null && createOrgId > 0 &&
		   closeUserId != null && closeUserId > 0 && closeOrgId != null && closeOrgId > 0) {
			if(closeUserId.equals(createUserId) && closeOrgId.equals(createOrgId)) {
				scoreType = SCORE_TYPE_1;
			} else {
				scoreType = SCORE_TYPE_2;
			}
			
			userDetailScore.setUserId(createUserId);
			userDetailScore.setUserName(createUserName);
			userDetailScore.setCreateBy(createUserId);
			userDetailScore.setScoreType(scoreType);
			userDetailScore.setDsTime(new Date());//当前时间
			userDetailScore.setRemark(String.valueOf(eventId));
			
			//获取事件详情
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			if(event!=null) {
				userDetailScore.setSourceRemark(event.getEventName()+"("+event.getHappenTimeStr()+")");//事件为：事件名称+上报时间（年月日时分秒）。
			}
			
			userScoreService.insertUserDetailScore(userDetailScore);
		}
	}
}
