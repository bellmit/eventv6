package cn.ffcs.zhsq.reportFocus.forestFirePrevention.service.impl;

/**
 * @Description: 森林防灭火(358)流程节点名称
 * @ClassName:   FocusReportNode358Enum   
 * @author:      张联松(zhangls)
 * @date:        2021年3月31日 上午10:38:36
 */
public enum FocusReportNode358Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("forestFirePrevention", "流程类型表单"),
	FORM_TYPE_ID("358", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	FIND_NODE_NAME("task1", "发现疑似森林火灾隐患"),
	DISTRIBUTE_NODE_NAME("task2", "任务派发"),
	VERIFY_NODE_NAME("task3", "现场核实"),
	COMMUNITY_HANDLE_NODE_NAME("task4", "一级网格处置"),
	STREET_HANDLE_NODE_NAME("task5", "镇级增员处置"),
	DISTRICT_HANDLE_NODE_NAME("task6", "市级支援处置"),
	STREET_ARCHIVE_NODE_NAME("task7", "镇级处置"),
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode358Enum(String taskNodeName, String taskNodeNameZH) {
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
