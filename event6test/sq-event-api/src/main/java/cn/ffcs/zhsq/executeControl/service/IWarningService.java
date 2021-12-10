package cn.ffcs.zhsq.executeControl.service;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

public interface IWarningService {

    JSONObject searchList(HashMap<String, String> hashMap, String token);

    String getToken();

    JSONObject findById(String id, String token);

    String save(String id, String isEffective, String operationDetail, String token);

    String searchHlsUrl(JSONObject jsonObject, String token);
}
