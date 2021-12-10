package cn.ffcs.zhsq.decisionMaking;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description:松溪县城管局事件流程决策类
 * @Author: zhangtc
 * @Date: 2018/12/26 15:26
 */
@Service(value = "workflowEventDecisionMaking4SXXService")
public class WorkflowEventDecisionMaking4SXXServiceImpl extends WorkflowEventDecisionMakingServiceImpl{

    @Autowired
    private OrgSocialInfoOutService orgSocialInfoService;

    protected static final String STREET_NODE_CODE = "task2";					//乡镇(街道)处理环节
    protected static final String DISTRICT_NODE_CODE = "task3";				    //县(区)处理环节

    @Override
    public String makeDecision(Map<String, Object> params) throws Exception {

        String curNodeCode = "",	//当前环节节点编码
                nextNodeCode = "";	//下一环节节点编码

        if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
            curNodeCode = params.get("curNodeCode").toString();
        } else {
            throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
        }

        if(DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode)) {//决策
            Long curOrgId = -1L;		//当前用户所属组织
            OrgSocialInfoBO orgInfo = null;	//当前组织信息

            if(CommonFunctions.isNotBlank(params, "curOrgId")) {
                try {
                    curOrgId = Long.valueOf(params.get("curOrgId").toString());
                } catch(NumberFormatException e) {
                    throw new NumberFormatException("参数[curOrgId]："+params.get("curOrgId")+" 不能转换为Long型！");
                }

                orgInfo = orgSocialInfoService.findByOrgId(curOrgId);
                if (orgInfo == null) {
                    throw new Exception("参数[curOrgId]："+params.get("curOrgId")+" 没有对应的组织信息！");
                } else {
                    String orgType = orgInfo.getOrgType(),
                           chiefLevel = orgInfo.getChiefLevel();

                    if (!String.valueOf(ConstantValue.ORG_TYPE_DEPT).equals(orgType)) {
                        throw new Exception("当前用户的组织类型有误：不是部门。请先修正！");
                    }
                    if (ConstantValue.STREET_ORG_LEVEL.equals(chiefLevel)) {
                        nextNodeCode = STREET_NODE_CODE;
                    } else if (ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevel)) {
                        nextNodeCode = DISTRICT_NODE_CODE;
                    }
                }

            } else {
                throw new IllegalArgumentException("缺失参数[curOrgId]，请检查！");
            }
        }

        if(StringUtils.isBlank(nextNodeCode)) {
            throw new Exception("没有可用的下一环节！");
        }

        return nextNodeCode;
    }
}
