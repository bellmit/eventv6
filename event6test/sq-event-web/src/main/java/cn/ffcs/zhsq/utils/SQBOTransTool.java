/**
 * 
 */
package cn.ffcs.zhsq.utils;

import cn.ffcs.common.GdZTreeNode;
import cn.ffcs.common.TreeNode;
import cn.ffcs.shequ.mybatis.domain.workflow.conf.WorkflowEvent;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResType;
import cn.ffcs.system.publicUtil.ZTreeNode;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;

/**
 * 社区BO转换工具
 * @author guohh
 *
 */
public class SQBOTransTool {

	/**
	 * 转换OrgEntityInfoBO成树节点
	 * @param orgEntity
	 * @return
	 */
	public static TreeNode transOrgEntityToTreeNode(OrgEntityInfoBO orgEntity) {
		TreeNode node = new TreeNode();
		node.setExpanded(false);
		node.setHasChildren(!orgEntity.isLeaf());
		node.setId(String.valueOf(orgEntity.getOrgId()));
		node.setPid(String.valueOf(orgEntity.getParentOrgId()));
		node.setText(orgEntity.getOrgName());
		return node;
	}
	
	/**
	 * 转换OrgEntityInfoBO成树节点
	 * @param orgEntity
	 * @return
	 */
	public static ZTreeNode transOrgEntityToZTreeNode(OrgEntityInfoBO orgEntity) {
		ZTreeNode node = new ZTreeNode();
		node.setOpen(false);
		node.setIsParent(!orgEntity.isLeaf());
		node.setId(String.valueOf(orgEntity.getOrgId()));
		node.setPId(String.valueOf(orgEntity.getParentOrgId()));
		node.setName(orgEntity.getOrgName());
		if(orgEntity.getIcon()!=null && orgEntity.getIcon().length()>0)
			node.setIcon(orgEntity.getIcon());
		node.setLayerType(orgEntity.getOrgType());
		node.setOrgCode(orgEntity.getOrgCode());
		return node;
	}
	
	/**
	 * 转换OrgSocialInfoBO成树节点
	 * @param socialOrg
	 * @return
	 */
	public static TreeNode transSocialOrgToTreeNode(OrgSocialInfoBO socialOrg) {
		TreeNode node = new TreeNode();
		node.setExpanded(false);
		node.setHasChildren(!socialOrg.isLeaf());
		node.setId(String.valueOf(socialOrg.getOrgId()));
		node.setPid(String.valueOf(socialOrg.getParentOrgId()));
		node.setText(socialOrg.getOrgName());
		return node;
	}
	
	/**
	 * 转换OrgSocialInfoBO成树节点
	 * @param socialOrg
	 * @return
	 */
	public static ZTreeNode transSocialOrgToZTreeNode(OrgSocialInfoBO socialOrg) {
		ZTreeNode node = new ZTreeNode();
		node.setOpen(false);
		node.setIsParent(!socialOrg.isLeaf());
		node.setId(String.valueOf(socialOrg.getOrgId()));
		node.setPId(String.valueOf(socialOrg.getParentOrgId()));
		node.setName(socialOrg.getOrgName());
		if(socialOrg.getIcon()!=null && socialOrg.getIcon().length()>0)
			node.setIcon(socialOrg.getIcon());
		node.setLayerType(socialOrg.getOrgType());
		node.setOrgCode(socialOrg.getOrgCode());
		return node;
	}
	
	/**
	 * 转换MixedGridInfo成树节点
	 * @param gridInfo
	 * @return
	 */
	public static GdZTreeNode transMixedGridInfoToGdZTreeNode(MixedGridInfo gridInfo, boolean withCode) {
		GdZTreeNode node = new GdZTreeNode();
		node.setOpen(false);
		node.setIsParent(!gridInfo.getIsLeaf());
		node.setId(String.valueOf(gridInfo.getGridId()));
		node.setPId(String.valueOf(gridInfo.getParentGridId()));
		node.setName(gridInfo.getGridName()+(withCode?(" - "+gridInfo.getGridCode()):""));
		//node.setName((withCode?(gridInfo.getGridCode() + " - "):"")+ gridInfo.getGridName());
		node.setOrgCode(gridInfo.getInfoOrgCode());
		node.setOrgId(gridInfo.getInfoOrgId());
		node.setGridPhoto(gridInfo.getGridPhoto());
		node.setLevel(String.valueOf(gridInfo.getGridLevel()));
		node.setGridLevel(String.valueOf(gridInfo.getGridLevel()));
		node.setGridId(gridInfo.getGridId());
		node.setGridCode(gridInfo.getGridCode());
		return node;
	}
	
	/**
	 * 转换WorkflowEvent成树节点
	 * @param workflowEvent
	 * @return
	 */
	public static GdZTreeNode transWorkflowEventToGdZTreeNode(WorkflowEvent workflowEvent) {
		GdZTreeNode node = new GdZTreeNode();
		node.setOpen(false);
		//node.setIsParent(!gridInfo.getIsLeaf());
		node.setId(String.valueOf(workflowEvent.getFlowEventId()));
		//node.setPId(String.valueOf(gridInfo.getParentGridId()));
		node.setName(workflowEvent.getFlowEventName());		
		return node;
	}
	
	/**
	 * 转换ResType成树节点
	 * @param resType
	 * @return
	 */
	public static GdZTreeNode transResTypeToGdZTreeNode(ResType resType) {
		GdZTreeNode node = new GdZTreeNode();
		node.setOpen(false);
		node.setId(String.valueOf(resType.getResTypeId()));
		node.setPId(String.valueOf(resType.getParentTypeId()));
		node.setName(resType.getTypeCode() + " - "+ resType.getName());
		node.setTypeCode(resType.getTypeCode());
		String targetUrl = (resType.getResCtrlHost()==null?"":resType.getResCtrlHost());
		targetUrl += (targetUrl.length()>0?resType.getResCtrlUrl():"");
		node.setTargetUrl(targetUrl);
		return node;
	}
}
