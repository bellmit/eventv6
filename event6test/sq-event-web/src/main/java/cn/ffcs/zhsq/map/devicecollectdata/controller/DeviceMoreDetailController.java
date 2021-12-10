package cn.ffcs.zhsq.map.devicecollectdata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceCollectAlertService;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceCollectDataService;
import cn.ffcs.zhsq.deviceinfos.service.DeviceInfosService;
import cn.ffcs.zhsq.mybatis.domain.devicealarminfo.ManholeCoverAlarm;
import cn.ffcs.zhsq.mybatis.domain.devicealarminfo.SmokeAlarm;
import cn.ffcs.zhsq.mybatis.domain.devicealarminfo.WaterMeterAlarm;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectAlert;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectData;
import cn.ffcs.zhsq.mybatis.domain.deviceinfos.DeviceInfos;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.AccessControlHis;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.BidWaterMeterHis;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.CarBistinguishHis;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.ChargingPileHis;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.EnvMonitoringHis;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.FireHydrantHis;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.GeomagneticHis;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.ManholeCoverHis;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.SmallWaterMeterHis;
import cn.ffcs.zhsq.mybatis.domain.historyInfo.StreetlightHis;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.JsonUtils;
import cn.ffcs.zhsq.utils.MwHistoryInfoUtil;

/**
 * 马尾项目 设备更多信息控制器
 * @author zhangzhh
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/mw/moreDetail")
public class DeviceMoreDetailController extends ZZBaseController{

	@Autowired
	private DeviceCollectAlertService deviceCollectAlertService;
	
	@Autowired
	private DeviceCollectDataService deviceCollectDataService;
	
	@Autowired 
	protected DeviceInfosService deviceInfosService;
	
	
	/**
	 * 获取烟感数据祥情(详情)
	 * @param session
	 * @param deviceId
	 * @param map
	 * @return
	 */
	
	@RequestMapping(value="/basInfo")
	public String basInfo(HttpSession session,
			@RequestParam(value="deviceId") Long deviceId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		//先判断是否有烟感预警告警的数据
		Map<String, Object> params = new HashMap<String, Object>();
		List<DeviceCollectAlert>  alertList=deviceCollectAlertService.findListByDeviceId(deviceId);
		
		if(alertList!=null && alertList.size()>0){
			//获取烟感告警的数据
			DeviceCollectAlert  deviceCollectAlert=alertList.get(0);
			String jsonStr=deviceCollectAlert.getCollectAlertJsonStr();
			Map<String, Object> itemMap=JsonUtils.json2Map(jsonStr);
			
			map.addAttribute("deviceCollectAlert",deviceCollectAlert);
			map.addAttribute("itemMap",itemMap);
			//记录时间
			map.addAttribute("collTime",deviceCollectAlert.getCollTime());
		}else{
			//获取烟感数据
			List<DeviceCollectData> dataList=deviceCollectDataService.findList(params);
			map.addAttribute("dataList",dataList);
			//取其中的一条记录时间
			if(dataList!=null && dataList.size()>0){
				//记录时间
				map.addAttribute("collTime",dataList.get(0).getCollTime());
			}
		}
		
		//获取烟感设备基础数据
		DeviceInfos deviceInfo = deviceInfosService.searchById(deviceId);
		map.addAttribute("deviceInfo",deviceInfo);
		//设备类型 :100001  水表（小）;100002  水表（大）;100003  消防栓;100004 烟感;
		//100005  井盖;100006  门禁(摄像头);100007  地磁; 100008  梯控;100009  梯口机（进出口门禁）;100010  车辆识别;
		//100012  电动车充电桩100013  井盖井下设备100014井盖路面积水设备
		//100015  井盖浊度设备100016  井盖流速设备 100017 环境监测
		
		String deviceType = deviceInfo.getDeviceType();
		String url = "";
		
		if("100001".equals(deviceType) || "100002".equals(deviceType) || "100005".equals(deviceType)){
			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_historyAndAlarmInfo.ftl";
		}
		if("100003".equals(deviceType) || "100007".equals(deviceType) || "100010".equals(deviceType) || "100011".equals(deviceType) || "100012".equals(deviceType)){
			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_historyInfo.ftl";
		}
		if("100004".equals(deviceType)){
			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_alarmInfo.ftl";
		}
		
//		if("100001".equals(deviceType)){//水表（小）
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_smallWater.ftl";
//		}
//		if("100002".equals(deviceType)){//水表（大）
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_bigWater.ftl";
//		}
//		if("100003".equals(deviceType)){//消防栓
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_fireHydrant.ftl";
//		}
//		if("100004".equals(deviceType)){//烟感
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_smoke.ftl";
//		}
//		if("100005".equals(deviceType)){//井盖
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_manholeCover.ftl";
//		}
//		if("100006".equals(deviceType)){//门禁（摄像头出入口）
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_.ftl";
//		}
//		if("100007".equals(deviceType)){//地磁
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_geomagnetic.ftl";
//		}
//		if("100008".equals(deviceType)){//梯控
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_.ftl";
//		}
//		if("100009".equals(deviceType)){//梯口机（电梯口出入，这才是所谓的门禁）
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_accessControl.ftl";
//		}
//		if("100010".equals(deviceType)){//车辆识别
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_carBistinguish.ftl";
//		}
//		if("100011".equals(deviceType)){//路灯
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_streetlight.ftl";
//		}
//		if("100012".equals(deviceType)){//电动车充电桩
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_chargingPile.ftl";
//		}
//		if("100013".equals(deviceType)){//井盖井下设备
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_.ftl";
//		}
//		if("100014".equals(deviceType)){//井盖路面积水设备
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_.ftl";
//		}
//		if("100015".equals(deviceType)){//井盖浊度设备
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_.ftl";
//		}
//		if("100016".equals(deviceType)){//井盖流速设备
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_.ftl";
//		}
//		if("100017".equals(deviceType)){//环境监测
//			url = "/map/arcgis/standardmappage/devicecollectdata/deviceMoreDetail/detail_more_envMonitoring.ftl";
//		}
		
		return url;
	}
	
	/**
	 * 历史信息
	 * @author zhangzhenhai
	 * @date 2017-10-19 下午8:25:57
	 */
	@ResponseBody
	@RequestMapping("/historyInfo")
	public Object historyInfo(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value="deviceId",required=false) String deviceId,
			@RequestParam(value="deviceType",required=false) String deviceType,
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows,
			@RequestParam(value="startTime",required=false) String startTime,
			@RequestParam(value="endTime",required=false) String endTime
			) {
		
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		
//		充电桩
//		deviceId="417837";
//		deviceType="100012";
//		井盖
//		deviceId="862631039548267";
//		小表
//		deviceId="201708070200000033041";
//		大表
//		deviceId="201707011501000000171";
//		消防栓
//		deviceId="1703160005";
//		路灯
//		deviceId="HFTest";
//		梯口机（门禁）
//		deviceId="02栋01单元(020100000)";
//		车辆识别
//		deviceId="10006_100010";
//		地磁
//		deviceId="90:00:00:00:00:03";
//		环境监测
//		deviceId="A0150301E01704270004";
		
		JSONObject json = MwHistoryInfoUtil.getBackJsonMsg(deviceId, deviceType, page, rows, null, startTime, endTime);
		
		int resCode = json.getInt("res_code");
		if(resCode == 0){//成功
			JSONArray array = null;
			if(json.getJSONArray("device_list") != null){
				array = json.getJSONArray("device_list");
			}
			long count =json.getLong("total_count");
			
			//针对不同的设备类型进行解析，用if做判断
			//小水表;大水表;消防栓;井盖;门禁;地磁;车辆识别;路灯照明;电动车充电桩;摄像头(不做了);环境监测
		
			EUDGPagination pagination = null;
			
			if("100001".equals(deviceType)){//水表（小）
				List<SmallWaterMeterHis> list = (List<SmallWaterMeterHis>) array.toCollection(array, SmallWaterMeterHis.class);
				pagination = new EUDGPagination(count, list);
			}
			if("100002".equals(deviceType)){//水表（大）
				List<BidWaterMeterHis> list = (List<BidWaterMeterHis>) array.toCollection(array, BidWaterMeterHis.class);
				pagination = new EUDGPagination(count, list);
			}
			if("100003".equals(deviceType)){//消防栓
				List<FireHydrantHis> list = (List<FireHydrantHis>) array.toCollection(array, FireHydrantHis.class);
				pagination = new EUDGPagination(count, list);
			}
			if("100004".equals(deviceType)){//烟感
//				List<SmokeAlarm> list = (List<SmokeAlarm>) array.toCollection(array, SmokeAlarm.class);
//				pagination = new EUDGPagination(count, list);
			}
			if("100005".equals(deviceType)){//井盖
				List<ManholeCoverHis> list = (List<ManholeCoverHis>) array.toCollection(array, ManholeCoverHis.class);
				pagination = new EUDGPagination(count, list);
			}
			if("100006".equals(deviceType)){//门禁（摄像头出入口）
//				List<AccessControlHis> list = (List<AccessControlHis>) array.toCollection(array, AccessControlHis.class);
//				pagination = new EUDGPagination(count, list);
			}
			if("100007".equals(deviceType)){//地磁
				List<GeomagneticHis> list = (List<GeomagneticHis>) array.toCollection(array, GeomagneticHis.class);
				pagination = new EUDGPagination(count, list);
			}
			if("100008".equals(deviceType)){//梯控
				//List<BidWaterMeterHis> list = (List<BidWaterMeterHis>) array.toCollection(array, BidWaterMeterHis.class);
				//pagination = new EUDGPagination(count, list);
			}
			if("100009".equals(deviceType)){//梯口机（电梯口出入，这才是所谓的门禁）
				List<AccessControlHis> list = (List<AccessControlHis>) array.toCollection(array, AccessControlHis.class);
				pagination = new EUDGPagination(count, list);
			}
			if("100010".equals(deviceType)){//车辆识别
				List<CarBistinguishHis> list = (List<CarBistinguishHis>) array.toCollection(array, CarBistinguishHis.class);
				pagination = new EUDGPagination(count, list);
			}
			if("100011".equals(deviceType)){//路灯
				List<StreetlightHis> list = (List<StreetlightHis>) array.toCollection(array, StreetlightHis.class);
				pagination = new EUDGPagination(count, list);
			}
			if("100012".equals(deviceType)){//电动车充电桩
				List<ChargingPileHis> list = (List<ChargingPileHis>) array.toCollection(array, ChargingPileHis.class);
				pagination = new EUDGPagination(count, list);
			}
			if("100013".equals(deviceType)){//井盖井下设备
				//List<BidWaterMeterHis> list = (List<BidWaterMeterHis>) array.toCollection(array, BidWaterMeterHis.class);
				//pagination = new EUDGPagination(count, list);
			}
			if("100014".equals(deviceType)){//井盖路面积水设备
				//List<BidWaterMeterHis> list = (List<BidWaterMeterHis>) array.toCollection(array, BidWaterMeterHis.class);
				//pagination = new EUDGPagination(count, list);
			}
			if("100015".equals(deviceType)){//井盖浊度设备
				//List<BidWaterMeterHis> list = (List<BidWaterMeterHis>) array.toCollection(array, BidWaterMeterHis.class);
				//pagination = new EUDGPagination(count, list);
			}
			if("100016".equals(deviceType)){//井盖流速设备
				//List<BidWaterMeterHis> list = (List<BidWaterMeterHis>) array.toCollection(array, BidWaterMeterHis.class);
				//pagination = new EUDGPagination(count, list);
			}
			if("100017".equals(deviceType)){//环境监测
				List<EnvMonitoringHis> list = (List<EnvMonitoringHis>) array.toCollection(array, EnvMonitoringHis.class);
				pagination = new EUDGPagination(count, list);
			}
			
			
			return  pagination;
			
		}else{//失败
			String resMsg = json.getString("res_message");
			List list = new ArrayList();
			EUDGPagination pagination = new EUDGPagination(0, list);
			return pagination;
		}
	}

	
	
	/**
	 * 告警信息
	 * @author zhangzhenhai
	 * @date 2017-10-19 下午8:25:57
	 */
	@ResponseBody
	@RequestMapping("/alarmInfo")
	public Object alarmInfo(HttpServletRequest request, HttpSession session, ModelMap map,
			@RequestParam(value="deviceId",required=false) String deviceId,
			@RequestParam(value="deviceType",required=false) String deviceType,
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows,
			@RequestParam(value="startTime",required=false) String startTime,
			@RequestParam(value="endTime",required=false) String endTime
			) {
		
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		
//		deviceId="201701070150000001437";
//		deviceType="100001";
		//http://27.155.67.52:10001/iot/platform/v1.0.0/device/query?app_key=0001&device_id=163628222645282787&device_type=100004&page_no=1&page_size=1200&start_time=2017-10-12&end_time=2017-10-14
		
//		小表
//		deviceId="201708070200000033041";
//		大表
//		deviceId="201707011501000000171";
//		井盖
//		deviceId="862631039548267";
//		消防栓
//		deviceId="1703160005";
//		烟感
//		deviceId="163628222645282787";
		
//		if(("").equals(startTime) || startTime == null){
//			startTime="2017-01-13";
//		}
//		if(("").equals(endTime) || endTime == null){
//			endTime="2017-10-31";
//		}
		
		
		
		
		String alarmType = "";
		if("100001".equals(deviceType) || "100002".equals(deviceType)){
			alarmType = "1";
		}
		if("100005".equals(deviceType)){
			alarmType = "2";
		}
		
		JSONObject json = MwHistoryInfoUtil.getBackJsonMsg(deviceId, deviceType, page, rows, alarmType, startTime, endTime);
		
		int resCode = json.getInt("res_code");
		if(resCode == 0){//成功
			JSONArray array = null;
			if(json.getJSONArray("device_list") != null){
				array = json.getJSONArray("device_list");
			}
			
			//针对不同的设备类型进行解析，用if做判断
			//烟感告警;井盖告警;水表告警;
			
			long count =json.getLong("total_count");
			EUDGPagination pagination = null;
			
			if("100001".equals(deviceType)){//水表（小）
				List<WaterMeterAlarm> list = (List<WaterMeterAlarm>) array.toCollection(array, WaterMeterAlarm.class);
				pagination = new EUDGPagination(count, list);
			}
			if("100002".equals(deviceType)){//水表（大）
				List<WaterMeterAlarm> list = (List<WaterMeterAlarm>) array.toCollection(array, WaterMeterAlarm.class);
				pagination = new EUDGPagination(count, list);
			}
			if("100004".equals(deviceType)){//烟感
				List<SmokeAlarm> list = (List<SmokeAlarm>) array.toCollection(array, SmokeAlarm.class);
				pagination = new EUDGPagination(count, list);
			}
			if("100005".equals(deviceType)){//井盖
				List<ManholeCoverAlarm> list = (List<ManholeCoverAlarm>) array.toCollection(array, ManholeCoverAlarm.class);
				pagination = new EUDGPagination(count, list);
			}
			
			return  pagination;
			
		}else{//失败
			String resMsg = json.getString("res_message");
			List list = new ArrayList();
			EUDGPagination pagination = new EUDGPagination(0, list);
			return pagination;
		}
	}
	
	
	
	
	

}
