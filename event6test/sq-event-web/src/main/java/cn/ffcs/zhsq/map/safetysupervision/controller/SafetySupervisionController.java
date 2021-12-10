package cn.ffcs.zhsq.map.safetysupervision.controller;


import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IAreaBuildingInfoService;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.buildaddress.service.IBuildAddressService;
import cn.ffcs.zhsq.map.thresholdcolorcfg.service.IThresholdColorCfgService;
import cn.ffcs.zhsq.mybatis.domain.map.buildaddress.BuildAddress;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;


@Controller
@RequestMapping(value="/zhsq/map/safetysupervision/safetySupervision")
public class SafetySupervisionController  extends ZZBaseController{
	@Autowired
	private IThresholdColorCfgService thresholdColorCfgService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private IAreaBuildingInfoService areaBuildingInfoService;
	@Autowired
	private IBuildAddressService buildAddressService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	/**
	 * 2014-10-13 liushi add
	 * 链接到图层菜单管理页面
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/index")
	public String index(HttpSession session, HttpServletRequest request, ModelMap map){
		String forward = "/map/safetysupervision/index_safetysupervision.ftl";
		Map<String, Object> defaultOrgInfo = this.getDefaultOrgInfo(session);
		String orgCode = (String)defaultOrgInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
		String gridName = (String)defaultOrgInfo.get(KEY_START_GRID_NAME);
		Long gridId = (Long)defaultOrgInfo.get(KEY_START_GRID_ID);
		map.put("orgCode", orgCode);
		map.put("gridName", gridName);
		map.put("gridId", gridId);
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.put("gridId", gridInfo.getGridId());
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		String OUTLINE_FONT_SETTINGS = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		String ARCGIS_DOCK_MODE = this.funConfigurationService.changeCodeToValue("ARCGIS_DOCK_MODE", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		
		String OUTLINE_FONT_SETTINGS_BUILD = this.funConfigurationService.changeCodeToValue("OUTLINE_FONT_SETTINGS_BUILD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode());
		if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
			OUTLINE_FONT_SETTINGS = "14";
		}
		if(ARCGIS_DOCK_MODE == null || "".equals(ARCGIS_DOCK_MODE)) {
			ARCGIS_DOCK_MODE = "0";
		}
		map.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
		map.put("OUTLINE_FONT_SETTINGS_BUILD", OUTLINE_FONT_SETTINGS_BUILD);
		map.put("ARCGIS_DOCK_MODE", ARCGIS_DOCK_MODE);
		return forward;
	}
	
	/**
	 * 2015-09-25 liushi add 根据信息域查询列表
	 * @param session
	 * @param request
	 * @param map
	 * @param pageNo
	 * @param pageSize
	 * @param orgCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/buildingListData")
	public EUDGPagination buildingListData(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows,
			@RequestParam(required = false) String buildingName,
			@RequestParam(required = false) String isRelationValue
			){
		Map<String,Object> mixedGridInfo = this.getDefaultGridInfo(session);
		if(page<=0) page=1;
		if(buildingName!=null) buildingName = buildingName.trim();
		if(isRelationValue!=null) isRelationValue = isRelationValue.trim();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("infoOrgCode", mixedGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		params.put("buildingName", buildingName);
		params.put("isRelationValue", isRelationValue);
		EUDGPagination pagination = this.buildAddressService.findBuildAddressPagination(page, rows, params);
		return pagination;
	}
	
	/**
	 * 2015-09-25 liushi add 新增
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/add")
	public String add(HttpSession session, HttpServletRequest request, ModelMap map
			,@RequestParam(value="relateType") String relateType
			,@RequestParam(value="addressId", required = false) Long addressId
			,@RequestParam(value="mapt", required = false) Integer mapt
			,@RequestParam(value="x", required = false) Double x
			,@RequestParam(value="y", required = false) Double y
			,@RequestParam(value="hs", required = false) String hs){
		map.put("relateType", relateType);
		map.put("addressId", addressId);
		map.put("mapt", mapt);
		map.put("x", x);
		map.put("y", y);
		map.put("hs", hs);
		String forward = "/map/buildaddress/add_buildAddress.ftl";
		return forward;
	}
	
	
	/**
	 * 2015-09-25 liushi add 保存（包含新增跟修改功能）
	 * @param session
	 * @param request
	 * @param map
	 * @param thresholdColorCfg
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public Map<String,Object> save(HttpSession session, HttpServletRequest request, ModelMap map,
			BuildAddress buildAddress
			){
		
		boolean flag = this.buildAddressService.saveBuildAddress(buildAddress);
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("flag", flag);
		return result;
	}
}
