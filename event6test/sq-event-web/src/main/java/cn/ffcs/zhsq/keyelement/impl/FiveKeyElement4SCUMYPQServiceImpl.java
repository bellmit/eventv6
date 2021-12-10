package cn.ffcs.zhsq.keyelement.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 智慧城管(Smart City Urban Manage)延平区(Yan Ping Qu) 人员选择接口
 * @ClassName:   FiveKeyElement4SCUMYPQServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年6月9日 上午11:24:32
 */
@Service(value = "fiveKeyElement4SCUMYPQService")
public class FiveKeyElement4SCUMYPQServiceImpl extends FiveKeyElementForEventServiceImpl {
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	private static final String DEPARTMENT_NODE_CODE = "task7";				//专业部门处理
	
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		INodeCodeHandler nodeCodeHandler = null;
		params = params == null ? new HashMap<String, Object>() : params;
		boolean isShowInterval = DEPARTMENT_NODE_CODE.equals(nodeName);
		
		try {
			nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(DEPARTMENT_NODE_CODE.equals(nodeName)) {
			boolean isSelectOrg = true;
			resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
			
			if(CommonFunctions.isNotBlank(resultMap, "isSelectOrg")) {
				isSelectOrg = Boolean.valueOf(resultMap.get("isSelectOrg").toString());
			}
			
			if(isSelectOrg) {
				ProInstance proInstance = null;
				
				if(CommonFunctions.isNotBlank(params, "instanceId")) {
					Long instanceId = null;
					
					try {
						instanceId = Long.valueOf(params.get("instanceId").toString());
					} catch(NumberFormatException e) {}
					
					if(instanceId != null && instanceId > 0) {
						proInstance = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
					}
				} else if(CommonFunctions.isNotBlank(params, "eventId")) {
					Long eventId = null;
					
					try {
						eventId = Long.valueOf(params.get("eventId").toString());
					} catch(NumberFormatException e) {}
					
					proInstance = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
				}
				
				if(proInstance != null) {
					Long eventId = proInstance.getFormId();
					StringBuffer trigCondition = new StringBuffer("");
					String configOrgCodeStr = null, 
						   workflowName = proInstance.getProName(),
						   eventType = null,
						   userOrgCode = userInfo.getOrgCode();
					
					trigCondition.append(workflowName).append("-").append(nodeName);
					
					if(eventId != null && eventId > 0) {
						EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
						
						if(event != null) {
							eventType = event.getType();
							trigCondition.append("-").append(eventType);
						}
					}
					
					//获取下一环节办理人员配置信息
					configOrgCodeStr = funConfigurationService.turnCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, trigCondition.toString(), IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0, IFunConfigurationService.DIRECTION_UP_FUZZY);
					
					if(StringUtils.isNotBlank(configOrgCodeStr)) {
						String[] configOrgCodeArray = configOrgCodeStr.split(";");//为了和操作类型A兼容
						List<UserInfo> userBOList = new ArrayList<UserInfo>();
						OrgSocialInfoBO orgInfo = null;
						Long removeUserId = userInfo.getUserId();
						Map<String, Object> userParams = new HashMap<String, Object>();
						
						userParams.put("nodeId", nodeId);
						
						for(String configOrgCode : configOrgCodeArray) {
							if(StringUtils.isNotBlank(configOrgCode)) {
								orgInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(configOrgCode);
								
								if(orgInfo != null) {
									userBOList.addAll(getUserInfoList(orgInfo.getOrgId(), nodeCode, removeUserId, userParams));
								}
							}
						}
						
						if(userBOList != null && userBOList.size() > 0) {
							StringBuffer configUserId = new StringBuffer(""),
									 	 configUserName = new StringBuffer(""),
									 	 configOrgId = new StringBuffer(""),
									 	 configOrgName = new StringBuffer("");
							Set<String> userSet = new HashSet<String>();
							String userIdKeyTemp = "@userId_@orgId", userIdKey = null;
							Long userId = null, userOrgId = null;
							
							for(UserInfo userBO : userBOList) {
								userId = userBO.getUserId();
								userOrgId = userBO.getOrgId();
								userIdKey = userIdKeyTemp.replaceAll("@userId", String.valueOf(userId))
											.replaceAll("@orgId", String.valueOf(userOrgId));
								
								if(!userSet.contains(userIdKey)) {
									configUserId.append(",").append(userBO.getUserId());
									configUserName.append(",").append(userBO.getPartyName());
									configOrgId.append(",").append(userBO.getOrgId());
									configOrgName.append(",").append(userBO.getOrgName());
									
									userSet.add(userIdKey);
								}
							}
							
							resultMap.put("userIds", configUserId.substring(1));
							resultMap.put("userNames", configUserName.substring(1));
							resultMap.put("orgIds", configOrgId.substring(1));
							resultMap.put("orgNames", configOrgName.substring(1));
						}
					}
					
					//获取下一环节时限配置信息
					StringBuffer trigCondition4Inteval = new StringBuffer("");
					String intervalDayStr = null;
					
					trigCondition4Inteval.append(ConstantValue.EVENT_HANDLE_DATE_INTERVAL).append("-").append(workflowName).append("-").append(curnodeName).append("-").append(nodeName).append("-").append(eventType);
					
					intervalDayStr = funConfigurationService.changeCodeToValue(ConstantValue.HANDLE_DATE_INTERVAL, trigCondition4Inteval.toString(), IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.DIRECTION_UP_FUZZY);
					
					if(StringUtils.isNotBlank(intervalDayStr)) {
						String intervalWeekdayStr = null,		//工作日
							   intervalNaturalDayStr = null,	//自然日
							   intervalHourStr = null,			//小时
							   WEEK_DAY_INTERVAL = "w",			//工作日间隔类型
							   NATURAL_DAY_INTERVAL = "n",		//自然日间隔类型
							   HOUR_INTERVAL = "h";				//小时间隔类型
						int INTERVAL_UNIT_WEEK_DAY = 1,			//时限单位——工作日
							INTERVAL_UNIT_NATURAL_DAY = 2,		//时限单位——自然日
							INTERVAL_UNIT_HOUT = 3;				//时限单位——小时
						int interval = 0, intervalUnit = 0;
						
						
						intervalDayStr = intervalDayStr.trim().toLowerCase();
						
						if(intervalDayStr.endsWith(WEEK_DAY_INTERVAL)) {
							intervalWeekdayStr = intervalDayStr.replace(WEEK_DAY_INTERVAL, "");
							intervalUnit = INTERVAL_UNIT_WEEK_DAY;
						} else if(intervalDayStr.endsWith(NATURAL_DAY_INTERVAL)) {
							intervalNaturalDayStr = intervalDayStr.replace(NATURAL_DAY_INTERVAL, "");
							intervalUnit = INTERVAL_UNIT_NATURAL_DAY;
						} else if(intervalDayStr.endsWith(HOUR_INTERVAL)) {
							intervalHourStr = intervalDayStr.replace(HOUR_INTERVAL, "");
							intervalUnit = INTERVAL_UNIT_HOUT;
						}
						
						try {
							if(StringUtils.isNotBlank(intervalWeekdayStr)) {
								interval = Integer.valueOf(intervalWeekdayStr);
							} else if(StringUtils.isNotBlank(intervalNaturalDayStr)) {
								interval = Integer.valueOf(intervalNaturalDayStr);
							} else if(StringUtils.isNotBlank(intervalHourStr)) {
								interval = Integer.valueOf(intervalHourStr);
							}
						} catch(NumberFormatException e) {}
						
						if(interval > 0 && intervalUnit > 0) {
							resultMap.put("interval", interval);
							resultMap.put("intervalUnit", intervalUnit);
						}
							
					}
				}
				
				resultMap.put("isSelectUser", false);
				resultMap.put("isSelectOrg", true);
			}
		} else if(nodeCodeHandler != null && nodeCodeHandler.isEquality()) {
			List<UserInfo> handlerUserList = null;
			
			if(CommonFunctions.isBlank(params, "nodeId")) {
				params.put("nodeId", nodeId);
			}
			
			handlerUserList = super.getUserInfoList(userInfo.getOrgId(), nodeCode, params);
			
			if(handlerUserList != null && handlerUserList.size() > 0) {
				StringBuffer handlerUserIds = new StringBuffer(""),
							 handlerUserNames = new StringBuffer(""),
							 handlerOrgIds = new StringBuffer("");
				
				for(UserInfo evaUser : handlerUserList) {
					handlerUserIds.append(",").append(evaUser.getUserId());
					handlerUserNames.append(",").append(evaUser.getPartyName());
					handlerOrgIds.append(",").append(evaUser.getOrgId());
				}
				
				resultMap.put("userIds", handlerUserIds.substring(1));
				resultMap.put("userNames", handlerUserNames.substring(1));
				resultMap.put("orgIds", handlerOrgIds.substring(1));
				
				resultMap.put("eventNodeCode", nodeCodeHandler);
				//办理页面是否显示人员选择 true为是，false为否
				resultMap.put("isSelectUser", true);
			} else {
				resultMap.put("msg", "缺少可办理人员！");
			}
		} else if(nodeCodeHandler != null && nodeCodeHandler.isReportX()) {
			List<GdZTreeNode> treeNodeList = null;
			
			nodeCode = changeNodeCode(nodeCode, nodeCodeHandler, userInfo);
			
			resultMap = this.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
			//获取办理人员组织
			treeNodeList = this.getTreeForEvent(userInfo, null, nodeCode, null, null, params);
			
			if(treeNodeList != null) {
				List<UserInfo> userInfoList = new ArrayList<UserInfo>();
				Long orgId = null;
				
				for(GdZTreeNode treeNode : treeNodeList) {
					orgId = Long.valueOf(treeNode.getId());
					
					userInfoList.addAll(this.getUserInfoList(orgId, nodeCode, params));
				}
				
				if(userInfoList.size() > 0) {
					StringBuffer handlerUserIds = new StringBuffer(""),
								 handlerUserNames = new StringBuffer(""),
								 handlerOrgIds = new StringBuffer("");
					Set<String> userSet = new HashSet<String>();
					String userIdKeyTemp = "@userId_@orgId", userIdKey = null;
					Long userId = null, userOrgId = null;
					
					for(UserInfo user : userInfoList) {
						userId = user.getUserId();
						userOrgId = user.getOrgId();
						userIdKey = userIdKeyTemp.replaceAll("@userId", String.valueOf(userId))
									.replaceAll("@orgId", String.valueOf(userOrgId));
						
						if(!userSet.contains(userIdKey)) {
							handlerUserIds.append(",").append(userId);
							handlerUserNames.append(",").append(user.getPartyName());
							handlerOrgIds.append(",").append(userOrgId);
							
							userSet.add(userIdKey);
						}
					}
					
					resultMap.put("userIds", handlerUserIds.substring(1));
					resultMap.put("userNames", handlerUserNames.substring(1));
					resultMap.put("orgIds", handlerOrgIds.substring(1));
					
					resultMap.put("eventNodeCode", nodeCodeHandler);
					//办理页面是否显示人员选择 true为是，false为否
					resultMap.put("isSelectUser", true);
					resultMap.put("isSelectOrg", false);
				}
			}
		} else {
			resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		}
		
		//true 为展示时限字段
		resultMap.put("isShowInterval", isShowInterval);
		//true 为可上传处理后图片
		resultMap.put("isUploadHandledPic", DEPARTMENT_NODE_CODE.equals(curnodeName));
		
		return resultMap;
	}
	
	@Override
	public List<GdZTreeNode> getTreeForEvent(
			UserInfo userInfo, Long orgId,
			String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception {
		INodeCodeHandler nodeCodeHandler = null;
		
		try {
			nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(nodeCodeHandler != null && nodeCodeHandler.isReportX()) {
			nodeCode = changeNodeCode(nodeCode, nodeCodeHandler, userInfo);
		}
		
		return super.getTreeForEvent(userInfo, orgId, nodeCode, level, professionCode, params);
	}
	
	/**
	 * 变级上报操作码变更
	 * @param nodeCode
	 * @param nodeCodeHandler
	 * @param userInfo
	 * @return
	 */
	protected String changeNodeCode(String nodeCode, INodeCodeHandler nodeCodeHandler, UserInfo userInfo) {
		if(nodeCodeHandler != null && nodeCodeHandler.isReportX()) {
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
			
			if(orgInfo != null) {
				String orgType = orgInfo.getOrgType(), orgChiefLevel = orgInfo.getChiefLevel();
				StringBuffer nodeCodeBuffer = new StringBuffer("");
				int lineLevel = 0;
				
				if(StringUtils.isNotBlank(orgChiefLevel)) {
					lineLevel = Math.abs(Integer.valueOf(orgChiefLevel) - INodeCodeHandler.COUNTY);
				}
				
				if(String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType)) {
					nodeCodeBuffer.append(INodeCodeHandler.ORG_DEPT);
				} else if(String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgType)) {
					nodeCodeBuffer.append(INodeCodeHandler.ORG_UINT);
				}
				
				nodeCodeBuffer.append(orgChiefLevel);
				
				if(lineLevel == 0 && String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType)) {
					nodeCodeBuffer.append(INodeCodeHandler.OPERATE_FLOW);
				} else {
					nodeCodeBuffer.append(INodeCodeHandler.OPERATE_REPORT);
				}
				
				nodeCodeBuffer.append(lineLevel).append(INodeCodeHandler.ORG_UINT).append(INodeCodeHandler.COUNTY);
				
				if(nodeCodeBuffer.length() == 6) {
					nodeCode = nodeCodeBuffer.toString();
				}
			}
		}
		
		return nodeCode;
	}
}
