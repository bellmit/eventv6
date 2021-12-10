package cn.ffcs.zhsq.pointInfo.controller;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.mybatis.domain.pointinfo.PointInfo;
import cn.ffcs.zhsq.pointInfo.service.PointInfoService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.JXLExcelUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**   
 * @Description: 设备点位信息表模块控制器
 * @Author: tangheng
 * @Date: 09-16 16:27:18
 * @Copyright: 2019 福富软件
 */ 
@Controller("pointInfoController")
@RequestMapping("/zhsq/event/pointInfo")
public class PointInfoController extends ZZBaseController {

	@Autowired
	private PointInfoService pointInfoService; //注入设备点位信息表模块服务
	@Autowired
	private IFunConfigurationService funConfigurationService;// 功能配置
	
	private SimpleDateFormat df = new SimpleDateFormat(DateUtils.PATTERN_24TIME);
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/index")
	public Object index(HttpServletRequest request, HttpSession session, ModelMap map) {
        Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
        map.addAttribute("gridId", defaultGridInfo.get(KEY_START_GRID_ID));
        map.addAttribute("regionCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
        return "/zzgl/pointinfo/list_pointInfo.ftl";
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listData")
	public Object listData(HttpServletRequest request, PointInfo bo, ModelMap map,
                           int page, int rows) {
		page = page < 1 ? 1 : page;
		rows = rows < 1 ? 20 : rows;
		Map<String, Object> params = new HashMap<String, Object>();
        String regionCode = bo.getRegionCode();
        if(regionCode==null || regionCode.toString().equals("")){
            Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(request.getSession());
            params.put("regionCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
        }else{
            params.put("regionCode",bo.getRegionCode());
        }
        params.put("deviceStatus",bo.getDeviceStatus());
        params.put("deviceName",bo.getDeviceName());
        EUDGPagination pagination = pointInfoService.searchList(page, rows, params);
		return pagination;
	}

	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping("/listAllData")
	public Object listAllData(HttpServletRequest request, PointInfo bo, ModelMap map) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceStatus",bo.getDeviceStatus());
		EUDGPagination pagination = pointInfoService.searchAllList(params);
		return pagination;
	}

    @RequestMapping(value = "/showVideo")
    public Object showVideo(HttpSession session, ModelMap map, HttpServletResponse res, Long id) throws Exception {
        PointInfo pointInfo = pointInfoService.searchById(id);
        //String token = pointInfoService.getToken();
        //String token = (String) ArcgisDataOfGoodsController.getToken();
        String position =String.valueOf( pointInfo.getDeviceCid());
        if(StringUtils.isNotBlank(position)) {
            Map<String,Object> tempMap = new HashMap<String,Object>();
            String hls = pointInfoService.getVideoUrlByCid(null,position);
            map.put("hls", hls);
        }
        return "/map/arcgis/standardmappage/equipforvideo/videoView.html";
    }

	/**
	 * 详情页面
	 */
	@RequestMapping("/detail")
	public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		PointInfo bo = pointInfoService.searchById(id);
		map.addAttribute("bo", bo);
		return "/zzgl/pointInfo/detail_pointInfo.ftl";
	}

	/**
	 * 表单页面
	 */
	@RequestMapping("/form")
	public Object form(HttpServletRequest request, HttpSession session, ModelMap map,
		Long id) {
		if (id != null) {
			PointInfo bo = pointInfoService.searchById(id);
			map.put("bo", bo);
		}
		return "/zzgl/pointInfo/form_pointInfo.ftl";
	}

	/**
	 * 保存数据
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
		PointInfo bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		if (bo.getGridPath() == null) { //新增
			Long id = pointInfoService.insert(bo);
			if (id != null && id > 0) {
				result = "success";
			};
		} else { //修改
			boolean updateResult = pointInfoService.update(bo);
			if (updateResult) {
				result = "success";
			}
		}
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 删除数据
	 */
	@ResponseBody
	@RequestMapping("/del")
	public Object del(HttpServletRequest request, HttpSession session, ModelMap map,
		PointInfo bo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = "fail";
		boolean delResult = pointInfoService.delete(bo);
		if (delResult) {
			result = "success";
		}
		resultMap.put("result", result);
		return resultMap;
	}

    /**
     * 办理
     */
    @ResponseBody
    @RequestMapping("/handle")
    public Object handle(HttpServletRequest request, HttpSession session, ModelMap map,
                         Long id) {
        pointInfoService.synchroDevicePoint();
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("x","115.266066000000000");
        param.put("y","25.323784000000000");
        param.put("distance","100");
        Long num =  pointInfoService.countPointZhouBian(param);
        return "success";
    }
    
    
    @RequestMapping("/index_alarms")
    public Object indexAlarms(HttpServletRequest request, HttpSession session, 
    		ModelMap map,@RequestParam(value="__moduleName",required=false)String __moduleName) {
    	if(StringUtils.isBlank(__moduleName)) {
    		__moduleName = "垃圾暴露";
    	}
    	map.put("__moduleName", __moduleName);
        return "/zzgl/pointinfo/alarms/alarms_index.ftl";
    }
   /*
    @RequestMapping("/detail_alarms")
    public Object detailAlarms(HttpServletRequest request, HttpSession session,
    		ModelMap map,String id) {
    	Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
    	String infoOrgCode = (String) defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
    	if (infoOrgCode.startsWith("360423")) {//武宁
    		Map<String, Object> alarms = pointInfoService.searchMyAlarmsById(id);
    		if (CommonFunctions.isNotBlank(alarms, "imageUrl")) {
    			String imgUrl = funConfigurationService.changeCodeToValue("APP_DOMAIN", "$IMG_DOMAIN",
    					IFunConfigurationService.CFG_TYPE_URL);
    			alarms.put("imageUrl", imgUrl+alarms.get("imageUrl").toString());
			}
    		map.put("bo", alarms);
		}else {
			map.put("bo", pointInfoService.searchAlarmsById(id));
		}
        return "/zzgl/pointinfo/alarms/alarms_detail.ftl";
    }*/
    
    /**
     *    告警信息列表
     *//*
    @ResponseBody
    @RequestMapping("/queryAlarms")
    public Object queryAlarms(HttpServletRequest request, HttpSession session, ModelMap map,
    		int page, int rows, @RequestParam Map<String,Object> params) {
    	
    	EUDGPagination searchAlarmsList = null;
    	Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
    	String infoOrgCode = (String) defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE);
    	if (infoOrgCode.startsWith("360423")) {//武宁
    		
			try {
				if (CommonFunctions.isNotBlank(params, "startTime")) {
					Date date = df.parse(params.get("startTime").toString() + " 00:00:00");
					params.put("startTime", date.getTime());
				}
				if (CommonFunctions.isNotBlank(params, "endTime")) {
					Date date = df.parse(params.get("endTime").toString() + " 23:59:59");
					params.put("endTime", date.getTime());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
    		searchAlarmsList = pointInfoService.searchMyAlarmsList(page, rows, params);
		}else {
			searchAlarmsList = pointInfoService.searchAlarmsList(page, rows, params);
		}
    	
    	return searchAlarmsList;
    }
    */

    /**
     * 导出数据
     * */
    @RequestMapping(value = "/exportData")
    public void exportData(HttpSession session,HttpServletResponse response,
                           HttpServletRequest request, PointInfo bo,
                           @RequestParam(value = "reportType", required = false, defaultValue = "1") Integer reportType
                           ){
        List<Map<String,Object>> dataList = null;
        String[] titles = null;
        String[] columns =null;
        Integer[] columnWidths = null;
        String excelName="智慧云眼信息表";
        Map<String, Object> params = new HashMap<>();
        String regionCode = bo.getRegionCode();
        if(regionCode==null || regionCode.toString().equals("")){
            Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(request.getSession());
            params.put("regionCode", defaultGridInfo.get(KEY_DEFAULT_INFO_ORG_CODE));
        }else{
            params.put("regionCode",bo.getRegionCode());
        }
        params.put("deviceStatus",bo.getDeviceStatus());
        params.put("deviceName",bo.getDeviceName());
        try {
            dataList = pointInfoService.exportDeviceList(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        titles=new String[] { "网格名称","设备名称","设备id", "设备纬度","设备纬度","状态"};
        columns = new String[] { "GRID_PATH","DEVICE_NAME","DEVICE_ID","LATITUDE", "LONGITUDE","DSTATUS"};
        columnWidths = new Integer[] { 35, 35, 35,30, 30, 30};
        
        JXLExcelUtil t= new JXLExcelUtil<Map<String, Object>>();
        Map map = new HashMap();
        map.put("list", dataList);
        map.put("columns", columns);
        map.put("titles", titles);
        map.put("widths", columnWidths);
        map.put("name", excelName);
        t.buildExcelDocument(map, request, response);
    }
}