package cn.ffcs.zhsq.bigScreen.jiangYin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.common.FileUtils;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.ArrayUtil;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

@Controller
@RequestMapping("/zhsq/event/centralControlCabinController")
public class CentralControlCabinController extends ZZBaseController {

	// 事件工作流服务
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	// 事件服务
	@Autowired
	private IEventDisposalService eventDisposalService;
	// 附件服务
	@Autowired
	private IAttachmentService attachmentService;
	
	@Autowired
	private IEvent4WorkflowService event4WorkflowService;
	
	//功能配置
	@Autowired
	private IFunConfigurationService configurationService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;

	// 附件类型
	public static final Pattern PIC_PATTERN = Pattern.compile("jpg|png|bmp|gif|jpeg|webp");
	public static final Pattern VIDEO_PATTERN = Pattern.compile("mp4");
	public static final Pattern SOUND_PATTERN = Pattern.compile("amr|mp3");

	/**
	 * 获取事件详情
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventInfo")
	public Map<String,Object> getEventInfo(HttpSession session, ModelMap map,
			@RequestParam(value = "eventId", required = false, defaultValue = "-1") Long eventId,
			@RequestParam(value = "nofull", required = false) String nofull,
			@RequestParam(value = "caseId", required = false, defaultValue = "-1") Long caseId) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String,Object> result=new HashMap<String,Object>();
		EventDisposal event = new EventDisposal();
		if (eventId != null && eventId > 0) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			Long instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
			list = eventDisposalWorkflowService.queryProInstanceDetail(instanceId,
					IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
			event = eventDisposalService.findEventById(eventId, userInfo.getOrgCode());
			List<Attachment> attlist = attachmentService.findByBizId(eventId, "ZHSQ_EVENT", null);
			List<Attachment> imgs = new ArrayList<Attachment>();
			List<Attachment> sounds = new ArrayList<Attachment>();
			List<Attachment> videos = new ArrayList<Attachment>();
			String filePath = "";
			String imgType = "";
			for (Attachment att : attlist) {
				filePath = att.getFilePath();
				imgType = FileUtils.getFileExtension(filePath).toLowerCase();
				if (PIC_PATTERN.matcher(imgType).find()) {
					imgs.add(att);
				} else if (SOUND_PATTERN.matcher(imgType).find()) {
					sounds.add(att);
				} else if (VIDEO_PATTERN.matcher(imgType).find()) {
					videos.add(att);
				}
			}
			result.put("imgs", imgs);
			result.put("sounds", sounds);
			result.put("videos", videos);
			result.put("instanceId", instanceId);

		}
		result.put("event", event);
		result.put("nofull", nofull);
		result.put("process", list);

		return result;
	}
	
	
	/**
	 * 跳转超时问题时间列表页
	 */
	@RequestMapping(value = "/toOvertimeEventList")
	public String toOvertimeEventList(HttpSession session, ModelMap map,
			@RequestParam Map<String, String> params) throws Exception {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("infoOrgCode", userInfo.getInfoOrgCodeStr());
		map.addAllAttributes(params);
		
		return "zzgl/bigScreen/jiangYin/centralControlCabin/list_event_overtime.ftl";
	}
	
	/**
	 * 跳转事件列表页
	 */
	@RequestMapping(value = "/toEventListPage")
	public String toEventListPage(HttpSession session, ModelMap map,
			@RequestParam Map<String, String> params) throws Exception {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			map.addAttribute("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		map.addAllAttributes(params);
		
		return "zzgl/bigScreen/jiangYin/centralControlCabin/list_event_page.ftl";
	}
	
	/**
	 * 跳转预警事件列表页
	 */
	@RequestMapping(value = "/toWarningEventList")
	public String toWarningEventList(HttpSession session, ModelMap map,
			@RequestParam Map<String, String> params) throws Exception {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("infoOrgCode", userInfo.getInfoOrgCodeStr());
		map.addAllAttributes(params);
		
		return "zzgl/bigScreen/jiangYin/centralControlCabin/list_event_warning.ftl";
	}
	
	/**
	 * 获取预警事件页面的数据
	 */
	@ResponseBody
	@RequestMapping(value = "/getWarningEventData")
	public Map<String,Object> getWarningEventData(HttpSession session
			,ModelMap map,@RequestParam Map<String, String> params) {
		Map<String,Object> result=new HashMap<String,Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		//获取紧急程度事件个数
		Map<String,Object> searchParam=new HashMap<String,Object>();
		searchParam.put("infoOrgCode", params.get("infoOrgCode"));
		searchParam.put("status", "00,01,02,03");//统计个数的时候使用全状态条件--先改成未办结的
		searchParam.put("listType", params.get("listType"));
		searchParam.put("pageNo", params.get("page"));
		searchParam.put("pageSize", params.get("rows"));
		//时间单位是年
		searchParam.put("createTimeStart", params.get("begintime"));
		searchParam.put("createTimeEnd", params.get("endtime"));
		
		searchParam.put("userId", userInfo.getUserId());
		searchParam.put("orgId", userInfo.getOrgId());
		searchParam.put("orgCode", userInfo.getOrgCode());
		
		List<BaseDataDict> urgencyDegreeDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.URGENCY_DEGREE_PCODE, userInfo.getOrgCode());
		Map<String,Object> urgCount=new HashMap<String,Object>();
		EUDGPagination urgList=new EUDGPagination();
		for (BaseDataDict baseDataDict : urgencyDegreeDict) {
			searchParam.put("urgencyDegree", baseDataDict.getDictGeneralCode());
			try {
				int findEventCount = event4WorkflowService.findEventCount(searchParam);
				urgCount.put(baseDataDict.getDictGeneralCode(), baseDataDict.getDictName()+findEventCount+"件");
				if(baseDataDict.getDictGeneralCode().toString().equals(params.get("searchUrgency").toString())) {
					urgList = event4WorkflowService.findEventListPagination(Integer.valueOf(params.get("page").toString()),Integer.valueOf(params.get("rows").toString()),searchParam);
				}
			} catch (ZhsqEventException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		result.put("urgencyDegreeCount", urgCount);
		result.put("urgencyDegreeList", urgList);
		
		//获取预警关键字
		String warnKeyWord = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, "warningKeyWord", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(warnKeyWord)) {
			result.put("warnKeyWord", warnKeyWord);
			
			//构造参数
			String warnKeyWordSearch=warnKeyWord.replace(",", "|");
			Map<String,Object> searchWarnParam=new HashMap<String,Object>();
			searchWarnParam.put("infoOrgCode", params.get("infoOrgCode"));
			searchWarnParam.put("status", params.get("status"));
			searchWarnParam.put("listType", params.get("listType"));
			searchWarnParam.put("pageNo", params.get("page"));
			searchWarnParam.put("pageSize", params.get("rows"));
			
			//时间单位是当前月
			searchWarnParam.put("createTimeStart", params.get("curMonthStart"));
			searchWarnParam.put("createTimeEnd", params.get("curMonthEnd"));
			
			searchWarnParam.put("userId", userInfo.getUserId());
			searchWarnParam.put("orgId", userInfo.getOrgId());
			searchWarnParam.put("orgCode", userInfo.getOrgCode());
			
			searchWarnParam.put("warnKeyWordSearch", warnKeyWordSearch);
			try {
				EUDGPagination findWarnList = event4WorkflowService.findEventListPagination(Integer.valueOf(params.get("page").toString()),Integer.valueOf(params.get("rows").toString()),searchWarnParam);
				result.put("warnList", findWarnList);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ZhsqEventException e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
	}
	
	
	/**
	 * 事件列表页数据加载
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
	 * @throws ZhsqEventException 
	 */
	@ResponseBody
	@RequestMapping(value = "/getEventListPageData", method = RequestMethod.POST)
	public Map<String,Object> getEventListPageData(
			HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "eventType") String eventType,
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
			@RequestParam Map<String, Object> paramMap) throws ZhsqEventException {
		Map<String,Object> result=new HashMap<String,Object>();
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if (page <= 0)
			page = 1;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(paramMap);
		
		params.put("userId", userInfo.getUserId());
		params.put("orgId", userInfo.getOrgId());
		params.put("orgCode", userInfo.getOrgCode());
		
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
		
		//获取辖区所有列表
		
		cn.ffcs.system.publicUtil.EUDGPagination pagination = new cn.ffcs.system.publicUtil.EUDGPagination();
			
		params.put("listType", 5);//辖区所有列表
			
		if(CommonFunctions.isBlank(params, "entryMatterDeptId")) {
			params.put("entryMatterDeptId", userInfo.getOrgId());
		}
			
		try {
			pagination = event4WorkflowService.findEventListPagination(page, rows, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		EUDGPagination paginationTarget = new EUDGPagination();
			
		paginationTarget.setTotal(pagination.getTotal());
		paginationTarget.setRows(pagination.getRows());
		
		result.put("eventList", paginationTarget);
		
		Map<String,Object> otherSearchParams=params;
		//督办数与超时数相互之间互不影响
		
		otherSearchParams.remove("superviseMark");
		
		//获取超时事件数
		otherSearchParams.put("handleDateFlag", "3");
		int findOvertimeEventCount = event4WorkflowService.findEventCount(otherSearchParams);
		result.put("findOvertimeEventCount", findOvertimeEventCount);
		
		//获取督办事件数
		otherSearchParams.remove("handleDateFlag");
		otherSearchParams.put("superviseMark", "1");
		int findSuperviseEventCount = event4WorkflowService.findEventCount(otherSearchParams);
		result.put("findSuperviseEventCount", findSuperviseEventCount);
		
		//获取满意度
		otherSearchParams.remove("superviseMark");
		
		//换成归档列表的查询
		otherSearchParams.put("listType", 4);//辖区归档列表
		otherSearchParams.remove("status");//初始化查询状态
		otherSearchParams.remove("statusList");//初始化查询状态
		otherSearchParams.put("bizPlatform", "0");//满意度只统计辖区内网格员上报的事件
		otherSearchParams.put("isUseTSjEvaResult", true);
		//先查所有有评价的事件 
		otherSearchParams.put("evaLevelList", "01,03,02");
		int allEvaEvent = event4WorkflowService.findEventCount(otherSearchParams);
		result.put("allEvaEvent", allEvaEvent);
		
		//再查询所有的满意事件
		otherSearchParams.put("evaLevelList", "02");
		int satisfyEvaEvent = event4WorkflowService.findEventCount(otherSearchParams);
		result.put("satisfyEvaEvent", satisfyEvaEvent);
		
		return result;
	}

	// 根据gridcode找到grid信息
	@ResponseBody
	@RequestMapping(value = "/getGridInfo")
	public MixedGridInfo getGridInfo(HttpServletRequest request, HttpSession session,
			@RequestParam Map<String, Object> paramMap) throws ZhsqEventException {

		MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(paramMap.get("infoOrgCode").toString());

		return gridInfo;

	}
	
	
	/**
	 * 检测是否需要关联表
	 * @param params
	 * @param checkType	0 总量；1 列表
	 * @return
	 * 		isUseWfInstanceStatus		是否关联WF_INSTANCE_STATUS
	 * 		isUseWfAttention			是否关联WF_ATTENTION
	 * 		isUseTEventExtend			是否关联T_EVENT_EXTEND
	 * 		isUseTRsAdminssionMatter	是否关联T_RS_ADMINSSION_MATTER
	 * 		isUseTZyResMarker			是否关联T_ZY_RES_MARKER
	 * 		isUseTDcOrgSocialInfo		是否关联T_DC_ORG_SOCIAL_INFO
	 * 		isUseTSjEvaResult			是否关联T_SJ_EVA_RESULT
	 */
	private Map<String, Object> checkAssociatedTable(Map<String, Object> params, int checkType) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isUseWfInstanceStatus = false,
				isUseWfAttention = false,
				isUseTEventExtend = false,
				isUseTRsAdminssionMatter = false,
				isUseTZyResMarker = false,
				isUseTDcOrgSocialInfo = false,
				isUseTSjEvaResult = false;
		
		boolean isEntryMatter = false,
				isCapMapInfo = false;
		
		int listType = 0;
		
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		isUseWfInstanceStatus = CommonFunctions.isNotBlank(params, "superviseMark");
		
		if(CommonFunctions.isNotBlank(params, "isEntryMatter")) {
			isEntryMatter = Boolean.valueOf(params.get("isEntryMatter").toString());
		}
		if(CommonFunctions.isNotBlank(params, "isCapMapInfo")) {
			isCapMapInfo = Boolean.valueOf(params.get("isCapMapInfo").toString());
		}
		
		isUseTRsAdminssionMatter = isEntryMatter 
								|| CommonFunctions.isNotBlank(params, "entryMatterDept")
								|| CommonFunctions.isNotBlank(params, "enterMatterChannel");
		isUseTZyResMarker = isCapMapInfo || CommonFunctions.isNotBlank(params, "mapType");
		
		isUseTDcOrgSocialInfo = CommonFunctions.isNotBlank(params, "creatorOrgChiefLevelList")
							 || CommonFunctions.isNotBlank(params, "creatorOrgType");
		
		switch(listType) {
			case 4: {//归档列表
				if(CommonFunctions.isNotBlank(params, "isUseTSjEvaResult")) {
					isUseTSjEvaResult = Boolean.valueOf(params.get("isUseTSjEvaResult").toString());
				} else {
					isUseTSjEvaResult = CommonFunctions.isNotBlank(params, "evaLevelList")
									 || CommonFunctions.isNotBlank(params, "evaLevel");
				}
				isUseTEventExtend = isEntryMatter || isUseTSjEvaResult;
				
				break;
			}
			case 9: {//辖区所有(展示当前环节)
				
			}
			case 5: {// 辖区所有
				isUseTEventExtend = isUseTRsAdminssionMatter;
				break;
			}
		}
		
		if(checkType == 1) {
			//列表默认关联表
			switch(listType) {
				case 3: {//辖区所有(支持办理人员查询)
					isUseWfInstanceStatus = true;
					break;
				}
				case 4: {//归档列表
					isUseWfInstanceStatus = true;
					if(CommonFunctions.isBlank(params, "isUseTEventExtend")) {
						isUseTEventExtend = true;
					}
					if(CommonFunctions.isBlank(params, "isUseTSjEvaResult")) {
						isUseTSjEvaResult = true;
					}
					
					break;
				}
				case 5: {// 辖区所有
					isUseWfInstanceStatus = true;
					isUseWfAttention = true;
					break;
				}
			}
		}
		
		resultMap.put("isUseWfInstanceStatus", isUseWfInstanceStatus);
		resultMap.put("isUseWfAttention", isUseWfAttention);
		resultMap.put("isUseTEventExtend", isUseTEventExtend);
		resultMap.put("isUseTRsAdminssionMatter", isUseTRsAdminssionMatter);
		resultMap.put("isUseTZyResMarker", isUseTZyResMarker);
		resultMap.put("isUseTDcOrgSocialInfo", isUseTDcOrgSocialInfo);
		resultMap.put("isUseTSjEvaResult", isUseTSjEvaResult);
		
		return resultMap;
	}

}
