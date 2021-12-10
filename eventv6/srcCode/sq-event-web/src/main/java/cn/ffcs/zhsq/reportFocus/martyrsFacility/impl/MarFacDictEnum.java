package cn.ffcs.zhsq.reportFocus.martyrsFacility.impl;

public enum MarFacDictEnum {

	REPORT_STATUS("B210013001", "上报状态"),//回笼处置,发现问题,任务派发,任务处置,情况反馈,办结销号
	DAMAGE_MODE("B210013002", "损坏方式"),//人为损坏、自然损坏
	FEEDBACK_TYPE("B210013003","反馈情况"),//不属实、已完成处置、处置难度较大
	DO_TYPE("B210013004", "处置结果"),//已处置完毕、处置难度较大
	ACCEPTANCE_RESULT("B210013005","验收结果"),//验收通过、验收不通过
	DATA_SOURCE("B210013006","发现来源");

	private String dictCode;
	private String dictName;
	
	private MarFacDictEnum(String dictCode, String dictName) {
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
