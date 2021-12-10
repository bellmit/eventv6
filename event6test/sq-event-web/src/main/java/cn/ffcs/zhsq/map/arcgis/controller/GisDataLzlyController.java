package cn.ffcs.zhsq.map.arcgis.controller;

import cn.ffcs.lzly.mybatis.domain.importantUnit.ImportantUnit;
import cn.ffcs.lzly.mybatis.domain.twoStation.TwoStation;
import cn.ffcs.lzly.service.importantUnit.ImportantUnitService;
import cn.ffcs.lzly.service.twoStation.ITwoStationService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisHeatMapInfo;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 两站两员地图数据
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/Lzly")
public class GisDataLzlyController extends ZZBaseController {

	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ITwoStationService twoStationService;

	@RequestMapping(value="/toTwoStationList")
	public String toTwoStationList(HttpSession session,
										  @RequestParam(value="orgCode") String orgCode,
										  @RequestParam(value="infoOrgCode") String infoOrgCode,
										  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr, ModelMap map) {
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/lzly/twoStation.ftl";
	}

	@ResponseBody
	@RequestMapping(value="/twoStationListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination twoStationListData(HttpSession session,
													 @RequestParam(value="page") int page,
													 @RequestParam(value="rows") int rows,
													 @RequestParam(value="regionCode") String regionCode,
													 @RequestParam(value="name", required=false) String stationName,
													 @RequestParam(value="stationType", required=false) String stationType) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(page<=0) page=1;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgCode", userInfo.getOrgCode());
		if (StringUtils.isNotBlank(stationName)) params.put("stationName", stationName);
		if (StringUtils.isNotBlank(stationType)) params.put("stationType", stationType);
		if (StringUtils.isNotBlank(regionCode)) params.put("regionCode", regionCode);
		cn.ffcs.common.EUDGPagination pagination = twoStationService.findPagination(page, rows, params);
		return pagination;
	}

	@ResponseBody
	@RequestMapping(value = "/twoStationListGisData")
	public List<ArcgisInfoOfPublic> twoStationListGisData(HttpSession session,
														  ModelMap map, HttpServletResponse res,
														  @RequestParam(value="ids") String ids,
														  @RequestParam(value="markType", required=false) String markType,
														  @RequestParam(value="mapt", required=false) Integer mapt,
														  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		if (StringUtils.isNotBlank(ids)) {
			try {
				list = this.arcgisDataOfLocalService.getArcgisTwoStationLocateDataListByIds(ids,mapt,markType);
				for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
					arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@RequestMapping(value="/twoStationDetail")
	public String twoStationDetail(HttpSession session,
								   @RequestParam(value="tsId") Long tsId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		TwoStation twoStation = twoStationService.findById(tsId, userInfo.getOrgCode());
//		PlaceInfo placeInfo = placeInfoService.findPlaceInfoById(plaId);
		map.addAttribute("twoStation", twoStation);
//		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(placeInfo.getGridId(), false);
//		Long gridId = gridInfo.getGridId();
//		String infoOrgCode=gridInfo.getInfoOrgCode();
//
//		String gridNames="";
//		if(gridId!=null){
//			gridNames= mixedGridInfoService.getGridPath(gridId);
//		}else{
//			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
//		}
//		map.addAttribute("gridNames", gridNames);
		map.addAttribute("RESOURSE_SERVER_PATH",  getNewUrl(session, App.IMG.getDomain(session)));//支持多个域名访问综治网格

		return "/map/arcgis/standardmappage/lzly/twoStationDetail.ftl";
	}

	@Autowired
	private ImportantUnitService importantUnitService;

	@RequestMapping(value="/toImportantUnitList")
	public String toImportantUnitList(HttpSession session,
										  @RequestParam(value="orgCode") String orgCode,
										  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr, ModelMap map) {
		map.addAttribute("infoOrgCode", orgCode);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/lzly/importantUnit.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value="/importantUnitListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination importantUnitListData(HttpSession session,
													 @RequestParam(value="page") int page,
													 @RequestParam(value="rows") int rows,
													 @RequestParam(value="infoOrgCode") String infoOrgCode,
													 @RequestParam(value="iuType", required=false) String iuType) {
		if(page<=0) page=1;
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(infoOrgCode)) params.put("infoOrgCode", infoOrgCode);
		if (StringUtils.isNotBlank(iuType)) params.put("iuType", iuType);
		cn.ffcs.common.EUDGPagination pagination = importantUnitService.searchList(page, rows, params);
		return pagination;
	}
	

	@ResponseBody
	@RequestMapping(value = "/importantUnitListGisData")
	public List<ArcgisInfoOfPublic> importantUnitListGisData(HttpSession session,
														  ModelMap map, HttpServletResponse res,
														  @RequestParam(value="ids") String ids,
														  @RequestParam(value="markType", required=false) String markType,
														  @RequestParam(value="mapt", required=false) Integer mapt,
														  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		if (StringUtils.isNotBlank(ids)) {
			try {
				list = this.arcgisDataOfLocalService.getArcgisImportantUnitLocateDataListByIds(ids,mapt,markType);
				for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
					arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	

	@RequestMapping(value="/importantUnitDetail")
	public String importantUnitDetail(HttpSession session,
								   @RequestParam(value="iuId") Long iuId, ModelMap map) {
		ImportantUnit importantUnit = importantUnitService.searchById(iuId);
		map.addAttribute("importantUnit", importantUnit);
		map.addAttribute("RESOURSE_SERVER_PATH",  getNewUrl(session, App.IMG.getDomain(session)));//支持多个域名访问综治网格

		return "/map/arcgis/standardmappage/lzly/importantUnitDetail.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/jumpTrafficAccidentStatPage")
	public List<ArcgisHeatMapInfo> jumpTrafficAccidentStatPage(HttpSession session, ModelMap map,HttpServletResponse res, 
			@RequestParam(value = "infoOrgCode") String infoOrgCode,
			@RequestParam(value = "mapt", required = false) Integer mapt) {
		List<ArcgisHeatMapInfo> statDatas = arcgisDataOfLocalService.getTrafficAccidentEvents(infoOrgCode,mapt);
		return statDatas;
	}
}
