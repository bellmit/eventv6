package cn.ffcs.zhsq.equipment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.doorsys.bo.equipment.PpsLog;
import cn.ffcs.doorsys.service.equipment.IPpsLogService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

/**
 * @author YangCQ
 */
@Controller
@RequestMapping(value = "/zhsq/visitorMachineController")
public class VisitorMachineController extends ZZBaseController {

	@Autowired
	private IPpsLogService ppsLogService;

	@RequestMapping(value = "/index")
	public String index(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value = "bizId") String bizId) {
		map.put("bizId", bizId);
		return "/map/arcgis/standardmappage/equipment/pps/listVisitorMachine.ftl";
	}
	
	@ResponseBody
    @RequestMapping(value="/listData")
    public EUDGPagination listData(HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	String bizId = request.getParameter("bizId");
    	String visitor = request.getParameter("visitor");
    	String respName = request.getParameter("respName");
    	params.put("bizId", bizId);
    	params.put("visitor", visitor);
    	params.put("respName", respName);
    	cn.ffcs.doorsys.common.EUDGPagination p = ppsLogService.findPagination(page, rows, params);
		EUDGPagination pagination = new EUDGPagination(p.getTotal(), p.getList());
    	return pagination;
    }
	
	@RequestMapping(value = "/detail")
	public String detailRelatedEvents(HttpSession session, 
			@RequestParam(value = "logId") String logId,
			ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		PpsLog ppsLog = ppsLogService.findById(logId);
		map.addAttribute("ppsLog", ppsLog);
		map.addAttribute("IMG", App.IMG.getDomain(session));
		return "/map/arcgis/standardmappage/equipment/pps/detailLog.ftl";
	}
}
