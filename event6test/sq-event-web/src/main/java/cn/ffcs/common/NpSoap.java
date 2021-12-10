package cn.ffcs.common;

import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author 
 * @version 
 */
public class NpSoap {
	
	
	public enum StrEnum{
        //多个个枚举值,注意名字并不是构造方法名
		
		postRequestType("requestMethod","POST"),
		paramStr("contentType","application/json");

        //枚举值所包含的属性
        private String key;
        private String value;

        //构造方法
        StrEnum(String key,String value){
            this.setKey(key);
            this.setValue(value);
        }
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
    }

	public static enum ContentTypeEnum {
		JSON("json","application/json"),
		FORM("form","application/x-www-form-urlencoded; charset=UTF-8");
		//枚举值所包含的属性
		private String key;
		private String value;
		//构造方法
		ContentTypeEnum(String key,String value){
			this.setKey(key);
			this.setValue(value);
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
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
		httpUrlConn.setRequestProperty("Content-Type", StrEnum.paramStr.getValue());
		httpUrlConn.setRequestProperty("accept", "*/*");
		httpUrlConn.setRequestProperty("connection", "Keep-Alive");
		//httpUrlConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
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
	
	
	public static JSONObject invokeOutInterface(String accessUrl,String requestType,String paramStr,ContentTypeEnum contentTypeEnum) throws Exception{
		URL url = new URL(accessUrl);
		HttpURLConnection httpUrlConn = null;
		httpUrlConn = (HttpURLConnection) url.openConnection();
		httpUrlConn.setConnectTimeout(30000);
		httpUrlConn.setReadTimeout(30000);
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		httpUrlConn.setRequestProperty("Content-Type",contentTypeEnum.getValue());
		httpUrlConn.setRequestProperty("accept", "*/*");
		httpUrlConn.setRequestProperty("connection", "Keep-Alive");
		//httpUrlConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
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
	
	public static InputStream invokeOutInterfaceStream(String accessUrl,String requestType,String contentType,String paramStr) throws Exception{
		URL url = new URL(accessUrl);
		HttpURLConnection httpUrlConn = null;
		httpUrlConn = (HttpURLConnection) url.openConnection();
		httpUrlConn.setConnectTimeout(30000);
		httpUrlConn.setReadTimeout(30000);
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		httpUrlConn.setRequestProperty("Content-Type",contentType);
		httpUrlConn.setRequestProperty("accept", "*/*");
		httpUrlConn.setRequestProperty("connection", "Keep-Alive");
		httpUrlConn.setRequestMethod(requestType);
		if ("GET".equalsIgnoreCase(requestType))
			httpUrlConn.connect();
		if (null != paramStr) {
			OutputStream outputStream = httpUrlConn.getOutputStream();
			outputStream.write(paramStr.getBytes("UTF-8"));// 注意编码格式，防止中文乱码
			outputStream.flush();
			outputStream.close();
		}
		InputStream inputStream = httpUrlConn.getInputStream();
		return inputStream;
	}
	
	
	public static JSONObject invokeOutInterface(String accessUrl,String requestType,String paramStr,String token) throws Exception{
		URL url = new URL(accessUrl);
		HttpURLConnection httpUrlConn = null;
		httpUrlConn = (HttpURLConnection) url.openConnection();
		httpUrlConn.setConnectTimeout(30000);
		httpUrlConn.setReadTimeout(30000);
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		httpUrlConn.setRequestProperty("Content-Type", StrEnum.paramStr.getValue());
		httpUrlConn.setRequestProperty("Authorization",token);
		httpUrlConn.setRequestProperty("accept", "*/*");
		httpUrlConn.setRequestProperty("connection", "Keep-Alive");
		//httpUrlConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
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
		InputStream inputStream = null;
		try {
			// 将返回的输入流转换成字符串
			inputStream = httpUrlConn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "{\"data\":{\"msg\":\"无权限访问该设备\"}}";
			return JSONObject.fromObject(msg);
		}
		
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
	
	public static final byte[] input2byte(InputStream inStream)  
            throws IOException {  
        try {
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
			byte[] buff = new byte[100];  
			int rc = 0;  
			while ((rc = inStream.read(buff, 0, 100)) > 0) {  
			    swapStream.write(buff, 0, rc);  
			}  
			byte[] in2b = swapStream.toByteArray();  
			return in2b;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(inStream != null)
					inStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        return null;
    }
	
	
	public static Date getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +2);//+1今天的时间加一天
        date = calendar.getTime();
        return date;
    }
	 
	//把系统时间转换为时间戳
	 public static String timestamp(){
	     Date date=new Date();
	     SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
	     String currentTime=null;
	     try {
	         currentTime=simpleDateFormat.format(getDay(date));
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
	
	     return currentTime;
	 }
	
	 //把时间戳转换为毫秒
	 public static String dateTimeMs(){
	     SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
	     long msTime = -1;
	     try {
	          msTime=simpleDateFormat.parse(timestamp()).getTime();
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
	     return String.valueOf(msTime);
	
	 }
	
	//获取秘钥
	public static String getSignature(String cid,String expire) {
		String signKey = "lingmou";// 请用申请的signKey
		Map<String, Object> signMap = new TreeMap<String, Object>();      
		signMap.put("type", "m3u8");      
		signMap.put("cid", cid);// cid为请求参数中的cid      
		signMap.put("expire", expire);        
		Iterator<String> iterator = signMap.keySet().iterator();      
		StringBuffer buffer = new StringBuffer();        
		while (iterator.hasNext()) {          
			String key = iterator.next();            
			buffer.append(key).append(signMap.get(key));       
			}        
		buffer.append(signKey);       
		String signature = DigestUtils.md5Hex(buffer.toString());
		
		System.out.println("::::::::::::::::"+signature);
		return signature;
		
	}
	
	
public static void main(String[] args) throws UnsupportedEncodingException {
		
	/*	String url="http://58.209.250.205:88/websites/_ext/yjcz/wybxMsgRemind.jsp";
		List<String> remindObj = new ArrayList<String>();
		String jgs = "jgs";
		String userLoginId = "{\"userLoginId\":\""+jgs+"\"}"; 
		
		remindObj.add(userLoginId);
	    String faultType ="房屋漏水";
	    
	    String createTime = "2019-11-19";
	    String repairId = "2019";
	    
	    
		//String remindMsg = "房屋漏水"; 
		String remindMsg = "faultType="+faultType+"&createTime="+createTime+"&repairId="+repairId; 
		
		String remindInfo = "{\"remindObj\":\""+remindObj+"\",\"remindMsg\":\""+remindMsg+"\"}"; 
		remindInfo = remindInfo.replace("\"[{", "[{");
		remindInfo = remindInfo.replace("}]\"", "}]");
		remindInfo = URLEncoder.encode(remindInfo,"UTF-8");
	    try {
	    	    url = url + "?remindInfo="+ remindInfo;
	            HttpClient client = new HttpClient();
	            GetMethod getMethod = new GetMethod(url);
	            int sendStatus = client.executeMethod(getMethod);
	            String response = getMethod.getResponseBodyAsString();
	            if (sendStatus == 200 ) {
	                if("1".equals(response.trim())){
	                    //1 成功 其余值失败
	                    System.out.println(11111111);
	                }else{
	                	System.out.println(00);
	                }
	            }else{
	            	System.out.println("异常");
	            }
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	
	
	
		System.out.println("::::::::::::;"+longToString(1574393242281l));
	}

	public static String longToString(long time){
		System.out.println("long:::::::::::::::"+time);
	    String result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
	   
	    System.out.println("result:::::::::::::"+result);
	    return result;
	}
	
}


