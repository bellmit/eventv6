package cn.ffcs.zhsq.map.arcgis.controller;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.gmis.landscapeManage.service.ILandscapeManageService;
import cn.ffcs.gmis.livableLF.LivableLFService;
import cn.ffcs.gmis.mybatis.domain.livableLF.LivableLF;
import cn.ffcs.resident.bo.Pagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfEvent;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.mybatis.domain.relatedEvents.RelatedEvents;
import cn.ffcs.zhsq.relatedEvents.service.ICommonRelatedEventsService;
import cn.ffcs.zhsq.relatedEvents.service.IRelatedEventsService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 2014-06-24
 * arcgis地图定位数据加载控制器
 * @author zhongshm
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisdataofeventlocal")
public class ArcgisDataOfEventLocalController extends ZZBaseController {
	
	@Autowired
	private IEvent4WorkflowService event4WorkflowService;
	
	@Autowired
	private cn.ffcs.shequ.zzgl.service.event.IEventDisposalService ieventDisposalService;

	@Autowired
	private IBaseDictionaryService dictionaryService;

	@Autowired
	private ILandscapeManageService iLandscapeManageService;

	@Autowired
	private LivableLFService livableLFService;
	
	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;

	@Autowired
	private IFunConfigurationService configurationService;
	@Autowired
	private ICommonRelatedEventsService commonRelatedEventsService;
	@Autowired
	private IRelatedEventsService relatedEventsService;
	
	
	@RequestMapping(value = "/toSearchEvent")
	public String toSearchEvent(HttpSession session, 
			HttpServletRequest request, 
			@RequestParam(value = "gridId") Long gridId,
			ModelMap map) {
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		iLandscapeManageService.getLandscapeManageById(1L,"320281");
		List<BaseDataDict> eventBigType = dictionaryService.getDataDictListOfSinglestage(ConstantValue.BIG_TYPE_PCODE, null);
		map.addAttribute("eventBigType", eventBigType);
		map.addAttribute("gridId", gridId);
		return "/map/arcgis/standardmappage/searchEvent.ftl";
	}
	
	/**
	 * 转到arcgis轮廓编辑的panel
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @param themeType
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/toArcgisDataListOfEvent")
	public String toArcgisDataListOfPeople(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "objectName",required=false) String objectName,
			@RequestParam(value = "type",required=false) String type,
			@RequestParam(value = "handleDateFlag",required=false) String handleDateFlag,
			@RequestParam(value = "infoOrgCode",required=false) String infoOrgCode,
			@RequestParam(value = "eventType") String eventType, 
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			@RequestParam(value = "trigger", required = false) String trigger,
			@RequestParam(value = "eventAttrTrigger", required = false) String eventAttrTrigger) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("infoOrgCode", StringUtils.isNotBlank(infoOrgCode)?infoOrgCode:defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("gridId", gridId);
		map.addAttribute("eventType", eventType);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		List<BaseDataDict> eventBigType = dictionaryService.getDataDictListOfSinglestage(ConstantValue.BIG_TYPE_PCODE, userInfo.getOrgCode());
		
		map.addAttribute("objectName", objectName);
		map.addAttribute("handleDateFlag", "01".equals(objectName)? "2":null);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("eventAttrTrigger", eventAttrTrigger);
		
		if(StringUtils.isNotBlank(eventType)){
			String location_single_flag =  configurationService.changeCodeToValue(ConstantValue.LOCATION_SINGLE_FLAG, eventType, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
			map.addAttribute("location_single_flag", location_single_flag);
		}
		//事件列表中可展示的事件类别，空则展示所有
		if(StringUtils.isNotBlank(trigger)){
			String typesForList =  configurationService.changeCodeToValue(ConstantValue.TYPES, trigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
			if(StringUtils.isNotBlank(typesForList)){
				typesForList = typesForList.trim();
				
				String[] typesTemp = typesForList.split(",");
				Set<String> bigTypeSet = new HashSet<String>();
				List<BaseDataDict> bigType = new ArrayList<BaseDataDict>();
				int bigTypeSize = 0;
				
				for(String typeTemp : typesTemp) {//获取所有事件大类，并去重
					if(StringUtils.isNotBlank(typeTemp)) {
						if(typeTemp.length() > 2) {
							typeTemp = typeTemp.substring(0, 2);
						}
						
						bigTypeSet.add(typeTemp);
					}
				}
				
				for(String bigTypeStr : bigTypeSet) {//验证配置值的有效性
					for(BaseDataDict dataDict : eventBigType) {
						if(bigTypeStr.equals(dataDict.getDictGeneralCode())) {
							bigType.add(dataDict); break;
						}
					}
				}
				
				bigTypeSize = bigType.size();
				
				if(bigTypeSize > 0) {//配置无效时，当作没有配置
					if(bigTypeSize == 1) {
						type = typesForList;
					} else if(bigTypeSize > 1) {
						eventBigType = bigType;
					} 
					
					map.addAttribute("typesForList", typesForList);
					map.addAttribute("trigger", trigger);//事件类型筛选触发条件
				}  
			}
		}
		
		map.addAttribute("type", type);
		map.addAttribute("eventBigType", eventBigType);
		String modeType = request.getParameter("modeType");
		if (StringUtils.isNotBlank(modeType)) {
			map.addAttribute("modeType", modeType.trim());
		}
		if(StringUtils.isNotBlank(request.getParameter("statusImg"))) {
			map.addAttribute("statusImg", request.getParameter("statusImg"));
		}
		String forward = "/map/arcgis/standardmappage/standardEvent.ftl";
		String page = request.getParameter("page");
		if("jiangyin".equals(page)){
			forward = "/szzg/event/standardEvent.ftl";
		} else if("globalEye".equals(page)){//用来添加南平视频大屏（南平综治智能监控指挥中心）的地图配置
			//modeType:jurisdiction 人员所在组织的辖区待办
			forward = "/map/arcgis/standardmappage/majorEvent.ftl";
		} else if("multiple".equals(page)) {//多图片
        	map.addAttribute("multiple","multiple");
        }
		
		
		
		return forward;
	}
	
	/**
	 * 台江地图图层分类管理代办事件（个人代办事件和辖区代办事件）
	 */
	@RequestMapping(value="/dbEvent")
	public String dbEvent(HttpSession session, @RequestParam(value="gridId", required=false) Long gridId, @RequestParam(value="gridFlag") Long gridFlag, ModelMap map) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("gridFlag", gridFlag);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String isNewEvent =  configurationService.turnCodeToValue(ConstantValue.IS_USE_NEW_EVENT,"",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isNewEvent", isNewEvent);
		if(null != isNewEvent && isNewEvent.equals("true")){
	        return "/map/arcgis/standardmappage/taijiang/dbEvent.ftl";
		}
		return "/map/arcgis/standardmappage/taijiang/old/dbEvent.ftl";
	}
	
	/**
	 * 事件列表接口替换，后续使用时，如有问题，再调整 2019-07-11 zhangls
	 * @param session
	 * @param page
	 * @param rows
	 * @param gridId
	 * @param content
	 * @param statusName
	 * @param gridFlag
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/dbEventListData", method=RequestMethod.POST)
	public cn.ffcs.system.publicUtil.EUDGPagination listData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId",required=false) String gridId, @RequestParam(value="content", required=false) String content,
			@RequestParam(value="statusName", required=false) String statusName, 
			@RequestParam(value="gridFlag", required=false) String gridFlag) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(page<=0) page=1;
		if(content!=null) content = content.trim();
		if(gridId!=null)gridId = gridId.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userInfo.getUserId());
		params.put("keyWord", content);
		params.put("orgId", userInfo.getOrgId());
		params.put("userOrgCode", userInfo.getOrgCode());
		
		cn.ffcs.system.publicUtil.EUDGPagination pagination = new cn.ffcs.system.publicUtil.EUDGPagination();
		if(!StringUtils.isBlank(gridFlag) && gridFlag.equals("0")){//个人待办
			params.put("listType", 1);
			
			try {
				pagination = event4WorkflowService.findEventListPagination(page, rows, params);
			} catch (ZhsqEventException e) {
				e.printStackTrace();
			}
		}else{//辖区待办
			if(StringUtils.isNotBlank(gridId)) {
				MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(Long.valueOf(gridId), false);
				
				if(gridInfo != null) {
					params.put("infoOrgCode", gridInfo.getInfoOrgCode());
					params.put("statusList", "00,01,02,03");
					params.put("listType", 5);
					
					try {
						pagination = event4WorkflowService.findEventListPagination(page, rows, params);
					} catch (ZhsqEventException e) {
						e.printStackTrace();
					}
				}
			}
		}
//		EUDGPagination pagination = ieventDisposalService.findEventDisposalPagination(page, rows, params);
		return pagination;
	}


	/**
	 * @Title: index
	 * @Description: 宜居罗坊页面
	 * @param @param session
	 * @param @param gridId
	 * @param @param resTypeId
	 * @param @param map
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping(value="/livableLF")
	public String livableLF(HttpSession session,
							@RequestParam(value = "livableType",required = false) String livableType,
							@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,ModelMap map) {

		if((livableType == null || "".equals(livableType))&&(elementsCollectionStr!=null||"".equals(elementsCollectionStr))){
			String menuCode = this.analysisOfElementsCollection(elementsCollectionStr,"menuCode");
			if("LivableLF1".equals(menuCode)) {
				livableType="xncjs";
			}else if("LivableLF2".equals(menuCode)) {
				livableType="yzccq";
			}else if("LivableLF3".equals(menuCode)) {
				livableType="ltxd";
			}
		}
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("orgCode", userInfo.getInfoOrgCodeStr());

		map.addAttribute("livableType", livableType);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/livableLF/livableLFList.ftl";
	}

	/**
	 * @Title: index
	 * @Description: 宜居罗坊详情页面
	 * @param @param session
	 * @param @param gridId
	 * @param @param resTypeId
	 * @param @param map
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping(value="/livableLFDetail")
	public String livableLF(HttpSession session,
							@RequestParam(value = "id",required = false) Long id,
							ModelMap map) {

		LivableLF livableLF=livableLFService.searchById(id);
		if(livableLF!=null)
		{
			map.addAttribute("livableLF", livableLF);
		}


		return "/map/arcgis/standardmappage/livableLF/livableLFDetail.ftl";
	}


	/**
	 * 宜居罗坊列表数据
	 */
	@ResponseBody
	@RequestMapping(value="/livableLFListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination livableLFListData(HttpServletRequest request, HttpSession session, ModelMap map,
								   int page, int rows,
								   @RequestParam(value = "orgCode",required = false) Long orgCode,
								   @RequestParam(value = "name",required = false) String name,
								   @RequestParam(value = "livableType",required = false) String livableType,
								   @RequestParam(value = "survey",required = false) String survey,
											@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		if((livableType == null || "".equals(livableType))&&(elementsCollectionStr!=null||"".equals(elementsCollectionStr))){
			String menuCode = this.analysisOfElementsCollection(elementsCollectionStr,"menuCode");
			if("LivableLF1".equals(menuCode)) {
				livableType="xncjs";
			}else if("LivableLF2".equals(menuCode)) {
				livableType="yzccq";
			}else if("LivableLF3".equals(menuCode)) {
				livableType="ltxd";
			}
		}
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();

		if(name!=null&&!name.equals(""))
		{
			params.put("name",name);
		}
		if(orgCode!=null&&!orgCode.equals(""))
		{
			params.put("orgCode",orgCode);
		}
		if(survey!=null&&!survey.equals(""))
		{
			params.put("survey",survey);
		}

		if(livableType!=null)
		{
			params.put("livableType",livableType);
		}


		cn.ffcs.common.EUDGPagination pagination = livableLFService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 2014-09-03 chenlf add
	 * 获取巡逻队、警务室、治保组织等社会组织的定位信息
	 * @param session
	 * @param ids ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfLivableLF")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfLivableLF(HttpSession session, ModelMap map,
																	  HttpServletResponse res, @RequestParam(value = "ids") String ids,
																	  @RequestParam(value = "mapt") Integer mapt,
																	  @RequestParam(value = "markerType", required = false) String markerType,
																	  @RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		markerType = "LIVABLE_LF";
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisLivableLocateDataListByIds(ids, mapt,
				markerType);
		for (ArcgisInfoOfPublic arcgisInfoOfPublic : list) {
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}

	
	@ResponseBody
	@RequestMapping(value="/old/dbEventListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination listDataOld(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId",required=false) String gridId, @RequestParam(value="content", required=false) String content,
			@RequestParam(value="statusName", required=false) String statusName, 
			@RequestParam(value="gridFlag", required=false) String gridFlag) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(page<=0) page=1;
		if(content!=null) content = content.trim();
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
		params.put("content", content);
		params.put("statusList", statusList);
		params.put("orgCode", userInfo.getOrgCode());
		params.put("statusName", statusName);
		cn.ffcs.common.EUDGPagination pagination = ieventDisposalService.findEventDisposalPagination(page, rows, params);
		return pagination;
	}
	
	/**
	 * 2014-06-25 zhongshm
	 * 事件数据
	 * 事件列表接口替换，后续使用时，如有问题，再调整 2019-07-11 zhangls
	 * @param session
	 * @param page
	 * @param gridId
	 * @param rows
	 * @param order
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisDataListOfEvent", method = RequestMethod.POST)
	public cn.ffcs.system.publicUtil.EUDGPagination getArcgisDataListOfPeople(HttpSession session,HttpServletResponse response,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value = "startHappenTime", required=false) String startHappenTime,
			@RequestParam(value = "endHappenTime", required=false) String endHappenTime,
			@RequestParam(value = "keyWord", required=false) String keyWord,
			@RequestParam(value = "types", required=false) String types,
			@RequestParam(value = "eventType") String eventType){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", userInfo.getOrgId());
		params.put("userId", userInfo.getUserId());
		params.put("userOrgCode", userInfo.getOrgCode());
		
		if(!StringUtils.isBlank(keyWord)){
			keyWord = keyWord.trim();
			params.put("keyWord", keyWord);
		}
		
		cn.ffcs.system.publicUtil.EUDGPagination eudgPagination = new cn.ffcs.system.publicUtil.EUDGPagination();
		if("todo".equals(eventType)) {//待办事件
			params.put("listType1", 1);
			
			try {
				eudgPagination = event4WorkflowService.findEventListPagination(page, rows, params);
			} catch (ZhsqEventException e) {
				e.printStackTrace();
			}
		}
		
		return eudgPagination;
	}
	
	/**
	 * 2014-06-26 zhongshm add
	 * 获取事件的定位信息
	 * @param session
	 * @param eventIds 人员ids(eventID,workFlowId,instanceId,taskId,eventType)
	 * @param mapt 地图类型
	 * @param type 人员类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfEvent")
	public List<ArcgisInfoOfEvent> getArcgisEventLocateDataList(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value = "multiple", required=false) String multiple,
			@RequestParam(value = "statusImg", required=false) String statusImg,
			@RequestParam(value = "modeType", required=false) String modeType,
			@RequestParam(value = "mapt", required=false) Integer mapt) {		
		String eventIds = "";
		String[] results = ids.split(",");
		for(String result : results){
			String eventId = result.split("!")[0];
			eventIds = eventIds + "," + eventId;
		}
		List<ArcgisInfoOfEvent> eventlist = new ArrayList<ArcgisInfoOfEvent>();

		if(results.length<=0){
			return eventlist;
		}
		eventIds = eventIds.substring(1, eventIds.length());
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisEventLocateDataListByIds(eventIds,mapt);
		boolean changeSmallIco =false;//定位图片是否动态替换
		boolean changeStatusIco =false;//定位图片是否动态替换 根据事件的紧急程度
		String ico = "";
		if(multiple != null) {//是否存在多图片替换
			changeSmallIco = true;
		}
		if(statusImg != null && statusImg != "undefined") {
			changeStatusIco = true;
		}
		for(ArcgisInfoOfPublic arcgisInfoOfPublic : list){
			ArcgisInfoOfEvent arcgisInfoOfEvent = new ArcgisInfoOfEvent();
//			arcgisInfoOfEvent.setWid(arcgisInfoOfPublic.getWid());
			arcgisInfoOfEvent.setX(arcgisInfoOfPublic.getX());
			arcgisInfoOfEvent.setY(arcgisInfoOfPublic.getY());
			arcgisInfoOfEvent.setMapt(arcgisInfoOfPublic.getMapt());
			arcgisInfoOfEvent.setGridName(arcgisInfoOfPublic.getName());
			arcgisInfoOfEvent.setName(arcgisInfoOfPublic.getName());
			arcgisInfoOfEvent.setModeType(modeType);
			if(changeSmallIco) {
				if("3".equals(arcgisInfoOfPublic.getHandleStatus())) {//超期
					ico="event_yellow.png";
				}else {
					switch(arcgisInfoOfPublic.getStatus()) {
					case "00":case "01":case "02":
						ico="event_red.png";break;
					case "03":case "04":
						ico="event_green.png";break;
					}
				}
				arcgisInfoOfEvent.setElementsCollectionStr("smallIco_,_/images/map/gisv0/map_config/unselected/event/"+ico);
			}
			if(changeStatusIco) {
				// 04 重大公共应急事件  03 加急  02 紧急 01 一般
				ico = "event_radar.gif";
				switch(arcgisInfoOfPublic.getUrgencyDegree()) {
				case "04":
					ico="event_major.gif";break;
				case "02":
					ico="event_emergency.gif";break;
				}
				arcgisInfoOfEvent.setElementsCollectionStr("smallIco_,_/images/map/gisv0/map_config/unselected/event/"+ico);
			}
			for(String result : results){
				String[] re=result.split("!");
				if(arcgisInfoOfPublic.getWid().toString().equals(re[0])){
					String wid = arcgisInfoOfPublic.getWid() + "," + re[1] + "," + re[2] + "," + re[3] + "," + re[4];
					arcgisInfoOfEvent.setWid(wid);
					    if(  null!=re[1]&& !"undefined".equals(re[1]) && !"null".equals(re[1])){
							arcgisInfoOfEvent.setWorkFlowId(Long.parseLong(re[1]));
						}
						if(null!=re[2]&& !"undefined".equals(re[2]) && !"null".equals(re[2])){
							arcgisInfoOfEvent.setInstanceId(Long.parseLong(re[2]));
						}
						if(null!=re[3]&& !"undefined".equals(re[3]) && !"null".equals(re[3])){
							arcgisInfoOfEvent.setTaskId(Long.parseLong(re[3]));
						}
						if(null!=re[4]&& !"undefined".equals(re[4]) && !"null".equals(re[4])){
							arcgisInfoOfEvent.setEventType(re[4]);
						}
				}
			}
			eventlist.add(arcgisInfoOfEvent);
		}
		return eventlist;
	}
	
	/**
	 * 获取事件定位
	 * @param session
	 * @param ids 事件id
	 * @param mapt地图类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataList")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataList(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt", required=false) Integer mapt) {	
		String eventIds = "";
		String[] results = ids.split(",");
		for(String result : results){
			String eventId = result.split("!")[0];
			eventIds = eventIds + "," + eventId;
		}
		eventIds = eventIds.substring(1, eventIds.length());
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisEventLocateDataListByIds(eventIds,mapt);
		return list;
	}

	/**
	 * 莆田地图首页页面已办(结案+评价)
	 * @param session
	 * @param orgCode 信息域组织编码
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/hadDoEventPutian")
	public String hadDoEventPutian(HttpSession session,
			@RequestParam(value = "orgCode") String orgCode, ModelMap map) {
//		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String gridId = defaultGridInfo.get(KEY_START_GRID_ID).toString();
		
		map.addAttribute("gridId", gridId);
		map.addAttribute("startOrgCode", orgCode);
		return "/map/arcgis/standardmappage/putian/hadDoEvent.ftl";
	}
	
	/**
	 * 莆田地图首页页面批办事件
	 * @param session
	 * @param orgCode 信息域组织编码
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/batchEventPutian")
	public String batchEventPutian(HttpSession session,
			@RequestParam(value = "orgCode") String orgCode, ModelMap map) {
//		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String gridId = defaultGridInfo.get(KEY_START_GRID_ID).toString();
		
		map.addAttribute("gridId", gridId);
		map.addAttribute("startOrgCode", orgCode);
		return "/map/arcgis/standardmappage/putian/batchEvent.ftl";
	}
	
	/**
	 * 已办事件数据
	 * @param session
	 * @param page 页码
	 * @param rows 每页条数
	 * @param orgCode 信息域组织编码
	 * @param platformName 平台名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/hadDoEventListData", method=RequestMethod.POST)
	public Pagination hadDoEventListData(HttpSession session, 
			@RequestParam(value="page") int page, 
			@RequestParam(value="rows") int rows,
			@RequestParam(value="orgCode") String orgCode, 
			@RequestParam(value="platformName", required=false) String platformName) {
		if(page<=0) page=1;
		if(platformName!=null) platformName = platformName.trim();
		return null;
	}
	
	/**
	 * 批办事件数据
	 * @param session
	 * @param page 页码
	 * @param rows 每页条数
	 * @param orgCode 信息域组织编码
	 * @param platformName 平台名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/batchEventListData", method=RequestMethod.POST)
	public Pagination batchEventListData(HttpSession session, 
			@RequestParam(value="page") int page, 
			@RequestParam(value="rows") int rows,
			@RequestParam(value="orgCode") String orgCode, 
			@RequestParam(value="platformName", required=false) String platformName) {
		if(page<=0) page=1;
		if(platformName!=null) platformName = platformName.trim();
		return null;
	}
	
	@RequestMapping(value = "/toMajorEvent")
	public String toMajorEvent(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "mapt", required = false) Integer mapt,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		map.addAttribute("mapt", mapt);
		map.addAttribute("gridId", gridId);
		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("eventTypeDC", ConstantValue.EVENT_TYPE_DC);
		map.addAttribute("eventLevelDC", ConstantValue.EVENT_LEVEL_DC);
		return "/map/arcgis/standardmappage/relatedEvent/standardMajorEvent.ftl";
	}
	
	@RequestMapping(value="/toMajorEventDetail")
	public String toMajorEventDetail(HttpSession session, @RequestParam(value = "reId") Long reId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		RelatedEvents eventInfo = commonRelatedEventsService.findRelatedEventsById(reId, userInfo.getOrgCode());
		map.addAttribute("eventInfo", eventInfo);
		MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(eventInfo.getGridCode());
		Long gridId = gridInfo.getGridId();
		String infoOrgCode=gridInfo.getInfoOrgCode();
		
		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
		}
		map.addAttribute("gridNames", gridNames);
		
		return "/map/arcgis/standardmappage/relatedEvent/standardMajorEventDetail.ftl";
	}
	
	@RequestMapping(value = "/toSchoolEvent")
	public String toSchoolEvent(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "mapt", required = false) Integer mapt,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		map.addAttribute("mapt", mapt);
		map.addAttribute("gridId", gridId);
		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/relatedEvent/standardSchoolEvent.ftl";
	}
	
	@RequestMapping(value="/toSchoolEventDetail")
	public String toSchoolEventDetail(HttpSession session, @RequestParam(value = "reId") Long reId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		RelatedEvents eventInfo = commonRelatedEventsService.findRelatedEventsById(reId, userInfo.getOrgCode());
		map.addAttribute("eventInfo", eventInfo);
		return "/map/arcgis/standardmappage/relatedEvent/standardSchoolEventDetail.ftl";
	}
	
	@RequestMapping(value = "/toRoadEvent")
	public String toRoadEvent(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "mapt", required = false) Integer mapt,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		map.addAttribute("mapt", mapt);
		map.addAttribute("gridId", gridId);
		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/relatedEvent/standardRoadEvent.ftl";
	}
	
	@RequestMapping(value="/toRoadEventDetail")
	public String toRoadEventDetail(HttpSession session, @RequestParam(value = "reId") Long reId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		RelatedEvents eventInfo = relatedEventsService.findRelatedEventsById(reId, userInfo.getOrgCode());
		map.addAttribute("eventInfo", eventInfo);
		return "/map/arcgis/standardmappage/relatedEvent/standardRoadEventDetail.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/relatedEventListData", method = RequestMethod.POST)
	public EUDGPagination relatedEventListData(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, 
			@RequestParam(value = "mapt", required = false) Integer mapt,
			@RequestParam(value = "gridId") String gridId,
			@RequestParam(value = "bizType") String bizType) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if (page <= 0) page = 1;
		String keyWord = request.getParameter("keyWord");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String eventType = request.getParameter("eventType");
		String eventLevel = request.getParameter("eventLevel");
		String infoOrgCode = request.getParameter("infoOrgCode");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("keyWord", keyWord);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("eventType", eventType);
		params.put("eventLevel", eventLevel);
		params.put("infoOrgCode", infoOrgCode);
		params.put("mapType", mapt);
		params.put("bizType", bizType);
		params.put("markerType", ConstantValue.MAP_TYPE_SAFE_EVENT);
		EUDGPagination pagination = null;
		if ("2".equals(bizType)) {
			pagination = commonRelatedEventsService.findSchoolEventsForGis(page, rows, params, userInfo.getOrgCode());
		} else if ("1".equals(bizType)) {
			pagination = commonRelatedEventsService.findRoadEventsForGis(page, rows, params, userInfo.getOrgCode());
		} else {
			pagination = commonRelatedEventsService.findRelatedEventsForGis(page, rows, params, userInfo.getOrgCode());
		}
		return pagination;
	}
}
