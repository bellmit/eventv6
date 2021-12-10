package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

/**
 * @Description: 疫情防控(Epidemic Prevention and Control)上报状态枚举类
 * @ClassName:   EPCReportStatusEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月11日 下午2:19:06
 */
public enum EPCReportStatusEnum {
	DRAFT_STATUS("99", "草稿"),
	FIND_STATUS("00", "发现人员"),
	DISTRUBUTE_STATUS("01", "任务派发"),
	VERIFY_STATUS("02", "踏勘核实"),
	HANDLE_STATUS("03", "整改处置"),
	FOLLOW_SUPERVISE_STATUS("04", "跟踪督促"),
	END_STATUS("60", "办结销号");
	
	private String reportStatus;
	private String reportStatusName;
	
	private EPCReportStatusEnum(String reportStatus, String reportStatusName) {
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
