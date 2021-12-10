package cn.ffcs.zhsq.reportFocus.service.impl;

/**
 * @Description: 入格事件编号配置业务编码，字典编码：B582013
 * @ClassName:   ReportNumberCfgBizCodeEnum   
 * @author:      张联松(zhangls)
 * @date:        2021年1月4日 上午10:38:24
 */
public enum ReportNumberCfgBizCodeEnum {
	twoVioPre("1301", "两违防治"),
	houseHiddenDanger("1302", "房屋安全隐患"),
	enterpriseHiddenDanger("1303", "企业安全隐患"),
	epidemicPreControl("1304", "疫情防控"),
	waterQuality("1305", "流域水质"),
	meetingAndLesson("1306", "三会一课"),
	poorSupportVisit("1307", "扶贫走访"),//两不愁三保障一饮水
	ruralHousing("1308", "农村建房"),//一告知四到场
	forestFirePrevention("1309", "森林防灭火"),
	businessProblem("1310", "营商问题"),
	povertyPreMonitor("1311", "致贫返贫监测"),
	petitionPerson("1312", "信访人员稳控"),
	martyrsFacility("1313", "烈士纪念设施"),
	environmentHealTreatment("1314", "环境卫生问题处置"),
	threeOneTreatment("1315", "三合一整治");

	private String bizCode;
	private String bizName;
	
	private ReportNumberCfgBizCodeEnum(String bizCode, String bizName) {
		this.bizCode = bizCode;
		this.bizName = bizName;
	}
	
	public String getBizCode() {
		return bizCode;
	}
	
	public String toString() {
		return bizName;
	}
}
