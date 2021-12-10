package cn.ffcs.zhsq.eventExtend.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PointInfo;
import cn.ffcs.shequ.zzgl.service.grid.IPointInfoService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.persistence.eventExtend.EventExtendMapper;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * 事件扩展属性
 * @author zhangls
 *
 */
@Service(value = "eventExtendService")
public class EventExtendServiceImpl implements IEventExtendService {
	@Autowired
	private EventExtendMapper eventExtendMapper;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private ITimeApplicationService timeApplicationService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	//责任点位
	@Autowired
	private IPointInfoService pointInfoService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final String STATUS_VALID = "1";//有效状态
	
	@Override
	public Long saveEventExtend(Map<String, Object> eventExtend) throws Exception {
		Long extendId = -1L;
		StringBuffer msgWrong = new StringBuffer("");
		
		if(CommonFunctions.isBlank(eventExtend, "eventId")) {
			msgWrong.append("缺少属性[eventId]，请检查！");
		} else {
			Long eventId = Long.valueOf(eventExtend.get("eventId").toString());
			Map<String, Object> eventEx = this.findEventExtendByEventId(eventId);
			if(CommonFunctions.isNotBlank(eventEx, "extendId")) {
				extendId = Long.valueOf(eventEx.get("extendId").toString());
			}
		}
		
		if(extendId > 0) {
			eventExtend.put("extendId", extendId);
			eventExtend.remove("eventId");
			
			this.updateEventExtendById(eventExtend);
		} else {
			if(msgWrong.length() > 0) {
				throw new Exception(msgWrong.toString());
			} else {
				this.formatParamIn(eventExtend);
				
				if(eventExtendMapper.insert(eventExtend) > 0) {
					extendId = Long.valueOf(eventExtend.get("extendId").toString());
				}
			}
		}
		
		return extendId;
	}

	@Override
	public boolean updateEventExtendById(Map<String, Object> eventExtend) throws Exception {
		boolean flag = false;
		StringBuffer msgWrong = new StringBuffer("");
		
		if(CommonFunctions.isBlank(eventExtend, "extendId")) {
			msgWrong.append("缺少属性[extendId]，请检查！");
		} else {
			if(CommonFunctions.isNotBlank(eventExtend, "eventId")) {
				Long eventId = Long.valueOf(eventExtend.get("eventId").toString());
				Map<String, Object> eventEx = this.findEventExtendByEventId(eventId);
				
				if(CommonFunctions.isNotBlank(eventEx, "extendId")) {
					if(!eventEx.get("extendId").equals(eventExtend.get("extendId"))) {
						msgWrong.append("事件id为[").append(eventId).append("] 的事件已存在扩展信息[").append(eventEx.get("extendId")).append("]，请检查!");
					}
				}
			}
			if(CommonFunctions.isBlank(eventExtend, "updaterId") && CommonFunctions.isNotBlank(eventExtend, "creatorId")) {
				eventExtend.put("updaterId", eventExtend.get("creatorId"));
			}
		}
		
		if(msgWrong.length() > 0) {
			throw new Exception(msgWrong.toString());
		} else {
			this.formatParamIn(eventExtend);
			
			flag = eventExtendMapper.update(eventExtend) > 0;
		}
		
		return flag;
	}

	@Override
	public boolean delEventExtendById(Long extendId, Long delUserId) {
		boolean flag = false;
		
		if(extendId != null && extendId > 0) {
			flag = eventExtendMapper.delete(extendId, delUserId) > 0;
		}
		
		return flag;
	}

	@Override
	public Map<String, Object> findEventExtendById(Long extendId) {
		Map<String, Object> eventExtend = null;
		
		if(extendId != null && extendId > 0) {
			eventExtend = eventExtendMapper.findById(extendId);
		}
		
		return eventExtend;
	}

	@Override
	public Map<String, Object> findEventExtendByEventId(Long eventId) {
		Map<String, Object> eventExtend = null;
		
		if(eventId != null && eventId > 0) {
			eventExtend = eventExtendMapper.findByEventId(eventId);
		}
		
		return eventExtend;
	}
	
	@Override
	public EUDGPagination findEventExtendPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		int listType = 0;
		EUDGPagination pagination = null;
		
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isBlank(params, "status")) {
			params.put("status", STATUS_VALID);
		}
		
		this.formatQueryParam(params);
		
		switch(listType) {
			case 1: {//辖区内被督查督办事件
				pagination = this.findEventAllExtendPagination(pageNo, pageSize, params);
				break;
			}
			case 2: {//我的被督查督办事件
				pagination = this.findEventTodoExtendPagination(pageNo, pageSize, params);
				break;
			}
			case 3: {//辖区内事件
				pagination = findEventAllPagination(pageNo, pageSize, params);
				break;
			}
			case 4: {//我的待办
				params.remove("status");
				
				pagination = this.findEventTodoExtendPagination(pageNo, pageSize, params);
				break;
			}
		}
		
		if(pagination == null) {
			pagination = new EUDGPagination();
			pagination.setRows(new ArrayList<Map<String, Object>>());
		}
		
		return pagination;
	}
	
	/**
	 * 获取辖区内事件扩展信息
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	private EUDGPagination findEventAllExtendPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		String userOrgCode = null;
		int count = 0;
		List<Map<String, Object>> eventExtendList = new ArrayList<Map<String, Object>>();
		
		if(CommonFunctions.isBlank(params, "infoOrgCode") && CommonFunctions.isNotBlank(params, "startInfoOrgCode")) {
			params.put("infoOrgCode", params.get("startInfoOrgCode"));
		}
		if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		}
		
		count = eventExtendMapper.findCount4EventExtendAll(params);
		
		if(count > 0) {
			eventExtendList = eventExtendMapper.findPageList4EventExtendAll(params, rowBounds);
			
			formatEventExDataOut(eventExtendList, userOrgCode);
		}
		
		EUDGPagination eventExtendPagination = new EUDGPagination(count, eventExtendList);
		
		return eventExtendPagination;
	}
	
	/**
	 * 获取辖区内事件信息
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	private EUDGPagination findEventAllPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		String userOrgCode = null;
		int count = 0;
		List<Map<String, Object>> eventList = new ArrayList<Map<String, Object>>();
		
		if(CommonFunctions.isBlank(params, "infoOrgCode") && CommonFunctions.isNotBlank(params, "startInfoOrgCode")) {
			params.put("infoOrgCode", params.get("startInfoOrgCode"));
		}
		if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		}
		
		count = eventExtendMapper.findCount4EventAll(params);
		
		if(count > 0) {
			eventList = eventExtendMapper.findPageList4EventAll(params, rowBounds);
			
			formatEventExDataOut(eventList, userOrgCode);
		}
		
		EUDGPagination eventPagination = new EUDGPagination(count, eventList);
		
		return eventPagination;
	}
	
	/**
	 * 获取待办事件扩展信息
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * 			operateUserId	操作用户id
	 * 			operateOrgId	操作用户组织id
	 * 			curUserId		当前办理用户id，String类型
	 * 			curOrgId		当前办理组织id，String类型
	 * @return
	 */
	private EUDGPagination findEventTodoExtendPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		String userOrgCode = null;
		List<Map<String, Object>> eventExtendList = new ArrayList<Map<String, Object>>();
		
		if(CommonFunctions.isBlank(params, "curUserId") && CommonFunctions.isNotBlank(params, "operateUserId")) {
			params.put("curUserId", params.get("operateUserId").toString());
		}
		if(CommonFunctions.isBlank(params, "curOrgId") && CommonFunctions.isNotBlank(params, "operateOrgId")) {
			params.put("curOrgId", params.get("operateOrgId").toString());
		}
		if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		}
		
		int count = eventExtendMapper.findCount4EventTodo(params);
		
		if(count > 0) {
			eventExtendList = eventExtendMapper.findPageList4EventTodo(params, rowBounds);
			
			formatEventExDataOut(eventExtendList, userOrgCode);
		}
		
		EUDGPagination eventExtendPagination = new EUDGPagination(count, eventExtendList);
		
		return eventExtendPagination;
	}
	
	@Override
	public Map<String, Object> initTimeApplication4Event(Long eventId, String applicationType, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		Long curTaskId = -1L, instanceId = -1L;
		//事件时限审核回调服务名称
		String eventCallbackServiceName = "timeApplicationCallback4EventService";
		boolean isAutoAudit = true;
		
		params.putAll(extraParam);//提前是为了后续的属性调整
		
		if(StringUtils.isBlank(applicationType) && CommonFunctions.isNotBlank(extraParam, "applicationType")) {
			applicationType = extraParam.get("applicationType").toString();
		}
		if(CommonFunctions.isNotBlank(extraParam, "instanceId")) {
			try {
				instanceId = Long.valueOf(extraParam.get("instanceId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(instanceId < 0) {
			instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
		}
		
		if(CommonFunctions.isNotBlank(extraParam, "curTaskId")) {
			try {
				curTaskId = Long.valueOf(extraParam.get("curTaskId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(curTaskId < 0 && instanceId != null && instanceId > 0) {
			curTaskId = eventDisposalWorkflowService.curNodeTaskId(instanceId);
		}
		
		if(CommonFunctions.isNotBlank(extraParam, "isAutoAudit")) {
			isAutoAudit = Boolean.valueOf(extraParam.get("isAutoAudit").toString());
		}
		
		if(isAutoAudit) {//需要自动审核时，才获取审核人员
			if(ITimeApplicationService.APPLICATION_TYPE.EVENT_INSPECT.getValue().equals(applicationType) || 
					ITimeApplicationService.APPLICATION_TYPE.EVENT_REPORT.getValue().equals(applicationType) || 
					ITimeApplicationService.APPLICATION_TYPE.EVENT_SEND.getValue().equals(applicationType)) {
				if(CommonFunctions.isBlank(extraParam, "auditorId")) {
					params.put("auditorId", userInfo.getUserId());
				}
				if(CommonFunctions.isBlank(extraParam, "auditorOrgId")) {
					params.put("auditorOrgId", userInfo.getOrgId());
				}
				
				params.put("timeAppCheckStatus", ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.getValue());
			} else if(ITimeApplicationService.APPLICATION_TYPE.EVENT_DELAY.getValue().equals(applicationType)) {
				List<Map<String, Object>> taskMapList = eventDisposalWorkflowService.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
				if(taskMapList != null) {//获取延时申请审核人员，该事件对应的最近一次督查督办操作人员
					Long taskId = -1L;
					Map<String, Object> timeAppCheckParam = new HashMap<String, Object>(),
										timeAppCheck = null;
					List<Map<String, Object>> timeAppCheckList = null;
					timeAppCheckParam.put("applicationType", ITimeApplicationService.APPLICATION_TYPE.EVENT_INSPECT.getValue());
					
					for(Map<String, Object> taskMap : taskMapList) {
						taskId = -1L;
						
						if(CommonFunctions.isNotBlank(taskMap, "TASK_ID")) {
							try {
								taskId = Long.valueOf(taskMap.get("TASK_ID").toString());
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
						}
						
						if(taskId > 0) {
							timeAppCheckParam.put("businessId", taskId);
							
							timeAppCheckList = timeApplicationService.findTimeApplicationList(timeAppCheckParam);
							
							if(timeAppCheckList != null && timeAppCheckList.size() > 0) {
								timeAppCheck = timeAppCheckList.get(0);
							}
							
							if(timeAppCheck != null && !timeAppCheck.isEmpty()) {
								if(CommonFunctions.isNotBlank(timeAppCheck, "auditorId")) {
									params.put("auditorId", timeAppCheck.get("auditorId"));
								}
								if(CommonFunctions.isNotBlank(timeAppCheck, "auditorOrgId")) {
									params.put("auditorOrgId", timeAppCheck.get("auditorOrgId"));
								}
								
								break;
							}
						}
					}
				}
			} else if(ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue().equals(applicationType)) {//待办延时申请
				EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
				if(event != null) {
					String eventStatus = event.getStatus();
					
					if(ConstantValue.EVENT_STATUS_REPORT.equals(eventStatus)) {//上报
						OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
						
						if(orgInfo != null && CommonFunctions.isBlank(extraParam, "auditorOrgId")) {
							params.put("auditorOrgId", orgInfo.getParentOrgId());//旧组织树的用法
						}
						
						if(CommonFunctions.isBlank(extraParam, "auditorType")) {//默认设置为 组织 进行审核
							params.put("auditorType", ITimeApplicationCheckService.AUDITOR_TYPE.AUDITOR_ORG.getValue());
						}
					} else if(ConstantValue.EVENT_STATUS_DISTRIBUTE.equals(eventStatus)) {//下派
						List<Map<String, Object>> queryTaskList = eventDisposalWorkflowService.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
						if(queryTaskList != null && queryTaskList.size() > 1) {
							Map<String, Object> parentTaskMap = queryTaskList.get(1);
							
							if(CommonFunctions.isBlank(extraParam, "auditorId") && CommonFunctions.isNotBlank(parentTaskMap, "TRANSACTOR_ID")) {
								params.put("auditorId", parentTaskMap.get("TRANSACTOR_ID"));
							}
							if(CommonFunctions.isBlank(extraParam, "auditorOrgId") && CommonFunctions.isNotBlank(parentTaskMap, "ORG_ID")) {
								params.put("auditorOrgId", parentTaskMap.get("ORG_ID"));
							}
						}
					} else {
						throw new Exception("目前缺少时限审核人员！");
					}
				}
			}
		}
		
		params.put("isAutoAudit", isAutoAudit);
		params.put("businessId", curTaskId);
		params.put("businessKeyId", eventId);
		params.put("applicationType", applicationType);
		params.put("serviceName", eventCallbackServiceName);
		
		return params;
	}
	
	@Override
	public int findAncestorCountByEventId(Long eventId,
			Map<String, Object> params) {
		int count = 0;
		
		if(eventId != null && eventId > 0) {
			if(params == null) {
				params = new HashMap<String, Object>();
			}
			
			params.put("eventId", eventId);
			
			count = eventExtendMapper.findAncestorCountByEventId(params);
		}
		
		return count;
	}

	@Override
	public List<Map<String, Object>> findAncestorListByEventId(Long eventId,
			Map<String, Object> params) {
		List<Map<String, Object>> resultMap = null;
		
		if(eventId != null && eventId > 0) {
			String userOrgCode = "";
			
			if(params == null) {
				params = new HashMap<String, Object>();
			}
			
			params.put("eventId", eventId);
			
			resultMap = eventExtendMapper.findAncestorListByEventId(params);
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			}
			
			formatEventExDataOut(resultMap, userOrgCode);
		}
		
		return resultMap;
	}
	
	/**
	 * 格式化输入参数
	 * @param params
	 */
	private void formatParamIn(Map<String, Object> params) {
		if(CommonFunctions.isNotBlank(params, "extendId")) {//更新
			if(params.containsKey("entryMatterCode") && CommonFunctions.isBlank(params, "entryMatterCode")) {
				params.put("entryMatterCode", 0);
			}
			if(params.containsKey("bonusPoint") && CommonFunctions.isBlank(params, "bonusPoint")) {
				params.put("bonusPoint", 0);
			}
		} else {//新增
			if(CommonFunctions.isBlank(params, "status")) {
				params.put("status", STATUS_VALID);
			}
			if(CommonFunctions.isBlank(params, "integrityScore")) {
				params.put("integrityScore", 0);
			}
			if(CommonFunctions.isBlank(params, "bonusPoint")) {
				params.put("bonusPoint", 0);
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "isBonusPoint")) {
			String GIVE_BONUS_POINT = "1",
				   isBonusPoint = params.get("isBonusPoint").toString();
			
			//只要不是给予积分，就清除积分
			if(!GIVE_BONUS_POINT.equals(isBonusPoint)) {
				params.put("bonusPoint", 0);
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "emtType")) {
			Integer emtType = null;
			
			try {
				emtType = Integer.valueOf(params.get("emtType").toString());
			} catch(NumberFormatException e) {
				if(logger.isErrorEnabled()) {
					logger.error("八员队伍类型【emtType】：【" + emtType + "】不是有效的数值");
				}
			}
			
			if(emtType != null && emtType >= 0) {
				params.put("emtType", emtType);
			} else {
				params.remove("emtType");
			}
		}
	}
	
	/**
	 * 格式化查询参数
	 * @param params
	 */
	@SuppressWarnings("serial")
	private void formatQueryParam(Map<String, Object> params) {
		if(params != null && !params.isEmpty()) {
			//事件的默认状态
			List<String> eventStatusList = new ArrayList<String>() {
				{
					add(ConstantValue.EVENT_STATUS_RECEIVED);
					add(ConstantValue.EVENT_STATUS_REPORT);
					add(ConstantValue.EVENT_STATUS_DISTRIBUTE);
					add(ConstantValue.EVENT_STATUS_ARCHIVE);
					add(ConstantValue.EVENT_STATUS_END);
				}
			};
			
			if(CommonFunctions.isNotBlank(params, "handleDateFlag")) {
				String handleDateFlag = params.get("handleDateFlag").toString();
				
				//超时事件中排除状态为已结束的事件
				if(ConstantValue.LIMIT_TIME_STATUS_EXPIRED.equals(handleDateFlag)) {
					eventStatusList.remove(ConstantValue.EVENT_STATUS_END);
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "eventStatus")) {
				String[] eventStatusArray = params.get("eventStatus").toString().split(",");
				
				eventStatusList.retainAll(Arrays.asList(eventStatusArray));
			}
			
			params.put("eventStatusList", eventStatusList);
			
			if(CommonFunctions.isNotBlank(params, "source")) {
				String[] sourceArray = params.get("source").toString().split(",");
				
				params.put("sourceList", Arrays.asList(sourceArray));
			}
			
			if(CommonFunctions.isNotBlank(params, "urgencyDegree")) {
				String[] urgencyDegreeArray = params.get("urgencyDegree").toString().split(",");
				
				params.put("urgencyDegreeList", Arrays.asList(urgencyDegreeArray));
			}
		}
	}
	
	/**
	 * 格式化输出数据
	 * @param eventExtendList
	 * @param orgCode			组织编码
	 */
	private void formatEventExDataOut(List<Map<String, Object>> eventExtendList, String orgCode) {
		if(eventExtendList != null) {
			Map<String, Object> dictMap = new HashMap<String, Object>();
			String PLAN_TYPE_DICT_PCODE = "A001139", PLAN_LEVEL_DICT_PCODE = "A001140";
			
			dictMap.put("orgCode", orgCode);
			dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
			
			List<BaseDataDict> eventTypeDict = baseDictionaryService.findDataDictListByCodes(dictMap),
							   statusDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.STATUS_PCODE, orgCode),
							   subStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SUB_STATUS_PCODE, orgCode),
			                   patrolTypeList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.PATROL_TYPE_PCODE, orgCode),
			                   planTypeList = baseDictionaryService.getDataDictListOfSinglestage(PLAN_TYPE_DICT_PCODE, orgCode),
			                   planLevelList = baseDictionaryService.getDataDictListOfSinglestage(PLAN_LEVEL_DICT_PCODE, orgCode);
			String eventStatus = null,
				   eventSubStatus = null, eventSubStatusName = null;
			
			for(Map<String, Object> eventExtend : eventExtendList) {
				if(CommonFunctions.isNotBlank(eventExtend, "happenTime") && CommonFunctions.isBlank(eventExtend, "happenTimeStr")) {
					eventExtend.put("happenTimeStr", DateUtils.formatDate((Date)eventExtend.get("happenTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(eventExtend, "handleDate") && CommonFunctions.isBlank(eventExtend, "handleDateStr")) {
					eventExtend.put("handleDateStr", DateUtils.formatDate((Date)eventExtend.get("handleDate"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(eventExtend, "createTime") && CommonFunctions.isBlank(eventExtend, "createTimeStr")) {
					eventExtend.put("createTimeStr", DateUtils.formatDate((Date)eventExtend.get("createTime"), DateUtils.PATTERN_24TIME));
				}
				
				// 设置事件分类全路径
				if(CommonFunctions.isNotBlank(eventExtend, "type") && eventTypeDict != null) {
					Map<String, Object> eventTypeMap = DataDictHelper.capMultilevelDictInfo(eventExtend.get("type").toString(), ConstantValue.BIG_TYPE_PCODE, eventTypeDict);
					
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictName")) {
						eventExtend.put("typeName", eventTypeMap.get("dictName"));
					}
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
						eventExtend.put("eventClass", eventTypeMap.get("dictFullPath"));
					}
				}
				
				if(CommonFunctions.isNotBlank(eventExtend, "eventStatus")) {// 事件状态
					eventStatus = eventExtend.get("eventStatus").toString();
					for(BaseDataDict statusDict : statusDictList) {
						if(eventStatus.equals(statusDict.getDictGeneralCode())) {
							eventExtend.put("eventStatusName", statusDict.getDictName());
							break;
						}
					}
				}
				if(CommonFunctions.isNotBlank(eventExtend, "eventSubStatus")) {
					eventSubStatus = eventExtend.get("eventSubStatus").toString();
					eventSubStatusName = null;
					
					for(BaseDataDict subStatusDict : subStatusDictList) {
						if(eventSubStatus.equals(subStatusDict.getDictGeneralCode())) {
							eventSubStatusName = subStatusDict.getDictName();
							break;
						}
					}
					
					
					if(CommonFunctions.isNotBlank(eventExtend, "eventStatusName")) {
						if(ConstantValue.REJECT_SUB_STATUS.equals(eventSubStatus)) {
							eventExtend.put("eventStatusName", eventSubStatusName);
						} else {
							eventExtend.put("eventStatusName", eventExtend.get("eventStatusName") + "-" + eventSubStatusName);
						}
					}
					
				}
				
				DataDictHelper.setDictValueForField(eventExtend, "patrolType", "patrolTypeName", patrolTypeList);
				DataDictHelper.setDictValueForField(eventExtend, "planType", "planTypeName", planTypeList);
				DataDictHelper.setDictValueForField(eventExtend, "planLevel", "planLevelName", planLevelList);
				
				if(CommonFunctions.isNotBlank(eventExtend, "pointSelection")) {
					Long pointSelection=Long.valueOf(eventExtend.get("pointSelection").toString());
					if(pointSelection!=null&&pointSelection>0) {
						PointInfo point = pointInfoService.searchById(pointSelection);
						if(null!=point) {
							eventExtend.put("pointSelectionName", point.getPointName());
						}
					}else if(pointSelection==0L) {
						eventExtend.put("pointSelectionName", "其他");
					}
				}
			}
		}
	}

	@Override
	public Map<String, Object> findEventExtendByEventId(Long eventId, Map<String, Object> map) {
		
		List<Map<String, Object>> eventExtendList=new ArrayList<Map<String, Object>>();
		
		Map<String, Object> EventExtend = this.findEventExtendByEventId(eventId);
		
		if(null!=EventExtend) {
			
			eventExtendList.add(EventExtend);
			
			if(CommonFunctions.isNotBlank(map, "isFormat")&&"1".equals(map.get("isFormat"))) {
				
				if(CommonFunctions.isNotBlank(map, "orgCode")) {
					this.formatEventExDataOut(eventExtendList, String.valueOf(map.get("orgCode")));
				}
				
			}
			
		}
		
		return EventExtend;
		
	}
}
