package cn.ffcs.zhsq.dispute.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.checkConfig.service.ICheckConfigService;
import cn.ffcs.zhsq.dispute.service.IDisputeMediationPTService;
import cn.ffcs.zhsq.mybatis.domain.checkConfig.CheckResultInfo;
import cn.ffcs.zhsq.mybatis.domain.dispute.DisputeMediationPT;
import cn.ffcs.zhsq.utils.domain.App;

@Controller
@RequestMapping(value = "/zhsq/dispute")
public class DisputeMediationPTController extends ZZBaseController {

	@Autowired 
	private IDisputeMediationPTService disputeMediationPTService;

	@RequestMapping(value="/main/index")
	public String toMapArcgis_pt(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map){
		map.addAttribute("BI_DOMAIN", App.BI.getDomain(session));
		String type = request.getParameter("type");
		if(StringUtils.isNotBlank(type)&&type.equals("big")){
			return "/map/arcgis/arcgis_base/pingtan/arcgis_index_versionnoe_big.ftl";
		}
		return "/map/arcgis/arcgis_base/pingtan/arcgis_index_versionnoe.ftl";
	}
	
	@RequestMapping(value = "/index")
	public String index(HttpSession session, ModelMap map){
		return "/zzgl/dispute/result.ftl";
	}
	
	@ResponseBody
	@RequestMapping(value = "/topDispute")
	public List<DisputeMediationPT> topDispute(HttpSession session,
			ModelMap map){
		Map<String, Object> gridInfo = this.getDefaultGridInfo(session);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("level", gridInfo.get(KEY_START_GRID_LEVEL));
		param.put("gridPid", gridInfo.get(KEY_START_GRID_ID));
		param.put("infoOrgCode", gridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
//		gridInfo.get(KEY_START_GRID_LEVEL)
		List<DisputeMediationPT> disputes = disputeMediationPTService.findDisputeMediationPT(param);
		return disputes;
	}
	
	@ResponseBody
	@RequestMapping(value = "/wholeInfo")
	public DisputeMediationPT wholeInfo(HttpSession session,
			ModelMap map){
		Map<String, Object> gridInfo = this.getDefaultGridInfo(session);
		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("regionCode", "3502");
//		param.put("regionLevel", "2");
		param.put("regionCode", gridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
		param.put("gridId", gridInfo.get(KEY_START_GRID_ID));
		DisputeMediationPT disputes = disputeMediationPTService.getWholeInfo(param);
		return disputes;
	} 
}
