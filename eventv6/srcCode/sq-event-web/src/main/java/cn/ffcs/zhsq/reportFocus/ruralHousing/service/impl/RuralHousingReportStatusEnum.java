package cn.ffcs.zhsq.reportFocus.ruralHousing.service.impl;

/**
 * @Description: 农村建房，上报状态枚举类，字典编码 B210008001
 * @ClassName:   RuralHousingReportStatusEnum   
 * @author:      张联松(zhangls)
 * @date:        2021年3月8日 下午5:55:06
 */
public enum RuralHousingReportStatusEnum {
	DRAFT_STATUS("99", "草稿"),
	SIGN_NOTIFICATION_STATUS("00", "签订告知书"),
	LOFTING_STATUS("01", "建筑放样到场"),
	LINE_CHECK_STATUS("02", "基槽验线到场"),
	CONSTRUCTION_STATUS("03", "施工节点到场"),
	COMPLETION_STATUS("04", "竣工验收到场"),
	TRANSFER_STATUS("05", "资料移交"),
	END_STATUS("60", "办结销号");
	
	private String reportStatus;
	private String reportStatusName;
	
	private RuralHousingReportStatusEnum(String reportStatus, String reportStatusName) {
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
