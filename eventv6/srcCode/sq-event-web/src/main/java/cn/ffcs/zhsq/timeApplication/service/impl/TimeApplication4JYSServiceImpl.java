package cn.ffcs.zhsq.timeApplication.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.api.enums.BizPlatfromEnum;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 江阴市(JYS) 时限申请
 * @ClassName:   TimeApplication4JYSServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年5月6日 下午2:25:58
 */
@Service(value = "timeApplication4JYSService")
public class TimeApplication4JYSServiceImpl extends TimeApplication4BaseServiceImpl {
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Override
	public Map<String, Object> initTimeApplication4Event(Long eventId, String applicationType, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		Long orgId = userInfo.getOrgId(), businessId = eventId, businessKeyId = eventId;
		Map<String, Object> params = new HashMap<String, Object>();
		EventDisposal event = null;
		
		params.putAll(super.initTimeApplication4Event(eventId, applicationType, userInfo, extraParam));
		
		if(StringUtils.isBlank(applicationType) && CommonFunctions.isNotBlank(extraParam, "applicationType")) {
			applicationType = extraParam.get("applicationType").toString();
		}
		
		if(eventId == null || eventId < 0) {
			throw new IllegalArgumentException("缺少有效的事件id！");
		} else if(ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue().equals(applicationType)) {
			event = eventDisposalService.findEventByIdSimple(eventId);
			if(event != null) {
				String eventStatus = event.getStatus(),
					   handleDateFlag = event.getHandleDateFlag();
				
				if(ConstantValue.EVENT_STATUS_END.equals(eventStatus)) {
					throw new ZhsqEventException("已办结事件不能申请延期！");
				} else if(ConstantValue.EVENT_STATUS_DRAFT.equals(eventStatus)) {
					throw new ZhsqEventException("草稿事件不能申请延期！");
				} else if(ConstantValue.LIMIT_TIME_STATUS_EXPIRED.equals(handleDateFlag)) {
					throw new ZhsqEventException("已过期事件不能申请延期！");
				} else if(ConstantValue.LIMIT_TIME_STATUS_NORMAL.equals(handleDateFlag)) {
					throw new ZhsqEventException("办理时长大于24小时，不能申请延期！");
				}
			} else {
				throw new IllegalArgumentException("事件id【" + eventId + "】没有对应有效的事件信息！");
			}
		}
		
		if(ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue().equals(applicationType)) {
			Map<String, Object> timeApplication = new HashMap<String, Object>();
			List<Map<String, Object>> timeAppList = null;
			
			timeApplication.put("applicationType", ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue());
			timeApplication.put("businessKeyId", eventId);
			timeApplication.put("isTransferUser", true);
			timeApplication.put("isTransferOrg", true);
			timeApplication.put("timeAppCheckStatus", ITimeApplicationCheckService.CHECK_STATUS.STATUS_INIT.getValue());
			
			timeAppList = this.findTimeApplicationList(timeApplication);
			
			if(timeAppList != null && timeAppList.size() > 0) {
				Map<String, Object> timeApp = timeAppList.get(0);
				StringBuffer msgWrong = new StringBuffer("当前事件存在待审核的记录！");
				
				if(CommonFunctions.isNotBlank(timeApp, "auditorName") || CommonFunctions.isNotBlank(timeApp, "auditorOrgName")) {
					msgWrong.append("审核人员：");
					
					if(CommonFunctions.isNotBlank(timeApp, "auditorName")) {
						msgWrong.append(timeApp.get("auditorName"));
					}
					
					if(CommonFunctions.isNotBlank(timeApp, "auditorOrgName")) {
						msgWrong.append("(").append(timeApp.get("auditorOrgName")).append(")");
					}
					
					msgWrong.append("！");
				}
				
				throw new ZhsqEventException(msgWrong.toString());
			}
			
			String bizPlatform = event.getBizPlatform();
			//事件时限审核回调服务名称
			String eventCallbackServiceName = "timeApplicationCallback4EventService",
				   userOrgCode = userInfo.getOrgCode();
			
			params = new HashMap<String, Object>();

			params.put("isAutoAudit", true);
			params.put("businessId", businessId);
			params.put("businessKeyId", businessKeyId);
			params.put("applicationType", applicationType);
			params.put("serviceName", eventCallbackServiceName);
			params.put("defaultIntervalUnit", ITimeApplicationService.INTERVAL_UNIT.WEEK_DAY.toString());
			params.put("maxInterval", 5);
			params.put("isShowIntervalUnit", false);
			params.put("userOrgCode", userOrgCode);
			
			if(BizPlatfromEnum.JY_CG.getValue().equals(bizPlatform)) {//数字城管对接
				ProInstance proInstance = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
				
				if(proInstance != null) {
					params.put("auditorType", ITimeApplicationCheckService.AUDITOR_TYPE.AUDITOR_USER.getValue());
					params.put("auditorOrgId", proInstance.getOrgId());
					params.put("auditorId", proInstance.getUserId());
				} else {
					throw new ZhsqEventException("该事件未找到有效的流程实例信息！");
				}
			} else {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
				
				if(orgInfo == null) {
					throw new IllegalArgumentException("组织id【" + orgId + "】没有找到有效的组织信息！");
				} else {
					int orgLevel = Integer.valueOf(orgInfo.getChiefLevel()), districtOrgLevel = Integer.valueOf(ConstantValue.DISTRICT_ORG_LEVEL);
					Long auditorOrgId = null;
					
					if(orgLevel >= districtOrgLevel) {//下派
						List<OrgSocialInfoBO> orgInfoList = fiveKeyElementService.findOrgByLevel(orgId, orgLevel - districtOrgLevel, ConstantValue.GOV_PROFESSION_CODE, userInfo);
						if(orgInfoList != null && orgInfoList.size() > 0) {
							auditorOrgId = orgInfoList.get(0).getOrgId();
						}
					}
					
					if(auditorOrgId == null || auditorOrgId < 0) {
						throw new IllegalArgumentException("【" + userInfo.getOrgName() + "】找不到有效的审核组织信息！");
					} else {
						params.put("auditorType", ITimeApplicationCheckService.AUDITOR_TYPE.AUDITOR_ORG.getValue());
						params.put("auditorOrgId", auditorOrgId);
					}
				}
			}
		}
		
		return params;
	}
	
	protected void formatParamIn4EventTimeApp(Map<String, Object> timeApplication) {
		int listType = 0;
		
		if(CommonFunctions.isNotBlank(timeApplication, "listType")) {
			listType = Integer.valueOf(timeApplication.get("listType").toString());
		}
		
		switch(listType) {
			case 1: {//我的审核列表
				
			}
			case 2: {//我的时限申请
				List<String> applicationTypeList = new ArrayList<String>();
				
				applicationTypeList.add(ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.toString());
				
				timeApplication.put("applicationTypeList", applicationTypeList);
				break;
			}
		}
		
		super.formatParamIn4EventTimeApp(timeApplication);
	}
	
	/**
	 * 只验证是否存在待核查的申请记录，审核不通过之后，仍然可以再次提出申请
	 */
	protected boolean isDuplicated(Map<String, Object> timeApplication) throws Exception {
		List<String> timeAppCheckStatusList = new ArrayList<String>();
		
		timeAppCheckStatusList.add(ITimeApplicationCheckService.CHECK_STATUS.STATUS_INIT.getValue());
		
		timeApplication.put("timeAppCheckStatusList", timeAppCheckStatusList);
		
		return super.isDuplicated(timeApplication);
	}
}
