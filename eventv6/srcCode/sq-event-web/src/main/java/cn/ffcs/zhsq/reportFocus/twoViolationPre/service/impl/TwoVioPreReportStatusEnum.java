package cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl;

/**
 * 两违防治上报状态枚举类，字典编码：B210001003
 * @Description: 
 * @ClassName:   TwoVioPreReportStatusEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年9月25日 上午10:32:55
 */
public enum TwoVioPreReportStatusEnum {
	DRAFT_STATUS("99", "草稿"),
	FIND_STATUS("00", "发现两违"),
	DISTRUBUTE_STATUS("01", "任务派发"),
	VERIFY_STATUS("02", "踏勘核实"),
	IDENTIFY_STATUS("03", "两违认定"),
	MANAGE_STATUS("04", "整改处置"),
	ASSIGN_STATUS("05", "交办查处"),
	DEPARTMENT_MANAGE_STATUS("06", "部门查处"),
	CHECK_STATUS("07", "监督检查"),
	HANDLE_STATUS("08", "验收核实"),
	END_STATUS("60", "办结销号");
	
	private String reportStatus;
	private String reportStatusName;
	
	private TwoVioPreReportStatusEnum(String reportStatus, String reportStatusName) {
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
