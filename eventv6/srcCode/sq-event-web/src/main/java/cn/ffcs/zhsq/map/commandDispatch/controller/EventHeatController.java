package cn.ffcs.zhsq.map.commandDispatch.controller;


import cn.ffcs.shequ.commons.utils.JsonHelper;
import cn.ffcs.shequ.utils.SpringUtils;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.nanChang3D.service.INanChang3DService;
import cn.ffcs.zhsq.szzg.statistics.service.IZgStatisticsService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "/zhsq/map/eventHeatController")
public class EventHeatController extends ZZBaseController {

	@Autowired
	private IZgStatisticsService statisticsService;
	@Autowired
	private INanChang3DService nanChang3DService;

	
	
	/**
	 * 
	 *获取热力图数据 
	 * @throws Exception 
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/findEventHeatData")
	public Map<String,Object> findEventHeatData(	
			HttpSession session,ModelMap map,
			@RequestParam(value = "infoOrgCode", required=false) String infoOrgCode,
			@RequestParam(value = "date_", required=false) String date_,
			@RequestParam(value = "type", required=false) String type,
			@RequestParam(value = "dateType", required=false) String dateType,
			@RequestParam(value = "beginTime", required=false) String beginTime,
			@RequestParam(value = "endTime", required=false) String endTime
			) throws Exception{
		
		Map<String, Object> params =new HashMap<String, Object>();
		params.put("infoOrgCode", getDefaultInfoOrgCode(session));

		if(!SpringUtils.isNullString(beginTime) && !SpringUtils.isNullString(endTime) ) {
			params.put("beginTime", beginTime+" 00:00:00");
			params.put("endTime", endTime+" 23:59:59");
		}
		params=statisticsService.findEventHeatMap(params);
			params.put("date_", date_);
			params.put("type", type);
			params.put("dateType", dateType);
	
		params.put("max", nanChang3DService.findHeatWeights(params).get("MAX"));
		
	

		return params;
				
	}
	
	
	/**
	 *获取3d几何图数据 
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/findEventGeometryData")
	public Map<String,Object> findEventGeometryData(HttpSession session, ModelMap map,	
			@RequestParam(value = "infoOrgCode", required=false) String infoOrgCode,
			@RequestParam(value = "date_", required=false) String date_,
			@RequestParam(value = "type", required=false) String type,
			@RequestParam(value = "gridId", required=false) String gridId,
			@RequestParam(value = "dateType", required=false) String dateType,
			@RequestParam(value = "beginTime", required=false) String beginTime,
			@RequestParam(value = "endTime", required=false) String endTime,
			@RequestParam(value = "ymax", required=false) String ymax,
			@RequestParam(value = "xmin", required=false) String xmin,
			@RequestParam(value = "xmax", required=false) String xmax,
			@RequestParam(value = "ymin", required=false) String ymin
          ) throws Exception {
	
		Map<String, Object> params =new HashMap<String, Object>();
		params.put("dateType",dateType );
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);


		if(infoOrgCode!=null && infoOrgCode.length()<=6) {
			params.put("infoOrgCode",infoOrgCode );
			params= nanChang3DService.findGeometryData(params);
		}else {
			params.put("infoOrgCode",getDefaultInfoOrgCode(session));
			params.put("type", type);
			params.put("date_", date_);
			params.put("ymax", ymax);
			params.put("xmin", xmin);
			params.put("xmax", xmax);
			params.put("ymin", ymin);
			params= nanChang3DService.findGeometryPoint(params);	
		}

		return params;
				
	}

	/**
	 *获取热力图数据
	 * @param
	 * 		infoOrgCode		地域编码
	 *      timeStart     开始时间
	 *      timeEnd       结束时间
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findEventHeatDataJsonp")
	public String findEventHeatDataJsonp(
			HttpSession session,@RequestParam Map<String, Object> params,
			 @RequestParam(value = "beginTime", required=false) String beginTime,
			 @RequestParam(value = "endTime", required=false) String endTime) throws Exception {
		String jsonpcallback = "";
		if(CommonFunctions.isNotBlank(params, "jsonpcallback") ) {
			jsonpcallback = params.get("jsonpcallback").toString();
		}
		if(CommonFunctions.isBlank(params, "infoOrgCode") ) {
			params.put("infoOrgCode", getDefaultInfoOrgCode(session));
		}
		if(StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime) ) {
			params.put("beginTime", beginTime+" 00:00:00");
			params.put("endTime", endTime+" 23:59:59");
		}
		Map<String, Object>  result=statisticsService.findEventHeatMap(params);
		if(StringUtils.isNotBlank(jsonpcallback)) {
			jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(result) + ")";
		} else {
			jsonpcallback = JsonHelper.getJsonString(result);
		}

		return jsonpcallback;
	}
	
	
}
