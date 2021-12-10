package cn.ffcs.zhsq.executeControl.controller;


import cn.ffcs.zhsq.executeControl.service.IMonitorTaskService;
import cn.ffcs.zhsq.executeControl.service.IWarningService;
import cn.ffcs.zhsq.mybatis.domain.executeControl.MonitorTask;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller("warningController")
@RequestMapping("/zhsq/event/warning")
public class WarningController {

    @Autowired
    private IWarningService warningService;

    @Autowired
    private IMonitorTaskService monitorTaskService; //注入布控任务管理模块服务

    /**
     * 列表页面
     * @param request
     * @param session
     * @param map
     * @param myControlLibraryId
     * @param controlLibraryId
     * @param controlTaskId
     * @param controlObjectId
     * @param identityCardNumber
     * @param name
     * @return
     */
    @RequestMapping("/index")
    public Object index(HttpServletRequest request, HttpSession session, ModelMap map,Long myControlLibraryId,
                        String controlLibraryId,String controlTaskId,String controlObjectId,
                        String identityCardNumber,String name) {
        map.put("sysDomain", App.SYSTEM.getDomain(session));
        map.put("uiDomain", App.UI.getDomain(session));
        String token = warningService.getToken();
        try {
            MonitorTask monitorTask = monitorTaskService.searchById(myControlLibraryId, token);
            map.put("cids",monitorTask.getDeviceIds());
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("controlLibraryId",controlLibraryId);
        map.put("controlTaskId",controlTaskId);
        map.put("controlObjectId",controlObjectId);
        map.put("identityCardNumber",identityCardNumber);
        map.put("name",name);
        map.put("alarmOperationType", ConstantValue.ALARM_OPERATION_TYPE);
        map.put("alarmTypes", ConstantValue.ALARM_TYPES);
        return "/zzgl/executeControl/list-warning.html";
    }

    /**
     * 列表数据
     * @param request
     * @param session
     * @param map
     * @param alarmOperationType
     * @param sort
     * @param startDateStr
     * @param endDateStr
     * @param cids
     * @param controlLibraryId
     * @param controlTaskId
     * @param controlObjectId
     * @param identityCardNumber
     * @param name
     * @param offset
     * @return
     */
    @ResponseBody
    @RequestMapping("/listData")
    public Object listData(HttpServletRequest request, HttpSession session, ModelMap map, String alarmOperationType,
                               String sort, String startDateStr, String endDateStr, String cids, String controlLibraryId,
                               String controlTaskId, String controlObjectId, String identityCardNumber, String name,String offset) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("alarmOperationType",alarmOperationType);
        hashMap.put("sort",sort);
        hashMap.put("startDateStr",startDateStr);
        hashMap.put("endDateStr",endDateStr);
        hashMap.put("cids",cids);
        hashMap.put("controlLibraryId",controlLibraryId);
        hashMap.put("controlTaskId",controlTaskId);
        hashMap.put("controlObjectId",controlObjectId);
        hashMap.put("identityCardNumber",identityCardNumber);
        hashMap.put("name",name);
        hashMap.put("offset",offset);
        String token = warningService.getToken();
        JSONObject jsonObject = warningService.searchList(hashMap,token);

        return jsonObject;
    }

    /**
     * 详情页面
     * @param request
     * @param session
     * @param map
     * @param id
     * @return
     */
    @RequestMapping("/detail")
    public Object view(HttpServletRequest request, HttpSession session, ModelMap map,
                       String id) {
        String token = warningService.getToken();
        JSONObject jsonObject  = warningService.findById(id,token);
        if (jsonObject != null){
            //获取录像播放地址
            String hlsUrl = warningService.searchHlsUrl(jsonObject,token);
            map.put("hlsUrl",hlsUrl);
        }
        map.put("jsonObject",jsonObject);
        HashMap<String, String> hashMap = new HashMap<>();
        JSONObject jsonObject1 = warningService.searchList(hashMap, token);
        JSONArray list = jsonObject1.getJSONArray("list");
        map.put("list",list);
        return "/zzgl/executeControl/detail-warning.html";
    }

    /**
     * 保存数据
     * @param request
     * @param session
     * @param map
     * @param id
     * @param isEffective
     * @param operationDetail
     * @return
     */
    @ResponseBody
    @RequestMapping("/save")
    public Object save(HttpServletRequest request, HttpSession session, ModelMap map,
                       String id,String isEffective,String operationDetail) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String token = warningService.getToken();
        String result = "fail";
        String newId = warningService.save(id,isEffective,operationDetail,token);
        if (newId != null && !"".equals(newId)) {
            result = "success";
        }
        resultMap.put("result",result);
        return resultMap;
    }
}
