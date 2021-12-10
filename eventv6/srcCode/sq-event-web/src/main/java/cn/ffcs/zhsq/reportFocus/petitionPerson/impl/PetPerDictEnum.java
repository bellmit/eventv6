package cn.ffcs.zhsq.reportFocus.petitionPerson.impl;

public enum PetPerDictEnum {

	REPORT_STATUS("B210012001", "上报状态"),//回笼处置,发现问题,任务派发,任务处置,情况反馈,办结销号
	CONTROL_TYPE("B210012002", "稳控类型"),//日常稳控,重要节点稳控
	PETITIONPARTY_TYPE("B210012003","信访人员类型"),//信访重点人员,信访关注人员
	CONTROL_RESULT("B210012004", "稳控结果"),//状态稳定,正在跟踪动态,正在化解稳控,稳控到位,不属信访关注人员
	FEEDBACK_RESULT("B210012005","反馈结果"),//状态稳定,不在视线范围,稳控到位
	DATA_SOURCE("B210012006","发现来源"),

	CERT_TYPE("D030001", "证件类型"),
	PARTY_TYPE("D069003", "人口类型"),
	PETITION_TYPE("A001130002", "上访类型");
	
	private String dictCode;
	private String dictName;
	
	private PetPerDictEnum(String dictCode, String dictName) {
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
