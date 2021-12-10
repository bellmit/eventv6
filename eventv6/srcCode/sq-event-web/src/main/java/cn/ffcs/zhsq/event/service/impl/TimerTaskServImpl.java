package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.common.GsonUtils;
import cn.ffcs.cookie.service.UserCookieService;
import cn.ffcs.json.utils.JsonUtil;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.APIHttpClient;
import cn.ffcs.uam.bo.FunConfigureSetting;
import cn.ffcs.zhsq.dataExchange.service.IBasDataExchangeService;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.BasDataExchange;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.utils.ConstantValue;
import com.ctc.wstx.util.StringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;

import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventSyncService;
import cn.ffcs.zhsq.event.service.ITimerTaskService;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


public class TimerTaskServImpl extends ApplicationObjectSupport implements ITimerTaskService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**迪艾斯便民服务*/
	private String BIZPLATFORM_DS = "004";
	/**南威市级平台*/	
	private String BIZPLATFORM_NW = "007";
	/**西滨*/
	private String BIZPLATFORM_XB = "008";
	/**安海*/
	private String BIZPLATFORM_AH = "009";
	/**梅岭街道*/
	private String BIZPLATFORM_ML = "010";
	
	@Autowired
	private IDataExchangeStatusService dataExchangeStatusService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	private IEventDisposalService eventDisposalService;
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IDataExchangeStatusTwoWayService dataExchangeStatusTwoWayService;
	@Autowired
	private IEventDisposalDockingService eventDisposalDockingService;
	@Autowired
	private IBasDataExchangeService basDataExchangeService;
	@Autowired
	private UserCookieService userCookieService;

	private String nwWebServiceUri;
	private String nwWebServicTaskUri;
	private String dsWebServiceUri;
	private String xbWebServiceUri = "http://220.162.239.191:7777/services/eventDockingServiceImpl?wsdl";
	private String ahWebServiceUri;
	
	private boolean doSync = true;
	
	@Override
	public void run(){
		String run = funConfigurationService.changeCodeToValue("ENV_EVENT","RUN", IFunConfigurationService.CFG_TYPE_FACT_VAL);
		if(StringUtils.isNotBlank(run) && "true".equals(run)){
			execute();
		}
	}

	private String doGetData(int pageNo, int pageSize, String startTime, String endTime) {
		String url = "http://10.23.86.85:7009/GGAQ/03/XM.GOV.YZ.GGAQ.GGAQ.HBJGETDATAS";
//		Map<String, String> headers = new HashMap<String, String>();
//		headers.put("COLLAGEN-REQUESTER_ID","GGAQ::HBJ");
//		headers.put("COLLAGEN-AUTHORIZE_ID","764e3dd0158c42888d0e4c345f5e242a");
//		headers.put("COLLAGEN-SESSION_ID","no-session");
//		headers.put("COLLAGEN-TIMEOUT","40S");
//		headers.put("COLLAGEN-OUT_PARAMETERS","_ALL_BUT_EXCEPTION_");
//		headers.put("COLLAGEN-PROXY_FLOW_ID","GGAQ::01::FLOW_C3_CALL_RESTFUL_PROXY");
//		headers.put("User-Agent","CollaGEN3");
//		headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.0,*/*;q=0.8");
//		headers.put("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
//		headers.put("Accept-Encoding","text/html,application/xhtml+xml,application/xml;q=0.0,*/*;q=0.8");
//		headers.put("Accept","deflate");
//		headers.put("Content-type","application/json\n");
//		HttpRequestWithBody header = Unirest.post(url).headers(headers);
//		Map<String, String> params = new HashMap<String, String>();
//		RequestBodyEntity body = header.body(GsonUtils.toJson(params));
//		Map map = null;
//		try {
//			map = GsonUtils.fromJson(body.asString().getBody(), Map.class);
//		} catch (UnirestException e) {
//			e.printStackTrace();
//		}
//		System.out.println(map);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put( "tableid", "XM_GGAQ_EVENTS" );
		map.put( "token", "b59cb246454347858a7215ff4111729c" );
		map.put( "limit", pageSize);
		map.put( "offset", pageNo);
		map.put( "startTime", startTime);
		map.put( "endTime", endTime);
		JSONObject jsonObject = JSONObject.fromObject(map);
//		String param = "query={\"startTime\":\"2016-06-01 23:59:59\",\"limit\":10,\"token\":\"b59cb246454347858a7215ff4111729c\",\"tableid\":\"XM_GGAQ_EVENTS\",\"endTime\":\"2017-01-01 23:59:59\",\"offset\":0}";
		String param = "query="+jsonObject.toString();
		System.out.println(param);
		BufferedReader in = null;
		PrintWriter out = null;
		String result = "";
		URL realUrl = null;
		URLConnection connection = null;
		try {
			realUrl = new URL(url);
			connection = realUrl.openConnection();
			connection.setRequestProperty("Content-type" , "application/x-www-form-urlencoded");
			connection.setRequestProperty("COLLAGEN-REQUESTER_ID" , "GGAQ::HBJ");
			connection.setRequestProperty("COLLAGEN-AUTHORIZE_ID", "764e3dd0158c42888d0e4c345f5e242a");
			connection.setRequestProperty("COLLAGEN-PROXY_FLOW_ID", "GGAQ::01::FLOW_C3_CALL_RESTFUL_PROXY");
			connection.setRequestProperty("COLLAGEN-SESSION_ID", "no-session");
			connection.setRequestProperty("COLLAGEN-TIMEOUT", "40S");
			connection.setRequestProperty("COLLAGEN-DEBUG", "ON");
			connection.setRequestProperty("COLLAGEN-OUT_PARAMETERS", "_ALL_BUT_EXCEPTION_");
			connection.setReadTimeout(100000);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(connection.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.out.println("result->"+result);
			return result;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		String url = "http://10.1.11.131:7009/GGAQ/03/XM.GOV.YZ.GGAQ.GGAQ.HBJGETDATAS";
//		APIHttpClient ac = new APIHttpClient(url);
//		JsonArray arry = new JsonArray();
//		JsonObject j = new JsonObject();
//		j.addProperty("token", "b59cb246454347858a7215ff4111729c");
//		j.addProperty("tableid", "XM_GGAQ_EVENTS");
//		j.addProperty("limit", pageSize);
//		j.addProperty("offset", pageNo);
//		j.addProperty("startTime", startTime);
//		j.addProperty("endTime", endTime);
//		arry.add(j);
//		String p = arry.toString();
//		p = p.substring(1, p.length()-1);
//		System.out.println("p->"+"query="+p);
//		String jsonStr = ac.post("query="+p);

		return "";
	}

	@Override
	public void execute(){
		logger.info("================Timer task start================");
//		IEventSyncService eventSyncService = null;
//		eventSyncService = (IEventSyncService)this.getApplicationContext().getBean("eventSyncForMLServImpl");
//		System.out.println(eventSyncService.syncData());
//		eventSyncService = (IEventSyncService)this.getApplicationContext().getBean("eventSyncForDSServImpl");
//		System.out.println(eventSyncService.syncData());
//		eventSyncService = (IEventSyncService)this.getApplicationContext().getBean("eventSyncForWXServImpl");
//		System.out.println(eventSyncService.syncData());
//		eventSyncService = (IEventSyncService)this.getApplicationContext().getBean("eventSyncServImpl");
//		System.out.println(eventSyncService.syncData());
//		eventSyncService = (IEventSyncService)this.getApplicationContext().getBean("eventSyncForSMServImpl");
//		System.out.println(eventSyncService.syncData());

//query={"startTime":"2016-06-01 23:59:59","limit":"10","tableid":"XM_GGAQ_EVENTS","token":"b59cb246454347858a7215ff4111729c","endTime":"2017-01-01 23:59:59","offset":"0"}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("exchangeType","001");//
		BasDataExchange basDataExchange = basDataExchangeService.findBasDataExchange(params);
		if (basDataExchange != null){
			Date timeStamp = basDataExchange.getTimeStamp();
			String startTime = DateUtils.format(timeStamp, "yyyy-MM-dd HH:mm:ss");

			Calendar cal = Calendar.getInstance();
			cal.setTime(timeStamp);
			cal.add(Calendar.DATE, 1);

			cal.getTime();
			Date now = new Date();
			String endTime = DateUtils.format(now, "yyyy-MM-dd HH:mm:ss");

			int pageNo = 0;
			int pageSize = 200;

			String jsonStr = doGetData(pageNo, pageSize, startTime, endTime);

			save(jsonStr);
			BasDataExchange nextBasDataExchange = new BasDataExchange();
			nextBasDataExchange.setExchangeId(basDataExchange.getExchangeId());
			nextBasDataExchange.setTimeStamp(now);
			this.basDataExchangeService.updateBasDataExchange(nextBasDataExchange);


//
//			JSONObject jsonObject = JSONObject.fromObject(jsonStr);
//			if(jsonObject.get("state")!=null&& jsonObject.get("state").toString().equals("200")){
//
//				JSONArray rows = (JSONArray)jsonObject.get("rows");
//				JSONObject row = (JSONObject)rows.get(0);
//				System.out.println(row.get("itil"));
//
//
//
//			}

//			String jsonStr = "{\"msg\":\"错误或成功提示信息\",\"state\":\"200\",\"rows\":[{\"evdt\":\"350205100\",\"evid\":\"1\",\"evti\":\"标题\"}],\"total\":1}";
//			System.out.println("r1->"+jsonStr);
//			if(StringUtils.isNotBlank(jsonStr)){
//				JSONObject jsonObject = JSONObject.fromObject(jsonStr);
//				if(null!=jsonObject && jsonObject.get("state").toString().equals("200")){
//					Integer total = (Integer)jsonObject.get("total");
//					for(int i = 0; i<total;i++){
//						jsonStr = doGetData(i, pageSize, startTime, endTime);
//						System.out.println("r2->"+jsonStr);
//						jsonStr = "{\"msg\":\"错误或成功提示信息\",\"state\":\"200\",\"rows\":[{\"evdt\":\"350205100\",\"evid\":\"1\",\"evti\":\"标题\"}],\"total\":1}";
						//save(jsonStr);
//					}
//					BasDataExchange nextBasDataExchange = new BasDataExchange();
//					nextBasDataExchange.setExchangeId(basDataExchange.getExchangeId());
//					nextBasDataExchange.setTimeStamp(endTimeDate);
//					this.basDataExchangeService.updateBasDataExchange(nextBasDataExchange);
//				}
//			}
		}else{
			logger.error("未查到上一有效时间。");
		}
		logger.info("================Timer task stop================");
	}

	private UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		List<FunConfigureSetting> env_event = funConfigurationService.findConfigureSettingLatestList("ENV_EVENT", null, null, null, null, 0);
		for(FunConfigureSetting setting : env_event){
			if(setting.getTrigCondition().equals("userId")){
				if(StringUtils.isNotBlank(setting.getCfgVal()))
					userInfo.setUserId(Long.valueOf(setting.getCfgVal()));
			}
			if(setting.getTrigCondition().equals("partyId")){
				if(StringUtils.isNotBlank(setting.getCfgVal()))
					userInfo.setPartyId(Long.valueOf(setting.getCfgVal()));
			}
			if(setting.getTrigCondition().equals("userName")){
				if(StringUtils.isNotBlank(setting.getCfgVal()))
					userInfo.setUserName(setting.getCfgVal());
			}
			if(setting.getTrigCondition().equals("partyName")){
				if(StringUtils.isNotBlank(setting.getCfgVal()))
					userInfo.setPartyName(setting.getCfgVal());
			}
			if(setting.getTrigCondition().equals("orgId")){
				if(StringUtils.isNotBlank(setting.getCfgVal()))
					userInfo.setOrgId(Long.valueOf(setting.getCfgVal()));
			}
			if(setting.getTrigCondition().equals("catalogInfoId")){
				if(StringUtils.isNotBlank(setting.getCfgVal()))
					userInfo.setCatalogInfoId(Long.valueOf(setting.getCfgVal()));
			}
			if(setting.getTrigCondition().equals("orgName")){
				if(StringUtils.isNotBlank(setting.getCfgVal()))
					userInfo.setOrgName(setting.getCfgVal());
			}
			if(setting.getTrigCondition().equals("orgCode")){
				if(StringUtils.isNotBlank(setting.getCfgVal()))
					userInfo.setOrgCode(setting.getCfgVal());
			}
		}
		return userInfo;
	}

	private void save(String jsonStr){
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		JSONArray rows = jsonObject.getJSONArray("rows");
		for (Object tempJsonObj : rows){
			EventDisposal event = new EventDisposal();

			JSONObject jsonRows = (JSONObject)tempJsonObj;

			if(jsonRows.get("evdt")!=null){//事件所属地域
				String gridCode = jsonRows.get("evdt").toString();
				List<MixedGridInfo> listGrid = mixedGridInfoService.getMixedGridMappingListByOrgCode(gridCode);
				if(listGrid!=null && listGrid.size() >0){
					event.setGridCode(gridCode);
					event.setGridId(listGrid.get(0).getGridId());
				}else{
					List<MixedGridInfo> listGrids = mixedGridInfoService.getMixedGridMappingListByOrgCode("3502");
					event.setGridCode("3502");
					event.setGridId(listGrids.get(0).getGridId());
				}
			}else{
				List<MixedGridInfo> listGrid = mixedGridInfoService.getMixedGridMappingListByOrgCode("3502");
				event.setGridCode("3502");
				event.setGridId(listGrid.get(0).getGridId());
			}
			if(jsonRows.get("itil")!=null){//事件标题
				String eventName = jsonRows.get("itil").toString();
				event.setEventName(eventName);
			}
			if(jsonRows.get("acte")!=null){//发生时间
				String happenTimeStr = jsonRows.get("acte").toString();
				happenTimeStr = happenTimeStr.replace("T"," ").replace("Z","");
				event.setHappenTimeStr(happenTimeStr);
			}
			event.setType("3101");
			if(jsonRows.get("evad")!=null){//事发地点
				String occurred = jsonRows.get("evad").toString();
				event.setOccurred(occurred);
			}
			if(jsonRows.get("ihdt")!=null){//事发描述
				String content = jsonRows.get("ihdt").toString();
				event.setContent(content);
			}
			UserInfo userinfo = getUserInfo();
			Map<String, Object> stringObjectMap = eventDisposalDockingService.saveEventDisposalAndEvaluate(event, userinfo, "");
			System.out.println(stringObjectMap);

			String outEventId = "";
			if(jsonRows.get("id")!=null) {//事件所属地域
				outEventId = jsonRows.get("id").toString();
			}
			DataExchangeStatus dataExchangeStatus = new DataExchangeStatus();
			dataExchangeStatus.setExchangeFlag("1");
			dataExchangeStatus.setOppoSideBizCode(outEventId);
			dataExchangeStatus.setOwnSideBizCode(stringObjectMap.get("eventId").toString());
			dataExchangeStatus.setStatus("1");
			String xmlData = tempJsonObj.toString();
			System.out.println("xmlData.length()================================"+xmlData.length());
			if(xmlData.length() > 4000){
				xmlData = xmlData.substring(0, 4000);
			}
			dataExchangeStatus.setXmlData("");
			dataExchangeStatus.setSrcPlatform("017");
			dataExchangeStatusService.save(dataExchangeStatus);
		}
	}
}
