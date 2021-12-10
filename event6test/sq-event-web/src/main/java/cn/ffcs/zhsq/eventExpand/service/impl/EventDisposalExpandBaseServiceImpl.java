package cn.ffcs.zhsq.eventExpand.service.impl;

import cn.ffcs.log.api.bo.DataLog;
import cn.ffcs.log.api.bo.Result;
import cn.ffcs.log.api.service.LogService;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EvaResult;
import cn.ffcs.shequ.zzgl.service.event.IEvaResultService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExpand.service.IEventDisposal4ExpandService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.planStaffing.PlanMember;
import cn.ffcs.zhsq.planStaffing.IPlanMemberService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service(value = "eventDisposalExpandBaseServiceImpl")
public class EventDisposalExpandBaseServiceImpl implements IEventDisposal4ExpandService {
	
	//事件处理基础服务类
	@Autowired
	private IEventDisposalService eventDisposalService;

	//事件评价
	@Autowired
	private IEvaResultService evaResultService;

	//事件扩展信息
	@Autowired
	private IEventExtendService eventExtendService;

	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IPlanMemberService planMemberService;
	
	//功能配置
	@Autowired
	private IFunConfigurationService configurationService;

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoService;
	
	@Autowired
	private LogService logService;
    
    @Autowired
	private IBaseDictionaryService baseDictionaryService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@Override
	public boolean updateEventDisposal(EventDisposal event, Map<String, Object> params,UserInfo userInfo) {
		boolean result = false;
		
		if(params == null) {
			params = new HashMap<String, Object>();
		}
		
		if(CommonFunctions.isBlank(params, "userInfo")) {
			params.put("userInfo", userInfo);
		}
		
		result = eventDisposalService.updateEventDisposal(event,params);

		return result;
	}

	@Override
	public boolean saveOrUpdateEventEvaluate(UserInfo userInfo,Long eventId,String evaLevel,String evaContent,Map<String, Object> params) throws Exception {

		Long erId = -1L;
		Long  eventExtendId = -1L;
		StringBuffer msgWrong = new StringBuffer("");

		if(null == eventId || eventId < 0){
			throw new IllegalArgumentException("参数【eventId】为空，请检查！");
		}

		if(StringUtils.isBlank(evaLevel)){
			throw new IllegalArgumentException("参数【evaLevel】为空，请检查！");
		}

		if(StringUtils.isBlank(evaContent)){
			throw new IllegalArgumentException("参数【evaContent】为空，请检查！");
		}

		if(null == userInfo){
			throw new IllegalArgumentException("参数【userInfo】为空，请检查！");
		}

		//流程结束后，生成评价信息
		if(StringUtils.isNotBlank(evaLevel) && StringUtils.isNotBlank(evaContent)){
			EvaResult evaResult = new EvaResult();
			evaResult.setObjectId(eventId);
			if(CommonFunctions.isNotBlank(params,"evaObj")){
				evaResult.setEvaObj(String.valueOf(params.get("evaObj")));
			}else {
				evaResult.setEvaObj(ConstantValue.EVA_OBJ_NEW_EVENT);
			}
			if(CommonFunctions.isNotBlank(params,"createDate")){
				try {
					evaResult.setCreateDate((Date)(params.get("createDate")));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (CommonFunctions.isNotBlank(params,"createorName")) {
            	evaResult.setCreatorName(params.get("createorName").toString());//组织名称-人员名称
			}else {
				evaResult.setCreatorName(userInfo.getOrgName()+"-"+userInfo.getPartyName());//组织名称-人员名称
			}
			evaResult.setEvaLevel(evaLevel);
			evaResult.setEvaContent(evaContent);
			evaResult.setCreateUser(userInfo.getUserId());
			evaResult.setUpdateUser(userInfo.getUserId());
			evaResult.setUpdaterName(userInfo.getPartyName());

			erId = evaResultService.saveEvaResult(evaResult);
		}
		//更新扩展信息表中事件的扩展信息，新增扩展信息记录或更新扩展记录的er_id
		if(erId > 0){
			Map<String,Object> eventExtend = new HashMap<>();

			eventExtend.put("eventId",eventId);
			eventExtend.put("erId",erId);
			eventExtendId = eventExtendService.saveEventExtend(eventExtend);
			if(eventExtendId < 0){
				msgWrong.append("事件评价时【").append(eventId).append("】保存扩展信息失败,请检查！");
			}
		}else {
			msgWrong.append("事件【").append(eventId).append("】保存评价信息失败,请检查！");
		}

		if(StringUtils.isNotBlank(msgWrong.toString())){
			throw new ZhsqEventException(msgWrong.toString());
		}

		return true;
	}

	@Override
	public List<Map<String, Object>> findEvaResultList(Long eventId,String evaObj,UserInfo userInfo, Map<String, Object> params) throws Exception {

		String IS_HIDE_EVARESULT = "IS_HIDE_EVARESULT";
		Boolean isHiddingEvaResult = false;

		List<EvaResult> evaResultList = new ArrayList<>();
		List<Map<String,Object>> resultList = new ArrayList<>();

		if(null != eventId && eventId > 0){
			params.put("objectId", eventId);
		}else {
			throw new IllegalArgumentException("参数【eventId】为空，请检查！");
		}
		
		if(StringUtils.isBlank(evaObj) && CommonFunctions.isNotBlank(params, "evaObj")) {
			evaObj = params.get("evaObj").toString();
		}

		if(StringUtils.isNotBlank(evaObj)){
			params.put("evaObj", evaObj);
		} else {
			throw new IllegalArgumentException("参数【evaObj】为空，请检查！");
		}

		if(null != userInfo && null != userInfo.getOrgCode()){
			params.put("orgCode", userInfo.getOrgCode());
		}else {
			throw new IllegalArgumentException("参数【orgCode】为空，请检查！");
		}

		//获取评价列表
		evaResultList = evaResultService.findEvaResultList(params);

		if(null != evaResultList && evaResultList.size() > 0){
			//获取事件初始额外信息
			isHiddingEvaResult = Boolean.valueOf(configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, IS_HIDE_EVARESULT, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(),IFunConfigurationService.CFG_ORG_TYPE_0));

			for(EvaResult evaResult:evaResultList){
				Map<String,Object> evaMap = new HashMap<>();

				//事件隐藏评价人信息
				if(isHiddingEvaResult){
					Long orgId = -1L;
					Long userId = -1L;
					Long collectorOrgId = -1L;
					Long collectorId = -1L;
					//当前环节办理人id
					Long curNodeTransactorId = -1L;
					//当前环节办理人组织id
					Long curNodeTransactorOrgId = -1L;
					EventDisposal event = null;
					Boolean isCollector = false;
					ProInstance proInstance = null;
					OrgSocialInfoBO orgInfo = null;
					String  patrolType = "",//巡防类型
							orgLevel = "",//组织层级
					CROSSING_PATROL_TYPE = "1";


					orgId = userInfo.getOrgId();
					userId = userInfo.getUserId();
					orgInfo = orgSocialInfoService.findByOrgId(orgId);

					if(eventId > 0){
						proInstance = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
					}

					if(null != proInstance){
						//判断当前用户是否是事件采集人员，是的话不对采集环节做处理
						collectorOrgId = proInstance.getOrgId();
						collectorId = proInstance.getUserId();
						if(userId.equals(collectorId) && orgId.equals(collectorOrgId)){
							isCollector = true;
						}
					}

					if(null != orgInfo){
						orgLevel = orgInfo.getChiefLevel();
					}

					Map<String,Object> eventMap = eventExtendService.findEventExtendByEventId(eventId);
					if(CommonFunctions.isNotBlank(eventMap,"patrolType")){
						patrolType = String.valueOf(eventMap.get("patrolType"));
					}

					//交叉巡防事件,对非采集人、非市级账号隐藏评价人员信息
					if(!isCollector && CROSSING_PATROL_TYPE.equals(patrolType) && ConstantValue.MUNICIPAL_ORG_LEVEL.compareTo(orgLevel) < 0){
						evaMap.put("creatorName","******");
					}else {
						evaMap.put("creatorName",evaResult.getCreatorName());
					}
				}else {
					evaMap.put("creatorName",evaResult.getCreatorName());
				}

				evaMap.put("createDateStr",evaResult.getCreateDateStr());
				evaMap.put("evaObj",evaResult.getEvaObj());
				evaMap.put("evaLevel",evaResult.getEvaLevel());
				evaMap.put("evaLevelName",evaResult.getEvaLevelName());
				evaMap.put("evaContent",evaResult.getEvaContent());

				resultList.add(evaMap);

			}
		}


		return resultList;
	}
	
	@Override
    public Map<String, Object> delEventById(List<Long> eventIdList, UserInfo userInfo, Map<String, Object> params) {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	int successTotal = 0;
    	
    	if(eventIdList == null || eventIdList.size() == 0) {
    		throw new IllegalArgumentException("缺少需要删除的事件信息！");
    	}
    	
    	if(userInfo == null) {
    		throw new IllegalArgumentException("缺少删除操作用户信息！");
    	}
    	
    	successTotal = eventDisposalService.deleteEventDisposalByIds(eventIdList, userInfo.getUserId());
    	
    	resultMap.put("total", eventIdList.size());
		resultMap.put("successTotal", successTotal);
		
    	//保存事件删除操作日志
		//if (successTotal > 0) {//为了支持批量删除，不逐条验证删除成功与否，默认删除均会成功
			//for (Long eventId : eventIdList) {
				//saveLog(eventId, "D", userInfo, null, null);
			//}
		//}
    			
    	return resultMap;
    }
    
	@Override
	public List<Map<String, Object>> capPlanConfigStaff(String planType, String planLevel, UserInfo userInfo, Map<String, Object> params) throws Exception {
		if(StringUtils.isBlank(planType)) {
			throw new IllegalArgumentException("缺少预案类型！");
		}
		if(StringUtils.isBlank(planLevel)) {
			throw new IllegalArgumentException("缺少预案等级！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		
		String regionCode = null;
		
		if(CommonFunctions.isNotBlank(params, "regionCode")) {
			regionCode = params.get("regionCode").toString();
		} else {
			Long userOrgId = userInfo.getOrgId();
			
			if(userOrgId != null && userOrgId > 0) {
				OrgEntityInfoBO regionInfo = orgEntityInfoService.findBySocialOrgId(userOrgId);
				
				if(regionInfo != null) {
					regionCode = regionInfo.getOrgCode();
				}
			}
		}
		
		if(StringUtils.isBlank(regionCode)) {
			throw new IllegalArgumentException("缺少有效地域编码信息！");
		}
		
		Map<String, Object> planParam = new HashMap<String, Object>();
		List<PlanMember> planMemberList = null;
		List<Map<String, Object>> planConfigStaffList = null;
		
		planParam.put("planType", planType);
		planParam.put("planLevel", planLevel);
		planParam.put("regionCodeAll", regionCode);
		
		planMemberList = planMemberService.findPlanMemberListByParams(planParam);
		
		if(planMemberList != null) {
			List<PlanMember> configStaffList = null;
			List<Map<String, Object>> configStaffMapList = null;
			Map<String, List<PlanMember>> planConfigStaffMap = new HashMap<String, List<PlanMember>>();
			Map<String, Object> userTypeMap = null, configStaffMap = null;
			String userType = null, planRole = null, planConfigId = null, staffIdKey = null;
			Long staffId = null, staffOrgId = null;
			Set<String> configUserSet = new HashSet<String>();
			StringBuffer configUserId = null,
						 configUserName = null,
						 configOrgId = null,
						 configOrgName = null;
			
			planConfigStaffList = new ArrayList<Map<String, Object>>();
			
			for(PlanMember planMember : planMemberList) {
				planRole =  planMember.getPlanRole();
				planConfigId = planMember.getPlanConfigId();
				userType = planRole + "-" + planConfigId;
				
				if(planConfigStaffMap.containsKey(userType)) {
					configStaffList = planConfigStaffMap.get(userType);
				} else {
					configStaffList = new ArrayList<PlanMember>();
				}
				
				configStaffList.add(planMember);
				
				planConfigStaffMap.put(userType, configStaffList);
			}
			
			for(String userTypeKey : planConfigStaffMap.keySet()) {
				userTypeMap = new HashMap<String, Object>();
				configStaffList = planConfigStaffMap.get(userTypeKey);
				configStaffMapList = new ArrayList<Map<String, Object>>();
				configUserId = new StringBuffer("");
				configUserName = new StringBuffer("");
				configOrgId = new StringBuffer("");
				configOrgName = new StringBuffer("");
				
				userTypeMap.put("userType", userTypeKey.split("-")[0]);
				userTypeMap.put("planConfigId", userTypeKey.split("-")[1]);
				
				for(PlanMember configStaff : configStaffList) {
					configStaffMap = new HashMap<String, Object>();
					
					staffId = configStaff.getUserId();
					staffOrgId = configStaff.getOrgId();
					staffIdKey = staffId + "-" + staffOrgId;
					
					if(!configUserSet.contains(staffIdKey) 
							&& staffId != null && staffId > 0 
							&& staffOrgId != null && staffOrgId > 0) {
						configStaffMap.put("userId", staffId);
						configStaffMap.put("userName", configStaff.getUserName());
						configStaffMap.put("orgId", staffOrgId);
						configStaffMap.put("orgName", configStaff.getOrgName());
						
						configUserId.append(",").append(staffId);
						configUserName.append(",").append(configStaff.getUserName());
						configOrgId.append(",").append(staffOrgId);
						configOrgName.append(",").append(configStaff.getOrgName());
						
						configStaffMapList.add(configStaffMap);
					}
				}
				
				userTypeMap.put("configStaff", configStaffMapList);
				if(configUserId.length() > 0) {
					userTypeMap.put("configUserIds", configUserId.substring(1));
				}
				if(configUserName.length() > 0) {
					userTypeMap.put("configUserNames", configUserName.substring(1));
				}
				if(configOrgId.length() > 0) {
					userTypeMap.put("configOrgIds", configOrgId.substring(1));
				}
				if(configOrgName.length() > 0) {
					userTypeMap.put("configOrgNames", configOrgName.substring(1));
				}
				
				planConfigStaffList.add(userTypeMap);
			}
		}
		
		return planConfigStaffList;
	}
	
    /**
	 * 事件属性字典转换，为了支持非页面调用的情况
	 * @param event
	 * @param userOrgCode
	 */
	private void formatEventInfo(EventDisposal event, String userOrgCode) {
		if(event != null) {
			String type = event.getType(),
				   typeName = event.getTypeName(),
				   eventClass = event.getEventClass(),
				   involvedNum = event.getInvolvedNum(),
				   involvedNumName = event.getInvolvedNumName(),
				   influenceDegree = event.getInfluenceDegree(),
				   influenceDegreeName = event.getInfluenceDegreeName(),
				   urgencyDegree = event.getUrgencyDegree(),
				   urgencyDegreeName = event.getUrgencyDegreeName(),
				   source = event.getSource(),
				   sourceName = event.getSourceName();
			
			if((StringUtils.isBlank(typeName) || StringUtils.isBlank(eventClass)) && StringUtils.isNotBlank(type)) {
				Map<String, Object> dictMap = new HashMap<String, Object>(), eventTypeMap = null;
				List<BaseDataDict> eventTypeDict = null;
				
				dictMap.put("orgCode", userOrgCode);
				dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
				
				eventTypeDict = baseDictionaryService.findDataDictListByCodes(dictMap);
				
				eventTypeMap = DataDictHelper.capMultilevelDictInfo(type, ConstantValue.BIG_TYPE_PCODE, eventTypeDict);
				
				if(CommonFunctions.isNotBlank(eventTypeMap, "dictName")) {
					event.setTypeName(eventTypeMap.get("dictName").toString());
				}
				if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
					event.setEventClass(eventTypeMap.get("dictFullPath").toString());
				}
			}
			
			if(StringUtils.isBlank(involvedNumName) && StringUtils.isNotBlank(involvedNum)) {
				DataDictHelper.setDictValueForField(event, "involvedNum", "involvedNumName", ConstantValue.INVOLVED_NUM_PCODE, userOrgCode);
			}
			
			if(StringUtils.isBlank(influenceDegreeName) && StringUtils.isNotBlank(influenceDegree)) {
				DataDictHelper.setDictValueForField(event, "influenceDegree", "influenceDegreeName", ConstantValue.INFLUENCE_DEGREE_PCODE, userOrgCode);
			}
			
			if(StringUtils.isBlank(urgencyDegreeName) && StringUtils.isNotBlank(urgencyDegree)) {
				DataDictHelper.setDictValueForField(event, "urgencyDegree", "urgencyDegreeName", ConstantValue.URGENCY_DEGREE_PCODE, userOrgCode);
			}
			
			if(StringUtils.isBlank(sourceName) && StringUtils.isNotBlank(source)) {
				DataDictHelper.setDictValueForField(event, "source", "sourceName", ConstantValue.SOURCE_PCODE, userOrgCode);
			}
		}
	}
	
    /**
	 * 记录事件操作日志-增删改
	 * @param userInfo		操作用户信息
	 * @param action		操作类型： 新增:I; 修改:U; 查询:Q, 删除:D, 导出:EXP, 导入:IMP;查看：V
	 * @param eventId		事件id
	 * @param eventAfter	事件修改之后值
	 * @return
	 */
	private boolean saveLog(Long eventId,  String action, UserInfo userInfo, EventDisposal eventBefore, EventDisposal eventAfter) {
		String valueBefore = "", valueAfter = "", 
			   userOrgCode = userInfo.getOrgCode();
		
		if(eventBefore != null) {
			valueBefore = JSONObject.fromObject(eventBefore).toString();
		}
		if(eventAfter != null) {
			formatEventInfo(eventAfter, userOrgCode);
			
			valueAfter = JSONObject.fromObject(eventAfter).toString();
		}
		
		return saveLog(userInfo, action, eventId, valueBefore, valueAfter);
	}
	
	/**
	 * 记录事件操作日志-增删改
	 * @param action	操作类型： 新增:I; 修改:U; 查询:Q, 删除:D, 导出:EXP, 导入:IMP;查看：V
	 * @param recordId 事件id
	 * @param valueBefore 事件修改之前值
	 * @param valueAfter 事件修改之后值
	 * */
	private boolean saveLog(UserInfo userInfo, String action, Long recordId,
							String valueBefore, String valueAfter){
		String appCode = "event";//应用平台编码
		String tableName = "t_event";
		String modelCode = "EVENT";
		String operaId = UUID.randomUUID().toString().replaceAll("-", "");
		DataLog dataLog = new DataLog();
		Result logResult = null;
		boolean logFlag = false;
		
		dataLog.setChangeStatus("S");
		dataLog.setAction(action);
		dataLog.setMemoInfo(tableName+"--"+recordId);
		dataLog.setAppCode(appCode);
		dataLog.setChangeTime(new Date());
		dataLog.setChangeValeAfter(valueAfter);
		dataLog.setChangeValeBefore(valueBefore);
		dataLog.setModelCode(modelCode);
		dataLog.setOperateId(operaId);
		dataLog.setRecordId(String.valueOf(recordId));
		dataLog.setTableName(tableName);
		dataLog.setOrgCode(userInfo.getOrgCode());
		dataLog.setUserId(userInfo.getUserId());
		dataLog.setUserName(userInfo.getPartyName());
		dataLog.setOrgCode(userInfo.getOrgCode());
		dataLog.setUserId(userInfo.getUserId());
		
		try {
			logResult = logService.saveUserOperateLog(dataLog);
		} catch (Exception e) {
			if(logger.isErrorEnabled()) {
				logger.info("记录事件操作日志失败！ " + e.getMessage());
			}
		}
		
		if(logResult != null) {
			logFlag = logResult.getResultCode() == 0;
			
			if(logFlag) {
				if(logger.isInfoEnabled()) {
					logger.info("记录事件操作日志成功！ ");
				}
			} else {
				if(logger.isErrorEnabled()) {
					logger.error("记录事件操作日志失败！" + logResult.getResultMemo());
				}
			}
		}
		
		return logFlag;
	}

	@Override
	public void expandFormatMapDataOut(Map<String, Object> eventMap,UserInfo userInfo, Map<String, Object> params) {

	}
	
	@Override
	public EventDisposal init4Event(EventDisposal event, UserInfo userInfo, Map<String, Object> params) throws Exception {
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		
		event = event == null ? new EventDisposal() : event;
		
		return eventDisposalService.defaultEvent(event, userInfo);
	}
}
