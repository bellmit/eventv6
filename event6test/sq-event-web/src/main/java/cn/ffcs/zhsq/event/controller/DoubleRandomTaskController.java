package cn.ffcs.zhsq.event.controller;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.shequ.crypto.HashIdManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.eventReportRecord.domain.EventReportRecordInfo;
import cn.ffcs.shequ.eventReportRecord.service.IEventReportRecordService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.GridAdmin;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.event.IEvaResultService;
import cn.ffcs.shequ.zzgl.service.grid.IGridAdminService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserExBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.DoubleRandomTaskService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.task.DoubleRandomTask;
import cn.ffcs.zhsq.mybatis.domain.event.task.DoubleRandomTaskStatistics;
import cn.ffcs.zhsq.utils.ArrayUtil;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.JsonUtils;
import cn.ffcs.zhsq.utils.domain.App;

/**   
 * @Description: 双随机任务模块控制器
 * @Author: dingyw
 * @Date: 11-07 10:15:17
 * @Copyright: 2017 福富软件
 */ 
@Controller("doubleRandomTaskController")
@RequestMapping("/zhsq/event/doubleRandomTask")
public class DoubleRandomTaskController extends ZZBaseController{

	@Autowired
	private DoubleRandomTaskService doubleRandomTaskService; //注入双随机任务模块服务
	@Autowired
	private IFunConfigurationService configurationService;//功能配置
	@Autowired
	private IAttachmentService attachmentService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private IGridAdminService iGridAdminService;
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	private HessianFlowService hessianFlowService;
	@Autowired
	private IEventReportRecordService eventReportRecordService;
	@Autowired
	private EventDisposalController eventDisposalController;
	@Autowired
	private UserManageOutService userManageOutService;
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	@Autowired
	private IEvaResultService iEvaResultService;
	/**
	 * 跳转至双随机抽查页面
	 * @param request
	 * @param model
	 * @param taskType 不传或传1则为登录市级账号的双随机数据,2:为镇街登录账号的双随机数据
	 * @return
	 */
	@RequestMapping({"/toGenerateEvent","index"})
	public String toGenerateEvent(HttpServletRequest request,ModelMap model,String taskType,Integer delay) {
		Date setDate = null;
		if(delay != null)
			setDate = new Date(new Date().getTime()-delay*1L*30*24*3600*1000);
		else
			setDate = new Date();
		if(StringUtils.isBlank(taskType)){
			taskType = "1";
		}
		Map<String,Object> ff = DateUtils.getFirstday_Lastday_Month(setDate,true);//当月第一天后最后一天
		String startTime = ff.get("first").toString();
		String endTime = ff.get("last").toString();
		HttpSession session = request.getSession();
		String targetGridCode = getDefaultInfoOrgCode(session);
		List<EventDisposal> eventDataList = doubleRandomTaskService.getRandomEvent(targetGridCode,startTime, endTime, 5);//随机抽取N条事件数据前端展示
		int eventSize = eventDataList==null?0:eventDataList.size();
		model.put("eventDataList",eventSize>0?eventDataList:null);
		if(eventSize>0){
			List<GridAdmin> personList = null;
			if("2".equals(taskType)){//镇街登录账号双随机
				personList = getRandomPersonData(request,"IN",eventDataList.get(0).getGridCode(),6);//随机抽取M条督察员
			}else{
				personList = getRandomPersonData(request,"OUT",eventDataList.get(0).getGridCode(),6);//市的随机抽取M条督察员
				if(personList == null || personList.size()==0){
					personList = getRandomPersonData(request,"IN",eventDataList.get(0).getGridCode(),6);//随机抽取M条督察员
				}
			}
			if(personList == null || personList.size()==0)
				personList = null;
			model.put("personList",personList);
		}
		model.put("imgDownPath", App.IMG.getDomain(request.getSession()));//用于附件上传组件中的缩略图展示
		model.put("taskType", taskType);
		return "/zzgl/event/doubleRandomTask/index.html";
	}
	
	/**
	 * 抽取随机的数据
	 * @param request
	 * @param type IN/OUT
	 * @param gridCode 事件所属网格
	 * @param delay 延迟的月(不传则为当月)
	 * @return
	 */
	@RequestMapping({"/generateEventData"})
	@ResponseBody
	public Object generateEventData(HttpServletRequest request,@RequestParam("type")String type,
			@RequestParam(value="gridCode",required=false)String gridCode,Integer delay){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("success", false);
		if(gridCode == null){
			Date setDate = null;
			if(delay != null)
				setDate = new Date(new Date().getTime()-delay*1L*30*24*3600*1000);
			else
				setDate = new Date();
			Map<String,Object> ff = DateUtils.getFirstday_Lastday_Month(setDate,true);//当月第一天后最后一天
			String startTime = ff.get("first").toString();
			String endTime = ff.get("last").toString();
			HttpSession session = request.getSession();
			String targetGridCode = getDefaultInfoOrgCode(session);
			List<EventDisposal> eventDataList = doubleRandomTaskService.getRandomEvent(targetGridCode,startTime, endTime, 1);//随机抽取100条事件数据前端展示
			int eventSize = eventDataList==null?0:eventDataList.size();
			if(eventSize>0){
				EventDisposal event = eventDataList.get(0);
				result.put("event", event);
				List<GridAdmin> personList = getRandomPersonData(request,type,event.getGridCode(),2);//随机抽取M条督察员
				if(personList != null && personList.size()>0){
					for(GridAdmin admin : personList){
						Map<String,Object> data = getUserInfo(admin.getUserId(),null);
						admin.setPartyName2((String)data.get("userName"));
						admin.setMobileTelephone((String)data.get("mobile"));
					}
					result.put("success", true);
					result.put("personList", personList);
				}else{
					result.put("msg", "暂无符合条件的督查员");
				}
			}else{
				result.put("msg", "暂无符合条件的事件数据");
			}
		}else{
			List<GridAdmin> personList = getRandomPersonData(request,type,gridCode,2);//指定督查事件,事件所属网格已存在
			if(personList != null && personList.size()>0){
				for(GridAdmin admin : personList){
					Map<String,Object> data = getUserInfo(admin.getUserId(),null);
					admin.setPartyName2((String)data.get("userName"));
					admin.setMobileTelephone((String)data.get("mobile"));
				}
				result.put("success", true);
				result.put("personList", personList);
			}else{
				result.put("msg", "暂无符合条件的督查员");
			}
		}
		return result;
	}
	
	/**
	 * 根据事件所属网格随机获取size个数的督察员
	 * @param request
	 * @param type 类型: IN：目标数据范围内，OUT：目标数据范围外
	 * @param gridCode 事件所属网格
	 * @param size 督察员数量
	 */
	public List<GridAdmin> getRandomPersonData(HttpServletRequest request,String type,String gridCode,int size){
		HttpSession session = request.getSession();
		String targetGridCode = getDefaultInfoOrgCode(session);
		if(StringUtils.isNotEmpty(gridCode)){
			if(gridCode.length() > 9)
				gridCode = gridCode.substring(0, 9);
		}
		//接口说明: http://192.168.21.83/pages/viewpage.action?pageId=3375611
		List<GridAdmin> list = iGridAdminService.findGridAdminRandom(gridCode,targetGridCode,type, size, null);
		for(GridAdmin admin : list){
			MixedGridInfo gridInfo = this.mixedGridInfoService.findMixedGridInfoById(admin.getGridId(), false);
			if(gridInfo != null){
				String gridName = gridInfo.getGridName();
				admin.setGridPath(gridInfo.getGridPath());
				admin.setGridName(gridName);
			}
		}
		return list;
	}
	/**
	 * 事件列表展示
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/listEvent")
	public String listEvent(HttpServletRequest request, 
			@RequestParam(value = "trigger", required = false) String trigger,
			@RequestParam(value = "eventAttrTrigger", required = false) String eventAttrTrigger,
			@RequestParam(value = "extraParams", required = false) String extraParams,
			ModelMap map,Integer delay) {
		HttpSession session = request.getSession();
		String eventType = "all";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		Map<String, Object> defaultGridInfo = getDefaultGridInfo(session);
		map.addAttribute("defaultInfoOrgCode",defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("orgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("gridName", defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("startGridCodeName", defaultGridInfo.get(KEY_START_GRID_CODE_NAME));
		List<BaseDataDict> bigTypeDC = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.BIG_TYPE_PCODE, userInfo.getOrgCode());
		List<BaseDataDict> smallTypeDC = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SMALL_TYPE_PCODE, userInfo.getOrgCode());
		
		List<BaseDataDict> collectWayDC = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.COLLECT_WAY_PCODE, userInfo.getOrgCode());
		List<BaseDataDict> urgencyDegreeDC = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.URGENCY_DEGREE_PCODE, userInfo.getOrgCode());
		List<BaseDataDict> influenceDegreeDC = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.INFLUENCE_DEGREE_PCODE, userInfo.getOrgCode());
		List<BaseDataDict> sourceDC = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SOURCE_PCODE, userInfo.getOrgCode());
		List<BaseDataDict> statusDC = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.STATUS_PCODE, userInfo.getOrgCode());
		List<BaseDataDict> attrFlagDC = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.ATTR_FLAG_PCODE, userInfo.getOrgCode());
		
		map.addAttribute("attrFlagDC", attrFlagDC);
		map.addAttribute("statusDC", statusDC);
		map.addAttribute("bigTypeDC", bigTypeDC);
		map.addAttribute("smallTypeDC", smallTypeDC);

		map.addAttribute("collectWayDC", collectWayDC);
		map.addAttribute("urgencyDegreeDC", urgencyDegreeDC);
		map.addAttribute("influenceDegreeDC", influenceDegreeDC);
		map.addAttribute("sourceDC", sourceDC);
		
		int isNoShow = 0;
		String itemCodes = ConstantValue.EVENT_NO_SHOW;
		String[] itemCodeArr = itemCodes.split(",");
		List<String> itemCodeList = Arrays.asList(itemCodeArr);
		for (int i = 0; i < itemCodeList.size(); i++) {
			if(userInfo.getOrgCode().startsWith(itemCodeList.get(i))){
				isNoShow = 1;
				break;
			}
		}
		//String[] eventTypeOrder = {"todo,待办事项","my,我发起的","done,已办事项","history,归档事项","draft,草稿事项","all,辖区所有","toRemind,督办事件"};
		map.addAttribute("isNoShow", isNoShow);
		map.addAttribute("eventType", eventType);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));

		//事件列表中可展示的事件类别，空则展示所有
		if(StringUtils.isNotBlank(trigger)){
			String typesForList = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
			boolean isRemoveTypes = false;
			
			if(StringUtils.isBlank(typesForList)) {
				isRemoveTypes = true;
				typesForList = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
			}
			
			if(StringUtils.isNotBlank(typesForList)) {
				typesForList = typesForList.trim();
				
				String[] typesTemp = typesForList.split(",");
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
				param.put("orgCode", userInfo.getOrgCode());
				
				BaseDataDict dataDict = null;
				StringBuffer typesDictCode = new StringBuffer("");
				
				for(int index = 0, len = typesTemp.length; index < len; index++){
					String typeTemp = typesTemp[index];
					String dictCode = "";
					
					if(StringUtils.isNotBlank(typeTemp)){
						param.put("dictGeneralCode", typeTemp);
						dataDict = baseDictionaryService.findByCodes(param);
						dictCode = dataDict.getDictCode();
						if(StringUtils.isNotBlank(dictCode)){
							typesDictCode.append(dictCode).append(",");
						}
					}
				}
				
				if(typesDictCode.length() > 0){
					typesDictCode = new StringBuffer(typesDictCode.subSequence(0, typesDictCode.length()-1));
					map.addAttribute("typesDictCode", "'" + typesDictCode.toString().replaceAll(",", "','") + "'");
					map.addAttribute("typesForList", typesForList);
					map.addAttribute("isRemoveTypes", isRemoveTypes);
				}
				
				map.addAttribute("trigger", trigger);//事件类型筛选触发条件
			}
		}
		
		//设置事件属性过滤触发条件
		if(StringUtils.isNotBlank(eventAttrTrigger)){
			map.addAttribute("eventAttrTrigger", eventAttrTrigger);
		}
		
		//设置额外参数
		if(StringUtils.isNotBlank(extraParams)){
			Map<String, Object> extraParamsMap = null;
			try{
				extraParamsMap = JsonUtils.json2Map(extraParams);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(extraParamsMap != null){
				map.addAttribute("extraParams", extraParams);
				Set<String> keySet = extraParamsMap.keySet();
				if(keySet!=null && keySet.size()>0){
					for(String keyStr : keySet){
						map.addAttribute(keyStr, extraParamsMap.get(keyStr).toString());
					}
				}
				
			}
		}
		map.addAttribute("delay", delay);
		map.addAttribute("model", "c");
		String dc = request.getParameter("dc");
		if(StringUtils.equals("dc", dc)) 
			return "/zzgl/event/doubleRandomTask/list_event_drt_db_dc.html";//返回督查列表
		if(StringUtils.equals("assign", dc)) 
			return "/zzgl/event/doubleRandomTask/list_event_drt_db_assign.html";//返回选定事件列表(抽取的时候选定事件)
		return "/zzgl/event/doubleRandomTask/list_event_drt_db.html";
	}
	
	@RequestMapping("sendSms")
	@ResponseBody
	public Object sendSms(String otherMobileNums,String userIds,HttpServletRequest request){
		String smsContent = "您好,您有一条新的督查任务,请及时处理";
		String isTest = request.getParameter("isTest");
		if("true".equals(isTest)){//测试使用
			userIds = null;
			otherMobileNums = "18206058762";
		}
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
		boolean b = eventDisposalWorkflowService.sendSmsSyn(userIds, otherMobileNums, smsContent, userInfo);
		return b;
	}
	/**
	 * 事件列表数据加载
	 * @param request
	 * @param session
	 * @param page
	 * @param rows
	 * @param eventType
	 * @param keyWord
	 * @param event
	 * @param generalSearch
	 * @param startHappenTime
	 * @param endHappenTime
	 * @param collectWays
	 * @param sources
	 * @param urgencyDegrees
	 * @param influenceDegrees
	 * @param types 事件大类 有传值时，以英文逗号开始
	 * @param eventStatus
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listData", method = RequestMethod.POST)
	public EUDGPagination listData(
			HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "eventType") String eventType,
			@RequestParam(value = "keyWord", required = false) String keyWord,
			@ModelAttribute(value = "event") EventDisposal event,
			@RequestParam(value = "generalSearch", required = false) String generalSearch,
			@RequestParam(value = "startHappenTime", required=false) String startHappenTime,
			@RequestParam(value = "endHappenTime", required=false) String endHappenTime,
			@RequestParam(value = "collectWays[]", required=false) String collectWays[],
			@RequestParam(value = "sources[]", required=false) String sources[], 
			@RequestParam(value = "statuss[]", required=false) String statuss[],
			@RequestParam(value = "urgencyDegrees[]", required=false) String urgencyDegrees[],
			@RequestParam(value = "influenceDegrees[]", required=false) String influenceDegrees[], 
			@RequestParam(value = "attrFlags[]", required=false) String attrFlags[], 
			@RequestParam(value = "handleStatus", required=false) String handleStatus,
			@RequestParam(value = "handleStatuss", required=false) String handleStatuss,
			@RequestParam(value = "remindStatus", required=false) String remindStatus,
			@RequestParam(value = "types", required=false) String types,
			@RequestParam(value = "eventStatus", required = false) String eventStatus,
			@RequestParam(value = "typesForList", required = false) String typesForList,
			@RequestParam(value = "handleDateFlag", required=false) String handleDateFlag,
			@RequestParam(value = "eventAttrTrigger", required = false) String eventAttrTrigger,
			@ModelAttribute(value = "doubleRandomTask")DoubleRandomTask doubleRandomTask,
			@RequestParam(value = "currStatusParam", required = false) String currStatusParam,
			@RequestParam(value = "dcResultParam", required = false) String dcResultParam,
			@RequestParam Map<String, Object> paramMap,Integer delay) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if (page <= 0)
			page = 1;

		if(!StringUtils.isBlank(eventStatus)){
			eventStatus = "'" + eventStatus.trim().replaceAll(",", "','") + "'";
		}
		Map<String, Object> params = new HashMap<String, Object>();
		/*params.put("userId", userInfo.getUserId());
		params.put("orgId", userInfo.getOrgId());*/
		params.put("orgCode", getDefaultInfoOrgCode(session));
		params.put("createOrgCode", getDefaultInfoOrgCode(session));
		
		params.put("judgePerson", userInfo.getUserId());
		if(StringUtils.isNotBlank(doubleRandomTask.getCreateTimeStart2()))
			params.put("createTimeStart2", doubleRandomTask.getCreateTimeStart2());
		if(StringUtils.isNotBlank(doubleRandomTask.getCreateTimeEnd2()))
			params.put("createTimeEnd2", doubleRandomTask.getCreateTimeEnd2());
		if(StringUtils.isNotBlank(doubleRandomTask.getOutOfDateFlag()))
			params.put("outOfDateFlag", doubleRandomTask.getOutOfDateFlag());
		if(StringUtils.isNotBlank(doubleRandomTask.getTaskType()))
			params.put("taskType", doubleRandomTask.getTaskType());
		if(StringUtils.isNotBlank(doubleRandomTask.getJudgeResule()) && !StringUtils.equalsIgnoreCase(doubleRandomTask.getJudgeResule(), "all")){
			params.put("judgeResule", doubleRandomTask.getJudgeResule());
		}
		
		if(!StringUtils.isBlank(types)){
			types = types.trim();
		}else{
			types = "";
		}
		//事件列表中可展示的事件类别，空则展示所有
		if(StringUtils.isNotBlank(typesForList)){
			types = typesForList.trim()+","+types;
		}
		
		if(StringUtils.isNotBlank(types)){
			params.put("types", types);
		}
		
		if(CommonFunctions.isNotBlank(paramMap, "isRemoveTypes")) {
			params.put("isRemoveTypes", paramMap.get("isRemoveTypes"));
		}
		
		if(!StringUtils.isBlank(keyWord)){
			keyWord = keyWord.trim();
			params.put("keyWord", keyWord);
		}
		if (!StringUtils.isBlank(event.getType())){
			params.put("type", event.getType());
		}else{
			if (!StringUtils.isBlank(event.getBigType())){
				params.put("type", event.getBigType());
			}
		}
		
		if(sources != null){
			String source = ArrayUtil.ArrayToString(sources);
	 		if (!StringUtils.isBlank(source)){
				params.put("source", source);
			}
		}
		if (!StringUtils.isBlank(event.getContent())){
			params.put("content", event.getContent());
		}
		if (!StringUtils.isBlank(event.getCode())){
			params.put("code", event.getCode());
		}
		
		if(collectWays != null){
			String collectWay = ArrayUtil.ArrayToString(collectWays);
			if (!StringUtils.isBlank(collectWay)){
				params.put("collectWay", collectWay);
			}
		}
		if(influenceDegrees != null){
			String influenceDegree = ArrayUtil.ArrayToString(influenceDegrees);
			if (!StringUtils.isBlank(influenceDegree)){
				params.put("influenceDegree", influenceDegree);
			}
		}
		if(attrFlags != null){
			attrFlags = CommonFunctions.orderInturn(attrFlags);
			String attrFlag = ArrayUtil.ArrayToString(attrFlags);
			if (StringUtils.isNotBlank(attrFlag)){
				attrFlag += ",";
				attrFlag = attrFlag.replaceAll(",", ",%");
				params.put("attrFlag", attrFlag);
			}
		}
		if(StringUtils.isNotBlank(handleStatuss) && StringUtils.isNotBlank(handleStatuss.trim())){
			params.put("handleStatuss", handleStatuss.trim());
		}
		if(StringUtils.isNotBlank(handleStatus) && StringUtils.isNotBlank(handleStatus.trim())){
			params.put("handleStatus", handleStatus.trim());
		}
		if(StringUtils.isNotBlank(handleDateFlag) && StringUtils.isNotBlank(handleDateFlag.trim())){
			params.put("handleDateFlag", handleDateFlag.trim());
		}
		if(StringUtils.isNotBlank(remindStatus) && StringUtils.isNotBlank(remindStatus.trim())){
			params.put("remindStatus", remindStatus.trim());
		}
		if(statuss != null){
			eventStatus = ArrayUtil.ArrayToString(statuss);
		}
		if(urgencyDegrees != null){
			String urgencyDegree = ArrayUtil.ArrayToString(urgencyDegrees);
			if (!StringUtils.isBlank(urgencyDegree)){
				params.put("urgencyDegree", urgencyDegree);
			}
		}
		if (event.getInfoOrgCode() != null && !"".equals(event.getInfoOrgCode())){
			params.put("infoOrgCode", event.getInfoOrgCode());
		}
		if (event.getGridCode() != null){
			params.put("gridCode", event.getGridCode());
		}
		if (event.getGridId() != null){
			params.put("gridId", event.getGridId());
		}
		if(!StringUtils.isBlank(event.getHappenTimeStart())){
			params.put("happenTimeStart", event.getHappenTimeStart());
		}
		if(!StringUtils.isBlank(event.getHappenTimeEnd())){
			params.put("happenTimeEnd", event.getHappenTimeEnd());
		}
		if(!StringUtils.isBlank(event.getCreateTimeStart())){
			params.put("createTimeStart", event.getCreateTimeStart());
		}
		if(!StringUtils.isBlank(event.getCreateTimeEnd())){
			params.put("createTimeEnd", event.getCreateTimeEnd());
		}
		if(!StringUtils.isBlank(generalSearch)){
			params.put("generalSearch", generalSearch);
		}
		if(!StringUtils.isBlank(startHappenTime)){
			params.put("startHappenTime", startHappenTime);
		}
		if(!StringUtils.isBlank(endHappenTime)){
			params.put("endHappenTime", endHappenTime);
		}
		if (!StringUtils.isBlank(event.getContactUser())){
			params.put("contactUser", event.getContactUser());
		}
		if(StringUtils.isNotBlank(eventAttrTrigger)){
			params.put("eventAttrTrigger", eventAttrTrigger);
			params.put("eventAttrOrgCode", userInfo.getOrgCode());
		}
		if(StringUtils.isNotBlank(event.getTaskReceivedStaus())) {
			params.put("taskReceivedStaus", event.getTaskReceivedStaus());
		}
		
		//当前状态
		if (!StringUtils.isBlank(currStatusParam)){
			params.put("currStatusParam", currStatusParam);
		}
		//督查结果
		if (!StringUtils.isBlank(dcResultParam)){
			params.put("dcResultParam", dcResultParam);
		}
		
//		params.put("statuss", statuss);
//		params.put("statusList", eventStatus);
		String dc = request.getParameter("dc");
		if(StringUtils.equals("dc", dc)){
			params.put("unionType", "OR");
			Map<String, Object> defaultGridInfo = getDefaultGridInfo(session);
			Long gridId = Long.parseLong(defaultGridInfo.get(KEY_START_GRID_ID).toString());
			params.put("judgePerson", gridId);//查看督查数据的时候,除了创建人地域可以看到数据,督查人员地域也可以看到数据
			EUDGPagination eventPageList = doubleRandomTaskService.findDcEventPageList(page, rows, params);//督查数据
			if(eventPageList.getRows() != null){
				List<EventDisposal> rows1 = (List<EventDisposal>) eventPageList.getRows();
				rows1.forEach(in->{
					in.setHashId(HashIdManager.encrypt(in.getEventId()));
					in.setEventId(-1L);
				});
			}
			return eventPageList;
		}else if(StringUtils.equals("assign", dc)){
			if(params.get("infoOrgCode")==null)
				params.put("infoOrgCode", getDefaultInfoOrgCode(session));
			Date setDate = null;
			if(delay != null)
				setDate = new Date(new Date().getTime()-delay*1L*30*24*3600*1000);
			else
				setDate = new Date();
			Map<String,Object> ff = DateUtils.getFirstday_Lastday_Month(setDate,true);//当月第一天后最后一天
			String startTime = ff.get("first").toString();
			String endTime = ff.get("last").toString();
			params.put("finStartTime", startTime);
			params.put("finEndTIme", endTime);
			EUDGPagination assignEvent = doubleRandomTaskService.findAssignEvent(page, rows, params);//指定督查数据
			if(assignEvent.getRows() != null){
				List<EventDisposal> rows1 = (List<EventDisposal>) assignEvent.getRows();
				rows1.forEach(in->{
					in.setHashId(HashIdManager.encrypt(in.getEventId()));
					in.setEventId(-1L);
				});
			}
			return assignEvent;
		}

		EUDGPagination EventPageList = doubleRandomTaskService.findEventPageList(page, rows, params);
		if(EventPageList.getRows() != null){
			List<EventDisposal> rows1 = (List<EventDisposal>) EventPageList.getRows();
			rows1.forEach(in->{
				in.setHashId(HashIdManager.encrypt(in.getEventId()));
				in.setEventId(-1L);
			});
		}
		return EventPageList;
	}
	
	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		DoubleRandomTask bo = doubleRandomTaskService.searchById(id);
		map.addAttribute("bo", bo);
		return "/gmis/doubleRandomTask/detail_doubleRandomTask.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		if (id != null) {
			DoubleRandomTask bo = doubleRandomTaskService.searchById(id);
			map.put("bo", bo);
		}
		return "/gmis/doubleRandomTask/form_doubleRandomTask.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		DoubleRandomTask bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		if (bo.getId() == null) { //新增
			Long id = doubleRandomTaskService.insert(bo);
			if (id != null && id > 0) {
				result = "success";
			};
		} else { //修改
			boolean updateResult = doubleRandomTaskService.update(bo);
			if (updateResult) {
				result = "success";
			}
		}
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		DoubleRandomTask bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = doubleRandomTaskService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 跳转至统计页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("toStatistics")
	public String toStatistics(HttpServletRequest request,ModelMap model){
		Map<String,Object> ff = DateUtils.getFirstday_Lastday_Month(new Date(),false);
		String curMonth = ff.get("first").toString()+" ~ "+ff.get("last").toString();
		model.put("curMonth", curMonth);
		return "/zzgl/event/doubleRandomTask/list_statistics.html";
	}
	
	/**
	 * 获取统计数据
	 * @param query
	 * @return
	 */
	@RequestMapping("getStatisticsData")
	@ResponseBody
	public Object getStatisticsData(DoubleRandomTaskStatistics query,HttpServletRequest request){
		if(StringUtils.isBlank(query.getStartTime())){
			Map<String,Object> ff = DateUtils.getFirstday_Lastday_Month(new Date(),true);//当月第一天后最后一天
			query.setStartTime(ff.get("first").toString());
			query.setEndTime(ff.get("last").toString());
		}else if(query.getStartTime().length()==7){
			try {
				Date d = DateUtils.convertStringToDate(query.getStartTime(),"yyyy-MM");
				Map<String,Object> ff = DateUtils.getFirstday_Lastday_Month(d,true);//选择月份的第一天后最后一天
				query.setStartTime(ff.get("first").toString());
				query.setEndTime(ff.get("last").toString());
			} catch (ParseException e) {
				throw new RuntimeException("日期参数异常");
			}
		}else if(query.getStartTime().length()==10 && StringUtils.isNotBlank(query.getStartTime()) && query.getEndTime().length()==10){
			query.setStartTime(query.getStartTime()+" 0:00:00");
			query.setEndTime(query.getEndTime() + "23:59:59");
		}else{
			throw new RuntimeException("参数异常:需要传参startTime,且格式为yyyy-MM");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startTime", query.getStartTime());
		params.put("endTime", query.getEndTime());
		List<DoubleRandomTaskStatistics> data = doubleRandomTaskService.statisticsByCountry(params);
		EUDGPagination pagination = new EUDGPagination(data.size(), data);
		return pagination;
	}
	
	/**
	 * 复核事件
	 * @param dId 双随机事件id
	 * @param eventId 事件id
	 * @param instanceId
	 * @param workFlowId
	 * @param map
	 * @return
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/judgeEvent")
	public String judgeEvent(HttpServletRequest request,
			@RequestParam(value = "dId") Long dId,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "instanceId", required=false) String instanceId,
			@RequestParam(value = "workFlowId", required=false) String workFlowId,
			ModelMap map) throws NumberFormatException, ParseException {
		Pattern pattern = Pattern.compile("[0-9]*");
	    System.out.println("instanceId================="+instanceId);

		String hashId = request.getParameter("hashId");
		if(StringUtils.isNotBlank(hashId) && eventId == null){
			eventId = HashIdManager.decryptLong(hashId);
		}
		if (StringUtils.isBlank(instanceId) || "null".equals(instanceId)) {
			instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(eventId);
		}
		if(StringUtils.isNotBlank(workFlowId) && !pattern.matcher(workFlowId.trim()).matches()){
			workFlowId = null;
		}
		map.put("dId", dId);
		eventDisposalController.toAddEventByType(request.getSession(), eventId, "", "", false, new HashMap<String, Object>(), map);
		return judgeEventType(request,dId,eventId, instanceId, workFlowId, "all", map);
	}
	
	private String judgeEventType(HttpServletRequest request,
			Long dId,Long eventId,String instanceId,String workFlowId,String eventType,
			ModelMap map) throws NumberFormatException, ParseException {
		String type = "";
		Long instanceIdL = -1L;
		map.put("mapEngineType", "");
		HttpSession session = request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		EventDisposal event = eventDisposalService.findEventByIdAndMapt(eventId,null,getDefaultInfoOrgCode(session));

		if(event != null) {
			type = event.getType();
		}

		@SuppressWarnings("deprecation")
		String workflowCtx = hessianFlowService.getWorkflowCtx();
		Pattern pattern = Pattern.compile("[0-9]*");
		String taskId = "";
		String handleTime = null;
		if (StringUtils.isNotBlank(instanceId) && pattern.matcher(instanceId.trim()).matches()) {
			instanceIdL = Long.valueOf(instanceId);
			Long taskIdL = eventDisposalWorkflowService.curNodeTaskId(instanceIdL);
			if(taskIdL!=null && !taskIdL.equals(-1L)){
				taskId = taskIdL.toString();
				Map<String, Object> taskInfo = null;
				//待办、我发起的
				try{
					taskInfo = hessianFlowService.getStartTimeAndTimeLimitByTaskId(taskId);
					String startTime = (String) taskInfo.get("START_TIME");
					Integer limitTime = Integer.parseInt(taskInfo.get("LIMIT_TIME").toString());
					if (StringUtils.isNotBlank(startTime) && limitTime != null) {
						handleTime = DateUtils.convertDateToString(DateUtils.addInterval(DateUtils
								.convertStringToDate(startTime, DateUtils.PATTERN_24TIME), limitTime, "00"));
					}
				}catch(Exception e){
					handleTime = null;
				}
				
			}
		}else{
			instanceId = null;
		}
		
		String filedomain = App.IMG.getDomain(session);
		map.put("downPath", filedomain);
		map.put("workflowCtx", workflowCtx);
		map.put("event", event);
		map.put("showNotice", "");
		
		map.addAttribute("instanceId", instanceId);
		map.addAttribute("workFlowId", workFlowId);
		map.addAttribute("taskId", taskId);
		
		if(!map.containsAttribute("curNodeTaskName")) {
			String taskName = null;
			
			try {
				taskName = eventDisposalWorkflowService.curNodeTaskName(instanceIdL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(!StringUtils.isBlank(taskName)){
				map.addAttribute("curNodeTaskName", taskName);
			}
		}
		
		map.addAttribute("model", "1");
		map.addAttribute("position", "");
		
		if(!StringUtils.isBlank(taskId)){//获取当前办理人信息
			List<Map<String, Object>> taskPerson = eventDisposalWorkflowService.queryMyTaskParticipation(taskId);
			StringBuffer taskPersonStr = null;
			if(taskPerson!=null && taskPerson.size()>0){
				taskPersonStr = new StringBuffer(";");
				boolean isCurrentUser = false;//判断当前登录人员是否在为当前办理人
				
				for(Map<String, Object> mapTemp : taskPerson){
					Object userTypeObj = mapTemp.get("USER_TYPE");//1表示USER_ID为组织ID；3表示USER_ID为用户ID
					Object orgNameObj = mapTemp.get("ORG_NAME");
					Long userOrgId = -1L;//当前办理人员所属组织
					
					if(CommonFunctions.isNotBlank(mapTemp, "ORG_ID")) {
						try {
							userOrgId = Long.valueOf(mapTemp.get("ORG_ID").toString());
						} catch(NumberFormatException e) {}
					}
					if(CommonFunctions.isNotBlank(mapTemp, "USER_NAME")){
						taskPersonStr.append(mapTemp.get("USER_NAME"));
						if(orgNameObj != null){
							taskPersonStr.append("(").append(orgNameObj).append(");");
						}
					}else if(orgNameObj != null){
						taskPersonStr.append(orgNameObj).append(";");
					}
					if(CommonFunctions.isNotBlank(mapTemp, "USER_ID") && !isCurrentUser){
						Long userId = Long.valueOf(mapTemp.get("USER_ID").toString());
						String userType = "";//1表示USER_ID为组织ID；3表示USER_ID为用户ID
						
						if(userTypeObj != null){
							userType = userTypeObj.toString();
						}
						//当前办理人要相同的用户和相同的组织
						if(IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userType) && userInfo.getUserId().equals(userId) && userInfo.getOrgId().equals(userOrgId)){
							isCurrentUser = true;
						}else if(IEventDisposalWorkflowService.ACTOR_TYPE_ORG.equals(userType) && userInfo.getOrgId().equals(userId)){
							isCurrentUser = true;
						}
					}
				}
				
				if(isCurrentUser){//当前登录人员未在当前办理人中时，不可办理该事件
					String handleEventPage = eventDisposalWorkflowService.capWorkflowHandlePage(eventId, type, userInfo, null);
					//是否上传处理中图片
					String isUploadHandlingPic = configurationService.turnCodeToValue(ConstantValue.IS_UPLOAD_HANDLING_PIC, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
					
					map.addAttribute("isUploadHandlingPic", Boolean.valueOf(isUploadHandlingPic));
					
					map.addAttribute("evaLevelDict", baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.EVALUATE_LEVEL_PCODE, userInfo.getOrgCode()));
					
					map.addAttribute("handleEventPage", handleEventPage);
				}
				
				taskPersonStr = new StringBuffer(taskPersonStr.substring(1));
			}
			map.addAttribute("taskPersonStr", taskPersonStr);
		}
		map.addAttribute("eventType", eventType);
		
		//获取关联模块
		EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();
		if(eventId!=null && !"".equals(eventId)){
			eventReportRecordInfo.setEventId(eventId);
			eventReportRecordInfo = eventReportRecordService.findEventReportRecordInfoByEventId(eventReportRecordInfo);
			String bizDetailUrl = "";
			String bizDetailUrl2 = "";
			if(eventReportRecordInfo!=null && eventReportRecordInfo.getBizType() != null){
				bizDetailUrl2 = configurationService.turnCodeToValue(ConstantValue.BIZ_DETAIL_URL_IN_EVENT, eventReportRecordInfo.getBizType(), IFunConfigurationService.CFG_TYPE_URL, IFunConfigurationService.CFG_ORG_TYPE_0);
				if(eventReportRecordInfo.getBizId() != null){
					bizDetailUrl = bizDetailUrl2.replaceFirst("\\$bizId", eventReportRecordInfo.getBizId()+"");
				}
				
				map.addAttribute("bizDetailUrl", bizDetailUrl);
			}
			//解决跨域关闭按钮的问题
			map.addAttribute("isDomain", "isDomain");
		}
		map.addAttribute("EVENT_ATTACHMENT_TYPE", ConstantValue.EVENT_ATTACHMENT_TYPE);//上传附件的类型
		
		//展示事件处理时限判断
		String showEventHandleDate = configurationService.turnCodeToValue(ConstantValue.SHOW_HANDLE_DATE, ConstantValue.SHOW_EVENT_HANDLE_DATE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(showEventHandleDate)){
			boolean flag = Boolean.valueOf(showEventHandleDate);
			if(flag){
				map.addAttribute("eventRemainTime", event.getRemainTime());
			}else{
				event.setHandleDateStr(null);
				map.addAttribute("event", event);
			}
		}
		//展示环节处理时限判断
		String showStepHandleDate = configurationService.turnCodeToValue(ConstantValue.SHOW_HANDLE_DATE, ConstantValue.SHOW_STEP_HANDLE_DATE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(showStepHandleDate)){
			boolean flag = Boolean.valueOf(showStepHandleDate);
			if(flag){
				map.addAttribute("stepRemainTime", event.getRemainTime(handleTime));
				map.addAttribute("handleTime", handleTime);
			}
		}
		
		map.put("mapt", "");
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		
		//是否在新窗口打开图片
		String isOpenInNewWindows = configurationService.turnCodeToValue(ConstantValue.IS_OPEN_NEW_WINDOWS, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isOpenInNewWindows", Boolean.valueOf(isOpenInNewWindows));	
		
		map.addAttribute("markerOperation", ConstantValue.TITLE_MARKER); // 查看地图标注操作
		
		String modeType = request.getParameter("modeType");
		if (StringUtils.isNotBlank(modeType)) {
			map.addAttribute("modeType", modeType.trim());
		}
		//return "/zzgl/event/detail_event.ftl";
		return "/zzgl/event/doubleRandomTask/judge_event.html";
	}

	/**
	 * 个人查看事件详情
	 * @param request
	 * @param dId    双随机事件主键
	 * @param eventId
	 * @param instanceId
	 * @param workFlowId
	 * @param model
	 * @return
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/detailEvent")
	public String detailEvent(HttpServletRequest request,
			@RequestParam(value = "dId") Long dId,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "instanceId", required=false) String instanceId,
			@RequestParam(value = "workFlowId", required=false) String workFlowId,
			ModelMap model) throws NumberFormatException, ParseException {
		
		
		DoubleRandomTask task = doubleRandomTaskService.searchById(dId);
		Long personId = task.getJudgePerson();
		Map<String,Object> extData = getUserInfo(personId,task.getJudgePersonGridId());
		task.setExtData(extData);
		List<Attachment> attachments = attachmentService.findByBizId(task.getId(), ConstantValue.EVENT_RANDOM_ATTACHMENT);
		model.put("imgDownPath", App.IMG.getDomain(request.getSession()));//用于附件上传组件中的缩略图展示
		model.put("attachments", attachments);
		model.put("randomTask", task);
		model.put("eventId", eventId);
		model.put("instanceId", instanceId);
		model.put("workFlowId", workFlowId);
		model.put("dId", dId);
		//eventDisposalController.toAddEventByType(request.getSession(), eventId, "", "", false, model);
		return "/zzgl/event/doubleRandomTask/detail_event.html";
	}
	
	
	@RequestMapping(value = "/dcDetailEvent")
	public String dcDetailEvent(HttpServletRequest request,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "instanceId", required=false) String instanceId,
			@RequestParam(value = "workFlowId", required=false) String workFlowId,
			ModelMap model) throws NumberFormatException, ParseException {
		String hcResult = "合格";
//		boolean flag = true;
//		long currentTimeMils = new Date().getTime();//当前时间毫秒数
		Map<String,Object> params = new HashMap<String,Object>();
		String hashId = request.getParameter("hashId");
		if(StringUtils.isNotBlank(hashId) && eventId == null){
			eventId = HashIdManager.decryptLong(hashId);
		}
		params.put("eventId", eventId);
		params.put("taskType", "all");//详情页面需要查看到所有的数据,不应该根据这个进行过滤
		
		@SuppressWarnings("unchecked")
		List<DoubleRandomTask> tasks = (List<DoubleRandomTask>)doubleRandomTaskService.searchList(1, 1000, params).getRows();
		String judgeResult = null;
		List<Attachment> attachments = null;
		for(DoubleRandomTask task : tasks){
			Long personId = task.getJudgePerson();
			Map<String,Object> extData = getUserInfo(personId,task.getJudgePersonGridId());
			attachments = attachmentService.findByBizId(task.getId(), ConstantValue.EVENT_RANDOM_ATTACHMENT);
			if(attachments != null){
				extData.put("attachments", attachments);
			}
			task.setExtData(extData);
			judgeResult = task.getJudgeResule();
			if(judgeResult==null){
				hcResult = "核查中";
				/*if(currentTimeMils-task.getUpdateTime().getTime()<=3*24*3600*1000L){
					if(flag)
						hcResult = "核查中";
					else
						hcResult = "不合格";
				}else{
					hcResult = "不合格";
					flag = false;
				}*/
			}else{
				if(StringUtils.equals(task.getJudgeResule(), "2")){
					hcResult = "不合格";
//					flag = false;
				}
			}
		}
		
		model.put("hcResult", hcResult);
		model.put("imgDownPath", App.IMG.getDomain(request.getSession()));//用于附件上传组件中的缩略图展示
		model.put("randomTasks", tasks);
		model.put("eventId", eventId);
		model.put("instanceId", instanceId);
		model.put("workFlowId", workFlowId);
		//eventDisposalController.toAddEventByType(request.getSession(), eventId, "", "", false, model);
		return "/zzgl/event/doubleRandomTask/dc_detail_event.html";
	}
	
	/**
	 * 获取缓存的用户信息
	 * @param userId
	 * @return
	 */
	@Cacheable("doubleEventUserCache")
	private Map<String,Object> getUserInfo(Long userId,Long gridId){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		UserBO bo = userManageOutService.getUserInfoByUserId(userId);//获取用户基本信息
		resultMap.put("mobile", bo.getVerifyMobile()+"");
		resultMap.put("userName", bo.getPartyName());
		UserExBO exbo = userManageOutService.findUserExBoForUserId(userId).get(0);//获取组织
		resultMap.put("orgName", exbo.getOrgName());
		OrgEntityInfoBO ebo = orgSocialInfoOutService.selectOrgEntityInfoByOrgCode(exbo.getOrgCode());//组织转地域
		resultMap.put("regionName", ebo.getOrgName());
		if(gridId != null){
			MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
			resultMap.put("gridName", mixedGridInfo.getGridPath());
		}
		return resultMap;
	}
	
	
	@RequestMapping("saveDoubleRandomEvent")
	@ResponseBody
	public Object saveDoubleRandomEvent(HttpServletRequest request,DoubleRandomTask task,
			@RequestParam("judgePersons[]")String[] judgePersons) throws CloneNotSupportedException{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", false);
		HttpSession session = request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		task.setCreateOrgCode(getDefaultInfoOrgCode(session));
		task.setCreateUserId(userInfo.getUserId());
		task.setStatus("1");
		Long id = null;
		if(judgePersons==null || judgePersons.length==0){
			resultMap.put("msg", "督查人员不能为空");
		}else{
			try {
				for(int i=0,len=judgePersons.length;i<len;i++){
					if(i==0){
						task.setJudgePerson(Long.parseLong(judgePersons[i].split("_")[0]));
						task.setJudgePersonGridId(Long.parseLong(judgePersons[i].split("_")[1]));
						id = doubleRandomTaskService.insert(task);
					}else{
						DoubleRandomTask clone = (DoubleRandomTask)task.clone();
						clone.setJudgePerson(Long.parseLong(judgePersons[i].split("_")[0]));
						clone.setJudgePersonGridId(Long.parseLong(judgePersons[i].split("_")[1]));//保存网格信息
						doubleRandomTaskService.insert(clone);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (id != null && id > 0) {
			resultMap.put("success", true);
		};
		return resultMap;
	}
	
	/**
	 * 保存复核结果
	 * @param request
	 * @param task
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("recheckDoubleRandomEvent")
	public Object recheckDoubleRandomEvent(
			HttpServletRequest request,DoubleRandomTask task,
			@RequestParam(value = "attachmentId", required = false) Long[] ids) throws Exception{
		boolean success = doubleRandomTaskService.update(task);
		if(success && ids!=null && ids.length>0){//保存图片
			attachmentService.updateBizId(task.getId(), ConstantValue.EVENT_RANDOM_ATTACHMENT, ids);
		}
		if(success){
			if(StringUtils.equals("2", task.getJudgeResule())){//不合格数据更改评价结果
				task = doubleRandomTaskService.searchById(task.getId());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("evaObj", "03");
				params.put("objectId", task.getEventId());
				UserInfo userInfo = (UserInfo) request.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
				params.put("evaLevel", "01");
				params.put("evaContent", "督查员["+userInfo.getUserName()+"]将评价结果调整为不满意");
				params.put("creatorName", userInfo.getUserName());
				iEvaResultService.updateEvaResultByMap(params);
			}
		}
		return success;
	}
}