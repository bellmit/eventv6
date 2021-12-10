package cn.ffcs.zhsq.reportFocus.martyrsFacility.impl;

/**
 * @Description: 信访人员稳控(359)流程节点名称
 * @ClassName:   FocusReportNode362Enum   
 * @author:      郭昱丹(guoyd)
 * @date:        2021年5月7日 上午10:16:15
 */
public enum FocusReportNode363Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("martyrsFacility", "流程类型表单"),
	FORM_TYPE_ID("363", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	FIND_NODE_NAME("task1", "发现问题"),
	DISTRIBUTE_NODE_NAME("task2", "交办任务"),
	FEEDBACK_NODE_NAME("task3", "问题反馈"),
	TODO_NODE_NAME("task4", "问题处置"),
	TODO_NODE_NAME2("task5", "镇级处置"),
	ACCEPTANCE_NODE_NAME("task6", "组织验收"),
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode363Enum(String taskNodeName, String taskNodeNameZH) {
		this.taskNodeName = taskNodeName;
		this.taskNodeNameZH = taskNodeNameZH;
	}

	public String getTaskNodeName() {
		return taskNodeName;
	}
	
	public String getTaskNodeNameZH() {
		return taskNodeNameZH;
	}
	
	public String toString() {
		return taskNodeName;
	}
}
