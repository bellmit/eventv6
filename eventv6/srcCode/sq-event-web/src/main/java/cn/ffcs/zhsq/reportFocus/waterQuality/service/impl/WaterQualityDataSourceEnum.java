package cn.ffcs.zhsq.reportFocus.waterQuality.service.impl;

/**
 * @Description: 流域水质数据来源枚举类
 * @ClassName:   WaterQualityDataSourceEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月16日 下午7:21:20
 */
public enum WaterQualityDataSourceEnum {
	GRID_INSPECT("01", "网格员巡查发现"),
	PLATFORM_12345("02", "12345便民服务平台受理登记"),
	MASSES_REPORT("03", "群众举报"),
	SUPERIOR_ASSIGN("04", "上级交办"),
	ONLINE_MONITOR("05", "在线监控发现");
	
	private String dataSource;
	private String dataSourceName;
	
	private WaterQualityDataSourceEnum(String dataSource, String dataSourceName) {
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
