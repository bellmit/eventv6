package cn.ffcs.zhsq.building.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.shequ.base.domain.db.AnnexInfo;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IAnnexInfoService;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.AreaBuildingInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.FloorUnitFigure;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IAreaBuildingInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 楼房信息
 * @author zhengcy
 *
 */
@Controller
@RequestMapping(value="/zhsq/building/areaBuildingInfo")
public class AreaBuildingInfoController extends ZZBaseController{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IAreaBuildingInfoService areaBuildingInfoService;
	
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	
	@Autowired
	private IDictionaryService dictionaryService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private IAnnexInfoService annexInfoService; // 附件服务
	
	//@Autowired
	//private IGridAdminService gridAdminService; // 网格管理员服务
	
	//-- 文件上传目录
	private static final String FILE_UPLOAD_FLOOR= "areaBuildingFloorStructure";
	//-- 文件上传目录
	private static final String FILE_UPLOAD_EXTERIOR = "areaBuildingBuildingExteriorFigure";
     
    
	/**
	 * 楼房信息
	 */
	@RequestMapping(value="/index")
	public String index(HttpSession session, ModelMap map) {
	   UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("startGridCodeName", defaultGridInfo.get(KEY_START_GRID_CODE_NAME));
		List<Map<String, Object>> useNatureDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_AREA_BUILDING_INFO, ConstantValue.COLUMN_AREA_BUILDING_INFO_USE_NATURE, userInfo.getOrgCode());
	    map.addAttribute("useNatureDC", useNatureDC);
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
        return "/zzgl_grid/areaBuildingInfo/index.ftl";
	}
	
	
	/**
	 * 房列表
	 * 
	 * @param session
	 * @param map
	 * @param useNature
	 * @return
	 * @throws Exception
	 * @author wwm
	 */
	@RequestMapping(value = "/gisList")
	public String gisList(
			HttpSession session,
			ModelMap map,
			@RequestParam(value = "useNature", required = false) String useNature)
			throws Exception {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("useNature", useNature);
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		return "zzgl_map_data/floor/obj_bulid.ftl";
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
			@RequestParam(value="buildingAddress", required=false) String buildingAddress,
			@RequestParam(value="useNature", required=false) String useNature,
			//2013-8-14 12:30
			@RequestParam(value="mapt", required=false) String mapt,
			//2013-10-01 liush add
			@RequestParam(value="buildingYear", required=false) String buildingYear
			
	) {
		if(page<=0) page=1;
		if(buildingName!=null) buildingName = buildingName.trim();
		if(buildingAddress!=null) buildingAddress = buildingAddress.trim();
		if(useNature!=null) useNature = useNature.trim();
		
		//2013-8-14 12:30
		//sulch
		if(mapt!=null) mapt = mapt.trim();
		
		if(customCode!=null){customCode=customCode.trim();}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", gridId);
		params.put("buildingName", buildingName);
		params.put("buildingAddress", buildingAddress);
		params.put("customCode", customCode);
		params.put("useNature", useNature);
		//2013-8-14 12:30
		//sulch
		params.put("mapt", mapt);
		//2013-10-01
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
	 * 网格楼栋名称唯一性判断
	 * @param session
	 * @param gridId
	 * @param buildingName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/checkBuildingName")
	public Map repeatCountForBuildingName(HttpSession session, 
			@RequestParam(value="gridId", required=false) Long gridId, 
			@RequestParam(value="buildingName", required=false) String buildingName
			) {
		Map<String, Object> result = new HashMap<String, Object>();
		Integer count = 0;
		if(gridId!=null && buildingName!=null){
			count = areaBuildingInfoService.RepeatCountForBuildingName(gridId,buildingName);
		}
		if(count>0){
			result.put("check", false);
		}else{
			result.put("check", true);
		}
		
		return result;
	}
	
	/**
	 * 楼房信息选择
	 * @param session
	 * @param gridId 网格ID
	 * @param roomSelect 是否提供房屋选择，为空或者不为false时提供
	 * @param buildingName 单元选择页面传入楼宇名
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/listBuilding")
	public String listBuilding(HttpSession session, @RequestParam(value="gridId", required=false) Long gridId,
			@RequestParam(value="roomSelect", required=false) String roomSelect,@RequestParam(value="buildingName", required=false) String buildingName, ModelMap map) {
		if(gridId==null){
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			gridId=(Long) defaultGridInfo.get(KEY_START_GRID_ID);
		}
		MixedGridInfo parentGrid = mixedGridInfoService.findMixedGridInfoById(gridId, false);
	    UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<Map<String, Object>> useNatureDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_AREA_BUILDING_INFO, ConstantValue.COLUMN_AREA_BUILDING_INFO_USE_NATURE, userInfo.getOrgCode());
	    map.addAttribute("useNatureDC", useNatureDC);
		map.addAttribute("gridId", gridId);
		if(parentGrid!=null){
		  map.addAttribute("gridName", parentGrid.getGridName());
		}
		if(buildingName != null){
			try {
				buildingName = java.net.URLDecoder.decode(buildingName, "utf-8");
				map.addAttribute("buildingName", buildingName);
			} catch (UnsupportedEncodingException e) {}
		}
		if(roomSelect!=null && roomSelect.equals("false")) return "/zzgl/building/list_building.ftl";
		else return "/zzgl/building/buildingRoomList.ftl";
	}
	
	/**
	 * 增加楼房信息
	 * @param gridId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/create")
	public String create(HttpSession session, @RequestParam(value="gridId") Long gridId, ModelMap map) {
	    MixedGridInfo mixedGridInfo=mixedGridInfoService.findMixedGridInfoById(gridId, false);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("userInfo", userInfo);
		map.addAttribute("gridId", gridId);
		AreaBuildingInfo record = new AreaBuildingInfo();
		if(mixedGridInfo!=null){
			record.setGridName(mixedGridInfo.getGridName());
			record.setGridCode(mixedGridInfo.getGridCode());
			record.setOrgId(mixedGridInfo.getInfoOrgId());
		}
		map.addAttribute("record", record);
		//-- 数据字典读取相关选项数据
		List<Map<String, Object>> useNatureDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_AREA_BUILDING_INFO, ConstantValue.COLUMN_AREA_BUILDING_INFO_USE_NATURE, userInfo.getOrgCode());
		List<Map<String, Object>> buildingStatusDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_AREA_BUILDING_INFO, ConstantValue.COLUMN_AREA_BUILDING_INFO_BUILDING_STATUS, userInfo.getOrgCode());
		map.addAttribute("buildingStatusDC", buildingStatusDC);
		map.addAttribute("useNatureDC", useNatureDC);
		///TODO 晋江个性化定制 ====================
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String orgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE)+"";
		if(orgCode!=null && orgCode.indexOf(ConstantValue.JINJIANG_FUNC_ORG_CODE)>-1){
			return "/zzgl_grid/areaBuildingInfo/create350582.ftl";
		}
		/// ================================== end 
		
		return "/zzgl_grid/areaBuildingInfo/create.ftl";
	}
	
	
	/**
	 * 保存新建的刑释解教信息
	 * @param session
	 * @param risk
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String save(HttpSession session, @ModelAttribute(value="record") AreaBuildingInfo record,
			@RequestParam(value="floorStructureFile", required=false) MultipartFile floorStructureFile,
			@RequestParam(value="buildingExteriorFigureFile", required=false) MultipartFile buildingExteriorFigureFile,
			@RequestParam(value="attachments2",required=false) MultipartFile[] attachments2,
			@RequestParam(value="attachments",required=false) MultipartFile[] attachments, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Date now = Calendar.getInstance().getTime();
		record.setUpdateDate(now);
		record.setCreateDate(now);
		record.setUpdateUser(userInfo.getUserId());
		record.setCreateUser(userInfo.getUserId());
		record.setStatus(ConstantValue.STATUS_DEFAULT);
		try {
			if(floorStructureFile!=null && !floorStructureFile.isEmpty()) {
			    String fileName = floorStructureFile.getOriginalFilename();
				byte[] multipartFileBytes = floorStructureFile.getBytes();
				String filePath = fileUploadService.uploadSingleFile(fileName, multipartFileBytes, ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_FLOOR);
				record.setFloorStructure(filePath);
			}
			if(buildingExteriorFigureFile!=null && !buildingExteriorFigureFile.isEmpty()) {
				String fileName = buildingExteriorFigureFile.getOriginalFilename();
				byte[] multipartFileBytes = buildingExteriorFigureFile.getBytes();
				String filePath = fileUploadService.uploadSingleFile(fileName, multipartFileBytes, ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_EXTERIOR);
				record.setBuildingExteriorFigure(filePath);
			}
		} catch (IOException e) {
			logger.info("创建楼房，上传楼房图像失败！");
			e.printStackTrace();
		}
		Long buildingId=null;
		try {
			buildingId = areaBuildingInfoService.saveAreaBuildingInfoReturnId(record);
			if(buildingId!=null && buildingId>0){
				//-- 保存楼栋外观附图和楼栋结构图
				try {
					// 新增的外观附图附件
					List<AnnexInfo> newAnnexList = new ArrayList<AnnexInfo>();
					if(attachments!=null) {
						for(MultipartFile file : attachments) {
							if(file!=null && !file.isEmpty()) {
								AnnexInfo annex = new AnnexInfo();
								annex.setAnnexName(file.getOriginalFilename());
								annex.setRelaKeyId(buildingId);
								annex.setUserId(userInfo.getUserId());
								annex.setUseType(ConstantValue.TYPE_ANNEX_017);
								String filePath = fileUploadService.uploadSingleFile(file.getOriginalFilename(), file.getBytes(), ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_EXTERIOR);
								annex.setAnnexUrl(filePath);
								newAnnexList.add(annex);
							}
						}
					}
					// 删除的附件
					List<Long> deleteAnnexList = new ArrayList<Long>();
					annexInfoService.saveAndUpdateAnnex(userInfo.getUserId(), newAnnexList, deleteAnnexList);
					
					// 新增的楼栋结构附图附件
					List<AnnexInfo> newAnnexList2 = new ArrayList<AnnexInfo>();
					if(attachments2!=null) {
						for(MultipartFile file2 : attachments2) {
							if(file2!=null && !file2.isEmpty()) {
								AnnexInfo annex2 = new AnnexInfo();
								annex2.setAnnexName(file2.getOriginalFilename());
								annex2.setRelaKeyId(buildingId);
								annex2.setUserId(userInfo.getUserId());
								annex2.setUseType(ConstantValue.TYPE_ANNEX_018);
								String filePath = fileUploadService.uploadSingleFile(file2.getOriginalFilename(), file2.getBytes(), ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_FLOOR);
								annex2.setAnnexUrl(filePath);
								newAnnexList2.add(annex2);
							}
						}
					}
					// 删除的附件
					List<Long> deleteAnnexList2 = new ArrayList<Long>();
					annexInfoService.saveAndUpdateAnnex(userInfo.getUserId(), newAnnexList2, deleteAnnexList2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				map.addAttribute("result","添加成功！");
			} else {
				map.addAttribute("result","添加失败！");
			}
		} catch (Exception e) {
			map.addAttribute("result","添加失败！");
			return "/zzgl_grid/result.ftl";
		}
	
		return "/zzgl_grid/result.ftl";
	}
	
	
	/**
	 * 编辑楼房信息
	 * @param ciRsId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(HttpSession session, @RequestParam(value="buildingId") Long buildingId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("userInfo", userInfo);
		AreaBuildingInfo record = areaBuildingInfoService.findAreaBuildingInfoById(buildingId);
		map.addAttribute("record", record);
		//-- 数据字典读取相关选项数据
		List<Map<String, Object>> useNatureDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_AREA_BUILDING_INFO, ConstantValue.COLUMN_AREA_BUILDING_INFO_USE_NATURE, userInfo.getOrgCode());
		List<Map<String, Object>> buildingStatusDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_AREA_BUILDING_INFO, ConstantValue.COLUMN_AREA_BUILDING_INFO_BUILDING_STATUS, userInfo.getOrgCode());
		map.addAttribute("buildingStatusDC", buildingStatusDC);
		map.addAttribute("useNatureDC", useNatureDC);
		map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		List<AnnexInfo> annexList = annexInfoService.getAnnexInfoList(buildingId, userInfo.getUserId(), ConstantValue.TYPE_ANNEX_017);
		map.addAttribute("annexList", annexList);
		List<AnnexInfo> annexList2 = annexInfoService.getAnnexInfoList(buildingId, userInfo.getUserId(), ConstantValue.TYPE_ANNEX_018);
		map.addAttribute("annexList2", annexList2);		
		
		///TODO 晋江个性化定制 ====================
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String orgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE)+"";
		if(orgCode!=null && orgCode.indexOf(ConstantValue.JINJIANG_FUNC_ORG_CODE)>-1){
			return "/zzgl_grid/areaBuildingInfo/edit350582.ftl";
		}
		/// ================================== end 
		return "/zzgl_grid/areaBuildingInfo/edit.ftl";
	}
	
	/**
	 * 更新楼房信息
	 * @param session
	 * @param risk
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(HttpSession session, @ModelAttribute(value="record") AreaBuildingInfo record,
			@RequestParam(value="floorStructureFile", required=false) MultipartFile floorStructureFile,
			@RequestParam(value="buildingExteriorFigureFile", required=false) MultipartFile buildingExteriorFigureFile,
			@RequestParam(value="attachments",required=false) MultipartFile[] attachments,
			@RequestParam(value="attachments2",required=false) MultipartFile[] attachments2,
			@RequestParam(value="delAnnexIds2") String delAnnexIds2,
			@RequestParam(value="delAnnexIds") String delAnnexIds, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Date now = Calendar.getInstance().getTime();
		record.setUpdateDate(now);
		record.setUpdateUser(userInfo.getUserId());
		try {
			if(floorStructureFile!=null && !floorStructureFile.isEmpty()) {
			    String fileName = floorStructureFile.getOriginalFilename();
				byte[] multipartFileBytes = floorStructureFile.getBytes();
				String filePath = fileUploadService.uploadSingleFile(fileName, multipartFileBytes, ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_FLOOR);
				record.setFloorStructure(filePath);
			}
			if(buildingExteriorFigureFile!=null && !buildingExteriorFigureFile.isEmpty()) {
				String fileName = buildingExteriorFigureFile.getOriginalFilename();
				byte[] multipartFileBytes = buildingExteriorFigureFile.getBytes();
				String filePath = fileUploadService.uploadSingleFile(fileName, multipartFileBytes, ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_EXTERIOR);
				record.setBuildingExteriorFigure(filePath);
			}
		} catch (IOException e) {
			logger.info("创建楼房，上传楼房图像失败！");
			e.printStackTrace();
		}
		boolean result = areaBuildingInfoService.updateAreaBuildingInfo(record);
		if(result) {
			//-- 保存楼栋外观附图
			try {
				// 新增的附件
				List<AnnexInfo> newAnnexList = new ArrayList<AnnexInfo>();
				if(attachments!=null) {
					for(MultipartFile file : attachments) {
						if(file!=null && !file.isEmpty()) {
							AnnexInfo annex = new AnnexInfo();
							annex.setAnnexName(file.getOriginalFilename());
							annex.setRelaKeyId(record.getBuildingId());
							annex.setUserId(userInfo.getUserId());
							annex.setUseType(ConstantValue.TYPE_ANNEX_017);
							String filePath = fileUploadService.uploadSingleFile(file.getOriginalFilename(), file.getBytes(), ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_EXTERIOR);
							annex.setAnnexUrl(filePath);
							newAnnexList.add(annex);
						}
					}
				}
				// 删除的附件
				List<Long> deleteAnnexList = new ArrayList<Long>();
				if(!StringUtils.isBlank(delAnnexIds)) {
					String[] deleteAnnexIdArray = delAnnexIds.split(",");
					for(String deleteAnnexId:deleteAnnexIdArray) {
						deleteAnnexList.add(Long.parseLong(deleteAnnexId));
					}
				}
				annexInfoService.saveAndUpdateAnnex(userInfo.getUserId(), newAnnexList, deleteAnnexList);
				
				//-- 保存楼栋结构附图
				
				// 新增的附件
				List<AnnexInfo> newAnnexList2 = new ArrayList<AnnexInfo>();
				if(attachments2!=null) {
					for(MultipartFile file2 : attachments2) {
						if(file2!=null && !file2.isEmpty()) {
							AnnexInfo annex2 = new AnnexInfo();
							annex2.setAnnexName(file2.getOriginalFilename());
							annex2.setRelaKeyId(record.getBuildingId());
							annex2.setUserId(userInfo.getUserId());
							annex2.setUseType(ConstantValue.TYPE_ANNEX_018);
							String filePath = fileUploadService.uploadSingleFile(file2.getOriginalFilename(), file2.getBytes(), ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_FLOOR);
							annex2.setAnnexUrl(filePath);
							newAnnexList2.add(annex2);
						}
					}
				}
				// 删除的附件
				List<Long> deleteAnnexList2 = new ArrayList<Long>();
				if(!StringUtils.isBlank(delAnnexIds2)) {
					String[] deleteAnnexIdArray = delAnnexIds2.split(",");
					for(String deleteAnnexId2:deleteAnnexIdArray) {
						deleteAnnexList2.add(Long.parseLong(deleteAnnexId2));
					}
				}
				annexInfoService.saveAndUpdateAnnex(userInfo.getUserId(), newAnnexList2, deleteAnnexList2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		map.addAttribute("result", result?"更新成功！":"更新失败！");
		return "/zzgl_grid/result.ftl";
	}

	/**
	 * 楼房信息详情
	 * @param session
	 * @param buildingId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/detail")
	public String detail(HttpSession session, @RequestParam(value="buildingId") Long buildingId, ModelMap map) {
		 UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		AreaBuildingInfo record = areaBuildingInfoService.findAreaBuildingInfoById(buildingId);
		this.addExtraInfo(record);
		List<Map<String, Object>> typeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_DICTIONARY_CI_RS, ConstantValue.COLUMN_CI_RS_TYPE, userInfo.getOrgCode());
	    map.addAttribute("typeDC", typeDC);
		map.addAttribute("record", record);
		map.addAttribute("PLATFORM_DOMAIN_ROOT", App.TOP.getDomain(session));
		map.addAttribute("RESIDENT_DOMAIN", App.RS.getDomain(session));
		map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		return "/zzgl_grid/areaBuildingInfo/detail.ftl";
	}
	/**
	 * 地图楼房信息详情
	 * @param session
	 * @param buildingId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/gisDetail")
	public String gisDetail(HttpSession session, @RequestParam(value="buildingId") Long buildingId, ModelMap map) {
		AreaBuildingInfo record = areaBuildingInfoService.findAreaBuildingInfoById(buildingId);
		this.addExtraInfo(record);
		map.addAttribute("record", record);
		return "/zzgl_grid/areaBuildingInfo/gisDetail.ftl";
	}
	
	
	/**
	 * 批量删除楼房信息详情
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/batchDelete", method=RequestMethod.POST)
	public Map<String, Object> batchDeleteCompany(HttpSession session, HttpServletRequest request, @RequestParam(value="idStr") String idStr) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String[] ids = idStr.split(",");
		List<Long> recordIdList = new ArrayList<Long>();
		for(int i=0; i<ids.length; i++) {
			try {
				long recordId = Long.parseLong(ids[i]);
				recordIdList.add(recordId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		boolean result = areaBuildingInfoService.deleteAreaBuildingInfoById(userInfo.getUserId(), recordIdList);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result?recordIdList.size():0L);
		return resultMap;
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
	@RequestMapping(value="/listCountData", method=RequestMethod.POST)
	public List<AreaBuildingInfo> listCountData(HttpSession session,@RequestParam(value="gridId") Long gridId,ModelMap map) {
		List<AreaBuildingInfo> list= areaBuildingInfoService.findCountByUseNature(gridId);
		List<AreaBuildingInfo>  removeAreaBuildingInfo=new ArrayList<AreaBuildingInfo>();
		for(int i=0;i<list.size();i++){
	    	 AreaBuildingInfo record=list.get(i);
	    	 if(!StringUtils.isBlank(record.getUseNature())) {
	    		 String userNatureStr=dictionaryService.getTableColumnText(ConstantValue.TABLE_AREA_BUILDING_INFO, ConstantValue.COLUMN_AREA_BUILDING_INFO_USE_NATURE, record.getUseNature());
	    		 if(userNatureStr!=null&&!"".equals(userNatureStr)){
	    			 record.setUseNatureLabel(userNatureStr);
	    		 }else{
	    			 //保存要删除的楼栋信息
	    			 removeAreaBuildingInfo.add(record);
	    		 }
	    	 }else{
	    		 //保存要删除的楼栋信息
    			 removeAreaBuildingInfo.add(record);
	    	 }
	     }
		//删除在数字字典中没查到的楼栋使用用途
		if(removeAreaBuildingInfo!=null&&removeAreaBuildingInfo.size()>0){
			for(int i=0;i<removeAreaBuildingInfo.size();i++){
				list.remove(removeAreaBuildingInfo.get(i));
			}
		}
		
		return list;
	}
	/**
	 * 2013-10-01 liush add
	 * 楼房信息汇总表曲线数据 
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
	@RequestMapping(value="/listLineCountData", method=RequestMethod.POST)
	public List<AreaBuildingInfo> listLineCountData(HttpSession session,@RequestParam(value="gridId") Long gridId,ModelMap map) {
		List<AreaBuildingInfo> list= areaBuildingInfoService.findLineCountByUseNature(gridId);
		List<AreaBuildingInfo> resultList = new ArrayList<AreaBuildingInfo>();
		int preNum = 5;
		if(preNum > list.size()) {
			preNum = list.size();
		}
		for(int i=0;i<preNum;i++){
			AreaBuildingInfo record=list.get(i);
			resultList.add(record);
		}
		return resultList;
	}
	/**
	 * 查看平面结构图 modify by wangty增加附图，查看多个附图
	 * @param session
	 * @param buildingId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/flatStructure")
	public String flatStructure(HttpSession session, @RequestParam(value="buildingId") Long buildingId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		AreaBuildingInfo record = areaBuildingInfoService.findAreaBuildingInfoById(buildingId);
		map.addAttribute("record", record);
		map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		List<AnnexInfo> annexList = annexInfoService.getAnnexInfoList(buildingId, userInfo.getUserId(), ConstantValue.TYPE_ANNEX_018);
		map.addAttribute("annexList", annexList);
		return "/zzgl_grid/areaBuildingInfo/flatStructure.ftl";
	}
	
	/**
	 * 查看楼宇外观图
	 * @param session
	 * @param buildingId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/appearancePhotos")
	public String appearancePhotos(HttpSession session, @RequestParam(value="buildingId") Long buildingId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		AreaBuildingInfo record = areaBuildingInfoService.findAreaBuildingInfoById(buildingId);
		map.addAttribute("record", record);
		map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		List<AnnexInfo> annexList = annexInfoService.getAnnexInfoList(buildingId, userInfo.getUserId(), ConstantValue.TYPE_ANNEX_017);
		map.addAttribute("annexList", annexList);
		return "/zzgl_grid/areaBuildingInfo/appearancePhotos.ftl";
	}
	
	/**
	 * 通过企业查找楼房信息汇总表数据
	 * @param session
	 * @param page 
	 * @param rows
	 * @param useNature
	 * @param cbiId
	 * @param buildingName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/corBaseAreaBuildinglistData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination corBaseAreaBuildinglistData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows, @RequestParam(value="buildingName", required=false) String buildingName,
			@RequestParam(value="useNature", required=false) String useNature,@RequestParam(value="cbiId") Long cbiId) {
		if(page<=0) page=1;
		if(buildingName!=null) buildingName = buildingName.trim();
		if(useNature!=null) useNature = useNature.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cbiId", cbiId);
		params.put("buildingName", buildingName);
		params.put("useNature", useNature);
		cn.ffcs.common.EUDGPagination pagination = areaBuildingInfoService.findCorBaseAreaBuildingInfoPagination(page, rows, params);
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

	@RequestMapping(value="/upFloorPic")
	public String upFloorPic(HttpSession session,
			@RequestParam(value = "buildingId") Long buildingId, ModelMap map,
			@RequestParam(value = "undergroundFloorNum") Integer undergroundFloorNum,// 地下
			@RequestParam(value = "groundFloorNum") Integer groundFloorNum) {// 地面
		List<FloorUnitFigure> figureList = this.areaBuildingInfoService.findFloorUnitFigureList(buildingId);
		List<FloorUnitFigure> list = new ArrayList<FloorUnitFigure>();
		for (int i = -undergroundFloorNum; i <= groundFloorNum; i++) {
			if (i == 0) continue;
			FloorUnitFigure figure = null;
			for (FloorUnitFigure f : figureList) {
				if (Integer.valueOf(String.valueOf(f.getBusiNo())) == i) {
					figure = f;
					break;
				}
			}
			if (figure == null) {
				figure = new FloorUnitFigure();
				figure.setBusiNo(Long.valueOf(i));
			}
			list.add(figure);
		}
		map.put("buildingId", buildingId);
		map.put("figureList", list);
		map.put("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		return "/zzgl_grid/areaBuildingInfo/upFloorPic.ftl";
	}

	@RequestMapping(value="/doUpFloorPic")
	public String doUpFloorPic(HttpSession session, MultipartHttpServletRequest request, ModelMap map) throws IOException {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Long buildingId = Long.valueOf(request.getParameter("buildingId"));
		Iterator<String> fileNames = request.getFileNames();
		boolean isSuccess = true;
		while (fileNames.hasNext()) {
			String fieldName = fileNames.next();
			MultipartFile multipartFile = request.getFile(fieldName);
			if (multipartFile.getSize() > 0) {
				String fileName = multipartFile.getOriginalFilename();
				String filePath = fileUploadService.uploadSingleFile(fileName, multipartFile.getBytes(), ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_FLOOR);
				FloorUnitFigure figure = new FloorUnitFigure();
				figure.setBuildingId(buildingId);
				figure.setBusiNo(Long.valueOf(fieldName.split("_")[1]));
				figure.setAttrFile(filePath);
				figure.setCreator(userInfo.getUserId());
				figure.setUpdater(userInfo.getUserId());
				isSuccess = isSuccess && this.areaBuildingInfoService.saveOrUpdateFloorUnitFigure(figure);
			}
		}
		map.addAttribute("result", "保存" + (isSuccess ? "成功" : "失败") + "！");
		return "/zzgl_grid/result.ftl";
	}
	
	
	//----------------------------------------------
	private void addExtraInfo(AreaBuildingInfo record) {
		//-- 晋江需求 modify by guohh 管理单位如果在楼栋中没填，那么该字段显示所属网格
		if(StringUtils.isBlank(record.getManagementCompany())) {
			record.setManagementCompany(record.getGridName());
		}
		//-- 如果管理员电话没填，那么该字段显示为所属网格的网格管理员和联系电话（有几个网格员显示几个）
		/*if(StringUtils.isBlank(record.getAdminPhone())) {
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
		}*/
	}
}
