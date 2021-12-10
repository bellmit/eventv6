package cn.ffcs.zhsq.reportFocus.martyrsFacility.impl;

public enum MarFacReportStatusEnum {

	DRAFT_STATUS("99", "草稿"),
	FIND_STATUS("00", "发现问题"),
	DISTRUBUTE_STATUS("01", "交办任务"),
	FEEDBACK_STATUS("02", "问题反馈"),
	TODO_STATUS("03", "问题处置"),
	ACCEPTANCE_STATUS("04", "组织验收"),
	END_STATUS("60", "办结销号");
	
	private String reportStatus;
	private String reportStatusName;
	
	private MarFacReportStatusEnum(String reportStatus, String reportStatusName) {
		this.reportStatus = reportStatus;
		this.reportStatusName = reportStatusName;
	}

	public String getReportStatus() {
		return reportStatus;
	}
	
	public String getReportStatusName() {
		return reportStatusName;
	}

	public String toString() {
		return reportStatus;
	}
}
