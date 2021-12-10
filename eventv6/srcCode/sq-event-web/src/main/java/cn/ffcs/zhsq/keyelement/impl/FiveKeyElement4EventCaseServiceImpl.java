package cn.ffcs.zhsq.keyelement.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.eventCase.service.IEventCaseWorkflowService;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 案件 人员选择
 * @author zhangls
 *
 */
@Service(value = "fiveKeyElement4EventCaseService")
public class FiveKeyElement4EventCaseServiceImpl extends
		FiveKeyElementForEventServiceImpl {
	@Autowired
	private IEventCaseWorkflowService eventCaseWorkflowService;
	
	@Autowired
	private IBaseWorkflowService baseWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	private String EVA_NODE_CODE = "PJ";//评价节点编码
	
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//可评价人员为实例创建者所属组织的父级组织人员
		if(StringUtils.isNotBlank(nodeCode) && nodeCode.endsWith(EVA_NODE_CODE)) {
			Long instanceId = -1L;
			
			if(CommonFunctions.isNotBlank(params, "instanceId")) {
				instanceId = Long.valueOf(params.get("instanceId").toString());
			} else if(CommonFunctions.isNotBlank(params, "formId")) {
				Long caseId = -1L;
				
				try {
					caseId = Long.valueOf(params.get("formId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if(caseId > 0) {
					instanceId = eventCaseWorkflowService.capInstanceId(caseId, userInfo);
				}
			}
			
			if(instanceId > 0) {
				ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
				if(proInstance != null) {
					Long orgId = proInstance.getOrgId();
					
					if(orgId != null && orgId > 0) {
						OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(orgId);
						if(orgInfo != null) {
							//旧组织才可以这样获取父级信息
							orgInfo = orgSocialInfoService.findByOrgId(orgInfo.getParentOrgId());
							if(orgInfo != null) {
								resultMap.put("userNames", orgInfo.getOrgName());
								resultMap.put("orgIds", orgInfo.getOrgId());
							}
						}
					}
				}
			} else {
				throw new Exception("缺少有效的instanceId！");
			}
			
			//办理页面是否显示人员选择 true为是，false为否
			resultMap.put("isSelectUser", true);
			//办理页面是否显示组织选中 true为是，false为否
			resultMap.put("isSelectOrg", false);
			
		} else {
			resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		}
		
		return resultMap;
	}
}
