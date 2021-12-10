package cn.ffcs.zhsq.api.enforceRecorder.service.impl;

import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResInfo;
import cn.ffcs.shequ.utils.CollectionUtils;
import cn.ffcs.shequ.zzgl.service.res.IResInfoService;
import cn.ffcs.zhsq.api.enforceRecorder.service.AbstractEnforceRecorderService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 延平执法仪设备对接实现
 * @Author huangjianming
 * @Date 2021/8/20 17:11
 */
@Service("enforceRecorder4YPServiceImpl")
public class EnforceRecorder4YPServiceImpl extends AbstractEnforceRecorderService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //同步最大请求失败次数
    private int maxFailRequestNum = 20;

    private String treatyType = "ehome_reg";

    private static String YP_CAMERAS_DEVICELIST_URL = "/api/resource/v1/cameras";

    @Autowired
    private IResInfoService resInfoService;

    @Override
    protected boolean isSupport() {
        return false;
    }

    @Override
    protected void setDefaultValue4Cfg(Map<String, Object> cfgMap) {
        if (CommonFunctions.isBlank(cfgMap,"host")) {
            cfgMap.put("host", ConstantValue.YP_HK_PLATFORM_IP);
        }
        if (CommonFunctions.isBlank(cfgMap,"port")) {
            cfgMap.put("port",ConstantValue.YP_HK_PLATFORM_PORT);
        }
        if (CommonFunctions.isBlank(cfgMap,"appKey")) {
            cfgMap.put("appKey",ConstantValue.YP_HK_PLATFORM_PORT);
        }
        if (CommonFunctions.isBlank(cfgMap,"secretKey")) {
            cfgMap.put("secretKey",ConstantValue.YP_HK_PLATFORM_SECRET_KEY);
        }
        if (CommonFunctions.isBlank(cfgMap,"camerasDeviceListUrl")) {
            cfgMap.put("camerasDeviceListUrl", YP_CAMERAS_DEVICELIST_URL);
        }
    }

    @Override
    public Map<String, Object> syncEnforceRecorders(Map<String, Object> params, String orgCode) {
        Map<String,Object> resultMap = new HashMap();
        resultMap.put("msg","同步失败");
        int total = 0;
        int failRequestNum = 0;
        int insert_num = 0;
        int update_num = 0;
        int sum = 0;
        int pageNo = 1;
        int pageSize = 500;
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
        while(true){
            boolean isSuccess = false;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("pageNo",pageNo);
            jsonBody.put("pageSize",pageSize);
            jsonBody.put("treeCode","0");
            params.put("jsonBody",jsonBody);
            Map<String, Object> jsonMap = super.syncEnforceRecorders(params, orgCode);
            if(CollectionUtils.isEmpty(jsonMap)){
                logger.error("调用接口失败,失败原因[海康接口请求超时]");
                resultMap.put("msg","调用接口失败,失败原因[海康接口请求超时]");
            }else{
                if ("0".equals(jsonMap.get("code"))) {
                    JSONObject dataObj = (JSONObject) jsonMap.get("data");
                    JSONArray list = dataObj.getJSONArray("list");
                    if (list!=null && list.size()>0) {
                        for (Object obj : list) {
                            JSONObject treeObj = (JSONObject) obj;
                            String name = treeObj.getString("name");//监控点名称
                            String cameraIndexCode = treeObj.getString("cameraIndexCode");//所属设备编号（通用唯一识别码UUID）
                            if(treatyType.equals(treeObj.getString("treatyType"))){
                                ++sum;
                                ResInfo oldResInfo = resInfoService.findResInfoByResno(cameraIndexCode,"enforce_recorder");
                                if (oldResInfo == null) {
                                    ResInfo resInfo = new ResInfo();
                                    resInfo.setResName(name);
                                    resInfo.setResno(cameraIndexCode);
                                    resInfo.setResTypeId(resTypeId);
                                    resInfo.setGridId(gridId);
                                    resInfoService.saveResInfoReturnId(resInfo);
                                    ++ insert_num;
                                }else{
                                    oldResInfo.setResName(name);
                                    resInfoService.updateResInfo(oldResInfo);
                                    ++ update_num;
                                }
                            }
                        }
                    }
                    if (total <= 0) {
                        total = Integer.valueOf(dataObj.get("total").toString());
                        logger.info("开始获取摄像头数据，共：" + total + "条（接口返回）");
                    }
                    logger.info("获取第" + pageNo + "页数据" + list.size() + "条,并更新数据库");
                    if (total > pageNo * pageSize) {
                        pageNo = pageNo + 1;
                    } else {
                        logger.info("请求摄像头列表接口结束");
                        break;
                    }
                    isSuccess = true;
                } else {
                    logger.error("调用接口失败,失败原因[海康接口报错，错误码：" + jsonMap.get("code") + "(" + jsonMap.get("msg") + ")]");
                    resultMap.put("msg","调用接口失败,失败原因[海康接口报错，错误码：" + jsonMap.get("code") + "(" + jsonMap.get("msg") + ")]");
                }
            }
            if(!isSuccess){
                if (failRequestNum < maxFailRequestNum){
                    failRequestNum ++;
                } else {
                    break;
                }
            }
        }
        logger.info("===sum:"+sum+"===insert_num:"+insert_num+"===update_num:"+update_num);
        resultMap.put("msg", sum == (insert_num+update_num)?"同步成功":"同步失败");
        return resultMap;
    }
}
