package cn.ffcs.zhsq.reportFocus.epidemicPreControl.service.impl;

/**
 * @Description: 病人情况枚举类
 * @ClassName:   EPCKpcSituationEnum   
 * @author:      张联松(zhangls)
 * @date:        2021年1月24日 下午3:23:32
 */
public enum EPCKpcSituationEnum {
	EXCULDE("01", "排除新冠肺炎"),
	SUSPECT("02", "怀疑新冠肺炎");
	
	private String kpcSituation;
	private String kpcSituationName;
	
	private EPCKpcSituationEnum(String kpcSituation, String kpcSituationName) {
		this.kpcSituation = kpcSituation;
		this.kpcSituationName = kpcSituationName;
	}
	
	public String getKpcSituation() {
		return kpcSituation;
	}
	public void setKpcSituation(String kpcSituation) {
		this.kpcSituation = kpcSituation;
	}
	public String getKpcSituationName() {
		return kpcSituationName;
	}
	public void setKpcSituationName(String kpcSituationName) {
		this.kpcSituationName = kpcSituationName;
	}
	
	public String toString() {
		return this.kpcSituation;
	}
}
