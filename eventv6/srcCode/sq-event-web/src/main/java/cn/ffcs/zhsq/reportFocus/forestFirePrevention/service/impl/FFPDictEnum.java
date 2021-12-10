package cn.ffcs.zhsq.reportFocus.forestFirePrevention.service.impl;

/**
 * @Description: 森林防灭火相关字典
 * @ClassName:   FFPDictEnum   
 * @author:      张联松(zhangls)
 * @date:        2021年3月31日 上午10:19:15
 */
public enum FFPDictEnum {
	REPORT_STATUS("B210009001", "上报状态"),
	DATA_SOURCE("B210009002", "数据来源"),
	COLLECT_SOURCE("B210009003", "采集来源"),
	DATA_S("B210009004", "发现来源");

	private String dictCode;
	private String dictName;
	
	private FFPDictEnum(String dictCode, String dictName) {
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
