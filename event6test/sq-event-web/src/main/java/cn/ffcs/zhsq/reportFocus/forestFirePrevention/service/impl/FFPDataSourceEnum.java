package cn.ffcs.zhsq.reportFocus.forestFirePrevention.service.impl;

/**
 * @Description: 森林防灭火数据来源枚举类
 * @ClassName:   FFPDataSourceEnum   
 * @author:      张联松(zhangls)
 * @date:        2021年3月31日 下午4:34:34
 */
public enum FFPDataSourceEnum {
	GRID_INSPECT("01", "网格员巡查发现"),
	FOREST_RANGER("02", "护林员巡查发现"),
	COMMUNITY_REPORT("03", "驻村工作队（一级网格员）巡查发现"),
	PLATFORM_12345("04", "12345便民服务平台受理登记"),
	MASSES_REPORT("05", "群众举报"),
	MONITOR_EQUIPMENT("06", "智能监控设备预警");
	
	private String dataSource;
	private String dataSourceName;
	
	private FFPDataSourceEnum(String dataSource, String dataSourceName) {
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
