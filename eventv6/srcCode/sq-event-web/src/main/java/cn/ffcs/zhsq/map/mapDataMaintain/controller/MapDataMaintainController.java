package cn.ffcs.zhsq.map.mapDataMaintain.controller;


import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.geo.mybatis.domain.xieJingAddress.XieJingAddress;
import cn.ffcs.geo.xieJingAddress.service.IXieJingAddressService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.AreaBuildingInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResType;
import cn.ffcs.shequ.zzgl.service.firegrid.IFireResourceService;
import cn.ffcs.shequ.zzgl.service.grid.IAreaBuildingInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.base.local.service.IGridLogService;
import cn.ffcs.zhsq.base.local.service.LogModule;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceGpsDataService;
import cn.ffcs.zhsq.enforceRecorder.service.IEnforceRecorderService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.map.mapDataMaintain.service.IMapDataMaintainService;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.*;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.szzg.resource.service.ZgResourceInfoService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 地图数据维护
 * @sulch
 * @2018-04-12
 */
@Controller
@RequestMapping(value="/zhsq/map/mapDataMaintain")
public class MapDataMaintainController extends ZZBaseController {

	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IAreaBuildingInfoService areaBuildingInfoService;
	@Autowired
	private IResInfoService resInfoService;
	@Autowired
	private IMenuConfigService menuConfigService;
	@Autowired
	private IArcgisInfoService arcgisInfoService;
	@Autowired
	private IXieJingAddressService xieJingAddressService;
	@Autowired
	private IMapDataMaintainService mapDataMaintainService;
	@Autowired
	private IResMarkerService resMarkerService;
	@Autowired
	private ZgResourceInfoService zgResourceInfoService;
	@Autowired
	private IFireResourceService fireResourceService;
	@Autowired
	private DeviceGpsDataService deviceGpsDataService;

	@Resource(name="enforceRecorderServiceImpl")
	private IEnforceRecorderService enforceRecorderService;


	/**
	 * 地图数据维护首页
	 * @param session
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/index")
	public String index(HttpSession session, HttpServletRequest request,
		  	HttpServletResponse response, ModelMap map) throws Exception{
		String forward = "/map/mapDataMaintain/mapDataMaintain_index.ftl";

		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");

		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);

		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));

		map.put("gridId", gridInfo.getGridId());
		map.put("gridCode", gridInfo.getGridCode());
		map.put("gridName", gridInfo.getGridName());
		map.put("gridLevel", gridInfo.getGridLevel());
		map.put("infoOrgCode", gridInfo.getInfoOrgCode());
		map.put("userName", userInfo.getUserName());
		return forward;
	}

	@RequestMapping(value="/toMapDataList")
	public String toMapDataList(HttpSession session, HttpServletRequest request,
						HttpServletResponse response, ModelMap map,
						@RequestParam(value="gridId", required = false) Long gridId) throws Exception{
		String forward = "/map/mapDataMaintain/mapDataList_index.ftl";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		MixedGridInfo gridInfo = new MixedGridInfo();
		if(gridId != null){
			gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		}else{
			gridInfo = getMixedGridInfo(session,request);
		}

		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");

		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String startOrgCode = (String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);

		//获取“是否启用三维地图”的功能配置
		String isUseTwoTypesMap = "";
		isUseTwoTypesMap =  funConfigurationService.
				turnCodeToValue(ConstantValue.IS_USE_TWO_TYPES_MAP,"",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUseTwoTypesMap", isUseTwoTypesMap);

		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.put("ZZGRID_URL", App.ZZGRID.getDomain(session));

		map.put("gridId", gridId);
		map.put("gridCode", gridInfo.getGridCode());
		map.put("gridName", gridInfo.getGridName());
		map.put("gridLevel", gridInfo.getGridLevel());
		map.put("infoOrgCode", gridInfo.getInfoOrgCode());
		map.put("socialOrgCode", userInfo.getOrgCode());
		map.put("userName", userInfo.getUserName());

		return forward;
	}

	/**
	 * 转载到地图加载页面
	 * @param session
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toMap")
	public String toMap(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception{
		String forward = "";
		forward = "/map/mapDataMaintain/map_index.ftl";
		MixedGridInfo gridInfo = getMixedGridInfo(session, request);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		map.put("gridId", gridInfo.getGridId());
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		String OUTLINE_FONT_SETTINGS = this.funConfigurationService.turnCodeToValue("OUTLINE_FONT_SETTINGS", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		String ARCGIS_DOCK_MODE = this.funConfigurationService.turnCodeToValue("ARCGIS_DOCK_MODE", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}

		String OUTLINE_FONT_SETTINGS_BUILD = this.funConfigurationService.turnCodeToValue("OUTLINE_FONT_SETTINGS_BUILD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		if(ARCGIS_DOCK_MODE == null || "".equals(ARCGIS_DOCK_MODE)) {
			ARCGIS_DOCK_MODE = "0";
		}
		map.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
		map.put("OUTLINE_FONT_SETTINGS_BUILD", OUTLINE_FONT_SETTINGS_BUILD);
		map.put("ARCGIS_DOCK_MODE", ARCGIS_DOCK_MODE);
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragrma", "no-cache");
		response.setDateHeader("Expires",0);
		return forward;
	}


	@ResponseBody
	@RequestMapping(value="/builgingListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination builgingListData(HttpServletRequest req,
			   @RequestParam(value="page") int page,
			   @RequestParam(value="rows") int rows,
			   @RequestParam(value="gridId") Long gridId,
			   @RequestParam(value="buildingName", required=false) String buildingName,
			   @RequestParam(value="queryStr", required=false) String queryStr,
			   @RequestParam(value="buildingId", required=false) String buildingId) {
		UserInfo userInfo = (UserInfo) req.getSession().getAttribute(ConstantValue.USER_IN_SESSION);
		if(page<=0) page=1;
		if(StringUtils.isNotBlank(buildingName)) buildingName = buildingName.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		if(buildingId!=null) params.put("bId", buildingId);
		params.put("gridId", gridId);
		params.put("buildingName", buildingName);
		params.put("q", queryStr);
		params.put("orgCode", userInfo.getOrgCode());

		cn.ffcs.common.EUDGPagination pagination = areaBuildingInfoService.findAreaBuildingInfoPagination(page, rows, params);
		return pagination;
	}

	/**
	 * 楼宇地址唯一性判断（全库）
	 * @param session
	 * @param buildingAddress
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/checkBuildingAddress", method=RequestMethod.POST)
	public Map<String, Object> checkBuildingAddress(HttpSession session,
													@RequestParam(value="buildingAddress", required=false) String buildingAddress,
													@RequestParam(value="buildingId", required=false) Long buildingId
	) {
		Map<String, Object> result = new HashMap<String, Object>();
		Integer count = 0;
		if(StringUtils.isNotBlank(buildingAddress)){
			count = areaBuildingInfoService.repeatCountForBuildingAddress(null, buildingAddress.trim(), buildingId);
		}
		if(count>0){
			result.put("checkResult", false);
		}else{
			result.put("checkResult", true);
		}

		return result;
	}

	/**
	 * 综治.市政设施（资源），数据列表
	 * @param session
	 * @param page
	 * @param rows
	 * @param resTypeId 资源类型
	 * @param gridId    网格id
	 * @param resName   资源名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/resourceListData", method=RequestMethod.POST)
	public EUDGPagination resourceListData(HttpSession session
			, @RequestParam(value="page") int page
			, @RequestParam(value="rows") int rows
			, @RequestParam(value="resTypeId", required = false) Long resTypeId
			, @RequestParam(value="resTypeCode", required=false) String resTypeCode
			, @RequestParam(value="gridId") Long gridId
			, @RequestParam(value="infoOrgCode") String infoOrgCode
			, @RequestParam(value="resName", required=false) String resName) {
		if(page<=0) page=1;
		if(StringUtils.isNotBlank(resName)) resName = resName.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resName", resName);
		params.put("resTypeId", resTypeId);
		params.put("startGridId", gridId);
		params.put("infoOrgCode", infoOrgCode);
		params.put("resTypeCode", resTypeCode);
		EUDGPagination pagination = mapDataMaintainService.findResInfoPagination(page, rows, params);
		return pagination;
	}

	/**
	 * 网格列表
	 * @param session
	 * @param gridName
	 * @param infoOrgCode
	 * @param drawedType
	 * @param isMarker
	 * @param mapt
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/gridListData", method = RequestMethod.POST)
	public EUDGPagination gridListData(
			HttpSession session,
			@RequestParam(value = "gridName", required = false) String gridName,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "drawedType", required = false) String drawedType,
			@RequestParam(value = "isMarker", required = false) String isMarker,
			@RequestParam(value = "mapt", required = false) String mapt,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(gridName)){
			paramsMap.put("gridName", gridName);
		}
		paramsMap.put("infoOrgCode", infoOrgCode);
		paramsMap.put("drawedType", drawedType);
		paramsMap.put("isMarker", isMarker);
		paramsMap.put("mapt", mapt);
		EUDGPagination eudPagination = new EUDGPagination();
		try {
			eudPagination = mapDataMaintainService.findGridInfoPagination(page, rows, paramsMap);
		}catch (Exception e){
			e.printStackTrace();
		}
		return eudPagination;
	}

	/**
	 * 网格列表
	 * @param session
	 * @param regionCode
	 * @param x
	 * @param y
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/xiejingListData", method = RequestMethod.POST)
	public List<XieJingAddress> xiejingListData(
			HttpSession session,
			@RequestParam(value = "regionCode", required = false) String regionCode,
			@RequestParam(value = "x", required = false) String x,
			@RequestParam(value = "y", required = false) String y,
			@RequestParam(value = "distance", required = false) String distance) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(regionCode)){
			paramsMap.put("regionCode", regionCode);
		}
		if(StringUtils.isNotBlank(x)){
			paramsMap.put("x", x);
		}
		if(StringUtils.isNotBlank(y)){
			paramsMap.put("y", y);
		}
		paramsMap.put("distance", StringUtils.isNotBlank(distance)?distance:100);//中心点范围
		distance=StringUtils.isNotBlank(distance)?distance:"100";
		List<XieJingAddress> xieJingAddressList = new ArrayList<>();
		if(userInfo.getOrgCode().startsWith(ConstantValue.JINJIANG_INFO_ORG_CODE)) {
			try {
				xieJingAddressList = xieJingAddressService.getAddressFromXiejing(x, y, Long.valueOf(distance), regionCode);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}else {
			xieJingAddressList = xieJingAddressService.findNearbyAddressByParam(paramsMap);
		}
		
		return xieJingAddressList;
	}

	@ResponseBody
	@RequestMapping(value = "/getGisDataCfgByCode", method = RequestMethod.POST)
	public GisDataCfg getGisDataCfgByCode(
			HttpSession session,
			@RequestParam(value = "menuCode", required = false) String menuCode) {
		GisDataCfg gisDataCfg = new GisDataCfg();
		if(StringUtils.isNotBlank(menuCode)){
			gisDataCfg = mapDataMaintainService.getGisDataCfgByCode(menuCode, null);
			if(gisDataCfg !=null) {
				transGisDataCfgUrl(gisDataCfg, session);
			}
		}
		return gisDataCfg;
	}

	@ResponseBody
	@RequestMapping(value="/updateMapDataOfBuilding")
	public Map<String,Object> updateMapDataOfBuilding(HttpSession session, HttpServletRequest request, ModelMap map,
				@RequestParam(value = "wid") Long wid,
				@RequestParam(value= "mapt") Integer mapt,
				@RequestParam(value= "x",required = false) String x,
				@RequestParam(value= "y",required = false) String y,
				@RequestParam(value= "hs",required = false) String hs
	) {
		ArcgisInfoOfBuild arcgisInfoOfBuild = new ArcgisInfoOfBuild();
		if(wid != null){
			arcgisInfoOfBuild.setWid(wid);
		}
		if(mapt != null){
			arcgisInfoOfBuild.setMapt(mapt);
		}
		if(StringUtils.isNotBlank(x) && !"undefined".equals(x) && !"NaN".equals(x)){
			arcgisInfoOfBuild.setX(Double.parseDouble(x));
		}
		if(StringUtils.isNotBlank(y) && !"undefined".equals(y) && !"NaN".equals(y)){
			arcgisInfoOfBuild.setY(Double.parseDouble(y));
		}
		if(StringUtils.isNotBlank(x) && !"undefined".equals(x)){
			arcgisInfoOfBuild.setX(Double.parseDouble(x));
		}else{
			arcgisInfoOfBuild.setX(null);
		}
		if(StringUtils.isNotBlank(y) && !"undefined".equals(x)){
			arcgisInfoOfBuild.setY(Double.parseDouble(y));
		}else{
			arcgisInfoOfBuild.setY(null);
		}
		if (hs != null && !"undefined".equals(hs)){
			arcgisInfoOfBuild.setHs(hs);
		}
		arcgisInfoOfBuild.setAreaColor("#ff0000");
		arcgisInfoOfBuild.setNameColor("#ff0000");
		arcgisInfoOfBuild.setLineColor("#ff0000");
		arcgisInfoOfBuild.setLineWidth(1);
		boolean flag = this.arcgisInfoService.saveArcgisDrawAreaOfBuild(arcgisInfoOfBuild);

		Map<String,Object> result = new HashMap<String,Object>();
		result.put("flag", flag);
		return result;
	}


	/**
	 * 批量删除楼房信息详情
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delBuilding", method=RequestMethod.POST)
	public Map<String, Object> delBuilding(HttpSession session, HttpServletRequest request, @RequestParam(value="idStr") String idStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String[] ids = idStr.split(",");
		List<Long> recordIdList = new ArrayList<Long>();
		for(int i=0; i<ids.length; i++) {
			try {
				long recordId = Long.parseLong(ids[i]);
				recordIdList.add(recordId);
				mixedGridInfoService.synchronousGrid(recordId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		boolean result = areaBuildingInfoService.deleteAreaBuildingInfoById(userInfo.getUserId(), recordIdList);
		//***********strart************记录日志**********************/
		if(result){
			for(int i=0; i<ids.length; i++) {
				long recordId1 = Long.parseLong(ids[i]);
				AreaBuildingInfo oldRecord = areaBuildingInfoService.findAreaBuildingInfoById(recordId1);//去除之前未更新BEAN
				String valueBefore = JSONObject.fromObject(oldRecord).toString();
				String recordName = oldRecord.getBuildingName();
				logService.savelog2(request, LogModule.areaBuildingInfo, IGridLogService.ActionType.delete,
						recordId1, recordName, valueBefore, null);
			}
		}
		//***********end************记录日志**********************/
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result?recordIdList.size():0L);
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "/updateMapDataOfGrid")
	public Map<String, Object> updateMapDataOfGrid(
			HttpSession session,
			HttpServletRequest request,
			ModelMap map,
			@RequestParam(value = "wid") Long wid,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "x", required = false) String x,
			@RequestParam(value = "y", required = false) String y,
			@RequestParam(value = "hs", required = false) String hs) {
		ArcgisInfoOfGrid arcgisInfoOfGrid = new ArcgisInfoOfGrid();
		List<ArcgisInfoOfGrid> arcgisInfoOfGrid2 = new ArrayList<ArcgisInfoOfGrid>();
		if (wid != null) {
			arcgisInfoOfGrid.setWid(wid);
		}
		if (mapt != null) {
			arcgisInfoOfGrid.setMapt(mapt);
		}

		if (StringUtils.isNotBlank(x) && !"undefined".equals(x) && !"NaN".equals(x)) {
			arcgisInfoOfGrid.setX(Double.parseDouble(x));
		}else {
			arcgisInfoOfGrid.setX(null);
		}
		if (StringUtils.isNotBlank(y) && !"undefined".equals(y) && !"NaN".equals(y)) {
			arcgisInfoOfGrid.setY(Double.parseDouble(y));
		}else {
			arcgisInfoOfGrid.setY(null);
		}
		if (StringUtils.isNotBlank(hs) && !"undefined".equals(hs)) {
			arcgisInfoOfGrid.setHs(hs);
		}else {
			arcgisInfoOfGrid.setHs("");
		}
		arcgisInfoOfGrid.setAreaColor("#ff0000");
		arcgisInfoOfGrid.setNameColor("#ff0000");
		arcgisInfoOfGrid.setLineColor("#ff0000");
		arcgisInfoOfGrid.setLineWidth(1);
		boolean flag = this.arcgisInfoService
				.saveArcgisDrawAreaOfGrid(arcgisInfoOfGrid);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		return result;
	}

	@ResponseBody
	@RequestMapping(value="/updateMapDataOfUrbanParts")
	public Map<String,Object> updateMapDataOfUrbanParts(HttpSession session, HttpServletRequest request, ModelMap map,
		  @RequestParam(value = "wid") Long wid,
		  @RequestParam(value= "mapt") String mapType,
		  @RequestParam(value= "bizType") String bizType,
		  @RequestParam(value= "x",required = false) String x,
		  @RequestParam(value= "y",required = false) String y
	) {
		ResMarker resMarker = new ResMarker();
		if(wid != null){
			resMarker.setResourcesId(wid);
		}
		if(StringUtils.isNotBlank(mapType)){
			resMarker.setMapType(mapType);
		}
		if(StringUtils.isNotBlank(bizType)){
			resMarker.setMarkerType(bizType);
		}
		if(StringUtils.isNotBlank(x) && !"undefined".equals(x) && !"NaN".equals(x)){
			resMarker.setX(x);
		}else{
			resMarker.setX(null);
		}
		if(StringUtils.isNotBlank(y) && !"undefined".equals(y) && !"NaN".equals(y)){
			resMarker.setY(y);
		}else{
			resMarker.setY(null);
		}
		//resMarkerService.deleteResMarker(bizType, wid, mapType);

		resMarker.setCatalog("02");
		boolean flag = resMarkerService.saveOrUpdateResMarker(resMarker);

		Map<String,Object> result = new HashMap<String,Object>();
		result.put("flag", flag);
		return result;
	}

	/**
	 * 批量删除综治.市政设施（资源）
	 *
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delUrbanParts", method = RequestMethod.POST)
	public Map<String, Object> delUrbanParts(HttpSession session, HttpServletRequest request,
			 @RequestParam(value = "idStr") String idStr,
			 @RequestParam(value = "bizType") String bizType) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String[] ids = idStr.split(",");
		List<Long> idList = new ArrayList<Long>();
		Map<String, Object> deleteResult = new HashMap<String, Object>();
		for (int i = 0; i < ids.length; i++) {
			try {
				long recordId = Long.parseLong(ids[i]);
				idList.add(recordId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if("02010601".equals(bizType)){
			int rows = fireResourceService.deleteByIds(idStr, userInfo);
			resultMap.put("result",rows);
		}else{
			boolean result = resInfoService.deleteResInfoById(userInfo.getUserId(), idList);

			for (Long id : idList) {
				ResInfo resInfo = resInfoService.findResInfoById(id);
				if (resInfo != null) {
					//同步删除事件资源
					Map<String, Object> params = new HashMap<String, Object>();

					ResType resType = resInfoService.findResTypeById(resInfo.getResTypeId());
					if (resType.getTypeCode() != null && !resType.getTypeCode().equals("")) {
						params.put("resTypeCode", resType.getTypeCode());
					}
					if (resInfo.getUpdateUser() != null) {
						params.put("updateUserId", resInfo.getUpdateUser());
					}
					if (resInfo.getResId() != null) {
						params.put("resTableId", resInfo.getResId());
					}
					deleteResult = zgResourceInfoService.delete(params);
				}
			}

			resultMap.put("result",result ?idList.size():0L);
		}

		return resultMap;
	}

	/**
	 * 根据市政设施的定位信息
	 * @param session
	 * @param request
	 * @param map
	 * @param wid
	 * @param mapt
	 * @param bizType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfUrbanParts")
	public Map<String,Object> getArcgisDataOfUrbanParts(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="wid") Long wid
			,@RequestParam(value="mapt") Integer mapt
			,@RequestParam(value="bizType") String bizType
			,@RequestParam(value="subBizType", required = false) String subBizType) {
		Map<String,Object> result = new HashMap<String, Object>();
		String markerType = ConstantValue.RES_DEFAULT;
		if(StringUtils.isNotBlank(bizType)){
			if(bizType.equals(ConstantValue.GLOBAL_EYE_TYPE_CODE)){
				markerType = ConstantValue.MARKER_TYPE_GLOBAL_EYES;
			}else if(bizType.equals(ConstantValue.HIRE_HYDRANT_TYPE_CODE)){
				if(StringUtils.isNotBlank(subBizType)){
					markerType = getMarkerType(subBizType);
				}
			}else{
				markerType = bizType;
			}

		}
		List<ArcgisInfoOfPublic> resultList = this.arcgisInfoService.getArcgisDataOfMarkerType(wid, markerType, "02", mapt);
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:resultList) {
			if(wid.equals(arcgisInfoOfPublic.getWid()) ) {
				list.add(arcgisInfoOfPublic);
				result.put("list", list);
			}
		}
		if((list == null || list.size()<=0) && resultList.size()>0) {
			resultList.get(0).setHs("");
			list.add(resultList.get(0));
			result.put("list", list);
		}
		return result;
	}

	private String getMarkerType(String catalog) {
		if (StringUtils.isNotBlank(catalog)) {
			if (catalog.equals("1")) {
				return ConstantValue.MARKER_TYPE_FIRE_SN;
			} else if (catalog.equals("2")) {
				return ConstantValue.MARKER_TYPE_FIRE_SW;
			} else if (catalog.equals("3")) {
				return ConstantValue.MARKER_TYPE_FIRE_TR;
			} else if (catalog.equals("4")) {
				return ConstantValue.MARKER_TYPE_FIRE_ZL;
			} else if (catalog.equals("5")) {
				return ConstantValue.MARKER_TYPE_FIRE_PL;
			}
		}
		return null;
	}

	/**
	 * 执法仪轨迹数据查询
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchGpsDataOfEnforceRecorder4Jsonp")
	public String fetchGpsDataOfEnforceRecorder4Jsonp(HttpSession session,
													  HttpServletRequest request, ModelMap map,
													  @RequestParam(value = "bizType", required = false) String bizType,
													  @RequestParam(value = "locateTimeBegin", required = false) String locateTimeBegin,
													  @RequestParam(value = "locateTimeEnd", required = false) String locateTimeEnd,
													  @RequestParam(value = "resno") String resno,
													  @RequestParam(value = "mapt", required = false) String mapt) {

		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		String trackDate = request.getParameter("trackDate");
		params.put("trackDate", trackDate);
		if(StringUtils.isBlank(bizType)){ //默认查01 执法仪记录
			params.put("bizType","01");
		}
		if (StringUtils.isNotBlank(locateTimeBegin)) {
			params.put("locateTimeBegin", locateTimeBegin);
		}
		if(StringUtils.isNotBlank(locateTimeEnd)){
			params.put("locateTimeEnd",locateTimeEnd);
		}
		params.put("resno",resno);
		if(StringUtils.isNotBlank(mapt)){
			params.put("mapt",mapt);
		}
		List<Map<String, Object>> points = deviceGpsDataService.searchGpsDataList(params);
		result.put("list", points);
		String jsonpcallback = request.getParameter("jsonpcallback");
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(result) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(result);
		}

		return jsonpcallback;
	}

	/**
	 * 获取执法仪最新定位数据
	 * @param session
	 * @param request
	 * @param resno
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectOneLastestGpsData4Jsonp")
	public String selectOneLastestGpsData4Jsonp(HttpSession session,
											    HttpServletRequest request,
											    @RequestParam(value = "resno") String resno,
												@RequestParam(value = "mapt") String mapt,
												@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<Map<String, Object>> gpsData = deviceGpsDataService.fetchEnForceRecorderLatestLocateDataListByResno(resno,mapt);
		for(Map<String, Object> map:gpsData){
			map.put("elementsCollectionStr",elementsCollectionStr);
		}
		String jsonpcallback = request.getParameter("jsonpcallback");
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(gpsData) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(gpsData);
		}
		return jsonpcallback;
	}


	/**
	 * 获取多个执法仪最新定位数据
	 * @param session
	 * @param request
	 * @param resIds 多个执法仪资源id，逗号分隔
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectMoreLastestGpsData4Jsonp")
	public String selectMoreLastestGpsData4Jsonp(HttpSession session,
												HttpServletRequest request,
												@RequestParam(value = "resIds") String resIds,
												@RequestParam(value = "mapt") String mapt,
												@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		Map<String,Object> params = new HashMap<>();
		if(StringUtils.isNotBlank(resIds) && resIds.contains(",")) {
			params.put("resIdArray", resIds.split(","));
		}else{
			params.put("resId", resIds);
		}
		params.put("mapt",mapt);
		List<Map<String, Object>> gpsData = deviceGpsDataService.fetchMoreEnForceRecorderLatestLocateDataList(params);
		
		StringBuffer indexCodesBuf = new StringBuffer();
		Map<String,Map<String, Object>> statusMap = new HashMap<>();
		for(Map<String, Object> map:gpsData){
			if (CommonFunctions.isNotBlank(map,"bizCode")){
				indexCodesBuf.append(map.get("bizCode").toString()).append(",");
				statusMap.put(map.get("bizCode").toString(),map);
			}
		}
		indexCodesBuf.deleteCharAt(indexCodesBuf.lastIndexOf(","));
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		//获取状态的接口能取到数据，状态就以此为准，以保证gps未推送也能实时获取最新状态。
		if(userInfo!=null && StringUtils.isNotBlank(indexCodesBuf.toString())){
			String resultJson = enforceRecorderService.fetchEnforceRecorderStatus(indexCodesBuf.toString(), userInfo.getOrgCode());
			if (StringUtils.isNotBlank(resultJson)) {
				Map jsonMap = JsonHelper.getMap(resultJson);
				if (CommonFunctions.isNotBlank(jsonMap,"code") && "0".equals(jsonMap.get("code"))) {
					JSONObject dataJson = (JSONObject) jsonMap.get("data");
					if (dataJson.containsKey("list")) {
						JSONArray dataArr = (JSONArray)dataJson.get("list");
						if (dataArr != null && !dataArr.isEmpty()) {
							for (Iterator iter1 = dataArr.iterator(); iter1.hasNext();) {
								JSONObject statusObj = (JSONObject) iter1.next();
								if (statusMap.containsKey(statusObj.get("indexCode").toString())) {
									Map<String, Object> gpsMap = statusMap.get(statusObj.get("indexCode").toString());
									if("1".equals(statusObj.get("online").toString())){
										gpsMap.put("onlineStatus","-1");
									} else if("0".equals(statusObj.get("online").toString())){
										gpsMap.put("onlineStatus","0");
									}
								}
							}
						}
					}
				}
			}
		}
		
		for(Map<String, Object> map:gpsData){
			map.put("bizType","enforceRecorder");
			if (CommonFunctions.isNotBlank(map,"onlineStatus") && Integer.valueOf(map.get("onlineStatus").toString())< 0) { //小于0 在线，否则离线。
				map.put("elementsCollectionStr",elementsCollectionStr);
			}else{
				map.put("elementsCollectionStr",elementsCollectionStr.replaceAll("enforceRecorder.png","offline_enforceRecorder.png"));
			}
		}
		String jsonpcallback = request.getParameter("jsonpcallback");
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(gpsData) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(gpsData);
		}
		return jsonpcallback;
	}

}
