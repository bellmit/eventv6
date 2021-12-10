package cn.ffcs.zhsq.mybatis.domain.devicecollectdata;

import java.io.Serializable;


/**
 * @Description: 井盖关联子设备模块bo对象
 * @Author: luth
 * @Date: 10-24 08:55:35
 * @Copyright: 2017 福富软件
 */
public class PefDevice implements Serializable {


	private static final long serialVersionUID = -2017842963496313320L;
	private Long refDevId; //主键ID
	private String deviceServiceId; //主设备ID
	private String refDeviceServiceId; //子设备ID


	public Long getRefDevId() {
		return refDevId;
	}
	public void setRefDevId(Long refDevId) {
		this.refDevId = refDevId;
	}
	public String getDeviceServiceId() {
		return deviceServiceId;
	}
	public void setDeviceServiceId(String deviceServiceId) {
		this.deviceServiceId = deviceServiceId;
	}
	public String getRefDeviceServiceId() {
		return refDeviceServiceId;
	}
	public void setRefDeviceServiceId(String refDeviceServiceId) {
		this.refDeviceServiceId = refDeviceServiceId;
	}


}