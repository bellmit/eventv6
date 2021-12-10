package cn.ffcs.zhsq.reportFocus.businessProblem.service.impl;

public enum BusProDictEnum {

	REPORT_STATUS("B210010001", "上报状态"),
	HANDLE_TYPE("B210010003", "处置情况-中期"),//预计限期内完成,预计延期完成,短期内无法解决,不符合规定
	HANDLE_TYPE_V2("B210010004","处置情况-期限内"),//已完成处置,申请延期,短期内无法解决,不符合规定
	DATA_SOURCE("B210010005","发现来源");

	private String dictCode;
	private String dictName;
	
	private BusProDictEnum(String dictCode, String dictName) {
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
