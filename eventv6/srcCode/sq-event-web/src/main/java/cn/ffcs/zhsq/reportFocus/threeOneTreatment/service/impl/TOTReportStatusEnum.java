package cn.ffcs.zhsq.reportFocus.threeOneTreatment.service.impl;

public enum TOTReportStatusEnum {
    DRAFT_STATUS("99", "草稿"),
    FIND_STATUS("00", "发现问题"),
    DISTRUBUTE_STATUS("01", "任务派发"),
    VERIFY_FEEDBACK_STATUS("02", "核实反馈"),
    COMMUNITY_HANDLE_STATUS("03", "一级网格处置"),
    STREET_HANDLE_STATUS("04", "镇级处置"),
    MUNICIPAL_HANDLE_STATUS("05", "市级处置"),
    ACCEPTANCE_STATUS("06", "组织验收"),
    DECOMPOSITION_TRACKING_TASK_STATUS("07", "分解跟踪任务"),
    PERIODIC_REPORTS_HDSTATUS_STATUS("08", "定期报告隐患状态"),
    END_STATUS("60", "办结销号");

    private String reportStatus;
    private String reportStatusName;

    private TOTReportStatusEnum(String reportStatus, String reportStatusName) {
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
