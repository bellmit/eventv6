package cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl;

/**
 * @Description:三会一课节点枚举类
 * @Author: zhangtc
 * @Date: 2020/12/4 10:02
 */
public enum FocusReportNode355Enum {
    WORKFLOW_TYPE_ID("focus_report", "流程类型"),
    FORM_TYPE("meetingsAndLesson", "流程表单类型"),
    FORM_TYPE_ID("355", "流程表单ID"),
    START_NODE_NAME("start", "流程开始"),
    //决策1
    VERIFY_DECISION_NODE_NAME("decision1", "决策1"),
    COLLECT_MEETING("task1", "会议采集"),
    //二级网格员上报
    GRID_REPORT("task2", "会议上报"),
    //一级网格员上报
    COMMUNITY_REPORT("task3", "会议上报"),
    //镇直单位党组织组织委员上报
    STREET_COMMITTEE_REPORT("task4", "会议上报"),
    //镇直单位党组织党小组组长上报
    STREET_GROUPLEADER_REPORT("task5", "会议上报"),
    //市直单位党组织组织委员
    CITY_COMMITTEE_TYPE("task6", "会议上报"),
    //市直单位各党小组组长
    CITY_GROUPLEADER_TYPE("task7", "会议上报"),

    PASS_NODE_TASK2("task9", "组织书记处理"),
    PASS_NODE_TASK3("task8", "组织委员处理"),
    PASS_NODE_TASK4("task10", "组织委员处理"),
    PASS_NODE_TASK5("task11", "组织书记处理"),
    PASS_NODE_TASK6("task12", "工作领导处理"),
    PASS_NODE_TASK7("task13", "组织书记处理"),

    END_NODE_NAME("end1", "归档");

    private String taskNodeName;
    private String taskNodeNameZH;

    private FocusReportNode355Enum(String taskNodeName, String taskNodeNameZH) {
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
