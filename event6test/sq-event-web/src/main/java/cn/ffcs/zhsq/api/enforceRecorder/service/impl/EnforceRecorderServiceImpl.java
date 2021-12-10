package cn.ffcs.zhsq.api.enforceRecorder.service.impl;

import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.enforceRecorder.service.IEnforceRecorderService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @Description 执法仪设备对接代理类
 * @Author huangjianming
 * @Date 2021/8/20 17:11
 */
@Service("enforceRecorderServiceImpl")
public class EnforceRecorderServiceImpl extends ApplicationObjectSupport implements IEnforceRecorderService {

    @Autowired
    private IFunConfigurationService funConfigurationService;

    @Override
    public String fetchEnforceRecorderStatus(String codes, String orgCode) {
        return this.capServiceImpl(null,orgCode).fetchEnforceRecorderStatus(codes,orgCode);
    }

    @Override
    public boolean doEventCallBack(Map jsonData) throws Exception {
        return this.capServiceImpl(null,null).doEventCallBack(jsonData);
    }

    @Override
    public Map<String,Object> syncEnforceRecorders(Map<String, Object> params, String orgCode) {
         return this.capServiceImpl(params,orgCode).syncEnforceRecorders(params,orgCode);
    }

    @Override
    public String rigistryEvent2HKPlatform(Map<String, Object> params, String orgCode) throws Exception {
        return this.capServiceImpl(params,orgCode).rigistryEvent2HKPlatform(params,orgCode);
    }

    @Override
    public String eventSubscriptionView(String orgCode) throws Exception{
        return this.capServiceImpl(null,orgCode).eventSubscriptionView(orgCode);
    }

    @Override
    public String cancelEvent(String eventTypes, String orgCode) throws Exception {
        return this.capServiceImpl(null,orgCode).cancelEvent(eventTypes,orgCode);
    }

    private IEnforceRecorderService capServiceImpl(Map<String, Object> params, String orgCode) {
        String serviceImpl = "";
        if(CommonFunctions.isNotBlank(params, "enforceRecorderServiceImpl")) {
            serviceImpl = params.get("enforceRecorderServiceImpl").toString();
        } else {
            serviceImpl = funConfigurationService.changeCodeToValue("HK_ENFORCE_RECORDER_CFG", "enforceRecorderServiceImpl", IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, orgCode, IFunConfigurationService.DIRECTION_UP_REGEXP);
        }

        if(StringUtils.isBlank(serviceImpl)) {
            serviceImpl = "enforceRecorder4ZNServiceImpl";
        }

        return (IEnforceRecorderService) this.getApplicationContext().getBean(serviceImpl);
    }
}
