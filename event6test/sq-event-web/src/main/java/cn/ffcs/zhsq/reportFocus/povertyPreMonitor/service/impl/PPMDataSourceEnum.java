package cn.ffcs.zhsq.reportFocus.povertyPreMonitor.service.impl;

/**
 * @Description: 致贫返贫监测数据来源枚举类
 * @ClassName:   PPMDataSourceEnum
 * @author:      ztc
 * @date:        2021年05月31日 下午3:07:58
 */
public enum PPMDataSourceEnum {
    GRID_INSPECT("01", "网格员巡查发现"),
    INVOLVED_UNIT_DEPT("02","单位科室排查发现"),
    REPORT_POVERTY_HOTLINE("03","一键报贫或扶贫热线电话"),//闽政通APP“一键报贫”或扶贫热线电话
    HIGNER_DEPT_FOUND_FEEDBACK("04","上级扶贫部门发现反馈"),//上级扶贫部门发现反馈
    BELOW_DIVISION_DEPT_VISITED("05","处级以下挂钩帮扶责任人走访发现"),//南安市处级以下挂钩帮扶责任人走访发现
    BELOW_DEPT_VISITED("06","处级干部挂钩帮扶责任人走访发现");//南安市处级以下挂钩帮扶责任人走访发现

    private String dataSource;
    private String dataSourceName;

    private PPMDataSourceEnum(String dataSource, String dataSourceName) {
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
