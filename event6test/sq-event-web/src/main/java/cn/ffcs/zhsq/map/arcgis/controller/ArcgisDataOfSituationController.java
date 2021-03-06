package cn.ffcs.zhsq.map.arcgis.controller;

/**
 * 
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.ffcs.shequ.zzgl.service.eyesbind.IEyesBindService;
import cn.ffcs.uam.bo.UserExBO;
import cn.ffcs.uam.service.UserManageOutService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.common.DictPcode;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.common.Pagination;
import cn.ffcs.doorsys.bo.control.ControlTargetRecord;
import cn.ffcs.gmis.mybatis.domain.teamMembers.TeamMembers;
import cn.ffcs.gmis.plc.service.IPlcService;
import cn.ffcs.gmis.prvetionTeam.service.IPrvetionTeamService;
import cn.ffcs.gmis.teamMembers.service.ITeamMembersService;
import cn.ffcs.resident.bo.CiRsCriteria;
import cn.ffcs.resident.bo.CiRsTop;
import cn.ffcs.resident.service.CiRsService;
import cn.ffcs.service.ag.Interface.AgricultureService;
import cn.ffcs.service.ag.Interface.PopulationService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.mybatis.domain.zzgl.globalEyes.MonitorBO;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.GridAdmin;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PrvetionTeam;
import cn.ffcs.shequ.mybatis.domain.zzgl.productSafety.SafetyCheckCon;
import cn.ffcs.shequ.mybatis.domain.zzgl.productSafety.SafetyCheckInfo;
import cn.ffcs.shequ.wap.domain.pojo.ResMarkedOM;
import cn.ffcs.shequ.zzgl.domain.db.CiRsCar;
import cn.ffcs.shequ.zzgl.service.globalEyes.MonitorService;
import cn.ffcs.shequ.zzgl.service.grid.ICareRoadOrgService;
import cn.ffcs.shequ.zzgl.service.grid.IGridAdminService;
import cn.ffcs.shequ.zzgl.service.main.ICiRsCarService;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.shequ.zzgl.service.productSafety.IRoutineExaminationService;
import cn.ffcs.shequ.zzgl.service.res.IResInfoService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.water.bo.MonitorSiteBO;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.be.service.arcgis.ArcgisInfoService;
import cn.ffcs.zhsq.idssControl.service.IIdssControlService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.map.coordinateconversion.service.IBaseConversionService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfo;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.ParamUtils;
import cn.ffcs.zhsq.utils.ReadProperties;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.wgs.WGSUtils;

/**
 * 2014-06-23 chenlf add
 * arcgis "???" ???????????????????????????
 * @author chenlfqun
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisDataOfSituationController")
public class ArcgisDataOfSituationController extends ZZBaseController {

	@Autowired
	private CiRsService ciRsService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	private IEyesBindService eyesBindService;
	@Autowired
	private MonitorService monitorService;
	@Autowired
	private cn.ffcs.water.service.MonitorService waterMonitorService;
	@Autowired
	private IGridAdminService gridAdminService;
	@Autowired 
	protected IGisInfoService gisInfoService; //gis?????????????????? 
	@Autowired
	private IBaseConversionService baseConversionService;
	@Autowired
	private IDictionaryService dictionaryService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private ITeamMembersService teamMembersService;
	@Autowired
	private IPrvetionTeamService prvetionTeamServiceGmis;
	@Autowired
	private ICareRoadOrgService careRoadOrgService;
	@Autowired
	private IRoutineExaminationService routineExaminationService; //????????????
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;  //?????????????????????
	@Autowired
	private IPlcService plcService;
	@Autowired
	private AgricultureService agricultureService;
	@Autowired
	private PopulationService populationService;
	@Autowired
	private IIdssControlService idssControlService;//????????????
	@Autowired
	private ICiRsCarService ciRsCarService;//??????
	@Autowired
    private IResInfoService resInfoService;//????????????????????????????????????????????????
	@Autowired
	private UserManageOutService userManageService;
	
	/*-------------------------------------------??????????????????-------------------------------------------*/
	/**
	 * ????????????
	 * @param session
	 * @param orgCode ?????????????????????
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/risk")
	public String risk(HttpSession session, @RequestParam(value="orgCode") String orgCode, ModelMap map,
			@RequestParam(value="standard", required=false) String standard) {
		map.addAttribute("orgCode", orgCode);
		return "/map/arcgis/standardmappage/risk/standardRisk.ftl";
	}
	
	/**
	 * @author huangmw
	 * @time 2015???4???9??? add
	 * @Description: ????????????????????????
	 * @param session
	 * @param request
	 * @param map
	 * @param page
	 * @param rows
	 * @param infoOrgCode
	 * @param corplaceName ????????????????????????????????????
	 * @param checkContent ????????????????????????????????????
	 * @param siteType ?????????????????????1????????????2????????????3???????????????
	 * @param beginCheckDate ??????????????????
	 * @param checkDate ??????????????????
	 * @return ??????????????????
	 */
	@RequestMapping(value="/riskListData")
	@ResponseBody
	public cn.ffcs.common.EUDGPagination riskListData(HttpSession session,HttpServletRequest request,ModelMap map,
			@RequestParam(value = "page") int page,
		    @RequestParam(value = "rows") int rows,
		    @RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
		    @RequestParam(value = "corplaceName", required = false) String corplaceName,
		    @RequestParam(value = "checkContent", required = false) String checkContent,
		    @RequestParam(value = "siteType", required = false) String siteType,
		    @RequestParam(value = "beginCheckDate", required = false) String beginCheckDate,
		    @RequestParam(value = "checkDate", required = false) String checkDate){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("infoOrgCode", infoOrgCode);
		params.put("corplaceName", corplaceName);
		params.put("checkContent", checkContent);
		params.put("siteType", siteType);
		params.put("beginCheckDate", beginCheckDate);
		params.put("checkDate", checkDate);
		cn.ffcs.common.EUDGPagination pagination = routineExaminationService.findRiskBuiling(page, rows, params);
		return pagination;
	}
	
	@RequestMapping(value="/viewRisk")
	public String viewRisk(HttpSession session, @RequestParam(value="id") Long id, ModelMap map) throws Exception{
		SafetyCheckInfo safetyCheckInfo = routineExaminationService.findSafetyCheckInfoById(id);
		SafetyCheckCon safetyCheckCon = routineExaminationService.getSafetyCheckConByCheckId(id);
		safetyCheckInfo.setSafetyCheckCon(safetyCheckCon);
		map.addAttribute("safetyCheckInfo", safetyCheckInfo);
		
		String orgName = orgSocialInfoOutService.findOrgNameByOrgCode(safetyCheckCon.getReportDepartment());
		safetyCheckCon.setReportDepartment(orgName);
		
		//-- ????????????		
		if(! org.apache.commons.lang.StringUtils.isBlank(safetyCheckCon.getRiskGrade())) {
			safetyCheckCon.setRiskGradeLabel(dictionaryService.getTableColumnText(ConstantValue.TABLE_NEW_SAFETY_CHECK_INFO, ConstantValue.COLUMN_DISPOSE_STATUS, safetyCheckCon.getRiskGrade()));
		}
		//-- ????????????		
		if(!org.apache.commons.lang.StringUtils.isBlank(safetyCheckCon.getReviewCase())) {
			safetyCheckCon.setReviewCaseLabel(dictionaryService.getTableColumnText(ConstantValue.TABLE_NEW_SAFETY_CHECK_INFO, ConstantValue.COLUMN_DISPOSE_STATUS, safetyCheckCon.getReviewCase()));
		}
		
		return "/map/arcgis/standardmappage/risk/viewRisk.ftl";
	}
	
	/**
	 * @author huangmw
	 * @time 2015???4???9??? add
	 * @Description: ??????????????????????????????
	 * @param session
	 * @param map
	 * @param res
	 * @param ids checkId??????
	 * @param mapt ????????????
	 * @return ??????????????????????????????????????????
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfRisk")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfRisk(HttpSession session,
			ModelMap map, HttpServletResponse res,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt") Integer mapt) {
		
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisRiskLocateDataListByIds(ids,mapt);
		return list;
	}
	
	/*-------------------------------------------??????????????????-------------------------------------------*/
	
	///---????????? start----------------------------------------------
	/**
	 * ??????????????????
	 * @param session
	 * @param orgCode ?????????????????????
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/globalEyes")
	public String globalEyes(HttpSession session, @RequestParam(value="orgCode") String orgCode, ModelMap map,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			@RequestParam(value = "pageType", required=false) String pageType) {
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		if(StringUtils.isNotBlank(pageType) && pageType.equals("smartCity")){ 
			return "/map/arcgis/arcgis_base/arcgis_smartCity/list_map_page/standardGlobalEyes.ftl";
		}else if(StringUtils.isNotBlank(pageType) && pageType.equals("SMART_MONITOR_CENTER")) {
			return "/map/arcgis/arcgis_base/smart_monitor_center/standardGlobalEyes.ftl";
		}else if(StringUtils.isNotBlank(pageType) && pageType.equals("SMART_MONITOR_CENTER_NANPING")) {
			return "/map/arcgis/arcgis_base/smart_monitor_center/standardGlobalEyes_nanping.ftl";
		}else{
			return "/map/arcgis/standardmappage/standardGlobalEyes.ftl";
		}
	}
	
	/**
	 * 
	 * ??????????????????-??????????????????????????????????????????????????????
	 * @param session
	 * @param gridId
	 * @param pageType
	 * @return
	 */
	@RequestMapping(value="/globalEyesWN")
	public String globalEyes1111(HttpSession session, 
			@RequestParam(value="gridId") String gridId, 
			@RequestParam(value="resTypeId") String resTypeId, 
			@RequestParam(value="typeCode") String typeCode,
			@RequestParam(value="orgCode") String orgCode, ModelMap map,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			@RequestParam(value = "pageType", required=false) String pageType) {
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("gridId", gridId);
		map.addAttribute("typeCode", typeCode);
		map.addAttribute("resTypeId", resTypeId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		if(StringUtils.isNotBlank(pageType) && pageType.equals("Pollution")) {
			return "/map/arcgis/arcgis_base/arcgis_smartCity/list_map_page/pollutionIndex.ftl";
		} else{
			return "/map/arcgis/arcgis_base/arcgis_smartCity/list_map_page/pollutionIndex.ftl";
		}
	}
	
	
	
	/**
	 * ??????????????????-????????????????????????
	 * @param session
	 * @param orgCode ?????????????????????
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/globalEyesGis")
	public String globalEyesGis(HttpSession session, @RequestParam(value="orgCode") String orgCode, ModelMap map,
			@RequestParam(value="standard", required=false) String standard) {
		map.addAttribute("orgCode", orgCode);
		return "/map/arcgis/standardmappage/standardGlobalEyesGis.ftl";
	}
	
	/**
	 * ????????????
	 * @param session
	 * @param orgCode
	 * @param map
	 * @param standard
	 * @return
	 */
	@RequestMapping(value="/spjk")
	public String spjk(HttpSession session, @RequestParam(value="orgCode") String orgCode, ModelMap map,
			@RequestParam(value="standard", required=false) String standard, @RequestParam(value="eyesType", required=false) String eyesType) {
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("eyesType", eyesType);
		return "/map/arcgis/standardmappage/standardSpjk.ftl";
	}
	
	/**
	 * ??????????????????
	 * @param session
	 * @param orgCode
	 * @param map
	 * @param standard
	 * @return
	 */
	@RequestMapping(value="/swjc")
	public String swjc(HttpSession session, @RequestParam(value="orgCode") String orgCode, ModelMap map,
			@RequestParam(value="standard", required=false) String standard) {
		map.addAttribute("orgCode", orgCode);
		return "/map/arcgis/standardmappage/standardSwjc.ftl";
	}
	
	/**
	 * ????????????????????????????????????
	 * @param session
	 * @param orgCode ?????????????????????
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/globalEyesPutian")
	public String globalEyesPutian(HttpSession session,
			@RequestParam(value = "orgCode") String orgCode, ModelMap map) {
		map.addAttribute("orgCode", orgCode);
		return "/map/arcgis/standardmappage/putian/globalEyes.ftl";
	}
	
	/**
	 * ???????????????????????????????????????
	 */
	@RequestMapping(value="/globalEyesTaijiang")
	public String globalEyesTaijiang(HttpSession session,
			@RequestParam(value = "orgCode") String orgCode, ModelMap map) {
		map.addAttribute("orgCode", orgCode);
		return "/map/arcgis/standardmappage/taijiang/globalEyes.ftl";
	}

	/**
	 * ???????????????
	 * @param session
	 * @param page ??????
	 * @param rows ????????????
	 * @param orgCode ?????????????????????
	 * @param platformName ????????????
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/globalEyesListData", method=RequestMethod.POST)
	public Pagination globalEyesListData(HttpSession session,
			@RequestParam(value="page") int page,
			@RequestParam(value="rows") int rows,
			@RequestParam(value="orgCode") String orgCode,
			@RequestParam(value="platformName", required=false) String platformName,
			@RequestParam(value="eyesType", required=false) String eyesType,
			@RequestParam(value="loginStatus", required=false) String loginStatus) {
		Map<String,Object> params = new HashMap<>();
		if(page<=0) page=1;
		if(platformName!=null) platformName = platformName.trim();
		/*if(StringUtils.isNotBlank(orgCode) && orgCode.startsWith(ConstantValue.NANPING_INFO_ORG_CODE)){
			if(orgCode.length() >= 6){//???????????????????????????????????????????????????????????????????????????
				orgCode = orgCode.substring(0,6);
			}
		}*/
		params.put("platformName", platformName);
		params.put("orgCode", orgCode);
		params.put("eyesType", eyesType);
		params.put("loginStatus", loginStatus);
		params.put("globalType", "003");
		
		Pagination pagination = monitorService.listGlobalEyeConfig(page, rows, params);
		return pagination;
	}
	
	
	/**
	 * ??????????????????????????????????????????????????????
	 * @param session
	 * @param page ??????
	 * @param rows ????????????
	 * @param resTypeId ??????id
	 * @param startGridId ??????id
	 * @param resName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pollutionListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination pollutionListData(HttpSession session,
			@RequestParam(value="page") int page,
			@RequestParam(value="rows") int rows,
			@RequestParam(value = "resTypeId") Long resTypeId,
            @RequestParam(value = "startGridId") Long startGridId,
            @RequestParam(value = "resName", required = false) String resName) {
		if(page<=0) page=1;
		if (resName != null)
            resName = resName.trim();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("resName", resName);
        params.put("resTypeId", resTypeId);
        params.put("startGridId", startGridId);
        cn.ffcs.common.EUDGPagination pagination = resInfoService.findResInfoPagination(page, rows, params);
        return pagination;
	}
	
	/**
	 * ????????????
	 * @param session
	 * @param page
	 * @param rows
	 * @param orgCode
	 * @param platformName
	 * @param eyesType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/spjkListData", method=RequestMethod.POST)
	public Pagination spjkListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="orgCode") String orgCode, @RequestParam(value="platformName", required=false) String platformName,
			@RequestParam(value="eyesType", required=false) String eyesType) {
		if(page<=0) page=1;
		if(platformName!=null) platformName = platformName.trim();
		Pagination pagination = monitorService.listGlobalEyeConfig(orgCode, page, rows, null, null, platformName, null,eyesType,"002");
		return pagination;
	}
	
	/**
	 * ??????????????????
	 * @param session
	 * @param page
	 * @param rows
	 * @param orgCode
	 * @param platformName
	 * @param eyesType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/swjcListData", method=RequestMethod.POST)
	public List<MonitorSiteBO> swjcListData(HttpSession session,HttpServletResponse response) {
		List<MonitorSiteBO> monitorSiteBOs = waterMonitorService.queryAllMonitorSiteList();
		return monitorSiteBOs;
	}
	
	/**
	 * 2014-06-24 chenlf add
	 * ??????????????????????????????
	 * @param session
	 * @param ids ?????????ids
	 * @param mapt ????????????
	 * @param showType ??????????????????1???????????????2?????????????????????
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfGlobalEyes")
	public List<ArcgisInfo> getArcgisGlobalEyesLocateDataList(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "ids20", required = false) String ids20,
		    String orgCode,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "showType") String showType,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfo> list = this.arcgisDataOfLocalService.getArcgisGlobalEyesLocateDataListByIds(ids,mapt,orgCode);
		if(StringUtils.isNotBlank(ids20)){
			List<ArcgisInfo> listFFcs = this.arcgisDataOfLocalService.getArcgisGlobalEyesLocateDataListByDeviceNums(ids20,mapt);
			if(listFFcs != null && listFFcs.size()>0){
				list.addAll(listFFcs);
			}
		}
		if(list != null && list.size() > 0) {
			for (ArcgisInfo arcgisInfo : list) {
				arcgisInfo.setElementsCollectionStr(elementsCollectionStr);
			}
		}
		return list;
	}


	/**
	 * 2014-06-24 chenlf add
	 * ??????????????????????????????
	 * @param session
	 * @param ids ?????????ids
	 * @param mapt ????????????
	 * @param showType ??????????????????1???????????????2?????????????????????
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEyesPointDataList")
	public List<ArcgisInfo> getEyesPointDataList(HttpSession session,
	  @RequestParam(value = "orgCode") String orgCode,
	  @RequestParam(value = "bindType") String bindType,
	  @RequestParam(value = "mapt") String mapt
												 ) {
		 Map<String, Object> param = new HashMap<>();
		param.put("infoOrgCode",orgCode);
		param.put("bindType",bindType);
		param.put("mapt",mapt);
		List<ArcgisInfo> list = this.eyesBindService.getEyesPointDataList(param);
		return list;
	}
	/**
	 * 
	 * ????????????????????????????????????????????????????????????????????????
	 * @param session
	 * @param ids ?????????ids
	 * @param mapt ????????????
	 * @param showType ??????????????????1???????????????2?????????????????????
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPollutionDataListOfGlobalEyes")
	public List<ArcgisInfo> getPollutionDataListOfGlobalEyes(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "ids20", required = false) String ids20,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value="typeCode") String typeCode,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "showType") String showType
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfo> list = this.arcgisDataOfLocalService.getArcgisGlobalEyesLocateDataListBypollution(typeCode,mapt);
		if("02070403".equals(typeCode) && list != null && list.size() > 0) {//????????????????????????
			for (ArcgisInfo arcgisInfo : list) {
				if(StringUtils.isNotBlank(arcgisInfo.getAddress())&& arcgisInfo.getAddress().startsWith("*")) {
					arcgisInfo.setElementsCollectionStr(elementsCollectionStr.replace("02070403_.png", "02070403.png"));
				}
			}
		}
		return list;
	}
	
	/**
	 * 2015-01-30 huangmw add
	 * ??????id ???mapt??????????????????????????????
	 * @param ids
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfSwjc")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfSwjc(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value = "mapt") Integer mapt) {
		
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisSwjcLocateDataListByIds(ids,mapt);
		return list;
	}
		
	
	/**
	 * ???????????????
	 * @param session
	 * @param monitorId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/globalEyesPlay")
	public String globalEyesPlay(HttpSession session, @RequestParam(value="monitorId") Long monitorId,
			@RequestParam(value="type",required=false) String type, ModelMap map) {
		MonitorBO monitor = monitorService.getMonitorById(monitorId);
		map.addAttribute("monitor", monitor);
		map.addAttribute("PLATFORM_DOMAIN_ROOT", App.TOP.getDomain(session));
		map.addAttribute("RESOURCE_DOMAIN", App.RESOURCE.getDomain(session));
		String forward = "";
		if(monitor.getCompanyType()==null || monitor.getCompanyType()==1 || "".equals(monitor.getCompanyType())){
			forward = "/map/arcgis/standardmappage/globalEyesPlay.ftl";
		}else if(monitor.getCompanyType()==2){
			map.addAttribute("type", type);
			forward = "/map/arcgis/standardmappage/globalEyesOcxPlay.ftl";
		}else if (monitor.getCompanyType()==5) {//????????????IVMS
			//type=1:????????????????????????????????????????????????stopPreview
			//??????????????????????????????????????????????????????????????????stopPreview
			map.addAttribute("type", type);
			forward = "/map/arcgis/standardmappage/globalEyesIvmsPlay.ftl";
		}else if (monitor.getCompanyType()==6) {//????????????DVR
			map.addAttribute("type", type);
			forward = "/map/arcgis/standardmappage/globalEyesNetVideoActivePlay.ftl";
		}else if (monitor.getCompanyType()==7) {//??????????????????VPN
			map.addAttribute("type", type);
			forward = "/map/arcgis/standardmappage/globalEyesPlayVPN.ftl";
		}else{
			forward = "/map/arcgis/standardmappage/globalEyesZXProx.ftl";
		}
		
		return forward;
	}
	
	/**
	 * 2014-11-04 chenlf add
	 * ?????????????????????????????????
	 * @param session
	 * @param orgCode
	 * @param map
	 * @param standard
	 * @param eyesType
	 * @return
	 */
	@RequestMapping(value="/standardFkqyGlobalEyes")
	public String fkqyGlobalEyes(HttpSession session, ModelMap map,
			@RequestParam(value="geoString") String geoString,
			@RequestParam(value="mapt") String mapt) {
		map.addAttribute("geoString", geoString);
		map.addAttribute("mapt", mapt);
		return "/map/arcgis/standardmappage/standardFkqyGlobalEyes.ftl";
	}
	
	/**
	 * ???????????????---??????
	 * @param session
	 * @param page ??????
	 * @param rows ????????????
	 * @param orgCode ?????????????????????
	 * @param platformName ????????????
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/boxglobalEyesListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination boxglobalEyesListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="geoString") String geoString,@RequestParam(value="mapt", required=false) String mapt,
			@RequestParam(value="platformName", required=false) String platformName) {
		if(page<=0) page=1;
		if(StringUtils.isNotBlank(platformName)){ 
			platformName = platformName.trim();
		}else{
			platformName=null;
		}
		
//		String geoStr = geoString.replaceFirst("8 ", "");
//		geoStr = geoStr.replace(" ", ",");
		String geoStr = geoString.replace(" ", ",");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("geoString", geoStr);
		params.put("markerType", ResMarkedOM.MEGAEYES);
		params.put("catalog", ResMarkedOM.RESOURCES);
		params.put("mapType", mapt);
		params.put("platformName", platformName);
		//Pagination pagination = monitorService.listGlobalEyeConfig(orgCode, page, rows, null, null, platformName, null);
		cn.ffcs.common.EUDGPagination pagination = gisInfoService.findMarkedPagination(page, rows,params);
		return pagination;
	}
	
	///---????????? end----------------------------------------------
	
	///---?????????????????? start----------------------------------------------
	/**
	 * ???????????????????????????
	 * @param session
	 * @param gridId ??????ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/careRoadMembers")
	public String careRoadMembers(HttpSession session, ModelMap map
			, @RequestParam(value="gridId") Long gridId
			, @RequestParam(value="gridCode", required=false) String gridCode
			, @RequestParam(value="type", required=false) String type
			, @RequestParam(value="standard", required=false) String standard
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardCareRoadMembers.ftl";
	}
	///---?????????????????? end----------------------------------------------
	
	/**
	 * ???????????????????????????
	 * @param session
	 * @param gridId ??????ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toTeamMembers")
	public String toTeamMembers(HttpSession session, ModelMap map
			, @RequestParam(value="gridId") Long gridId
			, @RequestParam(value="bizType", required=false) String bizType
			, @RequestParam(value="gridCode", required=false) String gridCode
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("bizType", bizType);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardTeamMembers.ftl";
	}
	
	///---????????? start----------------------------------------------
	/**
	 * ???????????????????????????
	 * @param session
	 * @param gridId ??????ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/gridAdmin")
	public String gridAdmin(HttpSession session, ModelMap map, @RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridId", gridId);
		map.addAttribute("infoOrgCode", super.getDefaultInfoOrgCode(session));
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		List<BaseDataDict> dutyDC = baseDictionaryService.getDataDictListOfSinglestage(DictPcode.GRID_ADMIN_DUTY,
				userInfo.getOrgCode());
		String infoOrgCode = super.getDefaultInfoOrgCode(session);
		if(infoOrgCode.startsWith(ConstantValue.NANAN_FUNC_ORG_CODE)){
			map.addAttribute("isNanan", "isNanan");
			dutyDC = baseDictionaryService.getDataDictListOfSinglestage(DictPcode.GRID_ADMIN_USER_DUTY,
					userInfo.getOrgCode());
		}
		map.addAttribute("dutyDC", dutyDC);
		if(pageSize != null){
			map.addAttribute("pageSize", pageSize);
		}
		//????????????ANYCHAT????????????
		String isUserAnychat = this.funConfigurationService.turnCodeToValue(ConstantValue.IS_USER_ANYCHAT, "",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUserAnychat", isUserAnychat);
		
		//????????????MEDIASOUP ????????????
		String isUseMediasoup = this.funConfigurationService.turnCodeToValue(ConstantValue.IS_USE_MEDIASOUP, "",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUseMediasoup", isUseMediasoup);
		
		if(StringUtils.isNotBlank(isUseMediasoup)&&isUseMediasoup.equals("true")) {
			map.put("userId", userInfo.getUserId());
			return "/map/arcgis/standardmappage/standardGridAdmin_mediasoup.ftl";
		}
		if(StringUtils.isNotBlank(isUserAnychat)&&isUserAnychat.equals("true")) {
			map.put("userId", userInfo.getUserId());
			return "/map/arcgis/standardmappage/standardGridAdmin_anychat.ftl";
		}
		if (infoOrgCode.startsWith(ConstantValue.NANCHANG_FUNC_ORG_CODE)) {
			return "/map/arcgis/standardmappage/standardGridAdmin_3601.ftl";
		}
		return "/map/arcgis/standardmappage/standardGridAdmin.ftl";
	}
	
	/**
	 * ?????????????????????????????????????????????????????????
	 * @param session
	 * @param gridId ??????ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/gridAdminTaijiang")
	public String gridAdminTaijiang(HttpSession session, @RequestParam(value="gridId") Long gridId, ModelMap map) {
		map.addAttribute("gridId", gridId);
		return "/map/arcgis/standardmappage/taijiang/gridAdmin.ftl";
	}
	
	/**
	 * ????????????????????????
	 * @param session
	 * @param page 
	 * @param rows
	 * @param gridId
	 * @param name
	 * @param mobileTelephone
	 * @param duty
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/careRoadMembersListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination careRoadMembersListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId") Long gridId, @RequestParam(value="name", required=false) String name) {
		if(page<=0) page=1;
		if(name!=null) name = name.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		String infoOrgCode = mixedGridInfo.getInfoOrgCode();
		params.put("regionCode", infoOrgCode);
		params.put("name", name);
		cn.ffcs.common.EUDGPagination pagination = teamMembersService.findCareRoadMembersPagination(page, rows, params);
		return pagination;
	}
	@ResponseBody
	@RequestMapping(value="/teamMembersListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination villageWorkTeamMembersListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId") Long gridId,
			@RequestParam(value="bizType") String bizType, 
			@RequestParam(value="name", required=false) String name,
			@RequestParam(value="teamName", required=false) String teamName,
			@RequestParam(value="orgCode", required=false) String orgCode) {
		if(page<=0) page=1;
		if(name!=null) name = name.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		if(orgCode == null || "".equals(orgCode)) {
			MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
			orgCode = mixedGridInfo.getInfoOrgCode();
		}
		
		params.put("regionCode", orgCode);
		params.put("name", name);
		params.put("bizType", bizType);
		params.put("teamName", teamName);
		cn.ffcs.common.EUDGPagination pagination = teamMembersService.findTeamMembersPagination(page,rows, params);
		return pagination;
	}
	/**
	 * ????????????????????????
	 * 
	 * @param session
	 * @param page
	 * @param rows
	 * @param gridId
	 * @param name
	 * @param mobileTelephone
	 * @param duty
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/gridAdminListData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination gridAdminListData(HttpSession session, HttpServletRequest request,
											@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, @RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "mobileTelephone", required = false) String mobileTelephone,
			@RequestParam(value = "duty", required = false) String duty,
			@RequestParam(value = "userDutys", required = false) String userDutys,
			@RequestParam(value = "regionCtrl", required = false) String regionCtrl,
			@RequestParam(value = "onlineStatus", required = false) String onlineStatus,
			@RequestParam(value = "gridLevel", required = false) Integer gridLevel) {
		if (page <= 0)
			page = 1;
		if (name != null)
			name = name.trim();
		if (onlineStatus != null)
			onlineStatus = onlineStatus.trim();
		if (duty != null)
			duty = duty.trim();
		String trackStartDate = ParamUtils.getString(request,"trackStartDate","");
		String trackEndDate = ParamUtils.getString(request,"trackEndDate","");
		//??????????????????????????????????????????????????????????????????
		String trackType = "";
		if(!StringUtils.isEmpty(trackStartDate)){
			String pattern = "yyyy-MM-dd HH:mm:ss";
			String today = DateUtils.getToday(pattern);
			try {
				boolean startDateIsBeforeToday = DateUtils.compareMinDate(trackStartDate,pattern,today,pattern);//??????????????????????????????????????????
				boolean endDateIsBeforeToday = DateUtils.compareMinDate(trackEndDate,pattern,today,pattern);//??????????????????????????????????????????
				if(startDateIsBeforeToday && !endDateIsBeforeToday){//??????????????? ????????????????????????
					trackType = "withToday";
				}
			} catch (ParseException e) {e.printStackTrace();}
		}
		String identityCard = request.getParameter("identityCard");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startGridId", gridId);
		params.put("name", name);
		params.put("duty", duty);
		if(StringUtils.isNotBlank(userDutys)){
			params.put("isNanan","isNanan");
			params.put("userDutys", userDutys.split(","));
		}
		params.put("regionCtrl", regionCtrl);
		params.put("trackStartDate",trackStartDate );
		params.put("trackEndDate",trackEndDate );
		params.put("trackType",trackType );
		params.put("onlineStatus", onlineStatus);
		params.put("gridLevel", gridLevel);
		if (StringUtils.isNotBlank(mobileTelephone)){
			params.put("mobileTelephone", mobileTelephone);
		}
		if (StringUtils.isNotBlank(identityCard)){
			params.put("identityCard", identityCard);
		}
		String infoOrgCode = super.getDefaultInfoOrgCode(session);
		if (infoOrgCode.startsWith(ConstantValue.NANCHANG_FUNC_ORG_CODE)) { //????????????????????????
			params.put("gridLevel", 6);
		}
        //params.put("orderCondition","T3.LOCATE_TIME DESC NULLS LAST");
		cn.ffcs.common.EUDGPagination pagination = gridAdminService.findGridAdminPagination(page, rows, params);
		List<GridAdmin> list = (List<GridAdmin>) pagination.getRows();
		for(int i=0;i<list.size();i++){
			if(StringUtils.isNotBlank(list.get(i).getInfoOrgCode()) && list.get(i).getInfoOrgCode().length() > 6){//????????????????????????????????????????????????????????????????????????????????????X
				MixedGridInfo mixedGridInfo = mixedGridInfoService.getDefaultGridByOrgCode(list.get(i).getInfoOrgCode().substring(0,6));
				if(mixedGridInfo != null && StringUtils.isNotBlank(mixedGridInfo.getGridPath())){
					String gridPath = list.get(i).getGridPath().replace(mixedGridInfo.getGridPath(), "");
					list.get(i).setGridPath(gridPath);
				}
			}
		}
		pagination.setRows(list);
		return pagination;
	}
	
	/**
	 * ????????????????????????
	 * 
	 * @param session
	 * @param page
	 * @param rows
	 * @param gridId
	 * @param name
	 * @param mobileTelephone
	 * @param duty
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/gridAdminCountData", method = RequestMethod.POST)
	public Map<String,Object> gridAdminCountData(HttpSession session, HttpServletRequest request,
											@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, @RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "mobileTelephone", required = false) String mobileTelephone,
			@RequestParam(value = "duty", required = false) String duty,
			@RequestParam(value = "userDutys", required = false) String userDutys,
			@RequestParam(value = "regionCtrl", required = false) String regionCtrl,
			@RequestParam(value = "onlineStatus", required = false) String onlineStatus,
			@RequestParam(value = "gridLevel", required = false) Integer gridLevel) {
		if (page <= 0)
			page = 1;
		if (name != null)
			name = name.trim();
		if (onlineStatus != null)
			onlineStatus = onlineStatus.trim();
		if (mobileTelephone != null)
			mobileTelephone = mobileTelephone.trim();
		if (duty != null)
			duty = duty.trim();
		String trackStartDate = ParamUtils.getString(request,"trackStartDate","");
		String trackEndDate = ParamUtils.getString(request,"trackEndDate","");
		//??????????????????????????????????????????????????????????????????
		String trackType = "";
		if(!StringUtils.isEmpty(trackStartDate)){
			String pattern = "yyyy-MM-dd HH:mm:ss";
			String today = DateUtils.getToday(pattern);
			try {
				boolean startDateIsBeforeToday = DateUtils.compareMinDate(trackStartDate,pattern,today,pattern);//??????????????????????????????????????????
				boolean endDateIsBeforeToday = DateUtils.compareMinDate(trackEndDate,pattern,today,pattern);//??????????????????????????????????????????
				if(startDateIsBeforeToday && !endDateIsBeforeToday){//??????????????? ????????????????????????
					trackType = "withToday";
				}
			} catch (ParseException e) {e.printStackTrace();}
		}
		String identityCard = request.getParameter("identityCard");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startGridId", gridId);
		params.put("name", name);
		params.put("duty", duty);
		params.put("mobileTelephone", mobileTelephone);
		params.put("regionCtrl", regionCtrl);
		params.put("trackStartDate",trackStartDate );
		params.put("trackEndDate",trackEndDate );
		params.put("trackType",trackType );
		params.put("onlineStatus", onlineStatus);
		params.put("gridLevel", gridLevel);
		if (StringUtils.isNotBlank(identityCard)){
			params.put("identityCard", identityCard);
		}
		if (StringUtils.isNotBlank(userDutys)){
			params.put("isNanan", "isNanan");
			params.put("userDutys", userDutys.split(","));
		}
		String infoOrgCode = super.getDefaultInfoOrgCode(session);
		if (infoOrgCode.startsWith(ConstantValue.NANCHANG_FUNC_ORG_CODE)) { //????????????????????????
			params.put("gridLevel", 6);
		}
        //params.put("orderCondition","T3.LOCATE_TIME DESC NULLS LAST");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = gridAdminService.findGridAdminCount(params);
		
		
		return resultMap;
	}
	
	/**
	 * 2014-06-24 chenlf add
	 * ??????????????????????????????
	 * @param session
	 * @param ids ?????????ids
	 * @param mapt ????????????
	 * @param showType ??????????????????1???????????????2?????????????????????
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfGridAdmin")
	public List<ArcgisInfoOfPublic> getArcgisGridAdminLocateDataList(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		String infoOrgCode = super.getDefaultInfoOrgCode(session);

		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		boolean getByUserNameFlag = false;
		if(StringUtils.isNotBlank(infoOrgCode) && infoOrgCode.startsWith(ConstantValue.JIANGYIN_FUNC_ORG_CODE)){
			getByUserNameFlag = true;
		}else{
			getByUserNameFlag = false;
		}
		list = this.arcgisDataOfLocalService.getArcgisGridAdminLocateDataListByIdsUserName(ids,mapt, getByUserNameFlag);
		for (ArcgisInfoOfPublic arcgisInfoOfPublic : list) {
			GridAdmin gridAdmin = gridAdminService.findGridAdminById(arcgisInfoOfPublic.getId());
			String gridAdminName = "";
			if (gridAdmin != null && StringUtils.isNotBlank(gridAdmin.getPartyName())) {
				gridAdminName = gridAdmin.getPartyName();
			}
			if (arcgisInfoOfPublic.getHandleDate() != null) {
				gridAdminName = gridAdminName + "????????????????????????" + DateUtils.formatDate(arcgisInfoOfPublic.getHandleDate(), "yyyy-MM-dd HH:mm:ss") + "???";
			}
			arcgisInfoOfPublic.setName(gridAdminName);
			String elementsCollectionStrType = formatElementsCollectionStr(elementsCollectionStr, "isOnline", gridAdmin.getIsOnline());
			arcgisInfoOfPublic.setBizType(gridAdmin.getDuty());
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStrType);
		}
		if (StringUtils.isNotBlank(infoOrgCode) && infoOrgCode.startsWith(ConstantValue.HAICANG_FUNC_ORG_CODE)) {
			WGSUtils.Wgs84ToXm92List(list);
		}
		return list;
	}
	
	/**
	 * ????????????????????????????????????
	 * ?????????????????????????????????????????????????????????
	 * @param session
	 * @param request
	 * @param ids
	 * @param order
	 * @param name
	 * @param mapt
	 * @param showType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisTeamMembersLocateDataList")
	public List<ArcgisInfoOfPublic> getArcgisVillageWorkTeamMembersLocateDataList(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "showType") String showType,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisCareRoadMemberLocateDataListByIds(ids,mapt);
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	/**
	 * ????????????????????????????????????
	 * @param session
	 * @param request
	 * @param ids
	 * @param order
	 * @param name
	 * @param mapt
	 * @param showType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisCareRoadMembersLocateDataList")
	public List<ArcgisInfoOfPublic> getArcgisCareRoadMembersLocateDataList(HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "showType") String showType,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisCareRoadMemberLocateDataListByIds(ids,mapt);
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	/**
	 * ??????????????? ??????
	 * @param session
	 * @param gridAdminId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/gridAdminDetail")
	public String gridAdminDetail(HttpSession session, ModelMap map
			, @RequestParam(value="gridAdminId", required=false) Long gridAdminId
			, @RequestParam(value="wid", required=false) Long wid
			, @RequestParam(value = "isCross", required=false) String isCross
			, @RequestParam(value = "queryDay", required=false) String queryDay
			, @RequestParam(value = "startTime", required=false) String startTime
			, @RequestParam(value = "endTime", required=false) String endTime
			, @RequestParam(value = "play", required=false) String play) {
		if(wid != null && wid !=0l){
			gridAdminId = wid;
		}
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String infoOrgCode = super.getDefaultInfoOrgCode(session);
		if (infoOrgCode.startsWith(ConstantValue.NANCHANG_FUNC_ORG_CODE)) { //??????????????????????????????????????????
			ca.add(Calendar.DATE,-1);
			if (StringUtils.isBlank(startTime)) {
				queryDay = sdf.format(ca.getTime()).substring(0,10);
				startTime = "00:00";
			}
			if (StringUtils.isBlank(endTime)) endTime = "23:59";
		} else {
			if (StringUtils.isBlank(queryDay)) {// ??????????????????
				queryDay = sdf.format(ca.getTime()).substring(0,10);
			}
			if (StringUtils.isBlank(startTime)) {// ????????????7???
				startTime = "07:00";
			}
			if (StringUtils.isBlank(endTime)) {// ????????????21???
				endTime = "21:00";
			}
		}
		map.addAttribute("queryDay", queryDay);
		map.addAttribute("startTime", startTime);
		map.addAttribute("endTime", endTime);
		map.addAttribute("currentDate", sdf.format(new Date()));
		if(StringUtils.isNotBlank(play)){//????????????????????????
			map.addAttribute("play", play);
		}
		//????????? ???????????????????????????????????????
		GridAdmin gridAdmin = null;
		if(infoOrgCode.startsWith(ConstantValue.DAFENG_ORG_CODE)){
			map.addAttribute("isDaFengFlag","true");
			gridAdmin = gridAdminService.findGridAdminById(gridAdminId);
		}else {
			gridAdmin = gridAdminService.findGridAdminsByAdminId(gridAdminId);
		}

		if(gridAdmin != null && gridAdmin.getUserId() != null){
			Map<String,Object>resMap;
			if(infoOrgCode.startsWith(ConstantValue.JIANGXI_FUNC_ORG_CODE)){
				Map<String, Object> param = new HashMap<>();
				param.put("infoOrgCode",infoOrgCode);
				param.put("userId",gridAdmin.getGridAdminId());
				resMap = gridAdminService.getPatrolNumsByInspection(param);
			}else {
				resMap = gridAdminService.getPatrolNumsByUserId2(gridAdmin.getGridAdminId());
			}

			map.addAttribute("patrolNums", resMap.get("PATROLNUMS"));
			map.addAttribute("patrolHours", resMap.get("PATROLHOURS"));
			List<UserExBO> userExBOList = userManageService.findUserExBoForUserId(gridAdmin.getUserId());
			if (userExBOList != null && userExBOList.size() > 0){
				for (UserExBO u: userExBOList) {
					if (u.getOrgName() != null && u.getOrgName().equals(gridAdmin.getGridName())){
						map.addAttribute("orgId", u.getSocialOrgId());
						break;
					}
				}
			}
		}
		map.addAttribute("RESOURSE_SERVER_PATH",  App.IMG.getDomain(session));
		map.addAttribute("tel_show", 1); //??????
		map.addAttribute("gota_show", 0); //gota
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		//?????????????????????
		/*Object[] bean = new Object[3];
		String[] arry = {"001","002","003"};
		List<GridAdmin> list = gridAdminService.getGridAdminListByGridId(gridAdmin.getGridId());
		for (int i = 0; i < arry.length; i++) {			
			for (int j = 0; j < list.size(); j++) {
				if(list.get(j).getDuty().equals(arry[i])){
					bean[i] = list.get(j);
					break;
				}
			}
		}
		for (int i = 0; i < bean.length; i++) {
			if(bean[i]!=null && gridAdmin.getDuty().equals(((GridAdmin)bean[i]).getDuty())){
				bean[i] = gridAdmin;
				break;
			}
		}
		
	*/	String hjOrgCodeStr = ReadProperties.javaLoadProperties("hujiao.org_code", session, "global.properties");
		int isHjCenter = 0;
		if(!StringUtils.isEmpty(hjOrgCodeStr)){
			String[] hjOrgCodes = hjOrgCodeStr.split(",");
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			String defaultInfoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
			for(String hjOrgCode : hjOrgCodes){
				if(defaultInfoOrgCode.startsWith(hjOrgCode)){
					isHjCenter = 1;
					break;
				}
			}
		}
		//????????????????????????????????????????????????
		map.addAttribute("callcenter_show", isHjCenter);//????????????
	/*	map.addAttribute("gridAdmin", bean[0]==null?null:(GridAdmin)bean[0]);
		map.addAttribute("gridAdmin1", bean[1]==null?null:(GridAdmin)bean[1]);
		map.addAttribute("gridAdmin2", bean[2]==null?null:(GridAdmin)bean[2]);*/
		map.addAttribute("gridAdmin", gridAdmin);
		map.addAttribute("isCross", isCross);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//????????????????????????
		String IS_USER_MMP = this.funConfigurationService.turnCodeToValue(
				ConstantValue.IS_USER_MMP, null,
				IFunConfigurationService.CFG_TYPE_FACT_VAL,
				userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUserMmp", IS_USER_MMP);
		//???????????????????????????
		String IS_USER_YYHHJ = this.funConfigurationService.turnCodeToValue(
				ConstantValue.IS_USER_YYHHJ, null,
				IFunConfigurationService.CFG_TYPE_FACT_VAL,
				userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isBlank(IS_USER_YYHHJ)){
			IS_USER_YYHHJ = "true";//???????????????
		}
		map.addAttribute("isUserYyhhj", IS_USER_YYHHJ);
		//????????????????????????
		String IS_USER_FSDX = this.funConfigurationService.turnCodeToValue(
				ConstantValue.IS_USER_FSDX, null,
				IFunConfigurationService.CFG_TYPE_FACT_VAL,
				userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isBlank(IS_USER_FSDX)){
			IS_USER_FSDX = "true";//???????????????
		}
		map.addAttribute("isUserFsdx", IS_USER_FSDX);
		//??????????????????????????????
		String GRID_ADMIN_WITH_WORK_FLAG = this.funConfigurationService.turnCodeToValue(
				ConstantValue.GRID_ADMIN_WITH_WORK_FLAG, null, IFunConfigurationService.CFG_TYPE_FACT_VAL, infoOrgCode,
				IFunConfigurationService.CFG_ORG_TYPE_0);
		if ("true".equals(GRID_ADMIN_WITH_WORK_FLAG)) {
			map.addAttribute("gridAdminWithWorkFlag", "true");
			map.addAttribute("OA_DOMAIN", App.OA.getDomain(session));
		}
		
		
		//????????????ANYCHAT????????????
		String isUserAnychat = this.funConfigurationService.turnCodeToValue(ConstantValue.IS_USER_ANYCHAT, "",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUserAnychat", isUserAnychat);
		//????????????MEDIASOUP ????????????
		String isUseMediasoup = this.funConfigurationService.turnCodeToValue(ConstantValue.IS_USE_MEDIASOUP, "",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUseMediasoup", isUseMediasoup);
		
		if(userInfo != null && gridAdmin != null && StringUtils.isNotBlank(gridAdmin.getUserName())){
			map.addAttribute("userName",gridAdmin.getUserName());//????????????????????????
		}
		if(gridAdmin != null && StringUtils.isNotBlank(gridAdmin.getInfoOrgCode())){
			if(gridAdmin.getInfoOrgCode().startsWith(ConstantValue.JIANGYIN_FUNC_ORG_CODE)){
				map.addAttribute("jsJiangYinFlag","true");
			}
		}
		
		map.addAttribute("userId",userInfo.getUserId());
		map.addAttribute("partyName",userInfo.getPartyName());
		return "/map/arcgis/standardmappage/gridAdminDetail.ftl";
	}


	/**
	 * ????????????????????????
	 * @param session
	 * @param map
	 * @param gridAdminId
	 * @return
	 */
	@RequestMapping(value="/xcyGridAdminVideo")
	public String gridAdminDetail(HttpSession session, ModelMap map
			, @RequestParam(value="gridAdminId", required=false) Long gridAdminId) {
		UserInfo u = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		GridAdmin gridAdmin = gridAdminService.findGridAdminById(gridAdminId, u.getOrgCode());
		String zfyNo = "8170502441310013745";
		if(gridAdmin != null && StringUtils.isNotBlank(gridAdmin.getRemark())){
			zfyNo = gridAdmin.getRemark().trim();
		}
		System.out.println("zfyNO:"+zfyNo);
		map.addAttribute("zfyNo", zfyNo);
		return "/map/arcgis/standardmappage/xcyGridAdminVideo.ftl";
	}

	/**
	 * ????????????
	 * @param session
	 * @param map
	 * @param gridAdminId
	 * @return
	 */
	@RequestMapping(value="/gridAdminWorkDetail")
	public String gridAdminWorkDetail(HttpSession session, ModelMap map,
									  @RequestParam(value="gridAdminUserId", required=false) Long gridAdminUserId,
									  @RequestParam(value="gridOrgCode", required=false) Long gridOrgCode,
									  @RequestParam(value="gridAdminId", required=false) Long gridAdminId,
									  @RequestParam(value="orgId", required=false) Long orgId) {
		UserInfo u = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridAdminUserId", gridAdminUserId);
		map.addAttribute("gridAdminId", gridAdminId);
		//??????????????????
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(new Date());
		map.addAttribute("date", date);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
        map.addAttribute("RS_URL", App.RS.getDomain(session));
		map.addAttribute("orgCode", gridOrgCode == null ? u.getOrgCode() : gridOrgCode);
		map.addAttribute("orgId", orgId == null ? u.getOrgId() : orgId);
		// ??????
		if (StringUtils.startsWith(u.getOrgCode(), ConstantValue.JIANHU_FUNC_ORG_CODE)) {
			map.addAttribute("region",ConstantValue.JIANHU_FUNC_ORG_CODE);
		}
		if (StringUtils.startsWith(u.getOrgCode(), ConstantValue.NANAN_FUNC_ORG_CODE)) {
			return "/map/arcgis/standardmappage/gridAdminWorkDetail_nanAn.ftl";
		}
		return "/map/arcgis/standardmappage/gridAdminWorkDetail.ftl";
	}
	
	/**
	 * ????????????????????????
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/careRoadMemberDetail")
	public String careRoadMemberDetail(HttpSession session, @RequestParam(value="memberId") Long memberId, 
			@RequestParam(value="imsi", required=false) Long imsi,
			@RequestParam(value = "mapt", required=false) Integer mapt,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.HOUR, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String startTime = sdf.format(ca.getTime());
		String endTime = sdf.format(new Date());
		map.addAttribute("startTime", startTime);
		map.addAttribute("endTime", endTime);
		map.addAttribute("RESOURSE_SERVER_PATH",  App.IMG.getDomain(session));
		map.addAttribute("tel_show", 1); //??????
		map.addAttribute("gota_show", 0); //gota
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		String hjOrgCodeStr = ReadProperties.javaLoadProperties("hujiao.org_code", session, "global.properties");
		 int isHjCenter = 0;
		 if(!StringUtils.isEmpty(hjOrgCodeStr)){
			 String[] hjOrgCodes = hjOrgCodeStr.split(",");
			 Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			 String defaultInfoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
			 for(String hjOrgCode : hjOrgCodes){
				 if(defaultInfoOrgCode.startsWith(hjOrgCode)){
					 isHjCenter = 1;
					 break;
				 }
			 }
		 }
		map.addAttribute("callcenter_show", isHjCenter);//????????????
		TeamMembers teamMember = teamMembersService.getTeamMembersById(memberId, userInfo.getOrgCode());
		PrvetionTeam prvetionTeam = careRoadOrgService.findCareRoadOrgByTeamId(teamMember.getTeamId());
		String IMSI = teamMember.getImsi();
		/*if (imsi == null && "".equals(imsi)) {
			List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisCareRoadMemberLocateDataListByIds(memberId+"",mapt);
			
			if (list != null && list.size() > 0) {
				imsi = list.get(0).getId();
			}
		}*/
		
		Long gridId = prvetionTeam.getGridId();
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		String infoOrgCode=gridInfo.getInfoOrgCode();
		
		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
		}
		
		map.addAttribute("teamMember", teamMember);
		map.addAttribute("prvetionTeam", prvetionTeam);
		map.addAttribute("gridNames", gridNames);
		map.addAttribute("memberId", memberId);
		map.addAttribute("imsi", IMSI);
		return "/map/arcgis/standardmappage/careRoadMemberDetail.ftl";
	}
	@RequestMapping(value="/teamMemberDetail")
	public String teamMemberDetail(HttpSession session, @RequestParam(value="memberId") Long memberId, 
			@RequestParam(value="imsi", required=false) Long imsi,
			@RequestParam(value = "mapt", required=false) Integer mapt,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.HOUR, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String startTime = sdf.format(ca.getTime());
		String endTime = sdf.format(new Date());
		map.addAttribute("startTime", startTime);
		map.addAttribute("endTime", endTime);
		map.addAttribute("RESOURSE_SERVER_PATH",  App.IMG.getDomain(session));
		map.addAttribute("tel_show", 1); //??????
		map.addAttribute("gota_show", 0); //gota
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		String hjOrgCodeStr = ReadProperties.javaLoadProperties("hujiao.org_code", session, "global.properties");
		 int isHjCenter = 0;
		 if(!StringUtils.isEmpty(hjOrgCodeStr)){
			 String[] hjOrgCodes = hjOrgCodeStr.split(",");
			 Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			 String defaultInfoOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
			 for(String hjOrgCode : hjOrgCodes){
				 if(defaultInfoOrgCode.startsWith(hjOrgCode)){
					 isHjCenter = 1;
					 break;
				 }
			 }
		 }
		map.addAttribute("callcenter_show", isHjCenter);//????????????
		TeamMembers teamMember = teamMembersService.getTeamMembersById(memberId, userInfo.getOrgCode());
		cn.ffcs.gmis.mybatis.domain.prvetionTeam.PrvetionTeam prvetionTeam = prvetionTeamServiceGmis
				.findPrvetionTeamById(teamMember.getTeamId(),
						userInfo.getOrgCode());
		
		Long gridId = prvetionTeam.getGridId();
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		String infoOrgCode=gridInfo.getInfoOrgCode();
		
		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
		}
		map.addAttribute("teamMember", teamMember);
		map.addAttribute("prvetionTeam", prvetionTeam);
		map.addAttribute("gridNames", gridNames);
		map.addAttribute("memberId", memberId);
		map.addAttribute("imsi", teamMember.getImsi());
		return "/map/arcgis/standardmappage/teamMemberDetail.ftl";
	}
	/**
	 * ??????????????? ????????????
	 * @param session
	 * @param gridAdminId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/sendMessageToGridAdmin")
	public String sendMessageToGridAdmin(HttpSession session, @RequestParam(value="gridAdminId") Long gridAdminId, ModelMap map) {
		GridAdmin gridAdmin = gridAdminService.findGridAdminById(gridAdminId);
		map.addAttribute("gridAdmin", gridAdmin);
		return "/map/arcgis/standardmappage/sendMessageToGridAdmin.ftl";
	}
	
	///---????????? end----------------------------------------------
	
	///---???????????? start----------------------------------------------
	@RequestMapping(value = "/plcIndex")
	public String plcIndex(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardPlc.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/plcListData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination plcListData(HttpSession session, @RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, @RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "gridName", required = false) String gridName) {
		if (page <= 0) page = 1;
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(gridName)) {
			params.put("gridName", gridName.trim());
		}
		if (gridId == null || gridId == 0L) {
			params.put("gridCode", this.getDefaultInfoOrgCode(session));
		} else {
			MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
			params.put("gridCode", mixedGridInfo.getInfoOrgCode());
		}
		cn.ffcs.common.EUDGPagination pagination = plcService.findByPagination(page, rows, params);
		return pagination;
	}
	
	@RequestMapping(value = "/plcDetail")
	public String plcDetail(HttpSession session,
			@RequestParam(value = "id") Long id, ModelMap map) throws Exception {
		if (id == null || id == 0L) throw new Exception("???????????????id??????");
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(id, false);
		Map<String, Object> statMap = agricultureService.getPoorStatisticsI(gridInfo.getInfoOrgCode());
		Map<String, Object> statMap2 = populationService.getPopulationNum(gridInfo.getInfoOrgCode());
		statMap.putAll(statMap2);
		map.addAttribute("statMap", statMap);
		return "/map/arcgis/standardmappage/plcDetail.ftl";
	}
	
	///---???????????? end----------------------------------------------
	
	/**************************************???????????? begin***********************************/
	/**
	 * ??????????????????????????????
	 * @param session
	 * @param gridId
	 * @param elementsCollectionStr
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfTargetRef")
	public String toArcgisDataListOfTargetRef(HttpSession session, 
			@RequestParam(value="gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			@RequestParam(value="pattern", required = false, defaultValue = "1") int pattern,
			ModelMap map) {
		
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		if(gridInfo != null) {
			map.addAttribute("infoOrgCode", gridInfo.getInfoOrgCode());
		}
		
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("gridId", gridId);
		
		String forwardUrl = "/map/arcgis/standardmappage/equipment/idssControl/standardApplyInfo.ftl";
		
		if(pattern == 0) {
			forwardUrl = "/map/arcgis/standardmappage/equipment/idssControl/standardTargetRef.ftl";
		}
			
		return forwardUrl;
	}
	
	/**
	 * ??????????????????????????????
	 * @param session
	 * @param page
	 * @param rows
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listTargetRefData", method=RequestMethod.POST)
	public EUDGPagination listTargetRefData(HttpSession session, 
			@RequestParam(value="page") int page, 
			@RequestParam(value="rows") int rows,
			@RequestParam(value="pattern", required = false, defaultValue = "1") int pattern,
			@RequestParam Map<String, Object> params) {
		cn.ffcs.system.publicUtil.EUDGPagination targetRefPagination = new cn.ffcs.system.publicUtil.EUDGPagination();
		
		if(pattern == 0) {
			targetRefPagination = idssControlService.findIdssControlTargetRef(page, rows, params);
		} else {
			targetRefPagination = idssControlService.findIdssControlApplyInfo(page, rows, params);
		}
		
		EUDGPagination pagination = new EUDGPagination(targetRefPagination.getTotal(), targetRefPagination.getRows());
		
		return pagination;
	}
	
	/**
	 * ????????????????????????????????????
	 * @param session
	 * @param map
	 * @param gridId
	 * @param elementsCollectionStr
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/toTargetRecordList")
	public String toIdssControlOfTarget(HttpSession session, ModelMap map,
			@RequestParam(value="gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			@RequestParam Map<String, Object> params) {
		
		List<ControlTargetRecord> targetRecordList = idssControlService.findIdssControlTargetRecord(params);
		
		map.addAttribute("targetRecordList", targetRecordList);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("gridId", gridId);
		
		if(CommonFunctions.isNotBlank(params, "controlTargetId")) {
			map.addAttribute("controlTargetId", params.get("controlTargetId"));
		}
		
		return "/map/arcgis/standardmappage/equipment/idssControl/list_targetRecord.ftl";
	}
	
	private Object getCiRsInfo(String idCard){
//		idCard = "350582198909022518";
		CiRsCriteria criteria = new CiRsCriteria();
		criteria.setIdCard(idCard);
		cn.ffcs.resident.bo.Pagination page = ciRsService.findPage(criteria, 1, 1);
		if(page.getList() != null && page.getList().size() > 0){
			return page.getList().get(0);
		}
		return null;
	}
	
	@RequestMapping(value="/getIdssControlOfTargetDetailOnMap")
	public String getIdssControlOfTargetDetailOnMap(HttpSession session, @RequestParam(value="recordId") Long recordId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//????????????
		ControlTargetRecord controlTargetRecord = this.idssControlService.findIdssControlTargetRecordById(recordId);
		map.addAttribute("controlTargetRecord", controlTargetRecord);
		//????????????
		if(StringUtils.isNotBlank(controlTargetRecord.getControlTargetType())){
			if(controlTargetRecord.getControlTargetType().equals("001")){//?????????
				map.addAttribute("ciRsTop", getCiRsInfo(controlTargetRecord.getControlTargetObject()));
//				map.addAttribute("ciRsTop", getCiRsInfo(null));
			}
			if(controlTargetRecord.getControlTargetType().equals("002")){//??????
				CiRsCar ciRsCar = ciRsCarService.getCiRsCar(controlTargetRecord.getControlTargetObject());
//				CiRsCar ciRsCar = ciRsCarService.getCiRsCar("???E8522R");
				map.addAttribute("ciRsTop", getCiRsInfo(ciRsCar.getIdentityCard()));
			}
			if(controlTargetRecord.getControlTargetType().equals("003")){//?????????
				CiRsCriteria criteria = new CiRsCriteria();
//				criteria.setMobile(controlTargetRecord.getControlTargetObject());
				criteria.setMobile("13333333333");
				cn.ffcs.resident.bo.Pagination page = ciRsService.findPage(criteria, 1, 1);
				if(page.getList() != null && page.getList().size() > 0)
					map.addAttribute("ciRsTop", page.getList().get(0));
			}
			if(map.get("ciRsTop") != null){
				CiRsTop cirs = (CiRsTop)map.get("ciRsTop");
				Long ciRsId = cirs.getCiRsId();
				//??????????????????
				Map<String, Object> tagFlag = ciRsService.getRsTagFlag(ciRsId);// ????????????
				
				//??????????????????
				String ENABLE_GB = this.funConfigurationService.turnCodeToValue(
						ConstantValue.ENABLE_GB, null,
						IFunConfigurationService.CFG_TYPE_FACT_VAL,
						userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
				List tagList = new ArrayList();
				Set set = tagFlag.keySet();
				Iterator it = set.iterator();
				while (it.hasNext()) {
					String s = (String) it.next();
					if (s.equals("hbcare")) {
						continue;
					}
					if (tagFlag.get(s).toString().equals("1")) {// 1??????????????????0????????????
						String typeVal = getTypeVlue(s);
						if (StringUtils.isNotBlank(typeVal)) {
							String[] tag = { typeVal, getValue(s) };
							tagList.add(tag);
						}
					}
				}
				map.addAttribute("tag", tagList);
			}
		}
		//????????????
		List<GridAdmin> gridAdmins = new ArrayList<GridAdmin>();
		String infoOrgCode = controlTargetRecord.getInfoOrgCode();
		MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(infoOrgCode);
		if(null != gridInfo){
			gridAdmins = gridAdminService.getGridAdminListByGridId(gridInfo.getGridId(), infoOrgCode);
		}
		map.addAttribute("gridAdmins", gridAdmins);
		
		map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("POPULATION_URL", App.RS.getDomain(session));
		map.addAttribute("gridId", this.getDefaultGridInfo(session).get(KEY_START_GRID_ID));
		return "/map/arcgis/standardmappage/equipment/idssControl/detait_targetRecord.ftl";
	}
	
	@RequestMapping(value="/showFamily")
	public String showFamily(HttpSession session, 
			@RequestParam(value="ciRsId") Long recordId, ModelMap map) {

		map.addAttribute("ciRsId", recordId);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		return "/map/arcgis/standardmappage/list_family_member.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value="/familyListData", method=RequestMethod.POST)
	public EUDGPagination familyListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="ciRsId") Long ciRsId) {
		if(page<=0) page=1;
		
		CiRsTop cirs = ciRsService.findCiRsTopsByCiRsId(ciRsId);
		EUDGPagination e = new EUDGPagination();
		//????????????
		if(StringUtils.isNotBlank(cirs.getFamilySn())){
			cn.ffcs.resident.bo.Pagination pagination = ciRsService.findPageByFamilySn(cirs.getFamilySn(), cirs.getOrgCode(), rows, page);
			if(pagination!=null){
				e.setTotal(pagination.getTotalCount());
				e.setRows(pagination.getList());
			}
		}
		return e;
	}
	
	/**************************************???????????? end*************************************/
	

	public String getTypeVlue(String key) {
		Map map = new HashMap();
		map.put("party", "1");
		map.put("retire", "2");
		map.put("old", "3");
		map.put("hbcare", "3");
		map.put("army", "4");
		map.put("unemployed", "5");
		map.put("poor", "6");
		map.put("deformity", "7");
		map.put("mentalIllnessRecord", "8");
		map.put("dangerousGoodsRecord", "9");
		map.put("petitionRecord", "10");
		map.put("drugRecord", "11");
		map.put("cultMemberRecord", "12");
		map.put("correctionalRecord", "13");
		map.put("releasedRecord", "14");
		map.put("volunteer", "15");
		map.put("taibao", "16");
		map.put("motoMemberRecord", "17");
		map.put("youthPerson", "19");
		map.put("cleaner", "20");
		map.put("aidsPerson", "21");
		return map.get(key) != null ? map.get(key).toString() : "";
	}
	
	public String getValue(String key) {
		Map map = new HashMap();
		map.put("party", "??????");
		map.put("retire", "??????");
		map.put("old", "?????????");
		map.put("hbcare", "????????????");
		map.put("army", "?????????");
		map.put("unemployed", "??????");
		map.put("poor", "??????");
		map.put("deformity", "??????");
		map.put("mentalIllnessRecord", "????????????");
		map.put("dangerousGoodsRecord", "?????????");
		map.put("petitionRecord", "??????");
		map.put("drugRecord", "??????");
		map.put("cultMemberRecord", "????????????");
		map.put("motoMemberRecord", "????????????");
		map.put("correctionalRecord", "??????");
		map.put("releasedRecord", "??????");
		map.put("volunteer", "?????????");
		map.put("taibao", "??????");
		map.put("youthPerson", "???????????????");
		map.put("cleaner", "??????????????????");
		map.put("aidsPerson", "???????????????");
		return map.get(key) != null ? map.get(key).toString() : "";
	}


	private String formatElementsCollectionStr(String elementsCollectionStr, String modelType, String isOnline){
		String elementsCollectionStrType = elementsCollectionStr;
		if (StringUtils.isNotBlank(modelType)) {
			if ("isOnline".equals(modelType) && "0".equals(isOnline)) {
				elementsCollectionStrType = elementsCollectionStrType
						.replaceAll("/images/map/gisv0/map_config/unselected/situation_gridAdmin_online.png",
								"/images/map/gisv0/map_config/unselected/situation_gridAdmin_offline.png");
			}
		}
		return elementsCollectionStrType;
	}
}
