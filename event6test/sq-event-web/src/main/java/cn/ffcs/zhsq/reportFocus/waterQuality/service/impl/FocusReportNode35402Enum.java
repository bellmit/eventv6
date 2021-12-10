package cn.ffcs.zhsq.reportFocus.waterQuality.service.impl;

/**
 * @Description: 南安流域水质信息流程_非网格员(35402)流程节点名称
 * @ClassName:   FocusReportNode35402Enum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月19日 下午2:49:57
 */
public enum FocusReportNode35402Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("waterQuality", "流程表单类型"),
	FORM_TYPE_ID("35402", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	FIND_NODE_NAME("task1", "发现疑似流域水质问题"),
	BEE_HANDLE_NODE_NAME("task2", "生态环境局处理"),
	BEE_BRIGADE_DISTRIBUTE_NODE_NAME("task3", "生态环境局监测大队任务派发"),
	BEE_SQUADRON_VERIFY_NODE_NAME("task4", "生态环境局监测属地中队核实"),
	BEE_BRIGADE_HANDLE_NODE_NAME("task5", "生态环境局监察大队问题处置"),
	FILE_CASE_NODE_NAME("task6", "立案查处"),
	ADMINISTRTIVE_SACTION_NODE_NAME("task7", "行政处罚"),
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode35402Enum(String taskNodeName, String taskNodeNameZH) {
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
