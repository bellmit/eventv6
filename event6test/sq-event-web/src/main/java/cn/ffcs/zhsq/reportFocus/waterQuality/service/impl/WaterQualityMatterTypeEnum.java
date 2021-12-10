package cn.ffcs.zhsq.reportFocus.waterQuality.service.impl;

/**
 * @Description: 流域水质数据来源枚举类
 * @ClassName:   WaterQualityMatterTypeEnum   
 * @author:      张联松(zhangls)
 * @date:        2020年11月16日 下午7:21:20
 */
public enum WaterQualityMatterTypeEnum {
	COMMON("1", "一般流域水质问题"),
	SUDDEN("2", "突发流域水质问题");
	
	private String matterType;
	private String matterTypeName;
	
	private WaterQualityMatterTypeEnum(String matterType, String matterTypeName) {
		this.matterType = matterType;
		this.matterTypeName = matterTypeName;
	}

	public String getMatterType() {
		return matterType;
	}
	
	public String getMatterTypeName() {
		return matterTypeName;
	}

	public String toString() {
		return matterType;
	}
}
