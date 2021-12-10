package cn.ffcs.zhsq.base.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.uam.bo.FunConfigureSetting;
import cn.ffcs.uam.service.IFunConfigurationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.ffcs.zhsq.utils.domain.App;

@Controller
@RequestMapping(value="/zhsq/showImage")
public class ShowImageController {
	@Autowired
	private IFunConfigurationService funConfigurationService;
	/**
	 * 通过传入的图片路径数组/字符串，查看图片
	 * @param session
	 * @param paths
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/indexOfPaths")
	public String indexOfPaths(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "paths",required = false) String paths,
			@RequestParam(value = "title",required = false) String title,
			@RequestParam(value = "fieldId",required = false) Long fieldId,
			ModelMap map){
		if(StringUtils.isNotBlank(paths)){
			map.put("paths", paths);
		}
		if(StringUtils.isBlank(title)){
			title = "";
		}
		map.put("title", title);
		if(fieldId!=null){
			map.put("fieldId", fieldId);
		}
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("ANOLE_COMPONENT_URL", App.SYSTEM.getDomain(session));
		return "/zzgl/showImages.ftl";
	}
	
	@RequestMapping(value="/indexOfPath")
	public String indexOfPath(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "paths",required = false) String paths,
			@RequestParam(value = "titles",required = false) String titles,
			@RequestParam(value = "fieldId",required = false) String fieldId,
			@RequestParam(value = "index",required = false) String index,
			@RequestParam(value = "drag",required = false) String drag,
			@RequestParam(value = "background",required = false) String background,
			@RequestParam Map<String, Object> params,
			ModelMap map){
		//UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.putAll(params);
		map.put("paths", paths);
		map.put("titles", titles);
		map.put("index", index);
		map.put("drag", drag);
		map.put("background", background);
		if(fieldId!=null){
			map.put("fieldId", fieldId);
		}
		FunConfigureSetting UISetting = funConfigurationService.findConfigureSettingLatest("APP_DOMAIN", "$UI_DOMAIN", IFunConfigurationService.CFG_TYPE_URL, IFunConfigurationService.DEFAULT_ORG_CODE, IFunConfigurationService.DIRECTION_PARALLEL);
		FunConfigureSetting SYSTEMSetting = funConfigurationService.findConfigureSettingLatest("APP_DOMAIN", "$SYSTEM_DOMAIN", IFunConfigurationService.CFG_TYPE_URL, IFunConfigurationService.DEFAULT_ORG_CODE, IFunConfigurationService.DIRECTION_PARALLEL);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("ANOLE_COMPONENT_URL", StringUtils.isNotBlank(App.SYSTEM.getDomain(session))?App.SYSTEM.getDomain(session):SYSTEMSetting.getCfgVal());
		map.put("UI_URL", StringUtils.isNotBlank(App.UI.getDomain(session))?App.UI.getDomain(session):UISetting.getCfgVal());
		return "/zzgl/showImage.ftl";
	}
	
	/**
	 * 通过传入的“获取图片服务地址”获取图片
	 * @param session
	 * @param url
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/indexOfUrl")
	public String indexOfUrl(HttpSession session, 
			@RequestParam(value = "url",required = false) String url,
			ModelMap map){
		
		return null;
	}
}
