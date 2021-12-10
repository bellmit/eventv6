package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

/**
 * @Description: 疫情防控采集来源枚举类
 * @ClassName:   EPCCollectSourceEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年12月7日 下午5:46:02
 */
public enum EPCCollectSourceEnum {
	GRID_INSPECT("01", "网格员巡查发现"),
	FOREIGN_AFFAIRS_SECTION("02", "市外事组推送"),
	EPIDEMINC_PRE_CONTROL("03", "市疫情防控组推送"),
	BIG_DATA_GROUP("04", "市大数据组推送"),
	SMALL_MEDICAL_INSTITUTION("05", "小型医疗机构"),
	BACK_HOME_INVISTIGATE("06", "返乡摸排");
	
	private String collectSource;
	private String collectSourceName;
	
	private EPCCollectSourceEnum(String collectSource, String collectSourceName) {
		this.collectSource = collectSource;
		this.collectSourceName = collectSourceName;
	}

	public String getCollectSource() {
		return collectSource;
	}
	
	public String getCollectSourceName() {
		return collectSourceName;
	}

	public String toString() {
		return collectSource;
	}
}
