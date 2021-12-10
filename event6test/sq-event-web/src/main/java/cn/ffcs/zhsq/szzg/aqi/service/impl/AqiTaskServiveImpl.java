package cn.ffcs.zhsq.szzg.aqi.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;

import cn.ffcs.shequ.utils.StringUtils;
import cn.ffcs.shequ.utils.date.DateUtils;
import cn.ffcs.zhsq.mybatis.domain.szzg.aqi.ZgAQI;
import cn.ffcs.zhsq.szzg.aqi.service.IZgAQIService;
import cn.ffcs.zhsq.utils.AqiState;
import cn.ffcs.zhsq.utils.AqiUtil;
import cn.ffcs.zhsq.utils.ConstantValue;
public class AqiTaskServiveImpl extends ApplicationObjectSupport {
	 @Autowired
	 private IZgAQIService zgAQIServiceImpl;
	 List<Map<String, Object>> stationList;
	public void run() {
		String statioId=ConstantValue.AQI_CITY_STATION_ID;
		stationList=zgAQIServiceImpl.getStationListByCity(statioId);
		saveData("http://www.pm25.com/city/"+statioId+".html");
	}
	
	/**
	 * 保存各项数据
	 * @param city
	 * @param cityCode
	 */
	public  void  saveData(String url){
		Document doc = null;
		String statioId="";
		Map<String, Object> params=new HashMap<String, Object>();
		ZgAQI aqi=null;
		try {
		    doc = getDoc(url);
		    if(doc==null){
		    	return ;
		    }
		    String city_name=doc.select(".city_name").text();
		    if(stationList!=null&&stationList.size()>0){
		    	for (Map<String, Object> station : stationList) {
					if(station.get("STATIONNAME").equals(city_name)){
						statioId=station.get("STATIONID").toString();
						break;
					}
				}
		    }
//		    if(city_name.equals(AqiStationName.江阴.toString()))
//		    	statioId=AqiStationName.江阴.getStationId();
		    //网站更新时间（按小时）
		    String citydata_updatetime=doc.select(".citydata_updatetime").text().split("更新时间：")[1];
		    Date monitorTime=DateUtils.convertStringToDate(citydata_updatetime, "yyyy-MM-dd HH:mm");
		   
		    aqi=new ZgAQI();//城市  按小时
		    aqi.setMonitorTime(monitorTime);
		    aqi.setStationName(city_name);
		    Elements citydata_banner_opacity=doc.select(".citydata_banner_opacity");
			aqi.setAqi(citydata_banner_opacity.select(".cbol_aqi_num").text());
			aqi.setState(AqiState.valueOf(citydata_banner_opacity.select(".cbor_gauge span").text()).getIndex());
			aqi.setMainFomite(citydata_banner_opacity.select(".cbol_wuranwu_num").text().equals("")?"暂未统计":citydata_banner_opacity.select(".cbol_wuranwu_num").text());
			aqi.setPm25(citydata_banner_opacity.select(".cbol_nongdu_num_1").text());
		    aqi.setMonitorType("1");
		    aqi.setStatioId(statioId);
		    params.put("statioId", statioId);
		    params.put("monitorType", "1");
			params.put("updateTime", citydata_updatetime);
			if(!StringUtils.isEmpty(statioId)){
			    if(!zgAQIServiceImpl.findCountByStatioId(params))//每小时
			    	zgAQIServiceImpl.insert(aqi);
			    //过去14天
			    checkOrSaveDayByLast14(statioId,city_name,monitorTime, getValsBy30(doc));//过去14天
			    //最近24小时
				checkOrSaveDayByLast24(statioId, city_name, monitorTime, getValsByCity24(doc));
			    Elements pj_area_data_details=doc.select(".pj_area_data_details").eq(0).select("li");
	            for (Element element : pj_area_data_details) {
	            	saveByStations(element, monitorTime);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存各检测点的数据
	 * @param element
	 * @param monitorTime
	 * @throws Exception
	 */
	private void saveByStations(Element element,Date monitorTime ) throws Exception{
		Document areaDoc = null;
		String statioId="";
		Map<String, Object> params=new HashMap<String, Object>();
     	Elements pjadt_location=element.select(".pjadt_location");
    	String href="http://www.pm25.com"+pjadt_location.attr("href");//各检测站点地址
    	areaDoc=getDoc(new URI(href, false, "utf-8").toString());
    	ZgAQI aqi=new ZgAQI();//城市各检查点数据 按小时
    	aqi.setMonitorTime(monitorTime);
    	String stationName=pjadt_location.text();
    	aqi.setStationName(stationName);
    	aqi.setAqi(element.select(".pjadt_aqi").text());
    	aqi.setState(AqiUtil.getStateByAqi(element.select(".pjadt_aqi").text()));
    	aqi.setState(AqiState.valueOf(element.select(".pjadt_quality").text()).getIndex());
    	aqi.setMainFomite(element.select(".pjadt_wuranwu").text());
		element.select(".pjadt_pm25").select("em").remove();
		aqi.setPm25(element.select(".pjadt_pm25").text());
		aqi.setPm10(element.select(".pjadt_pm10").text().split(" ")[0]);
		aqi.setMonitorType("1"); 
	    if(stationList!=null&&stationList.size()>0){
	    	for (Map<String, Object> station : stationList) {
				if(station.get("STATIONNAME").equals(stationName)){
					statioId=station.get("STATIONID").toString();
					aqi.setStatioId(statioId);
					break;
				}
			}
	    }
//		if(stationName.equals(AqiStationName.虹桥邮政.toString())){
//			statioId=AqiStationName.虹桥邮政.getStationId();
//			aqi.setStatioId(statioId);
//		}else if(stationName.equals(AqiStationName.五星公园.toString())){
//			statioId=AqiStationName.五星公园.getStationId();
//			aqi.setStatioId(statioId);
//		}else if(stationName.equals(AqiStationName.第二实验小学.toString())){
//			statioId=AqiStationName.第二实验小学.getStationId();
//			aqi.setStatioId(statioId);
//		}
		params.put("monitorType", "1");
		params.put("statioId", statioId);
		params.put("updateTime", DateUtils.formatDate(monitorTime,"yyyy-MM-dd HH:mm"));
		if(!StringUtils.isEmpty(statioId)){
			//按小时
			 if(!zgAQIServiceImpl.findCountByStatioId(params))
			    	zgAQIServiceImpl.insert(aqi);
			checkOrSaveDayByLast14(statioId,stationName, monitorTime,getValsBy30(areaDoc));//最近14天
			checkOrSaveDayByLast24(statioId, stationName, monitorTime, getValsByStation24(areaDoc));//最近24小时
		}
	}
	
	/**
	 * 过去14天
	 */
	private void checkOrSaveDayByLast14(String statioId,String stationName,Date monitorTime,String valsBy30){
		Map<String, Object> params=new HashMap<String, Object>();
		ZgAQI aqi=null;
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(monitorTime);
		String[] vals=valsBy30.split(",");
		for (int i = vals.length-1; i>15; i--) {//遍历最近14天数据
			aqi=new ZgAQI();
			aqi.setMonitorType("2");
	    	aqi.setStatioId(statioId);
	    	aqi.setStationName(stationName);
	    	aqi.setAqi(vals[i]);
	    	aqi.setState(AqiUtil.getStateByAqi(vals[i]));
			calendar.add(Calendar.DATE, -1);
			String updateTime=DateUtils.formatDate(calendar.getTime(), DateUtils.PATTERN_DATE);
			aqi.setMonitorTime(DateUtils.formatDate(updateTime, DateUtils.PATTERN_DATE));
			params.put("monitorType", "2");
			params.put("statioId", statioId);
			params.put("updateTime", updateTime);
			if(!zgAQIServiceImpl.findCountByStatioId(params))
		    	zgAQIServiceImpl.insert(aqi);
		}
	}
	/**
	 * 最近24小时
	 * @param statioId
	 * @param stationName
	 * @param monitorTime
	 * @param valsBy30
	 */
	private void checkOrSaveDayByLast24(String statioId,String stationName,Date monitorTime,String valsBy24){
		Map<String, Object> params=new HashMap<String, Object>();
		ZgAQI aqi=null;
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(monitorTime);
		String[] vals=valsBy24.split(",");
		for (int i = 23; i>0; i--) {//遍历最近24小时天数据
			aqi=new ZgAQI();
			aqi.setMonitorType("1");
	    	aqi.setStatioId(statioId);
	    	aqi.setStationName(stationName);
	    	aqi.setAqi(vals[i]);
	    	aqi.setState(AqiUtil.getStateByAqi(vals[i]));
			calendar.add(Calendar.HOUR_OF_DAY, -1);
			String updateTime=DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd HH");
			aqi.setMonitorTime(DateUtils.formatDate(updateTime, "yyyy-MM-dd HH"));
			params.put("monitorType", "1");
			params.put("statioId", statioId);
			params.put("updateTime", updateTime);
			if(!zgAQIServiceImpl.findCountByStatioId(params))
		    	zgAQIServiceImpl.insert(aqi);
		}
	}
	
	
	/**
	 * 根据网站最后30天数据获取最后一天aqi
	 * @param doc
	 * @return
	 */
	public static String   getLastValBy30(Document doc){
		String scriptStr=getValsBy30(doc);
		return scriptStr.split(",")[(scriptStr.split(",").length-1)];
	}
	/**
	 * 根据网站最后30天数据
	 * @param doc
	 * @return
	 */
	public static String   getValsBy30(Document doc){
		String scriptStr=doc.select("script").last().html();
		scriptStr=scriptStr.split("data:\\[")[scriptStr.split("data:\\[").length-1].split("\\]")[0];
		return scriptStr;
	}
	
	private static String getLastHoursBy24(Document doc){
		String scriptStr=doc.select("script").last().html().split("var option_d30")[0];
		scriptStr=scriptStr.split("\",\"")[scriptStr.split("\",\"").length-1].split("\\\\u65e5")[1].split("\\\\u65f6")[0];
		return scriptStr;
	}
	public static String   getValsByStation24(Document doc){
		String scriptStr=doc.select("script").last().html().split("var option_d30")[0];
		scriptStr=scriptStr.split("data:\\[")[scriptStr.split("data:\\[").length-1].split("\\]")[0];
		return scriptStr;
		//return scriptStr.split(",")[(scriptStr.split(",").length-1)];
	}
	//城市过去24小时的aqi值
	public static String   getValsByCity24(Document doc){
		String scriptStr=doc.select("script").last().html().split("pg_content_30d")[0];
		scriptStr=scriptStr.split("data:\\[")[scriptStr.split("data:\\[").length-1].split("\\]")[0];
		return scriptStr;
	}
	/**
	 * 获取页面文档
	 * @param url
	 * @return
	 */
	public static Document getDoc(String url){
		InputStream is = null;
		Document doc = null;
		HttpClient client = new HttpClient();
		GetMethod method =null;
		Elements scripts=null;
		//url="http://www.pm25.com/city/jiangyin.html";
//		url="http://www.pm25.com/city/jinchang.html";
		try {
			method=	new GetMethod(url);
			client.executeMethod(method);
			is = method.getResponseBodyAsStream();
		    doc = Jsoup.parse(is, "utf-8", "");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(is!=null)is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return doc;
	}

}
