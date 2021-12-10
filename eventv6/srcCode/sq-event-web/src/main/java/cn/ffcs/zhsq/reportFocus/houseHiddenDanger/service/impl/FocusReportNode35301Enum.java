package cn.ffcs.zhsq.reportFocus.houseHiddenDanger.service.impl;

/**
 * @Description: 南安房屋安全隐患信息流程_常规(35301)流程节点名称
 * @ClassName:   FocusReportNode35301Enum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月17日 下午5:57:14
 */
public enum FocusReportNode35301Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("houseHiddenDanger", "流程表单类型"),
	FORM_TYPE_ID("35301", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	FIND_HHD_NODE_NAME("task1", "发现房屋安全隐患"),
	DISTRUBUTE_TASK_NODE_NAME("task2", "第一网格长(驻村领导)任务派发"),
	VERIFY_TASK_NODE_NAME("task3", "第一副网格长(驻村工作队长)核实"),
	HHD_COGNIZANCE_NODE_NAME("task4", "第一网格长(驻村领导)隐患认定"),
	COMMUNITY_HANDLE_NODE_NAME("task5", "第一网格长(驻村领导)处置"),
	VERIFY_DECISION_NODE_NAME("decision1", "决策1"),
	MASSES_REPORT_NODE_NAME("task6", "乡镇(街道)任务派发"),//群众举报
	ASSIGNED_SUPERIOR_NODE_NAME("task7", "乡镇(街道)任务派发"),//上级交办
	FIREPROTECTION_NODE_NAME("task8", "乡镇(街道)任务派发"),//消防手续审批
	BUSINESS_LICENSE_NODE_NAME("task9", "乡镇(街道)任务派发"),//营业执照审批
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode35301Enum(String taskNodeName, String taskNodeNameZH) {
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
