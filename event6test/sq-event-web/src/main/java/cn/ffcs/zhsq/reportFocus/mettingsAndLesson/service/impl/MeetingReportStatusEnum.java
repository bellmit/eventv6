package cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl;

/**
 * @Description:三会一课上报状态枚举类
 * @Author: zhangtc
 * @Date: 2020/12/4 10:37
 */
public enum MeetingReportStatusEnum {
    DRAFT_STATUS("99", "草稿"),
    COLLECT_STATUS("00", "会议采集"),
    REPORT_STATUS("01", "会议上报"),
    ACCEPTANCE_CHECK_STATUS("08", "验收核实"),
    /*VERIFY_STATUS("02", "会议上报"),
    HANDLE_STATUS("03", "会议上报"),
    FILE_CASE_STATUS("04", "会议上报"),
    ADMINISTRTIVE_SACTION_STATUS("05", "会议上报"),*/
    END_STATUS("60", "会议结束");

    private String reportStatus;
    private String reportStatusName;

    private MeetingReportStatusEnum(String reportStatus, String reportStatusName) {
        this.reportStatus = reportStatus;
        this.reportStatusName = reportStatusName;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public String getReportStatusName() {
        return reportStatusName;
    }
    @Override
    public String toString() {
        return reportStatus;
    }
}
