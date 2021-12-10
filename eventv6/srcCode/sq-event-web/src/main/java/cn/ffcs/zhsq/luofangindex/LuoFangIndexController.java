package cn.ffcs.zhsq.luofangindex;

import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

/**   
 * @Description: 罗坊首页
 * @Author: cailf
 * @Date: 12-27 14:18:18
 * @Copyright: 2017 福富软件
 */ 
@Controller("LuoFangIndexController")
@RequestMapping("/zhsq/luofangindex")
public class LuoFangIndexController extends ZZBaseController {


	@RequestMapping("safety")
	public String safety(HttpSession session,HttpServletRequest request,ModelMap map){
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
//		map.addAttribute("ATTACHMENT_TYPE_VISIT_RECORD", ConstantValue.ATTACHMENT_TYPE_VISIT_RECORD);
		map.addAttribute("gridName", defaultGridInfo.get(KEY_START_GRID_NAME));
		map.addAttribute("gridCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.put("UAMurl", App.UAM.getDomain(session));
		map.put("eventUrl", App.EVENT.getDomain(session));
		map.put("downPath", App.IMG.getDomain(session));
		String today = DateUtils.getToday();
		Map<String, Object> firstday_lastday_month = DateUtils.getFirstday_Lastday_Month(new Date(), true);

		map.addAttribute("createTimeStart", today + " 00:00:00" );
		map.addAttribute("createTimeEnd", today + " 23:59:59" );
		map.addAttribute("endTimeStart", firstday_lastday_month.get("first"));
		map.addAttribute("endTimeEnd", firstday_lastday_month.get("last"));
		return "/zzgl/luofangindex/index_safety.ftl";
	}

}
