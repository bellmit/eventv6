package cn.ffcs.zhsq.timeApplication.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

/**
 * 时限申请
 * @author zhangls
 *
 */
@Controller
@RequestMapping("/zhsq/timeApplication")
public class TimeApplicationController extends ZZBaseController {
	@Autowired
	private ITimeApplicationService timeApplicationService;
	
	@Autowired
	private ITimeApplicationCheckService timeApplicationCheckService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	/**
	 * 跳转事件延时申请列表页面
	 * @param session
	 * @param params
	 * 			listType	列表类型；
	 * 				1 我的审核列表；
	 * 				2 我的申请列表；
	 * 				3 事件删除审核列表；
	 * 				31 事件督查审核列表；
	 * 				32 事件挂起审核列表；
	 * 				33 事件无效审核列表；
	 * 				4 事件删除列表；
	 * 				41 事件督查列表；
	 * 				42 事件挂起列表；
	 * 				43 事件无效列表；
	 * 				5 事件预处理审核列表；
	 * 				6 事件核验不通过列表；
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toList4Event")
	public String toList4Event(HttpSession session, 
			@RequestParam(value = "listType", required = false, defaultValue = "0") Integer listType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		Map<String, String> applicationTypeMap = new HashMap<String, String>();
		String forwardPath = "/zzgl/timeApplication/list_timeApp_event.ftl";
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String applicationTypeStr = null;
		StringBuffer applicationTypeBuffer = new StringBuffer("");
		
		//只有我的申请、我的审核列表才支持审核类型的裁剪
		if(listType == 1 || listType == 2) {
			if(CommonFunctions.isNotBlank(params, "applicationType")) {
				applicationTypeStr = params.get("applicationType").toString();
			} else {
				applicationTypeStr = funConfigurationService.turnCodeToValue(ConstantValue.TIME_APPLICATION_ATTRIBUTE, ConstantValue.LIST_APPLICATION_TYPE + listType, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			}
			
			for(ITimeApplicationService.APPLICATION_TYPE applicationType : ITimeApplicationService.APPLICATION_TYPE.values()) {
				applicationTypeMap.put(applicationType.getValue(), applicationType.getName());
				applicationTypeBuffer.append(",").append(applicationType.getValue());
			}
			
			if(StringUtils.isNotBlank(applicationTypeStr)) {
				String[] applicationTypeArray = applicationTypeStr.split(",");
				Map<String, String> appTypeMap = new HashMap<String, String>();
				applicationTypeBuffer.setLength(0);
				
				for(String appType : applicationTypeArray) {
					if(applicationTypeMap.containsKey(appType)) {
						appTypeMap.put(appType, applicationTypeMap.get(appType));
						applicationTypeBuffer.append(",").append(appType);
					}
				}
				
				if(!appTypeMap.isEmpty()) {
					applicationTypeMap = appTypeMap;
				}
			}
			
			if(applicationTypeBuffer.length() > 0) {
				map.addAttribute("applicationType", applicationTypeBuffer.substring(1));
			}
			
			if(!applicationTypeMap.isEmpty()) {
				map.addAttribute("applicationTypeMap", applicationTypeMap);
			}
		}
		
		//是否设置默认查询采集时间近一个月的事件
		if(CommonFunctions.isNotBlank(params, "isEnableDefaultCreatTime")) {
			boolean isEnableDefaultCreatTime = Boolean.valueOf(params.get("isEnableDefaultCreatTime").toString());
			
			if(isEnableDefaultCreatTime) {
				if(CommonFunctions.isBlank(params, "eventCreateTimeStart")) {
					params.put("eventCreateTimeStart", DateUtils.getMonthFirstDay());
				}
				if(CommonFunctions.isBlank(params, "eventCreateTimeEnd")) {
					params.put("eventCreateTimeEnd", DateUtils.getToday());
				}
			}
		}
		
		map.addAllAttributes(params);
		
		switch(listType) {
			case 3: {
				forwardPath = "/zzgl/timeApplication/delEventApplication/list_app_delEvent_audit.ftl";
				break;
			}
			case 31: {
				forwardPath = "/zzgl/timeApplication/inspectApplication/list_app_delEvent_audit.ftl";
				break;
			}
			case 32: {
				forwardPath = "/zzgl/timeApplication/suspendApplication/list_app_delEvent_audit.ftl";
				break;
			}
			case 33: {
				forwardPath = "/zzgl/timeApplication/invalidApplication/list_app_delEvent_audit.ftl";
				break;
			}
			case 4: {
				forwardPath = "/zzgl/timeApplication/delEventApplication/list_app_delEvent.ftl";
				break;
			}
			case 41: {
				forwardPath = "/zzgl/timeApplication/inspectApplication/list_app_delEvent.ftl";
				break;
			}
			case 42: {
				forwardPath = "/zzgl/timeApplication/suspendApplication/list_app_delEvent.ftl";
				break;
			}
			case 43: {
				forwardPath = "/zzgl/timeApplication/invalidApplication/list_app_delEvent.ftl";
				break;
			}
			case 5: {
				forwardPath = "/zzgl/timeApplication/prepressEventApplication/list_app_prepressEvent_audit.ftl";
				break;
			}
			case 6: {
				forwardPath = "/zzgl/timeApplication/eventCheckApplication/list_app_eventCheck_audit.ftl";
				break;
			}
		}
		
		return forwardPath;
	}
	
	/**
	 * 跳转时限审核详情页面
	 * @param session
	 * @param applicationId	时限申请id
	 * @param params
	 * 			listType	列表类型；1 我的审核列表；2 我的申请列表；
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toDetail")
	public String toDetail(HttpSession session, 
			@RequestParam(value = "applicationId") Long applicationId, 
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		Map<String, Object> timeApp = null;
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		if(applicationId > 0) {
			Map<String, Object> appParams = new HashMap<String, Object>();
			appParams.put("applicationId", applicationId);
			appParams.put("userOrgCode", userInfo.getOrgCode());
			appParams.put("isTimeAppCheckNeeded", true);
			appParams.put("isTransferUser", true);
			appParams.put("isTransferOrg", true);
			appParams.put("isTransferDict", true);
			
			timeApp = timeApplicationService.findTimeAppliationById(applicationId, appParams);
		}
		
		if(timeApp == null) {
			timeApp = new HashMap<String, Object>();
		} else {
			int timeAppCount = 0;
			String timeAppCheckStatus = null;
			
			if(CommonFunctions.isNotBlank(timeApp, "timeAppCheckStatus")) {
				timeAppCheckStatus = timeApp.get("timeAppCheckStatus").toString();
			}
			
			if(ITimeApplicationCheckService.CHECK_STATUS.STATUS_INIT.getValue().equals(timeAppCheckStatus)) {
				Map<String, Object> timeAppParam = new HashMap<String, Object>();
				
				if(CommonFunctions.isNotBlank(timeApp, "businessKeyId")) {
					timeAppParam.put("businessKeyId", timeApp.get("businessKeyId"));
				}
				if(CommonFunctions.isNotBlank(timeApp, "applicationType")) {
					timeAppParam.put("applicationType", timeApp.get("applicationType"));
				}
				
				if(!timeAppParam.isEmpty()) {
					timeAppCount = timeApplicationService.findTimeApplicationCount(timeAppParam);
				}
			}
			
			map.addAttribute("timeAppCount", timeAppCount);
		}
		
		map.addAttribute("timeApp", timeApp);
		map.addAllAttributes(params);
		
		return "/zzgl/timeApplication/detail_timeApplication.ftl";
	}
	
	/**
	 * 跳转申请审核页面
	 * @param session
	 * @param applicationId	申请id
	 * @param listType		事件列表
	 * 				3 事件删除审核
	 * 				5 事件预处理审核列表；
	 * @param params
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toAudit")
	public String toAudit(HttpSession session, 
			@RequestParam(value = "applicationId") Long applicationId, 
			@RequestParam(value = "listType", required = false, defaultValue = "0") Integer listType,
			@RequestParam Map<String, Object> params,
			ModelMap map) {
		String forwardPath = "/zzgl/timeApplication/audit_timeApplication_base.ftl";
		Map<String, Object> timeApp = null;
		
		if(applicationId > 0) {
			Map<String, Object> appParams = new HashMap<String, Object>();
			appParams.put("applicationId", applicationId);
			appParams.put("isTimeAppCheckNeeded", true);
			
			timeApp = timeApplicationService.findTimeAppliationById(applicationId, appParams);
		}
		
		if(timeApp == null) {
			timeApp = new HashMap<String, Object>();
		}
		
		switch(listType) {
			case 3: {
				forwardPath = "/zzgl/timeApplication/delEventApplication/audit_app_delEvent.ftl";
				break;
			}
			case 31: {
				forwardPath = "/zzgl/timeApplication/inspectApplication/audit_app_delEvent.ftl";
				break;
			}
			case 32: {
				forwardPath = "/zzgl/timeApplication/suspendApplication/audit_app_delEvent.ftl";
				break;
			}
			case 33: {
				forwardPath = "/zzgl/timeApplication/invalidApplication/audit_app_delEvent.ftl";
				break;
			}
			case 5: {
				forwardPath = "/zzgl/timeApplication/prepressEventApplication/audit_app_prepressEvent.ftl";
				break;
			}
		}
		
		map.addAttribute("timeApp", timeApp);
		map.addAllAttributes(params);
		
		return forwardPath;
	}
	
	/**
	 * 新增/编辑时限申请
	 * @param session
	 * @param timeApplication
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveTimeApplication")
	public ResultObj saveTimeApplication(HttpSession session, 
			@RequestParam Map<String, Object> timeApplication,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean result = false;
		ResultObj resultObj = null;
		
		timeApplication.put("userOrgCode", userInfo.getOrgCode());
		
		try {
			if(CommonFunctions.isNotBlank(timeApplication, "applicationId")) {
				timeApplication.put("updaterId", userInfo.getUserId());
				result = timeApplicationService.updateTimeApplicationById(timeApplication);
			} else {//新增信息
				timeApplication.put("creatorId", userInfo.getUserId());
				result = timeApplicationService.saveTimeApplication(timeApplication) > 0;
			}
			
			//使用“操作”这种没有明确指向性的提示
			resultObj = Msg.OPERATE.getResult(result);
			
			//设置默认提示信息
			if(CommonFunctions.isNotBlank(timeApplication, "defaultTipMsg")) {
				resultObj.setTipMsg(timeApplication.get("defaultTipMsg").toString());
			}
			
		} catch(Exception e) {
			resultObj = Msg.OPERATE.getResult(e);
			resultObj.setTipMsg(e.getMessage());
			e.printStackTrace();
		}
		
		return resultObj;
	}
	
	/**
	 * 审核时限申请
	 * @param session
	 * @param timeAppCheck
	 * 			checkId				审核记录id
	 * 			checkAdvice			审核意见
	 * 			timeAppCheckStatus	审核状态
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/auditTimeApplication")
	public ResultObj auditTimeApplication(HttpSession session, 
			@RequestParam Map<String, Object> timeAppCheck,
			ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean result = false;
		ResultObj resultObj = null;
		timeAppCheck.put("updaterId", userInfo.getUserId());
		timeAppCheck.put("updaterName", userInfo.getPartyName());
		timeAppCheck.put("updaterOrgId", userInfo.getOrgId());
		timeAppCheck.put("updaterOrgName", userInfo.getOrgName());
		timeAppCheck.put("userOrgCode", userInfo.getOrgCode());
		
		try {
			result = timeApplicationCheckService.updateTimeAppCheckById(timeAppCheck);
			
			resultObj = Msg.OPERATE.getResult(result);
		} catch (Exception e) {
			resultObj = Msg.OPERATE.getResult(e);
			
			e.printStackTrace();
		}
		
		return resultObj;
	}
	
	/**
	 * 获取事件延时申请列表
	 * @param session
	 * @param page
	 * @param rows
	 * @param params
	 * 			listType	列表类型
	 * 				1 我的审核列表；
	 * 				2 我的申请列表；
	 * 				3 事件删除审核列表；
	 * 				4 事件删除列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listData4Event")
	public EUDGPagination listData4Event(HttpSession session, 
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, 
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		if(CommonFunctions.isBlank(params, "operateUserId")) {
			params.put("operateUserId", userInfo.getUserId());
		}
		if(CommonFunctions.isBlank(params, "operateUserOrgId")) {
			params.put("operateUserOrgId", userInfo.getOrgId());
		}
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("startInfoOrgCode", this.getDefaultInfoOrgCode(session));
		}
		params.put("userOrgCode", userInfo.getOrgCode());
		
		EUDGPagination eventTimeAppPagination = timeApplicationService.findEventTimeAppPagination(page, rows, params);
		
		return eventTimeAppPagination;
	}
	
}
