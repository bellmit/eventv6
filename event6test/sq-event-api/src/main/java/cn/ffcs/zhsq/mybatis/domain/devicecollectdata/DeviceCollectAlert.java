package cn.ffcs.zhsq.mybatis.domain.devicecollectdata;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 设备采集告警数据模块bo对象
 * @Author: husp
 * @Date: 10-13 09:04:53
 * @Copyright: 2017 福富软件
 */
public class DeviceCollectAlert implements Serializable {
	private static final long serialVersionUID = 1L;
	//接口传入参数
	private String appKey;//验证Key
	private String sign;//验证签名
	private String timestamp;//日期验证是否过期  yyyyMMddHHmm
	private String deviceServiceId; //设备业务标识
	private String collectAlertJsonStr; //设备采集告警内容，json串
	private String bizType; //业务类型，00：马尾智慧社区物联网设备
	private String collTime; //设备采集时间
	
	//非接口传入参数
	private Long collectAlertId; //采集告警数据ID
	private String status; //状态，0：删除；1：有效；
	private Date collectTime; //设备采集时间
	private Date created; //创建时间
	private Date updated; //修改时间
	
	private String alertFlag;//'告警标记，0：不告警；1：告警


	public Long getCollectAlertId() {
		return collectAlertId;
	}
	public void setCollectAlertId(Long collectAlertId) {
		this.collectAlertId = collectAlertId;
	}

	public String getCollectAlertJsonStr() {
		return collectAlertJsonStr;
	}
	public void setCollectAlertJsonStr(String collectAlertJsonStr) {
		this.collectAlertJsonStr = collectAlertJsonStr;
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
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getCollTime() {
		return collTime;
	}
	public void setCollTime(String collTime) {
		this.collTime = collTime;
	}
	public String getDeviceServiceId() {
		return deviceServiceId;
	}
	public void setDeviceServiceId(String deviceServiceId) {
		this.deviceServiceId = deviceServiceId;
	}
	public String getAlertFlag() {
		return alertFlag;
	}
	public void setAlertFlag(String alertFlag) {
		this.alertFlag = alertFlag;
	}
}