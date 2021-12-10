package cn.ffcs.zhsq.keyelement.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 重庆万州区(WanZHouQu) 人员选择接口
 * @ClassName:   FiveKeyElement4WZHQServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年4月23日 上午10:36:45
 */
@Service(value = "fiveKeyElement4WZHQService")
public class FiveKeyElement4WZHQServiceImpl extends FiveKeyElementForEventServiceImpl {
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		INodeCodeHandler nodeCodeHandler = null;
		
		if(StringUtils.isNotBlank(nodeCode)) {
			nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		}
		
		if(nodeCodeHandler != null && nodeCodeHandler.isEquality()) {
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
		} else if(nodeCodeHandler != null && nodeCodeHandler.isGlobal()) {
			resultMap.put("isSelectUser", false);
			resultMap.put("isSelectOrg", true);
			resultMap.put("eventNodeCode", nodeCodeHandler);
		} else {
			resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		}
		
		return resultMap;
	}
	
	@Override
	public List<GdZTreeNode> getTreeForEvent(
			UserInfo userInfo, Long orgId,
			String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception {
		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		params = params == null ? new HashMap<String, Object>() : params;
		
		if(nodeCodeHandler != null && nodeCodeHandler.isGlobal() && CommonFunctions.isBlank(params, "orgRootId")) {
			params.put("orgRootId", userInfo.getOrgId());
		}
		
		return super.getTreeForEvent(userInfo, orgId, nodeCode, level, professionCode, params);
	}
}
