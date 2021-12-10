package cn.ffcs.zhsq.keyelement.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.api.enums.BizPlatfromEnum;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.intermediateData.eventVerify.service.IEventVerifyBaseService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 雄安新区容东县(RongDongXian) 人员选择接口
 * @ClassName:   FiveKeyElement4RDXServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年5月15日 上午11:10:20
 */
@Service(value = "fiveKeyElement4RDXService")
public class FiveKeyElement4RDXServiceImpl extends FiveKeyElementForEventServiceImpl {
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventVerifyBaseService eventVerifyBaseService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		Long eventId = null;
		boolean isShowPointConfirm = false;
		String END_NODE_NAME = "end1";	//归档节点名称
		
		if(CommonFunctions.isNotBlank(params, "isShowPointConfirm")) {
			isShowPointConfirm = Boolean.valueOf(params.get("isShowPointConfirm").toString());
		} else if(END_NODE_NAME.equals(nodeName)) {//只有下一节点为归档时，才进行积分确认是否展示的设置
			if(CommonFunctions.isNotBlank(params, "eventId")) {
				try {
					eventId = Long.valueOf(params.get("eventId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			if(eventId != null && eventId > 0) {
				Map<String, Object> verifyParam = new HashMap<String, Object>();
				int verifyCount = 0;
				String STATUS_REPORT = "02";//已上报
				
				verifyParam.put("userOrgCode", userInfo.getOrgCode());
				verifyParam.put("eventId", eventId);
				verifyParam.put("status", STATUS_REPORT);
				verifyParam.put("bizPlatform", BizPlatfromEnum.XA_XCX.getValue());
				
				verifyCount = eventVerifyBaseService.findEventVerifyCount(verifyParam);
				
				isShowPointConfirm = verifyCount > 0;
			}
			
			//是否展示积分确认
			resultMap.put("isShowPointConfirm", isShowPointConfirm);
		}
		
		return resultMap;
	}
	
	@Override
	public List<UserInfo> getUserInfoList(Long orgId, String nodeCode, Long removeUserId, Map<String, Object> extraParam) throws Exception {
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		if (nodeCode != null) {
			List<UserInfo> users = new ArrayList<UserInfo>();//获取组织下的所有用户
			List<Map<String, Object>> nodeActorsMap = null;
			List<UserBO> userBOList = new ArrayList<UserBO>();
			String partyName = "", userOrgName = null;
			boolean isActor4Org = false;//判断指定节点是否为组织配置，可以使用这种方法的前提是，工作流节点中组织、角色、职位、人员的配置只能选择其中一个
			
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
				
				if(nodeId > 0) {
					nodeActorsMap = eventDisposalWorkflowService.findNodeActorsById(Integer.valueOf(nodeId));
				}
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
					
					userBOList.clear();//清除原有的人员信息
					
					if(!userBOMap.isEmpty()) {
						for(Long userId : userBOMap.keySet()) {
							userBOList.add(userBOMap.get(userId));
						}
					}
				} else {
					String msgWrong = "该环节缺少如下配置的人员，";
					if(orgName.length() > 0) {
						msgWrong += "组织名称为 [" + orgName.substring(1) + "] ";
					}
					if(roleName.length() > 0) {
						msgWrong += "角色名称为 [" + roleName.substring(1) + "] ";
					}
					if(positionName.length() > 0) {
						msgWrong += "职位名称为 [" + positionName.substring(1) + "] ";
					}
					
					throw new Exception(msgWrong);
				}
			} else {
				/**
				 * orgId		组织id
				 * type			type为1 则可以查询到当前组织以及下级所有组织的用户； 其他值 则只能查询当前组织上的用户
				 * partyName	姓名  模糊查询
				 * userType 	用户类型  用户类型 001网站群注册，002后台注册  003超级管理员 004开发人员 005cpsp用户 
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
				
				if (users != null && users.size() > 0) {
					userInfos = users;
				}
			}
		} else {
			throw new Exception("环节编码不能为空！");
		}
		
		return userInfos;
	}
	
	@Override
	public boolean isUserIdGridAdmin(Long orgId, Long userId) {
		return false;
	}
	
	/**
	 * 设置节点的clickable属性
	 * @param nodeCodeHandler
	 * @param nodes
	 * @param isFromCommunityGridAdmin
	 */
	protected void addClickableAttr2Node(INodeCodeHandler nodeCodeHandler, List<GdZTreeNode> nodes, boolean isFromCommunityGridAdmin) {
		if(nodes != null) {
			//当为上报操作时，均可点击，因为上级树只会展示一级
			//当下派或者分立给职能部门时，可点击的只有末端的职能部门
			boolean isReport = nodeCodeHandler.isReport(),		//是否上报
					isSplitFlow = nodeCodeHandler.isSplitFlow(),//是否分流
					isSplitFlowGlobal = nodeCodeHandler.isSplitFlowGlobal(),//是否分流给所有职能部门
					isSend = nodeCodeHandler.isSend(),			//是否下派
					isToDept = nodeCodeHandler.isToDept(),		//是否到部门
					isGlobal = nodeCodeHandler.isGlobal(),		//sq_event_web
					isClickable = true;							//节点是否可点击
			
			int lineLevel = nodeCodeHandler.getLineLevel(),
				gridLevel = 0;									//节点层级
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
					
					if(isToDept) {//目标为职能部门办理
						isClickable = gridLevel == (lineLevel + 1);
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
}
