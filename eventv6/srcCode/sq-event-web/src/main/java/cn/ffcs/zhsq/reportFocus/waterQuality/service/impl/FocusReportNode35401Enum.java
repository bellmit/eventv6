package cn.ffcs.zhsq.reportFocus.waterQuality.service.impl;

/**
 * @Description: 南安流域水质信息流程_网格员(35401)流程节点名称
 * @ClassName:   FocusReportNode35401Enum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月19日 下午2:42:39
 */
public enum FocusReportNode35401Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("waterQuality", "流程表单类型"),
	FORM_TYPE_ID("35401", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	FIND_NODE_NAME("task1", "发现疑似流域水质问题"),
	DISTRUBUTE_TASK_NODE_NAME("task2", "第一网格长(驻村领导)任务派发"),
	VERIFY_TASK_GRID_NODE_NAME("task3", "第一副网格长(驻村工作队长)核实"),
	COMMUNITY_HANDLE_NODE_NAME("task4", "第一网格长(驻村领导)处置"),
	STREET_HANDLE_NODE_NAME("task5", "镇级处置"),
	BEE_HANDLE_NODE_NAME("task6", "生态环境局处置"),//Bureau of Ecological Environment
	FILE_CASE_NODE_NAME("task7", "立案查处"),
	ADMINISTRTIVE_SACTION_NODE_NAME("task8", "行政处罚"),
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode35401Enum(String taskNodeName, String taskNodeNameZH) {
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
