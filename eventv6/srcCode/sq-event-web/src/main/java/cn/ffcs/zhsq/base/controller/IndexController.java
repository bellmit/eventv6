/**
 * 
 */
package cn.ffcs.zhsq.base.controller;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 首页
 * @author zhongshm
 *
 */
@Controller
@RequestMapping(value="/system")
public class IndexController extends ZZBaseController{

	//--总入口------------------
	@RequestMapping(value="/index")
	public String index(HttpSession session, ModelMap map) {
//		@SuppressWarnings("unchecked")
//		List<Module> moduleList = (List<Module>) session.getAttribute(ConstantValue.MODULELIST_IN_SESSION);
//		if(moduleList==null) {
//			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
//			moduleList = sysModuleService.getModuleListByUserId(userInfo.getUserId());
//			session.setAttribute(ConstantValue.MODULELIST_IN_SESSION, moduleList);
//		}
//		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
//		List<Module> moduleList = sysModuleService.getModuleListByUserId(userInfo.getUserId());
//		session.setAttribute(ConstantValue.MODULELIST_IN_SESSION, moduleList);
		return "/frame/index.ftl";
	}
	@RequestMapping(value="/left")
	public String left(HttpSession session, ModelMap map) {
		return "/frame/left.ftl";
	}
	@RequestMapping(value="/switch")
	public String switchhtm(HttpSession session, ModelMap map) {
		return "/frame/switch.ftl";
	}
	@RequestMapping(value="/top")
	public String top(HttpSession session, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		map.addAttribute("userInfo", userInfo);
		map.addAttribute("version", "v6.0.065 20210610");
		return "/frame/top.ftl";
	}
	
	//--网格入口----------------
	/**
	 * 网格入口
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/admin/index")
	public String gridIndex(HttpSession session, ModelMap map) {
		return "/grid/frame/index.ftl";
	}
	
	@RequestMapping(value="/admin/frame/mid")
	public String mid(HttpSession session, ModelMap map) {
		return "/grid/frame/mid.ftl";
	}
	
	/**
	 * 网格入口
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/admin/indexForShow")
	public String gridIndexForShow(HttpSession session, ModelMap map) {
		return "/grid/frame/indexForShow.ftl";
	}
	
}
