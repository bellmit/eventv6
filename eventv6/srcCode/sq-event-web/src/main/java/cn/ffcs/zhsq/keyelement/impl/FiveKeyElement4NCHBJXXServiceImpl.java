package cn.ffcs.zhsq.keyelement.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:南昌市指挥调度人员选择处理类
 * @Author: zhangtc
 * @Date: 2019/8/1 16:55
 */
@Service(value = "fiveKeyElement4NCHBJXXService")
public class FiveKeyElement4NCHBJXXServiceImpl extends FiveKeyElementForEventServiceImpl {
	
	//特定职位办理人
	private static final String POSITION_NAME = "街巷长管理员";	
	
	private static final String TOWNDISPOSAl_STREET_LEADER_CODE = "task2";		//乡镇街巷长处理
    private static final String TOWNDISPOSAl_DEPARTMENT_STREET_LEADER_CODE = "task3";		//乡镇部门街巷长处理
    
    private static final String TOWNDISPOSAl_NODE_CODE = "task4";	//乡镇（街巷长管理员）处理环节
    private static final String DISTRICT_NODE_CODE = "task5";	//县区（街巷长管理员）处理环节
    private static final String CITY_NODE_CODE = "task6";	//市级（街巷长管理员）处理环节
    
    public final static Map<String, String> TaskChangeMapping = new HashMap<String, String>() {
	    {
	    	//乡镇街巷长处理-乡镇（街巷长管理员）处理环节
	        put(TOWNDISPOSAl_STREET_LEADER_CODE+"-"+TOWNDISPOSAl_NODE_CODE, "true");
	        //乡镇部门街巷长处理-乡镇（街巷长管理员）处理环节
	        put(TOWNDISPOSAl_DEPARTMENT_STREET_LEADER_CODE+"-"+TOWNDISPOSAl_NODE_CODE, "true");
	        //乡镇（街巷长管理员）处理环节-县区（街巷长管理员）处理环节
	        put(TOWNDISPOSAl_NODE_CODE+"-"+DISTRICT_NODE_CODE, "true");
	        //县区（街巷长管理员）处理环节-市级（街巷长管理员）处理环节
	        put(DISTRICT_NODE_CODE+"-"+CITY_NODE_CODE, "true");
	       
	    }
	};

    @Override
    public Map<String, Object> getNodeInfoForEvent(
            UserInfo userInfo,
            String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
            throws Exception {
        Map<String, Object> resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);

        //特定的环节扭转，需要自动检测出符合职位的办理人员展示出来
        if(CommonFunctions.isNotBlank(TaskChangeMapping, curnodeName+"-"+nodeName)) {
        	//设置返回标识
        	
        	// 当前用户及其组织
        	INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
			List<OrgSocialInfoBO> orgList = this.findOrgByLevel(userInfo.getOrgId(), nodeCodeHandler.getLineLevel(), ConstantValue.GOV_PROFESSION_CODE, nodeCode, userInfo, params);// level为1时，org为上级；level为2时，org为越级上级
			StringBuffer userIds = new StringBuffer("");
			StringBuffer userNames = new StringBuffer("");
			StringBuffer orgIds = new StringBuffer("");
			String positionName = POSITION_NAME;
			Long orgId = -1L;
			List<UserBO> users = null;
			
			for(OrgSocialInfoBO org : orgList) {
				if (org != null) {
					orgId = org.getOrgId();
					users = this.getReportUserList(nodeCodeHandler, positionName, null, "0", orgId, null);
					
					if (users != null && users.size() > 0) {
						StringBuffer userIdTemp = new StringBuffer("");
						for (UserBO user : users) {
							userIdTemp = new StringBuffer("").append(user.getUserId()).append(",");
							if (userIds.indexOf(userIdTemp.toString()) == -1) {// 排除重复的用户
								userIds.append(userIdTemp);
								userNames.append(user.getPartyName()).append(",");
								orgIds.append(orgId).append(",");
							}
						}
					}
				}
			}
			
			if(orgList == null || orgList.size() < 0) {
				resultMap.put("msg", "未配置相关上级组织");
			} else if (userNames != null && userNames.length() > 0) {
				userNames = new StringBuffer(userNames.substring(0, userNames.length() - 1));
				
				resultMap.put("userIds", userIds);
				resultMap.put("userNames", userNames);
				resultMap.put("orgIds", orgIds);
			} else {
				resultMap.put("msg", "职位未配置相关人员");
			}
			
			resultMap.put("isSelectUser", true);
			//办理页面是否显示组织选中 true为是，false为否
			resultMap.put("isSelectOrg", false);
        }
        

        return resultMap;
    }
}
