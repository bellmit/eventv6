package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

/**
 * @Description: 疫情防控(Epidemic Prevention and Control)
 * @ClassName:   EPCDictEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月11日 下午2:18:18
 */
public enum EPCDictEnum {
	REPORT_STATUS("B210003001", "上报状态"),
	KPC_TYPE("B210003002", "管控类型"),
	DISPOSAL_MODE("B210003003", "管控措施"),
	DATA_SOURCE("B210003004", "数据来源"),
	KPC_GOAL("B210003005", "入南目的"),
	KPC_SITUATION("B210003006", "病人情况"),
	TRAFFIC_MODE("B210003007", "交通方式"),
	DATA_S("B210003008", "采集来源"),
	KPC_CARD_TYPE("D030001", "证件类型"),
	KPC_ORIGIN("D033004", "来自何国");
	
	private String dictCode;
	private String dictName;
	
	private EPCDictEnum(String dictCode, String dictName) {
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
