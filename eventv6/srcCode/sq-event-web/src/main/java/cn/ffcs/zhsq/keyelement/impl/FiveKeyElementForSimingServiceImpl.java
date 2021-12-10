package cn.ffcs.zhsq.keyelement.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.WorkFlowService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.IEventOrgInfoService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.keyelement.EventNodeCode;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 思明区五要素实现类
 * @author zhangls
 *
 */
@Service(value = "fiveKeyElementForSimingService")
public class FiveKeyElementForSimingServiceImpl extends
		FiveKeyElementForEventServiceImpl {

	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IEventOrgInfoService eventOrgInfoService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private WorkFlowService workFlowService;
	
	private final String COMMUNITY_ORG_CHIEF_LEVEL = "5";//社区 组织层级
	private final Integer ORG_TYPE_DEPARTMENT = 0;// 组织类型 部门
	private final String EVENT_STATUS_WAITING_EVA = "03";//待评价
	
	@Override
	public List<GdZTreeNode> getTreeForEvent(
			UserInfo userInfo, Long orgId,
			String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception {
		List<GdZTreeNode> nodes = new ArrayList<GdZTreeNode>();
		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
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
		
		if (nodeCodeHandler.isSend() && nodeCodeHandler.isToDept()) {//下派给部门
			List<OrgSocialInfoBO> socialInfoBOs = new ArrayList<OrgSocialInfoBO>();
			
			if(nodeCodeHandler.isFromUnit() && nodeCodeHandler.isToDept()) {// 单位下派给部门
				//查找目标层级的政府管理专业的组织
				socialInfoBOs = orgSocialInfoService.findNewBySelfIdAndLevel(userInfo.getOrgId(), nodeCodeHandler.getToLevel(), ConstantValue.GOV_PROFESSION_CODE);
			} else if(nodeCodeHandler.isFromDept() && nodeCodeHandler.isToDept()) {// 部门下派给部门
				//获取部门同级政府专业组织
				List<OrgSocialInfoBO> parentOrgList = orgSocialInfoService.findParentOrg(userInfo.getOrgId());
				
				for(OrgSocialInfoBO orgInfoBO : parentOrgList) {
					//获取目标层级的政府专业的组织
					socialInfoBOs.addAll(orgSocialInfoService.findNewBySelfIdAndLevel(orgInfoBO.getOrgId(), nodeCodeHandler.getToLevel(), ConstantValue.GOV_PROFESSION_CODE));
				}
			}
			
			findDeptBySameAndAdd(socialInfoBOs, nodes, reserveProfessionCode);
		}else if(nodeCodeHandler.isToCollect()) {//采集人员所属组织处理
			Long eventId = -1L;
			
			if(CommonFunctions.isNotBlank(params, "eventId")) {
				try {
					eventId = Long.valueOf(params.get("eventId").toString());
				} catch(NumberFormatException e) {
					eventId = -1L;
					throw new NumberFormatException("参数[eventId]："+params.get("eventId")+" 不能转换为Long型！");
				}
			} else {
				throw new IllegalArgumentException("缺失参数[eventId]，请检查！");
			}
			if(eventId > 0) {
				ProInstance pro = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
				if(pro != null) {
					Long createOrgId = pro.getOrgId();
					OrgSocialInfoBO orgInfo = null;
					
					if(createOrgId != null && createOrgId > 0) {
						orgInfo = orgSocialInfoService.findByOrgId(createOrgId);
					}
					
					if(orgInfo != null) {
						GdZTreeNode node = CommonFunctions.transSocialOrgToZTreeNodeA(orgInfo, true);
						node.setClickable(true);//设置为可点击
						nodes.add(node);
					}
				}
			}
		} else if(StringUtils.isNotBlank(reserveProfessionCode) && nodeCodeHandler.isToDept()) {
			if(nodeCodeHandler.isSplitFlow()) {//职能部门分流到职能部门
				if(nodeCodeHandler.isFromDept() && nodeCodeHandler.isToDept()) {
					List<OrgSocialInfoBO> orgSocialInfoList = orgSocialInfoService.findParentOrg(userInfo.getOrgId());
					
					findDeptBySameAndAdd(orgSocialInfoList, nodes, reserveProfessionCode);
				}
			} else if(nodeCodeHandler.isReport()) {
				List<OrgSocialInfoBO> orgInfoList = this.findOrgByLevel(userInfo.getOrgId(), nodeCodeHandler.getLineLevel(), ConstantValue.GOV_PROFESSION_CODE, nodeCode, userInfo, params);
				
				if(nodeCodeHandler.isFromUnit() && nodeCodeHandler.isToDept()) {//单位上报给职能部门
					findDeptBySameAndAdd(orgInfoList, nodes, reserveProfessionCode);
				}
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
		INodeCodeHandler nodeCodeHandler = null;
		EventNodeCode codeHandler = null;
		
		if(StringUtils.isNotBlank(nodeCode)) {
			try {
				boolean isEvaluate = false;
				
				if(CommonFunctions.isNotBlank(params, "eventId")) {
					Long eventId = -1L;
					EventDisposal event = null;
					
					try {
						eventId = Long.valueOf(params.get("eventId").toString());
					} catch(NumberFormatException e) {}
					
					event = eventDisposalService.findEventByIdSimple(eventId);
					if(event != null) {
						isEvaluate = EVENT_STATUS_WAITING_EVA.equals(event.getStatus());
					}
				}
				
				codeHandler = createNodeCode(nodeCode);
				codeHandler.setPlaceFile(isEvaluate);//为了展示评价功能
				
				codeHandler.setClose(true);//为了展示处理后图片，因目前没有结案操作
				
				nodeCodeHandler = codeHandler;
				
			} catch (Exception e) {
				resultMap.put("msg", e.getMessage());
			}
		}
		
		resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, null);
		
		if(nodeCodeHandler != null) {
			resultMap.put("eventNodeCode", nodeCodeHandler);
		}
		
		return resultMap;
	}
	
	@Override
	public List<OrgSocialInfoBO> findOrgByLevel(Long orgId, int level, String professionCode, String nodeCode, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		List<OrgSocialInfoBO> orgSocialInfos = null;
		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
		
		if(orgId != null && orgId > 0){
			OrgSocialInfoBO orgSocialInfoBO = orgSocialInfoService.findByOrgId(orgId);
			
			if (orgSocialInfoBO != null) {
				if(!ConstantValue.GOV_PROFESSION_CODE.equals(orgSocialInfoBO.getProfessionCode())) {//思明区街道党政办上报给区指挥中心，层级加一，因为系统管理中会将层级减一GOV_PROFESSION_CODE
					level++;
				} else if (userInfo != null) {//社区级网格员上报社区处理时，需要降级处理
					if(nodeCodeHandler.isReport()) {
						int fromLevel = nodeCodeHandler.getFromLevel();
						int orgLevel = 0;
						
						String chiefLevel = orgSocialInfoBO.getChiefLevel();
						if(StringUtils.isNotBlank(chiefLevel)) {
							try {
								orgLevel = Integer.valueOf(chiefLevel);
							} catch(NumberFormatException e) {}
							
						}
						
						if(fromLevel < orgLevel) {
							level = orgLevel - nodeCodeHandler.getToLevel();
						}
						
						if (COMMUNITY_ORG_CHIEF_LEVEL.equals(orgSocialInfoBO.getChiefLevel()) 
								&& nodeCodeHandler.isFromGrid() && nodeCodeHandler.isToCommunity()
								&& isUserIdGridAdmin(orgId, userInfo.getUserId())) {
							level--;
						}
					}
				}
			}
			
			orgSocialInfos = orgSocialInfoService.findSuperiorByCode(orgId, level, professionCode);
		}
		
		return orgSocialInfos;
	}
	
	@Override
	public List<cn.ffcs.uam.bo.UserInfo> getUserInfoList(Long orgId, String nodeCode, Long removeUserId, Map<String, Object> extraParam) throws Exception {
		List<cn.ffcs.uam.bo.UserInfo> userInfos = new ArrayList<cn.ffcs.uam.bo.UserInfo>();
		if (nodeCode != null) {
			INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
			List<cn.ffcs.uam.bo.UserInfo> users =  this.workFlowService.findUserInfoByOrgId(orgId);//获取组织下的所有用户
			
			if (nodeCodeHandler.isSendToGrid()) {//下派到网格
				Long userId = -1L;
				for (UserInfo userInfo : users) {
					userId = userInfo.getUserId();
					if(this.isUserIdGridAdmin(orgId, userId)){
						if(!userId.equals(removeUserId)) {
							userInfos.add(userInfo);
						}
					}
				}
			} else {
				userInfos = super.getUserInfoList(orgId, nodeCode, removeUserId, extraParam);
			}
		} else {
			throw new Exception("环节编码不能为空！");
		}
		
		return userInfos;
	}
	
	/**
	 * 保留指定专业的组织，没有配置专业时，展示所有的
	 * @param orgInfoList
	 * @param nodes
	 * @param reserveProfessionCode 有多个时，以英文逗号分隔
	 * @return
	 */
	private Map<Long, OrgSocialInfoBO> findDeptBySameAndAdd(List<OrgSocialInfoBO> orgInfoList, List<GdZTreeNode> nodes, String reserveProfessionCode) {
		Long userOrgId = null;
		List<OrgSocialInfoBO> socialInfoBOs = null;
		Integer orgType = null;
		Map<Long, OrgSocialInfoBO> socialInfoMap = new HashMap<Long, OrgSocialInfoBO>();
		
		if(orgInfoList != null && orgInfoList.size() > 0) {
			String[] reserveCodes = null;
			
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
