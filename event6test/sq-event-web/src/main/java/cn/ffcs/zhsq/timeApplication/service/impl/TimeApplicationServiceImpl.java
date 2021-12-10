package cn.ffcs.zhsq.timeApplication.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 时限申请
 * @author zhangls
 *
 */
@Service(value = "timeApplicationService")
public class TimeApplicationServiceImpl extends
					ApplicationObjectSupport implements ITimeApplicationService {
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Override
	public Map<String, Object> initTimeApplication4Event(Long eventId, String applicationType, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		ITimeApplicationService timeApplicationService = this.capServiceImpl(null, userInfo.getOrgCode());
		
		return timeApplicationService.initTimeApplication4Event(eventId, applicationType, userInfo, extraParam);
	}
	
	@Override
	public Long saveTimeApplication(Map<String, Object> timeApplication) throws Exception {
		ITimeApplicationService timeApplicationService = capServiceImpl(timeApplication, null);
		
		return timeApplicationService.saveTimeApplication(timeApplication);
	}

	@Override
	public boolean updateTimeApplicationById(Map<String, Object> timeApplication) throws Exception {
		ITimeApplicationService timeApplicationService = capServiceImpl();
		
		return timeApplicationService.updateTimeApplicationById(timeApplication);
	}

	@Override
	public boolean delTimeApplicationById(Long applicationId, Long delUserId) {
		ITimeApplicationService timeApplicationService = capServiceImpl();
		
		return timeApplicationService.delTimeApplicationById(applicationId, delUserId);
	}

	@Override
	public Map<String, Object> findTimeAppliationById(Long applicationId, Map<String, Object> params) {
		ITimeApplicationService timeApplicationService = capServiceImpl();
		
		return timeApplicationService.findTimeAppliationById(applicationId, params);
	}

	@Override
	public int findTimeApplicationCount(Map<String, Object> timeApplication) {
		ITimeApplicationService timeApplicationService = capServiceImpl();
		
		return timeApplicationService.findTimeApplicationCount(timeApplication);
	}
	
	@Override
	public List<Map<String, Object>> findTimeApplicationList(
			Map<String, Object> timeApplication) {
		ITimeApplicationService timeApplicationService = capServiceImpl();
		
		return timeApplicationService.findTimeApplicationList(timeApplication);
	}

	@Override
	public EUDGPagination findTimeApplicationPagination(int pageNo,
			int pageSize, Map<String, Object> timeApplication) {
		ITimeApplicationService timeApplicationService = capServiceImpl();
		
		return timeApplicationService.findTimeApplicationPagination(pageNo, pageSize, timeApplication);
	}
	
	@Override
	public int findEventTimeAppCount(Map<String, Object> timeApplication) throws Exception {
		String userOrgCode = null;
		ITimeApplicationService timeApplicationService = null;
		
		if(CommonFunctions.isNotBlank(timeApplication, "userOrgCode")) {
			userOrgCode = timeApplication.get("userOrgCode").toString();
		}
		
		timeApplicationService = capServiceImpl(timeApplication, userOrgCode);
		
		return timeApplicationService.findEventTimeAppCount(timeApplication);
	}
	
	@Override
	public EUDGPagination findEventTimeAppPagination(int pageNo, int pageSize,
			Map<String, Object> timeApplication) {
		String userOrgCode = null;
		ITimeApplicationService timeApplicationService = null;
		
		if(CommonFunctions.isNotBlank(timeApplication, "userOrgCode")) {
			userOrgCode = timeApplication.get("userOrgCode").toString();
		}
		
		timeApplicationService = capServiceImpl(timeApplication, userOrgCode);
		
		return timeApplicationService.findEventTimeAppPagination(pageNo, pageSize, timeApplication);
	}

	/**
	 * 获取默认的接口实现
	 * @return
	 */
	private ITimeApplicationService capServiceImpl() {
		String serviceImpl = "timeApplication4BaseService";
		
		return (ITimeApplicationService) this.getApplicationContext().getBean(serviceImpl);
	}
	
	/**
	 * 获取接口实现
	 * @param params
	 * 			timeApplicationService	接口实现名称
	 * 			userOrgCode				组织编码
	 * @param orgCode	组织编码
	 * @return
	 */
	private ITimeApplicationService capServiceImpl(Map<String, Object> params, String orgCode) {
		String serviceImpl = "";

		if(CommonFunctions.isNotBlank(params, "timeApplicationService")) {
			serviceImpl = params.get("timeApplicationService").toString();
		} else {
			if(StringUtils.isBlank(orgCode) && CommonFunctions.isNotBlank(params, "userOrgCode")) {
				orgCode = params.get("userOrgCode").toString();
			}
			
			serviceImpl = funConfigurationService.changeCodeToValue(ConstantValue.TIME_APPLICATION_ATTRIBUTE, ConstantValue.TIME_APPLICATION_SERVICE, IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, orgCode);
		}
		
		if(StringUtils.isBlank(serviceImpl)) {
			serviceImpl = "timeApplication4BaseService";
		}
		
		return (ITimeApplicationService) this.getApplicationContext().getBean(serviceImpl);
	}

}
