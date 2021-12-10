package cn.ffcs.zhsq.map.arcgis.controller;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.base.BootstrapPagination;
import cn.ffcs.geo.mybatis.domain.geoStdAddress.GeoStdAddress;
import cn.ffcs.geo.xieJingAddress.service.IXieJingAddressService;
import cn.ffcs.gis.base.service.IResMarkerService;
import cn.ffcs.gis.mybatis.domain.base.ResMarker;
import cn.ffcs.oa.entity.OaCommunicatGroup;
import cn.ffcs.oa.entity.OaCommunicatPerson;
import cn.ffcs.oa.service.OaCommunicatGroupService;
import cn.ffcs.oa.service.OaCommunicatPersonService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.PositionInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.PositionInfoOutService;
import cn.ffcs.uam.service.UserInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IMigrationMapService;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisConfigInfo;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfTrajectory;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisMapConfigInfo;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.nanChang3D.service.INanChang3DService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.JsonUtils;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 2014-05-16 liushi add
 * arcgis地图加载控制器
 * @author liushi
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgis")
public class ArcgisController extends ZZBaseController {
	@Autowired
	private PositionInfoOutService positionInfoOutService;
	@Autowired
	private IArcgisInfoService arcgisInfoService;
	@Autowired
	private IMenuConfigService menuConfigService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IMigrationMapService migrationMapService;
	@Autowired
    private IResMarkerService resGisMarkerService; //地图标注服务
//	@Autowired
//	private IGisStatConfigService gisStatConfigService;
	@Autowired
	private IXieJingAddressService xieJingAddressService;
	@Autowired
	private UserManageOutService userManageOutService;
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	@Autowired
	private INanChang3DService nanChang3DService;
	@Autowired
	private OaCommunicatPersonService oaCommunicatPersonService;
	@Autowired
	private OaCommunicatGroupService oaCommunicatGroupService;
	
	@Autowired
    private UserInfoOutService userInfoService;//系统用户模块
	
	private static final String POSITION_NAME = "一键督察";	

	/**
	 * 2014-05-16 liushi add
	 * 获取当前网格信息以及对应的地图信息并转到对应的地图加载首页
	 * @param session
	 * @param request
	 * @param map
	 * @param arcgisUrlType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toMapArcgis")
	public String toMapArcgis(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="arcgisUrlType", required=false) String arcgisUrlType) throws Exception{
		String forward = "/map/arcgis/arcgis_base/arcgis_demo.ftl";
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		map.put("gridId", gridInfo.getGridId());
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		return forward;
	}
	/**
	 * 2015-02-03 liushi add 链接到福州相关首页
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toFuzhouArcgis")
	public String toFuzhouArcgis(HttpSession session, HttpServletRequest request, ModelMap map) throws Exception{
		String forward = "/map/arcgis/arcgis_base/arcgis_fuzhou/arcgis_fuzhou.ftl";
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		map.put("gridId", gridInfo.getGridId());
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.put("PEACE_REPORT_URL", App.PAJS.getDomain(session));
		return forward;
	}
	
	/**
	 * 2014-06-19 liushi add 
	 * 转到地图标准首页
	 * @param session
	 * @param request
	 * @param map
	 * @param arcgisUrlType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toMapArcgisOfStandard")
	public String toMapArcgisOfStandard(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="arcgisUrlType", required=false) String arcgisUrlType) throws Exception{
		//String forward = "/map/arcgis/arcgis_base/arcgis_standard_index.ftl";
		String forward = "/map/arcgis/arcgis_base/arcgis_config_index.ftl";
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String startOrgCode = (String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("POPULATION_URL", App.RS.getDomain(session));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.put("OAUTH_URL", App.UAM.getDomain(session));
		map.put("WATER_MONITOR_URL", App.WATER.getDomain(session));
		//FunConfigureSetting funConfigureSetting = funConfigurationService.f
		//List<FunConfigureSetting> funConfigureSettingList = findConfigureSettingLatestList(String funcCode, String trigCondition, String cfgValType, String orgCode, int direction);
		//是否自动清除图层
		String AUTOMATIC_CLEAR_MAP_LAYER = this.funConfigurationService.turnCodeToValue("AUTOMATIC_CLEAR_MAP_LAYER", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		//影像图是否显示楼宇轮廓
		String IS_IMAGE_MAP_SHOW_CONTOUR = this.funConfigurationService.turnCodeToValue("ARCGIS_SHOW_CONTOUR", "IMAGE", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		//影像图是否显示楼宇轮廓
		String IS_VECTOR_MAP_SHOW_CONTOUR = this.funConfigurationService.turnCodeToValue("ARCGIS_SHOW_CONTOUR", "VECTOR", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		
		String IS_GRID_ARCGIS_SHOW_CENTER_POINT = this.funConfigurationService.turnCodeToValue("ARCGIS_SHOW_CENTER_POINT", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		String OUTLINE_FONT_SETTINGS = this.funConfigurationService.turnCodeToValue("OUTLINE_FONT_SETTINGS", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		String LC_INFO_ORG_CODE = this.funConfigurationService.turnCodeToValue("LC_INFO_ORG_CODE", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		String OUTLINE_FONT_SETTINGS_BUILD = this.funConfigurationService.turnCodeToValue("OUTLINE_FONT_SETTINGS_BUILD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		String IS_ACCUMULATION_LAYER = this.funConfigurationService.turnCodeToValue("IS_ACCUMULATION_LAYER", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		String YOUKE_DOCK_MODE = this.funConfigurationService.turnCodeToValue("YOUKE_DOCK_MODE", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		
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
		
		if(YOUKE_DOCK_MODE == null || "".equals(YOUKE_DOCK_MODE)) {
			YOUKE_DOCK_MODE = "0";
		}
		map.put("AUTOMATIC_CLEAR_MAP_LAYER", AUTOMATIC_CLEAR_MAP_LAYER);
		map.put("IS_IMAGE_MAP_SHOW_CONTOUR", IS_IMAGE_MAP_SHOW_CONTOUR);
		map.put("IS_VECTOR_MAP_SHOW_CONTOUR", IS_VECTOR_MAP_SHOW_CONTOUR);
		map.put("IS_GRID_ARCGIS_SHOW_CENTER_POINT", IS_GRID_ARCGIS_SHOW_CENTER_POINT);
		map.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
		map.put("OUTLINE_FONT_SETTINGS_BUILD", OUTLINE_FONT_SETTINGS_BUILD);
		map.put("IS_ACCUMULATION_LAYER", IS_ACCUMULATION_LAYER);
		map.put("YOUKE_DOCK_MODE", YOUKE_DOCK_MODE);
		map.put("LC_INFO_ORG_CODE", LC_INFO_ORG_CODE);
		map.put("gridId", gridInfo.getGridId());
		map.put("gridCode", gridInfo.getGridCode());
		map.put("gridName", gridInfo.getGridName());
		map.put("gridLevel", gridInfo.getGridLevel());
		map.addAttribute("orgCode", startOrgCode);
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		/*
		if("gansu".equals(arcgisUrlType)) {//甘肃地图首页
			forward = "/map/arcgis/arcgis_base/arcgis_gansu/arcgis_standard_index.ftl";
		}else if("xinyangjcsj".equals(arcgisUrlType)) {//新阳街道基础数据
			forward = "/map/arcgis/arcgis_base/arcgis_xinyang/arcgis_standard_jcsj.ftl";
		}else if("xinyangdzz".equals(arcgisUrlType)) {//新阳街道党组织
			forward = "/map/arcgis/arcgis_base/arcgis_xinyang/arcgis_standard_dzz.ftl";
		}else if("xinyangzhdd".equals(arcgisUrlType)) {//新阳街道指挥调度
			forward = "/map/arcgis/arcgis_base/arcgis_xinyang/arcgis_standard_zhdd.ftl";
		}else if ("taijiang".equals(arcgisUrlType)) {//台江地图图层
			forward = "/map/arcgis/arcgis_base/arcgis_taijiang/arcgis_standard_index.ftl";
		}else if ("jinjiangfiregrid".equals(arcgisUrlType)) {//晋江消防网格图层
			forward = "/map/arcgis/arcgis_base/arcgis_jinjiang/arcgis_standard_index.ftl";
		}else if("standard".equals(arcgisUrlType)){
			forward = "/map/arcgis/arcgis_base/arcgis_standard_index.ftl";
		}eslse if("lyjj".equals(arcgisUrlType)){
			map.put("SQ_LYJJ_URL", App.LYJJ.getDomain(session));
			forward = "/map/arcgis/arcgis_base/arcgis_lyjj/arcgis_standard_index.ftl";
		}ele if("haicanganjian".equals(arcgisUrlType)) {
			forward = "/map/arcgis/arcgis_base/arcgis_haicang/arcgis_standard_anjian.ftl";
		}else if ("swjc".equals(arcgisUrlType)) {
			forward = "/map/arcgis/arcgis_base/arcgis_swjc/arcgis_standard_swjc.ftl";
		}else if ("putian".equals(arcgisUrlType)) {//莆田地图中间首页
			forward = "/map/arcgis/arcgis_base/arcgis_putian/arcgis_standard_index.ftl";
		}else */
		if ("taijiang".equals(arcgisUrlType)) {//台江地图图层
			forward = "/map/arcgis/arcgis_base/arcgis_taijiang/arcgis_standard_index.ftl";
		}else if ("zhonghuady".equals(arcgisUrlType)) {//中华街道党员图层
			forward = "/map/arcgis/arcgis_base/arcgis_zhonghua/arcgis_standard_dy.ftl";
		}else if ("zhonghuafkqy".equals(arcgisUrlType)) {//中华街道防控区域
			forward = "/map/arcgis/arcgis_base/arcgis_zhonghua/arcgis_standard_fkqy.ftl";
		}else if("tree".equals(arcgisUrlType)){
			forward = "/map/arcgis/arcgis_base/arcgis_config_tree_index.ftl";
		}else if ("001".equals(arcgisUrlType)||"002".equals(arcgisUrlType)||"003".equals(arcgisUrlType)||"004".equals(arcgisUrlType)||"005".equals(arcgisUrlType)||"006".equals(arcgisUrlType)) {
			map.addAttribute("eyesType", arcgisUrlType);
			forward = "/map/arcgis/arcgis_base/arcgis_spjk/arcgis_standard_spjk.ftl";
		}else if ("globalEyes".equals(arcgisUrlType)) {
			forward = "/map/arcgis/arcgis_base/arcgis_globalEyes/arcgis_standard_globalEyes.ftl";
		}else if("lyjj".equals(arcgisUrlType)){
			map.put("SQ_LYJJ_URL", App.LYJJ.getDomain(session));
			forward = "/map/arcgis/arcgis_base/arcgis_lyjj/arcgis_standard_index.ftl";
		}
		return forward;
	}
	
//	@RequestMapping(value="/dispute/toMapArcgis")
//	public String toMapArcgis_pt(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map){
//		map.addAttribute("BI_DOMAIN", App.BI.getDomain(session));
//		String regionCode = super.getDefaultInfoOrgCode(session);
//		map.addAttribute("regionCode", regionCode);
//		return "/map/arcgis/arcgis_base/arcgis_index_dispute_versionnoe.ftl";
//	}
	
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
	@RequestMapping(value="/toMapArcgisOfNewVersion")
	public String toMapArcgisOfNewVersion(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="homePageType", required=true) String homePageType) throws Exception{
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String forward = "/map/arcgis/arcgis_base/arcgis_config_index_versionnoe.ftl";
		String fillOpacity = request.getParameter("fillOpacity");
		String menuCode = request.getParameter("menuCode");
		
		
		
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String infoOrgCode = (String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		
		Map<String, Object> initMapParams = CommonFunctions.initMap(infoOrgCode, homePageType, session, map);
		
		if("ARCGIS_STANDARD_HOME".equals(homePageType)) { //盐都 指挥调度页面和应急指挥页面整合
			String isUserAnychat = (String) initMapParams.get("isUserAnychat");
			map.put("userInfo", userInfo);
			
		/*	if(StringUtils.isNotBlank(isUseMediasoup)&&isUseMediasoup.equals("true")) {
				//mediasoup相关
				map.put("anyChatServerUrl", (String) initMapParams.get("anyChatServerUrl"));
				forward = "/map/arcgis/arcgis_base/arcgis_config_index_versionnoe_ms.ftl";
			} else*/
			if (userInfo.getOrgCode().startsWith("320903") && StringUtils.isNotBlank(isUserAnychat)&&isUserAnychat.equals("true")) {
				forward = "/map/arcgis/arcgis_base/arcgis_config_index_versionnoe_yd.ftl";
				
			}
		}else if ("MAPABC_STANDARD_HOME".equals(homePageType)) {
			forward = "/map/mapabc/mapabc_index.ftl";
		} else if ("SPGIS_BUSI_HOME".equals(homePageType)) {
			forward = "/map/mapabc/arcgis_config_index_PingTan.ftl";
		} else if ("ARCGIS_JY_HOME".equals(homePageType)) {
			forward = "/map/arcgis/arcgis_base/arcgis_config_index_jy.ftl";
		} else if ("SPGIS_KEY_POPU".equals(homePageType)) {// 重点人群地图
			forward = "/map/mapabc/spgis_kePopu_index_PingTan.ftl";
		} else if ("SPGIS_KEY_POPU2".equals(homePageType)) {// 重点人群地图
			forward = "/map/mapabc/spgis_keyPopu_index2_PingTan.ftl";
		} else if("IDSS".equals(homePageType)){
			forward = "/map/arcgis/arcgis_base/arcgis_idss_index_versionnoe.ftl";
		} else if ("URBAN_OBJ_HOME".equals(homePageType)) {
			GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgByCode(menuCode, userInfo.getOrgCode());
			CommonFunctions.transGisDataCfgUrl(gisDataCfg, session);
			map.put("gisDataCfg", gisDataCfg);
			forward = "/map/arcgis/arcgis_base/urban_obj_index_versionnoe.ftl";
		} else if ("ARCGIS_KEY_POPU".equals(homePageType)) {
			forward = "/map/arcgis/arcgis_base/key_popu_index_versionnoe.ftl";
		}else if("INTELLIGENT_VIDEO_SURVEILLANCE".equals(homePageType)){
			forward = "/map/arcgis/arcgis_base/arcgis_video_surveillance_index_versionnoe.ftl";
		}else if("SMART_CITY".equals(homePageType)){
			GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgByCode("keyPop", userInfo.getOrgCode());
			super.transGisDataCfgUrl(gisDataCfg, session);
			if (gisDataCfg != null) request.setAttribute("keyPopEcs", gisDataCfg.getElementsCollectionStr());
			
			gisDataCfg = this.menuConfigService.getGisDataCfgByCode("statPartyMember", userInfo.getOrgCode());
			super.transGisDataCfgUrl(gisDataCfg, session);
			if (gisDataCfg != null) request.setAttribute("partyMemberEcs", gisDataCfg.getElementsCollectionStr());
			map.addAttribute("NEW_HEATMAP", "true");
			forward = "/map/arcgis/arcgis_base/arcgis_smartCity/arcgis_config_index_smartCity.ftl";
		}else if("SMART_COMMAND_CITY".equals(homePageType)){
			forward = "/map/arcgis/arcgis_base/arcgis_smartCity/arcgis_config_index_smartCommandCity.ftl";
		}else if("MAP_THEMATIC_LAYER_LIST".equals(homePageType)){
//			GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgByCode(menuCode, userInfo.getOrgCode());
//			CommonFunctions.transGisDataCfgUrl(gisDataCfg, session);
//			map.put("gisDataCfg", gisDataCfg);
			map.put("menuCode", menuCode);
			forward = "/map/arcgis/map_thematic_layer_list/map_thematic_layer_list.ftl";
		}else if("SMART_CITY_AIR".equals(homePageType)){//南平空气质量
            forward = "/map/arcgis/arcgis_base/arcgis_smartCity/arcgis_config_index_smartCity_AIR.ftl";
        }else if("SMART_MONITOR_CENTER".equals(homePageType)){//综治智能监控指挥中心
    		map.addAttribute("tempdir",System.getProperty("java.io.tmpdir"));
            forward = "/map/arcgis/arcgis_base/arcgis_smartCity/arcgis_config_index_smart_monitor_center.ftl";
        }else if("SMART_CITY_HEATMAP".equals(homePageType)){
			map.put("PARAM_DATE", request.getParameter("date"));
			forward = "/map/arcgis/arcgis_base/arcgis_smartCity/arcgis_config_index_smartCity_heatMap.ftl";
		} else if ("ARCGIS_3D_HOME".equals(homePageType)) {			
			UserBO user = userManageOutService.getUserInfoByUserId(userInfo.getUserId());
			map.addAttribute("user", user);//个人中心用
			
			map.put("dateNow", DateUtils.formatDate(new Date(), DateUtils.PATTERN_DATE));
			forward = "/map/arcgis/arcgis_3d/arcgis3d_index_versionnoe_20200203.ftl";
			
			//判断用户是否有一键督查的职位
			int cheakUserForParas = userInfoService.cheakUserForParas(userInfo.getUserId(), userInfo.getOrgId(), POSITION_NAME);
			map.put("showQuickSupervision", cheakUserForParas>0?"true":"false");
			
        	OrgSocialInfoBO orgSocialInfoBO =orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());
        	String professionCode=orgSocialInfoBO.getProfessionCode();
        	map.put("chiefLevel", orgSocialInfoBO.getChiefLevel());//组织层级:2-市级，3-区县级
        	String[] professionCodeArr=orgSocialInfoBO.getProfessionCode().split(",");
        	String orgType = orgSocialInfoBO.getOrgType();
        	Map<String,Object> params2 = new HashMap<String,Object>();
        	if("0".equals(orgType)) {
        		professionCode =professionCodeArr[0];
        		params2.put("infoOrgCode",orgSocialInfoBO.getOrgCode());
    			List<Map<String,Object>> civilizedPerInfos=nanChang3DService.findCivilizedPerInfoData(params2);
    			if(civilizedPerInfos.size()>0) {
    				map.put("workDepartment", orgSocialInfoBO.getOrgName());//工作部名称
    				map.put("pmTel", civilizedPerInfos.get(0).get("MOBILE_TELEPHONE"));//责任人电话
    				map.put("pointManager", civilizedPerInfos.get(0).get("NAME"));//责任人名称
    			}
        	}
        	map.put("orgType", orgType);
        	map.put("professionCode", professionCode);
        	map.put("orgId", userInfo.getOrgId());
        	Map<String, Object> params = new HashMap<String, Object>();
    		params.put("userId", userInfo.getUserId());
    		params.put("orgId", userInfo.getOrgId());
        	List<PositionInfoBO> list = positionInfoOutService.queryPositionByPara(params);
    		for (PositionInfoBO p : list) {
    			switch (p.getPositionCode()) {
				case "pointPosition":map.put("pointPosition", "true");break;
				case "videoPosition":map.put("videoPosition", "true");break;
				case "zhz":map.put("zhz", "true");break;//综合组
				case "yryd":map.put("yryd", "true");break;//一人疫档
				case "fcz":map.put("fcz", "true");break;//返城组
				case "qyfg":map.put("qyfg", "true");break;//企业复工
				case "qyfu":map.put("qyfu", "true");break;//企业复工南昌
				case "sqz":map.put("sqz", "true");break;//社区组
				case "ylz":map.put("ylz", "true");break;//医疗组
				case "wzz":map.put("wzz", "true");break;//物资组
				case "spz":map.put("spz", "true");break;//视频组
				case "lly":map.put("lly", "true");break;//联络员
				case "yqfx":map.put("yqfx", "true");break;//舆情分析
				case "yqck":
					forward = "/map/arcgis/arcgis_3d/arcgis3d_index_versionnoe.ftl";
    				if("15".equals(userInfo.getOrgCode())) {//内蒙古
    	    			forward = "/map/arcgis/arcgis_3d/arcgis3d_index_versionnoe_neimenggu.ftl";
    	    		}else if("3604".equals(userInfo.getOrgCode())) {//九江
    	    			forward = "/map/arcgis/arcgis_3d/arcgis3d_index_versionnoe_jiujiang.ftl";
    	    		}
					break;//疫情查看
				default:
					break;
				}
    			
    			
    		}
        } else if ("ARCGIS_TEXT_3D".equals(homePageType)) {
        	forward = "/map/arcgis/arcgis_3d/text3D.html";
        } else if ("ARCGIS_TEXT2_3D".equals(homePageType)) {
        	forward = "/map/arcgis/arcgis_3d/text3D2.html";
        } else if ("ARCGIS_TEXT4490_3D".equals(homePageType)) {
        	forward = "/map/arcgis/arcgis_3d/text3D_4490.html";
        }
		if (StringUtils.isNotBlank(fillOpacity)) {
			request.setAttribute("fillOpacity", fillOpacity);
		}
		if (StringUtils.isNotBlank(menuCode)) {
			request.setAttribute("menuCode", menuCode);
		}
		request.setAttribute("BI_REPORT", App.BI.getDomain(session));
		
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("GMIS_DOMAIN", App.GMIS.getDomain(session));
		map.put("POPULATION_URL", App.RS.getDomain(session));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.put("OAUTH_URL", App.UAM.getDomain(session));
		map.put("WATER_MONITOR_URL", App.WATER.getDomain(session));
		map.addAttribute("ZZGRID_DOMAIN", App.ZZGRID.getDomain(session));

		//甘肃
		if(userInfo != null && StringUtils.isNotBlank(userInfo.getOrgCode()) && userInfo.getOrgCode().startsWith(ConstantValue.GANSU_FUNC_ORG_CODE)){
			map.put("isGansu", "yes");
		}
		map.put("gridId", gridInfo.getGridId());
		map.put("gridCode", gridInfo.getGridCode());
		map.put("gridName", gridInfo.getGridName());
		map.put("gridLevel", gridInfo.getGridLevel());
		map.put("orgCode", infoOrgCode);
		map.put("socialOrgCode", userInfo.getOrgCode());
		map.put("homePageType", homePageType);
		map.put("GBP_DOMAIN", App.GBP.getDomain(session));
		
		map.put("userName", userInfo.getUserName());
		map.put("partyName", userInfo.getPartyName());
		map.put("userId", userInfo.getUserId());
		map.put("svrip", ConstantValue.MMP_SVRIP);
		map.put("mediaurl", ConstantValue.MMP_MEDIAURL);
		map.addAttribute("RESOURCE_DOMAIN", App.RESOURCE.getDomain(session));
		if(userInfo.getOrgCode().startsWith(ConstantValue.LUOFANG_INFO_ORG_CODE)){
			map.addAttribute("LUO_FANG", "true");
		}
		map.addAttribute("NAN_AN", infoOrgCode.startsWith(ConstantValue.NANAN_FUNC_ORG_CODE));//南安
		
		//是否启用MEDIASOUP 视频通话
		String isUseMediasoup = this.funConfigurationService.turnCodeToValue(ConstantValue.IS_USE_MEDIASOUP, "",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUseMediasoup", isUseMediasoup);
		if(StringUtils.isNotEmpty(isUseMediasoup)&&isUseMediasoup.equals("true")) {
					map.addAttribute("serverUrl",funConfigurationService.turnCodeToValue(ConstantValue.IS_USE_MEDIASOUP, "SERVER_URL",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0));
					map.addAttribute("wsUrl",funConfigurationService.turnCodeToValue(ConstantValue.IS_USE_MEDIASOUP, "WS_URL",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0));
					map.addAttribute("mediaUrl",funConfigurationService.turnCodeToValue(ConstantValue.IS_USE_MEDIASOUP, "MEDIA_URL",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0));
		}
		
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
	
	@RequestMapping(value="/toMigrationMap")
	public String toMigrationMap(HttpSession session, HttpServletRequest request, ModelMap map) {
		String[] codes = ConstantValue.FLOW_CODE.split("_");
		map.put("flowCode", codes[0]);
		map.put("districtcode", codes[1]);
		return "/map/mapabc/migration_map.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/findMigrationData")
	public List<Map<String, Object>> findMigrationData(@RequestParam(value = "date", required = true) String date,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "isOutType", required = true) String isOutType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("date", date);
		params.put("type", type);
		params.put("isOutType", isOutType);
		params.put("flowCode", ConstantValue.FLOW_CODE);
		List<Map<String, Object>> list = migrationMapService.findMigrationData(params);
		return list;
	}
	
	@RequestMapping(value="/toMapArcgisOfFactor")
	public String toMapArcgisOfFactor(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="homePageType", required=true) String homePageType) throws Exception{
		String forward = "/map/arcgis/arcgis_base/arcgis_config_index_factor.ftl";
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String startOrgCode = (String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("POPULATION_URL", App.RS.getDomain(session));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.put("OAUTH_URL", App.UAM.getDomain(session));
		map.put("WATER_MONITOR_URL", App.WATER.getDomain(session));
		//是否自动清除图层
		String AUTOMATIC_CLEAR_MAP_LAYER = this.funConfigurationService.turnCodeToValue("AUTOMATIC_CLEAR_MAP_LAYER", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
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
		map.put("AUTOMATIC_CLEAR_MAP_LAYER", AUTOMATIC_CLEAR_MAP_LAYER);
		map.put("IS_IMAGE_MAP_SHOW_CONTOUR", IS_IMAGE_MAP_SHOW_CONTOUR);
		map.put("IS_VECTOR_MAP_SHOW_CONTOUR", IS_VECTOR_MAP_SHOW_CONTOUR);
		map.put("IS_GRID_ARCGIS_SHOW_CENTER_POINT", IS_GRID_ARCGIS_SHOW_CENTER_POINT);
		map.put("LC_INFO_ORG_CODE", LC_INFO_ORG_CODE);
		map.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
		map.put("OUTLINE_FONT_SETTINGS_BUILD", OUTLINE_FONT_SETTINGS_BUILD);
		map.put("IS_ACCUMULATION_LAYER", IS_ACCUMULATION_LAYER);
		map.put("ARCGIS_DOCK_MODE", ARCGIS_DOCK_MODE);
		map.put("arcgisFactorUrl", arcgisFactorUrl);
		map.put("gridId", gridInfo.getGridId());
		map.put("gridCode", gridInfo.getGridCode());
		map.put("gridName", gridInfo.getGridName());
		map.put("gridLevel", gridInfo.getGridLevel());
		map.put("orgCode", startOrgCode);
		map.put("socialOrgCode", userInfo.getOrgCode());
		map.put("homePageType", homePageType);
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
	
	@RequestMapping(value="/toGisStatIndex")
	public String toGisStatIndex(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="homePageType") String homePageType) throws Exception{
		String forward = "/map/arcgis/arcgis_base/gis_stat/gis_stat_index.ftl";
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String startOrgCode = (String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		
		map.put("gridId", gridInfo.getGridId());
		map.put("gridCode", gridInfo.getGridCode());
		map.put("gridName", gridInfo.getGridName());
		map.put("gridLevel", gridInfo.getGridLevel());
		map.put("orgCode", startOrgCode);
		map.put("socialOrgCode", userInfo.getOrgCode());
		map.put("homePageType", homePageType);
		
		
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0);
		return forward;
	}
	
	/**
	 * 2014-05-28 liushi add
	 * 转载到地图编辑的加载页面
	 * @param session
	 * @param request
	 * @param map
	 * @param arcgisUrlType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toMapArcgisOfDraw")
	public String toMapArcgisOfDraw(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="arcgisUrlType", required=false) String arcgisUrlType) throws Exception{
		String forward = "/map/arcgis/arcgis_draw/arcgis_draw.ftl";
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		map.put("gridId", gridInfo.getGridId());
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
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
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
	@RequestMapping(value="/toMapArcgisOfDrawJJBuild")
	public String toMapArcgisOfDrawJJBuild(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="arcgisUrlType", required=false) String arcgisUrlType) throws Exception{
		String forward = "/map/arcgis/arcgis_draw/arcgis_draw_jjbuild.ftl";
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		map.put("gridId", gridInfo.getGridId());
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
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
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
	@RequestMapping(value="/toMapArcgisOfProxy")
	public String toMapArcgisOfProxy(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception{
		String forward = "/proxy";
		return forward;
	}
	/**
	 * 2014-06-19 liushi add
	 * 转到地图描点页面
	 * @param session
	 * @param request
	 * @param map
	 * @param arcgisUrlType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toMapArcgisOfAnchorPoint")
	public String toMapArcgisOfAnchorPoint(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="x", required=false) Double x
			,@RequestParam(value="y", required=false) Double y
			,@RequestParam(value="gridId", required=false) Long gridId
			,@RequestParam(value="mapt", required=false) Integer mapt
			,@RequestParam(value="mapType", required=false) String mapType
			,@RequestParam(value="isEdit", required=false) String isEdit) throws Exception{
		String forward = "/map/arcgis/arcgis_draw/arcgis_anchorpoint.ftl";
		if(mapt == null) {
			mapt = 5;
		}
		if(x==null || y==null){
			if(gridId == null){
				MixedGridInfo gridInfo = getMixedGridInfo(session,request);
				//获取当前登录人员所在网格信息
				if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
				gridId = gridInfo.getGridId();
			}
			List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,mapt);
			if(list.size() == 1){
				x = list.get(0).getX();
				y = list.get(0).getY();
			}
			
		}
		if(x==null) {
			x = 0.0;
		}
		if(y == null) {
			y = 0.0;
		}
		
		map.put("x", x);
		map.put("y", y);
		map.put("mapt", mapt);
		map.put("gridId", gridId);
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		if(mapType == null) {
			mapType = "";
		}
		if(isEdit == null || "".equals(isEdit)) {
			isEdit = "true";
		}
		map.put("isEdit", isEdit);
		map.put("mapType", mapType);
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
	/**
	 * 2014-06-19 liushi add
	 * 转到可跨域操作的地图描点页面
	 * @param session
	 * @param request
	 * @param map
	 * @param x
	 * @param y
	 * @param mapt
	 * @param domainUrl
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toMapArcgisOfAnchorPointCrossDomain")
	public String toMapArcgisOfAnchorPointCrossDomain(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="x", required=false) Double x
			,@RequestParam(value="y", required=false) Double y
			,@RequestParam(value="gridId", required=false) Long gridId
			,@RequestParam(value="mapt", required=false) Integer mapt) throws Exception{
		String forward = "/map/arcgis/arcgis_draw/arcgis_anchorpoint_domain.ftl";
		if(mapt == null) {
			mapt = 5;
		}
		if(x==null || y==null){
			if(gridId == null){
				MixedGridInfo gridInfo = getMixedGridInfo(session,request);
				//获取当前登录人员所在网格信息
				if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
				gridId = gridInfo.getGridId();
			}
			List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,mapt);
			if(list.size() == 1){
				x = list.get(0).getX();
				y = list.get(0).getY();
			}
		}
		if(x==null) {
			x = 0.0;
		}
		if(y == null) {
			y = 0.0;
		}
		map.put("x", x);
		map.put("y", y);
		map.put("mapt", mapt);
		map.put("gridId", gridId);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
	
	@RequestMapping(value="/outPlatCrossDomain")
	public String outPlatCrossDomain(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="x", required=false) Double x
			,@RequestParam(value="y", required=false) Double y
			,@RequestParam(value="gridId", required=false) Long gridId
			,@RequestParam(value="mapt", required=false) Integer mapt
			,@RequestParam(value="callBackUrl", required=false) String callBackUrl
			,@RequestParam(value="mapType", required=false) String mapType
			,@RequestParam(value="isEdit", required=false) String isEdit
			,@RequestParam(value="infoOrgCode", required=false) String infoOrgCode
			,@RequestParam(value="targetDownDivId", required=false) String targetDownDivId) throws Exception{
		String forward = "/map/arcgis/arcgis_draw/outplat_domain.ftl";
		if(mapt == null) {
			mapt = 5;
		}
		if(isEdit == null || "".equals(isEdit)) {
			isEdit = "true";
		}
		if(StringUtils.isBlank(mapType) || "undefined".equals(mapType)){
			mapType = "2";
		}
		if("3".equals(mapType)) {
			mapt = 30;
		}else if("2".equals(mapType)) {
			mapt = 5;
		}
		Integer mapCenterLevel = 5;
		if(x==null || y==null || x==0.0 || y==0.0){
			map.put("noPosition", "true");
			if(gridId == null){
				if(StringUtils.isNotBlank(infoOrgCode)){
					List<MixedGridInfo> gridInfos = mixedGridInfoService.getMixedGridMappingListByOrgCode(infoOrgCode);
					if(gridInfos != null && gridInfos.size()>0){
						gridId = gridInfos.get(0).getGridId();
					}
				}else {
					MixedGridInfo gridInfo = getMixedGridInfo(session, request);
					//获取当前登录人员所在网格信息
					if (gridInfo == null) throw new Exception("未找到当前网格信息！！");
					gridId = gridInfo.getGridId();
				}
			}
			//获取最接近改网格的且有数据的父级网格轮廓数据
			ArcgisInfoOfGrid arcgisInfoOfGrid = this.arcgisInfoService.getArcgisDataOfLastParentgrid(gridId, mapt);
			if(arcgisInfoOfGrid != null){
				x = arcgisInfoOfGrid.getX();
				y = arcgisInfoOfGrid.getY();
				mapCenterLevel = arcgisInfoOfGrid.getMapCenterLevel();
			}
			
		} else {
			if(gridId == null){
				if(StringUtils.isNotBlank(infoOrgCode)){
					List<MixedGridInfo> gridInfos = mixedGridInfoService.getMixedGridMappingListByOrgCode(infoOrgCode);
					if(gridInfos != null && gridInfos.size()>0){
						gridId = gridInfos.get(0).getGridId();
					}
				}else {
					MixedGridInfo gridInfo = getMixedGridInfo(session, request);
					//获取当前登录人员所在网格信息
					if (gridInfo == null) throw new Exception("未找到当前网格信息！！");
					gridId = gridInfo.getGridId();
				}
			}

			if(gridId != null){
				List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,mapt);
				if(list != null && list.size() == 1){
					mapCenterLevel = list.get(0).getMapCenterLevel();
				}else{
					//获取最接近改网格的且有数据的父级网格轮廓数据
					ArcgisInfoOfGrid arcgisInfoOfGrid = this.arcgisInfoService.getArcgisDataOfLastParentgrid(gridId, mapt);
					if(arcgisInfoOfGrid != null){
						mapCenterLevel = arcgisInfoOfGrid.getMapCenterLevel();
					}
				}
			}

		}
		if(x==null) {
			x = 0.0;
		}
		if(y == null) {
			y = 0.0;
		}
		if(mapCenterLevel == null){
			mapCenterLevel = 5;
		}
		if(callBackUrl == null){
			callBackUrl = "";
		}
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, true);
		map.addAttribute("gridInfo", gridInfo);
		if(gridInfo != null && StringUtils.isNotBlank(gridInfo.getInfoOrgCode())){
			if(//gridInfo.getInfoOrgCode().startsWith(ConstantValue.JINJIANG_INFO_ORG_CODE)
					//&& StringUtils.isNotBlank(isGetXIEJINGAddress)
					//&& isGetXIEJINGAddress.equals("true"
					// )
					StringUtils.isNotBlank(targetDownDivId) //不为空，这是从协警地址控件上弹出标注框，表示是协警地址
					){
				map.addAttribute("isGetXIEJINGAddress", "true");
				forward = "/map/arcgis/arcgis_draw/outplat_domain_XIEJING.ftl";
			}
		}
//		if(StringUtils.isNotBlank(isGetXIEJINGAddress) && isGetXIEJINGAddress.equals("true")){
//			map.addAttribute("isGetXIEJINGAddress", "true");
//			forward = "/map/arcgis/arcgis_draw/outplat_domain_XIEJING.ftl";
//		}
		//-- 区域点位坐标转换成json数据
		if(gridInfo != null && gridInfo.getLocationList() != null){
			String locationListJson = JSONArray.toJSONString(gridInfo.getLocationList());
			map.addAttribute("locationListJson", locationListJson);
		}else{
			map.addAttribute("locationListJson", null);
		}
		if(StringUtils.isNotBlank(targetDownDivId)){
			map.put("targetDownDivId", targetDownDivId);
		}

		map.put("x", x);
		map.put("y", y);
		map.put("mapCenterLevel", mapCenterLevel);
		map.put("mapt", mapt);
		map.put("gridId", gridId);
		
		map.put("isEdit", isEdit);
		map.put("mapType", mapType);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.put("callBackUrl", callBackUrl);
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
	@RequestMapping(value="/outPlatCrossDomainCallBack")
	public String outPlatCrossDomainCallBack(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="x", required=false) Double x
			,@RequestParam(value="y", required=false) Double y
			,@RequestParam(value="gridId", required=false) Long gridId
			,@RequestParam(value="mapt", required=false) Integer mapt) throws Exception{
		String forward = "/map/arcgis/arcgis_draw/gis_cross_domain_back.ftl";
		map.put("gridId", gridId);
		map.put("x", String.valueOf(x));
		map.put("y", String.valueOf(y));
		map.put("mapt", mapt);
		return forward;
	}
	@RequestMapping(value="/spgisCrossDomain")
	public String spgisCrossDomain(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			ModelMap map, @RequestParam(value = "x", required = false) Double x,
			@RequestParam(value = "y", required = false) Double y,
			@RequestParam(value = "gridId", required = false) Long gridId,
			@RequestParam(value = "mapt", required = false) Integer mapt,
			@RequestParam(value = "callBackUrl", required = false) String callBackUrl,
			@RequestParam(value = "mapType", required = false) String mapType,
			@RequestParam(value = "isEdit", required = false) String isEdit,
			@RequestParam(value = "mapEngineType", required = false) String mapEngineType) throws Exception {
		String forward = "/map/arcgis/arcgis_draw/spgis_domain.ftl";
		if(mapt == null) {
			mapt = 5;
		}
		if(isEdit == null || "".equals(isEdit)) {
			isEdit = "true";
		}
		if("3".equals(mapType)) {
			mapt = 30;
		}else if("2".equals(mapType)) {
			mapt = 5;
		}
		if (gridId == null) {
			MixedGridInfo gridInfo = getMixedGridInfo(session, request);
			// 获取当前登录人员所在网格信息
			if (gridInfo == null)
				throw new Exception("未找到当前网格信息！！");
			gridId = gridInfo.getGridId();
		}
		/*if(x==null || y==null){
			if(gridId == null){
				MixedGridInfo gridInfo = getMixedGridInfo(session,request);
				//获取当前登录人员所在网格信息
				if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
				gridId = gridInfo.getGridId();
			}
			List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,mapt);
			if(list.size() == 1){
				x = list.get(0).getX();
				y = list.get(0).getY();
			}
			
		} else {
			if(gridId == null){
				MixedGridInfo gridInfo = getMixedGridInfo(session,request);
				//获取当前登录人员所在网格信息
				if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
				gridId = gridInfo.getGridId();
			}
		}
		if(x==null) {
			x = 0.0;
		}
		if(y == null) {
			y = 0.0;
		}*/
		if(callBackUrl == null){
			callBackUrl = "";
		}
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, true);
		map.addAttribute("gridInfo", gridInfo);
		//-- 区域点位坐标转换成json数据
		String locationListJson = JSONArray.toJSONString(gridInfo.getLocationList());
		map.addAttribute("locationListJson", locationListJson);
		
		map.put("x", x);
		map.put("y", y);
		map.put("mapt", mapt);
		map.put("gridId", gridId);
		
		map.put("isEdit", isEdit);
		map.put("mapType", mapType);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.put("callBackUrl", callBackUrl);
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
	/**
	 * 2014-06-27可直接做点定位保存的市政设施定位
	 * @param session
	 * @param request
	 * @param map
	 * @param resourcesId
	 * @param markerType
	 * @param catalog
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toMapArcgisOfAnchorPointForSave")
	public String toMapArcgisOfAnchorPointCrossDomain(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="resourcesId") Long resourcesId
			,@RequestParam(value="markerType") String markerType
			,@RequestParam(value="catalog") String catalog) throws Exception{
		String forward = "/map/arcgis/arcgis_draw/arcgis_anchorpoint_forsave.ftl";
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		
		map.put("gridId", gridInfo.getGridId());
		map.put("resourcesId", resourcesId);
		map.put("markerType", markerType);
		map.put("catalog", catalog);
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
	/**
	 * 2014-06-19 liushi add
	 * 获取当前网格的配置信息
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/getMapArcgisInfo")
	public Map<String, Object> getMapArcgisInfo(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="gridId", required=false) Long gridId) throws Exception{
		Map<String, Object> result = new HashMap<String,Object>();
		//UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		ArcgisMapConfigInfo mapConfigInfo = this.arcgisInfoService.findGisMapConfigInfoByOrgCode(gridInfo.getInfoOrgCode(), "arcgis");
		if(mapConfigInfo==null) throw new Exception("未找到当前网格可使用的地图！！");
		result.put("mapConfigInfo", mapConfigInfo);
		return result;
	}
	
	/**
	 * 2014-08-06 liushi add
	 * arcgis信息多配置获取方法
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisInfo")
	public Map<String, Object> getArcgisInfo(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="gridId", required=false) Long gridId) throws Exception{
		Map<String, Object> result = new HashMap<String,Object>();
		//UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		String engineName = request.getParameter("engineName");
		List<ArcgisConfigInfo> arcgisConfigInfos =  this.arcgisInfoService.findArcgisConfigInfoByGridCode(gridInfo.getInfoOrgCode(), engineName);
		if(arcgisConfigInfos==null) throw new Exception("未找到当前网格可使用的地图！！");
		result.put("arcgisConfigInfos", arcgisConfigInfos);
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/getArcgisInfoForJsonp") 
	public String getArcgisInfoForJsonp(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="gridId", required=false) Long gridId) throws Exception {
		String jsoncallback = request.getParameter("jsoncallback");
		Map<String, Object> mapInfo = this.getArcgisInfo(session, request, map, gridId);
		jsoncallback = jsoncallback + "(" + JsonUtils.mapToJson(mapInfo) + ")";
		return jsoncallback;
	}
	
	/**
	 * 2014-06-19 liushi add
	 * 转到地图首页的网格选择树页面
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toMapTree")
	public String toMapTree(HttpSession session, HttpServletRequest request, ModelMap map) throws Exception{
		String forward = "/map/arcgis/standardmappage/grid_tree_standard.ftl";
		return forward;
	}
	
	@RequestMapping(value="/crossDomain")
	public String crossDomain(HttpSession session, HttpServletRequest request,
			ModelMap map,
			@RequestParam(value = "url", required = false) String url)
			throws Exception {
		map.addAttribute("url", url);
		String forward = "/map/arcgis/arcgis_base/crossDomain.ftl";
		return forward;
	}
	
	@RequestMapping(value="/crossDomain2")
	public String crossDomain2(HttpSession session, HttpServletRequest request,
			ModelMap map,
			@RequestParam(value = "url", required = false) String url)
					throws Exception {
		map.addAttribute("url", url);
		String forward = "/map/arcgis/arcgis_base/crossDomain2.ftl";
		return forward;
	}
	
	@RequestMapping(value="/commonCrossDomain")
	public String commonCrossDomain(HttpSession session, HttpServletRequest request,
			ModelMap map,
			@RequestParam(value = "callBackFunction") String callBackFunction)
			throws Exception {
		map.addAttribute("callBackFunction", callBackFunction);
		String forward = "/map/arcgis/arcgis_base/commonCrossDomain.ftl";
		return forward;
	}
	@RequestMapping(value="/toLcSkylineView")
	public String toLcSkylineView(HttpSession session, HttpServletRequest request,
			ModelMap map)
			throws Exception {
		String forward = "/map/arcgis/arcgis_base/lc_skylineview/lc_skylineview.ftl";
		return forward;
	}
	@RequestMapping(value = "/toArcgisCrossDomain")
	public String toArcgisCrossDomain(HttpSession session, ModelMap map,
			@RequestParam(value = "x") String x,
			@RequestParam(value="y") String y,
			@RequestParam(value="mapt") Long mapt) {
		map.addAttribute("x",x);
		map.addAttribute("y",y);
		map.addAttribute("mapt",mapt);

		return "/map/arcgis/arcgis_base/arcgis_cross_domain/map_cross_domain_back.ftl";
	}
	
	/**
	 * 获取地图引擎配置信息
	 * @param session
	 * @param map
	 * @param res
	 * @param mapType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getMapEngineInfo")
	public @ResponseBody Map<String, Object> getMapEngineInfo(HttpSession session, ModelMap map, HttpServletResponse res
			,@RequestParam(value="mapType", required=false) String mapType
			,@RequestParam(value="modularCode", required=false) String modularCode) throws Exception{
				
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(Long.valueOf(defaultGridInfo.get(KEY_START_GRID_ID).toString()), false);
		String mapTypeResult=null;
		if(mapType!=null && !"".equals(mapType) && mapType.length()>1) {
			mapTypeResult = this.funConfigurationService.turnCodeToValue("MAP_TYPE_CODE", mapType, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			if(mapTypeResult != null) {
				mapType = mapTypeResult;
			}
		}
		String orgCode = userInfo.getOrgCode();
		String mapEngineType = "";
		String markerType = "";
		String FACTOR_STATION = "";
		String FACTOR_URL = "";
		String FACTOR_SERVICE = "";
		if(orgCode.indexOf("350111106")==0) {
			mapEngineType="005";
		}else {
			mapEngineType = gridInfo.getMapType();
		}
		Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
		FACTOR_STATION = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", modularCode+"_FACTOR_STATION",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
		FACTOR_URL = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", modularCode+"_FACTOR_URL",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
		FACTOR_SERVICE = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", modularCode+"_FACTOR_SERVICE",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
		markerType = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", modularCode+"_MARKER_TYPE",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("mapEngineType", mapEngineType); 
		result.put("mapType", mapType); 
		result.put("ZHSQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		result.put("ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		result.put("FACTOR_STATION", FACTOR_STATION);
		result.put("FACTOR_URL", FACTOR_URL);
		result.put("FACTOR_SERVICE", FACTOR_SERVICE);
		result.put("markerType",markerType);
		return result;
	}
	@ResponseBody
	@RequestMapping(value="/getClearLyerNames")
	public Map<String, Object> getClearLyerNames(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = true) String homePageType){
		Long gdcId = 4L;
		// UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		String layerNameStr = this.menuConfigService.getGisDataLayerNameVersionNoe(infoOrgCode,homePageType, gdcId);
		Map<String, Object> resultmap = new HashMap<String,Object>();
		resultmap.put("layerNameStr", layerNameStr);
		return resultmap;
	}
	
	/**
	 * 获取新事件标注信息
	 * @param session
	 * @param request
	 * @param map
	 * @param bizType
	 * @param id
	 * @param module 模块（新事件：EVENT_V1）
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/getMapMarkerDataOfEvent")
	public Map<String, Object> getMapMarkerDataOfEvent(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="bizType", required=false) String bizType
			,@RequestParam(value="id", required=true) Long id
			,@RequestParam(value="module", required=false) String module) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		String mapt = null;
		
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);

		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(
				Long.valueOf(defaultGridInfo.get(KEY_START_GRID_ID).toString()), false);

		String mapEngineType = gridInfo.getMapType();
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 地图配置的是新地图(Arcgis地图引擎)还是旧地图(Gis地图引擎)
		if (StringUtils.isNotBlank(mapEngineType)) {
			// 根据模块查询地图配置的是几维地图（二维还是三维）
			String mapDimension = null;

			if (StringUtils.isNotBlank(module)) {
				mapDimension = this.funConfigurationService.turnCodeToValue(this.MAP_TYPE_CODE, module,
						IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);

				// 如果是新地图引擎
				if (mapEngineType.equals(this.NEW_MAP_ENGINE)) {
					// 有配置值
					if (StringUtils.isNotBlank(mapDimension)) {
						if (mapDimension.equals(this.TWO_DIMENSION)) { // 二维
							mapt = this.TWO_DIMENSION_MAPT_OF_NEWMAP; // 二维mapt为5
						} else if (mapDimension.equals(this.THREE_DIMENSION)) { // 三维
							mapt = this.THREE_DIMENSION_MAPT_OF_NEWMAP; // 三维mapt为30
						}
					}
				} else if (mapEngineType.equals(this.OLD_MAP_ENGINE)) { // 旧地图引擎
					// 有配置值
					if (StringUtils.isNotBlank(mapDimension)) {
						if (mapDimension.equals(this.TWO_DIMENSION)) { // 二维
							mapt = this.TWO_DIMENSION_MAPT_OF_OLDMAP;// 二维mapt为2
						} else if (mapDimension.equals(this.THREE_DIMENSION)) {// 三维
							mapt = this.THREE_DIMENSION_MAPT_OF_OLDMAP;// 三维mapt为20
						}
					}
				} else if (this.SPGIS_MAP_ENGINE.equals(mapEngineType)) {
					mapt = "5";
				}
			}
		}
		
		if (id != null) {
			String EVENT_MODULE_CODE = "EVENT_V1",//事件定位模块编码
				   markerType = module;
			List<ResMarker> resMarkers = null;
			
			if(EVENT_MODULE_CODE.equals(module)) {
				markerType = ConstantValue.MARKER_TYPE_EVENT;
			}
			
			resMarkers = resGisMarkerService.findResMarkerByParam(markerType, id, mapt);
			
			if (resMarkers != null && resMarkers.size() > 0) {
				result = getResult(resMarkers.get(0));
			}
		}
		
		return result;
	}
	
	/**
	 * 根据resMarker获取x,y,mapt信息
	 * 
	 * @param resMarker
	 * @return
	 */
	private Map<String, Object> getResult(ResMarker resMarker) {
		Map<String, Object> result = new HashMap<String, Object>();

		if (resMarker != null) {
			if (StringUtils.isNotBlank(resMarker.getX())) {
				result.put("x", Double.parseDouble(resMarker.getX()));
			}

			if (StringUtils.isNotBlank(resMarker.getY())) {
				result.put("y", Double.parseDouble(resMarker.getY()));
			}

			if (StringUtils.isNotBlank(resMarker.getMapType())) {
				result.put("mapt", Double.parseDouble(resMarker.getMapType()));
			}
		}
		
		return result;
	}
	
	/**
	 * 2015-11-11 sulch add
	 * 转载到地图楼宇轮廓编辑的加载页面
	 * @param session
	 * @param request
	 * @param map
	 * @param arcgisUrlType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toMapArcgisOfDrawBuilding")
	public String toMapArcgisOfDrawBuilding(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="arcgisUrlType", required=false) String arcgisUrlType) throws Exception{
		String forward = "/zzgl/drowBuilding/arcgis_draw_building.ftl";
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		map.put("gridId", gridInfo.getGridId());
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
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
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
	
	/**
	 * 2015-11-23 sulch add
	 * 转载到地图网格轮廓编辑的加载页面
	 * @param session
	 * @param request
	 * @param map
	 * @param arcgisUrlType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toMapArcgisOfDrawGrid")
	public String toMapArcgisOfDrawGrid(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="arcgisUrlType", required=false) String arcgisUrlType,
			@RequestParam(value="bindFlag", required=false) String bindFlag) throws Exception{
		String forward = "";
		if(forward != null && "true".equals(bindFlag)){
			forward = "/zzgl/bindGrid/arcgis_bind_grid.ftl";
		}else{
			forward = "/zzgl/drowGrid/arcgis_draw_grid.ftl";
		}
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		map.put("gridId", gridInfo.getGridId());
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
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
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
	
	@RequestMapping(value = "/getBizIdJs")
	public void getBizIdJs(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String infoOrgCode = super.getDefaultInfoOrgCode(session);
		response.setContentType("text/javascript;charset=UTF-8");
		StringBuffer js = new StringBuffer("var BIZ_IDS = {};");
		if (StringUtils.isNotBlank(infoOrgCode)) {
			String gridIds = arcgisInfoService.getBizIds(infoOrgCode, "grid");
			if (StringUtils.isNotBlank(gridIds)) {
				js.append("BIZ_IDS.GRID_IDS = '" + gridIds + "';");
			} else {
				js.append("BIZ_IDS.GRID_IDS = '';");
			}
			String buildingIds = arcgisInfoService.getBizIds(infoOrgCode, "build");
			if (StringUtils.isNotBlank(buildingIds)) {
				js.append("BIZ_IDS.BUILDING_IDS = '" + buildingIds + "';");
			} else {
				js.append("BIZ_IDS.BUILDING_IDS = '';");
			}
		}
		response.setContentLength(js.length());
		super.outJs(response, js.toString());
	}
	
	@RequestMapping(value = "/toPtHome")
	public String toPtHome(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		String menuCode = request.getParameter("menuCode");
		if (StringUtils.isNotBlank(menuCode)) {
			request.setAttribute("menuCode", menuCode.trim());
		}
		String fillOpacity = request.getParameter("fillOpacity");
		if (StringUtils.isNotBlank(fillOpacity)) {
			request.setAttribute("fillOpacity", fillOpacity);
		}
		return "/map/mapabc/ptHome.ftl";
	}

	@RequestMapping(value="/showMapDatasByDistanceDomain")
	public String showMapDatasByDistanceDomain(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="x", required=false) Double x
			,@RequestParam(value="y", required=false) Double y
			,@RequestParam(value="distance", required=false) Long distance
			,@RequestParam(value="bizType", required=false) String bizType) throws Exception{
		String forward = "/map/arcgis/arcgis_draw/show_map_datas_cross_domain.ftl";
		map.put("x", String.valueOf(x));
		map.put("y", String.valueOf(y));
		map.put("distance", distance);
		map.put("bizType", bizType);
		return forward;
	}

	/**
	 * 通过参数获取人员轨迹页面接口
	 * @param session
	 * @param request
	 * @param response
	 * @param map
	 * @param userId
	 * @param imsi
	 * @param userName
	 * @param locateTimeBegin
	 * @param locateTimeEnd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/showTrajectoryIndex")
	public String showTrajectoryIndex(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map
			,@RequestParam(value="userId", required=false) Long userId
			,@RequestParam(value="imsi", required=false) String imsi
			,@RequestParam(value="userName", required=false) String userName
			,@RequestParam(value="locateTimeBegin", required=false) String locateTimeBegin
			,@RequestParam(value="locateTimeEnd", required=false) String locateTimeEnd) throws Exception{
		String forward = "/map/arcgis/arcgis_draw/show_trajectory_index.ftl";
		Long gridId = 0l;
		MixedGridInfo gridInfo = getMixedGridInfo(session, request);
		//获取当前登录人员所在网格信息
		if (gridInfo == null) throw new Exception("未找到当前网格信息！！");
		gridId = gridInfo.getGridId();
		int mapt = 5;
		Double x = null;
		Double y = null;

		Integer mapCenterLevel = 5;

		//获取最接近改网格的且有数据的父级网格轮廓数据
		ArcgisInfoOfGrid arcgisInfoOfGrid = this.arcgisInfoService.getArcgisDataOfLastParentgrid(gridId, mapt);
		if(arcgisInfoOfGrid != null){
			x = arcgisInfoOfGrid.getX();
			y = arcgisInfoOfGrid.getY();
			mapCenterLevel = arcgisInfoOfGrid.getMapCenterLevel();
		}

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtils.isBlank(locateTimeEnd)){
			locateTimeEnd = dateFormat.format(date);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		if(StringUtils.isBlank(locateTimeBegin)){
			date = calendar.getTime();
			locateTimeBegin = dateFormat.format(date);
		}


		map.addAttribute("gridInfo", gridInfo);

		map.put("userId", userId);
		map.put("imsi", imsi);
		map.put("userName", userName);
		map.put("locateTimeBegin", locateTimeBegin);
		map.put("locateTimeEnd", locateTimeEnd);


		map.put("x", x);
		map.put("y", y);
		map.put("mapCenterLevel", mapCenterLevel);
		map.put("mapt", mapt);
		map.put("gridId", gridId);

		map.put("mapType", "2");
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragrma","no-cache");
		response.setDateHeader("Expires",0);
		return forward;
	}


	/**
	 * 轨迹数据查询
	 * @param session
	 * @param request
	 * @param map
	 * @param imsi
	 * @param locateTimeBegin
	 * @param locateTimeEnd
	 * @param mapt
	 * @param userId
	 * @param userName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getTrajectoryListData")
	public List<ArcgisInfoOfTrajectory> getTrajectoryListData(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="imsi", required=false)String imsi
			,@RequestParam(value="locateTimeBegin", required=false)String locateTimeBegin
			,@RequestParam(value="locateTimeEnd", required=false)String locateTimeEnd
			,@RequestParam(value="mapt", required=false)Integer mapt
			,@RequestParam(value="userId", required=false)Long userId
			,@RequestParam(value="userName",required = false)String userName) {
		//String infoOrgCode = super.getDefaultInfoOrgCode(session);
		List<ArcgisInfoOfTrajectory> list = new ArrayList<ArcgisInfoOfTrajectory>();
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);


		Map<String, Object> params = new HashMap<String, Object>();
		if(userId != null){
			params.put("userId", userId);
		}
		if(StringUtils.isNotBlank(userName)){
			params.put("userName",userName);
		}
		if(StringUtils.isNotBlank(imsi)) {
			params.put("imsi", imsi);
		}
		if(StringUtils.isBlank(locateTimeBegin)){
			date = calendar.getTime();
			locateTimeBegin = dateFormat.format(date);
		}
		if(StringUtils.isBlank(locateTimeEnd)){
			locateTimeEnd = dateFormat.format(date);
		}
		params.put("locateTimeBegin",locateTimeBegin);
		params.put("locateTimeEnd",locateTimeEnd);
		list = this.arcgisInfoService.getTrajectoryListByParams(params);

		return list;
	}

	/**
	 * 获取晋江协警地址
	 * @param session
	 * @param request
	 * @param response
	 * @param map
	 * @param x
	 * @param y
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/getAuxiliaryPoliceAddrsByXY")
	public EUDGPagination getAuxiliaryPoliceAddrsByXY(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map
			, @RequestParam(value="x", required=false) String x
			, @RequestParam(value="y", required=false) String y) throws Exception{
		List<GeoStdAddress> list = new ArrayList<GeoStdAddress>();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(x)){
				params.put("x",x);
			}
			if(StringUtils.isNotBlank(y)){
				params.put("y",y);
			}
			list = xieJingAddressService.findNearbyListByParam(params);//AuxiliaryPolice.getAddrs(x, y);
		}catch (Exception e){
			list = new ArrayList<GeoStdAddress>();
		}

		//String address = "";
		EUDGPagination eudgPagination = new EUDGPagination(0, null);;
		if(list != null && list.size()>0){
			eudgPagination = new EUDGPagination(list.size(), list);
		}else{
//			list.add("协警地址1");
//			list.add("协警地址11");
//			list.add("协警地址12");
//			list.add("协警地址13协警地址13协警地址13协警地址13协警地址13");
//			list.add("协警地址14");
//			list.add("协警地址15");
//			list.add("协警地址16");
//			list.add("协警地址17");
//			list.add("协警地址18");
//			list.add("协警地址19");
//			list.add("协警地址20");
//			list.add("协警地址21");
//			list.add("协警地址22");
//			list.add("协警地址23");
//			list.add("协警地址24");
//			list.add("协警地址25");
//			list.add("协警地址26");
//			list.add("协警地址15");
//			list.add("协警地址16");
//			list.add("协警地址17");
//			list.add("协警地址18");
//			list.add("协警地址19");
//			list.add("协警地址20");
//			list.add("协警地址21");
//			list.add("协警地址22");
//			list.add("协警地址23");
//			list.add("协警地址24");
//			list.add("协警地址25");
//			list.add("协警地址26");
		}
		eudgPagination = new EUDGPagination(list.size(), list);
		return eudgPagination;
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
	
	/**
	 * 加载通讯分组分页列表数据
	 * @param request
	 * @param oaCommunicatGroup
	 * @param createGroupParentId
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/loadOaCommunicatGroupList")
	public Object loadOaCommunicatGroupList(HttpServletRequest request,OaCommunicatGroup oaCommunicatGroup,
			Long createGroupParentId,ModelMap map)  {
		//获取当前登录用户信息
		HttpSession session = request.getSession();
		UserInfo currUser = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		//oaCommunicatGroup.setCreateById(currUser.getUserId());
		oaCommunicatGroup.setParentId(createGroupParentId);
		
		//区分顶层和其他层(1、非组织的通讯分组：顶层包含管理员和自身的通讯分组,2、组织构成的通讯分组)
		BootstrapPagination bootstrapPagination;
		if(createGroupParentId!=null && createGroupParentId>0){//获取非最顶层的分页数据
			OaCommunicatGroup parentOaCommunicatGroup = oaCommunicatGroupService.getOaCommunicatGroupById(createGroupParentId);
			if(parentOaCommunicatGroup!=null){
				oaCommunicatGroup.setIsDefData(parentOaCommunicatGroup.getIsDefData());
			}
			oaCommunicatGroup.setOrgCode(currUser.getOrgCode());
			oaCommunicatGroup.setCreateById(currUser.getUserId());
			bootstrapPagination=oaCommunicatGroupService.getOaCommunicatGroupPageList(oaCommunicatGroup);
		}else{//获取最顶层的分页数据(最顶层比较特殊，包含管理员创建大家都可以看,还有自己创建的数据)
			oaCommunicatGroup.setOrgCode(currUser.getOrgCode());
			oaCommunicatGroup.setCreateById(currUser.getUserId());
            //判断当前登录的是否是省级组织
            if(currUser.getOrgCode().length()==2){
                oaCommunicatGroup.setIsTopLevel("isTopLevel");
            }
			bootstrapPagination=oaCommunicatGroupService.getTopOaCommunicatGroupPageList(oaCommunicatGroup);
		}
		
		return bootstrapPagination;
	}
	
	/**
	 * 加载联系人列表数据
	 * @param request
	 * @param response
	 * @param commucatGroupId
	 * @param map
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/loadOaCommunicatPersonList")
	public Object loadOaCommunicatPersonList(HttpServletRequest request,HttpServletResponse response,
			Long commucatGroupId, String keyWords, ModelMap map){
		//获取当前登录用户信息
		HttpSession session = request.getSession();
		UserInfo currUser = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		OaCommunicatPerson oaCommunicatPerson =new OaCommunicatPerson();
		oaCommunicatPerson.setStatus(OaCommunicatPerson.DEL_N);
		oaCommunicatPerson.setCommucatGroupId(commucatGroupId);
		oaCommunicatPerson.setOrgCode(currUser.getOrgCode());
		oaCommunicatPerson.setKeyWords(keyWords);
		
		List<OaCommunicatPerson> oaCommunicatPersonList =  oaCommunicatPersonService.getOaCommunicatPersonList(oaCommunicatPerson);
		
		Map<String,Object> result = new HashMap<String,Object>();
		if(oaCommunicatPersonList!=null && oaCommunicatPersonList.size()>0){
			result.put("state", "success");
			result.put("resultList", oaCommunicatPersonList);
		}else{
			result.put("state", "fail");
		}
		return result;
	}
	
	
	/**
	 * 获取通讯联系人详情信息
	 * @param request
	 * @param response
	 * @param detailPersonId
	 * @param map
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/detailPerson")
	public Object detailPerson(HttpServletRequest request,HttpServletResponse response,
			Long detailPersonId,String orgCode,ModelMap map){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("id", detailPersonId);
		param.put("orgCode", orgCode);
		OaCommunicatPerson oaCommunicatPerson = oaCommunicatPersonService.getOaCommunicatPersonById(param);
		
		Map<String,Object> result = new HashMap<String,Object>();
		if(oaCommunicatPerson!=null && oaCommunicatPerson.getId()>0){
			result.put("state", "success");
			result.put("oaCommunicatPerson", oaCommunicatPerson);
		}else{
			result.put("state", "fail");
		}
		return result;
	}
}
