package cn.ffcs.zhsq.decisionMaking;

import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.eventExtend.service.IEventExtendService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description:
 * @Author: zhangtc
 * @Date: 2019/7/8 15:54
 */
@Service(value = "workflowNCHZHDDDecisionMakingService")
public class WorkflowNCHZHDDDecisionMakingServiceImpl extends WorkflowEventDecisionMakingServiceImpl{

    @Autowired
    private IBaseWorkflowService baseWorkflowService;

    @Autowired
    private IEventExtendService eventExtendService;

    //巡防类型
    private static final String CROSSING_PATROL_TYPE = "1";

    protected static final String START_NODE_CODE = "start";				//结案环节节点编码

    //决策1
    protected static final String DECISION_MAKING_NODE_CODE = "decision1";	//决策1环节节点编码
    protected static final String COLLECT_NODE_CODE = "task1";				//采集事件环节节点编码
    protected static final String CLOSE_NODE_CODE = "task8";				//结案环节节点编码

    //决策2
    private static final String DECISION_MAKING_NODE_CODE_2 = "decision2";				//决策2环节节点编码
    private static final String EVALUATE_NODE_CODE = "task9";				            //评价环节
    private static final String CONFIRM_NODE_CODE = "task13";				            //复核环节

    @Override
    public String makeDecision(Map<String, Object> params) throws Exception {

        String curNodeCode = "";	//当前环节节点编码
        String nextNodeCode = "";	//下一环节节点编码
        Long instanceId = -1L;
        Long eventId = -1L;

        if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
            curNodeCode = params.get("curNodeCode").toString();
        } else {
            throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
        }

        //决策1
        if(DECISION_MAKING_NODE_CODE.equals(curNodeCode) || COLLECT_NODE_CODE.equals(curNodeCode) || START_NODE_CODE.equals(curNodeCode)) {
            return super.makeDecision(params);
        } else if(DECISION_MAKING_NODE_CODE_2.equals(curNodeCode) || CLOSE_NODE_CODE.equals(curNodeCode)){
            String patrolType = null;

            if(CommonFunctions.isNotBlank(params,"instanceId")){
                instanceId = Long.valueOf(String.valueOf(params.get("instanceId")));
            }else {
                throw new Exception("决策类出错，流程实例【instanceId】为空，请检查！");
            }
            //决策2节点，巡防类型为交叉巡防时，下一环节为复核节点；否则为评价环节
            if(instanceId > 0){
                ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
                if(null != proInstance && null != proInstance.getFormId()){
                    eventId = proInstance.getFormId();
                }
                Map<String,Object> eventMap = eventExtendService.findEventExtendByEventId(eventId);
                if(CommonFunctions.isNotBlank(eventMap,"patrolType")){
                    patrolType = String.valueOf(eventMap.get("patrolType"));
                }else {
                    throw new Exception("未查询到事件的巡防类型，请检查！");
                }
            }

            if(CROSSING_PATROL_TYPE.equals(patrolType)){
                nextNodeCode = CONFIRM_NODE_CODE;
            }else {
                nextNodeCode = EVALUATE_NODE_CODE;
            }

        }

        if(StringUtils.isBlank(nextNodeCode)) {
            throw new Exception("没有可用的下一环节！");
        }

        return nextNodeCode;
    }
}
