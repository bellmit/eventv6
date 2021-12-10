package cn.ffcs.zhsq.reportFocus.petitionPerson.impl;

public enum PetPerReportStatusEnum {

	DRAFT_STATUS("99", "草稿"),
	FIND_STATUS("00", "发现问题"),
	DISTRUBUTE_STATUS("01", "任务派发"),
	LEAD_STATUS("02", "任务处置"),
	MUNICIPAL_STATUS("03", "情况反馈"),
	END_STATUS("60", "办结销号");
	
	private String reportStatus;
	private String reportStatusName;
	
	private PetPerReportStatusEnum(String reportStatus, String reportStatusName) {
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
