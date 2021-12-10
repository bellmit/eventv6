package cn.ffcs.zhsq.map.gridOL.controller;


import java.util.ArrayList;
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
import cn.ffcs.shequ.zzgl.service.grid.IAreaBuildingInfoService;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.map.buildaddress.service.IBuildAddressService;
import cn.ffcs.zhsq.map.thresholdcolorcfg.service.IThresholdColorCfgService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;


@Controller
@RequestMapping(value="/zhsq/map/gridOL/gridOLController")
public class GridOLController  extends ZZBaseController{
	@Autowired
	private IThresholdColorCfgService thresholdColorCfgService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private IAreaBuildingInfoService areaBuildingInfoService;
	@Autowired
	private IBuildAddressService buildAddressService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IArcgisInfoService arcgisInfoService;
	
	@RequestMapping(value="/blindGridIndex")
	public String blindGridIndex(HttpSession session, HttpServletRequest request, ModelMap map){
		String forward = "/map/gridOL/list_grid.ftl";
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
			@RequestParam(value = "singleFlag",required = false) String singleFlag,
			@RequestParam(value = "gridId",required = false)Long gridId){
		String forward = "";
		//if(StringUtils.isNotBlank(singleFlag) && "true".equals(singleFlag)){
			forward = "/map/gridOL/index_olwfs_grid_single.ftl";
		//}
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long startGridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		//​获取指定网格地图信息
		if(gridId==null){
			gridId = startGridId;
		}
		//defaultGridInfo.get(key)
		map.put("wid", gridId);
		List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,5);
		if(list != null && list.size()>0){
			for (ArcgisInfoOfGrid arcgisInfoOfGrid : list) {
				if(arcgisInfoOfGrid.getX() != null){
					Double mapCenterX = arcgisInfoOfGrid.getX();
					map.put("mapCenterX", mapCenterX.toString());
				}
				if(arcgisInfoOfGrid.getY() != null){
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
		map.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
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
	 * <p>Description:单条楼宇轮廓编辑跳转</p>
	 * @param session
	 * @param request
	 * @param map
	 * @param buildingId
	 * @return
	 * add sulch
	 * 2016年4月12日 下午2:24:58
	 */
	@RequestMapping(value="/singleGridDrawIndex")
	public String singleGridDrawIndex(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="buildingId",required = false) Long buildingId){
		String forward = "/map/buildaddress/index_single_building_draw.ftl";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String infoOrgCode = userInfo.getInfoOrgCodeStr();
		if(StringUtils.isNotBlank(infoOrgCode)){
			if(infoOrgCode.startsWith("350582")){
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
			if(infoOrgCode.startsWith("350582")){
				map.put("targetURL", "/zhsq/map/buildaddress/buildAddress/blindBuildAddressIndex.jhtml");
			}else{
				map.put("targetURL", "/zhsq/drowBuildingController/index.jhtml");
			}
		}
		return forward;
	}
	
	@ResponseBody
	@RequestMapping(value="/updateMapDataOfGridOfBinding")
	public Map<String,Object> updateMapDataOfGridOfBinding(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "wid") Long wid,
			@RequestParam(value="mapt") Integer mapt,
			@RequestParam(value="nameColor",required = false) String nameColor,
			@RequestParam(value="areaColor",required = false) String areaColor,
			@RequestParam(value="lineColor",required = false) String lineColor,
			@RequestParam(value="mapCenterLevel",required = false) String mapCenterLevel,
			@RequestParam(value="x",required = false) String x,
			@RequestParam(value="y",required = false) String y,
			@RequestParam(value="hs",required = false) String hs,
			@RequestParam(value="saveCenterPointFlag",required = false) Boolean saveCenterPointFlag
			) {
		if(saveCenterPointFlag == null){
			saveCenterPointFlag = false;
		}
		ArcgisInfoOfGrid arcgisInfoOfGrid = new ArcgisInfoOfGrid();
		List<ArcgisInfoOfGrid> arcgisInfoOfGrid2 = new ArrayList<ArcgisInfoOfGrid>();
		if(wid != null){
			arcgisInfoOfGrid.setWid(wid);
		}
		if(mapt != null){
			arcgisInfoOfGrid.setMapt(mapt);
		}
		arcgisInfoOfGrid2 = arcgisInfoService.getArcgisDataOfGrid(wid, mapt);
		
		if(StringUtils.isNotBlank(x) && !"undefined".equals(x) && !"NaN".equals(x)){
			arcgisInfoOfGrid.setX(Double.parseDouble(x));
		}
		if(StringUtils.isNotBlank(y) && !"undefined".equals(y) && !"NaN".equals(y)){
			arcgisInfoOfGrid.setY(Double.parseDouble(y));
		}
		
		if(StringUtils.isNotBlank(hs) && !"undefined".equals(hs)){
			//如果已经有数据了，编辑轮廓时，有中心坐标值的话就不更新中心点坐标
			if(arcgisInfoOfGrid2 != null && arcgisInfoOfGrid2.size()>0){
				if(StringUtils.isNotBlank(arcgisInfoOfGrid2.get(0).getHs())){
					if((arcgisInfoOfGrid2.get(0).getX() == null && arcgisInfoOfGrid2.get(0).getY()==null) || saveCenterPointFlag){
						if(StringUtils.isNotBlank(x) && !"undefined".equals(x)){
							arcgisInfoOfGrid.setX(Double.parseDouble(x));
						}
						if(StringUtils.isNotBlank(y) && !"undefined".equals(x)){
							arcgisInfoOfGrid.setY(Double.parseDouble(y));
						}
					} else {
						arcgisInfoOfGrid.setX(null);
						arcgisInfoOfGrid.setY(null);
					}
				}
			}
			arcgisInfoOfGrid.setHs(hs);
		}
		if(hs != null && "undefined".equals(hs)){
			arcgisInfoOfGrid.setHs("");
		}
		if(StringUtils.isBlank(nameColor) || "null".equals(mapCenterLevel)){
			arcgisInfoOfGrid.setNameColor("#ff0000");
		}else{
			arcgisInfoOfGrid.setNameColor(nameColor);
		}
		if(StringUtils.isBlank(lineColor) || "null".equals(mapCenterLevel)){
			arcgisInfoOfGrid.setLineColor("#ff0000");
		}else{
			arcgisInfoOfGrid.setLineColor(lineColor);
		}
		if(StringUtils.isBlank(areaColor) || "null".equals(mapCenterLevel)){
			arcgisInfoOfGrid.setAreaColor("#ff0000");
		}else{
			arcgisInfoOfGrid.setAreaColor(areaColor);
		}
		if(StringUtils.isBlank(mapCenterLevel) || "null".equals(mapCenterLevel)){
			arcgisInfoOfGrid.setMapCenterLevel(6);
		}else{
			arcgisInfoOfGrid.setMapCenterLevel(Integer.parseInt(mapCenterLevel));
		}
		arcgisInfoOfGrid.setLineWidth(1);
		boolean flag = this.arcgisInfoService.saveArcgisDrawAreaOfGrid(arcgisInfoOfGrid);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("flag", flag);
		return result;
	}
	
	@RequestMapping(value="/spGisGridIndex")
	public String spGisGridIndex(HttpSession session, HttpServletRequest request, ModelMap map){
		String forward = "/map/gridOL/list_spgis_grid.ftl";
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
			@RequestParam(value = "singleFlag",required = false) String singleFlag,
			@RequestParam(value = "gridId",required = false)Long gridId){
		String forward = "";
		forward = "/map/gridOL/index_pingtan_ol.ftl";
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long startGridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		//​获取指定网格地图信息
		if(gridId==null){
			gridId = startGridId;
		}
		map.put("wid", gridId);
		List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,5);
		if(list != null && list.size()>0){
			for (ArcgisInfoOfGrid arcgisInfoOfGrid : list) {
				if(arcgisInfoOfGrid.getX() != null){
					Double mapCenterX = arcgisInfoOfGrid.getX();
					map.put("mapCenterX", mapCenterX.toString());
				}
				if(arcgisInfoOfGrid.getY() != null){
					map.put("mapCenterY", arcgisInfoOfGrid.getY().toString());
				}
			}
		}
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		String OUTLINE_FONT_SETTINGS = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		map.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
		return forward;
	}
}
