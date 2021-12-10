package cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl;

/**
 * @Description:采集来源类型
 * @Author: zhangtc
 * @Date: 2020/12/3 20:24
 */
public enum MeetingCollectSourceEnum {
    COMMUNITY_REPORT("01", "村（社区）组织委员（一级网格员）"),
    GRID_REPORT("02", "村（社区）党小组组长（二级网格长）"),
    STREET_COMMITTEE_REPORT("03", "镇直单位党组织组织委员"),
    STREET_GROUPLEADER_REPORT("04", "镇直单位各党小组组长"),
    CITY_COMMITTEE_TYPE("05", "市直单位党组织组织委员"),
    CITY_GROUPLEADER_TYPE("06", "市直单位各党小组组长");

    private String collectSource;
    private String collectSourceName;

    private MeetingCollectSourceEnum(String collectSource, String collectSourceName) {
        this.collectSource = collectSource;
        this.collectSourceName = collectSourceName;
    }

    public String getCollectSource() {
        return collectSource;
    }
    public void setCollectSource(String collectSource) {
        this.collectSource = collectSource;
    }
    public String getCollectSourceName() {
        return collectSourceName;
    }
    public void setCollectSourcetName(String collectSourceName) {
        this.collectSourceName = collectSourceName;
    }
    @Override
    public String toString() {
        return this.collectSource;
    }
}
