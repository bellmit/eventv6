package cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl;

/**
 * @Description:
 * @Author: zhangtc
 * @Date: 2020/11/24 9:03
 */
public enum TwoVioPreDataSourceEnum {
    GRID_INSPECT("01","网格巡查发现"),
    PLATFORM_12345("02","12345便民服务平台受理登记"),
    PLATFORM_TUBAN("03","“两违”图斑推送"),
    PATROL_ASSIGN("04","各级巡视巡察发现交办"),
    MASSES_REPORT("05","群众举报");

    private String dataSource;
    private String dataSourceName;

    private TwoVioPreDataSourceEnum(String dataSource, String dataSourceName) {
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
