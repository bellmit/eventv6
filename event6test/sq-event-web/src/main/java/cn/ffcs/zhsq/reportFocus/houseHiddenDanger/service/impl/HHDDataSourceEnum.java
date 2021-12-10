package cn.ffcs.zhsq.reportFocus.houseHiddenDanger.service.impl;

/**
 * @Description: 疫情防控数据来源枚举类
 * @ClassName:   EPCDataSourceEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月12日 下午3:07:58
 */
public enum HHDDataSourceEnum {
	GRID_INSPECT("01", "网格员巡查发现"),
	MASSES_REPORT("02", "群众举报"),
	SUPERIOR_ASSIGN("03", "上级交办"),
	PLATFORM_12345("04", "12345便民服务平台受理登记"),
	FIRE_APPROVAL("05", "消防手续审批"),
	ELECTRICITY_ABNORMAL("06", "用电异常发现"),
	BUSSINESS_LICENSE_PROCESS("07", "营业执照办理");
	
	private String dataSource;
	private String dataSourceName;
	
	private HHDDataSourceEnum(String dataSource, String dataSourceName) {
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
