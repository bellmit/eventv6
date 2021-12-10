package cn.ffcs.zhsq.map.zhoubian.controller;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.shequ.utils.CollectionUtils;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.dubbo.rpc.RpcException;

import cn.ffcs.common.DictPcode;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.mobile.api.bo.Push;
import cn.ffcs.mobile.api.service.PushService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.GridAdmin;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.map.zhoubian.service.IZhouBianStatService;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 2014-05-16 liushi add
 * arcgis地图加载控制器
 * @author liushi
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/zhoubian/zhouBianStat")
public class ZhouBianStatController extends ZZBaseController {
	
	@Autowired
	private IZhouBianStatService zhouBianStatService;
	@Autowired
	private IDictionaryService dictionaryService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private IMenuConfigService menuConfigService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	@Autowired
	private PushService pushService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
		return "/map/arcgis/standardmappage/zhoubian/zhoubian_config.ftl";
	}
	
	@RequestMapping(value="/getZhouBianStatData")
	public String getKuangXuanStatData(HttpSession session, ModelMap map,HttpServletRequest request
			,@RequestParam(value="mapt") Integer mapt
			,@RequestParam(value="x") Double x
			,@RequestParam(value="y") Double y
			,@RequestParam(value="distance") Integer distance
			,@RequestParam(value="zhoubianTypeStr", required=false) String zhoubianTypeStr
			,@RequestParam(value="menuNameStr", required=false) String menuNameStr
			,@RequestParam(value="typeStr", required=false) String typeStr
			,@RequestParam(value="orgCode") String orgCode)throws Exception
	{
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long startGridId=Long.parseLong(defaultGridInfo.get(KEY_START_GRID_ID).toString());
		String infoOrgCode = (String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		String[] zhoubianTypes = zhoubianTypeStr.split(",");
		
		menuNameStr = java.net.URLDecoder.decode(menuNameStr, "utf-8");
		String[] menuNames = menuNameStr.split(",");
		Map<String, List<String>> result = new LinkedHashMap<String, List<String>>();;
		List<String> values = null;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resName", null);
		params.put("gridname", null);
		params.put("startGridId", startGridId);
		if(orgCode == null || "".equals(orgCode)) {
			orgCode=(String)defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		}
		params.put("infoOrgCode", orgCode);
		for(int i=0;i<zhoubianTypes.length;i++){
			params.put("x", x);
			params.put("y", y);
			params.put("mapType", mapt);
			params.put("distance", distance);
			params.put("zhoubianType", zhoubianTypes[i]);
			
			if("zhouBianStatOfPeopleService".equals(zhoubianTypes[i])) {
				String[] types = typeStr.split(",");
				
				for(int j=0;j<types.length;j++){
					params.put("type", types[j]);
					int resultCount = this.zhouBianStatService.statOfZhouBianCount(params);
					map.addAttribute(zhoubianTypes[i]+"Count"+types[j], resultCount);
					values = new ArrayList<String>();
					values.add(menuNames[i]);
					values.add(resultCount+"");
					result.put(zhoubianTypes[i], values);
				}
			} else {
				int resultCount = this.zhouBianStatService.statOfZhouBianCount(params);
				map.addAttribute(zhoubianTypes[i]+"Count", resultCount);
				values = new ArrayList<String>();
				values.add(menuNames[i]);
				values.add(resultCount+"");
				result.put(zhoubianTypes[i], values);
			}
		}
		map.addAttribute("x", String.valueOf(x));
		map.addAttribute("y", String.valueOf(y));
		map.addAttribute("distance", distance);
		map.addAttribute("mapType", mapt);
		map.addAttribute("result", result);
		return "/map/arcgis/standardmappage/zhoubian/zhoubian_config_info.ftl";
	}
	/**
	 * 2014-05-16 liushi add
	 * 获取当前网格信息以及对应的地图信息并转到对应的地图加载首页
	 * @param session
	 * @param request
	 * @param map
	 * @param arcgisUrlType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toMapArcgis")
	public String toMapArcgis(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="arcgisUrlType", required=false) String arcgisUrlType) throws Exception{
		String forward = "/map/arcgis/arcgis_base/arcgis_demo.ftl";
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		//获取当前登录人员所在网格信息
		if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
		map.put("gridId", gridInfo.getGridId());
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		return forward;
	}
	
	@RequestMapping(value = "/toZhouBianPage")
	public String toZhouBianPage(
			HttpSession session,
			HttpServletRequest request,
			ModelMap map,
			@RequestParam(value = "mapType", required = false) String mapType,
			@RequestParam(value = "distance", required = false) String distance,
			@RequestParam(value = "x", required = false) String x,
			@RequestParam(value = "y", required = false) String y,
			@RequestParam(value = "zhoubianType", required = false) String zhoubianType,
			@RequestParam(value = "homePageType", required = false) String homePageType,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "gridId", required = false) String gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if(elementsCollectionStr != null && !"".equals(elementsCollectionStr)) {
			map.put("elementsCollectionStr", elementsCollectionStr);
		}else {
			GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgByZBServiceName(infoOrgCode, homePageType, zhoubianType);
			if(gisDataCfg != null) {
				transGisDataCfgUrl(gisDataCfg, session);
				map.put("elementsCollectionStr", gisDataCfg.getElementsCollectionStr());
			}else {
				map.put("elementsCollectionStr", "");
			}
			
		}
		map.put("mapType", mapType);
		map.put("distance", distance);
		map.put("x", x);
		map.put("y", y);
		map.put("zhoubianType", zhoubianType);
		map.put("gridId", gridId);
		if (StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = userInfo.getInfoOrgCodeStr();
		}
		map.put("infoOrgCode", infoOrgCode);
		
		if("zhouBianStatOfGridAdminService".equals(zhoubianType)) {//网格员查询条件
			List<BaseDataDict> dutyDC = baseDictionaryService.getDataDictListOfSinglestage(DictPcode.GRID_ADMIN_DUTY, userInfo.getOrgCode());
			map.addAttribute("dutyDC", dutyDC);
		}else if("zhouBianStatOfFireHydrantService".equals(zhoubianType)) {//消防栓查询条件
			map.addAttribute("markType", "0601");
		}else if("zhouBianStatOfWaterSupplyCompanyService".equals(zhoubianType)) {//自来水公司查询条件
			map.addAttribute("markType", "0604");
		}else if("zhouBianStatOfNaturalWaterSourceService".equals(zhoubianType)) {//天然水源公司查询条件
			map.addAttribute("markType", "0603");
		}else if ("zhouBianStatOfFireTeamService".equals(zhoubianType)) {
			map.addAttribute("markType", "0701");
		}else if("zhouBianStatOfImportUnitService".equals(zhoubianType)){//重点单位
			List<Map<String, Object>> importUnitTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_IMPORT_UNIT, ConstantValue.COLUMN_IMPORT_UNITTYPE, userInfo.getOrgCode());
			map.addAttribute("importUnitTypeDC", importUnitTypeDC);
		}else if("zhouBianStatOfCampusService".equals(zhoubianType)){//校园周边
			map.addAttribute("markType", "0801");
		}
		
		if ("zhouBianStatOfXfsServic".equals(zhoubianType)) {// 消防栓
			map.put("resTypeCode", "02010601");
		} else if ("zhouBianStatOfWellLidService".equals(zhoubianType)) {// 井盖
			map.put("resTypeCode", "020107");
		} else if ("zhouBianStatOfStreetlightService".equals(zhoubianType)) {// 路灯
			map.put("resTypeCode", "02010501");
		} else if ("zhouBianStatOfGongShuiService".equals(zhoubianType)) {// 供水
			map.put("resTypeCode", "020201");
		} else if ("zhouBianStatOfGongReService".equals(zhoubianType)) {// 供热
			map.put("resTypeCode", "020202");
		} else if ("zhouBianStatOfRanQiService".equals(zhoubianType)) {// 燃气
			map.put("resTypeCode", "020203");
		} else if ("zhouBianStatOfBusStationService".equals(zhoubianType)) {// 公交站
			map.put("resTypeCode", "02020401");
		} else if ("zhouBianStatOfToiletService".equals(zhoubianType)) {// 公共厕所
			map.put("resTypeCode", "02010301");
		} else if ("zhouBianStatOfPoliceService".equals(zhoubianType)) {// 派出所
			map.put("resTypeCode", "020304");
		} else if ("zhouBianStatOfActiveFireTeamService".equals(zhoubianType)) {// 现役消防队
			map.put("markType", ConstantValue.MARKER_TYPE_FIRE_XF_XY);
		} else if ("zhouBianStatOfObligationFireTeamService".equals(zhoubianType)) {// 义务消防队
			map.put("markType", ConstantValue.MARKER_TYPE_FIRE_XF_YW);
		} else if ("zhouBianStatOfVolunteerFireTeamService".equals(zhoubianType)) {// 志愿消防队
			map.put("markType", ConstantValue.MARKER_TYPE_FIRE_XF_ZY);
		} else if ("zhouBianStatOfFirePoolService".equals(zhoubianType)) {// 消防水池
			map.put("markType", ConstantValue.MARKER_TYPE_FIRE_POOL);
		}
		String path = zhouBianStatService.getZhouBianPagePath(zhoubianType);
		String suffix = request.getParameter("suffix");
		if (StringUtils.isNotBlank(suffix)) {
			path = path.replace(".ftl", suffix + ".ftl");
		}
		return path;
	}
	public void transGisDataCfgUrl(GisDataCfg obj,HttpSession session) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
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
		}	
	}

	@ResponseBody
	@RequestMapping(value = "/queryZhouBianList", method = RequestMethod.POST)
	public EUDGPagination queryZhouBianList(
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "pageNo") int pageNo,
			@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "mapType", required = false) String mapType,
			@RequestParam(value = "distance", required = false) String distance,
			@RequestParam(value = "x", required = false) String x,
			@RequestParam(value = "y", required = false) String y,
			@RequestParam(value = "zhoubianType", required = false) String zhoubianType,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(StringUtils.isBlank(infoOrgCode)){
			infoOrgCode = this.getDefaultInfoOrgCode(session);
		}
		
		params.put("mapType", mapType);
		params.put("distance", distance);
		params.put("x", x);
		params.put("y", y);
		params.put("zhoubianType", zhoubianType);
		params.put("infoOrgCode", infoOrgCode);
		
		if ("zhouBianStatOfGridAdminService".equals(zhoubianType)) {//网格员查询条件
			String name = (String)request.getParameter("name");
			String duty = (String)request.getParameter("duty");
            String withoutPage = (String)request.getAttribute("withoutPage");
			params.put("name", name);
			params.put("duty", duty);
            params.put("withoutPage", withoutPage);
		} else if ("zhouBianStatOfKeyPlacesService".equals(zhoubianType)) {//重点场所
			String name = (String)request.getParameter("name");
			String address = (String)request.getParameter("address");
			params.put("name", name);
			params.put("address", address);
		} else if ("zhouBianStatOfFireTeamService".equals(zhoubianType)) {// 消防队
			String name = (String)request.getParameter("name");
			String address = (String)request.getParameter("address");
			params.put("name", name);
			params.put("address", address);
			params.put("markType", "0701");
		} else if ("zhouBianStatOfFireHydrantService".equals(zhoubianType)) {//消防栓查询条件
			String name = (String)request.getParameter("name");
			String address = (String)request.getParameter("address");
			String operateStatus = (String)request.getParameter("operateStatus");
			String gridName = (String)request.getParameter("gridName");
			params.put("name", name);
			params.put("address", address);
			params.put("operateStatus", operateStatus);
			params.put("gridName", gridName);
			params.put("markType", "'0601','0602'");
		} else if ("zhouBianStatOfWaterSupplyCompanyService".equals(zhoubianType)) {//自来水公司查询条件
			String name = (String)request.getParameter("name");
			String address = (String)request.getParameter("address");
			String operateStatus = (String)request.getParameter("operateStatus");
			String gridName = (String)request.getParameter("gridName");
			params.put("name", name);
			params.put("address", address);
			params.put("operateStatus", operateStatus);
			params.put("gridName", gridName);
			params.put("markType", "0604");
		} else if ("zhouBianStatOfBuildingService".equals(zhoubianType)) {//楼栋查询条件
			String name = (String)request.getParameter("name");
			String address = (String)request.getParameter("address");
			String operateStatus = (String)request.getParameter("operateStatus");
			String gridName = (String)request.getParameter("gridName");
			params.put("name", name);
			params.put("address", address);
			params.put("operateStatus", operateStatus);
			params.put("gridName", gridName);
			params.put("markType", "0604");
		} else if("zhouBianStatOfNaturalWaterSourceService".equals(zhoubianType)) {//天然水源公司查询条件
			String name = (String)request.getParameter("name");
			String address = (String)request.getParameter("address");
			String operateStatus = (String)request.getParameter("operateStatus");
			String gridName = (String)request.getParameter("gridName");
			params.put("name", name);
			params.put("address", address);
			params.put("operateStatus", operateStatus);
			params.put("gridName", gridName);
			params.put("markType", "0603");
		}else if("zhouBianStatOfCampusService".equals(zhoubianType)) {//校园周边查询条件
			String name = (String)request.getParameter("name");
			String address = (String)request.getParameter("address");
			String gridName = (String)request.getParameter("gridName");
			params.put("name", name);
			params.put("address", address);
			params.put("gridName", gridName);
			params.put("markType", "0801");
		}else if("zhouBianStatOfImportUnitService".equals(zhoubianType)) {//重点单位
			String name = request.getParameter("name");
			String address = request.getParameter("address");
			String type = request.getParameter("type");
			
			if(StringUtils.isNotBlank(name)){
				params.put("name", name);
			}
			if(StringUtils.isNotBlank(address)){
				params.put("address", address);
			}
			if(StringUtils.isNotBlank(type)){
				params.put("type", type);
			}
		}else if("zhouBianStatOfGlobalEyesService".equals(zhoubianType)) {//全球眼查询条件
			String name = (String)request.getParameter("name");
			params.put("name", name);
		}else if("zhouBianStatOfRentRoomService".equals(zhoubianType)) {//出租屋查询条件
			String name = (String)request.getParameter("name");
			String address = (String)request.getParameter("address");
			String gridName = (String)request.getParameter("gridName");
			params.put("name", name);
			params.put("address", address);
			params.put("gridName", gridName);
		}else if("zhouBianStatOfNonPublicOrgService".equals(zhoubianType)) {//新经济组织查询条件
			String name = (String)request.getParameter("name");
			String address = (String)request.getParameter("address");
			String gridName = (String)request.getParameter("gridName");
			params.put("name", name);
			params.put("address", address);
			params.put("gridName", gridName);
		}else if("zhouBianStatOfOrganizationService".equals(zhoubianType)) {//出租屋查询条件
			String name = (String)request.getParameter("name");
			String address = (String)request.getParameter("address");
			String gridName = (String)request.getParameter("gridName");
			params.put("name", name);
			params.put("address", address);
			params.put("gridName", gridName);
		} else if ("zhouBianStatOfCorBaseService".equals(zhoubianType)) { // 企业
			String name = (String)request.getParameter("name");
			String address = (String)request.getParameter("address");
			params.put("name", name);
			params.put("address", address);
		} else if ("zhouBianStatOfPartyService".equals(zhoubianType)) { // 党员
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("zhouBianStatOfXdsService".equals(zhoubianType)) { // 吸毒
			String name = (String)request.getParameter("name");
			String order = (String)request.getParameter("order");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
			
			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("zhouBianStatOfCorrectionalService".equals(zhoubianType)) { // 矫正
			String name = (String)request.getParameter("name");
			String order = (String)request.getParameter("order");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
			
			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("zhouBianStatOfReleasedService".equals(zhoubianType)) { // 刑释解教
			String name = (String)request.getParameter("name");
			String order = (String)request.getParameter("order");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
			
			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("zhouBianStatOfPetitionService".equals(zhoubianType)) { // 信访
			String name = (String)request.getParameter("name");
			String order = (String)request.getParameter("order");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
			
			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("zhouBianStatOfPetitionService".equals(zhoubianType)) { // 信访
			String name = (String)request.getParameter("name");
			String order = (String)request.getParameter("order");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
			
			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("zhouBianStatOfPetitionRecordService".equals(zhoubianType)) { // 上访
			String name = (String) request.getParameter("name");
			String order = (String) request.getParameter("order");

			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}

			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("zhouBianStatOfMentalService".equals(zhoubianType)) { // 重精神病
			String name = (String)request.getParameter("name");
			String order = (String)request.getParameter("order");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
			
			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		} else if ("zhouBianStatOfDangrousService".equals(zhoubianType)) { // 危险品从业人员
			String name = (String)request.getParameter("name");
			String order = (String)request.getParameter("order");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
			
			if (StringUtils.isNotBlank(order)) {
				params.put("order", order.trim());
			}
		}else if ("zhouBianStatOfXfsService".equals(zhoubianType)) {//消防栓
			String name = (String)request.getParameter("name");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		}else if ("zhouBianStatOfWellLidService".equals(zhoubianType)) {//井盖
			String name = (String)request.getParameter("name");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		}else if ("zhouBianStatOfStreetlightService".equals(zhoubianType)) {//路灯
			String name = (String)request.getParameter("name");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		}else if ("zhouBianStatOfGongShuiService".equals(zhoubianType)) {//供水
			String name = (String)request.getParameter("name");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		}else if ("zhouBianStatOfGongReService".equals(zhoubianType)) {//供热
			String name = (String)request.getParameter("name");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		}else if ("zhouBianStatOfRanQiService".equals(zhoubianType)) {//燃气
			String name = (String)request.getParameter("name");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		}
		else if ("zhouBianStatOfBusStationService".equals(zhoubianType)) {//公交站
			String name = (String)request.getParameter("name");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		}else if ("zhouBianStatOfToiletService".equals(zhoubianType)) {//公共厕所
			String name = (String)request.getParameter("name");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		}else if ("zhouBianStatOfPoliceService".equals(zhoubianType)) {//派出所
			String name = (String)request.getParameter("name");
			
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		}else if ("zhouBianStatOfActiveFireTeamService".equals(zhoubianType)) {// 现役消防队
			String name = (String)request.getParameter("name");
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		}else if ("zhouBianStatOfObligationFireTeamService".equals(zhoubianType)) {// 义务消防队
			String name = (String)request.getParameter("name");
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		}else if ("zhouBianStatOfVolunteerFireTeamService".equals(zhoubianType)) {// 志愿消防队
			String name = (String)request.getParameter("name");
			if (StringUtils.isNotBlank(name)) {
				params.put("name", name.trim());
			}
		}else if ("zhouBianStatOfFirePoolService".equals(zhoubianType)) {// 志愿消防队
			String name = (String)request.getParameter("name");
			String address = (String)request.getParameter("address");
			params.put("name", name);
			params.put("address", address);
		}else if ("zhouBianStatOfFirePoolService".equals(zhoubianType)) {// 志愿消防队
			String name = (String)request.getParameter("name");
			String address = (String)request.getParameter("address");
			params.put("name", name);
			params.put("address", address);
		}
		return zhouBianStatService.statOfZhouBianList(pageNo, pageSize, params);
	}

	/**
	 * 根据距离查询网格员按距离从近到远排列
	 * @param session
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryZhouBianList4Jsonp")
	public String queryZhouBianList4Jsonp(HttpSession session,HttpServletRequest request) {
		Map<String, Object> mapResult=new HashMap<String, Object>();
		Map<String, Object> params = super.getParameterMap(request);
		int pageNo=0;
		int pageSize=0;
		if(params.get("pageNo")!=null){
			pageNo=Integer.valueOf(params.get("pageNo").toString());
		}
		if(params.get("pageSize")!=null){
			pageSize=Integer.valueOf(params.get("pageSize").toString());
		}
		String withoutPage = null;
		if(pageNo == 0 || pageSize == 0){
			request.setAttribute("withoutPage",withoutPage="1");
		}
		String mapType = "5";
		if(CommonFunctions.isNotBlank(params,"mapType")){
			mapType = params.get("mapType").toString();
		}
		String distance = null;
		if(CommonFunctions.isNotBlank(params,"distance")){
			distance = params.get("distance").toString();
		}
		String x = null;
		if(CommonFunctions.isNotBlank(params,"x")){
			x = params.get("x").toString();
		}
		String y = null;
		if(CommonFunctions.isNotBlank(params,"y")){
			y = params.get("y").toString();
		}
		String zhoubianType = "zhouBianStatOfGridAdminService";
		if(CommonFunctions.isNotBlank(params,"zhoubianType")){
			zhoubianType = params.get("zhoubianType").toString();
		}
		String infoOrgCode = null;
		if(CommonFunctions.isNotBlank(params,"infoOrgCode")){
			infoOrgCode = params.get("infoOrgCode").toString();
		}else{
			infoOrgCode = super.getDefaultInfoOrgCode(session);
		}
		EUDGPagination pagination = this.queryZhouBianList(session, request, pageNo, pageSize, mapType, distance, x, y, zhoubianType, infoOrgCode);
		List<Map<String,Object>> resultList = new ArrayList<>();
		if (withoutPage == null) {
			if(pagination!=null && !CollectionUtils.isEmpty(pagination.getRows())){
				for (Map rowMap : (List<Map>)pagination.getRows()) {
					Map<String,Object> tempMap = new HashMap<>();
					tempMap.put("photo",rowMap.get("PHOTO"));
					tempMap.put("bizType",rowMap.get("IS_ONLINE"));
					tempMap.put("gridAdminId",rowMap.get("GRID_ADMIN_ID"));
					tempMap.put("partyName2",rowMap.get("PARTY_NAME2"));
					tempMap.put("partyName",rowMap.get("PARTY_NAME"));
					tempMap.put("gridName",rowMap.get("GRID_NAME"));
					tempMap.put("mobileTelephone",rowMap.get("MOBILE_TELEPHONE"));
					tempMap.put("distance",rowMap.get("DISTANCE"));
					tempMap.put("x",rowMap.get("LONGITUDE"));
					tempMap.put("y",rowMap.get("DIMENSIONS"));
					tempMap.put("z",rowMap.get("HEIGHT"));
					resultList.add(tempMap);
				}
				pagination.setRows(resultList);
			}
			mapResult.put("result", pagination);
		}else{
			if(pagination!=null && !CollectionUtils.isEmpty(pagination.getRows())){
				for (Map<String,Object> rowMap : (List<Map<String,Object>>)pagination.getRows()) {
					Map<String,Object> tempMap = new HashMap<>();
					tempMap.put("gridAdminId",rowMap.get("GRID_ADMIN_ID"));
					tempMap.put("bizType",rowMap.get("IS_ONLINE"));
					tempMap.put("partyName2",rowMap.get("PARTY_NAME2"));
					tempMap.put("x",rowMap.get("LONGITUDE"));
					tempMap.put("y",rowMap.get("DIMENSIONS"));
					resultList.add(tempMap);
				}
			}
			mapResult.put("result", resultList);
		}
		String jsoncallback = request.getParameter("jsoncallback");
		jsoncallback=jsoncallback+"("+ JsonHelper.getJsonString(mapResult)+")";
		return jsoncallback;
	}


	/**
	 * 跳转信息发送页面
	 * @param session
	 * @param bizType			消息业务类型
	 * 			00	通用
	 * 			01	网格员
	 * 			02	群防群治队伍
	 * @param gridAdmin
	 * 			gridAdminId		网格员id
	 * 			userId			网格员绑定的用户id
	 * 			partyName		网格员姓名
	 * 			mobileTelephone	工作手机号码
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toSendMsg")
	public String toSendMsg(HttpSession session, HttpServletRequest request,
			@RequestParam(value="bizType") String bizType,
			@ModelAttribute GridAdmin gridAdmin,
			ModelMap map) {
		String x = request.getParameter("x");
		String y = request.getParameter("y");
		String mapType = request.getParameter("mapType");
		String distance = request.getParameter("distance");

		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("infoOrgCode", getDefaultInfoOrgCode(session));
		
		map.addAttribute("bizType", bizType);
		map.addAttribute("gridAdmin", gridAdmin);

		map.addAttribute("mapType", mapType);
		map.addAttribute("distance", distance);
		map.addAttribute("x", x);
		map.addAttribute("y", y);
		
		return "/map/arcgis/standardmappage/sendMsg/sendMsg"+ bizType +".ftl";
	}
	
	/**
	 * 发送短信
	 * @param session
	 * @param sendType	默认为0
	 * 			0  异步发送
	 * 			1 同步发送
	 * @param params
	 * 			msgContent	短信内容
	 * 			phoneNums	短信接收手机号码，以英文逗号分隔
	 * @param map
	 * @return
	 * 		result	发送结果，true为发送成功，异步发送永远为true
	 * 		msg		异常信息
	 */
	@ResponseBody
	@RequestMapping(value="/sendSms")
	public Map<String, Object> sendSms(HttpSession session, 
			@RequestParam(value="sendType", required = false, defaultValue = "0") int sendType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		String msgContent = "",
			   phoneNums = "",
			   msgWrong = "";
		boolean result = true;
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(CommonFunctions.isNotBlank(params, "msgContent")) {
			msgContent = params.get("msgContent").toString();
		} else {
			result = false;
			msgWrong = "短信内容为空！";
		}
		if(CommonFunctions.isNotBlank(params, "phoneNums")) {
			phoneNums = params.get("phoneNums").toString();
		} else {
			result = false;
			msgWrong = "接收短信手机号码为空！";
		}
		
		if(result) {
			switch(sendType) {
				case 0: {//异步发送
					eventDisposalWorkflowService.sendSms(null, phoneNums, msgContent, userInfo);
					
					break;
				}
				case 1: {//同步发送
					result = eventDisposalWorkflowService.sendSmsSyn(null, phoneNums, msgContent, userInfo);
					
					break;
				}
			}
		}
		
		if(StringUtils.isNotBlank(msgWrong)) {
			resultMap.put("msg", msgWrong);
		}
		
		resultMap.put("result", result);
		
		return resultMap;
	}
	
	/**
	 * 向手机端推送消息
	 * @param session
	 * @param jsoncallback
	 * @param params
	 * 			msgContent	消息内容
	 * 			userIds		消息接收人员id，以英文逗号分隔
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/pushMsg2Mobile")
	public String pushMsg2Mobile(HttpSession session, 
			@RequestParam(value = "jsoncallback", required = false) String jsoncallback,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String msgContent = "",
			   msgUrl = "",
			   userIds = "",
			   msgWrong = "",
			   MOBILE_PUSH_TYPE = "2",//1：web，2：安卓
			   isSendMsg2MobileStr = funConfigurationService.turnCodeToValue(ConstantValue.SEND_MSG_AND_SMS_CFG, ConstantValue.SEND_MSG_AND_SMS_CFG_TRIGGER_4_MOBILE_MSG, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		Set<Long> userIdSet = new HashSet<Long>();
		List<Long> userIdList = new ArrayList<Long>();
		List<Push> push2MobileList = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean result = true,
				isSendMsg2Mobile = Boolean.valueOf(isSendMsg2MobileStr);
		
		if(isSendMsg2Mobile) {//开启消息推送功能后，继续后续的操作
			if(CommonFunctions.isNotBlank(params, "msgContent")) {
				try {
					msgContent = java.net.URLDecoder.decode(params.get("msgContent").toString(), "utf-8").trim();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if(CommonFunctions.isNotBlank(params, "msgUrl")) {
				msgUrl = params.get("msgUrl").toString();
			}
			if(CommonFunctions.isNotBlank(params, "userIds")) {
				userIds = params.get("userIds").toString();
				
				if(StringUtils.isNotBlank(userIds)) {
					String[] userIdArray = userIds.split(",");
					Long userId = -1L;
					
					for(String userIdStr : userIdArray) {
						try {
							userId = Long.valueOf(userIdStr);
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
						
						if(userId > 0) {
							userIdSet.add(userId);
						}
					}
					
					if(userIdSet.size() > 0) {
						push2MobileList = new ArrayList<Push>();
						String msgTitle = msgContent;
						if(StringUtils.isNotBlank(msgTitle) && msgTitle.length() > 7) {
							msgTitle = msgTitle.substring(0, 7);
						}
						
						for(Long _userId : userIdSet) {
							Push mobilePush = new Push();
							
							mobilePush.setUserId(_userId.toString());
							mobilePush.setTitle(msgTitle);
							mobilePush.setContent(msgContent);
							
							if(StringUtils.isNotBlank(msgUrl)) {
								mobilePush.setUrl(msgUrl);
								mobilePush.setType(MOBILE_PUSH_TYPE);//url不为空时，需要传递该属性
							}
							
							push2MobileList.add(mobilePush);
							
							userIdList.add(_userId);
						}
					}
				}
			}
			
			if(userIdList.size() > 0) {
				List<Push> resultPushList = null;
				
				if(push2MobileList != null && push2MobileList.size() > 0) {
					try {
						resultPushList = pushService.pushByUserId(push2MobileList);
					} catch(RpcException e) {
						result = false;
						e.printStackTrace();
					}
				}
				
				if(resultPushList != null && resultPushList.size() > 0) {
					result = false;
					msgWrong = "手机消息推送失败！";
					
					logger.error("手机消息推送错误，begin：==============================================================================");
					
					for(Push resultPush : resultPushList) {
						
						logger.error(resultPush.getUserId() + "  " + resultPush.getResult() + "  " + resultPush.getFailureReason());
					}
					
					logger.error("手机消息推送错误，end：==============================================================================");
				}
			} else {
				result = false;
				msgWrong = "没有可推送手机消息的人员！";
			}
		} else {
			logger.warn("手机消息推送功能未开启！功能编码为：" + ConstantValue.SEND_MSG_AND_SMS_CFG + "  触发条件为：" + ConstantValue.SEND_MSG_AND_SMS_CFG_TRIGGER_4_MOBILE_MSG);
		}
		
		resultMap.put("result", result);
		resultMap.put("msg", msgWrong);
		
		if(StringUtils.isNotBlank(jsoncallback)) {
			jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsoncallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsoncallback;
	}

	@ResponseBody
	@RequestMapping(value = "/queryZhouBianByBiztypeList", method = RequestMethod.POST)
	public List<ArcgisInfoOfPublic> queryZhouBianByBiztypeList(
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "mapType", required = false) String mapType,
			@RequestParam(value = "distance", required = false) String distance,
			@RequestParam(value = "x", required = false) String x,
			@RequestParam(value = "y", required = false) String y,
			@RequestParam(value = "zhoubianTypeStr", required = false) String zhoubianTypeStr,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		if(StringUtils.isBlank(infoOrgCode)){
			infoOrgCode = this.getDefaultInfoOrgCode(session);
		}
		if(StringUtils.isNotBlank(zhoubianTypeStr)){
			String[] zhoubianTypeArr = zhoubianTypeStr.split(",");
			if(zhoubianTypeArr != null && zhoubianTypeArr.length > 0){
				for (int i=0;i<zhoubianTypeArr.length;i++){
					List<ArcgisInfoOfPublic> arcgisInfoOfPublics = new ArrayList<ArcgisInfoOfPublic>();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("mapType", mapType);
					params.put("distance", distance);
					params.put("x", x);
					params.put("y", y);
					params.put("infoOrgCode", infoOrgCode);
					params.put("zhoubianType", zhoubianTypeArr[i]);
					params.put("elementsCollectionStr", elementsCollectionStr);
					arcgisInfoOfPublics = zhouBianStatService.statOfZhouBianMapInfoList(params);
					if(arcgisInfoOfPublics != null && arcgisInfoOfPublics.size()>0){
						list.addAll(arcgisInfoOfPublics);
					}
				}
			}
		}

		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getZhouBianResourceTypeList", method = RequestMethod.POST)
	public List<BaseDataDict> queryZhouBianResourceList(HttpSession session){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String orgCode =userInfo.getOrgCode();
		return  baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.RESOURCE_RANGE_PCODE, orgCode);
	}
}
