package cn.ffcs.zhsq.reportFocus.petitionPerson.impl;

public enum FocusReportNode36201Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE_ID("36201", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	DISTRIBUTE_NODE_NAME("task1", "子任务派发"),
	FEEDBACK_NODE_NAME("task2", "网格长反馈"),
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode36201Enum(String taskNodeName, String taskNodeNameZH) {
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