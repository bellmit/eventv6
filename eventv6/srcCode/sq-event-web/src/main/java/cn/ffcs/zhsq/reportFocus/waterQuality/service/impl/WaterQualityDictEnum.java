package cn.ffcs.zhsq.reportFocus.waterQuality.service.impl;

/**
 * @Description: 流域水质字典枚举类
 * @ClassName:   WaterQualityDictEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月16日 下午4:56:26
 */
public enum WaterQualityDictEnum {
	REPORT_STATUS("B210005001", "上报状态"),
	MATTER_TYPE("B210005002", "问题类型"),
	ADMINISTRTIVE_SACTION("B210005003", "行政处罚"),
	DATA_SOURCE("B210005004", "发现来源");

	private String dictCode;
	private String dictName;
	
	private WaterQualityDictEnum(String dictCode, String dictName) {
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
