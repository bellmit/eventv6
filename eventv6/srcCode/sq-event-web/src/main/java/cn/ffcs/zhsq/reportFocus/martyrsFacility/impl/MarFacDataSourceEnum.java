package cn.ffcs.zhsq.reportFocus.martyrsFacility.impl;

public enum MarFacDataSourceEnum {
	GRID_INSPECT("01", "网格员巡查发现"),
	PLATFORM_12345("02", "12345便民服务平台受理登记"),
	DEPARTMENT_INSPECTION("03", "部门检查发现交办"),
	TIP_OFFS("04", "群众举报"),
	NEWS_MEDIA("05", "新闻媒体曝光");
	
	private String dataSource;
	private String dataSourceName;
	
	private MarFacDataSourceEnum(String dataSource, String dataSourceName) {
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
		MarFacDataSourceEnum[] businessModeEnums = values();  
        for (MarFacDataSourceEnum businessModeEnum : businessModeEnums) {  
            if (businessModeEnum.getDataSource().equals(value)) {  
                return businessModeEnum.getDataSourceName();  
            }  
        }  
        return null;  
    }   
}
