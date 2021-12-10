package cn.ffcs.zhsq.map.arcgis.controller;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.ffcs.zhsq.utils.domain.App;


/**
 * 语音盒子信息 action
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/voiceInterface")
public class ArcgisEmpController {
	
	/**
	 * 获得令牌
	* @param session
	* @param bCall
	* @param userName
	* @param userImg
	* @param map
	 */
	@RequestMapping(value="/go")
	public String go(HttpSession session, @RequestParam(value="bCall", required=false) String bCall,
			@RequestParam(value="userName", required=false) String userName,
			@RequestParam(value="userImg", required=false) String userImg,
			@RequestParam(value="page", required=false) String page,
			@RequestParam(value="fromFlag", required=false) String fromFlag, ModelMap map) {
		map.addAttribute("bCall", bCall);
		if(userName!=null && userName.length()>0){
			try {
				userName = java.net.URLDecoder.decode(userName, "utf-8");
				userName = userName.trim();
			   }catch (UnsupportedEncodingException e) {}
		}		
		map.addAttribute("userName", userName);
		map.addAttribute("userImg", userImg);
		map.addAttribute("fromFlag", fromFlag==null?"":fromFlag);
		map.addAttribute("PLATFORM_DOMAIN_ROOT", App.TOP.getDomain(session));
		map.addAttribute("RESOURCE_DOMAIN", App.RESOURCE.getDomain(session));
		return "/map/arcgis/standardmappage/voiceInterface/"+(page!=null?page:"makeCallStatus")+".ftl";
	}
	
}
