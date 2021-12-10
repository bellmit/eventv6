package cn.ffcs.zhsq.nanChang3D.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import cn.ffcs.gmis.dailySupervision.DailySupervisionService;
import cn.ffcs.gmis.mybatis.domain.dailySupervision.DailySupervision;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PointInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IPointInfoService;
import cn.ffcs.system.publicUtil.CryptUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.eventExpand.service.IEventLabelService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.intermediateData.eventVerify.service.IEventVerifyBaseService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPoint;
import cn.ffcs.zhsq.nanChang3D.service.INanChang3DService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.ArrayUtil;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.JsonUtils;
import cn.ffcs.zhsq.utils.data.DateUtils;

@Controller
@RequestMapping(value="/zhsq/nanChang3D")
public class NanChang3DController extends ZZBaseController{
	
	@Autowired
	private INanChang3DService nanChang3DService;
	@Autowired
	private IPointInfoService pointInfoService;//点位服务
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	@Autowired
	private IEventDisposalDockingService eventDisposalDockingService;
	
	@Autowired
	private IInvolvedPeopleService involvedPeopleService;
	
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	
	//功能配置
	@Autowired
	private IFunConfigurationService configurationService;
	//日常督察
	@Autowired
	private DailySupervisionService dailySupervisionService;
	
	@Autowired
	private IEventLabelService eventLabelService;
	
	//网格树
	@ResponseBody
	@RequestMapping(value="/findGridData")
	public List<Map<String,Object>> findGridData(
			HttpSession session,
			@RequestParam(value="infoOrgCode")String infoOrgCode,
			Map<String,Object> params
			){
		params.put("infoOrgCode", infoOrgCode);
		return nanChang3DService.findGridData(params);
	}
	
	
	//多发区区县TOP10
	@ResponseBody
	@RequestMapping(value="/findMultipleCountiesData")
	public List<Map<String,Object>> findMultipleCountiesData(
			HttpSession session,
			@RequestParam(value="infoOrgCode")String infoOrgCode,
			@RequestParam(value="eventDateBegin")String eventDateBegin,
			@RequestParam(value="eventDateEnd")String eventDateEnd,
			@RequestParam(value="dateType")String dateType,
			Map<String,Object> params
			){
		
		params.put("infoOrgCode", infoOrgCode);
		params.put("dateType", dateType);
		params.put("eventDateBegin", eventDateBegin);
		params.put("eventDateEnd", eventDateEnd);
		return nanChang3DService.findMultipleCountiesData(params);
	}
	
	
	//多发点位TOP10
	@ResponseBody
	@RequestMapping(value="/findPointData")
	public List<Map<String,Object>> findPointData(
			HttpSession session,
			@RequestParam(value="infoOrgCode")String infoOrgCode,
			@RequestParam(value="gridId")String gridId,
			@RequestParam(value="date_")String date_,
			@RequestParam(value="dateType")String dateType,
			@RequestParam(value="type")String type,
			Map<String,Object> params
			){
		params.put("infoOrgCode", infoOrgCode);
		params.put("gridId", gridId);
		params.put("date_", date_);
		params.put("dateType", dateType);
		params.put("type", type);
		return nanChang3DService.findPointData(params);
	}
	
	//点位总数
	@ResponseBody
	@RequestMapping(value="/getPointTotal")
	public Integer getPointTotal(
			HttpSession session,
			@RequestParam(value="infoOrgCode")String infoOrgCode,
			Map<String,Object> params
			){
		params.put("infoOrgCode", infoOrgCode);
		cn.ffcs.system.publicUtil.EUDGPagination searchList = pointInfoService.searchList(1, 20, params);
		return searchList.getTotal();
	}
	
	// 跳转到地域列表页
	@RequestMapping(value = "/toDiyu")
	public String toDiyu(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value="infoOrgCode")String infoOrgCode) {
		map.put("infoOrgCode", infoOrgCode);
		return "/map/arcgis/arcgis_3d/regionalGrid.ftl";
	}
	
	/**
	 * 点位列表
	 * @param request
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findPointInfoList")
	public Object findAirHistoryList(HttpServletRequest request,
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows){
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("maxlng",request.getParameter("maxlng"));
		params.put("minlng",request.getParameter("minlng"));
		params.put("maxlat",request.getParameter("maxlat"));
		params.put("minlat",request.getParameter("minlat"));
		params.put("infoOrgCode",request.getParameter("infoOrgCode"));//
		params.put("mapt",request.getParameter("mapt"));
		params.put("sName",request.getParameter("pointName"));//点位名称
		String pointLevelsStr=request.getParameter("pointLevels");
		String pointTypesStr=request.getParameter("pointTypes");
		if(StringUtils.isNotBlank(pointLevelsStr)) {
			params.put("pointLevels",pointLevelsStr.split(","));//点位级别 A,B,C
		}
		if(StringUtils.isNotBlank(pointTypesStr)) {
			params.put("pointTypes",pointTypesStr.split(","));//点位类别
		}
		return nanChang3DService.findPointListByParams(page, rows, params);
	}
	/**
	 * 点位类型
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/pointTypeCheck")
	public String pointTypeCheck(HttpServletRequest request,HttpSession session, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<BaseDataDict> pointTypesByDict= baseDictionaryService.getDataDictTree(ConstantValue.MAP_POINT_TYPE_CODE, userInfo.getOrgCode());
		String pointTypes=null==request.getParameter("pointTypes")?"":request.getParameter("pointTypes").toString();
		String professionCode=null==request.getParameter("professionCode")?"":request.getParameter("professionCode").toString();
		if(!"".equals(professionCode)) {
			String[] professionCodeArr=professionCode.split("_");
			professionCode=professionCodeArr[1];
		}
		String[] checkedTypes=null;
		if(StringUtils.isNotBlank(pointTypes)) {
			checkedTypes=pointTypes.split(",");
		}
		map.put("checkedTypes", checkedTypes);
		map.put("professionCode", professionCode);
		map.put("pointTypesByDict", pointTypesByDict);
		map.put("dictPcode", ConstantValue.MAP_POINT_TYPE_CODE);
		return "/map/arcgis/arcgis_3d/pointTypeCheck.ftl";
	}
	
	/**
	 * 区域点位责任人
	 * @param request
	 * @param session
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/findCivilizedPerInfoData")
	public List<Map<String,Object>> findCivilizedPerInfoData( HttpServletRequest request,HttpSession session,  Map<String,Object> params){
		params.put("infoOrgCode",request.getParameter("infoOrgCode"));//
		return nanChang3DService.findCivilizedPerInfoData(params);
	}
	
	/**
	 * 工作部责任人
	 * @param request
	 * @param session
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/workDepartmentPerson")
	public Map<String,Object> findWorkDepartmentPerson( HttpServletRequest request,HttpSession session,  Map<String,Object> params){
		//String pointType =request.getParameter("pointType");
		params.put("dictGeneralCode",request.getParameter("pointType"));//
		params.put("dictName",request.getParameter("pointTypeStr"));//
		//获取pcode值
		List<BaseDataDict> DataDict=baseDictionaryService.getDataDictListOfSinglestage(params);
		String pcode =DataDict.get(0).getDictPcode();
		Map<String,Object> params1 = new HashMap<String,Object>();
		params1.put("dictCode",pcode);
		List<BaseDataDict> DataDict1=baseDictionaryService.getDataDictListOfSinglestage(params1);
		//获取上级字典dictGeneralCode值
		String dictGeneralCode =DataDict1.get(0).getDictGeneralCode();
		String dictName = DataDict1.get(0).getDictName();
		String professionCode="3601_"+dictGeneralCode;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<OrgSocialInfoBO> orgList =orgSocialInfoOutService.findOrgSocialListByCodition(userInfo.getOrgId(),professionCode,true);
		Map<String,Object> params2 = new HashMap<String,Object>();
		Map<String,Object> m = new HashMap<String,Object>();
		if(orgList.size()>0) {
			params2.put("infoOrgCode",orgList.get(0).getOrgCode());
			List<Map<String,Object>> civilizedPerInfos=nanChang3DService.findCivilizedPerInfoData(params2);
			if(civilizedPerInfos.size()>0) {
				m.put("workDepartment", dictName);//区域责任区名称
				m.put("pmTel", civilizedPerInfos.get(0).get("MOBILE_TELEPHONE"));//区域责任区名称
				m.put("pointManager", civilizedPerInfos.get(0).get("NAME"));//区域责任人电话
			}
		}
		return m;
	}
	
	/**
	 *点击轮廓 统计
	 * @param request
	 * @param session
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pointStat")
	public Map<String,Object> pointStat( HttpServletRequest request,HttpSession session,  Map<String,Object> params){
		params.put("infoOrgCode",request.getParameter("infoOrgCode"));//
		Map<String,Object> m=new HashMap<String,Object>();
		Map<String,Object> montiorStat= nanChang3DService.getMontiorStat(params);
		Map<String,Object> pontStat= nanChang3DService.getPointStat(params);
		params.put("dateType", request.getParameter("dateType"));
		
		String beginTime=request.getParameter("beginTime");
		String endTime=request.getParameter("endTime");
		if(StringUtils.isNotBlank(beginTime))
		params.put("eventDateBegin",beginTime);
		if(StringUtils.isNotBlank(endTime))
		params.put("eventDateEnd", endTime );
		String gridLevel=request.getParameter("gridLevel");
		if(StringUtils.isNotBlank(gridLevel)) {
			params.put("gridLevel", gridLevel);
			if(Integer.valueOf(gridLevel)<4) {//街道级以上
				 List<Map<String,Object>> civilizedPerInfos=nanChang3DService.findCivilizedPerInfoData(params);
				if(civilizedPerInfos.size()>0) {
					m.put("pmTel", civilizedPerInfos.get(0).get("MOBILE_TELEPHONE"));//区域责任区名称
					m.put("pointManager", civilizedPerInfos.get(0).get("NAME"));//区域责任人电话
				}
			}
		}
		Map<String,Object> eventStat= nanChang3DService.getEventStat(params);
		if(null!=montiorStat)
			m.putAll(montiorStat);
		if(null!=pontStat)
			m.putAll(pontStat);
		if(null!=eventStat)
			
			m.putAll(eventStat);
		
		return m;
	}
	/**
	 * 点位撒点
	 * @param request
	 * @param session
	 * @param params
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDrawDataOfPointsByCode")
	public List<ArcgisInfoOfPoint> getArcgisDrawDataOfPointsByCode( HttpServletRequest request,HttpSession session
			,@RequestParam(value="mapt",required=false) int mapt){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("xmax",request.getParameter("xmax"));
		params.put("xmin",request.getParameter("xmin"));
		params.put("ymax",request.getParameter("ymax"));
		params.put("ymin",request.getParameter("ymin"));
		params.put("infoOrgCode",request.getParameter("infoOrgCode"));//
		String pointLevelsStr=request.getParameter("pointLevels");
		params.put("sName",request.getParameter("pointName"));//点位名称
		String pointTypesStr=request.getParameter("pointTypes");
		if(StringUtils.isNotBlank(pointLevelsStr)) {
			params.put("pointLevels",pointLevelsStr.split(","));//点位级别 A,B,C
		}
		if(StringUtils.isNotBlank(pointTypesStr)) {
			params.put("pointTypes",pointTypesStr.split(","));//点位类别
		}
		params.put("mapt",mapt);//
		return nanChang3DService.getPointGisByOrgCode(params);
	}
	
	
	// 事件待办与点位详情部分start


	// 跳转到待办事件列表页
	@RequestMapping(value = "/eventList")
	public String EventList(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "beginTime", required = false) String beginTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "type", required = false) String type) {
		map.put("type", type);
		map.put("code", code);
		map.put("date", date);
		map.put("status", status);
		//获取当前月份的第一天和最后一天
		Calendar cale = Calendar.getInstance();
		cale.add(Calendar.MONTH, 0);
		cale.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		String firstDayOfMonth=DateUtils.formatDate(cale, "yyyy-MM-dd");
		cale.set(Calendar.DAY_OF_MONTH,cale.getActualMaximum(Calendar.DAY_OF_MONTH));//设置到最后一天
		String lastDayOfMonth=DateUtils.formatDate(cale, "yyyy-MM-dd");
		
		map.put("happenTimeStartForAll", firstDayOfMonth);
		map.put("happenTimeEndForAll", lastDayOfMonth);
		
		return "/map/arcgis/arcgis_3d/eventList.ftl";
	}

	// 跳转到设备、点位列表页
	@RequestMapping(value = "/rightList")
	public String rightList(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "beginTime", required = false) String beginTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "type", required = false) String type) {
		map.put("type", type);
		map.put("code", code);
		map.put("date", date);
		map.put("status", status);
		if (date != null && !"".equals(date)) {
			if (date.length() == 4) {
				map.put("beginTime", date + "-01-01");
				map.put("endTime", date + "-12-31 23:59:59");
			} else if (date.length() == 6) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1, 1);
				map.put("beginTime", sdf.format(cal.getTime()));
				cal.add(Calendar.MONTH, 1);
				map.put("endTime", sdf.format(cal.getTime()));
			}
		} else if (beginTime != null && endTime != null) {
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
		}

		return "/map/arcgis/arcgis_3d/nanchang-wenming-rightlist.ftl";
	}

	// 跳转到点位列表页
	@RequestMapping(value = "/pointSelectionList")
	public String pointSelectionList(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value = "code", required = false, defaultValue = "") String code) {

		map.put("code", code);

		return "/map/arcgis/arcgis_3d/pointList.ftl";
	}

	// 跳转到点位详情页
	@RequestMapping(value = "/pointSelectionInfo")
	public String pointSelectionInfo(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value = "pointId", required = false, defaultValue = "") Long pointId) {

		PointInfo pointInfo = new PointInfo();
		// 获取点位信息
		if (pointId != null && pointId > 0) {
			pointInfo = pointInfoService.searchById(pointId);
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("pointId", pointId);
		map.addAttribute("pointInfo", pointInfo);
		Map<String,Object> gis= nanChang3DService.getPointGis(param);
		map.put("gis", gis);
		param.put("pointType", pointInfo.getPointType());
		List<ArcgisInfoOfPoint> text= nanChang3DService.findFieldWorkData(param);
		if(text != null && text.size()>0 && text.get(0)!=null) {
			map.put("text", text.get(0).getElementsCollectionStr());
		}
		map.put("gis", gis);
		return "/map/arcgis/arcgis_3d/pointDetail.ftl";
	}

	// 跳转到事件详情页
	@RequestMapping(value = "/getEventInfo")
	public String getEventInfo(HttpSession session, ModelMap map,
			@RequestParam(value = "eventId", required = false, defaultValue = "-1") Long eventId,
			@RequestParam(value = "nofull", required = false) String nofull,
			@RequestParam(value = "caseId", required = false, defaultValue = "-1") Long caseId) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
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
			map.put("imgs", imgs);
			map.put("sounds", sounds);
			map.put("videos", videos);

			// 获取事件额外信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("isFormat", "1");
			params.put("orgCode", userInfo.getOrgCode());

			Map<String, Object> findEventExtendByEventId = eventExtendService.findEventExtendByEventId(eventId, params);
			if (null != findEventExtendByEventId && findEventExtendByEventId.size() > 0) {

				Map<String, Object> eventExtend = new HashMap<String, Object>();
				eventExtend.put("patrolType", findEventExtendByEventId.get("patrolType"));
				eventExtend.put("patrolTypeName", findEventExtendByEventId.get("patrolTypeName"));
				eventExtend.put("pointSelection", findEventExtendByEventId.get("pointSelection"));
				eventExtend.put("pointSelectionName", findEventExtendByEventId.get("pointSelectionName"));
				eventExtend.put("isNotice", findEventExtendByEventId.get("isNotice"));
				map.addAllAttributes(eventExtend);

			}
		}
		map.put("event", event);
		map.put("nofull", nofull);
		map.put("process", list);

		return "/map/arcgis/arcgis_3d/eventInfo.ftl";
	}
	
	// 跳转到审核事件详情页
	@RequestMapping(value = "/toEventVerifyInfo")
	public String toEventVerifyInfo(HttpSession session,
			@RequestParam(value = "eventVerifyId") Long eventVerifyId,
			@RequestParam(value = "verifyType", required = false) String verifyType,
			@RequestParam Map<String, Object> eventVerifyMap,
			ModelMap map) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		String infoOrgCode = this.getDefaultInfoOrgCode(session),
			   userOrgCode = userInfo.getOrgCode();
		
		eventVerifyMap.put("userOrgCode", userOrgCode);
		params.put("userOrgCode", userOrgCode);
		params.put("verifyType", verifyType);
		
		Map<String, Object> eventVerify = eventVerifyBaseService.findEventVerifyById(eventVerifyId, eventVerifyMap);
		
		
		if(eventVerify == null) {
			eventVerify = new HashMap<String, Object>();
		} else if(CommonFunctions.isNotBlank(eventVerify, "infoOrgCode")) {
			infoOrgCode = eventVerify.get("infoOrgCode").toString();
		}
		params.put("bizPlatform",eventVerify.get("bizPlatform"));
		if(StringUtils.isNotBlank(infoOrgCode)) {
			MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(infoOrgCode);
			if(gridInfo != null) {
				map.addAttribute("startGridId", gridInfo.getParentGridId());
				map.addAttribute("gridName", gridInfo.getGridName());
				map.addAttribute("gridId", gridInfo.getGridId());
			}
		}
		
		map.addAttribute("eventWechat", eventVerify);
		map.addAttribute("verifyType", verifyType);
		map.addAttribute("imgDownPath", App.IMG.getDomain(session));//用于附件上传组件中的缩略图展示
		map.addAttribute("markerOperation", ConstantValue.TITLE_MARKER);//标注操作
		map.addAllAttributes(eventVerifyBaseService.fetchParam4Event(params));
		
		return "/map/arcgis/arcgis_3d/eventVerify.ftl";	
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
	 * 获取点位列表
	 */
	@ResponseBody
	@RequestMapping(value = "/getPointList", method = RequestMethod.POST)
	public EUDGPagination getPointList(HttpSession session, ModelMap map,
			@RequestParam(value = "infoOrgCode") Long infoOrgCode, @RequestParam(value = "pageNo") int pageNo,
			@RequestParam(value = "pageSize") int pageSize) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("infoOrgCode", infoOrgCode);
		EUDGPagination searchList = pointInfoService.searchList(pageNo, pageSize, params);

		return searchList;
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
	 * 获取点位信息
	 */
	@ResponseBody
	@RequestMapping(value = "/getPointInfo", method = RequestMethod.POST)
	public PointInfo getPointInfo(HttpSession session, ModelMap map, @RequestParam(value = "pointId") Long pointId) {
		PointInfo pointInfo = new PointInfo();
		if (pointId != null && pointId > 0) {
			pointInfo = pointInfoService.searchById(pointId);
		}
		return pointInfo;
	}

	/**
	 * 获取其他平台地址
	 * @throws Exception 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 */
	@ResponseBody
	@RequestMapping(value = "/getOutUrl")
	public Map<String, Object> getOutUrl(HttpSession session, ModelMap map,@RequestParam(value = "host") String host) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String accessToken = sdf.format(new Date());
		String userCode = userInfo.getUserName();
		String encodeUserCode = CryptUtils.encryptor(accessToken,userCode); 
		String url = host+"/ctm01ssoservice/v1/login?accessToken="+accessToken+"&userCode="+encodeUserCode+"&target="+host+"/istreet/index/handler";
		params.put("aifx", url);
		return params;
	}
	
	// 获取点位相关联的事件信息
	/**
	 * 获取点位信息
	 */
	@ResponseBody
	@RequestMapping(value = "/getPointReEventInfo", method = RequestMethod.POST)
	public EUDGPagination getPointReEventInfo(HttpSession session, ModelMap map,
			@RequestParam(value = "pointId") Long pointId, @RequestParam(value = "pageNo") int pageNo,
			@RequestParam(value = "pageSize") int pageSize) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pointSelection", pointId);
		params.put("listType", 5);
		params.put("patrolType", "1,2");
		params.put("isCapEventAdditionalColumn", true);
		params.put("userId", userInfo.getUserId());
		params.put("orgId", userInfo.getOrgId());
		params.put("orgCode", userInfo.getOrgCode());

		// 获取点位相关联的事件信息
		EUDGPagination findEventListPagination = new EUDGPagination();
		try {
			findEventListPagination = event4WorkflowService.findEventListPagination(pageNo, pageSize, params);
		} catch (ZhsqEventException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return findEventListPagination;
	}

	// 获取事件额外信息
	private void getEventExtendInfo(Long eventId, ModelMap map, UserInfo userInfo) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("isFormat", "1");
		params.put("orgCode", userInfo.getOrgCode());

		// 获取事件额外信息
		Map<String, Object> findEventExtendByEventId = eventExtendService.findEventExtendByEventId(eventId, params);
		if (null != findEventExtendByEventId && findEventExtendByEventId.size() > 0) {

			Map<String, Object> eventExtend = new HashMap<String, Object>();
			eventExtend.put("patrolType", findEventExtendByEventId.get("patrolType"));
			eventExtend.put("patrolTypeName", findEventExtendByEventId.get("patrolTypeName"));
			eventExtend.put("pointSelection", findEventExtendByEventId.get("pointSelection"));
			eventExtend.put("pointSelectionName", findEventExtendByEventId.get("pointSelectionName"));
			eventExtend.put("isNotice", findEventExtendByEventId.get("isNotice"));
			map.addAllAttributes(eventExtend);

		}

	}

	/**
    *
    * @param live         是否调用live播放器
    * @param path         本地视频预览路径
    * @param streamAddr   流地址（直播流及视频流） 支持rtmp，hls,flv
    * @param model        页面模型
    * @param watermark    是否水印
    * @param liveStream   是否是直播流
    * @param request
    * @return
    */
   @RequestMapping("/index")
   public String shequVideoIndex(HttpSession session,String live,String path, String streamAddr, ModelMap model,
		   String watermark,String liveStream,String watermarkContent,String iframeId,String monitorId,HttpServletRequest request){
	   UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
       model.put("path",path);
       model.put("SKY_URL",App.SKY.getDomain(session));
       model.put("red5",streamAddr);
       model.put("liveStream",liveStream); 
       model.put("watermarkContent",userInfo.getUserName());
       model.put("red5",streamAddr);
       model.put("iframeId",iframeId);
       model.put("monitorId",monitorId);
       model.put("live",live);       
       model.put("type","rtmp/flv");
       return  "/map/arcgis/arcgis_3d/live_all.ftl";
   }
	
  
	// 事件工作流服务
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	// 事件服务员
	@Autowired
	private IEventDisposalService eventDisposalService;
	// 附件服务
	@Autowired
	private IAttachmentService attachmentService;

	@Autowired
	private IEvent4WorkflowService event4WorkflowService;
	@Autowired
	private IEventExtendService eventExtendService;

	@Autowired
	private IEventVerifyBaseService eventVerifyBaseService;

	// 附件类型
	public static final Pattern PIC_PATTERN = Pattern.compile("jpg|png|bmp|gif|jpeg|webp");
	public static final Pattern VIDEO_PATTERN = Pattern.compile("mp4");
	public static final Pattern SOUND_PATTERN = Pattern.compile("amr|mp3");
	
	
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
		
		if(CommonFunctions.isBlank(params, "infoOrgCodeForVerify")) {
			params.put("infoOrgCodeForVerify", userInfo.getInfoOrgCodeStr());
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
		
		if(eventType.equals("todo")){//--待办
			cn.ffcs.system.publicUtil.EUDGPagination pagination = new cn.ffcs.system.publicUtil.EUDGPagination();
			
			params.put("listType", 1);//待办列表
			
			try {
				pagination = nanChang3DService.findLargeDataPlatformEvenListPagination(page, rows, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			EUDGPagination paginationTarget = new EUDGPagination();
			
			paginationTarget.setTotal(pagination.getTotal());
			paginationTarget.setRows(pagination.getRows());
			
			return paginationTarget;
		}else if(eventType.equals("all")){//--辖区所有
			cn.ffcs.system.publicUtil.EUDGPagination pagination = new cn.ffcs.system.publicUtil.EUDGPagination();
			
			params.put("listType", 5);//辖区所有列表
			
			if(CommonFunctions.isBlank(params, "gridId")) {
				params.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
			}
			
			try {
				pagination = event4WorkflowService.findEventListPagination(page, rows, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			EUDGPagination paginationTarget = new EUDGPagination();
			
			paginationTarget.setTotal(pagination.getTotal());
			paginationTarget.setRows(pagination.getRows());
			
			return paginationTarget;
		}
		
		return null;
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
			HttpServletRequest request,
			ModelMap map){
		//UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		String eventJsonStr="";
		try {
			eventJsonStr = new String(URLDecoder.decode(request.getParameter("eventJson"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.toAddEventByJson(session, eventJsonStr, map);

		return "/map/arcgis/arcgis_3d/eventAdd.ftl";	 

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
		String typesForList = "",
			   trigger = "",
			   attachmentIds = "";
		boolean isReport = true, isShowSaveBtn = true, isShowCloseBtn = true, isRemoveTypes = false;
		
		if(StringUtils.isNotBlank(eventJson)){
			Map<String, Object> eventMap = null;
			Map<String, Object> eventRecordMap = null;
			try{
				eventMap = JsonUtils.json2Map(eventJson);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(CommonFunctions.isNotBlank(eventMap, "eventReportRecordInfo")) {
				try{
					eventRecordMap = JsonUtils.json2Map(eventMap.get("eventReportRecordInfo").toString());
					map.addAllAttributes(eventRecordMap);
				}catch(Exception e){
					e.printStackTrace();
				}
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
		
		String forwardUrl = this.toAddEventByType(session, event.getEventId(), trigger, typesForList, isRemoveTypes, map);
		
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
	@RequestMapping(value = "/toAddEventByType")
	public String toAddEventByType(HttpSession session, 
			@RequestParam(value = "eventId", required = false) Long eventId,
			@RequestParam(value = "trigger", required = false, defaultValue="") String trigger,
			@RequestParam(value = "typesForList", required = false) String typesForList,
			@RequestParam(value = "isRemoveTypes", required = false, defaultValue = "false") boolean isRemoveTypes,
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		EventDisposal event = null;
		List<OrgEntityInfoBO> orgEntityInfoList = null;
		
		if(eventId != null && eventId > 0){
			event = eventDisposalService.findEventById(eventId, userInfo.getOrgCode());
			
			if(event != null) {
				String eventInvolvedPeople = event.getEventInvolvedPeople();
				Long instanceId = -1L;
				
				if(StringUtils.isNotBlank(eventInvolvedPeople)) {
					//String[] ipArray = eventInvolvedPeople.split("；"),
							// item = null;
				//	String ip = null;
					//InvolvedPeople involvedPeople = null;

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
		map.addAttribute("GEO_URL", App.GEO.getDomain(session));//标准地址库应用
		
		//获取当前登录账号下所管理的所有网格
		orgEntityInfoList = userInfo.getInfoOrgList();

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

		//String privateFolderName = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.PRIVATE_FOLDER_NAME4_EVENT+"Add", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);

		//事件描述最小字符限制
		String characterLimit = configurationService.turnCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE,ConstantValue.DESCRIP_CHARACTER_LIMIT, IFunConfigurationService.CFG_TYPE_FACT_VAL,  userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(characterLimit)){
			try {
				map.addAttribute("characterLimit",Long.valueOf(characterLimit));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		return "/map/arcgis/arcgis_3d/eventAdd.ftl";
	}
	

	// 事件待办与点位详情部分end
	
	
	
	/**
	 * 大屏一键督察功能
	 */
	@ResponseBody
	@RequestMapping(value = "/quickSupervision")
	public Map<String,Object> quickSupervision(HttpSession session, ModelMap map, @RequestParam Map<String, Object> params
			,@RequestParam(value = "attachmentId", required = false) Long[] ids,@RequestParam(value = "resMarker", required = false) ResMarker resMarker) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("status", "0");
		result.put("msg", "");
		
		DailySupervision dailySupervision = new DailySupervision();
		dailySupervision.setOrgCode(params.get("gridCode").toString());
		//获取网格全路径名称
		MixedGridInfo defaultGridByOrgCode = mixedGridInfoService.getDefaultGridByOrgCode(params.get("gridCode").toString());
		dailySupervision.setOrgCodeName(defaultGridByOrgCode.getGridPath());
		dailySupervision.setEventContent(params.get("content").toString());
		dailySupervision.setEventType(params.get("type").toString());
		dailySupervision.setBizType("01");
		dailySupervision.setAddressInfo(params.get("occurred").toString());
		dailySupervision.setAddressLon(params.get("x").toString());
		dailySupervision.setAddressLat(params.get("y").toString());
		dailySupervision.setUses(0);
		try {
			dailySupervision.setReportTime(DateUtils.convertStringToDate(params.get("happenTimeStr").toString(), DateUtils.PATTERN_24TIME));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		dailySupervision.setCreator(userInfo.getUserId().intValue());
		dailySupervision.setCreateTime(new Date());
		dailySupervision.setUpdator(userInfo.getUserId().intValue());
		dailySupervision.setUpdateTime(new Date());
		
		dailySupervision.setDataSource("PC");
		
		dailySupervision.setIsValid("1");
		
		Long insert = dailySupervisionService.insert(dailySupervision);
		
		if(insert>0) {
			result.put("status", "1");
			//保存附件
			attachmentService.updateBizId(insert, "DailySup", ids);
		}else {
			result.put("msg", "保存失败");
		}
		
		return result;
	}
	
	private static final String NC_12345_NODE_CODE = "task15";//12345平台
	
	/**
	 * 大屏一键督办自动流转到12345
	 */
	@ResponseBody
	@RequestMapping(value = "/autoFlow")
	public Map<String,Object> autoFlow(HttpSession session, ModelMap map, 
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("status", "0");
		result.put("msg", "");
		
		//获取默认的流程人员
		String flowUserId= configurationService.changeCodeToValue(
				ConstantValue.EVENT_VERIFY_ATTRIBUTE, "FLOW_USER_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL,
				userInfo.getOrgCode());
		
		
		//获取默认的流程人员组织
		String flowOrgId= configurationService.changeCodeToValue(
				ConstantValue.EVENT_VERIFY_ATTRIBUTE, "FLOW_ORG_ID", IFunConfigurationService.CFG_TYPE_FACT_VAL,
				userInfo.getOrgCode());
		
		//获取instanceId
		Long instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(Long.valueOf(params.get("eventId").toString()));
		
		//判断组织层级，如果是市级的人员，则进行自动流转到12345
		OrgSocialInfoBO findByOrgId = orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());
		if(findByOrgId.getChiefLevel().equals(ConstantValue.MUNICIPAL_ORG_LEVEL)) {
			Long curNodeTaskId = eventDisposalWorkflowService.curNodeTaskId(instanceId);
			try {
				boolean subWorkFlowForEndingAndEvaluate = eventDisposalWorkflowService.subWorkFlowForEndingAndEvaluate(instanceId, curNodeTaskId.toString(), NC_12345_NODE_CODE, flowUserId, flowOrgId, "一键督办事件自动流转到12345热线处理", userInfo, null, new HashMap<String,Object>());
				if(!subWorkFlowForEndingAndEvaluate) {
					result.put("status", "1");
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("status", "1");
			}
		}
		
		
		return result;
	}
	
	
	
	
	// 跳转到不文明现象页面
	@RequestMapping(value = "/toUncivilizedPage")
	public String toEventVerifyInfo(HttpSession session,
			@RequestParam Map<String, Object> params,
			ModelMap map) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		try {
			Map<String,Object> searchLabelParams=new HashMap<String,Object>();
			searchLabelParams.put("labelModel", "002");
			List<Map<String, Object>> eventLabelList = eventLabelService.searchListByParam(searchLabelParams);
			map.addAttribute("eventLabelDict", eventLabelList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//获取近30天
		Date today = new Date();
        String endDate = DateUtils.convertDateToString(today); //当前日期
        //获取三十天前日期
        Calendar theCa = Calendar. getInstance ();
        theCa .setTime( today );
        theCa .add( theCa . DATE , -30); //最后一个数字30可改，30天的意思
        Date start = theCa .getTime();
        String startDate =  DateUtils.convertDateToString(start); //三十天之前日期
        map.put("createTimeStart", startDate);
        map.put("createTimeEnd", endDate);
        map.put("infoOrgCode", userInfo.getInfoOrgCodeStr());
		
		
		return "/map/arcgis/arcgis_3d/uncivilized_page.ftl";	
	}
	
	
	@RequestMapping(value = "/toImgGalleryPage")
	public Object toImgPage(HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> params, ModelMap map){
		map.putAll(params);
		return "/map/arcgis/arcgis_3d/show_img_page.ftl";
	}

	
	
	
}