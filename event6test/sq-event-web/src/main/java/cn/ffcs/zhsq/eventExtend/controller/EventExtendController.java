package cn.ffcs.zhsq.eventExtend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;

/**
 * 事件额外信息处理
 * @author zhangls
 *
 */
@Controller
@RequestMapping("/zhsq/eventExtend")
public class EventExtendController extends ZZBaseController {
	@Autowired
	private IEventExtendService eventExtendService;
	
	@Autowired
	private ITimeApplicationService timeApplicationService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	/**
	 * 跳转事件扩展信息列表
	 * @param session
	 * @param zoneCode		区域名称
	 * 			amoy		厦门环保
	 * @param params
	 * 			listType	列表类型
	 * 				1		辖区内被督察督办事件
	 * 				2		我的被督察督办事件
	 * 				3		辖区内事件
	 * 				4		我的待办列表
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toEventList")
	public String toEventList(HttpSession session, 
			@RequestParam(value = "zoneCode", required = false, defaultValue = "") String zoneCode,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		if(StringUtils.isNotBlank(zoneCode)) {
			//构建需要移除的事件状态字典
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			Map<String, Object> dictParams = new HashMap<String, Object>();
			
			dictParams.put("dictPcode", ConstantValue.STATUS_PCODE);
			dictParams.put("dictGeneralCodeList", new String[]{ConstantValue.EVENT_STATUS_DRAFT, ConstantValue.EVENT_STATUS_HAND_ON, ConstantValue.STATUS_DEL_AND_RETURN_BACK});
			dictParams.put("orgCode", userInfo.getOrgCode());
			
			List<BaseDataDict> dictList = baseDictionaryService.findDataDictListByCodes(dictParams);
			if(dictList != null) {
				StringBuffer eventStatusDictCodes4Remove = new StringBuffer("");
				
				for(BaseDataDict dict : dictList) {
					eventStatusDictCodes4Remove.append(",").append(dict.getDictCode());
				}
				
				if(eventStatusDictCodes4Remove.length() > 0) {
					map.addAttribute("eventStatusDictCodes4Remove", eventStatusDictCodes4Remove);
				}
			}
		}
		
		map.addAttribute("extraParam", params);
		
		return "/zzgl/event/eventExtend/" + zoneCode + "/list_eventEx.ftl";
	}
	
	/**
	 * 跳转到时限审核页面
	 * @param session
	 * @param eventId		事件id
	 * @param instanceId	流程实例id
	 * @param params
	 * 			applicationType	时限申请类型
	 * 				8	事件分类修改申请
	 * 				9	事件重置申请
	 * 			isAutoAudit		是否自动审核通过，true为是
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAddTimeApplication")
	public String toAddTimeApplication(HttpSession session, 
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam(value = "instanceId", required=false) Long instanceId,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String applicationTypeStr = null;
		int applicationType = 0;
		String forwardUrl = "/zzgl/timeApplication/add_timeApplication.ftl";
		
		if(CommonFunctions.isNotBlank(params, "applicationType")) {
			applicationTypeStr = params.get("applicationType").toString();
			
			try {
				applicationType = Integer.valueOf(applicationTypeStr);
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		try {
			map.addAllAttributes(timeApplicationService.initTimeApplication4Event(eventId, applicationTypeStr, userInfo, params));
		} catch (Exception e) {
			map.addAttribute("msgWrong", e.getMessage());
			e.printStackTrace();
		}
		
		switch(applicationType) {
			case 8: {
				EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
				
				if(event != null) {
					String eventType = event.getType();
					
					map.addAttribute("eventTypePre", eventType);
					map.addAttribute("eventTypePreMap", DataDictHelper.capMultilevelDictInfo(eventType, ConstantValue.BIG_TYPE_PCODE, userInfo.getOrgCode()));
				}
				
				forwardUrl = "/zzgl/timeApplication/eventTypeApplication/add_timeApplication_eventType.ftl";
				break;
			}
			case 9: {
				forwardUrl = "/zzgl/timeApplication/add_timeApplication_base.ftl";
				break;
			}
			case 10: {
				forwardUrl = "/zzgl/timeApplication/eventCheckApplication/add_app_eventCheck.ftl";
				break;
			}
		}
		
		return forwardUrl;
	}
	
	/**
	 * 加载事件扩展信息列表记录
	 * @param session
	 * @param page			页码
	 * @param rows			每页记录数
	 * @param params
	 * 			listType	列表类型
	 * 				1		辖区内被督察督办事件
	 * 				2		我的被督察督办事件
	 * 				3		辖区内事件
	 * 				4		我的待办列表
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listEventExData")
	public EUDGPagination listEventExData(HttpSession session, 
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		params.put("startInfoOrgCode", this.getDefaultInfoOrgCode(session));
		params.put("operateUserId", userInfo.getUserId());
		params.put("operateOrgId", userInfo.getOrgId());
		params.put("userOrgCode", userInfo.getOrgCode());
		
		EUDGPagination eventExPagination = eventExtendService.findEventExtendPagination(page, rows, params);
		
		return eventExPagination;
	}
}
