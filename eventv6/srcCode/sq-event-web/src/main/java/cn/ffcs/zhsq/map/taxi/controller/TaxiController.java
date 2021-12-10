package cn.ffcs.zhsq.map.taxi.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.http.CustomAuthenticator;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.taxi.service.ITaxiService;
import cn.ffcs.zhsq.mybatis.domain.map.taxi.CarInfo;
import cn.ffcs.zhsq.taxi.config.TaxiConfig;
import cn.ffcs.zhsq.utils.domain.App;

@Controller
@DependsOn(value="taxiConfig")
@RequestMapping(value = "/zhsq/map/taix")
public class TaxiController extends ZZBaseController {

	@Autowired
	private ITaxiService taxiService;
	
	@Autowired
	private TaxiConfig taxiConfig;
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	@RequestMapping(value="/list")
	public String list(HttpServletRequest request,HttpSession session, ModelMap map,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			@RequestParam(value = "isCross", required=false) String isCross) {
		map.addAttribute("isCross", isCross);
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		Long gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		map.put("gridId", gridId);
		map.put("elementsCollectionStr", elementsCollectionStr);
		String standardUrl = "/map/arcgis/standardmappage/taxi/standardTaxi.ftl";
		return standardUrl;
	}
	
	@RequestMapping(value="/pageList")
	public @ResponseBody Object pageList(HttpSession session, ModelMap map,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			@RequestParam(value = "carNo", required = false) String carNo) {
		try {
			Map<String,Object> params = new HashMap<String,Object>();
			
			if(StringUtils.isNotEmpty(carNo)){
				params.put("carNo", carNo);
			}
			
			EUDGPagination pagination = taxiService.findTaxiPage(page, rows, params);
			
			return pagination;
		} catch (Exception e) {
			e.printStackTrace();
			return new EUDGPagination();
		}
	}
	
	@RequestMapping(value="/taxiDetail")
	public String taxiDetail(HttpServletRequest request,HttpSession session,
			ModelMap map,@RequestParam("carId")Long carId,
			@RequestParam(value = "isCross", required=false) String isCross){
		map.addAttribute("isCross", isCross);
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.HOUR, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String startTime = sdf.format(ca.getTime());
		String endTime = sdf.format(new Date());
		map.addAttribute("startTime", startTime);
		map.addAttribute("endTime", endTime);
		CarInfo carInfo = this.taxiService.searchCarInfoById(carId);
		map.addAttribute("RESOURSE_SERVER_PATH",  App.IMG.getDomain(session));
		map.addAttribute("data", carInfo);
		map.addAttribute("tel_show", 1); //拨号
		map.addAttribute("gota_show", 0); //gota
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		return "/map/arcgis/standardmappage/taxi/taxiDetail.ftl";
	}
	
	/**
	 * 出租车历史轨迹
	 * @param request
	 * @param devId	车辆设备id
	 * @param stime	轨迹开始时间，日期格式：2014-03-26 09:00:00
	 * @param etime	轨迹结束时间，日期格式：2014-03-26 19:00:00
	 * @return
	 */
	@RequestMapping("/historyTrail")
	public @ResponseBody Object historyTrail(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("carId") Long carId,
			@RequestParam(value="stime",required=false) String stime,
			@RequestParam(value="etime",required=false) String etime){
		
		CarInfo car = this.taxiService.searchCarInfoById(carId);
		
		if(StringUtils.isEmpty(car.getDevId()))
			return null;
		
		JSONObject paramJson = new JSONObject();
		paramJson.put("dev_id", car.getDevId());
		if(StringUtils.isNotEmpty(stime)){
			paramJson.put("stime", stime);
		}
		if(StringUtils.isNotEmpty(etime)){
			paramJson.put("etime", etime);
		}
		try {
			String url = taxiConfig.getAccessUrl() + TaxiConfig.URL_TAXI_HISTORY_TRACK;
			CustomAuthenticator authenticator = new CustomAuthenticator(taxiConfig.getUserName(),taxiConfig.getPwd(),true);
			
			return HttpUtil.getHistoryTrail(url,authenticator,paramJson);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 出租车实时位置
	 * @param request
	 * @param response
	 * @param devId
	 * @return
	 */
	@RequestMapping("/daymicTrail")
	public @ResponseBody Object daymicTrail(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("devId") String devId){
		
		JSONObject paramJson = new JSONObject();
		paramJson.put("dev_id", devId);
		try {
			String url = taxiConfig.getAccessUrl() + TaxiConfig.URL_TAXI_LAST_POSITION_BY_DEVID;
			CustomAuthenticator authenticator = new CustomAuthenticator(taxiConfig.getUserName(),taxiConfig.getPwd(),true);
			
			return HttpUtil.getHistoryTrail(url,authenticator,paramJson);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*************************百度地图应用************************************/
	@RequestMapping(value="/taxiList")
	public String taxiList(HttpServletRequest request,HttpSession session, ModelMap map,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			@RequestParam(value = "isCross", required=false) String isCross) {
		map.addAttribute("isCross", isCross);
//		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
//		Long gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		map.put("gridId", 1);
		map.put("elementsCollectionStr", elementsCollectionStr);
		String standardUrl = "/map/arcgis/standardmappage/taxi/taxi_list.ftl";
		return standardUrl;
	}
	
	@RequestMapping(value="/detail")
	public String detail(HttpServletRequest request,HttpSession session,
			ModelMap map,@RequestParam("carId")Long carId,
			@RequestParam(value = "isCross", required=false) String isCross){
		map.addAttribute("isCross", isCross);
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.HOUR, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String startTime = sdf.format(ca.getTime());
		String endTime = sdf.format(new Date());
		map.addAttribute("startTime", startTime);
		map.addAttribute("endTime", endTime);
		CarInfo carInfo = this.taxiService.searchCarInfoById(carId);
		map.addAttribute("RESOURSE_SERVER_PATH",  App.IMG.getDomain(session));
		map.addAttribute("data", carInfo);
		map.addAttribute("tel_show", 1); //拨号
		map.addAttribute("gota_show", 0); //gota
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		return "/map/arcgis/standardmappage/taxi/taxiDetail4Baidu.ftl";
	}
	
	@RequestMapping("/taxiHistroyTrail")
	public @ResponseBody Object taxiHistroyTrail(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("carId") Long carId,
			@RequestParam(value="stime",required=false) String stime,
			@RequestParam(value="etime",required=false) String etime){
		
		Map<String,Object> reslut = new HashMap<String,Object>();
		
		CarInfo car = this.taxiService.searchCarInfoById(carId);
		
		if(StringUtils.isEmpty(car.getDevId()))
			return null;
		
		reslut.put("car", car);
		
		JSONObject paramJson = new JSONObject();
		paramJson.put("dev_id", car.getDevId());
		if(StringUtils.isNotEmpty(stime)){
			paramJson.put("stime", stime);
		}
		if(StringUtils.isNotEmpty(etime)){
			paramJson.put("etime", etime);
		}
		try {
			String url = taxiConfig.getAccessUrl() + TaxiConfig.URL_TAXI_HISTORY_TRACK;
			CustomAuthenticator authenticator = new CustomAuthenticator(taxiConfig.getUserName(),taxiConfig.getPwd(),true);
			
			reslut.put("points",HttpUtil.getHistoryTrail(url,authenticator,paramJson));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reslut;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param carId
	 * @param prevMinutes 多少秒前
	 * @return
	 */
	@RequestMapping("/getPervMinutesTrail")
	public @ResponseBody Object getPervMinutesTrail(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("carId") Long carId,
			Integer prevMills){
		
		Map<String,Object> reslut = new HashMap<String,Object>();
		
		CarInfo car = this.taxiService.searchCarInfoById(carId);
		
		if(StringUtils.isEmpty(car.getDevId()))
			return null;
		
		reslut.put("car", car);
		
		JSONObject paramJson = new JSONObject();
		paramJson.put("dev_id", car.getDevId());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Calendar beforeTime = Calendar.getInstance();
	    if(prevMills != null){
	    	beforeTime.add(Calendar.MINUTE, -(int)prevMills/60);// 2分钟之前的时间
	    } else {
	    	beforeTime.add(Calendar.MINUTE, -2);// 2分钟之前的时间
	    }
	    
	    Date beforeD = beforeTime.getTime();
	    String stime = sdf.format(beforeD);
		
		paramJson.put("stime", stime);
		paramJson.put("etime", sdf.format(new Date()));
		
		try {
			String url = taxiConfig.getAccessUrl() + TaxiConfig.URL_TAXI_HISTORY_TRACK;
			CustomAuthenticator authenticator = new CustomAuthenticator(taxiConfig.getUserName(),taxiConfig.getPwd(),true);
			
			reslut.put("points",HttpUtil.getHistoryTrail(url,authenticator,paramJson));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reslut;
	}
	
	/**
	 * 出租车实时位置
	 * @param request
	 * @param response
	 * @param devId
	 * @return
	 */
	@RequestMapping("/daymicTrailByCarId")
	public @ResponseBody Object daymicTrailByCarId(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("carId") Long carId,
			String isTest,Integer index){
		
		Map<String,Object> reslut = new HashMap<String,Object>();
		
		CarInfo car = this.taxiService.searchCarInfoById(carId);
		reslut.put("car", car);
		
		if("1".equals(isTest)){
			JSONArray points = this.initPoints();
			JSONArray array = new JSONArray();
			array.add(points.get(index));
			reslut.put("points", array);
			return reslut;
		}
		
		JSONObject paramJson = new JSONObject();
		paramJson.put("dev_id", car.getDevId());
		try {
			String url = taxiConfig.getAccessUrl() + TaxiConfig.URL_TAXI_LAST_POSITION_BY_DEVID;
			CustomAuthenticator authenticator = new CustomAuthenticator(taxiConfig.getUserName(),taxiConfig.getPwd(),true);
			
			reslut.put("points",HttpUtil.getHistoryTrail(url,authenticator,paramJson));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reslut;
	}
	
	@RequestMapping("playTip")
	public String playTip(HttpServletRequest request,ModelMap model){
		return "/map/arcgis/standardmappage/taxi/playTip.ftl";
	}
	
	@RequestMapping("run")
	public String run(HttpServletRequest request){
		return "/map/arcgis/standardmappage/taxi/run.ftl";
	}
	
	private JSONArray initPoints(){
		
		String  points = "[{\"x\":118.011891,\"y\":26.598214,\"gtime\":\"2017-11-18 10:24:30\"},";
				points+= "{\"x\":118.015425,\"y\":26.594706,\"gtime\":\"2017-11-18 10:25:00\"},";
				points+= "{\"x\":118.018549,\"y\":26.590096,\"gtime\":\"2017-11-18 10:25:30\"},";
				points+= "{\"x\":118.022576,\"y\":26.586879,\"gtime\":\"2017-11-18 10:26:00\"},";
				points+= "{\"x\":118.025974,\"y\":26.582911,\"gtime\":\"2017-11-18 10:26:30\"},";
				points+= "{\"x\":118.031168,\"y\":26.580845,\"gtime\":\"2017-11-18 10:27:00\"},";
				points+= "{\"x\":118.031168,\"y\":26.580845,\"gtime\":\"2017-11-18 10:27:30\"},";
				points+= "{\"x\":118.03714,\"y\":26.57928,\"gtime\":\"2017-11-18 10:28:00\"},";
				points+= "{\"x\":118.043603,\"y\":26.579563,\"gtime\":\"2017-11-18 10:28:30\"},";
				points+= "{\"x\":118.049168,\"y\":26.58112,\"gtime\":\"2017-11-18 10:29:00\"},";
				points+= "{\"x\":118.054583,\"y\":26.583708,\"gtime\":\"2017-11-18 10:29:30\"},";
				points+= "{\"x\":118.057948,\"y\":26.58786,\"gtime\":\"2017-11-18 10:30:00\"},";
				points+= "{\"x\":118.061786,\"y\":26.589904,\"gtime\":\"2017-11-18 10:30:30\"},";
				points+= "{\"x\":118.06646,\"y\":26.591661,\"gtime\":\"2017-11-18 10:31:00\"},";
				points+= "{\"x\":118.071136,\"y\":26.592871,\"gtime\":\"2017-11-18 10:31:30\"},";
				points+= "{\"x\":118.072831,\"y\":26.59492,\"gtime\":\"2017-11-18 10:32:00\"},";
				points+= "{\"x\":118.073666,\"y\":26.594211,\"gtime\":\"2017-11-18 10:32:30\"},";
				points+= "{\"x\":118.073666,\"y\":26.594211,\"gtime\":\"2017-11-18 10:33:00\"},";
				points+= "{\"x\":118.073676,\"y\":26.594033,\"gtime\":\"2017-11-18 10:33:30\"},";
				points+= "{\"x\":118.073676,\"y\":26.594033,\"gtime\":\"2017-11-18 10:34:00\"},";
				points+= "{\"x\":118.073676,\"y\":26.594033,\"gtime\":\"2017-11-18 10:34:30\"},";
				points+= "{\"x\":118.073075,\"y\":26.590653,\"gtime\":\"2017-11-18 10:35:00\"},";
				points+= "{\"x\":118.073075,\"y\":26.590653,\"gtime\":\"2017-11-18 10:35:30\"}]";
				
		return JSONArray.fromObject(points);
	}
	
}
