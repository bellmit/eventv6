package cn.ffcs.zhsq.event.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 晋江市 工作流处理
 * @author zhangls
 *
 */
@Service(value = "eventDisposalWorkflow4JinJiangService")
public class EventDisposalWorkflow4JinJiangServiceImpl extends
		EventDisposalWorkflowForEventServiceImpl {
	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	private String CLOSE_NODE_NAME = "结案";	//结案环节名称
	
	@Override
	public Map<String, Object> capInfo4SMS(String curNodeName, String nextNodeName, Map<String, Object> params, UserInfo userInfo) throws Exception {
		Map<String, Object> resultMap = null;
		
		if(CLOSE_NODE_NAME.equals(nextNodeName)) {
			Long eventId = -1L, 
				 curUserId = -1L, curOrgId = -1L,
				 createUserId = -1L, createOrgId = -1L;
			
			if(CommonFunctions.isNotBlank(params, "eventId")) {
				eventId = Long.valueOf(params.get("eventId").toString());
			}
			if(userInfo != null) {
				curUserId = userInfo.getUserId();
				curOrgId = userInfo.getOrgId();
			}
			
			if(eventId > 0) {
				ProInstance pro = this.capProInstanceByEventId(eventId);
				if(pro != null) {
					createUserId = pro.getUserId();
					createOrgId = Long.valueOf(createOrgId);
				}
			}
			
			//采集者与结案操作者为相同人员时
			if(curUserId > 0 && curOrgId > 0 && createUserId > 0 && createOrgId > 0) {
				if(curUserId.equals(createUserId) && curOrgId.equals(createOrgId)) {
					OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(createOrgId);
					Long parentOrgId = -1L;
					
					if(orgInfo != null) {//该方法只适用于旧组织
						parentOrgId = orgInfo.getParentOrgId();
						
						if(fiveKeyElementService.isUserIdGridAdmin(createOrgId, createUserId)) {
							List<UserBO> userBoList = fiveKeyElementService.findCollectToCloseUserList(parentOrgId);
							if(userBoList != null) {
								Set<String> receiverMobilePhoneSet = new HashSet<String>();
								Long verifyMobile = null;
								
								for(UserBO userBO : userBoList) {
									verifyMobile = userBO.getVerifyMobile();
									
									if(verifyMobile != null && verifyMobile > 0) {
										receiverMobilePhoneSet.add(verifyMobile.toString());
									}
								}
								
								if(receiverMobilePhoneSet.size() > 0) {
									params.put("receiverMobilePhones", StringUtils.join(receiverMobilePhoneSet, ","));
								}
							}
						} else {
							params.put("receiverOrgIds", parentOrgId);
						}
					}
				} else {
					params.put("receiverIds", createUserId);
				}
			}
		}
		
		resultMap = super.capInfo4SMS(curNodeName, nextNodeName, params, userInfo);
		
		return resultMap;
	}
	
	@Override
	public void sendSms(String userIds, String otherMobiles,
			String nextNodeName, Long instanceId, String smsContent, UserInfo userInfo) {
		if(StringUtils.isNotBlank(otherMobiles)) {
			this.sendSms(null, otherMobiles, smsContent, userInfo);
		} else {
			super.sendSms(userIds, otherMobiles, nextNodeName, instanceId, smsContent, userInfo);
		}
	}
}
