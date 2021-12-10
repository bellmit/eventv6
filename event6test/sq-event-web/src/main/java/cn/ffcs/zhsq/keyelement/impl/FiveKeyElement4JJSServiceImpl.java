package cn.ffcs.zhsq.keyelement.impl;

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
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 晋江市(JJS) 人员选择处理类
 * @author zhangls
 *
 */
@Service(value = "fiveKeyElement4JJSService")
public class FiveKeyElement4JJSServiceImpl extends
		FiveKeyElementForEventServiceImpl {
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService;
	
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		
		//是否可上传处理后图片
		resultMap.put("isUploadHandledPic", true);
		
		return resultMap;
	}
	
	/**
	 * 过滤出指定专业的部门
	 * @param orgInfoList	与目标部门组织同级的单位组织
	 * @param nodes			过滤后的的节点信息
	 * @param params
	 * 			nodeId			下一环节节点id
	 * 			instanceId		流程实例id
	 * 			nodeCode		节点办理编码
	 * @param removeOrgId	需要排除的组织，当前办理人员的组织
	 * @return
	 */
	protected Map<Long, OrgSocialInfoBO> findDeptByFilterProffession(List<OrgSocialInfoBO> orgInfoList, List<GdZTreeNode> nodes, Map<String, Object> params, Long removeOrgId) {
		Long userOrgId = null, instanceId = null;
		List<OrgSocialInfoBO> socialInfoBOs = null;
		Integer orgType = null,
					level = -1;
		Map<Long, OrgSocialInfoBO> socialInfoMap = new HashMap<Long, OrgSocialInfoBO>();
		
		if(orgInfoList != null && orgInfoList.size() > 0) {
			String[] reserveCodes = null;
			String reserveProfessionCode = "";
			
			if(CommonFunctions.isNotBlank(params, "level")) {
				try {
					level = Integer.valueOf(params.get("level").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				try {
					instanceId = Long.valueOf(params.get("instanceId").toString());
				} catch(NumberFormatException e) {
					throw new NumberFormatException("属性[instanceId]："+params.get("instanceId")+" 不能转换为Long型！");
				}
			} else if(CommonFunctions.isNotBlank(params, "formId")) {
				try {
					instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(Long.valueOf(params.get("formId").toString()));
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			} else if(CommonFunctions.isNotBlank(params, "eventId")) {
				try {
					instanceId = eventDisposalWorkflowService.capInstanceIdByEventIdForLong(Long.valueOf(params.get("eventId").toString()));
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			if(instanceId != null && instanceId > 0) {
				ProInstance proInstance = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
				
				if(proInstance != null) {
					if(ConstantValue.EVENT_SUB_WORKFLOW_FORM_TYPE_ID.equals(proInstance.getFormTypeId())) {
						INodeCodeHandler nodeCodeHandler = null;
						
						if(CommonFunctions.isNotBlank(params, "nodeCode")) {
							try {
								nodeCodeHandler = this.createNodeCodeHandle(params.get("nodeCode").toString());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						//只有叶子节点才进行专业过滤
						if(nodeCodeHandler != null && nodeCodeHandler.getLineLevel() == level) {
							if(removeOrgId != null && removeOrgId > 0) {
								OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(removeOrgId);
								if(orgInfo != null) {
									reserveProfessionCode = orgInfo.getProfessionCode();
								}
							}
						}
					}
				} else {
					throw new IllegalArgumentException("属性[instanceId]：" +params.get("instanceId") + " 没有对应有效的流程实例信息！");
				}
			} else {
				throw new IllegalArgumentException("缺少属性[instanceId]，请检查！");
			}
			
			if(StringUtils.isBlank(reserveProfessionCode)) {
				if(CommonFunctions.isNotBlank(params, "nodeId")) {//获取节点配置的专业过滤信息
					Integer nodeId = 0;
					
					try {
						nodeId = Integer.valueOf(params.get("nodeId").toString());
					} catch(NumberFormatException e) {
						nodeId = 0;
						throw new NumberFormatException("属性[nodeId]："+params.get("nodeId")+" 不能转换为Integer型！");
					}
					
					if(nodeId != 0) {
						Node node = eventDisposalWorkflowService.findNodeById(nodeId);
						if(node != null) {
							reserveProfessionCode = node.getProfessionCode();
						}
					}
				}
			}
			
			if(StringUtils.isNotBlank(reserveProfessionCode)) {
				reserveCodes = reserveProfessionCode.split(",");
			}
				
			for(OrgSocialInfoBO orgSocialInfo : orgInfoList) {
				userOrgId = orgSocialInfo.getOrgId();
				
				if(!eventOrgInfoService.isNewOrganization(userOrgId)){
					orgType = ConstantValue.ORG_TYPE_DEPT;
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
						socialOrg.setChiefLevel(String.valueOf(level + 1));
						
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
