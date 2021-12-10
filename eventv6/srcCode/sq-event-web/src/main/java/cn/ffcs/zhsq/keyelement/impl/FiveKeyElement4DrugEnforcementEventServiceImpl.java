package cn.ffcs.zhsq.keyelement.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.zhsq.drugEnforcementEvent.service.IDrugEnforcementEventService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 禁毒事件五要素实现类
 * @author zhangls
 *
 */
@Service(value = "fiveKeyElement4DrugEnforcementEventService")
public class FiveKeyElement4DrugEnforcementEventServiceImpl extends
		FiveKeyElementForEventServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IDrugEnforcementEventService drugEnforcementEventService;
	
	private final Integer ORG_TYPE_DEPARTMENT = 0;// 组织类型 部门
	
	@Override
	public List<GdZTreeNode> getTreeForEvent(
			UserInfo userInfo, Long orgId,
			String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception {
		List<GdZTreeNode> nodes = new ArrayList<GdZTreeNode>();
		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		
		if (nodeCodeHandler.isReport() && nodeCodeHandler.isToDept()) {//上报给职能部门
			int orgLevel = nodeCodeHandler.getLineLevel();
			List<OrgSocialInfoBO> orgInfoList = null;
			Long userOrgId = userInfo.getOrgId();
			
			//旧组织职能部门，层级加一
			if(!eventOrgInfoService.isNewOrganization(userOrgId) && nodeCodeHandler.isFromDept()) {
				orgLevel++;
			}

			orgInfoList = this.findOrgByLevel(userOrgId, orgLevel, ConstantValue.GOV_PROFESSION_CODE, nodeCode, userInfo, params);
			
			findDeptByFilterProffession(orgInfoList, nodes, params, null);
		} else if(nodeCodeHandler.isSplitFlow() && nodeCodeHandler.isFromDept() && nodeCodeHandler.isToDept()) {//分流 职能部门分流到职能部门
			List<OrgSocialInfoBO> orgSocialInfoList = orgSocialInfoService.findParentOrg(userInfo.getOrgId());
			
			findDeptByFilterProffession(orgSocialInfoList, nodes, params, userInfo.getOrgId());
		} else if(nodeCodeHandler.isSend() && nodeCodeHandler.isToDept() && (orgId != null && orgId > 0)) {//下派给部门
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
			
			if(orgInfo != null) {
				List<OrgSocialInfoBO> orgSocialInfoList = new ArrayList<OrgSocialInfoBO>();
				
				orgSocialInfoList.add(orgInfo);
				
				findDeptByFilterProffession(orgSocialInfoList, nodes, params, userInfo.getOrgId());
			}
		} else {
			nodes = super.getTreeForEvent(userInfo, orgId, nodeCode, level, professionCode, params);
		}
		
		return nodes;
	}
	
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(StringUtils.isNotBlank(nodeCode)) {
			INodeCodeHandler nodeCodeHandler = null;
			
			try {
				nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
				
				if(nodeCodeHandler.isToBegin()) {//由采集人员办理
					Long drugEnforcementId = -1L;
					Map<String, Object> proMap = null;
					
					if(CommonFunctions.isNotBlank(params, "formId")) {
						drugEnforcementId = Long.valueOf(params.get("formId").toString());
					}
					
					if(drugEnforcementId != null && drugEnforcementId > 0) {
						proMap = drugEnforcementEventService.capProInstance(drugEnforcementId, userInfo);
						
						if(proMap != null && !proMap.isEmpty()) {
							if(CommonFunctions.isNotBlank(proMap, "userId")) {
								resultMap.put("userIds", proMap.get("userId"));
							}
							if(CommonFunctions.isNotBlank(proMap, "userName")) {
								resultMap.put("userNames", proMap.get("userName"));
							}
							if(CommonFunctions.isNotBlank(proMap, "orgId")) {
								resultMap.put("orgIds", proMap.get("orgId"));
							}
							
							//办理页面是否显示人员选择 true为是，false为否
							resultMap.put("isSelectUser", true);
							//办理页面是否显示组织选中 true为是，false为否
							resultMap.put("isSelectOrg", false);
							
						}
					}
				} else {
					resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
				}
			} catch (Exception e) {
				resultMap.put("msg", e.getMessage());
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 过滤出指定专业的部门
	 * @param orgInfoList	与目标部门组织同级的单位组织
	 * @param nodes			过滤后的的节点信息
	 * @param params
	 * 			nodeId		下一环节节点id
	 * @param removeOrgId	需要排除的组织
	 * @return
	 */
	protected Map<Long, OrgSocialInfoBO> findDeptByFilterProffession(List<OrgSocialInfoBO> orgInfoList, List<GdZTreeNode> nodes, Map<String, Object> params, Long removeOrgId) {
		Long userOrgId = null;
		List<OrgSocialInfoBO> socialInfoBOs = null;
		Integer orgType = null;
		Map<Long, OrgSocialInfoBO> socialInfoMap = new HashMap<Long, OrgSocialInfoBO>();
		
		if(orgInfoList != null && orgInfoList.size() > 0) {
			String[] reserveCodes = null;
			
			String reserveProfessionCode = "";
			
			if(CommonFunctions.isNotBlank(params, "nodeId")) {//获取节点配置的专业过滤信息
				Integer nodeId = 0;
				
				try {
					nodeId = Integer.valueOf(params.get("nodeId").toString());
				} catch(NumberFormatException e) {
					nodeId = 0;
					throw new NumberFormatException("参数[nodeId]："+params.get("nodeId")+" 不能转换为Integer型！");
				}
				
				if(nodeId != 0) {
					Node node = eventDisposalWorkflowService.findNodeById(nodeId);
					if(node != null) {
						reserveProfessionCode = node.getProfessionCode();
					}
				}
			} else {
				throw new IllegalArgumentException("缺失参数[nodeId]，请检查！");
			}
			
			if(StringUtils.isNotBlank(reserveProfessionCode)) {
				reserveCodes = reserveProfessionCode.split(",");
			}
				
			for(OrgSocialInfoBO orgSocialInfo : orgInfoList) {
				userOrgId = orgSocialInfo.getOrgId();
				
				if(!eventOrgInfoService.isNewOrganization(userOrgId)){
					orgType = ORG_TYPE_DEPARTMENT;
				}
				
				socialInfoBOs = orgSocialInfoService.findUnitBySame(userOrgId, false, orgType);
				
				for (OrgSocialInfoBO socialOrg : socialInfoBOs) {
					if(removeOrgId != null && removeOrgId > 0 && removeOrgId.equals(socialOrg.getOrgId())) {
						//do nothing
					} else {
						if(reserveCodes != null) {
							for(String reserveCode : reserveCodes) {
								if(StringUtils.isNotBlank(reserveCode) && reserveCode.equals(socialOrg.getProfessionCode())) {
									socialInfoMap.put(socialOrg.getOrgId(), socialOrg);
									break;
								}
							}
						} else {
							socialInfoMap.put(socialOrg.getOrgId(), socialOrg);
						}
					}
				}
			}
			
			if(nodes != null) {
				Set<Long> socialInfoIdSet = socialInfoMap.keySet();
				OrgSocialInfoBO socialOrg = null;
				GdZTreeNode node = null;
				
				for(Long socialInfoId : socialInfoIdSet) {//为了去重，因为上级可能有多个
					socialOrg = socialInfoMap.get(socialInfoId);
					if (!eventOrgInfoService.isGovernment(socialOrg.getProfessionCode())) {
						socialOrg.setChiefLevel("0");
						
						node = CommonFunctions.transSocialOrgToZTreeNodeA(socialOrg, true);
						node.setClickable(true);
						
						nodes.add(node);
					}
				}
			}
		}
		
		return socialInfoMap;
	}
}
