package cn.ffcs.zhsq.keyelement.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ffcs.workflow.om.Node;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 南昌市(NCH)扫黑除恶(Sweep Black Remove Evil)线索管理(Clue)
 * @author zhangls
 *
 */
@Service(value = "fiveKeyElement4NCHSBREClueService")
public class FiveKeyElement4NCHSBREClueServiceImpl extends
		FiveKeyElement4NCHJointCooperationServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	//线索核查节点名称
	private final String CLUE_VERIFICATION_NODE_NAME = "task3";
	
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
		
		//当前办理人员环节扭转
		if(nodeCodeHandler != null && nodeCodeHandler.isEquality()) {
			resultMap.put("userIds", userInfo.getUserId());
			resultMap.put("userNames", userInfo.getPartyName());
			resultMap.put("orgIds", userInfo.getOrgId());
			//办理页面是否显示人员型人员选择 true为是，false为否
			resultMap.put("isSelectUser", true);
			//办理页面是否显示组织型人员选择 true为是，false为否
			resultMap.put("isSelectOrg", false);
		} else if(nodeCodeHandler != null && nodeCodeHandler.isToBegin()) {//发起人进行办理
			Long instanceId = -1L;
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			}
			
			if(instanceId > 0) {
				ProInstance pro = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
				if(pro != null) {
					resultMap.put("userIds", pro.getUserId());
					resultMap.put("userNames", pro.getUserName());
					resultMap.put("orgIds", pro.getOrgId());
				}
			}
			
			//办理页面是否显示人员型人员选择 true为是，false为否
			resultMap.put("isSelectUser", true);
			//办理页面是否显示组织型人员选择 true为是，false为否
			resultMap.put("isSelectOrg", false);
		} else {
			resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
			
			//true 则展示办理组织选择
			resultMap.put("isShowAssistOrg", CLUE_VERIFICATION_NODE_NAME.equals(nodeName));
		}
		
		return resultMap;
	}
	
	@Override
	public List<GdZTreeNode> getTreeForEvent(
			UserInfo userInfo, Long orgId,
			String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception {
		List<GdZTreeNode> nodes = new ArrayList<GdZTreeNode>();
		INodeCodeHandler nodeCodeHandler = null;

		if(StringUtils.isNotBlank(nodeCode)) {
			nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
			if(nodeCodeHandler != null && nodeCodeHandler.isSplitFlow()) {
				level = level == null ? 0 : level;
				
				Long userOrgId = userInfo.getOrgId();
				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userOrgId);
				
				if(orgInfo != null) {
					String orgType = orgInfo.getOrgType(),
						   chiefLevel = orgInfo.getChiefLevel();
					
					//只有根节点为职能部门时，才需要将其调整为对应的父级组织
					if(level == 0 && String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType)) {
						orgId = orgInfo.getParentOrgId();//将根节点替换为职能部门的上级，只有旧组织树才可以这样使用
					}
					
					//由于不论当前办理人员所属组织是单位还是部门，看到的效果都与单位的一致，故将起始操作编码固定为单位
					//获取本级及下一级的职能部门，因此将线层级设置为定值2，而不再动态调整
					nodeCode = INodeCodeHandler.ORG_UINT + chiefLevel + INodeCodeHandler.OPERATE_FLOW + "2" + nodeCode.substring(nodeCode.length() - 2);
				}
			}
		}

		if (StringUtils.isNotBlank(nodeCode)) {
			String nodeName = "",
					CLUEFLOW_NODENAME = "task2",
					CLUECHECK_NODENAME = "task3";

			//分流时（线索流转、线索核查）过滤出指定专业的部门(nodeCode有发生变化，故重新解析)，若有新节点增加人员组织过滤，要合并代码
			nodeCodeHandler = this.createNodeCodeHandle(nodeCode);

			//根据nodeId获取节点信息
			if (CommonFunctions.isNotBlank(params,"nodeId")) {
				Integer nodeId = 0;
				try {
					nodeId = Integer.valueOf(params.get("nodeId").toString());
				} catch (NumberFormatException e) {
					nodeId = 0;
					throw new NumberFormatException("参数[nodeId]:" + params.get("nodeId") + " 不能转换为Integer类型！");
				}
				if (nodeId != 0) {
					Node node = eventDisposalWorkflowService.findNodeById(nodeId);
					if (node != null) {
						nodeName = node.getNodeName();
					}
				}

			}

			if (nodeCodeHandler != null && nodeCodeHandler.isSplitFlow() && (CLUEFLOW_NODENAME.equals(nodeName) || CLUECHECK_NODENAME.equals(nodeName)) && level > 0 && level < 2) {
				//构造组织节点
				List<OrgSocialInfoBO> socialInfoBOs = orgSocialInfoService.findOrgSocialListByParentIdAndProId(orgId, null);
				List<GdZTreeNode> unitNodes = new ArrayList<>();
				List<OrgSocialInfoBO> cueOrgInfoBO = new ArrayList<>();
				//获取当前组织节点、专业过滤时是根据当前组织节点去过滤当前组织节点下的对应专业组织
				OrgSocialInfoBO curOrgInfo = orgSocialInfoService.findByOrgId(orgId);
				GdZTreeNode node = null;

				if (null != socialInfoBOs) {
					for (OrgSocialInfoBO socialInfoBO:socialInfoBOs) {
						if (String.valueOf(ConstantValue.ORG_TYPE_UNIT).equals(socialInfoBO.getOrgType())) {
							socialInfoBO.setChiefLevel(String.valueOf(level + 1));
							node = CommonFunctions.transSocialOrgToZTreeNodeA(socialInfoBO, false);
							unitNodes.add(node);
						}
					}
				}


				if (curOrgInfo != null) {
					cueOrgInfoBO.add(curOrgInfo);
					//节点专业过滤
					super.findDeptByFilterProffession(cueOrgInfoBO, nodes, params, null);
				}

				nodes.addAll(0, unitNodes);
			} else {
				nodes = super.getTreeForEvent(userInfo, orgId, nodeCode, level, professionCode, params);
			}
		}


		if(nodes != null) {
			for(GdZTreeNode node : nodes) {
				if(node.getIsParent() || ConstantValue.GOV_PROFESSION_CODE.equals(node.getProfessionCode())) {
					node.setClickable(false);
				}
			}
		}
		
		return nodes;
	}
}
