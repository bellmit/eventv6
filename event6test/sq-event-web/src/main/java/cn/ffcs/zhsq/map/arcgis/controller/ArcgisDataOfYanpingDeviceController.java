package cn.ffcs.zhsq.map.arcgis.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.shequ.utils.StringUtils;
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
 * 
 * 2017-10-13
 * arcgis 延平设备相关 定位数据加载控制器
 * @author linzhu
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisDataOfYanpingDeviceController")
public class ArcgisDataOfYanpingDeviceController extends ZZBaseController {
	@Autowired 
	protected DeviceInfosService deviceInfosService;
	
	@Autowired
	private DeviceRunningDataService deviceRunningDataService;//设备最新数据接口
	
	@Autowired
	private DeviceHistoryDataService deviceHistoryDataService;//注入监测历史数据接口
	
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
		return list;
	}
	
	
	
	/**
	 * 设备信息列表页面
	 * @param session
	 * @param map
	 * @param gridId
	 * @return
	 */
	@RequestMapping(value = "/standardDevice")
	public String standardDevice(HttpSession session, ModelMap map, @RequestParam(value = "gridId") long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			@RequestParam(value = "deviceType", required=false) String deviceType) {
//        List<Map<String, Object>> type = dictionaryService.getTableColumnDC(ConstantValue.T_ZZ_CASES, ConstantValue.COLUMN_CASES_TYPE, null);
		//List<BaseDataDict> standardSwjc = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.DEVICE_LIGHTING, null);
//        map.addAttribute("TYPE_", standardSwjc);
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("deviceType", deviceType);
		String forward ="/map/arcgis/standardmappage/yanpingDevice.ftl";
		return forward;
	}
	
	
	/**
     * 设备信息--列表数据
     * @param session
     * @param page
     * @param rows
     * @param gridId
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/listData", method= RequestMethod.POST)
    public EUDGPagination listData(
				    HttpSession session, @RequestParam(value = "page") int page,
				    @RequestParam(value = "rows") int rows,
				    @RequestParam(value = "deviceType", required = false) String deviceType,
				    @RequestParam(value = "deviceName", required = false) String deviceName,
				    @RequestParam(value = "gridId", required = false) Long gridId) {

	    if (page <= 0) page = 1;
	    Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);	   
        Map<String, Object> params = new HashMap<String, Object>();
        if(StringUtils.isEmpty(deviceType)){
        	deviceType=ConstantValue.DEVICE_LIGHTING;
        }
        params.put("deviceType", deviceType);
        params.put("deviceName", deviceName); 
        if(gridId==null){  
        	//默认网格设置
    		gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
        }
        //params.put("gridId",gridId);
        EUDGPagination pagination = deviceInfosService.searchList(page, rows, params);
        return pagination;
    }
	
	/**
	 * 单条资源数据的查看(智慧农业)
	 * @param session
	 * @param resId 
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/detailOfPlace")
	public String showResourceDetail2(HttpSession session, @RequestParam(value="deviceId") Long deviceId, ModelMap map) {
		
		DeviceInfos deviceInfos = deviceInfosService.searchById(deviceId);
		String deviceServiceId = deviceInfos.getDeviceServiceId();
		List<DeviceRunningData> dInfos=deviceRunningDataService.findByDeviceServiceId(deviceServiceId);
		List<Map<String, Object>> runData = new ArrayList<Map<String, Object>>();		
		
		for(DeviceRunningData vo : dInfos){
			Map<String, Object> item = JsonUtils.json2Map(vo.getRunningDataJsonStr());
			runData.add(item);
		}
		map.addAttribute("runData", runData);
		map.addAttribute("deviceInfos", deviceInfos);

		return "/map/arcgis/standardmappage/yanpingDeviceInfoDetail.ftl";
	}
	
	/**
	 * 进入农业设备历史记录页面
	 * @param session
	 * @param deviceId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/moreHistoryList")
	public String airHistoryList(HttpSession session,@RequestParam(value="deviceId") Long deviceId, ModelMap map) {
		DeviceInfos deviceInfo = deviceInfosService.searchById(deviceId);
		map.addAttribute("deviceInfo", deviceInfo);
		return "/map/arcgis/standardmappage/devicecollectdata/more_history_yanping.ftl";
	}
	/**
	 * 农业设备历史列表记录
	 * @param request
	 * @param deviceServiceId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findHistoryList")
	public Object findHistoryList(HttpServletRequest request,
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
		cn.ffcs.system.publicUtil.EUDGPagination pagination = deviceHistoryDataService.searchSensorHistoryList(page,rows,params);
		return pagination;
	}
}
