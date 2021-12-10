package cn.ffcs.zhsq.reportFocus.reportMsgCCSet.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.mybatis.persistence.reportFocus.reportMsgCCSet.ReportMsgCCSetMapper;
import cn.ffcs.zhsq.reportFocus.reportMsgCCSet.IReportMsgCCSetService;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 消息已配送人员信息相关操作
 * 				  相关表T_EVENT_MSG_CC_SET
 * @ClassName:   ReportMsgCCSetServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年10月13日 上午10:06:01
 */
@Service(value = "reportMsgCCSetService")
public class ReportMsgCCSetServiceImpl implements IReportMsgCCSetService {
	@Autowired
	private ReportMsgCCSetMapper reportMsgCCSetMapper;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Override
	public String saveMsgCCSet(Map<String, Object> msgCCSetMap, UserInfo userInfo) throws Exception {
		String setUUID = null;
		
		if(msgCCSetMap == null || msgCCSetMap.isEmpty()) {
			throw new IllegalArgumentException("缺少需要存储的配置人员信息！");
		}
		if(userInfo == null) {
			throw new IllegalArgumentException("缺少操作用户信息！");
		}
		
		Long operateUserId = userInfo.getUserId(), msgUserId = null, msgOrgId = null;
		
		if(CommonFunctions.isBlank(msgCCSetMap, "taskId")) {
			throw new ZhsqEventException("缺少任务id【taskId】！");
		} else {
			Long taskId = null;
			
			try {
				taskId = Long.valueOf(msgCCSetMap.get("taskId").toString());
			} catch(NumberFormatException e) {
				throw new IllegalArgumentException("任务id【" + msgCCSetMap.get("taskId") + "】不能转换为有效的数值！");
			}
			
			if(taskId != null && taskId > 0) {
				msgCCSetMap.put("taskId", taskId);
			} else {
				throw new IllegalArgumentException("任务id【" + taskId + "】不是有效的数值！");
			}
		}
		if(CommonFunctions.isBlank(msgCCSetMap, "ccType")) {
			throw new ZhsqEventException("缺少配送类型【ccType】！");
		}
		if(CommonFunctions.isBlank(msgCCSetMap, "orgId")) {
			throw new ZhsqEventException("缺少配送用户组织id【orgId】！");
		} else if(CommonFunctions.isBlank(msgCCSetMap, "orgName")) {
			OrgSocialInfoBO orgInfo = null;
			
			try {
				msgOrgId = Long.valueOf(msgCCSetMap.get("orgId").toString());
			} catch(NumberFormatException e) {
				throw new ZhsqEventException("组织id【" + msgCCSetMap.get("orgId") + "】不是转换为有效的数值！");
			}
			
			if(msgOrgId != null && msgOrgId > 0) {
				orgInfo = orgSocialInfoService.findByOrgId(msgOrgId);
			}
			
			if(orgInfo != null && orgInfo.getOrgId() != null) {
				msgCCSetMap.put("orgName", orgInfo.getOrgName());
			} else {
				throw new ZhsqEventException("组织id【" + msgOrgId + "】没有找到有效的组织信息！");
			}
		}
		if(CommonFunctions.isBlank(msgCCSetMap, "userId")) {
			throw new ZhsqEventException("缺少配送用户id【userId】！");
		} else if(CommonFunctions.isBlank(msgCCSetMap, "userName")) {
			UserBO userBO = null;
			
			try {
				msgUserId = Long.valueOf(msgCCSetMap.get("userId").toString());
			} catch(NumberFormatException e) {
				throw new ZhsqEventException("用户id【" + msgCCSetMap.get("userId") + "】不是转换为有效的数值！");
			}
			
			if(msgUserId != null && msgUserId > 0) {
				userBO = userManageService.getUserInfoByUserId(msgUserId);
			}
			
			if(userBO != null && userBO.getUserId() != null) {
				msgCCSetMap.put("userName", userBO.getPartyName());
			} else {
				throw new ZhsqEventException("用户id【" + msgUserId + "】没有找到有效的用户信息！");
			}
		}
		
		if(CommonFunctions.isBlank(msgCCSetMap, "creator")) {
			msgCCSetMap.put("creator", operateUserId);
		}
		
		if(CommonFunctions.isBlank(msgCCSetMap, "updator")) {
			msgCCSetMap.put("updator", operateUserId);
		}
		
		if(reportMsgCCSetMapper.insert(msgCCSetMap) > 0) {
			setUUID = msgCCSetMap.get("setUUID").toString();
		}
		
		return setUUID;
	}

	@Override
	public List<Map<String, Object>> findMsgCCSetByParams(Map<String, Object> params) {
		List<Map<String, Object>> msgCCSetList = null;
		
		if(params == null || params.isEmpty()) {
			throw new IllegalArgumentException("缺少查询条件！");
		}
		
		if(CommonFunctions.isBlank(params, "taskId") && CommonFunctions.isBlank(params, "ccType")) {
			throw new IllegalArgumentException("缺少任务id/配送类型！");
		}
		
		msgCCSetList = reportMsgCCSetMapper.findMsgCCSetByParams(params);
		
		return msgCCSetList;
	}

	@Override
	public List<Map<String, Object>> findMsgCCSetByInstanceId(Long instanceId, Map<String, Object> params) {
		List<Map<String, Object>> msgCCSetList = null;
		
		if(instanceId != null && instanceId > 0) {
			params = params == null ? new HashMap<String, Object>() : params;
			
			params.put("instanceId", instanceId);
			
			msgCCSetList = reportMsgCCSetMapper.findMsgCCSetByInstanceId(params);
		} else {
			throw new IllegalArgumentException("缺少有效的流程实例id！");
		}
		
		return msgCCSetList;
	}

}
