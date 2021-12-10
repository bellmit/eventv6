package cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl;

/**
 * @Description: 两违核实结果枚举类
 * @ClassName:   TwoVioPreVerifyStatusEnum   
 * @author:      张联松(zhangls)
 * @date:        2021年6月23日 下午3:19:07
 */
public enum TwoVioPreVerifyStatusEnum {
	BELONG_TWO_VIO("1","属于两违"),
    NOT_BELONG_TWO_VIO("2","不属两违"),
    BEYOND_JURISDICTION("3","不属本村");

    private String verifyStatus;
    private String verifyStatusName;

    private TwoVioPreVerifyStatusEnum(String verifyStatus, String verifyStatusName) {
        this.verifyStatus = verifyStatus;
        this.verifyStatusName = verifyStatusName;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public String getVerifyStatusName() {
        return verifyStatusName;
    }

    public String toString() {
        return verifyStatus;
    }
}
