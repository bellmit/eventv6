package cn.ffcs.zhsq.hotChangeLog4jLevel.controller;

import cn.ffcs.zhsq.util.Log4jConfigUtil;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:动态修改 log4j 日志级别、不重启服务器
 * @Author: ztc
 * @Date: 2018/9/21 16:32
 */
@Controller("/hotChangeLog4jLevelController")
@RequestMapping("/zhsq/hotChangeLog4jLevel")
public class HotChangeLog4jLevelController {
    /**
     * 跳转到日志配置页面
     * */
    @RequestMapping("/toIndex")
    public String toIndex(HttpSession session, HttpServletRequest request, ModelMap map){
    	Map<String, Object> resultMap = null;
        Map<String,Object> param = new HashMap<>();
        Log4jConfigUtil hotChangeLog4jLevelUtil = new Log4jConfigUtil();

        try {
        	resultMap = hotChangeLog4jLevelUtil.getLoggerNameAndLevel(param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.addAllAttributes(resultMap);

        return "/zzgl/hotChangeLog4jLevel/log4jLoggerLevel.ftl";
    }

    /**
     * 根据传参动态改变日志级别
     * @param param
     *        loggerName 日志名称
     *        loggerLevel 当前日志级别
     * */
    @ResponseBody
    @RequestMapping("/changeLog4jLevel")
    public Map<Object,String> changeLog4jLevel(HttpSession session, @RequestParam Map<String,Object> param){

        String loggerName = "";
        String loggerLevel = "";
        String result = "fail";

        if (CommonFunctions.isNotBlank(param,"loggerName")){
            loggerName = param.get("loggerName").toString();
        }
        if (CommonFunctions.isNotBlank(param,"loggerLevel")){
            loggerLevel = param.get("loggerLevel").toString();
        }

        Map<Object,String> resultMap = new HashMap<>();
        Log4jConfigUtil hotChangeLog4jLevelUtil = new Log4jConfigUtil();

        if (hotChangeLog4jLevelUtil.updateLoggerLevel(loggerName,loggerLevel)) {
            result = "success";
        }

        resultMap.put("result",result);

        return resultMap;
    }

    /**
     * 根据日志名称获取日志级别
     * */
    @ResponseBody
    @RequestMapping("/getLoggerLevel")
    public Map<String,Object> getLoggerLevel(HttpSession session,@RequestParam Map<String,Object> param){

        String loggerName = "";
        String result = "fail";
        Map<String,Object> resultMap = new HashMap<>();
        Log4jConfigUtil hotChangeLog4jLevelUtil = new Log4jConfigUtil();

        if (CommonFunctions.isNotBlank(param,"loggerName")){
            loggerName = param.get("loggerName").toString();
        }

        try{
            loggerName = hotChangeLog4jLevelUtil.getLoggerLevelByLoggerName(loggerName);
            result = "success";
        }catch (Exception e){
            e.printStackTrace();
        }

        resultMap.put("loggerName",loggerName);
        resultMap.put("result",result);

        return resultMap;
    }


    /**
     * 获取classes下 log4j.xml 配置文件中所有的日志名、日志输出地址（容器）和对应的日志级别
     * */
    @SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping("/getLoggerNameAndLevel")
    public List<Map<String,Object>> getLoggerNameAndLevel(HttpServletRequest request){
    	Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String,Object>> loggerAttrList = new ArrayList<>();
        Map<String,Object> param = new HashMap<>();
        Log4jConfigUtil hotChangeLog4jLevelUtil = new Log4jConfigUtil();


        try {
        	resultMap = hotChangeLog4jLevelUtil.getLoggerNameAndLevel(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(CommonFunctions.isNotBlank(resultMap, "loggerAttrList")) {
        	loggerAttrList = (List<Map<String,Object>>)resultMap.get("loggerAttrList");
        }

        return loggerAttrList;
    }
}
