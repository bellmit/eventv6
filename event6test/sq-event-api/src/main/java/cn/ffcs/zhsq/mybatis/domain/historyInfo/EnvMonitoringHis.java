package cn.ffcs.zhsq.mybatis.domain.historyInfo;

import java.io.Serializable;

/**
 * 环境监控
 * @author zhangzhenhai
 * @date 2017-10-23 上午9:53:00
 */
public class EnvMonitoringHis implements Serializable {

	private String pm25;//PM2.5
	private String temperature;//温度
	private String humidity;//湿度
	private String noise;//噪音
	private String recordtime;//记录时间:yyyy-MM-dd HH:mm:ss
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getNoise() {
		return noise;
	}
	public void setNoise(String noise) {
		this.noise = noise;
	}
	public String getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}
	
	
}
