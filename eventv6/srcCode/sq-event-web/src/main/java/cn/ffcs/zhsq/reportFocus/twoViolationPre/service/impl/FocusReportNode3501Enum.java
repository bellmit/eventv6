package cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl;

/**
 * @Description: 南安两违防治事件_报告整治进展情况(3501)流程节点名称
 * @ClassName:   FocusReportNode3501Enum   
 * @author:      张联松(zhangls)
 * @date:        2020年9月15日 下午3:02:05
 */
public enum FocusReportNode3501Enum {
	FORM_TYPE_ID("3501", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	ASSIGN_TASK_NODE_NAME("task1", "镇级报告整治进展情况"),
	DEAL_WITH_DEPARTMENT_TASK_NODE_NAME("task2", "市两违办跟踪整治进展情况"),
	END_NODE_NAME("end1", "结束");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode3501Enum(String taskNodeName, String taskNodeNameZH) {
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
