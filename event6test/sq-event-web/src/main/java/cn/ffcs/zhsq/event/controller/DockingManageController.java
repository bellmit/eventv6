package cn.ffcs.zhsq.event.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDockingTaskService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.ConstantValue;

@Controller
@RequestMapping("/zhsq/dockingmanage")
public class DockingManageController extends ZZBaseController {
	@Autowired
	private IEventDockingTaskService eventDockingManageServImpl;
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest req,
			HttpSession session, 
			ModelMap map) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		map.addAttribute("startGridId", defaultGridInfo.get(KEY_START_GRID_ID));
		map.addAttribute("startGridName", defaultGridInfo.get(KEY_START_GRID_NAME));
        return "/zzgl/DockingManage/index.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/listData", method = RequestMethod.POST)
	public List<Map<String,Object>> listData(
			HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "keyWord", required = false) String keyWord) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> params = new HashMap<String, Object>();
		if(!StringUtils.isBlank(keyWord)){
			keyWord = keyWord.trim();
			params.put("keyWord", keyWord);
		}
		params.put("userIds", userInfo.getUserId());
		params.put("groupIds", userInfo.getOrgId());
		params.put("destPlatform", request.getParameter("bizPlatform"));
//		params.put("orgCode", userInfo.getOrgCode());
		return eventDockingManageServImpl.findDataExchangeEvent(params);
	}
	
	@ResponseBody
	@RequestMapping(value = "/report", method = RequestMethod.POST)
	public Long report(HttpServletRequest request,HttpSession session){
		String eventId = request.getParameter("eventId");
		EventDisposal event = eventDisposalService.findEventById(Long.valueOf(eventId), null);
		Long result = eventDockingManageServImpl.report(event);
		return result;
	}

	@RequestMapping(value = "/sub/index")
	public String sub_index(HttpServletRequest req,HttpSession session, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String eventId = req.getParameter("eventId");
		String instanceId = eventDisposalWorkflowService.capInstanceIdByEventId(Long.valueOf(eventId));
		Long taskId = eventDisposalWorkflowService.curNodeTaskId(Long.valueOf(instanceId));
		List<Map<String, Object>> queryTaskList = eventDisposalWorkflowService.queryProInstanceDetail(Long.valueOf(instanceId), IEventDisposalWorkflowService.SQL_ORDER_DESC, userInfo);
		map.addAttribute("taskList", queryTaskList);
		
        return "/zzgl/DockingManage/sub/index.ftl";
	}
}
