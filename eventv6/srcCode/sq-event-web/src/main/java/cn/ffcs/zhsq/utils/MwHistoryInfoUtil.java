package cn.ffcs.zhsq.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.ffcs.zhsq.mybatis.domain.historyInfo.SmallWaterMeterHis;






public class MwHistoryInfoUtil {
	private static Logger logger = LoggerFactory.getLogger(MwHistoryInfoUtil.class);
	
	private static final String appKey = "0001";
	
	
	public static final String SERVER = "http://27.155.67.52:10001/iot/platform/v1.0.0/device/query?";
	
	
	public static JSONObject getBackJsonMsg(
			String deviceId,String deviceType,int pageNo,
			int pageSize,String alarmType,String startTime,String endTime
			) {
		
		String url = "http://27.155.67.52:10001/iot/platform/v1.0.0/device/query?";
		url += "app_key=0001";
		url += "&device_id=" +deviceId;
		//url += "&device_type=" +deviceType;
		url += "&page_no=" +pageNo;
		url += "&page_size=" +pageSize;
		
		if(!("").equals(alarmType) && alarmType != null){
			url += "&alarm_type=" +alarmType;
		}
		if(!("").equals(alarmType) && alarmType != null){
			url += "&device_type=0";
		}else{
			url += "&device_type=" +deviceType;
		}
		
		if(!("").equals(startTime) && startTime != null){
			url += "&start_time=" +startTime;
		}
		if(!("").equals(endTime) && endTime != null){
			url += "&end_time=" +endTime;
		}
		JSONObject json = null;
		try {
			json = invokeOutInterface(url, "POST", null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("调用url:{}",url);
		logger.info("调用接口返回结果:{}",json);
		
		return json;
	}
	
	
	
	
	
	
	public static void main(String[] args) throws Exception {
		//小水表可以有数据
		//http://27.155.67.52:10001/iot/platform/v1.0.0/device/query?app_key=150751581616010007&device_id=201701070150000001437&device_type=100001&start_time=2017-10-13&end_time=2017-10-14
		String url = "http://27.155.67.52:10001/iot/platform/v1.0.0/device/query?app_key=150751581616010007&device_id=201701070150000001437&device_type=100001&start_time=2017-10-13&end_time=2017-10-14";
		//String url = "http://27.155.67.52:10001/iot/platform/v1.0.0/device/query?app_key=150751581616010007&device_id=163628222645282787&device_type=100004&start_time=2017-10-13&end_time=2017-10-14";
		System.out.println(url);
		
		JSONObject json = invokeOutInterface(url, "POST", null);
		
		int resCode = json.getInt("res_code");
		JSONArray array1 =	json.getJSONArray("device_list");
		if(resCode == 0){
			
			JSONArray array = null;
			if(json.getJSONArray("device_list") != null){
				array = json.getJSONArray("device_list");
			}
			
			List<SmallWaterMeterHis> list = (List<SmallWaterMeterHis>) array.toCollection(array, SmallWaterMeterHis.class);
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i).getSignal_intensity());
			}
			
//				logger.info("调用接口返回结果:{}",json);
//				logger.info("调用接口返回list:{}",array);
		}else{
			String resMsg = json.getString("res_message");
			logger.info("调用接口返回信息:{}",resMsg);
			
		}
		
	}
	
	public static JSONObject invokeOutInterface(String accessUrl,String requestType,String paramStr) throws Exception{
		URL url = new URL(accessUrl);
		HttpURLConnection httpUrlConn = null;
		httpUrlConn = (HttpURLConnection) url.openConnection();
		httpUrlConn.setConnectTimeout(30000);
		httpUrlConn.setReadTimeout(30000);
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		//httpUrlConn.setRequestProperty("Content-Type","application/json");
		httpUrlConn.setRequestProperty("accept", "*/*");
		httpUrlConn.setRequestProperty("connection", "Keep-Alive");
		httpUrlConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
		httpUrlConn.setRequestMethod(requestType);
		if ("GET".equalsIgnoreCase(requestType))
			httpUrlConn.connect();
		if (null != paramStr) {
			OutputStream outputStream = httpUrlConn.getOutputStream();
			// 注意编码格式，防止中文乱码
			outputStream.write(paramStr.getBytes("UTF-8"));
			outputStream.flush();
			outputStream.close();
		}
		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		String str = null;
		StringBuffer buffer = new StringBuffer();
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		// 释放资源
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		String josnStr = buffer.toString();
		if(josnStr != null && josnStr.contains("HTTP/1.1")){
			josnStr = josnStr.substring(0,josnStr.indexOf("HTTP/1.1"));
		}
		JSONObject json = JSONObject.fromObject(josnStr);
		return json;
	}
}
