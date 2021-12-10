package cn.ffcs.zhsq.keyelement.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;

/**
 * @Description: 江西省吉安市吉州区码上办(Ji ZHou Qu Code) 人员选择处理类
 * @ClassName:   FiveKeyElement4JiZHouQuCodeServiceImpl   
 * @author:      张联松(zhangls)
 * @date:        2021年11月5日 下午4:18:08
 */
@Service(value = "fiveKeyElement4JiZHouQuCodeService")
public class FiveKeyElement4JiZHouQuCodeServiceImpl extends FiveKeyElement4JXPlatformServiceImpl {
	private static final String COMMUNITY_UNIT_ACCEPT_NODE_CODE = "task4";	//村社区综治中心受理
	
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
		
		if(COMMUNITY_UNIT_ACCEPT_NODE_CODE.equals(nodeName)) {
			resultMap.put("isSelectOrg", true);
			resultMap.put("eventNodeCode", nodeCodeHandler);
		} else {
			resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
		}
		
		return resultMap;
	}
}
