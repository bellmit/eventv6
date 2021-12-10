package cn.ffcs.zhsq.drowBuilding.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.zzgl.service.grid.IGisBuildingInfoService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 楼宇轮廓编辑控制器
 * @author sulch
 * 2015年11月11日 上午9:44:41
 */
@Controller
@RequestMapping(value = "/zhsq/drowBuildingController")
public class DrowBuildingController  extends ZZBaseController{
	@Autowired
	private IGisBuildingInfoService gisBuildingInfoService;
	@Autowired
	private IFunConfigurationService funConfigurationService;//功能配置
	
	@RequestMapping(value = "/index")
	public String index(HttpSession session,
			@RequestParam(value = "isDraw", required = false) String isDraw,
			@RequestParam(value = "editMapType", required = false) String editMapType, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String defaultOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE)
				.toString();
		//获取“是否启用三维地图”的功能配置
		String isUseTwoTypesMap = "";
		isUseTwoTypesMap =  funConfigurationService.
				turnCodeToValue(ConstantValue.IS_USE_TWO_TYPES_MAP,"",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		String USE_DRAW_BUILDING_OUTLINE = this.funConfigurationService.turnCodeToValue("USE_DRAW_BUILDING_OUTLINE", "",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUseTwoTypesMap", isUseTwoTypesMap);
		map.addAttribute("defaultInfoOrgCode", defaultOrgCode);
		map.addAttribute("startGridName",
				defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("USE_DRAW_BUILDING_OUTLINE", USE_DRAW_BUILDING_OUTLINE);
		if(StringUtils.isNotBlank(isDraw)){
			map.addAttribute("isDraw", isDraw);
		}else{
			map.addAttribute("isDraw", "-1");
		}
		if(StringUtils.isNotBlank(editMapType)){
			map.addAttribute("editMapType", editMapType);
		}else{
			map.addAttribute("editMapType", "1");
		}
		return "/zzgl/drowBuilding/list_drowBuilding.ftl";
	}
	@RequestMapping(value = "/indexBinding")
	public String indexBinding(HttpSession session,
			@RequestParam(value = "isDraw", required = false) String isDraw,
			@RequestParam(value = "editMapType", required = false) String editMapType, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		String defaultOrgCode = defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE)
				.toString();
		//获取“是否启用三维地图”的功能配置
		String isUseTwoTypesMap = "";
		isUseTwoTypesMap =  funConfigurationService.
				turnCodeToValue(ConstantValue.IS_USE_TWO_TYPES_MAP,"",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.addAttribute("isUseTwoTypesMap", isUseTwoTypesMap);
		map.addAttribute("defaultInfoOrgCode", defaultOrgCode);
		map.addAttribute("startGridName",defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		if(StringUtils.isNotBlank(isDraw)){
			map.addAttribute("isDraw", isDraw);
		}else{
			map.addAttribute("isDraw", "-1");
		}
		if(StringUtils.isNotBlank(editMapType)){
			map.addAttribute("editMapType", editMapType);
		}else{
			map.addAttribute("editMapType", "1");
		}
		return "/zzgl/drowBuilding/list_drowBuilding_binding.ftl";
	}
	@ResponseBody
	@RequestMapping(value = "/listData", method = RequestMethod.POST)
	public cn.ffcs.common.EUDGPagination listData(
			HttpSession session,
			@RequestParam(value = "buildingName", required = false) String buildingName,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "gridId", required = false) Long gridId,
			@RequestParam(value = "drawedMaptype", required = false) String drawedMaptype,
			@RequestParam(value = "isMarker", required = false) String isMarker,
			@RequestParam(value = "mapt", required = false) String mapt,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("buildingName", buildingName);
		paramsMap.put("gridId", gridId);
		paramsMap.put("infoOrgCode", infoOrgCode);
		paramsMap.put("drawedMaptype", drawedMaptype);
		paramsMap.put("isMarker", isMarker);
		paramsMap.put("orgCode", userInfo.getOrgCode());
		paramsMap.put("mapt", mapt);
		paramsMap.put("orderBy", "letter2");// letter2 = buildingName asc, letter2= buildingName desc
		cn.ffcs.common.EUDGPagination eudPagination = new cn.ffcs.common.EUDGPagination();
		eudPagination = gisBuildingInfoService.findGisBuildingInfoPagination(page, rows, paramsMap);
		return eudPagination;
	}
}
