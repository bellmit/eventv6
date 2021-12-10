package cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl;

/**
 * @Description:三会一课字典枚举
 * @Author: zhangtc
 * @Date: 2020/12/3 20:24
 */
public enum MeetingDictEnum {

    MEETING_TYPE("B210006001", "会议类型"),
    COLLECT_SOURCE("B210006003", "采集来源"),
    REPORT_STATUS("B210006002", "上报状态");

    private String dictCode;
    private String dictName;

    private MeetingDictEnum(String dictCode, String dictName) {
        this.dictCode = dictCode;
        this.dictName = dictName;
    }

    public String getDictCode() {
        return dictCode;
    }
    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }
    public String getDictName() {
        return dictName;
    }
    public void setDictName(String dictName) {
        this.dictName = dictName;
    }
    @Override
    public String toString() {
        return this.dictCode;
    }
}
