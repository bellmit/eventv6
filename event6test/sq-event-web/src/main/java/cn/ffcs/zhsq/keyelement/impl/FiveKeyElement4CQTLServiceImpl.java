package cn.ffcs.zhsq.keyelement.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.keyelement.EventNodeCode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:重庆铜梁区个性化五要素服务类
 * @Author: youwj
 * @Date: 2019/8/1 16:55
 */
@Service(value = "fiveKeyElement4CQTLService")
public class FiveKeyElement4CQTLServiceImpl extends FiveKeyElementForEventServiceImpl {
	
	private static final String INSPECTION_NODE_CODE = "task7";	//督导组抽查

    @Override
    public Map<String, Object> getNodeInfoForEvent(
            UserInfo userInfo,
            String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
            throws Exception {
        Map<String, Object> resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
        //如果下一环节是督导组抽查，那么相当于归档
        if(INSPECTION_NODE_CODE.equals(nodeName)) {
        	if(CommonFunctions.isNotBlank(resultMap, "eventNodeCode")) {
        		EventNodeCode object = (EventNodeCode) resultMap.get("eventNodeCode");
        		object.setPlaceFile(true);
        		
        		resultMap.put("eventNodeCode", object);
        	}
        }

        return resultMap;
    }
}
