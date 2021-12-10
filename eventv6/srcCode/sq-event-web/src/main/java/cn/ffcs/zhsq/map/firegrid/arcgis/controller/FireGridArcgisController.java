package cn.ffcs.zhsq.map.firegrid.arcgis.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.ffcs.shequ.base.domain.db.OrgExtraInfo;
import cn.ffcs.shequ.grid.domain.db.OrgEntityInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.firegrid.map.arcgis.service.IOrgConfigService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

@Controller
@RequestMapping(value="/zhsq/map/firegrid/arcgis/arcgis")
public class FireGridArcgisController extends ZZBaseController {
	@Autowired
	private IOrgConfigService orgConfigService;
	
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
	@RequestMapping(value="/toMapArcgisOfFireGridCrossDomain")
	public String toMapArcgisOfFireGridCrossDomain(HttpSession session, HttpServletResponse response, ModelMap map
			,@RequestParam(value="orgId", required=false) String orgId) throws Exception{
		String forward = "/map/firegridarcgis/arcgis_draw/arcgis_anchorpoint_domain.ftl";
		
		OrgExtraInfo orgExtraInfo = orgConfigService.getOrgExtraInfo(Long.valueOf(orgId));
		
		// -- 如果本层没有配置点位，默认定位到上级中心点
		try {
			long orgIdLong = Long.parseLong(orgId);
			if (orgExtraInfo == null) {
				orgExtraInfo = new OrgExtraInfo();
				orgExtraInfo.setOrgId(orgIdLong);
			}
			if (orgExtraInfo != null
					&& (orgExtraInfo.getLatitude() == null || orgExtraInfo
							.getLatitude() == null)) {
				OrgEntityInfo orgEntity = orgConfigService
						.queryEntityByPK(orgIdLong);
				if (orgEntity != null && orgEntity.getParentOrgId() != null
						&& orgEntity.getParentOrgId() > 0) {
					OrgExtraInfo parentOrgExtraInfo = orgConfigService
							.getOrgExtraInfo(orgEntity.getParentOrgId());
					if (parentOrgExtraInfo != null) {
						orgExtraInfo.setLatitude(parentOrgExtraInfo
								.getLatitude());
						orgExtraInfo.setLongitude(parentOrgExtraInfo
								.getLongitude());
						orgExtraInfo.setMapCoordinate(parentOrgExtraInfo
								.getMapCoordinate());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		map.addAttribute("orgId", orgId);
		map.addAttribute("orgExtraInfo", orgExtraInfo);
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session)); // zzgrid链接
		map.put("SQ_FIREGRID_URL", App.FIREGRID.getDomain(session)); // firegrid链接
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
	@RequestMapping(value="/toTDTMapArcgisOfFireGridCrossDomain")
	public String toTDTMapArcgisOfFireGridCrossDomain(HttpSession session, HttpServletResponse response, ModelMap map
			,@RequestParam(value="orgId", required=false) String orgId) throws Exception{
		String forward = "/map/firegridarcgis/arcgis_draw/display_arcgis_anchorpoint_domain.ftl";
		
		OrgExtraInfo orgExtraInfo = orgConfigService.getOrgExtraInfo(Long.valueOf(orgId));
		
		// -- 如果本层没有配置点位，默认定位到上级中心点
		try {
			long orgIdLong = Long.parseLong(orgId);
			if (orgExtraInfo == null) {
				orgExtraInfo = new OrgExtraInfo();
				orgExtraInfo.setOrgId(orgIdLong);
			}
			if (orgExtraInfo != null
					&& (orgExtraInfo.getLatitude() == null || orgExtraInfo
							.getLatitude() == null)) {
				OrgEntityInfo orgEntity = orgConfigService
				.queryEntityByPK(orgIdLong);
				if (orgEntity != null && orgEntity.getParentOrgId() != null
						&& orgEntity.getParentOrgId() > 0) {
					OrgExtraInfo parentOrgExtraInfo = orgConfigService
					.getOrgExtraInfo(orgEntity.getParentOrgId());
					if (parentOrgExtraInfo != null) {
						orgExtraInfo.setLatitude(parentOrgExtraInfo
								.getLatitude());
						orgExtraInfo.setLongitude(parentOrgExtraInfo
								.getLongitude());
						orgExtraInfo.setMapCoordinate(parentOrgExtraInfo
								.getMapCoordinate());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		map.addAttribute("orgId", orgId);
		map.addAttribute("orgExtraInfo", orgExtraInfo);
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session)); // zzgrid链接
		map.put("SQ_FIREGRID_URL", App.FIREGRID.getDomain(session)); // firegrid链接
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
}
