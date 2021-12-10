package cn.ffcs.zhsq.reportFocus.povertyPreMonitor.service.impl;


/**
 * @Description: 南安致贫返贫监测信息流程(360)筛查反馈环节指定部门专业编码
 * @ClassName:   FocusReportDeptProCodeEnum
 * @author:      ztc
 * @date:        2021年6月1日
 */
public enum FocusReportDeptProCodeEnum {
    EXIST("EXIST", "存在"),
    DOES_NOT_EXIST("DOES_NOT_EXIST", "不存在"),
    FINANCE_BUREAU_BUDGET_SECTION("caizhengju", "财政局预算科"),//市财政局预算科负责人
    NATURAL_RESOURCES_REGISTRATION_CENTER("nanAn_naturalResources_92", "自然资源局不动产登记中心"),//市自然资源局不动产登记中心负责人
    MARKET_APPROVAL_SECTION("nasscjdglj", "市场监督管理局审批科"),//市场监督管理局审批科负责人
    TRAFFIC_POLICE_BRIGADE("nasgaj", "公安局交警大队");//市公安局交警大队负责人

    private String professionCode;
    private String orgName;

    private FocusReportDeptProCodeEnum(String professionCode, String orgName) {
        this.professionCode = professionCode;
        this.orgName = orgName;
    }

    public String getProfessionCode() {
        return professionCode;
    }

    public String getOrgName() {
        return orgName;
    }

    @Override
    public String toString() {
        return orgName;
    }
}
