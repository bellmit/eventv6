package cn.ffcs.zhsq.reportFocus.threeOneTreatment.service.impl;

public enum FocusReportNode36401Enum {
    FORM_TYPE_ID("36401", "流程表单ID"),
    START_NODE_NAME("start", "流程开始"),
    DECOMPOSITION_TRACKING_TASK_NODE_NAME("task1", "分解跟踪任务"),
    PERIODIC_REPORTS_HDSTATUS_NODE_NAME("task2", "定期报告隐患状态"),
    END_NODE_NAME("end1", "结束");

    private String taskNodeName;
    private String taskNodeNameZH;

    private FocusReportNode36401Enum(String taskNodeName, String taskNodeNameZH) {
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
