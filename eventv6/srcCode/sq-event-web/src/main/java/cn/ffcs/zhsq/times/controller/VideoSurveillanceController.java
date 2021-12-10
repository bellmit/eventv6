package cn.ffcs.zhsq.times.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.common.CrytoUtils;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.common.message.bo.ReceiverBO;
import cn.ffcs.resident.bo.CiRsTop;
import cn.ffcs.resident.service.CiRsService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.utils.APIHttpClient;
import cn.ffcs.shequ.utils.EncryptUtil;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.uam.bo.FunConfigureSetting;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.map.arcgis.service.IArcgisDataOfLocalService;
import cn.ffcs.zhsq.mybatis.domain.alarm.Device;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.ArcgisInfo;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;

@Controller
@RequestMapping("/zhsq/alarm/videoSurveillanceController")
public class VideoSurveillanceController extends ZZBaseController {
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IArcgisDataOfLocalService arcgisDataOfLocalService;
	@Autowired
	private CiRsService ciRsService;
	@Autowired
	private MessageOutService messageOutService;
	
	@ResponseBody
	@RequestMapping(value = "/getDevUrl")
	public Map<String, Object> getDevUrl(HttpSession session, HttpServletRequest request,
		 @RequestParam(value="sign") String sign,
		 @RequestParam(value="account") String account,
		 @RequestParam(value="app_key") String app_key,
		 @RequestParam(value="format") String format,
		 @RequestParam(value="method") String method,
		 @RequestParam(value="password") String password,
		 @RequestParam(value="sign_method") String sign_method,
		 @RequestParam(value="timestamp") String timestamp,
		 @RequestParam(value="v") String v,
		 @RequestParam(value="deviceNum") String deviceNum){
		
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String url = "http://192.168.21.63:8060/cms/router/rest?";
		FunConfigureSetting funConfigureSetting = funConfigurationService.findConfigureSettingLatest("GLOBALEYES_NANPING", "GLOBALEYES_URL", IFunConfigurationService.CFG_TYPE_URL, IFunConfigurationService.DEFAULT_ORG_CODE, IFunConfigurationService.DIRECTION_PARALLEL);
		if(funConfigureSetting != null && StringUtils.isNotBlank(funConfigureSetting.getCfgVal())){
			url = funConfigureSetting.getCfgVal();
		}

		Map<String, Object> paramMap = this.getGlobalEysConfigSettings();

		String params = "sign="+sign+"&account="+account+"&app_key="+app_key+"&format="+format+"&method="+method+"&password="+password+"&sign_method="+sign_method+"&timestamp="+timestamp+"&v="+v+"&deviceNum="+deviceNum;
//		JSONObject data = HttpUtil.doBodyPost(url,params);
//		System.out.println(data.get("subMsg"));
		return  paramMap;
	}

	/**
	 * 全球眼数据页
	 * @param session
	 * @param orgCode 信息域组织编码
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/globalEyes")
	public String globalEyes(HttpSession session,
							 @RequestParam(value="orgCode") String orgCode, ModelMap map,
							 @RequestParam(value = "companyType", required = false) String companyType,
							 @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		map.addAttribute("orgCode", orgCode);
		map.addAttribute("companyType",companyType);
		map.addAttribute("elementsCollectionStr", elementsCollectionStr);
		return "/map/arcgis/standardmappage/standardGlobalEyes_nanping.ftl";
	}

	/**
	 * 全球眼数据
	 * @param session
	 * @param page 页码
	 * @param rows 每页条数
	 * @param orgCode 信息域组织编码
	 * @param platformName 平台名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/globalEyesListData", method= RequestMethod.POST)
	public EUDGPagination globalEyesListData(HttpSession session,
				 @RequestParam(value="page") int page,
				 @RequestParam(value="rows") int rows,
				 @RequestParam(value="orgCode") String orgCode,
				 @RequestParam(value="platformName", required=false) String platformName) {
		if(page<=0) page=1;
		if(platformName!=null) platformName = platformName.trim();
		EUDGPagination pagination = null;
		String url = "http://192.168.21.63:8060/cms/router/rest?";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap = this.getGlobalEysConfigSettings();
		//加密
		String sign = "";
		String params = "";
		if(paramMap != null){
			String signStr = "";
			String appsecret = "";
			if(paramMap.get("appsecret") != null){
				appsecret = (String)paramMap.get("appsecret");
			}
			if(paramMap.get("GLOBALEYES_URL") != null){
				url = (String)paramMap.get("GLOBALEYES_URL");
			}
			if(paramMap.get("account") != null){
				signStr = signStr + "account" + paramMap.get("account");
				params = params + "&account="+paramMap.get("account");
			}
			if(paramMap.get("app_key") != null){
				signStr = signStr + "app_key" + paramMap.get("app_key");
				params = params + "&app_key="+paramMap.get("app_key");
			}
			if(paramMap.get("format") != null){
				signStr = signStr + "format"  + paramMap.get("format");
				params = params + "&format="+paramMap.get("format");
			}
			signStr = signStr + "method" + "device/list";
			params = params + "&method="+ "device/list";
			if(paramMap.get("password") != null){
				signStr = signStr + "password" + paramMap.get("password");
				params = params + "&password="+paramMap.get("password");
			}
			signStr = signStr + "sign_method" + "md5";
			params = params + "&sign_method=" + "md5";

			signStr = signStr + "timestamp" + DateUtils.getNow();
			params = params + "&timestamp="+DateUtils.getNow();
			if(paramMap.get("v") != null){
				signStr = signStr + "v" + paramMap.get("v");
				params = params + "&v="+paramMap.get("v");
			}
			signStr = appsecret + signStr + appsecret;
			//md5加密
			sign = EncryptUtil.encryptMd5(signStr).toUpperCase();
			params = "sign="+ sign + params;
		}
		JSONObject data = HttpUtil.doBodyPost(url,params);
		JSONArray devices = (JSONArray) data.get("devices");
		List<Device> list = new ArrayList<Device>();
		for (int i = 0; i < devices.size(); i++) {
			JSONObject device = devices.getJSONObject(i);
			list.add((Device) JSONObject.toBean(device, Device.class));
		}
		pagination = new EUDGPagination(list.size(), list);
		return pagination;
	}

	/**
	 * 2014-06-24 chenlf add
	 * 获取全球眼的定位信息
	 * @param session
	 * @param ids 全球眼ids
	 * @param mapt 地图类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getArcgisLocateDataListOfGlobalEyes")
	public List<ArcgisInfo> getArcgisGlobalEyesLocateDataList(HttpSession session,
			  @RequestParam(value = "ids") String ids,
			  @RequestParam(value = "mapt") Integer mapt,
			  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		List<ArcgisInfo> list = this.arcgisDataOfLocalService.getArcgisGlobalEyesLocateDataListByDeviceNums(ids,mapt);
//		for(ArcgisInfo arcgisInfo:list){
//			arcgisInfo.setElementsCollectionStr(elementsCollectionStr);
//		}
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getGisEyesByDeviceNum")
	public List<ArcgisInfo> getArcgisGlobalEyesLocateDataListByDeviceNum(HttpSession session,
			  @RequestParam(value = "deviceNum") String deviceNum,
			  @RequestParam(value = "mapt") Integer mapt,
			  @RequestParam(value = "elementsCollectionStr", required=false) String elementsCollectionStr) {
		if(StringUtils.isNotBlank(deviceNum)){
			deviceNum = "'"+deviceNum+"'";
		}
		List<ArcgisInfo> list = this.arcgisDataOfLocalService.getArcgisGlobalEyesLocateDataListByDeviceNums(deviceNum,mapt);
//		if(StringUtils.isNotBlank(elementsCollectionStr)){
//			for(ArcgisInfo arcgisInfo:list){
//				arcgisInfo.setElementsCollectionStr(elementsCollectionStr);
//			}
//		}

		return list;
	}
	
	
	@RequestMapping(value="/hitFace")
	public String hitFace(HttpSession session,
							 @RequestParam(value="deviceNums") String deviceNums, ModelMap map) {
		
		
		System.out.println("");
		return "/map/arcgis/standardmappage/detail_hitFace.ftl";
	}

	/**
	 * 全球眼数据页
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/globalEyesPlay")
	public String globalEyesShow(HttpSession session,
							 @RequestParam(value="deviceNums") String deviceNums, ModelMap map) {
		map.addAttribute("deviceNums", deviceNums);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String url = "http://192.168.21.63:8060/cms/router/rest?";
		FunConfigureSetting funConfigureSetting = funConfigurationService.findConfigureSettingLatest("GLOBALEYES_NANPING", "GLOBALEYES_URL", IFunConfigurationService.CFG_TYPE_URL, IFunConfigurationService.DEFAULT_ORG_CODE, IFunConfigurationService.DIRECTION_PARALLEL);
		if(funConfigureSetting != null && StringUtils.isNotBlank(funConfigureSetting.getCfgVal())){
			url = funConfigureSetting.getCfgVal();
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap = this.getGlobalEysConfigSettings();
		//加密
		String sign = "";
		String params = "";
		if(paramMap != null){
			String signStr = "";
			String appsecret = "";
			if(paramMap.get("appsecret") != null){
				appsecret = (String)paramMap.get("appsecret");
			}
			if(paramMap.get("GLOBALEYES_URL") != null){
				url = (String)paramMap.get("GLOBALEYES_URL");
			}
			if(paramMap.get("account") != null){
				signStr = signStr + "account" + paramMap.get("account");
				params = params + "&account="+paramMap.get("account");
			}
			if(paramMap.get("app_key") != null){
				signStr = signStr + "app_key" + paramMap.get("app_key");
				params = params + "&app_key="+paramMap.get("app_key");
			}

			if(StringUtils.isNotBlank(deviceNums)){
				signStr = signStr + "deviceNum" + deviceNums;
				params = params + "&deviceNum="+ deviceNums;
			}

			if(paramMap.get("format") != null){
				signStr = signStr + "format"  + paramMap.get("format");
				params = params + "&format="+paramMap.get("format");
			}
			signStr = signStr + "method" + "device/getDevUrl";
			params = params + "&method="+ "device/getDevUrl";
			if(paramMap.get("password") != null){
				signStr = signStr + "password" + paramMap.get("password");
				params = params + "&password="+paramMap.get("password");
			}
			signStr = signStr + "sign_method" + "md5";
			params = params + "&sign_method=" + "md5";

			signStr = signStr + "timestamp" + DateUtils.getNow();
			params = params + "&timestamp="+DateUtils.getNow();
			if(paramMap.get("v") != null){
				signStr = signStr + "v" + paramMap.get("v");
				params = params + "&v="+paramMap.get("v");
			}
			signStr = appsecret + signStr + appsecret;
			//md5加密
			sign = EncryptUtil.encryptMd5(signStr).toUpperCase();
			params = "sign="+ sign + params;
		}

		JSONObject data = HttpUtil.doBodyPost(url,params);
		if(data != null && "1".equals(data.get("returnCode")) && data.get("rtspUrl") != null){
			map.addAttribute("rtspUrl", data.get("rtspUrl"));
		}
//		System.out.println(data.get("rtspUrl"));
		return "/map/arcgis/standardmappage/ffcsGlobalEyes_play.ftl";
	}

	/**
	 * 全球眼数据页
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/globalEyesShowUrl")
	public Map<String, Object> globalEyesShowUrl(HttpSession session,
								 @RequestParam(value="deviceNums") String deviceNums, ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String url = "http://192.168.21.63:8060/cms/router/rest?";
		FunConfigureSetting funConfigureSetting = funConfigurationService.findConfigureSettingLatest("GLOBALEYES_NANPING", "GLOBALEYES_URL", IFunConfigurationService.CFG_TYPE_URL, IFunConfigurationService.DEFAULT_ORG_CODE, IFunConfigurationService.DIRECTION_PARALLEL);
		if(funConfigureSetting != null && StringUtils.isNotBlank(funConfigureSetting.getCfgVal())){
			url = funConfigureSetting.getCfgVal();
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap = this.getGlobalEysConfigSettings();
		//加密
		String sign = "";
		String params = "";
		if(paramMap != null){
			String signStr = "";
			String appsecret = "";
			if(paramMap.get("appsecret") != null){
				appsecret = (String)paramMap.get("appsecret");
			}
			if(paramMap.get("GLOBALEYES_URL") != null){
				url = (String)paramMap.get("GLOBALEYES_URL");
			}
			if(paramMap.get("account") != null){
				signStr = signStr + "account" + paramMap.get("account");
				params = params + "&account="+paramMap.get("account");
			}
			if(paramMap.get("app_key") != null){
				signStr = signStr + "app_key" + paramMap.get("app_key");
				params = params + "&app_key="+paramMap.get("app_key");
			}

			if(StringUtils.isNotBlank(deviceNums)){
				signStr = signStr + "deviceNum" + deviceNums;
				params = params + "&deviceNum="+ deviceNums;
			}

			if(paramMap.get("format") != null){
				signStr = signStr + "format"  + paramMap.get("format");
				params = params + "&format="+paramMap.get("format");
			}
			signStr = signStr + "method" + "device/getDevUrl";
			params = params + "&method="+ "device/getDevUrl";
			if(paramMap.get("password") != null){
				signStr = signStr + "password" + paramMap.get("password");
				params = params + "&password="+paramMap.get("password");
			}
			signStr = signStr + "sign_method" + "md5";
			params = params + "&sign_method=" + "md5";

			signStr = signStr + "timestamp" + DateUtils.getNow();
			params = params + "&timestamp="+DateUtils.getNow();
			if(paramMap.get("v") != null){
				signStr = signStr + "v" + paramMap.get("v");
				params = params + "&v="+paramMap.get("v");
			}
			signStr = appsecret + signStr + appsecret;
			//md5加密
			sign = EncryptUtil.encryptMd5(signStr).toUpperCase();
			params = "sign="+ sign + params;
		}

		JSONObject data = HttpUtil.doBodyPost(url,params);
		if(data != null && "1".equals(data.get("returnCode")) && data.get("rtspUrl") != null){
			map.addAttribute("rtspUrl", data.get("rtspUrl"));
		}
//		System.out.println(data.get("rtspUrl"));
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("rtspUrl", data.get("rtspUrl"));
		return dataMap;
	}


	/**
	 * 全球眼数据页
	 * @param session
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getPointVideoUrl")
	public Map<String, Object> getPointVideoUrl(HttpSession session,
			 @RequestParam(value="deviceNums") String deviceNums,
			 @RequestParam(value="time") String time,
			 @RequestParam(value="interval") Integer interval,ModelMap map) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String url = "http://192.168.21.63:8060/cms/router/rest?";
		FunConfigureSetting funConfigureSetting = funConfigurationService.findConfigureSettingLatest("GLOBALEYES_NANPING", "GLOBALEYES_URL", IFunConfigurationService.CFG_TYPE_URL, IFunConfigurationService.DEFAULT_ORG_CODE, IFunConfigurationService.DIRECTION_PARALLEL);
		if(funConfigureSetting != null && StringUtils.isNotBlank(funConfigureSetting.getCfgVal())){
			url = funConfigureSetting.getCfgVal();
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap = this.getGlobalEysConfigSettings();
		//加密
		String sign = "";
		String params = "";

		if(paramMap != null){
			String signStr = "";
			String appsecret = "";
			if(paramMap.get("appsecret") != null){
				appsecret = (String)paramMap.get("appsecret");
			}
			if(paramMap.get("GLOBALEYES_URL") != null){
				url = (String)paramMap.get("GLOBALEYES_URL");
			}
			if(paramMap.get("account") != null){
				signStr = signStr + "account" + paramMap.get("account");
				params = params + "&account="+paramMap.get("account");
			}
			if(paramMap.get("app_key") != null){
				signStr = signStr + "app_key" + paramMap.get("app_key");
				params = params + "&app_key="+paramMap.get("app_key");
			}

			if(StringUtils.isNotBlank(deviceNums)){
				signStr = signStr + "deviceNum" + deviceNums;
				params = params + "&deviceNum="+ deviceNums;
			}
			if(paramMap.get("format") != null){
				signStr = signStr + "format"  + paramMap.get("format");
				params = params + "&format="+paramMap.get("format");
			}
			if(interval != null){
				signStr = signStr + "interval" + interval;
				params = params + "&interval="+ interval;
			}
			signStr = signStr + "method" + "face/pointVideo";
			params = params + "&method="+ "face/pointVideo";
			if(paramMap.get("password") != null){
				signStr = signStr + "password" + paramMap.get("password");
				params = params + "&password="+paramMap.get("password");
			}
			signStr = signStr + "sign_method" + "md5";
			params = params + "&sign_method=" + "md5";
			if(StringUtils.isNotBlank(time)){
				signStr = signStr + "time" + time;
				params = params + "&time="+ time;
			}
			signStr = signStr + "timestamp" + DateUtils.getNow();
			params = params + "&timestamp="+DateUtils.getNow();
			if(paramMap.get("v") != null){
				signStr = signStr + "v" + paramMap.get("v");
				params = params + "&v="+paramMap.get("v");
			}
			signStr = appsecret + signStr + appsecret;
			//md5加密
			sign = EncryptUtil.encryptMd5(signStr).toUpperCase();
			params = "sign="+ sign + params;
		}

		JSONObject data = HttpUtil.doBodyPost(url,params);
		List<String> urls = new ArrayList<String>();
		if(data != null && "1".equals(data.get("returnCode")) && data.get("urls") != null){
			urls = (List<String>) data.get("urls");
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		if(urls != null && urls.size()>0){
			dataMap.put("rtspUrl", urls.get(0));
		}
		return dataMap;
	}

	/**
	 * 获取功能编码为GLOBALEYES_NANPING的所有配置值
	 * @return
	 */
	private Map<String, Object> getGlobalEysConfigSettings(){
		String url = "";
		Map<String, Object> params = new HashMap<String, Object>();
		String funcCode = "GLOBALEYES_NANPING";
		String account = "";
		String app_key = "";
		String password = "";
		String v = "";
		String GLOBALEYES_URL = "";
		String appsecret = "";
		String paramsStr = "";
//		List<FunConfigureSetting> funConfigureSettings = funConfigurationService.findConfigureSettingLatestList(funcCode, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, null, IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_PARALLEL);
		List<FunConfigureSetting> funConfigureSettings = funConfigurationService.findConfigureSettingLatestList(funcCode, null, null, null, null, 0);
		if (funConfigureSettings != null && funConfigureSettings.size()>0){
			for (FunConfigureSetting funConfigureSetting:funConfigureSettings){
				if("appsecret".equals(funConfigureSetting.getTrigCondition())){
					appsecret = funConfigureSetting.getCfgVal();
					params.put("appsecret",appsecret);
				}
				if("account".equals(funConfigureSetting.getTrigCondition())){
					account = funConfigureSetting.getCfgVal();
					params.put("account",account);
				}
				if("app_key".equals(funConfigureSetting.getTrigCondition())){
					app_key = funConfigureSetting.getCfgVal();
					params.put("app_key",app_key);
				}
				if("password".equals(funConfigureSetting.getTrigCondition())){
					password = funConfigureSetting.getCfgVal();
					params.put("password",password);
				}
				if("v".equals(funConfigureSetting.getTrigCondition())){
					v = funConfigureSetting.getCfgVal();
					params.put("v",v);
				}

				if("GLOBALEYES_URL".equals(funConfigureSetting.getTrigCondition())){
					GLOBALEYES_URL = funConfigureSetting.getCfgVal();
					params.put("GLOBALEYES_URL",GLOBALEYES_URL);
				}
			}
			params.put("format","json");
		}
		return params;
	}

	@ResponseBody
	@RequestMapping(value = "/sendMsg")
	public Map<String, Object> sendMsg(HttpSession session, HttpServletRequest request, ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String url = request.getParameter("url");
		String data = request.getParameter("data");
		url = url + "&data=" + data;
		ReceiverBO receiver = new ReceiverBO();
		List<Long> userList = new ArrayList<Long>();
		userList.add(userInfo.getUserId());
		receiver.setUserIdList(userList);
		List<Long> orgList = new ArrayList<Long>();
		orgList.add(userInfo.getOrgId());
		receiver.setOrgIdList(orgList);
		Map<String, Object> result = messageOutService.addMessage(userInfo.getUserId(), userInfo.getOrgId(), url, "警报", "03", "99", receiver);
		return null;
	}



	@ResponseBody
	@RequestMapping(value = "/getData")
	public String getData(HttpSession session, HttpServletRequest request, ModelMap map){
		String GLOBALEYES_URL = "http://120.32.112.242:28249/ivm-api-data_input/ivm/face/queryInfo";
		String platform_id = "Global_Eyes";
		String similarity = "80";
		Calendar rightNow = Calendar.getInstance();
		String endTimeInp = DateUtils.formatDate(rightNow.getTime(), DateUtils.PATTERN_24TIME);
		rightNow.add(Calendar.SECOND,-10);
		String startTimeInp = DateUtils.formatDate(rightNow.getTime(), DateUtils.PATTERN_24TIME);
		String telephone = "Global_Eyes";
		String md5Key = "75BD2E98AC17564B2DB7C74B064F5084C6557FDDF3E4C286";
		String desKey = "b5eefe0437d945b98e82f46fbff8d3552c2ff6f7f8acd8de";

		List<FunConfigureSetting> funConfigureSettings = funConfigurationService.findConfigureSettingLatestList("GLOBALEYES_NANPING", null, null, null, null, 0);
		if (funConfigureSettings != null && funConfigureSettings.size()>0){
			for (FunConfigureSetting funConfigureSetting:funConfigureSettings){
				if("GLOBALEYES_URL_AI".equals(funConfigureSetting.getTrigCondition())){
					GLOBALEYES_URL = funConfigureSetting.getCfgVal();
				}
				if("startTime".equals(funConfigureSetting.getTrigCondition())){
					startTimeInp = funConfigureSetting.getCfgVal();
				}
				if("endTime".equals(funConfigureSetting.getTrigCondition())){
					endTimeInp = funConfigureSetting.getCfgVal();
				}
			}
		}
		
		String timestamp = DateUtils.formatDate(rightNow.getTime(), DateUtils.PATTERN_24TIME);
		
		String sign = "";
		//加密
		try {
		//		String timestamp = "2017-01-20 14:30:00";
			String params = telephone;
			String md5 = CrytoUtils.md5(params,md5Key,timestamp);
			System.out.println("md5加密后="+md5);
			sign = CrytoUtils.encode(desKey,timestamp, md5);
			System.out.println("3des加密后的sign="+sign);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		"platform_id":"Global_Eyes",
//		"similarity":"80",
//		"start_time":"2016-12-14 10:30:51",
//		"end_time":"2016-12-14 11:40:51",
//		"timestamp":"2017-01-20 14:30:00",
//		"sign":"pjXEzTI%2ByGimXsQtHdZ8fe93qyobGOrI5rc3j3EvXpPDgEYfPTL23lXUTgGsIF0aAj28kjjPfG8%3D"
		
		String param = "platform_id="+platform_id+"&similarity="+similarity;
		param = param + "&start_time="+startTimeInp+"&end_time="+endTimeInp;
		param = param + "&timestamp="+timestamp+"&sign="+sign;
		System.out.println("params:"+param);
		
//		String json = "{'platform_id':'"+platform_id+"', 'similarity':"+similarity+", 'start_time':"+startTimeInp+", 'end_time':"+endTimeInp+", 'timestamp':"+timestamp+", 'sign':"+sign+"}";
//		try {
//			String body  = HttpUtil.postBody(GLOBALEYES_URL, json);
//			System.out.println("body:"+body);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println("GLOBALEYES_URL--"+GLOBALEYES_URL);
		APIHttpClient ac = new APIHttpClient(GLOBALEYES_URL);  
        JsonArray arry = new JsonArray();  
        JsonObject j = new JsonObject();  
        j.addProperty("platform_id", platform_id);  
        j.addProperty("similarity", similarity);  
        j.addProperty("start_time", startTimeInp);  
        j.addProperty("end_time", endTimeInp);  
        j.addProperty("timestamp", timestamp);  
        j.addProperty("sign", sign);  
        arry.add(j);  
        String p = arry.toString();
        String result = ac.post(p.substring(1, p.length()-1));
//		String result = HttpUtil.doPost(GLOBALEYES_URL, p.substring(1, p.length()-1));
        System.out.println(result);  
		result = "{\"data\":[{\"camera_id\":\"101\",\"globalpic_image_url\":\"ivm_static/Global_Eyes/101/FaceRecognition-01-20170121153538007_GlobalPic.jpg\",\"candidatepic_image_url\":\"ivm_static/Global_Eyes/101/FaceRecognition-01-20170121153538007_CandidatesPic1.jpg\",\"facerecognition_image_url\":\"ivm_static/Global_Eyes/101/FaceRecognition-01-20170121153538007.jpg\",\"name\":\"20161228204039\",\"sex\":\"男\",\"similarity\":\"100%\",\"card_type\":\"identification card\",\"card_number\":\"1234567\",\"check_time\":\"2017-01-21 15:35:38\"}],\"request_id\":\"1485155392140\",\"result_code\":\"0\",\"result_desc\":\"request successful\",\"timestamp\":\"2017-01-23 15:09:52\"}";
		session.setAttribute("hitFaceDatas", result);
		System.out.println("result:"+result);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/ivm/getData")
	public String getData_ivm(HttpSession session, HttpServletRequest request, ModelMap map){
		String method = "ivm/face/zzQuery";
		String result = getUrl(method);
//		String result = "{\"returnCode\":\"1\",\"apiMethod\":\"/ivm/face/zzQuery\",\"ivmFaces\":[{\"ivmFacesId\":459,\"facerecognitionImageUrl\":\"http://oaknt.tpddns.cn:19249/ivm_static/Global_Eyes/34020000001320000101/1488763429698.jpg\",\"cameraId\":\"34020000001320000008\",\"globalpicImageUrl\":\"http://oaknt.tpddns.cn:19249/ivm_static/Global_Eyes/34020000001320000101/1488763429670.jpg\",\"capture_time\":\"2017-03-06 09:22:23\",\"hit_faces\":[{\"sex\":\"未知\",\"candidatepicImageUrl\":\"http://oaknt.tpddns.cn:19249/ivm_static/Global_Eyes/34020000001320000101/1488763429712.jpg\",\"name\":\"卓海敏\",\"similarity\":85.04667,\"checkTime\":\"2017-03-01 09:09:33\",\"cardType\":\"identification card\",\"cardNumber\":\"35012345678\"}]}],\"subCode\":\"10\",\"subMsg\":\"成功\"}";
		session.setAttribute("blackList", result);
		return result;
	}

	/**
	 *查看捕获信息详情1
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/ivm/index")
	public String ivm_index(HttpSession session,
							HttpServletRequest request, @RequestParam(value="mapt",required = false) int mapt,
							ModelMap map) {
		String data = (String)session.getAttribute("blackList");

		map.addAttribute("mapt",mapt);
		map.addAttribute("gridName",this.getDefaultGridInfo(session).get(KEY_START_GRID_NAME));
		map.addAttribute("gridId",this.getDefaultGridInfo(session).get(KEY_START_GRID_ID));
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("POPULATION_URL", App.RS.getDomain(session));
		map.addAttribute("data",data);

		JSONObject jsonObject = JSONObject.fromObject(data, new JsonConfig());
		Iterator<String> keys = jsonObject.keys();
		while(keys.hasNext()){
			String key = keys.next();
			Object value = jsonObject.get(key);
			if(key.equals("cameraId")){
				Map<String, Object> globals = arcgisDataOfLocalService.getGlobalListByDeviceNums("'"+value+"'");
				if(globals != null && globals.get("REMARK")!=null){
					map.addAttribute("REMARK",(String)globals.get("REMARK"));
				}
			}
		}


		Calendar rightNow = Calendar.getInstance();
		String endTimeInp = DateUtils.formatDate(rightNow.getTime(), DateUtils.PATTERN_24TIME);
		rightNow.add(Calendar.SECOND,-10);
		String startTimeInp = DateUtils.formatDate(rightNow.getTime(), DateUtils.PATTERN_24TIME);
		map.addAttribute("startTimeInp",startTimeInp);
		map.addAttribute("endTimeInp",endTimeInp);
		return "/map/messager/indexOfVideoSurveillance_ivm.ftl";
	}

	private String getUrl(String method){
		String result = "";

		String account = "";//用户名
		String app_key = "";//接口key
		String password = "";//接口密码
		String v = "";//版本
		String GLOBALEYES_URL = "http://218.66.30.108:8060/aom/router/rest?";
		String appsecret = "111111";//app密钥
//		method = "";

		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.SECOND,120);
		String endTimeInp = DateUtils.formatDate(rightNow.getTime(), DateUtils.PATTERN_24TIME);
		rightNow.add(Calendar.SECOND,-180);
		String startTimeInp = DateUtils.formatDate(rightNow.getTime(), DateUtils.PATTERN_24TIME);

		String deviceNum = "";

		List<FunConfigureSetting> funConfigureSettings = funConfigurationService.findConfigureSettingLatestList("GLOBALEYES_NANPING", null, null, null, null, 0);
		if (funConfigureSettings != null && funConfigureSettings.size()>0){
			for (FunConfigureSetting funConfigureSetting:funConfigureSettings){
				if("account".equals(funConfigureSetting.getTrigCondition())){
					account = funConfigureSetting.getCfgVal();
				}
				if("appsecret".equals(funConfigureSetting.getTrigCondition())){
					appsecret = funConfigureSetting.getCfgVal();
				}
				if("app_key".equals(funConfigureSetting.getTrigCondition())){
					app_key = funConfigureSetting.getCfgVal();
				}
				if("password".equals(funConfigureSetting.getTrigCondition())){
					password = funConfigureSetting.getCfgVal();
				}
				if("v".equals(funConfigureSetting.getTrigCondition())){
					v = funConfigureSetting.getCfgVal();
				}
				if("GLOBALEYES_URL".equals(funConfigureSetting.getTrigCondition())){
					GLOBALEYES_URL = funConfigureSetting.getCfgVal();
				}
				if("startTime".equals(funConfigureSetting.getTrigCondition())){
					startTimeInp = funConfigureSetting.getCfgVal();
				}
				if("endTime".equals(funConfigureSetting.getTrigCondition())){
					endTimeInp = funConfigureSetting.getCfgVal();
				}
				if("deviceNum".equals(funConfigureSetting.getTrigCondition())){
					deviceNum = funConfigureSetting.getCfgVal();
				}
			}
		}
		String startTime = "&startTime="+startTimeInp;
		String endTime = "&endTime="+endTimeInp;
		String timestamp = DateUtils.getNow();
//		String signStr = appsecret+"account"+account+"app_key"+app_key+"deviceNum35072300491321000001"+"endTime"+endTimeInp+"formatjsonmethod"+method+"password"+password+"sign_methodmd5startTime"+startTimeInp+"timestamp"+timestamp+"v"+v+appsecret;
//		StringBuffer signStr = new StringBuffer();
//		signStr.append(appsecret+"account"+account+"app_key"+app_key);

		String deviceNumStr = "";
		if(StringUtils.isNotBlank(deviceNum)){
			deviceNumStr = "&deviceNum="+deviceNum;
//			signStr.append("deviceNum"+deviceNum);
		}
//		signStr.append("endTime"+endTimeInp+"formatjsonmethod"+method+"password"+password+"sign_methodmd5startTime"+startTimeInp+"timestamp"+timestamp+"v"+v+appsecret);
//		System.out.println(signStr.toString());

		String signStr = appsecret+"account"+account+"app_key"+app_key+"deviceNum"+deviceNum+"endTime"+endTimeInp+"formatjsonmethod"+method+"password"+password+"sign_methodmd5startTime"+startTimeInp+"timestamp"+timestamp+"v"+v+appsecret;
//		String signStr = appsecret+"account"+account+"app_key"+app_key+"endTime"+endTimeInp+"formatjsonmethod"+method+"password"+password+"sign_methodmd5startTime"+startTimeInp+"timestamp"+timestamp+"v"+v+appsecret;

		System.out.println(signStr);
		String sign = "&sign="+EncryptUtil.encryptMd5(signStr.toString()).toUpperCase();
		String params = "account="+account+"&app_key="+app_key+"&format=json&method="+method+"&password="+password+"&sign_method=md5&timestamp="+timestamp+"&v="+v;
		result = HttpUtil.doPost(GLOBALEYES_URL, params+sign+startTime+endTime+deviceNumStr);
//		result = HttpUtil.doPost(GLOBALEYES_URL, params+sign+startTime+endTime);
//		System.out.println("result->"+result);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/getData2")
	public String getData2(HttpSession session, HttpServletRequest request, ModelMap map){
		
		String account = "";//用户名
		String app_key = "";//接口key
		String password = "";//接口密码
		String v = "";//版本
		String GLOBALEYES_URL = "http://192.168.21.64:8060/aom/router/rest?";
		String appsecret = "111111";//app密钥
//		String startTimeInp = "2016-12-10 17:47:39";
//		String endTimeInp = "2016-12-10 17:47:44";
//		System.out.println("DateUtils.getNow()"+DateUtils.getNow());
		
		Calendar rightNow = Calendar.getInstance();
		String endTimeInp = DateUtils.formatDate(rightNow.getTime(), DateUtils.PATTERN_24TIME);
		rightNow.add(Calendar.SECOND,-10);
		String startTimeInp = DateUtils.formatDate(rightNow.getTime(), DateUtils.PATTERN_24TIME);

		String key = request.getParameter("key");

		
		List<FunConfigureSetting> funConfigureSettings = funConfigurationService.findConfigureSettingLatestList("GLOBALEYES_NANPING", null, null, null, null, 0);
		if (funConfigureSettings != null && funConfigureSettings.size()>0){
			for (FunConfigureSetting funConfigureSetting:funConfigureSettings){
				if("account".equals(funConfigureSetting.getTrigCondition())){
					account = funConfigureSetting.getCfgVal();
				}
				if("appsecret".equals(funConfigureSetting.getTrigCondition())){
					appsecret = funConfigureSetting.getCfgVal();
				}
				if("app_key".equals(funConfigureSetting.getTrigCondition())){
					app_key = funConfigureSetting.getCfgVal();
				}
				if("password".equals(funConfigureSetting.getTrigCondition())){
					password = funConfigureSetting.getCfgVal();
				}
				if("v".equals(funConfigureSetting.getTrigCondition())){
					v = funConfigureSetting.getCfgVal();
				}
				if("GLOBALEYES_URL".equals(funConfigureSetting.getTrigCondition())){
					GLOBALEYES_URL = funConfigureSetting.getCfgVal();
				}
				if(StringUtils.isNotBlank(key) && key.equals("p")){
					if("startTime".equals(funConfigureSetting.getTrigCondition())){
						startTimeInp = funConfigureSetting.getCfgVal();
					}
					if("endTime".equals(funConfigureSetting.getTrigCondition())){
						endTimeInp = funConfigureSetting.getCfgVal();
					}
				}
			}
		}
		
		
		String startTime = "&startTime="+startTimeInp;
		String endTime = "&endTime="+endTimeInp;
		String timestamp = DateUtils.getNow();
		String signStr = appsecret+"account"+account+"app_key"+app_key+"endTime"+endTimeInp+"formatjsonmethodzzQuerypassword"+password+"sign_methodmd5startTime"+startTimeInp+"timestamp"+timestamp+"v"+v+appsecret;
		
		String sign = "&sign="+EncryptUtil.encryptMd5(signStr).toUpperCase();
		
		String params = "account="+account+"&app_key="+app_key+"&format=json&method=zzQuery&password="+password+"&sign_method=md5&timestamp="+timestamp+"&v="+v;
    	String result = HttpUtil.doPost(GLOBALEYES_URL, params+sign+startTime+endTime);

		session.setAttribute("blackList", result);
//    	String r = "{\"birthday\":\"1988-02-16\",\"gender\":\"0\",\"name\":\"张三\"}";
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/getBlackData")
	public List<Map<String, Object>> getBlackData(HttpSession session, HttpServletRequest request,
						  @RequestParam(value = "mapt", required = false) int mapt ,ModelMap map){
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		String data = (String)session.getAttribute("blackList");
        List<Map<String, Object>> result =  new ArrayList<Map<String, Object>>();
//		String data  = request.getParameter("data");
		if(StringUtils.isBlank(data)) return result;
		System.out.println("dddddddddddddata========="+data);
		Map<String, Object> mapp = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.fromObject(data, new JsonConfig());
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()){
            String key = keys.next();
            Object value = jsonObject.get(key);
//            System.out.println(key + "--" + value.toString());
            if(key.equals("hitFaces")){
            	String blackList = value.toString().substring(1, value.toString().length()-1);
//            	System.out.println("blackList--"+blackList);
//            	if(blackList)
            	JSONObject job = JSONObject.fromObject(blackList);
            	Iterator<String> subkeys = job.keys();
                List<Map<String, Object>> similarFaceObjList =  new ArrayList<Map<String, Object>>();
            	while (subkeys.hasNext()){
            		String subkey = subkeys.next();
                    Object subvalue = job.get(subkey);
                    if(subkey.equals("timestampStart")){
                		Long timestampStart = Long.valueOf(subvalue.toString());
                		Date d = new Date(timestampStart * 1000);
						String timestampStartStr = DateUtils.formatDate(d, DateUtils.PATTERN_24TIME);
//						System.out.println("timestampStart---"+timestampStartStr);
	                    mapp.put("timestampStartStr", timestampStartStr);
                    }
                    if(subkey.equals("timestampEnd")){
                		Long timestampEnd = Long.valueOf(subvalue.toString());
                		Date d = new Date(timestampEnd * 1000);
						String timestampEndStr = DateUtils.formatDate(d, DateUtils.PATTERN_24TIME);
//						System.out.println("timestampEndStr---"+timestampEndStr);
	                    mapp.put("timestampEndStr", timestampEndStr);
                    }
                    if(subkey.equals("similarFaceObj")){
                    	String similarFaceObj = subvalue.toString();
//                    	similarFaceObj = similarFaceObj.substring(1,similarFaceObj.length()-1);
//                    	System.out.println("similarFaceObj--"+similarFaceObj);
                    	JSONArray jsonArray = JSONArray.fromObject(similarFaceObj);
                    	Iterator similarFaceObjIt = jsonArray.iterator();
//                    	System.out.println("jsonArray.size()--"+jsonArray.size());
                    	for(int i=0;i<jsonArray.size();i++){
//                    		jsonArray.get(i);
                    		JSONObject jobsub = JSONObject.fromObject(jsonArray.get(i));
                        	Iterator<String> subkeysFace = jobsub.keys();
                        	Map<String, Object> mappFace = new HashMap<String, Object>();
                        	while (subkeysFace.hasNext()){
                        		String subkeysFaces = subkeysFace.next();
                                Object subvalueFace = jobsub.get(subkeysFaces);
//                                System.out.println(subkeysFaces+"---"+subvalueFace);
                        		mappFace.put(subkeysFaces, subvalueFace);
                                if(subkeysFaces.equals("person_id")){//身份证
                                	Map<String, Object> cirs = ciRsService.getCiRsTopByOrgCodeAndIdCard(userInfo.getOrgCode(), String.valueOf(subvalueFace));
//                                	System.out.println("cirs-"+cirs);
                            		List<String[]> tagList = new ArrayList<String[]>();
                            		Long ciRsId = null;
                            		String ciRsName = null;
                                	if(cirs.get("ciRs")!=null){
                                		CiRsTop ciRsTop = (CiRsTop)cirs.get("ciRs");
                                    	ciRsId = ciRsTop.getCiRsId();
                                    	ciRsName = ciRsTop.getName();
                                		Map<String, Object> tagFlag = ciRsService.getRsTagFlag(ciRsId);// 人员标签
//                                		System.out.println("tagFlag-"+tagFlag);
                                		Set set = tagFlag.keySet();
                                		Iterator it = set.iterator();
                                		while (it.hasNext()) {
                                			String s = (String) it.next();
                                			if (tagFlag.get(s).toString().equals("1")) {// 1为存在标签，0为不存在
                                				String typeVal = getTypeVlue(s);
                                				if (StringUtils.isNotBlank(typeVal)) {
                                					String[] tag = { typeVal, getValue(s) };
                                					tagList.add(tag);
                                				}
                                			}
                                		}
                                	}
                            		mappFace.put("tag", tagList);
                            		mappFace.put("ciRsId", ciRsId);
                            		mappFace.put("ciRsName", ciRsName);
                                }
                        	}
                        	similarFaceObjList.add(mappFace);
                    	}
//                    	while(similarFaceObjIt.hasNext()){
//                    		System.out.println("it.next()-"+similarFaceObjIt.next());
//                    		
//                        	similarFaceObjList.add(mappFace);
//                    	}
//                    	similarFaceObjList.add(mappFace);
                    	mapp.put("similarFaceObjMap", similarFaceObjList);
                    }
                    if(subkey.equals("deviceNum")){
                    	String remark = "";
						String deviceName = "";
                    	String deviceNum = subvalue.toString();
                    	Map<String, Object> globals = arcgisDataOfLocalService.getGlobalListByDeviceNums("'"+deviceNum+"'");
						List<ArcgisInfo> arcgisInfos = arcgisDataOfLocalService.getArcgisGlobalEyesLocateDataListByDeviceNums("'"+deviceNum+"'", mapt);
                    	if(globals != null && globals.get("REMARK")!=null){
                        	remark = (String)globals.get("REMARK");
							mapp.put("remark", remark);
                    	}
						if(globals != null && globals.get("PLATFORM_NAME")!=null){
							deviceName = (String)globals.get("PLATFORM_NAME");
							mapp.put("deviceName", deviceName);
						}
						if(arcgisInfos != null && arcgisInfos.size()>0){
							mapp.put("x", arcgisInfos.get(0).getX());
							mapp.put("y", arcgisInfos.get(0).getY());
						}
                    }
                    mapp.put(subkey, (Object) subvalue);
//                    System.out.println(subkey + "--" + subvalue.toString());
            	}
				Date currentDate = new Date();
				String hanpenTimeStr = "";
				hanpenTimeStr = DateUtils.formatDate(currentDate, DateUtils.PATTERN_24TIME);
				mapp.put("hanpenTimeStr", hanpenTimeStr);
            	result.add(mapp);
            }
        }
		return result;
	}


	
	/**
	 *查看捕获信息详情1
	 * @param session
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index(HttpSession session,
			HttpServletRequest request, @RequestParam(value="mapt",required = false) int mapt, 
//			@RequestParam(value="data",required = false) String data,
			ModelMap map) {
		String data = (String)session.getAttribute("hitFaceDatas");
//		String data = request.getParameter("data");
		System.out.println("data---"+data);
//		data = data.substring(1, data.length() - 1);
//		System.out.println("data-"+data);
//		data = data.replaceAll("\\\\", "");1
//		System.out.println("data-"+data);
//		map.addAttribute("data",data);
		map.addAttribute("mapt",mapt);
		map.addAttribute("gridName",this.getDefaultGridInfo(session).get(KEY_START_GRID_NAME));
		map.addAttribute("gridId",this.getDefaultGridInfo(session).get(KEY_START_GRID_ID));
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("POPULATION_URL", App.RS.getDomain(session));
//		JSONObject jsonObject = JSONObject.fromObject(data, new JsonConfig());
		map.addAttribute("data",data);
//		map.addAttribute("datas",jsonObject);
		return "/map/messager/indexOfVideoSurveillance2.ftl";
	}
	
	@RequestMapping(value = "/index2")
	public String index2(HttpSession session,
			HttpServletRequest request, @RequestParam(value="mapt",required = false) int mapt, 
//			@RequestParam(value="data",required = false) String data,
			ModelMap map) {

		String data = (String)session.getAttribute("hitFaceDatas");
//		String data = request.getParameter("data");
		System.out.println("data---"+data);
//		data = data.substring(1, data.length() - 1);
//		System.out.println("data-"+data);
//		data = data.replaceAll("\\\\", "");
//		System.out.println("data-"+data);
//		map.addAttribute("data",data);
		String key = request.getParameter("key");
		Calendar rightNow = Calendar.getInstance();
		String endTimeInp = DateUtils.formatDate(rightNow.getTime(), DateUtils.PATTERN_24TIME);
		rightNow.add(Calendar.SECOND,-10);
		String startTimeInp = DateUtils.formatDate(rightNow.getTime(), DateUtils.PATTERN_24TIME);
		map.addAttribute("startTimeInp",startTimeInp);
		map.addAttribute("endTimeInp",endTimeInp);

		map.addAttribute("key",key);
		map.addAttribute("mapt",mapt);
		map.addAttribute("gridName",this.getDefaultGridInfo(session).get(KEY_START_GRID_NAME));
		map.addAttribute("gridId",this.getDefaultGridInfo(session).get(KEY_START_GRID_ID));
		map.addAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
		map.addAttribute("POPULATION_URL", App.RS.getDomain(session));
//		JSONObject jsonObject = JSONObject.fromObject(data, new JsonConfig());
		map.addAttribute("data",data);
//		map.addAttribute("datas",jsonObject);
		return "/map/messager/indexOfVideoSurveillance_demo.ftl";
	}
	
	public String getTypeVlue(String key) {
		Map map = new HashMap();
		map.put("party", "1");
		map.put("retire", "2");
		map.put("old", "3");
		map.put("hbcare", "3");
		map.put("army", "4");
		map.put("unemployed", "5");
		map.put("poor", "6");
		map.put("deformity", "7");
		map.put("mentalIllnessRecord", "8");
		map.put("dangerousGoodsRecord", "9");
		map.put("petitionRecord", "10");
		map.put("drugRecord", "11");
		map.put("cultMemberRecord", "12");
		map.put("correctionalRecord", "13");
		map.put("releasedRecord", "14");
		map.put("volunteer", "15");
		map.put("taibao", "16");
		map.put("motoMemberRecord", "17");
		map.put("youthPerson", "19");
		map.put("cleaner", "20");
		map.put("aidsPerson", "21");
		return map.get(key) != null ? map.get(key).toString() : "";
	}


	@RequestMapping(value="/showGlobalEyesDetail")
	public String showGlobalEyesDetail(HttpSession session,
							 @RequestParam(value="deviceNums") String deviceNums, ModelMap map) {
		map.addAttribute("deviceNums", deviceNums);
		return "/map/messager/ffcsGlobalEyesDetail.ftl";
	}
	
	public String getValue(String key) {
		Map map = new HashMap();
		map.put("party", "党员");
		map.put("retire", "退休");
		map.put("old", "老年人");
		map.put("hbcare", "居家养老");
		map.put("army", "服兵役");
		map.put("unemployed", "失业");
		map.put("poor", "低保");
		map.put("deformity", "残障");
		map.put("mentalIllnessRecord", "精神障碍");
		map.put("dangerousGoodsRecord", "危险品");
		map.put("petitionRecord", "上访");
		map.put("drugRecord", "吸毒");
		map.put("cultMemberRecord", "邪教人员");
		map.put("motoMemberRecord", "摩托车工");
		map.put("correctionalRecord", "矫正");
		map.put("releasedRecord", "刑释");
		map.put("volunteer", "志愿者");
		map.put("taibao", "台胞");
		map.put("youthPerson", "重点青少年");
		map.put("cleaner", "清扫保洁人员");
		map.put("aidsPerson", "艾滋病人员");
		return map.get(key) != null ? map.get(key).toString() : "";
	}
}
