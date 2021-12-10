package cn.ffcs.shequ.utils;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class APIHttpClient {
	// 接口地址  
    private static String apiURL = "http://192.168.3.67:8080/lkgst_manager/order/order";  
    private Log logger = LogFactory.getLog(this.getClass());
    private static final String pattern = "yyyy-MM-dd HH:mm:ss:SSS";  
    private HttpClient httpClient = null;  
    private HttpPost method = null;  
    private long startTime = 0L;  
    private long endTime = 0L;  
    private int status = 0;  
  
    /** 
     * 接口地址 
     *  
     * @param url 
     */  
    public APIHttpClient(String url) {
  
        if (url != null) {  
            this.apiURL = url;  
        }  
        if (apiURL != null) {  
            httpClient = new DefaultHttpClient();  
            method = new HttpPost(apiURL);  
  
        }  
    }  
  
    /** 
     * 调用 API 
     *  
     * @param parameters 
     * @return 
     */  
    public String post(String parameters) {  
        String body = null;  
        logger.info("parameters:" + parameters);
  
        if (method != null & parameters != null) {
            try {  
  
                // 建立一个NameValuePair数组，用于存储欲传送的参数  
                method.addHeader("Content-type","application/json; charset=utf-8");  
                method.setHeader("Accept", "application/json");  
                method.setEntity(new StringEntity(parameters));
                startTime = System.currentTimeMillis();
  
                HttpResponse response = httpClient.execute(method);  
                  
                endTime = System.currentTimeMillis();  
                int statusCode = response.getStatusLine().getStatusCode();  
                  
                logger.info("statusCode:" + statusCode);
                logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
                if (statusCode != HttpStatus.SC_OK) {  
                    logger.error("Method failed:" + response.getStatusLine());
                    status = 1;  
                }  
  
                // Read the response body  
                body = EntityUtils.toString(response.getEntity());  
  
            } catch (IOException e) {  
                // 网络错误  
                status = 3;  
            } finally {  
                logger.info("调用接口状态：" + status);
            }  
  
        }  
        return body;  
    }  
  
    public static void main(String[] args) {  
//        APIHttpClient ac = new APIHttpClient("http://oaknt.tpddns.cn:28249/ivm-api-data_input/ivm/face/queryInfo");
//        JsonArray arry = new JsonArray();
//        JsonObject j = new JsonObject();
//        j.addProperty("platform_id", "Global_Eyes");
//        j.addProperty("similarity", "80");
//        j.addProperty("start_time", "2017-01-21 15:35:37");
//        j.addProperty("end_time", "2017-01-21 15:35:47");
//        j.addProperty("timestamp", "2017-01-24 10:19:58");
//        j.addProperty("sign", "pjXEzTI%2ByGgpyZ5Jv4FUa6Ls4RgRC81R8mM7bylTfr%2BXCqz%2FrLfIwUGKTKQPjmXfcuTATqJHUTI%3D");
//        arry.add(j);
//        String s = "";
//        System.out.println(ac.post(arry.toString().substring(1, arry.toString().length()-1)));

//        APIHttpClient ac = new APIHttpClient(url);
//        JsonArray arry = new JsonArray();
//        JsonObject j = new JsonObject();
//        j.addProperty("reqData", "Global_Eyes");
//        arry.add(j);
//        String s = "";
//        System.out.println(ac.post(arry.toString().substring(1, arry.toString().length()-1)));


        String url = "http://12345.yw.nc.gov.cn/nc12345/inter/order/addNetWorkOrder.inter?action=addNetWorkOrder";
        String param = "{\"reqData\":{\"paras\":{\"formOrigin\":\"综治网格化\",\"contentText\":\"工单内容\",\"cusType\":\"1\",\"cusSex\":\"1\",\"cusName\":\"张三\",\"cusPhone\":\"13333333333\",\"cusAddress\":\"市民地址\",\"idCardNum\":\"340827198006300089\",\"whConPerInfo\":\"1\",\"areaCode\":\"南昌市\"}}}";

        APIHttpClient ac = new APIHttpClient(url);

        String result = ac.post(param);

        System.out.println("=====================================");
        System.out.println(result);
        System.out.println("=====================================");
    }
  
    /** 
     * 0.成功 1.执行方法失败 2.协议错误 3.网络错误 
     *  
     * @return the status 
     */  
    public int getStatus() {  
        return status;  
    }  
  
    /** 
     * @param status 
     *            the status to set 
     */  
    public void setStatus(int status) {  
        this.status = status;  
    }  
  
    /** 
     * @return the startTime 
     */  
    public long getStartTime() {  
        return startTime;  
    }  
  
    /** 
     * @return the endTime 
     */  
    public long getEndTime() {  
        return endTime;  
    }  
}
