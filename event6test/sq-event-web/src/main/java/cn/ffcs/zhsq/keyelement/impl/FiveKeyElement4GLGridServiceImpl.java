package cn.ffcs.zhsq.keyelement.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.handlerCfg.service.IEventHandlerCfgService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.event.EventNodeHandler;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 鼓楼网格(GLGrid)五要素实现类
 * @author zhangls
 *
 */
@Service(value = "fiveKeyElement4GLGridService")
public class FiveKeyElement4GLGridServiceImpl extends
	FiveKeyElementForNCHServiceImpl {

	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserInfoOutService userInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IEventHandlerCfgService eventHandlerCfgService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService;
	
	//网格采集员核查反馈 节点编码
	private static final String GRID_COLLECTOR_CHECK_NODE_CODE = "U0";
	private final Integer ORG_TYPE_DEPARTMENT = 0;// 组织类型 部门
	
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String wrongMsg = "";
		INodeCodeHandler nodeCodeHandler = null;
		boolean isNotUseParent = true, isUseCfgUser = false;
		ProInstance proInstance = null;
		List<EventNodeHandler> eventUserCfgList = null;
		
		if(CommonFunctions.isNotBlank(params, "eventId")) {
			Long eventId = -1L;
			try {
				eventId = Long.valueOf(params.get("eventId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
			if(eventId > 0) {
				proInstance = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
			}
		}
		
		if(proInstance != null) {
			Long workflowId = proInstance.getWorkFlowId();
			EventDisposal event = eventDisposalService.findEventByIdSimple(proInstance.getFormId());
			if(event != null) {
				eventUserCfgList = eventHandlerCfgService.findEventNodeHandlers(workflowId, event.getType(), nodeName, event.getGridCode());
			}
		}
		
		if(eventUserCfgList != null && eventUserCfgList.size() > 0) {//人员自动分配
			StringBuffer transactorIds = new StringBuffer(""),
						 transactorOrgIds = new StringBuffer(""),
						 transactorNames = new StringBuffer("");
			Long userId = -1L, userOrgId = -1L;
			UserBO user = null;
			
			for(EventNodeHandler cfgUser : eventUserCfgList) {
				userId = cfgUser.getUserId();
				userOrgId = cfgUser.getOrgId();
				
				if(userId != null && userId > 0 && userOrgId != null && userOrgId > 0) {
					user = userManageService.getUserInfoByUserId(userId);
					
					if(user != null && user.getUserId() > 0) {
						transactorIds.append(",").append(userId);
						transactorNames.append(",").append(user.getPartyName());
						transactorOrgIds.append(",").append(userOrgId);
					}
				}
			}
			
			if(transactorNames.length() > 0) {
				resultMap.put("userIds", transactorIds.substring(1));
				resultMap.put("userNames", transactorNames.substring(1));
				resultMap.put("orgIds", transactorOrgIds.substring(1));
				
				isNotUseParent = true;
			}
		} else {
			//人为调整nodeCode
			if(StringUtils.isNotBlank(nodeCode) && nodeCode.length() >= 6) {
				if(GRID_COLLECTOR_CHECK_NODE_CODE.equals(nodeCode.subSequence(0, 2))) {
					Long orgId = userInfo.getOrgId();
					OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
					if(orgInfo != null) {
						int chiefLevel = 0, toChiefLevel = 0, lineLevel = 0;
						String chiefLevelStr = orgInfo.getChiefLevel(),
							   toChiefLevelStr = nodeCode.substring(5, 6);
						try {
							chiefLevel = Integer.valueOf(chiefLevelStr);
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
						
						try {
							toChiefLevel = Integer.valueOf(toChiefLevelStr);
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
						
						lineLevel = Math.abs(chiefLevel -  toChiefLevel);
						nodeCode = nodeCode.substring(0, 1) + chiefLevel + nodeCode.substring(2, 3) + lineLevel + nodeCode.substring(4);
					}
				} else {
					nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
					int toLevel = nodeCodeHandler.getToLevel(),
						fromLevel = nodeCodeHandler.getFromLevel(),
						lineLevel = nodeCodeHandler.getLineLevel();
					if(toLevel > 0 && fromLevel > 0) {
						int lineLevelTmp = Math.abs(toLevel - fromLevel);
						if(lineLevelTmp != lineLevel) {
							nodeCode = nodeCode.substring(0, 3) + lineLevelTmp + nodeCode.substring(4);
						}
					}
				}
			}
			
			if(StringUtils.isNotBlank(nodeCode)) {
				nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
				resultMap.put("eventNodeCode", nodeCodeHandler);
				
				StringBuffer userIds = new StringBuffer(""),
							 userNames = new StringBuffer(""),
							 orgIds = new StringBuffer("");
				List<Long> orgIdList = new ArrayList<Long>(),
						   positionIdList = new ArrayList<Long>();
				
				if(nodeCodeHandler.isReport() || nodeCodeHandler.isSplitFlow()) {//上报和下派优先使用工作流职位配置
					if(StringUtils.isNotBlank(nodeId)) {
						Integer nodeIdInt = -1;
						
						try {
							nodeIdInt = Integer.valueOf(nodeId);
						} catch (NumberFormatException e) {
							wrongMsg = "节点编码[" + nodeId + "]不是有效数值！";
						}
						
						if(nodeIdInt > 0) {
							List<Map<String, Object>> nodeActorsMapList = eventDisposalWorkflowService.findNodeActorsById(nodeIdInt);
							if(nodeActorsMapList != null) {
								Long positionId = -1L;
								//获取职位ID
								for(Map<String, Object> nodeActorsMap : nodeActorsMapList) {
									if(CommonFunctions.isNotBlank(nodeActorsMap, "POSITION_ID")) {
										
										try {
											positionId = Long.valueOf(nodeActorsMap.get("POSITION_ID").toString());
										} catch(NumberFormatException e) {
											wrongMsg = "职位配置信息中，职位编码[" + nodeActorsMap.get("POSITION_ID") + "]不是有效数值！";
										}
										
										if(positionId > 0) {
											positionIdList.add(positionId);
										}
									}
								}
							}
						}
					}
				}
				
				if(positionIdList != null && positionIdList.size() > 0) {//如果环节有配置职位，则优先使用职位配置
					//获取上级
					List<OrgSocialInfoBO> orgList = null;
					Long userOrgId = userInfo.getOrgId();
					int orgLevel = nodeCodeHandler.getLineLevel();
					
					if(nodeCodeHandler.isReport()) {
						//旧组织职能部门，层级加一
						if(!eventOrgInfoService.isNewOrganization(userOrgId) && nodeCodeHandler.isFromDept()) {
							orgLevel++;
						}
						
						orgList = this.findOrgByLevel(userOrgId, orgLevel, ConstantValue.GOV_PROFESSION_CODE, nodeCode, userInfo, params);// level为1时，org为上级；level为2时，org为越级上级
					} else if(nodeCodeHandler.isSplitFlow()) {
						if (nodeCodeHandler.isFromUnit() && nodeCodeHandler.isToDept()) {// 单位分流到职能部门
							Integer orgType = null;
							
							if(!eventOrgInfoService.isNewOrganization(userOrgId)){
								orgType = ORG_TYPE_DEPARTMENT;
							}
							
							orgList = orgSocialInfoService.findUnitBySame(userOrgId, false, orgType);
						} else if (nodeCodeHandler.isFromDept() && nodeCodeHandler.isToUnit()) {//职能部门分流到单位
							orgList = orgSocialInfoService.findParentOrg(userInfo.getOrgId());
						} else if(nodeCodeHandler.isFromDept() && nodeCodeHandler.isToDept()) {//职能部门分流到职能部门
							orgList = orgSocialInfoService.findParentOrg(userInfo.getOrgId());
						}
					}
					
					if(orgList != null) {//获取有效的组织id
						Long orgId = -1L;
						//获取组织id
						for(OrgSocialInfoBO orgInfo : orgList) {
							if(orgInfo != null) {
								orgId = orgInfo.getOrgId();
								if(orgId != null && orgId > 0) {
									orgIdList.add(orgId);
								}
							}
						}
					}
					
					if(orgIdList != null && orgIdList.size() > 0) {
						List<UserInfo> userInfoList = userInfoService.findUserListByOrgListAndPositonList(orgIdList, positionIdList);
						
						if(userInfoList != null && userInfoList.size() > 0) {
							for(UserInfo user : userInfoList) {
								userIds.append(",").append(user.getUserId());
								userNames.append(",").append(user.getPartyName());
								orgIds.append(",").append(user.getOrgId());
							}
							
							if(userIds.length() > 0) {
								resultMap.put("userIds", userIds.substring(1).toString());
								resultMap.put("userNames", userNames.substring(1).toString());
								resultMap.put("orgIds", orgIds.substring(1).toString());
								
								isUseCfgUser = true;
							}
						}
					}
				} else if(nodeCodeHandler.isToBegin()) {//发给事件发起人
					if(proInstance != null) {
						resultMap.put("userIds", proInstance.getUserId());
						resultMap.put("userNames", proInstance.getUserName());
						resultMap.put("orgIds", proInstance.getOrgId());
					}
				} else {
					resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
				} 
			} else {
				throw new Exception("环节编码不能为空！");
			}
			
			if(CommonFunctions.isNotBlank(resultMap, "isSelectUser")) {
				isNotUseParent = Boolean.valueOf(resultMap.get("isSelectUser").toString());
			} else {
				isNotUseParent = isUseCfgUser || nodeCodeHandler.isToBegin() || nodeCodeHandler.isToCollect();
			}
		}
		
		resultMap.put("msg", wrongMsg);
		
		//办理页面是否显示人员选择 true为是，false为否
		resultMap.put("isSelectUser", isNotUseParent);
		
		//是否可上传处理后图片
		resultMap.put("isUploadHandledPic", true);
		
		return resultMap;
	}
	
	@Override
	public List<UserInfo> getUserInfoList(Long orgId, String nodeCode, Long removeUserId, Map<String, Object> extraParam) throws Exception {
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		
		if (nodeCode != null) {
			INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
			
			if(nodeCodeHandler.isSend()) {
				List<Long> orgIdList = new ArrayList<Long>(),
						   positionIdList = new ArrayList<Long>();
				
				if(orgId != null && orgId > 0) {
					orgIdList.add(orgId);
				}
				
				if(CommonFunctions.isNotBlank(extraParam, "nodeId")) {
					Integer nodeId = -1;
					
					try {
						nodeId = Integer.valueOf(extraParam.get("nodeId").toString());
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
					
					if(nodeId > 0) {
						List<Map<String, Object>> nodeActorsMapList = eventDisposalWorkflowService.findNodeActorsById(nodeId);
						if(nodeActorsMapList != null && nodeActorsMapList.size() > 0) {
							for(Map<String, Object> nodeActorsMap : nodeActorsMapList) {
								if(CommonFunctions.isNotBlank(nodeActorsMap, "POSITION_ID")) {
									//获取组织下特定职位的人员
									Long positionId = -1L;
									try {
										positionId = Long.valueOf(nodeActorsMap.get("POSITION_ID").toString());
									} catch(NumberFormatException e) {
										e.printStackTrace();
									}
									if(positionId > 0) {
										positionIdList.add(positionId);
									}
								}
							}
						}
					}
					
					userInfos = userInfoService.findUserListByOrgListAndPositonList(orgIdList, positionIdList);
				}
				
			} else {
				userInfos = super.getUserInfoList(orgId, nodeCode, removeUserId, extraParam);
			}
		} else {
			throw new Exception("环节编码不能为空！");
		}
		
		return userInfos;
	}
	
	@Override
	public List<OrgSocialInfoBO> findOrgByLevel(Long orgId, int level, String professionCode, String nodeCode, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		List<OrgSocialInfoBO> orgSocialInfos = null;
		
		if(orgId != null && orgId > 0){
			orgSocialInfos = orgSocialInfoService.findSuperiorByCode(orgId, level, professionCode);
		}
		
		return orgSocialInfos;
	}
}
