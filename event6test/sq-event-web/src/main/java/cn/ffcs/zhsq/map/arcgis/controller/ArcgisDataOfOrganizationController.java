package cn.ffcs.zhsq.map.arcgis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CorBaseInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CorpTaxInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.legal.LegalInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.legal.LegalTagDict;
import cn.ffcs.shequ.zzgl.service.legal.ILegalInfoService;
import cn.ffcs.shequ.zzgl.service.legal.ILegalTagDictService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisBaseInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.mybatis.domain.zzgl.firegrid.FireTeam;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.work.WorkAtt;
import cn.ffcs.shequ.zzgl.service.firegrid.IFireTeamNewService;
import cn.ffcs.shequ.zzgl.service.work.IWorkAttService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 组织图层控制器
 * 
 * @author huangmw
 * 
 */
@Controller
@RequestMapping(value = "/zhsq/map/arcgis/arcgisdataoforganization")
public class ArcgisDataOfOrganizationController extends ZZBaseController {

	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	private IWorkAttService workAttService;
	@Autowired
	private IFireTeamNewService fireTeamNewService; // 消防队伍服务（形式分类1.现役消防队2.义务消防队
													// 3.志愿消防队）
	@Autowired
	private ILegalInfoService legalInfoService;//法人
	@Autowired
	private ILegalTagDictService tagDictService;

	public static final String ACTIVE_FIRETEAM_CATALOG = "active"; // 现役消防队
	public static final String OBLIGATION_FIRETEAM_CATALOG = "obligation"; // 义务消防队
	public static final String VOLUNTEER_FIRETEAM_CATALOG = "volunteer"; // 志愿消防队
	
	public static final String BUS_CODE = "fireTeam";

	@RequestMapping(value = "/fireTeamIndex")
	public String fireTeamIndex(HttpSession session, ModelMap map,
			@RequestParam(value = "teamType", required = true) String teamType,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("teamType", teamType);

		String markType = ConstantValue.MARKER_TYPE_FIRE_XF_XY; // 现役消防队

		if (OBLIGATION_FIRETEAM_CATALOG.equals(teamType)) {
			markType = ConstantValue.MARKER_TYPE_FIRE_XF_YW; // 义务消防队
		} else if (VOLUNTEER_FIRETEAM_CATALOG.equals(teamType)) {
			markType = ConstantValue.MARKER_TYPE_FIRE_XF_ZY; // 志愿消防队
		}

		map.addAttribute("markType", markType);

		if (ACTIVE_FIRETEAM_CATALOG.equals(teamType)) {
			// 1.现役消防队
			return "/map/arcgis/standardmappage/organization/fireTeamNew/activeFireTeamIndex.ftl";
		} else if (OBLIGATION_FIRETEAM_CATALOG.equals(teamType)) {
			// 2.义务消防队
			return "/map/arcgis/standardmappage/organization/fireTeamNew/obligationFireTeamIndex.ftl";
		} else {
			// 3.志愿消防队
			return "/map/arcgis/standardmappage/organization/fireTeamNew/volunteerFireTeamIndex.ftl";
		}
	}

	@ResponseBody
	@RequestMapping(value = "/fireTeamListData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination fireTeamListData(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "page") int page, @RequestParam(value = "rows") int rows) {
		String teamType = request.getParameter("teamType");
		String infoOrgCode = request.getParameter("infoOrgCode");
		String name = request.getParameter("name");

		Map<String, Object> params = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(teamType)) {
			params.put("teamType", teamType.trim());
		}

		if (StringUtils.isNotBlank(infoOrgCode)) {
			params.put("infoOrgCode", infoOrgCode.trim());
		}

		if (StringUtils.isNotBlank(name)) {
			params.put("name", name.trim());
		}

		cn.ffcs.common.EUDGPagination pagination = fireTeamNewService.findFireTeamPaginationForMap(teamType, page, rows, params);
		return pagination;
	}

	@ResponseBody
	@RequestMapping(value = "/fireTeamLocateData")
	public List<ArcgisInfoOfPublic> fireTeamLocateData(HttpSession session, ModelMap map, HttpServletResponse res,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "markType", required = false) String markType,
			@RequestParam(value = "mapt", required = false) Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisFireTeamNewLocateDataListByIds(ids,
				mapt, markType);
		for (ArcgisInfoOfPublic arcgisInfoOfPublic : list) {
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}

		return list;
	}

	@RequestMapping(value = "/fireTeamDetail")
	public String fireTeamDetail(HttpSession session, @RequestParam(value = "fireTeamId") Long fireTeamId, ModelMap map) {
		FireTeam fireTeam = fireTeamNewService.findFireTeamById(fireTeamId);
		
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(fireTeam.getGridId(), false);
		Long gridId = gridInfo.getGridId();
		String infoOrgCode=gridInfo.getInfoOrgCode();
		
		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
		}
		map.addAttribute("gridNames", gridNames);
		
		String rootPath = getNewUrl(session, App.IMG.getDomain(session)) + "/";
		List<WorkAtt> workAtts = workAttService.findWorkAtt(BUS_CODE, fireTeamId);
		
		if (workAtts.size() > 0) {
			if (!workAtts.get(0).getAttPath().contains("/zzgrid/")) { // 图片路径不包含zzgrid
				rootPath = rootPath + "zzgrid/";
			}
				  
			fireTeam.setPhotoPath(rootPath + workAtts.get(0).getAttPath());
		}
		
		map.addAttribute("fireTeam", fireTeam);
		return "/map/arcgis/standardmappage/organization/fireTeamNew/fireTeamDetail.ftl";
	}


	@RequestMapping(value = "/legalPersonIndex")
	public String legalPersonIndex(HttpSession session, ModelMap map,
			@RequestParam(value="gridId") Long gridId,
			@RequestParam(value = "legalName", required = false) String legalName,
			@RequestParam(value = "legalType", required = false) String legalType,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridId", gridId);

		if (StringUtils.isNotBlank(legalType)) {
			map.addAttribute("legalType", legalType);
		}
		if (StringUtils.isNotBlank(legalName)) {
			map.addAttribute("legalName", legalName);
		}

		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);

		//组织类型
		Map<String, Object> p1 = new HashMap<String, Object>();
		p1.put("pCode", "L002");
		map.addAttribute("legalTypeDC", tagDictService.findLegalTagDict(p1));

		return "/map/arcgis/standardmappage/organization/legalPerson/standardLegalPerson.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/legalPersonListData", method = RequestMethod.POST)
	public cn.ffcs.system.publicUtil.EUDGPagination legalPersonListData(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "legalName", required = true) String legalName,
			@RequestParam(value = "legalType", required = true) String legalType,
			@RequestParam(value = "industryClassification", required = true) String industryClassification,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows) {

		Map<String, Object> params = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(legalType)) {
			params.put("legalType", legalType.trim());
		}

		if (StringUtils.isNotBlank(infoOrgCode)) {
			params.put("infoOrgCode", infoOrgCode.trim());
		}

		if (StringUtils.isNotBlank(legalName)) {
			params.put("legalName", legalName.trim());
		}

		if (StringUtils.isNotBlank(industryClassification)) {
			params.put("industryClassification", industryClassification.trim());
		}

		cn.ffcs.system.publicUtil.EUDGPagination pagination = legalInfoService.searchList(page, rows, params);
		return pagination;
	}

	@ResponseBody
	@RequestMapping(value = "/legalPersonLocateData")
	public List<ArcgisBaseInfo> legalPersonLocateData(HttpSession session, ModelMap map, HttpServletResponse res,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt", required = false) Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		List<ArcgisBaseInfo> list = this.arcgisDataOfLocalService.getArcgisLegalPersonLocateDataListByIds(ids, mapt);
		for (ArcgisBaseInfo arcgisBaseInfo : list) {
			arcgisBaseInfo.setElementsCollectionStr(elementsCollectionStr);
		}

		return list;
	}

	/**
	 * 查看法人基础信息
	 * @param session
	 * @param plaId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getLegalPersonDetailOnMap")
	public String getCorInfoDetailOnMap(HttpSession session, HttpServletRequest request,
										@RequestParam(value="wid") String wid,
										ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		LegalInfo legalInfo=new LegalInfo();
		Long legalId = null;
		if (StringUtils.isNotBlank(wid)) {
			String[] widArr = wid.split("-");
			if(widArr != null && widArr.length >0) {
				legalId = Long.parseLong(widArr[0]);
			}
		}
		legalInfo.setLegalId(legalId);

		legalInfo = legalInfoService.searchById(legalId, userInfo);
		if(legalInfo != null){
			map.addAttribute("typeDC", getTypeDC(legalInfo.getLegalType()));
		}
		map.addAttribute("record", legalInfo);
		map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));

		return "/map/arcgis/standardmappage/organization/legalPerson/legalPersonDetail.ftl";
	}

	public String getTypeDC(String tagCode) {
		String typeDC = "";
		if (StringUtils.isNotBlank(tagCode)) {
			Map<String, Object> p1 = new HashMap<String, Object>();
			p1.put("pCode", "L002");
			List<LegalTagDict> legalTagDictList = tagDictService.findLegalTagDict(p1);
			for (LegalTagDict l : legalTagDictList) {
				if (tagCode.equals(l.getCode())) {
					typeDC = l.getTagName();
					return typeDC;
				}
			}
		}
		return typeDC;
	}
}
