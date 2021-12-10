package cn.ffcs.zhsq.reportFocus.petitionPerson.impl;

public enum PetPerDataSourceEnum {
	GRID_INSPECT("01", "网格员巡查发现"),
	PUBLIC_SECURITY("02", "公安指挥中心推送"),
	OFFICE_SPECIALIST("03", "信访部门交办");
	
	private String dataSource;
	private String dataSourceName;
	
	private PetPerDataSourceEnum(String dataSource, String dataSourceName) {
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
		PetPerDataSourceEnum[] businessModeEnums = values();  
        for (PetPerDataSourceEnum businessModeEnum : businessModeEnums) {  
            if (businessModeEnum.getDataSource().equals(value)) {  
                return businessModeEnum.getDataSourceName();  
            }  
        }  
        return null;  
    }   
}
