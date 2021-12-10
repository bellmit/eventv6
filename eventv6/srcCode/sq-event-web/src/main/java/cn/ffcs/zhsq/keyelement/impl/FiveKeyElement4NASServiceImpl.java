package cn.ffcs.zhsq.keyelement.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 泉州市南安市(NanAnShi) 人员选择接口
 * @ClassName:   FiveKeyElement4NASServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2020年5月19日 下午2:59:15
 */
@Service(value = "fiveKeyElement4NASService")
public class FiveKeyElement4NASServiceImpl extends FiveKeyElementForEventServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	private static final String DISTRICT_DEPARTMENT_BRIGADE_NODE_CODE = "task13";	//区职能部门执法大队处理
	private static final String DISTRICT_DEPARTMENT_SQUADRON_NODE_CODE = "task14";	//区职能部门执法中队处理
	
	@Override
	public List<GdZTreeNode> getTreeForEvent(
			UserInfo userInfo, Long orgId,
			String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception {
		List<GdZTreeNode> nodes = new ArrayList<GdZTreeNode>();
		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		boolean isDistrictDepartmentSend = false;
		
		//职能部门下派给职能部门
		if(nodeCodeHandler.isSend() && nodeCodeHandler.isFromDept()) {
			String curNodeName = null, nextNodeName = null;
			
			if(nodeCodeHandler.isToUnit()) {
				Long instanceId = null;
				
				if(CommonFunctions.isNotBlank(params, "instanceId")) {
					try {
						instanceId = Long.valueOf(params.get("instanceId").toString());
					} catch(NumberFormatException e) {}
				}
				
				if(instanceId != null && instanceId > 0) {
					Map<String, Object> curNodeData = eventDisposalWorkflowService.curNodeData(instanceId);
					
					if(CommonFunctions.isNotBlank(curNodeData, "NODE_NAME")) {
						curNodeName = curNodeData.get("NODE_NAME").toString();
					}
				}
			} else if(nodeCodeHandler.isToDept()) {
				Integer nodeId = null;
				
				if(CommonFunctions.isNotBlank(params, "nodeId")) {
					try {
						nodeId = Integer.valueOf(params.get("nodeId").toString());
					} catch(NumberFormatException e) {}
				}
				
				if(nodeId != null && nodeId > 0) {
					Node nextNodeInfo = baseWorkflowService.capNodeInfo(nodeId);
					if(nextNodeInfo != null) {
						nextNodeName = nextNodeInfo.getNodeName();
					}
				}
			}
			
			if(DISTRICT_DEPARTMENT_BRIGADE_NODE_CODE.equals(curNodeName) 
					|| DISTRICT_DEPARTMENT_SQUADRON_NODE_CODE.equals(curNodeName)
					|| DISTRICT_DEPARTMENT_BRIGADE_NODE_CODE.equals(nextNodeName) 
					|| DISTRICT_DEPARTMENT_SQUADRON_NODE_CODE.equals(nextNodeName)) {
				isDistrictDepartmentSend = nodeCodeHandler.isToDept() || nodeCodeHandler.isToUnit();
				
				if(nodeCodeHandler.isToDept() 
						&& (DISTRICT_DEPARTMENT_BRIGADE_NODE_CODE.equals(nextNodeName) 
						|| DISTRICT_DEPARTMENT_SQUADRON_NODE_CODE.equals(nextNodeName))) {
					List<OrgSocialInfoBO> orgSocialInfoList = null;
					
					orgId = orgId == null ? userInfo.getOrgId() : orgId;
					level = level == null ? 0 : level;
					
					orgSocialInfoList = orgSocialInfoService.findLevelByProfession(orgId, professionCode, false);
					
					if(orgSocialInfoList != null) {
						GdZTreeNode gdZTreeNode = null;
						boolean isLeaf = nodeCodeHandler.getLineLevel() == 1 || level == (nodeCodeHandler.getToLevel() - 1);
						
						for(OrgSocialInfoBO orgSocialInfo : orgSocialInfoList) {
							gdZTreeNode = CommonFunctions.transSocialOrgToZTreeNodeA(orgSocialInfo, isLeaf);
							gdZTreeNode.setClickable(isLeaf);
							nodes.add(gdZTreeNode);
						}
					}
				} else if(nodeCodeHandler.isToUnit() 
						&& (DISTRICT_DEPARTMENT_BRIGADE_NODE_CODE.equals(curNodeName) 
						|| DISTRICT_DEPARTMENT_SQUADRON_NODE_CODE.equals(curNodeName))) {
					List<OrgSocialInfoBO> parentOrgInfoList = this.findOrgByLevel(userInfo.getOrgId(), nodeCodeHandler.getFromLevel() - Integer.valueOf(ConstantValue.DISTRICT_ORG_LEVEL), professionCode, userInfo);
					
					if(parentOrgInfoList != null) {
						OrgSocialInfoBO parentOrgInfo = parentOrgInfoList.get(0);
						UserInfo transferUserInfo = new UserInfo();
						String transferNodeCode = nodeCode;
						
						transferUserInfo.setOrgId(parentOrgInfo.getOrgId());
						transferUserInfo.setOrgCode(parentOrgInfo.getOrgCode());
						transferUserInfo.setOrgName(parentOrgInfo.getOrgName());
						
						transferNodeCode = transferNodeCode.replaceFirst(transferNodeCode.substring(0, 2), INodeCodeHandler.ORG_DEPT + ConstantValue.DISTRICT_ORG_LEVEL);
						
						nodes = this.getTreeForEvent(transferUserInfo, null, transferNodeCode, null, null, null);
					}
				}
			}
		}
		
		if(!isDistrictDepartmentSend) {
			nodes = super.getTreeForEvent(userInfo, orgId, nodeCode, level, professionCode, params);
		}
		
		return nodes;
	}
}
