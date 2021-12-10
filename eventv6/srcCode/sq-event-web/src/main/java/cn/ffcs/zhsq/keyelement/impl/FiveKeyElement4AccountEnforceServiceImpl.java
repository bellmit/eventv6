package cn.ffcs.zhsq.keyelement.impl;

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
import cn.ffcs.uam.service.UserInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 新疆纪委扶贫问题人员选择相关
 * @author zhangls
 *
 */
@Service(value = "fiveKeyElement4AccountEnforceService")
public class FiveKeyElement4AccountEnforceServiceImpl extends
		FiveKeyElementForEventServiceImpl {
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserInfoOutService userInfoService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	//领导审核环节节点编码
	private static final String LEADER_AUDIT_NODE_CODE = "task11";			
	
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		INodeCodeHandler nodeCodeHandler = null;
		Long orgId = -1L;
		
		if(LEADER_AUDIT_NODE_CODE.equals(nodeName)) {
			Long instanceId = -1L;
			
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				instanceId = Long.valueOf(params.get("instanceId").toString());
				ProInstance pro = baseWorkflowService.findProByInstanceId(instanceId);
				
				if(pro != null) {
					Long create0rgId = pro.getOrgId();
					
					if(create0rgId != null && create0rgId > 0) {
						OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(create0rgId);
						
						if(orgInfo != null) {
							orgId = orgInfo.getOrgId();
						}
					}
				}
			}
		} else if(StringUtils.isNotBlank(nodeCode)) {
			nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
			
			if(nodeCodeHandler != null && nodeCodeHandler.isEquality()) {
				orgId = userInfo.getOrgId();
			}
		}
		
		if(orgId > 0) {
			List<Map<String, Object>> nodeActorsMap = this.eventDisposalWorkflowService.findNodeActorsById(Integer.valueOf(nodeId));
			
			if(nodeActorsMap != null) {
				Long positionId = -1L;
				List<Long> positionIdList = new ArrayList<Long>();
				
				for(Map<String, Object> nodeActorMap : nodeActorsMap) {
					if(CommonFunctions.isNotBlank(nodeActorMap, "POSITION_ID")) {
						try {
							positionId = Long.valueOf(nodeActorMap.get("POSITION_ID").toString());
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
						
						if(positionId > 0) {
							positionIdList.add(positionId);
						}
					}
				}
				
				if(positionIdList.size() > 0) {
					List<Long> orgIdList = new ArrayList<Long>();
					
					orgIdList.add(orgId);
					
					List<UserInfo> userInfoList = userInfoService.findUserListByOrgListAndPositonList(orgIdList, positionIdList);
					
					if(userInfoList != null && userInfoList.size() > 0) {
						StringBuffer userIds = new StringBuffer(""),
									 userNames = new StringBuffer(""),
									 orgIds = new StringBuffer("");
						
						for(UserInfo user : userInfoList) {
							userIds.append(",").append(user.getUserId());
							userNames.append(",").append(user.getPartyName());
							orgIds.append(",").append(user.getOrgId());
						}
						
						if(userIds.length() > 0) {
							resultMap.put("userIds", userIds.substring(1).toString());
							resultMap.put("userNames", userNames.substring(1).toString());
							resultMap.put("orgIds", orgIds.substring(1).toString());
						}
					}
				}
			}
			
			//办理页面是否显示人员选择 true为是，false为否
			resultMap.put("isSelectUser", true);
			//办理页面是否显示组织选中 true为是，false为否
			resultMap.put("isSelectOrg", false);
			
		} else {
			resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		}
		
		return resultMap;
	}
}
