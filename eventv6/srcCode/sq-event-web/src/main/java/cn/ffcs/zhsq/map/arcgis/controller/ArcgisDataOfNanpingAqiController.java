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

import com.ctc.wstx.util.StringUtil;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.sms.bo.SendResult;
import cn.ffcs.sms.service.SendSmsService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfoOfPublic;
import cn.ffcs.zhsq.mybatis.domain.szzg.aqi.ZgAqiStation;
import cn.ffcs.zhsq.szzg.aqi.service.IZgAQIService;
import cn.ffcs.zhsq.szzg.aqi.service.IZgAqiStationService;
import cn.ffcs.zhsq.utils.AqiUtil;
import cn.ffcs.zhsq.utils.ConstantValue;
/**
 * 南平api定位数据加载控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/arcgisDataOfNanpingAqiController")
public class ArcgisDataOfNanpingAqiController {
	@Autowired 
	protected  IZgAqiStationService zgAqiStationService;
	
	@Autowired 
	protected  IZgAQIService zgAQIService;
	@Autowired
	private IResMarkerService resMarkerService;
	
	//短信发送
	@Autowired
	private SendSmsService sendSmsService;
	/**
	 * 站点列表页面
	 * @param session
	 * @param map
	 * @param gridId
	 * @return
	 */
	@RequestMapping(value = "/standardDevice")
	public String standardDevice(HttpSession session, ModelMap map, @RequestParam(value = "gridId") long gridId,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,@RequestParam(value = "manufacturersNo", required=false) String manufacturersNo,
			@RequestParam(value = "sources", required=false) String sources) {
		map.addAttribute("gridId", gridId);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		map.addAttribute("sources", sources);
		map.addAttribute("manufacturersNo", manufacturersNo);
		String forward ="/map/arcgis/standardmappage/nanping/standardDevice.ftl";
		return forward;
	}
	
	/**
	 * 站点信息--列表数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param sources
	 * @param stationname
	 * @param regionCode
	 * @param gridId
	 * @return
	 */
    @ResponseBody
    @RequestMapping(value="/listData")
    public EUDGPagination listData(
				    HttpSession session, @RequestParam(value = "page") int page,
				    @RequestParam(value = "rows") int rows,
				    @RequestParam(value = "sources", required = false) String sources,
				    @RequestParam(value = "showType", required = false) String showType,
				    @RequestParam(value = "stationname", required = false) String stationname,
				    @RequestParam(value = "regionCode", required = false) String regionCode) {

	    if (page <= 0) page = 1;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sources", sources);
        params.put("showType", showType);
        params.put("regionCode", regionCode);
        params.put("stationName", stationname); 
        params.put("alarmValue", 100); 
        EUDGPagination pagination = zgAqiStationService.searchList(page, rows, params);
        return pagination;
    }
    
    /**
     * 发送短信
     * @param session
     * @param mobilePhones
     * @param smsContent
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/sendsms")
    public Map<String, Object> sendsms(HttpSession session,@RequestParam(value = "mobilePhones") String mobilePhones,
    		@RequestParam(value = "smsContent") String smsContent) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String[] phoneNum = mobilePhones.split(",");
		Map<String, Object> params = new HashMap<String, Object>();
    	try{
			SendResult result = sendSmsService.sendSms(ConstantValue.SMS_PLATFORM_FLAG,
									userInfo.getUserId(), userInfo.getOrgCode(),
									phoneNum, smsContent, SendSmsService.TYPE_SMS, null,
									null);
			if(result != null){
				params.put("flag", result.getCode() == 0);
			}
		}catch(Exception e){
			e.printStackTrace();
			params.put("flag", false);
		}
    	return params;
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
   	@RequestMapping(value = "/getArcgisLocateDataListOfInfos")
   	public List<ArcgisInfoOfPublic> getArcgisLocateDataListOfInfos(HttpSession session,
   			ModelMap map, HttpServletResponse res,
   			@RequestParam(value = "ids") String ids,
   			@RequestParam(value = "mapt") Integer mapt,
   			@RequestParam(value = "noImgSet", required=false) String noImgSet,
   			@RequestParam(value = "queryType", required=false) String queryType,
   			@RequestParam(value = "markerType") String markerType
   			, @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
   		markerType=ConstantValue.MARKER_TYPE_AQI;
   		List<ArcgisInfoOfPublic> list =this.zgAqiStationService.getArcgisInfosDataListByIds(ids,mapt,markerType);
   		Map<String, Object> params = new HashMap<String, Object>();
   		params.put("sources", "1");
   		params.put("queryType", queryType);
   		EUDGPagination page=zgAqiStationService.searchList(1, 100, params);
   		if(noImgSet!=null){
   			return list;
   		}
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				//bizte必须加，不加就无法替换图标
				list.get(i).setBizType("api"); 
//				/list.get(i).setElementsCollectionStr(elementsCollectionStr);
				for (ZgAqiStation zgAqiStation : (List<ZgAqiStation>)page.getRows()) {
					if(zgAqiStation.getSeqid()==list.get(i).getId()){
//						if(zgAqiStation.getZgAQI().getAqi()!=null&&Integer.valueOf(zgAqiStation.getZgAQI().getAqi())>50){//   >=0 <=50 为优   51-100为良   默认图标是优
//							//替换告警图标
//							list.get(i).setElementsCollectionStr(elementsCollectionStr.replaceAll(".gif",AqiUtil.getStateByAqi(zgAqiStation.getZgAQI().getAqi())+".gif"));
//							continue;
//						}
						if(zgAqiStation.getZgAQI().getAqi()!=null&&Integer.valueOf(zgAqiStation.getZgAQI().getAqi())>100){
							//替换告警图标
							list.get(i).setElementsCollectionStr(elementsCollectionStr.replaceAll(".png",".gif" ));
							continue;
						}
						
					}
				}
				
			}
		} 
   		return list;
   	}
    
    
 
    /**
	 * 站点详情页面
	 * @param session
	 * @param map
	 * @param gridId
	 * @return
	 */
	@RequestMapping(value = "/monitorair")
	public String monitorair(HttpServletRequest request,HttpSession session, ModelMap map) {
		map.addAttribute("seqid", request.getParameter("seqid"));
		 Map<String, Object> params = new HashMap<String, Object>();
		params.put("seqid", request.getParameter("seqid"));
		ZgAqiStation  zgAqiStation=zgAqiStationService.searchById(params);
		if(zgAqiStation!=null){
			zgAqiStation.getZgAQI().setState(AqiUtil.getStateByAqi(zgAqiStation.getZgAQI().getAqi()));
			zgAqiStation.getZgAQI().setStateName(AqiUtil.getStateNameByAqi(zgAqiStation.getZgAQI().getAqi()));
		}
		map.put("zgAqiStation", zgAqiStation);
		
		
		List<ResMarker> resMarkers = resMarkerService.findResMarkerByParam(ConstantValue.MARKER_TYPE_AQI, zgAqiStation.getSeqid(), "5");
		ResMarker resMarker = new ResMarker();
		if(resMarkers.size() >= 1) {
			resMarker = resMarkers.get(0);
		}
		map.put("resMarker", resMarker);
		return "/map/arcgis/standardmappage/nanping/monitorair.ftl";
	}
	
	
	/**
	 * 根据站点获取日和24个小时数据
	 * @param request
	 * @param session
	 * @param sources
	 * @param mtype
	 * @return
	 */
    @ResponseBody
    @RequestMapping(value="/getDataListByStation")
    public List<Map<String,Object>>  getDataListByStation(
    		HttpServletRequest request,HttpSession session, 
				    @RequestParam(value = "statioId", required = false) String sources,
				    @RequestParam(value = "mtype", required = false) String mtype ) {
    	Map<String, Object> params=new HashMap<String, Object>();
    	params.put("statioId", request.getParameter("statioId"));
    	params.put("mtype", request.getParameter("mtype"));
    	String total=request.getParameter("total");
    	if(StringUtils.isEmpty(total)) {
    		total="25";//默认返回24条
    	}
    	params.put("total", total);
    	List<Map<String,Object>>  list = zgAqiStationService.getDataListByStation(params);
        return list;
    }
 
    
    /**
     * 图层提示框
     * @param request
     * @param session
     * @param map
     * @return
     */
	@RequestMapping(value = "/tip")
	public String tip(HttpServletRequest request,HttpSession session, ModelMap map) {
		map.addAttribute("seqid", request.getParameter("seqid"));
		 Map<String, Object> params = new HashMap<String, Object>();
		params.put("seqid", request.getParameter("seqid"));
		ZgAqiStation  zgAqiStation=zgAqiStationService.searchById(params);
		if(zgAqiStation!=null){
			zgAqiStation.getZgAQI().setState(AqiUtil.getStateByAqi(zgAqiStation.getZgAQI().getAqi()));
			zgAqiStation.getZgAQI().setStateName(AqiUtil.getStateNameByAqi(zgAqiStation.getZgAQI().getAqi()));
		}
		map.put("zgAqiStation", zgAqiStation);
		return "/map/arcgis/standardmappage/nanping/tip.ftl";
	}
	
	

}
