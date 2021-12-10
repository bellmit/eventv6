package cn.ffcs.zhsq.reportFocus.businessProblem.service.impl;

public enum BusProReportStatusEnum {

	DRAFT_STATUS("99", "草稿"),
	FIND_STATUS("00", "发现问题"),
	DISTRUBUTE_STATUS("01", "任务派发"),
	LEAD_STATUS("02", "牵头部门处置"),
	MUNICIPAL_STATUS("03", "市直部门处置"),
	OFFICE_STATUS("04", "市招商办处置"),
	DELAY_STATUS("05", "延期意见反馈"),
	END_STATUS("60", "办结销号");
	
	private String reportStatus;
	private String reportStatusName;
	
	private BusProReportStatusEnum(String reportStatus, String reportStatusName) {
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
