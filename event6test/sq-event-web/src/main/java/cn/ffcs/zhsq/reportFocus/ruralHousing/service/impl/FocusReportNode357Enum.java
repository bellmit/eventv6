package cn.ffcs.zhsq.reportFocus.ruralHousing.service.impl;

/**
 * @Description: 南安一告知四到场流程(357)流程节点名称
 * @ClassName:   FocusReportNode357Enum   
 * @author:      张联松(zhangls)
 * @date:        2021年3月8日 下午5:26:00
 */
public enum FocusReportNode357Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("ruralHousing", "流程类型表单"),
	FORM_TYPE_ID("357", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	SIGN_NOTIFICATION_NODE_NAME("task1", "签订告知书"),
	LOFTING_START_NODE_NAME("task2", "提请放样"),
	LOFTING_ORGANIZE_NODE_NAME("task3", "组织放样"),
	LINE_CHECK_START_NODE_NAME("task4", "提请验线"),
	LINE_CHECK_ORGANIZE_NODE_NAME("task5", "组织验线"),
	LINE_CHECK_REFORM_NODE_NAME("task6", "组织验线整改"),
	CONSTRUCTION_ACCEPTANCE_START_NODE_NAME("task7", "提请施工节点验收"),
	CONSTRUCTION_ACCEPTANCE_ORGANIZE_NODE_NAME("task8", "组织施工节点验收"),
	CONSTRUCTION_ACCEPTANCE_REFORM_NODE_NAME("task9", "组织施工节点整改"),
	COMPLETION_ACCEPTANCE_START_NODE_NAME("task10", "提请竣工验收"),
	COMPLETION_ACCEPTANCE_ORGANIZE_NODE_NAME("task11", "组织竣工验收"),
	COMPLETION_ACCEPTANCE_REFORM_NODE_NAME("task12", "组织竣工整改"),
	COMPLETION_NODE_NAME("task13", "办结归档"),
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode357Enum(String taskNodeName, String taskNodeNameZH) {
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
