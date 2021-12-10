package cn.ffcs.zhsq.api.enforceRecorder.service;

import cn.ffcs.common.APICall;
import cn.ffcs.shequ.utils.JsonHelper;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.enforceRecorder.service.IEnforceRecorderService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执法仪设备对接抽象类
 * @Description
 * @Author huangjianming
 * @Date 2021/8/20 17:14
 */
public abstract class AbstractEnforceRecorderService implements IEnforceRecorderService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IFunConfigurationService funConfigurationService;

    @Override
    public String fetchEnforceRecorderStatus(String codes,String orgCode) {
        Map<String, Object> cfgMap = this.initCfg(orgCode);
        JSONObject paramBody = new JSONObject();
        paramBody.put("indexCodes", codes.split(","));
        return APICall.startReq(cfgMap.get("queryEnforceRecorderStatusUrl").toString(),paramBody);
    }

    @Override
    public boolean doEventCallBack(Map jsonData) throws Exception {
        if(isSupport()){
            return this.doEventCallBack(jsonData);
        }
        throw new IllegalAccessException("暂不支持的该方法调用！");
    }

    @Override
    public Map<String,Object> syncEnforceRecorders(Map<String, Object> params, String orgCode) {
        Map<String, Object> cfgMap = this.initCfg(orgCode);
        JSONObject jsonBody = new JSONObject();
        if (CommonFunctions.isNotBlank(params,"jsonBody")) {
            jsonBody = (JSONObject)params.get("jsonBody");
        }
        String resultJson = APICall.startReq(cfgMap.get("camerasDeviceListUrl").toString(), jsonBody);
        if (StringUtils.isNotBlank(resultJson)) {
            return JsonHelper.getMap(resultJson);
        }
        return null;
    }

    @Override
    public String rigistryEvent2HKPlatform(Map<String, Object> params, String orgCode) throws Exception{
        if(isSupport()){
            Map<String, Object> cfgMap = this.initCfg(orgCode);
            String eventTypes = null;
            String eventDest = null;
            if (CommonFunctions.isNotBlank(params,"eventTypes")) {
                eventTypes = params.get("eventTypes").toString();
            }else{
                throw new IllegalAccessException("参数eventTypes缺失！");
            }
            if (CommonFunctions.isBlank(params,"eventDest")) {
                String eventDomain = this.funConfigurationService.turnCodeToValue("HK_ENFORCE_RECORDER_CFG", "eventDomain", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
                eventDest = eventDomain+"/service/api/eventCallBack.applet";
            }else{
                eventDest = params.get("eventDest").toString();
            }
            JSONObject jsonBody = new JSONObject();
            List<Long> typeList = new ArrayList<>();
            String[] typeArr = eventTypes.split(",");
            for (String type:typeArr) {
                try {
                    typeList.add(Long.valueOf(type));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    throw new IllegalAccessException("eventType必须为数字类型！");
                }
            }
            jsonBody.put("eventTypes", typeList.toArray(new Long[typeList.size()]));
            jsonBody.put("eventDest", eventDest);
            return APICall.startReq(cfgMap.get("registryEventUrl").toString(),jsonBody);
        }
        throw new IllegalAccessException("暂不支持的该方法调用！");
    }

    @Override
    public String eventSubscriptionView(String orgCode) throws Exception{
        if(isSupport()) {
            Map<String, Object> cfgMap = this.initCfg(orgCode);
            JSONObject paramBody = new JSONObject();
            return APICall.startReq(cfgMap.get("eventSubscriptionViewUrl").toString(), paramBody);
        }
        throw new IllegalAccessException("暂不支持的该方法调用！");
    }

    @Override
    public String cancelEvent(String eventTypes, String orgCode) throws Exception {
        if(isSupport()) {
            Map<String, Object> cfgMap = this.initCfg(orgCode);
            JSONObject jsonBody = new JSONObject();
            List<Integer> typeList = new ArrayList<>();
            String[] typeArr = eventTypes.split(",");
            for (String type:typeArr) {
                try {
                    typeList.add(Integer.valueOf(type));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    throw new IllegalAccessException("eventType必须为数字类型！");
                }
            }
            jsonBody.put("eventTypes", typeList.toArray(new Integer[typeList.size()]));
            return APICall.startReq(cfgMap.get("cancelEventUrl").toString(),jsonBody);
        }
        throw new IllegalAccessException("暂不支持的该方法调用！");
    }


    protected abstract boolean isSupport();

    protected abstract void setDefaultValue4Cfg(Map<String,Object> cfgMap);

    private Map<String,Object> initCfg(String orgCode){
        String ip = this.funConfigurationService.turnCodeToValue("HK_ENFORCE_RECORDER_CFG", "host", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
        String port = this.funConfigurationService.turnCodeToValue("HK_ENFORCE_RECORDER_CFG", "port", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
        String appKey = this.funConfigurationService.turnCodeToValue("HK_ENFORCE_RECORDER_CFG", "appKey", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
        String secretKey = this.funConfigurationService.turnCodeToValue("HK_ENFORCE_RECORDER_CFG", "secretKey", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
        String camerasDeviceListUrl = this.funConfigurationService.turnCodeToValue("HK_ENFORCE_RECORDER_CFG", "camerasDeviceListUrl", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0);

//        String typeUrl = this.funConfigurationService.turnCodeToValue("HK_ENFORCE_RECORDER_CFG", "typeUrl", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
        Map<String,Object> cfgMap = new HashMap<>();
        cfgMap.put("host",ip);
        cfgMap.put("port",port);
        cfgMap.put("appKey",appKey);
        cfgMap.put("secretKey",secretKey);
        cfgMap.put("queryEnforceRecorderStatusUrl","/api/nms/v1/online/camera/get");
        cfgMap.put("registryEventUrl","/api/eventService/v1/eventSubscriptionByEventTypes");
        cfgMap.put("eventSubscriptionViewUrl","/api/eventService/v1/eventSubscriptionView");
        cfgMap.put("cancelEventUrl","/api/eventService/v1/eventUnSubscriptionByEventTypes");
        cfgMap.put("camerasDeviceListUrl",camerasDeviceListUrl);
        this.setDefaultValue4Cfg(cfgMap);
        ip = cfgMap.get("host").toString();
        port = cfgMap.get("port").toString();
        appKey = cfgMap.get("appKey").toString();
        secretKey = cfgMap.get("secretKey").toString();
        logger.info("[ip]={}",ip);
        logger.info("[port]={}",port);
        logger.info("[appKey]={}",appKey);
        logger.info("[secretKey]={}",secretKey);
        logger.info("[camerasDeviceListUrl]={}",cfgMap.get("camerasDeviceListUrl").toString());
        APICall.initCfg(ip,port,appKey,secretKey);
        return cfgMap;
    }
}
