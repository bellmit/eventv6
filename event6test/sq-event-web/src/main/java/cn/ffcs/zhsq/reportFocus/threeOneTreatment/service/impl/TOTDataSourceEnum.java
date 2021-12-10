package cn.ffcs.zhsq.reportFocus.threeOneTreatment.service.impl;

/**
 * @Description:
 * @Author: zhangtc
 * @Date: 2021/8/16 9:21
 */
public enum TOTDataSourceEnum {
    GRID_INSPECT("01", "网格员巡查发现"),
    PLATFORM_12345("02", "12345便民服务平台受理登记"),
    SUPERIOR_ASSIGN("03", "泉州市级相关部门督查（检查）发现"),
    UNANNOUNCED_VISIT("04", "南安市整治办明查暗访发现"),
    MASSES_REPORT("05", "群众举报");

    private String dataSource;
    private String dataSourceName;

    private TOTDataSourceEnum(String dataSource, String dataSourceName) {
        this.dataSource = dataSource;
        this.dataSourceName = dataSourceName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }


    @Override
    public String toString() {
        return dataSource;
    }
}
