package cn.ffcs.zhsq.map.arcgis.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IGisStatConfigService;
import cn.ffcs.zhsq.map.arcgis.service.IGisStatContConfigService;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.utils.ConstantValue;

@Controller
@RequestMapping(value = "/zhsq/map/gisStatContConfig")
public class GisStatContConfigController extends ZZBaseController {

	@Autowired
	private IGisStatConfigService gisStatConfigService;

	@Autowired
	private IGisStatContConfigService gisStatContConfigService;

	@Autowired
	private IMenuConfigService menuConfigService;

	@Autowired
	private OrgEntityInfoOutService orgEntityInfoOutService;

	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Map<String, Object> save(HttpSession session, HttpServletRequest request, ModelMap map) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		boolean result = false;

		String statObjName = request.getParameter("statObjName");// 统计对象
		String objClass = request.getParameter("objClass");// 所属类别
		String layCfgId = request.getParameter("layCfgId"); // 图层id


		Map<String, Object> results = new HashMap<String, Object>();
		results.put("result", result);
		return results;
	}
}
