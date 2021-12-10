package cn.ffcs.zhsq.reportFocus.waterQuality.service.impl;

/**
 * @Description: 南安流域水质信息流程_突发(35403)流程节点名称
 * @ClassName:   FocusReportNode35403Enum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月19日 下午3:07:08
 */
public enum FocusReportNode35403Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("waterQuality", "流程表单类型"),
	FORM_TYPE_ID("35403", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	FIND_NODE_NAME("task1", "发现突发流域水质异常"),
	BEE_HANDLE_NODE_NAME("task2", "生态环境局处置"),
	DISTRICT_HANDLE_NODE_NAME("task3", "联合处置"),
	FILE_CASE_NODE_NAME("task4", "立案查处"),
	ADMINISTRTIVE_SACTION_NODE_NAME("task5", "行政处罚"),
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode35403Enum(String taskNodeName, String taskNodeNameZH) {
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
