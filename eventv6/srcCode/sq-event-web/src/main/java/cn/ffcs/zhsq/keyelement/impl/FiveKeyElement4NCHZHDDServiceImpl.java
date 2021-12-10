package cn.ffcs.zhsq.keyelement.impl;

import cn.ffcs.uam.bo.UserInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description:南昌市指挥调度人员选择处理类
 * @Author: zhangtc
 * @Date: 2019/8/1 16:55
 */
@Service(value = "fiveKeyElement4NCHZHDDService")
public class FiveKeyElement4NCHZHDDServiceImpl extends FiveKeyElementForEventServiceImpl {
	
	//需要设置事件办结时限的环节
    private static final String DISTRICT_NODE_CODE = "task5";	//县区处理环节
    private static final String TOWNDISPOSAl_NODE_CODE = "task4";	//乡镇处理环节
    private static final String DISTRICT_DEPARTMENT_NODE_CODE = "task7";		//区职能部门处理环节

    @Override
    public Map<String, Object> getNodeInfoForEvent(
            UserInfo userInfo,
            String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
            throws Exception {
        Map<String, Object> resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);

        //是否可上传处理后图片
        resultMap.put("isUploadHandledPic", true);
        
        //判断是否展示设置时限按钮
        if(DISTRICT_NODE_CODE.equals(curnodeName)&&(TOWNDISPOSAl_NODE_CODE.equals(nodeName)||DISTRICT_DEPARTMENT_NODE_CODE.equals(nodeName))) {
        	resultMap.put("isShowInterval", true);
        }
        

        return resultMap;
    }
}
