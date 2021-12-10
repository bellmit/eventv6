package cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.service.impl;

/**
 * @Description: 上报信息配置人员配置类型枚举类
 * @ClassName:   ReportMsgCfgTypeEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年9月26日 下午4:00:07
 */
public enum ReportMsgCfgTypeEnum {
	CFG_TYPE_USER("1", "用户"),
	CFG_TYPE_ORG("2", "组织"),
	CFG_TYPE_ROLE("3", "角色"),
	CFG_TYPE_POSITION("4", "职位"),
	CFG_TYPE_FUNCTION("5", "职能");
	
	private String cfgType;
	private String cfgTypeName;
	
	private ReportMsgCfgTypeEnum(String cfgType, String cfgTypeName) {
		this.cfgType = cfgType;
		this.cfgTypeName = cfgTypeName;
	}
	
	public String getCfgType() {
		return cfgType;
	}
	public String getCfgTypeName() {
		return cfgTypeName;
	}
	
}
