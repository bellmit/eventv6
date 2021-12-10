package cn.ffcs.zhsq.map.arcgis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.ffcs.lzly.keyVehicles.service.IKeyVehiclesService;
import cn.ffcs.lzly.mybatis.domain.keyVehicles.KeyVehicles;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.common.GdZTreeNode;
import cn.ffcs.resident.bo.CiPartyGroup;
import cn.ffcs.resident.bo.CiRsCriteria;
import cn.ffcs.resident.bo.Pagination;
import cn.ffcs.resident.service.CiPartyGroupService;
import cn.ffcs.resident.service.CiRsPartyService;
import cn.ffcs.resident.service.CiRsService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.mybatis.domain.zzgl.dailyactivity.DailyActivity;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.AreaBuildingInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.domain.db.ImportUnit;
import cn.ffcs.shequ.zzgl.service.dailyactivity.IDailyActivityService;
import cn.ffcs.shequ.zzgl.service.grid.IAreaBuildingInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.main.IImportUnitService;
import cn.ffcs.shequ.zzgl.service.res.IUrbanObjService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfo;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisdataofthing")
public class ArcgisDataOfThingLocalController extends ZZBaseController {
	@Autowired
	private IAreaBuildingInfoService areaBuildingInfoService;
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	private CiPartyGroupService ciPartyGroupService;
	@Autowired
	private CiRsPartyService ciRsPartyService;
	@Autowired
	private CiRsService ciRsService;
	@Autowired
	private IImportUnitService importUnitService;//重点单位
	@Autowired
    private IDictionaryService dictionaryService;
	@Autowired
	private IDailyActivityService dailyActivityService ;
	@Autowired
	private IUrbanObjService urbanObjService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private IMenuConfigService menuConfigService;
	@Autowired
	private IKeyVehiclesService keyVehiclesService;
	
	/**
	 * 组织机构基础信息数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/partyOrgInfo")
	public String partyOrgInfo(HttpSession session, @RequestParam(value="gridId") Long gridId,@RequestParam(value="groupType", required=false) String groupType, ModelMap map
			,@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("groupType", groupType==null?"":groupType);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/partyOrganization/standardPartyOrg.ftl";
	}

	@RequestMapping(value = "/newPartyOrgInfo")
	public String newPartyOrgInfo(HttpSession session, HttpServletRequest request, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		String gridId = request.getParameter("gridId");
		String orgCode = request.getParameter("orgCode");
		String elementsCollectionStr = request.getParameter("elementsCollectionStr");
		map.put("gridId", gridId);
		map.put("orgCode", orgCode);
		map.put("elementsCollectionStr", elementsCollectionStr);
		GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgByCode("statPartyOrganization", userInfo.getOrgCode());
		super.transGisDataCfgUrl(gisDataCfg, session);
		if (gisDataCfg != null) request.setAttribute("statPartyOrgEcs", gisDataCfg.getElementsCollectionStr());
		return "/map/arcgis/standardmappage/partyOrganization/standardPartyOrgTree.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/partyOrgInfoTreeJson")
	public List<GdZTreeNode> partyOrgInfoTree(HttpSession session, HttpServletRequest request, ModelMap map) {
		List<CiPartyGroup> ciPartyGroups = new ArrayList<CiPartyGroup>();
		String partyGroupId = request.getParameter("partyGroupId");
		String orgCode = request.getParameter("orgCode");
		String partyGroupName = request.getParameter("partyGroupName");
		if (StringUtils.isNotBlank(partyGroupId)) {
			ciPartyGroups = ciPartyGroupService.findLeaves(Long.valueOf(partyGroupId));
		} else {
			if (StringUtils.isNotBlank(partyGroupName)) {
				CiPartyGroup ciPartyGroup = new CiPartyGroup();
				ciPartyGroup.setOrgCode(orgCode);
				ciPartyGroup.setPartyGroupName(partyGroupName);
				ciPartyGroups = ciPartyGroupService.findListByCriteria(ciPartyGroup, 1, Integer.MAX_VALUE);
			} else {
				CiPartyGroup ciPartyGroup = new CiPartyGroup();
				ciPartyGroup.setOrgCode(orgCode);
				ciPartyGroups = ciPartyGroupService.findPageRoots(ciPartyGroup);
			}
		}
		
		List<GdZTreeNode> zTreeNodes = new ArrayList<GdZTreeNode>();
		for (CiPartyGroup ciPartyGroup : ciPartyGroups) {
			GdZTreeNode node = new GdZTreeNode();
			node.setId(String.valueOf(ciPartyGroup.getPartyGroupId()));
			node.setName(ciPartyGroup.getPartyGroupName());
			node.setIsParent("closed".equals(ciPartyGroup.getState()));
			zTreeNodes.add(node);
		}
		return zTreeNodes;
	}
	
	/**
	 * 组织机构基础信息数据
	 * @param session
	 * @param page 
	 * @param rows
	 * @param gridId
	 * @param groupType
	 * @param partyGroupName	
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/partyOrgInfoListData", method=RequestMethod.POST)
	public Pagination partyOrgInfoListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId") Long gridId, @RequestParam(value="groupType", required=false) String groupType, @RequestParam(value="partyGroupName", required=false) String partyGroupName) {
		if(page<=0) page=1;
		if(partyGroupName!=null) partyGroupName = partyGroupName.trim();			
		//Map<String, Object> params = new HashMap<String, Object>();
		//params.put("startGridId", gridId);
		//params.put("name", name);
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, true);
		String orgCode = gridInfo.getInfoOrgCode();
		CiPartyGroup ciPartyGroup = new CiPartyGroup();
		ciPartyGroup.setPartyGroupName(partyGroupName);
		ciPartyGroup.setGridId(gridId);
		ciPartyGroup.setGroupType(groupType);
		ciPartyGroup.setOrgCode(orgCode);
		//EUDGPagination pagination = null;
		Pagination pagination = ciPartyGroupService.findPageByCriteria(ciPartyGroup, 1, Integer.MAX_VALUE);
		return pagination;
	}
	
	/**
	 * 组织结构GIS信息
	 * @param session
	 * @param map
	 * @param res
	 * @param result
	 * @param mapt
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisPartyOrgLocateDataList")
	public List<ArcgisInfoOfPublic> getArcgisPartyGroupLocateDataList(HttpSession session, ModelMap map, HttpServletResponse res,
			@RequestParam(value="ids") String ids, @RequestParam(value="mapt", required=false) Integer mapt
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr)throws Exception {
		String markerType = "RS_PARTY_GROUP";
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisPartyGroupLocateDataListByIds(ids, mapt,markerType);
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getNewPartyGroupXYList")
	public List<ArcgisInfo> getNewPartyGroupXYList(HttpSession session, ModelMap map, HttpServletRequest request,
			@RequestParam(value = "infoOrgCode") String infoOrgCode,
			@RequestParam(value = "mapt", required = false) Integer mapt) throws Exception {
		String markerType = "RS_PARTY_GROUP";
		List<ArcgisInfo> arcgisInfos = arcgisDataOfLocalService.getNewPartyGroupXYList(infoOrgCode, mapt, markerType);
		return arcgisInfos;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getNewPartyGroupXYById")
	public List<ArcgisInfo> getNewPartyGroupXYById(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "ids") String ids, @RequestParam(value = "mapt") Integer mapt) {
		String markerType = "RS_PARTY_GROUP";
		List<ArcgisInfo> arcgisInfos = arcgisDataOfLocalService.getNewPartyGroupXYById(ids, mapt, markerType);
		return arcgisInfos;
	}
	
	/**
	 * 组织机构
	 * @param session
	 * @param partyGroupId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getPartyOrgInfoDetailOnMap")
	public String getPartyOrgInfoDetailOnMap(HttpSession session,
			@RequestParam(value = "partyGroupId") Long partyGroupId,
			@RequestParam(value = "isCross", required = false) String isCross, ModelMap map) {
		CiPartyGroup ciPartyGroup = ciPartyGroupService.findById(partyGroupId);
		String buildingAddress = null;
		if (ciPartyGroup!=null){
			if (ciPartyGroup.getGridId()!=null){
				MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(ciPartyGroup.getGridId(), false);
				if (gridInfo!=null){
				  ciPartyGroup.setGridName(gridInfo.getGridName());
				}
			}
			if (ciPartyGroup.getBuildingId()!=null){
				AreaBuildingInfo areaBuildingInfo = areaBuildingInfoService.findAreaBuildingInfoById(ciPartyGroup.getBuildingId());
				if (areaBuildingInfo!=null){
				  ciPartyGroup.setBuildingName(areaBuildingInfo.getBuildingName());
				  buildingAddress = areaBuildingInfo.getBuildingAddress();
				}
			}
		}
		
		map.addAttribute("isCross", isCross);
		map.addAttribute("ciPartyGroup", ciPartyGroup);
		map.addAttribute("buildingAddress", buildingAddress);
		map.addAttribute("ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		String infoOrgCode = super.getDefaultInfoOrgCode(session);
		if (infoOrgCode.startsWith("350102")) {
			map.addAttribute("showPartyMember", true);
		}
		return "/map/arcgis/standardmappage/partyOrganization/partyOrgDetailOnMap.ftl";
	}
	
	/**
	 * 党组织-概要信息
	 * @param session
	 * @param partyGroupId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getPartyOrgSummaryInfoOnMap")
	public String getPartyOrgSummaryInfoOnMap(HttpSession session, @RequestParam(value="partyGroupId") Long partyGroupId, ModelMap map) {
		CiPartyGroup ciPartyGroup = ciPartyGroupService.findById(partyGroupId);
		//党组织地址
		String buildingAddress = null;
		if (ciPartyGroup!=null){
			if (ciPartyGroup.getGridId()!=null){
				MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(ciPartyGroup.getGridId(), false);
				if (gridInfo!=null){
				  ciPartyGroup.setGridName(gridInfo.getGridName());
				}
			}
			if (ciPartyGroup.getBuildingId()!=null){
				AreaBuildingInfo areaBuildingInfo = areaBuildingInfoService.findAreaBuildingInfoById(ciPartyGroup.getBuildingId());
				if (areaBuildingInfo!=null){
				  ciPartyGroup.setBuildingName(areaBuildingInfo.getBuildingName());
				  buildingAddress = areaBuildingInfo.getBuildingAddress();
				}
			}
		}
		map.addAttribute("ciPartyGroup", ciPartyGroup);
		map.addAttribute("buildingAddress", buildingAddress);
		String infoOrgCode = super.getDefaultInfoOrgCode(session);
		if (infoOrgCode.startsWith("350102")) {
			map.addAttribute("showPartyMember", true);
		}
		return "/map/arcgis/standardmappage/partyOrganization/partyOrgSummaryInfoOnMap.ftl";
	}
	
	@RequestMapping(value = "/partyMemberIndex")
	public String partyMemberIndex(HttpSession session, @RequestParam(value = "partyGroupId") Long partyGroupId,
			@RequestParam(value = "orgCode") String orgCode,
			@RequestParam(value = "isCross", required = false) String isCross, ModelMap map) {
		map.addAttribute("partyGroupId", partyGroupId);
		map.addAttribute("isCross", isCross);
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.addAttribute("POPULATION_URL", App.RS.getDomain(session));
		if (StringUtils.isNotBlank(orgCode) && orgCode.startsWith("320281")) {
			map.addAttribute("IsNewPartyOrg", true);
		}
		return "/map/arcgis/standardmappage/partyOrganization/partyMemberIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value="/listPartyMemberIndexData", method=RequestMethod.POST)
	public EUDGPagination listPartyMemberIndexData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="partyGroupId") Long partyGroupId,@RequestParam(value="orgCode") String orgCode) {
		if(page<=0) page=1;						
		//Map<String, Object> params = new HashMap<String, Object>();
		//params.put("startGridId", gridId);
		//prams.put("name", name);
		CiRsCriteria criteria = new CiRsCriteria();
		criteria.setPartyGroupId(partyGroupId.toString());
		criteria.setStatus("1");
		criteria.setPartyStatus("001");
		//criteria.setOrgCode(orgCode);
		Pagination pagination = ciRsPartyService.findPageNew(criteria, page, rows);
		EUDGPagination EUDGPagination = new EUDGPagination();
		EUDGPagination.setRows(pagination.getList());
		EUDGPagination.setTotal(pagination.getTotalCount());
		return EUDGPagination;
	}
	
	//----党员 start
	/**
	 * 党员信息
	 */
	@RequestMapping(value = "/partyMemberInfoIndex")
	public String partyMemberInfoIndex(HttpSession session, @RequestParam(value = "partyGroupId") Long partyGroupId,
			@RequestParam(value = "isCross", required = false) String isCross, ModelMap map) {
		map.addAttribute("partyGroupId", partyGroupId);
		map.addAttribute("isCross", isCross);
		map.addAttribute("ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		return "/map/arcgis/standardmappage/partyOrganization/partyMemberInfo.ftl";
	}
	
	/**
	 * 党员信息数据
	 * @param session
	 * @param page 
	 * @param rows
	 * @param partyGroupId	
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listPartyMemberData", method=RequestMethod.POST)
	public Pagination listPartyMemberData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="partyGroupId") Long partyGroupId) {
		if(page<=0) page=1;						
		//Map<String, Object> params = new HashMap<String, Object>();
		//params.put("startGridId", gridId);
		//prams.put("name", name);
		CiRsCriteria criteria = new CiRsCriteria();
		criteria.setPartyGroupId(partyGroupId.toString());
		Pagination pagination = ciRsPartyService.findPageNew(criteria, page, rows);
		return pagination;
	}
	
	/**
	 * 组织机构-党员详情
	 * @param session 
	 * @param ciPartyId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/partyMemberInfoDetail")
	public String partyMemberDetail(HttpSession session, @RequestParam(value="ciRsId") Long ciRsId, ModelMap map) {				
		Map<String, String> ciRsParty = ciRsService.getRsInfo(ciRsId, 1);
		//CiRsParty ciRsParty =resultMap.get("CiRsParty");
		map.addAttribute("ciRsParty", ciRsParty);
		return "/map/arcgis/standardmappage/partyOrganization/partyMemberDetail.ftl";
	}
	//----党员 end
	
	@RequestMapping(value="/toArcgisDataListOfDailyActivity")
	public String dailyActivity(HttpSession session, ModelMap map,
			@RequestParam(value = "activityType", required=false) String activityType,
			@RequestParam(value = "infoOrgCode", required=false) String infoOrgCode,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String importUnitOrgId = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_ID).toString();
		List<Map<String, Object>> importUnitTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_IMPORT_UNIT, ConstantValue.COLUMN_IMPORT_UNITTYPE, userInfo.getOrgCode());

		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("activityType", activityType);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("importUnitTypeDC", importUnitTypeDC);
		map.put("importUnitOrgId", importUnitOrgId);
		
		return "/map/arcgis/standardmappage/standardDailyActivity.ftl";
	}
	
	@ResponseBody	
	@RequestMapping(value="/listDailyActivityData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination dailyActivityListData(HttpSession session, 
			@RequestParam(value="page") int page, 
			@RequestParam(value="rows") int rows, 
			@RequestParam(value = "infoOrgCode", required=false) String infoOrgCode,
			@RequestParam(value="importUnitOrgId", required=false) String importUnitOrgId, 
			@RequestParam(value="name", required=false) String activityName, 
			@RequestParam(value="address", required=false) String address, 
			@RequestParam(value="type", required=false) String type,
			@RequestParam(value="activityType", required=false) String activityType) {
		if(page<=0) page=1;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridCode", infoOrgCode);
		params.put("infoOrgStr", importUnitOrgId);
		params.put("activityName", activityName);
		params.put("address", address);
		params.put("types", type);
		params.put("activityType", activityType);
		cn.ffcs.common.EUDGPagination pagination = dailyActivityService.findDailyActivityPagination(page, rows, params);
		return pagination;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getArcgisDailyActivityList")
	public List<ArcgisInfoOfPublic> getArcgisDailyActivityList(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "showType") String showType
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisActivityLocateDataListByIds(ids,mapt,"ACTIVITY");
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}

	@RequestMapping(value="/dailyActivityDetail")
	public String dailyActivityDetail(HttpSession session, ModelMap map, 
			@RequestParam(value = "infoOrgCode", required=false) String infoOrgCode,
			@RequestParam(value="activityId") Long activityId){

		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("activityId", activityId);
//		paramsMap.put("activityType", type);
//		paramsMap.put("userOrgCode", userInfo.getOrgCode());
		DailyActivity dailyActivity = dailyActivityService.findDailyActivityById(paramsMap);
//		ImportUnit importUnit = importUnitService.getImportUnitById(importUnitId);
		map.addAttribute("dailyActivity", dailyActivity);
		
		return "/map/arcgis/standardmappage/dailyActivityDetail.ftl";
	}
	
	//----重点单位 begin
	/**
	 * 跳转重点单位专题图层
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfImportUnit")
	public String keyPlace(HttpSession session, ModelMap map,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String importUnitOrgId = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_ID).toString();
		List<Map<String, Object>> importUnitTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_IMPORT_UNIT, ConstantValue.COLUMN_IMPORT_UNITTYPE, userInfo.getOrgCode());
		
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("importUnitTypeDC", importUnitTypeDC);
		map.put("importUnitOrgId", importUnitOrgId);
		
		return "/map/arcgis/standardmappage/standardImportUnit.ftl";
	}
	
	/**
	 * 获取重点单位列表记录
	 * @param session
	 * @param page
	 * @param rows
	 * @param importUnitOrgId	重点单位信息域编号
	 * @param enterpriseName	单位名称
	 * @param address			单位地址
	 * @param type				单位类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listImportUnitData", method=RequestMethod.POST)
	public EUDGPagination unitListData(HttpSession session, 
			@RequestParam(value="page") int page, 
			@RequestParam(value="rows") int rows, 
			@RequestParam(value="infoOrgCode", required=false) String infoOrgCode, 
			@RequestParam(value="importUnitOrgId", required=false) String importUnitOrgId, 
			@RequestParam(value="name", required=false) String enterpriseName, 
			@RequestParam(value="address", required=false) String address, 
			@RequestParam(value="type", required=false) String type) {
		if(page<=0) page=1;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("infoOrgStr", importUnitOrgId);
		params.put("infoOrgCode", infoOrgCode);
		params.put("enterpriseName", enterpriseName);
		params.put("address", address);
		params.put("types", type);
		cn.ffcs.common.Pagination unitPagination = importUnitService.getImportUnitPagination(page, rows, params);
				
		EUDGPagination  pagination = new EUDGPagination(unitPagination.getTotalCount(), unitPagination.getList());
		return pagination;
	}
	
	/**
	 * 重点单位详情
	 * @param session
	 * @param map
	 * @param importUnitId
	 * @return
	 */
	@RequestMapping(value="/importUnitDetail")
	public String importUnitDetail(HttpSession session, ModelMap map, 
			@RequestParam(value="importUnitId") Long importUnitId){
		ImportUnit importUnit = importUnitService.getImportUnitById(importUnitId);
		map.addAttribute("importUnit", importUnit);
		
		return "/map/arcgis/standardmappage/partyOrganization/importUnitDetail.ftl";
	}
	
	/**
	 * 重点单位多点定位
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
	@RequestMapping(value = "/getArcgisImportUnitLocateDataList")
	public List<ArcgisInfoOfPublic> getArcgisImportUnitLocateDataList(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "showType") String showType
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		
		if (StringUtils.isNotBlank(ids)) {
			list = this.arcgisDataOfLocalService.getArcgisImportUnitLocateDataListByIds(ids,mapt);
			for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
		}
		
		return list;
	}
	//----重点单位 end
	/**
	 * 跳转部件页面
	 * @param session
	 * @param mapt
	 * @param showType
	 * @return
	 */
	@RequestMapping(value = "/goToUrbanObjList")
	public String goToUrbanObjList(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "gridId", required = false) Long gridId,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defGridInfo = this.getDefaultGridInfo(session);
		if (gridId == null) {
			gridId = (Long) defGridInfo.get(KEY_START_GRID_ID);
		}
		if(StringUtils.isNotBlank(infoOrgCode)){
			infoOrgCode = (String) defGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		}
		map.put("gridId", gridId);
		map.put("infoOrgCode", infoOrgCode);
		if (StringUtils.isBlank(elementsCollectionStr)) {
			GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgByCode("URBAN_COMPONENTS", userInfo.getOrgCode());
			if (gisDataCfg != null) elementsCollectionStr = gisDataCfg.getElementsCollectionStr();
		}
		map.put("elementsCollectionStr", elementsCollectionStr);
		List<BaseDataDict> urbanTypes = baseDictionaryService.getDataDictTree("D005011", userInfo.getOrgCode());
		map.put("urbanTypes", urbanTypes);
		return "/map/arcgis/standardmappage/urbanObj/standardUrbanObj.ftl";
	}
	/**
	 * 部件定位gis信息
	 * @param session
	 * @param mapt
	 * @param showType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUrbanObjGisLocateDataList")
	public List<ArcgisInfo> getUrbanObjGisLocateDataList(HttpSession session,
			@RequestParam(value = "infoOrgCode") String infoOrgCode,
			@RequestParam(value = "mapt") Integer mapt, @RequestParam(value = "objCode") String objCode) {
		List<ArcgisInfo> list = this.arcgisDataOfLocalService.getUrbanObjGisLocateDataList(infoOrgCode, mapt, objCode);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/urbanObjListData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination urbanObjListData(HttpSession session, @RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "gridId", required = false) Long gridId,
			@RequestParam(value = "name", required = false) String name) {
		if (page <= 0)
			page = 1;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", gridId);
		params.put("name", name);
		return urbanObjService.list(page, rows, params);
	}

	/**
	 * 重点车辆图层列表
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @param orgCode
	 * @param elementsCollectionStr
	 * @return
	 */
	@RequestMapping(value = "/keyVehiclesIndex")
	public String keyVehiclesIndex(HttpSession session, HttpServletRequest request, ModelMap map,
								   @RequestParam(value = "gridId") Long gridId,
								   @RequestParam(value = "orgCode") Long orgCode,
								   @RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(StringUtils.isNotBlank(elementsCollectionStr)){
			map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		}
		map.addAttribute("gridId", gridId);
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("ZZGRID_DOMAIN", App.ZZGRID.getDomain(session));
		return "/map/arcgis/standardmappage/keyVehicles/standardKeyVehicles.ftl";
	}

	/**
	 * 获取分页信息
	 * @param session
	 * @param page
	 * @param rows
	 * @param gridId
	 * @param orgCode
	 * @param bizType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/keyVehiclesListData", method = RequestMethod.POST)
	public cn.ffcs.system.publicUtil.EUDGPagination keyVehiclesListData(HttpSession session, @RequestParam(value = "page") int page,
											  @RequestParam(value = "rows") int rows, @RequestParam(value = "gridId", required = false) Long gridId,
											  @RequestParam(value = "orgCode", required = false) String orgCode,
											  @RequestParam(value = "bizType", required = false) String bizType) throws Exception {
		if (page <= 0)
			page = 1;
		Map<String, Object> params = new HashMap<String, Object>();
		KeyVehicles criteria = new KeyVehicles();
		if(StringUtils.isNotBlank(orgCode)){
			params.put("regionCode", orgCode);
		}
		if(StringUtils.isNotBlank(bizType)){
			params.put("bizType", bizType);
		}

		cn.ffcs.system.publicUtil.EUDGPagination pagination = keyVehiclesService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 获取重点车辆定位信息
	 * @param session
	 * @param request
	 * @param map
	 * @param ids
	 * @param mapt
	 * @param elementsCollectionStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfKeyVehiclesListByIds")
	public List<ArcgisInfoOfPublic> getArcgisDataOfKeyVehiclesListByIds(HttpSession session, HttpServletRequest request, ModelMap map,
																		@RequestParam(value = "ids") String ids, @RequestParam(value = "mapt") Integer mapt,
																		@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisDataListOfKeyVehiclesByIds(ids, mapt);
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			String elementsCollectionStrType = formatElementsCollectionStr(elementsCollectionStr, "keyVehicles", arcgisInfoOfPublic.getBizType());
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStrType);
		}
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getArcgisPartyMemberDataList")
	public List<ArcgisInfo> getArcgisPartyMemberDataList(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "infoOrgCode") String infoOrgCode, @RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		List<ArcgisInfo> list = this.arcgisDataOfLocalService.getArcgisPartyMemberDataList(infoOrgCode, mapt);
		/*for (ArcgisInfo arcgisInfoOfPublic : list) {
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}*/
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getArcgisRectifyDataList")
	public List<ArcgisInfo> getArcgisRectifyDataList(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "infoOrgCode") String infoOrgCode, @RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		List<ArcgisInfo> list = this.arcgisDataOfLocalService.getArcgisRectifyDataList(infoOrgCode, mapt);
		/*for (ArcgisInfo arcgisInfoOfPublic : list) {
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}*/
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getArcgisJiangxiPetitionDataList")
	public List<ArcgisInfo> getArcgisJiangxiPetitionDataList(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "infoOrgCode") String infoOrgCode, @RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		List<ArcgisInfo> list = this.arcgisDataOfLocalService.getArcgisJiangxiPetitionDataList(infoOrgCode, mapt);
		/*for (ArcgisInfo arcgisInfoOfPublic : list) {
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}*/
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getArcgisCampsDataList")
	public List<ArcgisInfo> getArcgisCampsDataList(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "infoOrgCode") String infoOrgCode, @RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		List<ArcgisInfo> list = this.arcgisDataOfLocalService.getArcgisCampsDataList(infoOrgCode, mapt);
		/*for (ArcgisInfo arcgisInfoOfPublic : list) {
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}*/
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getArcgisCorDataList")
	public List<ArcgisInfo> getArcgisCorDataList(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "infoOrgCode") String infoOrgCode, @RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		List<ArcgisInfo> list = this.arcgisDataOfLocalService.getArcgisCorDataList(infoOrgCode, mapt);
		/*for (ArcgisInfo arcgisInfoOfPublic : list) {
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}*/
		return list;
	}

	/**
	 * 重点车辆概要信息
	 * @param session
	 * @param kvId
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/keyVehiclesInfo")
	public String keyVehiclesInfo(HttpSession session, @RequestParam(value = "kvId") Long kvId,
								  ModelMap map) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if (kvId == null || kvId == 0L)
			throw new Exception("缺少参数【id】！");
		KeyVehicles keyVehicles = keyVehiclesService.searchById(kvId);

		map.addAttribute("keyVehicles", keyVehicles);
		map.addAttribute("LZLY_DOMAIN", App.LZLY.getDomain(session));
		return "/map/arcgis/standardmappage/keyVehicles/keyVehiclesDetail.ftl";
	}

	private String formatElementsCollectionStr(String elementsCollectionStr, String modelType, String typeCode){
		System.out.println(elementsCollectionStr);
		String elementsCollectionStrType = elementsCollectionStr;
		if(StringUtils.isNotBlank(modelType)){
			if("keyVehicles".equals(modelType)){//重点车辆
				elementsCollectionStrType = elementsCollectionStrType
						.replaceAll("/images/map/gisv0/map_config/unselected/keyVehicles/mark_keyVehicles.png", "/images/map/gisv0/map_config/unselected/keyVehicles/mark_" + typeCode + ".png")
						.replaceAll("keyVehiclesLayer", "keyVehiclesLayer_"+typeCode);
			}
		}
		return elementsCollectionStrType;
	}

	@ResponseBody
	@RequestMapping(value = "/getArcgisGlobalEyesDataList")
	public List<ArcgisInfoOfPublic> getArcgisGlobalEyesDataList(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "infoOrgCode") String infoOrgCode, @RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisGlobalEyesDataList(infoOrgCode, mapt);
		/*for (ArcgisInfo arcgisInfoOfPublic : list) {
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}*/
		return list;
	}
}
