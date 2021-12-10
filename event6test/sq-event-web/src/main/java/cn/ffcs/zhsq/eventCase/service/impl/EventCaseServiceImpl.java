package cn.ffcs.zhsq.eventCase.service.impl;

import java.text.ParseException;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EvaResult;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.event.IEvaResultService;
import cn.ffcs.shequ.zzgl.service.holidayinfo.IHolidayInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.eventCase.service.IEventCaseService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
import cn.ffcs.zhsq.mybatis.persistence.eventCase.EventCaseMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.EventNoUtil;

/**
 * 案件 江西省罗坊镇
 * @author zhangls
 *
 */
@Service(value = "eventCaseService")
public class EventCaseServiceImpl extends EventCaseWorkflowServiceImpl implements IEventCaseService {
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IResMarkerService resMarkerService;
	
	@Autowired
	private IInvolvedPeopleService involvedPeopleService;
	
	@Autowired
	private IAttachmentService attachmentService;
	
	@Autowired
	private IArcgisInfoService arcgisInfoService;
	
	@Autowired
	private IEvaResultService evaResultService;
	
	@Autowired
	private IHolidayInfoService holidayInfoService;
	
	@Autowired
	private EventCaseMapper eventCaseMapper;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
  			 
	@Override
	public Long saveEventCase(Map<String, Object> eventCase, UserInfo userInfo) throws Exception {
		Long caseId = -1L;
		
		if(eventCase != null) {
			String eventCaseType = "";
			
			formatDataIn(eventCase, userInfo);
			
			if(CommonFunctions.isNotBlank(eventCase, "type")) {
				eventCaseType = eventCase.get("type").toString();
			}
			
			eventCase.put("code", EventNoUtil.getEventNO(null, ConstantValue.SEQ_EVENT_CASE_CODE, eventCaseType)); //--事件编码
			
			if(eventCaseMapper.insert(eventCase) > 0) {
				if(CommonFunctions.isNotBlank(eventCase, "caseId")) {
					caseId = Long.valueOf(eventCase.get("caseId").toString());
				}
				
				saveExtraInfo(caseId, eventCase);
				
				if(CommonFunctions.isNotBlank(eventCase, "isStart")) {
					boolean isStart = Boolean.valueOf(eventCase.get("isStart").toString());
					
					if(isStart) {
						this.startWorkflow4Case(caseId, userInfo, eventCase);
					}
				}
			}
		}
		
		return caseId;
	}

	@Override
	public boolean updateEventCase(Map<String, Object> eventCase, UserInfo userInfo) throws Exception {
		boolean result = false;
		
		if(eventCase != null && !eventCase.isEmpty()) {
			formatDataIn(eventCase, userInfo);
			
			result = eventCaseMapper.update(eventCase) > 0;
			
			if(result) {
				if(CommonFunctions.isNotBlank(eventCase, "caseId")) {
					Long caseId = Long.valueOf(eventCase.get("caseId").toString());
					
					saveExtraInfo(caseId, eventCase);
					
					if(CommonFunctions.isNotBlank(eventCase, "isStart")) {
						boolean isStart = Boolean.valueOf(eventCase.get("isStart").toString());
						
						if(isStart) {
							this.startWorkflow4Case(caseId, userInfo, eventCase);
						}
					}
				}
			}
		}
		
		return result;
	}

	@Override
	public boolean delEventCaseById(Long caseId, Long delUserId) {
		boolean result = false;
		
		if(caseId != null && caseId > 0) {
			result = eventCaseMapper.delete(caseId, delUserId) > 0;
		}
		return result;
	}

	@Override
	public Map<String, Object> findEventCaseByIdSimple(Long caseId) {
		Map<String, Object> eventCase = null;
		
		if(caseId != null && caseId > 0) {
			eventCase = eventCaseMapper.findById(caseId);
			
			formatDataOut4Detail(eventCase);
		}
		
		return eventCase;
	}

	@Override
	public Map<String, Object> findEventCaseById(Long caseId, Map<String, Object> params, String orgCode) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> eventCase = findEventCaseByIdSimple(caseId);
		
		formatDataOut4Detail(eventCase, orgCode);
		
		resultMap.put("eventCase", eventCase);
		resultMap.putAll(fetchExtraInfo(caseId, params));
		
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> findRemindListByCaseId(Long caseId) {
		List<Map<String, Object>> remindList = null;
		
		if(caseId != null && caseId > 0) {
			remindList = eventCaseMapper.findRemindListByCaseId(caseId);
			
			if(remindList != null) {
				Date remindDate = null;
				
				for(Map<String, Object> remind : remindList) {
					if(CommonFunctions.isNotBlank(remind, "remindDate")) {
						remindDate = (Date)remind.get("remindDate");
						remind.put("remindDateStr", DateUtils.formatDate(remindDate, DateUtils.PATTERN_24TIME));
					}
				}
			}
		}
		
		return remindList;
	}
	
	@Override
	public List<Map<String, Object>> findEvaListByCaseId(Long caseId, String orgCode) {
		List<Map<String, Object>> evaMapList = null;
		
		if(caseId != null && caseId > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("evaObj", ConstantValue.EVA_OBJ_CASE);
			params.put("objectId", caseId);
			params.put("orgCode", orgCode);
			
			List<EvaResult> evaResultList = evaResultService.findEvaResultList(params);
			
			if(evaResultList != null) {
				evaMapList = new ArrayList<Map<String, Object>>();
				Map<String, Object> evaMap = null;
				
				for(EvaResult eva : evaResultList) {
					evaMap = new HashMap<String, Object>();
					evaMap.put("creatorName", eva.getCreatorName());
					evaMap.put("createDateStr", eva.getCreateDateStr());
					evaMap.put("evaLevelName", eva.getEvaLevelName());
					evaMap.put("evaContent", eva.getEvaContent());
					
					evaMapList.add(evaMap);
				}
			}
		}
		
		return evaMapList;
	}

	@Override
	public int findEventCaseCount(Map<String, Object> params, UserInfo userInfo) {
		int listType = 1, total = 0;
		
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		capQueryParam(params, userInfo);
		
		switch(listType) {
			case 1: {//草稿列表
				total = findEventCaseCount4Draft(params);
				break;
			}
			case 2: {//待办列表
				total = findEventCaseCount4Todo(params);
				break;
			}
			case 3: {//经办列表
				total = findEventCaseCount4Handled(params);
				break;
			}
			case 4: {//我发起的列表
				total = findEventCaseCount4Initiator(params);
				break;
			}
			case 5: {//辖区所有列表
				total = findEventCaseCount4Jurisdiction(params);
				break;
			}
		}
		
		return total;
	}
	
	@Override
	public EUDGPagination findEventCasePagination(int pageNo, int pageSize,
			Map<String, Object> params, UserInfo userInfo) {
		int listType = 1;
		EUDGPagination eventCasePagination = null;
		
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		capQueryParam(params, userInfo);
		
		switch(listType) {
			case 1: {//草稿列表
				eventCasePagination = findEventCasePagination4Draft(pageNo, pageSize, params);
				break;
			}
			case 2: {//待办列表
				eventCasePagination = findEventCasePagination4Todo(pageNo, pageSize, params);
				break;
			}
			case 3: {//经办列表
				eventCasePagination = findEventCasePagination4Handled(pageNo, pageSize, params);
				break;
			}
			case 4: {//我发起的列表
				eventCasePagination = findEventCasePagination4Initiator(pageNo, pageSize, params);
				break;
			}
			case 5: {//辖区所有列表
				eventCasePagination = findEventCasePagination4Jurisdiction(pageNo, pageSize, params);
				break;
			}
		}
		
		if(eventCasePagination == null) {
			eventCasePagination = new EUDGPagination();
			eventCasePagination.setTotal(0);
			eventCasePagination.setRows(new ArrayList<Map<String, Object>>());//为了使页面能正常展示
		}
		
		return eventCasePagination;
	}
	
	/**
	 * 获取查询参数
	 * @param params
	 * 			listType	列表类型
	 * 				1 草稿
	 * 				2 待办
	 * 				3 经办
	 * 				4 我发起的
	 * 				5 辖区所有
	 * @param userInfo
	 * @return
	 */
	private Map<String, Object> capQueryParam(Map<String, Object> params, UserInfo userInfo) {
		int listType = 1;
		Long userId = -1L, userOrgId = -1L;
		String userOrgCode = null;
		List<String> statusList = new ArrayList<String>(),
					 statusOuterList = null;
		
		if(userInfo != null) {
			userOrgCode = userInfo.getOrgCode();
			userId = userInfo.getUserId();
			userOrgId = userInfo.getOrgId();
		}
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isBlank(params, "userOrgCode") && StringUtils.isNotBlank(userOrgCode)) {
			params.put("userOrgCode", userOrgCode);
		}
		if(CommonFunctions.isNotBlank(params, "urgencyDegree")) {
			params.put("urgencyDegreeArray", params.get("urgencyDegree").toString().split(","));
		}
		if(CommonFunctions.isNotBlank(params, "isAddExtendCol")) {
			params.put("isAddExtendCol", Boolean.valueOf(params.get("isAddExtendCol").toString()));
		}
		
		switch(listType) {
			case 1: {//草稿列表
				statusList.add(ConstantValue.EVENT_STATUS_DRAFT);
				break;
			}
			case 2: {//待办列表
				
			}
			case 4: {//我发起的列表
				statusList.add(ConstantValue.EVENT_STATUS_RECEIVED); 
				statusList.add(ConstantValue.EVENT_STATUS_REPORT); 
				statusList.add(ConstantValue.EVENT_STATUS_DISTRIBUTE); 
				statusList.add(ConstantValue.EVENT_STATUS_ARCHIVE);
				break;
			}
			case 3: {//经办列表
				
			}
			case 5: {//辖区所有列表
				statusList.add(ConstantValue.EVENT_STATUS_RECEIVED); 
				statusList.add(ConstantValue.EVENT_STATUS_REPORT); 
				statusList.add(ConstantValue.EVENT_STATUS_DISTRIBUTE); 
				statusList.add(ConstantValue.EVENT_STATUS_ARCHIVE);
				statusList.add(ConstantValue.EVENT_STATUS_END);
				break;
			}
		}
		
		if(CommonFunctions.isNotBlank(params, "status")) {
			String status = params.get("status").toString();
			
			statusOuterList = Arrays.asList(status.split(","));
		} else if(CommonFunctions.isNotBlank(params, "statusArray")) {
			Object statusArrayObj = params.get("statusArray");
			
			if(statusArrayObj instanceof String[]) {
				statusOuterList = Arrays.asList((String[])statusArrayObj);
			}
		} else if(CommonFunctions.isNotBlank(params, "statusList")) {
			Object statusListObj = params.get("statusList");
			
			if(statusListObj instanceof List) {
				statusOuterList = (List<String>)statusListObj;
			}
		}
		if(statusList.size() > 0) {
			
			if(statusOuterList != null) {
				statusList.retainAll(statusOuterList);
			}
			
			params.put("statusList", statusList);
		}
		
		switch(listType) {
			case 1: {//草稿列表
				if(CommonFunctions.isBlank(params, "creatorId") && userId > 0) {
					params.put("creatorId", userId);
				}
				
				break;
			}
			case 2: {//待办列表
				if(CommonFunctions.isBlank(params, "curUserId") && userId > 0) {
					params.put("curUserId", String.valueOf(userId));
				}
				if(CommonFunctions.isBlank(params, "curOrgId") && userOrgId > 0) {
					params.put("curOrgId", String.valueOf(userOrgId));
				}
				
				break;
			}
			case 3: {//经办列表
				if(CommonFunctions.isBlank(params, "handledUserId") && userId > 0) {
					params.put("handledUserId", String.valueOf(userId));
				}
				if(CommonFunctions.isBlank(params, "handledOrgId") && userOrgId > 0) {
					params.put("handledOrgId", String.valueOf(userOrgId));
				}
				
				break;
			}
			case 4: {//我发起的列表
				if(CommonFunctions.isBlank(params, "initiatorId") && userId > 0) {
					params.put("initiatorId", String.valueOf(userId));
				}
				if(CommonFunctions.isBlank(params, "initiatorOrgId") && userOrgId > 0) {
					params.put("initiatorOrgId", String.valueOf(userOrgId));
				}
				
				break;
			}
			case 5: {//辖区所有列表
				if(CommonFunctions.isBlank(params, "infoOrgCode") && CommonFunctions.isNotBlank(params, "startInfoOrgCode")) {
					params.put("infoOrgCode", params.get("startInfoOrgCode"));
				}
				
				break;
			}
		}
		
		return params;
	}
	
	/**
	 * 获取草稿列表总数
	 * @param params
	 * @return
	 */
	private int findEventCaseCount4Draft(Map<String, Object> params) {
		int count = eventCaseMapper.findCountByCriteria(params);
		
		return count;
	}
	
	/**
	 * 分页获取草稿列表信息
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	private EUDGPagination findEventCasePagination4Draft(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		String userOrgCode = null;
		List<Map<String, Object>> eventCaseList = new ArrayList<Map<String, Object>>();
		
		int count = findEventCaseCount4Draft(params);
		
		if(count > 0) {
			eventCaseList = eventCaseMapper.findPageList4Draft(params, rowBounds);
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			}
			
			formatDataOut4List(eventCaseList, userOrgCode);
		}
		
		EUDGPagination eventCasePagination = new EUDGPagination(count, eventCaseList);
		
		return eventCasePagination;
	}
	
	/**
	 * 获取待办列表总量
	 * @param params
	 * 			curUserId	当前办理人员id
	 * 			curOrgId	当前办理人员所属组织id
	 * @return
	 */
	private int findEventCaseCount4Todo(Map<String, Object> params) {
		int count = eventCaseMapper.findCount4Todo(params);
		
		return count;
	}
	
	/**
	 * 分页获取待办列表信息
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * 			curUserId	当前办理人员id
	 * 			curOrgId	当前办理人员所属组织id
	 * @return
	 */
	private EUDGPagination findEventCasePagination4Todo(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		String userOrgCode = null;
		List<Map<String, Object>> eventCaseList = new ArrayList<Map<String, Object>>();
		
		int count = findEventCaseCount4Todo(params);
		
		if(count > 0) {
			eventCaseList = eventCaseMapper.findPageList4Todo(params, rowBounds);
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			}
			
			formatDataOut4List(eventCaseList, userOrgCode);
		}
		
		EUDGPagination eventCasePagination = new EUDGPagination(count, eventCaseList);
		
		return eventCasePagination;
	}
	
	/**
	 * 获取经办列表总量
	 * @param params
	 * 			handledUserId	经办人员id
	 * 			handledOrgId	经办人员所属组织id
	 * @return
	 */
	private int findEventCaseCount4Handled(Map<String, Object> params) {
		int count = eventCaseMapper.findCount4Handled(params);
		
		return count;
	}
	
	/**
	 * 分页获取经办列表数据
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * 			handledUserId	经办人员id
	 * 			handledOrgId	经办人员所属组织id
	 * @return
	 */
	private EUDGPagination findEventCasePagination4Handled(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		String userOrgCode = null;
		List<Map<String, Object>> eventCaseList = new ArrayList<Map<String, Object>>();
		
		int count = findEventCaseCount4Handled(params);
		
		if(count > 0) {
			eventCaseList = eventCaseMapper.findPageList4Handled(params, rowBounds);
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			}
			
			formatDataOut4List(eventCaseList, userOrgCode);
		}
		
		EUDGPagination eventCasePagination = new EUDGPagination(count, eventCaseList);
		
		return eventCasePagination;
	}
	
	/**
	 * 获取我发起的列表总量
	 * @param params
	 * 			initiatorId		发起人员id
	 * 			initiatorOrgId	发起人员所属组织id
	 * @return
	 */
	private int findEventCaseCount4Initiator(Map<String, Object> params) {
		int count = eventCaseMapper.findCount4Initiator(params);
		
		return count;
	}
	
	/**
	 * 分页获取我发起的列表记录
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * 			initiatorId		发起人员id
	 * 			initiatorOrgId	发起人员所属组织id
	 * @return
	 */
	private EUDGPagination findEventCasePagination4Initiator(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		String userOrgCode = null;
		List<Map<String, Object>> eventCaseList = new ArrayList<Map<String, Object>>();
		
		int count = findEventCaseCount4Initiator(params);
		
		if(count > 0) {
			eventCaseList = eventCaseMapper.findPageList4Initiator(params, rowBounds);
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			}
			
			formatDataOut4List(eventCaseList, userOrgCode);
		}
		
		EUDGPagination eventCasePagination = new EUDGPagination(count, eventCaseList);
		
		return eventCasePagination;
	}
	
	/**
	 * 获取辖区所有列表总量
	 * @param params
	 * 			infoOrgCode	地域编码
	 * @return
	 */
	private int findEventCaseCount4Jurisdiction(Map<String, Object> params) {
		int count = eventCaseMapper.findCount4Jurisdiction(params);
		
		return count;
	}
	
	/**
	 * 分页获取辖区所有列表记录
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * 			infoOrgCode	地域编码
	 * @return
	 */
	private EUDGPagination findEventCasePagination4Jurisdiction(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		String userOrgCode = null;
		List<Map<String, Object>> eventCaseList = new ArrayList<Map<String, Object>>();
		
		int count = findEventCaseCount4Jurisdiction(params);
		
		if(count > 0) {
			eventCaseList = eventCaseMapper.findPageList4Jurisdiction(params, rowBounds);
			
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			}
			
			formatDataOut4List(eventCaseList, userOrgCode);
		}
		
		EUDGPagination eventCasePagination = new EUDGPagination(count, eventCaseList);
		
		return eventCasePagination;
	}
	
	/**
	 * 保存案件额外信息
	 * @param caseId	案件id
	 * @param params
	 * 			isAlterResMarker 		true，保存/更新定位信息；默认为false
	 * 				resMarker.mapType	地图类型
	 * 				resMarker.x			经度
	 * 				resMarker.y			纬度
	 * 			isAlterInvolvedPeople	true，保存/更新涉及人员信息；默认为false
	 * 				involvedPeople.eventInvolvedPeople	涉及人员信息，格式为：证件类型1，姓名1，身份证号码1，家庭住址1，联系电话1，备注1；证件类型2，姓名2，身份证号码2，家庭住址2，联系电话2，备注2；
	 * 			isAlterAttachment		true，更新附件业务信息；默认为false
	 * 				attachment.attachmentIds	附件id，以英文逗号分隔
	 * @return
	 */
	private Map<String, Object> saveExtraInfo(Long caseId, Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(caseId != null && caseId > 0) {
			if(CommonFunctions.isBlank(params, "caseId")) {
				params.put("caseId", caseId);
			}
			
			this.saveResmarker(params);
			this.saveInvolvedPeople(params);
			this.saveAttachment(params);
		}
		
		return resultMap;
	}
	
	/**
	 * 依据案件id获取案件额外信息
	 * @param caseId	案件id
	 * @param params
	 * 			isResMarkerNeeded		true，返回定位信息；默认为false
	 * 				resMarker.mapType	地图类型
	 * 			isInvolvedPeopleNeeded	true，返回涉及人员信息；默认为false
	 * 			isAttachmentNeeded		true，返回附件信息；默认为false
	 * 				attachment.eventSeq	附件类型，前、中、后
	 * @return
	 */
	private Map<String, Object> fetchExtraInfo(Long caseId, Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(caseId != null && caseId > 0) {
			Map<String, Object> eventCase = this.findEventCaseByIdSimple(caseId);
			
			if(eventCase != null) {
				if(CommonFunctions.isBlank(params, "caseId")) {
					params.put("caseId", caseId);
				}
				
				ResMarker resmarker = this.fetchResmarker(params);
				if(resmarker != null) {
					resultMap.put("resMarker", resmarker);
				}
				
				List<InvolvedPeople> involvedPeopleList = this.fetchInvolvedPeople(params);
				if(involvedPeopleList != null) {
					StringBuffer involvedPeopleBuffer = new StringBuffer("");
					
					for(InvolvedPeople involvedPeople : involvedPeopleList) {
						involvedPeopleBuffer.append(involvedPeople.getCardType()).append("，")
											.append(involvedPeople.getName()).append("，")
											.append(involvedPeople.getIdCard()).append("；");
					}
					
					resultMap.put("eventInvolvedPeople", involvedPeopleBuffer.toString());
					
					resultMap.put("involvedPeople", involvedPeopleList);
				}
				
				List<Attachment> attachmentList = this.fetchAttachment(params);
				if(attachmentList != null) {
					resultMap.put("attachment", attachmentList);
				}
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 保存案件定位信息，未给定位信息时，默认设置为网格中心点
	 * @param params
	 * 			isAlterResMarker	true保存/更新定位信息
	 * 			caseId				案件id
	 * 			gridId				案件所属网格id
	 * 			resMarker.mapType	地图类型
	 * 			resMarker.x			经度
	 * 			resMarker.y			纬度
	 */
	private void saveResmarker(Map<String, Object> params) {
		boolean isAlterResMarker = false;
		
		if(CommonFunctions.isNotBlank(params, "isAlterResMarker")) {
			isAlterResMarker = Boolean.valueOf(params.get("isAlterResMarker").toString());
		}
		
		if(isAlterResMarker) {
			Long caseId = -1L;
			
			if(CommonFunctions.isNotBlank(params, "caseId")) {
				caseId = Long.valueOf(params.get("caseId").toString());
			}
			
			if(caseId > 0) {
				if(CommonFunctions.isNotBlank(params, "resMarker.mapType") &&
				   CommonFunctions.isNotBlank(params, "resMarker.x") &&
				   CommonFunctions.isNotBlank(params, "resMarker.y")) {
					ResMarker resMarker = new ResMarker();
					
					resMarker.setMapType(params.get("resMarker.mapType").toString());
					resMarker.setX(params.get("resMarker.x").toString());
					resMarker.setY(params.get("resMarker.y").toString());
					resMarker.setResourcesId(caseId);
					resMarker.setCatalog("03");
					resMarker.setMarkerType(ConstantValue.EVENT_CASE_MODULE_CODE);
					
					resMarkerService.saveOrUpdateResMarker(resMarker);
				} else {
					Long gridId = -1L;
					
					if(CommonFunctions.isNotBlank(params, "gridId")) {
						gridId = Long.valueOf(params.get("gridId").toString());
					} else {
						Map<String, Object> eventCase = this.findEventCaseByIdSimple(caseId);
						
						if(CommonFunctions.isNotBlank(eventCase, "gridId")) {
							gridId = Long.valueOf(eventCase.get("gridId").toString());
						}
					}
					
					if(gridId > 0) {
						List<ArcgisInfoOfGrid> arcgisInfoList = arcgisInfoService.getArcgisDataOfGridForOuter(gridId, null);
						
						if(arcgisInfoList != null && arcgisInfoList.size() > 0){
							ResMarker resMarker = new ResMarker();
							
							resMarker.setResourcesId(caseId);
							resMarker.setCatalog("03");
							resMarker.setMarkerType(ConstantValue.EVENT_CASE_MODULE_CODE);
							
							for(ArcgisInfoOfGrid arcgis : arcgisInfoList){
								resMarker.setX(String.valueOf(arcgis.getX()));
								resMarker.setY(String.valueOf(arcgis.getY()));
								resMarker.setMapType(String.valueOf(arcgis.getMapt()));
								
								resMarkerService.saveOrUpdateResMarker(resMarker);	
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 获取定位信息
	 * @param params
	 * 			isResMarkerNeeded	true获取定位信息
	 * 			caseId				案件id
	 * 			resMarker.mapType	地图类型
	 * @return
	 * 		未找到定位信息时，返回null
	 */
	private ResMarker fetchResmarker(Map<String, Object> params) {
		boolean isResMarkerNeeded = false;
		ResMarker resMarker = null;
		
		if(CommonFunctions.isNotBlank(params, "isResMarkerNeeded")) {
			isResMarkerNeeded = Boolean.valueOf(params.get("isResMarkerNeeded").toString());
		}
		
		if(isResMarkerNeeded) {
			Long caseId = -1L;
			String mapType = null; 
			
			if(CommonFunctions.isNotBlank(params, "caseId")) {
				caseId = Long.valueOf(params.get("caseId").toString());
			}
			
			if(CommonFunctions.isNotBlank(params, "resMarker.mapType")) {
				mapType = params.get("resMarker.mapType").toString();
			}
			
			if(caseId > 0) {
				List<ResMarker> resMarkers = resMarkerService.findResMarkerByParam(ConstantValue.EVENT_CASE_MODULE_CODE, caseId, mapType);
				if(resMarkers != null && resMarkers.size() > 0){
					resMarker = resMarkers.get(0);
				}
			}
		}
		
		return resMarker;
	}
	
	/**
	 * 保存案件涉及人员信息，不支持累加新增
	 * @param params
	 * 			isAlterInvolvedPeople	true，保存涉及人员信息
	 * 			caseId					案件id
	 * 			involvedPeople.eventInvolvedPeople	涉及人员信息，格式为：证件类型1，姓名1，身份证号码1，家庭住址1，联系电话1，备注1；证件类型2，姓名2，身份证号码2，家庭住址2，联系电话2，备注2；
	 */
	private void saveInvolvedPeople(Map<String, Object> params) {
		boolean isAlterInvolvedPeople = false;
		
		if(CommonFunctions.isNotBlank(params, "isAlterInvolvedPeople")) {
			isAlterInvolvedPeople = Boolean.valueOf(params.get("isAlterInvolvedPeople").toString());
		}
		
		if(isAlterInvolvedPeople) {
			Long caseId = -1L;
			
			if(CommonFunctions.isNotBlank(params, "caseId")) {
				caseId = Long.valueOf(params.get("caseId").toString());
			}
			
			if(caseId > 0) {
				involvedPeopleService.deleteInvolvedPeopleByBiz(caseId, InvolvedPeople.BIZ_TYPE.EVENT_CASE.getBizType());//先删除数据
				
				if(CommonFunctions.isNotBlank(params, "involvedPeople.eventInvolvedPeople")) {
					String eventInvolvedPeople = params.get("involvedPeople.eventInvolvedPeople").toString();
					
					String[] ipArray = eventInvolvedPeople.split("；"),
							 item = null;
					String ip = null;
					InvolvedPeople involvedPeople = null;
					
					try {
						for(int index = 0, len = ipArray.length; index < len; index++) {
							ip = ipArray[index];
							item = ip.split("，");
							involvedPeople = new InvolvedPeople();
							
							involvedPeople.setBizId(caseId);
							involvedPeople.setBizType(InvolvedPeople.BIZ_TYPE.EVENT_CASE.getBizType());//表示事件，暂时未用
							involvedPeople.setCardType(item[0]);
							involvedPeople.setName(item[1]);
							involvedPeople.setIdCard(item[2]);
							
							if(item.length > 3){
								involvedPeople.setHomeAddr(item[3]);
								involvedPeople.setTel(item[4]);
								involvedPeople.setRemark(item[5]);
							}
							involvedPeopleService.insertInvolvedPeople(involvedPeople);
						}
					} catch (Exception e) {
						logger.error("涉及人员更新失败!");
					}
				}
			}
		}
	}
	
	/**
	 * 获取涉及人员定位信息
	 * @param params
	 * 			isInvolvedPeopleNeeded	true，获取涉及人员信息
	 * 			caseId					案件id
	 * @return
	 */
	private List<InvolvedPeople> fetchInvolvedPeople(Map<String, Object> params) {
		List<InvolvedPeople> involvedPeopleList = null;
		boolean isInvolvedPeopleNeeded = false;
		
		if(CommonFunctions.isNotBlank(params, "isInvolvedPeopleNeeded")) {
			isInvolvedPeopleNeeded = Boolean.valueOf(params.get("isInvolvedPeopleNeeded").toString());
		}
		
		if(isInvolvedPeopleNeeded) {
			Long caseId = -1L;
			
			if(CommonFunctions.isNotBlank(params, "caseId")) {
				caseId = Long.valueOf(params.get("caseId").toString());
			}
			
			if(caseId > 0) {
				InvolvedPeople involvedPeople = new InvolvedPeople();
				involvedPeople.setBizId(caseId);
				involvedPeople.setBizType(InvolvedPeople.BIZ_TYPE.EVENT_CASE.getBizType());
				
				involvedPeopleList = involvedPeopleService.findInvolvedPeopleList(involvedPeople);
			}
		}
		
		return involvedPeopleList;
	}
	
	/**
	 * 保存附件信息
	 * @param params
	 * 			isAlterAttachment			true，保存附件信息
	 * 			caseId						案件id
	 * 			attachment.attachmentIds	附件id，以英文逗号分隔
	 * @return
	 */
	private boolean saveAttachment(Map<String, Object> params) {
		boolean result = false,
				isAlterAttachment = false;
		
		if(CommonFunctions.isNotBlank(params, "isAlterAttachment")) {
			isAlterAttachment = Boolean.valueOf(params.get("isAlterAttachment").toString());
		}
		
		if(isAlterAttachment) {
			Long caseId = -1L;
			
			if(CommonFunctions.isNotBlank(params, "caseId")) {
				caseId = Long.valueOf(params.get("caseId").toString());
			}
			
			if(caseId != null && caseId > 0) {
				if(CommonFunctions.isNotBlank(params, "attachment.attachmentIds")) {
					String attachmentIds = params.get("attachment.attachmentIds").toString();
					
					result = attachmentService.updateBizId(caseId, ConstantValue.EVENT_CASE_ATTACHMENT_TYPE, attachmentIds);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 获取附件信息
	 * @param params
	 * 			isAttachmentNeeded	true，获取附件信息
	 * 			caseId				案件id
	 * @return
	 */
	private List<Attachment> fetchAttachment(Map<String, Object> params) {
		boolean isAttachmentNeeded = false;
		List<Attachment> attachmentList = null;
		
		if(CommonFunctions.isNotBlank(params, "isAttachmentNeeded")) {
			isAttachmentNeeded = Boolean.valueOf(params.get("isAttachmentNeeded").toString());
		}
		
		if(isAttachmentNeeded) {
			Long caseId = -1L;
			
			if(CommonFunctions.isNotBlank(params, "caseId")) {
				caseId = Long.valueOf(params.get("caseId").toString());
			}
			
			if(caseId > 0) {
				if(CommonFunctions.isNotBlank(params, "attachment.eventSeq")) {
					attachmentList = attachmentService.findByBizId(caseId, ConstantValue.EVENT_CASE_ATTACHMENT_TYPE, params.get("attachment.eventSeq").toString());
				} else {
					attachmentList = attachmentService.findByBizId(caseId, ConstantValue.EVENT_CASE_ATTACHMENT_TYPE);
				}
			}
		}
		
		return attachmentList;
	}
	
	/**
	 * 格式化输入数据
	 * @param eventCase	案件信息
	 * 			handleDateInterval	处理时限，单位为工作日
	 * @param userInfo	操作用户
	 */
	private void formatDataIn(Map<String, Object> eventCase, UserInfo userInfo) {
		if(eventCase != null && !eventCase.isEmpty()) {
			Long userId = -1L;
			
			if(userInfo != null) {
				userId = userInfo.getUserId();
			}
			
			if(CommonFunctions.isNotBlank(eventCase, "happenTimeStr") && CommonFunctions.isBlank(eventCase, "happenTime")) {
				try {
					eventCase.put("happenTime", DateUtils.convertStringToDate(eventCase.get("happenTimeStr").toString(), DateUtils.PATTERN_24TIME));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			if(CommonFunctions.isNotBlank(eventCase, "endTimeStr") && CommonFunctions.isBlank(eventCase, "endTime")) {
				try {
					eventCase.put("endTime", DateUtils.convertStringToDate(eventCase.get("endTimeStr").toString(), DateUtils.PATTERN_24TIME));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			if(CommonFunctions.isNotBlank(eventCase, "caseId")) {
				if(CommonFunctions.isBlank(eventCase, "updaterId")) {
					eventCase.put("updaterId", userId);
				}
			} else {
				if(CommonFunctions.isBlank(eventCase, "status")) {
					eventCase.put("status", ConstantValue.EVENT_STATUS_DRAFT);
				}
				if(CommonFunctions.isBlank(eventCase, "creatorId")) {
					eventCase.put("creatorId", userId);
				}
			}
			
			if(CommonFunctions.isNotBlank(eventCase, "handleDateInterval")) {
				int handleDateInterval = 0;
				
				try {
					handleDateInterval = Integer.valueOf(eventCase.get("handleDateInterval").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if(handleDateInterval > 0) {
					eventCase.put("handleDate", holidayInfoService.getWorkDateByAfterWorkDay(new Date(), handleDateInterval));
				}
			}
		}
	}
	
	/**
	 * 格式化输出详情数据
	 * @param eventCase
	 */
	@Cacheable(value = "baseCache")
	private void formatDataOut4Detail(Map<String, Object> eventCase) {
		if(eventCase != null) {
			List<Map<String, Object>> eventCaseMapList = new ArrayList<Map<String, Object>>();
			eventCaseMapList.add(eventCase);
			
			formatDataOut4Detail(eventCaseMapList);
		}
	}
	
	/**
	 * 格式化输出列表非字典项数据
	 * @param eventCaseMapList
	 */
	@Cacheable(value = "baseCache")
	private void formatDataOut4List(List<Map<String, Object>> eventCaseMapList) {
		if(eventCaseMapList != null) {
			Date createTime = null,
				 happenTime = null;
			
			for(Map<String, Object> eventCase : eventCaseMapList) {
				if(eventCase != null) {
					if(CommonFunctions.isNotBlank(eventCase, "createTime")) {
						createTime = (Date)eventCase.get("createTime");
					}
					if(CommonFunctions.isNotBlank(eventCase, "happenTime")) {
						happenTime = (Date)eventCase.get("happenTime");
					}
					
					if(createTime != null) {
						eventCase.put("createTimeStr", DateUtils.formatDate(createTime, DateUtils.PATTERN_24TIME));
					}
					if(happenTime != null) {
						eventCase.put("happenTimeStr", DateUtils.formatDate(happenTime, DateUtils.PATTERN_24TIME));
					}
				}
			}
		}
	}
	
	/**
	 * 格式化输出详情非字典项数据
	 * @param eventCaseMapList
	 */
	@Cacheable(value = "baseCache")
	private void formatDataOut4Detail(List<Map<String, Object>> eventCaseMapList) {
		if(eventCaseMapList != null) {
			Date handleDate = null;
			
			formatDataOut4List(eventCaseMapList);
			
			for(Map<String, Object> eventCase : eventCaseMapList) {
				if(eventCase != null) {
					if(CommonFunctions.isNotBlank(eventCase, "handleDate")) {
						handleDate = (Date)eventCase.get("handleDate");
					}
					
					if(handleDate != null) {
						eventCase.put("handleDateStr", DateUtils.formatDate(handleDate, DateUtils.PATTERN_24TIME));
					}
				}
			}
		}
	}
	
	/**
	 * 格式化输出详情数据
	 * @param eventCase
	 * @param orgCode	组织编码
	 */
	@Cacheable(value = "baseCache")
	private void formatDataOut4Detail(Map<String, Object> eventCase, String orgCode) {
		if(eventCase != null) {
			List<Map<String, Object>> eventCaseList = new ArrayList<Map<String, Object>>();
			eventCaseList.add(eventCase);
			
			formatDataOut4Detail(eventCaseList, orgCode);
		}
	}
	
	/**
	 * 列表数据格式化输出
	 * @param eventCaseMapList
	 * @param orgCode	组织编码
	 */
	@Cacheable(value = "baseCache")
	private void formatDataOut4List(List<Map<String, Object>> eventCaseMapList, String orgCode) {
		if(eventCaseMapList != null && eventCaseMapList.size() > 0) {
			Map<String, Object> dictMap = new HashMap<String, Object>();
			dictMap.put("orgCode", orgCode);
			dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
			
			List<BaseDataDict> urgencyDegreeDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.URGENCY_DEGREE_PCODE, orgCode),
							   statusDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.STATUS_PCODE, orgCode),
							   eventTypeDict = baseDictionaryService.findDataDictListByCodes(dictMap);
			
			formatDataOut4List(eventCaseMapList);
			
			for(Map<String, Object> eventCase : eventCaseMapList) {
				// 设置事件分类全路径
				if(CommonFunctions.isNotBlank(eventCase, "type") && eventTypeDict != null) {
					Map<String, Object> eventTypeMap = DataDictHelper.capMultilevelDictInfo(eventCase.get("type").toString(), ConstantValue.BIG_TYPE_PCODE, eventTypeDict);
					
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictName")) {
						eventCase.put("typeName", eventTypeMap.get("dictName"));
					}
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
						eventCase.put("eventClass", eventTypeMap.get("dictFullPath"));
					}
				}
				
				// 紧急程度
				DataDictHelper.setDictValueForField(eventCase, "urgencyDegree", "urgencyDegreeName", urgencyDegreeDict);
				
				// 案件状态
				DataDictHelper.setDictValueForField(eventCase, "status", "statusName", statusDict);
			}
		}
	}
	
	/**
	 * 格式化输出详情数据
	 * @param eventCaseMapList
	 * @param orgCode			组织编码
	 */
	@Cacheable(value = "baseCache")
	private void formatDataOut4Detail(List<Map<String, Object>> eventCaseMapList, String orgCode) {
		if(eventCaseMapList != null && eventCaseMapList.size() > 0) {
			List<BaseDataDict> influenceDegreeDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.INFLUENCE_DEGREE_PCODE, orgCode),
							   sourceDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SOURCE_PCODE, orgCode),
							   involvedNumDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.INVOLVED_NUM_PCODE, orgCode),
							   collectWayDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.COLLECT_WAY_PCODE, orgCode);
			
			formatDataOut4List(eventCaseMapList, orgCode);
			formatDataOut4Detail(eventCaseMapList);
			
			for(Map<String, Object> eventCase : eventCaseMapList) {
				// 影响范围
				DataDictHelper.setDictValueForField(eventCase, "influenceDegree", "influenceDegreeName", influenceDegreeDict);
				
				// 信息来源
				DataDictHelper.setDictValueForField(eventCase, "source", "sourceName", sourceDict);

				// 涉及人数
				DataDictHelper.setDictValueForField(eventCase, "involvedNum", "involvedNumName", involvedNumDict);

				// 采集渠道
				DataDictHelper.setDictValueForField(eventCase, "collectWay", "collectWayName", collectWayDict);
			}
		}
	}
}
