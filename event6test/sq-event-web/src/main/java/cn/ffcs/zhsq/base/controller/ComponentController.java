package cn.ffcs.zhsq.base.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequestMapping(value="/component")
public class ComponentController {
	/**
	 * 消息客户端，解决iframe间通信的问题
	 * @param session
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="/msgClient")
	public String registerMsg(HttpSession session,ModelMap modelMap,
			@RequestParam(value = "msgTag", required = false) String msgTag,
			@RequestParam(value = "msgCenter", required = false) String msgCenter){
		modelMap.addAttribute("msgCenter", msgCenter);
		modelMap.addAttribute("msgTag", msgTag);
		return "/component/msgCenter/msgClient.ftl";
	}

}
