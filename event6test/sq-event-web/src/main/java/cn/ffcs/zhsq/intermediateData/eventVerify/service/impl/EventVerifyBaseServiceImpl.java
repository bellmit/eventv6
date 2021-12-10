package cn.ffcs.zhsq.intermediateData.eventVerify.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.crypto.HashIdManager;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.intermediateData.eventVerify.service.IEventVerifyBaseService;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 事件审核实现跳转中心
 * @author zhangls
 *
 */
@Service("eventVerifyBaseService")
public class EventVerifyBaseServiceImpl extends ApplicationObjectSupport implements IEventVerifyBaseService {
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Override
	public Long saveEventVerify(Map<String, Object> eventVerify)
			throws Exception {
		IEventVerifyBaseService eventVerifyService = this.capServiceImpl(eventVerify);
		
		return eventVerifyService.saveEventVerify(eventVerify);
	}

	@Override
	public boolean updateEventVerifyById(Map<String, Object> eventVerify,
			UserInfo userInfo) throws Exception {
		IEventVerifyBaseService eventVerifyService = null;
		
		if(CommonFunctions.isBlank(eventVerify, "userOrgCode") && userInfo != null) {
			eventVerify.put("userOrgCode", userInfo.getOrgCode());
		}
		
		eventVerifyService = this.capServiceImpl(eventVerify);
		
		return eventVerifyService.updateEventVerifyById(eventVerify, userInfo);
	}

	@Override
	public boolean deleteEventVerifyById(Long eventVerifyId, Long delUserId,
			Map<String, Object> eventVerify) {
		IEventVerifyBaseService eventVerifyService = this.capServiceImpl(eventVerify);
		
		return eventVerifyService.deleteEventVerifyById(eventVerifyId, delUserId, eventVerify);
	}

	@Override
	public Map<String, Object> findEventVerifyById(Long eventVerifyId,
			Map<String, Object> eventVerify) {
		IEventVerifyBaseService eventVerifyService = this.capServiceImpl(eventVerify);
		
		return eventVerifyService.findEventVerifyById(eventVerifyId, eventVerify);
	}
	
	@Override
	public List<Map<String, Object>> findEventVerifyByParam(Map<String, Object> params) {
		IEventVerifyBaseService eventVerifyService = this.capServiceImpl(params);
		
		return eventVerifyService.findEventVerifyByParam(params);
	}

	@Override
	public int findEventVerifyCount(Map<String, Object> params) {
		IEventVerifyBaseService eventVerifyService = this.capServiceImpl(params);
		
		return eventVerifyService.findEventVerifyCount(params);
	}
	
	@Override
	public EUDGPagination findEventVerifyPagination(int pageNo, int pageSize,
			Map<String, Object> params) {
		IEventVerifyBaseService eventVerifyService = this.capServiceImpl(params);
		
		return eventVerifyService.findEventVerifyPagination(pageNo, pageSize, params);
	}
	
	@Override
	public Map<String, Object> fetchParam4Event(Map<String, Object> eventVerify) {
		IEventVerifyBaseService eventVerifyService = this.capServiceImpl(eventVerify);
		
		return eventVerifyService.fetchParam4Event(eventVerify);
	}
	
	/**
	 * 获取事件审核实现类
	 * @param eventVerify
	 * 			eventVerifyServiceName	事件审核实现类名称
	 * @return
	 */
	private IEventVerifyBaseService capServiceImpl(Map<String, Object> eventVerify) {
		String eventVerifyServiceName = "";
		
		if(CommonFunctions.isNotBlank(eventVerify, "eventVerifyHashId")
				&& CommonFunctions.isBlank(eventVerify, "eventVerifyId")) {
			eventVerify.put("eventVerifyId", HashIdManager.encrypt(eventVerify.get("eventVerifyId")));
		}
		
		if(CommonFunctions.isNotBlank(eventVerify, "eventVerifyServiceName")) {
			eventVerifyServiceName = eventVerify.get("eventVerifyServiceName").toString();
		} else {
			String EVENT_VERIFY_FUNC_CODE = "EVENT_VERIFY_ATTRIBUTE",
				   EVENT_VERIFY_SERVICE_NAME = "eventVerifyServiceName",
				   userOrgCode = "",
				   triggerCondition = EVENT_VERIFY_SERVICE_NAME;
			
			if(CommonFunctions.isNotBlank(eventVerify, "userOrgCode")) {
				userOrgCode = eventVerify.get("userOrgCode").toString();
			}
			if(CommonFunctions.isNotBlank(eventVerify, "verifyType")) {
				triggerCondition += eventVerify.get("verifyType").toString();
			}
			
			eventVerifyServiceName = funConfigurationService.turnCodeToValue(EVENT_VERIFY_FUNC_CODE, triggerCondition, IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
		}
		
		if(StringUtils.isBlank(eventVerifyServiceName)) {
			eventVerifyServiceName = "eventVerify4WechatService";
		}
		
		return (IEventVerifyBaseService) this.getApplicationContext().getBean(eventVerifyServiceName);
	}

}
