package cn.ffcs.zhsq.timeApplication.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 对外提供事件时限申请数据
 * @ClassName:   TimeApplication4OuterController   
 * @author:      张联松(zhangls)
 * @date:        2021年6月21日 下午3:04:42
 */
@Controller
@RequestMapping("/zhsq/timeApp4Outer")
public class TimeApplication4OuterController extends ZZBaseController {
	@Autowired
	private ITimeApplicationService timeApplicationService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	/**
	 * 获取事件相关审核记录数量
	 * @param session
	 * @param listType
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchEventTimeAppCount4Jsonp")
	public String fetchDataCount4Jsonp(
			HttpSession session,
			@RequestParam(value = "listType", required = false, defaultValue = "0") Integer listType,
			@RequestParam Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String jsonpcallback = "";
		int total = 0;
		
		fetchDefaultValue(session, listType, params);
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		
		try {
			total = timeApplicationService.findEventTimeAppCount(params);
		} catch (Exception e) {
			resultMap.put("msgWrong", e.getMessage());
			e.printStackTrace();
		}
		
		resultMap.put("total", total);
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	private void fetchDefaultValue(HttpSession session, Integer listType, Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(CommonFunctions.isBlank(params, "operateUserId")) {
			params.put("operateUserId", userInfo.getUserId());
		}
		if(CommonFunctions.isBlank(params, "operateUserOrgId")) {
			params.put("operateUserOrgId", userInfo.getOrgId());
		}
		if(CommonFunctions.isBlank(params, "infoOrgCode")) {
			params.put("startInfoOrgCode", this.getDefaultInfoOrgCode(session));
		}
		
		//只有我的申请、我的审核列表才支持审核类型的裁剪
		if(listType == 1 || listType == 2) {
			String applicationTypeStr = null;
			
			if(CommonFunctions.isNotBlank(params, "applicationType")) {
				applicationTypeStr = params.get("applicationType").toString();
			} else {
				applicationTypeStr = funConfigurationService.turnCodeToValue(ConstantValue.TIME_APPLICATION_ATTRIBUTE, ConstantValue.LIST_APPLICATION_TYPE + listType, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			}
			
			params.put("applicationType", applicationTypeStr);
		}
		
		params.put("userOrgCode", userInfo.getOrgCode());
	}
}
