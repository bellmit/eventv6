package cn.ffcs.zhsq.reportFocus.twoViolationPre.service.impl;

/**
 * @Description: 两违防治相关字典
 * @ClassName:   TwoVioPreDictEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年9月24日 下午8:27:00
 */
public enum TwoVioPreDictEnum {
	CONSTRUCTION_STATUS("B210001001", "建设状态"),
	BUILDING_USAGE("B210001002", "建筑用途"),
	REPORT_STATUS("B210001003", "上报状态"),
	REPORT_WAY("B210001004", "上报方式"),
	DISPOSAL_SITUATION("B210001005", "处置情况"),
	FIELD_SITUATION("B210001006", "实地情况"),
	IDENTIFICATION_RESULT("B210001007", "认定结果"),
	DATA_SOURCE("B210001008", "发现来源");

	private String dictCode;
	private String dictName;
	
	private TwoVioPreDictEnum(String dictCode, String dictName) {
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
