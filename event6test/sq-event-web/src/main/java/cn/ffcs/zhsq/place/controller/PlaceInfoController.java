package cn.ffcs.zhsq.place.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import cn.ffcs.file.service.FileUploadService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IDictionaryService;
import cn.ffcs.shequ.grid.domain.db.StoreInfo;
import cn.ffcs.shequ.grid.service.IStoreInfoService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.CorBaseInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PlaceFireFightMeasure;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PlaceInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.PlaceType;
import cn.ffcs.shequ.wap.domain.pojo.Bar;
import cn.ffcs.shequ.wap.domain.pojo.Campus;
import cn.ffcs.shequ.zzgl.service.grid.IBarService;
import cn.ffcs.shequ.zzgl.service.grid.ICampusService;
import cn.ffcs.shequ.zzgl.service.grid.ICorBaseInfoService;
import cn.ffcs.shequ.zzgl.service.grid.IPlaceInfoService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * 重点场所信息
 * @author wangty
 *
 */
@Controller
@RequestMapping(value="/zhsq/placeInfo")
public class PlaceInfoController extends ZZBaseController{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IPlaceInfoService placeInfoService;	
	//@Autowired
	private ICorBaseInfoService corBaseInfoService;	
	@Autowired
	private IDictionaryService dictionaryService;
	//@Autowired
	private IStoreInfoService storeInfoService;
	//@Autowired
	private ICampusService campusService;
	@Autowired
	private FileUploadService fileUploadService;
	//-- 文件上传目录
	private static final String FILE_UPLOAD_FLODER = "resRoad";
	//@Autowired
	private IBarService barService;
	
	
	/**
	 * 重点场所信息
	 */
	@RequestMapping(value="/index")
	public String index(HttpSession session, ModelMap map) {
	   //UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);	   
		List<PlaceType> plaTypeDC = placeInfoService.getPlaTypeList();
		map.addAttribute("plaTypeDC", plaTypeDC);
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("startGridCodeName", defaultGridInfo.get(KEY_START_GRID_CODE_NAME));
        return "/zzgl_grid/placeInfo/index.ftl";
	}
	
	/**
	 * 重点场所信息汇总表数据
	 * @param session
	 * @param page 
	 * @param rows
	 * @param gridId
	 * @param roomAddress
	 * @param placeType
	 * @param isFocus
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listData", method=RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination listData(HttpSession session, @RequestParam(value="page") int page, @RequestParam(value="rows") int rows,
			@RequestParam(value="gridId", required=false) Long gridId, @RequestParam(value="plaName", required=false) String plaName,@RequestParam(value="roomAddress", required=false) String roomAddress,
			@RequestParam(value="plaType", required=false) String plaType,@RequestParam(value="isFocus", required=false) String isFocus,@RequestParam(value="buildingId", required=false) Long buildingId,@RequestParam(value="roomId", required=false) Long roomId) {
		if(page<=0) page=1;
		if(plaName!=null) plaName = plaName.trim();
		if(roomAddress!=null) roomAddress = roomAddress.trim();
		if(plaType!=null) plaType = plaType.trim();
		if(isFocus!=null){isFocus=isFocus.trim();}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gridId", gridId);
		params.put("plaName", plaName);
		params.put("roomAddress", roomAddress);
		params.put("plaType", plaType);
		params.put("isFocus", isFocus);
		params.put("buildingId", buildingId);
		params.put("roomId", roomId);
		cn.ffcs.common.EUDGPagination pagination = placeInfoService.findPlaceInfoPagination(page, rows, params);
	     @SuppressWarnings("unchecked")
		List<PlaceInfo>	list=(List<PlaceInfo>) pagination.getRows();
	     for(int i=0;i<list.size();i++){
	    	 PlaceInfo record=list.get(i);
	    	// record.setPlaTypeLabel(placeInfoService.findPlaceTypeById(record.getPlaType()).getTypeName());	
	    	 PlaceType placeType = placeInfoService.findPlaceTypeById(record.getPlaType());
	    	 if (placeType!=null){
	    		 record.setPlaTypeLabel(placeType.getTypeName());
	    	 }else{
	    		 record.setPlaTypeLabel("");
	    	 }
	     }
		
		return pagination;
	}
	

	/**
	 * 添加重点场所信息
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/create")
	public String create(HttpSession session,@RequestParam(value="gridId") Long gridId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		MixedGridInfo mixedGridInfo=mixedGridInfoService.findMixedGridInfoById(gridId, false);		 
		map.addAttribute("gridId", gridId);
		PlaceInfo placeInfo = new PlaceInfo();
		if(mixedGridInfo!=null){
			placeInfo.setGridName(mixedGridInfo.getGridName());
		}	
		
		map.addAttribute("placeInfo", placeInfo);
		PlaceFireFightMeasure placeFireFightMeasure = new PlaceFireFightMeasure();
		map.addAttribute("placeFireFightMeasure", placeFireFightMeasure);
		CorBaseInfo corBaseInfo = new CorBaseInfo();
		map.addAttribute("corBaseInfo", corBaseInfo);
		
		StoreInfo storeInfo=new StoreInfo();
		map.addAttribute("storeInfo", storeInfo);
		Campus campus=new Campus();
		map.addAttribute("campus", campus);
		
		//-- 场所类型
		List<PlaceType> plaTypeDC = placeInfoService.getPlaTypeList();
		map.addAttribute("plaTypeDC", plaTypeDC);
		//-- 数据字典-法人单位信息相关
		List<Map<String, Object>> corTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_TYPE, userInfo.getOrgCode());
		map.addAttribute("corTypeDC", corTypeDC);
		List<Map<String, Object>> currencyTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_REGISTERED_CURRENCY, userInfo.getOrgCode());
		map.addAttribute("currencyTypeDC", currencyTypeDC);
		List<Map<String, Object>> categoryDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_CATEGORY, userInfo.getOrgCode());
		map.addAttribute("categoryDC", categoryDC);
		List<Map<String, Object>> economicTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_ECONOMIC_TYPE, userInfo.getOrgCode());
		map.addAttribute("economicTypeDC", economicTypeDC);
		//店招状态
		List<Map<String, Object>> storeStatusDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_PLA_STORE, ConstantValue.COLUMN_STORE_STATUS, userInfo.getOrgCode());
		map.addAttribute("storeStatusDC", storeStatusDC);	
		//店招审批状态
		List<Map<String, Object>> storeApprovaStatusDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_PLA_STORE, ConstantValue.COLUMN_GARBAGE_IS_COST, userInfo.getOrgCode());
		map.addAttribute("storeApprovaStatusDC", storeApprovaStatusDC);	
		//垃圾处置费是否缴纳
		List<Map<String, Object>> garbageIsCostDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_PLA_STORE, ConstantValue.COLUMN_STORE_APPROVA_STATUS, userInfo.getOrgCode());
		map.addAttribute("garbageIsCostDC", garbageIsCostDC);	
		
		//学校类型
		List<Map<String, Object>> campusTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_CAMPUS, ConstantValue.COLUMN_CAMPUS_TYPE,"");
		map.addAttribute("campusTypeDC", campusTypeDC);	
		return "/zzgl_grid/placeInfo/createOrEdit.ftl";
	}
	
	/**
	 * 编辑重点场所信息
	 * @param session
	 * @param plaId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(HttpSession session, @RequestParam(value="plaId") Long plaId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		PlaceInfo placeInfo = placeInfoService.findPlaceInfoById(plaId);
		map.addAttribute("placeInfo", placeInfo);
		PlaceFireFightMeasure placeFireFightMeasure = placeInfoService.findPlaceFireFightMeasureById(plaId);
		map.addAttribute("placeFireFightMeasure", placeFireFightMeasure);
		//获取法人单位信息(因为存在关系表,为了以后扩展,这里暂时当做一对一处理,取第一条满足的数据)
		CorBaseInfo corBaseInfo = new CorBaseInfo();
		List<CorBaseInfo> corBaseInfoList =corBaseInfoService.getCorBaseInfoListByPlaId(placeInfo.getPlaId());
		if (corBaseInfoList != null && corBaseInfoList.size()>0){
			corBaseInfo = corBaseInfoList.get(0);
		}		
		map.addAttribute("corBaseInfo", corBaseInfo);
		
		Campus campus=campusService.findCampusByPlaId(plaId);
		map.addAttribute("campus", campus);
		Bar bar=barService.findBarByPlaId(plaId);
		map.addAttribute("bar", bar);
		StoreInfo storeInfo=storeInfoService.getStoreInfoByPlaId(Integer.valueOf(plaId.toString()));
		map.addAttribute("storeInfo", storeInfo);
//		//获取学校信息
//		String plaType=placeInfo.getPlaType().toString();
//		if(("905").equals(plaType)){//学校
//			Campus campus=campusService.findCampusByPlaId(plaId);
//			map.addAttribute("campus", campus);
//		}else if(("801").equals(plaType)){//店
//			StoreInfo storeInfo=storeInfoService.getStoreInfoByPlaId(Integer.valueOf(plaId.toString()));
//			map.addAttribute("storeInfo", storeInfo);
//		}
		
		
		//-- 场所类型
		List<PlaceType> plaTypeDC = placeInfoService.getPlaTypeList();
		map.addAttribute("plaTypeDC", plaTypeDC);
		//-- 数据字典-法人单位信息相关
		List<Map<String, Object>> corTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_TYPE, userInfo.getOrgCode());
		map.addAttribute("corTypeDC", corTypeDC);
		List<Map<String, Object>> currencyTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_REGISTERED_CURRENCY, userInfo.getOrgCode());
		map.addAttribute("currencyTypeDC", currencyTypeDC);
		List<Map<String, Object>> categoryDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_CATEGORY, userInfo.getOrgCode());
		map.addAttribute("categoryDC", categoryDC);
		List<Map<String, Object>> economicTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_COR_BASE_INFO, ConstantValue.COLUMN_COR_ECONOMIC_TYPE, userInfo.getOrgCode());
		map.addAttribute("economicTypeDC", economicTypeDC);	
		//店招状态
		List<Map<String, Object>> storeStatusDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_PLA_STORE, ConstantValue.COLUMN_STORE_STATUS, userInfo.getOrgCode());
		map.addAttribute("storeStatusDC", storeStatusDC);	
		//店招审批状态
		List<Map<String, Object>> storeApprovaStatusDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_PLA_STORE, ConstantValue.COLUMN_GARBAGE_IS_COST, userInfo.getOrgCode());
		map.addAttribute("storeApprovaStatusDC", storeApprovaStatusDC);	
		//垃圾处置费是否缴纳
		List<Map<String, Object>> garbageIsCostDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_PLA_STORE, ConstantValue.COLUMN_STORE_APPROVA_STATUS, userInfo.getOrgCode());
		map.addAttribute("garbageIsCostDC", garbageIsCostDC);	
		//学校类型
		List<Map<String, Object>> campusTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_CAMPUS, ConstantValue.COLUMN_CAMPUS_TYPE,"");
		map.addAttribute("campusTypeDC", campusTypeDC);	
		//图片路径
	    map.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		return "/zzgl_grid/placeInfo/createOrEdit.ftl";
	}
	
	/**
	 * 保存或者更新重点场所信息
	 * @param session
	 * @param placeInfo
	 * @param placeFireFightMeasure
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/saveOrUpdate", method=RequestMethod.POST)
	public String save(HttpSession session, @RequestParam(value="addCorpFlag",required=false) String addCorpFlag,
			@ModelAttribute(value="placeFireFightMeasure") PlaceFireFightMeasure placeFireFightMeasure, 
			@ModelAttribute(value="corBaseInfo") CorBaseInfo corBaseInfo,
			@ModelAttribute(value="storeInfo") StoreInfo storeInfo, 
			@ModelAttribute(value="campus") Campus campus, 
			@ModelAttribute(value="bar") Bar bar,
			@ModelAttribute(value="placeInfo") PlaceInfo placeInfo, 
			@RequestParam(value="storePhotoFile", required=false) MultipartFile storePhotoFile,
			@RequestParam(value="storeSignsPhotoFile", required=false) MultipartFile storeSignsPhotoFile,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		//获取当前的存储类型
		Long palType=placeInfo.getPlaType();
		Date now = Calendar.getInstance().getTime();
		placeInfo.setUpdateDate(now);
		placeInfo.setUpdateUser(userInfo.getUserId());
		placeFireFightMeasure.setUpdateDate(now);
		placeFireFightMeasure.setUpdateUser(userInfo.getUserId());
		corBaseInfo.setUpdateDate(now);
		corBaseInfo.setUpdateUser(userInfo.getUserId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//Bar bar=new Bar();
		//bar.setAddress("福州");
		//bar.setCreateUser(12332L);
		
		//-- 采集时间,成立日期
		try {
			if(!StringUtils.isBlank(placeInfo.getGatheringDateStr()))
				placeInfo.setGatheringDate(sdf.parse(placeInfo.getGatheringDateStr()));
			if(!StringUtils.isBlank(corBaseInfo.getEstablishDateStr()))
				corBaseInfo.setEstablishDate(sdf.parse(corBaseInfo.getEstablishDateStr()));
		} catch (Exception e) { e.printStackTrace(); }
		
		//-- 保存或者更新重点场所信息
		boolean result = false;
		if(placeInfo.getPlaId()!=null && placeInfo.getPlaId()>0L) { //--更新
			placeFireFightMeasure.setPlaId(placeInfo.getPlaId());
			result = placeInfoService.updatePlaceInfo(placeInfo, placeFireFightMeasure);
			//CorBaseInfo corBaseInfo = corBaseInfoService.getCorBaseInfoListByPlaId(placeInfo.getPlaId()).get(0);
			if (addCorpFlag !=null) {
				if (corBaseInfo.getCbiId()!=null){
					corBaseInfoService.updateCorBaseInfo(corBaseInfo);
				}
				else{
					corBaseInfo.setCreateDate(now);
					corBaseInfo.setCreateUser(userInfo.getUserId());
					corBaseInfo.setStatus(ConstantValue.STATUS_DEFAULT);
					corBaseInfo.setPlaId(placeInfo.getPlaId());
					Long cbiId = corBaseInfoService.saveCorBaseInfoReturnId(corBaseInfo);	
				}
			}
			
			if(result){
				//如果上面的操作成功则：判断是什么类型
				if(palType==905L){//学校
					campusService.updateCampus(campus);
				}else if(palType==507){//网吧
					barService.insertBar(bar);
				}else if(palType==801L){//商场（店）
					//--图片
					try {
						if(storePhotoFile!=null && !storePhotoFile.isEmpty()) {
						    String fileName = storePhotoFile.getOriginalFilename();
							byte[] multipartFileBytes = storePhotoFile.getBytes();
							String filePath = fileUploadService.uploadSingleFile(fileName, multipartFileBytes, ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_FLODER);
							storeInfo.setStorePhoto(filePath);
						}
						if(storeSignsPhotoFile!=null && !storeSignsPhotoFile.isEmpty()) {
						    String fileName = storeSignsPhotoFile.getOriginalFilename();
							byte[] multipartFileBytes = storeSignsPhotoFile.getBytes();
							String filePath = fileUploadService.uploadSingleFile(fileName, multipartFileBytes, ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_FLODER);
							storeInfo.setStoreSignsPhoto(filePath);
						}
					} catch (IOException e) {
						logger.info("创建门店信息，上传图片失败！");
						e.printStackTrace();
					}
					storeInfoService.saveOrUpdate(storeInfo);
					//还有上传企业的图片
				}
			}
			
		} else { //--保存
			placeInfo.setCreateDate(now);
			placeInfo.setCreateUser(userInfo.getUserId());
			placeInfo.setStatus(ConstantValue.STATUS_DEFAULT);
			placeFireFightMeasure.setCreateDate(now);
			placeFireFightMeasure.setCreateUser(userInfo.getUserId());
			placeFireFightMeasure.setStatus(ConstantValue.STATUS_DEFAULT);
			//placeFireFightMeasure.setPlaId(placeInfo.getPlaId());
			Long plaId = placeInfoService.savePlaceInfoReturnId(placeInfo, placeFireFightMeasure);
			if (plaId>0){
				if (addCorpFlag !=null){
					corBaseInfo.setCreateDate(now);
					corBaseInfo.setCreateUser(userInfo.getUserId());
					corBaseInfo.setStatus(ConstantValue.STATUS_DEFAULT);
					corBaseInfo.setPlaId(plaId);
					Long cbiId = corBaseInfoService.saveCorBaseInfoReturnId(corBaseInfo);					
				}
				
				//判断是什么类型
				if(palType==905L){//学校
					campus.setPlaId(plaId);
					campusService.insertCampus(campus);
				}else if(palType==507){//网吧
					bar.setPlaId(plaId);
					barService.insertBar(bar);
				}else if(palType==801L){//商场（店）
					
					//--图片
					try {
						if(storePhotoFile!=null && !storePhotoFile.isEmpty()) {
						    String fileName = storePhotoFile.getOriginalFilename();
							byte[] multipartFileBytes = storePhotoFile.getBytes();
							String filePath = fileUploadService.uploadSingleFile(fileName, multipartFileBytes, ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_FLODER);
							storeInfo.setStorePhoto(filePath);
						}
						if(storeSignsPhotoFile!=null && !storeSignsPhotoFile.isEmpty()) {
						    String fileName = storeSignsPhotoFile.getOriginalFilename();
							byte[] multipartFileBytes = storeSignsPhotoFile.getBytes();
							String filePath = fileUploadService.uploadSingleFile(fileName, multipartFileBytes, ConstantValue.RESOURSE_DOMAIN_KEY, FILE_UPLOAD_FLODER);
							storeInfo.setStoreSignsPhoto(filePath);
						}
					} catch (IOException e) {
						logger.info("创建门店信息，上传图片失败！");
						e.printStackTrace();
					}
					
					storeInfo.setCreateDate(now);
					storeInfo.setCreateUserId(Integer.valueOf(userInfo.getUserId().toString()));
					storeInfo.setPlaId(Integer.valueOf(plaId.toString()));
					storeInfoService.saveOrUpdate(storeInfo);
					//还有上传企业的图片
				}
			}
			result = plaId>0?true:false;
		}
		map.addAttribute("result", result?"保存成功！":"保存失败！");
		return "/zzgl_grid/result.ftl";
	}
	
	/**
	 * 查看重点场所信息
	 * @param session
	 * @param plaId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/detail")
	public String show(HttpSession session, @RequestParam(value="plaId") Long plaId, ModelMap map) {
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		PlaceInfo placeInfo = placeInfoService.findPlaceInfoById(plaId);
		map.addAttribute("placeInfo", placeInfo);
		PlaceFireFightMeasure placeFireFightMeasure = placeInfoService.findPlaceFireFightMeasureById(plaId);
		map.addAttribute("placeFireFightMeasure", placeFireFightMeasure);
		//获取法人单位信息(因为存在关系表,为了以后扩展,这里暂时当做一对一处理,取第一条满足的数据)
		CorBaseInfo corBaseInfo = new CorBaseInfo();
		List<CorBaseInfo> corBaseInfoList =corBaseInfoService.getCorBaseInfoListByPlaId(placeInfo.getPlaId());
		if (corBaseInfoList != null && corBaseInfoList.size()>0){
			corBaseInfo = corBaseInfoList.get(0);
		}		
		map.addAttribute("corBaseInfo", corBaseInfo);
		
		Campus campus=campusService.findCampusByPlaId(plaId);
		map.addAttribute("campus", campus);
		Bar bar=barService.findBarByPlaId(plaId);
		map.addAttribute("bar", bar);
		StoreInfo storeInfo=storeInfoService.getStoreInfoByPlaId(Integer.valueOf(plaId.toString()));
		map.addAttribute("storeInfo", storeInfo);
		
		
		//-- 场所类型
		List<PlaceType> plaTypeDC = placeInfoService.getPlaTypeList();
		String placeType = "";
		if (placeInfo!=null){
	    	 placeType = placeInfo.getPlaType().toString();
				for (PlaceType dataDict : plaTypeDC) {
					if(placeType!=null&&placeType.equals(dataDict.getKeyId()+"")){						
						placeInfo.setPlaTypeLabel(dataDict.getTypeName());
						break;
					}
				}
	    }	
		map.addAttribute("plaTypeDC", plaTypeDC);
		
		//店招状态
		List<Map<String, Object>> storeStatusDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_PLA_STORE, ConstantValue.COLUMN_STORE_STATUS, userInfo.getOrgCode());
		map.addAttribute("storeStatusDC", storeStatusDC);	
		//店招审批状态
		List<Map<String, Object>> storeApprovaStatusDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_PLA_STORE, ConstantValue.COLUMN_GARBAGE_IS_COST, userInfo.getOrgCode());
		map.addAttribute("storeApprovaStatusDC", storeApprovaStatusDC);	
		//垃圾处置费是否缴纳
		List<Map<String, Object>> garbageIsCostDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_PLA_STORE, ConstantValue.COLUMN_STORE_APPROVA_STATUS, userInfo.getOrgCode());
		map.addAttribute("garbageIsCostDC", garbageIsCostDC);
		List<Map<String, Object>> campusTypeDC = dictionaryService.getTableColumnDC(ConstantValue.TABLE_CAMPUS, ConstantValue.COLUMN_CAMPUS_TYPE,"");
		map.addAttribute("campusTypeDC", campusTypeDC);	
		return "/zzgl_grid/placeInfo/detail.ftl";
	}
	
	/**
	 * 查看重点场所信息与企业基本信息
	 * @param session
	 * @param plaId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/placeCorDetail")
	public String placeCorDetail(HttpSession session, @RequestParam(value="plaId") Long plaId, ModelMap map) {
		PlaceInfo placeInfo = placeInfoService.findPlaceInfoById(plaId);
		if(placeInfo!=null&&placeInfo.getPlaType()!=null){
			placeInfo.setPlaTypeLabel(placeInfoService.findPlaceTypeById(placeInfo.getPlaType()).getTypeName());	   	 
		}
		map.addAttribute("placeInfo", placeInfo);
		List<CorBaseInfo> corBaseInfoList=corBaseInfoService.getCorBaseInfoListByPlaId(plaId);
		CorBaseInfo corBaseInfo=null;
		if(corBaseInfoList!=null&&corBaseInfoList.size()>0){
			corBaseInfo=corBaseInfoList.get(0);
		}else{
			corBaseInfo=new CorBaseInfo();
		}
		map.addAttribute("corBaseInfo", corBaseInfo);
		return "/zzgl_grid/placeInfo/placeCorDetail.ftl";
	}
	
	
	/**
	 * 批量删除重点场所信息
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
		boolean result = placeInfoService.deletePlaceInfoById(userInfo.getUserId(), idList);
		
		//删除法人代表与场所关系表,这里暂不对法人代表信息表进行删除
		corBaseInfoService.deletePlaceAndCorBaseInfoByPlaId(idList);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result?idList.size():0L);
		return resultMap;
	}
	
	/**
	 * 是否重点场所信息汇总表数据
	 * @param session
	 * @param gridId	
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listPlaFocusCountData", method=RequestMethod.POST)
	public List<PlaceInfo> listPlaFocusCountData(HttpSession session,@RequestParam(value="gridId") Long gridId,ModelMap map) {
		List<PlaceInfo>  focusList = new ArrayList();
		focusList  = placeInfoService.placesStaticByFocusList(gridId);		
	     for(int i=0;i<focusList.size();i++){
	    	 PlaceInfo  placeInfo=focusList.get(i);
	    	 //if(!StringUtils.isBlank(placeInfo.getIsFocus())) {
	    			if(placeInfo.getIsFocus()==null||placeInfo.getIsFocus().equals("")){
	    				placeInfo.setIsFocusLabel("未标识");
		   			}else{
		   				if(placeInfo.getIsFocus()!=null&&placeInfo.getIsFocus().equals("1")){
		   					placeInfo.setIsFocusLabel("重点场所");		   					
		   				}else{
		   					placeInfo.setIsFocusLabel("非重点场所");			   					
		   				}		   				
		   			}
				//}
	     }
	    //map.addAttribute("name", "场所分类数量");
		//map.addAttribute("columnarChartName", "重点场所分类");//图的名称
		return focusList;
	}
	
	/**
	 * 重点场所类型信息汇总表数据
	 * @param session
	 * @param gridId	
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listPlaTypeCountData", method=RequestMethod.POST)
	public List<PlaceInfo> listPlaTypeCountData(HttpSession session,@RequestParam(value="gridId") Long gridId,ModelMap map) {
		List<PlaceInfo>  playTypeList = new ArrayList();
		List<PlaceInfo>  resultPlayTypeList = new ArrayList();
		playTypeList = placeInfoService.lookPlacesStaticInfoList(gridId);
		List<PlaceType> plaTypeDC = placeInfoService.getPlaTypeList();
		String placeType = "";
		int j = 0;
		for (int i = playTypeList.size() - 1; i >= 0; i--) {
			/*if (j > 4) {
				break;
			}*/	    
	    	PlaceInfo placeInfo=playTypeList.get(i);
	    	if (placeInfo!=null && placeInfo.getPlaType()!=null){
		    	 placeType = placeInfo.getPlaType().toString();
					for (PlaceType dataDict : plaTypeDC) {
						if(placeType!=null&&placeType.equals(dataDict.getKeyId()+"")){						
							placeInfo.setPlaTypeLabel(dataDict.getTypeName());
							break;
						}
					}
		    }
	    	// Modify 2014-03-28 By @YangCQ 去掉未知类型
			if (placeInfo.getPlaTypeLabel() == null || placeInfo.getPlaTypeLabel().equals("")) {
				placeInfo.setPlaTypeLabel("未知类型");
			} else {
				resultPlayTypeList.add(placeInfo);
			}
			j++;			
	     }
	    //map.addAttribute("name", "场所数量");
		//map.addAttribute("columnarChartName", "前5种场所类型");//图的名称
		return resultPlayTypeList;//取前5种场所类型
	}
	
	/**
	 * 场所信息选择
	 * @param session
	 * @param gridId 网格ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/listPlace")
	public String listPlace(HttpSession session, @RequestParam(value="gridId", required=false) Long gridId,ModelMap map) {
		if(gridId==null){
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			gridId=(Long) defaultGridInfo.get(KEY_START_GRID_ID);
		}
		List<PlaceType> plaTypeDC = placeInfoService.getPlaTypeList();
	    map.addAttribute("plaTypeDC", plaTypeDC);
		map.addAttribute("gridId", gridId);
		return "/zzgl/place/list_place.ftl";		
	}
}
