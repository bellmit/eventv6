package cn.ffcs.zhsq.bigScreen.jiangYin;

import cn.ffcs.common.FileUtils;
import cn.ffcs.shequ.commons.util.StringUtils;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.CollectionUtils;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.event.service.*;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.map.jiangYinPlatform.service.IJiangYinPlatformService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.IReportIntegrationService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.szzg.event.service.IEventAndReportJsonpService;
import cn.ffcs.zhsq.utils.*;
import cn.ffcs.zhsq.utils.domain.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/zhsq/event/eventAnalyseController")
public class EventAnalyseController extends ZZBaseController {

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
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	
	@Autowired
	private IEventExtendService eventExtendService;
	
	//功能配置
	@Autowired
	private IFunConfigurationService configurationService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IInvolvedPeopleService involvedPeopleService;
	
	@Autowired
	private IEventDisposalDockingService eventDisposalDockingService;
	
	@Autowired
	private IReportFocusService reportFocusService;
	
	@Autowired
	protected IReportIntegrationService reportIntegrationService;

	@Autowired
	private IJiangYinPlatformService jiangYinPlatformService;
	@Autowired
	private IEventAndReportJsonpService eventAndReportJsonpService;
	

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
		boolean isCurrentUser = false;//判断当前登录人员是否在为当前办理人
		EventDisposal event = new EventDisposal();
		StringBuffer taskPersonStr = null;
		StringBuffer taskPersonUserId = null;
		StringBuffer taskPersonOrgId = null;
		if (eventId != null && eventId > 0) {
			
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			Long instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
			list = eventDisposalWorkflowService.queryProInstanceDetail(instanceId,
					IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
			
			event = eventDisposalService.findEventById(eventId, userInfo.getOrgCode());
			
			this.getEventExtendInfo(eventId,result,userInfo);
			
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
			
			//判断是否是当前办理人
			
			Long curNodeTaskId = eventDisposalWorkflowService.curNodeTaskId(instanceId);
			if (curNodeTaskId != null && curNodeTaskId > 0) {

				List<Map<String, Object>> taskPerson = eventDisposalWorkflowService
						.queryMyTaskParticipation(curNodeTaskId.toString());
				if (taskPerson != null && taskPerson.size() > 0) {
					taskPersonStr = new StringBuffer(";");
					taskPersonUserId = new StringBuffer(";");
					taskPersonOrgId = new StringBuffer(";");

					for (Map<String, Object> mapTemp : taskPerson) {
						Object userTypeObj = mapTemp.get("USER_TYPE");// 1表示USER_ID为组织ID；3表示USER_ID为用户ID
						Object orgNameObj = mapTemp.get("ORG_NAME");
						Long userOrgId = -1L;// 当前办理人员所属组织
						
						if(IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userTypeObj.toString())) {
							taskPersonUserId.append(mapTemp.get("USER_ID").toString()+";");
							taskPersonOrgId.append(mapTemp.get("ORG_ID").toString()+";");
						}else if (IEventDisposalWorkflowService.ACTOR_TYPE_ORG.equals(userTypeObj.toString())) {
							taskPersonUserId.append("0;");
							taskPersonOrgId.append(mapTemp.get("USER_ID").toString()+";");
						}

						if (CommonFunctions.isNotBlank(mapTemp, "ORG_ID")) {
							try {
								userOrgId = Long.valueOf(mapTemp.get("ORG_ID").toString());
								list.get(0).put("ORG_ID", userOrgId);//设置当前环节的组织Id
							} catch (NumberFormatException e) {
							}
						}
						if (CommonFunctions.isNotBlank(mapTemp, "USER_NAME")) {
							taskPersonStr.append(mapTemp.get("USER_NAME"));
							if (orgNameObj != null) {
								taskPersonStr.append("(").append(orgNameObj).append(");");
							}
						} else if (orgNameObj != null) {
							taskPersonStr.append(orgNameObj).append(";");
						}
						if (CommonFunctions.isNotBlank(mapTemp, "USER_ID") && !isCurrentUser) {
							Long userId = Long.valueOf(mapTemp.get("USER_ID").toString());
							String userType = "";// 1表示USER_ID为组织ID；3表示USER_ID为用户ID

							if (userTypeObj != null) {
								userType = userTypeObj.toString();
							}
							// 当前办理人要相同的用户和相同的组织
							if (IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userType)
									&& userInfo.getUserId().equals(userId) && userInfo.getOrgId().equals(userOrgId)) {
								isCurrentUser = true;
							} else if (IEventDisposalWorkflowService.ACTOR_TYPE_ORG.equals(userType)
									&& userInfo.getOrgId().equals(userId)) {
								isCurrentUser = true;
							}
						}

					}
				}
				
			}
			

		}
		
		//设置各个环节的组织层级
		for (Map<String, Object> task : list) {
			try {
				if(CommonFunctions.isNotBlank(task, "ORG_ID")) {
					OrgSocialInfoBO findByOrgId = orgSocialInfoOutService.findByOrgId(Long.valueOf(task.get("ORG_ID").toString()));
					task.put("ORG_LEVEL", findByOrgId.getChiefLevel());
				}
				if(CommonFunctions.isNotBlank(task, "IS_CURRENT_TASK")
						&&Boolean.valueOf(task.get("IS_CURRENT_TASK").toString())
						&&CommonFunctions.isBlank(task, "PARENT_PRO_TASK_ID")) {//只设置主流程
					task.put("HANDLE_PERSON", taskPersonStr.substring(1));
					task.put("HANDLE_PERSON_USER_ID", taskPersonUserId.substring(1));
					task.put("HANDLE_PERSON_ORG_ID", taskPersonOrgId.substring(1));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		result.put("isCurrentUser", isCurrentUser);
		result.put("event", event);
		result.put("nofull", nofull);
		result.put("process", list);

		return result;
	}
	
	
	private void getEventExtendInfo(Long eventId,Map<String,Object> map,UserInfo userInfo){
		
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
			map.putAll(eventExtend);
			
		}
		
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
		if("1".equals(params.get("canContactGridAdmin"))) {
			map.addAttribute("canContactGridAdmin", true);
		}
		map.addAllAttributes(params);
		
		String pageType="";
		try {
			String bigScreenDetailPage = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, "bigScreenDetailPage", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			if(StringUtils.isNotBlank(bigScreenDetailPage)) {
				pageType=bigScreenDetailPage;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "zzgl/bigScreen/jiangYin/eventAnalyse/list_event_page"+pageType+".ftl";
	}
	
	
	/**
	 * 跳转事件后台使用大屏风格列表页
	 */
	@RequestMapping(value = "/toEventListBackstagePage")
	public String toEventListBackstagePage(HttpSession session, ModelMap map,
			@RequestParam Map<String, String> params) throws Exception {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			map.addAttribute("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		map.addAllAttributes(params);
		
		map.addAttribute("createTimeStart", DateUtils.getMonthFirstDay());
		map.addAttribute("createTimeEnd", DateUtils.getToday());
		
		return "zzgl/bigScreen/jiangYin/eventAnalyse/list_event_backstage_page.ftl";
	}
	
	
	/**
	 *	 跳转事件详情页
	 */
	@RequestMapping(value = "/toEventDetailPage")
	public String toEventDetailPage(HttpSession session, ModelMap map,
			@RequestParam Map<String, String> params) throws Exception {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			map.addAttribute("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		map.addAllAttributes(params);
		
		return "zzgl/bigScreen/jiangYin/eventAnalyse/detail_event_page.ftl";
	}
	
	
	/**
	 * 跳转事件详情页
	 */
	@RequestMapping(value = "/toEventMediumDetailPage")
	public String toEventMediumDetailPage(HttpSession session, ModelMap map,
			@RequestParam Map<String, String> params) throws Exception {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			map.addAttribute("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		map.addAllAttributes(params);
		map.put("gisDomain", App.GIS.getDomain(session));
		
		return "zzgl/bigScreen/jiangYin/eventAnalyse/detail_event_medium_page.ftl";
	}
	
	
	/**
	 * 跳转事件详情页(带列表)
	 */
	@RequestMapping(value = "/toEventMediumListPage")
	public String toEventMediumListPage(HttpSession session, ModelMap map,
			@RequestParam Map<String, String> params) throws Exception {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			map.addAttribute("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		map.addAllAttributes(params);
		map.put("gisDomain", App.GIS.getDomain(session));
		
		return "zzgl/bigScreen/jiangYin/eventAnalyse/list_event_medium_page.ftl";
	}
	
	
	/**
	 * 跳转事件新增页
	 */
	@RequestMapping(value = "/toEventMediumAddPage")
	public String toEventMediumAddPage(HttpSession session, ModelMap map,
			@RequestParam(value = "eventJson", required = false) String eventJson,
			@RequestParam Map<String, String> params) throws Exception {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			map.addAttribute("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		map.addAllAttributes(params);
		map.put("gisDomain", App.GIS.getDomain(session));
		
		this.toAddEventByJson(session, eventJson, map);
		
		map.addAttribute("isShowSaveBtn", false);
		String privateFolderName="",privateFolderNameStr="";
		if(CommonFunctions.isNotBlank(params, "privateFolderNameStr")){
			privateFolderNameStr=params.get("privateFolderNameStr");
		}else{
			privateFolderNameStr = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.PRIVATE_FOLDER_NAME4_EVENT+"Add", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);
		}
		if(StringUtils.isNotBlank(privateFolderNameStr)) {
			privateFolderName=privateFolderNameStr;
		}
		
		return "zzgl/bigScreen/jiangYin/addPage"+privateFolderName+"/add_event_medium_page.ftl";
	}
	
	
	
	/**
	 * 跳转南安入格详情页
	 */
	@RequestMapping(value = "/toNananEntryEventPage")
	public String toNananEntryEventPage(HttpSession session, ModelMap map,
			@RequestParam Map<String, String> params) throws Exception {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			map.addAttribute("infoOrgCode", userInfo.getInfoOrgCodeStr());
		}
		if("1".equals(params.get("canContactGridAdmin"))) {
			map.addAttribute("canContactGridAdmin", true);
		}
		map.addAllAttributes(params);
		map.put("gisDomain", App.GIS.getDomain(session));
		
		String entryFlag="1";
		if(CommonFunctions.isNotBlank(params, "entryFlag")) {
			entryFlag=params.get("entryFlag").toString();
		}
		String reportTypeTitle = "";
		List<Map<String, String>> list = eventAndReportJsonpService.getReportFocusTypeList(null);
		for (Map<String, String> map2 : list) {
			if(entryFlag.equals(map2.get("REPORT_TYPE"))){
				reportTypeTitle = map2.get("REPORT_TITLE");
			}
		}
		map.put("reportTypeTitle", reportTypeTitle);
		map.put("reportType", entryFlag);
		return "zzgl/bigScreen/jiangYin/eventAnalyse/detail_"+entryFlag+"_page.ftl";
	}
	
	
	
	/**
	 * 获取南安入格事件详情
	 */
	@ResponseBody
	@RequestMapping(value = "/getNananEntryEventInfo")
	public Map<String,Object> getNananEntryEventInfo(HttpSession session, ModelMap map,
			@RequestParam(value = "eventId", required = false) String eventId,
			@RequestParam(value = "nofull", required = false) String nofull,
			@RequestParam(value = "caseId", required = false, defaultValue = "-1") Long caseId,
			@RequestParam Map<String, String> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String,Object> result=new HashMap<String,Object>();
		Map<String,Object> EpidemicInfo=new HashMap<String,Object>();
		String reportType="1";
		StringBuffer taskPersonStr = null;
		StringBuffer taskPersonUserId = null;
		StringBuffer taskPersonOrgId = null;
		boolean isCurrentUser = false;//判断当前登录人员是否在为当前办理人
		if (StringUtils.isNotBlank(eventId)) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			
			Map<String,Object> searchParams=new HashMap<String,Object>();
			//1标识两违防治
			//2标识房屋隐患住建业务
			//3标识企业安全隐患
			//4标识疫情防控
			//5标识流域水质
			//6标识三会一课
			//7标识扶贫走访
			if(CommonFunctions.isNotBlank(params, "reportType")) {
				searchParams.put("reportType", params.get("reportType").toString());
				reportType=params.get("reportType").toString();
			}
			if(CommonFunctions.isNotBlank(params, "listType")) {
				searchParams.put("listType", params.get("listType").toString());
			}else {
				searchParams.put("listType", "5");
			}
			searchParams.put("reportCode", eventId);
			searchParams.put("orgCode", userInfo.getOrgCode());
			
			EUDGPagination findPagination4ReportFocus = reportFocusService.findPagination4ReportFocus(1, 1, searchParams);
			List<Map<String,Object>> rows = (List<Map<String, Object>>) findPagination4ReportFocus.getRows();
			if(rows.size()>0) {
				EpidemicInfo=rows.get(0);
			}
			
			Long instanceId=Long.valueOf(EpidemicInfo.get("instanceId").toString());
			result.put("instanceId", instanceId);
			searchParams.put("isCapRegionName",true);
			EpidemicInfo = reportIntegrationService.findReportFocusByUUID(EpidemicInfo.get("reportUUID").toString(), userInfo, searchParams);

			Map<String,Object> searchFlowDetail=new HashMap<String,Object>();
			
			searchFlowDetail.put("listType", searchParams.get("listType"));
			searchFlowDetail.put("reportType", searchParams.get("reportType"));
			searchFlowDetail.put("instanceId", instanceId);
			
			list=reportIntegrationService.capHandledTaskInfoMap(instanceId, IWorkflow4BaseService.SQL_ORDER_DESC, searchFlowDetail);
			if (!CollectionUtils.isEmpty(list)) {
				EpidemicInfo.put("DUEDATESTR_",list.get(0).get("DUEDATESTR_"));
			}
			List<Map<String, String>> rList = eventAndReportJsonpService.getReportFocusTypeList(null);
			Map<String, String> attMap = new HashMap<String, String>();
			for (Map<String, String> rmap : rList) {
				attMap.put(rmap.get("REPORT_TYPE"), rmap.get("ATTACHMENT_TYPE"));
				attMap.put(rmap.get("REPORT_STR"), rmap.get("ATTACHMENT_TYPE"));
			}
			List<Attachment> attlist = attachmentService.findByBizId(Long.valueOf(EpidemicInfo.get("reportId").toString()), attMap.get(reportType), null);
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

			
			
			Long curNodeTaskId = eventDisposalWorkflowService.curNodeTaskId(instanceId);
			List<Map<String, Object>> taskPerson = eventDisposalWorkflowService
					.queryMyTaskParticipation(curNodeTaskId.toString());
			if (taskPerson != null && taskPerson.size() > 0) {
				taskPersonStr = new StringBuffer(";");
				taskPersonUserId = new StringBuffer(";");
				taskPersonOrgId = new StringBuffer(";");
				
				for (Map<String, Object> mapTemp : taskPerson) {
					Object userTypeObj = mapTemp.get("USER_TYPE");// 1表示USER_ID为组织ID；3表示USER_ID为用户ID
					Object orgNameObj = mapTemp.get("ORG_NAME");
					Long userOrgId = -1L;// 当前办理人员所属组织
					
					if(IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userTypeObj.toString())) {
						taskPersonUserId.append(mapTemp.get("USER_ID").toString()+";");
						taskPersonOrgId.append(mapTemp.get("ORG_ID").toString()+";");
					}else if (IEventDisposalWorkflowService.ACTOR_TYPE_ORG.equals(userTypeObj.toString())) {
						taskPersonUserId.append("0;");
						taskPersonOrgId.append(mapTemp.get("USER_ID").toString()+";");
					}
					
					if (CommonFunctions.isNotBlank(mapTemp, "ORG_ID")) {
						try {
							userOrgId = Long.valueOf(mapTemp.get("ORG_ID").toString());
							list.get(0).put("ORG_ID", userOrgId);//设置当前环节的组织Id
						} catch (NumberFormatException e) {
						}
					}
					if (CommonFunctions.isNotBlank(mapTemp, "USER_NAME")) {
						taskPersonStr.append(mapTemp.get("USER_NAME"));
						if (orgNameObj != null) {
							taskPersonStr.append("(").append(orgNameObj).append(");");
						}
					} else if (orgNameObj != null) {
						taskPersonStr.append(orgNameObj).append(";");
					}
					if (CommonFunctions.isNotBlank(mapTemp, "USER_ID") && !isCurrentUser) {
						Long userId = Long.valueOf(mapTemp.get("USER_ID").toString());
						String userType = "";// 1表示USER_ID为组织ID；3表示USER_ID为用户ID
						
						if (userTypeObj != null) {
							userType = userTypeObj.toString();
						}
						// 当前办理人要相同的用户和相同的组织
						if (IEventDisposalWorkflowService.ACTOR_TYPE_USER.equals(userType)
								&& userInfo.getUserId().equals(userId) && userInfo.getOrgId().equals(userOrgId)) {
							isCurrentUser = true;
						} else if (IEventDisposalWorkflowService.ACTOR_TYPE_ORG.equals(userType)
								&& userInfo.getOrgId().equals(userId)) {
							isCurrentUser = true;
						}
					}
					
				}
			}
			
			//设置各个环节的组织层级
			for (Map<String, Object> task : list) {
				try {
					if(CommonFunctions.isNotBlank(task, "ORG_ID")) {
						OrgSocialInfoBO findByOrgId = orgSocialInfoOutService.findByOrgId(Long.valueOf(task.get("ORG_ID").toString()));
						task.put("ORG_LEVEL", findByOrgId.getChiefLevel());
					}
					if(CommonFunctions.isNotBlank(task, "IS_CURRENT_TASK")
							&&Boolean.valueOf(task.get("IS_CURRENT_TASK").toString())
							&&CommonFunctions.isBlank(task, "PARENT_PRO_TASK_ID")) {//只设置主流程
						task.put("HANDLE_PERSON", taskPersonStr.substring(1));
						task.put("HANDLE_PERSON_USER_ID", taskPersonUserId.substring(1));
						task.put("HANDLE_PERSON_ORG_ID", taskPersonOrgId.substring(1));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		
		result.put("event", EpidemicInfo);
		result.put("nofull", nofull);
		result.put("process", list);

		return result;
	}
	
	
	
	
	@RequestMapping(value = "/toAddEventByJson")
	public String toAddEventByJson(HttpSession session, 
			@RequestParam(value = "eventJson", required = false) String eventJson,
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		EventDisposal event = new EventDisposal();
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
		}
		
		String forwardUrl = this.toAddEventByType(session, event.getEventId(), trigger, typesForList, isRemoveTypes, null, map);
		
		if(StringUtils.isNotBlank(eventJson)){
			if(StringUtils.isBlank(event.getContactUser())) {
				event.setContactUser(userInfo.getPartyName());
			}
			if(StringUtils.isBlank(event.getTel())) {
				event.setTel(userInfo.getVerifyMobile());
			}
			map.addAttribute("event", event);
		}
		
		map.addAttribute("isReport", isReport);
		map.addAttribute("isShowSaveBtn", isShowSaveBtn);
		map.addAttribute("isShowCloseBtn", isShowCloseBtn);
		map.addAttribute("rootGridId", event.getGridId());
		
		return forwardUrl;
	
	}
	
	@RequestMapping(value = "/toAddEventByType")
	public String toAddEventByType(HttpSession session, 
			@RequestParam(value = "eventId", required = false) Long eventId,
			@RequestParam(value = "trigger", required = false, defaultValue="") String trigger,
			@RequestParam(value = "typesForList", required = false) String typesForList,
			@RequestParam(value = "isRemoveTypes", required = false, defaultValue = "false") boolean isRemoveTypes,
			@RequestParam Map<String, Object> params,
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		EventDisposal event = null;
		
		if(eventId != null && eventId > 0) {
			Map<String, Object> eventParam = new HashMap<String, Object>(),
								eventResultMap = new HashMap<String, Object>();
			
			eventParam.put("userOrgCode", userInfo.getOrgCode());
			eventParam.putAll(params);
			
			try {
				eventResultMap = eventDisposalService.findEventByMap(eventId, eventParam);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(eventResultMap != null && !eventResultMap.isEmpty()) {
				if(CommonFunctions.isNotBlank(eventResultMap, "event")) {
					event = (EventDisposal) eventResultMap.get("event");
				}
				
				map.addAllAttributes(eventResultMap);
			}
			
			if(event != null) {
				String eventInvolvedPeople = event.getEventInvolvedPeople();
				Long instanceId = -1L;
				
				if(StringUtils.isNotBlank(eventInvolvedPeople)) {
					//String[] ipArray = eventInvolvedPeople.split("；"),
						//	 item = null;
					//String ip = null;
					//InvolvedPeople involvedPeople = null;

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
		
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
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

		//根据登陆组织跳转到个性化页面
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
		
//		Map<String,Object> otherSearchParams=params;
//		//督办数与超时数相互之间互不影响
//		
//		otherSearchParams.remove("superviseMark");
//		
//		//获取超时事件数
//		otherSearchParams.put("handleDateFlag", "3");
//		int findOvertimeEventCount = event4WorkflowService.findEventCount(otherSearchParams);
//		result.put("findOvertimeEventCount", findOvertimeEventCount);
//		
//		//获取督办事件数
//		otherSearchParams.remove("handleDateFlag");
//		otherSearchParams.put("superviseMark", "1");
//		int findSuperviseEventCount = event4WorkflowService.findEventCount(otherSearchParams);
//		result.put("findSuperviseEventCount", findSuperviseEventCount);
//		
//		//获取满意度
//		otherSearchParams.remove("superviseMark");
//		
//		//换成归档列表的查询
//		otherSearchParams.put("listType", 4);//辖区归档列表
//		otherSearchParams.remove("status");//初始化查询状态
//		otherSearchParams.remove("statusList");//初始化查询状态
//		otherSearchParams.put("bizPlatform", "0");//满意度只统计辖区内网格员上报的事件
//		otherSearchParams.put("isUseTSjEvaResult", true);
//		//先查所有有评价的事件 
//		otherSearchParams.put("evaLevelList", "01,03,02");
//		int allEvaEvent = event4WorkflowService.findEventCount(otherSearchParams);
//		result.put("allEvaEvent", allEvaEvent);
//		
//		//再查询所有的满意事件
//		otherSearchParams.put("evaLevelList", "02");
//		int satisfyEvaEvent = event4WorkflowService.findEventCount(otherSearchParams);
//		result.put("satisfyEvaEvent", satisfyEvaEvent);
		
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
	 * 获取工作流信息详情
	 */
	@ResponseBody
	@RequestMapping(value = "/getWorkflowHandleInfo", method = RequestMethod.POST)
	public Map<String, Object> getWorkflowHandleInfo(HttpSession session, ModelMap map,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "instanceId", required = false) String instanceId,
			@RequestParam(value = "workFlowId", required = false) String workFlowId) {

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Long instanceIdL = -1L;
		instanceIdL = Long.valueOf(instanceId);
		Long taskIdL = eventDisposalWorkflowService.curNodeTaskId(instanceIdL);
		String taskId = "";
		if (taskIdL != null && !taskIdL.equals(-1L)) {
			taskId = taskIdL.toString();
		}
		// 获取下一办理环节信息
		Map<String, Object> resultMap = eventDisposalWorkflowService.initFlowInfo(taskId, instanceIdL, userInfo, null);

		return resultMap;
	}


	/**
	 * 获取当前环节信息
	 */
	@ResponseBody
	@RequestMapping(value = "/getCurPro", method = RequestMethod.POST)
	public List<Map<String, Object>> getCurPro(HttpSession session, ModelMap map,
			@RequestParam(value = "eventId", required = false, defaultValue = "-1") Long eventId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (eventId != null && eventId > 0) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			Long instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
			list = eventDisposalWorkflowService.queryProInstanceDetail(instanceId,
					IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
			String taskId = "";
			if (null != list && list.size() > 0) {
				taskId = String.valueOf(list.get(0).get("TASK_ID"));
			}
			try {
				boolean userInfoCurrentUser = eventDisposalWorkflowService.isUserInfoCurrentUser(taskId, instanceId,
						userInfo);
				list.get(0).put("userInfoCurrentUser", userInfoCurrentUser);
			} catch (Exception e) {
			}
		}

		return list;
	}

	/**
	 *	根据组织orgId返回地域编码
	 *	orgId 组织orgId,多个就用逗号分割
	 */
	@ResponseBody
	@RequestMapping("/findInfoOrgCodesByOrgIds")
	public List<Map<String, Object>> findInfoOrgCodesByOrgIds(@RequestParam Map<String, Object> params) {
		return jiangYinPlatformService.findInfoOrgCodesByOrgIds(params);
	}

}
