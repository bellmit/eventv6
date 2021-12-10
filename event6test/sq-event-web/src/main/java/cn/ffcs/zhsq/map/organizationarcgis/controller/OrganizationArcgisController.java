package cn.ffcs.zhsq.map.organizationarcgis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.shequ.base.domain.db.OrgExtraInfo;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IOrgConfigService;
import cn.ffcs.shequ.grid.service.IOrgEntityInfoService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.statistics.domain.pojo.StatisticsInfo;
import cn.ffcs.shequ.statistics.service.IGridStatisticsService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.base.local.service.ICommonService;
import cn.ffcs.zhsq.map.organizationarcgis.service.IOrganizationArcgisService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfo;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

@Controller
@RequestMapping(value="/zhsq/map/organizationarcgis/organizationarcgis")
public class OrganizationArcgisController extends ZZBaseController {

	@Autowired
	private IOrgConfigService orgConfigService;
	@Autowired
	private IOrgEntityInfoService orgEntityInfoSvcImpl;
	@Autowired
	private IOrganizationArcgisService organizationArcgisServiceImpl;
	@Autowired
	private IGridStatisticsService gridStatisticsService;
	@Autowired
	private ICommonService commonService;
	
	
	
	@RequestMapping(value="/toMapArcgisOfFireGridCrossDomain")
	public String toMapArcgisOfFireGridCrossDomain(HttpSession session, HttpServletResponse response, ModelMap map
			,@RequestParam(value="orgId") Long orgId) throws Exception{
		String forward = "/map/organizationarcgis/arcgis_draw/display_arcgis_anchorpoint_domain.ftl";
		OrgExtraInfo orgExtraInfo = this.organizationArcgisServiceImpl.getOrgEntityInfo(orgId);
		List<OrgEntityInfoBO> orgEntityInfoList = orgConfigService.getOrgEntityInfoList(orgId);//zzgrid pom更新
		if(orgEntityInfoList!=null && orgEntityInfoList.size()>0) {
			map.addAttribute("isLeaf", "0");
		}else {
			map.addAttribute("isLeaf","1");
		}
		map.addAttribute("orgId", orgId);
		map.addAttribute("orgExtraInfo", orgExtraInfo);
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session)); // zzgrid链接
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
	/**
	 * 2014-09-17 liushi add 
	 * 根据组织id和mapt获取与其同级的的组织的轮廓信息
	 * @param session
	 * @param request
	 * @param map
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDrawDataOfOrgs")
	public List<ArcgisInfo> getArcgisDrawDataOfOrgs(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="orgId") Long orgId
			,@RequestParam(value="mapt") Integer mapt) {
		List<ArcgisInfo> list = this.organizationArcgisServiceImpl.getArcgisDrawDataOfOrgs(orgId, mapt);
		return list;
	}
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfOrg")
	public List<ArcgisInfo> getArcgisDataOfOrg(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="orgId") Long orgId
			,@RequestParam(value="mapt") Integer mapt) {
		List<ArcgisInfo> list = this.organizationArcgisServiceImpl.getArcgisDataOfOrg(orgId, mapt);
		return list;
	}
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfOrgCenter")
	public List<ArcgisInfo> getArcgisDataOfOrgCenter(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="orgId") Long orgId
			,@RequestParam(value="mapt") Integer mapt) {
		List<ArcgisInfo> list = this.organizationArcgisServiceImpl.getArcgisDataOfOrgCenter(orgId, mapt);
		return list;
	}
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfSubOrgs")
	public List<ArcgisInfo> getArcgisDataOfSubOrgs(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="orgId") Long orgId
			,@RequestParam(value="mapt") Integer mapt) {
		List<ArcgisInfo> list = this.organizationArcgisServiceImpl.getArcgisDataOfSubOrgs(orgId, mapt);
		return list;
	}
	@ResponseBody
	@RequestMapping(value="/getArcgisDataOfSubOrgsCenter")
	public List<ArcgisInfo> getArcgisDataOfSubOrgsCenter(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="orgId") Long orgId
			,@RequestParam(value="mapt") Integer mapt) {
		List<ArcgisInfo> list = this.organizationArcgisServiceImpl.getArcgisDataOfSubOrgsCenter(orgId, mapt);
		return list;
	}
	/**
	 * 2014-09-17 liushi add 
	 * 获取中心点位
	 * @param orgId
	 * @param mapt
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getArcgisDataCenterAndLevel")
	public Map<String,Object> getArcgisDataCenterAndLevel(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="orgId") Long orgId
			,@RequestParam(value="mapt") Integer mapt){
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String,Object> obj = new HashMap<String,Object>();
		OrgExtraInfo orgExtraInfo = this.organizationArcgisServiceImpl.getArcgisDataCenterAndLevel(orgId, mapt);
		if(orgExtraInfo == null){
			OrgEntityInfoBO orgEntity = orgEntityInfoSvcImpl.queryEntityByPK(orgId);//zzgrid pom更新
			orgExtraInfo = this.organizationArcgisServiceImpl.getArcgisDataCenterAndLevel(orgEntity.getParentOrgId(), mapt);
		}
		if(orgExtraInfo==null){
			obj= null;
		}else {
			obj.put("x", orgExtraInfo.getLongitude());
			obj.put("y", orgExtraInfo.getLatitude());
			obj.put("zoom", 10);
		}
		result.put("result", obj);
		return result;
	}
	@ResponseBody
	@RequestMapping(value="/saveArcgisDataOfOrg")
	public Map<String,Object> saveArcgisDataOfOrg(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="orgId") Long orgId
			,@RequestParam(value="mapt") Integer mapt
			,@RequestParam(value="x",required=false) Double x
			,@RequestParam(value="y",required=false) Double y
			,@RequestParam(value="hs",required=false) String hs){
		Map<String,Object> result = new HashMap<String,Object>();
		Boolean flag = this.organizationArcgisServiceImpl.saveArcgisDataOfOrg(orgId,mapt,x,y,hs);
		result.put("flag", flag);
		return result;
	}
	@ResponseBody
	@RequestMapping(value="/getOrgStatInfoList")
	public List getOrgStatInfoList(HttpSession session, @RequestParam(value="orgId") Long orgId) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		// -- 获取组织统计信息
		long statType = ConstantValue.STAT_TYPE_ID_FOR_RESIDENTGRID; //普通居民网格
		int enterpriseGridCount = mixedGridInfoService.findCountByInfoOrgIdAndGridType(orgId, ConstantValue.GRID_TYPE_ENTERPRISE);
		if(enterpriseGridCount>0) statType = ConstantValue.STAT_TYPE_ID_FOR_ENTERPRISEGRID; //企业网格
		List<StatisticsInfo> orgStatInfoList = gridStatisticsService.getLastStatisticsInfoByOrgId(orgId);
		//-- modify by guohh 2014.1.2 海沧街道过滤空房间数
		commonService.filterGridStatInfo(orgStatInfoList, userInfo.getOrgCode());
		//-- modify by huangwenbin 2019-9-16 没有其他地方用到接口，提取私有方法，系统管理有接口重名
		filterGridStatInfo(orgStatInfoList, userInfo.getOrgCode());
		return orgStatInfoList;
	}
			
	private void filterGridStatInfo(List<StatisticsInfo> orgStatInfoList, String orgCode) {
		int emptyIndex = -1;
		for(int i=0; i<orgStatInfoList.size(); i++) {
			StatisticsInfo statInfo = orgStatInfoList.get(i);
			if(statInfo.getStatTypeName().contains("空房间")) {
				emptyIndex = i;
				break;
			}
		}
		if(emptyIndex>0) orgStatInfoList.remove(emptyIndex);
		
	}
	
	@ResponseBody
	@RequestMapping(value="/getGridInfoList")
	public List getGridInfoList(HttpSession session, @RequestParam(value="orgId") Long orgId) {		
		// -- 获取用户管理的网格列表
//		orgId = 113214;
		//List<GridInfo> gridInfoList = gridInfoSvcImpl.getGridInfoListByOrgId(orgId);
		
		//--modify by guohh 2013.08.07 改成用新的网格
		List<Long> infoOrgIdList = new ArrayList<Long>();
		infoOrgIdList.add(orgId);
		List<MixedGridInfo> list = mixedGridInfoService.getMixedGridMappingListByOrgIdList(infoOrgIdList, ConstantValue.ORG_TYPE_ALL);
		//--modify by zhongshm 2013.09.04 添加网格过滤
//		List<MixedGridInfo> gridInfoList = new ArrayList<MixedGridInfo>();
//		Iterator<MixedGridInfo> it = list.iterator();
//		while (it.hasNext()) {
//			MixedGridInfo elem = (MixedGridInfo) it.next();
//			if(elem.getGridLevel()<6){
//				gridInfoList.add(elem);
//			}
//		}
		//----start 过滤同在一颗树下的子级节点
		List<MixedGridInfo> gridInfoList = new ArrayList<MixedGridInfo>();
		for(MixedGridInfo gridInfo : list) {
			boolean existsParent = false;
			for(MixedGridInfo subGridInfo : list) {
				if(gridInfo.getParentGridId().longValue()==subGridInfo.getGridId().longValue()) {
					existsParent = true;
					break;
				}
			}
			if(!existsParent) gridInfoList.add(gridInfo);
		}
		//----end
		return gridInfoList;
	}
}





