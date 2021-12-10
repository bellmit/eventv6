package cn.ffcs.zhsq.map.arcgis.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.common.message.bo.NewMessageBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceCollectAlertService;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceCollectDataService;
import cn.ffcs.zhsq.deviceinfos.service.DeviceInfosService;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectAlert;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceCollectData;
import cn.ffcs.zhsq.mybatis.domain.deviceinfos.DeviceInfos;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.JsonUtils;

/**
 * 
 * 2017-10-13
 * arcgis 设备相关 定位数据加载控制器
 * @author linzhu
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisDataOfDeviceController")
public class ArcgisDataOfDeviceController extends ZZBaseController {
	@Autowired 
	protected DeviceInfosService deviceInfosService;
	
	@Autowired
	private DeviceCollectAlertService deviceCollectAlertService;

	@Autowired
	private DeviceCollectDataService deviceCollectDataService;
	@Autowired
	private IBaseDictionaryService baseDictionaryService;//字典服务模块
	
	@Autowired
	private MessageOutService messageOutService;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ResponseBody
	@RequestMapping(value = "/fetchMsgForJsonp")
	public String fetchMsgForJsonp(HttpSession session, HttpServletRequest request) {
		String jsoncallback = request.getParameter("jsoncallback");
		 UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, String> params = new HashMap<String, String>();
		params.put("moduleCode", "99");
		params.put("messageStatus", "0");
		params.put("topN", "3");
		List<NewMessageBO> list = messageOutService.getMessageListByParas(userInfo.getUserId(), params);
		for (int i = 0; i < list.size(); i++) {
			NewMessageBO messageBO = list.get(i);
			messageBO.setSendTimeStr(sdf.format(messageBO.getSendTime()));
		}
		jsoncallback = jsoncallback + "(" + JsonHelper.getJsonString(list) + ")";
		return jsoncallback;
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
			@RequestParam(value = "noImgSet", required=false) String noImgSet,
			@RequestParam(value = "markerType") String markerType
			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		markerType=ConstantValue.MARKER_TYPE_DEVICE;
		List<ArcgisInfoOfPublic> list =this.deviceInfosService.getArcgisDeviceInfosDataListByIds(ids,mapt,markerType);
		if(noImgSet!=null){
			return list;
		}
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
				params.put("bizType", "00");
				list.get(i).setBizType("device"); 
				if(!StringUtils.isEmpty(deviceInfo.getDeviceType()) && ConstantValue.DEVICE_LIGHTING.endsWith(deviceInfo.getDeviceType())){//路灯
					//获取设备属性数据
					List<DeviceCollectData> dataList=deviceCollectDataService.findList(params);
					for (DeviceCollectData deviceCollectData : dataList) {
						if(deviceCollectData.getCollectItemCode().equals("lighting_status")){
							String lighting_status=deviceCollectData.getCollectItemValue();
							//照明状态 0：开，1：关
							if(!StringUtils.isEmpty(lighting_status)&&lighting_status.equals("1")) {
								list.get(i).setElementsCollectionStr(elementsCollectionStr.replaceAll(".png", "_close.png"));
							} 
						}
					}
				}else{
					List<DeviceCollectAlert>  alertList=deviceCollectAlertService.findList(params);
					if(alertList!=null && alertList.size()>0){
						DeviceCollectAlert dCollectAlert=alertList.get(0);
						//'告警标记，0：不告警；1：告警
						 if(dCollectAlert!=null && !StringUtils.isEmpty(dCollectAlert.getAlertFlag()) && dCollectAlert.getAlertFlag().equals("1")){
							list.get(i).setElementsCollectionStr(elementsCollectionStr.replaceAll(".png", "_alert.png"));
						 }
					}
				}
				
			}
		}
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
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,@RequestParam(value = "manufacturersNo", required=false) String manufacturersNo,
			@RequestParam(value = "deviceType", required=false) String deviceType) {
//        List<Map<String, Object>> type = dictionaryService.getTableColumnDC(ConstantValue.T_ZZ_CASES, ConstantValue.COLUMN_CASES_TYPE, null);
		//List<BaseDataDict> standardSwjc = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.DEVICE_LIGHTING, null);
//        map.addAttribute("TYPE_", standardSwjc);
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("deviceType", deviceType);
		map.addAttribute("manufacturersNo", manufacturersNo);
		String forward ="/map/arcgis/standardmappage/standardDevice.ftl";
		return forward;
	}
	
	/**
	 * 设备信息列表页面
	 * @param session
	 * @param map
	 * @param gridId
	 * @return
	 */
	@RequestMapping(value = "/standardDeviceParkinglot")
	public String standardDeviceParkinglot(HttpSession session, ModelMap map, @RequestParam(value = "gridId") long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			@RequestParam(value = "manufacturersNo", required=false) String manufacturersNo) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("manufacturersNo", manufacturersNo);
		return "/map/arcgis/standardmappage/standardDeviceParkinglot.ftl";
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
				    @RequestParam(value = "manufacturersNo", required = false) String manufacturersNo,
				    @RequestParam(value = "deviceName", required = false) String deviceName,
				    @RequestParam(value = "gridId", required = false) Long gridId) {

	    if (page <= 0) page = 1;
	    //Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);	   
        Map<String, Object> params = new HashMap<String, Object>();
        /*if(StringUtils.isEmpty(deviceType)){
        	deviceType=ConstantValue.DEVICE_LIGHTING;
        }*/
        params.put("manufacturersNo", manufacturersNo);
        params.put("deviceType", deviceType);
        params.put("deviceName", deviceName); 
       /* if(gridId==null){  
        	//默认网格设置
    		gridId = (Long) defaultGridInfo.get(KEY_START_GRID_ID);
        }*/
        //params.put("gridId",gridId);
        EUDGPagination pagination = deviceInfosService.searchList(page, rows, params);
        return pagination;
    }
	/**
	 * 单条资源数据的查看
	 * @param session
	 * @param resId 
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/detail")
	public String showResourceDetail(HttpSession session, @RequestParam(value="deviceId") Long deviceId, ModelMap map) {
		DeviceInfos deviceInfo = deviceInfosService.searchById(deviceId);
		map.addAttribute("deviceInfo", deviceInfo);
		//告警
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceServiceId", deviceInfo.getDeviceServiceId());
		params.put("bizType", "00");
		List<DeviceCollectAlert>  alertList=deviceCollectAlertService.findList(params);
		if(alertList!=null && alertList.size()>0){
			//获取告警的数据
			DeviceCollectAlert  deviceCollectAlert=alertList.get(0);
			String jsonStr=deviceCollectAlert.getCollectAlertJsonStr();
			Map<String, Object> itemMap=JsonUtils.json2Map(jsonStr);
			map.addAttribute("deviceCollectAlert",deviceCollectAlert);
			map.addAttribute("itemMap",itemMap);
			map.addAttribute("alertFlag",deviceCollectAlert.getAlertFlag());
			//记录时间
			map.addAttribute("collTime",deviceCollectAlert.getCollTime());
		}
		//获取设备属性数据
		List<DeviceCollectData> dataList=deviceCollectDataService.findList(params);
		map.addAttribute("dataList",dataList);
		//取其中的一条记录时间
		if(dataList!=null && dataList.size()>0){
			//记录时间
			map.addAttribute("collTime",dataList.get(0).getCollTime());
		}
		return "/map/arcgis/standardmappage/deviceInfoDetail.ftl";
	}
	/**
	 * 设备告警详情
	 * @param session
	 * @param msgId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/alertDeviceDetail")
	public String alertDetail(HttpSession session, @RequestParam(value="msgId") Long msgId, ModelMap map) {
		//当前登录用户信息
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		DeviceInfos bo =null;
		Map<String, Object> msgDevice=null;
		if (msgId != null) {
			msgDevice=deviceInfosService.searchByMsgId(msgId);
			if(msgDevice!=null&&msgDevice.get("DEVICE_ID")!=null){
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("deviceServiceId", msgDevice.get("DEVICE_ID"));
				List<DeviceInfos> list= deviceInfosService.findList(params);
				if(list.size()>0)
					bo=list.get(0);
			}
			
		}
		map.put("bo", bo);
		map.put("msgDevice", msgDevice);
		//获取数据字典中的设备类型
		List<BaseDataDict> deviceTypeDD = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.DICT_DEVICE_TYPE, userInfo.getOrgCode());
		map.addAttribute("deviceTypeDD", deviceTypeDD);
		return "/map/arcgis/standardmappage/device_alert_msg.ftl";
	}
	/**
	 * 路灯开关
	 * @param request
	 * @param session
	 * @param map
	 * @param deviceId照明状态 0：开，1：关
	 * @param lightingStatus
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/forcSwitch")
	public Object forcSwitch(String deviceId,String lightingStatus) {
		String url="http://27.155.67.52:10001/iot/platform/v1.0.0/lighting/instructions?device_id="+deviceId+"&lighting_status="+lightingStatus;
		String	params="";//"{\"device_id\":\""+deviceId+"\",\"lighting_status\":\""+lightingStatus+"\"}";////照明状态 0：开，1：关
		String jsonStr=HttpUtil.jsonPost(url, params);
		if(jsonStr==null){
			jsonStr="{\"res_code\":-4,\"res_message\":\"出错了\"}";
		}else{
			jsonStr=HttpUtil.jsonPost(url, params);
		}
		return     JSONObject.fromObject(jsonStr);

	}
	@ResponseBody
	@RequestMapping(value="/test")
	public Map<String, Object>  test(HttpSession session,  String  regionCode, String bizType,String startTime,String endTime) {
		Map<String, Object> map =new HashMap<String, Object>();
		// deviceCollectDataService.getChargingPileList(regionCode, bizType);
		 Map<String, Object> params =new HashMap<String, Object>();
		 params.put("startTime",startTime);
		 params.put("endTime", endTime);
		 params.put("rows", "20");
		 params.put("bizType", bizType);
		 params.put("regionCode", regionCode);
		 List<Map<String, Object>> list=null;//	 deviceCollectDataService.findAccessControlList(params);
		 map.put("list", deviceCollectDataService.findAccessControlList(params));
		 params.put("oper", "car");
		 list=	 deviceCollectDataService.findAccessControlList(params);
		 map.put("carList", list);
		 map.put("LightingCount", deviceInfosService.getLightingCountBYStatus(params));
//		 deviceCollectDataService.getEnvMonitoringHisMap(regionCode, bizType);
		return  map;
	}
	
}
