package cn.ffcs.zhsq.map.arcgis.controller;



import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.*;
import cn.ffcs.zhsq.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.ffcs.cookie.bo.UserCookie;
import cn.ffcs.resident.service.CiRsService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.AreaBuildingInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CareRoad;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CorBaseInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.GridAdmin;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PointInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PrvetionTeam;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.SegmentGrid;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.PreResources;
import cn.ffcs.shequ.mybatis.domain.zzgl.work.WorkAttachment;
import cn.ffcs.shequ.statistics.domain.pojo.StatisticsInfo;
import cn.ffcs.shequ.statistics.service.IGridStatisticsService;
import cn.ffcs.shequ.wap.domain.pojo.ResMarkedOM;
import cn.ffcs.shequ.zzgl.service.grid.IAreaBuildingInfoService;
import cn.ffcs.shequ.zzgl.service.grid.ICareRoadOrgService;
import cn.ffcs.shequ.zzgl.service.grid.ICareRoadService;
import cn.ffcs.shequ.zzgl.service.grid.ICorBaseInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IGridAdminService;
import cn.ffcs.shequ.zzgl.service.grid.IPointInfoService;
import cn.ffcs.shequ.zzgl.service.grid.ISegmentGridService;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.shequ.zzgl.service.res.IPreResourcesService;
import cn.ffcs.shequ.zzgl.service.work.IWorkAttachmentService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.Menu;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.MenuOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.be.bo.arcgis.ArcgisInfoOfBuildingBo;
import cn.ffcs.zhsq.be.bo.building.BuildingBo;
import cn.ffcs.zhsq.be.bo.corpbase.CorpBaseBo;
import cn.ffcs.zhsq.be.bo.event.Event;
import cn.ffcs.zhsq.be.service.arcgis.ArcgisInfoService;
import cn.ffcs.zhsq.be.service.building.BuildingService;
import cn.ffcs.zhsq.be.service.corpbase.CorpBaseService;
import cn.ffcs.zhsq.be.service.event.EventService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.map.arcgis.service.IKuangXuanStatService;
import cn.ffcs.zhsq.map.coordinateconversion.service.IBaseConversionService;
import cn.ffcs.zhsq.map.gisstat.service.IGisStatService;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.wgs.WGSUtils;


/**
 * 2014-05-28 liushi add
 * arcgis地图数据加载控制器
 * @author liushi
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisdata")
public class ArcgisDataController extends ZZBaseController {
	@Resource(name="buildingService")
	private BuildingService buildingService;
	@Resource(name="lyjjArcgisInfoService")
	private ArcgisInfoService lyjjArcgisInfoService;
	@Resource(name="corpBaseService")
	private CorpBaseService corpBaseService;
	@Autowired
	private EventService eventService;
	
	@Autowired
	private IArcgisInfoService arcgisInfoService;
	@Autowired
	private IAreaBuildingInfoService areaBuildingInfoService;//楼栋服务
	@Autowired
	private ISegmentGridService segmentGridService;
	@Autowired
	private IGridAdminService gridAdminService;
	@Autowired
	private IGridStatisticsService gridStatisticsService;
	@Autowired 
	protected IGisInfoService gisInfoService;
	@Autowired 
	protected IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	private IPreResourcesService preResourcesService;
	
	@Autowired
	private IBaseConversionService baseConversionService;
	
	@Autowired
	private MenuOutService menuService; //菜单
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;  //功能域组织服务
	
	@Autowired
	private IKuangXuanStatService kuangXuanStat;//框选统计服务
	
	@Autowired
	private ICareRoadService careRoadService;
	
	@Autowired
	private ICareRoadOrgService iCareRoadOrgService;
	
	@Autowired
	private CiRsService ciRsService;
	@Autowired
	private IMenuConfigService menuConfigService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	private ICorBaseInfoService corBaseInfoService;
	
	@Autowired
	private IGisStatService gisStatService;
	@Autowired
	private IPointInfoService pointInfoService; //注入点位信息模块服务
	@Autowired
    private IWorkAttachmentService workAttachmentService;


	/**
	 * 2014-05-28 liushi add
	 * 转到arcgis轮廓编辑的panel（主要针对网格grid、楼栋build）
	 * 需要获取目标的名称
	 * @param session
	 * @param request
	 * @param map
	 * @param targetType目标类型  grid   build
	 * @param wid  目标关联id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toArcgisDrawAreaPanel")
	public String toArcgisDrawAreaPanel(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="targetType") String targetType
			,@RequestParam(value="wid") Long wid
			,@RequestParam(value="isDetail", required = false) String isDetail) throws Exception{
		String forward = "/map/arcgis/arcgis_draw/draw_area_panel.ftl";
		String name = "";
		Long parentGridId = null;
		Long gridId = null;
		Integer gridLevel = 0;
		Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
		if("grid".equals(targetType)) {
			String GRID_FACTOR_STATION = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "GRID_FACTOR_STATION",
					IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
			String GRID_FACTOR_SERVICE = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "GRID_FACTOR_SERVICE",
					IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
			String GRID_FACTOR_URL = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "GRID_FACTOR_URL",
					IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
			MixedGridInfo grid = mixedGridInfoService.findMixedGridInfoById(wid, false);
			gridId = wid;
			name = grid.getGridName();
			parentGridId = grid.getParentGridId();
			gridLevel = grid.getGridLevel();
			map.addAttribute("FACTOR_STATION", GRID_FACTOR_STATION);
			map.addAttribute("FACTOR_SERVICE", GRID_FACTOR_SERVICE);
			map.addAttribute("FACTOR_URL", GRID_FACTOR_URL);
		}else if("build".equals(targetType)) {
			String BUILD_FACTOR_STATION = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "BUILD_FACTOR_STATION",
					IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
			String BUILD_FACTOR_SERVICE = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "BUILD_FACTOR_SERVICE",
					IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
			String BUILD_FACTOR_URL = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "BUILD_FACTOR_URL",
					IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
			String USE_DRAW_BUILDING_OUTLINE = this.funConfigurationService.turnCodeToValue("USE_DRAW_BUILDING_OUTLINE", "",
					IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
			AreaBuildingInfo building = areaBuildingInfoService.findAreaBuildingInfoById(wid);
			gridId = building.getGridId();
			name = building.getBuildingName();
			parentGridId = gridId;
			map.addAttribute("FACTOR_STATION", BUILD_FACTOR_STATION);
			map.addAttribute("FACTOR_SERVICE", BUILD_FACTOR_SERVICE);
			map.addAttribute("FACTOR_URL", BUILD_FACTOR_URL);
			map.addAttribute("USE_DRAW_BUILDING_OUTLINE", USE_DRAW_BUILDING_OUTLINE);
		}else if("segmentgrid".equals(targetType)) {//片区网格
			SegmentGrid segmentgrid = segmentGridService.findSegmentGridById(wid,null);
			gridId = segmentgrid.getParentGridId();
			name = segmentgrid.getSegmentGridName();
			parentGridId = segmentgrid.getParentGridId();
		}else if("lyjjBuilding".equals(targetType)) {//楼宇经济-》楼栋
			BuildingBo building = buildingService.getBuiding(wid);
			String gridCode = building.getBuildingGridCode();
			MixedGridInfo info = getGridInfoByOrgCode(gridCode);
			gridId = info.getGridId();
			name = building.getBuildingName();
			parentGridId = gridId;
		}else if("hlhx".equals(targetType)) {//护路护线
			CareRoad careRoad=careRoadService.findCareRoadById(wid);
			gridId = careRoad.getGridId();
			parentGridId = careRoad.getGridId();
			name = careRoad.getLotName();
		}else if("point".equals(targetType)){
			PointInfo pointInfo=pointInfoService.searchById(wid);
			String infoOrgCode = pointInfo.getInfoOrgCode();
			MixedGridInfo info = getGridInfoByOrgCode(infoOrgCode);
			gridId=info.getGridId();
			parentGridId=gridId;
			name=pointInfo.getPointName();
		}
		map.addAttribute("isDetail",isDetail);
		map.addAttribute("targetType", targetType);
		map.addAttribute("projectPath", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath());
		map.addAttribute("parentGridId", parentGridId+"");
		map.addAttribute("gridId", gridId+"");
		map.addAttribute("wid", wid.toString());
		map.addAttribute("gridLevel", gridLevel.toString());
		map.addAttribute("name", name);
		return forward;
	}
	
	/**
	 * 2014-06-05 liushi add
	 * 保存轮廓编辑的结果
	 * 1、分网格轮廓编辑和楼宇轮廓编辑两种
	 * 
	 * @param session
	 * @param request
	 * @param map
	 * @param targetType
	 * @param wid
	 * @param mapt
	 * @param x
	 * @param y
	 * @param levnum
	 * @param hs
	 * @param color
	 * @param mapCenterLevel
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/saveArcgisDrawAreaPanel")
	public Map<String, Object> saveArcgisDrawAreaPanel(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="targetType") String targetType
			,@RequestParam(value="wid") Long wid
			,@RequestParam(value="mapt") Integer mapt
			,@RequestParam(value="x") Double x
			,@RequestParam(value="y") Double y
			,@RequestParam(value="hs") String hs
			,@RequestParam(value="lineColor") String lineColor
			,@RequestParam(value="lineWidth") Integer lineWidth
			,@RequestParam(value="areaColor") String areaColor
			,@RequestParam(value="nameColor") String nameColor
			,@RequestParam(value="colorNum", required = false) Float colorNum
			,@RequestParam(value="mapCenterLevel", required=false) Integer mapCenterLevel) throws Exception{
		
		boolean flag = false;
		UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");
		if("grid".equals(targetType)) {
			ArcgisInfoOfGrid arcgisInfoOfGrid = new ArcgisInfoOfGrid();
			arcgisInfoOfGrid.setWid(wid);
			arcgisInfoOfGrid.setMapt(mapt);
			arcgisInfoOfGrid.setX(x);
			arcgisInfoOfGrid.setY(y);
			arcgisInfoOfGrid.setHs(hs);
			arcgisInfoOfGrid.setAreaColor(areaColor);
			arcgisInfoOfGrid.setNameColor(nameColor);
			arcgisInfoOfGrid.setLineColor(lineColor);
			arcgisInfoOfGrid.setLineWidth(lineWidth);
			if(colorNum !=null){
				arcgisInfoOfGrid.setColorNum(colorNum);
			}
			arcgisInfoOfGrid.setMapCenterLevel(mapCenterLevel);
			flag = this.arcgisInfoService.saveArcgisDrawAreaOfGrid(arcgisInfoOfGrid);
		}else if("build".equals(targetType)) {
			ArcgisInfoOfBuild arcgisInfoOfBuild = new ArcgisInfoOfBuild();
			arcgisInfoOfBuild.setWid(wid);
			arcgisInfoOfBuild.setMapt(mapt);
			arcgisInfoOfBuild.setX(x);
			arcgisInfoOfBuild.setY(y);
			arcgisInfoOfBuild.setHs(hs);
			arcgisInfoOfBuild.setAreaColor(areaColor);
			arcgisInfoOfBuild.setNameColor(nameColor);
			arcgisInfoOfBuild.setLineColor(lineColor);
			arcgisInfoOfBuild.setLineWidth(lineWidth);
			if(colorNum !=null){
				arcgisInfoOfBuild.setColorNum(colorNum);
			}
			arcgisInfoOfBuild.setLineWidth(lineWidth);
			arcgisInfoOfBuild.setUpdateUser(userInfo.getUserId());
			flag = this.arcgisInfoService.saveArcgisDrawAreaOfBuild(arcgisInfoOfBuild);
		}else if ("segmentgrid".equals(targetType)) {
			ArcgisInfoOfSegmentGrid arcgisInfoOfSegmentGrid = new ArcgisInfoOfSegmentGrid();
			arcgisInfoOfSegmentGrid.setWid(wid);
			arcgisInfoOfSegmentGrid.setMapt(mapt);
			arcgisInfoOfSegmentGrid.setX(x);
			arcgisInfoOfSegmentGrid.setY(y);
			arcgisInfoOfSegmentGrid.setHs(hs);
			flag = this.arcgisInfoService.saveArcgisDrawAreaOfSegmentGrid(arcgisInfoOfSegmentGrid);
		}else if("lyjjBuilding".equals(targetType)){
			ArcgisInfoOfBuildingBo arcgisInfoOfBuildingBo = new ArcgisInfoOfBuildingBo();
			arcgisInfoOfBuildingBo.setWid(wid);
			arcgisInfoOfBuildingBo.setMapt(mapt);
			arcgisInfoOfBuildingBo.setX(x);
			arcgisInfoOfBuildingBo.setY(y);
			arcgisInfoOfBuildingBo.setHs(hs);
			arcgisInfoOfBuildingBo.setAreaColor(areaColor);
			arcgisInfoOfBuildingBo.setNameColor(nameColor);
			arcgisInfoOfBuildingBo.setLineColor(lineColor);
			arcgisInfoOfBuildingBo.setLineWidth(lineWidth);
			flag = lyjjArcgisInfoService.saveArcgisInfoOfBuildingBo(arcgisInfoOfBuildingBo);
		}else if("hlhx".equals(targetType)) {
			ArcgisInfoOfPublic arcgisInfoOfPublic = new ArcgisInfoOfPublic();
			arcgisInfoOfPublic.setWid(wid);
			arcgisInfoOfPublic.setMapt(mapt);
			arcgisInfoOfPublic.setMarkerType(targetType);
			arcgisInfoOfPublic.setX(x);
			arcgisInfoOfPublic.setY(y);
			arcgisInfoOfPublic.setHs(hs);
			arcgisInfoOfPublic.setNameColor(nameColor);
			arcgisInfoOfPublic.setLineColor(lineColor);
			arcgisInfoOfPublic.setLineWidth(lineWidth);
			if(colorNum !=null){
				arcgisInfoOfPublic.setColorNum(colorNum);
			}
			flag = this.arcgisInfoService.saveArcgisDrawAreaOfResources(arcgisInfoOfPublic);
		}else if("point".equals(targetType)){
			ArcgisInfoOfPoint arcgisInfoOfPoint = new ArcgisInfoOfPoint();
			arcgisInfoOfPoint.setWid(wid);
			arcgisInfoOfPoint.setMapt(mapt);
			arcgisInfoOfPoint.setX(x);
			arcgisInfoOfPoint.setY(y);
			arcgisInfoOfPoint.setHs(hs);
			arcgisInfoOfPoint.setAreaColor(areaColor);
			arcgisInfoOfPoint.setNameColor(nameColor);
			arcgisInfoOfPoint.setLineColor(lineColor);
			arcgisInfoOfPoint.setLineWidth(lineWidth);
			if(colorNum !=null){
				arcgisInfoOfPoint.setColorNum(colorNum);
			}
			arcgisInfoOfPoint.setType(ConstantValue.POINT_INFO_TYPE);
			flag = this.arcgisInfoService.saveArcgisDrawPoint(arcgisInfoOfPoint);
		}
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("flag", flag);
		return result;
	}
	@ResponseBody
	@RequestMapping(value="/deleteArcgisDrawAreaPanel")
	public Map<String, Object> deleteArcgisDrawAreaPanel(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="targetType") String targetType
			,@RequestParam(value="wid") Long wid
			,@RequestParam(value="mapt") Integer mapt) throws Exception{
		
		boolean flag = false;
		if("grid".equals(targetType)) {
			ArcgisInfoOfGrid arcgisInfoOfGrid = new ArcgisInfoOfGrid();
			arcgisInfoOfGrid.setWid(wid);
			arcgisInfoOfGrid.setMapt(mapt);
			flag = this.arcgisInfoService.deleteArcgisDrawAreaOfGrid(arcgisInfoOfGrid);
		}else if("build".equals(targetType)) {
			ArcgisInfoOfBuild arcgisInfoOfBuild = new ArcgisInfoOfBuild();
			arcgisInfoOfBuild.setWid(wid);
			arcgisInfoOfBuild.setMapt(mapt);
			flag = this.arcgisInfoService.deleteArcgisDrawAreaOfBuild(arcgisInfoOfBuild);
		}else if("segmentgrid".equals(targetType)) {
			ArcgisInfoOfSegmentGrid arcgisInfoOfSegmentGrid = new ArcgisInfoOfSegmentGrid();
			arcgisInfoOfSegmentGrid.setWid(wid);
			arcgisInfoOfSegmentGrid.setMapt(mapt);
			flag = this.arcgisInfoService.deleteArcgisDrawAreaOfSegmentGrid(arcgisInfoOfSegmentGrid);
		}else if("lyjjBuilding".equals(targetType)) {
			ArcgisInfoOfBuildingBo arcgisInfoOfBuildingBo = new ArcgisInfoOfBuildingBo();
			arcgisInfoOfBuildingBo.setWid(wid);
			arcgisInfoOfBuildingBo.setMapt(mapt);
			flag = lyjjArcgisInfoService.deleteArcgisInfoOfBuildingBo(arcgisInfoOfBuildingBo);
		}else if("hlhx".equals(targetType)){
			ArcgisInfoOfPublic arcgisInfoOfPublic = new ArcgisInfoOfPublic();
			arcgisInfoOfPublic.setWid(wid);
			arcgisInfoOfPublic.setMapt(mapt);
			arcgisInfoOfPublic.setMarkerType(targetType);
			flag = this.arcgisInfoService.deleteArcgisDrawAreaOfResources(arcgisInfoOfPublic);
		}else if("point".equals(targetType)) {
			ArcgisInfoOfPoint arcgisInfoOfPoint = new ArcgisInfoOfPoint();
			arcgisInfoOfPoint.setWid(wid);
			arcgisInfoOfPoint.setMapt(mapt);
			arcgisInfoOfPoint.setType(ConstantValue.POINT_INFO_TYPE);
			flag = this.arcgisInfoService.deleteArcgisDrawAreaOfPoint(arcgisInfoOfPoint);
		}
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("flag", flag);
		return result;
	}
	/**
	 * 2014-06-11 liushi add
	 * 在画网格时显示本身以及周边的网格轮廓进行参考
	 * 1,先查询父节点，然后查询父节点以及其直属子节点的轮廓数据
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDrawDataOfGrids")
	public List<ArcgisInfoOfGrid> getArcgisDrawDataOfGrids(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="gridId") Long gridId
			,@RequestParam(value="mapt") Integer mapt) {
		List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDrawDataOfGrids(gridId, mapt);
		for(ArcgisInfoOfGrid arcgisInfoOfGrid:list) {
			if(gridId.equals(arcgisInfoOfGrid.getWid()) ) {
				arcgisInfoOfGrid.setEditAble(true);
				break;
			}
		}
		return list;
	}
	/**
	 * 2014-11-03 liushi add 画地图轮廓的时候需要同时显示周边的网格以用作对比
	 * 根据showType分别查询父级、自身、本级、下级的网格轮廓数据
	 * showType：0 代表自身轮廓数据查询  1代表父级轮廓数据查询  2代表本级（自己除外）轮廓数据查询 3、代表下级轮廓数据查询
	 * @param gridId
	 * @param mapt
	 * @param showType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDrawDataOfGridsByLevel")
	public List<ArcgisInfoOfGrid> getArcgisDrawDataOfGridsByLevel(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="gridId") Long gridId
			,@RequestParam(value="mapt") Integer mapt
			,@RequestParam(value="showType") String showType) {
		List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDrawDataOfGridsByLevel(gridId, mapt,showType);
		for(ArcgisInfoOfGrid arcgisInfoOfGrid:list) {
			if(gridId.equals(arcgisInfoOfGrid.getWid()) ) {
				arcgisInfoOfGrid.setEditAble(true);
//				arcgisInfoOfGrid.setLineColor("#000000");
//				arcgisInfoOfGrid.setNameColor("#000000");
				break;
			}
		}
		return list;
	}
	@ResponseBody
	@RequestMapping(value="/getArcgisDataCenterAndLevel")
	public Map<String, Object> getArcgisDataCenterAndLevel(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="wid") Long wid
			,@RequestParam(value="mapt") Integer mapt
			,@RequestParam(value="targetType") String targetType) {
		
		ArcgisInfoOfGrid result = null;
		if("grid".equals(targetType)) {
			List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDrawDataOfGrids(wid, mapt);
			for(ArcgisInfoOfGrid arcgisInfoOfGrid:list) {
				if(wid.equals(arcgisInfoOfGrid.getWid()) ) {
					result = arcgisInfoOfGrid;
				}
			}
			if(result == null && list.size()>0) {
				result = list.get(0);
			}
		}else if("build".equals(targetType)) {
			AreaBuildingInfo areaBuildingInfo = areaBuildingInfoService.findAreaBuildingInfoById(wid);
			List<ArcgisInfoOfGrid> listGrid = this.arcgisInfoService.getArcgisDrawDataOfGrids(areaBuildingInfo.getGridId(), mapt);
			ArcgisInfoOfGrid gridResult = null;
			for(ArcgisInfoOfGrid arcgisInfoOfGrid:listGrid) {
				if(areaBuildingInfo.getGridId().equals(arcgisInfoOfGrid.getWid()) ) {
					gridResult = arcgisInfoOfGrid;
				}
			}
			String infoOrgCode = "";
			if(gridResult != null && StringUtils.isNotBlank(gridResult.getInfoOrgCode())){
				infoOrgCode = gridResult.getInfoOrgCode();
			}
			//楼宇地图默认显示层级
			String DEFAULT_DISPLAY_GIS_LVL = 
						this.funConfigurationService.turnCodeToValue("DEFAULT_DISPLAY_GIS_LVL", "", 
																		IFunConfigurationService.CFG_TYPE_FACT_VAL, 
																		StringUtils.isNotBlank(infoOrgCode)?infoOrgCode:"",
																		null,
																		IFunConfigurationService.DIRECTION_UP);
			
			List<ArcgisInfoOfBuild> list = this.arcgisInfoService.getArcgisDrawDataOfBuildsByGridId(areaBuildingInfo.getGridId(), mapt);
			for(ArcgisInfoOfBuild arcgisInfoOfBuild:list) {
				if(wid.equals(arcgisInfoOfBuild.getWid())) {
					result = new ArcgisInfoOfGrid();
					result.setWid(wid);
					result.setX(arcgisInfoOfBuild.getX());
					result.setY(arcgisInfoOfBuild.getY());
					if(DEFAULT_DISPLAY_GIS_LVL != null && !"".equals(DEFAULT_DISPLAY_GIS_LVL)){
						result.setMapCenterLevel(Integer.parseInt(DEFAULT_DISPLAY_GIS_LVL));
					} else {
						if(gridResult != null) {
							result.setMapCenterLevel(gridResult.getMapCenterLevel());
						}
					}
					
				}
			}
			if(result == null) {
				List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoService.getArcgisDataOfGrid(areaBuildingInfo.getGridId() ,mapt);
				if(arcgisInfoOfGridList.size() == 1) {
					result = arcgisInfoOfGridList.get(0);
					if(DEFAULT_DISPLAY_GIS_LVL != null && !"".equals(DEFAULT_DISPLAY_GIS_LVL)){
						result.setMapCenterLevel(Integer.parseInt(DEFAULT_DISPLAY_GIS_LVL));
					} else {
						if(gridResult != null) {
							result.setMapCenterLevel(gridResult.getMapCenterLevel());
						}
					}
				}
			}
			
		}else if("lyjjBuilding".equals(targetType)){
			BuildingBo building = buildingService.getBuiding(wid);
			String gridCode = building.getBuildingGridCode();
			MixedGridInfo info = getGridInfoByOrgCode(gridCode);
			Long gridId = info.getGridId();
			
			List<ArcgisInfoOfBuildingBo> list = this.lyjjArcgisInfoService.getArcgisInfoOfBuildingBos(wid, mapt);
			for(ArcgisInfoOfBuildingBo lyjjbuiding:list) {
				if(wid.equals(lyjjbuiding.getWid())) {
					result = new ArcgisInfoOfGrid();
					result.setWid(wid);
					result.setX(lyjjbuiding.getX());
					result.setY(lyjjbuiding.getY());
					result.setMapCenterLevel(14);
				}
			}
			if(result == null) {
				List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,mapt);
				if(arcgisInfoOfGridList.size() == 1) {
					result = arcgisInfoOfGridList.get(0);
				}
			}
		}else if("hlhx".equals(targetType)) {
			List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisDataOfResourcesSelfById(wid, mapt, targetType);
			for(ArcgisInfoOfPublic arcgisInfoOfResources:list) {
				if(wid.equals(arcgisInfoOfResources.getWid())) {
					result = new ArcgisInfoOfGrid();
					result.setWid(wid);
					result.setX(arcgisInfoOfResources.getX());
					result.setY(arcgisInfoOfResources.getY());
					result.setMapCenterLevel(14);
				}
			}
			if(result == null) {
				CareRoad careRoad=careRoadService.findCareRoadById(wid);
				List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoService.getArcgisDataOfGrid(careRoad.getGridId(),mapt);
				if(arcgisInfoOfGridList.size() == 1) {
					result = arcgisInfoOfGridList.get(0);
				}
			}
		}else if("point".equals(targetType)) {
			PointInfo point=pointInfoService.searchById(wid);
			String pOrgCode="";
			if(point!=null){
				pOrgCode=point.getInfoOrgCode();
			}
			MixedGridInfo mix=mixedGridInfoService.getDefaultGridByOrgCode(pOrgCode);
			List<ArcgisInfoOfGrid> listGrid = this.arcgisInfoService.getArcgisDrawDataOfGrids(mix.getGridId(), mapt);
			ArcgisInfoOfGrid gridResult = null;
			for(ArcgisInfoOfGrid arcgisInfoOfGrid:listGrid) {
				if(mix.getGridId().equals(arcgisInfoOfGrid.getWid()) ) {
					gridResult = arcgisInfoOfGrid;
				}
			}
			UserInfo userInfo = (UserInfo) session.getAttribute("USER_IN_SESSION");
			String infoOrgCode = userInfo.getOrgCode();
			if(gridResult != null && StringUtils.isNotBlank(gridResult.getInfoOrgCode())){
				infoOrgCode = gridResult.getInfoOrgCode();
			}
			//默认显示层级
			String DEFAULT_DISPLAY_GIS_LVL = 
						this.funConfigurationService.turnCodeToValue("DEFAULT_DISPLAY_GIS_LVL", "", 
																		IFunConfigurationService.CFG_TYPE_FACT_VAL, 
																		StringUtils.isNotBlank(infoOrgCode)?infoOrgCode:"",
																		null,
																		IFunConfigurationService.DIRECTION_UP);
			List<ArcgisInfoOfPoint> list= this.arcgisInfoService.getArcgisDrawDataOfPointsByCode(pOrgCode, mapt);
			for(ArcgisInfoOfPoint arcgisInfoOfPoint:list) {
				if(wid.equals(arcgisInfoOfPoint.getWid())) {
					result = new ArcgisInfoOfGrid();
					result.setWid(wid);
					result.setX(arcgisInfoOfPoint.getX());
					result.setY(arcgisInfoOfPoint.getY());
					if(DEFAULT_DISPLAY_GIS_LVL != null && !"".equals(DEFAULT_DISPLAY_GIS_LVL)){
						result.setMapCenterLevel(Integer.parseInt(DEFAULT_DISPLAY_GIS_LVL));
					} else {
						if(gridResult != null) {
							result.setMapCenterLevel(gridResult.getMapCenterLevel());
						}
					}
					
				}
			}
			if(result == null) {
				List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoService.getArcgisDataOfGrid(mix.getGridId() ,mapt);
				if(arcgisInfoOfGridList.size() == 1) {
					result = arcgisInfoOfGridList.get(0);
					if(DEFAULT_DISPLAY_GIS_LVL != null && !"".equals(DEFAULT_DISPLAY_GIS_LVL)){
						result.setMapCenterLevel(Integer.parseInt(DEFAULT_DISPLAY_GIS_LVL));
					} else {
						if(gridResult != null) {
							result.setMapCenterLevel(gridResult.getMapCenterLevel());
						}
					}
				}
			}
			
		}
		Map<String, Object> res = new HashMap<String,Object>();
		res.put("result", result);
		return res;
	}
	
	/**
	 * 2014-10-10 huangmw add
	 * 查询当前片区网格的轮廓数据
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfSegmentGrid")
	public Map<String,Object> getArcgisDataOfSegmentGrid(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="wid") Long wid
			,@RequestParam(value="parentGridId") long parentGridId
			,@RequestParam(value="mapt") Integer mapt) {
		Map<String,Object> result = new HashMap<String, Object>();
		List<ArcgisInfoOfSegmentGrid> list = this.arcgisInfoService.getArcgisDataOfSegmentGrid(wid, parentGridId, mapt);
		result.put("list", list);
		return result;
	}
	
	/**
	 * 2014-10-31 chenlf add
	 * 获取防空区域片区网格
	 * @param gridId
	 * @param gridCode
	 * @param map
	 * @param gridLevel
	 * @param mapt
	 * @param segmentType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfFkqyGrids")
	public List<ArcgisInfoOfPublic> getArcgisDataOfFkqyGrids(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="gridId") Long gridId
			,@RequestParam(value="gridCode") String gridCode
			,@RequestParam(value="gridLevel") Long gridLevel
			,@RequestParam(value="mapt") Integer mapt
			,@RequestParam(value="segmentType", required = false) String segmentType){
		
		
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisSegmentGridDataListByParentGridId(gridId, mapt);
		
		//var geoString=data.hs.replace("8 ","");
//		if("quyufangkong".equals(segmentType)) {
//			for(ArcgisInfoOfPublic arcgisInfo : list) {
//				String hs = arcgisInfo.getHs();
//				hs = hs.replaceFirst("8 ", "");
//				hs = hs.replace(" ", ",");
//				System.out.println(hs);
//				int qqyCount = gisInfoService.getMarkedListboxChose(hs, ResMarkedOM.MEGAEYES, ResMarkedOM.RESOURCES, String.valueOf(mapt));//全球眼
//				//arcgisInfo.setNamePath(arcgisInfo.getName()+"（"+String.valueOf(qqyCount)+"个监控）");
//				arcgisInfo.setName("监控数："+String.valueOf(qqyCount));
//			}
//		}
		
		return list;
	}
	
	/**
	 * 2014-06-11 liushi add
	 * 页面显示时会显示当前网格下级的网格（有网格层级进行约束判断）
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfGrids")
	public List<ArcgisInfoOfGrid> getArcgisDataOfGrids(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="gridId") Long gridId
			,@RequestParam(value="gridCode") String gridCode
			,@RequestParam(value="gridLevel") Long gridLevel
			,@RequestParam(value="mapt") Integer mapt) {
		List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrids(gridId, gridLevel,mapt);
		return list;
	}
	/**
	 * 2015-07-08 liushi add
	 * 根据网格id和网格层级查询符合条件的gridId集合
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @param gridLevel
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataIdsOfGrids")
	public Map<String,Object> getArcgisDataIdsOfGrids(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="gridId") Long gridId
			,@RequestParam(value="gridLevel") Long gridLevel
			,@RequestParam(required = false) String orgCode
			,@RequestParam(required = false) String homePageType
			,@RequestParam(required = false) Long gdcId
			,@RequestParam(required = false) Integer isRootSearch) {
		Map<String,Object> result = new HashMap<String,Object>();
		String gridIdsStr = this.arcgisInfoService.getArcgisDataIdsOfGrids(gridId, gridLevel);
		
		GisDataCfg gisDataCfg = new GisDataCfg();
		// 网格宽高可配置
		GisDataCfg parentGisDataCfg = this.menuConfigService.getGisDataCfgRelationTreeVersionTwo(orgCode,homePageType, gdcId,isRootSearch);
		
		List<GisDataCfg> gisDataCfgs = parentGisDataCfg.getChildrenList();
		
		for (GisDataCfg gisDataCfg1 : gisDataCfgs) {
			if ("地".equals(gisDataCfg1.getMenuName())) {
				parentGisDataCfg = this.menuConfigService.getGisDataCfgRelationTreeVersionTwo(orgCode,homePageType, gisDataCfg1.getGdcId(),isRootSearch);
				
				gisDataCfgs = parentGisDataCfg.getChildrenList();
				
				for (GisDataCfg gisDataCfg2 : gisDataCfgs) {
					if ("网格".equals(gisDataCfg2.getMenuName())) {
						gisDataCfg = gisDataCfg2;
						break;
					}
 				}
			}
		}
		
		
//		String[] strs = gridIdsStr.split(",");
//		String resultStr = "";
//		for(int i=0; i<strs.length;i++) {
//			resultStr+=("".equals(resultStr))? "'"+strs[i]+"'":",'"+strs[i]+"'";
//		}
		result.put("gridIdsStr", gridIdsStr);
		result.put("gisDataCfg", gisDataCfg);
		return result;
	}
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfGridsListByIds")
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridsListByIds(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "ids") String ids, @RequestParam(value = "mapt") Integer mapt,
			 @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGridsListByIds(ids, mapt);
		for(ArcgisInfoOfGrid arcgisInfoOfGrid:list){
			arcgisInfoOfGrid.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfHlhxListByIds")
	public List<ArcgisInfoOfHlhx> getArcgisDataOfHlhxListByIds(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "ids") String ids, @RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfHlhx> list = this.arcgisInfoService.getArcgisDataOfHlhxListByIds(ids, mapt);
		for(ArcgisInfoOfHlhx arcgisInfoOfHlhx:list){
			arcgisInfoOfHlhx.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfGridsByLevels")
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridsByLevels(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="gridId") Long gridId
			,@RequestParam(value="gridCode") String gridCode
			,@RequestParam(value="gridLevels") String gridLevels
			,@RequestParam(value="mapt") Integer mapt) {
		List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGridsByLevels(gridCode, gridLevels,mapt);
		return list;
	}
	@RequestMapping(value = "/getGridInfoDetailOnMap")
	public String getGridInfoDetailOnMap(HttpSession session, ModelMap map,
			@RequestParam(value = "gridId", required = false) Long gridId,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode) {
		int[] counts=this.STAT_OF_GRID_SQ;
		String[] gridCodes = this.GRID_CODE;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		MixedGridInfo gridInfo = null;
		if (gridId != null && gridId > 0L) {
			gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		} else if (StringUtils.isNotBlank(infoOrgCode)) {
			gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(infoOrgCode);
		}
		List<String> gridLevelNames = CommonFunctions.fetchGridLevelNames(userInfo.getOrgCode());
		//List<Map<String,Object>> statofgridlist = mixedGridInfoService.statOfGrid(gridInfo.getParentGridId());

		List<MixedGridInfo> mixedGridInfoList = mixedGridInfoService.getMixedGridListByParentId(gridInfo.getParentGridId(),false,false);
		String statOfGridStr = null;

		MixedGridInfo parentMixedGridInfo = mixedGridInfoList.get(0);
		Map<String, Object> adminParams = new HashMap<>();
		if(!mixedGridInfoList.isEmpty()&&"3501".equals((String)parentMixedGridInfo.getInfoOrgCode())){
			for(int j=0;j<mixedGridInfoList.size();j++) {
				MixedGridInfo mixedGridInfo = mixedGridInfoList.get(j);
				if(mixedGridInfo.getInfoOrgCode() != null && mixedGridInfo.getInfoOrgCode().equals(gridInfo.getInfoOrgCode())){
					for(int x=0;x<gridCodes.length;x++) {
						if(mixedGridInfo.getInfoOrgCode().equals(gridCodes[x])) {
							Map<String, Object> countMap = mixedGridInfoService.countGridNumByLevel(mixedGridInfo.getGridId());
							statOfGridStr = "街镇："+String.valueOf(countMap.get("streetCount"));
							statOfGridStr = statOfGridStr + "&nbsp;社区|村："+String.valueOf(counts[x])+"|"+String.valueOf(((BigDecimal)(countMap.get("villageCount"))).intValue()-counts[x]);
							statOfGridStr = statOfGridStr + "&nbsp;网格："+String.valueOf(countMap.get("gridCount"));
							//statOfGridStr = statOfGridStr + "<br/>网&nbsp;格&nbsp;员："+String.valueOf(statofgridlist.get(j).get("COUNTADMIN"));

							//查询网格员
							adminParams.put("startGridId",mixedGridInfo.getGridId());
							int countadmin = gridAdminService.findCountGridAdmin(adminParams);
							map.addAttribute("countadmin", countadmin);
							adminParams.put("duty","001");
							int countgridadmin = gridAdminService.findCountGridAdmin(adminParams);
							map.addAttribute("countgridadmin",countgridadmin);
						}
					}
				}
			}
		}else {
			for(int j=0;j<mixedGridInfoList.size();j++) {
				if(mixedGridInfoList.get(j).getInfoOrgCode().equals(gridInfo.getInfoOrgCode())){
					String gridName4 = CommonFunctions.transGridLevelName(gridLevelNames, 4);
					gridName4 = "".equals(gridName4) ? "乡镇（街道）" : gridName4;
					String gridName5 = CommonFunctions.transGridLevelName(gridLevelNames, 5);
					gridName5 = "".equals(gridName5) ? "村（社区）" : gridName5;
					String gridName6 = CommonFunctions.transGridLevelName(gridLevelNames, 6);
					gridName6 = "".equals(gridName6) ? "网格" : gridName6;
					Map<String, Object> countMap = mixedGridInfoService.countGridNumByLevel(mixedGridInfoList.get(j).getGridId());
					statOfGridStr = gridName4 + "："+String.valueOf(countMap.get("streetCount"));
					statOfGridStr = statOfGridStr + "&nbsp;"+gridName5+"："+String.valueOf(countMap.get("villageCount"));
					statOfGridStr = statOfGridStr + "&nbsp;"+gridName6+"："+String.valueOf(countMap.get("gridCount"));
					//statOfGridStr = statOfGridStr + "<br/>网&nbsp;&nbsp;格&nbsp;员："+String.valueOf(statofgridlist.get(j).get("COUNTADMIN"));

					//查询网格员
					adminParams.put("startGridId",mixedGridInfoList.get(j).getGridId());
					int countadmin = gridAdminService.findCountGridAdmin(adminParams);
					map.addAttribute("countadmin", countadmin);
					adminParams.put("duty","001");
					int countgridadmin = gridAdminService.findCountGridAdmin(adminParams);
					map.addAttribute("countgridadmin",countgridadmin);
					break;
				}
			}
		}
		if(statOfGridStr != null && !"".equals(statOfGridStr)) {
			map.addAttribute("statOfGridStr",statOfGridStr);
		}

		infoOrgCode = gridInfo.getInfoOrgCode();

		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
		}

		gridInfo.setGridName(gridNames);
		map.addAttribute("gridInfo", gridInfo);

		// -- 网格管理员
		GridAdmin gridAdmin = gridAdminService.getGridAdminByGridIdAndDuty(gridId, ConstantValue.GRID_ADMIN_DUTY_001);
		map.addAttribute("gridAdmin", gridAdmin);

		// -- 网格统计信息
		//List<StatisticsInfo> orgStatInfoList = gridStatisticsService.getStatisticsInfoForGridId(ConstantValue.STAT_TYPE_ID_FOR_RESIDENTGRID, gridInfo.getGridId());
		// -- modify by guohh 2013.11.21 通过组织查找，后台数据已经联系数据库组进行相应改造
		List<StatisticsInfo> orgStatInfoList = gridStatisticsService.getLastStatisticsInfoForGridIntroByGridId(gridId);
		//-- modify by guohh 2014.1.2 海沧街道过滤空房间数
		this.filterGridStatInfo(orgStatInfoList, userInfo.getOrgCode());
		map.addAttribute("orgStatInfoList", orgStatInfoList);
		List<Map<String,Object>> list =null;
		if(gridInfo!=null && StringUtils.isNotBlank(gridInfo.getInfoOrgCode()) && gridInfo.getInfoOrgCode().startsWith(ConstantValue.YANDU_FUNC_ORG_CODE)) {
			list=gisStatService.getDisputeRectify(gridId,"my");
		}else {
			list=gisStatService.getDisputeRectify(gridId);			
		}
		int countDispute =0;
		for (Map<String, Object> map2 : list) {
			String total = map2.get("TOTAL_")+"";
			countDispute = countDispute+Integer.valueOf(total);
		}
		map.addAttribute("countDispute", countDispute);
		Map<String, Object> rsMap = ciRsService.getStatFromBi(infoOrgCode);
		if (gridInfo.getInfoOrgCode().startsWith(ConstantValue.JINJIANG_FUNC_ORG_CODE)) { // 晋江
			map.put("jj", "jj");
			if (rsMap != null) {
				int hjCount = Integer.valueOf(rsMap.get("hjrk").toString());//户籍人口
				int ppCount = Integer.valueOf(rsMap.get("xqwczrk").toString());//辖区外常住人口

				map.put("czCount", hjCount + ppCount); //常住人口
				map.put("hjCount", hjCount); //户籍人口
				map.put("ppCount", ppCount); //辖区外常住人口
				map.put("floatCount", rsMap.get("ldrk")); //流动人口
				map.put("totalCount", rsMap.get("total")); //总人口
			}

		} else if (gridInfo.getInfoOrgCode().startsWith(ConstantValue.NANCHANG_FUNC_ORG_CODE)) {//南昌个性化
			if (rsMap != null) {
				map.put("nc", "nc");
				map.put("hjCount", rsMap.get("hjrk")); //户籍人口
				map.put("floatCount", rsMap.get("ldrk")); //流动人口
				map.put("totalCount", rsMap.get("total")); //总人口
				map.put("otherCount", rsMap.get("otCount"));//其他人口
				map.put("ppCount", rsMap.get("czrk")); // 常住
				map.put("ghCount", rsMap.get("ghCount")); //挂户人口
			}
			map.put("countadmin", gridAdminService.countGridAdmin(gridInfo.getInfoOrgCode(),6)); //网格力量或网格员统计只统计终端网格
		} else if(gridInfo.getInfoOrgCode().startsWith(ConstantValue.YANDU_FUNC_ORG_CODE)) {//盐都个性化
			if (rsMap != null) {
				//map.addAttribute("BI_DOMAIN", App.BI.getDomain(session));
				//人
				map.put("hjCount", rsMap.get("hjrk")); //户籍人口
				map.put("floatCount", rsMap.get("ldrk")); //流动人口
				map.put("dzlCount", rsMap.get("SL2")); //待助类
				map.put("hglCount", rsMap.get("SL1"));//回归类
				map.put("rslCount", rsMap.get("SL3")); // 弱势类
				//地
				
				//获取楼栋数量
				//Map<String, Object> personParams = new HashMap<String, Object>();
				//personParams.put("gridId", gridId);
				//int buildingCount = areaBuildingInfoService.findCountByAreaBuildingInfo(personParams);
				map.put("lyCount", rsMap.get("lys")); // 楼宇we
				map.put("czwCount", rsMap.get("czws")); // 出租屋
				map.put("zzcsCount", rsMap.get("WN")); // 重点场所
				map.put("syxxCount", rsMap.get("syxx")); // 学校
				//事
				Map<String,Object> map5=new HashMap<String, Object>();
				map5.put("gridId", gridId);
				List<Map<String, Object>> map6=arcgisInfoService.getEventChildData(map5);
				Long total=0l;
				for (Map<String, Object> map2 : map6) {
					total+=Long.parseLong(map2.get("TOTAL").toString());
				}
				map.put("byxqsjCount", total); // 本月辖区事件
				//物
				map.put("qqyCount", rsMap.get("qqys")); // 全球眼
				//情
				//map.put("countDispute", countDispute); // 矛盾纠纷
				//组织
				map.put("wgyCount", gridAdminService.countGridAdmin(gridInfo.getInfoOrgCode(),6)); // 网格员
			}
		} 
		else if(gridInfo.getInfoOrgCode().startsWith(ConstantValue.YANCHENG_FUNC_ORG_CODE)) {//盐城个性化
			if (rsMap != null) {
				//map.addAttribute("BI_DOMAIN", App.BI.getDomain(session));
				//人
				map.put("hjCount", rsMap.get("hjrk")); //户籍人口
				map.put("floatCount", rsMap.get("ldrk")); //流动人口
				map.put("xjry", rsMap.get("xjry")); //邪教人员
				map.put("xdry", rsMap.get("xdry"));//吸毒人员
				map.put("sfry", rsMap.get("sfry")); // 上访人员
				map.put("zdqsn", rsMap.get("zdqsn")); // 重点青少年
				map.put("xmsfry", rsMap.get("xmsfry")); // 刑满释放人员
				map.put("zzjsbjl", rsMap.get("zzjsbjl")); // 重症精神病人员
				map.put("sqjzxx", rsMap.get("sqjzxx")); // 社区矫正人员
				map.put("azb", rsMap.get("azb")); // 艾滋病患者
				//地
				
				//获取楼栋数量
				//Map<String, Object> personParams = new HashMap<String, Object>();
				//personParams.put("gridId", gridId);
				//int buildingCount = areaBuildingInfoService.findCountByAreaBuildingInfo(personParams);
				map.put("lyCount", rsMap.get("lys")); // 楼宇we
				map.put("czwCount", rsMap.get("czws")); // 出租屋
				map.put("zzcsCount", rsMap.get("WN")); // 重点场所
				map.put("syxxCount", rsMap.get("syxx")); // 学校
				//事
				Map<String,Object> map5=new HashMap<String, Object>();
				map5.put("gridId", gridId);
				List<Map<String, Object>> map6=arcgisInfoService.getEventChildData(map5);
				Long total=0l;
				for (Map<String, Object> map2 : map6) {
					total+=Long.parseLong(map2.get("TOTAL").toString());
				}
				map.put("byxqsjCount", total); // 本月辖区事件
				//物
				map.put("qqyCount", rsMap.get("qqys")); // 全球眼
				//情
				//map.put("countDispute", countDispute); // 矛盾纠纷
				//组织
				map.put("wgyCount", gridAdminService.countGridAdmin(gridInfo.getInfoOrgCode(),6)); // 网格员
			}
		}
	else {
			if (rsMap != null) {
				map.put("hjCount", rsMap.get("hjrk")); //户籍人口
				map.put("floatCount", rsMap.get("ldrk")); //流动人口
				map.put("totalCount", rsMap.get("total")); //总人口
				map.put("otherCount", rsMap.get("otCount"));//其他人口
				map.put("ppCount", rsMap.get("czrk")); // 常住
				map.put("ghCount", rsMap.get("ghCount")); //挂户人口
			}
		}

		//常住
		//Long ppCount = mixedGridInfoService.getTotalByGridAndTypes(gridId, "'R'");
//		map.put("ppCount", ppCount);

		//流动
		//Long fpCount = mixedGridInfoService.getTotalByGridAndTypes(gridId, "'S'");
//		map.put("fpCount", fpCount);
		//盐都个性化
		if(userInfo!=null && StringUtils.isNotBlank(userInfo.getOrgCode()) && userInfo.getOrgCode().startsWith(ConstantValue.YANDU_FUNC_ORG_CODE)) {
			return "/map/arcgis/standardmappage/gridInfoDetailOnMap_yandu.ftl";
		}
		//盐城个性化，除盐都区外        暂不上线
		if(userInfo!=null && StringUtils.isNotBlank(userInfo.getOrgCode()) && userInfo.getOrgCode().startsWith(ConstantValue.YANCHENG_FUNC_ORG_CODE)) {
			map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
			List<WorkAttachment> attList = workAttachmentService.findByBizId(gridId,"gridHome");
			JSONArray array = new JSONArray();
			for (WorkAttachment workAttachment : attList) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("attachmentId",workAttachment.getAttId());
				jsonObject.put("path",workAttachment.getAttPath());
				jsonObject.put("fileName",workAttachment.getAttName());
				array.add(jsonObject);
			}
			map.put("attList",array.toString());
			return "/map/arcgis/standardmappage/gridInfoDetailOnMap_yanCheng.ftl";
		}
		return "/map/arcgis/standardmappage/gridInfoDetailOnMap.ftl";
	}

	@RequestMapping(value = "/getHBGridInfoDetailOnMap")
	public String getHBGridInfoDetailOnMap(HttpSession session
			,@RequestParam(value = "gridId") Long gridId
			,ModelMap map) {
		int[] counts=this.STAT_OF_GRID_SQ;
		String[] gridCodes = this.GRID_CODE;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		List<String> gridLevelNames = CommonFunctions.fetchGridLevelNames(userInfo.getOrgCode());
		List<Map<String,Object>> statofgridlist = mixedGridInfoService.statOfGrid(gridInfo.getParentGridId());
		String statOfGridStr = null;

		Map<String,Object> statofgrid = statofgridlist.get(0);
		if(!statofgridlist.isEmpty()&&"3501".equals((String)statofgrid.get("INFO_ORG_CODE"))){
			for(int j=0;j<statofgridlist.size();j++) {
				Map<String, Object> tMap = statofgridlist.get(j);
				if(tMap != null && tMap.get("INFO_ORG_CODE") != null && tMap.get("INFO_ORG_CODE").equals(gridInfo.getInfoOrgCode())){
					for(int x=0;x<gridCodes.length;x++) {
						if(tMap.get("INFO_ORG_CODE").equals(gridCodes[x])) {
							statOfGridStr = "街镇："+String.valueOf(statofgridlist.get(j).get("COUNT4"));
							statOfGridStr = statOfGridStr + "&nbsp;社区|村："+String.valueOf(counts[x])+"|"+String.valueOf(((BigDecimal)(statofgridlist.get(j).get("COUNT5"))).intValue()-counts[x]);
							statOfGridStr = statOfGridStr + "&nbsp;网格："+String.valueOf(statofgridlist.get(j).get("COUNT6"));
							//statOfGridStr = statOfGridStr + "<br/>网&nbsp;格&nbsp;员："+String.valueOf(statofgridlist.get(j).get("COUNTADMIN"));
							map.addAttribute("countgridadmin", String.valueOf(statofgridlist.get(j).get("COUNTGRIDADMIN")));
							map.addAttribute("countadmin", String.valueOf(statofgridlist.get(j).get("COUNTADMIN")));
						}
					}
				}
			}
		}else {
			for(int j=0;j<statofgridlist.size();j++) {
				if(statofgridlist.get(j).get("INFO_ORG_CODE").equals(gridInfo.getInfoOrgCode())){
					String gridName4 = CommonFunctions.transGridLevelName(gridLevelNames, 4);
					gridName4 = "".equals(gridName4) ? "乡镇（街道）" : gridName4;
					String gridName5 = CommonFunctions.transGridLevelName(gridLevelNames, 5);
					gridName5 = "".equals(gridName5) ? "村（社区）" : gridName5;
					String gridName6 = CommonFunctions.transGridLevelName(gridLevelNames, 6);
					gridName6 = "".equals(gridName6) ? "网格" : gridName6;
					statOfGridStr = gridName4 + "："+String.valueOf(statofgridlist.get(j).get("COUNT4"));
					statOfGridStr = statOfGridStr + "&nbsp;"+gridName5+"："+String.valueOf(statofgridlist.get(j).get("COUNT5"));
					statOfGridStr = statOfGridStr + "&nbsp;"+gridName6+"："+String.valueOf(statofgridlist.get(j).get("COUNT6"));
					//statOfGridStr = statOfGridStr + "<br/>网&nbsp;&nbsp;格&nbsp;员："+String.valueOf(statofgridlist.get(j).get("COUNTADMIN"));
					map.addAttribute("countgridadmin", String.valueOf(statofgridlist.get(j).get("COUNTGRIDADMIN")));
					map.addAttribute("countadmin", String.valueOf(statofgridlist.get(j).get("COUNTADMIN")));
					break;
				}
			}
		}
		if(statOfGridStr != null && !"".equals(statOfGridStr)) {
			map.addAttribute("statOfGridStr",statOfGridStr);
		}

		String infoOrgCode=gridInfo.getInfoOrgCode();

		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
		}

		gridInfo.setGridName(gridNames);
		map.addAttribute("gridInfo", gridInfo);

		Map<String, Object> param = null;
		//已办结量
		param =	new HashMap<String, Object>();
		param.put("gridId",gridId);
		param.put("statusList","03,04");
		param.put("userId",userInfo.getUserId());
		int hadEndEvent = eventDisposalService.findAllEventWorkFlowCount(param);
		map.addAttribute("hadEndEvent", hadEndEvent);
		//未办结量
		param =	new HashMap<String, Object>();
		param.put("gridId",gridId);
		param.put("statusList","00,01,02");
		param.put("userId",userInfo.getUserId());
		int noEndEvent = eventDisposalService.findAllEventWorkFlowCount(param);
		map.addAttribute("noEndEvent", noEndEvent);

		//企业
		CorBaseInfo obj = new CorBaseInfo();
		obj.setGridId(gridId);
		int corNum = corBaseInfoService.getCorpBaseInfoCount2(obj);
		map.addAttribute("corNum", corNum);
		return "/map/arcgis/standardmappage/gridInfoDetailOnMapHB.ftl";
	}
	public void filterGridStatInfo(List<StatisticsInfo> orgStatInfoList, String orgCode) {
		int emptyIndex = -1;
		for(int i=0; i<orgStatInfoList.size(); i++) {
			StatisticsInfo statInfo = orgStatInfoList.get(i);
			if(statInfo.getStatTypeName().contains("空房间")) {
				emptyIndex = i;
				break;
			}
		}
		if(emptyIndex>0) orgStatInfoList.remove(emptyIndex);
	}
	
	/**
	 * 2015-02-05 huangmw add
	 * 护路护线概要
	 * @param session
	 * @param lotId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getICareRoadInfoDetailOnMap")
	public String getICareRoadInfoDetailOnMap(HttpSession session,
			@RequestParam(value = "lotId") Long lotId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		CareRoad careRoad=careRoadService.findCareRoadById(lotId);
		List<BaseDataDict> secRouteTypeDC = baseDictionaryService.getDataDictListOfSinglestage("A001043274", userInfo.getOrgCode());
		
		for (int i = 0; i < secRouteTypeDC.size(); i++) {
			if(careRoad.getSecRouteType()!=null&&careRoad.getSecRouteType().equals(secRouteTypeDC.get(i).getDictGeneralCode())){
				String dictName = String.valueOf(secRouteTypeDC.get(i).getDictName());
				careRoad.setSecRouteTypeLabel(dictName);
				break;
			}
		}
		
		PrvetionTeam prvetionTeam = iCareRoadOrgService.findCareRoadOrgByLotId(lotId);
		Long gridId = careRoad.getGridId();
		String infoOrgCode1 = careRoad.getRegionCode();

		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode1);
		}
		map.addAttribute("gridNames", gridNames);
		
		map.addAttribute("careRoad", careRoad);
		map.addAttribute("prvetionTeam", prvetionTeam);
		return "/map/arcgis/standardmappage/iCareRoadDetail.ftl";
	}
	
	/**
	 * 2014-06-13 liushi add
	 * 查询当前网格的轮廓数据
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfGrid")
	public Map<String,Object> getArcgisDataOfGrid(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="wid") Long wid
			,@RequestParam(value="mapt") Integer mapt) {
		Map<String,Object> result = new HashMap<String, Object>();
		List<ArcgisInfoOfGrid> resultList = this.arcgisInfoService.getArcgisDrawDataOfGrids(wid, mapt);
		List<ArcgisInfoOfGrid> list = new ArrayList<ArcgisInfoOfGrid>();
		for(ArcgisInfoOfGrid arcgisInfoOfGrid:resultList) {
			if(wid.equals(arcgisInfoOfGrid.getWid()) ) {
				list.add(arcgisInfoOfGrid);
				result.put("list", list);
			}
		}
		if((list == null || list.size()<=0) && resultList.size()>0) {
			resultList.get(0).setHs("");
			list.add(resultList.get(0));
			result.put("list", list);
		}
//		List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(wid ,mapt);
//		result.put("list", list);
		return result;
	}
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfGridForPoint")
	public List<ArcgisInfoOfGrid> getArcgisDataOfGridForPoint(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="wid") Long wid
			,@RequestParam(value="mapt") Integer mapt) {
		Map<String,Object> result = new HashMap<String, Object>();
		List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(wid ,mapt);
		return list;
	}
	
	/**
	 * 2014-06-11 liushi add
	 * 用于楼宇地图编辑
	 * 页面显示楼宇数据，查询该楼栋所在网格下属的所有楼宇定位数据包括所在网格
	 * @param session
	 * @param request
	 * @param map
	 * @param buildingId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDrawDataOfBuilds")
	public List<ArcgisInfoOfBuild> getArcgisDrawDataOfBuilds(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="buildingId") Long buildingId
			,@RequestParam(value="mapt") Integer mapt) {
		AreaBuildingInfo areaBuildingInfo = areaBuildingInfoService.findAreaBuildingInfoById(buildingId);
		List<ArcgisInfoOfBuild> list = this.arcgisInfoService.getArcgisDrawDataOfBuildsByGridId(areaBuildingInfo.getGridId(), mapt);
		for(int i=0;i<list.size();i++) {
			if(buildingId.equals(list.get(i).getWid())) {
				list.remove(i);
				break;
			}
		}
		return list;
	}


	/**
	 * 根据楼宇ids获取楼宇轮廓信息
	 * @param session
	 * @param request
	 * @param map
	 * @param buildingIds
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDatasOfBuilds")
	public List<ArcgisInfoOfBuild> getArcgisDatasOfBuilds(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="buildingIds") String buildingIds
			,@RequestParam(value="mapt") Integer mapt) {
		List<ArcgisInfoOfBuild> list = arcgisInfoService.getArcgisDatasOfBuilds(buildingIds, mapt);

		return list;
	}

	/**
	 * 2014-06-11 liushi add
	 * 用于楼宇地图编辑
	 * 页面显示楼宇数据，查询该楼栋所在网格
	 * @param session
	 * @param request
	 * @param map
	 * @param buildingId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDrawDataOfGridOfBuilds")
	public List<ArcgisInfoOfBuild> getArcgisDrawDataOfGridOfBuilds(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="buildingId") Long buildingId
			,@RequestParam(value="mapt") Integer mapt) {
		AreaBuildingInfo areaBuildingInfo = areaBuildingInfoService.findAreaBuildingInfoById(buildingId);
		List<ArcgisInfoOfBuild> resultList = new ArrayList<ArcgisInfoOfBuild>();
		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoService.getArcgisDataOfGrid(areaBuildingInfo.getGridId() ,mapt);
		if(arcgisInfoOfGridList.size() == 1) {
			ArcgisInfoOfGrid arcgisInfoOfGrid = arcgisInfoOfGridList.get(0);
			ArcgisInfoOfBuild arcgisInfoOfBuild = new ArcgisInfoOfBuild();
			arcgisInfoOfBuild.setId(0L);
			arcgisInfoOfBuild.setWid(arcgisInfoOfGrid.getWid());
			arcgisInfoOfBuild.setName(arcgisInfoOfGrid.getName());
			arcgisInfoOfBuild.setX(arcgisInfoOfGrid.getX());
			arcgisInfoOfBuild.setY(arcgisInfoOfGrid.getY());
			arcgisInfoOfBuild.setHs(arcgisInfoOfGrid.getHs());
			arcgisInfoOfBuild.setLineColor(arcgisInfoOfGrid.getLineColor());
			arcgisInfoOfBuild.setLineWidth(arcgisInfoOfGrid.getLineWidth());
			arcgisInfoOfBuild.setNameColor(arcgisInfoOfGrid.getNameColor());
			arcgisInfoOfBuild.setAreaColor(arcgisInfoOfGrid.getAreaColor());
			resultList.add(arcgisInfoOfBuild);
		}
		List<ArcgisInfoOfBuild> list = this.arcgisInfoService.getArcgisDrawDataOfBuildsByGridId(areaBuildingInfo.getGridId(), mapt);
		for(ArcgisInfoOfBuild arcgisInfoOfBuild:list) {
			if(buildingId.equals(arcgisInfoOfBuild.getWid())) {
				arcgisInfoOfBuild.setEditAble(true);
				resultList.add(arcgisInfoOfBuild);
				break;
			}
		}
		return resultList;
	}
	
	@ResponseBody
	@RequestMapping(value="/getArcgisDrawDataOfGridOfBuild")
	public List<ArcgisInfoOfBuild> getArcgisDrawDataOfGridOfBuild(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="buildingId") Long buildingId
			,@RequestParam(value="mapt") Integer mapt) {
		AreaBuildingInfo areaBuildingInfo = areaBuildingInfoService.findAreaBuildingInfoById(buildingId);
		List<ArcgisInfoOfBuild> resultList = new ArrayList<ArcgisInfoOfBuild>();
		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoService.getArcgisDataOfGrid(areaBuildingInfo.getGridId() ,mapt);
		if(arcgisInfoOfGridList.size() == 1) {
			ArcgisInfoOfGrid arcgisInfoOfGrid = arcgisInfoOfGridList.get(0);
			ArcgisInfoOfBuild arcgisInfoOfBuild = new ArcgisInfoOfBuild();
			arcgisInfoOfBuild.setId(0L);
			arcgisInfoOfBuild.setWid(arcgisInfoOfGrid.getWid());
			arcgisInfoOfBuild.setName(arcgisInfoOfGrid.getName());
			arcgisInfoOfBuild.setX(arcgisInfoOfGrid.getX());
			arcgisInfoOfBuild.setY(arcgisInfoOfGrid.getY());
			arcgisInfoOfBuild.setHs(arcgisInfoOfGrid.getHs());
			arcgisInfoOfBuild.setLineColor(arcgisInfoOfGrid.getLineColor());
			arcgisInfoOfBuild.setLineWidth(arcgisInfoOfGrid.getLineWidth());
			arcgisInfoOfBuild.setNameColor(arcgisInfoOfGrid.getNameColor());
			arcgisInfoOfBuild.setAreaColor(arcgisInfoOfGrid.getAreaColor());
//			resultList.add(arcgisInfoOfBuild);
		}
		List<ArcgisInfoOfBuild> list = this.arcgisInfoService.getArcgisDrawDataOfBuildsByGridId(areaBuildingInfo.getGridId(), mapt);
		for(ArcgisInfoOfBuild arcgisInfoOfBuild:list) {
//			if(buildingId.equals(arcgisInfoOfBuild.getWid())) {
				arcgisInfoOfBuild.setEditAble(true);
				resultList.add(arcgisInfoOfBuild);
//				break;
//			}
		}
		return resultList;
	}
	
	/**
	 * 2014-06-11 liushi add
	 * 用于楼宇地图编辑
	 * 页面显示楼宇数据，查询该楼栋所在网格
	 * @param session
	 * @param request
	 * @param map
	 * @param buildingId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDrawDataOfGridOfLyjjbuildings")
	public List<ArcgisInfoOfBuildingBo> getArcgisDrawDataOfGridOfLyjjbuildings(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="buildingId") Long buildingId
			,@RequestParam(value="mapt") Integer mapt) {
		
		BuildingBo building = buildingService.getBuiding(buildingId);
		String gridCode = building.getBuildingGridCode();
		MixedGridInfo info = getGridInfoByOrgCode(gridCode);
		Long gridId = info.getGridId();
		
		List<ArcgisInfoOfBuildingBo> resultList = new ArrayList<ArcgisInfoOfBuildingBo>();
		List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,mapt);
		if(arcgisInfoOfGridList.size() == 1) {
			ArcgisInfoOfGrid arcgisInfoOfGrid = arcgisInfoOfGridList.get(0);
			ArcgisInfoOfBuildingBo arcgisInfoOfBuildingbo = new ArcgisInfoOfBuildingBo();
			arcgisInfoOfBuildingbo.setId(0L);
			arcgisInfoOfBuildingbo.setWid(arcgisInfoOfGrid.getWid());
			arcgisInfoOfBuildingbo.setName(arcgisInfoOfGrid.getName());
			arcgisInfoOfBuildingbo.setX(arcgisInfoOfGrid.getX());
			arcgisInfoOfBuildingbo.setY(arcgisInfoOfGrid.getY());
			arcgisInfoOfBuildingbo.setHs(arcgisInfoOfGrid.getHs());
			arcgisInfoOfBuildingbo.setLineColor(arcgisInfoOfGrid.getLineColor());
			arcgisInfoOfBuildingbo.setLineWidth(arcgisInfoOfGrid.getLineWidth());
			arcgisInfoOfBuildingbo.setNameColor(arcgisInfoOfGrid.getNameColor());
			arcgisInfoOfBuildingbo.setAreaColor(arcgisInfoOfGrid.getAreaColor());
			resultList.add(arcgisInfoOfBuildingbo);
		}
		
		List<ArcgisInfoOfBuildingBo> list = this.lyjjArcgisInfoService.getArcgisInfoOfBuildingBos(buildingId, mapt);
		for(ArcgisInfoOfBuildingBo lyjjBulidingBo:list) {
			if(buildingId.equals(lyjjBulidingBo.getWid())) {
				lyjjBulidingBo.setEditAble(true);
				resultList.add(lyjjBulidingBo);
				break;
			}
		}
		return list;
	}
	
	@RequestMapping(value = "/getBuildInfoDetailOnMap")
	public String getBuildInfoDetailOnMap(HttpSession session, @RequestParam(value = "buildingId") Long buildingId,
			@RequestParam(value = "isCross", required = false) String isCross, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		AreaBuildingInfo record = areaBuildingInfoService.findAreaBuildingInfoById(buildingId);
		this.addExtraInfo(record, userInfo.getOrgCode());
		MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(record.getGridId(), false);
		Long gridId = mixedGridInfo.getGridId();
		String infoOrgCode=mixedGridInfo.getInfoOrgCode();
		
		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
		}
		record.setManagementCompany(gridNames);
		map.addAttribute("isCross", isCross);
		map.addAttribute("record", record);
		map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		return "/map/arcgis/standardmappage/buildInfoDetailOnMap.ftl";
	}
	
	/**
	 * 楼宇经济地图楼宇详细信息
	 * @param session
	 * @param request
	 * @param buildingId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getlyjjBuildingInfoDetailOnMap")
	public String getlyjjBuildingInfoDetailOnMap(HttpSession session,HttpServletRequest request, @RequestParam(value="buildingId") Long buildingId, ModelMap map) {
		BuildingBo record = this.buildingService.getBuidingForMap(buildingId);
		MixedGridInfo mixedGridInfo = getGridInfoByOrgCode(record.getBuildingGridCode());
		Long gridId = mixedGridInfo.getGridId();
		String infoOrgCode=mixedGridInfo.getInfoOrgCode();
		
		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
		}
		
		request.setAttribute("record", record);
		request.setAttribute("gridNames", gridNames);
		request.setAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		return "/map/arcgis/standardmappage/lyjj/arcgis_lyjj_building_mapDetail.ftl";
	}
	
	/**
	 * 楼宇经济地图企业详细信息
	 * @param session
	 * @param request
	 * @param buildingId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getlyjjCorpBaseInfoDetailOnMap")
	public String getlyjjCorpBaseInfoDetailOnMap(HttpSession session,HttpServletRequest request, @RequestParam(value="corpId") Long corpId, ModelMap map) {
		CorpBaseBo corpBaseBo = new CorpBaseBo();
		corpBaseBo.setCorpId(corpId);
		CorpBaseBo bo = corpBaseService.getCorpBase(corpBaseBo);
		request.setAttribute("record", bo);
		return "/map/arcgis/standardmappage/lyjj/arcgis_lyjj_corpBase_mapDetail.ftl";
	}
	
	
	/**
	 * 楼宇经济地图事件详细信息
	 * @param session
	 * @param request
	 * @param buildingId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getlyjjEventInfoDetailOnMap")
	public String getlyjjEventInfoDetailOnMap(HttpSession session,HttpServletRequest request, Event event, ModelMap map) {
		
		event=eventService.getEvent(event);
		request.setAttribute("record", event);
		return "/map/arcgis/standardmappage/lyjj/arcgis_lyjj_event_mapDetail.ftl";
	}
	
	private void addExtraInfo(AreaBuildingInfo record, String orgCode) {
		//-- 晋江需求 modify by guohh 管理单位如果在楼栋中没填，那么该字段显示所属网格
		if(StringUtils.isBlank(record.getManagementCompany())) {
			record.setManagementCompany(record.getGridName());
		}
		//-- 如果管理员电话没填，那么该字段显示为所属网格的网格管理员和联系电话（有几个网格员显示几个）
		if(StringUtils.isBlank(record.getAdminPhone())) {
			List<GridAdmin> gridAdminList = gridAdminService.getGridAdminListByGridId(record.getGridId(), orgCode);
			if(gridAdminList!=null && gridAdminList.size()>0) {
				StringBuilder sb = new StringBuilder();
				for(int i=0; i<gridAdminList.size(); i++) {
					GridAdmin admin = gridAdminList.get(i);
					sb.append(admin.getPartyName()==null?"":admin.getPartyName());
					sb.append(" - ");
					sb.append(admin.getMobileTelephone()==null?"<span style='color:red;'>无电话</span>":admin.getMobileTelephone());
					if(i<(gridAdminList.size()-1)) {
						sb.append(";;");
					}
				}
				record.setAdminPhone(sb.toString());
			}
		}
	}
	/**
	 * 2014-06-11 liushi add
	 * 页面显示楼宇数据显示该网格下面所有（包括子网格的）楼宇数据
	 * @param session
	 * @param request
	 * @param map
	 * @param buildingId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfBuilds")
	public List<ArcgisInfoOfBuild> getArcgisDataOfBuilds(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="gridId") Long gridId
			,@RequestParam(value="gridCode") String gridCode
			,@RequestParam(value="mapt") Integer mapt) {
		List<ArcgisInfoOfBuild> list = this.arcgisInfoService.getArcgisDataOfBuilds(gridId, mapt);
		return list;
	}
	/**
	 * 2015-07-08 liushi add
	 * 根据网格id获取其下属的所有楼宇id
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataIdsOfBuilds")
	public Map<String,Object> getArcgisDataIdsOfBuilds(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="gridId") Long gridId) {
		Map<String,Object> result = new HashMap<String,Object>();
		String buildingIdsStr = this.arcgisInfoService.getArcgisDataIdsOfBuilds(gridId);
		result.put("buildingIdsStr", buildingIdsStr);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfBuildsPoints")
	public List<ArcgisInfoOfBuild> getArcgisDataOfBuildsPoints(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="gridId") Long gridId
			,@RequestParam(value="gridCode") String gridCode
			,@RequestParam(value="mapt") Integer mapt) {
		List<ArcgisInfoOfBuild> list = this.arcgisInfoService.getArcgisDataOfBuildsPoints(gridId, mapt);
		return list;
	}
	
	/**
	 * 2014-06-13 liushi add
	 * 查询当前楼宇数据
	 * @param session
	 * @param request
	 * @param map
	 * @param buildingId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfBuild")
	public Map<String,Object> getArcgisDataOfBuild(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="wid") Long wid
			,@RequestParam(value="mapt") Integer mapt) {
		Map<String,Object> result = new HashMap<String, Object>();
		List<ArcgisInfoOfBuild> list = this.arcgisInfoService.getArcgisDataOfBuild(wid, mapt);
		result.put("list", list);
		return result;
	}
	
	/**
	 * 查询当前楼宇经济楼宇数据
	 * @param session
	 * @param request
	 * @param map
	 * @param buildingId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfLyjjBuilding")
	public Map<String,Object> getArcgisDataOfLyjjBuilding(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="wid") Long wid
			,@RequestParam(value="mapt") Integer mapt) {
		Map<String,Object> result = new HashMap<String, Object>();
		List<ArcgisInfoOfBuildingBo> list = lyjjArcgisInfoService.getArcgisInfoOfBuildingBos(wid, mapt);
		result.put("list", list);
		return result;
	}
	
	/**
	 * 2014-06-26 liushi 获取市政设施等的定位数据
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfResMarker")
	public Map<String,Object> getArcgisDataOfResMarker(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="resourcesId") Long resourcesId
			,@RequestParam(value="markerType") String markerType
			,@RequestParam(value="catalog") String catalog
			,@RequestParam(value="mapt") Integer mapt) {
		Map<String,Object> result = new HashMap<String, Object>();
		List<ArcgisInfoOfPublic> list = this.arcgisInfoService.getArcgisDataOfMarkerType(resourcesId, markerType,catalog,mapt);
		if(list.size()==0) {
			list = this.arcgisInfoService.getArcgisDataOfGridByResId(resourcesId,markerType,mapt);
		}
		result.put("list", list);
		return result;
	}
	/**
	 * 2014-06-26 liushi 保存市政设施等的定位数据（分插入和修改两种）
	 * @param session
	 * @param request
	 * @param map
	 * @param resourcesId
	 * @param markerType
	 * @param catalog
	 * @param mapt
	 * @param x
	 * @param y
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/saveArcgisDataOfResMarker")
	public Map<String,Object> saveArcgisDataOfResMarker(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="resourcesId") Long resourcesId
			,@RequestParam(value="markerType") String markerType
			,@RequestParam(value="catalog") String catalog
			,@RequestParam(value="mapt") Integer mapt
			,@RequestParam(value="x") Double x
			,@RequestParam(value="y") Double y) {
		Map<String,Object> result = new HashMap<String, Object>();
		ArcgisInfoOfPublic arcgisInfoOfPublic = new ArcgisInfoOfPublic();
		arcgisInfoOfPublic.setWid(resourcesId);
		arcgisInfoOfPublic.setMapt(mapt);
		arcgisInfoOfPublic.setMarkerType(markerType);
		arcgisInfoOfPublic.setCatalog("02");
		arcgisInfoOfPublic.setX(x);
		arcgisInfoOfPublic.setY(y);
		
		boolean flag = true;
		flag = this.arcgisInfoService.saveArcgisDataOfMarkerType(arcgisInfoOfPublic);
		result.put("flag", flag);
		return result;
	}
	/**
	 * 2014-07-24 liushi add
	 * 为框选建立一个虚拟图层
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfKuangXuanLayer")
	public List<ArcgisInfoOfGrid> getArcgisDataOfKuangXuanLayer(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="geoString", required=false) String geoString) {
		List<ArcgisInfoOfGrid> list = new ArrayList<ArcgisInfoOfGrid>();
		if(geoString != null && !"".equals(geoString)) {
			ArcgisInfoOfGrid obj = new ArcgisInfoOfGrid();
			obj.setHs(geoString);
			obj.setLineColor("#000000");
			obj.setLineWidth(2);
			obj.setGridName("框选");
			obj.setName("框选");
			obj.setNameColor("#000000");
			list.add(obj);
		}
		return list;
	}
	
	@RequestMapping(value="/getKuangXuanLyjjData")
	public String boxLyjjChoose(HttpSession session, ModelMap map,HttpServletRequest request
			,@RequestParam(value="geoString") String geoString ,@RequestParam(value="mapt") String mapt
			,@RequestParam(value="kuangxuanType", required=false) String chooseType) throws Exception{
		Map<String, Object> results = lyjjArcgisInfoService.getLyjjChooseData(geoString,mapt,chooseType);
		request.setAttribute("results", results);
		return "/map/arcgis/standardmappage/lyjj/arcgis_lyjj_kuangxuan.ftl";
	}
	
	@RequestMapping(value="/getKuangXuanData")
	public String boxChoose(HttpSession session, ModelMap map,HttpServletRequest request
			,@RequestParam(value="geoString") String geoString
			,@RequestParam(value="mapt") String mapt
			,@RequestParam(value="kuangxuanType", required=false) String chooseType)throws Exception
	{
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long startGridId=Long.parseLong(defaultGridInfo.get(KEY_START_GRID_ID).toString());
		String infoOrgCode = (String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		//if(chooseType.equals("jcsj")){
			
			Map<String, Object> params = new HashMap<String, Object>();
			//params.put("startGridId", gridId);
			params.put("geoString", geoString);
			params.put("resName", null);
			
			params.put("gridname", null);
			
			params.put("startGridId", startGridId);
			params.put("infoOrgCode", infoOrgCode);
			params.put("mapType", mapt);
			int qqyCount = gisInfoService.getMarkedListboxChose(geoString, ResMarkedOM.MEGAEYES, ResMarkedOM.RESOURCES, mapt);//全球眼
			request.setAttribute("qqyCount", qqyCount);
			params.put("resTypeId", 22L);
			params.put("markerType", "02010601");
			int wgrysCount = gisInfoService.getWGRYListboxChose(geoString,startGridId);//网格员
			request.setAttribute("wgrysCount", wgrysCount); 
			int xfsCount = gisInfoService.getResObjectBoxChoseCount(params);//消防栓
			params.put("resTypeId", 25L);
			params.put("markerType", "020304");
			int paichusuoCount = gisInfoService.getResObjectBoxChoseCount(params);//派出所
			request.setAttribute("xfsCount", xfsCount); 
			request.setAttribute("geoString", geoString);
			request.setAttribute("paichusuoCount", paichusuoCount); 
			request.setAttribute("mapType", mapt);
			return "/map/arcgis/standardmappage/kuangxuan/kuangxuan_info.ftl";
	/*
		}else{
			
		
		
		int qqyCount = gisInfoService.getMarkedListboxChose(geoString, ResMarkedOM.MEGAEYES, ResMarkedOM.RESOURCES, mapt);//全球眼
		request.setAttribute("qqyCount", qqyCount);
		int czrysCount = gisInfoService.getZKSListboxChose(geoString, "T_DC_CI_RS_DEFORMITY", mapt, null);//残障人员
		request.setAttribute("czrysCount", czrysCount);
		int sysCount = gisInfoService.getZKSListboxChose(geoString, "T_DC_CI_RS_UNEMPLOYED", mapt, "shiye");//失业数
		request.setAttribute("sysCount", sysCount);
		int txsCount = gisInfoService.getZKSListboxChoseTxs(geoString, mapt);//退休
		request.setAttribute("txsCount", txsCount);
		int jjylsCount = gisInfoService.getZKSListboxChoseJjyls(geoString, mapt);//居家养老数
		request.setAttribute("jjylsCount", jjylsCount);
		int dbrysCount = gisInfoService.getZKSListboxChose(geoString, "T_DC_CI_RS_POOR", mapt, null);//低保人员数
		request.setAttribute("dbrysCount", dbrysCount);
		
		int xdsCount = gisInfoService.getZKSListboxChose(geoString, "T_ZZ_DRUG_RECORD", mapt, null);//吸毒数
		request.setAttribute("xdsCount", xdsCount);
		int jzsCount = gisInfoService.getZKSListboxChose(geoString, "T_ZZ_CORRECTIONAL_RECORD", mapt, null);//矫正数
		request.setAttribute("jzsCount", jzsCount);
		int xjsCount = gisInfoService.getZKSListboxChose(geoString, "T_ZZ_CULT_RECORD", mapt, null);// 邪教数
		request.setAttribute("xjsCount", xjsCount);
		int xssCount = gisInfoService.getZKSListboxChose(geoString, "T_ZZ_RELEASED_RECORD", mapt, null);// 刑释解教数
		request.setAttribute("xssCount", xssCount);
		int sfsCount = gisInfoService.getZKSListboxChose(geoString, "T_ZZ_PETITION_RECORD", mapt, null);//上访数
		request.setAttribute("sfsCount", sfsCount);
		int wxcysCount = gisInfoService.getZKSListboxChose(geoString, "T_ZZ_PETITION_RECORD", mapt, null);//危险品从业数
		request.setAttribute("wxcysCount", wxcysCount);
		int zjsbsCount = gisInfoService.getZKSListboxChose(geoString, "T_ZZ_MENTAL_ILLNESS_RECORD", mapt, null);//重精神病数
		request.setAttribute("zjsbsCount", zjsbsCount);
		
		int zksCount = xdsCount+jzsCount+xjsCount+xssCount+sfsCount+wxcysCount+zjsbsCount;//重点人口
		request.setAttribute("zksCount", zksCount);
		int czsCount = gisInfoService.getCZSorLDSListboxChose(geoString,mapt,"常住人口");//常住人口
		request.setAttribute("czsCount", czsCount);
		int ldsCount = gisInfoService.getCZSorLDSListboxChose(geoString,mapt,"流动人口");//流动人口
		request.setAttribute("ldsCount", ldsCount);
		int wgrysCount = gisInfoService.getWGRYListboxChose(geoString,startGridId);//网格员
		request.setAttribute("wgrysCount", wgrysCount); 
		int dysCount = gisInfoService.getZKSListboxChose(geoString, "T_DC_CI_RS_PARTY", mapt, null);//党员数
		request.setAttribute("dysCount", dysCount);
		request.setAttribute("geoString", geoString); 
		
/*
		int zhsCount = service.getZHSListboxChose(gisinfo);// 总户数
		int zksCount = service.getZKSListboxChose(gisinfo, "02%");// 重口数
		int dysCount = service.getZKSListboxChose(gisinfo, "0101");// 党员数
		int zyzsCount = service.getZKSListboxChose(gisinfo, "0304");// 志愿者数
		int dzzsCount = service.getMarkedListboxChose(gisinfo,
				ResMarkedOM.ORGANIZATION, ResMarkedOM.PARTY_ORGANIZATION,
				ResMarkedOM.THREE_DIMENSION);// 党组织数

		List<GisForm> list = service.regionQqyList(geoString);
		String result = "";
		for (GisForm gisForm : list) {
			if (result == null || "".equals(result)) {
				result = gisForm.getId();
			} else {
				result = result + "," + gisForm.getId();
			}
		}
		request.setAttribute("result", result);
		request.setAttribute("qqyCount", qqyCount);
		request.setAttribute("zhsCount", zhsCount);
		request.setAttribute("czsCount", czsCount);
		request.setAttribute("ldsCount", ldsCount);
		request.setAttribute("zksCount", zksCount);
		request.setAttribute("xdsCount", xdsCount);
		request.setAttribute("jzsCount", jzsCount);
		request.setAttribute("xssCount", xssCount);
		request.setAttribute("xjsCount", xjsCount);
		request.setAttribute("sfsCount", sfsCount);
		request.setAttribute("wxcysCount", wxcysCount);
		request.setAttribute("zjsbsCount", zjsbsCount);
		request.setAttribute("dysCount", dysCount);
		request.setAttribute("czrysCount", czrysCount);
		request.setAttribute("dbrysCount", dbrysCount);
		request.setAttribute("jjylsCount", jjylsCount);
		request.setAttribute("zyzsCount", zyzsCount);
		request.setAttribute("txsCount", txsCount);
		request.setAttribute("sysCount", sysCount);
		request.setAttribute("dzzsCount", dzzsCount);
		request.setAttribute("wgrysCount", wgrysCount);
		request.setAttribute("geoString", geoString);
		return mapping.findForward("boxChooseInfo");
		
		return "/zzgl_map/gis/pub_component/boxchooseinfo.ftl";
		}*/
	}
	/**
	 * 2014-11-13 liushi add 跳转到框选统计项配置页面
	 * @param session
	 * @param map
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toKuangXuanConfig")
	public String toKuangXuanConfig(HttpSession session, ModelMap map,HttpServletRequest request
			,@RequestParam(value="homePageType", required=false) String homePageType
			,@RequestParam(value="socialOrgCode", required=false) String socialOrgCode
			,@RequestParam(value="gridId", required=false) Long gridId)throws Exception{
		map.addAttribute("homePageType", homePageType);
		map.addAttribute("socialOrgCode", socialOrgCode);
		map.addAttribute("gridId", gridId);
		return "/map/arcgis/standardmappage/kuangxuan/kuangxuan_config.ftl";
	}
	/**
	 * 2014-11-12 liushi add 可配置的框选统计
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
			,@RequestParam(value="orgCode") String orgCode
			,@RequestParam(value="gridId") Long gridId)throws Exception
	{
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long startGridId=Long.parseLong(defaultGridInfo.get(KEY_START_GRID_ID).toString());
		String infoOrgCode = (String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		String[] kuangxuanTypes = kuangxuanTypeStr.split(",");
		menuNameStr = java.net.URLDecoder.decode(menuNameStr, "utf-8");
		String[] menuNames = menuNameStr.split(",");
		
		Map<String, List<String>> result = new LinkedHashMap<String, List<String>>();;
		List<String> values = null;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resName", null);
		params.put("gridname", null);
		params.put("gridId", gridId);
		params.put("infoOrgCode", orgCode);
		for(int i=0;i<kuangxuanTypes.length;i++){
			params.put("geoString", geoString);
			params.put("mapType", mapt);
			params.put("kuangxuanType", kuangxuanTypes[i]);
			int resultCount = this.kuangXuanStat.statOfKuangXuan(params);
			map.addAttribute(kuangxuanTypes[i]+"Count", resultCount);
			values = new ArrayList<String>();
			values.add(menuNames[i]);
			values.add(resultCount+"");
			result.put(kuangxuanTypes[i], values);
		}
		map.addAttribute("geoString", geoString);
		map.addAttribute("mapType", mapt);
		map.addAttribute("kuangxuanTypes", kuangxuanTypes);
		map.addAttribute("result", result);
		return "/map/arcgis/standardmappage/kuangxuan/kuangxuan_config_info.ftl";
	}
	/**
	 * 2014-07-30 liushi add
	 * 轨迹数据查询
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisDataTrajectoryOfGridAdmin")
	public List<ArcgisInfoOfTrajectory> getArcgisDataTrajectoryOfGridAdmin(HttpSession session,
			HttpServletRequest request, ModelMap map, @RequestParam(value = "imsi") String imsi,
			@RequestParam(value = "locateTimeBegin", required = false) String locateTimeBegin,
			@RequestParam(value = "locateTimeEnd", required = false) String locateTimeEnd,
			@RequestParam(value = "mobileTelephone") String mobileTelephone, 
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "userId") Long userId,
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "jsJiangYinFlag", required = false) String jsJiangYinFlag) {
		String infoOrgCode = super.getDefaultInfoOrgCode(session);
		List<ArcgisInfoOfTrajectory> list = new ArrayList<ArcgisInfoOfTrajectory>();
		Map<String, Object> params = new HashMap<String, Object>();
		
		String trackDate = request.getParameter("trackDate");
		params.put("trackDate", trackDate);
		if (StringUtils.isNotBlank(locateTimeBegin)) {
			params.put("locateTimeBegin", locateTimeBegin);
		}
		if(StringUtils.isNotBlank(locateTimeEnd)){
			params.put("locateTimeEnd",locateTimeEnd);
		}
		if(jsJiangYinFlag != null && jsJiangYinFlag.equals("true") && StringUtils.isNotBlank(userName)){
			params.put("userName",userName);
			if(userId != null) {
				params.put("userId", userId);
				params.put("jsJiangYinFlag", "true");
			}
			//list = this.arcgisInfoService.getGridAdminTrajectoryListByUserName(userName,locateTimeBegin,locateTimeEnd,mobileTelephone,userId,mapt);
		}else{
			if(userId != null){
				params.put("userId",userId);
			}
			//list = this.arcgisInfoService.getGridAdminTrajectoryList(imsi,locateTimeBegin,locateTimeEnd,mobileTelephone,userId,mapt);
		}
		list = arcgisInfoService.getDayTrajectoryListByParams(params);

		if (StringUtils.isNotBlank(infoOrgCode) && infoOrgCode.startsWith(ConstantValue.HAICANG_FUNC_ORG_CODE)) {
			WGSUtils.Wgs84ToXm92List(list);
		}
		return list;
	}

	/**
	 * 2017-5-17
	 * 队员轨迹数据查询
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataTrajectoryOfTeamMember")
	public List<ArcgisInfoOfTrajectory> getArcgisDataTrajectoryOfTeamMember(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="memberId")String memberId
			,@RequestParam(value="imsi")String imsi
			,@RequestParam(value="locateTimeBegin")String locateTimeBegin
			,@RequestParam(value="locateTimeEnd", required=false)String locateTimeEnd
			,@RequestParam(value="mapt")Integer mapt
			,@RequestParam(value="userId")Long userId) {

		List<ArcgisInfoOfTrajectory> list = this.arcgisInfoService.getTeamMemberTrajectoryList(memberId,imsi,locateTimeBegin,locateTimeEnd,userId,mapt);
		return list;
	}

	/**
	 * 2015-03-06 huangmw add
	 * 护路护线队员轨迹数据查询
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataTrajectoryOfCareRoadMember")
	public List<ArcgisInfoOfTrajectory> getArcgisDataTrajectoryOfCareRoadMember(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="memberId")String memberId
			,@RequestParam(value="imsi")String imsi
			,@RequestParam(value="locateTimeBegin")String locateTimeBegin
			,@RequestParam(value="locateTimeEnd", required=false)String locateTimeEnd
			,@RequestParam(value="mobileTelephone")String mobileTelephone
			,@RequestParam(value="mapt")Integer mapt
			,@RequestParam(value="bizType",required=false)String bizType
			,@RequestParam(value="userId")Long userId) {
		
		List<ArcgisInfoOfTrajectory> list = this.arcgisInfoService.getCareRoadMemberTrajectoryList(memberId,imsi,locateTimeBegin,locateTimeEnd,mobileTelephone,userId,mapt,bizType);
		return list;
	}
	/**
	 * 2014-08-19 liushi add
	 * 周边资源
	 * @param session
	 * @param map
	 * @param mapt
	 * @param sX
	 * @param sY
	 * @param eventId
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/showArcgisSurroundingResList")
	public String showArcgisSurroundingResList(HttpSession session, 
			 ModelMap map,
			@RequestParam(value="mapt", required=false) String mapt, 
			@RequestParam(value="sX", required=false) String sX, 
			@RequestParam(value="sY", required=false) String sY, 
			@RequestParam(value="eventId", required=false) String eventId, 
			HttpServletRequest req) throws Exception{
		Double x = 0.0d;
		Double y = 0.0d;
		x = sX == null ? 0.0d : Double.parseDouble(sX);
		y = sY == null ? 0.0d : Double.parseDouble(sX);
//		ResMarker resMarker = new ResMarker();
		ArcgisInfoOfPublic arcgisInfoOfPublic = new ArcgisInfoOfPublic();
		List<Long> idList = new ArrayList<Long>();
		idList.add(Long.parseLong(eventId));
		if (x == 0.0d && y == 0.0d) {
			List<ArcgisInfoOfPublic> mapDataList = this.arcgisDataOfLocalService.getArcgisEventLocateDataListByIds(eventId, Integer.parseInt(mapt));
			if(mapDataList != null && mapDataList.size() > 0) {
				arcgisInfoOfPublic = mapDataList.get(0);
			}
			x = arcgisInfoOfPublic.getX() == null ? 0.0d : arcgisInfoOfPublic.getX();
			y = arcgisInfoOfPublic.getY() == null ? 0.0d : arcgisInfoOfPublic.getY();
		}
		map.addAttribute("x", x);
		map.addAttribute("y", y);
		return "/map/arcgis/standardmappage/surroundingRes/surroundingRes.ftl";
	}
	@RequestMapping(value="/nearbyResources")
	public String nearbyResources(HttpSession session, 
			 ModelMap map,
			@RequestParam(value="distance", required=false) String distance, 
			@RequestParam(value="x", required=false) String x, 
			@RequestParam(value="y", required=false) String y, 
			@RequestParam(value="mapt") String mapt, 
			HttpServletRequest req) throws Exception{
		PreResources preResources = new PreResources();
		preResources.setX(x);
		preResources.setY(y);
		preResources.setDistance(distance);
		preResources.setMapt(mapt);
		//统计三种类弄型距离最小的数据
		if("99999".equals(distance)){	//查找最近的距离
			//楼宇信息
			PreResources objBuilding = preResourcesService.findObjBuilding(preResources);
			//统计组织信息
			PreResources partyInfo = preResourcesService.findPartyInfo(preResources);
			//统计全球眼信息
			PreResources ciCommunityMonitor = preResourcesService.findCiCommunityMonitor(preResources);
			//网格员
			PreResources gridAdmin = preResourcesService.findGridAdmin(preResources);
			//统计消防栓
			preResources.setMarkerType("02010601");
			PreResources xiaofangshuan = preResourcesService.findPublicObject(preResources);
			//统计派出所
			preResources.setMarkerType("020304");
			PreResources policeStation = preResourcesService.findPublicObject(preResources);
			
			DecimalFormat fnum = new DecimalFormat("##0.0");  
			
			//换算单位
			if(null != objBuilding){
				if(Integer.valueOf(objBuilding.getDistance()) > 1000){
					objBuilding.setDistance(fnum.format(Float.valueOf(objBuilding.getDistance()) / 1000f)  + "公里");
				}else{
					objBuilding.setDistance(objBuilding.getDistance() + "米");
				}
			} 
			//换算单位
			if(null != partyInfo){
				if(Integer.valueOf(partyInfo.getDistance()) > 1000){
					partyInfo.setDistance(fnum.format(Float.valueOf(partyInfo.getDistance()) / 1000f) + "公里");
				}else{
					partyInfo.setDistance(partyInfo.getDistance() + "米");
				}
			}
//			换算单位
			if(null != ciCommunityMonitor){
				if(Integer.valueOf(ciCommunityMonitor.getDistance()) > 1000){
					ciCommunityMonitor.setDistance(fnum.format(Float.valueOf(ciCommunityMonitor.getDistance()) / 1000f) + "公里");
				}else{
					ciCommunityMonitor.setDistance(ciCommunityMonitor.getDistance() + "米");
				}
			}
			//			换算单位
			if(null != xiaofangshuan){
				if(Integer.valueOf(xiaofangshuan.getDistance()) > 1000){
					xiaofangshuan.setDistance(fnum.format(Float.valueOf(xiaofangshuan.getDistance()) / 1000f) + "公里");
				}else{
					xiaofangshuan.setDistance(xiaofangshuan.getDistance() + "米");
				}
			}
			if(null != gridAdmin){
				if(Integer.valueOf(gridAdmin.getDistance()) > 1000){
					gridAdmin.setDistance(fnum.format(Float.valueOf(gridAdmin.getDistance()) / 1000f) + "公里");
				}else{
					gridAdmin.setDistance(gridAdmin.getDistance() + "米");
				}
			}
			if(null != policeStation){
				if(Integer.valueOf(policeStation.getDistance()) > 1000){
					policeStation.setDistance(fnum.format(Float.valueOf(policeStation.getDistance()) / 1000f) + "公里");
				}else{
					policeStation.setDistance(policeStation.getDistance() + "米");
				}
			}
			map.addAttribute("objBuilding", objBuilding);
			map.addAttribute("partyInfo", partyInfo);
			map.addAttribute("ciCommunityMonitor", ciCommunityMonitor);
			map.addAttribute("xiaofangshuan", xiaofangshuan);
			map.addAttribute("gridAdmin", gridAdmin);
			map.addAttribute("policeStation", policeStation);
		}else{
			//统计楼宇信息
			int buildingCount = preResourcesService.statisticsBuildingCount(preResources);
//			//统计组织信息
			int partyInfoCount = preResourcesService.statisticsPartyInfoCount(preResources);
//			//统计全球眼信息
			int ciCommunityMonitorCount = preResourcesService.statisticsCiCommunityMonitorCount(preResources);
			int gridAdminCount = preResourcesService.statisticsGridAdminCount(preResources);
			preResources.setMarkerType("02010601");
			int xiaofangshuanCount = preResourcesService.statisticsPublicObjectCount(preResources);
			preResources.setMarkerType("020304");
			int policeStationCount = preResourcesService.statisticsPublicObjectCount(preResources);
			map.addAttribute("buildingCount", buildingCount);
			map.addAttribute("partyInfoCount", partyInfoCount);
			map.addAttribute("ciCommunityMonitorCount", ciCommunityMonitorCount);
			map.addAttribute("xiaofangshuanCount", xiaofangshuanCount);
			map.addAttribute("gridAdminCount", gridAdminCount);
			map.addAttribute("policeStationCount", policeStationCount);
//			return mapping.findForward("resourcesList");
			return "/map/arcgis/standardmappage/surroundingRes/list_presoruces_form.ftl";
		}
		return "/map/arcgis/standardmappage/surroundingRes/data_presoruces_form.ftl";
	}
	@RequestMapping(value="/getDisplayMemu")
	public @ResponseBody Map<String, Object> getDisplayMemu(HttpSession session, ModelMap map, HttpServletResponse res, HttpServletRequest request) throws Exception{
		Map result = new HashMap<String,Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		UserCookie userCookie = (UserCookie) session.getAttribute(ConstantValue.USER_COOKIE_IN_SESSION);
		OrgSocialInfoBO nowOrg =orgSocialInfoService.findByOrgId(userInfo.getOrgId());//当前组织
		List<Menu> menuList=(List<Menu>)menuService.queryDisplayMemuByCondition(userInfo.getUserId(), nowOrg.getOrgCode(), nowOrg.getOrgId(), null);
		int menuCount = menuList.size();
		result.put("menuCount", menuCount);
		result.put("menuList", menuList);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfResources")
	public Map<String,Object> getArcgisDataOfResources(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="wid") Long wid
			,@RequestParam(value="mapt") Integer mapt
			,@RequestParam(value="targetType") String targetType) {
		Map<String,Object> result = new HashMap<String, Object>();
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisDataOfResourcesSelfById(wid, mapt, targetType);
		result.put("list", list);
		return result;
	}
	
	/**
	 * 2014-05-28 liushi add
	 * 转到arcgis楼宇轮廓编辑的panel
	 * @param session
	 * @param request
	 * @param map
	 * @param wid  目标关联id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toArcgisDrawBuildingPanel")
	public String toArcgisDrawBuildingPanel(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="buildingId", required = false) Long buildingId,
			@RequestParam(value="gridId", required = false) Long gridId) throws Exception{
		String forward = "/zzgl/drowBuilding/draw_building_panel.ftl";
		
		Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
		String BUILD_FACTOR_STATION = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "BUILD_FACTOR_STATION",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
		String BUILD_FACTOR_SERVICE = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "BUILD_FACTOR_SERVICE",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
		String BUILD_FACTOR_URL = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "BUILD_FACTOR_URL",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
		String USE_DRAW_BUILDING_OUTLINE = this.funConfigurationService.turnCodeToValue("USE_DRAW_BUILDING_OUTLINE", "",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("FACTOR_STATION", BUILD_FACTOR_STATION);
		map.addAttribute("FACTOR_SERVICE", BUILD_FACTOR_SERVICE);
		map.addAttribute("FACTOR_URL", BUILD_FACTOR_URL);
		map.addAttribute("USE_DRAW_BUILDING_OUTLINE", USE_DRAW_BUILDING_OUTLINE);
		
		String name = "";
		Long parentGridId = null;
		Integer gridLevel = 0;
		AreaBuildingInfo building = new AreaBuildingInfo();
		if(buildingId != null){
			building = areaBuildingInfoService.findAreaBuildingInfoById(buildingId);
		}
		if(building != null){
			if(building.getGridId() != null){
				gridId = building.getGridId();
			}
			if(StringUtils.isNotBlank(building.getBuildingName())){
				name = building.getBuildingName();
			}
		}
		parentGridId = gridId;
		map.addAttribute("projectPath", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath());
		map.addAttribute("parentGridId", parentGridId+"");
		map.addAttribute("gridId", gridId+"");
		if(buildingId != null){
			map.addAttribute("wid", buildingId.toString());
		}else{
			map.addAttribute("wid", "");
		}
		map.addAttribute("gridLevel", gridLevel.toString());
		map.addAttribute("name", name);
		return forward;
	}
	
	@RequestMapping(value="/toArcgisDrawBuildingPanelBinding")
	public String toArcgisDrawBuildingPanelBinding(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="buildingId", required = false) Long buildingId,
			@RequestParam(value="gridId", required = false) Long gridId) throws Exception{
		String forward = "/zzgl/drowBuilding/arcgis_draw_building_binding.ftl";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String name = "";
		Long parentGridId = null;
		Integer gridLevel = 0;
		AreaBuildingInfo building = new AreaBuildingInfo();
		if(buildingId != null){
			building = areaBuildingInfoService.findAreaBuildingInfoById(buildingId);
		}
		if(building != null){
			if(building.getGridId() != null){
				gridId = building.getGridId();
			}
			if(StringUtils.isNotBlank(building.getBuildingName())){
				name = building.getBuildingName();
			}
		}
		parentGridId = gridId;
		map.addAttribute("projectPath", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath());
		map.addAttribute("parentGridId", parentGridId+"");
		map.addAttribute("gridId", gridId+"");
		if(buildingId != null){
			map.addAttribute("wid", buildingId.toString());
		}else{
			map.addAttribute("wid", "");
		}
		map.addAttribute("gridLevel", gridLevel.toString());
		map.addAttribute("name", name);
		
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
		
		return forward;
	}
	
	@ResponseBody
	@RequestMapping(value="/getArcgisDataBuildCenterAndLevel")
	public Map<String, Object> getArcgisDataBuildCenterAndLevel(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="wid") Long wid
			,@RequestParam(value="mapt") Integer mapt
			,@RequestParam(value="targetType") String targetType) {
		
		ArcgisInfoOfGrid result = null;
		AreaBuildingInfo areaBuildingInfo = areaBuildingInfoService.findAreaBuildingInfoById(wid);
		List<ArcgisInfoOfBuild> list = this.arcgisInfoService.getArcgisDrawDataOfBuildsByGridId(areaBuildingInfo.getGridId(), mapt);
		for(ArcgisInfoOfBuild arcgisInfoOfBuild:list) {
			if(wid.equals(arcgisInfoOfBuild.getWid())) {
				result = new ArcgisInfoOfGrid();
				result.setX(arcgisInfoOfBuild.getX());
				result.setY(arcgisInfoOfBuild.getY());
				result.setMapCenterLevel(14);
			}
		}
		Map<String, Object> res = new HashMap<String,Object>();
		res.put("result", result);
		return res;
	}
	
	/**
	 * 2015-11-23 sulch add
	 * 转到arcgis网格轮廓编辑的panel（主要针对网格grid）
	 * 需要获取目标的名称
	 * @param session
	 * @param request
	 * @param map
	 * @param targetType目标类型  grid   build
	 * @param wid  目标关联id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toArcgisDrawGridPanel")
	public String toArcgisDrawGridPanel(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="gridId", required = false) Long gridId,
			@RequestParam(value="bindFlag", required = false) String bindFlag
			) throws Exception{
		String forward = "";
		if(bindFlag != null && "true".equals(bindFlag)){
			forward = "/zzgl/bindGrid/bind_grid_panel.ftl";
		}else{
			forward = "/zzgl/drowGrid/draw_grid_panel.ftl";
		}

		Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
		String GRID_FACTOR_STATION = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "GRID_FACTOR_STATION",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
		String GRID_FACTOR_SERVICE = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "GRID_FACTOR_SERVICE",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
		String GRID_FACTOR_URL = this.funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "GRID_FACTOR_URL",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("FACTOR_STATION", GRID_FACTOR_STATION);
		map.addAttribute("FACTOR_SERVICE", GRID_FACTOR_SERVICE);
		map.addAttribute("FACTOR_URL", GRID_FACTOR_URL);
		
		String name = "";
		Long parentGridId = null;
		Integer gridLevel = 0;
		MixedGridInfo grid = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		name = grid.getGridName();
		parentGridId = grid.getParentGridId();
		gridLevel = grid.getGridLevel();
		map.addAttribute("projectPath", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath());
		map.addAttribute("parentGridId", parentGridId+"");
		map.addAttribute("gridId", gridId+"");
		if(gridId != null){
			map.addAttribute("wid", gridId.toString());
		}else{
			map.addAttribute("wid", "");
		}
		map.addAttribute("gridLevel", gridLevel.toString());
		map.addAttribute("name", name);
		return forward;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getArcgisInfoList")
	public List<ArcgisInfo> getArcgisInfoList(HttpSession session,
			@RequestParam(value = "idStr") String idStr,
			@RequestParam(value = "mapt") Integer mapt, 
			@RequestParam(value = "markerType") String markerType) {
		List<ArcgisInfo> list = null;
		if (StringUtils.isNotBlank(idStr)) {
			List<Long> ids = new ArrayList<Long>();
			String[] idss = idStr.split(",");
			for (String id : idss) {
				ids.add(Long.valueOf(id.trim()));
			}
			list = this.arcgisDataOfLocalService.getArcgisInfoList(ids, mapt, markerType);
		}
		return list;
	}

	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfChildrenGridsByParentId")
	public List<ArcgisInfoOfGrid> getArcgisDataOfChildrenGridsByParentId(HttpSession session, HttpServletRequest request, ModelMap map,
																		 @RequestParam(value = "parentGridId") Long parentGridId, @RequestParam(value = "mapt") Integer mapt,
																		 @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfChildrenGridsByParentId(parentGridId, mapt);
		for(ArcgisInfoOfGrid arcgisInfoOfGrid:list){
			arcgisInfoOfGrid.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	
	/**
	 * 获取网格蒙版配置
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getGridMosaicCfg")
	public String getGridMosaicCfg(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "orgCode") String orgCode) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		// 获取网格蒙版配置
		String ARCGIS_GRID_MOSAIC_CFG = this.funConfigurationService.turnCodeToValue("ARCGIS_GRID_MOSAIC_CFG", "",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(),
				IFunConfigurationService.CFG_ORG_TYPE_0);
		return ARCGIS_GRID_MOSAIC_CFG;
	}
	
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfPoint")
	public Map<String,Object> getArcgisDataOfPoint(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="wid") Long wid
			,@RequestParam(value="mapt") Integer mapt) {
		Map<String,Object> result = new HashMap<String, Object>();
		List<ArcgisInfoOfPoint> list = this.arcgisInfoService.getArcgisDataOfPoint(wid, mapt);
		result.put("list", list);
		return result;
	}
	
	/**
	 * 2014-06-11 liushi add
	 * 用于楼宇地图编辑
	 * 页面显示楼宇数据，查询该楼栋所在网格
	 * @param session
	 * @param request
	 * @param map
	 * @param buildingId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDrawDataOfGridOfPoints")
	public List<ArcgisInfoOfPoint> getArcgisDrawDataOfGridOfPoints(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="pointId") Long pointId
			,@RequestParam(value="mapt") Integer mapt) {
		PointInfo p=pointInfoService.searchById(pointId);
		List<ArcgisInfoOfPoint> resultList = new ArrayList<ArcgisInfoOfPoint>();
		if(p!=null){
			MixedGridInfo mix=mixedGridInfoService.getDefaultGridByOrgCode(p.getInfoOrgCode());
			List<ArcgisInfoOfGrid> arcgisInfoOfGridList = this.arcgisInfoService.getArcgisDataOfGrid(mix.getGridId() ,mapt);
			if(arcgisInfoOfGridList.size() == 1) {
				ArcgisInfoOfGrid arcgisInfoOfGrid = arcgisInfoOfGridList.get(0);
				ArcgisInfoOfPoint arcgisInfoOfPoint = new ArcgisInfoOfPoint();
				arcgisInfoOfPoint.setId(0L);
				arcgisInfoOfPoint.setWid(arcgisInfoOfGrid.getWid());
				arcgisInfoOfPoint.setName(arcgisInfoOfGrid.getName());
				arcgisInfoOfPoint.setX(arcgisInfoOfGrid.getX());
				arcgisInfoOfPoint.setY(arcgisInfoOfGrid.getY());
				arcgisInfoOfPoint.setHs(arcgisInfoOfGrid.getHs());
				arcgisInfoOfPoint.setLineColor(arcgisInfoOfGrid.getLineColor());
				arcgisInfoOfPoint.setLineWidth(arcgisInfoOfGrid.getLineWidth());
				arcgisInfoOfPoint.setNameColor(arcgisInfoOfGrid.getNameColor());
				arcgisInfoOfPoint.setAreaColor(arcgisInfoOfGrid.getAreaColor());
				resultList.add(arcgisInfoOfPoint);
			}
			List<ArcgisInfoOfPoint> list = this.arcgisInfoService.getArcgisDrawDataOfPointsByCode(p.getInfoOrgCode(), mapt);
			for(ArcgisInfoOfPoint arcgisInfoOfPoint:list) {
				if(pointId.equals(arcgisInfoOfPoint.getWid())) {
					arcgisInfoOfPoint.setEditAble(true);
					resultList.add(arcgisInfoOfPoint);
				}
			}
		}
		return resultList;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/saveRandomBuild")
	public Map<String,Object> saveRandomBuild(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="infoOrgCode") String infoOrgCode) {
		Map<String,Object> result = new HashMap<String, Object>();
		List<Long> grids=new ArrayList<>();
		if(StringUtils.isNotEmpty(infoOrgCode)){
			List<MixedGridInfo> mixList=mixedGridInfoService.findGridInfoByInfoOrgCode(infoOrgCode);
			
			if(mixList!=null && mixList.size()>0){
				for(MixedGridInfo mix:mixList){
					grids.add(mix.getGridId());
				}
			}
		}
		boolean r=arcgisInfoService.saveRandomBuild(grids);
		result.put("result", r);
		return result;
	}
	
	/**
	 * 2014-06-11 liushi add
	 * 获取map_wg_gis表的轮廓信息
	 * @param session
	 * @param request
	 * @param map
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisCommonHs")
	public List<ArcgisInfo> getArcgisCommonHs(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="wid") Long wid
			,@RequestParam(value="type") String type
			,@RequestParam(value="mapt") Integer mapt) {
		List<ArcgisInfo> resultList = new ArrayList<ArcgisInfo>();
		resultList=arcgisInfoService.getArcgisCommonHs(wid, type, mapt);
		return resultList;
	}


	@ResponseBody
	@RequestMapping("/checkUsersHasTrajectoryJsonp")
	public String checkUsersHasTrajectory( HttpServletRequest request, @RequestParam("paramStr") String paramStr) {
		List<CheckUsersHasTrajectoryParam> param = JsonUtils.json2GenericList(paramStr, CheckUsersHasTrajectoryParam.class);
		String jsoncallback = request.getParameter("jsoncallback");
		jsoncallback=jsoncallback+"("+ JsonHelper.getJsonString(arcgisInfoService.checkUsersHasTrajectory(param))+")";
		return jsoncallback;
	}
}
