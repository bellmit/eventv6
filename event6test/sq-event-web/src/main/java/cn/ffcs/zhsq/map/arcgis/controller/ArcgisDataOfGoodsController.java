package cn.ffcs.zhsq.map.arcgis.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import cn.ffcs.doorsys.bo.equipment.AmsLog;
import cn.ffcs.doorsys.bo.equipment.Equipment;
import cn.ffcs.doorsys.service.equipment.IAcsLogService;
import cn.ffcs.doorsys.service.equipment.IAmsLogService;
import cn.ffcs.doorsys.service.equipment.IApsLogService;
import cn.ffcs.doorsys.service.equipment.IEquipmentService;
import cn.ffcs.doorsys.service.record.ICarRecordService;
import cn.ffcs.doorsys.service.record.IResidentRecordService;
import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.gmis.landscapeManage.service.ILandscapeManageService;
import cn.ffcs.gmis.mybatis.domain.prvetionTeam.PrvetionTeam;
import cn.ffcs.gmis.prvetionTeam.service.IPrvetionTeamService;
import cn.ffcs.shequ.crypto.HashIdManager;
import cn.ffcs.shequ.grid.domain.db.StoreInfo;
import cn.ffcs.shequ.grid.service.IStoreInfoService;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.firegrid.FireResource;
import cn.ffcs.shequ.mybatis.domain.zzgl.firegrid.FireTeam;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PlaceInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResType;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.UrbanObj;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.firegrid.IFireResourceService;
import cn.ffcs.shequ.zzgl.service.firegrid.IFireTeamService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IPlaceInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResTypeService;
import cn.ffcs.shequ.zzgl.service.res.IRoadManagementService;
import cn.ffcs.shequ.zzgl.service.res.IUrbanObjService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.CoordinateInverseQuery.service.ICoordinateInverseQueryService;
import cn.ffcs.zhsq.faithfulEnterprise.service.FaithfulEnterpriseService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.mybatis.domain.CoordinateInverseQuery.CoordinateInverseQueryGridInfo;
import cn.ffcs.zhsq.mybatis.domain.faithfulEnterprise.FaithfulEnterprise;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfo;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfHashId;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.mybatis.domain.trajectory.LocateInfo;
import cn.ffcs.zhsq.mybatis.domain.wellKnownTrademark.Trademark;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.JsonUtils;
import cn.ffcs.zhsq.utils.NpObjUtil;
import cn.ffcs.zhsq.utils.Ognl;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.gis.Gps;
import cn.ffcs.zhsq.utils.gis.GpsUtil;
import cn.ffcs.zhsq.utils.hikvision.ThirdBayonetServiceStub;
import cn.ffcs.zhsq.utils.hikvision.ThirdBayonetServiceStub.InitSystemResponse;
import cn.ffcs.zhsq.utils.hikvision.ThirdBayonetServiceStub.SearchVehicleInfoResponse;
import cn.ffcs.zhsq.wellKnownTrademark.service.TrademarkService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 2014-07-09 chenlf add
 * arcgis "物" 定位数据加载控制器
 * @author chenlf
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisdataofgoods")
public class ArcgisDataOfGoodsController extends ZZBaseController {

	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IResInfoService resInfoService;
	@Autowired
	private IResTypeService resTypeService;
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IFireResourceService fireResourceService;
	@Autowired
	private IFireTeamService fireTeamService;
	@Autowired
	private IPlaceInfoService placeInfoService;
	@Autowired
	private IPrvetionTeamService prvetionTeamService;
	@Autowired
	private IStoreInfoService storeInfoService;//门店
	@Autowired
	private IAttachmentService attachmentService;
	@Autowired
	private ILandscapeManageService landscapeManageService;
	@Autowired
	private IRoadManagementService roadManagementService;
	@Autowired
	private ICarRecordService carRecordService;
	@Autowired
	private IResidentRecordService residentRecordService;
	@Autowired
	private IEquipmentService equipmentService;//海康 设备信息
	@Autowired
	private IAmsLogService amsLogService;//报警日志 接口
	@Autowired
	private IAcsLogService acsLogService;//门禁设备日志
	@Autowired
	private IApsLogService apsLogService;//海康 设备信息 车闸记录
	@Autowired
	private IUrbanObjService urbanObjService;
	@Autowired
	private TrademarkService trademarkService;//驰名商标
	@Autowired
	private FaithfulEnterpriseService enterpriseService;//守重企业
	@Autowired
	private ICoordinateInverseQueryService coordinateInverseQueryService;//获取网格服务
	@Autowired
	private FileUploadService fileUploadService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	///---资源 start----------------------------------------------
	/**
	 * 综治.市政设施（资源）列表
	 * @param session
	 * @param gridId 网格ID
	 * @param typeCode 资源类型
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfResource")
	public String index(HttpSession session, ModelMap map
			, @RequestParam(value="gridId") Long gridId
			, @RequestParam(value="resTypeId", required=false) Long resTypeId
			, @RequestParam(value="typeCode", required=false) String typeCode
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		if (StringUtils.isNotBlank(typeCode)) {
			map.addAttribute("gridId", gridId);
			ResType resType = resInfoService.findResTypeByTypeCode(typeCode);
			map.addAttribute("resType", resType);
			map.addAttribute("resTypeId", resType.getResTypeId());
			map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		} else {
			map.addAttribute("gridId", gridId);
			map.addAttribute("resTypeId", resTypeId);
			ResType resType = resTypeService.getResTypeById(resTypeId);
			map.addAttribute("resType", resType);
			map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		}

		return "/map/arcgis/standardmappage/standardResource.ftl";
	}

	@RequestMapping(value = "/toMunicipalResource")
	public String toMunicipalResource(HttpSession session, ModelMap map, @RequestParam(value = "gridId") Long gridId,
									  @RequestParam(value = "resTypeCode") String resTypeCode,
									  @RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		map.addAttribute("gridId", gridId);
		ResType resType = resInfoService.findResTypeByTypeCode(resTypeCode);
		map.addAttribute("resTypeId", resType.getResTypeId());
		map.addAttribute("resType", resType);
		map.addAttribute("resTypeCode", resTypeCode);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		if ("020101".equals(resType.getTypeCode())) {
			return "/map/arcgis/standardmappage/standardRoadResource.ftl";
		}
		return "/map/arcgis/standardmappage/standardRoadResource.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/municipalResourceListData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination municipalResourceListData(HttpSession session, @RequestParam(value = "page") int page,
													@RequestParam(value = "rows") int rows,
													@RequestParam(value = "resTypeId") Long resTypeId,
													@RequestParam(value = "resTypeCode") String resTypeCode,
													@RequestParam(value = "gridId") Long gridId,
													@RequestParam(value = "resName", required = false) String resName,
													@RequestParam(value = "gridname", required = false) String gridname) {
		if (page <= 0)
			page = 1;
		if (resName != null)
			resName = resName.trim();
		if (gridname != null)
			gridname = gridname.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		if ("020101".equals(resTypeCode)) {

		}
		params.put("roadName", resName);
		params.put("startGridId", gridId);
		params.put("resTypeId", resTypeId);
		cn.ffcs.common.EUDGPagination pagination = roadManagementService.findRoadManagementPagination(page, rows, params);
		return pagination;
	}

	@ResponseBody
	@RequestMapping(value = "/getRoadMarkersList")
	public List<ArcgisInfoOfPublic> getRoadMarkersList(HttpSession session, ModelMap map, HttpServletResponse res,
													   @RequestParam(value = "ids") String ids,
													   @RequestParam(value = "resTypeCode") String resTypeCode,
													   @RequestParam(value = "mapt", required = false) Integer mapt,
													   @RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.findRoadMarkersByIds(ids, mapt, resTypeCode);
		for (ArcgisInfoOfPublic arcgisInfoOfPublic : list) {
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}

	///---消防栓 start----------------------------------------------
	/**
	 * 综治.市政设施（资源）列表
	 * @param session
	 * @param gridId 网格ID
	 * @param typeCode 资源类型
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/resource")
	public String resource(HttpSession session, @RequestParam(value="gridId") Long gridId, @RequestParam(value="resTypeId") Long resTypeId, ModelMap map) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("resTypeId", resTypeId);
		ResType resType = resTypeService.getResTypeById(resTypeId);
		map.addAttribute("resType", resType);
		return "/map/arcgis/standardmappage/taijiang/resource.ftl";
	}

	/**
	 * 综治.市政设施（资源），数据列表 
	 * @param session
	 * @param page
	 * @param rows
	 * @param resTypeId 资源类型
	 * @param startGridId 起始网格ID
	 * @param resName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/resourceListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination listData(HttpSession session
			, @RequestParam(value="page") int page
			, @RequestParam(value="rows") int rows
			, @RequestParam(value="resTypeId", required=false) Long resTypeId
			, @RequestParam(value="gridId", required=false) Long gridId
			, @RequestParam(value="resName", required=false) String resName
			, @RequestParam(value="gridname", required=false) String gridname
			, @RequestParam(value="typeCode", required=false) String typeCode
			, @RequestParam(value="orgCode", required=false) String orgCode) {
		if(page<=0) page=1;
		if(resName!=null) resName = resName.trim();
		if(gridname!=null) gridname = gridname.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resName", resName);
		params.put("gridname", gridname);
		params.put("resTypeId", resTypeId);
		params.put("startGridId", gridId);
		params.put("typeCode", typeCode);
		params.put("orgCode", orgCode);
		cn.ffcs.common.EUDGPagination pagination = resInfoService.findResInfoPagination(page, rows, params);
		return pagination;
	}
	
	@ResponseBody
	@RequestMapping(value="/resourceListDataJsonp")
	public String resourceListDataJsonp( HttpServletRequest request, HttpSession session
			, @RequestParam(value="page") int page
			, @RequestParam(value="rows") int rows
			, @RequestParam(value="resTypeId", required=false) Long resTypeId
			, @RequestParam(value="gridId", required=false) Long gridId
			, @RequestParam(value="resName", required=false) String resName
			, @RequestParam(value="gridname", required=false) String gridname
			, @RequestParam(value="typeCode", required=false) String typeCode
			, @RequestParam(value="orgCode", required=false) String orgCode) {
		String jsoncallback = request.getParameter("jsoncallback");
		cn.ffcs.common.EUDGPagination pagination = this.listData(session, page, rows, resTypeId, gridId, resName, gridname, typeCode, orgCode);
		jsoncallback = jsoncallback + "(" + JsonUtils.listToJson(pagination.getRows()) + ")";
		return jsoncallback;
	}

	/**
	 * 2014-07-8 chenlf add
	 * 获取资源的定位信息
	 * @param session
	 * @param ids 资源ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfResource")
	public List<ArcgisInfoOfPublic> getArcgisResourceLocateDataList(HttpSession session, ModelMap map,
			HttpServletResponse res, @RequestParam(value = "ids") String ids,
			@RequestParam(value = "resTypeCode") String resTypeCode,
			@RequestParam(value = "mapt", required = false) Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();

		if (StringUtils.isNotBlank(ids)) {
			// 市政消防栓是根据楼宇关联的
			if (StringUtils.isNotBlank(resTypeCode) && ConstantValue.SHIZHENG_XIAOFANGSHUAN.equals(resTypeCode)) {
				list = this.arcgisDataOfLocalService.getBuildLocateDataListByResIds(ids, mapt);
			} else {
				list = this.arcgisDataOfLocalService.getArcgisResourceLocateDataListByIds(ids, mapt, resTypeCode);
			}
			if (StringUtils.isNotBlank(elementsCollectionStr)) {
				for (ArcgisInfoOfPublic arcgisInfoOfPublic : list) {
					arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
				}
			}
		}
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getResourceGisDataForJsonp")
	public String getResourceGisDataForJsonp(HttpServletRequest request, HttpSession session, ModelMap map,
			HttpServletResponse res, @RequestParam(value = "ids") String ids,
			@RequestParam(value = "resTypeCode") String resTypeCode,
			@RequestParam(value = "mapt", required = false) Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		String jsoncallback = request.getParameter("jsoncallback");
		List<ArcgisInfoOfPublic> list = this.getArcgisResourceLocateDataList(session, map, res, ids, resTypeCode, mapt,
				elementsCollectionStr);
		jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(list) + ")";
		return jsoncallback;
	}

	/**
	 * 单条资源数据的查看
	 * @param session
	 * @param resId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/resourceDetail")
	public String showResourceDetail(HttpSession session, @RequestParam(value="resId") Long resId, ModelMap map) {
		ResInfo resInfo = resInfoService.findResInfoById(resId);
		map.addAttribute("resInfo", resInfo);
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(resInfo.getGridId(), false);
		Long gridId = gridInfo.getGridId();
		String infoOrgCode=gridInfo.getInfoOrgCode();

		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
		}

		map.addAttribute("gridNames", gridNames);

		return "/map/arcgis/standardmappage/resourceDetail.ftl";
	}
	///---资源 end----------------------------------------------

	@RequestMapping(value="/toArcgisDataListOfCarResource")
	public String carResourceindex(HttpSession session,
								   @RequestParam(value="orgCode") String orgCode,
								   HttpServletRequest request,
								   @RequestParam(value="gridId") Long gridId,
								   @RequestParam(value="resTypeId", required=false) Integer resTypeId, ModelMap map,
								   @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
								   @RequestParam(value="standard", required=false) String standard) {
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("gridId", gridId);
		map.addAttribute("resTypeId", resTypeId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		String markType = "";
		if(resTypeId==227){
			markType = "02011102";//清扫保洁车
		}else if(resTypeId==203){
			markType = "02011101";//市政维护车辆
		}else if(resTypeId==224){
			markType = "02011103"; //市政巡逻车
		}
		map.addAttribute("markType", markType);
		return "/map/arcgis/standardmappage/standardCarResource.ftl";
	}

	@ResponseBody
	@RequestMapping(value="/carResourceListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination carResourceListData(HttpSession session,
											  @RequestParam(value="page") int page,
											  @RequestParam(value="rows") int rows,
											  @RequestParam(value="resTypeId", required=false) int resTypeId,
											  @RequestParam(value="orgCode") String orgCode,
											  @RequestParam(value="resno", required=false) String resno,
											  @RequestParam(value="resName", required=false) String resName,
											  @RequestParam(value="gridId") Long gridId,
											  @RequestParam(value="address", required=false) String address,
											  @RequestParam(value="gridName", required=false) String gridName,
											  @RequestParam(value="operateStatus", required=false) String operateStatus,
											  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		if(page<=0) page=1;
		if(resName!=null) resName = resName.trim();
//		if(gridname!=null) gridname = gridname.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resName", resName);
		params.put("resno", resno);
		params.put("resTypeId", resTypeId);
		params.put("startGridId", gridId);
		cn.ffcs.common.EUDGPagination pagination = resInfoService.findResInfoPagination(page, rows, params);
		return pagination;
	}

	/**
	 * 综治.新消防栓/天然水源、自来水公司列表
	 * @param session
	 * @param gridId 网格ID
	 * @param typeCode 资源类型
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfNewResource")
	public String newResourceindex(HttpSession session,
								   @RequestParam(value="orgCode") String orgCode,
								   @RequestParam(value="resTypeId", required=false) Integer resTypeId, ModelMap map,
								   @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
								   @RequestParam(value="standard", required=false) String standard) {
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("resTypeId", resTypeId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		String markType = "";
		if(elementsCollectionStr!=null||"".equals(elementsCollectionStr)){
			String menuCode = "";
			menuCode = this.analysisOfElementsCollection(elementsCollectionStr,"menuCode");
			if("newFireHydrant".equals(menuCode)) {
				markType = "0601";
			}else if("naturalWaterSource".equals(menuCode)){
				markType = "0603";
			}else if("waterSupplyCompany".equals(menuCode)) {
				markType = "0604";
			}else if("firePool".equals(menuCode)) { // 消防水池
				markType = "0605";
			}

		}else {
			if(resTypeId==0 || resTypeId==1 || resTypeId==2){
				markType = "0601";//室内/外消防栓
			}else if(resTypeId==3){
				markType = "0603";//天然水源
			}else if(resTypeId==4){
				markType = "0604"; //自来水公司
			}else if(resTypeId==4){
				markType = "0605"; //消防水池
			}
		}
		map.addAttribute("markType", markType);
		return "/map/arcgis/standardmappage/standardNewResource.ftl";
	}

	/**
	 * 综治.新消防栓/天然水源、自来水公司，数据列表 
	 * @param session
	 * @param page
	 * @param rows
	 * @param resTypeId 资源类型
	 * @param startGridId 起始网格ID
	 * @param resName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/newResourceListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination newResourceListData(HttpSession session,
											  @RequestParam(value="page") int page,
											  @RequestParam(value="rows") int rows,
											  @RequestParam(value="resTypeId", required=false) int resTypeId,
											  @RequestParam(value="orgCode") String orgCode,
											  @RequestParam(value="resName", required=false) String resName,
											  @RequestParam(value="address", required=false) String address,
											  @RequestParam(value="gridName", required=false) String gridName,
											  @RequestParam(value="operateStatus", required=false) String operateStatus,
											  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		if(page<=0) page=1;
		if(resName!=null) resName = resName.trim();
		if(address!=null) address = address.trim();
		if(gridName!=null) gridName = gridName.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(gridName)){
			params.put("gridName", gridName);
		}
		if(StringUtils.isNotBlank(orgCode)){
			params.put("infoOrgCode", orgCode);
		}
		if(StringUtils.isNotBlank(resName)){
			params.put("name", resName);
		}
		if(StringUtils.isNotBlank(address)){
			params.put("address", address);
		}

		//消防资源分类:1市内消防栓2.市外消防栓3.天然水源4.自来水公司
		if(resTypeId == 0 ){
			params.put("catalog", "'1','2'");
		}else{
			params.put("catalog", resTypeId);
		}
		if(!StringUtils.isBlank(operateStatus)){
			params.put("operateStatus", operateStatus);
		}

		cn.ffcs.common.EUDGPagination pagination = new cn.ffcs.common.EUDGPagination();
		try {
			pagination = fireResourceService.findByPagination(page, rows, params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pagination;
	}

	/**
	 * 2014-07-8 sulch add
	 * 获取新资源的定位信息
	 * @param session
	 * @param ids 资源ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfNewResource")
	public List<ArcgisInfoOfPublic> getArcgisNewResourceLocateDataList(HttpSession session,
																	   ModelMap map, HttpServletResponse res,
																	   @RequestParam(value="ids") String ids,
																	   @RequestParam(value="resTypeCode") String resTypeCode,
																	   @RequestParam(value="mapt", required=false) Integer mapt
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
	) {
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();

		if (StringUtils.isNotBlank(ids)) {
			try{
				if("0601".equals(resTypeCode)){
					resTypeCode = "'0601','0602'";
				}else {
					resTypeCode = "'"+resTypeCode+"'";
				}
				list = this.arcgisDataOfLocalService.getArcgisNewResourceLocateDataListByIds(ids,mapt,resTypeCode);
				for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
					arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * 单条资源数据的查看
	 * @param session
	 * @param resId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/newResourceDetail")
	public String showNewResourceDetail(HttpSession session,
										@RequestParam(value="resId") Long resId, ModelMap map) {
		FireResource fireResource = new FireResource();
		fireResource = fireResourceService.findById(resId);
		map.addAttribute("fireResource", fireResource);
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(fireResource.getGridId(), false);
		Long gridId = gridInfo.getGridId();
		String infoOrgCode=gridInfo.getInfoOrgCode();

		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
		}
		map.addAttribute("gridNames", gridNames);
		map.addAttribute("RESOURSE_SERVER_PATH",  getNewUrl(session, App.IMG.getDomain(session)));//支持多个域名访问综治网格

		return "/map/arcgis/standardmappage/newResourceDetail.ftl";
	}

	/**
	 * 综治.消防队列表
	 * @param session
	 * @param gridId 网格ID
	 * @param typeCode 资源类型
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toArcgisDataListOfFireTeam")
	public String toArcgisDataListOfFireTeam(HttpSession session, ModelMap map,
											 @RequestParam(value = "infoOrgCode", required=false) String infoOrgCode
			,@RequestParam(value = "orgCode", required=false) String orgCode
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		if(infoOrgCode == null || "".equals(infoOrgCode)) {
			infoOrgCode = orgCode;
		}
		map.addAttribute("infoOrgCode", infoOrgCode);
		map.addAttribute("markType", "0701");
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardFireTeam.ftl";
	}

	/**
	 * 综治.消防队数据列表 
	 * @param session
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findArcgisDataListOfFireTeam", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination findArcgisDataListOfFireTeam(HttpSession session,
													   HttpServletRequest request, @RequestParam(value = "page") int page,
													   @RequestParam(value = "rows") int rows) {
		String infoOrgCode = request.getParameter("infoOrgCode");
		String name = request.getParameter("name");
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(infoOrgCode)) params.put("infoOrgCode", infoOrgCode.trim());
		if (StringUtils.isNotBlank(name)) params.put("name", name.trim());
		cn.ffcs.common.EUDGPagination pagination = fireTeamService.findFireTeamPagination(page, rows, params);
		return pagination;
	}

	/**
	 * 2014-07-8 sulch add
	 * 获取新资源的定位信息
	 * @param session
	 * @param ids 资源ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfFireTeam")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfFireTeam(
			HttpSession session, ModelMap map, HttpServletResponse res,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "markType") String markType,
			@RequestParam(value = "mapt", required = false) Integer mapt
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisFireTeamLocateDataListByIds(ids, mapt, markType);
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}

	/**
	 * 单条资源数据的查看
	 * @param session
	 * @param resId
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/fireTeamDetail")
	public String showFireDetailDetail(HttpSession session,
									   @RequestParam(value = "resId") Long resId, ModelMap map) {
		FireTeam fireTeam = fireTeamService.findFireTeamById(resId);
		map.addAttribute("fireTeam", fireTeam);
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
		map.addAttribute("RESOURSE_SERVER_PATH", getNewUrl(session, App.IMG.getDomain(session)));// 支持多个域名访问综治网格
		return "/map/arcgis/standardmappage/fireTeamDetail.ftl";
	}
	/**
	 * 园林绿化地图列表
	 * @param session
	 * @param orgCode
	 * @param standard
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfLandscape")
	public String landscapeindex(HttpSession session,
								 @RequestParam(value="orgCode") String orgCode,
								 @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
								 @RequestParam(value="standard", required=false) String standard,  ModelMap map) {
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("markType", "1302");
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardLandscape.ftl";
	}
	/**
	 * 园林绿化数据列表 
	 * @param session
	 * @param page
	 * @param rows
	 * @param orgCode
	 * @param campusName
	 * @param address
	 * @param gridName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/landscapeListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination landscapeListData(HttpSession session,
											@RequestParam(value="page") int page,
											@RequestParam(value="rows") int rows,
											@RequestParam(value="orgCode") String orgCode,
											@RequestParam(value="name_", required=false) String name_,
											@RequestParam(value="placeAddr", required=false) String placeAddr,
											@RequestParam(value="gridName", required=false) String gridName) {
		if(page<=0) page=1;
		if(gridName!=null) gridName = gridName.trim();
		if(placeAddr!=null) placeAddr = placeAddr.trim();
		if(name_!=null) name_ = name_.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(gridName)){
			params.put("gridName", gridName);
		}
		if(StringUtils.isNotBlank(orgCode)){
			params.put("infoOrgCode", orgCode);
		}
		if(StringUtils.isNotBlank(name_)){
			params.put("name_", name_);
		}
		if(StringUtils.isNotBlank(placeAddr)){
			params.put("placeAddr", placeAddr);
		}

		cn.ffcs.common.EUDGPagination pagination = new cn.ffcs.common.EUDGPagination();
		try {
			pagination = landscapeManageService.findLandscapeManagePagination(page,rows, params);
//			pagination = storeInfoService.findStoreInfoPagination(page,rows, params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pagination;
	}
	/**
	 * 园林绿化详情
	 * @param session
	 * @param map
	 * @param importUnitId
	 * @return
	 */
	@RequestMapping(value="/landscapeDetail")
	public String landscapeDetail(HttpSession session, ModelMap map,
								  @RequestParam(value="gardenId") Long gardenId){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		cn.ffcs.gmis.mybatis.domain.landscapeManage.LandscapeManage landscapeManage = landscapeManageService
				.getLandscapeManageById(gardenId, userInfo.getOrgCode());
		map.addAttribute("landscapeManage",landscapeManage);
		return "/map/arcgis/standardmappage/landscape/landscapeDetail.ftl";
	}

	/**
	 * 园林绿化多点定位
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
	@RequestMapping(value = "/getArcgisLandscapeLocateDataList")
	public List<ArcgisInfoOfPublic> getArcgisLandscapeLocateDataList(HttpSession session,
																	 @RequestParam(value = "ids") String ids,
																	 @RequestParam(value = "order", required=false) String order,
																	 @RequestParam(value = "name", required=false) String name,
																	 @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
																	 @RequestParam(value = "mapt") Integer mapt) {
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisLandscapeLocateDataListByIds(ids,"1302",mapt);
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	/**
	 * 门店地图列表
	 * @param session
	 * @param orgCode
	 * @param standard
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfStore")
	public String storeindex(HttpSession session,
							 @RequestParam(value="gridId") String gridId,
							 @RequestParam(value="orgCode") String orgCode,
							 @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
							 @RequestParam(value="standard", required=false) String standard,  ModelMap map) {
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("gridId", gridId);
		map.addAttribute("markType", "3101");
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardStore.ftl";
	}
	/**
	 * 门店数据列表 
	 * @param session
	 * @param page
	 * @param rows
	 * @param orgCode
	 * @param campusName
	 * @param address
	 * @param gridName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/storeListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination storeListData(HttpSession session,
										@RequestParam(value="page") int page,
										@RequestParam(value="rows") int rows,
										@RequestParam(value="orgCode") String orgCode,
										@RequestParam(value="gridId") Long gridId,
										@RequestParam(value="storeName", required=false) String storeName,
										@RequestParam(value="storeAddr", required=false) String storeAddr,
										@RequestParam(value="gridName", required=false) String gridName) {
		if(page<=0) page=1;
		if(gridName!=null) gridName = gridName.trim();
		if(storeAddr!=null) storeAddr = storeAddr.trim();
		if(storeName!=null) storeName = storeName.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(gridName)){
			params.put("gridName", gridName);
		}
		if(null != gridId){
			params.put("gridId", gridId);
		}
		if(StringUtils.isNotBlank(orgCode)){
			params.put("infoOrgCode", orgCode);
		}
		if(StringUtils.isNotBlank(storeName)){
			params.put("storeName", storeName);
		}
		if(StringUtils.isNotBlank(storeAddr)){
			params.put("storeAddr", storeAddr);
		}

		cn.ffcs.common.EUDGPagination pagination = new cn.ffcs.common.EUDGPagination();
		try {
			pagination = storeInfoService.findStoreInfoPagination(page,rows, params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pagination;
	}
	/**
	 * 门店详情
	 * @param session
	 * @param map
	 * @param importUnitId
	 * @return
	 */
	@RequestMapping(value="/storeDetail")
	public String storeDetail(HttpSession session, ModelMap map,
							  @RequestParam(value="storeId") Long storeId){
		StoreInfo storeInfo = storeInfoService.findEntityByPK(storeId);
		List<Attachment> attachment = attachmentService.findByBizId(storeId, "store_attachment_type", "1");
		if(attachment.size() > 0){
			map.addAttribute("attachment", attachment.get(0));
		}
		map.addAttribute("storeInfo", storeInfo);
		map.addAttribute("RESOURSE_SERVER_PATH",  getNewUrl(session, App.IMG.getDomain(session)));//支持多个域名访问综治网格
		return "/map/arcgis/standardmappage/store/storeDetail.ftl";
	}

	/**
	 * 门店多点定位
	 * @param session
	 * @param request
	 * @param ids
	 * @param order
	 * @param name
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisStoreLocateDataList")
	public List<ArcgisInfoOfPublic> getArcgisStoreLocateDataList(HttpSession session,
																 @RequestParam(value = "ids") String ids,
																 @RequestParam(value = "order", required=false) String order,
																 @RequestParam(value = "name", required=false) String name,
																 @RequestParam(value = "mapt") Integer mapt,
																 @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisStoreLocateDataListByIds(ids,mapt);
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}

	/**
	 * 综治.校园周边列表
	 * @param session
	 * @param orgCode
	 * @param standard
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfCampus")
	public String campusindex(HttpSession session,
							  @RequestParam(value="orgCode") String orgCode,
							  @RequestParam(value="standard", required=false) String standard,
							  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr, ModelMap map) {
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("markType", "0801");
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardCampus.ftl";
	}

	/**
	 * 综治.校园周边数据列表 
	 * @param session
	 * @param page
	 * @param rows
	 * @param gridId
	 * @param campusName
	 * @param address
	 * @param gridName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/campusListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination campusListData(HttpSession session,
										 @RequestParam(value="page") int page,
										 @RequestParam(value="rows") int rows,
										 @RequestParam(value="gridId") String gridId,
										 @RequestParam(value="campusName", required=false) String campusName,
										 @RequestParam(value="address", required=false) String address,
										 @RequestParam(value="gridName", required=false) String gridName) {
		if(page<=0) page=1;
		if(gridName!=null) gridName = gridName.trim();
		if(address!=null) address = address.trim();
		if(campusName!=null) campusName = campusName.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(gridName)){
			params.put("gridName", gridName);
		}
		if(StringUtils.isNotBlank(gridId)){
			params.put("gridId", gridId);
		}
		if(StringUtils.isNotBlank(campusName)){
			params.put("campusName", campusName);
		}
		if(StringUtils.isNotBlank(address)){
			params.put("address", address);
		}
		params.put("type", 1);
		cn.ffcs.common.EUDGPagination pagination = new cn.ffcs.common.EUDGPagination();
		try {
			pagination = placeInfoService.findPlaceInfoPagination(page, rows, params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pagination;
	}

	/**
	 * 2015-03-21 sulch add
	 * 获取新资源的定位信息
	 * @param session
	 * @param ids 资源ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfCampus")
	public List<ArcgisInfoOfPublic> getArcgisCampusLocateDataList(HttpSession session,
																  ModelMap map, HttpServletResponse res,
																  @RequestParam(value="ids") String ids,
																  @RequestParam(value="resTypeCode") String resTypeCode,
																  @RequestParam(value="mapt", required=false) Integer mapt,
																  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
	) {
		if(StringUtils.isBlank(resTypeCode)){
			resTypeCode = "0801";//校园周边
		}
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		try {
			list = this.arcgisDataOfLocalService.getArcgisCampusLocateDataListByIds(ids,mapt,resTypeCode);
			for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 单条资源数据的查看
	 * @param session
	 * @param plaId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/campusDetail")
	public String showCampusDetail(HttpSession session,
								   @RequestParam(value="plaId") Long plaId, ModelMap map) {
		PlaceInfo placeInfo = placeInfoService.findPlaceInfoById(plaId);
		map.addAttribute("placeInfo", placeInfo);
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(placeInfo.getGridId(), false);
		Long gridId = gridInfo.getGridId();
		String infoOrgCode=gridInfo.getInfoOrgCode();

		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
		}
		map.addAttribute("gridNames", gridNames);
		map.addAttribute("RESOURSE_SERVER_PATH",  getNewUrl(session, App.IMG.getDomain(session)));//支持多个域名访问综治网格

		return "/map/arcgis/standardmappage/campusDetail.ftl";
	}

	/**
	 * 综治中心
	 * @param session
	 * @param orgCode
	 * @param bizType
	 * @param elementsCollectionStr
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfCenters")
	public String toArcgisDataListOfCenters(HttpSession session,
											@RequestParam(value="orgCode") String orgCode,
											@RequestParam(value="bizType") String bizType,
											@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr, ModelMap map) {
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("markType", "1101");
		map.addAttribute("bizType", bizType);
		return "/map/arcgis/standardmappage/standardCenter.ftl";
	}


	@RequestMapping(value="/centerDetail")
	public String centerDetail(HttpSession session,
							   //@RequestParam(value="teamId") Long teamId,
			                   @RequestParam(value = "hashId") String hashId,
							   @RequestParam(value="bizType", required=false) String bizType,ModelMap map) {
		Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
		String infoOrgCode = defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		PrvetionTeam prvetionTeam = prvetionTeamService.findPrvetionTeamById(HashIdManager.decryptLong(hashId), infoOrgCode);
		prvetionTeam.setHashId(hashId);
		map.addAttribute("prvetionTeam", prvetionTeam);
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(prvetionTeam.getGridId(), false);
		Long gridId = gridInfo.getGridId();
		String infoOrgCode1=gridInfo.getInfoOrgCode();

		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode1);
		}
		map.addAttribute("gridNames", gridNames);
		map.addAttribute("bizType", bizType);
		map.addAttribute("RESOURSE_SERVER_PATH",  getNewUrl(session, App.IMG.getDomain(session)));//支持多个域名访问综治网格

		return "/map/arcgis/standardmappage/centerDetail.ftl";
	}

	/**
	 * 综治.安监队伍列表
	 * @param session
	 * @param orgCode
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfControlsafetyRanks")
	public String controlsafetyRanksindex(HttpSession session,
										  @RequestParam(value="orgCode") String orgCode,
										  @RequestParam(value="bizType") String bizType,
										  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr, ModelMap map) {
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		if("0".equals(bizType)){//机构队伍
			map.addAttribute("markType", "1101");
		}else if("1".equals(bizType)){//群防群治队伍
			map.addAttribute("markType", "1101");
		}else if("3".equals(bizType)){//安监队伍
			map.addAttribute("markType", "1101");
		}
		map.addAttribute("bizType", bizType);
		return "/map/arcgis/standardmappage/standardControlsafetyRanks.ftl";
	}

	/**
	 * 综治.群防群治队伍、机构队伍、安监队伍数据列表 
	 * @param session
	 * @param page
	 * @param rows
	 * @param orgCode
	 * @param campusName
	 * @param address
	 * @param gridName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/controlsafetyRanksListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination controlsafetyRanksListData(HttpSession session,
													 @RequestParam(value="page") int page,
													 @RequestParam(value="rows") int rows,
													 @RequestParam(value="orgCode") String orgCode,
													 @RequestParam(value="name", required=false) String name,
													 @RequestParam(value="gridName", required=false) String gridName,
													 @RequestParam(value="bizType", required=false) String bizType) {
		if(page<=0) page=1;
		if(gridName!=null) gridName = gridName.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(gridName)){
			params.put("gridName", gridName);
		}
		if(StringUtils.isNotBlank(orgCode)){
			params.put("regionCode", orgCode);
		}
		if(StringUtils.isNotBlank(name)){
			params.put("name", name);
		}
		if(StringUtils.isNotBlank(bizType)){
			params.put("bizType", bizType);
		}

		cn.ffcs.common.EUDGPagination pagination = new cn.ffcs.common.EUDGPagination();
		try {
			pagination = prvetionTeamService.findPrvetionTeamPagination(page, rows, params);
			if(pagination.getRows()!=null) {
				List<PrvetionTeam>  prvetionTeams =(List<PrvetionTeam>)pagination.getRows();
				prvetionTeams.forEach(bo->{
					bo.setHashId(HashIdManager.encrypt(bo.getTeamId()));
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pagination;
	}
public static void main(String[] args) {
	System.out.println(HashIdManager.encrypt(2301));
}
	/**
	 * 2015-03-21 sulch add
	 * 获取新资源的定位信息
	 * @param session
	 * @param ids 资源ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfControlsafetyRanks")
	public List<ArcgisInfoOfHashId> getArcgisControlsafetyRanksLocateDataList(HttpSession session,
																			  ModelMap map, HttpServletResponse res,
																			  @RequestParam(value="ids") String ids,
																			  @RequestParam(value="resTypeCode", required=false) String resTypeCode,
																			  @RequestParam(value="mapt", required=false) Integer mapt,
																			  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
	) {
		if(StringUtils.isBlank(resTypeCode)){
			resTypeCode = "1101";// 机构队伍、群防群治队伍、安监队伍
		}
		List<ArcgisInfoOfHashId> list = new ArrayList<ArcgisInfoOfHashId>();
        String idStrs=ids;
		if (StringUtils.isNotBlank(ids)) {
			String[] strs=ids.split(",");
			if(!Ognl.isNumber(strs[0])) {//非数字的即加密的要解密
				for (int i = 0; i < strs.length; i++) {
					if(i==0)idStrs="";
					idStrs+= HashIdManager.decryptLong(strs[i]);//
					if(i<strs.length-1)idStrs+=",";
				}
			}
			try {
				list = this.arcgisDataOfLocalService.getArcgisControlsafetyRanksLocateDataListByIds(idStrs,mapt,resTypeCode);
				for(ArcgisInfoOfHashId arcgisInfoOfHashId:list){
					arcgisInfoOfHashId.setElementsCollectionStr(elementsCollectionStr);
					if(!Ognl.isNumber(strs[0])) {//wid加密
						if(arcgisInfoOfHashId.getWid()!=null) {
							arcgisInfoOfHashId.setWid(HashIdManager.encrypt(arcgisInfoOfHashId.getWid()));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * 单条资源数据的查看
	 * @param session
	 * @param plaId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/controlsafetyRanksDetail")
	public String showControlsafetyRanksDetail(HttpSession session,
											   //@RequestParam(value="teamId") Long teamId,
			                                   @RequestParam(value = "hashId") String hashId,
											   @RequestParam(value="bizType", required=false) String bizType,ModelMap map) {
		Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
		String infoOrgCode = defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		PrvetionTeam prvetionTeam = prvetionTeamService.findPrvetionTeamById(HashIdManager.decryptLong(hashId), infoOrgCode);
		prvetionTeam.setHashId(hashId);
		map.addAttribute("prvetionTeam", prvetionTeam);
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(prvetionTeam.getGridId(), false);
		Long gridId = gridInfo.getGridId();
		String infoOrgCode1=gridInfo.getInfoOrgCode();

		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode1);
		}
		if (StringUtils.isBlank(gridNames)) gridNames = "";
		map.addAttribute("gridNames", gridNames);
		map.addAttribute("bizType", bizType);
		map.addAttribute("GMIS_PATH",  App.GMIS.getDomain(session));
		map.addAttribute("RESOURSE_SERVER_PATH",  App.IMG.getDomain(session));//支持多个域名访问综治网格
		if(prvetionTeam.getBizType().equals(ConstantValue.BIZ_TYPE_PREVTION)){//群防群治
			return "/map/arcgis/standardmappage/massPreventionTreatmentDetail.ftl";
		}else if(prvetionTeam.getBizType().equals(ConstantValue.BIZ_TYPE_ORG)){//综治机构
			return "/map/arcgis/standardmappage/comprehensiveMgrOrgDetail.ftl";
		}
		return "/map/arcgis/standardmappage/controlsafetyRanksDetail.ftl";
	}

	/**
	 * 2015-12-03 sulch add
	 * 获取出租屋的定位信息
	 * @param session
	 * @param ids 资源ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfRentRoom")
	public List<ArcgisInfoOfPublic> getArcgisRentRoomLocateDataList(HttpSession session,
																	ModelMap map, HttpServletResponse res,
																	@RequestParam(value="ids") String ids,
																	@RequestParam(value="resTypeCode") String resTypeCode,
																	@RequestParam(value="mapt", required=false) Integer mapt,
																	@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
	) {
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		try {
			list = this.arcgisDataOfLocalService.getArcgisRentRoomLocateDataListByIds(ids,mapt);
			for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 单条出租屋的查看
	 * @param session
	 * @param plaId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/rentRoomDetail")
	public String showRentRoomDetail(HttpSession session,
									 @RequestParam(value="plaId") Long plaId, ModelMap map) {
		PlaceInfo placeInfo = placeInfoService.findPlaceInfoById(plaId);
		map.addAttribute("placeInfo", placeInfo);
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(placeInfo.getGridId(), false);
		Long gridId = gridInfo.getGridId();
		String infoOrgCode=gridInfo.getInfoOrgCode();

		String gridNames="";
		if(gridId!=null){
			gridNames= mixedGridInfoService.getGridPath(gridId);
		}else{
			gridNames= mixedGridInfoService.getGridPath(infoOrgCode);
		}

		map.addAttribute("gridNames", gridNames);
		map.addAttribute("RESOURSE_SERVER_PATH",  getNewUrl(session, App.IMG.getDomain(session)));//支持多个域名访问综治网格

		return "/map/arcgis/standardmappage/rentRoomDetail.ftl";
	}

	/**
	 * 渔船轨迹数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/trajectory")
	public String trajectoryList(HttpSession session, ModelMap map
			, @RequestParam(value="gridId") Long gridId
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
//		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardTrajectory.ftl";
	}

	/**
	 * 渔船轨迹数据
	 */
	@ResponseBody
	@RequestMapping(value="/trajectoryListData", method=RequestMethod.POST)
	public Map<String, Object> gridAdminListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
												 @RequestParam(value="gridId") Long gridId, @RequestParam(value="name", required=false) String name) {
		if(page<=0) page=1;
//		if(name!=null) name = name.trim();
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("startGridId", gridId);
//		params.put("name", name);
//		EUDGPagination pagination = trajectoryService.findTrajectoryPagination(page, rows, params);
		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("deviceId", "1");
		infoMap.put("appDeviceId", "1");
		infoMap.put("deviceSn", "TEST20160601TEST20160601");
		infoMap.put("deviceName", "基站测试设备1基站测试设备1");
		infoMap.put("appName", "定位");

		return infoMap;   //pagination;
	}

	/**
	 * 渔船轨迹 单条
	 */
	@RequestMapping(value="/trajectoryDetail")
	public String gridAdminDetail(HttpSession session, ModelMap map
			, @RequestParam(value="deviceId", required=false) Long deviceId
			, @RequestParam(value="wid", required=false) Long wid
			, @RequestParam(value = "isCross", required=false) String isCross) {
//		if(wid != null && wid !=0l){
//			deviceId = wid;
//		}
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);

		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.HOUR, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String startTime = sdf.format(ca.getTime());
		String endTime = sdf.format(new Date());
		map.addAttribute("startTime", startTime);
		map.addAttribute("endTime", endTime);

		map.addAttribute("RESOURSE_SERVER_PATH",  App.IMG.getDomain(session));
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.addAttribute("isCross", isCross);

		map.addAttribute("deviceName", "基站测试设备1基站测试设备1");
		map.addAttribute("deviceSn", "TEST20160601");
		map.addAttribute("deviceManufacture", "龙芯科技");
		map.addAttribute("note", "");
		map.addAttribute("ownerRsName", "张三");
		map.addAttribute("infoOrgCode", defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("appDeviceId", "1");

		return "/map/arcgis/standardmappage/trajectoryDetail.ftl";
	}

	/**
	 * 2016-06-13 qiud
	 * 设备轨迹
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataTrajectoryOfTrajectory")
	public List<LocateInfo> getArcgisDataTrajectoryOfGridAdmin(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="locateTimeBegin")String locateTimeBegin
			,@RequestParam(value="locateTimeEnd", required=false)String locateTimeEnd
			,@RequestParam(value="appDeviceId")Long appDeviceId) {
		String infoOrgCode = super.getDefaultInfoOrgCode(session);
		List<LocateInfo> list = arcgisDataOfLocalService.getTrajectoryList(locateTimeBegin,locateTimeEnd,appDeviceId);
		return list;
	}

	/**
	 * 2016-06-14 qiud
	 * 获取渔船的定位信息
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfBoat")
	public List<ArcgisInfo> getArcgisBoatLocateDataList(HttpSession session,
														HttpServletRequest request,
														@RequestParam(value = "ids") String ids,
														@RequestParam(value = "order", required=false) String order,
														@RequestParam(value = "name", required=false) String name,
														@RequestParam(value = "mapt") Integer mapt,
														@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfo> list = this.arcgisDataOfLocalService.getArcgisBoatLocateDataListByIds(ids);
		for(ArcgisInfo arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}

	//海康 设备信息 start-------------------------------------------------------------------
	/**
	 * 跳转设备信息地图简介页面
	 * @param session
	 * @param eqpId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/equipmentDetail/003")
	public String equipmentDetailAps(HttpSession session,
									 @RequestParam(value="eqpId") Long eqpId, ModelMap map) {
		Equipment equipment = equipmentService.findById(eqpId);
		if(equipment == null)
			equipment = new Equipment();
		map.addAttribute("equipment", equipment);
		//车辆异常
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eqpSn", equipment.getEqpSn());
		Long result = carRecordService.findExceptionStatisticsListCount(null , params);
		map.addAttribute("result", result);
		return "/map/arcgis/standardmappage/equipment/detailEquipmentAps.ftl";
	}

	@RequestMapping(value="/toIndexEquipmentAps")
	public String toIndexEquipmentAps(HttpSession session, HttpServletRequest req, ModelMap map) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		return "/map/arcgis/standardmappage/equipment/indexEquipmentAps.ftl";
	}

	@ResponseBody
	@RequestMapping(value="/listEquipmentApsData", method=RequestMethod.POST)
	public EUDGPagination listEquipmentApsData(HttpSession session,
											   @RequestParam(value="page") int page,
											   @RequestParam(value="rows") int rows,
											   @RequestParam Map<String, Object> params) {
		cn.ffcs.doorsys.common.EUDGPagination equipmentPagination = apsLogService.findPagination(page, rows, params);
		Long listTotal = equipmentPagination.getTotal();
		
		EUDGPagination pagination = new EUDGPagination();
		pagination.setRows(equipmentPagination.getRows());
		pagination.setTotal(listTotal.intValue());
		return pagination;
	}

	@RequestMapping(value="/toEquipmentApsImgs")
	public String toEquipmentApsImgs(HttpSession session, HttpServletRequest req, ModelMap map) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("imgUrl", req.getParameter("imgUrl"));
		return "/map/arcgis/standardmappage/equipment/indexEquipmentApsImgs.ftl";
	}

	@RequestMapping(value="/toDataListOfEquipment")
	public String toDataListOfEquipment(HttpSession session,
										@RequestParam(value="gridId") Long gridId,
										@RequestParam(value = "infoOrgCode", required=false) String infoOrgCode,
										@RequestParam(value = "gdcId", required=false) String gdcId,
										@RequestParam(value = "keyWord", required=false) String keyWord,
										@RequestParam(value = "eqpType", required=false) String eqpType,
										ModelMap map) {
		map.addAttribute("infoOrgCode", infoOrgCode);
		if(StringUtils.isNotBlank(eqpType)) {
			map.addAttribute("eqpType", eqpType);
		}
		map.addAttribute("gdcId", gdcId);
		map.addAttribute("gridId", gridId);
		map.addAttribute("keyWord", keyWord);
		return "/map/arcgis/standardmappage/equipment/standardEquipmentIDSS.ftl";
	}

	/**
	 * 跳转设备信息列表页面
	 * @param session
	 * @param gridId
	 * @param elementsCollectionStr
	 * @param eqpType 设备类型 001 门禁设备；002 访客机；003 车闸设备；004 报警机；005 视频设备
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfEquipment")
	public String toArcgisDataListOfEquipment(HttpSession session,
											  @RequestParam(value="gridId") Long gridId,
											  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
											  @RequestParam(value = "eqpType", required=false) String eqpType,
											  ModelMap map) {

		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		if(gridInfo != null) {
			map.addAttribute("infoOrgCode", gridInfo.getInfoOrgCode());
		}
		if(StringUtils.isNotBlank(eqpType)) {
			map.addAttribute("eqpType", eqpType);
		}

		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("gridId", gridId);
		if ("001".equals(eqpType)) {
		} else if ("002".equals(eqpType)) { // 访客机
			return "/map/arcgis/standardmappage/equipment/pps/standardVisitorMachine.ftl";
		} else if ("003".equals(eqpType)) {

		} else if ("004".equals(eqpType)) {

		} else if ("005".equals(eqpType)) {

		}

		return "/map/arcgis/standardmappage/equipment/standardEquipment.ftl";
	}

	/**
	 * 加载设备信息数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param params
	 * 			infoOrgCode 网格所属地域
	 * 			eqpType 设备类型 001 门禁设备；002 访客机；003 车闸设备；004 报警机；005 视频设备
	 * 			eqpName 设备名称
	 * 			eqpSn	设备号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listEquipmentData", method=RequestMethod.POST)
	public EUDGPagination listEquipmentData(HttpSession session,
											@RequestParam(value="page") int page,
											@RequestParam(value="rows") int rows,
											@RequestParam Map<String, Object> params) {
		params.put("eqpTypes", "001,002,003,004,005,006");//除人脸
		cn.ffcs.doorsys.common.EUDGPagination equipmentPagination = equipmentService.findPagination(page, rows, params);
		Long listTotal = equipmentPagination.getTotal();
		
		EUDGPagination pagination = new EUDGPagination();

		pagination.setRows(equipmentPagination.getRows());
		pagination.setTotal(listTotal.intValue());

		return pagination;
	}

	/**
	 * 获取设备信息定位信息
	 * @param session
	 * @param ids
	 * @param mapt
	 * @param elementsCollectionStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfEquipment")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfEquipment(HttpSession session,
																	   @RequestParam(value = "ids") String ids,
																	   @RequestParam(value = "mapt") Integer mapt,
																	   @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
																	   @RequestParam(value = "isIdssControl", required=false, defaultValue="false") Boolean isIdssControl,
																	   @RequestParam(value = "controlTargetId", required=false) Long controlTargetId) {
		List<ArcgisInfoOfPublic> list = null;

		if (StringUtils.isNotBlank(ids)) {
			if(isIdssControl) {
				list = this.arcgisDataOfLocalService.getIdssControlEqpLocateDataListByIds(ids, mapt, controlTargetId);
			} else {
				list = this.arcgisDataOfLocalService.getArcgisEqpLocateDataListByIds(ids,mapt);
			}

			for(ArcgisInfoOfPublic arcgisInfoOfPublic : list){
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
		}

		return list;
	}

	@ResponseBody
	@RequestMapping(value = "/getLocateDataListOfEquipment")
	public List<ArcgisInfoOfPublic> getLocateDataListOfEquipment(HttpSession session,
																 @RequestParam(value = "eqpType", required=false) String eqpType,
																 @RequestParam(value = "mapt") Integer mapt,
																 @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String,Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(eqpType)){
			if(eqpType.equals("accessControl")){
				eqpType = "001";
			}else if(eqpType.equals("accessBrake")){
				eqpType = "003";
			}else if(eqpType.equals("bayonetEquipment")){
				eqpType = "006";
			}else if(eqpType.equals("accessAlarm")){
				eqpType = "004";
			}else if(eqpType.equals("visitorMachine")){
				eqpType = "002";
			}else if(eqpType.equals("videoCamera")){
				eqpType = "005";
				String IDSS_VIDEOCAMERA_RELATE = funConfigurationService.turnCodeToValue("IDSS_VIDEOCAMERA_RELATE", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
				if(StringUtils.isBlank(IDSS_VIDEOCAMERA_RELATE)){
					IDSS_VIDEOCAMERA_RELATE = "10288.383301570211,9028.077339627758,3312.383273954798,8100.077335954148,2496.3832707245556,12388.07735292876,9680.383299163362,13076.077355652298,9680.383299163362,13076.077355652298,9680.383299163362,13076.077355652298,10288.383301570211,9028.077339627758";
				}
//				params.put("relate", IDSS_VIDEOCAMERA_RELATE);
			}
			params.put("eqpType", eqpType);
		}
		params.put("mapt", mapt);
		List<ArcgisInfoOfPublic> result = new ArrayList<ArcgisInfoOfPublic>();
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisEqpLocateDataList(params);
		for (int i = 0; i < list.size(); i++) {
			result.add(list.get(i));
			if(i > 200){
				break;
			}
		}
		for(ArcgisInfoOfPublic arcgisInfoOfPublic : result){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}

		return result;
	}

	/**
	 * 跳转设备信息地图简介页面
	 * @param session
	 * @param eqpId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/equipmentDetail")
	public String equipmentDetail(HttpSession session,
								  @RequestParam(value="eqpId") Long eqpId, ModelMap map,
								  @RequestParam(value = "eqpType", required=false) String eqpType) {
		Equipment equipment = equipmentService.findById(eqpId);

		if(equipment == null) {
			equipment = new Equipment();
		} else if(StringUtils.isBlank(eqpType)) {
			eqpType = equipment.getEqpType();
		}

		map.addAttribute("equipment", equipment);
		String forwardPath = "";
		//重点人口
		if("001".equals(eqpType) || "002".equals(eqpType)){
			Long result = 0L;
//			result = residentRecordService.findKeyPersonCountByBuildId(1050568L);
			if("001".equals(eqpType) || "002".equals(eqpType)){
				if(equipment.getBuildingId() != null){
					System.out.println("buildingId-----------------"+equipment.getBuildingId());//1050568
					result = residentRecordService.findKeyPersonCountByBuildId(equipment.getBuildingId());
				}
				map.addAttribute("result", result);
			}
		}
		if("001".equals(eqpType)) {//门禁设备
			forwardPath = "/map/arcgis/standardmappage/equipment/accessControl/detail_accessControl.ftl";
		} else if ("002".equals(eqpType)) { // 访客机
			forwardPath = "/map/arcgis/standardmappage/equipment/pps/detailVisitorMachine.ftl";
		} else if("006".equals(eqpType)) {//卡口设备
			forwardPath = "/map/arcgis/standardmappage/equipment/bayonetEquipment/detail_bayonetEquipment.ftl";
		}

		return forwardPath;
	}

	/**
	 * 跳转报警机设备信息地图简介页面
	 * @param session
	 * @param eqpId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/accessAlarmEquipmentDetail")
	public String accessAlarmEquipmentDetail(HttpSession session,
											 @RequestParam(value="eqpId") Long eqpId, ModelMap map) {
		Equipment equipment = equipmentService.findById(eqpId);

		if(equipment == null) {
			equipment = new Equipment();
		}

		map.addAttribute("equipment", equipment);

		return "/map/arcgis/standardmappage/equipment/accessAlarm/detailAccessAlarmEquipment.ftl";
	}

	/**
	 * 跳转到报警机报警记录列表
	 * <p>Description:</p>
	 * @param session
	 * @param eqpId
	 * @param map
	 * @return
	 * add sulch
	 * 2016年6月25日 下午3:09:17
	 */
	@RequestMapping(value="/toDataListOfAmsLog")
	public String toDataListOfAmsLog(HttpSession session,
									 @RequestParam(value="bizId") String bizId,
									 ModelMap map) {
//		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		if(bizId != null) {
			map.addAttribute("bizId", bizId);
		}

		return "/map/arcgis/standardmappage/equipment/accessAlarm/list_amsLog.ftl";
	}

	/**
	 * 加载报警机报警记录列表
	 * <p>Description:</p>
	 * @param session
	 * @param page
	 * @param rows
	 * @param params
	 * @return
	 * add sulch
	 * 2016年6月25日 下午3:46:58
	 */
	@ResponseBody
	@RequestMapping(value="/listAmsLogData", method=RequestMethod.POST)
	public EUDGPagination listAmsLogData(HttpSession session,
										 @RequestParam(value="page") int page,
										 @RequestParam(value="rows") int rows,
										 @RequestParam Map<String, Object> params) {

//		params.put("logId", "40282e815571a9e0015571a9e0ff000012");
//		params.put("eqpSn", "sdff123");

		cn.ffcs.doorsys.common.EUDGPagination amsLogPagination = amsLogService.findPagination(page, rows, params);
		Long listTotal = amsLogPagination.getTotal();
		
		EUDGPagination pagination = new EUDGPagination();
		pagination.setRows(amsLogPagination.getRows());
		pagination.setTotal(listTotal.intValue());
		return pagination;
	}

	/**
	 * 跳转视频摄像头设备信息地图简介页面
	 * @param session
	 * @param eqpId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/showImg")
	public String showImg(HttpSession session,
						  @RequestParam(value="imgSrc", required = false) String imgSrc,
						  @RequestParam(value="logId", required = false) String logId, ModelMap map) {
		map.addAttribute("imgSrc", imgSrc);
		AmsLog amsLog = new AmsLog();
		if(logId != null){
			amsLog = amsLogService.findById(logId);
		}
		map.addAttribute("amsLog", amsLog);

		return "/map/arcgis/standardmappage/equipment/accessAlarm/detailOfImg.ftl";
	}

	/**
	 * 跳转视频摄像头设备信息地图简介页面
	 * @param session
	 * @param eqpId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/videoCameraEquipmentDetail")
	public String videoCameraEquipmentDetail(HttpSession session,
											 @RequestParam(value="eqpId") Long eqpId, ModelMap map) {
		Equipment equipment = equipmentService.findById(eqpId);

		if(equipment == null) {
			equipment = new Equipment();
		}

		map.addAttribute("equipment", equipment);
		map.addAttribute("IDSS_DOMAIN", App.IDSS.getDomain(session));

		return "/map/arcgis/standardmappage/equipment/videoCamera/detailVideoCameraEquipment.ftl";
	}

	/**
	 * 跳转门禁设备日志列表页面
	 * @param session
	 * @param eqpSn 设备号
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toAcsLogList")
	public String toAcsLogList(HttpSession session,
							   @RequestParam(value = "bizId") String bizId,
							   ModelMap map) {

		map.addAttribute("bizId", bizId);

		return "/map/arcgis/standardmappage/equipment/accessControl/list_acsLog.ftl";
	}

	/**
	 * 加载门禁设备日志列表数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listAcsLogData", method=RequestMethod.POST)
	public EUDGPagination listAcsLogData(HttpSession session,
										 @RequestParam(value="page") int page,
										 @RequestParam(value="rows") int rows,
										 @RequestParam Map<String, Object> params) {
		cn.ffcs.doorsys.common.EUDGPagination equipmentPagination = acsLogService.findPagination(page, rows, params);

		return new EUDGPagination(equipmentPagination.getTotal(), equipmentPagination.getRows());
	}

	/**
	 * 跳转卡口设备运行数据列表页面
	 * @param session
	 * @param map
	 * @param eqpSn 卡口设备号
	 * @param pattern 0 图片列表，其他 数据列表
	 * @return
	 */
	@RequestMapping(value="/toBayonetRunDataList")
	public String toBayonetRunDataList(HttpSession session,
									   ModelMap map,
									   @RequestParam(value="eqpSn") String eqpSn,
									   @RequestParam(value="pattern", required = false, defaultValue = "1") int pattern) {

		String forwardUrl = "/map/arcgis/standardmappage/equipment/bayonetEquipment/list_bayonetRunData.ftl";

		switch(pattern) {
			case 0: {//图片列表
				forwardUrl = "/map/arcgis/standardmappage/equipment/bayonetEquipment/pic_bayonetRunData.ftl";
				break;
			}
		}

		map.addAttribute("eqpSn", eqpSn);
		map.addAttribute("pattern", pattern);

		return forwardUrl;
	}

	/**
	 * 加载卡口运行数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listBayonetRunData", method=RequestMethod.POST)
	public EUDGPagination listBayonetRunData(HttpSession session,
											 @RequestParam(value="page") int page,
											 @RequestParam(value="rows") int rows,
											 @RequestParam Map<String, Object> params) {
		EUDGPagination pagination = fetchBayonetRunData(params);

		if(pagination.getTotal() > 0) {
			changeRunDataDict((List<Map<String, Object>>)pagination.getRows());
		}

		return pagination;
	}

	/**
	 * 获取卡口设备运行数据
	 * @param params
	 * 		sessionId		初始化连接得到的字符串 									不可空
	 * 		searchType 		搜索类型,1:过车信息, 2:异常信息, 3:违章搜索 				不可空
	 * 		passTime 		经过时间格式yyyy-MM-dd HH:mm:ss，以“_”相连，开始与结束时间均不得为空
	 * 		crossingIndexes	卡口编号，多个编号以”,”隔开
	 * 		plateInfo 		车牌号码，支持模糊查询，*号代表多个或零个字符，？号代表一个字符
	 * 		alarmAction 	违法行为，具体参照数据字典
	 * 		start 			开始记录行数，第一次为0，分页返回数据  						不可空
	 * 		limit 			每页数据数目										不可空
	 * @return
	 */
	private EUDGPagination fetchBayonetRunData(Map<String, Object> params) {
		StringBuffer xmlBuffer = new StringBuffer("");
		xmlBuffer.append("<?xml  version='1.0' encoding='UTF-8'?>")
				.append("	<vehicleQueryParam>")
						//.append("		<sessionId>").append(fetchSessionId()).append("</sessionId>")
				.append("		<sessionId>365267eb-1190-4dc6-8e0e-9a8639bd847c</sessionId>");


		if(CommonFunctions.isNotBlank(params, "start")) {//起始行数(包含)
			xmlBuffer.append("<start>").append(params.get("start")).append("</start>");
		} else if(CommonFunctions.isNotBlank(params, "page") && CommonFunctions.isNotBlank(params, "rows")) {
			Long page = 0L, rows = 0L, start = 0L;

			try {
				page = Long.valueOf(params.get("page").toString());
				rows = Long.valueOf(params.get("rows").toString());

				start = (page - 1) * rows;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			start = start < 0 ? 0 : start;

			xmlBuffer.append("<start>").append(start).append("</start>");
		}

		if(CommonFunctions.isNotBlank(params, "rows")) {//每页包含记录数
			xmlBuffer.append("<limit>").append(params.get("rows")).append("</limit>");
		}

		if(CommonFunctions.isNotBlank(params, "searchType")) {
			xmlBuffer.append("<searchType>").append(params.get("searchType")).append("</searchType>");
		}

		if(CommonFunctions.isNotBlank(params, "plateInfo")) {
			xmlBuffer.append("<plateInfo>*").append(params.get("plateInfo")).append("*</plateInfo>");
		}

		xmlBuffer.append("<passTime>");
		if(CommonFunctions.isNotBlank(params, "passTimeStart")) {
			xmlBuffer.append(params.get("passTimeStart"));
		}
		if(CommonFunctions.isNotBlank(params, "passTimeEnd")) {
			xmlBuffer.append("_").append(params.get("passTimeEnd"));
		}
		xmlBuffer.append("</passTime>");

		if(CommonFunctions.isNotBlank(params, "eqpSn")) {
			xmlBuffer.append("<crossingIndexes>").append(params.get("eqpSn")).append("</crossingIndexes>");
		}

		xmlBuffer.append("</vehicleQueryParam>");

		Long totalRecord = 0L;
		List<Map<String, Object>> recordMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> recordMap = null;

		try {
			//http://172.17.46.11:5300/services/ThirdBayonetService 
			ThirdBayonetServiceStub thirdBayonetServiceStub = new ThirdBayonetServiceStub("http://172.117.242.3:5300/services/ThirdBayonetService");
			ThirdBayonetServiceStub.SearchVehicleInfo  searchVehicleInfo = new ThirdBayonetServiceStub.SearchVehicleInfo();
			searchVehicleInfo.setXml(xmlBuffer.toString());
			SearchVehicleInfoResponse rsp = thirdBayonetServiceStub.searchVehicleInfo(searchVehicleInfo);
			String returnXml = rsp.get_return();

			if(returnXml != null){
				Document doc = DocumentHelper.parseText(returnXml);
				Element root = doc.getRootElement();

				if(root != null){
					String code = root.elementText("code");

					if("0".equals(code)) {
						Element vehicleInfos = root.element("vehiclePassInfos");
						if(vehicleInfos != null) {
							List<Element> vehicleInfoList = vehicleInfos.elements("vehiclePassInfo");
							if(vehicleInfoList != null && vehicleInfoList.size() > 0) {
								for(Element vehicleInfo : vehicleInfoList) {
									List<Element> vehicleItemList = vehicleInfo.elements();
									if(vehicleItemList != null && vehicleItemList.size() > 0) {
										recordMap = new HashMap<String, Object>();

										for(Element vehicleItem : vehicleItemList) {
											recordMap.put(vehicleItem.getName(), vehicleInfo.elementTextTrim(vehicleItem.getName()));
										}

										recordMapList.add(recordMap);
									}
								}
							}
						}

					} else {//请求失败时，输出失败原因
						String msgWrong = root.elementText("message");
						logger.error(msgWrong);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


		EUDGPagination pagination = new EUDGPagination();
		pagination.setTotal(totalRecord.intValue());
		pagination.setRows(recordMapList);

		return pagination;
	}

	/**
	 * 获取sessionId
	 * @return
	 */
	private String fetchSessionId() {
		StringBuffer xmlBuffer = new StringBuffer("");
		xmlBuffer.append("<?xml version='1.0' encoding='UTF-8'?>")
				.append("	<loginParam>")
				.append("		<cmsUrl></cmsUrl>")
				.append("		<userName>zhhgt</userName>")
				.append("		<passwd>hgt12345</passwd>")
				.append("	</loginParam>");
		String sessionId = "";

		try {
			//http://172.17.46.11:5300/services/ThirdBayonetService
			ThirdBayonetServiceStub thirdBayonetServiceStub = new ThirdBayonetServiceStub("http://172.117.242.3:5300/services/ThirdBayonetService");
			ThirdBayonetServiceStub.InitSystem  initSystem = new ThirdBayonetServiceStub.InitSystem();
			initSystem.setXml(xmlBuffer.toString());
			InitSystemResponse rsp = thirdBayonetServiceStub.initSystem(initSystem);
			String returnXml = rsp.get_return();

			if(returnXml != null){
				Document doc = DocumentHelper.parseText(returnXml);
				Element root = doc.getRootElement();

				if(root != null){
					String code = root.elementText("code");

					if("0".equals(code)){
						Element session = root.element("sessionId");
						sessionId = session.getTextTrim();
					} else {//请求失败时，输出失败原因
						String msgWrong = root.elementText("message");
						logger.error(msgWrong);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sessionId;

	}

	/**
	 * 获取的运行数据的字典转换
	 * @param runDataList
	 */
	private void changeRunDataDict(List<Map<String, Object>> runDataList) {
		if(runDataList != null && runDataList.size() > 0) {
			Map<String, Object> plateTypeMap = new HashMap<String, Object>(){
				{
					put("1", "无类型"); put("2", "92式民用车"); put("3", "警用车"); put("4", "上下军车");
					put("5", "92式武警车"); put("6", "左右军车"); put("7", "02式个性化车"); put("8", "黄色双行尾牌");
					put("9", "04式新军车"); put("10", "使馆车"); put("11", "一行结构的新WJ车"); put("12", "双行结构的新WJ车");
					put("13", "黄色1225农用车"); put("14", "绿色1325农用车"); put("15", "黄色1325农用车"); put("16", "摩托车");
					put("17", "教练车"); put("18", "临时行驶车"); put("19", "挂车"); put("20", "领馆汽车");
					put("21", "港澳出入车"); put("22", "临时入境车");
				}
			};

			Map<String, Object> vehicleTypeName = new HashMap<String, Object>() {
				{
					put("1", "其它车型"); put("2", "小型汽车"); put("3", "大型汽车");
					put("4", "二轮车"); put("5", "三轮车"); put("6", "行人");
				}
			};

			for(Map<String, Object> runDataMap : runDataList) {
				if(CommonFunctions.isNotBlank(runDataMap, "plateType")) {
					runDataMap.put("plateTypeName", plateTypeMap.get(runDataMap.get("plateType")));
				}
				if(CommonFunctions.isNotBlank(runDataMap, "vehicleType")) {
					runDataMap.put("vehicleTypeName", vehicleTypeName.get(runDataMap.get("vehicleType")));
				}
			}
		}
	}
	//海康 设备信息 end-------------------------------------------------------------------

	/**
	 * 跳转部件信息地图简介页面
	 * @param session
	 * @param map
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/urbanObjDetail")
	public String urbanObjDetail(HttpSession session, ModelMap map,
								 @RequestParam(value="id") Long id) {
		UrbanObj urbanObj = urbanObjService.findById(id);
		map.put("urbanObj", urbanObj);
		return "/map/arcgis/standardmappage/urbanObj/urbanObjDetail.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/getBizLocateInfoList")
	public List<ArcgisInfo> getBizLocateInfoList(HttpSession session,
												 @RequestParam(value = "bizType", required = false) String bizType,
												 @RequestParam(value = "mapt", required = false) Integer mapt) {
		List<ArcgisInfo> list = this.arcgisDataOfLocalService.getBizLocateInfoList(bizType);
		return list;
	}

	/**
	 * （甘肃）列表
	 * @param session
	 * @param gridId 网格ID
	 * @param typeCode 资源类型
	 *                  fireUnite ：消防部件
	 *                 	griderGps ： 小网格检查
	 *                 	publicityGpsData ：消防宣传检查
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfGSResource")
	public String toArcgisDataListOfGSResource(HttpSession session, ModelMap map
			, @RequestParam(value="gridId") Long gridId
			,@RequestParam(value = "beginTime", required = false) String beginTime
			,@RequestParam(value = "endTime", required = false) String endTime
			,@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
			, @RequestParam(value = "typeCode", required=false) String typeCode) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		if(StringUtils.isNotBlank(beginTime)){
			map.addAttribute("beginTime", beginTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			map.addAttribute("endTime", endTime);
		}
		String standardUrl = "/map/arcgis/standardmappage/standardFireUnit.ftl";
		if(StringUtils.isNotBlank(typeCode)){
			if("fireUnite".equals(typeCode)){
				standardUrl = "/map/arcgis/standardmappage/standardFireUnit.ftl";
			}else if("griderGps".equals(typeCode)){
				standardUrl = "/map/arcgis/standardmappage/standardGriderGps.ftl";
			}else if("publicityGpsData".equals(typeCode)){
				standardUrl = "/map/arcgis/standardmappage/standardPublicityGpsData.ftl";
			}
		}
		return standardUrl;
	}

	/**
	 * 单条消防部件甘肃对接数据的查看
	 * @param session
	 * @param resId
	 * @param map
	 * @param typeCode 资源类型
	 *                  fireUnite ：消防部件
	 *                 	griderGps ： 小网格检查
	 *                 	publicityGpsData ：消防宣传检查
	 * @return
	 */
	@RequestMapping(value="/detailofFireUnite")
	public String detailofFireUnite(HttpSession session,
									@RequestParam(value="resId") Long resId,
									@RequestParam(value="data", required = false) String data,
									@RequestParam(value = "typeCode", required=false) String typeCode, ModelMap map) {
		JSONObject dataObj = new JSONObject();
		if(StringUtils.isNotBlank(data)){
			dataObj = JSONObject.fromObject(data);
			map.addAttribute("data", dataObj);
		}
		String detailUrl = "/map/arcgis/standardmappage/fireUnitDetail.ftl";
		return detailUrl;
	}

	/**
	 * 单条小网格检查甘肃对接数据的查看
	 * @param session
	 * @param resId
	 * @param map
	 *                  fireUnite ：消防部件
	 *                 	griderGps ： 小网格检查
	 *                 	publicityGpsData ：消防宣传检查
	 * @return
	 */
	@RequestMapping(value="/detailofGriderGps")
	public String detailofGriderGps(HttpSession session,
									@RequestParam(value="resId") Long resId,
									@RequestParam(value="data", required = false) String data, ModelMap map) {
		JSONObject dataObj = new JSONObject();
		if(StringUtils.isNotBlank(data)){
			dataObj = JSONObject.fromObject(data);
			map.addAttribute("data", dataObj);
		}
		String detailUrl = "/map/arcgis/standardmappage/griderGpsDetail.ftl";
		return detailUrl;
	}

	/**
	 * 单条消防宣传检查甘肃对接数据的查看
	 * @param session
	 * @param resId
	 * @param map
	 * @param typeCode 资源类型
	 *                  fireUnite ：消防部件
	 *                 	griderGps ： 小网格检查
	 *                 	publicityGpsData ：消防宣传检查
	 * @return
	 */
	@RequestMapping(value="/detailofPublicityGpsData")
	public String detailofPublicityGpsData(HttpSession session,
										   @RequestParam(value="resId") Long resId,
										   @RequestParam(value="data", required = false) String data,
										   @RequestParam(value = "typeCode", required=false) String typeCode, ModelMap map) {
		JSONObject dataObj = new JSONObject();
		if(StringUtils.isNotBlank(data)){
			dataObj = JSONObject.fromObject(data);
			map.addAttribute("data", dataObj);
		}
		String detailUrl = "/map/arcgis/standardmappage/publicityGpsDataDetail.ftl";
		return detailUrl;
	}

	/**
	 * 消防部件列表请求
	 * @param map
	 * @param session
	 * @param page
	 * @param rows
	 * @param gridId
	 * @param resName
	 * @param gridname
	 * @param elementsCollectionStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fireUniteListData", method = RequestMethod.POST)
	public Map<String, Object> fireUniteListData(ModelMap map, HttpSession session, @RequestParam(value = "page") int page,
												 @RequestParam(value = "rows") int rows,
												 @RequestParam(value = "gridId") Long gridId,
												 @RequestParam(value = "resName", required = false) String resName,
												 @RequestParam(value = "gridname", required = false) String gridname,
												 @RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		Map<String, Object> resultDataMap = new HashMap<String, Object>(),
							resultMap = new HashMap<String, Object>();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String requestURL = "${ctx}/portal/pri/ic_queryTeamData.action?";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		requestURL =  funConfigurationService.
				changeCodeToValue(ConstantValue.FIRE_CONTROL_DOCKING_URL, ConstantValue.FIRE_KEY_COMPONENTS, IFunConfigurationService.CFG_TYPE_URL, userInfo.getInfoOrgCodeStr());
		String FIRE_URL_GS = "";
		FIRE_URL_GS =  funConfigurationService.
				changeCodeToValue(ConstantValue.FIRE_CONTROL_DOCKING_URL, ConstantValue.FIRE_URL_GS, IFunConfigurationService.CFG_TYPE_URL, userInfo.getInfoOrgCodeStr());
		String params = "";
		if(gridId != null){
//			params = params + "teamId=204773";
			params = params + "teamId=" + gridId;
		}
		JSONObject dataObj = HttpUtil.doBodyPost(requestURL, params);
		System.out.println(dataObj);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(dataObj != null){
			String retcode = dataObj.getString("retcode");
			if(StringUtils.isNotBlank(retcode) && "0".equals(retcode)) {
				JSONArray dataArray = (JSONArray) dataObj.get("retdata");

				List<Map<String, Object>> locationMaplist = new ArrayList<Map<String, Object>>();

				if (dataArray != null) {
					for (int i = 0; i < dataArray.size(); i++) {
						JSONObject data = dataArray.getJSONObject(i);
						Map<String, Object> dataMap = new HashMap<String, Object>();
						Map<String, Object> locationDataMap = new HashMap<String, Object>();
						Double x=0.0;
						Double y =0.0;
						if (isNotBlank(data, "location")) {//位置
							dataMap.put("location", data.getString("location"));
						} else {
							dataMap.put("location", "");
						}
						if (isNotBlank(data, "longitude")) {//x
							if(StringUtils.isNotBlank(data.getString("longitude"))){
								x = Double.parseDouble(data.getString("longitude"));
							}
						}
						if (isNotBlank(data, "latitude")) {//y
							if(StringUtils.isNotBlank(data.getString("latitude"))) {
								y = Double.parseDouble(data.getString("latitude"));
							}
						}
						if(x != null && y != null && x.doubleValue() != 0.0 && y.doubleValue() != 0.0){
							Gps gps = GpsUtil.bd09_To_Gps84(y, x);
							if(gps != null){
								x = gps.getWgLon();
								y = gps.getWgLat();
							}
						}
						if(x != null && x.doubleValue() != 0.0){
							dataMap.put("x", x);
							locationDataMap.put("x", x);
						}
						if(y != null&& y.doubleValue() != 0.0){
							dataMap.put("y", y);
							locationDataMap.put("y", y);
						}

						if (isNotBlank(data, "type")) {
							String type = data.getString("type");
							String name = "";
							if (StringUtils.isNumeric(type)) {
								if (Integer.parseInt(type) == 1) {
									name = "消火栓";
								} else if (Integer.parseInt(type) == 2) {
									name = "水泵结合器";
								} else if (Integer.parseInt(type) == 3) {
									name = "消防水池";
								} else if (Integer.parseInt(type) == 3) {
									name = "消防车取水点";
								} else {
									name = "未知";
								}
								dataMap.put("name", name);
								if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
									locationDataMap.put("name", name);
								}
							}

						} else {
							dataMap.put("name", "未知");
						}
						if (isNotBlank(data, "id")) {//位置
							dataMap.put("wid", data.get("id"));
							if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
								locationDataMap.put("wid", data.getString("id"));
							}
						} else {
							dataMap.put("wid", "");
						}
						if (isNotBlank(data, "createTime")) {//创建时间
							try {
								String createTime = data.getString("createTime");
								Date currdate = df.parse(createTime);
								createTime = df.format(currdate);//获取当前系统时间
								dataMap.put("createTime", createTime);
								if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
									locationDataMap.put("createTime", createTime);
								}
							}catch (Exception e){
								e.printStackTrace();
							}

						} else {
							dataMap.put("createTime", "");
						}
						JSONObject creator = new JSONObject();
						if (isNotBlank(data, "creator")) {
							creator = (JSONObject) data.get("creator");
							if (isNotBlank(creator, "team")) {
								JSONObject team = (JSONObject) creator.get("team");
								if (isNotBlank(team, "name")) {//所属网格
									String gridName = team.getString("name");
									dataMap.put("gridName", gridName);
									if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
										locationDataMap.put("gridName", gridName);
									}
								} else {
									dataMap.put("gridName", "");
								}
							}
						}
						if (isNotBlank(data, "photoUrl")) {//照片
							String photoUrl = FIRE_URL_GS + data.getString("photoUrl");
							dataMap.put("photoUrl", photoUrl);
							if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
								locationDataMap.put("photoUrl", photoUrl);
							}
						}
						if (isNotBlank(data, "status")) {//部件状态
							dataMap.put("status", data.getString("status"));
							if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
								locationDataMap.put("status", data.getString("status"));
							}
						} else {
							dataMap.put("status", "3");
							if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
								locationDataMap.put("status", "3");
							}
						}
						if (StringUtils.isNotBlank(elementsCollectionStr)) {
							dataMap.put("elementsCollectionStr", elementsCollectionStr);
							if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
								locationDataMap.put("elementsCollectionStr", elementsCollectionStr);
							}
						} else {
							dataMap.put("elementsCollectionStr", "");
						}
						dataMap.put("reLoadSummaryData", false);
						if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
							locationDataMap.put("reLoadSummaryData", false);
						}
						if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
							locationMaplist.add(locationDataMap);
						}
						list.add(dataMap);
					}
				}
				if (list != null && list.size() > 0) {
					resultMap.put("locationDatas", list);
					resultMap.put("locationMaplist", locationMaplist);
					resultMap.put("rows", list);
					resultMap.put("total", list.size());
				}
			}
		}
		return resultMap;
	}


	/**
	 * 小网格检查列表
	 * @param map
	 * @param session
	 * @param page
	 * @param rows
	 * @param gridId
	 * @param resName
	 * @param gridname
	 * @param elementsCollectionStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/griderGpsListData", method = RequestMethod.POST)
	public Map<String, Object> griderGpsListData(ModelMap map, HttpSession session,
												 @RequestParam(value = "page") int page,
												 @RequestParam(value = "rows") int rows,
												 @RequestParam(value = "gridId") Long gridId,
												 @RequestParam(value = "resName", required = false) String resName,
												 @RequestParam(value = "beginTime", required = false) String beginTime,
												 @RequestParam(value = "endTime", required = false) String endTime,
												 @RequestParam(value = "dayNum", required = false) Long dayNum,
												 @RequestParam(value = "gridname", required = false) String gridname,
												 @RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		String requestURL = "${ctx }/portal/pri/small_queryGriderGpsData.action?teamId=599";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		requestURL =  funConfigurationService.
				changeCodeToValue(ConstantValue.FIRE_CONTROL_DOCKING_URL, ConstantValue.FIRE_CONTROL,
						IFunConfigurationService.CFG_TYPE_URL, userInfo.getInfoOrgCodeStr());
		String FIRE_URL_GS = "";
		FIRE_URL_GS =  funConfigurationService.
				changeCodeToValue(ConstantValue.FIRE_CONTROL_DOCKING_URL, ConstantValue.FIRE_URL_GS, IFunConfigurationService.CFG_TYPE_URL, userInfo.getInfoOrgCodeStr());
		String params = "";
		if(gridId != null){
			params = params + "teamId=" + gridId;
//			params = params + "teamId=223951";
		}
		if(StringUtils.isBlank(beginTime)){
			if(dayNum != null){
				beginTime = df.format(new Date());//获取当前系统时间
			}
		}
		if(StringUtils.isBlank(endTime)){
			if(dayNum != null) {
				try {
					Calendar ca = Calendar.getInstance();
					ca.set(Calendar.DAY_OF_YEAR, ca.get(Calendar.DAY_OF_YEAR) - dayNum.intValue());
					Date currdate = df.parse(beginTime);
					currdate = ca.getTime();
					endTime = df.format(currdate);//获取当前系统时间
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		if(StringUtils.isNotBlank(beginTime)){
			params = params + "&beginTime="+beginTime;
		}
		if(StringUtils.isNotBlank(endTime)){
			params = params + "&endTime="+endTime;
		}

		JSONObject dataObj = HttpUtil.doBodyPost(requestURL, params);
		System.out.println(dataObj);
		if(dataObj != null){
			String retcode = dataObj.get("retcode") + "";
			if(StringUtils.isNotBlank(retcode) && "0".equals(retcode)) {
				JSONArray inspectionGpsDataArray = (JSONArray) dataObj.get("inspectionGpsData");
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> locationMaplist = new ArrayList<Map<String, Object>>();
				if (inspectionGpsDataArray != null) {
					for (int i = 0; i < inspectionGpsDataArray.size(); i++) {
						JSONObject inspectionGpsData = inspectionGpsDataArray.getJSONObject(i);
						Map<String, Object> dataMap = new HashMap<String, Object>();
						Map<String, Object> locationDataMap = new HashMap<String, Object>();
						Double x=0.0;
						Double y =0.0;
						if (isNotBlank(inspectionGpsData, "longitude")) {//x
							if(StringUtils.isNotBlank(inspectionGpsData.getString("longitude"))) {
								x = Double.parseDouble(inspectionGpsData.getString("longitude"));
							}
						}
						if (isNotBlank(inspectionGpsData, "latitude")) {//y
							if(StringUtils.isNotBlank(inspectionGpsData.getString("latitude"))) {
								y = Double.parseDouble(inspectionGpsData.getString("latitude"));
							}
						}
						if(x != null && y != null && x.doubleValue() != 0.0 && y.doubleValue() != 0.0){
							Gps gps = GpsUtil.bd09_To_Gps84(y, x);
							if(gps != null){
								x = gps.getWgLon();
								y = gps.getWgLat();
							}
						}
						if(x != null && x.doubleValue() != 0.0){
							dataMap.put("x", x);
							locationDataMap.put("x", x);
						}
						if(y != null&& y.doubleValue() != 0.0){
							dataMap.put("y", y);
							locationDataMap.put("y", y);
						}
						if (isNotBlank(inspectionGpsData, "inspectionId")) {//id
							dataMap.put("wid", inspectionGpsData.get("inspectionId"));
							if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
								locationDataMap.put("wid", inspectionGpsData.get("inspectionId"));
							}
						} else {
							dataMap.put("wid", "");
						}
						if (isNotBlank(inspectionGpsData, "checkUnitName")) {//检查单位
							dataMap.put("name", inspectionGpsData.get("checkUnitName"));
							if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
								locationDataMap.put("name", inspectionGpsData.get("checkUnitName"));
							}
						} else {
							dataMap.put("name", "");
						}
						if (isNotBlank(inspectionGpsData, "checkInfo")) {//检查结果
							dataMap.put("checkInfo", inspectionGpsData.get("checkInfo"));
							if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
								locationDataMap.put("checkInfo", inspectionGpsData.get("checkInfo"));
							}
						} else {
							dataMap.put("checkInfo", "");
						}
						if (isNotBlank(inspectionGpsData, "checkTime")) {//检查时间
							dataMap.put("checkTime", inspectionGpsData.get("checkTime"));
							if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
								locationDataMap.put("checkTime", inspectionGpsData.get("checkTime"));
							}
						} else {
							dataMap.put("checkTime", "");
						}
						if (isNotBlank(inspectionGpsData, "inspectionPhotoPath") && StringUtils.isNotBlank(inspectionGpsData.getString("inspectionPhotoPath"))) {//检查照片
							String inspectionPhotoPath = FIRE_URL_GS + inspectionGpsData.getString("inspectionPhotoPath");
							dataMap.put("inspectionPhotoPath", inspectionPhotoPath);
							if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
								locationDataMap.put("inspectionPhotoPath", inspectionGpsData.get("inspectionPhotoPath"));
							}
						}

						if (StringUtils.isNotBlank(elementsCollectionStr)) {
							dataMap.put("elementsCollectionStr", elementsCollectionStr);
							if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
								locationDataMap.put("elementsCollectionStr", elementsCollectionStr);
							}
						} else {
							dataMap.put("elementsCollectionStr", "");
						}
						dataMap.put("reLoadSummaryData", false);
						if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
							locationDataMap.put("reLoadSummaryData", false);
						}
						list.add(dataMap);
						if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
							locationMaplist.add(locationDataMap);
						}
					}
				}
				if (list != null && list.size() > 0) {
					resultMap.put("locationDatas", list);
					resultMap.put("locationMaplist", locationMaplist);
					resultMap.put("rows", list);
					resultMap.put("total", list.size());
				}
			}
		}
		return resultMap;
	}

	/**
	 * 消防宣传列表
	 * @param map
	 * @param session
	 * @param page
	 * @param rows
	 * @param gridId
	 * @param resName
	 * @param gridname
	 * @param elementsCollectionStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/publicityGpsDataListData", method = RequestMethod.POST)
	public Map<String, Object> publicityGpsDataListData(ModelMap map, HttpSession session,
														@RequestParam(value = "page") int page,
														@RequestParam(value = "rows") int rows,
														@RequestParam(value = "gridId") Long gridId,
														@RequestParam(value = "beginTime", required = false) String beginTime,
														@RequestParam(value = "endTime", required = false) String endTime,
														@RequestParam(value = "dayNum", required = false) Long dayNum,
														@RequestParam(value = "resName", required = false) String resName,
														@RequestParam(value = "gridname", required = false) String gridname,
														@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		String requestURL = "${ctx }/portal/pri/small_queryGriderGpsData.action?teamId=599";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		requestURL =  funConfigurationService.
				changeCodeToValue(ConstantValue.FIRE_CONTROL_DOCKING_URL, ConstantValue.FIRE_CONTROL,
						IFunConfigurationService.CFG_TYPE_URL, userInfo.getInfoOrgCodeStr());
		String FIRE_URL_GS = "";
		FIRE_URL_GS =  funConfigurationService.
				changeCodeToValue(ConstantValue.FIRE_CONTROL_DOCKING_URL, ConstantValue.FIRE_URL_GS, IFunConfigurationService.CFG_TYPE_URL, userInfo.getInfoOrgCodeStr());
		String params = "";
		if(gridId != null){
			params = params + "teamId=" + gridId;
//			params = params + "teamId=223951";

		}
		if(StringUtils.isBlank(beginTime)){
			if(dayNum != null){
				beginTime = df.format(new Date());//获取当前系统时间
			}
		}
		if(StringUtils.isBlank(endTime)){
			if(dayNum != null) {
				try {
					Calendar ca = Calendar.getInstance();
					ca.set(Calendar.DAY_OF_YEAR, ca.get(Calendar.DAY_OF_YEAR) - dayNum.intValue());
					Date currdate = df.parse(beginTime);
					currdate = ca.getTime();
					endTime = df.format(currdate);//获取当前系统时间
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}

		if(StringUtils.isNotBlank(beginTime)){
			params = params + "&beginTime="+beginTime;
		}
		if(StringUtils.isNotBlank(endTime)){
			params = params + "&endTime="+endTime;
		}

		JSONObject dataObj = HttpUtil.doBodyPost(requestURL, params);
		System.out.println(dataObj);
		if(dataObj != null){
			String retcode = dataObj.get("retcode") + "";
			if(StringUtils.isNotBlank(retcode) && "0".equals(retcode)) {
				JSONArray inspectionGpsDataArray = (JSONArray) dataObj.get("publicityGpsData");
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> locationMaplist = new ArrayList<Map<String, Object>>();
				if (inspectionGpsDataArray != null) {
					for (int i = 0; i < inspectionGpsDataArray.size(); i++) {
						JSONObject inspectionGpsData = inspectionGpsDataArray.getJSONObject(i);
						Map<String, Object> dataMap = new HashMap<String, Object>();
						Map<String, Object> locationDataMap = new HashMap<String, Object>();
						Double x=0.0;
						Double y =0.0;
						if (isNotBlank(inspectionGpsData, "longitude")) {//x
							if(StringUtils.isNotBlank(inspectionGpsData.getString("longitude"))) {
								x = Double.parseDouble(inspectionGpsData.getString("longitude"));
							}
						}
						if (isNotBlank(inspectionGpsData, "latitude")) {//y
							if(StringUtils.isNotBlank(inspectionGpsData.getString("latitude"))) {
								y = Double.parseDouble(inspectionGpsData.getString("latitude"));
							}
						}
						if(x != null && y != null && x.doubleValue() != 0.0 && y.doubleValue() != 0.0){
							Gps gps = GpsUtil.bd09_To_Gps84(y, x);
							if(gps != null){
								x = gps.getWgLon();
								y = gps.getWgLat();
							}
						}
						if(x != null && x.doubleValue() != 0.0){
							dataMap.put("x", x);
							locationDataMap.put("x", x);
						}
						if(y != null&& y.doubleValue() != 0.0){
							dataMap.put("y", y);
							locationDataMap.put("y", y);
						}
						if (isNotBlank(inspectionGpsData, "publicityId")) {//id
							dataMap.put("wid", inspectionGpsData.get("publicityId"));
							if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
								locationDataMap.put("wid", inspectionGpsData.get("publicityId"));
							}
						} else {
							dataMap.put("wid", "");
						}
						dataMap.put("name", "消防宣传");
						if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
							locationDataMap.put("name", "消防宣传");
						}
						if (isNotBlank(inspectionGpsData, "createTime")) {//宣传时间
							try {
								String createTime = inspectionGpsData.getString("createTime");
								Date currdate = df.parse(createTime);
								createTime = df.format(currdate);//获取当前系统时间
								dataMap.put("createTime", createTime);
								if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
									locationDataMap.put("createTime", createTime);
								}
							}catch (Exception e){
								e.printStackTrace();
							}
						} else {
							dataMap.put("createTime", "");
						}
						if (isNotBlank(inspectionGpsData, "photoPath") && StringUtils.isNotBlank(inspectionGpsData.getString("photoPath"))) {//检查照片
							String photoPath = FIRE_URL_GS + inspectionGpsData.getString("photoPath");
							dataMap.put("photoPath", photoPath);
							if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
								locationDataMap.put("photoPath", photoPath);
							}
						}

						if (StringUtils.isNotBlank(elementsCollectionStr)) {
							dataMap.put("elementsCollectionStr", elementsCollectionStr);
							if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
								locationDataMap.put("elementsCollectionStr", elementsCollectionStr);
							}
						} else {
							dataMap.put("elementsCollectionStr", "");
						}
						dataMap.put("reLoadSummaryData", false);
						if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
							locationDataMap.put("reLoadSummaryData", false);
						}
						list.add(dataMap);
						if(x != null && y != null && x.doubleValue() != 0.0 & y.doubleValue() != 0.0){
							locationMaplist.add(locationDataMap);
						}
					}
				}
				if (list != null && list.size() > 0) {
					resultMap.put("locationDatas", list);
					resultMap.put("locationMaplist", locationMaplist);
					resultMap.put("rows", list);
					resultMap.put("total", list.size());
				}
			}
		}
		return resultMap;
	}

	/**
	 * 判断jsonObject存在键值为key的属性
	 * @param jsonObject
	 * @param key
	 * @return 存在返回true，否则返回false
	 */
	private boolean isNotBlank(JSONObject jsonObject, String key) {
		boolean flag = false;

		if(jsonObject != null && StringUtils.isNotBlank(key)) {
			Object keyObj = jsonObject.get(key);

			flag = keyObj != null && !"null".equals(keyObj.toString());
		}

		return flag;
	}

	/**
	 * 驰名商标地图列表
	 * @param session
	 * @param orgCode
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfTrademark")
	public String trademarkindex(HttpSession session,
								 @RequestParam(value = "gridId")String gridId,
								 @RequestParam(value = "orgCode")String orgCode,
								 @RequestParam(value="elementsCollectionStr",required = false)String elementsCollectionStr,
								 @RequestParam(value = "standard",required = false)String stand,ModelMap map){
		map.addAttribute("orgCode",orgCode);
		map.addAttribute("gridId",gridId);
		map.addAttribute("markType","7781");
		map.addAttribute("elementsCollectionStr",elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardTrademark.ftl";
	}

	/**
	 * 商标数据列表
	 * @param session
	 * @param page
	 * @param rows
	 * @param orgCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/trademarkListData",method = RequestMethod.POST)
	public EUDGPagination trademarkListData(HttpSession session,
											@RequestParam(value="page") int page,
											@RequestParam(value="rows") int rows,
											@RequestParam(value="orgCode") String orgCode,
											@RequestParam(value="unitName",required = false) String unitName,
											@RequestParam(value="unitAddress",required = false) String unitAddress
	){
		if(page<=0) page=1;
		if(unitName!=null) unitName = unitName.trim();
		if(unitAddress!=null) unitAddress = unitAddress.trim();
		Map<String,Object> params = new HashMap();


		if(StringUtils.isNotBlank(orgCode)){
			params.put("gridCode",orgCode);
		}
		if(StringUtils.isNotBlank(unitName)){
			params.put("unitName",unitName);
		}
		if(StringUtils.isNotBlank(unitAddress)){
			params.put("unitAddress",unitAddress);
		}

		EUDGPagination pagination = new EUDGPagination();
		try {
			pagination = trademarkService.searchList(page,rows,params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	/**
	 * 驰名商标详情
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/trademarkDetail")
	public String trademarkDetail(HttpSession session,ModelMap map,
								  @RequestParam(value = "trademarkId") Long trademarkId){
		Trademark trademark = trademarkService.findById(trademarkId);
		map.addAttribute("trademark",trademark);

		//支持多个域名访问综治网格
		map.addAttribute("RESOURSE_SERVER_PATH",getNewUrl(session, App.IMG.getDomain(session)));
		return "/map/arcgis/standardmappage/trademark/trademarkDetail.ftl";

	}

	/**
	 * 驰名商标多点定位
	 * @param session
	 * @param ids
	 * @param mapt
	 * @param markType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisTrademarkLocateDataList")
	public List<ArcgisInfoOfPublic> getArcgisTrademarkLocateDataList(HttpSession session,
																	 @RequestParam(value = "ids") String ids,
																	 @RequestParam(value = "mapt") Integer mapt,
																	 @RequestParam(value = "markType") String markType,
																	 @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr){
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisTrademarkLocateDataListByIds(ids,mapt,markType);
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}

	/**
	 * 守重企业地图列表
	 * @param session
	 * @param orgCode
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toArcgisDataListOfEnterprise")
	public String enterpriseindex(HttpSession session,
								  @RequestParam(value = "gridId")String gridId,
								  @RequestParam(value = "orgCode")String orgCode,
								  @RequestParam(value="elementsCollectionStr",required = false)String elementsCollectionStr,
								  @RequestParam(value = "standard",required = false)String stand,ModelMap map){
		map.addAttribute("orgCode",orgCode);
		map.addAttribute("gridId",gridId);
		map.addAttribute("markType","7782");
		map.addAttribute("elementsCollectionStr",elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardEnterprise.ftl";
	}

	/**
	 * 守重企业数据列表
	 * @param session
	 * @param page
	 * @param rows
	 * @param orgCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/enterpriseListData",method = RequestMethod.POST)
	public EUDGPagination enterpriseListData(HttpSession session,
											 @RequestParam(value="page") int page,
											 @RequestParam(value="rows") int rows,
											 @RequestParam(value="orgCode") String orgCode,
											 @RequestParam(value="enterpriseName",required = false) String enterpriseName,
											 @RequestParam(value="enterpriseAddress",required = false) String enterpriseAddress){
		if(page<=0) page=1;
		if(enterpriseName!=null) enterpriseName = enterpriseName.trim();
		if(enterpriseAddress!=null) enterpriseAddress = enterpriseAddress.trim();
		Map<String,Object> params = new HashMap();


		if(StringUtils.isNotBlank(orgCode)){
			params.put("gridCode",orgCode);
		}
		if(StringUtils.isNotBlank(enterpriseName)){
			params.put("enterpriseName",enterpriseName);
		}
		if(StringUtils.isNotBlank(enterpriseAddress)){
			params.put("enterpriseAddress",enterpriseAddress);
		}

		EUDGPagination pagination = new EUDGPagination();
		try {
			pagination = enterpriseService.searchList(page,rows,params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	/**
	 * 守重企业商标详情
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/enterpriseDetail")
	public String enterpriseDetail(HttpSession session,ModelMap map,
								  @RequestParam(value = "enterpriseId") Long enterpriseId){
		FaithfulEnterprise enterprise = enterpriseService.findById(enterpriseId);
		map.addAttribute("enterprise",enterprise);

		//支持多个域名访问综治网格
		map.addAttribute("RESOURSE_SERVER_PATH",getNewUrl(session, App.IMG.getDomain(session)));
		return "/map/arcgis/standardmappage/faithfulEnterprise/enterpriseDetail.ftl";

	}

	/**
	 * 守重企业多点定位
	 * @param session
	 * @param ids
	 * @param mapt
	 * @param markType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisEnterpriseLocateDataList")
	public List<ArcgisInfoOfPublic> getArcgisEnterpriseLocateDataList(HttpSession session,
																	 @RequestParam(value = "ids") String ids,
																	 @RequestParam(value = "mapt") Integer mapt,
																	 @RequestParam(value = "markType") String markType,
																	 @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr){
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisEnterpriseLocateDataListByIds(ids,mapt,markType);
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	
	@RequestMapping(value = "/npObjsIndex")
	public String npObjsIndex(HttpSession session,ModelMap map){
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		//getData();
		List<ResType> resTypeList = resTypeService.getAllResTypeList();
		map.addAttribute("resTypeList", resTypeList);
		return "/map/arcgis/standardmappage/npObjs/index.ftl";

	}

	@ResponseBody
	@RequestMapping(value = "/getNpObjsData")
	public int getNpObjsData(HttpSession session,
		 	@RequestParam(value = "npFeatureUrl", required=false) String npFeatureUrl,
			@RequestParam(value = "service", required=false) String service,
			@RequestParam(value = "version", required=false) String version,
			@RequestParam(value = "request", required=false) String request,
			@RequestParam(value = "typeName", required=false) String typeName,
		  	@RequestParam(value = "maxFeatures", required=false) String maxFeatures,
		  	@RequestParam(value = "outputFormat", required=false) String outputFormat,
            @RequestParam(value = "photoPath", required=false) String photoPath,
			@RequestParam(value = "resTypeCode", required=false) String resTypeCode){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		int datasTotal = 0;
		try {
			String url = "";
			if(StringUtils.isBlank(npFeatureUrl)){
				npFeatureUrl = "http://117.27.128.42:8085/geo/Nanping_MapCG_Com/ows";
			}
			url = npFeatureUrl + "?";

			if(StringUtils.isBlank(service)){
				service = "WFS";
			}
			url = url + "&service="+service;

			if(StringUtils.isBlank(version)){
				version = "1.0.0";
			}
			url = url + "&version="+version;

			if(StringUtils.isBlank(request)){
				request = "GetFeature";
			}
			url = url + "&request="+request;

			if(StringUtils.isBlank(typeName)){
				typeName = "Nanping_MapCG_Com:comp_0101";
			}
			url = url + "&typeName="+typeName;

			if(StringUtils.isNotBlank(maxFeatures)){
				url = url + "&maxFeatures="+maxFeatures;
			}

			if(StringUtils.isBlank(outputFormat)){
				outputFormat = "GML2";
			}
			url = url + "&outputFormat="+outputFormat;

            if(StringUtils.isBlank(photoPath)){
                photoPath = "http://117.27.128.42:8085/DigitalCityWeb/compImg/nanping/photo/";
            }

			datasTotal = requestNpObjsData(url, resTypeCode, photoPath, userInfo);
		}catch (Exception e){
			e.printStackTrace();
		}
		return datasTotal;
	}

	private int requestNpObjsData(String urlStr, String resTypeCode, String photoPath, UserInfo userInfo) {
		int datasTotal = 0;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			OutputStream out = con.getOutputStream();

			String strQuest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<wfs:GetFeature service=\"WFS\" version=\"1.0.0\" xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/wfs.xsd\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> "
					+ " <wfs:Query typeName=\"Nanping_MapCG_Com:comp_0101\" srsName=\"EPSG:4326\">"
					+ "<ogc:Filter>"
//					+ "<ogc:And> "
//					+ "<ogc:PropertyIsLike wildCard=\"*\" singleChar=\".\" escape=\"!\"> "
//					+ " <ogc:PropertyName>NAME</ogc:PropertyName>"
//					+ " <ogc:Literal>***北京 超市**</ogc:Literal> "
//					+ "</ogc:PropertyIsLike>"
//					+ " </ogc:And>"
					+ "</ogc:Filter>"
					+ "</wfs:Query>"
					+ "</wfs:GetFeature>";

			out.write(strQuest.getBytes());

			out.close();
			//获取数据
			BufferedReader br = new BufferedReader(new InputStreamReader(con
					.getInputStream(),"utf-8"));
			String line = "";
			org.w3c.dom.Document doc = null;
			Element rootElement = null;
			for (line = br.readLine(); line != null; line = br.readLine()) {
				if(StringUtils.isNotBlank(line)) {
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					doc = builder.parse(new InputSource(new StringReader(line)));

					System.out.println(line);

				}
			}
			org.w3c.dom.Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("gml:featureMember");
			//解析数据
			if (nodes != null) {
				List<Map<String, Object>> listMap = new ArrayList<>();
				List<ResInfo> resInfos = new ArrayList<>();
				for (int i = 0; i < nodes.getLength(); i++) {
					String remark = "";
					NamedNodeMap attributes = nodes.item(i).getFirstChild().getAttributes();
					if(attributes.getLength()>0) {
						Node node = attributes.item(0);
						if(node != null) {
							String fid = node.getNodeName();
							String fidVale = node.getNodeValue();
							remark = remark + fid + ":" + fidVale;
						}
					}
					NodeList objList = nodes.item(i).getFirstChild().getChildNodes();
					System.out.println("index:"+i+1);
					String infoName = "";
					String infoCode = "";
					ResInfo resInfo = new ResInfo();
					String picUrl = "";
					byte[] buffer = new byte[1024];
					String fileName = "";
					for(int j=0;j<objList.getLength();j++){
						Map<String, Object> map = new HashMap<>();
						String a =objList.item(j).getNodeName();
						switch (objList.item(j).getNodeName()){
							case NpObjUtil.REGIONCODE:
								ResType resType = new ResType();
								if(StringUtils.isNotBlank(resTypeCode)){
									resType.setTypeCode(resTypeCode);
								}else{
									resType.setTypeCode(NpObjUtil.TYPEMAP.get(objList.item(j).getTextContent()));
								}
								resType = resTypeService.getResTypeByTypeCode(resType.getTypeCode());
								if(resType != null){
									resInfo.setResTypeId(resType.getResTypeId());
								}
								resInfo.setResType(resType);
								break;
							case NpObjUtil.OBJNAME:
								infoName = objList.item(j).getTextContent();
								break;
							case NpObjUtil.BGCODE:
								String infoOrgCode = objList.item(j).getTextContent();
								List<MixedGridInfo> mixedGridInfos = mixedGridInfoService.getMixedGridMappingListByOrgCode(infoOrgCode);
								if(mixedGridInfos != null && mixedGridInfos.size() > 0){
									resInfo.setGridId(mixedGridInfos.get(0).getGridId());
								}
//								resInfo.setGridId(195557L);
								break;
							case NpObjUtil.GEOM:
								String x = objList.item(j).getTextContent().split(",")[0];
								String y = objList.item(j).getTextContent().split(",")[1];
								resInfo.setX(x);
								resInfo.setY(y);
								resInfo.setMapType(IArcgisInfoService.TWO_DIMENSION_MAPT_OF_NEWMAP);
								Map<String, Object> params = new HashMap<>();
								params.put("x", x);
								params.put("y", y);
								
								List<CoordinateInverseQueryGridInfo> gridInfoList = coordinateInverseQueryService.findGridInfo(params);
								if (gridInfoList!=null && gridInfoList.size()>0) {
									resInfo.setGridId(gridInfoList.get(0).getGridId());
								} else {
									List<MixedGridInfo> mixedGridInfo = mixedGridInfoService.getMixedGridMappingListByOrgCode("3507");
									if(mixedGridInfo != null && mixedGridInfo.size() > 0){
										resInfo.setGridId(mixedGridInfo.get(0).getGridId());
									}
								}
								break;
							case NpObjUtil.ZPLJ:
								InputStream netFileInputStream = null;
								String str = objList.item(j).getTextContent();
								String[] photo = null;
								if (str!=null&&str!="") {
									photo =  str.split("\\\\");
								}
								
								if (photo!=null&&photo.length>0) {
									fileName = photo[photo.length-1].toString();
								}
								String path = photoPath + fileName;
								try {
									URL imgUrl = new URL(path);
									URLConnection urlConn = imgUrl.openConnection();
									netFileInputStream = urlConn.getInputStream();
									
									if(netFileInputStream != null) {
										int len = 0;
										//将获取到的附件转换为字节数组
										ByteArrayOutputStream bos = new ByteArrayOutputStream();
										while((len = netFileInputStream.read(buffer)) != -1) {
											bos.write(buffer, 0, len);
										}
										bos.close();
										
										byte[] bytes = bos.toByteArray();
										if(bytes.length > 0) {//上传附件
											picUrl = fileUploadService.uploadSingleFile(fileName, bytes, "zzgrid", "resourceInfo");
										}
									}
								} catch (IOException e) {
									e.printStackTrace();
								} finally {
									try {
										if (netFileInputStream != null) {
											netFileInputStream.close();
										}
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								break;
							case NpObjUtil.OBJCODE:
								remark = remark + "；部件编号：" + objList.item(j).getTextContent();
								infoCode = objList.item(j).getTextContent();
								break;
							case NpObjUtil.ORDATE:
								remark = remark + "；创建日期：" + objList.item(j).getTextContent();
								break;
							case NpObjUtil.DEPTNAME1:
								remark = remark + "；管理部门：" + objList.item(j).getTextContent();
								break;
							case NpObjUtil.CZ:
								remark = remark + "；材质：" + objList.item(j).getTextContent();
								break;
							case NpObjUtil.GG:
								remark = remark + "；规格：" + objList.item(j).getTextContent();
								break;
						}
						
					}
					//remark = remark.replaceFirst("；", "");
					resInfo.setRemark(remark + "；数据来源：数字城管对接。");
					resInfo.setCreateUser(userInfo.getUserId());
					resInfo.setUpdateUser(userInfo.getUserId());
					resInfo.setResName(infoName+"-"+infoCode);
					if(StringUtils.isNotBlank(picUrl)) {
						resInfo.setPhotoPath(picUrl);
					}
					//由于需要再去新增地图标注表，所以无法进行批量插入
					Long resId = resInfoService.findResIdByCode(resInfo.getResName(),resInfo.getResTypeId());
					if (resId!=null && resId>0L) {
						resInfo.setResId(resId);
						resInfoService.updateResInfo(resInfo);
					} else {
						resId = resInfoService.saveResInfoReturnId(resInfo);
					}

					if(resId != null && resId>0l){
						datasTotal = datasTotal + 1;
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return datasTotal;
	}

	/**
	 * 两违案件
	 * @param session
	 * @param gridId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/twoViolation")
	public String grid(HttpSession session,
		   @RequestParam(value="gridId") Long gridId,
		   @RequestParam(value="gridType", required = false) String gridType,
		   @RequestParam(value="regionType", required = false) String regionType,
		   @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr
			, ModelMap map) {
		MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		int gridLevel = mixedGridInfo.getGridLevel();
		map.addAttribute("gridId", gridId);
		if(StringUtils.isNotBlank(gridType)){
			map.addAttribute("gridType", gridType);
		}
		if(StringUtils.isNotBlank(regionType)){
			map.addAttribute("regionType", regionType);
		}
		map.addAttribute("gridLevel", gridLevel);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/twoViolation.ftl";
	}
	
}
