package cn.ffcs.zhsq.reportFocus.businessProblem.service.impl;

public enum BusProDataSourceEnum {
	GRID_INSPECT("01", "网格员巡查发现"),
	OFFICIAL_ACCOUNT("02", "南安网格微信公众号自主报告"),
	ENTERPRISE_SPECIALIST("03", "企业营商环境专员报告"),
	ENTERPRISE_THREE("04", "企业三员报告"),
	MUNICIPAL_DEPARTMENTS("05", "市直部门（单位）反映"),
	INDUSTRY_ASSOCIATION("06", "行业协会报告"),
	CHAMBER_COMMERCE("07", "南安总商会、异地南安商会、市域内异地商会、镇级商会、同业公会报告"),
	PLATFORM_12345("08", "12345便民服务平台受理登记"),
	ENTERPRISE_TRAIN("09", "政企直通车平台受理登记");
	
	private String dataSource;
	private String dataSourceName;
	
	private BusProDataSourceEnum(String dataSource, String dataSourceName) {
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
	
	public static String getValue(String value) {  
		BusProDataSourceEnum[] businessModeEnums = values();  
        for (BusProDataSourceEnum businessModeEnum : businessModeEnums) {  
            if (businessModeEnum.getDataSource().equals(value)) {  
                return businessModeEnum.getDataSourceName();  
            }  
        }  
        return null;  
    }   
}
