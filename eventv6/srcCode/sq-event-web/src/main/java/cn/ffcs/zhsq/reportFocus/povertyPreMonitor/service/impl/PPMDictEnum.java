package cn.ffcs.zhsq.reportFocus.povertyPreMonitor.service.impl;

public enum PPMDictEnum {
    REPORT_STATUS("B210011001", "上报状态"),
    DATA_SOURCE("B210011002", "数据来源"),
    PPM_RISK("B210011004", "存在致贫返贫风险"),
    POV_BACK_REASON("B210011003", "致贫返贫原因");

    private String dictCode;
    private String dictName;

    private PPMDictEnum(String dictCode, String dictName) {
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
