package cn.ffcs.zhsq.event.controller;


import java.text.ParseException;
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

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.file.service.FileUrlProvideService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EvaResult;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.event.IEvaResultService;
import cn.ffcs.uam.service.IFunConfigureService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.crowd.service.IVisitRecordService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.event.service.IIncidentInvolvedService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.mybatis.domain.crowd.VisitRecord;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.EventRent;
import cn.ffcs.zhsq.mybatis.domain.event.EventSurvey;
import cn.ffcs.zhsq.mybatis.domain.event.IncidentInvolved;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.rent.service.IEventRentService;
import cn.ffcs.zhsq.survey.IEventSurveyService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;

@Controller
@RequestMapping("/zhsq/event/eventDisposalController")
public class RedSleeveController extends ZZBaseController {
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private FileUrlProvideService fileUrlProvideService;
	
	@Autowired
	private IVisitRecordService visitRecordService;
	
	@Autowired
	private IEventRentService eventRentService;
	
	@Autowired
	private IEventSurveyService eventSurveyService;
	
	@Autowired
	private IInvolvedPeopleService involvedPeopleService;
	
	@Autowired
	private IIncidentInvolvedService incidentInvolvedService;
	
	//多图片上传
	@Autowired
	private IAttachmentService attachmentService;
	
	//工作流调用接口
	@Autowired
	private HessianFlowService hessianFlowService;
	
	//组织域信息接口
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	//事件评价
	@Autowired
	private IEvaResultService evaResultService;
	
	//功能配置
	@Autowired
	private IFunConfigureService configureService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	/**
	 * 保存事件
	 * @param session
	 * @param ids
	 * @param event
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveEvent/0603", method = RequestMethod.POST)
	public Map<String, Object> save(
			HttpSession session,
			@RequestParam(value = "attachmentId", required = false) Long[] ids,
			@ModelAttribute(value = "event") EventDisposal event, 
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
		
		Long recordId = eventDisposalService.saveEventDisposalReturnId(event, userInfo);
		if(ids!=null && ids.length>0){
			boolean b = attachmentService.updateBizId(recordId, event.getType(), ids);
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("eventId", recordId);
		resultMap.put("result", recordId > 0);
		resultMap.put("type", "save");
		
		//return "/zzgl/event/result.ftl";
		return resultMap;
	}
	
	//@RequestMapping(value = "/detailEvent/0603")
	public String detailEventType(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "instanceId", required=false) String instanceId,
			@RequestParam(value = "workFlowId", required=false) String workFlowId,
			@RequestParam(value = "position", required=false) String position, 
			@RequestParam(value = "eventType", required=false) String eventType,
			@RequestParam(value = "model", required=false,defaultValue="l") String model,
			ModelMap map) throws NumberFormatException, ParseException {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		EventDisposal event = eventDisposalService.findEventById(eventId, userInfo.getOrgCode());

		// 获取涉事人员信息
		InvolvedPeople involvedPeople = new InvolvedPeople();
		involvedPeople.setBizId(event.getEventId());
		involvedPeople.setBizType(InvolvedPeople.BIZ_TYPE.ZHSQ_EVENT.getBizType());
		List<InvolvedPeople> involvedPeopleList = involvedPeopleService.findInvolvedPeopleList(involvedPeople);

		// 获取评价列表
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("evaObj", ConstantValue.EVA_OBJ_NEW_EVENT);
		params.put("objectId", eventId);
		params.put("orgCode", userInfo.getOrgCode());
		List<EvaResult> evaResultList = evaResultService.findEvaResultList(params);

		//获取督办列表
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("eventId", eventId);
		param.put("userId", userInfo.getUserId());
		EUDGPagination result = eventDisposalService.findSupervisePagination(1, 20, param);
		List superviseResultList = result.getRows();
		
		// // 判断该事件为哪种类型事件
		if ("0205".equals(event.getType())) {// 重点人员管理0205
			VisitRecord visitRecord = visitRecordService.findVisitRecordByEventId(event.getEventId());
			if(visitRecord != null){
				map.put("visitRecord", visitRecord);
			}else{
				map.put("visitRecord", new VisitRecord());
			}
		} else if ("0207".equals(event.getType())) {// 出租屋管理
			EventRent eventRent = eventRentService.findEventRentByEventId(event.getEventId());
			if(eventRent != null){
				map.put("eventRent", eventRent);
			}else{
				map.put("eventRent", new EventRent());
			}
		} else if ("0602".equals(event.getType())) {// 消防安全
			// 根据事件ID去查找消防安全信息
			EventSurvey eventSurvey = eventSurveyService.findEventSurveyByEventId(event.getEventId());
			if(eventSurvey != null){
				map.put("eventSurvey", eventSurvey);
			}else{
				map.put("eventSurvey", new EventSurvey());
			}
		}else if("0215".equals(event.getType())){
			IncidentInvolved incidentInvolved = incidentInvolvedService.findIncidentInvolvedById(event.getEventId());
			if(incidentInvolved != null){
				map.put("incidentInvolved", incidentInvolved);
			}else{
				map.put("incidentInvolved", new IncidentInvolved());
			}
		}
		String workflowCtx = hessianFlowService.getWorkflowCtx();
		Pattern pattern = Pattern.compile("[0-9]*");
		String taskId = "";
		if (StringUtils.isNotBlank(instanceId) && pattern.matcher(instanceId.trim()).matches()) {
			Long instanceIdL = Long.valueOf(instanceId);
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
							map.put("handleTime", DateUtils.convertDateToString(DateUtils.addInterval(DateUtils
									.convertStringToDate(startTime, "yyyy-MM-dd HH:mm:ss"), limitTime, "00")));
						}
					}catch(Exception e){
						map.put("handleTime",null);
					}
				}
			}
			String taskName= null;
			
			try {
				taskName = eventDisposalWorkflowService.curNodeTaskName(instanceIdL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(!StringUtils.isBlank(taskName)){
				map.addAttribute("curNodeTaskName", taskName);
			}
		}else{
			instanceId = null;
		}
		
		String filedomain = fileUrlProvideService.getFileDomain();
		map.put("downPath", filedomain);
		map.put("workflowCtx", workflowCtx);
		map.put("event", event);
		map.put("evaResultList", evaResultList);
		map.put("superviseResultList", superviseResultList);
		map.put("involvedPeopleList", involvedPeopleList);
		map.put("isLock", true);//涉及金额 默认需要锁
		
		map.addAttribute("instanceId", instanceId);
		map.addAttribute("workFlowId", workFlowId);
		map.addAttribute("taskId", taskId);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String userOrgCodes = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		String ssCode = ConstantValue.SHISHI_INVOLVED_MONEY_ORG_CODE;
		if (!StringUtils.isBlank(ssCode)) {
			String[] ssCodes = ssCode.split(",");
			for (String sCode : ssCodes) {
				if (userOrgCodes.startsWith(sCode)) {
					map.put("isLock", false);// 涉及金额 不需要
					break;
				}
			}
		}
		
		if(!StringUtils.isBlank(eventType) && "todo".equals(eventType)){
			if(!StringUtils.isBlank(instanceId) && hessianFlowService.isCurTask(taskId)){
				Map<String, Object> resultMap = eventDisposalWorkflowService.initFlowInfo(taskId, Long.valueOf(instanceId), userInfo, null);
				String type = "";
				
				if(event != null){
					type = event.getType();
				}
				
				map.put("type", type);
				map.addAllAttributes(resultMap);
				map.addAttribute("isLoadJs", true);//用于判断是否重复加载handle_event.ftl中的js
				map.addAttribute("formId", eventId);
				map.addAttribute("isHandle", true);//用于判断当前任务是否可办理
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
				taskPersonStr = new StringBuffer("");
				for(Map<String, Object> mapTemp : taskPerson){
					Object userNameObj = mapTemp.get("USER_NAME");
					Object orgNameObj = mapTemp.get("ORG_NAME");
					if(userNameObj != null){
						taskPersonStr.append(userNameObj);
						if(orgNameObj != null){
							taskPersonStr.append("(").append(orgNameObj).append(");");
						}
					}else if(orgNameObj != null){
						taskPersonStr.append(orgNameObj).append(";");
					}
				}
				
				taskPersonStr = new StringBuffer(taskPersonStr.substring(0, taskPersonStr.length()-1));
			}
			map.addAttribute("taskPersonStr", taskPersonStr);
		}
		
		if(!StringUtils.isBlank(eventType) && "draft".equals(eventType)){
			return "/zzgl/event/redSleeve/detail_event_draft.ftl";
		}else if(!StringUtils.isBlank(eventType) && "map".equals(eventType)){
			return "/map/arcgis/standardmappage/detail_event.ftl";
		}else{
			return "/zzgl/event/redSleeve/detail_event.ftl";
		}
	}
}
