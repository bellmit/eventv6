package cn.ffcs.zhsq.keyelement.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.om.Workflow;
import cn.ffcs.workflow.spring.HessianFlowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service(value = "fiveKeyElementService")
public class FiveKeyElementServiceImpl extends
	ApplicationObjectSupport implements IFiveKeyElementService {
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private HessianFlowService hessianFlowService;
	
	@Override
	public List<UserInfo> getUserInfoList(Long orgId, String nodeCode, Map<String, Object> extraParam) throws Exception {
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl("");
		
		return fiveKeyElementService.getUserInfoList(orgId, nodeCode, extraParam);
	}
	
	@Override
	public List<UserInfo> getUserInfoList(Long orgId, String nodeCode, Long removeUserId, Map<String, Object> extraParam) throws Exception {
		String orgCode = "";
		
		if(orgId != null && orgId > 0) {
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
			if(orgInfo != null) {
				orgCode = orgInfo.getOrgCode();
			}
		}
		
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl(orgCode, this.capTrigcondition(extraParam));
		
		return fiveKeyElementService.getUserInfoList(orgId, nodeCode, removeUserId, extraParam);
	}

	@Override
	public INodeCodeHandler createNodeCodeHandle(String nodeCode) throws Exception {
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl("");
		
		return fiveKeyElementService.createNodeCodeHandle(nodeCode);
	}

	@Override
	public List<OrgSocialInfoBO> findOrgByLevel(Long orgId, int level, String professionCode, String nodeCode, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		//IFiveKeyElementService fiveKeyElementService = this.capServiceImpl(userInfo.getOrgCode(), this.capExtraType(extraParam));
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl(userInfo.getOrgCode(), this.capTrigcondition(extraParam));

		return fiveKeyElementService.findOrgByLevel(orgId, level, professionCode, nodeCode, userInfo, extraParam);
	}
	
	@Override
	public List<OrgSocialInfoBO> findOrgByLevel(Long orgId, int level, String professionCode, UserInfo userInfo) {
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl("");
		
		return fiveKeyElementService.findOrgByLevel(orgId, level, professionCode, userInfo);
	}
	
	@Override
	public List<OrgSocialInfoBO> findOrgByLevel(Long orgId, int level, UserInfo userInfo) {
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl("");
		
		return fiveKeyElementService.findOrgByLevel(orgId, level, userInfo);
	}
	
	@Override
	public boolean isUserIdGridAdmin(Long orgId, Long userId) {
		IFiveKeyElementService fiveKeyElementService = null;
		String userOrgCode = null;
		
		if(orgId != null && orgId > 0) {
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
			
			if(orgInfo != null) {
				userOrgCode = orgInfo.getOrgCode();
			}
		}
		
		fiveKeyElementService = this.capServiceImpl(userOrgCode, this.capTrigcondition(null));
		
		return fiveKeyElementService.isUserIdGridAdmin(orgId, userId);
	}
	
	@Override
	public String isUserIdGridAdminForString(Long orgId, Long userId){
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl("");
		
		return fiveKeyElementService.isUserIdGridAdminForString(orgId, userId);
	}
	
	@Override
	public String isUserIdGridAdminForString(boolean isGridAdmin) {
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl("");
		
		return fiveKeyElementService.isUserIdGridAdminForString(isGridAdmin);
	}
	
	/**
	 * 获取配置的网格管理员职务
	 * @param orgId 组织编码
	 * @return 没有获取到配置时，返回空串；否则，返回格式为'001','002'的网格管理员职务字符串
	 */
	@Override
	public String capGridAdminDuty(Long orgId) {
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl("");
		
		return fiveKeyElementService.capGridAdminDuty(orgId);
	}
	
	/**
	 * 判断人员是否是指定职务的网格管理员
	 * 如果组织没有对应的所属地域，则所有人员均认为不满足条件
	 * @param startGridId 起始查找的网格编码
	 * @param userId 用户id
	 * @param gridAdminDuty 网格管理员职务
	 * 001 网格管理员；002 网格协管员；003 网格督导员；004 包片段警；
	 * @return true 存在；false 不存在
	 */
	@Override
	public boolean isUserIdGridAdminExists(Long startGridId, Long userId, String gridAdminDuty){
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl("");
		
		return fiveKeyElementService.isUserIdGridAdminExists(startGridId, userId, gridAdminDuty);
	}
	
	@Override
	public List<UserBO> findReportUserList(String nodeCode,
			String positionName, Long positionId, String includeNext,
			Long orgId, String orgCode) throws Exception {
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl("");
		
		return fiveKeyElementService.findReportUserList(nodeCode, positionName, positionId, includeNext, orgId, orgCode);
	}

	@Override
	public List<UserBO> getReportUserList(INodeCodeHandler nodeCodeHandler,
			String positionName, Long positionId, String includeNext,
			Long orgId, String orgCode) {
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl("");
		
		return fiveKeyElementService.getReportUserList(nodeCodeHandler, positionName, positionId, includeNext, orgId, orgCode);
	}

	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		//IFiveKeyElementService fiveKeyElementService = this.capServiceImpl(userInfo.getOrgCode(), this.capExtraType(params));
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl(userInfo.getOrgCode(), this.capTrigcondition(params));
		
		return fiveKeyElementService.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
	}

	@Override
	public List<GdZTreeNode> getTreeForEvent(
			UserInfo userInfo, Long orgId,
			String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception {
		//IFiveKeyElementService fiveKeyElementService = this.capServiceImpl(userInfo.getOrgCode(), this.capExtraType(params));
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl(userInfo.getOrgCode(), this.capTrigcondition(params));
		
		return fiveKeyElementService.getTreeForEvent(userInfo, orgId, nodeCode, level, professionCode, params);
	}

	@Override
	public List<UserBO> findCollectToCloseUserList(Long orgId) throws Exception {
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl("");
		
		return fiveKeyElementService.findCollectToCloseUserList(orgId);
	}

	@Override
	public List<UserBO> filterGridAdmin(Long orgId, List<UserBO> users) {
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl("");
		
		return fiveKeyElementService.filterGridAdmin(orgId, users);
	}
	
	@Override
	public List<UserBO> filterGridAdmin(Long orgId, List<UserBO> users, boolean isRemoveGridAdmin) {
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl("");
		
		return fiveKeyElementService.filterGridAdmin(orgId, users, isRemoveGridAdmin);
	}
	
	@Override
	public Map<Long, OrgSocialInfoBO> findDeptBySameAndAdd(List<OrgSocialInfoBO> orgInfoList, List<GdZTreeNode> nodes, Long removeOrgId) {
		IFiveKeyElementService fiveKeyElementService = this.capServiceImpl("");
		
		return fiveKeyElementService.findDeptBySameAndAdd(orgInfoList, nodes, removeOrgId);
	}
	
	/**
	 * 获取对应实现类
	 * @param orgCode	组织编码
	 * @return
	 */
	private IFiveKeyElementService capServiceImpl(String orgCode) {
		return capServiceImpl(orgCode, null);
	}
	
	/**
	 * 获取对应实现类
	 * @param orgCode		组织编码
	 * @param trigCondition		触发条件
	 * @return
	 */
	private IFiveKeyElementService capServiceImpl(String orgCode, String trigCondition) {
		String serviceImpl = funConfigurationService.changeCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, trigCondition, IFunConfigurationService.CFG_TYPE_JAVA_PROCESSOR, orgCode, IFunConfigurationService.DIRECTION_UP_REGEXP);
		
		if(StringUtils.isBlank(serviceImpl)) {
			serviceImpl = "fiveKeyElementForEventService";
		}
		
		return (IFiveKeyElementService) this.getApplicationContext().getBean(serviceImpl);
	}

	/**
	 * 构建人员五要素接口获取功能配置触发条件
	 * @param params
	 * 			eventId		事件id
	 * 			formType	表单类型
	 * @return
	 */
	private String capTrigcondition(Map<String, Object> params) {
		String extraType = "", workflowName = null, bizPlatform = null,
			   FIVE_KEY_ELEMENT_TEMPLATE = ConstantValue.FIVEKEY_ELEMENT + "@workflowName@@bizPlatform@@extraType@",
			   fiveKeyElementTrig = null;
		StringBuffer trigcondition = new StringBuffer("");
		Long eventId = -1L, instanceId = -1L;
		
		try {
			if(CommonFunctions.isNotBlank(params, "eventId")) {
				eventId = Long.valueOf(params.get("eventId").toString());
			} else if(CommonFunctions.isBlank(params, "formType") && CommonFunctions.isNotBlank(params, "formId")) {
				eventId = Long.valueOf(params.get("formId").toString());
			}
		} catch(NumberFormatException e) {}
		
		if(CommonFunctions.isNotBlank(params, "instanceId")) {
			try {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} catch(NumberFormatException e) {}
		}
		
		if(CommonFunctions.isNotBlank(params, "workflowName")) {
			workflowName = params.get("workflowName").toString();
		} else {
			ProInstance proInstance = null;
			
			if(instanceId > 0) {
				proInstance = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
			} else if(eventId > 0) {
				proInstance = eventDisposalWorkflowService.capProInstanceByEventId(eventId);
			}
			
			if(proInstance != null) {
				workflowName = proInstance.getProName();
			} else if(CommonFunctions.isNotBlank(params, "workflowId")) {
				Long workflowId = null;
				
				try {
					workflowId = Long.valueOf(params.get("workflowId").toString());
				} catch(NumberFormatException e) {}
				
				if(workflowId != null && workflowId > 0) {
					Workflow workflow = new Workflow();
					List<Workflow> workflowList = null;
					
					workflow.setWorkFlowId(workflowId);
					workflowList = hessianFlowService.queryWorkflows(workflow);
					
					if(workflowList != null && workflowList.size() > 0) {
						workflowName = workflowList.get(0).getFlowName();
					}
				}
			}
		}
		
		if(eventId > 0) {
			EventDisposal event = eventDisposalService.findEventByIdSimple(eventId);
			
			if(event != null) {
				String eventBizPlatform = event.getBizPlatform();
				extraType = event.getType();

				if(StringUtils.isNotBlank(extraType) && StringUtils.isNotBlank(eventBizPlatform) && !ConstantValue.DEFAULT_EVENT_BIZ_PLATFORM.equals(eventBizPlatform)) {
					bizPlatform = eventBizPlatform;
				}
			}
		} else if(CommonFunctions.isNotBlank(params, "formType")) {
			extraType = params.get("formType").toString();
		}

		extraType = StringUtils.isBlank(extraType) ? "" : "_" + extraType;
		bizPlatform = StringUtils.isBlank(bizPlatform) ? "" : "_" + bizPlatform;
		
		fiveKeyElementTrig = FIVE_KEY_ELEMENT_TEMPLATE.replace("@extraType@", extraType);
		
		trigcondition.append("[").append(fiveKeyElementTrig.replace("@workflowName@", "").replace("@bizPlatform@", ""));
		
		fiveKeyElementTrig = fiveKeyElementTrig.replace("@bizPlatform@", bizPlatform);
		
		if(StringUtils.isNotBlank(bizPlatform)) {
			trigcondition.append("|").append(fiveKeyElementTrig.replace("@workflowName@", ""));
		}
		
		if(StringUtils.isNotBlank(workflowName)) {
			trigcondition.append("|").append(fiveKeyElementTrig.replace("@workflowName@", "-" + workflowName));
		}
		
		trigcondition.append("]");

		return trigcondition.toString();
	}
}
