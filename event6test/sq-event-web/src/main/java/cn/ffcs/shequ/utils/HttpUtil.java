package cn.ffcs.shequ.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import cn.ffcs.shequ.utils.http.CustomAuthenticator;
import cn.ffcs.shequ.utils.http.MyX509TrustManager;
import cn.ffcs.zhsq.mybatis.domain.map.taxi.CarHisTrack;
import cn.ffcs.zhsq.taxi.config.TaxiConfig;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.google.gson.Gson;




import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;



public class HttpUtil {
	
	
	/**
	 * post??????
	 * 
	 * @param urlStr
	 *            ??????????????????????????????
	 * @param params
	 *            ??????
	 * @return
	 */
	public static String postRequst(String strURL, Map<String, Object> params) {
		String result = "";
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(strURL);
		for (String key : params.keySet()) {
			String value = params.get(key).toString();
			postMethod.addParameter(key, value);
		}
		int sendStatus = 0;
		postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		try {
			sendStatus = client.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
     * ?????????URL??????GET???????????????
     * 
     * @param url
     *            ???????????????URL
     * @param param
     *            ???????????????????????????????????? name1=value1&name2=value2 ????????????
     * @return URL ????????????????????????????????????
     */
	
	public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // ?????????URL???????????????
            URLConnection connection = realUrl.openConnection();
            // ???????????????????????????
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ?????????????????????
            connection.connect();
            // ???????????????????????????
            Map<String, List<String>> map = connection.getHeaderFields();
            // ??????????????????????????????
            for (String key : map.keySet()) {
          //      System.out.println(key + "--->" + map.get(key));
            }
            // ?????? BufferedReader??????????????????URL?????????  utf-8?????????????????????
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("??????GET?????????????????????" + e);
            e.printStackTrace();
        }
        // ??????finally?????????????????????
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

	/**
	 * post??????
	 * 
	 * @param urlStr ??????????????????????????????
	 * @param params  ??????
	 * @return
	 */
	public static String post3(String strURL,String params) {
		 try {
		        URL url = new URL(strURL);// ???????????? 
		        HttpURLConnection connection = (HttpURLConnection) 
		        url.openConnection(); 
		        connection.setDoOutput(true); 
		        connection.setDoInput(true); 
		        connection.setUseCaches(false); 
		        connection.setInstanceFollowRedirects(true); 
		        connection.setRequestMethod("POST");// ??????????????????   
		        connection.setRequestProperty("Accept","application/json");// ???????????????????????????   
		 
		        connection.setRequestProperty("Content-Type","application/json");// ???????????????????????????   
		        connection.connect(); OutputStreamWriter out = new OutputStreamWriter( connection.getOutputStream(),"UTF-8");// utf-8??????   
		        out.append(params); out.flush(); out.close(); // ????????????   
		        int length = (int) connection.getContentLength();// ????????????   
		        InputStream is = connection.getInputStream(); 
		        if (length != -1){
		            byte[] data = new byte[length]; 
		            byte[] temp = new byte[512]; 
		            int readLen = 0; int destPos = 0; 
		            while ((readLen = is.read(temp)) > 0){
		                System.arraycopy(temp, 0, data, destPos, readLen); 
		                destPos += readLen; 
		            }
		            String result = new String(data, "UTF-8");
		            System.out.println(result); 
		            return result; 
		        } 
		    } catch (Exception e) {
		        // TODO: handle exception
		        e.printStackTrace();
		    }
		    return "error";
	}
    
	/**
	 * post??????
	 * 
	 * @param urlStr ??????????????????????????????
	 * @param params  ??????
	 * @return
	 */
	public static String request(String urlStr, Object params) {
        /*********************************************
     	1.????????????URL?????? URL url 
     	2.??????HttpURLConnection???????????????????????????????????? (HttpURLConnection) url.openConnection();
     	3.????????????
     	4.????????????????????????:if (conn.getResponseCode() != 200) throw new RuntimeException("??????url??????");
     	5.??????????????????????????????:InputStream is = conn.getInputStream();
     	6.????????????conn.disconnect();
         **********************************************/
		HttpURLConnection conn = null;
		try {

			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");

			// ?????????????????????
			String paramsStr = new Gson().toJson(params);
			OutputStream os = conn.getOutputStream();
			os.write(paramsStr.getBytes("UTF-8"));
			os.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));

			String output;
			String resultJson = null;
			while ((output = br.readLine()) != null) {
				resultJson = output;
			}
			return resultJson;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return null;
	}
	public static void main(String[] args) throws Exception{
		//String s = doPost("http://gd.fjsq.org:8450/zhsq_event/services/eventDisposalServImpl/shuntEvent?xmlData=<data><auth><username>mybk</username><password>75f62d22f3e3f3defbcb6bebfe997043</password></auth><event><gridCode>350102006001</gridCode><x>119.29159048319872</x><y>26.112267152106543</y><eventName>????????????</eventName><happenTimeStr>2017-04-10 22:41:45</happenTimeStr><occurred>??????????????????????????????198???</occurred><content>????????????????????????</content><oppoSideBusiCode>213308</oppoSideBusiCode><bizPlatform>015</bizPlatform><eventType>02</eventType><source>23</source><creatorId>459342</creatorId><creatorName>?????????</creatorName><contactTel>15759651613</contactTel><advice>???</advice><registerTimeStr>2017-04-10 22:41:45</registerTimeStr><tasks><task/></tasks><attrs><attr><attrBiz>0</attrBiz><attrURL>http://qfqz.cunnar.com/mass/share/cee3dfcb86ec4e42a6e5e15dec069f23/file/81680</attrURL><attrBASE64>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ceaLj3NsPV9jWhVtflaf3O58xaHr2v8AhwznSZZLfz9vmfuFfdtzj7ynHU1sf8LG8cf9BGT/AMA4v/iK+haK41hJxVlN/wBfM+gqZ/h6kuephotvq7N/+knzbpXhnxD4x1gyvDOfOfdPeTqQq++T1OOgH8q6X4ieGdR0TxBb65p0DvaRJDtkVd3lPGAo3D0wq89K9toqlg48rV9e5nLiKs68aiglFJrl8nbr8ux4YPjN4jA/489LP/bKT/4uuT8TeJr3xVqSX99FbxypEIQIFIXAJPcnn5jX0/RSlhak1aU/w/4JVDPMLQnz0sMk/wDF/wAA8P8AiP8A8if4K/68v/acVeh/DP8A5J5pX0l/9GvXW0VtChy1HO/SxwYnM/bYSOF5bWk3e/e+lreZ82eN/CV34W1ubET/ANnyuWtpwvy4PO3PZh0x7ZrpdH+M+o2VkkGo6dHfyIoUTCYxM3u3BBP5V7XNDFcRNFNGkkbDDI6ggj3BqlBoOjWpzb6TYRH1jtkX+Qr6R5tSrUlDFUuZrrex4nsmneLPH/EPjvV/GGn2kem+HLuGS2u0uo54Waf5lDYGAg9c/hWq/wAZb+xRI9R8MNHPj5sztEG9wrISB+Jr1sAAAAYAqKe3guojFcQxzRnqkihgfwNYfXsK0oSoe6v7zvr5j5Jb3Pnfxz45/wCEz+wf8S77H9k8z/lv5m/ft/2RjG39a6z4O+GbqO7m1+6haOAxGK23jG/JGWHsAMZ75PpXpsfhnQIpBJHoemo453LaRg/nitQAAAAYArXEZrB4b6th4csfW/mKNJ83NJnnnxl/5E22/wCv5P8A0B65Hw38Vv8AhH/D9ppX9i/aPs6keb9q2bssT02HHX1r3GivnJ0ZOfPGVvke9h8yoQwqw1ejzpO/xNfkjyP/AIXf/wBS9/5O/wD2uuL8S+JJPGfiezvI7BoHCJAsKv5hbDE56D+90xX0hRUTw9SatKenob4fNsJhp+0o4a0v8bf5o8i+N/39D+k//tOm+IviTplwt3ol/wCGxeJBI0IL3GMlSV3DC5U8djmvX6KqVCTlKUZWv5GNHNKMaNOlVpc3Jez5mt3foj5wsPDvi6XQ724sLa/h048vArsvmj2T+PHfitfwv480rwpa+UnhgC+A2zXHn/O579VJX6DiveKKiOEcGnGWvpc6qufxrxlCvRvFvpJx++25BZ3H2uxt7nbs86JZNuc4yM4zU9FFdh867N6BRRRQIKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAP/2Q==</attrBASE64></attr></attrs></event></data>", "");
		//System.out.println(s);
		CustomAuthenticator authenticator = new CustomAuthenticator("NPApi","NPApi_1234",true);
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Content-Type", "application/json");
		//headers.put("Authorization", "Baisc TlBBcGk6TlBBcGlfMTIzNA==");
		//String url = "https://218.6.54.78:8822/api/Busline/Get";
		//JSONObject json = invokeOutInterface(url, "POST", "", headers, authenticator);
		//String url = "http://27.155.67.52:10001/iot/platform/v1.0.0/device/query?app_key=150751581616010007&device_id=201701070150000001437&device_type=100001&start_time=2017-10-13&end_time=2017-10-14";
		//JSONObject json = invokeOutInterface(url, "POST", "", null, null);
		
//		String url = "http://218.6.54.78:8888/taxiapi"+TaxiConfig.URL_TAXI;
//		JSONObject json = invokeOutInterface(url, "POST", "1244", headers, authenticator);
//		System.out.println(json);
		
		
		/*int status = json.getInt("Status");
		if(status == 2000){
			JSONArray array = json.getJSONObject("Content").getJSONArray("Records");
			String jsonArrStr = array.toString();
			List<String> list = ReflectUtils.getAllFieldName(Busline.class);
			for(String fieldName : list){
				System.out.println(ReflectUtils.convert2DbField(fieldName, false)+"\t"+fieldName);
				jsonArrStr = jsonArrStr.replaceAll(ReflectUtils.convert2DbField(fieldName, false), fieldName);
			}
			System.out.println(jsonArrStr);
			List<Busline> list2 = JsonUtils.json2GenericList(jsonArrStr, Busline.class);
			System.out.println(list2.get(0));
		}*/
		
		/*
		String[] devIds = new String[]{"18054900344","18094180410","18039720435","18065769164","18039720614",
				"18065765840","18039756594","18065774584","18094183314","18065769141","18050384841",
				"18065766741","18039720584","18046493044","18065766204","18039720564","18065766774","18065765841"
		};
		String url = "http://218.6.54.78:8888/taxiapi"+TaxiConfig.URL_TAXI_HISTORY_TRACK;
		for(String d : devIds){
			JSONObject paramJson = new JSONObject();
			paramJson.put("dev_id", d);
			JSONObject json = invokeOutInterface(url, "POST", paramJson.toString(), headers, authenticator);
			System.out.println(d+"==="+json);
		}
		*/
//		String url = "http://218.6.54.78:8888/taxiapi"+TaxiConfig.URL_TAXI_HISTORY_TRACK;
//		JSONObject paramJson = new JSONObject();
//		paramJson.put("dev_id", "18065765840");
//		JSONObject json = invokeOutInterface(url, "POST", paramJson.toString(), headers, authenticator);
//		System.out.println("json==="+json);
		
//		String url = "http://218.6.54.78:8888/taxiapi"+TaxiConfig.URL_TAXI_LAST_POSITION_BY_USERID;
//		JSONObject paramJson = new JSONObject();
//		paramJson.put("UserID", 1244);
//		JSONObject json = invokeOutInterface(url, "POST", paramJson.toString(), headers, authenticator);
//		System.out.println(json);
		
		String url = "http://218.6.54.78:8888/taxiapi"+TaxiConfig.URL_TAXI_LAST_POSITION_BY_DEVID;
		JSONObject paramJson = new JSONObject();
		paramJson.put("dev_id", "18065769164");
		for(int i = 0 , len = 100 ; i < len ; i++){
			JSONObject json = invokeOutInterface(url, "POST", paramJson.toString(), headers, authenticator);
			System.out.println(json);
			Thread.sleep(1000);
		}
	}
	
	public static String jsonPost(String url, String param){
		System.out.println(url+param);
		String paramStr = param;
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			ClientHttpRequest request = factory.createRequest(new URI(url), HttpMethod.POST);
			byte[] bytes = paramStr.getBytes();
			HttpHeaders headers = request.getHeaders();
			headers.set("Content-Type", "application/json;charset=utf-8");
			headers.setContentLength(bytes.length);
			request.getBody().write(paramStr.getBytes("utf-8"));
			ClientHttpResponse response = request.execute();
			System.out.println(response.getStatusCode());

			InputStream in = response.getBody();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
//			System.out.println(sb.toString());
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String restTemplate(String mUrl,String mRequestXml){
		String returnXml = ""; // ?????????????????????????????????

		try {

			//???????????????????????????
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			requestFactory.setConnectTimeout(1000);// ????????????
			requestFactory.setReadTimeout(1000);

			//????????????????????????????????????????????????????????????????????? HttpClient
			RestTemplate restTemplate = new RestTemplate(requestFactory);

			//??????HTTP?????????????????????????????????
			HttpHeaders requestHeaders = new HttpHeaders();
			// requestHeaders.set("Accept", "text/");
			requestHeaders.set("Accept-Charset", "utf-8");
			requestHeaders.set("Content-type", "text/xml; charset=utf-8");// ????????????

			//???????????????????????????????????????
			HttpEntity<String> entity = new HttpEntity<String>(mRequestXml, requestHeaders);
			Map<String ,Object> urlVariables = new HashMap<String ,Object>();
			urlVariables.put("data",mRequestXml);
			returnXml = restTemplate.postForObject(mUrl,null, String.class, urlVariables);


			// ???????????????RestTemplate???????????????org.springframework.http.converter.StringHttpMessageConverter?????????
			// StringHttpMessageConverter ???????????? ISO-8859-1????????????
			returnXml = new String(returnXml.getBytes("ISO-8859-1"), "utf-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		System.out.println("restTemplate???????????????????????? \n" + returnXml);
		return returnXml;
	}

	/**
	 * 
	 * @param url
	 * @param param ??????????t=1&b=2
	 * @return
	 */
	public static String doPost(String url, String param) {

		String paramStr = param;
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			ClientHttpRequest request = factory.createRequest(new URI(url), HttpMethod.POST);
			// FileBody bin = new FileBody(new File(filePath));
			// reqEntity.addPart("Filedata", bin);
			byte[] bytes = paramStr.getBytes();
			HttpHeaders headers = request.getHeaders();
			//   multipart/form-data   ?????????????????????????????????????????????????????????????????? 
			//   application/x-www-form-urlencoded      form????????????????????????key/value????????????????????????
			headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			//headers.set("Content-Type", "application/json;charset=utf-8");
			// hc.setRequestProperty("Content-Type",
			// "multipart/form-data; boundary=ABCD");
			headers.setContentLength(bytes.length);
			OutputStreamWriter out = new OutputStreamWriter(request.getBody(), "UTF-8");
			out.write(paramStr);
			out.flush();
			out.close();
			// OutputStream out = request.getBody();
			// out.write(bytes);
			ClientHttpResponse response = request.execute();
			InputStream in = response.getBody();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

//			JSONObject json = JSONObject.fromObject(sb.toString());
//			String status = json.get("status").toString();
//			if (status != null && "1".equals(status)) {
//				String desc = json.get("desc").toString();
//				if (desc != null && "????????????????????????????????????,???????????????".equals(desc)) {
//					/*
//					 * key=getTokenKey(tokenKey.getLoginUrl(),tokenKey.getUserName
//					 * (),tokenKey.getPassword()); if (key != null) {
//					 * dataButtService.insertTokenKey(key); doBodyPost(url,
//					 * param); }
//					 */
//					dataButtService.insertTokenKey("");
//					throw new Exception(desc);
//				}
//			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param url
	 * @param param ??????????t=1&b=2
	 * @return
	 */
	public static String doPost2(String url, String param) {

		String paramStr = param;
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			ClientHttpRequest request = factory.createRequest(new URI(url), HttpMethod.POST);
			// FileBody bin = new FileBody(new File(filePath));
			// reqEntity.addPart("Filedata", bin);
			byte[] bytes = paramStr.getBytes();
			HttpHeaders headers = request.getHeaders();
			//   multipart/form-data   ?????????????????????????????????????????????????????????????????? 
			//   application/x-www-form-urlencoded      form????????????????????????key/value????????????????????????
			//headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			headers.set("Content-Type", "application/json;charset=utf-8");
			// hc.setRequestProperty("Content-Type",
			// "multipart/form-data; boundary=ABCD");
			headers.setContentLength(bytes.length);
			OutputStreamWriter out = new OutputStreamWriter(request.getBody(), "UTF-8");
			out.write(paramStr);
			out.flush();
			out.close();
			// OutputStream out = request.getBody();
			// out.write(bytes);
			ClientHttpResponse response = request.execute();
			InputStream in = response.getBody();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
//			JSONObject json = JSONObject.fromObject(sb.toString());
//			String status = json.get("status").toString();
//			if (status != null && "1".equals(status)) {
//				String desc = json.get("desc").toString();
//				if (desc != null && "????????????????????????????????????,???????????????".equals(desc)) {
//					/*
//					 * key=getTokenKey(tokenKey.getLoginUrl(),tokenKey.getUserName
//					 * (),tokenKey.getPassword()); if (key != null) {
//					 * dataButtService.insertTokenKey(key); doBodyPost(url,
//					 * param); }
//					 */
//					dataButtService.insertTokenKey("");
//					throw new Exception(desc);
//				}
//			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String postBody(String urlPath, String json) throws Exception {
		try {
			// Configure and open a connection to the site you will send the
			// request
			URL url = new URL(urlPath);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			// ??????doOutput?????????true??????????????????urlConnection????????????
			urlConnection.setDoOutput(true);
			// ??????????????????????????????????????????????????????application/x-www-form-urlencoded??????
			urlConnection.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			// ??????????????????????????????
			OutputStreamWriter out = new OutputStreamWriter(
					urlConnection.getOutputStream());
			// ????????????????????????Body
			out.write(json);
			out.flush();
			out.close();

			// ????????????????????????
			InputStream inputStream = urlConnection.getInputStream();
			String encoding = urlConnection.getContentEncoding();
			String body = IOUtils.toString(inputStream, encoding);
			if (urlConnection.getResponseCode() == 200) {
				System.out.println(body);
				return body;
			} else {
				throw new Exception(body);
			}
		} catch (IOException e) {
////			logger.error(e.getMessage(), e);
//			throw e;
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ????????? URL ??????POST???????????????
	 *
	 * @param url
	 *            ??????????????? URL
	 * @param param
	 *            ???????????????????????????????????? name1=value1&name2=value2 ????????????
	 * @return ????????????????????????????????????
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();// ????????????
			// ???????????????????????????
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("contentType", "UTF-8");
			conn.setRequestProperty("accept","*/*");
			conn.setRequestProperty("connection","Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ??????POST??????????????????????????????
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());// ??????URLConnection????????????????????????
			out.print(param); // ??????????????????
			out.flush();// flush??????????????????

			Map<String, List<String>> map = conn.getHeaderFields();// ??????  ??? ????????????  ??????
			// ??????????????????????????????
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}

			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));// ??????BufferedReader??????????????????URL?????????
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			System.out.println("?????? POST ?????????????????????"+e);
			e.printStackTrace();
		} finally{//???????????????????????????
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result.toString();
	}

	/**
	 * http post??????
	 * @param url						??????
	 * @param postContent				post???????????????param1=value??m2=value2??m3=value3
	 * @return
	 * @throws IOException
	 */
	public static String httpPostRequest(URL url, String postContent) throws Exception{
		OutputStream outputstream = null;
		BufferedReader in = null;
		try
		{
			URLConnection httpurlconnection = url.openConnection();
			httpurlconnection.setConnectTimeout(10 * 1000);
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setUseCaches(false);
			OutputStreamWriter out = new OutputStreamWriter(httpurlconnection
					.getOutputStream(), "UTF-8");
			out.write(postContent);
			out.flush();

			StringBuffer result = new StringBuffer();
			in = new BufferedReader(new InputStreamReader(httpurlconnection
					.getInputStream(),"UTF-8"));
			String line;
			while ((line = in.readLine()) != null)
			{
				result.append(line);
			}
			return result.toString();
		}
		catch(Exception ex){
			throw new Exception("post???????????????" + ex.getMessage());
		}
		finally
		{
			if (outputstream != null)
			{
				try
				{
					outputstream.close();
				}
				catch (IOException e)
				{
					outputstream = null;
				}
			}
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (IOException e)
				{
					in = null;
				}
			}
		}
	}
	
	public static JSONObject doBodyPost(String url, String param) {
		System.out.println(url+param);
		String paramStr = param;
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			ClientHttpRequest request = factory.createRequest(new URI(url), HttpMethod.POST);
			// FileBody bin = new FileBody(new File(filePath));
			// reqEntity.addPart("Filedata", bin);
			byte[] bytes = paramStr.getBytes();
			HttpHeaders headers = request.getHeaders();
			headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			// hc.setRequestProperty("Content-Type",
			// "multipart/form-data; boundary=ABCD");
			headers.setContentLength(bytes.length);
			OutputStreamWriter out = new OutputStreamWriter(request.getBody(), "UTF-8");
			out.write(paramStr);
			out.flush();
			out.close();
			// OutputStream out = request.getBody();
			// out.write(bytes);
			ClientHttpResponse response = request.execute();
			InputStream in = response.getBody();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			System.out.println(sb.toString());
//			JSONObject json = JSONObject.fromObject(sb.toString());
//			String status = json.get("status").toString();
//			if (status != null && "1".equals(status)) {
//				String desc = json.get("desc").toString();
//				if (desc != null && "????????????????????????????????????,???????????????".equals(desc)) {
//					/*
//					 * key=getTokenKey(tokenKey.getLoginUrl(),tokenKey.getUserName
//					 * (),tokenKey.getPassword()); if (key != null) {
//					 * dataButtService.insertTokenKey(key); doBodyPost(url,
//					 * param); }
//					 */
//					dataButtService.insertTokenKey("");
//					throw new Exception(desc);
//				}
//			}
			return JSONObject.fromObject(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ????????????url????????????Bitmap
	 * @param url
	 * @return
	 */
	public static InputStream returnBitMap(String path) {
		URL url = null;
		InputStream is =null;
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();//??????HttpURLConnection??????,??????????????????????????????????????????.
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();	//??????????????????????????????
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	public static void inputstreamtofile(InputStream ins, File file) throws IOException {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
	}

	/**
	 * ?????????????????????????????????
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static  byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}
	

	public static byte[] fileToBytes(String filePath) throws FileNotFoundException,
			IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				filePath));
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] temp = new byte[1024];
		int size = 0;
		while ((size = in.read(temp)) != -1) {
			out.write(temp, 0, size);
		}
		in.close();
		byte[] pictureByte = out.toByteArray();
		return pictureByte;
	}
	
	/**
	 * ??????HTTP/HTTPS??????,??????JSONObject??????(??????http???https??????)
	 * @param accessUrl ??????url
	 * @param requestType ????????????
	 * @param body ?????????
	 * @param headers ?????????   ??????null,???content-type???:application/x-www-form-urlencoded; charset=UTF-8
	 * @param authenticator http basic ??????,???????????????null
	 * @return
	 * @throws Exception
	 */
	public static JSONObject invokeOutInterface(String accessUrl,String requestType,String body,Map<String,String> headers,CustomAuthenticator authenticator) throws Exception{
		boolean isHttps = accessUrl.toLowerCase().startsWith("https:");
		URL url = new URL(accessUrl);
		HttpURLConnection httpUrlConn = null;
		
		if(isHttps){
			// ??????SSLContext?????????????????????????????????????????????????????????
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");//TLS
			sslContext.init(null, tm, new java.security.SecureRandom());
			// ?????????SSLContext???????????????SSLSocketFactory??????
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			HttpsURLConnection.setDefaultSSLSocketFactory(ssf);
			//????????????????????????
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			    public boolean verify(String hostname, SSLSession session) {
			      return true;
			    }
			});
		}
		if(isHttps){
			httpUrlConn = (HttpsURLConnection) url.openConnection();
		}else{
			httpUrlConn = (HttpURLConnection) url.openConnection();
		}
		if(authenticator != null && !authenticator.isOrginal()){//HTTP basic ??????,?????????????????????????????????
			Authenticator.setDefault(authenticator);
		}else if(authenticator != null && authenticator.isOrginal()){//HTTP basic ??????,????????????????????????
			httpUrlConn.setRequestProperty("Authorization",authenticator.getBasicAuthorization());
		}
		httpUrlConn.setConnectTimeout(30000);
		httpUrlConn.setReadTimeout(30000);
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		//httpUrlConn.setRequestProperty("Content-Type","application/json");
		httpUrlConn.setRequestProperty("accept", "*/*");
		httpUrlConn.setRequestProperty("connection", "Keep-Alive");
		if(headers != null && headers.containsKey("Content-Type".toLowerCase())){
			httpUrlConn.setRequestProperty("Content-Type", headers.get("Content-type".toLowerCase()));
		}else{
			httpUrlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		}
		if(headers != null){
			for(Entry<String,String> entry : headers.entrySet()){
				if(headers.containsKey("Content-type".toLowerCase()))
					continue;
				httpUrlConn.setRequestProperty(entry.getKey(),entry.getValue());
			}
		}
		httpUrlConn.setRequestMethod(requestType);
		if ("GET".equalsIgnoreCase(requestType))
			httpUrlConn.connect();
		if (null != body) {
			OutputStream outputStream = httpUrlConn.getOutputStream();
			// ???????????????????????????????????????
			outputStream.write(body.getBytes("UTF-8"));
			outputStream.flush();
			outputStream.close();
		}
		// ???????????????????????????????????????
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
		// ????????????
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
	
	/**
	 * ???????????????????????????
	 * @param devId ??????ID
	 * @param stime ????????????
	 * @param etime ????????????
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<CarHisTrack> getHistoryTrail(String url,CustomAuthenticator authenticator,JSONObject paramJson){
		List<CarHisTrack> list = null;
		try {
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("Content-Type", "application/json;charset=utf-8");
			JSONObject json = HttpUtil.invokeOutInterface(url, "POST", paramJson.toString(), headers, authenticator);
			
			if(json.getInt("Status") != 2000)
				return null;
			
			JSONObject content = json.getJSONObject("Content");
			if(content.isNullObject() || content.isEmpty())
				return null;
			
			JSONArray jsonArray = content.getJSONArray("Records");
			if(jsonArray.isEmpty() || jsonArray == null)
				return null;
			
			list = JsonHelper.getObjectList(jsonArray.toString(), CarHisTrack.class);
			for(CarHisTrack ct : list){
				ct.setX((double)ct.getLongtitude()/1000000);
				ct.setY((double)ct.getLatitude()/1000000);
				ct.setLocateTime(ct.getGtime());
//				System.out.println("{x:" + (double) ct.getLongtitude()
//						/ 1000000 + ", y:" + (double) ct.getLatitude()
//						/ 1000000 + ",gtime:" + ct.getGtime() + "},");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	
	/**
     * ??????Post????????????????????????????????????
     * @param url ????????????????????????
     * @param srd ?????????????????????
     * @return
     */
	public static String SendBusiFormByPostRequest(String url,String requestDataStr){
	    	int statusCode = -1;
			try {
				HttpClient httpClient=new HttpClient();
				HttpMethodBase hmb=createPostMethod(url, requestDataStr, 30000);
				statusCode = httpClient.executeMethod(hmb);
				String res = "";
				if(statusCode == HttpStatus.SC_OK){
					//????????????
					res = hmb.getResponseBodyAsString();
					System.out.println("??????????????????! ????????????:");
					System.out.println("http??????????????????:statusCode="+res);
					return res;
		        }else{
		        	//????????????
		        	System.out.println("??????????????????! http??????????????????:statusCode="+statusCode);
		        	res = "??????????????????! http??????????????????:statusCode="+statusCode;
		        	return res;
		        }
			} catch (Exception e) {
				//??????????????????
				System.out.println("??????????????????,??????????????????:");
				System.out.println(e.getStackTrace());
			} 
	    	return "";
	}

	//??????post??????
	public static HttpMethodBase createPostMethod(String url,String msg, int timeout) {
	        PostMethod method = null;
	        try {
	            method = new PostMethod(url);
	            RequestEntity se = new StringRequestEntity(msg, "application/json", "UTF-8");
	            method.setRequestEntity(se);
	            //??????????????????????????????????????????
	            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
	            //?????????????????????
	            method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, timeout);
	            method.releaseConnection();
	        } catch ( Exception e) {
	        	e.printStackTrace();
	        }
	        return method;
	}
	
}
