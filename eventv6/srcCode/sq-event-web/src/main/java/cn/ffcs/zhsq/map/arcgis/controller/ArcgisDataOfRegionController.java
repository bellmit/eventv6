package cn.ffcs.zhsq.map.arcgis.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.ffcs.shequ.zzgl.service.grid.*;
import cn.ffcs.shequ.zzgl.service.legal.ILegalInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.gmis.dangerChemistryManager.service.IDangerChemistryManagerService;
import cn.ffcs.gmis.majorindustry.service.MajorIndustryService;
import cn.ffcs.gmis.mybatis.domain.dangerChemistryManager.DangerChemistryManager;
import cn.ffcs.gmis.mybatis.domain.majorindustry.MajorIndustry;
import cn.ffcs.gmis.mybatis.domain.nic.NicInfo;
import cn.ffcs.gmis.mybatis.domain.policeManagement.PoliceManagement;
import cn.ffcs.gmis.mybatis.domain.railwayDefence.zzSmallPlaces.ZzSmallPlaces;
import cn.ffcs.gmis.mybatis.domain.serviceOutlets.ServiceOutlets;
import cn.ffcs.gmis.nic.service.INicService;
import cn.ffcs.gmis.policeManagement.PoliceManagementService;
import cn.ffcs.gmis.railwayDefence.zzSmallPlaces.service.IZzSmallPlacesService;
import cn.ffcs.gmis.serviceOutlets.service.IServiceOutletsService;
import cn.ffcs.lzly.mybatis.domain.dangroad.DangRoad;
import cn.ffcs.lzly.service.dangroad.DangRoadService;
import cn.ffcs.resident.bo.PartyIndividual;
import cn.ffcs.resident.service.PartyIndividualService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.grid.domain.db.StoreInfo;
import cn.ffcs.shequ.grid.service.IStoreInfoService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.AreaBuildingInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.AreaRoomRent;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CiRsRoom;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CorBaseInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CorEnv;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CorpCiRsTop;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CorpDept;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CorpTaxInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.GridAdmin;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PlaceFireFightMeasure;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PlaceFloorRela;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PlaceInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PlaceType;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.data.GisMapInfo;
import cn.ffcs.shequ.resident.service.IResidentService;
import cn.ffcs.shequ.wap.domain.pojo.Bar;
import cn.ffcs.shequ.wap.domain.pojo.Campus;
import cn.ffcs.shequ.workflow.ict.service.ISafeCheckDetailService;
import cn.ffcs.shequ.zzgl.service.event.IEventDisposalService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfEvent;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 2014-07-01 chenlf add
 * arcgis "地" 定位数据加载控制器
 * @author chenlf
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisdataofregion")
public class ArcgisDataOfRegionController extends ZZBaseController {
	
	@Autowired
	private IPlaceInfoService placeInfoService;	// 重点场所服务
	@Autowired
	private IDictionaryService dictionaryService;
	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	private ICorBaseInfoService corBaseInfoService;	
	@Autowired
	private ICampusService campusService;
	@Autowired
	private IBarService barService;
	@Autowired
	private IStoreInfoService storeInfoService;
	@Autowired
	private IAreaBuildingInfoService areaBuildingInfoService;
	@Autowired
	private IResidentService residentService;
	@Autowired
	private PartyIndividualService partyIndividualService;
	@Autowired
	private IAreaRoomRentService areaRoomRentService; //出租屋服务
	@Autowired
	private IGridAdminService gridAdminService;
	@Autowired
	private ICiRsRoomService ciRsRoomService;
	@Autowired
	private IEventDisposalService eventDisposalService;//事件服务
	@Autowired
	private IEvent4WorkflowService event4WorkflowService;
	@Autowired
	private ISegmentGridService segmentGridService;//片区网格
	@Autowired
	private ISafeCheckDetailService safeCheckDetailService;//隐患
	@Autowired
	private ICareRoadService careRoadService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService; // 新字典服务
	@Autowired
	private IServiceOutletsService serviceOutletsService; //便民服务网点

	@Autowired
	private IFunConfigurationService configurationService;
	
	@Autowired
	private IZzSmallPlacesService zzSmallPlacesService;
	
	@Autowired
	private INicService nicService;//各视联网信息中心
	
	@Autowired
	private IDangerChemistryManagerService dangerChemistryManagerService;//危险化学品企业管理
	
	@Autowired
	private DangRoadService dangRoadService;//两站两员 隐患路口、隐患路段
	
	@Autowired
	private MajorIndustryService majorIndustryService; //注入重大产业表模块服务
	
	//@Autowired
	//private MonitorService monitorService;
	
	@Autowired
	private PoliceManagementService policeManagementService;	// 大丰民警
	@Autowired
	private IBaseDictionaryService dictService;

	@Autowired
	private IBusInspectionRecordService busInspectionRecordServiceZZGL;


	@Autowired
    private ILegalInfoService legalInfoService;
	//民警管理数据字典
	private final static String PC_MANAGE = "D320";
	///---重点场所 start----------------------------------------------
	/**
	 * 重点场所数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfKeyPlace")
	public String keyPlace(HttpSession session, @RequestParam(value="gridId") Long gridId, ModelMap map,
			@RequestParam(value="standard", required=false) String standard,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<PlaceType> plaTypeDC = placeInfoService.getPlaTypeList();
		Map<String,Object> params = new HashMap();
		params.put("dictPcode",ConstantValue.NINE_SMALL_TYPE_DC);
		List<BaseDataDict> nineSmallTypeDC = dictService.getDataDictListOfSinglestage(params);
		map.addAttribute("nineSmallTypeDC", nineSmallTypeDC);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("plaTypeDC", plaTypeDC);
		map.addAttribute("gridId", gridId);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if (userInfo.getOrgCode().startsWith(ConstantValue.YANDU_FUNC_ORG_CODE)) {
			return "/map/arcgis/standardmappage/standardKeyPlace_yd.ftl";
		}
		return "/map/arcgis/standardmappage/standardKeyPlace.ftl";
	}
	
	/**
	 * 重点场所信息汇总表数据
	 * @param session
	 * @param page 
	 * @param rows
	 * @param gridId
	 * @param roomAddress
	 * @param placeType
	 * @param isFocus
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listKeyPlaceData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination listData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId", required=false) Long gridId, @RequestParam(value="plaName", required=false) String plaName,@RequestParam(value="roomAddress", required=false) String roomAddress,
			@RequestParam(value="plaType", required=false) String plaType,@RequestParam(value="isFocus", required=false) String isFocus,@RequestParam(value="buildingId", required=false) Long buildingId,
			@RequestParam(value="roomId", required=false) Long roomId, @RequestParam(value="nineSmallType", required=false) String nineSmallType) {
		if(page<=0) page=1;
		if(StringUtils.isNotBlank(plaName)) {
			plaName = plaName.trim();
		} else {
			plaName = null;
		}
		
		if(StringUtils.isNotBlank(roomAddress)) {
			roomAddress = roomAddress.trim();
		} else {
			roomAddress = null;
		}
		
		if(StringUtils.isNotBlank(plaType)) {
			plaType = plaType.trim();
		} else {
			plaType = null;
		}
		
		if(StringUtils.isNotBlank(isFocus)) {
			isFocus = isFocus.trim();
		} else {
			isFocus = null;
		}

		if(StringUtils.isNotBlank(nineSmallType)) {
			nineSmallType = nineSmallType.trim();
		} else {
			nineSmallType = null;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", gridId);
		params.put("plaName", plaName);
		params.put("roomAddress", roomAddress);
		params.put("plaType", plaType);
		params.put("isFocus", isFocus);
		params.put("buildingId", buildingId);
		params.put("roomId", roomId);
		params.put("nineSmallType", nineSmallType);
		cn.ffcs.common.EUDGPagination pagination = placeInfoService.findPlaceInfoPagination(page, rows, params);
	     @SuppressWarnings("unchecked")
		List<PlaceInfo>	list=(List<PlaceInfo>) pagination.getRows();
	     for(int i=0;i<list.size();i++){
	    	 PlaceInfo record=list.get(i);
	    	// record.setPlaTypeLabel(placeInfoService.findPlaceTypeById(record.getPlaType()).getTypeName());	
	    	 PlaceType placeType = placeInfoService.findPlaceTypeById(record.getPlaType());
	    	 if (placeType!=null){
	    		 record.setPlaTypeLabel(placeType.getTypeName());
	    	 }else{
	    		 record.setPlaTypeLabel("");
	    	 }
	     }
		return pagination;
	}
	
	/**
	 * 获取重点场所的定位信息
	 * @param session
	 * @param ids 重点场所ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfKeyPlace")
	public List<ArcgisInfoOfPublic> getArcgisKeyPlaceLocateDataList(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		List<ArcgisInfoOfPublic> list = null;
		if (StringUtils.isNotBlank(ids)) {
			if(infoOrgCode.startsWith(ConstantValue.HAICANG_FUNC_ORG_CODE)){
				list = arcgisDataOfLocalService.getKeyPlaceLocateDataListByIds(ids, mapt, "PLACE");
			}else if(infoOrgCode.startsWith(ConstantValue.YANDU_FUNC_ORG_CODE)){
				list = this.arcgisDataOfLocalService.getArcgisKeyPlaceLocateDataListByIdsYD(ids,mapt);
			}else{
				list = this.arcgisDataOfLocalService.getArcgisKeyPlaceLocateDataListByIds(ids,mapt);
			}
			for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
		}
		return list;
	}
	
	/**
	 * 查看重点场所信息
	 * @param session
	 * @param plaId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/keyPlaceDetail")
	public String showKeyPlaceDetail(HttpSession session, @RequestParam(value="plaId") Long plaId, ModelMap map) {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		PlaceInfo placeInfo = placeInfoService.findPlaceInfoById(plaId);
		map.addAttribute("placeInfo", placeInfo);
		PlaceFireFightMeasure placeFireFightMeasure = placeInfoService.findPlaceFireFightMeasureById(plaId);
		map.addAttribute("placeFireFightMeasure", placeFireFightMeasure);
		//获取法人单位信息(因为存在关系表,为了以后扩展,这里暂时当做一对一处理,取第一条满足的数据)
		CorBaseInfo corBaseInfo = new CorBaseInfo();
		List<CorBaseInfo> corBaseInfoList =corBaseInfoService.getCorBaseInfoListByPlaId(placeInfo.getPlaId());
		if (corBaseInfoList != null && corBaseInfoList.size()>0){
			corBaseInfo = corBaseInfoList.get(0);
		}		
		map.addAttribute("corBaseInfo", corBaseInfo);
	
		Campus campus=campusService.findCampusByPlaId(plaId);
		map.addAttribute("campus", campus);
		Bar bar=barService.findBarByPlaId(plaId);
		map.addAttribute("bar", bar);
		StoreInfo storeInfo=storeInfoService.getStoreInfoByPlaId(Integer.valueOf(plaId.toString()));
		map.addAttribute("storeInfo", storeInfo);
		
		
		//-- 场所类型
		List<PlaceType> plaTypeDC = placeInfoService.getPlaTypeList();
		String placeType = "";
		if (placeInfo!=null){
	    	 placeType = placeInfo.getPlaType().toString();
				for (PlaceType dataDict : plaTypeDC) {
					if(placeType!=null&&placeType.equals(dataDict.getKeyId()+"")){						
						placeInfo.setPlaTypeLabel(dataDict.getTypeName());
						break;
					}
				}
	    }	
		map.addAttribute("plaTypeDC", plaTypeDC);
		
		//店招状态
		List<Map<String, Object>> storeStatusDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_PLA_STORE, ConstantValue.COLUMN_STORE_STATUS, userInfo.getOrgCode());
		map.addAttribute("storeStatusDC", storeStatusDC);	
		//店招审批状态
		List<Map<String, Object>> storeApprovaStatusDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_PLA_STORE, ConstantValue.COLUMN_GARBAGE_IS_COST, userInfo.getOrgCode());
		map.addAttribute("storeApprovaStatusDC", storeApprovaStatusDC);	
		//垃圾处置费是否缴纳
		List<Map<String, Object>> garbageIsCostDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_PLA_STORE, ConstantValue.COLUMN_STORE_APPROVA_STATUS, userInfo.getOrgCode());
		map.addAttribute("garbageIsCostDC", garbageIsCostDC);
		List<Map<String, Object>> campusTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_CAMPUS, ConstantValue.COLUMN_CAMPUS_TYPE,"");
		map.addAttribute("campusTypeDC", campusTypeDC);	
		return "/map/arcgis/standardmappage/keyPlaceDetail.ftl";
	}
	
	///---重点场所 end------------------------------------------------
	
	///---隐患 start----------------------------------------------
	@RequestMapping(value="/hiddenTrouble")
	public String hiddenTrouble(HttpSession session, @RequestParam(value="gridId",required=false) String gridId, ModelMap map) {
		map.put("gridId", gridId);
		return "/map/arcgis/standardmappage/taijiang/trouble.ftl";
	}
	
	//--具体事件的事件列表
	@ResponseBody
	@RequestMapping(value="/hiddenTroubleListData")
	public cn.ffcs.common.EUDGPagination flowInsListData(HttpSession session, @RequestParam(value="pageNo") int pageNo,
            @RequestParam(value = "pageSize",required = false) int pageSize,
            @RequestParam(value="buildingName",required = false) String buildingName, 
            @RequestParam(value="placeName",required = false) String placeName, 
            ModelMap map) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("buildingName", buildingName);
		params.put("placeName", placeName);
		//UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		cn.ffcs.common.EUDGPagination pagination = safeCheckDetailService.findHiddenTroubleList(pageNo,pageSize,params);
		return pagination;
	}
	
	@RequestMapping(value="/toImportantEvent")
	public String hiddenTrouble(HttpSession session, ModelMap map,
			@RequestParam(value="gridId",required=false) String gridId,
			@RequestParam(value="statusName", required=false) String statusName, 
			@RequestParam(value="gridFlag", required=false) String gridFlag) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("statusName", statusName);
		map.addAttribute("gridFlag", gridFlag);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String isNewEvent =  configurationService.turnCodeToValue(ConstantValue.IS_USE_NEW_EVENT,"",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isNewEvent", isNewEvent);
		if(null != isNewEvent && isNewEvent.equals("true")){
			return "/map/arcgis/standardmappage/taijiang/importantEvent.ftl";
		}
		return "/map/arcgis/standardmappage/taijiang/old/importantEvent.ftl";
	}
	
	/**
	 * 事件列表接口调整，后续使用如有问题再调整，2019-07-11 zhangls
	 * @param session
	 * @param gridId
	 * @param statusName
	 * @param type
	 * @param gridFlag
	 * @return
	 */
	///---重大事件 start------------------------------------------------
	@ResponseBody
	@RequestMapping(value="/importantEvent", method=RequestMethod.POST)
	public  Map<String, Object> importantEvent(HttpSession session,
			@RequestParam(value="gridId",required=false) String gridId,
			@RequestParam(value="statusName", required=false) String statusName, 
			@RequestParam(value="type", required=false) String type, 
			@RequestParam(value="gridFlag", required=false) String gridFlag) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		cn.ffcs.system.publicUtil.EUDGPagination pagination = new cn.ffcs.system.publicUtil.EUDGPagination();
		cn.ffcs.system.publicUtil.EUDGPagination yinjipagination = new cn.ffcs.system.publicUtil.EUDGPagination();
		String gridCode="";
		Map<String,Object> result = new HashMap<String,Object>();
		
		params.put("userId", userInfo.getUserId());
		params.put("orgId", userInfo.getOrgId());
		params.put("userOrgCode", userInfo.getOrgCode());
		params.put("contingencyPlan","important");
		params.put("listType", 1);
		params.put("dynamicSql4Event", " AND (T1.URGENCY_DEGREE IN ('02','03') OR (T1.URGENCY_DEGREE != '04' AND T1.INFLUENCE_DEGREE = '04')) ");
		
		try {
			pagination = event4WorkflowService.findEventListPagination(1, 8, params);
		} catch (ZhsqEventException e1) {
			e1.printStackTrace();
		}
		
		type = null;
		if("jjya".equals(type)){
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			gridId = String.valueOf((Long)defaultGridInfo.get(KEY_START_GRID_ID));
//			params.put("gridId", gridId);
			params.put("contingencyPlan","urgency");
			params.put("dynamicSql4Event", " AND (T1.URGENCY_DEGREE = '04' AND T1.TYPE_ IN ('0216','0217','0218')) ");
			
			try {
				yinjipagination = event4WorkflowService.findEventListPagination(1, 8, params);
			} catch (ZhsqEventException e) {
				e.printStackTrace();
			}
			
			gridCode = mixedGridInfoService.findMixedGridInfoById(Long.valueOf(gridId),false).getGridCode();
			if(gridCode.length()<=12){
				result.put("show", "yes");
			}else{
				result.put("show", "no");
			}
		}
		result.put("result", "yes");
		result.put("eventList", pagination.getRows());
		result.put("yinjiEventList", yinjipagination.getRows());
		return result;
	}
	
	/**
	 * 新事件重大紧急接口
	 * @param session
	 * @param gridId
	 * @param statusName
	 * @param type
	 * @param gridFlag
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/old/importantEvent", method=RequestMethod.POST)
	public  Map<String, Object> importantEventOld(HttpSession session,
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
		cn.ffcs.common.EUDGPagination pagination = eventDisposalService.findEventDisposalPaginationForTaiJIang(1, 8, params);
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
			yinjipagination = eventDisposalService.findEventDisposalPaginationForTaiJIang(1, 8, params);
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
	
	///---出租屋 start----------------------------------------------
	/**
	 * 出租屋数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfRentRoom")
	public String rentRoom(HttpSession session, ModelMap map
			, @RequestParam(value="gridId") Long gridId
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		UserInfo user=(UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("liveTypeDC", baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.AREA_ROOM_RENT_LIVE_TYPE, user.getOrgCode()));
		
		return "/map/arcgis/standardmappage/standardRentRoom.ftl";
	}
	
	// 旧地图出租屋界面
	@RequestMapping(value="/toArcgisDataListOfRentRoomOld")
	public String rentRoomOld(HttpSession session, ModelMap map
			, @RequestParam(value="gridId") Long gridId
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardRentRoom_old.ftl";
	}
	
	/**
	 * 出租屋管理数据
	 * @param session
	 * @param page 页码
	 * @param rows 每页条数
	 * @param gridId 起始网格ID
	 * @param isExpire 是否过期，“true”为过期，“false”为正常
	 * @param roomAddress 房屋地址
	 * @param liveType 住户类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/rentRoomListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination rentRoomListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId") Long gridId, @RequestParam(value="isExpire") String isExpire, @RequestParam(value="roomAddress", required=false) String roomAddress,
			@RequestParam(value="liveType", required=false) String liveType) {
		if(page<=0) page=1;
		if(roomAddress!=null) roomAddress = roomAddress.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startGridId", gridId);
		params.put("roomAddress", roomAddress);
		params.put("liveType", liveType);
		if(StringUtils.isNotBlank(isExpire)){
			params.put("isExpire", isExpire);
		}
		cn.ffcs.common.EUDGPagination pagination = areaRoomRentService.findAreaRoomRentPagination(page, rows, params);
		return pagination;
	}
	/**
	 * 出租屋管理数据
	 * @param session
	 * @param page 页码
	 * @param rows 每页条数
	 * @param gridId 起始网格ID
	 * @param isExpire 是否过期，“true”为过期，“false”为正常
	 * @param roomAddress 房屋地址
	 * @param liveType 住户类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/roomRentCount", method=RequestMethod.POST)
	public Map<String,Object> roomRentCount(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId") Long gridId, @RequestParam(value="isExpire") String isExpire, @RequestParam(value="roomAddress", required=false) String roomAddress,
			@RequestParam(value="liveType", required=false) String liveType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startGridId", gridId);
		params.put("roomAddress", roomAddress);
		params.put("liveType", liveType);
		if(isExpire!=null && "true".equals(isExpire)){
			params.put("isExpire", isExpire);
		}
		Map<String,Object> map = areaRoomRentService.findMapRoomRentCount(params);
		return map;
	}
	@ResponseBody
	@RequestMapping(value = "/rentRoomListDataOld", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination rentRoomListDataOld(HttpSession session, @RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, @RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "isExpire") String isExpire,
			@RequestParam(value = "roomAddress", required = false) String roomAddress,
			@RequestParam(value = "liveType", required = false) String liveType,
			@RequestParam(value = "searchContent", required = false) String searchContent,
			@RequestParam(value = "searchStars", required = false) String searchStars) {
		if(page<=0) page=1;
		if(roomAddress!=null) roomAddress = roomAddress.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startGridId", gridId);
		params.put("roomAddress", roomAddress);
		params.put("liveType", liveType);
		params.put("isExpire", isExpire);
		params.put("searchContent", searchContent);
		params.put("searchStars", searchStars);
		params.put("old", "old"); // 旧界面
		cn.ffcs.common.EUDGPagination pagination = areaRoomRentService.findAreaRoomRentPagination(page, rows, params);
		return pagination;
	}
	
	/**
	 * 2014-07-09 chenlf add
	 * 获取出租屋的定位信息
	 * @param session
	 * @param ids 出租屋ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfRentRoom")
	public List<ArcgisInfoOfPublic> getArcgisRentRoomLocateDataList(HttpSession session,
			ModelMap map, HttpServletResponse res,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt") Integer mapt
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
			) {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//System.out.println("::::::::::::::::::::::::::::::::"+mapt);
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		//海沧的出租屋定位方式是手动定位，而不是与楼宇关联
		if(userInfo.getOrgCode().startsWith(ConstantValue.HAICANG_FUNC_ORG_CODE)){
			list = this.arcgisDataOfLocalService.getArcgisRentRoomLocateDataListByIdsHC(ids,mapt);
		}else {
			list = this.arcgisDataOfLocalService.getArcgisRentRoomLocateDataListByIds(ids,mapt);
		}
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	
	/**
	 * 出租屋概要信息
	 * @param session
	 * @param rentId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/rentRoomDetail")
	public String rentRoomDetail(HttpSession session, @RequestParam(value="rentId") Long rentId, 
			@RequestParam(value = "isCross", required=false) String isCross, ModelMap map) {
		AreaRoomRent areaRoomRent = areaRoomRentService.findAreaRoomRentById(rentId);
		map.addAttribute("areaRoomRent", areaRoomRent);
		//-- 网格管理员
		GridAdmin gridAdmin = gridAdminService.getGridAdminByGridIdAndDuty(areaRoomRent.getGridId(), ConstantValue.GRID_ADMIN_DUTY_001);
		map.addAttribute("gridAdmin", gridAdmin);
		
		//房主与租户关系
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roomId",areaRoomRent.getRoomId());
		params.put("buildingId",areaRoomRent.getBuildingId());
		List<CiRsRoom>  ciRsRoomList= ciRsRoomService.findCiRsRoomListByBuildingIdAndRooID(params);
		map.addAttribute("ciRsRoomList", ciRsRoomList);
		map.addAttribute("isCross", isCross);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));		
		map.put("roomId", areaRoomRent.getRoomId());

		//巡查记录
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(StringUtils.startsWith(userInfo.getOrgCode(),ConstantValue.XIANGSHUI_INFO_ORG_CODE)){
			params = new HashMap<String, Object>();
			params.put("targetId", areaRoomRent.getRoomId());
			cn.ffcs.common.EUDGPagination pagination = busInspectionRecordServiceZZGL.findPatrolList(1,20,params);
			map.addAttribute("inspectionList", pagination.getRows());
			map.addAttribute("inspectionCount", pagination.getTotal());
		}

		return "/map/arcgis/standardmappage/rentRoomDetail.ftl";
	}
	
	/**
	 * 房屋住户表（记录居民与房屋间的关系）
	 * @param session
	 * @param buildingId
	 * @param roomId
	 * @param type
	 * @param manageState
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/roomCiRsListData", method=RequestMethod.POST)
	cn.ffcs.common.EUDGPagination roomCiRsListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows, @RequestParam(value="buildingId", required=false) Long buildingId, @RequestParam(value="roomId", required=false) Long roomId,
			@RequestParam(value="type", required=false) String type,@RequestParam(value="manageState", required=false) String manageState,@RequestParam(value="name", required=false) String name, @RequestParam(value="residenceAddr", required=false) String residenceAddr) {
		if(page<=0) page=1;
		if(type!=null) type = type.trim();
		if(name!=null) name = name.trim();
		if(residenceAddr!=null) residenceAddr = residenceAddr.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roomId", roomId);
		params.put("type", type);
		params.put("name", name);
		params.put("residenceAddr", residenceAddr);
		params.put("manageState", manageState);
		params.put("buildingId",buildingId);
		cn.ffcs.common.EUDGPagination pagination = ciRsRoomService.findCiRsRoomListByBuildingIdAndRooIdPagination(page, rows, params);
	//	List<CiRsRoom>  ciRsRoomList= ciRsRoomService.findCiRsRoomListByBuildingIdAndRooID(params);
		return pagination;
	}

	/**
	 * 出租屋汇聚
	 * @param session
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rentRoomConverge")
	public List<Map<String, Object>> rentRoomConverge(HttpSession session, ModelMap map, HttpServletRequest request) {
		String infoOrgCode = request.getParameter("infoOrgCode");
		List<Map<String,Object>> list = areaRoomRentService.countConvergeByOrgCode(infoOrgCode);
		return list;
	}
	///---出租屋 end----------------------------------------------
	
	///---便民服务网点start----------------------------------------------
	@RequestMapping(value="/toArcgisDataListOfServiceOutlets")
	public String toArcgisDataListOfServiceOutlets(HttpSession session, @RequestParam(value="gridId") Long gridId,
			@RequestParam(value="standard", required=false) String standard,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
			, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		List<BaseDataDict> typeName = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SERVICE_OUTLETS_TYPE, userInfo.getOrgCode());
		map.addAttribute("typeName", typeName);
		
		
		return "/map/arcgis/standardmappage/standardServiceOutlets.ftl";
	}
	
	/**
	 * 加载列表数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param outlets
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/serviceOutletsListData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination listData(
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@ModelAttribute(value = "record") ServiceOutlets outlets) {
		Long gridId = outlets.getGridId();
		String name = outlets.getName();
		String type = outlets.getType();
		String placeAddr = outlets.getPlaceAddr();
		
		if (gridId == null || gridId == 0L) {
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			Object gridIdObj = defaultGridInfo.get(KEY_START_GRID_ID);
			if(gridIdObj != null){
				gridId = Long.valueOf(gridIdObj.toString());
			}
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(name)) params.put("name", name);
		if (StringUtils.isNotBlank(type)) params.put("type", type);
		if (StringUtils.isNotBlank(placeAddr)) params.put("placeAddr", placeAddr);
		params.put("gridId", gridId);
		
		cn.ffcs.common.EUDGPagination pagination = serviceOutletsService.findOSOPagination(page, rows, params);
		
		return pagination;
	}
	
	/**
	 * @author huangmw
	 * @time 2015年3月21日 add
	 * @Description: 获取便民服务网点的定位信息
	 * @param session
	 * @param request
	 * @param ids
	 * @param order
	 * @param name
	 * @param mapt
	 * @param showType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfServiceOutlets")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfServiceOutlets(HttpSession session,
			ModelMap map, HttpServletResponse res,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisServiceOutletsLocateDataListByIds(ids,mapt);
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	
	/**
	 * @author huangmw
	 * @time 2015年3月23日 add
	 * @Description: 便民服务网点概要信息
	 * @param session
	 * @param soId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getServiceOutletsInfoDetailOnMap")
	public String getServiceOutletsInfoDetailOnMap(HttpSession session, @RequestParam(value="soId") Long soId, ModelMap map) {
		// 便民服务网点信息
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ServiceOutlets outlets = this.serviceOutletsService.findServiceOutletsById(soId, userInfo.getOrgCode());
		
		if (outlets == null) {
			outlets = new ServiceOutlets();
		}
		map.addAttribute("outlets", outlets);
		return "/map/arcgis/standardmappage/serviceOutletsInfoDetailOnMap.ftl";
	}
	///---便民服务网点end----------------------------------------------
	
	///---护路护线start----------------------------------------------
	@RequestMapping(value="/toArcgisDataListOfICareRoad")
	public String toArcgisDataListOfICareRoad(HttpSession session, @RequestParam(value="gridId") Long gridId,
			@RequestParam(value="standard", required=false) String standard,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr, ModelMap map) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardICareRoad.ftl";
	}
	/**
	 * 护路护线信息汇总表数据
	 * @param session
	 * @param page 
	 * @param rows
	 * @param gridId
	 * @param lotName
	 * @param secRiskLevel	
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/iCareRoadListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination iCareRoadListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId",required=false) Long gridId, @RequestParam(value="lotName", required=false) String lotName,
			@RequestParam(value="secRiskLevel", required=false) String secRiskLevel, @RequestParam(value="secRouteTypeLabel", required=false) String secRouteTypeLabel) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(page<=0) page=1;
		Map<String, Object> params = new HashMap<String, Object>();
		if(lotName!=null) lotName = lotName.trim();
		params.put("lotName", lotName);
		params.put("gridId", gridId);
		params.put("secRiskLevel", secRiskLevel);
		params.put("secRouteTypeLabel", secRouteTypeLabel);
		cn.ffcs.common.EUDGPagination pagination = careRoadService.findCareRoadPagination(page, rows, params, userInfo.getOrgCode());
		return pagination;
	}
	///---护路护线end----------------------------------------------
	//---食药企业-------------------------------------------------------
	@RequestMapping(value="/toArcgisDataListOfCorEnt")
	public String toArcgisDataListOfCorEnt(HttpSession session, @RequestParam(value="gridId") Long gridId,
			@RequestParam(value="standard", required=false) String standard,
			@RequestParam(value="economicType", required=false) String economicType,
			@RequestParam(value="corType", required=false) String corType
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
			, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridId", gridId);
		List<Map<String, Object>> corTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_TYPE, userInfo.getOrgCode());
		map.addAttribute("corTypeDC", corTypeDC);
		List<Map<String, Object>> categoryDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_CATEGORY, userInfo.getOrgCode());
		map.addAttribute("categoryDC", categoryDC);
		if(economicType==null) 
			economicType="";
		
		if (StringUtils.isNotBlank(corType)) {
			map.addAttribute("corType", "09");
		}
		
		map.addAttribute("economicType", economicType);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardCorEnt.ftl";
	}
	//------------------------------------------------------------------
	//---环保企业-------------------------------------------------------
	@RequestMapping(value="/toArcgisDataListOfCorEnv")
	public String toArcgisDataListOfCorEnv(HttpSession session, @RequestParam(value="gridId") Long gridId,
			@RequestParam(value="standard", required=false) String standard,
			@RequestParam(value="economicType", required=false) String economicType,
			@RequestParam(value="corType", required=false) String corType
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
			, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridId", gridId);
		List<Map<String, Object>> corTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_TYPE, userInfo.getOrgCode());
		map.addAttribute("corTypeDC", corTypeDC);
		List<Map<String, Object>> categoryDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_CATEGORY, userInfo.getOrgCode());
		map.addAttribute("categoryDC", categoryDC);
		if(economicType==null) 
			economicType="";
		
		if (StringUtils.isNotBlank(corType)) {
			map.addAttribute("corType", "09");
		}
		
		map.addAttribute("economicType", economicType);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardCorEnv.ftl";
	}
	@ResponseBody
	@RequestMapping(value = "/corEnvListData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination corEnvListData(HttpSession session, @RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, @RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "corName", required = false) String corName,
			@RequestParam(value = "corType", required = false) String corType,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "economicType", required = false) String economicType) {
		if(page<=0) page=1;
		if(corName!=null) {
			corName = corName.trim();
		}
		if(corType!=null && !"".equals(corType)){
			corType = corType.trim();
		}else {
			corType = null;
		}
		if(category!=null & !"".equals(category)){
			category = category.trim();
		}else {
			category = null;
		}
		if(status!=null & !"".equals(status)){
			status = status.trim();
		}else {
			status = null;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", gridId);
		params.put("corName", corName);
		params.put("corType", corType);
		params.put("category", category);
		params.put("status", status);
		params.put("economicType", economicType);
		cn.ffcs.common.EUDGPagination pagination = corBaseInfoService.findPageListCorpEnv(page, rows, params);
		return pagination;
	}
	@ResponseBody
	@RequestMapping(value = "/getArcgisCorEnvDataList")
	public List<ArcgisInfoOfPublic> getArcgisCorEnvDataList(HttpSession session, ModelMap map, HttpServletResponse res,
			@RequestParam(value="ids") String ids, @RequestParam(value="mapt", required=false) Integer mapt,
			@RequestParam(value = "showType") String showType
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		//Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		//String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		List<ArcgisInfoOfPublic> list = null;
		list = arcgisDataOfLocalService.getArcgisCorEnvLocateDataListByIds(ids, mapt, "CORENV");
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	/**
	 * 查看法人单位基础信息
	 * @param session
	 * @param plaId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getCorEnvDetailOnMap")
	public String getCorEnvDetailOnMap(HttpSession session, HttpServletRequest request, 
			@RequestParam(value="envId") Long envId,
			ModelMap map){
		//UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		CorEnv corEnv = corBaseInfoService.findByEnvId(envId);
		map.addAttribute("record", corEnv);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		return "/map/arcgis/standardmappage/corEnvDetail.ftl";
	}
	//------------------------------------------------------------------
	///---企业 start----------------------------------------------
	/**
	 * 企业数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfCorBase")
	public String corBaseInfo(HttpSession session, @RequestParam(value="gridId") Long gridId,
			@RequestParam(value="standard", required=false) String standard,
			@RequestParam(value="economicType", required=false) String economicType,
			@RequestParam(value="corType", required=false) String corType
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
			, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridId", gridId);


		List<Map<String, Object>> corTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_TYPE, userInfo.getOrgCode());



            map.addAttribute("corTypeDC", corTypeDC);

		List<Map<String, Object>> categoryDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_CATEGORY, userInfo.getOrgCode());
		map.addAttribute("categoryDC", categoryDC);
		if(economicType==null) 
			economicType="";
		
		if (StringUtils.isNotBlank(corType)) {
			map.addAttribute("corType", "09");
		}
		
		map.addAttribute("economicType", economicType);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardCorBase.ftl";
	}
	
	/**
	 * 企业管理数据
	 * @param session
	 * @param page 页码
	 * @param rows 每页条数
	 * @param gridId 起始网格ID
	 * @param corName 法人名称
	 * @param corType 法人类型
	 * @param category 行业分类
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/corBaseListData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination corBaseListData(HttpSession session, @RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, @RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "corName", required = false) String corName,
			@RequestParam(value = "corType", required = false) String corType,
			@RequestParam(value = "cata", required = false) String cata,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "economicType", required = false) String economicType) {
		if(page<=0) page=1;
		if(corName!=null) {
			corName = corName.trim();
		}
		if(corType!=null && !"".equals(corType)){
			corType = corType.trim();
		}else {
			corType = null;
		}
		if(category!=null & !"".equals(category)){
			category = category.trim();
		}else {
			category = null;
		}
		if(status!=null & !"".equals(status)){
			status = status.trim();
		}else {
			status = null;
		}
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("gridId", gridId);
		params.put("corName", corName);
		params.put("cata", cata);
		params.put("corType", corType);
		params.put("category", category);
            params.put("status", status);
		params.put("economicType", economicType);

        legalInfoService.searchList(page, rows, params);

        cn.ffcs.common.EUDGPagination pagination = corBaseInfoService.findCorBaseInfoPagination(page, rows, params);
		//EUDGPagination pagination = corBaseInfoService.findCorBaseInfoByGridIdBuildingIdPagination(page, rows, params);
		return pagination;
	}
	
	/**
	 * 法人、企业GIS信息
	 * @param session
	 * @param map
	 * @param res
	 * @param ids
	 * @param mapt
	 * @param showType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisCorLocateDataList")
	public List<ArcgisInfoOfPublic> getArcgisCorLocateDataList(HttpSession session, ModelMap map, HttpServletResponse res,
			@RequestParam(value="ids") String ids, @RequestParam(value="mapt", required=false) Integer mapt,
			@RequestParam(value = "showType") String showType
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		List<ArcgisInfoOfPublic> list = null;
		if(infoOrgCode.startsWith(ConstantValue.HAICANG_FUNC_ORG_CODE)
				|| infoOrgCode.startsWith(ConstantValue.JIANGYIN_FUNC_ORG_CODE)
				|| infoOrgCode.startsWith(ConstantValue.YANPING_FUNC_ORG_CODE)){
			list = arcgisDataOfLocalService.getArcgisCorLocateDataListByIds(ids, mapt, "CORBASE");
		}else{
			list = this.arcgisDataOfLocalService.getArcgisCorLocateDataListByIds(ids, mapt);
		}
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	
	/**
	 * 查看法人单位基础信息
	 * @param session
	 * @param plaId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getCorInfoDetailOnMap")
	public String getCorInfoDetailOnMap(HttpSession session, HttpServletRequest request, 
			@RequestParam(value="cbiId", required = false) Long cbiId,
			@RequestParam(value="envId", required = false) Long envId,
			@RequestParam(value="isAnjian",required=false) String isAnjian, 
			ModelMap map){
		//获取环保企业扩展信息
		CorEnv corEnv = new CorEnv();
		if(cbiId == null && envId!=null){
			corEnv = corBaseInfoService.findByEnvId(envId);
			cbiId = corEnv.getCorpId();
		}
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		CorBaseInfo  corBaseInfo=new CorBaseInfo();
		corBaseInfo.setCbiId(cbiId);
		corBaseInfo=corBaseInfoService.getCorpBaseInfo(corBaseInfo);

		//法人类型
		if(corBaseInfo != null && StringUtils.isNotBlank(corBaseInfo.getCorType())){
			String  corTypeName=dictionaryService.getTableColumnText(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_TYPE,corBaseInfo.getCorType());
			corBaseInfo.setCorTypeName(corTypeName);
		}
		if(corBaseInfo != null && StringUtils.isNotBlank(corBaseInfo.getCategory())){
			//行业分类
			String  categoryName=dictionaryService.getTableColumnText(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_CATEGORY,corBaseInfo.getCategory());
			corBaseInfo.setCategoryName(categoryName);
		}
		if(corBaseInfo != null && StringUtils.isNotBlank(corBaseInfo.getEconomicType())){
			//经济类型
			String  economicTypeName=dictionaryService.getTableColumnText(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_ECONOMIC_TYPE,corBaseInfo.getEconomicType());
			corBaseInfo.setEconomicTypeName(economicTypeName);
		}
		if(corBaseInfo != null && StringUtils.isNotBlank(corBaseInfo.getRegisteredCurrency())) {
			//注册或开办资金币种
			String registeredCurrencyName = dictionaryService.getTableColumnText(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_REGISTERED_CURRENCY, corBaseInfo.getRegisteredCurrency());
			corBaseInfo.setRegisteredCurrencyName(registeredCurrencyName);
		}
		
		
		//法人类型
		List<Map<String, Object>> corTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_TYPE, userInfo.getOrgCode());
		//注册或开办资金币种
		List<Map<String, Object>> registeredCurrencyDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_REGISTERED_CURRENCY, userInfo.getOrgCode());
		//行业分类
		List<Map<String, Object>> categoryDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_CATEGORY, userInfo.getOrgCode());
		//经济类型
		List<Map<String, Object>> economicTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_ECONOMIC_TYPE, userInfo.getOrgCode());
		
		map.addAttribute("corTypeDC", corTypeDC);
		map.addAttribute("registeredCurrencyDC", registeredCurrencyDC);
		map.addAttribute("categoryDC", categoryDC);
		map.addAttribute("economicTypeDC", economicTypeDC);
		
		//回填管征机关类型
		String adminDivision = "";
		if(corBaseInfo != null && corBaseInfo.getAreaCorpTaxInfo() != null){
			CorpTaxInfo areaCorpTaxInfo = corBaseInfo.getAreaCorpTaxInfo();

			if(areaCorpTaxInfo!=null){
				adminDivision = areaCorpTaxInfo.getAdminDivisionCode();
				request.setAttribute("areaAdminDivisionCodeBak", adminDivision);
				if(adminDivision!=null&&!"".equals(adminDivision)){
					String areaAdminDivisionType = adminDivision.substring(adminDivision.length()-2, adminDivision.length());
					map.addAttribute("areaAdminDivisionType", areaAdminDivisionType);
				}
			}
		}
		if(corBaseInfo != null && corBaseInfo.getStateCorpTaxInfo() != null) {
			CorpTaxInfo stateCorpTaxInfo = corBaseInfo.getStateCorpTaxInfo();
			if (stateCorpTaxInfo != null) {
				adminDivision = stateCorpTaxInfo.getAdminDivisionCode();
				request.setAttribute("stateAdminDivisionCodeBak", adminDivision);
				if (adminDivision != null && !"".equals(adminDivision)) {
					String stateAdminDivisionType = adminDivision.substring(adminDivision.length() - 2, adminDivision.length());
					map.addAttribute("stateAdminDivisionType", stateAdminDivisionType);
				}
			}
		}
		
		map.addAttribute("record", corBaseInfo);
		String type = request.getParameter("type");
		map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		if(StringUtils.isNotBlank(type) && type.equals("ent")){
			return "/map/arcgis/standardmappage/corEntDetail.ftl";
		}else if(StringUtils.isNotBlank(type) && type.equals("env")){
//			corEnv = corBaseInfoService.findByEnvId(envId);
			return "/map/arcgis/standardmappage/corEnvDetail.ftl";
		}
//		if(StringUtils.isNotBlank(isAnjian) && "true".equals(isAnjian)){
//			return "/map/arcgis/standardmappage/anjianCorBaseDetail.ftl";
//		}else{
			return "/map/arcgis/standardmappage/corBaseDetail.ftl";
//		}
		
	}
	
	/**
	 * 更新法人单位基础信息
	 * @param session
	 * @param risk
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(HttpSession session,  @ModelAttribute(value="corBaseInfo") CorBaseInfo corBaseInfo, HttpServletRequest request,ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//开始
		String[] buildingIds = request.getParameterValues("buildingIds");
		String[] floorIds = request.getParameterValues("roomIds");
		String[] buildingName = request.getParameterValues("buildingName");//用于出错时回填回页面
		String[] floorName = request.getParameterValues("roomName");
		String[] floorNum = request.getParameterValues("floorNum");
		String[] gridName = request.getParameterValues("gridName");//用于出错时回填回页面
		String[] gridCode = request.getParameterValues("gridIds");//用于出错时回填回页面
		//CorBaseInfo obj2 = new CorBaseInfo();
		List<PlaceFloorRela> placeFloorRelaList=null;
		String modifyScope = request.getParameter("modifyScope");
		String eventId = request.getParameter("eventId");
		int eId = 0;
		if (!StringUtils.isBlank(eventId)) {
			eId = Integer.parseInt(eventId);
		}	
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if(!StringUtils.isBlank(corBaseInfo.getEstablishDateStr()))
				corBaseInfo.setEstablishDate(sdf.parse(corBaseInfo.getEstablishDateStr()));
			if(!StringUtils.isBlank(corBaseInfo.getRegistrationTimeStr()))
				corBaseInfo.setRegistrationTime(sdf.parse(corBaseInfo.getRegistrationTimeStr()));
			if(!StringUtils.isBlank(corBaseInfo.getYearCheckDateStr()))
				corBaseInfo.setYearCheckDate(sdf.parse(corBaseInfo.getYearCheckDateStr()));
			if(!StringUtils.isBlank(corBaseInfo.getStartDoBusiDateStr()))
				corBaseInfo.setStartDoBusiDate(sdf.parse(corBaseInfo.getStartDoBusiDateStr()));
			if(!StringUtils.isBlank(corBaseInfo.getEndDoBusiDateStr()))
				corBaseInfo.setEndDoBusiDate(sdf.parse(corBaseInfo.getEndDoBusiDateStr()));
			if(!StringUtils.isBlank(corBaseInfo.getCorpExtInfo().getSettledDateStr()))
				corBaseInfo.getCorpExtInfo().setSettledDate(sdf.parse(corBaseInfo.getCorpExtInfo().getSettledDateStr()));
		} catch (Exception e) { e.printStackTrace(); }
		
		
		try {
			CorBaseInfo obj = new CorBaseInfo();
			obj.setCbiId(corBaseInfo.getCbiId());
			obj.setIsDel(CorBaseInfo.DEL_FLAG_NO);
			obj.setStatus(CorBaseInfo.STATUS_NORMAL);
			obj = corBaseInfoService.getCorpBaseInfo(obj);
			
			//Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
	        //Long defaultInfoGridId = Long.parseLong(defaultGridInfo.get(KEY_START_GRID_ID).toString());
			//obj.setGridId(defaultInfoGridId);
			obj.setOrgCode(corBaseInfo.getOrgCode());//组织机构代码
			obj.setCorName(corBaseInfo.getCorName());//法人名称
			obj.setCorType(corBaseInfo.getCorType());//法人类型
			obj.setCorAddr(corBaseInfo.getCorAddr());//法人地址
			obj.setRegistrationTime(corBaseInfo.getRegistrationTime());//注册日期
			obj.setCategory(corBaseInfo.getCategory());//行业分类
			obj.setEconomicType(corBaseInfo.getEconomicType());//经济类型
			obj.setRepresentativeName(corBaseInfo.getRepresentativeName());//法人代表
			obj.setBusScope(corBaseInfo.getBusScope());//经营或业务范围
			obj.setEstablishDate(corBaseInfo.getEstablishDate());//成立日期
			obj.setRegisteredCapital(corBaseInfo.getRegisteredCapital());//注册或开办资金金额
			obj.setRegisteredCurrency(corBaseInfo.getRegisteredCurrency());//注册或开办资金币种
			obj.setCategory(corBaseInfo.getCategory());//行业分类
			obj.setEconomicType(corBaseInfo.getEconomicType());//经济类型
			obj.setRegistrationOrgName(corBaseInfo.getRegistrationOrgName());//注册或登记机构名称
			obj.setRegistrationNum(corBaseInfo.getRegistrationNum());//注册或登记号
			obj.setAdministrativeDivision(corBaseInfo.getAdministrativeDivision());//行政区划
			obj.setZipCode(corBaseInfo.getZipCode());//邮政编码
			obj.setTelephone(corBaseInfo.getTelephone());//电话号码
			obj.setWebSite(corBaseInfo.getWebSite());//网址
			obj.setGridId(corBaseInfo.getGridId());
			obj.setGridName(corBaseInfo.getGridName());
			obj.setUpdateUser(userInfo.getUserId());
			obj.setStartDoBusiDate(corBaseInfo.getStartDoBusiDate());
			obj.setEndDoBusiDate(corBaseInfo.getEndDoBusiDate());
			obj.setYearCheckDate(corBaseInfo.getYearCheckDate());
			obj.setAddressCode(corBaseInfo.getAddressCode());
			obj.setCorRemark(corBaseInfo.getCorRemark());
			obj.setIntoTheTraySituation(corBaseInfo.getIntoTheTraySituation());
			
			obj.setIntoType(corBaseInfo.getIntoType());
			obj.setCorpExtInfo(corBaseInfo.getCorpExtInfo());
			obj.setStateCorpTaxInfo(corBaseInfo.getStateCorpTaxInfo());
			obj.setAreaCorpTaxInfo(corBaseInfo.getAreaCorpTaxInfo());
			
			
			
			
			placeFloorRelaList= new ArrayList<PlaceFloorRela>();
			PlaceFloorRela placeFloorRela = null;
			if (buildingIds != null && buildingIds.length > 0) {
				int i = 0;
				for (String buildingId : buildingIds) {
					if (!StringUtils.isBlank(buildingId)) {
						placeFloorRela = new PlaceFloorRela();
						placeFloorRela.setPlaceFloorType(PlaceFloorRela.TYPE_CORP);
						buildingId=buildingId.replaceAll(",", "");
						placeFloorRela.setBuildingId(StringUtils.isBlank(buildingId) ? null : Long.parseLong(buildingId));
						floorIds[i]=floorIds[i].replaceAll(",", "");
						placeFloorRela.setRoomId(StringUtils.isBlank(floorIds[i]) ? null : Long.parseLong(floorIds[i]));
						placeFloorRela.setBuildingName(buildingName[i]);
						placeFloorRela.setRoomName(floorName[i]);
						placeFloorRela.setFloorNum(floorNum[i]);
						gridCode[i]=gridCode[i].replaceAll(",", "");
						placeFloorRela.setGridCode(gridCode[i]);
						placeFloorRela.setGridName(gridName[i]);
						placeFloorRelaList.add(placeFloorRela);
					}
					
					
					
					i++;
				}
			}
			corBaseInfoService.updateCorpBaseInfo(obj,null, placeFloorRelaList, modifyScope, eId);

			map.addAttribute("result", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.addAttribute("result", "修改失败");
		}
		
		return "/map/arcgis/standardmappage/corResult.ftl";
	}
	
	/**
	 * 根据企业ID查看人员组织信息
	 * @param session
	 * @param request
	 * @param cbiId
	 * @param map
	 * @return
	 */
	 
	@RequestMapping(value="/corBase_ciRsTopOrgInfo")
	public String corBase_ciRsTopOrgInfo(HttpSession session, HttpServletRequest request, @RequestParam(value="cbiId") Long cbiId,ModelMap map){		
		map.addAttribute("cbiId", cbiId);
		List<CorpDept> corpDeptList = corBaseInfoService.getCorpDeptListByCorBaseInfo(cbiId);	
		List<CorpCiRsTop> corpCiRsTopList = corBaseInfoService.getCorpCiRsTopListByCorBaseInfo(cbiId);
		map.addAttribute("corpDeptList", corpDeptList);
		map.addAttribute("corpCiRsTopList", corpCiRsTopList);
		return "/map/arcgis/standardmappage/corBase_ciRsTopOrgInfo.ftl";
	}
	
	/**
	 * 居民详细信息
	 * @param session
	 * @param ciRsId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/detail/{ciRsId}")
	public String detailInfo(HttpSession session, @PathVariable(value="ciRsId") Long ciRsId, ModelMap map) {
		Map<String, Object> residentDetail = residentService.getResidentDetail(ciRsId);
		List<Map<String, Object>> i_marriageDC = dictionaryService.getTableColumnDC("t_dc_ci_rs_top", "i_marriage", null);
		List<Map<String, Object>> i_typeDC = dictionaryService.getTableColumnDC("t_dc_ci_rs_top", "i_type", null);
		map.addAttribute("i_marriageDC", i_marriageDC);
		map.addAttribute("i_typeDC", i_typeDC);
		
		map.addAttribute("residentDetail", residentDetail);
		return "/map/arcgis/standardmappage/residentDetail.ftl";
	}
	
	/**
	 * 居民头像，先读blob字段，再读photoUrl，再默认图片
	 * @param session
	 * @param response
	 * @param ciRsId
	 * @return
	 */
	@RequestMapping(value="/photo/{ciRsId}")
	public ModelAndView createRisk(HttpSession session, HttpServletResponse response, @PathVariable(value="ciRsId") long ciRsId) {
		try {
			ServletOutputStream outputStream = response.getOutputStream();
			response.reset();// 清空输出流
			response.setContentType("image/png");// 定义输出类型
			//-- 取居民数据，优先读取头像路径
			boolean usePhotoUrl = true; // 标识是否使用头像照片路径
			String photoPath = session.getServletContext().getRealPath("/")+"images/untitled.png"; // 默认头像
			Map<String, Object> residentDetail = residentService.getResidentDetail(ciRsId);
			//-- 新接口,头像由partyIndividualService服务提供 modify by zhongshm 2013.10.16
			PartyIndividual partyIndividual = partyIndividualService.findById(Long.parseLong(residentDetail.get("PARTY_ID").toString()));
			if(partyIndividual!=null && partyIndividual.getPicUrl()!=null){
				try {
					String photoUrl = partyIndividual.getPicUrl();
					if(photoUrl.indexOf("http")!=-1) {
						//  /mnt/mfs/sq_upload/rs/perfile/2012/10/18/rs-perfile-dbc99c44597d49e1b4563ae4a88600b4.jpg
						//  http://img.cnsq.org/rs/perfile/2012/10/18/rs-perfile-dbc99c44597d49e1b4563ae4a88600b4.jpg
						int end = photoUrl.indexOf("/rs/perfile");
						String needReplaceStr = photoUrl.substring(0, end+1);
						photoUrl = photoUrl.replaceAll(needReplaceStr, ConstantValue.getRootResourseSavePath());
					} else if(photoUrl.indexOf("/rs/perfile")!=-1) {
						photoUrl = ConstantValue.getRootResourseSavePath()+photoUrl;
					} else if(photoUrl.indexOf("/ictfile/upload")!=-1){
						photoUrl = photoUrl.replace("\\", "/");
						photoUrl = ConstantValue.getRootResourseSavePath()+photoUrl;
					} else{
						photoUrl = ConstantValue.RESOURSE_SAVE_ROOT_PATH+photoUrl;
					}
					File tmpFile = new File(photoUrl);
					if(tmpFile.exists()) photoPath = photoUrl;
				} catch (Exception e) {
					e.printStackTrace();
					usePhotoUrl = false;
				}
			} else usePhotoUrl = false;
			//-- 取TOP表的的头像
//			if(residentDetail!=null && residentDetail.get("I_PHOTO_URL")!=null) {
//				try {
//					String photoUrl = residentDetail.get("I_PHOTO_URL").toString();
//					if(photoUrl.indexOf("http")!=-1) {
//						//  /mnt/mfs/sq_upload/rs/perfile/2012/10/18/rs-perfile-dbc99c44597d49e1b4563ae4a88600b4.jpg
//						//  http://img.cnsq.org/rs/perfile/2012/10/18/rs-perfile-dbc99c44597d49e1b4563ae4a88600b4.jpg
//						int end = photoUrl.indexOf("/rs/perfile");
//						String needReplaceStr = photoUrl.substring(0, end+1);
//						photoUrl = photoUrl.replaceAll(needReplaceStr, ConstantValue.getRootResourseSavePath());
//					} else if(photoUrl.indexOf("/rs/perfile")!=-1) {
//						photoUrl = ConstantValue.getRootResourseSavePath()+photoUrl;
//					} else {
//						photoUrl = ConstantValue.RESOURSE_SAVE_ROOT_PATH+photoUrl;
//					}
//					File tmpFile = new File(photoUrl);
//					if(tmpFile.exists()) photoPath = photoUrl;
//				} catch (Exception e) {
//					e.printStackTrace();
//					usePhotoUrl = false;
//				}
//			} else usePhotoUrl = false;
			//-- 如果头像路径未读取到或者读取出错，则读取存在数据库中的头像数据
			//-- 如果未读取到头像数据，则使用默认头像
			if(!usePhotoUrl) {
				try {
					byte photoData[] = residentService.getResidentPhoto(ciRsId);
					if(photoData!=null) outputStream.write(photoData);
					else usePhotoUrl = true;
				} catch (Exception e) {
					e.printStackTrace();
					usePhotoUrl = true;
				}
			}
			if(usePhotoUrl) {
				File photoFile = new File(photoPath);
				byte[] tempbytes = new byte[1024];
				int byteread = 0;
				FileInputStream in = new FileInputStream(photoFile);
				while ((byteread = in.read(tempbytes)) != -1) {
					outputStream.write(tempbytes, 0, byteread);
				}
			}
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("居民头像读取异常："+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 根据企业ID查看综治信息
	 * @param session
	 * @param request
	 * @param cbiId
	 * @param map
	 * @return
	 */
	
	@RequestMapping(value="/corBase_zzGroupInfo")
	public String corBase_zzGroupInfo(HttpSession session, HttpServletRequest request, @RequestParam(value="cbiId") Long cbiId,ModelMap map){		
		map.addAttribute("cbiId", cbiId);
		return "/map/arcgis/standardmappage/corBase_zzGroupInfo.ftl";
	}
	
	/**
	 * 通过企业ID查看综治信息数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param cbiId
	 * @return
	 */
	 
	@ResponseBody
	@RequestMapping(value="/corBaseZzGroupInfolistData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination corBaseZzGroupInfolistData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="cbiId") Long cbiId) {
		if(page<=0) page=1;		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cbiId", cbiId);		
		cn.ffcs.common.EUDGPagination pagination = corBaseInfoService.findCorBaseZzGroupInfoPagination(page, rows, params);		
		return pagination;
	}
	
	/**
	 * 根据企业ID查看楼栋信息
	 * @param session
	 * @param request
	 * @param cbiId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/corBase_areaBuilding")
	public String corBase_areaBuilding(HttpSession session, HttpServletRequest request, @RequestParam(value="cbiId") Long cbiId,ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<Map<String, Object>> useNatureDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_AREA_BUILDING_INFO, ConstantValue.COLUMN_AREA_BUILDING_INFO_USE_NATURE, userInfo.getOrgCode());
	    map.addAttribute("useNatureDC", useNatureDC);
		map.addAttribute("cbiId", cbiId);
		return "/map/arcgis/standardmappage/corBase_areaBuilding.ftl";
	}
	
	/**
	 * 通过企业查找楼房信息汇总表数据
	 * @param session
	 * @param page 
	 * @param rows
	 * @param useNature
	 * @param cbiId
	 * @param buildingName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/corBaseAreaBuildinglistData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination corBaseAreaBuildinglistData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows, @RequestParam(value="buildingName", required=false) String buildingName,
			@RequestParam(value="useNature", required=false) String useNature,@RequestParam(value="cbiId") Long cbiId) {
		if(page<=0) page=1;
		if(buildingName!=null) buildingName = buildingName.trim();
		if(useNature!=null) useNature = useNature.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cbiId", cbiId);
		params.put("buildingName", buildingName);
		params.put("useNature", useNature);
		cn.ffcs.common.EUDGPagination pagination = areaBuildingInfoService.findCorBaseAreaBuildingInfoPagination(page, rows, params);
	     @SuppressWarnings("unchecked")
		List<AreaBuildingInfo>	list=(List<AreaBuildingInfo>) pagination.getRows();
	     for(int i=0;i<list.size();i++){
	    	 AreaBuildingInfo record=list.get(i);
	    	 if(!StringUtils.isBlank(record.getUseNature())) {
					record.setUseNatureLabel(dictionaryService.getTableColumnText(ConstantValue.TABLE_AREA_BUILDING_INFO, ConstantValue.COLUMN_AREA_BUILDING_INFO_USE_NATURE, record.getUseNature()));
				}
	     }
		return pagination;
	}
	
	///---应急指挥 start----------------------------------------------
	/**
	 * 应急指挥里面的事件处理
	 */
	@RequestMapping(value="/standardYjzhEvent")
	public String yjzhEvent(HttpSession session, @RequestParam(value="gridId", required=false) Long gridId, 
			@RequestParam(value="bigType", required=false) String bigType,@RequestParam(value="type", required=false) String type,
			@RequestParam(value="statusName", required=false) String statusName, ModelMap map) {
		try {
			bigType = java.net.URLDecoder.decode(bigType, "utf-8").trim();
			type = java.net.URLDecoder.decode(type, "utf-8").trim();
			
		} catch (UnsupportedEncodingException e) {}
		map.addAttribute("gridId", gridId);
		map.addAttribute("type", type);
		map.addAttribute("bigType", bigType);
		map.addAttribute("statusName", statusName);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		String isNewEvent =  configurationService.turnCodeToValue(ConstantValue.IS_USE_NEW_EVENT,"",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isNewEvent", isNewEvent);
		if(null != isNewEvent && isNewEvent.equals("true")){
			map.addAttribute("type", type.equals("0214")?"0210":type);//新事件群体事件是0210
	        return "/map/arcgis/standardmappage/taijiang/standardYjzhEvent.ftl";
		}
        return "/map/arcgis/standardmappage/taijiang/old/standardYjzhEvent.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value="/yjzhEventListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination yjzhListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId",required=false) String gridId, @RequestParam(value="content", required=false) String content,
			@RequestParam(value="statusName", required=false) String statusName,@RequestParam(value="type", required=false) String type, 
			@RequestParam(value="bigType", required=false) String bigType ) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(page<=0) page=1;
		if(content!=null) content = content.trim();
		if(gridId!=null)gridId = gridId.trim();
		if(type!=null) type = type.trim();
		if(bigType!=null) bigType = bigType.trim();
		if(type==null || "".equals(type)){//如果小类存在，则用小类查询；否则用大类来查询
			type = bigType;
		}
		List<String> statusList=new ArrayList<String>();
		statusList.add(ConstantValue.EVENT_STATUS_REPORT );
		statusList.add(ConstantValue.EVENT_STATUS_DISTRIBUTE);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userInfo.getUserId());
		params.put("gridId", gridId);
		params.put("content", content);
		params.put("type", type);
		params.put("orgCode", userInfo.getOrgCode());
		params.put("statusName", statusName);
		cn.ffcs.common.EUDGPagination pagination = eventDisposalService.findEventDisposalPagination(page, rows, params);
		return pagination;
	}
	
	/**
	 * 获取事件的定位信息
	 * @param session
	 * @param ids 事件ids
	 * @param mapt 地图类型
	 * @param name 
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfYjzhEvent")
	public List<ArcgisInfoOfEvent> getArcgisYjzhEventLocateDataList(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value = "mapt", required=false) Integer mapt) {
		//String eventIds = "";
		String[] results = ids.split(",");
		List<Long> idList = new ArrayList<Long>();
		for(String result : results){
			String eventId = result.split("_")[0];
			idList.add(Long.parseLong(eventId));
		}
		List<GisMapInfo> mapDataList = this.eventDisposalService.findMapDataForEventListByIds(idList, String.valueOf(mapt));
		Long tid = null;
		for(GisMapInfo gisMapInfo:mapDataList){
			for(int i=0; i<results.length; i++) {
				if(results[i].contains((gisMapInfo.getId()+"_"))) {
					try {
						String tidStr = results[i].split("_")[1];
						if(tidStr!=null && tidStr.length()>0) {tid = Long.parseLong(tidStr);}
					} catch (Exception e) {}
				}
			}
			gisMapInfo.setTid(tid);
		}
		List<ArcgisInfoOfEvent> eventlist = new ArrayList<ArcgisInfoOfEvent>();
		for(GisMapInfo gisMapInfo : mapDataList){
			ArcgisInfoOfEvent arcgisInfoOfEvent = new ArcgisInfoOfEvent();
			arcgisInfoOfEvent.setWid(String.valueOf(gisMapInfo.getWid()));
			arcgisInfoOfEvent.setX(gisMapInfo.getX().doubleValue());
			arcgisInfoOfEvent.setY(gisMapInfo.getY().doubleValue());
			arcgisInfoOfEvent.setMapt(gisMapInfo.getMapt());
			arcgisInfoOfEvent.setGridName(gisMapInfo.getName());
			arcgisInfoOfEvent.setName(gisMapInfo.getName());
			arcgisInfoOfEvent.setTaskId(gisMapInfo.getTid());
			eventlist.add(arcgisInfoOfEvent);
		}
		
		return eventlist;
	}
	///---应急指挥 end----------------------------------------------
	
	///---楼宇信息 start------------------------------------------------
	
	/**
	 * 楼宇信息
	 */
	@RequestMapping(value="/building")
	public String building(HttpSession session, @RequestParam(value="gridId") Long gridId,@RequestParam(value="useNature") String useNature, ModelMap map) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("useNature", useNature);
		
		if("001".equals(useNature)){//物业管理住宅
			map.addAttribute("type", "estateManagement");
		}else if("002".equals(useNature)){//社区托管住宅
			map.addAttribute("type", "communityManaged");
		}else if("003".equals(useNature)){//单位自管住宅
			map.addAttribute("type", "unitsManaged");
		}else if("004".equals(useNature)){//宿舍
			map.addAttribute("type", "dormitory");
		}else if("005".equals(useNature)){//民房
			map.addAttribute("type", "citizenHouse");
		}else if("006".equals(useNature)){//写字楼
			map.addAttribute("type", "dormitory");
		}else if("007".equals(useNature)){//单位楼
			map.addAttribute("type", "unitsHouse");
		}else if("008".equals(useNature)){//城市综合体
			map.addAttribute("type", "urbanComplex");
		}else if("009".equals(useNature)){//公园广场
			map.addAttribute("type", "park");
		}else if("010".equals(useNature)){//旅馆
			map.addAttribute("type", "dormitory");
		}else if("011".equals(useNature)){//影剧院
			map.addAttribute("type", "dormitory");
		}else if("012".equals(useNature)){//体育馆
			map.addAttribute("type", "dormitory");
		}else if("013".equals(useNature)){//医院
			map.addAttribute("type", "dormitory");
		}else if("014".equals(useNature)){//银行
			map.addAttribute("type", "dormitory");
		}else if("015".equals(useNature)){//寺庙
			map.addAttribute("type", "workSite");
		}else if("016".equals(useNature)){//工地
			map.addAttribute("type", "dormitory");
		}else if("017".equals(useNature)){//仓库
			map.addAttribute("type", "dormitory");
		}else if("018".equals(useNature)){//商住两用
			map.addAttribute("type", "dormitory");
		}else if("999".equals(useNature)){//其他
			map.addAttribute("type", "dormitory");
		}
		
        return "/map/arcgis/standardmappage/taijiang/building.ftl";
	}
	
	
	/**
	 * 楼宇信息汇总表数据
	 * @param session
	 * @param page 
	 * @param rows
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/buildingListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination listData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId") Long gridId, @RequestParam(value="buildingName", required=false) String buildingName,
			@RequestParam(value="useNature", required=false) String useNature
	) {
		if(page<=0) page=1;
		if(buildingName!=null) buildingName = buildingName.trim();
		if(useNature!=null) useNature = useNature.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", gridId);
		params.put("buildingName", buildingName);
		params.put("useNature", useNature);
		
		cn.ffcs.common.EUDGPagination pagination = areaBuildingInfoService.findAreaBuildingInfoPagination(page, rows, params);
		return pagination;
	}
	///---楼宇信息 end------------------------------------------------
	
	///---片区网格 start------------------------------------------------
	/**
	 * 片区网格
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/segmentGrid")
	public String index(HttpSession session, ModelMap map,@RequestParam(value="gridId") Long gridId,
			@RequestParam(value="segmentGridType") String segmentGridType) {
		
		map.addAttribute("gridId", gridId);
		map.addAttribute("segmentGridType", segmentGridType);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		return "/map/arcgis/standardmappage/taijiang/segmentGrid.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/segmentGridListData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination listData(
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "staticGridId", required = true) Long staticGridId,
			@RequestParam(value = "segmentGridName", required = false) String segmentGridName,
			@RequestParam(value = "parentGridId", required = false) Long parentGridId,
			@RequestParam(value = "segmentGridType", required = false) String segmentGridType,
			@RequestParam(value = "loadMode", required = false) String loadMode) {/* 1:部分加载,2:全量加载*/
		if (page <= 0) page = 1;
		if (parentGridId == null || parentGridId == 0L) {
			parentGridId = staticGridId;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(segmentGridName)) params.put("segmentGridName", segmentGridName);
		if (StringUtils.isNotBlank(segmentGridType)) params.put("segmentGridType", segmentGridType);
		params.put("parentGridId", parentGridId);
		params.put("loadMode", loadMode);
		cn.ffcs.common.EUDGPagination pagination = segmentGridService.findSegmentGridPagination(page, rows, params);
		return pagination;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getArcgisSegmentGridDataList")
	public List<ArcgisInfoOfPublic> getArcgisSegmentGridDataList(HttpSession session, ModelMap map, HttpServletResponse res,
			@RequestParam(value="ids") String ids, @RequestParam(value="mapt", required=false) Integer mapt) {
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisSegmentGridDataListByIds(ids, mapt);
		return list;
	}
	///---片区网格 end------------------------------------------------
	
	/**
	 * 2015-01-20 liushi add 查询护路护线（还有类似的轮廓相关的资源）
	 * @param session
	 * @param map
	 * @param res
	 * @param ids
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisDataOfResourcesListByLevel")
	public List<ArcgisInfoOfPublic> getArcgisDataOfResourcesListByLevel(HttpSession session, ModelMap map, HttpServletResponse res,
			@RequestParam(value="wid") Long wid
			,@RequestParam(value="mapt") Integer mapt
			,@RequestParam(value="targetType") String targetType
			,@RequestParam(value="showType", required = false) String showType) {
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		if("hlhx".equals(targetType)) {
			if("0".equals(showType)) {//查询本身的地图轮廓数据
				list = this.arcgisDataOfLocalService.getArcgisDataOfResourcesSelfById(wid, mapt, targetType);
				for(ArcgisInfoOfPublic arcgisInfoOfPublic:list) {
					if(arcgisInfoOfPublic.getWid().longValue() == wid.longValue()) {
						arcgisInfoOfPublic.setEditAble(true);
					}
				}
			}else if("1".equals(showType)) {//查询上级地图轮廓数据（网格的）
				list = this.arcgisDataOfLocalService.getArcgisDataOfResourcesParentById(wid, mapt, targetType);
			}else if("2".equals(showType)){//查询该线路所属网格同一个层级下的所有网格的线路轮廓数据（不包括自身）
				list = this.arcgisDataOfLocalService.getArcgisDataOfResourcesSamelevelById(wid, mapt, targetType);
			}
		}
		return list;
	}
	
	
	///---台江重点场所 start----------------------------------------------
	/**
	 * 重点场所数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/place")
	public String place(HttpSession session, @RequestParam(value="gridId") Long gridId, ModelMap map) {
		List<PlaceType> plaTypeDC = placeInfoService.getPlaTypeList();
		map.addAttribute("plaTypeDC", plaTypeDC);
		map.addAttribute("gridId", gridId);
		return   "/map/arcgis/standardmappage/taijiang/place.ftl";
	}
	///---台江重点场所 end----------------------------------------------
	
	///---企业 start----------------------------------------------
	/**
	 * 企业数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/tjCorBase")
	public String tjCorBase(HttpSession session, @RequestParam(value="gridId") Long gridId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridId", gridId);
		List<Map<String, Object>> corTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_TYPE, userInfo.getOrgCode());
		map.addAttribute("corTypeDC", corTypeDC);
		List<Map<String, Object>> categoryDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_CATEGORY, userInfo.getOrgCode());
		map.addAttribute("categoryDC", categoryDC);
		return "/map/arcgis/standardmappage/taijiang/corBase.ftl";
	}
	///---企业 end----------------------------------------------
	
	/**
	 * 网格
	 * @param session
	 * @param gridId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/grid")
	public String grid(HttpSession session, @RequestParam(value="gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
			, ModelMap map) {
		MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		int gridLevel = mixedGridInfo.getGridLevel();
		map.addAttribute("gridId", gridId);
		map.addAttribute("gridLevel", gridLevel);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardGrid.ftl";
	}
	
	/**
	 * 网格数据
	 * @param session
	 * @param page 
	 * @param rows
	 * @param gridId
	 * @param name
	 * @param mobileTelephone
	 * @param duty
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/gridListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination gridListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId") Long gridId, @RequestParam(value="gridName", required=false) String gridName,
			@RequestParam(value="gridLevel", required=false) Long gridLevel) {
		
		MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		String orgInfoCode = mixedGridInfo.getInfoOrgCode();
		
		if(page<=0) page=1;
		if(gridName!=null) gridName = gridName.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startGridId", gridId);
		params.put("gridName", gridName);
		params.put("gridLevel", gridLevel);
		params.put("orgInfoCode", orgInfoCode);
		cn.ffcs.common.EUDGPagination pagination = mixedGridInfoService.findMixedGridInfoPaginationByGridLevel(page, rows, params);
		return pagination;
	}
	
	//安监企业start-------------------------------------------------------------------------
	/**
	 * 安监企业数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfAnjianCorBase")
	public String anjianCorBaseInfo(HttpSession session, 
			@RequestParam(value="gridId") Long gridId,
			@RequestParam(value="standard", required=false) String standard,
			@RequestParam(value="economicType", required=false) String economicType, 
			@RequestParam(value="cateVals", required=false) String cateVals,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
			, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridId", gridId);
		List<Map<String, Object>> corTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_TYPE, userInfo.getOrgCode());
		map.addAttribute("corTypeDC", corTypeDC);
		List<Map<String, Object>> categoryDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_CATEGORY, userInfo.getOrgCode());
		map.addAttribute("categoryDC", categoryDC);
		if(StringUtils.isNotBlank(cateVals)){
			map.addAttribute("cateVals", cateVals);
		}
		if(economicType==null) 
			economicType="";
		map.addAttribute("economicType", economicType);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardAnjianCorBase.ftl";
	}
	
	/**
	 * 安监企业管理数据
	 * @param session
	 * @param page 页码
	 * @param rows 每页条数
	 * @param gridId 起始网格ID
	 * @param corName 法人名称
	 * @param corType 法人类型
	 * @param category 行业分类
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/anjianCorBaseListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination anjianCorBaseListData(HttpSession session, 
			@RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId") Long gridId, 
			@RequestParam(value="corName", required=false) String corName,
			@RequestParam(value="corType", required=false) String corType, 
			@RequestParam(value="category", required=false) String category,
			@RequestParam(value="cateVals", required=false) String cateVals,
			@RequestParam(value="economicType", required=false) String economicType) {
		if(page<=0) page=1;
		if(corName!=null) {
			corName = corName.trim();
		}
		if(corType!=null && !"".equals(corType)){
			corType = corType.trim();
		}else {
			corType = null;
		}
		if(category!=null & !"".equals(category)){
			category = category.trim();
		}else {
			category = null;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", gridId);
		params.put("corName", corName);
		params.put("corType", corType);
		params.put("category", category);
		params.put("economicType", economicType);
		if(StringUtils.isNotBlank(cateVals)){
			params.put("cateVals", cateVals);
		}
		cn.ffcs.common.EUDGPagination pagination = corBaseInfoService.findCorBaseInfoByCorpEnvPagination(page, rows, params);
		//EUDGPagination pagination = corBaseInfoService.findCorBaseInfoByGridIdBuildingIdPagination(page, rows, params);
		return pagination;
	}
	
	
	//安监企业end-------------------------------------------------------------------------
	//九小场所start-------------------------------------------------------------------------
	/**
	 * 九小场所数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfZzSmallPlace")
	public String zzSmallPlace(HttpSession session, 
			@RequestParam(value="gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
			, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
//		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
//		map.addAttribute("startGridCode",defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
//		map.addAttribute("startGridName",defaultGridInfo.get(KEY_START_GRID_NAME));
		List<BaseDataDict> placeCataDD = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.DICT_PLACENAME,userInfo.getOrgCode());
		map.addAttribute("placeCataDD", placeCataDD);
		map.addAttribute("placeCataDDCode", ConstantValue.DICT_PLACENAME);// 场所类别
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardZzSmallPlace.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value="/zzSmallPlaceListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination zzSmallPlaceListData(HttpSession session, 
			@RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId") Long gridId,
			@RequestParam(value = "keyWord", required = false) String keyWord,
			@RequestParam(value = "placeCata", required = false) String placeCata) {
		if(page<=0) page=1;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", gridId);
		params.put("keyWord", keyWord);
		params.put("placeCata", placeCata);
		cn.ffcs.common.EUDGPagination pagination = zzSmallPlacesService.findZzSmallPlacesPagination(page, rows, params);
		return pagination;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getArcgisZzSmallPlaceLocateDataList")
	public List<ArcgisInfoOfPublic> getArcgisZzSmallPlaceLocateDataList(HttpSession session, ModelMap map, HttpServletResponse res,
			@RequestParam(value="ids") String ids
			, @RequestParam(value="mapt", required=false) Integer mapt
			,@RequestParam(value = "showType") String showType
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisZzSmallPlaceLocateDataList(ids, mapt);
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	@RequestMapping(value="/zzSmallPlaceDetail")
	public String zzSmallPlaceDetail(HttpSession session, 
			@RequestParam(value="placeId") Long placeId, ModelMap map) {
		ZzSmallPlaces zzSmallPlaces = zzSmallPlacesService
				.findZzSmallPlacesByPlaceId(placeId);
		if (zzSmallPlaces == null) {
			zzSmallPlaces = new ZzSmallPlaces();
		}
		// 获取数据字典中的场所类型
		UserInfo userInfo = (UserInfo) session
				.getAttribute(ConstantValue.USER_IN_SESSION);
		List<BaseDataDict> placeCataDD = baseDictionaryService
				.getDataDictListOfSinglestage(ConstantValue.DICT_PLACENAME,
						userInfo.getOrgCode());
		map.addAttribute("placeCataDD", placeCataDD);
		map.addAttribute("zzSmallPlaces", zzSmallPlaces);
		return "/map/arcgis/standardmappage/zzSmallPlaceDetail.ftl";
	}
	
	//各视联网信息中心 start-------------------------------------------------------------------
	/**
	 * 跳转地图菜单列表页面
	 * @param session
	 * @param gridId
	 * @param elementsCollectionStr
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfNic")
	public String toArcgisDataListOfNic(HttpSession session, 
			@RequestParam(value="gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		List<BaseDataDict> orgLevelDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.DICT_ORG_LEVEL, userInfo.getOrgCode());
		
		map.addAttribute("orgLevelDict", orgLevelDict);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("gridId", gridId);
		
		return "/map/arcgis/standardmappage/standardNic.ftl";
	}
	
	/**
	 * 加载地图页面列表数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param nicInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listNicData", method=RequestMethod.POST)
	public EUDGPagination listNicData(HttpSession session, 
			@RequestParam(value="page") int page, 
			@RequestParam(value="rows") int rows,
			@ModelAttribute(value = "nicInfo") NicInfo nicInfo) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		cn.ffcs.system.publicUtil.EUDGPagination nicInfoPagination = nicService.findNicInfoPagination(page, rows, nicInfo, userInfo.getOrgCode());
		
		EUDGPagination pagination = new EUDGPagination();
		pagination.setRows(nicInfoPagination.getRows());
		pagination.setTotal(nicInfoPagination.getTotal());
		
		return pagination;
	}
	
	/**
	 * 获取地图标注信息
	 * @param session
	 * @param ids 记录id，以英文逗号分隔
	 * @param mapt
	 * @param elementsCollectionStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfNic")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfNic(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = null;
		
		if (StringUtils.isNotBlank(ids)) {
			list = this.arcgisDataOfLocalService.getArcgisNicLocateDataListByIds(ids,mapt);
			
			for(ArcgisInfoOfPublic arcgisInfoOfPublic : list){
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
		}
		
		return list;
	}
	
	/**
	 * 跳转地图概要信息页面
	 * @param session
	 * @param nicId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/nicDetail")
	public String nicDetail(HttpSession session, @RequestParam(value="nicId") Long nicId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		NicInfo nicInfo = nicService.findNicInfoById(nicId, userInfo.getOrgCode());
		String infoOrgCode1 = nicInfo.getInfoOrgCode();

		String gridNames="";
		gridNames= mixedGridInfoService.getGridPath(infoOrgCode1);
		map.addAttribute("gridNames", gridNames);
		
		map.addAttribute("nicInfo", nicInfo);
		
		return "/map/arcgis/standardmappage/nicDetail.ftl";
	}
	//各视联网信息中心 end---------------------------------------------------------------------
	
	//危化企业 start-------------------------------------------------------------------
	/**
	 * 跳转地图菜单列表页面
	 */
	@RequestMapping(value="/toArcgisDataListOfDangerChemistry")
	public String toArcgisDataListOfDangerChemistry(HttpSession session, ModelMap map, 
			@RequestParam(value="gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("gridId", gridId);
		
		return "/map/arcgis/standardmappage/standardDangerChemistry.ftl";
	}
	
	/**
	 * 加载地图页面列表数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param companyName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listDangerChemistryData", method=RequestMethod.POST)
	public EUDGPagination listDangerChemistryData(HttpSession session, 
			@RequestParam(value="page") int page, 
			@RequestParam(value="rows") int rows,
			@RequestParam(value="companyName") String companyName) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("regionCode", userInfo.getOrgCode());
		params.put("companyName", companyName);
		params.put("orgCode", userInfo.getOrgCode());
		cn.ffcs.common.EUDGPagination nicInfoPagination = dangerChemistryManagerService.findDangerChemistryManagerPagination(page, rows, params);
		Long lisTotal = nicInfoPagination.getTotal();
		
		EUDGPagination pagination = new EUDGPagination();
		pagination.setRows(nicInfoPagination.getRows());
		pagination.setTotal(lisTotal.intValue());
		
		return pagination;
	}
	
	/**
	 * 获取地图标注信息
	 * @param session
	 * @param ids 记录id，以英文逗号分隔
	 * @param mapt
	 * @param elementsCollectionStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfDangerChemistry")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfDangerChemistry(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = null;
		
		if (StringUtils.isNotBlank(ids)) {
			list = this.arcgisDataOfLocalService.getArcgisNicLocateDataListByIds(ids,mapt);
			
			for(ArcgisInfoOfPublic arcgisInfoOfPublic : list){
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
		}
		
		return list;
	}
	
	/**
	 * 跳转地图概要信息页面
	 * @param session
	 * @param nicId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/dangerChemistryDetail")
	public String dangerChemistryDetail(HttpSession session, ModelMap map,
			@RequestParam(value="dcmId") Long dcmId) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		DangerChemistryManager dangerChemistryManager = dangerChemistryManagerService.findDangerChemistryManagerById(dcmId, userInfo.getOrgCode());
		String regionCode = dangerChemistryManager.getRegionCode();

		String gridNames="";
		gridNames= mixedGridInfoService.getGridPath(regionCode);
		map.addAttribute("gridNames", gridNames);
		
		map.addAttribute("dangerChemistryManager", dangerChemistryManager);
		
		return "/map/arcgis/standardmappage/dangerChemistryDetail.ftl";
	}
	//危化企业 end---------------------------------------------------------------------
	
	
	//两站两员 隐患路口、隐患路段 开始-------------------------------------------------------------------
	/**
	 * 跳转地图菜单列表页面
	 * @param session
	 * @param gridId
	 * @param elementsCollectionStr
	 * @param drType		隐患类型 1 隐患路口 2 隐患路段
	 * @param infoOrgCode	地图上选择网格过滤时，携带的地域信息
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfDangerRoad")
	public String toArcgisDataListOfDangerRoad(HttpSession session, 
			@RequestParam(value="gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			@RequestParam(value="drType", required=false) String drType,
			@RequestParam(value="infoOrgCode", required=false) String infoOrgCode,
			ModelMap map) {
		if(StringUtils.isNotBlank(infoOrgCode)) {
			map.addAttribute("infoOrgCode", infoOrgCode);
		}
		
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("gridId", gridId);
		map.addAttribute("drType", drType);
		
		return "/map/arcgis/standardmappage/hiddenDangerRoad/list_dangerRoad.ftl";
	}
	
	/**
	 * 加载地图页面列表数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param dangRoad
	 * 			infoOrgCode	所属地域编码
	 * 			drType 		隐患类型，1 隐患路口 2 隐患路段
	 * 			drName		路口/路段名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listDangerRoadData", method=RequestMethod.POST)
	public EUDGPagination listDangerRoadData(HttpSession session, 
			@RequestParam(value="page") int page, 
			@RequestParam(value="rows") int rows,
			@ModelAttribute(value = "dangRoad") DangRoad dangRoad) {
		Map<String, Object> params = new HashMap<String, Object>();
		String infoOrgCode = dangRoad.getInfoOrgCode(),
			   drName = dangRoad.getDrName();
		
		if(StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = this.getDefaultInfoOrgCode(session);
		}
		
		params.put("infoOrgCode", infoOrgCode);
		params.put("drType", dangRoad.getDrType());
		
		if(StringUtils.isNotBlank(drName)) {
			params.put("drName", drName);
		}
		
		cn.ffcs.system.publicUtil.EUDGPagination dataPagination = dangRoadService.searchList(page, rows, params);
		
		EUDGPagination pagination = new EUDGPagination();
		pagination.setRows(dataPagination.getRows());
		pagination.setTotal(dataPagination.getTotal());
		
		return pagination;
	}
	
	/**
	 * 获取地图标注信息
	 * @param session
	 * @param ids 记录id，以英文逗号分隔
	 * @param mapt
	 * @param elementsCollectionStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfDangerRoad")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfDangerRoad(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = null;
		
		if (StringUtils.isNotBlank(ids)) {
			list = this.arcgisDataOfLocalService.getArcgisDangerRoadLocateDataListByIds(ids,mapt);
			
			for(ArcgisInfoOfPublic arcgisInfoOfPublic : list){
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
		}
		
		return list;
	}
	
	/**
	 * 跳转地图概要信息页面
	 * @param session
	 * @param drId	隐患编号
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toDangerRoadDetail")
	public String toDangerRoadDetail(HttpSession session, 
			@RequestParam(value="drId") Long drId, 
			@RequestParam(value="isDetail", required=false, defaultValue="true") boolean isDetail, 
			ModelMap map) {
		DangRoad dangRoad = dangRoadService.searchById(drId);
		String forwardUrl = "/map/arcgis/standardmappage/hiddenDangerRoad/detail_dangerRoad_brief.ftl";
		
		if(dangRoad == null) {
			dangRoad = new DangRoad();
		}
		
		if(isDetail) {
			forwardUrl = "/map/arcgis/standardmappage/hiddenDangerRoad/detail_dangerRoad.ftl";
			
			map.addAttribute("downPath", App.IMG.getDomain(session));
		}
		
		map.addAttribute("dangRoad", dangRoad);
		
		return forwardUrl;
	}
	
	/**
	 * 两站两员隐患路口、隐患路段流程信息
	 * @param session
	 * @param drId	隐患编号
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/fetchDangerRoadflowDetail")
	public String fetchDangerRoadflowDetail(HttpSession session,
			@RequestParam(value = "drId") Long drId, 
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		try {
			List<Map<String, Object>> queryTaskList = dangRoadService.queryTaskList(drId, userInfo);
			
			if(queryTaskList != null) {
				boolean isCurrentTask = false;
				
				for(Map<String, Object> task : queryTaskList) {
					isCurrentTask = false;
					
					if(CommonFunctions.isNotBlank(task, "IS_CURRENT_TASK")) {
						isCurrentTask = Boolean.valueOf(task.get("IS_CURRENT_TASK").toString()); 
					}
					
					if(isCurrentTask) {//当前环节数据调整
						Map<String, Object> subTaskMap = new HashMap<String, Object>(), 
											timeAndRemarkMap = new HashMap<String, Object>();
						List<Map<String, Object>> subAndReceivedTaskList = new ArrayList<Map<String, Object>>(),
												  timeAndRemarkList = new ArrayList<Map<String, Object>>();
						StringBuffer handlePerson = new StringBuffer("");
						
						if(CommonFunctions.isNotBlank(task, "WF_ACTIVITY_NAME_") && CommonFunctions.isBlank(task, "TASK_NAME")) {
							task.put("TASK_NAME", task.get("WF_ACTIVITY_NAME_"));
						}
						if(CommonFunctions.isNotBlank(task, "WF_DBID_")) {
							Long taskId = -1L;
							
							try {
								Long.valueOf(task.get("WF_DBID_").toString());
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
							
							if(CommonFunctions.isBlank(task, "TASK_ID")) {
								task.put("TASK_ID", taskId);
							}
							subTaskMap.put("TASK_ID", taskId);
						}
						if(CommonFunctions.isNotBlank(task, "WF_USERNAME")) {
							handlePerson.append(task.get("WF_USERNAME"));
							
							subTaskMap.put("TRANSACTOR_NAME", task.get("WF_USERNAME"));
						}
						if(CommonFunctions.isNotBlank(task, "WF_ORGNAME")) {
							handlePerson.append("(").append(task.get("WF_ORGNAME")).append(")");
							
							subTaskMap.put("ORG_NAME", task.get("WF_ORGNAME"));
						}
						if(CommonFunctions.isBlank(task, "HANDLE_PERSON")) {
							task.put("HANDLE_PERSON", handlePerson);
						}
						if(CommonFunctions.isNotBlank(task, "WF_START_TIME")) {
							timeAndRemarkMap.put("RECEIVE_TIME", task.get("WF_START_TIME"));
						}
						
						timeAndRemarkList.add(timeAndRemarkMap);
						
						subTaskMap.put("timeAndRemarkList", timeAndRemarkList);
						subAndReceivedTaskList.add(subTaskMap);
						
						task.put("subAndReceivedTaskList", subAndReceivedTaskList);
					}
				}
			}
			
			map.addAttribute("taskList", queryTaskList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "/zzgl/event/workflow/detail_workflow.ftl";
	}
	//两站两员 隐患路口、隐患路段 结束---------------------------------------------------------------------
	//重大产业  start------------------------------------------------------------------------------
	/**
	 * 重大产业列表页面
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toArcgisDataListOfMajorIndustry")
	public String standardMajorIndustry(HttpSession session, ModelMap map, @RequestParam(value = "gridId") long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("gridId", gridId);
		String forward ="/map/arcgis/standardmappage/standardMajorIndustry.ftl";
		return forward;
	}
	
	/**
	 * 重大产业列表数据加载
	 * @param session
	 * @param industryName 重大产业名称
	 * @param industryStatus  重大产业状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/majorIndustryListData", method= RequestMethod.POST)
	public cn.ffcs.system.publicUtil.EUDGPagination majorIndustryListData(
					    HttpSession session, @RequestParam(value = "page") int page,
					    @RequestParam(value = "rows") int rows,
					    @RequestParam(value = "industryName", required = false) String industryName,
					    @RequestParam(value = "yearStr", required = false) String yearStr,
					    @RequestParam(value = "industryStatus", required = false) String industryStatus
					   ) {
		    if (page <= 0) page = 1;   
	        Map<String, Object> params = new HashMap<String, Object>();	  
	        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			String OrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
	        if(StringUtils.isNotBlank(yearStr) && (yearStr != null))
	        {
	        	String befinTime=yearStr+"-01-01";
	        	String endTime=yearStr+"-12-31";
	        	params.put("beginTime",befinTime);
	        	params.put("endTime",endTime);
	        }
	        if (StringUtils.isNotBlank(industryName) && (industryName != null)) {
				params.put("industryName", '%' + industryName + '%');
			}	  
	        if (StringUtils.isNotBlank(OrgCode) && (OrgCode != null)) {
				params.put("orgCode", OrgCode + '%');
			}
	        if (StringUtils.isNotBlank(industryStatus) && (industryStatus != null)) {
				params.put("industryStatus",industryStatus);
			}
	        cn.ffcs.system.publicUtil.EUDGPagination pagination =majorIndustryService.searchList(page,rows, params,"");
	        return pagination;
	    }
	/**
	 * 重大产业地图定位
	 * @param ids	重大产业id编号
	 * @param mapt 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfMajorIndustry")
		public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfmajorIndustry(HttpSession session,
				ModelMap map, HttpServletResponse res,
				@RequestParam(value = "ids") String ids,
				@RequestParam(value = "mapt") Integer mapt
				, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
	    	List<ArcgisInfoOfPublic> list=this.arcgisDataOfLocalService.getArcgisPoliceLocateDataListByIds(ids,05,"7766");
	    	for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
	    		String elementsCollectionStrType = elementsCollectionStr;
	    		MajorIndustry industry = majorIndustryService.searchById(arcgisInfoOfPublic.getId());
	    		if(StringUtils.isNotBlank(industry.getIndustryStatus())){
	    			if("已开工".equals(industry.getIndustryStatus())){  	    				
	    				elementsCollectionStrType = elementsCollectionStrType.replaceAll("/images/map/gisv0/map_config/unselected/industry_begin.png","/images/map/gisv0/map_config/unselected/industry_begin.png");
	    			}
	    			else if("年内开工".equals(industry.getIndustryStatus())){
	    				elementsCollectionStrType = elementsCollectionStrType.replaceAll("/images/map/gisv0/map_config/unselected/industry_begin.png","/images/map/gisv0/map_config/unselected/insdustry_year.png");	 
	    			}
	    			else{
	    				elementsCollectionStrType = elementsCollectionStrType.replaceAll("/images/map/gisv0/map_config/unselected/industry_begin.png","/images/map/gisv0/map_config/unselected/industry_store.png");	    			
	    			}
	    		}
	    		arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStrType);
	    		arcgisInfoOfPublic.setBizType(industry.getIndustryStatus());
	    	}
			return list;
	 }
	
	/**
	* 重大产业详情页面
	* 
    * @return
    */
	@RequestMapping(value = "/majorIndustryDetail")
	public String majorIndustryDetail(HttpSession session, ModelMap map,
				@RequestParam(value = "industryId") Long industryId) {
		MajorIndustry bo = majorIndustryService.searchById(industryId);
		map.addAttribute("industryinfo", bo);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		return "/map/arcgis/standardmappage/majorIndustryDetail.ftl";
	}

	/**
	 * 产业简介 
	 * @param session
	 * @param map
	 * @param industryId
     * @return
     */
	@RequestMapping(value = "/detail")
	public String cyjjDetail(HttpSession session, ModelMap map,
			@RequestParam(value = "industryId") Long industryId
			,@RequestParam(value = "type") String type) {
		if(StringUtils.isEmpty(type)){
			type="cyjj";
		}
		MajorIndustry bo = majorIndustryService.searchById(industryId);
		map.addAttribute("industryinfo", bo);
		return "/map/arcgis/standardmappage/"+type+"Detail.ftl";
	}  
	//重大产业 结束---------------------------------------------------------------------------------	
	
	///---民警 start----------------------------------------------
		/**
		 * 民警数据首页
		 * @param session
		 * @param gridId 网格ID
		 * @param map
		 * @return
		 */
		@RequestMapping(value="/toArcgisDataListOfPcManage")
		public String PcManage(HttpSession session, @RequestParam(value="gridId") long gridId,
				@RequestParam(value="gridCode") String gridCode,
				 @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
				, ModelMap map) {
			
			map.addAttribute("gridId", gridId);
			map.addAttribute("gridCode", gridCode);
			
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			
			List<BaseDataDict> PCManageDict = baseDictionaryService.getDataDictListOfSinglestage(PC_MANAGE, userInfo.getOrgCode());
			
			map.addAttribute("PCManageDict", PCManageDict);
		
			map.addAttribute("elementsCollectionStr", elementsCollectionStr);
			return "/map/arcgis/standardmappage/standardPcManage.ftl";
		}
		
		/**
		 * 企业管理数据
		 * @param session
		 * @param page 页码
		 * @param rows 每页条数
		 * @param gridId 起始网格ID
		 * @param corName 法人名称
		 * @param corType 法人类型
		 * @param category 行业分类
		 * @return
		 * @throws Exception 
		 */
		@ResponseBody
		@RequestMapping(value = "/PcManageListData", method = RequestMethod.POST)
		public cn.ffcs.system.publicUtil.EUDGPagination PcManageListData(HttpSession session, @RequestParam(value = "page") int page,
				@RequestParam(value = "rows") int rows, @RequestParam(value = "gridId") long gridId,@RequestParam(value = "gridCode") String gridCode,
				@RequestParam(value = "pName", required = false) String pName,
				@RequestParam(value = "certNumber", required = false) String certNumber,
				@RequestParam(value = "pStation", required = false) String pStation
				) throws Exception {
			if(page<=0) page=1;
			if(pName!=null) {
				pName = pName.trim();
			}
			if(certNumber!=null && !"".equals(certNumber)){
				certNumber = certNumber.trim();
			}else {
				certNumber = null;
			}
			if(pStation!=null & !"".equals(pStation)){
				pStation = pStation.trim();
			}else {
				pStation = null;
			}
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("gridCode", gridCode);
			params.put("pName", pName);
			params.put("certNumber", certNumber);
			params.put("pStation", pStation);
			PoliceManagement bo = new PoliceManagement();
			bo.setRegionCode(gridCode);
			bo.setCertNumber(certNumber);
			bo.setpName(pName);
			bo.setpStation(pStation);
			page = page < 1 ? 1 : page;
			rows = rows < 1 ? 20 : rows;
			bo.setPage(page);
			bo.setRows(rows);
			cn.ffcs.system.publicUtil.EUDGPagination pagination = policeManagementService.searchList(bo);
			//EUDGPagination pagination = corBaseInfoService.findCorBaseInfoByGridIdBuildingIdPagination(page, rows, params);
			return pagination;
		}
		
		
		/**
		 * 民警管理GIS信息
		 * @param session
		 * @param map
		 * @param res
		 * @param ids
		 * @param mapt
		 * @param showType
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getArcgisCorLocatePCDataList")
		public List<ArcgisInfoOfPublic> getArcgisCorLocatePCDataList(HttpSession session, ModelMap map, HttpServletResponse res,
				@RequestParam(value="ids") String ids, @RequestParam(value="mapt", required=false) Integer mapt,
				@RequestParam(value = "showType") String showType
				, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
			List<ArcgisInfoOfPublic> list = null;
			if(infoOrgCode.startsWith(ConstantValue.HAICANG_FUNC_ORG_CODE)
					|| infoOrgCode.startsWith(ConstantValue.JIANGYIN_FUNC_ORG_CODE)
					|| infoOrgCode.startsWith(ConstantValue.YANPING_FUNC_ORG_CODE)){
				list = arcgisDataOfLocalService.getArcgisCorLocateDataListByIds(ids, mapt, "CORBASE");
			}else{
				list = this.arcgisDataOfLocalService.getArcgisCorLocateDataListByIds(ids, mapt);
			}
			for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
			return list;
		}
		
		/**
		 * 查看法人单位基础信息
		 * @param session
		 * @param plaId
		 * @param map
		 * @return
		 * @throws Exception 
		 */
		@RequestMapping(value="/getPCInfoDetailOnMap")
		public String getPCInfoDetailOnMap(HttpSession session, HttpServletRequest request, 
				@RequestParam(value="id", required = false) Long id,
				ModelMap map) throws Exception{
			//System.out.println("++++++++++++++++++++id::::::::::"+id); 
			PoliceManagement bo = policeManagementService.searchById(id);
			//System.out.println("++++++++++++++++++++bo::::::::::"+bo); 
			map.addAttribute("bo", bo);
			
			map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
			return "/map/arcgis/standardmappage/pcManageBaseDetail.ftl";

		}
		
		
		/**
		 * 2014-07-09 chenlf add
		 * 获取出租屋的定位信息
		 * @param session
		 * @param ids 出租屋ids
		 * @param mapt 地图类型
		 * @param showType 地图显示类型1、全显示，2、只显示当前页
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getArcgisLocateDataListOfPcManage")
		public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfPcManage(HttpSession session, ModelMap map, HttpServletResponse res,
				@RequestParam(value="resourcesIds") String resourcesIds, @RequestParam(value="mapt", required=false) Integer mapt,
				@RequestParam(value = "showType") String showType
				, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
			//System.out.println("::::::::::::::::::::::::::::::::"+mapt);
			//Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			//String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
			String markerType = "3390";
			List<ArcgisInfoOfPublic> list = arcgisDataOfLocalService.getArcgisLocateDataListOfPcManageByIds(resourcesIds, mapt, markerType);
			//System.out.println("::::::::::::::::::::::::ids++++++::::::::"+resourcesIds);
			for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
			//System.out.println("::::::::::::::::::::::::::::::list++++++++++++++++++++++++++++++++::"+list.get(0).getX());
			//System.out.println("::::::::::::::::::::::::::::::list++++++++++++++++++++++++++++++++::"+list.get(0).getY());
			return list;
		}
		
		/**
		 * 南平企业
		 */
		@RequestMapping(value = "/indexEnterpriseNP")
		public String corpEnvNP(HttpSession session, ModelMap map, HttpServletResponse res,
				@RequestParam(value = "pollutantType",required=false) String pollutantType,
				@RequestParam(value = "isRegulatory",required=false) String isRegulatory,
				@RequestParam(value = "elementsCollectionStr",required=false) String elementsCollectionStr
				) {
			map.addAttribute("pollutantType", pollutantType);
			map.addAttribute("isRegulatory", isRegulatory);
			map.addAttribute("elementsCollectionStr", elementsCollectionStr);
			return "/map/arcgis/standardmappage/standardCorEnv_np.ftl";
		}
		
		@ResponseBody
		@RequestMapping(value = "/findPageListCorpEnvNP")
		public cn.ffcs.common.EUDGPagination findPageListCorpEnvNP(HttpSession session, 
				@RequestParam(value = "page") int page,
				@RequestParam(value = "rows") int rows,
				@RequestParam(value = "comName",required=false) String comName,
				@RequestParam(value = "isRegulatory",required=false) String isRegulatory,
				@RequestParam(value = "pollutantType",required=false) String pollutantType) {
			if(page<=0) page=1;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("infoOrgCode", getDefaultGridInfo(session).get(KEY_DEFAULT_INFO_ORG_CODE).toString());
			if(StringUtils.isNotBlank(comName)) {params.put("comName", comName);}
			if(StringUtils.isNotBlank(pollutantType)) {params.put("pollutantType", pollutantType);}
			if(StringUtils.isNotBlank(isRegulatory)) {params.put("isRegulatory", isRegulatory);}
			return corBaseInfoService.findPageListCorpEnvNP(page, rows, params);
		}
		
		@ResponseBody
		@RequestMapping(value = "/getArcgisCorEnvNPDataList")
		public List<ArcgisInfoOfPublic> getArcgisCorEnvNPDataList(HttpSession session, ModelMap map, HttpServletResponse res,
				@RequestParam(value="ids") String ids, 
				@RequestParam(value="mapt") Integer mapt,
				@RequestParam(value="markerType") String markerType,
				@RequestParam(value="elementsCollectionStr") String elementsCollectionStr,
				@RequestParam(value = "pollutantType",required=false) String pollutantType
				) {
			List<ArcgisInfoOfPublic> list = arcgisDataOfLocalService.getArcgisCorEnvNPLocateDataListByIds(ids, mapt, markerType);
			elementsCollectionStr=elementsCollectionStr.replace(pollutantType+".png", pollutantType+"_0.png");
			for(ArcgisInfoOfPublic arc:list){
				if(arc.getStatus()!=null && "0".equals(arc.getStatus())) {//不达标
					arc.setElementsCollectionStr(elementsCollectionStr);
				}
			}
			
			return list;
		}
	
}
