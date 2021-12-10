package cn.ffcs.zhsq.mybatis.domain.historyInfo;

import java.io.Serializable;

/**
 * 消防栓
 * @author zhangzhenhai
 * @date 2017-10-23 上午10:29:35
 */
public class FireHydrantHis implements Serializable {

	private String pressure;//压力
	private String temperature;//温度
	private String signal_intensity;//信号强度
	private String recordtime;//记录时间:yyyy-MM-dd HH:mm:ss
	public String getPressure() {
		return pressure;
	}
	public void setPressure(String pressure) {
		this.pressure = pressure;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getSignal_intensity() {
		return signal_intensity;
	}
	public void setSignal_intensity(String signal_intensity) {
		this.signal_intensity = signal_intensity;
	}
	public String getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}
	
	
}
