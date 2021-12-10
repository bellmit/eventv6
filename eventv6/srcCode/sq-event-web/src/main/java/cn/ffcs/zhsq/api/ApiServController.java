package cn.ffcs.zhsq.api;

import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.enforceRecorder.service.IEnforceRecorderService;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * @Description
 * @Author huangjianming
 * @Date 2021/3/9 14:33
 */
@Controller
@RequestMapping("/service/api")
public class ApiServController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="enforceRecorderServiceImpl")
    private IEnforceRecorderService enforceRecorderService;

    /**
     * 海康安防平台事件订阅的回调处理
     * eventType 事件类型
     * @param request
     * @param session
     */
    @ResponseBody
    @RequestMapping(value = "/eventCallBack")
    public String eventCallBack(HttpServletRequest request, HttpSession session) {
        String rawData = readData(request);
        System.out.println("rawData = [" + rawData + "]");
        Map map = JsonHelper.getMap(rawData); //String to JSONObject
        boolean result = false;
        try {
            result = enforceRecorderService.doEventCallBack(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result ? "success" : "fail";
    }

    private String readData(HttpServletRequest request) {
        BufferedReader br = null;

        try {
            br = request.getReader();
            String line = br.readLine();
            if (line == null) {
                return "";
            } else {
                StringBuilder ret = new StringBuilder();
                ret.append(line);

                while((line = br.readLine()) != null) {
                    ret.append('\n').append(line);
                }

                return ret.toString();
            }
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }

    /**
     * 获取监控点位状态信息
     * @param session
     * @param indexCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/loadEnforceRecorderStatus4Jsonp")
    public String loadEnforceRecorderStatus(HttpSession session,@RequestParam(value="indexCode") String indexCode,
                                            @RequestParam(value = "jsonpcallback") String jsonpcallback) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String resultJson = null;
        if(userInfo!=null && StringUtils.isNotBlank(indexCode)){
            resultJson = enforceRecorderService.fetchEnforceRecorderStatus(indexCode, userInfo.getOrgCode());
        }
        logger.info("resultJson:{}",resultJson);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + resultJson + ")";
        } else {
            jsonpcallback = resultJson;
        }
        return jsonpcallback;
    }

    /**
     * 同步执法仪列表信息
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/syncEnforceRecorders4Jsonp")
    public String syncEnforceRecorders(HttpSession session,
                                       @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                       @RequestParam(value = "resTypeId") long resTypeId,
                                       @RequestParam(value = "gridId", required = false) long gridId,
                                       @RequestParam(value = "jsonpcallback") String jsonpcallback) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        Map<String, Object> resultMap = null;
        if (userInfo != null) {
            Map<String,Object> params = new HashMap<>();
            params.put("pageNo",pageNo);
            params.put("pageSize",pageSize);
            params.put("resTypeId",resTypeId);
            params.put("gridId",gridId);
            resultMap = enforceRecorderService.syncEnforceRecorders(params, userInfo.getOrgCode());
        }
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + JsonHelper.getJsonString(resultMap) + ")";
        } else {
            jsonpcallback = JsonHelper.getJsonString(resultMap);
        }
        return jsonpcallback;
    }

    /**
     * 注册事件到海康平台
     * @param session
     * @param eventTypes
     * @param callbackUrl
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/rigistryEvent2HKPlatform")
    public String rigistryEvent2HKPlatform(HttpSession session,
                                           @RequestParam(value="eventTypes") String eventTypes,
                                           @RequestParam(value="callbackUrl", required = false) String callbackUrl,
                                           @RequestParam(value = "jsonpcallback") String jsonpcallback) throws Exception {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String resultJson = null;
        if(userInfo!=null && StringUtils.isNotBlank(eventTypes)){
            Map<String,Object> params = new HashMap<>();
            params.put("eventTypes",eventTypes);
            params.put("eventDest",callbackUrl);
            resultJson = enforceRecorderService.rigistryEvent2HKPlatform(params, userInfo.getOrgCode());
        }
        logger.info("resultJson:{}",resultJson);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + resultJson + ")";
        } else {
            jsonpcallback = resultJson;
        }
        return jsonpcallback;
    }

    /**
     * 查看在海康平台订阅的事件
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/eventSubscriptionView")
    public String eventSubscriptionView(HttpSession session,@RequestParam(value = "jsonpcallback") String jsonpcallback) throws Exception {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String resultJson = null;
        if(userInfo!=null){
            resultJson = enforceRecorderService.eventSubscriptionView(userInfo.getOrgCode());
        }
        logger.info("resultJson:{}",resultJson);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + resultJson + ")";
        } else {
            jsonpcallback = resultJson;
        }
        return jsonpcallback;
    }

    /**
     * 取消海康平台注册的事件
     * @param session
     * @param eventTypes
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/cancelEvent")
    public String cancelEvent(HttpSession session,
                                           @RequestParam(value="eventTypes") String eventTypes,
                                           @RequestParam(value = "jsonpcallback") String jsonpcallback) throws Exception {
        UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
        String resultJson = null;
        if(userInfo!=null && StringUtils.isNotBlank(eventTypes)){
            resultJson = enforceRecorderService.cancelEvent(eventTypes, userInfo.getOrgCode());
        }
        logger.info("resultJson:{}",resultJson);
        if(StringUtils.isNotBlank(jsonpcallback)) {
            jsonpcallback = jsonpcallback + "(" + resultJson + ")";
        } else {
            jsonpcallback = resultJson;
        }
        return jsonpcallback;
    }

}
