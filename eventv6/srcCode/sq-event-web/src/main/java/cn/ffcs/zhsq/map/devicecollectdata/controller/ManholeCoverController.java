package cn.ffcs.zhsq.map.devicecollectdata.controller;

import java.util.ArrayList;
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
 * 马尾项目 井盖控制器
 * @author luth
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/mw")
public class ManholeCoverController extends ZZBaseController{

	@Autowired
	private DeviceCollectAlertService deviceCollectAlertService;
	
	@Autowired
	private DeviceCollectDataService deviceCollectDataService;
	
	@Autowired 
	protected DeviceInfosService deviceInfosService;
	
	//井盖类型值
	public static String  MANHOLECOVER_VALUE = "100005";
	
	/**
	 * 获取井盖数据闲情
	 * @param session
	 * @param deviceId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/manholeCoverDetail")
	public String manholeCoverDetail(HttpSession session,
								   @RequestParam(value="deviceId") Long deviceId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//获取井盖设备基础数据
		DeviceInfos deviceInfo = deviceInfosService.searchById(deviceId);
		map.addAttribute("deviceInfo",deviceInfo);	
		
		//先判断是否有井盖预警告警的数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceServiceId", deviceInfo.getDeviceServiceId());
		List<DeviceCollectAlert>  alertList=deviceCollectAlertService.findList(params);
	
		
		if(alertList!=null && alertList.size()>0){
			//获取井盖告警的数据
			DeviceCollectAlert  deviceCollectAlert=alertList.get(0);
			String jsonStr=deviceCollectAlert.getCollectAlertJsonStr();
			Map<String, Object> itemMap=JsonUtils.json2Map(jsonStr);
			
			map.addAttribute("deviceCollectAlert",deviceCollectAlert);
			map.addAttribute("itemMap",itemMap);
			//记录时间
			map.addAttribute("collTime",deviceCollectAlert.getCollTime());
		}
		
		//获取井盖数据
		
		//获取井盖子设备数据
		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("parentDeviceServiceId", deviceInfo.getDeviceServiceId());


		Map<String, Object> p1 = new HashMap<String, Object>();
		Map<String, Object> p2 = new HashMap<String, Object>();
		Map<String, Object> p3 = new HashMap<String, Object>();
		Map<String, Object> p4 = new HashMap<String, Object>();

		List<DeviceCollectData> dataList=deviceCollectDataService.findList(params2);
		map.addAttribute("dataList",dataList);	
		if(dataList!=null && dataList.size()>0){
			Map<String, Object> ffParam = new HashMap<String, Object>();
			for(DeviceCollectData dcVo:dataList){
				String collectItemCode = dcVo.getCollectItemCode();
				// //井下
				if (collectItemCode.equals("under_the_shaft")) {
					
					ffParam.put("under_the_shaft",dcVo.getCollectItemValue());
					p1.put("collTime", dcVo.getCollTime());
				}
				// 路面积水
				if (collectItemCode.equals("surface_gathered_water")) {
					ffParam.put("surface_gathered_water",dcVo.getCollectItemValue());
					p2.put("collTime", dcVo.getCollTime());
				}
				// 浊度
				if (collectItemCode.equals("turbidity")) {
					ffParam.put("turbidity", dcVo.getCollectItemValue());
					p3.put("collTime", dcVo.getCollTime());
				}
				// 流速
				if (collectItemCode.equals("velocity_of_flow")) {
					ffParam.put("velocity_of_flow",dcVo.getCollectItemValue());
					p4.put("collTime", dcVo.getCollTime());
				}
			}
			
		
			
			
			if (ffParam.get("under_the_shaft") != null) { // 井下 数据为0
				p1.put("under_the_shaft",(String) ffParam.get("under_the_shaft"));
			} else {
				p1.put("under_the_shaft",0);// 井下 数据为0
				p1.put("collTime","-1");
			}

			if (ffParam.get("surface_gathered_water") != null) { // 路面积水
																	// 数据为0
				p2.put("surface_gathered_water",(String) ffParam.get("surface_gathered_water"));
			} else {
				p2.put("surface_gathered_water",0); // 路面积水数据为0
				p2.put("collTime","-1");
			}

			if (ffParam.get("turbidity") != null) { // 浊度为0
				p3.put("turbidity",(String) ffParam.get("turbidity"));
			} else {
				p3.put("turbidity",0); // 浊度 数据为0
				p3.put("collTime","-1");
			}
			if (ffParam.get("velocity_of_flow") != null) { // 流速 数据为0
				p4.put("velocity_of_flow",(String) ffParam.get("velocity_of_flow"));
			} else {
				p4.put("velocity_of_flow",0); // 流速 数据为0
				p4.put("collTime","-1");
			}

			
		}else{			
		    //井下
			p1.put("under_the_shaft",0);
			p1.put("collTime","-1");
			// 路面积水
			p2.put("surface_gathered_water",0);
			p2.put("collTime","-1");
			// 浊度
			p3.put("turbidity",0);
			p3.put("collTime","-1");
			// 流速
			p4.put("velocity_of_flow",0);
			p4.put("collTime","-1");
						
						
		}
		
		map.addAttribute("p1",p1);
		map.addAttribute("p2",p2);
		map.addAttribute("p3",p3);
		map.addAttribute("p4",p4);
		
		
		//取其中的一条记录时间
		/*if(dataList!=null && dataList.size()>0){
			//记录时间
			map.addAttribute("collTime",dataList.get(0).getCollTime());
		}*/
		
		//--测试数据 获取地磁数据
		
		//Map<String,Object> map1=deviceCollectDataService.findDcData();
        //-- 测试告警
		//numCount();
		//List<Map<String, Object>> aaList= deviceCollectDataService.manholeCoverCount();
		

		return "/map/arcgis/standardmappage/devicecollectdata/detail_manhole_cover.ftl";
	}
	
	
	/**
	 * 地磁设备数据详情
	 * @param session
	 * @param deviceId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/geomagneticDetail")
	public String geomagneticDetail(HttpSession session,
								   @RequestParam(value="deviceId") Long deviceId, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//获取地磁设备基础数据
		DeviceInfos deviceInfo = deviceInfosService.searchById(deviceId);
		map.addAttribute("deviceInfo",deviceInfo);	
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceServiceId", deviceInfo.getDeviceServiceId());
		
		//获取地磁数据
		List<DeviceCollectData> dataList=deviceCollectDataService.findList(params);
		map.addAttribute("dataList",dataList);
		//取其中的一条记录时间
		if(dataList!=null && dataList.size()>0){
			//记录时间
			map.addAttribute("collTime",dataList.get(0).getCollTime());
		}
		
		
		

		//map.addAttribute("RESOURSE_SERVER_PATH",  getNewUrl(session, App.IMG.getDomain(session)));//支持多个域名访问综治网格

		return "/map/arcgis/standardmappage/devicecollectdata/detail_geomagnetic.ftl";
	}
	
	
	
	
	/**
	 * 封装井盖子表的数据
	 * 
	 * @return
	 */
	public List<Map<String, Object>> manholeCoverCount() {
		List<Map<String, Object>> allData = new ArrayList<Map<String, Object>>();
		// 获取井盖所有设备
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceType", MANHOLECOVER_VALUE);
		List<DeviceInfos> devList = deviceInfosService.findList(params);
		if (devList != null && devList.size() > 0) {
			for (DeviceInfos vo : devList) {
				Map<String, Object> par1 = new HashMap<String, Object>();
				// 父设备名称
				par1.put("deviceName", vo.getDeviceName());
				List<String> sonList = new ArrayList<String>();
				// 获取子设备数据

				Map<String, Object> sonParam = new HashMap<String, Object>();
				sonParam.put("parentDeviceServiceId", vo.getDeviceServiceId());
				List<DeviceCollectData> dataList = deviceCollectDataService
						.findList(sonParam);
				if (dataList != null && dataList.size() > 0) {
					Map<String, Object> ffParam = new HashMap<String, Object>();
					for (DeviceCollectData dcVo : dataList) {
						String collectItemCode = dcVo.getCollectItemCode();
						// //井下
						if (collectItemCode.equals("under_the_shaft")) {
							ffParam.put("under_the_shaft",
									dcVo.getCollectItemValue());
						}
						// 路面积水
						if (collectItemCode.equals("surface_gathered_water")) {
							ffParam.put("surface_gathered_water",
									dcVo.getCollectItemValue());
						}
						// 浊度
						if (collectItemCode.equals("turbidity")) {
							ffParam.put("turbidity", dcVo.getCollectItemValue());
						}
						// 流速
						if (collectItemCode.equals("velocity_of_flow")) {
							ffParam.put("velocity_of_flow",
									dcVo.getCollectItemValue());
						}
					}

					if (ffParam.get("under_the_shaft") != null) { // 井下 数据为0
						sonList.add((String) ffParam.get("under_the_shaft"));
					} else {
						sonList.add("0"); // 井下 数据为0
					}

					if (ffParam.get("surface_gathered_water") != null) { // 路面积水
																			// 数据为0
						sonList.add((String) ffParam
								.get("surface_gathered_water"));
					} else {
						sonList.add("0"); // 路面积水数据为0
					}

					if (ffParam.get("turbidity") != null) { // 浊度为0
						sonList.add((String) ffParam.get("turbidity"));
					} else {
						sonList.add("0"); // 浊度 数据为0
					}
					if (ffParam.get("velocity_of_flow") != null) { // 流速 数据为0
						sonList.add((String) ffParam.get("velocity_of_flow"));
					} else {
						sonList.add("0"); // 流速 数据为0
					}

				} else {
					sonList.add("0"); // 井下 数据为0
					sonList.add("0"); // 路面积水为0
					sonList.add("0"); // 浊度 为0
					sonList.add("0"); // 流速 为0

				}

				par1.put("list", sonList);
				allData.add(par1);
			}

		}
		return allData;
	}

	
	/**
	 * 获取井盖总个数与开启, 关闭的个数
	 */
	public  void numCount(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceType", MANHOLECOVER_VALUE);
		//井盖总数
		Long allNum=deviceInfosService.countList(params);	
		//井盖开  的个数
		Long openNum=0L;
		params.put("alertFlag", "1");
		openNum=deviceCollectAlertService.countList(params);
		//井盖关的个数
		Long clossNum=0L;
		params.put("alertFlag", "0");
		clossNum=deviceCollectAlertService.countList(params);
		
	}
	
}
