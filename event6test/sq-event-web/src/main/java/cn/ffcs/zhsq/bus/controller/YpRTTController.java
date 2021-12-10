package cn.ffcs.zhsq.bus.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.ffcs.shequ.utils.StringUtils;

/**
 * 延平实时路况控制器
 * @author zkongbai
 *
 */
@Controller
@RequestMapping("/zhsq/map/ypRTT")
public class YpRTTController {

	@RequestMapping("index")
	public String index(HttpServletRequest request,ModelMap model,
			@RequestParam(value = "x", required = false) String x,@RequestParam(value = "y", required = false) String y) {
		
		if(StringUtils.isBlank(x)) {
			model.addAttribute("x", 118.17);
		}else {
			model.addAttribute("x",x);
		}
		if(StringUtils.isBlank(y)) {
			model.addAttribute("y", 26.65);
		}else {
			model.addAttribute("y", y);
		}
		return "/map/arcgis/standardmappage/ypRTT/ypRTT_index.html";
	}
}
