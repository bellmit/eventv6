package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.shequ.zzgl.service.event.IEvaResultService;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.decisionMaking.IWorkflowDecisionMakingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExpand.service.IEventDisposal4ExpandService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:海南省万宁市工作流处理类
 * @Author: zhangtc
 * @Date: 2019/3/14 14:43
 */
@Service(value = "eventDisposalWorkflow4WNSService")
public class EventDisposalWorkflow4WNSServiceImpl extends EventDisposalWorkflowForEventNewServiceImpl{

    @Autowired
    private OrgSocialInfoOutService orgSocialInfoService;

    @Autowired
    private IEvaResultService evaResultService;

    @Autowired
    private IBaseWorkflowService baseWorkflowService;
    
    @Autowired
	private IEventDisposalService eventDisposalService;
    
    @Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;

    @Autowired
    private IWorkflowDecisionMakingService eventStatusDecisionMakingService;

    //事件扩展信息接口
    @Autowired
    private IEventDisposal4ExpandService eventDisposalExpandService;

    private static final String COLLECT_NODE_CODE = "task1";//采集环节
    private static final String DISTRICT_NODE_CODE = "task5";//县(区)处理
    private static final String CLOSE_NODE_CODE = "task8";//结案环节编码
    private static final String END_NODE_CODE = "end1";//归档环节节点编码



    @Override
    public String startFlowByWorkFlow(Long eventId, Long workFlowId, String toClose, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {

        String instanceId = null,
               chiefLevel = "";
        OrgSocialInfoBO orgInfo = null;

        //县区采集并结案时，直接归档
        //判断当前用户的组织层级
        if (null != userInfo.getOrgId()) {
            orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
            if (null != orgInfo) {
                chiefLevel = orgInfo.getChiefLevel();
            }
        }

        //县区采集并结案时，直接归档--TODO之后县区采集并结案不做变化需要将归档代码单独抽成一个方法
        if (ConstantValue.WORKFLOW_IS_TO_COLSE.equals(toClose) && ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevel)) {
            instanceId = super.startFlowByWorkFlow(eventId, workFlowId, ConstantValue.WORKFLOW_IS_NO_CLOSE, userInfo, extraParam);

            //对采集并结案的事件进行归档
            if (baseWorkflowService.endWorkflow4Base(Long.valueOf(instanceId),userInfo,extraParam)){
                Map<String,Object> params = new HashMap<>();
                params.put("eventId",eventId);
                params.put("curNodeCode",END_NODE_CODE);
                params.put("nextNodeCode",END_NODE_CODE);
                params.put("userId",userInfo.getUserId());
                params.put("orgId",userInfo.getOrgId());
                //更新事件状态为结束
                eventStatusDecisionMakingService.makeDecision(params);

                //采集并结案的事件默认添加评价信息，评价等级为：满意
                String evaLevel = "02";
                String evaContent = "满意";
                try {
                    eventDisposalExpandService.saveOrUpdateEventEvaluate(userInfo,eventId,evaLevel,evaContent,params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            instanceId = super.startFlowByWorkFlow(eventId, workFlowId, toClose, userInfo, extraParam);
        }


        return instanceId;
    }

    @Override
    public boolean subWorkFlowForEndingAndEvaluate(Long instanceId, String taskId, String nextNodeName, String nextStaffId, String curOrgIds, String advice, UserInfo userInfo, String smsContent, Map<String, Object> extraParam) throws Exception {

        boolean flag = false;
        String chiefLevel = "";
        OrgSocialInfoBO orgInfo = null;

        //县区采集并结案时，直接归档（当前环节为县区环节处理，当前环节的前一环节为事件采集（task1），并且下一环节为结案环节）
        //判断当前用户的组织层级
        if (null != userInfo.getOrgId()) {
            orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
            if (null != orgInfo) {
                chiefLevel = orgInfo.getChiefLevel();
            }
        }

        //当前环节为县区环节处理，下一环节为结案环节时，并且当前环节的前一环节为事件采集（task1），提交完工作流之后直接归档事件
        if (CLOSE_NODE_CODE.equals(nextNodeName) && ConstantValue.DISTRICT_ORG_LEVEL.equals(chiefLevel)){

            //降序获取历史环节信息，第一个环节信息为当前环节curNodeName
            List<Map<String,Object>> taskMapList = super.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_DESC,userInfo);
            Map<String,Object> curNodeMap  = null;//当前环节信息
            Map<String,Object> collectNodeMap  = null;//采集环节信息
            String curTaskNode = null;
            String collectTaskNode = null;
            if(null == extraParam){
                extraParam = new HashMap<>();
            }
            if(null != taskMapList && taskMapList.size() > 1){
                curNodeMap = taskMapList.get(0);
                collectNodeMap = taskMapList.get(1);
                if(CommonFunctions.isNotBlank(curNodeMap,"TASK_CODE")){
                    curTaskNode = String.valueOf(curNodeMap.get("TASK_CODE"));
                }
                if(CommonFunctions.isNotBlank(collectNodeMap,"TASK_CODE")){
                    collectTaskNode = String.valueOf(collectNodeMap.get("TASK_CODE"));
                }
            }

            if(DISTRICT_NODE_CODE.equals(curTaskNode) && COLLECT_NODE_CODE.equals(collectTaskNode)){
                ProInstance pro = baseWorkflowService.findProByInstanceId(Long.valueOf(instanceId.toString()));
                Long eventId = -1L;

                extraParam.put("advice",advice);
                if (null != pro){
                    eventId = pro.getFormId();
                }
                //对采集并结案的事件进行归档
                if (baseWorkflowService.endWorkflow4Base(Long.valueOf(instanceId),userInfo,extraParam)){
                    Map<String,Object> params = new HashMap<>();
                    params.put("eventId",eventId);
                    params.put("curNodeCode",END_NODE_CODE);
                    params.put("nextNodeCode",END_NODE_CODE);
                    params.put("userId",userInfo.getUserId());
                    params.put("orgId",userInfo.getOrgId());
                    //更新事件状态为结束
                    eventStatusDecisionMakingService.makeDecision(params);

                    //采集并结案的事件默认添加评价信息，评价等级为：满意
                    String evaLevel = "02";
                    String evaContent = "满意";
                    try {
                        eventDisposalExpandService.saveOrUpdateEventEvaluate(userInfo,eventId,evaLevel,evaContent,params);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            } else {
                extraParam.put("isEvaluate",true);
            }
        }

       return super.subWorkFlowForEndingAndEvaluate(instanceId, taskId, nextNodeName, nextStaffId, curOrgIds, advice, userInfo , smsContent, extraParam);
    }

}
