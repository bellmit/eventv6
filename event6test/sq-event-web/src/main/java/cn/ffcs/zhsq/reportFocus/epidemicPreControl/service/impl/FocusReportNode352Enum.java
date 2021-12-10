package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

/**
 * @Description: 南安疫情防控信息流程(352)流程节点名称
 * @ClassName:   FocusReportNode352Enum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月11日 下午2:21:03
 */
public enum FocusReportNode352Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("epidemicPreControl", "流程表单类型"),
	FORM_TYPE_ID("352", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	FIND_NODE_NAME("task1", "发现重点管控人员"),
	DISTRUBUTE_TASK_NODE_NAME("task2", "第一网格长(驻村领导)任务派发"),
	VERIFY_DECISION_NODE_NAME("decision1", "决策1"),
	VERIFY_DECISION_NODE_NAME2("decision2", "决策2"),
	VERIFY_TASK_GRID_NODE_NAME("task3", "第一副网格长(驻村工作队长)核实"),//数据来源为：网格员巡查发现
	VERIFY_TASK_OTHER_NODE_NAME("task4", "第一副网格长(驻村工作队长)核实"),//数据来源为：非网格员巡查发现
	COMMUNITY_HANDLE_NODE_NAME("task5", "第一网格长(驻村领导)处置"),
	NOT_THE_GRID_NODE_NAME("task6", "乡镇(街道)疫情防控指挥部处置"),//重点管控人员不在本网格
	VICE_COMMUNITY_HANDLE_NODE_NAME("task7", "第一副网格长(驻村工作队长)处置"),//需居家隔离的重点管控人员
	GRID_HANDLE_NODE_NAME("task8", "二级网格长跟踪督促"),
	CENTRALIZED_ISOLATION_NODE_NAME("task9", "乡镇(街道)疫情防控指挥部处置"),//需集中隔离的重点管控人员
	//名称变更 20210914 只需核酸检测的重点管控人员-->需居家健康监测并核酸检测的重点管控人员
	NUCLEIC_ACID_DETECTION_NODE_NAME("task10", "乡镇(街道)疫情防控指挥部处置"),//只需核酸检测的重点管控人员
	NOT_THE_STREET_NODE_NAME("task11", "市疫情防控指挥部处置"),//重点管控人员不在本街道
	NOT_VERIFIED_NODE_NAME("task12", "归档"),//市疫情防控指挥部处置 重点管控人员无法核实
	FOREIGN_AFFAIRS_GROUP_NODE_NAME("task13", "乡镇(街道)疫情防控指挥部处置"),//无法明确村级地址 市外事组
	EPC_GROUP_NODE_NAME("task14", "乡镇(街道)疫情防控指挥部处置"),//无法明确村级地址 市疫情防控组
	BIG_DATA_GROUP_NODE_NAME("task15", "乡镇(街道)疫情防控指挥部处置"),//无法明确村级地址 市大数据组的
	END_NODE_NAME("end1", "归档");
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode352Enum(String taskNodeName, String taskNodeNameZH) {
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
