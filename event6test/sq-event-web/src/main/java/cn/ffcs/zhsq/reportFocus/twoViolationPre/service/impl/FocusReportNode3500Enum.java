package cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl;

/**
 * @Description: 南安两违防治事件-跟踪两违状态(3500)流程节点名称
 * @ClassName:   FocusReportNode3500Enum   
 * @author:      张联松(zhangls)
 * @date:        2020年9月15日 下午2:51:53
 */
public enum FocusReportNode3500Enum {
	FORM_TYPE_ID("3500", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	DISASSEMBLE_NODE_NAME("task1", "第一副网格长(驻村工作队长)分解跟踪任务"),
	REGULAR_REPORT_NODE_NAME("task2", "二级网格长(一级网格员)定期报告两违状态"),
	HANDLE_TASK_NODE_NAME("task3", "第一网格长(驻村领导)跟踪报告"),
	END_NODE_NAME("end1", "结束");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode3500Enum(String taskNodeName, String taskNodeNameZH) {
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
