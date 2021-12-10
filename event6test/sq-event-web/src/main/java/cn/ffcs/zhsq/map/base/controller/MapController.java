package cn.ffcs.zhsq.map.base.controller;

import cn.ffcs.geo.xieJingAddress.service.IXieJingAddressService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IGisStatConfigService;
import cn.ffcs.zhsq.map.arcgis.service.IMigrationMapService;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.*;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 2014-05-16 liushi add
 * arcgis地图加载控制器
 * @author liushi
 *
 */
@Controller
@RequestMapping(value={"/zhsq/map"})
public class MapController extends ZZBaseController {
	
	@Autowired
	private IArcgisInfoService arcgisInfoService;
	@Autowired
	private IMenuConfigService menuConfigService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	private IMigrationMapService migrationMapService;
	@Autowired
    private IResMarkerService resMarkerService; //地图标注服务
	@Autowired
	private IGisStatConfigService gisStatConfigService;
	@Autowired
	private IXieJingAddressService xieJingAddressService;

	/**
	 * 地图首页(新版)
	 *
	 * @param session
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/index")
	public String index(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception{
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String forward = "/map/base/map_base_index.ftl";

		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");

		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String startOrgCode = (String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("GMIS_DOMAIN", App.GMIS.getDomain(session));
		map.put("POPULATION_URL", App.RS.getDomain(session));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.put("OAUTH_URL", App.UAM.getDomain(session));
		map.put("WATER_MONITOR_URL", App.WATER.getDomain(session));

		map.put("gridId", gridInfo.getGridId());
		map.put("gridCode", gridInfo.getGridCode());
		map.put("gridName", gridInfo.getGridName());
		map.put("gridLevel", gridInfo.getGridLevel());
		map.put("orgCode", startOrgCode);
		map.put("socialOrgCode", userInfo.getOrgCode());
		map.put("userName", userInfo.getUserName());
		response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragrma","no-cache");
		response.setDateHeader("Expires",0);
		return forward;
	}

	/**
	 * 地图可配置首页(新版)
	 *
	 * @param session
	 * @param request
	 * @param response
	 * @param map
	 * @param homePageType ARCGIS_STANDARD_HOME
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toMapOfConfigIndex")
	public String toMapOfConfigIndex(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="homePageType", required=false) String homePageType) throws Exception{
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String forward = "/map/base/map_config_index.ftl";
		String fillOpacity = request.getParameter("fillOpacity");
		String menuCode = request.getParameter("menuCode");

		if (StringUtils.isNotBlank(fillOpacity)) {
			request.setAttribute("fillOpacity", fillOpacity);
		}
		if (StringUtils.isNotBlank(menuCode)) {
			request.setAttribute("menuCode", menuCode);
		}
		request.setAttribute("BI_REPORT", App.BI.getDomain(session));
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");

		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String startOrgCode = (String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("GMIS_DOMAIN", App.GMIS.getDomain(session));
		map.put("POPULATION_URL", App.RS.getDomain(session));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.put("OAUTH_URL", App.UAM.getDomain(session));
		map.put("WATER_MONITOR_URL", App.WATER.getDomain(session));

		//是否自动清除图层
		String AUTOMATIC_CLEAR_MAP_LAYER = this.funConfigurationService.turnCodeToValue("AUTOMATIC_CLEAR_MAP_LAYER", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		//影像图是否显示楼宇轮廓
		//String IS_IMAGE_MAP_SHOW_CONTOUR = this.funConfigurationService.turnCodeToValue("ARCGIS_SHOW_CONTOUR", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		//影像图是否显示楼宇轮廓
		String IS_IMAGE_MAP_SHOW_CONTOUR = this.funConfigurationService.turnCodeToValue("ARCGIS_SHOW_CONTOUR", "IMAGE", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		//影像图是否显示楼宇轮廓
		String IS_VECTOR_MAP_SHOW_CONTOUR = this.funConfigurationService.turnCodeToValue("ARCGIS_SHOW_CONTOUR", "VECTOR", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		//网格轮廓是否显示中心点图标
		String IS_GRID_ARCGIS_SHOW_CENTER_POINT = this.funConfigurationService.turnCodeToValue("ARCGIS_SHOW_CENTER_POINT", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		//广东乐从镇信息域code
		String LC_INFO_ORG_CODE = this.funConfigurationService.turnCodeToValue("LC_INFO_ORG_CODE", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		//网格轮廓字体设置
		String OUTLINE_FONT_SETTINGS = this.funConfigurationService.turnCodeToValue("OUTLINE_FONT_SETTINGS", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		//楼宇轮廓字体设置
		String OUTLINE_FONT_SETTINGS_BUILD = this.funConfigurationService.turnCodeToValue("OUTLINE_FONT_SETTINGS_BUILD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		//
		String IS_ACCUMULATION_LAYER = this.funConfigurationService.turnCodeToValue("IS_ACCUMULATION_LAYER", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		String ARCGIS_DOCK_MODE = this.funConfigurationService.turnCodeToValue("ARCGIS_DOCK_MODE", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		String arcgisFactorUrl = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "SAFTY_TEAM_FACTOR_URL",IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		//楼宇中心点图标样式
		String CENTER_POINT_SETTINGS_BUILD = this.funConfigurationService.turnCodeToValue("CENTER_POINT_SETTINGS_BUILD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);

		//网格各层级显示中心点名称的地图层级设置
		String MAP_LEVEL_TRIG_CONDITION_PROVINCE = this.funConfigurationService.turnCodeToValue(ConstantValue.SHOW_WG_POINT_MAP_LEVEL_CODE, ConstantValue.MAP_LEVEL_TRIG_CONDITION_PROVINCE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		String MAP_LEVEL_TRIG_CONDITION_CITY = this.funConfigurationService.turnCodeToValue(ConstantValue.SHOW_WG_POINT_MAP_LEVEL_CODE, ConstantValue.MAP_LEVEL_TRIG_CONDITION_CITY, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		String MAP_LEVEL_TRIG_CONDITION_COUNTY = this.funConfigurationService.turnCodeToValue(ConstantValue.SHOW_WG_POINT_MAP_LEVEL_CODE, ConstantValue.MAP_LEVEL_TRIG_CONDITION_COUNTY, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		String MAP_LEVEL_TRIG_CONDITION_STREET = this.funConfigurationService.turnCodeToValue(ConstantValue.SHOW_WG_POINT_MAP_LEVEL_CODE, ConstantValue.MAP_LEVEL_TRIG_CONDITION_STREET, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		String MAP_LEVEL_TRIG_CONDITION_COMMUNITY = this.funConfigurationService.turnCodeToValue(ConstantValue.SHOW_WG_POINT_MAP_LEVEL_CODE, ConstantValue.MAP_LEVEL_TRIG_CONDITION_COMMUNITY, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		String MAP_LEVEL_TRIG_CONDITION_GRID = this.funConfigurationService.turnCodeToValue(ConstantValue.SHOW_WG_POINT_MAP_LEVEL_CODE, ConstantValue.MAP_LEVEL_TRIG_CONDITION_GRID, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(MAP_LEVEL_TRIG_CONDITION_PROVINCE)){
			map.put("MAP_LEVEL_TRIG_CONDITION_PROVINCE", MAP_LEVEL_TRIG_CONDITION_PROVINCE);
		}
		if(StringUtils.isNotBlank(MAP_LEVEL_TRIG_CONDITION_CITY)){
			map.put("MAP_LEVEL_TRIG_CONDITION_CITY", MAP_LEVEL_TRIG_CONDITION_CITY);
		}
		if(StringUtils.isNotBlank(MAP_LEVEL_TRIG_CONDITION_COUNTY)){
			map.put("MAP_LEVEL_TRIG_CONDITION_COUNTY", MAP_LEVEL_TRIG_CONDITION_COUNTY);
		}
		if(StringUtils.isNotBlank(MAP_LEVEL_TRIG_CONDITION_STREET)){
			map.put("MAP_LEVEL_TRIG_CONDITION_STREET", MAP_LEVEL_TRIG_CONDITION_STREET);
		}
		if(StringUtils.isNotBlank(MAP_LEVEL_TRIG_CONDITION_COMMUNITY)){
			map.put("MAP_LEVEL_TRIG_CONDITION_COMMUNITY", MAP_LEVEL_TRIG_CONDITION_COMMUNITY);
		}
		if(StringUtils.isNotBlank(MAP_LEVEL_TRIG_CONDITION_GRID)){
			map.put("MAP_LEVEL_TRIG_CONDITION_GRID", MAP_LEVEL_TRIG_CONDITION_GRID);
		}


		//人脸识别调用方法获取
		String SURVEILLANCE_MSG_METHOD = this.funConfigurationService.turnCodeToValue("SURVEILLANCE_MSG_METHOD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(SURVEILLANCE_MSG_METHOD)){
			map.put("SURVEILLANCE_MSG_METHOD", SURVEILLANCE_MSG_METHOD);
		}


		if(AUTOMATIC_CLEAR_MAP_LAYER == null || "".equals(AUTOMATIC_CLEAR_MAP_LAYER)) {
			AUTOMATIC_CLEAR_MAP_LAYER = "1";
		}
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		if(IS_IMAGE_MAP_SHOW_CONTOUR == null || "".equals(IS_IMAGE_MAP_SHOW_CONTOUR)) {
			IS_IMAGE_MAP_SHOW_CONTOUR = "0";
		}
		if(IS_VECTOR_MAP_SHOW_CONTOUR == null || "".equals(IS_VECTOR_MAP_SHOW_CONTOUR)) {
			IS_VECTOR_MAP_SHOW_CONTOUR = "0";
		}
		if(IS_GRID_ARCGIS_SHOW_CENTER_POINT == null || "".equals(IS_GRID_ARCGIS_SHOW_CENTER_POINT)) {
			IS_GRID_ARCGIS_SHOW_CENTER_POINT = "0";
		}
		if(LC_INFO_ORG_CODE == null || "".equals(LC_INFO_ORG_CODE)) {
			LC_INFO_ORG_CODE = "010";
		}
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		if(IS_ACCUMULATION_LAYER == null || "".equals(IS_ACCUMULATION_LAYER)) {
			IS_ACCUMULATION_LAYER = "0";
		}
		if(ARCGIS_DOCK_MODE == null || "".equals(ARCGIS_DOCK_MODE)) {
			ARCGIS_DOCK_MODE = "0";
		}
		//楼宇中心点图标样式--默认图标
		if(CENTER_POINT_SETTINGS_BUILD == null || "".equals(CENTER_POINT_SETTINGS_BUILD)) {
			CENTER_POINT_SETTINGS_BUILD = "build_locate_point.png";
		}
		List<String> gridLevelNames = CommonFunctions.fetchGridLevelNames(userInfo.getOrgCode());
		//获取框选配置是否启用
		GisStatConfig gisStatConfig = new GisStatConfig();
		gisStatConfig = gisStatConfigService.getGisStatConfig("0", homePageType, gridInfo.getInfoOrgCode());
		if(gisStatConfig != null){
			map.put("kuangxuanFlag","yes");
		}
		if (gridLevelNames != null) {//地图显示XX轮廓配置
			map.put("DIVISIONS_LEVEL_NAME_CFG", gridLevelNames);
		}

		//甘肃
		if(userInfo != null && StringUtils.isNotBlank(userInfo.getOrgCode()) && userInfo.getOrgCode().startsWith(ConstantValue.GANSU_FUNC_ORG_CODE)){
			map.put("isGansu", "yes");
		}
		map.put("AUTOMATIC_CLEAR_MAP_LAYER", AUTOMATIC_CLEAR_MAP_LAYER);
		map.put("IS_IMAGE_MAP_SHOW_CONTOUR", IS_IMAGE_MAP_SHOW_CONTOUR);
		map.put("IS_VECTOR_MAP_SHOW_CONTOUR", IS_VECTOR_MAP_SHOW_CONTOUR);
		map.put("IS_GRID_ARCGIS_SHOW_CENTER_POINT", IS_GRID_ARCGIS_SHOW_CENTER_POINT);
		map.put("LC_INFO_ORG_CODE", LC_INFO_ORG_CODE);
		map.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
		map.put("OUTLINE_FONT_SETTINGS_BUILD", OUTLINE_FONT_SETTINGS_BUILD);
		map.put("IS_ACCUMULATION_LAYER", IS_ACCUMULATION_LAYER);
		map.put("ARCGIS_DOCK_MODE", ARCGIS_DOCK_MODE);
		map.put("CENTER_POINT_SETTINGS_BUILD", CENTER_POINT_SETTINGS_BUILD);
		map.put("arcgisFactorUrl", arcgisFactorUrl);
		map.put("gridId", gridInfo.getGridId());
		map.put("gridCode", gridInfo.getGridCode());
		map.put("gridName", gridInfo.getGridName());
		map.put("gridLevel", gridInfo.getGridLevel());
		map.put("orgCode", startOrgCode);
		map.put("socialOrgCode", userInfo.getOrgCode());
		map.put("homePageType", homePageType);

		map.put("userName", userInfo.getUserName());
		map.put("svrip", ConstantValue.MMP_SVRIP);
		map.put("mediaurl", ConstantValue.MMP_MEDIAURL);
		//是否启用视频通话
		String IS_USER_MMP = this.funConfigurationService.turnCodeToValue(ConstantValue.IS_USER_MMP, null,IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUserMmp", IS_USER_MMP);
		map.addAttribute("RESOURCE_DOMAIN", App.RESOURCE.getDomain(session));
		if(userInfo.getOrgCode().startsWith(ConstantValue.LUOFANG_INFO_ORG_CODE)){
			map.addAttribute("LUO_FANG", "true");
		}
		String SHOW_CURRENT_GRID_LEVEL_OUTLINE = this.funConfigurationService.turnCodeToValue(ConstantValue.SHOW_CURRENT_GRID_LEVEL_OUTLINE, null, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isNotBlank(SHOW_CURRENT_GRID_LEVEL_OUTLINE)){
			map.put("SHOW_CURRENT_GRID_LEVEL_OUTLINE", SHOW_CURRENT_GRID_LEVEL_OUTLINE);
			List<Long> infoOrgIdList = new ArrayList<Long>();
			if(userInfo.getInfoOrgList()!=null && userInfo.getInfoOrgList().size()>0) {
				for(OrgEntityInfoBO org : userInfo.getInfoOrgList()) {
					infoOrgIdList.add(org.getOrgId());
				}
			}
			//-- 获取对应的网格根节点
			List<MixedGridInfo> list = mixedGridInfoService.getMixedGridMappingListByOrgIdList(infoOrgIdList, ConstantValue.ORG_TYPE_ALL);
			if(list != null && list.size()>0){
				String gridIds = "",infoOrgCodes = "";
				for(int i=0;i<list.size();i++){
					gridIds = gridIds + list.get(i).getGridId() + ",";
					infoOrgCodes = infoOrgCodes + list.get(i).getInfoOrgCode() + ",";
				}
				map.put("gridIds", gridIds);
				map.put("infoOrgCodes", infoOrgCodes);
				map.put("currentUserGridLevel", list.get(0).getGridLevel());
			}
		}
		response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragrma","no-cache");
		response.setDateHeader("Expires",0);
		return forward;
	}

	
	// 地图标注使用的地图维度（二维、三维）
	private final String MAP_TYPE_CODE = "MAP_TYPE_CODE";
	// 星云高德地图
	private final String SPGIS_MAP_ENGINE = "006";
	// 新地图
	private final String NEW_MAP_ENGINE = "005";
	// 旧地图
	private final String OLD_MAP_ENGINE = "004";
	// 二维
	private final String TWO_DIMENSION = "2";
	// 三维
	private final String THREE_DIMENSION = "3";
	// 新地图二维mapt
	private final String TWO_DIMENSION_MAPT_OF_NEWMAP = "5";
	// 新地图三维mapt
	private final String THREE_DIMENSION_MAPT_OF_NEWMAP = "30";
	// 旧地图二维mapt
	private final String TWO_DIMENSION_MAPT_OF_OLDMAP = "2";
	// 旧地图三维mapt
	private final String THREE_DIMENSION_MAPT_OF_OLDMAP = "20";
}
