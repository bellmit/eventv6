package cn.ffcs.zhsq.keyelement.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 江阴市(JYS) 事件人员选择
 * @author zhangls
 *
 */
@Service(value = "fiveKeyElement4JYSService")
public class FiveKeyElement4JYSServiceImpl extends
		FiveKeyElementForEventServiceImpl {
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeIdStr, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<Long, UserBO> userBOMap = new HashMap<Long, UserBO>();
		resultMap.put("msg", "");
		INodeCodeHandler nodeCodeHandler = null;
		
		if(StringUtils.isNotBlank(nodeCode)) {
			try {
				nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
				resultMap.put("eventNodeCode", nodeCodeHandler);
				
				if(nodeCodeHandler.isReport() || (nodeCodeHandler.isFromDept() && nodeCodeHandler.isSplitFlow())) {//上报、部门分流给街道
					if(StringUtils.isNotBlank(nodeIdStr)) {
						Integer nodeId = Integer.valueOf(nodeIdStr);
						
						if(nodeId > 0) {
							List<Map<String, Object>> nodeActorsMap = eventDisposalWorkflowService.findNodeActorsById(Integer.valueOf(nodeId));
							if(nodeActorsMap != null && nodeActorsMap.size() > 0) {
								List<GdZTreeNode> treeNodeList = this.getTreeForEvent(userInfo, null, nodeCode, null, null, params);
								
								if(treeNodeList != null) {
									for(GdZTreeNode treeNode : treeNodeList) {
										Long roleId = -1L, positionId = -1L, orgId = Long.valueOf(treeNode.getId());
										List<UserBO> userBOList = null;
										
										for(Map<String, Object> nodeActor : nodeActorsMap) {
											if(CommonFunctions.isNotBlank(nodeActor, "ROLE_ID")) {
												roleId = Long.valueOf(nodeActor.get("ROLE_ID").toString());
												userBOList = userManageService.getUserListByUserExampleParamsOut(roleId, null, orgId);//roleId positionId orgId
											} else if(CommonFunctions.isNotBlank(nodeActor, "POSITION_ID")) {
												positionId = Long.valueOf(nodeActor.get("POSITION_ID").toString());
												userBOList = userManageService.getUserListByUserExampleParamsOut(null, positionId, orgId);//roleId positionId orgId
											}
											
											if(userBOList != null) {
												for(UserBO userBO : userBOList) {//去重配置了多个角色的相同人员
													userBOMap.put(userBO.getUserId(), userBO);
												}
											}
										}
									}
								}
							}
						}
						
					}
				}
			} catch (Exception e) {
				resultMap.put("msg", e.getMessage());
			}
		}
		
		if(!userBOMap.isEmpty()) {
			UserBO userBO = null;
			StringBuffer userIds = new StringBuffer(""),
						 userNames = new StringBuffer(""),
						 orgIds = new StringBuffer("");
			
			for(Long userId : userBOMap.keySet()) {
				userBO = userBOMap.get(userId);
				
				userIds.append(",").append(userId);
				userNames.append(",").append(userBO.getPartyName());
				orgIds.append(",").append(userBO.getSocialOrgId());
			}
			
			resultMap.put("userIds", userIds.substring(1));
			resultMap.put("userNames", userNames.substring(1));
			resultMap.put("orgIds", orgIds.substring(1));
			
			//办理页面是否只展示可办理人员信息 true为是，false为否
			resultMap.put("isDisplayUser", true);
			//办理页面是否显示人员选择 true为是，false为否
			resultMap.put("isSelectUser", false);
			//办理页面是否显示组织选中 true为是，false为否
			resultMap.put("isSelectOrg", false);
		} else {
			resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeIdStr, params);
		}
		
		return resultMap;
	}
}
