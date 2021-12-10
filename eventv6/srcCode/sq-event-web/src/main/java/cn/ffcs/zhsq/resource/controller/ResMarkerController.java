/**
 * 
 */
package cn.ffcs.zhsq.resource.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;

import com.alibaba.fastjson.JSONArray;

/**
 * 资源标注控制器
 * @author guohh
 *
 */
@Controller
@RequestMapping(value="/zhsq/res/marker")
public class ResMarkerController {

	@Autowired
	private IResMarkerService resMarkerService;

	/**
	 * 配置中心点位
	 * @param gridId 网格ID
	 * @param markerType 标注类型（资源类型）
	 * @param resourcesId 关联资源ID
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/config")
	public String config(@RequestParam(value="gridId") Long gridId, @RequestParam(value="markerType", required = false) String markerType,
			@RequestParam(value="resourcesId") Long resourcesId, ModelMap map) {
		//  Modify By YangCQ 2013-11-11 用于支持没有MarkerType也能调用
		markerType = markerType == null ? "" : markerType;
		map.addAttribute("gridId", gridId);
		map.addAttribute("markerType", markerType);
		map.addAttribute("resourcesId", resourcesId);
		//根据标注类型和资源ID获取标注列表
		List<ResMarker> markerList = resMarkerService.findResMarkerByParam(markerType, resourcesId, null);
		String markerListJson = JSONArray.toJSONString((markerList!=null?markerList:new ArrayList<ResMarker>()));
		map.addAttribute("markerListJson", markerListJson);
		return "/zzgl_res/marker/config.ftl";
	}
	
	/**
	 * 保存或者更新资源标注
	 * @param session
	 * @param request
	 * @param markerType 标注类型（资源类型）
	 * @param resourcesId 关联资源ID
	 * @param mapType 地图类型（二维，三维）
	 * @param x 经度
	 * @param y 维度
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/saveOrUpdateResMarker", method=RequestMethod.POST)
	public Map<String, Object> batchDeleteCompany(HttpSession session, HttpServletRequest request,
			@RequestParam(value="markerType") String markerType, @RequestParam(value="resourcesId") Long resourcesId,
			@RequestParam(value="mapType") String mapType, @RequestParam(value="x") String x, @RequestParam(value="y") String y) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean result = false;
		try {
			ResMarker marker = new ResMarker();
			//  Modify By YangCQ 2013-11-11 用于支持没有MarkerType也能保存
			if (StringUtils.isNotBlank(markerType)) {
				marker.setCatalog(markerType.substring(0, 2));
				marker.setMarkerType(markerType);
			} else {
				marker.setCatalog("");
				marker.setMarkerType("");
			}
			marker.setResourcesId(resourcesId);
			marker.setMapType(mapType);
			marker.setX(x);
			marker.setY(y);
			result = resMarkerService.saveOrUpdateResMarker(marker);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 根据标注类型、资源ID和加载的地图类型获取标注列表
	 * @param markerType 标注类型
	 * @param resourcesId 资源ID
	 * @param mapType 加载的地图类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getResMarkerList")
	public List<ResMarker> getResMarkerList(@RequestParam(value="markerType") String markerType,
			@RequestParam(value="resourcesId") Long resourcesId, @RequestParam(value="mapType") String mapType) {
		//根据标注类型和资源ID获取标注列表
		List<ResMarker> markerList = resMarkerService.findResMarkerByParam(markerType, resourcesId, mapType);
		return markerList;
	}
}
