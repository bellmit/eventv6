package cn.ffcs.zhsq.drugEnforcementEvent.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.drugEnforcementEvent.service.IDrugEnforcementEventService;
import cn.ffcs.zhsq.mybatis.domain.drugEnforcementEvent.DrugEnforcementEvent;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 禁毒事件对外接口
 * @author zhangls
 *
 */
@Controller
@RequestMapping(value="/zhsq/drugEnforcementEvent4Outer")
public class DrugEnforcementEvent4OuterController extends ZZBaseController {
	@Autowired
	private IDrugEnforcementEventService drugEnforcementEventService;
	
	/**
	 * 获取禁毒事件列表数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param params
	 * 			listType	列表类型 1 草稿；2 待办；3 辖区所有；默认为0
	 * @return
	 * 		total	记录总数
	 * 		list	当前分页记录数
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchList4Jsonp")
	public String fetchList4Jsonp(
			HttpSession session,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String jsonpcallback = "";
		EUDGPagination result = new EUDGPagination();
		int listType = 0;
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		switch(listType) {
			case 2: {//待办
				result = drugEnforcementEventService.findDrugEnforcementEventPagination(page, rows, userInfo, params);
				break;
			}
			case 3: {//辖区所有
				//默认地域编码
				params.put("startInfoOrgCode", this.getDefaultInfoOrgCode(session));
				
				result = drugEnforcementEventService.findDrugEnforcementEventPagination(page, rows, userInfo, params);
				break;
			}
			default: {
				result = new EUDGPagination();
				result.setRows(new ArrayList<DrugEnforcementEvent>());
				break;
			}
		}
		
		resultMap.put("total", result.getTotal());
		resultMap.put("list", result.getRows());
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
	
	/**
	 * 获取禁毒事件数量
	 * @param session
	 * @param params
	 * 			listType	列表类型 1 草稿；2 待办；3 辖区所有；默认为0
	 * @return
	 * 		total	记录总数
	 */
	@ResponseBody
	@RequestMapping(value = "/fetchCount4Jsonp")
	public String fetchCount4Jsonp(
			HttpSession session,
			@RequestParam Map<String, Object> params) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String jsonpcallback = "";
		int listType = 0, total = 0;
		
		if(CommonFunctions.isNotBlank(params, "jsonpcallback")) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		switch(listType) {
			case 2: {//待办
				total = drugEnforcementEventService.findDrugEnforcementEventCount(params, userInfo);
				break;
			}
			case 3: {//辖区所有
				//默认地域编码
				params.put("startInfoOrgCode", this.getDefaultInfoOrgCode(session));
				
				total = drugEnforcementEventService.findDrugEnforcementEventCount(params, userInfo);
				break;
			}
			default: {
				total = 0;
				break;
			}
		}
		
		resultMap.put("total", total);
		
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(resultMap);
		}
		
		return jsonpcallback;
	}
}
