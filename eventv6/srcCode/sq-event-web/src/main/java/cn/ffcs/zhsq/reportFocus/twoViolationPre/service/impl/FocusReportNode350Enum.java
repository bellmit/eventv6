package cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl;

/**
 * @Description: 南安两违防治事件(350)流程节点名称
 * @ClassName:   FocusReportNode350Enum   
 * @author:      张联松(zhangls)
 * @date:        2020年9月15日 下午2:48:43
 */
public enum FocusReportNode350Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("twoVioPre", "流程表单类型"),
	FORM_TYPE_ID("350", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	FIND_TWO_VIO_NODE_NAME("task1", "发现两违行为"),
	DISTRUBUTE_TASK_NODE_NAME("task2", "第一网格长(驻村领导)任务派发"),
	VERIFY_TASK_NODE_NAME("task3", "第一副网格长(驻村工作队长)核实"),
	IDENTIFY_TWO_VIO_TASK_NODE_NAME("task4", "第一网格长(驻村领导)两违认定"),
	STREET_DISTRIBUTE_TASK_NODE_NAME("task5", "镇级处置"),//不在本网格
	HANDLE_TWO_VIO_TASK_NODE_NAME("task6", "第一网格长(驻村领导)两违处置"),//下一环节选择“镇级处置”时，启动子流程“南安两违防治事件_跟踪两违状态”
	CHECK_TASK_NODE_NAME("task7", "市两违办监督检查"),
	STREET_DEAL_TASK_NODE_NAME("task8", "镇级处置"),//下一环节选择“镇级反馈”，启动子流程“南安两违防治事件_交办部门查处”
	STREET_FEEDBACK_TASK_NODE_NAME("task12", "镇级反馈"),//下一环节选择“市两违办交办部门查处”，启动子流程“南安两违防治事件_报告整治进展情况”
	ASSIGN_TASK_NODE_NAME("task9", "市两违办交办部门查处"),//启动子流程“南安两违防治事件_交办部门查处”，停用
	DEAL_WITH_DEPARTMENT_TASK_NODE_NAME("task10", "市直部门查处"),//停用
	DEPARTMENT_CHECK_TASK_NODE_NAME("task11", "市直部门核验"),//市职能部门进一步核验
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode350Enum(String taskNodeName, String taskNodeNameZH) {
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
