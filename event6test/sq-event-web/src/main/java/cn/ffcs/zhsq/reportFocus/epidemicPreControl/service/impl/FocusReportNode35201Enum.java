package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

/**
 * @Description: 南安疫情防控信息流程(352)流程节点名称
 * @ClassName:   FocusReportNode352Enum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月11日 下午2:21:03
 */
/**
 * @Description: 南安疫情防控信息流程_小型医疗机构(35201)流程节点名称
 * @ClassName:   FocusReportNode35201Enum   
 * @author:      张联松(zhangls)
 * @date:        2021年1月22日 下午2:52:58
 */
public enum FocusReportNode35201Enum {
	WORKFLOW_TYPE_ID("focus_report", "流程类型"),
	FORM_TYPE("epidemicPreControl", "流程表单类型"),
	FORM_TYPE_ID("35201", "流程表单ID"),
	START_NODE_NAME("start", "流程开始"),
	FIND_NODE_NAME("task1", "发现重点管控人员"),
	DISTRUBUTE_TASK_NODE_NAME("task2", "第一网格长(驻村领导)任务派发"),
	STREET_NAD_NODE_NAME("task3", "乡镇(街道)疫情防控指挥部处置"),//NUCLEIC ACID DETECTION
	STREET_HOSPITAL_NODE_NAME("task4", "乡镇卫生院处置"),
	DISTRICT_HOSPITAL_NODE_NAME("task5", "片区发热门诊医疗机构处置"),
	DISTRICT_NAD_NODE_NAME("task6", "市疫情防控指挥部处置"),//怀疑新冠肺炎疑似或确诊病例（或无症状感染者）
	END_NODE_NAME("end1", "归档");//排除新冠肺炎疑似或确诊病例（或无症状感染者）
	
	private String taskNodeName;
	private String taskNodeNameZH;
	
	private FocusReportNode35201Enum(String taskNodeName, String taskNodeNameZH) {
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
