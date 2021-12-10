package cn.ffcs.zhsq.map.arcgis.controller.firePlatform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import cn.ffcs.zhsq.utils.gis.Gps;
import cn.ffcs.zhsq.utils.gis.GpsUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.json.JsonUtils;
import cn.ffcs.shequ.utils.HttpUtil;

import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 社区/重点微型消防站
 * @author zhangls
 *
 */
@Controller
@RequestMapping(value="/zhsq/map/arcgis/microFirehouse")
public class MicroFirehouseController extends ZZBaseController {
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	/**
	 * 跳转到社区/重点微型消防站列表
	 * @param session
	 * @param microFirehouseType	消防站类型
	 * 					0	社区微型消防站列表
	 * 					1	重点微型消防站列表
	 * @param elementsCollectionStr
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toMicroFirehouseList")
	public String toMicroFirehouseList(HttpSession session, 
			@RequestParam(value="microFirehouseType", required=false, defaultValue="0") int microFirehouseType,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			ModelMap map) {
		map.addAttribute("microFirehouseType", microFirehouseType);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		
		return "/map/arcgis/firePlatform/list_microFirehouse.ftl";
	}
	
	/**
	 * 跳转社区/重点微型消防站概要信息
	 * @param session
	 * @param params
	 * @param summaryData
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/toSummary")
	public String toSummary(HttpSession session, 
			@RequestParam Map<String, Object> params,
			@RequestParam(value="data", required=false) String summaryData,
			ModelMap map) {
		map.addAllAttributes(params);
		
		if(StringUtils.isNotBlank(summaryData)) {
			map.addAllAttributes(JsonUtils.json2Map(summaryData));
		}
		
		return "/map/arcgis/firePlatform/summary_microFirehouse.ftl";
	}
	
	/**
	 * 加载社区/重点微型消防站数据
	 * @param session
	 * @param page
	 * @param rows
	 * @param microFirehouseType	消防站类型
	 * 					0	社区微型消防站列表
	 * 					1	重点微型消防站列表
	 * @param elementsCollectionStr
	 * @param gridId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listMicroFirehouseData", method = RequestMethod.POST)
	public EUDGPagination listMicroFirehouseData(HttpSession session, 
			@RequestParam(value = "page") int page,
			@RequestParam(value = "rows") int rows, 
			@RequestParam(value="microFirehouseType", required=false, defaultValue="0") int microFirehouseType,
			@RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr,
			@RequestParam(value = "gridId", required = false, defaultValue = "-1") Long gridId) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		
		if (page <= 0) {
			page = 1;
		}
		
		if(gridId < 0) {
			Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
			
			gridId = Long.valueOf(defaultGridInfo.get(KEY_START_GRID_ID).toString());
		}
		
		List<Map<String, Object>> microFirehouseList = fetchMicroFirehouse(gridId.toString(), elementsCollectionStr, userInfo.getOrgCode(), microFirehouseType);
		
		EUDGPagination pagination = new EUDGPagination();
		
		if(microFirehouseList != null) {
			pagination = new EUDGPagination(microFirehouseList.size(), microFirehouseList);
		}
		
		return pagination;
	}
	
	/**
	 * 获取并解析对接数据
	 * @param gridId				网格id
	 * @param elementsCollectionStr
	 * @param orgCode				组织编码
	 * @param microFirehouseType	消防站类型
	 * 					0	社区微型消防站列表
	 * 					1	重点微型消防站列表
	 * @return
	 */
	private List<Map<String, Object>> fetchMicroFirehouse(String gridId, String elementsCollectionStr, String orgCode, int microFirehouseType) {
		String jsonStr = "",
			   httpUrl = "",
			   httpUrlTrigger = "";
		
		JSONObject microFirehouseJsonObject = null;
		String params = "";
		switch(microFirehouseType) {
			case 0: {//社区微型消防站
				if(gridId != null){
					params = params + "flag=1";
				}
				httpUrlTrigger = ConstantValue.COMMUNITY_FIRE_STATION;
				break;
			}
			case 1: {//重点微型消防站
				if(gridId != null){
					params = params + "flag=2";
				}
				httpUrlTrigger = ConstantValue.KEY_MINI_FIRE_STATION;
				break;
			}
		}
		
		httpUrl = funConfigurationService.turnCodeToValue(ConstantValue.FIRE_CONTROL_DOCKING_URL, httpUrlTrigger, IFunConfigurationService.CFG_TYPE_URL, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0);

		if(gridId != null){
			params = params + "&teamId=" + gridId;
//			params = params + "&teamId=204772";
		}
		if(StringUtils.isNotBlank(httpUrl)) {
//			microFirehouseJsonObject = HttpUtil.doBodyPost(httpUrl, gridId);
			microFirehouseJsonObject = HttpUtil.doBodyPost(httpUrl, params);
		}
		
		List<Map<String, Object>> resultMapList = null;
		if(microFirehouseJsonObject != null){
			if(isNotBlank(microFirehouseJsonObject, "childrendDutyFireTeams")) {
				Map<String, Object> resultMap = null, locationMap = null, summaryMap = null;
				resultMapList = new ArrayList<Map<String, Object>>();
				
				JSONArray jsonArray = microFirehouseJsonObject.getJSONArray("childrendDutyFireTeams");
				JSONObject childrenJsonObject = null, relTeamJsonObject = null;
				
				for(Object tempJsonObj : jsonArray) {
					childrenJsonObject = (JSONObject)tempJsonObj;
					resultMap = new HashMap<String, Object>();
					locationMap = new HashMap<String, Object>();
					summaryMap = new HashMap<String, Object>();
					Double x=0.0;
					Double y =0.0;
					if (childrenJsonObject.getString("positionX") != null) {//x
						x = Double.parseDouble(childrenJsonObject.getString("positionX"));
					}
					if (childrenJsonObject.getString("positionY") != null) {//y
						y = Double.parseDouble(childrenJsonObject.getString("positionY"));
					}
					if(x != null && y != null && x.doubleValue() != 0.0 && y.doubleValue() != 0.0){
						Gps gps = GpsUtil.bd09_To_Gps84(y, x);
						if(gps != null){
							x = gps.getWgLon();
							y = gps.getWgLat();
						}
					}
					if(x != null && x.doubleValue() != 0.0){
						resultMap.put("x", x);
						locationMap.put("x", x);
					}
					if(y != null&& y.doubleValue() != 0.0){
						resultMap.put("y", y);
						locationMap.put("y", y);
					}

					if(isNotBlank(childrenJsonObject, "relTeam")) {
						relTeamJsonObject = (JSONObject)childrenJsonObject.get("relTeam");
						summaryMap.put("gridName", relTeamJsonObject.get("name"));
					}
					if(isNotBlank(childrenJsonObject, "fireTeamName")) {
						resultMap.put("fireTeamName", childrenJsonObject.get("fireTeamName"));
						if(x!= null && y!= null && x != 0.0 && y!= 0.0) {
							locationMap.put("name", childrenJsonObject.get("fireTeamName"));
						}
						summaryMap.put("fireTeamName", childrenJsonObject.get("fireTeamName"));
					}

					if(isNotBlank(childrenJsonObject, "teamLeader")) {
						resultMap.put("teamLeader", childrenJsonObject.get("teamLeader"));
						summaryMap.put("teamLeader", childrenJsonObject.get("teamLeader"));
					}
					if(isNotBlank(childrenJsonObject, "tel")) {
						resultMap.put("tel", childrenJsonObject.get("tel"));
						summaryMap.put("tel", childrenJsonObject.get("tel"));
					}
					if(isNotBlank(childrenJsonObject, "id")) {
						resultMap.put("id", childrenJsonObject.get("id"));
						if(x!= null && y!= null && x != 0.0 && y!= 0.0) {
							locationMap.put("wid", childrenJsonObject.get("id"));
						}
					}
					if(isNotBlank(childrenJsonObject, "positionDesc")) {
						resultMap.put("positionDesc", childrenJsonObject.get("positionDesc"));
						summaryMap.put("positionDesc", childrenJsonObject.get("positionDesc"));
					}
					if(isNotBlank(childrenJsonObject, "equipment")) {
						summaryMap.put("equipment", childrenJsonObject.get("equipment"));
					}
					if(isNotBlank(childrenJsonObject, "teamMemberAmount")) {
						summaryMap.put("teamMemberAmount", childrenJsonObject.get("teamMemberAmount"));
					}
					if(isNotBlank(childrenJsonObject, "type")) {
						summaryMap.put("type", childrenJsonObject.get("type"));
						
						dictChange(summaryMap);
					}
					
					if(!resultMap.isEmpty()) {
						if(!locationMap.isEmpty()) {
							locationMap.put("reLoadSummaryData", false);
							locationMap.put("elementsCollectionStr", elementsCollectionStr);
							
							if(!summaryMap.isEmpty()) {
								locationMap.putAll(summaryMap);
							}
							
							resultMap.put("locationData", locationMap);
						}
						
						if(!summaryMap.isEmpty()) {
							summaryMap.put("reLoadSummaryData", false);
							
							resultMap.put("summaryData", summaryMap);
						}
						
						resultMapList.add(resultMap);
					}
				}
			}
			
		}
		
		return resultMapList;
	}
	
	/**
	 * 字典转换
	 * @param map
	 */
	private void dictChange(Map<String, Object> map) {
		String KEY_MICRO_FIREHOUSE_TYPE = "2",
			   KEY_MICRO_FIREHOUSE_TYPENAME = "重点微型消防站",
			   COMMUNITY_MICRO_FIREHOUSE_TYPENAME = "社区微型消防站";
		//type为2是重点微型消防站，否则是社区微型消防站
		if(CommonFunctions.isNotBlank(map, "type")) {
			String typeName = COMMUNITY_MICRO_FIREHOUSE_TYPENAME;
			
			if(KEY_MICRO_FIREHOUSE_TYPE.equals(map.get("type").toString())) {
				typeName = KEY_MICRO_FIREHOUSE_TYPENAME;
			}
			
			map.put("typeName", typeName);
		}
	}
	
	/**
	 * 判断jsonObject存在键值为key的属性
	 * @param jsonObject
	 * @param key
	 * @return 存在返回true，否则返回false
	 */
	private boolean isNotBlank(JSONObject jsonObject, String key) {
		boolean flag = false;
		
		if(jsonObject != null && StringUtils.isNotBlank(key)) {
			Object keyObj = jsonObject.get(key);
			
			flag = keyObj != null && !"null".equals(keyObj.toString());
		}
		
		return flag;
	}
}
