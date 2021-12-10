package cn.ffcs.zhsq.times.service;

import cn.ffcs.cookie.service.UserCookieService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.utils.APIHttpClient;
import cn.ffcs.shequ.utils.HttpUtil;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.FunConfigureSetting;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.dataExchange.service.IBasDataExchangeService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusService;
import cn.ffcs.zhsq.dataExchange.service.IDataExchangeStatusTwoWayService;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.ITimerTaskService;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.BasDataExchange;
import cn.ffcs.zhsq.mybatis.domain.dataExchange.DataExchangeStatus;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


public class TimerTonganTaskServImpl extends ApplicationObjectSupport implements ITimerTaskService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

	@Override
	public void run(){
		String run = funConfigurationService.changeCodeToValue("TONGAN_EVENT","RUN", IFunConfigurationService.CFG_TYPE_FACT_VAL);
		if(StringUtils.isNotBlank(run) && "true".equals(run)){
			execute();
		}
	}

	private String doGetData(int pageNo, int pageSize, String startTime, String endTime) {
		String url = "http://zhhw.mysinosoft.com/tadc/incident/list/06";

		JSONObject params = new JSONObject();
		params.put("pageNo",1);
		params.put("pageSize",10);
//		params.put("beginTime",startTime);
//		params.put("endTime",endTime);
		params.put("beginTime","20170101000000");
		params.put("endTime","20170101120000");
		APIHttpClient ac = new APIHttpClient(url);
		String result = ac.post(params.toString());
		System.out.println(result);
		return result;
	}

	@Override
	public void execute(){
		logger.info("================TONGAN Timer task start================");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("exchangeType","002");//
		BasDataExchange basDataExchange = basDataExchangeService.findBasDataExchange(params);
		if (basDataExchange != null){
			Date timeStamp = basDataExchange.getTimeStamp();
			String startTime = DateUtils.format(timeStamp, "yyyyMMddHHmmss");

			Calendar cal = Calendar.getInstance();
			cal.setTime(timeStamp);
			cal.add(Calendar.DATE, -10);

			String endTime = DateUtils.format(cal.getTime(), "yyyyMMddHHmmss");

			int pageNo = 0;
			int pageSize = 200;

			String jsonStr = doGetData(pageNo, pageSize, endTime, startTime);

			save(jsonStr);
//			BasDataExchange nextBasDataExchange = new BasDataExchange();
//			nextBasDataExchange.setExchangeId(basDataExchange.getExchangeId());
//			nextBasDataExchange.setTimeStamp(cal.getTime());
//			this.basDataExchangeService.updateBasDataExchange(nextBasDataExchange);

		}else{
			logger.error("未查到上一有效时间。");
		}
		logger.info("================TONGAN Timer task stop================");
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
		JSONArray rows = jsonObject.getJSONArray("list");
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
			dataExchangeStatus.setXmlData(tempJsonObj.toString());
			dataExchangeStatus.setSrcPlatform("017");
			dataExchangeStatusService.save(dataExchangeStatus);
		}
	}
}
