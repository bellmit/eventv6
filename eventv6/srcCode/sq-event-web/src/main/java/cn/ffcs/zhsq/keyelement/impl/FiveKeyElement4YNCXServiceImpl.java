package cn.ffcs.zhsq.keyelement.impl;

import java.util.ArrayList;
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
 * @Description: 云南省->楚雄彝族自治州->楚雄市 五要素实现类
 * @ClassName:   FiveKeyElement4YNCXServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年7月5日 上午9:01:27
 */
@Service(value = "fiveKeyElement4YNCXService")
public class FiveKeyElement4YNCXServiceImpl extends FiveKeyElementForEventServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		INodeCodeHandler nodeCodeHandler = null;
		Map<String, Object> resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		
		if(StringUtils.isNotBlank(nodeCode)) {
			nodeCodeHandler = createNodeCodeHandle(nodeCode);
		}
		
		if(nodeCodeHandler != null && nodeCodeHandler.isEquality()) {
			resultMap.put("isSelectUser", false);
			resultMap.put("isSelectOrg", true);
		}
		
		return resultMap;
	}
	
	@Override
	public List<GdZTreeNode> getTreeForEvent(
			UserInfo userInfo, Long orgId,
			String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception {
		INodeCodeHandler nodeCodeHandler = createNodeCodeHandle(nodeCode);
		List<GdZTreeNode> treeNodeList = new ArrayList<GdZTreeNode>();
		
		if(nodeCodeHandler.isEquality()) {
			Long userOrgId = userInfo.getOrgId();
			OrgSocialInfoBO userOrgInfo = orgSocialInfoService.findByOrgId(userOrgId);
			
			if(userOrgInfo != null && userOrgInfo.getOrgId() != null) {
				GdZTreeNode treeNode = CommonFunctions.transSocialOrgToZTreeNodeA(userOrgInfo, true);
				treeNode.setClickable(true);
				
				treeNodeList.add(treeNode);
			}
		} else {
			treeNodeList = super.getTreeForEvent(userInfo, orgId, nodeCode, level, professionCode, params);
		}
		
		return treeNodeList;
	}
}
