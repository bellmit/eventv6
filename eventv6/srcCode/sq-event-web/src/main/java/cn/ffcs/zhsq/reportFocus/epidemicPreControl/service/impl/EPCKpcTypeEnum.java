package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

/**
 * 
 * @Description: 疫情防控重点人员管控类别枚举类
 * @ClassName:   EPCKpcTypeEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月14日 下午4:12:59
 */
public enum EPCKpcTypeEnum {
	IMMIGRATION_PERSONNEL("01", "入境人员"),
	//ILLEGAL_IMMIGRANTS("02", "非法入境人员"),//统一为入境人员 20210114
	KEY_AREA_RETURN("03", "重点地区返南人员"),
	CLOSE_CONTACTS("04", "密切接触者"),
	NOT_ISOLATED_HOME("05", "入境集中隔离14天后尚未居家隔离人员"),
	SECONDARY_ASYMPTOMATIC("06", "治疗出院后的新冠肺炎患者或无症状感染者"),
	OUTSIDE_PROVINCE("07", "省外入南人员"),
	OTHER_KEY_TYPE("99", "其他需重点管控人员");
	
	private String kpcType;
	private String kpcTypeName;
	
	private EPCKpcTypeEnum(String kpcType, String kpcTypeName) {
		this.kpcType = kpcType;
		this.kpcTypeName = kpcTypeName;
	}
	
	public String getKpcType() {
		return kpcType;
	}
	public void setKpcType(String kpcType) {
		this.kpcType = kpcType;
	}
	public String getKpcTypeName() {
		return kpcTypeName;
	}
	public void setKpcTypeName(String kpcTypeName) {
		this.kpcTypeName = kpcTypeName;
	}
	
	public String toString() {
		return this.kpcType;
	}
}
