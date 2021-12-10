package cn.ffcs.zhsq.event.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposal4ExtraService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExpand.service.IEventDisposal4ExpandService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**
 * 事件额外信息获取
 * @author zhangls
 *
 */
@Controller
@RequestMapping("/zhsq/event/eventDisposal4Extra")
public class EventDisposal4ExtraController extends ZZBaseController {
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventDisposal4ExtraService eventDisposal4ExtraService;
	
	@Autowired
	private IEventDisposal4ExpandService eventDisposalExpandService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;

	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	/**
	 * 跳转评价页面
	 * @param session
	 * @param eventIdStr	事件id，多个值使用英文逗号分隔
	 * @param map
	 * @return
	 */
	@RequestMapping("/toArchive")
    public String toArchive(HttpSession session, 
    		@RequestParam(value = "eventIdStr") String eventIdStr,
    		ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		map.addAttribute("eventIdStr", eventIdStr);
		map.addAttribute("evaLevelDict", baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.EVALUATE_LEVEL_PCODE, userInfo.getOrgCode()));
		
		return "/zzgl/event/archive_event.ftl";
	}
	
	/**
	 * 批量评价事件信息
	 * @param session
	 * @param eventIdStr	事件id，多个值使用英文逗号分隔
	 * @param params
	 * 			evaLevel	评价等级
	 * 			evaContent	评价内容
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/archiveEvent4Batch")
	public ResultObj archiveEvent4Batch(HttpSession session, 
			@RequestParam(value = "eventIdStr") String eventIdStr,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;
		
		try {
			Map<String, Object> archiveMap = eventDisposal4ExtraService.archiveWorkflow4Event(eventIdStr, userInfo, params);
			int successTotal = 0;
			StringBuffer msgWrong = new StringBuffer("");
			
			if(CommonFunctions.isNotBlank(archiveMap, "successTotal")) {
				try {
					successTotal = Integer.valueOf(archiveMap.get("successTotal").toString());
				} catch(NumberFormatException e) {}
			}
			
			if(successTotal > 0) {
				msgWrong.append("您已成功评价").append(successTotal).append("条事件！");
			} else {
				msgWrong.append("评价操作失败！");
			}
			
			if(CommonFunctions.isNotBlank(archiveMap, "msgWrong")) {
				msgWrong.append("<br/>")
						.append("<span style='cursor: pointer; color: red;' onclick='alert($(this).next().html());return false;'>查看错误信息</span>")
						.append("<div style='display:none;'>")
						.append(archiveMap.get("msgWrong"))
						.append("</div>");
			}
			
			resultObj = Msg.OPERATE.getResult(successTotal > 0, msgWrong.toString());
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(e);
		}
		
		return resultObj;
	}
	
	/**
	 * 跳转批量办理事件页面
	 * @param session
	 * @param eventIdStr	事件id，多个值使用英文逗号分隔
	 * @param params
	 * 			nextNodeName	下一环节名称
	 * @param map
	 * @return
	 */
	@RequestMapping("/toSubEvent")
    public String toSubEvent(HttpSession session, 
    		@RequestParam(value = "eventIdStr") String eventIdStr,
    		@RequestParam Map<String, Object> params,
    		ModelMap map){
		map.addAllAttributes(params);
		
		return "/zzgl/event/eventExtra/sub_event_batch.ftl";
	}
	
	/**
	 * 提交事件到指定下一环节
	 * @param session
	 * @param eventIdStr	事件id，多个值使用英文逗号进行分隔
	 * @param params
	 * 			advice		办理意见
	 * 			nextNodeName下一环节名称
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/subEvent4Batch")
	public ResultObj subEvent4Batch(HttpSession session, 
			@RequestParam(value = "eventIdStr") String eventIdStr,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;
		
		try {
			Map<String, Object> subEventMap = eventDisposal4ExtraService.subWorkflow2AppointedTask(params, userInfo);
			int successTotal = 0, failTotal = 0;
			StringBuffer message = new StringBuffer("");
			
			if(CommonFunctions.isNotBlank(subEventMap, "successTotal")) {
				try {
					successTotal = Integer.valueOf(subEventMap.get("successTotal").toString());
				} catch(NumberFormatException e) {}
			}
			
			if(CommonFunctions.isNotBlank(subEventMap, "failTotal")) {
				try {
					failTotal = Integer.valueOf(subEventMap.get("failTotal").toString());
				} catch(NumberFormatException e) {}
			}
			
			if(successTotal > 0) {
				message.append("您已成功提交").append(successTotal).append("条事件！");
			}
			
			if(failTotal > 0) {
				message.append("您有").append(failTotal).append("条事件提交失败！");
			}
			
			if(CommonFunctions.isNotBlank(subEventMap, "msgWrong")) {
				message.append("<br/>")
					   .append("<span style='cursor: pointer; color: red;' onclick='alert($(this).next().html());return false;'>查看错误信息</span>")
					   .append("<div style='display:none;'>")
					   .append(subEventMap.get("msgWrong"))
					   .append("</div>");
			}
			
			resultObj = Msg.OPERATE.getResult(successTotal > 0, message.toString());
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(e);
		}
		
		return resultObj;
	}
	
	/**
	 * 获取用于事件对接、对外等使用的信息
	 * @param session
	 * @param eventId	 事件id
	 * @param params	额外属性
	 * 					isCapAttachment	是否获取附件信息，true为获取
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchEventInfo4Dock", method = RequestMethod.POST)
	public Map<String, Object> fetchEventInfo4Dock (
			HttpSession session,
			@RequestParam(value = "eventId") Long eventId,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> eventMap = new HashMap<String, Object>();
		
		try {
			eventMap = eventDisposal4ExtraService.fetchEventInfo4Dock(eventId, userInfo, params);
		} catch (Exception e) {
			eventMap.put("msgWrong", e.getMessage());
			e.printStackTrace();
		}
		
		return eventMap;
	}
	
	/**
	 * 跳转需要评价操作的归档事件列表
	 * @param session
	 * @param params
	 * 			evaType		评价操作类型，1 镇级评价；2 区级复评；
	 * @param map
	 * @return
	 */
	@RequestMapping("/toListEventEva")
    public String toListEventEva(HttpSession session, 
    		@RequestParam Map<String, Object> params,
    		ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean isEnableDefaultCreatTime = true;
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		//获取网格树展示位置配置
		String treStyle= funConfigurationService.changeCodeToValue(
						ConstantValue.GRID_CONFIGURATION, ConstantValue.GRID, IFunConfigurationService.CFG_TYPE_FACT_VAL,
						userInfo.getOrgCode());
				
		if(CommonFunctions.isNotBlank(params, "isEnableDefaultCreatTime")) {
			isEnableDefaultCreatTime = Boolean.valueOf(params.get("isEnableDefaultCreatTime").toString());
		}
		
		if(isEnableDefaultCreatTime) {
			if(CommonFunctions.isBlank(params, "createDayStart")) {
				params.put("createDayStart", DateUtils.getMonthFirstDay());
			}
			if(CommonFunctions.isBlank(params, "createDayEnd")) {
				params.put("createDayEnd", DateUtils.getToday());
			}
		}
		
		map.addAttribute("treStyle", treStyle);
		
		//网格组件初始化时，需要使用非加密的根节点地域编码
		map.addAttribute("defaultInfoOrgCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		map.addAttribute("infoOrgCode", defaultGridInfo.get(KEY_DEFAULT_ENCRYPT_INFO_ORG_CODE));
		//网格名称，用于重置操作，回到默认根节点
		map.addAttribute("gridName", defaultGridInfo.get(KEY_START_GRID_NAME));
		
		map.addAllAttributes(params);
		
		return "/zzgl/event/eventExtra/eventEva/list_event_eva.ftl";
	}
	
	/**
	 * 加载需要评价操作的归档事件列表
	 * @param session
	 * @param page
	 * @param rows
	 * @param params
	 * 			evaType		评价操作类型，1 镇级评价；2 区级复评；
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listEventEvaData", method = RequestMethod.POST)
	public EUDGPagination listEventEvaData (
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam Map<String, Object> params) {
		params.put("startInfoOrgCode", this.getDefaultInfoOrgCode(session));
		
		return eventDisposal4ExtraService.findJurisdictionEvent4EvaPagination(page, rows, params);
	}
	
	/**
	 * 跳转事件评价操作页面
	 * @param session
	 * @param eventIdStr	事件id，多个值使用英文逗号进行分隔
	 * @param params
	 * 			evaObj		评价对象类型
	 * 			evaType		评价操作类型，1 镇级评价；2 区级复评；
	 * @param map
	 * @return
	 */
	@RequestMapping("/toEvaluateEvent")
    public String toEvaluateEvent(HttpSession session, 
    		@RequestParam(value = "eventIdStr") String eventIdStr,
    		@RequestParam Map<String, Object> params,
    		ModelMap map) {
		List<Map<String, Object>> evaLevelDictList = new ArrayList<Map<String, Object>>();
		Map<String, Object> evaLevalDidt = null;
		
		for(int index = 1; index < 6; index++) {
			evaLevalDidt = new HashMap<String, Object>();
			
			evaLevalDidt.put("dictId", index);
			evaLevalDidt.put("dictName", index + "星");
			evaLevalDidt.put("dictGeneralCode", index);
			
			evaLevelDictList.add(evaLevalDidt);
		}
		
		map.addAttribute("evaLevelDict", evaLevelDictList);
		map.addAttribute("eventIdStr", eventIdStr);
		map.addAllAttributes(params);
		
		return "/zzgl/event/evaluate_event.ftl";
	}
	
	/**
	 * 批量评价操作
	 * @param session
	 * @param eventIdStr	事件id，多个值使用英文逗号进行分隔
	 * @param params
	 * 			evaType		评价操作类型，1 镇级评价；2 区级复评；
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/evaluateEvent4Batch")
	public ResultObj evaluateEvent4Batch(HttpSession session, 
			@RequestParam(value = "eventIdStr") String eventIdStr,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		ResultObj resultObj = null;
		
		try {
			Map<String, Object> archiveMap = eventDisposal4ExtraService.evaEvent4Batch(eventIdStr, userInfo, params);
			int successTotal = 0;
			StringBuffer msgWrong = new StringBuffer("");
			
			if(CommonFunctions.isNotBlank(archiveMap, "successTotal")) {
				try {
					successTotal = Integer.valueOf(archiveMap.get("successTotal").toString());
				} catch(NumberFormatException e) {}
			}
			
			if(successTotal > 0) {
				int total = 0;
				
				if(CommonFunctions.isNotBlank(archiveMap, "total")) {
					try {
						total = Integer.valueOf(archiveMap.get("total").toString());
					} catch(NumberFormatException e) {}
				}
				
				if(total == 1) {
					msgWrong.append("评价操作成功！");
				} else {
					msgWrong.append("您共操作").append(total).append("条事件，其中成功评价").append(successTotal).append("条事件！");;
				}
			} else {
				msgWrong.append("评价操作失败！");
			}
			
			if(CommonFunctions.isNotBlank(archiveMap, "msgWrong")) {
				msgWrong.append("<br/>")
						.append("<span style='cursor: pointer; color: red;' onclick='alert($(this).next().html());return false;'>查看错误信息</span>")
						.append("<div style='display:none;'>")
						.append(archiveMap.get("msgWrong"))
						.append("</div>");
			}
			
			resultObj = Msg.OPERATE.getResult(successTotal > 0, msgWrong.toString());
		} catch(ZhsqEventException e) {
			resultObj = Msg.OPERATE.getResult(false, e.getMessage());
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(e);
		}
		
		return resultObj;
	}
	
	/**
	 * 异步短信发送
	 * @param session
	 * @param smsReceiverIds	短信接收人员id，多个值使用英文逗号分隔
	 * @param otherMobiles		短信接收手机号码，多个值使用英文逗号分隔
	 * @param smsContent		短信内容
	 * @param params
	 * @param map
	 */
	@ResponseBody
	@RequestMapping(value = "/sendSmsContent")
	public void sendSmsContent(HttpSession session, 
			@RequestParam(value = "smsReceiverIds", required = false) String smsReceiverIds,
			@RequestParam(value = "otherMobiles", required = false) String otherMobiles,
			@RequestParam(value = "smsContent") String smsContent,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		eventDisposalWorkflowService.sendSms(smsReceiverIds, otherMobiles, smsContent, userInfo, params);
	}
	
	/**
	 * 获取预案人员配置信息
	 * @param session
	 * @param planType	预案类型
	 * @param planLevel	预案等级
	 * @param params
	 * @param map
	 * @return
	 * 		planConfigStaff		人员信息
	 * 			userType		用户类型，1 牵头领导；2 配合领导；3 主办科室；4 配合科室；
     * 			planConfigId	预案配置id
     * 			configStaff		配置人员信息，类型为List<Map<String, Object>>
     * 				userId		用户id
     * 				userName	用户姓名
     * 				orgId		组织id
     * 				orgName		组织名称
	 * 		msgWrong			异常信息
	 */
	@ResponseBody
	@RequestMapping(value = "/capPlanConfigStaff")
	public Map<String, Object> capPlanConfigStaff(HttpSession session, 
			@RequestParam(value = "planType") String planType,
			@RequestParam(value = "planLevel") String planLevel,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		List<Map<String, Object>> planConfigStaff = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			planConfigStaff = eventDisposalExpandService.capPlanConfigStaff(planType, planLevel, userInfo, params);
			
			if(planConfigStaff != null) {
				resultMap.put("planConfigStaff", planConfigStaff);
			}
		} catch (Exception e) {
			resultMap.put("msgWrong", e.getMessage());
			e.printStackTrace();
		}
		
		return resultMap;
	}
}
