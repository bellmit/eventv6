package cn.ffcs.zhsq.intermediateData.eventWechat.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.intermediateData.eventVerify.service.impl.EventVerify4WechatServiceImpl;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 微信事件信息接收表模块服务实现
 * @Author: zhangls
 * @Date: 08-21 09:05:05
 * @Copyright: 2017 福富软件
 */
@Service("eventWechatService")
@Transactional
public class EventWechatServiceImpl extends EventVerify4WechatServiceImpl {
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	//属性默认设置值
	@SuppressWarnings("serial")
	private final Map<String, String> DEFAULT_MAP = new HashMap<String, String>() {
		{
			put("bizPlatform", "201");//微信对接
			put("attachmentType", ConstantValue.EVENT_WECHAT_ATTACHMENT_TYPE);//地图定位类型
			put("markerType", ConstantValue.EVENT_WECHAT_MODULE_CODE);//附件类型
			put("eventReportBizType", ConstantValue.EVENT_WECHAT_MODULE_CODE);//与事件关联业务类别
			put("moduleCode", ConstantValue.EVENT_WECHAT_MODULE_CODE);//第三方事件对接模块
		}
	};
	
	@Override
	public Long saveEventVerify(Map<String, Object> eventVerify) throws Exception {
		Long eventWechatId = -1L;
		
		if(eventVerify != null && !eventVerify.isEmpty()) {
			defaultParam(eventVerify, "attachmentType");
			defaultParam(eventVerify, "markerType");
			defaultParam(eventVerify, "bizPlatform");
			
			eventWechatId = super.saveEventVerify(eventVerify);
		}
		
		return eventWechatId;
	}

	@Override
	public boolean updateEventVerifyById(Map<String, Object> eventVerify, UserInfo userInfo) throws Exception {
		boolean flag = false;
		
		if(eventVerify != null && !eventVerify.isEmpty()) {
			this.defaultParam(eventVerify, "attachmentType");
			this.defaultParam(eventVerify, "markerType");
			
			flag = super.updateEventVerifyById(eventVerify, userInfo);
		}
		
		return flag;
	}
	
	@Override
	public Map<String, Object> findEventVerifyById(Long eventVerifyId, Map<String, Object> eventVerify) {
		Map<String, Object> resultMap = null;
		
		if(eventVerify == null) {
			eventVerify = new HashMap<String, Object>();
		}
		
		this.defaultParam(eventVerify, "eventReportBizType");
		
		resultMap = super.findEventVerifyById(eventVerifyId, eventVerify);
		
		return resultMap;
	}
	
	@Override
	public EUDGPagination findEventVerifyPagination(int pageNo, int pageSize,
			Map<String, Object> params) {
		if(params == null) {
			params = new HashMap<String, Object>();
		}
		
		defaultParam(params, "bizPlatform");
		
		return super.findEventVerifyPagination(pageNo, pageSize, params);
	}
	
	@Override
	public Map<String, Object> fetchParam4Event(Map<String, Object> eventVerify) {
		Map<String, Object> resultMap = null,
							defaultParamMap = new HashMap<String, Object>();
		
		if(CommonFunctions.isNotBlank(eventVerify, "userOrgCode")) {
			defaultParamMap.put("userOrgCode", eventVerify.get("userOrgCode"));
		}
		
		this.defaultParam(defaultParamMap, "attachmentType");
		
		eventVerify.putAll(defaultParamMap);
		
		resultMap = super.fetchParam4Event(eventVerify);
		
		this.defaultParam(defaultParamMap, "markerType");
		this.defaultParam(defaultParamMap, "moduleCode");
		
		resultMap.put("isReport", true);
		resultMap.put("isShowCloseBtn", false);
		resultMap.put("trigger", "EVENT_WECHAT");//事件类别过滤触发条件，功能配置编码为types
		resultMap.putAll(defaultParamMap);
		
		return resultMap;
	}
	
	/**
	 * 为指定参数设置默认值
	 * @param params
	 * @param mapKey
	 */
	private void defaultParam(Map<String, Object> params, String mapKey) {
		if(StringUtils.isNotBlank(mapKey)) {
			String EVENT_VERIFY_FUNC_CODE = "EVENT_VERIFY_ATTRIBUTE",
				   userOrgCode = "",
				   mapkeyValue = null;
			
			if(params == null) {
				params = new HashMap<String, Object>();
			}
			
			if(CommonFunctions.isNotBlank(params, mapKey)) {
				mapkeyValue = params.get(mapKey).toString();
			}
			
			if(StringUtils.isBlank(mapkeyValue)) {
				if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
					userOrgCode = params.get("userOrgCode").toString();
				}
				
				mapkeyValue = funConfigurationService.turnCodeToValue(EVENT_VERIFY_FUNC_CODE, mapKey, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
			}
			
			if(StringUtils.isBlank(mapkeyValue)) {
				mapkeyValue = DEFAULT_MAP.get(mapKey);
			}
			
			params.put(mapKey, mapkeyValue);
		}
	}
}