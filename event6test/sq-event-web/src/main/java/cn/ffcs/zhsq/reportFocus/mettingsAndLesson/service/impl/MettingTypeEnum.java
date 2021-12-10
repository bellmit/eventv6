package cn.ffcs.zhsq.reportFocus.mettingsAndLesson.service.impl;

/**
 * @Description:会议类型枚举类
 * @Author: zhangtc
 * @Date: 2020/12/3 20:39
 */
public enum MettingTypeEnum {
    PARTY_MEMBER_MEETING("01", "党员大会"),
    BRANCH_COMMITTEE_MEETING("02", "支部委员会"),
    PARTY_GROUP_MEETING("03", "党小组会议"),
    PARTY_CLASS_MEETING("04", "党课活动"),
    PARTY_THEME_ACTIVITIES("05", "主题党日活动"),
    PARTY_LIFE_MEETING("06", "党支部组织生活会");

    private String dictCode;
    private String dictName;

    private MettingTypeEnum(String dictCode, String dictName) {
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
