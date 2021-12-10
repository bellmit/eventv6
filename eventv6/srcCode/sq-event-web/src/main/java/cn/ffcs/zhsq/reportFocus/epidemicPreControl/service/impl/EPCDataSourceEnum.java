package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

/**
 * @Description: 疫情防控数据来源枚举类
 * @ClassName:   EPCDataSourceEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月12日 下午3:07:58
 */
public enum EPCDataSourceEnum {
	GRID_INSPECT("01", "网格员巡查发现"),
	PLATFORM_12345("02", "12345便民服务平台受理登记"),
	MASSES_REPORT("03", "群众举报"),
	POLICE_TRANSFER("04", "公安移送"),
	SUPERIOR_ASSIGN("05", "上级交办"),
	COUNTY_ASSIST("06", "其他县市指挥部协查信息");
	
	private String dataSource;
	private String dataSourceName;
	
	private EPCDataSourceEnum(String dataSource, String dataSourceName) {
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
