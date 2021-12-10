package cn.ffcs.zhsq.reportFocus.enterpriseHiddenDanger.service.impl;

/**
 * @Description: 企业安全隐患相关字典
 * @ClassName:   EHDDictEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年9月24日 下午8:27:00
 */
public enum EHDDictEnum {
	REPORT_STATUS("B210002001", "上报状态"),
	HIDDEN_DANGER_TYPE("B210002002", "隐患类型"),
	ADMINISTRTIVE_SACTION("B210002003", "行政处罚"),
	DATA_SOURCE("B210002005", "发现来源");

	private String dictCode;
	private String dictName;
	
	private EHDDictEnum(String dictCode, String dictName) {
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
