package cn.ffcs.zhsq.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import cn.ffcs.cookie.bo.UserCookie;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.grid.domain.db.Organization;
import cn.ffcs.shequ.grid.service.IOrganizationService;
import cn.ffcs.shequ.mybatis.domain.zzgl.event.EventDisposal;
import cn.ffcs.shequ.mybatis.domain.zzgl.firegrid.FireResource;
import cn.ffcs.shequ.mybatis.domain.zzgl.firegrid.FireTeam;
import cn.ffcs.shequ.mybatis.domain.zzgl.globalEyes.MonitorBO;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.AreaBuildingInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CorBaseInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PlaceInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.SegmentGrid;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapBuidingInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapConfigInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapConfigInfoOfType;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapSegmentGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.ZTreeNode;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.data.GisMapInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.RoadManagement;
import cn.ffcs.shequ.zzgl.service.event.IEventDisposalService;
import cn.ffcs.shequ.zzgl.service.firegrid.IFireResourceService;
import cn.ffcs.shequ.zzgl.service.firegrid.IFireTeamNewService;
import cn.ffcs.shequ.zzgl.service.firegrid.IFireTeamService;
import cn.ffcs.shequ.zzgl.service.globalEyes.MonitorService;
import cn.ffcs.shequ.zzgl.service.grid.IAreaBuildingInfoService;
import cn.ffcs.shequ.zzgl.service.grid.ICorBaseInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IPlaceInfoService;
import cn.ffcs.shequ.zzgl.service.grid.ISegmentGridService;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.shequ.zzgl.service.res.IRoadManagementService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.Menu;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.MenuOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.base.local.service.IGridLogService;
import cn.ffcs.zhsq.base.local.service.IGridLogService.ActionType;
import cn.ffcs.zhsq.base.local.service.LogModule;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.ReadProperties;
import cn.ffcs.zhsq.utils.domain.App;
import net.sf.json.JSONObject;


@Controller
@RequestMapping(value="/zhsq/map/gis")
public class GisController extends ZZBaseController{

	@Autowired
	private IDictionaryService dictionaryService;// 字典
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	protected IGisInfoService gisInfoService; // gis地图相关服务
	@Autowired
	private IAreaBuildingInfoService areaBuildingInfoService;// 楼宇服务
	@Autowired
	private ISegmentGridService segmentGridService;// 片区网格
	@Autowired
	private MenuOutService menuOutService; // 菜单
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService; // 功能域组织服务
	@Autowired
	private IResMarkerService resMarkerService;
	@Autowired
	private IGridLogService logService; // 日志服务
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IMenuConfigService menuConfigService;
	@Autowired
	private ICorBaseInfoService corBaseInfoService; // 企业服务
	@Autowired
	private IOrganizationService organizationService;// 组织服务
	@Autowired
	private IFireResourceService fireResourceService; // 消防服务
	@Autowired
	private IFireTeamService fireTeamService;// 消防队
	@Autowired
	private IFireTeamNewService fireTeamNewService;// 消防队
	@Autowired
	private MonitorService monitorService; // 全球眼
	@Autowired
	private IPlaceInfoService placeInfoService; // 校园周边
	@Autowired
	private IRoadManagementService roadManagementService; // 道路管理
	@Autowired
	private IResInfoService resInfoService; // 市政设施
	@Autowired
	private IEventDisposalService eventDisposalService; // 旧事件
	/*@Autowired
	private IKuangXuanStatService kuangXuanStatService;//框选统计服务
*/	
	/**
	 * 地图首页
	 * @param session
	 * @param request
	 * @param map
	 * @param sysId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mapindex")
	public String mapindex(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="sysId", required=false) String sysId) throws Exception{
		
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		Long startGridId = gridInfo.getGridId();
		
		GisMapConfigInfo mapConfig = gisInfoService.findGisMapConfigInfoByOrgCode(gridInfo.getInfoOrgCode());
		if(mapConfig==null) throw new Exception("未找到该网格组织对应的地图配置信息！！");
		
		map.addAttribute("sysId", sysId);
		map.addAttribute("gridId", startGridId);
		map.addAttribute("request", request);
		map.addAttribute("basePath", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort());
		
		return "/zzgl_map/gis/mapindex.ftl";
	}
	@RequestMapping(value="/mapPageIndex")
	public String mapPageIndex(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="mapPageType", required=false) String mapPageType) throws Exception{
		String forward = "/zzgl_map/gis/standardmappage/standardmappageindex.ftl";
		if("standardmappage".equals(mapPageType)) {
			forward = "/zzgl_map/gis/standardmappage/standardmappageindex.ftl";
		}
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		Long startGridId = gridInfo.getGridId();
		
		GisMapConfigInfo mapConfig = gisInfoService.findGisMapConfigInfoByOrgCode(gridInfo.getInfoOrgCode());
		if(mapConfig==null) throw new Exception("未找到该网格组织对应的地图配置信息！！");
		
		map.addAttribute("startGridId", startGridId);
		map.addAttribute("gridName", gridInfo.getGridName());
		map.addAttribute("request", request);
		map.addAttribute("basePath", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort());
		
		return forward;
	}
	/**
	 * 地图网格树
	 * @param session
	 * @param request
	 * @param cat
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/gridTree")
	public String gridTree(HttpSession session, HttpServletRequest request, @RequestParam(value="startGridId", required=false) Long startGridId, @RequestParam(value="cat", required=false) String cat, ModelMap map) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("startGridId", startGridId==null?defaultGridInfo.get(KEY_START_GRID_ID):startGridId);
		return "/zzgl_map/gis/gridTree.ftl";
	}
	@RequestMapping(value="/crossDomain")
	public String crossDomain(HttpSession session, HttpServletRequest request, ModelMap map
			, @RequestParam(value="x", required=false) Double x
			, @RequestParam(value="y", required=false) Double y
			, @RequestParam(value="mapt", required=false) Integer mapt) {
		map.put("x", x);
		map.put("y", y);
		map.put("mapt", mapt);
		return "/zzgl_map/gis/crossDomain.ftl";
	}
	/**
	 * 2015-01-07 liushi add
	 * 新事件使用旧地图进行定位标注
	 */
	@RequestMapping(value="/popMapMarkerSelectorCrossDomain")
	public String popMapMarkerSelectorCrossDomain(HttpSession session, HttpServletRequest request, ModelMap map
			, @RequestParam(value="gridId", required=false) Double gridId
			, @RequestParam(value="x", required=false) Double x
			, @RequestParam(value="y", required=false) Double y
			, @RequestParam(value="mapt", required=false) Integer mapt) {
		map.addAttribute("GIS_MAP_GANSU_URL", App.EVENT.getDomain(session));//支持多域名
		if(x == null){
			x = 0d;
			y = 0d;
			mapt = 2;
			
		}
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("gridId", gridId==null?defaultGridInfo.get(KEY_START_GRID_ID):gridId);
		map.put("x", String.valueOf(x));
		map.put("y", String.valueOf(y));
		map.put("mapt", mapt);
		return "/component/popMapMarkerSelectorCrossDomain.ftl";
	}
	/**
	 * 2015-01-07 liushi add
	 * 新事件使用旧地图进行定位标注
	 */
	@RequestMapping(value="/popMapMarkerSelectorCrossDomainNew")
	public String popMapMarkerSelectorCrossDomainNew(HttpSession session, HttpServletRequest request, ModelMap map
			, @RequestParam(value="gridId", required=false) Double gridId
			, @RequestParam(value="x", required=false) Double x
			, @RequestParam(value="y", required=false) Double y
			, @RequestParam(value="mapt", required=false) Integer mapt
			, @RequestParam(value="callBackUrl", required=false) String callBackUrl
			, @RequestParam(value="isEdit", required=false) String isEdit
			, @RequestParam(value="mapType", required=false) String mapType) {
		map.addAttribute("GIS_MAP_GANSU_URL", App.EVENT.getDomain(session));//支持多域名
		if(x == null){
			x = 0d;
			y = 0d;
			mapt = 2;
		}
		if(isEdit == null || "".equals(isEdit)) {
			isEdit = "true";
		}
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("gridId", gridId==null?defaultGridInfo.get(KEY_START_GRID_ID):gridId);
		map.put("x", String.valueOf(x));
		map.put("y", String.valueOf(y));
		map.put("mapt", mapt);
		map.put("callBackUrl", callBackUrl);
		map.put("isEdit", isEdit);
		map.put("mapType", mapType);
		return "/component/popMapMarkerSelectorCrossDomainNew.ftl";
	}
	
	@RequestMapping(value="/outPlatCrossDomainCallBack")
	public String outPlatCrossDomainCallBack(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="x", required=false) Double x
			,@RequestParam(value="y", required=false) Double y
			,@RequestParam(value="gridId", required=false) Long gridId
			,@RequestParam(value="mapt", required=false) Integer mapt) throws Exception{
		String forward = "/component/gis_cross_domain_back.ftl";
		map.put("gridId", gridId);
		map.put("x", String.valueOf(x));
		map.put("y", String.valueOf(y));
		map.put("mapt", mapt);
		return forward;
	}
	/**
	 * 带图层地图
	 * @param session
	 * @param request
	 * @param map
	 * @param cat
	 * @param gridId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/gisDemo")
	public String gisDemo(HttpSession session, HttpServletRequest request, ModelMap map
			, @RequestParam(value="cat", required=false) String cat, @RequestParam(value="gridId", required=false) Long gridId) throws Exception{
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		Long startGridId = gridInfo.getGridId();
		//查询当前登录人员信息域所配置的地图信息
		GisMapConfigInfo mapConfig = gisInfoService.findGisMapConfigInfoByOrgCode(gridInfo.getInfoOrgCode());
		if(mapConfig==null) throw new Exception("未找到该网格组织对应的地图配置信息！！");
		//地图类型二维、三维
		Integer start_mapt = mapConfig.getMap_start_type();
		Integer map_type = mapConfig.getMap_type();
		Integer map_type_3D = mapConfig.getMap_type_3D();
		boolean is3dMap = (start_mapt==4 && start_mapt==20) ? true: false;
		//地图主题：表示用的是属于哪个区域的地图，如：sosp_haicang
		String skin = mapConfig.getTheme();
		//二维地图路径：表示从哪个路径加载二维地图，如：/xzmap/%pic.jpg
		String map_file = mapConfig.getMap_file()!=null? mapConfig.getMap_file():"";
		//三维地图路径：表示从哪个路径加载三维地图，如：/%pic.jpg
		String map_file_3D = mapConfig.getMap_file_3D()!=null? mapConfig.getMap_file_3D():"";
		//获取地图中心位置坐标
		Float gis_map_center_x = mapConfig.getMap_center_x()!=null?mapConfig.getMap_center_x():0;
		Float gis_map_center_y = mapConfig.getMap_center_y()!=null?mapConfig.getMap_center_y():0;
		//2013-10-28 liush add 当页面进行二维三维地图切换时没有更新中心点坐标需要更新中心点坐标
		Float gis_map_center_x_3D = mapConfig.getMap_center_x_3D()!=null?mapConfig.getMap_center_x_3D():0;
		Float gis_map_center_y_3D = mapConfig.getMap_center_y_3D()!=null?mapConfig.getMap_center_y_3D():0;
		//2013-10-28 liush add 最大最小缩放比例
		Integer gis_map_minl = mapConfig.getMap_minl()!=null?mapConfig.getMap_minl():0;
		Integer gis_map_maxl = mapConfig.getMap_maxl()!=null?mapConfig.getMap_maxl():0;
		Integer gis_map_minl_3D = mapConfig.getMap_minl_3D()!=null?mapConfig.getMap_minl_3D():0;
		Integer gis_map_maxl_3D = mapConfig.getMap_maxl_3D()!=null?mapConfig.getMap_maxl_3D():0;
		//2013-10-29 liush add 轨迹定位偏移量
		Float position_offset_x = mapConfig.getPosition_offset_x()!=null?mapConfig.getPosition_offset_x():0;
		Float position_offset_y = mapConfig.getPosition_offset_y()!=null?mapConfig.getPosition_offset_y():0;
		Float position_offset_x_3D = mapConfig.getPosition_offset_x_3D()!=null?mapConfig.getPosition_offset_x_3D():0;
		Float position_offset_y_3D = mapConfig.getPosition_offset_x_3D()!=null?mapConfig.getPosition_offset_y_3D():0;
		
		//地图显示级别
		Integer gis_map_center_level = mapConfig.getMap_center_level()!=null?mapConfig.getMap_center_level():0;
		//2013-10-28 liush add
		Integer gis_map_center_level_3D = mapConfig.getMap_center_level_3D()!=null?mapConfig.getMap_center_level_3D():0;
		//通过网格ID获取，获取该地图类型的对应的地图信息.2013-10-28 liush add 将二维三维地图都查询出来
		GisMapInfo gridMapInfo = gisInfoService.findGisMapInfoByGridId(startGridId, mapConfig.getMap_type());
		if(gridMapInfo!=null){
			gis_map_center_x = gridMapInfo.getX();
			gis_map_center_y = gridMapInfo.getY();
//			gis_map_center_level = gridMapInfo.getLevel();
		}
		GisMapInfo gridMapInfo3D = gisInfoService.findGisMapInfoByGridId(startGridId, mapConfig.getMap_type_3D());
		if(gridMapInfo3D!=null){
			gis_map_center_x_3D = gridMapInfo3D.getX();
			gis_map_center_y_3D = gridMapInfo3D.getY();
//			gis_map_center_level = gridMapInfo.getLevel();
		}
		
		map.addAttribute("projectPath", skin);
		map.addAttribute("sys_pathMap_index", skin);
		map.addAttribute("sys_pathMap_default", skin);
		map.addAttribute("map_pe_dialog_width", "305");
		map.addAttribute("map_renthous_dialog_width", "249");
		map.addAttribute("ini_mpt", start_mapt);
		map.addAttribute("map_wggly", start_mapt);
		map.addAttribute("gis_map_type", start_mapt);
		map.addAttribute("map_type", map_type);
		map.addAttribute("map_type_3D", map_type_3D);
		//中心点坐标
		map.addAttribute("gis_map_center_x", gis_map_center_x.toString());
		map.addAttribute("gis_map_center_y", gis_map_center_y.toString());
		map.addAttribute("gis_map_center_x_3D", gis_map_center_x_3D.toString());
		map.addAttribute("gis_map_center_y_3D", gis_map_center_y_3D.toString());
		//地图缩放范围
		map.addAttribute("gis_map_minl", gis_map_minl.toString());
		map.addAttribute("gis_map_maxl", gis_map_maxl.toString());
		map.addAttribute("gis_map_minl_3D", gis_map_minl_3D.toString());
		map.addAttribute("gis_map_maxl_3D", gis_map_maxl_3D.toString());
		//轨迹定位偏移量
		map.addAttribute("position_offset_x", position_offset_x.toString());
		map.addAttribute("position_offset_y", position_offset_y.toString());
		map.addAttribute("position_offset_x_3D", position_offset_x_3D.toString());
		map.addAttribute("position_offset_y_3D", position_offset_y_3D.toString());
		
		map.addAttribute("gis_map_center_level", gis_map_center_level.toString());
		map.addAttribute("gis_map_center_level_3D", gis_map_center_level_3D.toString());
		
		map.addAttribute("map_gj_locl", "01");
		map.addAttribute("call_type", "01");
		map.addAttribute("m3dmap_url", map_file_3D);
		map.addAttribute("mgoogle_hangpai_map_url", map_file);
		
		map.addAttribute("gridCode", gridInfo.getInfoOrgCode());
		//屏蔽平潭的轮渡和台胞人员
		if(gridInfo.getInfoOrgCode().length()>=6&&"350113".equals(gridInfo.getInfoOrgCode().substring(0,6))){
			map.addAttribute("ptgridCode", "ptgridCode");//平潭过滤
		}
		if(gridInfo.getInfoOrgCode().length()>=6&&"350582".equals(gridInfo.getInfoOrgCode().substring(0,6))){
			map.addAttribute("jjgridCode", "jjgridCode");//晋江过滤
		}
		if(gridInfo.getInfoOrgCode().startsWith(ConstantValue.SIMING_FUNC_ORG_CODE)){
			map.addAttribute("smgridCode", "smgridCode");//厦门市思明区过滤
		}
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String orgCode = (String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		Long orgId = (Long)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_ID);
		Long userSelfGridId = (Long)defaultGridInfo.get(KEY_START_GRID_ID);
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("orgId", orgId);
		map.addAttribute("userSelfGridId", userSelfGridId.toString());
		map.addAttribute("gridId", startGridId.toString());
		map.addAttribute("gridName", gridInfo.getGridName());
		map.addAttribute("gridLevel", gridInfo.getGridLevel());
		
		map.addAttribute("skin", skin);
		map.addAttribute("appPath", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath());
		//map.addAttribute("mapBasePath", ConstantValue.GIS_MAP_BASE_PATH);
		map.addAttribute("mapBasePath", App.OLD_GIS_MAP.getDomain(session));//支持多个域名访问综治网格		
		
		//map.addAttribute("PLATFORM_DOMAIN_ROOT", ConstantValue.PLATFORM_DOMAIN_ROOT);
		map.addAttribute("PLATFORM_DOMAIN_ROOT", App.TOP.getDomain(session));
		map.addAttribute("RESIDENT_DOMAIN", App.RS.getDomain(session));
		//map.addAttribute("GIS_MAP_GANSU_URL", getNewUrl(session, ConstantValue.GIS_MAP_GANSU_URL));//支持多个域名访问综治网格	
		map.addAttribute("GIS_MAP_GANSU_URL", App.EVENT.getDomain(session));
		map.put("POPULATION_URL", App.RS.getDomain(session));
		String gisDemoPath = "/zzgl_map/gis/"+skin+"/";
		if("jcsj".equals(cat)){
			if(userInfo.getOrgCode().startsWith(ConstantValue.SHISHI_FUNC_ORG_CODE)) {
				return gisDemoPath+"GisDemo_jcsj_shishi.ftl";
			}
			if(userInfo.getOrgCode().startsWith(ConstantValue.JINJIANG_FUNC_ORG_CODE)) {
				return gisDemoPath+"GisDemo_jcsj_jinjiang.ftl";
			}
			return gisDemoPath+"GisDemo_jcsj.ftl";
		}else if("zhdd".equals(cat)){
			//字典图层事件类型显示
			List<BaseDataDict> bigTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.BIG_TYPE_PCODE, userInfo.getOrgCode());
			map.addAttribute("bigTypeDictList", bigTypeDictList);
			//String isCallIn = ReadProperties.javaLoadProperties("yuyin.callIn", session, "global.properties");
			boolean isCallIn = false;
			String yuyinOrgCodeStr = ReadProperties.javaLoadProperties("yuyin.org_code", session, "global.properties");
			if(!StringUtils.isBlank(yuyinOrgCodeStr)){
				String[] yuyinOrgCodes = yuyinOrgCodeStr.split(",");
				for(String yuyinOrgCode : yuyinOrgCodes){
					if(gridInfo.getInfoOrgCode().startsWith(yuyinOrgCode)){
						isCallIn = true;
						break;
					}
				}
			}
			map.addAttribute("isCallIn", String.valueOf(isCallIn));
			return gisDemoPath+"GisDemo_zhdd.ftl";
		}else if("ztyw".equals(cat)){
			//根据orgCode判断是否显示相关列表
			map.addAttribute("orgCode", userInfo.getOrgCode());//350206-同安
			return gisDemoPath+"GisDemo_ztyw.ftl";
		}else if("gzrw".equals(cat)) {
			return gisDemoPath+"GisDemo_gzrw.ftl";
		}else if("taijiang".equals(cat)) {
//			UserCookie userCookie = (UserCookie) session.getAttribute(ConstantValue.USER_COOKIE_IN_SESSION);
//			OrgSocialInfoBO nowOrg =orgSocialInfoOutService.findById(userInfo.getOrgId());//当前组织
//			List<Menu> menuList=(List<Menu>)menuOutService.queryDisplayMemuByCondition(userInfo.getUserId(), nowOrg.getOrgCode(), nowOrg.getOrgId(), userCookie.getPositionId());
//			map.addAttribute("menuList", menuList);
			map.addAttribute("oauthUrl", App.UAM.getDomain(session));
			return gisDemoPath+"GisDemo_taijiang.ftl";
		}else if("fuzhou".equals(cat)) {
//			return gisDemoPath+"fuzhou.ftl";
			return gisDemoPath+"GisDemo_fuzhou.ftl";
		}else if("putian".equals(cat)) {
			return "/zzgl_map/gis/sosp_putian/GisDemo_putian.ftl";
		}else if("test".equals(cat)) {
			return gisDemoPath+"GisDemo_jcsj_test.ftl";
		} else if ("xibin".equals(cat)) {
			UserCookie userCookie = (UserCookie) session.getAttribute(ConstantValue.USER_COOKIE_IN_SESSION);
			OrgSocialInfoBO nowOrg =orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());//当前组织
			List<Menu> menuList=(List<Menu>)menuOutService.queryDisplayMemuByCondition(userInfo.getUserId(), nowOrg.getOrgCode(), nowOrg.getOrgId(), null);
			map.addAttribute("menuList", menuList);
			map.addAttribute("oauthUrl", App.UAM.getDomain(session));
			return gisDemoPath+"GisDemo_xibin.ftl";
		} else if("jinjiang".equals(cat)) {
			return gisDemoPath+"GisDemo_jcsj_jinjiang.ftl";
		} else if("jinjiangSafetyPersonManage".equals(cat)) {
			return gisDemoPath+"GisDemo_jcsj_jinjiang_safetyPersonManage.ftl";
		} else if("xingwang".equals(cat)) {
			return "/zzgl_map/gis/sosp_xingwang/GisDemo_xingwang.ftl";
		}else if("standardmappage".equals(cat)) {
			return "/zzgl_map/gis/standardmappage/standardmappageindex.ftl";
		}else if("001".equals(cat)||"002".equals(cat)||"003".equals(cat)||"004".equals(cat)||"005".equals(cat)||"006".equals(cat)||"007".equals(cat)||"008".equals(cat)){
			map.addAttribute("WATER_MONITOR_URL", App.WATER.getDomain(session));
			map.addAttribute("eyesType", cat);
			return gisDemoPath+"GisDemo_spjk.ftl";
		}else if("zhjd_ms".equals(cat)){
			return gisDemoPath+"GisDemo_zhjd_ms.ftl";
		}else if("zhjd_dy".equals(cat)){
			return gisDemoPath+"GisDemo_zhjd_dy.ftl";
		}else if("zhjd_fkqy".equals(cat)){
			return gisDemoPath+"GisDemo_zhjd_fkqy.ftl";
		}else if("minhouxiaofang".equals(cat)){
			return gisDemoPath+"GisDemo_minhouxiaofang.ftl";
		}else if("hcanjian".equals(cat)){
			return gisDemoPath+"GisDemo_haicanganjian.ftl";
		}else if("new_hcanjian".equals(cat)){
			return gisDemoPath+"GisDemo_new_hcanjian.ftl";
		}else{
			return gisDemoPath+"GisDemo.ftl";
		}
		
	}
	/**
	 * 2015-05-20 liushi add 
	 * 旧地图可配置首页连接
	 * @param session
	 * @param request
	 * @param map
	 * @param homePageType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toGisConfigIndex")
	public String toGisConfigIndex(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="homePageType", required=true) String homePageType) throws Exception{
		map.addAttribute("appPath", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath());
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String startOrgCode = (String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		map.addAttribute("mapBasePath", App.OLD_GIS_MAP.getDomain(session));//支持多个域名访问综治网格
		map.put("gridId", gridInfo.getGridId());
		map.put("gridCode", gridInfo.getGridCode());
		map.put("gridName", gridInfo.getGridName());
		map.put("gridLevel", gridInfo.getGridLevel());
		map.put("orgCode", startOrgCode);//
		map.put("socialOrgCode", userInfo.getOrgCode());//信息域组织
		map.put("homePageType", homePageType);
		map.put("ZHSQ_EVENT_DOMAIN", App.EVENT.getDomain(session));
		map.addAttribute("iframeCloseCallBack", ConstantValue.closeCallBack);
		map.addAttribute("iframeUrl", App.ZZGRID.getDomain(session)+ConstantValue.iframeUrl2);
		return "/zzgl_map/gis_config/gis_config_index.ftl";
	}
	@RequestMapping(value="/toMapTree")
	public String toMapTree(HttpSession session, HttpServletRequest request, ModelMap map) throws Exception{
		String forward = "/zzgl_map/gis_config/grid_tree_standard.ftl";
		return forward;
	}
	
	
	@RequestMapping(value="/getMapConfig")
	public @ResponseBody Map<String, Object> getMapConfig(HttpSession session, HttpServletRequest request, ModelMap map) throws Exception{
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		Long startGridId = gridInfo.getGridId();
		//查询当前登录人员信息域所配置的地图信息
		GisMapConfigInfo mapConfig = gisInfoService.findGisMapConfigInfoByOrgCode(gridInfo.getInfoOrgCode());
		if(mapConfig==null) throw new Exception("未找到该网格组织对应的地图配置信息！！");
		GisMapConfigInfoOfType mapConfigOfType2 = new GisMapConfigInfoOfType();
		GisMapConfigInfoOfType mapConfigOfType3 = new GisMapConfigInfoOfType();
		if(mapConfig.getMap_file() != null && !"".equals(mapConfig.getMap_file())) {
			mapConfigOfType2.setOrgCode(mapConfig.getOrgCode());
			mapConfigOfType2.setMapFileUrl(mapConfig.getMap_file());
			mapConfigOfType2.setMapCenterX(mapConfig.getMap_center_x());
			mapConfigOfType2.setMapCenterY(mapConfig.getMap_center_y());
			mapConfigOfType2.setMapCenterLevel(mapConfig.getMap_center_level());
			mapConfigOfType2.setMapType(mapConfig.getMap_type());
			mapConfigOfType2.setMapTypeName("二维");
			mapConfigOfType2.setMapMinl(mapConfig.getMap_minl());
			mapConfigOfType2.setMapMaxl(mapConfig.getMap_maxl());
			mapConfigOfType2.setPositionOffsetX(mapConfig.getPosition_offset_x());
			mapConfigOfType2.setPositionOffsetY(mapConfig.getPosition_offset_y());
		}else {
			mapConfigOfType2 = null;
		}
		if(mapConfig.getMap_file_3D() != null && !"".equals(mapConfig.getMap_file_3D())) {
			mapConfigOfType3.setOrgCode(mapConfig.getOrgCode());
			mapConfigOfType3.setMapFileUrl(mapConfig.getMap_file_3D());
			mapConfigOfType3.setMapCenterX(mapConfig.getMap_center_x_3D());
			mapConfigOfType3.setMapCenterY(mapConfig.getMap_center_y_3D());
			mapConfigOfType3.setMapCenterLevel(mapConfig.getMap_center_level_3D());
			mapConfigOfType3.setMapType(mapConfig.getMap_type_3D());
			mapConfigOfType3.setMapTypeName("三维");
			mapConfigOfType3.setMapMinl(mapConfig.getMap_minl_3D());
			mapConfigOfType3.setMapMaxl(mapConfig.getMap_maxl_3D());
			mapConfigOfType3.setPositionOffsetX(mapConfig.getPosition_offset_x_3D());
			mapConfigOfType3.setPositionOffsetY(mapConfig.getPosition_offset_y_3D());
		}else {
			mapConfigOfType2 = null;
		}
		
		List<GisMapConfigInfoOfType> list = new ArrayList<GisMapConfigInfoOfType>();
		if(mapConfig.getMap_start_type()==2 || mapConfig.getMap_start_type() == 1) {
			if(mapConfigOfType2 != null) {
				list.add(mapConfigOfType2);
			}
			if(mapConfigOfType3 != null) {
				list.add(mapConfigOfType3);
			}
		}else if(mapConfig.getMap_start_type()==4 || mapConfig.getMap_start_type() == 20) {
			if(mapConfigOfType3 != null) {
				list.add(mapConfigOfType3);
			}
			if(mapConfigOfType2 != null) {
				list.add(mapConfigOfType2);
			}
		}
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("mapConfigs", list);
		return result;
	}
	
	/**
	 * 2015-06-01 liushi 获取配置的图层信息
	 * @param session
	 * @param request
	 * @param map
	 * @param orgCode
	 * @param homePageType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getGisDataCfgRelationTreeVersionNoe")
	public Map<String, Object> getGisDataCfgRelationTreeVersionNoe(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = true) String orgCode
			,@RequestParam(required = true) String homePageType){
		Long gdcId = 5L;
		GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgRelationTreeVersionNoe(orgCode,homePageType, gdcId);
		if(gisDataCfg !=null &&gisDataCfg.getChildrenList()!=null&& gisDataCfg.getChildrenList().size()>0) {
			this.transGisDataCfgUrl(gisDataCfg.getChildrenList(), session);
		}
		Map<String, Object> resultmap = new HashMap<String,Object>();
		resultmap.put("gisDataCfg", gisDataCfg);
		return resultmap;
	}
	/**
	 * 
	 * @param session
	 * @param request
	 * @param map
	 * @param orgCode
	 * @param homePageType
	 * @param gdcId 根节点5
	 * @param isRootSearch
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getGisDataCfgRelationTreeVersionTwo")
	public Map<String, Object> getGisDataCfgRelationTreeVersionTwo(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = true) String orgCode
			,@RequestParam(required = true) String homePageType
			,@RequestParam(required = true) Long gdcId
			,@RequestParam(required = true) Integer isRootSearch){
		GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgRelationTreeVersionTwo(orgCode,homePageType, gdcId,isRootSearch);
		if(gisDataCfg !=null &&gisDataCfg.getChildrenList()!=null&& gisDataCfg.getChildrenList().size()>0) {
			this.transGisDataCfgUrl(gisDataCfg.getChildrenList(), session);
		}
		Map<String, Object> resultmap = new HashMap<String,Object>();
		resultmap.put("gisDataCfg", gisDataCfg);
		return resultmap;
	}
	public void transGisDataCfgUrl(List<GisDataCfg> gisDataCfgList,HttpSession session) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		for(GisDataCfg obj:gisDataCfgList) {
			if(obj != null) {
				String menuListUrl = obj.getMenuListUrl();
				String menuSummaryUrl = obj.getMenuSummaryUrl();
				String menuDetailUrl = obj.getMenuDetailUrl();
				String elementsCollectionStr  = obj.getElementsCollectionStr();
				String callBack  = obj.getCallBack();
				if(menuListUrl != null && !"".equals(menuListUrl)) {
					String menuListUrlDomain = menuListUrl.split("/")[0];
					String actMenuListUrlDomain = CommonFunctions.getDomain(session, menuListUrlDomain);
					
					if (StringUtils.isBlank(actMenuListUrlDomain)) {
						actMenuListUrlDomain = funConfigurationService.turnCodeToValue("APP_DOMAIN", menuListUrlDomain,
								IFunConfigurationService.CFG_TYPE_URL, userInfo.getOrgCode(),
								IFunConfigurationService.CFG_ORG_TYPE_0);
					}
					
					menuListUrlDomain = "\\" + menuListUrlDomain;
					menuListUrl = menuListUrl.replaceFirst(menuListUrlDomain, actMenuListUrlDomain);
					elementsCollectionStr = elementsCollectionStr.replaceAll(menuListUrlDomain, actMenuListUrlDomain);
					callBack = callBack.replaceAll(menuListUrlDomain, actMenuListUrlDomain);
				}
				if(menuSummaryUrl != null && !"".equals(menuSummaryUrl)) {
					String menuSummaryUrlDomain = menuSummaryUrl.split("/")[0];
					String actMenuSummaryUrlDomain = CommonFunctions.getDomain(session, menuSummaryUrlDomain);
					
					if (StringUtils.isBlank(actMenuSummaryUrlDomain)) {
						actMenuSummaryUrlDomain = funConfigurationService.turnCodeToValue("APP_DOMAIN",
								menuSummaryUrlDomain, IFunConfigurationService.CFG_TYPE_URL, userInfo.getOrgCode(),
								IFunConfigurationService.CFG_ORG_TYPE_0);
					}
					
					menuSummaryUrlDomain = "\\" + menuSummaryUrlDomain;
					menuSummaryUrl = menuSummaryUrl.replaceFirst(menuSummaryUrlDomain, actMenuSummaryUrlDomain);
					elementsCollectionStr = elementsCollectionStr.replaceAll(menuSummaryUrlDomain,
							actMenuSummaryUrlDomain);
					callBack = callBack.replaceAll(menuSummaryUrlDomain, actMenuSummaryUrlDomain);
				}
				if(menuDetailUrl != null && !"".equals(menuDetailUrl)) {
					String menuDetailUrlDomain = menuDetailUrl.split("/")[0];
					String actMenuDetailUrlDomain = CommonFunctions.getDomain(session, menuDetailUrlDomain);
					
					if (StringUtils.isBlank(actMenuDetailUrlDomain)) {
						actMenuDetailUrlDomain = funConfigurationService.turnCodeToValue("APP_DOMAIN",
								menuDetailUrlDomain, IFunConfigurationService.CFG_TYPE_URL, userInfo.getOrgCode(),
								IFunConfigurationService.CFG_ORG_TYPE_0);
					}
					
					menuDetailUrlDomain = "\\" + menuDetailUrlDomain;
					menuDetailUrl = menuDetailUrl.replaceFirst(menuDetailUrlDomain, actMenuDetailUrlDomain);
					elementsCollectionStr = elementsCollectionStr.replaceAll(menuDetailUrlDomain,
							actMenuDetailUrlDomain);
					callBack = callBack.replaceAll(menuDetailUrlDomain, actMenuDetailUrlDomain);
				}
				obj.setMenuListUrl(menuListUrl);
				obj.setMenuSummaryUrl(menuSummaryUrl);
				obj.setMenuDetailUrl(menuDetailUrl);
				obj.setElementsCollectionStr(elementsCollectionStr);
				obj.setCallBack(callBack);
				if(obj.getChildrenList()!=null && obj.getChildrenList().size()>0) {
					this.transGisDataCfgUrl(obj.getChildrenList(),session);
				}
			}
		}
				
	}
	
	@ResponseBody
	@RequestMapping(value="/getGisDataCfgTree")
	public List<ZTreeNode> getGisDataCfgTree(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = false) Long gdcPid){
		if(gdcPid == null || gdcPid == 0) {//表示根节点
			gdcPid = 1L;
		}
		String orgCode = request.getParameter("orgCode");
		String homePageType = request.getParameter("homePageType");
		if(StringUtils.isBlank(homePageType)){
			homePageType = "ARCGIS_STANDARD_HOME";
		}
		
		String uiDomain = App.UI.getDomain(session);
		List<ZTreeNode> treeNodes = new ArrayList<ZTreeNode>();
		List<GisDataCfg> gisDataCfgs = menuConfigService.getGisDataCfgTree(orgCode, homePageType);
		
		this.transGisDataCfgUrl(gisDataCfgs, session);
		
		for (GisDataCfg gisDataCfg : gisDataCfgs) {
			ZTreeNode node = new ZTreeNode();
			String callBack = gisDataCfg.getCallBack();
			
			if (StringUtils.isNotBlank(callBack) && callBack.indexOf("classificationClick") == -1) {
				node.setCallBack(gisDataCfg.getCallBack());
			}
			
			node.setElementsCollectionStr(gisDataCfg.getElementsCollectionStr());
			node.setOrgCode(gisDataCfg.getOrgCode());
			node.setLayCfgId(gisDataCfg.getGdcId());
			node.setId(gisDataCfg.getGdcId());
			node.setpId(gisDataCfg.getGdcPid());
			node.setName(gisDataCfg.getMenuName());
			node.setIcon(uiDomain + gisDataCfg.getLargeIco());
			treeNodes.add(node);
		}
		
		return treeNodes;
	}
	
	@ResponseBody
	@RequestMapping(value="/getDisplayStyle")
	public String getDisplayStyle(HttpSession session, HttpServletRequest request, ModelMap map) {
		String orgCode = request.getParameter("orgCode");
		String homePageType = request.getParameter("homePageType");
		if(StringUtils.isBlank(homePageType)){
			homePageType = "ARCGIS_STANDARD_HOME";
		}
		
		return menuConfigService.getDisplayStyle(orgCode, homePageType);
	}
	
	@RequestMapping(value="/toZhouBianConfig")
	public String toKuangXuanConfig(HttpSession session, ModelMap map,HttpServletRequest request
			,@RequestParam(value="mapt") Integer mapt
			,@RequestParam(value="x") Double x
			,@RequestParam(value="y") Double y
			,@RequestParam(value="homePageType", required=false) String homePageType
			,@RequestParam(value="socialOrgCode", required=false) String socialOrgCode)throws Exception{
		map.addAttribute("x", String.valueOf(x));
		map.addAttribute("y", String.valueOf(y));
		map.addAttribute("mapt", mapt);
		map.addAttribute("homePageType", homePageType);
		map.addAttribute("socialOrgCode", socialOrgCode);
		map.addAttribute("ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		return "/zzgl_map/zhoubian/zhoubian_config.ftl";
	}
	
	/**
	 * 2015-8-7 liushi add 跳转到框选统计项配置页面
	 * @param session
	 * @param map
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toKuangXuanConfig")
	public String toKuangXuanConfig(HttpSession session, ModelMap map,HttpServletRequest request
			,@RequestParam(value="homePageType", required=false) String homePageType
			,@RequestParam(value="socialOrgCode", required=false) String socialOrgCode)throws Exception{
		map.addAttribute("homePageType", homePageType);
		map.addAttribute("socialOrgCode", socialOrgCode);
		map.addAttribute("ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		return "/zzgl_map/kuangxuan/kuangxuan_config.ftl";
	}
	/**
	 * 2015-8-17 liushi add 可配置的框选统计
	 * @param session
	 * @param map
	 * @param request
	 * @param geoString
	 * @param mapt
	 * @param kuangxuanTypeStr
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getKuangXuanStatData")
	public String getKuangXuanStatData(HttpSession session, ModelMap map,HttpServletRequest request
			,@RequestParam(value="geoString") String geoString
			,@RequestParam(value="mapt") String mapt
			,@RequestParam(value="kuangxuanTypeStr", required=false) String kuangxuanTypeStr
			,@RequestParam(value="menuNameStr", required=false) String menuNameStr
			,@RequestParam(value="orgCode") String orgCode)throws Exception
	{
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long startGridId=Long.parseLong(defaultGridInfo.get(KEY_START_GRID_ID).toString());
		String infoOrgCode = (String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		menuNameStr = java.net.URLDecoder.decode(menuNameStr, "utf-8");
		String[] kuangxuanTypes = kuangxuanTypeStr.split(",");

		String[] menuNames = menuNameStr.split(",");
		Map<String, List<String>> result = new LinkedHashMap<String, List<String>>();;
		List<String> values = null;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resName", null);
		params.put("gridname", null);
		params.put("startGridId", startGridId);
		params.put("infoOrgCode", orgCode);
		for(int i=0;i<kuangxuanTypes.length;i++){
			params.put("geoString", geoString);
			params.put("mapType", mapt);
			params.put("kuangxuanType", kuangxuanTypes[i]);
			//int resultCount = this.kuangXuanStatService.statOfKuangXuan(params);
			//map.addAttribute(kuangxuanTypes[i]+"Count", resultCount);
			values = new ArrayList<String>();
			values.add(menuNames[i]);
			//values.add(resultCount+"");
			result.put(kuangxuanTypes[i], values);
		}
		map.addAttribute("geoString", geoString);
		map.addAttribute("mapType", mapt);
		map.addAttribute("kuangxuanTypes", kuangxuanTypes);
		map.addAttribute("result", result);
		return "/zzgl_map/kuangxuan/kuangxuan_config_info.ftl";
	}
	@ResponseBody
	@RequestMapping(value="/getClearLyerNames")
	public Map<String, Object> getClearLyerNames(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = true) String homePageType){
		Long gdcId = 5L;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String infoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		String layerNameStr = this.menuConfigService.getGisDataLayerNameVersionNoe(infoOrgCode,homePageType, gdcId);
		Map<String, Object> resultmap = new HashMap<String,Object>();
		resultmap.put("layerNameStr", layerNameStr);
		return resultmap;
	}
	
	
	
	
	
	
	@RequestMapping(value="/getDisplayMemu")
	public @ResponseBody Map<String, Object> getDisplayMemu(HttpSession session, ModelMap map, HttpServletResponse res, HttpServletRequest request) throws Exception{
		Map result = new HashMap<String,Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		UserCookie userCookie = (UserCookie) session.getAttribute(ConstantValue.USER_COOKIE_IN_SESSION);
		OrgSocialInfoBO nowOrg =orgSocialInfoOutService.findByOrgId(userInfo.getOrgId());//当前组织
		List<Menu> menuList=(List<Menu>)menuOutService.queryDisplayMemuByCondition(userInfo.getUserId(), nowOrg.getOrgCode(), nowOrg.getOrgId(), null);
		int menuCount = menuList.size();
		result.put("menuCount", menuCount);
		result.put("menuList", menuList);
		return result;
	}
	/**
	 * 基础地图（该方法，属于公用的组件，非公用功能，请勿修改）
	 * @param session
	 * @param map
	 * @param request
	 * @param gridId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/gisMap")
	public String gisMap(HttpSession session, ModelMap map, HttpServletRequest request, @RequestParam(value="gridId", required=false) Long gridId
			, @RequestParam(value="isEdit", required=false) String isEdit
			, @RequestParam(value="mapType", required=false) String mapType) throws Exception {
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		Long startGridId = gridInfo.getGridId();
		
		GisMapConfigInfo mapConfig = gisInfoService.findGisMapConfigInfoByOrgCode(gridInfo.getInfoOrgCode());
		if(mapConfig==null) throw new Exception("未找到该网格组织对应的地图配置信息！！");
		
		Integer start_mapt = mapConfig.getMap_start_type();
		Integer map_type = mapConfig.getMap_type();
		Integer map_type_3D = mapConfig.getMap_type_3D();
		boolean is3dMap = (start_mapt==4 && start_mapt==20) ? true: false;

		String map_file = mapConfig.getMap_file()!=null? mapConfig.getMap_file():"";
		String map_file_3D = mapConfig.getMap_file_3D()!=null? mapConfig.getMap_file_3D():"";
		//2013-11-11 liush add 中心点位坐标
		Float gis_map_center_x = mapConfig.getMap_center_x() != null?mapConfig.getMap_center_x():0;
		Float gis_map_center_x_3D = mapConfig.getMap_center_x_3D()!=null?mapConfig.getMap_center_x_3D():0;
		Float gis_map_center_y = mapConfig.getMap_center_y()!=null?mapConfig.getMap_center_y():0;
		Float gis_map_center_y_3D = mapConfig.getMap_center_y_3D()!=null?mapConfig.getMap_center_y_3D():0;
		
		//2013-10-28 liush add 最大最小缩放比例
		Integer gis_map_minl = mapConfig.getMap_minl()!=null?mapConfig.getMap_minl():0;
		Integer gis_map_maxl = mapConfig.getMap_maxl()!=null?mapConfig.getMap_maxl():0;
		Integer gis_map_minl_3D = mapConfig.getMap_minl_3D()!=null?mapConfig.getMap_minl_3D():0;
		Integer gis_map_maxl_3D = mapConfig.getMap_maxl_3D()!=null?mapConfig.getMap_maxl_3D():0;
		//2013-10-29 liush add 轨迹定位偏移量
//		Integer position_offset_num = mapConfig.getPosition_offset_num()!=null?mapConfig.getPosition_offset_num():0;
//		Integer position_offset_num_3D = mapConfig.getPosition_offset_num_3D()!=null?mapConfig.getPosition_offset_num_3D():0;
		
		//地图显示级别
		Integer gis_map_center_level = mapConfig.getMap_center_level()!=null?mapConfig.getMap_center_level():0;
		//2013-10-28 liush add
		Integer gis_map_center_level_3D = mapConfig.getMap_center_level_3D()!=null?mapConfig.getMap_center_level_3D():0;
		GisMapInfo gridMapInfo = gisInfoService.findGisMapInfoByGridId(startGridId, mapConfig.getMap_type());
		if(gridMapInfo!=null){
			gis_map_center_x = gridMapInfo.getX();
			gis_map_center_y = gridMapInfo.getY();
//			gis_map_center_level = gridMapInfo.getLevel();
		}
		GisMapInfo gridMapInfo3D = gisInfoService.findGisMapInfoByGridId(startGridId, mapConfig.getMap_type_3D());
		if(gridMapInfo3D!=null){
			gis_map_center_x_3D = gridMapInfo3D.getX();
			gis_map_center_y_3D = gridMapInfo3D.getY();
//			gis_map_center_level = gridMapInfo.getLevel();
		}
		if(gis_map_center_level==null || "".equals(gis_map_center_level)) gis_map_center_level = 1;
		
		String startGridName = gridInfo.getGridName();
		Long starGridId = gridInfo.getGridId();
		Integer gridLevel = gridInfo.getGridLevel();
		
		String projectPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		
		map.addAttribute("startGridId", starGridId.toString());
		map.addAttribute("startGridOrgCode", gridInfo.getInfoOrgCode());
		map.addAttribute("startGridName", startGridName);
		map.addAttribute("gridLevel", gridLevel);
		
		map.addAttribute("projectPath", projectPath);
//		map.addAttribute("map_host", projectPath+"/gis");
		//map.addAttribute("map_host", ConstantValue.GIS_MAP_BASE_PATH+"/gis"); //调试环境
		map.addAttribute("map_host", App.OLD_GIS_MAP.getDomain(session)+"/gis");//支持多个域名访问综治网格	
		map.addAttribute("start_mapt", start_mapt);
		map.addAttribute("map_type", map_type);
		map.addAttribute("map_type_3D", map_type_3D);
		
		//中心点坐标
		map.addAttribute("map_center_x", gis_map_center_x.toString());
		map.addAttribute("map_center_y", gis_map_center_y.toString());
		map.addAttribute("map_center_x_3D", gis_map_center_x_3D.toString());
		map.addAttribute("map_center_y_3D", gis_map_center_y_3D.toString());
		
		//地图缩放范围
		map.addAttribute("gis_map_minl", gis_map_minl.toString());
		map.addAttribute("gis_map_maxl", gis_map_maxl.toString());
		map.addAttribute("gis_map_minl_3D", gis_map_minl_3D.toString());
		map.addAttribute("gis_map_maxl_3D", gis_map_maxl_3D.toString());
		
		map.addAttribute("map_center_level", gis_map_center_level);
		map.addAttribute("map_center_level_3D", gis_map_center_level_3D);
		map.addAttribute("map_file", map_file);
		map.addAttribute("map_file_3D", map_file_3D);
		if("".equals(mapType)) {
			if("2".equals(mapConfig.getMap_start_type())) {
				mapType = "2";
			}else if("4".equals(mapConfig.getMap_start_type())|| "20".equals(mapConfig.getMap_start_type())) {
				mapType = "3";
			}
		}else if(mapType == null || "null".equals(mapType)) {
			mapType="2,3";
		}
		map.addAttribute("mapType", mapType);
		if(isEdit == null || "".equals(isEdit)||"null".equals(isEdit)) {
			isEdit = "true";
		}
		map.addAttribute("isEdit", isEdit);
		return "/zzgl_map/gis/GisMap.ftl";
		
	}
	
	@ResponseBody
	@RequestMapping(value="/getGisMapConfigInfo")
	public Map<String,Object> getGisMapConfigInfo(HttpSession session, ModelMap map, HttpServletRequest request, @RequestParam(value="gridId", required=false) Long gridId) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		Long startGridId = gridInfo.getGridId();
		//查询当前登录人员信息域所配置的地图信息
		GisMapConfigInfo mapConfig = gisInfoService.findGisMapConfigInfoByOrgCode(gridInfo.getInfoOrgCode());
		if(mapConfig==null) throw new Exception("未找到该网格组织对应的地图配置信息！！");
		//地图类型二维、三维
		Integer start_mapt = mapConfig.getMap_start_type();
		Integer map_type = mapConfig.getMap_type();
		Integer map_type_3D = mapConfig.getMap_type_3D();
		boolean is3dMap = (start_mapt==4 && start_mapt==20) ? true: false;
		//地图主题：表示用的是属于哪个区域的地图，如：sosp_haicang
		String skin = mapConfig.getTheme();
		//二维地图路径：表示从哪个路径加载二维地图，如：/xzmap/%pic.jpg
		String map_file = mapConfig.getMap_file()!=null? mapConfig.getMap_file():"";
		//三维地图路径：表示从哪个路径加载三维地图，如：/%pic.jpg
		String map_file_3D = mapConfig.getMap_file_3D()!=null? mapConfig.getMap_file_3D():"";
		//获取地图中心位置坐标
		Float gis_map_center_x = mapConfig.getMap_center_x()!=null?mapConfig.getMap_center_x():0;
		Float gis_map_center_y = mapConfig.getMap_center_y()!=null?mapConfig.getMap_center_y():0;
		//2013-10-28 liush add 当页面进行二维三维地图切换时没有更新中心点坐标需要更新中心点坐标
		Float gis_map_center_x_3D = mapConfig.getMap_center_x_3D()!=null?mapConfig.getMap_center_x_3D():0;
		Float gis_map_center_y_3D = mapConfig.getMap_center_y_3D()!=null?mapConfig.getMap_center_y_3D():0;
		//2013-10-28 liush add 最大最小缩放比例
		Integer gis_map_minl = mapConfig.getMap_minl()!=null?mapConfig.getMap_minl():0;
		Integer gis_map_maxl = mapConfig.getMap_maxl()!=null?mapConfig.getMap_maxl():0;
		Integer gis_map_minl_3D = mapConfig.getMap_minl_3D()!=null?mapConfig.getMap_minl_3D():0;
		Integer gis_map_maxl_3D = mapConfig.getMap_maxl_3D()!=null?mapConfig.getMap_maxl_3D():0;
		//2013-10-29 liush add 轨迹定位偏移量
		Float position_offset_x = mapConfig.getPosition_offset_x()!=null?mapConfig.getPosition_offset_x():0;
		Float position_offset_y = mapConfig.getPosition_offset_y()!=null?mapConfig.getPosition_offset_y():0;
		Float position_offset_x_3D = mapConfig.getPosition_offset_x_3D()!=null?mapConfig.getPosition_offset_x_3D():0;
		Float position_offset_y_3D = mapConfig.getPosition_offset_x_3D()!=null?mapConfig.getPosition_offset_y_3D():0;
		
		//地图显示级别
		Integer gis_map_center_level = mapConfig.getMap_center_level()!=null?mapConfig.getMap_center_level():0;
		//2013-10-28 liush add
		Integer gis_map_center_level_3D = mapConfig.getMap_center_level_3D()!=null?mapConfig.getMap_center_level_3D():0;
		//通过网格ID获取，获取该地图类型的对应的地图信息.2013-10-28 liush add 将二维三维地图都查询出来
		GisMapInfo gridMapInfo = gisInfoService.findGisMapInfoByGridId(startGridId, mapConfig.getMap_type());
		if(gridMapInfo!=null){
			gis_map_center_x = gridMapInfo.getX();
			gis_map_center_y = gridMapInfo.getY();
//			gis_map_center_level = gridMapInfo.getLevel();
		}
		GisMapInfo gridMapInfo3D = gisInfoService.findGisMapInfoByGridId(startGridId, mapConfig.getMap_type_3D());
		if(gridMapInfo3D!=null){
			gis_map_center_x_3D = gridMapInfo3D.getX();
			gis_map_center_y_3D = gridMapInfo3D.getY();
//			gis_map_center_level = gridMapInfo.getLevel();
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("projectPath", skin);
		resultMap.put("sys_pathMap_index", skin);
		resultMap.put("sys_pathMap_default", skin);
		resultMap.put("map_pe_dialog_width", "305");
		resultMap.put("map_renthous_dialog_width", "249");
		resultMap.put("ini_mpt", start_mapt);
		resultMap.put("map_wggly", start_mapt);
		resultMap.put("gis_map_type", start_mapt);
		resultMap.put("map_type", map_type);
		resultMap.put("map_type_3D", map_type_3D);
		//中心点坐标
		resultMap.put("gis_map_center_x", gis_map_center_x.toString());
		resultMap.put("gis_map_center_y", gis_map_center_y.toString());
		resultMap.put("gis_map_center_x_3D", gis_map_center_x_3D.toString());
		resultMap.put("gis_map_center_y_3D", gis_map_center_y_3D.toString());
		//地图缩放范围
		resultMap.put("gis_map_minl", gis_map_minl.toString());
		resultMap.put("gis_map_maxl", gis_map_maxl.toString());
		resultMap.put("gis_map_minl_3D", gis_map_minl_3D.toString());
		resultMap.put("gis_map_maxl_3D", gis_map_maxl_3D.toString());
		//轨迹定位偏移量
		resultMap.put("position_offset_x", position_offset_x.toString());
		resultMap.put("position_offset_y", position_offset_y.toString());
		resultMap.put("position_offset_x_3D", position_offset_x_3D.toString());
		resultMap.put("position_offset_y_3D", position_offset_y_3D.toString());
		
		resultMap.put("gis_map_center_level", gis_map_center_level.toString());
		resultMap.put("gis_map_center_level_3D", gis_map_center_level_3D.toString());
		
		resultMap.put("map_gj_locl", "01");
		resultMap.put("call_type", "01");
		resultMap.put("m3dmap_url", map_file_3D);
		resultMap.put("mgoogle_hangpai_map_url", map_file);
		return resultMap;
		
	}
	/**
	 * 基础地图（该方法，属于公用的组件，非公用功能，请勿修改）
	 * @param session
	 * @param map
	 * @param request
	 * @param gridId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/gisMapForLocate")
	public String gisMapForLocate(HttpSession session, ModelMap map, HttpServletRequest request
			, @RequestParam(value="gridId", required=false) Long gridId
			, @RequestParam(value="buildingId", required=false) Long buildingId
			, @RequestParam(value="X", required=false) Long X
			, @RequestParam(value="Y", required=false) Long Y
			, @RequestParam(value="type", required=true) String type) throws Exception {
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		Long startGridId = gridInfo.getGridId();
		
		GisMapConfigInfo mapConfig = gisInfoService.findGisMapConfigInfoByOrgCode(gridInfo.getInfoOrgCode());
		if(mapConfig==null) throw new Exception("未找到该网格组织对应的地图配置信息！！");
		
		Integer start_mapt = mapConfig.getMap_start_type();
		Integer map_type = mapConfig.getMap_type();
		Integer map_type_3D = mapConfig.getMap_type_3D();
		boolean is3dMap = (start_mapt==4 && start_mapt==20) ? true: false;

		String map_file = mapConfig.getMap_file()!=null? mapConfig.getMap_file():"";
		String map_file_3D = mapConfig.getMap_file_3D()!=null? mapConfig.getMap_file_3D():"";
		//2013-11-11 liush add 中心点位坐标
		Float gis_map_center_x = mapConfig.getMap_center_x() != null?mapConfig.getMap_center_x():0;
		Float gis_map_center_x_3D = mapConfig.getMap_center_x_3D()!=null?mapConfig.getMap_center_x_3D():0;
		Float gis_map_center_y = mapConfig.getMap_center_y()!=null?mapConfig.getMap_center_y():0;
		Float gis_map_center_y_3D = mapConfig.getMap_center_y_3D()!=null?mapConfig.getMap_center_y_3D():0;
		
		//2013-10-28 liush add 最大最小缩放比例
		Integer gis_map_minl = mapConfig.getMap_minl()!=null?mapConfig.getMap_minl():0;
		Integer gis_map_maxl = mapConfig.getMap_maxl()!=null?mapConfig.getMap_maxl():0;
		Integer gis_map_minl_3D = mapConfig.getMap_minl_3D()!=null?mapConfig.getMap_minl_3D():0;
		Integer gis_map_maxl_3D = mapConfig.getMap_maxl_3D()!=null?mapConfig.getMap_maxl_3D():0;
		//2013-10-29 liush add 轨迹定位偏移量
//		Integer position_offset_num = mapConfig.getPosition_offset_num()!=null?mapConfig.getPosition_offset_num():0;
//		Integer position_offset_num_3D = mapConfig.getPosition_offset_num_3D()!=null?mapConfig.getPosition_offset_num_3D():0;
		
		//地图显示级别
		Integer gis_map_center_level = mapConfig.getMap_center_level()!=null?mapConfig.getMap_center_level():0;
		//2013-10-28 liush add
		Integer gis_map_center_level_3D = mapConfig.getMap_center_level_3D()!=null?mapConfig.getMap_center_level_3D():0;
		GisMapInfo gridMapInfo = gisInfoService.findGisMapInfoByGridId(startGridId, mapConfig.getMap_type());
		if(gridMapInfo!=null){
			gis_map_center_x = gridMapInfo.getX();
			gis_map_center_y = gridMapInfo.getY();
//			gis_map_center_level = gridMapInfo.getLevel();
		}
		GisMapInfo gridMapInfo3D = gisInfoService.findGisMapInfoByGridId(startGridId, mapConfig.getMap_type_3D());
		if(gridMapInfo3D!=null){
			gis_map_center_x_3D = gridMapInfo3D.getX();
			gis_map_center_y_3D = gridMapInfo3D.getY();
//			gis_map_center_level = gridMapInfo.getLevel();
		}
		if(gis_map_center_level==null || "".equals(gis_map_center_level)) gis_map_center_level = 1;
		
		String startGridName = gridInfo.getGridName();
		Long starGridId = gridInfo.getGridId();
		Integer gridLevel = gridInfo.getGridLevel();
		
		String projectPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		
		map.addAttribute("startGridId", starGridId.toString());
		map.addAttribute("startGridOrgCode", gridInfo.getInfoOrgCode());
		map.addAttribute("startGridName", startGridName);
		map.addAttribute("gridLevel", gridLevel);
		
		map.addAttribute("projectPath", projectPath);
//		map.addAttribute("map_host", projectPath+"/gis");
		//map.addAttribute("map_host", ConstantValue.GIS_MAP_BASE_PATH+"/gis"); //调试环境
		map.addAttribute("map_host", App.OLD_GIS_MAP.getDomain(session)+"/gis");//支持多个域名访问综治网格
		map.addAttribute("start_mapt", start_mapt);
		map.addAttribute("map_type", map_type);
		map.addAttribute("map_type_3D", map_type_3D);
		
		//中心点坐标
		map.addAttribute("map_center_x", gis_map_center_x.toString());
		map.addAttribute("map_center_y", gis_map_center_y.toString());
		map.addAttribute("map_center_x_3D", gis_map_center_x_3D.toString());
		map.addAttribute("map_center_y_3D", gis_map_center_y_3D.toString());
		
		//地图缩放范围
		map.addAttribute("gis_map_minl", gis_map_minl.toString());
		map.addAttribute("gis_map_maxl", gis_map_maxl.toString());
		map.addAttribute("gis_map_minl_3D", gis_map_minl_3D.toString());
		map.addAttribute("gis_map_maxl_3D", gis_map_maxl_3D.toString());
		
		map.addAttribute("map_center_level", gis_map_center_level);
		map.addAttribute("map_center_level_3D", gis_map_center_level_3D);
		map.addAttribute("map_file", map_file);
		map.addAttribute("map_file_3D", map_file_3D);
		map.addAttribute("gridId", gridId!=null?gridId:0);
		map.addAttribute("buildingId", buildingId!=null?buildingId:0);
		map.addAttribute("X", X!=null?X:0);
		map.addAttribute("Y", Y!=null?Y:0);
		map.addAttribute("type", type);
		
		return "/zzgl_map/gis/GisMapForLocate.ftl";
		
	}
	@RequestMapping(value="/GisMapTest")
	public String GisMapTest(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="gridId", required=false) Long gridId) throws Exception  {
		String projectPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		Long startGridId = gridInfo.getGridId();
		
		map.addAttribute("projectPath", projectPath);
		map.addAttribute("gridId", startGridId);
		
		return "/zzgl_map/gis/GisMapTest.ftl";
	}
	
	@RequestMapping(value="/popMapMarker")
	public String popMapMarker(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="gridId", required=false) Long gridId
			, @RequestParam(value="isEdit", required=false) String isEdit
			, @RequestParam(value="mapType", required=false) String mapType) throws Exception  {
		String projectPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		Long startGridId = gridInfo.getGridId();
		
		map.addAttribute("projectPath", projectPath);
		map.addAttribute("gridId", startGridId);
		map.addAttribute("isEdit", isEdit);
		map.addAttribute("mapType", mapType);
		return "/zzgl_map/gis/popMapMarker.ftl";
	}
	
	
	////=============GIS地图画轮廓===================================================================================================
	
	/**
	 * 地图画轮廓
	 * @param session
	 * @param request
	 * @param map
	 * @param areaType 区域类型：画建筑物=building，画网格=grid
	 * @param wid 地图关联ID：画建筑物=buildingId, 画网格=gridId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/drawArea")
	public String drawArea(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="areaType", required=false) String areaType,
			@RequestParam(value="gridId", required=false) Long gridId,
			@RequestParam(value="name", required=false) String name,
			@RequestParam(value="wid", required=true) Long wid) throws Exception {
		//Long gridId=null;
		Long parentGridId = null;
		//String name=null;
		String forward = "/zzgl_map/gis/drawArea.ftl";
		if("building".equals(areaType)){//画建筑物
			AreaBuildingInfo building = areaBuildingInfoService.findAreaBuildingInfoById(wid);
			gridId = building.getGridId();
			name = building.getBuildingName();
			parentGridId = gridId;
		}else if("grid".equals(areaType)){//画网格
			gridId = wid;
			MixedGridInfo grid = mixedGridInfoService.findMixedGridInfoById(gridId, false);
			name = grid.getGridName();
			parentGridId = grid.getParentGridId();
		}else if("segmentgrid".equals(areaType)) {//片区网格
			SegmentGrid segmentgrid = segmentGridService.findSegmentGridById(wid,null);
			gridId = segmentgrid.getParentGridId();
			name = segmentgrid.getSegmentGridName();
			parentGridId = segmentgrid.getParentGridId();
			forward = "/zzgl_map/gis/drawAreaForSegmentGrid.ftl";
		}else{//画资源
			if(gridId==null){
				MixedGridInfo gridInfo = getMixedGridInfo(session,request);
				gridId = gridInfo.getGridId();
			}
			if(name!=null)
				try {
					name = java.net.URLDecoder.decode(name, "utf-8");
				} catch (UnsupportedEncodingException e) {}
			forward = "/zzgl_map/gis/drawAreaForResMarker.ftl";
		}
		map.addAttribute("projectPath", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath());
		map.addAttribute("gridId", gridId+"");
		map.addAttribute("parentGridId", parentGridId+"");
		map.addAttribute("areaType", areaType);
		map.addAttribute("wid", wid.toString());
		map.addAttribute("name", name);
		return forward;
	}
	
	
	@RequestMapping(value="/saveDrawArea")
	public @ResponseBody int saveDrawArea(HttpSession session, ModelMap map, HttpServletResponse res, HttpServletRequest request
			,@RequestParam(value="areaType", required=false) String areaType
			,@RequestParam(value="wid", required=false) Long wid
			,@RequestParam(value="mapt", required=false) Integer mapt
			,@RequestParam(value="x", required=false) Float x
			,@RequestParam(value="y", required=false) Float y
			,@RequestParam(value="levnum", required=false) String levnum
			,@RequestParam(value="hs", required=false) String hs
			,@RequestParam(value="color", required=false) String color
			,@RequestParam(value="mapCenterLevel", required=false) Integer mapCenterLevel
			,@RequestParam(value="gjColor", required=false) String gjColor
			) throws Exception{
		int result = 0;
		if("building".equals(areaType)){//画建筑物
			GisMapBuidingInfo bean = gisInfoService.findGisMapBuidingInfoByBuildingIdAndMapt(wid, mapt);
			if(bean==null){
				bean = new GisMapBuidingInfo();
				bean.setBuildingId(wid);
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				
				result = gisInfoService.insertGisMapBuidingInfo(bean);
				
				///记录日志
				if(result>0){
					bean = gisInfoService.findGisMapBuidingInfoByBuildingIdAndMapt(wid, mapt);
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getBuildingName();
					logService.savelog2(request, LogModule.mapDataForBuilding, ActionType.insert, 
							bean.getId(), recordName, null, valueAfter);
				}
			}else{
				String valueBefore = JSONObject.fromObject(bean).toString();
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				result = gisInfoService.updateGisMapBuidingInfo(bean);
				///记录日志
				if(result>0){
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getBuildingName();
					logService.savelog2(request, LogModule.mapDataForBuilding, ActionType.update, 
							bean.getId(), recordName, valueBefore, valueAfter);
				}
			}
		}else if("grid".equals(areaType)){//画网格
			GisMapGridInfo bean = gisInfoService.findGisMapGridInfoByGridIdAndMapt(wid, mapt);
			
			if(bean==null){
				bean = new GisMapGridInfo();
				bean.setGridId(wid);
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				bean.setMapCenterLevel(mapCenterLevel);
				bean.setGjColor(gjColor);
				result = gisInfoService.insertGisMapGridInfo(bean);
				///记录日志
				if(result>0){
					bean = gisInfoService.findGisMapGridInfoByGridIdAndMapt(wid, mapt);
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getGridName();
					logService.savelog2(request, LogModule.mapDataForGrid, ActionType.insert, 
							bean.getId(), recordName, null, valueAfter);
				}
			}else{
				String valueBefore = JSONObject.fromObject(bean).toString();
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				bean.setMapCenterLevel(mapCenterLevel);
				bean.setGjColor(gjColor);
				result = gisInfoService.updateGisMapGridInfo(bean);
				///记录日志
				if(result>0){
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getGridName();
					logService.savelog2(request, LogModule.mapDataForGrid, ActionType.update, 
							bean.getId(), recordName, valueBefore, valueAfter);
				}
			}
		}else if("segmentgrid".equals(areaType)) {
			GisMapSegmentGridInfo bean = gisInfoService.findGisMapSegmentGridInfoBySegmentGridIdAndMapt(wid, mapt);
			if(bean == null) {
				bean = new GisMapSegmentGridInfo();
				bean.setSegmentGridId(wid);
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				result = gisInfoService.insertGisMapSegmentGridInfo(bean);
				///记录日志
				if(result>0){
					bean  = gisInfoService.findGisMapSegmentGridInfoBySegmentGridIdAndMapt(wid, mapt);
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getSegmentGridName();
					logService.savelog2(request, LogModule.mapDataForSegmentGrid, ActionType.insert, 
							bean.getId(), recordName, null, valueAfter);
				}
			}else {
				String valueBefore = JSONObject.fromObject(bean).toString();
				bean.setMapt(mapt);
				bean.setCenLon(x);
				bean.setCenLat(y);
				bean.setHs(hs);
				bean.setColorDesc(color);
				result = gisInfoService.updateGisMapSegmentGridInfo(bean);
				///记录日志
				if(result>0){
					String valueAfter = JSONObject.fromObject(bean).toString();
					String recordName = bean.getSegmentGridName();
					logService.savelog2(request, LogModule.mapDataForSegmentGrid, ActionType.update, 
							bean.getId(), recordName, valueBefore, valueAfter);
				}
			}
		}else{ //其他资源标注信息
			ResMarker marker = null;
			ActionType action;
			Long keyId=null;
			String valueBefore =null, valueAfter=null;
			List<ResMarker> list = resMarkerService.findResMarkerByParam(areaType, wid, mapt+"");
			if(list!=null && list.size()>0){
				marker = list.get(0);
				keyId = marker.getMarkerId();
				action = ActionType.update;
				valueBefore = JSONObject.fromObject(marker).toString();
			}else{
				marker = new ResMarker();
				action = ActionType.insert;
				valueBefore = null;
			}
			
			marker.setResourcesId(wid);
			marker.setMarkerType(areaType);
			marker.setMapType(mapt+"");
			marker.setX(x+"");
			marker.setY(y+"");
			marker.setHs(hs);
			marker.setColordesc(color);
			marker.setLevnum(levnum);
			
			if(resMarkerService.saveOrUpdateResMarker(marker)){
				result++;
				if(keyId==null){
					List<ResMarker> list2 = resMarkerService.findResMarkerByParam(areaType, wid, mapt+"");
					keyId = list2.get(0).getMarkerId();
				}
				///记录日志
				logService.savelog2(request, LogModule.mapDataForResMarker, action, 
						keyId, areaType+ "--"+ keyId, valueBefore, valueAfter);
			}
		}
		return result;
	}
	
	@RequestMapping(value="/deleteDataForDrawArea")
	public @ResponseBody int deleteDataForDrawArea(HttpSession session, ModelMap map, HttpServletResponse res, HttpServletRequest request
			,@RequestParam(value="areaType", required=false) String areaType
			,@RequestParam(value="wid", required=false) Long wid
			,@RequestParam(value="mapt", required=false) Integer mapt
			) throws Exception{
		int result = 0;
		if("building".equals(areaType)){//删除建筑物地图数据
			GisMapBuidingInfo bean = gisInfoService.findGisMapBuidingInfoByBuildingIdAndMapt(wid, mapt);
			if(bean!=null){
				result = gisInfoService.deleteGisMapBuidingInfoById(bean.getId());
				///保存日志
				String valueBefore = JSONObject.fromObject(bean).toString();
				String recordName = bean.getBuildingName();
				logService.savelog2(request, LogModule.mapDataForBuilding, ActionType.delete, 
						bean.getId(), recordName, valueBefore, null);
			}
		}else if("grid".equals(areaType)){//删除网格地图数据
			GisMapGridInfo bean = gisInfoService.findGisMapGridInfoByGridIdAndMapt(wid, mapt);
			if(bean!=null){
				result = gisInfoService.deleteGisMapGridInfoById(bean.getId());
				///保存日志
				String valueBefore = JSONObject.fromObject(bean).toString();
				String recordName = bean.getGridName();
				logService.savelog2(request, LogModule.mapDataForGrid, ActionType.delete, 
						bean.getId(), recordName, valueBefore, null);
			}
		}else if("segmentgrid".equals(areaType)) {
			GisMapSegmentGridInfo bean = gisInfoService.findGisMapSegmentGridInfoBySegmentGridIdAndMapt(wid, mapt);
			if(bean!=null) {
				result = gisInfoService.deleteGisMapSegmentGridInfoById(bean.getId());
				///保存日志
				String valueBefore = JSONObject.fromObject(bean).toString();
				String recordName = bean.getSegmentGridName();
				logService.savelog2(request, LogModule.mapDataForSegmentGrid, ActionType.delete, 
						bean.getId(), recordName, valueBefore, null);
			}
		}else{//其他资源标注信息
			List<ResMarker> list = resMarkerService.findResMarkerByParam(areaType, wid, mapt+"");
			if(list!=null && list.size()>0){
				ResMarker bean = list.get(0);
				String valueBefore = JSONObject.fromObject(bean).toString();
				String recordName = bean.getMarkerId()+"";
				if(resMarkerService.deleteResMarker(areaType, wid, mapt+"")){
					result++;
					///记录日志
					logService.savelog2(request, LogModule.mapDataForResMarker, ActionType.delete, 
							bean.getMarkerId(), recordName, valueBefore, null);
				}
			}
		}
		return result;
	}
	
	//// =============GIS地图配置管理===================================================================================================
	@RequestMapping(value="/mapConfig")
	public String mapConfig(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="gridId", required=false) Long gridId
			,@RequestParam(value="showDefault", required=false) boolean showDefault) throws Exception {
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		if(gridId==null) gridId = gridInfo.getGridId();
		String orgCode = gridInfo.getInfoOrgCode();
		String gridName = gridInfo.getGridName();
		GisMapConfigInfo bean;
		if(showDefault){
			bean = gisInfoService.findGisMapConfigInfoByOrgCode(orgCode);
		}else{
			bean = gisInfoService.getGisMapConfigInfoByOrgCode(orgCode);
		}
		
		map.addAttribute("projectPath", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath());
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("gridId", gridId.toString());
		map.addAttribute("gridName", gridName);
		
		
		if(bean!=null){
			map.addAttribute("map_start_type", bean.getMap_start_type()!=null ? bean.getMap_start_type():"");
			map.addAttribute("mapFile2d",  bean.getMap_file()!=null ? bean.getMap_file():"");
			map.addAttribute("mapFile3d", bean.getMap_file_3D()!=null ? bean.getMap_file_3D():"");
			map.addAttribute("map_center_x", bean.getMap_center_x()!=null ? bean.getMap_center_x():"");
			map.addAttribute("map_center_y", bean.getMap_center_y()!=null ? bean.getMap_center_y():"");
			map.addAttribute("map_center_level", bean.getMap_center_level()!=null ? bean.getMap_center_level():"1");
			map.addAttribute("map_center_x_3d", bean.getMap_center_x_3D()!=null ? bean.getMap_center_x_3D():"");
			map.addAttribute("map_center_y_3d", bean.getMap_center_y_3D()!=null ? bean.getMap_center_y_3D():"");
			map.addAttribute("map_center_level_3D", bean.getMap_center_level_3D()!=null ? bean.getMap_center_level_3D():"3");
			map.addAttribute("theme",bean.getTheme()!=null ? bean.getTheme(): "sosp_haicang");
		}else{
			map.addAttribute("map_start_type", 2);
			map.addAttribute("mapFile2d",  "");
			map.addAttribute("mapFile3d", "");
			map.addAttribute("map_center_x", "");
			map.addAttribute("map_center_y", "");
			map.addAttribute("map_center_level", "1");
			map.addAttribute("map_center_x_3d", "");
			map.addAttribute("map_center_y_3d", "");
			map.addAttribute("map_center_level_3D", "3");
			map.addAttribute("theme","sosp_haicang");
		}
		
		return "/zzgl_map/gis/mapConfig.ftl";
	}
	
	
	@RequestMapping(value="/saveMapConig")
	public @ResponseBody int saveMapConig(HttpSession session, ModelMap map, HttpServletResponse res
			,@RequestParam(value="orgCode", required=false) String orgCode
			,@RequestParam(value="map_start_type", required=false) Integer map_start_type
			,@RequestParam(value="mapFile2d", required=false) String mapFile2d
			,@RequestParam(value="mapFile3d", required=false) String mapFile3d
			,@RequestParam(value="map_center_x", required=false) Float map_center_x
			,@RequestParam(value="map_center_y", required=false) Float map_center_y
			,@RequestParam(value="map_center_level", required=false) Integer map_center_level
			,@RequestParam(value="map_center_x_3d", required=false) Float map_center_x_3d
			,@RequestParam(value="map_center_y_3d", required=false) Float map_center_y_3d
			,@RequestParam(value="map_center_level_3D", required=false) Integer map_center_level_3D
			,@RequestParam(value="theme", required=false) String theme
			) throws Exception{
		
		int result = 0;
		boolean isEdit = false;
		GisMapConfigInfo bean = gisInfoService.getGisMapConfigInfoByOrgCode(orgCode);
		if(bean==null){
			bean = new GisMapConfigInfo(); 
		}else{
			isEdit = true;
		}
		bean.setOrgCode(orgCode);
		bean.setMap_start_type(map_start_type);
		bean.setMap_file(mapFile2d);
		bean.setMap_file_3D(mapFile3d);
		bean.setMap_center_x(map_center_x);
		bean.setMap_center_y(map_center_y);
		bean.setMap_center_level(map_center_level);
		bean.setMap_center_x_3D(map_center_x_3d);
		bean.setMap_center_y_3D(map_center_y_3d);
		bean.setMap_center_level_3D(map_center_level_3D);
		bean.setTheme(theme);
		if(isEdit){
			result = gisInfoService.updateGisMapConfigInfo(bean);
		}else{
			result = gisInfoService.insertGisMapConfigInfo(bean);
		}
		
		return result;
	}
	@RequestMapping(value="/deleteGisMapConfigData")
	public @ResponseBody int deleteGisMapConfigData(HttpSession session, ModelMap map, HttpServletResponse res
			,@RequestParam(value="orgCode", required=false) String orgCode
			) throws Exception{
		return gisInfoService.deleteGisMapConfigInfoByOrgCode(orgCode);
	}
	
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
			if(StringUtils.isNotBlank(mapTypeResult)) {
				mapType = mapTypeResult;
			} else{
				mapType = "2";
			}
		}
		if(StringUtils.isBlank(mapType)){
			mapType = "2";
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
		result.put("ZHSQ_EVENT_URL", App.EVENT.getDomain(session));//支持多域名

		result.put("ZHSQ_ZZGRID_URL", App.ZZGRID.getDomain(session));//支持多域名
		result.put("FACTOR_STATION", FACTOR_STATION);
		result.put("FACTOR_URL", FACTOR_URL);
		result.put("FACTOR_SERVICE", FACTOR_SERVICE);
		result.put("markerType",markerType);
		return result;
	}
	
	@RequestMapping(value="/commonCrossDomain")
	public String commonCrossDomain(HttpSession session, HttpServletRequest request,
			ModelMap map,
			@RequestParam(value = "callBack", required = false) String callBack)
			throws Exception {
		callBack = java.net.URLDecoder.decode(callBack, "utf-8");
		map.addAttribute("callBack", callBack);
		String forward = "/zzgl_map/gis/standardmappage/commonCrossDomain.ftl";
		return forward;
	}
	
	/**
	 * 获取标注信息
	 * 
	 * @param session
	 * @param request
	 * @param map
	 * @param bizType
	 * @param id
	 * @param module
	 *            模块类型（新经济组织：NEW_NONPUBLICORG_CODE；新社会组织：NEW_ORGANIZATION_CODE）
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getMapMarkerData")
	public Map<String, Object> getMapMarkerData(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "id", required = true) Long id,
			@RequestParam(value = "module", required = false) String module) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);

		// 数据库存储的地图类型值
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
				} else {
					mapt = "5";
				}
			}
			if(StringUtils.isNotBlank(mapt) && ("4".equals(mapt) ||  "20".equals(mapt))){
				GisMapConfigInfo mapConfig = gisInfoService.findGisMapConfigInfoByOrgCode(gridInfo.getInfoOrgCode());
				mapt = mapConfig.getMap_type_3D().toString();
			}
		}

		// 模块类型不为空
		if (StringUtils.isNotBlank(module)) {
			if (id != null) {
				// 新经济组织模块
				if (module.equals(ConstantValue.NEW_NONPUBLICORG_CODE)) {
					CorBaseInfo corBaseInfo = new CorBaseInfo();
					corBaseInfo.setCbiId(id);
					corBaseInfo = corBaseInfoService.getCorpBaseInfo(corBaseInfo, mapt);

					if (StringUtils.isNotBlank(corBaseInfo.getX())) {
						result.put("x", corBaseInfo.getX());
					}

					if (StringUtils.isNotBlank(corBaseInfo.getY())) {
						result.put("y", corBaseInfo.getY());
					}

					if (StringUtils.isNotBlank(corBaseInfo.getMapType())) {
						result.put("mapt", corBaseInfo.getMapType());
					}
				}/* else if (module.equals(ConstantValue.MARKER_TYPE_CORBASE)) { // 企业
					CorBaseInfo corBaseInfo = new CorBaseInfo();
					corBaseInfo.setCbiId(id);
					corBaseInfo = corBaseInfoService.getCorBaseInfo(corBaseInfo);

					if (StringUtils.isNotBlank(corBaseInfo.getX())) {
						result.put("x", corBaseInfo.getX());
					}

					if (StringUtils.isNotBlank(corBaseInfo.getY())) {
						result.put("y", corBaseInfo.getY());
					}

					if (StringUtils.isNotBlank(corBaseInfo.getMapType())) {
						result.put("mapt", corBaseInfo.getMapType());
					}
				}*/ else if (module.equals(ConstantValue.MARKER_TYPE_PLACEINFO)) { // 重点场所
					PlaceInfo placeInfo = placeInfoService.findPlaceInfoByIdAndMapt(id, mapt);

					if (StringUtils.isNotBlank(placeInfo.getX())) {
						result.put("x", placeInfo.getX());
					}

					if (StringUtils.isNotBlank(placeInfo.getY())) {
						result.put("y", placeInfo.getY());
					}

					if (StringUtils.isNotBlank(placeInfo.getMapType())) {
						result.put("mapt", placeInfo.getMapType());
					}
				} else if (module.equals(ConstantValue.NEW_ORGANIZATION_CODE)) { // 新社会组织模块
					Organization organization = organizationService.getOrganizationByIdAndMapt(id, mapt);

					if (StringUtils.isNotBlank(organization.getX())) {
						result.put("x", organization.getX());
					}

					if (StringUtils.isNotBlank(organization.getY())) {
						result.put("y", organization.getY());
					}

					if (StringUtils.isNotBlank(organization.getMapType())) {
						result.put("mapt", organization.getMapType());
					}
				} else if (module.equals(ConstantValue.FIRERES2) || module.equals(ConstantValue.FIRERES3)
						|| module.equals(ConstantValue.FIRERES4)) { // 室外消防栓、天然水源、自来水公司
					FireResource fireResource = fireResourceService.findByIdAndMapt(id, mapt);

					if (StringUtils.isNotBlank(fireResource.getX())) {
						result.put("x", fireResource.getX());
					}

					if (StringUtils.isNotBlank(fireResource.getY())) {
						result.put("y", fireResource.getY());
					}

					if (StringUtils.isNotBlank(fireResource.getMapType())) {
						result.put("mapt", fireResource.getMapType());
					}
				} else if (module.equals(ConstantValue.FIRETEAM)) { // 消防队
					FireTeam fireTeam = fireTeamService.findFireTeamByIdAndMapt(id, mapt);
					if (StringUtils.isNotBlank(fireTeam.getX())) {
						result.put("x", fireTeam.getX());
					}

					if (StringUtils.isNotBlank(fireTeam.getY())) {
						result.put("y", fireTeam.getY());
					}

					if (StringUtils.isNotBlank(fireTeam.getMapType())) {
						result.put("mapt", fireTeam.getMapType());
					}
				}else if (module.equals(ConstantValue.FIRETEAM_NEW)) { // 新消防队
					FireTeam fireTeam = fireTeamNewService.findFireTeamByIdAndMapt(id, mapt);
					if (StringUtils.isNotBlank(fireTeam.getX())) {
						result.put("x", fireTeam.getX());
					}

					if (StringUtils.isNotBlank(fireTeam.getY())) {
						result.put("y", fireTeam.getY());
					}

					if (StringUtils.isNotBlank(fireTeam.getMapType())) {
						result.put("mapt", fireTeam.getMapType());
					}
					
				}else if (module.equals(ConstantValue.FIRE_POOL)) { // 消防水池
					FireResource fireResource = fireResourceService.findByIdAndMapt(id, mapt);
					
					if (StringUtils.isNotBlank(fireResource.getX())) {
						result.put("x", fireResource.getX());
					}

					if (StringUtils.isNotBlank(fireResource.getY())) {
						result.put("y", fireResource.getY());
					}

					if (StringUtils.isNotBlank(fireResource.getMapType())) {
						result.put("mapt", fireResource.getMapType());
					}
				}  else if (module.equals(ConstantValue.GLOBAL_EYES)) { // 全球眼
					MonitorBO monitorBO = monitorService.getMonitorById(id);

					List<ResMarker> makers = resMarkerService.findResMarkerByParam(
							ConstantValue.MARKER_TYPE_GLOBAL_EYES, monitorBO.getMonitorId(), mapt);

					if (!makers.isEmpty()) {
						ResMarker marker = makers.get(0);
						result.put("x", marker.getX());
						result.put("y", marker.getY());
						result.put("mapt", marker.getMapType());
					}
				} else if (module.equals(ConstantValue.CAMPUS_AROUND)) { // 校园周边
					PlaceInfo placeInfo = placeInfoService.findPlaceInfoByIdAndMapt(id, mapt);

					if (StringUtils.isNotBlank(placeInfo.getX())) {
						result.put("x", placeInfo.getX());
					}

					if (StringUtils.isNotBlank(placeInfo.getY())) {
						result.put("y", placeInfo.getY());
					}

					if (StringUtils.isNotBlank(placeInfo.getMapType())) {
						result.put("mapt", placeInfo.getMapType());
					} 
				} else if (module.equals(ConstantValue.ROAD_MANAGE)) { // 道路管理
					RoadManagement roadManagement = roadManagementService.findRoadManagementByIdAndMapt(id, mapt);

					if (StringUtils.isNotBlank(roadManagement.getX())) {
						result.put("x", roadManagement.getX());
					}

					if (StringUtils.isNotBlank(roadManagement.getY())) {
						result.put("y", roadManagement.getY());
					}

					if (StringUtils.isNotBlank(roadManagement.getMapType())) {
						result.put("mapt", roadManagement.getMapType());
					}
				} else if (module.equals(ConstantValue.RES_DEFAULT)) { // 市政设施
					ResInfo resInfo = resInfoService.findResInfoByIdAndMapt(id, mapt);

					if (StringUtils.isNotBlank(resInfo.getX())) {
						result.put("x", resInfo.getX());
					}

					if (StringUtils.isNotBlank(resInfo.getY())) {
						result.put("y", resInfo.getY());
					}

					if (StringUtils.isNotBlank(resInfo.getMapType())) {
						result.put("mapt", resInfo.getMapType());
					}
				} else if (module.equals(ConstantValue.EVENT_V0)) { // 旧事件
					EventDisposal event = eventDisposalService.findEventByIdAndMapt(id, mapt);

					if (StringUtils.isNotBlank(event.getX())) {
						result.put("x", event.getX());
					}

					if (StringUtils.isNotBlank(event.getY())) {
						result.put("y", event.getY());
					}

					if (StringUtils.isNotBlank(event.getMapType())) {
						result.put("mapt", event.getMapType());
					}
				} else if (module.equals(ConstantValue.SAFE_CHECK)) {
					String markerType = ConstantValue.SAFETY_INSPECTION;
					
					List<ResMarker> makers = resMarkerService.findResMarkerByParam(markerType, id, mapt);

					if (makers != null && makers.size() > 0) {
						ResMarker mark = makers.get(0);

						if (StringUtils.isNotBlank(mark.getX())) {
							result.put("x", mark.getX());
						}

						if (StringUtils.isNotBlank(mark.getY())) {
							result.put("y", mark.getY());
						}

						if (StringUtils.isNotBlank(mark.getMapType())) {
							result.put("mapt", mark.getMapType());
						}
					}
				} else if (module.equals(ConstantValue.ROOM_RENT)) {// 出租屋
					String markerType = ConstantValue.ROOM_RENT;
					
					List<ResMarker> makers = resMarkerService.findResMarkerByParam(markerType, id, mapt);

					if (makers != null && makers.size() > 0) {
						ResMarker mark = makers.get(0);

						if (StringUtils.isNotBlank(mark.getX())) {
							result.put("x", mark.getX());
						}

						if (StringUtils.isNotBlank(mark.getY())) {
							result.put("y", mark.getY());
						}

						if (StringUtils.isNotBlank(mark.getMapType())) {
							result.put("mapt", mark.getMapType());
						}
					}
				}else if (module.equals(ConstantValue.DISPUTE_MEDIATION)) {// 矛盾纠纷
					String markerType = ConstantValue.MARKER_TYPE_DISPUTE_MEDIATION;
					
					List<ResMarker> makers = resMarkerService.findResMarkerByParam(markerType, id, mapt);

					if (makers != null && makers.size() > 0) {
						ResMarker mark = makers.get(0);

						if (StringUtils.isNotBlank(mark.getX())) {
							result.put("x", mark.getX());
						}

						if (StringUtils.isNotBlank(mark.getY())) {
							result.put("y", mark.getY());
						}

						if (StringUtils.isNotBlank(mark.getMapType())) {
							result.put("mapt", mark.getMapType());
						}
					}
				}else if (module.equals(ConstantValue.GENERAL_EVENT)) {// 通用事件
					String markerType = ConstantValue.MARKER_TYPE_GENERAL_EVENT;
					
					List<ResMarker> makers = resMarkerService.findResMarkerByParam(markerType, id, mapt);

					if (makers != null && makers.size() > 0) {
						ResMarker mark = makers.get(0);

						if (StringUtils.isNotBlank(mark.getX())) {
							result.put("x", mark.getX());
						}

						if (StringUtils.isNotBlank(mark.getY())) {
							result.put("y", mark.getY());
						}

						if (StringUtils.isNotBlank(mark.getMapType())) {
							result.put("mapt", mark.getMapType());
						}
					}
				}else if (module.equals(ConstantValue.FIRE_CONTROL)) {// 通用事件
					String markerType = ConstantValue.MARKER_TYPE_FIRE_CONTROL;
					
					List<ResMarker> makers = resMarkerService.findResMarkerByParam(markerType, id, mapt);

					if (makers != null && makers.size() > 0) {
						ResMarker mark = makers.get(0);

						if (StringUtils.isNotBlank(mark.getX())) {
							result.put("x", mark.getX());
						}

						if (StringUtils.isNotBlank(mark.getY())) {
							result.put("y", mark.getY());
						}

						if (StringUtils.isNotBlank(mark.getMapType())) {
							result.put("mapt", mark.getMapType());
						}
					}
				}else{
					List<ResMarker> makers = resMarkerService.findResMarkerByParam(module, id, mapt);
					if (makers != null && makers.size() > 0) {
						ResMarker mark = makers.get(0);

						if (StringUtils.isNotBlank(mark.getX())) {
							result.put("x", mark.getX());
						}

						if (StringUtils.isNotBlank(mark.getY())) {
							result.put("y", mark.getY());
						}

						if (StringUtils.isNotBlank(mark.getMapType())) {
							result.put("mapt", mark.getMapType());
						}
					}
				}
			}
		}
		
		return result;
	}
	
	// 地图标注使用的地图维度（二维、三维）
	private final String MAP_TYPE_CODE = "MAP_TYPE_CODE";
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
