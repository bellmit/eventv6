package cn.ffcs.zhsq.reportFocus.forestFirePrevention.service.impl;

/**
 * @Description: 森林防灭火，上报状态枚举类，字典编码 B210009001
 * @ClassName:   FFPReportStatusEnum   
 * @author:      张联松(zhangls)
 * @date:        2021年3月31日 上午10:19:46
 */
public enum FFPReportStatusEnum {
	DRAFT_STATUS("99", "草稿"),
	FIND_STATUS("00", "发现隐患"),
	DISTRUBUTE_STATUS("01", "任务派发"),
	VERIFY_STATUS("02", "现场核实"),
	COMMUNITY_HANDLE_STATUS("03", "一级网格处置"),
	STREET_HANDLE_STATUS("04", "镇级增员处置"),
	DISTRICT_HANDLE_STATUS("05", "市级支援处置"),
	STREET_ARCHIVE_STATUS("06", "镇级处置"),
	END_STATUS("60", "办结销号");
	
	private String reportStatus;
	private String reportStatusName;
	
	private FFPReportStatusEnum(String reportStatus, String reportStatusName) {
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
