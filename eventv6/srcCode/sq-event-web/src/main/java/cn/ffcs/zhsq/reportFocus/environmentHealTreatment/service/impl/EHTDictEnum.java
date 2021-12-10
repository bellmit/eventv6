package cn.ffcs.zhsq.reportFocus.environmentHealTreatment.service.impl;

public enum EHTDictEnum {
    REPORT_STATUS("B210014001", "上报状态"),
    DATA_SOURCE("B210014002", "数据来源"),
    PROBLEM_TYPE("B210014003", "问题类型"),
    DISCOVERY_CHANNELS("B210014004", "发现渠道");

    private String dictCode;
    private String dictName;

    private EHTDictEnum(String dictCode, String dictName) {
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
