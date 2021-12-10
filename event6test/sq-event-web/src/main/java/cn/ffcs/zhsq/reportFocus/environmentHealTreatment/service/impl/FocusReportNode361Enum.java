package cn.ffcs.zhsq.reportFocus.environmentHealTreatment.service.impl;

/**
 * @Description: 南安环境卫生处置流程(361)流程节点名称
 * @ClassName:   FocusReportNode361Enum
 * @author:      ztc
 * @date:        2021年6月1日
 */
public enum FocusReportNode361Enum {
    WORKFLOW_TYPE_ID("focus_report", "流程类型"),
    FORM_TYPE("environmentHealTreatment", "流程表单类型"),
    FORM_TYPE_ID("361", "流程表单ID"),
    START_NODE_NAME("start", "流程开始"),
    FIND_NODE_NAME("task1", "发现问题"),
    COMMUNITY_HANDLE_NODE_NAME("task2", "一级网格处置"),
    DISTRUBUTE_TASK_NODE_NAME("task3", "任务派发"),
    STREET_HANDLE_NODE_NAME("task4", "镇级处置"),
    ADMINISTRATION_BUREAU_HANDLE_NODE_NAME("task5","城市管理局处置"),
    RELEVANT_UNIT_HANDLE_NODE_NAME("task6","市直单位处置"),
    END_NODE_NAME("end1", "归档");

    private String taskNodeName;
    private String taskNodeNameZH;

    private FocusReportNode361Enum(String taskNodeName, String taskNodeNameZH) {
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
