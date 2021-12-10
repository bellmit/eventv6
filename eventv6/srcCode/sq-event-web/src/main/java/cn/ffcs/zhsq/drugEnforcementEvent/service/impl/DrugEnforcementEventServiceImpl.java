package cn.ffcs.zhsq.drugEnforcementEvent.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.drugEnforcementEvent.service.IDrugEnforcementEventService;
import cn.ffcs.zhsq.mybatis.domain.drugEnforcementEvent.DrugEnforcementEvent;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 禁毒事件，序列：SEQ_DRUG_ENFORCEMENT_ID模块服务
 * @Author: zhangls
 * @Date: 07-05 15:43:49
 * @Copyright: 2017 福富软件
 */
@Service("drugEnforcementEventService")
public class DrugEnforcementEventServiceImpl extends 
		ApplicationObjectSupport implements IDrugEnforcementEventService {

	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Override
	public Long saveOrUpdateDrugEnforcementEvent(
			DrugEnforcementEvent drugEnforcementEvent, UserInfo userInfo,
			Map<String, Object> extraParam) {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.saveOrUpdateDrugEnforcementEvent(drugEnforcementEvent, userInfo, extraParam);
	}

	@Override
	public boolean deleteDrugEnforcementEventById(Long drugEnforcementId,
			Long delUserId) {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.deleteDrugEnforcementEventById(drugEnforcementId, delUserId);
	}

	@Override
	public DrugEnforcementEvent findDrugEnforcementEventById(
			Long drugEnforcementId, String orgCode) {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.findDrugEnforcementEventById(drugEnforcementId, orgCode);
	}

	@Override
	public int findDrugEnforcementEventCount(Map<String, Object> params, UserInfo userInfo) {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.findDrugEnforcementEventCount(params, userInfo);
	}
	
	@Override
	public EUDGPagination findDrugEnforcementEventPagination(int pageNo,
			int pageSize, UserInfo userInfo, Map<String, Object> params) {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.findDrugEnforcementEventPagination(pageNo, pageSize, userInfo, params);
	}

	@Override
	public boolean reportDrugEnforcementEvent(Long drugEnforcementId,
			UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(userInfo, extraParam);
		
		return drugEnforcementEventService.reportDrugEnforcementEvent(drugEnforcementId, userInfo, extraParam);
	}

	@Override
	public boolean subWorkflow(Long drugEnforcementId, String nextNodeName,
			List<UserInfo> nextUserInfoList, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.subWorkflow(drugEnforcementId, nextNodeName, nextUserInfoList, userInfo, extraParam);
	}

	@Override
	public boolean rejectWorkflow(Long drugEnforcementId,
			String rejectToNodeName, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.rejectWorkflow(drugEnforcementId, rejectToNodeName, userInfo, extraParam);
	}

	@Override
	public List<Map<String, Object>> findNextTaskNodes(Long drugEnforcementId,
			UserInfo userInfo, Map<String, Object> params) throws Exception {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.findNextTaskNodes(drugEnforcementId, userInfo, params);
	}

	@Override
	public List<Map<String, Object>> findNextTaskNodes(Long drugEnforcementId,
			UserInfo userInfo) throws Exception {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.findNextTaskNodes(drugEnforcementId, userInfo);
	}

	@Override
	public Map<String, Object> findCurTaskData(Long drugEnforcementId,
			UserInfo userInfo) throws Exception {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.findCurTaskData(drugEnforcementId, userInfo);
	}

	@Override
	public List<Map<String, Object>> findParticipantsByTaskId(Long taskId) {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.findParticipantsByTaskId(taskId);
	}

	@Override
	public boolean isCurTaskPaticipant(Long drugEnforcementId,
			UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.isCurTaskPaticipant(drugEnforcementId, userInfo, extraParam);
	}

	@Override
	public boolean isCurTaskPaticipant(
			List<Map<String, Object>> participantMapList, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.isCurTaskPaticipant(participantMapList, userInfo, extraParam);
	}

	@Override
	public Long capInstanceId(Long drugEnforcementId, UserInfo userInfo)
			throws Exception {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.capInstanceId(drugEnforcementId, userInfo);
	}

	@Override
	public Map<String, Object> capProInstance(Long instanceId) {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.capProInstance(instanceId);
	}

	@Override
	public Map<String, Object> capProInstance(Long drugEnforcementId,
			UserInfo userInfo) throws Exception {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.capProInstance(drugEnforcementId, userInfo);
	}

	@Override
	public List<Map<String, Object>> findOperateByNodeId(Integer nodeId) {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.findOperateByNodeId(nodeId);
	}

	@Override
	public String capSMSContent(Long drugEnforcementId, String curNodeName,
			String NextNodeName, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.capSMSContent(drugEnforcementId, curNodeName, NextNodeName, userInfo, extraParam);
	}

	@Override
	public boolean sendSMS(UserInfo userInfo, Map<String, Object> params)
			throws Exception {
		IDrugEnforcementEventService drugEnforcementEventService = capServiceImpl(null, null);
		
		return drugEnforcementEventService.sendSMS(userInfo, params);
	}
	
	/**
	 * 获取禁毒事件实现类
	 * @param userInfo
	 * @param params
	 * 			userOrgCode	用户组织编码
	 * @return
	 */
	private IDrugEnforcementEventService capServiceImpl(UserInfo userInfo, Map<String, Object> params) {
		String serviceImpl = null, orgCode = null;
		
		if(userInfo != null) {
			orgCode = userInfo.getOrgCode();
		}
		
		if(StringUtils.isBlank(orgCode) && CommonFunctions.isNotBlank(params, "userOrgCode")) {
			orgCode = params.get("userOrgCode").toString();
		}
		
		serviceImpl = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.DRUG_ENFORCEMENT_EVENT_WORKFLOW, IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, orgCode);
		
		if(StringUtils.isBlank(serviceImpl)) {
			serviceImpl = "drugEnforcementEvent4LXHZService";
		}
		
		return (IDrugEnforcementEventService)this.getApplicationContext().getBean(serviceImpl);
	}
	
}