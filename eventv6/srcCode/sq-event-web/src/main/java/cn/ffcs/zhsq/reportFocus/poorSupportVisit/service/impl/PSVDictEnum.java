package cn.ffcs.zhsq.reportFocus.poorSupportVisit.service.impl;

/**
 * @Description: 扶贫走访(Poor Support Visit)相关字典
 * @ClassName:   PSVDictEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年12月4日 上午9:28:29
 */
public enum PSVDictEnum {
	REPORT_STATUS("B210007001", "上报状态"),
	COLLECT_SOURCE("B210007002", "采集来源"),
	DIFFICULTY_TYPE("B210007003", "困难类型");
	
	private String dictCode;
	private String dictName;
	
	private PSVDictEnum(String dictCode, String dictName) {
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
