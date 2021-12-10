package cn.ffcs.zhsq.reportFocus.ruralHousing.service.impl;

/**
 * @Description: 农村建设相关字典
 * @ClassName:   RuralHousingDictEnum   
 * @author:      张联松(zhangls)
 * @date:        2021年3月8日 下午5:49:34
 */
public enum RuralHousingDictEnum {
	REPORT_STATUS("B210008001", "上报状态"),
	RH_CARD_TYPE("D030001", "证件类型");
	
	private String dictCode;
	private String dictName;
	
	private RuralHousingDictEnum(String dictCode, String dictName) {
		this.dictCode = dictCode;
		this.dictName = dictName;
	}
	
	public String getDictCode() {
		return dictCode;
	}
	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	public String getDictName() {
		return dictName;
	}
	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
	
	public String toString() {
		return this.dictCode;
	}
	
}
