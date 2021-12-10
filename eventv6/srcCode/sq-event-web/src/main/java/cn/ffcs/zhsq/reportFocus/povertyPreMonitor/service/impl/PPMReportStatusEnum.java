package cn.ffcs.zhsq.reportFocus.povertyPreMonitor.service.impl;

/**
 * @Description:致贫返贫监测上报状态枚举类
 * @Author: zhangtc
 * @Date: 2021/6/1 10:09
 */
public enum  PPMReportStatusEnum {
    DRAFT_STATUS("99", "草稿"),
    FIND_STATUS("00", "发现人员"),
    DISTRUBUTE_STATUS("01", "任务派发"),
    FEEDBACK_STATUS("02", "反馈情况"),
    COMMUNITY_COMMENT_STATUS("03", "村级评议"),
    STREET_AUDIT_COMMENT_STATUS("04", "镇级审核"),
    MUNICIPAL_SEND_STATUS("05", "市级任务派发"),
    SCREENING_FEEDBACK_STATUS("06", "筛查反馈"),
    DETERMINE_OBJECT_STATUS("07", "对象确定"),
    DE_MONITORING_STATUS("08", "解除监测"),
    CONTINUOUS_MONITORING_STATUS("09", "持续监测"),
    END_STATUS("60", "办结销号");

    private String reportStatus;
    private String reportStatusName;

    private PPMReportStatusEnum(String reportStatus, String reportStatusName) {
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
