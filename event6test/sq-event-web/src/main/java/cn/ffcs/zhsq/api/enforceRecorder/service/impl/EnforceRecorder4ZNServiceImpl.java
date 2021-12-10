package cn.ffcs.zhsq.api.enforceRecorder.service.impl;

import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResInfo;
import cn.ffcs.shequ.zzgl.service.res.IResInfoService;
import cn.ffcs.zhsq.api.enforceRecorder.service.AbstractEnforceRecorderService;
import cn.ffcs.zhsq.devicecollectdata.service.DeviceGpsDataService;
import cn.ffcs.zhsq.mybatis.domain.devicecollectdata.DeviceGpsData;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description 周宁执法仪设备对接实现
 * @Author huangjianming
 * @Date 2021/8/20 17:10
 */
@Service("enforceRecorder4ZNServiceImpl")
public class EnforceRecorder4ZNServiceImpl extends AbstractEnforceRecorderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static String ZN_CAMERAS_DEVICELIST_URL = "/api/resource/v1/camera/advance/cameraList";

    @Autowired
    private DeviceGpsDataService deviceGpsDataService;
    @Autowired
    private IResInfoService resInfoService;

    @Override
    protected void setDefaultValue4Cfg(Map<String, Object> cfgMap) {
        if (CommonFunctions.isBlank(cfgMap,"host")) {
            cfgMap.put("host",ConstantValue.ZN_HK_PLATFORM_IP);
        }
        if (CommonFunctions.isBlank(cfgMap,"port")) {
            cfgMap.put("port",ConstantValue.ZN_HK_PLATFORM_PORT);
        }
        if (CommonFunctions.isBlank(cfgMap,"appKey")) {
            cfgMap.put("appKey",ConstantValue.ZN_HK_PLATFORM_APPKEY);
        }
        if (CommonFunctions.isBlank(cfgMap,"secretKey")) {
            cfgMap.put("secretKey",ConstantValue.ZN_HK_PLATFORM_SECRET_KEY);
        }
        if (CommonFunctions.isBlank(cfgMap,"camerasDeviceListUrl")) {
            cfgMap.put("camerasDeviceListUrl", ZN_CAMERAS_DEVICELIST_URL);
        }
    }

    @Override
    public boolean doEventCallBack(Map jsonData) {
        boolean result = false;
        if (CommonFunctions.isNotBlank(jsonData,"params")) {
            JSONObject params = (JSONObject) jsonData.get("params");
            if (params.containsKey("events")) {
                JSONArray events = (JSONArray)params.get("events");
                if (events != null) {
                    for (Iterator iter1 = events.iterator(); iter1.hasNext();) {
                        JSONObject jo = (JSONObject) iter1.next();
                        Object eventType = jo.get("eventType");
                        if (eventType != null) {
                            if (ConstantValue.ZN_HK_PLATFORM_EVENTTYPE_GPS.equals(eventType.toString())) { //851969
                                this.handleGPSData(jo);
                                result = true;
                            }else if(ConstantValue.ZN_HK_PLATFORM_EVENTTYPE_ALARM.equals(eventType.toString())){
                                //TODO
                                result = true;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> syncEnforceRecorders(Map<String, Object> params, String orgCode) {
        Map<String,Object> resultMap = new HashMap();
        resultMap.put("msg","同步失败");
        int countSuc = 0;
        int pageNo = 1;
        int pageSize = 100;
        long resTypeId = -1;
        long gridId = -1;

        if (CommonFunctions.isNotBlank(params,"pageNo")) {
            pageNo = Integer.valueOf(params.get("pageNo").toString());
        }
        if (CommonFunctions.isNotBlank(params,"pageSize")) {
            pageSize = Integer.valueOf(params.get("pageSize").toString());
        }
        if (CommonFunctions.isNotBlank(params,"resTypeId")) {
            resTypeId = Long.valueOf(params.get("resTypeId").toString());
        }
        if (CommonFunctions.isNotBlank(params,"gridId")) {
            gridId = Long.valueOf(params.get("gridId").toString());
        }
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo",pageNo);
        jsonBody.put("pageSize",pageSize);
        params.put("jsonBody",jsonBody);
        Map<String, Object> jsonMap = super.syncEnforceRecorders(params, orgCode);
        if (CommonFunctions.isNotBlank(jsonMap,"data")) {
            JSONObject data = (JSONObject) jsonMap.get("data");
            if (data.containsKey("list")) {
                JSONArray list = (JSONArray)data.get("list");
                if (list != null) {
                    for (Iterator iter = list.iterator(); iter.hasNext();) {
                        JSONObject jo = (JSONObject) iter.next();
                        String cameraIndexCode = jo.getString("cameraIndexCode");
                        String cameraName = jo.getString("cameraName");
                        Object createTime = jo.get("createTime");
                        System.out.println("createTime = [" + dealDateFormat(createTime.toString()) + "]" );
                        ResInfo oldResInfo = resInfoService.findResInfoByResno(cameraIndexCode,"enforce_recorder");
                        if (oldResInfo == null) {
                            ResInfo resInfo = new ResInfo();
                            resInfo.setResName(cameraName);
                            resInfo.setResno(cameraIndexCode);
                            resInfo.setResTypeId(resTypeId);
                            resInfo.setGridId(gridId);
                            resInfoService.saveResInfoReturnId(resInfo);
                            ++ countSuc;
                        }else{
                            oldResInfo.setResName(cameraName.toString());
                            resInfoService.updateResInfo(oldResInfo);
                            ++ countSuc;
                        }
                    }
                    resultMap.put("msg", list.size() == countSuc?"同步成功":"同步失败");
                }
            }
        }
        return resultMap;
    }

    @Override
    protected boolean isSupport() {
        return true;
    }

    private void handleGPSData(JSONObject json){
        JSONObject data = (JSONObject)json.get("data");
        JSONArray gpsCollection = (JSONArray)data.get("gpsCollection");
        for (Iterator iter2 = gpsCollection.iterator(); iter2.hasNext();) {
            DeviceGpsData gpsData = new DeviceGpsData();
            JSONObject jo = (JSONObject) iter2.next();
            JSONObject targetAttrs = (JSONObject)jo.get("targetAttrs");
            Object cameraIndexCode = targetAttrs.get("cameraIndexCode");
            Object latitude = jo.get("latitude");
            Object longitude = jo.get("longitude");
            BigDecimal floatLt = new BigDecimal(latitude.toString()).divide(new BigDecimal(360000),20, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal floatLg = new BigDecimal(longitude.toString()).divide(new BigDecimal(360000),20, BigDecimal.ROUND_HALF_EVEN);
            Object time = jo.get("time");
            gpsData.setBizType("01");
            gpsData.setBizCode(cameraIndexCode.toString());
            gpsData.setLat(floatLt.toString());
            gpsData.setLon(floatLg.toString());
            gpsData.setCreateTime(dealDateFormat(time.toString()));
            gpsData.setMapt("5");
            deviceGpsDataService.insert(gpsData);//保存日表
            System.out.println("gpsId = [" + gpsData.getGpsId() + "]");
            deviceGpsDataService.saveOrUpdateLastestGpsData(gpsData);//保存最新轨迹表，第一次新增，后面删除
            System.out.println("gpsId = [" + gpsData.getGpsId() + "]");
           /* System.out.println("latitude = [" + Integer.valueOf(latitude.toString())/360000d + "]"); //26.114358333333332
            System.out.println("longitude = [" + Integer.valueOf(longitude.toString())/360000d + "]");19.26455277777778*/
        }
    }

    private Date dealDateFormat(String oldDate) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            java.util.Date date = df.parse(oldDate);
            SimpleDateFormat sdf = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            return sdf.parse(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
