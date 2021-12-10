package cn.ffcs.zhsq.reportFocus.waterQuality.service.impl;

/**
 * @Description: 流域水质问题(Water Quality)上报状态枚举类
 * @ClassName:   WaterQualityReportStatusEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月19日 下午3:19:50
 */
public enum WaterQualityReportStatusEnum {
	DRAFT_STATUS("99", "草稿"),
	FIND_STATUS("00", "发现问题"),
	DISTRUBUTE_STATUS("01", "任务派发"),
	VERIFY_STATUS("02", "踏勘核实"),
	HANDLE_STATUS("03", "整改处置"),
	FILE_CASE_STATUS("04", "立案查处"),
	ADMINISTRTIVE_SACTION_STATUS("05", "行政处罚"),
	END_STATUS("60", "办结销号");
	
	private String reportStatus;
	private String reportStatusName;
	
	private WaterQualityReportStatusEnum(String reportStatus, String reportStatusName) {
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
