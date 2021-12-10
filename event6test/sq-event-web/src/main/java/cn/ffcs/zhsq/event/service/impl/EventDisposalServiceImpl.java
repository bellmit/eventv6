package cn.ffcs.zhsq.event.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.ffcs.cache.redis.service.ManipulateRedisService;
import cn.ffcs.cookie.service.CacheService;
import cn.ffcs.gis.base.service.IResMarkerService;
import cn.ffcs.gis.mybatis.domain.base.ResMarker;
import cn.ffcs.resident.bo.PartyIndividual;
import cn.ffcs.resident.service.PartyIndividualService;
import cn.ffcs.rmq.producer.RmqProducer;
import cn.ffcs.shequ.eventReportRecord.domain.EventReportRecordInfo;
import cn.ffcs.shequ.eventReportRecord.service.IEventReportRecordService;
import cn.ffcs.shequ.json.JsonUtils;
import cn.ffcs.shequ.mybatis.domain.zzgl.dispute.DisputeMediation;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EventInter;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.WindowHelper;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.dispute.IDisputeMediationService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.holidayinfo.IHolidayInfoService;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.event.service.IIncidentInvolvedService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.eventExpand.service.IEventLabelRelaService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.EventProcess;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEvents;
import cn.ffcs.zhsq.mybatis.persistence.event.EventDisposalMapper;
import cn.ffcs.zhsq.mybatis.persistence.event.EventOverviewMapper;
import cn.ffcs.zhsq.mybatis.persistence.event.EventProcessMapper;
import cn.ffcs.zhsq.relatedEvents.service.ICommonRelatedEventsService;
import cn.ffcs.zhsq.relatedEvents.service.IRelatedEventsService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.EventNoUtil;
import cn.ffcs.zhsq.utils.ReadProperties;

@Service(value = "eventDisposalServiceImpl")
public class EventDisposalServiceImpl implements IEventDisposalService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private EventDisposalMapper eventDisposalMapper;
	
	@Autowired
	private EventProcessMapper eventProcessMapper;

	@Autowired
	private IInvolvedPeopleService involvedPeopleService;

	@Autowired
	private IBaseDictionaryService dictionaryService;

	@Autowired
	protected IIncidentInvolvedService incidentInvolvedService;

	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;

	// 工作流调用接口
	@Autowired
	private HessianFlowService hessianFlowService;

	@Autowired
	private IResMarkerService resGisMarkerService;
	
	@Autowired
	private IFunConfigurationService configurationService;
	
	@Autowired
	private EventOverviewMapper eventOverviewMapper;
	
	@Autowired
	private IArcgisInfoService arcgisInfoService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventReportRecordService eventReportRecordService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService; 
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private ManipulateRedisService manipulateRedisService;
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private IAttachmentService attachmentService;

	@Autowired
	private PartyIndividualService partyIndividualService;
	
	@Autowired
	private IHolidayInfoService holidayInfoService;
	
	@Autowired
	private IEventExtendService eventExtendService;
	
	@Autowired
	private IEvent4WorkflowService event4WorkflowService;

	//（通用的案事件）重特大案事件（bizType 3）、师生安全案（事）件（bizType 2）
	@Autowired
	private ICommonRelatedEventsService commonRelatedEventsService;

	//命案防控、路案事件（bizType 1）、
	@Autowired
	private IRelatedEventsService relatedEventsService;//涉事案件服务

	//矛盾纠纷
	@Autowired
	private IDisputeMediationService disputeMediationService;

	@Autowired
	private IEventLabelRelaService eventLabelRelaService;
	
	@Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;
	
	String dataBaseUser = ReadProperties.javaLoadProperties("event.default.database", "global.properties");//数据库用户名
	String workFlowTypeId = ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID;//事件工作流标识
	String workFlowStatusStart = ConstantValue.EVENT_WORKFLOW_STATUS_START;//工作流流程状态 启动
	String workFlowStatusEnd = ConstantValue.EVENT_WORKFLOW_STATUS_END;//工作流流程状态 结束
	
	//RMQ事件状态变化监听配置
    private String RMQ_IP = ReadProperties.javaLoadProperties("rmq.ip", "global.properties");//RMQ的ip地址
    private String RMQ_PORT = ReadProperties.javaLoadProperties("rmq.port", "global.properties");//RMQ的端口
    private String RMQ_VHOST_EVENT = ReadProperties.javaLoadProperties("rmq.vhost_event_status", "global.properties");//RMQ的虚拟主机
    private String RMQ_EXCHANGESTR_EVENT = ReadProperties.javaLoadProperties("rmq.exchangeStr_event_status", "global.properties");//RMQ的交换机
    private String RMQ_IS_OPEN = ReadProperties.javaLoadProperties("event.isOpenStatusMonitor", "global.properties");//开启监听推送的区域
	
    @Override
	public int deleteEventDisposalByIds(List<Long> eventIdList,Long updatorId) {
		int result = 0;
		for(Long eventId : eventIdList) {
			result += eventDisposalMapper.delete(eventId,updatorId);
		}
		
		List<EventDisposal> eventList = new ArrayList<EventDisposal>();
		
		// 是否发送rmq消息
		Boolean isContain = false;
		// 获取是否需要发送rmq信息
		if (StringUtils.isNotBlank(RMQ_IS_OPEN)) {
			Map<String, Object> searchParam = new HashMap<String, Object>();
			searchParam.put("isIgnoreStatus", true);
			for (Long eventId : eventIdList) {
				EventDisposal findEvent = null;
				try {
					Map<String, Object> findEventByMap = this.findEventByMap(eventId, searchParam);
					if (CommonFunctions.isNotBlank(findEventByMap, "event")) {
						findEvent = (EventDisposal) findEventByMap.get("event");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (findEvent != null) {
					eventList.add(findEvent);
				}
			}

			String[] infoOrgCodeArr = RMQ_IS_OPEN.split("-");

			for (String code : infoOrgCodeArr) {
				if (eventList.size() > 0 && eventList.get(0).getGridCode().startsWith(code)) {
					isContain = true;
				}
			}

		}
		
		//事件删除成功，且需要推送消息
		if(result>0 && isContain) {
			
			Map<String, Object> params = new HashMap<String, Object>();
            params.put("eventFlag", "delete");
            params.put("eventIdList",eventIdList);
            params.put("deleteEvent", eventList);
			//发送rmq消息
			try {
				sendEventStatusRmqMsg(params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
	}

	/**
	 * 依据事件id查找事件信息
	 * @param eventId
	 * @return
	 */
	@Override
	public EventDisposal findEventByIdSimple(Long eventId) {
		EventDisposal event = null;
		
		if(eventId != null && eventId > 0) {
			Map<String, Object> eventMap = new HashMap<String, Object>();
			eventMap.put("eventId", eventId);
			
			event = eventDisposalMapper.findById(eventMap);
			formatEvent(event);
		}
		
		return event;
	}
	
	@Override
	public EventDisposal findEventById(Long eventId, String orgCode) {
		return this.findEventByIdAndMapt(eventId, null, orgCode);
	}
	
	@Override
	public EventDisposal findEventByIdAndMapt(Long eventId, String mapType, String orgCode) {
		EventDisposal event = this.findEventByIdSimple(eventId);
		if(event != null) {
			ResMarker resMarker = new ResMarker();
			resMarker.setResourcesId(event.getEventId());
			resMarker.setMarkerType(ConstantValue.MARKER_TYPE_EVENT);
			List<ResMarker> resMarkers = resGisMarkerService.findResMarkerByParam(ConstantValue.MARKER_TYPE_EVENT, event.getEventId(), mapType);
			if(resMarkers.size()>0){
				event.setResMarker(resMarkers.get(0));
			}
			
			List<EventDisposal> eventList = new ArrayList<EventDisposal>();
			eventList.add(event);
			
			formatData(eventList, orgCode);
		}
		return event;
	}
	
	@Override
	public Map<String, Object> findEventByMap(Long eventId, Map<String, Object> params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		EventDisposal event = null;

		//测试参数
		//params.put("isCapEventLabelInfo",true);

		if(eventId != null && eventId > 0) {
			Map<String, Object> eventMap = new HashMap<String, Object>();
			boolean isIgnoreStatus = false;
			
			if(CommonFunctions.isNotBlank(params, "isIgnoreStatus")) {
				isIgnoreStatus = Boolean.valueOf(params.get("isIgnoreStatus").toString());
			}
			
			eventMap.put("eventId", eventId);
			
			if(isIgnoreStatus) {
				eventMap.put("isIgnoreStatus", isIgnoreStatus);
			}
			
			event = eventDisposalMapper.findById(eventMap);
			
			formatEvent(event);
		} else {
			throw new IllegalArgumentException("缺少有效的事件id！");
		}
		
		if(event != null) {
			boolean isCapMapInfo = false, isCapEventLabelInfo = false, isCapEventExtend = false;
			String userOrgCode = null;
			
			if(CommonFunctions.isNotBlank(params, "isCapMapInfo")) {
				isCapMapInfo = Boolean.valueOf(params.get("isCapMapInfo").toString());
			}
			if(CommonFunctions.isNotBlank(params, "isCapEventLabelInfo")) {
				isCapEventLabelInfo = Boolean.valueOf(params.get("isCapEventLabelInfo").toString());
			}
			if(CommonFunctions.isNotBlank(params, "isCapEventExtend")) {
				isCapEventExtend = Boolean.valueOf(params.get("isCapEventExtend").toString());
			}
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			}
			
			if(isCapMapInfo) {
				if(CommonFunctions.isNotBlank(params, "mapType")) {
					ResMarker resMarker = new ResMarker();
					resMarker.setResourcesId(event.getEventId());
					resMarker.setMarkerType(ConstantValue.MARKER_TYPE_EVENT);
					List<ResMarker> resMarkers = resGisMarkerService.findResMarkerByParam(ConstantValue.MARKER_TYPE_EVENT, eventId, params.get("mapType").toString());
					if(resMarkers.size()>0){
						event.setResMarker(resMarkers.get(0));
					}
				} else {
					throw new IllegalArgumentException("缺少地图类型属性【mapType】");
				}
			}
			
			if(isCapEventLabelInfo) {
				//获取事件有效的（IS_VALID = 1）标签信息
				List<Map<String,Object>> labelMapList =  null;
				Map<String,Object> labelContentMap = null;
				Long bizId = -1L;
				String  labelName = null,
						isLabelValid = "1",
						majorRelatedEvents = "majorRelatedEvents",
						careRoads = "careRoads",
						schoolRelatedEvents = "schoolRelatedEvents",
						homicideCase = "homicideCase",
						disputeMediation = "disputeMediation";

				params.put("isValid",isLabelValid);
				labelMapList = eventLabelRelaService.searchByEventId(eventId,params);

				if(null != labelMapList && labelMapList.size() > 0){
					for(Map labelMap:labelMapList){
						if(CommonFunctions.isNotBlank(labelMap,"LABEL_NAME")){
							labelName = String.valueOf(labelMap.get("LABEL_NAME"));
							bizId = Long.valueOf(String.valueOf(labelMap.get("BIZ_ID")));

							//获取标签内容:1.重特大案事件
							if(majorRelatedEvents.equals(labelName)){
								resultMap.put("majorRelatedEvents",commonRelatedEventsService.findRelatedEventsById(bizId,userOrgCode));
							}
							//路案事件
							if(careRoads.equals(labelName)){
								RelatedEvents careRoadsBean = null;
								BeanMap careRoadsBeanMap = null;
								Map<String,Object> careRoadsMap = new HashMap<>();
								String suspectType = InvolvedPeople.BIZ_TYPE.CARE_ROADS.getBizType();//"12"
								List<InvolvedPeople> suspectList = null;

								careRoadsBean = relatedEventsService.findRelatedEventsById(bizId,userOrgCode);
								if(null != careRoadsBean){
									careRoadsBeanMap = new BeanMap(careRoadsBean);
									careRoadsMap.putAll(careRoadsBeanMap);

									//查找路案事件主要嫌疑人 bizId bizType
									suspectList = involvedPeopleService.findInvolvedPeopleListByBizAndExtPar(bizId,suspectType,params);
									if(null != careRoadsMap && null != suspectList && suspectList.size() > 0){
										careRoadsMap.put("suspectList",suspectList);
									}
								}
								resultMap.put("careRoads",careRoadsMap);
							}
							//师生案事件
							if(schoolRelatedEvents.equals(labelName)){
								RelatedEvents schoolRelatedEventsBean = null;
								BeanMap schoolRelatedEventsBeanMap = null;
								Map<String,Object> schoolRelatedEventsMap = new HashMap<>();
								String suspectType = InvolvedPeople.BIZ_TYPE.SCHOOL_RELATED_EVENTS.getBizType();//"13"
								List<InvolvedPeople> suspectList = null;

								schoolRelatedEventsBean = relatedEventsService.findRelatedEventsById(bizId,userOrgCode);
								if(null != schoolRelatedEventsBean){
									schoolRelatedEventsBeanMap = new BeanMap(schoolRelatedEventsBean);
									schoolRelatedEventsMap.putAll(schoolRelatedEventsBeanMap);

									//查找涉及师生案件主要嫌疑人 bizId bizType
									suspectList = involvedPeopleService.findInvolvedPeopleListByBizAndExtPar(bizId,suspectType,params);
									if(null != schoolRelatedEventsMap && null != suspectList && suspectList.size() > 0){
										schoolRelatedEventsMap.put("suspectList",suspectList);
									}
								}

								resultMap.put("schoolRelatedEvents",schoolRelatedEventsMap);
							}

							//命案防控
							if(homicideCase.equals(labelName)){
								RelatedEvents homicideCaseBean = null;
								BeanMap homicideCaseBeanMap = null;
								Map<String,Object> homicideCaseMap = new HashMap<>();
								String suspectType = InvolvedPeople.BIZ_TYPE.HOMICIDE_SUSPECT.getBizType();//"03"
								String victimType = InvolvedPeople.BIZ_TYPE.HOMICIDE_VITICM.getBizType();//"04";
								List<InvolvedPeople> suspectList = null;
								List<InvolvedPeople> victimList = null;

								homicideCaseBean = relatedEventsService.findRelatedEventsById(bizId,userOrgCode);

								if(null != homicideCaseBean){
									homicideCaseBeanMap = new BeanMap(homicideCaseBean);
									homicideCaseMap.putAll(homicideCaseBeanMap);

									//查找命案防控嫌疑人/受害人信息 bizId bizType
									//嫌疑人
									suspectList = involvedPeopleService.findInvolvedPeopleListByBizAndExtPar(bizId,suspectType,params);
									if(null != homicideCaseMap && null != suspectList && suspectList.size() > 0){
										homicideCaseMap.put("suspectList",suspectList);
									}
									//受害人
									victimList = involvedPeopleService.findInvolvedPeopleListByBizAndExtPar(bizId,victimType,params);
									if(null != homicideCaseMap &&  null != victimList && victimList.size() > 0){
										homicideCaseMap.put("victimList",victimList);
									}
									resultMap.put("homicideCase",homicideCaseMap);
								}
							}
							//矛盾纠纷
							if(disputeMediation.equals(labelName)){
								DisputeMediation disputeMediationBo = disputeMediationService.selectByPrimaryKey(bizId);
								BeanMap disputeMediationBeanMap = null;
								Map<String,Object> disputeMediationMap = new HashMap<>();
								String suspectType = InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType();//"06"，矛盾纠纷当事人
								String peopleInChargeType = InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION_PEOPLE_IN_CHARGE.getBizType();//"14",矛盾纠纷化解责任人
								List<InvolvedPeople> suspectList = null;
								List<InvolvedPeople> peopleInChargeList = null;

								//翻译矛盾纠纷模块事件规模字典
								List<BaseDataDict> disputeScaleDictList = dictionaryService.getDataDictListOfSinglestage(ConstantValue.DISPUTE_SCALE_CODE, userOrgCode);
								DataDictHelper.setDictValueForField(disputeMediationBo, "disputeScale", "disputeScaleStr", disputeScaleDictList);

								if(null != disputeMediationBo){
									disputeMediationBeanMap = new BeanMap(disputeMediationBo);
									disputeMediationMap.putAll(disputeMediationBeanMap);

									//矛盾纠纷当事人信息
									suspectList = involvedPeopleService.findInvolvedPeopleListByBizAndExtPar(bizId,suspectType,params);
									if(null != disputeMediationMap && null != suspectList && suspectList.size() > 0){
										disputeMediationMap.put("suspectList",suspectList);
									}
									//矛盾纠纷化解责任人
									peopleInChargeList = involvedPeopleService.findInvolvedPeopleListByBizAndExtPar(bizId,peopleInChargeType,params);
									if(null != disputeMediationMap && null != peopleInChargeList && peopleInChargeList.size() > 0){
										disputeMediationMap.put("peopleInChargeList",peopleInChargeList);
									}
								}
								
								resultMap.put("disputeMediation",disputeMediationMap);
							}
						}
					}
				}
			}
			
			if(isCapEventExtend) {
				Map<String, Object> eventExtendMap = null,
									eventExtendParam = new HashMap<String, Object>();
				
				eventExtendParam.put("isFormat", "1");
				eventExtendParam.put("orgCode", userOrgCode);
				
				eventExtendMap = eventExtendService.findEventExtendByEventId(eventId, eventExtendParam);
				if(eventExtendMap != null && !eventExtendMap.isEmpty()) {
					event.setEventExtend(eventExtendMap);
				}
			}
			
			if(StringUtils.isNotBlank(userOrgCode)) {
				List<EventDisposal> eventList = new ArrayList<EventDisposal>();
				eventList.add(event);
				
				formatData(eventList, userOrgCode);
			}
			
			resultMap.put("event", event);
		} else {
			throw new IllegalArgumentException("事件id【" + eventId + "】没有有效的事件信息！");
		}
		
		return resultMap;
	}

	/**
	 * 获取事件记录数（个人待办数、结案数、结案率、处理中数、超时未结案数）
	 * @param param
	 * @return
	 */
	@Override
	public Map<String, Object> findEventCount(Map<String, Object> params) {
		if(CommonFunctions.isBlank(params, "gridId")){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", "[gridId] is empty!");
			return map;
		}
		if(CommonFunctions.isBlank(params, "orgId")){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", "[orgId] is empty!");
			return map;
		}
		if(CommonFunctions.isBlank(params, "userId")){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", "[userId] is empty!");
			return map;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("gridId", params.get("gridId"));
		
		List<String> statusList = new ArrayList<String>();
		statusList.add(ConstantValue.EVENT_STATUS_RECEIVED);//00 
		statusList.add(ConstantValue.EVENT_STATUS_REPORT);//01
		statusList.add(ConstantValue.EVENT_STATUS_DISTRIBUTE);//02 
		param.put("statusList", statusList);
		int todo = eventDisposalMapper.findCountByCriteria(param);
		resultMap.put("todo", todo);//待办数
		param.remove("statusList");

		statusList = new ArrayList<String>();
		statusList.add(ConstantValue.EVENT_STATUS_ARCHIVE);//03 
		statusList.add(ConstantValue.EVENT_STATUS_END);//04
		param.put("statusList", statusList);
		int done = eventDisposalMapper.findCountByCriteria(param);
		resultMap.put("done", done);//结案数
		param.remove("statusList");

		int doneRate = (done + todo) == 0 ? 0 : (done * 100 / (done + todo)); 
		resultMap.put("doneRate", doneRate);//结案率

		statusList = new ArrayList<String>();
		statusList.add(ConstantValue.EVENT_STATUS_RECEIVED);//00 
		statusList.add(ConstantValue.EVENT_STATUS_REPORT);//01
		statusList.add(ConstantValue.EVENT_STATUS_DISTRIBUTE);//02 
		param.put("statusList", statusList);
		param.put("handleStatus", ConstantValue.EVENT_TODO_HANDLE_STATUS);//01 超时待办
		int handleTodo = eventDisposalMapper.findCountByCriteria(param);
		resultMap.put("handleTodo", handleTodo);//超时待办数
		param.remove("statusList");
		
		StringBuffer eventWhereSql = addWhereSql(params);
		cn.ffcs.system.publicUtil.EUDGPagination pagination = hessianFlowService.queryNewTaskList(1,
				1, Long.valueOf(params.get("orgId").toString()), null,
				null, null, Long.valueOf(params.get("userId").toString()),
				null, dataBaseUser + ".T_EVENT", "E.EVENT_ID", "E.EVENT_ID",
				workFlowTypeId, eventWhereSql.toString(), null,"WF_CREATE_ DESC");
		resultMap.put("individualTodo", pagination.getTotal());//个人待办数
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> findEventCountA(Map<String, Object> params) throws Exception {
		Map<String, Object> info = new HashMap<String, Object>();
		if (StringUtils.isBlank(WindowHelper.getPmStr(params, "gridId"))) {
			throw new Exception("缺少[gridId]参数");
		}
		String infoOrgCode = WindowHelper.getPmStr(params, "infoOrgCode");
		if (StringUtils.isBlank(infoOrgCode)) {
			throw new Exception("缺少[infoOrgCode]参数");
		}
		if (!"Y".equals(WindowHelper.getPmStr(params, "isFast"))) {
			List<Map<String, Object>> details = eventOverviewMapper.findEventCountB(params);
			info.put("details", details);
		}
		Map<String, Object> overview = eventOverviewMapper.findEventCountA(params);
		List<Map<String, Object>> mylDetails = eventOverviewMapper.findEventCountForMyL(params);
		if (mylDetails != null && mylDetails.size() > 0) {
			overview.putAll(mylDetails.remove(0));
			info.put("mylDetails", mylDetails);
		}
		params.put("superviseMark", "1");
		params.put("infoOrgCode", infoOrgCode);
		params.put("createTimeStart", params.get("startTime"));
		params.put("createTimeEnd", params.get("endTime"));
		params.put("listType", "5");
		int dubanCount = event4WorkflowService.findEventCount(params);
		// findRemindEventWorkFlowPagination(1, 1, params).getTotal();
		overview.put("DUBAN", dubanCount);
		info.put("overview", overview);
		return info;
	}
	
	@Override
	public Map<String, Object> findEventCountB(Map<String, Object> params)
			throws Exception {
		if (StringUtils.isBlank(WindowHelper.getPmStr(params, "gridId"))) {
			throw new Exception("缺少[gridId]参数");
		}
		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("dataList", eventOverviewMapper.findEventCountC(params));
		infoMap.put("dataDetails", eventOverviewMapper.findEventCountD(params));
		return infoMap;
	}
	
	private void excTypes(Map<String, Object> params){
		if(CommonFunctions.isNotBlank(params, "types")) {
			boolean isRemoveTypes = false;
			String types = params.get("types").toString().trim();
			
			if(StringUtils.isNotBlank(types)) {
				if(CommonFunctions.isNotBlank(params, "isRemoveTypes")) {
					isRemoveTypes = Boolean.valueOf(params.get("isRemoveTypes").toString());
				}
				
				if(isRemoveTypes) {
					params.put("removeTypeArray", types.split(","));
				} else {
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
			}
			
		}
		
		if(CommonFunctions.isNotBlank(params, "eventLabel")) {
			Object evaLevelListObj = params.get("eventLabel");
			if(evaLevelListObj instanceof String) {
				params.put("eventLabelList", Arrays.asList(evaLevelListObj.toString().split(",")));
			}
		}
	}

	@Override
	public EUDGPagination findTodoViewPagination(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo<1?1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		
		excTypes(params);
		
		int count = eventDisposalMapper.findTodoCountViewByCriteria(params);
		List<EventDisposal> list = eventDisposalMapper.findPageTodoViewByCriteria(params, rowBounds);
		if(list.size()>0){
			formatEvent(list, params);
		}
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}
	
	@Override
	public EUDGPagination findEventByInvolvedPeoplePagination(int pageNo,
			int pageSize, Map<String, Object> params) throws Exception {
		StringBuffer wrongMsg = new StringBuffer("");
		List<String> eventStatusList = null,
							eventDefaultStatusList = new ArrayList<String>() {
			{
				add(ConstantValue.EVENT_STATUS_RECEIVED);
				add(ConstantValue.EVENT_STATUS_REPORT);
				add(ConstantValue.EVENT_STATUS_DISTRIBUTE);
				add(ConstantValue.EVENT_STATUS_ARCHIVE);
				add(ConstantValue.EVENT_STATUS_END);
			}
			
		};
		
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			wrongMsg.append("缺少参数：地域编码[infoOrgCode]！");
		}
		if(CommonFunctions.isBlank(params, "idCard")) {
			wrongMsg.append("缺少参数：身份证号[idCard]！");
		}
		if(CommonFunctions.isBlank(params, "orgCode")) {
			wrongMsg.append("缺少参数：组织编码[orgCode]！");
		}
		if(CommonFunctions.isNotBlank(params, "eventStatusList")) {
			try {
				String[] eventStatusArray = (String[])params.get("eventStatusList");
				eventStatusList = Arrays.asList(eventStatusArray);
			} catch(ClassCastException e) {
				try {
					eventStatusList = (List<String>)params.get("eventStatusList");
				} catch(ClassCastException e1) {
					wrongMsg.append("参数：事件状态[eventStatusList]，不是String[]类型！");
				}
			}
		} else if(CommonFunctions.isNotBlank(params, "eventStatus")) {
			eventStatusList = new ArrayList<String>();
			eventStatusList.add(params.get("eventStatus").toString());
		}
		if(wrongMsg.length() > 0) {
			throw new IllegalArgumentException(wrongMsg.toString());
		}
		
		pageNo = pageNo<1?1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		
		if(eventStatusList != null && eventStatusList.size() > 0) {
			eventDefaultStatusList.retainAll(eventStatusList);
		}
		
		params.put("eventStatusList", eventDefaultStatusList);
		
		if(CommonFunctions.isBlank(params, "involvedPeopleBizType")) {
			params.put("involvedPeopleBizType", InvolvedPeople.BIZ_TYPE.ZHSQ_EVENT.getBizType());
		}
		
		int count = eventDisposalMapper.countEventByInvolvedPeople(params);
		List<EventDisposal> list = null;
		
		if(count > 0) {
			list = eventDisposalMapper.findEventByInvolvedPeople(params, rowBounds);
			if(list != null && list.size( )> 0){
				formatEvent(list, params);
			}
		} else {
			list = new ArrayList<EventDisposal>();
		}
		
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		
		return eudgPagination;
	}

	@Override
	public EUDGPagination findHistoryViewPagination(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo<1?1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		
		excTypes(params);
		formatParamIn(params);
		
		int count = eventDisposalMapper.findCountHistoryViewByCriteria(params);
		List<EventDisposal> list = eventDisposalMapper.findPageHistoryViewByCriteria(params, rowBounds);
		if(list.size()>0){
			formatEvent(list, params);
		}
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}
	
	@Override
	public EUDGPagination findEventDataForCommon(int pageNo, int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 20 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);

		excTypes(params);

		int count = eventDisposalMapper.countEventDataForCommon(params);
		List<EventDisposal> list = eventDisposalMapper.findEventDataForCommon(params, rowBounds);
		if (list.size() > 0) {
			formatEvent(list, params);
		}
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}

	@Override
	public EUDGPagination findEventDisposalPagination(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo<1?1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		
		excTypes(params);
		
		int count = eventDisposalMapper.findCountByCriteria(params);
		List<EventDisposal> list = eventDisposalMapper.findPageListByCriteria(params, rowBounds);
		
		formatEvent(list, params);
		
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		return eudgPagination;
	}

	@Override
	public int findCountByCriteria(Map<String, Object> params){
		return eventDisposalMapper.findCountByCriteria(params);
	}

	@Override
	public Map<String, Object> findEvent4Year(Map<String, Object> params){
		return eventDisposalMapper.findEvent4Year(params);
	}

	@Override
	public List<Map<String, Object>> findTop10OfEventType4Month(Map<String, Object> params){
		Map<String, Object> dictMap = new HashMap<String, Object>();
//		dictMap.put("orgCode", orgCode);
		dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
		List<BaseDataDict> eventTypeDict = dictionaryService.findDataDictListByCodes(dictMap);


		List<Map<String, Object>> result = eventDisposalMapper.findTop10OfEventType4Month(params);
		for(Map<String, Object> map : result){
			for(BaseDataDict dataDict : eventTypeDict){
				if(dataDict.getDictGeneralCode().equals(map.get("EVENT_TYPE"))){
					//System.out.println(dataDict.getDictGeneralCode() + "--" + map.get("EVENT_TYPE"));
					map.put("TYPE_NAME",dataDict.getDictName());break;
				}
			}

		}

		return result;
	}

	@Override
	public List<Map<String, Object>> findGrid4Month(Map<String, Object> params){
		return eventDisposalMapper.findGrid4Month(params);
	}

	@Override
	public int findCountByEventType(Map<String, Object> params){
		return eventDisposalMapper.findCountByEventType(params);
	}

	@Override
	public Long saveEventDisposalReturnId(EventDisposal event, Map<String, Object> params, UserInfo userInfo) {

		//保存事件
		Long eventId = -1L;
		boolean isAble2Operate = true;
		
		if(CommonFunctions.isNotBlank(params, "redisKey")) {
			String redisKey = params.get("redisKey").toString();
			String redisValue = manipulateRedisService.valueGet(redisKey, ConstantValue.JEDIS_DB_EVENT_ADDRESS);
			
			if(StringUtils.isNotBlank(redisValue)) {
				isAble2Operate = Boolean.valueOf(redisValue);
			} else {
				manipulateRedisService.valueSetExpire(redisKey, false, 10L, TimeUnit.MINUTES, ConstantValue.JEDIS_DB_EVENT_ADDRESS);
			}
		}
		
		if(isAble2Operate) {
			eventId = this.saveEventDisposalReturnId(event,userInfo);
		} else {
			throw new IllegalArgumentException("该记录已上报为事件，无需重复上报！");
		}
		
		//保存涉事人员，目前晋江特有
		this.saveEventInvolvedPersion(eventId,params);
		//测试参数
		//params.put("isCapEventLabelInfo",true);

		//事件保存成功后 保存重特大案事件、命案防控、矛盾纠纷排查化解、涉及师生安全、路案事件（目前晋江特有）
		this.saveEventLabelContent(event,userInfo,params);
		
		//保存拓展表信息
		this.saveEventExtendInfo(params, eventId);
		
		//保存事件相关标签信息
		this.saveEventLabelRelaInfo(params, eventId,userInfo);
		
		return eventId;
	}

	private void saveEventExtendInfo(Map<String,Object> params,Long eventId) {
		
		Map<String, Object> eventExtend = new HashMap<String, Object>();
		
		eventExtend.put("eventId", eventId);
		
		if(CommonFunctions.isNotBlank(params, "parentEventId")) {
			Long parentEventId = -1L;
			
			try {
				parentEventId = Long.valueOf(params.get("parentEventId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
			if(parentEventId > 0) {
				eventExtend.put("parentEventId", parentEventId);
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "patrolType")) {
			String patrolType=String.valueOf(params.get("patrolType"));
			eventExtend.put("patrolType", patrolType);
		}
		
		if(CommonFunctions.isNotBlank(params, "pointSelection")) {
			eventExtend.put("pointSelection", params.get("pointSelection"));
		}
		
		if(CommonFunctions.isNotBlank(params, "isNotice")) {
			eventExtend.put("isNotice", params.get("isNotice"));
		}
		
		if(eventExtend.size()>1) {
			
			try {
				eventExtendService.saveEventExtend(eventExtend);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void saveEventLabelRelaInfo(Map<String,Object> params,Long eventId,UserInfo userInfo) {
		
		if(eventId!=null&&eventId>0) {
			if(CommonFunctions.isNotBlank(params, "eventLabelIds")) {
				String[] eventLabelIdArr= {};
				try {
					eventLabelIdArr = params.get("eventLabelIds").toString().split(",");
				} catch (Exception e) {
					e.printStackTrace();
				}
				for (String eventLabelId : eventLabelIdArr) {
					
					try {
						Map<String, Object> eventLabelRela = new HashMap<String, Object>();
						
						eventLabelRela.put("eventId", eventId);
						eventLabelRela.put("bizId", eventId);
						eventLabelRela.put("labelId", eventLabelId);
						eventLabelRela.put("labelName", Long.valueOf(eventLabelId));
						eventLabelRelaService.saveOrUpdate(eventLabelRela, userInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}

	@Override
	public Long saveEventDisposalReturnId(EventDisposal event, UserInfo userInfo) {
		int rela = 0;
		try {
			String happenTimeStr = event.getHappenTimeStr(),
				   handleDateStr = event.getHandleDateStr(),
				   creatorName = event.getCreatorName(),
				   userPartyName = null,
				   userOrgCode = null;
			Date happenTime = event.getHappenTime(),
				 handleDate = event.getHandleDate();
			Long creatorId = event.getCreatorId(),
				 userId = null;
			
			if(userInfo != null) {
				userId = userInfo.getUserId();
				userPartyName = userInfo.getPartyName();
				userOrgCode = userInfo.getOrgCode();
			}
			
			if((creatorId == null || creatorId < 0) && (userId != null && userId > 0)) {
				creatorId = userId;
			}
			if(StringUtils.isBlank(creatorName) && StringUtils.isNotBlank(userPartyName)) {
				creatorName = userPartyName;
			}
			
			if(happenTime == null) {
				if (StringUtils.isBlank(happenTimeStr)){
					event.setHappenTime(new Date());
				} else {
					event.setHappenTime(DateUtils.convertStringToDate(event.getHappenTimeStr(), DateUtils.PATTERN_24TIME));
				}
			}
			if((handleDate == null) && StringUtils.isNotBlank(handleDateStr)) {
				event.setHandleDate(DateUtils.convertStringToDate(event.getHandleDateStr(), DateUtils.PATTERN_24TIME));
			}
			if (StringUtils.isNotBlank(event.getEndTimeStr())){
				event.setEndTime(DateUtils.convertStringToDate(event.getEndTimeStr(), DateUtils.PATTERN_24TIME));
			}
			if (StringUtils.isBlank(event.getEventName()) && StringUtils.isNotBlank(event.getTypeName())){
				event.setEventName(event.getTypeName() + "事件");
			}
			if (StringUtils.isBlank(event.getUrgencyDegree())){
				event.setUrgencyDegree(ConstantValue.URGENCY_DEGREE_1);
			}
			if (StringUtils.isBlank(event.getBizPlatform())) {
				event.setBizPlatform("0");
			}
			//event.setCode(EventNoUtil.getEventNO(null, ConstantValue.SEQ_EVENT_CODE, event.getType())); //--事件编码
			event.setCode(EventNoUtil.getEventNoByRedis(null, ConstantValue.SEQ_EVENT_CODE, event.getType())); //--事件编码redis方式获取
			
			//当为重点人员和消防安全，设置事件描述为小类名称
			/*if ("0205".equals(event.getType())||"0602".equals(event.getType())) {
				event.setContent(dictionaryService.getTableColumnText(ConstantValue.TABLE_EVENT_DISPOSAL, 
						ConstantValue.COLUMN_EVENT_DISPOSAL_SMALL_TYPE, event.getType()));//设置事件描述
			}*/
			
			event.setCreatorId(creatorId);
			event.setCreatorName(creatorName);
			
			formatEvent4Save(event);
			
			///**
			rela = eventDisposalMapper.insert(event);
			
			//保存定位信息
			this.saveEventResmarker(event);
			
			//保存涉事人员
			this.saveEventInvolvedPersion(event);
			
			//保存上报关联信息
			this.saveEventReportRecordInfo(event, userOrgCode);
			
			//保存额外的附件信息
			this.saveEventAttachment(event);
			
		} catch (Exception e) {
			logger.error("新增事件出错!");
			e.printStackTrace();
		}
		return (rela>0)?event.getEventId():-1L;
	}

	@Override
	public boolean updateEventDisposal(EventDisposal event, Map<String, Object> params) {

		boolean result = this.updateEventDisposal(event);

		//保存涉事人员信息（晋江）
		this.saveEventInvolvedPersion(event.getEventId(),params);

		//测试参数
		//params.put("isCapEventLabelInfo",true);

		//事件保存成功后 保存重特大案事件、命案防控、矛盾纠纷排查化解、涉及师生安全、路案事件（目前晋江特有）
		this.saveEventLabelContent(event,null,params);

		return result;
	}

	@Override
	public boolean updateEventDisposal(EventDisposal event) {

		// 更新之前的事件信息
		EventDisposal beforeEvent = null;

		Long eventRow = 0L;
		try {
			/**
			 * 四种情况都要设置处理时限 0205和0602默认设置发生时间为当前，紧急程度为01（一般） 根据发生时间，紧急程度设置
			 */
			if (StringUtils.isBlank(event.getHappenTimeStr())) {
				event.setHappenTime(new Date());
			} else {
				event.setHappenTime(DateUtils.convertStringToDate(event.getHappenTimeStr(), DateUtils.PATTERN_24TIME));
			}
			if (StringUtils.isBlank(event.getUrgencyDegree())) {
				event.setUrgencyDegree(ConstantValue.URGENCY_DEGREE_1);
			}

			eventRow = updateEventDisposalById(event);

			if (eventRow > 0) {
				// 保存涉事人员
				saveEventInvolvedPersion(event);

				// 保存定位信息
				saveEventResmarker(event);

				// 保存额外的附件信息
				this.saveEventAttachment(event);

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新事件出错!");
			return false;
		}
		return eventRow > 0 ? true : false;
	}

	@Override
	public Long updateEventDisposalById(EventDisposal event) {
		
		
		// 更新之前的事件信息
		EventDisposal beforeEvent = null;

		// 是否发送rmq消息
		Boolean isContain = false;
		if (StringUtils.isNotBlank(RMQ_IS_OPEN)) {

			// 获取更新之前的事件信息
			try {
				beforeEvent = this.findEventByIdSimple(event.getEventId());
			} catch (Exception e) {
				e.printStackTrace();
			}

			String[] infoOrgCodeArr = RMQ_IS_OPEN.split("-");
			if (null != beforeEvent && StringUtils.isNotBlank(beforeEvent.getGridCode())) {
				String gridCode = "";
				if (StringUtils.isNotBlank(event.getGridCode())) {
					gridCode = event.getGridCode();
				} else {
					gridCode = beforeEvent.getGridCode();
				}
				for (String code : infoOrgCodeArr) {
					if (gridCode.startsWith(code)) {
						isContain = true;
					}
				}
			}
		}
		
		Long eventId = -1L;
		
		if(event != null) {
			eventId = event.getEventId();
			
			if(eventId != null && eventId > 0) {
				boolean result = false;
				
				formatEvent4Save(event);
				
				result = eventDisposalMapper.update(event) > 0;
				
				if(!result) {
					eventId = -1L;
				}
			} else {
				throw new IllegalArgumentException("缺少有效的事件id！");
			}
		} else {
			throw new IllegalArgumentException("缺少有效的事件信息！");
		}
		
		// 事件更新成功，且需要发送消息
		if (eventId > 0 && isContain) {

			try {
				Map<String, Object> params = new HashMap<String, Object>();
                params.put("eventFlag", "update");
                Map<String, Object> evtMap = new HashMap<String, Object>();
				evtMap.put("beforeEvent", beforeEvent);
				evtMap.put("aftereEvent", event);
				params.put("event", evtMap);
				// 发送rmq消息
				sendEventStatusRmqMsg(params);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return eventId;
	}
	
	@Override
	public List<Map<String, Object>> findReportEventListWithEventInter(Map<String, Object> param){
		return this.eventDisposalMapper.findReportEventListWithEventInter(param);
	}
	
	@Override
	public List<Map<String, Object>> findClosedEventListWithEventInter(Map<String, Object> param){
		return this.eventDisposalMapper.findClosedEventListWithEventInter(param);
	}
	
	/**
	 * 保存事件涉事人员
	 * @param event
	 */
	private void saveEventInvolvedPersion(EventDisposal event){
		involvedPeopleService.deleteInvolvedPeopleByBiz(event.getEventId(), InvolvedPeople.BIZ_TYPE.ZHSQ_EVENT.getBizType());//先删除数据
		if (StringUtils.isNotBlank(event.getEventInvolvedPeople())) {
			String[] ipArray = event.getEventInvolvedPeople().split("；"),
					 item = null;
			StringBuffer involvedPersion = new StringBuffer("");//涉及人员姓名
			String ip = null;
			InvolvedPeople involvedPeople = null;
			
			try {
				for(int index = 0, len = ipArray.length; index < len; index++) {
					ip = ipArray[index];
					item = ip.split("，");
					involvedPeople = new InvolvedPeople();
					
					involvedPeople.setBizId(event.getEventId());
					involvedPeople.setBizType(InvolvedPeople.BIZ_TYPE.ZHSQ_EVENT.getBizType());//表示事件，暂时未用
					involvedPeople.setCardType(item[0]);
					involvedPeople.setName(item[1]);
					involvedPeople.setIdCard(item[2]);
					if(item.length > 3){
						involvedPeople.setHomeAddr(item[3]);
						involvedPeople.setTel(item[4]);
						involvedPeople.setRemark(item[5]);
					}
					involvedPeopleService.insertInvolvedPeople(involvedPeople);
					
					involvedPersion.append(item[1]).append(",");
				}
				
				if(StringUtils.isBlank(event.getInvolvedPersion()) && involvedPersion.length()>0){
					event.setInvolvedPersion(involvedPersion.substring(0, involvedPersion.length() - 1));
					this.updateEventDisposalById(event);//更新事件的涉及人员姓名
				}
			} catch (Exception e) {
				logger.error("涉及人员更新失败!");
			}
		} else if(StringUtils.isNotBlank(event.getInvolvedPersion())) {
			InvolvedPeople involvedPeople = new InvolvedPeople();
			involvedPeople.setBizId(event.getEventId());
			involvedPeople.setBizType(InvolvedPeople.BIZ_TYPE.ZHSQ_EVENT.getBizType());//表示事件，暂时未用
			involvedPeople.setName(event.getInvolvedPersion());
			try {
				involvedPeopleService.insertInvolvedPeople(involvedPeople);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存涉事人员（目前晋江特有）
	 * @param eventId
	 * @param params
	 * */
	private void saveEventInvolvedPersion(Long eventId,Map<String,Object> params){
		if (!CommonFunctions.isBlank(params, "isInvolvedPeopleAltered")) {

			EventDisposal event = new EventDisposal();
			if (eventId != null && eventId > 0) {

				//删除数据
				involvedPeopleService.deleteInvolvedPeopleByBiz(eventId, InvolvedPeople.BIZ_TYPE.ZHSQ_EVENT.getBizType());
				if (CommonFunctions.isNotBlank(params, "peopleListJson")) {
					String peopleListJson = (String) params.get("peopleListJson");
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
						//判断人口库是否存在该人员信息，不存在的话将人员信息保存到人口临时库中
						if (involvedPeople.getCiRsId() == null) {
							PartyIndividual peopleExistence = new PartyIndividual();

							if (StringUtils.isNotBlank(involvedPeople.getName())) {
								peopleExistence.setPartyName(involvedPeople.getName());
							}
							if (StringUtils.isNotBlank(involvedPeople.getIdCard())) {
								peopleExistence.setIdentityCard(involvedPeople.getIdCard());
							}
							if (StringUtils.isNotBlank(involvedPeople.getCardType())) {
								peopleExistence.setCertType(involvedPeople.getCardType());
							}
							if (StringUtils.isNotBlank(involvedPeople.getTel())) {
								peopleExistence.setMobilePhone(involvedPeople.getTel());
							}

							try {
								partyIndividualService.addAndUpdate(peopleExistence);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}

					if (involvedPersion.length() > 0) {
						event.setEventId(eventId);
						event.setInvolvedPersion(involvedPersion.substring(1));
						this.updateEventDisposalById(event);
					}
				}
			}
		}
	}

	/**
	 * 保存重特大案事件、命案防控、矛盾纠纷排查化解、涉及师生安全、路案事件（目前晋江特有）
	 * @param event
	 * @param params
	 * */
	private void saveEventLabelContent(EventDisposal event,UserInfo userInfo,Map<String,Object> params){

		//创建人员id
		Long createUserId = null,
				eventId = null;
		//组织编码
		String infoOrgCode = null;

		//查找事件所有标签关联关系
		List<Map<String,Object>> labelMapList =  null;
		Map<String,Object> labelMap = null;
		Map<String,Object> paramsMap = new HashMap<>();
		String labelName = "";

		if(null == userInfo && CommonFunctions.isNotBlank(params,"userInfo")){
			userInfo = (UserInfo) params.get("userInfo");
		}
		
		if(userInfo != null) {
			if(null != userInfo.getUserId() && userInfo.getUserId() > 0){
				createUserId = userInfo.getUserId();
			}
			if(StringUtils.isNotBlank(userInfo.getOrgCode())){
				infoOrgCode = userInfo.getOrgCode();
			}
		}
		
		if(null != event && null != event.getEventId()){
			eventId = event.getEventId();
		}

		if(null != eventId && eventId > 0){
			//保存重特大案事件
			if(CommonFunctions.isNotBlank(params,"isSaveMajorRelatedEvents")){
				Long reId = null;
				Boolean isSaveMajorRelatedEvents = Boolean.valueOf(String.valueOf(params.get("isSaveMajorRelatedEvents")));//判断重特大案事件新增还是删除
				Boolean isAdd = false;//判断重特大案事件是新增还是更新
				RelatedEvents relatedEvents = null;
				labelName = "majorRelatedEvents";
				labelMap = null;

				//查找关联关系，判断关联关系是进行业务操作还是删除关联关系
				paramsMap.put("eventId",eventId);
				paramsMap.put("labelName",labelName);
				labelMapList = eventLabelRelaService.searchByEventId(eventId,paramsMap);

				if(null != labelMapList && labelMapList.size() > 0){
					labelMap = labelMapList.get(0);
				}
				//1.若关联关系已经存在并且重特大案事件有效则更新，否则新增重特大案件并且更新关联关系--等伟进提供接口  参数 labelName:majorRelatedEvents
				//2.删除关联关系

				//获取 reId，即关联关系中的 bizId
				if( null != labelMap && CommonFunctions.isNotBlank(labelMap,"BIZ_ID")){
					reId = Long.valueOf(String.valueOf(labelMap.get("BIZ_ID")));
				}
				if(reId != null && reId > 0){
					paramsMap.put("bizId",reId);
				}

				//保存或更新重特大案事件
				if(isSaveMajorRelatedEvents){
					//查找案事件记录
					relatedEvents = commonRelatedEventsService.findRelatedEventsById(reId, infoOrgCode);

					//重特大案事件不存在，则新增记录；存在则更新
					if(null == relatedEvents){
						isAdd = true;
					}

					//从 params 中获取参数构造对象属性
					relatedEvents = new RelatedEvents();
					relatedEvents = (RelatedEvents)convertMapToBean(relatedEvents,params,labelName);


					//保存或更新重特大案事件
					try {
						//新增
						if(isAdd){
							relatedEvents.setCreateUserId(createUserId);
							reId = commonRelatedEventsService.insertRelatedEvents(relatedEvents, infoOrgCode);
						}else {
							//reId
							relatedEvents.setReId(reId);
							//更新
							relatedEvents.setUpdateUserId(createUserId);//更新人

							commonRelatedEventsService.updateRelatedEvents(relatedEvents);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					//保存关联关系，等伟进提供接口
					if(null != reId && reId > 0){
						paramsMap.put("bizId",reId);
						//重启关联关系
						paramsMap.put("isValid","1");
						try {
							eventLabelRelaService.saveOrUpdate(paramsMap,userInfo);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else {
					//删除关联关系
					eventLabelRelaService.deleteEventLabelRela(paramsMap,userInfo);
				}
			}

			//路案事件
			if(CommonFunctions.isNotBlank(params,"isSaveCareRoads")){
				Long reId = null;
				Boolean isSaveCareRoads = Boolean.valueOf(String.valueOf(params.get("isSaveCareRoads")));//判断路案事件新增还是删除
				Boolean isAdd = false;//判断路案事件是新增还是更新
				RelatedEvents relatedEvents = null;
				labelName = "careRoads";
				labelMap = null;
				List<InvolvedPeople> involvedPeopleList = null;

				//查找关联关系，判断关联关系是进行业务操作还是删除关联关系
				paramsMap.put("eventId",eventId);
				paramsMap.put("labelName",labelName);
				labelMapList = eventLabelRelaService.searchByEventId(eventId,paramsMap);

				if(null != labelMapList && labelMapList.size() > 0){
					labelMap = labelMapList.get(0);
				}
				//1.若关联关系已经存在并且路案事件有效则更新，否则新增路案事件并且更新关联关系--等伟进提供接口  参数 labelName:homicideCase
				//2.删除关联关系

				//获取 reId，即关联关系中的 bizId
				if( null != labelMap && CommonFunctions.isNotBlank(labelMap,"BIZ_ID")){
					reId = Long.valueOf(String.valueOf(labelMap.get("BIZ_ID")));
				}
				if(null != reId && reId > 0){
					paramsMap.put("bizId",reId);
				}

				//保存或更新路案事件
				if(isSaveCareRoads){
					//查找案事件记录
					relatedEvents = relatedEventsService.findRelatedEventsById(reId, infoOrgCode);

					//路案事件不存在，则新增记录；存在则更新
					if(null == relatedEvents){
						isAdd = true;
					}

					//判断命案防控是否包含嫌疑人/受害人信息
					if(CommonFunctions.isNotBlank(params,"careRoads.personJson")){
						//JSONArray involvePeopleArr = (JSONArray) params.get("homicideCase.personJson");
						String jsonString = String.valueOf(params.get("careRoads.personJson"));

						if(StringUtils.isNotBlank(jsonString)){
							//JSONObject.parseArray(involvePeopleArr.toJSONString(),InvolvedPeople.class);
							involvedPeopleList = JsonUtils.json2List(jsonString,InvolvedPeople.class);
						}
					}

					//去除 careRoads.personJson,为了不使得从 params 中获取参数构造对象属性失败
					if(null != params && params.containsKey("careRoads.personJson")){
						params.remove("careRoads.personJson");
					}

					//从 params 中获取参数构造对象属性
					relatedEvents = new RelatedEvents();
					relatedEvents = (RelatedEvents)convertMapToBean(relatedEvents,params,labelName);


					//保存或更新路案事件
					try {
						//新增
						if(isAdd){
							relatedEvents.setCreateUserId(createUserId);
							reId = relatedEventsService.insertRelatedEvents(relatedEvents);
						}else {
							//更新
							//reId
							relatedEvents.setReId(reId);
							relatedEvents.setUpdateUserId(createUserId);//更新人
							relatedEventsService.updateRelatedEvents(relatedEvents);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					//保存关联关系
					if(null != reId && reId > 0){
						
						//不论相关人员的集合是否为空，都应该先清空人员的关联信息，页面传的值为空则说明需要完全删除相关的人员，而不能不做处理
						//综合考虑先清空人员关系集合
						involvedPeopleService.deleteInvolvedPeopleByBiz(reId,InvolvedPeople.BIZ_TYPE.CARE_ROADS.getBizType());

						//保存主要嫌疑人信息
						if(null != involvedPeopleList && involvedPeopleList.size() > 0){
							try {
								//新增被害人/受害人信息
								involvedPeopleService.batchSaveOrUpdate(reId,involvedPeopleList);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						paramsMap.put("bizId",reId);
						//重启关联关系
						paramsMap.put("isValid","1");
						try {
							eventLabelRelaService.saveOrUpdate(paramsMap,userInfo);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else {
					//删除关联关系
					eventLabelRelaService.deleteEventLabelRela(paramsMap,userInfo);
				}
			}

			//涉及师生安全的案（事）件
			if(CommonFunctions.isNotBlank(params,"isSaveSchoolRelatedEvents")){
				Long reId = null;
				Boolean isSaveSchoolRelatedEvents = Boolean.valueOf(String.valueOf(params.get("isSaveSchoolRelatedEvents")));//判断新增还是删除
				Boolean isAdd = false;//判断是新增还是更新
				RelatedEvents relatedEvents = null;
				labelName = "schoolRelatedEvents";
				labelMap = null;
				List<InvolvedPeople> involvedPeopleList = null;

				//查找关联关系，判断关联关系是进行业务操作还是删除关联关系
				paramsMap.put("eventId",eventId);
				paramsMap.put("labelName",labelName);
				labelMapList = eventLabelRelaService.searchByEventId(eventId,paramsMap);

				if(null != labelMapList && labelMapList.size() > 0){
					labelMap = labelMapList.get(0);
				}
				//1.若关联关系已经存在并且有效则更新，否则新增并且更新关联关系--等伟进提供接口  参数 labelName:homicideCase
				//2.删除关联关系

				//获取 reId，即关联关系中的 bizId
				if( null != labelMap && CommonFunctions.isNotBlank(labelMap,"BIZ_ID")){
					reId = Long.valueOf(String.valueOf(labelMap.get("BIZ_ID")));
				}
				if(null != reId && reId > 0){
					paramsMap.put("bizId",reId);
				}

				//保存或更新
				if(isSaveSchoolRelatedEvents){
					//查找案事件记录
					relatedEvents = relatedEventsService.findRelatedEventsById(reId, infoOrgCode);

					//不存在，则新增记录；存在则更新
					if(null == relatedEvents){
						isAdd = true;
					}

					//判断命案防控是否包含嫌疑人/受害人信息
					if(CommonFunctions.isNotBlank(params,"schoolRelatedEvents.personJson")){
						//JSONArray involvePeopleArr = (JSONArray) params.get("homicideCase.personJson");
						String jsonString = String.valueOf(params.get("schoolRelatedEvents.personJson"));

						if(StringUtils.isNotBlank(jsonString)){
							//JSONObject.parseArray(involvePeopleArr.toJSONString(),InvolvedPeople.class);
							involvedPeopleList = JsonUtils.json2List(jsonString,InvolvedPeople.class);
						}
					}

					//去除 careRoads.personJson,为了不使得从 params 中获取参数构造对象属性失败
					if(null != params && params.containsKey("schoolRelatedEvents.personJson")){
						params.remove("schoolRelatedEvents.personJson");
					}

					//从 params 中获取参数构造对象属性
					relatedEvents = new RelatedEvents();
					relatedEvents = (RelatedEvents)convertMapToBean(relatedEvents,params,labelName);


					//保存或更新
					try {
						//新增
						if(isAdd){
							relatedEvents.setCreateUserId(createUserId);
							reId = relatedEventsService.insertRelatedEvents(relatedEvents);
						}else {
							//更新
							relatedEvents.setReId(reId);
							relatedEvents.setUpdateUserId(createUserId);//更新人
							relatedEventsService.updateRelatedEvents(relatedEvents);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					//保存关联关系
					if(null != reId && reId > 0){
						
						//不论相关人员的集合是否为空，都应该先清空人员的关联信息，页面传的值为空则说明需要完全删除相关的人员，而不能不做处理
						//综合考虑先清空人员关系集合
						involvedPeopleService.deleteInvolvedPeopleByBiz(reId,InvolvedPeople.BIZ_TYPE.SCHOOL_RELATED_EVENTS.getBizType());
						
						//保存主要嫌疑人信息
						if(null != involvedPeopleList && involvedPeopleList.size() > 0){
							try {
								//新增被害人/受害人信息
								involvedPeopleService.batchSaveOrUpdate(reId,involvedPeopleList);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						paramsMap.put("bizId",reId);
						paramsMap.put("isValid","1");
						try {
							eventLabelRelaService.saveOrUpdate(paramsMap,userInfo);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else {
					//删除关联关系
					eventLabelRelaService.deleteEventLabelRela(paramsMap,userInfo);
				}
			}

			//保存命案防控
			if(CommonFunctions.isNotBlank(params,"isSaveHomicideCase")){
				Long reId = null;
				Boolean isSaveHomicideCase = Boolean.valueOf(String.valueOf(params.get("isSaveHomicideCase")));//判断命案防控新增还是删除
				Boolean isAdd = false;//判断命案防控是新增还是更新
				RelatedEvents relatedEvents = null;
				labelName = "homicideCase";
				labelMap = null;
				List<InvolvedPeople> involvedPeopleList = null;

				//查找关联关系，判断关联关系是进行业务操作还是删除关联关系
				paramsMap.put("eventId",eventId);
				paramsMap.put("labelName",labelName);
				labelMapList = eventLabelRelaService.searchByEventId(eventId,paramsMap);

				if(null != labelMapList && labelMapList.size() > 0){
					labelMap = labelMapList.get(0);
				}
				//1.若关联关系已经存在并且命案防控有效则更新，否则新增命案防控并且更新关联关系--等伟进提供接口  参数 labelName:homicideCase
				//2.删除关联关系

				//获取 reId，即关联关系中的 bizId
				if( null != labelMap && CommonFunctions.isNotBlank(labelMap,"BIZ_ID")){
					reId = Long.valueOf(String.valueOf(labelMap.get("BIZ_ID")));
				}
				if(null != reId && reId > 0){
					paramsMap.put("bizId",reId);
				}

				//保存或更新命案防控
				if(isSaveHomicideCase){
					//查找案事件记录
					relatedEvents = relatedEventsService.findRelatedEventsById(reId, infoOrgCode);

					//命案防控不存在，则新增记录；存在则更新
					if(null == relatedEvents){
						isAdd = true;
					}

					//判断命案防控是否包含嫌疑人/受害人信息
					if(CommonFunctions.isNotBlank(params,"homicideCase.personJson")){
						//JSONArray involvePeopleArr = (JSONArray) params.get("homicideCase.personJson");
						String jsonString = String.valueOf(params.get("homicideCase.personJson"));

						if(StringUtils.isNotBlank(jsonString)){
							//JSONObject.parseArray(involvePeopleArr.toJSONString(),InvolvedPeople.class);
							involvedPeopleList = JsonUtils.json2List(jsonString,InvolvedPeople.class);
						}
					}

					//去除 homicideCase.personJson,为了不使得从 params 中获取参数构造对象属性失败
					if(null != params && params.containsKey("homicideCase.personJson")){
						params.remove("homicideCase.personJson");
					}
					//从 params 中获取参数构造对象属性
					relatedEvents = new RelatedEvents();
					relatedEvents = (RelatedEvents)convertMapToBean(relatedEvents,params,labelName);


					//保存或更新命案防控
					try {
						//新增
						if(isAdd){
							relatedEvents.setCreateUserId(createUserId);
							reId = relatedEventsService.insertRelatedEvents(relatedEvents);
						}else {
							//更新
							relatedEvents.setReId(reId);
							relatedEvents.setUpdateUserId(createUserId);//更新人
							relatedEventsService.updateRelatedEvents(relatedEvents);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					//保存关联关系
					if(null != reId && reId > 0){
						
						//不论相关人员的集合是否为空，都应该先清空人员的关联信息，页面传的值为空则说明需要完全删除相关的人员，而不能不做处理
						//综合考虑先清空人员关系集合
						involvedPeopleService.deleteInvolvedPeopleByBiz(reId,InvolvedPeople.BIZ_TYPE.HOMICIDE_SUSPECT.getBizType());
						involvedPeopleService.deleteInvolvedPeopleByBiz(reId,InvolvedPeople.BIZ_TYPE.HOMICIDE_VITICM.getBizType());
						
						//保存命案嫌疑人/受害人信息
						if(null != involvedPeopleList && involvedPeopleList.size() > 0){
							try {
								//新增被害人/受害人信息
								involvedPeopleService.batchSaveOrUpdate(reId,involvedPeopleList);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						paramsMap.put("bizId",reId);
						//重启关联关系
						paramsMap.put("isValid","1");
						try {
							eventLabelRelaService.saveOrUpdate(paramsMap,userInfo);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else {
					//删除关联关系
					eventLabelRelaService.deleteEventLabelRela(paramsMap,userInfo);
				}
			}
			//保存矛盾纠纷信息
			if(CommonFunctions.isNotBlank(params,"isSaveDisputeMediation")){
				Long reId = null;
				Boolean isSaveDisputeMediation = Boolean.valueOf(String.valueOf(params.get("isSaveDisputeMediation")));//判断新增还是删除
				Boolean isAdd = false;//判断新增还是更新
				//矛盾纠纷主信息
				DisputeMediation disputeMediation = null;
				labelName = "disputeMediation";
				labelMap = null;
				List<InvolvedPeople> involvedPeopleList = null;

				//查找关联关系，判断关联关系是进行业务操作还是删除关联关系
				paramsMap.put("eventId",eventId);
				paramsMap.put("labelName",labelName);
				labelMapList = eventLabelRelaService.searchByEventId(eventId,paramsMap);

				if(null != labelMapList && labelMapList.size() > 0){
					labelMap = labelMapList.get(0);
				}
				//1.若关联关系已经存在并且矛盾纠纷有效则更新，否则新增矛盾纠纷并且更新关联关系--等伟进提供接口  参数 labelName:disputeMediation
				//2.删除关联关系

				//获取 reId，即关联关系中的 bizId
				if( null != labelMap && CommonFunctions.isNotBlank(labelMap,"BIZ_ID")){
					reId = Long.valueOf(String.valueOf(labelMap.get("BIZ_ID")));
				}
				if(null != reId && reId > 0){
					paramsMap.put("bizId",reId);
				}

				//保存或更新矛盾纠纷
				if(isSaveDisputeMediation){
					//查找矛盾纠纷记录
					disputeMediation = disputeMediationService.selectByPrimaryKey(reId);

					//矛盾纠纷不存在，则新增记录；存在则更新
					if(null == disputeMediation){
						isAdd = true;
					}

					//判断矛盾纠纷当事人/化解责任人信息
					if(CommonFunctions.isNotBlank(params,"disputeMediation.personJson")){
						//JSONArray involvePeopleArr = (JSONArray) params.get("homicideCase.personJson");
						String jsonString = String.valueOf(params.get("disputeMediation.personJson"));

						if(StringUtils.isNotBlank(jsonString)){
							//JSONObject.parseArray(involvePeopleArr.toJSONString(),InvolvedPeople.class);
							involvedPeopleList = JsonUtils.json2List(jsonString,InvolvedPeople.class);
						}
					}

					//获取 gridId，矛盾纠纷必填字段
					if(null != event.getGridId()){
						params.put("disputeMediation.gridId",event.getGridId());
					}

					//去除 disputeMediation.personJson,为了不使得从 params 中获取参数构造对象属性失败
					if(null != params && params.containsKey("disputeMediation.personJson")){
						params.remove("disputeMediation.personJson");
					}

					//从 params 中获取参数构造对象属性
					disputeMediation = new DisputeMediation();
					disputeMediation = (DisputeMediation)convertMapToBean(disputeMediation,params,labelName);


					//保存或更新命案防控
					try {
						//新增
						if(isAdd){
							disputeMediation.setCreatorId(createUserId);
							reId = disputeMediationService.insertDisputeMediation(disputeMediation);
						}else {
							//更新
							disputeMediation.setMediationId(reId);
							disputeMediation.setUpdateId(createUserId.intValue());//更新人
							disputeMediationService.updateByPrimaryKeySelective(disputeMediation);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					//保存关联关系
					if(null != reId && reId > 0){
						
						//不论相关人员的集合是否为空，都应该先清空人员的关联信息，页面传的值为空则说明需要完全删除相关的人员，而不能不做处理
						//综合考虑先清空人员关系集合
						involvedPeopleService.deleteInvolvedPeopleByBiz(reId,InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION.getBizType());
						involvedPeopleService.deleteInvolvedPeopleByBiz(reId,InvolvedPeople.BIZ_TYPE.DISPUTE_MEDIATION_PEOPLE_IN_CHARGE.getBizType());

						//保存命案嫌疑人/受害人信息
						if(null != involvedPeopleList && involvedPeopleList.size() > 0){
							try {
								//新增当事人信息信息
								involvedPeopleService.batchSaveOrUpdate(reId,involvedPeopleList);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						paramsMap.put("bizId",reId);
						//重启关联关系
						paramsMap.put("isValid","1");
						try {
							eventLabelRelaService.saveOrUpdate(paramsMap,userInfo);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else {
					//删除关联关系
					eventLabelRelaService.deleteEventLabelRela(paramsMap,userInfo);
				}
			}
		}

	}

	/**
	 * 将map中的实体属性值转换为实体类
	* @param params 包含实体属性值的map，
	 *       key    实体名字.属性名
	 *       value	属性值
	 *@param beanPrefix	实体名字
	 *@return 实体对象
	* */
	public static <T> T convertMapToBean(T bean,Map<String,Object> params, String beanPrefix){

		if(StringUtils.isNotBlank(beanPrefix)){
			//书体属性map
			Map<String,Object> attrMap = new HashMap<>();
			//实体属性名
			String fieldName = "";
			Object fieldValue = null;

			for(String key:params.keySet()){
				if(key.startsWith(beanPrefix)){
					fieldName = key.substring(key.lastIndexOf(".")+1);
					fieldValue = params.get(key);

					attrMap.put(fieldName,fieldValue);
				}

			}
			//根据 beanPrefix 将 map 转成 实体
			if (null != attrMap) {
				try {
					BeanUtils.populate(bean,attrMap);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}


		}
		return bean;
	}
	
	
	/**
	 * 保存标注信息(如果没有手动标注，就默认用网格中心点经纬度)
	 * @param event
	 */
	private void saveEventResmarker(EventDisposal event){
		if (!StringUtils.isBlank(event.getResMarker().getMapType())
				&& !StringUtils.isBlank(event.getResMarker().getX())
				&& !StringUtils.isBlank(event.getResMarker().getY())) {
			if (!"0.0".equals(event.getResMarker().getX())
					&& !"0.0".equals(event.getResMarker().getY())) {
				event.getResMarker().setResourcesId(event.getEventId());
				event.getResMarker().setCatalog("03");
				event.getResMarker().setMarkerType(ConstantValue.MARKER_TYPE_EVENT);
				resGisMarkerService.saveOrUpdateResMarker(event.getResMarker());
			}
		} else {//如果没有标注信息，默认使用事件的网格中心点
			if(event.getGridId() != null){
				List<ArcgisInfoOfGrid> arcgisInfoList = arcgisInfoService.getArcgisDataOfGridForOuter(event.getGridId(), null);
				if(arcgisInfoList!=null && arcgisInfoList.size()>0){
					if(event.getResMarker() == null){
						event.setResMarker(new ResMarker());
					}
					event.getResMarker().setResourcesId(event.getEventId());
					event.getResMarker().setCatalog("03");
					event.getResMarker().setMarkerType(ConstantValue.MARKER_TYPE_EVENT);
					
					for(ArcgisInfoOfGrid arcgis : arcgisInfoList){
						event.getResMarker().setX(String.valueOf(arcgis.getX()));
						event.getResMarker().setY(String.valueOf(arcgis.getY()));
						event.getResMarker().setMapType(String.valueOf(arcgis.getMapt()));
						resGisMarkerService.saveOrUpdateResMarker(event.getResMarker());	
					}
				}
			}
		}
	}
	
	/**
	 * 保存事件上报关联记录
	 * @param event
	 */
	private void saveEventReportRecordInfo(EventDisposal event, String orgCode){
		if(event != null){
			EventReportRecordInfo reportRecord = event.getEventReportRecordInfo();
			
			if(reportRecord != null) {
				Long bizId = reportRecord.getBizId();
				
				if(bizId != null && bizId > 0) {
					Long eventId = reportRecord.getEventId();
					String bizType = reportRecord.getBizType(),
						   serviceName = reportRecord.getServiceName(),
						   procStatus = reportRecord.getProcStatus();
					
					if(eventId == null || eventId < 0){
						eventId = event.getEventId();
					}
				
					if(eventId != null && eventId > 0) {
						reportRecord.setEventId(eventId);
						
						if(StringUtils.isBlank(bizType)){
							bizType = configurationService.turnCodeToValue(ConstantValue.EVENT_BIZ_TYPE, event.getType(), IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
						}
						
						if(StringUtils.isNotBlank(bizType)) {
							reportRecord.setBizType(bizType);
							
							if(StringUtils.isBlank(serviceName)){
								serviceName = configurationService.turnCodeToValue(ConstantValue.EVENT_ARCH_CALLBACK_SERVICE, bizType, IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, IFunConfigurationService.CFG_ORG_TYPE_0);
							}
						}
						
						if(StringUtils.isNotBlank(serviceName)){
							reportRecord.setServiceName(serviceName);
							
							if(StringUtils.isBlank(procStatus)){
								reportRecord.setProcStatus("0");
							}
							
							eventReportRecordService.insertEventReportRecordInfo(reportRecord, orgCode);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 事件属性格式化，不做字典翻译
	 * @param event
	 */
	@Cacheable(value = "baseCache")
	private void formatEvent(EventDisposal event) {
		if(event != null) {
			Date happenTime = event.getHappenTime(),
				 finTime = event.getFinTime(),
				 handleDate = event.getHandleDate(),
				 createTime = event.getCreateTime(),
				 endTime = event.getEndTime();
			
			if(happenTime != null) {
				event.setHappenTimeStr(DateUtils.formatDate(happenTime, DateUtils.PATTERN_24TIME));
			}
			if(finTime != null) {
				event.setFinTimeStr(DateUtils.formatDate(finTime, DateUtils.PATTERN_24TIME));
			}
			if(handleDate != null) {
				event.setHandleDateStr(DateUtils.formatDate(handleDate, DateUtils.PATTERN_24TIME));
			}
			if(createTime != null) {
				event.setCreateTimeStr(DateUtils.formatDate(createTime, DateUtils.PATTERN_24TIME));
			}
			if(endTime != null) {
				event.setEndTimeStr(DateUtils.formatDate(endTime, DateUtils.PATTERN_24TIME));
			}
		}
	}
	
	@Cacheable(value = "baseCache")
	private void formatEvent(List<EventDisposal> list, Map<String, Object> params){
		if(list!=null && list.size()>0){
			String orgCode = null;
			if(CommonFunctions.isNotBlank(params, "orgCode")) {
				orgCode = params.get("orgCode").toString();
			}
			
			formatData(list, orgCode);
		}
	}
	
	@Cacheable(value = "baseCache")
	private void formatEvent(List<Map> eventMapList){
		if(eventMapList != null && eventMapList.size() > 0) {
			Long gridId = -1L, instanceId = -1L, taskId = -1L;
			MixedGridInfo mixedGridInfo = null;
			Map<String, Object> curNodeMap = null;
			List<Map<String, Object>> taskPerson = null;
			StringBuffer taskPersonStr = null;
			
			for(Map eventMap : eventMapList) {
				if(CommonFunctions.isNotBlank(eventMap, "GRID_ID")) {
					try {
						gridId = Long.valueOf(eventMap.get("GRID_ID").toString());
					} catch(NumberFormatException e) {
						gridId = -1L;
					}
					
					if(gridId > 0) {
						mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
						if(mixedGridInfo != null){
							eventMap.put("GRID_NAME", mixedGridInfo.getGridName());
							eventMap.put("GRID_PATH", mixedGridInfo.getGridPath());
						}
					}
				}
				
				if(CommonFunctions.isNotBlank(eventMap, "WF_CREATE_")) {
					eventMap.put("CUR_TASK_CREATE_TIME", eventMap.get("WF_CREATE_"));
				}
				
				if(CommonFunctions.isNotBlank(eventMap, "WF_INSTANCE_ID")) {
					try {
						instanceId = Long.valueOf(eventMap.get("WF_INSTANCE_ID").toString());
						eventMap.put("INSTANCE_ID", eventMap.get("WF_INSTANCE_ID"));
					} catch(NumberFormatException e) {
						instanceId = -1L;
					}
					
					curNodeMap = eventDisposalWorkflowService.curNodeData(instanceId);
					 
					if(curNodeMap != null) {
						if(CommonFunctions.isNotBlank(curNodeMap, "WF_ACTIVITY_NAME_")) {
							eventMap.put("CUR_ACTIVITY_NAME", curNodeMap.get("WF_ACTIVITY_NAME_"));
						}
						if(CommonFunctions.isNotBlank(curNodeMap, "WF_DBID_")) {//当前任务id
							try {
								taskId = Long.valueOf(curNodeMap.get("WF_DBID_").toString());
							} catch(NumberFormatException e) {
								taskId = -1L;
							}
						}
					}
					
					if(taskId > 0){//获取当前办理人信息
						taskPerson = eventDisposalWorkflowService.queryMyTaskParticipation(taskId.toString());
						
						if(taskPerson!=null && taskPerson.size()>0){
							taskPersonStr = new StringBuffer(";");
							
							for(Map<String, Object> mapTemp : taskPerson){
								Object orgNameObj = mapTemp.get("ORG_NAME");
								
								if(CommonFunctions.isNotBlank(mapTemp, "USER_NAME")){
									taskPersonStr.append(mapTemp.get("USER_NAME"));
									if(orgNameObj != null){
										taskPersonStr.append("(").append(orgNameObj).append(");");
									}
								}else if(orgNameObj != null){
									taskPersonStr.append(orgNameObj).append(";");
								}
							}
							
							taskPersonStr = new StringBuffer(taskPersonStr.substring(1));
							
							eventMap.put("CUR_USER_NAME", taskPersonStr);
						}
						
						eventMap.put("CUR_TASK_ID", taskId);
					}
				}
				
			}
			
		}
	}
	
	@Cacheable(value = "baseCache")
	@Override
	public EventDisposal formatData(EventDisposal event, String orgCode) {
		List<EventDisposal> eventList = new ArrayList<EventDisposal>();
		eventList.add(event);
		
		this.formatData(eventList, orgCode);
		
		return event;
	}

	/**
	 * 格式化输出事件数据
	 * @param eventList	事件列表
	 * @param orgCode	组织编码
	 */
	@Cacheable(value = "baseCache")
	private void formatData(List<EventDisposal> eventList, String orgCode) {
		if(eventList != null && eventList.size() > 0) {
			Map<String, Object> dictMap = new HashMap<String, Object>();
			dictMap.put("orgCode", orgCode);
			dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
			
			List<BaseDataDict> urgencyDegreeDict = dictionaryService.getDataDictListOfSinglestage(ConstantValue.URGENCY_DEGREE_PCODE, orgCode),
							   influenceDegreeDict = dictionaryService.getDataDictListOfSinglestage(ConstantValue.INFLUENCE_DEGREE_PCODE, orgCode),
							   sourceDict = dictionaryService.getDataDictListOfSinglestage(ConstantValue.SOURCE_PCODE, orgCode),
							   involvedNumDict = dictionaryService.getDataDictListOfSinglestage(ConstantValue.INVOLVED_NUM_PCODE, orgCode),
							   statusDict = dictionaryService.getDataDictListOfSinglestage(ConstantValue.STATUS_PCODE, orgCode),
							   subStatusDict = dictionaryService.getDataDictListOfSinglestage(ConstantValue.SUB_STATUS_PCODE, orgCode),
							   collectWayDict = dictionaryService.getDataDictListOfSinglestage(ConstantValue.COLLECT_WAY_PCODE, orgCode),
							   eventTypeDict = dictionaryService.findDataDictListByCodes(dictMap);
			
			String eventType = "",
				      urgencyDegree = "",
				      influenceDegree = "",
				      source = "",
				      involvedNum = "",
				      status = "",
				      subStatus = "",
				      collectWay = "",
				      gridPath = null;
			Long eventId = -1L;
			
			InvolvedPeople involvedPeople = new InvolvedPeople();
			involvedPeople.setBizType(InvolvedPeople.BIZ_TYPE.ZHSQ_EVENT.getBizType());//事件类型
			StringBuffer eventInvolvedPeople = new StringBuffer("");
			List<InvolvedPeople> involvedPeopleList = null;
			
			for(EventDisposal event : eventList) {
				// 设置事件分类的值 大类+小类
				eventType = event.getType();
				urgencyDegree = event.getUrgencyDegree();
				influenceDegree = event.getInfluenceDegree();
				source = event.getSource();
				involvedNum = event.getInvolvedNum();
				status = event.getStatus();
				subStatus = event.getSubStatus();
				collectWay = event.getCollectWay();
				eventId = event.getEventId();
				
				// 设置事件分类全路径
				if(StringUtils.isNotBlank(eventType) && eventTypeDict != null) {
					Map<String, Object> eventTypeMap = DataDictHelper.capMultilevelDictInfo(eventType, ConstantValue.BIG_TYPE_PCODE, eventTypeDict);
					
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictName")) {
						event.setTypeName(eventTypeMap.get("dictName").toString());
					}
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
						event.setEventClass(eventTypeMap.get("dictFullPath").toString());
					}
				}
				
				if (StringUtils.isNotBlank(urgencyDegree)) {// 紧急程度
					try {
						DataDictHelper.setDictValueForField(event, "urgencyDegree", "urgencyDegreeName", urgencyDegreeDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
				}
				if (StringUtils.isNotBlank(influenceDegree)) {// 影响范围
					try {
						DataDictHelper.setDictValueForField(event, "influenceDegree", "influenceDegreeName", influenceDegreeDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
				} else {
					event.setInfluenceDegreeName("");
				}
				if (StringUtils.isNotBlank(source)) {// 信息来源
					try {
						DataDictHelper.setDictValueForField(event, "source", "sourceName", sourceDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
				}
				// 涉及人数
				if (StringUtils.isNotBlank(involvedNum)) {
					try {
						DataDictHelper.setDictValueForField(event, "involvedNum", "involvedNumName", involvedNumDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
				}
				if (StringUtils.isNotBlank(status)) {// 事件状态
					try {
						DataDictHelper.setDictValueForField(event, "status", "statusName", statusDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
				}
				// 事件子状态
				if (StringUtils.isNotBlank(subStatus)) {
					try {
						DataDictHelper.setDictValueForField(event, "subStatus", "subStatusName", subStatusDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
					
					if(StringUtils.isNotBlank(event.getSubStatusName())) {
						if(ConstantValue.REJECT_SUB_STATUS.equals(subStatus)) {
							event.setStatusName(event.getSubStatusName());
						} else {
							event.setStatusName(event.getStatusName()+"-"+event.getSubStatusName());
						}
					}
				}
				if (StringUtils.isNotBlank(collectWay)) {// 采集渠道
					try {
						DataDictHelper.setDictValueForField(event, "collectWay", "collectWayName", collectWayDict);
					} catch (Exception e) {//防止字典转换失败，导致后续的操作失败
						e.printStackTrace();
					}
				}
				
				//构造涉及人员
				if(StringUtils.isBlank(event.getEventInvolvedPeople()) || StringUtils.isNotBlank(event.getInvolvedPersion())) {
					if(eventId != null && eventId > 0) {//eventId有效时，才构造涉及人员信息，否则会造成查出所有的涉及人员信息
						involvedPeople.setBizId(event.getEventId());
						involvedPeopleList = involvedPeopleService.findInvolvedPeopleList(involvedPeople);
						
						if (involvedPeopleList != null && involvedPeopleList.size() > 0) {
							eventInvolvedPeople.setLength(0);
							
							for (InvolvedPeople i : involvedPeopleList) {
								eventInvolvedPeople.append(i.getCardType()).append("，").append(i.getName()).append("，").append(i.getIdCard()).append("；");
							}
							
							event.setEventInvolvedPeople(eventInvolvedPeople.toString());
						}
					}
				}
				
				gridPath = event.getGridPath();
				
				//去除网格全路径中的顶级路径
				if(StringUtils.isNotBlank(gridPath)) {
					event.setGridPath(CommonFunctions.replaceScopePath(gridPath, cacheService));
				}
			}
		}
	}
	
	/**
	 * 查询事件交互中间表
	 */
	@Override
	public List<EventInter> findEventInterList(EventInter inter) {
		return eventDisposalMapper.findEventInterList(inter);
	}

	/**
	 * 新增事件交互记录 interId>-1 新增成功 0 重复新增  -1 新增失败
	 * @param inter
	 * @return
	 */
	@Override
	public Long saveEventInterAndReturnId(EventInter inter){
		int result = 0;
		Long interId = -1L;
		
		if(isEventInterNotExists(inter)){
			result = eventDisposalMapper.eventInterInsert(inter);
			if(result > 0){
				interId = inter.getInterId();
			}
		}else{
			interId = 0L;
		}
		
		return interId;
	}
	
	@Override
	public boolean updateEventInter(EventInter inter){
		int result = eventDisposalMapper.updateEventInter(inter);
		return result > 0;
	}
	
	@Override
	public boolean updateEventInterOppoSideBusiCode(EventInter inter){
		int result = eventDisposalMapper.updateEventInterOppoSideBusiCode(inter);
		return result > 0;
	}
	
	/**
	 * 依据ownSideEventCode oppoSideBusiCode eventSrc三个字段判重
	 * @param inter
	 * @return
	 */
	private boolean isEventInterNotExists(EventInter inter){
		EventInter interTmp = new EventInter();
		String ownSideEventCode = inter.getOwnSideEventCode();
		String oppoSideBusiCode = inter.getOppoSideBusiCode();
		String eventSrc = inter.getEventSrc();
		List<EventInter> eventInterList = null;
		boolean validateFlag = true;
		
		if(StringUtils.isNotBlank(ownSideEventCode)){
			interTmp.setOwnSideEventCode(ownSideEventCode);
		}else{
			validateFlag = false;
		}
		if(StringUtils.isNotBlank(oppoSideBusiCode)){
			interTmp.setOppoSideBusiCode(oppoSideBusiCode);
		}else{
			interTmp.setOppoSideBusiCode(null);
		}
		if(StringUtils.isNotBlank(eventSrc)){
			interTmp.setEventSrc(eventSrc);
		}else{
			validateFlag = false;
		}
		
		if(validateFlag){
			eventInterList = this.findEventInterList(interTmp);
		}
		
		return (eventInterList==null || eventInterList.size()==0);
	}
	
	/**
	 * 工作流同步更新事件信息接口
	 * userId和orgId不做有效性验证，因为在与第三方对接时，这两个字段为空
	 * @param jsonString
	 * @return
	 */
	@Override
	public Long updateEventForWorkflow(Long eventId, String eventStatus,
			String handleDateStr, String finTimeStr, Long userId, Long orgId) {
		logger.debug("finTimeStr ---> " + finTimeStr);
		logger.debug("handleDateStr ---> " + handleDateStr);
		Date handleDate = null,
			 finTime = null,
			 createTime = null;
		boolean validateFlag = false;//判断参数有效性标识
		Long result = -1L;//返回结果
		EventDisposal event = null;
		
		if (null!=eventId && eventId>0) {
			event = this.findEventByIdSimple(eventId);// 用于查询的对象参数
		}
		
		if(event != null) {//事件有效时，才进行后续操作
			if (StringUtils.isBlank(eventStatus)) {
				
			}else if (ConstantValue.EVENT_STATUS_RECEIVED.equals(eventStatus) && StringUtils.isNotBlank(handleDateStr)) {
				UserInfo userInfo = new UserInfo();
				String orgCode = null;
				int intervalDays = 0;
				validateFlag = true;
				
				if(orgId != null && orgId > 0) {
					OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
					if(orgInfo != null) {
						orgCode = orgInfo.getOrgCode();
					}
				}
				
				userInfo.setUserId(userId);
				userInfo.setOrgCode(orgCode);
				
				intervalDays = this.countDayInterval(event.getUrgencyDegree(), userInfo);
				
				if(intervalDays > 0) {
					createTime = new Date();
					handleDate = holidayInfoService.getWorkDateByAfterWorkDay(createTime, intervalDays);
				} else {
					try {
						handleDate = DateUtils.convertStringToDate(handleDateStr, DateUtils.PATTERN_24TIME);
					}catch(ParseException e) {
						validateFlag = false;
						handleDate = null;
						e.printStackTrace();
					}
				}
			} else if (ConstantValue.EVENT_STATUS_ARCHIVE.equals(eventStatus) && StringUtils.isNotBlank(finTimeStr)) {
				try {
					validateFlag = true;
					finTime = DateUtils.convertStringToDate(finTimeStr, DateUtils.PATTERN_24TIME);
				}catch(ParseException e) {
					validateFlag = false;
					finTime = null;
					e.printStackTrace();
				}
			}else{
				validateFlag = true;
			}
			
			if(validateFlag) {
				EventDisposal eventTmp = new EventDisposal();
				//如果事件已有处理时限，则不使用工作流的处理时限
				if (StringUtils.isBlank(event.getHandleDateStr()) && handleDate != null) {
					if(createTime != null) {
						eventTmp.setCreateTime(createTime);
					}
					eventTmp.setHandleDate(handleDate);
					eventTmp.setHandleDateStr(handleDateStr);
				} else if (StringUtils.isBlank(event.getFinTimeStr()) && finTime != null) {
					eventTmp.setFinTime(finTime);
				}
				
				eventTmp.setEventId(eventId);
				eventTmp.setStatus(eventStatus);//更新事件状态
				result = this.updateEventDisposalById(eventTmp);//更新事件的处理时限或者结案时间
			}
			
			//新增处理步骤记录
			EventProcess record = new EventProcess();
			record.setEventId(eventId);
			record.setCreateTime(new Date());
			record.setStatus(eventStatus);
			record.setOrgId(orgId);
			record.setUserId(userId);
			
			if(handleDate == null && StringUtils.isNotBlank(handleDateStr)) {
				try {
					handleDate = DateUtils.convertStringToDate(handleDateStr, DateUtils.PATTERN_24TIME);
				}catch(ParseException e) {
					handleDate = null;
					e.printStackTrace();
				}
			}
			record.setHandleDate(handleDate);
			saveEventProcess(record);
		}
		
		return result;
	}

	@Override
	public Long saveEventProcess(EventProcess eventProcess) {
		int result = 0;
		
		if(eventProcess != null) {
			if(eventProcess.getCreateTime() == null) {
				eventProcess.setCreateTime(new Date());
			}
			result = eventProcessMapper.insert(eventProcess);
		}
		
		return result > 0 ? eventProcess.getEventProcessId() : -1L;
	}

	@Override
	public EUDGPagination findDoneHistoryEventWorkFlowPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		List<EventDisposal> list = new ArrayList<EventDisposal>();
		Long userId = null, orgId = null;
		int total = 0;
		
		if(CommonFunctions.isNotBlank(params, "userId")) {
			try {
				userId = Long.valueOf(params.get("userId").toString());
			} catch(NumberFormatException e) {
				logger.error("用户id[userId]：" + params.get("userId") + " 不能转换为有效的数值！");
			}
		} else {
			logger.error("缺少用户id[userId]！");
		}
		if(CommonFunctions.isNotBlank(params, "orgId")) {
			try {
				orgId = Long.valueOf(params.get("orgId").toString());
			} catch(NumberFormatException e) {
				logger.error("组织id[orgId]：" + params.get("orgId") + " 不能转换为有效的数值！");
			}
		} else {
			logger.error("缺少组织id[orgId]！");
		}
		
		/**
		 * 可展示同一流程实例多条或单条办理环节的参与列表(关联业务)
		 * @param pageNo 		初始页
		 * @param pageSize		页面记录数
		 * @param userId		用户ID
		 * @param userName		发起人名称
		 * @param status		流程状态
		 * @param eventTable --ConstantValue.DATABASETABLESPACE + ".T_TASKMNG"（数据库名.表名）
		 * @param eventTablesql --"E.TASK_ID, E.TASK_NAME, E.TASK_SN, E.BSTYPE, E.PRIORITY..."（自己的表单在列表上返回的对象）
		 * @param eventTableRelevance --"E.TASK_ID"（关联工作流的表单字段）
		 * @param formTypeId	表单类型
		 * @param eventTableSelectsql --" AND E.USERID=184 AND E.TASK_NAME LIKE '%dasdas%' AND E.BSTYPE = '1'"（自己表单的查询sql）
		 * @param isSignle true-展示流程实例的最新办理环节，false-展示同一实例的全部已办理环节
		 * @param attentionType 关注状态("1"表示过滤至关注状态)
		 * @param orderSQL 字段 --"E.TASK_ID DESC,E.TASK_NAME DESC" ,可为null表示默认以流程状态升序,任务创建时间降序
		 * @return
		 */
		if(userId != null && userId > 0 && orgId != null && orgId > 0) {
			StringBuffer eventWhereSql = addWhereSql(params);
			String orderByField = "E.HAPPEN_TIME DESC";//按时间发生时间降序排列
			
			if(CommonFunctions.isNotBlank(params, "orderByField")) {
				orderByField = params.get("orderByField").toString();
			}
			
			cn.ffcs.system.publicUtil.EUDGPagination pagination = hessianFlowService.queryAdrParticipationList(pageNo, pageSize,
					userId, orgId.toString(), "2",
					dataBaseUser + ".T_EVENT", eventTableSql, "E.EVENT_ID",
					workFlowTypeId, eventWhereSql.toString(), true, "1", orderByField);
			List<Map> listMap = (List<Map>) pagination.getRows();
			
			list = resultMapToEvent(listMap, null,false, params);
			total = pagination.getTotal();
		}
		
		EUDGPagination eudgPagination = new EUDGPagination(total, list);
		return eudgPagination;
	}

	@Override
	public EUDGPagination findHistoryEventWorkFlowPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		StringBuffer eventWhereSql = addWhereSql(params);
		String orderByField = null;//按照归档时间降序排列 WF_END_
		
		if(CommonFunctions.isNotBlank(params, "orderByField")) {
			orderByField = params.get("orderByField").toString();
		}
		
		/*
		 * 获取与任务工单关联列表
		 * @param pageNo	初始页
		 * @param pageSize	页面记录数
		 * @param userId	发起人Id
		 * @param userName(发起人名字可为null)
		 * @param status（流程状态可为null)
		 * @param eventTable --ConstantValue.DATABASETABLESPACE + ".T_TASKMNG"（数据库名.表名）
		 * @param eventTablesql --"E.TASK_ID, E.TASK_NAME, E.TASK_SN, E.BSTYPE, E.PRIORITY, E.INDICATORS, E.DESC_, E.USERID"（自己的表单在列表上返回的对象）
		 * @param eventTableRelevance --"E.TASK_ID"（关联工作流的表单字段）
		 * @param formTypeId	表单类型
		 * @param eventTableSelectsql--" AND E.USERID=184 AND E.TASK_NAME LIKE '%dasdas%' AND E.BSTYPE = '1'"（自己表单的查询sql）
		 * @param attentionType	关注状态("1"表示过滤至关注状态)
		 * @param orderSQL 字段 --"E.TASK_ID DESC,E.TASK_NAME DESC", 可以为null表示按催办时间 升序,流程状态升序,任务到达时间 降序
		 */
		cn.ffcs.system.publicUtil.EUDGPagination pagination = hessianFlowService.queryEndProcList(pageNo,
				pageSize, Long.valueOf(params.get("userId").toString()), params.get("orgId").toString(),
				dataBaseUser + ".T_EVENT", eventTableSql, "E.EVENT_ID",
				workFlowTypeId, eventWhereSql.toString(), null, orderByField);
		List<Map> listMap = (List<Map>) pagination.getRows();
		List<EventDisposal> list = resultMapToEvent(listMap, null,false, params);
		EUDGPagination eudgPagination = new EUDGPagination(pagination
				.getTotal(), list);
		return eudgPagination;
	}

	@Override
	public EUDGPagination findAllTodoEventWorkFlowPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		StringBuffer eventWhereSql = addWhereSql(params);
		cn.ffcs.system.publicUtil.EUDGPagination pagination = hessianFlowService.queryTaskMngList(pageNo,
				pageSize, Long.valueOf(params.get("userId").toString()), null,
				null, dataBaseUser + ".T_EVENT", eventTableSql, "E.EVENT_ID",
				workFlowTypeId, eventWhereSql.toString(), null, "E.HAPPEN_TIME DESC");
		List<Map> listMap = (List<Map>) pagination.getRows();
		List<EventDisposal> list = resultMapToEvent(listMap ,Long.valueOf(params.get("userId").toString()),true, params);
		EUDGPagination eudgPagination = new EUDGPagination(pagination
				.getTotal(), list);
		return eudgPagination;
	}

	@Override
	public int findAllEventWorkFlowCount(Map<String, Object> params) {
		StringBuffer eventWhereSql = addWhereSql(params);
		
		/**
		 * 统计辖区所有数据总量
		 * @param userId	发起人Id
		 * @param userName(发起人名字可为null)
		 * @param status（流程状态可为null)
		 * @param eventTable --ConstantValue.DATABASETABLESPACE + ".T_TASKMNG"（数据库名.表名）
		 * @param eventTablesql --"E.TASK_ID, E.TASK_NAME, E.TASK_SN, E.BSTYPE, E.PRIORITY, E.INDICATORS, E.DESC_, E.USERID"（自己的表单在列表上返回的对象）
		 * @param eventTableRelevance --"E.TASK_ID"（关联工作流的表单字段）
		 * @param formTypeId	表单类型
		 * @param eventTableSelectsql--" AND E.USERID=184 AND E.TASK_NAME LIKE '%dasdas%' AND E.BSTYPE = '1'"（自己表单的查询sql）
		 * @param attentionType	关注状态("1"表示过滤至关注状态)
		 * @param remindStatus	督办状态"2"
		 * @return
		 */
		int total = hessianFlowService.queryAllProcCount(Long.valueOf(params.get("userId").toString()), null, null, 
				dataBaseUser + ".T_EVENT", eventTableSql, 
				"E.EVENT_ID", workFlowTypeId, eventWhereSql.toString(), 
				null, null);
		
		return total;
	}
	
	@Override
	public EUDGPagination findEventTaskMapPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo<1 ? 1 : pageNo;
		pageSize = pageSize<1 ? 10 : pageSize;
		
		if(params == null) {
			params = new HashMap<String, Object>();
		}
		
		StringBuffer eventWhereSql = addWhereSql(params).append(" AND E.STATUS != '04' ");
		String colNames = "E.EVENT_ID, E.EVENT_NAME, E.GRID_ID, E.GRID_CODE";
		
		params.put("eventTable", dataBaseUser + ".T_EVENT");
		params.put("eventTablesql", colNames);
		params.put("eventTableRelevance", "E.EVENT_ID");
		params.put("formTypeId", workFlowTypeId);
		params.put("eventTableSelectsql", eventWhereSql);
		params.put("orderSQL", "E.HAPPEN_TIME DESC");
		
		cn.ffcs.system.publicUtil.EUDGPagination pagination = 
				eventDisposalWorkflowService.queryTaskMngList(pageNo, pageSize, params);
		
		List<Map> listMap = (List<Map>) pagination.getRows();
		
		formatEvent(listMap);
		
		EUDGPagination eudgPagination = 
				new EUDGPagination(pagination.getTotal(), listMap);
		
		return eudgPagination;
	}
	
	@Override
	public EUDGPagination findSupervisePagination(int pageNo, int pageSize, Map<String, Object> params) {
		StringBuffer eventWhereSql = addWhereSql(params);
		/*EUDGPagination pagination = hessianFlowService.queryRemindList(pageNo,
				pageSize, null, null, null, null, null, Long.valueOf(params.get("userId").toString()), null,
				workFlowTypeId, dataBaseUser + ".T_EVENT", eventTableSql,
				"E.EVENT_ID", eventWhereSql.toString());*/
		/*
		 * 督办列表
		 * @param pageNo	起始页
		 * @param pageSize	页面记录数
		 * @param userId	流程发起人Id
		 * @param userName	流程发起人名称
		 * @param status	流程状态
		 * @param superviseUserId	督办人ID
		 * @param superviseUserName	督办人名称
		 * @param supervisedUserId	被督办人ID
		 * @param supervisedUserName	被督办人名称
		 * @param formTypeId	表单类型ID
		 * @param eventTable	--ConstantValue.DATABASETABLESPACE + ".T_TASKMNG"（数据库名.表名）
		 * @param eventTablesql	 --"E.TASK_ID, E.TASK_NAME, E.TASK_SN, E.USERID..."（自己的表单在列表上返回的对象）
		 * @param eventTableRelevance	--"E.TASK_ID"（关联工作流的表单字段）
		 * @param eventTableSelectsql	--" AND E.USERID=184 AND E.TASK_NAME LIKE '%dasdas%' AND E.BSTYPE = '1'"（自己表单的查询sql）
		 */
		cn.ffcs.system.publicUtil.EUDGPagination pagination = hessianFlowService.querySuperviseList(pageNo,
				pageSize, null, null, null, null, null, null, null,
				workFlowTypeId, dataBaseUser + ".T_EVENT", eventTableSql,
				"E.EVENT_ID", eventWhereSql.toString());
		List<Map> listMap = (List<Map>) pagination.getRows();
		List<EventDisposal> list = resultMapToEvent(listMap ,null,true, params);
		
		List<EventDisposal> listRemind = new ArrayList<EventDisposal>();//督办人信息列表
		
		if(list!=null && list.size()>0){
			StringBuffer remindNameAndDate = new StringBuffer("");
			StringBuffer remindTemp = new StringBuffer("");
			for(EventDisposal event : list){
				remindTemp = new StringBuffer("").append(event.getRemindUserName()).append("_").append(event.getRemindDate()).append(",");
				if(remindNameAndDate.indexOf(remindTemp.toString()) == -1){//去除重复的督办人信息
					remindNameAndDate.append(remindTemp);
					listRemind.add(event);
				}
			}
		}
		
		EUDGPagination eudgPagination = new EUDGPagination(pagination.getTotal(), listRemind);
		return eudgPagination;
	}

	@Override
	public EUDGPagination findSupervisedPagination(int pageNo, int pageSize, Map<String, Object> params) {
		StringBuffer eventWhereSql = addWhereSql(params);
		/*
		 * 督办列表
		 * @param pageNo	起始页
		 * @param pageSize	页面记录数
		 * @param userId	流程发起人Id
		 * @param userName	流程发起人名称
		 * @param status	流程状态
		 * @param superviseUserId	督办人ID
		 * @param superviseUserName	督办人名称
		 * @param supervisedUserId	被督办人ID
		 * @param supervisedUserName	被督办人名称
		 * @param formTypeId	表单类型ID
		 * @param eventTable	--ConstantValue.DATABASETABLESPACE + ".T_TASKMNG"（数据库名.表名）
		 * @param eventTablesql	 --"E.TASK_ID, E.TASK_NAME, E.TASK_SN, E.USERID..."（自己的表单在列表上返回的对象）
		 * @param eventTableRelevance	--"E.TASK_ID"（关联工作流的表单字段）
		 * @param eventTableSelectsql	--" AND E.USERID=184 AND E.TASK_NAME LIKE '%dasdas%' AND E.BSTYPE = '1'"（自己表单的查询sql）
		 */
		cn.ffcs.system.publicUtil.EUDGPagination pagination = hessianFlowService.querySuperviseList(pageNo,
				pageSize, null, null, null, null, null, null, null,
				workFlowTypeId, dataBaseUser + ".T_EVENT", eventTableSql,
				"E.EVENT_ID", eventWhereSql.toString());
		List<Map> listMap = (List<Map>) pagination.getRows();
		List<EventDisposal> list = resultMapToEvent(listMap ,null,true, params);
		EUDGPagination eudgPagination = new EUDGPagination(pagination.getTotal(), list);
		return eudgPagination;
	}
	
	@Override
	public EUDGPagination findRemindedPagination(int pageNo, int pageSize, Map<String, Object> params) {
		StringBuffer eventWhereSql = addWhereSql(params);
		/*
		 * 催办列表
		 * @param pageNo	起始页
		 * @param pageSize	页面记录数
		 * @param userId	流程发起人Id
		 * @param userName	流程发起人名称
		 * @param status	流程状态
		 * @param superviseUserId	督办人ID
		 * @param superviseUserName	督办人名称
		 * @param supervisedUserId	被督办人ID
		 * @param supervisedUserName	被督办人名称
		 * @param formTypeId	表单类型ID
		 * @param eventTable	--ConstantValue.DATABASETABLESPACE + ".T_TASKMNG"（数据库名.表名）
		 * @param eventTablesql	 --"E.TASK_ID, E.TASK_NAME, E.TASK_SN, E.USERID..."（自己的表单在列表上返回的对象）
		 * @param eventTableRelevance	--"E.TASK_ID"（关联工作流的表单字段）
		 * @param eventTableSelectsql	--" AND E.USERID=184 AND E.TASK_NAME LIKE '%dasdas%' AND E.BSTYPE = '1'"（自己表单的查询sql）
		 */
		cn.ffcs.system.publicUtil.EUDGPagination pagination = hessianFlowService.queryRemindList(pageNo,
				pageSize, null, null, null, null, null, null, null,
				workFlowTypeId, dataBaseUser + ".T_EVENT", eventTableSql,
				"E.EVENT_ID", eventWhereSql.toString());
		List<Map> listMap = (List<Map>) pagination.getRows();
		List<EventDisposal> list = resultMapToEvent(listMap ,null,true, params);
		EUDGPagination eudgPagination = new EUDGPagination(pagination.getTotal(), list);
		return eudgPagination;
	}
	
	@Override
	public EUDGPagination findRemindEventWorkFlowPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		params.put("isRemind", true);
		StringBuffer eventWhereSql = addWhereSql(params);
		String orderByField = "E.HAPPEN_TIME";//按时间发生时间升序排列
		
		if(CommonFunctions.isNotBlank(params, "orderByField")) {
			orderByField = params.get("orderByField").toString();
		}
		
		cn.ffcs.system.publicUtil.EUDGPagination pagination = hessianFlowService.queryNewTaskList(pageNo,
				pageSize, Long.valueOf(params.get("orgId").toString()), null,
				null, null, Long.valueOf(params.get("userId").toString()),
				null, dataBaseUser + ".T_EVENT", eventTableSql, "E.EVENT_ID",
				workFlowTypeId, eventWhereSql.toString(), "1", orderByField);
		List<Map> listMap = (List<Map>) pagination.getRows();
		logger.info("resultMap ---> " + listMap);
		List<EventDisposal> list = resultMapToEvent(listMap, null,false, params);
		EUDGPagination eudgPagination = new EUDGPagination(pagination
				.getTotal(), list);
		return eudgPagination;
	}
	
	/*String eventTableSql = "E.EVENT_ID,"
			+ "E.CODE_ code,"
			+ "E.EVENT_NAME eventName,"
			+ "E.TYPE_ eventType,"
			+ "E.CONTENT_ content,"
			+ "E.OCCURRED occurred,"
			+ "E.HAPPEN_TIME happenTime,"
			+ "TO_CHAR(E.HAPPEN_TIME, 'YYYY-MM-DD HH24:MI:SS') happenTimeStr,"
			+ "E.GRID_ID gridId,"
			+ "(SELECT G.GRID_NAME FROM "
			+ dataBaseUser
			+ ".T_DC_GRID G WHERE GRID_ID = E.GRID_ID) gridName,"
			+ "E.GRID_CODE gridCode,"
			+ "E.EVENT_NATURE eventNature, "
			+ "E.SOURCE source,"
			+ "E.INVOLVED_NUM involvedNum,"
			+ "E.INFLUENCE_DEGREE influenceDegree,"
			+ "E.URGENCY_DEGREE urgencyDegree,"
			+ "E.TEL tel,"
			+ "E.CONTACT_USER contactUser,"
			+ "E.STATUS eventStatus,"
			+ "E.CREATE_TIME createTime,"
			+ "TO_CHAR(E.CREATE_TIME, 'YYYY-MM-DD HH24:MI:SS') createTimeStr,"
			+ "E.FIN_TIME finTime,"
			+ "TO_CHAR(E.FIN_TIME, 'YYYY-MM-DD HH24:MI:SS') finTimeStr,"
			+ "E.HANDLE_DATE handleDate,TO_CHAR(E.HANDLE_DATE, 'YYYY-MM-DD HH24:MI:SS') handleDateStr,"
			+ "E.ATTR_FLAG,"
			+ "E.HANDLE_DATE_FLAG";*/
	
	String eventTableSql = "E.EVENT_ID";
	
	/**
	 * 格式化输入参数
	 * @param params
	 */
	private void formatParamIn(Map<String, Object> params) {
		if(CommonFunctions.isNotBlank(params, "eInfoOrgCode")) {//有加密的地域编码
			byte[] decodedData = Base64.decode(params.get("eInfoOrgCode").toString());
			params.put("infoOrgCode", new String(decodedData));
		}
		//未传入地域编码，且有传入网格id时，通过网格id转换对应的地域信息
		if(CommonFunctions.isBlank(params, "infoOrgCode") && CommonFunctions.isNotBlank(params, "gridId")) {
			Long gridId = -1L;
			
			try {
				gridId = Long.valueOf(params.get("gridId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
			if(gridId > 0) {
				MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
				if(gridInfo != null) {
					params.put("infoOrgCode", gridInfo.getInfoOrgCode());
				}
			}
		}
	}
	
	/**
	 * 通用查询条件
	 * 
	 * @param params
	 * @return
	 */
	private StringBuffer addWhereSql(Map<String, Object> params) {
		StringBuffer whereSql = new StringBuffer();
		
		formatParamIn(params);
		
		if(CommonFunctions.isNotBlank(params, "eventId")) {
			whereSql.append(" AND E.EVENT_ID = '" + params.get("eventId") + "'");
		}
		if(CommonFunctions.isNotBlank(params, "eventIdList")) {
			Object eventIdListObj = params.get("eventIdList");
			String[] eventIdArray = null;
			
			if(eventIdListObj instanceof String) {
				eventIdArray = ((String) eventIdListObj).split(",");
			} if(eventIdListObj instanceof String[]) {
				eventIdArray = (String[])eventIdListObj;
			} else if(eventIdListObj instanceof List) {
				List<Object> eventIdList = (List<Object>)eventIdListObj;
				List<String> eventIdStrList = new ArrayList<String>();
				
				for(Object eventIdObj : eventIdList) {
					eventIdStrList.add(eventIdObj.toString());
				}
				
				eventIdArray = eventIdStrList.toArray(new String[eventIdStrList.size()]);
			}
			
			if(eventIdArray != null) {
				StringBuffer eventIdBuffer = new StringBuffer("");
				Long eventIdL = -1L;
				
				for(String eventId : eventIdArray) {
					if(StringUtils.isNotBlank(eventId)) {
						try {
							eventIdL = Long.valueOf(eventId);
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
						
						if(eventIdL != null && eventIdL > 0) {
							eventIdBuffer.append(",").append(eventIdL);
						}
					}
				}
				
				if(eventIdBuffer.length() > 0) {
					whereSql.append(" AND E.EVENT_ID IN (").append(eventIdBuffer.substring(1)).append(")");
				}
			}
		}
		if(CommonFunctions.isNotBlank(params, "content")) {
			whereSql.append(" AND E.CONTENT_ LIKE '%" + params.get("content") +"%'");
		}
		if(CommonFunctions.isNotBlank(params, "eventName")) {
			whereSql.append(" AND E.EVENT_NAME LIKE '%" + params.get("eventName") +"%'");
		}
		if(CommonFunctions.isNotBlank(params, "contingencyPlan")) {
			String contingencyPlan = params.get("contingencyPlan").toString();
			
			if("important".equals(contingencyPlan)){//应急预案--重大或紧急事件
				whereSql.append(" AND (E.URGENCY_DEGREE IN ('02','03') OR (E.URGENCY_DEGREE != '04' AND E.INFLUENCE_DEGREE = '04'))");
			} else if("urgency".equals(contingencyPlan)){//应急预案--重大应急事件
				whereSql.append(" AND (E.URGENCY_DEGREE = '04' AND E.TYPE_ in ('0216','0217','0218'))");
			}
		}
		if(CommonFunctions.isNotBlank(params, "keyWord")) {
			whereSql.append(" AND (E.CONTENT_ LIKE '%" + params.get("keyWord") +"%' OR E.EVENT_NAME LIKE '%" + params.get("keyWord") +"%' OR E.OCCURRED LIKE '%" + params.get("keyWord") +"%')");
		}
//		if(params.get("gridId")!=null){
//			whereSql.append(" AND E.GRID_ID IN (SELECT GRID_ID FROM "+dataBaseUser+".T_DC_GRID WHERE STATUS='001' START WITH GRID_ID=" + params.get("gridId") +" CONNECT BY PRIOR GRID_ID=PARENT_GRID_ID)");
//		}
		
		//增添网格有效性过滤条件--------------begin--------------
		StringBuffer subQuery = new StringBuffer("");
		subQuery.append(" SELECT 1 FROM ").append(dataBaseUser).append(".T_DC_GRID WHERE GRID_ID = E.GRID_ID AND STATUS='001'");
		
		if(params.get("infoOrgCode")!=null) {
			subQuery.append(" AND INFO_ORG_CODE LIKE '").append(params.get("infoOrgCode")).append("%'");
		}
		
		whereSql.append(" AND EXISTS ( ").append(subQuery).append(" )");
		//增添网格有效性过滤条件--------------end--------------


		// add by zhongshm 采集人编码
		if(params.get("creatorOrgCode")!=null) {
			StringBuffer sub2Query = new StringBuffer("");
			sub2Query.append(" SELECT 1 FROM ").append(dataBaseUser).append(".T_DC_PRIVILEGE_EXAMPLE INNER JOIN ").append(dataBaseUser).append(".T_DC_ORG_SOCIAL_INFO S ON S.ORG_ID = SOCIAL_ORG_ID WHERE USER_ID = E.CREATOR_ID");
			sub2Query.append(" AND S.ORG_CODE LIKE '").append(params.get("creatorOrgCode")).append("%'");
			whereSql.append(" AND EXISTS ( ").append(sub2Query).append(" )");
		}

		// ADD BY @YangCQ : type支持多个事件类型大类和小类，以逗号隔开。（types废除）
		String type = WindowHelper.getPmStr(params.get("type"));
		if (!"".equals(type)) {
			String[] types = type.split(",");

			List<String> list = new ArrayList<String>();
			for (int i = 0,len = types.length;i < len;i++) {
				if (StringUtils.isNotBlank((types[i]))) {
					list.add(types[i]);
				}
			}
			types = list.toArray(new String[types.length]);

			if (types.length > 0 && types.length < 2) {
				whereSql.append(" AND E.TYPE_ LIKE '" + types[0] + "%'");
			} else if (types.length > 1) {
				whereSql.append(" AND (");
				for (int i = 0; i < types.length; i++) {
					if(StringUtils.isNotBlank(types[i])) {
						if (i > 0) {
							whereSql.append(" OR ");
						}
						whereSql.append(" E.TYPE_ LIKE '" + types[i] + "%'");
					}
				}
				whereSql.append(") ");
			}
		}
		/*if(params.get("type")!=null && !"".equals(params.get("type"))){
			whereSql.append(" AND E.TYPE_ = '" + params.get("type") + "'");
		}*/

		if(CommonFunctions.isNotBlank(params, "types") || CommonFunctions.isNotBlank(params, "trigger")) {
			boolean isRemoveTypes = false;
			String types = "";
			
			if(CommonFunctions.isNotBlank(params, "isRemoveTypes")) {//isRemoveTypes为true时，去除types中包含的事件类型；isRemoveTypes为false时，展示types中包含的事件类型
				isRemoveTypes = Boolean.valueOf(params.get("isRemoveTypes").toString());
			}
			
			if(CommonFunctions.isNotBlank(params, "types")) {
				types = params.get("types").toString().trim();
			}else if(CommonFunctions.isNotBlank(params, "trigger")) {
				String trigger = params.get("trigger").toString(),
					   orgCode = "";
				
				if(CommonFunctions.isNotBlank(params, "orgCode")) {
					orgCode = params.get("orgCode").toString();
				}
				
				types = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
				
				if(StringUtils.isBlank(types)) {
					isRemoveTypes = true;
					types = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
				}
			}
			
			if(StringUtils.isNotBlank(types)) {
				String[] typesArray = types.split(",");
				
				if(typesArray.length > 0) {
					if(isRemoveTypes) {
						for(String _type : typesArray) {
							if(StringUtils.isNotBlank(_type)) {
								whereSql.append(" AND E.TYPE_ NOT LIKE '").append(_type).append("%'");
							}
						}
					} else {
						whereSql.append(" AND ( 1 != 1 ");
						for(String _type : typesArray) {
							if(StringUtils.isNotBlank(_type)) {
								whereSql.append(" OR E.TYPE_ LIKE '" + _type + "%'");
							}
						}
						whereSql.append(") ");
					}
				}
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "happenTimeStart")) {
			whereSql.append(" AND E.HAPPEN_TIME >= TO_DATE('" + params.get("happenTimeStart") + "', 'YYYY-MM-DD') ");
		}
		if(CommonFunctions.isNotBlank(params, "happenTimeEnd")) {
			whereSql.append(" AND E.HAPPEN_TIME < TO_DATE('" + params.get("happenTimeEnd") + "', 'YYYY-MM-DD') + 1 ");
		}
		if(CommonFunctions.isNotBlank(params, "createTimeStart")) {
			whereSql.append(" AND E.CREATE_TIME >= TO_DATE('" + params.get("createTimeStart") + "', 'YYYY-MM-DD HH24:MI:SS') ");
		}
		if(CommonFunctions.isNotBlank(params, "createTimeEnd")) {
			whereSql.append(" AND E.CREATE_TIME < TO_DATE('" + params.get("createTimeEnd") + "', 'YYYY-MM-DD HH24:MI:SS') + 1 ");
		}
		if(CommonFunctions.isNotBlank(params, "code")) {
			whereSql.append(" AND E.CODE_ like '%" + params.get("code") + "%'");
		}
		if(CommonFunctions.isNotBlank(params, "contactUser")){
			whereSql.append(" AND E.CONTACT_USER = '" + params.get("contactUser") + "'");
		}
		if(CommonFunctions.isNotBlank(params, "collectWay")){
			whereSql.append(" AND E.COLLECT_WAY in (" + params.get("collectWay") + ")");
		}
		if(CommonFunctions.isNotBlank(params, "source")){
			whereSql.append(" AND E.SOURCE in (" + params.get("source") + ")");
		}
		if(CommonFunctions.isNotBlank(params, "attrFlag")){
			String attrFlag = params.get("attrFlag").toString();
			
			if(!attrFlag.contains(",%")) {
				attrFlag = attrFlag.replaceAll(",", ",%");
			}
			whereSql.append(" AND E.ATTR_FLAG LIKE '%" + attrFlag + "%'");
		}
		if(CommonFunctions.isNotBlank(params, "handleDateFlag")){
			whereSql.append(" AND E.HANDLE_DATE_FLAG = '" + params.get("handleDateFlag") + "'");
		}
		
		if(CommonFunctions.isNotBlank(params, "influenceDegree")){
			whereSql.append(" AND E.INFLUENCE_DEGREE in (" + params.get("influenceDegree") + ")");
		}
		if(CommonFunctions.isNotBlank(params, "urgencyDegree")){
			whereSql.append(" AND E.URGENCY_DEGREE in (" + params.get("urgencyDegree") + ")");
		}
		if(CommonFunctions.isNotBlank(params, "statusList")){
			whereSql.append(" AND E.STATUS in (" + params.get("statusList") + ")");
		}
		if(CommonFunctions.isNotBlank(params, "creatorId")){
			whereSql.append(" AND E.CREATOR_ID in (" + params.get("creatorId") + ")");
		}
		if(CommonFunctions.isNotBlank(params, "finTimeStart")){
			whereSql.append(" AND E.HANDLE_DATE <= TO_DATE('" + params.get("finTimeStart") + "', 'YYYY-MM-DD') ");
		}
		if(CommonFunctions.isNotBlank(params, "finTimeEnd")){
			whereSql.append(" AND E.HANDLE_DATE >= TO_DATE('" + params.get("finTimeEnd") + "', 'YYYY-MM-DD') ");
		}
		if(CommonFunctions.isNotBlank(params, "overTimeStart")){
			whereSql.append(" AND E.FIN_TIME <= TO_DATE('" + params.get("overTimeStart") + "', 'YYYY-MM-DD') ");		
		}
		if(CommonFunctions.isNotBlank(params, "overTimeEnd")){
			whereSql.append(" AND E.FIN_TIME >= TO_DATE('" + params.get("overTimeEnd") + "', 'YYYY-MM-DD') ");
		}
		if(CommonFunctions.isNotBlank(params, "handleStatuss")){
			if(params.get("handleStatuss").equals(ConstantValue.HANDLE_STATUS_IN)){//将到期
				whereSql.append(" AND SYSDATE > E.HANDLE_DATE -1 AND SYSDATE < E.HANDLE_DATE");
			}
			if(params.get("handleStatuss").equals(ConstantValue.HANDLE_STATUS_OVER_TIME)){//江阴大屏超期
				whereSql.append(" AND ((E.FIN_TIME IS NULL   and  SYSDATE > E.HANDLE_DATE)"
						+ " or( E.FIN_TIME IS NOT NULL  and E.FIN_TIME>E.HANDLE_DATE))");
			}
		}
		if(CommonFunctions.isNotBlank(params, "handleStatus")){
			whereSql.append(" AND (");
			String[] handleStatuss = params.get("handleStatus").toString().split(",");
			for(int i = 0; i< handleStatuss.length; i++){
				if(i > 0){
					whereSql.append(" OR ");
				}
				if(handleStatuss[i].equals("00")){//未过期
					whereSql.append("SYSDATE < E.HANDLE_DATE -1");
				}
				if(handleStatuss[i].equals("01")){//将过期
					whereSql.append("SYSDATE > E.HANDLE_DATE -1 AND SYSDATE < E.HANDLE_DATE");
				}
				if(handleStatuss[i].equals("02")){//已过期
					whereSql.append("SYSDATE > E.HANDLE_DATE");
				}
			}
			whereSql.append(")");
		}
		whereSql.append(" AND E.STATUS NOT IN ('06', '99') ");
		if(params.get("isRemind")!=null && (Boolean)params.get("isRemind")){
			//whereSql.append(" AND T5.REMIND_STATUS='2'");
			whereSql.append(" AND WIS.SUPERVISE_MARK IS NOT NULL ");
		}
		/*if(params.get("remindStatus")!=null && !"".equals(params.get("remindStatus"))){
			whereSql.append(" AND T5.REMIND_STATUS=").append(params.get("remindStatus"));
		}*/
		if(CommonFunctions.isNotBlank(params, "remindStatus")) {
			String remindStatus = params.get("remindStatus").toString();
			if("1".equals(remindStatus)){
				whereSql.append(" AND WIS.REMIND_MARK IS NOT NULL ");
			}else if("2".equals(remindStatus)){
				whereSql.append(" AND WIS.SUPERVISE_MARK IS NOT NULL ");
			}
		}
		/*if(params.get("types")!=null && !params.get("types").equals("")){
			whereSql.append(" AND E.TYPE_ IN ("+ params.get("types") +")");
		}*/

		//经办/待办环节筛选条件
		if(CommonFunctions.isNotBlank(params, "curHandledTaskName")){
			whereSql.append("  AND ( EXISTS ( SELECT 1 FROM ").append(ConstantValue.WORKFLOW_DB).append("WF_TASK C,")
					.append(ConstantValue.WORKFLOW_DB).append("WF_PROCESS_INSTANCE B")
					.append(" 	WHERE C.INSTANCE_ID = B.INSTANCE_ID AND C.TASK_NAME = '" + params.get("curHandledTaskName") + "'")
					.append(" AND E.EVENT_ID = B.FORM_ID and B.FORM_TYPE_ID = 300)")
					.append("	OR EXISTS (  SELECT 1 FROM ").append(ConstantValue.WORKFLOW_DB).append("JBPM4_TASK D,")
					.append(ConstantValue.WORKFLOW_DB).append("WF_PROCESS_INSTANCE B")
					.append("  WHERE D.PROCINST_ = B.INSTANCE_ID AND D.NAME_ =  '"+ params.get("curHandledTaskName") + "'")
					.append(" AND E.EVENT_ID = B.FORM_ID and B.FORM_TYPE_ID = 300))");
		}

		if(CommonFunctions.isNotBlank(params, "eventAttrTrigger")){
			String eventAttrTrigger = params.get("eventAttrTrigger").toString();
			String eventAttrName = "";
			String orgCode = "";
			
			if(CommonFunctions.isNotBlank(params, "eventAttrOrgCode")){
				orgCode = params.get("eventAttrOrgCode").toString();
			}
			
			eventAttrName = configurationService.changeCodeToValue(ConstantValue.EVENT_ATTRIBUTE, eventAttrTrigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
			
			if(StringUtils.isNotBlank(eventAttrName)){
				String[] eventAttrNameArray = eventAttrName.split(",");
				String attrValue = "";
				
				for(String attrName : eventAttrNameArray){
					attrValue = configurationService.changeCodeToValue(ConstantValue.EVENT_ATTRIBUTE, eventAttrTrigger+"_"+attrName, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
					
					if(StringUtils.isNotBlank(attrValue)){
						whereSql.append(" AND E.").append(attrName).append(" IN ('").append(attrValue.trim().replaceAll(",", "','")).append("')");
					} else {
						attrValue = configurationService.changeCodeToValue(ConstantValue.EVENT_ATTRIBUTE, eventAttrTrigger+"_"+attrName+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
						
						if(StringUtils.isNotBlank(attrValue)) {
							whereSql.append(" AND E.").append(attrName).append(" NOT IN ('").append(attrValue.trim().replaceAll(",", "','")).append("')");
						}
					}
				}
			}
		}
		//增添当前任务是否接收的查询条件
		if(CommonFunctions.isNotBlank(params, "taskReceivedStaus")) {
			whereSql.append(" AND NVL(TR.IS_RECEIVE, 0) = ").append(params.get("taskReceivedStaus"));
		}
		//增添评价等级查询，以最后一次评价结果为准
		if(CommonFunctions.isNotBlank(params, "evaLevelList")) {
			Object evaLevelListObj = params.get("evaLevelList");
			StringBuffer evaLevel = new StringBuffer("");
			String[] evaLevelArray = null;
			
			if(evaLevelListObj instanceof List) {
				List<String> evaLevelList = (List<String>)evaLevelListObj;
				
				evaLevelArray = evaLevelList.toArray(new String[evaLevelList.size()]);
			} else if(evaLevelListObj instanceof String[]) {
				evaLevelArray = (String[])evaLevelListObj;
			} else if(evaLevelListObj instanceof String) {
				evaLevelArray = evaLevelListObj.toString().split(",");
			}
			
			//为了防止有多次评价的情况，需要使用max来获取最后一次的评价信息
			whereSql.append(" AND EXISTS (SELECT 1 FROM ").append(dataBaseUser).append(".T_SJ_EVA_RESULT evaResult ")
					.append(" 		  WHERE evaResult.ER_ID = ( ")
					.append("			SELECT MAX(maxEvaResult.ER_ID) FROM ").append(dataBaseUser).append(".T_SJ_EVA_RESULT maxEvaResult ")
					.append("			WHERE maxEvaResult.EVA_OBJ = '").append(ConstantValue.EVA_OBJ_NEW_EVENT).append("' AND maxEvaResult.OBJECT_ID = E.EVENT_ID ")
					.append("		  ) ");
			
			if(evaLevelArray != null) {
				for(String evalLevel : evaLevelArray) {
					evaLevel.append(",'").append(evalLevel.trim()).append("'");
				}
				
				if(evaLevel.length() > 0) {
					evaLevel = new StringBuffer(evaLevel.substring(1));
					
					whereSql.append(" AND evaResult.EVA_LEVEL IN ( ").append(evaLevel).append(") "); 
				}
			}
			
			whereSql.append("		)");
			
		}
		
		logger.info(whereSql.toString());
		//System.out.println(whereSql.toString());
		return whereSql;
	}

	/**
	 * 通用结果集转换
	 * 
	 * @param listMap
	 * @return
	 */
	private List<EventDisposal> resultMapToEvent(List<Map> listMap, Long userId, boolean includeAction, Map<String, Object> params) {
		List<EventDisposal> list = new ArrayList<EventDisposal>();
		for (Map eventMap : listMap) {
			EventDisposal e = new EventDisposal();
			if (eventMap.get("EVENT_ID") != null) {
				Long eventId = Long.parseLong(eventMap.get("EVENT_ID").toString());
				e = this.findEventByIdSimple(eventId);
				if(e == null) {//不是有效的事件
					new Exception("事件["+ eventId +"] 没有对应有效的事件！").printStackTrace();
					continue;
				}
			}
			if (eventMap.get("WF_INSTANCE_ID") != null) {
				e.setInstanceId(eventMap.get("WF_INSTANCE_ID").toString());
			}
			if (eventMap.get("WF_WORKFLOW_ID")!=null){
				e.setWorkFlowId(eventMap.get("WF_WORKFLOW_ID").toString());
			}
			if (eventMap.get("WF_DBID_") != null) {
				e.setTaskId(eventMap.get("WF_DBID_").toString());
			}
			if (eventMap.get("WF_USERID") != null) {
				e.setUserIds(eventMap.get("WF_USERID").toString());
			}
			if (eventMap.get("WF_USERNAME") != null) {
				e.setUserNames(eventMap.get("WF_USERNAME").toString());
			}
			if(eventMap.get("INFLUENCEDEGREE")!=null){
				e.setInfluenceDegree(eventMap.get("INFLUENCEDEGREE").toString());
			}
			if(eventMap.get("WF_TASK_NAME")!=null){
				e.setTaskName(eventMap.get("WF_TASK_NAME").toString());
			}
			if(eventMap.get("WF_OPERATE")!=null){
				e.setOperate(eventMap.get("WF_OPERATE").toString());
			}
			/*if(eventMap.get("WF_REMIND_STATUS")!=null){
				e.setRemindStatus(eventMap.get("WF_REMIND_STATUS").toString());
			}*/
			if(eventMap.get("REMIND_MARK")!=null){//催办
				e.setRemindStatus("1");
			}
			if(eventMap.get("SUPERVISE_MARK")!=null){//督办
				String remindStatus = e.getRemindStatus();
				if(StringUtils.isNotBlank(remindStatus)){
					e.setRemindStatus("3");
				}else{
					e.setRemindStatus("2");
				}
			}
			if(eventMap.get("WF_STATUS")!=null){
				String wfStatus = eventMap.get("WF_STATUS").toString();
				String wfStatusName = "";
				if (workFlowStatusStart.equals(wfStatus)) {
					wfStatusName = ConstantValue.EVENT_WORKFLOW_STATUS_START_NAME;
				} else if (workFlowStatusEnd.equals(wfStatus)) {
					wfStatusName = ConstantValue.EVENT_WORKFLOW_STATUS_END_NAME;
				} else {
					wfStatusName = ConstantValue.EVENT_WORKFLOW_STATUS_DEFAULT_NAME;
				}
				e.setTaskStatusName(wfStatusName);
				e.setWfStatus(wfStatus);
			}
			if(eventMap.get("WF_ATTENTION_STATUS")!=null){
				e.setIsAttention("1".equals(eventMap.get("WF_ATTENTION_STATUS")));
			}else{
				e.setIsAttention(false);
			}
			if(eventMap.get("WF_CREATE_")!=null){
				e.setWfCreate(eventMap.get("WF_CREATE_").toString());
			}
			if(eventMap.get("WF_REMARKS")!=null){
				e.setRemarks(eventMap.get("WF_REMARKS").toString());
			}
			if(eventMap.get("WF_REMINDED_USER_NAME")!=null){
				e.setRemindedUserName(eventMap.get("WF_REMINDED_USER_NAME").toString());
			}
			if(eventMap.get("WF_REMIND_USER_NAME")!=null){
				e.setRemindUserName(eventMap.get("WF_REMIND_USER_NAME").toString());
			}
			if(eventMap.get("WF_REMIND_DATE")!=null){
				e.setRemindDate(eventMap.get("WF_REMIND_DATE").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "IS_RECEIVE")) {//设置任务接收状态 0：未接收；1：表示已接收
				e.setTaskReceivedStaus(eventMap.get("IS_RECEIVE").toString());
			}
			if(eventMap.get("SUPERVISION_TYPE")!=null){
				e.setSupervisionType(eventMap.get("SUPERVISION_TYPE").toString());
			}
			list.add(e);
		}
		if (list.size() > 0) {
			formatEvent(list, params);
		}
		return list;
	}
	
	/**
	 * 为事件设置默认属性值
	 * @param event
	 * @param userInfo
	 */
	public EventDisposal defaultEvent(EventDisposal event, UserInfo userInfo){
		String DEFAULT_INFLUENCE_DEGREE = "01";//影响范围默认值 小
		String DEFAULT_SOURCE = "01";//信息来源默认值 目击
		String DEFAULT_URGENCY_DEGREE = ConstantValue.URGENCY_DEGREE_1;//紧急程度默认值 一般
		String DEFAULT_INVOLVED_NUM = "00";//涉及人数默认值 无
		
		if(event != null){
			if(StringUtils.isBlank(event.getCollectWay()) || StringUtils.isBlank(event.getCollectWay().trim())){
				event.setCollectWay(ConstantValue.COLLECT_WAY_PC);// PC录入
			}
			if(StringUtils.isBlank(event.getStatus()) || StringUtils.isBlank(event.getStatus().trim())){
				event.setStatus(ConstantValue.EVENT_STATUS_DRAFT);// 99
			}
			if(StringUtils.isBlank(event.getInfluenceDegree()) || StringUtils.isBlank(event.getInfluenceDegree().trim())){
				event.setInfluenceDegree(DEFAULT_INFLUENCE_DEGREE);
			}
			if(StringUtils.isBlank(event.getSource()) || StringUtils.isBlank(event.getSource().trim())){
				event.setSource(DEFAULT_SOURCE);
			}
			if(StringUtils.isBlank(event.getUrgencyDegree()) || StringUtils.isBlank(event.getUrgencyDegree().trim())){
				event.setUrgencyDegree(DEFAULT_URGENCY_DEGREE);
			}
			if(StringUtils.isBlank(event.getInvolvedNum()) || StringUtils.isBlank(event.getInvolvedNum().trim())){
				event.setInvolvedNum(DEFAULT_INVOLVED_NUM);
			}
			if(StringUtils.isBlank(event.getHappenTimeStr()) || StringUtils.isBlank(event.getHappenTimeStr().trim())){
				event.setHappenTimeStr(DateUtils.getNow());
			}
			if(event.getGridId()!=null && StringUtils.isBlank(event.getGridCode())){
				MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(event.getGridId(), false);
				if(mixedGridInfo != null){
					event.setGridCode(mixedGridInfo.getGridCode());
					event.setGridName(mixedGridInfo.getGridName());
				}
			}
			
			if(userInfo != null){
				if(event.getCreatorId()==null || event.getCreatorId().equals(-1L)){
					event.setCreatorId(userInfo.getUserId());
				}
				//事件对接中要求不自动填写联系人信息 2016-03-08
				/*if(StringUtils.isBlank(event.getContactUser()) || StringUtils.isBlank(event.getContactUser().trim())){
					event.setContactUser(userInfo.getPartyName());
				}
				if(StringUtils.isBlank(event.getTel()) || StringUtils.isBlank(event.getTel().trim())){
					event.setTel(userInfo.getVerifyMobile());
				}*/
				if(StringUtils.isNotBlank(event.getType()) && StringUtils.isBlank(event.getTypeName())){
					String typeName = dictionaryService.changeCodeToName(ConstantValue.BIG_TYPE_PCODE, event.getType(), userInfo.getOrgCode(), true);
					
					event.setTypeName(typeName);
				}
			}
		}
		
		return event;
	}
	
	@Override
	public boolean isUserNewHandleEvent(Long eventId, UserInfo userInfo){
		boolean isUserNewHandleEvent = false;
		
		if(eventId!=null && eventId>0 && userInfo!=null){
			EventDisposal event = this.findEventByIdSimple(eventId);//验证eventId有效性
			
			if(event != null){
				//判断当前组织是否使用新组织
				boolean isNewOrganization = eventOrgInfoService.isNewOrganization(userInfo.getOrgId());
				
				//是否启用新的事件办理页面
				String isUseNewHandleEventStr = configurationService.turnCodeToValue(ConstantValue.IS_USER_NEW_HANDLE_EVENT, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
				//启用新的事件办理页面的时间
				String timeToUseNewHandleEvent = configurationService.turnCodeToValue(ConstantValue.TIME_TO_USE_NEW_HANDLE_EVENT, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
				String createTime = event.getCreateTimeStr();
				
				boolean isUseNewHandleEventFlag = Boolean.valueOf(isUseNewHandleEventStr);
				boolean isTimeToUseNewHandleEvent = true;
				if(StringUtils.isNotBlank(createTime) && createTime.length()>=10){
					createTime = createTime.substring(0, 10);
					isTimeToUseNewHandleEvent = createTime.compareTo(timeToUseNewHandleEvent) > 0;
				}
				
				isUserNewHandleEvent = isNewOrganization || (isUseNewHandleEventFlag && isTimeToUseNewHandleEvent);
			}
		}
		
		return isUserNewHandleEvent;
	}

    @Override
    public boolean pushRemindMsg(Long instanceId,String infoOrgCode,Map<String, Object> params) {
        // 是否发送rmq消息
        Boolean isContain = false;
        if (StringUtils.isNotBlank(RMQ_IS_OPEN)) {
            String[] infoOrgCodeArr = RMQ_IS_OPEN.split("-");
            for (String code : infoOrgCodeArr) {
                if (infoOrgCode.startsWith(code)) {
                    isContain = true;
                    break;
                }
            }
        }
        if (isContain){
            Map<String,Object> mqParams=new HashMap<String,Object>();
            mqParams.put("eventFlag", "remind");
            mqParams.put("instanceId", instanceId);
            mqParams.put("remindInfo", params);
            //发送rmq消息
            try {
                sendEventStatusRmqMsg(mqParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
	 * 获取处理时限的工作日时间间隔
	 * @param urgencyDegree 紧急程度
	 * @param orgCode 组织编码
	 * @return 一般 30工作日天；紧急 1工作日天；其他0天
	 */
	private int countDayInterval(String urgencyDegree, UserInfo userInfo){
		int dayIntervalInt = 0;
		if(userInfo != null) {
			String dayInterval = configurationService.changeCodeToValue(ConstantValue.HANDLE_DATE_INTERVAL, urgencyDegree, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
			if(StringUtils.isNotBlank(dayInterval)){
				try{
					dayIntervalInt = Integer.valueOf(dayInterval);
				}catch(Exception e){
					e.printStackTrace();
					dayIntervalInt = 0;
				}
			}
		} else {
			logger.error("用户信息为空，时限时间间隔获取失败！");
		}
		
		return dayIntervalInt;
	}
	
	/**
	 * 事件附件信息关联事件id
	 * @param event
	 * @return 至少有一条更新成功，返回true；否则，返回false
	 */
	private boolean saveEventAttachment(EventDisposal event) {
		boolean result = false;
		
		if(event != null) {
			String attachmentIds = event.getAttachmentIds();
			Long eventId = event.getEventId();
			
			if(StringUtils.isNotBlank(attachmentIds) && eventId != null && eventId > 0) {
				result = attachmentService.updateBizId(eventId, ConstantValue.EVENT_ATTACHMENT_TYPE, attachmentIds);
			}
		}
		
		return result;
	}
	
	/**
	 * 事件保存/更新参数格式化输入
	 * @param event
	 */
	private void formatEvent4Save(EventDisposal event) {
		if(event != null) {
			String infoOrgCode = event.getGridCode();
			Long gridId = event.getGridId();
			
			if(gridId != null && gridId > 0) {
				MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
				if(mixedGridInfo != null) {
					event.setGridCode(mixedGridInfo.getInfoOrgCode());
				}
			} else if(StringUtils.isNotBlank(infoOrgCode)) {
				MixedGridInfo defaultGridByOrgCode = mixedGridInfoService.getDefaultGridByOrgCode(infoOrgCode);
				if(defaultGridByOrgCode != null) {
					event.setGridId(defaultGridByOrgCode.getGridId());
				}
			}
		}
	}
	
	
	/**
	 * 使用RMQ 发送事件状态变化消息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private Boolean sendEventStatusRmqMsg(Map<String, Object> params) throws Exception {

		if (StringUtils.isNotBlank(RMQ_IS_OPEN)) {

			Object eventMonitorRmqProducer = null;
			try {
				//事件重启时，第一次获取为空，构造一个RmqProducer创建完后续即可直接使用
				eventMonitorRmqProducer = defaultListableBeanFactory.getBean("eventMonitorRmqProducer");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (eventMonitorRmqProducer == null) {
				
				RmqProducer thisProducer = new RmqProducer();
				
				if (StringUtils.isBlank(RMQ_IP)) {
					logger.error("===事件状态推送失败，未配置rmq连接的IP地址，请在zk中配置rmq.ip后重新启动");
					return false;
				} else {
					thisProducer.setRmqServerIP(RMQ_IP);
				}
				
				if (StringUtils.isBlank(RMQ_PORT)) {
					logger.error("===事件状态推送失败，未配置rmq连接的PORT端口，请在zk中配置rmq.port后重新启动");
					return false;
				} else {
					thisProducer.setRmqServerPort(Integer.valueOf(RMQ_PORT));
				}
				
				if (StringUtils.isBlank(RMQ_VHOST_EVENT)) {
					logger.error("===事件状态推送失败，未配置rmq连接的虚拟主机，请在zk中配置rmq.vhost_event后重新启动");
					return false;
				} else {
					thisProducer.setRmqVirtualHost(RMQ_VHOST_EVENT);
				}
				
				if (StringUtils.isBlank(RMQ_EXCHANGESTR_EVENT)) {
					logger.error("===事件状态推送失败，未配置rmq连接的交换机，请在zk中配置rmq.exchangeStr_event后重新启动");
					return false;
				} else {
					thisProducer.setExchangeStr(RMQ_EXCHANGESTR_EVENT);
				}

				eventMonitorRmqProducer = thisProducer;
				// 将new出的对象放入Spring容器中
				defaultListableBeanFactory.registerSingleton("eventMonitorRmqProducer", thisProducer);
				// 自动注入依赖
				beanFactory.autowireBean(thisProducer);

				// 重连机制初始化
				thisProducer.afterPropertiesSet();

			}

			RmqProducer flowRmqProducer = (RmqProducer) eventMonitorRmqProducer;

			try {

				flowRmqProducer.sendMessage(RMQ_EXCHANGESTR_EVENT, params);

			} catch (Exception e) {

				logger.error("发送rmq信息出错，事件状态推送失败!");
				e.printStackTrace();

			}
		}

		return true;
	}
	
}
