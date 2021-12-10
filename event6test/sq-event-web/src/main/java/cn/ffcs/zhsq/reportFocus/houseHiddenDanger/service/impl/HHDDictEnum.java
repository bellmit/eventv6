package cn.ffcs.zhsq.reportFocus.houseHiddenDanger.service.impl;

/**
 * @Description: 房屋安全隐患(House Hidden Danger)相关字典
 * @ClassName:   HHDDictEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月17日 下午5:11:34
 */
public enum HHDDictEnum {
	REPORT_STATUS("B210004001", "上报状态"),
	HIDDEN_DANGER_TYPE("B210004002", "隐患类型"), // 一般安全隐患、重大安全隐患、突发安全隐患
	HHD_TYPE("B210004003", "隐患说明"),//结构性开裂、沉降、倾斜、其他
	HANDLE_TYPE("B210004004", "处置类型"),//封房、拆除、加固、委托第三方结构安全性鉴定、经专家认定符合处置要求、签订片区改造协议、物理隔离
	HANDLE_TYPE_SUB("B210004007", "处置类型"),//拆除、加固、委托第三方结构安全性鉴定、经专家认定符合处置要求
	MANAGE_TYPE("B210004005", "处置类型"),//封房、拆除、加固、鉴定并符合结构安全要求、经专家认定符合处置要求、签订片区改造协议、物理隔离并确保安全
	MANAGE_TYPE_SUB("B210004008", "处置类型"),//拆除、加固、鉴定并符合结构安全要求、经专家认定符合处置要求
	HIDDEN_DANGER_STATUS("B210004006", "隐患状态"),
	DATA_SOURCE("B210004009", "发现来源");

	private String dictCode;
	private String dictName;
	
	private HHDDictEnum(String dictCode, String dictName) {
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
