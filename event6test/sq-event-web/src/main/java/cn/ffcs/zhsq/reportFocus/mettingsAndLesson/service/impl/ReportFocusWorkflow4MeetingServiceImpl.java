package cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.workflow.spring.IBaseWorkflowService;
import cn.ffcs.zhsq.base.workflow.IWorkflow4BaseService;
import cn.ffcs.zhsq.reportFocus.IReportFocusService;
import cn.ffcs.zhsq.reportFocus.service.impl.ReportTypeEnum;
import cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl.ReportFocusWorkflow4TwoVioPreServiceImpl;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:三会一课工作流相关接口
 * @Author: zhangtc
 * @Date: 2020/12/4 9:44
 */
@Service(value = "reportFocusWorkflow4MeetingService")
public class ReportFocusWorkflow4MeetingServiceImpl extends ReportFocusWorkflow4TwoVioPreServiceImpl {

    @Autowired
    private IBaseWorkflowService baseWorkflowService;

    @Autowired
    private OrgSocialInfoOutService orgSocialInfoService;

    @Autowired
    private IReportFocusService reportFocusService;

    @Override
    public boolean startWorkflow4Report(Long reportId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
        boolean flag = false;
        Map<String,Object> reportFocusMap = null;

        //根据事件采集来源判断当前用户组织层级是否可以启动流程，来源和组织层级要一一对应，否则主送人员会匹配不上
        //collectSource 组织层级
        //01：5 ；02：6 ；03：4 ；04：4 ；05：3 ；06：3
        String orgChiefLevel = null,collectSource = null;
        OrgSocialInfoBO orgInfo = null;

        if(null != userInfo){
            orgInfo = orgSocialInfoService.findByOrgId(userInfo.getOrgId());
            if(null != orgInfo){
                orgChiefLevel = orgInfo.getChiefLevel();
            }
        }

        if(CommonFunctions.isNotBlank(extraParam,"collectSource")){
            collectSource = String.valueOf(extraParam.get("collectSource"));
        }else if(null != reportId && reportId> 0){
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.putAll(extraParam);
            paramsMap.put("isWithReportFocus",false);
            reportFocusMap = reportFocusService.findReportFocusByUUID(null, userInfo, paramsMap);

            if(CommonFunctions.isNotBlank(reportFocusMap,"collectSource")){
                collectSource = String.valueOf(reportFocusMap.get("collectSource"));
            }
        }

        if(MeetingCollectSourceEnum.COMMUNITY_REPORT.getCollectSource().equals(collectSource)
                && !ConstantValue.COMMUNITY_ORG_LEVEL.equals(orgChiefLevel)){
            //按钮权限适配的组织层级为【村/社区】，用户组织层级为非【村/社区】：按钮配置错误校验
            throw new ZhsqEventException("当前用户没有权限新增该类型的会议！");
        }else if(MeetingCollectSourceEnum.GRID_REPORT.getCollectSource().equals(collectSource)
                && !ConstantValue.GRID_ORG_LEVEL.equals(orgChiefLevel)){
            //按钮权限适配的组织层级为【网格】，用户组织层级为非【村/社区】：按钮配置错误校验
            throw new ZhsqEventException("当前用户没有权限新增该类型的会议！");
        }else if((MeetingCollectSourceEnum.STREET_COMMITTEE_REPORT.getCollectSource().equals(collectSource)||MeetingCollectSourceEnum.STREET_GROUPLEADER_REPORT.getCollectSource().equals(collectSource))
                && !ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel)){
            //按钮权限适配的组织层级为【镇街职能部门】，用户组织层级为非【村/社区】：按钮配置错误校验
            throw new ZhsqEventException("当前用户没有权限新增该类型的会议！");
        }else if((MeetingCollectSourceEnum.CITY_COMMITTEE_TYPE.getCollectSource().equals(collectSource)||MeetingCollectSourceEnum.CITY_GROUPLEADER_TYPE.getCollectSource().equals(collectSource))
                && !ConstantValue.STREET_ORG_LEVEL.equals(orgChiefLevel)){
            //按钮权限适配的组织层级为【县区职能部门】，用户组织层级为非【村/社区】：按钮配置错误校验
            throw new ZhsqEventException("当前用户没有权限新增该类型的会议！");
        }

        flag = super.startWorkflow4Report(reportId, userInfo, extraParam);

        //流程启动成功、当前环节为会议采集、下一环节为决策、自动提交一步
        if(flag){
            Long instanceId = null;
            String curNodeName = null, nextNodeName = null;
            ProInstance proInstance = null;
            List<UserInfo> nextUserInfoList = new ArrayList<>();

            if(CommonFunctions.isNotBlank(extraParam, "instanceId")) {
                instanceId = Long.valueOf(extraParam.get("instanceId").toString());
            }

            if(instanceId != null && instanceId > 0) {
                proInstance = baseWorkflowService.findProByInstanceId(instanceId);
                if(proInstance != null) {
                    curNodeName = proInstance.getCurNode();
                } else {
                    throw new IllegalArgumentException("流程实例id【"+ instanceId +"】没有找到有效的流程信息！");
                }
            }
            //当前环节为会议采集时，才进行决策
            if(StringUtils.isNotBlank(curNodeName) && FocusReportNode355Enum.COLLECT_MEETING.getTaskNodeName().equals(curNodeName)){
                //提交成功，获取下一可用节点
                extraParam.put("isDecisionMaking",true);
                extraParam.put("curNodeName",curNodeName);
                List<Map<String, Object>> nextTaskNodes = this.findNextTaskNodes(instanceId,userInfo,extraParam);

                if(null != nextTaskNodes && nextTaskNodes.size() == 1){
                    Map<String,Object> taskNode = nextTaskNodes.get(0);
                    if(CommonFunctions.isNotBlank(taskNode,"nodeName")){
                        nextNodeName = String.valueOf(taskNode.get("nodeName"));
                    }
                }
                nextUserInfoList.add(userInfo);

                if(StringUtils.isNotBlank(nextNodeName)){
                    flag = subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);
                }
            }
        }

        return flag;
    }

    //当前环节
    @SuppressWarnings("unchecked")
	@Override
    public Map<String, Object> initWorkflow4Handle(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
        Map<String, Object> initMap = super.initWorkflow4Handle(instanceId, userInfo, params);
        String curNodeName = null;
        String formTypeIdStr = null;

        if(CommonFunctions.isBlank(params, "reportType")) {
            initMap.put("reportType", ReportTypeEnum.waterQuality.getReportTypeIndex());
        }

        if(CommonFunctions.isNotBlank(initMap, "curNodeName")) {
            curNodeName = initMap.get("curNodeName").toString();
        }

        if(CommonFunctions.isNotBlank(initMap, "formTypeId")) {
            formTypeIdStr = initMap.get("formTypeId").toString();
        }

        //过渡环节展示名称变更
        if(StringUtils.isNotBlank(curNodeName)
        		&& FocusReportNode355Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)
                &&(FocusReportNode355Enum.GRID_REPORT.getTaskNodeName().equals(curNodeName)
                ||FocusReportNode355Enum.COMMUNITY_REPORT.getTaskNodeName().equals(curNodeName)
                ||FocusReportNode355Enum.STREET_COMMITTEE_REPORT.getTaskNodeName().equals(curNodeName)
                ||FocusReportNode355Enum.STREET_GROUPLEADER_REPORT.getTaskNodeName().equals(curNodeName)
                ||FocusReportNode355Enum.CITY_COMMITTEE_TYPE.getTaskNodeName().equals(curNodeName)
                ||FocusReportNode355Enum.CITY_GROUPLEADER_TYPE.getTaskNodeName().equals(curNodeName)
        )
        ){
            //nextTaskNodes nodeName nodeNameZH
            List<Map<String,Object>> nextTaskNodes = new ArrayList<>();

            if(CommonFunctions.isNotBlank(initMap,"nextTaskNodes")){
                nextTaskNodes = (List<Map<String, Object>>) initMap.get("nextTaskNodes");
            }
            if(nextTaskNodes.size()>0){
                for(Map<String, Object> taskMap:nextTaskNodes){
                    if(CommonFunctions.isNotBlank(taskMap,"nodeName")){
                        taskMap.put("nodeNameZH","审核");
                    }
                }
            }
        }

        //过渡环节展示名称变更--注释
        if(StringUtils.isNotBlank(curNodeName)
                &&(FocusReportNode355Enum.PASS_NODE_TASK2.getTaskNodeName().equals(curNodeName)
                ||FocusReportNode355Enum.PASS_NODE_TASK3.getTaskNodeName().equals(curNodeName)
                ||FocusReportNode355Enum.PASS_NODE_TASK4.getTaskNodeName().equals(curNodeName)
                ||FocusReportNode355Enum.PASS_NODE_TASK5.getTaskNodeName().equals(curNodeName)
                ||FocusReportNode355Enum.PASS_NODE_TASK6.getTaskNodeName().equals(curNodeName)
                ||FocusReportNode355Enum.PASS_NODE_TASK7.getTaskNodeName().equals(curNodeName)
        )
        ){
            //nextTaskNodes nodeName nodeNameZH
            List<Map<String,Object>> nextTaskNodes = new ArrayList<>();

            if(CommonFunctions.isNotBlank(initMap,"nextTaskNodes")){
                nextTaskNodes = (List<Map<String, Object>>) initMap.get("nextTaskNodes");
            }
            if(nextTaskNodes.size()==2){
                int x = 0;
                for(Map<String, Object> taskMap:nextTaskNodes){
                    if(CommonFunctions.isNotBlank(taskMap,"nodeName")){
                        if(x==0){
                            taskMap.put("nodeNameZH","归档(审核通过)");
                        }else{
                            taskMap.put("nodeNameZH","驳回(审核不通过)");
                        }
                    }
                    x++;
                }
            }
        }

        return initMap;
    }

    //处理详情页，处理环节
    @Override
    public List<Map<String, Object>> capHandledTaskInfoMap(Long instanceId, String order, Map<String, Object> params) throws Exception {
        List<Map<String, Object>> taskInfoMapList = super.capHandledTaskInfoMap(instanceId, order, params);

        if(taskInfoMapList != null) {
            String formTypeIdStr = null;

            if(CommonFunctions.isNotBlank(params, "formTypeId")) {
                formTypeIdStr = params.get("formTypeId").toString();
            } else if(instanceId != null && instanceId > 0) {
                ProInstance proInstance = baseWorkflowService.findProByInstanceId(instanceId);
                if(proInstance != null) {
                    formTypeIdStr = String.valueOf(proInstance.getFormTypeId());
                }
            }

            //三会一课流程 去除“会议采集”、“组织处理环节”
            if(FocusReportNode355Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
                String taskCode = null;
                List<Map<String, Object>> removeTaskInfoList = new ArrayList<Map<String, Object>>();

                for(Map<String, Object> taskInfoMap : taskInfoMapList) {
                    if(CommonFunctions.isNotBlank(taskInfoMap, "TASK_CODE")) {
                        taskCode = taskInfoMap.get("TASK_CODE").toString();
                    }

                    if(FocusReportNode355Enum.COLLECT_MEETING.getTaskNodeName().equals(taskCode)
//                            || FocusReportNode355Enum.PASS_NODE_TASK2.getTaskNodeName().equals(taskCode)
//                            || FocusReportNode355Enum.PASS_NODE_TASK3.getTaskNodeName().equals(taskCode)
//                            || FocusReportNode355Enum.PASS_NODE_TASK4.getTaskNodeName().equals(taskCode)
//                            || FocusReportNode355Enum.PASS_NODE_TASK5.getTaskNodeName().equals(taskCode)
//                            || FocusReportNode355Enum.PASS_NODE_TASK6.getTaskNodeName().equals(taskCode)
//                            || FocusReportNode355Enum.PASS_NODE_TASK7.getTaskNodeName().equals(taskCode)
                    ) {
                        removeTaskInfoList.add(taskInfoMap);
                    }
                }

                taskInfoMapList.removeAll(removeTaskInfoList);
            }

            //归档事件增加虚拟归档环节
            if(taskInfoMapList.size() > 0 && IWorkflow4BaseService.SQL_ORDER_DESC.equals(order)) {
                Map<String, Object> taskMap = taskInfoMapList.get(0);

                if(CommonFunctions.isBlank(taskMap, "IS_CURRENT_TASK")) {
                    Map<String, Object> endTaskMap = new HashMap<String, Object>();

                    endTaskMap.put("TASK_NAME", FocusReportNode355Enum.END_NODE_NAME.getTaskNodeNameZH());

                    taskInfoMapList.add(0, endTaskMap);
                }
            }
        }

        return taskInfoMapList;
    }

    /**
     * 获取流程图名称
     * @param reportId	上报id
     * @param userInfo	操作用户
     * @param params	额外参数
     * @return
     */
    @Override
    protected String findWorkflowName(Long reportId, UserInfo userInfo, Map<String, Object> params) {
        String reportType = null, workflowName = null;

        if(CommonFunctions.isNotBlank(params, "reportType")) {
            reportType = params.get("reportType").toString();
        } else {
            throw new IllegalArgumentException("缺少上报类型【reportType】！");
        }

        //后续转功能配置
        if(ReportTypeEnum.meetingAndLesson.getReportTypeIndex().equals(reportType)) {
            workflowName = "南安三会一课流程";
        }

        return workflowName;
    }

    /**
     * 获取流程类型
     * @param reportId	上报id
     * @param userInfo	操作用户
     * @param params	额外参数
     * @return
     */
    @Override
    protected String findWfTypeId(Long reportId, UserInfo userInfo, Map<String, Object> params) {
        String reportType = null, wfTypeId = null;

        if(CommonFunctions.isNotBlank(params, "reportType")) {
            reportType = params.get("reportType").toString();
        } else {
            throw new IllegalArgumentException("缺少上报类型【reportType】！");
        }

        //后续转功能配置
        if(ReportTypeEnum.meetingAndLesson.getReportTypeIndex().equals(reportType)) {
            wfTypeId = "focus_report";
        }

        return wfTypeId;
    }

    @Override
    public boolean subWorkflow4Report(Long instanceId, String nextNodeName, List<UserInfo> nextUserInfoList, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
        if(CommonFunctions.isNotBlank(extraParam, "nextNodeName")){
            if("end1".equals(extraParam.get("nextNodeName"))){
                extraParam.put("advice", "");
                extraParam.put("adviceNote", "");
            }
        }
        if(CommonFunctions.isNotBlank(extraParam, "nextNode")){
            if ("end1".equals(extraParam.get("nextNode"))){
                extraParam.put("advice", "");
                extraParam.put("adviceNote", "");
            }
        }
        boolean flag = super.subWorkflow4Report(instanceId, nextNodeName, nextUserInfoList, userInfo, extraParam);

        //当前环节为过渡环节时，提交成功之后自动归档
//        if(flag &&
//                (FocusReportNode355Enum.PASS_NODE_TASK2.getTaskNodeName().equals(nextNodeName)
//                        || FocusReportNode355Enum.PASS_NODE_TASK3.getTaskNodeName().equals(nextNodeName)
//                        || FocusReportNode355Enum.PASS_NODE_TASK4.getTaskNodeName().equals(nextNodeName)
//                        || FocusReportNode355Enum.PASS_NODE_TASK5.getTaskNodeName().equals(nextNodeName)
//                        || FocusReportNode355Enum.PASS_NODE_TASK6.getTaskNodeName().equals(nextNodeName)
//                        || FocusReportNode355Enum.PASS_NODE_TASK7.getTaskNodeName().equals(nextNodeName))
//        )
//        {
//            Map<String,Object> curTaskMap = baseWorkflowService.findCurTaskData(instanceId);
//            UserInfo operateUser = new UserInfo();
//
//            if(CommonFunctions.isNotBlank(curTaskMap, "WF_USERID_ALL")) {
//                operateUser.setUserId(Long.valueOf(curTaskMap.get("WF_USERID_ALL").toString().split(",")[0]));
//            }
//            if(CommonFunctions.isNotBlank(curTaskMap, "WF_USERNAME_ALL")) {
//                operateUser.setPartyName(curTaskMap.get("WF_USERNAME_ALL").toString().split(",")[0]);
//            }
//            if(CommonFunctions.isNotBlank(curTaskMap, "WF_ORGID_ALL")) {
//                operateUser.setOrgId(Long.valueOf(curTaskMap.get("WF_ORGID_ALL").toString().split(",")[0]));
//            }
//            if(CommonFunctions.isNotBlank(curTaskMap, "WF_ORGNAME_ALL")) {
//                operateUser.setOrgName(curTaskMap.get("WF_ORGNAME_ALL").toString().split(",")[0]);
//            }
//
//            flag = super.subWorkflow4Report(instanceId, FocusReportNode355Enum.END_NODE_NAME.getTaskNodeName(), null, operateUser, extraParam);
//        }

        return flag;
    }

    @Override
	public boolean rejectWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false, isRejectByParent = true, isBack2Draft = false;
		String formTypeIdStr = null, curNodeName = null;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		if(CommonFunctions.isNotBlank(extraParam, "curNodeName")) {
			curNodeName = extraParam.get("curNodeName").toString();
		}
		if(CommonFunctions.isNotBlank(extraParam, "formTypeId")) {
			formTypeIdStr = extraParam.get("formTypeId").toString();
		}
		
		if(FocusReportNode355Enum.FORM_TYPE_ID.toString().equals(formTypeIdStr)) {
			if(FocusReportNode355Enum.GRID_REPORT.getTaskNodeName().equals(curNodeName)
					|| FocusReportNode355Enum.COMMUNITY_REPORT.getTaskNodeName().equals(curNodeName)
					|| FocusReportNode355Enum.STREET_COMMITTEE_REPORT.getTaskNodeName().equals(curNodeName)
					|| FocusReportNode355Enum.STREET_GROUPLEADER_REPORT.getTaskNodeName().equals(curNodeName)
					|| FocusReportNode355Enum.CITY_COMMITTEE_TYPE.getTaskNodeName().equals(curNodeName)
					|| FocusReportNode355Enum.CITY_GROUPLEADER_TYPE.getTaskNodeName().equals(curNodeName)) {
				isRejectByParent = false;
				isBack2Draft = true;
				
				throw new ZhsqEventException("当前环节不支持驳回操作！");
			}
		}
		
		extraParam.put("isRejectByParent", isRejectByParent);
		extraParam.put("isBack2Draft", isBack2Draft);
		
		flag = super.rejectWorkflow4Report(instanceId, userInfo, extraParam);
		
		return flag;
	}
    
    @Override
	public boolean recallWorkflow4Report(Long instanceId, UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
		boolean flag = false;
		extraParam = extraParam == null ? new HashMap<String, Object>() : extraParam;
		
		extraParam.put("isAble2Recall", false);
		
		flag = super.recallWorkflow4Report(instanceId, userInfo, extraParam);
		
		return flag;
	}
    
    /**
     * 获取下一可办理环节
     * @param instanceId	流程实例id
     * @param userInfo		用户信息
     * @param params		额外参数
     * 			instanceId	流程实例id
     * @return
     * @throws Exception
     */
    @Override
    protected List<Map<String, Object>> findNextTaskNodes(Long instanceId, UserInfo userInfo, Map<String, Object> params) throws Exception {
        List<Map<String, Object>> nodeMapList = null;
        boolean isDecisionMaking = false;
        params = params == null ? new HashMap<String, Object>() : params;
        String curNodeName = null;

        if(CommonFunctions.isNotBlank(params, "curNodeName")) {
            curNodeName = params.get("curNodeName").toString();
        }

        isDecisionMaking = FocusReportNode355Enum.COLLECT_MEETING.getTaskNodeName().equals(curNodeName);

        params.put("isDecisionMaking", isDecisionMaking);

        nodeMapList = super.findNextTaskNodes(instanceId, userInfo, params);

        return nodeMapList;
    }

}
