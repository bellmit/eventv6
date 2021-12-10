package cn.ffcs.zhsq.reportFocus.houseHiddenDanger.service.impl;

/**
 * @Description: 企业安全隐患(Enterprise Hidden Danger)上报状态枚举类，字典编码 B210002001
 * @ClassName:   EHDReportStatusEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月6日 上午10:44:03
 */
public enum HHDReportStatusEnum {
	DRAFT_STATUS("99", "草稿"),
	FIND_STATUS("00", "发现隐患"),
	DISTRUBUTE_STATUS("01", "任务派发"),
	VERIFY_STATUS("02", "踏勘核实"),
	HHD_COGNIZANCE("03", "隐患认定"),
	HANDLE_STATUS("04", "整改处置"),
	END_STATUS("60", "办结销号");
	
	private String reportStatus;
	private String reportStatusName;
	
	private HHDReportStatusEnum(String reportStatus, String reportStatusName) {
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
