package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

/**
 * @Description: 疫情防控属实情况枚举类
 * @author:      ztc
 * @date:        2021年05月27日
 */
public enum EPCVerifyStatusEnum {

    IS_TRUE("1", "属实"),
    NOT_TRUE("2", "不属实"),
    UN_TRUE("3", "无法核实");

    private String verifyStatus;
    private String verifyStatusName;

    private EPCVerifyStatusEnum(String verifyStatus, String verifyStatusName) {
        this.verifyStatus = verifyStatus;
        this.verifyStatusName = verifyStatusName;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public String getVerifyStatusName() {
        return verifyStatusName;
    }

    @Override
    public String toString() {
        return verifyStatus;
    }
}
