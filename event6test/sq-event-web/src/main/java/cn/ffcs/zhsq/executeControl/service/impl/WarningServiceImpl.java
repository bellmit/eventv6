package cn.ffcs.zhsq.executeControl.service.impl;

import cn.ffcs.shequ.utils.Base64;
import cn.ffcs.shequ.utils.SSLClient;
import cn.ffcs.zhsq.executeControl.service.IWarningService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

@Service("warningServiceImpl")
@Transactional
public class WarningServiceImpl implements IWarningService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value(value="${DOMAIN_GANZHOU:jxsr-eye.antelopecloud.cn}")
    private String domain;

    @Value(value="${LOGINNAME_GANZHOU:18970005533_ysq}")
    private String loginName;

    @Value(value="${PASSWORD_GANZHOU:Kd_123456}")
    private String password;

    @Override
    public JSONObject searchList(HashMap<String, String> hashMap, String token) {

        String alarmOperationType =  hashMap.get("alarmOperationType");
        String sort =  hashMap.get("sort");
        String startDateStr =  hashMap.get("startDateStr");
        String endDateStr =  hashMap.get("endDateStr");
        String cids =  hashMap.get("cids");
        String controlLibraryId =  hashMap.get("controlLibraryId");
        String controlTaskId =  hashMap.get("controlTaskId");
        String controlObjectId =  hashMap.get("controlObjectId");
        String identityCardNumber =  hashMap.get("identityCardNumber");
        String name =  hashMap.get("name");
        String offset =  hashMap.get("offset");

        String url = "https://jxsr-eye.antelopecloud.cn/api/alarm/v1/alarmResult/queryAlarmResults";
        if (cids != null && !"".equals(cids)){
            StringBuilder params = new StringBuilder("{\"cids\":" + Arrays.toString(cids.split(",")) + "" +
                    ",\"libIds\":[\"" + controlLibraryId + "\"],\"taskIds\":[\"" + controlTaskId + "\"],\"monitorPersonId\":\"" + controlObjectId + "\"" +
                    ",\"monitorPersonKeywords\":\"" + name + "\"");
            if (!"".equals(startDateStr) && !"".equals(endDateStr)){
                params.append(",\"startTime\":\""+getTimeStamp(startDateStr)+"\",\"endTime\":\""+getTimeStamp(endDateStr)+"\"");
            }
            if (!"".equals(sort)){
                params.append(",\"sort\":[\""+sort+"\"]");
            }
            if(!"".equals(alarmOperationType)){
                params.append(",\"alarmOperationType\":" + alarmOperationType + "");
            }
            if(StringUtils.isNotEmpty(offset)){
                params.append(",\"offset\":" + offset + "");
            }
            params.append("}");
            JSONObject jsonObject = doPost(url, token, params.toString());
            return jsonObject;
        }

      /*  String param = null;
        if (offset != null){
             param = "{\"alarmTypes\":["+1+"],\"offset\":"+ offset +" }";
        }else {
             param = "{\"alarmTypes\":["+1+"]}";
        }

        JSONObject jsonObject = doPost(url, token, param);*/
        return null;
    }

    @Override
    public String getToken() {
        return this.login();
    }

    @Override
    public JSONObject findById(String id, String token) {
        String url = "https://"+domain+"/api/alarm/v1/alarmResult/alarmResults/" + id;
        String params = "{\"id\":\""+id+"\"}";
        JSONObject jsonObject = doPost(url, token, params);
        if (jsonObject != null){
            return jsonObject;
        }
        return null;
    }

    @Override
    public String save(String id, String isEffective, String operationDetail,String token) {
        String url = "https://"+domain+"/api/alarm/v1/alarmResult/handleAlarmResult";
        String params = null;
        if (isEffective != null){
            params = "{\"id\":\""+id+"\",\"isEffective\":"+isEffective+",\"operationDetail\":\""+operationDetail+"\"}";
        }else {
            params = "{\"id\":\""+id+"\",\"operationDetail\":\""+operationDetail+"\"}";
        }

        JSONObject jsonObject = doPost(url, token, params);
        if (jsonObject != null){
            String newId = jsonObject.getString("id");
            return newId;
        }
        return null;
    }

    @Override
    public String searchHlsUrl(JSONObject jsonObject, String token) {
        String captureTime = jsonObject.getString("captureTime").substring(0,10);
        Long beginTime = Long.valueOf(captureTime) - 15;
        Long endTime = Long.valueOf(captureTime) + 15;
        String captureUrl = "https://"+domain+"/api/staticResource/v2/video/queryHistoryAddress";
        String cid = jsonObject.getString("cid");
        String captureParams = "{\"cid\":\""+cid+"\",\"beginTime\":\""+ beginTime + "\",\"endTime\":\""+endTime+"\"}";
        JSONArray jsonArray = doPostArray(captureUrl, token, captureParams);
        if (jsonArray != null){
            String hls = jsonArray.getJSONObject(0).getString("url");
            return hls;
        }
        return null;
    }

    //赣州登录获取token
    public String login(){
        String url = "https://"+domain+"/api/user/v1/loginWithoutIdentifyCode";
        String passwordBase = new String(Base64.encode(password.getBytes()));
        String params = "{\"loginName\":\""+loginName+"\",\"userPassword\":\""+passwordBase+"\"}";
        Object result = doPost(url,null,params);
        if(result!=null){
            JSONObject jsonObject = (JSONObject)result;
            String token = jsonObject.get("token").toString();
            logger.info("token = "+token);
            return token;
        }else{
            logger.info("赣州登录失败！");
        }
        return null;
    }

    public static JSONObject doPost(String url,String token,String params){
        HttpClient httpclient = null;
        try {
            httpclient = new SSLClient();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        // 执行请求操作，并拿到结果（同步阻塞）
        HttpResponse response = null;
        try {
            httpPost.setHeader("Content-Type","application/json;charset=UTF-8");
            if(token!=null){
                httpPost.setHeader("Authorization", token);
            }
            // 封装请求参数
            if (params != null){
                HttpEntity httpEntity = new StringEntity(params, ContentType.APPLICATION_JSON);
                httpPost.setEntity(httpEntity);
            }
            response = httpclient.execute(httpPost, new BasicHttpContext());
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                JSONObject jsonObj = JSONObject.parseObject(jsonString);
                if ("0".equals(jsonObj.get("code").toString())){
                    return jsonObj.getJSONObject("data");
                }else {
                    //logger.error("获取羚眸视频图像智能应用平台接口失败");
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray doPostArray(String url, String token, String params){
        HttpClient httpclient = null;
        try {
            httpclient = new SSLClient();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        // 执行请求操作，并拿到结果（同步阻塞）
        HttpResponse response = null;
        try {
            httpPost.setHeader("Content-Type","application/json;charset=UTF-8");
            if(token!=null){
                httpPost.setHeader("Authorization", token);
            }
            // 封装请求参数
            if (params != null){
                HttpEntity httpEntity = new StringEntity(params, ContentType.APPLICATION_JSON);
                httpPost.setEntity(httpEntity);
            }
            response = httpclient.execute(httpPost, new BasicHttpContext());
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                JSONObject jsonObj = JSONObject.parseObject(jsonString);
                if ("0".equals(jsonObj.get("code").toString())){
                    return jsonObj.getJSONArray("data");
                }else {
                    //logger.error("获取羚眸视频图像智能应用平台接口失败");
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTimeStamp(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        try {
            if (time != null && !"".equals(time)){
                Date date = simpleDateFormat.parse(time);
                return String.valueOf(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
