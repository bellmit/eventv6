package cn.ffcs.zhsq.reportFocus.enterpriseHiddenDanger.service.impl;

/**
 * @Description: 南安企业隐患信息流程(351)流程节点名称
 * @ClassName:   FocusReportNode351Enum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月6日 上午10:32:47
 */
public enum FocusReportNode351Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("enterpriseHiddenDanger", "流程类型表单"),
	FORM_TYPE_ID("351", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	FIND_EHD_NODE_NAME("task1", "发现企业隐患信息"),
	DISTRUBUTE_TASK_NODE_NAME("task2", "第一网格长(驻村领导)任务派发"),
	VERIFY_TASK_NODE_NAME("task3", "第一副网格长(驻村工作队长)核实"),
	JOINT_CONSULTATION_NODE_NAME("task4", "联合会商"),
	VICE_COMMUNITY_HANDLE_NODE_NAME("task5", "第一副网格长查处"),
	STEET_HANDLE_NODE_NAME("task6", "乡镇查处"),
	SAB_HANDLE_NODE_NAME("task7", "市安办查处"),
	DEPARTMENT_HANDLE_NODE_NAME("task8", "牵头部门处置"),
	FILE_CASE_NODE_NAME("task9", "立案查处"),
	ADMINISTRTIVE_SACTION_NODE_NAME("task10", "行政处罚"),
	COMMUNITY_END_NODE_NAME("task11", "归档"),//未发现安全隐患，第一网格长归档
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode351Enum(String taskNodeName, String taskNodeNameZH) {
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
