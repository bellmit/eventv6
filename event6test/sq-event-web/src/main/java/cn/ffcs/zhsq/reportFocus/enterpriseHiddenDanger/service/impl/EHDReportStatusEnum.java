package cn.ffcs.zhsq.reportFocus.enterpriseHiddenDanger.service.impl;

/**
 * @Description: 企业安全隐患(Enterprise Hidden Danger)上报状态枚举类，字典编码 B210002001
 * @ClassName:   EHDReportStatusEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月6日 上午10:44:03
 */
public enum EHDReportStatusEnum {
	DRAFT_STATUS("99", "草稿"),
	FIND_STATUS("00", "发现隐患"),
	DISTRUBUTE_STATUS("01", "任务派发"),
	VERIFY_STATUS("02", "踏勘核实"),
	JOINT_CONSULTATION_STATUS("03", "联合会商"),
	HANDLE_STATUS("04", "整改处置"),
	FILE_CASE_STATUS("05", "立案查处"),
	ADMINISTRTIVE_SACTION_STATUS("06", "行政处罚"),
	END_STATUS("60", "办结销号");
	
	private String reportStatus;
	private String reportStatusName;
	
	private EHDReportStatusEnum(String reportStatus, String reportStatusName) {
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
