package cn.ffcs.zhsq.reportFocus.threeOneTreatment.service.impl;

public enum TOTDictEnum {
    DATA_SOURCE("B210015001", "数据来源"),
    REPORT_STATUS("B210015002", "上报状态"),
    PROBLEM_TYPE("B210015003", "隐患类型");

    private String dictCode;
    private String dictName;

    private TOTDictEnum(String dictCode, String dictName) {
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
