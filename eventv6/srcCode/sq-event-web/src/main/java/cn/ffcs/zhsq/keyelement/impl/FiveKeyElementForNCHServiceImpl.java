package cn.ffcs.zhsq.keyelement.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * 江西省南昌市南昌县五要素实现类
 * @author zhangls
 *
 */
@Service(value = "fiveKeyElementForNCHService")
public class FiveKeyElementForNCHServiceImpl extends
		FiveKeyElementForEventServiceImpl {

	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Override
	public Map<String, Object> getNodeInfoForEvent(
			UserInfo userInfo,
			String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("msg", "");
		INodeCodeHandler nodeCodeHandler = null;
		boolean isNotUseParent = true;
		
		if(StringUtils.isNotBlank(nodeCode)) {
			try {
				nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
				resultMap.put("eventNodeCode", nodeCodeHandler);
				
				isNotUseParent = nodeCodeHandler.isPerson() || nodeCodeHandler.isToCollect();
				
				if (nodeCodeHandler.isPerson()) {//指派到特定的人
					//获取评价人员 数据格式 orgCode1:userName11,userName12;orgCode2:userName2;
					String userNameOrgCodes = funConfigurationService.turnCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.EVALUATE_USER_ORG_ID, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
					StringBuffer userIds = new StringBuffer("");
					StringBuffer orgIds = new StringBuffer("");
					
					if(StringUtils.isNotBlank(userNameOrgCodes)) {
						String[] userNameOrgCodeArray = userNameOrgCodes.split(";");//分割各用户id和组织id组合
						String[] nameCodeArray = new String[]{};
						String userIdsStr = "";
						String regisValues = "";
						String[] regisValueArray = new String[]{};
						String userOrgCode = "";
						OrgSocialInfoBO orgInfo = null;
						UserBO user = null;
						
						for(String userIdOrgId : userNameOrgCodeArray) {
							if(StringUtils.isNotBlank(userIdOrgId)) {
								nameCodeArray = userIdOrgId.split(":");//分割用户id和组织id
								if(nameCodeArray.length == 2) {
									userOrgCode = nameCodeArray[0];
									regisValues = nameCodeArray[1];
									
									orgInfo = orgSocialInfoService.selectOrgSocialInfoByOrgCode(userOrgCode);
									if(orgInfo != null) {
										if(StringUtils.isNotBlank(regisValues)) {
											regisValueArray = regisValues.split(",");
											for(String regisValue : regisValueArray) {
												if(StringUtils.isNotBlank(regisValue)) {
													user = userManageService.getUserInfoByRegistValue(regisValue);
													if(user != null) {
														userIds.append(",").append(user.getUserId());//userId与orgId是一一对应的
														orgIds.append(",").append(orgInfo.getOrgId());
													}
												}
											}
										}
									}
								}
							}
						}
						
						userIdsStr = userIds.substring(1);
						
						resultMap.put("userIds", userIdsStr);
						resultMap.put("userNames", eventDisposalWorkflowService.curNodeUserNames(userIdsStr));
						resultMap.put("orgIds", orgIds.substring(1));
					}
				} else if(nodeCodeHandler.isToCollect()) {//驳回到采集事件
					resultMap.put("userIds", userInfo.getUserId());
					resultMap.put("userNames", userInfo.getPartyName());
					resultMap.put("orgIds", userInfo.getOrgId());
				}
			} catch (Exception e) {
				resultMap.put("msg", e.getMessage());
			}
		}
		
		if(!isNotUseParent) {
			resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		} 
		
		//办理页面是否显示人员选择 true为是，false为否
		resultMap.put("isSelectUser", isNotUseParent);
		
		return resultMap;
	}
}
