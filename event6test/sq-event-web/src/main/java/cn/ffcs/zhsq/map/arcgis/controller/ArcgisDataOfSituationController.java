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
 * arcgis "情" 定位数据加载控制器
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
	protected IGisInfoService gisInfoService; //gis地图相关服务 
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
	private IRoutineExaminationService routineExaminationService; //安全隐患
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;  //功能域组织服务
	@Autowired
	private IPlcService plcService;
	@Autowired
	private AgricultureService agricultureService;
	@Autowired
	private PopulationService populationService;
	@Autowired
	private IIdssControlService idssControlService;//布控任务
	@Autowired
	private ICiRsCarService ciRsCarService;//车辆
	@Autowired
    private IResInfoService resInfoService;//污染监控、血库信息统计、通讯覆盖
	@Autowired
	private UserManageOutService userManageService;
	
	/*-------------------------------------------安全隐患开始-------------------------------------------*/
	/**
	 * 安全隐患
	 * @param session
	 * @param orgCode 信息域组织编码
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
	 * @time 2015年4月9日 add
	 * @Description: 安全隐患信息列表
	 * @param session
	 * @param request
	 * @param map
	 * @param page
	 * @param rows
	 * @param infoOrgCode
	 * @param corplaceName 检查场所名称（模糊查询）
	 * @param checkContent 检查情况说明（模糊查询）
	 * @param siteType 检查对象类型（1、企业；2、场所；3、出租屋）
	 * @param beginCheckDate 检查开始时间
	 * @param checkDate 检查结束时间
	 * @return 安全隐患列表
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
		
		//-- 隐患等级		
		if(! org.apache.commons.lang.StringUtils.isBlank(safetyCheckCon.getRiskGrade())) {
			safetyCheckCon.setRiskGradeLabel(dictionaryService.getTableColumnText(ConstantValue.TABLE_NEW_SAFETY_CHECK_INFO, ConstantValue.COLUMN_DISPOSE_STATUS, safetyCheckCon.getRiskGrade()));
		}
		//-- 复查情况		
		if(!org.apache.commons.lang.StringUtils.isBlank(safetyCheckCon.getReviewCase())) {
			safetyCheckCon.setReviewCaseLabel(dictionaryService.getTableColumnText(ConstantValue.TABLE_NEW_SAFETY_CHECK_INFO, ConstantValue.COLUMN_DISPOSE_STATUS, safetyCheckCon.getReviewCase()));
		}
		
		return "/map/arcgis/standardmappage/risk/viewRisk.ftl";
	}
	
	/**
	 * @author huangmw
	 * @time 2015年4月9日 add
	 * @Description: 安全隐患列表地图定位
	 * @param session
	 * @param map
	 * @param res
	 * @param ids checkId集合
	 * @param mapt 地图类型
	 * @return 返回安全隐患地图定位信息数据
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
	
	/*-------------------------------------------安全隐患结束-------------------------------------------*/
	
	///---全球眼 start----------------------------------------------
	/**
	 * 全球眼数据页
	 * @param session
	 * @param orgCode 信息域组织编码
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
	 * 全球眼数据页-万宁污染检测、血库信息统计、通讯覆盖
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
	 * 全球眼数据页-重庆万州牌楼街道
	 * @param session
	 * @param orgCode 信息域组织编码
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
	 * 视屏监控
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
	 * 水文监测点位
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
	 * 莆田地图首页页面视频调度
	 * @param session
	 * @param orgCode 信息域组织编码
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
	 * 台江地图图层分类管理全球眼
	 */
	@RequestMapping(value="/globalEyesTaijiang")
	public String globalEyesTaijiang(HttpSession session,
			@RequestParam(value = "orgCode") String orgCode, ModelMap map) {
		map.addAttribute("orgCode", orgCode);
		return "/map/arcgis/standardmappage/taijiang/globalEyes.ftl";
	}

	/**
	 * 全球眼数据
	 * @param session
	 * @param page 页码
	 * @param rows 每页条数
	 * @param orgCode 信息域组织编码
	 * @param platformName 平台名称
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
			if(orgCode.length() >= 6){//南平区县及以下的层级都可以看到本区县所有的监控资源
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
	 * 污染监控、血库信息统计、通讯覆盖数据
	 * @param session
	 * @param page 页码
	 * @param rows 每页条数
	 * @param resTypeId 类型id
	 * @param startGridId 网格id
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
	 * 视频监控
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
	 * 水文监测点位
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
	 * 获取全球眼的定位信息
	 * @param session
	 * @param ids 全球眼ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
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
	 * 获取全球眼的定位信息
	 * @param session
	 * @param ids 全球眼ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
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
	 * 获取污染监测点、血库信息统计、通讯覆盖的定位信息
	 * @param session
	 * @param ids 全球眼ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
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
		if("02070403".equals(typeCode) && list != null && list.size() > 0) {//血库缺血替换图标
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
	 * 根据id 和mapt获取水文监测点位信息
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
	 * 全球眼播放
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
		}else if (monitor.getCompanyType()==5) {//海康威视IVMS
			//type=1:数据列表查看全球眼时，关闭窗口前stopPreview
			//地图图层：全球眼展示，弹出框关闭前，父窗口先stopPreview
			map.addAttribute("type", type);
			forward = "/map/arcgis/standardmappage/globalEyesIvmsPlay.ftl";
		}else if (monitor.getCompanyType()==6) {//海康威视DVR
			map.addAttribute("type", type);
			forward = "/map/arcgis/standardmappage/globalEyesNetVideoActivePlay.ftl";
		}else if (monitor.getCompanyType()==7) {//北京互信互通VPN
			map.addAttribute("type", type);
			forward = "/map/arcgis/standardmappage/globalEyesPlayVPN.ftl";
		}else{
			forward = "/map/arcgis/standardmappage/globalEyesZXProx.ftl";
		}
		
		return forward;
	}
	
	/**
	 * 2014-11-04 chenlf add
	 * 防控区域全球眼查询页面
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
	 * 全球眼数据---框选
	 * @param session
	 * @param page 页码
	 * @param rows 每页条数
	 * @param orgCode 信息域组织编码
	 * @param platformName 平台名称
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
	
	///---全球眼 end----------------------------------------------
	
	///---护路护线队员 start----------------------------------------------
	/**
	 * 网格管理人员数据页
	 * @param session
	 * @param gridId 网格ID
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
	///---护路护线队员 end----------------------------------------------
	
	/**
	 * 驻村工作队员数据页
	 * @param session
	 * @param gridId 网格ID
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
	
	///---网格员 start----------------------------------------------
	/**
	 * 网格管理人员数据页
	 * @param session
	 * @param gridId 网格ID
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
		//是否启用ANYCHAT视频通话
		String isUserAnychat = this.funConfigurationService.turnCodeToValue(ConstantValue.IS_USER_ANYCHAT, "",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUserAnychat", isUserAnychat);
		
		//是否启用MEDIASOUP 视频通话
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
	 * 台江地图图层分类管理网格管理人员数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/gridAdminTaijiang")
	public String gridAdminTaijiang(HttpSession session, @RequestParam(value="gridId") Long gridId, ModelMap map) {
		map.addAttribute("gridId", gridId);
		return "/map/arcgis/standardmappage/taijiang/gridAdmin.ftl";
	}
	
	/**
	 * 网格管理人员数据
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
	 * 护路护线队员数据
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
		//判断查询时间是否包含当天轨迹还是只查历史轨迹
		String trackType = "";
		if(!StringUtils.isEmpty(trackStartDate)){
			String pattern = "yyyy-MM-dd HH:mm:ss";
			String today = DateUtils.getToday(pattern);
			try {
				boolean startDateIsBeforeToday = DateUtils.compareMinDate(trackStartDate,pattern,today,pattern);//判断起始时间是否早于当前时间
				boolean endDateIsBeforeToday = DateUtils.compareMinDate(trackEndDate,pattern,today,pattern);//判断结束时间是否早于当前时间
				if(startDateIsBeforeToday && !endDateIsBeforeToday){//当前日期在 查询日期的区间里
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
		if (infoOrgCode.startsWith(ConstantValue.NANCHANG_FUNC_ORG_CODE)) { //南昌只查网格层级
			params.put("gridLevel", 6);
		}
        //params.put("orderCondition","T3.LOCATE_TIME DESC NULLS LAST");
		cn.ffcs.common.EUDGPagination pagination = gridAdminService.findGridAdminPagination(page, rows, params);
		List<GridAdmin> list = (List<GridAdmin>) pagination.getRows();
		for(int i=0;i<list.size();i++){
			if(StringUtils.isNotBlank(list.get(i).getInfoOrgCode()) && list.get(i).getInfoOrgCode().length() > 6){//街道及以下的，列表上的所属网格只要镇（街道）村（居）网格X
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
	 * 护路护线队员数据
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
		//判断查询时间是否包含当天轨迹还是只查历史轨迹
		String trackType = "";
		if(!StringUtils.isEmpty(trackStartDate)){
			String pattern = "yyyy-MM-dd HH:mm:ss";
			String today = DateUtils.getToday(pattern);
			try {
				boolean startDateIsBeforeToday = DateUtils.compareMinDate(trackStartDate,pattern,today,pattern);//判断起始时间是否早于当前时间
				boolean endDateIsBeforeToday = DateUtils.compareMinDate(trackEndDate,pattern,today,pattern);//判断结束时间是否早于当前时间
				if(startDateIsBeforeToday && !endDateIsBeforeToday){//当前日期在 查询日期的区间里
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
		if (infoOrgCode.startsWith(ConstantValue.NANCHANG_FUNC_ORG_CODE)) { //南昌只查网格层级
			params.put("gridLevel", 6);
		}
        //params.put("orderCondition","T3.LOCATE_TIME DESC NULLS LAST");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = gridAdminService.findGridAdminCount(params);
		
		
		return resultMap;
	}
	
	/**
	 * 2014-06-24 chenlf add
	 * 获取网格员的定位信息
	 * @param session
	 * @param ids 网格员ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
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
				gridAdminName = gridAdminName + "（最后定位时间：" + DateUtils.formatDate(arcgisInfoOfPublic.getHandleDate(), "yyyy-MM-dd HH:mm:ss") + "）";
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
	 * 获取驻村工作队员定位信息
	 * 因为用的是同一张表定位信息获取可以共用
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
	 * 获取护路护线队员定位信息
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
	 * 网格管理员 单条
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
		if (infoOrgCode.startsWith(ConstantValue.NANCHANG_FUNC_ORG_CODE)) { //南昌默认显示前一天开始和结束
			ca.add(Calendar.DATE,-1);
			if (StringUtils.isBlank(startTime)) {
				queryDay = sdf.format(ca.getTime()).substring(0,10);
				startTime = "00:00";
			}
			if (StringUtils.isBlank(endTime)) endTime = "23:59";
		} else {
			if (StringUtils.isBlank(queryDay)) {// 为空默认当天
				queryDay = sdf.format(ca.getTime()).substring(0,10);
			}
			if (StringUtils.isBlank(startTime)) {// 为空默认7点
				startTime = "07:00";
			}
			if (StringUtils.isBlank(endTime)) {// 为空默认21点
				endTime = "21:00";
			}
		}
		map.addAttribute("queryDay", queryDay);
		map.addAttribute("startTime", startTime);
		map.addAttribute("endTime", endTime);
		map.addAttribute("currentDate", sdf.format(new Date()));
		if(StringUtils.isNotBlank(play)){//是否立即播放轨迹
			map.addAttribute("play", play);
		}
		//户总数 人总数根据地域获取方式变更
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
		map.addAttribute("tel_show", 1); //拨号
		map.addAttribute("gota_show", 0); //gota
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		//网格管理员结束
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
		//根据权限点的开关显示隐藏拨号功能
		map.addAttribute("callcenter_show", isHjCenter);//呼叫中心
	/*	map.addAttribute("gridAdmin", bean[0]==null?null:(GridAdmin)bean[0]);
		map.addAttribute("gridAdmin1", bean[1]==null?null:(GridAdmin)bean[1]);
		map.addAttribute("gridAdmin2", bean[2]==null?null:(GridAdmin)bean[2]);*/
		map.addAttribute("gridAdmin", gridAdmin);
		map.addAttribute("isCross", isCross);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//是否启用视频通话
		String IS_USER_MMP = this.funConfigurationService.turnCodeToValue(
				ConstantValue.IS_USER_MMP, null,
				IFunConfigurationService.CFG_TYPE_FACT_VAL,
				userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUserMmp", IS_USER_MMP);
		//是否启用语音盒呼叫
		String IS_USER_YYHHJ = this.funConfigurationService.turnCodeToValue(
				ConstantValue.IS_USER_YYHHJ, null,
				IFunConfigurationService.CFG_TYPE_FACT_VAL,
				userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isBlank(IS_USER_YYHHJ)){
			IS_USER_YYHHJ = "true";//默认为启用
		}
		map.addAttribute("isUserYyhhj", IS_USER_YYHHJ);
		//是否启用发送短信
		String IS_USER_FSDX = this.funConfigurationService.turnCodeToValue(
				ConstantValue.IS_USER_FSDX, null,
				IFunConfigurationService.CFG_TYPE_FACT_VAL,
				userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		if(StringUtils.isBlank(IS_USER_FSDX)){
			IS_USER_FSDX = "true";//默认为启用
		}
		map.addAttribute("isUserFsdx", IS_USER_FSDX);
		//网格员是否有工作情况
		String GRID_ADMIN_WITH_WORK_FLAG = this.funConfigurationService.turnCodeToValue(
				ConstantValue.GRID_ADMIN_WITH_WORK_FLAG, null, IFunConfigurationService.CFG_TYPE_FACT_VAL, infoOrgCode,
				IFunConfigurationService.CFG_ORG_TYPE_0);
		if ("true".equals(GRID_ADMIN_WITH_WORK_FLAG)) {
			map.addAttribute("gridAdminWithWorkFlag", "true");
			map.addAttribute("OA_DOMAIN", App.OA.getDomain(session));
		}
		
		
		//是否启用ANYCHAT视频通话
		String isUserAnychat = this.funConfigurationService.turnCodeToValue(ConstantValue.IS_USER_ANYCHAT, "",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUserAnychat", isUserAnychat);
		//是否启用MEDIASOUP 视频通话
		String isUseMediasoup = this.funConfigurationService.turnCodeToValue(ConstantValue.IS_USE_MEDIASOUP, "",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUseMediasoup", isUseMediasoup);
		
		if(userInfo != null && gridAdmin != null && StringUtils.isNotBlank(gridAdmin.getUserName())){
			map.addAttribute("userName",gridAdmin.getUserName());//江阴查询轨迹使用
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
	 * 播放巡查员执法仪
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
	 * 工作详情
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
		//设置日期格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(new Date());
		map.addAttribute("date", date);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
        map.addAttribute("RS_URL", App.RS.getDomain(session));
		map.addAttribute("orgCode", gridOrgCode == null ? u.getOrgCode() : gridOrgCode);
		map.addAttribute("orgId", orgId == null ? u.getOrgId() : orgId);
		// 建湖
		if (StringUtils.startsWith(u.getOrgCode(), ConstantValue.JIANHU_FUNC_ORG_CODE)) {
			map.addAttribute("region",ConstantValue.JIANHU_FUNC_ORG_CODE);
		}
		if (StringUtils.startsWith(u.getOrgCode(), ConstantValue.NANAN_FUNC_ORG_CODE)) {
			return "/map/arcgis/standardmappage/gridAdminWorkDetail_nanAn.ftl";
		}
		return "/map/arcgis/standardmappage/gridAdminWorkDetail.ftl";
	}
	
	/**
	 * 护路护线队员单条
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
		map.addAttribute("tel_show", 1); //拨号
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
		map.addAttribute("callcenter_show", isHjCenter);//呼叫中心
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
		map.addAttribute("tel_show", 1); //拨号
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
		map.addAttribute("callcenter_show", isHjCenter);//呼叫中心
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
	 * 网格管理员 发送短信
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
	
	///---网格员 end----------------------------------------------
	
	///---精准扶贫 start----------------------------------------------
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
		if (id == null || id == 0L) throw new Exception("缺少参数【id】！");
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(id, false);
		Map<String, Object> statMap = agricultureService.getPoorStatisticsI(gridInfo.getInfoOrgCode());
		Map<String, Object> statMap2 = populationService.getPopulationNum(gridInfo.getInfoOrgCode());
		statMap.putAll(statMap2);
		map.addAttribute("statMap", statMap);
		return "/map/arcgis/standardmappage/plcDetail.ftl";
	}
	
	///---精准扶贫 end----------------------------------------------
	
	/**************************************布控列表 begin***********************************/
	/**
	 * 跳转布控申请目标列表
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
	 * 加载布控申请目标数据
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
	 * 跳转布控目标消息记录列表
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
		//设备信息
		ControlTargetRecord controlTargetRecord = this.idssControlService.findIdssControlTargetRecordById(recordId);
		map.addAttribute("controlTargetRecord", controlTargetRecord);
		//关联人员
		if(StringUtils.isNotBlank(controlTargetRecord.getControlTargetType())){
			if(controlTargetRecord.getControlTargetType().equals("001")){//身份证
				map.addAttribute("ciRsTop", getCiRsInfo(controlTargetRecord.getControlTargetObject()));
//				map.addAttribute("ciRsTop", getCiRsInfo(null));
			}
			if(controlTargetRecord.getControlTargetType().equals("002")){//车牌
				CiRsCar ciRsCar = ciRsCarService.getCiRsCar(controlTargetRecord.getControlTargetObject());
//				CiRsCar ciRsCar = ciRsCarService.getCiRsCar("闽E8522R");
				map.addAttribute("ciRsTop", getCiRsInfo(ciRsCar.getIdentityCard()));
			}
			if(controlTargetRecord.getControlTargetType().equals("003")){//手机号
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
				//相关人员重口
				Map<String, Object> tagFlag = ciRsService.getRsTagFlag(ciRsId);// 人员标签
				
				//是否启用国标
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
					if (tagFlag.get(s).toString().equals("1")) {// 1为存在标签，0为不存在
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
		//网格力量
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
		//家庭信息
		if(StringUtils.isNotBlank(cirs.getFamilySn())){
			cn.ffcs.resident.bo.Pagination pagination = ciRsService.findPageByFamilySn(cirs.getFamilySn(), cirs.getOrgCode(), rows, page);
			if(pagination!=null){
				e.setTotal(pagination.getTotalCount());
				e.setRows(pagination.getList());
			}
		}
		return e;
	}
	
	/**************************************布控列表 end*************************************/
	

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
		map.put("party", "党员");
		map.put("retire", "退休");
		map.put("old", "老年人");
		map.put("hbcare", "居家养老");
		map.put("army", "服兵役");
		map.put("unemployed", "失业");
		map.put("poor", "低保");
		map.put("deformity", "残障");
		map.put("mentalIllnessRecord", "精神障碍");
		map.put("dangerousGoodsRecord", "危险品");
		map.put("petitionRecord", "上访");
		map.put("drugRecord", "吸毒");
		map.put("cultMemberRecord", "邪教人员");
		map.put("motoMemberRecord", "摩托车工");
		map.put("correctionalRecord", "矫正");
		map.put("releasedRecord", "刑释");
		map.put("volunteer", "志愿者");
		map.put("taibao", "台胞");
		map.put("youthPerson", "重点青少年");
		map.put("cleaner", "清扫保洁人员");
		map.put("aidsPerson", "艾滋病人员");
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
