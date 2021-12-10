package cn.ffcs.zhsq.mybatis.domain.devicealarminfo;

import java.io.Serializable;

/**
 * 井盖告警
 * @author zhangzhenhai
 * @date 2017-10-23 上午10:36:45
 */
public class ManholeCoverAlarm implements Serializable {

	private String device_status;//设备状态 0：开，1：关 
	private String recordtime;//记录时间:yyyy-MM-dd HH:mm:ss
	public String getDevice_status() {
		return device_status;
	}
	public void setDevice_status(String device_status) {
		this.device_status = device_status;
	}
	public String getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(String recordtime) {
		this.recordtime = recordtime;
	}
	
	
}
