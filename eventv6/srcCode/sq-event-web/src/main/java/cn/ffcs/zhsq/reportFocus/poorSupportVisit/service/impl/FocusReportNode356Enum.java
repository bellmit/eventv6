package cn.ffcs.zhsq.reportFocus.poorSupportVisit.service.impl;

/**
 * @Description: 扶贫走访(356)流程节点名称
 * @ClassName:   FocusReportNode356Enum   
 * @author:      张联松(zhangls)
 * @date:        2020年12月4日 上午9:18:30
 */
public enum FocusReportNode356Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("poorSupportVisit", "流程表单类型"),
	FORM_TYPE_ID("356", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	FILL_REPORT_NODE_NAME("task1", "填写走访报告"),
	DISTRIBUTE_TASK_NODE_NAME("task2", "乡镇（街道）扶贫办负责人任务派发"),
	
	DECISION_NODE_NAME("decision1", "决策1"),
	HRP_VERIFY_NODE_NAME("task3", "挂钩帮扶责任人核实反馈"),//Hook Responsible Person
	COMMUNITY_VERIFY_NODE_NAME("task4", "第一网格长（驻村领导）核实反馈"),
	
	STREET_HANDLE_TASK_NODE_NAME("task5", "乡镇（街道）扶贫办负责人处置"),
	COUNTY_HANDLE_TASK_NODE_NAME("task6", "市扶贫办处置"),
	COUNTY_DEPARTMENT_HANDLE_TASK_NODE_NAME("task7", "市直职能部门处置"),
	
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode356Enum(String taskNodeName, String taskNodeNameZH) {
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
