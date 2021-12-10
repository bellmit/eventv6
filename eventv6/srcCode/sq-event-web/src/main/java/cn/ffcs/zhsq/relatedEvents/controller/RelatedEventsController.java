package cn.ffcs.zhsq.relatedEvents.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.zhsq.base.local.service.IGridLogService;
import cn.ffcs.zhsq.base.local.service.LogModule;
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

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CareRoad;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.ICareRoadService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEvents;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEventsForSearch;
import cn.ffcs.zhsq.relatedEvents.service.IRelatedEventsService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 路案事件
 * @author zhangls
 *
 */
@Controller
@RequestMapping("/zhsq/relatedEvents/RelatedEventsController")
public class RelatedEventsController extends ZZBaseController {
	@Autowired
	private IRelatedEventsService relatedEventsService;
	
	@Autowired
	private IBaseDictionaryService dictionaryService;
	
	//护路护线接口
	@Autowired
	private ICareRoadService careRoadService;
	
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;

	@Autowired
	private IResMarkerService resMarkerService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 跳转到新增页面或者编辑页面
	 * @param session
	 * @param involvedId 涉事案件主键，不为空时，跳转到编辑页面
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddRelatedEvents")
	public String toAddRelatedEvents(HttpSession session, 
			@RequestParam(value = "reId", required = false) Long reId,
			@RequestParam(value = "bizType", required=false) String bizType,
			ModelMap map) {
		bizType = StringUtils.isNotBlank(bizType) ? bizType : "1";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		RelatedEvents relatedEvents = null;
		if(reId!=null && !reId.equals(-1)){
			relatedEvents = relatedEventsService.findRelatedEventsById(reId, userInfo.getOrgCode());
		}else{
			relatedEvents = new RelatedEvents();
			relatedEvents.setOccuDateStr(DateUtils.getNow());
			relatedEvents.setBizType(bizType);
		}
		
		List<BaseDataDict> prisonersDocTypeList = dictionaryService.getDataDictListOfSinglestage(ConstantValue.PRISONERS_DOC_TYPE, userInfo.getOrgCode());
		List<BaseDataDict> natureList = dictionaryService.getDataDictListOfSinglestage(ConstantValue.NATURE, userInfo.getOrgCode());
		
		
		map.addAttribute("relatedEvents", relatedEvents);
		
		map.addAttribute("prisonersDocTypeList", prisonersDocTypeList);
		map.addAttribute("natureList", natureList);
		
		map.addAttribute("prisonersDocTypePcode", ConstantValue.PRISONERS_DOC_TYPE);
		map.addAttribute("naturePcode", ConstantValue.NATURE);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		map.addAttribute("gridId", gridId);
		map.addAttribute("module", ConstantValue.MAP_TYPE_SAFE_EVENT);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		return "/zzgl/relatedEvents/add_relatedEvents.ftl";
	}
	
	/**
	 * 获取所在地区
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listCareRoads1", method = RequestMethod.POST)
	public Map<String, Object> listCareRoads1(HttpSession session, 
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session),
							resultMap = new HashMap<String, Object>();
		boolean result = false;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", defaultGridInfo.get(KEY_START_GRID_ID));		
		cn.ffcs.common.EUDGPagination careRoadsPagination = careRoadService.findCareRoadPagination(1, 20, params, userInfo.getOrgCode());
		List<CareRoad> careRoadsList = (List<CareRoad>) careRoadsPagination.getRows();
		StringBuffer careRoadsData = new StringBuffer("[");
		if(careRoadsList!=null && careRoadsList.size()>0){
			result = true;
			for(CareRoad careRoad : careRoadsList){
				careRoadsData.append("{lotName:'").append(careRoad.getLotName()).append("',lotId:'").append(careRoad.getLotId()).append("'},");
			}
		}
		careRoadsData.append("]");
		
		resultMap.put("result", result);
		resultMap.put("careRoadsData", careRoadsData.toString());
		return resultMap;
	}
	
	/**
	 * 获取所在地区
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listCareRoads", method = RequestMethod.POST)
	public List<Map<String, Object>> listCareRoads(HttpSession session, 
			@RequestParam(value = "lotName", required = false) String lotName,
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(!StringUtils.isBlank(lotName)){
			lotName = lotName.trim();
			params.put("lotName", lotName);
		}
		
		params.put("gridId", defaultGridInfo.get(KEY_START_GRID_ID));		
		cn.ffcs.common.EUDGPagination careRoadsPagination = careRoadService.findCareRoadPagination(1, 2000, params, userInfo.getOrgCode());
		List<CareRoad> careRoadsList = (List<CareRoad>) careRoadsPagination.getRows();
		
		List<Map<String, Object>> careRoadsData = new ArrayList<Map<String, Object>>();
		for(int index = 0, lenght = careRoadsList.size(); index < lenght; index++){
			CareRoad careRoad = careRoadsList.get(index);
			Map<String, Object> careRoadsMap = new HashMap<String, Object>();
			careRoadsMap.put("lotName", careRoad.getLotName());
			careRoadsMap.put("lotId", careRoad.getLotId());
			careRoadsData.add(careRoadsMap);
		}
		return careRoadsData;
	}
	
	/**
	 * 编辑涉事案件信息
	 * @param session
	 * @param relatedEvents 涉事案件对象
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editRelatedEvents", method = RequestMethod.POST)
	public Map<String, Object> editRelatedEvents(
			HttpSession session,
			HttpServletRequest request,
			@ModelAttribute(value = "relatedEvents") RelatedEvents relatedEvents,
			ModelMap map) {
		UserInfo userInfo = (UserInfo)session.getAttribute(ConstantValue.USER_IN_SESSION);
		RelatedEvents beforeRelatedEvents = relatedEventsService.findRelatedEventsById(relatedEvents.getReId(), userInfo.getOrgCode());
		boolean result = false;
		//csk 查询日志
		try {
			relatedEvents.setReNo(beforeRelatedEvents.getReNo());
			List<ResMarker> bList = resMarkerService.findResMarkerByParam(ConstantValue.MAP_TYPE_SAFE_EVENT,beforeRelatedEvents.getReId(),null);//修改前坐标
			JSONObject beforeJson = JSONObject.fromObject(beforeRelatedEvents);
            if(bList !=null &&bList.size()>0){
				String bresMarker = "X:"+bList.get(0).getX() +"Y:"+ bList.get(0).getY();
				beforeJson.put("resMarker",bresMarker);
			}
			CareRoad careRoad = careRoadService.findCareRoadById(beforeRelatedEvents.getBizId());
			if(careRoad != null){
				beforeRelatedEvents.setBizName(careRoad.getLotName());
				beforeJson.put("bizName",beforeRelatedEvents.getBizName());
			}
			String valueBefore=beforeJson.toString();
			//更新
			result = relatedEventsService.updateRelatedEvents(relatedEvents);
			
			RelatedEvents afterRelatedEvents = relatedEventsService.findRelatedEventsById(
					relatedEvents.getReId(), userInfo.getOrgCode());
			JSONObject afterJson = JSONObject.fromObject(afterRelatedEvents);
			
			List<ResMarker> bListAfter = resMarkerService.findResMarkerByParam(ConstantValue.MAP_TYPE_SAFE_EVENT,beforeRelatedEvents.getReId(),null);//修改前坐标
            if(bListAfter !=null &&bListAfter.size()>0){
				String bresMarker = "X:"+bListAfter.get(0).getX() +"Y:"+ bListAfter.get(0).getY();
				afterJson.put("resMarker",bresMarker);
			}
			
			String valueAfter = afterJson.toString();
			logService.savelog2(request, LogModule.roadCase, IGridLogService.ActionType.update,userInfo.getOrgId(),
					"更新表"+LogModule.roadCase.getTableName(),valueBefore,valueAfter);
		} catch (Exception e) {
			logger.info("保存护路护线查询日志出错！");
			e.printStackTrace();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result);
		return resultMap;
	}


	/**
	 * 保存涉事案件信息
	 * @param session
	 * @param relatedEvents 涉事案件对象
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveRelatedEvents", method = RequestMethod.POST)
	public Map<String, Object> saveRelatedEvents(
			HttpSession session,
			HttpServletRequest request,
			@ModelAttribute(value = "relatedEvents") RelatedEvents relatedEvents,
			ModelMap map) {
		UserInfo userInfo = (UserInfo)session.getAttribute(ConstantValue.USER_IN_SESSION);
		relatedEvents.setCreateUserId(userInfo.getUserId());
		
		Long result = relatedEventsService.insertRelatedEvents(relatedEvents);
		//csk 查询日志
		try {
			JSONObject afterJson = JSONObject.fromObject(relatedEvents);
			String resmarker = "X:"+relatedEvents.getResMarker().getX() +"Y:"+ relatedEvents.getResMarker().getY();
			afterJson.put("resMarker",resmarker);
			CareRoad careRoad = careRoadService.findCareRoadById(relatedEvents.getBizId());
			if(careRoad != null){
				relatedEvents.setBizName(careRoad.getLotName());
				afterJson.put("bizName",relatedEvents.getBizName());
			}
			String valueAfter = afterJson.toString();
			logService.savelog2(request, LogModule.roadCase, IGridLogService.ActionType.insert,userInfo.getOrgId(),
					"新增数据至表"+LogModule.roadCase.getTableName(),"",valueAfter);
		} catch (Exception e) {
			logger.info("保存护路护线查询日志出错！");
			e.printStackTrace();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result > 0);
		return resultMap;
	}
	
	/**
	 * 删除涉事案件信息
	 * @param session
	 * @param request
	 * @param idStr 涉事案件主键串，以英文逗号分隔
	 * @return 返回受影响的行数，存放在map中，键值为result
	 */
	@ResponseBody
	@RequestMapping(value = "/delRelatedEvents", method = RequestMethod.POST)
	public Map<String, Object> delRelatedEvents(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "idStr") String idStr) {
		String[] ids = idStr.split(",");
		List<Long> involvedIdList = new ArrayList<Long>();
		UserInfo userInfo = (UserInfo)session.getAttribute(ConstantValue.USER_IN_SESSION);
		for(int index = 0, length = ids.length; index < length; index++){
			long involvedId = Long.parseLong(ids[index]);
			involvedIdList.add(involvedId);
			//csk 查询日志
			RelatedEvents byId = relatedEventsService.findRelatedEventsById(involvedId, userInfo.getOrgCode());
			List<ResMarker> bList = resMarkerService.findResMarkerByParam(ConstantValue.MAP_TYPE_SAFE_EVENT,byId.getReId(),null);//修改前坐标
			JSONObject beforeJson = JSONObject.fromObject(byId);
			String bresMarker = "";
			if(bList !=null &&bList.size()>0){
				bresMarker = "X:"+bList.get(0).getX() +"Y:"+ bList.get(0).getY();
			}else {
				bresMarker = "X:" +"Y:";
			}
			beforeJson.put("resMarker",bresMarker);
			beforeJson.put("bizName",byId.getBizName());
			String valueBefore=beforeJson.toString();
			logService.savelog2(request, LogModule.roadCase, IGridLogService.ActionType.delete,userInfo.getOrgId(),
					"删除表"+LogModule.roadCase.getTableName(),valueBefore,"");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int result = relatedEventsService.deleteRelatedEventsByIds(involvedIdList);

		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 跳转详情页面
	 * @param session
	 * @param reId 涉事案件主键
	 * @param map result为true表示查询成功；relatedEvents 返回的对象
	 * @return
	 */
	@RequestMapping(value = "/detailRelatedEvents")
	public String detailRelatedEvents(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "reId") Long reId,
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		RelatedEvents relatedEvents = relatedEventsService.findRelatedEventsById(reId, userInfo.getOrgCode());
		boolean result = false;//用于判断详情是否获取成功
		if(relatedEvents != null){
			result = true;
		}else{
			relatedEvents = new RelatedEvents();
		}
		
		map.addAttribute("result", result);
		map.addAttribute("relatedEvents", relatedEvents);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		map.addAttribute("gridId", gridId);
		map.addAttribute("module", ConstantValue.MAP_TYPE_SAFE_EVENT);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		String showType = request.getParameter("showType");
		map.addAttribute("showType", showType);
		logService.savelog2(request, LogModule.roadCase, IGridLogService.ActionType.view,reId,
				"查看表："+LogModule.roadCase.getTableName()+"中的记录："+reId,"","");

		return "/zzgl/relatedEvents/detail_relatedEvents.ftl";
	}

	/**
	 * 跳转至列表展示
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/listRelatedEvents")
	public String listRelatedEvents(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "beginTime", required = false) String beginTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@ModelAttribute(value = "relatedEventsForSearch") RelatedEventsForSearch relatedEventsForSearch,
			ModelMap map) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		MixedGridInfo gridInfo = null;
		Object gridIdObj = defaultGridInfo.get(KEY_START_GRID_ID),
			   infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE),
				gridName=defaultGridInfo.get(KEY_START_GRID_NAME);
		
		Long gridId = relatedEventsForSearch.getGridId();
		String gridCode = relatedEventsForSearch.getGridCode(),
			   bizType = relatedEventsForSearch.getBizType(),
			   nature = relatedEventsForSearch.getNature(),
			   occuDateStartStr = relatedEventsForSearch.getOccuDateStartStr(),
			   occuDateEndStr = relatedEventsForSearch.getOccuDateEndStr();
		
		if (gridId != null && gridId > 0) {
			gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, true);
		} else if(StringUtils.isNotBlank(gridCode)) {
			gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(gridCode);
		}
		
		if(gridInfo != null) {
			gridIdObj = gridInfo.getGridId();
			infoOrgCode = gridInfo.getInfoOrgCode();
			gridName = gridInfo.getGridName();
		}
		
		if(beginTime!=null && !"".equals(beginTime)){
			map.addAttribute("beginTime", beginTime);
		}
		if(endTime!=null && !"".equals(endTime)){
			map.addAttribute("endTime", endTime);
		}
		
		map.addAttribute("gridId", gridIdObj);
		map.addAttribute("gridCode", infoOrgCode);
		map.addAttribute("gridName", gridName);
		
		if (StringUtils.isNotBlank(nature)) {
			map.addAttribute("nature", nature);
		}
		if(StringUtils.isNotBlank(occuDateStartStr)) {
			map.addAttribute("occuDateStartStr", occuDateStartStr);
		}
		if(StringUtils.isNotBlank(occuDateEndStr)) {
			map.addAttribute("occuDateEndStr", occuDateEndStr);
		}
		
		if (StringUtils.isNotBlank(nature)) {
			map.addAttribute("nature", nature);
		}
		bizType = StringUtils.isNotBlank(bizType) ? bizType : "1";
		map.addAttribute("bizType", bizType);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		
		return "/zzgl/relatedEvents/list_relatedEvents.ftl";
	}
	
	/**
	 * 跳转至列表展示
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/listRelatedEventsForMap")
	public String listRelatedEventsForMap(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "id", required=false) Long gridId,
			@RequestParam(value = "gridCode", required=false) String gridCode,
			@RequestParam(value = "bizType", required=false) String bizType,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		if(StringUtils.isBlank(gridCode)){
			gridCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		}
		map.addAttribute("gridCode", gridCode);
		map.addAttribute("gridId", Long.valueOf(gridId));
		String nature = request.getParameter("nature");
		if (StringUtils.isNotBlank(nature)) {
			map.addAttribute("nature", nature);
		}
		bizType = StringUtils.isNotBlank(bizType) ? bizType : "1";
		map.addAttribute("bizType", bizType);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		return "/zzgl/relatedEvents/list_relatedEvents.ftl";
	}
	
	/**
	 * 列表数据加载
	 * @param request
	 * @param session
	 * @param page
	 * @param rows
	 * @param keyWord 关键字
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listRelatedEventsData", method = RequestMethod.POST)
	public EUDGPagination listRelatedEventsData(
			HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "beginTime", required = false) String beginTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@ModelAttribute(value = "relatedEventsForSearch") RelatedEventsForSearch relatedEventsForSearch,
			@RequestParam(value = "keyWord", required = false) String keyWord){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if (page <= 0)
			page = 1;
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		
		Map<String, Object> params = new HashMap<String, Object>();
		String gridCode = relatedEventsForSearch.getGridCode();
		Long gridId = relatedEventsForSearch.getGridId();
		
		if(StringUtils.isBlank(gridCode) && (gridId==null || gridId<0)){
			relatedEventsForSearch.setGridCode(defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString());
		}
		if(beginTime != null && !"".equals(beginTime)) {
			params.put("beginTime", beginTime);
		}
		if(endTime != null && !"".equals(endTime)) {
			params.put("endTime", endTime);
		}
		params.put("relatedEventsForSearch", relatedEventsForSearch);
		params.put("keyWord", keyWord);
		
		EUDGPagination pagination = relatedEventsService.findRelatedEventsPagination(
				page, rows, params, userInfo.getOrgCode());
		
		logService.savelog2(request, LogModule.roadCase, IGridLogService.ActionType.query,null,
				 "查询表："+LogModule.roadCase.getTableName()+"，条数："+pagination.getTotal(),"","");
		
		return pagination;
	}
}
