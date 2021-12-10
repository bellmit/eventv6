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
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 江西省吉安市吉州区(Ji ZHou Qu) 人员选择处理类，被替换为FiveKeyElement4JiZHouQuServiceImpl
 * @ClassName:   FiveKeyElement4JZHQServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年7月27日 上午10:07:07
 */
@Service(value = "fiveKeyElement4JZHQService")
@Deprecated
public class FiveKeyElement4JZHQServiceImpl extends FiveKeyElementForEventServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
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
			resultMap.put("isSelectOrg", true);
			resultMap.put("eventNodeCode", nodeCodeHandler);
		} else {
			resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		}
		
		resultMap = resultMap == null ? new HashMap<String, Object>() : resultMap;
		
		resultMap.put("isUploadHandledPic", true);
		
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
			
			if(treeNodeList != null && treeNodeList.size() > 0) {
	    		if(userInfo != null 
	    				&& nodeCodeHandler.isFromDept() 
	    				&& nodeCodeHandler.isToDept() 
	    				&& (nodeCodeHandler.isReport()
	    						|| (nodeCodeHandler.isSend() 
	    								&& level != null 
	    								&& level == nodeCodeHandler.getLineLevel()))) {
	    			Long userOrgId = userInfo.getOrgId();
	    			String reserveProfessionCode = null;
	    			
	    			if(userOrgId != null && userOrgId > 0) {
	    				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userOrgId);
	    				
	    				if(orgInfo != null) {
	    					reserveProfessionCode = orgInfo.getProfessionCode();
	    				}
	    			}
	    			
	    			if(StringUtils.isNotBlank(reserveProfessionCode)) {
	    				List<GdZTreeNode> reserveTreeNodeList = new ArrayList<GdZTreeNode>();
	    				
	    				for(GdZTreeNode treeNode : treeNodeList) {
	    					if(reserveProfessionCode.equals(treeNode.getProfessionCode())) {
	    						reserveTreeNodeList.add(treeNode);
	    					}
	    				}
	    				
	    				treeNodeList = reserveTreeNodeList;
	    			}
	    		}
	    	}
		}
		
		return treeNodeList;
	}
	
	@Override
	public boolean isUserIdGridAdmin(Long orgId, Long userId) {
		boolean isGridAdmin = false;
		OrgSocialInfoBO orgInfo = null;
		String chiefLevel = null;
		
		if(orgId != null && orgId > 0) {
			orgInfo = orgSocialInfoService.findByOrgId(orgId);
			
			if(orgInfo != null) {
				chiefLevel = orgInfo.getChiefLevel();
			}
		}
		
		isGridAdmin = ConstantValue.GRID_ORG_LEVEL.equals(chiefLevel);
		
		return isGridAdmin;
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
