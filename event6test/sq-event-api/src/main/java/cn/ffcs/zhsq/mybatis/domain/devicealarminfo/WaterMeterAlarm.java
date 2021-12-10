package cn.ffcs.zhsq.mybatis.domain.devicealarminfo;

import java.io.Serializable;

/**
 * 水表告警
 * @author zhangzhenhai
 * @date 2017-10-23 上午10:36:24
 */
public class WaterMeterAlarm implements Serializable {

	private String alarm_info;//告警信息
	private String device_type;//设备类型 1.大表，2.小表，3.消防栓
	private String recordtime;//记录时间:yyyy-MM-dd HH:mm:ss
	public String getAlarm_info() {
		return alarm_info;
	}
	public void setAlarm_info(String alarm_info) {
		this.alarm_info = alarm_info;
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public String getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}
	
	
}
