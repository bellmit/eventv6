package cn.ffcs.zhsq.reportFocus.threeOneTreatment.service.impl;

public enum FocusReportNode364Enum {
    WORKFLOW_TYPE_ID("focus_report", "流程类型"),
    FORM_TYPE("threeOneTreatment", "流程表单类型"),
    FORM_TYPE_ID("364", "流程表单ID"),
    START_NODE_NAME("start", "流程开始"),
    FIND_NODE_NAME("task1", "发现现象"),
    DISTRUBUTE_TASK_NODE_NAME("task2", "任务派发"),
    VERIFY_FEEDBACK_NODE_NAME("task3", "核实反馈"),
    COMMUNITY_HANDLE_NODE_NAME("task4", "一级网格处置"),
    STREET_HANDLE_NODE_NAME("task5", "镇级处置"),
    MUNICIPAL_HANDLE_NODE_NAME("task6","市级处置"),
    ACCEPTANCE_NODE_NAME("task7","组织验收"),
    END_NODE_NAME("end1", "归档");

    private String taskNodeName;
    private String taskNodeNameZH;

    private FocusReportNode364Enum(String taskNodeName, String taskNodeNameZH) {
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
