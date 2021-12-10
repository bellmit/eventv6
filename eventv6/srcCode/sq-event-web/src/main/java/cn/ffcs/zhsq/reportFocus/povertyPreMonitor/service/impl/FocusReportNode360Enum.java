package cn.ffcs.zhsq.reportFocus.povertyPreMonitor.service.impl;

/**
 * @Description: 南安致贫返贫监测信息流程(360)流程节点名称
 * @ClassName:   FocusReportNode360Enum
 * @author:      ztc
 * @date:        2021年6月1日
 */
public enum FocusReportNode360Enum {
    WORKFLOW_TYPE_ID("focus_report", "流程类型"),
    FORM_TYPE("povertyPreMonitor", "流程表单类型"),
    FORM_TYPE_ID("360", "流程表单ID"),
    START_NODE_NAME("start", "流程开始"),
    FIND_NODE_NAME("task1", "发现致贫返贫风险人员"),
    DISTRUBUTE_TASK_NODE_NAME("task2", "一级网格长任务派发"),
    GRID_FEEDBACK_NODE_NAME("task3", "二级网格长反馈情况"),
    COMMUNITY_COMMENT_NODE_NAME("task4", "村级评议"),
    STREET_AUDIT_NODE_NAME("task5","镇级审核"),
    MUNICIPAL_SEND_NODE_NAME("task6","市扶贫办任务派发"),
    SCREENING_FEEDBACK_NODE_NAME("task7","筛查反馈"),
    DETERMINE_OBJECT_NODE_NAME("task8","对象确定"),
    DE_MONITORING_NODE_NAME("task9","解除致贫返贫监测"),
    CONTINUOUS_MONITORING_NODE_NAME("task0","持续监测"),
    END_NODE_NAME("end1", "归档");

    private String taskNodeName;
    private String taskNodeNameZH;

    private FocusReportNode360Enum(String taskNodeName, String taskNodeNameZH) {
        this.taskNodeName = taskNodeName;
        this.taskNodeNameZH = taskNodeNameZH;
    }

    public String getTaskNodeName() {
        return taskNodeName;
    }

    public String getTaskNodeNameZH() {
        return taskNodeNameZH;
    }

    @Override
    public String toString() {
        return taskNodeName;
    }

}
