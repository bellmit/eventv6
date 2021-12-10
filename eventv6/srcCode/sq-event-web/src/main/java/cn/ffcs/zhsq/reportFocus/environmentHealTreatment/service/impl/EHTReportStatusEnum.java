package cn.ffcs.zhsq.reportFocus.environmentHealTreatment.service.impl;

/**
 * @Description:环境卫生问题上报状态枚举类
 * @Author: zhangtc
 * @Date: 2021/6/1 10:09
 */
public enum EHTReportStatusEnum {
    DRAFT_STATUS("99", "草稿"),
    FIND_STATUS("00", "发现问题"),
    DISTRUBUTE_STATUS("01", "任务派发"),
    COMMUNITY_HANDLE_STATUS("02", "一级网格处置"),
    STREET_HANDLE_STATUS("03", "镇级处置"),
    ADMINISTRATION_BUREAU_HANDLE_STATUS("04", "城市管理局处置"),
    RELEVANT_UNIT_HANDLE_STATUS("05", "市直单位处置"),
    END_STATUS("60", "办结销号");

    private String reportStatus;
    private String reportStatusName;

    private EHTReportStatusEnum(String reportStatus, String reportStatusName) {
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
