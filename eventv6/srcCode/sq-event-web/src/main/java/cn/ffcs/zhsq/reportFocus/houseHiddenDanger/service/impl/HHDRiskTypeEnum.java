package cn.ffcs.zhsq.reportFocus.houseHiddenDanger.service.impl;

/**
 * @Description: 房屋安全隐患(House Hidden Danger)相关字典
 * @ClassName:   HHDDictEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月17日 下午5:11:34
 */
public enum HHDRiskTypeEnum {
	INIT("0", "暂无隐患"),
	COMMON("1", "一般安全隐患"),
	GREAT("2", "重大安全隐患"),
	SUDDEN("3", "突发安全隐患");
	
	private String riskType;
	private String riskTypeName;
	
	private HHDRiskTypeEnum(String riskType, String riskTypeName) {
		this.riskType = riskType;
		this.riskTypeName = riskTypeName;
	}
	
	public String getRiskType() {
		return riskType;
	}
	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}
	public String getRiskTypeName() {
		return riskTypeName;
	}
	public void setRiskTypeName(String riskTypeName) {
		this.riskTypeName = riskTypeName;
	}
	
	public String toString() {
		return this.riskType;
	}
	
}
