package cn.ffcs.zhsq.mybatis.domain.historyInfo;

import java.io.Serializable;

/**
 * 路灯
 * @author zhangzhenhai
 * @date 2017-10-23 上午9:33:43
 */
public class StreetlightHis implements Serializable {

	private String lighting_status;//照明状态 0：开，1：关
	private String brightness;//亮度 百分比
	private String voltage;//电压 V
	private String electricity;//电流
	private String recordtime;//记录时间:yyyy-MM-dd HH:mm:ss
	public String getLighting_status() {
		return lighting_status;
	}
	public void setLighting_status(String lighting_status) {
		this.lighting_status = lighting_status;
	}
	public String getBrightness() {
		return brightness;
	}
	public void setBrightness(String brightness) {
		this.brightness = brightness;
	}
	public String getVoltage() {
		return voltage;
	}
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}
	public String getElectricity() {
		return electricity;
	}
	public void setElectricity(String electricity) {
		this.electricity = electricity;
	}
	public String getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}
	
	
}
