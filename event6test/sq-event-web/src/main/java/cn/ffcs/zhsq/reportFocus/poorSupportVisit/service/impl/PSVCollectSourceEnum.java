package cn.ffcs.zhsq.reportFocus.poorSupportVisit.service.impl;

/**
 * @Description: 扶贫走访采集来源枚举类，字典编码B210007002
 * @ClassName:   PSVCollectSourceEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年12月4日 上午9:45:50
 */
public enum PSVCollectSourceEnum {
	GRID_REPORT("01", "二级网格长走访报告"),
	COMMUNITY_REPORT("02", "驻村工作队走访报告"),
	STREET_REPORT("03", "镇级挂钩帮扶责任人走访报告"),
	COUNTY_DEPARTMENT_REPORT("04", "市直单位（部门）挂钩帮扶责任人走访报告"),
	MUNICIPAL_REPORT("05", "泉州市级挂钩帮扶责任人走访报告");
	
	private String collectSource;
	private String collectSourceName;
	
	private PSVCollectSourceEnum(String collectSource, String collectSourceName) {
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
