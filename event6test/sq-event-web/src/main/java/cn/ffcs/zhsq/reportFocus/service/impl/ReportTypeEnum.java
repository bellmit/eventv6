package cn.ffcs.zhsq.reportFocus.service.impl;

import org.apache.commons.lang.StringUtils;

/**
 * @Description: 上报类型枚举类
 * @ClassName:   ReportTypeEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月6日 上午11:06:56
 */
public enum ReportTypeEnum {
	twoVioPre("1", "两违防治"),
	houseHiddenDanger("2", "房屋安全隐患"),
	enterpriseHiddenDanger("3", "企业安全隐患"),
	epidemicPreControl("4", "疫情防控"),
	waterQuality("5", "流域水质"),
	meetingAndLesson("6", "三会一课"),
	poorSupportVisit("7", "扶贫走访"),//两不愁三保障一饮水
	ruralHousing("8", "农村建房"),//一告知四到场
	forestFirePrevention("9", "森林防灭火"),
	businessProblem("10", "营商问题"),
	povertyPreMonitor("11", "致贫返贫监测"),
	petitionPerson("12", "信访人员稳控"),
	martyrsFacility("13", "烈士纪念设施"),
	environmentHealTreatment("14", "环境卫生问题处置"),
	threeOneTreatment("15", "三合一整治");

	private String reportTypeIndex;
	private String reportTypeName;
	
	private ReportTypeEnum(String reportTypeIndex, String reportTypeName) {
		this.reportTypeIndex = reportTypeIndex;
		this.reportTypeName = reportTypeName;
	}
	
	public String getReportTypeIndex() {
		return reportTypeIndex;
	}
	
	public String getReportTypeName() {
		return reportTypeName;
	}
	
	public String toString() {
		return reportTypeName;
	}

	public static String getReportTypeName(String reportTypeIndex){
		for(ReportTypeEnum reportTypeEnum : ReportTypeEnum.values()){
			if(StringUtils.equals(reportTypeIndex, reportTypeEnum.getReportTypeIndex())){
				return reportTypeEnum.getReportTypeName();
			}
		}
		return null;
	}
}
