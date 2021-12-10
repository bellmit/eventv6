package cn.ffcs.zhsq.bus.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.bus.job.RealTimeBusSocket;
import cn.ffcs.zhsq.bus.job.SynchroBusJob;
import cn.ffcs.zhsq.bus.service.BusService;
import cn.ffcs.zhsq.mybatis.domain.map.bus.Busline;
import cn.ffcs.zhsq.mybatis.domain.map.bus.CarBusline;
import cn.ffcs.zhsq.mybatis.domain.map.bus.Station;
import cn.ffcs.zhsq.taxi.job.SynchroTaxiJob;
import cn.ffcs.zhsq.utils.domain.App;

@Controller
@RequestMapping("/zhsq/map/bus")
public class BusController {
	@Autowired
	private BusService busService;
	@Value("${config.bus.websocket.url}")
	private String busWebSocketUrl;
	static boolean init = false;
	static boolean can = false;//关闭socket,使用单独的socket工程进行跑
	//private RealTimeBusSocket busSocket = new RealTimeBusSocket();
	private static final String STATION_UP_LINE="1";//上行线路
	private static final String STATION_DOWN_LINE="2";//下行线路
	@Autowired
	private SynchroBusJob job;
	@Autowired
	private SynchroTaxiJob taxiJob;
	
	@RequestMapping(value="/runSocket")
	@ResponseBody
	public String runSocket() {
		if(can){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						new RealTimeBusSocket().init();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		return "success";
	}
	
	/**
	 * 手动同步接口数据
	 * @return
	 */
	@RequestMapping(value="/executeAsync")
	@ResponseBody
	public Object executeAsync() {
		try {
			job.execute();
			taxiJob.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"success\":false}";
		}
		return "{\"success\":true}";
	}
	
	@RequestMapping(value="/list")
	public String list(HttpServletRequest request,HttpSession session, ModelMap map,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		//Map<String, Object> defaultGridInfo = getDefaultGridInfo(session);
		//Long gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
		//map.put("gridId", gridId);
		map.put("gridId", 1);
		map.put("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/bus/bus_list.ftl";
	}
	
	@RequestMapping("index")
	public String index(HttpServletRequest request,ModelMap model,
			@RequestParam(value = "x", required = false) String x,
			@RequestParam(value = "y", required = false) String y) {
		model.put("busWebSocketUrl", busWebSocketUrl);
		
			if(StringUtils.isBlank(x)) {
				model.addAttribute("x", 118.17);
			}else {
				model.addAttribute("x",x);
			}
			if(StringUtils.isBlank(y)) {
				model.addAttribute("y", 26.65);
			}else {
				model.addAttribute("y", y);
			}
		return "/map/arcgis/standardmappage/bus/bus_index.html";
	}
	
	@RequestMapping(value="/pageData")
	public @ResponseBody Object pageData(HttpSession session, ModelMap map,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows,
			Busline busline) {
		try {
			Map<String,Object> params = new HashMap<String,Object>();
			
			if(busline.getBuslineId() != null){
				params.put("buslineId", busline.getBuslineId());
			}
			if(StringUtils.isNotBlank(busline.getBuslineName())){
				params.put("buslineName", busline.getBuslineName());
			}
			EUDGPagination pagination = busService.findBuslinePage(page, rows, params);
			
			return pagination;
		} catch (Exception e) {
			e.printStackTrace();
			return new EUDGPagination();
		}
	}
	
	/**
	 * 详情页面
	 * @param request
	 * @param model
	 * @param buslineId
	 * @return
	 */
	@RequestMapping(value="/buslineDetail")
	public String buslineDetail(HttpServletRequest request,
			ModelMap model,@RequestParam("buslineId")Long buslineId){
		HttpSession session = request.getSession();
		Busline buslineInfo = busService.searchBuslineById(buslineId);
		model.addAttribute("RESOURSE_SERVER_PATH", App.IMG.getDomain(session));
		model.addAttribute("data", buslineInfo);
		model.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		model.addAttribute("SQ_ZHSQ_EVENT_URL", App.EVENT.getDomain(session));
		
		Map<String,Object> query = new HashMap<String,Object>();
		query.put("buslineId", buslineId);
		List<CarBusline> carList = busService.searchCarBuslineList(query);
		model.put("carList", carList);
		query.put("upDown", STATION_UP_LINE);//上行线路
		List<Station> upStation = busService.searchStationListByLineInfo(query);
		query.put("upDown", STATION_DOWN_LINE);//下行线路
		List<Station> downStation = busService.searchStationListByLineInfo(query);
		model.put("upStation", upStation);
		model.put("downStation", downStation);
		
		return "/map/arcgis/standardmappage/bus/bus_detail.html";
	}
	
	@RequestMapping("/buslineDetailData")
	@ResponseBody
	public Object buslineDetailData(HttpServletRequest request,@RequestParam("buslineId")Long buslineId){
		if(!init || "runSocket".equals(request.getParameter("runSocket"))){
			runSocket();
			init = true;
		}
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String,Object> query = new HashMap<String,Object>();
		query.put("buslineId", buslineId);
		Busline buslineInfo = busService.searchBuslineById(buslineId);
		List<CarBusline> carList = busService.searchCarBuslineList(query);
		query.put("upDown", STATION_UP_LINE);//上行线路
		List<Station> upStation = busService.searchStationListByLineInfo(query);
		query.put("upDown", STATION_DOWN_LINE);//下行线路
		List<Station> downStation = busService.searchStationListByLineInfo(query);
		result.put("buslineInfo", buslineInfo);
		result.put("carList", carList);
		result.put("upStation", upStation);
		result.put("downStation", downStation);
		
		return result;
	}
}
