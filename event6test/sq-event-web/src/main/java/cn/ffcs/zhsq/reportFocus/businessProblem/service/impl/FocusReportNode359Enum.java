package cn.ffcs.zhsq.reportFocus.businessProblem.service.impl;

/**
 * @Description: 营商问题(359)流程节点名称
 * @ClassName:   FocusReportNode359Enum   
 * @author:      郭昱丹(guoyd)
 * @date:        2021年5月7日 上午10:16:15
 */
public enum FocusReportNode359Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("businessProblem", "流程类型表单"),
	FORM_TYPE_ID("359", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	FIND_NODE_NAME("task1", "发现问题"),
	DISTRIBUTE_NODE_NAME("task2", "任务派发"),
	LEAD_NODE_NAME("task3", "牵头部门处置"),
	DOING_NODE_NAME("task0", "处理中"),
	MUNICIPAL_NODE_NAME("task4", "市直部门处置"),
	OFFICE_NODE_NAME("task5", "市招商办处置"),
	DELAY_NODE_NAME("task6", "延期意见反馈"),
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode359Enum(String taskNodeName, String taskNodeNameZH) {
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
