package cn.ffcs.zhsq.dataExchange.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.common.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.utils.ConstantValue;

@Controller
@RequestMapping(value = "/zhsq/dataExchangeController")
public class DataExchangeController  extends ZZBaseController{
	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;
	
	@RequestMapping(value = "/index")
	public String index(HttpSession session, ModelMap map) {
		return "/zzgl/dataExchange/list_dataExchange.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/listData", method = RequestMethod.POST)
	public EUDGPagination listData(
			HttpSession session,
			@RequestParam(value = "gridName", required = false) String gridName,
			@RequestParam(value = "infoOrgCode", required = false) String infoOrgCode,
			@RequestParam(value = "gridId", required = false) Long gridId,
			@RequestParam(value = "drawedMaptype", required = false) String drawedMaptype,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		//paramsMap.put("gridName", gridName);
		//paramsMap.put("gridId", gridId);
		paramsMap.put("infoOrgCode", infoOrgCode);
		paramsMap.put("drawedMaptype", drawedMaptype);
		//paramsMap.put("orgCode", userInfo.getOrgCode());
//		EUDGPagination eudPagination = new EUDGPagination();
//		eudPagination = mixedGridInfoService.findMixedGridInfoPagination(page, rows, paramsMap);
		EUDGPagination eudPagination = dataExchangeStatusService.findDataExchangePagination(page, rows, paramsMap);
		
		return eudPagination;
	}
}
