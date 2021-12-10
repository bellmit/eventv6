package cn.ffcs.zhsq.map.devicecollectdata.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceCollectAlertService;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceCollectDataService;
import cn.ffcs.zhsq.deviceinfos.service.DeviceInfosService;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectAlert;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectData;
import cn.ffcs.zhsq.mybatis.domain.deviceinfos.DeviceInfos;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.JsonUtils;

/**
 * 马尾项目 烟感控制器
 * @author zhangzhh
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/smoke")
public class SmokeController extends ZZBaseController{

	@Autowired
	private DeviceCollectAlertService deviceCollectAlertService;
	
	@Autowired
	private DeviceCollectDataService deviceCollectDataService;
	
	@Autowired 
	protected DeviceInfosService deviceInfosService;
	
	/**
	 * 获取烟感数据祥情(概要)
	 * @param session
	 * @param deviceId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/smokeDetailGnr")
	public String smokeDetail(HttpSession session,
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

		
//		烟感的正常,异常,告警统计
//		String regionCode="350105002223001";
//		String alertFlagType = "0";
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("regionCode", regionCode);
//		Map<String, Object> countMap = deviceInfosService.countStatus(param);
//		System.out.println("*******数量>>>>>>>>>>>>>>:"+countMap.get("countNormal"));
		

		return "/map/arcgis/standardmappage/devicecollectdata/smoke/detail_smoke.ftl";
	}
	
//	/**
//	 * 获取烟感数据祥情(详情)
//	 * @param session
//	 * @param deviceId
//	 * @param map
//	 * @return
//	 */
//	
//	@RequestMapping(value="/smokeDetailMore")
//	public String smokeDetailMore(HttpSession session,
//			@RequestParam(value="deviceId") Long deviceId, ModelMap map) {
//		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
//		
//		//先判断是否有烟感预警告警的数据
//		Map<String, Object> params = new HashMap<String, Object>();
//		List<DeviceCollectAlert>  alertList=deviceCollectAlertService.findListByDeviceId(deviceId);
//		
//		if(alertList!=null && alertList.size()>0){
//			//获取烟感告警的数据
//			DeviceCollectAlert  deviceCollectAlert=alertList.get(0);
//			String jsonStr=deviceCollectAlert.getCollectAlertJsonStr();
//			Map<String, Object> itemMap=JsonUtils.json2Map(jsonStr);
//			
//			map.addAttribute("deviceCollectAlert",deviceCollectAlert);
//			map.addAttribute("itemMap",itemMap);
//			//记录时间
//			map.addAttribute("collTime",deviceCollectAlert.getCollTime());
//		}else{
//			//获取烟感数据
//			List<DeviceCollectData> dataList=deviceCollectDataService.findList(params);
//			map.addAttribute("dataList",dataList);
//			//取其中的一条记录时间
//			if(dataList!=null && dataList.size()>0){
//				//记录时间
//				map.addAttribute("collTime",dataList.get(0).getCollTime());
//			}
//		
//		}
//		//获取烟感设备基础数据
//		DeviceInfos deviceInfo = deviceInfosService.searchById(deviceId);
//		map.addAttribute("deviceInfo",deviceInfo);
//
//		return "/map/arcgis/standardmappage/devicecollectdata/smoke/detail_more_smoke.ftl";
//	}
//	
//	/**
//	 * 历史信息
//	 * @author zhangzhenhai
//	 * @date 2017-10-19 下午8:25:57
//	 */
//	@ResponseBody
//	@RequestMapping("/historyInfo")
//	public Object historyInfo(HttpServletRequest request, HttpSession session, ModelMap map,
//			@RequestParam(value="deviceId",required=false) String deviceId,
//			@RequestParam(value="deviceType",required=false) String deviceType,
//			@RequestParam(value="page",required=false) int page,
//			@RequestParam(value="rows",required=false) int rows,
//			@RequestParam(value="startTime",required=false) String startTime,
//			@RequestParam(value="endTime",required=false) String endTime
//			) {
//		
//		page = page < 1 ? 1 : page;
//		rows = rows < 1 ? 20 : rows;
//		
//		deviceId="201701070150000001437";
//		deviceType="100001";
//		
//		JSONObject json = MwHistoryInfoUtil.getBackJsonMsg(deviceId, deviceType, page, rows, startTime, endTime);
//		
//		int resCode = json.getInt("res_code");
//		if(resCode == 0){//成功
//			JSONArray array = null;
//			if(json.getJSONArray("device_list") != null){
//				array = json.getJSONArray("device_list");
//			}
//			
//			//针对不同的设备类型进行解析，用if做判断
//			
//			//小水表()
//			List<SmallWaterMeterHis> list = (List<SmallWaterMeterHis>) array.toCollection(array, SmallWaterMeterHis.class);
////			long count =json.getLong("total_count");
//			long count =110L;
//			EUDGPagination pagination = new EUDGPagination(count, list);
//			return  pagination;
//			
//			
//			
//		}else{//失败
//			String resMsg = json.getString("res_message");
//			EUDGPagination pagination = new EUDGPagination(0, null);
//			return pagination;
//		}
//	}
//
//	
//	
//	/**
//	 * 告警信息
//	 * @author zhangzhenhai
//	 * @date 2017-10-19 下午8:25:57
//	 */
//	@ResponseBody
//	@RequestMapping("/alarmInfo")
//	public Object alarmInfo(HttpServletRequest request, HttpSession session, ModelMap map,
//			@RequestParam(value="deviceId",required=false) String deviceId,
//			@RequestParam(value="deviceType",required=false) String deviceType,
//			@RequestParam(value="page",required=false) int page,
//			@RequestParam(value="rows",required=false) int rows,
//			@RequestParam(value="startTime",required=false) String startTime,
//			@RequestParam(value="endTime",required=false) String endTime
//			) {
//		
//		page = page < 1 ? 1 : page;
//		rows = rows < 1 ? 20 : rows;
//		
//		deviceId="201701070150000001437";
//		deviceType="100001";
//		
//		JSONObject json = MwHistoryInfoUtil.getBackJsonMsg(deviceId, deviceType, page, rows, startTime, endTime);
//		
//		int resCode = json.getInt("res_code");
//		if(resCode == 0){//成功
//			JSONArray array = null;
//			if(json.getJSONArray("device_list") != null){
//				array = json.getJSONArray("device_list");
//			}
//			
//			//针对不同的设备类型进行解析，用if做判断
//			
//			//小水表()
//			List<SmallWaterMeterHis> list = (List<SmallWaterMeterHis>) array.toCollection(array, SmallWaterMeterHis.class);
////			long count =json.getLong("total_count");
//			long count =110L;
//			EUDGPagination pagination = new EUDGPagination(count, list);
//			return  pagination;
//			
//			
//			
//		}else{//失败
//			String resMsg = json.getString("res_message");
//			EUDGPagination pagination = new EUDGPagination(0, null);
//			return pagination;
//		}
//	}
	
	
	
	
	

}
