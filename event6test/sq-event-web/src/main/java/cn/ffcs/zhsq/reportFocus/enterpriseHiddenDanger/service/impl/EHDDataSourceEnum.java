package cn.ffcs.zhsq.reportFocus.enterpriseHiddenDanger.service.impl;

/**
 * @Description:
 * @Author: zhangtc
 * @Date: 2020/11/24 10:44
 */
public enum EHDDataSourceEnum {
    GRID_INSPECT("01", "网格员巡查发现"),
    SECURITY_MANAGER("02", "企业安全管理员发现"),
    VILLAGETEAM_REPORT("03", "驻村工作巡查发现"),
    PLATFORM_12345("04", "12345便民服务平台受理登记"),
    MASSES_REPORT("05", "群众举报"),
    PATROL_ASSIGN("06", "上级巡查检查发现"),
    PLATFORM_SECURITYDEVICE_WARNING("07", "安全监控设备预警");

    private String dataSource;
    private String dataSourceName;

    private EHDDataSourceEnum(String dataSource, String dataSourceName) {
        this.dataSource = dataSource;
        this.dataSourceName = dataSourceName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public String toString() {
        return dataSource;
    }
}
