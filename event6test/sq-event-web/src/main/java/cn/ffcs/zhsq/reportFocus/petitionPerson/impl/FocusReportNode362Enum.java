package cn.ffcs.zhsq.reportFocus.petitionPerson.impl;

/**
 * @Description: 信访人员稳控(359)流程节点名称
 * @ClassName:   FocusReportNode362Enum   
 * @author:      郭昱丹(guoyd)
 * @date:        2021年5月7日 上午10:16:15
 */
public enum FocusReportNode362Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("petitionPerson", "流程类型表单"),
	FORM_TYPE_ID("362", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	FIND_NODE_NAME("task1", "发现问题"),
	DISTRIBUTE_NODE_NAME("task2", "任务派发"),
	TODO_NODE_NAME("task3", "任务处置"),
	DOING_NODE_NAME("task0", "处理中"),
	FEEDBACK_NODE_NAME("task4", "情况反馈"),
	DISTRIBUTE_NODE_NAME2("task5", "任务派发"),
	DISTRIBUTE_NODE_NAME3("task6", "任务派发"),
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode362Enum(String taskNodeName, String taskNodeNameZH) {
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
