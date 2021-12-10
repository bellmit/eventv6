package cn.ffcs.zhsq.map.arcgis.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.common.DictPcode;
import cn.ffcs.gmis.mybatis.domain.parkingManage.ParkingManage;
import cn.ffcs.gmis.parkingManage.service.IParkingManageService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.AreaBuildingInfo;
import cn.ffcs.shequ.zzgl.service.grid.IAreaBuildingInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IGridAdminService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfo;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfBuild;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 2014-06-30 chenlf add
 * arcgis "房" 定位数据加载控制器
 * @author chenlf
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisdataofbuilding")
public class ArcgisDataOfBuildingController extends ZZBaseController {
	@Autowired
	private IBaseDictionaryService baseDictionaryService; // 新字典服务
	@Autowired
	private IAreaBuildingInfoService areaBuildingInfoService;
	@Autowired
	private IDictionaryService dictionaryService;
	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	private IGridAdminService gridAdminService; // 网格管理员服务
	@Autowired
    private IParkingManageService parkingManageService; // 两车管理服务 
	@Autowired
	private IArcgisInfoService arcgisInfoService;
	
	/**
	 * 2016-01-05 liushi add
	 * 用于楼宇地图编辑
	 * 页面显示楼宇数据，查询该楼栋所在网格
	 * @param session
	 * @param request
	 * @param map
	 * @param buildingId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDrawDataOfBuildsOfBinding")
	public List<ArcgisInfoOfPublic> getArcgisDrawDataOfBuildsOfBinding(HttpSession session, HttpServletRequest request, ModelMap map
			) {
		List<ArcgisInfoOfPublic> resultList = this.arcgisInfoService.getArcgisDataOfBuildBinding(null);
		return resultList;
	}
	@ResponseBody
	@RequestMapping(value="/updateArcgisDrawDataOfBuildOfBinding")
	public Map<String,Object> updateArcgisDrawDataOfBuildOfBinding(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "id") Long id,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "names") String names,
			@RequestParam(value = "wid") Long wid,
			@RequestParam(value = "name") String name,
			@RequestParam(value="mapt") Integer mapt,
			@RequestParam(value="x") Double x,
			@RequestParam(value="y") Double y,
			@RequestParam(value="hs") String hs
			) {
		int count = 0;
		List<ArcgisInfoOfPublic> resultList = this.arcgisInfoService.getArcgisDataOfBuildBinding(id);
		names = resultList.get(0).getNames();
		ids = resultList.get(0).getIds();
		
		if(ids != null && ids != "") {
			if(ids.indexOf(String.valueOf(wid))<0) {
				ids = ids+","+String.valueOf(wid);
				names = names +","+ name;
			}
		}else {
			ids = String.valueOf(wid);
			names = name;
		}
		this.arcgisInfoService.updateArcgisDataOfBuildBinding(id,ids,names);
		
		ArcgisInfoOfBuild arcgisInfoOfBuild = new ArcgisInfoOfBuild();
		arcgisInfoOfBuild.setWid(wid);
		arcgisInfoOfBuild.setMapt(mapt);
		arcgisInfoOfBuild.setX(x);
		arcgisInfoOfBuild.setY(y);
		arcgisInfoOfBuild.setHs(hs);
		arcgisInfoOfBuild.setAreaColor("#ff0000");
		arcgisInfoOfBuild.setNameColor("#ff0000");
		arcgisInfoOfBuild.setLineColor("#ff0000");
		arcgisInfoOfBuild.setLineWidth(1);
		boolean flag = this.arcgisInfoService.saveArcgisDrawAreaOfBuild(arcgisInfoOfBuild);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("flag", flag);
		return result;
	}
	@ResponseBody
	@RequestMapping(value="/updateMapDataOfBuildOfBinding")
	public Map<String,Object> updateMapDataOfBuildOfBinding(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "wid") Long wid,
			@RequestParam(value="mapt") Integer mapt,
			@RequestParam(value="x",required = false) String x,
			@RequestParam(value="y",required = false) String y,
			@RequestParam(value="hs",required = false) String hs,
			@RequestParam(value="saveCenterPointFlag",required = false) Boolean saveCenterPointFlag
			) {
		ArcgisInfoOfBuild arcgisInfoOfBuild = new ArcgisInfoOfBuild();
		List<ArcgisInfoOfBuild> arcgisInfoOfBuild2 = new ArrayList<ArcgisInfoOfBuild>();
		if(wid != null){
			arcgisInfoOfBuild.setWid(wid);
		}
		if(mapt != null){
			arcgisInfoOfBuild.setMapt(mapt);
		}
		arcgisInfoOfBuild2 = arcgisInfoService.getArcgisDataOfBuild(wid, mapt);
		
		if(StringUtils.isNotBlank(x) && !"undefined".equals(x) && !"NaN".equals(x)){
			arcgisInfoOfBuild.setX(Double.parseDouble(x));
		}
		if(StringUtils.isNotBlank(y) && !"undefined".equals(y) && !"NaN".equals(y)){
			arcgisInfoOfBuild.setY(Double.parseDouble(y));
		}
		
		if(StringUtils.isNotBlank(hs) && !"undefined".equals(hs)){
			//如果已经有数据了，编辑轮廓时，有中心坐标值的话就不更新中心点坐标
			if(arcgisInfoOfBuild2 != null && arcgisInfoOfBuild2.size()>0){
				if(StringUtils.isNotBlank(arcgisInfoOfBuild2.get(0).getHs())){
					if((arcgisInfoOfBuild2.get(0).getX() == null && arcgisInfoOfBuild2.get(0).getY()==null) || saveCenterPointFlag){
						if(StringUtils.isNotBlank(x) && !"undefined".equals(x)){
							arcgisInfoOfBuild.setX(Double.parseDouble(x));
						}
						if(StringUtils.isNotBlank(y) && !"undefined".equals(x)){
							arcgisInfoOfBuild.setY(Double.parseDouble(y));
						}
					} else {
						arcgisInfoOfBuild.setX(null);
						arcgisInfoOfBuild.setY(null);
					}
				}
			}
			arcgisInfoOfBuild.setHs(hs);
		}
		if(hs != null && "undefined".equals(hs)){
			arcgisInfoOfBuild.setHs("");
		}
		arcgisInfoOfBuild.setAreaColor("#ff0000");
		arcgisInfoOfBuild.setNameColor("#ff0000");
		arcgisInfoOfBuild.setLineColor("#ff0000");
		arcgisInfoOfBuild.setLineWidth(1);
		boolean flag = this.arcgisInfoService.saveArcgisDrawAreaOfBuild(arcgisInfoOfBuild);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("flag", flag);
		return result;
	}
	/**
	 * 房模块数据页
	 * @param session
	 * @param orgCode 信息域组织编码
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toArcgisDataListOfBuilding")
	public String toArcgisDataListOfBuilding(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "type" , required=false) String type,
			@RequestParam(value = "useNature" , required=false) String useNature
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		String forward = "/map/arcgis/standardmappage/standardBuilding.ftl";
		map.addAttribute("type", type);//房类型
		map.addAttribute("gridId", gridId);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		
		if(useNature != null && !"".equals(useNature)) {
			map.addAttribute("type","");
			map.addAttribute("useNature",useNature);
			map.addAttribute("elementsCollectionStr",elementsCollectionStr);
		}else {
			if("wyglzz".equals(type)){//物业管理住宅
				map.addAttribute("useNature","001");
			}else if("sqtgzz".equals(type)){//社区托管住宅
				map.addAttribute("useNature","002");
			}else if("dwzgzz".equals(type)){//单位自管住宅
				map.addAttribute("useNature","003");
			}else if("dwl".equals(type)){//单位楼
				map.addAttribute("useNature","007");
			}else if("xzl".equals(type)){//写字楼
				map.addAttribute("useNature","006");
			}else if("cszht".equals(type)){//城市综合体
				map.addAttribute("useNature","008");
			}else if("gd".equals(type)){//工地
				map.addAttribute("useNature","016");
			}else if("gygc".equals(type)){//公园广场
				map.addAttribute("useNature","009");
			}else if("ss".equals(type)){//宿舍
				map.addAttribute("useNature","004");
			}else if("xx".equals(type)){//学校
				map.addAttribute("useNature","019");
			}else if("mf".equals(type)){//民房
				map.addAttribute("useNature","005");
			}else if("cf".equals(type)){//厂房
				map.addAttribute("useNature","021");
			}
			map.addAttribute("type",type);
		}
		return forward;
	}
	@RequestMapping(value = "/toArcgisDataListOfBuildingOne")
	public String toArcgisDataListOfBuilding(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "type" , required=false) String type
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		String forward = "/map/arcgis/standardmappage/standardBuildingOne.ftl";
		map.addAttribute("gridId", gridId);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("type","");
		map.addAttribute("AREA_BUILDING_INFO_USE_NATURE", DictPcode.AREA_BUILDING_INFO_USE_NATURE);
		map.addAttribute("elementsCollectionStr",elementsCollectionStr);
		return forward;
	}
	/**
	 * 楼房信息汇总表数据
	 * @param session
	 * @param page 
	 * @param rows
	 * @param orgCode
	 * @param name
	 * @param type
	 * @param gender
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination listData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId") Long gridId, @RequestParam(value="buildingName", required=false) String buildingName,
			@RequestParam(value="customCode", required=false) String customCode,
			@RequestParam(value="addressKey", required=false) String addressKey,
			@RequestParam(value="useNature", required=false) String useNature,
			@RequestParam(value="mapt", required=false) String mapt,
			@RequestParam(value="buildingYear", required=false) String buildingYear
			
	) {
		if(page<=0) 
			page=1;
		if(buildingName!=null) 
			buildingName = buildingName.trim();
		if(addressKey!=null) 
			addressKey = addressKey.trim();
		if(useNature!=null) 
			useNature = useNature.trim();
		
		if(mapt!=null) 
			mapt = mapt.trim();
		
		if(customCode!=null){
			customCode=customCode.trim();
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", gridId);
		params.put("buildingName", buildingName);
		params.put("addressKey", addressKey);
		params.put("customCode", customCode);
		params.put("useNature", useNature);
		params.put("mapt", mapt);

		if(buildingYear!=null&&!"".equals(buildingYear)){
			params.put("buildingYear", buildingYear);
		}else{
			params.put("buildingYear", null);
		}
		
		cn.ffcs.common.EUDGPagination pagination = areaBuildingInfoService.findAreaBuildingInfoPagination(page, rows, params);
	    
		@SuppressWarnings("unchecked")
		List<AreaBuildingInfo>	list=(List<AreaBuildingInfo>) pagination.getRows();
	     for(int i=0;i<list.size();i++){
	    	 AreaBuildingInfo record=list.get(i);
	    	 if(!StringUtils.isBlank(record.getUseNature())) {
					record.setUseNatureLabel(dictionaryService.getTableColumnText(ConstantValue.TABLE_AREA_BUILDING_INFO, ConstantValue.COLUMN_AREA_BUILDING_INFO_USE_NATURE, record.getUseNature()));
				}
	     }
	     
		return pagination;
	}
	
	/**
	 * 2014-06-30 chenlf add
	 * 获取"房"的定位信息
	 * @param session
	 * @param ids 房ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfBuilding")
	public List<ArcgisInfoOfPublic> getArcgisGlobalEyesLocateDataList(HttpSession session,
			@RequestParam(value = "buildingIds") String buildingIds,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "name", required=false) String name,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "showType") String showType
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		
		List<ArcgisInfoOfPublic> list = new ArrayList<ArcgisInfoOfPublic>();
		
		if (StringUtils.isNotBlank(buildingIds)) {
			list = this.arcgisDataOfLocalService.getArcgisBuildingLocateDataListByIds(buildingIds,mapt);
			for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
				arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
			}
		}
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getLocateDataListOfBuilding")
	public List<ArcgisInfo> getLocateDataListOfBuilding(HttpSession session,
			@RequestParam(value = "infoOrgCode") String infoOrgCode,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "elementsCollectionStr", required = false) String elementsCollectionStr) {
		if (StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = super.getDefaultInfoOrgCode(session);
		}
		List<ArcgisInfo> list = this.arcgisDataOfLocalService.getBuildingLocateDataList(infoOrgCode, mapt);
		for (ArcgisInfo arcgisInfo : list) {
			arcgisInfo.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	
	///---两车管理start---------------------------------------------
	/**
	 * 两车管理数据页
	 * @author huangmw
	 * @time 2015年4月22日 add
	 * @param session
	 * @param request
	 * @param map
	 * @param gridId
	 * @param elementsCollectionStr
	 * @return
	 */
	@RequestMapping(value = "/toArcgisDataListOfParkingManage")
	public String toArcgisDataListOfParkingManage(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "gridId") Long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String forward = "/map/arcgis/standardmappage/parkingManage/standardParkingManage.ftl";
		map.addAttribute("gridId", gridId);
		List<BaseDataDict> typeName = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.PARKING_MANAGE_TYPE, userInfo.getOrgCode());
		map.addAttribute("typeName", typeName);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return forward;
	}
	
	/**
	 * 两车管理信息汇总表数据
	 * @param page 
	 * @param rows
	 * @param name
	 * @param type
	 * @param placeAddr
	 * @param gridId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/parkingManageListData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination parkingManageListData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="keyWord", required=false) String keyWord,@RequestParam(value="gridId") Long gridId,
			@RequestParam(value="type", required=false) String type) {
		if(page<=0) page=1;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		params.put("keyWord", keyWord);
		params.put("gridId", gridId);
		cn.ffcs.common.EUDGPagination pagination = parkingManageService.findParkingManagePagination(page, rows, params);
		return pagination;
	}
	
	/**
	 * 获取两车管理的定位信息
	 * @param session
	 * @param ids ids
	 * @param mapt 地图类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfParkingManage")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfParkingManage(HttpSession session,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt") Integer mapt, 
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfoOfPublic> list = this.arcgisDataOfLocalService.getArcgisParkingManageLocateDataListByIds(ids,mapt);
		for(ArcgisInfoOfPublic arcgisInfoOfPublic:list){
			arcgisInfoOfPublic.setElementsCollectionStr(elementsCollectionStr);
		}
		return list;
	}
	
	/**
	 * @Description: 两车管理概要信息
	 * @param session
	 * @param pmId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getParkingManageInfoDetailOnMap")
	public String getParkingManageInfoDetailOnMap(HttpSession session, @RequestParam(value="pmId") Long pmId, ModelMap map) {
		// 便民服务网点信息
		ParkingManage parkingManage = this.parkingManageService.findParkingManageById(pmId);
		map.addAttribute("parkingManage", parkingManage);
		return "/map/arcgis/standardmappage/parkingManage/parkingManageInfoDetailOnMap.ftl";
	}
	
	///---两车管理end---------------------------------------------
	
	/**
	 * 楼房信息详情
	 * @param session
	 * @param buildingId
	 * @param map
	 * @return
	 */
/*	@RequestMapping(value="/detail")
	public String detail(HttpSession session, @RequestParam(value="buildingId") Long buildingId, ModelMap map) {
		 UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		AreaBuildingInfo record = areaBuildingInfoService.findAreaBuildingInfoById(buildingId);
		this.addExtraInfo(record);
		List<Map<String, Object>> typeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_DICTIONARY_CI_RS, ConstantValue.COLUMN_CI_RS_TYPE, userInfo.getOrgCode());
	    map.addAttribute("typeDC", typeDC);
		map.addAttribute("record", record);
		map.addAttribute("PLATFORM_DOMAIN_ROOT", ConstantValue.PLATFORM_DOMAIN_ROOT);
		map.addAttribute("RESIDENT_DOMAIN", ConstantValue.RESIDENT_DOMAIN);
		map.addAttribute("RESOURSE_SERVER_PATH",  ConstantValue.getRootResourseServerPath());
		return "/map/arcgis/standardmappage/buildingDetail.ftl";
		
	}
	
	//----------------------------------------------
	private void addExtraInfo(AreaBuildingInfo record) {
		//-- 晋江需求 modify by guohh 管理单位如果在楼栋中没填，那么该字段显示所属网格
		if(StringUtils.isBlank(record.getManagementCompany())) {
			record.setManagementCompany(record.getGridName());
		}
		//-- 如果管理员电话没填，那么该字段显示为所属网格的网格管理员和联系电话（有几个网格员显示几个）
		if(StringUtils.isBlank(record.getAdminPhone())) {
			List<GridAdmin> gridAdminList = gridAdminService.getGridAdminListByGridId(record.getGridId());
			if(gridAdminList!=null && gridAdminList.size()>0) {
				StringBuilder sb = new StringBuilder();
				for(int i=0; i<gridAdminList.size(); i++) {
					GridAdmin admin = gridAdminList.get(i);
					sb.append(admin.getPartyName()==null?"":admin.getPartyName());
					sb.append(" - ");
					sb.append(admin.getFixedTelephone()==null?"<span style='color:red;'>无电话</span>":admin.getFixedTelephone());
					if(i<(gridAdminList.size()-1)) {
						sb.append(";;");
					}
				}
				record.setAdminPhone(sb.toString());
			}
		}
	}*/
}
