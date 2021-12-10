package cn.ffcs.zhsq.reportFocus.houseHiddenDanger.service.impl;

/**
 * @Description: 南安房屋安全隐患信息流程_突发_定期报告(3530201)流程节点名称
 * @ClassName:   FocusReportNode3530201Enum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月25日 下午3:03:17
 */
public enum FocusReportNode3530201Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE_ID("3530201", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	DISTRIBUTE_NODE_NAME("task1", "第一网格长(驻村领导)任务派发"),
	GRID_ROUTINE_NODE_NAME("task2", "二级网格长(一级网格员)定期报告安全隐患状态"),
	COMMUNITY_HANDLE_NODE_NAME("task3", "第一网格长(驻村领导)处置"),
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode3530201Enum(String taskNodeName, String taskNodeNameZH) {
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
