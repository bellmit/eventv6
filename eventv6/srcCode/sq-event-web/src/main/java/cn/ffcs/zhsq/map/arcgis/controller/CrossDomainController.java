package cn.ffcs.zhsq.map.arcgis.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.map.coordinateconversion.service.IBaseConversionService;
import cn.ffcs.zhsq.map.coordinateconversion.service.IConversionFormulaService;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisConfigInfo;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisMapConfigInfo;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 2014-05-16 liushi add
 * arcgis地图加载控制器
 * @author liushi
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/crossDomain")
public class CrossDomainController extends ZZBaseController {
	

	@RequestMapping(value="/toCrossDomainFuzhou")
	public String toCrossDomainFuzhou(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="eventResultsId", required=false) String eventResultsId) throws Exception{
		String forward = "/map/arcgis/arcgis_base/arcgis_cross_domain/cross_domain_fuzhou_event.ftl";
		map.put("eventResultsId", eventResultsId);
		return forward;
	}
	@RequestMapping(value="/toCrossDomainFuzhouEventClick")
	public String toCrossDomainFuzhouEventClick(HttpSession session, HttpServletRequest request, ModelMap map,
			@RequestParam(value="tid", required=false) String tid
			,@RequestParam(value="eventId", required=false) String eventId
			,@RequestParam(value="type", required=false) String type) throws Exception{
		String forward = "/map/arcgis/arcgis_base/arcgis_cross_domain/cross_domain_fuzhou_event_click.ftl";
		map.put("tid", tid);
		map.put("eventId", eventId);
		map.put("type", type);
		return forward;
	}
}
