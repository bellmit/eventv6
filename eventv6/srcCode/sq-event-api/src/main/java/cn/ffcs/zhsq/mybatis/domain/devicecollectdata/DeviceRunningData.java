package cn.ffcs.zhsq.mybatis.domain.devicecollectdata;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 设备运行数据表模块bo对象
 * @Author: husp
 * @Date: 11-28 08:49:19
 * @Copyright: 2017 福富软件
 */
public class DeviceRunningData implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long runId; //运行数据ID
	private String deviceServiceId; //业务标识（场地、点位或设备等业务标识，父级别）
	private String deviceServiceType;//类型（场地、点位或设备类型，为T_DEVICE_INFO的DEVICE_TYPE）
	private String deviceId; //设备ID(场地、点位对应下的设备，为T_DEVICE_INFO的DEVICE_SERVICE_ID，子级别)
	private String deviceName;//设备名称
	private String deviceType;//设备类型
	private String runningDataJsonStr; //设备运行数据json串
	private String alertDataJsonStr; //设备告警数据json串
	private String alertFlag; //告警标记，0：不告警；1：告警；2：异常
	private String status; //状态，0：删除；1：有效；
	private Date collectTime; //设备采集时间
	private String bizType; //业务类型，00：马尾智慧社区物联网设备
	private Date created; //创建时间
	private Date updated; //修改时间


	public Long getRunId() {
		return runId;
	}
	public void setRunId(Long runId) {
		this.runId = runId;
	}
	public String getDeviceServiceId() {
		return deviceServiceId;
	}
	public void setDeviceServiceId(String deviceServiceId) {
		this.deviceServiceId = deviceServiceId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getRunningDataJsonStr() {
		return runningDataJsonStr;
	}
	public void setRunningDataJsonStr(String runningDataJsonStr) {
		this.runningDataJsonStr = runningDataJsonStr;
	}
	public String getAlertDataJsonStr() {
		return alertDataJsonStr;
	}
	public void setAlertDataJsonStr(String alertDataJsonStr) {
		this.alertDataJsonStr = alertDataJsonStr;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getAlertFlag() {
		return alertFlag;
	}
	public void setAlertFlag(String alertFlag) {
		this.alertFlag = alertFlag;
	}
	public String getDeviceServiceType() {
		return deviceServiceType;
	}
	public void setDeviceServiceType(String deviceServiceType) {
		this.deviceServiceType = deviceServiceType;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

}