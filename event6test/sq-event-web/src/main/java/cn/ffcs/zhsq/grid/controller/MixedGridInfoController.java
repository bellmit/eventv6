/**
 * 
 */
package cn.ffcs.zhsq.grid.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.uam.service.OrgEntityInfoOutService;
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
import org.springframework.web.multipart.MultipartFile;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.common.GdZTreeNode;
import cn.ffcs.common.TreeNode;
import cn.ffcs.cookie.bo.UserCookie;
import cn.ffcs.cookie.service.UserCookieService;
import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.grid.service.IGridResultModelService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.SQBOTransTool;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 网格数据库
 * @author guohh
 *
 */
@Controller
@RequestMapping(value="/zhsq/grid/mixedGrid")
public class MixedGridInfoController extends ZZBaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IGridResultModelService gridResultModelService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private UserCookieService userCookieService;
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoService;

	
	//-- 文件上传目录
	private static final String FILE_UPLOAD_FLODER = "mixedGrid";
	
	/**
	 * 网格数据库汇总表
	 */
	@RequestMapping(value="/index")
	public String index(HttpSession session, ModelMap map) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("startInfoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("startGridCodeName", defaultGridInfo.get(KEY_START_GRID_CODE_NAME));
		map.addAttribute("startGridPhoto", defaultGridInfo.get(KEY_START_GRID_PHOTO));
        map.addAttribute("RESOURSE_SERVER_PATH",  App.IMG.getDomain(session));
		return "/zzgl_grid/mixedGrid/index.ftl";
	}
	
	/**
	 * 网格数据库汇总表数据
	 * @param session
	 * @param page 
	 * @param rows
	 * @param startGridId
	 * @param gridName
	 * @param gridCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination listData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="startGridId") Long startGridId, @RequestParam(value="gridName", required=false) String gridName,
			@RequestParam(value="gridCode", required=false) String gridCode) {
		if(page<=0) page=1;
		if(gridName!=null) gridName = gridName.trim();
		if(gridCode!=null) gridCode = gridCode.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startGridId", startGridId);
		params.put("gridName", gridName);
		params.put("gridCode", gridCode);
		cn.ffcs.common.EUDGPagination pagination = mixedGridInfoService.findMixedGridInfoPagination(page, rows, params);
		return pagination;
	}
	@RequestMapping(value="/statOfGrid")
	public String statOfGrid(HttpSession session, ModelMap map,@RequestParam(value="gridId") Long gridId) {
		if(gridId == null || gridId == 0) {
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			gridId = (Long)defaultGridInfo.get(KEY_START_GRID_ID);
		}
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		List<Map<String,Object>> statofgridlist = mixedGridInfoService.statOfGrid(gridId);
		Map<String,Object> statofgrid = statofgridlist.get(0);
		List<Map<String,Object>> bos = new ArrayList<Map<String,Object>>();
		int[] count=this.STAT_OF_GRID_SQ;
		int sum = 0;
		Map<String, String> country = new HashMap<String, String>();
		if(!statofgridlist.isEmpty()&&"3501".equals((String)statofgrid.get("INFO_ORG_CODE"))){
			String[] gridCode = this.GRID_CODE;
				for (int x = 0; x < gridCode.length; x++) {
					sum += count[x];
				}
				
				if(statofgrid.get("INFO_ORG_CODE").equals("3501")) {
					statofgrid.put("COUNT5_1", sum);
				}
			for (int i = 0; i < gridCode.length; i++) {
				for (int j = 0; j < statofgridlist.size(); j++) {
					if(statofgridlist.get(j).get("INFO_ORG_CODE").toString().equals(gridCode[i])){
						statofgridlist.get(j).put("COUNT5_1", count[i]);
						bos.add(statofgridlist.get(j));
					}
				}
			}
			statofgridlist = bos;
		}else {
			for (int j = 1; j < statofgridlist.size(); j++) {
				bos.add(statofgridlist.get(j));
			}
			statofgridlist = bos;
		}
		map.addAttribute("gridInfo", gridInfo);
		map.addAttribute("statofgrid", statofgrid);
		map.addAttribute("statofgridlist", statofgridlist);
		return "/zzgl_grid/mixedGrid/statOfGrid.ftl";
	}
	/**
	 * 增加网格信息
	 * @param session
	 * @param parentGridId 父级网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/create")
	public String create(HttpSession session, @RequestParam(value="parentGridId") Long parentGridId, ModelMap map) {
		map.addAttribute("parentGridId", parentGridId);
		MixedGridInfo gridInfo = new MixedGridInfo();
		map.addAttribute("gridInfo", gridInfo);
		//-- 网格模版列表
		List<Map<String, Object>> modelList = gridResultModelService.queryForModelNameAndId();
		map.addAttribute("modelList", modelList);
		//-- 父级网格信息
		MixedGridInfo parentGridInfo = mixedGridInfoService.findMixedGridInfoById(parentGridId, false);
		map.addAttribute("parentGridInfo", parentGridInfo);
		return "/zzgl_grid/mixedGrid/create.ftl";
	}
	
	/**
	 * 保存新建的网格信息
	 * @param session
	 * @param gridInfo 网格信息
	 * @param gridPhotoFile 网格图片
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String save(HttpSession session, @ModelAttribute(value="gridInfo") MixedGridInfo gridInfo,
			@RequestParam(value="shequGridFlag", required=false) String shequGridFlag,
			@RequestParam(value="gridPhotoFile", required=false) MultipartFile gridPhotoFile, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Date now = Calendar.getInstance().getTime();
		gridInfo.setUpdateDate(now);
		gridInfo.setCreateDate(now);
		gridInfo.setUpdateUser(userInfo.getUserId());
		gridInfo.setCreateUser(userInfo.getUserId());
		gridInfo.setStatus(ConstantValue.STATUS_DEFAULT);
		if(gridInfo.getGridCode()!=null)
			gridInfo.setGridCode(gridInfo.getGridCode().toUpperCase());
		try {
			if(gridPhotoFile!=null && !gridPhotoFile.isEmpty()) {
			    String fileName = gridPhotoFile.getOriginalFilename();
				byte[] multipartFileBytes = gridPhotoFile.getBytes();
				String filePath = fileUploadService.uploadSingleFile(fileName, multipartFileBytes, ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_FLODER);
				gridInfo.setGridPhoto(filePath);
			}
		} catch (IOException e) {
			logger.info("创建网格信息，上传网格图像失败！");
			e.printStackTrace();
		}
		//-- 设置网格生成状态
		if(shequGridFlag!=null) gridInfo.setGridStatus("002");
		else gridInfo.setGridStatus("000");
		gridInfo.setGridModel(IMixedGridInfoService.GRID_MODEL_XINGZHENG);
		Long gridId = mixedGridInfoService.saveMixedGridInfoReturnId(gridInfo);
		map.addAttribute("result", gridId>0?"添加成功！":"添加失败！");
		return "/zzgl_grid/result.ftl";
	}
	
	/**
	 * 编辑网格信息
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(HttpSession session, @RequestParam(value="gridId") Long gridId, ModelMap map) {
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		map.addAttribute("gridInfo", gridInfo);
		//-- 网格模版列表
		List<Map<String, Object>> modelList = gridResultModelService.queryForModelNameAndId();
		map.addAttribute("modelList", modelList);
			if(gridInfo.getParentGridId()>0) {
			//-- 父级网格信息
			MixedGridInfo parentGridInfo = mixedGridInfoService.findMixedGridInfoById(gridInfo.getParentGridId(), false);
			map.addAttribute("parentGridInfo", parentGridInfo);
		}
        map.addAttribute("RESOURSE_SERVER_PATH",  App.IMG.getDomain(session));
		return "/zzgl_grid/mixedGrid/edit.ftl";
	}
	
	/**
	 * 更新网格信息
	 * @param session
	 * @param gridInfo 网格信息
	 * @param gridPhotoFile 网格图片
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(HttpSession session, @ModelAttribute(value="gridInfo") MixedGridInfo gridInfo,
			@RequestParam(value="shequGridFlag", required=false) String shequGridFlag,
			@RequestParam(value="gridPhotoFile", required=false) MultipartFile gridPhotoFile, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Date now = Calendar.getInstance().getTime();
		gridInfo.setUpdateDate(now);
		gridInfo.setUpdateUser(userInfo.getUserId());
		if(gridInfo.getGridCode()!=null)
			gridInfo.setGridCode(gridInfo.getGridCode().toUpperCase());
		try {
			if(gridPhotoFile!=null && !gridPhotoFile.isEmpty()) {
			    String fileName = gridPhotoFile.getOriginalFilename();
				byte[] multipartFileBytes = gridPhotoFile.getBytes();
				String filePath = fileUploadService.uploadSingleFile(fileName, multipartFileBytes, ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_FLODER);
				gridInfo.setGridPhoto(filePath);
			}
		} catch (IOException e) {
			logger.info("创建网格信息，上传网格图像失败！");
			e.printStackTrace();
		}
		//-- 设置网格生成状态
		if(shequGridFlag!=null) gridInfo.setGridStatus("002");
		else gridInfo.setGridStatus("000");
		gridInfo.setGridModel(IMixedGridInfoService.GRID_MODEL_XINGZHENG);
		boolean result = mixedGridInfoService.updateMixedGridInfo(gridInfo);
		map.addAttribute("result", result?"更新成功！":"更新失败！");
		return "/zzgl_grid/result.ftl";
	}
	
	/**
	 * 批量删除网格信息
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/batchDelete", method=RequestMethod.POST)
	public Map<String, Object> batchDeleteCompany(HttpSession session, HttpServletRequest request, @RequestParam(value="idStr") String idStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String[] ids = idStr.split(",");
		List<Long> idList = new ArrayList<Long>();
		for(int i=0; i<ids.length; i++) {
			try {
				long recordId = Long.parseLong(ids[i]);
				idList.add(recordId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		boolean result = mixedGridInfoService.deleteMixedGridInfoById(userInfo.getUserId(), idList);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result?idList.size():0L);
		return resultMap;
	}
	
	//-----------网格树 start------------------------------------------
	/**
	 * 网格树
	 * @param session
	 * @param gridId 网格ID，空时取信息域对应的网格根节点
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/gridZTree")
	public List<GdZTreeNode> gridZTree(HttpSession session, @RequestParam(value="gridId", required=false) Long gridId,
			@RequestParam(value="withCode", required=false) Integer withCode, @RequestParam(value="startGridId", required=false) Long startGridId) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<GdZTreeNode> nodes = new ArrayList<GdZTreeNode>();
		if(gridId==null) { //-- 根
			GdZTreeNode rootNode = new GdZTreeNode();
			rootNode.setName("网格信息");
			rootNode.setId("-9999");
			rootNode.setPId("-1");
			rootNode.setOpen(true);
			rootNode.setIsParent(true);
			rootNode.setNocheck(true);
			if(startGridId==null || startGridId<0L) { //取默认管理的网格
				//-- 获取管理的信息域组织
				List<Long> infoOrgIdList = new ArrayList<Long>();
				if(userInfo.getInfoOrgList()!=null && userInfo.getInfoOrgList().size()>0) {
					for(OrgEntityInfoBO org : userInfo.getInfoOrgList()) {
						infoOrgIdList.add(org.getOrgId());
					}
				}
				//-- 获取对应的网格根节点
				List<MixedGridInfo> list = mixedGridInfoService.getMixedGridMappingListByOrgIdList(infoOrgIdList, ConstantValue.ORG_TYPE_ALL);
				//-- 添加社区级用户登录 过滤基础网格 add by zhongshm 2013.09.05----------------------无效代码，modify by guohh 2013.11.5
				/*
				List<OrgEntityInfo> orgEntytyInfoList = iOrgConfigService.getOrgEntityInfoList(userInfo.getCatalogInfoId(), userInfo.getOrgId());
				if(orgEntytyInfoList!=null&&orgEntytyInfoList.size()>0){
					OrgEntityInfo orgEntityInfo = orgEntytyInfoList.get(0);
					if(orgEntityInfo.getChiefLevel()!=null&&orgEntityInfo.getChiefLevel().equals("5")){
						List<MixedGridInfo> list = new ArrayList<MixedGridInfo>();
						for(MixedGridInfo gridInfo : gridInfoList){
							if(gridInfo.getGridLevel()<6){
								list.add(gridInfo);
							}
						}
						gridInfoList = list;
					}
				}
				*/
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
				if(gridInfoList!=null && gridInfoList.size()>0) {
					for(MixedGridInfo gridInfo : gridInfoList) {
						rootNode.addChildren(SQBOTransTool.transMixedGridInfoToGdZTreeNode(gridInfo, ((withCode!=null && withCode==1)?true:false)));
					}
				}
			} else { //取指定的网格
				MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(startGridId, false);
				rootNode.addChildren(SQBOTransTool.transMixedGridInfoToGdZTreeNode(gridInfo, ((withCode!=null && withCode==1)?true:false)));
			}
			nodes.add(rootNode);
		} else if(gridId>0) {
			List<MixedGridInfo> gridInfoList = mixedGridInfoService.getMixedGridListByParentId(gridId, false, false, ConstantValue.ORG_TYPE_ALL);
			if (!gridInfoList.isEmpty()) {
				MixedGridInfo mixedGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
				List<MixedGridInfo> newGridInfoList = new ArrayList<MixedGridInfo>();
				if (mixedGridInfo != null && "3501".equals(mixedGridInfo.getInfoOrgCode())) {// 福州市级
					for (int i = 0; i < GRID_CODE.length; i++) {
						for (int j = 0; j < gridInfoList.size(); j++) {
							if (GRID_CODE[i].equals(gridInfoList.get(j).getInfoOrgCode())) {
								newGridInfoList.add(gridInfoList.get(j));
							}
						}
					}
					gridInfoList = newGridInfoList;
				}
			}
			if(gridInfoList!=null) {
				for(MixedGridInfo gridInfo : gridInfoList) {
					nodes.add(SQBOTransTool.transMixedGridInfoToGdZTreeNode(gridInfo, ((withCode!=null && withCode==1)?true:false)));
				}
			}
		}
		return nodes;
	}
	
	//-----------网格树 end  ------------------------------------------
	
	/**
	 * 根据gridCode查找数据
	 * @param session
	 * @param request
	 * @param gridCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/findByGridCode", method=RequestMethod.POST)
	public Map<String, Object> findByGridCode(HttpSession session, HttpServletRequest request, @RequestParam(value="gridCode") String gridCode,@RequestParam(value="gridId",required=false) Long gridId ) {
		if(gridCode!=null){gridCode=gridCode.trim();}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridCode", gridCode);
		params.put("gridId", gridId);
		List<MixedGridInfo> list=mixedGridInfoService.findByGridCode(params);
		Map<String, Object> resultMap=new HashMap<String, Object>();
		if(list!=null&&list.size()>0){
			resultMap.put("result", true);
		}else{
			resultMap.put("result", false);
		}
		return resultMap;
		
	}
	
	/**
	 * 信息域组织树
	 * @param session
	 * @param orgId 组织ID，空时为登录的用户管理的信息域根节点
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/orgTree")
	public List<TreeNode> newInfoOrgTree(HttpSession session, @RequestParam(value="orgId", required=false) Long orgId) {
		UserCookie userCookie = (UserCookie) session.getAttribute(ConstantValue.USER_COOKIE_IN_SESSION);
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		if(orgId==null || orgId<=0L) { //-- 根
			List<OrgEntityInfoBO> orgList = userCookie.getLocationList();
			if(orgList!=null) {
				for(OrgEntityInfoBO org : orgList) {
					TreeNode node = SQBOTransTool.transOrgEntityToTreeNode(org);
					//地域树上的部门不显示，修改根据信息域类型、上级信息域ID或者信息域编码取得下级列表，orgType  1 为地域：福州市；  0为部门如：财政厅
					//List<OrgEntityInfoBO> subList = userCookie.queryOrgListById(org.getOrgId(), null, ConstantValue.INTF_ORG_QUERY_TYPE_ID);
					List<OrgEntityInfoBO> subList = orgEntityInfoService.findOrgListByParentId(org.getOrgId());
					node.setHasChildren((subList!=null && subList.size()>0)?true:false);
					nodes.add(node);
				}
			}
		} else { //-- 子级节点
			//List<OrgEntityInfoBO> orgList = userCookie.queryOrgListById(orgId, null, ConstantValue.INTF_ORG_QUERY_TYPE_ID);
			List<OrgEntityInfoBO> orgList = orgEntityInfoService.findOrgListByParentId(orgId);
			if(orgList!=null) {
				for(OrgEntityInfoBO org : orgList) {
					TreeNode node = SQBOTransTool.transOrgEntityToTreeNode(org);
					//List<OrgEntityInfoBO> subList = userCookie.queryOrgListById(org.getOrgId(), null, ConstantValue.INTF_ORG_QUERY_TYPE_ID);
					List<OrgEntityInfoBO> subList = orgEntityInfoService.findOrgListByParentId(org.getOrgId());
					node.setHasChildren((subList!=null && subList.size()>0)?true:false);
					nodes.add(node);
				}
			}
		}
		
		//@add by wushy 2014-4-29  需要把有下级的节点排在前面
		List<TreeNode> l = new ArrayList<TreeNode>();
		if(nodes.size() > 0){
			for(TreeNode node : nodes){
				if(node.isHasChildren()){
					l.add(node);
				}
			}
			for(TreeNode node : nodes){
				if(!node.isHasChildren()){
					l.add(node);
				}
			}
		}
		return l;
	}
}
