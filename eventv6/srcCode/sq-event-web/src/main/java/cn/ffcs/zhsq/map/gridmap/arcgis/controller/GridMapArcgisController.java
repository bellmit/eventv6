package cn.ffcs.zhsq.map.gridmap.arcgis.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.ffcs.icgrid.grid.service.IIcGridMapService;
import cn.ffcs.shequ.base.domain.db.OrgExtraInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

@Controller
@RequestMapping(value="/zhsq/map/gridmap/arcgis/arcgis")
public class GridMapArcgisController extends ZZBaseController {
	@Autowired
	private IIcGridMapService icGridMapService;
	//private static final String mapType = ConstantValue.ma;
	
	@RequestMapping(value="/viewGridMapCrossDomain")
	public String viewGridMapCrossDomain(
			@RequestParam(value = "orgId", required = false) Long orgId,
			@RequestParam(value = "gridId", required = false) Long gridId,
			HttpSession session, ModelMap map) throws Exception{
		map.addAttribute("enterpriseInfoString", "[{\"id\":\"123\",\"name\":\"mysh\",\"latitude\":25," +
						"\"longitude\":116}]");
		fetchMapInfo(orgId, gridId, true, map);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.addAttribute("ICGRID_URL", App.ICGRID.getDomain(session));
		
		String forward = "/map/gridmaparcgis/arcgis_draw/view_gridmap.ftl";
		return forward;
	}
	
	@RequestMapping(value="/editGridMapCrossDomain")
	public String editGridMapCrossDomain(
			@RequestParam(value = "orgId", required = false) Long orgId,
			@RequestParam(value = "gridId", required = false) Long gridId,
			HttpSession session, ModelMap map) throws Exception{
//		map.addAttribute("enterpriseInfoString", "[{\"id\":\"123\",\"name\":\"mysh\",\"latitude\":25," +
//						"\"longitude\":116}]");
		fetchMapInfo(orgId, gridId, true, map);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.addAttribute("ICGRID_URL", App.ICGRID.getDomain(session));
		
		String forward = "/map/gridmaparcgis/arcgis_draw/edit_gridmap.ftl";
		return forward;
	}
	
	private void fetchMapInfo(Long orgId, Long gridId, boolean isGetSubArea, ModelMap map) {
		OrgExtraInfo orgExtraInfo = null;
		if (orgId != null) {
			orgExtraInfo = icGridMapService.getOrgExtraInfo(orgId, "001", isGetSubArea);
			map.addAttribute("orgId", orgId);
			map.addAttribute("isOrgOrGrid", 0);
		}
		if (gridId != null) {
			orgExtraInfo = icGridMapService.getOrgExtraInfoByGridId(gridId, isGetSubArea);
			map.addAttribute("orgId", gridId);
			map.addAttribute("isOrgOrGrid", 1);
		}
		map.addAttribute("orgExtraInfo", orgExtraInfo);
	}
}
