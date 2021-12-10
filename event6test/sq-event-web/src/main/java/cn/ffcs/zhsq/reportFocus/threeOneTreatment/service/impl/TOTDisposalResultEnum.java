package cn.ffcs.zhsq.reportFocus.threeOneTreatment.service.impl;

public enum TOTDisposalResultEnum {
    REMEDIATION_COMPLETE("0", "已完成整治"),
    APPLY_EXTESION("1", "申请延期"),
    STREET_HANDLE("2", "提请镇级处置"),
    CHECK_NOPASSED("0", "验收不通过"),
    CHECK_PASSED("1", "验收通过");

    private String disposalResult;
    private String disposalResultName;

    private TOTDisposalResultEnum(String disposalResult, String disposalResultName) {
        this.disposalResult = disposalResult;
        this.disposalResultName = disposalResultName;
    }

    public String getDisposalResult() {
        return disposalResult;
    }

    public String getDisposalResultName() {
        return disposalResultName;
    }


    @Override
    public String toString() {
        return disposalResult;
    }
}
