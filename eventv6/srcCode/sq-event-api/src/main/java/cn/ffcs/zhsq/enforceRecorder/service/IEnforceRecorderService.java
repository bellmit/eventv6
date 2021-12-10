package cn.ffcs.zhsq.enforceRecorder.service;

import java.util.Map;

/**
 * @Description
 * @Author huangjianming
 * @Date 2021/8/20 17:06
 */
public interface IEnforceRecorderService {

    /**
     * 获取监控点位状态信息
     * @return
     */
    String fetchEnforceRecorderStatus(String codes,String orgCode);

    /**
     * 提供给海康平台事件回调
     * @param jsonData
     * @return
     */
    boolean doEventCallBack(Map jsonData) throws Exception ;


    /**
     * 同步执法仪列表信息
     * @param params
     * @param orgCode
     */
    Map<String,Object> syncEnforceRecorders(Map<String, Object> params, String orgCode);

    /**
     * 注册事件到海康平台
     * @param params
     * @param orgCode
     * @return
     */
    String rigistryEvent2HKPlatform(Map<String, Object> params, String orgCode) throws Exception;

    /**
     * 查看在海康平台订阅的事件
     * @param orgCode
     * @return
     */
    String eventSubscriptionView(String orgCode) throws Exception;

    /**
     * 取消海康平台注册的事件
     * @param eventTypes
     * @param orgCode
     * @return
     */
    String cancelEvent(String eventTypes, String orgCode) throws Exception;
}
