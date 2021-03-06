package cn.ffcs.zhsq.keyelement.impl;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.GridAdmin;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IGridAdminService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserExBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.keyelement.EventNodeCode;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(value = "fiveKeyElementForEventService")
public class FiveKeyElementForEventServiceImpl implements IFiveKeyElementService {

	@Autowired
	private IGridAdminService gridAdminService;

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private OrgEntityInfoOutService orgEntityInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	private final String COMMUNITY_ORG_CHIEF_LEVEL = "5";//?????? ????????????
	private final int GRID_INFO_ORG_CHIEF_LEVEL = 6;//?????? ????????????
	private final int UNITED_PREVENTION_GROUP_ORG_LEVEL = 7;//??????????????? ????????????
	private final Integer ORG_TYPE_UNIT = 1;//???????????? ??????
	private final Integer ORG_TYPE_DEPARTMENT = 0;// ???????????? ??????
	
	@Override
	public List<UserInfo> getUserInfoList(Long orgId, String nodeCode, Map<String, Object> extraParam) throws Exception {
		return this.getUserInfoList(orgId, nodeCode, null, extraParam);
	}
	
	@Override
	public List<UserInfo> getUserInfoList(Long orgId, String nodeCode, Long removeUserId, Map<String, Object> extraParam) throws Exception {
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		if (nodeCode != null) {
			INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
			List<UserInfo> users = new ArrayList<UserInfo>();//??????????????????????????????
			List<Map<String, Object>> nodeActorsMap = null;
			List<UserBO> userBOList = new ArrayList<UserBO>();
			String partyName = "", userOrgName = null;
			boolean isActor4Org = false;//?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			
			if(orgId != null && orgId > 0) {
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
				if(orgInfo != null) {
					userOrgName = orgInfo.getOrgName();
				}
			}
			if(CommonFunctions.isNotBlank(extraParam, "partyName")) {
				partyName = extraParam.get("partyName").toString();
			}
			
			if(CommonFunctions.isNotBlank(extraParam, "nodeId")) {
				Integer nodeId = Integer.valueOf(extraParam.get("nodeId").toString());

				//?????????????????????nodeId??????????????????????????? ????????????????????????????????? nodeId > 0
				/*if(nodeId > 0) {*/
					nodeActorsMap = eventDisposalWorkflowService.findNodeActorsById(Integer.valueOf(nodeId));
				/*}*/
			}
			
			if(nodeActorsMap != null && nodeActorsMap.size() > 0) {
				Long roleId = -1L, positionId = -1L;
				Map<Long, UserBO> userBOMap = new HashMap<Long, UserBO>();
				String actorName = null;
				StringBuffer roleName = new StringBuffer(""),
						     positionName = new StringBuffer(""),
						     orgName = new StringBuffer("");
				
				for(Map<String, Object> nodeActor : nodeActorsMap) {
					if(CommonFunctions.isNotBlank(nodeActor, "ACTOR_NAME")) {
						actorName = nodeActor.get("ACTOR_NAME").toString();
					}
					
					if(CommonFunctions.isNotBlank(nodeActor, "ORG_ID")) {
						isActor4Org = true;
						userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, null, Long.valueOf(nodeActor.get("ORG_ID").toString())));//roleId positionId orgId
						orgName.append(",").append(actorName);
					} else if(CommonFunctions.isNotBlank(nodeActor, "ROLE_ID")) {
						roleId = Long.valueOf(nodeActor.get("ROLE_ID").toString());
						userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(roleId, null, orgId));//roleId positionId orgId
						roleName.append(",").append(actorName);
					} else if(CommonFunctions.isNotBlank(nodeActor, "POSITION_ID")) {
						positionId = Long.valueOf(nodeActor.get("POSITION_ID").toString());
						userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, positionId, orgId));//roleId positionId orgId
						positionName.append(",").append(actorName);
					}
				}
				
				if(userBOList != null && userBOList.size() > 0) {
					String userPartyName = null;
					
					for(UserBO userBO : userBOList) {
						userPartyName = userBO.getPartyName();
						
						if(StringUtils.isBlank(partyName) || (StringUtils.isNotBlank(userPartyName) && userPartyName.contains(partyName))) {
							userBOMap.put(userBO.getUserId(), userBO);
						}
					}
					
					userBOList.clear();//???????????????????????????
					
					if(!userBOMap.isEmpty()) {
						for(Long userId : userBOMap.keySet()) {
							userBOList.add(userBOMap.get(userId));
						}
					}
				} else {
					String msgWrong = "???????????????????????????????????????";
					if(orgName.length() > 0) {
						msgWrong += "??????????????? [" + orgName.substring(1) + "] ";
					}
					if(roleName.length() > 0) {
						msgWrong += "??????????????? [" + roleName.substring(1) + "] ";
					}
					if(positionName.length() > 0) {
						msgWrong += "??????????????? [" + positionName.substring(1) + "] ";
					}
					
					throw new Exception(msgWrong);
				}
			} else {
				/**
				 * orgId		??????id
				 * type			type???1 ?????????????????????????????????????????????????????????????????? ????????? ???????????????????????????????????????
				 * partyName	??????  ????????????
				 * userType 	????????????  ???????????? 001??????????????????002????????????  003??????????????? 004???????????? 005cpsp?????? 
				 */
				userBOList = userManageService.queryUserListByCondition(orgId, null, partyName, null);
			}
			
			if(userBOList != null) {
				UserInfo userInfoTmp = null;
				for(UserBO userBO : userBOList) {
					userInfoTmp = new UserInfo();
					
					BeanUtils.copyProperties(userBO, userInfoTmp);
					
					if(isActor4Org) {
						userInfoTmp.setOrgId(Long.valueOf(userBO.getSocialOrgId()));
					} else {
						userInfoTmp.setOrgId(orgId);
						userInfoTmp.setOrgName(userOrgName);
					}
					userInfoTmp.setUserName(userBO.getRegisValue());
					
					users.add(userInfoTmp);
				}
				
				if (nodeCodeHandler.isToGrid()) {//?????????????????????????????????
					Long userId = -1L;
					for (UserInfo userInfo : users) {
						userId = userInfo.getUserId();
						if(this.isUserIdGridAdmin(orgId, userId)){
							userInfos.add(userInfo);
						}
					}
				} else if (nodeCodeHandler.isSendToCommunity() || (nodeCodeHandler.isReport() && nodeCodeHandler.isToCommunity())) {
					//??????????????? ?????? ????????????????????????
					MixedGridInfo mixedGridInfo = eventOrgInfoService.findGridInfoBySocialInfoOrgId(orgId);
					String gridAdminDuty = capGridAdminDuty(orgId);
					
					if(mixedGridInfo != null) {
						for (UserInfo userInfo : users) {
							if(StringUtils.isNotBlank(gridAdminDuty)) {
								if(isUserIdGridAdminExists(mixedGridInfo.getGridId(), userInfo.getUserId(), gridAdminDuty)) {
									userInfos.add(userInfo);
								}
							} else {
								if(!this.isUserIdGridAdmin(orgId, userInfo.getUserId())){
									userInfos.add(userInfo);
								}
							}
						}
					}
				} else {
					if (users != null && users.size() > 0) {
						userInfos = users;
					}
				}
			}
		} else {
			throw new Exception("???????????????????????????");
		}
		
		return userInfos;
	}

	/**
	 * ????????????????????????????????????
	 * @param nodeCode ????????????
	 * @return
	 * @throws Exception
	 */
	protected EventNodeCode createNodeCode(String nodeCode) throws Exception {
		if (StringUtils.isNotBlank(nodeCode)) {
			nodeCode = nodeCode.trim();
			if (nodeCode.length() >= 6) {
				EventNodeCode eventNodeCode = new EventNodeCode(nodeCode);
				for (int i = 0; i < 6; i++) {
					char c = nodeCode.charAt(i);
					if (i % 2 == 0) {
						handleA(eventNodeCode, i, c);
					} else {
						handleB(eventNodeCode, i, c);
					}
				}
				
				return eventNodeCode;
			} else {
				throw new Exception("??????????????????????????????6???");
			}
		} else {
			throw new Exception("???????????????????????????");
		}
	}
	
	@Override
	public INodeCodeHandler createNodeCodeHandle(String nodeCode) throws Exception {
		if (StringUtils.isNotBlank(nodeCode)) {
			nodeCode = nodeCode.trim();
			if (nodeCode.length() >= 6) {
				EventNodeCode codeHandler = createNodeCode(nodeCode);
				
				String to = String.valueOf(nodeCode.charAt(4)) + String.valueOf(nodeCode.charAt(5));
				
				if ("CJ__JA".equals(nodeCode)) {
					codeHandler.setCollect(true);
					codeHandler.setClose(true);
				} else if ("CJ".equals(to)) {
					codeHandler.setCollect(true);
				} else if ("JA".equals(to)) {
					codeHandler.setClose(true);
				} else if ("PJ".equals(to)) {
					codeHandler.setComment(true);
				} else if ("GD".equals(to)) {
					codeHandler.setPlaceFile(true);
				}
				
				return codeHandler;
			} else {
				throw new Exception("??????????????????????????????6???");
			}
		} else {
			throw new Exception("???????????????????????????");
		}
	}

	@Override
	public List<OrgSocialInfoBO> findOrgByLevel(Long orgId, int level, String professionCode, String nodeCode, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		List<OrgSocialInfoBO> orgSocialInfos = null;
		
		if(orgId != null && orgId > 0){
			OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoService.findByOrgId(orgId);
			if (orgSocialInfoBO != null) {
				if (userInfo != null) {
					if (COMMUNITY_ORG_CHIEF_LEVEL.equals(orgSocialInfoBO.getChiefLevel()) && isUserIdGridAdmin(orgId, userInfo.getUserId())) {
						level--;
					}
				}
			}
			orgSocialInfos = orgSocialInfoService.findSuperiorByCode(orgId, level, professionCode);
		}
		
		return orgSocialInfos;
	}
	
	@Override
	public List<OrgSocialInfoBO> findOrgByLevel(Long orgId, int level, String professionCode, UserInfo userInfo) {
		List<OrgSocialInfoBO> orgSocialInfos = orgSocialInfoService.findSuperiorByCode(orgId, level, professionCode);
		
		return orgSocialInfos;
	}
	
	@Override
	public List<OrgSocialInfoBO> findOrgByLevel(Long orgId, int level, UserInfo userInfo) {
		List<OrgSocialInfoBO> orgSocialInfos = this.findOrgByLevel(orgId, level, ConstantValue.GOV_PROFESSION_CODE, userInfo);
		return orgSocialInfos;
	}
	
	@Override
	public boolean isUserIdGridAdmin(Long orgId, Long userId){
		boolean isGridAdmin = false;
		
		OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
		String chiefLevel = "";
		
		if(orgInfo != null) {
			chiefLevel = orgInfo.getChiefLevel();
			//?????????????????????????????????????????????????????????????????????????????????????????????????????????
			if(COMMUNITY_ORG_CHIEF_LEVEL.equals(chiefLevel) || String.valueOf(GRID_INFO_ORG_CHIEF_LEVEL).equals(chiefLevel)) {
				OrgEntityInfoBO regionInfo = orgEntityInfoService.findByOrgId(orgInfo.getLocationOrgId());
				
				if (regionInfo != null && userId != null && userId > 0) {
					Map<String, Object> params = new HashMap<String, Object>();
					List<GridAdmin> gridAdminList = null;
					
					params.put("infoOrgCode", regionInfo.getOrgCode());
					params.put("userId", userId);
					params.put("gridLevel", ConstantValue.GRID_ORG_LEVEL);
					params.put("extraInfo", "0");
					
					gridAdminList = gridAdminService.findPageList(params);
					
					if (gridAdminList!=null && gridAdminList.size()>0) {
						for (GridAdmin gridAdmin : gridAdminList) {
							if (ConstantValue.GRID_ORG_LEVEL.equals(gridAdmin.getGridLevel())) {
								isGridAdmin = true;
								break;
							}
						}
					}
				}
			}
		}
		
		if(isGridAdmin){//?????????????????????????????????????????????
			boolean isCommunityGridAdmin = true;
			
			if(COMMUNITY_ORG_CHIEF_LEVEL.equals(chiefLevel) ){
				String isCommunityGridAdminStr = funConfigurationService.turnCodeToValue(ConstantValue.IS_COMMUNITY_GRID_ADMIN, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
				if(StringUtils.isNotBlank(isCommunityGridAdminStr)){
					isCommunityGridAdmin = Boolean.valueOf(isCommunityGridAdminStr);
				}
			}
			isGridAdmin = isCommunityGridAdmin;
		}
		
		return isGridAdmin;
	}
	
	@Override
	public String isUserIdGridAdminForString(Long orgId, Long userId){
		return isUserIdGridAdminForString(this.isUserIdGridAdmin(orgId, userId));
	}
	
	@Override
	public String isUserIdGridAdminForString(boolean isGridAdmin) {
		String userIdGridAdmin = "0";
		
		if(isGridAdmin){
			userIdGridAdmin = "1";
		}
		
		return userIdGridAdmin;
	}
	
	/**
	 * ????????????????????????????????????
	 * @param orgId ????????????
	 * @return ??????????????????????????????????????????????????????????????????'001','002'?????????????????????????????????
	 */
	@Override
	public String capGridAdminDuty(Long orgId) {
		String gridAdminDuty = "";
		
		if(orgId != null && orgId > 0) {
			OrgSocialInfoBO orgSocialInfo = orgSocialInfoService.findByOrgId(orgId);
			
			if(orgSocialInfo != null) {
				String orgCode = orgSocialInfo.getOrgCode();
				String workflowAttrName = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.GRID_ADMIN, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
				
				if(StringUtils.isNotBlank(workflowAttrName)) {
					gridAdminDuty = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.GRID_ADMIN_DUTY, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
					if(StringUtils.isNotBlank(gridAdminDuty)) {
						gridAdminDuty = "'" + gridAdminDuty.replaceAll(",", "','") + "'";
					}
				
				}
			}
		}
		
		return gridAdminDuty;
	}
	
	/**
	 * ???????????????????????????????????????????????????
	 * ?????????????????????????????????????????????????????????????????????????????????
	 * @param startGridId ???????????????????????????
	 * @param userId ??????id
	 * @param gridAdminDuty ?????????????????????
	 * 001 ??????????????????002 ??????????????????003 ??????????????????004 ???????????????
	 * @return true ?????????false ?????????
	 */
	@Override
	public boolean isUserIdGridAdminExists(Long startGridId, Long userId, String gridAdminDuty){
		boolean isGridAdminExists = false;
		
		if (startGridId != null && startGridId > 0) {
			
			Map<String, Object> params = new HashMap<String, Object>();
			
			if(StringUtils.isNotBlank(gridAdminDuty)) {
				params.put("duty", gridAdminDuty);//????????????????????????????????????'001','002'
			}
			
			params.put("startGridId", startGridId);
			params.put("userId", userId);
			
			List<GridAdmin> gridAdminList = gridAdminService.findPageList(params);
			isGridAdminExists = gridAdminList.size() > 0;
		}
		
		return isGridAdminExists;
	}
	
	@Override
	public List<UserBO> findReportUserList(String nodeCode,
			String positionName, Long positionId, String includeNext,
			Long orgId, String orgCode) throws Exception {
		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		return this.getReportUserList(nodeCodeHandler, positionName, positionId, includeNext, orgId, orgCode);
	}

	@Override
	public List<UserBO> getReportUserList(INodeCodeHandler nodeCodeHandler,
			String positionName, Long positionId, String includeNext,
			Long orgId, String orgCode) {
		List<UserBO> users = null;
		
		if (nodeCodeHandler.isCollect() && nodeCodeHandler.isClose()) {// CJ__JA
			users = new ArrayList<UserBO>();
			OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoService.findByOrgId(orgId);
			
			if(orgSocialInfoBO != null) {
				String orgChiefLevel = orgSocialInfoBO.getChiefLevel();
				
				users.addAll(userManageService.getUserListByUserExampleParamsOut(null, null, orgId));
				
				if(ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)) {
					users = this.filterGridAdmin(orgId, users, true);
				} else if(ConstantValue.GRID_ORG_LEVEL.equals(orgChiefLevel)) {
					users = this.filterGridAdmin(orgId, users, false);
				}
			}
		} else {
			users = userManageService.queryUserListByPositionAndOrg(positionName, null, "0", orgId, null);
			if(nodeCodeHandler.isGridReportToCommunity()) {// ???????????????????????????????????????
				users = this.filterGridAdmin(orgId, users, true);
			}
		}
		
		return users;
	}

	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("msg", "");
		Map<String, Object> levelAndPostionName = eventDisposalWorkflowService.capTurnLevel(curnodeName, nodeName, userInfo);
		INodeCodeHandler nodeCodeHandler = null;
		
		Map<String, Object> allocationMap = fetchAllocation(nodeCodeHandler, nodeName, userInfo);
		if(!allocationMap.isEmpty()) {//????????????????????????????????????
			resultMap.putAll(allocationMap);
		} else {
			if(StringUtils.isNotBlank(nodeCode)) {
				try {
					nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
					resultMap.put("eventNodeCode", nodeCodeHandler);
					
					//???????????????????????????????????????????????????????????????????????????
					if (false && nodeCodeHandler.isReport() && nodeCodeHandler.isFromUnit() && nodeCodeHandler.isToUnit()) {// ???????????????
						// ????????????????????????
						List<OrgSocialInfoBO> orgList = this.findOrgByLevel(userInfo.getOrgId(), nodeCodeHandler.getLineLevel(), ConstantValue.GOV_PROFESSION_CODE, nodeCode, userInfo, params);// level???1??????org????????????level???2??????org???????????????
						StringBuffer userIds = new StringBuffer("");
						StringBuffer userNames = new StringBuffer("");
						StringBuffer orgIds = new StringBuffer("");
						String positionName = (String) levelAndPostionName.get("positionName");
						Long orgId = -1L;
						List<UserBO> users = null;
						
						for(OrgSocialInfoBO org : orgList) {
							if (org != null) {
								orgId = org.getOrgId();
								users = this.getReportUserList(nodeCodeHandler, positionName, null, "0", orgId, null);
								
								if (users != null && users.size() > 0) {
									StringBuffer userIdTemp = new StringBuffer("");
									for (UserBO user : users) {
										userIdTemp = new StringBuffer("").append(user.getUserId()).append(",");
										if (userIds.indexOf(userIdTemp.toString()) == -1) {// ?????????????????????
											userIds.append(userIdTemp);
											userNames.append(user.getPartyName()).append(",");
											orgIds.append(orgId).append(",");
										}
									}
								}
							}
						}
						
						if(orgList == null || orgList.size() < 0) {
							resultMap.put("msg", "???????????????????????????");
						} else if (userNames != null && userNames.length() > 0) {
							userNames = new StringBuffer(userNames.substring(0, userNames.length() - 1));
							
							resultMap.put("userIds", userIds);
							resultMap.put("userNames", userNames);
							resultMap.put("orgIds", orgIds);
						} else {
							resultMap.put("msg", "???????????????????????????");
						}
						
					} else if (nodeCodeHandler.isReport() && nodeCodeHandler.isFromDept() && nodeCodeHandler.isToDept()) {// ?????????????????????????????????
						
					} else if (nodeCodeHandler.isPerson()) {//?????????????????????
						List<Map<String, Object>> nodeActorsMap = this.eventDisposalWorkflowService.findNodeActorsById(Integer.valueOf(nodeId));
						Set<String> userIdSet = new HashSet<String>();
						if (nodeActorsMap != null && nodeActorsMap.size() > 0) {
							String userIdStr = "";
							for (Map<String, Object> nodeActor : nodeActorsMap) {
								if (CommonFunctions.isNotBlank(nodeActor, "USER_ID")) {
									userIdStr = nodeActor.get("USER_ID").toString();
									
									userIdSet.add(userIdStr);
								}
							}
							
							if (userIdSet.size() > 0) {
								StringBuffer userIds = new StringBuffer(""),
											 userNames = new StringBuffer(""),
											 orgIds = new StringBuffer("");
								UserBO userBO = null;
								List<UserExBO> userExBOList = null;
								
								for(String userId : userIdSet) {
									userBO = userManageService.getUserInfoByUserId(Long.valueOf(userId));
									if(userBO != null) {
										userExBOList = userBO.getUserExBOs();
										if(userExBOList != null) {
											for(UserExBO userExBO : userExBOList) {
												userIds.append(",").append(userId);
												userNames.append(",").append(userBO.getPartyName()).append("(").append(userExBO.getOrgName()).append(")");
												orgIds.append(",").append(userExBO.getSocialOrgId());
											}
										}
									}
								}
								
								resultMap.put("userIds", userIds.substring(1));
								resultMap.put("userNames", userNames.substring(1));
								resultMap.put("orgIds", orgIds.substring(1));
							}
						}
					} else if (nodeCodeHandler.isOrganization()) {//?????????????????????
						List<Map<String, Object>> nodeActorsMap = this.eventDisposalWorkflowService.findNodeActorsById(Integer.valueOf(nodeId));
						
						if (nodeActorsMap != null && nodeActorsMap.size() > 0) {
							List<UserBO> userBOList = new ArrayList<UserBO>();
							String orgIdStr = "";
							
							for (Map<String, Object> nodeActor : nodeActorsMap) {
								if (CommonFunctions.isNotBlank(nodeActor, "ORG_ID")) {
									orgIdStr = nodeActor.get("ORG_ID").toString();
									if(StringUtils.isNotBlank(orgIdStr)) {
										userBOList.addAll(userManageService.getUserListByUserExampleParamsOut(null, null, Long.valueOf(orgIdStr)));
									}
								}
							}
							
							if(userBOList.size() > 0) {
								StringBuffer userIds = new StringBuffer(""),
										     userNames = new StringBuffer(""),
										     orgIds = new StringBuffer("");
								
								for(UserBO userBO : userBOList) {
									userIds.append(",").append(userBO.getUserId());
									userNames.append(",").append(userBO.getPartyName());
									orgIds.append(",").append(userBO.getSocialOrgId());
								}
								
								resultMap.put("userIds", userIds.substring(1));
								resultMap.put("userNames", userNames.substring(1));
								resultMap.put("orgIds", orgIds.substring(1));
							}
						}
						
					} else if(nodeCodeHandler.isToBegin()) {//?????????????????????
						if(CommonFunctions.isNotBlank(params, "instanceId")) {
							Long instanceId = Long.valueOf(params.get("instanceId").toString());
							ProInstance pro = baseWorkflowService.findProByInstanceId(instanceId);
							if(pro != null) {
								resultMap.put("userIds", pro.getUserId());
								resultMap.put("userNames", pro.getUserName());
								resultMap.put("orgIds", pro.getOrgId());
							}
						} else {
							resultMap.put("userIds", userInfo.getUserId());
							resultMap.put("userNames", userInfo.getPartyName());
							resultMap.put("orgIds", userInfo.getOrgId());
						}
					} else if(nodeCodeHandler.isAllocation()) {//???????????????????????????????????????????????????????????????????????????????????????????????????????????????
						resultMap.putAll(fetchAllocation(nodeCodeHandler, nodeName, userInfo));
					}
				} catch (Exception e) {
					resultMap.put("msg", e.getMessage());
				}
			} else {
				resultMap.put("userIds", userInfo.getUserId());
				resultMap.put("userNames", userInfo.getPartyName());
				resultMap.put("orgIds", userInfo.getOrgId());
			}
		}
		
		//???????????????????????????????????? true?????????false??????
		resultMap.put("isSelectUser", nodeCodeHandler==null || nodeCodeHandler.isPerson() || nodeCodeHandler.isOrganization() || nodeCodeHandler.isToBegin() || nodeCodeHandler.isAllocation());
		//???????????????????????????????????? true?????????false??????
		resultMap.put("isSelectOrg", nodeCodeHandler!=null && (nodeCodeHandler.isSend() || nodeCodeHandler.isSplitFlow() || nodeCodeHandler.isSplitFlowGlobal() || nodeCodeHandler.isReport() || nodeCodeHandler.isToCollect()));
		
		return resultMap;
	}

	@Override
	public List<GdZTreeNode> getTreeForEvent(
			UserInfo userInfo, Long orgId,
			String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception {
		List<GdZTreeNode> nodes = new ArrayList<GdZTreeNode>();
		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		level = level == null ? 0 : level;
		boolean isFromCommunityGridAdmin = false;
		
		if (nodeCodeHandler.isGlobal()) {//??????????????????
			List<OrgSocialInfoBO> socialInfoBOs = null;
			
			if(orgId == null || orgId < 0) {
				orgId = -1L;
			}
			
			//G0 ??????????????????????????????????????????
			if(nodeCodeHandler.getLineLevel() == 0 && level == 0 && CommonFunctions.isNotBlank(params, "orgRootId")) {
				OrgSocialInfoBO orgInfo = null;
				
				try {
					Long orgRootId = Long.valueOf(params.get("orgRootId").toString());
					orgInfo = orgSocialInfoService.findByOrgId(orgRootId);
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if(orgInfo != null && orgInfo.getOrgId() != null) {
					socialInfoBOs = new ArrayList<OrgSocialInfoBO>();
					socialInfoBOs.add(orgInfo);
				}
			} else {//??????????????????????????????????????????
				socialInfoBOs = orgSocialInfoService.findOrgSocialListByParentIdAndProId(orgId, null);
			}
			
			for (OrgSocialInfoBO socialOrg : socialInfoBOs) {
				nodes.add(CommonFunctions.transSocialOrgToZTreeNodeA(socialOrg, socialOrg.isLeaf()));
			}
			
		} else if(nodeCodeHandler.isSplitFlowGlobal()) {//???????????????????????????
			List<OrgSocialInfoBO> socialInfoBOs = null;
			int levelGap = level - nodeCodeHandler.getFromLevel() - nodeCodeHandler.getLineLevel();
			
			if(orgId == null || orgId < 0) {
				if(nodeCodeHandler.getLineLevel() == 0) {
					orgId = -1L;
				} else {
					orgId = userInfo.getOrgId();
				}
			}
			
			//f0 ??????????????????????????????????????????
			if(level == 0 && CommonFunctions.isNotBlank(params, "orgRootId")) {
				OrgSocialInfoBO orgInfo = null;
				
				try {
					Long orgRootId = Long.valueOf(params.get("orgRootId").toString());
					orgInfo = orgSocialInfoService.findByOrgId(orgRootId);
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if(orgInfo != null && orgInfo.getOrgId() != null) {
					socialInfoBOs = new ArrayList<OrgSocialInfoBO>();
					
					socialInfoBOs.add(orgInfo);
				}
			} else if(levelGap <= 0) {//??????????????????????????????????????????
				if(levelGap == 0) {
					OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
					
					if(orgInfo != null) {
						List<OrgSocialInfoBO> orgInfoList = new ArrayList<OrgSocialInfoBO>();
						
						orgInfoList.add(orgInfo);
						
						this.findDeptBySameAndAdd(orgInfoList, nodes, null);
					}
				} else {
					socialInfoBOs = orgSocialInfoService.findOrgSocialListByParentIdAndProId(orgId, null);
				}
			}
			
			if(socialInfoBOs != null) {
				for (OrgSocialInfoBO socialOrg : socialInfoBOs) {
					nodes.add(CommonFunctions.transSocialOrgToZTreeNodeA(socialOrg, socialOrg.isLeaf()));
				}
			}
		} else if (nodeCodeHandler.isSend()) {// ??????
			boolean isRootNode = (orgId == null || orgId < 0);
			
			if (isRootNode) {
				orgId = userInfo.getOrgId();
				
				if (nodeCodeHandler.isFromUnit()) {
					OrgSocialInfoBO socialOrg = orgSocialInfoService.findByOrgId(orgId);
					socialOrg.setChiefLevel("0");
					//boolean isLeaf = !(nodeCodeHandler.isFromCommunity() && nodeCodeHandler.isToGrid());
					nodes.add(CommonFunctions.transSocialOrgToZTreeNode(socialOrg));
				}
			}
			
			if ((nodeCodeHandler.isFromDept() && isRootNode) || !isRootNode) {
				// ??????????????????
				boolean isLeaf = !(level + ((nodeCodeHandler.isSendToGrid() && nodeCodeHandler.getToLevel() - nodeCodeHandler.getFromLevel() > 1) ? 2 : 1) == nodeCodeHandler.getLineLevel());
				List<OrgSocialInfoBO> socialInfoBOs = null;
				GdZTreeNode node = null;
				
				if(nodeCodeHandler.isToDept() && level == nodeCodeHandler.getLineLevel() ) {//???????????????
					List<OrgSocialInfoBO> orgSocialInfoList = null;
					
					OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
					if(orgInfo != null) {
						orgSocialInfoList = new ArrayList<OrgSocialInfoBO>();
						orgSocialInfoList.add(orgInfo);
					}
					
					findDeptByFilterProffession(orgSocialInfoList, nodes, params, userInfo.getOrgId());
				} else {
					socialInfoBOs = orgSocialInfoService.findLevelByParam(orgId, ConstantValue.GOV_PROFESSION_CODE, false);
					
					OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoService.findByOrgId(orgId);
					if (orgSocialInfoBO != null) {
						if (userInfo != null) {
							isFromCommunityGridAdmin = COMMUNITY_ORG_CHIEF_LEVEL.equals(orgSocialInfoBO.getChiefLevel()) && isUserIdGridAdmin(orgId, userInfo.getUserId());
						}
					}
					
					int lineLevel = nodeCodeHandler.getLineLevel(), toLevel = nodeCodeHandler.getToLevel();
					
					for (OrgSocialInfoBO socialOrg : socialInfoBOs) {
						socialOrg.setChiefLevel(String.valueOf(level + 1));
						node = CommonFunctions.transSocialOrgToZTreeNodeA(socialOrg, isLeaf);
						if(node != null){
							if(nodeCodeHandler.isToDept()) {
								node.setIsParent(!socialOrg.isLeaf());
							} else {
								if(isFromCommunityGridAdmin && toLevel == UNITED_PREVENTION_GROUP_ORG_LEVEL) {//????????????????????????????????????????????????????????????lineLevel????????????toLevel???????????????????????????????????????????????????????????????????????????????????????????????????
									lineLevel++;
								}
								
								node.setIsParent(!socialOrg.isLeaf() && (level < (lineLevel - 1)));
							}
							
							nodes.add(node);
						}
					}
				}
			}
		} else if(nodeCodeHandler.isSplitFlow()) {//??????
			if (nodeCodeHandler.isFromUnit() && nodeCodeHandler.isToDept()) {// ???????????????????????????
				List<OrgSocialInfoBO> orgSocialInfoList = null;
				OrgSocialInfoBO orgInfo = null;
				int lineLevel = nodeCodeHandler.getLineLevel();
				
				if(orgId == null) {
					orgId = userInfo.getOrgId();
				}
				
				orgInfo = orgSocialInfoService.findByOrgId(orgId);
				
				//???????????????
				if(lineLevel == 1 && level == 0 && orgInfo != null) {
					GdZTreeNode rootNode = CommonFunctions.transSocialOrgToZTreeNodeA(orgInfo, true);
					rootNode.setIsParent(true);
					nodes.add(rootNode);
					
					orgInfo = null;
				}
				
				if(orgInfo != null) {
					orgSocialInfoList = new ArrayList<OrgSocialInfoBO>();
					orgSocialInfoList.add(orgInfo);
					
					findDeptByFilterProffession(orgSocialInfoList, nodes, params, userInfo.getOrgId());
				}
			} else if (nodeCodeHandler.isFromDept() && nodeCodeHandler.isToUnit()) {//???????????????????????????
				List<OrgSocialInfoBO> orgSocialInfoList = orgSocialInfoService.findParentOrg(userInfo.getOrgId());
				
				for(OrgSocialInfoBO orgSocialInfo : orgSocialInfoList) {
					nodes.add(CommonFunctions.transSocialOrgToZTreeNodeA(orgSocialInfo, true));
				}
			} else if(nodeCodeHandler.isFromDept() && nodeCodeHandler.isToDept()) {//?????????????????????????????????
				List<OrgSocialInfoBO> orgSocialInfoList = orgSocialInfoService.findParentOrg(userInfo.getOrgId());
				
				findDeptByFilterProffession(orgSocialInfoList, nodes, params, userInfo.getOrgId());
			}
		} else if (nodeCodeHandler.isReport()) {//??????
			int orgLevel = nodeCodeHandler.getLineLevel();
			List<OrgSocialInfoBO> orgInfoList = null;
			Long userOrgId = userInfo.getOrgId();
			
			//????????????????????????????????????
			if(nodeCodeHandler.isFromDept()) {
				orgLevel++;
			}

			orgInfoList = this.findOrgByLevel(userOrgId, orgLevel, ConstantValue.GOV_PROFESSION_CODE, nodeCode, userInfo, params);
			
			if(nodeCodeHandler.isToDept()) {//??????????????????????????? ?????????????????????????????????
				findDeptByFilterProffession(orgInfoList, nodes, params, null);
			} else {
				for (OrgSocialInfoBO orgSocialInfo : orgInfoList) {
					nodes.add(CommonFunctions.transSocialOrgToZTreeNodeA(orgSocialInfo, true));
				}
			}
		}
		
		///////////////////////////////???????????? clickable??????///////////////////////////////////////////////////////////
		this.addClickableAttr2Node(nodeCodeHandler, nodes, isFromCommunityGridAdmin);
		
		return nodes;
	}

	/**
	 * ???????????????clickable??????
	 * @param nodeCodeHandler
	 * @param nodes
	 * @param isFromCommunityGridAdmin
	 */
	protected void addClickableAttr2Node(INodeCodeHandler nodeCodeHandler, List<GdZTreeNode> nodes, boolean isFromCommunityGridAdmin) {
		if(nodes != null) {
			//????????????????????????????????????????????????????????????????????????
			//?????????????????????????????????????????????????????????????????????????????????
			boolean isReport = nodeCodeHandler.isReport(),		//????????????
					isSplitFlow = nodeCodeHandler.isSplitFlow(),//????????????
					isSplitFlowGlobal = nodeCodeHandler.isSplitFlowGlobal(),//?????????????????????????????????
					isSend = nodeCodeHandler.isSend(),			//????????????
					isToUnit = nodeCodeHandler.isToUnit(),		//???????????????
					isToDept = nodeCodeHandler.isToDept(),		//???????????????
					isGlobal = nodeCodeHandler.isGlobal(),		//sq_event_web
					isClickable = true;							//?????????????????????
			
			int toLevel = nodeCodeHandler.getToLevel(),
				lineLevel = nodeCodeHandler.getLineLevel(),
				levelGap = 0,
				gridLevel = 0;									//????????????
			String gridLevelStr = "",
				   ORG_TYPE_DEPT = String.valueOf(ConstantValue.ORG_TYPE_DEPT);
				
			for(GdZTreeNode node : nodes) {
				if(isSplitFlowGlobal) {
					isClickable = !node.getIsParent() && ORG_TYPE_DEPT.equals(node.getLayerType());
				} else if(!isReport && !isSplitFlow && !isGlobal) {
					isClickable = false;
					gridLevelStr = node.getGridLevel();
					gridLevel = 0;
					
					if(StringUtils.isNotBlank(gridLevelStr)) {
						try{
							gridLevel = Integer.valueOf(gridLevelStr);
						} catch(NumberFormatException e) {
							gridLevel = 0;
							e.printStackTrace();
						}
					}
					
					if(isToDept) {//???????????????????????????
						isClickable = gridLevel == (lineLevel + 1);
					} else if(toLevel == 6) {
						levelGap = (isSend && isToUnit && toLevel == 6) ? 1 : 0;
						
						if(isFromCommunityGridAdmin) {
							lineLevel++;
						}
						
						if (lineLevel - gridLevel <= levelGap) {
							isClickable = true;
						}
					} else if(isSend) {
						isClickable = gridLevel == lineLevel;
					} else {
						isClickable = !node.getIsParent();
					}
				}
				
				node.setClickable(isClickable);
			}
		}
	}
	
	@Override
	public List<UserBO> findCollectToCloseUserList(Long orgId) throws Exception {
		return this.findReportUserList("CJ__JA", null, null, null, orgId, null);
	}

	@Override
	public List<UserBO> filterGridAdmin(Long orgId, List<UserBO> users) {
		return filterGridAdmin(orgId, users, true);
	}
	
	@Override
	public List<UserBO> filterGridAdmin(Long orgId, List<UserBO> users, boolean isRemoveGridAdmin) {
		List<UserBO> userInfos = new ArrayList<UserBO>();
		MixedGridInfo gridInfo = eventOrgInfoService.findGridInfoBySocialInfoOrgId(orgId);
		if (gridInfo != null && gridInfo.getGridId() != null && users != null) {
			boolean isGridAdmin = false;
			
			for (UserBO user : users) {
				isGridAdmin = this.isUserIdGridAdmin(orgId, user.getUserId());
				
				if(isRemoveGridAdmin && !isGridAdmin){
					userInfos.add(user);
				} else if(!isRemoveGridAdmin && isGridAdmin) {
					userInfos.add(user);
				}
			}
		}
		
		return userInfos;
	}
	
	@Override
	public Map<Long, OrgSocialInfoBO> findDeptBySameAndAdd(List<OrgSocialInfoBO> orgInfoList, List<GdZTreeNode> nodes, Long removeOrgId) {
		Long userOrgId = null;
		List<OrgSocialInfoBO> socialInfoBOs = null;
		Integer orgType = null;
		String removeProfessionCode = "";
		Map<Long, OrgSocialInfoBO> socialInfoMap = new HashMap<Long, OrgSocialInfoBO>();
		
		if(orgInfoList != null && orgInfoList.size() > 0) {
			if(removeOrgId != null && removeOrgId > 0) {
				OrgSocialInfoBO removeOrgSocialInfo = orgSocialInfoService.findByOrgId(removeOrgId);
				if(removeOrgSocialInfo != null) {
					removeProfessionCode = removeOrgSocialInfo.getProfessionCode();
				}
			}
			for(OrgSocialInfoBO orgSocialInfo : orgInfoList) {
				userOrgId = orgSocialInfo.getOrgId();
				
				if(!eventOrgInfoService.isNewOrganization(userOrgId)){
					orgType = ORG_TYPE_DEPARTMENT;
				}
				
				socialInfoBOs = orgSocialInfoService.findUnitBySame(userOrgId, false, orgType);
				
				for (OrgSocialInfoBO socialOrg : socialInfoBOs) {
					if(StringUtils.isBlank(removeProfessionCode) || !removeProfessionCode.equals(socialOrg.getProfessionCode())) {
						socialInfoMap.put(socialOrg.getOrgId(), socialOrg);
					}
				}
			}
			
			if(nodes != null) {
				Set<Long> socialInfoIdSet = socialInfoMap.keySet();
				for(Long socialInfoId : socialInfoIdSet) {//??????????????????????????????????????????
					OrgSocialInfoBO socialOrg = socialInfoMap.get(socialInfoId);
					if (!eventOrgInfoService.isGovernment(socialOrg.getProfessionCode())) {
						socialOrg.setChiefLevel("0");
						nodes.add(CommonFunctions.transSocialOrgToZTreeNodeA(socialOrg, true));
					}
				}
			}
		}
		
		return socialInfoMap;
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param eventNodeCode
	 * @param index 0 ?????????????????????2 ???????????????4 ?????????????????????
	 * @param c
	 */
	private void handleA(EventNodeCode eventNodeCode, int index, char c) {
		switch (index) {
		case 0:
			switch (c) {
			case 'D':
				eventNodeCode.setFromUnit(false);
				eventNodeCode.setFromDept(true);
				eventNodeCode.setFromExclusiveGrid(false);
				break;
			case 'U':
				eventNodeCode.setFromUnit(true);
				eventNodeCode.setFromDept(false);
				eventNodeCode.setFromExclusiveGrid(false);
				break;
			case 'E':
				eventNodeCode.setFromUnit(false);
				eventNodeCode.setFromDept(false);
				eventNodeCode.setFromExclusiveGrid(true);
				break;
			default:
				eventNodeCode.setFromUnit(false);
				eventNodeCode.setFromDept(false);
				eventNodeCode.setFromExclusiveGrid(false);
			}
			break;
		case 2:
			eventNodeCode.setSend(false);
			eventNodeCode.setReport(false);
			eventNodeCode.setReportX(false);
			eventNodeCode.setSplitFlow(false);
			eventNodeCode.setSplitFlowGlobal(false);
			eventNodeCode.setPerson(false);
			eventNodeCode.setOrganization(false);
			eventNodeCode.setToCollect(false);
			eventNodeCode.setToBegin(false);
			eventNodeCode.setGlobal(false);
			eventNodeCode.setAllocation(false);
			eventNodeCode.setEquality(false);
			
			switch (c) {
			case 'S'://Send
				eventNodeCode.setSend(true);
				break;
			case 'R'://Report
				eventNodeCode.setReport(true);
				break;
			case 'r'://ReportX
				eventNodeCode.setReportX(true);
				break;
			case 'F'://Flow
				eventNodeCode.setSplitFlow(true);
				break;
			case 'f':
				eventNodeCode.setSplitFlowGlobal(true);
				break;
			case 'P'://Person
				eventNodeCode.setPerson(true);
				break;
			case 'O'://Organization
				eventNodeCode.setOrganization(true);
				break;
			case 'C'://Collect
				eventNodeCode.setToCollect(true);
				break;
			case 'B'://Begin
				eventNodeCode.setToBegin(true);
				break;
			case 'G'://Global
				eventNodeCode.setGlobal(true);
				break;
			case 'A'://Allocation
				eventNodeCode.setAllocation(true);
				break;
			case 'E'://Equality
				eventNodeCode.setEquality(true);
				break;
			default:
			}
			eventNodeCode.setLineCode(String.valueOf(c));
			break;
		case 4:
			switch (c) {
			case 'D':
				eventNodeCode.setToUnit(false);
				eventNodeCode.setToDept(true);
				eventNodeCode.setToExclusiveGrid(false);
				break;
			case 'U':
				eventNodeCode.setToUnit(true);
				eventNodeCode.setToDept(false);
				eventNodeCode.setToExclusiveGrid(false);
				break;
			case 'E':
				eventNodeCode.setToUnit(false);
				eventNodeCode.setToDept(false);
				eventNodeCode.setToExclusiveGrid(true);
				break;
			default:
				eventNodeCode.setToUnit(false);
				eventNodeCode.setToDept(false);
				eventNodeCode.setToExclusiveGrid(false);
			}
			break;
		}
	}
	
	/**
	 * ????????????????????????
	 * @param eventNodeCode
	 * @param index	1 ???????????????????????????3 ??????????????????????????????5 ???????????????????????????
	 * @param c
	 */
	private void handleB(EventNodeCode eventNodeCode, int index, char c) {
		Integer decimal = Integer.valueOf(c);
		if (decimal >= 48 && decimal <= 57) {
			Integer level = Integer.valueOf(String.valueOf(c));
			switch (index) {
			case 1:
				eventNodeCode.setFromLevel(level);
				break;
			case 3:
				eventNodeCode.setLineLevel(level);
				break;
			case 5:
				eventNodeCode.setToLevel(level);
				break;
			}
		} else {
			switch (index) {
			case 1:
				eventNodeCode.setFromLevel(-1);
				break;
			case 3:
				eventNodeCode.setLineLevel(-1);
				break;
			case 5:
				eventNodeCode.setToLevel(-1);
				break;
			}
		}
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param nodeCodeHandler
	 * @param nextNodeName	??????????????????
	 * @param userInfo		????????????????????????
	 * @return
	 */
	private Map<String, Object> fetchAllocation(INodeCodeHandler nodeCodeHandler, String nextNodeName, UserInfo userInfo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String orgCode = userInfo.getOrgCode(),
			   userNameOrgCodes = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, nextNodeName, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
		
		if(StringUtils.isNotBlank(userNameOrgCodes)) {
			StringBuffer userIds = new StringBuffer(""),
						 orgIds = new StringBuffer(""),
						 userNames = new StringBuffer("");
			
			String[] userNameOrgCodeArray = userNameOrgCodes.split(";"),//???????????????id?????????id??????
					 nameCodeArray = new String[]{},
					 regisValueArray = new String[]{};
			String userIdsStr = "",
				   regisValues = "",
				   userOrgCode = "";
			OrgSocialInfoBO orgInfo = null;
			UserBO user = null;
			
			for(String userNameOrgCode : userNameOrgCodeArray) {
				if(StringUtils.isNotBlank(userNameOrgCode)) {
					nameCodeArray = userNameOrgCode.split(":");//???????????????????????????????????????
					if(nameCodeArray.length == 2) {
						userOrgCode = nameCodeArray[0];
						regisValues = nameCodeArray[1];
						
						orgInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(userOrgCode);
						if(orgInfo != null) {
							if(StringUtils.isNotBlank(regisValues)) {
								regisValueArray = regisValues.split(",");
								for(String regisValue : regisValueArray) {
									if(StringUtils.isNotBlank(regisValue)) {
										user = userManageService.getUserInfoByRegistValue(regisValue);
										if(user != null) {
											userIds.append(",").append(user.getUserId());//userId???orgId??????????????????
											orgIds.append(",").append(orgInfo.getOrgId());
											userNames.append(",").append(user.getPartyName());
										}
									}
								}
							}
						}
					}
				}
			}
			
			if(userIds.length() > 0) {
				userIdsStr = userIds.substring(1);
				
				resultMap.put("userIds", userIdsStr);
				resultMap.put("userNames", userNames.substring(1));
				resultMap.put("orgIds", orgIds.substring(1));
			}
		}
		
		return resultMap;
	}
	
	/**
	 * ??????????????????????????????
	 * @param orgInfoList	??????????????????????????????????????????
	 * @param nodes			???????????????????????????
	 * @param params
	 * 			nodeId		??????????????????id
	 * @param removeOrgId	?????????????????????
	 * @return
	 */
	protected Map<Long, OrgSocialInfoBO> findDeptByFilterProffession(List<OrgSocialInfoBO> orgInfoList, List<GdZTreeNode> nodes, Map<String, Object> params, Long removeOrgId) {
		Long userOrgId = null;
		List<OrgSocialInfoBO> socialInfoBOs = null;
		Integer orgType = null;
		Map<Long, OrgSocialInfoBO> socialInfoMap = new HashMap<Long, OrgSocialInfoBO>();
		
		if(orgInfoList != null && orgInfoList.size() > 0) {
			String[] reserveCodes = null;
			
			String reserveProfessionCode = "";
			
			if(CommonFunctions.isNotBlank(params, "nodeId")) {//???????????????????????????????????????
				Integer nodeId = 0;
				
				try {
					nodeId = Integer.valueOf(params.get("nodeId").toString());
				} catch(NumberFormatException e) {
					nodeId = 0;
					throw new NumberFormatException("??????[nodeId]???"+params.get("nodeId")+" ???????????????Integer??????");
				}
				
				if(nodeId != 0) {
					Node node = eventDisposalWorkflowService.findNodeById(nodeId);
					if(node != null) {
						reserveProfessionCode = node.getProfessionCode();
					}
				}
			}
			
			if(StringUtils.isNotBlank(reserveProfessionCode)) {
				reserveCodes = reserveProfessionCode.split(",");
			}
				
			for(OrgSocialInfoBO orgSocialInfo : orgInfoList) {
				userOrgId = orgSocialInfo.getOrgId();
				
				orgType = ORG_TYPE_DEPARTMENT;
				
				socialInfoBOs = orgSocialInfoService.findUnitBySame(userOrgId, false, orgType);
				
				for (OrgSocialInfoBO socialOrg : socialInfoBOs) {
					if(removeOrgId != null && removeOrgId > 0 && removeOrgId.equals(socialOrg.getOrgId())) {
						//do nothing
					} else {
						if(reserveCodes != null) {
							for(String reserveCode : reserveCodes) {
								if(StringUtils.isNotBlank(reserveCode) && reserveCode.equals(socialOrg.getProfessionCode())) {
									socialInfoMap.put(socialOrg.getOrgId(), socialOrg);
									break;
								}
							}
						} else {
							socialInfoMap.put(socialOrg.getOrgId(), socialOrg);
						}
					}
				}
			}
			
			if(nodes != null) {
				Set<Long> socialInfoIdSet = socialInfoMap.keySet();
				OrgSocialInfoBO socialOrg = null;
				GdZTreeNode node = null;
				Integer level = -1;
				
				if(CommonFunctions.isNotBlank(params, "level")) {
					try {
						level = Integer.valueOf(params.get("level").toString());
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
				}
				
				for(Long socialInfoId : socialInfoIdSet) {//??????????????????????????????????????????
					socialOrg = socialInfoMap.get(socialInfoId);
					if (!eventOrgInfoService.isGovernment(socialOrg.getProfessionCode())) {
						socialOrg.setChiefLevel(String.valueOf(level + 1));
						
						node = CommonFunctions.transSocialOrgToZTreeNodeA(socialOrg, true);
						node.setClickable(true);
						
						nodes.add(node);
					}
				}
			}
		}
		
		return socialInfoMap;
	}
}
