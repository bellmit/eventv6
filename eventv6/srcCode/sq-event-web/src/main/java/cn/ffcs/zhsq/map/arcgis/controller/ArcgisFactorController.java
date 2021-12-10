package cn.ffcs.zhsq.map.arcgis.controller;



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

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisInfoService;
import cn.ffcs.zhsq.map.menuconfigure.service.IMenuConfigService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisConfigInfo;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfGrid;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisMapConfigInfo;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

import com.alibaba.fastjson.JSONArray;

/**
 * 2014-05-16 liushi add
 * arcgis地图加载控制器
 * @author liushi
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisFactor")
public class ArcgisFactorController extends ZZBaseController {
	
	@Autowired
	private IArcgisInfoService arcgisInfoService;
	@Autowired
	private IMenuConfigService menuConfigService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@RequestMapping(value="/toFactorPointTag")
	public String outPlatCrossDomain(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="id", required=false) Long id
			,@RequestParam(value="name", required=false) String name
			,@RequestParam(value="arcgisFactorUrl", required=false) String arcgisFactorUrl
			,@RequestParam(value="markerType", required=false) String markerType
			,@RequestParam(value="x", required=false) Double x
			,@RequestParam(value="y", required=false) Double y
			,@RequestParam(value="gridId", required=false) Long gridId
			,@RequestParam(value="mapt", required=false) Integer mapt
			,@RequestParam(value="callBackUrl", required=false) String callBackUrl
			,@RequestParam(value="mapType", required=false) String mapType
			,@RequestParam(value="isEdit", required=false) String isEdit
			,@RequestParam(value="parameterJsonStr", required=false) String parameterJsonStr) throws Exception{
		String forward = "/map/arcgis/arcgis_factor/arcgis_factor_pointTag.ftl";
		if(mapt == null) {
			mapt = 5;
		}
		if(isEdit == null || "".equals(isEdit)) {
			isEdit = "true";
		}
		if("3".equals(mapType)) {
			mapt = 30;
		}else if("2".equals(mapType)) {
			mapt = 5;
		}
		if(x==null || y==null){
			if(gridId == null){
				MixedGridInfo gridInfo = getMixedGridInfo(session,request);
				//获取当前登录人员所在网格信息
				if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
				gridId = gridInfo.getGridId();
			}
			List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,mapt);
			if(list.size() == 1){
				x = list.get(0).getX();
				y = list.get(0).getY();
			}
		}
		if(x==null) {
			x = 0.0;
		}
		if(y == null) {
			y = 0.0;
		}
		if(callBackUrl == null){
			callBackUrl = "";
		}
		/*
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, true);
		map.addAttribute("gridInfo", gridInfo);
		//-- 区域点位坐标转换成json数据
		String locationListJson = JSONArray.toJSONString(gridInfo.getLocationList());
		map.addAttribute("locationListJson", locationListJson);
		*/
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		String ARCGIS_DOCK_MODE = this.funConfigurationService.turnCodeToValue("ARCGIS_DOCK_MODE", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, gridInfo.getInfoOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.put("id", id);
		map.put("name", name);
		map.put("arcgisFactorUrl", arcgisFactorUrl);
		map.put("markerType", markerType);
		map.put("ARCGIS_DOCK_MODE", ARCGIS_DOCK_MODE);
		map.put("x", x.toString());
		map.put("y", y.toString());
		map.put("mapt", mapt);
		map.put("gridId", gridId);
		
		map.put("isEdit", isEdit);
		map.put("mapType", mapType);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.put("callBackUrl", callBackUrl);
		map.put("parameterJsonStr", parameterJsonStr.replaceAll("\"", "'"));
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
	
	@RequestMapping(value="/toFactorPointTagCommon")
	public String toFactorPointTagCommon(HttpSession session, HttpServletRequest request,HttpServletResponse response, ModelMap map,
			@RequestParam(value="id", required=false) Long id
			,@RequestParam(value="name", required=false) String name
			,@RequestParam(value="FACTOR_URL", required=false) String FACTOR_URL
			,@RequestParam(value="FACTOR_SERVICE", required=false) String FACTOR_SERVICE
			,@RequestParam(value="markerType", required=false) String markerType
			,@RequestParam(value="x", required=false) Double x
			,@RequestParam(value="y", required=false) Double y
			,@RequestParam(value="gridId", required=false) Long gridId
			,@RequestParam(value="mapt", required=false) Integer mapt
			,@RequestParam(value="callBackUrl", required=false) String callBackUrl
			,@RequestParam(value="mapType", required=false) String mapType
			,@RequestParam(value="isEdit", required=false) String isEdit
			,@RequestParam(value="parameterJsonStr", required=false) String parameterJsonStr) throws Exception{
		String forward = "/map/arcgis/arcgis_factor/arcgis_factor_pointTag_common.ftl";
		if(mapt == null) {
			mapt = 5;
		}
		if(isEdit == null || "".equals(isEdit)) {
			isEdit = "true";
		}
		if("3".equals(mapType)) {
			mapt = 30;
		}else if("2".equals(mapType)) {
			mapt = 5;
		}
		if(x==null || y==null){
			if(gridId == null){
				MixedGridInfo gridInfo = getMixedGridInfo(session,request);
				//获取当前登录人员所在网格信息
				if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
				gridId = gridInfo.getGridId();
			}
			List<ArcgisInfoOfGrid> list = this.arcgisInfoService.getArcgisDataOfGrid(gridId ,mapt);
			if(list.size() == 1){
				x = list.get(0).getX();
				y = list.get(0).getY();
			}
		} else {
			if(gridId == null){
				MixedGridInfo gridInfo = getMixedGridInfo(session,request);
				//获取当前登录人员所在网格信息
				if(gridInfo==null) throw new Exception("未找到当前网格信息！！");
				gridId = gridInfo.getGridId();
			}
		}
		if(x==null) {
			x = 0.0;
		}
		if(y == null) {
			y = 0.0;
		}
		if(callBackUrl == null){
			callBackUrl = "";
		}
		/*
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, true);
		map.addAttribute("gridInfo", gridInfo);
		//-- 区域点位坐标转换成json数据
		String locationListJson = JSONArray.toJSONString(gridInfo.getLocationList());
		map.addAttribute("locationListJson", locationListJson);
		*/
		MixedGridInfo gridInfo = getMixedGridInfo(session,request);
		String ARCGIS_DOCK_MODE = this.funConfigurationService.turnCodeToValue("ARCGIS_DOCK_MODE", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, gridInfo.getInfoOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
		map.put("id", id);
		map.put("name", name);
		map.put("FACTOR_URL", FACTOR_URL);
		map.put("FACTOR_SERVICE", FACTOR_SERVICE);
		map.put("markerType", markerType);
		map.put("ARCGIS_DOCK_MODE", ARCGIS_DOCK_MODE);
		map.put("x", x.toString());
		map.put("y", y.toString());
		map.put("mapt", mapt);
		map.put("gridId", gridId);
		
		map.put("isEdit", isEdit);
		map.put("mapType", mapType);
		map.put("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.put("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		map.put("callBackUrl", callBackUrl);
		map.put("parameterJsonStr", parameterJsonStr.replaceAll("\"", "'"));
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragrma","no-cache"); 
		response.setDateHeader("Expires",0); 
		return forward;
	}
}
