package cn.ffcs.zhsq.mybatis.domain.historyInfo;

import java.io.Serializable;

/**
 * 大水表
 * @author zhangzhenhai
 * @date 2017-10-23 上午9:53:30
 */
public class BidWaterMeterHis implements Serializable {

	private String forward_flow;//正向流量
	private String reverse_flow;//反向流量
	private String pressure;//压力
	private String temperature;//温度
	private String signal_intensity;//信号强度
	private String recordtime;//记录时间:yyyy-MM-dd HH:mm:ss
	public String getForward_flow() {
		return forward_flow;
	}
	public void setForward_flow(String forward_flow) {
		this.forward_flow = forward_flow;
	}
	public String getReverse_flow() {
		return reverse_flow;
	}
	public void setReverse_flow(String reverse_flow) {
		this.reverse_flow = reverse_flow;
	}
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
