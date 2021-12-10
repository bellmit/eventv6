package cn.ffcs.zhsq.reportFocus.reportMsgCCCfg.service.impl;

/**
 * @Description: 消息抄送类型枚举类
 * @ClassName:   ReportMsgCCTypeEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年9月27日 上午11:18:41
 */
public enum ReportMsgCCTypeEnum {
	CCTYPE_DISTRIBUTE("1", "分送"),
	CCTYPE_SELECT("2", "选送"),
	CCTYPE_MAIN("3", "主送");

	private String ccType;
	private String ccTypeName;
	
	private ReportMsgCCTypeEnum(String ccType, String ccTypeName) {
		this.ccType = ccType;
		this.ccTypeName = ccTypeName;
	}

	public String getCcType() {
		return ccType;
	}

	public String getCcTypeName() {
		return ccTypeName;
	}
}
