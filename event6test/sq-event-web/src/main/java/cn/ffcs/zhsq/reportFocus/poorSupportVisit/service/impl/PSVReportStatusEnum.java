package cn.ffcs.zhsq.reportFocus.poorSupportVisit.service.impl;

/**
 * @Description: 扶贫走访(Poor Support Visit)上报状态枚举类，字典编码B210007001
 * @ClassName:   PSVReportStatusEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年12月4日 上午9:51:49
 */
public enum PSVReportStatusEnum {
	DRAFT_STATUS("99", "草稿"),
	FILL_REPORT_STATUS("00", "填写报告"),
	DISTRUBUTE_STATUS("01", "任务派发"),
	VERIFY_STATUS("02", "核实反馈"),
	HANDLE_STATUS("03", "整改处置"),
	END_STATUS("60", "办结销号");
	
	private String reportStatus;
	private String reportStatusName;
	
	private PSVReportStatusEnum(String reportStatus, String reportStatusName) {
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
