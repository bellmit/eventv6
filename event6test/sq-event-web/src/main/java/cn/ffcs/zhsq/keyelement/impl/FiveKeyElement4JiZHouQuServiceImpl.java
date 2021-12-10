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
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * @Description: 江西省吉安市吉州区(Ji ZHou Qu) 人员选择处理类
 * @ClassName:   FiveKeyElement4JiZHouQuServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年8月24日 下午3:07:11
 */
@Service(value = "fiveKeyElement4JiZHouQuService")
public class FiveKeyElement4JiZHouQuServiceImpl extends FiveKeyElement4JXPlatformServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	private static final String COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE = "task22";//八员队伍办理
	
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
		
		if(COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE.equals(nodeName)) {
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
		List<GdZTreeNode> treeNodeList = new ArrayList<GdZTreeNode>();
		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		
		if(nodeCodeHandler.isEquality()) {
			OrgSocialInfoBO socialOrg = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
			GdZTreeNode treeNode = CommonFunctions.transSocialOrgToZTreeNodeA(socialOrg, true);
			
			treeNode.setClickable(true);
			treeNodeList.add(treeNode);
		} else {
			treeNodeList = super.getTreeForEvent(userInfo, orgId, nodeCode, level, professionCode, params);
		}
		
		return treeNodeList;
	}
	
	protected Map<String, String> init4TaskUserConstruct() {
		Map<String, String> initMap = super.init4TaskUserConstruct();
		
		initMap.put(COMMUNITY_EIGHT_MEMBER_TEAM_NODE_CODE, "2");
		
		return initMap;
	}
}
