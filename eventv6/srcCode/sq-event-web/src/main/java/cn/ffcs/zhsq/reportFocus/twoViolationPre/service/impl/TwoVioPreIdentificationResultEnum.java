package cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl;

/**
 * @Description: 认定结果枚举类
 * @ClassName:   TwoVioPreIdentificationResultEnum   
 * @author:      张联松(zhangls)
 * @date:        2021年5月8日 上午9:10:41
 */
public enum TwoVioPreIdentificationResultEnum {
    NOT_BELONG_TWO_VIO("01","不属两违"),
    COMMUNITY_CLOSE("02","一级网格办结"),
    STREET_CLOSE("03","镇级办结"),
    MUNICIPAL_DEPARTMENT_CLOSE("04","部门办结");

    private String identificationResult;
    private String identificationResultName;

    private TwoVioPreIdentificationResultEnum(String identificationResult, String identificationResultName) {
        this.identificationResult = identificationResult;
        this.identificationResultName = identificationResultName;
    }

    public String getIdentificationResult() {
        return identificationResult;
    }

    public String getIdentificationResultName() {
        return identificationResultName;
    }

    public String toString() {
        return identificationResult;
    }
}
