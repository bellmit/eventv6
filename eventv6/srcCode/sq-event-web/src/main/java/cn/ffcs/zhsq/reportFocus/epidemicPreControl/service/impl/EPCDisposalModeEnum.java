package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

/**
 * @Description: 疫情防控重点人员处置方式枚举类，字典编码：B210003003
 * @ClassName:   EPCDisposalModeEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月14日 上午9:50:42
 */
public enum EPCDisposalModeEnum {
	INIT_MODE("00", "无"),
	CENTRALIZED_ISOLATION("01", "需集中隔离的重点管控人员"),
	//名称变更 20210916（5807861） 需居家隔离的重点管控人员-->需居家隔离或需居家健康监测的重点管控人员
	HOME_QUARANTINE("02", "需居家隔离或需居家健康监测的重点管控人员"),
	//名称变更 20210914 只需核酸检测的重点管控人员-->需居家健康监测并核酸检测的重点管控人员
	//名称变更 20210916（5807861） 需居家健康监测并核酸检测的重点管控人员-->只需核酸检测的重点管控人员
	NUCLEIC_ACID_DETECTION("03", "只需核酸检测的重点管控人员"),
	HEALTH_MONITOR("04", "需居家健康监测人员"),
	KEY_CONTROL_PERSON_UNTRUE("05", "重点管控人员无法核实"),
	KEY_CONTROL_PERSON_NOT_GRID("06", "重点管控人员不在本网格"),
	BELONG_STREET("07", "属于本乡镇"),
	NOT_STREET("08", "不在本乡镇"),
	BELONG_CITY("10", "属于本市"),
	NOT_CITY("11", "不在本市(归档)"),
	EXCLUDE_CONTROL("11", "排除管控对象(归档)");

	private String disposalMode;
	private String disposalModeName;
	
	private EPCDisposalModeEnum(String disposalMode, String disposalModeName) {
		this.disposalMode = disposalMode;
		this.disposalModeName = disposalModeName;
	}
	
	public String getDisposalMode() {
		return disposalMode;
	}
	
	public String getDisposalModeName() {
		return disposalModeName;
	}

	public String toString() {
		return disposalMode;
	}
}
