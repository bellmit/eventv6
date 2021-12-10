package cn.ffcs.zhsq.szzg.aqi.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;



import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.lang.StringUtils;

import cn.ffcs.zhsq.mybatis.domain.szzg.aqi.ZgAQI;
import cn.ffcs.zhsq.szzg.aqi.service.IZgAQIService;
import cn.ffcs.zhsq.utils.ConstantValue;

@Controller
@RequestMapping(value = "/zhsq/szzg/aqi")
public class AqiController {
	@Autowired
	private IZgAQIService zgAQIService;
	// 模块路径
	private final static String REAL_PATH = "/szzg/";
	// 模块名称
	private final static String SUB_MAIN = "aqi";
	private DateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH时");
	   /**
     * 首页
     *
     * @param map
     * @return
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request,HttpSession session, ModelMap model)throws Exception {
    	String selType = request.getParameter("selType");
    	String type = request.getParameter("type");
    	String statioId = request.getParameter("statioId");
    	model.addAttribute("statioId",statioId);
    	if(StringUtils.isEmpty(selType))
    		selType="kqzl-1";
    	if ("kqzl-1".equals(selType)) {//空气质量
			Map map = find_kq(model,statioId);
			model.putAll(map);
		} else if ("kqzl-2".equals(selType)) {
			String statioid = request.getParameter("statioid");
			Map map = find_pic(statioid, model);
			model.putAll(map);
		} 
    	Enumeration enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			model.addAttribute(paraName,request.getParameter(paraName));
		}
		if (StringUtils.isNotBlank(type) && type.equals("kq")) {
			return REAL_PATH + SUB_MAIN + "/yp_big_screen_right" + ".ftl";
		}
    	return REAL_PATH + SUB_MAIN + "/index_" + SUB_MAIN + ".ftl";
    } 
	
    @ResponseBody
    @RequestMapping(value = "/jsonData")
    public Map<String, Object> jsonData(HttpServletRequest request,HttpSession session, ModelMap model)throws Exception {
    	Map<String, Object> map =null;
    	String selType = request.getParameter("selType");
    	String statioId = request.getParameter("statioid");
    	if(StringUtils.isEmpty(selType))
    		selType="kqzl-1";
    	if ("kqzl-1".equals(selType)) {
			 map = find_kq(model,  statioId);
		} else if ("kqzl-2".equals(selType)) {
			 map = find_pic(statioId, model);
		} 
    	return map;
    }
    
	public Map<String, Object> find_kq(ModelMap model, String statioId)
			throws JsonGenerationException, JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		map.put("statioId", statioId!=null?statioId:ConstantValue.AQI_CITY_STATION_ID);
		List<ZgAQI> kq_list = zgAQIService.findList(map);// 站点空气质量指数查询
		ZgAQI aqi_map = kq_list.remove(0);
		Date date = new Date();
		date.setHours(date.getHours() -1);
		String nowtime = df.format(date);
		map.put("aqi_map", mapper.writeValueAsString(aqi_map));
		map.put("kq_list", mapper.writeValueAsString(kq_list));
		map.put("nowtime", nowtime);
		return map;
	}
	
	public Map<String, Object> find_pic(String statioid, ModelMap model) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> hour_list = zgAQIService.getHour(statioid);
		List<String> hour_list_name = new ArrayList<String>();
		List<String> hour_list_value = new ArrayList<String>();
		for (Map<String, Object> hourMap : hour_list) {
			hour_list_name.add((String) hourMap.get("LABEL"));
			String value = (String) hourMap.get("VALUE") == null ? "0"
					: (String) hourMap.get("VALUE");
			hour_list_value.add(value);
		}
		List<Map<String, Object>> day_list = zgAQIService.getDay(statioid);
		List<String> day_list_name = new ArrayList<String>();
		List<String> day_list_value = new ArrayList<String>();
		for (Map<String, Object> dayMap : day_list) {
			day_list_name.add((String) dayMap.get("LABEL"));
			String value = (String) dayMap.get("VALUE") == null ? "0"
					: (String) dayMap.get("VALUE");
			day_list_value.add(value);
		}
		try {
			// map.put("hour_list", mapper.writeValueAsString(hour_list));
			// map.put("day_list", mapper.writeValueAsString(day_list));
			map.put("hour_list_name", mapper.writeValueAsString(hour_list_name));
			map.put("hour_list_value",
					mapper.writeValueAsString(hour_list_value));
			map.put("day_list_name", mapper.writeValueAsString(day_list_name));
			map.put("day_list_value", mapper.writeValueAsString(day_list_value));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
