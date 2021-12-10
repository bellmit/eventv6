package cn.ffcs.zhsq.sanitation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/zhsq/map/truck")
public class TruckController {

	@Value("${config.bus.websocket.url}")
	private String truckWebSocketUrl;
	
	@RequestMapping("index")
	public String index(HttpServletRequest request,ModelMap model) {
		model.put("truckWebSocketUrl", truckWebSocketUrl);
		return "/map/arcgis/standardmappage/sanitation/truck_index.html";
	}
}
