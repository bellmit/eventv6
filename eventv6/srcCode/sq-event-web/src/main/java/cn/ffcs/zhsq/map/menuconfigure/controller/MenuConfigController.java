package cn.ffcs.zhsq.map.menuconfigure.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.common.TreeNode;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.map.menuconfigure.service.IPageContItemCfgService;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisOrgAcc;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.PageContItemCfg;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.PageIndexCfg;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.ZTreeNode;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

@Controller
@RequestMapping(value="/zhsq/map/menuconfigure/menuConfig")
public class MenuConfigController  extends ZZBaseController{
	
	@Autowired
	private IMenuConfigService menuConfigService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IMixedGridInfoService iMixedGridInfoService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoOutService;
	
	@Autowired
	private IPageContItemCfgService pageContItemCfgService;
	
	@ResponseBody
	@RequestMapping(value="/exists")
	public Map<String, Object> exists(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "orgCode", required=false) String orgCode,
			@RequestParam(value = "pgIdxType", required=false) String pgIdxType,
			@RequestParam(value = "pgIdxId", required=false) Integer pgIdxId){
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("infoOrgCode", orgCode);
		params.put("orgCode", this.getDefaultInfoOrgCode(session));
		params.put("pgIdxType", pgIdxType);
		PageIndexCfg pageIndexCfg = pageContItemCfgService.findByOrgCode(params);
		Map<String, Object> resultmap = new HashMap<String,Object>();
		if(null == pageIndexCfg){
			resultmap.put("result", true);
		}else if(pageIndexCfg.getPgIdxCfgId().equals(pgIdxId)){
			resultmap.put("result", true);
		}else{
			resultmap.put("result", false);
		}
		return resultmap;
	}
	
	@ResponseBody
	@RequestMapping(value="/del")
	public Map<String, Object> del(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = true) Long idStr){
		Long result = pageContItemCfgService.del(idStr);
		Map<String, Object> resultmap = new HashMap<String,Object>();
		if(result>0){
			resultmap.put("result", true);
		}else{
			resultmap.put("result", false);
		}
		return resultmap;
	}
	
	@RequestMapping(value = "/listPageIndexCfg")
	public String listRelatedEvents(HttpSession session, 
			@RequestParam(value = "gridCode", required=false) String gridCode,
			@RequestParam(value = "r", required=false) String r,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		if(StringUtils.isBlank(gridCode)){
			gridCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString();
		}
		
		map.addAttribute("gridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("gridName", defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("gridCode", gridCode);
		map.addAttribute("admin", "admin".equals(r) ? true : false);
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		return "/map/menuconfigure/list_menuConfig.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/listPageIndexCfgData", method = RequestMethod.POST)
	public EUDGPagination listRelatedEventsData(
			HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows){
		if (page <= 0)
			page = 1;
		String infoOrgCode = request.getParameter("gridCode");
		String pgIdxType = request.getParameter("pgIdxType");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("infoOrgCode", infoOrgCode);
		params.put("pgIdxType", pgIdxType);
		params.put("orgCode", this.getDefaultInfoOrgCode(session));
		EUDGPagination record = pageContItemCfgService.findPageIndexCfgPagination(page, rows, params);
		return record;
	}
	
	@ResponseBody 
	@RequestMapping(value="/saveMenuConfig")
	public String saveMenuConfig(HttpSession session, HttpServletRequest request, ModelMap map){
		String orgCode = request.getParameter("orgCode");
	    String srcOrgCode = request.getParameter("srcOrgCode");
	    String srcPgIdxType = request.getParameter("srcPgIdxType");
		String strNodes = request.getParameter("strNodes"); //保存的动态菜单树所有节点信息
		String pgIdxType = request.getParameter("pgIdxType");//ARCGIS_STANDARD_HOMEpgIdxId
		String pgIdxId = request.getParameter("pgIdxId");
		String status = request.getParameter("status");
		if(StringUtils.isBlank(pgIdxType)){
			pgIdxType = "ARCGIS_STANDARD_HOME";
		}
		String displayStyle = request.getParameter("displayStyle");//ARCGIS_STANDARD_HOME
		JSONArray jsonArray = JSONArray.fromObject(strNodes);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgCode", orgCode);
//		params.put("pgIdxType", pgIdxType);
//		pageContItemCfgService.findPageContItemCfg(pageContItemCfg);
//		PageContItemCfg pageContItemCfg = new PageContItemCfg();
		List<PageContItemCfg> pageContItemCfgs = new ArrayList<PageContItemCfg>();
		for(int i=0; i<jsonArray.size(); i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			int id = (Integer) jsonObject.get("id");
			int pId = -1;
			if(!"null".equals(jsonObject.get("pId").toString())){
				pId = (Integer) jsonObject.get("pId");
			}
			if(id == pId){
				return "System Error: ID==PARENTID";
			}
			String name = (String)jsonObject.get("name");
			Integer displayOrder = (Integer) jsonObject.get("index");
			
			PageContItemCfg pageContItemCfg = new PageContItemCfg();
			pageContItemCfg.setName(name);
			pageContItemCfg.setContItemId(id);
			pageContItemCfg.setpContItemId(pId);
			pageContItemCfg.setDisplayOrder(displayOrder);
			pageContItemCfgs.add(pageContItemCfg);
		}
		pageContItemCfgService.savePageContItemCfgs(jsonArray,srcOrgCode, srcPgIdxType, orgCode, pgIdxType, displayStyle, pgIdxId, status);
		return "";
	}

	@RequestMapping(value="/menuConfig")
	public String menuConfig(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = false) Long pgIdxCfgId){
		Map<String, Object> defaultOrgInfo = this.getDefaultGridInfo(session);
		String orgCode = (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		String gridName = (String)defaultOrgInfo.get(KEY_START_GRID_NAME);
		Long gridId = (Long)defaultOrgInfo.get(KEY_START_GRID_ID);
		map.put("orgCode", orgCode);
		map.put("gridName", gridName);
		map.put("gridId", gridId);
		PageIndexCfg pageIndexCfg = new PageIndexCfg();
		if(null != pgIdxCfgId){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pgIdxCfgId", pgIdxCfgId);
			params.put("orgCode", orgCode);
			pageIndexCfg = pageContItemCfgService.findById(params);
		}else{
			pageIndexCfg.setGridName(gridName);
			pageIndexCfg.setRegionCode(orgCode);
			pageIndexCfg.setGridId(gridId);
			pageIndexCfg.setStatus("1");
			pageIndexCfg.setPgIdxType("ARCGIS_STANDARD_HOME");
			pageIndexCfg.setDisplayStyle("0");
		}
		map.put("pageIndexCfg", pageIndexCfg);
		map.put("admin", Boolean.valueOf(request.getParameter("r")));
		return "/map/menuconfigure/menuConfig.ftl";
	}

	@RequestMapping(value="/menuConfig/copy")
	public String copy(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = false) Long pgIdxCfgId){
		String result = menuConfig(session, request, map, pgIdxCfgId);
		PageIndexCfg pageIndexCfg = (PageIndexCfg) map.get("pageIndexCfg");
//		pageIndexCfg.setPgIdxCfgId(null);
		map.put("pageIndexCfg", pageIndexCfg);
		map.put("save", "copy");
		return result;
	}

	@RequestMapping(value="/test")
	public String test(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = false) Long gdcPid){
		if(gdcPid == null || gdcPid == 0) {//表示根节点
			gdcPid = 2L;
		}
		map.addAttribute("gdcPid", gdcPid);
		String forward = "/map/menuconfigure/test.ftl";
		return forward;
	}
	/**
	 * 2014-10-13 liushi add
	 * 链接到图层菜单管理页面
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/index")
	public String index(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = false) Long gdcPid){
		if(gdcPid == null || gdcPid == 0) {//表示根节点
			gdcPid = 2L;
		}
		map.addAttribute("gdcPid", gdcPid);
		String forward = "/map/menuconfigure/index.ftl";
		return forward;
	}
	/**
	 * 2014-10-13 liushi add
	 * 链接到图层菜单配置页面
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/configIndex")
	public String configIndex(HttpSession session, HttpServletRequest request, ModelMap map){
		Map<String,Object> defaultGridInfo = this.getDefaultOrgInfo(session);
		String orgCode = (String)defaultGridInfo.get(this.KEY_DEFAULT_INFO_ORG_CODE);
		map.addAttribute("orgCode", orgCode);
		String forward = "/map/menuconfigure/config_index.ftl";
		return forward;
	}
	@RequestMapping(value="/configIndexOfNewVersion")
	public String configIndexOfNewVersion(HttpSession session, HttpServletRequest request, ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String,Object> defaultGridInfo = this.getDefaultOrgInfo(session);
		String orgCode = (String)defaultGridInfo.get(this.KEY_DEFAULT_INFO_ORG_CODE);
		String orgName = (String)defaultGridInfo.get(this.KEY_DEFAULT_INFO_ORG_NAME);
		map.addAttribute("orgCode", orgCode);//组织code
		map.addAttribute("orgName", orgName);//组织名称
		String forward = "/map/menuconfigure/config_index_version_noe.ftl";
		return forward;
	}
	/**
	 * 2014-10-13 liushi add
	 * 链接到图层菜单树页面
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/left")
	public String left(HttpSession session, HttpServletRequest request, ModelMap map){
		String forward = "/map/menuconfigure/gis_data_cfg_left.ftl";
		return forward;
	}
	@RequestMapping(value="/leftGridTree")
	public String leftGridTree(HttpSession session, HttpServletRequest request, ModelMap map){
		
		String forward = "/map/menuconfigure/leftGridTree.ftl";
		return forward;
	}
	@RequestMapping(value="/mid")
	public String mid(HttpSession session, ModelMap map) {
		return "/map/menuconfigure/mid.ftl";
	}
	@RequestMapping(value="/toMenuConfig")
	public String toMenuConfig(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = true) String orgCode){
		
		List<GisOrgAcc> gisOrgAccList = this.menuConfigService.getGisOrgAccListByOrgCode(orgCode);
		String gdcIdStr = ",";
		for(GisOrgAcc gisOrgAcc:gisOrgAccList) {
			gdcIdStr += String.valueOf(gisOrgAcc.getGdcId()) + ",";
		}
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("gdcIdStr", gdcIdStr);
		String forward = "/map/menuconfigure/menuConfigTree.ftl";
		return forward;
	}
	@ResponseBody
	@RequestMapping(value="/saveGisOrgAcc")
	public String saveGisOrgAcc(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = true) String orgCode
			,@RequestParam(required = true) String gdcIdStr){
		Boolean flag = this.menuConfigService.saveGisOrgAcc(gdcIdStr, orgCode);
		return String.valueOf(flag);
	}
	/**
	 * 2014-10-13 liushi add
	 * 连接到图层菜单列表页面
	 * @param session
	 * @param request
	 * @param map
	 * @param gdcPid
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toList(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = false) Long gdcPid) {
		if (gdcPid == null || gdcPid == 0) {// 表示根节点
			gdcPid = 4L;
		}

		map.addAttribute("gdcPid", gdcPid);
		String forward = "/map/menuconfigure/gis_data_cfg_list.ftl";
		return forward;
	}
	/**
	 * 2014-10-13 liushi add
	 * 图层菜单列表数据查询（不需要分页信息，直接一页就显示所有的节点下的子节点）
	 * @param session
	 * @param request
	 * @param map
	 * @param gdcPid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listData")
	public List<GisDataCfg> listData(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = true) Long gdcPid, @RequestParam(required = false) String keywords) {
		List<GisDataCfg> gisDataCfgList = new ArrayList<GisDataCfg>();
		List<GisDataCfg> descGisDataCfgList = menuConfigService.getGisDataCfgListByPid(gdcPid);

		if (descGisDataCfgList.isEmpty()) {
			GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgById(gdcPid);
			if (gisDataCfg != null) {
				gisDataCfgList.add(gisDataCfg);
			}
		} else {
			gisDataCfgList = this.menuConfigService.getGisDataCfgListByPidAndKeywords(gdcPid, keywords);
		}

		return gisDataCfgList;
	}

	@ResponseBody
	@RequestMapping(value="/gisPageDataCfgTree")
	public List<ZTreeNode> gisPageDataCfgTree(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = false) Long gdcPid){
		if(gdcPid == null || gdcPid == 0) {//表示根节点
			gdcPid = 1L;
		}
		String pgIdxId = request.getParameter("pgIdxId");
		String orgCode = request.getParameter("orgCode");
		String pgIdxType = request.getParameter("pgIdxType");
		if(StringUtils.isBlank(pgIdxType)){
			pgIdxType = "ARCGIS_STANDARD_HOME";
		}
		Map<String, Object> params = new HashMap<String, Object>();
		PageContItemCfg pageContItemCfg = new PageContItemCfg();
		pageContItemCfg.setPgIdxType(pgIdxType);
		pageContItemCfg.setOrgCode(orgCode);
		if(StringUtils.isNotBlank(pgIdxId)){
			pageContItemCfg.setPgIdxId(Integer.valueOf(pgIdxId));
			params.put("pgIdxId", pgIdxId);
		}
		if(StringUtils.isNotBlank(orgCode)){
			params.put("orgCode", orgCode);
		}
		if(StringUtils.isNotBlank(pgIdxType)){
			params.put("pgIdxType", pgIdxType);
		}
		List<PageContItemCfg> pageContItemCfgs = pageContItemCfgService.findPageContItemCfgById(params);
//		List<GisDataCfg> gisDataCfgList = this.menuConfigService.getAllGisDataCfgListByPid(gdcPid);
		List<ZTreeNode> treeNodes = new ArrayList<ZTreeNode>();
		for(PageContItemCfg record : pageContItemCfgs) {
			ZTreeNode node = new ZTreeNode();
			if(gdcPid.equals(record.getContItemId()))
				node.setId(0L);
			else
			node.setId(Long.valueOf(record.getContItemId()));
//			node.setExpanded(false);
//			node.setHasChildren("1".equals(gisDataCfg.getIsLeaf())? false:true);
			node.setpId(Long.valueOf(record.getpContItemId()));
//			node.setText(gisDataCfg.getMenuName());
			node.setName(record.getName());
			node.setLayCfgId(record.getLayCfgId().longValue());
			treeNodes.add(node);
		}
		return treeNodes;
	}
	
	@ResponseBody
	@RequestMapping(value="/getGisDataCfgTree")
	public List<ZTreeNode> getGisDataCfgTree(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = false) Long gdcPid){
		if(gdcPid == null || gdcPid == 0) {//表示根节点
			gdcPid = 1L;
		}
		String orgCode = request.getParameter("orgCode");
		String homePageType = request.getParameter("homePageType");
		if(StringUtils.isBlank(homePageType)){
			homePageType = "ARCGIS_STANDARD_HOME";
		}
		
		String uiDomain = App.UI.getDomain(session);
		List<ZTreeNode> treeNodes = new ArrayList<ZTreeNode>();
		List<GisDataCfg> gisDataCfgs = menuConfigService.getGisDataCfgTree(orgCode, homePageType);
		
		this.transGisDataCfgUrl(gisDataCfgs, session);
		
		for (GisDataCfg gisDataCfg : gisDataCfgs) {
			ZTreeNode node = new ZTreeNode();
			String callBack = gisDataCfg.getCallBack();
			
			if (StringUtils.isNotBlank(callBack) && callBack.indexOf("classificationClick") == -1) {
				node.setCallBack(gisDataCfg.getCallBack());
			}
			
			node.setElementsCollectionStr(gisDataCfg.getElementsCollectionStr());
			node.setOrgCode(gisDataCfg.getOrgCode());
			node.setLayCfgId(gisDataCfg.getGdcId());
			node.setId(gisDataCfg.getGdcId());
			node.setpId(gisDataCfg.getGdcPid());
			node.setName(gisDataCfg.getMenuName());
			node.setIcon(uiDomain + gisDataCfg.getTreeIcon());
			treeNodes.add(node);
		}
		
		return treeNodes;
	}
	
	@ResponseBody
	@RequestMapping(value="/getDisplayStyle")
	public String getDisplayStyle(HttpSession session, HttpServletRequest request, ModelMap map) {
		String orgCode = request.getParameter("orgCode");
		String homePageType = request.getParameter("homePageType");
		if(StringUtils.isBlank(homePageType)){
			homePageType = "ARCGIS_STANDARD_HOME";
		}
		
		return menuConfigService.getDisplayStyle(orgCode, homePageType);
	}
	/**
	 * 2014-10-13 liushi add
	 * 图层菜单树子节点数据查询
	 * @param session
	 * @param request
	 * @param map
	 * @param gdcPid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/gisDataCfgTreeLeft")
	public List<ZTreeNode> gisDataCfgTreeLeft(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = false) Long gdcPid){
		if(gdcPid == null || gdcPid == 0) {//表示根节点
			gdcPid = 1L;
		}
		List<GisDataCfg> gisDataCfgList = this.menuConfigService.getAllGisDataCfgListByPid(gdcPid);
		List<ZTreeNode> treeNodes = new ArrayList<ZTreeNode>();
		for(GisDataCfg gisDataCfg : gisDataCfgList) {
			ZTreeNode node = new ZTreeNode();
			if(gdcPid.equals(gisDataCfg.getGdcId()))
				node.setId(0L);
			else
				node.setId(gisDataCfg.getGdcId());
//			node.setExpanded(false);
//			node.setHasChildren("1".equals(gisDataCfg.getIsLeaf())? false:true);
			node.setpId(gisDataCfg.getGdcPid());
//			node.setText(gisDataCfg.getMenuName());
			node.setLayCfgId(gisDataCfg.getGdcId());
			node.setName(gisDataCfg.getMenuName());
			treeNodes.add(node);
		}
		return treeNodes;
	}
	
	private ZTreeNode formateNode(GisDataCfg gisDataCfg){
		ZTreeNode node = new ZTreeNode();
		node.setId(gisDataCfg.getGdcId());
		node.setpId(gisDataCfg.getGdcPid());
		List<GisDataCfg> list = menuConfigService.getGisDataCfgListByPid(gisDataCfg.getGdcId());
		if(list.isEmpty()){
			node.setIsParent(false);
		} else {
			node.setIsParent(true);
		}
		node.setOpen(true);
		node.setName(gisDataCfg.getMenuName());
		return node;
	}
	
	@ResponseBody
	@RequestMapping("/menuZTreeForJsonp")
	public String menuZTreeForJsonp(HttpSession session,
			HttpServletRequest request,
			@RequestParam(required = false) Long gdcPid,
			@RequestParam(required = false) Long rootGdcPid) {
		List<ZTreeNode> nodes = new ArrayList<ZTreeNode>();
		
		String jsoncallback = request.getParameter("jsoncallback");
		
		if (gdcPid == null) {
			gdcPid = rootGdcPid;
			GisDataCfg gisDataCfg = menuConfigService.getRootGisDataCfgById(gdcPid);
			ZTreeNode rootNode = new ZTreeNode();
			rootNode.setId(gisDataCfg.getGdcId());
			rootNode.setpId(gisDataCfg.getGdcPid());
			rootNode.setOpen(true);
			rootNode.setIsParent(true);
			rootNode.setName(gisDataCfg.getMenuName());
			nodes.add(rootNode);
		} else {
			List<GisDataCfg> gisDataCfgList = this.menuConfigService.getGisDataCfgListByPid(gdcPid);
			
			for (GisDataCfg gisDataCfg2 : gisDataCfgList) {
				ZTreeNode node = new ZTreeNode();
				node = formateNode(gisDataCfg2);
				nodes.add(node);
			}
		}
		
		try {
			jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(nodes)
					+ ")";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsoncallback;
	}
	
	@ResponseBody
	@RequestMapping(value="/gisDataCfgTree")
	public List<TreeNode> gisDataCfgTree(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = false) Long gdcPid){
		if(gdcPid == null || gdcPid == 0) {//表示根节点
			gdcPid = 1L;
		}
		List<GisDataCfg> gisDataCfgList = this.menuConfigService.getGisDataCfgListByPid(gdcPid);
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		for(GisDataCfg gisDataCfg : gisDataCfgList) {
			TreeNode node = new TreeNode();
			node.setExpanded(false);
			node.setHasChildren("1".equals(gisDataCfg.getIsLeaf())? false:true);
			node.setId(String.valueOf(gisDataCfg.getGdcId()));
			node.setPid(String.valueOf(gisDataCfg.getGdcPid()));
			node.setText(gisDataCfg.getMenuName());
			treeNodes.add(node);
		}
		
		return treeNodes;
	}
	@ResponseBody
	@RequestMapping(value="/gisDataCfgTreeForConfig")
	public List<GdZTreeNode> gisDataCfgTreeForConfig(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = false) Long gdcPid
			,@RequestParam(required = true) String orgCode){
		if(gdcPid == null || gdcPid == 0) {//表示根节点
			gdcPid = 1L;
		}
		List<GisOrgAcc> gisOrgAccList = this.menuConfigService.getGisOrgAccListByOrgCode(orgCode);
		String gdcIdStr = ",";
		for(GisOrgAcc gisOrgAcc:gisOrgAccList) {
			gdcIdStr += String.valueOf(gisOrgAcc.getGdcId()) + ",";
		}
		List<GdZTreeNode> nodes = new ArrayList<GdZTreeNode>();
		nodes = this.getGisDataCfgTreeNodes(gdcPid, gdcIdStr);
		return nodes;
	}
	
	@ResponseBody
	@RequestMapping(value="/gisDataCfgTreeForConfigVersionNoe")
	public List<GdZTreeNode> gisDataCfgTreeForConfigVersionNoe(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = false) Long gdcPid
			,@RequestParam(required = true) String orgCode
			,@RequestParam(required = true) String homePageType){
		if(gdcPid == null || gdcPid == 0) {//表示根节点
			gdcPid = 1L;
		}
		List<GisOrgAcc> gisOrgAccList = this.menuConfigService.getGisOrgAccListByOrgCodeVersionNoe(orgCode,homePageType);
		OrgSocialInfoBO orgSocialInfoBO = null;
		int levelNum = 0;
		while (gisOrgAccList.isEmpty()) {
			orgSocialInfoBO = orgSocialInfoOutService.selectOrgSocialInfoByOrgCode(orgCode);
			levelNum++;
			if (orgSocialInfoBO.getParentOrgId() == null || levelNum>7) {
				break;
			}
			
			orgSocialInfoBO = orgSocialInfoOutService.findByOrgId(orgSocialInfoBO.getParentOrgId());
			orgCode = orgSocialInfoBO.getOrgCode();
			gisOrgAccList = this.menuConfigService.getGisOrgAccListByOrgCodeVersionNoe(orgCode ,homePageType);
		}
		
		String gdcIdStr = ",";
		for(GisOrgAcc gisOrgAcc:gisOrgAccList) {
			gdcIdStr += String.valueOf(gisOrgAcc.getGdcId()) + ",";
		}
		List<GdZTreeNode> nodes = new ArrayList<GdZTreeNode>();
		nodes = this.getGisDataCfgTreeNodes(gdcPid, gdcIdStr);
		return nodes;
	}
	
	public List<GdZTreeNode> getGisDataCfgTreeNodes(Long gdcPid,String gdcIds){
		List<GisDataCfg> gisDataCfgList = this.menuConfigService.getGisDataCfgListByPid(gdcPid);
		List<GdZTreeNode> nodes = new ArrayList<GdZTreeNode>();
		for(GisDataCfg gisDataCfg : gisDataCfgList) {
			GdZTreeNode node = new GdZTreeNode();
			node.setName(gisDataCfg.getMenuName());
			node.setId(String.valueOf(gisDataCfg.getGdcId()));
			node.setPId(String.valueOf(gdcPid));
			if(gdcPid == 1) {
				node.setOpen(true);
			}
			if("0".equals(gisDataCfg.getIsLeaf())){
				node.setIsParent(true);
			}
			if(gdcIds.indexOf(","+node.getId()+",")>=0){
				node.setChecked(true);
			}
			List<GdZTreeNode> childrenNodes = new ArrayList<GdZTreeNode>();
			int childrenCount = this.menuConfigService.getGisDataCfgCountByPid(gisDataCfg.getGdcId());
			if(childrenCount>0){
				childrenNodes = this.getGisDataCfgTreeNodes(gisDataCfg.getGdcId(), gdcIds);
				node.setChildren(childrenNodes);
			}
			nodes.add(node);
		}
		return nodes;
	}
	@ResponseBody
	@RequestMapping(value="/saveGisDataCfgTreeForConfig")
	public Map<String,Object> saveGisDataCfgTreeForConfig(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = true) String orgCode
			,@RequestParam(required = true) String gdcIdsStr){
		Boolean flag = this.menuConfigService.saveGisOrgAcc(gdcIdsStr,orgCode);
		Map<String,Object> result = new HashMap<String,Object>();
		
		result.put("result", (flag == true)?true:false);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/saveGisDataCfgTreeForConfigVersionNoe")
	public Map<String,Object> saveGisDataCfgTreeForConfigVersionNoe(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = true) String orgCode
			,@RequestParam(required = true) String homePageType
			,@RequestParam(required = true) String gdcIdsStr){
		Boolean flag = this.menuConfigService.saveGisOrgAccVersionNoe(gdcIdsStr,orgCode,homePageType);
		Map<String,Object> result = new HashMap<String,Object>();
		
		result.put("result", (flag == true)?true:false);
		return result;
	}
	
	/**
	 * 2014-10-13 liushi add
	 * 链接到图层菜单新增页面
	 * @param session
	 * @param request
	 * @param map
	 * @param gdcPid
	 * @return
	 */
	@RequestMapping(value="/toAddGisDataCfg")
	public String toAddGisDataCfg(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = true) Long gdcPid) {
		GisDataCfg gisDataCfg = menuConfigService.getGisDataCfgById(gdcPid);
		GisDataCfg rootGisDataCfg = menuConfigService.getRootGisDataCfgById(gdcPid);
		map.addAttribute("gisDataCfg", gisDataCfg);
		map.addAttribute("rootGisDataCfg", rootGisDataCfg);
		String forward = "/map/menuconfigure/gis_data_cfg_add.ftl";
		return forward;
	}
	/**
	 * 2014-10-13 liushi add
	 * 新增保存操作
	 * @param session
	 * @param request
	 * @param map
	 * @param gisDataCfg
	 * @return
	 */
	@RequestMapping(value="/add")
	public String add(HttpSession session, HttpServletRequest request, ModelMap map, GisDataCfg gisDataCfg){
		GisDataCfg parentGisDataCfg = menuConfigService.getGisDataCfgById(gisDataCfg.getGdcPid());
		
		// 人，地，事，物，情，组织，精准扶贫
		/*
		if (parentGisDataCfg != null && parentGisDataCfg.getGdcPid() == 1) {
			gisDataCfg.setIsLeaf("0"); // 有子节点
		} else {
			gisDataCfg.setIsLeaf("1");
		}
		*/
		
		if (gisDataCfg.getStatus() == null) {
			gisDataCfg.setStatus("003"); // 禁用
		}
		
		Integer result = this.menuConfigService.insertGisDataCfg(gisDataCfg);
		
		map.addAttribute("result", (result != null && result>0)?"添加成功！":"添加失败！");
		map.addAttribute("reload", (result != null && result>0)? "true":"false");
		String forward = "/zzgl/result.ftl";
		return forward;
	}
	/**
	 * 2014-10-13 liushi add
	 * 连接到图层菜单信息编辑页面
	 * @param session
	 * @param request
	 * @param map
	 * @param gdcId
	 * @return
	 */
	@RequestMapping(value="/toEditGisDataCfg")
	public String toEditGisDataCfg(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = true) Long gdcId){
		GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgById(gdcId);
		GisDataCfg rootGisDataCfg = menuConfigService.getRootGisDataCfgById(gdcId);
		map.addAttribute("gisDataCfg", gisDataCfg);
		map.addAttribute("rootGisDataCfg", rootGisDataCfg);
		String forward = "/map/menuconfigure/gis_data_cfg_edit.ftl";
		return forward;
	}
	/**
	 * 2014-10-13 liushi add
	 * 图层菜单编辑保存功能
	 * @param session
	 * @param request
	 * @param map
	 * @param gisDataCfg
	 * @return
	 */
	@RequestMapping(value="/update")
	public String update(HttpSession session, HttpServletRequest request, ModelMap map, GisDataCfg gisDataCfg){
		if (gisDataCfg.getStatus() == null) {
			gisDataCfg.setStatus("003"); // 禁用
		}
		
		Integer result = this.menuConfigService.updateGisDataCfg(gisDataCfg);
		map.addAttribute("result", (result != null && result>0)?"修改成功！":"修改失败！");
		map.addAttribute("reload", (result != null && result>0)? "true":"false");
		String forward = "/zzgl/result.ftl";
		return forward;
	}
	/**
	 * 2014-10-13 liushi add
	 * 删除时需要验证是否有子节点
	 * @param session
	 * @param request
	 * @param map
	 * @param gdcPid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/isHaveChildren")
	public Map<String, Object> isHaveChildren(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = true) Long gdcPid){
		Integer count = this.menuConfigService.getGisDataCfgCountByPid(gdcPid);
		Map<String, Object> resultmap = new HashMap<String,Object>();
		if(count>0){
			resultmap.put("result", true);
		}else{
			resultmap.put("result", false);
		}
		return resultmap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/checkMenuName")
	public Map<String, Object> checkMenuName(HttpSession session,
			@RequestParam(value = "gdcPid", required = false) Long gdcPid,
			@RequestParam(value = "menuName", required = false) String menuName) {
		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gdcPid", gdcPid);
		params.put("menuName", menuName.trim());

		int count = menuConfigService.checkMenuName(params);

		if (count > 0) {
			result.put("check", false);
		} else {
			result.put("check", true);
		}

		return result;
	}
	
	/**
	 * 2014-10-13 liushi add
	 * 删除操作
	 * @param session
	 * @param request
	 * @param map
	 * @param gdcId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete")
	public Map<String, Object> delete(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = true) Long gdcId){
		Integer count = this.menuConfigService.deleteGisDataCfg(gdcId);
		Map<String, Object> resultmap = new HashMap<String,Object>();
		if(count>0){
			resultmap.put("result", true);
		}else{
			resultmap.put("result", false);
		}
		return resultmap;
	}
	/**
	 * 2014-10-20 liushi add
	 * 获取该登陆网格的图层配置树
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getGisDataCfgRelationTree")
	public Map<String, Object> getGisDataCfgRelationTree(HttpSession session, HttpServletRequest request, ModelMap map){
		Long gdcId = 2L;
		Map<String,Object> defaultGridInfo = this.getDefaultOrgInfo(session);
		String orgCode = (String)defaultGridInfo.get(this.KEY_DEFAULT_INFO_ORG_CODE);
		GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgRelationTree(orgCode, gdcId);
		Map<String, Object> resultmap = new HashMap<String,Object>();
		resultmap.put("gisDataCfg", gisDataCfg);
		return resultmap;
	}
	/**
	 * orgCode 是组织的code
	 * @param session
	 * @param request
	 * @param map
	 * @param orgCode
	 * @param homePageType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getGisDataCfgRelationTreeVersionNoe")
	public Map<String, Object> getGisDataCfgRelationTreeVersionNoe(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = true) String orgCode
			,@RequestParam(required = true) String homePageType){
		Long gdcId = 4L;
		GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgRelationTreeVersionNoe(orgCode,homePageType, gdcId);
		
		if(gisDataCfg !=null &&gisDataCfg.getChildrenList()!=null&& gisDataCfg.getChildrenList().size()>0) {
			this.transGisDataCfgUrl(gisDataCfg.getChildrenList(), session);
		}
		Map<String, Object> resultmap = new HashMap<String,Object>();
		resultmap.put("gisDataCfg", gisDataCfg);
		return resultmap;
	}
	
	@ResponseBody
	@RequestMapping(value="/getGisDataCfgs")
	public Map<String, Object> getGisDataCfgs(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = false) String orgCode
			,@RequestParam(required = false) String homePageType
			,@RequestParam(required = false) Long gdcPid){
		if (StringUtils.isNotBlank(homePageType) && homePageType.equals("GIS_STANDARD_HOME")) {
			gdcPid = 5L;
		} else {
			gdcPid = 4L;
		}
		
		List<GisDataCfg> resultGisDataCfgs = this.menuConfigService.getGisDataCfgListByPid(gdcPid);
		List<GisDataCfg> descGisDataCfgs = null;
		List<GisDataCfg> gisDataCfgs = this.menuConfigService.getAllGisDataCfgsByPid(gdcPid);
		
		for (GisDataCfg gisDataCfg : resultGisDataCfgs) {
			descGisDataCfgs = new ArrayList<GisDataCfg>();
			
			for (GisDataCfg gisDataCfg2 : gisDataCfgs) {
				if (gisDataCfg.getGdcId().longValue() == gisDataCfg2.getGdcPid().longValue()) {
					descGisDataCfgs.add(gisDataCfg2);
				}
			}
			
			gisDataCfg.setChildrenList(descGisDataCfgs);
		}

		/*Long gdcId = 0L;
		
		if (gisDataCfgs != null && gisDataCfgs.size() > 0) {
			for (GisDataCfg gisDataCfg : gisDataCfgs) {
				gdcId = gisDataCfg.getGdcId();
				
				descGisDataCfgs = menuConfigService.getGisDataCfgListByPid(gdcId);
				if (descGisDataCfgs != null && descGisDataCfgs.size() > 0) {
					gisDataCfg.setChildrenList(descGisDataCfgs);
				}
			}
		}*/
		
		Map<String, Object> resultmap = new HashMap<String, Object>();
		resultmap.put("gisDataCfgs", resultGisDataCfgs);
		return resultmap;
	}
	
	
	/**
	 * 第二次改造图层项查询
	 * @param session
	 * @param request
	 * @param map
	 * @param orgCode 是组织的code
	 * @param homePageType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getGisDataCfgRelationTreeVersionTwo")
	public Map<String, Object> getGisDataCfgRelationTreeVersionTwo(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value = "orgCode",required = true) String orgCode
			,@RequestParam(value = "homePageType",required = true) String homePageType
			,@RequestParam(value = "gdcId",required = true) Long gdcId
			,@RequestParam(value = "isRootSearch",required = true) Integer isRootSearch){
		GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgRelationTreeVersionTwo(orgCode,homePageType, gdcId,isRootSearch);
		
		if(gisDataCfg !=null &&gisDataCfg.getChildrenList()!=null&& gisDataCfg.getChildrenList().size()>0) {
			this.transGisDataCfgUrl(gisDataCfg.getChildrenList(), session);
		}
		Map<String, Object> resultmap = new HashMap<String,Object>();
		resultmap.put("gisDataCfg", gisDataCfg);
		return resultmap;
	}
	/**
	 * 2015-09-06 liushi add 根据根节点id获取整个树的节点
	 * @param session
	 * @param request
	 * @param map
	 * @param orgCode
	 * @param homePageType
	 * @param gdcId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getGisDataCfgRelationTreeByRoot")
	public Map<String, Object> getGisDataCfgRelationTreeVersionNoe(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(required = true) String socialOrgCode
			,@RequestParam(required = true) String homePageType
			,@RequestParam(required = true) Long gdcId){
		GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgRelationTreeVersionNoe(socialOrgCode,homePageType, gdcId);
		
		if(gisDataCfg !=null &&gisDataCfg.getChildrenList()!=null&& gisDataCfg.getChildrenList().size()>0) {
			this.transGisDataCfgUrl(gisDataCfg.getChildrenList(), session);
		}
		
		Map<String, Object> resultmap = new HashMap<String,Object>();
		resultmap.put("gisDataCfg", gisDataCfg);
		return resultmap;
	}
	
	/**
	 * 2014-10-20 liushi add
	 * 获取该登陆网格的图层配置树
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getGisDataCfgRelationTreeShow")
	public List<GdZTreeNode> getGisDataCfgRelationTreeShow(HttpSession session, HttpServletRequest request, ModelMap map){
		Long gdcId = 3L;
		Map<String,Object> defaultGridInfo = this.getDefaultOrgInfo(session);
		String orgCode = (String)defaultGridInfo.get(this.KEY_DEFAULT_INFO_ORG_CODE);
		GisDataCfg gisDataCfgRoot = this.menuConfigService.getGisDataCfgRelationTree(orgCode, gdcId);
		String uiDomain=App.UI.getDomain(session);
		List<GdZTreeNode> nodes = this.toTreeNodes(gisDataCfgRoot.getChildrenList(),uiDomain);
		return nodes;
	}
	public List<GdZTreeNode> toTreeNodes(List<GisDataCfg> gisDataCfgList,String uiDomain){
		List<GdZTreeNode> nodes = new ArrayList<GdZTreeNode>();
		
		if (gisDataCfgList != null) {
			for(GisDataCfg gisDataCfg : gisDataCfgList) {
				GdZTreeNode node = new GdZTreeNode();
				node.setName(gisDataCfg.getMenuName());
				node.setId(String.valueOf(gisDataCfg.getGdcId()));
				node.setPId(String.valueOf(gisDataCfg.getGdcPid()));
				if(gisDataCfg.getLargeIco() != null && !"".equals(gisDataCfg.getLargeIco())) {
					node.setIcon(uiDomain+gisDataCfg.getLargeIco());
					node.setIconOpen(uiDomain+gisDataCfg.getLargeIco());
					node.setIconClose(uiDomain+gisDataCfg.getLargeIco());
				}
				node.setGridPhoto(gisDataCfg.getCallBack());
				if("0".equals(gisDataCfg.getIsLeaf())){
					node.setIsParent(true);
				}
				if(gisDataCfg.getGdcPid() == 3) {
					node.setOpen(true);
				}
				List<GdZTreeNode> childrenNodes = new ArrayList<GdZTreeNode>();
				if(gisDataCfg.getChildrenList()!=null && gisDataCfg.getChildrenList().size()>0){
					childrenNodes = this.toTreeNodes(gisDataCfg.getChildrenList(),uiDomain);
					node.setChildren(childrenNodes);
				}
				nodes.add(node);
			}
		}
		return nodes;
	}
	
	@ResponseBody
	@RequestMapping(value = "/sort")
	public String sort(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "code", required = true) String code,
			@RequestParam(value = "ids", required = true) String ids)
			throws Exception {
		String jsoncallback = request.getParameter("jsoncallback");
		Map<String, Object> rs = new HashMap<String, Object>();
		boolean flag = false;
		String msg = "";
		if (StringUtils.isNotBlank(ids)) {
			try {
				flag = this.menuConfigService.saveSort(Arrays.asList(ids.replaceAll(" ", "").split(",")));
				msg = flag ? "保存成功！" : "保存失败！";
			} catch (Exception e) {
				msg = "请配置编码[" + code + "]对应的排序信息！";
			}
		} else {
			msg = "缺少排序对象，请检查！";
		}
		rs.put("flag", flag);
		rs.put("msg", msg);
		jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(rs) + ")";
		return jsoncallback;
	}

	/**
	 * 根据图层编码menuCode获取图层信息
	 * @param session
	 * @param request
	 * @param map
	 * @param menuCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getGisDataCfgByCode")
	public GisDataCfg getGisDataCfgByCode(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value = "menuCode", required = true) String menuCode){
		GisDataCfg gisDataCfg = this.menuConfigService.getGisDataCfgByCode(menuCode, null);
		transGisDataCfgUrl(gisDataCfg, session);
		return gisDataCfg;
	}
	
	/**
	 * 天梯图初始化列表LadderDiagram
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/indexLa")
	public String indexLa(HttpSession session,ModelMap map) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("gridName", defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("gridCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE).toString());
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		
		return "/map/menuconfigure/ladder_diagram_index.ftl";
	}
	
	
	/**
	 * 天梯图初始化列表LadderDiagram
	 * @param session
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/loadLaData", method = RequestMethod.POST)
	public EUDGPagination loadLaData(HttpServletRequest request,HttpSession session,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode){
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isBlank(infoOrgCode)) {
			infoOrgCode = this.getDefaultInfoOrgCode(session);
		}
		params.put("infoOrgCode", infoOrgCode);
		
		EUDGPagination record = menuConfigService.findLadderDiagrmData(params);
		return record;
	}
	
	/**
	 * 保存天梯图初始化数据LadderDiagram
	 * @param session
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveLd", method = RequestMethod.POST)
	public Map<String,Object>  saveLd(HttpServletRequest request,HttpSession session){
		Map<String, Object> resultmap = new HashMap<String,Object>();
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("infoOrgCode",request.getParameter("infoOrgCode"));
		params.put("mapType",request.getParameter("mapType"));
		params.put("x",request.getParameter("x"));
		params.put("y",request.getParameter("y"));
		params.put("zoom",request.getParameter("zoom"));
		params.put("order",request.getParameter("order"));
		
		Boolean result = menuConfigService.saveLadderDiagrm(params);
		if(result){
			resultmap.put("result", true);
		}else{
			resultmap.put("result", false);
		}
		return resultmap;
	}
	
	/**
	 * 删除天梯图初始化数据LadderDiagram
	 * @param session
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteLd", method = RequestMethod.POST)
	public Map<String, Object> deleteLd(HttpServletRequest request,HttpSession session,
			@RequestParam(value = "id") Long id){
		Map<String, Object> resultmap = new HashMap<String,Object>();
		
		int count = menuConfigService.deleteLadderDiagrm(id);
		if(count>0){
			resultmap.put("result", true);
		}else{
			resultmap.put("result", false);
		}
		return resultmap;
	}
}
