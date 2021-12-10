package cn.ffcs.zhsq.map.arcgis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.workflow.om.QueryBean;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.be.bo.arcgis.ArcgisInfoOfPublicBo;
import cn.ffcs.zhsq.be.bo.building.BuildingBo;
import cn.ffcs.zhsq.be.bo.corpbase.CorpBaseBo;
import cn.ffcs.zhsq.be.bo.event.Event;
import cn.ffcs.zhsq.be.service.arcgis.ArcgisInfoService;
import cn.ffcs.zhsq.be.service.building.BuildingService;
import cn.ffcs.zhsq.be.service.corpbase.CorpBaseService;
import cn.ffcs.zhsq.be.service.event.EventService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfBuild;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 2014-01-22
 * 专门用于获取楼宇经济的地图数据
 * @author wangzhihong
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisdataoflyjj")
public class ArcgisDataOfLyjjController extends ZZBaseController  {
	@Resource(name="buildingService")
	private BuildingService buildingService;
	@Autowired
	private CorpBaseService corpBaseService ;
	@Resource(name="lyjjArcgisInfoService")
	private ArcgisInfoService lyjjArcgisInfoService;
	
	@Autowired
	private EventService eventService;
	
	
	/**
	 * 地图楼栋列表数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toLyjjBuildingIndex")
	public String toLyjjBuildingIndex(HttpSession session,HttpServletRequest request, @RequestParam(value="orgCode") String orgCode,
			@RequestParam(value="standard", required=false) String standard, ModelMap map) {
		request.setAttribute("orgCode", orgCode);
		return "/map/arcgis/standardmappage/lyjj/arcgis_lyjj_building.ftl";
	}
	
	/**
	 * 地图企业列表数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toLyjjCorpIndex")
	public String toLyjjCorpIndex(HttpSession session,HttpServletRequest request, @RequestParam(value="orgCode") String orgCode ,
			@RequestParam(value="standard", required=false) String standard, ModelMap map) {
		request.setAttribute("orgCode", orgCode);
		return "/map/arcgis/standardmappage/lyjj/arcgis_lyjj_corp.ftl";
	}
	
	/**
	 * 地图事件列表数据页
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toLyjjEventIndex")
	public String toLyjjEventIndex(HttpSession session,HttpServletRequest request, @RequestParam(value="orgCode") String orgCode ,
			@RequestParam(value="standard", required=false) String standard, ModelMap map) {
		request.setAttribute("orgCode", orgCode);
		
		map.addAttribute("FORM_TYPE_MAP", Event.FORM_TYPE_MAP);
		map.addAttribute("FORM_OPT_MAP", Event.FORM_OPT_MAP);
		return "/map/arcgis/standardmappage/lyjj/arcgis_lyjj_event.ftl";
	}
	
	/**
	 * 获取楼宇经济楼栋数据
	 */
	@ResponseBody
	@RequestMapping(value ="/getArcgisLyjjBuildingDataList")
	public EUDGPagination getArcgisLyjjBuildingDataList(HttpSession session,HttpServletRequest request,BuildingBo buildingBo,@RequestParam(value="orgCode") String orgCode,
			@RequestParam(value="buildingName", required=false) String buildingName,
			@RequestParam(value="buildingType", required=false) String buildingType,
			@RequestParam(value="buildingStatus", required=false) String buildingStatus) {
		
		/***默认取登陆人网格的楼栋数据***/
		String ss = getMixedGridInfo(session,request).getGridCode();
		System.out.println(ss+"   "+orgCode);
		if(StringUtils.isEmpty(orgCode)){
			buildingBo.setBuildingGridCode(getMixedGridInfo(session,request).getGridCode());
		}else{
			buildingBo.setBuildingGridCode(orgCode);
		}
		
		List<BuildingBo> list = buildingService.loadPagination(buildingBo);
		long total = buildingService.queryListCount(buildingBo);
		
		EUDGPagination pagination = new EUDGPagination(total,list);
		return pagination;
	}
	
	/**
	 * 获取"楼宇"的定位信息
	 * @param session
	 * @param ids 楼宇ids
	 * @param mapt 地图类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfBuilding")
	public List<ArcgisInfoOfPublicBo> getArcgisLocateDataListOfBuilding(HttpSession session,
			@RequestParam(value = "buildingIds") String buildingIds, @RequestParam(value = "mapt") Integer mapt) {
		List<ArcgisInfoOfPublicBo> list = this.lyjjArcgisInfoService.getArcgisBuildingLocateDataListByIds(buildingIds,mapt);
		return list;
	}
	
	/**
	 * 获取"企业"的定位信息
	 * @param session
	 * @param ids 楼宇ids
	 * @param mapt 地图类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfCorp")
	public List<ArcgisInfoOfPublicBo> getArcgisLocateDataListOfCorp(HttpSession session,
			@RequestParam(value = "corpIds") String corpIds, @RequestParam(value = "mapt") Integer mapt) {
		List<ArcgisInfoOfPublicBo> list = this.lyjjArcgisInfoService.getArcgisCorpLocateDataListByIds(corpIds,mapt);
		return list;
	}
	
	/**
	 * 获取"事件"的定位信息
	 * @param session
	 * @param ids 楼宇ids
	 * @param mapt 地图类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfEvent")
	public List<ArcgisInfoOfPublicBo> getArcgisLocateDataListOfEvent(HttpSession session,
			@RequestParam(value = "eventIds") String eventIds, @RequestParam(value = "mapt") Integer mapt) {
		List<ArcgisInfoOfPublicBo> list = this.lyjjArcgisInfoService.getArcgisLocateDataOfEvent(eventIds,mapt);
		return list;
	}
	
	/**
	 * 楼宇名片
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/viewBuildingDetail")
	public String viewBuildingDetail(HttpSession session,HttpServletRequest request, ModelMap map,
			@RequestParam(value="buildingId") Long buildingId) {
		BuildingBo buildingBo = this.buildingService.getBuiding(buildingId);
		request.setAttribute("buildingBo", buildingBo);
		
		return "/map/arcgis/standardmappage/lyjj/arcgis_lyjj_building_view.ftl";
	}
	
	
	/**
	 * 获取楼宇经济企业数据
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLyjjCorpDataList")
	public EUDGPagination getArcgisLyjjCorpBaseDataList(HttpSession session,HttpServletRequest request,CorpBaseBo corpBaseBo,@RequestParam(value="orgCode") String orgCode,
			@RequestParam(value="corpName", required=false) String corpName,
			@RequestParam(value="corpStatus", required=false) String corpStatus )  {
		if(StringUtils.isEmpty(orgCode)){
			corpBaseBo.setNowGridCode(getMixedGridInfo(session,request).getGridCode());
		}else{
			corpBaseBo.setNowGridCode(orgCode);
		}
		List<CorpBaseBo>  list = corpBaseService.loadPagination(corpBaseBo);
		long total = corpBaseService.queryListCount(corpBaseBo);
		
		EUDGPagination pagination = new EUDGPagination(total,list);
		return pagination;
	}
	
	/**
	 * 获取楼宇经济事件数据
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLyjjEventDataList")
	public cn.ffcs.system.publicUtil.EUDGPagination getArcgisLyjjEventDataList(HttpSession session,HttpServletRequest request,Event event)throws Exception  {
		
		QueryBean queryBean=new QueryBean();
		queryBean.setPageNo(event.getPage());
		queryBean.setPageSize(event.getRows());
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		queryBean.setUserId(userInfo.getUserId());
		queryBean.setOrgId(userInfo.getOrgId());
		
		cn.ffcs.system.publicUtil.EUDGPagination  aa=eventService.queryTodoList(queryBean, event);
		return aa;
	}
	
	/**
	 * 法人、企业GIS信息
	 * @param session
	 * @param map
	 * @param res
	 * @param ids
	 * @param mapt
	 * @param showType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisCorLocateDataList")
	public List<ArcgisInfoOfPublic> getArcgisCorLocateDataList(HttpSession session, ModelMap map, HttpServletResponse res,
			@RequestParam(value="ids") String ids, @RequestParam(value="mapt", required=false) Integer mapt,
			@RequestParam(value = "showType") String showType) {
		
		
		return null;
		
	}
	
	/**
	 * 楼宇轮廓编辑
	 */
	@RequestMapping(value="/toArcgisDrawAreaPanel")
	public String toArcgisDrawAreaPanel(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="targetType") String targetType
			,@RequestParam(value="wid") Long wid) throws Exception{
		String forward = "/map/arcgis/arcgis_draw/lyjj/draw_area_panel.ftl";
		String name = "";
		Long parentGridId = null;
		Long gridId = null;
		if("build".equals(targetType)) {
			BuildingBo building = buildingService.getBuiding(wid);
			String gridCode = building.getBuildingGridCode();
			MixedGridInfo info = getGridInfoByOrgCode(gridCode);
			gridId = info.getGridId();
			name = building.getBuildingName();
			parentGridId = gridId;
		}
		map.put("targetType", targetType);
		map.put("wid", wid);
		map.addAttribute("projectPath", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath());
		map.addAttribute("parentGridId", parentGridId+"");
		map.addAttribute("gridId", gridId+"");
		map.addAttribute("wid", wid.toString());
		map.addAttribute("name", name);
		return forward;
	}
	
	/**
	 * 页面显示楼宇数据显示该网格下面所有（包括子网格的）楼宇数据
	 * @param session
	 * @param request
	 * @param map
	 * @param buildingId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfBuilds")
	public List<ArcgisInfoOfBuild> getArcgisDataOfBuilds(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="gridId") Long gridId
			,@RequestParam(value="gridCode") String gridCode
			,@RequestParam(value="mapt") Integer mapt) {
		return null;
	}
	
	
	/**
	 * 根据事件ID,获取事件流程信息
	 */
	@ResponseBody
	@RequestMapping(value="/getLyjjEventInfo", method=RequestMethod.POST)
	public Map<String, Object> getLyjjEventInfo(HttpSession session,Event event,ModelMap map,HttpServletRequest request){
		
		
		//如果有传了eventId过来，则会查询当前这条事件的数据
		QueryBean queryBean=new QueryBean();
		queryBean.setPageNo(event.getPage());
		queryBean.setPageSize(event.getRows());
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		queryBean.setUserId(userInfo.getUserId());
		queryBean.setOrgId(userInfo.getOrgId());
		cn.ffcs.system.publicUtil.EUDGPagination  aa=eventService.queryTodoList(queryBean, event);
		
		Map<String,Object> params = new HashMap<String,Object>();
		if (aa.getRows().size()==1) {
			params.put("result", "success");
			params.put("record", aa.getRows().get(0));
		} else {
			params.put("result", "error");
		}
		return params;
	}
	
}
