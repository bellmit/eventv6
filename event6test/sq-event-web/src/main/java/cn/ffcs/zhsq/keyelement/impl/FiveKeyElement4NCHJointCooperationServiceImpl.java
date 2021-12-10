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
 * 江西省南昌市(NCH)大联动(JointCooperation) 五要素实现类
 * @author zhangls
 *
 */
@Service(value = "fiveKeyElement4NCHJointCooperationService")
public class FiveKeyElement4NCHJointCooperationServiceImpl extends
		FiveKeyElementForEventServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	private String JOINT_HANDLE_NODE = "task14",//联合办理
				   JOINT_OPERATE_NODE = "task15";//联席交办
	
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isShowInterval = false;
		
		if(StringUtils.isNotBlank(nodeCode)) {
			INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
			
			isShowInterval = nodeCodeHandler.isFromUnit() && (nodeCodeHandler.isSend() || (nodeCodeHandler.isSplitFlow() && nodeCodeHandler.getLineLevel() == 0));
		}
		
		resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		
		//true 为是联合办理
		resultMap.put("isJointHandled", JOINT_HANDLE_NODE.equals(nodeName));
		//true 为是联席交办
		resultMap.put("isJointOperated", JOINT_OPERATE_NODE.equals(nodeName));
		//true 为展示时限字段
		resultMap.put("isShowInterval", isShowInterval);
		
		return resultMap;
	}
	
	@Override
	public List<GdZTreeNode> getTreeForEvent(
			UserInfo userInfo, Long orgId,
			String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception {
		List<GdZTreeNode> nodes = new ArrayList<GdZTreeNode>();
		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		int lineLevel = nodeCodeHandler.getLineLevel();
		List<OrgSocialInfoBO> socialInfoBOs = null;
		level = level == null ? 0 : level;
		
		//单位分流给部门
		if(lineLevel > 0 && nodeCodeHandler.isFromUnit() && nodeCodeHandler.isSplitFlow()) {
			OrgSocialInfoBO orgInfo = null;
			
			if(orgId == null || orgId < 0) {
				if(CommonFunctions.isNotBlank(params, "orgRootId")) {
					orgId = Long.valueOf(params.get("orgRootId").toString());
				} else if(userInfo != null) {
					orgId = userInfo.getOrgId();
				}
			}
			
			if(level == 0) {//设置根节点
				orgInfo = orgSocialInfoService.findByOrgId(orgId);
				
				if(orgInfo != null) {
					orgInfo.setChiefLevel(String.valueOf(level + 1));//构造组织树层级
					GdZTreeNode rootNode = CommonFunctions.transSocialOrgToZTreeNodeA(orgInfo, true);
					rootNode.setIsParent(true);
					nodes.add(rootNode);
				}
			} else if(level < lineLevel) {//查找父级节点下的所有下级组织信息
				socialInfoBOs = orgSocialInfoService.findOrgSocialListByParentIdAndProId(orgId, null);
				
				if(socialInfoBOs != null) {
					for (OrgSocialInfoBO socialOrg : socialInfoBOs) {
						socialOrg.setChiefLevel(String.valueOf(level + 1));//构造组织树层级
						nodes.add(CommonFunctions.transSocialOrgToZTreeNodeA(socialOrg, socialOrg.isLeaf()));
					}
				}
			} else {
				orgInfo = orgSocialInfoService.findByOrgId(orgId);
				
				if(orgInfo != null) {
					socialInfoBOs = new ArrayList<OrgSocialInfoBO>();
					socialInfoBOs.add(orgInfo);
					
					//查找同级职能部门
					findDeptByFilterProffession(socialInfoBOs, nodes, params, userInfo.getOrgId());
				}
			}
			
			if(nodes != null) {
				for(GdZTreeNode node : nodes) {
					node.setClickable(true);
				}
			}
		} else {
			nodes = super.getTreeForEvent(userInfo, orgId, nodeCode, level, professionCode, params);
		}
		
		return nodes;
	}
}
