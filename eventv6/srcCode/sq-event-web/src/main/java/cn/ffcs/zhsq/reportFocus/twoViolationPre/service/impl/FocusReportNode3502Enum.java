package cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl;

/**
 * @Description: 南安两违防治事件-交办部门查处(3502)流程
 * @ClassName:   FocusReportNode3502Enum   
 * @author:      张联松(zhangls)
 * @date:        2020年9月15日 下午3:06:30
 */
public enum FocusReportNode3502Enum {
	FORM_TYPE_ID("3502", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	DEAL_WITH_TASK_NODE_NAME("task1", "镇级反馈"),
	CHECK_TASK_NODE_NAME("task2", "交办部门查处"),
	HANDLE_TASK_NODE_NAME("task3", "部门查处反馈"),
	END_NODE_NAME("end1", "结束");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode3502Enum(String taskNodeName, String taskNodeNameZH) {
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
