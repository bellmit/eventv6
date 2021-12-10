package cn.ffcs.zhsq.reportFocus.environmentHealTreatment.service.impl;

/**
 * @Description: 环境卫生问题处置数据来源枚举类
 * @ClassName:   EHTDataSourceEnum
 * @author:      ztc
 * @date:        2021年05月31日 下午3:07:58
 */
public enum EHTDataSourceEnum {
    GRID_INSPECT("01", "网格员巡查发现"),
    PLATFORM_12345("02", "12345便民服务平台受理登记"),
    SUPERIOR_ASSIGN("03", "上级派发"),
    MASSES_REPORT("04", "群众举报");

    private String dataSource;
    private String dataSourceName;

    private EHTDataSourceEnum(String dataSource, String dataSourceName) {
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
