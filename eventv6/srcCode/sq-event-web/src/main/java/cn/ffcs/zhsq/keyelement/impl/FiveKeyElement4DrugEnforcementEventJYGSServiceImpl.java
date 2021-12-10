package cn.ffcs.zhsq.keyelement.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 嘉峪关市(JYGS) 禁毒事件五要素实现类
 * @author zhangls
 *
 */
@Service(value = "fiveKeyElement4DrugEnforcementEventJYGSService")
public class FiveKeyElement4DrugEnforcementEventJYGSServiceImpl extends
		FiveKeyElement4DrugEnforcementEventServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Override
	public boolean isUserIdGridAdmin(Long orgId, Long userId) {
		boolean isGridAdmin = false;
		
		if(orgId != null && orgId > 0) {
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
			if(orgInfo != null) {
				String orgChiefLevel = orgInfo.getChiefLevel(),
					   orgType = orgInfo.getOrgType();
				
				isGridAdmin = ConstantValue.GRID_ORG_LEVEL.equals(orgChiefLevel) && String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(orgType);
			}
		}
		
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
			//当下派给网格、社区时，只有末端节点可以点击
			boolean isSend = nodeCodeHandler.isSend(),				//是否下派
					isToUnit = nodeCodeHandler.isToUnit(),			//是否到单位
					isFromDept = nodeCodeHandler.isFromDept(),		//是否来自部门
					isToGrid = nodeCodeHandler.isToGrid(),			//是否到网格
					isToCommunity = nodeCodeHandler.isToCommunity(),//是否到社区
					isClickable = true;								//节点是否可点击
			
			if(isSend && isFromDept && isToUnit && (isToGrid || isToCommunity)) {
				int lineLevel = nodeCodeHandler.getLineLevel(),
					gridLevel = 0;									//节点层级
				String gridLevelStr = "";
						
				for(GdZTreeNode node : nodes) {
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
					
					isClickable = lineLevel == gridLevel;
					
					node.setClickable(isClickable);
				}
			} else {
				super.addClickableAttr2Node(nodeCodeHandler, nodes, isFromCommunityGridAdmin);
			}
		}
	}
}
