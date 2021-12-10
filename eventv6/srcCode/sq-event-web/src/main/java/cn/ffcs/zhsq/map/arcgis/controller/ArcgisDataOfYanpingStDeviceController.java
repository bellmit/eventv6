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

import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceHistoryDataService;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceRunningDataService;
import cn.ffcs.zhsq.deviceinfos.service.DeviceInfosService;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceRunningData;
import cn.ffcs.zhsq.mybatis.domain.deviceinfos.DeviceInfos;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.JsonUtils;

/**
 * 延平生态
 * @author husp
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisDataOfYanpingStDeviceController")
public class ArcgisDataOfYanpingStDeviceController extends ZZBaseController {
	@Autowired 
	protected DeviceInfosService deviceInfosService;//设备信息接口
	@Autowired
	private DeviceRunningDataService deviceRunningDataService;//设备最新数据接口
	@Autowired
	private DeviceHistoryDataService deviceHistoryDataService;//注入监测历史数据接口
	
	/**
	 * 查看设备详情
	 * @param session
	 * @param resId 
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/detail")
	public String showResourceDetail(HttpSession session, @RequestParam(value="deviceId") Long deviceId, ModelMap map) {
		DeviceInfos deviceInfo = deviceInfosService.searchById(deviceId);
		map.addAttribute("deviceInfo", deviceInfo);
		//设备运行数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceServiceId", deviceInfo.getDeviceServiceId());
		params.put("bizType", "01");
		List<DeviceRunningData>  rundataList=deviceRunningDataService.findList(1, 1, params);
		if(rundataList!=null && rundataList.size()>0){
			//获取告警的数据
			DeviceRunningData  deviceRunningData=rundataList.get(0);
			String jsonStr=deviceRunningData.getRunningDataJsonStr();
			Map<String, Object> itemMap=JsonUtils.json2Map(jsonStr);
			map.addAttribute("deviceRunningData",deviceRunningData);
			map.addAttribute("itemMap",itemMap);
			map.addAttribute("alertFlag",deviceRunningData.getAlertFlag());
			map.addAttribute("collTime",deviceRunningData.getCollectTime());
		}
		return "/map/arcgis/standardmappage/devicecollectdata/detail_yanping_shengtai.ftl";
	}
	
	/**
	 * 进入空气历史记录页面
	 * @param session
	 * @param deviceId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/airHistoryList")
	public String airHistoryList(HttpSession session,@RequestParam(value="deviceId") Long deviceId, ModelMap map) {
		DeviceInfos deviceInfo = deviceInfosService.searchById(deviceId);
		map.addAttribute("deviceInfo", deviceInfo);
		return "/map/arcgis/standardmappage/devicecollectdata/more_air_yanping.ftl";
	}
	/**
	 * 空气历史列表记录
	 * @param request
	 * @param deviceServiceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findAirHistoryList")
	public Object findAirHistoryList(HttpServletRequest request,
			@RequestParam(value="deviceServiceId",required=false) String deviceServiceId,
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows,
			String startTime,String endTime){
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("deviceServiceId", deviceServiceId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		EUDGPagination pagination = deviceHistoryDataService.searchAirHistoryList(page, rows, params);
		return pagination;
	}
	/**
	 * 进入水历史记录页面
	 * @param session
	 * @param deviceId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/waterHistoryList")
	public String waterHistoryList(HttpSession session,@RequestParam(value="deviceId") Long deviceId, ModelMap map) {
		DeviceInfos deviceInfo = deviceInfosService.searchById(deviceId);
		map.addAttribute("deviceInfo", deviceInfo);
		return "/map/arcgis/standardmappage/devicecollectdata/more_water_yanping.ftl";
	}
	/**
	 * 水历史列表记录
	 * @param request
	 * @param deviceServiceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findWaterHistoryList")
	public Object findWaterHistoryList(HttpServletRequest request,
			@RequestParam(value="deviceServiceId",required=false) String deviceServiceId,
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows,
			String startTime,String endTime){
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("deviceServiceId", deviceServiceId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		EUDGPagination pagination = deviceHistoryDataService.searchWaterHistoryList(page, rows, params);
		return pagination;
	}
	/**
	 * 进入小流域历史记录页面
	 * @param session
	 * @param deviceId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/riverHistoryList")
	public String riverHistoryList(HttpSession session,@RequestParam(value="deviceId") Long deviceId, ModelMap map) {
		DeviceInfos deviceInfo = deviceInfosService.searchById(deviceId);
		map.addAttribute("deviceInfo", deviceInfo);
		return "/map/arcgis/standardmappage/devicecollectdata/more_river_yanping.ftl";
	}
	/**
	 * 小流域历史列表记录
	 * @param request
	 * @param deviceServiceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findRiverHistoryList")
	public Object findRiverHistoryList(HttpServletRequest request,
			@RequestParam(value="deviceServiceId",required=false) String deviceServiceId,
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows,
			String startTime,String endTime){
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("deviceServiceId", deviceServiceId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		EUDGPagination pagination = deviceHistoryDataService.searchRiverHistoryList(page, rows, params);
		return pagination;
	}
	
    /**
	 * 获取定位信息
	 * @param session
	 * @param ids ids
	 * @param mapt 地图类型
	 * @param showType 地图显示类型1、全显示，2、只显示当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfDeviceInfos")
	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfDeviceInfos(HttpSession session,
			ModelMap map, HttpServletResponse res,
			@RequestParam(value = "ids") String ids,
			@RequestParam(value = "mapt") Integer mapt,
			@RequestParam(value = "markerType") String markerType
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		markerType=ConstantValue.MARKER_TYPE_DEVICE;
		List<ArcgisInfoOfPublic> list =this.deviceInfosService.getArcgisDeviceInfosDataListByIds(ids,mapt,markerType);
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				
				DeviceInfos deviceInfo = deviceInfosService.searchById(list.get(i).getId());
				if(!StringUtils.isEmpty(list.get(i).getName()) && list.get(i).getName().length()>10){
					list.get(i).setName(list.get(i).getName().substring(0, 9));
				}
				list.get(i).getEventDisposal().setCode(deviceInfo.getDeviceServiceId());
				list.get(i).getEventDisposal().setOccurred(deviceInfo.getDeviceInstallAddress());
				Map<String, Object> params=new HashMap<String, Object>();
				params.put("deviceServiceId", deviceInfo.getDeviceServiceId());
				params.put("bizType", "01");
				list.get(i).setBizType("device"); 
				List<DeviceRunningData>  rundataList=deviceRunningDataService.findList(1, 1, params);
				if(rundataList!=null && rundataList.size()>0){
					DeviceRunningData  deviceRunningData=rundataList.get(0);
					//'告警标记，0：不告警；1：告警
					 if(deviceRunningData!=null && !StringUtils.isEmpty(deviceRunningData.getAlertFlag()) && deviceRunningData.getAlertFlag().equals("1")){
						list.get(i).setElementsCollectionStr(elementsCollectionStr.replaceAll(".png", "_alert.png"));
					 }
				}
			}
		}
		return list;
	}
	
	
	
	/**
	 * 地图图层设备信息列表页面
	 * @param session
	 * @param map
	 * @param gridId
	 * @return
	 */
	@RequestMapping(value = "/standardDevice")
	public String standardDevice(HttpSession session, ModelMap map, @RequestParam(value = "gridId") long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			@RequestParam(value = "deviceType", required=false) String deviceType) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("deviceType", deviceType);
		String forward ="/map/arcgis/standardmappage/standardDevice_yanping.ftl";
		return forward;
	}
	
	/**
	 * 包含告警数据的设备详情页面
	 * @param request
	 * @param session
	 * @param deviceId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/showDetail")
	public String showResourceDetail(HttpServletRequest request,HttpSession session, @RequestParam(value="deviceId") Long deviceId, ModelMap map) {
		DeviceInfos deviceInfo = deviceInfosService.searchById(deviceId);
		map.addAttribute("deviceInfo", deviceInfo);
		//设备运行数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceServiceId", deviceInfo.getDeviceServiceId());
		params.put("bizType", "01");
		List<DeviceRunningData>  rundataList=deviceRunningDataService.findList(1, 1, params);
		if(rundataList!=null && rundataList.size()>0){
			
			DeviceRunningData  deviceRunningData=rundataList.get(0);
			//获取采集的数据
			String jsonStr=deviceRunningData.getRunningDataJsonStr();
			Map<String, Object> itemMap=JsonUtils.json2Map(jsonStr);
			map.addAttribute("deviceRunningData",deviceRunningData);
			map.addAttribute("itemMap",itemMap);
			map.addAttribute("alertFlag",deviceRunningData.getAlertFlag());
			map.addAttribute("collTime",deviceRunningData.getCollectTime());
			//获取告警的数据
			jsonStr=deviceRunningData.getAlertDataJsonStr();
			Map<String, Object> alertMap=new HashMap<String, Object>();
			if(jsonStr!=null&&!jsonStr.equals("{}")){
				alertMap=JsonUtils.json2Map(jsonStr);
			}
			map.addAttribute("alertMap",alertMap);
		}
		return "/map/arcgis/standardmappage/devicecollectdata/detail_yanping_device.ftl";
	}
	
	/**
	 * 进入井盖历史记录页面
	 * @param session
	 * @param deviceId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/coverHistoryList")
	public String coverHistoryList(HttpSession session,@RequestParam(value="deviceId") Long deviceId, ModelMap map) {
		DeviceInfos deviceInfo = deviceInfosService.searchById(deviceId);
		map.addAttribute("deviceInfo", deviceInfo);
		return "/map/arcgis/standardmappage/devicecollectdata/more_cover_yanping.ftl";
	}
	/**
	 * 井盖历史列表记录
	 * @param request
	 * @param deviceServiceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findCoverHistoryList")
	public Object findCoverHistoryList(HttpServletRequest request,
			@RequestParam(value="deviceServiceId",required=false) String deviceServiceId,
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows,
			String startTime,String endTime){
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("deviceServiceId", deviceServiceId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		EUDGPagination pagination = deviceHistoryDataService.searchCoverHistoryList(page, rows, params);
		return pagination;
	}
	
	/**
	 * 进入垃圾桶历史记录页面
	 * @param session
	 * @param deviceId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/trashHistoryList")
	public String trashHistoryList(HttpSession session,@RequestParam(value="deviceId") Long deviceId, ModelMap map) {
		DeviceInfos deviceInfo = deviceInfosService.searchById(deviceId);
		map.addAttribute("deviceInfo", deviceInfo);
		return "/map/arcgis/standardmappage/devicecollectdata/more_trash_yanping.ftl";
	}
	/**
	 * 垃圾桶列表记录
	 * @param request
	 * @param deviceServiceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findTrashHistoryList")
	public Object findTrashHistoryList(HttpServletRequest request,
			@RequestParam(value="deviceServiceId",required=false) String deviceServiceId,
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows,
			String startTime,String endTime){
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("deviceServiceId", deviceServiceId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		EUDGPagination pagination = deviceHistoryDataService.searchTrashHistoryList(page, rows, params);
		return pagination;
	}
}
