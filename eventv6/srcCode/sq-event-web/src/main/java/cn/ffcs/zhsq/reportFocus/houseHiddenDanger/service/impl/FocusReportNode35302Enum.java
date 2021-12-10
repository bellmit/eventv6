package cn.ffcs.zhsq.reportFocus.houseHiddenDanger.service.impl;

/**
 * @Description: 南安房屋安全隐患信息流程_突发(35302)流程节点名称
 * @ClassName:   FocusReportNode35302Enum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月17日 下午6:05:22
 */
public enum FocusReportNode35302Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("houseHiddenDanger", "流程表单类型"),
	FORM_TYPE_ID("35302", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	FIND_HHD_NODE_NAME("task1", "发现突发房屋安全隐患"),
	COMMUNITY_HANDLE_NODE_NAME("task2", "第一网格长(驻村领导)处置"),
	STREET_HANDLE_NODE_NAME("task3", "乡镇(街道)任务派发"),
	GRID_HANDLE_NODE_NAME("task99", "二级网格长跟踪报告"),
	VERIFY_DECISION_NODE_NAME("decision1", "决策1"),
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode35302Enum(String taskNodeName, String taskNodeNameZH) {
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
