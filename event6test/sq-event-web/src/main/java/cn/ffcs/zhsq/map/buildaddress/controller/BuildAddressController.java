package cn.ffcs.zhsq.map.buildaddress.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.GisBuildingInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IGisBuildingInfoService;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.map.buildaddress.service.IBuildAddressService;
import cn.ffcs.zhsq.map.thresholdcolorcfg.service.IThresholdColorCfgService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
import cn.ffcs.zhsq.mybatis.domain.map.buildaddress.BuildAddress;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;


@Controller
@RequestMapping(value="/zhsq/map/buildaddress/buildAddress")
public class BuildAddressController  extends ZZBaseController{
	@Autowired
	private IThresholdColorCfgService thresholdColorCfgService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private IGisBuildingInfoService gisBuildingInfoService;
	@Autowired
	private IBuildAddressService buildAddressService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IArcgisInfoService arcgisInfoService;
	/**
	 * 2014-10-13 liushi add
	 * 链接到图层菜单管理页面
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/index")
	public String index(HttpSession session, HttpServletRequest request, ModelMap map){
		String forward = "/map/buildaddress/index_buildaddress.ftl";
		Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
		String orgCode = (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		String gridName = (String)defaultOrgInfo.get(KEY_START_GRID_NAME);
		Long gridId = (Long)defaultOrgInfo.get(KEY_START_GRID_ID);
		map.put("orgCode", orgCode);
		map.put("gridName", gridName);
		map.put("gridId", gridId);
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.put("gridId", gridInfo.getGridId());
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		String OUTLINE_FONT_SETTINGS = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		String ARCGIS_DOCK_MODE = this.funConfigurationService.changeCodeToValue("ARCGIS_DOCK_MODE", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		
		String OUTLINE_FONT_SETTINGS_BUILD = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS_BUILD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		if(ARCGIS_DOCK_MODE == null || "".equals(ARCGIS_DOCK_MODE)) {
			ARCGIS_DOCK_MODE = "0";
		}
		map.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
		map.put("OUTLINE_FONT_SETTINGS_BUILD", OUTLINE_FONT_SETTINGS_BUILD);
		map.put("ARCGIS_DOCK_MODE", ARCGIS_DOCK_MODE);
		return forward;
	}
	@RequestMapping(value="/cswfs")
	public String cswfs(HttpSession session, HttpServletRequest request, ModelMap map){
		String forward = "/map/buildaddress/index_cswfs.ftl";
		Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
		String orgCode = (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		String gridName = (String)defaultOrgInfo.get(KEY_START_GRID_NAME);
		Long gridId = (Long)defaultOrgInfo.get(KEY_START_GRID_ID);
		map.put("orgCode", orgCode);
		map.put("gridName", gridName);
		map.put("gridId", gridId);
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.put("gridId", gridInfo.getGridId());
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		String OUTLINE_FONT_SETTINGS = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		String ARCGIS_DOCK_MODE = this.funConfigurationService.changeCodeToValue("ARCGIS_DOCK_MODE", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		
		String OUTLINE_FONT_SETTINGS_BUILD = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS_BUILD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		if(ARCGIS_DOCK_MODE == null || "".equals(ARCGIS_DOCK_MODE)) {
			ARCGIS_DOCK_MODE = "0";
		}
		map.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
		map.put("OUTLINE_FONT_SETTINGS_BUILD", OUTLINE_FONT_SETTINGS_BUILD);
		map.put("ARCGIS_DOCK_MODE", ARCGIS_DOCK_MODE);
		return forward;
	}
	@RequestMapping(value="/blindBuildAddressIndex")
	public String blindBuildAddressIndex(HttpSession session, HttpServletRequest request, ModelMap map){
		String forward = "/map/buildaddress/list_buildAddress.ftl";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String defaultOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE)
				.toString();
		//获取“是否启用三维地图”的功能配置
		String isUseTwoTypesMap = "";
		isUseTwoTypesMap =  funConfigurationService.
				turnCodeToValue(ConstantValue.IS_USE_TWO_TYPES_MAP,"",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUseTwoTypesMap", isUseTwoTypesMap);
		map.addAttribute("defaultInfoOrgCode", defaultOrgCode);
		map.addAttribute("startGridName",
				defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		String OUTLINE_FONT_SETTINGS = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		
		String OUTLINE_FONT_SETTINGS_BUILD = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS_BUILD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		map.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
		map.put("OUTLINE_FONT_SETTINGS_BUILD", OUTLINE_FONT_SETTINGS_BUILD);
		return forward;
	}
	
	@RequestMapping(value="/olwfs")
	public String olwfs(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "buildingId",required = false) String buildingIdStr,
			@RequestParam(value = "gridId",required = false)Long gridId){
		String forward = "";
		if(StringUtils.isNotBlank(buildingIdStr)){
			Long buildingId = Long.parseLong(buildingIdStr);
			forward = "/map/buildaddress/index_olwfs_single.ftl";
			GisBuildingInfo gisBuildingInfo = gisBuildingInfoService.findGisBuildingInfoById(buildingId);
			if(gisBuildingInfo != null && gisBuildingInfo.getGridId() != null){
				map.put("showGridId", gisBuildingInfo.getGridId());
			}
			map.put("wid", buildingId);
			
		} else {
			forward = "/map/buildaddress/index_olwfs.ftl";
		}
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long startGridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		//​获取指定网格地图信息
		if(gridId==null){
			gridId = startGridId;
		}
		List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,5);
		if(list != null && list.size()>0){
			for (ArcgisInfoOfGrid arcgisInfoOfGrid : list) {
				if(arcgisInfoOfGrid.getX()!=null){
					Double mapCenterX = arcgisInfoOfGrid.getX();
					map.put("mapCenterX", mapCenterX.toString());
				}
				if(arcgisInfoOfGrid.getY()!=null){
					map.put("mapCenterY", arcgisInfoOfGrid.getY().toString());
				}
			}
		}
		map.put("ZZGRID_DOMAIN", App.ZZGRID.getDomain(session));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		String OUTLINE_FONT_SETTINGS = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		
		String OUTLINE_FONT_SETTINGS_BUILD = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS_BUILD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		//楼宇轮廓绑定界面上的“辅助显示”功能是否显示
		String BUILDING_BIND_AID_DISPLAY = 
					this.funConfigurationService.turnCodeToValue("BUILDING_BIND_AID_DISPLAY", "", 
																	IFunConfigurationService.CFG_TYPE_FACT_VAL, 
																	userInfo.getOrgCode(), 
																	IFunConfigurationService.CFG_ORG_TYPE_0, 
																	IFunConfigurationService.DIRECTION_UP);
		if(BUILDING_BIND_AID_DISPLAY == null || "".equals(BUILDING_BIND_AID_DISPLAY)) {
			BUILDING_BIND_AID_DISPLAY = "1";
		}
		map.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
		map.put("OUTLINE_FONT_SETTINGS_BUILD", OUTLINE_FONT_SETTINGS_BUILD);
		map.put("BUILDING_BIND_AID_DISPLAY", BUILDING_BIND_AID_DISPLAY);
		return forward;
	}
	
	/**
	 * 2015-09-25 liushi add 根据信息域查询列表
	 * @param session
	 * @param request
	 * @param map
	 * @param pageNo
	 * @param pageSize
	 * @param orgCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/buildingListData")
	public EUDGPagination buildingListData(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows,
			@RequestParam(required = false) String buildingName,
			@RequestParam(required = false) String isRelationValue
			){
		Map<String,Object> mixedGridInfo = this.getDefaultGridInfo(session);
		if(page<=0) page=1;
		if(buildingName!=null) buildingName = buildingName.trim();
		if(isRelationValue!=null) isRelationValue = isRelationValue.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("infoOrgCode", mixedGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		params.put("buildingName", buildingName);
		params.put("isRelationValue", isRelationValue);
		EUDGPagination pagination = this.buildAddressService.findBuildAddressPagination(page, rows, params);
		return pagination;
	}
	
	/**
	 * 2015-09-25 liushi add 新增
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/add")
	public String add(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="relateType") String relateType
			,@RequestParam(value="addressId", required = false) Long addressId
			,@RequestParam(value="mapt", required = false) Integer mapt
			,@RequestParam(value="x", required = false) Double x
			,@RequestParam(value="y", required = false) Double y
			,@RequestParam(value="hs", required = false) String hs){
		map.put("relateType", relateType);
		map.put("addressId", addressId);
		map.put("mapt", mapt);
		map.put("x", x);
		map.put("y", y);
		map.put("hs", hs);
		String forward = "/map/buildaddress/add_buildAddress.ftl";
		return forward;
	}
	
	
	/**
	 * 2015-09-25 liushi add 保存（包含新增跟修改功能）
	 * @param session
	 * @param request
	 * @param map
	 * @param thresholdColorCfg
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public Map<String,Object> save(HttpSession session, HttpServletRequest request, ModelMap map,
			BuildAddress buildAddress
			){
		
		boolean flag = this.buildAddressService.saveBuildAddress(buildAddress);
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("flag", flag);
		return result;
	}
	
	/**
	 * <p>Description:单条楼宇轮廓编辑跳转</p>
	 * @param session
	 * @param request
	 * @param map
	 * @param buildingId
	 * @return
	 * add sulch
	 * 2016年4月11日 下午4:50:46
	 */
	@RequestMapping(value="/singleBuildingDrawIndex")
	public String singleBuildingDrawIndex(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="buildingId",required = false) Long buildingId){
		String forward = "/map/buildaddress/index_single_building_draw.ftl";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String infoOrgCode = userInfo.getInfoOrgCodeStr();
		if(StringUtils.isNotBlank(infoOrgCode)){
			if(infoOrgCode.startsWith(ConstantValue.JINJIANG_FUNC_ORG_CODE)){//"350582"
				map.put("targetURL", "/zhsq/map/buildaddress/buildAddress/olwfs.jhtml?buildingId="+buildingId);
			}else{
				map.put("targetURL", "/zhsq/map/arcgis/arcgisdata/toArcgisDrawAreaPanel.jhtml?targetType=build&wid="+buildingId);
			}
		}
		return forward;
	}
	
	/**
	 * <p>Description:楼宇批量编辑跳转</p>
	 * @param session
	 * @param request
	 * @param map
	 * @param buildingId
	 * @return
	 * add sulch
	 * 2016年4月11日 下午4:51:31
	 */
	@RequestMapping(value="/batchBuildingDrawIndex")
	public String batchBuildingDrawIndex(HttpSession session, HttpServletRequest request, ModelMap map){
		String forward = "/map/buildaddress/index_batch_building_draw.ftl";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String infoOrgCode = userInfo.getInfoOrgCodeStr();
		if(StringUtils.isNotBlank(infoOrgCode)){
			if(infoOrgCode.startsWith(ConstantValue.JINJIANG_FUNC_ORG_CODE)){
				map.put("targetURL", "/zhsq/map/buildaddress/buildAddress/blindBuildAddressIndex.jhtml");
			}else{
				map.put("targetURL", "/zhsq/drowBuildingController/index.jhtml");
			}
		}
		return forward;
	}
	
	

	@RequestMapping(value="/spGisBlindBuildIndex")
	public String spGisBlindBuildIndex(HttpSession session, HttpServletRequest request, ModelMap map){
		String forward = "/map/buildaddress/list_spgis_building.ftl";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String defaultOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE)
				.toString();
		//获取“是否启用三维地图”的功能配置
		String isUseTwoTypesMap = "";
		isUseTwoTypesMap =  funConfigurationService.
				turnCodeToValue(ConstantValue.IS_USE_TWO_TYPES_MAP,"",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUseTwoTypesMap", isUseTwoTypesMap);
		map.addAttribute("defaultInfoOrgCode", defaultOrgCode);
		map.addAttribute("startGridName",
				defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		String OUTLINE_FONT_SETTINGS = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		
		String OUTLINE_FONT_SETTINGS_BUILD = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS_BUILD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		map.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
		map.put("OUTLINE_FONT_SETTINGS_BUILD", OUTLINE_FONT_SETTINGS_BUILD);
		return forward;
	}
	
	@RequestMapping(value="/spgisMap")
	public String spgisMap(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "buildingId",required = false) String buildingIdStr,
			@RequestParam(value = "gridId",required = false)Long gridId){
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String startOrgCode = (String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		String forward = "";
		if(StringUtils.isNotBlank(buildingIdStr)){
			Long buildingId = Long.parseLong(buildingIdStr);
			forward = "/map/buildaddress/index_pingtan_ol.ftl";
			GisBuildingInfo gisBuildingInfo = gisBuildingInfoService.findGisBuildingInfoById(buildingId);
			if(gisBuildingInfo != null && gisBuildingInfo.getGridId() != null){
				map.put("showGridId", gisBuildingInfo.getGridId());
			}
			map.put("wid", buildingId);
			
		} else {
			forward = "/map/buildaddress/index_pingtan_ol.ftl";
		}
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Long startGridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		//​获取指定网格地图信息
		if(gridId==null){
			gridId = startGridId;
		}
		map.put("gridId", gridId);
		List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,5);
		if(list != null && list.size()>0){
			for (ArcgisInfoOfGrid arcgisInfoOfGrid : list) {
				if(arcgisInfoOfGrid.getX()!=null){
					Double mapCenterX = arcgisInfoOfGrid.getX();
					map.put("mapCenterX", mapCenterX.toString());
				}
				if(arcgisInfoOfGrid.getY()!=null){
					map.put("mapCenterY", arcgisInfoOfGrid.getY().toString());
				}
			}
		}
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		String OUTLINE_FONT_SETTINGS = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		
		String OUTLINE_FONT_SETTINGS_BUILD = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS_BUILD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		//楼宇轮廓绑定界面上的“辅助显示”功能是否显示
		String BUILDING_BIND_AID_DISPLAY = 
					this.funConfigurationService.turnCodeToValue("BUILDING_BIND_AID_DISPLAY", "", 
																	IFunConfigurationService.CFG_TYPE_FACT_VAL, 
																	userInfo.getOrgCode(), 
																	IFunConfigurationService.CFG_ORG_TYPE_0, 
																	IFunConfigurationService.DIRECTION_UP);
		if(BUILDING_BIND_AID_DISPLAY == null || "".equals(BUILDING_BIND_AID_DISPLAY)) {
			BUILDING_BIND_AID_DISPLAY = "1";
		}
		map.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
		map.put("OUTLINE_FONT_SETTINGS_BUILD", OUTLINE_FONT_SETTINGS_BUILD);
		map.put("BUILDING_BIND_AID_DISPLAY", BUILDING_BIND_AID_DISPLAY);
		map.put("gridId", gridInfo.getGridId());
		map.put("gridCode", gridInfo.getGridCode());
		map.put("gridName", gridInfo.getGridName());
		map.put("gridLevel", gridInfo.getGridLevel());
		map.put("orgCode", startOrgCode);
		map.put("socialOrgCode", userInfo.getOrgCode());
		return forward;
	}
}
