package cn.ffcs.zhsq.timeApplication.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.mybatis.persistence.timeApplication.TimeApplicationCheckMapper;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCallbackService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 时限审核
 * @author zhangls
 *
 */
@Service(value = "timeApplicationCheckService")
public class TimeApplicationCheckServiceImpl extends 
		ApplicationObjectSupport implements ITimeApplicationCheckService {
	@Autowired
	private TimeApplicationCheckMapper timeApplicationCheckMapper;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Override
	public Long saveTimeAppCheck(Map<String, Object> timeAppCheck) throws Exception {
		Long checkId = -1L;
		StringBuffer msgWrong = new StringBuffer("");
		String auditorType = ITimeApplicationCheckService.AUDITOR_TYPE.AUDITOR_USER.getValue();
		
		if(CommonFunctions.isNotBlank(timeAppCheck, "auditorType")) {
			auditorType = timeAppCheck.get("auditorType").toString();
		} else {
			timeAppCheck.put("auditorType", auditorType);
		}
		
		if(CommonFunctions.isBlank(timeAppCheck, "applicationId")) {
			msgWrong.append("缺少属性[applicationId]，请检查！");
		}
		if(CommonFunctions.isBlank(timeAppCheck, "userOrgCode")) {
			msgWrong.append("缺少属性[userOrgCode]，请检查！");
		}
		if(CommonFunctions.isBlank(timeAppCheck, "auditorId")) {
			//审核人员为用户时，才需要必填审核人员id
			if(ITimeApplicationCheckService.AUDITOR_TYPE.AUDITOR_USER.getValue().equals(auditorType)) {
				msgWrong.append("缺少属性[auditorId]，请检查！");
			}
		} else if(CommonFunctions.isBlank(timeAppCheck, "creatorId")) {
			timeAppCheck.put("creatorId", timeAppCheck.get("auditorId"));
		}
		
		if(CommonFunctions.isBlank(timeAppCheck, "updaterId") && CommonFunctions.isNotBlank(timeAppCheck, "creatorId")) {
			timeAppCheck.put("updaterId", timeAppCheck.get("creatorId"));
		}
		
		//审核人员为用户、组织时，才需要必填审核组织id
		if(ITimeApplicationCheckService.AUDITOR_TYPE.AUDITOR_USER.getValue().equals(auditorType)
		|| ITimeApplicationCheckService.AUDITOR_TYPE.AUDITOR_ORG.getValue().equals(auditorType)) {
			if(CommonFunctions.isBlank(timeAppCheck, "auditorOrgId")) {
				msgWrong.append("缺少属性[auditorOrgId]，请检查！");
			}
		}
		
		if(msgWrong.length() == 0) {
			if(timeApplicationCheckMapper.insert(timeAppCheck) > 0) {
				checkId = Long.valueOf(timeAppCheck.get("checkId").toString());
				
				checkCallback(timeAppCheck);
			}
		} else {
			throw new Exception(msgWrong.toString());
		}
		
		return checkId;
	}

	@Override
	public boolean updateTimeAppCheckById(Map<String, Object> timeAppCheck) throws Exception {
		boolean flag = false;
		StringBuffer msgWrong = new StringBuffer("");
		
		if(CommonFunctions.isBlank(timeAppCheck, "checkId")) {
			msgWrong.append("缺少属性[checkId]，请检查！");
		}
		if(CommonFunctions.isBlank(timeAppCheck, "userOrgCode")) {
			msgWrong.append("缺少属性[userOrgCode]，请检查！");
		}
		
		if(msgWrong.length() == 0) {
			flag = timeApplicationCheckMapper.update(timeAppCheck) > 0;
			
			if(flag) {
				checkCallback(timeAppCheck);
			}
		} else {
			throw new Exception(msgWrong.toString());
		}
		
		return flag;
	}

	@Override
	public boolean delTimeAppCheckById(Long checkId, Long delUserId) {
		boolean flag = false;
		
		if(checkId != null && checkId > 0) {
			flag = timeApplicationCheckMapper.delete(checkId, delUserId) > 0;
		}
		
		return flag;
	}

	@Override
	public Map<String, Object> findTimeAppCheckById(Long checkId) {
		Map<String, Object> timeAppCheck = null;
		
		if(checkId != null && checkId > 0) {
			timeAppCheck = timeApplicationCheckMapper.findById(checkId);
			
			if(timeAppCheck != null && !timeAppCheck.isEmpty()) {
				List<Map<String, Object>> timeAppCheckList = new ArrayList<Map<String, Object>>();
				
				timeAppCheckList.add(timeAppCheck);
				
				formatDataOut(timeAppCheckList, null);
			}
		}
		
		return timeAppCheck;
	}
	
	@Override
	public List<Map<String, Object>> findTimeAppCheckList(Map<String, Object> timeAppCheck) {
		List<Map<String, Object>> timeAppCheckList = null;
		
		timeAppCheckList = timeApplicationCheckMapper.findTimeAppCheckList(timeAppCheck);
		
		formatDataOut(timeAppCheckList, timeAppCheck);
		
		return timeAppCheckList;
	}
	
	/**
	 * 格式化输出数据
	 * @param timeAppCheckList
	 * @param params
	 * 			isTransferUser	是否转换审核人员姓名，true为转换，默认为false
	 * 			isTransferOrg	是否转换审核组织名称，true为转换，默认为false
	 */
	private void formatDataOut(List<Map<String, Object>> timeAppCheckList, Map<String, Object> params) {
		if(timeAppCheckList != null) {
			Long auditorId = -1L, auditorOrgId = -1L;
			boolean isTransferUser = false, isTransferOrg = false;
			UserBO userBO = null;
			OrgSocialInfoBO orgInfo = null;
			//时限申请审核状态
			Map<String, String> checkStatusMap = new HashMap<String, String>() {
				{
					put(ITimeApplicationCheckService.CHECK_STATUS.STATUS_INVALID.getValue(), ITimeApplicationCheckService.CHECK_STATUS.STATUS_INVALID.getName());
					put(ITimeApplicationCheckService.CHECK_STATUS.STATUS_FAIL.getValue(), ITimeApplicationCheckService.CHECK_STATUS.STATUS_FAIL.getName());
					put(ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.getValue(), ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.getName());
					put(ITimeApplicationCheckService.CHECK_STATUS.STATUS_INIT.getValue(), ITimeApplicationCheckService.CHECK_STATUS.STATUS_INIT.getName());
				}
			};
			
			if(CommonFunctions.isNotBlank(params, "isTransferUser")) {
				isTransferUser = Boolean.valueOf(params.get("isTransferUser").toString());
			}
			if(CommonFunctions.isNotBlank(params, "isTransferOrg")) {
				isTransferOrg = Boolean.valueOf(params.get("isTransferOrg").toString());
			}
			
			for(Map<String, Object> timeAppCheck : timeAppCheckList) {
				auditorId = -1L;
				auditorOrgId = -1L;
				
				if(CommonFunctions.isNotBlank(timeAppCheck, "auditorId")) {
					auditorId = Long.valueOf(timeAppCheck.get("auditorId").toString());
				}
				
				if(CommonFunctions.isNotBlank(timeAppCheck, "auditorOrgId")) {
					auditorOrgId = Long.valueOf(timeAppCheck.get("auditorOrgId").toString());
				}
				
				if(isTransferUser && auditorId > 0) {
					userBO = userManageService.getUserInfoByUserId(auditorId);
					if(userBO != null) {
						timeAppCheck.put("auditorName", userBO.getPartyName());
					}
				}
				
				if(isTransferOrg && auditorOrgId > 0) {
					orgInfo = orgSocialInfoService.findByOrgId(auditorOrgId);
					if(orgInfo != null) {
						timeAppCheck.put("auditorOrgName", orgInfo.getOrgName());
					}
				}
				
				if(CommonFunctions.isNotBlank(timeAppCheck, "timeAppCheckStatus")) {
					timeAppCheck.put("timeAppCheckStatusName", checkStatusMap.get(timeAppCheck.get("timeAppCheckStatus").toString()));
				}
			}
		}
	}
	
	/**
	 * 审核后，回调处理其他业务
	 * 不进行状态的限制，在各自回调内部自行处理
	 * @param timeAppCheck
	 * 			timeAppCheckStatus	审核状态
	 * 			checkId				审核记录id
	 * 			isCallback			是否回调，true为是，默认为true
	 */
	private void checkCallback(Map<String, Object> timeAppCheck) {
		Map<String, Object> timeApplicationCheck = null;
		
		Long checkId = -1L;
		String serviceName = null;
		boolean isCallback = true;
		
		if(CommonFunctions.isNotBlank(timeAppCheck, "isCallback")) {
			isCallback = Boolean.valueOf(timeAppCheck.get("isCallback").toString());
		}
		
		if(isCallback) {
			if(CommonFunctions.isNotBlank(timeAppCheck, "checkId")) {
				checkId = Long.valueOf(timeAppCheck.get("checkId").toString());
			}
			
			if(checkId > 0) {
				timeApplicationCheck = this.findTimeAppCheckById(checkId);
				if(CommonFunctions.isNotBlank(timeApplicationCheck, "serviceName")) {
					serviceName = timeApplicationCheck.get("serviceName").toString();
				}
				
				if(timeAppCheck != null && timeApplicationCheck != null) {
					timeAppCheck.putAll(timeApplicationCheck);
					timeApplicationCheck = timeAppCheck;
				}
			}
			
			if(StringUtils.isNotBlank(serviceName)) {
				ITimeApplicationCallbackService timeApplicationCallbackService = (ITimeApplicationCallbackService)this.getApplicationContext().getBean(serviceName);
				
				timeApplicationCallbackService.timeApplicationCallback(timeApplicationCheck);
			}
		}
	}

}
