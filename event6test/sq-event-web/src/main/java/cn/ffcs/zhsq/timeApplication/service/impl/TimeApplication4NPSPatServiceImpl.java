package cn.ffcs.zhsq.timeApplication.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.intermediateData.eventVerify.service.IEventVerifyBaseService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 时限申请 南平市(NPS)随手拍(Pat)
 * @author zhangls
 *
 */
@Service(value = "timeApplication4NPSPatService")
public class TimeApplication4NPSPatServiceImpl extends
		TimeApplication4BaseServiceImpl {
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
    private IEventVerifyBaseService eventVerifyBaseService;
	
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Override
	public Long saveTimeApplication(Map<String, Object> timeApplication) throws Exception {
		Long applicationId = -1L;
		StringBuffer msgWrong = new StringBuffer("");
		String applicationType = null;
		
		if(CommonFunctions.isBlank(timeApplication, "applicationType")) {
			throw new IllegalArgumentException("缺少属性[applicationType]，请检查！");
		} else {
			applicationType = timeApplication.get("applicationType").toString();
		}
		
		if(ITimeApplicationService.APPLICATION_TYPE.EVENT_TYPE.getValue().equals(applicationType)
		|| ITimeApplicationService.APPLICATION_TYPE.EVENT_VERIFY.getValue().equals(applicationType)) {
			Long eventId = null;
			
			if(CommonFunctions.isNotBlank(timeApplication, "businessKeyId")) {
				try {
					eventId = Long.valueOf(timeApplication.get("businessKeyId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if(eventId == null || eventId < 0) {
					throw new IllegalArgumentException("属性[businessKeyId]：【"+ timeApplication.get("businessKeyId") +"】不是有效的数值，请检查！");
				}
			} else {
				throw new IllegalArgumentException("缺少属性[businessKeyId]，请检查！");
			}
			
			if(eventId != null && eventId > 0) {
				EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
				String applicationTypeName = null;
				
				for(ITimeApplicationService.APPLICATION_TYPE applicationTypeEnum : ITimeApplicationService.APPLICATION_TYPE.values()) {
					if(applicationTypeEnum.getValue().equals(applicationType)) {
						applicationTypeName = applicationTypeEnum.getName();
						break;
					}
				}
				
				if(event != null) {
					String eventStatus = event.getStatus();
					
					if(ConstantValue.EVENT_STATUS_END.equals(eventStatus)) {
						throw new ZhsqEventException("该事件已归档，无法提交【"+ applicationTypeName +"】！");
					}
				} else {
					throw new ZhsqEventException("属性[businessKeyId]：【"+ eventId +"】没有对应有效的事件信息，请检查！");
				}
				
				if(ITimeApplicationService.APPLICATION_TYPE.EVENT_VERIFY.getValue().equals(applicationType)) {
					Map<String, Object> verifyParam = new HashMap<String, Object>();
					int eventVerifyCount = 0;
					String REPORT_STATUS = "02";//已上报
					
					verifyParam.put("eventId", eventId);
					verifyParam.put("status", REPORT_STATUS);
					eventVerifyCount = eventVerifyBaseService.findEventVerifyCount(verifyParam);
					
					if(eventVerifyCount == 0) {
						throw new ZhsqEventException("该事件没有【已上报】状态的事件审核记录！");
					} else if(eventVerifyCount > 1) {
						throw new ZhsqEventException("该事件共有【已上报】状态的事件审核记录【"+ eventVerifyCount +"】条，无法进行【"+ applicationTypeName +"】！");
					}
				}
			}
		}
		
		if(msgWrong.length() > 0) {
			throw new Exception(msgWrong.toString());
		} else {
			applicationId = super.saveTimeApplication(timeApplication);
		}
		
		return applicationId;
	}
	
	protected void formatParamIn4EventTimeApp(Map<String, Object> timeApplication) {
		int listType = 1;
		
		if(CommonFunctions.isNotBlank(timeApplication, "listType")) {
			listType = Integer.valueOf(timeApplication.get("listType").toString());
		}
		
		switch(listType) {
			case 2: {//我的时限申请
				if(CommonFunctions.isBlank(timeApplication, "applicationTypeList")) {
					List<String> applicationTypeList = new ArrayList<String>();
					
					applicationTypeList.add(ITimeApplicationService.APPLICATION_TYPE.EVENT_DELAY.toString());
					applicationTypeList.add(ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.toString());
					applicationTypeList.add(ITimeApplicationService.APPLICATION_TYPE.EVENT_TYPE.toString());
					applicationTypeList.add(ITimeApplicationService.APPLICATION_TYPE.EVENT_VERIFY.toString());
					
					timeApplication.put("applicationTypeList", applicationTypeList);
				}
				
				break;
			}
		}
		
		super.formatParamIn4EventTimeApp(timeApplication);
	}
	
	@Override
	public Map<String, Object> initTimeApplication4Event(Long eventId, String applicationType, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		Long orgId = userInfo.getOrgId(), businessId = null, businessKeyId = eventId;
		int fromOrgLevel = 0;
		String orgType = null;
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.putAll(super.initTimeApplication4Event(eventId, applicationType, userInfo, extraParam));
		
		if(StringUtils.isBlank(applicationType) && CommonFunctions.isNotBlank(extraParam, "applicationType")) {
			applicationType = extraParam.get("applicationType").toString();
		}
		
		if(eventId == null || eventId < 0) {
			throw new IllegalArgumentException("缺少有效的事件id！");
		} else {
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			if(event != null) {
				String eventStatus = event.getStatus();
				
				if(ConstantValue.EVENT_STATUS_END.equals(eventStatus)) {
					throw new ZhsqEventException("该事件已归档，无法提交申请！");
				} else if(ConstantValue.EVENT_STATUS_DRAFT.equals(eventStatus)) {
					throw new ZhsqEventException("该事件为草稿，无需提交申请！");
				}
			} else {
				throw new IllegalArgumentException("事件id【" + eventId + "】没有对应有效的事件信息！");
			}
		}
		
		if(ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue().equals(applicationType)) {
			Long curTaskId = null;
			
			if(eventId != null && eventId > 0) {
				Long instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(eventId);
				
				if(instanceId != null && instanceId > 0) {
					curTaskId = eventDisposalWorkflowService.curNodeTaskId(instanceId);
				}
				
			}
			
			if(curTaskId == null || curTaskId < 0) {
				throw new Exception("未能找到有效的当前任务信息！");
			}
			
			businessId = curTaskId;
		} else if(ITimeApplicationService.APPLICATION_TYPE.EVENT_TYPE.getValue().equals(applicationType)) {
			businessId = eventId;
		} else if(ITimeApplicationService.APPLICATION_TYPE.EVENT_VERIFY.getValue().equals(applicationType)) {
			Map<String, Object> verifyParam = new HashMap<String, Object>();
			String REPORT_STATUS = "02",//已上报
				   applicationTypeName = ITimeApplicationService.APPLICATION_TYPE.EVENT_VERIFY.getName();
			List<Map<String, Object>> eventVerifyMapList = null;
			
			verifyParam.put("eventId", eventId);
			verifyParam.put("status", REPORT_STATUS);
			eventVerifyMapList = eventVerifyBaseService.findEventVerifyByParam(verifyParam);
			
			if(eventVerifyMapList == null || eventVerifyMapList.size() == 0) {
				throw new ZhsqEventException("该事件没有【已上报】状态的事件审核记录！");
			} else if(eventVerifyMapList.size() > 1) {
				throw new ZhsqEventException("该事件共有【已上报】状态的事件审核记录【"+ eventVerifyMapList.size() +"】条，无法进行【"+ applicationTypeName +"】！");
			} else {
				Map<String, Object> eventVerifyMap = eventVerifyMapList.get(0);
				
				//若为空或者转换有误，直接抛出异常，因为eventVerifyId为主键
				businessId = Long.valueOf(eventVerifyMap.get("eventVerifyId").toString());
			}
		}
		
		if(orgId != null && orgId > 0) {
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
			
			if(orgInfo != null) {
				String orgChiefLevelStr = orgInfo.getChiefLevel();
				if(StringUtils.isNotBlank(orgChiefLevelStr)) {
					fromOrgLevel = Integer.valueOf(orgChiefLevelStr);
					orgType = orgInfo.getOrgType();
				}
			}
		}
		
		if(fromOrgLevel > 0) {
			String fromOrgType = null,
				   toOrgType = INodeCodeHandler.ORG_UINT,
				   operateType = INodeCodeHandler.OPERATE_REPORT,
				   nodeCode = null;
			int toOrgLevel = 0;
			
			if(String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType)) {
				fromOrgType = INodeCodeHandler.ORG_DEPT;
			} else if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgType)) {
				fromOrgType = INodeCodeHandler.ORG_UINT;
			}
			
			if(fromOrgLevel >= 3) {//县区以下(包含县区)
				toOrgLevel = 3;
			} else if(fromOrgLevel == 2) {//市级
				toOrgLevel = 2;
			}
			
			if(toOrgLevel > 0) {//构造nodeCode
				nodeCode = fromOrgType + fromOrgLevel + operateType + (fromOrgLevel - toOrgLevel) + toOrgType + toOrgLevel; 
			}
			
			if(StringUtils.isNotBlank(nodeCode)) {
				List<GdZTreeNode> nodeList = fiveKeyElementService.getTreeForEvent(userInfo, orgId, nodeCode, null, null, null);
				
				if(nodeList != null && nodeList.size() > 0) {
					//事件时限审核回调服务名称
					String eventCallbackServiceName = "timeApplicationCallback4NPSPatEventService",
						   userOrgCode = userInfo.getOrgCode();
					
					params = new HashMap<String, Object>();
					
					params.put("auditorOrgId", Long.valueOf(nodeList.get(0).getId()));
					params.put("auditorType", ITimeApplicationCheckService.AUDITOR_TYPE.AUDITOR_ORG.getValue());
					params.put("isAutoAudit", true);
					params.put("businessId", businessId);
					params.put("businessKeyId", businessKeyId);
					params.put("applicationType", applicationType);
					params.put("serviceName", eventCallbackServiceName);
					params.put("defaultIntervalUnit", ITimeApplicationService.INTERVAL_UNIT.NATURAL_DAY.toString());
					params.put("userOrgCode", userOrgCode);
				}
			}
		}
		
		return params;
	}
}
