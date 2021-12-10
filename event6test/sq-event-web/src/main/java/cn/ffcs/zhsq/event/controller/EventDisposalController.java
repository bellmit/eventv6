package cn.ffcs.zhsq.event.controller;

import cn.ffcs.gis.base.service.IResMarkerService;
import cn.ffcs.gis.mybatis.domain.base.ResMarker;
import cn.ffcs.gmis.mybatis.domain.userscore.UserDetailScore;
import cn.ffcs.gmis.userscore.service.UserScoreService;
import cn.ffcs.log.api.bo.DataLog;
import cn.ffcs.log.api.bo.Result;
import cn.ffcs.log.api.service.LogService;
import cn.ffcs.oa.entity.interalrule.IdRecord;
import cn.ffcs.oa.service.interalrule.IntegralRuleService;
import cn.ffcs.resident.service.PartyIndividualService;
import cn.ffcs.shequ.eventReportRecord.domain.EventReportRecordInfo;
import cn.ffcs.shequ.eventReportRecord.service.IEventReportRecordService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EventRelaItem;
import cn.ffcs.shequ.mybatis.domain.zzgl.globalEyes.MonitorBO;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PointInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapBuidingInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapSegmentGridInfo;
import cn.ffcs.shequ.resident.service.IResidentService;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.event.IEventRelaItemService;
import cn.ffcs.shequ.zzgl.service.globalEyes.MonitorService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IPointInfoService;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.om.TaskReceive;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.zhsq.CoordinateInverseQuery.service.ICoordinateInverseQueryService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.base.local.service.IGridLogService.ActionType;
import cn.ffcs.zhsq.base.local.service.LogModule;
import cn.ffcs.zhsq.dispute.service.IDisputeMediationService;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposal4ExtraService;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.eventDuplication.service.IEventDuplicationService;
import cn.ffcs.zhsq.eventExpand.service.IEventDisposal4ExpandService;
import cn.ffcs.zhsq.eventExpand.service.IEventLabelRelaService;
import cn.ffcs.zhsq.eventExpand.service.IEventLabelService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.mybatis.domain.CoordinateInverseQuery.CoordinateInverseQueryGridInfo;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediation;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
import cn.ffcs.zhsq.reportFocus.reportFeedback.IReportFeedbackService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.ArrayUtil;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.JXLExcelUtil;
import cn.ffcs.zhsq.utils.JsonUtils;
import cn.ffcs.zhsq.utils.domain.App;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/zhsq/event/eventDisposalController")
public class EventDisposalController extends ZZBaseController {
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventDisposal4ExpandService eventDisposalExpandService;
	
	@Autowired
	private IEventDisposal4ExtraService eventDisposal4ExtraService;
	
	@Autowired
	private IEventDisposalDockingService eventDisposalDockingService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private ICoordinateInverseQueryService coordinateInverseQueryService;
	
	//多图片上传
	@Autowired
	private IAttachmentService attachmentService;
	
	//工作流调用接口
	@Autowired
	private HessianFlowService hessianFlowService;
	
	//组织域信息接口 
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	//责任点位
	@Autowired
	private IPointInfoService pointInfoService;
	
	//功能配置
	@Autowired
	private IFunConfigurationService configurationService;
	
	@Autowired
	private IArcgisInfoService arcgisInfoService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IResidentService residentService;

	@Autowired
	private IEventRelaItemService eventRelaItemService;
	
	@Autowired
	private IEventReportRecordService eventReportRecordService;
	
	@Autowired
	private cn.ffcs.shequ.zzgl.service.event.IEventDisposalService oldEventDisposalService;
	
	@Autowired
	private IResMarkerService resGisMarkerService;
	
	@Autowired
	private ITimeApplicationService timeApplicationService;
	
	@Autowired
	private IEvent4WorkflowService event4WorkflowService;
	
	@Autowired
	private PartyIndividualService partyIndividualService;

	@Autowired
	private IInvolvedPeopleService involvedPeopleService;
	
	@Autowired
	private IEventExtendService eventExtendService;
	
	@Autowired
	private IEventDuplicationService eventDuplicationService;
	
	@Autowired
	private MonitorService monitorService;
 
	
	@Autowired
	private UserScoreService userScoreService;//积分添加接口
	
	@Autowired
	private UserManageOutService userManageService;
	//信息域信息接口 
	//@Autowired
	//private OrgEntityInfoOutService orgEntityInfoOutService;
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IDisputeMediationService disputeMediationService;

	@Autowired
	private LogService userLogService; //日志服务
	
	@Autowired
	private IEventLabelService eventLabelService;
	
	@Autowired
	private IEventLabelRelaService eventLabelRelaService;

	@Autowired
	IReportFeedbackService reportFeedbackService;

	@Autowired
	private IntegralRuleService integralRuleService;
	// -- 文件上传目录
	//private static final String FILE_UPLOAD_EXTERIOR = "attachment";
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 附件所属模块类型
	 */
	private final static String attachmentType = "dispute_attachment_type";
	
	@ResponseBody
	@RequestMapping(value = "/remindDate", method = RequestMethod.POST)
	public EUDGPagination remindDate(
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "eventId") Long eventId) {
		if (page <= 0)
			page = 1;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eventId", eventId);
		EUDGPagination result = eventDisposalService.findSupervisePagination(page, rows, params);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/checkWorkFlow", method = RequestMethod.POST)
	public Boolean checkWorkFlow(
			@RequestParam(value = "taskId") String taskId) {
		return hessianFlowService.isCurTask(taskId);
	}
	
	/**
	 * 催办跳转
	 * @param session
	 * @param taskId
	 * @param remarks
	 * @param instanceId
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toRemind")
	public String toRemind(HttpSession session, 
			@RequestParam(value = "userIds") String userIds,
			@RequestParam(value = "userNames") String userNames,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam(value = "taskId") Long taskId,
			ModelMap map) {
		map.addAttribute("userIds",userIds);
		map.addAttribute("userNames",userNames);
		map.addAttribute("eventId",eventId);
		map.addAttribute("instanceId",instanceId);
		map.addAttribute("taskId",taskId);
		return "/zzgl/event/list_remind.ftl";
	}
	
	/**
	 * 督办跳转
	 * @param session
	 * @param eventId
	 * @param instanceId
	 * @param taskId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddRemind")
	public String toAddRemind(HttpSession session,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam(value = "page", required = false) String page,
			@RequestParam(value = "color", required = false) String color,
			@RequestParam(value = "taskId", required = false) Long taskId,
			@RequestParam(value = "backUrl", required = false) String backUrl,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean isShowSendMsg = true;
		String isShowSendMsgStr = null, userOrgCode = userInfo.getOrgCode();
		if(taskId==null || taskId.equals(-1L)){
			taskId = eventDisposalWorkflowService.curNodeTaskId(instanceId);
		}
		
		isShowSendMsgStr = configurationService.turnCodeToValue(ConstantValue.SEND_MSG_AND_SMS_CFG, ConstantValue.IS_SHOW_SEND_MSG, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);

		//是否显示督办类型选项
		boolean isShowSupervisionType = false;
		String isShowSupervisionTypeStr=null;
		isShowSupervisionTypeStr=configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.IS_SHOW_SUPERVISION_TYPE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(isShowSupervisionTypeStr)) {
			isShowSupervisionType = Boolean.valueOf(isShowSupervisionTypeStr);
		}

		if(StringUtils.isNotBlank(isShowSendMsgStr)) {
			isShowSendMsg = Boolean.valueOf(isShowSendMsgStr);
		}
		
		map.addAttribute("isShowSendMsg", isShowSendMsg);
		map.addAttribute("isShowSupervisionType", isShowSupervisionType);
		map.addAttribute("eventId",eventId);
		map.addAttribute("instanceId",instanceId);
		map.addAttribute("taskId",taskId);
		if(StringUtils.isNotBlank(page)) {
			map.addAttribute("color",color);
			if (StringUtils.isBlank(backUrl)) {
				String biDomain = App.BI.getDomain(session);
				backUrl = biDomain + "/report/nanan/leader/eventProgress.jhtml";
			}
			map.addAttribute("backUrl",backUrl);
			return "/zzgl/event/add_remind"+page+".ftl";
		}
		return "/zzgl/event/add_remind.ftl";
	}
	
	/**
	 * 督办
	 * @param session
	 * @param map
	 * @param eventId			事件id
	 * @param taskId			任务id
	 * @param remarks			督办意见
	 * @param instanceId		实例id
	 * @param supervisionType	督办类型
	 * @param ootherMobileNums	额外发送短信号码
	 * @param smsContent		短信内容
	 * @param params			额外参数
	 * 			formId			事件id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addRemind", method = RequestMethod.POST)
	public Map<String, Object> addRemind(
			HttpSession session,ModelMap map,
			@RequestParam(value = "eventId", required = false) Long eventId,
			@RequestParam(value = "taskId", required = false) Long taskId,
			@RequestParam(value = "remarks") String remarks,//催办意见
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam(value = "supervisionType", required = false) String supervisionType,
			@RequestParam(value = "otherMobileNums", required = false) String otherMobileNums,
			@RequestParam(value = "smsContent", required = false) String smsContent,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean result = false;
		params = params == null ? new HashMap<String, Object>() : params;
		
		params.put("category",IEventDisposalWorkflowService.REMIND_CATEGORY.SUPERVISE.getCategory());
		
		try {
			result = eventDisposalWorkflowService.addRemind(instanceId,userInfo,params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap.putAll(params);
		resultMap.put("result", result);
		
		return resultMap;
	}
	
	/**
	 * 跳转催办页面
	 * @param session
	 * @param eventId
	 * @param instanceId
	 * @param taskId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddUrge")
	public String toAddUrge(HttpSession session,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam(value = "taskId", required=false) Long taskId,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean isShowSendMsg = true;
		String isShowSendMsgStr = null, userOrgCode = userInfo.getOrgCode();
		if(taskId==null || taskId.equals(-1L)){
			taskId = eventDisposalWorkflowService.curNodeTaskId(instanceId);
		}
		
		isShowSendMsg = true;
		isShowSendMsgStr = configurationService.turnCodeToValue(ConstantValue.SEND_MSG_AND_SMS_CFG, ConstantValue.IS_SHOW_SEND_MSG, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
		
		if(StringUtils.isNotBlank(isShowSendMsgStr)) {
			isShowSendMsg = Boolean.valueOf(isShowSendMsgStr);
		}
		
		map.addAttribute("isShowSendMsg", isShowSendMsg);
		map.addAttribute("eventId",eventId);
		map.addAttribute("instanceId",instanceId);
		map.addAttribute("taskId",taskId);
		
		return "/zzgl/event/add_urge.ftl";
	}
	
	/**
	 * 催办
	 * @param session
	 * @param eventId
	 * @param taskId
	 * @param remarks
	 * @param instanceId
	 * @param otherMobileNums 其他电话号码
	 * @param smsContent 短信内容
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addUrge", method = RequestMethod.POST)
	public Map<String, Object> addUrge(
			HttpSession session,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "taskId") Long taskId,
			@RequestParam(value = "remarks") String remarks,//催办意见
			@RequestParam(value = "instanceId") Long instanceId,
			@RequestParam(value = "otherMobileNums", required = false) String otherMobileNums,
			@RequestParam(value = "smsContent", required = false) String smsContent,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean result = false;
		params = params == null ? new HashMap<>() : params;
		
		params.put("curTaskId",taskId);
		params.put("category",IEventDisposalWorkflowService.REMIND_CATEGORY.URGE.getCategory());
		params.put("remindUserId",userInfo.getUserId());
		params.put("remindUserPartyName",userInfo.getPartyName());
		params.put("remindRemark",remarks);

		try {
			result = eventDisposalWorkflowService.addRemind(instanceId, userInfo, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 添加关注
	 * @param session
	 * @param instanceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addAttention", method = RequestMethod.POST)
	public Boolean addAttention(
			HttpSession session,
			@RequestParam(value = "eventId") Long eventId)
	{
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		return hessianFlowService.addAttention(eventId, ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID, userInfo.getUserId(), userInfo.getUserName(), new Date());
	}
	
	/**
	 * 取消关注
	 * @param session
	 * @param instanceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/canclAttention", method = RequestMethod.POST)
	public Boolean canclAttention(
			HttpSession session,
			@RequestParam(value = "eventId") Long eventId) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		return hessianFlowService.cancleAttention(eventId, ConstantValue.EVENT_WORKFLOW_FORM_TYPE_ID, userInfo.getUserId());
	}
	
	/**
	 * 获取旧事件的ID
	 * @param session
	 * @param eventId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOldEventId", method = RequestMethod.POST)
	public EventRelaItem getOldEventId(
			HttpSession session,
			@RequestParam(value = "eventId") Integer eventId) {
		List<EventRelaItem> eventRelaItems = eventRelaItemService.findByEventId(eventId);
		return eventRelaItems.size() > 0 ? eventRelaItems.get(0) : null;
	}
	
	/**
	 * 事件列表展示
	 * @param session
	 * @param eventType			列表类型
	 * @param model
	 * @param trigger				事件类别过滤功能配置触发条件，功能配置编码为：types
	 * @param eventAttrTrigger	事件基本属性过滤触发条件，功能配置编码为：EVENT_ATTRIBUTE
	 * @param extraParams		额外属性设置，格式为json字符串
	 * 							extraParams={isEnableDefaultCreatTime:true,exhibitParam:{isEditEventCreateTime:false,isShowEventType:false}}
	 * 							exhibitParam为列表页面元素展示、可编辑性等设置，格式为json字符串
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/listEvent")
	public String listEvent(HttpSession session,
			@RequestParam(value = "t") String eventType,
			@RequestParam(value = "model") String model,
			@RequestParam(value = "trigger", required = false) String trigger,
			@RequestParam(value = "eventAttrTrigger", required = false) String eventAttrTrigger,
			@RequestParam(value = "extraParams", required = false) String extraParams,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Map<String, Object> extraParamsMap = null;//事件额外参数设置
		String userOrgCode = userInfo.getOrgCode(),
			   privateFolderName = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.PRIVATE_FOLDER_NAME4_EVENT + "List", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY),
			   forwardUrl = "/zzgl/event" + privateFolderName + "/list_event.ftl",
			   eventModel = "c",
			   typesForList = null;
		MixedGridInfo gridInfo = null;

		//设置额外参数
		if(StringUtils.isNotBlank(extraParams)){
			try{
				extraParamsMap = JsonUtils.json2Map(extraParams);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		extraParamsMap = extraParamsMap == null ? new HashMap<String, Object>() : extraParamsMap;
		
		if(CommonFunctions.isNotBlank(extraParamsMap, "eInfoOrgCodeForOut")) {
			byte[] decodedData = Base64.decode(extraParamsMap.get("eInfoOrgCodeForOut").toString());
			String infoOrgCode = new String(decodedData);
			gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(infoOrgCode);
		} else if(CommonFunctions.isNotBlank(extraParamsMap, "startGridId4Out")) {
			Long startGridId = null;
			
			try {
				startGridId = Long.valueOf(extraParamsMap.get("startGridId4Out").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
			
			if(startGridId != null && startGridId > 0) {
				gridInfo = mixedGridInfoService.findMixedGridInfoById(startGridId, false);
				extraParamsMap.put("startGridId4Out", startGridId);
			}
		}
		
		if(CommonFunctions.isNotBlank(extraParamsMap, "typesForList")) {
			typesForList = extraParamsMap.get("typesForList").toString();
		}
		
		//获取网格树展示位置配置
		String treStyle = null;
		
		if(CommonFunctions.isNotBlank(extraParamsMap, "treStyle")) {
			treStyle = extraParamsMap.get("treStyle").toString();
		} else {
			treStyle = configurationService.changeCodeToValue(
					ConstantValue.GRID_CONFIGURATION, ConstantValue.GRID, IFunConfigurationService.CFG_TYPE_FACT_VAL,
					userInfo.getOrgCode());
		}
		
		map.addAttribute("treStyle",treStyle);
		
		if(gridInfo != null) {
			String infoOrgCode = gridInfo.getInfoOrgCode(),
					  gridName = gridInfo.getGridName();
			
			map.addAttribute("defaultInfoOrgCode", infoOrgCode);
			map.addAttribute("startGridId", gridInfo.getGridId());
			map.addAttribute("orgCode", infoOrgCode);
			map.addAttribute("infoOrgCode", new String(Base64.encode(infoOrgCode.getBytes())));
			map.addAttribute("gridName", gridName);
			map.addAttribute("startGridCodeName", gridInfo.getGridCode() + " - " + gridName);
			map.addAttribute("startGridLevel", gridInfo.getGridLevel());
		} else {
			map.addAttribute("defaultInfoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
			map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
			map.addAttribute("orgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
			map.addAttribute("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_ENCRYPT_INFO_ORG_CODE));
			map.addAttribute("gridName", defaultGridInfo.get(KEY_START_GRID_NAME));
			map.addAttribute("startGridCodeName", defaultGridInfo.get(KEY_START_GRID_CODE_NAME));
			map.addAttribute("startGridLevel", defaultGridInfo.get(KEY_START_GRID_LEVEL));
		}
		
		List<BaseDataDict> statusDC = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.STATUS_PCODE, userOrgCode);
		
		if(statusDC != null) {//合并字典名称相同的字典项
			LinkedHashMap<String, BaseDataDict> dictMap = new LinkedHashMap<String, BaseDataDict>();
			String dictName = null;
			BaseDataDict dictTmp = null;
			List<BaseDataDict> dictList = new ArrayList<BaseDataDict>();
			
			for(BaseDataDict dict : statusDC) {
				dictName = dict.getDictName();
				
				if(dictMap.containsKey(dictName)) {
					dictTmp = dictMap.get(dictName);
					dictTmp.setDictGeneralCode(dictTmp.getDictGeneralCode() + "," + dict.getDictGeneralCode());
					
					dictMap.put(dictName, dictTmp);
				} else {
					dictMap.put(dictName, dict);
				}
			}
			
			for(String key : dictMap.keySet()) {
				dictList.add(dictMap.get(key));
			}
			
			map.addAttribute("statusDC", dictList);
		}

		int isNoShow = 0;
		String itemCodes = ConstantValue.EVENT_NO_SHOW;
		String[] itemCodeArr = itemCodes.split(",");
		List<String> itemCodeList = Arrays.asList(itemCodeArr);
		for (int i = 0; i < itemCodeList.size(); i++) {
			if(userOrgCode.startsWith(itemCodeList.get(i))){
				isNoShow = 1;
				break;
			}
		}
		String[] eventTypeOrder = {"todo,待办事项","my,我发起的","done,已办事项","history,归档事项","draft,草稿事项","all,辖区所有","toRemind,督办事件"};
		map.addAttribute("isNoShow", isNoShow);
		map.addAttribute("eventType", eventType);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));

		//事件列表中可展示的事件类别，空则展示所有
		boolean isRemoveTypes = false;
		
		if(StringUtils.isNotBlank(trigger) && StringUtils.isBlank(typesForList)) {
			typesForList = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode);
			
			if(StringUtils.isBlank(typesForList)) {
				isRemoveTypes = true;
				typesForList = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode);
			}
		}
		
		if(StringUtils.isNotBlank(typesForList)) {
			typesForList = typesForList.trim();
			
			String[] typesTemp = typesForList.split(",");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
			param.put("orgCode", userOrgCode);
			
			BaseDataDict dataDict = null;
			StringBuffer typesDictCode = new StringBuffer(""), typeBuffer = new StringBuffer("");
			
			for(int index = 0, len = typesTemp.length; index < len; index++){
				String typeTemp = typesTemp[index];
				String dictCode = "";
				
				if(StringUtils.isNotBlank(typeTemp)){
					param.put("dictGeneralCode", typeTemp);
					dataDict = baseDictionaryService.findByCodes(param);
					
					if(dataDict != null) {
						dictCode = dataDict.getDictCode();
						if(StringUtils.isNotBlank(dictCode)){
							typesDictCode.append(",").append(dictCode);
							typeBuffer.append(",").append(typeTemp);
						}
					}
				}
			}
			
			if(typesDictCode.length() > 0){
				typesDictCode = new StringBuffer(typesDictCode.substring(1));
				map.addAttribute("typesDictCode", "'" + typesDictCode.toString().replaceAll(",", "','") + "'");
				if(typeBuffer.length() > 0) {
					typeBuffer = new StringBuffer(typeBuffer.substring(1));
				}
				map.addAttribute("isRemoveTypes", isRemoveTypes);
			}
			
			extraParamsMap.put("typesForList", typeBuffer.toString());
			map.addAttribute("trigger", trigger);//事件类型筛选触发条件
		}
		
		//事件列表是否显示事件标签选项
		String showLabelInput = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.SHOW_LABEL_INPUT, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(showLabelInput)) {
			
			try {
				Map<String,Object> searchLabelParams=new HashMap<String,Object>();
				searchLabelParams.put("labelModel", showLabelInput);
				List<Map<String, Object>> eventLabelList = eventLabelService.searchListByParam(searchLabelParams);
				map.addAttribute("eventLabelDict", eventLabelList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		map.addAllAttributes(dictItemHandle(extraParamsMap, userOrgCode));
		
		//设置事件属性过滤触发条件
		if(StringUtils.isNotBlank(eventAttrTrigger)){
			map.addAttribute("eventAttrTrigger", eventAttrTrigger);
		}
		
		//辖区内归档、视图归档、辖区所有列表增添入格事项、入格部门查询条件
		if("history".equals(eventType) || "all".equals(eventType) || "view".equals(eventType)) {
			//是否设置默认查询采集时间近一个月的事件
			if(CommonFunctions.isNotBlank(extraParamsMap, "isEnableDefaultCreatTime")) {
				boolean isEnableDefaultCreatTime = Boolean.valueOf(extraParamsMap.get("isEnableDefaultCreatTime").toString());
				
				if(isEnableDefaultCreatTime) {
					if(CommonFunctions.isBlank(extraParamsMap, "createTimeStart")) {
						extraParamsMap.put("createTimeStart", DateUtils.getMonthFirstDay());
					}
					if(CommonFunctions.isBlank(extraParamsMap, "createTimeEnd")) {
						extraParamsMap.put("createTimeEnd", DateUtils.getToday());
					}
					if(CommonFunctions.isBlank(extraParamsMap, "createDateTimeEnd")) {
						extraParamsMap.put("createDateTimeEnd", DateUtils.getNow());
					}
				}
			}
			
		}

		if("all".equals(eventType)) {//辖区所有
			boolean isShowPlatformSelect = false,
					isShowOrgSelect = false;

			//对接平台来源下拉框
			String isShowPlatformSelectStr = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.IS_SHOW_PLATFORM_SELECT, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
			if(StringUtils.isNotBlank(isShowPlatformSelectStr)) {
				isShowPlatformSelect = Boolean.valueOf(isShowPlatformSelectStr);
			}

			//经办/待办环节下拉框
			String isShowOrgSelectStr = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.IS_SHOW_ORG_SELECT, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
			if(StringUtils.isNotBlank(isShowOrgSelectStr)) {
				isShowOrgSelect = Boolean.valueOf(isShowOrgSelectStr);
			}
           
			map.addAttribute("isShowPlatformSelect",isShowPlatformSelect);
			map.addAttribute("isShowOrgSelect",isShowOrgSelect);
		}

		//设置列表默认排序
		map.addAttribute("defaultOrderBy", configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.DEFAULT_ORDER_BY + "_" + eventType, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0));
		//获取可进行远程排序的网格层级
		map.addAttribute("remotSortGridLevel", configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.REMOT_SORT_GRID_LEVEL, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0));
		
		if(extraParamsMap != null && !extraParamsMap.isEmpty()){
			extraParams = JsonUtils.mapToJson(extraParamsMap);
			map.addAttribute("extraParams", extraParams);
			map.addAllAttributes(extraParamsMap);
		}
		
		if(model.equals("c")) {
			eventModel = "l";
			
			for(int i = 0; i<eventTypeOrder.length; i++){
				String eType = eventTypeOrder[i].split(",")[0];
				if(eType.equals(eventType)){
					int prev = i - 1;
					int next = i + 1;
					if(prev == -1){
						prev = eventTypeOrder.length-1;
					}
					if(next == eventTypeOrder.length){
						next = 0;
					}
					map.addAttribute("prevEventType", eventTypeOrder[prev].split(",")[0]);
					map.addAttribute("nextEventType", eventTypeOrder[next].split(",")[0]);
					map.addAttribute("middEventType", eventTypeOrder[i].split(",")[0]);
					map.addAttribute("middEventName", eventTypeOrder[i].split(",")[1]);
					map.addAttribute("prevEventName", eventTypeOrder[prev].split(",")[1]);
					map.addAttribute("nextEventName", eventTypeOrder[next].split(",")[1]);
				}
			}
			
			forwardUrl = "/zzgl/event" + privateFolderName + "/column_event.ftl";
		}else if(model.equals("e")){//导出页面
			map.addAttribute("model", "e");
			eventModel="e";
			privateFolderName = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.PRIVATE_FOLDER_NAME4_EVENT 
					+ "Export", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);
			
			map.addAttribute("privateFolderName", privateFolderName);
			forwardUrl = "/zzgl/event" + privateFolderName + "/excel_event.ftl";
		}else {//modul为l
			if("all".equals(eventType)) {
				boolean isShowAttentionBtn = true;
				String isShowAttentionBtnStr = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.IS_SHOW_ATTENTION_BTN, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
				
				if(StringUtils.isNotBlank(isShowAttentionBtnStr)) {
					isShowAttentionBtn = Boolean.valueOf(isShowAttentionBtnStr);
				}
				
				map.addAttribute("isShowAttentionBtn", isShowAttentionBtn);
			}
		}
		
		map.addAttribute("model", eventModel);
		
		return forwardUrl;
	}

	/**
	 * 报表弹出事件列表页面
	 * @param session
	 * @param listType	列表类型
	 * 						1 待办；2 经办；3 辖区所有(支持办理人员查询)；
	 * 						4 归档列表；5 辖区所有；9 辖区所有(展示当前环节)；
	 * 						6 我发起的；7 我的关注；8 辖区内需要督办；
	 * @param params	额外查询条件
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toListEvent4Report")
	public String toListEvent4Report(HttpSession session,
			@RequestParam(value = "listType") int listType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		String forwardUrl = "/zzgl/event/yanduqu/list_event_report.ftl";
		
		map.addAttribute("outerParamJson", JsonUtils.mapToJson(params));
		
		return forwardUrl;
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
			@RequestParam(value = "eventType", required=false, defaultValue="") String eventType,
			@ModelAttribute(value = "event") EventDisposal event,
			@RequestParam(value = "generalSearch", required = false) String generalSearch,
			@RequestParam(value = "startHappenTime", required=false) String startHappenTime,
			@RequestParam(value = "endHappenTime", required=false) String endHappenTime,
			@RequestParam(value = "statuss[]", required=false) String statuss[],
			@RequestParam(value = "handleStatus", required=false) String handleStatus,
			@RequestParam(value = "handleStatuss", required=false) String handleStatuss,
			@RequestParam(value = "types", required=false) String types,
			@RequestParam(value = "eventStatus", required = false) String eventStatus,
			@RequestParam(value = "typesForList", required = false) String typesForList,
			@RequestParam(value = "eventAttrTrigger", required = false) String eventAttrTrigger,
			@RequestParam Map<String, Object> paramMap) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		int listType = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(paramMap);
		
		params.put("userId", userInfo.getUserId());
		params.put("orgId", userInfo.getOrgId());
		params.put("orgCode", userInfo.getOrgCode());

		//保存查询日志信息
		saveLog(request,null, "Q", userInfo, null, null);

		if(!StringUtils.isBlank(types)){
			types = types.trim();
			//params.put("types", types);
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
		
		if (!StringUtils.isBlank(event.getContent())){
			params.put("content", event.getContent());
		}
		
		if(StringUtils.isNotBlank(handleStatuss) && StringUtils.isNotBlank(handleStatuss.trim())){
			params.put("handleStatuss", handleStatuss.trim());
		}
		if(StringUtils.isNotBlank(handleStatus) && StringUtils.isNotBlank(handleStatus.trim())){
			params.put("handleStatus", handleStatus.trim());
		}
		if(statuss != null){
			eventStatus = ArrayUtil.ArrayToString(statuss);
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
		if(!StringUtils.isBlank(generalSearch)){
			params.put("generalSearch", generalSearch);
		}
		if(!StringUtils.isBlank(startHappenTime)){
			params.put("startHappenTime", startHappenTime);
		}
		if(!StringUtils.isBlank(endHappenTime)){
			params.put("endHappenTime", endHappenTime);
		}
		if(StringUtils.isNotBlank(eventAttrTrigger)){
			params.put("eventAttrTrigger", eventAttrTrigger);
			params.put("eventAttrOrgCode", userInfo.getOrgCode());
		}
		if(StringUtils.isNotBlank(event.getTaskReceivedStaus())) {
			params.put("taskReceivedStaus", event.getTaskReceivedStaus());
		}
//		params.put("statuss", statuss);
		params.put("statusList", eventStatus);
		
		if(CommonFunctions.isNotBlank(paramMap, "listType")) {
			try {
				listType = Integer.valueOf(paramMap.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if(listType == 1 || eventType.equals("todo")) {//--待办
			cn.ffcs.system.publicUtil.EUDGPagination pagination = new cn.ffcs.system.publicUtil.EUDGPagination();
			
			params.put("listType", 1);//待办列表
			
			try {
				pagination = event4WorkflowService.findEventListPagination(page, rows, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			EUDGPagination paginationTarget = new EUDGPagination();
			
			paginationTarget.setTotal(pagination.getTotal());
			paginationTarget.setRows(pagination.getRows());
			
			return paginationTarget;
		}else if(listType == 6 || eventType.equals("my")){//--我发起的
			cn.ffcs.system.publicUtil.EUDGPagination pagination = new cn.ffcs.system.publicUtil.EUDGPagination();
			
			params.put("listType", 6);//我发起的列表
			
			try {
				pagination = event4WorkflowService.findEventListPagination(page, rows, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			EUDGPagination paginationTarget = new EUDGPagination();
			
			paginationTarget.setTotal(pagination.getTotal());
			paginationTarget.setRows(pagination.getRows());
			
			return paginationTarget;
		}else if(eventType.equals("todoNoEval")){//--待办没有评价
			cn.ffcs.system.publicUtil.EUDGPagination pagination = new cn.ffcs.system.publicUtil.EUDGPagination();
			
			params.put("statusList", "00,01,02");
			params.put("listType", 1);//待办列表
			
			try {
				pagination = event4WorkflowService.findEventListPagination(page, rows, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			EUDGPagination paginationTarget = new EUDGPagination();
			
			paginationTarget.setTotal(pagination.getTotal());
			paginationTarget.setRows(pagination.getRows());
			
			return paginationTarget;
		}else if(eventType.equals("todoOnlyEval")){//--待办只有评价
			cn.ffcs.system.publicUtil.EUDGPagination pagination = new cn.ffcs.system.publicUtil.EUDGPagination();
			
			params.put("statusList", "03");
			params.put("listType", 1);//待办列表
			
			try {
				pagination = event4WorkflowService.findEventListPagination(page, rows, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			EUDGPagination paginationTarget = new EUDGPagination();
			
			paginationTarget.setTotal(pagination.getTotal());
			paginationTarget.setRows(pagination.getRows());
			
			return paginationTarget;
		}else if(listType == 2 || eventType.equals("done")){//--经办
			cn.ffcs.system.publicUtil.EUDGPagination pagination = new cn.ffcs.system.publicUtil.EUDGPagination();
			
			params.put("listType", 2);//经办列表
			
			try {
				pagination = event4WorkflowService.findEventListPagination(page, rows, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			EUDGPagination paginationTarget = new EUDGPagination();
			
			paginationTarget.setTotal(pagination.getTotal());
			paginationTarget.setRows(pagination.getRows());
			
			return paginationTarget;
		}else if(eventType.equals("myhistory")){//--我的归档
			return eventDisposalService.findHistoryEventWorkFlowPagination(page, rows, params);
		}else if(listType == 4 || eventType.equals("history")){//--辖区归档
			cn.ffcs.system.publicUtil.EUDGPagination pagination = new cn.ffcs.system.publicUtil.EUDGPagination();
			
			params.put("listType", 4);//辖区归档列表
			
			try {
				pagination = event4WorkflowService.findEventListPagination(page, rows, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			EUDGPagination paginationTarget = new EUDGPagination();
			
			paginationTarget.setTotal(pagination.getTotal());
			paginationTarget.setRows(pagination.getRows());
			
			return paginationTarget;
		}else if(eventType.equals("historynoworkflow")){//--辖区归档不带工作流
			params.put("statusList", null);//清空状态列表
			params.put("status", ConstantValue.EVENT_STATUS_END);
			return eventDisposalService.findEventDisposalPagination(page, rows, params);
//			return eventDisposalService.findAllFileEventWorkFlowPagination(page, rows, params);
		}else if(eventType.equals("view")){//--视图
			params.put("statusList", null);//清空状态列表
//			params.put("status", ConstantValue.EVENT_STATUS_END);
			return eventDisposalService.findHistoryViewPagination(page, rows, params);
//			return eventDisposalService.findAllFileEventWorkFlowPagination(page, rows, params);
		}else if(eventType.equals("draft")){//--草稿
			params.put("statusList", null);//清空状态列表
			params.put("status", ConstantValue.EVENT_STATUS_DRAFT);
			params.put("creatorId", userInfo.getUserId());//过滤创建人员
			EUDGPagination findEventListPagination=new EUDGPagination();
			try {
				findEventListPagination = event4WorkflowService.findEventListPagination(page, rows, params);
			} catch (ZhsqEventException e) {
				e.printStackTrace();
			}
			return findEventListPagination;
		}else if(eventType.equals("had")){//--已办（结案+历史）
			params.put("creatorId", userInfo.getUserId());//过滤创建人员
			List<String> statusList = new ArrayList<String>();
			statusList.add(ConstantValue.EVENT_STATUS_ARCHIVE);//结案
			statusList.add(ConstantValue.EVENT_STATUS_END);//归档
			params.put("statusList", statusList);//过滤创建人员
			return eventDisposalService.findEventDisposalPagination(page, rows, params);
		}else if(listType == 5 || eventType.equals("all")){//--辖区所有
			cn.ffcs.system.publicUtil.EUDGPagination pagination = new cn.ffcs.system.publicUtil.EUDGPagination();
			
			params.put("listType", 5);//辖区所有列表
			
			try {
				pagination = event4WorkflowService.findEventListPagination(page, rows, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			EUDGPagination paginationTarget = new EUDGPagination();
			
			paginationTarget.setTotal(pagination.getTotal());
			paginationTarget.setRows(pagination.getRows());
			
			return paginationTarget;
		}else if(listType == 7 || eventType.equals("attention")){//--辖区所有---关注
			cn.ffcs.system.publicUtil.EUDGPagination pagination = new cn.ffcs.system.publicUtil.EUDGPagination();
			
			params.put("listType", 7);//辖区所有关注列表
			
			try {
				pagination = event4WorkflowService.findEventListPagination(page, rows, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			EUDGPagination paginationTarget = new EUDGPagination();
			
			paginationTarget.setTotal(pagination.getTotal());
			paginationTarget.setRows(pagination.getRows());
			
			return paginationTarget;
		}else if(eventType.equals("remind")){//--被督办列表
			return eventDisposalService.findRemindEventWorkFlowPagination(page, rows, params);
		}else if(listType == 8 || eventType.equals("toRemind")){//--辖区需要督办列表
			cn.ffcs.system.publicUtil.EUDGPagination pagination = new cn.ffcs.system.publicUtil.EUDGPagination();
			
			params.put("listType", 8);//辖区所有需要督办列表
			
			try {
				pagination = event4WorkflowService.findEventListPagination(page, rows, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			EUDGPagination paginationTarget = new EUDGPagination();
			
			paginationTarget.setTotal(pagination.getTotal());
			paginationTarget.setRows(pagination.getRows());
			
			return paginationTarget;
		}else if(eventType.equals("donehistory")){//-- 经办历史
			return eventDisposalService.findDoneHistoryEventWorkFlowPagination(page, rows, params);
		}
		
		EUDGPagination paginationTarget = new EUDGPagination();
		
		paginationTarget.setTotal(0);
		paginationTarget.setRows(new ArrayList<Map<String, Object>>());
		
		return paginationTarget;
	}
	
	/**
	 * 事件基本属性更新页面
	 * @param session
	 * @param eventId	事件id
	 * @param params	额外参数
	 * 			zonePath	区域路径，后续如果有增多，转换为功能配置取值，功能配置编码为EVENT_EXHIBITION_ATTRIBUTE->folderName4EventAttrEdit
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toEditEventAttr")
	public String toEditEventAttr(HttpSession session, 
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		EventDisposal event = null;
		String zonePath = "";
		
		if(eventId > 0) {
			event = eventDisposalService.findEventByIdSimple(eventId);
		}
		
		if(event == null) {
			event = new EventDisposal();
		} else if(CommonFunctions.isNotBlank(params, "zonePath")) {
			zonePath = params.get("zonePath").toString();
			
			if(!zonePath.startsWith("/")) {
				zonePath = "/" + zonePath;
			}
		}
		
		map.addAttribute("event", event);
		
		return "/zzgl/event" + zonePath + "/eventAttrEdit/event_attr_edit.ftl";
	}
	
	/**
	 * 新增事件信息页面
	 * 
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddEvent")
	public String toAddEvent(HttpSession session, 
			@RequestParam(value = "eventId", required = false) Long eventId,
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		EventDisposal event = null;
		
		if(eventId!=null && !eventId.equals(-1L)){
			event = eventDisposalService.findEventById(eventId, userInfo.getOrgCode());
			//获取事件额外信息
			this.getEventExtendInfo(eventId, map, userInfo);
			
		}else{
			event = new EventDisposal();
			
			String gridId = defaultGridInfo.get(KEY_START_GRID_ID).toString();
			String gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString();
			String gridCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
			String eventDefaultTypeName = "";
			String eventDefaultType = ConstantValue.EVENT_DEFAULT_TYPE;
			
			map.addAttribute("startGridId", gridId);
			map.addAttribute("gridCode",gridCode);
			
			event.setGridId(Long.valueOf(gridId));
			event.setGridName(gridName.trim());
			event.setGridCode(gridCode);
			
			if(StringUtils.isBlank(eventDefaultType)){
				eventDefaultType = ConstantValue.DEFAULT_SMALL_TYPE;
			}
			
			eventDefaultTypeName = baseDictionaryService.changeCodeToName(ConstantValue.BIG_TYPE_PCODE, eventDefaultType, userInfo.getOrgCode(), true);
			
			event.setTypeName(eventDefaultTypeName);//设置默认的事件类型
			event.setType(eventDefaultType);
			
			//获取事件初始额外信息
			String eventDefaultPatrolType = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.DEFAULT_PATROL_TYPE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(),IFunConfigurationService.CFG_ORG_TYPE_0);
			map.addAttribute("patrolType", eventDefaultPatrolType);
		}
		
		// 将限制：屏蔽越级上报功能，转成可配置
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
		map.addAttribute("isNoShow", isNoShow);
		
		map.addAttribute("event", event);
		
//		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(Long.valueOf(defaultGridInfo.get(KEY_START_GRID_ID).toString()), false);
//		String result = "";
//		if(gridInfo.getMapType()!=null && gridInfo.getMapType().equals("004")) {
//			result = "gis"; 
//		}
//		map.addAttribute("result", result);
		
		return "/zzgl/event/add_event.ftl";
	}
	
	/**
	 * 跳转事件新增页面
	 * @param session
	 * @param eventId			事件Id
	 * @param trigger			事件类别过滤触发条件，对应功能编码为types，设定默认值为空串，是为了防止未设置trigger时，其值会被设置为字符串null
	 * @param typesForList		可查看的事件类别，以英文逗号相连
	 * @param isRemoveTypes		是否移除trigger、typesForList对应的事件类别，true为移除
	 * @param params			额外参数
	 * 			triggerSource		信息来源过滤触发条件，对应功能编码为sources
	 * 			sourceForList		可查看的信息来源，以英文逗号相连
	 * 			isRemoveSource		是否移除triggerSource、typesForList对应的事件类别，true为移除
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddEventByType")
	public String toAddEventByType(HttpSession session, 
			@RequestParam(value = "eventId", required = false) Long eventId,
			@RequestParam(value = "trigger", required = false, defaultValue="") String trigger,
			@RequestParam(value = "typesForList", required = false) String typesForList,
			@RequestParam(value = "isRemoveTypes", required = false, defaultValue = "false") boolean isRemoveTypes,
			@RequestParam Map<String, Object> params,
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session), eventResultMap = null;
		EventDisposal event = null;
		List<OrgEntityInfoBO> orgEntityInfoList = null;
		//过滤信息来源相关参数
		String triggerSource = "", sourceForList = null, userOrgCode = userInfo.getOrgCode();
		boolean isRemoveSource=false;
		
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isNotBlank(params, "triggerSource")) {
			triggerSource=params.get("triggerSource").toString();
		}
		
		if(CommonFunctions.isNotBlank(params, "sourceForList")) {
			sourceForList=params.get("sourceForList").toString();
		}
		
		if(CommonFunctions.isNotBlank(params, "isRemoveSource")) {
			isRemoveSource=Boolean.valueOf(params.get("isRemoveSource").toString());
		}
		
		map.addAllAttributes(params);
		
		params.put("userOrgCode", userOrgCode);
		
		if(eventId != null && eventId > 0){
			try {
				eventResultMap = eventDisposalService.findEventByMap(eventId, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(eventResultMap != null && !eventResultMap.isEmpty()) {
				map.addAllAttributes(eventResultMap);
				
				if(CommonFunctions.isNotBlank(eventResultMap, "event")) {
					event = (EventDisposal) eventResultMap.get("event");
				}
			}
			
			if(event != null) {
				String eventInvolvedPeople = event.getInvolvedPersion();
				Long instanceId = -1L;
				
				if(StringUtils.isNotBlank(eventInvolvedPeople)) {
					map.addAttribute("involvedPeopleList", involvedPeopleService.findInvolvedPeopleListByBiz(eventId, InvolvedPeople.BIZ_TYPE.ZHSQ_EVENT.getBizType()));
				}
				
				instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
				if(instanceId != null && instanceId > 0) {
					map.addAttribute("instanceId", instanceId);
				}
				
				//获取事件额外信息
				this.getEventExtendInfo(eventId, map, userInfo);
			}
		} else {
			event = new EventDisposal();
			
			//获取事件初始额外信息
			String eventDefaultPatrolType = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.DEFAULT_PATROL_TYPE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(),IFunConfigurationService.CFG_ORG_TYPE_0);
			map.addAttribute("patrolType", eventDefaultPatrolType);
			
			String gridId = defaultGridInfo.get(KEY_START_GRID_ID).toString(),
				   gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString(),
				   gridCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString(),
				   eventDefaultTypeName = "",
				   eventDefaultType = "";
			       
			
			event.setHappenTimeStr(DateUtils.getNow());
			event.setGridId(Long.valueOf(gridId));
			event.setGridName(gridName.trim());
			event.setGridCode(gridCode);
			
			eventDefaultType = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger+ConstantValue.DEFAULT_TYPE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
			
			if(StringUtils.isNotBlank(trigger) && StringUtils.isBlank(typesForList)) {
				if(isRemoveTypes) {
					typesForList = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
				} else {
					typesForList = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
				}
			}
			
			if(StringUtils.isNotBlank(triggerSource) && StringUtils.isBlank(sourceForList)) {
				if(isRemoveSource) {
					sourceForList = configurationService.changeCodeToValue(ConstantValue.SOURCES, triggerSource+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
				} else {
					sourceForList = configurationService.changeCodeToValue(ConstantValue.SOURCES, triggerSource, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
				}
			}
			
			if(StringUtils.isNotBlank(eventDefaultType)) {
				eventDefaultTypeName = baseDictionaryService.changeCodeToName(ConstantValue.BIG_TYPE_PCODE, eventDefaultType, userInfo.getOrgCode(), true);
			}
			
			event.setTypeName(eventDefaultTypeName);//设置默认的事件类型
			event.setType(eventDefaultType);
			
			event.setContactUser(userInfo.getPartyName());
			event.setTel(userInfo.getVerifyMobile());
			
			try {
				eventDisposalExpandService.init4Event(event, userInfo, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(StringUtils.isNotBlank(typesForList)) {
			String[] typesTemp = typesForList.split(",");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
			param.put("orgCode", userInfo.getOrgCode());
			
			BaseDataDict dataDict = null;
			StringBuffer typesDictCode = new StringBuffer("");
			
			for(int index = 0, len = typesTemp.length; index < len; index++) {
				String typeTemp = typesTemp[index];
				String dictCode = "";
				
				if(StringUtils.isNotBlank(typeTemp)){
					param.put("dictGeneralCode", typeTemp);
					dataDict = baseDictionaryService.findByCodes(param);
					
					if(dataDict != null) {
						dictCode = dataDict.getDictCode();
						if(StringUtils.isNotBlank(dictCode)){
							typesDictCode.append(",").append(dictCode);
						}
					}
				}
			}
			
			if(typesDictCode.length() > 0) {
				map.addAttribute("typesDictCode", "'" + typesDictCode.substring(1).replaceAll(",", "','") + "'");
				map.addAttribute("typesForList", typesForList);
				map.addAttribute("isRemoveTypes", isRemoveTypes);
			}
		}
		
		if(StringUtils.isNotBlank(sourceForList)) {
			String[] sourceTemp = sourceForList.split(",");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("dictPcode", ConstantValue.SOURCE_PCODE);
			param.put("orgCode", userInfo.getOrgCode());
			
			BaseDataDict dataDict = null;
			StringBuffer sourceDictCode = new StringBuffer("");
			
			for(int index = 0, len = sourceTemp.length; index < len; index++) {
				String souTemp = sourceTemp[index];
				String dictCode = "";
				
				if(StringUtils.isNotBlank(souTemp)){
					param.put("dictGeneralCode", souTemp);
					dataDict = baseDictionaryService.findByCodes(param);
					
					if(dataDict != null) {
						dictCode = dataDict.getDictCode();
						if(StringUtils.isNotBlank(dictCode)){
							sourceDictCode.append(",").append(dictCode);
						}
					}
				}
			}
			
			if(sourceDictCode.length() > 0) {
				map.addAttribute("sourceDictCode", "'" + sourceDictCode.substring(1).replaceAll(",", "','") + "'");
				map.addAttribute("sourceForList", sourceForList);
				map.addAttribute("isRemoveSource", isRemoveSource);
			}
		}
		
		map.addAttribute("bigTypePcode", ConstantValue.BIG_TYPE_PCODE);
		map.addAttribute("urgencyDegreePcode", ConstantValue.URGENCY_DEGREE_PCODE);
		map.addAttribute("influenceDegreePcode", ConstantValue.INFLUENCE_DEGREE_PCODE);
		map.addAttribute("sourcePcode", ConstantValue.SOURCE_PCODE);
		map.addAttribute("involvedNumPcode", ConstantValue.INVOLVED_NUM_PCODE);
		
		map.addAttribute("event", event);
		
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		
		//获取当前登录账号下所管理的所有网格
		orgEntityInfoList = userInfo.getInfoOrgList();

		map.addAttribute("START_DIVISION_CODE", "-1");//标准地址库控件起始网格默认值
		
		String showLabelInput = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.SHOW_LABEL_INPUT, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(showLabelInput)) {
			
			try {
				Map<String,Object> searchLabelParams=new HashMap<String,Object>();
				searchLabelParams.put("labelModel", showLabelInput);
				List<Map<String, Object>> eventLabelList = eventLabelService.searchListByParam(searchLabelParams);
				map.addAttribute("eventLabelDict", eventLabelList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (null != orgEntityInfoList && orgEntityInfoList.size() > 0) {
			StringBuffer orgCodeSb = new StringBuffer("");
			String infoOrgCode = null;
			
			for (OrgEntityInfoBO orgEntityInfo : orgEntityInfoList) {
				infoOrgCode = orgEntityInfo.getOrgCode();
				
				if(StringUtils.isNotBlank(infoOrgCode)) {
					orgCodeSb.append(",").append(infoOrgCode);
				}
			}
			
			if(orgCodeSb.length() > 0) {
				map.addAttribute("START_DIVISION_CODE", orgCodeSb.substring(1));//标准地址库控件起始网格
			}
		}

		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(Long.valueOf(defaultGridInfo.get(KEY_START_GRID_ID).toString()), false);
		String result = "";
		if(gridInfo != null && gridInfo.getMapType()!=null && gridInfo.getMapType().equals("004")) {
			result = "gis"; 
		}
		map.addAttribute("result", result);
		map.addAttribute("EVENT_ATTACHMENT_TYPE", ConstantValue.EVENT_ATTACHMENT_TYPE);//上传附件的类型
		
		String isUploadHandlingPic = configurationService.turnCodeToValue(ConstantValue.IS_UPLOAD_HANDLING_PIC, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUploadHandlingPic", Boolean.valueOf(isUploadHandlingPic));
		
		if (eventId != null && !eventId.equals(-1L)) {
			map.addAttribute("markerOperation", ConstantValue.EDIT_MARKER); // 编辑标注操作
		} else {
			map.addAttribute("markerOperation", ConstantValue.ADD_MARKER); // 添加标注操作
		}
		
		map.put("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		map.put("maxHappenTime", DateUtils.getNow());//发生时间上限时间 yyyy-mm-dd hh:mi:ss，当happenTimeStr为空时，使用

		String privateFolderName = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.PRIVATE_FOLDER_NAME4_EVENT+"Add", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);

		//事件描述最小字符限制
		String characterLimit = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.DESCRIP_CHARACTER_LIMIT, IFunConfigurationService.CFG_TYPE_FACT_VAL,  userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(characterLimit)){
			try {
				map.addAttribute("characterLimit",Long.valueOf(characterLimit));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		return "/zzgl/event"+privateFolderName+"/add_event_000.ftl";
	}
	
	/**
	 * 重启事件
	 * @param session
	 * @param eventId
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toRestartEvent")
	public String toRestartEvent(HttpSession session, 
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		String path = "",
			   trigger = "",
			   typesForList = "";
		boolean isRemoveTypes = false;
		
		path = this.toAddEventByType(session, eventId, trigger, typesForList, isRemoveTypes, params, map);
		
		if(map.containsAttribute("event")) {
			EventDisposal event = (EventDisposal)map.get("event");
			String status = event.getStatus();
			
			if(ConstantValue.EVENT_STATUS_END.equals(status)) {//事件状态为归档
				event.setEventId(null);
				event.setCode(null);
				event.setHappenTimeStr(DateUtils.getNow());
				
				map.addAttribute("event", event);
				map.addAttribute("parentEventId", eventId);
			} else {
				map.remove("event");
			}
		}
		if(map.containsAttribute("instanceId")) {
			map.remove("instanceId");
		}
		if(map.containsAttribute("involvedPeopleList")) {
			map.remove("involvedPeopleList");
		}
		
		return path;
	}
			
	/**
	 * 依据json串跳转事件新增页面
	 * @param session
	 * @param eventJson
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddEventByJson")
	public String toAddEventByJson(HttpSession session, 
			@RequestParam(value = "eventJson", required = false) String eventJson,
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		EventDisposal event = new EventDisposal();
		Map<String,Object> toAddPageParmas=new HashMap<String,Object>();
		String typesForList = "",
			   trigger = "",
			   attachmentIds = "";
		boolean isReport = true, isShowSaveBtn = true, isShowCloseBtn = true, isRemoveTypes = false;
		
		if(StringUtils.isNotBlank(eventJson)){
			Map<String, Object> eventMap = null;
			try{
				eventMap = JsonUtils.json2Map(eventJson);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(CommonFunctions.isBlank(eventMap, "orgCode")) {
				eventMap.put("orgCode", userInfo.getOrgCode());
			}
			event = eventDisposalDockingService.jsonToEvent(eventMap);
			attachmentIds = event.getAttachmentIds();
			
			map.addAllAttributes(eventMap);
			
			if(event.getGridId()==null || event.getGridId().equals(-99L)){
				Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
				String gridId = defaultGridInfo.get(KEY_START_GRID_ID).toString();
				String gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString();
				String gridCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
				
				event.setGridId(Long.valueOf(gridId));
				event.setGridName(gridName.trim());
				event.setGridCode(gridCode);
			}
			if(CommonFunctions.isNotBlank(eventMap, "callBack")){
				map.addAttribute("callBack", eventMap.get("callBack"));
			}
			if(CommonFunctions.isNotBlank(eventMap, "typesForList")){
				typesForList = eventMap.get("typesForList").toString();
			}
			if(CommonFunctions.isNotBlank(eventMap, "trigger")){
				trigger = eventMap.get("trigger").toString();
			}
			if(CommonFunctions.isNotBlank(eventMap, "isRemoveTypes")) {
				isRemoveTypes = Boolean.valueOf(eventMap.get("isRemoveTypes").toString());
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "iframeUrl")){
				String iframeUrl = eventMap.get("iframeUrl").toString();
				
				if(iframeUrl.contains("$ZZGRID_DOMAIN")) {
					iframeUrl = iframeUrl.replace("$ZZGRID_DOMAIN", App.ZZGRID.getDomain(session));
				}
				
				map.addAttribute("iframeUrl", iframeUrl);
			}
			if(CommonFunctions.isNotBlank(eventMap, "iframeCloseCallBack")){
				map.addAttribute("iframeCloseCallBack", eventMap.get("iframeCloseCallBack"));
			}
			if(CommonFunctions.isNotBlank(eventMap, "advice")) {
				map.addAttribute("advice", eventMap.get("advice").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "msg")) {
				map.addAttribute("msgWrong", eventMap.get("msg").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "redisKey")) {
				map.addAttribute("redisKey", eventMap.get("redisKey").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "isReport")) {
				isReport = Boolean.valueOf(eventMap.get("isReport").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "isShowSaveBtn")) {
				isShowSaveBtn = Boolean.valueOf(eventMap.get("isShowSaveBtn").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "isShowCloseBtn")) {
				isShowCloseBtn = Boolean.valueOf(eventMap.get("isShowCloseBtn").toString());
			}
			if(StringUtils.isBlank(event.getType())){
				String eventDefaultType = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger+ConstantValue.DEFAULT_TYPE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode()),
					   eventDefaultTypeName = "";
				
				if(StringUtils.isNotBlank(trigger) && StringUtils.isBlank(typesForList)) {
					if(isRemoveTypes) {
						typesForList = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
					} else {
						typesForList = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
					}
				}
				
				if(StringUtils.isNotBlank(eventDefaultType)) {
					eventDefaultTypeName = baseDictionaryService.changeCodeToName(ConstantValue.BIG_TYPE_PCODE, eventDefaultType, userInfo.getOrgCode(), true);
				}
				
				event.setTypeName(eventDefaultTypeName);
				event.setType(eventDefaultType);
			}
			if(StringUtils.isNotBlank(attachmentIds)) {
				map.addAttribute("attachmentIds", attachmentIds);
			}
			
			map.addAttribute("startGridId", event.getGridId());
			map.addAttribute("gridCode",event.getGridCode());
			
			
			if(CommonFunctions.isNotBlank(eventMap, "triggerSource")) {
				toAddPageParmas.put("triggerSource",eventMap.get("triggerSource").toString());
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "sourceForList")) {
				toAddPageParmas.put("sourceForList",eventMap.get("sourceForList").toString());
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "isRemoveSource")) {
				toAddPageParmas.put("isRemoveSource",eventMap.get("isRemoveSource").toString());
			}
		}
		
		String forwardUrl = this.toAddEventByType(session, event.getEventId(), trigger, typesForList, isRemoveTypes, toAddPageParmas, map);
		
		if(StringUtils.isNotBlank(eventJson)){
			if(StringUtils.isBlank(event.getContactUser())) {
				event.setContactUser(userInfo.getPartyName());
			}
			if(StringUtils.isBlank(event.getTel())) {
				event.setTel(userInfo.getVerifyMobile());
			}
			
			if(CommonFunctions.isNotBlank(map, "event")) {
				try {
					CommonFunctions.mergeBean(event, (EventDisposal) map.get("event"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			map.addAttribute("event", event);
		}
		
		String showLabelInput = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.SHOW_LABEL_INPUT, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(showLabelInput)) {
			
			try {
				Map<String,Object> searchLabelParams=new HashMap<String,Object>();
				searchLabelParams.put("labelModel", showLabelInput);
				List<Map<String, Object>> eventLabelList = eventLabelService.searchListByParam(searchLabelParams);
				map.addAttribute("eventLabelDict", eventLabelList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		map.addAttribute("isReport", isReport);
		map.addAttribute("isShowSaveBtn", isShowSaveBtn);
		map.addAttribute("isShowCloseBtn", isShowCloseBtn);
		map.addAttribute("rootGridId", event.getGridId());
		
		
		return forwardUrl;
	}
	
	/**
	 * 菜单跳转事件新增页面
	 * @param session
	 * @param eventJson
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddEventByMenu")
	public String toAddEventByMenu(HttpSession session, 
			@RequestParam(value = "eventJson", required = false) String eventJson,
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		this.toAddEventByJson(session, eventJson, map);

		String privateFolderName = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.PRIVATE_FOLDER_NAME4_EVENT+"Add", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);

		return "/zzgl/event"+privateFolderName+"/add_event_menu.ftl";
	}
	
	/**
	 * 全球眼跳转事件新增页面
	 * @param session
	 * @param eventJson
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddEventByVideo")
	public String toAddEventByVideo(HttpServletRequest req,HttpSession session, 
			@RequestParam(value = "eventJson", required = false) String eventJson,
			@RequestParam(value = "monitorById", required=false) Long monitorById,
			@RequestParam(value = "attrId", required=false) Long attrId,
			String picName, ModelMap map){
		MonitorBO monitorBO =monitorService.getMonitorById(monitorById);
		
		if(monitorBO!=null) {
			eventJson=eventJson.replace("}", ",\"eventName\":\""+monitorBO.getChannelName()+"\"}");
		}
		this.toAddEventByJson(session, eventJson, map);
		
		String from=req.getParameter("from");
		if(StringUtils.isBlank(from)) {
			from="";
		}else {
			EventDisposal event =(EventDisposal)map.get("event");
			event.setEventName("");
			event.setOccurred(monitorBO.getChannelName());
			ResMarker resMarker=resGisMarkerService.findResMarkerByResId(ConstantValue.MARKER_TYPE_GLOBAL_EYES, monitorById, "5");
			event.setInfoOrgCode(monitorBO.getOrgCode());
			if(resMarker!=null) {
				event.setResMarker(resMarker); 
		    }
				if(StringUtils.isNotBlank(monitorBO.getOrgCode())) {//所属网格
					//根据地域code获取网格
					MixedGridInfo grid = mixedGridInfoService.getDefaultGridByOrgCode(monitorBO.getOrgCode());
					if(grid!=null) {
					  	 event.setGridName(grid.getGridName());
			        	 event.setGridCode(grid.getGridCode());
					}
				}
			map.put("event", event);
			if(attrId!=null) {
				map.put("attr", attachmentService.findById(attrId));
			}
		}
		
		return "/zzgl/event/add_event_video"+from+".ftl";
	}
	@RequestMapping(value = "/detailByVideo")
	public String detailByVideo(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "instanceId", required=false) String instanceId,
			@RequestParam(value = "workFlowId", required=false) String workFlowId,
			@RequestParam(value = "taskId", required=false) String taskId, 
			@RequestParam(value = "position", required=false) String position, 
			@RequestParam(value = "eventType", required=false) String eventType,
			@RequestParam(value = "source", required=false) String source,
			@RequestParam(value = "model", required=false,defaultValue="l") String model,
			@RequestParam(value = "mapt", required=false) String mapt,
			@RequestParam(value = "showNotice", required=false) String showNotice,
			@RequestParam(value = "mapEngineType", required=false) String mapEngineType,
			ModelMap map) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Pattern pattern = Pattern.compile("[0-9]*");
	    
		if (StringUtils.isBlank(instanceId) || "null".equals(instanceId)) {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("eventType", eventType);
			params.put("handlingUserId", userInfo.getUserId());
			params.put("handlingOrgId", userInfo.getOrgId());
			params.put("userOrgCode", userInfo.getOrgCode());
			
			instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(eventId, params);
		}
		if(StringUtils.isNotBlank(workFlowId) && !pattern.matcher(workFlowId.trim()).matches()){
			workFlowId = null;
		}
		  this.detailEventType(session, request, eventId, instanceId, workFlowId, position, eventType, source, model, mapt, showNotice, mapEngineType, null, map);
		return  "/zzgl/event/detail_by_video.ftl";
	}
	
	@RequestMapping(value = "/detailEvent")
	public String detailEvent(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "instanceId", required=false) String instanceId,
			@RequestParam(value = "workFlowId", required=false) String workFlowId,
			@RequestParam(value = "taskId", required=false) String taskId, 
			@RequestParam(value = "position", required=false) String position, 
			@RequestParam(value = "eventType", required=false) String eventType,
			@RequestParam(value = "source", required=false) String source,
			@RequestParam(value = "model", required=false,defaultValue="l") String model,
			@RequestParam(value = "mapt", required=false) String mapt,
			@RequestParam(value = "showNotice", required=false) String showNotice,
			@RequestParam(value = "mapEngineType", required=false) String mapEngineType,
			@RequestParam Map<String, Object> extraParams,
			ModelMap map) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Pattern pattern = Pattern.compile("[0-9]*");
	    
		if (StringUtils.isBlank(instanceId) || "null".equals(instanceId)) {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("eventType", eventType);
			params.put("handlingUserId", userInfo.getUserId());
			params.put("handlingOrgId", userInfo.getOrgId());
			params.put("userOrgCode", userInfo.getOrgCode());
			
			instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(eventId, params);
		}
		if(StringUtils.isNotBlank(workFlowId) && !pattern.matcher(workFlowId.trim()).matches()){
			workFlowId = null;
		}
		return this.detailEventType(session, request, eventId, instanceId, workFlowId, position, eventType, source, model, mapt, showNotice, mapEngineType, extraParams, map);
	}

	/**
	 * 跳转事件详情页面
	 * @param session
	 * @param request
	 * @param eventId
	 * @param instanceId
	 * @param workFlowId
	 * @param position
	 * @param eventType
	 * @param source
	 * @param model
	 * @param mapt
	 * @param showNotice
	 * @param mapEngineType
	 * @param params
	 * 			isIgnoreStatus	是否忽略事件状态，true忽略；为true时，不可办理事件
	 * 			mapType			地图类型，为空时，mapt不为空时，使用mapt
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/detailEvent/{type}")
	public String detailEventType(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "instanceId", required=false) String instanceId,
			@RequestParam(value = "workFlowId", required=false) String workFlowId,
			@RequestParam(value = "position", required=false) String position, 
			@RequestParam(value = "eventType", required=false) String eventType,
			@RequestParam(value = "source", required=false) String source,
			@RequestParam(value = "model", required=false,defaultValue="l") String model,
			@RequestParam(value = "mapt", required=false) String mapt,
			@RequestParam(value = "showNotice", required=false) String showNotice,
			@RequestParam(value = "mapEngineType", required=false) String mapEngineType,
			@RequestParam Map<String, Object> params,
			ModelMap map) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String type = "", userOrgCode = userInfo.getOrgCode(),
			   privateFolderName = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.PRIVATE_FOLDER_NAME4_EVENT + "Detail", IFunConfigurationService.CFG_TYPE_FACT_VAL,  userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);
		Long instanceIdL = -1L;
		boolean isDetail2Edit = false, isHandleEdit = false;
		EventDisposal event = null;
		Map<String, Object> eventResultMap = null;
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(!params.isEmpty()) {
			map.addAllAttributes(params);
		}
		
		params.put("userOrgCode", userOrgCode);
		
		if(CommonFunctions.isBlank(params, "mapType") && StringUtils.isNotBlank(mapt)) {
			params.put("isCapMapInfo", true);
			params.put("mapType", mapt);
		}
		
		//是否获取事件相关类别信息
		if(CommonFunctions.isBlank(params, ConstantValue.IS_CAP_EVENT_LABEL_INFO)) {
			
			String isCapEventLabelInfo = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.IS_CAP_EVENT_LABEL_INFO, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
			if(StringUtils.isNotBlank(isCapEventLabelInfo)) {
				boolean flag = Boolean.valueOf(isCapEventLabelInfo);
				if(flag){
					params.put(ConstantValue.IS_CAP_EVENT_LABEL_INFO, flag);
				}
			}
		}
		//默认获取地图定位信息
		params.put("isCapMapInfo",true);
		if(CommonFunctions.isBlank(params,"mapType")){
			params.put("mapType","5");
		}
		try {
			eventResultMap = eventDisposalService.findEventByMap(eventId, params);
		} catch(Exception e) {
			map.addAttribute("msgWrong", e.getMessage());
		}
		
		if(eventResultMap != null && !eventResultMap.isEmpty()) {
			map.addAllAttributes(eventResultMap);
			map.addAttribute("bizType", IReportFeedbackService.BizType.NORMAL_EVENT.getCode());
		}
		
		if(CommonFunctions.isNotBlank(eventResultMap, "event")) {
			event = (EventDisposal) eventResultMap.get("event");
		}
		
		if(event != null) {
			map.addAttribute("involvedPeopleList", involvedPeopleService.findInvolvedPeopleListByBiz(eventId, InvolvedPeople.BIZ_TYPE.ZHSQ_EVENT.getBizType()));
	
			if(event != null) {
				type = event.getType();
			}
	
			if (StringUtils.isBlank(mapEngineType)) {
				mapEngineType = "";
			}
			
			map.put("mapEngineType", mapEngineType);
			
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
					if("done".equals(eventType)){//经办
						taskInfo = hessianFlowService.getStartTimeAndTimeLimitByHistoryTaskId(taskId);
					}else{//待办、我发起的
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
					
					if(!map.containsAttribute("curNodeTaskName")) {
						String taskName = null;
						
						try {
							taskName = eventDisposalWorkflowService.curNodeTaskName(instanceIdL);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						if(StringUtils.isNotBlank(taskName)){
							map.addAttribute("curNodeTaskName", taskName);
						}
					}
				}
			}else{
				instanceId = null;
			}
			
			String filedomain = App.IMG.getDomain(session);
			map.put("downPath", filedomain);
			map.put("workflowCtx", workflowCtx);
			map.put("event", event);
			map.put("showNotice", showNotice);
			
			map.addAttribute("instanceId", instanceId);
			map.addAttribute("workFlowId", workFlowId);
			map.addAttribute("taskId", taskId);
			
			if(!StringUtils.isBlank(eventType) && ("todo".equals(eventType) || "map".equals(eventType) || "workStation".equals(eventType))){
				if(!StringUtils.isBlank(instanceId) && hessianFlowService.isCurTask(taskId)){
					Map<String, Object> initMap = new HashMap<String, Object>(),
										resultMap = null;
					
					initMap.put("eventId", eventId);
					initMap.put("instanceId", instanceIdL);
					initMap.put("type", type);
					initMap.put("event", event);

					resultMap = eventDisposalWorkflowService.initFlowInfo(taskId, instanceIdL, userInfo, initMap);
					
					map.put("type", type);
					map.addAllAttributes(resultMap);
					map.addAttribute("isLoadJs", true);//用于判断是否重复加载handle_event.ftl中的js
					map.addAttribute("formId", eventId);
					map.addAttribute("isHandle", true);//用于判断当前任务是否可办理
					
					if(CommonFunctions.isNotBlank(resultMap, "curNode")) {
						Node curNode = (Node)resultMap.get("curNode");
						
						if(CommonFunctions.isBlank(resultMap, "isDetail2Edit")) {//当前页面是否可以对事件进行修改
							String workflowName="";
							if(CommonFunctions.isNotBlank(resultMap, "proInstance")) {
								ProInstance proInstance= (ProInstance) resultMap.get("proInstance");
								workflowName = proInstance.getProName();
							}
							String isDetail2EditStr = configurationService.turnCodeToValue(
									ConstantValue.IS_DETAIL_2_EDIT,
									"[" + curNode.getNodeName() + "|" + curNode.getNodeName() + "_" + workflowName
											+ "]",
									IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode,
									IFunConfigurationService.CFG_ORG_TYPE_0,
									IFunConfigurationService.DIRECTION_UP_REGEXP);
							if (StringUtils.isNotBlank(isDetail2EditStr)) {
								isDetail2Edit = Boolean.valueOf(isDetail2EditStr);
							}
						}else {
							isDetail2Edit = Boolean.valueOf(resultMap.get("isDetail2Edit").toString());
						}
						
						map.addAttribute("curNodeTaskName", curNode.getNodeNameZH());
					}
					
					if(CommonFunctions.isNotBlank(resultMap, "isHandleEdit")) {
						isHandleEdit = Boolean.valueOf(resultMap.get("isHandleEdit").toString());
					}
				}else{
					map.addAttribute("isHandle", false);
				}
			}
			
			map.addAttribute("model", model);
			map.addAttribute("position", position==null?"":position);
			
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
					
					if(isCurrentUser || "map".equals(eventType)){//事件待办详情的短信发送、地图事件详情的“一键发送”
						boolean isShowSendMsg = true;
						String isShowSendMsgStr = configurationService.turnCodeToValue(ConstantValue.SEND_MSG_AND_SMS_CFG, ConstantValue.IS_SHOW_SEND_MSG, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
						
						if(StringUtils.isNotBlank(isShowSendMsgStr)) {
							isShowSendMsg = Boolean.valueOf(isShowSendMsgStr);
						}
						
						map.addAttribute("isShowSendMsg", isShowSendMsg);
					}
					
					if(isCurrentUser){//当前登录人员未在当前办理人中时，不可办理该事件
						String handleEventPage = eventDisposalWorkflowService.capWorkflowHandlePage(eventId, type, userInfo, map);
						boolean isIgnoreStatus = false;
						
						if(CommonFunctions.isNotBlank(params, "isIgnoreStatus")) {
							isIgnoreStatus = Boolean.valueOf(params.get("isIgnoreStatus").toString());
						}
						
						if(!isIgnoreStatus) {//有忽略事件状态的，不可办理事件
							map.addAttribute("evaLevelDict", baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.EVALUATE_LEVEL_PCODE, userOrgCode));
							map.addAttribute("handleEventPage", handleEventPage);
							
							//获取事件默认办结期限
							String eventDefaultHandleInterval = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.EVENT_DEFAULT_HANDLE_INTERVAL, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
							if(StringUtils.isNotBlank(eventDefaultHandleInterval)) {
								map.addAttribute("eventDefaultHandleInterval", eventDefaultHandleInterval);
							}
						}
					}
					
					taskPersonStr = new StringBuffer(taskPersonStr.substring(1));
				}
				map.addAttribute("taskPersonStr", taskPersonStr);
			}
			map.addAttribute("eventType", eventType);
			
			//获取关联模块
			EventReportRecordInfo eventReportRecordInfo = new EventReportRecordInfo();
			if(eventId!=null && eventId > 0){
				eventReportRecordInfo.setEventId(eventId);
				eventReportRecordInfo = eventReportRecordService.findEventReportRecordInfoByEventId(eventReportRecordInfo);
				String bizDetailUrl = "";
				String bizDetailUrl2 = "";
				if(eventReportRecordInfo!=null && eventReportRecordInfo.getBizType() != null){
					String eventBizPlatform = event.getBizPlatform(), eventReportBizTypeName = null,
						   trigCondition = eventReportRecordInfo.getBizType();
					
					if(!ConstantValue.DEFAULT_EVENT_BIZ_PLATFORM.equals(eventBizPlatform)) {
						trigCondition += "_" + eventBizPlatform;
					}
					
					eventReportBizTypeName = configurationService.turnCodeToValue(ConstantValue.EVENT_REPORT_BIZ_TYPE_NAME, trigCondition, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);
					
					bizDetailUrl2 = configurationService.turnCodeToValue(ConstantValue.BIZ_DETAIL_URL_IN_EVENT, eventReportRecordInfo.getBizType(), IFunConfigurationService.CFG_TYPE_URL, IFunConfigurationService.CFG_ORG_TYPE_0);
					
					String eventReportBizId = "";
					if(eventReportRecordInfo.getBizId() == null){
						
						if (StringUtils.isNotBlank(eventReportRecordInfo.getBizUuId())) {
							eventReportBizId = eventReportRecordInfo.getBizUuId();
						}
					}else {
						eventReportBizId = eventReportRecordInfo.getBizId().toString();
					}
					
					map.addAttribute("eventReportBizId", eventReportBizId);
					bizDetailUrl = bizDetailUrl2.replaceFirst("\\$bizId", eventReportBizId);
					
					
					if(StringUtils.isNotBlank(eventReportBizTypeName)) {
						map.addAttribute("eventReportBizTypeName", eventReportBizTypeName);
					}
					
					map.addAttribute("bizDetailUrl", bizDetailUrl);
				}
				//解决跨域关闭按钮的问题
				map.addAttribute("isDomain", "isDomain");
			}
			map.addAttribute("EVENT_ATTACHMENT_TYPE", ConstantValue.EVENT_ATTACHMENT_TYPE);//上传附件的类型
			
			//展示事件处理时限判断
			String showEventHandleDate = configurationService.turnCodeToValue(ConstantValue.SHOW_HANDLE_DATE, ConstantValue.SHOW_EVENT_HANDLE_DATE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
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
			String showStepHandleDate = configurationService.turnCodeToValue(ConstantValue.SHOW_HANDLE_DATE, ConstantValue.SHOW_STEP_HANDLE_DATE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
			if(StringUtils.isNotBlank(showStepHandleDate)){
				boolean flag = Boolean.valueOf(showStepHandleDate);
				if(flag){
					map.addAttribute("stepRemainTime", event.getRemainTime(handleTime));
					map.addAttribute("handleTime", handleTime);
				}
			}
			
			//是否上传处理中图片
			String isUploadHandlingPic = configurationService.turnCodeToValue(ConstantValue.IS_UPLOAD_HANDLING_PIC, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
			
			map.put("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
			map.addAttribute("isUploadHandlingPic", Boolean.valueOf(isUploadHandlingPic));
			
			if(!StringUtils.isBlank(source)){
				map.addAttribute("domain", App.TOP.getDomain(session));
				map.addAttribute("source", source);
			}
			map.put("mapt", mapt);
			map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
			
			//是否在新窗口打开图片
			String isOpenInNewWindows = configurationService.turnCodeToValue(ConstantValue.IS_OPEN_NEW_WINDOWS, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
			map.addAttribute("isOpenInNewWindows", Boolean.valueOf(isOpenInNewWindows));	
			
			map.addAttribute("markerOperation", ConstantValue.TITLE_MARKER); // 查看地图标注操作
			
			String modeType = request.getParameter("modeType");
			if (StringUtils.isNotBlank(modeType)) {
				map.addAttribute("modeType", modeType.trim());
			}
			
			//获取事件额外信息
			this.getEventExtendInfo(eventId, map, userInfo);
			
			if("my".equals(eventType) || "todo".equals(eventType)) {
				String isOnVoiceCallStr = configurationService.turnCodeToValue(ConstantValue.VOICE_CALL_ATTRIBUTE, ConstantValue.ON_VOICE_CALL, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
				boolean isOnVoiceCall = true;
				
				if(StringUtils.isNotBlank(isOnVoiceCallStr)) {
					isOnVoiceCall = Boolean.valueOf(isOnVoiceCallStr);
				}
				
				map.addAttribute("isOnVoiceCall", isOnVoiceCall);
			}

			//保存事件查看日志
			saveLog(request,eventId, "V", userInfo, null, null);
		} else {
			map.addAttribute("msgWrong", "未能找到有效的事件信息！");
		}
		
		String forwardUrl = "/zzgl/event"+privateFolderName+"/detail_event.ftl";
		
		if(isHandleEdit) {
			forwardUrl = "/zzgl/event"+privateFolderName+"/detail_event_edit.ftl";
		}
		
		if(CommonFunctions.isNotBlank(params, "evaType") && "history".equals(eventType)) {
			Map<String, Object> evaParam = new HashMap<String, Object>();
			
			evaParam.put("evaType", params.get("evaType"));
			evaParam.put("objectId", eventId);
			
			map.addAttribute("isAble2Evaluate", eventDisposal4ExtraService.findCount4Eva(evaParam) == 0);
		}
		
		if("draft".equals(eventType)) {
			map.addAttribute("markerOperation", ConstantValue.WATCH_MARKER); // 查看地图标注操作
			forwardUrl = "/zzgl/event"+privateFolderName+"/detail_event_draft.ftl";
		} else if("map".equals(eventType)) {
			forwardUrl = "/map/arcgis/standardmappage/detail_event.ftl";
		} else if("workStation".equals(eventType)) {
			map.addAttribute("domain", App.TOP.getDomain(session));
		} else if(isDetail2Edit || isHandleEdit) {
			map.addAttribute("isDetail2Edit", isDetail2Edit);
			map.addAttribute("markerOperation", ConstantValue.EDIT_MARKER);//编辑地图定位信息
			
			//获取当前登录账号下所管理的所有网格
			List<OrgEntityInfoBO> orgEntityInfoList = userInfo.getInfoOrgList();

			map.addAttribute("START_DIVISION_CODE", "-1");//标准地址库控件起始网格默认值

			if (null != orgEntityInfoList && orgEntityInfoList.size() > 0) {
				StringBuffer orgCodeSb = new StringBuffer("");
				String infoOrgCode = null;
				
				for (OrgEntityInfoBO orgEntityInfo : orgEntityInfoList) {
					infoOrgCode = orgEntityInfo.getOrgCode();
					
					if(StringUtils.isNotBlank(infoOrgCode)) {
						orgCodeSb.append(",").append(infoOrgCode);
					}
				}
				
				if(orgCodeSb.length() > 0) {
					map.addAttribute("START_DIVISION_CODE", orgCodeSb.substring(1));//标准地址库控件起始网格
				}
			}
		}
		
		return forwardUrl;
	}
	
	/**
	 * 获取事件的额外信息
	 * @param session
	 * @param eventId	事件id
	 * @param params	额外参数
	 * 			evaObj	评价类型，多个值使用英文逗号进行分割
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchEventExtraAttr")
	public Map<String, Object> fetchEventExtraAttr(
			HttpSession session,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String,Object>> evaResultList = null;
		List<?> superviseResultList = null;
		List<Map<String, Object>> timeAppList = null;
		int ancestorCount = 0, eventDuplicationCount = 0,feedbackCount = 0;
		
		if(eventId != null && eventId > 0) {
			// 获取评价列表
			if(CommonFunctions.isBlank(params, "evaObj")) {
				params.put("evaObj", ConstantValue.EVA_OBJ_NEW_EVENT);
			}
			
			try {
				evaResultList = eventDisposalExpandService.findEvaResultList(eventId, null, userInfo, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//获取被督办列表
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("eventId", eventId);
			param.put("userId", userInfo.getUserId());
			param.put("category", IEventDisposalWorkflowService.REMIND_CATEGORY.SUPERVISE.getCategory());
			
			try {
				superviseResultList = eventDisposalWorkflowService.findRemindList(param);
			} catch (Exception e) {
				if(logger.isErrorEnabled()) {
					logger.error("获取事件督办列表失败：", e);
				}
			}
			
			//获取时限申请列表
			Map<String, Object> timeApplication = new HashMap<String, Object>();
			List<String> applicationTypeList = new ArrayList<String>();
			
			for(ITimeApplicationService.APPLICATION_TYPE applicationType : ITimeApplicationService.APPLICATION_TYPE.values()) {
				applicationTypeList.add(applicationType.getValue());
			}
			
			applicationTypeList.remove(ITimeApplicationService.APPLICATION_TYPE.EVENT_VERIFY.getValue());
			
			timeApplication.put("businessKeyId", eventId);
			timeApplication.put("applicationTypeList", applicationTypeList);
			timeApplication.put("isTransferUser", true);
			timeApplication.put("isTransferOrg", true);
			
			timeAppList = timeApplicationService.findTimeApplicationList(timeApplication);
			
			//获取历史关联事件
			ancestorCount = eventExtendService.findAncestorCountByEventId(eventId, null);
			
			//获取重复事件
			Map<String, Object> eventDuplicateMap = new HashMap<String, Object>();
			eventDuplicateMap.put("leadEventId", eventId);
			
			eventDuplicationCount = eventDuplicationService.findDuplicatedEventCount(eventDuplicateMap, userInfo);

			//获取关联的反馈信息数量
			if(CommonFunctions.isNotBlank(params, "bizType")) {
				feedbackCount = reportFeedbackService.findReportFeedbackCount(String.valueOf(eventId),params.get("bizType").toString(),null).intValue();
			}
		}
		
		resultMap.put("feedbackCount", feedbackCount);
		resultMap.put("eventDuplicationCount", eventDuplicationCount);
		resultMap.put("ancestorCount", ancestorCount);
		resultMap.put("timeAppList", timeAppList);
		resultMap.put("evaResultList", evaResultList);
		resultMap.put("superviseResultList", superviseResultList);
		
		return resultMap;
	}
			
	@ResponseBody
	@RequestMapping(value = "/getEventGisDataByMapt")
	public ResMarker getEventGisDataByMapt(HttpSession session
			,@RequestParam(value = "eventId") Long eventId
			,@RequestParam(value = "mapt") String mapt
			,ModelMap map) {
		List<ResMarker> resMarkers = resGisMarkerService.findResMarkerByParam(ConstantValue.MARKER_TYPE_EVENT, eventId, mapt);
		ResMarker resMarker = new ResMarker();
		if(resMarkers.size() >= 1) {
			resMarker = resMarkers.get(0);
		}
		return resMarker;
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/getEventGisDataByMaptForJsonp")
	public String getEventGisDataByMaptForJsonp(HttpSession session, ModelMap map,
			@RequestParam(value = "eventId") Long eventId, @RequestParam(value = "mapt") String mapt,
			String jsoncallback) {
		ResMarker resMarker = this.getEventGisDataByMapt(session, eventId, mapt, map);
		String data = JSONObject.fromObject(resMarker).toString();
		String jsonData = jsoncallback + "(" + data +")";
		return jsonData;
	}

	
	/**
	 * 查看事件详情，增添流程查看
	 * @param session
	 * @param request
	 * @param eventId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/detailEventWorkflow")
	public String detailEventWorkflow(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "eventId") Long eventId, ModelMap map) {
		map.addAttribute("eventId", eventId);
		map.addAttribute("RESOURSE_SERVER_PATH",
				App.IMG.getDomain(session));
		return "/zzgl/event/detail_event_workflow.ftl";
	}
	
	/**
	 * 保存事件
	 * @param session
	 * @param ids
	 * @param event
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveEvent/{type}", method = RequestMethod.POST)
	public Map<String, Object> saveEvent(HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "attachmentId", required = false) Long[] ids,
			@ModelAttribute(value = "event") EventDisposal event,
			@RequestParam Map<String,Object> params,
			ModelMap map) {
		//SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
		UserInfo userInfo = (UserInfo) session
				.getAttribute(ConstantValue.USER_IN_SESSION);
		//event.setCode(formate.format(new Date()));
		event.setCollectWay(ConstantValue.COLLECT_WAY_PC);// PC录入
		event.setCreatorId(userInfo.getUserId());
		event.setStatus(ConstantValue.EVENT_STATUS_DRAFT);// 00

		// 把反馈人员设置默认为当前登录的用户
		if (StringUtils.isBlank(event.getContactUser())) {
			event.setContactUser(userInfo.getPartyName());
			if(!StringUtils.isBlank(userInfo.getVerifyMobile())){
				event.setTel(userInfo.getVerifyMobile());
			}
		} else if (event.getContactUser().equals(userInfo.getPartyName())
				&& StringUtils.isBlank(event.getTel())) {
			// 如果反馈人员是当前登录用户且反馈电话为空时，事件反馈电话默认设置为当前用户的联系电话
			if(!StringUtils.isBlank(userInfo.getVerifyMobile())){
				event.setTel(userInfo.getVerifyMobile());
			}
		}
		
//		if ("0205".equals(event.getType())) {
//			event.getVisitRecord().setCreateUser(event.getCreatorId());
//			event.getVisitRecord().setUpdateUser(event.getCreatorId());
//		} else if ("0602".equals(event.getType())) {
//			event.getEventSurvey().setCreateUser(event.getCreatorId());
//			event.getEventSurvey().setCreateDate(new Date());
//		}

		// 事件表(如果为0205,0207,0602,还要增加任务表和关联表)
		Long recordId = eventDisposalService.saveEventDisposalReturnId(event,params,userInfo);

		//保存事件操作日志
		if (null != recordId && recordId > 0) {
			//插入新增事件日志
			saveLog(request,recordId, "I", userInfo, null, event);
		}
		
		if(ids!=null && ids.length>0){
			attachmentService.updateBizId(recordId, ConstantValue.EVENT_ATTACHMENT_TYPE, ids);
		}
		
		//Map<String, Object> resultMap = this.startWorkFlow(session, recordId, toClose, map);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("eventId", recordId);
		resultMap.put("result", recordId > 0);
		resultMap.put("type", "save");
		
		return resultMap;
	}
	
	/**
	 * 保存事件并启动流程
	 * @param session
	 * @param ids
	 * @param event
	 * @param toClose
	 * @param advice 办理意见
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveEventAndStart/{type}", method = RequestMethod.POST)
	public Map<String, Object> saveEventAndStart(HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "attachmentId", required = false) Long[] ids,
			@ModelAttribute(value = "event") EventDisposal event,
			@RequestParam(value = "toClose", required=false) String toClose,
			@RequestParam(value = "advice", defaultValue="", required=false) String advice,
			@RequestParam Map<String,Object> params,
			ModelMap map) {
		Map<String, Object> resultMap = this.saveEvent(request,session, ids, event, params,map);
		Object eventIdObj = resultMap.get("eventId");
		Long eventId = -1L;
		if(eventIdObj != null){
			eventId = Long.valueOf(eventIdObj.toString());
		}
		resultMap.putAll(this.startWorkFlow(session, eventId, toClose, advice, map));
		
		return resultMap;
	}
	
	/**
	 * 保存事件并上报
	 * @param session
	 * @param ids			附件id
	 * @param event			事件信息
	 * @param userInfo
	 * @param params
	 * 			advice		办理意见
	 * 			isSendSms	是否发送短信
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveEventAndReport/{type}", method = RequestMethod.POST)
	public Map<String, Object> saveEventAndReport(
			HttpSession session,
			@RequestParam(value = "attachmentId", required = false) Long[] ids,
			@ModelAttribute(value = "event") EventDisposal event, 
			@RequestParam(value = "userInfo", required=false) UserInfo userInfo,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long eventId = -1L;
		
		if(userInfo == null){
			userInfo = (UserInfo)session.getAttribute(ConstantValue.USER_IN_SESSION);
		}
		
		params.put("event", event);
		params.put("userInfo", userInfo);
		
		resultMap = eventDisposalDockingService.saveEventDisposalAndReport(params);
		
		if(CommonFunctions.isNotBlank(resultMap, "eventId")){
			eventId = Long.valueOf(resultMap.get("eventId").toString());
			if(ids!=null && ids.length>0){//保存图片
				attachmentService.updateBizId(eventId, ConstantValue.EVENT_ATTACHMENT_TYPE, ids);
			}
		}
		return resultMap;
	}
	
	/**
	 * 修改事件
	 * @param session
	 * @param ids
	 * @param event
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editEvent/{type}", method = RequestMethod.POST)
	public Map<String, Object> editEvent(HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "attachmentId", required = false) Long[] ids,
			@ModelAttribute(value = "event") EventDisposal event,
			@RequestParam Map<String,Object> params,
			ModelMap map) {

		UserInfo userInfo = (UserInfo)session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean result = false;
		Map<String, Object> eventParams = new HashMap<String, Object>(), eventMap = null;
		EventDisposal eventBefore = null;

		event.setUpdatorId(userInfo.getUserId());

		eventParams.put("userOrgCode", userInfo.getOrgCode());

		try {
			eventMap = this.eventDisposalService.findEventByMap(event.getEventId(), eventParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(params == null) {
			params = new HashMap<String, Object>();
		}

		result = eventDisposalExpandService.updateEventDisposal(event,params,userInfo);

		//保存事件操作日志
		if (result) {
			//插入更新事件日志
			if(CommonFunctions.isNotBlank(eventMap, "event")) {
				eventBefore = (EventDisposal) eventMap.get("event");
			}
			saveLog(request,event.getEventId(), "U", userInfo, eventBefore, event);
		}
		
		Long eventId = event.getEventId();
		if(ids!=null && ids.length>0){
			attachmentService.updateBizId(eventId, ConstantValue.EVENT_ATTACHMENT_TYPE, ids);
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("eventId", eventId);
		resultMap.put("result", result);
		resultMap.put("type", "update");
		
		return resultMap;
	}
	
	/**
	 * 修改事件并启动流程
	 * @param session
	 * @param ids
	 * @param event
	 * @param toClose
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editEventAndStart/{type}", method = RequestMethod.POST)
	public Map<String, Object> editEventAndStart(HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "attachmentId", required = false) Long[] ids,
			@ModelAttribute(value = "event") EventDisposal event,
			@RequestParam(value = "toClose", required=false) String toClose,
			@RequestParam(value = "advice", defaultValue="", required=false) String advice,
			@RequestParam Map<String,Object> params,
			ModelMap map) {
		Map<String, Object> resultMap = this.editEvent(request,session, ids, event, params,map);
		Object eventIdObj = resultMap.get("eventId");
		Long eventId = -1L;
		if(eventIdObj != null){
			eventId = Long.valueOf(eventIdObj.toString());
		}
		resultMap.putAll(this.startWorkFlow(session, eventId, toClose, advice, map));
		return resultMap;
	}
	

	/**
	 * 删除事件 支持批量删除
	 * @param session
	 * @param request
	 * @param idStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delOldEvent", method = RequestMethod.POST)
	public Map<String, Object> delOldEvent(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "oldidStr") String idStr) {
		UserInfo userInfo = (UserInfo) session
				.getAttribute(ConstantValue.USER_IN_SESSION);
		String[] ids = idStr.split(",");
		List<Long> recordIdList = new ArrayList<Long>();
		for (int i = 0; i < ids.length; i++) {
			try {
				long recordId = Long.parseLong(ids[i]);
				recordIdList.add(recordId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		boolean result = oldEventDisposalService.deleteEventDisposalById(
				userInfo.getUserId(), recordIdList);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result ? recordIdList.size() : 0L);
		return resultMap;
	}
	
	/**
	 * 删除事件 支持批量删除
	 * @param session
	 * @param request
	 * @param idStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delEvent", method = RequestMethod.POST)
	public Map<String, Object> delEvent(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "idStr", required = false) String idStr,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<Long> recordIdList = new ArrayList<Long>();
		
		if(StringUtils.isNotBlank(idStr)) {
			String[] ids = idStr.split(",");
			
			for(int index = 0, length = ids.length; index < length; index++){
				long recordId = Long.parseLong(ids[index]);
				recordIdList.add(recordId);
				saveLog( request,recordId, "D", userInfo, null, null);
			}
		}
		
		Map<String, Object> resultMap = eventDisposalExpandService.delEventById(recordIdList, userInfo, params);
		
		//为了减少页面调整
		if(CommonFunctions.isNotBlank(resultMap, "successTotal")) {
			resultMap.put("result", resultMap.get("successTotal"));
		}
		
		// 结案时，如果websiteId不为空是来自网站群，需要发送数据到网站群
		/*if (eventDisposal != null && eventDisposal.getWebsiteId() != null) {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("id", eventDisposal.getWebsiteId());
			String jsonStr = JSON.toJSONString(returnMap);
			// 格式：{"id":75}
			publicRegionService.publicRegionHandle(
					"zzgrid_zzgl_event_eventdisposal_delete",
					jsonStr);
		}*/
		
		return resultMap;
	}
	
	/**
	 * 跳转废除事件操作页面
	 * @param session
	 * @param eventId	事件id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toInvalidEvent")
	public String toInvalidEvent(HttpSession session,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "invalidType", required = false, defaultValue = "1") Integer invalidType,
			@RequestParam Map<String,Object> params,
			ModelMap map) {
		Map<String, Object> initAppMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String forwardPath = "/zzgl/timeApplication/add_timeApplication_base.ftl";
		
		//设置默认值
		initAppMap.put("applicationType", ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_INVALID.toString());
		try {
			initAppMap = timeApplicationService.initTimeApplication4Event(eventId, ITimeApplicationService.APPLICATION_TYPE_HIDE.EVENT_INVALID.toString(), userInfo, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//最后已外部传入为准
		initAppMap.putAll(params);
		
		map.addAllAttributes(initAppMap);

		switch(invalidType) {
			case 2: {//督查
				forwardPath = "/zzgl/timeApplication/inspectApplication/add_timeApplication_inspect.ftl"; break;
			}
		}
		
		return forwardPath;
	}
	
	
	/**
	 * 跳转到首页的结案页面
	 * 
	 * @param session
	 * @param eventId
	 * @param result
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/showUpdateAndClosedPage")
	public String updateAndClosedPage(HttpSession session,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "type", required=false) String type,
			ModelMap map) {
		//UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		if(!StringUtils.isBlank(type)){
			map.put("type", type);
		}else{
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			map.put("type", event.getType());
		}
		map.put("eventId", eventId);
		return "/zzgl/event/outPlatform/close_event.ftl";
	}
	/**
	 * 更新事件信息
	 * @param session
	 * @param request
	 * @param eventId
	 * @param eventStatus
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateEvent", method = RequestMethod.POST)
	public Map<String, Object> updateEvent(HttpSession session,
			HttpServletRequest request,
			@ModelAttribute(value = "event") EventDisposal event) {
		boolean updateFlag = true;
		Long eventId = event.getEventId();
		String eventStatus = event.getStatus();
		String eventSubStatus = event.getSubStatus();
		Long updateEventId = -1L;
		
		if(eventId==null || eventId.equals(-1L)){
			updateFlag = false;
		}
		if(StringUtils.isBlank(eventStatus)){
			updateFlag = false;
		}
		
		if(updateFlag){
			Map<String, Object> eventParams = new HashMap<String, Object>(), eventMap = null;
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

			event.setEventId(eventId);
			event.setStatus(eventStatus);
			if(eventSubStatus != null) {//子状态可置空
				event.setSubStatus(eventSubStatus);
			}

			eventParams.put("userOrgCode", userInfo.getOrgCode());
			try {
				eventMap = this.eventDisposalService.findEventByMap(event.getEventId(), eventParams);
			} catch (Exception e) {
				e.printStackTrace();
			}

			updateEventId = eventDisposalService.updateEventDisposalById(event);

			//保存事件更新操作日志
			if(updateEventId > -1L){
				EventDisposal eventBefore = null;

				if(CommonFunctions.isNotBlank(eventMap, "event")) {
					eventBefore = (EventDisposal) eventMap.get("event");
				}

				saveLog(request,eventId, "U", userInfo, eventBefore, event);
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", updateEventId > -1L);
		
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/startWorkFlow", method = RequestMethod.POST)
	public Map<String, Object> startWorkFlow(
			HttpSession session,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "toClose", required=false) String toClose,
			@RequestParam(value = "advice", defaultValue="", required=false) String advice,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		OrgSocialInfoBO org = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
		
		Long workflowId = eventDisposalWorkflowService.capWorkflowId(null, eventId, userInfo, null);
		
		if(StringUtils.isBlank(toClose)){
			toClose = "0";
		}
		
		resultMap.put("formId", eventId);
		resultMap.put("workflowId", workflowId);//130000,w.getWorkFlowId()
		resultMap.put("wftypeId", ConstantValue.EVENT_WORKFLOW_WFTYPEID);
		resultMap.put("orgCode", userInfo.getOrgCode());
		resultMap.put("orgType", org.getOrgType());
		resultMap.put("toClose", toClose);
		resultMap.put("advice", advice);
		
		return resultMap;
	}
	
	/**
	 * 接收任务，添加任务接收时间
	 * @param session
	 * @param eventId		事件id
	 * @param taskReceive
	 *  必填属性:
	 * 			taskId:任务Id
	 * 			instanceId:流程Id
	 * 			userId:用户Id
	 * 			userName:用户名
	 * 			orgId:组织Id
	 * 			orgName:组织名
	 * 			receiveTime:接收时间
	 * 非必填属性:
	 * 			remark:接收意见(预留接口)
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/receiveTask", method = RequestMethod.POST)
	public Map<String, Object> receiveTask(
			HttpSession session,
			@RequestParam(value = "eventId", required = false) Long eventId,
			@ModelAttribute(value = "taskReceive") TaskReceive taskReceive,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>(),
							extraParam = new HashMap<String, Object>();
		
		if(eventId != null && eventId > 0) {
			extraParam.put("eventId", eventId);
		}
		
		eventDisposalWorkflowService.receiveTask(taskReceive, userInfo, extraParam);
		
		resultMap.put("result", true);
		
		return resultMap;
	}
	
	/**
	 * 数据字典树
	 * @param session
	 * @param dictPcode
	 * @param orgCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDataDictTree", method = RequestMethod.POST)
	public List<Map<String, Object>> getDataDictTree(
			HttpSession session,
			@RequestParam(value = "dictPcode", required = false) String dictPcode,
			@RequestParam(value = "orgCode", required = false) String orgCode) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(StringUtils.isBlank(orgCode)){
			orgCode = userInfo.getOrgCode();
		}
		return DataDictHelper.getDataDictTree(dictPcode, orgCode);
	}
	
	/**
	 * 跳转到评论页面
	 * @param session
	 * @param eventId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toCommentEvent")
	public String toCommentEvent(HttpSession session,
			@RequestParam(value = "eventId") Long eventId,
			ModelMap map) {
		map.addAttribute("eventId", eventId);
		return "/zzgl/event/workflow/comment_event.ftl";
	}
	
	/**
	 * 事件评价功能
	 * @param session
	 * @param eventId
	 * @param evaLevel
	 * @param evaContent
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/commentEvent")
	public Map<String, Object> commentEvent(HttpSession session,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "evaLevel") String evaLevel,
			@RequestParam(value = "evaContent") String evaContent, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//EvaResult evaResult = new EvaResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String,Object> params = new HashMap<>();

		boolean result = false;
		try {
			result = eventDisposalExpandService.saveOrUpdateEventEvaluate(userInfo,eventId,evaLevel,evaContent,params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("result", result);
		resultMap.put("type", "closed");
		
		return resultMap;
	}
	
	/**
	 * 跳转到多图片上传页面
	 * @param session
	 * @param eventId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toUploadMultiFiles")
	public String toUploadMultiFiles(HttpSession session,
			@RequestParam(value = "eventId") Long eventId,
			ModelMap map) {
		//UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
		
		map.addAttribute("eventId", eventId);
		map.put("type", event.getType());
		return "/zzgl/event/workflow/upload_multifiles.ftl";
	}
	
	/**
	 * 多图片上传
	 * @param session
	 * @param ids
	 * @param bizId
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadMultiFiles", method = RequestMethod.POST)
	public Map<String, Object> uploadMultiFiles(
			HttpSession session,
			@RequestParam(value = "attachmentId", required = false) Long[] ids,
			@RequestParam(value = "bizId") Long bizId,
			ModelMap map) {
		boolean result = true;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(ids!=null && ids.length>0){
			result = attachmentService.updateBizId(bizId, ConstantValue.EVENT_ATTACHMENT_TYPE, ids);
		}
		
		resultMap.put("result", result);
		resultMap.put("type", "closed");
		
		return resultMap;
	}
	
	/**
	 * 通过名字和身份证号获取居民信息
	 * @param session
	 * @param name
	 * @param identityCard
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCiRsIdByNameAndIdentityCard", method=RequestMethod.POST)
	public Map<String, Object> getCiRsIdByNameAndIdentityCard(HttpSession session, 
			@RequestParam(value="name") String name,
			@RequestParam(value="identityCard") String identityCard, 
			ModelMap map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			name = java.net.URLDecoder.decode(name, "utf-8").trim();
		} catch (UnsupportedEncodingException e) {}
		Long ciRsId = residentService.getCiRsIdByNameAndIdentityCard(name, identityCard);
		resultMap.put("ciRsId", ciRsId>0?ciRsId:0L);
		
		return resultMap;
	}
	
	/**
	 * 打印事件信息页面
	 * 
	 * @param session
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/print")
	public String print(HttpSession session,HttpServletRequest request ,
			@RequestParam(value = "eventId", required = true) Long eventId,
			@RequestParam(value = "instanceId", required = false) String instanceId,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String privateFolderName = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.PRIVATE_FOLDER_NAME4_EVENT + "Print", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);
		EventDisposal event = eventDisposalService.findEventById(eventId, userInfo.getOrgCode());
		instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(eventId);
		
		if (StringUtils.isNotBlank(instanceId) && !"null".equals(instanceId)) {
			List<Map<String, Object>> queryTaskList = eventDisposalWorkflowService.queryProInstanceDetail(Long.valueOf(instanceId), IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
			map.addAttribute("taskList", queryTaskList);
		}
		if(eventId != null && eventId > 0 ) {
			//设置事件详情页面是否展示图片
			String EVENT_DETAILS_SHOW_PICTURE = this.configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, "event_detail_show_picture", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			if(StringUtils.isNotBlank(EVENT_DETAILS_SHOW_PICTURE)) {
				int pictureNum =Integer.parseInt(EVENT_DETAILS_SHOW_PICTURE);
				List<Attachment> list = attachmentService.findByBizId(eventId, ConstantValue.EVENT_ATTACHMENT_TYPE);
				
				map.addAttribute("EVENT_DETAILS_SHOW_PICTURE", EVENT_DETAILS_SHOW_PICTURE);
				map.addAttribute("attList", list);
				map.addAttribute("pictureNum", pictureNum);
			}
		}
		map.addAttribute("event", event);
		map.put("imgDomain", App.IMG.getDomain(request.getSession()));
		map.addAttribute("year", DateUtils.getToday("yyyy"));
		map.addAttribute("month", DateUtils.getToday("MM"));
		map.addAttribute("day", DateUtils.getToday("dd"));
		
		return "/zzgl/event"+privateFolderName+"/print.ftl";
	}
	
	@RequestMapping(value="/isDomain")
	public String isDomain(HttpSession session,ModelMap modelMap,
			@RequestParam(value = "callBack", required = false) String callBack, 
			@RequestParam(value = "callBackParams", required = false) String callBackParams, 
			@RequestParam(value = "source", required = false) String source){
		if(StringUtils.isNotBlank(callBack)){
			if(StringUtils.isNotBlank(callBackParams)){
				callBack += "(" + callBackParams +")";
			}else{
				callBack += "()";
			}
		}
		if(StringUtils.isNotBlank(source)){
			modelMap.addAttribute("domain", App.TOP.getDomain(session));
			modelMap.addAttribute("source", source);
		}
		modelMap.addAttribute("callBack", callBack);
		return "/zzgl/event/domain.ftl";
	}
	///---重大事件 start------------------------------------------------
	@ResponseBody
	@RequestMapping(value="/importantEvent", method=RequestMethod.POST)
	public  Map<String, Object> importantEvent(HttpSession session,
			@RequestParam(value="gridId",required=false) String gridId,
			@RequestParam(value="statusName", required=false) String statusName, 
			@RequestParam(value="type", required=false) String type, 
			@RequestParam(value="gridFlag", required=false) String gridFlag) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(gridId!=null)gridId = gridId.trim();
		//内平台的状态为01和02
		List<String> statusList=new ArrayList<String>();
		statusList.add(ConstantValue.EVENT_STATUS_REPORT );
		statusList.add(ConstantValue.EVENT_STATUS_DISTRIBUTE);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userInfo.getUserId());
		if("1".equals(gridFlag)){
			params.put("gridId", gridId);
		}
		params.put("statusList", statusList);
		params.put("orgCode", userInfo.getOrgCode());
		params.put("statusName", statusName);
		//List<String> influences=new ArrayList<String>();
		//influences.add("04");
		//params.put("influences",influences);//影响范围为重大
		params.put("tjImportantEvent","tjImportantEvent");
		cn.ffcs.common.EUDGPagination pagination = oldEventDisposalService.findEventDisposalPaginationForTaiJIang(1, 8, params);
		cn.ffcs.common.EUDGPagination yinjipagination = new cn.ffcs.common.EUDGPagination();
		String gridCode="";
		Map<String,Object> result = new HashMap<String,Object>();
		if("jjya".equals(type)){
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			gridId = String.valueOf((Long)defaultGridInfo.get(KEY_START_GRID_ID));
			params.put("gridId", gridId);
			params.remove("tjImportantEvent");
			params.put("tjImportantEvent", "tjYinjiEvent");
			params.put("statusName", "tjYinjiEvent");
			yinjipagination = oldEventDisposalService.findEventDisposalPaginationForTaiJIang(1, 8, params);
			gridCode = mixedGridInfoService.findMixedGridInfoById(Long.valueOf(gridId),false).getGridCode();
			if(gridCode.length()<=12){
				result.put("show", "yes");
			}else{
				result.put("show", "no");
			}
		}
		//List<EventDisposal>  eventList= eventDisposalService.findTodoEventList(params);
		result.put("result", "yes");
		//result.put("eventList", eventList);
		result.put("eventList", pagination.getRows());
		result.put("yinjiEventList", yinjipagination.getRows());
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/findPerson")
	public Map<String,Object> findPerson(@RequestParam(value = "name")String name,@RequestParam(value = "cardType")String cardType,
										 @RequestParam(value = "cardNumber")String cardNumber){
		Map<String,Object> returnMap = new HashMap<String,Object>();

		//根据身份证号码查找人员信息
		if ("001".equals(cardType)) {
			returnMap.put("entity", partyIndividualService.findByIdentityCard(cardNumber));
		} else {
			//根据其他证件类型和证件号码查找人员信息
			if (StringUtils.isNotBlank(cardNumber)&&StringUtils.isNotBlank(cardType)) {
				returnMap.put("entity", partyIndividualService.findByNumberAndType(cardNumber,cardType));
			}
		}

		return returnMap;
	}
	
	/**
	 * 跳转事件关联历史列表
	 * @param session
	 * @param eventId	事件id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toListEventAncestor")
	public String toListEventAncestor(HttpSession session,
			@RequestParam(value = "eventId") Long eventId,
			ModelMap map) {
		
		map.addAttribute("eventId", eventId);
		
		return "/zzgl/event/list_event_ancestor.ftl";
	}
	
	/**
	 * 加载事件关联历史列表记录
	 * @param session
	 * @param eventId	事件id
	 * @param params
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listEventAncestorData", method = RequestMethod.POST)
	public List<Map<String, Object>> listEventAncestorData(HttpSession session,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		List<Map<String, Object>> resultMapList = eventExtendService.findAncestorListByEventId(eventId, params);
		
		return resultMapList;
	}
	
	/**
	 * 导出信息
	 * @param session
	 * @param request
	 * @param response
	 * @param params
	 * @param exportName	导出文件名称
	 * @throws Exception
	 */
	@RequestMapping(value = "/toExport")
	public void toExport(HttpSession session,
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam Map<String, Object> params,
			@RequestParam(value = "exportName") String exportName
			
			) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		params.put("userOrgCode", userInfo.getOrgCode());
		params.put("userId", userInfo.getUserId());
		params.put("orgId", userInfo.getOrgId());
		params.put("orgCode", userInfo.getOrgCode());
		
		String eventIdList=request.getParameter("eventIdList");
		String imageList=request.getParameter("imageList");
		int pageSize=50;
		if(StringUtils.isNotEmpty(eventIdList)){
			params.put("eventIdList",Arrays.asList(eventIdList.split("\\|")));
			pageSize=eventIdList.split("\\|").length;
		}
		cn.ffcs.system.publicUtil.EUDGPagination p = new cn.ffcs.system.publicUtil.EUDGPagination();
		try {
			if(StringUtils.isNotEmpty(eventIdList)){//页面有传导出id时候才查isCapCurHandlerName
				p = event4WorkflowService.findEventListPagination(1, pageSize, params);
				list=(List<Map<String, Object>>)p.getRows();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			String filedomain = App.IMG.getDomain(session); 
			Map<String, Object> dataMap = new HashMap<String, Object>();
			List<String> imgsBefor=null;
			List<String> imgsAffter=null;
			List<Attachment>  attas=null;
			String eventSeq=null;
			for (Map<String, Object> eventDisposal : list) {
				imgsBefor=new ArrayList<String>();
				imgsAffter=new ArrayList<String>();
				if(StringUtils.isNotEmpty(imageList)){
					for (String id : imageList.split("\\|")) {
						if(eventDisposal.get("eventId").toString().equals(id)){
							attas=attachmentService.findByBizId( Long.valueOf(eventDisposal.get("eventId")+""), ConstantValue.EVENT_ATTACHMENT_TYPE);
							for (Attachment attachment : attas) {
								eventSeq=attachment.getEventSeq();
								if(StringUtils.isNotEmpty(eventSeq)&&isImg(attachment.getFilePath())){//图片    还有判断是否导出图片 
									switch (eventSeq) {
									case "1"://处理前
										imgsBefor.add( filedomain+ attachment.getFilePath());
										break;
									case "2"://处理中
										break;
									case "3"://处理后
										imgsAffter.add( filedomain+ attachment.getFilePath());
										break;
									}
								}
							}
						
						}
					}
					eventDisposal.put("imgsBefor", imgsBefor);
					eventDisposal.put("imgsAffter", imgsAffter);
				}
			}
			dataMap.put("list", list);
			
			String[] titles = null;  
			String[] columns =null; 
			Integer[] columnWidths = null;
			
			if(null!=params.get("excelType")){//南昌事件导出
				if(params.get("excelType").equals("3")) {
					titles=new String[] { "事件名称","事件类型","责任点位名称","事件内容","发生时间","发生地址","当前办理人（部门）","所属县区","巡访类型","现场图片"};  
					columns = new String[] { "eventName","eventClass","pointName","content","happenTimeStr","occurred","curHandlerName","gridPath","patrolTypeName","imgsBefor"}; 
					columnWidths = new Integer[] { 50, 20, 20,20,20,50, 20, 20,20,20}; 
					
				} 
			}else {
				String JIANGYIN_FOLDER_NAME = "/jiangyin",//江阴
					   XINXIAN_FOLDER_NAME = "/xinxian";//山东省聊城市莘县
				String  privateFolderName = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.PRIVATE_FOLDER_NAME4_EVENT + "List", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);
				if(JIANGYIN_FOLDER_NAME.equals(privateFolderName)){//江阴或者万宁
					titles=new String[] { "所属网格","事件标题","事件编号","事件分类","事发详址","事件描述","补充描述","入格部门名称","经度","纬度","事发时间", "办结期限", "当前状态","采集时间" ,"联系人员","当前环节","涉及人员", "影响范围","信息来源","紧急程度","采集渠道","处理前图片","处理后图片"};  
					columns = new String[] { "gridPath","eventName","code","eventClass","occurred","content","remark","amDeptName","longitude","latitude","happenTimeStr", "handleDateStr", "statusName","createTimeStr" ,"contactUser","curNodeNameZH","involvedPersion"
							,"influenceDegreeName","sourceName","urgencyDegreeName","collectWayName","imgsBefor","imgsAffter"}; 
					columnWidths = new Integer[] { 50, 20, 20,20,20,50, 20, 20,20,20,20, 20, 20,20,20,20,20, 20,20,20,20,20,10,10}; 
				} else if(XINXIAN_FOLDER_NAME.equals(privateFolderName)) {
					titles=new String[] { "所属区域","事件标题","事件编号","事件分类","事发详址","事件描述","事发时间", "办结期限", "当前状态","采集时间" ,"联系人员","当前环节","承办单位","涉及人员",
							"影响范围","信息来源","紧急程度","采集渠道","处理前图片","处理后图片"};  
					columns = new String[] { "gridPath","eventName","code","eventClass","occurred","content","happenTimeStr", "handleDateStr", "statusName","createTimeStr" ,"contactUser","curNodeNameZH","curOrgName","involvedPersion" ,"influenceDegreeName","sourceName","urgencyDegreeName","collectWayName","imgsBefor","imgsAffter"}; 
					columnWidths = new Integer[] { 50, 20, 20,20,20,50, 20, 20,20,20,20, 20, 20,20,20,20,10, 10, 10,10,10}; 
				} else {
						titles=new String[] { "所属网格","事件标题","事件编号","事件分类","事发详址","事件描述","事发时间", "办结期限", "当前状态","采集时间" ,"联系人员","当前环节","承办单位","涉及人员",
								"影响范围","信息来源","紧急程度","采集渠道","处理前图片","处理后图片"};  
						columns = new String[] { "gridPath","eventName","code","eventClass","occurred","content","happenTimeStr", "handleDateStr", "statusName","createTimeStr" ,"contactUser","curNodeNameZH","curOrgName","involvedPersion" ,"influenceDegreeName","sourceName","urgencyDegreeName","collectWayName","imgsBefor","imgsAffter"}; 
						columnWidths = new Integer[] { 50, 20, 20,20,20,50, 20, 20,20,20,20, 20, 20,20,20,20,10, 10, 10,10,10}; 
				}
			}

             //导出excel
			JXLExcelUtil t= new JXLExcelUtil<EventDisposal>();
			Map map = new HashMap();  
			map.put("list", list);         
			map.put("columns", columns);  
			map.put("titles", titles);  
			map.put("widths", columnWidths);  
			map.put("name", exportName);
			t.buildExcelDocument(map, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 是否图片
	 * @param str
	 * @return
	 */
	private boolean isImg(String filePath){
		boolean isImg=false;
		int suffixStartIndex=filePath.lastIndexOf(".");
		String imgSuffix = filePath.substring(suffixStartIndex + 1).toLowerCase();
		if(filePath.indexOf(imgSuffix)>0){
			if(imgSuffix.matches("^[(jpg)|(png)|(gif)|(jpeg)]+$"))
			isImg=true;
		}
		return isImg;
		
	}

	/**
	 * 跳转重复事件列表
	 * @param session
	 * @param eventId	事件id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toListEventDuplication")
	public String toListEventDuplication(HttpSession session,
			@RequestParam(value = "leadEventId") Long leadEventId,
			ModelMap map) {
		
		map.addAttribute("leadEventId", leadEventId);
		
		return "/zzgl/event/list_event_duplication.ftl";
	}
	
	/**
	 * 加载事件关联历史列表记录
	 * @param session
	 * @param eventId	事件id
	 * @param params
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listEventDuplicationData", method = RequestMethod.POST)
	public List<Map<String, Object>> listEventDuplicationData(HttpSession session,
			@RequestParam(value = "leadEventId") Long leadEventId,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		List<Map<String, Object>> resultMapList = eventDuplicationService.findDuplicatedEventList(params, userInfo);
		
		return resultMapList;
	}
	
	/**
	 * 获取周边责任点位
	 * @param session
	 * @param params
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findResposibilityPoints", method = RequestMethod.POST)
	public List<PointInfo> findResposibilityPoints(HttpSession session,
			@RequestParam(value = "x") Double x,
			@RequestParam(value = "y") Double y,
			@RequestParam(value = "limits") Double limits,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		List<PointInfo> findNeighPosition=new ArrayList<PointInfo>();
		
		if(null!=x&&null!=y) {
			findNeighPosition = pointInfoService.findNeighPosition(x, y, limits);
		}
				
		return findNeighPosition;
		
	}
	
	/**
	 * 获取网格中心点经纬度
	 * @param session
	 * @param params
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findGridCenter", method = RequestMethod.POST)
	public List<ArcgisInfoOfGrid> findGridCenter(HttpSession session,
			@RequestParam(value = "gridId") String gridId,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		
		return arcgisInfoService.getArcgisDataOfGridForOuter(Long.valueOf(gridId), null);
	}
	
	
	/**
	 * 获取责任点位详细信息
	 * @param session
	 * @param params
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/searchResposibilityPointInfo", method = RequestMethod.POST)
	public Map<String,Object> searchResposibilityPointInfo(HttpSession session,
			@RequestParam(value = "pointId") Long pointId,
			@RequestParam(value = "x") Double x,
			@RequestParam(value = "y") Double y,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String,Object> result=new HashMap<String,Object>();

		// 根据经纬度转换出gridCode
		if(null!=x&&null!=y) {
			
			Map<String, Object> gridParams = new HashMap<>();
			gridParams.put("x", x.toString());
			gridParams.put("y", y.toString());
			List<CoordinateInverseQueryGridInfo> gridInfos = coordinateInverseQueryService.findGridInfo(gridParams);
			if (null != gridInfos && gridInfos.size() > 0) {
				
				CoordinateInverseQueryGridInfo gridInfo = gridInfos.get(0);
				MixedGridInfo gridInfoById = null;
				Long gridId = gridInfo.getGridId();
				while (true) {
					gridInfoById = mixedGridInfoService.findMixedGridInfoById(gridId, true);
					
					if (null != gridInfoById) {
						
						if (gridInfoById.getGridLevel() <= 4) {
							break;
						} else {
							gridId = gridInfoById.getParentGridId();
						}
					} else {
						result.put("msg", "该经纬度下未找到所属网格");
						break;
					}
				}
				if (null != gridInfoById) {
					result.put("gridName", gridInfoById.getGridName());
					result.put("gridId", gridInfoById.getGridId());
					result.put("gridCode", gridInfoById.getGridCode());
				}
				
			} else {
				result.put("msg", "该经纬度下未找到所属网格");
			}
			
		}else {
			result.put("msg", "传入的经纬度不能为空");
		}
		
		//判断如果点位Id为0则事件类型需自选，且联系人，联系人电话选择所属县区的管理员
		if(pointId==0L) {
			if(null!=x&&null!=y) {
				
				Map<String, Object> searchParams = new HashMap<String, Object>();
				
				try {
					List<UserBO> userBo = coordinateInverseQueryService.findUserInfoByRegionCodeAndPosition(x.toString(), y.toString(), userInfo.getOrgCode(), searchParams);
					if(null!=userBo&&userBo.size()>0) {
						result.put("pointManager", userBo.get(0).getPartyName());
						result.put("pmTel", userBo.get(0).getVerifyMobile());
					}else {
						result.put("msg", "该经纬度下未找到所属区县巡防团负责人,请手动填写。");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				result.put("msg", "传入的经纬度不能为空");
			}
		}else {
			PointInfo searchById = pointInfoService.searchById(pointId);
			result.put("pointManager", searchById.getPointManager());
			result.put("pmTel", searchById.getPmTel());
			try {
				result.put("eventType", baseDictionaryService.changeCodeToName(ConstantValue.EVENT_TYPE_PCODE,
						searchById.getPointType(), userInfo.getOrgCode(), true));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		

		return result;
	}
	
	/**
	 * 依据json串跳转事件新增页面
	 * @param session
	 * @param eventJson
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddEventByDisputeJson")
	public String toAddEventByDisputeJson(HttpSession session,HttpServletRequest request, 
			@RequestParam(value = "eventJson", required = false) String eventJson,
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		EventDisposal event = new EventDisposal();
		String typesForList = "",
			   trigger = "",
			   attachmentIds = "";
		boolean isReport = true, isShowSaveBtn = true, isShowCloseBtn = true, isRemoveTypes = false;
		long mediationId = 0l;
		if(StringUtils.isNotBlank(eventJson)){
			Map<String, Object> eventMap = null;
			try{
				eventMap = JsonUtils.json2Map(eventJson);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(CommonFunctions.isBlank(eventMap, "orgCode")) {
				eventMap.put("orgCode", userInfo.getOrgCode());
			}
			event = eventDisposalDockingService.jsonToEvent(eventMap);
			attachmentIds = event.getAttachmentIds();
			
			if(event.getGridId()==null || event.getGridId().equals(-99L)){
				Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
				String gridId = defaultGridInfo.get(KEY_START_GRID_ID).toString();
				String gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString();
				String gridCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
				
				event.setGridId(Long.valueOf(gridId));
				event.setGridName(gridName.trim());
				event.setGridCode(gridCode);
			}
			if(CommonFunctions.isNotBlank(eventMap, "callBack")){
				map.addAttribute("callBack", eventMap.get("callBack"));
			}
			if(CommonFunctions.isNotBlank(eventMap, "typesForList")){
				typesForList = eventMap.get("typesForList").toString();
			}
			if(CommonFunctions.isNotBlank(eventMap, "trigger")){
				trigger = eventMap.get("trigger").toString();
			}
			if(CommonFunctions.isNotBlank(eventMap, "isRemoveTypes")) {
				isRemoveTypes = Boolean.valueOf(eventMap.get("isRemoveTypes").toString());
			}
			
//			if(CommonFunctions.isNotBlank(eventMap, "iframeUrl")){
//				String iframeUrl = eventMap.get("iframeUrl").toString();
//				
//				if(iframeUrl.contains("$ZZGRID_DOMAIN")) {
//					iframeUrl = iframeUrl.replace("$ZZGRID_DOMAIN", App.ZZGRID.getDomain(session));
//				}
//				
//				map.addAttribute("iframeUrl", iframeUrl);
//			}
			if(CommonFunctions.isNotBlank(eventMap, "iframeCloseCallBack")){
				map.addAttribute("iframeCloseCallBack", eventMap.get("iframeCloseCallBack"));
			}
			if(CommonFunctions.isNotBlank(eventMap, "advice")) {
				map.addAttribute("advice", eventMap.get("advice").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "isReport")) {
				isReport = Boolean.valueOf(eventMap.get("isReport").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "isShowSaveBtn")) {
				isShowSaveBtn = Boolean.valueOf(eventMap.get("isShowSaveBtn").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "isShowCloseBtn")) {
				isShowCloseBtn = Boolean.valueOf(eventMap.get("isShowCloseBtn").toString());
			}
			if(CommonFunctions.isNotBlank(eventMap, "mediationId")) {
				mediationId = Long.valueOf(eventMap.get("mediationId").toString()) ;
			}
			if(StringUtils.isBlank(event.getType())){
				String eventDefaultType = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger+ConstantValue.DEFAULT_TYPE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode()),
					   eventDefaultTypeName = "";
				
				if(StringUtils.isNotBlank(trigger) && StringUtils.isBlank(typesForList)) {
					if(isRemoveTypes) {
						typesForList = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
					} else {
						typesForList = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
					}
				}
				
				if(StringUtils.isNotBlank(eventDefaultType)) {
					eventDefaultTypeName = baseDictionaryService.changeCodeToName(ConstantValue.BIG_TYPE_PCODE, eventDefaultType, userInfo.getOrgCode(), true);
				}
				
				event.setTypeName(eventDefaultTypeName);
				event.setType(eventDefaultType);
			}
			if(StringUtils.isNotBlank(attachmentIds)) {
				map.addAttribute("attachmentIds", attachmentIds);
			}
			
			map.addAttribute("startGridId", event.getGridId());
			map.addAttribute("gridCode",event.getGridCode());
		}
		
		String forwardUrl = this.toAddEventByDispute(session, request,event.getEventId(),mediationId, trigger, typesForList, isRemoveTypes, map);
		
		if(StringUtils.isNotBlank(eventJson)){
			if(StringUtils.isBlank(event.getContactUser())) {
				event.setContactUser(userInfo.getPartyName());
			}
			if(StringUtils.isBlank(event.getTel())) {
				event.setTel(userInfo.getVerifyMobile());
			}
			map.addAttribute("event", event);
		}
		map.addAttribute("mediationId", mediationId);
		map.addAttribute("module", ConstantValue.DISPUTE_MEDIATION); // 模块地图
		map.addAttribute("isReport", isReport);
		map.addAttribute("isShowSaveBtn", isShowSaveBtn);
		map.addAttribute("isShowCloseBtn", isShowCloseBtn);
		map.addAttribute("rootGridId", event.getGridId());
		
		return forwardUrl;
	}


	/**
	 * 跳转事件新增页面
	 * @param session
	 * @param eventId		事件Id
	 * @param trigger		事件类别过滤触发条件，对应功能编码为types，设定默认值为空串，是为了防止未设置trigger时，其值会被设置为字符串null
	 * @param typesForList	可查看的事件类别，以英文逗号相连
	 * @param isRemoveTypes	是否移除trigger、typesForList对应的事件类别，true为移除
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddEventByDispute")
	public String toAddEventByDispute(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "eventId", required = false) Long eventId,
			@RequestParam(value = "mediationId", required = false) Long mediationId,
			@RequestParam(value = "trigger", required = false, defaultValue="") String trigger,
			@RequestParam(value = "typesForList", required = false) String typesForList,
			@RequestParam(value = "isRemoveTypes", required = false, defaultValue = "false") boolean isRemoveTypes,
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		EventDisposal event = null;
		
		if(eventId != null && eventId > 0){
			event = eventDisposalService.findEventById(eventId, userInfo.getOrgCode());
			
			if(event != null) {
				String eventInvolvedPeople = event.getEventInvolvedPeople();
				Long instanceId = -1L;
				
				if(StringUtils.isNotBlank(eventInvolvedPeople)) {
					/*
					 * String[] ipArray = eventInvolvedPeople.split("；"), item = null; String ip =
					 * null; InvolvedPeople involvedPeople = null;
					 */

					map.addAttribute("involvedPeopleList", involvedPeopleService.findInvolvedPeopleListByBiz(eventId, InvolvedPeople.BIZ_TYPE.ZHSQ_EVENT.getBizType()));
				}
				
				instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
				if(instanceId != null && instanceId > 0) {
					map.addAttribute("instanceId", instanceId);
				}
			}
		} else {
			event = new EventDisposal();
			
			String gridId = defaultGridInfo.get(KEY_START_GRID_ID).toString(),
				   gridName = defaultGridInfo.get(KEY_START_GRID_NAME).toString(),
				   gridCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString(),
				   eventDefaultTypeName = "",
				   eventDefaultType = "";
			
			event.setHappenTimeStr(DateUtils.getNow());
			event.setGridId(Long.valueOf(gridId));
			event.setGridName(gridName.trim());
			event.setGridCode(gridCode);
			
			eventDefaultType = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger+ConstantValue.DEFAULT_TYPE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
			
			if(StringUtils.isNotBlank(trigger) && StringUtils.isBlank(typesForList)) {
				if(isRemoveTypes) {
					typesForList = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
				} else {
					typesForList = configurationService.changeCodeToValue(ConstantValue.TYPES, trigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
				}
			}
			
			if(StringUtils.isNotBlank(eventDefaultType)) {
				eventDefaultTypeName = baseDictionaryService.changeCodeToName(ConstantValue.BIG_TYPE_PCODE, eventDefaultType, userInfo.getOrgCode(), true);
			}
			
			event.setTypeName(eventDefaultTypeName);//设置默认的事件类型
			event.setType(eventDefaultType);
			
			event.setContactUser(userInfo.getPartyName());
			event.setTel(userInfo.getVerifyMobile());
		}
		
		if(StringUtils.isNotBlank(typesForList)) {
			String[] typesTemp = typesForList.split(",");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
			param.put("orgCode", userInfo.getOrgCode());
			
			BaseDataDict dataDict = null;
			StringBuffer typesDictCode = new StringBuffer("");
			
			for(int index = 0, len = typesTemp.length; index < len; index++) {
				String typeTemp = typesTemp[index];
				String dictCode = "";
				
				if(StringUtils.isNotBlank(typeTemp)){
					param.put("dictGeneralCode", typeTemp);
					dataDict = baseDictionaryService.findByCodes(param);
					
					if(dataDict != null) {
						dictCode = dataDict.getDictCode();
						if(StringUtils.isNotBlank(dictCode)){
							typesDictCode.append(",").append(dictCode);
						}
					}
				}
			}
			
			if(typesDictCode.length() > 0) {
				map.addAttribute("typesDictCode", "'" + typesDictCode.substring(1).replaceAll(",", "','") + "'");
				map.addAttribute("typesForList", typesForList);
				map.addAttribute("isRemoveTypes", isRemoveTypes);
			}
		}
		
		map.addAttribute("bigTypePcode", ConstantValue.BIG_TYPE_PCODE);
		map.addAttribute("urgencyDegreePcode", ConstantValue.URGENCY_DEGREE_PCODE);
		map.addAttribute("influenceDegreePcode", ConstantValue.INFLUENCE_DEGREE_PCODE);
		map.addAttribute("sourcePcode", ConstantValue.SOURCE_PCODE);
		map.addAttribute("involvedNumPcode", ConstantValue.INVOLVED_NUM_PCODE);
		
		map.addAttribute("event", event);
		
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.addAttribute("ORG_CODE", userInfo.getOrgCode());//标准地址库应用
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(Long.valueOf(defaultGridInfo.get(KEY_START_GRID_ID).toString()), false);
		String result = "";
		if(gridInfo.getMapType()!=null && gridInfo.getMapType().equals("004")) {
			result = "gis"; 
		}
		map.addAttribute("result", result);
		map.addAttribute("EVENT_ATTACHMENT_TYPE", ConstantValue.EVENT_ATTACHMENT_TYPE);//上传附件的类型
		
		String isUploadHandlingPic = configurationService.turnCodeToValue(ConstantValue.IS_UPLOAD_HANDLING_PIC, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUploadHandlingPic", Boolean.valueOf(isUploadHandlingPic));
		
		if (eventId != null && !eventId.equals(-1L)) {
			map.addAttribute("markerOperation", ConstantValue.EDIT_MARKER); // 编辑标注操作
		} else {
			map.addAttribute("markerOperation", ConstantValue.ADD_MARKER); // 添加标注操作
		}
		
		map.put("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		map.put("maxHappenTime", DateUtils.getNow());//发生时间上限时间 yyyy-mm-dd hh:mi:ss，当happenTimeStr为空时，使用

		//String isJinJiang = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.PRIVATE_FOLDER_NAME4_EVENT, IFunConfigurationService.CFG_TYPE_FACT_VAL,  userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.put("fileDomain", App.SQFILE.getDomain(request.getSession()));
		map.put("imgDomain", App.IMG.getDomain(request.getSession()));
		//将附件传回前端
		List<Attachment> att = attachmentService.findByBizId(mediationId, attachmentType);
		map.addAttribute("attList", formateAttList(att));
		
		//return "/zzgl/event"+isJinJiang+"/add_event_000.ftl";
		//return "/zzgl/event/add_event_000.ftl";
		return "/zzgl/dispute/mediation_9x/dafeng/report_event.ftl";
	}
	
	/**
	 * 用Attachment类中的title字段表示其后缀，方便页面上展示
	 * @param attList
	 * @return
	 */
	private List<Attachment> formateAttList(List<Attachment> attList) {
		for (Attachment att : attList) {
			att.setTitle(getFileSuffixByPath(att.getFilePath()));
		}
		return attList;
	}
	
	private String getFileSuffixByPath(String path) {
		// 获取文件后缀名并转化为写，用于后续比较
		String fileType = path.substring(path.lastIndexOf(".") + 1,path.length()).toLowerCase();
		
		if ("bmp|jpg|jpeg|png|gif".indexOf(fileType) > -1) {
			return "image";
		} else if ("xls|xlsx|xlsm|xltx".indexOf(fileType) > -1) {
			return "excel";
		} else if("docx|docm|dotx|doc".indexOf(fileType) > -1) {
			return "word";
		} else if("pptx|pptm|ppsx|ppt".indexOf(fileType) > -1) {
			return "ppt";
		} 
		return "other";
	}
	
	/**
	 * 保存事件并上报
	 * @param session
	 * @param ids			附件id
	 * @param event			事件信息
	 * @param userInfo
	 * @param params
	 * 			advice		办理意见
	 * 			isSendSms	是否发送短信
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/saveEventAndReportDispute/{type}", method = RequestMethod.POST)
	public Map<String, Object> saveEventAndReportDispute(
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "attachmentId", required = false) Long[] ids,
			@ModelAttribute(value = "event") EventDisposal event, 
			@RequestParam(value = "userInfo", required=false) UserInfo userInfo,
			@RequestParam Map<String, Object> params,
			ModelMap map
			,@RequestParam(value="areaType", required=false) String areaType
			,@RequestParam(value="wid", required=false) Long wid
			,@RequestParam(value="mapt", required=false) Integer mapt
			,@RequestParam(value="x", required=false) Float x
			,@RequestParam(value="y", required=false) Float y
			,@RequestParam(value="levnum", required=false) String levnum
			,@RequestParam(value="hs", required=false) String hs
			,@RequestParam(value="color", required=false) String color
			,@RequestParam(value="mapCenterLevel", required=false) Integer mapCenterLevel
			,@RequestParam(value="gjColor", required=false) String gjColor) throws Exception {
	
		if(!StringUtils.isBlank(request.getParameter("resMarker.x"))){
			x =Float.valueOf(request.getParameter("resMarker.x"));
		}
		if(!StringUtils.isBlank(request.getParameter("resMarker.y"))){
			y =Float.valueOf(request.getParameter("resMarker.y"));
		}
		if(!StringUtils.isBlank(request.getParameter("resMarker.mapType"))){
			mapt =Integer.valueOf(request.getParameter("resMarker.mapType"));
		}
		
		String eventSeq = null;
		if(!StringUtils.isBlank(request.getParameter("eventSeq"))){
			eventSeq = request.getParameter("eventSeq");
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long eventId = -1L;
		
		if(userInfo == null){
			userInfo = (UserInfo)session.getAttribute(ConstantValue.USER_IN_SESSION);
		}
		
		params.put("event", event);
		params.put("userInfo", userInfo);
		
		resultMap = eventDisposalDockingService.saveEventDisposalAndReport(params);
		String id = request.getParameter("id");//矛盾纠纷主键
		if(StringUtils.isNotEmpty(id)){
			eventId = (Long) resultMap.get("eventId");
			DisputeMediation disputeMediation = new DisputeMediation();
			disputeMediation.setMediationId(Long.valueOf(id));
			disputeMediation.setEventId(eventId);
			disputeMediationService.updateByPrimaryKeySelective(disputeMediation);
			//卓尼矛盾纠纷 上报添加积分
			IdRecord idRecord = new IdRecord();
			idRecord.setPartyMemberId(userInfo.getUserId());
			idRecord.setOrgCode(userInfo.getOrgCode());
			idRecord.setRegionCode(userInfo.getInfoOrgList().get(0).getOrgCode());
			idRecord.setCreator(userInfo.getUserId());
			idRecord.setSubmodularCode("F-OA004ZHN002");
			idRecord.setTableType("GRID_USER");
			idRecord.setBizId(disputeMediation.getMediationId());
			idRecord.setRuleType("01");
			idRecord.setInteralReason("矛盾纠纷上报采集");
			idRecord.setInteralDate(new Date());
			boolean flag = integralRuleService.rewardPoints(idRecord, null);
			if(!flag){
				if(!flag && logger.isErrorEnabled()) {
					logger.error("积分添加失败！参数如下：" + JsonUtils.beanToJson(idRecord));
				}
			}

		}
		Long disputeMediationOrgId = userInfo.getOrgId(),
				createUserId = userInfo.getUserId();
				String orgCode = "";
				System.out.println("11111111111111111111");
				if(createUserId != null && createUserId > 0) {
					if(disputeMediationOrgId != null && disputeMediationOrgId > 0) {
						OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(disputeMediationOrgId);
						if(orgInfo != null) {
							orgCode = orgInfo.getOrgCode();  
						}
					}
					if(StringUtils.isNotBlank(orgCode)) {
						String userPartyName = "";
						UserBO userBO = userManageService.getUserInfoByUserId(createUserId);
						UserDetailScore userDetailScore = new UserDetailScore();
						
						if(userBO != null) {
							userPartyName = userBO.getPartyName();
						}
						userDetailScore.setUserId(createUserId);
						userDetailScore.setUserName(userPartyName);
						userDetailScore.setOrgCode(orgCode);
						userDetailScore.setCreateBy(createUserId);
						
						userDetailScore.setScoreType("D1");
						
						userDetailScore.setRemark(String.valueOf(event.getEventId()));
						
						userScoreService.insertUserDetailScore(userDetailScore);
					}
				}
		
		try {
			if (event.getAttList() != null && event.getAttList().size() > 0) {
				List<Attachment> newList = new ArrayList<>();
				for (Attachment att : event.getAttList()) {
					if(att.getFilePath() == null){
						continue;
					}
					att.setAttachmentType(ConstantValue.EVENT_ATTACHMENT_TYPE);
					att.setBizId(event.getEventId());
					att.setCreateTime(new Date());
					att.setCreatorId(userInfo.getUserId());
					att.setCreatorName(userInfo.getUserName());
					att.setEventSeq(eventSeq);
					newList.add(att);
				}
				attachmentService.saveAttachment(newList);
			}
		} catch (Exception e) {
			map.put("msg", "保存失败");
		}
		if(x != null ){
			saveDrawAreaDispute(session, map, request,areaType,wid,mapt,x, y,levnum,hs,color,mapCenterLevel,gjColor);
		}
		return resultMap;
	}
	
	
	public int saveDrawAreaDispute(HttpSession session, ModelMap map, HttpServletRequest request
			,@RequestParam(value="areaType", required=false) String areaType
			,@RequestParam(value="wid", required=false) Long wid
			,@RequestParam(value="mapt", required=false) Integer mapt
			,@RequestParam(value="x", required=false) Float x
			,@RequestParam(value="y", required=false) Float y
			,@RequestParam(value="levnum", required=false) String levnum
			,@RequestParam(value="hs", required=false) String hs
			,@RequestParam(value="color", required=false) String color
			,@RequestParam(value="mapCenterLevel", required=false) Integer mapCenterLevel
			,@RequestParam(value="gjColor", required=false) String gjColor
			) throws Exception{
		int result = 0;
		if("building".equals(areaType)){//画建筑物
			GisMapBuidingInfo bean = gisInfoService.findGisMapBuidingInfoByBuildingIdAndMapt(wid, mapt);
			if(bean==null){
				bean = new GisMapBuidingInfo();
				bean.setBuildingId(wid);
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				
				result = gisInfoService.insertGisMapBuidingInfo(bean);
				
				///记录日志
				if(result>0){
					bean = gisInfoService.findGisMapBuidingInfoByBuildingIdAndMapt(wid, mapt);
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getBuildingName();
					logService.savelog2(request, LogModule.mapDataForBuilding, ActionType.insert, 
							bean.getId(), recordName, null, valueAfter);
				}
			}else{
				String valueBefore = JSONObject.fromObject(bean).toString();
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				result = gisInfoService.updateGisMapBuidingInfo(bean);
				///记录日志
				if(result>0){
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getBuildingName();
					logService.savelog2(request, LogModule.mapDataForBuilding, ActionType.update, 
							bean.getId(), recordName, valueBefore, valueAfter);
				}
			}
		}else if("grid".equals(areaType)){//画网格
			GisMapGridInfo bean = gisInfoService.findGisMapGridInfoByGridIdAndMapt(wid, mapt);
			
			if(bean==null){
				bean = new GisMapGridInfo();
				bean.setGridId(wid);
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				bean.setMapCenterLevel(mapCenterLevel);
				bean.setGjColor(gjColor);
				result = gisInfoService.insertGisMapGridInfo(bean);
				///记录日志
				if(result>0){
					bean = gisInfoService.findGisMapGridInfoByGridIdAndMapt(wid, mapt);
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getGridName();
					logService.savelog2(request, LogModule.mapDataForGrid, ActionType.insert, 
							bean.getId(), recordName, null, valueAfter);
				}
			}else{
				String valueBefore = JSONObject.fromObject(bean).toString();
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				bean.setMapCenterLevel(mapCenterLevel);
				bean.setGjColor(gjColor);
				result = gisInfoService.updateGisMapGridInfo(bean);
				///记录日志
				if(result>0){
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getGridName();
					logService.savelog2(request, LogModule.mapDataForGrid, ActionType.update, 
							bean.getId(), recordName, valueBefore, valueAfter);
				}
			}
		}else if("segmentgrid".equals(areaType)) {
			GisMapSegmentGridInfo bean = gisInfoService.findGisMapSegmentGridInfoBySegmentGridIdAndMapt(wid, mapt);
			if(bean == null) {
				bean = new GisMapSegmentGridInfo();
				bean.setSegmentGridId(wid);
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				result = gisInfoService.insertGisMapSegmentGridInfo(bean);
				///记录日志
				if(result>0){
					bean  = gisInfoService.findGisMapSegmentGridInfoBySegmentGridIdAndMapt(wid, mapt);
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getSegmentGridName();
					logService.savelog2(request, LogModule.mapDataForSegmentGrid, ActionType.insert, 
							bean.getId(), recordName, null, valueAfter);
				}
			}else {
				String valueBefore = JSONObject.fromObject(bean).toString();
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				result = gisInfoService.updateGisMapSegmentGridInfo(bean);
				///记录日志
				if(result>0){
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getSegmentGridName();
					logService.savelog2(request, LogModule.mapDataForSegmentGrid, ActionType.update, 
							bean.getId(), recordName, valueBefore, valueAfter);
				}
			}
		}else{ //其他资源标注信息
			ResMarker marker = null;
			ActionType action;
			Long keyId=null;
			String valueBefore =null, valueAfter=null;
			List<ResMarker> list = resGisMarkerService.findResMarkerByParam(areaType, wid, mapt+"");
			if(list!=null && list.size()>0){
				marker = list.get(0);
				keyId = marker.getMarkerId();
				action = ActionType.update;
				valueBefore = JSONObject.fromObject(marker).toString();
			}else{
				marker = new ResMarker();
				action = ActionType.insert;
				valueBefore = null;
			}
			
			marker.setResourcesId(wid);
			String markerType = ConstantValue.MARKER_TYPE_DISPUTE_MEDIATION;
			marker.setMarkerType(markerType);
			marker.setMapType(mapt+"");
			marker.setX(x+"");
			marker.setY(y+"");
			marker.setHs(hs);
			marker.setColordesc(color);
			marker.setLevnum(levnum);
			boolean re = resGisMarkerService.saveOrUpdateResMarker(marker);
			if(re){
				result++;
				if(keyId==null){
					List<ResMarker> list2 = resGisMarkerService.findResMarkerByParam(areaType, wid, mapt+"");
					keyId = list2.get(0).getMarkerId();
				}
				///记录日志
				logService.savelog2(request, LogModule.mapDataForResMarker, action, 
						keyId, areaType+ "--"+ keyId, valueBefore, valueAfter);
			}
		}
		return result;
	}
	
	
	
	private void getEventExtendInfo(Long eventId,ModelMap map,UserInfo userInfo){
		
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("isFormat", "1");
		params.put("orgCode", userInfo.getOrgCode());
		
		//获取事件额外信息
		Map<String, Object> findEventExtendByEventId = eventExtendService.findEventExtendByEventId(eventId,params);
		if(null!=findEventExtendByEventId&&findEventExtendByEventId.size()>0) {
			
			Map<String, Object> eventExtend=new HashMap<String, Object>();
			eventExtend.put("patrolType", findEventExtendByEventId.get("patrolType"));
			eventExtend.put("patrolTypeName", findEventExtendByEventId.get("patrolTypeName"));
			eventExtend.put("pointSelection", findEventExtendByEventId.get("pointSelection"));
			eventExtend.put("pointSelectionName", findEventExtendByEventId.get("pointSelectionName"));
			eventExtend.put("isNotice", findEventExtendByEventId.get("isNotice"));
			map.addAllAttributes(eventExtend);
			
		}
		
		//获取额外的标签信息
		this.getEventLabelInfo(eventId, map, userInfo);
		
	}
	
	
	
	private void getEventLabelInfo(Long eventId,ModelMap map,UserInfo userInfo){
		String labelModel = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.SHOW_LABEL_INPUT, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		Map<String,Object> eventLabelMap=new HashMap<String,Object>();
		try {
			Map<String,Object> searchLabelParams=new HashMap<String,Object>();
			searchLabelParams.put("labelModel", labelModel);
			List<Map<String, Object>> eventLabelList = eventLabelService.searchListByParam(searchLabelParams);
			for (Map<String, Object> labelMap : eventLabelList) {
				eventLabelMap.put(labelMap.get("labelId").toString(), labelMap.get("labelName").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(eventLabelMap.size()>0) {
			String eventLabelName="";
			String eventLabelIds="";
			try {
				List<Map<String, Object>> eventLabelRelaList = eventLabelRelaService.searchByEventId(eventId, new HashMap<String,Object>());
				for (int i = 0, j = eventLabelRelaList.size();i<j; i++) {
					String key=eventLabelRelaList.get(i).get("LABEL_ID").toString();
					if(eventLabelMap.containsKey(key)) {
						if(i==(j-1)) {
							eventLabelName+=eventLabelMap.get(key);
							eventLabelIds+=key;
						}else {
							eventLabelName+=eventLabelMap.get(key)+",";
							eventLabelIds+=key+",";
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			map.put("eventLabelName", eventLabelName);
			map.put("eventLabelIds", eventLabelIds);
		}
		
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
	private boolean saveLog(HttpServletRequest request,Long eventId,  String action, UserInfo userInfo, EventDisposal eventBefore, EventDisposal eventAfter) {
		String valueBefore = "", valueAfter = "",
				userOrgCode = userInfo.getOrgCode();

		if(eventBefore != null) {
			valueBefore = JSONObject.fromObject(eventBefore).toString();
		}
		if(eventAfter != null) {
			formatEventInfo(eventAfter, userOrgCode);

			valueAfter = JSONObject.fromObject(eventAfter).toString();
		}

		return saveLog(request,userInfo, action, eventId, valueBefore, valueAfter);
	}

	/**
	 * 记录事件操作日志-增删改
	 * @param action	操作类型： 新增:I; 修改:U; 查询:Q, 删除:D, 导出:EXP, 导入:IMP;查看：V
	 * @param recordId 事件id
	 * @param valueBefore 事件修改之前值
	 * @param valueAfter 事件修改之后值
	 * */
	private boolean saveLog(HttpServletRequest request,UserInfo userInfo, String action, Long recordId,
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
		dataLog.setMemoInfo(tableName);
		dataLog.setAppCode(appCode);
		dataLog.setChangeTime(Calendar.getInstance().getTime());
		dataLog.setChangeValeAfter(valueAfter);
		dataLog.setChangeValeBefore(valueBefore);
		dataLog.setModelCode(modelCode);
		dataLog.setOperateId(operaId);
		dataLog.setRecordId(String.valueOf(recordId));
		dataLog.setTableName(tableName);
		dataLog.setOrgCode(userInfo.getOrgCode());
		dataLog.setUserId(userInfo.getUserId());
		dataLog.setUserName(userInfo.getUserName());
		dataLog.setClientType("PC");
		dataLog.setOperateIp(CommonFunctions.getRemoteIp(request));
		String token = (String) request.getSession().getAttribute(ConstantValue.SESSION_TOKEN_KEY);
		dataLog.setToken(token);
		try {
			logResult = userLogService.saveUserOperateLog(dataLog);
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
	
	/**
	 * 事件列表字典查询条件子项过滤设置
	 * @param params
	 * @param userOrgCode	组织编码
	 * @return
	 */
	@SuppressWarnings("serial")
	private Map<String, Object> dictItemHandle(Map<String, Object> params, String userOrgCode) {
		String itemValue = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, String> DICT_ITEM = new HashMap<String, String>() {
			{
				put("source", ConstantValue.SOURCE_PCODE);
				put("collectWay", ConstantValue.COLLECT_WAY_PCODE);
				put("attrFlag", ConstantValue.ATTR_FLAG_PCODE);
				put("urgencyDegree", ConstantValue.URGENCY_DEGREE_PCODE);
				put("influenceDegree", ConstantValue.INFLUENCE_DEGREE_PCODE);
			}
		};
		String dictItemKey = null;
		
		for(String key : DICT_ITEM.keySet()) {
			dictItemKey = key + "4List";
			itemValue = null;
			
			if(CommonFunctions.isNotBlank(params, dictItemKey)) {
				itemValue = params.get(dictItemKey).toString();
			}
			
			if(StringUtils.isNotBlank(itemValue)) {
				String[] itemArray = null;
				Map<String, Object> param = new HashMap<String, Object>();
				BaseDataDict dataDict = null;
				StringBuffer dictCodeBuffer = new StringBuffer("");
				String dictCode = null;
				
				itemValue = itemValue.trim();
				itemArray = itemValue.split(",");
				param.put("dictPcode", DICT_ITEM.get(key));
				param.put("orgCode", userOrgCode);
				
				for(String item : itemArray) {
					dictCode = null;
					
					if(StringUtils.isNotBlank(item)) {
						param.put("dictGeneralCode", item);
						dataDict = baseDictionaryService.findByCodes(param);
						
						if(dataDict != null) {
							dictCode = dataDict.getDictCode();
							if(StringUtils.isNotBlank(dictCode)){
								dictCodeBuffer.append(",").append(dictCode);
							}
						}
					}
				}
				
				if(dictCodeBuffer.length() > 0) {
					resultMap.put(key + "DictCode", "'" + dictCodeBuffer.substring(1).replaceAll(",", "','") + "'");
					resultMap.put(dictItemKey, itemValue);
				}
			}
		}
		
		return resultMap;
	}
	
}
